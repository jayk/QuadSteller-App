package org.greenrobot.greendao.test;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoLog;

public abstract class AbstractDaoTestLongPk<D extends AbstractDao<T, Long>, T> extends AbstractDaoTestSinglePk<D, T, Long> {
  public AbstractDaoTestLongPk(Class<D> paramClass) {
    super(paramClass);
  }
  
  protected Long createRandomPk() {
    return Long.valueOf(this.random.nextLong());
  }
  
  public void testAssignPk() {
    if (this.daoAccess.isEntityUpdateable()) {
      T t = createEntity((Long)null);
      if (t != null) {
        T t1 = createEntity((Long)null);
        this.dao.insert(t);
        this.dao.insert(t1);
        Long long_1 = (Long)this.daoAccess.getKey(t);
        assertNotNull(long_1);
        Long long_2 = (Long)this.daoAccess.getKey(t1);
        assertNotNull(long_2);
        assertFalse(long_1.equals(long_2));
        assertNotNull(this.dao.load(long_1));
        assertNotNull(this.dao.load(long_2));
        return;
      } 
      DaoLog.d("Skipping testAssignPk for " + this.daoClass + " (createEntity returned null for null key)");
      return;
    } 
    DaoLog.d("Skipping testAssignPk for not updateable " + this.daoClass);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/test/AbstractDaoTestLongPk.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */