package com.tencent.bugly;

import android.content.Context;

public class Bugly {
  public static final String SDK_IS_DEV = "false";
  
  private static boolean a;
  
  public static Context applicationContext;
  
  private static String[] b;
  
  private static String[] c;
  
  public static boolean enable = true;
  
  public static Boolean isDev;
  
  static {
    applicationContext = null;
    b = new String[] { "BuglyCrashModule", "BuglyRqdModule", "BuglyBetaModule" };
    c = new String[] { "BuglyRqdModule", "BuglyCrashModule", "BuglyBetaModule" };
  }
  
  public static String getAppChannel() {
    // Byte code:
    //   0: aconst_null
    //   1: astore_0
    //   2: ldc com/tencent/bugly/Bugly
    //   4: monitorenter
    //   5: invokestatic b : ()Lcom/tencent/bugly/crashreport/common/info/a;
    //   8: astore_1
    //   9: aload_1
    //   10: ifnonnull -> 18
    //   13: ldc com/tencent/bugly/Bugly
    //   15: monitorexit
    //   16: aload_0
    //   17: areturn
    //   18: aload_1
    //   19: getfield l : Ljava/lang/String;
    //   22: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   25: ifeq -> 86
    //   28: invokestatic a : ()Lcom/tencent/bugly/proguard/p;
    //   31: astore_0
    //   32: aload_0
    //   33: ifnonnull -> 44
    //   36: aload_1
    //   37: getfield l : Ljava/lang/String;
    //   40: astore_0
    //   41: goto -> 13
    //   44: aload_0
    //   45: sipush #556
    //   48: aconst_null
    //   49: iconst_1
    //   50: invokevirtual a : (ILcom/tencent/bugly/proguard/o;Z)Ljava/util/Map;
    //   53: astore_0
    //   54: aload_0
    //   55: ifnull -> 86
    //   58: aload_0
    //   59: ldc 'app_channel'
    //   61: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   66: checkcast [B
    //   69: astore_0
    //   70: aload_0
    //   71: ifnull -> 86
    //   74: new java/lang/String
    //   77: dup
    //   78: aload_0
    //   79: invokespecial <init> : ([B)V
    //   82: astore_0
    //   83: goto -> 13
    //   86: aload_1
    //   87: getfield l : Ljava/lang/String;
    //   90: astore_0
    //   91: goto -> 13
    //   94: astore_0
    //   95: ldc com/tencent/bugly/Bugly
    //   97: monitorexit
    //   98: aload_0
    //   99: athrow
    // Exception table:
    //   from	to	target	type
    //   5	9	94	finally
    //   18	32	94	finally
    //   36	41	94	finally
    //   44	54	94	finally
    //   58	70	94	finally
    //   74	83	94	finally
    //   86	91	94	finally
  }
  
  public static void init(Context paramContext, String paramString, boolean paramBoolean) {
    init(paramContext, paramString, paramBoolean, null);
  }
  
  public static void init(Context paramContext, String paramString, boolean paramBoolean, BuglyStrategy paramBuglyStrategy) {
    // Byte code:
    //   0: ldc com/tencent/bugly/Bugly
    //   2: monitorenter
    //   3: getstatic com/tencent/bugly/Bugly.a : Z
    //   6: istore #4
    //   8: iload #4
    //   10: ifeq -> 17
    //   13: ldc com/tencent/bugly/Bugly
    //   15: monitorexit
    //   16: return
    //   17: iconst_1
    //   18: putstatic com/tencent/bugly/Bugly.a : Z
    //   21: aload_0
    //   22: invokestatic a : (Landroid/content/Context;)Landroid/content/Context;
    //   25: astore_0
    //   26: aload_0
    //   27: putstatic com/tencent/bugly/Bugly.applicationContext : Landroid/content/Context;
    //   30: aload_0
    //   31: ifnonnull -> 52
    //   34: getstatic com/tencent/bugly/proguard/x.a : Ljava/lang/String;
    //   37: ldc 'init arg 'context' should not be null!'
    //   39: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   42: pop
    //   43: goto -> 13
    //   46: astore_0
    //   47: ldc com/tencent/bugly/Bugly
    //   49: monitorexit
    //   50: aload_0
    //   51: athrow
    //   52: invokestatic isDev : ()Z
    //   55: ifeq -> 64
    //   58: getstatic com/tencent/bugly/Bugly.c : [Ljava/lang/String;
    //   61: putstatic com/tencent/bugly/Bugly.b : [Ljava/lang/String;
    //   64: getstatic com/tencent/bugly/Bugly.b : [Ljava/lang/String;
    //   67: astore_0
    //   68: aload_0
    //   69: arraylength
    //   70: istore #5
    //   72: iconst_0
    //   73: istore #6
    //   75: iload #6
    //   77: iload #5
    //   79: if_icmpge -> 152
    //   82: aload_0
    //   83: iload #6
    //   85: aaload
    //   86: astore #7
    //   88: aload #7
    //   90: ldc 'BuglyCrashModule'
    //   92: invokevirtual equals : (Ljava/lang/Object;)Z
    //   95: ifeq -> 110
    //   98: invokestatic getInstance : ()Lcom/tencent/bugly/CrashModule;
    //   101: invokestatic a : (Lcom/tencent/bugly/a;)V
    //   104: iinc #6, 1
    //   107: goto -> 75
    //   110: aload #7
    //   112: ldc 'BuglyBetaModule'
    //   114: invokevirtual equals : (Ljava/lang/Object;)Z
    //   117: ifne -> 104
    //   120: aload #7
    //   122: ldc 'BuglyRqdModule'
    //   124: invokevirtual equals : (Ljava/lang/Object;)Z
    //   127: ifne -> 104
    //   130: aload #7
    //   132: ldc 'BuglyFeedbackModule'
    //   134: invokevirtual equals : (Ljava/lang/Object;)Z
    //   137: pop
    //   138: goto -> 104
    //   141: astore #7
    //   143: aload #7
    //   145: invokestatic b : (Ljava/lang/Throwable;)Z
    //   148: pop
    //   149: goto -> 104
    //   152: getstatic com/tencent/bugly/Bugly.enable : Z
    //   155: putstatic com/tencent/bugly/b.a : Z
    //   158: getstatic com/tencent/bugly/Bugly.applicationContext : Landroid/content/Context;
    //   161: aload_1
    //   162: iload_2
    //   163: aload_3
    //   164: invokestatic a : (Landroid/content/Context;Ljava/lang/String;ZLcom/tencent/bugly/BuglyStrategy;)V
    //   167: goto -> 13
    // Exception table:
    //   from	to	target	type
    //   3	8	46	finally
    //   17	30	46	finally
    //   34	43	46	finally
    //   52	64	46	finally
    //   64	72	46	finally
    //   88	104	141	java/lang/Throwable
    //   88	104	46	finally
    //   110	138	141	java/lang/Throwable
    //   110	138	46	finally
    //   143	149	46	finally
    //   152	167	46	finally
  }
  
  public static boolean isDev() {
    if (isDev == null)
      isDev = Boolean.valueOf(Boolean.parseBoolean("false".replace("@", ""))); 
    return isDev.booleanValue();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/Bugly.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */