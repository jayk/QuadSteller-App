package com.tencent.bugly.proguard;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public final class y {
  public static boolean a;
  
  private static SimpleDateFormat b = null;
  
  private static int c;
  
  private static StringBuilder d;
  
  private static StringBuilder e;
  
  private static boolean f;
  
  private static a g;
  
  private static String h;
  
  private static String i;
  
  private static Context j;
  
  private static String k;
  
  private static boolean l;
  
  private static int m;
  
  private static final Object n;
  
  static {
    a = true;
    c = 5120;
    n = new Object();
    try {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
      this("MM-dd HH:mm:ss");
      b = simpleDateFormat;
    } catch (Throwable throwable) {}
  }
  
  public static void a(int paramInt) {
    synchronized (n) {
      c = paramInt;
      if (paramInt < 0) {
        c = 0;
      } else if (paramInt > 10240) {
        c = 10240;
      } 
      return;
    } 
  }
  
  public static void a(Context paramContext) {
    // Byte code:
    //   0: ldc com/tencent/bugly/proguard/y
    //   2: monitorenter
    //   3: getstatic com/tencent/bugly/proguard/y.l : Z
    //   6: ifne -> 21
    //   9: aload_0
    //   10: ifnull -> 21
    //   13: getstatic com/tencent/bugly/proguard/y.a : Z
    //   16: istore_1
    //   17: iload_1
    //   18: ifne -> 25
    //   21: ldc com/tencent/bugly/proguard/y
    //   23: monitorexit
    //   24: return
    //   25: new java/lang/StringBuilder
    //   28: astore_2
    //   29: aload_2
    //   30: iconst_0
    //   31: invokespecial <init> : (I)V
    //   34: aload_2
    //   35: putstatic com/tencent/bugly/proguard/y.e : Ljava/lang/StringBuilder;
    //   38: new java/lang/StringBuilder
    //   41: astore_2
    //   42: aload_2
    //   43: iconst_0
    //   44: invokespecial <init> : (I)V
    //   47: aload_2
    //   48: putstatic com/tencent/bugly/proguard/y.d : Ljava/lang/StringBuilder;
    //   51: aload_0
    //   52: putstatic com/tencent/bugly/proguard/y.j : Landroid/content/Context;
    //   55: aload_0
    //   56: invokestatic a : (Landroid/content/Context;)Lcom/tencent/bugly/crashreport/common/info/a;
    //   59: astore_0
    //   60: aload_0
    //   61: getfield d : Ljava/lang/String;
    //   64: putstatic com/tencent/bugly/proguard/y.h : Ljava/lang/String;
    //   67: aload_0
    //   68: invokevirtual getClass : ()Ljava/lang/Class;
    //   71: pop
    //   72: ldc ''
    //   74: putstatic com/tencent/bugly/proguard/y.i : Ljava/lang/String;
    //   77: new java/lang/StringBuilder
    //   80: astore_0
    //   81: aload_0
    //   82: invokespecial <init> : ()V
    //   85: aload_0
    //   86: getstatic com/tencent/bugly/proguard/y.j : Landroid/content/Context;
    //   89: invokevirtual getFilesDir : ()Ljava/io/File;
    //   92: invokevirtual getPath : ()Ljava/lang/String;
    //   95: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   98: ldc '/buglylog_'
    //   100: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   103: getstatic com/tencent/bugly/proguard/y.h : Ljava/lang/String;
    //   106: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   109: ldc '_'
    //   111: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   114: getstatic com/tencent/bugly/proguard/y.i : Ljava/lang/String;
    //   117: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   120: ldc '.txt'
    //   122: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   125: invokevirtual toString : ()Ljava/lang/String;
    //   128: putstatic com/tencent/bugly/proguard/y.k : Ljava/lang/String;
    //   131: invokestatic myPid : ()I
    //   134: putstatic com/tencent/bugly/proguard/y.m : I
    //   137: iconst_1
    //   138: putstatic com/tencent/bugly/proguard/y.l : Z
    //   141: goto -> 21
    //   144: astore_0
    //   145: ldc com/tencent/bugly/proguard/y
    //   147: monitorexit
    //   148: aload_0
    //   149: athrow
    //   150: astore_0
    //   151: goto -> 137
    // Exception table:
    //   from	to	target	type
    //   3	9	144	finally
    //   13	17	144	finally
    //   25	137	150	java/lang/Throwable
    //   25	137	144	finally
    //   137	141	144	finally
  }
  
  public static void a(String paramString1, String paramString2, String paramString3) {
    // Byte code:
    //   0: ldc com/tencent/bugly/proguard/y
    //   2: monitorenter
    //   3: getstatic com/tencent/bugly/proguard/y.l : Z
    //   6: ifeq -> 17
    //   9: getstatic com/tencent/bugly/proguard/y.a : Z
    //   12: istore_3
    //   13: iload_3
    //   14: ifne -> 21
    //   17: ldc com/tencent/bugly/proguard/y
    //   19: monitorexit
    //   20: return
    //   21: aload_0
    //   22: aload_1
    //   23: aload_2
    //   24: invokestatic b : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z
    //   27: pop
    //   28: invokestatic myTid : ()I
    //   31: i2l
    //   32: lstore #4
    //   34: getstatic com/tencent/bugly/proguard/y.d : Ljava/lang/StringBuilder;
    //   37: iconst_0
    //   38: invokevirtual setLength : (I)V
    //   41: aload_2
    //   42: astore #6
    //   44: aload_2
    //   45: invokevirtual length : ()I
    //   48: sipush #30720
    //   51: if_icmple -> 74
    //   54: aload_2
    //   55: aload_2
    //   56: invokevirtual length : ()I
    //   59: sipush #30720
    //   62: isub
    //   63: aload_2
    //   64: invokevirtual length : ()I
    //   67: iconst_1
    //   68: isub
    //   69: invokevirtual substring : (II)Ljava/lang/String;
    //   72: astore #6
    //   74: new java/util/Date
    //   77: astore_2
    //   78: aload_2
    //   79: invokespecial <init> : ()V
    //   82: getstatic com/tencent/bugly/proguard/y.b : Ljava/text/SimpleDateFormat;
    //   85: ifnull -> 209
    //   88: getstatic com/tencent/bugly/proguard/y.b : Ljava/text/SimpleDateFormat;
    //   91: aload_2
    //   92: invokevirtual format : (Ljava/util/Date;)Ljava/lang/String;
    //   95: astore_2
    //   96: getstatic com/tencent/bugly/proguard/y.d : Ljava/lang/StringBuilder;
    //   99: aload_2
    //   100: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   103: ldc ' '
    //   105: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   108: getstatic com/tencent/bugly/proguard/y.m : I
    //   111: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   114: ldc ' '
    //   116: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   119: lload #4
    //   121: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   124: ldc ' '
    //   126: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   129: aload_0
    //   130: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   133: ldc ' '
    //   135: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   138: aload_1
    //   139: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   142: ldc ': '
    //   144: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   147: aload #6
    //   149: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   152: ldc '\\r\\n'
    //   154: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   157: pop
    //   158: getstatic com/tencent/bugly/proguard/y.d : Ljava/lang/StringBuilder;
    //   161: invokevirtual toString : ()Ljava/lang/String;
    //   164: astore #6
    //   166: getstatic com/tencent/bugly/proguard/y.n : Ljava/lang/Object;
    //   169: astore_0
    //   170: aload_0
    //   171: monitorenter
    //   172: getstatic com/tencent/bugly/proguard/y.e : Ljava/lang/StringBuilder;
    //   175: aload #6
    //   177: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   180: pop
    //   181: getstatic com/tencent/bugly/proguard/y.e : Ljava/lang/StringBuilder;
    //   184: invokevirtual length : ()I
    //   187: getstatic com/tencent/bugly/proguard/y.c : I
    //   190: if_icmpgt -> 217
    //   193: aload_0
    //   194: monitorexit
    //   195: goto -> 17
    //   198: astore_1
    //   199: aload_0
    //   200: monitorexit
    //   201: aload_1
    //   202: athrow
    //   203: astore_0
    //   204: ldc com/tencent/bugly/proguard/y
    //   206: monitorexit
    //   207: aload_0
    //   208: athrow
    //   209: aload_2
    //   210: invokevirtual toString : ()Ljava/lang/String;
    //   213: astore_2
    //   214: goto -> 96
    //   217: getstatic com/tencent/bugly/proguard/y.f : Z
    //   220: ifeq -> 228
    //   223: aload_0
    //   224: monitorexit
    //   225: goto -> 17
    //   228: iconst_1
    //   229: putstatic com/tencent/bugly/proguard/y.f : Z
    //   232: invokestatic a : ()Lcom/tencent/bugly/proguard/w;
    //   235: astore_1
    //   236: new com/tencent/bugly/proguard/y$1
    //   239: astore_2
    //   240: aload_2
    //   241: aload #6
    //   243: invokespecial <init> : (Ljava/lang/String;)V
    //   246: aload_1
    //   247: aload_2
    //   248: invokevirtual a : (Ljava/lang/Runnable;)Z
    //   251: pop
    //   252: aload_0
    //   253: monitorexit
    //   254: goto -> 17
    // Exception table:
    //   from	to	target	type
    //   3	13	203	finally
    //   21	41	203	finally
    //   44	74	203	finally
    //   74	96	203	finally
    //   96	172	203	finally
    //   172	195	198	finally
    //   199	203	203	finally
    //   209	214	203	finally
    //   217	225	198	finally
    //   228	254	198	finally
  }
  
  public static void a(String paramString1, String paramString2, Throwable paramThrowable) {
    if (paramThrowable != null) {
      String str1 = paramThrowable.getMessage();
      String str2 = str1;
      if (str1 == null)
        str2 = ""; 
      a(paramString1, paramString2, str2 + '\n' + z.b(paramThrowable));
    } 
  }
  
  public static byte[] a() {
    byte[] arrayOfByte = null;
    if (!a)
      return arrayOfByte; 
    synchronized (n) {
      if (g != null && a.d(g)) {
        arrayOfByte1 = (byte[])a.a(g);
      } else {
        arrayOfByte1 = null;
      } 
      int i = e.length();
      if (i == 0 && arrayOfByte1 == null)
        return arrayOfByte; 
      byte[] arrayOfByte1 = z.a((File)arrayOfByte1, e.toString(), "BuglyLog.txt");
    } 
    return (byte[])SYNTHETIC_LOCAL_VARIABLE_1;
  }
  
  private static boolean b(String paramString1, String paramString2, String paramString3) {
    try {
      com.tencent.bugly.crashreport.common.info.a a1 = com.tencent.bugly.crashreport.common.info.a.b();
      if (a1 != null && a1.D != null)
        return a1.D.appendLogToNative(paramString1, paramString2, paramString3); 
    } catch (Throwable throwable) {
      if (!x.a(throwable))
        throwable.printStackTrace(); 
    } 
    return false;
  }
  
  public static final class a {
    private boolean a;
    
    private File b;
    
    private String c;
    
    private long d;
    
    private long e = 30720L;
    
    public a(String param1String) {
      if (param1String != null && !param1String.equals("")) {
        this.c = param1String;
        this.a = a();
      } 
    }
    
    private boolean a() {
      null = false;
      try {
        File file = new File();
        this(this.c);
        this.b = file;
        if (this.b.exists() && !this.b.delete()) {
          this.a = false;
          return null;
        } 
        if (!this.b.createNewFile()) {
          this.a = false;
          return null;
        } 
      } catch (Throwable throwable) {
        this.a = false;
      } 
      return true;
    }
    
    public final boolean a(String param1String) {
      FileOutputStream fileOutputStream;
      boolean bool = false;
      if (!this.a)
        return bool; 
      try {
        fileOutputStream = new FileOutputStream();
        this(this.b, true);
      } catch (Throwable null) {
        throwable = null;
        return bool1;
      } finally {
        param1String = null;
      } 
      if (fileOutputStream != null)
        try {
          fileOutputStream.close();
        } catch (IOException iOException) {} 
      throw param1String;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/y.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */