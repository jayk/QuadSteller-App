package com.tencent.bugly.proguard;

public final class e {
  private static final char[] a = new char[] { 
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
      'A', 'B', 'C', 'D', 'E', 'F' };
  
  public static String a(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte == null || paramArrayOfbyte.length == 0)
      return null; 
    char[] arrayOfChar = new char[paramArrayOfbyte.length * 2];
    for (byte b = 0; b < paramArrayOfbyte.length; b++) {
      byte b1 = paramArrayOfbyte[b];
      arrayOfChar[b * 2 + 1] = (char)a[b1 & 0xF];
      b1 = (byte)(b1 >>> 4);
      arrayOfChar[b * 2] = (char)a[b1 & 0xF];
    } 
    return new String(arrayOfChar);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/e.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */