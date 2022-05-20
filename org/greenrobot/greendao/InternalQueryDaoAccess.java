package org.greenrobot.greendao;

import android.database.Cursor;
import java.util.List;
import org.greenrobot.greendao.internal.TableStatements;

public final class InternalQueryDaoAccess<T> {
  private final AbstractDao<T, ?> dao;
  
  public InternalQueryDaoAccess(AbstractDao<T, ?> paramAbstractDao) {
    this.dao = paramAbstractDao;
  }
  
  public static <T2> TableStatements getStatements(AbstractDao<T2, ?> paramAbstractDao) {
    return paramAbstractDao.getStatements();
  }
  
  public TableStatements getStatements() {
    return this.dao.getStatements();
  }
  
  public List<T> loadAllAndCloseCursor(Cursor paramCursor) {
    return this.dao.loadAllAndCloseCursor(paramCursor);
  }
  
  public T loadCurrent(Cursor paramCursor, int paramInt, boolean paramBoolean) {
    return this.dao.loadCurrent(paramCursor, paramInt, paramBoolean);
  }
  
  public T loadUniqueAndCloseCursor(Cursor paramCursor) {
    return this.dao.loadUniqueAndCloseCursor(paramCursor);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/InternalQueryDaoAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */