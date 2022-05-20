package org.greenrobot.greendao.query;

import android.database.Cursor;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoException;

public class CountQuery<T> extends AbstractQuery<T> {
  private final QueryData<T> queryData;
  
  private CountQuery(QueryData<T> paramQueryData, AbstractDao<T, ?> paramAbstractDao, String paramString, String[] paramArrayOfString) {
    super(paramAbstractDao, paramString, paramArrayOfString);
    this.queryData = paramQueryData;
  }
  
  static <T2> CountQuery<T2> create(AbstractDao<T2, ?> paramAbstractDao, String paramString, Object[] paramArrayOfObject) {
    return (new QueryData<T2>(paramAbstractDao, paramString, toStringArray(paramArrayOfObject))).forCurrentThread();
  }
  
  public long count() {
    checkThread();
    Cursor cursor = this.dao.getDatabase().rawQuery(this.sql, this.parameters);
    try {
      if (!cursor.moveToNext()) {
        DaoException daoException = new DaoException();
        this("No result for count");
        throw daoException;
      } 
    } finally {
      cursor.close();
    } 
    if (cursor.getColumnCount() != 1) {
      DaoException daoException = new DaoException();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      this(stringBuilder.append("Unexpected column count: ").append(cursor.getColumnCount()).toString());
      throw daoException;
    } 
    long l = cursor.getLong(0);
    cursor.close();
    return l;
  }
  
  public CountQuery<T> forCurrentThread() {
    return this.queryData.forCurrentThread(this);
  }
  
  private static final class QueryData<T2> extends AbstractQueryData<T2, CountQuery<T2>> {
    private QueryData(AbstractDao<T2, ?> param1AbstractDao, String param1String, String[] param1ArrayOfString) {
      super(param1AbstractDao, param1String, param1ArrayOfString);
    }
    
    protected CountQuery<T2> createQuery() {
      return new CountQuery<T2>(this, this.dao, this.sql, (String[])this.initialValues.clone());
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/query/CountQuery.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */