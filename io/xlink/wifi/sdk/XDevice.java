package io.xlink.wifi.sdk;

import io.xlink.wifi.sdk.event.c;
import io.xlink.wifi.sdk.global.a;
import io.xlink.wifi.sdk.udp.b;
import io.xlink.wifi.sdk.util.MyLog;
import io.xlink.wifi.sdk.util.b;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.SocketAddress;

public final class XDevice implements Serializable {
  public int a;
  
  private int b = a.h;
  
  private int c;
  
  private long d;
  
  private int e = -1;
  
  private InetAddress f;
  
  private byte g = (byte)0;
  
  private String h;
  
  private int i;
  
  private int j;
  
  private String k;
  
  private String l;
  
  private byte m = (byte)0;
  
  private int n = 0;
  
  private int o = -1;
  
  private boolean p = false;
  
  private String q;
  
  private int r = -1;
  
  private int s;
  
  private SocketAddress t;
  
  private int u = 0;
  
  private long v = 0L;
  
  private long w = 0L;
  
  protected XDevice(String paramString) {
    this.h = paramString;
    this.b = a.h;
    this.c = this.b * 1000;
  }
  
  private void c(String paramString) {
    MyLog.e("Device", paramString);
  }
  
  private boolean c() {
    null = true;
    if (this.u > 3) {
      c("device mac :" + getMacAddress() + "count>3----offline");
      return null;
    } 
    if (System.currentTimeMillis() - this.d > this.c) {
      c("mac :" + getMacAddress() + "sessionId :" + this.e + " offline");
      return null;
    } 
    return false;
  }
  
  private void d() {
    this.d = System.currentTimeMillis();
    c(0);
  }
  
  protected void a(int paramInt) {
    this.o = paramInt;
  }
  
  protected void a(String paramString) {
    this.l = paramString;
  }
  
  protected void a(InetAddress paramInetAddress) {
    this.f = paramInetAddress;
  }
  
  protected void a(boolean paramBoolean) {
    this.p = paramBoolean;
  }
  
  protected void a(byte[] paramArrayOfbyte) {
    this.h = b.d(paramArrayOfbyte);
  }
  
  protected boolean a() {
    long l1 = System.currentTimeMillis();
    long l2 = this.d;
    if (this.v == 0L)
      this.v = ((this.b / 4 - 1) * 1000); 
    if (l1 - l2 >= this.v) {
      this.d = System.currentTimeMillis();
      return true;
    } 
    return false;
  }
  
  protected void b() {
    this.u = 0;
    this.d = System.currentTimeMillis();
  }
  
  protected void b(int paramInt) {
    this.r = paramInt;
  }
  
  protected void b(String paramString) {
    this.h = paramString;
  }
  
  protected void c(int paramInt) {
    this.d = System.currentTimeMillis();
    if (this.e != paramInt) {
      this.e = paramInt;
      if (paramInt <= 0) {
        if (this.o == 0) {
          c.a(-2, this);
          a(-1);
        } 
        return;
      } 
      b();
      a(0);
    } 
  }
  
  public void checkKeepAlive() {
    if (c()) {
      d();
      return;
    } 
    if (a()) {
      b.a().a(this);
      this.u++;
    } 
  }
  
  protected void d(int paramInt) {
    this.s = paramInt;
  }
  
  protected void e(int paramInt) {
    this.i = paramInt;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof XDevice) {
      XDevice xDevice = (XDevice)paramObject;
      if (xDevice.getMacAddress() != null && getMacAddress() != null)
        return xDevice.getMacAddress().equals(getMacAddress()); 
    } 
    return super.equals(paramObject);
  }
  
  protected void f(int paramInt) {
    this.j = paramInt;
  }
  
  public int getAccessKey() {
    return this.r;
  }
  
  public InetAddress getAddress() {
    return this.f;
  }
  
  public String getAuthkey() {
    return this.q;
  }
  
  public int getDevcieConnectStates() {
    if (this.o == 0 && !isLAN())
      this.o = -1; 
    return this.o;
  }
  
  public int getDeviceId() {
    return this.j;
  }
  
  public String getDeviceName() {
    return this.k;
  }
  
  public SocketAddress getLocalAddress() {
    return this.t;
  }
  
  public String getMacAddress() {
    return this.h;
  }
  
  public byte getMcuHardVersion() {
    return this.m;
  }
  
  public int getMcuSoftVersion() {
    return this.n;
  }
  
  public int getPort() {
    return this.i;
  }
  
  public String getProductId() {
    return this.l;
  }
  
  public int getProductType() {
    return this.s;
  }
  
  public int getSessionId() {
    return this.e;
  }
  
  public int getTimeout() {
    return this.b;
  }
  
  public byte getVersion() {
    return this.g;
  }
  
  public int hashCode() {
    return (getMacAddress() != null) ? getMacAddress().hashCode() : super.hashCode();
  }
  
  public boolean isInit() {
    return this.p;
  }
  
  public boolean isLAN() {
    if (System.currentTimeMillis() - this.d > (this.b * 1000))
      c(-1); 
    return (this.e > 0);
  }
  
  public boolean isValidId() {
    return (this.j > 0);
  }
  
  public void setAuthkey(String paramString) {
    this.q = paramString;
  }
  
  public void setDeviceName(String paramString) {
    this.k = paramString;
  }
  
  public void setLocalAddress(SocketAddress paramSocketAddress) {
    this.t = paramSocketAddress;
  }
  
  public void setMcuHardVersion(byte paramByte) {
    this.m = (byte)paramByte;
  }
  
  public void setMcuSoftVersion(int paramInt) {
    this.n = paramInt;
  }
  
  public void setVersion(byte paramByte) {
    this.g = (byte)paramByte;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("MacAddress:" + getMacAddress());
    stringBuilder.append(" Did:" + getDeviceId());
    stringBuilder.append(" IP:" + getAddress());
    stringBuilder.append(" Ssid :" + getSessionId());
    return stringBuilder.toString();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/XDevice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */