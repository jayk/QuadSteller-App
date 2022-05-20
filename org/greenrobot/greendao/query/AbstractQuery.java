package org.greenrobot.greendao.query;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.InternalQueryDaoAccess;

abstract class AbstractQuery<T> {
  protected final AbstractDao<T, ?> dao;
  
  protected final InternalQueryDaoAccess<T> daoAccess;
  
  protected final Thread ownerThread;
  
  protected final String[] parameters;
  
  protected final String sql;
  
  protected AbstractQuery(AbstractDao<T, ?> paramAbstractDao, String paramString, String[] paramArrayOfString) {
    this.dao = paramAbstractDao;
    this.daoAccess = new InternalQueryDaoAccess(paramAbstractDao);
    this.sql = paramString;
    this.parameters = paramArrayOfString;
    this.ownerThread = Thread.currentThread();
  }
  
  protected static String[] toStringArray(Object[] paramArrayOfObject) {
    int i = paramArrayOfObject.length;
    String[] arrayOfString = new String[i];
    for (byte b = 0; b < i; b++) {
      Object object = paramArrayOfObject[b];
      if (object != null) {
        arrayOfString[b] = object.toString();
      } else {
        arrayOfString[b] = null;
      } 
    } 
    return arrayOfString;
  }
  
  protected void checkThread() {
    if (Thread.currentThread() != this.ownerThread)
      throw new DaoException("Method may be called only in owner thread, use forCurrentThread to get an instance for this thread"); 
  }
  
  public AbstractQuery<T> setParameter(int paramInt, Object paramObject) {
    checkThread();
    if (paramObject != null) {
      this.parameters[paramInt] = paramObject.toString();
      return this;
    } 
    this.parameters[paramInt] = null;
    return this;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/query/AbstractQuery.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */