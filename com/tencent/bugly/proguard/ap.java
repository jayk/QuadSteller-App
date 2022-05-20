package com.tencent.bugly.proguard;

import java.util.HashMap;
import java.util.Map;

public final class ap extends k implements Cloneable {
  private static ao m;
  
  private static Map<String, String> n;
  
  public boolean a = true;
  
  public boolean b = true;
  
  public boolean c = true;
  
  public String d = "";
  
  public String e = "";
  
  public ao f = null;
  
  public Map<String, String> g = null;
  
  public long h = 0L;
  
  public int i = 0;
  
  private String j = "";
  
  private String k = "";
  
  private int l = 0;
  
  static {
    boolean bool;
    if (!ap.class.desiredAssertionStatus()) {
      bool = true;
    } else {
      bool = false;
    } 
    o = bool;
    m = new ao();
    n = new HashMap<String, String>();
    n.put("", "");
  }
  
  public final void a(i parami) {
    boolean bool = this.a;
    this.a = parami.a(0, true);
    bool = this.b;
    this.b = parami.a(1, true);
    bool = this.c;
    this.c = parami.a(2, true);
    this.d = parami.b(3, false);
    this.e = parami.b(4, false);
    this.f = (ao)parami.a(m, 5, false);
    this.g = (Map<String, String>)parami.<Map<String, String>>a(n, 6, false);
    this.h = parami.a(this.h, 7, false);
    this.j = parami.b(8, false);
    this.k = parami.b(9, false);
    this.l = parami.a(this.l, 10, false);
    this.i = parami.a(this.i, 11, false);
  }
  
  public final void a(j paramj) {
    paramj.a(this.a, 0);
    paramj.a(this.b, 1);
    paramj.a(this.c, 2);
    if (this.d != null)
      paramj.a(this.d, 3); 
    if (this.e != null)
      paramj.a(this.e, 4); 
    if (this.f != null)
      paramj.a(this.f, 5); 
    if (this.g != null)
      paramj.a(this.g, 6); 
    paramj.a(this.h, 7);
    if (this.j != null)
      paramj.a(this.j, 8); 
    if (this.k != null)
      paramj.a(this.k, 9); 
    paramj.a(this.l, 10);
    paramj.a(this.i, 11);
  }
  
  public final void a(StringBuilder paramStringBuilder, int paramInt) {
    h h = new h(paramStringBuilder, paramInt);
    h.a(this.a, "enable");
    h.a(this.b, "enableUserInfo");
    h.a(this.c, "enableQuery");
    h.a(this.d, "url");
    h.a(this.e, "expUrl");
    h.a(this.f, "security");
    h.a(this.g, "valueMap");
    h.a(this.h, "strategylastUpdateTime");
    h.a(this.j, "httpsUrl");
    h.a(this.k, "httpsExpUrl");
    h.a(this.l, "eventRecordCount");
    h.a(this.i, "eventTimeInterval");
  }
  
  public final Object clone() {
    Object object = null;
    try {
      Object object1 = super.clone();
      object = object1;
    } catch (CloneNotSupportedException cloneNotSupportedException) {}
    return object;
  }
  
  public final boolean equals(Object paramObject) {
    boolean bool1 = false;
    if (paramObject == null)
      return bool1; 
    paramObject = paramObject;
    boolean bool2 = bool1;
    if (l.a(this.a, ((ap)paramObject).a)) {
      bool2 = bool1;
      if (l.a(this.b, ((ap)paramObject).b)) {
        bool2 = bool1;
        if (l.a(this.c, ((ap)paramObject).c)) {
          bool2 = bool1;
          if (l.a(this.d, ((ap)paramObject).d)) {
            bool2 = bool1;
            if (l.a(this.e, ((ap)paramObject).e)) {
              bool2 = bool1;
              if (l.a(this.f, ((ap)paramObject).f)) {
                bool2 = bool1;
                if (l.a(this.g, ((ap)paramObject).g)) {
                  bool2 = bool1;
                  if (l.a(this.h, ((ap)paramObject).h)) {
                    bool2 = bool1;
                    if (l.a(this.j, ((ap)paramObject).j)) {
                      bool2 = bool1;
                      if (l.a(this.k, ((ap)paramObject).k)) {
                        bool2 = bool1;
                        if (l.a(this.l, ((ap)paramObject).l)) {
                          bool2 = bool1;
                          if (l.a(this.i, ((ap)paramObject).i))
                            bool2 = true; 
                        } 
                      } 
                    } 
                  } 
                } 
              } 
            } 
          } 
        } 
      } 
    } 
    return bool2;
  }
  
  public final int hashCode() {
    try {
      Exception exception = new Exception();
      this("Need define key first!");
      throw exception;
    } catch (Exception exception) {
      exception.printStackTrace();
      return 0;
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/ap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */