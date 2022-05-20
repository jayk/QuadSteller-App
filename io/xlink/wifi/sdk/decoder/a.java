package io.xlink.wifi.sdk.decoder;

import java.net.InetAddress;
import java.net.SocketAddress;

public class a {
  public int a;
  
  private b b;
  
  private io.xlink.wifi.sdk.buffer.a c;
  
  private InetAddress d;
  
  private SocketAddress e;
  
  public a(io.xlink.wifi.sdk.buffer.a parama, b paramb) {
    this.c = parama;
    this.b = paramb;
  }
  
  public InetAddress a() {
    return this.d;
  }
  
  public void a(InetAddress paramInetAddress) {
    this.d = paramInetAddress;
  }
  
  public void a(SocketAddress paramSocketAddress) {
    this.e = paramSocketAddress;
  }
  
  public io.xlink.wifi.sdk.buffer.a b() {
    return this.c;
  }
  
  public int c() {
    return this.b.c();
  }
  
  public b d() {
    return this.b;
  }
  
  public SocketAddress e() {
    return this.e;
  }
  
  public String toString() {
    StringBuffer stringBuffer = new StringBuffer();
    if (this.d != null) {
      stringBuffer.append("address :");
      stringBuffer.append(this.d + " port" + this.a);
    } 
    stringBuffer.append(" length : ");
    stringBuffer.append(this.b.a());
    stringBuffer.append(" type:");
    stringBuffer.append(this.b.c());
    return stringBuffer.toString();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/decoder/a.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */