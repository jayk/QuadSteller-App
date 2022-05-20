package com.tencent.bugly;

import android.content.Context;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.crash.c;

public class CrashModule extends a {
  public static final int MODULE_ID = 1004;
  
  private static int c = 0;
  
  private static boolean d = false;
  
  private static CrashModule e = new CrashModule();
  
  private long a;
  
  private BuglyStrategy.a b;
  
  private void a(Context paramContext, BuglyStrategy paramBuglyStrategy) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_2
    //   3: ifnonnull -> 9
    //   6: aload_0
    //   7: monitorexit
    //   8: return
    //   9: aload_2
    //   10: invokevirtual getLibBuglySOFilePath : ()Ljava/lang/String;
    //   13: astore_3
    //   14: aload_3
    //   15: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   18: ifne -> 43
    //   21: aload_1
    //   22: invokestatic a : (Landroid/content/Context;)Lcom/tencent/bugly/crashreport/common/info/a;
    //   25: aload_3
    //   26: putfield m : Ljava/lang/String;
    //   29: ldc 'setted libBugly.so file path :%s'
    //   31: iconst_1
    //   32: anewarray java/lang/Object
    //   35: dup
    //   36: iconst_0
    //   37: aload_3
    //   38: aastore
    //   39: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   42: pop
    //   43: aload_2
    //   44: invokevirtual getCrashHandleCallback : ()Lcom/tencent/bugly/BuglyStrategy$a;
    //   47: ifnull -> 68
    //   50: aload_0
    //   51: aload_2
    //   52: invokevirtual getCrashHandleCallback : ()Lcom/tencent/bugly/BuglyStrategy$a;
    //   55: putfield b : Lcom/tencent/bugly/BuglyStrategy$a;
    //   58: ldc 'setted CrashHanldeCallback'
    //   60: iconst_0
    //   61: anewarray java/lang/Object
    //   64: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   67: pop
    //   68: aload_2
    //   69: invokevirtual getAppReportDelay : ()J
    //   72: lconst_0
    //   73: lcmp
    //   74: ifle -> 6
    //   77: aload_0
    //   78: aload_2
    //   79: invokevirtual getAppReportDelay : ()J
    //   82: putfield a : J
    //   85: ldc 'setted delay: %d'
    //   87: iconst_1
    //   88: anewarray java/lang/Object
    //   91: dup
    //   92: iconst_0
    //   93: aload_0
    //   94: getfield a : J
    //   97: invokestatic valueOf : (J)Ljava/lang/Long;
    //   100: aastore
    //   101: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   104: pop
    //   105: goto -> 6
    //   108: astore_1
    //   109: aload_0
    //   110: monitorexit
    //   111: aload_1
    //   112: athrow
    // Exception table:
    //   from	to	target	type
    //   9	43	108	finally
    //   43	68	108	finally
    //   68	105	108	finally
  }
  
  public static CrashModule getInstance() {
    e.id = 1004;
    return e;
  }
  
  public static boolean hasInitialized() {
    return d;
  }
  
  public String[] getTables() {
    return new String[] { "t_cr" };
  }
  
  public void init(Context paramContext, boolean paramBoolean, BuglyStrategy paramBuglyStrategy) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ifnull -> 16
    //   6: getstatic com/tencent/bugly/CrashModule.d : Z
    //   9: istore #4
    //   11: iload #4
    //   13: ifeq -> 19
    //   16: aload_0
    //   17: monitorexit
    //   18: return
    //   19: ldc 'Initializing crash module.'
    //   21: iconst_0
    //   22: anewarray java/lang/Object
    //   25: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   28: pop
    //   29: invokestatic a : ()Lcom/tencent/bugly/proguard/n;
    //   32: astore #5
    //   34: getstatic com/tencent/bugly/CrashModule.c : I
    //   37: iconst_1
    //   38: iadd
    //   39: istore #6
    //   41: iload #6
    //   43: putstatic com/tencent/bugly/CrashModule.c : I
    //   46: aload #5
    //   48: sipush #1004
    //   51: iload #6
    //   53: invokevirtual a : (II)V
    //   56: iconst_1
    //   57: putstatic com/tencent/bugly/CrashModule.d : Z
    //   60: aload_1
    //   61: invokestatic setContext : (Landroid/content/Context;)V
    //   64: aload_0
    //   65: aload_1
    //   66: aload_3
    //   67: invokespecial a : (Landroid/content/Context;Lcom/tencent/bugly/BuglyStrategy;)V
    //   70: sipush #1004
    //   73: aload_1
    //   74: iload_2
    //   75: aload_0
    //   76: getfield b : Lcom/tencent/bugly/BuglyStrategy$a;
    //   79: aconst_null
    //   80: aconst_null
    //   81: invokestatic a : (ILandroid/content/Context;ZLcom/tencent/bugly/BuglyStrategy$a;Lcom/tencent/bugly/proguard/o;Ljava/lang/String;)V
    //   84: invokestatic a : ()Lcom/tencent/bugly/crashreport/crash/c;
    //   87: astore #5
    //   89: aload #5
    //   91: invokevirtual e : ()V
    //   94: aload_3
    //   95: ifnull -> 105
    //   98: aload_3
    //   99: invokevirtual isEnableNativeCrashMonitor : ()Z
    //   102: ifeq -> 179
    //   105: aload #5
    //   107: invokevirtual g : ()V
    //   110: aload_3
    //   111: ifnull -> 121
    //   114: aload_3
    //   115: invokevirtual isEnableANRCrashMonitor : ()Z
    //   118: ifeq -> 197
    //   121: aload #5
    //   123: invokevirtual h : ()V
    //   126: aload_1
    //   127: invokestatic a : (Landroid/content/Context;)Lcom/tencent/bugly/crashreport/crash/d;
    //   130: pop
    //   131: invokestatic getInstance : ()Lcom/tencent/bugly/crashreport/crash/BuglyBroadcastRecevier;
    //   134: astore_3
    //   135: aload_3
    //   136: ldc 'android.net.conn.CONNECTIVITY_CHANGE'
    //   138: invokevirtual addFilter : (Ljava/lang/String;)V
    //   141: aload_3
    //   142: aload_1
    //   143: invokevirtual register : (Landroid/content/Context;)V
    //   146: invokestatic a : ()Lcom/tencent/bugly/proguard/n;
    //   149: astore_1
    //   150: getstatic com/tencent/bugly/CrashModule.c : I
    //   153: iconst_1
    //   154: isub
    //   155: istore #6
    //   157: iload #6
    //   159: putstatic com/tencent/bugly/CrashModule.c : I
    //   162: aload_1
    //   163: sipush #1004
    //   166: iload #6
    //   168: invokevirtual a : (II)V
    //   171: goto -> 16
    //   174: astore_1
    //   175: aload_0
    //   176: monitorexit
    //   177: aload_1
    //   178: athrow
    //   179: ldc '[crash] Closed native crash monitor!'
    //   181: iconst_0
    //   182: anewarray java/lang/Object
    //   185: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   188: pop
    //   189: aload #5
    //   191: invokevirtual f : ()V
    //   194: goto -> 110
    //   197: ldc '[crash] Closed ANR monitor!'
    //   199: iconst_0
    //   200: anewarray java/lang/Object
    //   203: invokestatic a : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   206: pop
    //   207: aload #5
    //   209: invokevirtual i : ()V
    //   212: goto -> 126
    // Exception table:
    //   from	to	target	type
    //   6	11	174	finally
    //   19	94	174	finally
    //   98	105	174	finally
    //   105	110	174	finally
    //   114	121	174	finally
    //   121	126	174	finally
    //   126	171	174	finally
    //   179	194	174	finally
    //   197	212	174	finally
  }
  
  public void onServerStrategyChanged(StrategyBean paramStrategyBean) {
    if (paramStrategyBean != null) {
      c c = c.a();
      if (c != null)
        c.a(paramStrategyBean); 
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/CrashModule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */