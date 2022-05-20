package com.loopj.android.http;

class AssertUtils {
  public static void asserts(boolean paramBoolean, String paramString) {
    if (!paramBoolean)
      throw new AssertionError(paramString); 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/loopj/android/http/AssertUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */