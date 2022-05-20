package com.blankj.utilcode.util;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

public final class SizeUtils {
  private SizeUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static float applyDimension(int paramInt, float paramFloat, DisplayMetrics paramDisplayMetrics) {
    float f = paramFloat;
    switch (paramInt) {
      default:
        f = 0.0F;
      case 0:
        return f;
      case 1:
        f = paramFloat * paramDisplayMetrics.density;
      case 2:
        f = paramFloat * paramDisplayMetrics.scaledDensity;
      case 3:
        f = paramDisplayMetrics.xdpi * paramFloat * 0.013888889F;
      case 4:
        f = paramFloat * paramDisplayMetrics.xdpi;
      case 5:
        break;
    } 
    f = paramDisplayMetrics.xdpi * paramFloat * 0.03937008F;
  }
  
  public static int dp2px(float paramFloat) {
    return (int)(paramFloat * (Utils.getApp().getResources().getDisplayMetrics()).density + 0.5F);
  }
  
  public static void forceGetViewSize(final View view, final onGetSizeListener listener) {
    view.post(new Runnable() {
          public void run() {
            if (listener != null)
              listener.onGetSize(view); 
          }
        });
  }
  
  public static int getMeasuredHeight(View paramView) {
    return measureView(paramView)[1];
  }
  
  public static int getMeasuredWidth(View paramView) {
    return measureView(paramView)[0];
  }
  
  public static int[] measureView(View paramView) {
    ViewGroup.LayoutParams layoutParams1 = paramView.getLayoutParams();
    ViewGroup.LayoutParams layoutParams2 = layoutParams1;
    if (layoutParams1 == null)
      layoutParams2 = new ViewGroup.LayoutParams(-1, -2); 
    int i = ViewGroup.getChildMeasureSpec(0, 0, layoutParams2.width);
    int j = layoutParams2.height;
    if (j > 0) {
      j = View.MeasureSpec.makeMeasureSpec(j, 1073741824);
      paramView.measure(i, j);
      return new int[] { paramView.getMeasuredWidth(), paramView.getMeasuredHeight() };
    } 
    j = View.MeasureSpec.makeMeasureSpec(0, 0);
    paramView.measure(i, j);
    return new int[] { paramView.getMeasuredWidth(), paramView.getMeasuredHeight() };
  }
  
  public static int px2dp(float paramFloat) {
    return (int)(paramFloat / (Utils.getApp().getResources().getDisplayMetrics()).density + 0.5F);
  }
  
  public static int px2sp(float paramFloat) {
    return (int)(paramFloat / (Utils.getApp().getResources().getDisplayMetrics()).scaledDensity + 0.5F);
  }
  
  public static int sp2px(float paramFloat) {
    return (int)(paramFloat * (Utils.getApp().getResources().getDisplayMetrics()).scaledDensity + 0.5F);
  }
  
  public static interface onGetSizeListener {
    void onGetSize(View param1View);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/SizeUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */