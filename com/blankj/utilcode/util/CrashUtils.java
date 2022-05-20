package com.blankj.utilcode.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.support.annotation.NonNull;
import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class CrashUtils {
  private static final String CRASH_HEAD;
  
  private static final Thread.UncaughtExceptionHandler DEFAULT_UNCAUGHT_EXCEPTION_HANDLER;
  
  private static final String FILE_SEP = System.getProperty("file.separator");
  
  private static final Format FORMAT = new SimpleDateFormat("MM-dd HH-mm-ss", Locale.getDefault());
  
  private static final Thread.UncaughtExceptionHandler UNCAUGHT_EXCEPTION_HANDLER;
  
  private static String defaultDir;
  
  private static String dir;
  
  private static ExecutorService sExecutor;
  
  private static int versionCode;
  
  private static String versionName;
  
  static {
    try {
      PackageInfo packageInfo = Utils.getApp().getPackageManager().getPackageInfo(Utils.getApp().getPackageName(), 0);
      if (packageInfo != null) {
        versionName = packageInfo.versionName;
        versionCode = packageInfo.versionCode;
      } 
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      nameNotFoundException.printStackTrace();
    } 
    CRASH_HEAD = "\n************* Crash Log Head ****************\nDevice Manufacturer: " + Build.MANUFACTURER + "\nDevice Model       : " + Build.MODEL + "\nAndroid Version    : " + Build.VERSION.RELEASE + "\nAndroid SDK        : " + Build.VERSION.SDK_INT + "\nApp VersionName    : " + versionName + "\nApp VersionCode    : " + versionCode + "\n************* Crash Log Head ****************\n\n";
    DEFAULT_UNCAUGHT_EXCEPTION_HANDLER = Thread.getDefaultUncaughtExceptionHandler();
    UNCAUGHT_EXCEPTION_HANDLER = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread param1Thread, final Throwable e) {
          if (e == null) {
            Process.killProcess(Process.myPid());
            System.exit(0);
            return;
          } 
          Date date = new Date(System.currentTimeMillis());
          String str2 = CrashUtils.FORMAT.format(date) + ".txt";
          StringBuilder stringBuilder = new StringBuilder();
          if (CrashUtils.dir == null) {
            str1 = CrashUtils.defaultDir;
          } else {
            str1 = CrashUtils.dir;
          } 
          final String fullPath = stringBuilder.append(str1).append(str2).toString();
          if (CrashUtils.createOrExistsFile(str1)) {
            if (CrashUtils.sExecutor == null)
              CrashUtils.access$402(Executors.newSingleThreadExecutor()); 
            CrashUtils.sExecutor.execute(new Runnable() {
                  public void run() {
                    // Byte code:
                    //   0: aconst_null
                    //   1: astore_1
                    //   2: aconst_null
                    //   3: astore_2
                    //   4: aload_1
                    //   5: astore_3
                    //   6: new java/io/PrintWriter
                    //   9: astore #4
                    //   11: aload_1
                    //   12: astore_3
                    //   13: new java/io/FileWriter
                    //   16: astore #5
                    //   18: aload_1
                    //   19: astore_3
                    //   20: aload #5
                    //   22: aload_0
                    //   23: getfield val$fullPath : Ljava/lang/String;
                    //   26: iconst_0
                    //   27: invokespecial <init> : (Ljava/lang/String;Z)V
                    //   30: aload_1
                    //   31: astore_3
                    //   32: aload #4
                    //   34: aload #5
                    //   36: invokespecial <init> : (Ljava/io/Writer;)V
                    //   39: aload #4
                    //   41: invokestatic access$500 : ()Ljava/lang/String;
                    //   44: invokevirtual write : (Ljava/lang/String;)V
                    //   47: aload_0
                    //   48: getfield val$e : Ljava/lang/Throwable;
                    //   51: aload #4
                    //   53: invokevirtual printStackTrace : (Ljava/io/PrintWriter;)V
                    //   56: aload_0
                    //   57: getfield val$e : Ljava/lang/Throwable;
                    //   60: invokevirtual getCause : ()Ljava/lang/Throwable;
                    //   63: astore_3
                    //   64: aload_3
                    //   65: ifnull -> 82
                    //   68: aload_3
                    //   69: aload #4
                    //   71: invokevirtual printStackTrace : (Ljava/io/PrintWriter;)V
                    //   74: aload_3
                    //   75: invokevirtual getCause : ()Ljava/lang/Throwable;
                    //   78: astore_3
                    //   79: goto -> 64
                    //   82: aload #4
                    //   84: ifnull -> 146
                    //   87: aload #4
                    //   89: invokevirtual close : ()V
                    //   92: return
                    //   93: astore_1
                    //   94: aload_2
                    //   95: astore #4
                    //   97: aload #4
                    //   99: astore_3
                    //   100: aload_1
                    //   101: invokevirtual printStackTrace : ()V
                    //   104: aload #4
                    //   106: ifnull -> 92
                    //   109: aload #4
                    //   111: invokevirtual close : ()V
                    //   114: goto -> 92
                    //   117: astore #4
                    //   119: aload_3
                    //   120: astore_1
                    //   121: aload_1
                    //   122: ifnull -> 129
                    //   125: aload_1
                    //   126: invokevirtual close : ()V
                    //   129: aload #4
                    //   131: athrow
                    //   132: astore_3
                    //   133: aload #4
                    //   135: astore_1
                    //   136: aload_3
                    //   137: astore #4
                    //   139: goto -> 121
                    //   142: astore_1
                    //   143: goto -> 97
                    //   146: goto -> 92
                    // Exception table:
                    //   from	to	target	type
                    //   6	11	93	java/io/IOException
                    //   6	11	117	finally
                    //   13	18	93	java/io/IOException
                    //   13	18	117	finally
                    //   20	30	93	java/io/IOException
                    //   20	30	117	finally
                    //   32	39	93	java/io/IOException
                    //   32	39	117	finally
                    //   39	64	142	java/io/IOException
                    //   39	64	132	finally
                    //   68	79	142	java/io/IOException
                    //   68	79	132	finally
                    //   100	104	117	finally
                  }
                });
            if (CrashUtils.DEFAULT_UNCAUGHT_EXCEPTION_HANDLER != null)
              CrashUtils.DEFAULT_UNCAUGHT_EXCEPTION_HANDLER.uncaughtException(param1Thread, e); 
          } 
        }
      };
  }
  
  private CrashUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  private static boolean createOrExistsDir(File paramFile) {
    return (paramFile != null && (paramFile.exists() ? paramFile.isDirectory() : paramFile.mkdirs()));
  }
  
  private static boolean createOrExistsFile(String paramString) {
    boolean bool1 = false;
    File file = new File(paramString);
    if (file.exists())
      return file.isFile(); 
    boolean bool2 = bool1;
    if (createOrExistsDir(file.getParentFile()))
      try {
        bool2 = file.createNewFile();
      } catch (IOException iOException) {
        iOException.printStackTrace();
        bool2 = bool1;
      }  
    return bool2;
  }
  
  public static void init() {
    init("");
  }
  
  public static void init(@NonNull File paramFile) {
    init(paramFile.getAbsolutePath());
  }
  
  public static void init(String paramString) {
    if (isSpace(paramString)) {
      dir = null;
    } else {
      if (!paramString.endsWith(FILE_SEP))
        paramString = paramString + FILE_SEP; 
      dir = paramString;
    } 
    if ("mounted".equals(Environment.getExternalStorageState()) && Utils.getApp().getExternalCacheDir() != null) {
      defaultDir = Utils.getApp().getExternalCacheDir() + FILE_SEP + "crash" + FILE_SEP;
    } else {
      defaultDir = Utils.getApp().getCacheDir() + FILE_SEP + "crash" + FILE_SEP;
    } 
    Thread.setDefaultUncaughtExceptionHandler(UNCAUGHT_EXCEPTION_HANDLER);
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


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/CrashUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */