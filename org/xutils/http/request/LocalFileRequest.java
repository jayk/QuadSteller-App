package org.xutils.http.request;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import org.xutils.common.util.IOUtil;
import org.xutils.http.RequestParams;

public class LocalFileRequest extends UriRequest {
  private InputStream inputStream;
  
  LocalFileRequest(RequestParams paramRequestParams, Type paramType) throws Throwable {
    super(paramRequestParams, paramType);
  }
  
  private File getFile() {
    if (this.queryUrl.startsWith("file:")) {
      String str1 = this.queryUrl.substring("file:".length());
      return new File(str1);
    } 
    String str = this.queryUrl;
    return new File(str);
  }
  
  public void clearCacheHeader() {}
  
  public void close() throws IOException {
    IOUtil.closeQuietly(this.inputStream);
    this.inputStream = null;
  }
  
  public String getCacheKey() {
    return null;
  }
  
  public long getContentLength() {
    return getFile().length();
  }
  
  public String getETag() {
    return null;
  }
  
  public long getExpiration() {
    return -1L;
  }
  
  public long getHeaderFieldDate(String paramString, long paramLong) {
    return paramLong;
  }
  
  public InputStream getInputStream() throws IOException {
    if (this.inputStream == null)
      this.inputStream = new FileInputStream(getFile()); 
    return this.inputStream;
  }
  
  public long getLastModified() {
    return getFile().lastModified();
  }
  
  public int getResponseCode() throws IOException {
    return getFile().exists() ? 200 : 404;
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
    return (this.loader instanceof org.xutils.http.loader.FileLoader) ? getFile() : this.loader.load(this);
  }
  
  public Object loadResultFromCache() throws Throwable {
    return null;
  }
  
  public void save2Cache() {}
  
  public void sendRequest() throws Throwable {}
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/request/LocalFileRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */