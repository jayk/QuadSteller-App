package com.tencent.bugly.proguard;

import android.util.Log;
import java.util.Locale;

public final class x {
  public static String a;
  
  public static boolean b;
  
  private static String c = "CrashReportInfo";
  
  static {
    a = "CrashReport";
    b = false;
  }
  
  private static boolean a(int paramInt, String paramString, Object... paramVarArgs) {
    String str;
    boolean bool = false;
    if (!b);
    if (paramString == null) {
      str = "null";
    } else {
      str = paramString;
      if (paramVarArgs != null) {
        str = paramString;
        if (paramVarArgs.length != 0)
          str = String.format(Locale.US, paramString, paramVarArgs); 
      } 
    } 
    switch (paramInt) {
      default:
        return bool;
      case 0:
        Log.i(a, str);
        bool = true;
      case 5:
        Log.i(c, str);
        bool = true;
      case 1:
        Log.d(a, str);
        bool = true;
      case 2:
        Log.w(a, str);
        bool = true;
      case 3:
        break;
    } 
    Log.e(a, str);
    bool = true;
  }
  
  public static boolean a(Class paramClass, String paramString, Object... paramVarArgs) {
    return a(0, String.format(Locale.US, "[%s] %s", new Object[] { paramClass.getSimpleName(), paramString }), paramVarArgs);
  }
  
  public static boolean a(String paramString, Object... paramVarArgs) {
    return a(0, paramString, paramVarArgs);
  }
  
  public static boolean a(Throwable paramThrowable) {
    boolean bool = false;
    if (b)
      bool = a(2, z.a(paramThrowable), new Object[0]); 
    return bool;
  }
  
  public static boolean b(Class paramClass, String paramString, Object... paramVarArgs) {
    return a(1, String.format(Locale.US, "[%s] %s", new Object[] { paramClass.getSimpleName(), paramString }), paramVarArgs);
  }
  
  public static boolean b(String paramString, Object... paramVarArgs) {
    return a(5, paramString, paramVarArgs);
  }
  
  public static boolean b(Throwable paramThrowable) {
    boolean bool = false;
    if (b)
      bool = a(3, z.a(paramThrowable), new Object[0]); 
    return bool;
  }
  
  public static boolean c(String paramString, Object... paramVarArgs) {
    return a(1, paramString, paramVarArgs);
  }
  
  public static boolean d(String paramString, Object... paramVarArgs) {
    return a(2, paramString, paramVarArgs);
  }
  
  public static boolean e(String paramString, Object... paramVarArgs) {
    return a(3, paramString, paramVarArgs);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/x.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */