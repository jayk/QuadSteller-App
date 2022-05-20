package app.gamer.quadstellar.ui.widget.ptr;

import android.os.Build;
import android.view.View;
import android.widget.AbsListView;

public abstract class PtrDefaultHandler implements PtrHandler {
  public static boolean canChildScrollUp(View paramView) {
    AbsListView absListView;
    boolean bool = true;
    if (Build.VERSION.SDK_INT < 14) {
      if (paramView instanceof AbsListView) {
        absListView = (AbsListView)paramView;
        if (absListView.getChildCount() > 0) {
          boolean bool2 = bool;
          if (absListView.getFirstVisiblePosition() <= 0) {
            if (absListView.getChildAt(0).getTop() < absListView.getPaddingTop())
              return bool; 
          } else {
            return bool2;
          } 
        } 
        return false;
      } 
      boolean bool1 = bool;
      if (absListView.getScrollY() <= 0)
        bool1 = false; 
      return bool1;
    } 
    return absListView.canScrollVertically(-1);
  }
  
  public static boolean checkContentCanBePulledDown(PtrFrameLayout paramPtrFrameLayout, View paramView1, View paramView2) {
    return !canChildScrollUp(paramView1);
  }
  
  public boolean checkCanDoRefresh(PtrFrameLayout paramPtrFrameLayout, View paramView1, View paramView2) {
    return checkContentCanBePulledDown(paramPtrFrameLayout, paramView1, paramView2);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/ptr/PtrDefaultHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */