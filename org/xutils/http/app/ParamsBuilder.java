package org.xutils.http.app;

import javax.net.ssl.SSLSocketFactory;
import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;

public interface ParamsBuilder {
  String buildCacheKey(RequestParams paramRequestParams, String[] paramArrayOfString);
  
  void buildParams(RequestParams paramRequestParams) throws Throwable;
  
  void buildSign(RequestParams paramRequestParams, String[] paramArrayOfString) throws Throwable;
  
  String buildUri(RequestParams paramRequestParams, HttpRequest paramHttpRequest) throws Throwable;
  
  SSLSocketFactory getSSLSocketFactory() throws Throwable;
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/app/ParamsBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */