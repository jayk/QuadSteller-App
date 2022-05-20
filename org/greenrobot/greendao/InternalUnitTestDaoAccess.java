package org.greenrobot.greendao;

import android.database.Cursor;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScope;
import org.greenrobot.greendao.internal.DaoConfig;

public class InternalUnitTestDaoAccess<T, K> {
  private final AbstractDao<T, K> dao;
  
  public InternalUnitTestDaoAccess(Database paramDatabase, Class<AbstractDao<T, K>> paramClass, IdentityScope<?, ?> paramIdentityScope) throws Exception {
    DaoConfig daoConfig = new DaoConfig(paramDatabase, paramClass);
    daoConfig.setIdentityScope(paramIdentityScope);
    this.dao = paramClass.getConstructor(new Class[] { DaoConfig.class }).newInstance(new Object[] { daoConfig });
  }
  
  public AbstractDao<T, K> getDao() {
    return this.dao;
  }
  
  public K getKey(T paramT) {
    return this.dao.getKey(paramT);
  }
  
  public Property[] getProperties() {
    return this.dao.getProperties();
  }
  
  public boolean isEntityUpdateable() {
    return this.dao.isEntityUpdateable();
  }
  
  public T readEntity(Cursor paramCursor, int paramInt) {
    return this.dao.readEntity(paramCursor, paramInt);
  }
  
  public K readKey(Cursor paramCursor, int paramInt) {
    return this.dao.readKey(paramCursor, paramInt);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/InternalUnitTestDaoAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */