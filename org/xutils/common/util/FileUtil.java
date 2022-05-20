package org.xutils.common.util;

import android.os.Environment;
import android.os.StatFs;
import java.io.File;
import org.xutils.x;

public class FileUtil {
  public static boolean copy(String paramString1, String paramString2) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: new java/io/File
    //   5: dup
    //   6: aload_0
    //   7: invokespecial <init> : (Ljava/lang/String;)V
    //   10: astore_3
    //   11: aload_3
    //   12: invokevirtual exists : ()Z
    //   15: ifne -> 22
    //   18: iconst_0
    //   19: istore_2
    //   20: iload_2
    //   21: ireturn
    //   22: new java/io/File
    //   25: dup
    //   26: aload_1
    //   27: invokespecial <init> : (Ljava/lang/String;)V
    //   30: astore #4
    //   32: aload #4
    //   34: invokestatic deleteFileOrDir : (Ljava/io/File;)Z
    //   37: pop
    //   38: aload #4
    //   40: invokevirtual getParentFile : ()Ljava/io/File;
    //   43: astore_0
    //   44: aload_0
    //   45: invokevirtual exists : ()Z
    //   48: ifne -> 58
    //   51: aload_0
    //   52: invokevirtual mkdirs : ()Z
    //   55: ifeq -> 121
    //   58: aconst_null
    //   59: astore #5
    //   61: aconst_null
    //   62: astore #6
    //   64: aconst_null
    //   65: astore #7
    //   67: aconst_null
    //   68: astore #8
    //   70: aconst_null
    //   71: astore #9
    //   73: aload #5
    //   75: astore_1
    //   76: aload #7
    //   78: astore #10
    //   80: new java/io/FileInputStream
    //   83: astore_0
    //   84: aload #5
    //   86: astore_1
    //   87: aload #7
    //   89: astore #10
    //   91: aload_0
    //   92: aload_3
    //   93: invokespecial <init> : (Ljava/io/File;)V
    //   96: new java/io/FileOutputStream
    //   99: astore_1
    //   100: aload_1
    //   101: aload #4
    //   103: invokespecial <init> : (Ljava/io/File;)V
    //   106: aload_0
    //   107: aload_1
    //   108: invokestatic copy : (Ljava/io/InputStream;Ljava/io/OutputStream;)V
    //   111: iconst_1
    //   112: istore_2
    //   113: aload_0
    //   114: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   117: aload_1
    //   118: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   121: goto -> 20
    //   124: astore #5
    //   126: aload #6
    //   128: astore_0
    //   129: aload_0
    //   130: astore_1
    //   131: aload #9
    //   133: astore #10
    //   135: aload #5
    //   137: invokevirtual getMessage : ()Ljava/lang/String;
    //   140: aload #5
    //   142: invokestatic d : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   145: iconst_0
    //   146: istore_2
    //   147: aload_0
    //   148: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   151: aload #9
    //   153: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   156: goto -> 121
    //   159: astore_0
    //   160: aload_1
    //   161: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   164: aload #10
    //   166: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   169: aload_0
    //   170: athrow
    //   171: astore #5
    //   173: aload_0
    //   174: astore_1
    //   175: aload #8
    //   177: astore #10
    //   179: aload #5
    //   181: astore_0
    //   182: goto -> 160
    //   185: astore #5
    //   187: aload_1
    //   188: astore #10
    //   190: aload_0
    //   191: astore_1
    //   192: aload #5
    //   194: astore_0
    //   195: goto -> 160
    //   198: astore #5
    //   200: goto -> 129
    //   203: astore #5
    //   205: aload_1
    //   206: astore #9
    //   208: goto -> 129
    // Exception table:
    //   from	to	target	type
    //   80	84	124	java/lang/Throwable
    //   80	84	159	finally
    //   91	96	124	java/lang/Throwable
    //   91	96	159	finally
    //   96	106	198	java/lang/Throwable
    //   96	106	171	finally
    //   106	111	203	java/lang/Throwable
    //   106	111	185	finally
    //   135	145	159	finally
  }
  
  public static Boolean existsSdcard() {
    return Boolean.valueOf(Environment.getExternalStorageState().equals("mounted"));
  }
  
  public static File getCacheDir(String paramString) {
    File file;
    if (existsSdcard().booleanValue()) {
      File file1 = x.app().getExternalCacheDir();
      if (file1 == null) {
        file = new File(Environment.getExternalStorageDirectory(), "Android/data/" + x.app().getPackageName() + "/cache/" + paramString);
      } else {
        file = new File(file1, (String)file);
      } 
    } else {
      file = new File(x.app().getCacheDir(), (String)file);
    } 
    null = file;
    if (!file.exists()) {
      if (file.mkdirs())
        return file; 
    } else {
      return null;
    } 
    return null;
  }
  
  public static long getDiskAvailableSize() {
    if (!existsSdcard().booleanValue())
      return 0L; 
    StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
    null = statFs.getBlockSize();
    return statFs.getAvailableBlocks() * null;
  }
  
  public static long getFileOrDirSize(File paramFile) {
    if (!paramFile.exists())
      return 0L; 
    if (!paramFile.isDirectory())
      return paramFile.length(); 
    long l2 = 0L;
    File[] arrayOfFile = paramFile.listFiles();
    long l1 = l2;
    if (arrayOfFile != null) {
      int i = arrayOfFile.length;
      byte b = 0;
      while (true) {
        l1 = l2;
        if (b < i) {
          l2 += getFileOrDirSize(arrayOfFile[b]);
          b++;
          continue;
        } 
        return l1;
      } 
    } 
    return l1;
  }
  
  public static boolean isDiskAvailable() {
    return (getDiskAvailableSize() > 10485760L);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/common/util/FileUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */