package okhttp3.internal.cache;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Internal;
import okhttp3.internal.http.HttpDate;
import okhttp3.internal.http.HttpHeaders;

public final class CacheStrategy {
  @Nullable
  public final Response cacheResponse;
  
  @Nullable
  public final Request networkRequest;
  
  CacheStrategy(Request paramRequest, Response paramResponse) {
    this.networkRequest = paramRequest;
    this.cacheResponse = paramResponse;
  }
  
  public static boolean isCacheable(Response paramResponse, Request paramRequest) {
    boolean bool1 = false;
    switch (paramResponse.code()) {
      default:
        return bool1;
      case 302:
      case 307:
        if (paramResponse.header("Expires") == null && paramResponse.cacheControl().maxAgeSeconds() == -1 && !paramResponse.cacheControl().isPublic()) {
          boolean bool = bool1;
          if (paramResponse.cacheControl().isPrivate())
            break; 
          return bool;
        } 
        break;
      case 200:
      case 203:
      case 204:
      case 300:
      case 301:
      case 308:
      case 404:
      case 405:
      case 410:
      case 414:
      case 501:
        break;
    } 
    boolean bool2 = bool1;
    if (!paramResponse.cacheControl().noStore()) {
      bool2 = bool1;
      if (!paramRequest.cacheControl().noStore())
        bool2 = true; 
    } 
    return bool2;
  }
  
  public static class Factory {
    private int ageSeconds = -1;
    
    final Response cacheResponse;
    
    private String etag;
    
    private Date expires;
    
    private Date lastModified;
    
    private String lastModifiedString;
    
    final long nowMillis;
    
    private long receivedResponseMillis;
    
    final Request request;
    
    private long sentRequestMillis;
    
    private Date servedDate;
    
    private String servedDateString;
    
    public Factory(long param1Long, Request param1Request, Response param1Response) {
      this.nowMillis = param1Long;
      this.request = param1Request;
      this.cacheResponse = param1Response;
      if (param1Response != null) {
        this.sentRequestMillis = param1Response.sentRequestAtMillis();
        this.receivedResponseMillis = param1Response.receivedResponseAtMillis();
        Headers headers = param1Response.headers();
        byte b = 0;
        int i = headers.size();
        while (b < i) {
          String str1 = headers.name(b);
          String str2 = headers.value(b);
          if ("Date".equalsIgnoreCase(str1)) {
            this.servedDate = HttpDate.parse(str2);
            this.servedDateString = str2;
          } else if ("Expires".equalsIgnoreCase(str1)) {
            this.expires = HttpDate.parse(str2);
          } else if ("Last-Modified".equalsIgnoreCase(str1)) {
            this.lastModified = HttpDate.parse(str2);
            this.lastModifiedString = str2;
          } else if ("ETag".equalsIgnoreCase(str1)) {
            this.etag = str2;
          } else if ("Age".equalsIgnoreCase(str1)) {
            this.ageSeconds = HttpHeaders.parseSeconds(str2, -1);
          } 
          b++;
        } 
      } 
    }
    
    private long cacheResponseAge() {
      long l = 0L;
      if (this.servedDate != null)
        l = Math.max(0L, this.receivedResponseMillis - this.servedDate.getTime()); 
      if (this.ageSeconds != -1)
        l = Math.max(l, TimeUnit.SECONDS.toMillis(this.ageSeconds)); 
      return l + this.receivedResponseMillis - this.sentRequestMillis + this.nowMillis - this.receivedResponseMillis;
    }
    
    private long computeFreshnessLifetime() {
      long l1 = 0L;
      CacheControl cacheControl = this.cacheResponse.cacheControl();
      if (cacheControl.maxAgeSeconds() != -1)
        return TimeUnit.SECONDS.toMillis(cacheControl.maxAgeSeconds()); 
      if (this.expires != null) {
        if (this.servedDate != null) {
          l = this.servedDate.getTime();
        } else {
          l = this.receivedResponseMillis;
        } 
        long l = this.expires.getTime() - l;
        if (l <= 0L)
          l = 0L; 
        return l;
      } 
      long l2 = l1;
      if (this.lastModified != null) {
        l2 = l1;
        if (this.cacheResponse.request().url().query() == null) {
          if (this.servedDate != null) {
            l2 = this.servedDate.getTime();
          } else {
            l2 = this.sentRequestMillis;
          } 
          long l = l2 - this.lastModified.getTime();
          l2 = l1;
          if (l > 0L)
            l2 = l / 10L; 
        } 
      } 
      return l2;
    }
    
    private CacheStrategy getCandidate() {
      String str;
      if (this.cacheResponse == null)
        return new CacheStrategy(this.request, null); 
      if (this.request.isHttps() && this.cacheResponse.handshake() == null)
        return new CacheStrategy(this.request, null); 
      if (!CacheStrategy.isCacheable(this.cacheResponse, this.request))
        return new CacheStrategy(this.request, null); 
      CacheControl cacheControl1 = this.request.cacheControl();
      if (cacheControl1.noCache() || hasConditions(this.request))
        return new CacheStrategy(this.request, null); 
      long l1 = cacheResponseAge();
      long l2 = computeFreshnessLifetime();
      long l3 = l2;
      if (cacheControl1.maxAgeSeconds() != -1)
        l3 = Math.min(l2, TimeUnit.SECONDS.toMillis(cacheControl1.maxAgeSeconds())); 
      l2 = 0L;
      if (cacheControl1.minFreshSeconds() != -1)
        l2 = TimeUnit.SECONDS.toMillis(cacheControl1.minFreshSeconds()); 
      long l4 = 0L;
      CacheControl cacheControl2 = this.cacheResponse.cacheControl();
      long l5 = l4;
      if (!cacheControl2.mustRevalidate()) {
        l5 = l4;
        if (cacheControl1.maxStaleSeconds() != -1)
          l5 = TimeUnit.SECONDS.toMillis(cacheControl1.maxStaleSeconds()); 
      } 
      if (!cacheControl2.noCache() && l1 + l2 < l3 + l5) {
        Response.Builder builder1 = this.cacheResponse.newBuilder();
        if (l1 + l2 >= l3)
          builder1.addHeader("Warning", "110 HttpURLConnection \"Response is stale\""); 
        if (l1 > 86400000L && isFreshnessLifetimeHeuristic())
          builder1.addHeader("Warning", "113 HttpURLConnection \"Heuristic expiration\""); 
        return new CacheStrategy(null, builder1.build());
      } 
      if (this.etag != null) {
        String str1 = "If-None-Match";
        str = this.etag;
      } else if (this.lastModified != null) {
        String str1 = "If-Modified-Since";
        str = this.lastModifiedString;
      } else if (this.servedDate != null) {
        String str1 = "If-Modified-Since";
        str = this.servedDateString;
      } else {
        return new CacheStrategy(this.request, null);
      } 
      Headers.Builder builder = this.request.headers().newBuilder();
      Internal.instance.addLenient(builder, (String)cacheControl1, str);
      return new CacheStrategy(this.request.newBuilder().headers(builder.build()).build(), this.cacheResponse);
    }
    
    private static boolean hasConditions(Request param1Request) {
      return (param1Request.header("If-Modified-Since") != null || param1Request.header("If-None-Match") != null);
    }
    
    private boolean isFreshnessLifetimeHeuristic() {
      return (this.cacheResponse.cacheControl().maxAgeSeconds() == -1 && this.expires == null);
    }
    
    public CacheStrategy get() {
      CacheStrategy cacheStrategy1 = getCandidate();
      CacheStrategy cacheStrategy2 = cacheStrategy1;
      if (cacheStrategy1.networkRequest != null) {
        cacheStrategy2 = cacheStrategy1;
        if (this.request.cacheControl().onlyIfCached())
          cacheStrategy2 = new CacheStrategy(null, null); 
      } 
      return cacheStrategy2;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/cache/CacheStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */