package okhttp3.internal.cache;

import java.io.IOException;
import okhttp3.Request;
import okhttp3.Response;

public interface InternalCache {
  Response get(Request paramRequest) throws IOException;
  
  CacheRequest put(Response paramResponse) throws IOException;
  
  void remove(Request paramRequest) throws IOException;
  
  void trackConditionalCacheHit();
  
  void trackResponse(CacheStrategy paramCacheStrategy);
  
  void update(Response paramResponse1, Response paramResponse2);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/cache/InternalCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */