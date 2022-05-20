package org.xutils.http;

import java.lang.reflect.Type;
import org.xutils.HttpManager;
import org.xutils.common.Callback;
import org.xutils.x;

public final class HttpManagerImpl implements HttpManager {
  private static volatile HttpManagerImpl instance;
  
  private static final Object lock = new Object();
  
  public static void registerInstance() {
    if (instance == null)
      synchronized (lock) {
        if (instance == null) {
          HttpManagerImpl httpManagerImpl = new HttpManagerImpl();
          this();
          instance = httpManagerImpl;
        } 
        x.Ext.setHttpManager(instance);
        return;
      }  
    x.Ext.setHttpManager(instance);
  }
  
  public <T> Callback.Cancelable get(RequestParams paramRequestParams, Callback.CommonCallback<T> paramCommonCallback) {
    return request(HttpMethod.GET, paramRequestParams, paramCommonCallback);
  }
  
  public <T> T getSync(RequestParams paramRequestParams, Class<T> paramClass) throws Throwable {
    return requestSync(HttpMethod.GET, paramRequestParams, paramClass);
  }
  
  public <T> Callback.Cancelable post(RequestParams paramRequestParams, Callback.CommonCallback<T> paramCommonCallback) {
    return request(HttpMethod.POST, paramRequestParams, paramCommonCallback);
  }
  
  public <T> T postSync(RequestParams paramRequestParams, Class<T> paramClass) throws Throwable {
    return requestSync(HttpMethod.POST, paramRequestParams, paramClass);
  }
  
  public <T> Callback.Cancelable request(HttpMethod paramHttpMethod, RequestParams paramRequestParams, Callback.CommonCallback<T> paramCommonCallback) {
    Callback.Cancelable cancelable;
    paramRequestParams.setMethod(paramHttpMethod);
    paramHttpMethod = null;
    if (paramCommonCallback instanceof Callback.Cancelable)
      cancelable = (Callback.Cancelable)paramCommonCallback; 
    HttpTask<T> httpTask = new HttpTask<T>(paramRequestParams, cancelable, paramCommonCallback);
    return (Callback.Cancelable)x.task().start(httpTask);
  }
  
  public <T> T requestSync(HttpMethod paramHttpMethod, RequestParams paramRequestParams, Class<T> paramClass) throws Throwable {
    return requestSync(paramHttpMethod, paramRequestParams, new DefaultSyncCallback<T>(paramClass));
  }
  
  public <T> T requestSync(HttpMethod paramHttpMethod, RequestParams paramRequestParams, Callback.TypedCallback<T> paramTypedCallback) throws Throwable {
    paramRequestParams.setMethod(paramHttpMethod);
    HttpTask<T> httpTask = new HttpTask<T>(paramRequestParams, null, (Callback.CommonCallback<T>)paramTypedCallback);
    return (T)x.task().startSync(httpTask);
  }
  
  private class DefaultSyncCallback<T> implements Callback.TypedCallback<T> {
    private final Class<T> resultType;
    
    public DefaultSyncCallback(Class<T> param1Class) {
      this.resultType = param1Class;
    }
    
    public Type getLoadType() {
      return this.resultType;
    }
    
    public void onCancelled(Callback.CancelledException param1CancelledException) {}
    
    public void onError(Throwable param1Throwable, boolean param1Boolean) {}
    
    public void onFinished() {}
    
    public void onSuccess(T param1T) {}
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/HttpManagerImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */