package io.xlink.wifi.sdk.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.HandlerThread;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Random;

public final class b {
  public static Context a;
  
  private static Handler b = null;
  
  private static Handler c = null;
  
  private static HandlerThread d = null;
  
  private static int e = 0;
  
  public static byte a(int paramInt, byte paramByte) {
    if (paramInt > 7)
      throw new IllegalAccessError("setByteBit error index>7!!! "); 
    if (paramInt == 0) {
      paramInt = (byte)(paramByte | 0x1);
      return paramInt;
    } 
    if (paramInt == 1) {
      paramInt = (byte)(paramByte | 0x2);
      return paramInt;
    } 
    if (paramInt == 2) {
      paramInt = (byte)(paramByte | 0x4);
      return paramInt;
    } 
    if (paramInt == 3) {
      paramInt = (byte)(paramByte | 0x8);
      return paramInt;
    } 
    if (paramInt == 4) {
      paramInt = (byte)(paramByte | 0x10);
      return paramInt;
    } 
    if (paramInt == 5) {
      paramInt = (byte)(paramByte | 0x20);
      return paramInt;
    } 
    if (paramInt == 6) {
      paramInt = (byte)(paramByte | 0x40);
      return paramInt;
    } 
    int i = paramByte;
    if (paramInt == 7) {
      paramInt = (byte)(paramByte | 0x80);
      i = paramInt;
    } 
    return i;
  }
  
  public static byte a(int paramInt1, int paramInt2) {
    return (byte)((byte)(paramInt1 << 4) | (byte)(paramInt2 << 3 & 0x8));
  }
  
  public static int a(byte paramByte) {
    return 0x0 | paramByte >> 4 & 0xF;
  }
  
  public static String a(byte[] paramArrayOfbyte) {
    StringBuffer stringBuffer = new StringBuffer();
    for (byte b1 = 0; b1 < paramArrayOfbyte.length; b1++) {
      stringBuffer.append(String.format("%02X", new Object[] { Byte.valueOf(paramArrayOfbyte[b1]) }) + " ");
    } 
    return stringBuffer.toString();
  }
  
  public static short a(byte[] paramArrayOfbyte, int paramInt) {
    return (short)((paramArrayOfbyte[paramInt] & 0xFF) << 8 | paramArrayOfbyte[paramInt + 1] & 0xFF);
  }
  
  public static void a() {
    d = new HandlerThread("Xlink Worker Thread", 10);
    d.start();
    b = new Handler(d.getLooper());
    c = new Handler();
  }
  
  public static void a(Runnable paramRunnable) {
    c.post(paramRunnable);
  }
  
  protected static void a(Runnable paramRunnable, Boolean paramBoolean, long paramLong) {
    if (paramBoolean.booleanValue()) {
      b.postAtFrontOfQueue(paramRunnable);
      return;
    } 
    if (paramLong != 0L) {
      b.postDelayed(paramRunnable, paramLong);
      return;
    } 
    b.post(paramRunnable);
  }
  
  public static boolean a(byte paramByte, int paramInt) {
    boolean bool = true;
    if (paramInt > 7)
      throw new IllegalAccessError("readFlagsBit error index>7!!! "); 
    if ((paramByte << 7 - paramInt >> 7 & 0x1) != 1)
      bool = false; 
    return bool;
  }
  
  public static final byte[] a(int paramInt) {
    try {
      byte[] arrayOfByte1 = c(paramInt);
      MessageDigest messageDigest = MessageDigest.getInstance("MD5");
      messageDigest.update(arrayOfByte1);
      byte[] arrayOfByte2 = messageDigest.digest();
    } catch (Exception exception) {
      exception.printStackTrace();
      exception = null;
    } 
    return (byte[])exception;
  }
  
  public static byte[] a(int paramInt, byte[] paramArrayOfbyte) {
    paramArrayOfbyte[3] = (byte)(byte)(paramInt & 0xFF);
    paramArrayOfbyte[2] = (byte)(byte)((0xFF00 & paramInt) >> 8);
    paramArrayOfbyte[1] = (byte)(byte)((0xFF0000 & paramInt) >> 16);
    paramArrayOfbyte[0] = (byte)(byte)((0xFF000000 & paramInt) >> 24);
    return paramArrayOfbyte;
  }
  
  public static final byte[] a(String paramString) {
    try {
      byte[] arrayOfByte2 = paramString.getBytes();
      MessageDigest messageDigest = MessageDigest.getInstance("MD5");
      messageDigest.update(arrayOfByte2);
      byte[] arrayOfByte1 = messageDigest.digest();
    } catch (Exception exception) {
      exception.printStackTrace();
      exception = null;
    } 
    return (byte[])exception;
  }
  
  public static byte[] a(short paramShort) {
    byte[] arrayOfByte = new byte[2];
    for (byte b1 = 0; b1 < 2; b1++)
      arrayOfByte[b1] = (byte)(byte)(paramShort >>> (arrayOfByte.length - 1 - b1) * 8 & 0xFF); 
    return arrayOfByte;
  }
  
  public static byte[] a(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    byte[] arrayOfByte = new byte[paramInt2 - paramInt1];
    System.arraycopy(paramArrayOfbyte, paramInt1, arrayOfByte, 0, paramInt2);
    return arrayOfByte;
  }
  
  public static int b(byte[] paramArrayOfbyte) {
    return (paramArrayOfbyte[0] & 0xFF) << 24 | (paramArrayOfbyte[1] & 0xFF) << 16 | (paramArrayOfbyte[2] & 0xFF) << 8 | paramArrayOfbyte[3] & 0xFF;
  }
  
  public static String b(byte paramByte) {
    String str = "";
    for (byte b1 = 0; b1 < 8; b1++)
      str = str + ((paramByte << b1 & 0x80) >> 7 & 0x1); 
    return str;
  }
  
  public static InetAddress b() {
    InetAddress inetAddress = null;
    try {
      InetAddress inetAddress1 = InetAddress.getByName("255.255.255.255");
      inetAddress = inetAddress1;
    } catch (UnknownHostException unknownHostException) {
      unknownHostException.printStackTrace();
    } 
    return inetAddress;
  }
  
  public static void b(Runnable paramRunnable) {
    a(paramRunnable, Boolean.valueOf(false), 0L);
  }
  
  public static byte[] b(int paramInt) {
    return new byte[] { (byte)(paramInt >> 24 & 0xFF), (byte)(paramInt >> 16 & 0xFF), (byte)(paramInt >> 8 & 0xFF), (byte)(paramInt & 0xFF) };
  }
  
  public static byte[] b(String paramString) {
    byte[] arrayOfByte = a(paramString);
    int i = arrayOfByte.length;
    for (byte b1 = 0; b1 < i; b1++)
      arrayOfByte[b1] = (byte)(byte)(arrayOfByte[b1] ^ 0x35); 
    return arrayOfByte;
  }
  
  public static int c() {
    if (e == 0 || e > 64000)
      e = f(); 
    int i = e;
    e = i + 1;
    return i;
  }
  
  public static String c(byte[] paramArrayOfbyte) {
    return new String(paramArrayOfbyte, Charset.forName("UTF-8"));
  }
  
  public static byte[] c(int paramInt) {
    byte b1 = (byte)(paramInt >> 24 & 0xFF);
    byte b2 = (byte)(paramInt >> 16 & 0xFF);
    byte b3 = (byte)(paramInt >> 8 & 0xFF);
    return new byte[] { (byte)(paramInt & 0xFF), b3, b2, b1 };
  }
  
  public static byte[] c(String paramString) {
    byte b1 = 0;
    if (paramString.isEmpty() || paramString.length() % 2 != 0)
      return null; 
    paramString.replaceAll(":", "");
    String str = paramString.toLowerCase();
    byte[] arrayOfByte = new byte[str.length() / 2];
    byte b2 = 0;
    while (true) {
      if (b1 < arrayOfByte.length) {
        arrayOfByte[b1] = (byte)(byte)((byte)(Character.digit(str.charAt(b2), 16) & 0xFF) << 4 | (byte)(Character.digit(str.charAt(b2 + 1), 16) & 0xFF));
        b2 += 2;
        b1++;
        continue;
      } 
      return arrayOfByte;
    } 
  }
  
  public static int d(int paramInt) {
    int i = paramInt;
    if (paramInt < 0)
      i = paramInt + 65536; 
    return i;
  }
  
  public static String d() {
    WifiManager wifiManager = (WifiManager)a.getSystemService("wifi");
    return !wifiManager.isWifiEnabled() ? null : e(wifiManager.getConnectionInfo().getIpAddress());
  }
  
  public static String d(byte[] paramArrayOfbyte) {
    return String.format("%02X%02X%02X%02X%02X%02X", new Object[] { Byte.valueOf(paramArrayOfbyte[0]), Byte.valueOf(paramArrayOfbyte[1]), Byte.valueOf(paramArrayOfbyte[2]), Byte.valueOf(paramArrayOfbyte[3]), Byte.valueOf(paramArrayOfbyte[4]), Byte.valueOf(paramArrayOfbyte[5]) });
  }
  
  public static byte[] d(String paramString) {
    return paramString.getBytes(Charset.forName("UTF-8"));
  }
  
  private static String e(int paramInt) {
    return (paramInt & 0xFF) + "." + (paramInt >> 8 & 0xFF) + "." + (paramInt >> 16 & 0xFF) + "." + (paramInt >> 24 & 0xFF);
  }
  
  public static InetAddress e(String paramString) {
    UnknownHostException unknownHostException2 = null;
    try {
      InetAddress inetAddress = InetAddress.getByName(paramString);
    } catch (UnknownHostException unknownHostException1) {
      unknownHostException1.printStackTrace();
      unknownHostException1 = unknownHostException2;
    } 
    return (InetAddress)unknownHostException1;
  }
  
  public static boolean e() {
    boolean bool1 = false;
    ConnectivityManager connectivityManager = (ConnectivityManager)a.getSystemService("connectivity");
    boolean bool2 = bool1;
    if (connectivityManager != null) {
      NetworkInfo[] arrayOfNetworkInfo = connectivityManager.getAllNetworkInfo();
      bool2 = bool1;
      if (arrayOfNetworkInfo != null)
        for (byte b1 = 0;; b1++) {
          bool2 = bool1;
          if (b1 < arrayOfNetworkInfo.length) {
            if (arrayOfNetworkInfo[b1].getTypeName().equals("WIFI") && arrayOfNetworkInfo[b1].isConnected())
              return true; 
          } else {
            return bool2;
          } 
        }  
    } 
    return bool2;
  }
  
  private static int f() {
    return (new Random()).nextInt(64000) % 63001 + 1000;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/util/b.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */