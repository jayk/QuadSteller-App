package okhttp3.internal.platform;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import javax.net.ssl.SSLSocket;
import okhttp3.Protocol;

class JdkWithJettyBootPlatform extends Platform {
  private final Class<?> clientProviderClass;
  
  private final Method getMethod;
  
  private final Method putMethod;
  
  private final Method removeMethod;
  
  private final Class<?> serverProviderClass;
  
  JdkWithJettyBootPlatform(Method paramMethod1, Method paramMethod2, Method paramMethod3, Class<?> paramClass1, Class<?> paramClass2) {
    this.putMethod = paramMethod1;
    this.getMethod = paramMethod2;
    this.removeMethod = paramMethod3;
    this.clientProviderClass = paramClass1;
    this.serverProviderClass = paramClass2;
  }
  
  public static Platform buildIfSupported() {
    try {
      Class<?> clazz1 = Class.forName("org.eclipse.jetty.alpn.ALPN");
      StringBuilder stringBuilder1 = new StringBuilder();
      this();
      Class<?> clazz3 = Class.forName(stringBuilder1.append("org.eclipse.jetty.alpn.ALPN").append("$Provider").toString());
      stringBuilder1 = new StringBuilder();
      this();
      Class<?> clazz2 = Class.forName(stringBuilder1.append("org.eclipse.jetty.alpn.ALPN").append("$ClientProvider").toString());
      StringBuilder stringBuilder2 = new StringBuilder();
      this();
      Class<?> clazz4 = Class.forName(stringBuilder2.append("org.eclipse.jetty.alpn.ALPN").append("$ServerProvider").toString());
      Method method2 = clazz1.getMethod("put", new Class[] { SSLSocket.class, clazz3 });
      Method method1 = clazz1.getMethod("get", new Class[] { SSLSocket.class });
      Method method3 = clazz1.getMethod("remove", new Class[] { SSLSocket.class });
      JdkWithJettyBootPlatform jdkWithJettyBootPlatform = new JdkWithJettyBootPlatform();
      this(method2, method1, method3, clazz2, clazz4);
      return jdkWithJettyBootPlatform;
    } catch (ClassNotFoundException classNotFoundException) {
    
    } catch (NoSuchMethodException noSuchMethodException) {}
    return null;
  }
  
  public void afterHandshake(SSLSocket paramSSLSocket) {
    try {
      this.removeMethod.invoke(null, new Object[] { paramSSLSocket });
      return;
    } catch (IllegalAccessException illegalAccessException) {
    
    } catch (InvocationTargetException invocationTargetException) {}
    throw new AssertionError();
  }
  
  public void configureTlsExtensions(SSLSocket paramSSLSocket, String paramString, List<Protocol> paramList) {
    List<String> list = alpnProtocolNames(paramList);
    try {
      ClassLoader classLoader = Platform.class.getClassLoader();
      Class<?> clazz1 = this.clientProviderClass;
      Class<?> clazz2 = this.serverProviderClass;
      JettyNegoProvider jettyNegoProvider = new JettyNegoProvider();
      this(list);
      Object object = Proxy.newProxyInstance(classLoader, new Class[] { clazz1, clazz2 }, jettyNegoProvider);
      this.putMethod.invoke(null, new Object[] { paramSSLSocket, object });
      return;
    } catch (InvocationTargetException invocationTargetException) {
    
    } catch (IllegalAccessException illegalAccessException) {}
    throw new AssertionError(illegalAccessException);
  }
  
  public String getSelectedProtocol(SSLSocket paramSSLSocket) {
    SSLSocket sSLSocket = null;
    try {
      String str;
      JettyNegoProvider jettyNegoProvider = (JettyNegoProvider)Proxy.getInvocationHandler(this.getMethod.invoke(null, new Object[] { paramSSLSocket }));
      if (!jettyNegoProvider.unsupported && jettyNegoProvider.selected == null) {
        Platform.get().log(4, "ALPN callback dropped: HTTP/2 is disabled. Is alpn-boot on the boot class path?", null);
        return (String)sSLSocket;
      } 
      paramSSLSocket = sSLSocket;
      if (!jettyNegoProvider.unsupported)
        str = jettyNegoProvider.selected; 
      return str;
    } catch (InvocationTargetException invocationTargetException) {
    
    } catch (IllegalAccessException illegalAccessException) {}
    throw new AssertionError();
  }
  
  private static class JettyNegoProvider implements InvocationHandler {
    private final List<String> protocols;
    
    String selected;
    
    boolean unsupported;
    
    JettyNegoProvider(List<String> param1List) {
      this.protocols = param1List;
    }
    
    public Object invoke(Object param1Object, Method param1Method, Object[] param1ArrayOfObject) throws Throwable {
      // Byte code:
      //   0: aload_2
      //   1: invokevirtual getName : ()Ljava/lang/String;
      //   4: astore #4
      //   6: aload_2
      //   7: invokevirtual getReturnType : ()Ljava/lang/Class;
      //   10: astore #5
      //   12: aload_3
      //   13: astore_1
      //   14: aload_3
      //   15: ifnonnull -> 22
      //   18: getstatic okhttp3/internal/Util.EMPTY_STRING_ARRAY : [Ljava/lang/String;
      //   21: astore_1
      //   22: aload #4
      //   24: ldc 'supports'
      //   26: invokevirtual equals : (Ljava/lang/Object;)Z
      //   29: ifeq -> 47
      //   32: getstatic java/lang/Boolean.TYPE : Ljava/lang/Class;
      //   35: aload #5
      //   37: if_acmpne -> 47
      //   40: iconst_1
      //   41: invokestatic valueOf : (Z)Ljava/lang/Boolean;
      //   44: astore_1
      //   45: aload_1
      //   46: areturn
      //   47: aload #4
      //   49: ldc 'unsupported'
      //   51: invokevirtual equals : (Ljava/lang/Object;)Z
      //   54: ifeq -> 75
      //   57: getstatic java/lang/Void.TYPE : Ljava/lang/Class;
      //   60: aload #5
      //   62: if_acmpne -> 75
      //   65: aload_0
      //   66: iconst_1
      //   67: putfield unsupported : Z
      //   70: aconst_null
      //   71: astore_1
      //   72: goto -> 45
      //   75: aload #4
      //   77: ldc 'protocols'
      //   79: invokevirtual equals : (Ljava/lang/Object;)Z
      //   82: ifeq -> 98
      //   85: aload_1
      //   86: arraylength
      //   87: ifne -> 98
      //   90: aload_0
      //   91: getfield protocols : Ljava/util/List;
      //   94: astore_1
      //   95: goto -> 45
      //   98: aload #4
      //   100: ldc 'selectProtocol'
      //   102: invokevirtual equals : (Ljava/lang/Object;)Z
      //   105: ifne -> 118
      //   108: aload #4
      //   110: ldc 'select'
      //   112: invokevirtual equals : (Ljava/lang/Object;)Z
      //   115: ifeq -> 233
      //   118: ldc java/lang/String
      //   120: aload #5
      //   122: if_acmpne -> 233
      //   125: aload_1
      //   126: arraylength
      //   127: iconst_1
      //   128: if_icmpne -> 233
      //   131: aload_1
      //   132: iconst_0
      //   133: aaload
      //   134: instanceof java/util/List
      //   137: ifeq -> 233
      //   140: aload_1
      //   141: iconst_0
      //   142: aaload
      //   143: checkcast java/util/List
      //   146: astore_1
      //   147: iconst_0
      //   148: istore #6
      //   150: aload_1
      //   151: invokeinterface size : ()I
      //   156: istore #7
      //   158: iload #6
      //   160: iload #7
      //   162: if_icmpge -> 211
      //   165: aload_0
      //   166: getfield protocols : Ljava/util/List;
      //   169: aload_1
      //   170: iload #6
      //   172: invokeinterface get : (I)Ljava/lang/Object;
      //   177: invokeinterface contains : (Ljava/lang/Object;)Z
      //   182: ifeq -> 205
      //   185: aload_1
      //   186: iload #6
      //   188: invokeinterface get : (I)Ljava/lang/Object;
      //   193: checkcast java/lang/String
      //   196: astore_1
      //   197: aload_0
      //   198: aload_1
      //   199: putfield selected : Ljava/lang/String;
      //   202: goto -> 45
      //   205: iinc #6, 1
      //   208: goto -> 158
      //   211: aload_0
      //   212: getfield protocols : Ljava/util/List;
      //   215: iconst_0
      //   216: invokeinterface get : (I)Ljava/lang/Object;
      //   221: checkcast java/lang/String
      //   224: astore_1
      //   225: aload_0
      //   226: aload_1
      //   227: putfield selected : Ljava/lang/String;
      //   230: goto -> 45
      //   233: aload #4
      //   235: ldc 'protocolSelected'
      //   237: invokevirtual equals : (Ljava/lang/Object;)Z
      //   240: ifne -> 253
      //   243: aload #4
      //   245: ldc 'selected'
      //   247: invokevirtual equals : (Ljava/lang/Object;)Z
      //   250: ifeq -> 274
      //   253: aload_1
      //   254: arraylength
      //   255: iconst_1
      //   256: if_icmpne -> 274
      //   259: aload_0
      //   260: aload_1
      //   261: iconst_0
      //   262: aaload
      //   263: checkcast java/lang/String
      //   266: putfield selected : Ljava/lang/String;
      //   269: aconst_null
      //   270: astore_1
      //   271: goto -> 45
      //   274: aload_2
      //   275: aload_0
      //   276: aload_1
      //   277: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
      //   280: astore_1
      //   281: goto -> 45
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/platform/JdkWithJettyBootPlatform.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */