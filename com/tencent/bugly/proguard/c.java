package com.tencent.bugly.proguard;

import java.util.HashMap;

public class c extends a {
  protected HashMap<String, byte[]> d = null;
  
  private HashMap<String, Object> e = new HashMap<String, Object>();
  
  private i f = new i();
  
  public <T> void a(String paramString, T paramT) {
    byte[] arrayOfByte;
    if (this.d != null) {
      if (paramString == null)
        throw new IllegalArgumentException("put key can not is null"); 
      if (paramT == null)
        throw new IllegalArgumentException("put value can not is null"); 
      if (paramT instanceof java.util.Set)
        throw new IllegalArgumentException("can not support Set"); 
      j j = new j();
      j.a(this.b);
      j.a(paramT, 0);
      arrayOfByte = l.a(j.a());
      this.d.put(paramString, arrayOfByte);
      return;
    } 
    super.a(paramString, arrayOfByte);
  }
  
  public void a(byte[] paramArrayOfbyte) {
    try {
      super.a(paramArrayOfbyte);
    } catch (Exception exception) {
      this.f.a(paramArrayOfbyte);
      this.f.a(this.b);
      HashMap<Object, Object> hashMap = new HashMap<Object, Object>(1);
      hashMap.put("", new byte[0]);
      this.d = this.f.a(hashMap, 0, false);
    } 
  }
  
  public byte[] a() {
    if (this.d != null) {
      j j = new j(0);
      j.a(this.b);
      j.a(this.d, 0);
      return l.a(j.a());
    } 
    return super.a();
  }
  
  public final <T> T b(String paramString, T paramT) throws b {
    Object object = null;
    if (this.d != null) {
      if (this.d.containsKey(paramString)) {
        if (this.e.containsKey(paramString))
          return (T)this.e.get(paramString); 
        object = this.d.get(paramString);
        try {
          this.f.a((byte[])object);
          this.f.a(this.b);
          paramT = this.f.a(paramT, 0, true);
          object = paramT;
          if (paramT != null) {
            this.e.put(paramString, paramT);
            object = paramT;
          } 
        } catch (Exception exception) {
          throw new b(exception);
        } 
      } 
      return (T)object;
    } 
    if (this.a.containsKey(exception)) {
      if (this.e.containsKey(exception))
        return (T)this.e.get(exception); 
      object = ((HashMap)this.a.get(exception)).entrySet().iterator();
      if (object.hasNext()) {
        object = object.next();
        object.getKey();
        object = object.getValue();
      } else {
        object = new byte[0];
      } 
      try {
        this.f.a((byte[])object);
        this.f.a(this.b);
        object = this.f.a(paramT, 0, true);
        this.e.put(exception, object);
      } catch (Exception exception1) {
        throw new b(exception1);
      } 
    } 
    return (T)object;
  }
  
  public void b() {
    this.d = (HashMap)new HashMap<String, byte>();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/c.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */