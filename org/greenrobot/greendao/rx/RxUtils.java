package org.greenrobot.greendao.rx;

import java.util.concurrent.Callable;
import org.greenrobot.greendao.annotation.apihint.Internal;
import rx.Observable;
import rx.functions.Func0;

@Internal
class RxUtils {
  @Internal
  static <T> Observable<T> fromCallable(final Callable<T> callable) {
    return Observable.defer(new Func0<Observable<T>>() {
          public Observable<T> call() {
            Observable<T> observable;
            try {
              observable = (Observable<T>)callable.call();
              observable = Observable.just(observable);
            } catch (Exception exception) {
              observable = Observable.error(exception);
            } 
            return observable;
          }
        });
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/rx/RxUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */