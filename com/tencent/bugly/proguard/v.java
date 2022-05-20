package com.tencent.bugly.proguard;

import android.content.Context;
import android.os.Process;
import com.tencent.bugly.crashreport.common.info.a;
import com.tencent.bugly.crashreport.common.info.b;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.common.strategy.a;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class v implements Runnable {
  private int a = 2;
  
  private int b = 30000;
  
  private final Context c;
  
  private final int d;
  
  private final byte[] e;
  
  private final a f;
  
  private final a g;
  
  private final s h;
  
  private final u i;
  
  private final int j;
  
  private final t k;
  
  private final t l;
  
  private String m = null;
  
  private final String n;
  
  private final Map<String, String> o;
  
  private int p = 0;
  
  private long q = 0L;
  
  private long r = 0L;
  
  private boolean s = true;
  
  private boolean t = false;
  
  public v(Context paramContext, int paramInt1, int paramInt2, byte[] paramArrayOfbyte, String paramString1, String paramString2, t paramt, boolean paramBoolean1, int paramInt3, int paramInt4, boolean paramBoolean2, Map<String, String> paramMap) {
    this.c = paramContext;
    this.f = a.a(paramContext);
    this.e = paramArrayOfbyte;
    this.g = a.a();
    this.h = s.a(paramContext);
    this.i = u.a();
    this.j = paramInt1;
    this.m = paramString1;
    this.n = paramString2;
    this.k = paramt;
    u u1 = this.i;
    this.l = null;
    this.s = paramBoolean1;
    this.d = paramInt2;
    if (paramInt3 > 0)
      this.a = paramInt3; 
    if (paramInt4 > 0)
      this.b = paramInt4; 
    this.t = paramBoolean2;
    this.o = paramMap;
  }
  
  public v(Context paramContext, int paramInt1, int paramInt2, byte[] paramArrayOfbyte, String paramString1, String paramString2, t paramt, boolean paramBoolean1, boolean paramBoolean2) {
    this(paramContext, paramInt1, paramInt2, paramArrayOfbyte, paramString1, paramString2, paramt, paramBoolean1, 2, 30000, paramBoolean2, null);
  }
  
  private static String a(String paramString) {
    if (!z.a(paramString))
      try {
        String str = String.format("%s?aid=%s", new Object[] { paramString, UUID.randomUUID().toString() });
        paramString = str;
      } catch (Throwable throwable) {
        x.a(throwable);
      }  
    return paramString;
  }
  
  private void a(an paraman, boolean paramBoolean, int paramInt1, String paramString, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: getfield d : I
    //   4: lookupswitch default -> 48, 630 -> 205, 640 -> 211, 830 -> 205, 840 -> 211
    //   48: aload_0
    //   49: getfield d : I
    //   52: invokestatic valueOf : (I)Ljava/lang/String;
    //   55: astore_1
    //   56: iload_2
    //   57: ifeq -> 217
    //   60: ldc '[Upload] Success: %s'
    //   62: iconst_1
    //   63: anewarray java/lang/Object
    //   66: dup
    //   67: iconst_0
    //   68: aload_1
    //   69: aastore
    //   70: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   73: pop
    //   74: aload_0
    //   75: getfield q : J
    //   78: aload_0
    //   79: getfield r : J
    //   82: ladd
    //   83: lconst_0
    //   84: lcmp
    //   85: ifle -> 132
    //   88: aload_0
    //   89: getfield i : Lcom/tencent/bugly/proguard/u;
    //   92: aload_0
    //   93: getfield t : Z
    //   96: invokevirtual a : (Z)J
    //   99: lstore #6
    //   101: aload_0
    //   102: getfield q : J
    //   105: lstore #8
    //   107: aload_0
    //   108: getfield r : J
    //   111: lstore #10
    //   113: aload_0
    //   114: getfield i : Lcom/tencent/bugly/proguard/u;
    //   117: lload #6
    //   119: lload #8
    //   121: ladd
    //   122: lload #10
    //   124: ladd
    //   125: aload_0
    //   126: getfield t : Z
    //   129: invokevirtual a : (JZ)V
    //   132: aload_0
    //   133: getfield k : Lcom/tencent/bugly/proguard/t;
    //   136: ifnull -> 168
    //   139: aload_0
    //   140: getfield k : Lcom/tencent/bugly/proguard/t;
    //   143: astore_1
    //   144: aload_0
    //   145: getfield d : I
    //   148: istore_3
    //   149: aload_0
    //   150: getfield q : J
    //   153: lstore #6
    //   155: aload_0
    //   156: getfield r : J
    //   159: lstore #6
    //   161: aload_1
    //   162: iload_2
    //   163: invokeinterface a : (Z)V
    //   168: aload_0
    //   169: getfield l : Lcom/tencent/bugly/proguard/t;
    //   172: ifnull -> 204
    //   175: aload_0
    //   176: getfield l : Lcom/tencent/bugly/proguard/t;
    //   179: astore_1
    //   180: aload_0
    //   181: getfield d : I
    //   184: istore_3
    //   185: aload_0
    //   186: getfield q : J
    //   189: lstore #6
    //   191: aload_0
    //   192: getfield r : J
    //   195: lstore #6
    //   197: aload_1
    //   198: iload_2
    //   199: invokeinterface a : (Z)V
    //   204: return
    //   205: ldc 'crash'
    //   207: astore_1
    //   208: goto -> 56
    //   211: ldc 'userinfo'
    //   213: astore_1
    //   214: goto -> 56
    //   217: ldc '[Upload] Failed to upload(%d) %s: %s'
    //   219: iconst_3
    //   220: anewarray java/lang/Object
    //   223: dup
    //   224: iconst_0
    //   225: iload_3
    //   226: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   229: aastore
    //   230: dup
    //   231: iconst_1
    //   232: aload_1
    //   233: aastore
    //   234: dup
    //   235: iconst_2
    //   236: aload #4
    //   238: aastore
    //   239: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   242: pop
    //   243: aload_0
    //   244: getfield s : Z
    //   247: ifeq -> 74
    //   250: aload_0
    //   251: getfield i : Lcom/tencent/bugly/proguard/u;
    //   254: iload #5
    //   256: aconst_null
    //   257: invokevirtual a : (ILcom/tencent/bugly/proguard/an;)V
    //   260: goto -> 74
  }
  
  private static boolean a(an paraman, a parama, a parama1) {
    // Byte code:
    //   0: aload_0
    //   1: ifnonnull -> 18
    //   4: ldc 'resp == null!'
    //   6: iconst_0
    //   7: anewarray java/lang/Object
    //   10: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   13: pop
    //   14: iconst_0
    //   15: istore_3
    //   16: iload_3
    //   17: ireturn
    //   18: aload_0
    //   19: getfield a : B
    //   22: ifeq -> 50
    //   25: ldc 'resp result error %d'
    //   27: iconst_1
    //   28: anewarray java/lang/Object
    //   31: dup
    //   32: iconst_0
    //   33: aload_0
    //   34: getfield a : B
    //   37: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   40: aastore
    //   41: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   44: pop
    //   45: iconst_0
    //   46: istore_3
    //   47: goto -> 16
    //   50: aload_0
    //   51: getfield d : Ljava/lang/String;
    //   54: invokestatic a : (Ljava/lang/String;)Z
    //   57: ifne -> 107
    //   60: invokestatic b : ()Lcom/tencent/bugly/crashreport/common/info/a;
    //   63: invokevirtual i : ()Ljava/lang/String;
    //   66: aload_0
    //   67: getfield d : Ljava/lang/String;
    //   70: invokevirtual equals : (Ljava/lang/Object;)Z
    //   73: ifne -> 107
    //   76: invokestatic a : ()Lcom/tencent/bugly/proguard/p;
    //   79: getstatic com/tencent/bugly/crashreport/common/strategy/a.a : I
    //   82: ldc 'key_ip'
    //   84: aload_0
    //   85: getfield d : Ljava/lang/String;
    //   88: ldc 'UTF-8'
    //   90: invokevirtual getBytes : (Ljava/lang/String;)[B
    //   93: aconst_null
    //   94: iconst_1
    //   95: invokevirtual a : (ILjava/lang/String;[BLcom/tencent/bugly/proguard/o;Z)Z
    //   98: pop
    //   99: aload_1
    //   100: aload_0
    //   101: getfield d : Ljava/lang/String;
    //   104: invokevirtual d : (Ljava/lang/String;)V
    //   107: aload_0
    //   108: getfield f : Ljava/lang/String;
    //   111: invokestatic a : (Ljava/lang/String;)Z
    //   114: ifne -> 164
    //   117: invokestatic b : ()Lcom/tencent/bugly/crashreport/common/info/a;
    //   120: invokevirtual j : ()Ljava/lang/String;
    //   123: aload_0
    //   124: getfield f : Ljava/lang/String;
    //   127: invokevirtual equals : (Ljava/lang/Object;)Z
    //   130: ifne -> 164
    //   133: invokestatic a : ()Lcom/tencent/bugly/proguard/p;
    //   136: getstatic com/tencent/bugly/crashreport/common/strategy/a.a : I
    //   139: ldc 'key_imei'
    //   141: aload_0
    //   142: getfield f : Ljava/lang/String;
    //   145: ldc 'UTF-8'
    //   147: invokevirtual getBytes : (Ljava/lang/String;)[B
    //   150: aconst_null
    //   151: iconst_1
    //   152: invokevirtual a : (ILjava/lang/String;[BLcom/tencent/bugly/proguard/o;Z)Z
    //   155: pop
    //   156: aload_1
    //   157: aload_0
    //   158: getfield f : Ljava/lang/String;
    //   161: invokevirtual e : (Ljava/lang/String;)V
    //   164: aload_1
    //   165: aload_0
    //   166: getfield e : J
    //   169: putfield i : J
    //   172: aload_0
    //   173: getfield b : I
    //   176: sipush #510
    //   179: if_icmpne -> 272
    //   182: aload_0
    //   183: getfield c : [B
    //   186: ifnonnull -> 225
    //   189: ldc '[Upload] Strategy data is null. Response cmd: %d'
    //   191: iconst_1
    //   192: anewarray java/lang/Object
    //   195: dup
    //   196: iconst_0
    //   197: aload_0
    //   198: getfield b : I
    //   201: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   204: aastore
    //   205: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   208: pop
    //   209: iconst_0
    //   210: istore_3
    //   211: goto -> 16
    //   214: astore #4
    //   216: aload #4
    //   218: invokestatic a : (Ljava/lang/Throwable;)Z
    //   221: pop
    //   222: goto -> 164
    //   225: aload_0
    //   226: getfield c : [B
    //   229: ldc com/tencent/bugly/proguard/ap
    //   231: invokestatic a : ([BLjava/lang/Class;)Lcom/tencent/bugly/proguard/k;
    //   234: checkcast com/tencent/bugly/proguard/ap
    //   237: astore_1
    //   238: aload_1
    //   239: ifnonnull -> 267
    //   242: ldc '[Upload] Failed to decode strategy from server. Response cmd: %d'
    //   244: iconst_1
    //   245: anewarray java/lang/Object
    //   248: dup
    //   249: iconst_0
    //   250: aload_0
    //   251: getfield b : I
    //   254: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   257: aastore
    //   258: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   261: pop
    //   262: iconst_0
    //   263: istore_3
    //   264: goto -> 16
    //   267: aload_2
    //   268: aload_1
    //   269: invokevirtual a : (Lcom/tencent/bugly/proguard/ap;)V
    //   272: iconst_1
    //   273: istore_3
    //   274: goto -> 16
    // Exception table:
    //   from	to	target	type
    //   50	107	214	java/lang/Throwable
    //   107	164	214	java/lang/Throwable
  }
  
  public final void a(long paramLong) {
    this.p++;
    this.q += paramLong;
  }
  
  public final void b(long paramLong) {
    this.r += paramLong;
  }
  
  public final void run() {
    try {
      this.p = 0;
      this.q = 0L;
      this.r = 0L;
      byte[] arrayOfByte1 = this.e;
      if (b.f(this.c) == null) {
        a(null, false, 0, "network is not available", 0);
        return;
      } 
      if (arrayOfByte1 == null || arrayOfByte1.length == 0) {
        a(null, false, 0, "request package is empty!", 0);
        return;
      } 
    } catch (Throwable throwable) {
      if (!x.a(throwable))
        throwable.printStackTrace(); 
      return;
    } 
    long l = this.i.a(this.t);
    if (throwable.length + l >= 2097152L) {
      x.e("[Upload] Upload too much data, try next time: %d/%d", new Object[] { Long.valueOf(l), Long.valueOf(2097152L) });
      stringBuilder1 = new StringBuilder();
      this("over net consume: ");
      a(null, false, 0, stringBuilder1.append(2048L).append("K").toString(), 0);
      return;
    } 
    x.c("[Upload] Run upload task with cmd: %d", new Object[] { Integer.valueOf(this.d) });
    if (this.c == null || this.f == null || this.g == null || this.h == null) {
      a(null, false, 0, "illegal access error", 0);
      return;
    } 
    StrategyBean strategyBean = this.g.c();
    if (strategyBean == null) {
      a(null, false, 0, "illegal local strategy", 0);
      return;
    } 
    int i = 0;
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    this();
    StringBuilder stringBuilder1;
    byte[] arrayOfByte;
    hashMap.put("prodId", this.f.f());
    hashMap.put("bundleId", this.f.c);
    hashMap.put("appVer", this.f.j);
    if (this.o != null)
      hashMap.putAll(this.o); 
    StringBuilder stringBuilder2 = stringBuilder1;
    if (this.s) {
      hashMap.put("cmd", Integer.toString(this.d));
      hashMap.put("platformId", Byte.toString((byte)1));
      this.f.getClass();
      hashMap.put("sdkVer", "2.6.6");
      hashMap.put("strategylastUpdateTime", Long.toString(strategyBean.p));
      if (!this.i.a((Map)hashMap)) {
        a(null, false, 0, "failed to add security info to HTTP headers", 0);
        return;
      } 
      byte[] arrayOfByte1 = z.a((byte[])stringBuilder1, 2);
      if (arrayOfByte1 == null) {
        a(null, false, 0, "failed to zip request body", 0);
        return;
      } 
      arrayOfByte1 = this.i.a(arrayOfByte1);
      arrayOfByte = arrayOfByte1;
      if (arrayOfByte1 == null) {
        a(null, false, 0, "failed to encrypt request body", 0);
        return;
      } 
    } 
    this.i.a(this.j, System.currentTimeMillis());
    if (this.k != null) {
      t t1 = this.k;
      int m = this.d;
    } 
    if (this.l != null) {
      t t1 = this.l;
      int m = this.d;
    } 
    String str = this.m;
    int k = -1;
    int j = 0;
    while (true) {
      int m = j + 1;
      if (j < this.a) {
        byte[] arrayOfByte1;
        StringBuilder stringBuilder;
        byte[] arrayOfByte2;
        String str1 = str;
        if (m > 1) {
          x.d("[Upload] Failed to upload last time, wait and try(%d) again.", new Object[] { Integer.valueOf(m) });
          z.b(this.b);
          str1 = str;
          if (m == this.a) {
            x.d("[Upload] Use the back-up url at the last time: %s", new Object[] { this.n });
            str1 = this.n;
          } 
        } 
        x.c("[Upload] Send %d bytes", new Object[] { Integer.valueOf(arrayOfByte.length) });
        if (this.s) {
          str = a(str1);
        } else {
          str = str1;
        } 
        x.c("[Upload] Upload to %s with cmd %d (pid=%d | tid=%d).", new Object[] { str, Integer.valueOf(this.d), Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
        byte[] arrayOfByte3 = this.h.a(str, arrayOfByte, this, (Map)hashMap);
        if (arrayOfByte3 == null) {
          x.e("[Upload] Failed to upload(%d): %s", new Object[] { Integer.valueOf(1), "Failed to upload for no response!" });
          i = 1;
          j = m;
          continue;
        } 
        Map<String, String> map = this.h.a;
        j = k;
        if (this.s) {
          if (map == null || map.size() == 0) {
            x.d("[Upload] Headers is empty.", new Object[0]);
            j = 0;
          } else if (!map.containsKey("status")) {
            x.d("[Upload] Headers does not contain %s", new Object[] { "status" });
            j = 0;
          } else if (!map.containsKey("Bugly-Version")) {
            x.d("[Upload] Headers does not contain %s", new Object[] { "Bugly-Version" });
            j = 0;
          } else {
            String str2 = map.get("Bugly-Version");
            if (!str2.contains("bugly")) {
              x.d("[Upload] Bugly version is not valid: %s", new Object[] { str2 });
              j = 0;
            } else {
              x.c("[Upload] Bugly version from headers is: %s", new Object[] { str2 });
              j = 1;
            } 
          } 
          if (j == 0) {
            x.c("[Upload] Headers from server is not valid, just try again (pid=%d | tid=%d).", new Object[] { Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
            x.e("[Upload] Failed to upload(%d): %s", new Object[] { Integer.valueOf(1), "[Upload] Failed to upload for no status header." });
            if (map != null)
              for (Map.Entry<String, String> entry : map.entrySet()) {
                x.c(String.format("[key]: %s, [value]: %s", new Object[] { entry.getKey(), entry.getValue() }), new Object[0]);
              }  
            x.c("[Upload] Failed to upload for no status header.", new Object[0]);
            i = 1;
            j = m;
            continue;
          } 
          try {
            i = Integer.parseInt(entry.get("status"));
            k = i;
            x.c("[Upload] Status from server is %d (pid=%d | tid=%d).", new Object[] { Integer.valueOf(i), Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
            j = i;
            if (i != 0) {
              if (i == 2) {
                if (this.q + this.r > 0L) {
                  long l1 = this.i.a(this.t);
                  l = this.q;
                  long l2 = this.r;
                  this.i.a(l1 + l + l2, this.t);
                } 
                this.i.a(i, (an)null);
                x.a("[Upload] Session ID is invalid, will try again immediately (pid=%d | tid=%d).", new Object[] { Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
                this.i.a(this.j, this.d, this.e, this.m, this.n, this.k, this.a, this.b, true, this.o);
                return;
              } 
              StringBuilder stringBuilder3 = new StringBuilder();
              this("status of server is ");
              a(null, false, 1, stringBuilder3.append(i).toString(), i);
              return;
            } 
          } catch (Throwable throwable1) {
            stringBuilder = new StringBuilder();
            this("[Upload] Failed to upload for format of status header is invalid: ");
            x.e("[Upload] Failed to upload(%d): %s", new Object[] { Integer.valueOf(1), stringBuilder.append(Integer.toString(k)).toString() });
            i = 1;
            j = m;
            continue;
          } 
        } 
        x.c("[Upload] Received %d bytes", new Object[] { Integer.valueOf(stringBuilder.length) });
        if (this.s) {
          if (stringBuilder.length == 0) {
            for (Map.Entry<String, String> entry1 : entry.entrySet()) {
              x.c("[Upload] HTTP headers from server: key = %s, value = %s", new Object[] { entry1.getKey(), entry1.getValue() });
            } 
            a(null, false, 1, "response data from server is empty", 0);
            return;
          } 
          arrayOfByte1 = this.i.b((byte[])stringBuilder);
          if (arrayOfByte1 == null) {
            a(null, false, 1, "failed to decrypt response from server", 0);
            return;
          } 
          arrayOfByte2 = z.b(arrayOfByte1, 2);
          arrayOfByte1 = arrayOfByte2;
          if (arrayOfByte2 == null) {
            a(null, false, 1, "failed unzip(Gzip) response from server", 0);
            return;
          } 
        } else {
          arrayOfByte1 = arrayOfByte2;
        } 
        an an = a.a(arrayOfByte1, this.s);
        if (an == null) {
          a(null, false, 1, "failed to decode response package", 0);
          return;
        } 
        if (this.s)
          this.i.a(j, an); 
        i = an.b;
        if (an.c == null) {
          j = 0;
        } else {
          j = an.c.length;
        } 
        x.c("[Upload] Response cmd is: %d, length of sBuffer is: %d", new Object[] { Integer.valueOf(i), Integer.valueOf(j) });
        if (!a(an, this.f, this.g)) {
          a(an, false, 2, "failed to process response package", 0);
          return;
        } 
        a(an, true, 2, "successfully uploaded", 0);
        return;
      } 
      a(null, false, i, "failed after many attempts", 0);
      return;
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/v.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */