package org.xutils;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

public interface HttpManager {
  <T> Callback.Cancelable get(RequestParams paramRequestParams, Callback.CommonCallback<T> paramCommonCallback);
  
  <T> T getSync(RequestParams paramRequestParams, Class<T> paramClass) throws Throwable;
  
  <T> Callback.Cancelable post(RequestParams paramRequestParams, Callback.CommonCallback<T> paramCommonCallback);
  
  <T> T postSync(RequestParams paramRequestParams, Class<T> paramClass) throws Throwable;
  
  <T> Callback.Cancelable request(HttpMethod paramHttpMethod, RequestParams paramRequestParams, Callback.CommonCallback<T> paramCommonCallback);
  
  <T> T requestSync(HttpMethod paramHttpMethod, RequestParams paramRequestParams, Class<T> paramClass) throws Throwable;
  
  <T> T requestSync(HttpMethod paramHttpMethod, RequestParams paramRequestParams, Callback.TypedCallback<T> paramTypedCallback) throws Throwable;
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/HttpManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */