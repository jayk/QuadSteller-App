package com.tencent.bugly.proguard;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public final class i {
  private ByteBuffer a;
  
  private String b = "GBK";
  
  public i() {}
  
  public i(byte[] paramArrayOfbyte) {
    this.a = ByteBuffer.wrap(paramArrayOfbyte);
  }
  
  public i(byte[] paramArrayOfbyte, int paramInt) {
    this.a = ByteBuffer.wrap(paramArrayOfbyte);
    this.a.position(4);
  }
  
  private double a(double paramDouble, int paramInt, boolean paramBoolean) {
    if (a(paramInt)) {
      a a = new a();
      a(a, this.a);
      switch (a.a) {
        default:
          throw new g("type mismatch.");
        case 12:
          return 0.0D;
        case 4:
          return this.a.getFloat();
        case 5:
          break;
      } 
      return this.a.getDouble();
    } 
    if (paramBoolean)
      throw new g("require field not exist."); 
    return paramDouble;
  }
  
  private float a(float paramFloat, int paramInt, boolean paramBoolean) {
    if (a(paramInt)) {
      a a = new a();
      a(a, this.a);
      switch (a.a) {
        default:
          throw new g("type mismatch.");
        case 12:
          return 0.0F;
        case 4:
          break;
      } 
      return this.a.getFloat();
    } 
    if (paramBoolean)
      throw new g("require field not exist."); 
    return paramFloat;
  }
  
  private static int a(a parama, ByteBuffer paramByteBuffer) {
    null = paramByteBuffer.get();
    parama.a = (byte)(byte)(null & 0xF);
    parama.b = (null & 0xF0) >> 4;
    if (parama.b == 15) {
      parama.b = paramByteBuffer.get();
      return 2;
    } 
    return 1;
  }
  
  private <K, V> Map<K, V> a(Map<K, V> paramMap1, Map<K, V> paramMap2, int paramInt, boolean paramBoolean) {
    if (paramMap2 == null || paramMap2.isEmpty())
      return new HashMap<K, V>(); 
    Map.Entry entry = paramMap2.entrySet().iterator().next();
    Object object1 = entry.getKey();
    Object object2 = entry.getValue();
    if (a(paramInt)) {
      a a = new a();
      a(a, this.a);
      switch (a.a) {
        default:
          throw new g("type mismatch.");
        case 8:
          break;
      } 
      int j = a(0, 0, true);
      if (j < 0)
        throw new g("size invalid: " + j); 
      paramInt = 0;
      while (true) {
        Map<K, V> map1 = paramMap1;
        if (paramInt < j) {
          paramMap1.put((K)a(object1, 0, true), (V)a(object2, 1, true));
          paramInt++;
          continue;
        } 
        return map1;
      } 
    } 
    Map<K, V> map = paramMap1;
    if (paramBoolean)
      throw new g("require field not exist."); 
    return map;
  }
  
  private void a() {
    a a = new a();
    do {
      a(a, this.a);
      a(a.a);
    } while (a.a != 11);
  }
  
  private void a(int paramByte) {
    int k;
    int j;
    a a;
    int m = 0;
    int n = 0;
    switch (paramByte) {
      default:
        throw new g("invalid type.");
      case 0:
        this.a.position(this.a.position() + 1);
      case 11:
      case 12:
        return;
      case 1:
        this.a.position(2 + this.a.position());
      case 2:
        this.a.position(this.a.position() + 4);
      case 3:
        this.a.position(this.a.position() + 8);
      case 4:
        this.a.position(this.a.position() + 4);
      case 5:
        this.a.position(this.a.position() + 8);
      case 6:
        n = this.a.get();
        paramByte = n;
        if (n < 0)
          k = n + 256; 
        this.a.position(k + this.a.position());
      case 7:
        k = this.a.getInt();
        this.a.position(k + this.a.position());
      case 8:
        m = a(0, 0, true);
        k = n;
        while (true) {
          if (k < m << 1) {
            a a1 = new a();
            a(a1, this.a);
            a(a1.a);
            k++;
          } 
        } 
      case 9:
        n = a(0, 0, true);
        j = m;
        while (true) {
          if (j < n) {
            a a1 = new a();
            a(a1, this.a);
            a(a1.a);
            j++;
          } 
        } 
      case 13:
        a = new a();
        a(a, this.a);
        if (a.a != 0)
          throw new g("skipField with invalid type, type value: " + j + ", " + a.a); 
        j = a(0, 0, true);
        this.a.position(j + this.a.position());
      case 10:
        break;
    } 
    a();
  }
  
  private boolean a(int paramInt) {
    boolean bool2;
    boolean bool1 = false;
    try {
      a a = new a();
      this();
      while (true) {
        int j = a(a, this.a.duplicate());
        if (paramInt <= a.b || a.a == 11) {
          bool2 = bool1;
          if (paramInt == a.b)
            bool2 = true; 
          return bool2;
        } 
        this.a.position(j + this.a.position());
        a(a.a);
      } 
    } catch (g g) {
      bool2 = bool1;
    } catch (BufferUnderflowException bufferUnderflowException) {
      bool2 = bool1;
    } 
    return bool2;
  }
  
  private <T> T[] a(T[] paramArrayOfT, int paramInt, boolean paramBoolean) {
    if (paramArrayOfT == null || paramArrayOfT.length == 0)
      throw new g("unable to get type of key and value."); 
    return b(paramArrayOfT[0], paramInt, paramBoolean);
  }
  
  private <T> T[] b(T paramT, int paramInt, boolean paramBoolean) {
    T[] arrayOfT;
    if (a(paramInt)) {
      arrayOfT = (T[])new a();
      a((a)arrayOfT, this.a);
      switch (((a)arrayOfT).a) {
        default:
          throw new g("type mismatch.");
        case 9:
          break;
      } 
      int j = a(0, 0, true);
      if (j < 0)
        throw new g("size invalid: " + j); 
      Object[] arrayOfObject = (Object[])Array.newInstance(paramT.getClass(), j);
      paramInt = 0;
      while (true) {
        Object[] arrayOfObject1 = arrayOfObject;
        if (paramInt < j) {
          arrayOfObject[paramInt] = a(paramT, 0, true);
          paramInt++;
          continue;
        } 
        break;
      } 
    } else {
      if (paramBoolean)
        throw new g("require field not exist."); 
      arrayOfT = null;
    } 
    return arrayOfT;
  }
  
  private boolean[] d(int paramInt, boolean paramBoolean) {
    boolean[] arrayOfBoolean;
    a a = null;
    if (a(paramInt)) {
      a = new a();
      a(a, this.a);
      switch (a.a) {
        default:
          throw new g("type mismatch.");
        case 9:
          break;
      } 
      int j = a(0, 0, true);
      if (j < 0)
        throw new g("size invalid: " + j); 
      arrayOfBoolean = new boolean[j];
      for (paramInt = 0; paramInt < j; paramInt++) {
        if (a((byte)0, 0, true) != 0) {
          paramBoolean = true;
        } else {
          paramBoolean = false;
        } 
        arrayOfBoolean[paramInt] = paramBoolean;
      } 
      return arrayOfBoolean;
    } 
    if (paramBoolean)
      throw new g("require field not exist."); 
    return arrayOfBoolean;
  }
  
  private short[] e(int paramInt, boolean paramBoolean) {
    short[] arrayOfShort;
    a a = null;
    if (a(paramInt)) {
      a = new a();
      a(a, this.a);
      switch (a.a) {
        default:
          throw new g("type mismatch.");
        case 9:
          break;
      } 
      int j = a(0, 0, true);
      if (j < 0)
        throw new g("size invalid: " + j); 
      short[] arrayOfShort1 = new short[j];
      paramInt = 0;
      while (true) {
        arrayOfShort = arrayOfShort1;
        if (paramInt < j) {
          arrayOfShort1[paramInt] = a(arrayOfShort1[0], 0, true);
          paramInt++;
          continue;
        } 
        break;
      } 
    } else if (paramBoolean) {
      throw new g("require field not exist.");
    } 
    return arrayOfShort;
  }
  
  private int[] f(int paramInt, boolean paramBoolean) {
    int[] arrayOfInt;
    a a = null;
    if (a(paramInt)) {
      a = new a();
      a(a, this.a);
      switch (a.a) {
        default:
          throw new g("type mismatch.");
        case 9:
          break;
      } 
      int j = a(0, 0, true);
      if (j < 0)
        throw new g("size invalid: " + j); 
      int[] arrayOfInt1 = new int[j];
      paramInt = 0;
      while (true) {
        arrayOfInt = arrayOfInt1;
        if (paramInt < j) {
          arrayOfInt1[paramInt] = a(arrayOfInt1[0], 0, true);
          paramInt++;
          continue;
        } 
        break;
      } 
    } else if (paramBoolean) {
      throw new g("require field not exist.");
    } 
    return arrayOfInt;
  }
  
  private long[] g(int paramInt, boolean paramBoolean) {
    long[] arrayOfLong;
    a a = null;
    if (a(paramInt)) {
      a = new a();
      a(a, this.a);
      switch (a.a) {
        default:
          throw new g("type mismatch.");
        case 9:
          break;
      } 
      int j = a(0, 0, true);
      if (j < 0)
        throw new g("size invalid: " + j); 
      long[] arrayOfLong1 = new long[j];
      paramInt = 0;
      while (true) {
        arrayOfLong = arrayOfLong1;
        if (paramInt < j) {
          arrayOfLong1[paramInt] = a(arrayOfLong1[0], 0, true);
          paramInt++;
          continue;
        } 
        break;
      } 
    } else if (paramBoolean) {
      throw new g("require field not exist.");
    } 
    return arrayOfLong;
  }
  
  private float[] h(int paramInt, boolean paramBoolean) {
    float[] arrayOfFloat;
    a a = null;
    if (a(paramInt)) {
      a = new a();
      a(a, this.a);
      switch (a.a) {
        default:
          throw new g("type mismatch.");
        case 9:
          break;
      } 
      int j = a(0, 0, true);
      if (j < 0)
        throw new g("size invalid: " + j); 
      float[] arrayOfFloat1 = new float[j];
      paramInt = 0;
      while (true) {
        arrayOfFloat = arrayOfFloat1;
        if (paramInt < j) {
          arrayOfFloat1[paramInt] = a(arrayOfFloat1[0], 0, true);
          paramInt++;
          continue;
        } 
        break;
      } 
    } else if (paramBoolean) {
      throw new g("require field not exist.");
    } 
    return arrayOfFloat;
  }
  
  private double[] i(int paramInt, boolean paramBoolean) {
    double[] arrayOfDouble;
    a a = null;
    if (a(paramInt)) {
      a = new a();
      a(a, this.a);
      switch (a.a) {
        default:
          throw new g("type mismatch.");
        case 9:
          break;
      } 
      int j = a(0, 0, true);
      if (j < 0)
        throw new g("size invalid: " + j); 
      double[] arrayOfDouble1 = new double[j];
      paramInt = 0;
      while (true) {
        arrayOfDouble = arrayOfDouble1;
        if (paramInt < j) {
          arrayOfDouble1[paramInt] = a(arrayOfDouble1[0], 0, true);
          paramInt++;
          continue;
        } 
        break;
      } 
    } else if (paramBoolean) {
      throw new g("require field not exist.");
    } 
    return arrayOfDouble;
  }
  
  public final byte a(byte paramByte, int paramInt, boolean paramBoolean) {
    if (a(paramInt)) {
      a a = new a();
      a(a, this.a);
      switch (a.a) {
        default:
          throw new g("type mismatch.");
        case 12:
          paramByte = 0;
          return paramByte;
        case 0:
          break;
      } 
      paramByte = this.a.get();
      return paramByte;
    } 
    byte b = paramByte;
    if (paramBoolean)
      throw new g("require field not exist."); 
    return b;
  }
  
  public final int a(int paramInt1, int paramInt2, boolean paramBoolean) {
    if (a(paramInt2)) {
      a a = new a();
      a(a, this.a);
      switch (a.a) {
        default:
          throw new g("type mismatch.");
        case 12:
          return 0;
        case 0:
          return this.a.get();
        case 1:
          return this.a.getShort();
        case 2:
          break;
      } 
      return this.a.getInt();
    } 
    if (paramBoolean)
      throw new g("require field not exist."); 
    return paramInt1;
  }
  
  public final int a(String paramString) {
    this.b = paramString;
    return 0;
  }
  
  public final long a(long paramLong, int paramInt, boolean paramBoolean) {
    if (a(paramInt)) {
      a a = new a();
      a(a, this.a);
      switch (a.a) {
        default:
          throw new g("type mismatch.");
        case 12:
          return 0L;
        case 0:
          return this.a.get();
        case 1:
          return this.a.getShort();
        case 2:
          return this.a.getInt();
        case 3:
          break;
      } 
      return this.a.getLong();
    } 
    if (paramBoolean)
      throw new g("require field not exist."); 
    return paramLong;
  }
  
  public final k a(k paramk, int paramInt, boolean paramBoolean) {
    a a2 = null;
    if (a(paramInt)) {
      try {
        paramk = (k)paramk.getClass().newInstance();
        a2 = new a();
        a(a2, this.a);
        if (a2.a != 10)
          throw new g("type mismatch."); 
      } catch (Exception exception) {
        throw new g(exception.getMessage());
      } 
      exception.a(this);
      a();
      return (k)exception;
    } 
    a a1 = a2;
    if (paramBoolean)
      throw new g("require field not exist."); 
    return (k)a1;
  }
  
  public final <T> Object a(T paramT, int paramInt, boolean paramBoolean) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #4
    //   3: iconst_0
    //   4: istore #5
    //   6: aload_1
    //   7: instanceof java/lang/Byte
    //   10: ifeq -> 26
    //   13: aload_0
    //   14: iconst_0
    //   15: iload_2
    //   16: iload_3
    //   17: invokevirtual a : (BIZ)B
    //   20: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   23: astore_1
    //   24: aload_1
    //   25: areturn
    //   26: aload_1
    //   27: instanceof java/lang/Boolean
    //   30: ifeq -> 55
    //   33: aload_0
    //   34: iconst_0
    //   35: iload_2
    //   36: iload_3
    //   37: invokevirtual a : (BIZ)B
    //   40: ifeq -> 46
    //   43: iconst_1
    //   44: istore #5
    //   46: iload #5
    //   48: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   51: astore_1
    //   52: goto -> 24
    //   55: aload_1
    //   56: instanceof java/lang/Short
    //   59: ifeq -> 76
    //   62: aload_0
    //   63: iconst_0
    //   64: iload_2
    //   65: iload_3
    //   66: invokevirtual a : (SIZ)S
    //   69: invokestatic valueOf : (S)Ljava/lang/Short;
    //   72: astore_1
    //   73: goto -> 24
    //   76: aload_1
    //   77: instanceof java/lang/Integer
    //   80: ifeq -> 97
    //   83: aload_0
    //   84: iconst_0
    //   85: iload_2
    //   86: iload_3
    //   87: invokevirtual a : (IIZ)I
    //   90: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   93: astore_1
    //   94: goto -> 24
    //   97: aload_1
    //   98: instanceof java/lang/Long
    //   101: ifeq -> 118
    //   104: aload_0
    //   105: lconst_0
    //   106: iload_2
    //   107: iload_3
    //   108: invokevirtual a : (JIZ)J
    //   111: invokestatic valueOf : (J)Ljava/lang/Long;
    //   114: astore_1
    //   115: goto -> 24
    //   118: aload_1
    //   119: instanceof java/lang/Float
    //   122: ifeq -> 139
    //   125: aload_0
    //   126: fconst_0
    //   127: iload_2
    //   128: iload_3
    //   129: invokespecial a : (FIZ)F
    //   132: invokestatic valueOf : (F)Ljava/lang/Float;
    //   135: astore_1
    //   136: goto -> 24
    //   139: aload_1
    //   140: instanceof java/lang/Double
    //   143: ifeq -> 160
    //   146: aload_0
    //   147: dconst_0
    //   148: iload_2
    //   149: iload_3
    //   150: invokespecial a : (DIZ)D
    //   153: invokestatic valueOf : (D)Ljava/lang/Double;
    //   156: astore_1
    //   157: goto -> 24
    //   160: aload_1
    //   161: instanceof java/lang/String
    //   164: ifeq -> 180
    //   167: aload_0
    //   168: iload_2
    //   169: iload_3
    //   170: invokevirtual b : (IZ)Ljava/lang/String;
    //   173: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   176: astore_1
    //   177: goto -> 24
    //   180: aload_1
    //   181: instanceof java/util/Map
    //   184: ifeq -> 213
    //   187: aload_1
    //   188: checkcast java/util/Map
    //   191: astore_1
    //   192: aload_0
    //   193: new java/util/HashMap
    //   196: dup
    //   197: invokespecial <init> : ()V
    //   200: aload_1
    //   201: iload_2
    //   202: iload_3
    //   203: invokespecial a : (Ljava/util/Map;Ljava/util/Map;IZ)Ljava/util/Map;
    //   206: checkcast java/util/HashMap
    //   209: astore_1
    //   210: goto -> 24
    //   213: aload_1
    //   214: instanceof java/util/List
    //   217: ifeq -> 310
    //   220: aload_1
    //   221: checkcast java/util/List
    //   224: astore_1
    //   225: aload_1
    //   226: ifnull -> 238
    //   229: aload_1
    //   230: invokeinterface isEmpty : ()Z
    //   235: ifeq -> 249
    //   238: new java/util/ArrayList
    //   241: dup
    //   242: invokespecial <init> : ()V
    //   245: astore_1
    //   246: goto -> 24
    //   249: aload_0
    //   250: aload_1
    //   251: iconst_0
    //   252: invokeinterface get : (I)Ljava/lang/Object;
    //   257: iload_2
    //   258: iload_3
    //   259: invokespecial b : (Ljava/lang/Object;IZ)[Ljava/lang/Object;
    //   262: astore #6
    //   264: aload #6
    //   266: ifnonnull -> 274
    //   269: aconst_null
    //   270: astore_1
    //   271: goto -> 24
    //   274: new java/util/ArrayList
    //   277: dup
    //   278: invokespecial <init> : ()V
    //   281: astore_1
    //   282: iload #4
    //   284: istore_2
    //   285: iload_2
    //   286: aload #6
    //   288: arraylength
    //   289: if_icmpge -> 307
    //   292: aload_1
    //   293: aload #6
    //   295: iload_2
    //   296: aaload
    //   297: invokevirtual add : (Ljava/lang/Object;)Z
    //   300: pop
    //   301: iinc #2, 1
    //   304: goto -> 285
    //   307: goto -> 24
    //   310: aload_1
    //   311: instanceof com/tencent/bugly/proguard/k
    //   314: ifeq -> 331
    //   317: aload_0
    //   318: aload_1
    //   319: checkcast com/tencent/bugly/proguard/k
    //   322: iload_2
    //   323: iload_3
    //   324: invokevirtual a : (Lcom/tencent/bugly/proguard/k;IZ)Lcom/tencent/bugly/proguard/k;
    //   327: astore_1
    //   328: goto -> 24
    //   331: aload_1
    //   332: invokevirtual getClass : ()Ljava/lang/Class;
    //   335: invokevirtual isArray : ()Z
    //   338: ifeq -> 481
    //   341: aload_1
    //   342: instanceof [B
    //   345: ifne -> 355
    //   348: aload_1
    //   349: instanceof [Ljava/lang/Byte;
    //   352: ifeq -> 365
    //   355: aload_0
    //   356: iload_2
    //   357: iload_3
    //   358: invokevirtual c : (IZ)[B
    //   361: astore_1
    //   362: goto -> 24
    //   365: aload_1
    //   366: instanceof [Z
    //   369: ifeq -> 382
    //   372: aload_0
    //   373: iload_2
    //   374: iload_3
    //   375: invokespecial d : (IZ)[Z
    //   378: astore_1
    //   379: goto -> 24
    //   382: aload_1
    //   383: instanceof [S
    //   386: ifeq -> 399
    //   389: aload_0
    //   390: iload_2
    //   391: iload_3
    //   392: invokespecial e : (IZ)[S
    //   395: astore_1
    //   396: goto -> 24
    //   399: aload_1
    //   400: instanceof [I
    //   403: ifeq -> 416
    //   406: aload_0
    //   407: iload_2
    //   408: iload_3
    //   409: invokespecial f : (IZ)[I
    //   412: astore_1
    //   413: goto -> 24
    //   416: aload_1
    //   417: instanceof [J
    //   420: ifeq -> 433
    //   423: aload_0
    //   424: iload_2
    //   425: iload_3
    //   426: invokespecial g : (IZ)[J
    //   429: astore_1
    //   430: goto -> 24
    //   433: aload_1
    //   434: instanceof [F
    //   437: ifeq -> 450
    //   440: aload_0
    //   441: iload_2
    //   442: iload_3
    //   443: invokespecial h : (IZ)[F
    //   446: astore_1
    //   447: goto -> 24
    //   450: aload_1
    //   451: instanceof [D
    //   454: ifeq -> 467
    //   457: aload_0
    //   458: iload_2
    //   459: iload_3
    //   460: invokespecial i : (IZ)[D
    //   463: astore_1
    //   464: goto -> 24
    //   467: aload_0
    //   468: aload_1
    //   469: checkcast [Ljava/lang/Object;
    //   472: iload_2
    //   473: iload_3
    //   474: invokespecial a : ([Ljava/lang/Object;IZ)[Ljava/lang/Object;
    //   477: astore_1
    //   478: goto -> 24
    //   481: new com/tencent/bugly/proguard/g
    //   484: dup
    //   485: ldc_w 'read object error: unsupport type.'
    //   488: invokespecial <init> : (Ljava/lang/String;)V
    //   491: athrow
  }
  
  public final <K, V> HashMap<K, V> a(Map<K, V> paramMap, int paramInt, boolean paramBoolean) {
    return (HashMap<K, V>)a(new HashMap<K, V>(), paramMap, paramInt, paramBoolean);
  }
  
  public final short a(short paramShort, int paramInt, boolean paramBoolean) {
    if (a(paramInt)) {
      a a = new a();
      a(a, this.a);
      switch (a.a) {
        default:
          throw new g("type mismatch.");
        case 12:
          paramShort = 0;
          return paramShort;
        case 0:
          paramShort = (short)this.a.get();
          return paramShort;
        case 1:
          break;
      } 
      paramShort = this.a.getShort();
      return paramShort;
    } 
    short s = paramShort;
    if (paramBoolean)
      throw new g("require field not exist."); 
    return s;
  }
  
  public final void a(byte[] paramArrayOfbyte) {
    if (this.a != null)
      this.a.clear(); 
    this.a = ByteBuffer.wrap(paramArrayOfbyte);
  }
  
  public final boolean a(int paramInt, boolean paramBoolean) {
    boolean bool = false;
    if (a((byte)0, paramInt, paramBoolean) != 0)
      bool = true; 
    return bool;
  }
  
  public final String b(int paramInt, boolean paramBoolean) {
    String str;
    a a = null;
    if (a(paramInt)) {
      byte b;
      a = new a();
      a(a, this.a);
      switch (a.a) {
        default:
          throw new g("type mismatch.");
        case 6:
          b = this.a.get();
          paramInt = b;
          if (b < 0)
            paramInt = b + 256; 
          arrayOfByte = new byte[paramInt];
          this.a.get(arrayOfByte);
          try {
            str = new String();
            this(arrayOfByte, this.b);
          } catch (UnsupportedEncodingException unsupportedEncodingException) {
            str = new String(arrayOfByte);
          } 
          return str;
        case 7:
          break;
      } 
      paramInt = this.a.getInt();
      if (paramInt > 104857600 || paramInt < 0)
        throw new g("String too long: " + paramInt); 
      byte[] arrayOfByte = new byte[paramInt];
      this.a.get(arrayOfByte);
      try {
        str = new String();
        this(arrayOfByte, this.b);
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
        str = new String(arrayOfByte);
      } 
      return str;
    } 
    if (paramBoolean)
      throw new g("require field not exist."); 
    return str;
  }
  
  public final byte[] c(int paramInt, boolean paramBoolean) {
    byte[] arrayOfByte;
    a a = null;
    if (a(paramInt)) {
      a a1 = new a();
      a(a1, this.a);
      switch (a1.a) {
        default:
          throw new g("type mismatch.");
        case 13:
          a = new a();
          a(a, this.a);
          if (a.a != 0)
            throw new g("type mismatch, tag: " + paramInt + ", type: " + a1.a + ", " + a.a); 
          j = a(0, 0, true);
          if (j < 0)
            throw new g("invalid size, tag: " + paramInt + ", type: " + a1.a + ", " + a.a + ", size: " + j); 
          arrayOfByte = new byte[j];
          this.a.get(arrayOfByte);
          return arrayOfByte;
        case 9:
          break;
      } 
      int j = a(0, 0, true);
      if (j < 0)
        throw new g("size invalid: " + j); 
      byte[] arrayOfByte1 = new byte[j];
      paramInt = 0;
      while (true) {
        arrayOfByte = arrayOfByte1;
        if (paramInt < j) {
          arrayOfByte1[paramInt] = a(arrayOfByte1[0], 0, true);
          paramInt++;
          continue;
        } 
        return arrayOfByte;
      } 
    } 
    if (paramBoolean)
      throw new g("require field not exist."); 
    return arrayOfByte;
  }
  
  public static final class a {
    public byte a;
    
    public int b;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/i.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */