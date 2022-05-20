package org.xutils.common;

import java.lang.reflect.Type;

public interface Callback {
  public static interface CacheCallback<ResultType> extends CommonCallback<ResultType> {
    boolean onCache(ResultType param1ResultType);
  }
  
  public static interface Callable<ResultType> {
    void call(ResultType param1ResultType);
  }
  
  public static interface Cancelable {
    void cancel();
    
    boolean isCancelled();
  }
  
  public static class CancelledException extends RuntimeException {
    public CancelledException(String param1String) {
      super(param1String);
    }
  }
  
  public static interface CommonCallback<ResultType> extends Callback {
    void onCancelled(Callback.CancelledException param1CancelledException);
    
    void onError(Throwable param1Throwable, boolean param1Boolean);
    
    void onFinished();
    
    void onSuccess(ResultType param1ResultType);
  }
  
  public static interface GroupCallback<ItemType> extends Callback {
    void onAllFinished();
    
    void onCancelled(ItemType param1ItemType, Callback.CancelledException param1CancelledException);
    
    void onError(ItemType param1ItemType, Throwable param1Throwable, boolean param1Boolean);
    
    void onFinished(ItemType param1ItemType);
    
    void onSuccess(ItemType param1ItemType);
  }
  
  public static interface PrepareCallback<PrepareType, ResultType> extends CommonCallback<ResultType> {
    ResultType prepare(PrepareType param1PrepareType);
  }
  
  public static interface ProgressCallback<ResultType> extends CommonCallback<ResultType> {
    void onLoading(long param1Long1, long param1Long2, boolean param1Boolean);
    
    void onStarted();
    
    void onWaiting();
  }
  
  public static interface ProxyCacheCallback<ResultType> extends CacheCallback<ResultType> {
    boolean onlyCache();
  }
  
  public static interface TypedCallback<ResultType> extends CommonCallback<ResultType> {
    Type getLoadType();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/common/Callback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */