package com.blankj.utilcode.util;

import android.app.ActivityManager;
import android.support.annotation.NonNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class ProcessUtils {
  private ProcessUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static Set<String> getAllBackgroundProcesses() {
    List list = ((ActivityManager)Utils.getApp().getSystemService("activity")).getRunningAppProcesses();
    HashSet<? super String> hashSet = new HashSet();
    Iterator iterator = list.iterator();
    while (iterator.hasNext())
      Collections.addAll(hashSet, ((ActivityManager.RunningAppProcessInfo)iterator.next()).pkgList); 
    return (Set)hashSet;
  }
  
  public static String getForegroundProcessName() {
    // Byte code:
    //   0: invokestatic getApp : ()Landroid/app/Application;
    //   3: ldc 'activity'
    //   5: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   8: checkcast android/app/ActivityManager
    //   11: invokevirtual getRunningAppProcesses : ()Ljava/util/List;
    //   14: astore_0
    //   15: aload_0
    //   16: ifnull -> 70
    //   19: aload_0
    //   20: invokeinterface size : ()I
    //   25: ifeq -> 70
    //   28: aload_0
    //   29: invokeinterface iterator : ()Ljava/util/Iterator;
    //   34: astore_1
    //   35: aload_1
    //   36: invokeinterface hasNext : ()Z
    //   41: ifeq -> 70
    //   44: aload_1
    //   45: invokeinterface next : ()Ljava/lang/Object;
    //   50: checkcast android/app/ActivityManager$RunningAppProcessInfo
    //   53: astore_0
    //   54: aload_0
    //   55: getfield importance : I
    //   58: bipush #100
    //   60: if_icmpne -> 35
    //   63: aload_0
    //   64: getfield processName : Ljava/lang/String;
    //   67: astore_0
    //   68: aload_0
    //   69: areturn
    //   70: getstatic android/os/Build$VERSION.SDK_INT : I
    //   73: bipush #21
    //   75: if_icmple -> 320
    //   78: invokestatic getApp : ()Landroid/app/Application;
    //   81: invokevirtual getPackageManager : ()Landroid/content/pm/PackageManager;
    //   84: astore_2
    //   85: new android/content/Intent
    //   88: dup
    //   89: ldc 'android.settings.USAGE_ACCESS_SETTINGS'
    //   91: invokespecial <init> : (Ljava/lang/String;)V
    //   94: astore_0
    //   95: aload_2
    //   96: aload_0
    //   97: ldc 65536
    //   99: invokevirtual queryIntentActivities : (Landroid/content/Intent;I)Ljava/util/List;
    //   102: astore_1
    //   103: getstatic java/lang/System.out : Ljava/io/PrintStream;
    //   106: aload_1
    //   107: invokevirtual println : (Ljava/lang/Object;)V
    //   110: aload_1
    //   111: invokeinterface size : ()I
    //   116: ifle -> 325
    //   119: aload_2
    //   120: invokestatic getApp : ()Landroid/app/Application;
    //   123: invokevirtual getPackageName : ()Ljava/lang/String;
    //   126: iconst_0
    //   127: invokevirtual getApplicationInfo : (Ljava/lang/String;I)Landroid/content/pm/ApplicationInfo;
    //   130: astore_2
    //   131: invokestatic getApp : ()Landroid/app/Application;
    //   134: ldc 'appops'
    //   136: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   139: checkcast android/app/AppOpsManager
    //   142: astore_1
    //   143: aload_1
    //   144: ldc 'android:get_usage_stats'
    //   146: aload_2
    //   147: getfield uid : I
    //   150: aload_2
    //   151: getfield packageName : Ljava/lang/String;
    //   154: invokevirtual checkOpNoThrow : (Ljava/lang/String;ILjava/lang/String;)I
    //   157: ifeq -> 167
    //   160: invokestatic getApp : ()Landroid/app/Application;
    //   163: aload_0
    //   164: invokevirtual startActivity : (Landroid/content/Intent;)V
    //   167: aload_1
    //   168: ldc 'android:get_usage_stats'
    //   170: aload_2
    //   171: getfield uid : I
    //   174: aload_2
    //   175: getfield packageName : Ljava/lang/String;
    //   178: invokevirtual checkOpNoThrow : (Ljava/lang/String;ILjava/lang/String;)I
    //   181: ifeq -> 203
    //   184: ldc 'getForegroundApp'
    //   186: iconst_1
    //   187: anewarray java/lang/Object
    //   190: dup
    //   191: iconst_0
    //   192: ldc '没有打开"有权查看使用权限的应用"选项'
    //   194: aastore
    //   195: invokestatic d : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   198: aconst_null
    //   199: astore_0
    //   200: goto -> 68
    //   203: invokestatic getApp : ()Landroid/app/Application;
    //   206: ldc 'usagestats'
    //   208: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   211: checkcast android/app/usage/UsageStatsManager
    //   214: astore_0
    //   215: invokestatic currentTimeMillis : ()J
    //   218: lstore_3
    //   219: aload_0
    //   220: iconst_4
    //   221: lload_3
    //   222: ldc2_w 604800000
    //   225: lsub
    //   226: lload_3
    //   227: invokevirtual queryUsageStats : (IJJ)Ljava/util/List;
    //   230: astore_1
    //   231: aload_1
    //   232: ifnull -> 244
    //   235: aload_1
    //   236: invokeinterface isEmpty : ()Z
    //   241: ifeq -> 249
    //   244: aconst_null
    //   245: astore_0
    //   246: goto -> 68
    //   249: aconst_null
    //   250: astore_0
    //   251: aload_1
    //   252: invokeinterface iterator : ()Ljava/util/Iterator;
    //   257: astore_2
    //   258: aload_2
    //   259: invokeinterface hasNext : ()Z
    //   264: ifeq -> 298
    //   267: aload_2
    //   268: invokeinterface next : ()Ljava/lang/Object;
    //   273: checkcast android/app/usage/UsageStats
    //   276: astore_1
    //   277: aload_0
    //   278: ifnull -> 293
    //   281: aload_1
    //   282: invokevirtual getLastTimeUsed : ()J
    //   285: aload_0
    //   286: invokevirtual getLastTimeUsed : ()J
    //   289: lcmp
    //   290: ifle -> 258
    //   293: aload_1
    //   294: astore_0
    //   295: goto -> 258
    //   298: aload_0
    //   299: ifnonnull -> 307
    //   302: aconst_null
    //   303: astore_0
    //   304: goto -> 68
    //   307: aload_0
    //   308: invokevirtual getPackageName : ()Ljava/lang/String;
    //   311: astore_0
    //   312: goto -> 68
    //   315: astore_0
    //   316: aload_0
    //   317: invokevirtual printStackTrace : ()V
    //   320: aconst_null
    //   321: astore_0
    //   322: goto -> 68
    //   325: ldc 'ProcessUtils'
    //   327: ldc 'getForegroundProcessName() called: 无"有权查看使用权限的应用"选项'
    //   329: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   332: pop
    //   333: goto -> 320
    // Exception table:
    //   from	to	target	type
    //   119	167	315	android/content/pm/PackageManager$NameNotFoundException
    //   167	198	315	android/content/pm/PackageManager$NameNotFoundException
    //   203	231	315	android/content/pm/PackageManager$NameNotFoundException
    //   235	244	315	android/content/pm/PackageManager$NameNotFoundException
    //   251	258	315	android/content/pm/PackageManager$NameNotFoundException
    //   258	277	315	android/content/pm/PackageManager$NameNotFoundException
    //   281	293	315	android/content/pm/PackageManager$NameNotFoundException
    //   307	312	315	android/content/pm/PackageManager$NameNotFoundException
  }
  
  public static Set<String> killAllBackgroundProcesses() {
    ActivityManager activityManager = (ActivityManager)Utils.getApp().getSystemService("activity");
    List list = activityManager.getRunningAppProcesses();
    HashSet<String> hashSet = new HashSet();
    Iterator iterator2 = list.iterator();
    while (iterator2.hasNext()) {
      for (String str : ((ActivityManager.RunningAppProcessInfo)iterator2.next()).pkgList) {
        activityManager.killBackgroundProcesses(str);
        hashSet.add(str);
      } 
    } 
    Iterator iterator1 = activityManager.getRunningAppProcesses().iterator();
    while (iterator1.hasNext()) {
      String[] arrayOfString = ((ActivityManager.RunningAppProcessInfo)iterator1.next()).pkgList;
      int i = arrayOfString.length;
      for (byte b = 0; b < i; b++)
        hashSet.remove(arrayOfString[b]); 
    } 
    return hashSet;
  }
  
  public static boolean killBackgroundProcesses(@NonNull String paramString) {
    boolean bool1 = true;
    ActivityManager activityManager = (ActivityManager)Utils.getApp().getSystemService("activity");
    List list2 = activityManager.getRunningAppProcesses();
    boolean bool2 = bool1;
    if (list2 != null) {
      if (list2.size() == 0)
        return bool1; 
    } else {
      return bool2;
    } 
    Iterator iterator = list2.iterator();
    while (iterator.hasNext()) {
      if (Arrays.<String>asList(((ActivityManager.RunningAppProcessInfo)iterator.next()).pkgList).contains(paramString))
        activityManager.killBackgroundProcesses(paramString); 
    } 
    List list1 = activityManager.getRunningAppProcesses();
    bool2 = bool1;
    if (list1 != null) {
      bool2 = bool1;
      if (list1.size() != 0) {
        Iterator iterator1 = list1.iterator();
        while (true) {
          bool2 = bool1;
          if (iterator1.hasNext()) {
            if (Arrays.<String>asList(((ActivityManager.RunningAppProcessInfo)iterator1.next()).pkgList).contains(paramString))
              return false; 
            continue;
          } 
          return bool2;
        } 
      } 
    } 
    return bool2;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/ProcessUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */