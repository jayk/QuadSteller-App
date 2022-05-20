package com.blankj.utilcode.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public final class FileIOUtils {
  private static final String LINE_SEP = System.getProperty("line.separator");
  
  private static int sBufferSize = 8192;
  
  private FileIOUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  private static boolean createOrExistsDir(File paramFile) {
    return (paramFile != null && (paramFile.exists() ? paramFile.isDirectory() : paramFile.mkdirs()));
  }
  
  private static boolean createOrExistsFile(File paramFile) {
    boolean bool1 = false;
    if (paramFile == null)
      return bool1; 
    if (paramFile.exists())
      return paramFile.isFile(); 
    boolean bool2 = bool1;
    if (createOrExistsDir(paramFile.getParentFile()))
      try {
        bool2 = paramFile.createNewFile();
      } catch (IOException iOException) {
        iOException.printStackTrace();
        bool2 = bool1;
      }  
    return bool2;
  }
  
  private static boolean createOrExistsFile(String paramString) {
    return createOrExistsFile(getFileByPath(paramString));
  }
  
  private static File getFileByPath(String paramString) {
    return isSpace(paramString) ? null : new File(paramString);
  }
  
  private static boolean isFileExists(File paramFile) {
    return (paramFile != null && paramFile.exists());
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
  
  public static byte[] readFile2BytesByChannel(File paramFile) {
    byte[] arrayOfByte2;
    byte[] arrayOfByte1 = null;
    if (!isFileExists(paramFile))
      return arrayOfByte1; 
    FileChannel fileChannel2 = null;
    FileChannel fileChannel3 = null;
    FileChannel fileChannel4 = fileChannel3;
    FileChannel fileChannel1 = fileChannel2;
    try {
      RandomAccessFile randomAccessFile = new RandomAccessFile();
      fileChannel4 = fileChannel3;
      fileChannel1 = fileChannel2;
      this(paramFile, "r");
      fileChannel4 = fileChannel3;
      fileChannel1 = fileChannel2;
      FileChannel fileChannel = randomAccessFile.getChannel();
      fileChannel4 = fileChannel;
      fileChannel1 = fileChannel;
      ByteBuffer byteBuffer = ByteBuffer.allocate((int)fileChannel.size());
      while (true) {
        fileChannel4 = fileChannel;
        fileChannel1 = fileChannel;
        if (fileChannel.read(byteBuffer) <= 0) {
          fileChannel4 = fileChannel;
          fileChannel1 = fileChannel;
          byte[] arrayOfByte = byteBuffer.array();
          arrayOfByte2 = arrayOfByte;
          return arrayOfByte2;
        } 
      } 
    } catch (IOException iOException) {
      fileChannel1 = fileChannel4;
      iOException.printStackTrace();
      CloseUtils.closeIO(new Closeable[] { fileChannel4 });
    } finally {
      CloseUtils.closeIO(new Closeable[] { (Closeable)arrayOfByte2 });
    } 
    return arrayOfByte2;
  }
  
  public static byte[] readFile2BytesByChannel(String paramString) {
    return readFile2BytesByChannel(getFileByPath(paramString));
  }
  
  public static byte[] readFile2BytesByMap(File paramFile) {
    if (!isFileExists(paramFile))
      return null; 
    FileChannel fileChannel1 = null;
    FileChannel fileChannel2 = null;
    FileChannel fileChannel3 = fileChannel2;
    FileChannel fileChannel4 = fileChannel1;
    try {
      RandomAccessFile randomAccessFile = new RandomAccessFile();
      fileChannel3 = fileChannel2;
      fileChannel4 = fileChannel1;
      this(paramFile, "r");
      fileChannel3 = fileChannel2;
      fileChannel4 = fileChannel1;
      FileChannel fileChannel = randomAccessFile.getChannel();
      fileChannel3 = fileChannel;
      fileChannel4 = fileChannel;
      int i = (int)fileChannel.size();
      fileChannel3 = fileChannel;
      fileChannel4 = fileChannel;
      MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, i).load();
      fileChannel3 = fileChannel;
      fileChannel4 = fileChannel;
      byte[] arrayOfByte2 = new byte[i];
      fileChannel3 = fileChannel;
      fileChannel4 = fileChannel;
      mappedByteBuffer.get(arrayOfByte2, 0, i);
      CloseUtils.closeIO(new Closeable[] { fileChannel });
    } catch (IOException iOException) {
      fileChannel4 = fileChannel3;
      iOException.printStackTrace();
      CloseUtils.closeIO(new Closeable[] { fileChannel3 });
    } finally {
      CloseUtils.closeIO(new Closeable[] { fileChannel4 });
    } 
    return (byte[])paramFile;
  }
  
  public static byte[] readFile2BytesByMap(String paramString) {
    return readFile2BytesByMap(getFileByPath(paramString));
  }
  
  public static byte[] readFile2BytesByStream(File paramFile) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aload_0
    //   3: invokestatic isFileExists : (Ljava/io/File;)Z
    //   6: ifne -> 13
    //   9: aload_1
    //   10: astore_2
    //   11: aload_2
    //   12: areturn
    //   13: aconst_null
    //   14: astore_3
    //   15: aconst_null
    //   16: astore #4
    //   18: aconst_null
    //   19: astore #5
    //   21: aconst_null
    //   22: astore #6
    //   24: aconst_null
    //   25: astore #7
    //   27: aload_3
    //   28: astore_2
    //   29: aload #5
    //   31: astore #8
    //   33: new java/io/FileInputStream
    //   36: astore #9
    //   38: aload_3
    //   39: astore_2
    //   40: aload #5
    //   42: astore #8
    //   44: aload #9
    //   46: aload_0
    //   47: invokespecial <init> : (Ljava/io/File;)V
    //   50: new java/io/ByteArrayOutputStream
    //   53: astore_0
    //   54: aload_0
    //   55: invokespecial <init> : ()V
    //   58: getstatic com/blankj/utilcode/util/FileIOUtils.sBufferSize : I
    //   61: newarray byte
    //   63: astore_2
    //   64: aload #9
    //   66: aload_2
    //   67: iconst_0
    //   68: getstatic com/blankj/utilcode/util/FileIOUtils.sBufferSize : I
    //   71: invokevirtual read : ([BII)I
    //   74: istore #10
    //   76: iload #10
    //   78: iconst_m1
    //   79: if_icmpeq -> 135
    //   82: aload_0
    //   83: aload_2
    //   84: iconst_0
    //   85: iload #10
    //   87: invokevirtual write : ([BII)V
    //   90: goto -> 64
    //   93: astore_2
    //   94: aload_0
    //   95: astore #7
    //   97: aload #9
    //   99: astore_0
    //   100: aload_2
    //   101: astore #9
    //   103: aload_0
    //   104: astore_2
    //   105: aload #7
    //   107: astore #8
    //   109: aload #9
    //   111: invokevirtual printStackTrace : ()V
    //   114: iconst_2
    //   115: anewarray java/io/Closeable
    //   118: dup
    //   119: iconst_0
    //   120: aload_0
    //   121: aastore
    //   122: dup
    //   123: iconst_1
    //   124: aload #7
    //   126: aastore
    //   127: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   130: aload_1
    //   131: astore_2
    //   132: goto -> 11
    //   135: aload_0
    //   136: invokevirtual toByteArray : ()[B
    //   139: astore_2
    //   140: iconst_2
    //   141: anewarray java/io/Closeable
    //   144: dup
    //   145: iconst_0
    //   146: aload #9
    //   148: aastore
    //   149: dup
    //   150: iconst_1
    //   151: aload_0
    //   152: aastore
    //   153: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   156: goto -> 11
    //   159: astore_0
    //   160: iconst_2
    //   161: anewarray java/io/Closeable
    //   164: dup
    //   165: iconst_0
    //   166: aload_2
    //   167: aastore
    //   168: dup
    //   169: iconst_1
    //   170: aload #8
    //   172: aastore
    //   173: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   176: aload_0
    //   177: athrow
    //   178: astore_0
    //   179: aload #9
    //   181: astore_2
    //   182: aload #6
    //   184: astore #8
    //   186: goto -> 160
    //   189: astore #7
    //   191: aload #9
    //   193: astore_2
    //   194: aload_0
    //   195: astore #8
    //   197: aload #7
    //   199: astore_0
    //   200: goto -> 160
    //   203: astore #9
    //   205: aload #4
    //   207: astore_0
    //   208: goto -> 103
    //   211: astore_2
    //   212: aload #9
    //   214: astore_0
    //   215: aload_2
    //   216: astore #9
    //   218: goto -> 103
    // Exception table:
    //   from	to	target	type
    //   33	38	203	java/io/IOException
    //   33	38	159	finally
    //   44	50	203	java/io/IOException
    //   44	50	159	finally
    //   50	58	211	java/io/IOException
    //   50	58	178	finally
    //   58	64	93	java/io/IOException
    //   58	64	189	finally
    //   64	76	93	java/io/IOException
    //   64	76	189	finally
    //   82	90	93	java/io/IOException
    //   82	90	189	finally
    //   109	114	159	finally
    //   135	140	93	java/io/IOException
    //   135	140	189	finally
  }
  
  public static byte[] readFile2BytesByStream(String paramString) {
    return readFile2BytesByStream(getFileByPath(paramString));
  }
  
  public static List<String> readFile2List(File paramFile) {
    return readFile2List(paramFile, 0, 2147483647, (String)null);
  }
  
  public static List<String> readFile2List(File paramFile, int paramInt1, int paramInt2) {
    return readFile2List(paramFile, paramInt1, paramInt2, (String)null);
  }
  
  public static List<String> readFile2List(File paramFile, int paramInt1, int paramInt2, String paramString) {
    if (!isFileExists(paramFile))
      return null; 
    if (paramInt1 > paramInt2)
      return null; 
    BufferedReader bufferedReader1 = null;
    BufferedReader bufferedReader2 = null;
    byte b = 1;
    BufferedReader bufferedReader3 = bufferedReader2;
    BufferedReader bufferedReader4 = bufferedReader1;
    try {
      BufferedReader bufferedReader5;
      BufferedReader bufferedReader6;
      ArrayList<String> arrayList = new ArrayList();
      bufferedReader3 = bufferedReader2;
      bufferedReader4 = bufferedReader1;
      this();
      bufferedReader3 = bufferedReader2;
      bufferedReader4 = bufferedReader1;
      if (isSpace(paramString)) {
        bufferedReader3 = bufferedReader2;
        bufferedReader4 = bufferedReader1;
        bufferedReader6 = new BufferedReader();
        bufferedReader3 = bufferedReader2;
        bufferedReader4 = bufferedReader1;
        InputStreamReader inputStreamReader = new InputStreamReader();
        bufferedReader3 = bufferedReader2;
        bufferedReader4 = bufferedReader1;
        FileInputStream fileInputStream = new FileInputStream();
        bufferedReader3 = bufferedReader2;
        bufferedReader4 = bufferedReader1;
        this(paramFile);
        bufferedReader3 = bufferedReader2;
        bufferedReader4 = bufferedReader1;
        this(fileInputStream);
        bufferedReader3 = bufferedReader2;
        bufferedReader4 = bufferedReader1;
        this(inputStreamReader);
        bufferedReader5 = bufferedReader6;
      } else {
        bufferedReader3 = bufferedReader2;
        bufferedReader4 = bufferedReader1;
        InputStreamReader inputStreamReader = new InputStreamReader();
        bufferedReader3 = bufferedReader2;
        bufferedReader4 = bufferedReader1;
        FileInputStream fileInputStream = new FileInputStream();
        bufferedReader3 = bufferedReader2;
        bufferedReader4 = bufferedReader1;
        this((File)bufferedReader5);
        bufferedReader3 = bufferedReader2;
        bufferedReader4 = bufferedReader1;
        this(fileInputStream, (String)bufferedReader6);
        bufferedReader3 = bufferedReader2;
        bufferedReader4 = bufferedReader1;
        bufferedReader5 = new BufferedReader(inputStreamReader);
      } 
      while (true) {
        bufferedReader3 = bufferedReader5;
        bufferedReader4 = bufferedReader5;
        String str = bufferedReader5.readLine();
        if (str == null || b > paramInt2)
          return arrayList; 
        if (paramInt1 <= b && b <= paramInt2) {
          bufferedReader3 = bufferedReader5;
          bufferedReader4 = bufferedReader5;
          arrayList.add(str);
        } 
        b++;
      } 
    } catch (IOException iOException) {
      bufferedReader4 = bufferedReader3;
      iOException.printStackTrace();
      CloseUtils.closeIO(new Closeable[] { bufferedReader3 });
    } finally {
      CloseUtils.closeIO(new Closeable[] { bufferedReader4 });
    } 
    return (List<String>)paramFile;
  }
  
  public static List<String> readFile2List(File paramFile, String paramString) {
    return readFile2List(paramFile, 0, 2147483647, paramString);
  }
  
  public static List<String> readFile2List(String paramString) {
    return readFile2List(getFileByPath(paramString), (String)null);
  }
  
  public static List<String> readFile2List(String paramString, int paramInt1, int paramInt2) {
    return readFile2List(getFileByPath(paramString), paramInt1, paramInt2, (String)null);
  }
  
  public static List<String> readFile2List(String paramString1, int paramInt1, int paramInt2, String paramString2) {
    return readFile2List(getFileByPath(paramString1), paramInt1, paramInt2, paramString2);
  }
  
  public static List<String> readFile2List(String paramString1, String paramString2) {
    return readFile2List(getFileByPath(paramString1), paramString2);
  }
  
  public static String readFile2String(File paramFile) {
    return readFile2String(paramFile, (String)null);
  }
  
  public static String readFile2String(File paramFile, String paramString) {
    String str = null;
    if (!isFileExists(paramFile))
      return str; 
    BufferedReader bufferedReader1 = null;
    BufferedReader bufferedReader2 = null;
    BufferedReader bufferedReader3 = bufferedReader2;
    BufferedReader bufferedReader4 = bufferedReader1;
    try {
      BufferedReader bufferedReader5;
      BufferedReader bufferedReader6;
      StringBuilder stringBuilder = new StringBuilder();
      bufferedReader3 = bufferedReader2;
      bufferedReader4 = bufferedReader1;
      this();
      bufferedReader3 = bufferedReader2;
      bufferedReader4 = bufferedReader1;
      if (isSpace(paramString)) {
        bufferedReader3 = bufferedReader2;
        bufferedReader4 = bufferedReader1;
        bufferedReader6 = new BufferedReader();
        bufferedReader3 = bufferedReader2;
        bufferedReader4 = bufferedReader1;
        InputStreamReader inputStreamReader = new InputStreamReader();
        bufferedReader3 = bufferedReader2;
        bufferedReader4 = bufferedReader1;
        FileInputStream fileInputStream = new FileInputStream();
        bufferedReader3 = bufferedReader2;
        bufferedReader4 = bufferedReader1;
        this(paramFile);
        bufferedReader3 = bufferedReader2;
        bufferedReader4 = bufferedReader1;
        this(fileInputStream);
        bufferedReader3 = bufferedReader2;
        bufferedReader4 = bufferedReader1;
        this(inputStreamReader);
        bufferedReader5 = bufferedReader6;
      } else {
        bufferedReader3 = bufferedReader2;
        bufferedReader4 = bufferedReader1;
        InputStreamReader inputStreamReader = new InputStreamReader();
        bufferedReader3 = bufferedReader2;
        bufferedReader4 = bufferedReader1;
        FileInputStream fileInputStream = new FileInputStream();
        bufferedReader3 = bufferedReader2;
        bufferedReader4 = bufferedReader1;
        this((File)bufferedReader5);
        bufferedReader3 = bufferedReader2;
        bufferedReader4 = bufferedReader1;
        this(fileInputStream, (String)bufferedReader6);
        bufferedReader3 = bufferedReader2;
        bufferedReader4 = bufferedReader1;
        bufferedReader5 = new BufferedReader(inputStreamReader);
      } 
      bufferedReader3 = bufferedReader5;
      bufferedReader4 = bufferedReader5;
      String str1 = bufferedReader5.readLine();
      if (str1 != null) {
        bufferedReader3 = bufferedReader5;
        bufferedReader4 = bufferedReader5;
        stringBuilder.append(str1);
        while (true) {
          bufferedReader3 = bufferedReader5;
          bufferedReader4 = bufferedReader5;
          str1 = bufferedReader5.readLine();
          if (str1 != null) {
            bufferedReader3 = bufferedReader5;
            bufferedReader4 = bufferedReader5;
            stringBuilder.append(LINE_SEP).append(str1);
            continue;
          } 
          break;
        } 
      } 
      bufferedReader3 = bufferedReader5;
      bufferedReader4 = bufferedReader5;
      str1 = stringBuilder.toString();
    } catch (IOException iOException) {
      bufferedReader4 = bufferedReader3;
      iOException.printStackTrace();
      CloseUtils.closeIO(new Closeable[] { bufferedReader3 });
    } finally {
      CloseUtils.closeIO(new Closeable[] { bufferedReader4 });
    } 
    return paramString;
  }
  
  public static String readFile2String(String paramString) {
    return readFile2String(getFileByPath(paramString), (String)null);
  }
  
  public static String readFile2String(String paramString1, String paramString2) {
    return readFile2String(getFileByPath(paramString1), paramString2);
  }
  
  public static void setBufferSize(int paramInt) {
    sBufferSize = paramInt;
  }
  
  public static boolean writeFileFromBytesByChannel(File paramFile, byte[] paramArrayOfbyte, boolean paramBoolean) {
    return writeFileFromBytesByChannel(paramFile, paramArrayOfbyte, false, paramBoolean);
  }
  
  public static boolean writeFileFromBytesByChannel(File paramFile, byte[] paramArrayOfbyte, boolean paramBoolean1, boolean paramBoolean2) {
    boolean bool = false;
    if (paramArrayOfbyte == null)
      return bool; 
    FileChannel fileChannel1 = null;
    FileChannel fileChannel2 = null;
    FileChannel fileChannel3 = fileChannel2;
    FileChannel fileChannel4 = fileChannel1;
    try {
      FileOutputStream fileOutputStream = new FileOutputStream();
      fileChannel3 = fileChannel2;
      fileChannel4 = fileChannel1;
      this(paramFile, paramBoolean1);
      fileChannel3 = fileChannel2;
      fileChannel4 = fileChannel1;
      FileChannel fileChannel = fileOutputStream.getChannel();
      fileChannel3 = fileChannel;
      fileChannel4 = fileChannel;
      fileChannel.position(fileChannel.size());
      fileChannel3 = fileChannel;
      fileChannel4 = fileChannel;
      fileChannel.write(ByteBuffer.wrap(paramArrayOfbyte));
      if (paramBoolean2) {
        fileChannel3 = fileChannel;
        fileChannel4 = fileChannel;
        fileChannel.force(true);
      } 
      CloseUtils.closeIO(new Closeable[] { fileChannel });
    } catch (IOException iOException) {
      fileChannel4 = fileChannel3;
      iOException.printStackTrace();
      CloseUtils.closeIO(new Closeable[] { fileChannel3 });
    } finally {
      CloseUtils.closeIO(new Closeable[] { fileChannel4 });
    } 
    return paramBoolean1;
  }
  
  public static boolean writeFileFromBytesByChannel(String paramString, byte[] paramArrayOfbyte, boolean paramBoolean) {
    return writeFileFromBytesByChannel(getFileByPath(paramString), paramArrayOfbyte, false, paramBoolean);
  }
  
  public static boolean writeFileFromBytesByChannel(String paramString, byte[] paramArrayOfbyte, boolean paramBoolean1, boolean paramBoolean2) {
    return writeFileFromBytesByChannel(getFileByPath(paramString), paramArrayOfbyte, paramBoolean1, paramBoolean2);
  }
  
  public static boolean writeFileFromBytesByMap(File paramFile, byte[] paramArrayOfbyte, boolean paramBoolean) {
    return writeFileFromBytesByMap(paramFile, paramArrayOfbyte, false, paramBoolean);
  }
  
  public static boolean writeFileFromBytesByMap(File paramFile, byte[] paramArrayOfbyte, boolean paramBoolean1, boolean paramBoolean2) {
    if (paramArrayOfbyte == null || !createOrExistsFile(paramFile))
      return false; 
    FileChannel fileChannel1 = null;
    FileChannel fileChannel2 = null;
    FileChannel fileChannel3 = fileChannel2;
    FileChannel fileChannel4 = fileChannel1;
    try {
      FileOutputStream fileOutputStream = new FileOutputStream();
      fileChannel3 = fileChannel2;
      fileChannel4 = fileChannel1;
      this(paramFile, paramBoolean1);
      fileChannel3 = fileChannel2;
      fileChannel4 = fileChannel1;
      FileChannel fileChannel = fileOutputStream.getChannel();
      fileChannel3 = fileChannel;
      fileChannel4 = fileChannel;
      MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, fileChannel.size(), paramArrayOfbyte.length);
      fileChannel3 = fileChannel;
      fileChannel4 = fileChannel;
      mappedByteBuffer.put(paramArrayOfbyte);
      if (paramBoolean2) {
        fileChannel3 = fileChannel;
        fileChannel4 = fileChannel;
        mappedByteBuffer.force();
      } 
      CloseUtils.closeIO(new Closeable[] { fileChannel });
    } catch (IOException iOException) {
      fileChannel4 = fileChannel3;
      iOException.printStackTrace();
      CloseUtils.closeIO(new Closeable[] { fileChannel3 });
    } finally {
      CloseUtils.closeIO(new Closeable[] { fileChannel4 });
    } 
    return paramBoolean1;
  }
  
  public static boolean writeFileFromBytesByMap(String paramString, byte[] paramArrayOfbyte, boolean paramBoolean) {
    return writeFileFromBytesByMap(paramString, paramArrayOfbyte, false, paramBoolean);
  }
  
  public static boolean writeFileFromBytesByMap(String paramString, byte[] paramArrayOfbyte, boolean paramBoolean1, boolean paramBoolean2) {
    return writeFileFromBytesByMap(getFileByPath(paramString), paramArrayOfbyte, paramBoolean1, paramBoolean2);
  }
  
  public static boolean writeFileFromBytesByStream(File paramFile, byte[] paramArrayOfbyte) {
    return writeFileFromBytesByStream(paramFile, paramArrayOfbyte, false);
  }
  
  public static boolean writeFileFromBytesByStream(File paramFile, byte[] paramArrayOfbyte, boolean paramBoolean) {
    File file2;
    boolean bool = true;
    if (paramArrayOfbyte == null || !createOrExistsFile(paramFile))
      return false; 
    BufferedOutputStream bufferedOutputStream1 = null;
    File file1 = null;
    BufferedOutputStream bufferedOutputStream2 = bufferedOutputStream1;
    try {
      BufferedOutputStream bufferedOutputStream = new BufferedOutputStream();
      bufferedOutputStream2 = bufferedOutputStream1;
      FileOutputStream fileOutputStream = new FileOutputStream();
      bufferedOutputStream2 = bufferedOutputStream1;
      this(paramFile, paramBoolean);
      bufferedOutputStream2 = bufferedOutputStream1;
      this(fileOutputStream);
      try {
        bufferedOutputStream.write(paramArrayOfbyte);
        CloseUtils.closeIO(new Closeable[] { bufferedOutputStream });
      } catch (IOException iOException) {
      
      } finally {
        paramFile = null;
      } 
    } catch (IOException iOException) {
      paramFile = file1;
      file2 = paramFile;
      iOException.printStackTrace();
      CloseUtils.closeIO(new Closeable[] { (Closeable)paramFile });
      return false;
    } finally {}
    CloseUtils.closeIO(new Closeable[] { (Closeable)file2 });
    throw paramFile;
  }
  
  public static boolean writeFileFromBytesByStream(String paramString, byte[] paramArrayOfbyte) {
    return writeFileFromBytesByStream(getFileByPath(paramString), paramArrayOfbyte, false);
  }
  
  public static boolean writeFileFromBytesByStream(String paramString, byte[] paramArrayOfbyte, boolean paramBoolean) {
    return writeFileFromBytesByStream(getFileByPath(paramString), paramArrayOfbyte, paramBoolean);
  }
  
  public static boolean writeFileFromIS(File paramFile, InputStream paramInputStream) {
    return writeFileFromIS(paramFile, paramInputStream, false);
  }
  
  public static boolean writeFileFromIS(File paramFile, InputStream paramInputStream, boolean paramBoolean) {
    boolean bool = true;
    if (!createOrExistsFile(paramFile) || paramInputStream == null)
      return false; 
    IOException iOException1 = null;
    File file = null;
    IOException iOException2 = iOException1;
    try {
      IOException iOException;
      BufferedOutputStream bufferedOutputStream = new BufferedOutputStream();
      iOException2 = iOException1;
      FileOutputStream fileOutputStream = new FileOutputStream();
      iOException2 = iOException1;
      this(paramFile, paramBoolean);
      iOException2 = iOException1;
      this(fileOutputStream);
    } catch (IOException iOException) {
    
    } finally {
      CloseUtils.closeIO(new Closeable[] { paramInputStream, (Closeable)iOException2 });
    } 
    return paramBoolean;
  }
  
  public static boolean writeFileFromIS(String paramString, InputStream paramInputStream) {
    return writeFileFromIS(getFileByPath(paramString), paramInputStream, false);
  }
  
  public static boolean writeFileFromIS(String paramString, InputStream paramInputStream, boolean paramBoolean) {
    return writeFileFromIS(getFileByPath(paramString), paramInputStream, paramBoolean);
  }
  
  public static boolean writeFileFromString(File paramFile, String paramString) {
    return writeFileFromString(paramFile, paramString, false);
  }
  
  public static boolean writeFileFromString(File paramFile, String paramString, boolean paramBoolean) {
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (paramFile != null) {
      if (paramString == null)
        return bool1; 
    } else {
      return bool2;
    } 
    bool2 = bool1;
    if (createOrExistsFile(paramFile)) {
      File file2;
      BufferedWriter bufferedWriter1 = null;
      File file1 = null;
      BufferedWriter bufferedWriter2 = bufferedWriter1;
      try {
        BufferedWriter bufferedWriter = new BufferedWriter();
        bufferedWriter2 = bufferedWriter1;
        FileWriter fileWriter = new FileWriter();
        bufferedWriter2 = bufferedWriter1;
        this(paramFile, paramBoolean);
        bufferedWriter2 = bufferedWriter1;
        this(fileWriter);
        try {
          bufferedWriter.write(paramString);
          CloseUtils.closeIO(new Closeable[] { bufferedWriter });
        } catch (IOException iOException) {
        
        } finally {
          paramFile = null;
        } 
      } catch (IOException iOException) {
        paramFile = file1;
        file2 = paramFile;
        iOException.printStackTrace();
        CloseUtils.closeIO(new Closeable[] { (Closeable)paramFile });
        return bool1;
      } finally {}
      CloseUtils.closeIO(new Closeable[] { (Closeable)file2 });
      throw paramFile;
    } 
    return bool2;
  }
  
  public static boolean writeFileFromString(String paramString1, String paramString2) {
    return writeFileFromString(getFileByPath(paramString1), paramString2, false);
  }
  
  public static boolean writeFileFromString(String paramString1, String paramString2, boolean paramBoolean) {
    return writeFileFromString(getFileByPath(paramString1), paramString2, paramBoolean);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/FileIOUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */