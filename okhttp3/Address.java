package okhttp3;

import java.net.Proxy;
import java.net.ProxySelector;
import java.util.List;
import javax.annotation.Nullable;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.internal.Util;

public final class Address {
  @Nullable
  final CertificatePinner certificatePinner;
  
  final List<ConnectionSpec> connectionSpecs;
  
  final Dns dns;
  
  @Nullable
  final HostnameVerifier hostnameVerifier;
  
  final List<Protocol> protocols;
  
  @Nullable
  final Proxy proxy;
  
  final Authenticator proxyAuthenticator;
  
  final ProxySelector proxySelector;
  
  final SocketFactory socketFactory;
  
  @Nullable
  final SSLSocketFactory sslSocketFactory;
  
  final HttpUrl url;
  
  public Address(String paramString, int paramInt, Dns paramDns, SocketFactory paramSocketFactory, @Nullable SSLSocketFactory paramSSLSocketFactory, @Nullable HostnameVerifier paramHostnameVerifier, @Nullable CertificatePinner paramCertificatePinner, Authenticator paramAuthenticator, @Nullable Proxy paramProxy, List<Protocol> paramList, List<ConnectionSpec> paramList1, ProxySelector paramProxySelector) {
    String str;
    HttpUrl.Builder builder = new HttpUrl.Builder();
    if (paramSSLSocketFactory != null) {
      str = "https";
    } else {
      str = "http";
    } 
    this.url = builder.scheme(str).host(paramString).port(paramInt).build();
    if (paramDns == null)
      throw new NullPointerException("dns == null"); 
    this.dns = paramDns;
    if (paramSocketFactory == null)
      throw new NullPointerException("socketFactory == null"); 
    this.socketFactory = paramSocketFactory;
    if (paramAuthenticator == null)
      throw new NullPointerException("proxyAuthenticator == null"); 
    this.proxyAuthenticator = paramAuthenticator;
    if (paramList == null)
      throw new NullPointerException("protocols == null"); 
    this.protocols = Util.immutableList(paramList);
    if (paramList1 == null)
      throw new NullPointerException("connectionSpecs == null"); 
    this.connectionSpecs = Util.immutableList(paramList1);
    if (paramProxySelector == null)
      throw new NullPointerException("proxySelector == null"); 
    this.proxySelector = paramProxySelector;
    this.proxy = paramProxy;
    this.sslSocketFactory = paramSSLSocketFactory;
    this.hostnameVerifier = paramHostnameVerifier;
    this.certificatePinner = paramCertificatePinner;
  }
  
  @Nullable
  public CertificatePinner certificatePinner() {
    return this.certificatePinner;
  }
  
  public List<ConnectionSpec> connectionSpecs() {
    return this.connectionSpecs;
  }
  
  public Dns dns() {
    return this.dns;
  }
  
  public boolean equals(@Nullable Object paramObject) {
    return (paramObject instanceof Address && this.url.equals(((Address)paramObject).url) && equalsNonHost((Address)paramObject));
  }
  
  boolean equalsNonHost(Address paramAddress) {
    return (this.dns.equals(paramAddress.dns) && this.proxyAuthenticator.equals(paramAddress.proxyAuthenticator) && this.protocols.equals(paramAddress.protocols) && this.connectionSpecs.equals(paramAddress.connectionSpecs) && this.proxySelector.equals(paramAddress.proxySelector) && Util.equal(this.proxy, paramAddress.proxy) && Util.equal(this.sslSocketFactory, paramAddress.sslSocketFactory) && Util.equal(this.hostnameVerifier, paramAddress.hostnameVerifier) && Util.equal(this.certificatePinner, paramAddress.certificatePinner) && url().port() == paramAddress.url().port());
  }
  
  public int hashCode() {
    byte b1;
    byte b2;
    byte b3;
    int i = 0;
    int j = this.url.hashCode();
    int k = this.dns.hashCode();
    int m = this.proxyAuthenticator.hashCode();
    int n = this.protocols.hashCode();
    int i1 = this.connectionSpecs.hashCode();
    int i2 = this.proxySelector.hashCode();
    if (this.proxy != null) {
      b1 = this.proxy.hashCode();
    } else {
      b1 = 0;
    } 
    if (this.sslSocketFactory != null) {
      b2 = this.sslSocketFactory.hashCode();
    } else {
      b2 = 0;
    } 
    if (this.hostnameVerifier != null) {
      b3 = this.hostnameVerifier.hashCode();
    } else {
      b3 = 0;
    } 
    if (this.certificatePinner != null)
      i = this.certificatePinner.hashCode(); 
    return (((((((((j + 527) * 31 + k) * 31 + m) * 31 + n) * 31 + i1) * 31 + i2) * 31 + b1) * 31 + b2) * 31 + b3) * 31 + i;
  }
  
  @Nullable
  public HostnameVerifier hostnameVerifier() {
    return this.hostnameVerifier;
  }
  
  public List<Protocol> protocols() {
    return this.protocols;
  }
  
  @Nullable
  public Proxy proxy() {
    return this.proxy;
  }
  
  public Authenticator proxyAuthenticator() {
    return this.proxyAuthenticator;
  }
  
  public ProxySelector proxySelector() {
    return this.proxySelector;
  }
  
  public SocketFactory socketFactory() {
    return this.socketFactory;
  }
  
  @Nullable
  public SSLSocketFactory sslSocketFactory() {
    return this.sslSocketFactory;
  }
  
  public String toString() {
    StringBuilder stringBuilder = (new StringBuilder()).append("Address{").append(this.url.host()).append(":").append(this.url.port());
    if (this.proxy != null) {
      stringBuilder.append(", proxy=").append(this.proxy);
      stringBuilder.append("}");
      return stringBuilder.toString();
    } 
    stringBuilder.append(", proxySelector=").append(this.proxySelector);
    stringBuilder.append("}");
    return stringBuilder.toString();
  }
  
  public HttpUrl url() {
    return this.url;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/Address.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */