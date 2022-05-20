package app.lib.slideexpandable;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

public class ExpandCollapseAnimation extends Animation {
  public static final int COLLAPSE = 1;
  
  public static final int EXPAND = 0;
  
  private View mAnimatedView;
  
  private int mEndHeight;
  
  private LinearLayout.LayoutParams mLayoutParams;
  
  private int mType;
  
  public ExpandCollapseAnimation(View paramView, int paramInt) {
    this.mAnimatedView = paramView;
    this.mEndHeight = this.mAnimatedView.getMeasuredHeight();
    this.mLayoutParams = (LinearLayout.LayoutParams)paramView.getLayoutParams();
    this.mType = paramInt;
    if (this.mType == 0) {
      this.mLayoutParams.bottomMargin = -this.mEndHeight;
    } else {
      this.mLayoutParams.bottomMargin = 0;
    } 
    paramView.setVisibility(0);
  }
  
  protected void applyTransformation(float paramFloat, Transformation paramTransformation) {
    super.applyTransformation(paramFloat, paramTransformation);
    if (paramFloat < 1.0F) {
      if (this.mType == 0) {
        this.mLayoutParams.bottomMargin = -this.mEndHeight + (int)(this.mEndHeight * paramFloat);
      } else {
        this.mLayoutParams.bottomMargin = -((int)(this.mEndHeight * paramFloat));
      } 
      Log.d("ExpandCollapseAnimation", "anim height " + this.mLayoutParams.bottomMargin);
      this.mAnimatedView.requestLayout();
      return;
    } 
    if (this.mType == 0) {
      this.mLayoutParams.bottomMargin = 0;
      this.mAnimatedView.requestLayout();
      return;
    } 
    this.mLayoutParams.bottomMargin = -this.mEndHeight;
    this.mAnimatedView.setVisibility(8);
    this.mAnimatedView.requestLayout();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/slideexpandable/ExpandCollapseAnimation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */