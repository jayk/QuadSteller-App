package io.xlink.wifi.sdk.buffer;

public class b {
  private byte[] a;
  
  private int b;
  
  public b(int paramInt) {
    this.a = new byte[paramInt];
    this.b = 0;
  }
  
  public void a(byte paramByte) {
    this.a[this.b] = (byte)paramByte;
    this.b++;
  }
  
  public void a(int paramInt) {
    a((short)paramInt);
  }
  
  public void a(short paramShort) {
    a(io.xlink.wifi.sdk.util.b.a(paramShort));
  }
  
  public void a(boolean paramBoolean) {
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (paramBoolean) {
      bool1 = true;
      bool2 = bool1;
    } 
    a(bool2);
  }
  
  public void a(byte[] paramArrayOfbyte) {
    int i = paramArrayOfbyte.length;
    System.arraycopy(paramArrayOfbyte, 0, this.a, this.b, i);
    this.b = i + this.b;
  }
  
  public byte[] a() {
    return this.a;
  }
  
  public void b(int paramInt) {
    a(io.xlink.wifi.sdk.util.b.b(paramInt));
  }
  
  public void c(int paramInt) {
    a((byte)paramInt);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/buffer/b.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */