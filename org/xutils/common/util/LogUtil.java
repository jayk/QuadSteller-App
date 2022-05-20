package org.xutils.common.util;

import android.text.TextUtils;
import android.util.Log;
import org.xutils.x;

public class LogUtil {
  public static String customTagPrefix = "x_log";
  
  public static void d(String paramString) {
    if (x.isDebug())
      Log.d(generateTag(), paramString); 
  }
  
  public static void d(String paramString, Throwable paramThrowable) {
    if (x.isDebug())
      Log.d(generateTag(), paramString, paramThrowable); 
  }
  
  public static void e(String paramString) {
    if (x.isDebug())
      Log.e(generateTag(), paramString); 
  }
  
  public static void e(String paramString, Throwable paramThrowable) {
    if (x.isDebug())
      Log.e(generateTag(), paramString, paramThrowable); 
  }
  
  private static String generateTag() {
    StackTraceElement stackTraceElement = (new Throwable()).getStackTrace()[2];
    String str2 = stackTraceElement.getClassName();
    String str1 = String.format("%s.%s(L:%d)", new Object[] { str2.substring(str2.lastIndexOf(".") + 1), stackTraceElement.getMethodName(), Integer.valueOf(stackTraceElement.getLineNumber()) });
    if (!TextUtils.isEmpty(customTagPrefix))
      str1 = customTagPrefix + ":" + str1; 
    return str1;
  }
  
  public static void i(String paramString) {
    if (x.isDebug())
      Log.i(generateTag(), paramString); 
  }
  
  public static void i(String paramString, Throwable paramThrowable) {
    if (x.isDebug())
      Log.i(generateTag(), paramString, paramThrowable); 
  }
  
  public static void v(String paramString) {
    if (x.isDebug())
      Log.v(generateTag(), paramString); 
  }
  
  public static void v(String paramString, Throwable paramThrowable) {
    if (x.isDebug())
      Log.v(generateTag(), paramString, paramThrowable); 
  }
  
  public static void w(String paramString) {
    if (x.isDebug())
      Log.w(generateTag(), paramString); 
  }
  
  public static void w(String paramString, Throwable paramThrowable) {
    if (x.isDebug())
      Log.w(generateTag(), paramString, paramThrowable); 
  }
  
  public static void w(Throwable paramThrowable) {
    if (x.isDebug())
      Log.w(generateTag(), paramThrowable); 
  }
  
  public static void wtf(String paramString) {
    if (x.isDebug())
      Log.wtf(generateTag(), paramString); 
  }
  
  public static void wtf(String paramString, Throwable paramThrowable) {
    if (x.isDebug())
      Log.wtf(generateTag(), paramString, paramThrowable); 
  }
  
  public static void wtf(Throwable paramThrowable) {
    if (x.isDebug())
      Log.wtf(generateTag(), paramThrowable); 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/common/util/LogUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */