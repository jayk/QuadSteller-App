package com.tencent.bugly.crashreport.crash.anr;

import android.content.Context;
import android.os.FileObserver;
import com.tencent.bugly.crashreport.common.info.a;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.common.strategy.a;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.crashreport.crash.c;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.y;
import com.tencent.bugly.proguard.z;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class b {
  private AtomicInteger a = new AtomicInteger(0);
  
  private long b = -1L;
  
  private final Context c;
  
  private final a d;
  
  private final w e;
  
  private final a f;
  
  private final String g;
  
  private final com.tencent.bugly.crashreport.crash.b h;
  
  private FileObserver i;
  
  private boolean j = true;
  
  public b(Context paramContext, a parama, a parama1, w paramw, com.tencent.bugly.crashreport.crash.b paramb) {
    this.c = z.a(paramContext);
    this.g = paramContext.getDir("bugly", 0).getAbsolutePath();
    this.d = parama1;
    this.e = paramw;
    this.f = parama;
    this.h = paramb;
  }
  
  private CrashDetailBean a(a parama) {
    CrashDetailBean crashDetailBean = new CrashDetailBean();
    try {
      String str;
      crashDetailBean.B = com.tencent.bugly.crashreport.common.info.b.h();
      crashDetailBean.C = com.tencent.bugly.crashreport.common.info.b.f();
      crashDetailBean.D = com.tencent.bugly.crashreport.common.info.b.j();
      crashDetailBean.E = this.d.p();
      crashDetailBean.F = this.d.o();
      crashDetailBean.G = this.d.q();
      crashDetailBean.w = z.a(this.c, c.e, null);
      crashDetailBean.b = 3;
      crashDetailBean.e = this.d.h();
      crashDetailBean.f = this.d.j;
      crashDetailBean.g = this.d.w();
      crashDetailBean.m = this.d.g();
      crashDetailBean.n = "ANR_EXCEPTION";
      crashDetailBean.o = parama.f;
      crashDetailBean.q = parama.g;
      HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
      this();
      crashDetailBean.N = hashMap;
      crashDetailBean.N.put("BUGLY_CR_01", parama.e);
      int i = -1;
      if (crashDetailBean.q != null)
        i = crashDetailBean.q.indexOf("\n"); 
      if (i > 0) {
        str = crashDetailBean.q.substring(0, i);
      } else {
        str = "GET_FAIL";
      } 
      crashDetailBean.p = str;
      crashDetailBean.r = parama.c;
      if (crashDetailBean.q != null)
        crashDetailBean.u = z.b(crashDetailBean.q.getBytes()); 
      crashDetailBean.y = parama.b;
      crashDetailBean.z = this.d.d;
      crashDetailBean.A = "main(1)";
      crashDetailBean.H = this.d.y();
      crashDetailBean.h = this.d.v();
      crashDetailBean.i = this.d.K();
      crashDetailBean.v = parama.d;
      crashDetailBean.K = this.d.n;
      crashDetailBean.L = this.d.a;
      crashDetailBean.M = this.d.a();
      crashDetailBean.O = this.d.H();
      crashDetailBean.P = this.d.I();
      crashDetailBean.Q = this.d.B();
      crashDetailBean.R = this.d.G();
      this.h.c(crashDetailBean);
      crashDetailBean.x = y.a();
    } catch (Throwable throwable) {}
    return crashDetailBean;
  }
  
  private static boolean a(String paramString1, String paramString2, String paramString3) {
    boolean bool;
    TraceFileHelper.a a1 = TraceFileHelper.readTargetDumpInfo(paramString3, paramString1, true);
    if (a1 == null || a1.d == null || a1.d.size() <= 0) {
      x.e("not found trace dump for %s", new Object[] { paramString3 });
      return false;
    } 
    File file = new File(paramString2);
    try {
      if (!file.exists()) {
        if (!file.getParentFile().exists())
          file.getParentFile().mkdirs(); 
        file.createNewFile();
      } 
      if (!file.exists() || !file.canWrite()) {
        x.e("backup file create fail %s", new Object[] { paramString2 });
        return false;
      } 
    } catch (Exception exception) {
      if (!x.a(exception))
        exception.printStackTrace(); 
      x.e("backup file create error! %s  %s", new Object[] { exception.getClass().getName() + ":" + exception.getMessage(), paramString2 });
      return false;
    } 
    paramString1 = null;
    try {
      BufferedWriter bufferedWriter = new BufferedWriter();
      FileWriter fileWriter = new FileWriter();
      this(file, false);
    } catch (IOException iOException) {
    
    } finally {
      paramString2 = null;
      if (paramString2 != null)
        try {
          paramString2.close();
        } catch (IOException iOException) {} 
    } 
    return bool;
  }
  
  private void b(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iload_1
    //   3: ifeq -> 13
    //   6: aload_0
    //   7: invokespecial c : ()V
    //   10: aload_0
    //   11: monitorexit
    //   12: return
    //   13: aload_0
    //   14: invokespecial d : ()V
    //   17: goto -> 10
    //   20: astore_2
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_2
    //   24: athrow
    // Exception table:
    //   from	to	target	type
    //   6	10	20	finally
    //   13	17	20	finally
  }
  
  private void c() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial e : ()Z
    //   6: ifeq -> 23
    //   9: ldc_w 'start when started!'
    //   12: iconst_0
    //   13: anewarray java/lang/Object
    //   16: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   19: pop
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: new com/tencent/bugly/crashreport/crash/anr/b$1
    //   26: astore_1
    //   27: aload_1
    //   28: aload_0
    //   29: ldc_w '/data/anr/'
    //   32: bipush #8
    //   34: invokespecial <init> : (Lcom/tencent/bugly/crashreport/crash/anr/b;Ljava/lang/String;I)V
    //   37: aload_0
    //   38: aload_1
    //   39: putfield i : Landroid/os/FileObserver;
    //   42: aload_0
    //   43: getfield i : Landroid/os/FileObserver;
    //   46: invokevirtual startWatching : ()V
    //   49: ldc_w 'start anr monitor!'
    //   52: iconst_0
    //   53: anewarray java/lang/Object
    //   56: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   59: pop
    //   60: aload_0
    //   61: getfield e : Lcom/tencent/bugly/proguard/w;
    //   64: astore_2
    //   65: new com/tencent/bugly/crashreport/crash/anr/b$2
    //   68: astore_1
    //   69: aload_1
    //   70: aload_0
    //   71: invokespecial <init> : (Lcom/tencent/bugly/crashreport/crash/anr/b;)V
    //   74: aload_2
    //   75: aload_1
    //   76: invokevirtual a : (Ljava/lang/Runnable;)Z
    //   79: pop
    //   80: goto -> 20
    //   83: astore_1
    //   84: aload_0
    //   85: aconst_null
    //   86: putfield i : Landroid/os/FileObserver;
    //   89: ldc_w 'start anr monitor failed!'
    //   92: iconst_0
    //   93: anewarray java/lang/Object
    //   96: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   99: pop
    //   100: aload_1
    //   101: invokestatic a : (Ljava/lang/Throwable;)Z
    //   104: ifne -> 20
    //   107: aload_1
    //   108: invokevirtual printStackTrace : ()V
    //   111: goto -> 20
    //   114: astore_1
    //   115: aload_0
    //   116: monitorexit
    //   117: aload_1
    //   118: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	114	finally
    //   23	42	114	finally
    //   42	80	83	java/lang/Throwable
    //   42	80	114	finally
    //   84	111	114	finally
  }
  
  private void c(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield j : Z
    //   6: iload_1
    //   7: if_icmpeq -> 33
    //   10: ldc_w 'user change anr %b'
    //   13: iconst_1
    //   14: anewarray java/lang/Object
    //   17: dup
    //   18: iconst_0
    //   19: iload_1
    //   20: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   23: aastore
    //   24: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   27: pop
    //   28: aload_0
    //   29: iload_1
    //   30: putfield j : Z
    //   33: aload_0
    //   34: monitorexit
    //   35: return
    //   36: astore_2
    //   37: aload_0
    //   38: monitorexit
    //   39: aload_2
    //   40: athrow
    // Exception table:
    //   from	to	target	type
    //   2	33	36	finally
  }
  
  private void d() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial e : ()Z
    //   6: ifne -> 23
    //   9: ldc_w 'close when closed!'
    //   12: iconst_0
    //   13: anewarray java/lang/Object
    //   16: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   19: pop
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: aload_0
    //   24: getfield i : Landroid/os/FileObserver;
    //   27: invokevirtual stopWatching : ()V
    //   30: aload_0
    //   31: aconst_null
    //   32: putfield i : Landroid/os/FileObserver;
    //   35: ldc_w 'close anr monitor!'
    //   38: iconst_0
    //   39: anewarray java/lang/Object
    //   42: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   45: pop
    //   46: goto -> 20
    //   49: astore_1
    //   50: ldc_w 'stop anr monitor failed!'
    //   53: iconst_0
    //   54: anewarray java/lang/Object
    //   57: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   60: pop
    //   61: aload_1
    //   62: invokestatic a : (Ljava/lang/Throwable;)Z
    //   65: ifne -> 20
    //   68: aload_1
    //   69: invokevirtual printStackTrace : ()V
    //   72: goto -> 20
    //   75: astore_1
    //   76: aload_0
    //   77: monitorexit
    //   78: aload_1
    //   79: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	75	finally
    //   23	46	49	java/lang/Throwable
    //   23	46	75	finally
    //   50	72	75	finally
  }
  
  private boolean e() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield i : Landroid/os/FileObserver;
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
  
  private boolean f() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield j : Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public final void a(StrategyBean paramStrategyBean) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_2
    //   2: aload_0
    //   3: monitorenter
    //   4: aload_1
    //   5: ifnonnull -> 11
    //   8: aload_0
    //   9: monitorexit
    //   10: return
    //   11: aload_1
    //   12: getfield j : Z
    //   15: aload_0
    //   16: invokespecial e : ()Z
    //   19: if_icmpeq -> 43
    //   22: ldc_w 'server anr changed to %b'
    //   25: iconst_1
    //   26: anewarray java/lang/Object
    //   29: dup
    //   30: iconst_0
    //   31: aload_1
    //   32: getfield j : Z
    //   35: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   38: aastore
    //   39: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   42: pop
    //   43: aload_1
    //   44: getfield j : Z
    //   47: ifeq -> 96
    //   50: aload_0
    //   51: invokespecial f : ()Z
    //   54: ifeq -> 96
    //   57: iload_2
    //   58: aload_0
    //   59: invokespecial e : ()Z
    //   62: if_icmpeq -> 8
    //   65: ldc_w 'anr changed to %b'
    //   68: iconst_1
    //   69: anewarray java/lang/Object
    //   72: dup
    //   73: iconst_0
    //   74: iload_2
    //   75: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   78: aastore
    //   79: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   82: pop
    //   83: aload_0
    //   84: iload_2
    //   85: invokespecial b : (Z)V
    //   88: goto -> 8
    //   91: astore_1
    //   92: aload_0
    //   93: monitorexit
    //   94: aload_1
    //   95: athrow
    //   96: iconst_0
    //   97: istore_2
    //   98: goto -> 57
    // Exception table:
    //   from	to	target	type
    //   11	43	91	finally
    //   43	57	91	finally
    //   57	88	91	finally
  }
  
  public final void a(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield a : Ljava/util/concurrent/atomic/AtomicInteger;
    //   6: invokevirtual get : ()I
    //   9: ifeq -> 26
    //   12: ldc_w 'trace started return '
    //   15: iconst_0
    //   16: anewarray java/lang/Object
    //   19: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   22: pop
    //   23: aload_0
    //   24: monitorexit
    //   25: return
    //   26: aload_0
    //   27: getfield a : Ljava/util/concurrent/atomic/AtomicInteger;
    //   30: iconst_1
    //   31: invokevirtual set : (I)V
    //   34: aload_0
    //   35: monitorexit
    //   36: ldc_w 'read trace first dump for create time!'
    //   39: iconst_0
    //   40: anewarray java/lang/Object
    //   43: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   46: pop
    //   47: ldc2_w -1
    //   50: lstore_2
    //   51: aload_1
    //   52: iconst_0
    //   53: invokestatic readFirstDumpInfo : (Ljava/lang/String;Z)Lcom/tencent/bugly/crashreport/crash/anr/TraceFileHelper$a;
    //   56: astore #4
    //   58: aload #4
    //   60: ifnull -> 69
    //   63: aload #4
    //   65: getfield c : J
    //   68: lstore_2
    //   69: lload_2
    //   70: ldc2_w -1
    //   73: lcmp
    //   74: ifne -> 1179
    //   77: ldc_w 'trace dump fail could not get time!'
    //   80: iconst_0
    //   81: anewarray java/lang/Object
    //   84: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   87: pop
    //   88: invokestatic currentTimeMillis : ()J
    //   91: lstore_2
    //   92: lload_2
    //   93: aload_0
    //   94: getfield b : J
    //   97: lsub
    //   98: invokestatic abs : (J)J
    //   101: ldc2_w 10000
    //   104: lcmp
    //   105: ifge -> 144
    //   108: ldc_w 'should not process ANR too Fre in %d'
    //   111: iconst_1
    //   112: anewarray java/lang/Object
    //   115: dup
    //   116: iconst_0
    //   117: sipush #10000
    //   120: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   123: aastore
    //   124: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   127: pop
    //   128: aload_0
    //   129: getfield a : Ljava/util/concurrent/atomic/AtomicInteger;
    //   132: iconst_0
    //   133: invokevirtual set : (I)V
    //   136: goto -> 25
    //   139: astore_1
    //   140: aload_0
    //   141: monitorexit
    //   142: aload_1
    //   143: athrow
    //   144: aload_0
    //   145: lload_2
    //   146: putfield b : J
    //   149: aload_0
    //   150: getfield a : Ljava/util/concurrent/atomic/AtomicInteger;
    //   153: iconst_1
    //   154: invokevirtual set : (I)V
    //   157: getstatic com/tencent/bugly/crashreport/crash/c.f : I
    //   160: iconst_0
    //   161: invokestatic a : (IZ)Ljava/util/Map;
    //   164: astore #5
    //   166: aload #5
    //   168: ifnull -> 181
    //   171: aload #5
    //   173: invokeinterface size : ()I
    //   178: ifgt -> 231
    //   181: ldc_w 'can't get all thread skip this anr'
    //   184: iconst_0
    //   185: anewarray java/lang/Object
    //   188: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   191: pop
    //   192: aload_0
    //   193: getfield a : Ljava/util/concurrent/atomic/AtomicInteger;
    //   196: iconst_0
    //   197: invokevirtual set : (I)V
    //   200: goto -> 25
    //   203: astore_1
    //   204: aload_1
    //   205: invokestatic a : (Ljava/lang/Throwable;)Z
    //   208: pop
    //   209: ldc_w 'get all thread stack fail!'
    //   212: iconst_0
    //   213: anewarray java/lang/Object
    //   216: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   219: pop
    //   220: aload_0
    //   221: getfield a : Ljava/util/concurrent/atomic/AtomicInteger;
    //   224: iconst_0
    //   225: invokevirtual set : (I)V
    //   228: goto -> 25
    //   231: aload_0
    //   232: getfield c : Landroid/content/Context;
    //   235: astore #4
    //   237: ldc2_w 10000
    //   240: lconst_0
    //   241: lcmp
    //   242: ifge -> 384
    //   245: lconst_0
    //   246: lstore #6
    //   248: ldc_w 'to find!'
    //   251: iconst_0
    //   252: anewarray java/lang/Object
    //   255: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   258: pop
    //   259: aload #4
    //   261: ldc_w 'activity'
    //   264: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   267: checkcast android/app/ActivityManager
    //   270: astore #8
    //   272: lload #6
    //   274: ldc2_w 500
    //   277: ldiv
    //   278: lstore #6
    //   280: iconst_0
    //   281: istore #9
    //   283: ldc_w 'waiting!'
    //   286: iconst_0
    //   287: anewarray java/lang/Object
    //   290: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   293: pop
    //   294: aload #8
    //   296: invokevirtual getProcessesInErrorState : ()Ljava/util/List;
    //   299: astore #4
    //   301: aload #4
    //   303: ifnull -> 392
    //   306: aload #4
    //   308: invokeinterface iterator : ()Ljava/util/Iterator;
    //   313: astore #10
    //   315: aload #10
    //   317: invokeinterface hasNext : ()Z
    //   322: ifeq -> 392
    //   325: aload #10
    //   327: invokeinterface next : ()Ljava/lang/Object;
    //   332: checkcast android/app/ActivityManager$ProcessErrorStateInfo
    //   335: astore #4
    //   337: aload #4
    //   339: getfield condition : I
    //   342: iconst_2
    //   343: if_icmpne -> 315
    //   346: ldc_w 'found!'
    //   349: iconst_0
    //   350: anewarray java/lang/Object
    //   353: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   356: pop
    //   357: aload #4
    //   359: ifnonnull -> 424
    //   362: ldc_w 'proc state is unvisiable!'
    //   365: iconst_0
    //   366: anewarray java/lang/Object
    //   369: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   372: pop
    //   373: aload_0
    //   374: getfield a : Ljava/util/concurrent/atomic/AtomicInteger;
    //   377: iconst_0
    //   378: invokevirtual set : (I)V
    //   381: goto -> 25
    //   384: ldc2_w 10000
    //   387: lstore #6
    //   389: goto -> 248
    //   392: ldc2_w 500
    //   395: invokestatic b : (J)V
    //   398: iload #9
    //   400: i2l
    //   401: lload #6
    //   403: lcmp
    //   404: iflt -> 1173
    //   407: ldc_w 'end!'
    //   410: iconst_0
    //   411: anewarray java/lang/Object
    //   414: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   417: pop
    //   418: aconst_null
    //   419: astore #4
    //   421: goto -> 357
    //   424: aload #4
    //   426: getfield pid : I
    //   429: invokestatic myPid : ()I
    //   432: if_icmpeq -> 465
    //   435: ldc_w 'not mind proc!'
    //   438: iconst_1
    //   439: anewarray java/lang/Object
    //   442: dup
    //   443: iconst_0
    //   444: aload #4
    //   446: getfield processName : Ljava/lang/String;
    //   449: aastore
    //   450: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   453: pop
    //   454: aload_0
    //   455: getfield a : Ljava/util/concurrent/atomic/AtomicInteger;
    //   458: iconst_0
    //   459: invokevirtual set : (I)V
    //   462: goto -> 25
    //   465: ldc_w 'found visiable anr , start to process!'
    //   468: iconst_0
    //   469: anewarray java/lang/Object
    //   472: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   475: pop
    //   476: aload_0
    //   477: getfield c : Landroid/content/Context;
    //   480: astore #8
    //   482: aload_0
    //   483: getfield f : Lcom/tencent/bugly/crashreport/common/strategy/a;
    //   486: invokevirtual c : ()Lcom/tencent/bugly/crashreport/common/strategy/StrategyBean;
    //   489: pop
    //   490: aload_0
    //   491: getfield f : Lcom/tencent/bugly/crashreport/common/strategy/a;
    //   494: invokevirtual b : ()Z
    //   497: ifne -> 550
    //   500: ldc_w 'waiting for remote sync'
    //   503: iconst_0
    //   504: anewarray java/lang/Object
    //   507: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   510: pop
    //   511: iconst_0
    //   512: istore #9
    //   514: aload_0
    //   515: getfield f : Lcom/tencent/bugly/crashreport/common/strategy/a;
    //   518: invokevirtual b : ()Z
    //   521: ifne -> 550
    //   524: ldc2_w 500
    //   527: invokestatic b : (J)V
    //   530: iload #9
    //   532: sipush #500
    //   535: iadd
    //   536: istore #11
    //   538: iload #11
    //   540: istore #9
    //   542: iload #11
    //   544: sipush #3000
    //   547: if_icmplt -> 514
    //   550: new java/io/File
    //   553: astore #10
    //   555: aload #8
    //   557: invokevirtual getFilesDir : ()Ljava/io/File;
    //   560: astore #8
    //   562: new java/lang/StringBuilder
    //   565: astore #12
    //   567: aload #12
    //   569: ldc_w 'bugly/bugly_trace_'
    //   572: invokespecial <init> : (Ljava/lang/String;)V
    //   575: aload #10
    //   577: aload #8
    //   579: aload #12
    //   581: lload_2
    //   582: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   585: ldc_w '.txt'
    //   588: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   591: invokevirtual toString : ()Ljava/lang/String;
    //   594: invokespecial <init> : (Ljava/io/File;Ljava/lang/String;)V
    //   597: new com/tencent/bugly/crashreport/crash/anr/a
    //   600: astore #8
    //   602: aload #8
    //   604: invokespecial <init> : ()V
    //   607: aload #8
    //   609: lload_2
    //   610: putfield c : J
    //   613: aload #8
    //   615: aload #10
    //   617: invokevirtual getAbsolutePath : ()Ljava/lang/String;
    //   620: putfield d : Ljava/lang/String;
    //   623: aload #8
    //   625: aload #4
    //   627: getfield processName : Ljava/lang/String;
    //   630: putfield a : Ljava/lang/String;
    //   633: aload #8
    //   635: aload #4
    //   637: getfield shortMsg : Ljava/lang/String;
    //   640: putfield f : Ljava/lang/String;
    //   643: aload #8
    //   645: aload #4
    //   647: getfield longMsg : Ljava/lang/String;
    //   650: putfield e : Ljava/lang/String;
    //   653: aload #8
    //   655: aload #5
    //   657: putfield b : Ljava/util/Map;
    //   660: aload #5
    //   662: ifnull -> 776
    //   665: aload #5
    //   667: invokeinterface keySet : ()Ljava/util/Set;
    //   672: invokeinterface iterator : ()Ljava/util/Iterator;
    //   677: astore #4
    //   679: aload #4
    //   681: invokeinterface hasNext : ()Z
    //   686: ifeq -> 776
    //   689: aload #4
    //   691: invokeinterface next : ()Ljava/lang/Object;
    //   696: checkcast java/lang/String
    //   699: astore #10
    //   701: aload #10
    //   703: ldc_w 'main('
    //   706: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   709: ifeq -> 679
    //   712: aload #8
    //   714: aload #5
    //   716: aload #10
    //   718: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   723: checkcast java/lang/String
    //   726: putfield g : Ljava/lang/String;
    //   729: goto -> 679
    //   732: astore_1
    //   733: aload_1
    //   734: invokestatic a : (Ljava/lang/Throwable;)Z
    //   737: ifne -> 744
    //   740: aload_1
    //   741: invokevirtual printStackTrace : ()V
    //   744: ldc_w 'handle anr error %s'
    //   747: iconst_1
    //   748: anewarray java/lang/Object
    //   751: dup
    //   752: iconst_0
    //   753: aload_1
    //   754: invokevirtual getClass : ()Ljava/lang/Class;
    //   757: invokevirtual toString : ()Ljava/lang/String;
    //   760: aastore
    //   761: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   764: pop
    //   765: aload_0
    //   766: getfield a : Ljava/util/concurrent/atomic/AtomicInteger;
    //   769: iconst_0
    //   770: invokevirtual set : (I)V
    //   773: goto -> 25
    //   776: aload #8
    //   778: getfield c : J
    //   781: lstore_2
    //   782: aload #8
    //   784: getfield d : Ljava/lang/String;
    //   787: astore #4
    //   789: aload #8
    //   791: getfield a : Ljava/lang/String;
    //   794: astore #10
    //   796: aload #8
    //   798: getfield f : Ljava/lang/String;
    //   801: astore #12
    //   803: aload #8
    //   805: getfield e : Ljava/lang/String;
    //   808: astore #5
    //   810: aload #8
    //   812: getfield b : Ljava/util/Map;
    //   815: ifnonnull -> 921
    //   818: iconst_0
    //   819: istore #9
    //   821: ldc_w 'anr tm:%d\\ntr:%s\\nproc:%s\\nsMsg:%s\\n lMsg:%s\\n threads:%d'
    //   824: bipush #6
    //   826: anewarray java/lang/Object
    //   829: dup
    //   830: iconst_0
    //   831: lload_2
    //   832: invokestatic valueOf : (J)Ljava/lang/Long;
    //   835: aastore
    //   836: dup
    //   837: iconst_1
    //   838: aload #4
    //   840: aastore
    //   841: dup
    //   842: iconst_2
    //   843: aload #10
    //   845: aastore
    //   846: dup
    //   847: iconst_3
    //   848: aload #12
    //   850: aastore
    //   851: dup
    //   852: iconst_4
    //   853: aload #5
    //   855: aastore
    //   856: dup
    //   857: iconst_5
    //   858: iload #9
    //   860: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   863: aastore
    //   864: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   867: pop
    //   868: aload_0
    //   869: getfield f : Lcom/tencent/bugly/crashreport/common/strategy/a;
    //   872: invokevirtual b : ()Z
    //   875: ifne -> 936
    //   878: ldc_w 'crash report sync remote fail, will not upload to Bugly , print local for helpful!'
    //   881: iconst_0
    //   882: anewarray java/lang/Object
    //   885: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   888: pop
    //   889: ldc_w 'ANR'
    //   892: invokestatic a : ()Ljava/lang/String;
    //   895: aload #8
    //   897: getfield a : Ljava/lang/String;
    //   900: aconst_null
    //   901: aload #8
    //   903: getfield e : Ljava/lang/String;
    //   906: aconst_null
    //   907: invokestatic a : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Thread;Ljava/lang/String;Lcom/tencent/bugly/crashreport/crash/CrashDetailBean;)V
    //   910: aload_0
    //   911: getfield a : Ljava/util/concurrent/atomic/AtomicInteger;
    //   914: iconst_0
    //   915: invokevirtual set : (I)V
    //   918: goto -> 25
    //   921: aload #8
    //   923: getfield b : Ljava/util/Map;
    //   926: invokeinterface size : ()I
    //   931: istore #9
    //   933: goto -> 821
    //   936: aload_0
    //   937: getfield f : Lcom/tencent/bugly/crashreport/common/strategy/a;
    //   940: invokevirtual c : ()Lcom/tencent/bugly/crashreport/common/strategy/StrategyBean;
    //   943: getfield j : Z
    //   946: ifne -> 974
    //   949: ldc_w 'ANR Report is closed!'
    //   952: iconst_0
    //   953: anewarray java/lang/Object
    //   956: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   959: pop
    //   960: goto -> 910
    //   963: astore_1
    //   964: aload_0
    //   965: getfield a : Ljava/util/concurrent/atomic/AtomicInteger;
    //   968: iconst_0
    //   969: invokevirtual set : (I)V
    //   972: aload_1
    //   973: athrow
    //   974: ldc_w 'found visiable anr , start to upload!'
    //   977: iconst_0
    //   978: anewarray java/lang/Object
    //   981: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   984: pop
    //   985: aload_0
    //   986: aload #8
    //   988: invokespecial a : (Lcom/tencent/bugly/crashreport/crash/anr/a;)Lcom/tencent/bugly/crashreport/crash/CrashDetailBean;
    //   991: astore #5
    //   993: aload #5
    //   995: ifnonnull -> 1012
    //   998: ldc_w 'pack anr fail!'
    //   1001: iconst_0
    //   1002: anewarray java/lang/Object
    //   1005: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   1008: pop
    //   1009: goto -> 910
    //   1012: invokestatic a : ()Lcom/tencent/bugly/crashreport/crash/c;
    //   1015: aload #5
    //   1017: invokevirtual a : (Lcom/tencent/bugly/crashreport/crash/CrashDetailBean;)V
    //   1020: aload #5
    //   1022: getfield a : J
    //   1025: lconst_0
    //   1026: lcmp
    //   1027: iflt -> 1159
    //   1030: ldc_w 'backup anr record success!'
    //   1033: iconst_0
    //   1034: anewarray java/lang/Object
    //   1037: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   1040: pop
    //   1041: aload_1
    //   1042: ifnull -> 1100
    //   1045: new java/io/File
    //   1048: astore #4
    //   1050: aload #4
    //   1052: aload_1
    //   1053: invokespecial <init> : (Ljava/lang/String;)V
    //   1056: aload #4
    //   1058: invokevirtual exists : ()Z
    //   1061: ifeq -> 1100
    //   1064: aload_0
    //   1065: getfield a : Ljava/util/concurrent/atomic/AtomicInteger;
    //   1068: iconst_3
    //   1069: invokevirtual set : (I)V
    //   1072: aload_1
    //   1073: aload #8
    //   1075: getfield d : Ljava/lang/String;
    //   1078: aload #8
    //   1080: getfield a : Ljava/lang/String;
    //   1083: invokestatic a : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z
    //   1086: ifeq -> 1100
    //   1089: ldc_w 'backup trace success'
    //   1092: iconst_0
    //   1093: anewarray java/lang/Object
    //   1096: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   1099: pop
    //   1100: ldc_w 'ANR'
    //   1103: invokestatic a : ()Ljava/lang/String;
    //   1106: aload #8
    //   1108: getfield a : Ljava/lang/String;
    //   1111: aconst_null
    //   1112: aload #8
    //   1114: getfield e : Ljava/lang/String;
    //   1117: aload #5
    //   1119: invokestatic a : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Thread;Ljava/lang/String;Lcom/tencent/bugly/crashreport/crash/CrashDetailBean;)V
    //   1122: aload_0
    //   1123: getfield h : Lcom/tencent/bugly/crashreport/crash/b;
    //   1126: aload #5
    //   1128: invokevirtual a : (Lcom/tencent/bugly/crashreport/crash/CrashDetailBean;)Z
    //   1131: ifne -> 1147
    //   1134: aload_0
    //   1135: getfield h : Lcom/tencent/bugly/crashreport/crash/b;
    //   1138: aload #5
    //   1140: ldc2_w 3000
    //   1143: iconst_1
    //   1144: invokevirtual a : (Lcom/tencent/bugly/crashreport/crash/CrashDetailBean;JZ)V
    //   1147: aload_0
    //   1148: getfield h : Lcom/tencent/bugly/crashreport/crash/b;
    //   1151: aload #5
    //   1153: invokevirtual b : (Lcom/tencent/bugly/crashreport/crash/CrashDetailBean;)V
    //   1156: goto -> 910
    //   1159: ldc_w 'backup anr record fail!'
    //   1162: iconst_0
    //   1163: anewarray java/lang/Object
    //   1166: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   1169: pop
    //   1170: goto -> 1041
    //   1173: iinc #9, 1
    //   1176: goto -> 283
    //   1179: goto -> 92
    // Exception table:
    //   from	to	target	type
    //   2	25	139	finally
    //   26	36	139	finally
    //   36	47	732	java/lang/Throwable
    //   36	47	963	finally
    //   51	58	732	java/lang/Throwable
    //   51	58	963	finally
    //   63	69	732	java/lang/Throwable
    //   63	69	963	finally
    //   77	92	732	java/lang/Throwable
    //   77	92	963	finally
    //   92	128	732	java/lang/Throwable
    //   92	128	963	finally
    //   144	157	732	java/lang/Throwable
    //   144	157	963	finally
    //   157	166	203	java/lang/Throwable
    //   157	166	963	finally
    //   171	181	732	java/lang/Throwable
    //   171	181	963	finally
    //   181	192	732	java/lang/Throwable
    //   181	192	963	finally
    //   204	220	732	java/lang/Throwable
    //   204	220	963	finally
    //   231	237	732	java/lang/Throwable
    //   231	237	963	finally
    //   248	280	732	java/lang/Throwable
    //   248	280	963	finally
    //   283	301	732	java/lang/Throwable
    //   283	301	963	finally
    //   306	315	732	java/lang/Throwable
    //   306	315	963	finally
    //   315	357	732	java/lang/Throwable
    //   315	357	963	finally
    //   362	373	732	java/lang/Throwable
    //   362	373	963	finally
    //   392	398	732	java/lang/Throwable
    //   392	398	963	finally
    //   407	418	732	java/lang/Throwable
    //   407	418	963	finally
    //   424	454	732	java/lang/Throwable
    //   424	454	963	finally
    //   465	511	732	java/lang/Throwable
    //   465	511	963	finally
    //   514	530	732	java/lang/Throwable
    //   514	530	963	finally
    //   550	660	732	java/lang/Throwable
    //   550	660	963	finally
    //   665	679	732	java/lang/Throwable
    //   665	679	963	finally
    //   679	729	732	java/lang/Throwable
    //   679	729	963	finally
    //   733	744	963	finally
    //   744	765	963	finally
    //   776	818	732	java/lang/Throwable
    //   776	818	963	finally
    //   821	910	732	java/lang/Throwable
    //   821	910	963	finally
    //   921	933	732	java/lang/Throwable
    //   921	933	963	finally
    //   936	960	732	java/lang/Throwable
    //   936	960	963	finally
    //   974	993	732	java/lang/Throwable
    //   974	993	963	finally
    //   998	1009	732	java/lang/Throwable
    //   998	1009	963	finally
    //   1012	1041	732	java/lang/Throwable
    //   1012	1041	963	finally
    //   1045	1100	732	java/lang/Throwable
    //   1045	1100	963	finally
    //   1100	1147	732	java/lang/Throwable
    //   1100	1147	963	finally
    //   1147	1156	732	java/lang/Throwable
    //   1147	1156	963	finally
    //   1159	1170	732	java/lang/Throwable
    //   1159	1170	963	finally
  }
  
  public final void a(boolean paramBoolean) {
    c(paramBoolean);
    boolean bool = f();
    a a1 = a.a();
    paramBoolean = bool;
    if (a1 != null)
      if (bool && (a1.c()).g) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      }  
    if (paramBoolean != e()) {
      x.a("anr changed to %b", new Object[] { Boolean.valueOf(paramBoolean) });
      b(paramBoolean);
    } 
  }
  
  public final boolean a() {
    return (this.a.get() != 0);
  }
  
  protected final void b() {
    // Byte code:
    //   0: invokestatic b : ()J
    //   3: lstore_1
    //   4: getstatic com/tencent/bugly/crashreport/crash/c.g : J
    //   7: lstore_3
    //   8: new java/io/File
    //   11: dup
    //   12: aload_0
    //   13: getfield g : Ljava/lang/String;
    //   16: invokespecial <init> : (Ljava/lang/String;)V
    //   19: astore #5
    //   21: aload #5
    //   23: invokevirtual exists : ()Z
    //   26: ifeq -> 55
    //   29: aload #5
    //   31: invokevirtual isDirectory : ()Z
    //   34: ifeq -> 55
    //   37: aload #5
    //   39: invokevirtual listFiles : ()[Ljava/io/File;
    //   42: astore #5
    //   44: aload #5
    //   46: ifnull -> 55
    //   49: aload #5
    //   51: arraylength
    //   52: ifne -> 56
    //   55: return
    //   56: ldc_w 'bugly_trace_'
    //   59: invokevirtual length : ()I
    //   62: istore #6
    //   64: aload #5
    //   66: arraylength
    //   67: istore #7
    //   69: iconst_0
    //   70: istore #8
    //   72: iconst_0
    //   73: istore #9
    //   75: iload #8
    //   77: iload #7
    //   79: if_icmpge -> 202
    //   82: aload #5
    //   84: iload #8
    //   86: aaload
    //   87: astore #10
    //   89: aload #10
    //   91: invokevirtual getName : ()Ljava/lang/String;
    //   94: astore #11
    //   96: iload #9
    //   98: istore #12
    //   100: aload #11
    //   102: ldc_w 'bugly_trace_'
    //   105: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   108: ifeq -> 153
    //   111: aload #11
    //   113: ldc_w '.txt'
    //   116: invokevirtual indexOf : (Ljava/lang/String;)I
    //   119: istore #12
    //   121: iload #12
    //   123: ifle -> 181
    //   126: aload #11
    //   128: iload #6
    //   130: iload #12
    //   132: invokevirtual substring : (II)Ljava/lang/String;
    //   135: invokestatic parseLong : (Ljava/lang/String;)J
    //   138: lstore #13
    //   140: lload #13
    //   142: lload_1
    //   143: lload_3
    //   144: lsub
    //   145: lcmp
    //   146: iflt -> 181
    //   149: iload #9
    //   151: istore #12
    //   153: iinc #8, 1
    //   156: iload #12
    //   158: istore #9
    //   160: goto -> 75
    //   163: astore #15
    //   165: ldc_w 'tomb format error delete %s'
    //   168: iconst_1
    //   169: anewarray java/lang/Object
    //   172: dup
    //   173: iconst_0
    //   174: aload #11
    //   176: aastore
    //   177: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   180: pop
    //   181: iload #9
    //   183: istore #12
    //   185: aload #10
    //   187: invokevirtual delete : ()Z
    //   190: ifeq -> 153
    //   193: iload #9
    //   195: iconst_1
    //   196: iadd
    //   197: istore #12
    //   199: goto -> 153
    //   202: ldc_w 'clean tombs %d'
    //   205: iconst_1
    //   206: anewarray java/lang/Object
    //   209: dup
    //   210: iconst_0
    //   211: iload #9
    //   213: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   216: aastore
    //   217: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   220: pop
    //   221: goto -> 55
    // Exception table:
    //   from	to	target	type
    //   111	121	163	java/lang/Throwable
    //   126	140	163	java/lang/Throwable
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/crashreport/crash/anr/b.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */