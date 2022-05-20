package okhttp3.internal.http2;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import okhttp3.Protocol;
import okhttp3.internal.NamedRunnable;
import okhttp3.internal.Util;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;

public final class Http2Connection implements Closeable {
  private static final int OKHTTP_CLIENT_WINDOW_SIZE = 16777216;
  
  static final ExecutorService executor = new ThreadPoolExecutor(0, 2147483647, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp Http2Connection", true));
  
  long bytesLeftInWriteWindow;
  
  final boolean client;
  
  final Set<Integer> currentPushRequests = new LinkedHashSet<Integer>();
  
  final String hostname;
  
  int lastGoodStreamId;
  
  final Listener listener;
  
  private int nextPingId;
  
  int nextStreamId;
  
  Settings okHttpSettings = new Settings();
  
  final Settings peerSettings = new Settings();
  
  private Map<Integer, Ping> pings;
  
  private final ExecutorService pushExecutor;
  
  final PushObserver pushObserver;
  
  final ReaderRunnable readerRunnable;
  
  boolean receivedInitialPeerSettings = false;
  
  boolean shutdown;
  
  final Socket socket;
  
  final Map<Integer, Http2Stream> streams = new LinkedHashMap<Integer, Http2Stream>();
  
  long unacknowledgedBytesRead = 0L;
  
  final Http2Writer writer;
  
  Http2Connection(Builder paramBuilder) {
    this.pushObserver = paramBuilder.pushObserver;
    this.client = paramBuilder.client;
    this.listener = paramBuilder.listener;
    if (paramBuilder.client) {
      b2 = 1;
    } else {
      b2 = 2;
    } 
    this.nextStreamId = b2;
    if (paramBuilder.client)
      this.nextStreamId += 2; 
    byte b2 = b1;
    if (paramBuilder.client)
      b2 = 1; 
    this.nextPingId = b2;
    if (paramBuilder.client)
      this.okHttpSettings.set(7, 16777216); 
    this.hostname = paramBuilder.hostname;
    this.pushExecutor = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), Util.threadFactory(Util.format("OkHttp %s Push Observer", new Object[] { this.hostname }), true));
    this.peerSettings.set(7, 65535);
    this.peerSettings.set(5, 16384);
    this.bytesLeftInWriteWindow = this.peerSettings.getInitialWindowSize();
    this.socket = paramBuilder.socket;
    this.writer = new Http2Writer(paramBuilder.sink, this.client);
    this.readerRunnable = new ReaderRunnable(new Http2Reader(paramBuilder.source, this.client));
  }
  
  private Http2Stream newStream(int paramInt, List<Header> paramList, boolean paramBoolean) throws IOException {
    // Byte code:
    //   0: iload_3
    //   1: ifne -> 46
    //   4: iconst_1
    //   5: istore #4
    //   7: aload_0
    //   8: getfield writer : Lokhttp3/internal/http2/Http2Writer;
    //   11: astore #5
    //   13: aload #5
    //   15: monitorenter
    //   16: aload_0
    //   17: monitorenter
    //   18: aload_0
    //   19: getfield shutdown : Z
    //   22: ifeq -> 52
    //   25: new okhttp3/internal/http2/ConnectionShutdownException
    //   28: astore_2
    //   29: aload_2
    //   30: invokespecial <init> : ()V
    //   33: aload_2
    //   34: athrow
    //   35: astore_2
    //   36: aload_0
    //   37: monitorexit
    //   38: aload_2
    //   39: athrow
    //   40: astore_2
    //   41: aload #5
    //   43: monitorexit
    //   44: aload_2
    //   45: athrow
    //   46: iconst_0
    //   47: istore #4
    //   49: goto -> 7
    //   52: aload_0
    //   53: getfield nextStreamId : I
    //   56: istore #6
    //   58: aload_0
    //   59: aload_0
    //   60: getfield nextStreamId : I
    //   63: iconst_2
    //   64: iadd
    //   65: putfield nextStreamId : I
    //   68: new okhttp3/internal/http2/Http2Stream
    //   71: astore #7
    //   73: aload #7
    //   75: iload #6
    //   77: aload_0
    //   78: iload #4
    //   80: iconst_0
    //   81: aload_2
    //   82: invokespecial <init> : (ILokhttp3/internal/http2/Http2Connection;ZZLjava/util/List;)V
    //   85: iload_3
    //   86: ifeq -> 108
    //   89: aload_0
    //   90: getfield bytesLeftInWriteWindow : J
    //   93: lconst_0
    //   94: lcmp
    //   95: ifeq -> 108
    //   98: aload #7
    //   100: getfield bytesLeftInWriteWindow : J
    //   103: lconst_0
    //   104: lcmp
    //   105: ifne -> 173
    //   108: iconst_1
    //   109: istore #8
    //   111: aload #7
    //   113: invokevirtual isOpen : ()Z
    //   116: ifeq -> 136
    //   119: aload_0
    //   120: getfield streams : Ljava/util/Map;
    //   123: iload #6
    //   125: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   128: aload #7
    //   130: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   135: pop
    //   136: aload_0
    //   137: monitorexit
    //   138: iload_1
    //   139: ifne -> 179
    //   142: aload_0
    //   143: getfield writer : Lokhttp3/internal/http2/Http2Writer;
    //   146: iload #4
    //   148: iload #6
    //   150: iload_1
    //   151: aload_2
    //   152: invokevirtual synStream : (ZIILjava/util/List;)V
    //   155: aload #5
    //   157: monitorexit
    //   158: iload #8
    //   160: ifeq -> 170
    //   163: aload_0
    //   164: getfield writer : Lokhttp3/internal/http2/Http2Writer;
    //   167: invokevirtual flush : ()V
    //   170: aload #7
    //   172: areturn
    //   173: iconst_0
    //   174: istore #8
    //   176: goto -> 111
    //   179: aload_0
    //   180: getfield client : Z
    //   183: ifeq -> 198
    //   186: new java/lang/IllegalArgumentException
    //   189: astore_2
    //   190: aload_2
    //   191: ldc 'client streams shouldn't have associated stream IDs'
    //   193: invokespecial <init> : (Ljava/lang/String;)V
    //   196: aload_2
    //   197: athrow
    //   198: aload_0
    //   199: getfield writer : Lokhttp3/internal/http2/Http2Writer;
    //   202: iload_1
    //   203: iload #6
    //   205: aload_2
    //   206: invokevirtual pushPromise : (IILjava/util/List;)V
    //   209: goto -> 155
    // Exception table:
    //   from	to	target	type
    //   16	18	40	finally
    //   18	35	35	finally
    //   36	38	35	finally
    //   38	40	40	finally
    //   41	44	40	finally
    //   52	85	35	finally
    //   89	108	35	finally
    //   111	136	35	finally
    //   136	138	35	finally
    //   142	155	40	finally
    //   155	158	40	finally
    //   179	198	40	finally
    //   198	209	40	finally
  }
  
  void addBytesToWriteWindow(long paramLong) {
    this.bytesLeftInWriteWindow += paramLong;
    if (paramLong > 0L)
      notifyAll(); 
  }
  
  public void close() throws IOException {
    close(ErrorCode.NO_ERROR, ErrorCode.CANCEL);
  }
  
  void close(ErrorCode paramErrorCode1, ErrorCode paramErrorCode2) throws IOException {
    // Byte code:
    //   0: getstatic okhttp3/internal/http2/Http2Connection.$assertionsDisabled : Z
    //   3: ifne -> 21
    //   6: aload_0
    //   7: invokestatic holdsLock : (Ljava/lang/Object;)Z
    //   10: ifeq -> 21
    //   13: new java/lang/AssertionError
    //   16: dup
    //   17: invokespecial <init> : ()V
    //   20: athrow
    //   21: aconst_null
    //   22: astore_3
    //   23: aload_0
    //   24: aload_1
    //   25: invokevirtual shutdown : (Lokhttp3/internal/http2/ErrorCode;)V
    //   28: aload_3
    //   29: astore_1
    //   30: aconst_null
    //   31: astore #4
    //   33: aconst_null
    //   34: astore #5
    //   36: aload_0
    //   37: monitorenter
    //   38: aload_0
    //   39: getfield streams : Ljava/util/Map;
    //   42: invokeinterface isEmpty : ()Z
    //   47: ifne -> 90
    //   50: aload_0
    //   51: getfield streams : Ljava/util/Map;
    //   54: invokeinterface values : ()Ljava/util/Collection;
    //   59: aload_0
    //   60: getfield streams : Ljava/util/Map;
    //   63: invokeinterface size : ()I
    //   68: anewarray okhttp3/internal/http2/Http2Stream
    //   71: invokeinterface toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   76: checkcast [Lokhttp3/internal/http2/Http2Stream;
    //   79: astore #4
    //   81: aload_0
    //   82: getfield streams : Ljava/util/Map;
    //   85: invokeinterface clear : ()V
    //   90: aload_0
    //   91: getfield pings : Ljava/util/Map;
    //   94: ifnull -> 133
    //   97: aload_0
    //   98: getfield pings : Ljava/util/Map;
    //   101: invokeinterface values : ()Ljava/util/Collection;
    //   106: aload_0
    //   107: getfield pings : Ljava/util/Map;
    //   110: invokeinterface size : ()I
    //   115: anewarray okhttp3/internal/http2/Ping
    //   118: invokeinterface toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   123: checkcast [Lokhttp3/internal/http2/Ping;
    //   126: astore #5
    //   128: aload_0
    //   129: aconst_null
    //   130: putfield pings : Ljava/util/Map;
    //   133: aload_0
    //   134: monitorexit
    //   135: aload_1
    //   136: astore_3
    //   137: aload #4
    //   139: ifnull -> 203
    //   142: aload #4
    //   144: arraylength
    //   145: istore #6
    //   147: iconst_0
    //   148: istore #7
    //   150: aload_1
    //   151: astore_3
    //   152: iload #7
    //   154: iload #6
    //   156: if_icmpge -> 203
    //   159: aload #4
    //   161: iload #7
    //   163: aaload
    //   164: astore_3
    //   165: aload_3
    //   166: aload_2
    //   167: invokevirtual close : (Lokhttp3/internal/http2/ErrorCode;)V
    //   170: aload_1
    //   171: astore_3
    //   172: iinc #7, 1
    //   175: aload_3
    //   176: astore_1
    //   177: goto -> 150
    //   180: astore_1
    //   181: goto -> 30
    //   184: astore_1
    //   185: aload_0
    //   186: monitorexit
    //   187: aload_1
    //   188: athrow
    //   189: astore #8
    //   191: aload_1
    //   192: astore_3
    //   193: aload_1
    //   194: ifnull -> 172
    //   197: aload #8
    //   199: astore_3
    //   200: goto -> 172
    //   203: aload #5
    //   205: ifnull -> 237
    //   208: aload #5
    //   210: arraylength
    //   211: istore #6
    //   213: iconst_0
    //   214: istore #7
    //   216: iload #7
    //   218: iload #6
    //   220: if_icmpge -> 237
    //   223: aload #5
    //   225: iload #7
    //   227: aaload
    //   228: invokevirtual cancel : ()V
    //   231: iinc #7, 1
    //   234: goto -> 216
    //   237: aload_0
    //   238: getfield writer : Lokhttp3/internal/http2/Http2Writer;
    //   241: invokevirtual close : ()V
    //   244: aload_3
    //   245: astore_1
    //   246: aload_0
    //   247: getfield socket : Ljava/net/Socket;
    //   250: invokevirtual close : ()V
    //   253: aload_1
    //   254: ifnull -> 275
    //   257: aload_1
    //   258: athrow
    //   259: astore_2
    //   260: aload_3
    //   261: astore_1
    //   262: aload_3
    //   263: ifnonnull -> 246
    //   266: aload_2
    //   267: astore_1
    //   268: goto -> 246
    //   271: astore_1
    //   272: goto -> 253
    //   275: return
    // Exception table:
    //   from	to	target	type
    //   23	28	180	java/io/IOException
    //   38	90	184	finally
    //   90	133	184	finally
    //   133	135	184	finally
    //   165	170	189	java/io/IOException
    //   185	187	184	finally
    //   237	244	259	java/io/IOException
    //   246	253	271	java/io/IOException
  }
  
  public void flush() throws IOException {
    this.writer.flush();
  }
  
  public Protocol getProtocol() {
    return Protocol.HTTP_2;
  }
  
  Http2Stream getStream(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield streams : Ljava/util/Map;
    //   6: iload_1
    //   7: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   10: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   15: checkcast okhttp3/internal/http2/Http2Stream
    //   18: astore_2
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_2
    //   22: areturn
    //   23: astore_2
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_2
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	23	finally
  }
  
  public boolean isShutdown() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield shutdown : Z
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
  
  public int maxConcurrentStreams() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield peerSettings : Lokhttp3/internal/http2/Settings;
    //   6: ldc 2147483647
    //   8: invokevirtual getMaxConcurrentStreams : (I)I
    //   11: istore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: iload_1
    //   15: ireturn
    //   16: astore_2
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_2
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	16	finally
  }
  
  public Http2Stream newStream(List<Header> paramList, boolean paramBoolean) throws IOException {
    return newStream(0, paramList, paramBoolean);
  }
  
  public int openStreamCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield streams : Ljava/util/Map;
    //   6: invokeinterface size : ()I
    //   11: istore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: iload_1
    //   15: ireturn
    //   16: astore_2
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_2
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	16	finally
  }
  
  public Ping ping() throws IOException {
    // Byte code:
    //   0: new okhttp3/internal/http2/Ping
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_1
    //   8: aload_0
    //   9: monitorenter
    //   10: aload_0
    //   11: getfield shutdown : Z
    //   14: ifeq -> 32
    //   17: new okhttp3/internal/http2/ConnectionShutdownException
    //   20: astore_1
    //   21: aload_1
    //   22: invokespecial <init> : ()V
    //   25: aload_1
    //   26: athrow
    //   27: astore_1
    //   28: aload_0
    //   29: monitorexit
    //   30: aload_1
    //   31: athrow
    //   32: aload_0
    //   33: getfield nextPingId : I
    //   36: istore_2
    //   37: aload_0
    //   38: aload_0
    //   39: getfield nextPingId : I
    //   42: iconst_2
    //   43: iadd
    //   44: putfield nextPingId : I
    //   47: aload_0
    //   48: getfield pings : Ljava/util/Map;
    //   51: ifnonnull -> 67
    //   54: new java/util/LinkedHashMap
    //   57: astore_3
    //   58: aload_3
    //   59: invokespecial <init> : ()V
    //   62: aload_0
    //   63: aload_3
    //   64: putfield pings : Ljava/util/Map;
    //   67: aload_0
    //   68: getfield pings : Ljava/util/Map;
    //   71: iload_2
    //   72: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   75: aload_1
    //   76: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   81: pop
    //   82: aload_0
    //   83: monitorexit
    //   84: aload_0
    //   85: iconst_0
    //   86: iload_2
    //   87: ldc_w 1330343787
    //   90: aload_1
    //   91: invokevirtual writePing : (ZIILokhttp3/internal/http2/Ping;)V
    //   94: aload_1
    //   95: areturn
    // Exception table:
    //   from	to	target	type
    //   10	27	27	finally
    //   28	30	27	finally
    //   32	67	27	finally
    //   67	84	27	finally
  }
  
  void pushDataLater(final int streamId, BufferedSource paramBufferedSource, final int byteCount, final boolean inFinished) throws IOException {
    final Buffer buffer = new Buffer();
    paramBufferedSource.require(byteCount);
    paramBufferedSource.read(buffer, byteCount);
    if (buffer.size() != byteCount)
      throw new IOException(buffer.size() + " != " + byteCount); 
    this.pushExecutor.execute((Runnable)new NamedRunnable("OkHttp %s Push Data[%s]", new Object[] { this.hostname, Integer.valueOf(streamId) }) {
          public void execute() {
            try {
              boolean bool = Http2Connection.this.pushObserver.onData(streamId, (BufferedSource)buffer, byteCount, inFinished);
              if (bool)
                Http2Connection.this.writer.rstStream(streamId, ErrorCode.CANCEL); 
              if (bool || inFinished)
                synchronized (Http2Connection.this) {
                  Http2Connection.this.currentPushRequests.remove(Integer.valueOf(streamId));
                  return;
                }  
            } catch (IOException iOException) {}
          }
        });
  }
  
  void pushHeadersLater(final int streamId, final List<Header> requestHeaders, final boolean inFinished) {
    this.pushExecutor.execute((Runnable)new NamedRunnable("OkHttp %s Push Headers[%s]", new Object[] { this.hostname, Integer.valueOf(streamId) }) {
          public void execute() {
            // Byte code:
            //   0: aload_0
            //   1: getfield this$0 : Lokhttp3/internal/http2/Http2Connection;
            //   4: getfield pushObserver : Lokhttp3/internal/http2/PushObserver;
            //   7: aload_0
            //   8: getfield val$streamId : I
            //   11: aload_0
            //   12: getfield val$requestHeaders : Ljava/util/List;
            //   15: aload_0
            //   16: getfield val$inFinished : Z
            //   19: invokeinterface onHeaders : (ILjava/util/List;Z)Z
            //   24: istore_1
            //   25: iload_1
            //   26: ifeq -> 46
            //   29: aload_0
            //   30: getfield this$0 : Lokhttp3/internal/http2/Http2Connection;
            //   33: getfield writer : Lokhttp3/internal/http2/Http2Writer;
            //   36: aload_0
            //   37: getfield val$streamId : I
            //   40: getstatic okhttp3/internal/http2/ErrorCode.CANCEL : Lokhttp3/internal/http2/ErrorCode;
            //   43: invokevirtual rstStream : (ILokhttp3/internal/http2/ErrorCode;)V
            //   46: iload_1
            //   47: ifne -> 57
            //   50: aload_0
            //   51: getfield val$inFinished : Z
            //   54: ifeq -> 86
            //   57: aload_0
            //   58: getfield this$0 : Lokhttp3/internal/http2/Http2Connection;
            //   61: astore_2
            //   62: aload_2
            //   63: monitorenter
            //   64: aload_0
            //   65: getfield this$0 : Lokhttp3/internal/http2/Http2Connection;
            //   68: getfield currentPushRequests : Ljava/util/Set;
            //   71: aload_0
            //   72: getfield val$streamId : I
            //   75: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   78: invokeinterface remove : (Ljava/lang/Object;)Z
            //   83: pop
            //   84: aload_2
            //   85: monitorexit
            //   86: return
            //   87: astore_3
            //   88: aload_2
            //   89: monitorexit
            //   90: aload_3
            //   91: athrow
            //   92: astore_3
            //   93: goto -> 86
            // Exception table:
            //   from	to	target	type
            //   29	46	92	java/io/IOException
            //   50	57	92	java/io/IOException
            //   57	64	92	java/io/IOException
            //   64	86	87	finally
            //   88	90	87	finally
            //   90	92	92	java/io/IOException
          }
        });
  }
  
  void pushRequestLater(int paramInt, List<Header> paramList) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield currentPushRequests : Ljava/util/Set;
    //   6: iload_1
    //   7: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   10: invokeinterface contains : (Ljava/lang/Object;)Z
    //   15: ifeq -> 29
    //   18: aload_0
    //   19: iload_1
    //   20: getstatic okhttp3/internal/http2/ErrorCode.PROTOCOL_ERROR : Lokhttp3/internal/http2/ErrorCode;
    //   23: invokevirtual writeSynResetLater : (ILokhttp3/internal/http2/ErrorCode;)V
    //   26: aload_0
    //   27: monitorexit
    //   28: return
    //   29: aload_0
    //   30: getfield currentPushRequests : Ljava/util/Set;
    //   33: iload_1
    //   34: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   37: invokeinterface add : (Ljava/lang/Object;)Z
    //   42: pop
    //   43: aload_0
    //   44: monitorexit
    //   45: aload_0
    //   46: getfield pushExecutor : Ljava/util/concurrent/ExecutorService;
    //   49: new okhttp3/internal/http2/Http2Connection$4
    //   52: dup
    //   53: aload_0
    //   54: ldc_w 'OkHttp %s Push Request[%s]'
    //   57: iconst_2
    //   58: anewarray java/lang/Object
    //   61: dup
    //   62: iconst_0
    //   63: aload_0
    //   64: getfield hostname : Ljava/lang/String;
    //   67: aastore
    //   68: dup
    //   69: iconst_1
    //   70: iload_1
    //   71: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   74: aastore
    //   75: iload_1
    //   76: aload_2
    //   77: invokespecial <init> : (Lokhttp3/internal/http2/Http2Connection;Ljava/lang/String;[Ljava/lang/Object;ILjava/util/List;)V
    //   80: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   85: goto -> 28
    //   88: astore_2
    //   89: aload_0
    //   90: monitorexit
    //   91: aload_2
    //   92: athrow
    // Exception table:
    //   from	to	target	type
    //   2	28	88	finally
    //   29	45	88	finally
    //   89	91	88	finally
  }
  
  void pushResetLater(final int streamId, final ErrorCode errorCode) {
    this.pushExecutor.execute((Runnable)new NamedRunnable("OkHttp %s Push Reset[%s]", new Object[] { this.hostname, Integer.valueOf(streamId) }) {
          public void execute() {
            Http2Connection.this.pushObserver.onReset(streamId, errorCode);
            synchronized (Http2Connection.this) {
              Http2Connection.this.currentPushRequests.remove(Integer.valueOf(streamId));
              return;
            } 
          }
        });
  }
  
  public Http2Stream pushStream(int paramInt, List<Header> paramList, boolean paramBoolean) throws IOException {
    if (this.client)
      throw new IllegalStateException("Client cannot push requests."); 
    return newStream(paramInt, paramList, paramBoolean);
  }
  
  boolean pushedStream(int paramInt) {
    return (paramInt != 0 && (paramInt & 0x1) == 0);
  }
  
  Ping removePing(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield pings : Ljava/util/Map;
    //   6: ifnull -> 30
    //   9: aload_0
    //   10: getfield pings : Ljava/util/Map;
    //   13: iload_1
    //   14: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   17: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   22: checkcast okhttp3/internal/http2/Ping
    //   25: astore_2
    //   26: aload_0
    //   27: monitorexit
    //   28: aload_2
    //   29: areturn
    //   30: aconst_null
    //   31: astore_2
    //   32: goto -> 26
    //   35: astore_2
    //   36: aload_0
    //   37: monitorexit
    //   38: aload_2
    //   39: athrow
    // Exception table:
    //   from	to	target	type
    //   2	26	35	finally
  }
  
  Http2Stream removeStream(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield streams : Ljava/util/Map;
    //   6: iload_1
    //   7: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   10: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   15: checkcast okhttp3/internal/http2/Http2Stream
    //   18: astore_2
    //   19: aload_0
    //   20: invokevirtual notifyAll : ()V
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_2
    //   26: areturn
    //   27: astore_2
    //   28: aload_0
    //   29: monitorexit
    //   30: aload_2
    //   31: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	27	finally
  }
  
  public void setSettings(Settings paramSettings) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield writer : Lokhttp3/internal/http2/Http2Writer;
    //   4: astore_2
    //   5: aload_2
    //   6: monitorenter
    //   7: aload_0
    //   8: monitorenter
    //   9: aload_0
    //   10: getfield shutdown : Z
    //   13: ifeq -> 36
    //   16: new okhttp3/internal/http2/ConnectionShutdownException
    //   19: astore_1
    //   20: aload_1
    //   21: invokespecial <init> : ()V
    //   24: aload_1
    //   25: athrow
    //   26: astore_1
    //   27: aload_0
    //   28: monitorexit
    //   29: aload_1
    //   30: athrow
    //   31: astore_1
    //   32: aload_2
    //   33: monitorexit
    //   34: aload_1
    //   35: athrow
    //   36: aload_0
    //   37: getfield okHttpSettings : Lokhttp3/internal/http2/Settings;
    //   40: aload_1
    //   41: invokevirtual merge : (Lokhttp3/internal/http2/Settings;)V
    //   44: aload_0
    //   45: getfield writer : Lokhttp3/internal/http2/Http2Writer;
    //   48: aload_1
    //   49: invokevirtual settings : (Lokhttp3/internal/http2/Settings;)V
    //   52: aload_0
    //   53: monitorexit
    //   54: aload_2
    //   55: monitorexit
    //   56: return
    // Exception table:
    //   from	to	target	type
    //   7	9	31	finally
    //   9	26	26	finally
    //   27	29	26	finally
    //   29	31	31	finally
    //   32	34	31	finally
    //   36	54	26	finally
    //   54	56	31	finally
  }
  
  public void shutdown(ErrorCode paramErrorCode) throws IOException {
    synchronized (this.writer) {
      /* monitor enter ThisExpression{ObjectType{okhttp3/internal/http2/Http2Connection}} */
      try {
        if (this.shutdown) {
          /* monitor exit ThisExpression{ObjectType{okhttp3/internal/http2/Http2Connection}} */
          return;
        } 
        this.shutdown = true;
        int i = this.lastGoodStreamId;
        /* monitor exit ThisExpression{ObjectType{okhttp3/internal/http2/Http2Connection}} */
        this.writer.goAway(i, paramErrorCode, Util.EMPTY_BYTE_ARRAY);
        return;
      } finally {}
    } 
    /* monitor exit ThisExpression{ObjectType{okhttp3/internal/http2/Http2Connection}} */
    throw paramErrorCode;
  }
  
  public void start() throws IOException {
    start(true);
  }
  
  void start(boolean paramBoolean) throws IOException {
    if (paramBoolean) {
      this.writer.connectionPreface();
      this.writer.settings(this.okHttpSettings);
      int i = this.okHttpSettings.getInitialWindowSize();
      if (i != 65535)
        this.writer.windowUpdate(0, (i - 65535)); 
    } 
    (new Thread((Runnable)this.readerRunnable)).start();
  }
  
  public void writeData(int paramInt, boolean paramBoolean, Buffer paramBuffer, long paramLong) throws IOException {
    // Byte code:
    //   0: lload #4
    //   2: lstore #6
    //   4: lload #4
    //   6: lconst_0
    //   7: lcmp
    //   8: ifne -> 98
    //   11: aload_0
    //   12: getfield writer : Lokhttp3/internal/http2/Http2Writer;
    //   15: iload_2
    //   16: iload_1
    //   17: aload_3
    //   18: iconst_0
    //   19: invokevirtual data : (ZILokio/Buffer;I)V
    //   22: return
    //   23: lload #6
    //   25: aload_0
    //   26: getfield bytesLeftInWriteWindow : J
    //   29: invokestatic min : (JJ)J
    //   32: l2i
    //   33: aload_0
    //   34: getfield writer : Lokhttp3/internal/http2/Http2Writer;
    //   37: invokevirtual maxDataLength : ()I
    //   40: invokestatic min : (II)I
    //   43: istore #8
    //   45: aload_0
    //   46: aload_0
    //   47: getfield bytesLeftInWriteWindow : J
    //   50: iload #8
    //   52: i2l
    //   53: lsub
    //   54: putfield bytesLeftInWriteWindow : J
    //   57: aload_0
    //   58: monitorexit
    //   59: lload #6
    //   61: iload #8
    //   63: i2l
    //   64: lsub
    //   65: lstore #6
    //   67: aload_0
    //   68: getfield writer : Lokhttp3/internal/http2/Http2Writer;
    //   71: astore #9
    //   73: iload_2
    //   74: ifeq -> 168
    //   77: lload #6
    //   79: lconst_0
    //   80: lcmp
    //   81: ifne -> 168
    //   84: iconst_1
    //   85: istore #10
    //   87: aload #9
    //   89: iload #10
    //   91: iload_1
    //   92: aload_3
    //   93: iload #8
    //   95: invokevirtual data : (ZILokio/Buffer;I)V
    //   98: lload #6
    //   100: lconst_0
    //   101: lcmp
    //   102: ifle -> 22
    //   105: aload_0
    //   106: monitorenter
    //   107: aload_0
    //   108: getfield bytesLeftInWriteWindow : J
    //   111: lconst_0
    //   112: lcmp
    //   113: ifgt -> 23
    //   116: aload_0
    //   117: getfield streams : Ljava/util/Map;
    //   120: iload_1
    //   121: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   124: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   129: ifne -> 161
    //   132: new java/io/IOException
    //   135: astore_3
    //   136: aload_3
    //   137: ldc_w 'stream closed'
    //   140: invokespecial <init> : (Ljava/lang/String;)V
    //   143: aload_3
    //   144: athrow
    //   145: astore_3
    //   146: new java/io/InterruptedIOException
    //   149: astore_3
    //   150: aload_3
    //   151: invokespecial <init> : ()V
    //   154: aload_3
    //   155: athrow
    //   156: astore_3
    //   157: aload_0
    //   158: monitorexit
    //   159: aload_3
    //   160: athrow
    //   161: aload_0
    //   162: invokevirtual wait : ()V
    //   165: goto -> 107
    //   168: iconst_0
    //   169: istore #10
    //   171: goto -> 87
    // Exception table:
    //   from	to	target	type
    //   23	59	156	finally
    //   107	145	145	java/lang/InterruptedException
    //   107	145	156	finally
    //   146	156	156	finally
    //   157	159	156	finally
    //   161	165	145	java/lang/InterruptedException
    //   161	165	156	finally
  }
  
  void writePing(boolean paramBoolean, int paramInt1, int paramInt2, Ping paramPing) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield writer : Lokhttp3/internal/http2/Http2Writer;
    //   4: astore #5
    //   6: aload #5
    //   8: monitorenter
    //   9: aload #4
    //   11: ifnull -> 19
    //   14: aload #4
    //   16: invokevirtual send : ()V
    //   19: aload_0
    //   20: getfield writer : Lokhttp3/internal/http2/Http2Writer;
    //   23: iload_1
    //   24: iload_2
    //   25: iload_3
    //   26: invokevirtual ping : (ZII)V
    //   29: aload #5
    //   31: monitorexit
    //   32: return
    //   33: astore #4
    //   35: aload #5
    //   37: monitorexit
    //   38: aload #4
    //   40: athrow
    // Exception table:
    //   from	to	target	type
    //   14	19	33	finally
    //   19	32	33	finally
    //   35	38	33	finally
  }
  
  void writePingLater(final boolean reply, final int payload1, final int payload2, final Ping ping) {
    executor.execute((Runnable)new NamedRunnable("OkHttp %s ping %08x%08x", new Object[] { this.hostname, Integer.valueOf(payload1), Integer.valueOf(payload2) }) {
          public void execute() {
            try {
              Http2Connection.this.writePing(reply, payload1, payload2, ping);
            } catch (IOException iOException) {}
          }
        });
  }
  
  void writeSynReply(int paramInt, boolean paramBoolean, List<Header> paramList) throws IOException {
    this.writer.synReply(paramBoolean, paramInt, paramList);
  }
  
  void writeSynReset(int paramInt, ErrorCode paramErrorCode) throws IOException {
    this.writer.rstStream(paramInt, paramErrorCode);
  }
  
  void writeSynResetLater(final int streamId, final ErrorCode errorCode) {
    executor.execute((Runnable)new NamedRunnable("OkHttp %s stream %d", new Object[] { this.hostname, Integer.valueOf(streamId) }) {
          public void execute() {
            try {
              Http2Connection.this.writeSynReset(streamId, errorCode);
            } catch (IOException iOException) {}
          }
        });
  }
  
  void writeWindowUpdateLater(final int streamId, final long unacknowledgedBytesRead) {
    executor.execute((Runnable)new NamedRunnable("OkHttp Window Update %s stream %d", new Object[] { this.hostname, Integer.valueOf(streamId) }) {
          public void execute() {
            try {
              Http2Connection.this.writer.windowUpdate(streamId, unacknowledgedBytesRead);
            } catch (IOException iOException) {}
          }
        });
  }
  
  static {
    boolean bool;
    if (!Http2Connection.class.desiredAssertionStatus()) {
      bool = true;
    } else {
      bool = false;
    } 
    $assertionsDisabled = bool;
  }
  
  public static class Builder {
    boolean client;
    
    String hostname;
    
    Http2Connection.Listener listener = Http2Connection.Listener.REFUSE_INCOMING_STREAMS;
    
    PushObserver pushObserver = PushObserver.CANCEL;
    
    BufferedSink sink;
    
    Socket socket;
    
    BufferedSource source;
    
    public Builder(boolean param1Boolean) {
      this.client = param1Boolean;
    }
    
    public Http2Connection build() throws IOException {
      return new Http2Connection(this);
    }
    
    public Builder listener(Http2Connection.Listener param1Listener) {
      this.listener = param1Listener;
      return this;
    }
    
    public Builder pushObserver(PushObserver param1PushObserver) {
      this.pushObserver = param1PushObserver;
      return this;
    }
    
    public Builder socket(Socket param1Socket) throws IOException {
      return socket(param1Socket, ((InetSocketAddress)param1Socket.getRemoteSocketAddress()).getHostName(), Okio.buffer(Okio.source(param1Socket)), Okio.buffer(Okio.sink(param1Socket)));
    }
    
    public Builder socket(Socket param1Socket, String param1String, BufferedSource param1BufferedSource, BufferedSink param1BufferedSink) {
      this.socket = param1Socket;
      this.hostname = param1String;
      this.source = param1BufferedSource;
      this.sink = param1BufferedSink;
      return this;
    }
  }
  
  public static abstract class Listener {
    public static final Listener REFUSE_INCOMING_STREAMS = new Listener() {
        public void onStream(Http2Stream param2Http2Stream) throws IOException {
          param2Http2Stream.close(ErrorCode.REFUSED_STREAM);
        }
      };
    
    public void onSettings(Http2Connection param1Http2Connection) {}
    
    public abstract void onStream(Http2Stream param1Http2Stream) throws IOException;
  }
  
  final class null extends Listener {
    public void onStream(Http2Stream param1Http2Stream) throws IOException {
      param1Http2Stream.close(ErrorCode.REFUSED_STREAM);
    }
  }
  
  class ReaderRunnable extends NamedRunnable implements Http2Reader.Handler {
    final Http2Reader reader;
    
    ReaderRunnable(Http2Reader param1Http2Reader) {
      super("OkHttp %s", new Object[] { this$0.hostname });
      this.reader = param1Http2Reader;
    }
    
    private void applyAndAckSettings(final Settings peerSettings) {
      Http2Connection.executor.execute((Runnable)new NamedRunnable("OkHttp %s ACK Settings", new Object[] { this.this$0.hostname }) {
            public void execute() {
              try {
                Http2Connection.this.writer.applyAndAckSettings(peerSettings);
              } catch (IOException iOException) {}
            }
          });
    }
    
    public void ackSettings() {}
    
    public void alternateService(int param1Int1, String param1String1, ByteString param1ByteString, String param1String2, int param1Int2, long param1Long) {}
    
    public void data(boolean param1Boolean, int param1Int1, BufferedSource param1BufferedSource, int param1Int2) throws IOException {
      if (Http2Connection.this.pushedStream(param1Int1)) {
        Http2Connection.this.pushDataLater(param1Int1, param1BufferedSource, param1Int2, param1Boolean);
        return;
      } 
      Http2Stream http2Stream = Http2Connection.this.getStream(param1Int1);
      if (http2Stream == null) {
        Http2Connection.this.writeSynResetLater(param1Int1, ErrorCode.PROTOCOL_ERROR);
        param1BufferedSource.skip(param1Int2);
        return;
      } 
      http2Stream.receiveData(param1BufferedSource, param1Int2);
      if (param1Boolean)
        http2Stream.receiveFin(); 
    }
    
    protected void execute() {
      // Byte code:
      //   0: getstatic okhttp3/internal/http2/ErrorCode.INTERNAL_ERROR : Lokhttp3/internal/http2/ErrorCode;
      //   3: astore_1
      //   4: getstatic okhttp3/internal/http2/ErrorCode.INTERNAL_ERROR : Lokhttp3/internal/http2/ErrorCode;
      //   7: astore_2
      //   8: aload_1
      //   9: astore_3
      //   10: aload_1
      //   11: astore #4
      //   13: aload_0
      //   14: getfield reader : Lokhttp3/internal/http2/Http2Reader;
      //   17: aload_0
      //   18: invokevirtual readConnectionPreface : (Lokhttp3/internal/http2/Http2Reader$Handler;)V
      //   21: aload_1
      //   22: astore_3
      //   23: aload_1
      //   24: astore #4
      //   26: aload_0
      //   27: getfield reader : Lokhttp3/internal/http2/Http2Reader;
      //   30: iconst_0
      //   31: aload_0
      //   32: invokevirtual nextFrame : (ZLokhttp3/internal/http2/Http2Reader$Handler;)Z
      //   35: ifne -> 21
      //   38: aload_1
      //   39: astore_3
      //   40: aload_1
      //   41: astore #4
      //   43: getstatic okhttp3/internal/http2/ErrorCode.NO_ERROR : Lokhttp3/internal/http2/ErrorCode;
      //   46: astore_1
      //   47: aload_1
      //   48: astore_3
      //   49: aload_1
      //   50: astore #4
      //   52: getstatic okhttp3/internal/http2/ErrorCode.CANCEL : Lokhttp3/internal/http2/ErrorCode;
      //   55: astore #5
      //   57: aload_0
      //   58: getfield this$0 : Lokhttp3/internal/http2/Http2Connection;
      //   61: aload_1
      //   62: aload #5
      //   64: invokevirtual close : (Lokhttp3/internal/http2/ErrorCode;Lokhttp3/internal/http2/ErrorCode;)V
      //   67: aload_0
      //   68: getfield reader : Lokhttp3/internal/http2/Http2Reader;
      //   71: invokestatic closeQuietly : (Ljava/io/Closeable;)V
      //   74: return
      //   75: astore #4
      //   77: aload_3
      //   78: astore #4
      //   80: getstatic okhttp3/internal/http2/ErrorCode.PROTOCOL_ERROR : Lokhttp3/internal/http2/ErrorCode;
      //   83: astore_3
      //   84: aload_3
      //   85: astore #4
      //   87: getstatic okhttp3/internal/http2/ErrorCode.PROTOCOL_ERROR : Lokhttp3/internal/http2/ErrorCode;
      //   90: astore_1
      //   91: aload_0
      //   92: getfield this$0 : Lokhttp3/internal/http2/Http2Connection;
      //   95: aload_3
      //   96: aload_1
      //   97: invokevirtual close : (Lokhttp3/internal/http2/ErrorCode;Lokhttp3/internal/http2/ErrorCode;)V
      //   100: aload_0
      //   101: getfield reader : Lokhttp3/internal/http2/Http2Reader;
      //   104: invokestatic closeQuietly : (Ljava/io/Closeable;)V
      //   107: goto -> 74
      //   110: astore_3
      //   111: aload_0
      //   112: getfield this$0 : Lokhttp3/internal/http2/Http2Connection;
      //   115: aload #4
      //   117: aload_2
      //   118: invokevirtual close : (Lokhttp3/internal/http2/ErrorCode;Lokhttp3/internal/http2/ErrorCode;)V
      //   121: aload_0
      //   122: getfield reader : Lokhttp3/internal/http2/Http2Reader;
      //   125: invokestatic closeQuietly : (Ljava/io/Closeable;)V
      //   128: aload_3
      //   129: athrow
      //   130: astore #4
      //   132: goto -> 121
      //   135: astore #4
      //   137: goto -> 100
      //   140: astore #4
      //   142: goto -> 67
      // Exception table:
      //   from	to	target	type
      //   13	21	75	java/io/IOException
      //   13	21	110	finally
      //   26	38	75	java/io/IOException
      //   26	38	110	finally
      //   43	47	75	java/io/IOException
      //   43	47	110	finally
      //   52	57	75	java/io/IOException
      //   52	57	110	finally
      //   57	67	140	java/io/IOException
      //   80	84	110	finally
      //   87	91	110	finally
      //   91	100	135	java/io/IOException
      //   111	121	130	java/io/IOException
    }
    
    public void goAway(int param1Int, ErrorCode param1ErrorCode, ByteString param1ByteString) {
      Http2Connection http2Connection;
      Http2Stream http2Stream;
      if (param1ByteString.size() > 0);
      synchronized (Http2Connection.this) {
        Http2Stream[] arrayOfHttp2Stream = (Http2Stream[])Http2Connection.this.streams.values().toArray((Object[])new Http2Stream[Http2Connection.this.streams.size()]);
        Http2Connection.this.shutdown = true;
        int i = arrayOfHttp2Stream.length;
        for (byte b = 0; b < i; b++) {
          http2Stream = arrayOfHttp2Stream[b];
          if (http2Stream.getId() > param1Int && http2Stream.isLocallyInitiated()) {
            http2Stream.receiveRstStream(ErrorCode.REFUSED_STREAM);
            Http2Connection.this.removeStream(http2Stream.getId());
          } 
        } 
      } 
    }
    
    public void headers(boolean param1Boolean, int param1Int1, int param1Int2, List<Header> param1List) {
      ExecutorService executorService;
      if (Http2Connection.this.pushedStream(param1Int1)) {
        Http2Connection.this.pushHeadersLater(param1Int1, param1List, param1Boolean);
        return;
      } 
      synchronized (Http2Connection.this) {
        if (Http2Connection.this.shutdown)
          return; 
      } 
      Http2Stream http2Stream = Http2Connection.this.getStream(param1Int1);
      if (http2Stream == null) {
        if (param1Int1 <= Http2Connection.this.lastGoodStreamId) {
          /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_5} */
          return;
        } 
        if (param1Int1 % 2 == Http2Connection.this.nextStreamId % 2) {
          /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_5} */
          return;
        } 
        http2Stream = new Http2Stream();
        this(param1Int1, Http2Connection.this, false, param1Boolean, param1List);
        Http2Connection.this.lastGoodStreamId = param1Int1;
        Http2Connection.this.streams.put(Integer.valueOf(param1Int1), http2Stream);
        executorService = Http2Connection.executor;
        NamedRunnable namedRunnable = new NamedRunnable() {
            public void execute() {
              try {
                Http2Connection.this.listener.onStream(newStream);
              } catch (IOException iOException) {
                Platform.get().log(4, "Http2Connection.Listener failure for " + Http2Connection.this.hostname, iOException);
              } 
            }
          };
        super(this, "OkHttp %s stream %d", new Object[] { this.this$0.hostname, Integer.valueOf(param1Int1) }, http2Stream);
        executorService.execute((Runnable)namedRunnable);
        /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_5} */
        return;
      } 
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_5} */
      http2Stream.receiveHeaders((List<Header>)executorService);
      if (param1Boolean)
        http2Stream.receiveFin(); 
    }
    
    public void ping(boolean param1Boolean, int param1Int1, int param1Int2) {
      if (param1Boolean) {
        Ping ping = Http2Connection.this.removePing(param1Int1);
        if (ping != null)
          ping.receive(); 
        return;
      } 
      Http2Connection.this.writePingLater(true, param1Int1, param1Int2, null);
    }
    
    public void priority(int param1Int1, int param1Int2, int param1Int3, boolean param1Boolean) {}
    
    public void pushPromise(int param1Int1, int param1Int2, List<Header> param1List) {
      Http2Connection.this.pushRequestLater(param1Int2, param1List);
    }
    
    public void rstStream(int param1Int, ErrorCode param1ErrorCode) {
      if (Http2Connection.this.pushedStream(param1Int)) {
        Http2Connection.this.pushResetLater(param1Int, param1ErrorCode);
        return;
      } 
      Http2Stream http2Stream = Http2Connection.this.removeStream(param1Int);
      if (http2Stream != null)
        http2Stream.receiveRstStream(param1ErrorCode); 
    }
    
    public void settings(boolean param1Boolean, Settings param1Settings) {
      long l = 0L;
      Settings settings = null;
      synchronized (Http2Connection.this) {
        Http2Stream[] arrayOfHttp2Stream;
        int i = Http2Connection.this.peerSettings.getInitialWindowSize();
        if (param1Boolean)
          Http2Connection.this.peerSettings.clear(); 
        Http2Connection.this.peerSettings.merge(param1Settings);
        applyAndAckSettings(param1Settings);
        int j = Http2Connection.this.peerSettings.getInitialWindowSize();
        long l1 = l;
        param1Settings = settings;
        if (j != -1) {
          l1 = l;
          param1Settings = settings;
          if (j != i) {
            l = (j - i);
            if (!Http2Connection.this.receivedInitialPeerSettings) {
              Http2Connection.this.addBytesToWriteWindow(l);
              Http2Connection.this.receivedInitialPeerSettings = true;
            } 
            l1 = l;
            param1Settings = settings;
            if (!Http2Connection.this.streams.isEmpty()) {
              arrayOfHttp2Stream = (Http2Stream[])Http2Connection.this.streams.values().toArray((Object[])new Http2Stream[Http2Connection.this.streams.size()]);
              l1 = l;
            } 
          } 
        } 
        ExecutorService executorService = Http2Connection.executor;
        NamedRunnable namedRunnable = new NamedRunnable() {
            public void execute() {
              Http2Connection.this.listener.onSettings(Http2Connection.this);
            }
          };
        super(this, "OkHttp %s settings", new Object[] { this.this$0.hostname });
        executorService.execute((Runnable)namedRunnable);
        if (arrayOfHttp2Stream != null && l1 != 0L) {
          j = arrayOfHttp2Stream.length;
          i = 0;
          while (i < j) {
            synchronized (arrayOfHttp2Stream[i]) {
              null.addBytesToWriteWindow(l1);
              i++;
            } 
          } 
        } 
      } 
    }
    
    public void windowUpdate(int param1Int, long param1Long) {
      // Byte code:
      //   0: iload_1
      //   1: ifne -> 50
      //   4: aload_0
      //   5: getfield this$0 : Lokhttp3/internal/http2/Http2Connection;
      //   8: astore #4
      //   10: aload #4
      //   12: monitorenter
      //   13: aload_0
      //   14: getfield this$0 : Lokhttp3/internal/http2/Http2Connection;
      //   17: astore #5
      //   19: aload #5
      //   21: aload #5
      //   23: getfield bytesLeftInWriteWindow : J
      //   26: lload_2
      //   27: ladd
      //   28: putfield bytesLeftInWriteWindow : J
      //   31: aload_0
      //   32: getfield this$0 : Lokhttp3/internal/http2/Http2Connection;
      //   35: invokevirtual notifyAll : ()V
      //   38: aload #4
      //   40: monitorexit
      //   41: return
      //   42: astore #5
      //   44: aload #4
      //   46: monitorexit
      //   47: aload #5
      //   49: athrow
      //   50: aload_0
      //   51: getfield this$0 : Lokhttp3/internal/http2/Http2Connection;
      //   54: iload_1
      //   55: invokevirtual getStream : (I)Lokhttp3/internal/http2/Http2Stream;
      //   58: astore #5
      //   60: aload #5
      //   62: ifnull -> 41
      //   65: aload #5
      //   67: monitorenter
      //   68: aload #5
      //   70: lload_2
      //   71: invokevirtual addBytesToWriteWindow : (J)V
      //   74: aload #5
      //   76: monitorexit
      //   77: goto -> 41
      //   80: astore #4
      //   82: aload #5
      //   84: monitorexit
      //   85: aload #4
      //   87: athrow
      // Exception table:
      //   from	to	target	type
      //   13	41	42	finally
      //   44	47	42	finally
      //   68	77	80	finally
      //   82	85	80	finally
    }
  }
  
  class null extends NamedRunnable {
    null(String param1String, Object[] param1ArrayOfObject) {
      super(param1String, param1ArrayOfObject);
    }
    
    public void execute() {
      try {
        Http2Connection.this.listener.onStream(newStream);
      } catch (IOException iOException) {
        Platform.get().log(4, "Http2Connection.Listener failure for " + Http2Connection.this.hostname, iOException);
      } 
    }
  }
  
  class null extends NamedRunnable {
    null(String param1String, Object... param1VarArgs) {
      super(param1String, param1VarArgs);
    }
    
    public void execute() {
      Http2Connection.this.listener.onSettings(Http2Connection.this);
    }
  }
  
  class null extends NamedRunnable {
    null(String param1String, Object[] param1ArrayOfObject) {
      super(param1String, param1ArrayOfObject);
    }
    
    public void execute() {
      try {
        Http2Connection.this.writer.applyAndAckSettings(peerSettings);
      } catch (IOException iOException) {}
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/http2/Http2Connection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */