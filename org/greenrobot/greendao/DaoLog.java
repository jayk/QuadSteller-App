package org.greenrobot.greendao;

import android.util.Log;

public class DaoLog {
  public static final int ASSERT = 7;
  
  public static final int DEBUG = 3;
  
  public static final int ERROR = 6;
  
  public static final int INFO = 4;
  
  private static final String TAG = "greenDAO";
  
  public static final int VERBOSE = 2;
  
  public static final int WARN = 5;
  
  public static int d(String paramString) {
    return Log.d("greenDAO", paramString);
  }
  
  public static int d(String paramString, Throwable paramThrowable) {
    return Log.d("greenDAO", paramString, paramThrowable);
  }
  
  public static int e(String paramString) {
    return Log.w("greenDAO", paramString);
  }
  
  public static int e(String paramString, Throwable paramThrowable) {
    return Log.e("greenDAO", paramString, paramThrowable);
  }
  
  public static String getStackTraceString(Throwable paramThrowable) {
    return Log.getStackTraceString(paramThrowable);
  }
  
  public static int i(String paramString) {
    return Log.i("greenDAO", paramString);
  }
  
  public static int i(String paramString, Throwable paramThrowable) {
    return Log.i("greenDAO", paramString, paramThrowable);
  }
  
  public static boolean isLoggable(int paramInt) {
    return Log.isLoggable("greenDAO", paramInt);
  }
  
  public static int println(int paramInt, String paramString) {
    return Log.println(paramInt, "greenDAO", paramString);
  }
  
  public static int v(String paramString) {
    return Log.v("greenDAO", paramString);
  }
  
  public static int v(String paramString, Throwable paramThrowable) {
    return Log.v("greenDAO", paramString, paramThrowable);
  }
  
  public static int w(String paramString) {
    return Log.w("greenDAO", paramString);
  }
  
  public static int w(String paramString, Throwable paramThrowable) {
    return Log.w("greenDAO", paramString, paramThrowable);
  }
  
  public static int w(Throwable paramThrowable) {
    return Log.w("greenDAO", paramThrowable);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/DaoLog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */