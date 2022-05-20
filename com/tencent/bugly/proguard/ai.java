package com.tencent.bugly.proguard;

import java.util.ArrayList;

public final class ai extends k implements Cloneable {
  private static ArrayList<String> c;
  
  private String a = "";
  
  private ArrayList<String> b = null;
  
  public final void a(i parami) {
    this.a = parami.b(0, true);
    if (c == null) {
      c = new ArrayList<String>();
      c.add("");
    } 
    this.b = (ArrayList<String>)parami.<ArrayList<String>>a(c, 1, false);
  }
  
  public final void a(j paramj) {
    paramj.a(this.a, 0);
    if (this.b != null)
      paramj.a(this.b, 1); 
  }
  
  public final void a(StringBuilder paramStringBuilder, int paramInt) {}
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/ai.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */