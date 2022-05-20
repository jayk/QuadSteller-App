package com.tencent.bugly.crashreport.crash;

import android.content.Context;
import android.os.Process;
import com.tencent.bugly.crashreport.common.info.a;
import com.tencent.bugly.crashreport.common.info.b;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.common.strategy.a;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.y;
import com.tencent.bugly.proguard.z;
import java.util.HashMap;

public final class e implements Thread.UncaughtExceptionHandler {
  private static String h = null;
  
  private static final Object i = new Object();
  
  private Context a;
  
  private b b;
  
  private a c;
  
  private a d;
  
  private Thread.UncaughtExceptionHandler e;
  
  private Thread.UncaughtExceptionHandler f;
  
  private boolean g = false;
  
  private int j;
  
  public e(Context paramContext, b paramb, a parama, a parama1) {
    this.a = paramContext;
    this.b = paramb;
    this.c = parama;
    this.d = parama1;
  }
  
  private static String a(Throwable paramThrowable, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: ifnonnull -> 8
    //   4: aconst_null
    //   5: astore_0
    //   6: aload_0
    //   7: areturn
    //   8: new java/lang/StringBuilder
    //   11: dup
    //   12: invokespecial <init> : ()V
    //   15: astore_2
    //   16: aload_0
    //   17: invokevirtual getStackTrace : ()[Ljava/lang/StackTraceElement;
    //   20: ifnull -> 133
    //   23: aload_0
    //   24: invokevirtual getStackTrace : ()[Ljava/lang/StackTraceElement;
    //   27: astore_0
    //   28: aload_0
    //   29: arraylength
    //   30: istore_3
    //   31: iconst_0
    //   32: istore #4
    //   34: iload #4
    //   36: iload_3
    //   37: if_icmpge -> 133
    //   40: aload_0
    //   41: iload #4
    //   43: aaload
    //   44: astore #5
    //   46: iload_1
    //   47: ifle -> 94
    //   50: aload_2
    //   51: invokevirtual length : ()I
    //   54: iload_1
    //   55: if_icmplt -> 94
    //   58: new java/lang/StringBuilder
    //   61: astore_0
    //   62: aload_0
    //   63: ldc '\\n[Stack over limit size :'
    //   65: invokespecial <init> : (Ljava/lang/String;)V
    //   68: aload_2
    //   69: aload_0
    //   70: iload_1
    //   71: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   74: ldc ' , has been cutted !]'
    //   76: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   79: invokevirtual toString : ()Ljava/lang/String;
    //   82: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   85: pop
    //   86: aload_2
    //   87: invokevirtual toString : ()Ljava/lang/String;
    //   90: astore_0
    //   91: goto -> 6
    //   94: aload_2
    //   95: aload #5
    //   97: invokevirtual toString : ()Ljava/lang/String;
    //   100: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   103: ldc '\\n'
    //   105: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   108: pop
    //   109: iinc #4, 1
    //   112: goto -> 34
    //   115: astore_0
    //   116: ldc 'gen stack error %s'
    //   118: iconst_1
    //   119: anewarray java/lang/Object
    //   122: dup
    //   123: iconst_0
    //   124: aload_0
    //   125: invokevirtual toString : ()Ljava/lang/String;
    //   128: aastore
    //   129: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   132: pop
    //   133: aload_2
    //   134: invokevirtual toString : ()Ljava/lang/String;
    //   137: astore_0
    //   138: goto -> 6
    // Exception table:
    //   from	to	target	type
    //   16	31	115	java/lang/Throwable
    //   50	91	115	java/lang/Throwable
    //   94	109	115	java/lang/Throwable
  }
  
  private static boolean a(Thread.UncaughtExceptionHandler paramUncaughtExceptionHandler) {
    boolean bool = true;
    if (paramUncaughtExceptionHandler == null)
      return bool; 
    String str = paramUncaughtExceptionHandler.getClass().getName();
    StackTraceElement[] arrayOfStackTraceElement = Thread.currentThread().getStackTrace();
    int i = arrayOfStackTraceElement.length;
    byte b1 = 0;
    while (true) {
      boolean bool1 = bool;
      if (b1 < i) {
        StackTraceElement stackTraceElement = arrayOfStackTraceElement[b1];
        String str2 = stackTraceElement.getClassName();
        String str1 = stackTraceElement.getMethodName();
        if (str.equals(str2) && "uncaughtException".equals(str1))
          return false; 
        b1++;
        continue;
      } 
      return bool1;
    } 
  }
  
  private static boolean a(Thread paramThread) {
    synchronized (i) {
      if (h == null || !paramThread.getName().equals(h)) {
        h = paramThread.getName();
        return false;
      } 
      return true;
    } 
  }
  
  private CrashDetailBean b(Thread paramThread, Throwable paramThrowable, boolean paramBoolean, String paramString, byte[] paramArrayOfbyte) {
    String str1;
    StringBuilder stringBuilder;
    if (paramThrowable == null) {
      x.d("We can do nothing with a null throwable.", new Object[0]);
      return null;
    } 
    boolean bool = c.a().l();
    if (bool && paramBoolean) {
      String str = " This Crash Caused By ANR , PLS To Fix ANR , This Trace May Be Not Useful![Bugly]";
    } else {
      String str = "";
    } 
    if (bool && paramBoolean)
      x.e("This Crash Caused By ANR , PLS To Fix ANR , This Trace May Be Not Useful!", new Object[0]); 
    CrashDetailBean crashDetailBean = new CrashDetailBean();
    crashDetailBean.B = b.h();
    crashDetailBean.C = b.f();
    crashDetailBean.D = b.j();
    crashDetailBean.E = this.d.p();
    crashDetailBean.F = this.d.o();
    crashDetailBean.G = this.d.q();
    crashDetailBean.w = z.a(this.a, c.e, null);
    crashDetailBean.x = y.a();
    if (crashDetailBean.x == null) {
      i = 0;
    } else {
      i = crashDetailBean.x.length;
    } 
    x.a("user log size:%d", new Object[] { Integer.valueOf(i) });
    if (paramBoolean) {
      i = 0;
    } else {
      i = 2;
    } 
    crashDetailBean.b = i;
    crashDetailBean.e = this.d.h();
    crashDetailBean.f = this.d.j;
    crashDetailBean.g = this.d.w();
    crashDetailBean.m = this.d.g();
    String str2 = paramThrowable.getClass().getName();
    String str3 = b(paramThrowable, 1000);
    String str4 = str3;
    if (str3 == null)
      str4 = ""; 
    int i = (paramThrowable.getStackTrace()).length;
    if (paramThrowable.getCause() != null) {
      bool = true;
    } else {
      bool = false;
    } 
    x.e("stack frame :%d, has cause %b", new Object[] { Integer.valueOf(i), Boolean.valueOf(bool) });
    str3 = "";
    if ((paramThrowable.getStackTrace()).length > 0)
      str3 = paramThrowable.getStackTrace()[0].toString(); 
    Throwable throwable2;
    for (throwable2 = paramThrowable; throwable2 != null && throwable2.getCause() != null; throwable2 = throwable2.getCause());
    if (throwable2 != null && throwable2 != paramThrowable) {
      crashDetailBean.n = throwable2.getClass().getName();
      crashDetailBean.o = b(throwable2, 1000);
      if (crashDetailBean.o == null)
        crashDetailBean.o = ""; 
      if ((throwable2.getStackTrace()).length > 0)
        crashDetailBean.p = throwable2.getStackTrace()[0].toString(); 
      stringBuilder = new StringBuilder();
      stringBuilder.append(str2).append(":").append(str4).append("\n");
      stringBuilder.append(str3);
      stringBuilder.append("\n......");
      stringBuilder.append("\nCaused by:\n");
      stringBuilder.append(crashDetailBean.n).append(":").append(crashDetailBean.o).append("\n");
      str1 = a(throwable2, c.f);
      stringBuilder.append(str1);
      crashDetailBean.q = stringBuilder.toString();
    } else {
      crashDetailBean.n = str2;
      crashDetailBean.o = str4 + stringBuilder;
      if (crashDetailBean.o == null)
        crashDetailBean.o = ""; 
      crashDetailBean.p = str3;
      str1 = a((Throwable)str1, c.f);
      crashDetailBean.q = str1;
    } 
    crashDetailBean.r = System.currentTimeMillis();
    crashDetailBean.u = z.b(crashDetailBean.q.getBytes());
    try {
      crashDetailBean.y = z.a(c.f, false);
      crashDetailBean.z = this.d.d;
      StringBuilder stringBuilder1 = new StringBuilder();
      this();
      crashDetailBean.A = stringBuilder1.append(paramThread.getName()).append("(").append(paramThread.getId()).append(")").toString();
      crashDetailBean.y.put(crashDetailBean.A, str1);
      crashDetailBean.H = this.d.y();
      crashDetailBean.h = this.d.v();
      crashDetailBean.i = this.d.K();
      crashDetailBean.L = this.d.a;
      crashDetailBean.M = this.d.a();
      crashDetailBean.O = this.d.H();
      crashDetailBean.P = this.d.I();
      crashDetailBean.Q = this.d.B();
      crashDetailBean.R = this.d.G();
      if (paramBoolean) {
        this.b.c(crashDetailBean);
      } else {
        boolean bool1;
        if (paramString != null && paramString.length() > 0) {
          i = 1;
        } else {
          i = 0;
        } 
        if (paramArrayOfbyte != null && paramArrayOfbyte.length > 0) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        if (i != 0) {
          crashDetailBean.N = new HashMap<String, String>(1);
          crashDetailBean.N.put("UserData", paramString);
        } 
        if (bool1)
          crashDetailBean.S = paramArrayOfbyte; 
      } 
      CrashDetailBean crashDetailBean1 = crashDetailBean;
    } catch (Throwable throwable1) {
      x.e("handle crash error %s", new Object[] { throwable1.toString() });
    } 
    return (CrashDetailBean)throwable1;
  }
  
  private static String b(Throwable paramThrowable, int paramInt) {
    return (paramThrowable.getMessage() == null) ? "" : ((paramThrowable.getMessage().length() <= 1000) ? paramThrowable.getMessage() : (paramThrowable.getMessage().substring(0, 1000) + "\n[Message over limit size:1000" + ", has been cutted!]"));
  }
  
  public final void a() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield j : I
    //   6: bipush #10
    //   8: if_icmplt -> 33
    //   11: ldc_w 'java crash handler over %d, no need set.'
    //   14: iconst_1
    //   15: anewarray java/lang/Object
    //   18: dup
    //   19: iconst_0
    //   20: bipush #10
    //   22: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   25: aastore
    //   26: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   29: pop
    //   30: aload_0
    //   31: monitorexit
    //   32: return
    //   33: aload_0
    //   34: iconst_1
    //   35: putfield g : Z
    //   38: invokestatic getDefaultUncaughtExceptionHandler : ()Ljava/lang/Thread$UncaughtExceptionHandler;
    //   41: astore_1
    //   42: aload_1
    //   43: ifnull -> 110
    //   46: aload_0
    //   47: invokevirtual getClass : ()Ljava/lang/Class;
    //   50: invokevirtual getName : ()Ljava/lang/String;
    //   53: aload_1
    //   54: invokevirtual getClass : ()Ljava/lang/Class;
    //   57: invokevirtual getName : ()Ljava/lang/String;
    //   60: invokevirtual equals : (Ljava/lang/Object;)Z
    //   63: ifne -> 30
    //   66: ldc_w 'com.android.internal.os.RuntimeInit$UncaughtHandler'
    //   69: aload_1
    //   70: invokevirtual getClass : ()Ljava/lang/Class;
    //   73: invokevirtual getName : ()Ljava/lang/String;
    //   76: invokevirtual equals : (Ljava/lang/Object;)Z
    //   79: ifeq -> 150
    //   82: ldc_w 'backup system java handler: %s'
    //   85: iconst_1
    //   86: anewarray java/lang/Object
    //   89: dup
    //   90: iconst_0
    //   91: aload_1
    //   92: invokevirtual toString : ()Ljava/lang/String;
    //   95: aastore
    //   96: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   99: pop
    //   100: aload_0
    //   101: aload_1
    //   102: putfield f : Ljava/lang/Thread$UncaughtExceptionHandler;
    //   105: aload_0
    //   106: aload_1
    //   107: putfield e : Ljava/lang/Thread$UncaughtExceptionHandler;
    //   110: aload_0
    //   111: invokestatic setDefaultUncaughtExceptionHandler : (Ljava/lang/Thread$UncaughtExceptionHandler;)V
    //   114: aload_0
    //   115: aload_0
    //   116: getfield j : I
    //   119: iconst_1
    //   120: iadd
    //   121: putfield j : I
    //   124: ldc_w 'registered java monitor: %s'
    //   127: iconst_1
    //   128: anewarray java/lang/Object
    //   131: dup
    //   132: iconst_0
    //   133: aload_0
    //   134: invokevirtual toString : ()Ljava/lang/String;
    //   137: aastore
    //   138: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   141: pop
    //   142: goto -> 30
    //   145: astore_1
    //   146: aload_0
    //   147: monitorexit
    //   148: aload_1
    //   149: athrow
    //   150: ldc_w 'backup java handler: %s'
    //   153: iconst_1
    //   154: anewarray java/lang/Object
    //   157: dup
    //   158: iconst_0
    //   159: aload_1
    //   160: invokevirtual toString : ()Ljava/lang/String;
    //   163: aastore
    //   164: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   167: pop
    //   168: aload_0
    //   169: aload_1
    //   170: putfield e : Ljava/lang/Thread$UncaughtExceptionHandler;
    //   173: goto -> 110
    // Exception table:
    //   from	to	target	type
    //   2	30	145	finally
    //   33	42	145	finally
    //   46	110	145	finally
    //   110	142	145	finally
    //   150	173	145	finally
  }
  
  public final void a(StrategyBean paramStrategyBean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ifnull -> 49
    //   6: aload_1
    //   7: getfield g : Z
    //   10: aload_0
    //   11: getfield g : Z
    //   14: if_icmpeq -> 49
    //   17: ldc_w 'java changed to %b'
    //   20: iconst_1
    //   21: anewarray java/lang/Object
    //   24: dup
    //   25: iconst_0
    //   26: aload_1
    //   27: getfield g : Z
    //   30: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   33: aastore
    //   34: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   37: pop
    //   38: aload_1
    //   39: getfield g : Z
    //   42: ifeq -> 52
    //   45: aload_0
    //   46: invokevirtual a : ()V
    //   49: aload_0
    //   50: monitorexit
    //   51: return
    //   52: aload_0
    //   53: invokevirtual b : ()V
    //   56: goto -> 49
    //   59: astore_1
    //   60: aload_0
    //   61: monitorexit
    //   62: aload_1
    //   63: athrow
    // Exception table:
    //   from	to	target	type
    //   6	49	59	finally
    //   52	56	59	finally
  }
  
  public final void a(Thread paramThread, Throwable paramThrowable, boolean paramBoolean, String paramString, byte[] paramArrayOfbyte) {
    if (paramBoolean) {
      x.e("Java Crash Happen cause by %s(%d)", new Object[] { paramThread.getName(), Long.valueOf(paramThread.getId()) });
      if (a(paramThread)) {
        x.a("this class has handled this exception", new Object[0]);
        if (this.f != null) {
          x.a("call system handler", new Object[0]);
          this.f.uncaughtException(paramThread, paramThrowable);
        } else {
          x.e("current process die", new Object[0]);
          Process.killProcess(Process.myPid());
          System.exit(1);
        } 
      } 
    } else {
      x.e("Java Catch Happen", new Object[0]);
    } 
    try {
      if (!this.g) {
        x.c("Java crash handler is disable. Just return.", new Object[0]);
        if (paramBoolean) {
          if (this.e != null && a(this.e)) {
            x.e("sys default last handle start!", new Object[0]);
            this.e.uncaughtException(paramThread, paramThrowable);
            return;
          } 
        } else {
          return;
        } 
        if (this.f != null) {
          x.e("system handle start!", new Object[0]);
          this.f.uncaughtException(paramThread, paramThrowable);
          return;
        } 
        x.e("crashreport last handle start!", new Object[0]);
        x.e("current process die", new Object[0]);
        Process.killProcess(Process.myPid());
        System.exit(1);
        return;
      } 
      if (!this.c.b()) {
        x.e("waiting for remote sync", new Object[0]);
        int i = 0;
        while (!this.c.b()) {
          z.b(500L);
          int j = i + 500;
          i = j;
          if (j >= 3000)
            break; 
        } 
      } 
      if (!this.c.b())
        x.d("no remote but still store!", new Object[0]); 
      if (!(this.c.c()).g && this.c.b()) {
        x.e("crash report was closed by remote , will not upload to Bugly , print local for helpful!", new Object[0]);
        if (paramBoolean) {
          paramString = "JAVA_CRASH";
        } else {
          paramString = "JAVA_CATCH";
        } 
        b.a(paramString, z.a(), this.d.d, paramThread, z.a(paramThrowable), null);
        return;
      } 
      CrashDetailBean crashDetailBean = b(paramThread, paramThrowable, paramBoolean, paramString, paramArrayOfbyte);
      if (crashDetailBean == null) {
        x.e("pkg crash datas fail!", new Object[0]);
        return;
      } 
      if (paramBoolean) {
        paramString = "JAVA_CRASH";
      } else {
        paramString = "JAVA_CATCH";
      } 
      b.a(paramString, z.a(), this.d.d, paramThread, z.a(paramThrowable), crashDetailBean);
      if (!this.b.a(crashDetailBean))
        this.b.a(crashDetailBean, 3000L, paramBoolean); 
      this.b.b(crashDetailBean);
      return;
    } catch (Throwable throwable) {
      if (!x.a(throwable))
        throwable.printStackTrace(); 
      return;
    } finally {
      paramString = null;
    } 
    throw paramString;
  }
  
  public final void b() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_0
    //   4: putfield g : Z
    //   7: ldc_w 'close java monitor!'
    //   10: iconst_0
    //   11: anewarray java/lang/Object
    //   14: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   17: pop
    //   18: invokestatic getDefaultUncaughtExceptionHandler : ()Ljava/lang/Thread$UncaughtExceptionHandler;
    //   21: invokevirtual getClass : ()Ljava/lang/Class;
    //   24: invokevirtual getName : ()Ljava/lang/String;
    //   27: ldc_w 'bugly'
    //   30: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   33: ifeq -> 71
    //   36: ldc_w 'Java monitor to unregister: %s'
    //   39: iconst_1
    //   40: anewarray java/lang/Object
    //   43: dup
    //   44: iconst_0
    //   45: aload_0
    //   46: invokevirtual toString : ()Ljava/lang/String;
    //   49: aastore
    //   50: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   53: pop
    //   54: aload_0
    //   55: getfield e : Ljava/lang/Thread$UncaughtExceptionHandler;
    //   58: invokestatic setDefaultUncaughtExceptionHandler : (Ljava/lang/Thread$UncaughtExceptionHandler;)V
    //   61: aload_0
    //   62: aload_0
    //   63: getfield j : I
    //   66: iconst_1
    //   67: isub
    //   68: putfield j : I
    //   71: aload_0
    //   72: monitorexit
    //   73: return
    //   74: astore_1
    //   75: aload_0
    //   76: monitorexit
    //   77: aload_1
    //   78: athrow
    // Exception table:
    //   from	to	target	type
    //   2	71	74	finally
  }
  
  public final void uncaughtException(Thread paramThread, Throwable paramThrowable) {
    synchronized (i) {
      a(paramThread, paramThrowable, true, null, null);
      return;
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/crashreport/crash/e.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */