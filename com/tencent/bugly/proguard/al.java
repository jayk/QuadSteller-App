package com.tencent.bugly.proguard;

import java.util.ArrayList;

public final class al extends k implements Cloneable {
  private static ArrayList<ak> b;
  
  public ArrayList<ak> a = null;
  
  public final void a(i parami) {
    if (b == null) {
      b = new ArrayList<ak>();
      ak ak = new ak();
      b.add(ak);
    } 
    this.a = (ArrayList<ak>)parami.<ArrayList<ak>>a(b, 0, true);
  }
  
  public final void a(j paramj) {
    paramj.a(this.a, 0);
  }
  
  public final void a(StringBuilder paramStringBuilder, int paramInt) {}
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/al.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */