package org.greenrobot.greendao.rx;

import java.util.concurrent.Callable;
import org.greenrobot.greendao.annotation.apihint.Experimental;
import org.greenrobot.greendao.annotation.apihint.Internal;
import rx.Observable;
import rx.Scheduler;

@Internal
class RxBase {
  protected final Scheduler scheduler = null;
  
  RxBase() {}
  
  @Experimental
  RxBase(Scheduler paramScheduler) {}
  
  @Experimental
  public Scheduler getScheduler() {
    return this.scheduler;
  }
  
  protected <R> Observable<R> wrap(Callable<R> paramCallable) {
    return wrap(RxUtils.fromCallable(paramCallable));
  }
  
  protected <R> Observable<R> wrap(Observable<R> paramObservable) {
    Observable<R> observable = paramObservable;
    if (this.scheduler != null)
      observable = paramObservable.subscribeOn(this.scheduler); 
    return observable;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/rx/RxBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */