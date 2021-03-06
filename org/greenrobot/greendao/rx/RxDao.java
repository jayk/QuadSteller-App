package org.greenrobot.greendao.rx;

import java.util.List;
import java.util.concurrent.Callable;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.annotation.apihint.Experimental;
import rx.Observable;
import rx.Scheduler;

@Experimental
public class RxDao<T, K> extends RxBase {
  private final AbstractDao<T, K> dao;
  
  @Experimental
  public RxDao(AbstractDao<T, K> paramAbstractDao) {
    this(paramAbstractDao, null);
  }
  
  @Experimental
  public RxDao(AbstractDao<T, K> paramAbstractDao, Scheduler paramScheduler) {
    super(paramScheduler);
    this.dao = paramAbstractDao;
  }
  
  @Experimental
  public Observable<Long> count() {
    return wrap(new Callable<Long>() {
          public Long call() throws Exception {
            return Long.valueOf(RxDao.this.dao.count());
          }
        });
  }
  
  @Experimental
  public Observable<Void> delete(final T entity) {
    return wrap(new Callable<Void>() {
          public Void call() throws Exception {
            RxDao.this.dao.delete(entity);
            return null;
          }
        });
  }
  
  @Experimental
  public Observable<Void> deleteAll() {
    return wrap(new Callable<Void>() {
          public Void call() throws Exception {
            RxDao.this.dao.deleteAll();
            return null;
          }
        });
  }
  
  @Experimental
  public Observable<Void> deleteByKey(final K key) {
    return wrap(new Callable<Void>() {
          public Void call() throws Exception {
            RxDao.this.dao.deleteByKey(key);
            return null;
          }
        });
  }
  
  @Experimental
  public Observable<Void> deleteByKeyInTx(final Iterable<K> keys) {
    return wrap(new Callable<Void>() {
          public Void call() throws Exception {
            RxDao.this.dao.deleteByKeyInTx(keys);
            return null;
          }
        });
  }
  
  @Experimental
  public Observable<Void> deleteByKeyInTx(K... keys) {
    return wrap(new Callable<Void>() {
          public Void call() throws Exception {
            RxDao.this.dao.deleteByKeyInTx(keys);
            return null;
          }
        });
  }
  
  @Experimental
  public Observable<Void> deleteInTx(final Iterable<T> entities) {
    return wrap(new Callable<Void>() {
          public Void call() throws Exception {
            RxDao.this.dao.deleteInTx(entities);
            return null;
          }
        });
  }
  
  @Experimental
  public Observable<Void> deleteInTx(T... entities) {
    return wrap(new Callable<Void>() {
          public Void call() throws Exception {
            RxDao.this.dao.deleteInTx(entities);
            return null;
          }
        });
  }
  
  @Experimental
  public AbstractDao<T, K> getDao() {
    return this.dao;
  }
  
  @Experimental
  public Observable<T> insert(final T entity) {
    return wrap(new Callable<T>() {
          public T call() throws Exception {
            RxDao.this.dao.insert(entity);
            return (T)entity;
          }
        });
  }
  
  @Experimental
  public Observable<Iterable<T>> insertInTx(final Iterable<T> entities) {
    return wrap(new Callable<Iterable<T>>() {
          public Iterable<T> call() throws Exception {
            RxDao.this.dao.insertInTx(entities);
            return entities;
          }
        });
  }
  
  @Experimental
  public Observable<Object[]> insertInTx(T... entities) {
    return wrap(new Callable<Object[]>() {
          public Object[] call() throws Exception {
            RxDao.this.dao.insertInTx(entities);
            return entities;
          }
        });
  }
  
  @Experimental
  public Observable<T> insertOrReplace(final T entity) {
    return wrap(new Callable<T>() {
          public T call() throws Exception {
            RxDao.this.dao.insertOrReplace(entity);
            return (T)entity;
          }
        });
  }
  
  @Experimental
  public Observable<Iterable<T>> insertOrReplaceInTx(final Iterable<T> entities) {
    return wrap(new Callable<Iterable<T>>() {
          public Iterable<T> call() throws Exception {
            RxDao.this.dao.insertOrReplaceInTx(entities);
            return entities;
          }
        });
  }
  
  @Experimental
  public Observable<Object[]> insertOrReplaceInTx(T... entities) {
    return wrap(new Callable<Object[]>() {
          public Object[] call() throws Exception {
            RxDao.this.dao.insertOrReplaceInTx(entities);
            return entities;
          }
        });
  }
  
  @Experimental
  public Observable<T> load(final K key) {
    return wrap(new Callable<T>() {
          public T call() throws Exception {
            return (T)RxDao.this.dao.load(key);
          }
        });
  }
  
  @Experimental
  public Observable<List<T>> loadAll() {
    return wrap(new Callable<List<T>>() {
          public List<T> call() throws Exception {
            return RxDao.this.dao.loadAll();
          }
        });
  }
  
  @Experimental
  public Observable<T> refresh(final T entity) {
    return wrap(new Callable<T>() {
          public T call() throws Exception {
            RxDao.this.dao.refresh(entity);
            return (T)entity;
          }
        });
  }
  
  @Experimental
  public Observable<T> save(final T entity) {
    return wrap(new Callable<T>() {
          public T call() throws Exception {
            RxDao.this.dao.save(entity);
            return (T)entity;
          }
        });
  }
  
  @Experimental
  public Observable<Iterable<T>> saveInTx(final Iterable<T> entities) {
    return wrap(new Callable<Iterable<T>>() {
          public Iterable<T> call() throws Exception {
            RxDao.this.dao.saveInTx(entities);
            return entities;
          }
        });
  }
  
  @Experimental
  public Observable<Object[]> saveInTx(T... entities) {
    return wrap(new Callable<Object[]>() {
          public Object[] call() throws Exception {
            RxDao.this.dao.saveInTx(entities);
            return entities;
          }
        });
  }
  
  @Experimental
  public Observable<T> update(final T entity) {
    return wrap(new Callable<T>() {
          public T call() throws Exception {
            RxDao.this.dao.update(entity);
            return (T)entity;
          }
        });
  }
  
  @Experimental
  public Observable<Iterable<T>> updateInTx(final Iterable<T> entities) {
    return wrap(new Callable<Iterable<T>>() {
          public Iterable<T> call() throws Exception {
            RxDao.this.dao.updateInTx(entities);
            return entities;
          }
        });
  }
  
  @Experimental
  public Observable<Object[]> updateInTx(T... entities) {
    return wrap(new Callable<Object[]>() {
          public Object[] call() throws Exception {
            RxDao.this.dao.updateInTx(entities);
            return entities;
          }
        });
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/rx/RxDao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */