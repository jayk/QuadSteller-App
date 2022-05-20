package org.xutils.http.loader;

import android.text.TextUtils;
import java.io.InputStream;
import org.json.JSONArray;
import org.xutils.cache.DiskCacheEntity;
import org.xutils.common.util.IOUtil;
import org.xutils.http.RequestParams;
import org.xutils.http.request.UriRequest;

class JSONArrayLoader extends Loader<JSONArray> {
  private String charset = "UTF-8";
  
  private String resultStr = null;
  
  public JSONArray load(InputStream paramInputStream) throws Throwable {
    this.resultStr = IOUtil.readStr(paramInputStream, this.charset);
    return new JSONArray(this.resultStr);
  }
  
  public JSONArray load(UriRequest paramUriRequest) throws Throwable {
    paramUriRequest.sendRequest();
    return load(paramUriRequest.getInputStream());
  }
  
  public JSONArray loadFromCache(DiskCacheEntity paramDiskCacheEntity) throws Throwable {
    if (paramDiskCacheEntity != null) {
      String str = paramDiskCacheEntity.getTextContent();
      if (!TextUtils.isEmpty(str))
        return new JSONArray(str); 
    } 
    return null;
  }
  
  public Loader<JSONArray> newInstance() {
    return new JSONArrayLoader();
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


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/loader/JSONArrayLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */