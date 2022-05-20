package com.squareup.picasso;

import android.content.Context;
import android.net.Uri;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OkHttpDownloader implements Downloader {
  private final OkHttpClient client;
  
  public OkHttpDownloader(Context paramContext) {
    this(Utils.createDefaultCacheDir(paramContext));
  }
  
  public OkHttpDownloader(Context paramContext, long paramLong) {
    this(Utils.createDefaultCacheDir(paramContext), paramLong);
  }
  
  public OkHttpDownloader(OkHttpClient paramOkHttpClient) {
    this.client = paramOkHttpClient;
  }
  
  public OkHttpDownloader(File paramFile) {
    this(paramFile, Utils.calculateDiskCacheSize(paramFile));
  }
  
  public OkHttpDownloader(File paramFile, long paramLong) {
    this(defaultOkHttpClient());
    try {
      OkHttpClient okHttpClient = this.client;
      Cache cache = new Cache();
      this(paramFile, paramLong);
      okHttpClient.setCache(cache);
    } catch (IOException iOException) {}
  }
  
  private static OkHttpClient defaultOkHttpClient() {
    OkHttpClient okHttpClient = new OkHttpClient();
    okHttpClient.setConnectTimeout(15000L, TimeUnit.MILLISECONDS);
    okHttpClient.setReadTimeout(20000L, TimeUnit.MILLISECONDS);
    okHttpClient.setWriteTimeout(20000L, TimeUnit.MILLISECONDS);
    return okHttpClient;
  }
  
  protected final OkHttpClient getClient() {
    return this.client;
  }
  
  public Downloader.Response load(Uri paramUri, int paramInt) throws IOException {
    CacheControl cacheControl = null;
    if (paramInt != 0)
      if (NetworkPolicy.isOfflineOnly(paramInt)) {
        cacheControl = CacheControl.FORCE_CACHE;
      } else {
        CacheControl.Builder builder1 = new CacheControl.Builder();
        if (!NetworkPolicy.shouldReadFromDiskCache(paramInt))
          builder1.noCache(); 
        if (!NetworkPolicy.shouldWriteToDiskCache(paramInt))
          builder1.noStore(); 
        cacheControl = builder1.build();
      }  
    Request.Builder builder = (new Request.Builder()).url(paramUri.toString());
    if (cacheControl != null)
      builder.cacheControl(cacheControl); 
    Response response = this.client.newCall(builder.build()).execute();
    int i = response.code();
    if (i >= 300) {
      response.body().close();
      throw new Downloader.ResponseException(i + " " + response.message(), paramInt, i);
    } 
    if (response.cacheResponse() != null) {
      boolean bool1 = true;
      responseBody = response.body();
      return new Downloader.Response(responseBody.byteStream(), bool1, responseBody.contentLength());
    } 
    boolean bool = false;
    ResponseBody responseBody = responseBody.body();
    return new Downloader.Response(responseBody.byteStream(), bool, responseBody.contentLength());
  }
  
  public void shutdown() {
    Cache cache = this.client.getCache();
    if (cache != null)
      try {
        cache.close();
      } catch (IOException iOException) {} 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/squareup/picasso/OkHttpDownloader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */