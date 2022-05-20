package com.tencent.bugly.crashreport.common.info;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.io.File;
import java.util.Locale;

public final class b {
  private static String a = null;
  
  private static String b = null;
  
  public static String a() {
    try {
      String str = Build.MODEL;
    } catch (Throwable throwable) {}
    return (String)throwable;
  }
  
  public static String a(Context paramContext) {
    Context context = null;
    if (paramContext == null)
      return (String)context; 
    if (!AppInfo.a(paramContext, "android.permission.READ_PHONE_STATE")) {
      x.d("no READ_PHONE_STATE permission to get IMEI", new Object[0]);
      return (String)context;
    } 
    try {
      TelephonyManager telephonyManager = (TelephonyManager)paramContext.getSystemService("phone");
      if (telephonyManager != null) {
        String str2 = telephonyManager.getDeviceId();
        String str1 = str2;
        if (str2 != null)
          try {
            str1 = str2.toLowerCase();
          } catch (Throwable throwable) {
            str1 = str2;
          }  
        return str1;
      } 
    } catch (Throwable throwable) {
      throwable = null;
      x.a("Failed to get IMEI.", new Object[0]);
      return (String)throwable;
    } 
    return null;
  }
  
  public static String a(boolean paramBoolean) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: iload_0
    //   3: ifeq -> 10
    //   6: invokestatic q : ()Ljava/lang/String;
    //   9: astore_1
    //   10: aload_1
    //   11: astore_2
    //   12: aload_1
    //   13: ifnonnull -> 22
    //   16: ldc 'os.arch'
    //   18: invokestatic getProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   21: astore_2
    //   22: new java/lang/StringBuilder
    //   25: astore_1
    //   26: aload_1
    //   27: invokespecial <init> : ()V
    //   30: aload_1
    //   31: aload_2
    //   32: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   35: invokevirtual toString : ()Ljava/lang/String;
    //   38: astore_1
    //   39: aload_1
    //   40: areturn
    //   41: astore_1
    //   42: aload_1
    //   43: invokestatic a : (Ljava/lang/Throwable;)Z
    //   46: ifne -> 53
    //   49: aload_1
    //   50: invokevirtual printStackTrace : ()V
    //   53: ldc 'fail'
    //   55: astore_1
    //   56: goto -> 39
    // Exception table:
    //   from	to	target	type
    //   6	10	41	java/lang/Throwable
    //   16	22	41	java/lang/Throwable
    //   22	39	41	java/lang/Throwable
  }
  
  public static String b() {
    try {
      String str = Build.VERSION.RELEASE;
    } catch (Throwable throwable) {}
    return (String)throwable;
  }
  
  public static String b(Context paramContext) {
    Context context = null;
    if (paramContext == null)
      return (String)context; 
    if (!AppInfo.a(paramContext, "android.permission.READ_PHONE_STATE")) {
      x.d("no READ_PHONE_STATE permission to get IMSI", new Object[0]);
      return (String)context;
    } 
    try {
      TelephonyManager telephonyManager = (TelephonyManager)paramContext.getSystemService("phone");
      if (telephonyManager != null) {
        String str2 = telephonyManager.getSubscriberId();
        String str1 = str2;
        if (str2 != null)
          try {
            str1 = str2.toLowerCase();
          } catch (Throwable throwable) {
            str1 = str2;
          }  
        return str1;
      } 
    } catch (Throwable throwable) {
      throwable = null;
      x.a("Failed to get IMSI.", new Object[0]);
      return (String)throwable;
    } 
    return null;
  }
  
  public static int c() {
    int i;
    try {
      i = Build.VERSION.SDK_INT;
    } catch (Throwable throwable) {}
    return i;
  }
  
  public static String c(Context paramContext) {
    String str1;
    String str2 = "fail";
    if (paramContext == null)
      return str2; 
    try {
      str1 = Settings.Secure.getString(paramContext.getContentResolver(), "android_id");
      if (str1 == null)
        return "null"; 
      try {
        str2 = str1.toLowerCase();
        str1 = str2;
      } catch (Throwable throwable) {
        str2 = str1;
      } 
    } catch (Throwable throwable) {}
    return str1;
  }
  
  public static String d() {
    String str;
    try {
      str = Build.SERIAL;
    } catch (Throwable throwable) {
      x.a("Failed to get hardware serial number.", new Object[0]);
      str = "fail";
    } 
    return str;
  }
  
  public static String d(Context paramContext) {
    // Byte code:
    //   0: ldc 'fail'
    //   2: astore_1
    //   3: aload_0
    //   4: ifnonnull -> 9
    //   7: aload_1
    //   8: areturn
    //   9: aload_0
    //   10: ldc 'wifi'
    //   12: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   15: checkcast android/net/wifi/WifiManager
    //   18: astore_2
    //   19: aload_1
    //   20: astore_3
    //   21: aload_2
    //   22: ifnull -> 167
    //   25: aload_2
    //   26: invokevirtual getConnectionInfo : ()Landroid/net/wifi/WifiInfo;
    //   29: astore_2
    //   30: aload_1
    //   31: astore_3
    //   32: aload_2
    //   33: ifnull -> 167
    //   36: aload_2
    //   37: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   40: astore_3
    //   41: aload_3
    //   42: ifnull -> 56
    //   45: aload_3
    //   46: astore_1
    //   47: aload_3
    //   48: ldc '02:00:00:00:00:00'
    //   50: invokevirtual equals : (Ljava/lang/Object;)Z
    //   53: ifeq -> 120
    //   56: aload_0
    //   57: ldc 'wifi.interface'
    //   59: invokestatic a : (Landroid/content/Context;Ljava/lang/String;)Ljava/lang/String;
    //   62: astore_0
    //   63: ldc 'MAC interface: %s'
    //   65: iconst_1
    //   66: anewarray java/lang/Object
    //   69: dup
    //   70: iconst_0
    //   71: aload_0
    //   72: aastore
    //   73: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   76: pop
    //   77: aload_0
    //   78: invokestatic getByName : (Ljava/lang/String;)Ljava/net/NetworkInterface;
    //   81: astore_1
    //   82: aload_1
    //   83: astore_0
    //   84: aload_1
    //   85: ifnonnull -> 94
    //   88: ldc 'wlan0'
    //   90: invokestatic getByName : (Ljava/lang/String;)Ljava/net/NetworkInterface;
    //   93: astore_0
    //   94: aload_0
    //   95: astore_2
    //   96: aload_0
    //   97: ifnonnull -> 106
    //   100: ldc 'eth0'
    //   102: invokestatic getByName : (Ljava/lang/String;)Ljava/net/NetworkInterface;
    //   105: astore_2
    //   106: aload_3
    //   107: astore_1
    //   108: aload_2
    //   109: ifnull -> 120
    //   112: aload_2
    //   113: invokevirtual getHardwareAddress : ()[B
    //   116: invokestatic d : ([B)Ljava/lang/String;
    //   119: astore_1
    //   120: aload_1
    //   121: astore_0
    //   122: aload_1
    //   123: ifnonnull -> 129
    //   126: ldc 'null'
    //   128: astore_0
    //   129: ldc 'MAC address: %s'
    //   131: iconst_1
    //   132: anewarray java/lang/Object
    //   135: dup
    //   136: iconst_0
    //   137: aload_0
    //   138: aastore
    //   139: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   142: pop
    //   143: aload_0
    //   144: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   147: astore_1
    //   148: goto -> 7
    //   151: astore_0
    //   152: aload_1
    //   153: astore_3
    //   154: aload_0
    //   155: invokestatic a : (Ljava/lang/Throwable;)Z
    //   158: ifne -> 167
    //   161: aload_0
    //   162: invokevirtual printStackTrace : ()V
    //   165: aload_1
    //   166: astore_3
    //   167: aload_3
    //   168: astore_1
    //   169: goto -> 120
    //   172: astore_0
    //   173: aload_3
    //   174: astore_1
    //   175: goto -> 152
    // Exception table:
    //   from	to	target	type
    //   9	19	151	java/lang/Throwable
    //   25	30	151	java/lang/Throwable
    //   36	41	151	java/lang/Throwable
    //   47	56	172	java/lang/Throwable
    //   56	82	172	java/lang/Throwable
    //   88	94	172	java/lang/Throwable
    //   100	106	172	java/lang/Throwable
    //   112	120	172	java/lang/Throwable
  }
  
  public static long e() {
    long l2;
    long l1 = -1L;
    try {
      File file = Environment.getDataDirectory();
      StatFs statFs = new StatFs();
      this(file.getPath());
      l2 = statFs.getBlockSize();
      int i = statFs.getBlockCount();
      l2 = i * l2;
    } catch (Throwable throwable) {
      l2 = l1;
    } 
    return l2;
  }
  
  public static String e(Context paramContext) {
    String str = "fail";
    if (paramContext == null)
      return str; 
    try {
      TelephonyManager telephonyManager = (TelephonyManager)paramContext.getSystemService("phone");
      if (telephonyManager != null) {
        str = telephonyManager.getSimSerialNumber();
        String str1 = str;
        if (str == null)
          str1 = "null"; 
        return str1;
      } 
    } catch (Throwable throwable) {
      String str1 = "fail";
      x.a("Failed to get SIM serial number.", new Object[0]);
      return str1;
    } 
    return "fail";
  }
  
  public static long f() {
    long l2;
    long l1 = -1L;
    try {
      File file = Environment.getDataDirectory();
      StatFs statFs = new StatFs();
      this(file.getPath());
      l2 = statFs.getBlockSize();
      int i = statFs.getAvailableBlocks();
      l2 = i * l2;
    } catch (Throwable throwable) {
      l2 = l1;
    } 
    return l2;
  }
  
  public static String f(Context paramContext) {
    String str = "unknown";
    try {
      NetworkInfo networkInfo = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
      if (networkInfo == null)
        return null; 
      if (networkInfo.getType() == 1)
        return "WIFI"; 
      if (networkInfo.getType() == 0) {
        TelephonyManager telephonyManager = (TelephonyManager)paramContext.getSystemService("phone");
        if (telephonyManager != null) {
          StringBuilder stringBuilder;
          int i = telephonyManager.getNetworkType();
          switch (i) {
            default:
              stringBuilder = new StringBuilder();
              this("MOBILE(");
              return stringBuilder.append(i).append(")").toString();
            case 1:
              return "GPRS";
            case 2:
              return "EDGE";
            case 3:
              return "UMTS";
            case 8:
              return "HSDPA";
            case 9:
              return "HSUPA";
            case 10:
              return "HSPA";
            case 4:
              return "CDMA";
            case 5:
              return "EVDO_0";
            case 6:
              return "EVDO_A";
            case 7:
              return "1xRTT";
            case 11:
              return "iDen";
            case 12:
              return "EVDO_B";
            case 13:
              return "LTE";
            case 14:
              return "eHRPD";
            case 15:
              break;
          } 
          return "HSPA+";
        } 
      } 
    } catch (Exception exception) {
      String str1 = str;
      if (!x.a(exception)) {
        exception.printStackTrace();
        str1 = str;
      } 
      return str1;
    } 
    return "unknown";
  }
  
  public static long g() {
    // Byte code:
    //   0: aconst_null
    //   1: astore_0
    //   2: new java/io/FileReader
    //   5: astore_1
    //   6: aload_1
    //   7: ldc_w '/proc/meminfo'
    //   10: invokespecial <init> : (Ljava/lang/String;)V
    //   13: new java/io/BufferedReader
    //   16: astore_2
    //   17: aload_2
    //   18: aload_1
    //   19: sipush #2048
    //   22: invokespecial <init> : (Ljava/io/Reader;I)V
    //   25: aload_2
    //   26: invokevirtual readLine : ()Ljava/lang/String;
    //   29: astore_3
    //   30: aload_3
    //   31: ifnonnull -> 80
    //   34: aload_2
    //   35: invokevirtual close : ()V
    //   38: aload_1
    //   39: invokevirtual close : ()V
    //   42: ldc2_w -1
    //   45: lstore #4
    //   47: lload #4
    //   49: lreturn
    //   50: astore_3
    //   51: aload_3
    //   52: invokestatic a : (Ljava/lang/Throwable;)Z
    //   55: ifne -> 38
    //   58: aload_3
    //   59: invokevirtual printStackTrace : ()V
    //   62: goto -> 38
    //   65: astore_3
    //   66: aload_3
    //   67: invokestatic a : (Ljava/lang/Throwable;)Z
    //   70: ifne -> 42
    //   73: aload_3
    //   74: invokevirtual printStackTrace : ()V
    //   77: goto -> 42
    //   80: aload_3
    //   81: ldc_w ':\s+'
    //   84: iconst_2
    //   85: invokevirtual split : (Ljava/lang/String;I)[Ljava/lang/String;
    //   88: iconst_1
    //   89: aaload
    //   90: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   93: ldc_w 'kb'
    //   96: ldc_w ''
    //   99: invokevirtual replace : (Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   102: invokevirtual trim : ()Ljava/lang/String;
    //   105: invokestatic parseLong : (Ljava/lang/String;)J
    //   108: lstore #4
    //   110: lload #4
    //   112: bipush #10
    //   114: lshl
    //   115: lstore #6
    //   117: aload_2
    //   118: invokevirtual close : ()V
    //   121: aload_1
    //   122: invokevirtual close : ()V
    //   125: lload #6
    //   127: lstore #4
    //   129: goto -> 47
    //   132: astore_3
    //   133: lload #6
    //   135: lstore #4
    //   137: aload_3
    //   138: invokestatic a : (Ljava/lang/Throwable;)Z
    //   141: ifne -> 47
    //   144: aload_3
    //   145: invokevirtual printStackTrace : ()V
    //   148: lload #6
    //   150: lstore #4
    //   152: goto -> 47
    //   155: astore_3
    //   156: aload_3
    //   157: invokestatic a : (Ljava/lang/Throwable;)Z
    //   160: ifne -> 121
    //   163: aload_3
    //   164: invokevirtual printStackTrace : ()V
    //   167: goto -> 121
    //   170: astore_1
    //   171: aconst_null
    //   172: astore_3
    //   173: aload_0
    //   174: astore_2
    //   175: aload_1
    //   176: invokestatic a : (Ljava/lang/Throwable;)Z
    //   179: ifne -> 186
    //   182: aload_1
    //   183: invokevirtual printStackTrace : ()V
    //   186: aload_2
    //   187: ifnull -> 194
    //   190: aload_2
    //   191: invokevirtual close : ()V
    //   194: aload_3
    //   195: ifnull -> 202
    //   198: aload_3
    //   199: invokevirtual close : ()V
    //   202: ldc2_w -2
    //   205: lstore #4
    //   207: goto -> 47
    //   210: astore_2
    //   211: aload_2
    //   212: invokestatic a : (Ljava/lang/Throwable;)Z
    //   215: ifne -> 194
    //   218: aload_2
    //   219: invokevirtual printStackTrace : ()V
    //   222: goto -> 194
    //   225: astore_3
    //   226: aload_3
    //   227: invokestatic a : (Ljava/lang/Throwable;)Z
    //   230: ifne -> 202
    //   233: aload_3
    //   234: invokevirtual printStackTrace : ()V
    //   237: goto -> 202
    //   240: astore_3
    //   241: aconst_null
    //   242: astore_2
    //   243: aconst_null
    //   244: astore_1
    //   245: aload_2
    //   246: ifnull -> 253
    //   249: aload_2
    //   250: invokevirtual close : ()V
    //   253: aload_1
    //   254: ifnull -> 261
    //   257: aload_1
    //   258: invokevirtual close : ()V
    //   261: aload_3
    //   262: athrow
    //   263: astore_2
    //   264: aload_2
    //   265: invokestatic a : (Ljava/lang/Throwable;)Z
    //   268: ifne -> 253
    //   271: aload_2
    //   272: invokevirtual printStackTrace : ()V
    //   275: goto -> 253
    //   278: astore_2
    //   279: aload_2
    //   280: invokestatic a : (Ljava/lang/Throwable;)Z
    //   283: ifne -> 261
    //   286: aload_2
    //   287: invokevirtual printStackTrace : ()V
    //   290: goto -> 261
    //   293: astore_3
    //   294: aconst_null
    //   295: astore_2
    //   296: goto -> 245
    //   299: astore_3
    //   300: goto -> 245
    //   303: astore_0
    //   304: aload_3
    //   305: astore_1
    //   306: aload_0
    //   307: astore_3
    //   308: goto -> 245
    //   311: astore_2
    //   312: aload_1
    //   313: astore_3
    //   314: aload_2
    //   315: astore_1
    //   316: aload_0
    //   317: astore_2
    //   318: goto -> 175
    //   321: astore_0
    //   322: aload_1
    //   323: astore_3
    //   324: aload_0
    //   325: astore_1
    //   326: goto -> 175
    // Exception table:
    //   from	to	target	type
    //   2	13	170	java/lang/Throwable
    //   2	13	240	finally
    //   13	25	311	java/lang/Throwable
    //   13	25	293	finally
    //   25	30	321	java/lang/Throwable
    //   25	30	299	finally
    //   34	38	50	java/io/IOException
    //   38	42	65	java/io/IOException
    //   80	110	321	java/lang/Throwable
    //   80	110	299	finally
    //   117	121	155	java/io/IOException
    //   121	125	132	java/io/IOException
    //   175	186	303	finally
    //   190	194	210	java/io/IOException
    //   198	202	225	java/io/IOException
    //   249	253	263	java/io/IOException
    //   257	261	278	java/io/IOException
  }
  
  public static String g(Context paramContext) {
    String str = z.a(paramContext, "ro.miui.ui.version.name");
    if (!z.a(str) && !str.equals("fail"))
      return "XiaoMi/MIUI/" + str; 
    str = z.a(paramContext, "ro.build.version.emui");
    if (!z.a(str) && !str.equals("fail"))
      return "HuaWei/EMOTION/" + str; 
    str = z.a(paramContext, "ro.lenovo.series");
    if (!z.a(str) && !str.equals("fail")) {
      null = z.a(paramContext, "ro.build.version.incremental");
      return "Lenovo/VIBE/" + null;
    } 
    str = z.a(paramContext, "ro.build.nubia.rom.name");
    if (!z.a(str) && !str.equals("fail"))
      return "Zte/NUBIA/" + str + "_" + z.a(paramContext, "ro.build.nubia.rom.code"); 
    str = z.a(paramContext, "ro.meizu.product.model");
    if (!z.a(str) && !str.equals("fail"))
      return "Meizu/FLYME/" + z.a(paramContext, "ro.build.display.id"); 
    str = z.a(paramContext, "ro.build.version.opporom");
    if (!z.a(str) && !str.equals("fail"))
      return "Oppo/COLOROS/" + str; 
    str = z.a(paramContext, "ro.vivo.os.build.display.id");
    if (!z.a(str) && !str.equals("fail"))
      return "vivo/FUNTOUCH/" + str; 
    str = z.a(paramContext, "ro.aa.romver");
    if (!z.a(str) && !str.equals("fail"))
      return "htc/" + str + "/" + z.a(paramContext, "ro.build.description"); 
    str = z.a(paramContext, "ro.lewa.version");
    if (!z.a(str) && !str.equals("fail"))
      return "tcl/" + str + "/" + z.a(paramContext, "ro.build.display.id"); 
    str = z.a(paramContext, "ro.gn.gnromvernumber");
    if (!z.a(str) && !str.equals("fail"))
      return "amigo/" + str + "/" + z.a(paramContext, "ro.build.display.id"); 
    str = z.a(paramContext, "ro.build.tyd.kbstyle_version");
    return (!z.a(str) && !str.equals("fail")) ? ("dido/" + str) : (z.a(paramContext, "ro.build.fingerprint") + "/" + z.a(paramContext, "ro.build.rom.id"));
  }
  
  public static long h() {
    // Byte code:
    //   0: aconst_null
    //   1: astore_0
    //   2: ldc2_w -1
    //   5: lstore_1
    //   6: new java/io/FileReader
    //   9: astore_3
    //   10: aload_3
    //   11: ldc_w '/proc/meminfo'
    //   14: invokespecial <init> : (Ljava/lang/String;)V
    //   17: new java/io/BufferedReader
    //   20: astore #4
    //   22: aload #4
    //   24: aload_3
    //   25: sipush #2048
    //   28: invokespecial <init> : (Ljava/io/Reader;I)V
    //   31: aload #4
    //   33: invokevirtual readLine : ()Ljava/lang/String;
    //   36: pop
    //   37: aload #4
    //   39: invokevirtual readLine : ()Ljava/lang/String;
    //   42: astore_0
    //   43: aload_0
    //   44: ifnonnull -> 98
    //   47: aload #4
    //   49: invokevirtual close : ()V
    //   52: aload_3
    //   53: invokevirtual close : ()V
    //   56: lload_1
    //   57: lstore #5
    //   59: lload #5
    //   61: lreturn
    //   62: astore_0
    //   63: aload_0
    //   64: invokestatic a : (Ljava/lang/Throwable;)Z
    //   67: ifne -> 52
    //   70: aload_0
    //   71: invokevirtual printStackTrace : ()V
    //   74: goto -> 52
    //   77: astore_0
    //   78: lload_1
    //   79: lstore #5
    //   81: aload_0
    //   82: invokestatic a : (Ljava/lang/Throwable;)Z
    //   85: ifne -> 59
    //   88: aload_0
    //   89: invokevirtual printStackTrace : ()V
    //   92: lload_1
    //   93: lstore #5
    //   95: goto -> 59
    //   98: aload_0
    //   99: ldc_w ':\s+'
    //   102: iconst_2
    //   103: invokevirtual split : (Ljava/lang/String;I)[Ljava/lang/String;
    //   106: iconst_1
    //   107: aaload
    //   108: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   111: ldc_w 'kb'
    //   114: ldc_w ''
    //   117: invokevirtual replace : (Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   120: invokevirtual trim : ()Ljava/lang/String;
    //   123: invokestatic parseLong : (Ljava/lang/String;)J
    //   126: lstore #5
    //   128: aload #4
    //   130: invokevirtual readLine : ()Ljava/lang/String;
    //   133: astore_0
    //   134: aload_0
    //   135: ifnonnull -> 189
    //   138: aload #4
    //   140: invokevirtual close : ()V
    //   143: aload_3
    //   144: invokevirtual close : ()V
    //   147: lload_1
    //   148: lstore #5
    //   150: goto -> 59
    //   153: astore_0
    //   154: lload_1
    //   155: lstore #5
    //   157: aload_0
    //   158: invokestatic a : (Ljava/lang/Throwable;)Z
    //   161: ifne -> 59
    //   164: aload_0
    //   165: invokevirtual printStackTrace : ()V
    //   168: lload_1
    //   169: lstore #5
    //   171: goto -> 59
    //   174: astore_0
    //   175: aload_0
    //   176: invokestatic a : (Ljava/lang/Throwable;)Z
    //   179: ifne -> 143
    //   182: aload_0
    //   183: invokevirtual printStackTrace : ()V
    //   186: goto -> 143
    //   189: aload_0
    //   190: ldc_w ':\s+'
    //   193: iconst_2
    //   194: invokevirtual split : (Ljava/lang/String;I)[Ljava/lang/String;
    //   197: iconst_1
    //   198: aaload
    //   199: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   202: ldc_w 'kb'
    //   205: ldc_w ''
    //   208: invokevirtual replace : (Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   211: invokevirtual trim : ()Ljava/lang/String;
    //   214: invokestatic parseLong : (Ljava/lang/String;)J
    //   217: lstore #7
    //   219: aload #4
    //   221: invokevirtual readLine : ()Ljava/lang/String;
    //   224: astore_0
    //   225: aload_0
    //   226: ifnonnull -> 280
    //   229: aload #4
    //   231: invokevirtual close : ()V
    //   234: aload_3
    //   235: invokevirtual close : ()V
    //   238: lload_1
    //   239: lstore #5
    //   241: goto -> 59
    //   244: astore_0
    //   245: lload_1
    //   246: lstore #5
    //   248: aload_0
    //   249: invokestatic a : (Ljava/lang/Throwable;)Z
    //   252: ifne -> 59
    //   255: aload_0
    //   256: invokevirtual printStackTrace : ()V
    //   259: lload_1
    //   260: lstore #5
    //   262: goto -> 59
    //   265: astore_0
    //   266: aload_0
    //   267: invokestatic a : (Ljava/lang/Throwable;)Z
    //   270: ifne -> 234
    //   273: aload_0
    //   274: invokevirtual printStackTrace : ()V
    //   277: goto -> 234
    //   280: aload_0
    //   281: ldc_w ':\s+'
    //   284: iconst_2
    //   285: invokevirtual split : (Ljava/lang/String;I)[Ljava/lang/String;
    //   288: iconst_1
    //   289: aaload
    //   290: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   293: ldc_w 'kb'
    //   296: ldc_w ''
    //   299: invokevirtual replace : (Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   302: invokevirtual trim : ()Ljava/lang/String;
    //   305: invokestatic parseLong : (Ljava/lang/String;)J
    //   308: lstore_1
    //   309: lload_1
    //   310: bipush #10
    //   312: lshl
    //   313: lconst_0
    //   314: lload #5
    //   316: bipush #10
    //   318: lshl
    //   319: ladd
    //   320: lload #7
    //   322: bipush #10
    //   324: lshl
    //   325: ladd
    //   326: ladd
    //   327: lstore_1
    //   328: aload #4
    //   330: invokevirtual close : ()V
    //   333: aload_3
    //   334: invokevirtual close : ()V
    //   337: lload_1
    //   338: lstore #5
    //   340: goto -> 59
    //   343: astore_0
    //   344: lload_1
    //   345: lstore #5
    //   347: aload_0
    //   348: invokestatic a : (Ljava/lang/Throwable;)Z
    //   351: ifne -> 59
    //   354: aload_0
    //   355: invokevirtual printStackTrace : ()V
    //   358: lload_1
    //   359: lstore #5
    //   361: goto -> 59
    //   364: astore_0
    //   365: aload_0
    //   366: invokestatic a : (Ljava/lang/Throwable;)Z
    //   369: ifne -> 333
    //   372: aload_0
    //   373: invokevirtual printStackTrace : ()V
    //   376: goto -> 333
    //   379: astore_3
    //   380: aconst_null
    //   381: astore #4
    //   383: aload_3
    //   384: invokestatic a : (Ljava/lang/Throwable;)Z
    //   387: ifne -> 394
    //   390: aload_3
    //   391: invokevirtual printStackTrace : ()V
    //   394: aload #4
    //   396: ifnull -> 404
    //   399: aload #4
    //   401: invokevirtual close : ()V
    //   404: aload_0
    //   405: ifnull -> 412
    //   408: aload_0
    //   409: invokevirtual close : ()V
    //   412: ldc2_w -2
    //   415: lstore #5
    //   417: goto -> 59
    //   420: astore #4
    //   422: aload #4
    //   424: invokestatic a : (Ljava/lang/Throwable;)Z
    //   427: ifne -> 404
    //   430: aload #4
    //   432: invokevirtual printStackTrace : ()V
    //   435: goto -> 404
    //   438: astore_0
    //   439: aload_0
    //   440: invokestatic a : (Ljava/lang/Throwable;)Z
    //   443: ifne -> 412
    //   446: aload_0
    //   447: invokevirtual printStackTrace : ()V
    //   450: goto -> 412
    //   453: astore_0
    //   454: aconst_null
    //   455: astore #4
    //   457: aconst_null
    //   458: astore_3
    //   459: aload #4
    //   461: ifnull -> 469
    //   464: aload #4
    //   466: invokevirtual close : ()V
    //   469: aload_3
    //   470: ifnull -> 477
    //   473: aload_3
    //   474: invokevirtual close : ()V
    //   477: aload_0
    //   478: athrow
    //   479: astore #4
    //   481: aload #4
    //   483: invokestatic a : (Ljava/lang/Throwable;)Z
    //   486: ifne -> 469
    //   489: aload #4
    //   491: invokevirtual printStackTrace : ()V
    //   494: goto -> 469
    //   497: astore #4
    //   499: aload #4
    //   501: invokestatic a : (Ljava/lang/Throwable;)Z
    //   504: ifne -> 477
    //   507: aload #4
    //   509: invokevirtual printStackTrace : ()V
    //   512: goto -> 477
    //   515: astore_0
    //   516: aconst_null
    //   517: astore #4
    //   519: goto -> 459
    //   522: astore_0
    //   523: goto -> 459
    //   526: astore #9
    //   528: aload_0
    //   529: astore_3
    //   530: aload #9
    //   532: astore_0
    //   533: goto -> 459
    //   536: astore #4
    //   538: aconst_null
    //   539: astore #9
    //   541: aload_3
    //   542: astore_0
    //   543: aload #4
    //   545: astore_3
    //   546: aload #9
    //   548: astore #4
    //   550: goto -> 383
    //   553: astore #9
    //   555: aload_3
    //   556: astore_0
    //   557: aload #9
    //   559: astore_3
    //   560: goto -> 383
    // Exception table:
    //   from	to	target	type
    //   6	17	379	java/lang/Throwable
    //   6	17	453	finally
    //   17	31	536	java/lang/Throwable
    //   17	31	515	finally
    //   31	43	553	java/lang/Throwable
    //   31	43	522	finally
    //   47	52	62	java/io/IOException
    //   52	56	77	java/io/IOException
    //   98	134	553	java/lang/Throwable
    //   98	134	522	finally
    //   138	143	174	java/io/IOException
    //   143	147	153	java/io/IOException
    //   189	225	553	java/lang/Throwable
    //   189	225	522	finally
    //   229	234	265	java/io/IOException
    //   234	238	244	java/io/IOException
    //   280	309	553	java/lang/Throwable
    //   280	309	522	finally
    //   328	333	364	java/io/IOException
    //   333	337	343	java/io/IOException
    //   383	394	526	finally
    //   399	404	420	java/io/IOException
    //   408	412	438	java/io/IOException
    //   464	469	479	java/io/IOException
    //   473	477	497	java/io/IOException
  }
  
  public static String h(Context paramContext) {
    return z.a(paramContext, "ro.board.platform");
  }
  
  public static long i() {
    long l;
    if (!p())
      return 0L; 
    try {
      StatFs statFs = new StatFs();
      this(Environment.getExternalStorageDirectory().getPath());
      int i = statFs.getBlockSize();
      int j = statFs.getBlockCount();
      l = j;
      l = i * l;
    } catch (Throwable throwable) {}
    return l;
  }
  
  public static boolean i(Context paramContext) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: new java/io/File
    //   5: astore_2
    //   6: aload_2
    //   7: ldc_w '/system/app/Superuser.apk'
    //   10: invokespecial <init> : (Ljava/lang/String;)V
    //   13: aload_2
    //   14: invokevirtual exists : ()Z
    //   17: istore_3
    //   18: aconst_null
    //   19: astore #4
    //   21: aconst_null
    //   22: astore #5
    //   24: aload_0
    //   25: iconst_3
    //   26: anewarray java/lang/String
    //   29: dup
    //   30: iconst_0
    //   31: ldc_w '/system/bin/sh'
    //   34: aastore
    //   35: dup
    //   36: iconst_1
    //   37: ldc_w '-c'
    //   40: aastore
    //   41: dup
    //   42: iconst_2
    //   43: ldc_w 'type su'
    //   46: aastore
    //   47: invokestatic a : (Landroid/content/Context;[Ljava/lang/String;)Ljava/util/ArrayList;
    //   50: astore_0
    //   51: aload #4
    //   53: astore_2
    //   54: aload_0
    //   55: ifnull -> 153
    //   58: aload #4
    //   60: astore_2
    //   61: aload_0
    //   62: invokevirtual size : ()I
    //   65: ifle -> 153
    //   68: aload_0
    //   69: invokevirtual iterator : ()Ljava/util/Iterator;
    //   72: astore_2
    //   73: aload #5
    //   75: astore_0
    //   76: aload_2
    //   77: invokeinterface hasNext : ()Z
    //   82: ifeq -> 142
    //   85: aload_2
    //   86: invokeinterface next : ()Ljava/lang/Object;
    //   91: checkcast java/lang/String
    //   94: astore #5
    //   96: aload #5
    //   98: iconst_0
    //   99: anewarray java/lang/Object
    //   102: invokestatic c : (Ljava/lang/String;[Ljava/lang/Object;)Z
    //   105: pop
    //   106: aload #5
    //   108: ldc_w 'not found'
    //   111: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   114: ifeq -> 222
    //   117: iconst_0
    //   118: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   121: astore_0
    //   122: goto -> 76
    //   125: astore_2
    //   126: aload_2
    //   127: invokestatic b : (Ljava/lang/Throwable;)Z
    //   130: ifne -> 137
    //   133: aload_2
    //   134: invokevirtual printStackTrace : ()V
    //   137: iconst_0
    //   138: istore_3
    //   139: goto -> 18
    //   142: aload_0
    //   143: astore_2
    //   144: aload_0
    //   145: ifnonnull -> 153
    //   148: iconst_1
    //   149: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   152: astore_2
    //   153: aload_2
    //   154: ifnonnull -> 207
    //   157: iconst_0
    //   158: istore #6
    //   160: getstatic android/os/Build.TAGS : Ljava/lang/String;
    //   163: ifnull -> 216
    //   166: getstatic android/os/Build.TAGS : Ljava/lang/String;
    //   169: ldc_w 'test-keys'
    //   172: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   175: ifeq -> 216
    //   178: iconst_1
    //   179: istore #7
    //   181: iload #7
    //   183: ifne -> 203
    //   186: iload_3
    //   187: ifne -> 203
    //   190: iload_1
    //   191: istore_3
    //   192: iload #6
    //   194: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   197: invokevirtual booleanValue : ()Z
    //   200: ifeq -> 205
    //   203: iconst_1
    //   204: istore_3
    //   205: iload_3
    //   206: ireturn
    //   207: aload_2
    //   208: invokevirtual booleanValue : ()Z
    //   211: istore #6
    //   213: goto -> 160
    //   216: iconst_0
    //   217: istore #7
    //   219: goto -> 181
    //   222: goto -> 122
    // Exception table:
    //   from	to	target	type
    //   2	18	125	java/lang/Throwable
  }
  
  public static long j() {
    long l;
    if (!p())
      return 0L; 
    try {
      StatFs statFs = new StatFs();
      this(Environment.getExternalStorageDirectory().getPath());
      int i = statFs.getBlockSize();
      int j = statFs.getAvailableBlocks();
      l = j;
      l = i * l;
    } catch (Throwable throwable) {}
    return l;
  }
  
  public static String j(Context paramContext) {
    StringBuilder stringBuilder = new StringBuilder();
    String str2 = z.a(paramContext, "ro.genymotion.version");
    if (str2 != null) {
      stringBuilder.append("ro.genymotion.version");
      stringBuilder.append("|");
      stringBuilder.append(str2);
      stringBuilder.append("\n");
    } 
    str2 = z.a(paramContext, "androVM.vbox_dpi");
    if (str2 != null) {
      stringBuilder.append("androVM.vbox_dpi");
      stringBuilder.append("|");
      stringBuilder.append(str2);
      stringBuilder.append("\n");
    } 
    String str1 = z.a(paramContext, "qemu.sf.fake_camera");
    if (str1 != null) {
      stringBuilder.append("qemu.sf.fake_camera");
      stringBuilder.append("|");
      stringBuilder.append(str1);
    } 
    return stringBuilder.toString();
  }
  
  public static String k() {
    String str2;
    String str1 = "fail";
    try {
      str2 = Locale.getDefault().getCountry();
    } catch (Throwable throwable) {
      str2 = str1;
    } 
    return str2;
  }
  
  public static String k(Context paramContext) {
    // Byte code:
    //   0: new java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_1
    //   8: getstatic com/tencent/bugly/crashreport/common/info/b.a : Ljava/lang/String;
    //   11: ifnonnull -> 24
    //   14: aload_0
    //   15: ldc_w 'ro.secure'
    //   18: invokestatic a : (Landroid/content/Context;Ljava/lang/String;)Ljava/lang/String;
    //   21: putstatic com/tencent/bugly/crashreport/common/info/b.a : Ljava/lang/String;
    //   24: getstatic com/tencent/bugly/crashreport/common/info/b.a : Ljava/lang/String;
    //   27: ifnull -> 62
    //   30: aload_1
    //   31: ldc_w 'ro.secure'
    //   34: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   37: pop
    //   38: aload_1
    //   39: ldc_w '|'
    //   42: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   45: pop
    //   46: aload_1
    //   47: getstatic com/tencent/bugly/crashreport/common/info/b.a : Ljava/lang/String;
    //   50: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   53: pop
    //   54: aload_1
    //   55: ldc_w '\\n'
    //   58: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   61: pop
    //   62: getstatic com/tencent/bugly/crashreport/common/info/b.b : Ljava/lang/String;
    //   65: ifnonnull -> 78
    //   68: aload_0
    //   69: ldc_w 'ro.debuggable'
    //   72: invokestatic a : (Landroid/content/Context;Ljava/lang/String;)Ljava/lang/String;
    //   75: putstatic com/tencent/bugly/crashreport/common/info/b.b : Ljava/lang/String;
    //   78: getstatic com/tencent/bugly/crashreport/common/info/b.b : Ljava/lang/String;
    //   81: ifnull -> 116
    //   84: aload_1
    //   85: ldc_w 'ro.debuggable'
    //   88: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   91: pop
    //   92: aload_1
    //   93: ldc_w '|'
    //   96: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   99: pop
    //   100: aload_1
    //   101: getstatic com/tencent/bugly/crashreport/common/info/b.b : Ljava/lang/String;
    //   104: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   107: pop
    //   108: aload_1
    //   109: ldc_w '\\n'
    //   112: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   115: pop
    //   116: new java/io/BufferedReader
    //   119: astore_2
    //   120: new java/io/FileReader
    //   123: astore_0
    //   124: aload_0
    //   125: ldc_w '/proc/self/status'
    //   128: invokespecial <init> : (Ljava/lang/String;)V
    //   131: aload_2
    //   132: aload_0
    //   133: invokespecial <init> : (Ljava/io/Reader;)V
    //   136: aload_2
    //   137: astore_0
    //   138: aload_2
    //   139: invokevirtual readLine : ()Ljava/lang/String;
    //   142: astore_3
    //   143: aload_3
    //   144: ifnull -> 159
    //   147: aload_2
    //   148: astore_0
    //   149: aload_3
    //   150: ldc_w 'TracerPid:'
    //   153: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   156: ifeq -> 136
    //   159: aload_3
    //   160: ifnull -> 203
    //   163: aload_2
    //   164: astore_0
    //   165: aload_3
    //   166: bipush #10
    //   168: invokevirtual substring : (I)Ljava/lang/String;
    //   171: invokevirtual trim : ()Ljava/lang/String;
    //   174: astore_3
    //   175: aload_2
    //   176: astore_0
    //   177: aload_1
    //   178: ldc_w 'tracer_pid'
    //   181: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   184: pop
    //   185: aload_2
    //   186: astore_0
    //   187: aload_1
    //   188: ldc_w '|'
    //   191: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   194: pop
    //   195: aload_2
    //   196: astore_0
    //   197: aload_1
    //   198: aload_3
    //   199: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   202: pop
    //   203: aload_2
    //   204: astore_0
    //   205: aload_1
    //   206: invokevirtual toString : ()Ljava/lang/String;
    //   209: astore_3
    //   210: aload_3
    //   211: astore_0
    //   212: aload_2
    //   213: invokevirtual close : ()V
    //   216: aload_0
    //   217: areturn
    //   218: astore_2
    //   219: aload_2
    //   220: invokestatic a : (Ljava/lang/Throwable;)Z
    //   223: pop
    //   224: goto -> 216
    //   227: astore_3
    //   228: aconst_null
    //   229: astore_2
    //   230: aload_2
    //   231: astore_0
    //   232: aload_3
    //   233: invokestatic a : (Ljava/lang/Throwable;)Z
    //   236: pop
    //   237: aload_2
    //   238: ifnull -> 245
    //   241: aload_2
    //   242: invokevirtual close : ()V
    //   245: aload_1
    //   246: invokevirtual toString : ()Ljava/lang/String;
    //   249: astore_0
    //   250: goto -> 216
    //   253: astore_0
    //   254: aload_0
    //   255: invokestatic a : (Ljava/lang/Throwable;)Z
    //   258: pop
    //   259: goto -> 245
    //   262: astore_2
    //   263: aconst_null
    //   264: astore_0
    //   265: aload_0
    //   266: ifnull -> 273
    //   269: aload_0
    //   270: invokevirtual close : ()V
    //   273: aload_2
    //   274: athrow
    //   275: astore_0
    //   276: aload_0
    //   277: invokestatic a : (Ljava/lang/Throwable;)Z
    //   280: pop
    //   281: goto -> 273
    //   284: astore_2
    //   285: goto -> 265
    //   288: astore_3
    //   289: goto -> 230
    // Exception table:
    //   from	to	target	type
    //   116	136	227	java/lang/Throwable
    //   116	136	262	finally
    //   138	143	288	java/lang/Throwable
    //   138	143	284	finally
    //   149	159	288	java/lang/Throwable
    //   149	159	284	finally
    //   165	175	288	java/lang/Throwable
    //   165	175	284	finally
    //   177	185	288	java/lang/Throwable
    //   177	185	284	finally
    //   187	195	288	java/lang/Throwable
    //   187	195	284	finally
    //   197	203	288	java/lang/Throwable
    //   197	203	284	finally
    //   205	210	288	java/lang/Throwable
    //   205	210	284	finally
    //   212	216	218	java/io/IOException
    //   232	237	284	finally
    //   241	245	253	java/io/IOException
    //   269	273	275	java/io/IOException
  }
  
  public static String l() {
    String str2;
    String str1 = "fail";
    try {
      str2 = Build.BRAND;
    } catch (Throwable throwable) {
      str2 = str1;
    } 
    return str2;
  }
  
  public static String l(Context paramContext) {
    StringBuilder stringBuilder = new StringBuilder();
    String str2 = z.a(paramContext, "gsm.sim.state");
    if (str2 != null) {
      stringBuilder.append("gsm.sim.state");
      stringBuilder.append("|");
      stringBuilder.append(str2);
    } 
    stringBuilder.append("\n");
    String str1 = z.a(paramContext, "gsm.sim.state2");
    if (str1 != null) {
      stringBuilder.append("gsm.sim.state2");
      stringBuilder.append("|");
      stringBuilder.append(str1);
    } 
    return stringBuilder.toString();
  }
  
  public static String m() {
    // Byte code:
    //   0: aconst_null
    //   1: astore_0
    //   2: new java/lang/StringBuilder
    //   5: astore_1
    //   6: aload_1
    //   7: invokespecial <init> : ()V
    //   10: new java/io/File
    //   13: astore_2
    //   14: aload_2
    //   15: ldc_w '/sys/block/mmcblk0/device/type'
    //   18: invokespecial <init> : (Ljava/lang/String;)V
    //   21: aload_2
    //   22: invokevirtual exists : ()Z
    //   25: ifeq -> 431
    //   28: new java/io/BufferedReader
    //   31: astore_3
    //   32: new java/io/FileReader
    //   35: astore_2
    //   36: aload_2
    //   37: ldc_w '/sys/block/mmcblk0/device/type'
    //   40: invokespecial <init> : (Ljava/lang/String;)V
    //   43: aload_3
    //   44: aload_2
    //   45: invokespecial <init> : (Ljava/io/Reader;)V
    //   48: aload_3
    //   49: astore_2
    //   50: aload_3
    //   51: invokevirtual readLine : ()Ljava/lang/String;
    //   54: astore #4
    //   56: aload #4
    //   58: ifnull -> 70
    //   61: aload_3
    //   62: astore_2
    //   63: aload_1
    //   64: aload #4
    //   66: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   69: pop
    //   70: aload_3
    //   71: astore_2
    //   72: aload_3
    //   73: invokevirtual close : ()V
    //   76: aload_3
    //   77: astore #4
    //   79: aload_1
    //   80: ldc_w ','
    //   83: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   86: pop
    //   87: aload_3
    //   88: astore #4
    //   90: new java/io/File
    //   93: astore #5
    //   95: aload_3
    //   96: astore #4
    //   98: aload #5
    //   100: ldc_w '/sys/block/mmcblk0/device/name'
    //   103: invokespecial <init> : (Ljava/lang/String;)V
    //   106: aload_3
    //   107: astore_2
    //   108: aload_3
    //   109: astore #4
    //   111: aload #5
    //   113: invokevirtual exists : ()Z
    //   116: ifeq -> 186
    //   119: aload_3
    //   120: astore #4
    //   122: new java/io/BufferedReader
    //   125: astore #5
    //   127: aload_3
    //   128: astore #4
    //   130: new java/io/FileReader
    //   133: astore_2
    //   134: aload_3
    //   135: astore #4
    //   137: aload_2
    //   138: ldc_w '/sys/block/mmcblk0/device/name'
    //   141: invokespecial <init> : (Ljava/lang/String;)V
    //   144: aload_3
    //   145: astore #4
    //   147: aload #5
    //   149: aload_2
    //   150: invokespecial <init> : (Ljava/io/Reader;)V
    //   153: aload #5
    //   155: astore_2
    //   156: aload #5
    //   158: invokevirtual readLine : ()Ljava/lang/String;
    //   161: astore_3
    //   162: aload_3
    //   163: ifnull -> 175
    //   166: aload #5
    //   168: astore_2
    //   169: aload_1
    //   170: aload_3
    //   171: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: pop
    //   175: aload #5
    //   177: astore_2
    //   178: aload #5
    //   180: invokevirtual close : ()V
    //   183: aload #5
    //   185: astore_2
    //   186: aload_2
    //   187: astore #4
    //   189: aload_1
    //   190: ldc_w ','
    //   193: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   196: pop
    //   197: aload_2
    //   198: astore #4
    //   200: new java/io/File
    //   203: astore_3
    //   204: aload_2
    //   205: astore #4
    //   207: aload_3
    //   208: ldc_w '/sys/block/mmcblk0/device/cid'
    //   211: invokespecial <init> : (Ljava/lang/String;)V
    //   214: aload_2
    //   215: astore #4
    //   217: aload_3
    //   218: invokevirtual exists : ()Z
    //   221: ifeq -> 426
    //   224: aload_2
    //   225: astore #4
    //   227: new java/io/BufferedReader
    //   230: astore #5
    //   232: aload_2
    //   233: astore #4
    //   235: new java/io/FileReader
    //   238: astore_3
    //   239: aload_2
    //   240: astore #4
    //   242: aload_3
    //   243: ldc_w '/sys/block/mmcblk0/device/cid'
    //   246: invokespecial <init> : (Ljava/lang/String;)V
    //   249: aload_2
    //   250: astore #4
    //   252: aload #5
    //   254: aload_3
    //   255: invokespecial <init> : (Ljava/io/Reader;)V
    //   258: aload #5
    //   260: astore_2
    //   261: aload #5
    //   263: invokevirtual readLine : ()Ljava/lang/String;
    //   266: astore #4
    //   268: aload #5
    //   270: astore_3
    //   271: aload #4
    //   273: ifnull -> 289
    //   276: aload #5
    //   278: astore_2
    //   279: aload_1
    //   280: aload #4
    //   282: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   285: pop
    //   286: aload #5
    //   288: astore_3
    //   289: aload_3
    //   290: astore_2
    //   291: aload_1
    //   292: invokevirtual toString : ()Ljava/lang/String;
    //   295: astore #4
    //   297: aload #4
    //   299: astore_2
    //   300: aload_2
    //   301: astore #4
    //   303: aload_3
    //   304: ifnull -> 314
    //   307: aload_3
    //   308: invokevirtual close : ()V
    //   311: aload_2
    //   312: astore #4
    //   314: aload #4
    //   316: areturn
    //   317: astore_3
    //   318: aload_3
    //   319: invokestatic a : (Ljava/lang/Throwable;)Z
    //   322: pop
    //   323: aload_2
    //   324: astore #4
    //   326: goto -> 314
    //   329: astore_2
    //   330: aconst_null
    //   331: astore_2
    //   332: aload_0
    //   333: astore #4
    //   335: aload_2
    //   336: ifnull -> 314
    //   339: aload_2
    //   340: invokevirtual close : ()V
    //   343: aload_0
    //   344: astore #4
    //   346: goto -> 314
    //   349: astore_2
    //   350: aload_2
    //   351: invokestatic a : (Ljava/lang/Throwable;)Z
    //   354: pop
    //   355: aload_0
    //   356: astore #4
    //   358: goto -> 314
    //   361: astore_2
    //   362: aconst_null
    //   363: astore_3
    //   364: aload_3
    //   365: ifnull -> 372
    //   368: aload_3
    //   369: invokevirtual close : ()V
    //   372: aload_2
    //   373: athrow
    //   374: astore_3
    //   375: aload_3
    //   376: invokestatic a : (Ljava/lang/Throwable;)Z
    //   379: pop
    //   380: goto -> 372
    //   383: astore_2
    //   384: goto -> 364
    //   387: astore_2
    //   388: aload #4
    //   390: astore_3
    //   391: goto -> 364
    //   394: astore_2
    //   395: aload #5
    //   397: astore_3
    //   398: goto -> 364
    //   401: astore_2
    //   402: aload #5
    //   404: astore_3
    //   405: goto -> 364
    //   408: astore_2
    //   409: goto -> 364
    //   412: astore_3
    //   413: goto -> 332
    //   416: astore_2
    //   417: aload_3
    //   418: astore_2
    //   419: goto -> 332
    //   422: astore_3
    //   423: goto -> 332
    //   426: aload_2
    //   427: astore_3
    //   428: goto -> 289
    //   431: aconst_null
    //   432: astore_3
    //   433: goto -> 76
    // Exception table:
    //   from	to	target	type
    //   2	48	329	java/lang/Throwable
    //   2	48	361	finally
    //   50	56	412	java/lang/Throwable
    //   50	56	383	finally
    //   63	70	412	java/lang/Throwable
    //   63	70	383	finally
    //   72	76	412	java/lang/Throwable
    //   72	76	383	finally
    //   79	87	416	java/lang/Throwable
    //   79	87	387	finally
    //   90	95	416	java/lang/Throwable
    //   90	95	387	finally
    //   98	106	416	java/lang/Throwable
    //   98	106	387	finally
    //   111	119	416	java/lang/Throwable
    //   111	119	387	finally
    //   122	127	416	java/lang/Throwable
    //   122	127	387	finally
    //   130	134	416	java/lang/Throwable
    //   130	134	387	finally
    //   137	144	416	java/lang/Throwable
    //   137	144	387	finally
    //   147	153	416	java/lang/Throwable
    //   147	153	387	finally
    //   156	162	412	java/lang/Throwable
    //   156	162	394	finally
    //   169	175	412	java/lang/Throwable
    //   169	175	394	finally
    //   178	183	412	java/lang/Throwable
    //   178	183	394	finally
    //   189	197	422	java/lang/Throwable
    //   189	197	387	finally
    //   200	204	422	java/lang/Throwable
    //   200	204	387	finally
    //   207	214	422	java/lang/Throwable
    //   207	214	387	finally
    //   217	224	422	java/lang/Throwable
    //   217	224	387	finally
    //   227	232	422	java/lang/Throwable
    //   227	232	387	finally
    //   235	239	422	java/lang/Throwable
    //   235	239	387	finally
    //   242	249	422	java/lang/Throwable
    //   242	249	387	finally
    //   252	258	422	java/lang/Throwable
    //   252	258	387	finally
    //   261	268	412	java/lang/Throwable
    //   261	268	401	finally
    //   279	286	412	java/lang/Throwable
    //   279	286	401	finally
    //   291	297	412	java/lang/Throwable
    //   291	297	408	finally
    //   307	311	317	java/io/IOException
    //   339	343	349	java/io/IOException
    //   368	372	374	java/io/IOException
  }
  
  public static String n() {
    // Byte code:
    //   0: new java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_0
    //   8: aconst_null
    //   9: astore_1
    //   10: aconst_null
    //   11: astore_2
    //   12: aload_1
    //   13: astore_3
    //   14: new java/io/File
    //   17: astore #4
    //   19: aload_1
    //   20: astore_3
    //   21: aload #4
    //   23: ldc_w '/sys/class/power_supply/ac/online'
    //   26: invokespecial <init> : (Ljava/lang/String;)V
    //   29: aload_1
    //   30: astore_3
    //   31: aload #4
    //   33: invokevirtual exists : ()Z
    //   36: ifeq -> 124
    //   39: aload_1
    //   40: astore_3
    //   41: new java/io/BufferedReader
    //   44: astore #4
    //   46: aload_1
    //   47: astore_3
    //   48: new java/io/FileReader
    //   51: astore_2
    //   52: aload_1
    //   53: astore_3
    //   54: aload_2
    //   55: ldc_w '/sys/class/power_supply/ac/online'
    //   58: invokespecial <init> : (Ljava/lang/String;)V
    //   61: aload_1
    //   62: astore_3
    //   63: aload #4
    //   65: aload_2
    //   66: invokespecial <init> : (Ljava/io/Reader;)V
    //   69: aload #4
    //   71: astore_2
    //   72: aload #4
    //   74: invokevirtual readLine : ()Ljava/lang/String;
    //   77: astore_3
    //   78: aload_3
    //   79: ifnull -> 113
    //   82: aload #4
    //   84: astore_2
    //   85: aload_0
    //   86: ldc_w 'ac_online'
    //   89: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   92: pop
    //   93: aload #4
    //   95: astore_2
    //   96: aload_0
    //   97: ldc_w '|'
    //   100: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   103: pop
    //   104: aload #4
    //   106: astore_2
    //   107: aload_0
    //   108: aload_3
    //   109: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   112: pop
    //   113: aload #4
    //   115: astore_2
    //   116: aload #4
    //   118: invokevirtual close : ()V
    //   121: aload #4
    //   123: astore_2
    //   124: aload_2
    //   125: astore_3
    //   126: aload_0
    //   127: ldc_w '\\n'
    //   130: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   133: pop
    //   134: aload_2
    //   135: astore_3
    //   136: new java/io/File
    //   139: astore_1
    //   140: aload_2
    //   141: astore_3
    //   142: aload_1
    //   143: ldc_w '/sys/class/power_supply/usb/online'
    //   146: invokespecial <init> : (Ljava/lang/String;)V
    //   149: aload_2
    //   150: astore #4
    //   152: aload_2
    //   153: astore_3
    //   154: aload_1
    //   155: invokevirtual exists : ()Z
    //   158: ifeq -> 243
    //   161: aload_2
    //   162: astore_3
    //   163: new java/io/BufferedReader
    //   166: astore #4
    //   168: aload_2
    //   169: astore_3
    //   170: new java/io/FileReader
    //   173: astore_1
    //   174: aload_2
    //   175: astore_3
    //   176: aload_1
    //   177: ldc_w '/sys/class/power_supply/usb/online'
    //   180: invokespecial <init> : (Ljava/lang/String;)V
    //   183: aload_2
    //   184: astore_3
    //   185: aload #4
    //   187: aload_1
    //   188: invokespecial <init> : (Ljava/io/Reader;)V
    //   191: aload #4
    //   193: astore_2
    //   194: aload #4
    //   196: invokevirtual readLine : ()Ljava/lang/String;
    //   199: astore_3
    //   200: aload_3
    //   201: ifnull -> 235
    //   204: aload #4
    //   206: astore_2
    //   207: aload_0
    //   208: ldc_w 'usb_online'
    //   211: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   214: pop
    //   215: aload #4
    //   217: astore_2
    //   218: aload_0
    //   219: ldc_w '|'
    //   222: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   225: pop
    //   226: aload #4
    //   228: astore_2
    //   229: aload_0
    //   230: aload_3
    //   231: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   234: pop
    //   235: aload #4
    //   237: astore_2
    //   238: aload #4
    //   240: invokevirtual close : ()V
    //   243: aload #4
    //   245: astore_3
    //   246: aload_0
    //   247: ldc_w '\\n'
    //   250: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   253: pop
    //   254: aload #4
    //   256: astore_3
    //   257: new java/io/File
    //   260: astore_2
    //   261: aload #4
    //   263: astore_3
    //   264: aload_2
    //   265: ldc_w '/sys/class/power_supply/battery/capacity'
    //   268: invokespecial <init> : (Ljava/lang/String;)V
    //   271: aload #4
    //   273: astore_3
    //   274: aload_2
    //   275: invokevirtual exists : ()Z
    //   278: ifeq -> 470
    //   281: aload #4
    //   283: astore_3
    //   284: new java/io/BufferedReader
    //   287: astore_1
    //   288: aload #4
    //   290: astore_3
    //   291: new java/io/FileReader
    //   294: astore_2
    //   295: aload #4
    //   297: astore_3
    //   298: aload_2
    //   299: ldc_w '/sys/class/power_supply/battery/capacity'
    //   302: invokespecial <init> : (Ljava/lang/String;)V
    //   305: aload #4
    //   307: astore_3
    //   308: aload_1
    //   309: aload_2
    //   310: invokespecial <init> : (Ljava/io/Reader;)V
    //   313: aload_1
    //   314: astore_2
    //   315: aload_1
    //   316: invokevirtual readLine : ()Ljava/lang/String;
    //   319: astore #4
    //   321: aload #4
    //   323: ifnull -> 355
    //   326: aload_1
    //   327: astore_2
    //   328: aload_0
    //   329: ldc_w 'battery_capacity'
    //   332: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   335: pop
    //   336: aload_1
    //   337: astore_2
    //   338: aload_0
    //   339: ldc_w '|'
    //   342: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   345: pop
    //   346: aload_1
    //   347: astore_2
    //   348: aload_0
    //   349: aload #4
    //   351: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   354: pop
    //   355: aload_1
    //   356: astore_2
    //   357: aload_1
    //   358: invokevirtual close : ()V
    //   361: aload_1
    //   362: astore #4
    //   364: aload #4
    //   366: ifnull -> 374
    //   369: aload #4
    //   371: invokevirtual close : ()V
    //   374: aload_0
    //   375: invokevirtual toString : ()Ljava/lang/String;
    //   378: areturn
    //   379: astore_2
    //   380: aload_2
    //   381: invokestatic a : (Ljava/lang/Throwable;)Z
    //   384: pop
    //   385: goto -> 374
    //   388: astore_2
    //   389: aconst_null
    //   390: astore_2
    //   391: aload_2
    //   392: ifnull -> 374
    //   395: aload_2
    //   396: invokevirtual close : ()V
    //   399: goto -> 374
    //   402: astore_2
    //   403: aload_2
    //   404: invokestatic a : (Ljava/lang/Throwable;)Z
    //   407: pop
    //   408: goto -> 374
    //   411: astore_2
    //   412: aload_3
    //   413: ifnull -> 420
    //   416: aload_3
    //   417: invokevirtual close : ()V
    //   420: aload_2
    //   421: athrow
    //   422: astore #4
    //   424: aload #4
    //   426: invokestatic a : (Ljava/lang/Throwable;)Z
    //   429: pop
    //   430: goto -> 420
    //   433: astore_2
    //   434: aload #4
    //   436: astore_3
    //   437: goto -> 412
    //   440: astore_2
    //   441: aload #4
    //   443: astore_3
    //   444: goto -> 412
    //   447: astore_2
    //   448: aload_1
    //   449: astore_3
    //   450: goto -> 412
    //   453: astore #4
    //   455: goto -> 391
    //   458: astore #4
    //   460: goto -> 391
    //   463: astore_2
    //   464: aload #4
    //   466: astore_2
    //   467: goto -> 391
    //   470: goto -> 364
    // Exception table:
    //   from	to	target	type
    //   14	19	388	java/lang/Throwable
    //   14	19	411	finally
    //   21	29	388	java/lang/Throwable
    //   21	29	411	finally
    //   31	39	388	java/lang/Throwable
    //   31	39	411	finally
    //   41	46	388	java/lang/Throwable
    //   41	46	411	finally
    //   48	52	388	java/lang/Throwable
    //   48	52	411	finally
    //   54	61	388	java/lang/Throwable
    //   54	61	411	finally
    //   63	69	388	java/lang/Throwable
    //   63	69	411	finally
    //   72	78	453	java/lang/Throwable
    //   72	78	433	finally
    //   85	93	453	java/lang/Throwable
    //   85	93	433	finally
    //   96	104	453	java/lang/Throwable
    //   96	104	433	finally
    //   107	113	453	java/lang/Throwable
    //   107	113	433	finally
    //   116	121	453	java/lang/Throwable
    //   116	121	433	finally
    //   126	134	458	java/lang/Throwable
    //   126	134	411	finally
    //   136	140	458	java/lang/Throwable
    //   136	140	411	finally
    //   142	149	458	java/lang/Throwable
    //   142	149	411	finally
    //   154	161	458	java/lang/Throwable
    //   154	161	411	finally
    //   163	168	458	java/lang/Throwable
    //   163	168	411	finally
    //   170	174	458	java/lang/Throwable
    //   170	174	411	finally
    //   176	183	458	java/lang/Throwable
    //   176	183	411	finally
    //   185	191	458	java/lang/Throwable
    //   185	191	411	finally
    //   194	200	453	java/lang/Throwable
    //   194	200	440	finally
    //   207	215	453	java/lang/Throwable
    //   207	215	440	finally
    //   218	226	453	java/lang/Throwable
    //   218	226	440	finally
    //   229	235	453	java/lang/Throwable
    //   229	235	440	finally
    //   238	243	453	java/lang/Throwable
    //   238	243	440	finally
    //   246	254	463	java/lang/Throwable
    //   246	254	411	finally
    //   257	261	463	java/lang/Throwable
    //   257	261	411	finally
    //   264	271	463	java/lang/Throwable
    //   264	271	411	finally
    //   274	281	463	java/lang/Throwable
    //   274	281	411	finally
    //   284	288	463	java/lang/Throwable
    //   284	288	411	finally
    //   291	295	463	java/lang/Throwable
    //   291	295	411	finally
    //   298	305	463	java/lang/Throwable
    //   298	305	411	finally
    //   308	313	463	java/lang/Throwable
    //   308	313	411	finally
    //   315	321	453	java/lang/Throwable
    //   315	321	447	finally
    //   328	336	453	java/lang/Throwable
    //   328	336	447	finally
    //   338	346	453	java/lang/Throwable
    //   338	346	447	finally
    //   348	355	453	java/lang/Throwable
    //   348	355	447	finally
    //   357	361	453	java/lang/Throwable
    //   357	361	447	finally
    //   369	374	379	java/io/IOException
    //   395	399	402	java/io/IOException
    //   416	420	422	java/io/IOException
  }
  
  public static long o() {
    // Byte code:
    //   0: fconst_0
    //   1: fstore_0
    //   2: fconst_0
    //   3: fstore_1
    //   4: new java/io/BufferedReader
    //   7: astore_2
    //   8: new java/io/FileReader
    //   11: astore_3
    //   12: aload_3
    //   13: ldc_w '/proc/uptime'
    //   16: invokespecial <init> : (Ljava/lang/String;)V
    //   19: aload_2
    //   20: aload_3
    //   21: invokespecial <init> : (Ljava/io/Reader;)V
    //   24: aload_2
    //   25: astore_3
    //   26: aload_2
    //   27: invokevirtual readLine : ()Ljava/lang/String;
    //   30: astore #4
    //   32: aload #4
    //   34: ifnull -> 76
    //   37: aload_2
    //   38: astore_3
    //   39: aload #4
    //   41: ldc_w ' '
    //   44: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
    //   47: iconst_0
    //   48: aaload
    //   49: astore #4
    //   51: aload_2
    //   52: astore_3
    //   53: invokestatic currentTimeMillis : ()J
    //   56: ldc2_w 1000
    //   59: ldiv
    //   60: l2f
    //   61: fstore_1
    //   62: aload_2
    //   63: astore_3
    //   64: aload #4
    //   66: invokestatic parseFloat : (Ljava/lang/String;)F
    //   69: fstore #5
    //   71: fload_1
    //   72: fload #5
    //   74: fsub
    //   75: fstore_1
    //   76: aload_2
    //   77: invokevirtual close : ()V
    //   80: fload_1
    //   81: f2l
    //   82: lreturn
    //   83: astore_3
    //   84: aload_3
    //   85: invokestatic a : (Ljava/lang/Throwable;)Z
    //   88: pop
    //   89: goto -> 80
    //   92: astore #4
    //   94: aconst_null
    //   95: astore_2
    //   96: aload_2
    //   97: astore_3
    //   98: aload #4
    //   100: invokestatic a : (Ljava/lang/Throwable;)Z
    //   103: pop
    //   104: fload_0
    //   105: fstore_1
    //   106: aload_2
    //   107: ifnull -> 80
    //   110: aload_2
    //   111: invokevirtual close : ()V
    //   114: fload_0
    //   115: fstore_1
    //   116: goto -> 80
    //   119: astore_3
    //   120: aload_3
    //   121: invokestatic a : (Ljava/lang/Throwable;)Z
    //   124: pop
    //   125: fload_0
    //   126: fstore_1
    //   127: goto -> 80
    //   130: astore_2
    //   131: aconst_null
    //   132: astore_3
    //   133: aload_3
    //   134: ifnull -> 141
    //   137: aload_3
    //   138: invokevirtual close : ()V
    //   141: aload_2
    //   142: athrow
    //   143: astore_3
    //   144: aload_3
    //   145: invokestatic a : (Ljava/lang/Throwable;)Z
    //   148: pop
    //   149: goto -> 141
    //   152: astore_2
    //   153: goto -> 133
    //   156: astore #4
    //   158: goto -> 96
    // Exception table:
    //   from	to	target	type
    //   4	24	92	java/lang/Throwable
    //   4	24	130	finally
    //   26	32	156	java/lang/Throwable
    //   26	32	152	finally
    //   39	51	156	java/lang/Throwable
    //   39	51	152	finally
    //   53	62	156	java/lang/Throwable
    //   53	62	152	finally
    //   64	71	156	java/lang/Throwable
    //   64	71	152	finally
    //   76	80	83	java/io/IOException
    //   98	104	152	finally
    //   110	114	119	java/io/IOException
    //   137	141	143	java/io/IOException
  }
  
  private static boolean p() {
    try {
      boolean bool = Environment.getExternalStorageState().equals("mounted");
      if (bool)
        return true; 
    } catch (Throwable throwable) {
      if (!x.a(throwable))
        throwable.printStackTrace(); 
    } 
    return false;
  }
  
  private static String q() {
    // Byte code:
    //   0: new java/io/FileReader
    //   3: astore_0
    //   4: aload_0
    //   5: ldc_w '/system/build.prop'
    //   8: invokespecial <init> : (Ljava/lang/String;)V
    //   11: new java/io/BufferedReader
    //   14: astore_1
    //   15: aload_1
    //   16: aload_0
    //   17: sipush #2048
    //   20: invokespecial <init> : (Ljava/io/Reader;I)V
    //   23: aload_1
    //   24: astore_2
    //   25: aload_0
    //   26: astore_3
    //   27: aload_1
    //   28: invokevirtual readLine : ()Ljava/lang/String;
    //   31: astore #4
    //   33: aload #4
    //   35: ifnull -> 368
    //   38: aload_1
    //   39: astore_2
    //   40: aload_0
    //   41: astore_3
    //   42: aload #4
    //   44: ldc_w '='
    //   47: iconst_2
    //   48: invokevirtual split : (Ljava/lang/String;I)[Ljava/lang/String;
    //   51: astore #4
    //   53: aload_1
    //   54: astore_2
    //   55: aload_0
    //   56: astore_3
    //   57: aload #4
    //   59: arraylength
    //   60: iconst_2
    //   61: if_icmpne -> 23
    //   64: aload_1
    //   65: astore_2
    //   66: aload_0
    //   67: astore_3
    //   68: aload #4
    //   70: iconst_0
    //   71: aaload
    //   72: ldc_w 'ro.product.cpu.abilist'
    //   75: invokevirtual equals : (Ljava/lang/Object;)Z
    //   78: ifeq -> 128
    //   81: aload #4
    //   83: iconst_1
    //   84: aaload
    //   85: astore #4
    //   87: aload #4
    //   89: astore_3
    //   90: aload #4
    //   92: ifnull -> 114
    //   95: aload_1
    //   96: astore_2
    //   97: aload_0
    //   98: astore_3
    //   99: aload #4
    //   101: ldc_w ','
    //   104: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
    //   107: iconst_0
    //   108: aaload
    //   109: astore #4
    //   111: aload #4
    //   113: astore_3
    //   114: aload_1
    //   115: invokevirtual close : ()V
    //   118: aload_0
    //   119: invokevirtual close : ()V
    //   122: aload_3
    //   123: astore #4
    //   125: aload #4
    //   127: areturn
    //   128: aload_1
    //   129: astore_2
    //   130: aload_0
    //   131: astore_3
    //   132: aload #4
    //   134: iconst_0
    //   135: aaload
    //   136: ldc_w 'ro.product.cpu.abi'
    //   139: invokevirtual equals : (Ljava/lang/Object;)Z
    //   142: ifeq -> 23
    //   145: aload #4
    //   147: iconst_1
    //   148: aaload
    //   149: astore #4
    //   151: goto -> 87
    //   154: astore #4
    //   156: aload #4
    //   158: invokestatic a : (Ljava/lang/Throwable;)Z
    //   161: ifne -> 118
    //   164: aload #4
    //   166: invokevirtual printStackTrace : ()V
    //   169: goto -> 118
    //   172: astore_0
    //   173: aload_3
    //   174: astore #4
    //   176: aload_0
    //   177: invokestatic a : (Ljava/lang/Throwable;)Z
    //   180: ifne -> 125
    //   183: aload_0
    //   184: invokevirtual printStackTrace : ()V
    //   187: aload_3
    //   188: astore #4
    //   190: goto -> 125
    //   193: astore #5
    //   195: aconst_null
    //   196: astore #4
    //   198: aconst_null
    //   199: astore_0
    //   200: aload #4
    //   202: astore_2
    //   203: aload_0
    //   204: astore_3
    //   205: aload #5
    //   207: invokestatic a : (Ljava/lang/Throwable;)Z
    //   210: ifne -> 223
    //   213: aload #4
    //   215: astore_2
    //   216: aload_0
    //   217: astore_3
    //   218: aload #5
    //   220: invokevirtual printStackTrace : ()V
    //   223: aload #4
    //   225: ifnull -> 233
    //   228: aload #4
    //   230: invokevirtual close : ()V
    //   233: aload_0
    //   234: ifnull -> 241
    //   237: aload_0
    //   238: invokevirtual close : ()V
    //   241: aconst_null
    //   242: astore #4
    //   244: goto -> 125
    //   247: astore #4
    //   249: aload #4
    //   251: invokestatic a : (Ljava/lang/Throwable;)Z
    //   254: ifne -> 233
    //   257: aload #4
    //   259: invokevirtual printStackTrace : ()V
    //   262: goto -> 233
    //   265: astore #4
    //   267: aload #4
    //   269: invokestatic a : (Ljava/lang/Throwable;)Z
    //   272: ifne -> 241
    //   275: aload #4
    //   277: invokevirtual printStackTrace : ()V
    //   280: goto -> 241
    //   283: astore #4
    //   285: aconst_null
    //   286: astore_2
    //   287: aconst_null
    //   288: astore_0
    //   289: aload_2
    //   290: ifnull -> 297
    //   293: aload_2
    //   294: invokevirtual close : ()V
    //   297: aload_0
    //   298: ifnull -> 305
    //   301: aload_0
    //   302: invokevirtual close : ()V
    //   305: aload #4
    //   307: athrow
    //   308: astore_3
    //   309: aload_3
    //   310: invokestatic a : (Ljava/lang/Throwable;)Z
    //   313: ifne -> 297
    //   316: aload_3
    //   317: invokevirtual printStackTrace : ()V
    //   320: goto -> 297
    //   323: astore_0
    //   324: aload_0
    //   325: invokestatic a : (Ljava/lang/Throwable;)Z
    //   328: ifne -> 305
    //   331: aload_0
    //   332: invokevirtual printStackTrace : ()V
    //   335: goto -> 305
    //   338: astore #4
    //   340: aconst_null
    //   341: astore_2
    //   342: goto -> 289
    //   345: astore #4
    //   347: aload_3
    //   348: astore_0
    //   349: goto -> 289
    //   352: astore #5
    //   354: aconst_null
    //   355: astore #4
    //   357: goto -> 200
    //   360: astore #5
    //   362: aload_1
    //   363: astore #4
    //   365: goto -> 200
    //   368: aconst_null
    //   369: astore #4
    //   371: goto -> 87
    // Exception table:
    //   from	to	target	type
    //   0	11	193	java/lang/Throwable
    //   0	11	283	finally
    //   11	23	352	java/lang/Throwable
    //   11	23	338	finally
    //   27	33	360	java/lang/Throwable
    //   27	33	345	finally
    //   42	53	360	java/lang/Throwable
    //   42	53	345	finally
    //   57	64	360	java/lang/Throwable
    //   57	64	345	finally
    //   68	81	360	java/lang/Throwable
    //   68	81	345	finally
    //   99	111	360	java/lang/Throwable
    //   99	111	345	finally
    //   114	118	154	java/io/IOException
    //   118	122	172	java/io/IOException
    //   132	145	360	java/lang/Throwable
    //   132	145	345	finally
    //   205	213	345	finally
    //   218	223	345	finally
    //   228	233	247	java/io/IOException
    //   237	241	265	java/io/IOException
    //   293	297	308	java/io/IOException
    //   301	305	323	java/io/IOException
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/crashreport/common/info/b.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */