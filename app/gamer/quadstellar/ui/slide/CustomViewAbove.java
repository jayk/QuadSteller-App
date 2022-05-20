package app.gamer.quadstellar.ui.slide;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import java.util.ArrayList;
import java.util.List;

public class CustomViewAbove extends ViewGroup {
  private static final boolean DEBUG = false;
  
  private static final int INVALID_POINTER = -1;
  
  private static final int MAX_SETTLE_DURATION = 600;
  
  private static final int MIN_DISTANCE_FOR_FLING = 25;
  
  private static final String TAG = "CustomViewAbove";
  
  private static final boolean USE_CACHE = false;
  
  private static final Interpolator sInterpolator = new Interpolator() {
      public float getInterpolation(float param1Float) {
        param1Float--;
        return param1Float * param1Float * param1Float * param1Float * param1Float + 1.0F;
      }
    };
  
  protected int mActivePointerId = -1;
  
  private SlidingMenu.OnClosedListener mClosedListener;
  
  private View mContent;
  
  private int mCurItem;
  
  private boolean mEnabled = true;
  
  private int mFlingDistance;
  
  private List<View> mIgnoredViews = new ArrayList<View>();
  
  private float mInitialMotionX;
  
  private OnPageChangeListener mInternalPageChangeListener;
  
  private boolean mIsBeingDragged;
  
  private boolean mIsUnableToDrag;
  
  private float mLastMotionX;
  
  private float mLastMotionY;
  
  protected int mMaximumVelocity;
  
  private int mMinimumVelocity;
  
  private OnPageChangeListener mOnPageChangeListener;
  
  private SlidingMenu.OnOpenedListener mOpenedListener;
  
  private boolean mQuickReturn = false;
  
  private float mScrollX = 0.0F;
  
  private Scroller mScroller;
  
  private boolean mScrolling;
  
  private boolean mScrollingCacheEnabled;
  
  protected int mTouchMode = 0;
  
  private int mTouchSlop;
  
  private SlidingMenu.CanvasTransformer mTransformer;
  
  protected VelocityTracker mVelocityTracker;
  
  private CustomViewBehind mViewBehind;
  
  public CustomViewAbove(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public CustomViewAbove(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    initCustomViewAbove();
  }
  
  private void completeScroll() {
    if (this.mScrolling) {
      setScrollingCacheEnabled(false);
      this.mScroller.abortAnimation();
      int i = getScrollX();
      int j = getScrollY();
      int k = this.mScroller.getCurrX();
      int m = this.mScroller.getCurrY();
      if (i != k || j != m)
        scrollTo(k, m); 
      if (isMenuOpen()) {
        if (this.mOpenedListener != null)
          this.mOpenedListener.onOpened(); 
      } else if (this.mClosedListener != null) {
        this.mClosedListener.onClosed();
      } 
    } 
    this.mScrolling = false;
  }
  
  private void determineDrag(MotionEvent paramMotionEvent) {
    int i = this.mActivePointerId;
    int j = getPointerIndex(paramMotionEvent, i);
    if (i != -1 && j != -1) {
      float f1 = MotionEventCompat.getX(paramMotionEvent, j);
      float f2 = f1 - this.mLastMotionX;
      float f3 = Math.abs(f2);
      float f4 = MotionEventCompat.getY(paramMotionEvent, j);
      float f5 = Math.abs(f4 - this.mLastMotionY);
      if (isMenuOpen()) {
        i = this.mTouchSlop / 2;
      } else {
        i = this.mTouchSlop;
      } 
      if (f3 > i && f3 > f5 && thisSlideAllowed(f2)) {
        startDrag();
        this.mLastMotionX = f1;
        this.mLastMotionY = f4;
        setScrollingCacheEnabled(true);
        return;
      } 
      if (f3 > this.mTouchSlop)
        this.mIsUnableToDrag = true; 
    } 
  }
  
  private int determineTargetPage(float paramFloat, int paramInt1, int paramInt2) {
    int i = this.mCurItem;
    if (Math.abs(paramInt2) > this.mFlingDistance && Math.abs(paramInt1) > this.mMinimumVelocity) {
      if (paramInt1 > 0 && paramInt2 > 0)
        return i - 1; 
      int j = i;
      if (paramInt1 < 0) {
        j = i;
        if (paramInt2 < 0)
          j = i + 1; 
      } 
      return j;
    } 
    return Math.round(this.mCurItem + paramFloat);
  }
  
  private void endDrag() {
    this.mQuickReturn = false;
    this.mIsBeingDragged = false;
    this.mIsUnableToDrag = false;
    this.mActivePointerId = -1;
    if (this.mVelocityTracker != null) {
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
    } 
  }
  
  private int getLeftBound() {
    return this.mViewBehind.getAbsLeftBound(this.mContent);
  }
  
  private int getPointerIndex(MotionEvent paramMotionEvent, int paramInt) {
    paramInt = MotionEventCompat.findPointerIndex(paramMotionEvent, paramInt);
    if (paramInt == -1)
      this.mActivePointerId = -1; 
    return paramInt;
  }
  
  private int getRightBound() {
    return this.mViewBehind.getAbsRightBound(this.mContent);
  }
  
  private boolean isInIgnoredView(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: new android/graphics/Rect
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_2
    //   8: aload_0
    //   9: getfield mIgnoredViews : Ljava/util/List;
    //   12: invokeinterface iterator : ()Ljava/util/Iterator;
    //   17: astore_3
    //   18: aload_3
    //   19: invokeinterface hasNext : ()Z
    //   24: ifeq -> 63
    //   27: aload_3
    //   28: invokeinterface next : ()Ljava/lang/Object;
    //   33: checkcast android/view/View
    //   36: aload_2
    //   37: invokevirtual getHitRect : (Landroid/graphics/Rect;)V
    //   40: aload_2
    //   41: aload_1
    //   42: invokevirtual getX : ()F
    //   45: f2i
    //   46: aload_1
    //   47: invokevirtual getY : ()F
    //   50: f2i
    //   51: invokevirtual contains : (II)Z
    //   54: ifeq -> 18
    //   57: iconst_1
    //   58: istore #4
    //   60: iload #4
    //   62: ireturn
    //   63: iconst_0
    //   64: istore #4
    //   66: goto -> 60
  }
  
  private void onSecondaryPointerUp(MotionEvent paramMotionEvent) {
    int i = MotionEventCompat.getActionIndex(paramMotionEvent);
    if (MotionEventCompat.getPointerId(paramMotionEvent, i) == this.mActivePointerId) {
      if (i == 0) {
        i = 1;
      } else {
        i = 0;
      } 
      this.mLastMotionX = MotionEventCompat.getX(paramMotionEvent, i);
      this.mActivePointerId = MotionEventCompat.getPointerId(paramMotionEvent, i);
      if (this.mVelocityTracker != null)
        this.mVelocityTracker.clear(); 
    } 
  }
  
  private void pageScrolled(int paramInt) {
    int i = getWidth();
    int j = paramInt / i;
    paramInt %= i;
    onPageScrolled(j, paramInt / i, paramInt);
  }
  
  private void setScrollingCacheEnabled(boolean paramBoolean) {
    if (this.mScrollingCacheEnabled != paramBoolean)
      this.mScrollingCacheEnabled = paramBoolean; 
  }
  
  private void startDrag() {
    this.mIsBeingDragged = true;
    this.mQuickReturn = false;
  }
  
  private boolean thisSlideAllowed(float paramFloat) {
    return isMenuOpen() ? this.mViewBehind.menuOpenSlideAllowed(paramFloat) : this.mViewBehind.menuClosedSlideAllowed(paramFloat);
  }
  
  private boolean thisTouchAllowed(MotionEvent paramMotionEvent) {
    boolean bool1 = false;
    int i = (int)(paramMotionEvent.getX() + this.mScrollX);
    if (isMenuOpen())
      boolean bool = this.mViewBehind.menuOpenTouchAllowed(this.mContent, this.mCurItem, i); 
    boolean bool2 = bool1;
    switch (this.mTouchMode) {
      case 2:
        return bool2;
      default:
        bool2 = bool1;
      case 0:
        bool2 = this.mViewBehind.marginTouchAllowed(this.mContent, i);
      case 1:
        break;
    } 
    bool2 = bool1;
    if (!isInIgnoredView(paramMotionEvent))
      bool2 = true; 
  }
  
  public void addIgnoredView(View paramView) {
    if (!this.mIgnoredViews.contains(paramView))
      this.mIgnoredViews.add(paramView); 
  }
  
  public boolean arrowScroll(int paramInt) {
    View view1 = findFocus();
    View view2 = view1;
    if (view1 == this)
      view2 = null; 
    boolean bool = false;
    view1 = FocusFinder.getInstance().findNextFocus(this, view2, paramInt);
    if (view1 != null && view1 != view2) {
      if (paramInt == 17) {
        bool = view1.requestFocus();
      } else if (paramInt == 66) {
        if (view2 != null && view1.getLeft() <= view2.getLeft()) {
          bool = pageRight();
        } else {
          bool = view1.requestFocus();
        } 
      } 
    } else if (paramInt == 17 || paramInt == 1) {
      bool = pageLeft();
    } else if (paramInt == 66 || paramInt == 2) {
      bool = pageRight();
    } 
    if (bool)
      playSoundEffect(SoundEffectConstants.getContantForFocusDirection(paramInt)); 
    return bool;
  }
  
  protected boolean canScroll(View paramView, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3) {
    if (paramView instanceof ViewGroup) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      int i = paramView.getScrollX();
      int j = paramView.getScrollY();
      for (int k = viewGroup.getChildCount() - 1; k >= 0; k--) {
        View view = viewGroup.getChildAt(k);
        if (paramInt2 + i >= view.getLeft() && paramInt2 + i < view.getRight() && paramInt3 + j >= view.getTop() && paramInt3 + j < view.getBottom() && canScroll(view, true, paramInt1, paramInt2 + i - view.getLeft(), paramInt3 + j - view.getTop()))
          return true; 
      } 
    } 
    return (paramBoolean && ViewCompat.canScrollHorizontally(paramView, -paramInt1));
  }
  
  public void clearIgnoredViews() {
    this.mIgnoredViews.clear();
  }
  
  public void computeScroll() {
    if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
      int i = getScrollX();
      int j = getScrollY();
      int k = this.mScroller.getCurrX();
      int m = this.mScroller.getCurrY();
      if (i != k || j != m) {
        scrollTo(k, m);
        pageScrolled(k);
      } 
      invalidate();
      return;
    } 
    completeScroll();
  }
  
  protected void dispatchDraw(Canvas paramCanvas) {
    this.mViewBehind.drawShadow(this.mContent, paramCanvas);
    this.mViewBehind.drawFade(this.mContent, paramCanvas, getPercentOpen());
    this.mViewBehind.drawSelector(this.mContent, paramCanvas, getPercentOpen());
    if (this.mTransformer != null) {
      paramCanvas.save();
      this.mTransformer.transformCanvas(paramCanvas, getPercentOpen());
      super.dispatchDraw(paramCanvas);
      paramCanvas.restore();
      return;
    } 
    super.dispatchDraw(paramCanvas);
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
    return (super.dispatchKeyEvent(paramKeyEvent) || executeKeyEvent(paramKeyEvent));
  }
  
  float distanceInfluenceForSnapDuration(float paramFloat) {
    return (float)Math.sin((float)((paramFloat - 0.5F) * 0.4712389167638204D));
  }
  
  public boolean executeKeyEvent(KeyEvent paramKeyEvent) {
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (paramKeyEvent.getAction() == 0) {
      switch (paramKeyEvent.getKeyCode()) {
        default:
          return bool1;
        case 21:
          return arrowScroll(17);
        case 22:
          return arrowScroll(66);
        case 61:
          break;
      } 
    } else {
      return bool2;
    } 
    bool2 = bool1;
    if (Build.VERSION.SDK_INT >= 11) {
      if (KeyEventCompat.hasNoModifiers(paramKeyEvent))
        return arrowScroll(2); 
      bool2 = bool1;
      if (KeyEventCompat.hasModifiers(paramKeyEvent, 1))
        bool2 = arrowScroll(1); 
    } 
    return bool2;
  }
  
  public int getBehindWidth() {
    return (this.mViewBehind == null) ? 0 : this.mViewBehind.getBehindWidth();
  }
  
  public int getChildWidth(int paramInt) {
    switch (paramInt) {
      default:
        return 0;
      case 0:
        return getBehindWidth();
      case 1:
        break;
    } 
    return this.mContent.getWidth();
  }
  
  public View getContent() {
    return this.mContent;
  }
  
  public int getContentLeft() {
    return this.mContent.getLeft() + this.mContent.getPaddingLeft();
  }
  
  public int getCurrentItem() {
    return this.mCurItem;
  }
  
  public int getDestScrollX(int paramInt) {
    switch (paramInt) {
      default:
        return 0;
      case 0:
      case 2:
        return this.mViewBehind.getMenuLeft(this.mContent, paramInt);
      case 1:
        break;
    } 
    return this.mContent.getLeft();
  }
  
  protected float getPercentOpen() {
    return Math.abs(this.mScrollX - this.mContent.getLeft()) / getBehindWidth();
  }
  
  public int getTouchMode() {
    return this.mTouchMode;
  }
  
  void initCustomViewAbove() {
    setWillNotDraw(false);
    setDescendantFocusability(262144);
    setFocusable(true);
    Context context = getContext();
    this.mScroller = new Scroller(context, sInterpolator);
    ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
    this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(viewConfiguration);
    this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
    setInternalPageChangeListener(new SimpleOnPageChangeListener() {
          public void onPageSelected(int param1Int) {
            if (CustomViewAbove.this.mViewBehind != null) {
              switch (param1Int) {
                default:
                  return;
                case 0:
                case 2:
                  CustomViewAbove.this.mViewBehind.setChildrenEnabled(true);
                case 1:
                  break;
              } 
              CustomViewAbove.this.mViewBehind.setChildrenEnabled(false);
            } 
          }
        });
    this.mFlingDistance = (int)(25.0F * (context.getResources().getDisplayMetrics()).density);
  }
  
  public boolean isMenuOpen() {
    return (this.mCurItem == 0 || this.mCurItem == 2);
  }
  
  public boolean isSlidingEnabled() {
    return this.mEnabled;
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    boolean bool = false;
    if (this.mEnabled) {
      int i = paramMotionEvent.getAction() & 0xFF;
      if (i == 3 || i == 1 || (i != 0 && this.mIsUnableToDrag)) {
        endDrag();
        return bool;
      } 
      switch (i) {
        default:
          if (!this.mIsBeingDragged) {
            if (this.mVelocityTracker == null)
              this.mVelocityTracker = VelocityTracker.obtain(); 
            this.mVelocityTracker.addMovement(paramMotionEvent);
          } 
          if (this.mIsBeingDragged || this.mQuickReturn)
            bool = true; 
          return bool;
        case 2:
          determineDrag(paramMotionEvent);
        case 0:
          i = MotionEventCompat.getActionIndex(paramMotionEvent);
          this.mActivePointerId = MotionEventCompat.getPointerId(paramMotionEvent, i);
          if (this.mActivePointerId != -1) {
            float f = MotionEventCompat.getX(paramMotionEvent, i);
            this.mInitialMotionX = f;
            this.mLastMotionX = f;
            this.mLastMotionY = MotionEventCompat.getY(paramMotionEvent, i);
            if (thisTouchAllowed(paramMotionEvent)) {
              this.mIsBeingDragged = false;
              this.mIsUnableToDrag = false;
              if (isMenuOpen() && this.mViewBehind.menuTouchInQuickReturn(this.mContent, this.mCurItem, paramMotionEvent.getX() + this.mScrollX))
                this.mQuickReturn = true; 
            } else {
              this.mIsUnableToDrag = true;
            } 
          } 
        case 6:
          break;
      } 
      onSecondaryPointerUp(paramMotionEvent);
    } 
    return bool;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mContent.layout(0, 0, paramInt3 - paramInt1, paramInt4 - paramInt2);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = getDefaultSize(0, paramInt1);
    int j = getDefaultSize(0, paramInt2);
    setMeasuredDimension(i, j);
    paramInt1 = getChildMeasureSpec(paramInt1, 0, i);
    paramInt2 = getChildMeasureSpec(paramInt2, 0, j);
    this.mContent.measure(paramInt1, paramInt2);
  }
  
  protected void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
    if (this.mOnPageChangeListener != null)
      this.mOnPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2); 
    if (this.mInternalPageChangeListener != null)
      this.mInternalPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2); 
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 != paramInt3) {
      completeScroll();
      scrollTo(getDestScrollX(this.mCurItem), getScrollY());
    } 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    float f;
    if (!this.mEnabled)
      return false; 
    if (!this.mIsBeingDragged && !thisTouchAllowed(paramMotionEvent))
      return false; 
    int i = paramMotionEvent.getAction();
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
    this.mVelocityTracker.addMovement(paramMotionEvent);
    switch (i & 0xFF) {
      default:
        return true;
      case 0:
        completeScroll();
        this.mActivePointerId = MotionEventCompat.getPointerId(paramMotionEvent, MotionEventCompat.getActionIndex(paramMotionEvent));
        f = paramMotionEvent.getX();
        this.mInitialMotionX = f;
        this.mLastMotionX = f;
      case 2:
        if (!this.mIsBeingDragged) {
          determineDrag(paramMotionEvent);
          if (this.mIsUnableToDrag)
            return false; 
        } 
        if (this.mIsBeingDragged) {
          i = getPointerIndex(paramMotionEvent, this.mActivePointerId);
          if (this.mActivePointerId != -1) {
            f = MotionEventCompat.getX(paramMotionEvent, i);
            float f1 = this.mLastMotionX;
            this.mLastMotionX = f;
            float f2 = getScrollX() + f1 - f;
            f = getLeftBound();
            f1 = getRightBound();
            if (f2 >= f) {
              f = f2;
              if (f2 > f1)
                f = f1; 
            } 
            this.mLastMotionX += f - (int)f;
            scrollTo((int)f, getScrollY());
            pageScrolled((int)f);
          } 
        } 
      case 1:
        if (this.mIsBeingDragged) {
          VelocityTracker velocityTracker = this.mVelocityTracker;
          velocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
          i = (int)VelocityTrackerCompat.getXVelocity(velocityTracker, this.mActivePointerId);
          f = (getScrollX() - getDestScrollX(this.mCurItem)) / getBehindWidth();
          int j = getPointerIndex(paramMotionEvent, this.mActivePointerId);
          if (this.mActivePointerId != -1) {
            setCurrentItemInternal(determineTargetPage(f, i, (int)(MotionEventCompat.getX(paramMotionEvent, j) - this.mInitialMotionX)), true, true, i);
          } else {
            setCurrentItemInternal(this.mCurItem, true, true, i);
          } 
          this.mActivePointerId = -1;
          endDrag();
        } else if (this.mQuickReturn && this.mViewBehind.menuTouchInQuickReturn(this.mContent, this.mCurItem, paramMotionEvent.getX() + this.mScrollX)) {
          setCurrentItem(1);
          endDrag();
        } 
      case 3:
        if (this.mIsBeingDragged) {
          setCurrentItemInternal(this.mCurItem, true, true);
          this.mActivePointerId = -1;
          endDrag();
        } 
      case 5:
        i = MotionEventCompat.getActionIndex(paramMotionEvent);
        this.mLastMotionX = MotionEventCompat.getX(paramMotionEvent, i);
        this.mActivePointerId = MotionEventCompat.getPointerId(paramMotionEvent, i);
      case 6:
        break;
    } 
    onSecondaryPointerUp(paramMotionEvent);
    i = getPointerIndex(paramMotionEvent, this.mActivePointerId);
    if (this.mActivePointerId != -1)
      this.mLastMotionX = MotionEventCompat.getX(paramMotionEvent, i); 
  }
  
  boolean pageLeft() {
    null = true;
    if (this.mCurItem > 0) {
      setCurrentItem(this.mCurItem - 1, true);
      return null;
    } 
    return false;
  }
  
  boolean pageRight() {
    null = true;
    if (this.mCurItem < 1) {
      setCurrentItem(this.mCurItem + 1, true);
      return null;
    } 
    return false;
  }
  
  public void removeIgnoredView(View paramView) {
    this.mIgnoredViews.remove(paramView);
  }
  
  public void scrollTo(int paramInt1, int paramInt2) {
    super.scrollTo(paramInt1, paramInt2);
    this.mScrollX = paramInt1;
    this.mViewBehind.scrollBehindTo(this.mContent, paramInt1, paramInt2);
    ((SlidingMenu)getParent()).manageLayers(getPercentOpen());
    if (this.mTransformer != null)
      invalidate(); 
  }
  
  public void setAboveOffset(int paramInt) {
    this.mContent.setPadding(paramInt, this.mContent.getPaddingTop(), this.mContent.getPaddingRight(), this.mContent.getPaddingBottom());
  }
  
  public void setCanvasTransformer(SlidingMenu.CanvasTransformer paramCanvasTransformer) {
    this.mTransformer = paramCanvasTransformer;
  }
  
  public void setContent(View paramView) {
    if (this.mContent != null)
      removeView(this.mContent); 
    this.mContent = paramView;
    addView(this.mContent);
  }
  
  public void setCurrentItem(int paramInt) {
    setCurrentItemInternal(paramInt, true, false);
  }
  
  public void setCurrentItem(int paramInt, boolean paramBoolean) {
    setCurrentItemInternal(paramInt, paramBoolean, false);
  }
  
  void setCurrentItemInternal(int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
    setCurrentItemInternal(paramInt, paramBoolean1, paramBoolean2, 0);
  }
  
  void setCurrentItemInternal(int paramInt1, boolean paramBoolean1, boolean paramBoolean2, int paramInt2) {
    if (!paramBoolean2 && this.mCurItem == paramInt1) {
      setScrollingCacheEnabled(false);
      return;
    } 
    int i = this.mViewBehind.getMenuPage(paramInt1);
    if (this.mCurItem != i) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    this.mCurItem = i;
    int j = getDestScrollX(this.mCurItem);
    if (paramInt1 != 0 && this.mOnPageChangeListener != null)
      this.mOnPageChangeListener.onPageSelected(i); 
    if (paramInt1 != 0 && this.mInternalPageChangeListener != null)
      this.mInternalPageChangeListener.onPageSelected(i); 
    if (paramBoolean1) {
      smoothScrollTo(j, 0, paramInt2);
      return;
    } 
    completeScroll();
    scrollTo(j, 0);
  }
  
  public void setCustomViewBehind(CustomViewBehind paramCustomViewBehind) {
    this.mViewBehind = paramCustomViewBehind;
  }
  
  OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener paramOnPageChangeListener) {
    OnPageChangeListener onPageChangeListener = this.mInternalPageChangeListener;
    this.mInternalPageChangeListener = paramOnPageChangeListener;
    return onPageChangeListener;
  }
  
  public void setOnClosedListener(SlidingMenu.OnClosedListener paramOnClosedListener) {
    this.mClosedListener = paramOnClosedListener;
  }
  
  public void setOnOpenedListener(SlidingMenu.OnOpenedListener paramOnOpenedListener) {
    this.mOpenedListener = paramOnOpenedListener;
  }
  
  public void setOnPageChangeListener(OnPageChangeListener paramOnPageChangeListener) {
    this.mOnPageChangeListener = paramOnPageChangeListener;
  }
  
  public void setSlidingEnabled(boolean paramBoolean) {
    this.mEnabled = paramBoolean;
  }
  
  public void setTouchMode(int paramInt) {
    this.mTouchMode = paramInt;
  }
  
  void smoothScrollTo(int paramInt1, int paramInt2) {
    smoothScrollTo(paramInt1, paramInt2, 0);
  }
  
  void smoothScrollTo(int paramInt1, int paramInt2, int paramInt3) {
    if (getChildCount() == 0) {
      setScrollingCacheEnabled(false);
      return;
    } 
    int i = getScrollX();
    int j = getScrollY();
    int k = paramInt1 - i;
    paramInt2 -= j;
    if (k == 0 && paramInt2 == 0) {
      completeScroll();
      if (isMenuOpen()) {
        if (this.mOpenedListener != null)
          this.mOpenedListener.onOpened(); 
        return;
      } 
      if (this.mClosedListener != null)
        this.mClosedListener.onClosed(); 
      return;
    } 
    setScrollingCacheEnabled(true);
    this.mScrolling = true;
    paramInt1 = getBehindWidth();
    int m = paramInt1 / 2;
    float f1 = Math.min(1.0F, 1.0F * Math.abs(k) / paramInt1);
    float f2 = m;
    float f3 = m;
    f1 = distanceInfluenceForSnapDuration(f1);
    paramInt3 = Math.abs(paramInt3);
    if (paramInt3 > 0) {
      paramInt1 = Math.round(1000.0F * Math.abs((f2 + f3 * f1) / paramInt3)) * 4;
    } else {
      paramInt1 = (int)((1.0F + Math.abs(k) / paramInt1) * 100.0F);
      paramInt1 = 600;
    } 
    paramInt1 = Math.min(paramInt1, 600);
    this.mScroller.startScroll(i, j, k, paramInt2, paramInt1);
    invalidate();
  }
  
  public static interface OnPageChangeListener {
    void onPageScrolled(int param1Int1, float param1Float, int param1Int2);
    
    void onPageSelected(int param1Int);
  }
  
  public static class SimpleOnPageChangeListener implements OnPageChangeListener {
    public void onPageScrollStateChanged(int param1Int) {}
    
    public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {}
    
    public void onPageSelected(int param1Int) {}
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/slide/CustomViewAbove.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */