package app.gamer.quadstellar.ui.widget.ptr;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class PtrLocalDisplay {
  public static float SCREEN_DENSITY;
  
  public static int SCREEN_HEIGHT_DP;
  
  public static int SCREEN_HEIGHT_PIXELS;
  
  public static int SCREEN_WIDTH_DP;
  
  public static int SCREEN_WIDTH_PIXELS;
  
  public static int designedDP2px(float paramFloat) {
    float f = paramFloat;
    if (SCREEN_WIDTH_DP != 320)
      f = SCREEN_WIDTH_DP * paramFloat / 320.0F; 
    return dp2px(f);
  }
  
  public static int dp2px(float paramFloat) {
    return (int)(paramFloat * SCREEN_DENSITY + 0.5F);
  }
  
  public static void init(Context paramContext) {
    if (paramContext != null) {
      DisplayMetrics displayMetrics = new DisplayMetrics();
      ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
      SCREEN_WIDTH_PIXELS = displayMetrics.widthPixels;
      SCREEN_HEIGHT_PIXELS = displayMetrics.heightPixels;
      SCREEN_DENSITY = displayMetrics.density;
      SCREEN_WIDTH_DP = (int)(SCREEN_WIDTH_PIXELS / displayMetrics.density);
      SCREEN_HEIGHT_DP = (int)(SCREEN_HEIGHT_PIXELS / displayMetrics.density);
    } 
  }
  
  public static void setPadding(View paramView, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    paramView.setPadding(designedDP2px(paramFloat1), dp2px(paramFloat2), designedDP2px(paramFloat3), dp2px(paramFloat4));
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/ptr/PtrLocalDisplay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */