package okhttp3.internal.http;

import java.io.IOException;
import java.net.ProtocolException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.StreamAllocation;
import okio.BufferedSink;
import okio.Okio;

public final class CallServerInterceptor implements Interceptor {
  private final boolean forWebSocket;
  
  public CallServerInterceptor(boolean paramBoolean) {
    this.forWebSocket = paramBoolean;
  }
  
  public Response intercept(Interceptor.Chain paramChain) throws IOException {
    Response.Builder builder1;
    paramChain = paramChain;
    HttpCodec httpCodec = paramChain.httpStream();
    StreamAllocation streamAllocation = paramChain.streamAllocation();
    RealConnection realConnection = (RealConnection)paramChain.connection();
    Request request = paramChain.request();
    long l = System.currentTimeMillis();
    httpCodec.writeRequestHeaders(request);
    Interceptor.Chain chain = null;
    Response.Builder builder2 = null;
    paramChain = chain;
    if (HttpMethod.permitsRequestBody(request.method())) {
      paramChain = chain;
      if (request.body() != null) {
        if ("100-continue".equalsIgnoreCase(request.header("Expect"))) {
          httpCodec.flushRequest();
          builder2 = httpCodec.readResponseHeaders(true);
        } 
        if (builder2 == null) {
          BufferedSink bufferedSink = Okio.buffer(httpCodec.createRequestBody(request, request.body().contentLength()));
          request.body().writeTo(bufferedSink);
          bufferedSink.close();
          builder1 = builder2;
        } else {
          builder1 = builder2;
          if (!realConnection.isMultiplexed()) {
            streamAllocation.noNewStreams();
            builder1 = builder2;
          } 
        } 
      } 
    } 
    httpCodec.finishRequest();
    builder2 = builder1;
    if (builder1 == null)
      builder2 = httpCodec.readResponseHeaders(false); 
    Response response = builder2.request(request).handshake(streamAllocation.connection().handshake()).sentRequestAtMillis(l).receivedResponseAtMillis(System.currentTimeMillis()).build();
    int i = response.code();
    if (this.forWebSocket && i == 101) {
      response = response.newBuilder().body(Util.EMPTY_RESPONSE).build();
    } else {
      response = response.newBuilder().body(httpCodec.openResponseBody(response)).build();
    } 
    if ("close".equalsIgnoreCase(response.request().header("Connection")) || "close".equalsIgnoreCase(response.header("Connection")))
      streamAllocation.noNewStreams(); 
    if ((i == 204 || i == 205) && response.body().contentLength() > 0L)
      throw new ProtocolException("HTTP " + i + " had non-zero Content-Length: " + response.body().contentLength()); 
    return response;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/http/CallServerInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */