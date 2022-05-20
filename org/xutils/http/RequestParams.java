package org.xutils.http;

import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.util.List;
import java.util.concurrent.Executor;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import org.xutils.common.task.Priority;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;
import org.xutils.http.app.HttpRetryHandler;
import org.xutils.http.app.ParamsBuilder;
import org.xutils.http.app.RedirectHandler;
import org.xutils.http.app.RequestTracker;
import org.xutils.http.body.RequestBody;

public class RequestParams extends BaseParams {
  private boolean autoRename;
  
  private boolean autoResume;
  
  private String buildCacheKey;
  
  private String buildUri;
  
  private ParamsBuilder builder;
  
  private String cacheDirName;
  
  private final String[] cacheKeys;
  
  private long cacheMaxAge;
  
  private long cacheSize;
  
  private boolean cancelFast;
  
  private int connectTimeout;
  
  private Executor executor;
  
  private HostnameVerifier hostnameVerifier;
  
  private HttpRequest httpRequest;
  
  private HttpRetryHandler httpRetryHandler;
  
  private boolean invokedGetHttpRequest;
  
  private int loadingUpdateMaxTimeSpan;
  
  private int maxRetryCount;
  
  private Priority priority;
  
  private Proxy proxy;
  
  private int readTimeout;
  
  private RedirectHandler redirectHandler;
  
  private RequestTracker requestTracker;
  
  private String saveFilePath;
  
  private final String[] signs;
  
  private SSLSocketFactory sslSocketFactory;
  
  private String uri;
  
  private boolean useCookie;
  
  public RequestParams() {
    this(null, null, null, null);
  }
  
  public RequestParams(String paramString) {
    this(paramString, null, null, null);
  }
  
  public RequestParams(String paramString, ParamsBuilder paramParamsBuilder, String[] paramArrayOfString1, String[] paramArrayOfString2) {
    DefaultParamsBuilder defaultParamsBuilder;
    this.useCookie = true;
    this.priority = Priority.DEFAULT;
    this.connectTimeout = 15000;
    this.readTimeout = 15000;
    this.autoResume = true;
    this.autoRename = false;
    this.maxRetryCount = 2;
    this.cancelFast = false;
    this.loadingUpdateMaxTimeSpan = 300;
    this.invokedGetHttpRequest = false;
    ParamsBuilder paramsBuilder = paramParamsBuilder;
    if (paramString != null) {
      paramsBuilder = paramParamsBuilder;
      if (paramParamsBuilder == null)
        defaultParamsBuilder = new DefaultParamsBuilder(); 
    } 
    this.uri = paramString;
    this.signs = paramArrayOfString1;
    this.cacheKeys = paramArrayOfString2;
    this.builder = (ParamsBuilder)defaultParamsBuilder;
  }
  
  private HttpRequest getHttpRequest() {
    if (this.httpRequest == null && !this.invokedGetHttpRequest) {
      this.invokedGetHttpRequest = true;
      Class<?> clazz = getClass();
      if (clazz != RequestParams.class)
        this.httpRequest = clazz.<HttpRequest>getAnnotation(HttpRequest.class); 
    } 
    return this.httpRequest;
  }
  
  private void initEntityParams() {
    RequestParamsHelper.parseKV(this, getClass(), new RequestParamsHelper.ParseKVListener() {
          public void onParseKV(String param1String, Object param1Object) {
            RequestParams.this.addParameter(param1String, param1Object);
          }
        });
  }
  
  public String getCacheDirName() {
    return this.cacheDirName;
  }
  
  public String getCacheKey() {
    if (TextUtils.isEmpty(this.buildCacheKey) && this.builder != null) {
      HttpRequest httpRequest = getHttpRequest();
      if (httpRequest != null) {
        this.buildCacheKey = this.builder.buildCacheKey(this, httpRequest.cacheKeys());
        return this.buildCacheKey;
      } 
    } else {
      return this.buildCacheKey;
    } 
    this.buildCacheKey = this.builder.buildCacheKey(this, this.cacheKeys);
    return this.buildCacheKey;
  }
  
  public long getCacheMaxAge() {
    return this.cacheMaxAge;
  }
  
  public long getCacheSize() {
    return this.cacheSize;
  }
  
  public int getConnectTimeout() {
    return this.connectTimeout;
  }
  
  public Executor getExecutor() {
    return this.executor;
  }
  
  public HostnameVerifier getHostnameVerifier() {
    return this.hostnameVerifier;
  }
  
  public HttpRetryHandler getHttpRetryHandler() {
    return this.httpRetryHandler;
  }
  
  public int getLoadingUpdateMaxTimeSpan() {
    return this.loadingUpdateMaxTimeSpan;
  }
  
  public int getMaxRetryCount() {
    return this.maxRetryCount;
  }
  
  public Priority getPriority() {
    return this.priority;
  }
  
  public Proxy getProxy() {
    return this.proxy;
  }
  
  public int getReadTimeout() {
    return this.readTimeout;
  }
  
  public RedirectHandler getRedirectHandler() {
    return this.redirectHandler;
  }
  
  public RequestTracker getRequestTracker() {
    return this.requestTracker;
  }
  
  public String getSaveFilePath() {
    return this.saveFilePath;
  }
  
  public SSLSocketFactory getSslSocketFactory() {
    return this.sslSocketFactory;
  }
  
  public String getUri() {
    return TextUtils.isEmpty(this.buildUri) ? this.uri : this.buildUri;
  }
  
  void init() throws Throwable {
    if (TextUtils.isEmpty(this.buildUri)) {
      if (TextUtils.isEmpty(this.uri) && getHttpRequest() == null)
        throw new IllegalStateException("uri is empty && @HttpRequest == null"); 
      initEntityParams();
      this.buildUri = this.uri;
      HttpRequest httpRequest = getHttpRequest();
      if (httpRequest != null) {
        this.builder = httpRequest.builder().newInstance();
        this.buildUri = this.builder.buildUri(this, httpRequest);
        this.builder.buildParams(this);
        this.builder.buildSign(this, httpRequest.signs());
        if (this.sslSocketFactory == null)
          this.sslSocketFactory = this.builder.getSSLSocketFactory(); 
        return;
      } 
      if (this.builder != null) {
        this.builder.buildParams(this);
        this.builder.buildSign(this, this.signs);
        if (this.sslSocketFactory == null)
          this.sslSocketFactory = this.builder.getSSLSocketFactory(); 
      } 
    } 
  }
  
  public boolean isAutoRename() {
    return this.autoRename;
  }
  
  public boolean isAutoResume() {
    return this.autoResume;
  }
  
  public boolean isCancelFast() {
    return this.cancelFast;
  }
  
  public boolean isUseCookie() {
    return this.useCookie;
  }
  
  public void setAutoRename(boolean paramBoolean) {
    this.autoRename = paramBoolean;
  }
  
  public void setAutoResume(boolean paramBoolean) {
    this.autoResume = paramBoolean;
  }
  
  public void setCacheDirName(String paramString) {
    this.cacheDirName = paramString;
  }
  
  public void setCacheMaxAge(long paramLong) {
    this.cacheMaxAge = paramLong;
  }
  
  public void setCacheSize(long paramLong) {
    this.cacheSize = paramLong;
  }
  
  public void setCancelFast(boolean paramBoolean) {
    this.cancelFast = paramBoolean;
  }
  
  public void setConnectTimeout(int paramInt) {
    if (paramInt > 0)
      this.connectTimeout = paramInt; 
  }
  
  public void setExecutor(Executor paramExecutor) {
    this.executor = paramExecutor;
  }
  
  public void setHostnameVerifier(HostnameVerifier paramHostnameVerifier) {
    this.hostnameVerifier = paramHostnameVerifier;
  }
  
  public void setHttpRetryHandler(HttpRetryHandler paramHttpRetryHandler) {
    this.httpRetryHandler = paramHttpRetryHandler;
  }
  
  public void setLoadingUpdateMaxTimeSpan(int paramInt) {
    this.loadingUpdateMaxTimeSpan = paramInt;
  }
  
  public void setMaxRetryCount(int paramInt) {
    this.maxRetryCount = paramInt;
  }
  
  public void setPriority(Priority paramPriority) {
    this.priority = paramPriority;
  }
  
  public void setProxy(Proxy paramProxy) {
    this.proxy = paramProxy;
  }
  
  public void setReadTimeout(int paramInt) {
    if (paramInt > 0)
      this.readTimeout = paramInt; 
  }
  
  public void setRedirectHandler(RedirectHandler paramRedirectHandler) {
    this.redirectHandler = paramRedirectHandler;
  }
  
  public void setRequestTracker(RequestTracker paramRequestTracker) {
    this.requestTracker = paramRequestTracker;
  }
  
  public void setSaveFilePath(String paramString) {
    this.saveFilePath = paramString;
  }
  
  public void setSslSocketFactory(SSLSocketFactory paramSSLSocketFactory) {
    this.sslSocketFactory = paramSSLSocketFactory;
  }
  
  public void setUri(String paramString) {
    if (TextUtils.isEmpty(this.buildUri)) {
      this.uri = paramString;
      return;
    } 
    this.buildUri = paramString;
  }
  
  public void setUseCookie(boolean paramBoolean) {
    this.useCookie = paramBoolean;
  }
  
  public String toString() {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual init : ()V
    //   4: aload_0
    //   5: invokevirtual getUri : ()Ljava/lang/String;
    //   8: astore_1
    //   9: aload_1
    //   10: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   13: ifeq -> 35
    //   16: aload_0
    //   17: invokespecial toString : ()Ljava/lang/String;
    //   20: astore_1
    //   21: aload_1
    //   22: areturn
    //   23: astore_1
    //   24: aload_1
    //   25: invokevirtual getMessage : ()Ljava/lang/String;
    //   28: aload_1
    //   29: invokestatic e : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   32: goto -> 4
    //   35: new java/lang/StringBuilder
    //   38: dup
    //   39: invokespecial <init> : ()V
    //   42: aload_1
    //   43: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   46: astore_2
    //   47: aload_1
    //   48: ldc_w '?'
    //   51: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   54: ifeq -> 80
    //   57: ldc_w '&'
    //   60: astore_1
    //   61: aload_2
    //   62: aload_1
    //   63: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   66: aload_0
    //   67: invokespecial toString : ()Ljava/lang/String;
    //   70: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   73: invokevirtual toString : ()Ljava/lang/String;
    //   76: astore_1
    //   77: goto -> 21
    //   80: ldc_w '?'
    //   83: astore_1
    //   84: goto -> 61
    // Exception table:
    //   from	to	target	type
    //   0	4	23	java/lang/Throwable
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/RequestParams.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */