package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.ScaleGestureDetector;

@TargetApi(19)
@RequiresApi(19)
class ScaleGestureDetectorCompatKitKat {
  public static boolean isQuickScaleEnabled(Object paramObject) {
    return ((ScaleGestureDetector)paramObject).isQuickScaleEnabled();
  }
  
  public static void setQuickScaleEnabled(Object paramObject, boolean paramBoolean) {
    ((ScaleGestureDetector)paramObject).setQuickScaleEnabled(paramBoolean);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/view/ScaleGestureDetectorCompatKitKat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */