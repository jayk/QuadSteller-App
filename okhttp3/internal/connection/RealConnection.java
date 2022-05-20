package okhttp3.internal.connection;

import java.io.IOException;
import java.lang.ref.Reference;
import java.net.ConnectException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.Address;
import okhttp3.CipherSuite;
import okhttp3.Connection;
import okhttp3.ConnectionPool;
import okhttp3.ConnectionSpec;
import okhttp3.Handshake;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.Version;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http1.Http1Codec;
import okhttp3.internal.http2.ErrorCode;
import okhttp3.internal.http2.Http2Codec;
import okhttp3.internal.http2.Http2Connection;
import okhttp3.internal.http2.Http2Stream;
import okhttp3.internal.platform.Platform;
import okhttp3.internal.tls.OkHostnameVerifier;
import okhttp3.internal.ws.RealWebSocket;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

public final class RealConnection extends Http2Connection.Listener implements Connection {
  private static final String NPE_THROW_WITH_NULL = "throw with null exception";
  
  public int allocationLimit = 1;
  
  public final List<Reference<StreamAllocation>> allocations = new ArrayList<Reference<StreamAllocation>>();
  
  private final ConnectionPool connectionPool;
  
  private Handshake handshake;
  
  private Http2Connection http2Connection;
  
  public long idleAtNanos = Long.MAX_VALUE;
  
  public boolean noNewStreams;
  
  private Protocol protocol;
  
  private Socket rawSocket;
  
  private final Route route;
  
  private BufferedSink sink;
  
  private Socket socket;
  
  private BufferedSource source;
  
  public int successCount;
  
  public RealConnection(ConnectionPool paramConnectionPool, Route paramRoute) {
    this.connectionPool = paramConnectionPool;
    this.route = paramRoute;
  }
  
  private void connectSocket(int paramInt1, int paramInt2) throws IOException {
    Socket socket;
    Proxy proxy = this.route.proxy();
    Address address = this.route.address();
    if (proxy.type() == Proxy.Type.DIRECT || proxy.type() == Proxy.Type.HTTP) {
      socket = address.socketFactory().createSocket();
    } else {
      socket = new Socket((Proxy)socket);
    } 
    this.rawSocket = socket;
    this.rawSocket.setSoTimeout(paramInt2);
    try {
      Platform.get().connectSocket(this.rawSocket, this.route.socketAddress(), paramInt1);
      try {
        this.source = Okio.buffer(Okio.source(this.rawSocket));
        this.sink = Okio.buffer(Okio.sink(this.rawSocket));
      } catch (NullPointerException nullPointerException) {}
    } catch (ConnectException connectException1) {
      ConnectException connectException2 = new ConnectException("Failed to connect to " + this.route.socketAddress());
      connectException2.initCause(connectException1);
      throw connectException2;
    } 
  }
  
  private void connectTls(ConnectionSpecSelector paramConnectionSpecSelector) throws IOException {
    Protocol protocol;
    IOException iOException1;
    SSLPeerUnverifiedException sSLPeerUnverifiedException;
    Address address = this.route.address();
    SSLSocketFactory sSLSocketFactory = address.sslSocketFactory();
    SSLSocket sSLSocket1 = null;
    SSLSocket sSLSocket2 = null;
    try {
      SSLSocket sSLSocket = (SSLSocket)sSLSocketFactory.createSocket(this.rawSocket, address.url().host(), address.url().port(), true);
      sSLSocket2 = sSLSocket;
      sSLSocket1 = sSLSocket;
      ConnectionSpec connectionSpec = paramConnectionSpecSelector.configureSecureSocket(sSLSocket);
      sSLSocket2 = sSLSocket;
      sSLSocket1 = sSLSocket;
      if (connectionSpec.supportsTlsExtensions()) {
        sSLSocket2 = sSLSocket;
        sSLSocket1 = sSLSocket;
        Platform.get().configureTlsExtensions(sSLSocket, address.url().host(), address.protocols());
      } 
      sSLSocket2 = sSLSocket;
      sSLSocket1 = sSLSocket;
      sSLSocket.startHandshake();
      sSLSocket2 = sSLSocket;
      sSLSocket1 = sSLSocket;
      Handshake handshake = Handshake.get(sSLSocket.getSession());
      sSLSocket2 = sSLSocket;
    } catch (AssertionError assertionError) {
    
    } finally {
      if (sSLSocket1 != null)
        Platform.get().afterHandshake(sSLSocket1); 
      if (!false)
        Util.closeQuietly(sSLSocket1); 
    } 
    IOException iOException3 = iOException1;
    IOException iOException2 = iOException1;
    address.certificatePinner().check(address.url().host(), sSLPeerUnverifiedException.peerCertificates());
    iOException3 = iOException1;
    iOException2 = iOException1;
    if (paramConnectionSpecSelector.supportsTlsExtensions()) {
      iOException3 = iOException1;
      iOException2 = iOException1;
      String str = Platform.get().getSelectedProtocol((SSLSocket)iOException1);
    } else {
      paramConnectionSpecSelector = null;
    } 
    iOException3 = iOException1;
    iOException2 = iOException1;
    this.socket = (Socket)iOException1;
    iOException3 = iOException1;
    iOException2 = iOException1;
    this.source = Okio.buffer(Okio.source(this.socket));
    iOException3 = iOException1;
    iOException2 = iOException1;
    this.sink = Okio.buffer(Okio.sink(this.socket));
    iOException3 = iOException1;
    iOException2 = iOException1;
    this.handshake = (Handshake)sSLPeerUnverifiedException;
    if (paramConnectionSpecSelector != null) {
      iOException3 = iOException1;
      iOException2 = iOException1;
      protocol = Protocol.get((String)paramConnectionSpecSelector);
    } else {
      iOException3 = iOException1;
      iOException2 = iOException1;
      protocol = Protocol.HTTP_1_1;
    } 
    iOException3 = iOException1;
    iOException2 = iOException1;
    this.protocol = protocol;
    if (iOException1 != null)
      Platform.get().afterHandshake((SSLSocket)iOException1); 
    if (!true)
      Util.closeQuietly((Socket)iOException1); 
  }
  
  private void connectTunnel(int paramInt1, int paramInt2, int paramInt3) throws IOException {
    Request request = createTunnelRequest();
    HttpUrl httpUrl = request.url();
    byte b = 0;
    while (true) {
      if (++b > 21)
        throw new ProtocolException("Too many tunnel connections attempted: " + '\025'); 
      connectSocket(paramInt1, paramInt2);
      request = createTunnel(paramInt2, paramInt3, request, httpUrl);
      if (request == null)
        return; 
      Util.closeQuietly(this.rawSocket);
      this.rawSocket = null;
      this.sink = null;
      this.source = null;
    } 
  }
  
  private Request createTunnel(int paramInt1, int paramInt2, Request paramRequest, HttpUrl paramHttpUrl) throws IOException {
    Request request;
    Response response;
    Http1Codec http1Codec = null;
    String str = "CONNECT " + Util.hostHeader(paramHttpUrl, true) + " HTTP/1.1";
    do {
      Http1Codec http1Codec1 = new Http1Codec(null, null, this.source, this.sink);
      this.source.timeout().timeout(paramInt1, TimeUnit.MILLISECONDS);
      this.sink.timeout().timeout(paramInt2, TimeUnit.MILLISECONDS);
      http1Codec1.writeRequest(paramRequest.headers(), str);
      http1Codec1.finishRequest();
      response = http1Codec1.readResponseHeaders(false).request(paramRequest).build();
      long l1 = HttpHeaders.contentLength(response);
      long l2 = l1;
      if (l1 == -1L)
        l2 = 0L; 
      Source source = http1Codec1.newFixedLengthSource(l2);
      Util.skipAll(source, 2147483647, TimeUnit.MILLISECONDS);
      source.close();
      switch (response.code()) {
        default:
          throw new IOException("Unexpected response code for CONNECT: " + response.code());
        case 200:
          if (this.source.buffer().exhausted()) {
            http1Codec1 = http1Codec;
            if (!this.sink.buffer().exhausted())
              throw new IOException("TLS tunnel buffered too many bytes!"); 
            break;
          } 
          throw new IOException("TLS tunnel buffered too many bytes!");
        case 407:
          break;
      } 
      request = this.route.address().proxyAuthenticator().authenticate(this.route, response);
      if (request == null)
        throw new IOException("Failed to authenticate with proxy"); 
      Request request1 = request;
    } while (!"close".equalsIgnoreCase(response.header("Connection")));
    return request;
  }
  
  private Request createTunnelRequest() {
    return (new Request.Builder()).url(this.route.address().url()).header("Host", Util.hostHeader(this.route.address().url(), true)).header("Proxy-Connection", "Keep-Alive").header("User-Agent", Version.userAgent()).build();
  }
  
  private void establishProtocol(ConnectionSpecSelector paramConnectionSpecSelector) throws IOException {
    if (this.route.address().sslSocketFactory() == null) {
      this.protocol = Protocol.HTTP_1_1;
      this.socket = this.rawSocket;
      return;
    } 
    connectTls(paramConnectionSpecSelector);
    if (this.protocol == Protocol.HTTP_2) {
      this.socket.setSoTimeout(0);
      this.http2Connection = (new Http2Connection.Builder(true)).socket(this.socket, this.route.address().url().host(), this.source, this.sink).listener(this).build();
      this.http2Connection.start();
    } 
  }
  
  public static RealConnection testConnection(ConnectionPool paramConnectionPool, Route paramRoute, Socket paramSocket, long paramLong) {
    RealConnection realConnection = new RealConnection(paramConnectionPool, paramRoute);
    realConnection.socket = paramSocket;
    realConnection.idleAtNanos = paramLong;
    return realConnection;
  }
  
  public void cancel() {
    Util.closeQuietly(this.rawSocket);
  }
  
  public void connect(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
    if (this.protocol != null)
      throw new IllegalStateException("already connected"); 
    ConnectionPool connectionPool1 = null;
    List<ConnectionSpec> list = this.route.address().connectionSpecs();
    ConnectionSpecSelector connectionSpecSelector = new ConnectionSpecSelector(list);
    ConnectionPool connectionPool2 = connectionPool1;
    if (this.route.address().sslSocketFactory() == null) {
      if (!list.contains(ConnectionSpec.CLEARTEXT))
        throw new RouteException(new UnknownServiceException("CLEARTEXT communication not enabled for client")); 
      String str = this.route.address().url().host();
      connectionPool2 = connectionPool1;
      if (!Platform.get().isCleartextTrafficPermitted(str))
        throw new RouteException(new UnknownServiceException("CLEARTEXT communication to " + str + " not permitted by network security policy")); 
    } 
    while (true) {
      try {
        if (this.route.requiresTunnel()) {
          connectTunnel(paramInt1, paramInt2, paramInt3);
        } else {
          connectSocket(paramInt1, paramInt2);
        } 
        establishProtocol(connectionSpecSelector);
        if (this.http2Connection != null)
          synchronized (this.connectionPool) {
            this.allocationLimit = this.http2Connection.maxConcurrentStreams();
            return;
          }  
        return;
      } catch (IOException iOException) {
        Util.closeQuietly(this.socket);
        Util.closeQuietly(this.rawSocket);
        this.socket = null;
        this.rawSocket = null;
        this.source = null;
        this.sink = null;
        this.handshake = null;
        this.protocol = null;
        this.http2Connection = null;
        if (connectionPool2 == null) {
          RouteException routeException = new RouteException(iOException);
        } else {
          connectionPool2.addConnectException(iOException);
          connectionPool1 = connectionPool2;
        } 
        if (paramBoolean) {
          connectionPool2 = connectionPool1;
          if (!connectionSpecSelector.connectionFailed(iOException))
            throw connectionPool1; 
          continue;
        } 
        throw connectionPool1;
      } 
    } 
  }
  
  public Handshake handshake() {
    return this.handshake;
  }
  
  public boolean isEligible(Address paramAddress, @Nullable Route paramRoute) {
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (this.allocations.size() < this.allocationLimit) {
      if (this.noNewStreams)
        return bool1; 
    } else {
      return bool2;
    } 
    bool2 = bool1;
    if (Internal.instance.equalsNonHost(this.route.address(), paramAddress)) {
      if (paramAddress.url().host().equals(route().address().url().host()))
        return true; 
      bool2 = bool1;
      if (this.http2Connection != null) {
        bool2 = bool1;
        if (paramRoute != null) {
          bool2 = bool1;
          if (paramRoute.proxy().type() == Proxy.Type.DIRECT) {
            bool2 = bool1;
            if (this.route.proxy().type() == Proxy.Type.DIRECT) {
              bool2 = bool1;
              if (this.route.socketAddress().equals(paramRoute.socketAddress())) {
                bool2 = bool1;
                if (paramRoute.address().hostnameVerifier() == OkHostnameVerifier.INSTANCE) {
                  bool2 = bool1;
                  if (supportsUrl(paramAddress.url()))
                    try {
                      paramAddress.certificatePinner().check(paramAddress.url().host(), handshake().peerCertificates());
                      bool2 = true;
                    } catch (SSLPeerUnverifiedException sSLPeerUnverifiedException) {
                      bool2 = bool1;
                    }  
                } 
              } 
            } 
          } 
        } 
      } 
    } 
    return bool2;
  }
  
  public boolean isHealthy(boolean paramBoolean) {
    boolean bool1 = true;
    if (this.socket.isClosed() || this.socket.isInputShutdown() || this.socket.isOutputShutdown())
      return false; 
    if (this.http2Connection != null) {
      boolean bool = bool1;
      if (this.http2Connection.isShutdown())
        bool = false; 
      return bool;
    } 
    boolean bool2 = bool1;
    if (paramBoolean)
      try {
        int i = this.socket.getSoTimeout();
        try {
          this.socket.setSoTimeout(1);
          paramBoolean = this.source.exhausted();
          if (paramBoolean) {
            this.socket.setSoTimeout(i);
            return false;
          } 
          this.socket.setSoTimeout(i);
          bool2 = bool1;
        } finally {
          Exception exception;
        } 
      } catch (SocketTimeoutException socketTimeoutException) {
        bool2 = bool1;
      } catch (IOException iOException) {
        bool2 = false;
      }  
    return bool2;
  }
  
  public boolean isMultiplexed() {
    return (this.http2Connection != null);
  }
  
  public HttpCodec newCodec(OkHttpClient paramOkHttpClient, StreamAllocation paramStreamAllocation) throws SocketException {
    if (this.http2Connection != null)
      return (HttpCodec)new Http2Codec(paramOkHttpClient, paramStreamAllocation, this.http2Connection); 
    this.socket.setSoTimeout(paramOkHttpClient.readTimeoutMillis());
    this.source.timeout().timeout(paramOkHttpClient.readTimeoutMillis(), TimeUnit.MILLISECONDS);
    this.sink.timeout().timeout(paramOkHttpClient.writeTimeoutMillis(), TimeUnit.MILLISECONDS);
    return (HttpCodec)new Http1Codec(paramOkHttpClient, paramStreamAllocation, this.source, this.sink);
  }
  
  public RealWebSocket.Streams newWebSocketStreams(final StreamAllocation streamAllocation) {
    return new RealWebSocket.Streams(true, this.source, this.sink) {
        public void close() throws IOException {
          streamAllocation.streamFinished(true, streamAllocation.codec());
        }
      };
  }
  
  public void onSettings(Http2Connection paramHttp2Connection) {
    synchronized (this.connectionPool) {
      this.allocationLimit = paramHttp2Connection.maxConcurrentStreams();
      return;
    } 
  }
  
  public void onStream(Http2Stream paramHttp2Stream) throws IOException {
    paramHttp2Stream.close(ErrorCode.REFUSED_STREAM);
  }
  
  public Protocol protocol() {
    return this.protocol;
  }
  
  public Route route() {
    return this.route;
  }
  
  public Socket socket() {
    return this.socket;
  }
  
  public boolean supportsUrl(HttpUrl paramHttpUrl) {
    boolean bool = false;
    if (paramHttpUrl.port() == this.route.address().url().port()) {
      if (!paramHttpUrl.host().equals(this.route.address().url().host()))
        return (this.handshake != null && OkHostnameVerifier.INSTANCE.verify(paramHttpUrl.host(), this.handshake.peerCertificates().get(0))); 
      bool = true;
    } 
    return bool;
  }
  
  public String toString() {
    StringBuilder stringBuilder = (new StringBuilder()).append("Connection{").append(this.route.address().url().host()).append(":").append(this.route.address().url().port()).append(", proxy=").append(this.route.proxy()).append(" hostAddress=").append(this.route.socketAddress()).append(" cipherSuite=");
    if (this.handshake != null) {
      CipherSuite cipherSuite = this.handshake.cipherSuite();
      return stringBuilder.append(cipherSuite).append(" protocol=").append(this.protocol).append('}').toString();
    } 
    String str = "none";
    return stringBuilder.append(str).append(" protocol=").append(this.protocol).append('}').toString();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/connection/RealConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */