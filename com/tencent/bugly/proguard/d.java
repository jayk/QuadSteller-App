package com.tencent.bugly.proguard;

import java.nio.ByteBuffer;
import java.util.HashMap;

public final class d extends c {
  private static HashMap<String, byte[]> f = null;
  
  private static HashMap<String, HashMap<String, byte[]>> g = null;
  
  private f e = new f();
  
  public d() {
    this.e.a = (short)2;
  }
  
  public final <T> void a(String paramString, T paramT) {
    if (paramString.startsWith("."))
      throw new IllegalArgumentException("put name can not startwith . , now is " + paramString); 
    super.a(paramString, paramT);
  }
  
  public final void a(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte.length < 4)
      throw new IllegalArgumentException("decode package must include size head"); 
    try {
      i i2 = new i();
      this(paramArrayOfbyte, 4);
      i2.a(this.b);
      this.e.a(i2);
      if (this.e.a == 3) {
        i2 = new i();
        this(this.e.e);
        i2.a(this.b);
        if (f == null) {
          HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
          this();
          f = (HashMap)hashMap;
          hashMap.put("", new byte[0]);
        } 
        this.d = i2.a(f, 0, false);
        return;
      } 
      i i1 = new i();
      this(this.e.e);
      i1.a(this.b);
      if (g == null) {
        HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        this();
        g = (HashMap)hashMap;
        hashMap = new HashMap<Object, Object>();
        this();
        hashMap.put("", new byte[0]);
        g.put("", hashMap);
      } 
      this.a = i1.a(g, 0, false);
      new HashMap<Object, Object>();
      return;
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    } 
  }
  
  public final byte[] a() {
    byte[] arrayOfByte2;
    if (this.e.a == 2) {
      if (this.e.c.equals(""))
        throw new IllegalArgumentException("servantName can not is null"); 
      if (this.e.d.equals(""))
        throw new IllegalArgumentException("funcName can not is null"); 
    } else {
      if (this.e.c == null)
        this.e.c = ""; 
      if (this.e.d == null)
        this.e.d = ""; 
    } 
    j j2 = new j(0);
    j2.a(this.b);
    if (this.e.a == 2) {
      j2.a(this.a, 0);
      this.e.e = l.a(j2.a());
      j2 = new j(0);
      j2.a(this.b);
      this.e.a(j2);
      arrayOfByte2 = l.a(j2.a());
      int j = arrayOfByte2.length;
      ByteBuffer byteBuffer1 = ByteBuffer.allocate(j + 4);
      byteBuffer1.putInt(j + 4).put(arrayOfByte2).flip();
      return byteBuffer1.array();
    } 
    arrayOfByte2.a(this.d, 0);
    this.e.e = l.a(arrayOfByte2.a());
    j j1 = new j(0);
    j1.a(this.b);
    this.e.a(j1);
    byte[] arrayOfByte1 = l.a(j1.a());
    int i = arrayOfByte1.length;
    ByteBuffer byteBuffer = ByteBuffer.allocate(i + 4);
    byteBuffer.putInt(i + 4).put(arrayOfByte1).flip();
    return byteBuffer.array();
  }
  
  public final void b() {
    super.b();
    this.e.a = (short)3;
  }
  
  public final void b(int paramInt) {
    this.e.b = 1;
  }
  
  public final void b(String paramString) {
    this.e.c = paramString;
  }
  
  public final void c(String paramString) {
    this.e.d = paramString;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/d.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */