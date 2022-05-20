package android.support.design.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

abstract class HeaderBehavior<V extends View> extends ViewOffsetBehavior<V> {
  private static final int INVALID_POINTER = -1;
  
  private int mActivePointerId = -1;
  
  private Runnable mFlingRunnable;
  
  private boolean mIsBeingDragged;
  
  private int mLastMotionY;
  
  ScrollerCompat mScroller;
  
  private int mTouchSlop = -1;
  
  private VelocityTracker mVelocityTracker;
  
  public HeaderBehavior() {}
  
  public HeaderBehavior(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  private void ensureVelocityTracker() {
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
  }
  
  boolean canDragView(V paramV) {
    return false;
  }
  
  final boolean fling(CoordinatorLayout paramCoordinatorLayout, V paramV, int paramInt1, int paramInt2, float paramFloat) {
    boolean bool = false;
    if (this.mFlingRunnable != null) {
      paramV.removeCallbacks(this.mFlingRunnable);
      this.mFlingRunnable = null;
    } 
    if (this.mScroller == null)
      this.mScroller = ScrollerCompat.create(paramV.getContext()); 
    this.mScroller.fling(0, getTopAndBottomOffset(), 0, Math.round(paramFloat), 0, 0, paramInt1, paramInt2);
    if (this.mScroller.computeScrollOffset()) {
      this.mFlingRunnable = new FlingRunnable(paramCoordinatorLayout, paramV);
      ViewCompat.postOnAnimation((View)paramV, this.mFlingRunnable);
      return true;
    } 
    onFlingFinished(paramCoordinatorLayout, paramV);
    return bool;
  }
  
  int getMaxDragOffset(V paramV) {
    return -paramV.getHeight();
  }
  
  int getScrollRangeForDragFling(V paramV) {
    return paramV.getHeight();
  }
  
  int getTopBottomOffsetForScrollingSibling() {
    return getTopAndBottomOffset();
  }
  
  void onFlingFinished(CoordinatorLayout paramCoordinatorLayout, V paramV) {}
  
  public boolean onInterceptTouchEvent(CoordinatorLayout paramCoordinatorLayout, V paramV, MotionEvent paramMotionEvent) {
    boolean bool = true;
    if (this.mTouchSlop < 0)
      this.mTouchSlop = ViewConfiguration.get(paramCoordinatorLayout.getContext()).getScaledTouchSlop(); 
    if (paramMotionEvent.getAction() != 2 || !this.mIsBeingDragged) {
      int i;
      int j;
      switch (MotionEventCompat.getActionMasked(paramMotionEvent)) {
        default:
          if (this.mVelocityTracker != null)
            this.mVelocityTracker.addMovement(paramMotionEvent); 
          return this.mIsBeingDragged;
        case 0:
          this.mIsBeingDragged = false;
          i = (int)paramMotionEvent.getX();
          j = (int)paramMotionEvent.getY();
          if (canDragView(paramV) && paramCoordinatorLayout.isPointInChildBounds((View)paramV, i, j)) {
            this.mLastMotionY = j;
            this.mActivePointerId = paramMotionEvent.getPointerId(0);
            ensureVelocityTracker();
          } 
        case 2:
          j = this.mActivePointerId;
          if (j != -1) {
            j = paramMotionEvent.findPointerIndex(j);
            if (j != -1) {
              j = (int)paramMotionEvent.getY(j);
              if (Math.abs(j - this.mLastMotionY) > this.mTouchSlop) {
                this.mIsBeingDragged = true;
                this.mLastMotionY = j;
              } 
            } 
          } 
        case 1:
        case 3:
          break;
      } 
      this.mIsBeingDragged = false;
      this.mActivePointerId = -1;
      if (this.mVelocityTracker != null) {
        this.mVelocityTracker.recycle();
        this.mVelocityTracker = null;
      } 
    } 
    return bool;
  }
  
  public boolean onTouchEvent(CoordinatorLayout paramCoordinatorLayout, V paramV, MotionEvent paramMotionEvent) {
    int i;
    int j;
    int k;
    if (this.mTouchSlop < 0)
      this.mTouchSlop = ViewConfiguration.get(paramCoordinatorLayout.getContext()).getScaledTouchSlop(); 
    switch (MotionEventCompat.getActionMasked(paramMotionEvent)) {
      default:
        if (this.mVelocityTracker != null)
          this.mVelocityTracker.addMovement(paramMotionEvent); 
        return true;
      case 0:
        i = (int)paramMotionEvent.getX();
        j = (int)paramMotionEvent.getY();
        if (paramCoordinatorLayout.isPointInChildBounds((View)paramV, i, j) && canDragView(paramV)) {
          this.mLastMotionY = j;
          this.mActivePointerId = paramMotionEvent.getPointerId(0);
          ensureVelocityTracker();
        } else {
          return false;
        } 
      case 2:
        j = paramMotionEvent.findPointerIndex(this.mActivePointerId);
        if (j == -1)
          return false; 
        k = (int)paramMotionEvent.getY(j);
        i = this.mLastMotionY - k;
        j = i;
        if (!this.mIsBeingDragged) {
          j = i;
          if (Math.abs(i) > this.mTouchSlop) {
            this.mIsBeingDragged = true;
            if (i > 0) {
              j = i - this.mTouchSlop;
            } else {
              j = i + this.mTouchSlop;
            } 
          } 
        } 
        if (this.mIsBeingDragged) {
          this.mLastMotionY = k;
          scroll(paramCoordinatorLayout, paramV, j, getMaxDragOffset(paramV), 0);
        } 
      case 1:
        if (this.mVelocityTracker != null) {
          this.mVelocityTracker.addMovement(paramMotionEvent);
          this.mVelocityTracker.computeCurrentVelocity(1000);
          float f = VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId);
          fling(paramCoordinatorLayout, paramV, -getScrollRangeForDragFling(paramV), 0, f);
        } 
        break;
      case 3:
        break;
    } 
    this.mIsBeingDragged = false;
    this.mActivePointerId = -1;
    if (this.mVelocityTracker != null) {
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
    } 
  }
  
  final int scroll(CoordinatorLayout paramCoordinatorLayout, V paramV, int paramInt1, int paramInt2, int paramInt3) {
    return setHeaderTopBottomOffset(paramCoordinatorLayout, paramV, getTopBottomOffsetForScrollingSibling() - paramInt1, paramInt2, paramInt3);
  }
  
  int setHeaderTopBottomOffset(CoordinatorLayout paramCoordinatorLayout, V paramV, int paramInt) {
    return setHeaderTopBottomOffset(paramCoordinatorLayout, paramV, paramInt, -2147483648, 2147483647);
  }
  
  int setHeaderTopBottomOffset(CoordinatorLayout paramCoordinatorLayout, V paramV, int paramInt1, int paramInt2, int paramInt3) {
    int i = getTopAndBottomOffset();
    byte b = 0;
    int j = b;
    if (paramInt2 != 0) {
      j = b;
      if (i >= paramInt2) {
        j = b;
        if (i <= paramInt3) {
          paramInt1 = MathUtils.constrain(paramInt1, paramInt2, paramInt3);
          j = b;
          if (i != paramInt1) {
            setTopAndBottomOffset(paramInt1);
            j = i - paramInt1;
          } 
        } 
      } 
    } 
    return j;
  }
  
  private class FlingRunnable implements Runnable {
    private final V mLayout;
    
    private final CoordinatorLayout mParent;
    
    FlingRunnable(CoordinatorLayout param1CoordinatorLayout, V param1V) {
      this.mParent = param1CoordinatorLayout;
      this.mLayout = param1V;
    }
    
    public void run() {
      if (this.mLayout != null && HeaderBehavior.this.mScroller != null) {
        if (HeaderBehavior.this.mScroller.computeScrollOffset()) {
          HeaderBehavior.this.setHeaderTopBottomOffset(this.mParent, this.mLayout, HeaderBehavior.this.mScroller.getCurrY());
          ViewCompat.postOnAnimation((View)this.mLayout, this);
          return;
        } 
      } else {
        return;
      } 
      HeaderBehavior.this.onFlingFinished(this.mParent, this.mLayout);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/HeaderBehavior.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */