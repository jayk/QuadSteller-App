package org.greenrobot.greendao.query;

import android.database.Cursor;
import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.apihint.Internal;
import org.greenrobot.greendao.rx.RxQuery;
import rx.schedulers.Schedulers;

public class Query<T> extends AbstractQueryWithLimit<T> {
  private final QueryData<T> queryData;
  
  private volatile RxQuery rxTxIo;
  
  private volatile RxQuery rxTxPlain;
  
  private Query(QueryData<T> paramQueryData, AbstractDao<T, ?> paramAbstractDao, String paramString, String[] paramArrayOfString, int paramInt1, int paramInt2) {
    super(paramAbstractDao, paramString, paramArrayOfString, paramInt1, paramInt2);
    this.queryData = paramQueryData;
  }
  
  static <T2> Query<T2> create(AbstractDao<T2, ?> paramAbstractDao, String paramString, Object[] paramArrayOfObject, int paramInt1, int paramInt2) {
    return (new QueryData<T2>(paramAbstractDao, paramString, toStringArray(paramArrayOfObject), paramInt1, paramInt2)).forCurrentThread();
  }
  
  public static <T2> Query<T2> internalCreate(AbstractDao<T2, ?> paramAbstractDao, String paramString, Object[] paramArrayOfObject) {
    return create(paramAbstractDao, paramString, paramArrayOfObject, -1, -1);
  }
  
  @Internal
  public RxQuery __InternalRx() {
    if (this.rxTxIo == null)
      this.rxTxIo = new RxQuery(this, Schedulers.io()); 
    return this.rxTxIo;
  }
  
  @Internal
  public RxQuery __internalRxPlain() {
    if (this.rxTxPlain == null)
      this.rxTxPlain = new RxQuery(this); 
    return this.rxTxPlain;
  }
  
  public Query<T> forCurrentThread() {
    return this.queryData.forCurrentThread(this);
  }
  
  public List<T> list() {
    checkThread();
    Cursor cursor = this.dao.getDatabase().rawQuery(this.sql, this.parameters);
    return this.daoAccess.loadAllAndCloseCursor(cursor);
  }
  
  public CloseableListIterator<T> listIterator() {
    return listLazyUncached().listIteratorAutoClose();
  }
  
  public LazyList<T> listLazy() {
    checkThread();
    Cursor cursor = this.dao.getDatabase().rawQuery(this.sql, this.parameters);
    return new LazyList<T>(this.daoAccess, cursor, true);
  }
  
  public LazyList<T> listLazyUncached() {
    checkThread();
    Cursor cursor = this.dao.getDatabase().rawQuery(this.sql, this.parameters);
    return new LazyList<T>(this.daoAccess, cursor, false);
  }
  
  public Query<T> setParameter(int paramInt, Boolean paramBoolean) {
    return (Query<T>)super.setParameter(paramInt, paramBoolean);
  }
  
  public Query<T> setParameter(int paramInt, Object paramObject) {
    return (Query<T>)super.setParameter(paramInt, paramObject);
  }
  
  public Query<T> setParameter(int paramInt, Date paramDate) {
    return (Query<T>)super.setParameter(paramInt, paramDate);
  }
  
  public T unique() {
    checkThread();
    Cursor cursor = this.dao.getDatabase().rawQuery(this.sql, this.parameters);
    return (T)this.daoAccess.loadUniqueAndCloseCursor(cursor);
  }
  
  public T uniqueOrThrow() {
    T t = unique();
    if (t == null)
      throw new DaoException("No entity found for query"); 
    return t;
  }
  
  private static final class QueryData<T2> extends AbstractQueryData<T2, Query<T2>> {
    private final int limitPosition;
    
    private final int offsetPosition;
    
    QueryData(AbstractDao<T2, ?> param1AbstractDao, String param1String, String[] param1ArrayOfString, int param1Int1, int param1Int2) {
      super(param1AbstractDao, param1String, param1ArrayOfString);
      this.limitPosition = param1Int1;
      this.offsetPosition = param1Int2;
    }
    
    protected Query<T2> createQuery() {
      return new Query<T2>(this, this.dao, this.sql, (String[])this.initialValues.clone(), this.limitPosition, this.offsetPosition);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/query/Query.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */