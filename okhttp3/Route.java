package okhttp3;

import java.net.InetSocketAddress;
import java.net.Proxy;
import javax.annotation.Nullable;

public final class Route {
  final Address address;
  
  final InetSocketAddress inetSocketAddress;
  
  final Proxy proxy;
  
  public Route(Address paramAddress, Proxy paramProxy, InetSocketAddress paramInetSocketAddress) {
    if (paramAddress == null)
      throw new NullPointerException("address == null"); 
    if (paramProxy == null)
      throw new NullPointerException("proxy == null"); 
    if (paramInetSocketAddress == null)
      throw new NullPointerException("inetSocketAddress == null"); 
    this.address = paramAddress;
    this.proxy = paramProxy;
    this.inetSocketAddress = paramInetSocketAddress;
  }
  
  public Address address() {
    return this.address;
  }
  
  public boolean equals(@Nullable Object paramObject) {
    return (paramObject instanceof Route && ((Route)paramObject).address.equals(this.address) && ((Route)paramObject).proxy.equals(this.proxy) && ((Route)paramObject).inetSocketAddress.equals(this.inetSocketAddress));
  }
  
  public int hashCode() {
    return ((this.address.hashCode() + 527) * 31 + this.proxy.hashCode()) * 31 + this.inetSocketAddress.hashCode();
  }
  
  public Proxy proxy() {
    return this.proxy;
  }
  
  public boolean requiresTunnel() {
    return (this.address.sslSocketFactory != null && this.proxy.type() == Proxy.Type.HTTP);
  }
  
  public InetSocketAddress socketAddress() {
    return this.inetSocketAddress;
  }
  
  public String toString() {
    return "Route{" + this.inetSocketAddress + "}";
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/Route.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */