package okhttp3.internal.http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Challenge;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

public final class HttpHeaders {
  private static final Pattern PARAMETER = Pattern.compile(" +([^ \"=]*)=(:?\"([^\"]*)\"|([^ \"=]*)) *(:?,|$)");
  
  private static final String QUOTED_STRING = "\"([^\"]*)\"";
  
  private static final String TOKEN = "([^ \"=]*)";
  
  public static long contentLength(Headers paramHeaders) {
    return stringToLong(paramHeaders.get("Content-Length"));
  }
  
  public static long contentLength(Response paramResponse) {
    return contentLength(paramResponse.headers());
  }
  
  public static boolean hasBody(Response paramResponse) {
    boolean bool = false;
    if (!paramResponse.request().method().equals("HEAD")) {
      int i = paramResponse.code();
      if ((i < 100 || i >= 200) && i != 204 && i != 304)
        return true; 
      if (contentLength(paramResponse) != -1L || "chunked".equalsIgnoreCase(paramResponse.header("Transfer-Encoding")))
        bool = true; 
    } 
    return bool;
  }
  
  public static boolean hasVaryAll(Headers paramHeaders) {
    return varyFields(paramHeaders).contains("*");
  }
  
  public static boolean hasVaryAll(Response paramResponse) {
    return hasVaryAll(paramResponse.headers());
  }
  
  public static List<Challenge> parseChallenges(Headers paramHeaders, String paramString) {
    ArrayList<Challenge> arrayList = new ArrayList();
    for (String str : paramHeaders.values(paramString)) {
      int i = str.indexOf(' ');
      if (i != -1) {
        Matcher matcher = PARAMETER.matcher(str);
        int j;
        for (j = i; matcher.find(j); j = matcher.end()) {
          if (str.regionMatches(true, matcher.start(1), "realm", 0, 5)) {
            paramString = str.substring(0, i);
            String str1 = matcher.group(3);
            if (str1 != null) {
              arrayList.add(new Challenge(paramString, str1));
              break;
            } 
          } 
        } 
      } 
    } 
    return arrayList;
  }
  
  public static int parseSeconds(String paramString, int paramInt) {
    try {
      long l = Long.parseLong(paramString);
      if (l > 2147483647L)
        return Integer.MAX_VALUE; 
      if (l < 0L)
        return 0; 
      paramInt = (int)l;
    } catch (NumberFormatException numberFormatException) {}
    return paramInt;
  }
  
  public static void receiveHeaders(CookieJar paramCookieJar, HttpUrl paramHttpUrl, Headers paramHeaders) {
    if (paramCookieJar != CookieJar.NO_COOKIES) {
      List list = Cookie.parseAll(paramHttpUrl, paramHeaders);
      if (!list.isEmpty())
        paramCookieJar.saveFromResponse(paramHttpUrl, list); 
    } 
  }
  
  public static int skipUntil(String paramString1, int paramInt, String paramString2) {
    while (true) {
      if (paramInt >= paramString1.length() || paramString2.indexOf(paramString1.charAt(paramInt)) != -1)
        return paramInt; 
      paramInt++;
    } 
  }
  
  public static int skipWhitespace(String paramString, int paramInt) {
    while (true) {
      if (paramInt < paramString.length()) {
        char c = paramString.charAt(paramInt);
        if (c == ' ' || c == '\t') {
          paramInt++;
          continue;
        } 
      } 
      return paramInt;
    } 
  }
  
  private static long stringToLong(String paramString) {
    long l2;
    long l1 = -1L;
    if (paramString == null)
      return l1; 
    try {
      l2 = Long.parseLong(paramString);
    } catch (NumberFormatException numberFormatException) {
      l2 = l1;
    } 
    return l2;
  }
  
  public static Set<String> varyFields(Headers paramHeaders) {
    Set<?> set = Collections.emptySet();
    byte b = 0;
    int i = paramHeaders.size();
    label17: while (b < i) {
      if (!"Vary".equalsIgnoreCase(paramHeaders.name(b)))
        continue; 
      String str = paramHeaders.value(b);
      Set<?> set1 = set;
      if (set.isEmpty())
        set1 = new TreeSet(String.CASE_INSENSITIVE_ORDER); 
      String[] arrayOfString = str.split(",");
      int j = arrayOfString.length;
      byte b1 = 0;
      while (true) {
        set = set1;
        if (b1 < j) {
          set1.add(arrayOfString[b1].trim());
          b1++;
          continue;
        } 
        b++;
        continue label17;
      } 
    } 
    return (Set)set;
  }
  
  private static Set<String> varyFields(Response paramResponse) {
    return varyFields(paramResponse.headers());
  }
  
  public static Headers varyHeaders(Headers paramHeaders1, Headers paramHeaders2) {
    Set<String> set = varyFields(paramHeaders2);
    if (set.isEmpty())
      return (new Headers.Builder()).build(); 
    Headers.Builder builder = new Headers.Builder();
    byte b = 0;
    int i = paramHeaders1.size();
    while (b < i) {
      String str = paramHeaders1.name(b);
      if (set.contains(str))
        builder.add(str, paramHeaders1.value(b)); 
      b++;
    } 
    return builder.build();
  }
  
  public static Headers varyHeaders(Response paramResponse) {
    return varyHeaders(paramResponse.networkResponse().request().headers(), paramResponse.headers());
  }
  
  public static boolean varyMatches(Response paramResponse, Headers paramHeaders, Request paramRequest) {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic varyFields : (Lokhttp3/Response;)Ljava/util/Set;
    //   4: invokeinterface iterator : ()Ljava/util/Iterator;
    //   9: astore_3
    //   10: aload_3
    //   11: invokeinterface hasNext : ()Z
    //   16: ifeq -> 51
    //   19: aload_3
    //   20: invokeinterface next : ()Ljava/lang/Object;
    //   25: checkcast java/lang/String
    //   28: astore_0
    //   29: aload_1
    //   30: aload_0
    //   31: invokevirtual values : (Ljava/lang/String;)Ljava/util/List;
    //   34: aload_2
    //   35: aload_0
    //   36: invokevirtual headers : (Ljava/lang/String;)Ljava/util/List;
    //   39: invokestatic equal : (Ljava/lang/Object;Ljava/lang/Object;)Z
    //   42: ifne -> 10
    //   45: iconst_0
    //   46: istore #4
    //   48: iload #4
    //   50: ireturn
    //   51: iconst_1
    //   52: istore #4
    //   54: goto -> 48
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/http/HttpHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */