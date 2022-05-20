package android.support.transition;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;

@TargetApi(14)
@RequiresApi(14)
class ViewGroupOverlay extends ViewOverlay {
  ViewGroupOverlay(Context paramContext, ViewGroup paramViewGroup, View paramView) {
    super(paramContext, paramViewGroup, paramView);
  }
  
  public static ViewGroupOverlay createFrom(ViewGroup paramViewGroup) {
    return (ViewGroupOverlay)ViewOverlay.createFrom((View)paramViewGroup);
  }
  
  public void add(View paramView) {
    this.mOverlayViewGroup.add(paramView);
  }
  
  public void remove(View paramView) {
    this.mOverlayViewGroup.remove(paramView);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/ViewGroupOverlay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */