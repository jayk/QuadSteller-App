package com.loopj.android.http;

import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.protocol.HttpContext;

class MyRedirectHandler extends DefaultRedirectHandler {
  private static final String REDIRECT_LOCATIONS = "http.protocol.redirect-locations";
  
  private final boolean enableRedirects;
  
  public MyRedirectHandler(boolean paramBoolean) {
    this.enableRedirects = paramBoolean;
  }
  
  public URI getLocationURI(HttpResponse paramHttpResponse, HttpContext paramHttpContext) throws ProtocolException {
    // Byte code:
    //   0: aload_1
    //   1: ifnonnull -> 14
    //   4: new java/lang/IllegalArgumentException
    //   7: dup
    //   8: ldc 'HTTP response may not be null'
    //   10: invokespecial <init> : (Ljava/lang/String;)V
    //   13: athrow
    //   14: aload_1
    //   15: ldc 'location'
    //   17: invokeinterface getFirstHeader : (Ljava/lang/String;)Lorg/apache/http/Header;
    //   22: astore_3
    //   23: aload_3
    //   24: ifnonnull -> 64
    //   27: new org/apache/http/ProtocolException
    //   30: dup
    //   31: new java/lang/StringBuilder
    //   34: dup
    //   35: invokespecial <init> : ()V
    //   38: ldc 'Received redirect response '
    //   40: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   43: aload_1
    //   44: invokeinterface getStatusLine : ()Lorg/apache/http/StatusLine;
    //   49: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   52: ldc ' but no location header'
    //   54: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   57: invokevirtual toString : ()Ljava/lang/String;
    //   60: invokespecial <init> : (Ljava/lang/String;)V
    //   63: athrow
    //   64: aload_3
    //   65: invokeinterface getValue : ()Ljava/lang/String;
    //   70: ldc ' '
    //   72: ldc '%20'
    //   74: invokevirtual replaceAll : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   77: astore #4
    //   79: new java/net/URI
    //   82: dup
    //   83: aload #4
    //   85: invokespecial <init> : (Ljava/lang/String;)V
    //   88: astore_3
    //   89: aload_1
    //   90: invokeinterface getParams : ()Lorg/apache/http/params/HttpParams;
    //   95: astore #4
    //   97: aload_3
    //   98: astore_1
    //   99: aload_3
    //   100: invokevirtual isAbsolute : ()Z
    //   103: ifne -> 253
    //   106: aload #4
    //   108: ldc 'http.protocol.reject-relative-redirect'
    //   110: invokeinterface isParameterTrue : (Ljava/lang/String;)Z
    //   115: ifeq -> 180
    //   118: new org/apache/http/ProtocolException
    //   121: dup
    //   122: new java/lang/StringBuilder
    //   125: dup
    //   126: invokespecial <init> : ()V
    //   129: ldc 'Relative redirect location ''
    //   131: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   134: aload_3
    //   135: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   138: ldc '' not allowed'
    //   140: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   143: invokevirtual toString : ()Ljava/lang/String;
    //   146: invokespecial <init> : (Ljava/lang/String;)V
    //   149: athrow
    //   150: astore_1
    //   151: new org/apache/http/ProtocolException
    //   154: dup
    //   155: new java/lang/StringBuilder
    //   158: dup
    //   159: invokespecial <init> : ()V
    //   162: ldc 'Invalid redirect URI: '
    //   164: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   167: aload #4
    //   169: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   172: invokevirtual toString : ()Ljava/lang/String;
    //   175: aload_1
    //   176: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   179: athrow
    //   180: aload_2
    //   181: ldc 'http.target_host'
    //   183: invokeinterface getAttribute : (Ljava/lang/String;)Ljava/lang/Object;
    //   188: checkcast org/apache/http/HttpHost
    //   191: astore #5
    //   193: aload #5
    //   195: ifnonnull -> 208
    //   198: new java/lang/IllegalStateException
    //   201: dup
    //   202: ldc 'Target host not available in the HTTP context'
    //   204: invokespecial <init> : (Ljava/lang/String;)V
    //   207: athrow
    //   208: aload_2
    //   209: ldc 'http.request'
    //   211: invokeinterface getAttribute : (Ljava/lang/String;)Ljava/lang/Object;
    //   216: checkcast org/apache/http/HttpRequest
    //   219: astore #6
    //   221: new java/net/URI
    //   224: astore_1
    //   225: aload_1
    //   226: aload #6
    //   228: invokeinterface getRequestLine : ()Lorg/apache/http/RequestLine;
    //   233: invokeinterface getUri : ()Ljava/lang/String;
    //   238: invokespecial <init> : (Ljava/lang/String;)V
    //   241: aload_1
    //   242: aload #5
    //   244: iconst_1
    //   245: invokestatic rewriteURI : (Ljava/net/URI;Lorg/apache/http/HttpHost;Z)Ljava/net/URI;
    //   248: aload_3
    //   249: invokestatic resolve : (Ljava/net/URI;Ljava/net/URI;)Ljava/net/URI;
    //   252: astore_1
    //   253: aload #4
    //   255: ldc 'http.protocol.allow-circular-redirects'
    //   257: invokeinterface isParameterFalse : (Ljava/lang/String;)Z
    //   262: ifeq -> 415
    //   265: aload_2
    //   266: ldc 'http.protocol.redirect-locations'
    //   268: invokeinterface getAttribute : (Ljava/lang/String;)Ljava/lang/Object;
    //   273: checkcast org/apache/http/impl/client/RedirectLocations
    //   276: astore #4
    //   278: aload #4
    //   280: astore_3
    //   281: aload #4
    //   283: ifnonnull -> 303
    //   286: new org/apache/http/impl/client/RedirectLocations
    //   289: dup
    //   290: invokespecial <init> : ()V
    //   293: astore_3
    //   294: aload_2
    //   295: ldc 'http.protocol.redirect-locations'
    //   297: aload_3
    //   298: invokeinterface setAttribute : (Ljava/lang/String;Ljava/lang/Object;)V
    //   303: aload_1
    //   304: invokevirtual getFragment : ()Ljava/lang/String;
    //   307: ifnull -> 405
    //   310: new org/apache/http/HttpHost
    //   313: astore_2
    //   314: aload_2
    //   315: aload_1
    //   316: invokevirtual getHost : ()Ljava/lang/String;
    //   319: aload_1
    //   320: invokevirtual getPort : ()I
    //   323: aload_1
    //   324: invokevirtual getScheme : ()Ljava/lang/String;
    //   327: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
    //   330: aload_1
    //   331: aload_2
    //   332: iconst_1
    //   333: invokestatic rewriteURI : (Ljava/net/URI;Lorg/apache/http/HttpHost;Z)Ljava/net/URI;
    //   336: astore_2
    //   337: aload_3
    //   338: aload_2
    //   339: invokevirtual contains : (Ljava/net/URI;)Z
    //   342: ifeq -> 410
    //   345: new org/apache/http/client/CircularRedirectException
    //   348: dup
    //   349: new java/lang/StringBuilder
    //   352: dup
    //   353: invokespecial <init> : ()V
    //   356: ldc 'Circular redirect to ''
    //   358: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   361: aload_2
    //   362: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   365: ldc '''
    //   367: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   370: invokevirtual toString : ()Ljava/lang/String;
    //   373: invokespecial <init> : (Ljava/lang/String;)V
    //   376: athrow
    //   377: astore_1
    //   378: new org/apache/http/ProtocolException
    //   381: dup
    //   382: aload_1
    //   383: invokevirtual getMessage : ()Ljava/lang/String;
    //   386: aload_1
    //   387: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   390: athrow
    //   391: astore_1
    //   392: new org/apache/http/ProtocolException
    //   395: dup
    //   396: aload_1
    //   397: invokevirtual getMessage : ()Ljava/lang/String;
    //   400: aload_1
    //   401: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   404: athrow
    //   405: aload_1
    //   406: astore_2
    //   407: goto -> 337
    //   410: aload_3
    //   411: aload_2
    //   412: invokevirtual add : (Ljava/net/URI;)V
    //   415: aload_1
    //   416: areturn
    // Exception table:
    //   from	to	target	type
    //   79	89	150	java/net/URISyntaxException
    //   221	253	377	java/net/URISyntaxException
    //   310	337	391	java/net/URISyntaxException
  }
  
  public boolean isRedirectRequested(HttpResponse paramHttpResponse, HttpContext paramHttpContext) {
    boolean bool = false;
    if (!this.enableRedirects);
    if (paramHttpResponse == null)
      throw new IllegalArgumentException("HTTP response may not be null"); 
    switch (paramHttpResponse.getStatusLine().getStatusCode()) {
      default:
        return bool;
      case 301:
      case 302:
      case 303:
      case 307:
        break;
    } 
    bool = true;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/loopj/android/http/MyRedirectHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */