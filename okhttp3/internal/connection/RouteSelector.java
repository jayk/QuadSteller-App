package okhttp3.internal.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import okhttp3.Address;
import okhttp3.HttpUrl;
import okhttp3.Route;
import okhttp3.internal.Util;

public final class RouteSelector {
  private final Address address;
  
  private List<InetSocketAddress> inetSocketAddresses = Collections.emptyList();
  
  private InetSocketAddress lastInetSocketAddress;
  
  private Proxy lastProxy;
  
  private int nextInetSocketAddressIndex;
  
  private int nextProxyIndex;
  
  private final List<Route> postponedRoutes = new ArrayList<Route>();
  
  private List<Proxy> proxies = Collections.emptyList();
  
  private final RouteDatabase routeDatabase;
  
  public RouteSelector(Address paramAddress, RouteDatabase paramRouteDatabase) {
    this.address = paramAddress;
    this.routeDatabase = paramRouteDatabase;
    resetNextProxy(paramAddress.url(), paramAddress.proxy());
  }
  
  static String getHostString(InetSocketAddress paramInetSocketAddress) {
    InetAddress inetAddress = paramInetSocketAddress.getAddress();
    return (inetAddress == null) ? paramInetSocketAddress.getHostName() : inetAddress.getHostAddress();
  }
  
  private boolean hasNextInetSocketAddress() {
    return (this.nextInetSocketAddressIndex < this.inetSocketAddresses.size());
  }
  
  private boolean hasNextPostponed() {
    return !this.postponedRoutes.isEmpty();
  }
  
  private boolean hasNextProxy() {
    return (this.nextProxyIndex < this.proxies.size());
  }
  
  private InetSocketAddress nextInetSocketAddress() throws IOException {
    if (!hasNextInetSocketAddress())
      throw new SocketException("No route to " + this.address.url().host() + "; exhausted inet socket addresses: " + this.inetSocketAddresses); 
    List<InetSocketAddress> list = this.inetSocketAddresses;
    int i = this.nextInetSocketAddressIndex;
    this.nextInetSocketAddressIndex = i + 1;
    return list.get(i);
  }
  
  private Route nextPostponed() {
    return this.postponedRoutes.remove(0);
  }
  
  private Proxy nextProxy() throws IOException {
    if (!hasNextProxy())
      throw new SocketException("No route to " + this.address.url().host() + "; exhausted proxy configurations: " + this.proxies); 
    List<Proxy> list = this.proxies;
    int i = this.nextProxyIndex;
    this.nextProxyIndex = i + 1;
    Proxy proxy = list.get(i);
    resetNextInetSocketAddress(proxy);
    return proxy;
  }
  
  private void resetNextInetSocketAddress(Proxy paramProxy) throws IOException {
    String str;
    int i;
    this.inetSocketAddresses = new ArrayList<InetSocketAddress>();
    if (paramProxy.type() == Proxy.Type.DIRECT || paramProxy.type() == Proxy.Type.SOCKS) {
      str = this.address.url().host();
      i = this.address.url().port();
    } else {
      SocketAddress socketAddress = paramProxy.address();
      if (!(socketAddress instanceof InetSocketAddress))
        throw new IllegalArgumentException("Proxy.address() is not an InetSocketAddress: " + socketAddress.getClass()); 
      InetSocketAddress inetSocketAddress = (InetSocketAddress)socketAddress;
      str = getHostString(inetSocketAddress);
      i = inetSocketAddress.getPort();
    } 
    if (i < 1 || i > 65535)
      throw new SocketException("No route to " + str + ":" + i + "; port is out of range"); 
    if (paramProxy.type() == Proxy.Type.SOCKS) {
      this.inetSocketAddresses.add(InetSocketAddress.createUnresolved(str, i));
    } else {
      List<InetAddress> list = this.address.dns().lookup(str);
      if (list.isEmpty())
        throw new UnknownHostException(this.address.dns() + " returned no addresses for " + str); 
      byte b = 0;
      int j = list.size();
      while (true) {
        if (b < j) {
          InetAddress inetAddress = list.get(b);
          this.inetSocketAddresses.add(new InetSocketAddress(inetAddress, i));
          b++;
          continue;
        } 
        this.nextInetSocketAddressIndex = 0;
        return;
      } 
    } 
    this.nextInetSocketAddressIndex = 0;
  }
  
  private void resetNextProxy(HttpUrl paramHttpUrl, Proxy paramProxy) {
    if (paramProxy != null) {
      this.proxies = Collections.singletonList(paramProxy);
    } else {
      List<Proxy> list = this.address.proxySelector().select(paramHttpUrl.uri());
      if (list != null && !list.isEmpty()) {
        list = Util.immutableList(list);
      } else {
        list = Util.immutableList((Object[])new Proxy[] { Proxy.NO_PROXY });
      } 
      this.proxies = list;
    } 
    this.nextProxyIndex = 0;
  }
  
  public void connectFailed(Route paramRoute, IOException paramIOException) {
    if (paramRoute.proxy().type() != Proxy.Type.DIRECT && this.address.proxySelector() != null)
      this.address.proxySelector().connectFailed(this.address.url().uri(), paramRoute.proxy().address(), paramIOException); 
    this.routeDatabase.failed(paramRoute);
  }
  
  public boolean hasNext() {
    return (hasNextInetSocketAddress() || hasNextProxy() || hasNextPostponed());
  }
  
  public Route next() throws IOException {
    if (!hasNextInetSocketAddress()) {
      if (!hasNextProxy()) {
        if (!hasNextPostponed())
          throw new NoSuchElementException(); 
        return nextPostponed();
      } 
      this.lastProxy = nextProxy();
    } 
    this.lastInetSocketAddress = nextInetSocketAddress();
    Route route2 = new Route(this.address, this.lastProxy, this.lastInetSocketAddress);
    Route route1 = route2;
    if (this.routeDatabase.shouldPostpone(route2)) {
      this.postponedRoutes.add(route2);
      route1 = next();
    } 
    return route1;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/connection/RouteSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */