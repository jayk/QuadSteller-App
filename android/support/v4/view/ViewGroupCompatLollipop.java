package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.ViewGroup;

@TargetApi(21)
@RequiresApi(21)
class ViewGroupCompatLollipop {
  public static int getNestedScrollAxes(ViewGroup paramViewGroup) {
    return paramViewGroup.getNestedScrollAxes();
  }
  
  public static boolean isTransitionGroup(ViewGroup paramViewGroup) {
    return paramViewGroup.isTransitionGroup();
  }
  
  public static void setTransitionGroup(ViewGroup paramViewGroup, boolean paramBoolean) {
    paramViewGroup.setTransitionGroup(paramBoolean);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/view/ViewGroupCompatLollipop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */