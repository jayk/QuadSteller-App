package android.support.design.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

class ViewOffsetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
  private int mTempLeftRightOffset = 0;
  
  private int mTempTopBottomOffset = 0;
  
  private ViewOffsetHelper mViewOffsetHelper;
  
  public ViewOffsetBehavior() {}
  
  public ViewOffsetBehavior(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  public int getLeftAndRightOffset() {
    return (this.mViewOffsetHelper != null) ? this.mViewOffsetHelper.getLeftAndRightOffset() : 0;
  }
  
  public int getTopAndBottomOffset() {
    return (this.mViewOffsetHelper != null) ? this.mViewOffsetHelper.getTopAndBottomOffset() : 0;
  }
  
  protected void layoutChild(CoordinatorLayout paramCoordinatorLayout, V paramV, int paramInt) {
    paramCoordinatorLayout.onLayoutChild((View)paramV, paramInt);
  }
  
  public boolean onLayoutChild(CoordinatorLayout paramCoordinatorLayout, V paramV, int paramInt) {
    layoutChild(paramCoordinatorLayout, paramV, paramInt);
    if (this.mViewOffsetHelper == null)
      this.mViewOffsetHelper = new ViewOffsetHelper((View)paramV); 
    this.mViewOffsetHelper.onViewLayout();
    if (this.mTempTopBottomOffset != 0) {
      this.mViewOffsetHelper.setTopAndBottomOffset(this.mTempTopBottomOffset);
      this.mTempTopBottomOffset = 0;
    } 
    if (this.mTempLeftRightOffset != 0) {
      this.mViewOffsetHelper.setLeftAndRightOffset(this.mTempLeftRightOffset);
      this.mTempLeftRightOffset = 0;
    } 
    return true;
  }
  
  public boolean setLeftAndRightOffset(int paramInt) {
    if (this.mViewOffsetHelper != null)
      return this.mViewOffsetHelper.setLeftAndRightOffset(paramInt); 
    this.mTempLeftRightOffset = paramInt;
    return false;
  }
  
  public boolean setTopAndBottomOffset(int paramInt) {
    if (this.mViewOffsetHelper != null)
      return this.mViewOffsetHelper.setTopAndBottomOffset(paramInt); 
    this.mTempTopBottomOffset = paramInt;
    return false;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/ViewOffsetBehavior.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */