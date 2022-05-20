package okhttp3.internal.platform;

import android.util.Log;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.Protocol;
import okhttp3.internal.Util;
import okhttp3.internal.tls.CertificateChainCleaner;

class AndroidPlatform extends Platform {
  private static final int MAX_LOG_LENGTH = 4000;
  
  private final CloseGuard closeGuard = CloseGuard.get();
  
  private final OptionalMethod<Socket> getAlpnSelectedProtocol;
  
  private final OptionalMethod<Socket> setAlpnProtocols;
  
  private final OptionalMethod<Socket> setHostname;
  
  private final OptionalMethod<Socket> setUseSessionTickets;
  
  private final Class<?> sslParametersClass;
  
  AndroidPlatform(Class<?> paramClass, OptionalMethod<Socket> paramOptionalMethod1, OptionalMethod<Socket> paramOptionalMethod2, OptionalMethod<Socket> paramOptionalMethod3, OptionalMethod<Socket> paramOptionalMethod4) {
    this.sslParametersClass = paramClass;
    this.setUseSessionTickets = paramOptionalMethod1;
    this.setHostname = paramOptionalMethod2;
    this.getAlpnSelectedProtocol = paramOptionalMethod3;
    this.setAlpnProtocols = paramOptionalMethod4;
  }
  
  public static Platform buildIfSupported() {
    Class<?> clazz;
    try {
      clazz = Class.forName("com.android.org.conscrypt.SSLParametersImpl");
    } catch (ClassNotFoundException classNotFoundException) {}
    try {
      OptionalMethod<Socket> optionalMethod1 = new OptionalMethod();
      this(null, "setUseSessionTickets", new Class[] { boolean.class });
      OptionalMethod<Socket> optionalMethod2 = new OptionalMethod();
      this(null, "setHostname", new Class[] { String.class });
      OptionalMethod<Socket> optionalMethod3 = null;
      OptionalMethod<Socket> optionalMethod4 = null;
      try {
        Class.forName("android.net.Network");
        OptionalMethod optionalMethod = new OptionalMethod();
        this(byte[].class, "getAlpnSelectedProtocol", new Class[0]);
        try {
          optionalMethod3 = new OptionalMethod();
          this(null, "setAlpnProtocols", new Class[] { byte[].class });
          optionalMethod4 = optionalMethod3;
        } catch (ClassNotFoundException classNotFoundException1) {}
      } catch (ClassNotFoundException classNotFoundException2) {
        classNotFoundException2 = classNotFoundException1;
      } 
      AndroidPlatform androidPlatform1 = new AndroidPlatform();
      this(clazz, optionalMethod1, optionalMethod2, (OptionalMethod<Socket>)classNotFoundException2, optionalMethod4);
      AndroidPlatform androidPlatform2 = androidPlatform1;
    } catch (ClassNotFoundException classNotFoundException) {
      classNotFoundException = null;
    } 
  }
  
  public CertificateChainCleaner buildCertificateChainCleaner(X509TrustManager paramX509TrustManager) {
    CertificateChainCleaner certificateChainCleaner;
    try {
      Class<?> clazz = Class.forName("android.net.http.X509TrustManagerExtensions");
      Object object = clazz.getConstructor(new Class[] { X509TrustManager.class }).newInstance(new Object[] { paramX509TrustManager });
      Method method = clazz.getMethod("checkServerTrusted", new Class[] { X509Certificate[].class, String.class, String.class });
      AndroidCertificateChainCleaner androidCertificateChainCleaner = new AndroidCertificateChainCleaner();
      this(object, method);
      certificateChainCleaner = androidCertificateChainCleaner;
    } catch (Exception exception) {
      certificateChainCleaner = super.buildCertificateChainCleaner((X509TrustManager)certificateChainCleaner);
    } 
    return certificateChainCleaner;
  }
  
  public void configureTlsExtensions(SSLSocket paramSSLSocket, String paramString, List<Protocol> paramList) {
    if (paramString != null) {
      this.setUseSessionTickets.invokeOptionalWithoutCheckedException(paramSSLSocket, new Object[] { Boolean.valueOf(true) });
      this.setHostname.invokeOptionalWithoutCheckedException(paramSSLSocket, new Object[] { paramString });
    } 
    if (this.setAlpnProtocols != null && this.setAlpnProtocols.isSupported(paramSSLSocket)) {
      byte[] arrayOfByte = concatLengthPrefixed(paramList);
      this.setAlpnProtocols.invokeWithoutCheckedException(paramSSLSocket, new Object[] { arrayOfByte });
    } 
  }
  
  public void connectSocket(Socket paramSocket, InetSocketAddress paramInetSocketAddress, int paramInt) throws IOException {
    try {
      paramSocket.connect(paramInetSocketAddress, paramInt);
      return;
    } catch (AssertionError assertionError) {
      if (Util.isAndroidGetsocknameError(assertionError))
        throw new IOException(assertionError); 
      throw assertionError;
    } catch (SecurityException securityException) {
      IOException iOException = new IOException("Exception in connect");
      iOException.initCause(securityException);
      throw iOException;
    } 
  }
  
  public String getSelectedProtocol(SSLSocket paramSSLSocket) {
    String str1 = null;
    if (this.getAlpnSelectedProtocol == null)
      return str1; 
    String str2 = str1;
    if (this.getAlpnSelectedProtocol.isSupported(paramSSLSocket)) {
      byte[] arrayOfByte = (byte[])this.getAlpnSelectedProtocol.invokeWithoutCheckedException(paramSSLSocket, new Object[0]);
      str2 = str1;
      if (arrayOfByte != null)
        str2 = new String(arrayOfByte, Util.UTF_8); 
    } 
    return str2;
  }
  
  public Object getStackTraceForCloseable(String paramString) {
    return this.closeGuard.createAndOpen(paramString);
  }
  
  public boolean isCleartextTrafficPermitted(String paramString) {
    try {
      Class<?> clazz = Class.forName("android.security.NetworkSecurityPolicy");
      Object object = clazz.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
      return ((Boolean)clazz.getMethod("isCleartextTrafficPermitted", new Class[] { String.class }).invoke(object, new Object[] { paramString })).booleanValue();
    } catch (ClassNotFoundException classNotFoundException) {
    
    } catch (NoSuchMethodException noSuchMethodException) {
    
    } catch (IllegalAccessException illegalAccessException) {
      throw new AssertionError();
    } catch (IllegalArgumentException illegalArgumentException) {
      throw new AssertionError();
    } catch (InvocationTargetException invocationTargetException) {
      throw new AssertionError();
    } 
    return super.isCleartextTrafficPermitted((String)invocationTargetException);
  }
  
  public void log(int paramInt, String paramString, Throwable paramThrowable) {
    byte b = 5;
    if (paramInt != 5)
      b = 3; 
    String str = paramString;
    if (paramThrowable != null)
      str = paramString + '\n' + Log.getStackTraceString(paramThrowable); 
    paramInt = 0;
    int i = str.length();
    while (paramInt < i) {
      int j = str.indexOf('\n', paramInt);
      if (j == -1)
        j = i; 
      while (true) {
        int k = Math.min(j, paramInt + 4000);
        Log.println(b, "OkHttp", str.substring(paramInt, k));
        paramInt = k;
        if (k >= j)
          paramInt = k + 1; 
      } 
    } 
  }
  
  public void logCloseableLeak(String paramString, Object paramObject) {
    if (!this.closeGuard.warnIfOpen(paramObject))
      log(5, paramString, (Throwable)null); 
  }
  
  public X509TrustManager trustManager(SSLSocketFactory paramSSLSocketFactory) {
    // Byte code:
    //   0: aload_1
    //   1: aload_0
    //   2: getfield sslParametersClass : Ljava/lang/Class;
    //   5: ldc_w 'sslParameters'
    //   8: invokestatic readFieldOrNull : (Ljava/lang/Object;Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Object;
    //   11: astore_2
    //   12: aload_2
    //   13: astore_3
    //   14: aload_2
    //   15: ifnonnull -> 40
    //   18: aload_1
    //   19: ldc_w 'com.google.android.gms.org.conscrypt.SSLParametersImpl'
    //   22: iconst_0
    //   23: aload_1
    //   24: invokevirtual getClass : ()Ljava/lang/Class;
    //   27: invokevirtual getClassLoader : ()Ljava/lang/ClassLoader;
    //   30: invokestatic forName : (Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;
    //   33: ldc_w 'sslParameters'
    //   36: invokestatic readFieldOrNull : (Ljava/lang/Object;Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Object;
    //   39: astore_3
    //   40: aload_3
    //   41: ldc javax/net/ssl/X509TrustManager
    //   43: ldc_w 'x509TrustManager'
    //   46: invokestatic readFieldOrNull : (Ljava/lang/Object;Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Object;
    //   49: checkcast javax/net/ssl/X509TrustManager
    //   52: astore_1
    //   53: aload_1
    //   54: ifnull -> 69
    //   57: aload_1
    //   58: areturn
    //   59: astore_3
    //   60: aload_0
    //   61: aload_1
    //   62: invokespecial trustManager : (Ljavax/net/ssl/SSLSocketFactory;)Ljavax/net/ssl/X509TrustManager;
    //   65: astore_1
    //   66: goto -> 57
    //   69: aload_3
    //   70: ldc javax/net/ssl/X509TrustManager
    //   72: ldc_w 'trustManager'
    //   75: invokestatic readFieldOrNull : (Ljava/lang/Object;Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Object;
    //   78: checkcast javax/net/ssl/X509TrustManager
    //   81: astore_1
    //   82: goto -> 57
    // Exception table:
    //   from	to	target	type
    //   18	40	59	java/lang/ClassNotFoundException
  }
  
  static final class AndroidCertificateChainCleaner extends CertificateChainCleaner {
    private final Method checkServerTrusted;
    
    private final Object x509TrustManagerExtensions;
    
    AndroidCertificateChainCleaner(Object param1Object, Method param1Method) {
      this.x509TrustManagerExtensions = param1Object;
      this.checkServerTrusted = param1Method;
    }
    
    public List<Certificate> clean(List<Certificate> param1List, String param1String) throws SSLPeerUnverifiedException {
      try {
        X509Certificate[] arrayOfX509Certificate = param1List.<X509Certificate>toArray(new X509Certificate[param1List.size()]);
        return (List)this.checkServerTrusted.invoke(this.x509TrustManagerExtensions, new Object[] { arrayOfX509Certificate, "RSA", param1String });
      } catch (InvocationTargetException invocationTargetException) {
        SSLPeerUnverifiedException sSLPeerUnverifiedException = new SSLPeerUnverifiedException(invocationTargetException.getMessage());
        sSLPeerUnverifiedException.initCause(invocationTargetException);
        throw sSLPeerUnverifiedException;
      } catch (IllegalAccessException illegalAccessException) {
        throw new AssertionError(illegalAccessException);
      } 
    }
    
    public boolean equals(Object param1Object) {
      return param1Object instanceof AndroidCertificateChainCleaner;
    }
    
    public int hashCode() {
      return 0;
    }
  }
  
  static final class CloseGuard {
    private final Method getMethod;
    
    private final Method openMethod;
    
    private final Method warnIfOpenMethod;
    
    CloseGuard(Method param1Method1, Method param1Method2, Method param1Method3) {
      this.getMethod = param1Method1;
      this.openMethod = param1Method2;
      this.warnIfOpenMethod = param1Method3;
    }
    
    static CloseGuard get() {
      Method method1;
      Method method2;
      try {
        Class<?> clazz = Class.forName("dalvik.system.CloseGuard");
        method2 = clazz.getMethod("get", new Class[0]);
        Method method = clazz.getMethod("open", new Class[] { String.class });
        method1 = clazz.getMethod("warnIfOpen", new Class[0]);
      } catch (Exception exception) {
        method2 = null;
        exception = null;
        method1 = null;
      } 
      return new CloseGuard(method2, (Method)exception, method1);
    }
    
    Object createAndOpen(String param1String) {
      if (this.getMethod != null)
        try {
          Object object = this.getMethod.invoke(null, new Object[0]);
          this.openMethod.invoke(object, new Object[] { param1String });
          return object;
        } catch (Exception exception) {} 
      return null;
    }
    
    boolean warnIfOpen(Object param1Object) {
      boolean bool1 = false;
      boolean bool2 = bool1;
      if (param1Object != null)
        try {
          this.warnIfOpenMethod.invoke(param1Object, new Object[0]);
          bool2 = true;
        } catch (Exception exception) {
          bool2 = bool1;
        }  
      return bool2;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/platform/AndroidPlatform.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */