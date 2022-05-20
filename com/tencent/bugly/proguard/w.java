package com.tencent.bugly.proguard;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

public final class w {
  private static w a;
  
  private ScheduledExecutorService b = null;
  
  protected w() {
    this.b = Executors.newScheduledThreadPool(3, new ThreadFactory(this) {
          public final Thread newThread(Runnable param1Runnable) {
            param1Runnable = new Thread(param1Runnable);
            param1Runnable.setName("BUGLY_THREAD");
            return (Thread)param1Runnable;
          }
        });
    if (this.b == null || this.b.isShutdown())
      x.d("[AsyncTaskHandler] ScheduledExecutorService is not valiable!", new Object[0]); 
  }
  
  public static w a() {
    // Byte code:
    //   0: ldc com/tencent/bugly/proguard/w
    //   2: monitorenter
    //   3: getstatic com/tencent/bugly/proguard/w.a : Lcom/tencent/bugly/proguard/w;
    //   6: ifnonnull -> 21
    //   9: new com/tencent/bugly/proguard/w
    //   12: astore_0
    //   13: aload_0
    //   14: invokespecial <init> : ()V
    //   17: aload_0
    //   18: putstatic com/tencent/bugly/proguard/w.a : Lcom/tencent/bugly/proguard/w;
    //   21: getstatic com/tencent/bugly/proguard/w.a : Lcom/tencent/bugly/proguard/w;
    //   24: astore_0
    //   25: ldc com/tencent/bugly/proguard/w
    //   27: monitorexit
    //   28: aload_0
    //   29: areturn
    //   30: astore_0
    //   31: ldc com/tencent/bugly/proguard/w
    //   33: monitorexit
    //   34: aload_0
    //   35: athrow
    // Exception table:
    //   from	to	target	type
    //   3	21	30	finally
    //   21	25	30	finally
  }
  
  public final boolean a(Runnable paramRunnable) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: aload_0
    //   3: monitorenter
    //   4: aload_0
    //   5: invokevirtual c : ()Z
    //   8: ifne -> 27
    //   11: ldc '[AsyncTaskHandler] Async handler was closed, should not post task.'
    //   13: iconst_0
    //   14: anewarray java/lang/Object
    //   17: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   20: pop
    //   21: iload_2
    //   22: istore_3
    //   23: aload_0
    //   24: monitorexit
    //   25: iload_3
    //   26: ireturn
    //   27: aload_1
    //   28: ifnonnull -> 51
    //   31: ldc '[AsyncTaskHandler] Task input is null.'
    //   33: iconst_0
    //   34: anewarray java/lang/Object
    //   37: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   40: pop
    //   41: iload_2
    //   42: istore_3
    //   43: goto -> 23
    //   46: astore_1
    //   47: aload_0
    //   48: monitorexit
    //   49: aload_1
    //   50: athrow
    //   51: ldc '[AsyncTaskHandler] Post a normal task: %s'
    //   53: iconst_1
    //   54: anewarray java/lang/Object
    //   57: dup
    //   58: iconst_0
    //   59: aload_1
    //   60: invokevirtual getClass : ()Ljava/lang/Class;
    //   63: invokevirtual getName : ()Ljava/lang/String;
    //   66: aastore
    //   67: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   70: pop
    //   71: aload_0
    //   72: getfield b : Ljava/util/concurrent/ScheduledExecutorService;
    //   75: aload_1
    //   76: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   81: iconst_1
    //   82: istore_3
    //   83: goto -> 23
    //   86: astore_1
    //   87: iload_2
    //   88: istore_3
    //   89: getstatic com/tencent/bugly/b.c : Z
    //   92: ifeq -> 23
    //   95: aload_1
    //   96: invokevirtual printStackTrace : ()V
    //   99: iload_2
    //   100: istore_3
    //   101: goto -> 23
    // Exception table:
    //   from	to	target	type
    //   4	21	46	finally
    //   31	41	46	finally
    //   51	71	46	finally
    //   71	81	86	java/lang/Throwable
    //   71	81	46	finally
    //   89	99	46	finally
  }
  
  public final boolean a(Runnable paramRunnable, long paramLong) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #4
    //   3: aload_0
    //   4: monitorenter
    //   5: aload_0
    //   6: invokevirtual c : ()Z
    //   9: ifne -> 31
    //   12: ldc '[AsyncTaskHandler] Async handler was closed, should not post task.'
    //   14: iconst_0
    //   15: anewarray java/lang/Object
    //   18: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   21: pop
    //   22: iload #4
    //   24: istore #5
    //   26: aload_0
    //   27: monitorexit
    //   28: iload #5
    //   30: ireturn
    //   31: aload_1
    //   32: ifnonnull -> 57
    //   35: ldc '[AsyncTaskHandler] Task input is null.'
    //   37: iconst_0
    //   38: anewarray java/lang/Object
    //   41: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   44: pop
    //   45: iload #4
    //   47: istore #5
    //   49: goto -> 26
    //   52: astore_1
    //   53: aload_0
    //   54: monitorexit
    //   55: aload_1
    //   56: athrow
    //   57: lload_2
    //   58: lconst_0
    //   59: lcmp
    //   60: ifle -> 111
    //   63: ldc '[AsyncTaskHandler] Post a delay(time: %dms) task: %s'
    //   65: iconst_2
    //   66: anewarray java/lang/Object
    //   69: dup
    //   70: iconst_0
    //   71: lload_2
    //   72: invokestatic valueOf : (J)Ljava/lang/Long;
    //   75: aastore
    //   76: dup
    //   77: iconst_1
    //   78: aload_1
    //   79: invokevirtual getClass : ()Ljava/lang/Class;
    //   82: invokevirtual getName : ()Ljava/lang/String;
    //   85: aastore
    //   86: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   89: pop
    //   90: aload_0
    //   91: getfield b : Ljava/util/concurrent/ScheduledExecutorService;
    //   94: aload_1
    //   95: lload_2
    //   96: getstatic java/util/concurrent/TimeUnit.MILLISECONDS : Ljava/util/concurrent/TimeUnit;
    //   99: invokeinterface schedule : (Ljava/lang/Runnable;JLjava/util/concurrent/TimeUnit;)Ljava/util/concurrent/ScheduledFuture;
    //   104: pop
    //   105: iconst_1
    //   106: istore #5
    //   108: goto -> 26
    //   111: lconst_0
    //   112: lstore_2
    //   113: goto -> 63
    //   116: astore_1
    //   117: iload #4
    //   119: istore #5
    //   121: getstatic com/tencent/bugly/b.c : Z
    //   124: ifeq -> 26
    //   127: aload_1
    //   128: invokevirtual printStackTrace : ()V
    //   131: iload #4
    //   133: istore #5
    //   135: goto -> 26
    // Exception table:
    //   from	to	target	type
    //   5	22	52	finally
    //   35	45	52	finally
    //   63	90	52	finally
    //   90	105	116	java/lang/Throwable
    //   90	105	52	finally
    //   121	131	52	finally
  }
  
  public final void b() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield b : Ljava/util/concurrent/ScheduledExecutorService;
    //   6: ifnull -> 41
    //   9: aload_0
    //   10: getfield b : Ljava/util/concurrent/ScheduledExecutorService;
    //   13: invokeinterface isShutdown : ()Z
    //   18: ifne -> 41
    //   21: ldc '[AsyncTaskHandler] Close async handler.'
    //   23: iconst_0
    //   24: anewarray java/lang/Object
    //   27: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   30: pop
    //   31: aload_0
    //   32: getfield b : Ljava/util/concurrent/ScheduledExecutorService;
    //   35: invokeinterface shutdownNow : ()Ljava/util/List;
    //   40: pop
    //   41: aload_0
    //   42: monitorexit
    //   43: return
    //   44: astore_1
    //   45: aload_0
    //   46: monitorexit
    //   47: aload_1
    //   48: athrow
    // Exception table:
    //   from	to	target	type
    //   2	41	44	finally
  }
  
  public final boolean c() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield b : Ljava/util/concurrent/ScheduledExecutorService;
    //   6: ifnull -> 29
    //   9: aload_0
    //   10: getfield b : Ljava/util/concurrent/ScheduledExecutorService;
    //   13: invokeinterface isShutdown : ()Z
    //   18: istore_1
    //   19: iload_1
    //   20: ifne -> 29
    //   23: iconst_1
    //   24: istore_1
    //   25: aload_0
    //   26: monitorexit
    //   27: iload_1
    //   28: ireturn
    //   29: iconst_0
    //   30: istore_1
    //   31: goto -> 25
    //   34: astore_2
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_2
    //   38: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	34	finally
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/w.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */