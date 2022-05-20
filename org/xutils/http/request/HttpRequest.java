package org.xutils.http.request;

import android.annotation.TargetApi;
import android.net.Uri;
import android.text.TextUtils;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.xutils.cache.DiskCacheEntity;
import org.xutils.cache.LruDiskCache;
import org.xutils.common.util.IOUtil;
import org.xutils.common.util.KeyValue;
import org.xutils.common.util.LogUtil;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.http.cookie.DbCookieStore;

public class HttpRequest extends UriRequest {
  private static final CookieManager COOKIE_MANAGER = new CookieManager((CookieStore)DbCookieStore.INSTANCE, CookiePolicy.ACCEPT_ALL);
  
  private String cacheKey = null;
  
  private HttpURLConnection connection = null;
  
  private InputStream inputStream = null;
  
  private boolean isLoading = false;
  
  private int responseCode = 0;
  
  HttpRequest(RequestParams paramRequestParams, Type paramType) throws Throwable {
    super(paramRequestParams, paramType);
  }
  
  private static String toGMTString(Date paramDate) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM y HH:mm:ss 'GMT'", Locale.US);
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    simpleDateFormat.setTimeZone(timeZone);
    (new GregorianCalendar(timeZone)).setTimeInMillis(paramDate.getTime());
    return simpleDateFormat.format(paramDate);
  }
  
  protected String buildQueryUrl(RequestParams paramRequestParams) {
    String str = paramRequestParams.getUri();
    StringBuilder stringBuilder = new StringBuilder(str);
    if (!str.contains("?")) {
      stringBuilder.append("?");
    } else if (!str.endsWith("?")) {
      stringBuilder.append("&");
    } 
    List list = paramRequestParams.getQueryStringParams();
    if (list != null)
      for (KeyValue keyValue : list) {
        String str2 = keyValue.key;
        String str1 = keyValue.getValueStr();
        if (!TextUtils.isEmpty(str2) && str1 != null)
          stringBuilder.append(Uri.encode(str2, paramRequestParams.getCharset())).append("=").append(Uri.encode(str1, paramRequestParams.getCharset())).append("&"); 
      }  
    if (stringBuilder.charAt(stringBuilder.length() - 1) == '&')
      stringBuilder.deleteCharAt(stringBuilder.length() - 1); 
    if (stringBuilder.charAt(stringBuilder.length() - 1) == '?')
      stringBuilder.deleteCharAt(stringBuilder.length() - 1); 
    return stringBuilder.toString();
  }
  
  public void clearCacheHeader() {
    this.params.setHeader("If-Modified-Since", null);
    this.params.setHeader("If-None-Match", null);
  }
  
  public void close() throws IOException {
    if (this.inputStream != null) {
      IOUtil.closeQuietly(this.inputStream);
      this.inputStream = null;
    } 
    if (this.connection != null)
      this.connection.disconnect(); 
  }
  
  public String getCacheKey() {
    if (this.cacheKey == null) {
      this.cacheKey = this.params.getCacheKey();
      if (TextUtils.isEmpty(this.cacheKey))
        this.cacheKey = this.params.toString(); 
    } 
    return this.cacheKey;
  }
  
  public long getContentLength() {
    long l = 0L;
    if (this.connection != null) {
      long l1;
      try {
        int i = this.connection.getContentLength();
        l1 = i;
      } catch (Throwable throwable) {
        LogUtil.e(throwable.getMessage(), throwable);
        l1 = l;
      } 
      l = l1;
      if (l1 < 1L)
        try {
          int i = getInputStream().available();
          l = i;
        } catch (Throwable throwable) {
          l = l1;
        }  
      return l;
    } 
    try {
      int i = getInputStream().available();
      l = i;
    } catch (Throwable throwable) {}
    return l;
  }
  
  public String getETag() {
    return (this.connection == null) ? null : this.connection.getHeaderField("ETag");
  }
  
  public long getExpiration() {
    // Byte code:
    //   0: aload_0
    //   1: getfield connection : Ljava/net/HttpURLConnection;
    //   4: ifnonnull -> 13
    //   7: ldc2_w -1
    //   10: lstore_1
    //   11: lload_1
    //   12: lreturn
    //   13: ldc2_w -1
    //   16: lstore_1
    //   17: aload_0
    //   18: getfield connection : Ljava/net/HttpURLConnection;
    //   21: ldc 'Cache-Control'
    //   23: invokevirtual getHeaderField : (Ljava/lang/String;)Ljava/lang/String;
    //   26: astore_3
    //   27: lload_1
    //   28: lstore #4
    //   30: aload_3
    //   31: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   34: ifne -> 141
    //   37: new java/util/StringTokenizer
    //   40: dup
    //   41: aload_3
    //   42: ldc_w ','
    //   45: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
    //   48: astore #6
    //   50: lload_1
    //   51: lstore #4
    //   53: aload #6
    //   55: invokevirtual hasMoreTokens : ()Z
    //   58: ifeq -> 141
    //   61: aload #6
    //   63: invokevirtual nextToken : ()Ljava/lang/String;
    //   66: invokevirtual trim : ()Ljava/lang/String;
    //   69: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   72: astore_3
    //   73: aload_3
    //   74: ldc_w 'max-age'
    //   77: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   80: ifeq -> 50
    //   83: aload_3
    //   84: bipush #61
    //   86: invokevirtual indexOf : (I)I
    //   89: istore #7
    //   91: lload_1
    //   92: lstore #4
    //   94: iload #7
    //   96: ifle -> 141
    //   99: aload_3
    //   100: iload #7
    //   102: iconst_1
    //   103: iadd
    //   104: invokevirtual substring : (I)Ljava/lang/String;
    //   107: invokevirtual trim : ()Ljava/lang/String;
    //   110: invokestatic parseLong : (Ljava/lang/String;)J
    //   113: lstore #8
    //   115: lload_1
    //   116: lstore #4
    //   118: lload #8
    //   120: lconst_0
    //   121: lcmp
    //   122: ifle -> 141
    //   125: invokestatic currentTimeMillis : ()J
    //   128: lstore #4
    //   130: lload #4
    //   132: ldc2_w 1000
    //   135: lload #8
    //   137: lmul
    //   138: ladd
    //   139: lstore #4
    //   141: lload #4
    //   143: lstore_1
    //   144: lload #4
    //   146: lconst_0
    //   147: lcmp
    //   148: ifgt -> 159
    //   151: aload_0
    //   152: getfield connection : Ljava/net/HttpURLConnection;
    //   155: invokevirtual getExpiration : ()J
    //   158: lstore_1
    //   159: lload_1
    //   160: lstore #4
    //   162: lload_1
    //   163: lconst_0
    //   164: lcmp
    //   165: ifgt -> 196
    //   168: lload_1
    //   169: lstore #4
    //   171: aload_0
    //   172: getfield params : Lorg/xutils/http/RequestParams;
    //   175: invokevirtual getCacheMaxAge : ()J
    //   178: lconst_0
    //   179: lcmp
    //   180: ifle -> 196
    //   183: invokestatic currentTimeMillis : ()J
    //   186: aload_0
    //   187: getfield params : Lorg/xutils/http/RequestParams;
    //   190: invokevirtual getCacheMaxAge : ()J
    //   193: ladd
    //   194: lstore #4
    //   196: lload #4
    //   198: lstore_1
    //   199: lload #4
    //   201: lconst_0
    //   202: lcmp
    //   203: ifgt -> 11
    //   206: ldc2_w 9223372036854775807
    //   209: lstore_1
    //   210: goto -> 11
    //   213: astore_3
    //   214: aload_3
    //   215: invokevirtual getMessage : ()Ljava/lang/String;
    //   218: aload_3
    //   219: invokestatic e : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   222: lload_1
    //   223: lstore #4
    //   225: goto -> 141
    // Exception table:
    //   from	to	target	type
    //   99	115	213	java/lang/Throwable
    //   125	130	213	java/lang/Throwable
  }
  
  public long getHeaderFieldDate(String paramString, long paramLong) {
    if (this.connection != null)
      paramLong = this.connection.getHeaderFieldDate(paramString, paramLong); 
    return paramLong;
  }
  
  public InputStream getInputStream() throws IOException {
    if (this.connection != null && this.inputStream == null) {
      InputStream inputStream;
      if (this.connection.getResponseCode() >= 400) {
        inputStream = this.connection.getErrorStream();
      } else {
        inputStream = this.connection.getInputStream();
      } 
      this.inputStream = inputStream;
    } 
    return this.inputStream;
  }
  
  public long getLastModified() {
    return getHeaderFieldDate("Last-Modified", System.currentTimeMillis());
  }
  
  public String getRequestUri() {
    String str1 = this.queryUrl;
    String str2 = str1;
    if (this.connection != null) {
      URL uRL = this.connection.getURL();
      str2 = str1;
      if (uRL != null)
        str2 = uRL.toString(); 
    } 
    return str2;
  }
  
  public int getResponseCode() throws IOException {
    return (this.connection != null) ? this.responseCode : ((getInputStream() != null) ? 200 : 404);
  }
  
  public String getResponseHeader(String paramString) {
    return (this.connection == null) ? null : this.connection.getHeaderField(paramString);
  }
  
  public Map<String, List<String>> getResponseHeaders() {
    return (this.connection == null) ? null : this.connection.getHeaderFields();
  }
  
  public String getResponseMessage() throws IOException {
    return (this.connection != null) ? URLDecoder.decode(this.connection.getResponseMessage(), this.params.getCharset()) : null;
  }
  
  public boolean isLoading() {
    return this.isLoading;
  }
  
  public Object loadResult() throws Throwable {
    this.isLoading = true;
    return super.loadResult();
  }
  
  public Object loadResultFromCache() throws Throwable {
    this.isLoading = true;
    null = LruDiskCache.getDiskCache(this.params.getCacheDirName()).setMaxSize(this.params.getCacheSize()).get(getCacheKey());
    if (null != null) {
      if (HttpMethod.permitsCache(this.params.getMethod())) {
        Date date = null.getLastModify();
        if (date.getTime() > 0L)
          this.params.setHeader("If-Modified-Since", toGMTString(date)); 
        String str = null.getEtag();
        if (!TextUtils.isEmpty(str))
          this.params.setHeader("If-None-Match", str); 
      } 
      return this.loader.loadFromCache(null);
    } 
    return null;
  }
  
  @TargetApi(19)
  public void sendRequest() throws Throwable {
    // Byte code:
    //   0: aload_0
    //   1: iconst_0
    //   2: putfield isLoading : Z
    //   5: aload_0
    //   6: iconst_0
    //   7: putfield responseCode : I
    //   10: new java/net/URL
    //   13: dup
    //   14: aload_0
    //   15: getfield queryUrl : Ljava/lang/String;
    //   18: invokespecial <init> : (Ljava/lang/String;)V
    //   21: astore_1
    //   22: aload_0
    //   23: getfield params : Lorg/xutils/http/RequestParams;
    //   26: invokevirtual getProxy : ()Ljava/net/Proxy;
    //   29: astore_2
    //   30: aload_2
    //   31: ifnull -> 313
    //   34: aload_0
    //   35: aload_1
    //   36: aload_2
    //   37: invokevirtual openConnection : (Ljava/net/Proxy;)Ljava/net/URLConnection;
    //   40: checkcast java/net/HttpURLConnection
    //   43: putfield connection : Ljava/net/HttpURLConnection;
    //   46: getstatic android/os/Build$VERSION.SDK_INT : I
    //   49: bipush #19
    //   51: if_icmpge -> 67
    //   54: aload_0
    //   55: getfield connection : Ljava/net/HttpURLConnection;
    //   58: ldc_w 'Connection'
    //   61: ldc_w 'close'
    //   64: invokevirtual setRequestProperty : (Ljava/lang/String;Ljava/lang/String;)V
    //   67: aload_0
    //   68: getfield connection : Ljava/net/HttpURLConnection;
    //   71: aload_0
    //   72: getfield params : Lorg/xutils/http/RequestParams;
    //   75: invokevirtual getReadTimeout : ()I
    //   78: invokevirtual setReadTimeout : (I)V
    //   81: aload_0
    //   82: getfield connection : Ljava/net/HttpURLConnection;
    //   85: aload_0
    //   86: getfield params : Lorg/xutils/http/RequestParams;
    //   89: invokevirtual getConnectTimeout : ()I
    //   92: invokevirtual setConnectTimeout : (I)V
    //   95: aload_0
    //   96: getfield connection : Ljava/net/HttpURLConnection;
    //   99: astore_2
    //   100: aload_0
    //   101: getfield params : Lorg/xutils/http/RequestParams;
    //   104: invokevirtual getRedirectHandler : ()Lorg/xutils/http/app/RedirectHandler;
    //   107: ifnonnull -> 327
    //   110: iconst_1
    //   111: istore_3
    //   112: aload_2
    //   113: iload_3
    //   114: invokevirtual setInstanceFollowRedirects : (Z)V
    //   117: aload_0
    //   118: getfield connection : Ljava/net/HttpURLConnection;
    //   121: instanceof javax/net/ssl/HttpsURLConnection
    //   124: ifeq -> 150
    //   127: aload_0
    //   128: getfield params : Lorg/xutils/http/RequestParams;
    //   131: invokevirtual getSslSocketFactory : ()Ljavax/net/ssl/SSLSocketFactory;
    //   134: astore_2
    //   135: aload_2
    //   136: ifnull -> 150
    //   139: aload_0
    //   140: getfield connection : Ljava/net/HttpURLConnection;
    //   143: checkcast javax/net/ssl/HttpsURLConnection
    //   146: aload_2
    //   147: invokevirtual setSSLSocketFactory : (Ljavax/net/ssl/SSLSocketFactory;)V
    //   150: aload_0
    //   151: getfield params : Lorg/xutils/http/RequestParams;
    //   154: invokevirtual isUseCookie : ()Z
    //   157: ifeq -> 222
    //   160: getstatic org/xutils/http/request/HttpRequest.COOKIE_MANAGER : Ljava/net/CookieManager;
    //   163: astore_2
    //   164: aload_1
    //   165: invokevirtual toURI : ()Ljava/net/URI;
    //   168: astore #4
    //   170: new java/util/HashMap
    //   173: astore #5
    //   175: aload #5
    //   177: iconst_0
    //   178: invokespecial <init> : (I)V
    //   181: aload_2
    //   182: aload #4
    //   184: aload #5
    //   186: invokevirtual get : (Ljava/net/URI;Ljava/util/Map;)Ljava/util/Map;
    //   189: ldc_w 'Cookie'
    //   192: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   197: checkcast java/util/List
    //   200: astore_2
    //   201: aload_2
    //   202: ifnull -> 222
    //   205: aload_0
    //   206: getfield connection : Ljava/net/HttpURLConnection;
    //   209: ldc_w 'Cookie'
    //   212: ldc_w ';'
    //   215: aload_2
    //   216: invokestatic join : (Ljava/lang/CharSequence;Ljava/lang/Iterable;)Ljava/lang/String;
    //   219: invokevirtual setRequestProperty : (Ljava/lang/String;Ljava/lang/String;)V
    //   222: aload_0
    //   223: getfield params : Lorg/xutils/http/RequestParams;
    //   226: invokevirtual getHeaders : ()Ljava/util/List;
    //   229: astore_2
    //   230: aload_2
    //   231: ifnull -> 358
    //   234: aload_2
    //   235: invokeinterface iterator : ()Ljava/util/Iterator;
    //   240: astore_2
    //   241: aload_2
    //   242: invokeinterface hasNext : ()Z
    //   247: ifeq -> 358
    //   250: aload_2
    //   251: invokeinterface next : ()Ljava/lang/Object;
    //   256: checkcast org/xutils/http/BaseParams$Header
    //   259: astore #6
    //   261: aload #6
    //   263: getfield key : Ljava/lang/String;
    //   266: astore #4
    //   268: aload #6
    //   270: invokevirtual getValueStr : ()Ljava/lang/String;
    //   273: astore #5
    //   275: aload #4
    //   277: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   280: ifne -> 241
    //   283: aload #5
    //   285: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   288: ifne -> 241
    //   291: aload #6
    //   293: getfield setHeader : Z
    //   296: ifeq -> 344
    //   299: aload_0
    //   300: getfield connection : Ljava/net/HttpURLConnection;
    //   303: aload #4
    //   305: aload #5
    //   307: invokevirtual setRequestProperty : (Ljava/lang/String;Ljava/lang/String;)V
    //   310: goto -> 241
    //   313: aload_0
    //   314: aload_1
    //   315: invokevirtual openConnection : ()Ljava/net/URLConnection;
    //   318: checkcast java/net/HttpURLConnection
    //   321: putfield connection : Ljava/net/HttpURLConnection;
    //   324: goto -> 46
    //   327: iconst_0
    //   328: istore_3
    //   329: goto -> 112
    //   332: astore_2
    //   333: aload_2
    //   334: invokevirtual getMessage : ()Ljava/lang/String;
    //   337: aload_2
    //   338: invokestatic e : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   341: goto -> 222
    //   344: aload_0
    //   345: getfield connection : Ljava/net/HttpURLConnection;
    //   348: aload #4
    //   350: aload #5
    //   352: invokevirtual addRequestProperty : (Ljava/lang/String;Ljava/lang/String;)V
    //   355: goto -> 241
    //   358: aload_0
    //   359: getfield requestInterceptListener : Lorg/xutils/http/app/RequestInterceptListener;
    //   362: ifnull -> 375
    //   365: aload_0
    //   366: getfield requestInterceptListener : Lorg/xutils/http/app/RequestInterceptListener;
    //   369: aload_0
    //   370: invokeinterface beforeRequest : (Lorg/xutils/http/request/UriRequest;)V
    //   375: aload_0
    //   376: getfield params : Lorg/xutils/http/RequestParams;
    //   379: invokevirtual getMethod : ()Lorg/xutils/http/HttpMethod;
    //   382: astore #4
    //   384: aload_0
    //   385: getfield connection : Ljava/net/HttpURLConnection;
    //   388: aload #4
    //   390: invokevirtual toString : ()Ljava/lang/String;
    //   393: invokevirtual setRequestMethod : (Ljava/lang/String;)V
    //   396: aload #4
    //   398: invokestatic permitsRequestBody : (Lorg/xutils/http/HttpMethod;)Z
    //   401: ifeq -> 529
    //   404: aload_0
    //   405: getfield params : Lorg/xutils/http/RequestParams;
    //   408: invokevirtual getRequestBody : ()Lorg/xutils/http/body/RequestBody;
    //   411: astore #4
    //   413: aload #4
    //   415: ifnull -> 529
    //   418: aload #4
    //   420: instanceof org/xutils/http/body/ProgressBody
    //   423: ifeq -> 440
    //   426: aload #4
    //   428: checkcast org/xutils/http/body/ProgressBody
    //   431: aload_0
    //   432: getfield progressHandler : Lorg/xutils/http/ProgressHandler;
    //   435: invokeinterface setProgressHandler : (Lorg/xutils/http/ProgressHandler;)V
    //   440: aload #4
    //   442: invokeinterface getContentType : ()Ljava/lang/String;
    //   447: astore_2
    //   448: aload_2
    //   449: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   452: ifne -> 466
    //   455: aload_0
    //   456: getfield connection : Ljava/net/HttpURLConnection;
    //   459: ldc_w 'Content-Type'
    //   462: aload_2
    //   463: invokevirtual setRequestProperty : (Ljava/lang/String;Ljava/lang/String;)V
    //   466: aload #4
    //   468: invokeinterface getContentLength : ()J
    //   473: lstore #7
    //   475: lload #7
    //   477: lconst_0
    //   478: lcmp
    //   479: ifge -> 663
    //   482: aload_0
    //   483: getfield connection : Ljava/net/HttpURLConnection;
    //   486: ldc_w 262144
    //   489: invokevirtual setChunkedStreamingMode : (I)V
    //   492: aload_0
    //   493: getfield connection : Ljava/net/HttpURLConnection;
    //   496: ldc_w 'Content-Length'
    //   499: lload #7
    //   501: invokestatic valueOf : (J)Ljava/lang/String;
    //   504: invokevirtual setRequestProperty : (Ljava/lang/String;Ljava/lang/String;)V
    //   507: aload_0
    //   508: getfield connection : Ljava/net/HttpURLConnection;
    //   511: iconst_1
    //   512: invokevirtual setDoOutput : (Z)V
    //   515: aload #4
    //   517: aload_0
    //   518: getfield connection : Ljava/net/HttpURLConnection;
    //   521: invokevirtual getOutputStream : ()Ljava/io/OutputStream;
    //   524: invokeinterface writeTo : (Ljava/io/OutputStream;)V
    //   529: aload_0
    //   530: getfield params : Lorg/xutils/http/RequestParams;
    //   533: invokevirtual isUseCookie : ()Z
    //   536: ifeq -> 562
    //   539: aload_0
    //   540: getfield connection : Ljava/net/HttpURLConnection;
    //   543: invokevirtual getHeaderFields : ()Ljava/util/Map;
    //   546: astore_2
    //   547: aload_2
    //   548: ifnull -> 562
    //   551: getstatic org/xutils/http/request/HttpRequest.COOKIE_MANAGER : Ljava/net/CookieManager;
    //   554: aload_1
    //   555: invokevirtual toURI : ()Ljava/net/URI;
    //   558: aload_2
    //   559: invokevirtual put : (Ljava/net/URI;Ljava/util/Map;)V
    //   562: aload_0
    //   563: aload_0
    //   564: getfield connection : Ljava/net/HttpURLConnection;
    //   567: invokevirtual getResponseCode : ()I
    //   570: putfield responseCode : I
    //   573: aload_0
    //   574: getfield requestInterceptListener : Lorg/xutils/http/app/RequestInterceptListener;
    //   577: ifnull -> 590
    //   580: aload_0
    //   581: getfield requestInterceptListener : Lorg/xutils/http/app/RequestInterceptListener;
    //   584: aload_0
    //   585: invokeinterface afterRequest : (Lorg/xutils/http/request/UriRequest;)V
    //   590: aload_0
    //   591: getfield responseCode : I
    //   594: sipush #204
    //   597: if_icmpeq -> 610
    //   600: aload_0
    //   601: getfield responseCode : I
    //   604: sipush #205
    //   607: if_icmpne -> 730
    //   610: new org/xutils/ex/HttpException
    //   613: dup
    //   614: aload_0
    //   615: getfield responseCode : I
    //   618: aload_0
    //   619: invokevirtual getResponseMessage : ()Ljava/lang/String;
    //   622: invokespecial <init> : (ILjava/lang/String;)V
    //   625: athrow
    //   626: astore_2
    //   627: ldc java/net/HttpURLConnection
    //   629: ldc_w 'method'
    //   632: invokevirtual getDeclaredField : (Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   635: astore #5
    //   637: aload #5
    //   639: iconst_1
    //   640: invokevirtual setAccessible : (Z)V
    //   643: aload #5
    //   645: aload_0
    //   646: getfield connection : Ljava/net/HttpURLConnection;
    //   649: aload #4
    //   651: invokevirtual toString : ()Ljava/lang/String;
    //   654: invokevirtual set : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   657: goto -> 396
    //   660: astore_1
    //   661: aload_2
    //   662: athrow
    //   663: lload #7
    //   665: ldc2_w 2147483647
    //   668: lcmp
    //   669: ifge -> 685
    //   672: aload_0
    //   673: getfield connection : Ljava/net/HttpURLConnection;
    //   676: lload #7
    //   678: l2i
    //   679: invokevirtual setFixedLengthStreamingMode : (I)V
    //   682: goto -> 492
    //   685: getstatic android/os/Build$VERSION.SDK_INT : I
    //   688: bipush #19
    //   690: if_icmplt -> 705
    //   693: aload_0
    //   694: getfield connection : Ljava/net/HttpURLConnection;
    //   697: lload #7
    //   699: invokevirtual setFixedLengthStreamingMode : (J)V
    //   702: goto -> 492
    //   705: aload_0
    //   706: getfield connection : Ljava/net/HttpURLConnection;
    //   709: ldc_w 262144
    //   712: invokevirtual setChunkedStreamingMode : (I)V
    //   715: goto -> 492
    //   718: astore_1
    //   719: aload_1
    //   720: invokevirtual getMessage : ()Ljava/lang/String;
    //   723: aload_1
    //   724: invokestatic e : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   727: goto -> 562
    //   730: aload_0
    //   731: getfield responseCode : I
    //   734: sipush #300
    //   737: if_icmplt -> 809
    //   740: new org/xutils/ex/HttpException
    //   743: dup
    //   744: aload_0
    //   745: getfield responseCode : I
    //   748: aload_0
    //   749: invokevirtual getResponseMessage : ()Ljava/lang/String;
    //   752: invokespecial <init> : (ILjava/lang/String;)V
    //   755: astore_1
    //   756: aload_1
    //   757: aload_0
    //   758: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   761: aload_0
    //   762: getfield params : Lorg/xutils/http/RequestParams;
    //   765: invokevirtual getCharset : ()Ljava/lang/String;
    //   768: invokestatic readStr : (Ljava/io/InputStream;Ljava/lang/String;)Ljava/lang/String;
    //   771: invokevirtual setResult : (Ljava/lang/String;)V
    //   774: new java/lang/StringBuilder
    //   777: dup
    //   778: invokespecial <init> : ()V
    //   781: aload_1
    //   782: invokevirtual toString : ()Ljava/lang/String;
    //   785: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   788: ldc_w ', url: '
    //   791: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   794: aload_0
    //   795: getfield queryUrl : Ljava/lang/String;
    //   798: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   801: invokevirtual toString : ()Ljava/lang/String;
    //   804: invokestatic e : (Ljava/lang/String;)V
    //   807: aload_1
    //   808: athrow
    //   809: aload_0
    //   810: iconst_1
    //   811: putfield isLoading : Z
    //   814: return
    //   815: astore_2
    //   816: goto -> 774
    // Exception table:
    //   from	to	target	type
    //   160	201	332	java/lang/Throwable
    //   205	222	332	java/lang/Throwable
    //   384	396	626	java/net/ProtocolException
    //   539	547	718	java/lang/Throwable
    //   551	562	718	java/lang/Throwable
    //   627	657	660	java/lang/Throwable
    //   756	774	815	java/lang/Throwable
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/request/HttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */