package android.support.v7.widget;

import android.view.View;

class ScrollbarHelper {
  static int computeScrollExtent(RecyclerView.State paramState, OrientationHelper paramOrientationHelper, View paramView1, View paramView2, RecyclerView.LayoutManager paramLayoutManager, boolean paramBoolean) {
    if (paramLayoutManager.getChildCount() == 0 || paramState.getItemCount() == 0 || paramView1 == null || paramView2 == null)
      return 0; 
    if (!paramBoolean)
      return Math.abs(paramLayoutManager.getPosition(paramView1) - paramLayoutManager.getPosition(paramView2)) + 1; 
    int i = paramOrientationHelper.getDecoratedEnd(paramView2);
    null = paramOrientationHelper.getDecoratedStart(paramView1);
    return Math.min(paramOrientationHelper.getTotalSpace(), i - null);
  }
  
  static int computeScrollOffset(RecyclerView.State paramState, OrientationHelper paramOrientationHelper, View paramView1, View paramView2, RecyclerView.LayoutManager paramLayoutManager, boolean paramBoolean1, boolean paramBoolean2) {
    int i = 0;
    int j = i;
    if (paramLayoutManager.getChildCount() != 0) {
      j = i;
      if (paramState.getItemCount() != 0) {
        j = i;
        if (paramView1 != null) {
          if (paramView2 == null)
            return i; 
        } else {
          return j;
        } 
      } else {
        return j;
      } 
    } else {
      return j;
    } 
    i = Math.min(paramLayoutManager.getPosition(paramView1), paramLayoutManager.getPosition(paramView2));
    j = Math.max(paramLayoutManager.getPosition(paramView1), paramLayoutManager.getPosition(paramView2));
    if (paramBoolean2) {
      i = Math.max(0, paramState.getItemCount() - j - 1);
    } else {
      i = Math.max(0, i);
    } 
    j = i;
    if (paramBoolean1) {
      j = Math.abs(paramOrientationHelper.getDecoratedEnd(paramView2) - paramOrientationHelper.getDecoratedStart(paramView1));
      int k = Math.abs(paramLayoutManager.getPosition(paramView1) - paramLayoutManager.getPosition(paramView2));
      float f = j / (k + 1);
      j = Math.round(i * f + (paramOrientationHelper.getStartAfterPadding() - paramOrientationHelper.getDecoratedStart(paramView1)));
    } 
    return j;
  }
  
  static int computeScrollRange(RecyclerView.State paramState, OrientationHelper paramOrientationHelper, View paramView1, View paramView2, RecyclerView.LayoutManager paramLayoutManager, boolean paramBoolean) {
    if (paramLayoutManager.getChildCount() == 0 || paramState.getItemCount() == 0 || paramView1 == null || paramView2 == null)
      return 0; 
    if (!paramBoolean)
      return paramState.getItemCount(); 
    null = paramOrientationHelper.getDecoratedEnd(paramView2);
    int i = paramOrientationHelper.getDecoratedStart(paramView1);
    int j = Math.abs(paramLayoutManager.getPosition(paramView1) - paramLayoutManager.getPosition(paramView2));
    return (int)((null - i) / (j + 1) * paramState.getItemCount());
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/ScrollbarHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */