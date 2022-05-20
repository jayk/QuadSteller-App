package org.greenrobot.greendao.rx;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import org.greenrobot.greendao.annotation.apihint.Experimental;
import org.greenrobot.greendao.query.LazyList;
import org.greenrobot.greendao.query.Query;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.exceptions.Exceptions;

@Experimental
public class RxQuery<T> extends RxBase {
  private final Query<T> query;
  
  public RxQuery(Query<T> paramQuery) {
    this.query = paramQuery;
  }
  
  public RxQuery(Query<T> paramQuery, Scheduler paramScheduler) {
    super(paramScheduler);
    this.query = paramQuery;
  }
  
  @Experimental
  public Observable<List<T>> list() {
    return wrap(new Callable<List<T>>() {
          public List<T> call() throws Exception {
            return RxQuery.this.query.forCurrentThread().list();
          }
        });
  }
  
  public Observable<T> oneByOne() {
    return wrap(Observable.create(new Observable.OnSubscribe<T>() {
            public void call(Subscriber<? super T> param1Subscriber) {
              try {
                LazyList lazyList = RxQuery.this.query.forCurrentThread().listLazyUncached();
                try {
                  Iterator<Object> iterator = lazyList.iterator();
                  while (true) {
                    if (iterator.hasNext()) {
                      Object object = iterator.next();
                      boolean bool = param1Subscriber.isUnsubscribed();
                      if (!bool) {
                        param1Subscriber.onNext(object);
                        continue;
                      } 
                    } 
                    lazyList.close();
                    return;
                  } 
                } finally {
                  lazyList.close();
                } 
              } catch (Throwable throwable) {
                Exceptions.throwIfFatal(throwable);
                param1Subscriber.onError(throwable);
              } 
            }
          }));
  }
  
  @Experimental
  public Observable<T> unique() {
    return wrap(new Callable<T>() {
          public T call() throws Exception {
            return (T)RxQuery.this.query.forCurrentThread().unique();
          }
        });
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/rx/RxQuery.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */