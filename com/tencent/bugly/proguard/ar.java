package com.tencent.bugly.proguard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class ar extends k implements Cloneable {
  private static ArrayList<aq> f;
  
  private static Map<String, String> g;
  
  public byte a = (byte)0;
  
  public String b = "";
  
  public String c = "";
  
  public ArrayList<aq> d = null;
  
  public Map<String, String> e = null;
  
  public final void a(i parami) {
    this.a = parami.a(this.a, 0, true);
    this.b = parami.b(1, false);
    this.c = parami.b(2, false);
    if (f == null) {
      f = new ArrayList<aq>();
      aq aq = new aq();
      f.add(aq);
    } 
    this.d = (ArrayList<aq>)parami.<ArrayList<aq>>a(f, 3, false);
    if (g == null) {
      g = new HashMap<String, String>();
      g.put("", "");
    } 
    this.e = (Map<String, String>)parami.<Map<String, String>>a(g, 4, false);
  }
  
  public final void a(j paramj) {
    paramj.a(this.a, 0);
    if (this.b != null)
      paramj.a(this.b, 1); 
    if (this.c != null)
      paramj.a(this.c, 2); 
    if (this.d != null)
      paramj.a(this.d, 3); 
    if (this.e != null)
      paramj.a(this.e, 4); 
  }
  
  public final void a(StringBuilder paramStringBuilder, int paramInt) {}
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/ar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */