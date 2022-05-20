package com.blankj.utilcode.util;

import android.annotation.SuppressLint;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public final class FileUtils {
  private static final String LINE_SEP = System.getProperty("line.separator");
  
  private static final char[] hexDigits = new char[] { 
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
      'A', 'B', 'C', 'D', 'E', 'F' };
  
  private FileUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  @SuppressLint({"DefaultLocale"})
  private static String byte2FitMemorySize(long paramLong) {
    return (paramLong < 0L) ? "shouldn't be less than zero!" : ((paramLong < 1024L) ? String.format("%.3fB", new Object[] { Double.valueOf(paramLong) }) : ((paramLong < 1048576L) ? String.format("%.3fKB", new Object[] { Double.valueOf(paramLong / 1024.0D) }) : ((paramLong < 1073741824L) ? String.format("%.3fMB", new Object[] { Double.valueOf(paramLong / 1048576.0D) }) : String.format("%.3fGB", new Object[] { Double.valueOf(paramLong / 1.073741824E9D) }))));
  }
  
  private static String bytes2HexString(byte[] paramArrayOfbyte) {
    String str;
    char[] arrayOfChar = null;
    if (paramArrayOfbyte != null) {
      int i = paramArrayOfbyte.length;
      if (i > 0) {
        arrayOfChar = new char[i << 1];
        byte b = 0;
        int j = 0;
        while (b < i) {
          int k = j + 1;
          arrayOfChar[j] = (char)hexDigits[paramArrayOfbyte[b] >>> 4 & 0xF];
          j = k + 1;
          arrayOfChar[k] = (char)hexDigits[paramArrayOfbyte[b] & 0xF];
          b++;
        } 
        str = new String(arrayOfChar);
      } 
    } 
    return str;
  }
  
  public static boolean copyDir(File paramFile1, File paramFile2, OnReplaceListener paramOnReplaceListener) {
    return copyOrMoveDir(paramFile1, paramFile2, paramOnReplaceListener, false);
  }
  
  public static boolean copyDir(String paramString1, String paramString2, OnReplaceListener paramOnReplaceListener) {
    return copyDir(getFileByPath(paramString1), getFileByPath(paramString2), paramOnReplaceListener);
  }
  
  public static boolean copyFile(File paramFile1, File paramFile2, OnReplaceListener paramOnReplaceListener) {
    return copyOrMoveFile(paramFile1, paramFile2, paramOnReplaceListener, false);
  }
  
  public static boolean copyFile(String paramString1, String paramString2, OnReplaceListener paramOnReplaceListener) {
    return copyFile(getFileByPath(paramString1), getFileByPath(paramString2), paramOnReplaceListener);
  }
  
  private static boolean copyOrMoveDir(File paramFile1, File paramFile2, OnReplaceListener paramOnReplaceListener, boolean paramBoolean) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #4
    //   3: iload #4
    //   5: istore #5
    //   7: aload_0
    //   8: ifnull -> 19
    //   11: aload_1
    //   12: ifnonnull -> 22
    //   15: iload #4
    //   17: istore #5
    //   19: iload #5
    //   21: ireturn
    //   22: new java/lang/StringBuilder
    //   25: dup
    //   26: invokespecial <init> : ()V
    //   29: aload_0
    //   30: invokevirtual getPath : ()Ljava/lang/String;
    //   33: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   36: getstatic java/io/File.separator : Ljava/lang/String;
    //   39: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   42: invokevirtual toString : ()Ljava/lang/String;
    //   45: astore #6
    //   47: new java/lang/StringBuilder
    //   50: dup
    //   51: invokespecial <init> : ()V
    //   54: aload_1
    //   55: invokevirtual getPath : ()Ljava/lang/String;
    //   58: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   61: getstatic java/io/File.separator : Ljava/lang/String;
    //   64: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   67: invokevirtual toString : ()Ljava/lang/String;
    //   70: astore #7
    //   72: iload #4
    //   74: istore #5
    //   76: aload #7
    //   78: aload #6
    //   80: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   83: ifne -> 19
    //   86: iload #4
    //   88: istore #5
    //   90: aload_0
    //   91: invokevirtual exists : ()Z
    //   94: ifeq -> 19
    //   97: iload #4
    //   99: istore #5
    //   101: aload_0
    //   102: invokevirtual isDirectory : ()Z
    //   105: ifeq -> 19
    //   108: aload_1
    //   109: invokevirtual exists : ()Z
    //   112: ifeq -> 135
    //   115: aload_2
    //   116: invokeinterface onReplace : ()Z
    //   121: ifeq -> 234
    //   124: iload #4
    //   126: istore #5
    //   128: aload_1
    //   129: invokestatic deleteAllInDir : (Ljava/io/File;)Z
    //   132: ifeq -> 19
    //   135: iload #4
    //   137: istore #5
    //   139: aload_1
    //   140: invokestatic createOrExistsDir : (Ljava/io/File;)Z
    //   143: ifeq -> 19
    //   146: aload_0
    //   147: invokevirtual listFiles : ()[Ljava/io/File;
    //   150: astore #8
    //   152: aload #8
    //   154: arraylength
    //   155: istore #9
    //   157: iconst_0
    //   158: istore #10
    //   160: iload #10
    //   162: iload #9
    //   164: if_icmpge -> 266
    //   167: aload #8
    //   169: iload #10
    //   171: aaload
    //   172: astore #6
    //   174: new java/io/File
    //   177: dup
    //   178: new java/lang/StringBuilder
    //   181: dup
    //   182: invokespecial <init> : ()V
    //   185: aload #7
    //   187: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   190: aload #6
    //   192: invokevirtual getName : ()Ljava/lang/String;
    //   195: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   198: invokevirtual toString : ()Ljava/lang/String;
    //   201: invokespecial <init> : (Ljava/lang/String;)V
    //   204: astore_1
    //   205: aload #6
    //   207: invokevirtual isFile : ()Z
    //   210: ifeq -> 240
    //   213: iload #4
    //   215: istore #5
    //   217: aload #6
    //   219: aload_1
    //   220: aload_2
    //   221: iload_3
    //   222: invokestatic copyOrMoveFile : (Ljava/io/File;Ljava/io/File;Lcom/blankj/utilcode/util/FileUtils$OnReplaceListener;Z)Z
    //   225: ifeq -> 19
    //   228: iinc #10, 1
    //   231: goto -> 160
    //   234: iconst_1
    //   235: istore #5
    //   237: goto -> 19
    //   240: aload #6
    //   242: invokevirtual isDirectory : ()Z
    //   245: ifeq -> 228
    //   248: aload #6
    //   250: aload_1
    //   251: aload_2
    //   252: iload_3
    //   253: invokestatic copyOrMoveDir : (Ljava/io/File;Ljava/io/File;Lcom/blankj/utilcode/util/FileUtils$OnReplaceListener;Z)Z
    //   256: ifne -> 228
    //   259: iload #4
    //   261: istore #5
    //   263: goto -> 19
    //   266: iload_3
    //   267: ifeq -> 281
    //   270: iload #4
    //   272: istore #5
    //   274: aload_0
    //   275: invokestatic deleteDir : (Ljava/io/File;)Z
    //   278: ifeq -> 19
    //   281: iconst_1
    //   282: istore #5
    //   284: goto -> 19
  }
  
  private static boolean copyOrMoveDir(String paramString1, String paramString2, OnReplaceListener paramOnReplaceListener, boolean paramBoolean) {
    return copyOrMoveDir(getFileByPath(paramString1), getFileByPath(paramString2), paramOnReplaceListener, paramBoolean);
  }
  
  private static boolean copyOrMoveFile(File paramFile1, File paramFile2, OnReplaceListener paramOnReplaceListener, boolean paramBoolean) {
    // Byte code:
    //   0: iconst_1
    //   1: istore #4
    //   3: iconst_0
    //   4: istore #5
    //   6: iload #5
    //   8: istore #6
    //   10: aload_0
    //   11: ifnull -> 22
    //   14: aload_1
    //   15: ifnonnull -> 25
    //   18: iload #5
    //   20: istore #6
    //   22: iload #6
    //   24: ireturn
    //   25: iload #5
    //   27: istore #6
    //   29: aload_0
    //   30: aload_1
    //   31: invokevirtual equals : (Ljava/lang/Object;)Z
    //   34: ifne -> 22
    //   37: iload #5
    //   39: istore #6
    //   41: aload_0
    //   42: invokevirtual exists : ()Z
    //   45: ifeq -> 22
    //   48: iload #5
    //   50: istore #6
    //   52: aload_0
    //   53: invokevirtual isFile : ()Z
    //   56: ifeq -> 22
    //   59: aload_1
    //   60: invokevirtual exists : ()Z
    //   63: ifeq -> 86
    //   66: aload_2
    //   67: invokeinterface onReplace : ()Z
    //   72: ifeq -> 142
    //   75: iload #5
    //   77: istore #6
    //   79: aload_1
    //   80: invokevirtual delete : ()Z
    //   83: ifeq -> 22
    //   86: iload #5
    //   88: istore #6
    //   90: aload_1
    //   91: invokevirtual getParentFile : ()Ljava/io/File;
    //   94: invokestatic createOrExistsDir : (Ljava/io/File;)Z
    //   97: ifeq -> 22
    //   100: new java/io/FileInputStream
    //   103: astore_2
    //   104: aload_2
    //   105: aload_0
    //   106: invokespecial <init> : (Ljava/io/File;)V
    //   109: aload_1
    //   110: aload_2
    //   111: iconst_0
    //   112: invokestatic writeFileFromIS : (Ljava/io/File;Ljava/io/InputStream;Z)Z
    //   115: ifeq -> 148
    //   118: iload #4
    //   120: istore #6
    //   122: iload_3
    //   123: ifeq -> 139
    //   126: aload_0
    //   127: invokestatic deleteFile : (Ljava/io/File;)Z
    //   130: istore_3
    //   131: iload_3
    //   132: ifeq -> 148
    //   135: iload #4
    //   137: istore #6
    //   139: goto -> 22
    //   142: iconst_1
    //   143: istore #6
    //   145: goto -> 22
    //   148: iconst_0
    //   149: istore #6
    //   151: goto -> 139
    //   154: astore_0
    //   155: aload_0
    //   156: invokevirtual printStackTrace : ()V
    //   159: iload #5
    //   161: istore #6
    //   163: goto -> 22
    // Exception table:
    //   from	to	target	type
    //   100	118	154	java/io/FileNotFoundException
    //   126	131	154	java/io/FileNotFoundException
  }
  
  private static boolean copyOrMoveFile(String paramString1, String paramString2, OnReplaceListener paramOnReplaceListener, boolean paramBoolean) {
    return copyOrMoveFile(getFileByPath(paramString1), getFileByPath(paramString2), paramOnReplaceListener, paramBoolean);
  }
  
  public static boolean createFileByDeleteOldFile(File paramFile) {
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
    //   14: ifeq -> 26
    //   17: iload_1
    //   18: istore_2
    //   19: aload_0
    //   20: invokevirtual delete : ()Z
    //   23: ifeq -> 8
    //   26: iload_1
    //   27: istore_2
    //   28: aload_0
    //   29: invokevirtual getParentFile : ()Ljava/io/File;
    //   32: invokestatic createOrExistsDir : (Ljava/io/File;)Z
    //   35: ifeq -> 8
    //   38: aload_0
    //   39: invokevirtual createNewFile : ()Z
    //   42: istore_2
    //   43: goto -> 8
    //   46: astore_0
    //   47: aload_0
    //   48: invokevirtual printStackTrace : ()V
    //   51: iload_1
    //   52: istore_2
    //   53: goto -> 8
    // Exception table:
    //   from	to	target	type
    //   38	43	46	java/io/IOException
  }
  
  public static boolean createFileByDeleteOldFile(String paramString) {
    return createFileByDeleteOldFile(getFileByPath(paramString));
  }
  
  public static boolean createOrExistsDir(File paramFile) {
    return (paramFile != null && (paramFile.exists() ? paramFile.isDirectory() : paramFile.mkdirs()));
  }
  
  public static boolean createOrExistsDir(String paramString) {
    return createOrExistsDir(getFileByPath(paramString));
  }
  
  public static boolean createOrExistsFile(File paramFile) {
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
  
  public static boolean createOrExistsFile(String paramString) {
    return createOrExistsFile(getFileByPath(paramString));
  }
  
  public static boolean deleteAllInDir(File paramFile) {
    return deleteFilesInDirWithFilter(paramFile, new FileFilter() {
          public boolean accept(File param1File) {
            return true;
          }
        });
  }
  
  public static boolean deleteAllInDir(String paramString) {
    return deleteAllInDir(getFileByPath(paramString));
  }
  
  public static boolean deleteDir(File paramFile) {
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
  
  public static boolean deleteDir(String paramString) {
    return deleteDir(getFileByPath(paramString));
  }
  
  public static boolean deleteFile(File paramFile) {
    return (paramFile != null && (!paramFile.exists() || (paramFile.isFile() && paramFile.delete())));
  }
  
  public static boolean deleteFile(String paramString) {
    return deleteFile(getFileByPath(paramString));
  }
  
  public static boolean deleteFilesInDir(File paramFile) {
    return deleteFilesInDirWithFilter(paramFile, new FileFilter() {
          public boolean accept(File param1File) {
            return param1File.isFile();
          }
        });
  }
  
  public static boolean deleteFilesInDir(String paramString) {
    return deleteFilesInDir(getFileByPath(paramString));
  }
  
  public static boolean deleteFilesInDirWithFilter(File paramFile, FileFilter paramFileFilter) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: aload_0
    //   3: ifnonnull -> 10
    //   6: iload_2
    //   7: istore_3
    //   8: iload_3
    //   9: ireturn
    //   10: aload_0
    //   11: invokevirtual exists : ()Z
    //   14: ifne -> 22
    //   17: iconst_1
    //   18: istore_3
    //   19: goto -> 8
    //   22: iload_2
    //   23: istore_3
    //   24: aload_0
    //   25: invokevirtual isDirectory : ()Z
    //   28: ifeq -> 8
    //   31: aload_0
    //   32: invokevirtual listFiles : ()[Ljava/io/File;
    //   35: astore_0
    //   36: aload_0
    //   37: ifnull -> 121
    //   40: aload_0
    //   41: arraylength
    //   42: ifeq -> 121
    //   45: aload_0
    //   46: arraylength
    //   47: istore #4
    //   49: iconst_0
    //   50: istore #5
    //   52: iload #5
    //   54: iload #4
    //   56: if_icmpge -> 121
    //   59: aload_0
    //   60: iload #5
    //   62: aaload
    //   63: astore #6
    //   65: aload_1
    //   66: aload #6
    //   68: invokeinterface accept : (Ljava/io/File;)Z
    //   73: ifeq -> 94
    //   76: aload #6
    //   78: invokevirtual isFile : ()Z
    //   81: ifeq -> 100
    //   84: iload_2
    //   85: istore_3
    //   86: aload #6
    //   88: invokevirtual delete : ()Z
    //   91: ifeq -> 8
    //   94: iinc #5, 1
    //   97: goto -> 52
    //   100: aload #6
    //   102: invokevirtual isDirectory : ()Z
    //   105: ifeq -> 94
    //   108: aload #6
    //   110: invokestatic deleteDir : (Ljava/io/File;)Z
    //   113: ifne -> 94
    //   116: iload_2
    //   117: istore_3
    //   118: goto -> 8
    //   121: iconst_1
    //   122: istore_3
    //   123: goto -> 8
  }
  
  public static boolean deleteFilesInDirWithFilter(String paramString, FileFilter paramFileFilter) {
    return deleteFilesInDirWithFilter(getFileByPath(paramString), paramFileFilter);
  }
  
  public static long getDirLength(File paramFile) {
    if (!isDir(paramFile))
      return -1L; 
    long l2 = 0L;
    File[] arrayOfFile = paramFile.listFiles();
    long l1 = l2;
    if (arrayOfFile != null) {
      l1 = l2;
      if (arrayOfFile.length != 0) {
        int i = arrayOfFile.length;
        byte b = 0;
        while (true) {
          l1 = l2;
          if (b < i) {
            paramFile = arrayOfFile[b];
            if (paramFile.isDirectory()) {
              l2 += getDirLength(paramFile);
            } else {
              l2 += paramFile.length();
            } 
            b++;
            continue;
          } 
          return l1;
        } 
      } 
    } 
    return l1;
  }
  
  public static long getDirLength(String paramString) {
    return getDirLength(getFileByPath(paramString));
  }
  
  public static String getDirName(File paramFile) {
    return (paramFile == null) ? null : getDirName(paramFile.getPath());
  }
  
  public static String getDirName(String paramString) {
    if (!isSpace(paramString)) {
      int i = paramString.lastIndexOf(File.separator);
      if (i == -1)
        return ""; 
      paramString = paramString.substring(0, i + 1);
    } 
    return paramString;
  }
  
  public static String getDirSize(File paramFile) {
    long l = getDirLength(paramFile);
    return (l == -1L) ? "" : byte2FitMemorySize(l);
  }
  
  public static String getDirSize(String paramString) {
    return getDirSize(getFileByPath(paramString));
  }
  
  public static File getFileByPath(String paramString) {
    return isSpace(paramString) ? null : new File(paramString);
  }
  
  public static String getFileCharsetSimple(File paramFile) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aconst_null
    //   3: astore_2
    //   4: aconst_null
    //   5: astore_3
    //   6: aload_2
    //   7: astore #4
    //   9: new java/io/BufferedInputStream
    //   12: astore #5
    //   14: aload_2
    //   15: astore #4
    //   17: new java/io/FileInputStream
    //   20: astore #6
    //   22: aload_2
    //   23: astore #4
    //   25: aload #6
    //   27: aload_0
    //   28: invokespecial <init> : (Ljava/io/File;)V
    //   31: aload_2
    //   32: astore #4
    //   34: aload #5
    //   36: aload #6
    //   38: invokespecial <init> : (Ljava/io/InputStream;)V
    //   41: aload #5
    //   43: invokevirtual read : ()I
    //   46: istore #7
    //   48: aload #5
    //   50: invokevirtual read : ()I
    //   53: istore #8
    //   55: iload #7
    //   57: bipush #8
    //   59: ishl
    //   60: iload #8
    //   62: iadd
    //   63: istore_1
    //   64: iconst_1
    //   65: anewarray java/io/Closeable
    //   68: dup
    //   69: iconst_0
    //   70: aload #5
    //   72: aastore
    //   73: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   76: iload_1
    //   77: lookupswitch default -> 112, 61371 -> 159, 65279 -> 173, 65534 -> 166
    //   112: ldc_w 'GBK'
    //   115: astore_0
    //   116: aload_0
    //   117: areturn
    //   118: astore #5
    //   120: aload_3
    //   121: astore_0
    //   122: aload_0
    //   123: astore #4
    //   125: aload #5
    //   127: invokevirtual printStackTrace : ()V
    //   130: iconst_1
    //   131: anewarray java/io/Closeable
    //   134: dup
    //   135: iconst_0
    //   136: aload_0
    //   137: aastore
    //   138: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   141: goto -> 76
    //   144: astore_0
    //   145: iconst_1
    //   146: anewarray java/io/Closeable
    //   149: dup
    //   150: iconst_0
    //   151: aload #4
    //   153: aastore
    //   154: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   157: aload_0
    //   158: athrow
    //   159: ldc_w 'UTF-8'
    //   162: astore_0
    //   163: goto -> 116
    //   166: ldc_w 'Unicode'
    //   169: astore_0
    //   170: goto -> 116
    //   173: ldc_w 'UTF-16BE'
    //   176: astore_0
    //   177: goto -> 116
    //   180: astore_0
    //   181: aload #5
    //   183: astore #4
    //   185: goto -> 145
    //   188: astore_0
    //   189: aload #5
    //   191: astore #4
    //   193: aload_0
    //   194: astore #5
    //   196: aload #4
    //   198: astore_0
    //   199: goto -> 122
    // Exception table:
    //   from	to	target	type
    //   9	14	118	java/io/IOException
    //   9	14	144	finally
    //   17	22	118	java/io/IOException
    //   17	22	144	finally
    //   25	31	118	java/io/IOException
    //   25	31	144	finally
    //   34	41	118	java/io/IOException
    //   34	41	144	finally
    //   41	55	188	java/io/IOException
    //   41	55	180	finally
    //   125	130	144	finally
  }
  
  public static String getFileCharsetSimple(String paramString) {
    return getFileCharsetSimple(getFileByPath(paramString));
  }
  
  public static String getFileExtension(File paramFile) {
    return (paramFile == null) ? null : getFileExtension(paramFile.getPath());
  }
  
  public static String getFileExtension(String paramString) {
    if (!isSpace(paramString)) {
      int i = paramString.lastIndexOf('.');
      int j = paramString.lastIndexOf(File.separator);
      if (i == -1 || j >= i)
        return ""; 
      paramString = paramString.substring(i + 1);
    } 
    return paramString;
  }
  
  public static long getFileLastModified(File paramFile) {
    return (paramFile == null) ? -1L : paramFile.lastModified();
  }
  
  public static long getFileLastModified(String paramString) {
    return getFileLastModified(getFileByPath(paramString));
  }
  
  public static long getFileLength(File paramFile) {
    return !isFile(paramFile) ? -1L : paramFile.length();
  }
  
  public static long getFileLength(String paramString) {
    return getFileLength(getFileByPath(paramString));
  }
  
  public static int getFileLines(File paramFile) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_1
    //   2: iconst_1
    //   3: istore_2
    //   4: iconst_1
    //   5: istore_3
    //   6: iconst_1
    //   7: istore #4
    //   9: aconst_null
    //   10: astore #5
    //   12: aconst_null
    //   13: astore #6
    //   15: aload #5
    //   17: astore #7
    //   19: new java/io/BufferedInputStream
    //   22: astore #8
    //   24: aload #5
    //   26: astore #7
    //   28: new java/io/FileInputStream
    //   31: astore #9
    //   33: aload #5
    //   35: astore #7
    //   37: aload #9
    //   39: aload_0
    //   40: invokespecial <init> : (Ljava/io/File;)V
    //   43: aload #5
    //   45: astore #7
    //   47: aload #8
    //   49: aload #9
    //   51: invokespecial <init> : (Ljava/io/InputStream;)V
    //   54: iload_3
    //   55: istore_2
    //   56: sipush #1024
    //   59: newarray byte
    //   61: astore_0
    //   62: iload_3
    //   63: istore_2
    //   64: getstatic com/blankj/utilcode/util/FileUtils.LINE_SEP : Ljava/lang/String;
    //   67: ldc_w '\\n'
    //   70: invokevirtual endsWith : (Ljava/lang/String;)Z
    //   73: ifeq -> 137
    //   76: iload #4
    //   78: istore_1
    //   79: iload_1
    //   80: istore_2
    //   81: aload #8
    //   83: aload_0
    //   84: iconst_0
    //   85: sipush #1024
    //   88: invokevirtual read : ([BII)I
    //   91: istore_3
    //   92: iload_1
    //   93: istore_2
    //   94: iload_3
    //   95: iconst_m1
    //   96: if_icmpeq -> 197
    //   99: iconst_0
    //   100: istore_2
    //   101: iload_1
    //   102: istore #4
    //   104: iload #4
    //   106: istore_1
    //   107: iload_2
    //   108: iload_3
    //   109: if_icmpge -> 79
    //   112: iload #4
    //   114: istore_1
    //   115: aload_0
    //   116: iload_2
    //   117: baload
    //   118: bipush #10
    //   120: if_icmpne -> 128
    //   123: iload #4
    //   125: iconst_1
    //   126: iadd
    //   127: istore_1
    //   128: iinc #2, 1
    //   131: iload_1
    //   132: istore #4
    //   134: goto -> 104
    //   137: iload_1
    //   138: istore_2
    //   139: aload #8
    //   141: aload_0
    //   142: iconst_0
    //   143: sipush #1024
    //   146: invokevirtual read : ([BII)I
    //   149: istore_3
    //   150: iload_1
    //   151: istore_2
    //   152: iload_3
    //   153: iconst_m1
    //   154: if_icmpeq -> 197
    //   157: iconst_0
    //   158: istore #4
    //   160: iload_1
    //   161: istore_2
    //   162: iload_2
    //   163: istore_1
    //   164: iload #4
    //   166: iload_3
    //   167: if_icmpge -> 137
    //   170: aload_0
    //   171: iload #4
    //   173: baload
    //   174: istore #10
    //   176: iload_2
    //   177: istore_1
    //   178: iload #10
    //   180: bipush #13
    //   182: if_icmpne -> 189
    //   185: iload_2
    //   186: iconst_1
    //   187: iadd
    //   188: istore_1
    //   189: iinc #4, 1
    //   192: iload_1
    //   193: istore_2
    //   194: goto -> 162
    //   197: iconst_1
    //   198: anewarray java/io/Closeable
    //   201: dup
    //   202: iconst_0
    //   203: aload #8
    //   205: aastore
    //   206: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   209: iload_2
    //   210: ireturn
    //   211: astore #8
    //   213: aload #6
    //   215: astore_0
    //   216: aload_0
    //   217: astore #7
    //   219: aload #8
    //   221: invokevirtual printStackTrace : ()V
    //   224: iconst_1
    //   225: anewarray java/io/Closeable
    //   228: dup
    //   229: iconst_0
    //   230: aload_0
    //   231: aastore
    //   232: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   235: goto -> 209
    //   238: astore_0
    //   239: iconst_1
    //   240: anewarray java/io/Closeable
    //   243: dup
    //   244: iconst_0
    //   245: aload #7
    //   247: aastore
    //   248: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   251: aload_0
    //   252: athrow
    //   253: astore_0
    //   254: aload #8
    //   256: astore #7
    //   258: goto -> 239
    //   261: astore #7
    //   263: aload #8
    //   265: astore_0
    //   266: aload #7
    //   268: astore #8
    //   270: goto -> 216
    // Exception table:
    //   from	to	target	type
    //   19	24	211	java/io/IOException
    //   19	24	238	finally
    //   28	33	211	java/io/IOException
    //   28	33	238	finally
    //   37	43	211	java/io/IOException
    //   37	43	238	finally
    //   47	54	211	java/io/IOException
    //   47	54	238	finally
    //   56	62	261	java/io/IOException
    //   56	62	253	finally
    //   64	76	261	java/io/IOException
    //   64	76	253	finally
    //   81	92	261	java/io/IOException
    //   81	92	253	finally
    //   139	150	261	java/io/IOException
    //   139	150	253	finally
    //   219	224	238	finally
  }
  
  public static int getFileLines(String paramString) {
    return getFileLines(getFileByPath(paramString));
  }
  
  public static byte[] getFileMD5(File paramFile) {
    DigestInputStream digestInputStream;
    IOException iOException2;
    byte[] arrayOfByte1 = null;
    if (paramFile == null)
      return arrayOfByte1; 
    byte[] arrayOfByte4 = null;
    File file2 = null;
    NoSuchAlgorithmException noSuchAlgorithmException = null;
    byte[] arrayOfByte3 = arrayOfByte4;
    try {
      byte[] arrayOfByte;
      FileInputStream fileInputStream = new FileInputStream();
      arrayOfByte3 = arrayOfByte4;
      this(paramFile);
      arrayOfByte3 = arrayOfByte4;
      MessageDigest messageDigest = MessageDigest.getInstance("MD5");
      arrayOfByte3 = arrayOfByte4;
      DigestInputStream digestInputStream1 = new DigestInputStream();
      arrayOfByte3 = arrayOfByte4;
      this(fileInputStream, messageDigest);
      try {
        arrayOfByte3 = new byte[262144];
        do {
        
        } while (digestInputStream1.read(arrayOfByte3) > 0);
      } catch (NoSuchAlgorithmException noSuchAlgorithmException1) {
      
      } catch (IOException null) {
      
      } finally {
        arrayOfByte3 = null;
        digestInputStream = digestInputStream1;
      } 
      CloseUtils.closeIO(new Closeable[] { digestInputStream });
      throw arrayOfByte;
    } catch (NoSuchAlgorithmException noSuchAlgorithmException1) {
    
    } catch (IOException iOException1) {
    
    } finally {
      iOException2 = iOException1;
      CloseUtils.closeIO(new Closeable[] { (Closeable)iOException2 });
    } 
    File file1 = paramFile;
    iOException2.printStackTrace();
    CloseUtils.closeIO(new Closeable[] { (Closeable)paramFile });
    byte[] arrayOfByte2 = arrayOfByte1;
  }
  
  public static byte[] getFileMD5(String paramString) {
    return getFileMD5(getFileByPath(paramString));
  }
  
  public static String getFileMD5ToString(File paramFile) {
    return bytes2HexString(getFileMD5(paramFile));
  }
  
  public static String getFileMD5ToString(String paramString) {
    if (isSpace(paramString)) {
      paramString = null;
      return getFileMD5ToString((File)paramString);
    } 
    File file = new File(paramString);
    return getFileMD5ToString(file);
  }
  
  public static String getFileName(File paramFile) {
    return (paramFile == null) ? null : getFileName(paramFile.getPath());
  }
  
  public static String getFileName(String paramString) {
    if (isSpace(paramString))
      return paramString; 
    int i = paramString.lastIndexOf(File.separator);
    String str = paramString;
    if (i != -1)
      str = paramString.substring(i + 1); 
    return str;
  }
  
  public static String getFileNameNoExtension(File paramFile) {
    return (paramFile == null) ? null : getFileNameNoExtension(paramFile.getPath());
  }
  
  public static String getFileNameNoExtension(String paramString) {
    if (isSpace(paramString))
      return paramString; 
    int i = paramString.lastIndexOf('.');
    int j = paramString.lastIndexOf(File.separator);
    if (j == -1) {
      String str = paramString;
      if (i != -1)
        str = paramString.substring(0, i); 
      return str;
    } 
    return (i == -1 || j > i) ? paramString.substring(j + 1) : paramString.substring(j + 1, i);
  }
  
  public static String getFileSize(File paramFile) {
    long l = getFileLength(paramFile);
    return (l == -1L) ? "" : byte2FitMemorySize(l);
  }
  
  public static String getFileSize(String paramString) {
    return getFileSize(getFileByPath(paramString));
  }
  
  public static boolean isDir(File paramFile) {
    return (paramFile != null && paramFile.exists() && paramFile.isDirectory());
  }
  
  public static boolean isDir(String paramString) {
    return isDir(getFileByPath(paramString));
  }
  
  public static boolean isFile(File paramFile) {
    return (paramFile != null && paramFile.exists() && paramFile.isFile());
  }
  
  public static boolean isFile(String paramString) {
    return isFile(getFileByPath(paramString));
  }
  
  public static boolean isFileExists(File paramFile) {
    return (paramFile != null && paramFile.exists());
  }
  
  public static boolean isFileExists(String paramString) {
    return isFileExists(getFileByPath(paramString));
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
  
  public static List<File> listFilesInDir(File paramFile) {
    return listFilesInDir(paramFile, false);
  }
  
  public static List<File> listFilesInDir(File paramFile, boolean paramBoolean) {
    return listFilesInDirWithFilter(paramFile, new FileFilter() {
          public boolean accept(File param1File) {
            return true;
          }
        },  paramBoolean);
  }
  
  public static List<File> listFilesInDir(String paramString) {
    return listFilesInDir(paramString, false);
  }
  
  public static List<File> listFilesInDir(String paramString, boolean paramBoolean) {
    return listFilesInDir(getFileByPath(paramString), paramBoolean);
  }
  
  public static List<File> listFilesInDirWithFilter(File paramFile, FileFilter paramFileFilter) {
    return listFilesInDirWithFilter(paramFile, paramFileFilter, false);
  }
  
  public static List<File> listFilesInDirWithFilter(File paramFile, FileFilter paramFileFilter, boolean paramBoolean) {
    File file;
    if (!isDir(paramFile))
      return null; 
    ArrayList<File> arrayList2 = new ArrayList();
    File[] arrayOfFile = paramFile.listFiles();
    ArrayList<File> arrayList1 = arrayList2;
    if (arrayOfFile != null) {
      arrayList1 = arrayList2;
      if (arrayOfFile.length != 0) {
        int i = arrayOfFile.length;
        byte b = 0;
        while (true) {
          arrayList1 = arrayList2;
          if (b < i) {
            file = arrayOfFile[b];
            if (paramFileFilter.accept(file))
              arrayList2.add(file); 
            if (paramBoolean && file.isDirectory())
              arrayList2.addAll(listFilesInDirWithFilter(file, paramFileFilter, true)); 
            b++;
            continue;
          } 
          return (List<File>)file;
        } 
      } 
    } 
    return (List<File>)file;
  }
  
  public static List<File> listFilesInDirWithFilter(String paramString, FileFilter paramFileFilter) {
    return listFilesInDirWithFilter(getFileByPath(paramString), paramFileFilter, false);
  }
  
  public static List<File> listFilesInDirWithFilter(String paramString, FileFilter paramFileFilter, boolean paramBoolean) {
    return listFilesInDirWithFilter(getFileByPath(paramString), paramFileFilter, paramBoolean);
  }
  
  public static boolean moveDir(File paramFile1, File paramFile2, OnReplaceListener paramOnReplaceListener) {
    return copyOrMoveDir(paramFile1, paramFile2, paramOnReplaceListener, true);
  }
  
  public static boolean moveDir(String paramString1, String paramString2, OnReplaceListener paramOnReplaceListener) {
    return moveDir(getFileByPath(paramString1), getFileByPath(paramString2), paramOnReplaceListener);
  }
  
  public static boolean moveFile(File paramFile1, File paramFile2, OnReplaceListener paramOnReplaceListener) {
    return copyOrMoveFile(paramFile1, paramFile2, paramOnReplaceListener, true);
  }
  
  public static boolean moveFile(String paramString1, String paramString2, OnReplaceListener paramOnReplaceListener) {
    return moveFile(getFileByPath(paramString1), getFileByPath(paramString2), paramOnReplaceListener);
  }
  
  public static boolean rename(File paramFile, String paramString) {
    boolean bool1 = true;
    boolean bool2 = false;
    if (paramFile == null)
      return bool2; 
    boolean bool3 = bool2;
    if (paramFile.exists()) {
      bool3 = bool2;
      if (!isSpace(paramString)) {
        if (paramString.equals(paramFile.getName()))
          return true; 
        File file = new File(paramFile.getParent() + File.separator + paramString);
        if (!file.exists() && paramFile.renameTo(file))
          return bool1; 
        bool3 = false;
      } 
    } 
    return bool3;
  }
  
  public static boolean rename(String paramString1, String paramString2) {
    return rename(getFileByPath(paramString1), paramString2);
  }
  
  public static interface OnReplaceListener {
    boolean onReplace();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/FileUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */