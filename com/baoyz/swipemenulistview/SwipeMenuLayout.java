package com.baoyz.swipemenulistview;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;

public class SwipeMenuLayout extends FrameLayout {
  private static final int CONTENT_VIEW_ID = 1;
  
  private static final int MENU_VIEW_ID = 2;
  
  private static final int STATE_CLOSE = 0;
  
  private static final int STATE_OPEN = 1;
  
  private int MAX_VELOCITYX = -dp2px(500);
  
  private int MIN_FLING = dp2px(15);
  
  private boolean isFling;
  
  private int mBaseX;
  
  private Interpolator mCloseInterpolator;
  
  private ScrollerCompat mCloseScroller;
  
  private View mContentView;
  
  private int mDownX;
  
  private GestureDetectorCompat mGestureDetector;
  
  private GestureDetector.OnGestureListener mGestureListener;
  
  private SwipeMenuView mMenuView;
  
  private Interpolator mOpenInterpolator;
  
  private ScrollerCompat mOpenScroller;
  
  private int mSwipeDirection;
  
  private int position;
  
  private int state = 0;
  
  private SwipeMenuLayout(Context paramContext) {
    super(paramContext);
  }
  
  private SwipeMenuLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  public SwipeMenuLayout(View paramView, SwipeMenuView paramSwipeMenuView) {
    this(paramView, paramSwipeMenuView, (Interpolator)null, (Interpolator)null);
  }
  
  public SwipeMenuLayout(View paramView, SwipeMenuView paramSwipeMenuView, Interpolator paramInterpolator1, Interpolator paramInterpolator2) {
    super(paramView.getContext());
    this.mCloseInterpolator = paramInterpolator1;
    this.mOpenInterpolator = paramInterpolator2;
    this.mContentView = paramView;
    this.mMenuView = paramSwipeMenuView;
    this.mMenuView.setLayout(this);
    init();
  }
  
  private int dp2px(int paramInt) {
    return (int)TypedValue.applyDimension(1, paramInt, getContext().getResources().getDisplayMetrics());
  }
  
  private void init() {
    setLayoutParams((ViewGroup.LayoutParams)new AbsListView.LayoutParams(-1, -2));
    this.mGestureListener = (GestureDetector.OnGestureListener)new GestureDetector.SimpleOnGestureListener() {
        public boolean onDown(MotionEvent param1MotionEvent) {
          SwipeMenuLayout.access$002(SwipeMenuLayout.this, false);
          return true;
        }
        
        public boolean onFling(MotionEvent param1MotionEvent1, MotionEvent param1MotionEvent2, float param1Float1, float param1Float2) {
          if (Math.abs(param1MotionEvent1.getX() - param1MotionEvent2.getX()) > SwipeMenuLayout.this.MIN_FLING && param1Float1 < SwipeMenuLayout.this.MAX_VELOCITYX)
            SwipeMenuLayout.access$002(SwipeMenuLayout.this, true); 
          return super.onFling(param1MotionEvent1, param1MotionEvent2, param1Float1, param1Float2);
        }
      };
    this.mGestureDetector = new GestureDetectorCompat(getContext(), this.mGestureListener);
    if (this.mCloseInterpolator != null) {
      this.mCloseScroller = ScrollerCompat.create(getContext(), this.mCloseInterpolator);
    } else {
      this.mCloseScroller = ScrollerCompat.create(getContext());
    } 
    if (this.mOpenInterpolator != null) {
      this.mOpenScroller = ScrollerCompat.create(getContext(), this.mOpenInterpolator);
    } else {
      this.mOpenScroller = ScrollerCompat.create(getContext());
    } 
    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2);
    this.mContentView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    if (this.mContentView.getId() < 1)
      this.mContentView.setId(1); 
    this.mMenuView.setId(2);
    this.mMenuView.setLayoutParams((ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-2, -2));
    addView(this.mContentView);
    addView((View)this.mMenuView);
  }
  
  private void swipe(int paramInt) {
    int i;
    if (Math.signum(paramInt) != this.mSwipeDirection) {
      i = 0;
    } else {
      i = paramInt;
      if (Math.abs(paramInt) > this.mMenuView.getWidth())
        i = this.mMenuView.getWidth() * this.mSwipeDirection; 
    } 
    this.mContentView.layout(-i, this.mContentView.getTop(), this.mContentView.getWidth() - i, getMeasuredHeight());
    if (this.mSwipeDirection == 1) {
      this.mMenuView.layout(this.mContentView.getWidth() - i, this.mMenuView.getTop(), this.mContentView.getWidth() + this.mMenuView.getWidth() - i, this.mMenuView.getBottom());
      return;
    } 
    this.mMenuView.layout(-this.mMenuView.getWidth() - i, this.mMenuView.getTop(), -i, this.mMenuView.getBottom());
  }
  
  public void closeMenu() {
    if (this.mCloseScroller.computeScrollOffset())
      this.mCloseScroller.abortAnimation(); 
    if (this.state == 1) {
      this.state = 0;
      swipe(0);
    } 
  }
  
  public void computeScroll() {
    if (this.state == 1) {
      if (this.mOpenScroller.computeScrollOffset()) {
        swipe(this.mOpenScroller.getCurrX() * this.mSwipeDirection);
        postInvalidate();
      } 
      return;
    } 
    if (this.mCloseScroller.computeScrollOffset()) {
      swipe((this.mBaseX - this.mCloseScroller.getCurrX()) * this.mSwipeDirection);
      postInvalidate();
    } 
  }
  
  public View getContentView() {
    return this.mContentView;
  }
  
  public SwipeMenuView getMenuView() {
    return this.mMenuView;
  }
  
  public int getPosition() {
    return this.position;
  }
  
  public boolean isOpen() {
    boolean bool = true;
    if (this.state != 1)
      bool = false; 
    return bool;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mContentView.layout(0, 0, getMeasuredWidth(), this.mContentView.getMeasuredHeight());
    if (this.mSwipeDirection == 1) {
      this.mMenuView.layout(getMeasuredWidth(), 0, getMeasuredWidth() + this.mMenuView.getMeasuredWidth(), this.mContentView.getMeasuredHeight());
      return;
    } 
    this.mMenuView.layout(-this.mMenuView.getMeasuredWidth(), 0, 0, this.mContentView.getMeasuredHeight());
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    this.mMenuView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824));
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public boolean onSwipe(MotionEvent paramMotionEvent) {
    int i;
    int j;
    null = false;
    this.mGestureDetector.onTouchEvent(paramMotionEvent);
    switch (paramMotionEvent.getAction()) {
      default:
        return true;
      case 0:
        this.mDownX = (int)paramMotionEvent.getX();
        this.isFling = false;
      case 2:
        i = (int)(this.mDownX - paramMotionEvent.getX());
        j = i;
        if (this.state == 1)
          j = i + this.mMenuView.getWidth() * this.mSwipeDirection; 
        swipe(j);
      case 1:
        break;
    } 
    if ((this.isFling || Math.abs(this.mDownX - paramMotionEvent.getX()) > (this.mMenuView.getWidth() / 2)) && Math.signum(this.mDownX - paramMotionEvent.getX()) == this.mSwipeDirection)
      smoothOpenMenu(); 
    smoothCloseMenu();
    return SYNTHETIC_LOCAL_VARIABLE_2;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    return super.onTouchEvent(paramMotionEvent);
  }
  
  public void openMenu() {
    if (this.state == 0) {
      this.state = 1;
      swipe(this.mMenuView.getWidth() * this.mSwipeDirection);
    } 
  }
  
  public void setMenuHeight(int paramInt) {
    Log.i("byz", "pos = " + this.position + ", height = " + paramInt);
    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)this.mMenuView.getLayoutParams();
    if (layoutParams.height != paramInt) {
      layoutParams.height = paramInt;
      this.mMenuView.setLayoutParams(this.mMenuView.getLayoutParams());
    } 
  }
  
  public void setPosition(int paramInt) {
    this.position = paramInt;
    this.mMenuView.setPosition(paramInt);
  }
  
  public void setSwipeDirection(int paramInt) {
    this.mSwipeDirection = paramInt;
  }
  
  public void smoothCloseMenu() {
    this.state = 0;
    if (this.mSwipeDirection == 1) {
      this.mBaseX = -this.mContentView.getLeft();
      this.mCloseScroller.startScroll(0, 0, this.mMenuView.getWidth(), 0, 350);
    } else {
      this.mBaseX = this.mMenuView.getRight();
      this.mCloseScroller.startScroll(0, 0, this.mMenuView.getWidth(), 0, 350);
    } 
    postInvalidate();
  }
  
  public void smoothOpenMenu() {
    this.state = 1;
    if (this.mSwipeDirection == 1) {
      this.mOpenScroller.startScroll(-this.mContentView.getLeft(), 0, this.mMenuView.getWidth(), 0, 350);
    } else {
      this.mOpenScroller.startScroll(this.mContentView.getLeft(), 0, this.mMenuView.getWidth(), 0, 350);
    } 
    postInvalidate();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/baoyz/swipemenulistview/SwipeMenuLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */