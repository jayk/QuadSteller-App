package org.xutils.http.app;

import org.xutils.http.RequestParams;
import org.xutils.http.request.UriRequest;

public interface RequestTracker {
  void onCache(UriRequest paramUriRequest, Object paramObject);
  
  void onCancelled(UriRequest paramUriRequest);
  
  void onError(UriRequest paramUriRequest, Throwable paramThrowable, boolean paramBoolean);
  
  void onFinished(UriRequest paramUriRequest);
  
  void onRequestCreated(UriRequest paramUriRequest);
  
  void onStart(RequestParams paramRequestParams);
  
  void onSuccess(UriRequest paramUriRequest, Object paramObject);
  
  void onWaiting(RequestParams paramRequestParams);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/app/RequestTracker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */