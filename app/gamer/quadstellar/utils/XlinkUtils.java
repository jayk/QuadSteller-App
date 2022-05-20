package app.gamer.quadstellar.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Base64;
import app.gamer.quadstellar.App;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class XlinkUtils {
  private static final int bitValue0 = 1;
  
  private static final int bitValue1 = 2;
  
  private static final int bitValue2 = 4;
  
  private static final int bitValue3 = 8;
  
  private static final int bitValue4 = 16;
  
  private static final int bitValue5 = 32;
  
  private static final int bitValue6 = 64;
  
  private static final int bitValue7 = 128;
  
  public static final String MD5(String paramString) {
    char[] arrayOfChar = new char[16];
    arrayOfChar[0] = '0';
    arrayOfChar[1] = '1';
    arrayOfChar[2] = '2';
    arrayOfChar[3] = '3';
    arrayOfChar[4] = '4';
    arrayOfChar[5] = '5';
    arrayOfChar[6] = '6';
    arrayOfChar[7] = '7';
    arrayOfChar[8] = '8';
    arrayOfChar[9] = '9';
    arrayOfChar[10] = 'A';
    arrayOfChar[11] = 'B';
    arrayOfChar[12] = 'C';
    arrayOfChar[13] = 'D';
    arrayOfChar[14] = 'E';
    arrayOfChar[15] = 'F';
    try {
      byte[] arrayOfByte = paramString.getBytes();
      MessageDigest messageDigest = MessageDigest.getInstance("MD5");
      messageDigest.update(arrayOfByte);
      arrayOfByte = messageDigest.digest();
      int i = arrayOfByte.length;
      char[] arrayOfChar1 = new char[i * 2];
      byte b = 0;
      int j = 0;
      while (b < i) {
        byte b1 = arrayOfByte[b];
        int k = j + 1;
        arrayOfChar1[j] = (char)arrayOfChar[b1 >>> 4 & 0xF];
        j = k + 1;
        arrayOfChar1[k] = (char)arrayOfChar[b1 & 0xF];
        b++;
      } 
      String str = new String();
      this(arrayOfChar1);
    } catch (Exception exception) {
      exception.printStackTrace();
      exception = null;
    } 
    return (String)exception;
  }
  
  public static byte[] base64Decrypt(String paramString) {
    null = null;
    if (paramString != null) {
      byte[] arrayOfByte = Base64.decode(paramString, 0);
      if (arrayOfByte != null) {
        null = arrayOfByte;
        return (arrayOfByte.length == 0) ? paramString.getBytes() : null;
      } 
    } else {
      return null;
    } 
    return paramString.getBytes();
  }
  
  public static String base64Encrypt(byte[] paramArrayOfbyte) {
    return new String(Base64.encode(paramArrayOfbyte, 0));
  }
  
  public static String base64EncryptUTF(byte[] paramArrayOfbyte) {
    UnsupportedEncodingException unsupportedEncodingException2 = null;
    try {
      String str2 = new String();
      this(Base64.encode(paramArrayOfbyte, 0), "UTF-8");
      String str1 = str2;
    } catch (UnsupportedEncodingException unsupportedEncodingException1) {
      unsupportedEncodingException1.printStackTrace();
      unsupportedEncodingException1 = unsupportedEncodingException2;
    } 
    return (String)unsupportedEncodingException1;
  }
  
  public static String getBinString(byte paramByte) {
    String str = "";
    for (byte b = 0; b < 8; b++)
      str = str + ((paramByte << b & 0x80) >> 7 & 0x1); 
    return str;
  }
  
  public static String getHexBinString(byte[] paramArrayOfbyte) {
    StringBuffer stringBuffer = new StringBuffer();
    for (byte b = 0; b < paramArrayOfbyte.length; b++) {
      stringBuffer.append(String.format("%02X", new Object[] { Byte.valueOf(paramArrayOfbyte[b]) }) + " ");
    } 
    return stringBuffer.toString();
  }
  
  public static JSONObject getJsonObject(Map<String, Object> paramMap) {
    JSONObject jSONObject = new JSONObject();
    for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
      try {
        jSONObject.put((String)entry.getKey(), entry.getValue());
      } catch (JSONException jSONException) {
        jSONException.printStackTrace();
      } 
    } 
    return jSONObject;
  }
  
  public static boolean isConnected() {
    ConnectivityManager connectivityManager = (ConnectivityManager)App.getInstance().getSystemService("connectivity");
    if (connectivityManager != null) {
      NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
      if (networkInfo != null && networkInfo.isAvailable() && connectivityManager.getBackgroundDataSetting())
        return true; 
    } 
    return false;
  }
  
  public static boolean isWifi() {
    boolean bool = true;
    ConnectivityManager connectivityManager = (ConnectivityManager)App.getInstance().getSystemService("connectivity");
    if (connectivityManager == null || connectivityManager.getActiveNetworkInfo() == null)
      return false; 
    if (connectivityManager.getActiveNetworkInfo().getType() != 1)
      bool = false; 
    return bool;
  }
  
  public static void longTips(int paramInt) {
    if (paramInt != 0)
      Tips.showLongToast(App.getAppResources().getString(paramInt)); 
  }
  
  public static void longTips(String paramString) {
    if (paramString != null)
      Tips.showLongToast(paramString); 
  }
  
  public static void openSetting(Activity paramActivity) {
    Intent intent;
    if (Build.VERSION.SDK_INT > 10) {
      intent = new Intent("android.settings.WIFI_SETTINGS");
    } else {
      intent = new Intent("/");
      intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.WirelessSettings"));
      intent.setAction("android.intent.action.VIEW");
    } 
    paramActivity.startActivityForResult(intent, 0);
  }
  
  public static final boolean ping() {
    try {
      Runtime runtime = Runtime.getRuntime();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      Process process = runtime.exec(stringBuilder.append("ping -c 3 -w 100 ").append("www.baidu.com").toString());
      InputStream inputStream = process.getInputStream();
      BufferedReader bufferedReader = new BufferedReader();
      InputStreamReader inputStreamReader = new InputStreamReader();
      this(inputStream);
      this(inputStreamReader);
      StringBuffer stringBuffer = new StringBuffer();
      this();
      while (true) {
        String str = bufferedReader.readLine();
        if (str != null) {
          stringBuffer.append(str);
          continue;
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        this();
        LogUtil.d("------ping-----", stringBuilder1.append("result content : ").append(stringBuffer.toString()).toString());
        if (process.waitFor() == 0)
          return true; 
        return false;
      } 
    } catch (IOException iOException) {
      return false;
    } catch (InterruptedException interruptedException) {
      return false;
    } finally {
      LogUtil.d("----result---", "result = " + null);
    } 
  }
  
  public static boolean readFlagsBit(byte paramByte, int paramInt) {
    boolean bool = true;
    if (paramInt > 7)
      throw new IllegalAccessError("readFlagsBit error index>7!!! "); 
    if ((paramByte << 7 - paramInt >> 7 & 0x1) != 1)
      bool = false; 
    return bool;
  }
  
  public static byte setByteBit(int paramInt, byte paramByte) {
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
  
  public static void shortTips(int paramInt) {
    if (paramInt != 0)
      Tips.showShortToast(App.getAppResources().getString(paramInt)); 
  }
  
  public static void shortTips(String paramString) {
    if (paramString != null)
      Tips.showShortToast(paramString); 
  }
  
  public static byte[] shortToByteArray(short paramShort) {
    byte[] arrayOfByte = new byte[2];
    for (byte b = 0; b < 2; b++)
      arrayOfByte[b] = (byte)(byte)(paramShort >>> (arrayOfByte.length - 1 - b) * 8 & 0xFF); 
    return arrayOfByte;
  }
  
  public static byte[] subBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    byte[] arrayOfByte = new byte[paramInt2];
    System.arraycopy(paramArrayOfbyte, paramInt1, arrayOfByte, 0, paramInt2);
    return arrayOfByte;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/utils/XlinkUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */