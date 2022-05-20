package org.xutils.http.app;

import java.security.cert.X509Certificate;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;

public class DefaultParamsBuilder implements ParamsBuilder {
  private static SSLSocketFactory trustAllSSlSocketFactory;
  
  public static SSLSocketFactory getTrustAllSSLSocketFactory() {
    // Byte code:
    //   0: getstatic org/xutils/http/app/DefaultParamsBuilder.trustAllSSlSocketFactory : Ljavax/net/ssl/SSLSocketFactory;
    //   3: ifnonnull -> 53
    //   6: ldc org/xutils/http/app/DefaultParamsBuilder
    //   8: monitorenter
    //   9: getstatic org/xutils/http/app/DefaultParamsBuilder.trustAllSSlSocketFactory : Ljavax/net/ssl/SSLSocketFactory;
    //   12: ifnonnull -> 50
    //   15: new org/xutils/http/app/DefaultParamsBuilder$1
    //   18: astore_0
    //   19: aload_0
    //   20: invokespecial <init> : ()V
    //   23: ldc 'TLS'
    //   25: invokestatic getInstance : (Ljava/lang/String;)Ljavax/net/ssl/SSLContext;
    //   28: astore_1
    //   29: aload_1
    //   30: aconst_null
    //   31: iconst_1
    //   32: anewarray javax/net/ssl/TrustManager
    //   35: dup
    //   36: iconst_0
    //   37: aload_0
    //   38: aastore
    //   39: aconst_null
    //   40: invokevirtual init : ([Ljavax/net/ssl/KeyManager;[Ljavax/net/ssl/TrustManager;Ljava/security/SecureRandom;)V
    //   43: aload_1
    //   44: invokevirtual getSocketFactory : ()Ljavax/net/ssl/SSLSocketFactory;
    //   47: putstatic org/xutils/http/app/DefaultParamsBuilder.trustAllSSlSocketFactory : Ljavax/net/ssl/SSLSocketFactory;
    //   50: ldc org/xutils/http/app/DefaultParamsBuilder
    //   52: monitorexit
    //   53: getstatic org/xutils/http/app/DefaultParamsBuilder.trustAllSSlSocketFactory : Ljavax/net/ssl/SSLSocketFactory;
    //   56: areturn
    //   57: astore_1
    //   58: aload_1
    //   59: invokevirtual getMessage : ()Ljava/lang/String;
    //   62: aload_1
    //   63: invokestatic e : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   66: goto -> 50
    //   69: astore_1
    //   70: ldc org/xutils/http/app/DefaultParamsBuilder
    //   72: monitorexit
    //   73: aload_1
    //   74: athrow
    // Exception table:
    //   from	to	target	type
    //   9	23	69	finally
    //   23	50	57	java/lang/Throwable
    //   23	50	69	finally
    //   50	53	69	finally
    //   58	66	69	finally
    //   70	73	69	finally
  }
  
  public String buildCacheKey(RequestParams paramRequestParams, String[] paramArrayOfString) {
    String str1 = null;
    String str2 = str1;
    if (paramArrayOfString != null) {
      str2 = str1;
      if (paramArrayOfString.length > 0) {
        str1 = paramRequestParams.getUri() + "?";
        int i = paramArrayOfString.length;
        byte b = 0;
        while (true) {
          str2 = str1;
          if (b < i) {
            String str3 = paramArrayOfString[b];
            String str4 = paramRequestParams.getStringParameter(str3);
            str2 = str1;
            if (str4 != null)
              str2 = str1 + str3 + "=" + str4 + "&"; 
            b++;
            str1 = str2;
            continue;
          } 
          break;
        } 
      } 
    } 
    return str2;
  }
  
  public void buildParams(RequestParams paramRequestParams) throws Throwable {}
  
  public void buildSign(RequestParams paramRequestParams, String[] paramArrayOfString) throws Throwable {}
  
  public String buildUri(RequestParams paramRequestParams, HttpRequest paramHttpRequest) throws Throwable {
    return paramHttpRequest.host() + "/" + paramHttpRequest.path();
  }
  
  public SSLSocketFactory getSSLSocketFactory() throws Throwable {
    return getTrustAllSSLSocketFactory();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/app/DefaultParamsBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */