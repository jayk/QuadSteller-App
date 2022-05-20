package io.xlink.wifi.sdk.decoder;

public class b {
  private int a = -1;
  
  private boolean b = false;
  
  private byte c;
  
  private int d = -1;
  
  private byte[] e;
  
  public b(byte[] paramArrayOfbyte) {
    this.e = paramArrayOfbyte;
    this.c = (byte)paramArrayOfbyte[0];
    this.a = a(this.c);
    this.b = b(this.c);
    this.d = a(paramArrayOfbyte);
  }
  
  private int a(byte paramByte) {
    return 0x0 | paramByte >> 4 & 0xF;
  }
  
  private int a(byte[] paramArrayOfbyte) {
    return 0x0 | paramArrayOfbyte[4] & 0xFF | (paramArrayOfbyte[3] & 0xFF) << 8 | (paramArrayOfbyte[2] & 0xFF) << 16 | (paramArrayOfbyte[1] & 0xFF) << 24;
  }
  
  private boolean b(byte paramByte) {
    boolean bool = true;
    if ((paramByte >> 3 & 0x1 | 0x0) != 1)
      bool = false; 
    return bool;
  }
  
  public int a() {
    return this.d;
  }
  
  public boolean b() {
    return (this.a > 0 && this.a < 16);
  }
  
  public int c() {
    return this.a;
  }
  
  public boolean d() {
    return this.b;
  }
  
  public String toString() {
    String str1 = io.xlink.wifi.sdk.util.b.b(this.c);
    int i = this.a;
    if (this.b) {
      String str = "true";
      return String.format("Fix message header %s; type: %d; response: %s;", new Object[] { str1, Integer.valueOf(i), str });
    } 
    String str2 = "false";
    return String.format("Fix message header %s; type: %d; response: %s;", new Object[] { str1, Integer.valueOf(i), str2 });
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/decoder/b.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */