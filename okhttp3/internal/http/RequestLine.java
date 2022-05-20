package okhttp3.internal.http;

import java.net.Proxy;
import okhttp3.HttpUrl;
import okhttp3.Request;

public final class RequestLine {
  public static String get(Request paramRequest, Proxy.Type paramType) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramRequest.method());
    stringBuilder.append(' ');
    if (includeAuthorityInRequestLine(paramRequest, paramType)) {
      stringBuilder.append(paramRequest.url());
      stringBuilder.append(" HTTP/1.1");
      return stringBuilder.toString();
    } 
    stringBuilder.append(requestPath(paramRequest.url()));
    stringBuilder.append(" HTTP/1.1");
    return stringBuilder.toString();
  }
  
  private static boolean includeAuthorityInRequestLine(Request paramRequest, Proxy.Type paramType) {
    return (!paramRequest.isHttps() && paramType == Proxy.Type.HTTP);
  }
  
  public static String requestPath(HttpUrl paramHttpUrl) {
    String str2 = paramHttpUrl.encodedPath();
    String str3 = paramHttpUrl.encodedQuery();
    String str1 = str2;
    if (str3 != null)
      str1 = str2 + '?' + str3; 
    return str1;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/http/RequestLine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */