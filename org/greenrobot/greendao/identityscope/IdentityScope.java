package org.greenrobot.greendao.identityscope;

public interface IdentityScope<K, T> {
  void clear();
  
  boolean detach(K paramK, T paramT);
  
  T get(K paramK);
  
  T getNoLock(K paramK);
  
  void lock();
  
  void put(K paramK, T paramT);
  
  void putNoLock(K paramK, T paramT);
  
  void remove(Iterable<K> paramIterable);
  
  void remove(K paramK);
  
  void reserveRoom(int paramInt);
  
  void unlock();
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/identityscope/IdentityScope.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */