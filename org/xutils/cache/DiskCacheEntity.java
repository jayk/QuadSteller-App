package org.xutils.cache;

import java.util.Date;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "disk_cache")
public final class DiskCacheEntity {
  @Column(name = "etag")
  private String etag;
  
  @Column(name = "expires")
  private long expires = Long.MAX_VALUE;
  
  @Column(name = "hits")
  private long hits;
  
  @Column(isId = true, name = "id")
  private long id;
  
  @Column(name = "key", property = "UNIQUE")
  private String key;
  
  @Column(name = "lastAccess")
  private long lastAccess;
  
  @Column(name = "lastModify")
  private Date lastModify;
  
  @Column(name = "path")
  private String path;
  
  @Column(name = "textContent")
  private String textContent;
  
  public String getEtag() {
    return this.etag;
  }
  
  public long getExpires() {
    return this.expires;
  }
  
  public long getHits() {
    return this.hits;
  }
  
  public long getId() {
    return this.id;
  }
  
  public String getKey() {
    return this.key;
  }
  
  public long getLastAccess() {
    return (this.lastAccess == 0L) ? System.currentTimeMillis() : this.lastAccess;
  }
  
  public Date getLastModify() {
    return this.lastModify;
  }
  
  String getPath() {
    return this.path;
  }
  
  public String getTextContent() {
    return this.textContent;
  }
  
  public void setEtag(String paramString) {
    this.etag = paramString;
  }
  
  public void setExpires(long paramLong) {
    this.expires = paramLong;
  }
  
  public void setHits(long paramLong) {
    this.hits = paramLong;
  }
  
  public void setId(long paramLong) {
    this.id = paramLong;
  }
  
  public void setKey(String paramString) {
    this.key = paramString;
  }
  
  public void setLastAccess(long paramLong) {
    this.lastAccess = paramLong;
  }
  
  public void setLastModify(Date paramDate) {
    this.lastModify = paramDate;
  }
  
  void setPath(String paramString) {
    this.path = paramString;
  }
  
  public void setTextContent(String paramString) {
    this.textContent = paramString;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/cache/DiskCacheEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */