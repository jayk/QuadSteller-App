package app.gamer.quadstellar.newdevices.view;

import android.content.Context;
import android.view.WindowManager;

public class ScreenUtils {
  private static Context sContext;
  
  public static int dp2px(float paramFloat) {
    return (int)(paramFloat * (sContext.getResources().getDisplayMetrics()).density + 0.5F);
  }
  
  public static int getScreenWidth() {
    return ((WindowManager)sContext.getSystemService("window")).getDefaultDisplay().getWidth();
  }
  
  public static int getSystemBarHeight() {
    int i = 0;
    int j = sContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (j > 0)
      i = sContext.getResources().getDimensionPixelSize(j); 
    return i;
  }
  
  public static void init(Context paramContext) {
    sContext = paramContext.getApplicationContext();
  }
  
  public static int px2dp(float paramFloat) {
    return (int)(paramFloat / (sContext.getResources().getDisplayMetrics()).density + 0.5F);
  }
  
  public static int sp2px(float paramFloat) {
    return (int)(paramFloat * (sContext.getResources().getDisplayMetrics()).scaledDensity + 0.5F);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/view/ScreenUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */