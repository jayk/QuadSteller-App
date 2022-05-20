package org.greenrobot.greendao;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import org.greenrobot.greendao.annotation.apihint.Experimental;
import org.greenrobot.greendao.async.AsyncSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.rx.RxTransaction;
import rx.schedulers.Schedulers;

public class AbstractDaoSession {
  private final Database db;
  
  private final Map<Class<?>, AbstractDao<?, ?>> entityToDao;
  
  private volatile RxTransaction rxTxIo;
  
  private volatile RxTransaction rxTxPlain;
  
  public AbstractDaoSession(Database paramDatabase) {
    this.db = paramDatabase;
    this.entityToDao = new HashMap<Class<?>, AbstractDao<?, ?>>();
  }
  
  public <V> V callInTx(Callable<V> paramCallable) throws Exception {
    this.db.beginTransaction();
    try {
      paramCallable = (Callable<V>)paramCallable.call();
      this.db.setTransactionSuccessful();
      return (V)paramCallable;
    } finally {
      this.db.endTransaction();
    } 
  }
  
  public <V> V callInTxNoException(Callable<V> paramCallable) {
    this.db.beginTransaction();
    try {
      paramCallable = (Callable<V>)paramCallable.call();
      this.db.setTransactionSuccessful();
      return (V)paramCallable;
    } catch (Exception exception) {
      DaoException daoException = new DaoException();
      this("Callable failed", exception);
      throw daoException;
    } finally {
      this.db.endTransaction();
    } 
  }
  
  public <T> void delete(T paramT) {
    getDao((Class)paramT.getClass()).delete(paramT);
  }
  
  public <T> void deleteAll(Class<T> paramClass) {
    getDao((Class)paramClass).deleteAll();
  }
  
  public Collection<AbstractDao<?, ?>> getAllDaos() {
    return Collections.unmodifiableCollection(this.entityToDao.values());
  }
  
  public AbstractDao<?, ?> getDao(Class<? extends Object> paramClass) {
    AbstractDao<?, ?> abstractDao = this.entityToDao.get(paramClass);
    if (abstractDao == null)
      throw new DaoException("No DAO registered for " + paramClass); 
    return abstractDao;
  }
  
  public Database getDatabase() {
    return this.db;
  }
  
  public <T> long insert(T paramT) {
    return getDao((Class)paramT.getClass()).insert(paramT);
  }
  
  public <T> long insertOrReplace(T paramT) {
    return getDao((Class)paramT.getClass()).insertOrReplace(paramT);
  }
  
  public <T, K> T load(Class<T> paramClass, K paramK) {
    return (T)getDao((Class)paramClass).load(paramK);
  }
  
  public <T, K> List<T> loadAll(Class<T> paramClass) {
    return (List)getDao((Class)paramClass).loadAll();
  }
  
  public <T> QueryBuilder<T> queryBuilder(Class<T> paramClass) {
    return (QueryBuilder)getDao((Class)paramClass).queryBuilder();
  }
  
  public <T, K> List<T> queryRaw(Class<T> paramClass, String paramString, String... paramVarArgs) {
    return (List)getDao((Class)paramClass).queryRaw(paramString, paramVarArgs);
  }
  
  public <T> void refresh(T paramT) {
    getDao((Class)paramT.getClass()).refresh(paramT);
  }
  
  protected <T> void registerDao(Class<T> paramClass, AbstractDao<T, ?> paramAbstractDao) {
    this.entityToDao.put(paramClass, paramAbstractDao);
  }
  
  public void runInTx(Runnable paramRunnable) {
    this.db.beginTransaction();
    try {
      paramRunnable.run();
      this.db.setTransactionSuccessful();
      return;
    } finally {
      this.db.endTransaction();
    } 
  }
  
  @Experimental
  public RxTransaction rxTx() {
    if (this.rxTxIo == null)
      this.rxTxIo = new RxTransaction(this, Schedulers.io()); 
    return this.rxTxIo;
  }
  
  @Experimental
  public RxTransaction rxTxPlain() {
    if (this.rxTxPlain == null)
      this.rxTxPlain = new RxTransaction(this); 
    return this.rxTxPlain;
  }
  
  public AsyncSession startAsyncSession() {
    return new AsyncSession(this);
  }
  
  public <T> void update(T paramT) {
    getDao((Class)paramT.getClass()).update(paramT);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/AbstractDaoSession.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */