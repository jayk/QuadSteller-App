package com.blankj.utilcode.util;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public final class NetworkUtils {
  private static final int NETWORK_TYPE_GSM = 16;
  
  private static final int NETWORK_TYPE_IWLAN = 18;
  
  private static final int NETWORK_TYPE_TD_SCDMA = 17;
  
  private NetworkUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  private static NetworkInfo getActiveNetworkInfo() {
    return ((ConnectivityManager)Utils.getApp().getSystemService("connectivity")).getActiveNetworkInfo();
  }
  
  public static boolean getDataEnabled() {
    try {
      TelephonyManager telephonyManager = (TelephonyManager)Utils.getApp().getSystemService("phone");
      Method method = telephonyManager.getClass().getDeclaredMethod("getDataEnabled", new Class[0]);
      if (method != null)
        return ((Boolean)method.invoke(telephonyManager, new Object[0])).booleanValue(); 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    return false;
  }
  
  public static String getDomainAddress(String paramString) {
    try {
      paramString = InetAddress.getByName(paramString).getHostAddress();
    } catch (UnknownHostException unknownHostException) {
      unknownHostException.printStackTrace();
      unknownHostException = null;
    } 
    return (String)unknownHostException;
  }
  
  public static String getIPAddress(boolean paramBoolean) {
    try {
      Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
      while (enumeration.hasMoreElements()) {
        NetworkInterface networkInterface = enumeration.nextElement();
        if (networkInterface.isUp()) {
          Enumeration<InetAddress> enumeration1 = networkInterface.getInetAddresses();
          while (enumeration1.hasMoreElements()) {
            InetAddress inetAddress = enumeration1.nextElement();
            if (!inetAddress.isLoopbackAddress()) {
              int i;
              String str = inetAddress.getHostAddress();
              if (str.indexOf(':') < 0) {
                i = 1;
              } else {
                i = 0;
              } 
              if (paramBoolean) {
                if (i)
                  return str; 
                continue;
              } 
              if (!i) {
                i = str.indexOf('%');
                return (i < 0) ? str.toUpperCase() : str.substring(0, i).toUpperCase();
              } 
            } 
          } 
        } 
      } 
    } catch (SocketException socketException) {
      socketException.printStackTrace();
    } 
    return null;
  }
  
  public static String getNetworkOperatorName() {
    null = (TelephonyManager)Utils.getApp().getSystemService("phone");
    return (null != null) ? null.getNetworkOperatorName() : null;
  }
  
  public static NetworkType getNetworkType() {
    NetworkType networkType = NetworkType.NETWORK_NO;
    NetworkInfo networkInfo = getActiveNetworkInfo();
    null = networkType;
    if (networkInfo != null) {
      null = networkType;
      if (networkInfo.isAvailable()) {
        if (networkInfo.getType() == 1)
          return NetworkType.NETWORK_WIFI; 
      } else {
        return null;
      } 
    } else {
      return null;
    } 
    if (networkInfo.getType() == 0) {
      String str;
      switch (networkInfo.getSubtype()) {
        default:
          str = networkInfo.getSubtypeName();
          if (str.equalsIgnoreCase("TD-SCDMA") || str.equalsIgnoreCase("WCDMA") || str.equalsIgnoreCase("CDMA2000"))
            return NetworkType.NETWORK_3G; 
          break;
        case 1:
        case 2:
        case 4:
        case 7:
        case 11:
        case 16:
          return NetworkType.NETWORK_2G;
        case 3:
        case 5:
        case 6:
        case 8:
        case 9:
        case 10:
        case 12:
        case 14:
        case 15:
        case 17:
          return NetworkType.NETWORK_3G;
        case 13:
        case 18:
          return NetworkType.NETWORK_4G;
      } 
      return NetworkType.NETWORK_UNKNOWN;
    } 
    return NetworkType.NETWORK_UNKNOWN;
  }
  
  public static boolean getWifiEnabled() {
    return ((WifiManager)Utils.getApp().getSystemService("wifi")).isWifiEnabled();
  }
  
  public static boolean is4G() {
    NetworkInfo networkInfo = getActiveNetworkInfo();
    return (networkInfo != null && networkInfo.isAvailable() && networkInfo.getSubtype() == 13);
  }
  
  public static boolean isAvailableByPing() {
    return isAvailableByPing(null);
  }
  
  public static boolean isAvailableByPing(String paramString) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_1
    //   2: aload_0
    //   3: ifnull -> 15
    //   6: aload_0
    //   7: astore_2
    //   8: aload_0
    //   9: invokevirtual length : ()I
    //   12: ifgt -> 18
    //   15: ldc '223.5.5.5'
    //   17: astore_2
    //   18: ldc 'ping -c 1 %s'
    //   20: iconst_1
    //   21: anewarray java/lang/Object
    //   24: dup
    //   25: iconst_0
    //   26: aload_2
    //   27: aastore
    //   28: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   31: iconst_0
    //   32: invokestatic execCmd : (Ljava/lang/String;Z)Lcom/blankj/utilcode/util/ShellUtils$CommandResult;
    //   35: astore_0
    //   36: aload_0
    //   37: getfield result : I
    //   40: ifne -> 115
    //   43: aload_0
    //   44: getfield errorMsg : Ljava/lang/String;
    //   47: ifnull -> 78
    //   50: ldc 'NetworkUtils'
    //   52: new java/lang/StringBuilder
    //   55: dup
    //   56: invokespecial <init> : ()V
    //   59: ldc 'isAvailableByPing() called'
    //   61: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   64: aload_0
    //   65: getfield errorMsg : Ljava/lang/String;
    //   68: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   71: invokevirtual toString : ()Ljava/lang/String;
    //   74: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   77: pop
    //   78: aload_0
    //   79: getfield successMsg : Ljava/lang/String;
    //   82: ifnull -> 113
    //   85: ldc 'NetworkUtils'
    //   87: new java/lang/StringBuilder
    //   90: dup
    //   91: invokespecial <init> : ()V
    //   94: ldc 'isAvailableByPing() called'
    //   96: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   99: aload_0
    //   100: getfield successMsg : Ljava/lang/String;
    //   103: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   106: invokevirtual toString : ()Ljava/lang/String;
    //   109: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   112: pop
    //   113: iload_1
    //   114: ireturn
    //   115: iconst_0
    //   116: istore_1
    //   117: goto -> 43
  }
  
  public static boolean isConnected() {
    NetworkInfo networkInfo = getActiveNetworkInfo();
    return (networkInfo != null && networkInfo.isConnected());
  }
  
  public static boolean isWifiAvailable() {
    return (getWifiEnabled() && isAvailableByPing());
  }
  
  public static boolean isWifiConnected() {
    boolean bool = true;
    ConnectivityManager connectivityManager = (ConnectivityManager)Utils.getApp().getSystemService("connectivity");
    if (connectivityManager == null || connectivityManager.getActiveNetworkInfo() == null || connectivityManager.getActiveNetworkInfo().getType() != 1)
      bool = false; 
    return bool;
  }
  
  public static void openWirelessSettings() {
    Utils.getApp().startActivity((new Intent("android.settings.WIRELESS_SETTINGS")).setFlags(268435456));
  }
  
  public static void setDataEnabled(boolean paramBoolean) {
    try {
      TelephonyManager telephonyManager = (TelephonyManager)Utils.getApp().getSystemService("phone");
      Method method = telephonyManager.getClass().getDeclaredMethod("setDataEnabled", new Class[] { boolean.class });
      if (method != null)
        method.invoke(telephonyManager, new Object[] { Boolean.valueOf(paramBoolean) }); 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public static void setWifiEnabled(boolean paramBoolean) {
    WifiManager wifiManager = (WifiManager)Utils.getApp().getSystemService("wifi");
    if (paramBoolean) {
      if (!wifiManager.isWifiEnabled())
        wifiManager.setWifiEnabled(true); 
      return;
    } 
    if (wifiManager.isWifiEnabled())
      wifiManager.setWifiEnabled(false); 
  }
  
  public enum NetworkType {
    NETWORK_2G, NETWORK_3G, NETWORK_4G, NETWORK_NO, NETWORK_UNKNOWN, NETWORK_WIFI;
    
    static {
      NETWORK_3G = new NetworkType("NETWORK_3G", 2);
      NETWORK_2G = new NetworkType("NETWORK_2G", 3);
      NETWORK_UNKNOWN = new NetworkType("NETWORK_UNKNOWN", 4);
      NETWORK_NO = new NetworkType("NETWORK_NO", 5);
      $VALUES = new NetworkType[] { NETWORK_WIFI, NETWORK_4G, NETWORK_3G, NETWORK_2G, NETWORK_UNKNOWN, NETWORK_NO };
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/NetworkUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */