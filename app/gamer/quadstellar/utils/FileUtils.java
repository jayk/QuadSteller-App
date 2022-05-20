package app.gamer.quadstellar.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;

public final class FileUtils {
  private static final String CAPTURE_FILE_NAME = "_capture.jpg";
  
  public static boolean bitmapToFile(Bitmap paramBitmap, String paramString) {
    Bitmap bitmap2;
    boolean bool = false;
    if (paramBitmap == null)
      return false; 
    BufferedOutputStream bufferedOutputStream1 = null;
    Bitmap bitmap1 = null;
    BufferedOutputStream bufferedOutputStream2 = bufferedOutputStream1;
    try {
      BufferedOutputStream bufferedOutputStream = new BufferedOutputStream();
      bufferedOutputStream2 = bufferedOutputStream1;
      FileOutputStream fileOutputStream = new FileOutputStream();
      bufferedOutputStream2 = bufferedOutputStream1;
      this(paramString);
      bufferedOutputStream2 = bufferedOutputStream1;
      this(fileOutputStream, 8192);
      try {
        boolean bool1 = paramBitmap.compress(Bitmap.CompressFormat.PNG, 70, bufferedOutputStream);
        bool = bool1;
      } catch (FileNotFoundException fileNotFoundException) {
      
      } finally {
        paramBitmap = null;
      } 
    } catch (FileNotFoundException fileNotFoundException) {
      paramBitmap = bitmap1;
      bitmap2 = paramBitmap;
      fileNotFoundException.printStackTrace();
      closeIO(new Closeable[] { (Closeable)paramBitmap });
      return bool;
    } finally {}
    closeIO(new Closeable[] { (Closeable)bitmap2 });
    throw paramBitmap;
  }
  
  public static File buildPath(File paramFile, String... paramVarArgs) {
    int i = paramVarArgs.length;
    for (byte b = 0; b < i; b++) {
      String str = paramVarArgs[b];
      if (paramFile == null) {
        paramFile = new File(str);
      } else {
        paramFile = new File(paramFile, str);
      } 
    } 
    return paramFile;
  }
  
  public static String bytes2kb(long paramLong) {
    BigDecimal bigDecimal = new BigDecimal(paramLong);
    float f = bigDecimal.divide(new BigDecimal(1048576), 2, 0).floatValue();
    if (f > 1.0F)
      return f + "MB"; 
    f = bigDecimal.divide(new BigDecimal(1024), 2, 0).floatValue();
    return f + "KB";
  }
  
  public static boolean checkSDcard() {
    return "mounted".equals(Environment.getExternalStorageState());
  }
  
  public static void closeIO(Closeable... paramVarArgs) {
    if (paramVarArgs != null && paramVarArgs.length > 0) {
      int i = paramVarArgs.length;
      byte b = 0;
      while (true) {
        if (b < i) {
          Closeable closeable = paramVarArgs[b];
          if (closeable != null)
            try {
              closeable.close();
            } catch (IOException iOException) {
              iOException.printStackTrace();
            }  
          b++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  public static String convertToFileName(String paramString1, String paramString2, String paramString3) {
    return paramString1.replace(paramString2, paramString3);
  }
  
  public static void copyFile(File paramFile1, File paramFile2) {
    // Byte code:
    //   0: aload_0
    //   1: ifnull -> 11
    //   4: aload_0
    //   5: invokevirtual exists : ()Z
    //   8: ifne -> 12
    //   11: return
    //   12: aload_1
    //   13: ifnull -> 11
    //   16: aconst_null
    //   17: astore_2
    //   18: aconst_null
    //   19: astore_3
    //   20: aconst_null
    //   21: astore #4
    //   23: aconst_null
    //   24: astore #5
    //   26: aconst_null
    //   27: astore #6
    //   29: aload_2
    //   30: astore #7
    //   32: aload #4
    //   34: astore #8
    //   36: new java/io/FileInputStream
    //   39: astore #9
    //   41: aload_2
    //   42: astore #7
    //   44: aload #4
    //   46: astore #8
    //   48: aload #9
    //   50: aload_0
    //   51: invokespecial <init> : (Ljava/io/File;)V
    //   54: aload_1
    //   55: invokevirtual exists : ()Z
    //   58: ifne -> 66
    //   61: aload_1
    //   62: invokevirtual createNewFile : ()Z
    //   65: pop
    //   66: new java/io/FileOutputStream
    //   69: astore_0
    //   70: aload_0
    //   71: aload_1
    //   72: invokespecial <init> : (Ljava/io/File;)V
    //   75: aload #9
    //   77: aload_0
    //   78: invokestatic copyFileFast : (Ljava/io/FileInputStream;Ljava/io/FileOutputStream;)V
    //   81: iconst_2
    //   82: anewarray java/io/Closeable
    //   85: dup
    //   86: iconst_0
    //   87: aload #9
    //   89: aastore
    //   90: dup
    //   91: iconst_1
    //   92: aload_0
    //   93: aastore
    //   94: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   97: goto -> 11
    //   100: astore_1
    //   101: aload #6
    //   103: astore #9
    //   105: aload_3
    //   106: astore_0
    //   107: aload_0
    //   108: astore #7
    //   110: aload #9
    //   112: astore #8
    //   114: aload_1
    //   115: invokevirtual printStackTrace : ()V
    //   118: iconst_2
    //   119: anewarray java/io/Closeable
    //   122: dup
    //   123: iconst_0
    //   124: aload_0
    //   125: aastore
    //   126: dup
    //   127: iconst_1
    //   128: aload #9
    //   130: aastore
    //   131: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   134: goto -> 11
    //   137: astore_0
    //   138: iconst_2
    //   139: anewarray java/io/Closeable
    //   142: dup
    //   143: iconst_0
    //   144: aload #7
    //   146: aastore
    //   147: dup
    //   148: iconst_1
    //   149: aload #8
    //   151: aastore
    //   152: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   155: aload_0
    //   156: athrow
    //   157: astore_0
    //   158: aload #9
    //   160: astore #7
    //   162: aload #5
    //   164: astore #8
    //   166: goto -> 138
    //   169: astore_1
    //   170: aload #9
    //   172: astore #7
    //   174: aload_0
    //   175: astore #8
    //   177: aload_1
    //   178: astore_0
    //   179: goto -> 138
    //   182: astore_1
    //   183: aload #9
    //   185: astore_0
    //   186: aload #6
    //   188: astore #9
    //   190: goto -> 107
    //   193: astore_1
    //   194: aload_0
    //   195: astore #7
    //   197: aload #9
    //   199: astore_0
    //   200: aload #7
    //   202: astore #9
    //   204: goto -> 107
    // Exception table:
    //   from	to	target	type
    //   36	41	100	java/lang/Exception
    //   36	41	137	finally
    //   48	54	100	java/lang/Exception
    //   48	54	137	finally
    //   54	66	182	java/lang/Exception
    //   54	66	157	finally
    //   66	75	182	java/lang/Exception
    //   66	75	157	finally
    //   75	81	193	java/lang/Exception
    //   75	81	169	finally
    //   114	118	137	finally
  }
  
  public static void copyFileFast(FileInputStream paramFileInputStream, FileOutputStream paramFileOutputStream) throws IOException {
    FileChannel fileChannel1 = paramFileInputStream.getChannel();
    FileChannel fileChannel2 = paramFileOutputStream.getChannel();
    fileChannel1.transferTo(0L, fileChannel1.size(), fileChannel2);
  }
  
  public static boolean deleteFile(String paramString) {
    return (new File(paramString)).delete();
  }
  
  public static long getAvailableExternalMemorySize() {
    if (checkSDcard()) {
      StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
      null = statFs.getBlockSize();
      return statFs.getAvailableBlocks() * null;
    } 
    return -1L;
  }
  
  public static File getCaptureFile(Context paramContext) {
    String str = SystemClock.elapsedRealtime() + "_capture.jpg";
    return new File(getStoragePathIfMounted(paramContext), str);
  }
  
  public static File getFile(Context paramContext, String paramString) {
    return new File(paramContext.getExternalCacheDir(), paramString);
  }
  
  public static String getFileName(String paramString) {
    int i = paramString.lastIndexOf("/");
    int j = paramString.lastIndexOf(".");
    return (i != -1 && j != -1) ? paramString.substring(i + 1, j) : null;
  }
  
  public static Uri getFileUri(Context paramContext, String paramString) {
    return Uri.fromFile(new File(paramContext.getExternalCacheDir(), paramString));
  }
  
  public static long getLocalFileSize(File paramFile) {
    long l = 0L;
    if (paramFile != null)
      l = paramFile.length(); 
    return l;
  }
  
  public static long getLocalFileSize(String paramString1, String paramString2) {
    return (new File(paramString1, paramString2)).length();
  }
  
  public static File getSaveFile(String paramString1, String paramString2) {
    File file = new File(getSavePath(paramString1) + File.separator + paramString2);
    try {
      file.createNewFile();
    } catch (IOException iOException) {
      iOException.printStackTrace();
    } 
    return file;
  }
  
  public static File getSaveFolder(String paramString) {
    File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + paramString + File.separator);
    file.mkdirs();
    return file;
  }
  
  public static String getSavePath(String paramString) {
    return getSaveFolder(paramString).getAbsolutePath();
  }
  
  public static String getStoragePathIfMounted(Context paramContext) {
    File file2 = paramContext.getFilesDir();
    if (Environment.getExternalStorageState().equals("mounted"))
      file2 = Environment.getExternalStorageDirectory(); 
    File file1 = buildPath(file2, new String[] { paramContext.getString(2131296421) });
    if (!file1.exists())
      file1.mkdirs(); 
    Log.e("storage path", file1.getAbsolutePath());
    return file1.getAbsolutePath();
  }
  
  public static final byte[] input2byte(InputStream paramInputStream) {
    if (paramInputStream == null)
      return null; 
    IOException iOException = null;
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    byte[] arrayOfByte = new byte[1024];
    try {
      while (true) {
        int i = paramInputStream.read(arrayOfByte, 0, arrayOfByte.length);
        if (i > 0) {
          byteArrayOutputStream.write(arrayOfByte, 0, i);
          continue;
        } 
        return byteArrayOutputStream.toByteArray();
      } 
    } catch (IOException iOException1) {
      iOException1.printStackTrace();
      closeIO(new Closeable[] { byteArrayOutputStream });
    } finally {
      closeIO(new Closeable[] { byteArrayOutputStream });
    } 
    return (byte[])paramInputStream;
  }
  
  public static String inputStream2String(InputStream paramInputStream) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aload_0
    //   3: ifnonnull -> 10
    //   6: aload_1
    //   7: astore_0
    //   8: aload_0
    //   9: areturn
    //   10: aconst_null
    //   11: astore_2
    //   12: new java/io/BufferedReader
    //   15: astore_3
    //   16: new java/io/InputStreamReader
    //   19: astore #4
    //   21: aload #4
    //   23: aload_0
    //   24: invokespecial <init> : (Ljava/io/InputStream;)V
    //   27: aload_3
    //   28: aload #4
    //   30: invokespecial <init> : (Ljava/io/Reader;)V
    //   33: new java/lang/StringBuilder
    //   36: astore #4
    //   38: aload #4
    //   40: invokespecial <init> : ()V
    //   43: aload_3
    //   44: invokevirtual readLine : ()Ljava/lang/String;
    //   47: astore_2
    //   48: aload_2
    //   49: ifnull -> 90
    //   52: aload #4
    //   54: aload_2
    //   55: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   58: pop
    //   59: goto -> 43
    //   62: astore_2
    //   63: iconst_1
    //   64: anewarray java/io/Closeable
    //   67: dup
    //   68: iconst_0
    //   69: aload_0
    //   70: aastore
    //   71: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   74: aload_1
    //   75: astore_0
    //   76: aload #4
    //   78: ifnull -> 8
    //   81: aload #4
    //   83: invokevirtual toString : ()Ljava/lang/String;
    //   86: astore_0
    //   87: goto -> 8
    //   90: iconst_1
    //   91: anewarray java/io/Closeable
    //   94: dup
    //   95: iconst_0
    //   96: aload_0
    //   97: aastore
    //   98: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   101: goto -> 74
    //   104: astore #4
    //   106: iconst_1
    //   107: anewarray java/io/Closeable
    //   110: dup
    //   111: iconst_0
    //   112: aload_0
    //   113: aastore
    //   114: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   117: aload #4
    //   119: athrow
    //   120: astore #4
    //   122: goto -> 106
    //   125: astore #4
    //   127: aload_2
    //   128: astore #4
    //   130: goto -> 63
    // Exception table:
    //   from	to	target	type
    //   12	43	125	java/lang/Exception
    //   12	43	104	finally
    //   43	48	62	java/lang/Exception
    //   43	48	120	finally
    //   52	59	62	java/lang/Exception
    //   52	59	120	finally
  }
  
  public static boolean isExists(String paramString) {
    return (new File(paramString)).exists();
  }
  
  public static boolean isExists(String paramString1, String paramString2) {
    return (new File(paramString1, paramString2)).exists();
  }
  
  public static String readFile(String paramString) {
    Exception exception2 = null;
    try {
      FileInputStream fileInputStream2 = new FileInputStream();
      this(paramString);
      FileInputStream fileInputStream1 = fileInputStream2;
    } catch (Exception exception1) {
      exception1.printStackTrace();
      exception1 = exception2;
    } 
    return inputStream2String((InputStream)exception1);
  }
  
  public static String readFileFromAssets(Context paramContext, String paramString) {
    Exception exception2 = null;
    try {
      InputStream inputStream = paramContext.getResources().getAssets().open(paramString);
    } catch (Exception exception1) {
      exception1.printStackTrace();
      exception1 = exception2;
    } 
    return inputStream2String((InputStream)exception1);
  }
  
  public static void saveFileCache(byte[] paramArrayOfbyte, String paramString1, String paramString2) {
    // Byte code:
    //   0: new java/io/File
    //   3: dup
    //   4: aload_1
    //   5: invokespecial <init> : (Ljava/lang/String;)V
    //   8: invokevirtual mkdirs : ()Z
    //   11: pop
    //   12: new java/io/File
    //   15: dup
    //   16: aload_1
    //   17: aload_2
    //   18: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
    //   21: astore_3
    //   22: new java/io/ByteArrayInputStream
    //   25: dup
    //   26: aload_0
    //   27: invokespecial <init> : ([B)V
    //   30: astore #4
    //   32: aconst_null
    //   33: astore_2
    //   34: aconst_null
    //   35: astore #5
    //   37: aload_3
    //   38: invokevirtual exists : ()Z
    //   41: ifne -> 120
    //   44: aload_2
    //   45: astore_0
    //   46: aload_3
    //   47: invokevirtual createNewFile : ()Z
    //   50: pop
    //   51: aload_2
    //   52: astore_0
    //   53: new java/io/FileOutputStream
    //   56: astore_1
    //   57: aload_2
    //   58: astore_0
    //   59: aload_1
    //   60: aload_3
    //   61: invokespecial <init> : (Ljava/io/File;)V
    //   64: sipush #1024
    //   67: newarray byte
    //   69: astore_0
    //   70: aload #4
    //   72: aload_0
    //   73: invokevirtual read : ([B)I
    //   76: istore #6
    //   78: iconst_m1
    //   79: iload #6
    //   81: if_icmpeq -> 121
    //   84: aload_1
    //   85: aload_0
    //   86: iconst_0
    //   87: iload #6
    //   89: invokevirtual write : ([BII)V
    //   92: goto -> 70
    //   95: astore_0
    //   96: aload_0
    //   97: astore_2
    //   98: aload_1
    //   99: astore_0
    //   100: aload_2
    //   101: invokevirtual printStackTrace : ()V
    //   104: iconst_2
    //   105: anewarray java/io/Closeable
    //   108: dup
    //   109: iconst_0
    //   110: aload #4
    //   112: aastore
    //   113: dup
    //   114: iconst_1
    //   115: aload_1
    //   116: aastore
    //   117: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   120: return
    //   121: aload_1
    //   122: invokevirtual flush : ()V
    //   125: iconst_2
    //   126: anewarray java/io/Closeable
    //   129: dup
    //   130: iconst_0
    //   131: aload #4
    //   133: aastore
    //   134: dup
    //   135: iconst_1
    //   136: aload_1
    //   137: aastore
    //   138: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   141: goto -> 120
    //   144: astore_2
    //   145: aload_0
    //   146: astore_1
    //   147: iconst_2
    //   148: anewarray java/io/Closeable
    //   151: dup
    //   152: iconst_0
    //   153: aload #4
    //   155: aastore
    //   156: dup
    //   157: iconst_1
    //   158: aload_1
    //   159: aastore
    //   160: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   163: aload_2
    //   164: athrow
    //   165: astore_0
    //   166: aload_0
    //   167: astore_2
    //   168: goto -> 147
    //   171: astore_2
    //   172: aload #5
    //   174: astore_1
    //   175: goto -> 98
    // Exception table:
    //   from	to	target	type
    //   46	51	171	java/lang/Exception
    //   46	51	144	finally
    //   53	57	171	java/lang/Exception
    //   53	57	144	finally
    //   59	64	171	java/lang/Exception
    //   59	64	144	finally
    //   64	70	95	java/lang/Exception
    //   64	70	165	finally
    //   70	78	95	java/lang/Exception
    //   70	78	165	finally
    //   84	92	95	java/lang/Exception
    //   84	92	165	finally
    //   100	104	144	finally
    //   121	125	95	java/lang/Exception
    //   121	125	165	finally
  }
  
  public static void uri2File(Activity paramActivity, Uri paramUri) {}
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/utils/FileUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */