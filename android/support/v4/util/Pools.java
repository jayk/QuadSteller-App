package android.support.v4.util;

public final class Pools {
  public static interface Pool<T> {
    T acquire();
    
    boolean release(T param1T);
  }
  
  public static class SimplePool<T> implements Pool<T> {
    private final Object[] mPool;
    
    private int mPoolSize;
    
    public SimplePool(int param1Int) {
      if (param1Int <= 0)
        throw new IllegalArgumentException("The max pool size must be > 0"); 
      this.mPool = new Object[param1Int];
    }
    
    private boolean isInPool(T param1T) {
      // Byte code:
      //   0: iconst_0
      //   1: istore_2
      //   2: iload_2
      //   3: aload_0
      //   4: getfield mPoolSize : I
      //   7: if_icmpge -> 30
      //   10: aload_0
      //   11: getfield mPool : [Ljava/lang/Object;
      //   14: iload_2
      //   15: aaload
      //   16: aload_1
      //   17: if_acmpne -> 24
      //   20: iconst_1
      //   21: istore_3
      //   22: iload_3
      //   23: ireturn
      //   24: iinc #2, 1
      //   27: goto -> 2
      //   30: iconst_0
      //   31: istore_3
      //   32: goto -> 22
    }
    
    public T acquire() {
      if (this.mPoolSize > 0) {
        int i = this.mPoolSize - 1;
        Object object = this.mPool[i];
        this.mPool[i] = null;
        this.mPoolSize--;
        return (T)object;
      } 
      return null;
    }
    
    public boolean release(T param1T) {
      if (isInPool(param1T))
        throw new IllegalStateException("Already in the pool!"); 
      if (this.mPoolSize < this.mPool.length) {
        this.mPool[this.mPoolSize] = param1T;
        this.mPoolSize++;
        return true;
      } 
      return false;
    }
  }
  
  public static class SynchronizedPool<T> extends SimplePool<T> {
    private final Object mLock = new Object();
    
    public SynchronizedPool(int param1Int) {
      super(param1Int);
    }
    
    public T acquire() {
      synchronized (this.mLock) {
        return super.acquire();
      } 
    }
    
    public boolean release(T param1T) {
      synchronized (this.mLock) {
        return super.release(param1T);
      } 
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/util/Pools.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */