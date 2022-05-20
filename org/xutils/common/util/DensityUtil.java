package org.xutils.common.util;

import org.xutils.x;

public final class DensityUtil {
  private static float density = -1.0F;
  
  private static int heightPixels;
  
  private static int widthPixels = -1;
  
  static {
    heightPixels = -1;
  }
  
  public static int dip2px(float paramFloat) {
    return (int)(getDensity() * paramFloat + 0.5F);
  }
  
  public static float getDensity() {
    if (density <= 0.0F)
      density = (x.app().getResources().getDisplayMetrics()).density; 
    return density;
  }
  
  public static int getScreenHeight() {
    if (heightPixels <= 0)
      heightPixels = (x.app().getResources().getDisplayMetrics()).heightPixels; 
    return heightPixels;
  }
  
  public static int getScreenWidth() {
    if (widthPixels <= 0)
      widthPixels = (x.app().getResources().getDisplayMetrics()).widthPixels; 
    return widthPixels;
  }
  
  public static int px2dip(float paramFloat) {
    return (int)(paramFloat / getDensity() + 0.5F);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/common/util/DensityUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */