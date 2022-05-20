package org.xutils.image;

final class MemCacheKey {
  public final ImageOptions options;
  
  public final String url;
  
  public MemCacheKey(String paramString, ImageOptions paramImageOptions) {
    this.url = paramString;
    this.options = paramImageOptions;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool1 = false;
    if (this == paramObject)
      return true; 
    boolean bool2 = bool1;
    if (paramObject != null) {
      bool2 = bool1;
      if (getClass() == paramObject.getClass()) {
        paramObject = paramObject;
        bool2 = bool1;
        if (this.url.equals(((MemCacheKey)paramObject).url))
          bool2 = this.options.equals(((MemCacheKey)paramObject).options); 
      } 
    } 
    return bool2;
  }
  
  public int hashCode() {
    return this.url.hashCode() * 31 + this.options.hashCode();
  }
  
  public String toString() {
    return this.url + this.options.toString();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/image/MemCacheKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */