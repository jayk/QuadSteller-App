package org.xutils.http.loader;

import android.text.TextUtils;
import java.io.InputStream;
import org.xutils.cache.DiskCacheEntity;
import org.xutils.common.util.IOUtil;
import org.xutils.http.RequestParams;
import org.xutils.http.request.UriRequest;

class StringLoader extends Loader<String> {
  private String charset = "UTF-8";
  
  private String resultStr = null;
  
  public String load(InputStream paramInputStream) throws Throwable {
    this.resultStr = IOUtil.readStr(paramInputStream, this.charset);
    return this.resultStr;
  }
  
  public String load(UriRequest paramUriRequest) throws Throwable {
    paramUriRequest.sendRequest();
    return load(paramUriRequest.getInputStream());
  }
  
  public String loadFromCache(DiskCacheEntity paramDiskCacheEntity) throws Throwable {
    return (paramDiskCacheEntity != null) ? paramDiskCacheEntity.getTextContent() : null;
  }
  
  public Loader<String> newInstance() {
    return new StringLoader();
  }
  
  public void save2Cache(UriRequest paramUriRequest) {
    saveStringCache(paramUriRequest, this.resultStr);
  }
  
  public void setParams(RequestParams paramRequestParams) {
    if (paramRequestParams != null) {
      String str = paramRequestParams.getCharset();
      if (!TextUtils.isEmpty(str))
        this.charset = str; 
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/loader/StringLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */