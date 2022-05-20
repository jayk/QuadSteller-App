package android.support.design.widget;

import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

class ViewGroupUtils {
  private static final ViewGroupUtilsImpl IMPL = new ViewGroupUtilsImplBase();
  
  static void getDescendantRect(ViewGroup paramViewGroup, View paramView, Rect paramRect) {
    paramRect.set(0, 0, paramView.getWidth(), paramView.getHeight());
    offsetDescendantRect(paramViewGroup, paramView, paramRect);
  }
  
  static void offsetDescendantRect(ViewGroup paramViewGroup, View paramView, Rect paramRect) {
    IMPL.offsetDescendantRect(paramViewGroup, paramView, paramRect);
  }
  
  static {
    if (Build.VERSION.SDK_INT >= 11) {
      IMPL = new ViewGroupUtilsImplHoneycomb();
      return;
    } 
  }
  
  private static interface ViewGroupUtilsImpl {
    void offsetDescendantRect(ViewGroup param1ViewGroup, View param1View, Rect param1Rect);
  }
  
  private static class ViewGroupUtilsImplBase implements ViewGroupUtilsImpl {
    public void offsetDescendantRect(ViewGroup param1ViewGroup, View param1View, Rect param1Rect) {
      param1ViewGroup.offsetDescendantRectToMyCoords(param1View, param1Rect);
      param1Rect.offset(param1View.getScrollX(), param1View.getScrollY());
    }
  }
  
  private static class ViewGroupUtilsImplHoneycomb implements ViewGroupUtilsImpl {
    public void offsetDescendantRect(ViewGroup param1ViewGroup, View param1View, Rect param1Rect) {
      ViewGroupUtilsHoneycomb.offsetDescendantRect(param1ViewGroup, param1View, param1Rect);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/ViewGroupUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */