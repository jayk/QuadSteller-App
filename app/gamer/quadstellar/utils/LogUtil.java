package app.gamer.quadstellar.utils;

import android.util.Log;

public class LogUtil {
  public static void d(String paramString1, String paramString2) {
    if (debugOpen())
      Log.d(paramString1, paramString2); 
  }
  
  public static void d(String paramString1, String paramString2, Exception paramException) {
    if (debugOpen())
      Log.d(paramString1, paramString2, paramException); 
  }
  
  private static boolean debugOpen() {
    return true;
  }
  
  public static void e(String paramString1, String paramString2) {
    if (debugOpen())
      Log.e(paramString1, paramString2); 
  }
  
  public static void e(String paramString1, String paramString2, Exception paramException) {
    if (debugOpen())
      Log.e(paramString1, paramString2, paramException); 
  }
  
  public static void i(String paramString1, String paramString2) {
    if (debugOpen())
      Log.i(paramString1, paramString2); 
  }
  
  public static void i(String paramString1, String paramString2, Exception paramException) {
    if (debugOpen())
      Log.i(paramString1, paramString2, paramException); 
  }
  
  public static void v(String paramString1, String paramString2) {
    if (debugOpen())
      Log.i(paramString1, paramString2); 
  }
  
  public static void v(String paramString1, String paramString2, Exception paramException) {
    if (debugOpen())
      Log.i(paramString1, paramString2, paramException); 
  }
  
  public static void w(String paramString, Exception paramException) {
    if (debugOpen())
      Log.w(paramString, paramException); 
  }
  
  public static void w(String paramString1, String paramString2) {
    if (debugOpen())
      Log.w(paramString1, paramString2); 
  }
  
  public static void w(String paramString1, String paramString2, Exception paramException) {
    if (debugOpen())
      Log.w(paramString1, paramString2, paramException); 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/utils/LogUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */