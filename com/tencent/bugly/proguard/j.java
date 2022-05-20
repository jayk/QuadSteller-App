package com.tencent.bugly.proguard;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class j {
  private ByteBuffer a;
  
  private String b = "GBK";
  
  public j() {
    this(128);
  }
  
  public j(int paramInt) {
    this.a = ByteBuffer.allocate(paramInt);
  }
  
  private void a(int paramInt) {
    if (this.a.remaining() < paramInt) {
      ByteBuffer byteBuffer = ByteBuffer.allocate(this.a.capacity() + paramInt << 1);
      byteBuffer.put(this.a.array(), 0, this.a.position());
      this.a = byteBuffer;
    } 
  }
  
  private void b(byte paramByte, int paramInt) {
    if (paramInt < 15) {
      byte b = (byte)(paramInt << 4 | paramByte);
      this.a.put(b);
      return;
    } 
    if (paramInt < 256) {
      byte b = (byte)(paramByte | 0xF0);
      this.a.put(b);
      this.a.put((byte)paramInt);
      return;
    } 
    throw new b("tag is too large: " + paramInt);
  }
  
  public final int a(String paramString) {
    this.b = paramString;
    return 0;
  }
  
  public final ByteBuffer a() {
    return this.a;
  }
  
  public final void a(byte paramByte, int paramInt) {
    a(3);
    if (paramByte == 0) {
      b((byte)12, paramInt);
      return;
    } 
    b((byte)0, paramInt);
    this.a.put(paramByte);
  }
  
  public final void a(int paramInt1, int paramInt2) {
    a(6);
    if (paramInt1 >= -32768 && paramInt1 <= 32767) {
      a((short)paramInt1, paramInt2);
      return;
    } 
    b((byte)2, paramInt2);
    this.a.putInt(paramInt1);
  }
  
  public final void a(long paramLong, int paramInt) {
    a(10);
    if (paramLong >= -2147483648L && paramLong <= 2147483647L) {
      a((int)paramLong, paramInt);
      return;
    } 
    b((byte)3, paramInt);
    this.a.putLong(paramLong);
  }
  
  public final void a(k paramk, int paramInt) {
    a(2);
    b((byte)10, paramInt);
    paramk.a(this);
    a(2);
    b((byte)11, 0);
  }
  
  public final void a(Object paramObject, int paramInt) {
    int i = 1;
    if (paramObject instanceof Byte) {
      a(((Byte)paramObject).byteValue(), paramInt);
      return;
    } 
    if (paramObject instanceof Boolean) {
      if (!((Boolean)paramObject).booleanValue())
        i = 0; 
      a((byte)i, paramInt);
      return;
    } 
    if (paramObject instanceof Short) {
      a(((Short)paramObject).shortValue(), paramInt);
      return;
    } 
    if (paramObject instanceof Integer) {
      a(((Integer)paramObject).intValue(), paramInt);
      return;
    } 
    if (paramObject instanceof Long) {
      a(((Long)paramObject).longValue(), paramInt);
      return;
    } 
    if (paramObject instanceof Float) {
      float f = ((Float)paramObject).floatValue();
      a(6);
      b((byte)4, paramInt);
      this.a.putFloat(f);
      return;
    } 
    if (paramObject instanceof Double) {
      double d = ((Double)paramObject).doubleValue();
      a(10);
      b((byte)5, paramInt);
      this.a.putDouble(d);
      return;
    } 
    if (paramObject instanceof String) {
      a((String)paramObject, paramInt);
      return;
    } 
    if (paramObject instanceof Map) {
      a((Map<?, ?>)paramObject, paramInt);
      return;
    } 
    if (paramObject instanceof List) {
      a((List)paramObject, paramInt);
      return;
    } 
    if (paramObject instanceof k) {
      paramObject = paramObject;
      a(2);
      b((byte)10, paramInt);
      paramObject.a(this);
      a(2);
      b((byte)11, 0);
      return;
    } 
    if (paramObject instanceof byte[]) {
      a((byte[])paramObject, paramInt);
      return;
    } 
    if (paramObject instanceof boolean[]) {
      paramObject = paramObject;
      a(8);
      b((byte)9, paramInt);
      a(paramObject.length, 0);
      int k = paramObject.length;
      paramInt = 0;
      while (true) {
        if (paramInt < k) {
          if (paramObject[paramInt] != null) {
            i = 1;
          } else {
            i = 0;
          } 
          a((byte)i, 0);
          paramInt++;
          continue;
        } 
        return;
      } 
    } 
    if (paramObject instanceof short[]) {
      paramObject = paramObject;
      a(8);
      b((byte)9, paramInt);
      a(paramObject.length, 0);
      i = paramObject.length;
      paramInt = 0;
      while (true) {
        if (paramInt < i) {
          a(paramObject[paramInt], 0);
          paramInt++;
          continue;
        } 
        return;
      } 
    } 
    if (paramObject instanceof int[]) {
      paramObject = paramObject;
      a(8);
      b((byte)9, paramInt);
      a(paramObject.length, 0);
      i = paramObject.length;
      paramInt = 0;
      while (true) {
        if (paramInt < i) {
          a(paramObject[paramInt], 0);
          paramInt++;
          continue;
        } 
        return;
      } 
    } 
    if (paramObject instanceof long[]) {
      paramObject = paramObject;
      a(8);
      b((byte)9, paramInt);
      a(paramObject.length, 0);
      i = paramObject.length;
      paramInt = 0;
      while (true) {
        if (paramInt < i) {
          a(paramObject[paramInt], 0);
          paramInt++;
          continue;
        } 
        return;
      } 
    } 
    if (paramObject instanceof float[]) {
      paramObject = paramObject;
      a(8);
      b((byte)9, paramInt);
      a(paramObject.length, 0);
      i = paramObject.length;
      paramInt = 0;
      while (true) {
        if (paramInt < i) {
          Object object = paramObject[paramInt];
          a(6);
          b((byte)4, 0);
          this.a.putFloat(object);
          paramInt++;
          continue;
        } 
        return;
      } 
    } 
    if (paramObject instanceof double[]) {
      paramObject = paramObject;
      a(8);
      b((byte)9, paramInt);
      a(paramObject.length, 0);
      i = paramObject.length;
      paramInt = 0;
      while (true) {
        if (paramInt < i) {
          Object object = paramObject[paramInt];
          a(10);
          b((byte)5, 0);
          this.a.putDouble(object);
          paramInt++;
          continue;
        } 
        return;
      } 
    } 
    if (paramObject.getClass().isArray()) {
      paramObject = paramObject;
      a(8);
      b((byte)9, paramInt);
      a(paramObject.length, 0);
      i = paramObject.length;
      paramInt = 0;
      while (true) {
        if (paramInt < i) {
          a(paramObject[paramInt], 0);
          paramInt++;
          continue;
        } 
        return;
      } 
    } 
    if (paramObject instanceof Collection) {
      a((Collection)paramObject, paramInt);
      return;
    } 
    throw new b("write object error: unsupport type. " + paramObject.getClass());
  }
  
  public final void a(String paramString, int paramInt) {
    // Byte code:
    //   0: aload_1
    //   1: aload_0
    //   2: getfield b : Ljava/lang/String;
    //   5: invokevirtual getBytes : (Ljava/lang/String;)[B
    //   8: astore_3
    //   9: aload_3
    //   10: astore_1
    //   11: aload_0
    //   12: aload_1
    //   13: arraylength
    //   14: bipush #10
    //   16: iadd
    //   17: invokespecial a : (I)V
    //   20: aload_1
    //   21: arraylength
    //   22: sipush #255
    //   25: if_icmple -> 64
    //   28: aload_0
    //   29: bipush #7
    //   31: iload_2
    //   32: invokespecial b : (BI)V
    //   35: aload_0
    //   36: getfield a : Ljava/nio/ByteBuffer;
    //   39: aload_1
    //   40: arraylength
    //   41: invokevirtual putInt : (I)Ljava/nio/ByteBuffer;
    //   44: pop
    //   45: aload_0
    //   46: getfield a : Ljava/nio/ByteBuffer;
    //   49: aload_1
    //   50: invokevirtual put : ([B)Ljava/nio/ByteBuffer;
    //   53: pop
    //   54: return
    //   55: astore_3
    //   56: aload_1
    //   57: invokevirtual getBytes : ()[B
    //   60: astore_1
    //   61: goto -> 11
    //   64: aload_0
    //   65: bipush #6
    //   67: iload_2
    //   68: invokespecial b : (BI)V
    //   71: aload_0
    //   72: getfield a : Ljava/nio/ByteBuffer;
    //   75: aload_1
    //   76: arraylength
    //   77: i2b
    //   78: invokevirtual put : (B)Ljava/nio/ByteBuffer;
    //   81: pop
    //   82: aload_0
    //   83: getfield a : Ljava/nio/ByteBuffer;
    //   86: aload_1
    //   87: invokevirtual put : ([B)Ljava/nio/ByteBuffer;
    //   90: pop
    //   91: goto -> 54
    // Exception table:
    //   from	to	target	type
    //   0	9	55	java/io/UnsupportedEncodingException
  }
  
  public final <T> void a(Collection<T> paramCollection, int paramInt) {
    a(8);
    b((byte)9, paramInt);
    if (paramCollection == null) {
      paramInt = 0;
    } else {
      paramInt = paramCollection.size();
    } 
    a(paramInt, 0);
    if (paramCollection != null) {
      Iterator<T> iterator = paramCollection.iterator();
      while (iterator.hasNext())
        a(iterator.next(), 0); 
    } 
  }
  
  public final <K, V> void a(Map<K, V> paramMap, int paramInt) {
    a(8);
    b((byte)8, paramInt);
    if (paramMap == null) {
      paramInt = 0;
    } else {
      paramInt = paramMap.size();
    } 
    a(paramInt, 0);
    if (paramMap != null)
      for (Map.Entry<K, V> entry : paramMap.entrySet()) {
        a(entry.getKey(), 0);
        a(entry.getValue(), 1);
      }  
  }
  
  public final void a(short paramShort, int paramInt) {
    a(4);
    if (paramShort >= -128 && paramShort <= 127) {
      a((byte)paramShort, paramInt);
      return;
    } 
    b((byte)1, paramInt);
    this.a.putShort(paramShort);
  }
  
  public final void a(boolean paramBoolean, int paramInt) {
    boolean bool;
    if (paramBoolean) {
      bool = true;
    } else {
      bool = false;
    } 
    a((byte)bool, paramInt);
  }
  
  public final void a(byte[] paramArrayOfbyte, int paramInt) {
    a(paramArrayOfbyte.length + 8);
    b((byte)13, paramInt);
    b((byte)0, 0);
    a(paramArrayOfbyte.length, 0);
    this.a.put(paramArrayOfbyte);
  }
  
  public final byte[] b() {
    byte[] arrayOfByte = new byte[this.a.position()];
    System.arraycopy(this.a.array(), 0, arrayOfByte, 0, this.a.position());
    return arrayOfByte;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/j.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */