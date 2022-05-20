package okhttp3.internal.connection;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.Socket;
import okhttp3.Address;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Route;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpCodec;

public final class StreamAllocation {
  public final Address address;
  
  private final Object callStackTrace;
  
  private boolean canceled;
  
  private HttpCodec codec;
  
  private RealConnection connection;
  
  private final ConnectionPool connectionPool;
  
  private int refusedStreamCount;
  
  private boolean released;
  
  private Route route;
  
  private final RouteSelector routeSelector;
  
  static {
    boolean bool;
    if (!StreamAllocation.class.desiredAssertionStatus()) {
      bool = true;
    } else {
      bool = false;
    } 
    $assertionsDisabled = bool;
  }
  
  public StreamAllocation(ConnectionPool paramConnectionPool, Address paramAddress, Object paramObject) {
    this.connectionPool = paramConnectionPool;
    this.address = paramAddress;
    this.routeSelector = new RouteSelector(paramAddress, routeDatabase());
    this.callStackTrace = paramObject;
  }
  
  private Socket deallocate(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
    // Byte code:
    //   0: getstatic okhttp3/internal/connection/StreamAllocation.$assertionsDisabled : Z
    //   3: ifne -> 24
    //   6: aload_0
    //   7: getfield connectionPool : Lokhttp3/ConnectionPool;
    //   10: invokestatic holdsLock : (Ljava/lang/Object;)Z
    //   13: ifne -> 24
    //   16: new java/lang/AssertionError
    //   19: dup
    //   20: invokespecial <init> : ()V
    //   23: athrow
    //   24: iload_3
    //   25: ifeq -> 33
    //   28: aload_0
    //   29: aconst_null
    //   30: putfield codec : Lokhttp3/internal/http/HttpCodec;
    //   33: iload_2
    //   34: ifeq -> 42
    //   37: aload_0
    //   38: iconst_1
    //   39: putfield released : Z
    //   42: aconst_null
    //   43: astore #4
    //   45: aconst_null
    //   46: astore #5
    //   48: aload #4
    //   50: astore #6
    //   52: aload_0
    //   53: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   56: ifnull -> 175
    //   59: iload_1
    //   60: ifeq -> 71
    //   63: aload_0
    //   64: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   67: iconst_1
    //   68: putfield noNewStreams : Z
    //   71: aload #4
    //   73: astore #6
    //   75: aload_0
    //   76: getfield codec : Lokhttp3/internal/http/HttpCodec;
    //   79: ifnonnull -> 175
    //   82: aload_0
    //   83: getfield released : Z
    //   86: ifne -> 103
    //   89: aload #4
    //   91: astore #6
    //   93: aload_0
    //   94: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   97: getfield noNewStreams : Z
    //   100: ifeq -> 175
    //   103: aload_0
    //   104: aload_0
    //   105: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   108: invokespecial release : (Lokhttp3/internal/connection/RealConnection;)V
    //   111: aload #5
    //   113: astore #6
    //   115: aload_0
    //   116: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   119: getfield allocations : Ljava/util/List;
    //   122: invokeinterface isEmpty : ()Z
    //   127: ifeq -> 170
    //   130: aload_0
    //   131: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   134: invokestatic nanoTime : ()J
    //   137: putfield idleAtNanos : J
    //   140: aload #5
    //   142: astore #6
    //   144: getstatic okhttp3/internal/Internal.instance : Lokhttp3/internal/Internal;
    //   147: aload_0
    //   148: getfield connectionPool : Lokhttp3/ConnectionPool;
    //   151: aload_0
    //   152: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   155: invokevirtual connectionBecameIdle : (Lokhttp3/ConnectionPool;Lokhttp3/internal/connection/RealConnection;)Z
    //   158: ifeq -> 170
    //   161: aload_0
    //   162: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   165: invokevirtual socket : ()Ljava/net/Socket;
    //   168: astore #6
    //   170: aload_0
    //   171: aconst_null
    //   172: putfield connection : Lokhttp3/internal/connection/RealConnection;
    //   175: aload #6
    //   177: areturn
  }
  
  private RealConnection findConnection(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) throws IOException {
    synchronized (this.connectionPool) {
      if (this.released) {
        IllegalStateException illegalStateException = new IllegalStateException();
        this("released");
        throw illegalStateException;
      } 
    } 
    if (this.codec != null) {
      IllegalStateException illegalStateException = new IllegalStateException();
      this("codec != null");
      throw illegalStateException;
    } 
    if (this.canceled) {
      IOException iOException = new IOException();
      this("Canceled");
      throw iOException;
    } 
    RealConnection realConnection2 = this.connection;
    if (realConnection2 != null && !realConnection2.noNewStreams) {
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_5} */
      return realConnection2;
    } 
    Internal.instance.get(this.connectionPool, this.address, this, null);
    if (this.connection != null) {
      realConnection2 = this.connection;
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_5} */
      return realConnection2;
    } 
    Route route = this.route;
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_5} */
    null = route;
    if (route == null)
      null = this.routeSelector.next(); 
    synchronized (this.connectionPool) {
      if (this.canceled) {
        IOException iOException = new IOException();
        this("Canceled");
        throw iOException;
      } 
    } 
    Internal.instance.get(this.connectionPool, this.address, this, (Route)realConnection2);
    if (this.connection != null) {
      this.route = (Route)realConnection2;
      realConnection2 = this.connection;
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{okhttp3/Route}, name=null} */
      return realConnection2;
    } 
    this.route = (Route)realConnection2;
    this.refusedStreamCount = 0;
    RealConnection realConnection1 = new RealConnection();
    this(this.connectionPool, (Route)realConnection2);
    acquire(realConnection1);
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{okhttp3/Route}, name=null} */
    realConnection1.connect(paramInt1, paramInt2, paramInt3, paramBoolean);
    routeDatabase().connected(realConnection1.route());
    route = null;
    ConnectionPool connectionPool = this.connectionPool;
    /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{okhttp3/ConnectionPool}, name=null} */
    try {
      Socket socket;
      Internal.instance.put(this.connectionPool, realConnection1);
      realConnection2 = realConnection1;
      if (realConnection1.isMultiplexed()) {
        socket = Internal.instance.deduplicate(this.connectionPool, this.address, this);
        realConnection2 = this.connection;
      } 
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{okhttp3/ConnectionPool}, name=null} */
      Util.closeQuietly(socket);
    } finally {}
    return realConnection2;
  }
  
  private RealConnection findHealthyConnection(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2) throws IOException {
    while (true) {
      null = findConnection(paramInt1, paramInt2, paramInt3, paramBoolean1);
      synchronized (this.connectionPool) {
        if (null.successCount == 0)
          return null; 
        if (!null.isHealthy(paramBoolean2)) {
          noNewStreams();
          continue;
        } 
        return null;
      } 
    } 
  }
  
  private void release(RealConnection paramRealConnection) {
    byte b = 0;
    int i = paramRealConnection.allocations.size();
    while (b < i) {
      if (((Reference<StreamAllocation>)paramRealConnection.allocations.get(b)).get() == this) {
        paramRealConnection.allocations.remove(b);
        return;
      } 
      b++;
    } 
    throw new IllegalStateException();
  }
  
  private RouteDatabase routeDatabase() {
    return Internal.instance.routeDatabase(this.connectionPool);
  }
  
  public void acquire(RealConnection paramRealConnection) {
    assert Thread.holdsLock(this.connectionPool);
    if (this.connection != null)
      throw new IllegalStateException(); 
    this.connection = paramRealConnection;
    paramRealConnection.allocations.add(new StreamAllocationReference(this, this.callStackTrace));
  }
  
  public void cancel() {
    synchronized (this.connectionPool) {
      this.canceled = true;
      HttpCodec httpCodec = this.codec;
      RealConnection realConnection = this.connection;
      if (httpCodec != null) {
        httpCodec.cancel();
        return;
      } 
    } 
    if (SYNTHETIC_LOCAL_VARIABLE_3 != null)
      SYNTHETIC_LOCAL_VARIABLE_3.cancel(); 
  }
  
  public HttpCodec codec() {
    synchronized (this.connectionPool) {
      return this.codec;
    } 
  }
  
  public RealConnection connection() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield connection : Lokhttp3/internal/connection/RealConnection;
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
  
  public boolean hasMoreRoutes() {
    return (this.route != null || this.routeSelector.hasNext());
  }
  
  public HttpCodec newStream(OkHttpClient paramOkHttpClient, boolean paramBoolean) {
    int i = paramOkHttpClient.connectTimeoutMillis();
    int j = paramOkHttpClient.readTimeoutMillis();
    int k = paramOkHttpClient.writeTimeoutMillis();
    boolean bool = paramOkHttpClient.retryOnConnectionFailure();
    try {
      null = findHealthyConnection(i, j, k, bool, paramBoolean).newCodec(paramOkHttpClient, this);
      synchronized (this.connectionPool) {
        this.codec = null;
        return null;
      } 
    } catch (IOException iOException) {
      throw new RouteException(iOException);
    } 
  }
  
  public void noNewStreams() {
    synchronized (this.connectionPool) {
      Socket socket = deallocate(true, false, false);
      Util.closeQuietly(socket);
      return;
    } 
  }
  
  public void release() {
    synchronized (this.connectionPool) {
      Socket socket = deallocate(false, true, false);
      Util.closeQuietly(socket);
      return;
    } 
  }
  
  public Socket releaseAndAcquire(RealConnection paramRealConnection) {
    assert Thread.holdsLock(this.connectionPool);
    if (this.codec != null || this.connection.allocations.size() != 1)
      throw new IllegalStateException(); 
    Reference<StreamAllocation> reference = this.connection.allocations.get(0);
    Socket socket = deallocate(true, false, false);
    this.connection = paramRealConnection;
    paramRealConnection.allocations.add(reference);
    return socket;
  }
  
  public void streamFailed(IOException paramIOException) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: aload_0
    //   3: getfield connectionPool : Lokhttp3/ConnectionPool;
    //   6: astore_3
    //   7: aload_3
    //   8: monitorenter
    //   9: aload_1
    //   10: instanceof okhttp3/internal/http2/StreamResetException
    //   13: ifeq -> 86
    //   16: aload_1
    //   17: checkcast okhttp3/internal/http2/StreamResetException
    //   20: astore_1
    //   21: aload_1
    //   22: getfield errorCode : Lokhttp3/internal/http2/ErrorCode;
    //   25: getstatic okhttp3/internal/http2/ErrorCode.REFUSED_STREAM : Lokhttp3/internal/http2/ErrorCode;
    //   28: if_acmpne -> 41
    //   31: aload_0
    //   32: aload_0
    //   33: getfield refusedStreamCount : I
    //   36: iconst_1
    //   37: iadd
    //   38: putfield refusedStreamCount : I
    //   41: aload_1
    //   42: getfield errorCode : Lokhttp3/internal/http2/ErrorCode;
    //   45: getstatic okhttp3/internal/http2/ErrorCode.REFUSED_STREAM : Lokhttp3/internal/http2/ErrorCode;
    //   48: if_acmpne -> 62
    //   51: iload_2
    //   52: istore #4
    //   54: aload_0
    //   55: getfield refusedStreamCount : I
    //   58: iconst_1
    //   59: if_icmple -> 70
    //   62: iconst_1
    //   63: istore #4
    //   65: aload_0
    //   66: aconst_null
    //   67: putfield route : Lokhttp3/Route;
    //   70: aload_0
    //   71: iload #4
    //   73: iconst_0
    //   74: iconst_1
    //   75: invokespecial deallocate : (ZZZ)Ljava/net/Socket;
    //   78: astore_1
    //   79: aload_3
    //   80: monitorexit
    //   81: aload_1
    //   82: invokestatic closeQuietly : (Ljava/net/Socket;)V
    //   85: return
    //   86: iload_2
    //   87: istore #4
    //   89: aload_0
    //   90: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   93: ifnull -> 70
    //   96: aload_0
    //   97: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   100: invokevirtual isMultiplexed : ()Z
    //   103: ifeq -> 116
    //   106: iload_2
    //   107: istore #4
    //   109: aload_1
    //   110: instanceof okhttp3/internal/http2/ConnectionShutdownException
    //   113: ifeq -> 70
    //   116: iconst_1
    //   117: istore_2
    //   118: iload_2
    //   119: istore #4
    //   121: aload_0
    //   122: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   125: getfield successCount : I
    //   128: ifne -> 70
    //   131: aload_0
    //   132: getfield route : Lokhttp3/Route;
    //   135: ifnull -> 154
    //   138: aload_1
    //   139: ifnull -> 154
    //   142: aload_0
    //   143: getfield routeSelector : Lokhttp3/internal/connection/RouteSelector;
    //   146: aload_0
    //   147: getfield route : Lokhttp3/Route;
    //   150: aload_1
    //   151: invokevirtual connectFailed : (Lokhttp3/Route;Ljava/io/IOException;)V
    //   154: aload_0
    //   155: aconst_null
    //   156: putfield route : Lokhttp3/Route;
    //   159: iload_2
    //   160: istore #4
    //   162: goto -> 70
    //   165: astore_1
    //   166: aload_3
    //   167: monitorexit
    //   168: aload_1
    //   169: athrow
    // Exception table:
    //   from	to	target	type
    //   9	41	165	finally
    //   41	51	165	finally
    //   54	62	165	finally
    //   65	70	165	finally
    //   70	81	165	finally
    //   89	106	165	finally
    //   109	116	165	finally
    //   121	138	165	finally
    //   142	154	165	finally
    //   154	159	165	finally
    //   166	168	165	finally
  }
  
  public void streamFinished(boolean paramBoolean, HttpCodec paramHttpCodec) {
    // Byte code:
    //   0: aload_0
    //   1: getfield connectionPool : Lokhttp3/ConnectionPool;
    //   4: astore_3
    //   5: aload_3
    //   6: monitorenter
    //   7: aload_2
    //   8: ifnull -> 19
    //   11: aload_2
    //   12: aload_0
    //   13: getfield codec : Lokhttp3/internal/http/HttpCodec;
    //   16: if_acmpeq -> 75
    //   19: new java/lang/IllegalStateException
    //   22: astore #4
    //   24: new java/lang/StringBuilder
    //   27: astore #5
    //   29: aload #5
    //   31: invokespecial <init> : ()V
    //   34: aload #4
    //   36: aload #5
    //   38: ldc_w 'expected '
    //   41: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   44: aload_0
    //   45: getfield codec : Lokhttp3/internal/http/HttpCodec;
    //   48: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   51: ldc_w ' but was '
    //   54: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   57: aload_2
    //   58: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   61: invokevirtual toString : ()Ljava/lang/String;
    //   64: invokespecial <init> : (Ljava/lang/String;)V
    //   67: aload #4
    //   69: athrow
    //   70: astore_2
    //   71: aload_3
    //   72: monitorexit
    //   73: aload_2
    //   74: athrow
    //   75: iload_1
    //   76: ifne -> 94
    //   79: aload_0
    //   80: getfield connection : Lokhttp3/internal/connection/RealConnection;
    //   83: astore_2
    //   84: aload_2
    //   85: aload_2
    //   86: getfield successCount : I
    //   89: iconst_1
    //   90: iadd
    //   91: putfield successCount : I
    //   94: aload_0
    //   95: iload_1
    //   96: iconst_0
    //   97: iconst_1
    //   98: invokespecial deallocate : (ZZZ)Ljava/net/Socket;
    //   101: astore_2
    //   102: aload_3
    //   103: monitorexit
    //   104: aload_2
    //   105: invokestatic closeQuietly : (Ljava/net/Socket;)V
    //   108: return
    // Exception table:
    //   from	to	target	type
    //   11	19	70	finally
    //   19	70	70	finally
    //   71	73	70	finally
    //   79	94	70	finally
    //   94	104	70	finally
  }
  
  public String toString() {
    RealConnection realConnection = connection();
    return (realConnection != null) ? realConnection.toString() : this.address.toString();
  }
  
  public static final class StreamAllocationReference extends WeakReference<StreamAllocation> {
    public final Object callStackTrace;
    
    StreamAllocationReference(StreamAllocation param1StreamAllocation, Object param1Object) {
      super(param1StreamAllocation);
      this.callStackTrace = param1Object;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/connection/StreamAllocation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */