package okhttp3.internal.ws;

import java.io.Closeable;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.connection.StreamAllocation;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;

public final class RealWebSocket implements WebSocket, WebSocketReader.FrameCallback {
  private static final long CANCEL_AFTER_CLOSE_MILLIS = 60000L;
  
  private static final long MAX_QUEUE_SIZE = 16777216L;
  
  private static final List<Protocol> ONLY_HTTP1 = Collections.singletonList(Protocol.HTTP_1_1);
  
  private Call call;
  
  private ScheduledFuture<?> cancelFuture;
  
  private boolean enqueuedClose;
  
  private ScheduledExecutorService executor;
  
  private boolean failed;
  
  private final String key;
  
  final WebSocketListener listener;
  
  private final ArrayDeque<Object> messageAndCloseQueue = new ArrayDeque();
  
  private final Request originalRequest;
  
  int pingCount;
  
  int pongCount;
  
  private final ArrayDeque<ByteString> pongQueue = new ArrayDeque<ByteString>();
  
  private long queueSize;
  
  private final Random random;
  
  private WebSocketReader reader;
  
  private int receivedCloseCode = -1;
  
  private String receivedCloseReason;
  
  private Streams streams;
  
  private WebSocketWriter writer;
  
  private final Runnable writerRunnable;
  
  public RealWebSocket(Request paramRequest, WebSocketListener paramWebSocketListener, Random paramRandom) {
    if (!"GET".equals(paramRequest.method()))
      throw new IllegalArgumentException("Request must be GET: " + paramRequest.method()); 
    this.originalRequest = paramRequest;
    this.listener = paramWebSocketListener;
    this.random = paramRandom;
    byte[] arrayOfByte = new byte[16];
    paramRandom.nextBytes(arrayOfByte);
    this.key = ByteString.of(arrayOfByte).base64();
    this.writerRunnable = new Runnable() {
        public void run() {
          try {
            boolean bool;
            do {
              bool = RealWebSocket.this.writeOneFrame();
            } while (bool);
          } catch (IOException iOException) {
            RealWebSocket.this.failWebSocket(iOException, null);
          } 
        }
      };
  }
  
  private void runWriter() {
    assert Thread.holdsLock(this);
    if (this.executor != null)
      this.executor.execute(this.writerRunnable); 
  }
  
  private boolean send(ByteString paramByteString, int paramInt) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: aload_0
    //   3: monitorenter
    //   4: iload_3
    //   5: istore #4
    //   7: aload_0
    //   8: getfield failed : Z
    //   11: ifne -> 28
    //   14: aload_0
    //   15: getfield enqueuedClose : Z
    //   18: istore #4
    //   20: iload #4
    //   22: ifeq -> 33
    //   25: iload_3
    //   26: istore #4
    //   28: aload_0
    //   29: monitorexit
    //   30: iload #4
    //   32: ireturn
    //   33: aload_0
    //   34: getfield queueSize : J
    //   37: aload_1
    //   38: invokevirtual size : ()I
    //   41: i2l
    //   42: ladd
    //   43: ldc2_w 16777216
    //   46: lcmp
    //   47: ifle -> 70
    //   50: aload_0
    //   51: sipush #1001
    //   54: aconst_null
    //   55: invokevirtual close : (ILjava/lang/String;)Z
    //   58: pop
    //   59: iload_3
    //   60: istore #4
    //   62: goto -> 28
    //   65: astore_1
    //   66: aload_0
    //   67: monitorexit
    //   68: aload_1
    //   69: athrow
    //   70: aload_0
    //   71: aload_0
    //   72: getfield queueSize : J
    //   75: aload_1
    //   76: invokevirtual size : ()I
    //   79: i2l
    //   80: ladd
    //   81: putfield queueSize : J
    //   84: aload_0
    //   85: getfield messageAndCloseQueue : Ljava/util/ArrayDeque;
    //   88: astore #5
    //   90: new okhttp3/internal/ws/RealWebSocket$Message
    //   93: astore #6
    //   95: aload #6
    //   97: iload_2
    //   98: aload_1
    //   99: invokespecial <init> : (ILokio/ByteString;)V
    //   102: aload #5
    //   104: aload #6
    //   106: invokevirtual add : (Ljava/lang/Object;)Z
    //   109: pop
    //   110: aload_0
    //   111: invokespecial runWriter : ()V
    //   114: iconst_1
    //   115: istore #4
    //   117: goto -> 28
    // Exception table:
    //   from	to	target	type
    //   7	20	65	finally
    //   33	59	65	finally
    //   70	114	65	finally
  }
  
  void awaitTermination(int paramInt, TimeUnit paramTimeUnit) throws InterruptedException {
    this.executor.awaitTermination(paramInt, paramTimeUnit);
  }
  
  public void cancel() {
    this.call.cancel();
  }
  
  void checkResponse(Response paramResponse) throws ProtocolException {
    if (paramResponse.code() != 101)
      throw new ProtocolException("Expected HTTP 101 response but was '" + paramResponse.code() + " " + paramResponse.message() + "'"); 
    String str2 = paramResponse.header("Connection");
    if (!"Upgrade".equalsIgnoreCase(str2))
      throw new ProtocolException("Expected 'Connection' header value 'Upgrade' but was '" + str2 + "'"); 
    str2 = paramResponse.header("Upgrade");
    if (!"websocket".equalsIgnoreCase(str2))
      throw new ProtocolException("Expected 'Upgrade' header value 'websocket' but was '" + str2 + "'"); 
    str2 = paramResponse.header("Sec-WebSocket-Accept");
    String str1 = ByteString.encodeUtf8(this.key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").sha1().base64();
    if (!str1.equals(str2))
      throw new ProtocolException("Expected 'Sec-WebSocket-Accept' header value '" + str1 + "' but was '" + str2 + "'"); 
  }
  
  public boolean close(int paramInt, String paramString) {
    return close(paramInt, paramString, 60000L);
  }
  
  boolean close(int paramInt, String paramString, long paramLong) {
    // Byte code:
    //   0: iconst_1
    //   1: istore #5
    //   3: aload_0
    //   4: monitorenter
    //   5: iload_1
    //   6: invokestatic validateCloseCode : (I)V
    //   9: aconst_null
    //   10: astore #6
    //   12: aload_2
    //   13: ifnull -> 82
    //   16: aload_2
    //   17: invokestatic encodeUtf8 : (Ljava/lang/String;)Lokio/ByteString;
    //   20: astore #7
    //   22: aload #7
    //   24: astore #6
    //   26: aload #7
    //   28: invokevirtual size : ()I
    //   31: i2l
    //   32: ldc2_w 123
    //   35: lcmp
    //   36: ifle -> 82
    //   39: new java/lang/IllegalArgumentException
    //   42: astore #6
    //   44: new java/lang/StringBuilder
    //   47: astore #7
    //   49: aload #7
    //   51: invokespecial <init> : ()V
    //   54: aload #6
    //   56: aload #7
    //   58: ldc_w 'reason.size() > 123: '
    //   61: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   64: aload_2
    //   65: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   68: invokevirtual toString : ()Ljava/lang/String;
    //   71: invokespecial <init> : (Ljava/lang/String;)V
    //   74: aload #6
    //   76: athrow
    //   77: astore_2
    //   78: aload_0
    //   79: monitorexit
    //   80: aload_2
    //   81: athrow
    //   82: aload_0
    //   83: getfield failed : Z
    //   86: ifne -> 100
    //   89: aload_0
    //   90: getfield enqueuedClose : Z
    //   93: istore #8
    //   95: iload #8
    //   97: ifeq -> 108
    //   100: iconst_0
    //   101: istore #5
    //   103: aload_0
    //   104: monitorexit
    //   105: iload #5
    //   107: ireturn
    //   108: aload_0
    //   109: iconst_1
    //   110: putfield enqueuedClose : Z
    //   113: aload_0
    //   114: getfield messageAndCloseQueue : Ljava/util/ArrayDeque;
    //   117: astore #7
    //   119: new okhttp3/internal/ws/RealWebSocket$Close
    //   122: astore_2
    //   123: aload_2
    //   124: iload_1
    //   125: aload #6
    //   127: lload_3
    //   128: invokespecial <init> : (ILokio/ByteString;J)V
    //   131: aload #7
    //   133: aload_2
    //   134: invokevirtual add : (Ljava/lang/Object;)Z
    //   137: pop
    //   138: aload_0
    //   139: invokespecial runWriter : ()V
    //   142: goto -> 103
    // Exception table:
    //   from	to	target	type
    //   5	9	77	finally
    //   16	22	77	finally
    //   26	77	77	finally
    //   82	95	77	finally
    //   108	142	77	finally
  }
  
  public void connect(OkHttpClient paramOkHttpClient) {
    OkHttpClient okHttpClient = paramOkHttpClient.newBuilder().protocols(ONLY_HTTP1).build();
    final int pingIntervalMillis = okHttpClient.pingIntervalMillis();
    final Request request = this.originalRequest.newBuilder().header("Upgrade", "websocket").header("Connection", "Upgrade").header("Sec-WebSocket-Key", this.key).header("Sec-WebSocket-Version", "13").build();
    this.call = Internal.instance.newWebSocketCall(okHttpClient, request);
    this.call.enqueue(new Callback() {
          public void onFailure(Call param1Call, IOException param1IOException) {
            RealWebSocket.this.failWebSocket(param1IOException, null);
          }
          
          public void onResponse(Call param1Call, Response param1Response) {
            String str;
            try {
              RealWebSocket.this.checkResponse(param1Response);
              StreamAllocation streamAllocation = Internal.instance.streamAllocation(param1Call);
              streamAllocation.noNewStreams();
              RealWebSocket.Streams streams = streamAllocation.connection().newWebSocketStreams(streamAllocation);
              try {
                RealWebSocket.this.listener.onOpen(RealWebSocket.this, param1Response);
                StringBuilder stringBuilder = new StringBuilder();
                this();
                str = stringBuilder.append("OkHttp WebSocket ").append(request.url().redact()).toString();
                RealWebSocket.this.initReaderAndWriter(str, pingIntervalMillis, streams);
                streamAllocation.connection().socket().setSoTimeout(0);
                RealWebSocket.this.loopReader();
              } catch (Exception exception) {
                RealWebSocket.this.failWebSocket(exception, null);
              } 
            } catch (ProtocolException protocolException) {
              RealWebSocket.this.failWebSocket(protocolException, (Response)str);
              Util.closeQuietly((Closeable)str);
            } 
          }
        });
  }
  
  public void failWebSocket(Exception paramException, Response paramResponse) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield failed : Z
    //   6: ifeq -> 12
    //   9: aload_0
    //   10: monitorexit
    //   11: return
    //   12: aload_0
    //   13: iconst_1
    //   14: putfield failed : Z
    //   17: aload_0
    //   18: getfield streams : Lokhttp3/internal/ws/RealWebSocket$Streams;
    //   21: astore_3
    //   22: aload_0
    //   23: aconst_null
    //   24: putfield streams : Lokhttp3/internal/ws/RealWebSocket$Streams;
    //   27: aload_0
    //   28: getfield cancelFuture : Ljava/util/concurrent/ScheduledFuture;
    //   31: ifnull -> 45
    //   34: aload_0
    //   35: getfield cancelFuture : Ljava/util/concurrent/ScheduledFuture;
    //   38: iconst_0
    //   39: invokeinterface cancel : (Z)Z
    //   44: pop
    //   45: aload_0
    //   46: getfield executor : Ljava/util/concurrent/ScheduledExecutorService;
    //   49: ifnull -> 61
    //   52: aload_0
    //   53: getfield executor : Ljava/util/concurrent/ScheduledExecutorService;
    //   56: invokeinterface shutdown : ()V
    //   61: aload_0
    //   62: monitorexit
    //   63: aload_0
    //   64: getfield listener : Lokhttp3/WebSocketListener;
    //   67: aload_0
    //   68: aload_1
    //   69: aload_2
    //   70: invokevirtual onFailure : (Lokhttp3/WebSocket;Ljava/lang/Throwable;Lokhttp3/Response;)V
    //   73: aload_3
    //   74: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   77: goto -> 11
    //   80: astore_1
    //   81: aload_0
    //   82: monitorexit
    //   83: aload_1
    //   84: athrow
    //   85: astore_1
    //   86: aload_3
    //   87: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   90: aload_1
    //   91: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	80	finally
    //   12	45	80	finally
    //   45	61	80	finally
    //   61	63	80	finally
    //   63	73	85	finally
    //   81	83	80	finally
  }
  
  public void initReaderAndWriter(String paramString, long paramLong, Streams paramStreams) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload #4
    //   5: putfield streams : Lokhttp3/internal/ws/RealWebSocket$Streams;
    //   8: new okhttp3/internal/ws/WebSocketWriter
    //   11: astore #5
    //   13: aload #5
    //   15: aload #4
    //   17: getfield client : Z
    //   20: aload #4
    //   22: getfield sink : Lokio/BufferedSink;
    //   25: aload_0
    //   26: getfield random : Ljava/util/Random;
    //   29: invokespecial <init> : (ZLokio/BufferedSink;Ljava/util/Random;)V
    //   32: aload_0
    //   33: aload #5
    //   35: putfield writer : Lokhttp3/internal/ws/WebSocketWriter;
    //   38: new java/util/concurrent/ScheduledThreadPoolExecutor
    //   41: astore #5
    //   43: aload #5
    //   45: iconst_1
    //   46: aload_1
    //   47: iconst_0
    //   48: invokestatic threadFactory : (Ljava/lang/String;Z)Ljava/util/concurrent/ThreadFactory;
    //   51: invokespecial <init> : (ILjava/util/concurrent/ThreadFactory;)V
    //   54: aload_0
    //   55: aload #5
    //   57: putfield executor : Ljava/util/concurrent/ScheduledExecutorService;
    //   60: lload_2
    //   61: lconst_0
    //   62: lcmp
    //   63: ifeq -> 96
    //   66: aload_0
    //   67: getfield executor : Ljava/util/concurrent/ScheduledExecutorService;
    //   70: astore_1
    //   71: new okhttp3/internal/ws/RealWebSocket$PingRunnable
    //   74: astore #5
    //   76: aload #5
    //   78: aload_0
    //   79: invokespecial <init> : (Lokhttp3/internal/ws/RealWebSocket;)V
    //   82: aload_1
    //   83: aload #5
    //   85: lload_2
    //   86: lload_2
    //   87: getstatic java/util/concurrent/TimeUnit.MILLISECONDS : Ljava/util/concurrent/TimeUnit;
    //   90: invokeinterface scheduleAtFixedRate : (Ljava/lang/Runnable;JJLjava/util/concurrent/TimeUnit;)Ljava/util/concurrent/ScheduledFuture;
    //   95: pop
    //   96: aload_0
    //   97: getfield messageAndCloseQueue : Ljava/util/ArrayDeque;
    //   100: invokevirtual isEmpty : ()Z
    //   103: ifne -> 110
    //   106: aload_0
    //   107: invokespecial runWriter : ()V
    //   110: aload_0
    //   111: monitorexit
    //   112: aload_0
    //   113: new okhttp3/internal/ws/WebSocketReader
    //   116: dup
    //   117: aload #4
    //   119: getfield client : Z
    //   122: aload #4
    //   124: getfield source : Lokio/BufferedSource;
    //   127: aload_0
    //   128: invokespecial <init> : (ZLokio/BufferedSource;Lokhttp3/internal/ws/WebSocketReader$FrameCallback;)V
    //   131: putfield reader : Lokhttp3/internal/ws/WebSocketReader;
    //   134: return
    //   135: astore_1
    //   136: aload_0
    //   137: monitorexit
    //   138: aload_1
    //   139: athrow
    // Exception table:
    //   from	to	target	type
    //   2	60	135	finally
    //   66	96	135	finally
    //   96	110	135	finally
    //   110	112	135	finally
    //   136	138	135	finally
  }
  
  public void loopReader() throws IOException {
    while (this.receivedCloseCode == -1)
      this.reader.processNextFrame(); 
  }
  
  public void onReadClose(int paramInt, String paramString) {
    // Byte code:
    //   0: iload_1
    //   1: iconst_m1
    //   2: if_icmpne -> 13
    //   5: new java/lang/IllegalArgumentException
    //   8: dup
    //   9: invokespecial <init> : ()V
    //   12: athrow
    //   13: aconst_null
    //   14: astore_3
    //   15: aload_0
    //   16: monitorenter
    //   17: aload_0
    //   18: getfield receivedCloseCode : I
    //   21: iconst_m1
    //   22: if_icmpeq -> 43
    //   25: new java/lang/IllegalStateException
    //   28: astore_2
    //   29: aload_2
    //   30: ldc_w 'already closed'
    //   33: invokespecial <init> : (Ljava/lang/String;)V
    //   36: aload_2
    //   37: athrow
    //   38: astore_2
    //   39: aload_0
    //   40: monitorexit
    //   41: aload_2
    //   42: athrow
    //   43: aload_0
    //   44: iload_1
    //   45: putfield receivedCloseCode : I
    //   48: aload_0
    //   49: aload_2
    //   50: putfield receivedCloseReason : Ljava/lang/String;
    //   53: aload_3
    //   54: astore #4
    //   56: aload_0
    //   57: getfield enqueuedClose : Z
    //   60: ifeq -> 114
    //   63: aload_3
    //   64: astore #4
    //   66: aload_0
    //   67: getfield messageAndCloseQueue : Ljava/util/ArrayDeque;
    //   70: invokevirtual isEmpty : ()Z
    //   73: ifeq -> 114
    //   76: aload_0
    //   77: getfield streams : Lokhttp3/internal/ws/RealWebSocket$Streams;
    //   80: astore #4
    //   82: aload_0
    //   83: aconst_null
    //   84: putfield streams : Lokhttp3/internal/ws/RealWebSocket$Streams;
    //   87: aload_0
    //   88: getfield cancelFuture : Ljava/util/concurrent/ScheduledFuture;
    //   91: ifnull -> 105
    //   94: aload_0
    //   95: getfield cancelFuture : Ljava/util/concurrent/ScheduledFuture;
    //   98: iconst_0
    //   99: invokeinterface cancel : (Z)Z
    //   104: pop
    //   105: aload_0
    //   106: getfield executor : Ljava/util/concurrent/ScheduledExecutorService;
    //   109: invokeinterface shutdown : ()V
    //   114: aload_0
    //   115: monitorexit
    //   116: aload_0
    //   117: getfield listener : Lokhttp3/WebSocketListener;
    //   120: aload_0
    //   121: iload_1
    //   122: aload_2
    //   123: invokevirtual onClosing : (Lokhttp3/WebSocket;ILjava/lang/String;)V
    //   126: aload #4
    //   128: ifnull -> 141
    //   131: aload_0
    //   132: getfield listener : Lokhttp3/WebSocketListener;
    //   135: aload_0
    //   136: iload_1
    //   137: aload_2
    //   138: invokevirtual onClosed : (Lokhttp3/WebSocket;ILjava/lang/String;)V
    //   141: aload #4
    //   143: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   146: return
    //   147: astore_2
    //   148: aload #4
    //   150: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   153: aload_2
    //   154: athrow
    // Exception table:
    //   from	to	target	type
    //   17	38	38	finally
    //   39	41	38	finally
    //   43	53	38	finally
    //   56	63	38	finally
    //   66	105	38	finally
    //   105	114	38	finally
    //   114	116	38	finally
    //   116	126	147	finally
    //   131	141	147	finally
  }
  
  public void onReadMessage(String paramString) throws IOException {
    this.listener.onMessage(this, paramString);
  }
  
  public void onReadMessage(ByteString paramByteString) throws IOException {
    this.listener.onMessage(this, paramByteString);
  }
  
  public void onReadPing(ByteString paramByteString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield failed : Z
    //   6: ifne -> 28
    //   9: aload_0
    //   10: getfield enqueuedClose : Z
    //   13: ifeq -> 31
    //   16: aload_0
    //   17: getfield messageAndCloseQueue : Ljava/util/ArrayDeque;
    //   20: invokevirtual isEmpty : ()Z
    //   23: istore_2
    //   24: iload_2
    //   25: ifeq -> 31
    //   28: aload_0
    //   29: monitorexit
    //   30: return
    //   31: aload_0
    //   32: getfield pongQueue : Ljava/util/ArrayDeque;
    //   35: aload_1
    //   36: invokevirtual add : (Ljava/lang/Object;)Z
    //   39: pop
    //   40: aload_0
    //   41: invokespecial runWriter : ()V
    //   44: aload_0
    //   45: aload_0
    //   46: getfield pingCount : I
    //   49: iconst_1
    //   50: iadd
    //   51: putfield pingCount : I
    //   54: goto -> 28
    //   57: astore_1
    //   58: aload_0
    //   59: monitorexit
    //   60: aload_1
    //   61: athrow
    // Exception table:
    //   from	to	target	type
    //   2	24	57	finally
    //   31	54	57	finally
  }
  
  public void onReadPong(ByteString paramByteString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield pongCount : I
    //   7: iconst_1
    //   8: iadd
    //   9: putfield pongCount : I
    //   12: aload_0
    //   13: monitorexit
    //   14: return
    //   15: astore_1
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_1
    //   19: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	15	finally
  }
  
  int pingCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield pingCount : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  boolean pong(ByteString paramByteString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield failed : Z
    //   6: ifne -> 28
    //   9: aload_0
    //   10: getfield enqueuedClose : Z
    //   13: ifeq -> 34
    //   16: aload_0
    //   17: getfield messageAndCloseQueue : Ljava/util/ArrayDeque;
    //   20: invokevirtual isEmpty : ()Z
    //   23: istore_2
    //   24: iload_2
    //   25: ifeq -> 34
    //   28: iconst_0
    //   29: istore_2
    //   30: aload_0
    //   31: monitorexit
    //   32: iload_2
    //   33: ireturn
    //   34: aload_0
    //   35: getfield pongQueue : Ljava/util/ArrayDeque;
    //   38: aload_1
    //   39: invokevirtual add : (Ljava/lang/Object;)Z
    //   42: pop
    //   43: aload_0
    //   44: invokespecial runWriter : ()V
    //   47: iconst_1
    //   48: istore_2
    //   49: goto -> 30
    //   52: astore_1
    //   53: aload_0
    //   54: monitorexit
    //   55: aload_1
    //   56: athrow
    // Exception table:
    //   from	to	target	type
    //   2	24	52	finally
    //   34	47	52	finally
  }
  
  int pongCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield pongCount : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  boolean processNextFrame() throws IOException {
    boolean bool = false;
    try {
      this.reader.processNextFrame();
      int i = this.receivedCloseCode;
      if (i == -1)
        bool = true; 
    } catch (Exception exception) {
      failWebSocket(exception, null);
    } 
    return bool;
  }
  
  public long queueSize() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield queueSize : J
    //   6: lstore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: lload_1
    //   10: lreturn
    //   11: astore_3
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_3
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public Request request() {
    return this.originalRequest;
  }
  
  public boolean send(String paramString) {
    if (paramString == null)
      throw new NullPointerException("text == null"); 
    return send(ByteString.encodeUtf8(paramString), 1);
  }
  
  public boolean send(ByteString paramByteString) {
    if (paramByteString == null)
      throw new NullPointerException("bytes == null"); 
    return send(paramByteString, 2);
  }
  
  void tearDown() throws InterruptedException {
    if (this.cancelFuture != null)
      this.cancelFuture.cancel(false); 
    this.executor.shutdown();
    this.executor.awaitTermination(10L, TimeUnit.SECONDS);
  }
  
  boolean writeOneFrame() throws IOException {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aconst_null
    //   3: astore_2
    //   4: iconst_m1
    //   5: istore_3
    //   6: aconst_null
    //   7: astore #4
    //   9: aconst_null
    //   10: astore #5
    //   12: aload_0
    //   13: monitorenter
    //   14: aload_0
    //   15: getfield failed : Z
    //   18: ifeq -> 25
    //   21: aload_0
    //   22: monitorexit
    //   23: iload_1
    //   24: ireturn
    //   25: aload_0
    //   26: getfield writer : Lokhttp3/internal/ws/WebSocketWriter;
    //   29: astore #6
    //   31: aload_0
    //   32: getfield pongQueue : Ljava/util/ArrayDeque;
    //   35: invokevirtual poll : ()Ljava/lang/Object;
    //   38: checkcast okio/ByteString
    //   41: astore #7
    //   43: iload_3
    //   44: istore #8
    //   46: aload #4
    //   48: astore #9
    //   50: aload #5
    //   52: astore #10
    //   54: aload #7
    //   56: ifnonnull -> 117
    //   59: aload_0
    //   60: getfield messageAndCloseQueue : Ljava/util/ArrayDeque;
    //   63: invokevirtual poll : ()Ljava/lang/Object;
    //   66: astore #11
    //   68: aload #11
    //   70: instanceof okhttp3/internal/ws/RealWebSocket$Close
    //   73: ifeq -> 197
    //   76: aload_0
    //   77: getfield receivedCloseCode : I
    //   80: istore #8
    //   82: aload_0
    //   83: getfield receivedCloseReason : Ljava/lang/String;
    //   86: astore #9
    //   88: iload #8
    //   90: iconst_m1
    //   91: if_icmpeq -> 141
    //   94: aload_0
    //   95: getfield streams : Lokhttp3/internal/ws/RealWebSocket$Streams;
    //   98: astore #10
    //   100: aload_0
    //   101: aconst_null
    //   102: putfield streams : Lokhttp3/internal/ws/RealWebSocket$Streams;
    //   105: aload_0
    //   106: getfield executor : Ljava/util/concurrent/ScheduledExecutorService;
    //   109: invokeinterface shutdown : ()V
    //   114: aload #11
    //   116: astore_2
    //   117: aload_0
    //   118: monitorexit
    //   119: aload #7
    //   121: ifnull -> 221
    //   124: aload #6
    //   126: aload #7
    //   128: invokevirtual writePong : (Lokio/ByteString;)V
    //   131: iconst_1
    //   132: istore_1
    //   133: aload #10
    //   135: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   138: goto -> 23
    //   141: aload_0
    //   142: getfield executor : Ljava/util/concurrent/ScheduledExecutorService;
    //   145: astore_2
    //   146: new okhttp3/internal/ws/RealWebSocket$CancelRunnable
    //   149: astore #10
    //   151: aload #10
    //   153: aload_0
    //   154: invokespecial <init> : (Lokhttp3/internal/ws/RealWebSocket;)V
    //   157: aload_0
    //   158: aload_2
    //   159: aload #10
    //   161: aload #11
    //   163: checkcast okhttp3/internal/ws/RealWebSocket$Close
    //   166: getfield cancelAfterCloseMillis : J
    //   169: getstatic java/util/concurrent/TimeUnit.MILLISECONDS : Ljava/util/concurrent/TimeUnit;
    //   172: invokeinterface schedule : (Ljava/lang/Runnable;JLjava/util/concurrent/TimeUnit;)Ljava/util/concurrent/ScheduledFuture;
    //   177: putfield cancelFuture : Ljava/util/concurrent/ScheduledFuture;
    //   180: aload #11
    //   182: astore_2
    //   183: aload #5
    //   185: astore #10
    //   187: goto -> 117
    //   190: astore #10
    //   192: aload_0
    //   193: monitorexit
    //   194: aload #10
    //   196: athrow
    //   197: aload #11
    //   199: astore_2
    //   200: iload_3
    //   201: istore #8
    //   203: aload #4
    //   205: astore #9
    //   207: aload #5
    //   209: astore #10
    //   211: aload #11
    //   213: ifnonnull -> 117
    //   216: aload_0
    //   217: monitorexit
    //   218: goto -> 23
    //   221: aload_2
    //   222: instanceof okhttp3/internal/ws/RealWebSocket$Message
    //   225: ifeq -> 309
    //   228: aload_2
    //   229: checkcast okhttp3/internal/ws/RealWebSocket$Message
    //   232: getfield data : Lokio/ByteString;
    //   235: astore #9
    //   237: aload #6
    //   239: aload_2
    //   240: checkcast okhttp3/internal/ws/RealWebSocket$Message
    //   243: getfield formatOpcode : I
    //   246: aload #9
    //   248: invokevirtual size : ()I
    //   251: i2l
    //   252: invokevirtual newMessageSink : (IJ)Lokio/Sink;
    //   255: invokestatic buffer : (Lokio/Sink;)Lokio/BufferedSink;
    //   258: astore_2
    //   259: aload_2
    //   260: aload #9
    //   262: invokeinterface write : (Lokio/ByteString;)Lokio/BufferedSink;
    //   267: pop
    //   268: aload_2
    //   269: invokeinterface close : ()V
    //   274: aload_0
    //   275: monitorenter
    //   276: aload_0
    //   277: aload_0
    //   278: getfield queueSize : J
    //   281: aload #9
    //   283: invokevirtual size : ()I
    //   286: i2l
    //   287: lsub
    //   288: putfield queueSize : J
    //   291: aload_0
    //   292: monitorexit
    //   293: goto -> 131
    //   296: astore_2
    //   297: aload_0
    //   298: monitorexit
    //   299: aload_2
    //   300: athrow
    //   301: astore_2
    //   302: aload #10
    //   304: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   307: aload_2
    //   308: athrow
    //   309: aload_2
    //   310: instanceof okhttp3/internal/ws/RealWebSocket$Close
    //   313: ifeq -> 354
    //   316: aload_2
    //   317: checkcast okhttp3/internal/ws/RealWebSocket$Close
    //   320: astore_2
    //   321: aload #6
    //   323: aload_2
    //   324: getfield code : I
    //   327: aload_2
    //   328: getfield reason : Lokio/ByteString;
    //   331: invokevirtual writeClose : (ILokio/ByteString;)V
    //   334: aload #10
    //   336: ifnull -> 131
    //   339: aload_0
    //   340: getfield listener : Lokhttp3/WebSocketListener;
    //   343: aload_0
    //   344: iload #8
    //   346: aload #9
    //   348: invokevirtual onClosed : (Lokhttp3/WebSocket;ILjava/lang/String;)V
    //   351: goto -> 131
    //   354: new java/lang/AssertionError
    //   357: astore_2
    //   358: aload_2
    //   359: invokespecial <init> : ()V
    //   362: aload_2
    //   363: athrow
    // Exception table:
    //   from	to	target	type
    //   14	23	190	finally
    //   25	43	190	finally
    //   59	88	190	finally
    //   94	114	190	finally
    //   117	119	190	finally
    //   124	131	301	finally
    //   141	180	190	finally
    //   192	194	190	finally
    //   216	218	190	finally
    //   221	276	301	finally
    //   276	293	296	finally
    //   297	299	296	finally
    //   299	301	301	finally
    //   309	334	301	finally
    //   339	351	301	finally
    //   354	364	301	finally
  }
  
  void writePingFrame() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield failed : Z
    //   6: ifeq -> 12
    //   9: aload_0
    //   10: monitorexit
    //   11: return
    //   12: aload_0
    //   13: getfield writer : Lokhttp3/internal/ws/WebSocketWriter;
    //   16: astore_1
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_1
    //   20: getstatic okio/ByteString.EMPTY : Lokio/ByteString;
    //   23: invokevirtual writePing : (Lokio/ByteString;)V
    //   26: goto -> 11
    //   29: astore_1
    //   30: aload_0
    //   31: aload_1
    //   32: aconst_null
    //   33: invokevirtual failWebSocket : (Ljava/lang/Exception;Lokhttp3/Response;)V
    //   36: goto -> 11
    //   39: astore_1
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_1
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	39	finally
    //   12	19	39	finally
    //   19	26	29	java/io/IOException
    //   40	42	39	finally
  }
  
  static {
    boolean bool;
    if (!RealWebSocket.class.desiredAssertionStatus()) {
      bool = true;
    } else {
      bool = false;
    } 
    $assertionsDisabled = bool;
  }
  
  final class CancelRunnable implements Runnable {
    public void run() {
      RealWebSocket.this.cancel();
    }
  }
  
  static final class Close {
    final long cancelAfterCloseMillis;
    
    final int code;
    
    final ByteString reason;
    
    Close(int param1Int, ByteString param1ByteString, long param1Long) {
      this.code = param1Int;
      this.reason = param1ByteString;
      this.cancelAfterCloseMillis = param1Long;
    }
  }
  
  static final class Message {
    final ByteString data;
    
    final int formatOpcode;
    
    Message(int param1Int, ByteString param1ByteString) {
      this.formatOpcode = param1Int;
      this.data = param1ByteString;
    }
  }
  
  private final class PingRunnable implements Runnable {
    public void run() {
      RealWebSocket.this.writePingFrame();
    }
  }
  
  public static abstract class Streams implements Closeable {
    public final boolean client;
    
    public final BufferedSink sink;
    
    public final BufferedSource source;
    
    public Streams(boolean param1Boolean, BufferedSource param1BufferedSource, BufferedSink param1BufferedSink) {
      this.client = param1Boolean;
      this.source = param1BufferedSource;
      this.sink = param1BufferedSink;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/ws/RealWebSocket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */