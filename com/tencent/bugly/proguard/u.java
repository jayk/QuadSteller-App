package com.tencent.bugly.proguard;

import android.content.Context;
import android.os.Process;
import android.util.Base64;
import com.tencent.bugly.b;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public final class u {
  private static u b = null;
  
  public boolean a = true;
  
  private final p c;
  
  private final Context d;
  
  private Map<Integer, Long> e = new HashMap<Integer, Long>();
  
  private long f;
  
  private long g;
  
  private LinkedBlockingQueue<Runnable> h = new LinkedBlockingQueue<Runnable>();
  
  private LinkedBlockingQueue<Runnable> i = new LinkedBlockingQueue<Runnable>();
  
  private final Object j = new Object();
  
  private String k = null;
  
  private byte[] l = null;
  
  private long m = 0L;
  
  private byte[] n = null;
  
  private long o = 0L;
  
  private String p = null;
  
  private long q = 0L;
  
  private final Object r = new Object();
  
  private boolean s = false;
  
  private final Object t = new Object();
  
  private int u = 0;
  
  private u(Context paramContext) {
    this.d = paramContext;
    this.c = p.a();
    try {
      Class.forName("android.util.Base64");
    } catch (ClassNotFoundException classNotFoundException) {
      x.a("[UploadManager] Error: Can not find Base64 class, will not use stronger security way to upload", new Object[0]);
      this.a = false;
    } 
    if (this.a) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDP9x32s5pPtZBXzJBz2GWM/sbTvVO2+RvW0PH01IdaBxc/").append("fB6fbHZocC9T3nl1+J5eAFjIRVuV8vHDky7Qo82Mnh0PVvcZIEQvMMVKU8dsMQopxgsOs2gkSHJwgWdinKNS8CmWobo6pFwPUW11lMv714jAUZRq2GBOqiO2vQI6iwIDAQAB");
      this.k = stringBuilder.toString();
    } 
  }
  
  public static u a() {
    // Byte code:
    //   0: ldc com/tencent/bugly/proguard/u
    //   2: monitorenter
    //   3: getstatic com/tencent/bugly/proguard/u.b : Lcom/tencent/bugly/proguard/u;
    //   6: astore_0
    //   7: ldc com/tencent/bugly/proguard/u
    //   9: monitorexit
    //   10: aload_0
    //   11: areturn
    //   12: astore_0
    //   13: ldc com/tencent/bugly/proguard/u
    //   15: monitorexit
    //   16: aload_0
    //   17: athrow
    // Exception table:
    //   from	to	target	type
    //   3	7	12	finally
  }
  
  public static u a(Context paramContext) {
    // Byte code:
    //   0: ldc com/tencent/bugly/proguard/u
    //   2: monitorenter
    //   3: getstatic com/tencent/bugly/proguard/u.b : Lcom/tencent/bugly/proguard/u;
    //   6: ifnonnull -> 22
    //   9: new com/tencent/bugly/proguard/u
    //   12: astore_1
    //   13: aload_1
    //   14: aload_0
    //   15: invokespecial <init> : (Landroid/content/Context;)V
    //   18: aload_1
    //   19: putstatic com/tencent/bugly/proguard/u.b : Lcom/tencent/bugly/proguard/u;
    //   22: getstatic com/tencent/bugly/proguard/u.b : Lcom/tencent/bugly/proguard/u;
    //   25: astore_0
    //   26: ldc com/tencent/bugly/proguard/u
    //   28: monitorexit
    //   29: aload_0
    //   30: areturn
    //   31: astore_0
    //   32: ldc com/tencent/bugly/proguard/u
    //   34: monitorexit
    //   35: aload_0
    //   36: athrow
    // Exception table:
    //   from	to	target	type
    //   3	22	31	finally
    //   22	26	31	finally
  }
  
  private void a(Runnable paramRunnable, long paramLong) {
    if (paramRunnable == null) {
      x.d("[UploadManager] Upload task should not be null", new Object[0]);
      return;
    } 
    x.c("[UploadManager] Execute synchronized upload task (pid=%d | tid=%d)", new Object[] { Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
    Thread thread = z.a(paramRunnable, "BUGLY_SYNC_UPLOAD");
    if (thread == null) {
      x.e("[UploadManager] Failed to start a thread to execute synchronized upload task, add it to queue.", new Object[0]);
      a(paramRunnable, true);
      return;
    } 
    try {
      thread.join(paramLong);
    } catch (Throwable throwable) {
      x.e("[UploadManager] Failed to join upload synchronized task with message: %s. Add it to queue.", new Object[] { throwable.getMessage() });
      a(paramRunnable, true);
      c(0);
    } 
  }
  
  private void a(Runnable paramRunnable, boolean paramBoolean1, boolean paramBoolean2, long paramLong) {
    if (paramRunnable == null)
      x.d("[UploadManager] Upload task should not be null", new Object[0]); 
    x.c("[UploadManager] Add upload task (pid=%d | tid=%d)", new Object[] { Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
    if (this.p != null) {
      if (b()) {
        x.c("[UploadManager] Sucessfully got session ID, try to execute upload task now (pid=%d | tid=%d)", new Object[] { Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
        if (paramBoolean2) {
          a(paramRunnable, paramLong);
          return;
        } 
        a(paramRunnable, paramBoolean1);
        c(0);
        return;
      } 
      x.a("[UploadManager] Session ID is expired, drop it (pid=%d | tid=%d)", new Object[] { Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
      b(false);
    } 
    synchronized (this.t) {
      if (this.s) {
        a(paramRunnable, paramBoolean1);
        return;
      } 
    } 
    this.s = true;
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_6} */
    x.c("[UploadManager] Initialize security context now (pid=%d | tid=%d)", new Object[] { Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
    if (paramBoolean2) {
      a(new a(this, this.d, paramRunnable, paramLong), 0L);
      return;
    } 
    a(paramRunnable, paramBoolean1);
    paramRunnable = new a(this, this.d);
    x.a("[UploadManager] Create and start a new thread to execute a task of initializing security context: %s", new Object[] { "BUGLY_ASYNC_UPLOAD" });
    if (z.a(paramRunnable, "BUGLY_ASYNC_UPLOAD") == null) {
      x.d("[UploadManager] Failed to start a thread to execute task of initializing security context, try to post it into thread pool.", new Object[0]);
      w w = w.a();
      if (w != null) {
        w.a(paramRunnable);
        return;
      } 
      x.e("[UploadManager] Asynchronous thread pool is unavailable now, try next time.", new Object[0]);
      synchronized (this.t) {
        this.s = false;
        return;
      } 
    } 
  }
  
  private boolean a(Runnable paramRunnable, boolean paramBoolean) {
    boolean bool = false;
    if (paramRunnable == null) {
      x.a("[UploadManager] Upload task should not be null", new Object[0]);
      return bool;
    } 
    try {
      x.c("[UploadManager] Add upload task to queue (pid=%d | tid=%d)", new Object[] { Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
      Object object = this.j;
      /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
      if (paramBoolean) {
        try {
          this.h.put(paramRunnable);
          /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
          paramBoolean = true;
        } finally {}
        return paramBoolean;
      } 
      this.i.put(paramRunnable);
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
      paramBoolean = true;
    } catch (Throwable throwable) {
      x.e("[UploadManager] Failed to add upload task to queue: %s", new Object[] { throwable.getMessage() });
      paramBoolean = bool;
    } 
    return paramBoolean;
  }
  
  private void c(int paramInt) {
    // Byte code:
    //   0: iload_1
    //   1: ifge -> 15
    //   4: ldc '[UploadManager] Number of task to execute should >= 0'
    //   6: iconst_0
    //   7: anewarray java/lang/Object
    //   10: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   13: pop
    //   14: return
    //   15: invokestatic a : ()Lcom/tencent/bugly/proguard/w;
    //   18: astore_2
    //   19: new java/util/concurrent/LinkedBlockingQueue
    //   22: dup
    //   23: invokespecial <init> : ()V
    //   26: astore_3
    //   27: new java/util/concurrent/LinkedBlockingQueue
    //   30: dup
    //   31: invokespecial <init> : ()V
    //   34: astore #4
    //   36: aload_0
    //   37: getfield j : Ljava/lang/Object;
    //   40: astore #5
    //   42: aload #5
    //   44: monitorenter
    //   45: ldc '[UploadManager] Try to poll all upload task need and put them into temp queue (pid=%d | tid=%d)'
    //   47: iconst_2
    //   48: anewarray java/lang/Object
    //   51: dup
    //   52: iconst_0
    //   53: invokestatic myPid : ()I
    //   56: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   59: aastore
    //   60: dup
    //   61: iconst_1
    //   62: invokestatic myTid : ()I
    //   65: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   68: aastore
    //   69: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   72: pop
    //   73: aload_0
    //   74: getfield h : Ljava/util/concurrent/LinkedBlockingQueue;
    //   77: invokevirtual size : ()I
    //   80: istore #6
    //   82: aload_0
    //   83: getfield i : Ljava/util/concurrent/LinkedBlockingQueue;
    //   86: invokevirtual size : ()I
    //   89: istore #7
    //   91: iload #6
    //   93: ifne -> 125
    //   96: iload #7
    //   98: ifne -> 125
    //   101: ldc '[UploadManager] There is no upload task in queue.'
    //   103: iconst_0
    //   104: anewarray java/lang/Object
    //   107: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   110: pop
    //   111: aload #5
    //   113: monitorexit
    //   114: goto -> 14
    //   117: astore #4
    //   119: aload #5
    //   121: monitorexit
    //   122: aload #4
    //   124: athrow
    //   125: iload_1
    //   126: ifeq -> 588
    //   129: iload_1
    //   130: iload #6
    //   132: if_icmpge -> 204
    //   135: iconst_0
    //   136: istore #7
    //   138: iload_1
    //   139: istore #6
    //   141: iload #7
    //   143: istore_1
    //   144: aload_2
    //   145: ifnull -> 155
    //   148: aload_2
    //   149: invokevirtual c : ()Z
    //   152: ifne -> 585
    //   155: iconst_0
    //   156: istore_1
    //   157: iconst_0
    //   158: istore #7
    //   160: iload #7
    //   162: iload #6
    //   164: if_icmpge -> 245
    //   167: aload_0
    //   168: getfield h : Ljava/util/concurrent/LinkedBlockingQueue;
    //   171: invokevirtual peek : ()Ljava/lang/Object;
    //   174: checkcast java/lang/Runnable
    //   177: astore #8
    //   179: aload #8
    //   181: ifnull -> 245
    //   184: aload_3
    //   185: aload #8
    //   187: invokevirtual put : (Ljava/lang/Object;)V
    //   190: aload_0
    //   191: getfield h : Ljava/util/concurrent/LinkedBlockingQueue;
    //   194: invokevirtual poll : ()Ljava/lang/Object;
    //   197: pop
    //   198: iinc #7, 1
    //   201: goto -> 160
    //   204: iload_1
    //   205: iload #6
    //   207: iload #7
    //   209: iadd
    //   210: if_icmpge -> 588
    //   213: iload_1
    //   214: iload #6
    //   216: isub
    //   217: istore_1
    //   218: goto -> 144
    //   221: astore #8
    //   223: ldc_w '[UploadManager] Failed to add upload task to temp urgent queue: %s'
    //   226: iconst_1
    //   227: anewarray java/lang/Object
    //   230: dup
    //   231: iconst_0
    //   232: aload #8
    //   234: invokevirtual getMessage : ()Ljava/lang/String;
    //   237: aastore
    //   238: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   241: pop
    //   242: goto -> 198
    //   245: iconst_0
    //   246: istore #7
    //   248: iload #7
    //   250: iload_1
    //   251: if_icmpge -> 316
    //   254: aload_0
    //   255: getfield i : Ljava/util/concurrent/LinkedBlockingQueue;
    //   258: invokevirtual peek : ()Ljava/lang/Object;
    //   261: checkcast java/lang/Runnable
    //   264: astore #8
    //   266: aload #8
    //   268: ifnull -> 316
    //   271: aload #4
    //   273: aload #8
    //   275: invokevirtual put : (Ljava/lang/Object;)V
    //   278: aload_0
    //   279: getfield i : Ljava/util/concurrent/LinkedBlockingQueue;
    //   282: invokevirtual poll : ()Ljava/lang/Object;
    //   285: pop
    //   286: iinc #7, 1
    //   289: goto -> 248
    //   292: astore #8
    //   294: ldc_w '[UploadManager] Failed to add upload task to temp urgent queue: %s'
    //   297: iconst_1
    //   298: anewarray java/lang/Object
    //   301: dup
    //   302: iconst_0
    //   303: aload #8
    //   305: invokevirtual getMessage : ()Ljava/lang/String;
    //   308: aastore
    //   309: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   312: pop
    //   313: goto -> 286
    //   316: aload #5
    //   318: monitorexit
    //   319: iload #6
    //   321: ifle -> 361
    //   324: ldc_w '[UploadManager] Execute urgent upload tasks of queue which has %d tasks (pid=%d | tid=%d)'
    //   327: iconst_3
    //   328: anewarray java/lang/Object
    //   331: dup
    //   332: iconst_0
    //   333: iload #6
    //   335: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   338: aastore
    //   339: dup
    //   340: iconst_1
    //   341: invokestatic myPid : ()I
    //   344: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   347: aastore
    //   348: dup
    //   349: iconst_2
    //   350: invokestatic myTid : ()I
    //   353: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   356: aastore
    //   357: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   360: pop
    //   361: iconst_0
    //   362: istore #7
    //   364: iload #7
    //   366: iload #6
    //   368: if_icmpge -> 522
    //   371: aload_3
    //   372: invokevirtual poll : ()Ljava/lang/Object;
    //   375: checkcast java/lang/Runnable
    //   378: astore #8
    //   380: aload #8
    //   382: ifnull -> 522
    //   385: aload_0
    //   386: getfield j : Ljava/lang/Object;
    //   389: astore #5
    //   391: aload #5
    //   393: monitorenter
    //   394: aload_0
    //   395: getfield u : I
    //   398: iconst_2
    //   399: if_icmplt -> 422
    //   402: aload_2
    //   403: ifnull -> 422
    //   406: aload_2
    //   407: aload #8
    //   409: invokevirtual a : (Ljava/lang/Runnable;)Z
    //   412: pop
    //   413: aload #5
    //   415: monitorexit
    //   416: iinc #7, 1
    //   419: goto -> 364
    //   422: aload #5
    //   424: monitorexit
    //   425: ldc_w '[UploadManager] Create and start a new thread to execute a upload task: %s'
    //   428: iconst_1
    //   429: anewarray java/lang/Object
    //   432: dup
    //   433: iconst_0
    //   434: ldc 'BUGLY_ASYNC_UPLOAD'
    //   436: aastore
    //   437: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   440: pop
    //   441: new com/tencent/bugly/proguard/u$1
    //   444: dup
    //   445: aload_0
    //   446: aload #8
    //   448: invokespecial <init> : (Lcom/tencent/bugly/proguard/u;Ljava/lang/Runnable;)V
    //   451: ldc 'BUGLY_ASYNC_UPLOAD'
    //   453: invokestatic a : (Ljava/lang/Runnable;Ljava/lang/String;)Ljava/lang/Thread;
    //   456: ifnull -> 500
    //   459: aload_0
    //   460: getfield j : Ljava/lang/Object;
    //   463: astore #5
    //   465: aload #5
    //   467: monitorenter
    //   468: aload_0
    //   469: aload_0
    //   470: getfield u : I
    //   473: iconst_1
    //   474: iadd
    //   475: putfield u : I
    //   478: aload #5
    //   480: monitorexit
    //   481: goto -> 416
    //   484: astore #4
    //   486: aload #5
    //   488: monitorexit
    //   489: aload #4
    //   491: athrow
    //   492: astore #4
    //   494: aload #5
    //   496: monitorexit
    //   497: aload #4
    //   499: athrow
    //   500: ldc_w '[UploadManager] Failed to start a thread to execute asynchronous upload task, will try again next time.'
    //   503: iconst_0
    //   504: anewarray java/lang/Object
    //   507: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   510: pop
    //   511: aload_0
    //   512: aload #8
    //   514: iconst_1
    //   515: invokespecial a : (Ljava/lang/Runnable;Z)Z
    //   518: pop
    //   519: goto -> 416
    //   522: iload_1
    //   523: ifle -> 562
    //   526: ldc_w '[UploadManager] Execute upload tasks of queue which has %d tasks (pid=%d | tid=%d)'
    //   529: iconst_3
    //   530: anewarray java/lang/Object
    //   533: dup
    //   534: iconst_0
    //   535: iload_1
    //   536: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   539: aastore
    //   540: dup
    //   541: iconst_1
    //   542: invokestatic myPid : ()I
    //   545: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   548: aastore
    //   549: dup
    //   550: iconst_2
    //   551: invokestatic myTid : ()I
    //   554: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   557: aastore
    //   558: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   561: pop
    //   562: aload_2
    //   563: ifnull -> 14
    //   566: aload_2
    //   567: new com/tencent/bugly/proguard/u$2
    //   570: dup
    //   571: aload_0
    //   572: iload_1
    //   573: aload #4
    //   575: invokespecial <init> : (Lcom/tencent/bugly/proguard/u;ILjava/util/concurrent/LinkedBlockingQueue;)V
    //   578: invokevirtual a : (Ljava/lang/Runnable;)Z
    //   581: pop
    //   582: goto -> 14
    //   585: goto -> 157
    //   588: iload #7
    //   590: istore_1
    //   591: goto -> 144
    // Exception table:
    //   from	to	target	type
    //   45	91	117	finally
    //   101	114	117	finally
    //   148	155	117	finally
    //   167	179	117	finally
    //   184	198	221	java/lang/Throwable
    //   184	198	117	finally
    //   223	242	117	finally
    //   254	266	117	finally
    //   271	286	292	java/lang/Throwable
    //   271	286	117	finally
    //   294	313	117	finally
    //   316	319	117	finally
    //   394	402	492	finally
    //   406	416	492	finally
    //   468	481	484	finally
  }
  
  private static boolean c() {
    boolean bool = false;
    x.c("[UploadManager] Drop security info of database (pid=%d | tid=%d)", new Object[] { Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
    try {
      p p1 = p.a();
      if (p1 == null) {
        x.d("[UploadManager] Failed to get Database", new Object[0]);
        return bool;
      } 
      boolean bool1 = p1.a(555, "security_info", (o)null, true);
      bool = bool1;
    } catch (Throwable throwable) {
      x.a(throwable);
    } 
    return bool;
  }
  
  private boolean d() {
    boolean bool;
    x.c("[UploadManager] Record security info to database (pid=%d | tid=%d)", new Object[] { Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
    try {
      p p1 = p.a();
      if (p1 == null) {
        x.d("[UploadManager] Failed to get database", new Object[0]);
        return false;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      this();
      if (this.n != null) {
        stringBuilder.append(Base64.encodeToString(this.n, 0));
        stringBuilder.append("#");
        if (this.o != 0L) {
          stringBuilder.append(Long.toString(this.o));
        } else {
          stringBuilder.append("null");
        } 
        stringBuilder.append("#");
        if (this.p != null) {
          stringBuilder.append(this.p);
        } else {
          stringBuilder.append("null");
        } 
        stringBuilder.append("#");
        if (this.q != 0L) {
          stringBuilder.append(Long.toString(this.q));
        } else {
          stringBuilder.append("null");
        } 
        p1.a(555, "security_info", stringBuilder.toString().getBytes(), (o)null, true);
        return true;
      } 
      x.c("[UploadManager] AES key is null, will not record", new Object[0]);
      bool = false;
    } catch (Throwable throwable) {
      x.a(throwable);
      c();
      bool = false;
    } 
    return bool;
  }
  
  private boolean e() {
    // Byte code:
    //   0: ldc_w '[UploadManager] Load security info from database (pid=%d | tid=%d)'
    //   3: iconst_2
    //   4: anewarray java/lang/Object
    //   7: dup
    //   8: iconst_0
    //   9: invokestatic myPid : ()I
    //   12: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   15: aastore
    //   16: dup
    //   17: iconst_1
    //   18: invokestatic myTid : ()I
    //   21: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   24: aastore
    //   25: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   28: pop
    //   29: invokestatic a : ()Lcom/tencent/bugly/proguard/p;
    //   32: astore_1
    //   33: aload_1
    //   34: ifnonnull -> 52
    //   37: ldc_w '[UploadManager] Failed to get database'
    //   40: iconst_0
    //   41: anewarray java/lang/Object
    //   44: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   47: pop
    //   48: iconst_0
    //   49: istore_2
    //   50: iload_2
    //   51: ireturn
    //   52: aload_1
    //   53: sipush #555
    //   56: aconst_null
    //   57: iconst_1
    //   58: invokevirtual a : (ILcom/tencent/bugly/proguard/o;Z)Ljava/util/Map;
    //   61: astore_1
    //   62: aload_1
    //   63: ifnull -> 299
    //   66: aload_1
    //   67: ldc_w 'security_info'
    //   70: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   75: ifeq -> 299
    //   78: new java/lang/String
    //   81: astore_3
    //   82: aload_3
    //   83: aload_1
    //   84: ldc_w 'security_info'
    //   87: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   92: checkcast [B
    //   95: invokespecial <init> : ([B)V
    //   98: aload_3
    //   99: ldc_w '#'
    //   102: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
    //   105: astore_1
    //   106: aload_1
    //   107: arraylength
    //   108: iconst_4
    //   109: if_icmpne -> 340
    //   112: aload_1
    //   113: iconst_0
    //   114: aaload
    //   115: invokevirtual isEmpty : ()Z
    //   118: ifne -> 380
    //   121: aload_1
    //   122: iconst_0
    //   123: aaload
    //   124: ldc_w 'null'
    //   127: invokevirtual equals : (Ljava/lang/Object;)Z
    //   130: istore_2
    //   131: iload_2
    //   132: ifne -> 380
    //   135: aload_0
    //   136: aload_1
    //   137: iconst_0
    //   138: aaload
    //   139: iconst_0
    //   140: invokestatic decode : (Ljava/lang/String;I)[B
    //   143: putfield n : [B
    //   146: iconst_0
    //   147: istore #4
    //   149: iload #4
    //   151: istore #5
    //   153: iload #4
    //   155: ifne -> 203
    //   158: iload #4
    //   160: istore #5
    //   162: aload_1
    //   163: iconst_1
    //   164: aaload
    //   165: invokevirtual isEmpty : ()Z
    //   168: ifne -> 203
    //   171: aload_1
    //   172: iconst_1
    //   173: aaload
    //   174: ldc_w 'null'
    //   177: invokevirtual equals : (Ljava/lang/Object;)Z
    //   180: istore_2
    //   181: iload #4
    //   183: istore #5
    //   185: iload_2
    //   186: ifne -> 203
    //   189: aload_0
    //   190: aload_1
    //   191: iconst_1
    //   192: aaload
    //   193: invokestatic parseLong : (Ljava/lang/String;)J
    //   196: putfield o : J
    //   199: iload #4
    //   201: istore #5
    //   203: iload #5
    //   205: ifne -> 236
    //   208: aload_1
    //   209: iconst_2
    //   210: aaload
    //   211: invokevirtual isEmpty : ()Z
    //   214: ifne -> 236
    //   217: aload_1
    //   218: iconst_2
    //   219: aaload
    //   220: ldc_w 'null'
    //   223: invokevirtual equals : (Ljava/lang/Object;)Z
    //   226: ifne -> 236
    //   229: aload_0
    //   230: aload_1
    //   231: iconst_2
    //   232: aaload
    //   233: putfield p : Ljava/lang/String;
    //   236: iload #5
    //   238: istore #4
    //   240: iload #5
    //   242: ifne -> 290
    //   245: iload #5
    //   247: istore #4
    //   249: aload_1
    //   250: iconst_3
    //   251: aaload
    //   252: invokevirtual isEmpty : ()Z
    //   255: ifne -> 290
    //   258: aload_1
    //   259: iconst_3
    //   260: aaload
    //   261: ldc_w 'null'
    //   264: invokevirtual equals : (Ljava/lang/Object;)Z
    //   267: istore_2
    //   268: iload #5
    //   270: istore #4
    //   272: iload_2
    //   273: ifne -> 290
    //   276: aload_0
    //   277: aload_1
    //   278: iconst_3
    //   279: aaload
    //   280: invokestatic parseLong : (Ljava/lang/String;)J
    //   283: putfield q : J
    //   286: iload #5
    //   288: istore #4
    //   290: iload #4
    //   292: ifeq -> 299
    //   295: invokestatic c : ()Z
    //   298: pop
    //   299: iconst_1
    //   300: istore_2
    //   301: goto -> 50
    //   304: astore_3
    //   305: aload_3
    //   306: invokestatic a : (Ljava/lang/Throwable;)Z
    //   309: pop
    //   310: iconst_1
    //   311: istore #4
    //   313: goto -> 149
    //   316: astore_3
    //   317: aload_3
    //   318: invokestatic a : (Ljava/lang/Throwable;)Z
    //   321: pop
    //   322: iconst_1
    //   323: istore #5
    //   325: goto -> 203
    //   328: astore_1
    //   329: aload_1
    //   330: invokestatic a : (Ljava/lang/Throwable;)Z
    //   333: pop
    //   334: iconst_1
    //   335: istore #4
    //   337: goto -> 290
    //   340: ldc_w 'SecurityInfo = %s, Strings.length = %d'
    //   343: iconst_2
    //   344: anewarray java/lang/Object
    //   347: dup
    //   348: iconst_0
    //   349: aload_3
    //   350: aastore
    //   351: dup
    //   352: iconst_1
    //   353: aload_1
    //   354: arraylength
    //   355: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   358: aastore
    //   359: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   362: pop
    //   363: iconst_1
    //   364: istore #4
    //   366: goto -> 290
    //   369: astore_1
    //   370: aload_1
    //   371: invokestatic a : (Ljava/lang/Throwable;)Z
    //   374: pop
    //   375: iconst_0
    //   376: istore_2
    //   377: goto -> 50
    //   380: iconst_0
    //   381: istore #4
    //   383: goto -> 149
    // Exception table:
    //   from	to	target	type
    //   29	33	369	java/lang/Throwable
    //   37	48	369	java/lang/Throwable
    //   52	62	369	java/lang/Throwable
    //   66	131	369	java/lang/Throwable
    //   135	146	304	java/lang/Throwable
    //   162	181	369	java/lang/Throwable
    //   189	199	316	java/lang/Throwable
    //   208	236	369	java/lang/Throwable
    //   249	268	369	java/lang/Throwable
    //   276	286	328	java/lang/Throwable
    //   295	299	369	java/lang/Throwable
    //   305	310	369	java/lang/Throwable
    //   317	322	369	java/lang/Throwable
    //   329	334	369	java/lang/Throwable
    //   340	363	369	java/lang/Throwable
  }
  
  public final long a(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: lconst_0
    //   3: lstore_2
    //   4: iload_1
    //   5: iflt -> 191
    //   8: aload_0
    //   9: getfield e : Ljava/util/Map;
    //   12: iload_1
    //   13: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   16: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   21: checkcast java/lang/Long
    //   24: astore #4
    //   26: aload #4
    //   28: ifnull -> 43
    //   31: aload #4
    //   33: invokevirtual longValue : ()J
    //   36: lstore #5
    //   38: aload_0
    //   39: monitorexit
    //   40: lload #5
    //   42: lreturn
    //   43: aload_0
    //   44: getfield c : Lcom/tencent/bugly/proguard/p;
    //   47: iload_1
    //   48: invokevirtual a : (I)Ljava/util/List;
    //   51: astore #4
    //   53: lload_2
    //   54: lstore #5
    //   56: aload #4
    //   58: ifnull -> 38
    //   61: lload_2
    //   62: lstore #5
    //   64: aload #4
    //   66: invokeinterface size : ()I
    //   71: ifle -> 38
    //   74: aload #4
    //   76: invokeinterface size : ()I
    //   81: iconst_1
    //   82: if_icmple -> 158
    //   85: aload #4
    //   87: invokeinterface iterator : ()Ljava/util/Iterator;
    //   92: astore #7
    //   94: lload_2
    //   95: lstore #5
    //   97: aload #7
    //   99: invokeinterface hasNext : ()Z
    //   104: ifeq -> 140
    //   107: aload #7
    //   109: invokeinterface next : ()Ljava/lang/Object;
    //   114: checkcast com/tencent/bugly/proguard/r
    //   117: astore #4
    //   119: aload #4
    //   121: getfield e : J
    //   124: lload #5
    //   126: lcmp
    //   127: ifle -> 215
    //   130: aload #4
    //   132: getfield e : J
    //   135: lstore #5
    //   137: goto -> 97
    //   140: aload_0
    //   141: getfield c : Lcom/tencent/bugly/proguard/p;
    //   144: iload_1
    //   145: invokevirtual b : (I)V
    //   148: goto -> 38
    //   151: astore #4
    //   153: aload_0
    //   154: monitorexit
    //   155: aload #4
    //   157: athrow
    //   158: aload #4
    //   160: iconst_0
    //   161: invokeinterface get : (I)Ljava/lang/Object;
    //   166: checkcast com/tencent/bugly/proguard/r
    //   169: getfield e : J
    //   172: lstore #5
    //   174: goto -> 38
    //   177: astore #4
    //   179: aload #4
    //   181: invokestatic a : (Ljava/lang/Throwable;)Z
    //   184: pop
    //   185: lload_2
    //   186: lstore #5
    //   188: goto -> 38
    //   191: ldc_w '[UploadManager] Unknown upload ID: %d'
    //   194: iconst_1
    //   195: anewarray java/lang/Object
    //   198: dup
    //   199: iconst_0
    //   200: iload_1
    //   201: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   204: aastore
    //   205: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   208: pop
    //   209: lload_2
    //   210: lstore #5
    //   212: goto -> 38
    //   215: goto -> 137
    // Exception table:
    //   from	to	target	type
    //   8	26	151	finally
    //   31	38	151	finally
    //   43	53	151	finally
    //   64	94	151	finally
    //   97	137	151	finally
    //   140	148	151	finally
    //   158	174	177	java/lang/Throwable
    //   158	174	151	finally
    //   179	185	151	finally
    //   191	209	151	finally
  }
  
  public final long a(boolean paramBoolean) {
    byte b;
    long l1 = 0L;
    long l2 = z.b();
    if (paramBoolean) {
      b = 5;
    } else {
      b = 3;
    } 
    List<r> list = this.c.a(b);
    if (list != null && list.size() > 0) {
      long l5;
      long l4 = l1;
      try {
        r r = list.get(0);
        l5 = l1;
        l4 = l1;
        if (r.e >= l2) {
          l4 = l1;
          l5 = z.c(r.g);
          if (b == 3) {
            l4 = l5;
            this.f = l5;
          } else {
            l4 = l5;
            this.g = l5;
          } 
          l4 = l5;
          list.remove(r);
        } 
      } catch (Throwable throwable) {
        x.a(throwable);
        l5 = l4;
      } 
      l4 = l5;
      if (list.size() > 0) {
        this.c.a(list);
        l4 = l5;
      } 
      x.c("[UploadManager] Local network consume: %d KB", new Object[] { Long.valueOf(l4 / 1024L) });
      return l4;
    } 
    if (paramBoolean) {
      long l = this.g;
      x.c("[UploadManager] Local network consume: %d KB", new Object[] { Long.valueOf(l / 1024L) });
      return l;
    } 
    long l3 = this.f;
    x.c("[UploadManager] Local network consume: %d KB", new Object[] { Long.valueOf(l3 / 1024L) });
    return l3;
  }
  
  public final void a(int paramInt1, int paramInt2, byte[] paramArrayOfbyte, String paramString1, String paramString2, t paramt, int paramInt3, int paramInt4, boolean paramBoolean, Map<String, String> paramMap) {
    try {
      v v = new v();
      this(this.d, paramInt1, paramInt2, paramArrayOfbyte, paramString1, paramString2, paramt, this.a, paramInt3, paramInt4, false, paramMap);
      a(v, paramBoolean, false, 0L);
    } catch (Throwable throwable) {}
  }
  
  public final void a(int paramInt, long paramLong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iload_1
    //   3: iflt -> 116
    //   6: aload_0
    //   7: getfield e : Ljava/util/Map;
    //   10: iload_1
    //   11: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   14: lload_2
    //   15: invokestatic valueOf : (J)Ljava/lang/Long;
    //   18: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   23: pop
    //   24: new com/tencent/bugly/proguard/r
    //   27: astore #4
    //   29: aload #4
    //   31: invokespecial <init> : ()V
    //   34: aload #4
    //   36: iload_1
    //   37: putfield b : I
    //   40: aload #4
    //   42: lload_2
    //   43: putfield e : J
    //   46: aload #4
    //   48: ldc_w ''
    //   51: putfield c : Ljava/lang/String;
    //   54: aload #4
    //   56: ldc_w ''
    //   59: putfield d : Ljava/lang/String;
    //   62: aload #4
    //   64: iconst_0
    //   65: newarray byte
    //   67: putfield g : [B
    //   70: aload_0
    //   71: getfield c : Lcom/tencent/bugly/proguard/p;
    //   74: iload_1
    //   75: invokevirtual b : (I)V
    //   78: aload_0
    //   79: getfield c : Lcom/tencent/bugly/proguard/p;
    //   82: aload #4
    //   84: invokevirtual a : (Lcom/tencent/bugly/proguard/r;)Z
    //   87: pop
    //   88: ldc_w '[UploadManager] Uploading(ID:%d) time: %s'
    //   91: iconst_2
    //   92: anewarray java/lang/Object
    //   95: dup
    //   96: iconst_0
    //   97: iload_1
    //   98: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   101: aastore
    //   102: dup
    //   103: iconst_1
    //   104: lload_2
    //   105: invokestatic a : (J)Ljava/lang/String;
    //   108: aastore
    //   109: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   112: pop
    //   113: aload_0
    //   114: monitorexit
    //   115: return
    //   116: ldc_w '[UploadManager] Unknown uploading ID: %d'
    //   119: iconst_1
    //   120: anewarray java/lang/Object
    //   123: dup
    //   124: iconst_0
    //   125: iload_1
    //   126: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   129: aastore
    //   130: invokestatic e : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   133: pop
    //   134: goto -> 113
    //   137: astore #4
    //   139: aload_0
    //   140: monitorexit
    //   141: aload #4
    //   143: athrow
    // Exception table:
    //   from	to	target	type
    //   6	113	137	finally
    //   116	134	137	finally
  }
  
  public final void a(int paramInt, am paramam, String paramString1, String paramString2, t paramt, long paramLong, boolean paramBoolean) {
    int i = paramam.g;
    byte[] arrayOfByte = a.a(paramam);
    try {
      v v = new v();
      this(this.d, paramInt, i, arrayOfByte, paramString1, paramString2, paramt, this.a, paramBoolean);
      a(v, true, true, paramLong);
    } catch (Throwable throwable) {}
  }
  
  public final void a(int paramInt, am paramam, String paramString1, String paramString2, t paramt, boolean paramBoolean) {
    a(paramInt, paramam.g, a.a(paramam), paramString1, paramString2, paramt, 0, 0, paramBoolean, null);
  }
  
  public final void a(int paramInt, an paraman) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_3
    //   2: iconst_1
    //   3: istore #4
    //   5: iconst_1
    //   6: istore #5
    //   8: aload_0
    //   9: getfield a : Z
    //   12: ifne -> 16
    //   15: return
    //   16: iload_1
    //   17: iconst_2
    //   18: if_icmpne -> 99
    //   21: ldc_w '[UploadManager] Session ID is invalid, will clear security context (pid=%d | tid=%d)'
    //   24: iconst_2
    //   25: anewarray java/lang/Object
    //   28: dup
    //   29: iconst_0
    //   30: invokestatic myPid : ()I
    //   33: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   36: aastore
    //   37: dup
    //   38: iconst_1
    //   39: invokestatic myTid : ()I
    //   42: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   45: aastore
    //   46: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   49: pop
    //   50: aload_0
    //   51: iconst_1
    //   52: invokevirtual b : (Z)V
    //   55: aload_0
    //   56: getfield t : Ljava/lang/Object;
    //   59: astore #6
    //   61: aload #6
    //   63: monitorenter
    //   64: aload_0
    //   65: getfield s : Z
    //   68: ifeq -> 87
    //   71: aload_0
    //   72: iconst_0
    //   73: putfield s : Z
    //   76: aload_0
    //   77: getfield d : Landroid/content/Context;
    //   80: ldc_w 'security_info'
    //   83: invokestatic b : (Landroid/content/Context;Ljava/lang/String;)Z
    //   86: pop
    //   87: aload #6
    //   89: monitorexit
    //   90: goto -> 15
    //   93: astore_2
    //   94: aload #6
    //   96: monitorexit
    //   97: aload_2
    //   98: athrow
    //   99: aload_0
    //   100: getfield t : Ljava/lang/Object;
    //   103: astore #6
    //   105: aload #6
    //   107: monitorenter
    //   108: aload_0
    //   109: getfield s : Z
    //   112: ifne -> 127
    //   115: aload #6
    //   117: monitorexit
    //   118: goto -> 15
    //   121: astore_2
    //   122: aload #6
    //   124: monitorexit
    //   125: aload_2
    //   126: athrow
    //   127: aload #6
    //   129: monitorexit
    //   130: aload_2
    //   131: ifnull -> 559
    //   134: ldc_w '[UploadManager] Record security context (pid=%d | tid=%d)'
    //   137: iconst_2
    //   138: anewarray java/lang/Object
    //   141: dup
    //   142: iconst_0
    //   143: invokestatic myPid : ()I
    //   146: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   149: aastore
    //   150: dup
    //   151: iconst_1
    //   152: invokestatic myTid : ()I
    //   155: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   158: aastore
    //   159: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   162: pop
    //   163: iload #4
    //   165: istore #7
    //   167: aload_2
    //   168: getfield g : Ljava/util/Map;
    //   171: astore #6
    //   173: iload_3
    //   174: istore_1
    //   175: aload #6
    //   177: ifnull -> 464
    //   180: iload_3
    //   181: istore_1
    //   182: iload #4
    //   184: istore #7
    //   186: aload #6
    //   188: ldc_w 'S1'
    //   191: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   196: ifeq -> 464
    //   199: iload_3
    //   200: istore_1
    //   201: iload #4
    //   203: istore #7
    //   205: aload #6
    //   207: ldc_w 'S2'
    //   210: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   215: ifeq -> 464
    //   218: iload #4
    //   220: istore #7
    //   222: aload_0
    //   223: aload_2
    //   224: getfield e : J
    //   227: invokestatic currentTimeMillis : ()J
    //   230: lsub
    //   231: putfield m : J
    //   234: iload #4
    //   236: istore #7
    //   238: ldc_w '[UploadManager] Time lag of server is: %d'
    //   241: iconst_1
    //   242: anewarray java/lang/Object
    //   245: dup
    //   246: iconst_0
    //   247: aload_0
    //   248: getfield m : J
    //   251: invokestatic valueOf : (J)Ljava/lang/Long;
    //   254: aastore
    //   255: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   258: pop
    //   259: iload #4
    //   261: istore #7
    //   263: aload_0
    //   264: aload #6
    //   266: ldc_w 'S1'
    //   269: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   274: checkcast java/lang/String
    //   277: putfield p : Ljava/lang/String;
    //   280: iload #4
    //   282: istore #7
    //   284: ldc_w '[UploadManager] Session ID from server is: %s'
    //   287: iconst_1
    //   288: anewarray java/lang/Object
    //   291: dup
    //   292: iconst_0
    //   293: aload_0
    //   294: getfield p : Ljava/lang/String;
    //   297: aastore
    //   298: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   301: pop
    //   302: iload #4
    //   304: istore #7
    //   306: aload_0
    //   307: getfield p : Ljava/lang/String;
    //   310: invokevirtual length : ()I
    //   313: istore_1
    //   314: iload_1
    //   315: ifle -> 539
    //   318: iload #4
    //   320: istore #7
    //   322: aload_0
    //   323: aload #6
    //   325: ldc_w 'S2'
    //   328: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   333: checkcast java/lang/String
    //   336: invokestatic parseLong : (Ljava/lang/String;)J
    //   339: putfield q : J
    //   342: iload #4
    //   344: istore #7
    //   346: aload_0
    //   347: getfield q : J
    //   350: lstore #8
    //   352: iload #4
    //   354: istore #7
    //   356: new java/util/Date
    //   359: astore_2
    //   360: iload #4
    //   362: istore #7
    //   364: aload_2
    //   365: aload_0
    //   366: getfield q : J
    //   369: invokespecial <init> : (J)V
    //   372: iload #4
    //   374: istore #7
    //   376: ldc_w '[UploadManager] Session expired time from server is: %d(%s)'
    //   379: iconst_2
    //   380: anewarray java/lang/Object
    //   383: dup
    //   384: iconst_0
    //   385: lload #8
    //   387: invokestatic valueOf : (J)Ljava/lang/Long;
    //   390: aastore
    //   391: dup
    //   392: iconst_1
    //   393: aload_2
    //   394: invokevirtual toString : ()Ljava/lang/String;
    //   397: aastore
    //   398: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   401: pop
    //   402: iload #4
    //   404: istore #7
    //   406: aload_0
    //   407: getfield q : J
    //   410: ldc2_w 1000
    //   413: lcmp
    //   414: ifge -> 443
    //   417: iload #4
    //   419: istore #7
    //   421: ldc_w '[UploadManager] Session expired time from server is less than 1 second, will set to default value'
    //   424: iconst_0
    //   425: anewarray java/lang/Object
    //   428: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   431: pop
    //   432: iload #4
    //   434: istore #7
    //   436: aload_0
    //   437: ldc2_w 259200000
    //   440: putfield q : J
    //   443: iload #4
    //   445: istore #7
    //   447: aload_0
    //   448: invokespecial d : ()Z
    //   451: ifeq -> 518
    //   454: iconst_0
    //   455: istore_1
    //   456: iload_1
    //   457: istore #7
    //   459: aload_0
    //   460: iconst_0
    //   461: invokespecial c : (I)V
    //   464: iload_1
    //   465: ifeq -> 55
    //   468: aload_0
    //   469: iconst_0
    //   470: invokevirtual b : (Z)V
    //   473: goto -> 55
    //   476: astore_2
    //   477: iload #4
    //   479: istore #7
    //   481: ldc_w '[UploadManager] Session expired time is invalid, will set to default value'
    //   484: iconst_0
    //   485: anewarray java/lang/Object
    //   488: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   491: pop
    //   492: iload #4
    //   494: istore #7
    //   496: aload_0
    //   497: ldc2_w 259200000
    //   500: putfield q : J
    //   503: goto -> 443
    //   506: astore_2
    //   507: aload_2
    //   508: invokestatic a : (Ljava/lang/Throwable;)Z
    //   511: pop
    //   512: iload #7
    //   514: istore_1
    //   515: goto -> 464
    //   518: iload #4
    //   520: istore #7
    //   522: ldc_w '[UploadManager] Failed to record database'
    //   525: iconst_0
    //   526: anewarray java/lang/Object
    //   529: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   532: pop
    //   533: iload #5
    //   535: istore_1
    //   536: goto -> 456
    //   539: iload #4
    //   541: istore #7
    //   543: ldc_w '[UploadManager] Session ID from server is invalid, try next time'
    //   546: iconst_0
    //   547: anewarray java/lang/Object
    //   550: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   553: pop
    //   554: iload_3
    //   555: istore_1
    //   556: goto -> 464
    //   559: ldc_w '[UploadManager] Fail to init security context and clear local info (pid=%d | tid=%d)'
    //   562: iconst_2
    //   563: anewarray java/lang/Object
    //   566: dup
    //   567: iconst_0
    //   568: invokestatic myPid : ()I
    //   571: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   574: aastore
    //   575: dup
    //   576: iconst_1
    //   577: invokestatic myTid : ()I
    //   580: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   583: aastore
    //   584: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   587: pop
    //   588: aload_0
    //   589: iconst_0
    //   590: invokevirtual b : (Z)V
    //   593: goto -> 55
    // Exception table:
    //   from	to	target	type
    //   64	87	93	finally
    //   87	90	93	finally
    //   108	118	121	finally
    //   167	173	506	java/lang/Throwable
    //   186	199	506	java/lang/Throwable
    //   205	218	506	java/lang/Throwable
    //   222	234	506	java/lang/Throwable
    //   238	259	506	java/lang/Throwable
    //   263	280	506	java/lang/Throwable
    //   284	302	506	java/lang/Throwable
    //   306	314	506	java/lang/Throwable
    //   322	342	476	java/lang/NumberFormatException
    //   322	342	506	java/lang/Throwable
    //   346	352	476	java/lang/NumberFormatException
    //   346	352	506	java/lang/Throwable
    //   356	360	476	java/lang/NumberFormatException
    //   356	360	506	java/lang/Throwable
    //   364	372	476	java/lang/NumberFormatException
    //   364	372	506	java/lang/Throwable
    //   376	402	476	java/lang/NumberFormatException
    //   376	402	506	java/lang/Throwable
    //   406	417	476	java/lang/NumberFormatException
    //   406	417	506	java/lang/Throwable
    //   421	432	476	java/lang/NumberFormatException
    //   421	432	506	java/lang/Throwable
    //   436	443	476	java/lang/NumberFormatException
    //   436	443	506	java/lang/Throwable
    //   447	454	506	java/lang/Throwable
    //   459	464	506	java/lang/Throwable
    //   481	492	506	java/lang/Throwable
    //   496	503	506	java/lang/Throwable
    //   522	533	506	java/lang/Throwable
    //   543	554	506	java/lang/Throwable
  }
  
  protected final void a(long paramLong, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iload_3
    //   3: ifeq -> 112
    //   6: iconst_5
    //   7: istore #4
    //   9: new com/tencent/bugly/proguard/r
    //   12: astore #5
    //   14: aload #5
    //   16: invokespecial <init> : ()V
    //   19: aload #5
    //   21: iload #4
    //   23: putfield b : I
    //   26: aload #5
    //   28: invokestatic b : ()J
    //   31: putfield e : J
    //   34: aload #5
    //   36: ldc_w ''
    //   39: putfield c : Ljava/lang/String;
    //   42: aload #5
    //   44: ldc_w ''
    //   47: putfield d : Ljava/lang/String;
    //   50: aload #5
    //   52: lload_1
    //   53: invokestatic c : (J)[B
    //   56: putfield g : [B
    //   59: aload_0
    //   60: getfield c : Lcom/tencent/bugly/proguard/p;
    //   63: iload #4
    //   65: invokevirtual b : (I)V
    //   68: aload_0
    //   69: getfield c : Lcom/tencent/bugly/proguard/p;
    //   72: aload #5
    //   74: invokevirtual a : (Lcom/tencent/bugly/proguard/r;)Z
    //   77: pop
    //   78: iload_3
    //   79: ifeq -> 118
    //   82: aload_0
    //   83: lload_1
    //   84: putfield g : J
    //   87: ldc_w '[UploadManager] Network total consume: %d KB'
    //   90: iconst_1
    //   91: anewarray java/lang/Object
    //   94: dup
    //   95: iconst_0
    //   96: lload_1
    //   97: ldc2_w 1024
    //   100: ldiv
    //   101: invokestatic valueOf : (J)Ljava/lang/Long;
    //   104: aastore
    //   105: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   108: pop
    //   109: aload_0
    //   110: monitorexit
    //   111: return
    //   112: iconst_3
    //   113: istore #4
    //   115: goto -> 9
    //   118: aload_0
    //   119: lload_1
    //   120: putfield f : J
    //   123: goto -> 87
    //   126: astore #5
    //   128: aload_0
    //   129: monitorexit
    //   130: aload #5
    //   132: athrow
    // Exception table:
    //   from	to	target	type
    //   9	78	126	finally
    //   82	87	126	finally
    //   87	109	126	finally
    //   118	123	126	finally
  }
  
  public final boolean a(Map<String, String> paramMap) {
    boolean bool = false;
    if (paramMap != null) {
      x.c("[UploadManager] Integrate security to HTTP headers (pid=%d | tid=%d)", new Object[] { Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
      if (this.p != null) {
        paramMap.put("secureSessionId", this.p);
        return true;
      } 
      if (this.n == null || this.n.length << 3 != 128) {
        x.d("[UploadManager] AES key is invalid", new Object[0]);
        return bool;
      } 
      if (this.l == null) {
        this.l = Base64.decode(this.k, 0);
        if (this.l == null) {
          x.d("[UploadManager] Failed to decode RSA public key", new Object[0]);
          return bool;
        } 
      } 
      byte[] arrayOfByte = z.b(1, this.n, this.l);
      if (arrayOfByte == null) {
        x.d("[UploadManager] Failed to encrypt AES key", new Object[0]);
        return bool;
      } 
      String str = Base64.encodeToString(arrayOfByte, 0);
      if (str == null) {
        x.d("[UploadManager] Failed to encode AES key", new Object[0]);
        return bool;
      } 
      paramMap.put("raKey", str);
      bool = true;
    } 
    return bool;
  }
  
  public final byte[] a(byte[] paramArrayOfbyte) {
    if (this.n == null || this.n.length << 3 != 128) {
      x.d("[UploadManager] AES key is invalid (pid=%d | tid=%d)", new Object[] { Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
      return null;
    } 
    return z.a(1, paramArrayOfbyte, this.n);
  }
  
  protected final void b(boolean paramBoolean) {
    synchronized (this.r) {
      x.c("[UploadManager] Clear security context (pid=%d | tid=%d)", new Object[] { Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
      this.n = null;
      this.p = null;
      this.q = 0L;
      if (paramBoolean)
        c(); 
      return;
    } 
  }
  
  protected final boolean b() {
    boolean bool = false;
    null = bool;
    if (this.p != null) {
      if (this.q == 0L)
        return bool; 
    } else {
      return null;
    } 
    long l = System.currentTimeMillis() + this.m;
    if (this.q < l) {
      x.c("[UploadManager] Session ID expired time from server is: %d(%s), but now is: %d(%s)", new Object[] { Long.valueOf(this.q), (new Date(this.q)).toString(), Long.valueOf(l), (new Date(l)).toString() });
      return bool;
    } 
    return true;
  }
  
  public final boolean b(int paramInt) {
    boolean bool = true;
    if (b.c) {
      x.c("Uploading frequency will not be checked if SDK is in debug mode.", new Object[0]);
      return bool;
    } 
    long l = System.currentTimeMillis() - a(paramInt);
    x.c("[UploadManager] Time interval is %d seconds since last uploading(ID: %d).", new Object[] { Long.valueOf(l / 1000L), Integer.valueOf(paramInt) });
    if (l < 30000L) {
      x.a("[UploadManager] Data only be uploaded once in %d seconds.", new Object[] { Long.valueOf(30L) });
      bool = false;
    } 
    return bool;
  }
  
  public final byte[] b(byte[] paramArrayOfbyte) {
    if (this.n == null || this.n.length << 3 != 128) {
      x.d("[UploadManager] AES key is invalid (pid=%d | tid=%d)", new Object[] { Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
      return null;
    } 
    return z.a(2, paramArrayOfbyte, this.n);
  }
  
  final class a implements Runnable {
    private final Context a;
    
    private final Runnable b;
    
    private final long c;
    
    public a(u this$0, Context param1Context) {
      this.a = param1Context;
      this.b = null;
      this.c = 0L;
    }
    
    public a(u this$0, Context param1Context, Runnable param1Runnable, long param1Long) {
      this.a = param1Context;
      this.b = param1Runnable;
      this.c = param1Long;
    }
    
    public final void run() {
      if (!z.a(this.a, "security_info", 30000L)) {
        x.c("[UploadManager] Sleep %d try to lock security file again (pid=%d | tid=%d)", new Object[] { Integer.valueOf(5000), Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
        z.b(5000L);
        if (z.a(this, "BUGLY_ASYNC_UPLOAD") == null) {
          x.d("[UploadManager] Failed to start a thread to execute task of initializing security context, try to post it into thread pool.", new Object[0]);
          w w = w.a();
          if (w != null) {
            w.a(this);
            return;
          } 
        } else {
          return;
        } 
        x.e("[UploadManager] Asynchronous thread pool is unavailable now, try next time.", new Object[0]);
        return;
      } 
      if (!u.c(this.d)) {
        x.d("[UploadManager] Failed to load security info from database", new Object[0]);
        this.d.b(false);
      } 
      if (u.d(this.d) != null) {
        if (this.d.b()) {
          x.c("[UploadManager] Sucessfully got session ID, try to execute upload tasks now (pid=%d | tid=%d)", new Object[] { Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
          if (this.b != null)
            u.a(this.d, this.b, this.c); 
          u.a(this.d, 0);
          z.b(this.a, "security_info");
          synchronized (u.e(this.d)) {
            u.a(this.d, false);
            return;
          } 
        } 
        x.a("[UploadManager] Session ID is expired, drop it.", new Object[0]);
        this.d.b(true);
      } 
      null = z.a(128);
      if (null != null && null.length << 3 == 128) {
        u.a(this.d, null);
        x.c("[UploadManager] Execute one upload task for requesting session ID (pid=%d | tid=%d)", new Object[] { Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
        if (this.b != null) {
          u.a(this.d, this.b, this.c);
          return;
        } 
        u.a(this.d, 1);
        return;
      } 
      x.d("[UploadManager] Failed to create AES key (pid=%d | tid=%d)", new Object[] { Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()) });
      this.d.b(false);
      z.b(this.a, "security_info");
      synchronized (u.e(this.d)) {
        u.a(this.d, false);
        return;
      } 
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/u.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */