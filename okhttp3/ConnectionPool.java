package okhttp3;

import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.RouteDatabase;
import okhttp3.internal.connection.StreamAllocation;

public final class ConnectionPool {
  private static final Executor executor = new ThreadPoolExecutor(0, 2147483647, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp ConnectionPool", true));
  
  private final Runnable cleanupRunnable = new Runnable() {
      public void run() {
        while (true) {
          long l = ConnectionPool.this.cleanup(System.nanoTime());
          if (l == -1L)
            return; 
          if (l > 0L) {
            long l1 = l / 1000000L;
            synchronized (ConnectionPool.this) {
              ConnectionPool.this.wait(l1, (int)(l - l1 * 1000000L));
            } 
            /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_5} */
          } 
        } 
      }
    };
  
  boolean cleanupRunning;
  
  private final Deque<RealConnection> connections = new ArrayDeque<RealConnection>();
  
  private final long keepAliveDurationNs;
  
  private final int maxIdleConnections;
  
  final RouteDatabase routeDatabase = new RouteDatabase();
  
  public ConnectionPool() {
    this(5, 5L, TimeUnit.MINUTES);
  }
  
  public ConnectionPool(int paramInt, long paramLong, TimeUnit paramTimeUnit) {
    this.maxIdleConnections = paramInt;
    this.keepAliveDurationNs = paramTimeUnit.toNanos(paramLong);
    if (paramLong <= 0L)
      throw new IllegalArgumentException("keepAliveDuration <= 0: " + paramLong); 
  }
  
  private int pruneAndGetAllocationCount(RealConnection paramRealConnection, long paramLong) {
    // Byte code:
    //   0: aload_1
    //   1: getfield allocations : Ljava/util/List;
    //   4: astore #4
    //   6: iconst_0
    //   7: istore #5
    //   9: iload #5
    //   11: aload #4
    //   13: invokeinterface size : ()I
    //   18: if_icmpge -> 145
    //   21: aload #4
    //   23: iload #5
    //   25: invokeinterface get : (I)Ljava/lang/Object;
    //   30: checkcast java/lang/ref/Reference
    //   33: astore #6
    //   35: aload #6
    //   37: invokevirtual get : ()Ljava/lang/Object;
    //   40: ifnull -> 49
    //   43: iinc #5, 1
    //   46: goto -> 9
    //   49: aload #6
    //   51: checkcast okhttp3/internal/connection/StreamAllocation$StreamAllocationReference
    //   54: astore #7
    //   56: new java/lang/StringBuilder
    //   59: dup
    //   60: invokespecial <init> : ()V
    //   63: ldc 'A connection to '
    //   65: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   68: aload_1
    //   69: invokevirtual route : ()Lokhttp3/Route;
    //   72: invokevirtual address : ()Lokhttp3/Address;
    //   75: invokevirtual url : ()Lokhttp3/HttpUrl;
    //   78: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   81: ldc ' was leaked. Did you forget to close a response body?'
    //   83: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   86: invokevirtual toString : ()Ljava/lang/String;
    //   89: astore #6
    //   91: invokestatic get : ()Lokhttp3/internal/platform/Platform;
    //   94: aload #6
    //   96: aload #7
    //   98: getfield callStackTrace : Ljava/lang/Object;
    //   101: invokevirtual logCloseableLeak : (Ljava/lang/String;Ljava/lang/Object;)V
    //   104: aload #4
    //   106: iload #5
    //   108: invokeinterface remove : (I)Ljava/lang/Object;
    //   113: pop
    //   114: aload_1
    //   115: iconst_1
    //   116: putfield noNewStreams : Z
    //   119: aload #4
    //   121: invokeinterface isEmpty : ()Z
    //   126: ifeq -> 9
    //   129: aload_1
    //   130: lload_2
    //   131: aload_0
    //   132: getfield keepAliveDurationNs : J
    //   135: lsub
    //   136: putfield idleAtNanos : J
    //   139: iconst_0
    //   140: istore #5
    //   142: iload #5
    //   144: ireturn
    //   145: aload #4
    //   147: invokeinterface size : ()I
    //   152: istore #5
    //   154: goto -> 142
  }
  
  long cleanup(long paramLong) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: iconst_0
    //   3: istore #4
    //   5: aconst_null
    //   6: astore #5
    //   8: ldc2_w -9223372036854775808
    //   11: lstore #6
    //   13: aload_0
    //   14: monitorenter
    //   15: aload_0
    //   16: getfield connections : Ljava/util/Deque;
    //   19: invokeinterface iterator : ()Ljava/util/Iterator;
    //   24: astore #8
    //   26: aload #8
    //   28: invokeinterface hasNext : ()Z
    //   33: ifeq -> 106
    //   36: aload #8
    //   38: invokeinterface next : ()Ljava/lang/Object;
    //   43: checkcast okhttp3/internal/connection/RealConnection
    //   46: astore #9
    //   48: aload_0
    //   49: aload #9
    //   51: lload_1
    //   52: invokespecial pruneAndGetAllocationCount : (Lokhttp3/internal/connection/RealConnection;J)I
    //   55: ifle -> 64
    //   58: iinc #3, 1
    //   61: goto -> 26
    //   64: iload #4
    //   66: iconst_1
    //   67: iadd
    //   68: istore #10
    //   70: lload_1
    //   71: aload #9
    //   73: getfield idleAtNanos : J
    //   76: lsub
    //   77: lstore #11
    //   79: iload #10
    //   81: istore #4
    //   83: lload #11
    //   85: lload #6
    //   87: lcmp
    //   88: ifle -> 26
    //   91: lload #11
    //   93: lstore #6
    //   95: aload #9
    //   97: astore #5
    //   99: iload #10
    //   101: istore #4
    //   103: goto -> 26
    //   106: lload #6
    //   108: aload_0
    //   109: getfield keepAliveDurationNs : J
    //   112: lcmp
    //   113: ifge -> 125
    //   116: iload #4
    //   118: aload_0
    //   119: getfield maxIdleConnections : I
    //   122: if_icmple -> 151
    //   125: aload_0
    //   126: getfield connections : Ljava/util/Deque;
    //   129: aload #5
    //   131: invokeinterface remove : (Ljava/lang/Object;)Z
    //   136: pop
    //   137: aload_0
    //   138: monitorexit
    //   139: aload #5
    //   141: invokevirtual socket : ()Ljava/net/Socket;
    //   144: invokestatic closeQuietly : (Ljava/net/Socket;)V
    //   147: lconst_0
    //   148: lstore_1
    //   149: lload_1
    //   150: lreturn
    //   151: iload #4
    //   153: ifle -> 176
    //   156: aload_0
    //   157: getfield keepAliveDurationNs : J
    //   160: lload #6
    //   162: lsub
    //   163: lstore_1
    //   164: aload_0
    //   165: monitorexit
    //   166: goto -> 149
    //   169: astore #5
    //   171: aload_0
    //   172: monitorexit
    //   173: aload #5
    //   175: athrow
    //   176: iload_3
    //   177: ifle -> 190
    //   180: aload_0
    //   181: getfield keepAliveDurationNs : J
    //   184: lstore_1
    //   185: aload_0
    //   186: monitorexit
    //   187: goto -> 149
    //   190: aload_0
    //   191: iconst_0
    //   192: putfield cleanupRunning : Z
    //   195: ldc2_w -1
    //   198: lstore_1
    //   199: aload_0
    //   200: monitorexit
    //   201: goto -> 149
    // Exception table:
    //   from	to	target	type
    //   15	26	169	finally
    //   26	58	169	finally
    //   70	79	169	finally
    //   106	125	169	finally
    //   125	139	169	finally
    //   156	166	169	finally
    //   171	173	169	finally
    //   180	187	169	finally
    //   190	195	169	finally
    //   199	201	169	finally
  }
  
  boolean connectionBecameIdle(RealConnection paramRealConnection) {
    assert Thread.holdsLock(this);
    if (paramRealConnection.noNewStreams || this.maxIdleConnections == 0) {
      this.connections.remove(paramRealConnection);
      return true;
    } 
    notifyAll();
    return false;
  }
  
  public int connectionCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield connections : Ljava/util/Deque;
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
  
  @Nullable
  Socket deduplicate(Address paramAddress, StreamAllocation paramStreamAllocation) {
    Socket socket;
    RealConnection realConnection = null;
    assert Thread.holdsLock(this);
    Iterator<RealConnection> iterator = this.connections.iterator();
    while (true) {
      RealConnection realConnection1 = realConnection;
      if (iterator.hasNext()) {
        realConnection1 = iterator.next();
        if (realConnection1.isEligible(paramAddress, null) && realConnection1.isMultiplexed() && realConnection1 != paramStreamAllocation.connection()) {
          socket = paramStreamAllocation.releaseAndAcquire(realConnection1);
          break;
        } 
        continue;
      } 
      break;
    } 
    return socket;
  }
  
  public void evictAll() {
    // Byte code:
    //   0: new java/util/ArrayList
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_1
    //   8: aload_0
    //   9: monitorenter
    //   10: aload_0
    //   11: getfield connections : Ljava/util/Deque;
    //   14: invokeinterface iterator : ()Ljava/util/Iterator;
    //   19: astore_2
    //   20: aload_2
    //   21: invokeinterface hasNext : ()Z
    //   26: ifeq -> 78
    //   29: aload_2
    //   30: invokeinterface next : ()Ljava/lang/Object;
    //   35: checkcast okhttp3/internal/connection/RealConnection
    //   38: astore_3
    //   39: aload_3
    //   40: getfield allocations : Ljava/util/List;
    //   43: invokeinterface isEmpty : ()Z
    //   48: ifeq -> 20
    //   51: aload_3
    //   52: iconst_1
    //   53: putfield noNewStreams : Z
    //   56: aload_1
    //   57: aload_3
    //   58: invokeinterface add : (Ljava/lang/Object;)Z
    //   63: pop
    //   64: aload_2
    //   65: invokeinterface remove : ()V
    //   70: goto -> 20
    //   73: astore_3
    //   74: aload_0
    //   75: monitorexit
    //   76: aload_3
    //   77: athrow
    //   78: aload_0
    //   79: monitorexit
    //   80: aload_1
    //   81: invokeinterface iterator : ()Ljava/util/Iterator;
    //   86: astore_3
    //   87: aload_3
    //   88: invokeinterface hasNext : ()Z
    //   93: ifeq -> 114
    //   96: aload_3
    //   97: invokeinterface next : ()Ljava/lang/Object;
    //   102: checkcast okhttp3/internal/connection/RealConnection
    //   105: invokevirtual socket : ()Ljava/net/Socket;
    //   108: invokestatic closeQuietly : (Ljava/net/Socket;)V
    //   111: goto -> 87
    //   114: return
    // Exception table:
    //   from	to	target	type
    //   10	20	73	finally
    //   20	70	73	finally
    //   74	76	73	finally
    //   78	80	73	finally
  }
  
  @Nullable
  RealConnection get(Address paramAddress, StreamAllocation paramStreamAllocation, Route paramRoute) {
    // Byte code:
    //   0: getstatic okhttp3/ConnectionPool.$assertionsDisabled : Z
    //   3: ifne -> 21
    //   6: aload_0
    //   7: invokestatic holdsLock : (Ljava/lang/Object;)Z
    //   10: ifne -> 21
    //   13: new java/lang/AssertionError
    //   16: dup
    //   17: invokespecial <init> : ()V
    //   20: athrow
    //   21: aload_0
    //   22: getfield connections : Ljava/util/Deque;
    //   25: invokeinterface iterator : ()Ljava/util/Iterator;
    //   30: astore #4
    //   32: aload #4
    //   34: invokeinterface hasNext : ()Z
    //   39: ifeq -> 75
    //   42: aload #4
    //   44: invokeinterface next : ()Ljava/lang/Object;
    //   49: checkcast okhttp3/internal/connection/RealConnection
    //   52: astore #5
    //   54: aload #5
    //   56: aload_1
    //   57: aload_3
    //   58: invokevirtual isEligible : (Lokhttp3/Address;Lokhttp3/Route;)Z
    //   61: ifeq -> 32
    //   64: aload_2
    //   65: aload #5
    //   67: invokevirtual acquire : (Lokhttp3/internal/connection/RealConnection;)V
    //   70: aload #5
    //   72: astore_1
    //   73: aload_1
    //   74: areturn
    //   75: aconst_null
    //   76: astore_1
    //   77: goto -> 73
  }
  
  public int idleConnectionCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iconst_0
    //   3: istore_1
    //   4: aload_0
    //   5: getfield connections : Ljava/util/Deque;
    //   8: invokeinterface iterator : ()Ljava/util/Iterator;
    //   13: astore_2
    //   14: aload_2
    //   15: invokeinterface hasNext : ()Z
    //   20: ifeq -> 51
    //   23: aload_2
    //   24: invokeinterface next : ()Ljava/lang/Object;
    //   29: checkcast okhttp3/internal/connection/RealConnection
    //   32: getfield allocations : Ljava/util/List;
    //   35: invokeinterface isEmpty : ()Z
    //   40: istore_3
    //   41: iload_3
    //   42: ifeq -> 14
    //   45: iinc #1, 1
    //   48: goto -> 14
    //   51: aload_0
    //   52: monitorexit
    //   53: iload_1
    //   54: ireturn
    //   55: astore_2
    //   56: aload_0
    //   57: monitorexit
    //   58: aload_2
    //   59: athrow
    // Exception table:
    //   from	to	target	type
    //   4	14	55	finally
    //   14	41	55	finally
  }
  
  void put(RealConnection paramRealConnection) {
    assert Thread.holdsLock(this);
    if (!this.cleanupRunning) {
      this.cleanupRunning = true;
      executor.execute(this.cleanupRunnable);
    } 
    this.connections.add(paramRealConnection);
  }
  
  static {
    boolean bool;
    if (!ConnectionPool.class.desiredAssertionStatus()) {
      bool = true;
    } else {
      bool = false;
    } 
    $assertionsDisabled = bool;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/ConnectionPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */