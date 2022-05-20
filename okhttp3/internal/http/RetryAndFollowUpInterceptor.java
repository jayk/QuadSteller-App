package okhttp3.internal.http;

import java.io.Closeable;
import java.io.IOException;
import java.net.HttpRetryException;
import java.net.ProtocolException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.Address;
import okhttp3.CertificatePinner;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RouteException;
import okhttp3.internal.connection.StreamAllocation;

public final class RetryAndFollowUpInterceptor implements Interceptor {
  private static final int MAX_FOLLOW_UPS = 20;
  
  private Object callStackTrace;
  
  private volatile boolean canceled;
  
  private final OkHttpClient client;
  
  private final boolean forWebSocket;
  
  private StreamAllocation streamAllocation;
  
  public RetryAndFollowUpInterceptor(OkHttpClient paramOkHttpClient, boolean paramBoolean) {
    this.client = paramOkHttpClient;
    this.forWebSocket = paramBoolean;
  }
  
  private Address createAddress(HttpUrl paramHttpUrl) {
    SSLSocketFactory sSLSocketFactory = null;
    HostnameVerifier hostnameVerifier = null;
    CertificatePinner certificatePinner = null;
    if (paramHttpUrl.isHttps()) {
      sSLSocketFactory = this.client.sslSocketFactory();
      hostnameVerifier = this.client.hostnameVerifier();
      certificatePinner = this.client.certificatePinner();
    } 
    return new Address(paramHttpUrl.host(), paramHttpUrl.port(), this.client.dns(), this.client.socketFactory(), sSLSocketFactory, hostnameVerifier, certificatePinner, this.client.proxyAuthenticator(), this.client.proxy(), this.client.protocols(), this.client.connectionSpecs(), this.client.proxySelector());
  }
  
  private Request followUpRequest(Response paramResponse) throws IOException {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aload_1
    //   3: ifnonnull -> 14
    //   6: new java/lang/IllegalStateException
    //   9: dup
    //   10: invokespecial <init> : ()V
    //   13: athrow
    //   14: aload_0
    //   15: getfield streamAllocation : Lokhttp3/internal/connection/StreamAllocation;
    //   18: invokevirtual connection : ()Lokhttp3/internal/connection/RealConnection;
    //   21: astore_3
    //   22: aload_3
    //   23: ifnull -> 136
    //   26: aload_3
    //   27: invokeinterface route : ()Lokhttp3/Route;
    //   32: astore_3
    //   33: aload_1
    //   34: invokevirtual code : ()I
    //   37: istore #4
    //   39: aload_1
    //   40: invokevirtual request : ()Lokhttp3/Request;
    //   43: invokevirtual method : ()Ljava/lang/String;
    //   46: astore #5
    //   48: iload #4
    //   50: lookupswitch default -> 132, 300 -> 239, 301 -> 239, 302 -> 239, 303 -> 239, 307 -> 217, 308 -> 217, 401 -> 199, 407 -> 141, 408 -> 444
    //   132: aload_2
    //   133: astore_3
    //   134: aload_3
    //   135: areturn
    //   136: aconst_null
    //   137: astore_3
    //   138: goto -> 33
    //   141: aload_3
    //   142: ifnull -> 170
    //   145: aload_3
    //   146: invokevirtual proxy : ()Ljava/net/Proxy;
    //   149: astore_2
    //   150: aload_2
    //   151: invokevirtual type : ()Ljava/net/Proxy$Type;
    //   154: getstatic java/net/Proxy$Type.HTTP : Ljava/net/Proxy$Type;
    //   157: if_acmpeq -> 181
    //   160: new java/net/ProtocolException
    //   163: dup
    //   164: ldc 'Received HTTP_PROXY_AUTH (407) code while not using proxy'
    //   166: invokespecial <init> : (Ljava/lang/String;)V
    //   169: athrow
    //   170: aload_0
    //   171: getfield client : Lokhttp3/OkHttpClient;
    //   174: invokevirtual proxy : ()Ljava/net/Proxy;
    //   177: astore_2
    //   178: goto -> 150
    //   181: aload_0
    //   182: getfield client : Lokhttp3/OkHttpClient;
    //   185: invokevirtual proxyAuthenticator : ()Lokhttp3/Authenticator;
    //   188: aload_3
    //   189: aload_1
    //   190: invokeinterface authenticate : (Lokhttp3/Route;Lokhttp3/Response;)Lokhttp3/Request;
    //   195: astore_3
    //   196: goto -> 134
    //   199: aload_0
    //   200: getfield client : Lokhttp3/OkHttpClient;
    //   203: invokevirtual authenticator : ()Lokhttp3/Authenticator;
    //   206: aload_3
    //   207: aload_1
    //   208: invokeinterface authenticate : (Lokhttp3/Route;Lokhttp3/Response;)Lokhttp3/Request;
    //   213: astore_3
    //   214: goto -> 134
    //   217: aload #5
    //   219: ldc 'GET'
    //   221: invokevirtual equals : (Ljava/lang/Object;)Z
    //   224: ifne -> 239
    //   227: aload_2
    //   228: astore_3
    //   229: aload #5
    //   231: ldc 'HEAD'
    //   233: invokevirtual equals : (Ljava/lang/Object;)Z
    //   236: ifeq -> 134
    //   239: aload_2
    //   240: astore_3
    //   241: aload_0
    //   242: getfield client : Lokhttp3/OkHttpClient;
    //   245: invokevirtual followRedirects : ()Z
    //   248: ifeq -> 134
    //   251: aload_1
    //   252: ldc 'Location'
    //   254: invokevirtual header : (Ljava/lang/String;)Ljava/lang/String;
    //   257: astore #6
    //   259: aload_2
    //   260: astore_3
    //   261: aload #6
    //   263: ifnull -> 134
    //   266: aload_1
    //   267: invokevirtual request : ()Lokhttp3/Request;
    //   270: invokevirtual url : ()Lokhttp3/HttpUrl;
    //   273: aload #6
    //   275: invokevirtual resolve : (Ljava/lang/String;)Lokhttp3/HttpUrl;
    //   278: astore #6
    //   280: aload_2
    //   281: astore_3
    //   282: aload #6
    //   284: ifnull -> 134
    //   287: aload #6
    //   289: invokevirtual scheme : ()Ljava/lang/String;
    //   292: aload_1
    //   293: invokevirtual request : ()Lokhttp3/Request;
    //   296: invokevirtual url : ()Lokhttp3/HttpUrl;
    //   299: invokevirtual scheme : ()Ljava/lang/String;
    //   302: invokevirtual equals : (Ljava/lang/Object;)Z
    //   305: ifne -> 320
    //   308: aload_2
    //   309: astore_3
    //   310: aload_0
    //   311: getfield client : Lokhttp3/OkHttpClient;
    //   314: invokevirtual followSslRedirects : ()Z
    //   317: ifeq -> 134
    //   320: aload_1
    //   321: invokevirtual request : ()Lokhttp3/Request;
    //   324: invokevirtual newBuilder : ()Lokhttp3/Request$Builder;
    //   327: astore_2
    //   328: aload #5
    //   330: invokestatic permitsRequestBody : (Ljava/lang/String;)Z
    //   333: ifeq -> 385
    //   336: aload #5
    //   338: invokestatic redirectsWithBody : (Ljava/lang/String;)Z
    //   341: istore #7
    //   343: aload #5
    //   345: invokestatic redirectsToGet : (Ljava/lang/String;)Z
    //   348: ifeq -> 415
    //   351: aload_2
    //   352: ldc 'GET'
    //   354: aconst_null
    //   355: invokevirtual method : (Ljava/lang/String;Lokhttp3/RequestBody;)Lokhttp3/Request$Builder;
    //   358: pop
    //   359: iload #7
    //   361: ifne -> 385
    //   364: aload_2
    //   365: ldc 'Transfer-Encoding'
    //   367: invokevirtual removeHeader : (Ljava/lang/String;)Lokhttp3/Request$Builder;
    //   370: pop
    //   371: aload_2
    //   372: ldc 'Content-Length'
    //   374: invokevirtual removeHeader : (Ljava/lang/String;)Lokhttp3/Request$Builder;
    //   377: pop
    //   378: aload_2
    //   379: ldc 'Content-Type'
    //   381: invokevirtual removeHeader : (Ljava/lang/String;)Lokhttp3/Request$Builder;
    //   384: pop
    //   385: aload_0
    //   386: aload_1
    //   387: aload #6
    //   389: invokespecial sameConnection : (Lokhttp3/Response;Lokhttp3/HttpUrl;)Z
    //   392: ifne -> 402
    //   395: aload_2
    //   396: ldc 'Authorization'
    //   398: invokevirtual removeHeader : (Ljava/lang/String;)Lokhttp3/Request$Builder;
    //   401: pop
    //   402: aload_2
    //   403: aload #6
    //   405: invokevirtual url : (Lokhttp3/HttpUrl;)Lokhttp3/Request$Builder;
    //   408: invokevirtual build : ()Lokhttp3/Request;
    //   411: astore_3
    //   412: goto -> 134
    //   415: iload #7
    //   417: ifeq -> 439
    //   420: aload_1
    //   421: invokevirtual request : ()Lokhttp3/Request;
    //   424: invokevirtual body : ()Lokhttp3/RequestBody;
    //   427: astore_3
    //   428: aload_2
    //   429: aload #5
    //   431: aload_3
    //   432: invokevirtual method : (Ljava/lang/String;Lokhttp3/RequestBody;)Lokhttp3/Request$Builder;
    //   435: pop
    //   436: goto -> 359
    //   439: aconst_null
    //   440: astore_3
    //   441: goto -> 428
    //   444: aload_2
    //   445: astore_3
    //   446: aload_1
    //   447: invokevirtual request : ()Lokhttp3/Request;
    //   450: invokevirtual body : ()Lokhttp3/RequestBody;
    //   453: instanceof okhttp3/internal/http/UnrepeatableRequestBody
    //   456: ifne -> 134
    //   459: aload_1
    //   460: invokevirtual request : ()Lokhttp3/Request;
    //   463: astore_3
    //   464: goto -> 134
  }
  
  private boolean isRecoverable(IOException paramIOException, boolean paramBoolean) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_3
    //   2: iconst_0
    //   3: istore #4
    //   5: aload_1
    //   6: instanceof java/net/ProtocolException
    //   9: ifeq -> 17
    //   12: iload #4
    //   14: istore_2
    //   15: iload_2
    //   16: ireturn
    //   17: aload_1
    //   18: instanceof java/io/InterruptedIOException
    //   21: ifeq -> 45
    //   24: aload_1
    //   25: instanceof java/net/SocketTimeoutException
    //   28: ifeq -> 40
    //   31: iload_2
    //   32: ifne -> 40
    //   35: iload_3
    //   36: istore_2
    //   37: goto -> 15
    //   40: iconst_0
    //   41: istore_2
    //   42: goto -> 37
    //   45: aload_1
    //   46: instanceof javax/net/ssl/SSLHandshakeException
    //   49: ifeq -> 65
    //   52: iload #4
    //   54: istore_2
    //   55: aload_1
    //   56: invokevirtual getCause : ()Ljava/lang/Throwable;
    //   59: instanceof java/security/cert/CertificateException
    //   62: ifne -> 15
    //   65: iload #4
    //   67: istore_2
    //   68: aload_1
    //   69: instanceof javax/net/ssl/SSLPeerUnverifiedException
    //   72: ifne -> 15
    //   75: iconst_1
    //   76: istore_2
    //   77: goto -> 15
  }
  
  private boolean recover(IOException paramIOException, boolean paramBoolean, Request paramRequest) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #4
    //   3: aload_0
    //   4: getfield streamAllocation : Lokhttp3/internal/connection/StreamAllocation;
    //   7: aload_1
    //   8: invokevirtual streamFailed : (Ljava/io/IOException;)V
    //   11: aload_0
    //   12: getfield client : Lokhttp3/OkHttpClient;
    //   15: invokevirtual retryOnConnectionFailure : ()Z
    //   18: ifne -> 28
    //   21: iload #4
    //   23: istore #5
    //   25: iload #5
    //   27: ireturn
    //   28: iload_2
    //   29: ifeq -> 46
    //   32: iload #4
    //   34: istore #5
    //   36: aload_3
    //   37: invokevirtual body : ()Lokhttp3/RequestBody;
    //   40: instanceof okhttp3/internal/http/UnrepeatableRequestBody
    //   43: ifne -> 25
    //   46: iload #4
    //   48: istore #5
    //   50: aload_0
    //   51: aload_1
    //   52: iload_2
    //   53: invokespecial isRecoverable : (Ljava/io/IOException;Z)Z
    //   56: ifeq -> 25
    //   59: iload #4
    //   61: istore #5
    //   63: aload_0
    //   64: getfield streamAllocation : Lokhttp3/internal/connection/StreamAllocation;
    //   67: invokevirtual hasMoreRoutes : ()Z
    //   70: ifeq -> 25
    //   73: iconst_1
    //   74: istore #5
    //   76: goto -> 25
  }
  
  private boolean sameConnection(Response paramResponse, HttpUrl paramHttpUrl) {
    HttpUrl httpUrl = paramResponse.request().url();
    return (httpUrl.host().equals(paramHttpUrl.host()) && httpUrl.port() == paramHttpUrl.port() && httpUrl.scheme().equals(paramHttpUrl.scheme()));
  }
  
  public void cancel() {
    this.canceled = true;
    StreamAllocation streamAllocation = this.streamAllocation;
    if (streamAllocation != null)
      streamAllocation.cancel(); 
  }
  
  public Response intercept(Interceptor.Chain paramChain) throws IOException {
    Request request = paramChain.request();
    this.streamAllocation = new StreamAllocation(this.client.connectionPool(), createAddress(request.url()), this.callStackTrace);
    byte b = 0;
    Response response = null;
    while (true) {
      Response response1;
      if (this.canceled) {
        this.streamAllocation.release();
        throw new IOException("Canceled");
      } 
      try {
        Response response2 = ((RealInterceptorChain)paramChain).proceed(request, this.streamAllocation, null, null);
        if (false) {
          this.streamAllocation.streamFailed(null);
          this.streamAllocation.release();
        } 
        response1 = response2;
        if (response != null)
          response1 = response2.newBuilder().priorResponse(response.newBuilder().body(null).build()).build(); 
        Request request1 = followUpRequest(response1);
      } catch (RouteException routeException) {
        if (!recover(routeException.getLastConnectException(), false, (Request)response1))
          throw routeException.getLastConnectException(); 
        continue;
      } catch (IOException iOException2) {
        boolean bool;
        if (!(iOException2 instanceof okhttp3.internal.http2.ConnectionShutdownException)) {
          bool = true;
        } else {
          bool = false;
        } 
        if (!recover(iOException2, bool, (Request)response1))
          throw iOException2; 
        continue;
      } finally {
        if (true) {
          this.streamAllocation.streamFailed(null);
          this.streamAllocation.release();
        } 
      } 
      Util.closeQuietly((Closeable)response1.body());
      if (++b > 20) {
        this.streamAllocation.release();
        throw new ProtocolException("Too many follow-up requests: " + b);
      } 
      if (iOException2.body() instanceof UnrepeatableRequestBody) {
        this.streamAllocation.release();
        throw new HttpRetryException("Cannot retry streamed HTTP body", response1.code());
      } 
      if (!sameConnection(response1, iOException2.url())) {
        this.streamAllocation.release();
        this.streamAllocation = new StreamAllocation(this.client.connectionPool(), createAddress(iOException2.url()), this.callStackTrace);
      } else if (this.streamAllocation.codec() != null) {
        throw new IllegalStateException("Closing the body of " + response1 + " didn't close its backing stream. Bad interceptor?");
      } 
      response = response1;
      IOException iOException1 = iOException2;
    } 
  }
  
  public boolean isCanceled() {
    return this.canceled;
  }
  
  public void setCallStackTrace(Object paramObject) {
    this.callStackTrace = paramObject;
  }
  
  public StreamAllocation streamAllocation() {
    return this.streamAllocation;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/http/RetryAndFollowUpInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */