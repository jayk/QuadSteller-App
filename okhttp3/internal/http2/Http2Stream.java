package okhttp3.internal.http2;

import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.List;
import okio.AsyncTimeout;
import okio.Buffer;
import okio.BufferedSource;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public final class Http2Stream {
  long bytesLeftInWriteWindow;
  
  final Http2Connection connection;
  
  ErrorCode errorCode = null;
  
  private boolean hasResponseHeaders;
  
  final int id;
  
  final StreamTimeout readTimeout = new StreamTimeout();
  
  private final List<Header> requestHeaders;
  
  private List<Header> responseHeaders;
  
  final FramingSink sink;
  
  private final FramingSource source;
  
  long unacknowledgedBytesRead = 0L;
  
  final StreamTimeout writeTimeout = new StreamTimeout();
  
  static {
    boolean bool;
    if (!Http2Stream.class.desiredAssertionStatus()) {
      bool = true;
    } else {
      bool = false;
    } 
    $assertionsDisabled = bool;
  }
  
  Http2Stream(int paramInt, Http2Connection paramHttp2Connection, boolean paramBoolean1, boolean paramBoolean2, List<Header> paramList) {
    if (paramHttp2Connection == null)
      throw new NullPointerException("connection == null"); 
    if (paramList == null)
      throw new NullPointerException("requestHeaders == null"); 
    this.id = paramInt;
    this.connection = paramHttp2Connection;
    this.bytesLeftInWriteWindow = paramHttp2Connection.peerSettings.getInitialWindowSize();
    this.source = new FramingSource(paramHttp2Connection.okHttpSettings.getInitialWindowSize());
    this.sink = new FramingSink();
    this.source.finished = paramBoolean2;
    this.sink.finished = paramBoolean1;
    this.requestHeaders = paramList;
  }
  
  private boolean closeInternal(ErrorCode paramErrorCode) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: getstatic okhttp3/internal/http2/Http2Stream.$assertionsDisabled : Z
    //   5: ifne -> 23
    //   8: aload_0
    //   9: invokestatic holdsLock : (Ljava/lang/Object;)Z
    //   12: ifeq -> 23
    //   15: new java/lang/AssertionError
    //   18: dup
    //   19: invokespecial <init> : ()V
    //   22: athrow
    //   23: aload_0
    //   24: monitorenter
    //   25: aload_0
    //   26: getfield errorCode : Lokhttp3/internal/http2/ErrorCode;
    //   29: ifnull -> 36
    //   32: aload_0
    //   33: monitorexit
    //   34: iload_2
    //   35: ireturn
    //   36: aload_0
    //   37: getfield source : Lokhttp3/internal/http2/Http2Stream$FramingSource;
    //   40: getfield finished : Z
    //   43: ifeq -> 66
    //   46: aload_0
    //   47: getfield sink : Lokhttp3/internal/http2/Http2Stream$FramingSink;
    //   50: getfield finished : Z
    //   53: ifeq -> 66
    //   56: aload_0
    //   57: monitorexit
    //   58: goto -> 34
    //   61: astore_1
    //   62: aload_0
    //   63: monitorexit
    //   64: aload_1
    //   65: athrow
    //   66: aload_0
    //   67: aload_1
    //   68: putfield errorCode : Lokhttp3/internal/http2/ErrorCode;
    //   71: aload_0
    //   72: invokevirtual notifyAll : ()V
    //   75: aload_0
    //   76: monitorexit
    //   77: aload_0
    //   78: getfield connection : Lokhttp3/internal/http2/Http2Connection;
    //   81: aload_0
    //   82: getfield id : I
    //   85: invokevirtual removeStream : (I)Lokhttp3/internal/http2/Http2Stream;
    //   88: pop
    //   89: iconst_1
    //   90: istore_2
    //   91: goto -> 34
    // Exception table:
    //   from	to	target	type
    //   25	34	61	finally
    //   36	58	61	finally
    //   62	64	61	finally
    //   66	77	61	finally
  }
  
  void addBytesToWriteWindow(long paramLong) {
    this.bytesLeftInWriteWindow += paramLong;
    if (paramLong > 0L)
      notifyAll(); 
  }
  
  void cancelStreamIfNecessary() throws IOException {
    // Byte code:
    //   0: getstatic okhttp3/internal/http2/Http2Stream.$assertionsDisabled : Z
    //   3: ifne -> 21
    //   6: aload_0
    //   7: invokestatic holdsLock : (Ljava/lang/Object;)Z
    //   10: ifeq -> 21
    //   13: new java/lang/AssertionError
    //   16: dup
    //   17: invokespecial <init> : ()V
    //   20: athrow
    //   21: aload_0
    //   22: monitorenter
    //   23: aload_0
    //   24: getfield source : Lokhttp3/internal/http2/Http2Stream$FramingSource;
    //   27: getfield finished : Z
    //   30: ifne -> 84
    //   33: aload_0
    //   34: getfield source : Lokhttp3/internal/http2/Http2Stream$FramingSource;
    //   37: getfield closed : Z
    //   40: ifeq -> 84
    //   43: aload_0
    //   44: getfield sink : Lokhttp3/internal/http2/Http2Stream$FramingSink;
    //   47: getfield finished : Z
    //   50: ifne -> 63
    //   53: aload_0
    //   54: getfield sink : Lokhttp3/internal/http2/Http2Stream$FramingSink;
    //   57: getfield closed : Z
    //   60: ifeq -> 84
    //   63: iconst_1
    //   64: istore_1
    //   65: aload_0
    //   66: invokevirtual isOpen : ()Z
    //   69: istore_2
    //   70: aload_0
    //   71: monitorexit
    //   72: iload_1
    //   73: ifeq -> 94
    //   76: aload_0
    //   77: getstatic okhttp3/internal/http2/ErrorCode.CANCEL : Lokhttp3/internal/http2/ErrorCode;
    //   80: invokevirtual close : (Lokhttp3/internal/http2/ErrorCode;)V
    //   83: return
    //   84: iconst_0
    //   85: istore_1
    //   86: goto -> 65
    //   89: astore_3
    //   90: aload_0
    //   91: monitorexit
    //   92: aload_3
    //   93: athrow
    //   94: iload_2
    //   95: ifne -> 83
    //   98: aload_0
    //   99: getfield connection : Lokhttp3/internal/http2/Http2Connection;
    //   102: aload_0
    //   103: getfield id : I
    //   106: invokevirtual removeStream : (I)Lokhttp3/internal/http2/Http2Stream;
    //   109: pop
    //   110: goto -> 83
    // Exception table:
    //   from	to	target	type
    //   23	63	89	finally
    //   65	72	89	finally
    //   90	92	89	finally
  }
  
  void checkOutNotClosed() throws IOException {
    if (this.sink.closed)
      throw new IOException("stream closed"); 
    if (this.sink.finished)
      throw new IOException("stream finished"); 
    if (this.errorCode != null)
      throw new StreamResetException(this.errorCode); 
  }
  
  public void close(ErrorCode paramErrorCode) throws IOException {
    if (closeInternal(paramErrorCode))
      this.connection.writeSynReset(this.id, paramErrorCode); 
  }
  
  public void closeLater(ErrorCode paramErrorCode) {
    if (closeInternal(paramErrorCode))
      this.connection.writeSynResetLater(this.id, paramErrorCode); 
  }
  
  public Http2Connection getConnection() {
    return this.connection;
  }
  
  public ErrorCode getErrorCode() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield errorCode : Lokhttp3/internal/http2/ErrorCode;
    //   6: astore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_1
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public int getId() {
    return this.id;
  }
  
  public List<Header> getRequestHeaders() {
    return this.requestHeaders;
  }
  
  public Sink getSink() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield hasResponseHeaders : Z
    //   6: ifne -> 33
    //   9: aload_0
    //   10: invokevirtual isLocallyInitiated : ()Z
    //   13: ifne -> 33
    //   16: new java/lang/IllegalStateException
    //   19: astore_1
    //   20: aload_1
    //   21: ldc 'reply before requesting the sink'
    //   23: invokespecial <init> : (Ljava/lang/String;)V
    //   26: aload_1
    //   27: athrow
    //   28: astore_1
    //   29: aload_0
    //   30: monitorexit
    //   31: aload_1
    //   32: athrow
    //   33: aload_0
    //   34: monitorexit
    //   35: aload_0
    //   36: getfield sink : Lokhttp3/internal/http2/Http2Stream$FramingSink;
    //   39: areturn
    // Exception table:
    //   from	to	target	type
    //   2	28	28	finally
    //   29	31	28	finally
    //   33	35	28	finally
  }
  
  public Source getSource() {
    return this.source;
  }
  
  public boolean isLocallyInitiated() {
    boolean bool = true;
    if ((this.id & 0x1) == 1) {
      null = true;
    } else {
      null = false;
    } 
    return (this.connection.client == null) ? bool : false;
  }
  
  public boolean isOpen() {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: monitorenter
    //   4: aload_0
    //   5: getfield errorCode : Lokhttp3/internal/http2/ErrorCode;
    //   8: astore_2
    //   9: aload_2
    //   10: ifnull -> 17
    //   13: aload_0
    //   14: monitorexit
    //   15: iload_1
    //   16: ireturn
    //   17: aload_0
    //   18: getfield source : Lokhttp3/internal/http2/Http2Stream$FramingSource;
    //   21: getfield finished : Z
    //   24: ifne -> 37
    //   27: aload_0
    //   28: getfield source : Lokhttp3/internal/http2/Http2Stream$FramingSource;
    //   31: getfield closed : Z
    //   34: ifeq -> 66
    //   37: aload_0
    //   38: getfield sink : Lokhttp3/internal/http2/Http2Stream$FramingSink;
    //   41: getfield finished : Z
    //   44: ifne -> 57
    //   47: aload_0
    //   48: getfield sink : Lokhttp3/internal/http2/Http2Stream$FramingSink;
    //   51: getfield closed : Z
    //   54: ifeq -> 66
    //   57: aload_0
    //   58: getfield hasResponseHeaders : Z
    //   61: istore_3
    //   62: iload_3
    //   63: ifne -> 13
    //   66: iconst_1
    //   67: istore_1
    //   68: goto -> 13
    //   71: astore_2
    //   72: aload_0
    //   73: monitorexit
    //   74: aload_2
    //   75: athrow
    // Exception table:
    //   from	to	target	type
    //   4	9	71	finally
    //   17	37	71	finally
    //   37	57	71	finally
    //   57	62	71	finally
  }
  
  public Timeout readTimeout() {
    return (Timeout)this.readTimeout;
  }
  
  void receiveData(BufferedSource paramBufferedSource, int paramInt) throws IOException {
    assert !Thread.holdsLock(this);
    this.source.receive(paramBufferedSource, paramInt);
  }
  
  void receiveFin() {
    // Byte code:
    //   0: getstatic okhttp3/internal/http2/Http2Stream.$assertionsDisabled : Z
    //   3: ifne -> 21
    //   6: aload_0
    //   7: invokestatic holdsLock : (Ljava/lang/Object;)Z
    //   10: ifeq -> 21
    //   13: new java/lang/AssertionError
    //   16: dup
    //   17: invokespecial <init> : ()V
    //   20: athrow
    //   21: aload_0
    //   22: monitorenter
    //   23: aload_0
    //   24: getfield source : Lokhttp3/internal/http2/Http2Stream$FramingSource;
    //   27: iconst_1
    //   28: putfield finished : Z
    //   31: aload_0
    //   32: invokevirtual isOpen : ()Z
    //   35: istore_1
    //   36: aload_0
    //   37: invokevirtual notifyAll : ()V
    //   40: aload_0
    //   41: monitorexit
    //   42: iload_1
    //   43: ifne -> 58
    //   46: aload_0
    //   47: getfield connection : Lokhttp3/internal/http2/Http2Connection;
    //   50: aload_0
    //   51: getfield id : I
    //   54: invokevirtual removeStream : (I)Lokhttp3/internal/http2/Http2Stream;
    //   57: pop
    //   58: return
    //   59: astore_2
    //   60: aload_0
    //   61: monitorexit
    //   62: aload_2
    //   63: athrow
    // Exception table:
    //   from	to	target	type
    //   23	42	59	finally
    //   60	62	59	finally
  }
  
  void receiveHeaders(List<Header> paramList) {
    // Byte code:
    //   0: getstatic okhttp3/internal/http2/Http2Stream.$assertionsDisabled : Z
    //   3: ifne -> 21
    //   6: aload_0
    //   7: invokestatic holdsLock : (Ljava/lang/Object;)Z
    //   10: ifeq -> 21
    //   13: new java/lang/AssertionError
    //   16: dup
    //   17: invokespecial <init> : ()V
    //   20: athrow
    //   21: iconst_1
    //   22: istore_2
    //   23: aload_0
    //   24: monitorenter
    //   25: aload_0
    //   26: iconst_1
    //   27: putfield hasResponseHeaders : Z
    //   30: aload_0
    //   31: getfield responseHeaders : Ljava/util/List;
    //   34: ifnonnull -> 70
    //   37: aload_0
    //   38: aload_1
    //   39: putfield responseHeaders : Ljava/util/List;
    //   42: aload_0
    //   43: invokevirtual isOpen : ()Z
    //   46: istore_2
    //   47: aload_0
    //   48: invokevirtual notifyAll : ()V
    //   51: aload_0
    //   52: monitorexit
    //   53: iload_2
    //   54: ifne -> 69
    //   57: aload_0
    //   58: getfield connection : Lokhttp3/internal/http2/Http2Connection;
    //   61: aload_0
    //   62: getfield id : I
    //   65: invokevirtual removeStream : (I)Lokhttp3/internal/http2/Http2Stream;
    //   68: pop
    //   69: return
    //   70: new java/util/ArrayList
    //   73: astore_3
    //   74: aload_3
    //   75: invokespecial <init> : ()V
    //   78: aload_3
    //   79: aload_0
    //   80: getfield responseHeaders : Ljava/util/List;
    //   83: invokeinterface addAll : (Ljava/util/Collection;)Z
    //   88: pop
    //   89: aload_3
    //   90: aconst_null
    //   91: invokeinterface add : (Ljava/lang/Object;)Z
    //   96: pop
    //   97: aload_3
    //   98: aload_1
    //   99: invokeinterface addAll : (Ljava/util/Collection;)Z
    //   104: pop
    //   105: aload_0
    //   106: aload_3
    //   107: putfield responseHeaders : Ljava/util/List;
    //   110: goto -> 51
    //   113: astore_1
    //   114: aload_0
    //   115: monitorexit
    //   116: aload_1
    //   117: athrow
    // Exception table:
    //   from	to	target	type
    //   25	51	113	finally
    //   51	53	113	finally
    //   70	110	113	finally
    //   114	116	113	finally
  }
  
  void receiveRstStream(ErrorCode paramErrorCode) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield errorCode : Lokhttp3/internal/http2/ErrorCode;
    //   6: ifnonnull -> 18
    //   9: aload_0
    //   10: aload_1
    //   11: putfield errorCode : Lokhttp3/internal/http2/ErrorCode;
    //   14: aload_0
    //   15: invokevirtual notifyAll : ()V
    //   18: aload_0
    //   19: monitorexit
    //   20: return
    //   21: astore_1
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_1
    //   25: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	21	finally
  }
  
  public void sendResponseHeaders(List<Header> paramList, boolean paramBoolean) throws IOException {
    // Byte code:
    //   0: getstatic okhttp3/internal/http2/Http2Stream.$assertionsDisabled : Z
    //   3: ifne -> 21
    //   6: aload_0
    //   7: invokestatic holdsLock : (Ljava/lang/Object;)Z
    //   10: ifeq -> 21
    //   13: new java/lang/AssertionError
    //   16: dup
    //   17: invokespecial <init> : ()V
    //   20: athrow
    //   21: aload_1
    //   22: ifnonnull -> 35
    //   25: new java/lang/NullPointerException
    //   28: dup
    //   29: ldc 'responseHeaders == null'
    //   31: invokespecial <init> : (Ljava/lang/String;)V
    //   34: athrow
    //   35: iconst_0
    //   36: istore_3
    //   37: aload_0
    //   38: monitorenter
    //   39: aload_0
    //   40: iconst_1
    //   41: putfield hasResponseHeaders : Z
    //   44: iload_2
    //   45: ifne -> 58
    //   48: aload_0
    //   49: getfield sink : Lokhttp3/internal/http2/Http2Stream$FramingSink;
    //   52: iconst_1
    //   53: putfield finished : Z
    //   56: iconst_1
    //   57: istore_3
    //   58: aload_0
    //   59: monitorexit
    //   60: aload_0
    //   61: getfield connection : Lokhttp3/internal/http2/Http2Connection;
    //   64: aload_0
    //   65: getfield id : I
    //   68: iload_3
    //   69: aload_1
    //   70: invokevirtual writeSynReply : (IZLjava/util/List;)V
    //   73: iload_3
    //   74: ifeq -> 84
    //   77: aload_0
    //   78: getfield connection : Lokhttp3/internal/http2/Http2Connection;
    //   81: invokevirtual flush : ()V
    //   84: return
    //   85: astore_1
    //   86: aload_0
    //   87: monitorexit
    //   88: aload_1
    //   89: athrow
    // Exception table:
    //   from	to	target	type
    //   39	44	85	finally
    //   48	56	85	finally
    //   58	60	85	finally
    //   86	88	85	finally
  }
  
  public List<Header> takeResponseHeaders() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual isLocallyInitiated : ()Z
    //   6: ifne -> 26
    //   9: new java/lang/IllegalStateException
    //   12: astore_1
    //   13: aload_1
    //   14: ldc 'servers cannot read response headers'
    //   16: invokespecial <init> : (Ljava/lang/String;)V
    //   19: aload_1
    //   20: athrow
    //   21: astore_1
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_1
    //   25: athrow
    //   26: aload_0
    //   27: getfield readTimeout : Lokhttp3/internal/http2/Http2Stream$StreamTimeout;
    //   30: invokevirtual enter : ()V
    //   33: aload_0
    //   34: getfield responseHeaders : Ljava/util/List;
    //   37: ifnonnull -> 64
    //   40: aload_0
    //   41: getfield errorCode : Lokhttp3/internal/http2/ErrorCode;
    //   44: ifnonnull -> 64
    //   47: aload_0
    //   48: invokevirtual waitForIo : ()V
    //   51: goto -> 33
    //   54: astore_1
    //   55: aload_0
    //   56: getfield readTimeout : Lokhttp3/internal/http2/Http2Stream$StreamTimeout;
    //   59: invokevirtual exitAndThrowIfTimedOut : ()V
    //   62: aload_1
    //   63: athrow
    //   64: aload_0
    //   65: getfield readTimeout : Lokhttp3/internal/http2/Http2Stream$StreamTimeout;
    //   68: invokevirtual exitAndThrowIfTimedOut : ()V
    //   71: aload_0
    //   72: getfield responseHeaders : Ljava/util/List;
    //   75: astore_1
    //   76: aload_1
    //   77: ifnull -> 89
    //   80: aload_0
    //   81: aconst_null
    //   82: putfield responseHeaders : Ljava/util/List;
    //   85: aload_0
    //   86: monitorexit
    //   87: aload_1
    //   88: areturn
    //   89: new okhttp3/internal/http2/StreamResetException
    //   92: astore_1
    //   93: aload_1
    //   94: aload_0
    //   95: getfield errorCode : Lokhttp3/internal/http2/ErrorCode;
    //   98: invokespecial <init> : (Lokhttp3/internal/http2/ErrorCode;)V
    //   101: aload_1
    //   102: athrow
    // Exception table:
    //   from	to	target	type
    //   2	21	21	finally
    //   26	33	21	finally
    //   33	51	54	finally
    //   55	64	21	finally
    //   64	76	21	finally
    //   80	85	21	finally
    //   89	103	21	finally
  }
  
  void waitForIo() throws InterruptedIOException {
    try {
      wait();
      return;
    } catch (InterruptedException interruptedException) {
      throw new InterruptedIOException();
    } 
  }
  
  public Timeout writeTimeout() {
    return (Timeout)this.writeTimeout;
  }
  
  final class FramingSink implements Sink {
    private static final long EMIT_BUFFER_SIZE = 16384L;
    
    boolean closed;
    
    boolean finished;
    
    private final Buffer sendBuffer = new Buffer();
    
    static {
      boolean bool;
      if (!Http2Stream.class.desiredAssertionStatus()) {
        bool = true;
      } else {
        bool = false;
      } 
      $assertionsDisabled = bool;
    }
    
    private void emitFrame(boolean param1Boolean) throws IOException {
      synchronized (Http2Stream.this) {
        Http2Stream.this.writeTimeout.enter();
      } 
      Http2Stream.this.writeTimeout.exitAndThrowIfTimedOut();
      Http2Stream.this.checkOutNotClosed();
      long l = Math.min(Http2Stream.this.bytesLeftInWriteWindow, this.sendBuffer.size());
      Http2Stream http2Stream = Http2Stream.this;
      http2Stream.bytesLeftInWriteWindow -= l;
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_2} */
      Http2Stream.this.writeTimeout.enter();
      try {
        Http2Connection http2Connection = Http2Stream.this.connection;
        int i = Http2Stream.this.id;
        if (param1Boolean && l == this.sendBuffer.size()) {
          param1Boolean = true;
        } else {
          param1Boolean = false;
        } 
        http2Connection.writeData(i, param1Boolean, this.sendBuffer, l);
        return;
      } finally {
        Http2Stream.this.writeTimeout.exitAndThrowIfTimedOut();
      } 
    }
    
    public void close() throws IOException {
      assert !Thread.holdsLock(Http2Stream.this);
      synchronized (Http2Stream.this) {
        if (this.closed)
          return; 
        if (!Http2Stream.this.sink.finished)
          if (this.sendBuffer.size() > 0L) {
            while (this.sendBuffer.size() > 0L)
              emitFrame(true); 
          } else {
            Http2Stream.this.connection.writeData(Http2Stream.this.id, true, null, 0L);
          }  
      } 
      synchronized (Http2Stream.this) {
        this.closed = true;
        Http2Stream.this.connection.flush();
        Http2Stream.this.cancelStreamIfNecessary();
        return;
      } 
    }
    
    public void flush() throws IOException {
      assert !Thread.holdsLock(Http2Stream.this);
      synchronized (Http2Stream.this) {
        Http2Stream.this.checkOutNotClosed();
        while (this.sendBuffer.size() > 0L) {
          emitFrame(false);
          Http2Stream.this.connection.flush();
        } 
      } 
    }
    
    public Timeout timeout() {
      return (Timeout)Http2Stream.this.writeTimeout;
    }
    
    public void write(Buffer param1Buffer, long param1Long) throws IOException {
      assert !Thread.holdsLock(Http2Stream.this);
      this.sendBuffer.write(param1Buffer, param1Long);
      while (this.sendBuffer.size() >= 16384L)
        emitFrame(false); 
    }
  }
  
  private final class FramingSource implements Source {
    boolean closed;
    
    boolean finished;
    
    private final long maxByteCount;
    
    private final Buffer readBuffer = new Buffer();
    
    private final Buffer receiveBuffer = new Buffer();
    
    static {
      boolean bool;
      if (!Http2Stream.class.desiredAssertionStatus()) {
        bool = true;
      } else {
        bool = false;
      } 
      $assertionsDisabled = bool;
    }
    
    FramingSource(long param1Long) {
      this.maxByteCount = param1Long;
    }
    
    private void checkNotClosed() throws IOException {
      if (this.closed)
        throw new IOException("stream closed"); 
      if (Http2Stream.this.errorCode != null)
        throw new StreamResetException(Http2Stream.this.errorCode); 
    }
    
    private void waitUntilReadable() throws IOException {
      Http2Stream.this.readTimeout.enter();
      try {
        while (this.readBuffer.size() == 0L && !this.finished && !this.closed && Http2Stream.this.errorCode == null)
          Http2Stream.this.waitForIo(); 
      } finally {
        Http2Stream.this.readTimeout.exitAndThrowIfTimedOut();
      } 
    }
    
    public void close() throws IOException {
      synchronized (Http2Stream.this) {
        this.closed = true;
        this.readBuffer.clear();
        Http2Stream.this.notifyAll();
        Http2Stream.this.cancelStreamIfNecessary();
        return;
      } 
    }
    
    public long read(Buffer param1Buffer, long param1Long) throws IOException {
      if (param1Long < 0L)
        throw new IllegalArgumentException("byteCount < 0: " + param1Long); 
      synchronized (Http2Stream.this) {
        waitUntilReadable();
        checkNotClosed();
        if (this.readBuffer.size() == 0L) {
          param1Long = -1L;
          return param1Long;
        } 
        param1Long = this.readBuffer.read(param1Buffer, Math.min(param1Long, this.readBuffer.size()));
        Http2Stream http2Stream = Http2Stream.this;
        http2Stream.unacknowledgedBytesRead += param1Long;
        if (Http2Stream.this.unacknowledgedBytesRead >= (Http2Stream.this.connection.okHttpSettings.getInitialWindowSize() / 2)) {
          Http2Stream.this.connection.writeWindowUpdateLater(Http2Stream.this.id, Http2Stream.this.unacknowledgedBytesRead);
          Http2Stream.this.unacknowledgedBytesRead = 0L;
        } 
        Http2Connection http2Connection = Http2Stream.this.connection;
        /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{okhttp3/internal/http2/Http2Connection}, name=null} */
        try {
          Http2Connection http2Connection1 = Http2Stream.this.connection;
          http2Connection1.unacknowledgedBytesRead += param1Long;
          if (Http2Stream.this.connection.unacknowledgedBytesRead >= (Http2Stream.this.connection.okHttpSettings.getInitialWindowSize() / 2)) {
            Http2Stream.this.connection.writeWindowUpdateLater(0, Http2Stream.this.connection.unacknowledgedBytesRead);
            Http2Stream.this.connection.unacknowledgedBytesRead = 0L;
          } 
          /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{okhttp3/internal/http2/Http2Connection}, name=null} */
        } finally {}
        return param1Long;
      } 
    }
    
    void receive(BufferedSource param1BufferedSource, long param1Long) throws IOException {
      long l = param1Long;
      assert false;
      while (true) {
        boolean bool;
        if (l > 0L) {
          synchronized (Http2Stream.this) {
            boolean bool1;
            bool = this.finished;
            if (this.readBuffer.size() + l > this.maxByteCount) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            if (bool1) {
              param1BufferedSource.skip(l);
              Http2Stream.this.closeLater(ErrorCode.FLOW_CONTROL_ERROR);
              return;
            } 
          } 
        } else {
          return;
        } 
        if (bool) {
          param1BufferedSource.skip(l);
          return;
        } 
        param1Long = param1BufferedSource.read(this.receiveBuffer, l);
        if (param1Long == -1L)
          throw new EOFException(); 
        l -= param1Long;
        synchronized (Http2Stream.this) {
          boolean bool1;
          if (this.readBuffer.size() == 0L) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          this.readBuffer.writeAll((Source)this.receiveBuffer);
          if (bool1)
            Http2Stream.this.notifyAll(); 
        } 
      } 
    }
    
    public Timeout timeout() {
      return (Timeout)Http2Stream.this.readTimeout;
    }
  }
  
  class StreamTimeout extends AsyncTimeout {
    public void exitAndThrowIfTimedOut() throws IOException {
      if (exit())
        throw newTimeoutException(null); 
    }
    
    protected IOException newTimeoutException(IOException param1IOException) {
      SocketTimeoutException socketTimeoutException = new SocketTimeoutException("timeout");
      if (param1IOException != null)
        socketTimeoutException.initCause(param1IOException); 
      return socketTimeoutException;
    }
    
    protected void timedOut() {
      Http2Stream.this.closeLater(ErrorCode.CANCEL);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/http2/Http2Stream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */