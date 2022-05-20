package org.xutils.http.loader;

import android.text.TextUtils;
import java.io.InputStream;
import java.util.Date;
import org.xutils.cache.DiskCacheEntity;
import org.xutils.cache.LruDiskCache;
import org.xutils.http.ProgressHandler;
import org.xutils.http.RequestParams;
import org.xutils.http.request.UriRequest;

public abstract class Loader<T> {
  protected RequestParams params;
  
  protected ProgressHandler progressHandler;
  
  public abstract T load(InputStream paramInputStream) throws Throwable;
  
  public abstract T load(UriRequest paramUriRequest) throws Throwable;
  
  public abstract T loadFromCache(DiskCacheEntity paramDiskCacheEntity) throws Throwable;
  
  public abstract Loader<T> newInstance();
  
  public abstract void save2Cache(UriRequest paramUriRequest);
  
  protected void saveStringCache(UriRequest paramUriRequest, String paramString) {
    if (!TextUtils.isEmpty(paramString)) {
      DiskCacheEntity diskCacheEntity = new DiskCacheEntity();
      diskCacheEntity.setKey(paramUriRequest.getCacheKey());
      diskCacheEntity.setLastAccess(System.currentTimeMillis());
      diskCacheEntity.setEtag(paramUriRequest.getETag());
      diskCacheEntity.setExpires(paramUriRequest.getExpiration());
      diskCacheEntity.setLastModify(new Date(paramUriRequest.getLastModified()));
      diskCacheEntity.setTextContent(paramString);
      LruDiskCache.getDiskCache(paramUriRequest.getParams().getCacheDirName()).put(diskCacheEntity);
    } 
  }
  
  public void setParams(RequestParams paramRequestParams) {
    this.params = paramRequestParams;
  }
  
  public void setProgressHandler(ProgressHandler paramProgressHandler) {
    this.progressHandler = paramProgressHandler;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/loader/Loader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */