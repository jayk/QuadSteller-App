package com.tencent.bugly.proguard;

import java.util.HashMap;
import java.util.Map;

public final class an extends k {
  private static byte[] i;
  
  private static Map<String, String> j = new HashMap<String, String>();
  
  public byte a = (byte)0;
  
  public int b = 0;
  
  public byte[] c = null;
  
  public String d = "";
  
  public long e = 0L;
  
  public String f = "";
  
  public Map<String, String> g = null;
  
  private String h = "";
  
  static {
    j.put("", "");
  }
  
  public final void a(i parami) {
    this.a = parami.a(this.a, 0, true);
    this.b = parami.a(this.b, 1, true);
    byte[] arrayOfByte = i;
    this.c = parami.c(2, false);
    this.d = parami.b(3, false);
    this.e = parami.a(this.e, 4, false);
    this.h = parami.b(5, false);
    this.f = parami.b(6, false);
    this.g = (Map<String, String>)parami.<Map<String, String>>a(j, 7, false);
  }
  
  public final void a(j paramj) {
    paramj.a(this.a, 0);
    paramj.a(this.b, 1);
    if (this.c != null)
      paramj.a(this.c, 2); 
    if (this.d != null)
      paramj.a(this.d, 3); 
    paramj.a(this.e, 4);
    if (this.h != null)
      paramj.a(this.h, 5); 
    if (this.f != null)
      paramj.a(this.f, 6); 
    if (this.g != null)
      paramj.a(this.g, 7); 
  }
  
  static {
    byte[] arrayOfByte = new byte[1];
    i = arrayOfByte;
    arrayOfByte[0] = (byte)0;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/an.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */