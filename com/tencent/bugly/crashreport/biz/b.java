package com.tencent.bugly.crashreport.biz;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.crashreport.common.info.a;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.common.strategy.a;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;

public class b {
  public static a a;
  
  private static boolean b;
  
  private static int c = 10;
  
  private static long d = 300000L;
  
  private static long e = 30000L;
  
  private static long f = 0L;
  
  private static int g;
  
  private static long h;
  
  private static long i;
  
  private static long j = 0L;
  
  private static Application.ActivityLifecycleCallbacks k = null;
  
  private static Class<?> l = null;
  
  private static boolean m = true;
  
  public static void a() {
    if (a != null)
      a.a(2, false, 0L); 
  }
  
  public static void a(long paramLong) {
    long l = paramLong;
    if (paramLong < 0L)
      l = (a.a().c()).q; 
    f = l;
  }
  
  public static void a(Context paramContext) {
    if (!b || paramContext == null)
      return; 
    Application application = null;
    if (Build.VERSION.SDK_INT >= 14) {
      if (paramContext.getApplicationContext() instanceof Application)
        application = (Application)paramContext.getApplicationContext(); 
      if (application != null)
        try {
          if (k != null)
            application.unregisterActivityLifecycleCallbacks(k); 
        } catch (Exception exception) {} 
    } 
    b = false;
  }
  
  public static void a(Context paramContext, BuglyStrategy paramBuglyStrategy) {
    if (!b) {
      long l;
      m = (a.a(paramContext)).e;
      a = new a(paramContext, m);
      b = true;
      if (paramBuglyStrategy != null) {
        l = paramBuglyStrategy.getUserInfoActivity();
        l = paramBuglyStrategy.getAppReportDelay();
      } else {
        l = 0L;
      } 
      if (l <= 0L) {
        c(paramContext, paramBuglyStrategy);
        return;
      } 
      w.a().a(new Runnable(paramContext, paramBuglyStrategy) {
            public final void run() {
              b.b(this.a, this.b);
            }
          }l);
    } 
  }
  
  public static void a(StrategyBean paramStrategyBean, boolean paramBoolean) {
    if (a != null && !paramBoolean) {
      a a1 = a;
      w w = w.a();
      if (w != null)
        w.a((Runnable)new Object(a1)); 
    } 
    if (paramStrategyBean != null) {
      if (paramStrategyBean.q > 0L)
        e = paramStrategyBean.q; 
      if (paramStrategyBean.w > 0)
        c = paramStrategyBean.w; 
      if (paramStrategyBean.x > 0L)
        d = paramStrategyBean.x; 
    } 
  }
  
  private static void c(Context paramContext, BuglyStrategy paramBuglyStrategy) {
    // Byte code:
    //   0: aload_1
    //   1: ifnull -> 444
    //   4: aload_1
    //   5: invokevirtual recordUserInfoOnceADay : ()Z
    //   8: istore_2
    //   9: aload_1
    //   10: invokevirtual isEnableUserInfo : ()Z
    //   13: istore_3
    //   14: iload_2
    //   15: ifeq -> 181
    //   18: aload_0
    //   19: invokestatic a : (Landroid/content/Context;)Lcom/tencent/bugly/crashreport/common/info/a;
    //   22: astore_1
    //   23: aload_1
    //   24: getfield d : Ljava/lang/String;
    //   27: astore #4
    //   29: getstatic com/tencent/bugly/crashreport/biz/b.a : Lcom/tencent/bugly/crashreport/biz/a;
    //   32: aload #4
    //   34: invokevirtual a : (Ljava/lang/String;)Ljava/util/List;
    //   37: astore #4
    //   39: aload #4
    //   41: ifnull -> 173
    //   44: iconst_0
    //   45: istore #5
    //   47: iload #5
    //   49: aload #4
    //   51: invokeinterface size : ()I
    //   56: if_icmpge -> 173
    //   59: aload #4
    //   61: iload #5
    //   63: invokeinterface get : (I)Ljava/lang/Object;
    //   68: checkcast com/tencent/bugly/crashreport/biz/UserInfoBean
    //   71: astore #6
    //   73: aload #6
    //   75: getfield n : Ljava/lang/String;
    //   78: aload_1
    //   79: getfield j : Ljava/lang/String;
    //   82: invokevirtual equals : (Ljava/lang/Object;)Z
    //   85: ifeq -> 167
    //   88: aload #6
    //   90: getfield b : I
    //   93: iconst_1
    //   94: if_icmpne -> 167
    //   97: invokestatic b : ()J
    //   100: lstore #7
    //   102: lload #7
    //   104: lconst_0
    //   105: lcmp
    //   106: ifle -> 173
    //   109: aload #6
    //   111: getfield e : J
    //   114: lload #7
    //   116: lcmp
    //   117: iflt -> 167
    //   120: aload #6
    //   122: getfield f : J
    //   125: lconst_0
    //   126: lcmp
    //   127: ifgt -> 158
    //   130: getstatic com/tencent/bugly/crashreport/biz/b.a : Lcom/tencent/bugly/crashreport/biz/a;
    //   133: astore_1
    //   134: invokestatic a : ()Lcom/tencent/bugly/proguard/w;
    //   137: astore #4
    //   139: aload #4
    //   141: ifnull -> 158
    //   144: aload #4
    //   146: new com/tencent/bugly/crashreport/biz/a$2
    //   149: dup
    //   150: aload_1
    //   151: invokespecial <init> : (Lcom/tencent/bugly/crashreport/biz/a;)V
    //   154: invokevirtual a : (Ljava/lang/Runnable;)Z
    //   157: pop
    //   158: iconst_0
    //   159: istore #5
    //   161: iload #5
    //   163: ifne -> 179
    //   166: return
    //   167: iinc #5, 1
    //   170: goto -> 47
    //   173: iconst_1
    //   174: istore #5
    //   176: goto -> 161
    //   179: iconst_0
    //   180: istore_3
    //   181: invokestatic b : ()Lcom/tencent/bugly/crashreport/common/info/a;
    //   184: astore #4
    //   186: aload #4
    //   188: ifnull -> 288
    //   191: iconst_0
    //   192: istore #9
    //   194: invokestatic currentThread : ()Ljava/lang/Thread;
    //   197: invokevirtual getStackTrace : ()[Ljava/lang/StackTraceElement;
    //   200: astore #6
    //   202: aload #6
    //   204: arraylength
    //   205: istore #10
    //   207: aconst_null
    //   208: astore_1
    //   209: iconst_0
    //   210: istore #5
    //   212: iload #5
    //   214: iload #10
    //   216: if_icmpge -> 267
    //   219: aload #6
    //   221: iload #5
    //   223: aaload
    //   224: astore #11
    //   226: aload #11
    //   228: invokevirtual getMethodName : ()Ljava/lang/String;
    //   231: ldc 'onCreate'
    //   233: invokevirtual equals : (Ljava/lang/Object;)Z
    //   236: ifeq -> 245
    //   239: aload #11
    //   241: invokevirtual getClassName : ()Ljava/lang/String;
    //   244: astore_1
    //   245: aload #11
    //   247: invokevirtual getClassName : ()Ljava/lang/String;
    //   250: ldc 'android.app.Activity'
    //   252: invokevirtual equals : (Ljava/lang/Object;)Z
    //   255: ifeq -> 261
    //   258: iconst_1
    //   259: istore #9
    //   261: iinc #5, 1
    //   264: goto -> 212
    //   267: aload_1
    //   268: ifnull -> 422
    //   271: iload #9
    //   273: ifeq -> 415
    //   276: aload #4
    //   278: iconst_1
    //   279: invokevirtual a : (Z)V
    //   282: aload #4
    //   284: aload_1
    //   285: putfield p : Ljava/lang/String;
    //   288: iload_3
    //   289: ifeq -> 349
    //   292: aconst_null
    //   293: astore_1
    //   294: getstatic android/os/Build$VERSION.SDK_INT : I
    //   297: bipush #14
    //   299: if_icmplt -> 349
    //   302: aload_0
    //   303: invokevirtual getApplicationContext : ()Landroid/content/Context;
    //   306: instanceof android/app/Application
    //   309: ifeq -> 320
    //   312: aload_0
    //   313: invokevirtual getApplicationContext : ()Landroid/content/Context;
    //   316: checkcast android/app/Application
    //   319: astore_1
    //   320: aload_1
    //   321: ifnull -> 349
    //   324: getstatic com/tencent/bugly/crashreport/biz/b.k : Landroid/app/Application$ActivityLifecycleCallbacks;
    //   327: ifnonnull -> 342
    //   330: new com/tencent/bugly/crashreport/biz/b$2
    //   333: astore_0
    //   334: aload_0
    //   335: invokespecial <init> : ()V
    //   338: aload_0
    //   339: putstatic com/tencent/bugly/crashreport/biz/b.k : Landroid/app/Application$ActivityLifecycleCallbacks;
    //   342: aload_1
    //   343: getstatic com/tencent/bugly/crashreport/biz/b.k : Landroid/app/Application$ActivityLifecycleCallbacks;
    //   346: invokevirtual registerActivityLifecycleCallbacks : (Landroid/app/Application$ActivityLifecycleCallbacks;)V
    //   349: getstatic com/tencent/bugly/crashreport/biz/b.m : Z
    //   352: ifeq -> 166
    //   355: invokestatic currentTimeMillis : ()J
    //   358: putstatic com/tencent/bugly/crashreport/biz/b.i : J
    //   361: getstatic com/tencent/bugly/crashreport/biz/b.a : Lcom/tencent/bugly/crashreport/biz/a;
    //   364: iconst_1
    //   365: iconst_0
    //   366: lconst_0
    //   367: invokevirtual a : (IZJ)V
    //   370: ldc_w '[session] launch app, new start'
    //   373: iconst_0
    //   374: anewarray java/lang/Object
    //   377: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   380: pop
    //   381: getstatic com/tencent/bugly/crashreport/biz/b.a : Lcom/tencent/bugly/crashreport/biz/a;
    //   384: invokevirtual a : ()V
    //   387: getstatic com/tencent/bugly/crashreport/biz/b.a : Lcom/tencent/bugly/crashreport/biz/a;
    //   390: astore_0
    //   391: invokestatic a : ()Lcom/tencent/bugly/proguard/w;
    //   394: new com/tencent/bugly/crashreport/biz/a$c
    //   397: dup
    //   398: aload_0
    //   399: ldc2_w 21600000
    //   402: invokespecial <init> : (Lcom/tencent/bugly/crashreport/biz/a;J)V
    //   405: ldc2_w 21600000
    //   408: invokevirtual a : (Ljava/lang/Runnable;J)Z
    //   411: pop
    //   412: goto -> 166
    //   415: ldc_w 'background'
    //   418: astore_1
    //   419: goto -> 282
    //   422: ldc_w 'unknown'
    //   425: astore_1
    //   426: goto -> 282
    //   429: astore_0
    //   430: aload_0
    //   431: invokestatic a : (Ljava/lang/Throwable;)Z
    //   434: ifne -> 349
    //   437: aload_0
    //   438: invokevirtual printStackTrace : ()V
    //   441: goto -> 349
    //   444: iconst_1
    //   445: istore_3
    //   446: iconst_0
    //   447: istore_2
    //   448: goto -> 14
    // Exception table:
    //   from	to	target	type
    //   324	342	429	java/lang/Exception
    //   342	349	429	java/lang/Exception
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/crashreport/biz/b.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */