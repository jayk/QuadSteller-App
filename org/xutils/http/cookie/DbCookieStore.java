package org.xutils.http.cookie;

import android.text.TextUtils;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import org.xutils.DbManager;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.common.util.LogUtil;
import org.xutils.config.DbConfigs;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.DbModel;
import org.xutils.x;

public enum DbCookieStore implements CookieStore {
  INSTANCE;
  
  private static final int LIMIT_COUNT = 5000;
  
  private static final long TRIM_TIME_SPAN = 1000L;
  
  private final DbManager db = x.getDb(DbConfigs.COOKIE.getConfig());
  
  private long lastTrimTime = 0L;
  
  private final Executor trimExecutor = (Executor)new PriorityExecutor(1, true);
  
  static {
    $VALUES = new DbCookieStore[] { INSTANCE };
  }
  
  DbCookieStore() {
    try {
      this.db.delete(CookieEntity.class, WhereBuilder.b("expiry", "=", Long.valueOf(-1L)));
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
    } 
  }
  
  private URI getEffectiveURI(URI paramURI) {
    try {
      URI uRI = new URI();
      this("http", paramURI.getHost(), paramURI.getPath(), null, null);
      paramURI = uRI;
    } catch (Throwable throwable) {}
    return paramURI;
  }
  
  private void trimSize() {
    this.trimExecutor.execute(new Runnable() {
          public void run() {
            long l = System.currentTimeMillis();
            if (l - DbCookieStore.this.lastTrimTime >= 1000L) {
              DbCookieStore.access$002(DbCookieStore.this, l);
              try {
                DbCookieStore.this.db.delete(CookieEntity.class, WhereBuilder.b("expiry", "<", Long.valueOf(System.currentTimeMillis())).and("expiry", "!=", Long.valueOf(-1L)));
              } catch (Throwable throwable) {
                LogUtil.e(throwable.getMessage(), throwable);
              } 
              try {
                int i = (int)DbCookieStore.this.db.selector(CookieEntity.class).count();
                if (i > 5010) {
                  List list = DbCookieStore.this.db.selector(CookieEntity.class).where("expiry", "!=", Long.valueOf(-1L)).orderBy("expiry", false).limit(i - 5000).findAll();
                  if (list != null)
                    DbCookieStore.this.db.delete(list); 
                } 
              } catch (Throwable throwable) {
                LogUtil.e(throwable.getMessage(), throwable);
              } 
            } 
          }
        });
  }
  
  public void add(URI paramURI, HttpCookie paramHttpCookie) {
    if (paramHttpCookie != null) {
      URI uRI = getEffectiveURI(paramURI);
      try {
        DbManager dbManager = this.db;
        CookieEntity cookieEntity = new CookieEntity();
        this(uRI, paramHttpCookie);
        dbManager.replace(cookieEntity);
      } catch (Throwable throwable) {
        LogUtil.e(throwable.getMessage(), throwable);
      } 
      trimSize();
    } 
  }
  
  public List<HttpCookie> get(URI paramURI) {
    if (paramURI == null)
      throw new NullPointerException("uri is null"); 
    URI uRI = getEffectiveURI(paramURI);
    ArrayList<HttpCookie> arrayList = new ArrayList();
    try {
      Selector selector = this.db.selector(CookieEntity.class);
      WhereBuilder whereBuilder = WhereBuilder.b();
      String str = uRI.getHost();
      if (!TextUtils.isEmpty(str)) {
        WhereBuilder whereBuilder1 = WhereBuilder.b("domain", "=", str);
        StringBuilder stringBuilder = new StringBuilder();
        this();
        WhereBuilder whereBuilder2 = whereBuilder1.or("domain", "=", stringBuilder.append(".").append(str).toString());
        int i = str.indexOf(".");
        int j = str.lastIndexOf(".");
        if (i > 0 && j > i) {
          str = str.substring(i, str.length());
          if (!TextUtils.isEmpty(str))
            whereBuilder2.or("domain", "=", str); 
        } 
        whereBuilder.and(whereBuilder2);
      } 
      str = uRI.getPath();
      if (!TextUtils.isEmpty(str)) {
        WhereBuilder whereBuilder1 = WhereBuilder.b("path", "=", str).or("path", "=", "/").or("path", "=", null);
        int i;
        for (i = str.lastIndexOf("/"); i > 0; i = str.lastIndexOf("/")) {
          str = str.substring(0, i);
          whereBuilder1.or("path", "=", str);
        } 
        whereBuilder.and(whereBuilder1);
      } 
      whereBuilder.or("uri", "=", uRI.toString());
      List list = selector.where(whereBuilder).findAll();
      if (list != null)
        for (CookieEntity cookieEntity : list) {
          if (!cookieEntity.isExpired())
            arrayList.add(cookieEntity.toHttpCookie()); 
        }  
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
    } 
    return arrayList;
  }
  
  public List<HttpCookie> getCookies() {
    ArrayList<HttpCookie> arrayList = new ArrayList();
    try {
      List list = this.db.findAll(CookieEntity.class);
      if (list != null)
        for (CookieEntity cookieEntity : list) {
          if (!cookieEntity.isExpired())
            arrayList.add(cookieEntity.toHttpCookie()); 
        }  
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
    } 
    return arrayList;
  }
  
  public List<URI> getURIs() {
    ArrayList<URI> arrayList = new ArrayList();
    try {
      List list = this.db.selector(CookieEntity.class).select(new String[] { "uri" }).findAll();
      if (list != null) {
        Iterator<DbModel> iterator = list.iterator();
        while (iterator.hasNext()) {
          String str = ((DbModel)iterator.next()).getString("uri");
          boolean bool = TextUtils.isEmpty(str);
          if (!bool)
            try {
              URI uRI = new URI();
              this(str);
              arrayList.add(uRI);
            } catch (Throwable throwable) {
              LogUtil.e(throwable.getMessage(), throwable);
              try {
                this.db.delete(CookieEntity.class, WhereBuilder.b("uri", "=", str));
              } catch (Throwable throwable1) {
                LogUtil.e(throwable1.getMessage(), throwable1);
              } 
            }  
        } 
      } 
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
    } 
    return arrayList;
  }
  
  public boolean remove(URI paramURI, HttpCookie paramHttpCookie) {
    if (paramHttpCookie == null)
      return true; 
    boolean bool = false;
    try {
      WhereBuilder whereBuilder = WhereBuilder.b("name", "=", paramHttpCookie.getName());
      String str1 = paramHttpCookie.getDomain();
      if (!TextUtils.isEmpty(str1))
        whereBuilder.and("domain", "=", str1); 
      String str2 = paramHttpCookie.getPath();
      if (!TextUtils.isEmpty(str2)) {
        str1 = str2;
        if (str2.length() > 1) {
          str1 = str2;
          if (str2.endsWith("/"))
            str1 = str2.substring(0, str2.length() - 1); 
        } 
        whereBuilder.and("path", "=", str1);
      } 
      this.db.delete(CookieEntity.class, whereBuilder);
      bool = true;
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
    } 
    return bool;
  }
  
  public boolean removeAll() {
    try {
      this.db.delete(CookieEntity.class);
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
    } 
    return true;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/cookie/DbCookieStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */