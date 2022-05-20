package app.esptouch.util;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class ByteUtil {
  public static final String ESPTOUCH_ENCODING_CHARSET = "UTF-8";
  
  public static byte combine2bytesToOne(byte paramByte1, byte paramByte2) {
    if (paramByte1 < 0 || paramByte1 > 15 || paramByte2 < 0 || paramByte2 > 15)
      throw new RuntimeException("Out of Boundary"); 
    return (byte)(paramByte1 << 4 | paramByte2);
  }
  
  public static char combine2bytesToU16(byte paramByte1, byte paramByte2) {
    return (char)(convertByte2Uint8(paramByte1) << 8 | convertByte2Uint8(paramByte2));
  }
  
  public static String convertByte2HexString(byte paramByte) {
    return Integer.toHexString(convertByte2Uint8(paramByte));
  }
  
  public static char convertByte2Uint8(byte paramByte) {
    return (char)(paramByte & 0xFF);
  }
  
  public static char[] convertBytes2Uint8s(byte[] paramArrayOfbyte) {
    int i = paramArrayOfbyte.length;
    char[] arrayOfChar = new char[i];
    for (byte b = 0; b < i; b++)
      arrayOfChar[b] = convertByte2Uint8(paramArrayOfbyte[b]); 
    return arrayOfChar;
  }
  
  public static String convertU8ToHexString(char paramChar) {
    return Integer.toHexString(paramChar);
  }
  
  public static byte convertUint8toByte(char paramChar) {
    if (paramChar > 'ÿ')
      throw new RuntimeException("Out of Boundary"); 
    return (byte)paramChar;
  }
  
  public static byte[] genSpecBytes(byte paramByte) {
    return genSpecBytes(convertByte2Uint8(paramByte));
  }
  
  public static byte[] genSpecBytes(char paramChar) {
    byte[] arrayOfByte = new byte[paramChar];
    for (byte b = 0; b < paramChar; b++)
      arrayOfByte[b] = (byte)49; 
    return arrayOfByte;
  }
  
  public static byte[] getBytesByString(String paramString) {
    try {
      return paramString.getBytes("UTF-8");
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      throw new IllegalArgumentException("the charset is invalid");
    } 
  }
  
  public static void main(String[] paramArrayOfString) {
    test_convertUint8toByte();
    test_convertChar2Uint8();
    test_splitUint8To2bytes();
    test_combine2bytesToOne();
    test_parseBssid();
  }
  
  public static String parseBssid(byte[] paramArrayOfbyte) {
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b < paramArrayOfbyte.length; b++) {
      int i = paramArrayOfbyte[b] & 0xFF;
      String str = Integer.toHexString(i);
      if (i < 16)
        str = "0" + str; 
      stringBuilder.append(str);
    } 
    return stringBuilder.toString();
  }
  
  public static String parseBssid(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    byte[] arrayOfByte = new byte[paramInt2];
    for (byte b = 0; b < paramInt2; b++)
      arrayOfByte[b] = (byte)paramArrayOfbyte[b + paramInt1]; 
    return parseBssid(arrayOfByte);
  }
  
  public static void putString2bytes(byte[] paramArrayOfbyte, String paramString, int paramInt1, int paramInt2, int paramInt3) {
    for (paramInt1 = 0; paramInt1 < paramInt3; paramInt1++)
      paramArrayOfbyte[paramInt3 + paramInt1] = (byte)paramString.getBytes()[paramInt1]; 
  }
  
  public static void putbytes2Uint8s(char[] paramArrayOfchar, byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3) {
    for (byte b = 0; b < paramInt3; b++)
      paramArrayOfchar[paramInt1 + b] = convertByte2Uint8(paramArrayOfbyte[paramInt2 + b]); 
  }
  
  private static byte randomByte() {
    return (byte)(127 - (new Random()).nextInt(256));
  }
  
  public static byte[] randomBytes(byte paramByte) {
    return randomBytes(convertByte2Uint8(paramByte));
  }
  
  public static byte[] randomBytes(char paramChar) {
    byte[] arrayOfByte = new byte[paramChar];
    for (byte b = 0; b < paramChar; b++)
      arrayOfByte[b] = randomByte(); 
    return arrayOfByte;
  }
  
  public static byte[] splitUint8To2bytes(char paramChar) {
    if (paramChar < '\000' || paramChar > 'ÿ')
      throw new RuntimeException("Out of Boundary"); 
    String str = Integer.toHexString(paramChar);
    if (str.length() > 1) {
      byte b3 = (byte)Integer.parseInt(str.substring(0, 1), 16);
      byte b2 = (byte)Integer.parseInt(str.substring(1, 2), 16);
      byte b4 = b2;
      byte b5 = b3;
      return new byte[] { b5, b4 };
    } 
    paramChar = Character.MIN_VALUE;
    byte b = (byte)Integer.parseInt(str.substring(0, 1), 16);
    char c = paramChar;
    byte b1 = b;
    return new byte[] { c, b1 };
  }
  
  private static void test_combine2bytesToOne() {
    if (combine2bytesToOne((byte)1, (byte)4) == 20);
  }
  
  private static void test_convertChar2Uint8() {
    if (convertByte2Uint8((byte)97) == 'a' && convertByte2Uint8(-128) == '' && convertByte2Uint8((byte)-1) == 'ÿ') {
      System.out.println("test_convertChar2Uint8(): pass");
      return;
    } 
    System.out.println("test_convertChar2Uint8(): fail");
  }
  
  private static void test_convertUint8toByte() {
    if (convertUint8toByte('a') == 97 && convertUint8toByte('') == Byte.MIN_VALUE && convertUint8toByte('ÿ') == -1) {
      System.out.println("test_convertUint8toByte(): pass");
      return;
    } 
    System.out.println("test_convertUint8toByte(): fail");
  }
  
  private static void test_parseBssid() {
    if (parseBssid(new byte[] { 15, -2, 52, -102, -93, -60 }).equals("0ffe349aa3c4")) {
      System.out.println("test_parseBssid(): pass");
      return;
    } 
    System.out.println("test_parseBssid(): fail");
  }
  
  private static void test_splitUint8To2bytes() {
    byte[] arrayOfByte = splitUint8To2bytes('\024');
    if (arrayOfByte[0] != 1 || arrayOfByte[1] == 4);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/esptouch/util/ByteUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */