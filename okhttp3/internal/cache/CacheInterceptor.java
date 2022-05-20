package okhttp3.internal.cache;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.http.RealResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public final class CacheInterceptor implements Interceptor {
  final InternalCache cache;
  
  public CacheInterceptor(InternalCache paramInternalCache) {
    this.cache = paramInternalCache;
  }
  
  private Response cacheWritingResponse(final CacheRequest cacheRequest, Response paramResponse) throws IOException {
    if (cacheRequest == null)
      return paramResponse; 
    Sink sink = cacheRequest.body();
    Response response = paramResponse;
    if (sink != null) {
      Source source = new Source() {
          boolean cacheRequestClosed;
          
          public void close() throws IOException {
            if (!this.cacheRequestClosed && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
              this.cacheRequestClosed = true;
              cacheRequest.abort();
            } 
            source.close();
          }
          
          public long read(Buffer param1Buffer, long param1Long) throws IOException {
            try {
              param1Long = source.read(param1Buffer, param1Long);
              if (param1Long == -1L) {
                if (!this.cacheRequestClosed) {
                  this.cacheRequestClosed = true;
                  cacheBody.close();
                } 
                return -1L;
              } 
            } catch (IOException iOException) {
              if (!this.cacheRequestClosed) {
                this.cacheRequestClosed = true;
                cacheRequest.abort();
              } 
              throw iOException;
            } 
            iOException.copyTo(cacheBody.buffer(), iOException.size() - param1Long, param1Long);
            cacheBody.emitCompleteSegments();
            return param1Long;
          }
          
          public Timeout timeout() {
            return source.timeout();
          }
        };
      response = paramResponse.newBuilder().body((ResponseBody)new RealResponseBody(paramResponse.headers(), Okio.buffer(source))).build();
    } 
    return response;
  }
  
  private static Headers combine(Headers paramHeaders1, Headers paramHeaders2) {
    Headers.Builder builder = new Headers.Builder();
    byte b = 0;
    int i = paramHeaders1.size();
    while (b < i) {
      String str1 = paramHeaders1.name(b);
      String str2 = paramHeaders1.value(b);
      if ((!"Warning".equalsIgnoreCase(str1) || !str2.startsWith("1")) && (!isEndToEnd(str1) || paramHeaders2.get(str1) == null))
        Internal.instance.addLenient(builder, str1, str2); 
      b++;
    } 
    b = 0;
    i = paramHeaders2.size();
    while (b < i) {
      String str = paramHeaders2.name(b);
      if (!"Content-Length".equalsIgnoreCase(str) && isEndToEnd(str))
        Internal.instance.addLenient(builder, str, paramHeaders2.value(b)); 
      b++;
    } 
    return builder.build();
  }
  
  static boolean isEndToEnd(String paramString) {
    return (!"Connection".equalsIgnoreCase(paramString) && !"Keep-Alive".equalsIgnoreCase(paramString) && !"Proxy-Authenticate".equalsIgnoreCase(paramString) && !"Proxy-Authorization".equalsIgnoreCase(paramString) && !"TE".equalsIgnoreCase(paramString) && !"Trailers".equalsIgnoreCase(paramString) && !"Transfer-Encoding".equalsIgnoreCase(paramString) && !"Upgrade".equalsIgnoreCase(paramString));
  }
  
  private static Response stripBody(Response paramResponse) {
    Response response = paramResponse;
    if (paramResponse != null) {
      response = paramResponse;
      if (paramResponse.body() != null)
        response = paramResponse.newBuilder().body(null).build(); 
    } 
    return response;
  }
  
  public Response intercept(Interceptor.Chain paramChain) throws IOException {
    Response response3;
    if (this.cache != null) {
      response2 = this.cache.get(paramChain.request());
    } else {
      response2 = null;
    } 
    CacheStrategy cacheStrategy = (new CacheStrategy.Factory(System.currentTimeMillis(), paramChain.request(), response2)).get();
    Request request = cacheStrategy.networkRequest;
    Response response4 = cacheStrategy.cacheResponse;
    if (this.cache != null)
      this.cache.trackResponse(cacheStrategy); 
    if (response2 != null && response4 == null)
      Util.closeQuietly((Closeable)response2.body()); 
    if (request == null && response4 == null)
      return (new Response.Builder()).request(paramChain.request()).protocol(Protocol.HTTP_1_1).code(504).message("Unsatisfiable Request (only-if-cached)").body(Util.EMPTY_RESPONSE).sentRequestAtMillis(-1L).receivedResponseAtMillis(System.currentTimeMillis()).build(); 
    if (request == null)
      return response4.newBuilder().cacheResponse(stripBody(response4)).build(); 
    try {
      response3 = paramChain.proceed(request);
      if (response3 == null && response2 != null)
        Util.closeQuietly((Closeable)response2.body()); 
    } finally {
      if (!false && response2 != null)
        Util.closeQuietly((Closeable)response2.body()); 
    } 
    Response response2 = response3.newBuilder().cacheResponse(stripBody(response4)).networkResponse(stripBody(response3)).build();
    Response response1 = response2;
    if (this.cache != null) {
      if (HttpHeaders.hasBody(response2) && CacheStrategy.isCacheable(response2, request))
        return cacheWritingResponse(this.cache.put(response2), response2); 
      response1 = response2;
      if (HttpMethod.invalidatesCache(request.method()))
        try {
          this.cache.remove(request);
          response1 = response2;
        } catch (IOException iOException) {
          response1 = response2;
        }  
    } 
    return response1;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/cache/CacheInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */