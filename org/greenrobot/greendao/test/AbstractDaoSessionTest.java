package org.greenrobot.greendao.test;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;

public abstract class AbstractDaoSessionTest<T extends AbstractDaoMaster, S extends AbstractDaoSession> extends DbTest {
  protected T daoMaster;
  
  private final Class<T> daoMasterClass;
  
  protected S daoSession;
  
  public AbstractDaoSessionTest(Class<T> paramClass) {
    this(paramClass, true);
  }
  
  public AbstractDaoSessionTest(Class<T> paramClass, boolean paramBoolean) {
    super(paramBoolean);
    this.daoMasterClass = paramClass;
  }
  
  protected void setUp() throws Exception {
    super.setUp();
    try {
      this.daoMaster = (T)this.daoMasterClass.getConstructor(new Class[] { Database.class }).newInstance(new Object[] { this.db });
      this.daoMasterClass.getMethod("createAllTables", new Class[] { Database.class, boolean.class }).invoke(null, new Object[] { this.db, Boolean.valueOf(false) });
      this.daoSession = (S)this.daoMaster.newSession();
      return;
    } catch (Exception exception) {
      throw new RuntimeException("Could not prepare DAO session test", exception);
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/test/AbstractDaoSessionTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */