package com.tencent.bugly.proguard;

public final class aj extends k implements Cloneable {
  private static byte[] d;
  
  private byte a = (byte)0;
  
  private String b = "";
  
  private byte[] c = null;
  
  public aj() {}
  
  public aj(byte paramByte, String paramString, byte[] paramArrayOfbyte) {
    this.a = (byte)paramByte;
    this.b = paramString;
    this.c = paramArrayOfbyte;
  }
  
  public final void a(i parami) {
    this.a = parami.a(this.a, 0, true);
    this.b = parami.b(1, true);
    if (d == null) {
      byte[] arrayOfByte1 = new byte[1];
      d = arrayOfByte1;
      arrayOfByte1[0] = (byte)0;
    } 
    byte[] arrayOfByte = d;
    this.c = parami.c(2, false);
  }
  
  public final void a(j paramj) {
    paramj.a(this.a, 0);
    paramj.a(this.b, 1);
    if (this.c != null)
      paramj.a(this.c, 2); 
  }
  
  public final void a(StringBuilder paramStringBuilder, int paramInt) {}
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/aj.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */