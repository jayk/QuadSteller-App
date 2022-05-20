package org.xutils.common.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD5 {
  private static final char[] hexDigits = new char[] { 
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
      'a', 'b', 'c', 'd', 'e', 'f' };
  
  public static String md5(File paramFile) throws IOException {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aconst_null
    //   3: astore_2
    //   4: aconst_null
    //   5: astore_3
    //   6: aconst_null
    //   7: astore #4
    //   9: aconst_null
    //   10: astore #5
    //   12: aconst_null
    //   13: astore #6
    //   15: aload_3
    //   16: astore #7
    //   18: aload_1
    //   19: astore #8
    //   21: ldc 'MD5'
    //   23: invokestatic getInstance : (Ljava/lang/String;)Ljava/security/MessageDigest;
    //   26: astore #9
    //   28: aload_3
    //   29: astore #7
    //   31: aload_1
    //   32: astore #8
    //   34: new java/io/FileInputStream
    //   37: astore #10
    //   39: aload_3
    //   40: astore #7
    //   42: aload_1
    //   43: astore #8
    //   45: aload #10
    //   47: aload_0
    //   48: invokespecial <init> : (Ljava/io/File;)V
    //   51: aload #4
    //   53: astore #7
    //   55: aload #5
    //   57: astore #8
    //   59: aload #10
    //   61: invokevirtual getChannel : ()Ljava/nio/channels/FileChannel;
    //   64: astore #5
    //   66: aload #5
    //   68: astore #7
    //   70: aload #5
    //   72: astore #8
    //   74: aload #9
    //   76: aload #5
    //   78: getstatic java/nio/channels/FileChannel$MapMode.READ_ONLY : Ljava/nio/channels/FileChannel$MapMode;
    //   81: lconst_0
    //   82: aload_0
    //   83: invokevirtual length : ()J
    //   86: invokevirtual map : (Ljava/nio/channels/FileChannel$MapMode;JJ)Ljava/nio/MappedByteBuffer;
    //   89: invokevirtual update : (Ljava/nio/ByteBuffer;)V
    //   92: aload #5
    //   94: astore #7
    //   96: aload #5
    //   98: astore #8
    //   100: aload #9
    //   102: invokevirtual digest : ()[B
    //   105: astore_0
    //   106: aload #10
    //   108: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   111: aload #5
    //   113: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   116: aload_0
    //   117: invokestatic toHexString : ([B)Ljava/lang/String;
    //   120: areturn
    //   121: astore #5
    //   123: aload_2
    //   124: astore #10
    //   126: aload #6
    //   128: astore_0
    //   129: aload_0
    //   130: astore #7
    //   132: aload #10
    //   134: astore #8
    //   136: new java/lang/RuntimeException
    //   139: astore #6
    //   141: aload_0
    //   142: astore #7
    //   144: aload #10
    //   146: astore #8
    //   148: aload #6
    //   150: aload #5
    //   152: invokespecial <init> : (Ljava/lang/Throwable;)V
    //   155: aload_0
    //   156: astore #7
    //   158: aload #10
    //   160: astore #8
    //   162: aload #6
    //   164: athrow
    //   165: astore_0
    //   166: aload #8
    //   168: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   171: aload #7
    //   173: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   176: aload_0
    //   177: athrow
    //   178: astore_0
    //   179: aload #10
    //   181: astore #8
    //   183: goto -> 166
    //   186: astore #5
    //   188: aload #8
    //   190: astore_0
    //   191: goto -> 129
    // Exception table:
    //   from	to	target	type
    //   21	28	121	java/security/NoSuchAlgorithmException
    //   21	28	165	finally
    //   34	39	121	java/security/NoSuchAlgorithmException
    //   34	39	165	finally
    //   45	51	121	java/security/NoSuchAlgorithmException
    //   45	51	165	finally
    //   59	66	186	java/security/NoSuchAlgorithmException
    //   59	66	178	finally
    //   74	92	186	java/security/NoSuchAlgorithmException
    //   74	92	178	finally
    //   100	106	186	java/security/NoSuchAlgorithmException
    //   100	106	178	finally
    //   136	141	165	finally
    //   148	155	165	finally
    //   162	165	165	finally
  }
  
  public static String md5(String paramString) {
    try {
      byte[] arrayOfByte = MessageDigest.getInstance("MD5").digest(paramString.getBytes("UTF-8"));
      return toHexString(arrayOfByte);
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      throw new RuntimeException(noSuchAlgorithmException);
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      throw new RuntimeException(unsupportedEncodingException);
    } 
  }
  
  public static String toHexString(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte == null)
      return ""; 
    StringBuilder stringBuilder = new StringBuilder(paramArrayOfbyte.length * 2);
    int i = paramArrayOfbyte.length;
    for (byte b = 0; b < i; b++) {
      byte b1 = paramArrayOfbyte[b];
      stringBuilder.append(hexDigits[b1 >> 4 & 0xF]);
      stringBuilder.append(hexDigits[b1 & 0xF]);
    } 
    return stringBuilder.toString();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/common/util/MD5.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */