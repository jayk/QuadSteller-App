package okhttp3.internal.platform;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.internal.tls.BasicCertificateChainCleaner;
import okhttp3.internal.tls.CertificateChainCleaner;
import okhttp3.internal.tls.TrustRootIndex;
import okio.Buffer;

public class Platform {
  public static final int INFO = 4;
  
  private static final Platform PLATFORM = findPlatform();
  
  public static final int WARN = 5;
  
  private static final Logger logger = Logger.getLogger(OkHttpClient.class.getName());
  
  public static List<String> alpnProtocolNames(List<Protocol> paramList) {
    ArrayList<String> arrayList = new ArrayList(paramList.size());
    byte b = 0;
    int i = paramList.size();
    while (b < i) {
      Protocol protocol = paramList.get(b);
      if (protocol != Protocol.HTTP_1_0)
        arrayList.add(protocol.toString()); 
      b++;
    } 
    return arrayList;
  }
  
  static byte[] concatLengthPrefixed(List<Protocol> paramList) {
    Buffer buffer = new Buffer();
    byte b = 0;
    int i = paramList.size();
    while (b < i) {
      Protocol protocol = paramList.get(b);
      if (protocol != Protocol.HTTP_1_0) {
        buffer.writeByte(protocol.toString().length());
        buffer.writeUtf8(protocol.toString());
      } 
      b++;
    } 
    return buffer.readByteArray();
  }
  
  private static Platform findPlatform() {
    Platform platform = AndroidPlatform.buildIfSupported();
    if (platform == null) {
      platform = Jdk9Platform.buildIfSupported();
      if (platform == null) {
        platform = JdkWithJettyBootPlatform.buildIfSupported();
        if (platform == null)
          platform = new Platform(); 
      } 
    } 
    return platform;
  }
  
  public static Platform get() {
    return PLATFORM;
  }
  
  static <T> T readFieldOrNull(Object paramObject, Class<T> paramClass, String paramString) {
    Field field1 = null;
    Class<?> clazz = paramObject.getClass();
    while (clazz != Object.class) {
      try {
        null = clazz.getDeclaredField(paramString);
        null.setAccessible(true);
        Object object = null.get(paramObject);
        null = field1;
        if (object != null) {
          if (!paramClass.isInstance(object))
            return (T)field1; 
        } else {
          return (T)null;
        } 
        return paramClass.cast(object);
      } catch (NoSuchFieldException noSuchFieldException) {
        clazz = clazz.getSuperclass();
      } catch (IllegalAccessException illegalAccessException) {
        throw new AssertionError();
      } 
    } 
    Field field2 = field1;
    if (!paramString.equals("delegate")) {
      illegalAccessException = readFieldOrNull(illegalAccessException, Object.class, "delegate");
      field2 = field1;
      if (illegalAccessException != null)
        field2 = readFieldOrNull(illegalAccessException, (Class)paramClass, paramString); 
    } 
    return (T)field2;
  }
  
  public void afterHandshake(SSLSocket paramSSLSocket) {}
  
  public CertificateChainCleaner buildCertificateChainCleaner(X509TrustManager paramX509TrustManager) {
    return (CertificateChainCleaner)new BasicCertificateChainCleaner(TrustRootIndex.get(paramX509TrustManager));
  }
  
  public void configureTlsExtensions(SSLSocket paramSSLSocket, String paramString, List<Protocol> paramList) {}
  
  public void connectSocket(Socket paramSocket, InetSocketAddress paramInetSocketAddress, int paramInt) throws IOException {
    paramSocket.connect(paramInetSocketAddress, paramInt);
  }
  
  public String getPrefix() {
    return "OkHttp";
  }
  
  public String getSelectedProtocol(SSLSocket paramSSLSocket) {
    return null;
  }
  
  public Object getStackTraceForCloseable(String paramString) {
    return logger.isLoggable(Level.FINE) ? new Throwable(paramString) : null;
  }
  
  public boolean isCleartextTrafficPermitted(String paramString) {
    return true;
  }
  
  public void log(int paramInt, String paramString, Throwable paramThrowable) {
    Level level;
    if (paramInt == 5) {
      level = Level.WARNING;
    } else {
      level = Level.INFO;
    } 
    logger.log(level, paramString, paramThrowable);
  }
  
  public void logCloseableLeak(String paramString, Object paramObject) {
    String str = paramString;
    if (paramObject == null)
      str = paramString + " To see where this was allocated, set the OkHttpClient logger level to FINE: Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);"; 
    log(5, str, (Throwable)paramObject);
  }
  
  public X509TrustManager trustManager(SSLSocketFactory paramSSLSocketFactory) {
    try {
      paramSSLSocketFactory = readFieldOrNull(paramSSLSocketFactory, Class.forName("sun.security.ssl.SSLContextImpl"), "context");
      if (paramSSLSocketFactory == null)
        return null; 
      X509TrustManager x509TrustManager = readFieldOrNull(paramSSLSocketFactory, X509TrustManager.class, "trustManager");
    } catch (ClassNotFoundException classNotFoundException) {
      classNotFoundException = null;
    } 
    return (X509TrustManager)classNotFoundException;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/platform/Platform.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */