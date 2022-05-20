package com.baoyz.swipemenulistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class SwipeMenuListView extends ListView {
  public static final int DIRECTION_LEFT = 1;
  
  public static final int DIRECTION_RIGHT = -1;
  
  private static final int TOUCH_STATE_NONE = 0;
  
  private static final int TOUCH_STATE_X = 1;
  
  private static final int TOUCH_STATE_Y = 2;
  
  private int MAX_X = 3;
  
  private int MAX_Y = 5;
  
  private Interpolator mCloseInterpolator;
  
  private int mDirection = 1;
  
  private float mDownX;
  
  private float mDownY;
  
  private SwipeMenuCreator mMenuCreator;
  
  private OnMenuItemClickListener mOnMenuItemClickListener;
  
  private OnMenuStateChangeListener mOnMenuStateChangeListener;
  
  private OnSwipeListener mOnSwipeListener;
  
  private Interpolator mOpenInterpolator;
  
  private int mTouchPosition;
  
  private int mTouchState;
  
  private SwipeMenuLayout mTouchView;
  
  public SwipeMenuListView(Context paramContext) {
    super(paramContext);
    init();
  }
  
  public SwipeMenuListView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init();
  }
  
  public SwipeMenuListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init();
  }
  
  private int dp2px(int paramInt) {
    return (int)TypedValue.applyDimension(1, paramInt, getContext().getResources().getDisplayMetrics());
  }
  
  private void init() {
    this.MAX_X = dp2px(this.MAX_X);
    this.MAX_Y = dp2px(this.MAX_Y);
    this.mTouchState = 0;
  }
  
  public Interpolator getCloseInterpolator() {
    return this.mCloseInterpolator;
  }
  
  public Interpolator getOpenInterpolator() {
    return this.mOpenInterpolator;
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    return super.onInterceptTouchEvent(paramMotionEvent);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    int i;
    View view;
    float f1;
    float f2;
    boolean bool = true;
    if (paramMotionEvent.getAction() != 0 && this.mTouchView == null)
      return super.onTouchEvent(paramMotionEvent); 
    switch (paramMotionEvent.getAction()) {
      default:
        return super.onTouchEvent(paramMotionEvent);
      case 0:
        i = this.mTouchPosition;
        this.mDownX = paramMotionEvent.getX();
        this.mDownY = paramMotionEvent.getY();
        this.mTouchState = 0;
        this.mTouchPosition = pointToPosition((int)paramMotionEvent.getX(), (int)paramMotionEvent.getY());
        if (this.mTouchPosition == i && this.mTouchView != null && this.mTouchView.isOpen()) {
          this.mTouchState = 1;
          this.mTouchView.onSwipe(paramMotionEvent);
          return bool;
        } 
        view = getChildAt(this.mTouchPosition - getFirstVisiblePosition());
        if (this.mTouchView != null && this.mTouchView.isOpen()) {
          this.mTouchView.smoothCloseMenu();
          this.mTouchView = null;
          paramMotionEvent = MotionEvent.obtain(paramMotionEvent);
          paramMotionEvent.setAction(3);
          onTouchEvent(paramMotionEvent);
          boolean bool1 = bool;
          if (this.mOnMenuStateChangeListener != null) {
            this.mOnMenuStateChangeListener.onMenuClose(i);
            bool1 = bool;
          } 
          return bool1;
        } 
        if (view instanceof SwipeMenuLayout) {
          this.mTouchView = (SwipeMenuLayout)view;
          this.mTouchView.setSwipeDirection(this.mDirection);
        } 
        if (this.mTouchView != null)
          this.mTouchView.onSwipe(paramMotionEvent); 
      case 2:
        f1 = Math.abs(paramMotionEvent.getY() - this.mDownY);
        f2 = Math.abs(paramMotionEvent.getX() - this.mDownX);
        if (this.mTouchState == 1) {
          if (this.mTouchView != null)
            this.mTouchView.onSwipe(paramMotionEvent); 
          getSelector().setState(new int[] { 0 });
          paramMotionEvent.setAction(3);
          super.onTouchEvent(paramMotionEvent);
          return bool;
        } 
        if (this.mTouchState == 0)
          if (Math.abs(f1) > this.MAX_Y) {
            this.mTouchState = 2;
          } else if (f2 > this.MAX_X) {
            this.mTouchState = 1;
            if (this.mOnSwipeListener != null)
              this.mOnSwipeListener.onSwipeStart(this.mTouchPosition); 
          }  
      case 1:
        break;
    } 
    if (this.mTouchState == 1) {
      if (this.mTouchView != null) {
        boolean bool1 = this.mTouchView.isOpen();
        this.mTouchView.onSwipe(paramMotionEvent);
        boolean bool2 = this.mTouchView.isOpen();
        if (bool1 != bool2 && this.mOnMenuStateChangeListener != null)
          if (bool2) {
            this.mOnMenuStateChangeListener.onMenuOpen(this.mTouchPosition);
          } else {
            this.mOnMenuStateChangeListener.onMenuClose(this.mTouchPosition);
          }  
        if (!bool2) {
          this.mTouchPosition = -1;
          this.mTouchView = null;
        } 
      } 
      if (this.mOnSwipeListener != null)
        this.mOnSwipeListener.onSwipeEnd(this.mTouchPosition); 
      paramMotionEvent.setAction(3);
      super.onTouchEvent(paramMotionEvent);
      return bool;
    } 
  }
  
  public void setAdapter(ListAdapter paramListAdapter) {
    super.setAdapter((ListAdapter)new SwipeMenuAdapter(getContext(), paramListAdapter) {
          public void createMenu(SwipeMenu param1SwipeMenu) {
            if (SwipeMenuListView.this.mMenuCreator != null)
              SwipeMenuListView.this.mMenuCreator.create(param1SwipeMenu); 
          }
          
          public void onItemClick(SwipeMenuView param1SwipeMenuView, SwipeMenu param1SwipeMenu, int param1Int) {
            boolean bool = false;
            if (SwipeMenuListView.this.mOnMenuItemClickListener != null)
              bool = SwipeMenuListView.this.mOnMenuItemClickListener.onMenuItemClick(param1SwipeMenuView.getPosition(), param1SwipeMenu, param1Int); 
            if (SwipeMenuListView.this.mTouchView != null && !bool)
              SwipeMenuListView.this.mTouchView.smoothCloseMenu(); 
          }
        });
  }
  
  public void setCloseInterpolator(Interpolator paramInterpolator) {
    this.mCloseInterpolator = paramInterpolator;
  }
  
  public void setMenuCreator(SwipeMenuCreator paramSwipeMenuCreator) {
    this.mMenuCreator = paramSwipeMenuCreator;
  }
  
  public void setOnMenuItemClickListener(OnMenuItemClickListener paramOnMenuItemClickListener) {
    this.mOnMenuItemClickListener = paramOnMenuItemClickListener;
  }
  
  public void setOnMenuStateChangeListener(OnMenuStateChangeListener paramOnMenuStateChangeListener) {
    this.mOnMenuStateChangeListener = paramOnMenuStateChangeListener;
  }
  
  public void setOnSwipeListener(OnSwipeListener paramOnSwipeListener) {
    this.mOnSwipeListener = paramOnSwipeListener;
  }
  
  public void setOpenInterpolator(Interpolator paramInterpolator) {
    this.mOpenInterpolator = paramInterpolator;
  }
  
  public void setSwipeDirection(int paramInt) {
    this.mDirection = paramInt;
  }
  
  public void smoothCloseMenu() {
    if (this.mTouchView != null && this.mTouchView.isOpen())
      this.mTouchView.smoothCloseMenu(); 
  }
  
  public void smoothOpenMenu(int paramInt) {
    if (paramInt >= getFirstVisiblePosition() && paramInt <= getLastVisiblePosition()) {
      View view = getChildAt(paramInt - getFirstVisiblePosition());
      if (view instanceof SwipeMenuLayout) {
        this.mTouchPosition = paramInt;
        if (this.mTouchView != null && this.mTouchView.isOpen())
          this.mTouchView.smoothCloseMenu(); 
        this.mTouchView = (SwipeMenuLayout)view;
        this.mTouchView.setSwipeDirection(this.mDirection);
        this.mTouchView.smoothOpenMenu();
      } 
    } 
  }
  
  public static interface OnMenuItemClickListener {
    boolean onMenuItemClick(int param1Int1, SwipeMenu param1SwipeMenu, int param1Int2);
  }
  
  public static interface OnMenuStateChangeListener {
    void onMenuClose(int param1Int);
    
    void onMenuOpen(int param1Int);
  }
  
  public static interface OnSwipeListener {
    void onSwipeEnd(int param1Int);
    
    void onSwipeStart(int param1Int);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/baoyz/swipemenulistview/SwipeMenuListView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */