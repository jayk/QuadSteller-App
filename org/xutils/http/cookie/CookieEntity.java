package org.xutils.http.cookie;

import android.text.TextUtils;
import java.net.HttpCookie;
import java.net.URI;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "cookie", onCreated = "CREATE UNIQUE INDEX index_cookie_unique ON cookie(\"name\",\"domain\",\"path\")")
final class CookieEntity {
  private static final long MAX_EXPIRY = System.currentTimeMillis() + 3110400000000L;
  
  @Column(name = "comment")
  private String comment;
  
  @Column(name = "commentURL")
  private String commentURL;
  
  @Column(name = "discard")
  private boolean discard;
  
  @Column(name = "domain")
  private String domain;
  
  @Column(name = "expiry")
  private long expiry;
  
  @Column(isId = true, name = "id")
  private long id;
  
  @Column(name = "name")
  private String name;
  
  @Column(name = "path")
  private String path;
  
  @Column(name = "portList")
  private String portList;
  
  @Column(name = "secure")
  private boolean secure;
  
  @Column(name = "uri")
  private String uri;
  
  @Column(name = "value")
  private String value;
  
  @Column(name = "version")
  private int version;
  
  public CookieEntity() {
    this.expiry = MAX_EXPIRY;
    this.version = 1;
  }
  
  public CookieEntity(URI paramURI, HttpCookie paramHttpCookie) {
    String str;
    this.expiry = MAX_EXPIRY;
    this.version = 1;
    if (paramURI == null) {
      paramURI = null;
    } else {
      str = paramURI.toString();
    } 
    this.uri = str;
    this.name = paramHttpCookie.getName();
    this.value = paramHttpCookie.getValue();
    this.comment = paramHttpCookie.getComment();
    this.commentURL = paramHttpCookie.getCommentURL();
    this.discard = paramHttpCookie.getDiscard();
    this.domain = paramHttpCookie.getDomain();
    long l = paramHttpCookie.getMaxAge();
    if (l != -1L && l > 0L) {
      this.expiry = 1000L * l + System.currentTimeMillis();
      if (this.expiry < 0L)
        this.expiry = MAX_EXPIRY; 
    } else {
      this.expiry = -1L;
    } 
    this.path = paramHttpCookie.getPath();
    if (!TextUtils.isEmpty(this.path) && this.path.length() > 1 && this.path.endsWith("/"))
      this.path = this.path.substring(0, this.path.length() - 1); 
    this.portList = paramHttpCookie.getPortlist();
    this.secure = paramHttpCookie.getSecure();
    this.version = paramHttpCookie.getVersion();
  }
  
  public long getId() {
    return this.id;
  }
  
  public String getUri() {
    return this.uri;
  }
  
  public boolean isExpired() {
    return (this.expiry != -1L && this.expiry < System.currentTimeMillis());
  }
  
  public void setId(long paramLong) {
    this.id = paramLong;
  }
  
  public void setUri(String paramString) {
    this.uri = paramString;
  }
  
  public HttpCookie toHttpCookie() {
    HttpCookie httpCookie = new HttpCookie(this.name, this.value);
    httpCookie.setComment(this.comment);
    httpCookie.setCommentURL(this.commentURL);
    httpCookie.setDiscard(this.discard);
    httpCookie.setDomain(this.domain);
    if (this.expiry == -1L) {
      httpCookie.setMaxAge(-1L);
      httpCookie.setPath(this.path);
      httpCookie.setPortlist(this.portList);
      httpCookie.setSecure(this.secure);
      httpCookie.setVersion(this.version);
      return httpCookie;
    } 
    httpCookie.setMaxAge((this.expiry - System.currentTimeMillis()) / 1000L);
    httpCookie.setPath(this.path);
    httpCookie.setPortlist(this.portList);
    httpCookie.setSecure(this.secure);
    httpCookie.setVersion(this.version);
    return httpCookie;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/cookie/CookieEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */