package com.loopj.android.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class MySSLSocketFactory extends SSLSocketFactory {
  SSLContext sslContext = SSLContext.getInstance("TLS");
  
  public MySSLSocketFactory(KeyStore paramKeyStore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
    super(paramKeyStore);
    X509TrustManager x509TrustManager = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] param1ArrayOfX509Certificate, String param1String) throws CertificateException {}
        
        public void checkServerTrusted(X509Certificate[] param1ArrayOfX509Certificate, String param1String) throws CertificateException {}
        
        public X509Certificate[] getAcceptedIssuers() {
          return null;
        }
      };
    this.sslContext.init(null, new TrustManager[] { x509TrustManager }, null);
  }
  
  public static SSLSocketFactory getFixedSocketFactory() {
    SSLSocketFactory sSLSocketFactory;
    try {
      sSLSocketFactory = new MySSLSocketFactory();
      this(getKeystore());
      sSLSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      sSLSocketFactory = SSLSocketFactory.getSocketFactory();
    } 
    return sSLSocketFactory;
  }
  
  public static KeyStore getKeystore() {
    KeyStore keyStore = null;
    try {
      KeyStore keyStore1 = KeyStore.getInstance(KeyStore.getDefaultType());
      keyStore = keyStore1;
      keyStore1.load(null, null);
      keyStore = keyStore1;
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    } 
    return keyStore;
  }
  
  public static KeyStore getKeystoreOfCA(InputStream paramInputStream) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aconst_null
    //   3: astore_2
    //   4: aconst_null
    //   5: astore_3
    //   6: aload_1
    //   7: astore #4
    //   9: ldc 'X.509'
    //   11: invokestatic getInstance : (Ljava/lang/String;)Ljava/security/cert/CertificateFactory;
    //   14: astore #5
    //   16: aload_1
    //   17: astore #4
    //   19: new java/io/BufferedInputStream
    //   22: astore #6
    //   24: aload_1
    //   25: astore #4
    //   27: aload #6
    //   29: aload_0
    //   30: invokespecial <init> : (Ljava/io/InputStream;)V
    //   33: aload #5
    //   35: aload #6
    //   37: invokevirtual generateCertificate : (Ljava/io/InputStream;)Ljava/security/cert/Certificate;
    //   40: astore #4
    //   42: aload #6
    //   44: ifnull -> 52
    //   47: aload #6
    //   49: invokevirtual close : ()V
    //   52: invokestatic getDefaultType : ()Ljava/lang/String;
    //   55: astore #6
    //   57: aconst_null
    //   58: astore_0
    //   59: aload #6
    //   61: invokestatic getInstance : (Ljava/lang/String;)Ljava/security/KeyStore;
    //   64: astore #6
    //   66: aload #6
    //   68: astore_0
    //   69: aload #6
    //   71: aconst_null
    //   72: aconst_null
    //   73: invokevirtual load : (Ljava/io/InputStream;[C)V
    //   76: aload #6
    //   78: astore_0
    //   79: aload #6
    //   81: ldc 'ca'
    //   83: aload #4
    //   85: invokevirtual setCertificateEntry : (Ljava/lang/String;Ljava/security/cert/Certificate;)V
    //   88: aload #6
    //   90: astore_0
    //   91: aload_0
    //   92: areturn
    //   93: astore_0
    //   94: aload_0
    //   95: invokevirtual printStackTrace : ()V
    //   98: goto -> 52
    //   101: astore #6
    //   103: aload_2
    //   104: astore_0
    //   105: aload_0
    //   106: astore #4
    //   108: aload #6
    //   110: invokevirtual printStackTrace : ()V
    //   113: aload_3
    //   114: astore #4
    //   116: aload_0
    //   117: ifnull -> 52
    //   120: aload_0
    //   121: invokevirtual close : ()V
    //   124: aload_3
    //   125: astore #4
    //   127: goto -> 52
    //   130: astore_0
    //   131: aload_0
    //   132: invokevirtual printStackTrace : ()V
    //   135: aload_3
    //   136: astore #4
    //   138: goto -> 52
    //   141: astore_0
    //   142: aload #4
    //   144: ifnull -> 152
    //   147: aload #4
    //   149: invokevirtual close : ()V
    //   152: aload_0
    //   153: athrow
    //   154: astore #4
    //   156: aload #4
    //   158: invokevirtual printStackTrace : ()V
    //   161: goto -> 152
    //   164: astore #4
    //   166: aload #4
    //   168: invokevirtual printStackTrace : ()V
    //   171: goto -> 91
    //   174: astore_0
    //   175: aload #6
    //   177: astore #4
    //   179: goto -> 142
    //   182: astore #4
    //   184: aload #6
    //   186: astore_0
    //   187: aload #4
    //   189: astore #6
    //   191: goto -> 105
    // Exception table:
    //   from	to	target	type
    //   9	16	101	java/security/cert/CertificateException
    //   9	16	141	finally
    //   19	24	101	java/security/cert/CertificateException
    //   19	24	141	finally
    //   27	33	101	java/security/cert/CertificateException
    //   27	33	141	finally
    //   33	42	182	java/security/cert/CertificateException
    //   33	42	174	finally
    //   47	52	93	java/io/IOException
    //   59	66	164	java/lang/Exception
    //   69	76	164	java/lang/Exception
    //   79	88	164	java/lang/Exception
    //   108	113	141	finally
    //   120	124	130	java/io/IOException
    //   147	152	154	java/io/IOException
  }
  
  public static DefaultHttpClient getNewHttpClient(KeyStore paramKeyStore) {
    DefaultHttpClient defaultHttpClient;
    try {
      MySSLSocketFactory mySSLSocketFactory = new MySSLSocketFactory();
      this(paramKeyStore);
      SchemeRegistry schemeRegistry = new SchemeRegistry();
      this();
      Scheme scheme = new Scheme();
      this("http", (SocketFactory)PlainSocketFactory.getSocketFactory(), 80);
      schemeRegistry.register(scheme);
      scheme = new Scheme();
      this("https", (SocketFactory)mySSLSocketFactory, 443);
      schemeRegistry.register(scheme);
      BasicHttpParams basicHttpParams = new BasicHttpParams();
      this();
      HttpProtocolParams.setVersion((HttpParams)basicHttpParams, (ProtocolVersion)HttpVersion.HTTP_1_1);
      HttpProtocolParams.setContentCharset((HttpParams)basicHttpParams, "UTF-8");
      ThreadSafeClientConnManager threadSafeClientConnManager = new ThreadSafeClientConnManager();
      this((HttpParams)basicHttpParams, schemeRegistry);
      defaultHttpClient = new DefaultHttpClient();
      this((ClientConnectionManager)threadSafeClientConnManager, (HttpParams)basicHttpParams);
    } catch (Exception exception) {
      defaultHttpClient = new DefaultHttpClient();
    } 
    return defaultHttpClient;
  }
  
  public Socket createSocket() throws IOException {
    return this.sslContext.getSocketFactory().createSocket();
  }
  
  public Socket createSocket(Socket paramSocket, String paramString, int paramInt, boolean paramBoolean) throws IOException {
    return this.sslContext.getSocketFactory().createSocket(paramSocket, paramString, paramInt, paramBoolean);
  }
  
  public void fixHttpsURLConnection() {
    HttpsURLConnection.setDefaultSSLSocketFactory(this.sslContext.getSocketFactory());
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/loopj/android/http/MySSLSocketFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */