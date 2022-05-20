package io.xlink.wifi.sdk.util;

import io.xlink.wifi.sdk.listener.LogListener;

public class MyLog {
  public static Boolean a = Boolean.valueOf(true);
  
  private static char b = (char)'v';
  
  private static LogListener c;
  
  private static void a(String paramString1, String paramString2, char paramChar) {
    // Byte code:
    //   0: ldc io/xlink/wifi/sdk/util/MyLog
    //   2: monitorenter
    //   3: getstatic io/xlink/wifi/sdk/util/MyLog.a : Ljava/lang/Boolean;
    //   6: invokevirtual booleanValue : ()Z
    //   9: ifeq -> 40
    //   12: bipush #101
    //   14: iload_2
    //   15: if_icmpne -> 86
    //   18: bipush #101
    //   20: getstatic io/xlink/wifi/sdk/util/MyLog.b : C
    //   23: if_icmpeq -> 34
    //   26: bipush #118
    //   28: getstatic io/xlink/wifi/sdk/util/MyLog.b : C
    //   31: if_icmpne -> 86
    //   34: aload_0
    //   35: aload_1
    //   36: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   39: pop
    //   40: getstatic io/xlink/wifi/sdk/util/MyLog.c : Lio/xlink/wifi/sdk/listener/LogListener;
    //   43: ifnull -> 82
    //   46: getstatic io/xlink/wifi/sdk/util/MyLog.c : Lio/xlink/wifi/sdk/listener/LogListener;
    //   49: astore_3
    //   50: new java/lang/StringBuilder
    //   53: astore #4
    //   55: aload #4
    //   57: invokespecial <init> : ()V
    //   60: aload_3
    //   61: iload_2
    //   62: aload_0
    //   63: aload #4
    //   65: aload_1
    //   66: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   69: ldc ''
    //   71: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   74: invokevirtual toString : ()Ljava/lang/String;
    //   77: invokeinterface onLog : (CLjava/lang/String;Ljava/lang/String;)V
    //   82: ldc io/xlink/wifi/sdk/util/MyLog
    //   84: monitorexit
    //   85: return
    //   86: bipush #119
    //   88: iload_2
    //   89: if_icmpne -> 123
    //   92: bipush #119
    //   94: getstatic io/xlink/wifi/sdk/util/MyLog.b : C
    //   97: if_icmpeq -> 108
    //   100: bipush #118
    //   102: getstatic io/xlink/wifi/sdk/util/MyLog.b : C
    //   105: if_icmpne -> 123
    //   108: aload_0
    //   109: aload_1
    //   110: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
    //   113: pop
    //   114: goto -> 40
    //   117: astore_0
    //   118: ldc io/xlink/wifi/sdk/util/MyLog
    //   120: monitorexit
    //   121: aload_0
    //   122: athrow
    //   123: bipush #100
    //   125: iload_2
    //   126: if_icmpne -> 154
    //   129: bipush #100
    //   131: getstatic io/xlink/wifi/sdk/util/MyLog.b : C
    //   134: if_icmpeq -> 145
    //   137: bipush #118
    //   139: getstatic io/xlink/wifi/sdk/util/MyLog.b : C
    //   142: if_icmpne -> 154
    //   145: aload_0
    //   146: aload_1
    //   147: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   150: pop
    //   151: goto -> 40
    //   154: bipush #105
    //   156: iload_2
    //   157: if_icmpne -> 185
    //   160: bipush #100
    //   162: getstatic io/xlink/wifi/sdk/util/MyLog.b : C
    //   165: if_icmpeq -> 176
    //   168: bipush #118
    //   170: getstatic io/xlink/wifi/sdk/util/MyLog.b : C
    //   173: if_icmpne -> 185
    //   176: aload_0
    //   177: aload_1
    //   178: invokestatic i : (Ljava/lang/String;Ljava/lang/String;)I
    //   181: pop
    //   182: goto -> 40
    //   185: aload_0
    //   186: aload_1
    //   187: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   190: pop
    //   191: goto -> 40
    // Exception table:
    //   from	to	target	type
    //   3	12	117	finally
    //   18	34	117	finally
    //   34	40	117	finally
    //   40	82	117	finally
    //   92	108	117	finally
    //   108	114	117	finally
    //   129	145	117	finally
    //   145	151	117	finally
    //   160	176	117	finally
    //   176	182	117	finally
    //   185	191	117	finally
  }
  
  public static void d(String paramString, Object paramObject) {
    a(paramString, paramObject.toString(), 'd');
  }
  
  public static void d(String paramString1, String paramString2) {
    a(paramString1, paramString2, 'd');
  }
  
  public static void e(String paramString, Object paramObject) {
    a(paramString, paramObject.toString(), 'e');
  }
  
  public static void e(String paramString1, String paramString2) {
    a(paramString1, paramString2, 'e');
  }
  
  public static void i(String paramString, Object paramObject) {
    a(paramString, paramObject.toString(), 'i');
  }
  
  public static void i(String paramString1, String paramString2) {
    a(paramString1, paramString2, 'i');
  }
  
  public static void setListener(LogListener paramLogListener) {
    c = paramLogListener;
  }
  
  public static void v(String paramString, Object paramObject) {
    a(paramString, paramObject.toString(), 'v');
  }
  
  public static void v(String paramString1, String paramString2) {
    a(paramString1, paramString2, 'v');
  }
  
  public static void w(String paramString, Object paramObject) {
    a(paramString, paramObject.toString(), 'w');
  }
  
  public static void w(String paramString1, String paramString2) {
    a(paramString1, paramString2, 'w');
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/util/MyLog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */