package okhttp3.internal.http2;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.http.RealResponseBody;
import okhttp3.internal.http.RequestLine;
import okhttp3.internal.http.StatusLine;
import okio.ByteString;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

public final class Http2Codec implements HttpCodec {
  private static final ByteString CONNECTION = ByteString.encodeUtf8("connection");
  
  private static final ByteString ENCODING;
  
  private static final ByteString HOST = ByteString.encodeUtf8("host");
  
  private static final List<ByteString> HTTP_2_SKIPPED_REQUEST_HEADERS;
  
  private static final List<ByteString> HTTP_2_SKIPPED_RESPONSE_HEADERS;
  
  private static final ByteString KEEP_ALIVE = ByteString.encodeUtf8("keep-alive");
  
  private static final ByteString PROXY_CONNECTION = ByteString.encodeUtf8("proxy-connection");
  
  private static final ByteString TE;
  
  private static final ByteString TRANSFER_ENCODING = ByteString.encodeUtf8("transfer-encoding");
  
  private static final ByteString UPGRADE;
  
  private final OkHttpClient client;
  
  private final Http2Connection connection;
  
  private Http2Stream stream;
  
  final StreamAllocation streamAllocation;
  
  static {
    TE = ByteString.encodeUtf8("te");
    ENCODING = ByteString.encodeUtf8("encoding");
    UPGRADE = ByteString.encodeUtf8("upgrade");
    HTTP_2_SKIPPED_REQUEST_HEADERS = Util.immutableList((Object[])new ByteString[] { 
          CONNECTION, HOST, KEEP_ALIVE, PROXY_CONNECTION, TE, TRANSFER_ENCODING, ENCODING, UPGRADE, Header.TARGET_METHOD, Header.TARGET_PATH, 
          Header.TARGET_SCHEME, Header.TARGET_AUTHORITY });
    HTTP_2_SKIPPED_RESPONSE_HEADERS = Util.immutableList((Object[])new ByteString[] { CONNECTION, HOST, KEEP_ALIVE, PROXY_CONNECTION, TE, TRANSFER_ENCODING, ENCODING, UPGRADE });
  }
  
  public Http2Codec(OkHttpClient paramOkHttpClient, StreamAllocation paramStreamAllocation, Http2Connection paramHttp2Connection) {
    this.client = paramOkHttpClient;
    this.streamAllocation = paramStreamAllocation;
    this.connection = paramHttp2Connection;
  }
  
  public static List<Header> http2HeadersList(Request paramRequest) {
    Headers headers = paramRequest.headers();
    ArrayList<Header> arrayList = new ArrayList(headers.size() + 4);
    arrayList.add(new Header(Header.TARGET_METHOD, paramRequest.method()));
    arrayList.add(new Header(Header.TARGET_PATH, RequestLine.requestPath(paramRequest.url())));
    String str = paramRequest.header("Host");
    if (str != null)
      arrayList.add(new Header(Header.TARGET_AUTHORITY, str)); 
    arrayList.add(new Header(Header.TARGET_SCHEME, paramRequest.url().scheme()));
    byte b = 0;
    int i = headers.size();
    while (b < i) {
      ByteString byteString = ByteString.encodeUtf8(headers.name(b).toLowerCase(Locale.US));
      if (!HTTP_2_SKIPPED_REQUEST_HEADERS.contains(byteString))
        arrayList.add(new Header(byteString, headers.value(b))); 
      b++;
    } 
    return arrayList;
  }
  
  public static Response.Builder readHttp2HeadersList(List<Header> paramList) throws IOException {
    Header header = null;
    Headers.Builder builder = new Headers.Builder();
    byte b = 0;
    int i = paramList.size();
    while (b < i) {
      Headers.Builder builder1;
      Header header1 = paramList.get(b);
      if (header1 == null) {
        builder1 = builder;
        header1 = header;
        if (header != null) {
          builder1 = builder;
          header1 = header;
          if (((StatusLine)header).code == 100) {
            header1 = null;
            builder1 = new Headers.Builder();
          } 
        } 
      } else {
        ByteString byteString = header1.name;
        String str = header1.value.utf8();
        if (byteString.equals(Header.RESPONSE_STATUS)) {
          StatusLine statusLine = StatusLine.parse("HTTP/1.1 " + str);
          builder1 = builder;
        } else {
          builder1 = builder;
          header1 = header;
          if (!HTTP_2_SKIPPED_RESPONSE_HEADERS.contains(byteString)) {
            Internal.instance.addLenient(builder, byteString.utf8(), str);
            builder1 = builder;
            header1 = header;
          } 
        } 
      } 
      b++;
      builder = builder1;
      header = header1;
    } 
    if (header == null)
      throw new ProtocolException("Expected ':status' header not present"); 
    return (new Response.Builder()).protocol(Protocol.HTTP_2).code(((StatusLine)header).code).message(((StatusLine)header).message).headers(builder.build());
  }
  
  public void cancel() {
    if (this.stream != null)
      this.stream.closeLater(ErrorCode.CANCEL); 
  }
  
  public Sink createRequestBody(Request paramRequest, long paramLong) {
    return this.stream.getSink();
  }
  
  public void finishRequest() throws IOException {
    this.stream.getSink().close();
  }
  
  public void flushRequest() throws IOException {
    this.connection.flush();
  }
  
  public ResponseBody openResponseBody(Response paramResponse) throws IOException {
    StreamFinishingSource streamFinishingSource = new StreamFinishingSource(this.stream.getSource());
    return (ResponseBody)new RealResponseBody(paramResponse.headers(), Okio.buffer((Source)streamFinishingSource));
  }
  
  public Response.Builder readResponseHeaders(boolean paramBoolean) throws IOException {
    Response.Builder builder1 = readHttp2HeadersList(this.stream.takeResponseHeaders());
    Response.Builder builder2 = builder1;
    if (paramBoolean) {
      builder2 = builder1;
      if (Internal.instance.code(builder1) == 100)
        builder2 = null; 
    } 
    return builder2;
  }
  
  public void writeRequestHeaders(Request paramRequest) throws IOException {
    if (this.stream == null) {
      boolean bool;
      if (paramRequest.body() != null) {
        bool = true;
      } else {
        bool = false;
      } 
      List<Header> list = http2HeadersList(paramRequest);
      this.stream = this.connection.newStream(list, bool);
      this.stream.readTimeout().timeout(this.client.readTimeoutMillis(), TimeUnit.MILLISECONDS);
      this.stream.writeTimeout().timeout(this.client.writeTimeoutMillis(), TimeUnit.MILLISECONDS);
    } 
  }
  
  class StreamFinishingSource extends ForwardingSource {
    StreamFinishingSource(Source param1Source) {
      super(param1Source);
    }
    
    public void close() throws IOException {
      Http2Codec.this.streamAllocation.streamFinished(false, Http2Codec.this);
      super.close();
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/http2/Http2Codec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */