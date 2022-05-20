package com.blankj.utilcode.util;

import android.util.Base64;
import java.io.File;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class EncryptUtils {
  private static final String AES_Algorithm = "AES";
  
  public static String AES_Transformation;
  
  private static final String DES_Algorithm = "DES";
  
  public static String DES_Transformation = "DES/ECB/NoPadding";
  
  private static final String TripleDES_Algorithm = "DESede";
  
  public static String TripleDES_Transformation = "DESede/ECB/NoPadding";
  
  private static final char[] hexDigits;
  
  static {
    AES_Transformation = "AES/ECB/NoPadding";
    hexDigits = new char[] { 
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
        'A', 'B', 'C', 'D', 'E', 'F' };
  }
  
  private EncryptUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  private static byte[] base64Decode(byte[] paramArrayOfbyte) {
    return Base64.decode(paramArrayOfbyte, 2);
  }
  
  private static byte[] base64Encode(byte[] paramArrayOfbyte) {
    return Base64.encode(paramArrayOfbyte, 2);
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
  
  public static byte[] decrypt3DES(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return desTemplate(paramArrayOfbyte1, paramArrayOfbyte2, "DESede", TripleDES_Transformation, false);
  }
  
  public static byte[] decryptAES(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return desTemplate(paramArrayOfbyte1, paramArrayOfbyte2, "AES", AES_Transformation, false);
  }
  
  public static byte[] decryptBase64AES(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return decryptAES(base64Decode(paramArrayOfbyte1), paramArrayOfbyte2);
  }
  
  public static byte[] decryptBase64DES(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return decryptDES(base64Decode(paramArrayOfbyte1), paramArrayOfbyte2);
  }
  
  public static byte[] decryptBase64_3DES(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return decrypt3DES(base64Decode(paramArrayOfbyte1), paramArrayOfbyte2);
  }
  
  public static byte[] decryptDES(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return desTemplate(paramArrayOfbyte1, paramArrayOfbyte2, "DES", DES_Transformation, false);
  }
  
  public static byte[] decryptHexString3DES(String paramString, byte[] paramArrayOfbyte) {
    return decrypt3DES(hexString2Bytes(paramString), paramArrayOfbyte);
  }
  
  public static byte[] decryptHexStringAES(String paramString, byte[] paramArrayOfbyte) {
    return decryptAES(hexString2Bytes(paramString), paramArrayOfbyte);
  }
  
  public static byte[] decryptHexStringDES(String paramString, byte[] paramArrayOfbyte) {
    return decryptDES(hexString2Bytes(paramString), paramArrayOfbyte);
  }
  
  public static byte[] desTemplate(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, String paramString1, String paramString2, boolean paramBoolean) {
    byte[] arrayOfByte1 = null;
    byte[] arrayOfByte2 = arrayOfByte1;
    if (paramArrayOfbyte1 != null) {
      arrayOfByte2 = arrayOfByte1;
      if (paramArrayOfbyte1.length != 0) {
        arrayOfByte2 = arrayOfByte1;
        if (paramArrayOfbyte2 != null) {
          if (paramArrayOfbyte2.length == 0)
            return arrayOfByte1; 
        } else {
          return arrayOfByte2;
        } 
      } else {
        return arrayOfByte2;
      } 
    } else {
      return arrayOfByte2;
    } 
    try {
      byte b;
      SecretKeySpec secretKeySpec = new SecretKeySpec();
      this(paramArrayOfbyte2, paramString1);
      Cipher cipher = Cipher.getInstance(paramString2);
      SecureRandom secureRandom = new SecureRandom();
      this();
      if (paramBoolean) {
        b = 1;
      } else {
        b = 2;
      } 
      cipher.init(b, secretKeySpec, secureRandom);
      byte[] arrayOfByte = cipher.doFinal(paramArrayOfbyte1);
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      arrayOfByte2 = arrayOfByte1;
    } 
    return arrayOfByte2;
  }
  
  public static byte[] encrypt3DES(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return desTemplate(paramArrayOfbyte1, paramArrayOfbyte2, "DESede", TripleDES_Transformation, true);
  }
  
  public static byte[] encrypt3DES2Base64(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return base64Encode(encrypt3DES(paramArrayOfbyte1, paramArrayOfbyte2));
  }
  
  public static String encrypt3DES2HexString(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return bytes2HexString(encrypt3DES(paramArrayOfbyte1, paramArrayOfbyte2));
  }
  
  public static byte[] encryptAES(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return desTemplate(paramArrayOfbyte1, paramArrayOfbyte2, "AES", AES_Transformation, true);
  }
  
  public static byte[] encryptAES2Base64(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return base64Encode(encryptAES(paramArrayOfbyte1, paramArrayOfbyte2));
  }
  
  public static String encryptAES2HexString(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return bytes2HexString(encryptAES(paramArrayOfbyte1, paramArrayOfbyte2));
  }
  
  public static byte[] encryptDES(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return desTemplate(paramArrayOfbyte1, paramArrayOfbyte2, "DES", DES_Transformation, true);
  }
  
  public static byte[] encryptDES2Base64(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return base64Encode(encryptDES(paramArrayOfbyte1, paramArrayOfbyte2));
  }
  
  public static String encryptDES2HexString(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return bytes2HexString(encryptDES(paramArrayOfbyte1, paramArrayOfbyte2));
  }
  
  public static byte[] encryptHmacMD5(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return hmacTemplate(paramArrayOfbyte1, paramArrayOfbyte2, "HmacMD5");
  }
  
  public static String encryptHmacMD5ToString(String paramString1, String paramString2) {
    return encryptHmacMD5ToString(paramString1.getBytes(), paramString2.getBytes());
  }
  
  public static String encryptHmacMD5ToString(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return bytes2HexString(encryptHmacMD5(paramArrayOfbyte1, paramArrayOfbyte2));
  }
  
  public static byte[] encryptHmacSHA1(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return hmacTemplate(paramArrayOfbyte1, paramArrayOfbyte2, "HmacSHA1");
  }
  
  public static String encryptHmacSHA1ToString(String paramString1, String paramString2) {
    return encryptHmacSHA1ToString(paramString1.getBytes(), paramString2.getBytes());
  }
  
  public static String encryptHmacSHA1ToString(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return bytes2HexString(encryptHmacSHA1(paramArrayOfbyte1, paramArrayOfbyte2));
  }
  
  public static byte[] encryptHmacSHA224(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return hmacTemplate(paramArrayOfbyte1, paramArrayOfbyte2, "HmacSHA224");
  }
  
  public static String encryptHmacSHA224ToString(String paramString1, String paramString2) {
    return encryptHmacSHA224ToString(paramString1.getBytes(), paramString2.getBytes());
  }
  
  public static String encryptHmacSHA224ToString(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return bytes2HexString(encryptHmacSHA224(paramArrayOfbyte1, paramArrayOfbyte2));
  }
  
  public static byte[] encryptHmacSHA256(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return hmacTemplate(paramArrayOfbyte1, paramArrayOfbyte2, "HmacSHA256");
  }
  
  public static String encryptHmacSHA256ToString(String paramString1, String paramString2) {
    return encryptHmacSHA256ToString(paramString1.getBytes(), paramString2.getBytes());
  }
  
  public static String encryptHmacSHA256ToString(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return bytes2HexString(encryptHmacSHA256(paramArrayOfbyte1, paramArrayOfbyte2));
  }
  
  public static byte[] encryptHmacSHA384(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return hmacTemplate(paramArrayOfbyte1, paramArrayOfbyte2, "HmacSHA384");
  }
  
  public static String encryptHmacSHA384ToString(String paramString1, String paramString2) {
    return encryptHmacSHA384ToString(paramString1.getBytes(), paramString2.getBytes());
  }
  
  public static String encryptHmacSHA384ToString(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return bytes2HexString(encryptHmacSHA384(paramArrayOfbyte1, paramArrayOfbyte2));
  }
  
  public static byte[] encryptHmacSHA512(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return hmacTemplate(paramArrayOfbyte1, paramArrayOfbyte2, "HmacSHA512");
  }
  
  public static String encryptHmacSHA512ToString(String paramString1, String paramString2) {
    return encryptHmacSHA512ToString(paramString1.getBytes(), paramString2.getBytes());
  }
  
  public static String encryptHmacSHA512ToString(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    return bytes2HexString(encryptHmacSHA512(paramArrayOfbyte1, paramArrayOfbyte2));
  }
  
  public static byte[] encryptMD2(byte[] paramArrayOfbyte) {
    return hashTemplate(paramArrayOfbyte, "MD2");
  }
  
  public static String encryptMD2ToString(String paramString) {
    return encryptMD2ToString(paramString.getBytes());
  }
  
  public static String encryptMD2ToString(byte[] paramArrayOfbyte) {
    return bytes2HexString(encryptMD2(paramArrayOfbyte));
  }
  
  public static byte[] encryptMD5(byte[] paramArrayOfbyte) {
    return hashTemplate(paramArrayOfbyte, "MD5");
  }
  
  public static byte[] encryptMD5File(File paramFile) {
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
    //   12: aconst_null
    //   13: astore_3
    //   14: aconst_null
    //   15: astore #4
    //   17: aload_2
    //   18: astore #5
    //   20: new java/io/FileInputStream
    //   23: astore #6
    //   25: aload_2
    //   26: astore #5
    //   28: aload #6
    //   30: aload_0
    //   31: invokespecial <init> : (Ljava/io/File;)V
    //   34: ldc 'MD5'
    //   36: invokestatic getInstance : (Ljava/lang/String;)Ljava/security/MessageDigest;
    //   39: astore #5
    //   41: new java/security/DigestInputStream
    //   44: astore_0
    //   45: aload_0
    //   46: aload #6
    //   48: aload #5
    //   50: invokespecial <init> : (Ljava/io/InputStream;Ljava/security/MessageDigest;)V
    //   53: ldc 262144
    //   55: newarray byte
    //   57: astore #5
    //   59: aload_0
    //   60: aload #5
    //   62: invokevirtual read : ([B)I
    //   65: ifgt -> 59
    //   68: aload_0
    //   69: invokevirtual getMessageDigest : ()Ljava/security/MessageDigest;
    //   72: invokevirtual digest : ()[B
    //   75: astore_0
    //   76: iconst_1
    //   77: anewarray java/io/Closeable
    //   80: dup
    //   81: iconst_0
    //   82: aload #6
    //   84: aastore
    //   85: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   88: goto -> 8
    //   91: astore #6
    //   93: aload #4
    //   95: astore_0
    //   96: aload_0
    //   97: astore #5
    //   99: aload #6
    //   101: invokevirtual printStackTrace : ()V
    //   104: iconst_1
    //   105: anewarray java/io/Closeable
    //   108: dup
    //   109: iconst_0
    //   110: aload_0
    //   111: aastore
    //   112: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   115: aload_1
    //   116: astore_0
    //   117: goto -> 8
    //   120: astore_0
    //   121: iconst_1
    //   122: anewarray java/io/Closeable
    //   125: dup
    //   126: iconst_0
    //   127: aload #5
    //   129: aastore
    //   130: invokestatic closeIO : ([Ljava/io/Closeable;)V
    //   133: aload_0
    //   134: athrow
    //   135: astore #6
    //   137: aload_3
    //   138: astore_0
    //   139: goto -> 96
    //   142: astore_0
    //   143: aload #6
    //   145: astore #5
    //   147: goto -> 121
    //   150: astore #5
    //   152: aload #6
    //   154: astore_0
    //   155: aload #5
    //   157: astore #6
    //   159: goto -> 96
    //   162: astore #5
    //   164: aload #6
    //   166: astore_0
    //   167: aload #5
    //   169: astore #6
    //   171: goto -> 139
    // Exception table:
    //   from	to	target	type
    //   20	25	91	java/security/NoSuchAlgorithmException
    //   20	25	135	java/io/IOException
    //   20	25	120	finally
    //   28	34	91	java/security/NoSuchAlgorithmException
    //   28	34	135	java/io/IOException
    //   28	34	120	finally
    //   34	59	150	java/security/NoSuchAlgorithmException
    //   34	59	162	java/io/IOException
    //   34	59	142	finally
    //   59	76	150	java/security/NoSuchAlgorithmException
    //   59	76	162	java/io/IOException
    //   59	76	142	finally
    //   99	104	120	finally
  }
  
  public static byte[] encryptMD5File(String paramString) {
    if (isSpace(paramString)) {
      paramString = null;
      return encryptMD5File((File)paramString);
    } 
    File file = new File(paramString);
    return encryptMD5File(file);
  }
  
  public static String encryptMD5File2String(File paramFile) {
    return bytes2HexString(encryptMD5File(paramFile));
  }
  
  public static String encryptMD5File2String(String paramString) {
    if (isSpace(paramString)) {
      paramString = null;
      return encryptMD5File2String((File)paramString);
    } 
    File file = new File(paramString);
    return encryptMD5File2String(file);
  }
  
  public static String encryptMD5ToString(String paramString) {
    return encryptMD5ToString(paramString.getBytes());
  }
  
  public static String encryptMD5ToString(String paramString1, String paramString2) {
    return bytes2HexString(encryptMD5((paramString1 + paramString2).getBytes()));
  }
  
  public static String encryptMD5ToString(byte[] paramArrayOfbyte) {
    return bytes2HexString(encryptMD5(paramArrayOfbyte));
  }
  
  public static String encryptMD5ToString(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    if (paramArrayOfbyte1 == null || paramArrayOfbyte2 == null)
      return null; 
    byte[] arrayOfByte = new byte[paramArrayOfbyte1.length + paramArrayOfbyte2.length];
    System.arraycopy(paramArrayOfbyte1, 0, arrayOfByte, 0, paramArrayOfbyte1.length);
    System.arraycopy(paramArrayOfbyte2, 0, arrayOfByte, paramArrayOfbyte1.length, paramArrayOfbyte2.length);
    return bytes2HexString(encryptMD5(arrayOfByte));
  }
  
  public static byte[] encryptSHA1(byte[] paramArrayOfbyte) {
    return hashTemplate(paramArrayOfbyte, "SHA1");
  }
  
  public static String encryptSHA1ToString(String paramString) {
    return encryptSHA1ToString(paramString.getBytes());
  }
  
  public static String encryptSHA1ToString(byte[] paramArrayOfbyte) {
    return bytes2HexString(encryptSHA1(paramArrayOfbyte));
  }
  
  public static byte[] encryptSHA224(byte[] paramArrayOfbyte) {
    return hashTemplate(paramArrayOfbyte, "SHA224");
  }
  
  public static String encryptSHA224ToString(String paramString) {
    return encryptSHA224ToString(paramString.getBytes());
  }
  
  public static String encryptSHA224ToString(byte[] paramArrayOfbyte) {
    return bytes2HexString(encryptSHA224(paramArrayOfbyte));
  }
  
  public static byte[] encryptSHA256(byte[] paramArrayOfbyte) {
    return hashTemplate(paramArrayOfbyte, "SHA256");
  }
  
  public static String encryptSHA256ToString(String paramString) {
    return encryptSHA256ToString(paramString.getBytes());
  }
  
  public static String encryptSHA256ToString(byte[] paramArrayOfbyte) {
    return bytes2HexString(encryptSHA256(paramArrayOfbyte));
  }
  
  public static byte[] encryptSHA384(byte[] paramArrayOfbyte) {
    return hashTemplate(paramArrayOfbyte, "SHA384");
  }
  
  public static String encryptSHA384ToString(String paramString) {
    return encryptSHA384ToString(paramString.getBytes());
  }
  
  public static String encryptSHA384ToString(byte[] paramArrayOfbyte) {
    return bytes2HexString(encryptSHA384(paramArrayOfbyte));
  }
  
  public static byte[] encryptSHA512(byte[] paramArrayOfbyte) {
    return hashTemplate(paramArrayOfbyte, "SHA512");
  }
  
  public static String encryptSHA512ToString(String paramString) {
    return encryptSHA512ToString(paramString.getBytes());
  }
  
  public static String encryptSHA512ToString(byte[] paramArrayOfbyte) {
    return bytes2HexString(encryptSHA512(paramArrayOfbyte));
  }
  
  private static byte[] hashTemplate(byte[] paramArrayOfbyte, String paramString) {
    byte[] arrayOfByte1 = null;
    byte[] arrayOfByte2 = arrayOfByte1;
    if (paramArrayOfbyte != null) {
      if (paramArrayOfbyte.length <= 0)
        return arrayOfByte1; 
    } else {
      return arrayOfByte2;
    } 
    try {
      MessageDigest messageDigest = MessageDigest.getInstance(paramString);
      messageDigest.update(paramArrayOfbyte);
      arrayOfByte2 = messageDigest.digest();
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      noSuchAlgorithmException.printStackTrace();
      arrayOfByte2 = arrayOfByte1;
    } 
    return arrayOfByte2;
  }
  
  private static int hex2Dec(char paramChar) {
    if (paramChar >= '0' && paramChar <= '9') {
      paramChar -= '0';
      return paramChar;
    } 
    if (paramChar >= 'A' && paramChar <= 'F')
      return paramChar - 65 + 10; 
    throw new IllegalArgumentException();
  }
  
  private static byte[] hexString2Bytes(String paramString) {
    if (isSpace(paramString))
      return null; 
    int i = paramString.length();
    int j = i;
    String str = paramString;
    if (i % 2 != 0) {
      str = "0" + paramString;
      j = i + 1;
    } 
    char[] arrayOfChar = str.toUpperCase().toCharArray();
    byte[] arrayOfByte = new byte[j >> 1];
    i = 0;
    while (true) {
      byte[] arrayOfByte1 = arrayOfByte;
      if (i < j) {
        arrayOfByte[i >> 1] = (byte)(byte)(hex2Dec(arrayOfChar[i]) << 4 | hex2Dec(arrayOfChar[i + 1]));
        i += 2;
        continue;
      } 
      return arrayOfByte1;
    } 
  }
  
  private static byte[] hmacTemplate(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, String paramString) {
    byte[] arrayOfByte = null;
    null = arrayOfByte;
    if (paramArrayOfbyte1 != null) {
      null = arrayOfByte;
      if (paramArrayOfbyte1.length != 0) {
        null = arrayOfByte;
        if (paramArrayOfbyte2 != null) {
          if (paramArrayOfbyte2.length == 0)
            return arrayOfByte; 
        } else {
          return null;
        } 
      } else {
        return null;
      } 
    } else {
      return null;
    } 
    try {
      SecretKeySpec secretKeySpec = new SecretKeySpec();
      this(paramArrayOfbyte2, paramString);
      Mac mac = Mac.getInstance(paramString);
      mac.init(secretKeySpec);
      return mac.doFinal(paramArrayOfbyte1);
    } catch (InvalidKeyException invalidKeyException) {
    
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {}
    noSuchAlgorithmException.printStackTrace();
    return arrayOfByte;
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


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/EncryptUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */