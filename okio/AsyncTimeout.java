package okio;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public class AsyncTimeout extends Timeout {
  private static final long IDLE_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(60L);
  
  private static final long IDLE_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(IDLE_TIMEOUT_MILLIS);
  
  private static final int TIMEOUT_WRITE_SIZE = 65536;
  
  @Nullable
  static AsyncTimeout head;
  
  private boolean inQueue;
  
  @Nullable
  private AsyncTimeout next;
  
  private long timeoutAt;
  
  @Nullable
  static AsyncTimeout awaitTimeout() throws InterruptedException {
    AsyncTimeout asyncTimeout1 = null;
    AsyncTimeout asyncTimeout2 = head.next;
    if (asyncTimeout2 == null) {
      long l1 = System.nanoTime();
      AsyncTimeout.class.wait(IDLE_TIMEOUT_MILLIS);
      asyncTimeout2 = asyncTimeout1;
      if (head.next == null) {
        asyncTimeout2 = asyncTimeout1;
        if (System.nanoTime() - l1 >= IDLE_TIMEOUT_NANOS)
          asyncTimeout2 = head; 
      } 
      return asyncTimeout2;
    } 
    long l = asyncTimeout2.remainingNanos(System.nanoTime());
    if (l > 0L) {
      long l1 = l / 1000000L;
      AsyncTimeout.class.wait(l1, (int)(l - l1 * 1000000L));
      return asyncTimeout1;
    } 
    head.next = asyncTimeout2.next;
    asyncTimeout2.next = null;
    return asyncTimeout2;
  }
  
  private static boolean cancelScheduledTimeout(AsyncTimeout paramAsyncTimeout) {
    // Byte code:
    //   0: ldc okio/AsyncTimeout
    //   2: monitorenter
    //   3: getstatic okio/AsyncTimeout.head : Lokio/AsyncTimeout;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnull -> 47
    //   11: aload_1
    //   12: getfield next : Lokio/AsyncTimeout;
    //   15: aload_0
    //   16: if_acmpne -> 39
    //   19: aload_1
    //   20: aload_0
    //   21: getfield next : Lokio/AsyncTimeout;
    //   24: putfield next : Lokio/AsyncTimeout;
    //   27: aload_0
    //   28: aconst_null
    //   29: putfield next : Lokio/AsyncTimeout;
    //   32: iconst_0
    //   33: istore_2
    //   34: ldc okio/AsyncTimeout
    //   36: monitorexit
    //   37: iload_2
    //   38: ireturn
    //   39: aload_1
    //   40: getfield next : Lokio/AsyncTimeout;
    //   43: astore_1
    //   44: goto -> 7
    //   47: iconst_1
    //   48: istore_2
    //   49: goto -> 34
    //   52: astore_0
    //   53: ldc okio/AsyncTimeout
    //   55: monitorexit
    //   56: aload_0
    //   57: athrow
    // Exception table:
    //   from	to	target	type
    //   3	7	52	finally
    //   11	32	52	finally
    //   39	44	52	finally
  }
  
  private long remainingNanos(long paramLong) {
    return this.timeoutAt - paramLong;
  }
  
  private static void scheduleTimeout(AsyncTimeout paramAsyncTimeout, long paramLong, boolean paramBoolean) {
    // Byte code:
    //   0: ldc okio/AsyncTimeout
    //   2: monitorenter
    //   3: getstatic okio/AsyncTimeout.head : Lokio/AsyncTimeout;
    //   6: ifnonnull -> 39
    //   9: new okio/AsyncTimeout
    //   12: astore #4
    //   14: aload #4
    //   16: invokespecial <init> : ()V
    //   19: aload #4
    //   21: putstatic okio/AsyncTimeout.head : Lokio/AsyncTimeout;
    //   24: new okio/AsyncTimeout$Watchdog
    //   27: astore #4
    //   29: aload #4
    //   31: invokespecial <init> : ()V
    //   34: aload #4
    //   36: invokevirtual start : ()V
    //   39: invokestatic nanoTime : ()J
    //   42: lstore #5
    //   44: lload_1
    //   45: lconst_0
    //   46: lcmp
    //   47: ifeq -> 139
    //   50: iload_3
    //   51: ifeq -> 139
    //   54: aload_0
    //   55: lload_1
    //   56: aload_0
    //   57: invokevirtual deadlineNanoTime : ()J
    //   60: lload #5
    //   62: lsub
    //   63: invokestatic min : (JJ)J
    //   66: lload #5
    //   68: ladd
    //   69: putfield timeoutAt : J
    //   72: aload_0
    //   73: lload #5
    //   75: invokespecial remainingNanos : (J)J
    //   78: lstore_1
    //   79: getstatic okio/AsyncTimeout.head : Lokio/AsyncTimeout;
    //   82: astore #4
    //   84: aload #4
    //   86: getfield next : Lokio/AsyncTimeout;
    //   89: ifnull -> 107
    //   92: lload_1
    //   93: aload #4
    //   95: getfield next : Lokio/AsyncTimeout;
    //   98: lload #5
    //   100: invokespecial remainingNanos : (J)J
    //   103: lcmp
    //   104: ifge -> 187
    //   107: aload_0
    //   108: aload #4
    //   110: getfield next : Lokio/AsyncTimeout;
    //   113: putfield next : Lokio/AsyncTimeout;
    //   116: aload #4
    //   118: aload_0
    //   119: putfield next : Lokio/AsyncTimeout;
    //   122: aload #4
    //   124: getstatic okio/AsyncTimeout.head : Lokio/AsyncTimeout;
    //   127: if_acmpne -> 135
    //   130: ldc okio/AsyncTimeout
    //   132: invokevirtual notify : ()V
    //   135: ldc okio/AsyncTimeout
    //   137: monitorexit
    //   138: return
    //   139: lload_1
    //   140: lconst_0
    //   141: lcmp
    //   142: ifeq -> 162
    //   145: aload_0
    //   146: lload #5
    //   148: lload_1
    //   149: ladd
    //   150: putfield timeoutAt : J
    //   153: goto -> 72
    //   156: astore_0
    //   157: ldc okio/AsyncTimeout
    //   159: monitorexit
    //   160: aload_0
    //   161: athrow
    //   162: iload_3
    //   163: ifeq -> 177
    //   166: aload_0
    //   167: aload_0
    //   168: invokevirtual deadlineNanoTime : ()J
    //   171: putfield timeoutAt : J
    //   174: goto -> 72
    //   177: new java/lang/AssertionError
    //   180: astore_0
    //   181: aload_0
    //   182: invokespecial <init> : ()V
    //   185: aload_0
    //   186: athrow
    //   187: aload #4
    //   189: getfield next : Lokio/AsyncTimeout;
    //   192: astore #4
    //   194: goto -> 84
    // Exception table:
    //   from	to	target	type
    //   3	39	156	finally
    //   39	44	156	finally
    //   54	72	156	finally
    //   72	84	156	finally
    //   84	107	156	finally
    //   107	135	156	finally
    //   145	153	156	finally
    //   166	174	156	finally
    //   177	187	156	finally
    //   187	194	156	finally
  }
  
  public final void enter() {
    if (this.inQueue)
      throw new IllegalStateException("Unbalanced enter/exit"); 
    long l = timeoutNanos();
    boolean bool = hasDeadline();
    if (l != 0L || bool) {
      this.inQueue = true;
      scheduleTimeout(this, l, bool);
    } 
  }
  
  final IOException exit(IOException paramIOException) throws IOException {
    if (exit())
      paramIOException = newTimeoutException(paramIOException); 
    return paramIOException;
  }
  
  final void exit(boolean paramBoolean) throws IOException {
    if (exit() && paramBoolean)
      throw newTimeoutException(null); 
  }
  
  public final boolean exit() {
    boolean bool = false;
    if (this.inQueue) {
      this.inQueue = false;
      bool = cancelScheduledTimeout(this);
    } 
    return bool;
  }
  
  protected IOException newTimeoutException(@Nullable IOException paramIOException) {
    InterruptedIOException interruptedIOException = new InterruptedIOException("timeout");
    if (paramIOException != null)
      interruptedIOException.initCause(paramIOException); 
    return interruptedIOException;
  }
  
  public final Sink sink(final Sink sink) {
    return new Sink() {
        public void close() throws IOException {
          AsyncTimeout.this.enter();
          try {
            sink.close();
            return;
          } catch (IOException iOException) {
            throw AsyncTimeout.this.exit(iOException);
          } finally {
            AsyncTimeout.this.exit(false);
          } 
        }
        
        public void flush() throws IOException {
          AsyncTimeout.this.enter();
          try {
            sink.flush();
            return;
          } catch (IOException iOException) {
            throw AsyncTimeout.this.exit(iOException);
          } finally {
            AsyncTimeout.this.exit(false);
          } 
        }
        
        public Timeout timeout() {
          return AsyncTimeout.this;
        }
        
        public String toString() {
          return "AsyncTimeout.sink(" + sink + ")";
        }
        
        public void write(Buffer param1Buffer, long param1Long) throws IOException {
          Util.checkOffsetAndCount(param1Buffer.size, 0L, param1Long);
          label21: while (param1Long > 0L) {
            long l = 0L;
            Segment segment = param1Buffer.head;
            while (true) {
              long l1 = l;
              if (l < 65536L) {
                l += (param1Buffer.head.limit - param1Buffer.head.pos);
                if (l >= param1Long) {
                  l1 = param1Long;
                } else {
                  segment = segment.next;
                  continue;
                } 
              } 
              AsyncTimeout.this.enter();
              try {
                sink.write(param1Buffer, l1);
                param1Long -= l1;
                AsyncTimeout.this.exit(true);
              } catch (IOException iOException) {
                throw AsyncTimeout.this.exit(iOException);
              } finally {
                AsyncTimeout.this.exit(false);
              } 
              continue label21;
            } 
          } 
        }
      };
  }
  
  public final Source source(final Source source) {
    return new Source() {
        public void close() throws IOException {
          try {
            source.close();
            return;
          } catch (IOException iOException) {
            throw AsyncTimeout.this.exit(iOException);
          } finally {
            AsyncTimeout.this.exit(false);
          } 
        }
        
        public long read(Buffer param1Buffer, long param1Long) throws IOException {
          AsyncTimeout.this.enter();
          try {
            param1Long = source.read(param1Buffer, param1Long);
            return param1Long;
          } catch (IOException iOException) {
            throw AsyncTimeout.this.exit(iOException);
          } finally {
            AsyncTimeout.this.exit(false);
          } 
        }
        
        public Timeout timeout() {
          return AsyncTimeout.this;
        }
        
        public String toString() {
          return "AsyncTimeout.source(" + source + ")";
        }
      };
  }
  
  protected void timedOut() {}
  
  private static final class Watchdog extends Thread {
    Watchdog() {
      super("Okio Watchdog");
      setDaemon(true);
    }
    
    public void run() {
      // Byte code:
      //   0: ldc okio/AsyncTimeout
      //   2: monitorenter
      //   3: invokestatic awaitTimeout : ()Lokio/AsyncTimeout;
      //   6: astore_1
      //   7: aload_1
      //   8: ifnonnull -> 27
      //   11: ldc okio/AsyncTimeout
      //   13: monitorexit
      //   14: goto -> 0
      //   17: astore_1
      //   18: ldc okio/AsyncTimeout
      //   20: monitorexit
      //   21: aload_1
      //   22: athrow
      //   23: astore_1
      //   24: goto -> 0
      //   27: aload_1
      //   28: getstatic okio/AsyncTimeout.head : Lokio/AsyncTimeout;
      //   31: if_acmpne -> 42
      //   34: aconst_null
      //   35: putstatic okio/AsyncTimeout.head : Lokio/AsyncTimeout;
      //   38: ldc okio/AsyncTimeout
      //   40: monitorexit
      //   41: return
      //   42: ldc okio/AsyncTimeout
      //   44: monitorexit
      //   45: aload_1
      //   46: invokevirtual timedOut : ()V
      //   49: goto -> 0
      // Exception table:
      //   from	to	target	type
      //   0	3	23	java/lang/InterruptedException
      //   3	7	17	finally
      //   11	14	17	finally
      //   18	21	17	finally
      //   21	23	23	java/lang/InterruptedException
      //   27	41	17	finally
      //   42	45	17	finally
      //   45	49	23	java/lang/InterruptedException
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okio/AsyncTimeout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */