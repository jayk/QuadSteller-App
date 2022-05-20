package com.loopj.android.http;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;

public class AsyncHttpClient {
  public static final int DEFAULT_MAX_CONNECTIONS = 10;
  
  public static final int DEFAULT_MAX_RETRIES = 5;
  
  public static final int DEFAULT_RETRY_SLEEP_TIME_MILLIS = 1500;
  
  public static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
  
  public static final int DEFAULT_SOCKET_TIMEOUT = 10000;
  
  public static final String ENCODING_GZIP = "gzip";
  
  public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
  
  public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
  
  public static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
  
  public static final String HEADER_CONTENT_RANGE = "Content-Range";
  
  public static final String HEADER_CONTENT_TYPE = "Content-Type";
  
  public static final String LOG_TAG = "AsyncHttpClient";
  
  private final Map<String, String> clientHeaderMap;
  
  private int connectTimeout = 10000;
  
  private final DefaultHttpClient httpClient;
  
  private final HttpContext httpContext;
  
  private boolean isUrlEncodingEnabled = true;
  
  private int maxConnections = 10;
  
  private final Map<Context, List<RequestHandle>> requestMap;
  
  private int responseTimeout = 10000;
  
  private ExecutorService threadPool;
  
  public AsyncHttpClient() {
    this(false, 80, 443);
  }
  
  public AsyncHttpClient(int paramInt) {
    this(false, paramInt, 443);
  }
  
  public AsyncHttpClient(int paramInt1, int paramInt2) {
    this(false, paramInt1, paramInt2);
  }
  
  public AsyncHttpClient(SchemeRegistry paramSchemeRegistry) {
    BasicHttpParams basicHttpParams = new BasicHttpParams();
    ConnManagerParams.setTimeout((HttpParams)basicHttpParams, this.connectTimeout);
    ConnManagerParams.setMaxConnectionsPerRoute((HttpParams)basicHttpParams, (ConnPerRoute)new ConnPerRouteBean(this.maxConnections));
    ConnManagerParams.setMaxTotalConnections((HttpParams)basicHttpParams, 10);
    HttpConnectionParams.setSoTimeout((HttpParams)basicHttpParams, this.responseTimeout);
    HttpConnectionParams.setConnectionTimeout((HttpParams)basicHttpParams, this.connectTimeout);
    HttpConnectionParams.setTcpNoDelay((HttpParams)basicHttpParams, true);
    HttpConnectionParams.setSocketBufferSize((HttpParams)basicHttpParams, 8192);
    HttpProtocolParams.setVersion((HttpParams)basicHttpParams, (ProtocolVersion)HttpVersion.HTTP_1_1);
    ThreadSafeClientConnManager threadSafeClientConnManager = new ThreadSafeClientConnManager((HttpParams)basicHttpParams, paramSchemeRegistry);
    this.threadPool = getDefaultThreadPool();
    this.requestMap = Collections.synchronizedMap(new WeakHashMap<Context, List<RequestHandle>>());
    this.clientHeaderMap = new HashMap<String, String>();
    this.httpContext = (HttpContext)new SyncBasicHttpContext((HttpContext)new BasicHttpContext());
    this.httpClient = new DefaultHttpClient((ClientConnectionManager)threadSafeClientConnManager, (HttpParams)basicHttpParams);
    this.httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
          public void process(HttpRequest param1HttpRequest, HttpContext param1HttpContext) {
            if (!param1HttpRequest.containsHeader("Accept-Encoding"))
              param1HttpRequest.addHeader("Accept-Encoding", "gzip"); 
            for (String str : AsyncHttpClient.this.clientHeaderMap.keySet()) {
              if (param1HttpRequest.containsHeader(str)) {
                Header header = param1HttpRequest.getFirstHeader(str);
                Log.d("AsyncHttpClient", String.format("Headers were overwritten! (%s | %s) overwrites (%s | %s)", new Object[] { str, AsyncHttpClient.access$000(this.this$0).get(str), header.getName(), header.getValue() }));
                param1HttpRequest.removeHeader(header);
              } 
              param1HttpRequest.addHeader(str, (String)AsyncHttpClient.this.clientHeaderMap.get(str));
            } 
          }
        });
    this.httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
          public void process(HttpResponse param1HttpResponse, HttpContext param1HttpContext) {
            HttpEntity httpEntity = param1HttpResponse.getEntity();
            if (httpEntity != null) {
              Header header = httpEntity.getContentEncoding();
              if (header != null) {
                HeaderElement[] arrayOfHeaderElement = header.getElements();
                int i = arrayOfHeaderElement.length;
                byte b = 0;
                while (true) {
                  if (b < i) {
                    if (arrayOfHeaderElement[b].getName().equalsIgnoreCase("gzip")) {
                      param1HttpResponse.setEntity((HttpEntity)new AsyncHttpClient.InflatingEntity(httpEntity));
                      return;
                    } 
                    b++;
                    continue;
                  } 
                  return;
                } 
              } 
            } 
          }
        });
    this.httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
          public void process(HttpRequest param1HttpRequest, HttpContext param1HttpContext) throws HttpException, IOException {
            AuthState authState = (AuthState)param1HttpContext.getAttribute("http.auth.target-scope");
            CredentialsProvider credentialsProvider = (CredentialsProvider)param1HttpContext.getAttribute("http.auth.credentials-provider");
            HttpHost httpHost = (HttpHost)param1HttpContext.getAttribute("http.target_host");
            if (authState.getAuthScheme() == null) {
              Credentials credentials = credentialsProvider.getCredentials(new AuthScope(httpHost.getHostName(), httpHost.getPort()));
              if (credentials != null) {
                authState.setAuthScheme((AuthScheme)new BasicScheme());
                authState.setCredentials(credentials);
              } 
            } 
          }
        }0);
    this.httpClient.setHttpRequestRetryHandler(new RetryHandler(5, 1500));
  }
  
  public AsyncHttpClient(boolean paramBoolean, int paramInt1, int paramInt2) {
    this(getDefaultSchemeRegistry(paramBoolean, paramInt1, paramInt2));
  }
  
  private HttpEntityEnclosingRequestBase addEntityToRequestBase(HttpEntityEnclosingRequestBase paramHttpEntityEnclosingRequestBase, HttpEntity paramHttpEntity) {
    if (paramHttpEntity != null)
      paramHttpEntityEnclosingRequestBase.setEntity(paramHttpEntity); 
    return paramHttpEntityEnclosingRequestBase;
  }
  
  public static void allowRetryExceptionClass(Class<?> paramClass) {
    if (paramClass != null)
      RetryHandler.addClassToWhitelist(paramClass); 
  }
  
  public static void blockRetryExceptionClass(Class<?> paramClass) {
    if (paramClass != null)
      RetryHandler.addClassToBlacklist(paramClass); 
  }
  
  public static void endEntityViaReflection(HttpEntity paramHttpEntity) {
    if (paramHttpEntity instanceof HttpEntityWrapper) {
      Field field = null;
      try {
        Field[] arrayOfField = HttpEntityWrapper.class.getDeclaredFields();
        int i = arrayOfField.length;
        byte b = 0;
        while (true) {
          Field field1 = field;
          if (b < i) {
            field1 = arrayOfField[b];
            if (!field1.getName().equals("wrappedEntity")) {
              b++;
              continue;
            } 
          } 
          if (field1 != null) {
            field1.setAccessible(true);
            paramHttpEntity = (HttpEntity)field1.get(paramHttpEntity);
            if (paramHttpEntity != null)
              paramHttpEntity.consumeContent(); 
          } 
          return;
        } 
      } catch (Throwable throwable) {
        Log.e("AsyncHttpClient", "wrappedEntity consume", throwable);
      } 
    } 
  }
  
  private static SchemeRegistry getDefaultSchemeRegistry(boolean paramBoolean, int paramInt1, int paramInt2) {
    if (paramBoolean)
      Log.d("AsyncHttpClient", "Beware! Using the fix is insecure, as it doesn't verify SSL certificates."); 
    int i = paramInt1;
    if (paramInt1 < 1) {
      i = 80;
      Log.d("AsyncHttpClient", "Invalid HTTP port number specified, defaulting to 80");
    } 
    paramInt1 = paramInt2;
    if (paramInt2 < 1) {
      paramInt1 = 443;
      Log.d("AsyncHttpClient", "Invalid HTTPS port number specified, defaulting to 443");
    } 
    if (paramBoolean) {
      SSLSocketFactory sSLSocketFactory1 = MySSLSocketFactory.getFixedSocketFactory();
      SchemeRegistry schemeRegistry1 = new SchemeRegistry();
      schemeRegistry1.register(new Scheme("http", (SocketFactory)PlainSocketFactory.getSocketFactory(), i));
      schemeRegistry1.register(new Scheme("https", (SocketFactory)sSLSocketFactory1, paramInt1));
      return schemeRegistry1;
    } 
    SSLSocketFactory sSLSocketFactory = SSLSocketFactory.getSocketFactory();
    SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(new Scheme("http", (SocketFactory)PlainSocketFactory.getSocketFactory(), i));
    schemeRegistry.register(new Scheme("https", (SocketFactory)sSLSocketFactory, paramInt1));
    return schemeRegistry;
  }
  
  public static String getUrlWithQueryString(boolean paramBoolean, String paramString, RequestParams paramRequestParams) {
    if (paramString == null)
      return null; 
    String str = paramString;
    if (paramBoolean)
      str = paramString.replace(" ", "%20"); 
    paramString = str;
    if (paramRequestParams != null) {
      String str1 = paramRequestParams.getParamString().trim();
      paramString = str;
      if (!str1.equals("")) {
        paramString = str;
        if (!str1.equals("?")) {
          StringBuilder stringBuilder = (new StringBuilder()).append(str);
          if (str.contains("?")) {
            paramString = "&";
          } else {
            paramString = "?";
          } 
          paramString = stringBuilder.append(paramString).toString();
          paramString = paramString + str1;
        } 
      } 
    } 
    return paramString;
  }
  
  public static boolean isInputStreamGZIPCompressed(PushbackInputStream paramPushbackInputStream) throws IOException {
    boolean bool1 = true;
    boolean bool2 = false;
    if (paramPushbackInputStream == null)
      return bool2; 
    byte[] arrayOfByte = new byte[2];
    int i = paramPushbackInputStream.read(arrayOfByte);
    paramPushbackInputStream.unread(arrayOfByte);
    byte b1 = arrayOfByte[0];
    byte b2 = arrayOfByte[1];
    if (i != 2 || 35615 != (b1 & 0xFF | b2 << 8 & 0xFF00))
      bool1 = false; 
    return bool1;
  }
  
  private HttpEntity paramsToEntity(RequestParams paramRequestParams, ResponseHandlerInterface paramResponseHandlerInterface) {
    HttpEntity httpEntity1 = null;
    HttpEntity httpEntity2 = httpEntity1;
    if (paramRequestParams != null)
      try {
        httpEntity2 = paramRequestParams.getEntity(paramResponseHandlerInterface);
      } catch (IOException iOException) {} 
    return httpEntity2;
  }
  
  public static void silentCloseInputStream(InputStream paramInputStream) {
    if (paramInputStream != null)
      try {
        paramInputStream.close();
      } catch (IOException iOException) {
        Log.w("AsyncHttpClient", "Cannot close input stream", iOException);
      }  
  }
  
  public static void silentCloseOutputStream(OutputStream paramOutputStream) {
    if (paramOutputStream != null)
      try {
        paramOutputStream.close();
      } catch (IOException iOException) {
        Log.w("AsyncHttpClient", "Cannot close output stream", iOException);
      }  
  }
  
  public void addHeader(String paramString1, String paramString2) {
    this.clientHeaderMap.put(paramString1, paramString2);
  }
  
  public void cancelAllRequests(boolean paramBoolean) {
    for (List<RequestHandle> list : this.requestMap.values()) {
      if (list != null) {
        Iterator<RequestHandle> iterator = list.iterator();
        while (iterator.hasNext())
          ((RequestHandle)iterator.next()).cancel(paramBoolean); 
      } 
    } 
    this.requestMap.clear();
  }
  
  public void cancelRequests(final Context context, final boolean mayInterruptIfRunning) {
    if (context == null) {
      Log.e("AsyncHttpClient", "Passed null Context to cancelRequests");
      return;
    } 
    Runnable runnable = new Runnable() {
        public void run() {
          List list = (List)AsyncHttpClient.this.requestMap.get(context);
          if (list != null) {
            Iterator<RequestHandle> iterator = list.iterator();
            while (iterator.hasNext())
              ((RequestHandle)iterator.next()).cancel(mayInterruptIfRunning); 
            AsyncHttpClient.this.requestMap.remove(context);
          } 
        }
      };
    if (Looper.myLooper() == Looper.getMainLooper()) {
      (new Thread(runnable)).start();
      return;
    } 
    runnable.run();
  }
  
  @Deprecated
  public void clearBasicAuth() {
    clearCredentialsProvider();
  }
  
  public void clearCredentialsProvider() {
    this.httpClient.getCredentialsProvider().clear();
  }
  
  public RequestHandle delete(Context paramContext, String paramString, ResponseHandlerInterface paramResponseHandlerInterface) {
    HttpDelete httpDelete = new HttpDelete(URI.create(paramString).normalize());
    return sendRequest(this.httpClient, this.httpContext, (HttpUriRequest)httpDelete, null, paramResponseHandlerInterface, paramContext);
  }
  
  public RequestHandle delete(Context paramContext, String paramString, Header[] paramArrayOfHeader, RequestParams paramRequestParams, ResponseHandlerInterface paramResponseHandlerInterface) {
    HttpDelete httpDelete = new HttpDelete(getUrlWithQueryString(this.isUrlEncodingEnabled, paramString, paramRequestParams));
    if (paramArrayOfHeader != null)
      httpDelete.setHeaders(paramArrayOfHeader); 
    return sendRequest(this.httpClient, this.httpContext, (HttpUriRequest)httpDelete, null, paramResponseHandlerInterface, paramContext);
  }
  
  public RequestHandle delete(Context paramContext, String paramString, Header[] paramArrayOfHeader, ResponseHandlerInterface paramResponseHandlerInterface) {
    HttpDelete httpDelete = new HttpDelete(URI.create(paramString).normalize());
    if (paramArrayOfHeader != null)
      httpDelete.setHeaders(paramArrayOfHeader); 
    return sendRequest(this.httpClient, this.httpContext, (HttpUriRequest)httpDelete, null, paramResponseHandlerInterface, paramContext);
  }
  
  public RequestHandle delete(String paramString, ResponseHandlerInterface paramResponseHandlerInterface) {
    return delete(null, paramString, paramResponseHandlerInterface);
  }
  
  public RequestHandle get(Context paramContext, String paramString, RequestParams paramRequestParams, ResponseHandlerInterface paramResponseHandlerInterface) {
    return sendRequest(this.httpClient, this.httpContext, (HttpUriRequest)new HttpGet(getUrlWithQueryString(this.isUrlEncodingEnabled, paramString, paramRequestParams)), null, paramResponseHandlerInterface, paramContext);
  }
  
  public RequestHandle get(Context paramContext, String paramString, ResponseHandlerInterface paramResponseHandlerInterface) {
    return get(paramContext, paramString, null, paramResponseHandlerInterface);
  }
  
  public RequestHandle get(Context paramContext, String paramString, Header[] paramArrayOfHeader, RequestParams paramRequestParams, ResponseHandlerInterface paramResponseHandlerInterface) {
    HttpGet httpGet = new HttpGet(getUrlWithQueryString(this.isUrlEncodingEnabled, paramString, paramRequestParams));
    if (paramArrayOfHeader != null)
      httpGet.setHeaders(paramArrayOfHeader); 
    return sendRequest(this.httpClient, this.httpContext, (HttpUriRequest)httpGet, null, paramResponseHandlerInterface, paramContext);
  }
  
  public RequestHandle get(String paramString, RequestParams paramRequestParams, ResponseHandlerInterface paramResponseHandlerInterface) {
    return get(null, paramString, paramRequestParams, paramResponseHandlerInterface);
  }
  
  public RequestHandle get(String paramString, ResponseHandlerInterface paramResponseHandlerInterface) {
    return get(null, paramString, null, paramResponseHandlerInterface);
  }
  
  public int getConnectTimeout() {
    return this.connectTimeout;
  }
  
  protected ExecutorService getDefaultThreadPool() {
    return Executors.newCachedThreadPool();
  }
  
  public HttpClient getHttpClient() {
    return (HttpClient)this.httpClient;
  }
  
  public HttpContext getHttpContext() {
    return this.httpContext;
  }
  
  public int getMaxConnections() {
    return this.maxConnections;
  }
  
  public int getResponseTimeout() {
    return this.responseTimeout;
  }
  
  public ExecutorService getThreadPool() {
    return this.threadPool;
  }
  
  public int getTimeout() {
    return this.connectTimeout;
  }
  
  public RequestHandle head(Context paramContext, String paramString, RequestParams paramRequestParams, ResponseHandlerInterface paramResponseHandlerInterface) {
    return sendRequest(this.httpClient, this.httpContext, (HttpUriRequest)new HttpHead(getUrlWithQueryString(this.isUrlEncodingEnabled, paramString, paramRequestParams)), null, paramResponseHandlerInterface, paramContext);
  }
  
  public RequestHandle head(Context paramContext, String paramString, ResponseHandlerInterface paramResponseHandlerInterface) {
    return head(paramContext, paramString, null, paramResponseHandlerInterface);
  }
  
  public RequestHandle head(Context paramContext, String paramString, Header[] paramArrayOfHeader, RequestParams paramRequestParams, ResponseHandlerInterface paramResponseHandlerInterface) {
    HttpHead httpHead = new HttpHead(getUrlWithQueryString(this.isUrlEncodingEnabled, paramString, paramRequestParams));
    if (paramArrayOfHeader != null)
      httpHead.setHeaders(paramArrayOfHeader); 
    return sendRequest(this.httpClient, this.httpContext, (HttpUriRequest)httpHead, null, paramResponseHandlerInterface, paramContext);
  }
  
  public RequestHandle head(String paramString, RequestParams paramRequestParams, ResponseHandlerInterface paramResponseHandlerInterface) {
    return head(null, paramString, paramRequestParams, paramResponseHandlerInterface);
  }
  
  public RequestHandle head(String paramString, ResponseHandlerInterface paramResponseHandlerInterface) {
    return head(null, paramString, null, paramResponseHandlerInterface);
  }
  
  public boolean isUrlEncodingEnabled() {
    return this.isUrlEncodingEnabled;
  }
  
  protected AsyncHttpRequest newAsyncHttpRequest(DefaultHttpClient paramDefaultHttpClient, HttpContext paramHttpContext, HttpUriRequest paramHttpUriRequest, String paramString, ResponseHandlerInterface paramResponseHandlerInterface, Context paramContext) {
    return new AsyncHttpRequest((AbstractHttpClient)paramDefaultHttpClient, paramHttpContext, paramHttpUriRequest, paramResponseHandlerInterface);
  }
  
  public RequestHandle post(Context paramContext, String paramString, RequestParams paramRequestParams, ResponseHandlerInterface paramResponseHandlerInterface) {
    return post(paramContext, paramString, paramsToEntity(paramRequestParams, paramResponseHandlerInterface), null, paramResponseHandlerInterface);
  }
  
  public RequestHandle post(Context paramContext, String paramString1, HttpEntity paramHttpEntity, String paramString2, ResponseHandlerInterface paramResponseHandlerInterface) {
    return sendRequest(this.httpClient, this.httpContext, (HttpUriRequest)addEntityToRequestBase((HttpEntityEnclosingRequestBase)new HttpPost(URI.create(paramString1).normalize()), paramHttpEntity), paramString2, paramResponseHandlerInterface, paramContext);
  }
  
  public RequestHandle post(Context paramContext, String paramString1, Header[] paramArrayOfHeader, RequestParams paramRequestParams, String paramString2, ResponseHandlerInterface paramResponseHandlerInterface) {
    HttpPost httpPost = new HttpPost(URI.create(paramString1).normalize());
    if (paramRequestParams != null)
      httpPost.setEntity(paramsToEntity(paramRequestParams, paramResponseHandlerInterface)); 
    if (paramArrayOfHeader != null)
      httpPost.setHeaders(paramArrayOfHeader); 
    return sendRequest(this.httpClient, this.httpContext, (HttpUriRequest)httpPost, paramString2, paramResponseHandlerInterface, paramContext);
  }
  
  public RequestHandle post(Context paramContext, String paramString1, Header[] paramArrayOfHeader, HttpEntity paramHttpEntity, String paramString2, ResponseHandlerInterface paramResponseHandlerInterface) {
    HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase = addEntityToRequestBase((HttpEntityEnclosingRequestBase)new HttpPost(URI.create(paramString1).normalize()), paramHttpEntity);
    if (paramArrayOfHeader != null)
      httpEntityEnclosingRequestBase.setHeaders(paramArrayOfHeader); 
    return sendRequest(this.httpClient, this.httpContext, (HttpUriRequest)httpEntityEnclosingRequestBase, paramString2, paramResponseHandlerInterface, paramContext);
  }
  
  public RequestHandle post(String paramString, RequestParams paramRequestParams, ResponseHandlerInterface paramResponseHandlerInterface) {
    return post(null, paramString, paramRequestParams, paramResponseHandlerInterface);
  }
  
  public RequestHandle post(String paramString, ResponseHandlerInterface paramResponseHandlerInterface) {
    return post(null, paramString, null, paramResponseHandlerInterface);
  }
  
  public RequestHandle put(Context paramContext, String paramString, RequestParams paramRequestParams, ResponseHandlerInterface paramResponseHandlerInterface) {
    return put(paramContext, paramString, paramsToEntity(paramRequestParams, paramResponseHandlerInterface), null, paramResponseHandlerInterface);
  }
  
  public RequestHandle put(Context paramContext, String paramString1, HttpEntity paramHttpEntity, String paramString2, ResponseHandlerInterface paramResponseHandlerInterface) {
    return sendRequest(this.httpClient, this.httpContext, (HttpUriRequest)addEntityToRequestBase((HttpEntityEnclosingRequestBase)new HttpPut(URI.create(paramString1).normalize()), paramHttpEntity), paramString2, paramResponseHandlerInterface, paramContext);
  }
  
  public RequestHandle put(Context paramContext, String paramString1, Header[] paramArrayOfHeader, HttpEntity paramHttpEntity, String paramString2, ResponseHandlerInterface paramResponseHandlerInterface) {
    HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase = addEntityToRequestBase((HttpEntityEnclosingRequestBase)new HttpPut(URI.create(paramString1).normalize()), paramHttpEntity);
    if (paramArrayOfHeader != null)
      httpEntityEnclosingRequestBase.setHeaders(paramArrayOfHeader); 
    return sendRequest(this.httpClient, this.httpContext, (HttpUriRequest)httpEntityEnclosingRequestBase, paramString2, paramResponseHandlerInterface, paramContext);
  }
  
  public RequestHandle put(String paramString, RequestParams paramRequestParams, ResponseHandlerInterface paramResponseHandlerInterface) {
    return put(null, paramString, paramRequestParams, paramResponseHandlerInterface);
  }
  
  public RequestHandle put(String paramString, ResponseHandlerInterface paramResponseHandlerInterface) {
    return put(null, paramString, null, paramResponseHandlerInterface);
  }
  
  public void removeAllHeaders() {
    this.clientHeaderMap.clear();
  }
  
  public void removeHeader(String paramString) {
    this.clientHeaderMap.remove(paramString);
  }
  
  protected RequestHandle sendRequest(DefaultHttpClient paramDefaultHttpClient, HttpContext paramHttpContext, HttpUriRequest paramHttpUriRequest, String paramString, ResponseHandlerInterface paramResponseHandlerInterface, Context paramContext) {
    // Byte code:
    //   0: aload_3
    //   1: ifnonnull -> 15
    //   4: new java/lang/IllegalArgumentException
    //   7: dup
    //   8: ldc_w 'HttpUriRequest must not be null'
    //   11: invokespecial <init> : (Ljava/lang/String;)V
    //   14: athrow
    //   15: aload #5
    //   17: ifnonnull -> 31
    //   20: new java/lang/IllegalArgumentException
    //   23: dup
    //   24: ldc_w 'ResponseHandler must not be null'
    //   27: invokespecial <init> : (Ljava/lang/String;)V
    //   30: athrow
    //   31: aload #5
    //   33: invokeinterface getUseSynchronousMode : ()Z
    //   38: ifeq -> 52
    //   41: new java/lang/IllegalArgumentException
    //   44: dup
    //   45: ldc_w 'Synchronous ResponseHandler used in AsyncHttpClient. You should create your response handler in a looper thread or use SyncHttpClient instead.'
    //   48: invokespecial <init> : (Ljava/lang/String;)V
    //   51: athrow
    //   52: aload #4
    //   54: ifnull -> 83
    //   57: aload_3
    //   58: instanceof org/apache/http/client/methods/HttpEntityEnclosingRequestBase
    //   61: ifeq -> 274
    //   64: aload_3
    //   65: checkcast org/apache/http/client/methods/HttpEntityEnclosingRequestBase
    //   68: invokevirtual getEntity : ()Lorg/apache/http/HttpEntity;
    //   71: ifnull -> 274
    //   74: ldc 'AsyncHttpClient'
    //   76: ldc_w 'Passed contentType will be ignored because HttpEntity sets content type'
    //   79: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
    //   82: pop
    //   83: aload #5
    //   85: aload_3
    //   86: invokeinterface getAllHeaders : ()[Lorg/apache/http/Header;
    //   91: invokeinterface setRequestHeaders : ([Lorg/apache/http/Header;)V
    //   96: aload #5
    //   98: aload_3
    //   99: invokeinterface getURI : ()Ljava/net/URI;
    //   104: invokeinterface setRequestURI : (Ljava/net/URI;)V
    //   109: aload_0
    //   110: aload_1
    //   111: aload_2
    //   112: aload_3
    //   113: aload #4
    //   115: aload #5
    //   117: aload #6
    //   119: invokevirtual newAsyncHttpRequest : (Lorg/apache/http/impl/client/DefaultHttpClient;Lorg/apache/http/protocol/HttpContext;Lorg/apache/http/client/methods/HttpUriRequest;Ljava/lang/String;Lcom/loopj/android/http/ResponseHandlerInterface;Landroid/content/Context;)Lcom/loopj/android/http/AsyncHttpRequest;
    //   122: astore_1
    //   123: aload_0
    //   124: getfield threadPool : Ljava/util/concurrent/ExecutorService;
    //   127: aload_1
    //   128: invokeinterface submit : (Ljava/lang/Runnable;)Ljava/util/concurrent/Future;
    //   133: pop
    //   134: new com/loopj/android/http/RequestHandle
    //   137: dup
    //   138: aload_1
    //   139: invokespecial <init> : (Lcom/loopj/android/http/AsyncHttpRequest;)V
    //   142: astore #7
    //   144: aload #6
    //   146: ifnull -> 293
    //   149: aload_0
    //   150: getfield requestMap : Ljava/util/Map;
    //   153: aload #6
    //   155: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   160: checkcast java/util/List
    //   163: astore_2
    //   164: aload_0
    //   165: getfield requestMap : Ljava/util/Map;
    //   168: astore #4
    //   170: aload #4
    //   172: monitorenter
    //   173: aload_2
    //   174: astore_1
    //   175: aload_2
    //   176: ifnonnull -> 205
    //   179: new java/util/LinkedList
    //   182: astore_1
    //   183: aload_1
    //   184: invokespecial <init> : ()V
    //   187: aload_1
    //   188: invokestatic synchronizedList : (Ljava/util/List;)Ljava/util/List;
    //   191: astore_1
    //   192: aload_0
    //   193: getfield requestMap : Ljava/util/Map;
    //   196: aload #6
    //   198: aload_1
    //   199: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   204: pop
    //   205: aload #4
    //   207: monitorexit
    //   208: aload #5
    //   210: instanceof com/loopj/android/http/RangeFileAsyncHttpResponseHandler
    //   213: ifeq -> 225
    //   216: aload #5
    //   218: checkcast com/loopj/android/http/RangeFileAsyncHttpResponseHandler
    //   221: aload_3
    //   222: invokevirtual updateRequestHeaders : (Lorg/apache/http/client/methods/HttpUriRequest;)V
    //   225: aload_1
    //   226: aload #7
    //   228: invokeinterface add : (Ljava/lang/Object;)Z
    //   233: pop
    //   234: aload_1
    //   235: invokeinterface iterator : ()Ljava/util/Iterator;
    //   240: astore_1
    //   241: aload_1
    //   242: invokeinterface hasNext : ()Z
    //   247: ifeq -> 293
    //   250: aload_1
    //   251: invokeinterface next : ()Ljava/lang/Object;
    //   256: checkcast com/loopj/android/http/RequestHandle
    //   259: invokevirtual shouldBeGarbageCollected : ()Z
    //   262: ifeq -> 241
    //   265: aload_1
    //   266: invokeinterface remove : ()V
    //   271: goto -> 241
    //   274: aload_3
    //   275: ldc 'Content-Type'
    //   277: aload #4
    //   279: invokeinterface setHeader : (Ljava/lang/String;Ljava/lang/String;)V
    //   284: goto -> 83
    //   287: astore_1
    //   288: aload #4
    //   290: monitorexit
    //   291: aload_1
    //   292: athrow
    //   293: aload #7
    //   295: areturn
    // Exception table:
    //   from	to	target	type
    //   179	205	287	finally
    //   205	208	287	finally
    //   288	291	287	finally
  }
  
  public void setAuthenticationPreemptive(boolean paramBoolean) {
    if (paramBoolean) {
      this.httpClient.addRequestInterceptor(new PreemtiveAuthorizationHttpRequestInterceptor(), 0);
      return;
    } 
    this.httpClient.removeRequestInterceptorByClass(PreemtiveAuthorizationHttpRequestInterceptor.class);
  }
  
  public void setBasicAuth(String paramString1, String paramString2) {
    setBasicAuth(paramString1, paramString2, false);
  }
  
  public void setBasicAuth(String paramString1, String paramString2, AuthScope paramAuthScope) {
    setBasicAuth(paramString1, paramString2, paramAuthScope, false);
  }
  
  public void setBasicAuth(String paramString1, String paramString2, AuthScope paramAuthScope, boolean paramBoolean) {
    setCredentials(paramAuthScope, (Credentials)new UsernamePasswordCredentials(paramString1, paramString2));
    setAuthenticationPreemptive(paramBoolean);
  }
  
  public void setBasicAuth(String paramString1, String paramString2, boolean paramBoolean) {
    setBasicAuth(paramString1, paramString2, null, paramBoolean);
  }
  
  public void setConnectTimeout(int paramInt) {
    int i = paramInt;
    if (paramInt < 1000)
      i = 10000; 
    this.connectTimeout = i;
    HttpParams httpParams = this.httpClient.getParams();
    ConnManagerParams.setTimeout(httpParams, this.connectTimeout);
    HttpConnectionParams.setConnectionTimeout(httpParams, this.connectTimeout);
  }
  
  public void setCookieStore(CookieStore paramCookieStore) {
    this.httpContext.setAttribute("http.cookie-store", paramCookieStore);
  }
  
  public void setCredentials(AuthScope paramAuthScope, Credentials paramCredentials) {
    if (paramCredentials == null) {
      Log.d("AsyncHttpClient", "Provided credentials are null, not setting");
      return;
    } 
    CredentialsProvider credentialsProvider = this.httpClient.getCredentialsProvider();
    AuthScope authScope = paramAuthScope;
    if (paramAuthScope == null)
      authScope = AuthScope.ANY; 
    credentialsProvider.setCredentials(authScope, paramCredentials);
  }
  
  public void setEnableRedirects(boolean paramBoolean) {
    setEnableRedirects(paramBoolean, paramBoolean, paramBoolean);
  }
  
  public void setEnableRedirects(boolean paramBoolean1, boolean paramBoolean2) {
    setEnableRedirects(paramBoolean1, paramBoolean2, true);
  }
  
  public void setEnableRedirects(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
    HttpParams httpParams = this.httpClient.getParams();
    if (!paramBoolean2) {
      paramBoolean2 = true;
    } else {
      paramBoolean2 = false;
    } 
    httpParams.setBooleanParameter("http.protocol.reject-relative-redirect", paramBoolean2);
    this.httpClient.getParams().setBooleanParameter("http.protocol.allow-circular-redirects", paramBoolean3);
    this.httpClient.setRedirectHandler((RedirectHandler)new MyRedirectHandler(paramBoolean1));
  }
  
  public void setMaxConnections(int paramInt) {
    int i = paramInt;
    if (paramInt < 1)
      i = 10; 
    this.maxConnections = i;
    ConnManagerParams.setMaxConnectionsPerRoute(this.httpClient.getParams(), (ConnPerRoute)new ConnPerRouteBean(this.maxConnections));
  }
  
  public void setMaxRetriesAndTimeout(int paramInt1, int paramInt2) {
    this.httpClient.setHttpRequestRetryHandler(new RetryHandler(paramInt1, paramInt2));
  }
  
  public void setProxy(String paramString, int paramInt) {
    HttpHost httpHost = new HttpHost(paramString, paramInt);
    this.httpClient.getParams().setParameter("http.route.default-proxy", httpHost);
  }
  
  public void setProxy(String paramString1, int paramInt, String paramString2, String paramString3) {
    this.httpClient.getCredentialsProvider().setCredentials(new AuthScope(paramString1, paramInt), (Credentials)new UsernamePasswordCredentials(paramString2, paramString3));
    HttpHost httpHost = new HttpHost(paramString1, paramInt);
    this.httpClient.getParams().setParameter("http.route.default-proxy", httpHost);
  }
  
  public void setRedirectHandler(RedirectHandler paramRedirectHandler) {
    this.httpClient.setRedirectHandler(paramRedirectHandler);
  }
  
  public void setResponseTimeout(int paramInt) {
    int i = paramInt;
    if (paramInt < 1000)
      i = 10000; 
    this.responseTimeout = i;
    HttpConnectionParams.setSoTimeout(this.httpClient.getParams(), this.responseTimeout);
  }
  
  public void setSSLSocketFactory(SSLSocketFactory paramSSLSocketFactory) {
    this.httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", (SocketFactory)paramSSLSocketFactory, 443));
  }
  
  public void setThreadPool(ExecutorService paramExecutorService) {
    this.threadPool = paramExecutorService;
  }
  
  public void setTimeout(int paramInt) {
    int i = paramInt;
    if (paramInt < 1000)
      i = 10000; 
    setConnectTimeout(i);
    setResponseTimeout(i);
  }
  
  public void setURLEncodingEnabled(boolean paramBoolean) {
    this.isUrlEncodingEnabled = paramBoolean;
  }
  
  public void setUserAgent(String paramString) {
    HttpProtocolParams.setUserAgent(this.httpClient.getParams(), paramString);
  }
  
  private static class InflatingEntity extends HttpEntityWrapper {
    GZIPInputStream gzippedStream;
    
    PushbackInputStream pushbackStream;
    
    InputStream wrappedStream;
    
    public InflatingEntity(HttpEntity param1HttpEntity) {
      super(param1HttpEntity);
    }
    
    public void consumeContent() throws IOException {
      AsyncHttpClient.silentCloseInputStream(this.wrappedStream);
      AsyncHttpClient.silentCloseInputStream(this.pushbackStream);
      AsyncHttpClient.silentCloseInputStream(this.gzippedStream);
      super.consumeContent();
    }
    
    public InputStream getContent() throws IOException {
      this.wrappedStream = this.wrappedEntity.getContent();
      this.pushbackStream = new PushbackInputStream(this.wrappedStream, 2);
      if (AsyncHttpClient.isInputStreamGZIPCompressed(this.pushbackStream)) {
        this.gzippedStream = new GZIPInputStream(this.pushbackStream);
        return this.gzippedStream;
      } 
      return this.pushbackStream;
    }
    
    public long getContentLength() {
      return (this.wrappedEntity == null) ? 0L : this.wrappedEntity.getContentLength();
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/loopj/android/http/AsyncHttpClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */