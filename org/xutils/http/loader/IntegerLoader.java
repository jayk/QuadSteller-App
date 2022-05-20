package org.xutils.http.loader;

import java.io.InputStream;
import org.xutils.cache.DiskCacheEntity;
import org.xutils.http.request.UriRequest;

class IntegerLoader extends Loader<Integer> {
  public Integer load(InputStream paramInputStream) throws Throwable {
    return Integer.valueOf(100);
  }
  
  public Integer load(UriRequest paramUriRequest) throws Throwable {
    paramUriRequest.sendRequest();
    return Integer.valueOf(paramUriRequest.getResponseCode());
  }
  
  public Integer loadFromCache(DiskCacheEntity paramDiskCacheEntity) throws Throwable {
    return null;
  }
  
  public Loader<Integer> newInstance() {
    return new IntegerLoader();
  }
  
  public void save2Cache(UriRequest paramUriRequest) {}
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/loader/IntegerLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */