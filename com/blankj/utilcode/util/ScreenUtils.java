package com.blankj.utilcode.util;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;

public final class ScreenUtils {
  private ScreenUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static float getScreenDensity() {
    return (Utils.getApp().getResources().getDisplayMetrics()).density;
  }
  
  public static int getScreenDensityDpi() {
    return (Utils.getApp().getResources().getDisplayMetrics()).densityDpi;
  }
  
  public static int getScreenHeight() {
    return (Utils.getApp().getResources().getDisplayMetrics()).heightPixels;
  }
  
  public static int getScreenRotation(@NonNull Activity paramActivity) {
    switch (paramActivity.getWindowManager().getDefaultDisplay().getRotation()) {
      default:
        return 0;
      case 1:
        return 90;
      case 2:
        return 180;
      case 3:
        break;
    } 
    return 270;
  }
  
  public static int getScreenWidth() {
    return (Utils.getApp().getResources().getDisplayMetrics()).widthPixels;
  }
  
  public static int getSleepDuration() {
    byte b;
    try {
      b = Settings.System.getInt(Utils.getApp().getContentResolver(), "screen_off_timeout");
    } catch (android.provider.Settings.SettingNotFoundException settingNotFoundException) {
      settingNotFoundException.printStackTrace();
      b = -123;
    } 
    return b;
  }
  
  public static boolean isLandscape() {
    return ((Utils.getApp().getResources().getConfiguration()).orientation == 2);
  }
  
  public static boolean isPortrait() {
    boolean bool = true;
    if ((Utils.getApp().getResources().getConfiguration()).orientation != 1)
      bool = false; 
    return bool;
  }
  
  public static boolean isScreenLock() {
    return ((KeyguardManager)Utils.getApp().getSystemService("keyguard")).inKeyguardRestrictedInputMode();
  }
  
  public static boolean isTablet() {
    return (((Utils.getApp().getResources().getConfiguration()).screenLayout & 0xF) >= 3);
  }
  
  public static Bitmap screenShot(@NonNull Activity paramActivity) {
    return screenShot(paramActivity, false);
  }
  
  public static Bitmap screenShot(@NonNull Activity paramActivity, boolean paramBoolean) {
    View view = paramActivity.getWindow().getDecorView();
    view.setDrawingCacheEnabled(true);
    view.buildDrawingCache();
    Bitmap bitmap2 = view.getDrawingCache();
    DisplayMetrics displayMetrics = new DisplayMetrics();
    paramActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    if (paramBoolean) {
      Resources resources = paramActivity.getResources();
      int i = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"));
      Bitmap bitmap = Bitmap.createBitmap(bitmap2, 0, i, displayMetrics.widthPixels, displayMetrics.heightPixels - i);
      view.destroyDrawingCache();
      return bitmap;
    } 
    Bitmap bitmap1 = Bitmap.createBitmap(bitmap2, 0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
    view.destroyDrawingCache();
    return bitmap1;
  }
  
  public static void setFullScreen(@NonNull Activity paramActivity) {
    paramActivity.requestWindowFeature(1);
    paramActivity.getWindow().setFlags(1024, 1024);
  }
  
  public static void setLandscape(@NonNull Activity paramActivity) {
    paramActivity.setRequestedOrientation(0);
  }
  
  public static void setPortrait(@NonNull Activity paramActivity) {
    paramActivity.setRequestedOrientation(1);
  }
  
  public static void setSleepDuration(int paramInt) {
    Settings.System.putInt(Utils.getApp().getContentResolver(), "screen_off_timeout", paramInt);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/ScreenUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */