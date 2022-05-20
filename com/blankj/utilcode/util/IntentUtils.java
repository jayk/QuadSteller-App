package com.blankj.utilcode.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.FileProvider;
import java.io.File;

public final class IntentUtils {
  private IntentUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static Intent getAppDetailsSettingsIntent(String paramString) {
    Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
    intent.setData(Uri.parse("package:" + paramString));
    return intent.addFlags(268435456);
  }
  
  public static Intent getCallIntent(String paramString) {
    return (new Intent("android.intent.action.CALL", Uri.parse("tel:" + paramString))).addFlags(268435456);
  }
  
  public static Intent getCaptureIntent(Uri paramUri) {
    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
    intent.putExtra("output", (Parcelable)paramUri);
    return intent.addFlags(268435457);
  }
  
  public static Intent getComponentIntent(String paramString1, String paramString2) {
    return getComponentIntent(paramString1, paramString2, null);
  }
  
  public static Intent getComponentIntent(String paramString1, String paramString2, Bundle paramBundle) {
    Intent intent = new Intent("android.intent.action.VIEW");
    if (paramBundle != null)
      intent.putExtras(paramBundle); 
    intent.setComponent(new ComponentName(paramString1, paramString2));
    return intent.addFlags(268435456);
  }
  
  public static Intent getDialIntent(String paramString) {
    return (new Intent("android.intent.action.DIAL", Uri.parse("tel:" + paramString))).addFlags(268435456);
  }
  
  public static Intent getInstallAppIntent(File paramFile, String paramString) {
    Uri uri;
    if (paramFile == null)
      return null; 
    Intent intent = new Intent("android.intent.action.VIEW");
    if (Build.VERSION.SDK_INT < 24) {
      uri = Uri.fromFile(paramFile);
    } else {
      intent.setFlags(1);
      uri = FileProvider.getUriForFile((Context)Utils.getApp(), paramString, (File)uri);
    } 
    intent.setDataAndType(uri, "application/vnd.android.package-archive");
    return intent.addFlags(268435456);
  }
  
  public static Intent getInstallAppIntent(String paramString1, String paramString2) {
    return getInstallAppIntent(FileUtils.getFileByPath(paramString1), paramString2);
  }
  
  public static Intent getLaunchAppIntent(String paramString) {
    return Utils.getApp().getPackageManager().getLaunchIntentForPackage(paramString);
  }
  
  public static Intent getSendSmsIntent(String paramString1, String paramString2) {
    Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + paramString1));
    intent.putExtra("sms_body", paramString2);
    return intent.addFlags(268435456);
  }
  
  public static Intent getShareImageIntent(String paramString, Uri paramUri) {
    Intent intent = new Intent("android.intent.action.SEND");
    intent.putExtra("android.intent.extra.TEXT", paramString);
    intent.putExtra("android.intent.extra.STREAM", (Parcelable)paramUri);
    intent.setType("image/*");
    return intent.setFlags(268435456);
  }
  
  public static Intent getShareImageIntent(String paramString, File paramFile) {
    return !FileUtils.isFileExists(paramFile) ? null : getShareImageIntent(paramString, Uri.fromFile(paramFile));
  }
  
  public static Intent getShareImageIntent(String paramString1, String paramString2) {
    return getShareImageIntent(paramString1, FileUtils.getFileByPath(paramString2));
  }
  
  public static Intent getShareTextIntent(String paramString) {
    Intent intent = new Intent("android.intent.action.SEND");
    intent.setType("text/plain");
    intent.putExtra("android.intent.extra.TEXT", paramString);
    return intent.setFlags(268435456);
  }
  
  public static Intent getShutdownIntent() {
    return (new Intent("android.intent.action.ACTION_SHUTDOWN")).addFlags(268435456);
  }
  
  public static Intent getUninstallAppIntent(String paramString) {
    Intent intent = new Intent("android.intent.action.DELETE");
    intent.setData(Uri.parse("package:" + paramString));
    return intent.addFlags(268435456);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/IntentUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */