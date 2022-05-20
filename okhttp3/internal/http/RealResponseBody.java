package okhttp3.internal.http;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;

public final class RealResponseBody extends ResponseBody {
  private final Headers headers;
  
  private final BufferedSource source;
  
  public RealResponseBody(Headers paramHeaders, BufferedSource paramBufferedSource) {
    this.headers = paramHeaders;
    this.source = paramBufferedSource;
  }
  
  public long contentLength() {
    return HttpHeaders.contentLength(this.headers);
  }
  
  public MediaType contentType() {
    null = this.headers.get("Content-Type");
    return (null != null) ? MediaType.parse(null) : null;
  }
  
  public BufferedSource source() {
    return this.source;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/http/RealResponseBody.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */