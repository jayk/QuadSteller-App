package okhttp3.internal.cache;

import java.io.IOException;
import okio.Sink;

public interface CacheRequest {
  void abort();
  
  Sink body() throws IOException;
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/cache/CacheRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */