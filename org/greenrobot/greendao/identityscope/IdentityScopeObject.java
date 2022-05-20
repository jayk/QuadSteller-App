package org.greenrobot.greendao.identityscope;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class IdentityScopeObject<K, T> implements IdentityScope<K, T> {
  private final ReentrantLock lock = new ReentrantLock();
  
  private final HashMap<K, Reference<T>> map = new HashMap<K, Reference<T>>();
  
  public void clear() {
    this.lock.lock();
    try {
      this.map.clear();
      return;
    } finally {
      this.lock.unlock();
    } 
  }
  
  public boolean detach(K paramK, T paramT) {
    this.lock.lock();
    try {
      if (get(paramK) == paramT && paramT != null) {
        remove(paramK);
        return true;
      } 
      return false;
    } finally {
      this.lock.unlock();
    } 
  }
  
  public T get(K paramK) {
    this.lock.lock();
    try {
      Reference<Reference> reference = (Reference)this.map.get(paramK);
      this.lock.unlock();
    } finally {
      this.lock.unlock();
    } 
    return null;
  }
  
  public T getNoLock(K paramK) {
    null = (Reference)this.map.get(paramK);
    return (T)((null != null) ? null.get() : null);
  }
  
  public void lock() {
    this.lock.lock();
  }
  
  public void put(K paramK, T paramT) {
    this.lock.lock();
    try {
      HashMap<K, Reference<T>> hashMap = this.map;
      WeakReference<T> weakReference = new WeakReference();
      this(paramT);
      hashMap.put(paramK, weakReference);
      return;
    } finally {
      this.lock.unlock();
    } 
  }
  
  public void putNoLock(K paramK, T paramT) {
    this.map.put(paramK, new WeakReference<T>(paramT));
  }
  
  public void remove(Iterable<K> paramIterable) {
    // Byte code:
    //   0: aload_0
    //   1: getfield lock : Ljava/util/concurrent/locks/ReentrantLock;
    //   4: invokevirtual lock : ()V
    //   7: aload_1
    //   8: invokeinterface iterator : ()Ljava/util/Iterator;
    //   13: astore_1
    //   14: aload_1
    //   15: invokeinterface hasNext : ()Z
    //   20: ifeq -> 52
    //   23: aload_1
    //   24: invokeinterface next : ()Ljava/lang/Object;
    //   29: astore_2
    //   30: aload_0
    //   31: getfield map : Ljava/util/HashMap;
    //   34: aload_2
    //   35: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   38: pop
    //   39: goto -> 14
    //   42: astore_1
    //   43: aload_0
    //   44: getfield lock : Ljava/util/concurrent/locks/ReentrantLock;
    //   47: invokevirtual unlock : ()V
    //   50: aload_1
    //   51: athrow
    //   52: aload_0
    //   53: getfield lock : Ljava/util/concurrent/locks/ReentrantLock;
    //   56: invokevirtual unlock : ()V
    //   59: return
    // Exception table:
    //   from	to	target	type
    //   7	14	42	finally
    //   14	39	42	finally
  }
  
  public void remove(K paramK) {
    this.lock.lock();
    try {
      this.map.remove(paramK);
      return;
    } finally {
      this.lock.unlock();
    } 
  }
  
  public void reserveRoom(int paramInt) {}
  
  public void unlock() {
    this.lock.unlock();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/identityscope/IdentityScopeObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */