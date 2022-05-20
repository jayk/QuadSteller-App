package org.greenrobot.greendao.rx;

import java.util.concurrent.Callable;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.annotation.apihint.Experimental;
import rx.Observable;
import rx.Scheduler;

@Experimental
public class RxTransaction extends RxBase {
  private final AbstractDaoSession daoSession;
  
  public RxTransaction(AbstractDaoSession paramAbstractDaoSession) {
    this.daoSession = paramAbstractDaoSession;
  }
  
  public RxTransaction(AbstractDaoSession paramAbstractDaoSession, Scheduler paramScheduler) {
    super(paramScheduler);
    this.daoSession = paramAbstractDaoSession;
  }
  
  @Experimental
  public <T> Observable<T> call(final Callable<T> callable) {
    return wrap(new Callable<T>() {
          public T call() throws Exception {
            return (T)RxTransaction.this.daoSession.callInTx(callable);
          }
        });
  }
  
  @Experimental
  public AbstractDaoSession getDaoSession() {
    return this.daoSession;
  }
  
  @Experimental
  public Observable<Void> run(final Runnable runnable) {
    return wrap(new Callable<Void>() {
          public Void call() throws Exception {
            RxTransaction.this.daoSession.runInTx(runnable);
            return null;
          }
        });
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/rx/RxTransaction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */