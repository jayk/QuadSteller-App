package com.tencent.bugly.crashreport.common.strategy;

import android.content.Context;
import com.tencent.bugly.crashreport.biz.b;
import com.tencent.bugly.proguard.ap;
import com.tencent.bugly.proguard.p;
import com.tencent.bugly.proguard.r;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.util.List;
import java.util.Map;

public final class a {
  public static int a = 1000;
  
  private static a b = null;
  
  private static String h = null;
  
  private final List<com.tencent.bugly.a> c;
  
  private final w d;
  
  private final StrategyBean e;
  
  private StrategyBean f = null;
  
  private Context g;
  
  private a(Context paramContext, List<com.tencent.bugly.a> paramList) {
    this.g = paramContext;
    this.e = new StrategyBean();
    this.c = paramList;
    this.d = w.a();
  }
  
  public static a a() {
    // Byte code:
    //   0: ldc com/tencent/bugly/crashreport/common/strategy/a
    //   2: monitorenter
    //   3: getstatic com/tencent/bugly/crashreport/common/strategy/a.b : Lcom/tencent/bugly/crashreport/common/strategy/a;
    //   6: astore_0
    //   7: ldc com/tencent/bugly/crashreport/common/strategy/a
    //   9: monitorexit
    //   10: aload_0
    //   11: areturn
    //   12: astore_0
    //   13: ldc com/tencent/bugly/crashreport/common/strategy/a
    //   15: monitorexit
    //   16: aload_0
    //   17: athrow
    // Exception table:
    //   from	to	target	type
    //   3	7	12	finally
  }
  
  public static a a(Context paramContext, List<com.tencent.bugly.a> paramList) {
    // Byte code:
    //   0: ldc com/tencent/bugly/crashreport/common/strategy/a
    //   2: monitorenter
    //   3: getstatic com/tencent/bugly/crashreport/common/strategy/a.b : Lcom/tencent/bugly/crashreport/common/strategy/a;
    //   6: ifnonnull -> 23
    //   9: new com/tencent/bugly/crashreport/common/strategy/a
    //   12: astore_2
    //   13: aload_2
    //   14: aload_0
    //   15: aload_1
    //   16: invokespecial <init> : (Landroid/content/Context;Ljava/util/List;)V
    //   19: aload_2
    //   20: putstatic com/tencent/bugly/crashreport/common/strategy/a.b : Lcom/tencent/bugly/crashreport/common/strategy/a;
    //   23: getstatic com/tencent/bugly/crashreport/common/strategy/a.b : Lcom/tencent/bugly/crashreport/common/strategy/a;
    //   26: astore_0
    //   27: ldc com/tencent/bugly/crashreport/common/strategy/a
    //   29: monitorexit
    //   30: aload_0
    //   31: areturn
    //   32: astore_0
    //   33: ldc com/tencent/bugly/crashreport/common/strategy/a
    //   35: monitorexit
    //   36: aload_0
    //   37: athrow
    // Exception table:
    //   from	to	target	type
    //   3	23	32	finally
    //   23	27	32	finally
  }
  
  public static StrategyBean d() {
    null = p.a().a(2);
    if (null != null && null.size() > 0) {
      r r = null.get(0);
      if (r.g != null)
        return (StrategyBean)z.a(r.g, StrategyBean.CREATOR); 
    } 
    return null;
  }
  
  public final void a(long paramLong) {
    this.d.a(new Thread(this) {
          public final void run() {
            try {
              Map map = p.a().a(a.a, null, true);
              if (map != null) {
                byte[] arrayOfByte2 = (byte[])map.get("key_imei");
                byte[] arrayOfByte1 = (byte[])map.get("key_ip");
                if (arrayOfByte2 != null) {
                  com.tencent.bugly.crashreport.common.info.a a3 = com.tencent.bugly.crashreport.common.info.a.a(a.a(this.a));
                  String str = new String();
                  this(arrayOfByte2);
                  a3.e(str);
                } 
                if (arrayOfByte1 != null) {
                  com.tencent.bugly.crashreport.common.info.a a3 = com.tencent.bugly.crashreport.common.info.a.a(a.a(this.a));
                  String str = new String();
                  this(arrayOfByte1);
                  a3.d(str);
                } 
              } 
              a a1 = this.a;
              a a2 = this.a;
              a.a(a1, a.d());
              if (a.b(this.a) != null && !z.a(a.e()) && z.c(a.e())) {
                (a.b(this.a)).r = a.e();
                (a.b(this.a)).s = a.e();
              } 
            } catch (Throwable throwable) {}
            this.a.a(a.b(this.a), false);
          }
        }paramLong);
  }
  
  protected final void a(StrategyBean paramStrategyBean, boolean paramBoolean) {
    x.c("[Strategy] Notify %s", new Object[] { b.class.getName() });
    b.a(paramStrategyBean, paramBoolean);
    for (com.tencent.bugly.a a1 : this.c) {
      try {
        x.c("[Strategy] Notify %s", new Object[] { a1.getClass().getName() });
        a1.onServerStrategyChanged(paramStrategyBean);
      } catch (Throwable throwable) {
        if (!x.a(throwable))
          throwable.printStackTrace(); 
      } 
    } 
  }
  
  public final void a(ap paramap) {
    // Byte code:
    //   0: aload_1
    //   1: ifnonnull -> 5
    //   4: return
    //   5: aload_0
    //   6: getfield f : Lcom/tencent/bugly/crashreport/common/strategy/StrategyBean;
    //   9: ifnull -> 27
    //   12: aload_1
    //   13: getfield h : J
    //   16: aload_0
    //   17: getfield f : Lcom/tencent/bugly/crashreport/common/strategy/StrategyBean;
    //   20: getfield p : J
    //   23: lcmp
    //   24: ifeq -> 4
    //   27: new com/tencent/bugly/crashreport/common/strategy/StrategyBean
    //   30: dup
    //   31: invokespecial <init> : ()V
    //   34: astore_2
    //   35: aload_2
    //   36: aload_1
    //   37: getfield a : Z
    //   40: putfield g : Z
    //   43: aload_2
    //   44: aload_1
    //   45: getfield c : Z
    //   48: putfield i : Z
    //   51: aload_2
    //   52: aload_1
    //   53: getfield b : Z
    //   56: putfield h : Z
    //   59: aconst_null
    //   60: invokestatic a : (Ljava/lang/String;)Z
    //   63: ifne -> 73
    //   66: aconst_null
    //   67: invokestatic c : (Ljava/lang/String;)Z
    //   70: ifne -> 143
    //   73: aload_1
    //   74: getfield d : Ljava/lang/String;
    //   77: invokestatic c : (Ljava/lang/String;)Z
    //   80: ifeq -> 108
    //   83: ldc '[Strategy] Upload url changes to %s'
    //   85: iconst_1
    //   86: anewarray java/lang/Object
    //   89: dup
    //   90: iconst_0
    //   91: aload_1
    //   92: getfield d : Ljava/lang/String;
    //   95: aastore
    //   96: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   99: pop
    //   100: aload_2
    //   101: aload_1
    //   102: getfield d : Ljava/lang/String;
    //   105: putfield r : Ljava/lang/String;
    //   108: aload_1
    //   109: getfield e : Ljava/lang/String;
    //   112: invokestatic c : (Ljava/lang/String;)Z
    //   115: ifeq -> 143
    //   118: ldc '[Strategy] Exception upload url changes to %s'
    //   120: iconst_1
    //   121: anewarray java/lang/Object
    //   124: dup
    //   125: iconst_0
    //   126: aload_1
    //   127: getfield e : Ljava/lang/String;
    //   130: aastore
    //   131: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   134: pop
    //   135: aload_2
    //   136: aload_1
    //   137: getfield e : Ljava/lang/String;
    //   140: putfield s : Ljava/lang/String;
    //   143: aload_1
    //   144: getfield f : Lcom/tencent/bugly/proguard/ao;
    //   147: ifnull -> 174
    //   150: aload_1
    //   151: getfield f : Lcom/tencent/bugly/proguard/ao;
    //   154: getfield a : Ljava/lang/String;
    //   157: invokestatic a : (Ljava/lang/String;)Z
    //   160: ifne -> 174
    //   163: aload_2
    //   164: aload_1
    //   165: getfield f : Lcom/tencent/bugly/proguard/ao;
    //   168: getfield a : Ljava/lang/String;
    //   171: putfield u : Ljava/lang/String;
    //   174: aload_1
    //   175: getfield h : J
    //   178: lconst_0
    //   179: lcmp
    //   180: ifeq -> 191
    //   183: aload_2
    //   184: aload_1
    //   185: getfield h : J
    //   188: putfield p : J
    //   191: aload_1
    //   192: getfield g : Ljava/util/Map;
    //   195: ifnull -> 377
    //   198: aload_1
    //   199: getfield g : Ljava/util/Map;
    //   202: invokeinterface size : ()I
    //   207: ifle -> 377
    //   210: aload_2
    //   211: aload_1
    //   212: getfield g : Ljava/util/Map;
    //   215: putfield v : Ljava/util/Map;
    //   218: aload_1
    //   219: getfield g : Ljava/util/Map;
    //   222: ldc 'B11'
    //   224: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   229: checkcast java/lang/String
    //   232: astore_3
    //   233: aload_3
    //   234: ifnull -> 559
    //   237: aload_3
    //   238: ldc '1'
    //   240: invokevirtual equals : (Ljava/lang/Object;)Z
    //   243: ifeq -> 559
    //   246: aload_2
    //   247: iconst_1
    //   248: putfield j : Z
    //   251: aload_1
    //   252: getfield g : Ljava/util/Map;
    //   255: ldc 'B3'
    //   257: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   262: checkcast java/lang/String
    //   265: astore_3
    //   266: aload_3
    //   267: ifnull -> 281
    //   270: aload_2
    //   271: aload_3
    //   272: invokestatic valueOf : (Ljava/lang/String;)Ljava/lang/Long;
    //   275: invokevirtual longValue : ()J
    //   278: putfield y : J
    //   281: aload_2
    //   282: aload_1
    //   283: getfield i : I
    //   286: i2l
    //   287: putfield q : J
    //   290: aload_2
    //   291: aload_1
    //   292: getfield i : I
    //   295: i2l
    //   296: putfield x : J
    //   299: aload_1
    //   300: getfield g : Ljava/util/Map;
    //   303: ldc_w 'B27'
    //   306: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   311: checkcast java/lang/String
    //   314: astore_3
    //   315: aload_3
    //   316: ifnull -> 343
    //   319: aload_3
    //   320: invokevirtual length : ()I
    //   323: ifle -> 343
    //   326: aload_3
    //   327: invokestatic parseInt : (Ljava/lang/String;)I
    //   330: istore #4
    //   332: iload #4
    //   334: ifle -> 343
    //   337: aload_2
    //   338: iload #4
    //   340: putfield w : I
    //   343: aload_1
    //   344: getfield g : Ljava/util/Map;
    //   347: ldc_w 'B25'
    //   350: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   355: checkcast java/lang/String
    //   358: astore_1
    //   359: aload_1
    //   360: ifnull -> 582
    //   363: aload_1
    //   364: ldc '1'
    //   366: invokevirtual equals : (Ljava/lang/Object;)Z
    //   369: ifeq -> 582
    //   372: aload_2
    //   373: iconst_1
    //   374: putfield l : Z
    //   377: ldc_w '[Strategy] enableCrashReport:%b, enableQuery:%b, enableUserInfo:%b, enableAnr:%b, enableBlock:%b, enableSession:%b, enableSessionTimer:%b, sessionOverTime:%d, enableCocos:%b, strategyLastUpdateTime:%d'
    //   380: bipush #10
    //   382: anewarray java/lang/Object
    //   385: dup
    //   386: iconst_0
    //   387: aload_2
    //   388: getfield g : Z
    //   391: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   394: aastore
    //   395: dup
    //   396: iconst_1
    //   397: aload_2
    //   398: getfield i : Z
    //   401: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   404: aastore
    //   405: dup
    //   406: iconst_2
    //   407: aload_2
    //   408: getfield h : Z
    //   411: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   414: aastore
    //   415: dup
    //   416: iconst_3
    //   417: aload_2
    //   418: getfield j : Z
    //   421: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   424: aastore
    //   425: dup
    //   426: iconst_4
    //   427: aload_2
    //   428: getfield k : Z
    //   431: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   434: aastore
    //   435: dup
    //   436: iconst_5
    //   437: aload_2
    //   438: getfield n : Z
    //   441: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   444: aastore
    //   445: dup
    //   446: bipush #6
    //   448: aload_2
    //   449: getfield o : Z
    //   452: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   455: aastore
    //   456: dup
    //   457: bipush #7
    //   459: aload_2
    //   460: getfield q : J
    //   463: invokestatic valueOf : (J)Ljava/lang/Long;
    //   466: aastore
    //   467: dup
    //   468: bipush #8
    //   470: aload_2
    //   471: getfield l : Z
    //   474: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   477: aastore
    //   478: dup
    //   479: bipush #9
    //   481: aload_2
    //   482: getfield p : J
    //   485: invokestatic valueOf : (J)Ljava/lang/Long;
    //   488: aastore
    //   489: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   492: pop
    //   493: aload_0
    //   494: aload_2
    //   495: putfield f : Lcom/tencent/bugly/crashreport/common/strategy/StrategyBean;
    //   498: invokestatic a : ()Lcom/tencent/bugly/proguard/p;
    //   501: iconst_2
    //   502: invokevirtual b : (I)V
    //   505: new com/tencent/bugly/proguard/r
    //   508: dup
    //   509: invokespecial <init> : ()V
    //   512: astore_1
    //   513: aload_1
    //   514: iconst_2
    //   515: putfield b : I
    //   518: aload_1
    //   519: aload_2
    //   520: getfield e : J
    //   523: putfield a : J
    //   526: aload_1
    //   527: aload_2
    //   528: getfield f : J
    //   531: putfield e : J
    //   534: aload_1
    //   535: aload_2
    //   536: invokestatic a : (Landroid/os/Parcelable;)[B
    //   539: putfield g : [B
    //   542: invokestatic a : ()Lcom/tencent/bugly/proguard/p;
    //   545: aload_1
    //   546: invokevirtual a : (Lcom/tencent/bugly/proguard/r;)Z
    //   549: pop
    //   550: aload_0
    //   551: aload_2
    //   552: iconst_1
    //   553: invokevirtual a : (Lcom/tencent/bugly/crashreport/common/strategy/StrategyBean;Z)V
    //   556: goto -> 4
    //   559: aload_2
    //   560: iconst_0
    //   561: putfield j : Z
    //   564: goto -> 251
    //   567: astore_3
    //   568: aload_3
    //   569: invokestatic a : (Ljava/lang/Throwable;)Z
    //   572: ifne -> 343
    //   575: aload_3
    //   576: invokevirtual printStackTrace : ()V
    //   579: goto -> 343
    //   582: aload_2
    //   583: iconst_0
    //   584: putfield l : Z
    //   587: goto -> 377
    // Exception table:
    //   from	to	target	type
    //   326	332	567	java/lang/Exception
    //   337	343	567	java/lang/Exception
  }
  
  public final boolean b() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield f : Lcom/tencent/bugly/crashreport/common/strategy/StrategyBean;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnull -> 17
    //   11: iconst_1
    //   12: istore_2
    //   13: aload_0
    //   14: monitorexit
    //   15: iload_2
    //   16: ireturn
    //   17: iconst_0
    //   18: istore_2
    //   19: goto -> 13
    //   22: astore_1
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_1
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	22	finally
  }
  
  public final StrategyBean c() {
    return (this.f != null) ? this.f : this.e;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/crashreport/common/strategy/a.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */