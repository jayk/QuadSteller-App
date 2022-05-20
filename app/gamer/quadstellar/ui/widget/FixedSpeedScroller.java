package app.gamer.quadstellar.ui.widget;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class FixedSpeedScroller extends Scroller {
  private int mDuration = 0;
  
  public FixedSpeedScroller(Context paramContext) {
    super(paramContext);
  }
  
  public FixedSpeedScroller(Context paramContext, Interpolator paramInterpolator) {
    super(paramContext, paramInterpolator);
  }
  
  public FixedSpeedScroller(Context paramContext, Interpolator paramInterpolator, boolean paramBoolean) {
    super(paramContext, paramInterpolator, paramBoolean);
  }
  
  public void startScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.startScroll(paramInt1, paramInt2, paramInt3, paramInt4, this.mDuration);
  }
  
  public void startScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    super.startScroll(paramInt1, paramInt2, paramInt3, paramInt4, this.mDuration);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/FixedSpeedScroller.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */