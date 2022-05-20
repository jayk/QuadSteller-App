package org.greenrobot.greendao.query;

import android.database.Cursor;
import java.util.Date;
import org.greenrobot.greendao.AbstractDao;

public class CursorQuery<T> extends AbstractQueryWithLimit<T> {
  private final QueryData<T> queryData;
  
  private CursorQuery(QueryData<T> paramQueryData, AbstractDao<T, ?> paramAbstractDao, String paramString, String[] paramArrayOfString, int paramInt1, int paramInt2) {
    super(paramAbstractDao, paramString, paramArrayOfString, paramInt1, paramInt2);
    this.queryData = paramQueryData;
  }
  
  static <T2> CursorQuery<T2> create(AbstractDao<T2, ?> paramAbstractDao, String paramString, Object[] paramArrayOfObject, int paramInt1, int paramInt2) {
    return (new QueryData<T2>(paramAbstractDao, paramString, toStringArray(paramArrayOfObject), paramInt1, paramInt2)).forCurrentThread();
  }
  
  public static <T2> CursorQuery<T2> internalCreate(AbstractDao<T2, ?> paramAbstractDao, String paramString, Object[] paramArrayOfObject) {
    return create(paramAbstractDao, paramString, paramArrayOfObject, -1, -1);
  }
  
  public CursorQuery forCurrentThread() {
    return this.queryData.forCurrentThread(this);
  }
  
  public Cursor query() {
    checkThread();
    return this.dao.getDatabase().rawQuery(this.sql, this.parameters);
  }
  
  private static final class QueryData<T2> extends AbstractQueryData<T2, CursorQuery<T2>> {
    private final int limitPosition;
    
    private final int offsetPosition;
    
    QueryData(AbstractDao<T2, ?> param1AbstractDao, String param1String, String[] param1ArrayOfString, int param1Int1, int param1Int2) {
      super(param1AbstractDao, param1String, param1ArrayOfString);
      this.limitPosition = param1Int1;
      this.offsetPosition = param1Int2;
    }
    
    protected CursorQuery<T2> createQuery() {
      return new CursorQuery<T2>(this, this.dao, this.sql, (String[])this.initialValues.clone(), this.limitPosition, this.offsetPosition);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/query/CursorQuery.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */