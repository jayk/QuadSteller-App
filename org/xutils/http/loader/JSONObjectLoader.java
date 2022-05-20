package org.xutils.http.loader;

import android.text.TextUtils;
import java.io.InputStream;
import org.json.JSONObject;
import org.xutils.cache.DiskCacheEntity;
import org.xutils.common.util.IOUtil;
import org.xutils.http.RequestParams;
import org.xutils.http.request.UriRequest;

class JSONObjectLoader extends Loader<JSONObject> {
  private String charset = "UTF-8";
  
  private String resultStr = null;
  
  public JSONObject load(InputStream paramInputStream) throws Throwable {
    this.resultStr = IOUtil.readStr(paramInputStream, this.charset);
    return new JSONObject(this.resultStr);
  }
  
  public JSONObject load(UriRequest paramUriRequest) throws Throwable {
    paramUriRequest.sendRequest();
    return load(paramUriRequest.getInputStream());
  }
  
  public JSONObject loadFromCache(DiskCacheEntity paramDiskCacheEntity) throws Throwable {
    if (paramDiskCacheEntity != null) {
      String str = paramDiskCacheEntity.getTextContent();
      if (!TextUtils.isEmpty(str))
        return new JSONObject(str); 
    } 
    return null;
  }
  
  public Loader<JSONObject> newInstance() {
    return new JSONObjectLoader();
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


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/loader/JSONObjectLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */