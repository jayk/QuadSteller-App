package com.blankj.utilcode.util;

import android.os.Environment;
import java.io.File;

public final class CleanUtils {
  private CleanUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static boolean cleanCustomCache(File paramFile) {
    return deleteFilesInDir(paramFile);
  }
  
  public static boolean cleanCustomCache(String paramString) {
    return deleteFilesInDir(paramString);
  }
  
  public static boolean cleanExternalCache() {
    return ("mounted".equals(Environment.getExternalStorageState()) && deleteFilesInDir(Utils.getApp().getExternalCacheDir()));
  }
  
  public static boolean cleanInternalCache() {
    return deleteFilesInDir(Utils.getApp().getCacheDir());
  }
  
  public static boolean cleanInternalDbByName(String paramString) {
    return Utils.getApp().deleteDatabase(paramString);
  }
  
  public static boolean cleanInternalDbs() {
    return deleteFilesInDir(Utils.getApp().getFilesDir().getParent() + File.separator + "databases");
  }
  
  public static boolean cleanInternalFiles() {
    return deleteFilesInDir(Utils.getApp().getFilesDir());
  }
  
  public static boolean cleanInternalSP() {
    return deleteFilesInDir(Utils.getApp().getFilesDir().getParent() + File.separator + "shared_prefs");
  }
  
  private static boolean deleteDir(File paramFile) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: ifnonnull -> 10
    //   6: iload_1
    //   7: istore_2
    //   8: iload_2
    //   9: ireturn
    //   10: aload_0
    //   11: invokevirtual exists : ()Z
    //   14: ifne -> 22
    //   17: iconst_1
    //   18: istore_2
    //   19: goto -> 8
    //   22: iload_1
    //   23: istore_2
    //   24: aload_0
    //   25: invokevirtual isDirectory : ()Z
    //   28: ifeq -> 8
    //   31: aload_0
    //   32: invokevirtual listFiles : ()[Ljava/io/File;
    //   35: astore_3
    //   36: aload_3
    //   37: ifnull -> 110
    //   40: aload_3
    //   41: arraylength
    //   42: ifeq -> 110
    //   45: aload_3
    //   46: arraylength
    //   47: istore #4
    //   49: iconst_0
    //   50: istore #5
    //   52: iload #5
    //   54: iload #4
    //   56: if_icmpge -> 110
    //   59: aload_3
    //   60: iload #5
    //   62: aaload
    //   63: astore #6
    //   65: aload #6
    //   67: invokevirtual isFile : ()Z
    //   70: ifeq -> 89
    //   73: iload_1
    //   74: istore_2
    //   75: aload #6
    //   77: invokevirtual delete : ()Z
    //   80: ifeq -> 8
    //   83: iinc #5, 1
    //   86: goto -> 52
    //   89: aload #6
    //   91: invokevirtual isDirectory : ()Z
    //   94: ifeq -> 83
    //   97: aload #6
    //   99: invokestatic deleteDir : (Ljava/io/File;)Z
    //   102: ifne -> 83
    //   105: iload_1
    //   106: istore_2
    //   107: goto -> 8
    //   110: aload_0
    //   111: invokevirtual delete : ()Z
    //   114: istore_2
    //   115: goto -> 8
  }
  
  private static boolean deleteFilesInDir(File paramFile) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: ifnonnull -> 10
    //   6: iload_1
    //   7: istore_2
    //   8: iload_2
    //   9: ireturn
    //   10: aload_0
    //   11: invokevirtual exists : ()Z
    //   14: ifne -> 22
    //   17: iconst_1
    //   18: istore_2
    //   19: goto -> 8
    //   22: iload_1
    //   23: istore_2
    //   24: aload_0
    //   25: invokevirtual isDirectory : ()Z
    //   28: ifeq -> 8
    //   31: aload_0
    //   32: invokevirtual listFiles : ()[Ljava/io/File;
    //   35: astore_3
    //   36: aload_3
    //   37: ifnull -> 105
    //   40: aload_3
    //   41: arraylength
    //   42: ifeq -> 105
    //   45: aload_3
    //   46: arraylength
    //   47: istore #4
    //   49: iconst_0
    //   50: istore #5
    //   52: iload #5
    //   54: iload #4
    //   56: if_icmpge -> 105
    //   59: aload_3
    //   60: iload #5
    //   62: aaload
    //   63: astore_0
    //   64: aload_0
    //   65: invokevirtual isFile : ()Z
    //   68: ifeq -> 86
    //   71: iload_1
    //   72: istore_2
    //   73: aload_0
    //   74: invokevirtual delete : ()Z
    //   77: ifeq -> 8
    //   80: iinc #5, 1
    //   83: goto -> 52
    //   86: aload_0
    //   87: invokevirtual isDirectory : ()Z
    //   90: ifeq -> 80
    //   93: aload_0
    //   94: invokestatic deleteDir : (Ljava/io/File;)Z
    //   97: ifne -> 80
    //   100: iload_1
    //   101: istore_2
    //   102: goto -> 8
    //   105: iconst_1
    //   106: istore_2
    //   107: goto -> 8
  }
  
  public static boolean deleteFilesInDir(String paramString) {
    return deleteFilesInDir(getFileByPath(paramString));
  }
  
  private static File getFileByPath(String paramString) {
    return isSpace(paramString) ? null : new File(paramString);
  }
  
  private static boolean isSpace(String paramString) {
    boolean bool = true;
    if (paramString == null)
      return bool; 
    byte b = 0;
    int i = paramString.length();
    while (true) {
      boolean bool1 = bool;
      if (b < i) {
        if (!Character.isWhitespace(paramString.charAt(b)))
          return false; 
        b++;
        continue;
      } 
      return bool1;
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/CleanUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */