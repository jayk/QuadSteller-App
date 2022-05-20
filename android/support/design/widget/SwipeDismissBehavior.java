package android.support.design.widget;

import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class SwipeDismissBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
  private static final float DEFAULT_ALPHA_END_DISTANCE = 0.5F;
  
  private static final float DEFAULT_ALPHA_START_DISTANCE = 0.0F;
  
  private static final float DEFAULT_DRAG_DISMISS_THRESHOLD = 0.5F;
  
  public static final int STATE_DRAGGING = 1;
  
  public static final int STATE_IDLE = 0;
  
  public static final int STATE_SETTLING = 2;
  
  public static final int SWIPE_DIRECTION_ANY = 2;
  
  public static final int SWIPE_DIRECTION_END_TO_START = 1;
  
  public static final int SWIPE_DIRECTION_START_TO_END = 0;
  
  float mAlphaEndSwipeDistance = 0.5F;
  
  float mAlphaStartSwipeDistance = 0.0F;
  
  private final ViewDragHelper.Callback mDragCallback = new ViewDragHelper.Callback() {
      private static final int INVALID_POINTER_ID = -1;
      
      private int mActivePointerId = -1;
      
      private int mOriginalCapturedViewLeft;
      
      private boolean shouldDismiss(View param1View, float param1Float) {
        boolean bool = true;
        if (param1Float != 0.0F) {
          boolean bool1;
          if (ViewCompat.getLayoutDirection(param1View) == 1) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          if (SwipeDismissBehavior.this.mSwipeDirection != 2) {
            if (SwipeDismissBehavior.this.mSwipeDirection == 0) {
              if (bool1) {
                if (param1Float >= 0.0F)
                  bool = false; 
                return bool;
              } 
              if (param1Float <= 0.0F)
                bool = false; 
              return bool;
            } 
            if (SwipeDismissBehavior.this.mSwipeDirection == 1) {
              if (bool1) {
                if (param1Float <= 0.0F)
                  bool = false; 
                return bool;
              } 
              if (param1Float >= 0.0F)
                bool = false; 
              return bool;
            } 
            bool = false;
          } 
          return bool;
        } 
        int j = param1View.getLeft();
        int i = this.mOriginalCapturedViewLeft;
        int k = Math.round(param1View.getWidth() * SwipeDismissBehavior.this.mDragDismissThreshold);
        if (Math.abs(j - i) < k)
          bool = false; 
        return bool;
      }
      
      public int clampViewPositionHorizontal(View param1View, int param1Int1, int param1Int2) {
        if (ViewCompat.getLayoutDirection(param1View) == 1) {
          param1Int2 = 1;
        } else {
          param1Int2 = 0;
        } 
        if (SwipeDismissBehavior.this.mSwipeDirection == 0) {
          if (param1Int2 != 0) {
            int k = this.mOriginalCapturedViewLeft - param1View.getWidth();
            param1Int2 = this.mOriginalCapturedViewLeft;
            return SwipeDismissBehavior.clamp(k, param1Int1, param1Int2);
          } 
          int j = this.mOriginalCapturedViewLeft;
          param1Int2 = this.mOriginalCapturedViewLeft + param1View.getWidth();
          return SwipeDismissBehavior.clamp(j, param1Int1, param1Int2);
        } 
        if (SwipeDismissBehavior.this.mSwipeDirection == 1) {
          if (param1Int2 != 0) {
            int k = this.mOriginalCapturedViewLeft;
            param1Int2 = this.mOriginalCapturedViewLeft + param1View.getWidth();
            return SwipeDismissBehavior.clamp(k, param1Int1, param1Int2);
          } 
          int j = this.mOriginalCapturedViewLeft - param1View.getWidth();
          param1Int2 = this.mOriginalCapturedViewLeft;
          return SwipeDismissBehavior.clamp(j, param1Int1, param1Int2);
        } 
        int i = this.mOriginalCapturedViewLeft - param1View.getWidth();
        param1Int2 = this.mOriginalCapturedViewLeft + param1View.getWidth();
        return SwipeDismissBehavior.clamp(i, param1Int1, param1Int2);
      }
      
      public int clampViewPositionVertical(View param1View, int param1Int1, int param1Int2) {
        return param1View.getTop();
      }
      
      public int getViewHorizontalDragRange(View param1View) {
        return param1View.getWidth();
      }
      
      public void onViewCaptured(View param1View, int param1Int) {
        this.mActivePointerId = param1Int;
        this.mOriginalCapturedViewLeft = param1View.getLeft();
        ViewParent viewParent = param1View.getParent();
        if (viewParent != null)
          viewParent.requestDisallowInterceptTouchEvent(true); 
      }
      
      public void onViewDragStateChanged(int param1Int) {
        if (SwipeDismissBehavior.this.mListener != null)
          SwipeDismissBehavior.this.mListener.onDragStateChanged(param1Int); 
      }
      
      public void onViewPositionChanged(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
        float f1 = this.mOriginalCapturedViewLeft + param1View.getWidth() * SwipeDismissBehavior.this.mAlphaStartSwipeDistance;
        float f2 = this.mOriginalCapturedViewLeft + param1View.getWidth() * SwipeDismissBehavior.this.mAlphaEndSwipeDistance;
        if (param1Int1 <= f1) {
          ViewCompat.setAlpha(param1View, 1.0F);
          return;
        } 
        if (param1Int1 >= f2) {
          ViewCompat.setAlpha(param1View, 0.0F);
          return;
        } 
        ViewCompat.setAlpha(param1View, SwipeDismissBehavior.clamp(0.0F, 1.0F - SwipeDismissBehavior.fraction(f1, f2, param1Int1), 1.0F));
      }
      
      public void onViewReleased(View param1View, float param1Float1, float param1Float2) {
        this.mActivePointerId = -1;
        int i = param1View.getWidth();
        boolean bool = false;
        if (shouldDismiss(param1View, param1Float1)) {
          if (param1View.getLeft() < this.mOriginalCapturedViewLeft) {
            i = this.mOriginalCapturedViewLeft - i;
          } else {
            i = this.mOriginalCapturedViewLeft + i;
          } 
          bool = true;
        } else {
          i = this.mOriginalCapturedViewLeft;
        } 
        if (SwipeDismissBehavior.this.mViewDragHelper.settleCapturedViewAt(i, param1View.getTop())) {
          ViewCompat.postOnAnimation(param1View, new SwipeDismissBehavior.SettleRunnable(param1View, bool));
          return;
        } 
        if (bool && SwipeDismissBehavior.this.mListener != null)
          SwipeDismissBehavior.this.mListener.onDismiss(param1View); 
      }
      
      public boolean tryCaptureView(View param1View, int param1Int) {
        return (this.mActivePointerId == -1 && SwipeDismissBehavior.this.canSwipeDismissView(param1View));
      }
    };
  
  float mDragDismissThreshold = 0.5F;
  
  private boolean mInterceptingEvents;
  
  OnDismissListener mListener;
  
  private float mSensitivity = 0.0F;
  
  private boolean mSensitivitySet;
  
  int mSwipeDirection = 2;
  
  ViewDragHelper mViewDragHelper;
  
  static float clamp(float paramFloat1, float paramFloat2, float paramFloat3) {
    return Math.min(Math.max(paramFloat1, paramFloat2), paramFloat3);
  }
  
  static int clamp(int paramInt1, int paramInt2, int paramInt3) {
    return Math.min(Math.max(paramInt1, paramInt2), paramInt3);
  }
  
  private void ensureViewDragHelper(ViewGroup paramViewGroup) {
    if (this.mViewDragHelper == null) {
      ViewDragHelper viewDragHelper;
      if (this.mSensitivitySet) {
        viewDragHelper = ViewDragHelper.create(paramViewGroup, this.mSensitivity, this.mDragCallback);
      } else {
        viewDragHelper = ViewDragHelper.create((ViewGroup)viewDragHelper, this.mDragCallback);
      } 
      this.mViewDragHelper = viewDragHelper;
    } 
  }
  
  static float fraction(float paramFloat1, float paramFloat2, float paramFloat3) {
    return (paramFloat3 - paramFloat1) / (paramFloat2 - paramFloat1);
  }
  
  public boolean canSwipeDismissView(@NonNull View paramView) {
    return true;
  }
  
  public int getDragState() {
    return (this.mViewDragHelper != null) ? this.mViewDragHelper.getViewDragState() : 0;
  }
  
  public boolean onInterceptTouchEvent(CoordinatorLayout paramCoordinatorLayout, V paramV, MotionEvent paramMotionEvent) {
    boolean bool1 = false;
    boolean bool2 = this.mInterceptingEvents;
    boolean bool3 = bool2;
    switch (MotionEventCompat.getActionMasked(paramMotionEvent)) {
      default:
        bool3 = bool2;
      case 2:
        if (bool3) {
          ensureViewDragHelper(paramCoordinatorLayout);
          bool1 = this.mViewDragHelper.shouldInterceptTouchEvent(paramMotionEvent);
        } 
        return bool1;
      case 0:
        this.mInterceptingEvents = paramCoordinatorLayout.isPointInChildBounds((View)paramV, (int)paramMotionEvent.getX(), (int)paramMotionEvent.getY());
        bool3 = this.mInterceptingEvents;
      case 1:
      case 3:
        break;
    } 
    this.mInterceptingEvents = false;
    bool3 = bool2;
  }
  
  public boolean onTouchEvent(CoordinatorLayout paramCoordinatorLayout, V paramV, MotionEvent paramMotionEvent) {
    if (this.mViewDragHelper != null) {
      this.mViewDragHelper.processTouchEvent(paramMotionEvent);
      return true;
    } 
    return false;
  }
  
  public void setDragDismissDistance(float paramFloat) {
    this.mDragDismissThreshold = clamp(0.0F, paramFloat, 1.0F);
  }
  
  public void setEndAlphaSwipeDistance(float paramFloat) {
    this.mAlphaEndSwipeDistance = clamp(0.0F, paramFloat, 1.0F);
  }
  
  public void setListener(OnDismissListener paramOnDismissListener) {
    this.mListener = paramOnDismissListener;
  }
  
  public void setSensitivity(float paramFloat) {
    this.mSensitivity = paramFloat;
    this.mSensitivitySet = true;
  }
  
  public void setStartAlphaSwipeDistance(float paramFloat) {
    this.mAlphaStartSwipeDistance = clamp(0.0F, paramFloat, 1.0F);
  }
  
  public void setSwipeDirection(int paramInt) {
    this.mSwipeDirection = paramInt;
  }
  
  public static interface OnDismissListener {
    void onDismiss(View param1View);
    
    void onDragStateChanged(int param1Int);
  }
  
  private class SettleRunnable implements Runnable {
    private final boolean mDismiss;
    
    private final View mView;
    
    SettleRunnable(View param1View, boolean param1Boolean) {
      this.mView = param1View;
      this.mDismiss = param1Boolean;
    }
    
    public void run() {
      if (SwipeDismissBehavior.this.mViewDragHelper != null && SwipeDismissBehavior.this.mViewDragHelper.continueSettling(true)) {
        ViewCompat.postOnAnimation(this.mView, this);
        return;
      } 
      if (this.mDismiss && SwipeDismissBehavior.this.mListener != null)
        SwipeDismissBehavior.this.mListener.onDismiss(this.mView); 
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/SwipeDismissBehavior.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */