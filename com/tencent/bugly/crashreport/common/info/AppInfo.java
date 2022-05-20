package com.tencent.bugly.crashreport.common.info;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppInfo {
  private static ActivityManager a;
  
  static {
    "@buglyAllChannel@".split(",");
    "@buglyAllChannelPriority@".split(",");
  }
  
  public static String a(int paramInt) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: new java/io/FileReader
    //   5: astore_2
    //   6: new java/lang/StringBuilder
    //   9: astore_3
    //   10: aload_3
    //   11: ldc '/proc/'
    //   13: invokespecial <init> : (Ljava/lang/String;)V
    //   16: aload_2
    //   17: aload_3
    //   18: iload_0
    //   19: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   22: ldc '/cmdline'
    //   24: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   27: invokevirtual toString : ()Ljava/lang/String;
    //   30: invokespecial <init> : (Ljava/lang/String;)V
    //   33: aload_2
    //   34: astore_3
    //   35: sipush #512
    //   38: newarray char
    //   40: astore #4
    //   42: aload_2
    //   43: astore_3
    //   44: aload_2
    //   45: aload #4
    //   47: invokevirtual read : ([C)I
    //   50: pop
    //   51: aload_2
    //   52: astore_3
    //   53: iload_1
    //   54: aload #4
    //   56: arraylength
    //   57: if_icmpge -> 73
    //   60: aload #4
    //   62: iload_1
    //   63: caload
    //   64: ifeq -> 73
    //   67: iinc #1, 1
    //   70: goto -> 51
    //   73: aload_2
    //   74: astore_3
    //   75: new java/lang/String
    //   78: astore #5
    //   80: aload_2
    //   81: astore_3
    //   82: aload #5
    //   84: aload #4
    //   86: invokespecial <init> : ([C)V
    //   89: aload_2
    //   90: astore_3
    //   91: aload #5
    //   93: iconst_0
    //   94: iload_1
    //   95: invokevirtual substring : (II)Ljava/lang/String;
    //   98: astore #5
    //   100: aload #5
    //   102: astore_3
    //   103: aload_2
    //   104: invokevirtual close : ()V
    //   107: aload_3
    //   108: areturn
    //   109: astore #5
    //   111: aconst_null
    //   112: astore_2
    //   113: aload_2
    //   114: astore_3
    //   115: aload #5
    //   117: invokestatic a : (Ljava/lang/Throwable;)Z
    //   120: ifne -> 130
    //   123: aload_2
    //   124: astore_3
    //   125: aload #5
    //   127: invokevirtual printStackTrace : ()V
    //   130: iload_0
    //   131: invokestatic valueOf : (I)Ljava/lang/String;
    //   134: astore #5
    //   136: aload #5
    //   138: astore_3
    //   139: aload_2
    //   140: ifnull -> 107
    //   143: aload_2
    //   144: invokevirtual close : ()V
    //   147: aload #5
    //   149: astore_3
    //   150: goto -> 107
    //   153: astore_3
    //   154: aload #5
    //   156: astore_3
    //   157: goto -> 107
    //   160: astore_2
    //   161: aconst_null
    //   162: astore_3
    //   163: aload_3
    //   164: ifnull -> 171
    //   167: aload_3
    //   168: invokevirtual close : ()V
    //   171: aload_2
    //   172: athrow
    //   173: astore_2
    //   174: goto -> 107
    //   177: astore_3
    //   178: goto -> 171
    //   181: astore_2
    //   182: goto -> 163
    //   185: astore #5
    //   187: goto -> 113
    // Exception table:
    //   from	to	target	type
    //   2	33	109	java/lang/Throwable
    //   2	33	160	finally
    //   35	42	185	java/lang/Throwable
    //   35	42	181	finally
    //   44	51	185	java/lang/Throwable
    //   44	51	181	finally
    //   53	60	185	java/lang/Throwable
    //   53	60	181	finally
    //   75	80	185	java/lang/Throwable
    //   75	80	181	finally
    //   82	89	185	java/lang/Throwable
    //   82	89	181	finally
    //   91	100	185	java/lang/Throwable
    //   91	100	181	finally
    //   103	107	173	java/lang/Throwable
    //   115	123	181	finally
    //   125	130	181	finally
    //   143	147	153	java/lang/Throwable
    //   167	171	177	java/lang/Throwable
  }
  
  public static String a(Context paramContext) {
    if (paramContext == null)
      return null; 
    try {
      String str = paramContext.getPackageName();
    } catch (Throwable throwable) {}
    return (String)throwable;
  }
  
  private static String a(byte[] paramArrayOfbyte) {
    StringBuilder stringBuilder = new StringBuilder();
    if (paramArrayOfbyte != null && paramArrayOfbyte.length > 0) {
      try {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        if (certificateFactory == null)
          return null; 
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream();
        this(paramArrayOfbyte);
        X509Certificate x509Certificate = (X509Certificate)certificateFactory.generateCertificate(byteArrayInputStream);
        if (x509Certificate == null)
          return null; 
        stringBuilder.append("Issuer|");
        Principal principal = x509Certificate.getIssuerDN();
        if (principal != null) {
          stringBuilder.append(principal.toString());
        } else {
          stringBuilder.append("unknown");
        } 
        stringBuilder.append("\n");
        stringBuilder.append("SerialNumber|");
        BigInteger bigInteger = x509Certificate.getSerialNumber();
        if (principal != null) {
          stringBuilder.append(bigInteger.toString(16));
        } else {
          stringBuilder.append("unknown");
        } 
        stringBuilder.append("\n");
        stringBuilder.append("NotBefore|");
        Date date = x509Certificate.getNotBefore();
        if (principal != null) {
          stringBuilder.append(date.toString());
        } else {
          stringBuilder.append("unknown");
        } 
        stringBuilder.append("\n");
        stringBuilder.append("NotAfter|");
        date = x509Certificate.getNotAfter();
        if (principal != null) {
          stringBuilder.append(date.toString());
        } else {
          stringBuilder.append("unknown");
        } 
        stringBuilder.append("\n");
        stringBuilder.append("SHA1|");
        String str2 = z.a(MessageDigest.getInstance("SHA1").digest(x509Certificate.getEncoded()));
        if (str2 != null && str2.length() > 0) {
          stringBuilder.append(str2.toString());
        } else {
          stringBuilder.append("unknown");
        } 
        stringBuilder.append("\n");
        stringBuilder.append("MD5|");
        String str1 = z.a(MessageDigest.getInstance("MD5").digest(x509Certificate.getEncoded()));
        if (str1 != null && str1.length() > 0) {
          stringBuilder.append(str1.toString());
        } else {
          stringBuilder.append("unknown");
        } 
        if (stringBuilder.length() == 0)
          return "unknown"; 
      } catch (CertificateException certificateException) {
        if (!x.a(certificateException))
          certificateException.printStackTrace(); 
        if (stringBuilder.length() == 0)
          return "unknown"; 
      } catch (Throwable throwable) {
        if (!x.a(throwable))
          throwable.printStackTrace(); 
        if (stringBuilder.length() == 0)
          return "unknown"; 
      } 
      return stringBuilder.toString();
    } 
    if (stringBuilder.length() == 0)
      return "unknown"; 
  }
  
  public static List<String> a(Map<String, String> paramMap) {
    if (paramMap == null)
      return null; 
    try {
      String str = paramMap.get("BUGLY_DISABLE");
      if (str == null || str.length() == 0)
        return null; 
      String[] arrayOfString = str.split(",");
      for (byte b = 0; b < arrayOfString.length; b++)
        arrayOfString[b] = arrayOfString[b].trim(); 
      List<String> list = Arrays.asList(arrayOfString);
    } catch (Throwable throwable) {}
    return (List<String>)throwable;
  }
  
  public static boolean a(Context paramContext, String paramString) {
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (paramContext != null) {
      bool2 = bool1;
      if (paramString != null) {
        if (paramString.trim().length() <= 0)
          return bool1; 
      } else {
        return bool2;
      } 
    } else {
      return bool2;
    } 
    try {
      String[] arrayOfString = (paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 4096)).requestedPermissions;
      bool2 = bool1;
      if (arrayOfString != null) {
        int i = arrayOfString.length;
        byte b = 0;
        while (true) {
          bool2 = bool1;
          if (b < i) {
            bool2 = paramString.equals(arrayOfString[b]);
            if (bool2)
              return true; 
            b++;
            continue;
          } 
          return bool2;
        } 
      } 
    } catch (Throwable throwable) {
      bool2 = bool1;
    } 
    return bool2;
  }
  
  public static PackageInfo b(Context paramContext) {
    try {
      String str = a(paramContext);
      PackageInfo packageInfo = paramContext.getPackageManager().getPackageInfo(str, 0);
    } catch (Throwable throwable) {}
    return (PackageInfo)throwable;
  }
  
  public static String c(Context paramContext) {
    Context context = null;
    if (paramContext == null)
      return (String)context; 
    try {
      PackageManager packageManager = paramContext.getPackageManager();
      ApplicationInfo applicationInfo = paramContext.getApplicationInfo();
      paramContext = context;
      if (packageManager != null) {
        paramContext = context;
        if (applicationInfo != null) {
          CharSequence charSequence = packageManager.getApplicationLabel(applicationInfo);
          paramContext = context;
          if (charSequence != null)
            String str = charSequence.toString(); 
        } 
      } 
    } catch (Throwable throwable) {
      paramContext = context;
    } 
    return (String)paramContext;
  }
  
  public static Map<String, String> d(Context paramContext) {
    Context context = null;
    if (paramContext == null)
      return (Map<String, String>)context; 
    try {
      ApplicationInfo applicationInfo = paramContext.getPackageManager().getApplicationInfo(paramContext.getPackageName(), 128);
      if (applicationInfo.metaData != null) {
        HashMap<Object, Object> hashMap2 = new HashMap<Object, Object>();
        this();
        Object object = applicationInfo.metaData.get("BUGLY_DISABLE");
        if (object != null)
          hashMap2.put("BUGLY_DISABLE", object.toString()); 
        object = applicationInfo.metaData.get("BUGLY_APPID");
        if (object != null)
          hashMap2.put("BUGLY_APPID", object.toString()); 
        object = applicationInfo.metaData.get("BUGLY_APP_CHANNEL");
        if (object != null)
          hashMap2.put("BUGLY_APP_CHANNEL", object.toString()); 
        object = applicationInfo.metaData.get("BUGLY_APP_VERSION");
        if (object != null)
          hashMap2.put("BUGLY_APP_VERSION", object.toString()); 
        object = applicationInfo.metaData.get("BUGLY_ENABLE_DEBUG");
        if (object != null)
          hashMap2.put("BUGLY_ENABLE_DEBUG", object.toString()); 
        object = applicationInfo.metaData.get("com.tencent.rdm.uuid");
        HashMap<Object, Object> hashMap1 = hashMap2;
        if (object != null) {
          hashMap2.put("com.tencent.rdm.uuid", object.toString());
          hashMap1 = hashMap2;
        } 
        return (Map)hashMap1;
      } 
    } catch (Throwable throwable) {
      paramContext = context;
      if (!x.a(throwable)) {
        throwable.printStackTrace();
        paramContext = context;
      } 
      return (Map<String, String>)paramContext;
    } 
    return null;
  }
  
  public static String e(Context paramContext) {
    Context context1;
    Context context2 = null;
    String str = a(paramContext);
    if (str == null)
      return (String)context2; 
    try {
      PackageInfo packageInfo = paramContext.getPackageManager().getPackageInfo(str, 64);
      paramContext = context2;
      if (packageInfo != null) {
        Signature[] arrayOfSignature = packageInfo.signatures;
        paramContext = context2;
        if (arrayOfSignature != null) {
          paramContext = context2;
          if (arrayOfSignature.length != 0)
            String str1 = a(packageInfo.signatures[0].toByteArray()); 
        } 
      } 
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      context1 = context2;
    } 
    return (String)context1;
  }
  
  public static boolean f(Context paramContext) {
    boolean bool;
    if (paramContext == null)
      return false; 
    if (a == null)
      a = (ActivityManager)paramContext.getSystemService("activity"); 
    try {
      ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
      this();
      a.getMemoryInfo(memoryInfo);
      if (memoryInfo.lowMemory) {
        x.c("Memory is low.", new Object[0]);
        return true;
      } 
      bool = false;
    } catch (Throwable throwable) {}
    return bool;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/crashreport/common/info/AppInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */