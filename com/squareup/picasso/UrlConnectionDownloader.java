package com.squareup.picasso;

import android.content.Context;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.Build;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlConnectionDownloader implements Downloader {
  private static final ThreadLocal<StringBuilder> CACHE_HEADER_BUILDER;
  
  private static final String FORCE_CACHE = "only-if-cached,max-age=2147483647";
  
  static final String RESPONSE_SOURCE = "X-Android-Response-Source";
  
  static volatile Object cache;
  
  private static final Object lock = new Object();
  
  private final Context context;
  
  static {
    CACHE_HEADER_BUILDER = new ThreadLocal<StringBuilder>() {
        protected StringBuilder initialValue() {
          return new StringBuilder();
        }
      };
  }
  
  public UrlConnectionDownloader(Context paramContext) {
    this.context = paramContext.getApplicationContext();
  }
  
  private static void installCacheIfNeeded(Context paramContext) {
    if (cache == null)
      try {
        synchronized (lock) {
          if (cache == null)
            cache = ResponseCacheIcs.install(paramContext); 
          return;
        } 
      } catch (IOException iOException) {} 
  }
  
  public Downloader.Response load(Uri paramUri, int paramInt) throws IOException {
    if (Build.VERSION.SDK_INT >= 14)
      installCacheIfNeeded(this.context); 
    HttpURLConnection httpURLConnection = openConnection(paramUri);
    httpURLConnection.setUseCaches(true);
    if (paramInt != 0) {
      String str;
      if (NetworkPolicy.isOfflineOnly(paramInt)) {
        str = "only-if-cached,max-age=2147483647";
      } else {
        StringBuilder stringBuilder = CACHE_HEADER_BUILDER.get();
        stringBuilder.setLength(0);
        if (!NetworkPolicy.shouldReadFromDiskCache(paramInt))
          stringBuilder.append("no-cache"); 
        if (!NetworkPolicy.shouldWriteToDiskCache(paramInt)) {
          if (stringBuilder.length() > 0)
            stringBuilder.append(','); 
          stringBuilder.append("no-store");
        } 
        str = stringBuilder.toString();
      } 
      httpURLConnection.setRequestProperty("Cache-Control", str);
    } 
    int i = httpURLConnection.getResponseCode();
    if (i >= 300) {
      httpURLConnection.disconnect();
      throw new Downloader.ResponseException(i + " " + httpURLConnection.getResponseMessage(), paramInt, i);
    } 
    long l = httpURLConnection.getHeaderFieldInt("Content-Length", -1);
    boolean bool = Utils.parseResponseSourceHeader(httpURLConnection.getHeaderField("X-Android-Response-Source"));
    return new Downloader.Response(httpURLConnection.getInputStream(), bool, l);
  }
  
  protected HttpURLConnection openConnection(Uri paramUri) throws IOException {
    HttpURLConnection httpURLConnection = (HttpURLConnection)(new URL(paramUri.toString())).openConnection();
    httpURLConnection.setConnectTimeout(15000);
    httpURLConnection.setReadTimeout(20000);
    return httpURLConnection;
  }
  
  public void shutdown() {
    if (Build.VERSION.SDK_INT >= 14 && cache != null)
      ResponseCacheIcs.close(cache); 
  }
  
  private static class ResponseCacheIcs {
    static void close(Object param1Object) {
      try {
        ((HttpResponseCache)param1Object).close();
      } catch (IOException iOException) {}
    }
    
    static Object install(Context param1Context) throws IOException {
      File file = Utils.createDefaultCacheDir(param1Context);
      HttpResponseCache httpResponseCache2 = HttpResponseCache.getInstalled();
      HttpResponseCache httpResponseCache1 = httpResponseCache2;
      if (httpResponseCache2 == null)
        httpResponseCache1 = HttpResponseCache.install(file, Utils.calculateDiskCacheSize(file)); 
      return httpResponseCache1;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/squareup/picasso/UrlConnectionDownloader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */