package com.tencent.bugly.proguard;

import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class ae implements ag {
  private String a = null;
  
  public final void a(String paramString) {
    if (paramString != null) {
      for (int i = paramString.length(); i < 16; i++)
        paramString = paramString + "0"; 
      this.a = paramString.substring(0, 16);
    } 
  }
  
  public final byte[] a(byte[] paramArrayOfbyte) throws Exception {
    byte b1 = 0;
    if (this.a == null || paramArrayOfbyte == null)
      return null; 
    StringBuffer stringBuffer1 = new StringBuffer();
    int i = paramArrayOfbyte.length;
    byte b2;
    for (b2 = 0; b2 < i; b2++) {
      byte b = paramArrayOfbyte[b2];
      stringBuffer1.append(b + " ");
    } 
    SecretKeySpec secretKeySpec = new SecretKeySpec(this.a.getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(2, secretKeySpec, new IvParameterSpec(this.a.getBytes()));
    byte[] arrayOfByte = cipher.doFinal(paramArrayOfbyte);
    StringBuffer stringBuffer2 = new StringBuffer();
    i = arrayOfByte.length;
    b2 = b1;
    while (true) {
      paramArrayOfbyte = arrayOfByte;
      if (b2 < i) {
        b1 = arrayOfByte[b2];
        stringBuffer2.append(b1 + " ");
        b2++;
        continue;
      } 
      return paramArrayOfbyte;
    } 
  }
  
  public final byte[] b(byte[] paramArrayOfbyte) throws Exception, NoSuchAlgorithmException {
    byte b1 = 0;
    if (this.a == null || paramArrayOfbyte == null)
      return null; 
    StringBuffer stringBuffer1 = new StringBuffer();
    int i = paramArrayOfbyte.length;
    byte b2;
    for (b2 = 0; b2 < i; b2++) {
      byte b = paramArrayOfbyte[b2];
      stringBuffer1.append(b + " ");
    } 
    SecretKeySpec secretKeySpec = new SecretKeySpec(this.a.getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(1, secretKeySpec, new IvParameterSpec(this.a.getBytes()));
    byte[] arrayOfByte = cipher.doFinal(paramArrayOfbyte);
    StringBuffer stringBuffer2 = new StringBuffer();
    i = arrayOfByte.length;
    b2 = b1;
    while (true) {
      paramArrayOfbyte = arrayOfByte;
      if (b2 < i) {
        b1 = arrayOfByte[b2];
        stringBuffer2.append(b1 + " ");
        b2++;
        continue;
      } 
      return paramArrayOfbyte;
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/proguard/ae.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */