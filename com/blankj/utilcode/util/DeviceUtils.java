package com.blankj.utilcode.util;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

public final class DeviceUtils {
  private DeviceUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  @SuppressLint({"HardwareIds"})
  public static String getAndroidID() {
    return Settings.Secure.getString(Utils.getApp().getContentResolver(), "android_id");
  }
  
  public static String getMacAddress() {
    String str = getMacAddressByWifiInfo();
    if ("02:00:00:00:00:00".equals(str)) {
      String str1 = getMacAddressByNetworkInterface();
      str = str1;
      if ("02:00:00:00:00:00".equals(str1)) {
        str1 = getMacAddressByFile();
        str = str1;
        if ("02:00:00:00:00:00".equals(str1))
          str = "please open wifi"; 
      } 
    } 
    return str;
  }
  
  private static String getMacAddressByFile() {
    ShellUtils.CommandResult commandResult = ShellUtils.execCmd("getprop wifi.interface", false);
    if (commandResult.result == 0) {
      String str = commandResult.successMsg;
      if (str != null) {
        ShellUtils.CommandResult commandResult1 = ShellUtils.execCmd("cat /sys/class/net/" + str + "/address", false);
        if (commandResult1.result == 0 && commandResult1.successMsg != null)
          return commandResult1.successMsg; 
      } 
    } 
    return "02:00:00:00:00:00";
  }
  
  private static String getMacAddressByNetworkInterface() {
    // Byte code:
    //   0: iconst_0
    //   1: istore_0
    //   2: invokestatic getNetworkInterfaces : ()Ljava/util/Enumeration;
    //   5: invokestatic list : (Ljava/util/Enumeration;)Ljava/util/ArrayList;
    //   8: invokeinterface iterator : ()Ljava/util/Iterator;
    //   13: astore_1
    //   14: aload_1
    //   15: invokeinterface hasNext : ()Z
    //   20: ifeq -> 125
    //   23: aload_1
    //   24: invokeinterface next : ()Ljava/lang/Object;
    //   29: checkcast java/net/NetworkInterface
    //   32: astore_2
    //   33: aload_2
    //   34: invokevirtual getName : ()Ljava/lang/String;
    //   37: ldc 'wlan0'
    //   39: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   42: ifeq -> 14
    //   45: aload_2
    //   46: invokevirtual getHardwareAddress : ()[B
    //   49: astore_2
    //   50: aload_2
    //   51: ifnull -> 14
    //   54: aload_2
    //   55: arraylength
    //   56: ifle -> 14
    //   59: new java/lang/StringBuilder
    //   62: astore_1
    //   63: aload_1
    //   64: invokespecial <init> : ()V
    //   67: aload_2
    //   68: arraylength
    //   69: istore_3
    //   70: iload_0
    //   71: iload_3
    //   72: if_icmpge -> 104
    //   75: aload_1
    //   76: ldc '%02x:'
    //   78: iconst_1
    //   79: anewarray java/lang/Object
    //   82: dup
    //   83: iconst_0
    //   84: aload_2
    //   85: iload_0
    //   86: baload
    //   87: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   90: aastore
    //   91: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   94: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   97: pop
    //   98: iinc #0, 1
    //   101: goto -> 70
    //   104: aload_1
    //   105: aload_1
    //   106: invokevirtual length : ()I
    //   109: iconst_1
    //   110: isub
    //   111: invokevirtual deleteCharAt : (I)Ljava/lang/StringBuilder;
    //   114: invokevirtual toString : ()Ljava/lang/String;
    //   117: astore_2
    //   118: aload_2
    //   119: areturn
    //   120: astore_2
    //   121: aload_2
    //   122: invokevirtual printStackTrace : ()V
    //   125: ldc '02:00:00:00:00:00'
    //   127: astore_2
    //   128: goto -> 118
    // Exception table:
    //   from	to	target	type
    //   2	14	120	java/lang/Exception
    //   14	50	120	java/lang/Exception
    //   54	70	120	java/lang/Exception
    //   75	98	120	java/lang/Exception
    //   104	118	120	java/lang/Exception
  }
  
  @SuppressLint({"HardwareIds"})
  private static String getMacAddressByWifiInfo() {
    try {
      WifiManager wifiManager = (WifiManager)Utils.getApp().getSystemService("wifi");
      if (wifiManager != null) {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo != null)
          return wifiInfo.getMacAddress(); 
      } 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    return "02:00:00:00:00:00";
  }
  
  public static String getManufacturer() {
    return Build.MANUFACTURER;
  }
  
  public static String getModel() {
    null = Build.MODEL;
    return (null != null) ? null.trim().replaceAll("\\s*", "") : "";
  }
  
  public static int getSDKVersion() {
    return Build.VERSION.SDK_INT;
  }
  
  public static boolean isDeviceRooted() {
    // Byte code:
    //   0: iconst_1
    //   1: istore_0
    //   2: bipush #8
    //   4: anewarray java/lang/String
    //   7: astore_1
    //   8: aload_1
    //   9: iconst_0
    //   10: ldc '/system/bin/'
    //   12: aastore
    //   13: aload_1
    //   14: iconst_1
    //   15: ldc '/system/xbin/'
    //   17: aastore
    //   18: aload_1
    //   19: iconst_2
    //   20: ldc '/sbin/'
    //   22: aastore
    //   23: aload_1
    //   24: iconst_3
    //   25: ldc '/system/sd/xbin/'
    //   27: aastore
    //   28: aload_1
    //   29: iconst_4
    //   30: ldc '/system/bin/failsafe/'
    //   32: aastore
    //   33: aload_1
    //   34: iconst_5
    //   35: ldc '/data/local/xbin/'
    //   37: aastore
    //   38: aload_1
    //   39: bipush #6
    //   41: ldc '/data/local/bin/'
    //   43: aastore
    //   44: aload_1
    //   45: bipush #7
    //   47: ldc '/data/local/'
    //   49: aastore
    //   50: aload_1
    //   51: arraylength
    //   52: istore_2
    //   53: iconst_0
    //   54: istore_3
    //   55: iload_3
    //   56: iload_2
    //   57: if_icmpge -> 106
    //   60: aload_1
    //   61: iload_3
    //   62: aaload
    //   63: astore #4
    //   65: new java/io/File
    //   68: dup
    //   69: new java/lang/StringBuilder
    //   72: dup
    //   73: invokespecial <init> : ()V
    //   76: aload #4
    //   78: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   81: ldc 'su'
    //   83: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   86: invokevirtual toString : ()Ljava/lang/String;
    //   89: invokespecial <init> : (Ljava/lang/String;)V
    //   92: invokevirtual exists : ()Z
    //   95: ifeq -> 100
    //   98: iload_0
    //   99: ireturn
    //   100: iinc #3, 1
    //   103: goto -> 55
    //   106: iconst_0
    //   107: istore_0
    //   108: goto -> 98
  }
  
  public static void reboot() {
    ShellUtils.execCmd("reboot", true);
    Intent intent = new Intent("android.intent.action.REBOOT");
    intent.putExtra("nowait", 1);
    intent.putExtra("interval", 1);
    intent.putExtra("window", 0);
    Utils.getApp().sendBroadcast(intent);
  }
  
  public static void reboot(String paramString) {
    PowerManager powerManager = (PowerManager)Utils.getApp().getSystemService("power");
    try {
      powerManager.reboot(paramString);
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public static void reboot2Bootloader() {
    ShellUtils.execCmd("reboot bootloader", true);
  }
  
  public static void reboot2Recovery() {
    ShellUtils.execCmd("reboot recovery", true);
  }
  
  public static void shutdown() {
    ShellUtils.execCmd("reboot -p", true);
    Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
    intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
    intent.setFlags(268435456);
    Utils.getApp().startActivity(intent);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/DeviceUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */