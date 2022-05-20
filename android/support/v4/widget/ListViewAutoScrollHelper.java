package android.support.v4.widget;

import android.view.View;
import android.widget.ListView;

public class ListViewAutoScrollHelper extends AutoScrollHelper {
  private final ListView mTarget;
  
  public ListViewAutoScrollHelper(ListView paramListView) {
    super((View)paramListView);
    this.mTarget = paramListView;
  }
  
  public boolean canTargetScrollHorizontally(int paramInt) {
    return false;
  }
  
  public boolean canTargetScrollVertically(int paramInt) {
    boolean bool = false;
    ListView listView = this.mTarget;
    int i = listView.getCount();
    if (i == 0)
      return bool; 
    int j = listView.getChildCount();
    int k = listView.getFirstVisiblePosition();
    if (paramInt > 0) {
      if (k + j >= i) {
        boolean bool1 = bool;
        return (listView.getChildAt(j - 1).getBottom() > listView.getHeight()) ? true : bool1;
      } 
    } else {
      boolean bool1 = bool;
      if (paramInt < 0) {
        if (k <= 0 && listView.getChildAt(0).getTop() >= 0)
          return bool; 
      } else {
        return bool1;
      } 
    } 
    return true;
  }
  
  public void scrollTargetBy(int paramInt1, int paramInt2) {
    ListViewCompat.scrollListBy(this.mTarget, paramInt2);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/widget/ListViewAutoScrollHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */