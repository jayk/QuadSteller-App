package com.blankj.utilcode.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class AppUtils {
  private AppUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static boolean cleanAppData(File... paramVarArgs) {
    boolean bool;
    int i = CleanUtils.cleanInternalCache() & CleanUtils.cleanInternalDbs() & CleanUtils.cleanInternalSP() & CleanUtils.cleanInternalFiles() & CleanUtils.cleanExternalCache();
    int j = paramVarArgs.length;
    for (byte b = 0; b < j; b++)
      bool = i & CleanUtils.cleanCustomCache(paramVarArgs[b]); 
    return bool;
  }
  
  public static boolean cleanAppData(String... paramVarArgs) {
    File[] arrayOfFile = new File[paramVarArgs.length];
    int i = paramVarArgs.length;
    byte b1 = 0;
    for (byte b2 = 0; b1 < i; b2++) {
      arrayOfFile[b2] = new File(paramVarArgs[b1]);
      b1++;
    } 
    return cleanAppData(arrayOfFile);
  }
  
  public static void exitApp() {
    List<Activity> list = Utils.sActivityList;
    for (int i = list.size() - 1; i >= 0; i--) {
      ((Activity)list.get(i)).finish();
      list.remove(i);
    } 
    System.exit(0);
  }
  
  public static void getAppDetailsSettings() {
    getAppDetailsSettings(Utils.getApp().getPackageName());
  }
  
  public static void getAppDetailsSettings(String paramString) {
    if (!isSpace(paramString))
      Utils.getApp().startActivity(IntentUtils.getAppDetailsSettingsIntent(paramString)); 
  }
  
  public static Drawable getAppIcon() {
    return getAppIcon(Utils.getApp().getPackageName());
  }
  
  public static Drawable getAppIcon(String paramString) {
    String str1;
    String str2 = null;
    if (isSpace(paramString))
      return (Drawable)str2; 
    try {
      PackageManager packageManager = Utils.getApp().getPackageManager();
      PackageInfo packageInfo = packageManager.getPackageInfo(paramString, 0);
      paramString = str2;
      if (packageInfo != null)
        Drawable drawable = packageInfo.applicationInfo.loadIcon(packageManager); 
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      nameNotFoundException.printStackTrace();
      str1 = str2;
    } 
    return (Drawable)str1;
  }
  
  public static AppInfo getAppInfo() {
    return getAppInfo(Utils.getApp().getPackageName());
  }
  
  public static AppInfo getAppInfo(String paramString) {
    try {
      PackageManager packageManager = Utils.getApp().getPackageManager();
      AppInfo appInfo = getBean(packageManager, packageManager.getPackageInfo(paramString, 0));
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      nameNotFoundException.printStackTrace();
      nameNotFoundException = null;
    } 
    return (AppInfo)nameNotFoundException;
  }
  
  public static String getAppName() {
    return getAppName(Utils.getApp().getPackageName());
  }
  
  public static String getAppName(String paramString) {
    String str1;
    String str2 = null;
    if (isSpace(paramString))
      return str2; 
    try {
      PackageManager packageManager = Utils.getApp().getPackageManager();
      PackageInfo packageInfo = packageManager.getPackageInfo(paramString, 0);
      paramString = str2;
      if (packageInfo != null)
        paramString = packageInfo.applicationInfo.loadLabel(packageManager).toString(); 
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      nameNotFoundException.printStackTrace();
      str1 = str2;
    } 
    return str1;
  }
  
  public static String getAppPackageName() {
    return Utils.getApp().getPackageName();
  }
  
  public static String getAppPath() {
    return getAppPath(Utils.getApp().getPackageName());
  }
  
  public static String getAppPath(String paramString) {
    String str1;
    String str2 = null;
    if (isSpace(paramString))
      return str2; 
    try {
      PackageInfo packageInfo = Utils.getApp().getPackageManager().getPackageInfo(paramString, 0);
      paramString = str2;
      if (packageInfo != null)
        paramString = packageInfo.applicationInfo.sourceDir; 
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      nameNotFoundException.printStackTrace();
      str1 = str2;
    } 
    return str1;
  }
  
  public static Signature[] getAppSignature() {
    return getAppSignature(Utils.getApp().getPackageName());
  }
  
  public static Signature[] getAppSignature(String paramString) {
    String str1;
    String str2 = null;
    if (isSpace(paramString))
      return (Signature[])str2; 
    try {
      PackageInfo packageInfo = Utils.getApp().getPackageManager().getPackageInfo(paramString, 64);
      paramString = str2;
      if (packageInfo != null)
        Signature[] arrayOfSignature = packageInfo.signatures; 
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      nameNotFoundException.printStackTrace();
      str1 = str2;
    } 
    return (Signature[])str1;
  }
  
  public static String getAppSignatureSHA1() {
    return getAppSignatureSHA1(Utils.getApp().getPackageName());
  }
  
  public static String getAppSignatureSHA1(String paramString) {
    Signature[] arrayOfSignature = getAppSignature(paramString);
    return (arrayOfSignature == null) ? null : EncryptUtils.encryptSHA1ToString(arrayOfSignature[0].toByteArray()).replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
  }
  
  public static int getAppVersionCode() {
    return getAppVersionCode(Utils.getApp().getPackageName());
  }
  
  public static int getAppVersionCode(String paramString) {
    byte b2;
    byte b1 = -1;
    if (isSpace(paramString))
      return b1; 
    try {
      PackageInfo packageInfo = Utils.getApp().getPackageManager().getPackageInfo(paramString, 0);
      b2 = b1;
      if (packageInfo != null)
        b2 = packageInfo.versionCode; 
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      nameNotFoundException.printStackTrace();
      b2 = b1;
    } 
    return b2;
  }
  
  public static String getAppVersionName() {
    return getAppVersionName(Utils.getApp().getPackageName());
  }
  
  public static String getAppVersionName(String paramString) {
    String str1;
    String str2 = null;
    if (isSpace(paramString))
      return str2; 
    try {
      PackageInfo packageInfo = Utils.getApp().getPackageManager().getPackageInfo(paramString, 0);
      paramString = str2;
      if (packageInfo != null)
        paramString = packageInfo.versionName; 
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      nameNotFoundException.printStackTrace();
      str1 = str2;
    } 
    return str1;
  }
  
  public static List<AppInfo> getAppsInfo() {
    ArrayList<AppInfo> arrayList = new ArrayList();
    PackageManager packageManager = Utils.getApp().getPackageManager();
    Iterator<PackageInfo> iterator = packageManager.getInstalledPackages(0).iterator();
    while (iterator.hasNext()) {
      AppInfo appInfo = getBean(packageManager, iterator.next());
      if (appInfo != null)
        arrayList.add(appInfo); 
    } 
    return arrayList;
  }
  
  private static AppInfo getBean(PackageManager paramPackageManager, PackageInfo paramPackageInfo) {
    boolean bool;
    if (paramPackageManager == null || paramPackageInfo == null)
      return null; 
    ApplicationInfo applicationInfo = paramPackageInfo.applicationInfo;
    String str2 = paramPackageInfo.packageName;
    String str3 = applicationInfo.loadLabel(paramPackageManager).toString();
    Drawable drawable = applicationInfo.loadIcon(paramPackageManager);
    String str4 = applicationInfo.sourceDir;
    String str1 = paramPackageInfo.versionName;
    int i = paramPackageInfo.versionCode;
    if ((applicationInfo.flags & 0x1) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return new AppInfo(str2, str3, drawable, str4, str1, i, bool);
  }
  
  public static void installApp(Activity paramActivity, File paramFile, String paramString, int paramInt) {
    if (FileUtils.isFileExists(paramFile))
      paramActivity.startActivityForResult(IntentUtils.getInstallAppIntent(paramFile, paramString), paramInt); 
  }
  
  public static void installApp(Activity paramActivity, String paramString1, String paramString2, int paramInt) {
    installApp(paramActivity, FileUtils.getFileByPath(paramString1), paramString2, paramInt);
  }
  
  public static void installApp(File paramFile, String paramString) {
    if (FileUtils.isFileExists(paramFile))
      Utils.getApp().startActivity(IntentUtils.getInstallAppIntent(paramFile, paramString)); 
  }
  
  public static void installApp(String paramString1, String paramString2) {
    installApp(FileUtils.getFileByPath(paramString1), paramString2);
  }
  
  public static boolean installAppSilent(String paramString) {
    boolean bool1 = true;
    boolean bool2 = false;
    if (FileUtils.isFileExists(FileUtils.getFileByPath(paramString))) {
      paramString = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install " + paramString;
      if (!isSystemApp()) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      ShellUtils.CommandResult commandResult = ShellUtils.execCmd(paramString, bool2, true);
      if (commandResult.successMsg != null && commandResult.successMsg.toLowerCase().contains("success"))
        return bool1; 
      bool2 = false;
    } 
    return bool2;
  }
  
  public static boolean isAppDebug() {
    return isAppDebug(Utils.getApp().getPackageName());
  }
  
  public static boolean isAppDebug(String paramString) {
    boolean bool2;
    boolean bool1 = false;
    if (isSpace(paramString))
      return bool1; 
    try {
      ApplicationInfo applicationInfo = Utils.getApp().getPackageManager().getApplicationInfo(paramString, 0);
      bool2 = bool1;
      if (applicationInfo != null) {
        int i = applicationInfo.flags;
        bool2 = bool1;
        if ((i & 0x2) != 0)
          bool2 = true; 
      } 
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      nameNotFoundException.printStackTrace();
      bool2 = bool1;
    } 
    return bool2;
  }
  
  public static boolean isAppForeground() {
    boolean bool1 = false;
    List list = ((ActivityManager)Utils.getApp().getSystemService("activity")).getRunningAppProcesses();
    boolean bool2 = bool1;
    if (list != null) {
      if (list.size() == 0)
        return bool1; 
    } else {
      return bool2;
    } 
    Iterator<ActivityManager.RunningAppProcessInfo> iterator = list.iterator();
    while (true) {
      bool2 = bool1;
      if (iterator.hasNext()) {
        ActivityManager.RunningAppProcessInfo runningAppProcessInfo = iterator.next();
        if (runningAppProcessInfo.importance == 100)
          return runningAppProcessInfo.processName.equals(Utils.getApp().getPackageName()); 
        continue;
      } 
      return bool2;
    } 
  }
  
  public static boolean isAppForeground(String paramString) {
    return (!isSpace(paramString) && paramString.equals(ProcessUtils.getForegroundProcessName()));
  }
  
  public static boolean isAppRoot() {
    boolean bool = true;
    ShellUtils.CommandResult commandResult = ShellUtils.execCmd("echo root", true);
    if (commandResult.result != 0) {
      if (commandResult.errorMsg != null)
        Log.d("AppUtils", "isAppRoot() called" + commandResult.errorMsg); 
      bool = false;
    } 
    return bool;
  }
  
  public static boolean isInstallApp(String paramString) {
    return (!isSpace(paramString) && IntentUtils.getLaunchAppIntent(paramString) != null);
  }
  
  public static boolean isInstallApp(String paramString1, String paramString2) {
    boolean bool = false;
    Intent intent = new Intent(paramString1);
    intent.addCategory(paramString2);
    if (Utils.getApp().getPackageManager().resolveActivity(intent, 0) != null)
      bool = true; 
    return bool;
  }
  
  private static boolean isSpace(String paramString) {
    boolean bool = true;
    if (paramString == null)
      return bool; 
    byte b = 0;
    int i = paramString.length();
    while (true) {
      boolean bool1 = bool;
      if (b < i) {
        if (!Character.isWhitespace(paramString.charAt(b)))
          return false; 
        b++;
        continue;
      } 
      return bool1;
    } 
  }
  
  public static boolean isSystemApp() {
    return isSystemApp(Utils.getApp().getPackageName());
  }
  
  public static boolean isSystemApp(String paramString) {
    boolean bool2;
    boolean bool1 = false;
    if (isSpace(paramString))
      return bool1; 
    try {
      ApplicationInfo applicationInfo = Utils.getApp().getPackageManager().getApplicationInfo(paramString, 0);
      bool2 = bool1;
      if (applicationInfo != null) {
        int i = applicationInfo.flags;
        bool2 = bool1;
        if ((i & 0x1) != 0)
          bool2 = true; 
      } 
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      nameNotFoundException.printStackTrace();
      bool2 = bool1;
    } 
    return bool2;
  }
  
  public static void launchApp(Activity paramActivity, String paramString, int paramInt) {
    if (!isSpace(paramString))
      paramActivity.startActivityForResult(IntentUtils.getLaunchAppIntent(paramString), paramInt); 
  }
  
  public static void launchApp(String paramString) {
    if (!isSpace(paramString))
      Utils.getApp().startActivity(IntentUtils.getLaunchAppIntent(paramString)); 
  }
  
  public static void uninstallApp(Activity paramActivity, String paramString, int paramInt) {
    if (!isSpace(paramString))
      paramActivity.startActivityForResult(IntentUtils.getUninstallAppIntent(paramString), paramInt); 
  }
  
  public static void uninstallApp(String paramString) {
    if (!isSpace(paramString))
      Utils.getApp().startActivity(IntentUtils.getUninstallAppIntent(paramString)); 
  }
  
  public static boolean uninstallAppSilent(String paramString, boolean paramBoolean) {
    String str;
    boolean bool1 = true;
    boolean bool2 = false;
    if (isSpace(paramString))
      return bool2; 
    StringBuilder stringBuilder = (new StringBuilder()).append("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm uninstall ");
    if (paramBoolean) {
      str = "-k ";
    } else {
      str = "";
    } 
    paramString = stringBuilder.append(str).append(paramString).toString();
    if (!isSystemApp()) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    } 
    ShellUtils.CommandResult commandResult = ShellUtils.execCmd(paramString, paramBoolean, true);
    return (commandResult.successMsg != null && commandResult.successMsg.toLowerCase().contains("success")) ? bool1 : false;
  }
  
  public static class AppInfo {
    private Drawable icon;
    
    private boolean isSystem;
    
    private String name;
    
    private String packageName;
    
    private String packagePath;
    
    private int versionCode;
    
    private String versionName;
    
    public AppInfo(String param1String1, String param1String2, Drawable param1Drawable, String param1String3, String param1String4, int param1Int, boolean param1Boolean) {
      setName(param1String2);
      setIcon(param1Drawable);
      setPackageName(param1String1);
      setPackagePath(param1String3);
      setVersionName(param1String4);
      setVersionCode(param1Int);
      setSystem(param1Boolean);
    }
    
    public Drawable getIcon() {
      return this.icon;
    }
    
    public String getName() {
      return this.name;
    }
    
    public String getPackageName() {
      return this.packageName;
    }
    
    public String getPackagePath() {
      return this.packagePath;
    }
    
    public int getVersionCode() {
      return this.versionCode;
    }
    
    public String getVersionName() {
      return this.versionName;
    }
    
    public boolean isSystem() {
      return this.isSystem;
    }
    
    public void setIcon(Drawable param1Drawable) {
      this.icon = param1Drawable;
    }
    
    public void setName(String param1String) {
      this.name = param1String;
    }
    
    public void setPackageName(String param1String) {
      this.packageName = param1String;
    }
    
    public void setPackagePath(String param1String) {
      this.packagePath = param1String;
    }
    
    public void setSystem(boolean param1Boolean) {
      this.isSystem = param1Boolean;
    }
    
    public void setVersionCode(int param1Int) {
      this.versionCode = param1Int;
    }
    
    public void setVersionName(String param1String) {
      this.versionName = param1String;
    }
    
    public String toString() {
      return "pkg name: " + getPackageName() + "\napp name: " + getName() + "\napp path: " + getPackagePath() + "\napp v name: " + getVersionName() + "\napp v code: " + getVersionCode() + "\nis system: " + isSystem();
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/AppUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */