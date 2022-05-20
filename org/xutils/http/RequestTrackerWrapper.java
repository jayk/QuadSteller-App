package org.xutils.http;

import org.xutils.common.util.LogUtil;
import org.xutils.http.app.RequestTracker;
import org.xutils.http.request.UriRequest;

final class RequestTrackerWrapper implements RequestTracker {
  private final RequestTracker base;
  
  public RequestTrackerWrapper(RequestTracker paramRequestTracker) {
    this.base = paramRequestTracker;
  }
  
  public void onCache(UriRequest paramUriRequest, Object paramObject) {
    try {
      this.base.onCache(paramUriRequest, paramObject);
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
    } 
  }
  
  public void onCancelled(UriRequest paramUriRequest) {
    try {
      this.base.onCancelled(paramUriRequest);
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
    } 
  }
  
  public void onError(UriRequest paramUriRequest, Throwable paramThrowable, boolean paramBoolean) {
    try {
      this.base.onError(paramUriRequest, paramThrowable, paramBoolean);
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
    } 
  }
  
  public void onFinished(UriRequest paramUriRequest) {
    try {
      this.base.onFinished(paramUriRequest);
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
    } 
  }
  
  public void onRequestCreated(UriRequest paramUriRequest) {
    try {
      this.base.onRequestCreated(paramUriRequest);
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
    } 
  }
  
  public void onStart(RequestParams paramRequestParams) {
    try {
      this.base.onStart(paramRequestParams);
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
    } 
  }
  
  public void onSuccess(UriRequest paramUriRequest, Object paramObject) {
    try {
      this.base.onSuccess(paramUriRequest, paramObject);
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
    } 
  }
  
  public void onWaiting(RequestParams paramRequestParams) {
    try {
      this.base.onWaiting(paramRequestParams);
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/RequestTrackerWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */