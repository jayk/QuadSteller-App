package org.greenrobot.greendao.query;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;

public class DeleteQuery<T> extends AbstractQuery<T> {
  private final QueryData<T> queryData;
  
  private DeleteQuery(QueryData<T> paramQueryData, AbstractDao<T, ?> paramAbstractDao, String paramString, String[] paramArrayOfString) {
    super(paramAbstractDao, paramString, paramArrayOfString);
    this.queryData = paramQueryData;
  }
  
  static <T2> DeleteQuery<T2> create(AbstractDao<T2, ?> paramAbstractDao, String paramString, Object[] paramArrayOfObject) {
    return (new QueryData<T2>(paramAbstractDao, paramString, toStringArray(paramArrayOfObject))).forCurrentThread();
  }
  
  public void executeDeleteWithoutDetachingEntities() {
    checkThread();
    Database database = this.dao.getDatabase();
    if (database.isDbLockedByCurrentThread()) {
      this.dao.getDatabase().execSQL(this.sql, (Object[])this.parameters);
      return;
    } 
    database.beginTransaction();
    try {
      this.dao.getDatabase().execSQL(this.sql, (Object[])this.parameters);
      database.setTransactionSuccessful();
      return;
    } finally {
      database.endTransaction();
    } 
  }
  
  public DeleteQuery<T> forCurrentThread() {
    return this.queryData.forCurrentThread(this);
  }
  
  private static final class QueryData<T2> extends AbstractQueryData<T2, DeleteQuery<T2>> {
    private QueryData(AbstractDao<T2, ?> param1AbstractDao, String param1String, String[] param1ArrayOfString) {
      super(param1AbstractDao, param1String, param1ArrayOfString);
    }
    
    protected DeleteQuery<T2> createQuery() {
      return new DeleteQuery<T2>(this, this.dao, this.sql, (String[])this.initialValues.clone());
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/query/DeleteQuery.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */