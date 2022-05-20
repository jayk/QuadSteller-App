package app.gamer.quadstellar.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.zhy.android.percent.support.PercentRelativeLayout;

public class InterceptPercentRelativeLayout extends PercentRelativeLayout {
  private boolean childClickable = true;
  
  public InterceptPercentRelativeLayout(Context paramContext) {
    super(paramContext);
  }
  
  public InterceptPercentRelativeLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  public InterceptPercentRelativeLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    return !this.childClickable;
  }
  
  public void setChildClickable(boolean paramBoolean) {
    this.childClickable = paramBoolean;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/InterceptPercentRelativeLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */