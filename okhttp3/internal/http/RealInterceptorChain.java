package okhttp3.internal.http;

import java.io.IOException;
import java.util.List;
import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.StreamAllocation;

public final class RealInterceptorChain implements Interceptor.Chain {
  private int calls;
  
  private final RealConnection connection;
  
  private final HttpCodec httpCodec;
  
  private final int index;
  
  private final List<Interceptor> interceptors;
  
  private final Request request;
  
  private final StreamAllocation streamAllocation;
  
  public RealInterceptorChain(List<Interceptor> paramList, StreamAllocation paramStreamAllocation, HttpCodec paramHttpCodec, RealConnection paramRealConnection, int paramInt, Request paramRequest) {
    this.interceptors = paramList;
    this.connection = paramRealConnection;
    this.streamAllocation = paramStreamAllocation;
    this.httpCodec = paramHttpCodec;
    this.index = paramInt;
    this.request = paramRequest;
  }
  
  public Connection connection() {
    return (Connection)this.connection;
  }
  
  public HttpCodec httpStream() {
    return this.httpCodec;
  }
  
  public Response proceed(Request paramRequest) throws IOException {
    return proceed(paramRequest, this.streamAllocation, this.httpCodec, this.connection);
  }
  
  public Response proceed(Request paramRequest, StreamAllocation paramStreamAllocation, HttpCodec paramHttpCodec, RealConnection paramRealConnection) throws IOException {
    if (this.index >= this.interceptors.size())
      throw new AssertionError(); 
    this.calls++;
    if (this.httpCodec != null && !this.connection.supportsUrl(paramRequest.url()))
      throw new IllegalStateException("network interceptor " + this.interceptors.get(this.index - 1) + " must retain the same host and port"); 
    if (this.httpCodec != null && this.calls > 1)
      throw new IllegalStateException("network interceptor " + this.interceptors.get(this.index - 1) + " must call proceed() exactly once"); 
    RealInterceptorChain realInterceptorChain = new RealInterceptorChain(this.interceptors, paramStreamAllocation, paramHttpCodec, paramRealConnection, this.index + 1, paramRequest);
    Interceptor interceptor = this.interceptors.get(this.index);
    Response response = interceptor.intercept(realInterceptorChain);
    if (paramHttpCodec != null && this.index + 1 < this.interceptors.size() && realInterceptorChain.calls != 1)
      throw new IllegalStateException("network interceptor " + interceptor + " must call proceed() exactly once"); 
    if (response == null)
      throw new NullPointerException("interceptor " + interceptor + " returned null"); 
    return response;
  }
  
  public Request request() {
    return this.request;
  }
  
  public StreamAllocation streamAllocation() {
    return this.streamAllocation;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/http/RealInterceptorChain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */