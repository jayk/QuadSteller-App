package org.xutils.http.request;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import org.xutils.common.util.LogUtil;
import org.xutils.http.ProgressHandler;
import org.xutils.http.RequestParams;
import org.xutils.http.app.RequestInterceptListener;
import org.xutils.http.loader.Loader;
import org.xutils.http.loader.LoaderFactory;
import org.xutils.x;

public abstract class UriRequest implements Closeable {
  protected ClassLoader callingClassLoader = null;
  
  protected final Loader<?> loader;
  
  protected final RequestParams params;
  
  protected ProgressHandler progressHandler = null;
  
  protected final String queryUrl;
  
  protected RequestInterceptListener requestInterceptListener = null;
  
  UriRequest(RequestParams paramRequestParams, Type paramType) throws Throwable {
    this.params = paramRequestParams;
    this.queryUrl = buildQueryUrl(paramRequestParams);
    this.loader = LoaderFactory.getLoader(paramType, paramRequestParams);
  }
  
  protected String buildQueryUrl(RequestParams paramRequestParams) {
    return paramRequestParams.getUri();
  }
  
  public abstract void clearCacheHeader();
  
  public abstract void close() throws IOException;
  
  public abstract String getCacheKey();
  
  public abstract long getContentLength();
  
  public abstract String getETag();
  
  public abstract long getExpiration();
  
  public abstract long getHeaderFieldDate(String paramString, long paramLong);
  
  public abstract InputStream getInputStream() throws IOException;
  
  public abstract long getLastModified();
  
  public RequestParams getParams() {
    return this.params;
  }
  
  public String getRequestUri() {
    return this.queryUrl;
  }
  
  public abstract int getResponseCode() throws IOException;
  
  public abstract String getResponseHeader(String paramString);
  
  public abstract Map<String, List<String>> getResponseHeaders();
  
  public abstract String getResponseMessage() throws IOException;
  
  public abstract boolean isLoading();
  
  public Object loadResult() throws Throwable {
    return this.loader.load(this);
  }
  
  public abstract Object loadResultFromCache() throws Throwable;
  
  public void save2Cache() {
    x.task().run(new Runnable() {
          public void run() {
            try {
              UriRequest.this.loader.save2Cache(UriRequest.this);
            } catch (Throwable throwable) {
              LogUtil.e(throwable.getMessage(), throwable);
            } 
          }
        });
  }
  
  public abstract void sendRequest() throws Throwable;
  
  public void setCallingClassLoader(ClassLoader paramClassLoader) {
    this.callingClassLoader = paramClassLoader;
  }
  
  public void setProgressHandler(ProgressHandler paramProgressHandler) {
    this.progressHandler = paramProgressHandler;
    this.loader.setProgressHandler(paramProgressHandler);
  }
  
  public void setRequestInterceptListener(RequestInterceptListener paramRequestInterceptListener) {
    this.requestInterceptListener = paramRequestInterceptListener;
  }
  
  public String toString() {
    return getRequestUri();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/request/UriRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */