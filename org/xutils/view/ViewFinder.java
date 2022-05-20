package org.xutils.view;

import android.app.Activity;
import android.view.View;

final class ViewFinder {
  private Activity activity;
  
  private View view;
  
  public ViewFinder(Activity paramActivity) {
    this.activity = paramActivity;
  }
  
  public ViewFinder(View paramView) {
    this.view = paramView;
  }
  
  public View findViewById(int paramInt) {
    return (this.view != null) ? this.view.findViewById(paramInt) : ((this.activity != null) ? this.activity.findViewById(paramInt) : null);
  }
  
  public View findViewById(int paramInt1, int paramInt2) {
    null = null;
    if (paramInt2 > 0)
      null = findViewById(paramInt2); 
    return (null != null) ? null.findViewById(paramInt1) : findViewById(paramInt1);
  }
  
  public View findViewByInfo(ViewInfo paramViewInfo) {
    return findViewById(paramViewInfo.value, paramViewInfo.parentId);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/view/ViewFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */