package com.tencent.bugly.crashreport.biz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.tencent.bugly.proguard.p;
import com.tencent.bugly.proguard.t;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.util.ArrayList;
import java.util.List;

public final class a {
  private Context a;
  
  private long b;
  
  private int c;
  
  private boolean d = true;
  
  public a(Context paramContext, boolean paramBoolean) {
    this.a = paramContext;
    this.d = paramBoolean;
  }
  
  private static ContentValues a(UserInfoBean paramUserInfoBean) {
    UserInfoBean userInfoBean = null;
    if (paramUserInfoBean == null)
      return (ContentValues)userInfoBean; 
    try {
      ContentValues contentValues2 = new ContentValues();
      this();
      if (paramUserInfoBean.a > 0L)
        contentValues2.put("_id", Long.valueOf(paramUserInfoBean.a)); 
      contentValues2.put("_tm", Long.valueOf(paramUserInfoBean.e));
      contentValues2.put("_ut", Long.valueOf(paramUserInfoBean.f));
      contentValues2.put("_tp", Integer.valueOf(paramUserInfoBean.b));
      contentValues2.put("_pc", paramUserInfoBean.c);
      contentValues2.put("_dt", z.a(paramUserInfoBean));
      ContentValues contentValues1 = contentValues2;
    } catch (Throwable throwable) {
      paramUserInfoBean = userInfoBean;
    } 
    return (ContentValues)paramUserInfoBean;
  }
  
  private static UserInfoBean a(Cursor paramCursor) {
    if (paramCursor == null)
      return null; 
    try {
      byte[] arrayOfByte = paramCursor.getBlob(paramCursor.getColumnIndex("_dt"));
      if (arrayOfByte == null)
        return null; 
      long l = paramCursor.getLong(paramCursor.getColumnIndex("_id"));
      UserInfoBean userInfoBean2 = (UserInfoBean)z.a(arrayOfByte, UserInfoBean.CREATOR);
      UserInfoBean userInfoBean1 = userInfoBean2;
      if (userInfoBean2 != null) {
        userInfoBean2.a = l;
        userInfoBean1 = userInfoBean2;
      } 
    } catch (Throwable throwable) {}
    return (UserInfoBean)throwable;
  }
  
  private static void a(List<UserInfoBean> paramList) {
    if (paramList != null && paramList.size() != 0) {
      StringBuilder stringBuilder = new StringBuilder();
      for (byte b = 0; b < paramList.size() && b < 50; b++) {
        UserInfoBean userInfoBean = paramList.get(b);
        stringBuilder.append(" or _id").append(" = ").append(userInfoBean.a);
      } 
      String str2 = stringBuilder.toString();
      String str1 = str2;
      if (str2.length() > 0)
        str1 = str2.substring(4); 
      stringBuilder.setLength(0);
      try {
        x.c("[Database] deleted %s data %d", new Object[] { "t_ui", Integer.valueOf(p.a().a("t_ui", str1, null, null, true)) });
      } catch (Throwable throwable) {}
    } 
  }
  
  private void c() {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: monitorenter
    //   4: aload_0
    //   5: getfield d : Z
    //   8: istore_2
    //   9: iload_2
    //   10: ifne -> 16
    //   13: aload_0
    //   14: monitorexit
    //   15: return
    //   16: invokestatic a : ()Lcom/tencent/bugly/proguard/u;
    //   19: astore_3
    //   20: aload_3
    //   21: ifnull -> 13
    //   24: invokestatic a : ()Lcom/tencent/bugly/crashreport/common/strategy/a;
    //   27: astore #4
    //   29: aload #4
    //   31: ifnull -> 13
    //   34: aload #4
    //   36: invokevirtual b : ()Z
    //   39: ifeq -> 52
    //   42: aload_3
    //   43: sipush #1001
    //   46: invokevirtual b : (I)Z
    //   49: ifeq -> 13
    //   52: aload_0
    //   53: getfield a : Landroid/content/Context;
    //   56: invokestatic a : (Landroid/content/Context;)Lcom/tencent/bugly/crashreport/common/info/a;
    //   59: getfield d : Ljava/lang/String;
    //   62: astore #4
    //   64: new java/util/ArrayList
    //   67: astore #5
    //   69: aload #5
    //   71: invokespecial <init> : ()V
    //   74: aload_0
    //   75: aload #4
    //   77: invokevirtual a : (Ljava/lang/String;)Ljava/util/List;
    //   80: astore #4
    //   82: aload #4
    //   84: ifnull -> 467
    //   87: aload #4
    //   89: invokeinterface size : ()I
    //   94: bipush #20
    //   96: isub
    //   97: istore #6
    //   99: iload #6
    //   101: ifle -> 263
    //   104: iconst_0
    //   105: istore #7
    //   107: iload #7
    //   109: aload #4
    //   111: invokeinterface size : ()I
    //   116: iconst_1
    //   117: isub
    //   118: if_icmpge -> 230
    //   121: iload #7
    //   123: iconst_1
    //   124: iadd
    //   125: istore #8
    //   127: iload #8
    //   129: aload #4
    //   131: invokeinterface size : ()I
    //   136: if_icmpge -> 224
    //   139: aload #4
    //   141: iload #7
    //   143: invokeinterface get : (I)Ljava/lang/Object;
    //   148: checkcast com/tencent/bugly/crashreport/biz/UserInfoBean
    //   151: getfield e : J
    //   154: aload #4
    //   156: iload #8
    //   158: invokeinterface get : (I)Ljava/lang/Object;
    //   163: checkcast com/tencent/bugly/crashreport/biz/UserInfoBean
    //   166: getfield e : J
    //   169: lcmp
    //   170: ifle -> 218
    //   173: aload #4
    //   175: iload #7
    //   177: invokeinterface get : (I)Ljava/lang/Object;
    //   182: checkcast com/tencent/bugly/crashreport/biz/UserInfoBean
    //   185: astore #9
    //   187: aload #4
    //   189: iload #7
    //   191: aload #4
    //   193: iload #8
    //   195: invokeinterface get : (I)Ljava/lang/Object;
    //   200: invokeinterface set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   205: pop
    //   206: aload #4
    //   208: iload #8
    //   210: aload #9
    //   212: invokeinterface set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   217: pop
    //   218: iinc #8, 1
    //   221: goto -> 127
    //   224: iinc #7, 1
    //   227: goto -> 107
    //   230: iconst_0
    //   231: istore #7
    //   233: iload #7
    //   235: iload #6
    //   237: if_icmpge -> 263
    //   240: aload #5
    //   242: aload #4
    //   244: iload #7
    //   246: invokeinterface get : (I)Ljava/lang/Object;
    //   251: invokeinterface add : (Ljava/lang/Object;)Z
    //   256: pop
    //   257: iinc #7, 1
    //   260: goto -> 233
    //   263: aload #4
    //   265: invokeinterface iterator : ()Ljava/util/Iterator;
    //   270: astore #9
    //   272: iconst_0
    //   273: istore #7
    //   275: aload #9
    //   277: invokeinterface hasNext : ()Z
    //   282: ifeq -> 387
    //   285: aload #9
    //   287: invokeinterface next : ()Ljava/lang/Object;
    //   292: checkcast com/tencent/bugly/crashreport/biz/UserInfoBean
    //   295: astore #10
    //   297: aload #10
    //   299: getfield f : J
    //   302: ldc2_w -1
    //   305: lcmp
    //   306: ifeq -> 338
    //   309: aload #9
    //   311: invokeinterface remove : ()V
    //   316: aload #10
    //   318: getfield e : J
    //   321: invokestatic b : ()J
    //   324: lcmp
    //   325: ifge -> 338
    //   328: aload #5
    //   330: aload #10
    //   332: invokeinterface add : (Ljava/lang/Object;)Z
    //   337: pop
    //   338: aload #10
    //   340: getfield e : J
    //   343: invokestatic currentTimeMillis : ()J
    //   346: ldc2_w 600000
    //   349: lsub
    //   350: lcmp
    //   351: ifle -> 732
    //   354: aload #10
    //   356: getfield b : I
    //   359: iconst_1
    //   360: if_icmpeq -> 381
    //   363: aload #10
    //   365: getfield b : I
    //   368: iconst_4
    //   369: if_icmpeq -> 381
    //   372: aload #10
    //   374: getfield b : I
    //   377: iconst_3
    //   378: if_icmpne -> 732
    //   381: iinc #7, 1
    //   384: goto -> 275
    //   387: iload #7
    //   389: bipush #15
    //   391: if_icmple -> 726
    //   394: ldc_w '[UserInfo] Upload user info too many times in 10 min: %d'
    //   397: iconst_1
    //   398: anewarray java/lang/Object
    //   401: dup
    //   402: iconst_0
    //   403: iload #7
    //   405: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   408: aastore
    //   409: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   412: pop
    //   413: iconst_0
    //   414: istore #7
    //   416: aload #5
    //   418: invokeinterface size : ()I
    //   423: ifle -> 431
    //   426: aload #5
    //   428: invokestatic a : (Ljava/util/List;)V
    //   431: iload #7
    //   433: ifeq -> 446
    //   436: aload #4
    //   438: invokeinterface size : ()I
    //   443: ifne -> 482
    //   446: ldc_w '[UserInfo] There is no user info in local database.'
    //   449: iconst_0
    //   450: anewarray java/lang/Object
    //   453: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   456: pop
    //   457: goto -> 13
    //   460: astore #4
    //   462: aload_0
    //   463: monitorexit
    //   464: aload #4
    //   466: athrow
    //   467: new java/util/ArrayList
    //   470: dup
    //   471: invokespecial <init> : ()V
    //   474: astore #4
    //   476: iconst_1
    //   477: istore #7
    //   479: goto -> 416
    //   482: ldc_w '[UserInfo] Upload user info(size: %d)'
    //   485: iconst_1
    //   486: anewarray java/lang/Object
    //   489: dup
    //   490: iconst_0
    //   491: aload #4
    //   493: invokeinterface size : ()I
    //   498: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   501: aastore
    //   502: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   505: pop
    //   506: aload_0
    //   507: getfield c : I
    //   510: iconst_1
    //   511: if_icmpne -> 545
    //   514: iconst_1
    //   515: istore #7
    //   517: aload #4
    //   519: iload #7
    //   521: invokestatic a : (Ljava/util/List;I)Lcom/tencent/bugly/proguard/ar;
    //   524: astore #5
    //   526: aload #5
    //   528: ifnonnull -> 551
    //   531: ldc_w '[UserInfo] Failed to create UserInfoPackage.'
    //   534: iconst_0
    //   535: anewarray java/lang/Object
    //   538: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   541: pop
    //   542: goto -> 13
    //   545: iconst_2
    //   546: istore #7
    //   548: goto -> 517
    //   551: aload #5
    //   553: invokestatic a : (Lcom/tencent/bugly/proguard/k;)[B
    //   556: astore #5
    //   558: aload #5
    //   560: ifnonnull -> 577
    //   563: ldc_w '[UserInfo] Failed to encode data.'
    //   566: iconst_0
    //   567: anewarray java/lang/Object
    //   570: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   573: pop
    //   574: goto -> 13
    //   577: aload_3
    //   578: getfield a : Z
    //   581: ifeq -> 621
    //   584: sipush #840
    //   587: istore #7
    //   589: aload_0
    //   590: getfield a : Landroid/content/Context;
    //   593: iload #7
    //   595: aload #5
    //   597: invokestatic a : (Landroid/content/Context;I[B)Lcom/tencent/bugly/proguard/am;
    //   600: astore #9
    //   602: aload #9
    //   604: ifnonnull -> 629
    //   607: ldc_w '[UserInfo] Request package is null.'
    //   610: iconst_0
    //   611: anewarray java/lang/Object
    //   614: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   617: pop
    //   618: goto -> 13
    //   621: sipush #640
    //   624: istore #7
    //   626: goto -> 589
    //   629: new com/tencent/bugly/crashreport/biz/a$1
    //   632: astore #5
    //   634: aload #5
    //   636: aload_0
    //   637: aload #4
    //   639: invokespecial <init> : (Lcom/tencent/bugly/crashreport/biz/a;Ljava/util/List;)V
    //   642: invokestatic a : ()Lcom/tencent/bugly/crashreport/common/strategy/a;
    //   645: invokevirtual c : ()Lcom/tencent/bugly/crashreport/common/strategy/StrategyBean;
    //   648: astore #4
    //   650: aload_3
    //   651: getfield a : Z
    //   654: ifeq -> 709
    //   657: aload #4
    //   659: getfield r : Ljava/lang/String;
    //   662: astore #4
    //   664: aload_3
    //   665: getfield a : Z
    //   668: ifeq -> 719
    //   671: getstatic com/tencent/bugly/crashreport/common/strategy/StrategyBean.b : Ljava/lang/String;
    //   674: astore_3
    //   675: invokestatic a : ()Lcom/tencent/bugly/proguard/u;
    //   678: astore #10
    //   680: aload_0
    //   681: getfield c : I
    //   684: iconst_1
    //   685: if_icmpne -> 690
    //   688: iconst_1
    //   689: istore_1
    //   690: aload #10
    //   692: sipush #1001
    //   695: aload #9
    //   697: aload #4
    //   699: aload_3
    //   700: aload #5
    //   702: iload_1
    //   703: invokevirtual a : (ILcom/tencent/bugly/proguard/am;Ljava/lang/String;Ljava/lang/String;Lcom/tencent/bugly/proguard/t;Z)V
    //   706: goto -> 13
    //   709: aload #4
    //   711: getfield t : Ljava/lang/String;
    //   714: astore #4
    //   716: goto -> 664
    //   719: getstatic com/tencent/bugly/crashreport/common/strategy/StrategyBean.a : Ljava/lang/String;
    //   722: astore_3
    //   723: goto -> 675
    //   726: iconst_1
    //   727: istore #7
    //   729: goto -> 416
    //   732: goto -> 384
    // Exception table:
    //   from	to	target	type
    //   4	9	460	finally
    //   16	20	460	finally
    //   24	29	460	finally
    //   34	52	460	finally
    //   52	82	460	finally
    //   87	99	460	finally
    //   107	121	460	finally
    //   127	218	460	finally
    //   240	257	460	finally
    //   263	272	460	finally
    //   275	338	460	finally
    //   338	381	460	finally
    //   394	413	460	finally
    //   416	431	460	finally
    //   436	446	460	finally
    //   446	457	460	finally
    //   467	476	460	finally
    //   482	514	460	finally
    //   517	526	460	finally
    //   531	542	460	finally
    //   551	558	460	finally
    //   563	574	460	finally
    //   577	584	460	finally
    //   589	602	460	finally
    //   607	618	460	finally
    //   629	664	460	finally
    //   664	675	460	finally
    //   675	680	460	finally
    //   680	688	460	finally
    //   690	706	460	finally
    //   709	716	460	finally
    //   719	723	460	finally
  }
  
  public final List<UserInfoBean> a(String paramString) {
    Cursor cursor1;
    Cursor cursor2;
    try {
      Throwable throwable;
      Exception exception;
      if (z.a(paramString)) {
        paramString = null;
      } else {
        StringBuilder stringBuilder = new StringBuilder();
        this("_pc = '");
        paramString = stringBuilder.append(paramString).append("'").toString();
      } 
      cursor1 = p.a().a("t_ui", null, paramString, null, null, true);
      if (cursor1 == null)
        return null; 
      try {
        ArrayList<UserInfoBean> arrayList;
        StringBuilder stringBuilder = new StringBuilder();
      } catch (Throwable null) {
        return (List<UserInfoBean>)null;
      } finally {
        exception = null;
        throwable = null;
        null = exception;
        if (throwable != null)
          throwable.close(); 
      } 
      String str = exception.toString();
      return (List<UserInfoBean>)throwable;
    } catch (Throwable throwable) {
    
    } finally {
      cursor2 = null;
      if (cursor2 != null)
        cursor2.close(); 
    } 
    try {
    
    } finally {
      String str1;
      cursor2 = null;
      String str2 = paramString;
      cursor1 = cursor2;
    } 
    return (List<UserInfoBean>)cursor1;
  }
  
  public final void a() {
    this.b = z.b() + 86400000L;
    w.a().a(new b(this), this.b - System.currentTimeMillis() + 5000L);
  }
  
  public final void a(int paramInt, boolean paramBoolean, long paramLong) {
    boolean bool = true;
    com.tencent.bugly.crashreport.common.strategy.a a1 = com.tencent.bugly.crashreport.common.strategy.a.a();
    if (a1 != null && !(a1.c()).h && paramInt != 1 && paramInt != 3) {
      x.e("UserInfo is disable", new Object[0]);
      return;
    } 
    if (paramInt == 1 || paramInt == 3)
      this.c++; 
    com.tencent.bugly.crashreport.common.info.a a2 = com.tencent.bugly.crashreport.common.info.a.a(this.a);
    UserInfoBean userInfoBean = new UserInfoBean();
    userInfoBean.b = paramInt;
    userInfoBean.c = a2.d;
    userInfoBean.d = a2.g();
    userInfoBean.e = System.currentTimeMillis();
    userInfoBean.f = -1L;
    userInfoBean.n = a2.j;
    if (paramInt == 1) {
      paramInt = bool;
    } else {
      paramInt = 0;
    } 
    userInfoBean.o = paramInt;
    userInfoBean.l = a2.a();
    userInfoBean.m = a2.p;
    userInfoBean.g = a2.q;
    userInfoBean.h = a2.r;
    userInfoBean.i = a2.s;
    userInfoBean.k = a2.t;
    userInfoBean.r = a2.B();
    userInfoBean.s = a2.G();
    userInfoBean.p = a2.H();
    userInfoBean.q = a2.I();
    w.a().a(new a(this, userInfoBean, paramBoolean), 0L);
  }
  
  public final void b() {
    w w = w.a();
    if (w != null)
      w.a(new Runnable(this) {
            public final void run() {
              try {
                a.a(this.a);
              } catch (Throwable throwable) {
                x.a(throwable);
              } 
            }
          }); 
  }
  
  final class a implements Runnable {
    private boolean a;
    
    private UserInfoBean b;
    
    public a(a this$0, UserInfoBean param1UserInfoBean, boolean param1Boolean) {
      this.b = param1UserInfoBean;
      this.a = param1Boolean;
    }
    
    public final void run() {
      try {
        if (this.b != null) {
          UserInfoBean userInfoBean = this.b;
          if (userInfoBean != null) {
            com.tencent.bugly.crashreport.common.info.a a1 = com.tencent.bugly.crashreport.common.info.a.b();
            if (a1 != null)
              userInfoBean.j = a1.e(); 
          } 
          x.c("[UserInfo] Record user info.", new Object[0]);
          a.a(this.c, this.b, false);
        } 
        if (this.a) {
          a a1 = this.c;
          w w = w.a();
          if (w != null) {
            Runnable runnable = new Runnable() {
                public final void run() {
                  try {
                    a.a(this.a);
                  } catch (Throwable throwable) {
                    x.a(throwable);
                  } 
                }
              };
            super(a1);
            w.a(runnable);
          } 
        } 
      } catch (Throwable throwable) {}
    }
  }
  
  final class b implements Runnable {
    b(a this$0) {}
    
    public final void run() {
      long l = System.currentTimeMillis();
      if (l < a.b(this.a)) {
        w.a().a(new b(this.a), a.b(this.a) - l + 5000L);
        return;
      } 
      this.a.a(3, false, 0L);
      this.a.a();
    }
  }
  
  final class c implements Runnable {
    private long a = 21600000L;
    
    public c(a this$0, long param1Long) {
      this.a = param1Long;
    }
    
    public final void run() {
      a a1 = this.b;
      w w = w.a();
      if (w != null)
        w.a(new Runnable(a1) {
              public final void run() {
                try {
                  a.a(this.a);
                } catch (Throwable throwable) {
                  x.a(throwable);
                } 
              }
            }); 
      a1 = this.b;
      long l = this.a;
      w.a().a(new c(a1, l), l);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/crashreport/biz/a.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */