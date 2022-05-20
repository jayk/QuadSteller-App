package okhttp3.internal.connection;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http.RealInterceptorChain;

public final class ConnectInterceptor implements Interceptor {
  public final OkHttpClient client;
  
  public ConnectInterceptor(OkHttpClient paramOkHttpClient) {
    this.client = paramOkHttpClient;
  }
  
  public Response intercept(Interceptor.Chain paramChain) throws IOException {
    RealInterceptorChain realInterceptorChain = (RealInterceptorChain)paramChain;
    Request request = realInterceptorChain.request();
    StreamAllocation streamAllocation = realInterceptorChain.streamAllocation();
    if (!request.method().equals("GET")) {
      boolean bool1 = true;
      return realInterceptorChain.proceed(request, streamAllocation, streamAllocation.newStream(this.client, bool1), streamAllocation.connection());
    } 
    boolean bool = false;
    return realInterceptorChain.proceed(request, streamAllocation, streamAllocation.newStream(this.client, bool), streamAllocation.connection());
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/connection/ConnectInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */