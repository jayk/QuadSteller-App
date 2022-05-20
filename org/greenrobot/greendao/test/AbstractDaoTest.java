package org.greenrobot.greendao.test;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoLog;
import org.greenrobot.greendao.InternalUnitTestDaoAccess;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScope;

public abstract class AbstractDaoTest<D extends AbstractDao<T, K>, T, K> extends DbTest {
  protected D dao;
  
  protected InternalUnitTestDaoAccess<T, K> daoAccess;
  
  protected final Class<D> daoClass;
  
  protected IdentityScope<K, T> identityScopeForDao;
  
  protected Property pkColumn;
  
  public AbstractDaoTest(Class<D> paramClass) {
    this(paramClass, true);
  }
  
  public AbstractDaoTest(Class<D> paramClass, boolean paramBoolean) {
    super(paramBoolean);
    this.daoClass = paramClass;
  }
  
  protected void clearIdentityScopeIfAny() {
    if (this.identityScopeForDao != null) {
      this.identityScopeForDao.clear();
      DaoLog.d("Identity scope cleared");
      return;
    } 
    DaoLog.d("No identity scope to clear");
  }
  
  protected void logTableDump() {
    logTableDump(this.dao.getTablename());
  }
  
  public void setIdentityScopeBeforeSetUp(IdentityScope<K, T> paramIdentityScope) {
    this.identityScopeForDao = paramIdentityScope;
  }
  
  protected void setUp() throws Exception {
    super.setUp();
    try {
      setUpTableForDao();
      InternalUnitTestDaoAccess<T, K> internalUnitTestDaoAccess = new InternalUnitTestDaoAccess();
      this(this.db, this.daoClass, this.identityScopeForDao);
      this.daoAccess = internalUnitTestDaoAccess;
      this.dao = (D)this.daoAccess.getDao();
      return;
    } catch (Exception exception) {
      throw new RuntimeException("Could not prepare DAO Test", exception);
    } 
  }
  
  protected void setUpTableForDao() throws Exception {
    try {
      this.daoClass.getMethod("createTable", new Class[] { Database.class, boolean.class }).invoke(null, new Object[] { this.db, Boolean.valueOf(false) });
    } catch (NoSuchMethodException noSuchMethodException) {
      DaoLog.i("No createTable method");
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/test/AbstractDaoTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */