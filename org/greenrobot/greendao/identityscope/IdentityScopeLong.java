package org.greenrobot.greendao.identityscope;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.concurrent.locks.ReentrantLock;
import org.greenrobot.greendao.internal.LongHashMap;

public class IdentityScopeLong<T> implements IdentityScope<Long, T> {
  private final ReentrantLock lock = new ReentrantLock();
  
  private final LongHashMap<Reference<T>> map = new LongHashMap();
  
  public void clear() {
    this.lock.lock();
    try {
      this.map.clear();
      return;
    } finally {
      this.lock.unlock();
    } 
  }
  
  public boolean detach(Long paramLong, T paramT) {
    this.lock.lock();
    try {
      if (get(paramLong) == paramT && paramT != null) {
        remove(paramLong);
        return true;
      } 
      return false;
    } finally {
      this.lock.unlock();
    } 
  }
  
  public T get(Long paramLong) {
    return get2(paramLong.longValue());
  }
  
  public T get2(long paramLong) {
    this.lock.lock();
    try {
      Reference<Reference> reference = (Reference)this.map.get(paramLong);
      this.lock.unlock();
    } finally {
      this.lock.unlock();
    } 
    return null;
  }
  
  public T get2NoLock(long paramLong) {
    null = (Reference)this.map.get(paramLong);
    return (T)((null != null) ? null.get() : null);
  }
  
  public T getNoLock(Long paramLong) {
    return get2NoLock(paramLong.longValue());
  }
  
  public void lock() {
    this.lock.lock();
  }
  
  public void put(Long paramLong, T paramT) {
    put2(paramLong.longValue(), paramT);
  }
  
  public void put2(long paramLong, T paramT) {
    this.lock.lock();
    try {
      LongHashMap<Reference<T>> longHashMap = this.map;
      WeakReference weakReference = new WeakReference();
      this(paramT);
      longHashMap.put(paramLong, weakReference);
      return;
    } finally {
      this.lock.unlock();
    } 
  }
  
  public void put2NoLock(long paramLong, T paramT) {
    this.map.put(paramLong, new WeakReference<T>(paramT));
  }
  
  public void putNoLock(Long paramLong, T paramT) {
    put2NoLock(paramLong.longValue(), paramT);
  }
  
  public void remove(Iterable<Long> paramIterable) {
    this.lock.lock();
    try {
    
    } finally {
      this.lock.unlock();
    } 
    this.lock.unlock();
  }
  
  public void remove(Long paramLong) {
    this.lock.lock();
    try {
      this.map.remove(paramLong.longValue());
      return;
    } finally {
      this.lock.unlock();
    } 
  }
  
  public void reserveRoom(int paramInt) {
    this.map.reserveRoom(paramInt);
  }
  
  public void unlock() {
    this.lock.unlock();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/identityscope/IdentityScopeLong.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */