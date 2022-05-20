package io.xlink.wifi.sdk.encoder;

import io.xlink.wifi.sdk.XDevice;
import io.xlink.wifi.sdk.buffer.b;
import io.xlink.wifi.sdk.global.a;
import io.xlink.wifi.sdk.util.b;
import java.net.InetAddress;

public class d {
  public boolean a = false;
  
  public b b;
  
  private int c = -1;
  
  private int d;
  
  private int e;
  
  private String f;
  
  private XDevice g;
  
  private int h = 2;
  
  private boolean i;
  
  private int j;
  
  private InetAddress k;
  
  private int l;
  
  public d(int paramInt1, int paramInt2, boolean paramBoolean) {
    this.b = new b(paramInt1 + 5);
    this.c = paramInt1 + 5;
    a(paramInt2, paramInt1, paramBoolean);
  }
  
  public d(b paramb) {
    this.b = paramb;
  }
  
  private void a(int paramInt1, int paramInt2, boolean paramBoolean) {
    this.e = paramInt1;
    this.i = paramBoolean;
    this.j = paramInt2;
    if (paramBoolean) {
      this.b.a(b.a(paramInt1, 1));
    } else {
      this.b.a(b.a(paramInt1, 0));
    } 
    byte[] arrayOfByte = new byte[4];
    this.b.a(b.a(paramInt2, arrayOfByte));
  }
  
  public int a() {
    return this.h;
  }
  
  public void a(int paramInt) {
    this.d = paramInt;
  }
  
  public void a(XDevice paramXDevice) {
    this.g = paramXDevice;
  }
  
  public void a(String paramString) {
    this.f = paramString;
  }
  
  public void a(InetAddress paramInetAddress) {
    this.k = paramInetAddress;
    this.h = 1;
  }
  
  public XDevice b() {
    return this.g;
  }
  
  public int c() {
    if (this.d == 0)
      this.d = a.f; 
    return this.d;
  }
  
  public InetAddress d() {
    return this.k;
  }
  
  public int e() {
    return this.e;
  }
  
  public String f() {
    return this.f + "";
  }
  
  public int g() {
    return this.l;
  }
  
  public void h() {
    short s = (short)b.c();
    this.b.a(s);
    this.f = s + "";
    this.l = Math.abs(s);
  }
  
  public String toString() {
    StringBuffer stringBuffer = new StringBuffer();
    if (this.k != null) {
      stringBuffer.append("send address：");
      stringBuffer.append(this.k.getHostAddress());
    } 
    stringBuffer.append("type ：");
    stringBuffer.append(this.e);
    stringBuffer.append(" isResponse：");
    stringBuffer.append(this.i);
    stringBuffer.append(" dataLength：");
    stringBuffer.append(this.j);
    stringBuffer.append(" msgId：");
    stringBuffer.append(f());
    stringBuffer.append("size:");
    stringBuffer.append(this.c);
    return stringBuffer.toString();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/encoder/d.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */