package app.gamer.quadstellar.ui.slide;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public interface MenuInterface {
  void drawFade(Canvas paramCanvas, int paramInt, CustomViewBehind paramCustomViewBehind, View paramView);
  
  void drawSelector(View paramView, Canvas paramCanvas, float paramFloat);
  
  void drawShadow(Canvas paramCanvas, Drawable paramDrawable, int paramInt);
  
  int getAbsLeftBound(CustomViewBehind paramCustomViewBehind, View paramView);
  
  int getAbsRightBound(CustomViewBehind paramCustomViewBehind, View paramView);
  
  int getMenuLeft(CustomViewBehind paramCustomViewBehind, View paramView);
  
  boolean marginTouchAllowed(View paramView, int paramInt1, int paramInt2);
  
  boolean menuClosedSlideAllowed(int paramInt);
  
  boolean menuOpenSlideAllowed(int paramInt);
  
  boolean menuOpenTouchAllowed(View paramView, int paramInt1, int paramInt2);
  
  boolean menuTouchInQuickReturn(View paramView, int paramInt1, int paramInt2);
  
  void scrollBehindTo(int paramInt1, int paramInt2, CustomViewBehind paramCustomViewBehind, float paramFloat);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/slide/MenuInterface.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */