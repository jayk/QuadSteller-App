package com.tencent.bugly.proguard;

import java.util.List;
import java.util.Map;

public final class h {
  private StringBuilder a;
  
  private int b = 0;
  
  public h(StringBuilder paramStringBuilder, int paramInt) {
    this.a = paramStringBuilder;
    this.b = paramInt;
  }
  
  private <T> h a(T paramT, String paramString) {
    StringBuilder stringBuilder;
    List list;
    short[] arrayOfShort;
    int[] arrayOfInt;
    long[] arrayOfLong;
    float[] arrayOfFloat;
    double[] arrayOfDouble;
    h h1;
    int i = 0;
    int j = 0;
    boolean bool1 = false;
    boolean bool2 = false;
    int k = 0;
    if (paramT == null) {
      this.a.append("null\n");
      return this;
    } 
    if (paramT instanceof Byte) {
      k = ((Byte)paramT).byteValue();
      a(paramString);
      this.a.append(k).append('\n');
      return this;
    } 
    if (paramT instanceof Boolean) {
      int m;
      boolean bool = ((Boolean)paramT).booleanValue();
      a(paramString);
      stringBuilder = this.a;
      if (bool) {
        k = 84;
        m = k;
      } else {
        k = 70;
        m = k;
      } 
      stringBuilder.append(m).append('\n');
      return this;
    } 
    if (stringBuilder instanceof Short) {
      k = ((Short)stringBuilder).shortValue();
      a(paramString);
      this.a.append(k).append('\n');
      return this;
    } 
    if (stringBuilder instanceof Integer) {
      k = ((Integer)stringBuilder).intValue();
      a(paramString);
      this.a.append(k).append('\n');
      return this;
    } 
    if (stringBuilder instanceof Long) {
      long l = ((Long)stringBuilder).longValue();
      a(paramString);
      this.a.append(l).append('\n');
      return this;
    } 
    if (stringBuilder instanceof Float) {
      float f = ((Float)stringBuilder).floatValue();
      a(paramString);
      this.a.append(f).append('\n');
      return this;
    } 
    if (stringBuilder instanceof Double) {
      double d = ((Double)stringBuilder).doubleValue();
      a(paramString);
      this.a.append(d).append('\n');
      return this;
    } 
    if (stringBuilder instanceof String) {
      a((String)stringBuilder, paramString);
      return this;
    } 
    if (stringBuilder instanceof Map) {
      a((Map<?, ?>)stringBuilder, paramString);
      return this;
    } 
    if (stringBuilder instanceof List) {
      list = (List)stringBuilder;
      if (list == null) {
        a(paramString);
        this.a.append("null\t");
        return this;
      } 
      a(list.toArray(), paramString);
      return this;
    } 
    if (list instanceof k) {
      a((k)list, paramString);
      return this;
    } 
    if (list instanceof byte[]) {
      a((byte[])list, paramString);
      return this;
    } 
    if (list instanceof boolean[]) {
      a((boolean[])list, paramString);
      return this;
    } 
    if (list instanceof short[]) {
      arrayOfShort = (short[])list;
      a(paramString);
      if (arrayOfShort == null) {
        this.a.append("null\n");
        return this;
      } 
      if (arrayOfShort.length == 0) {
        this.a.append(arrayOfShort.length).append(", []\n");
        return this;
      } 
      this.a.append(arrayOfShort.length).append(", [\n");
      h1 = new h(this.a, this.b + 1);
      i = arrayOfShort.length;
      while (k < i) {
        j = arrayOfShort[k];
        h1.a(null);
        h1.a.append(j).append('\n');
        k++;
      } 
      a(null);
      this.a.append(']').append('\n');
      return this;
    } 
    if (arrayOfShort instanceof int[]) {
      arrayOfInt = (int[])arrayOfShort;
      a((String)h1);
      if (arrayOfInt == null) {
        this.a.append("null\n");
        return this;
      } 
      if (arrayOfInt.length == 0) {
        this.a.append(arrayOfInt.length).append(", []\n");
        return this;
      } 
      this.a.append(arrayOfInt.length).append(", [\n");
      h1 = new h(this.a, this.b + 1);
      j = arrayOfInt.length;
      for (k = i; k < j; k++) {
        i = arrayOfInt[k];
        h1.a(null);
        h1.a.append(i).append('\n');
      } 
      a(null);
      this.a.append(']').append('\n');
      return this;
    } 
    if (arrayOfInt instanceof long[]) {
      arrayOfLong = (long[])arrayOfInt;
      a((String)h1);
      if (arrayOfLong == null) {
        this.a.append("null\n");
        return this;
      } 
      if (arrayOfLong.length == 0) {
        this.a.append(arrayOfLong.length).append(", []\n");
        return this;
      } 
      this.a.append(arrayOfLong.length).append(", [\n");
      h1 = new h(this.a, this.b + 1);
      i = arrayOfLong.length;
      for (k = j; k < i; k++) {
        long l = arrayOfLong[k];
        h1.a(null);
        h1.a.append(l).append('\n');
      } 
      a(null);
      this.a.append(']').append('\n');
      return this;
    } 
    if (arrayOfLong instanceof float[]) {
      arrayOfFloat = (float[])arrayOfLong;
      a((String)h1);
      if (arrayOfFloat == null) {
        this.a.append("null\n");
        return this;
      } 
      if (arrayOfFloat.length == 0) {
        this.a.append(arrayOfFloat.length).append(", []\n");
        return this;
      } 
      this.a.append(arrayOfFloat.length).append(", [\n");
      h1 = new h(this.a, this.b + 1);
      i = arrayOfFloat.length;
      for (k = bool1; k < i; k++) {
        float f = arrayOfFloat[k];
        h1.a(null);
        h1.a.append(f).append('\n');
      } 
      a(null);
      this.a.append(']').append('\n');
      return this;
    } 
    if (arrayOfFloat instanceof double[]) {
      arrayOfDouble = (double[])arrayOfFloat;
      a((String)h1);
      if (arrayOfDouble == null) {
        this.a.append("null\n");
        return this;
      } 
      if (arrayOfDouble.length == 0) {
        this.a.append(arrayOfDouble.length).append(", []\n");
        return this;
      } 
      this.a.append(arrayOfDouble.length).append(", [\n");
      h1 = new h(this.a, this.b + 1);
      i = arrayOfDouble.length;
      for (k = bool2; k < i; k++) {
        double d = arrayOfDouble[k];
        h1.a(null);
        h1.a.append(d).append('\n');
      } 
      a(null);
      this.a.append(']').append('\n');
      return this;
    } 
    if (arrayOfDouble.getClass().isArray()) {
      a((Object[])arrayOfDouble, (String)h1);
      return this;
    } 
    throw new b("write object error: unsupport type.");
  }
  
  private <T> h a(T[] paramArrayOfT, String paramString) {
    a(paramString);
    if (paramArrayOfT == null) {
      this.a.append("null\n");
      return this;
    } 
    if (paramArrayOfT.length == 0) {
      this.a.append(paramArrayOfT.length).append(", []\n");
      return this;
    } 
    this.a.append(paramArrayOfT.length).append(", [\n");
    h h1 = new h(this.a, this.b + 1);
    int i = paramArrayOfT.length;
    for (byte b = 0; b < i; b++)
      h1.a(paramArrayOfT[b], (String)null); 
    a(null);
    this.a.append(']').append('\n');
    return this;
  }
  
  private void a(String paramString) {
    for (byte b = 0; b < this.b; b++)
      this.a.append('\t'); 
    if (paramString != null)
      this.a.append(paramString).append(": "); 
  }
  
  public final h a(byte paramByte, String paramString) {
    a(paramString);
    this.a.append(paramByte).append('\n');
    return this;
  }
  
  public final h a(int paramInt, String paramString) {
    a(paramString);
    this.a.append(paramInt).append('\n');
    return this;
  }
  
  public final h a(long paramLong, String paramString) {
    a(paramString);
    this.a.append(paramLong).append('\n');
    return this;
  }
  
  public final h a(k paramk, String paramString) {
    a(paramString);
    this.a.append('{').append('\n');
    if (paramk == null) {
      this.a.append('\t').append("null");
      a(null);
      this.a.append('}').append('\n');
      return this;
    } 
    paramk.a(this.a, this.b + 1);
    a(null);
    this.a.append('}').append('\n');
    return this;
  }
  
  public final h a(String paramString1, String paramString2) {
    a(paramString2);
    if (paramString1 == null) {
      this.a.append("null\n");
      return this;
    } 
    this.a.append(paramString1).append('\n');
    return this;
  }
  
  public final <K, V> h a(Map<K, V> paramMap, String paramString) {
    a(paramString);
    if (paramMap == null) {
      this.a.append("null\n");
      return this;
    } 
    if (paramMap.isEmpty()) {
      this.a.append(paramMap.size()).append(", {}\n");
      return this;
    } 
    this.a.append(paramMap.size()).append(", {\n");
    h h2 = new h(this.a, this.b + 1);
    h h1 = new h(this.a, this.b + 2);
    for (Map.Entry<K, V> entry : paramMap.entrySet()) {
      h2.a(null);
      h2.a.append('(').append('\n');
      h1.a(entry.getKey(), (String)null);
      h1.a(entry.getValue(), (String)null);
      h2.a(null);
      h2.a.append(')').append('\n');
    } 
    a(null);
    this.a.append('}').append('\n');
    return this;
  }
  
  public final h a(short paramShort, String paramString) {
    a(paramString);
    this.a.append(paramShort).append('\n');
    return this;
  }
  
  public final h a(boolean paramBoolean, String paramString) {
    a(paramString);
    StringBuilder stringBuilder = this.a;
    if (paramBoolean) {
      byte b3 = 84;
      byte b4 = b3;
      stringBuilder.append(b4).append('\n');
      return this;
    } 
    byte b1 = 70;
    byte b2 = b1;
    stringBuilder.append(b2).append('\n');
    return this;
  }
  
  public final h a(byte[] paramArrayOfbyte, String paramString) {
    a(paramString);
    if (paramArrayOfbyte == null) {
      this.a.append("null\n");
      return this;
    } 
    if (paramArrayOfbyte.length == 0) {
      this.a.append(paramArrayOfbyte.length).append(", []\n");
      return this;
    } 
    this.a.append(paramArrayOfbyte.length).append(", [\n");
    h h1 = new h(this.a, this.b + 1);
    int i = paramArrayOfbyte.length;
    for (byte b = 0; b < i; b++) {
      byte b1 = paramArrayOfbyte[b];
      h1.a(null);
      h1.a.append(b1).append('\n');
    } 
    a(null);
    this.a.append(']').append('\n');
    return this;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/h.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */