package okhttp3.internal.platform;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.Protocol;

final class Jdk9Platform extends Platform {
  final Method getProtocolMethod;
  
  final Method setProtocolMethod;
  
  Jdk9Platform(Method paramMethod1, Method paramMethod2) {
    this.setProtocolMethod = paramMethod1;
    this.getProtocolMethod = paramMethod2;
  }
  
  public static Jdk9Platform buildIfSupported() {
    try {
      Method method1 = SSLParameters.class.getMethod("setApplicationProtocols", new Class[] { String[].class });
      Method method2 = SSLSocket.class.getMethod("getApplicationProtocol", new Class[0]);
      Jdk9Platform jdk9Platform = new Jdk9Platform();
      this(method1, method2);
    } catch (NoSuchMethodException noSuchMethodException) {
      noSuchMethodException = null;
    } 
    return (Jdk9Platform)noSuchMethodException;
  }
  
  public void configureTlsExtensions(SSLSocket paramSSLSocket, String paramString, List<Protocol> paramList) {
    try {
      SSLParameters sSLParameters = paramSSLSocket.getSSLParameters();
      paramList = (List)alpnProtocolNames(paramList);
      this.setProtocolMethod.invoke(sSLParameters, new Object[] { paramList.toArray(new String[paramList.size()]) });
      paramSSLSocket.setSSLParameters(sSLParameters);
      return;
    } catch (IllegalAccessException illegalAccessException) {
    
    } catch (InvocationTargetException invocationTargetException) {}
    throw new AssertionError();
  }
  
  public String getSelectedProtocol(SSLSocket paramSSLSocket) {
    try {
      null = (String)this.getProtocolMethod.invoke(paramSSLSocket, new Object[0]);
      if (null != null) {
        boolean bool = null.equals("");
        return bool ? null : null;
      } 
      return null;
    } catch (IllegalAccessException illegalAccessException) {
    
    } catch (InvocationTargetException invocationTargetException) {}
    throw new AssertionError();
  }
  
  public X509TrustManager trustManager(SSLSocketFactory paramSSLSocketFactory) {
    throw new UnsupportedOperationException("clientBuilder.sslSocketFactory(SSLSocketFactory) not supported on JDK 9+");
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/platform/Jdk9Platform.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */