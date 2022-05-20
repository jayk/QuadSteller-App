package org.xutils.http.loader;

import java.io.InputStream;
import org.xutils.cache.DiskCacheEntity;
import org.xutils.http.request.UriRequest;

class BooleanLoader extends Loader<Boolean> {
  public Boolean load(InputStream paramInputStream) throws Throwable {
    return Boolean.valueOf(false);
  }
  
  public Boolean load(UriRequest paramUriRequest) throws Throwable {
    paramUriRequest.sendRequest();
    if (paramUriRequest.getResponseCode() < 300) {
      boolean bool1 = true;
      return Boolean.valueOf(bool1);
    } 
    boolean bool = false;
    return Boolean.valueOf(bool);
  }
  
  public Boolean loadFromCache(DiskCacheEntity paramDiskCacheEntity) throws Throwable {
    return null;
  }
  
  public Loader<Boolean> newInstance() {
    return new BooleanLoader();
  }
  
  public void save2Cache(UriRequest paramUriRequest) {}
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/loader/BooleanLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */