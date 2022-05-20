package app.gamer.quadstellar.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.DisplayMetrics;

public final class DensityUtils {
  public static int dip2px(Context paramContext, float paramFloat) {
    return (int)(paramFloat * (paramContext.getResources().getDisplayMetrics()).density + 0.5F);
  }
  
  public static int getDialogW(Activity paramActivity) {
    new DisplayMetrics();
    return (paramActivity.getResources().getDisplayMetrics()).widthPixels - 100;
  }
  
  public static int getScreenH(Activity paramActivity) {
    new DisplayMetrics();
    return (paramActivity.getResources().getDisplayMetrics()).heightPixels;
  }
  
  public static Point getScreenMetrics(Context paramContext) {
    DisplayMetrics displayMetrics = paramContext.getResources().getDisplayMetrics();
    return new Point(displayMetrics.widthPixels, displayMetrics.heightPixels);
  }
  
  public static int getScreenW(Activity paramActivity) {
    new DisplayMetrics();
    return (paramActivity.getResources().getDisplayMetrics()).widthPixels;
  }
  
  public static void prepareMatrix(Matrix paramMatrix, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3) {
    float f;
    if (paramBoolean) {
      f = -1.0F;
    } else {
      f = 1.0F;
    } 
    paramMatrix.setScale(f, 1.0F);
    paramMatrix.postRotate(paramInt1);
    paramMatrix.postScale(paramInt2 / 2000.0F, paramInt3 / 2000.0F);
    paramMatrix.postTranslate(paramInt2 / 2.0F, paramInt3 / 2.0F);
  }
  
  public static int px2dip(Context paramContext, float paramFloat) {
    return (int)(paramFloat / (paramContext.getResources().getDisplayMetrics()).density + 0.5F);
  }
  
  public static int px2sp(Context paramContext, float paramFloat) {
    return (int)(paramFloat / (paramContext.getResources().getDisplayMetrics()).scaledDensity + 0.5F);
  }
  
  public static int sp2px(Context paramContext, float paramFloat) {
    return (int)(paramFloat * (paramContext.getResources().getDisplayMetrics()).scaledDensity + 0.5F);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/utils/DensityUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */