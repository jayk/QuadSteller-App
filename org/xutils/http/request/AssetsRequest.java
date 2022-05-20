package org.xutils.http.request;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.xutils.cache.DiskCacheEntity;
import org.xutils.cache.LruDiskCache;
import org.xutils.common.util.IOUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class AssetsRequest extends UriRequest {
  private long contentLength = 0L;
  
  private InputStream inputStream;
  
  public AssetsRequest(RequestParams paramRequestParams, Type paramType) throws Throwable {
    super(paramRequestParams, paramType);
  }
  
  public void clearCacheHeader() {}
  
  public void close() throws IOException {
    IOUtil.closeQuietly(this.inputStream);
    this.inputStream = null;
  }
  
  protected long getAssetsLastModified() {
    return (new File((x.app().getApplicationInfo()).sourceDir)).lastModified();
  }
  
  public String getCacheKey() {
    return this.queryUrl;
  }
  
  public long getContentLength() {
    long l;
    try {
      getInputStream();
      l = this.contentLength;
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
      l = 0L;
    } 
    return l;
  }
  
  public String getETag() {
    return null;
  }
  
  public long getExpiration() {
    return Long.MAX_VALUE;
  }
  
  public long getHeaderFieldDate(String paramString, long paramLong) {
    return paramLong;
  }
  
  public InputStream getInputStream() throws IOException {
    if (this.inputStream == null && this.callingClassLoader != null) {
      String str = "assets/" + this.queryUrl.substring("assets://".length());
      this.inputStream = this.callingClassLoader.getResourceAsStream(str);
      this.contentLength = this.inputStream.available();
    } 
    return this.inputStream;
  }
  
  public long getLastModified() {
    return getAssetsLastModified();
  }
  
  public int getResponseCode() throws IOException {
    return (getInputStream() != null) ? 200 : 404;
  }
  
  public String getResponseHeader(String paramString) {
    return null;
  }
  
  public Map<String, List<String>> getResponseHeaders() {
    return null;
  }
  
  public String getResponseMessage() throws IOException {
    return null;
  }
  
  public boolean isLoading() {
    return true;
  }
  
  public Object loadResult() throws Throwable {
    return this.loader.load(this);
  }
  
  public Object loadResultFromCache() throws Throwable {
    Object object = null;
    DiskCacheEntity diskCacheEntity = LruDiskCache.getDiskCache(this.params.getCacheDirName()).setMaxSize(this.params.getCacheSize()).get(getCacheKey());
    null = object;
    if (diskCacheEntity != null) {
      Date date = diskCacheEntity.getLastModify();
      null = object;
      if (date != null) {
        if (date.getTime() < getAssetsLastModified())
          return object; 
      } else {
        return null;
      } 
    } else {
      return null;
    } 
    return this.loader.loadFromCache(diskCacheEntity);
  }
  
  public void sendRequest() throws Throwable {}
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/request/AssetsRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */