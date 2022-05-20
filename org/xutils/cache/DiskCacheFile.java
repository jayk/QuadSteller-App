package org.xutils.cache;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import org.xutils.common.util.IOUtil;
import org.xutils.common.util.ProcessLock;

public final class DiskCacheFile extends File implements Closeable {
  DiskCacheEntity cacheEntity;
  
  ProcessLock lock;
  
  DiskCacheFile(DiskCacheEntity paramDiskCacheEntity, String paramString, ProcessLock paramProcessLock) {
    super(paramString);
    this.cacheEntity = paramDiskCacheEntity;
    this.lock = paramProcessLock;
  }
  
  public void close() throws IOException {
    IOUtil.closeQuietly((Closeable)this.lock);
  }
  
  public DiskCacheFile commit() throws IOException {
    return getDiskCache().commitDiskCacheFile(this);
  }
  
  protected void finalize() throws Throwable {
    super.finalize();
    close();
  }
  
  public DiskCacheEntity getCacheEntity() {
    return this.cacheEntity;
  }
  
  public LruDiskCache getDiskCache() {
    return LruDiskCache.getDiskCache(getParentFile().getName());
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/cache/DiskCacheFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */