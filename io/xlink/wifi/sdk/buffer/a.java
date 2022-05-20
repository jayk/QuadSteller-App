package io.xlink.wifi.sdk.buffer;

import io.xlink.wifi.sdk.util.b;

public class a {
  private int a;
  
  private byte[] b;
  
  public a(byte[] paramArrayOfbyte, int paramInt) {
    this.b = paramArrayOfbyte;
    this.a = paramInt;
  }
  
  private void b(int paramInt) {
    this.a += paramInt;
    if (this.b.length == this.a);
  }
  
  public void a() {
    this.b = null;
    this.a = 0;
  }
  
  public byte[] a(int paramInt) {
    byte[] arrayOfByte = new byte[paramInt];
    System.arraycopy(this.b, this.a, arrayOfByte, 0, arrayOfByte.length);
    b(paramInt);
    return arrayOfByte;
  }
  
  public byte[] b() {
    return this.b;
  }
  
  public boolean c() {
    boolean bool = true;
    byte b = this.b[this.a];
    b(1);
    if (b == 0)
      bool = false; 
    return bool;
  }
  
  public byte d() {
    byte b = this.b[this.a];
    b(1);
    return b;
  }
  
  public byte[] e() {
    byte[] arrayOfByte = new byte[this.b.length - this.a];
    System.arraycopy(this.b, this.a, arrayOfByte, 0, arrayOfByte.length);
    this.a += arrayOfByte.length;
    a();
    return arrayOfByte;
  }
  
  public int f() {
    byte[] arrayOfByte = new byte[4];
    System.arraycopy(this.b, this.a, arrayOfByte, 0, arrayOfByte.length);
    b(arrayOfByte.length);
    return b.b(arrayOfByte);
  }
  
  public short g() {
    short s = b.a(this.b, this.a);
    b(2);
    return s;
  }
  
  public String toString() {
    return "当前索引: " + this.a + " 数据总长度 ：" + this.b.length + " " + this.b;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/buffer/a.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */