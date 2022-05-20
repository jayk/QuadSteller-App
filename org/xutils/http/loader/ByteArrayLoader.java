package org.xutils.http.loader;

import java.io.InputStream;
import org.xutils.cache.DiskCacheEntity;
import org.xutils.common.util.IOUtil;
import org.xutils.http.request.UriRequest;

class ByteArrayLoader extends Loader<byte[]> {
  public byte[] load(InputStream paramInputStream) throws Throwable {
    return IOUtil.readBytes(paramInputStream);
  }
  
  public byte[] load(UriRequest paramUriRequest) throws Throwable {
    paramUriRequest.sendRequest();
    return load(paramUriRequest.getInputStream());
  }
  
  public byte[] loadFromCache(DiskCacheEntity paramDiskCacheEntity) throws Throwable {
    return null;
  }
  
  public Loader<byte[]> newInstance() {
    return new ByteArrayLoader();
  }
  
  public void save2Cache(UriRequest paramUriRequest) {}
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/loader/ByteArrayLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */