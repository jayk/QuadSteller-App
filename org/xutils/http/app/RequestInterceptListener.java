package org.xutils.http.app;

import org.xutils.http.request.UriRequest;

public interface RequestInterceptListener {
  void afterRequest(UriRequest paramUriRequest) throws Throwable;
  
  void beforeRequest(UriRequest paramUriRequest) throws Throwable;
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/app/RequestInterceptListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */