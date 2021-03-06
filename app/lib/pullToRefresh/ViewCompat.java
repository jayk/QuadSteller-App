package app.lib.pullToRefresh;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

public class ViewCompat {
  public static void postOnAnimation(View paramView, Runnable paramRunnable) {
    if (Build.VERSION.SDK_INT >= 16) {
      SDK16.postOnAnimation(paramView, paramRunnable);
      return;
    } 
    paramView.postDelayed(paramRunnable, 16L);
  }
  
  public static void setBackground(View paramView, Drawable paramDrawable) {
    if (Build.VERSION.SDK_INT >= 16) {
      SDK16.setBackground(paramView, paramDrawable);
      return;
    } 
    paramView.setBackgroundDrawable(paramDrawable);
  }
  
  public static void setLayerType(View paramView, int paramInt) {
    if (Build.VERSION.SDK_INT >= 11)
      SDK11.setLayerType(paramView, paramInt); 
  }
  
  @TargetApi(11)
  static class SDK11 {
    public static void setLayerType(View param1View, int param1Int) {}
  }
  
  @TargetApi(16)
  static class SDK16 {
    public static void postOnAnimation(View param1View, Runnable param1Runnable) {
      param1View.postDelayed(param1Runnable, 16L);
    }
    
    public static void setBackground(View param1View, Drawable param1Drawable) {
      param1View.setBackgroundDrawable(param1Drawable);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/pullToRefresh/ViewCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */