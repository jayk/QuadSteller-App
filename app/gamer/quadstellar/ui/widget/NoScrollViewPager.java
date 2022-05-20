package app.gamer.quadstellar.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollViewPager extends ViewPager {
  public NoScrollViewPager(Context paramContext) {
    super(paramContext);
  }
  
  public NoScrollViewPager(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    return false;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    return false;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/NoScrollViewPager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */