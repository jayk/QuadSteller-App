package android.support.v4.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import java.util.ArrayList;

public class NestedScrollView extends FrameLayout implements NestedScrollingParent, NestedScrollingChild, ScrollingView {
  private static final AccessibilityDelegate ACCESSIBILITY_DELEGATE = new AccessibilityDelegate();
  
  static final int ANIMATED_SCROLL_GAP = 250;
  
  private static final int INVALID_POINTER = -1;
  
  static final float MAX_SCROLL_FACTOR = 0.5F;
  
  private static final int[] SCROLLVIEW_STYLEABLE = new int[] { 16843130 };
  
  private static final String TAG = "NestedScrollView";
  
  private int mActivePointerId = -1;
  
  private final NestedScrollingChildHelper mChildHelper;
  
  private View mChildToScrollTo = null;
  
  private EdgeEffectCompat mEdgeGlowBottom;
  
  private EdgeEffectCompat mEdgeGlowTop;
  
  private boolean mFillViewport;
  
  private boolean mIsBeingDragged = false;
  
  private boolean mIsLaidOut = false;
  
  private boolean mIsLayoutDirty = true;
  
  private int mLastMotionY;
  
  private long mLastScroll;
  
  private int mMaximumVelocity;
  
  private int mMinimumVelocity;
  
  private int mNestedYOffset;
  
  private OnScrollChangeListener mOnScrollChangeListener;
  
  private final NestedScrollingParentHelper mParentHelper;
  
  private SavedState mSavedState;
  
  private final int[] mScrollConsumed = new int[2];
  
  private final int[] mScrollOffset = new int[2];
  
  private ScrollerCompat mScroller;
  
  private boolean mSmoothScrollingEnabled = true;
  
  private final Rect mTempRect = new Rect();
  
  private int mTouchSlop;
  
  private VelocityTracker mVelocityTracker;
  
  private float mVerticalScrollFactor;
  
  public NestedScrollView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public NestedScrollView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public NestedScrollView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    initScrollView();
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, SCROLLVIEW_STYLEABLE, paramInt, 0);
    setFillViewport(typedArray.getBoolean(0, false));
    typedArray.recycle();
    this.mParentHelper = new NestedScrollingParentHelper((ViewGroup)this);
    this.mChildHelper = new NestedScrollingChildHelper((View)this);
    setNestedScrollingEnabled(true);
    ViewCompat.setAccessibilityDelegate((View)this, ACCESSIBILITY_DELEGATE);
  }
  
  private boolean canScroll() {
    boolean bool1 = false;
    View view = getChildAt(0);
    boolean bool2 = bool1;
    if (view != null) {
      int i = view.getHeight();
      bool2 = bool1;
      if (getHeight() < getPaddingTop() + i + getPaddingBottom())
        bool2 = true; 
    } 
    return bool2;
  }
  
  private static int clamp(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt2 >= paramInt3 || paramInt1 < 0)
      return 0; 
    int i = paramInt1;
    if (paramInt2 + paramInt1 > paramInt3)
      i = paramInt3 - paramInt2; 
    return i;
  }
  
  private void doScrollY(int paramInt) {
    if (paramInt != 0) {
      if (this.mSmoothScrollingEnabled) {
        smoothScrollBy(0, paramInt);
        return;
      } 
    } else {
      return;
    } 
    scrollBy(0, paramInt);
  }
  
  private void endDrag() {
    this.mIsBeingDragged = false;
    recycleVelocityTracker();
    stopNestedScroll();
    if (this.mEdgeGlowTop != null) {
      this.mEdgeGlowTop.onRelease();
      this.mEdgeGlowBottom.onRelease();
    } 
  }
  
  private void ensureGlows() {
    if (getOverScrollMode() != 2) {
      if (this.mEdgeGlowTop == null) {
        Context context = getContext();
        this.mEdgeGlowTop = new EdgeEffectCompat(context);
        this.mEdgeGlowBottom = new EdgeEffectCompat(context);
      } 
      return;
    } 
    this.mEdgeGlowTop = null;
    this.mEdgeGlowBottom = null;
  }
  
  private View findFocusableViewInBounds(boolean paramBoolean, int paramInt1, int paramInt2) {
    ArrayList<View> arrayList = getFocusables(2);
    View view = null;
    boolean bool = false;
    int i = arrayList.size();
    byte b = 0;
    while (b < i) {
      View view1 = arrayList.get(b);
      int j = view1.getTop();
      int k = view1.getBottom();
      View view2 = view;
      boolean bool1 = bool;
      if (paramInt1 < k) {
        view2 = view;
        bool1 = bool;
        if (j < paramInt2) {
          boolean bool2;
          if (paramInt1 < j && k < paramInt2) {
            bool2 = true;
          } else {
            bool2 = false;
          } 
          if (view == null) {
            view2 = view1;
            bool1 = bool2;
          } else {
            if ((paramBoolean && j < view.getTop()) || (!paramBoolean && k > view.getBottom())) {
              j = 1;
            } else {
              j = 0;
            } 
            if (bool) {
              view2 = view;
              bool1 = bool;
              if (bool2) {
                view2 = view;
                bool1 = bool;
                if (j != 0) {
                  view2 = view1;
                  bool1 = bool;
                } 
              } 
            } else if (bool2) {
              view2 = view1;
              bool1 = true;
            } else {
              view2 = view;
              bool1 = bool;
              if (j != 0) {
                view2 = view1;
                bool1 = bool;
              } 
            } 
          } 
        } 
      } 
      b++;
      view = view2;
      bool = bool1;
    } 
    return view;
  }
  
  private void flingWithNestedDispatch(int paramInt) {
    boolean bool;
    int i = getScrollY();
    if ((i > 0 || paramInt > 0) && (i < getScrollRange() || paramInt < 0)) {
      bool = true;
    } else {
      bool = false;
    } 
    if (!dispatchNestedPreFling(0.0F, paramInt)) {
      dispatchNestedFling(0.0F, paramInt, bool);
      if (bool)
        fling(paramInt); 
    } 
  }
  
  private float getVerticalScrollFactorCompat() {
    if (this.mVerticalScrollFactor == 0.0F) {
      TypedValue typedValue = new TypedValue();
      Context context = getContext();
      if (!context.getTheme().resolveAttribute(16842829, typedValue, true))
        throw new IllegalStateException("Expected theme to define listPreferredItemHeight."); 
      this.mVerticalScrollFactor = typedValue.getDimension(context.getResources().getDisplayMetrics());
    } 
    return this.mVerticalScrollFactor;
  }
  
  private boolean inChild(int paramInt1, int paramInt2) {
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (getChildCount() > 0) {
      int i = getScrollY();
      View view = getChildAt(0);
      bool2 = bool1;
      if (paramInt2 >= view.getTop() - i) {
        bool2 = bool1;
        if (paramInt2 < view.getBottom() - i) {
          bool2 = bool1;
          if (paramInt1 >= view.getLeft()) {
            bool2 = bool1;
            if (paramInt1 < view.getRight())
              bool2 = true; 
          } 
        } 
      } 
    } 
    return bool2;
  }
  
  private void initOrResetVelocityTracker() {
    if (this.mVelocityTracker == null) {
      this.mVelocityTracker = VelocityTracker.obtain();
      return;
    } 
    this.mVelocityTracker.clear();
  }
  
  private void initScrollView() {
    this.mScroller = ScrollerCompat.create(getContext(), null);
    setFocusable(true);
    setDescendantFocusability(262144);
    setWillNotDraw(false);
    ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
    this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
    this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
  }
  
  private void initVelocityTrackerIfNotExists() {
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
  }
  
  private boolean isOffScreen(View paramView) {
    boolean bool = false;
    if (!isWithinDeltaOfScreen(paramView, 0, getHeight()))
      bool = true; 
    return bool;
  }
  
  private static boolean isViewDescendantOf(View paramView1, View paramView2) {
    boolean bool = true;
    if (paramView1 != paramView2) {
      ViewParent viewParent = paramView1.getParent();
      if (!(viewParent instanceof ViewGroup) || !isViewDescendantOf((View)viewParent, paramView2))
        bool = false; 
    } 
    return bool;
  }
  
  private boolean isWithinDeltaOfScreen(View paramView, int paramInt1, int paramInt2) {
    paramView.getDrawingRect(this.mTempRect);
    offsetDescendantRectToMyCoords(paramView, this.mTempRect);
    return (this.mTempRect.bottom + paramInt1 >= getScrollY() && this.mTempRect.top - paramInt1 <= getScrollY() + paramInt2);
  }
  
  private void onSecondaryPointerUp(MotionEvent paramMotionEvent) {
    int i = (paramMotionEvent.getAction() & 0xFF00) >> 8;
    if (paramMotionEvent.getPointerId(i) == this.mActivePointerId) {
      if (i == 0) {
        i = 1;
      } else {
        i = 0;
      } 
      this.mLastMotionY = (int)paramMotionEvent.getY(i);
      this.mActivePointerId = paramMotionEvent.getPointerId(i);
      if (this.mVelocityTracker != null)
        this.mVelocityTracker.clear(); 
    } 
  }
  
  private void recycleVelocityTracker() {
    if (this.mVelocityTracker != null) {
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
    } 
  }
  
  private boolean scrollAndFocus(int paramInt1, int paramInt2, int paramInt3) {
    boolean bool2;
    NestedScrollView nestedScrollView;
    boolean bool1 = true;
    int i = getHeight();
    int j = getScrollY();
    i = j + i;
    if (paramInt1 == 33) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    View view1 = findFocusableViewInBounds(bool2, paramInt2, paramInt3);
    View view2 = view1;
    if (view1 == null)
      nestedScrollView = this; 
    if (paramInt2 >= j && paramInt3 <= i) {
      bool2 = false;
    } else {
      if (bool2) {
        paramInt2 -= j;
      } else {
        paramInt2 = paramInt3 - i;
      } 
      doScrollY(paramInt2);
      bool2 = bool1;
    } 
    if (nestedScrollView != findFocus())
      nestedScrollView.requestFocus(paramInt1); 
    return bool2;
  }
  
  private void scrollToChild(View paramView) {
    paramView.getDrawingRect(this.mTempRect);
    offsetDescendantRectToMyCoords(paramView, this.mTempRect);
    int i = computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
    if (i != 0)
      scrollBy(0, i); 
  }
  
  private boolean scrollToChildRect(Rect paramRect, boolean paramBoolean) {
    boolean bool;
    int i = computeScrollDeltaToGetChildRectOnScreen(paramRect);
    if (i != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool) {
      if (paramBoolean) {
        scrollBy(0, i);
        return bool;
      } 
    } else {
      return bool;
    } 
    smoothScrollBy(0, i);
    return bool;
  }
  
  public void addView(View paramView) {
    if (getChildCount() > 0)
      throw new IllegalStateException("ScrollView can host only one direct child"); 
    super.addView(paramView);
  }
  
  public void addView(View paramView, int paramInt) {
    if (getChildCount() > 0)
      throw new IllegalStateException("ScrollView can host only one direct child"); 
    super.addView(paramView, paramInt);
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    if (getChildCount() > 0)
      throw new IllegalStateException("ScrollView can host only one direct child"); 
    super.addView(paramView, paramInt, paramLayoutParams);
  }
  
  public void addView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    if (getChildCount() > 0)
      throw new IllegalStateException("ScrollView can host only one direct child"); 
    super.addView(paramView, paramLayoutParams);
  }
  
  public boolean arrowScroll(int paramInt) {
    null = false;
    View view1 = findFocus();
    View view2 = view1;
    if (view1 == this)
      view2 = null; 
    view1 = FocusFinder.getInstance().findNextFocus((ViewGroup)this, view2, paramInt);
    int i = getMaxScrollAmount();
    if (view1 != null && isWithinDeltaOfScreen(view1, i, getHeight())) {
      view1.getDrawingRect(this.mTempRect);
      offsetDescendantRectToMyCoords(view1, this.mTempRect);
      doScrollY(computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
      view1.requestFocus(paramInt);
    } else {
      int k;
      int j = i;
      if (paramInt == 33 && getScrollY() < j) {
        k = getScrollY();
      } else {
        k = j;
        if (paramInt == 130) {
          k = j;
          if (getChildCount() > 0) {
            int m = getChildAt(0).getBottom();
            int n = getScrollY() + getHeight() - getPaddingBottom();
            k = j;
            if (m - n < i)
              k = m - n; 
          } 
        } 
      } 
      if (k != 0) {
        if (paramInt == 130) {
          paramInt = k;
        } else {
          paramInt = -k;
        } 
        doScrollY(paramInt);
      } else {
        return null;
      } 
    } 
    if (view2 != null && view2.isFocused() && isOffScreen(view2)) {
      paramInt = getDescendantFocusability();
      setDescendantFocusability(131072);
      requestFocus();
      setDescendantFocusability(paramInt);
    } 
    return true;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public int computeHorizontalScrollExtent() {
    return super.computeHorizontalScrollExtent();
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public int computeHorizontalScrollOffset() {
    return super.computeHorizontalScrollOffset();
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public int computeHorizontalScrollRange() {
    return super.computeHorizontalScrollRange();
  }
  
  public void computeScroll() {
    int i;
    int j;
    int k;
    boolean bool = true;
    if (this.mScroller.computeScrollOffset()) {
      int m = getScrollX();
      i = getScrollY();
      int n = this.mScroller.getCurrX();
      j = this.mScroller.getCurrY();
      if (m != n || i != j) {
        k = getScrollRange();
        int i1 = getOverScrollMode();
        boolean bool1 = bool;
        if (i1 != 0)
          if (i1 == 1 && k > 0) {
            bool1 = bool;
          } else {
            bool1 = false;
          }  
        overScrollByCompat(n - m, j - i, m, i, 0, k, 0, 0, false);
        if (bool1) {
          ensureGlows();
          if (j <= 0 && i > 0) {
            this.mEdgeGlowTop.onAbsorb((int)this.mScroller.getCurrVelocity());
            return;
          } 
        } else {
          return;
        } 
      } else {
        return;
      } 
    } else {
      return;
    } 
    if (j >= k && i < k)
      this.mEdgeGlowBottom.onAbsorb((int)this.mScroller.getCurrVelocity()); 
  }
  
  protected int computeScrollDeltaToGetChildRectOnScreen(Rect paramRect) {
    if (getChildCount() == 0)
      return 0; 
    int j = getHeight();
    int k = getScrollY();
    int i = k + j;
    int m = getVerticalFadingEdgeLength();
    int n = k;
    if (paramRect.top > 0)
      n = k + m; 
    k = i;
    if (paramRect.bottom < getChildAt(0).getHeight())
      k = i - m; 
    m = 0;
    if (paramRect.bottom > k && paramRect.top > n) {
      if (paramRect.height() > j) {
        i = 0 + paramRect.top - n;
      } else {
        i = 0 + paramRect.bottom - k;
      } 
      return Math.min(i, getChildAt(0).getBottom() - k);
    } 
    i = m;
    if (paramRect.top < n) {
      i = m;
      if (paramRect.bottom < k) {
        if (paramRect.height() > j) {
          i = 0 - k - paramRect.bottom;
        } else {
          i = 0 - n - paramRect.top;
        } 
        i = Math.max(i, -getScrollY());
      } 
    } 
    return i;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public int computeVerticalScrollExtent() {
    return super.computeVerticalScrollExtent();
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public int computeVerticalScrollOffset() {
    return Math.max(0, super.computeVerticalScrollOffset());
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public int computeVerticalScrollRange() {
    int i = getChildCount();
    int j = getHeight() - getPaddingBottom() - getPaddingTop();
    if (i != 0) {
      i = getChildAt(0).getBottom();
      int k = getScrollY();
      int m = Math.max(0, i - j);
      if (k < 0)
        return i - k; 
      j = i;
      if (k > m)
        j = i + k - m; 
    } 
    return j;
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
    return (super.dispatchKeyEvent(paramKeyEvent) || executeKeyEvent(paramKeyEvent));
  }
  
  public boolean dispatchNestedFling(float paramFloat1, float paramFloat2, boolean paramBoolean) {
    return this.mChildHelper.dispatchNestedFling(paramFloat1, paramFloat2, paramBoolean);
  }
  
  public boolean dispatchNestedPreFling(float paramFloat1, float paramFloat2) {
    return this.mChildHelper.dispatchNestedPreFling(paramFloat1, paramFloat2);
  }
  
  public boolean dispatchNestedPreScroll(int paramInt1, int paramInt2, int[] paramArrayOfint1, int[] paramArrayOfint2) {
    return this.mChildHelper.dispatchNestedPreScroll(paramInt1, paramInt2, paramArrayOfint1, paramArrayOfint2);
  }
  
  public boolean dispatchNestedScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint) {
    return this.mChildHelper.dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfint);
  }
  
  public void draw(Canvas paramCanvas) {
    super.draw(paramCanvas);
    if (this.mEdgeGlowTop != null) {
      int i = getScrollY();
      if (!this.mEdgeGlowTop.isFinished()) {
        int j = paramCanvas.save();
        int k = getWidth();
        int m = getPaddingLeft();
        int n = getPaddingRight();
        paramCanvas.translate(getPaddingLeft(), Math.min(0, i));
        this.mEdgeGlowTop.setSize(k - m - n, getHeight());
        if (this.mEdgeGlowTop.draw(paramCanvas))
          ViewCompat.postInvalidateOnAnimation((View)this); 
        paramCanvas.restoreToCount(j);
      } 
      if (!this.mEdgeGlowBottom.isFinished()) {
        int j = paramCanvas.save();
        int m = getWidth() - getPaddingLeft() - getPaddingRight();
        int k = getHeight();
        paramCanvas.translate((-m + getPaddingLeft()), (Math.max(getScrollRange(), i) + k));
        paramCanvas.rotate(180.0F, m, 0.0F);
        this.mEdgeGlowBottom.setSize(m, k);
        if (this.mEdgeGlowBottom.draw(paramCanvas))
          ViewCompat.postInvalidateOnAnimation((View)this); 
        paramCanvas.restoreToCount(j);
      } 
    } 
  }
  
  public boolean executeKeyEvent(KeyEvent paramKeyEvent) {
    View view;
    boolean bool1 = false;
    this.mTempRect.setEmpty();
    if (!canScroll()) {
      boolean bool = bool1;
      if (isFocused()) {
        bool = bool1;
        if (paramKeyEvent.getKeyCode() != 4) {
          View view1 = findFocus();
          view = view1;
          if (view1 == this)
            view = null; 
          view = FocusFinder.getInstance().findNextFocus((ViewGroup)this, view, 130);
          bool = bool1;
          if (view != null) {
            bool = bool1;
            if (view != this) {
              bool = bool1;
              if (view.requestFocus(130))
                bool = true; 
            } 
          } 
        } 
      } 
      return bool;
    } 
    bool1 = false;
    boolean bool2 = bool1;
    if (view.getAction() == 0) {
      char c;
      switch (view.getKeyCode()) {
        default:
          return bool1;
        case 19:
          return !view.isAltPressed() ? arrowScroll(33) : fullScroll(33);
        case 20:
          return !view.isAltPressed() ? arrowScroll(130) : fullScroll(130);
        case 62:
          break;
      } 
      if (view.isShiftPressed()) {
        c = '!';
      } else {
        c = '??';
      } 
      pageScroll(c);
      bool2 = bool1;
    } 
    return bool2;
  }
  
  public void fling(int paramInt) {
    if (getChildCount() > 0) {
      int i = getHeight() - getPaddingBottom() - getPaddingTop();
      int j = getChildAt(0).getHeight();
      this.mScroller.fling(getScrollX(), getScrollY(), 0, paramInt, 0, 0, 0, Math.max(0, j - i), 0, i / 2);
      ViewCompat.postInvalidateOnAnimation((View)this);
    } 
  }
  
  public boolean fullScroll(int paramInt) {
    int i;
    if (paramInt == 130) {
      i = 1;
    } else {
      i = 0;
    } 
    int j = getHeight();
    this.mTempRect.top = 0;
    this.mTempRect.bottom = j;
    if (i) {
      i = getChildCount();
      if (i > 0) {
        View view = getChildAt(i - 1);
        this.mTempRect.bottom = view.getBottom() + getPaddingBottom();
        this.mTempRect.top = this.mTempRect.bottom - j;
      } 
    } 
    return scrollAndFocus(paramInt, this.mTempRect.top, this.mTempRect.bottom);
  }
  
  protected float getBottomFadingEdgeStrength() {
    if (getChildCount() == 0)
      return 0.0F; 
    int i = getVerticalFadingEdgeLength();
    int j = getHeight();
    int k = getPaddingBottom();
    j = getChildAt(0).getBottom() - getScrollY() - j - k;
    return (j < i) ? (j / i) : 1.0F;
  }
  
  public int getMaxScrollAmount() {
    return (int)(0.5F * getHeight());
  }
  
  public int getNestedScrollAxes() {
    return this.mParentHelper.getNestedScrollAxes();
  }
  
  int getScrollRange() {
    int i = 0;
    if (getChildCount() > 0)
      i = Math.max(0, getChildAt(0).getHeight() - getHeight() - getPaddingBottom() - getPaddingTop()); 
    return i;
  }
  
  protected float getTopFadingEdgeStrength() {
    if (getChildCount() == 0)
      return 0.0F; 
    int i = getVerticalFadingEdgeLength();
    int j = getScrollY();
    return (j < i) ? (j / i) : 1.0F;
  }
  
  public boolean hasNestedScrollingParent() {
    return this.mChildHelper.hasNestedScrollingParent();
  }
  
  public boolean isFillViewport() {
    return this.mFillViewport;
  }
  
  public boolean isNestedScrollingEnabled() {
    return this.mChildHelper.isNestedScrollingEnabled();
  }
  
  public boolean isSmoothScrollingEnabled() {
    return this.mSmoothScrollingEnabled;
  }
  
  protected void measureChild(View paramView, int paramInt1, int paramInt2) {
    ViewGroup.LayoutParams layoutParams = paramView.getLayoutParams();
    paramView.measure(getChildMeasureSpec(paramInt1, getPaddingLeft() + getPaddingRight(), layoutParams.width), View.MeasureSpec.makeMeasureSpec(0, 0));
  }
  
  protected void measureChildWithMargins(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    paramView.measure(getChildMeasureSpec(paramInt1, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + paramInt2, marginLayoutParams.width), View.MeasureSpec.makeMeasureSpec(marginLayoutParams.topMargin + marginLayoutParams.bottomMargin, 0));
  }
  
  public void onAttachedToWindow() {
    super.onAttachedToWindow();
    this.mIsLaidOut = false;
  }
  
  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent) {
    if ((paramMotionEvent.getSource() & 0x2) != 0) {
      switch (paramMotionEvent.getAction()) {
        default:
          return false;
        case 8:
          break;
      } 
      if (!this.mIsBeingDragged) {
        float f = MotionEventCompat.getAxisValue(paramMotionEvent, 9);
        if (f != 0.0F) {
          int i = (int)(getVerticalScrollFactorCompat() * f);
          int j = getScrollRange();
          int k = getScrollY();
          int m = k - i;
          if (m < 0) {
            i = 0;
          } else {
            i = m;
            if (m > j)
              i = j; 
          } 
          if (i != k) {
            super.scrollTo(getScrollX(), i);
            return true;
          } 
        } 
      } 
    } 
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    boolean bool = true;
    boolean bool1 = false;
    int i = paramMotionEvent.getAction();
    if (i != 2 || !this.mIsBeingDragged) {
      ViewParent viewParent;
      int j;
      switch (i & 0xFF) {
        default:
          return this.mIsBeingDragged;
        case 2:
          j = this.mActivePointerId;
          if (j != -1) {
            i = paramMotionEvent.findPointerIndex(j);
            if (i == -1) {
              Log.e("NestedScrollView", "Invalid pointerId=" + j + " in onInterceptTouchEvent");
            } else {
              i = (int)paramMotionEvent.getY(i);
              if (Math.abs(i - this.mLastMotionY) > this.mTouchSlop && (getNestedScrollAxes() & 0x2) == 0) {
                this.mIsBeingDragged = true;
                this.mLastMotionY = i;
                initVelocityTrackerIfNotExists();
                this.mVelocityTracker.addMovement(paramMotionEvent);
                this.mNestedYOffset = 0;
                viewParent = getParent();
                if (viewParent != null)
                  viewParent.requestDisallowInterceptTouchEvent(true); 
              } 
            } 
          } 
        case 0:
          i = (int)viewParent.getY();
          if (!inChild((int)viewParent.getX(), i)) {
            this.mIsBeingDragged = false;
            recycleVelocityTracker();
          } else {
            this.mLastMotionY = i;
            this.mActivePointerId = viewParent.getPointerId(0);
            initOrResetVelocityTracker();
            this.mVelocityTracker.addMovement((MotionEvent)viewParent);
            this.mScroller.computeScrollOffset();
            bool = bool1;
            if (!this.mScroller.isFinished())
              bool = true; 
            this.mIsBeingDragged = bool;
            startNestedScroll(2);
          } 
        case 1:
        case 3:
          this.mIsBeingDragged = false;
          this.mActivePointerId = -1;
          recycleVelocityTracker();
          if (this.mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange()))
            ViewCompat.postInvalidateOnAnimation((View)this); 
          stopNestedScroll();
        case 6:
          break;
      } 
      onSecondaryPointerUp((MotionEvent)viewParent);
    } 
    return bool;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    this.mIsLayoutDirty = false;
    if (this.mChildToScrollTo != null && isViewDescendantOf(this.mChildToScrollTo, (View)this))
      scrollToChild(this.mChildToScrollTo); 
    this.mChildToScrollTo = null;
    if (!this.mIsLaidOut) {
      if (this.mSavedState != null) {
        scrollTo(getScrollX(), this.mSavedState.scrollPosition);
        this.mSavedState = null;
      } 
      if (getChildCount() > 0) {
        paramInt1 = getChildAt(0).getMeasuredHeight();
      } else {
        paramInt1 = 0;
      } 
      paramInt1 = Math.max(0, paramInt1 - paramInt4 - paramInt2 - getPaddingBottom() - getPaddingTop());
      if (getScrollY() > paramInt1) {
        scrollTo(getScrollX(), paramInt1);
      } else if (getScrollY() < 0) {
        scrollTo(getScrollX(), 0);
      } 
    } 
    scrollTo(getScrollX(), getScrollY());
    this.mIsLaidOut = true;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    if (this.mFillViewport && View.MeasureSpec.getMode(paramInt2) != 0 && getChildCount() > 0) {
      View view = getChildAt(0);
      paramInt2 = getMeasuredHeight();
      if (view.getMeasuredHeight() < paramInt2) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
        view.measure(getChildMeasureSpec(paramInt1, getPaddingLeft() + getPaddingRight(), layoutParams.width), View.MeasureSpec.makeMeasureSpec(paramInt2 - getPaddingTop() - getPaddingBottom(), 1073741824));
      } 
    } 
  }
  
  public boolean onNestedFling(View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean) {
    if (!paramBoolean) {
      flingWithNestedDispatch((int)paramFloat2);
      return true;
    } 
    return false;
  }
  
  public boolean onNestedPreFling(View paramView, float paramFloat1, float paramFloat2) {
    return dispatchNestedPreFling(paramFloat1, paramFloat2);
  }
  
  public void onNestedPreScroll(View paramView, int paramInt1, int paramInt2, int[] paramArrayOfint) {
    dispatchNestedPreScroll(paramInt1, paramInt2, paramArrayOfint, (int[])null);
  }
  
  public void onNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramInt1 = getScrollY();
    scrollBy(0, paramInt4);
    paramInt1 = getScrollY() - paramInt1;
    dispatchNestedScroll(0, paramInt1, 0, paramInt4 - paramInt1, (int[])null);
  }
  
  public void onNestedScrollAccepted(View paramView1, View paramView2, int paramInt) {
    this.mParentHelper.onNestedScrollAccepted(paramView1, paramView2, paramInt);
    startNestedScroll(2);
  }
  
  protected void onOverScrolled(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2) {
    super.scrollTo(paramInt1, paramInt2);
  }
  
  protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect) {
    int i;
    View view;
    boolean bool = false;
    if (paramInt == 2) {
      i = 130;
    } else {
      i = paramInt;
      if (paramInt == 1)
        i = 33; 
    } 
    if (paramRect == null) {
      view = FocusFinder.getInstance().findNextFocus((ViewGroup)this, null, i);
    } else {
      view = FocusFinder.getInstance().findNextFocusFromRect((ViewGroup)this, paramRect, i);
    } 
    if (view != null && !isOffScreen(view))
      bool = view.requestFocus(i, paramRect); 
    return bool;
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    this.mSavedState = savedState;
    requestLayout();
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.scrollPosition = getScrollY();
    return (Parcelable)savedState;
  }
  
  protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (this.mOnScrollChangeListener != null)
      this.mOnScrollChangeListener.onScrollChange(this, paramInt1, paramInt2, paramInt3, paramInt4); 
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    View view = findFocus();
    if (view != null && this != view && isWithinDeltaOfScreen(view, 0, paramInt4)) {
      view.getDrawingRect(this.mTempRect);
      offsetDescendantRectToMyCoords(view, this.mTempRect);
      doScrollY(computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
    } 
  }
  
  public boolean onStartNestedScroll(View paramView1, View paramView2, int paramInt) {
    return ((paramInt & 0x2) != 0);
  }
  
  public void onStopNestedScroll(View paramView) {
    this.mParentHelper.onStopNestedScroll(paramView);
    stopNestedScroll();
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    VelocityTracker velocityTracker;
    boolean bool;
    int j;
    initVelocityTrackerIfNotExists();
    MotionEvent motionEvent = MotionEvent.obtain(paramMotionEvent);
    int i = MotionEventCompat.getActionMasked(paramMotionEvent);
    if (i == 0)
      this.mNestedYOffset = 0; 
    motionEvent.offsetLocation(0.0F, this.mNestedYOffset);
    switch (i) {
      default:
        if (this.mVelocityTracker != null)
          this.mVelocityTracker.addMovement(motionEvent); 
        motionEvent.recycle();
        return true;
      case 0:
        if (getChildCount() == 0)
          return false; 
        if (!this.mScroller.isFinished()) {
          bool = true;
        } else {
          bool = false;
        } 
        this.mIsBeingDragged = bool;
        if (bool) {
          ViewParent viewParent = getParent();
          if (viewParent != null)
            viewParent.requestDisallowInterceptTouchEvent(true); 
        } 
        if (!this.mScroller.isFinished())
          this.mScroller.abortAnimation(); 
        this.mLastMotionY = (int)paramMotionEvent.getY();
        this.mActivePointerId = paramMotionEvent.getPointerId(0);
        startNestedScroll(2);
      case 2:
        j = paramMotionEvent.findPointerIndex(this.mActivePointerId);
        if (j == -1) {
          Log.e("NestedScrollView", "Invalid pointerId=" + this.mActivePointerId + " in onTouchEvent");
        } else {
          int k = (int)paramMotionEvent.getY(j);
          i = this.mLastMotionY - k;
          int m = i;
          if (dispatchNestedPreScroll(0, i, this.mScrollConsumed, this.mScrollOffset)) {
            m = i - this.mScrollConsumed[1];
            motionEvent.offsetLocation(0.0F, this.mScrollOffset[1]);
            this.mNestedYOffset += this.mScrollOffset[1];
          } 
          i = m;
          if (!this.mIsBeingDragged) {
            i = m;
            if (Math.abs(m) > this.mTouchSlop) {
              ViewParent viewParent = getParent();
              if (viewParent != null)
                viewParent.requestDisallowInterceptTouchEvent(true); 
              this.mIsBeingDragged = true;
              if (m > 0) {
                i = m - this.mTouchSlop;
              } else {
                i = m + this.mTouchSlop;
              } 
            } 
          } 
          if (this.mIsBeingDragged) {
            this.mLastMotionY = k - this.mScrollOffset[1];
            int n = getScrollY();
            k = getScrollRange();
            m = getOverScrollMode();
            if (m == 0 || (m == 1 && k > 0)) {
              m = 1;
            } else {
              m = 0;
            } 
            if (overScrollByCompat(0, i, 0, getScrollY(), 0, k, 0, 0, true) && !hasNestedScrollingParent())
              this.mVelocityTracker.clear(); 
            int i1 = getScrollY() - n;
            if (dispatchNestedScroll(0, i1, 0, i - i1, this.mScrollOffset)) {
              this.mLastMotionY -= this.mScrollOffset[1];
              motionEvent.offsetLocation(0.0F, this.mScrollOffset[1]);
              this.mNestedYOffset += this.mScrollOffset[1];
            } else if (m != 0) {
              ensureGlows();
              m = n + i;
              if (m < 0) {
                this.mEdgeGlowTop.onPull(i / getHeight(), paramMotionEvent.getX(j) / getWidth());
                if (!this.mEdgeGlowBottom.isFinished())
                  this.mEdgeGlowBottom.onRelease(); 
              } else if (m > k) {
                this.mEdgeGlowBottom.onPull(i / getHeight(), 1.0F - paramMotionEvent.getX(j) / getWidth());
                if (!this.mEdgeGlowTop.isFinished())
                  this.mEdgeGlowTop.onRelease(); 
              } 
              if (this.mEdgeGlowTop != null && (!this.mEdgeGlowTop.isFinished() || !this.mEdgeGlowBottom.isFinished()))
                ViewCompat.postInvalidateOnAnimation((View)this); 
            } 
          } 
        } 
      case 1:
        if (this.mIsBeingDragged) {
          velocityTracker = this.mVelocityTracker;
          velocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
          i = (int)VelocityTrackerCompat.getYVelocity(velocityTracker, this.mActivePointerId);
          if (Math.abs(i) > this.mMinimumVelocity) {
            flingWithNestedDispatch(-i);
          } else if (this.mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
            ViewCompat.postInvalidateOnAnimation((View)this);
          } 
        } 
        this.mActivePointerId = -1;
        endDrag();
      case 3:
        if (this.mIsBeingDragged && getChildCount() > 0 && this.mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange()))
          ViewCompat.postInvalidateOnAnimation((View)this); 
        this.mActivePointerId = -1;
        endDrag();
      case 5:
        i = MotionEventCompat.getActionIndex((MotionEvent)velocityTracker);
        this.mLastMotionY = (int)velocityTracker.getY(i);
        this.mActivePointerId = velocityTracker.getPointerId(i);
      case 6:
        break;
    } 
    onSecondaryPointerUp((MotionEvent)velocityTracker);
    this.mLastMotionY = (int)velocityTracker.getY(velocityTracker.findPointerIndex(this.mActivePointerId));
  }
  
  boolean overScrollByCompat(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, boolean paramBoolean) {
    boolean bool1;
    boolean bool2;
    int i = getOverScrollMode();
    if (computeHorizontalScrollRange() > computeHorizontalScrollExtent()) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (computeVerticalScrollRange() > computeVerticalScrollExtent()) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (i == 0 || (i == 1 && bool1)) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (i == 0 || (i == 1 && bool2)) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    paramInt3 += paramInt1;
    if (!bool1)
      paramInt7 = 0; 
    paramInt4 += paramInt2;
    if (!bool2)
      paramInt8 = 0; 
    paramInt2 = -paramInt7;
    paramInt1 = paramInt7 + paramInt5;
    paramInt5 = -paramInt8;
    paramInt6 = paramInt8 + paramInt6;
    paramBoolean = false;
    if (paramInt3 > paramInt1) {
      paramBoolean = true;
    } else {
      paramInt1 = paramInt3;
      if (paramInt3 < paramInt2) {
        paramInt1 = paramInt2;
        paramBoolean = true;
      } 
    } 
    boolean bool3 = false;
    if (paramInt4 > paramInt6) {
      paramInt2 = paramInt6;
      bool3 = true;
    } else {
      paramInt2 = paramInt4;
      if (paramInt4 < paramInt5) {
        paramInt2 = paramInt5;
        bool3 = true;
      } 
    } 
    if (bool3)
      this.mScroller.springBack(paramInt1, paramInt2, 0, 0, 0, getScrollRange()); 
    onOverScrolled(paramInt1, paramInt2, paramBoolean, bool3);
    return (paramBoolean || bool3);
  }
  
  public boolean pageScroll(int paramInt) {
    int i;
    if (paramInt == 130) {
      i = 1;
    } else {
      i = 0;
    } 
    int j = getHeight();
    if (i) {
      this.mTempRect.top = getScrollY() + j;
      i = getChildCount();
      if (i > 0) {
        View view = getChildAt(i - 1);
        if (this.mTempRect.top + j > view.getBottom())
          this.mTempRect.top = view.getBottom() - j; 
      } 
      this.mTempRect.bottom = this.mTempRect.top + j;
      return scrollAndFocus(paramInt, this.mTempRect.top, this.mTempRect.bottom);
    } 
    this.mTempRect.top = getScrollY() - j;
    if (this.mTempRect.top < 0)
      this.mTempRect.top = 0; 
    this.mTempRect.bottom = this.mTempRect.top + j;
    return scrollAndFocus(paramInt, this.mTempRect.top, this.mTempRect.bottom);
  }
  
  public void requestChildFocus(View paramView1, View paramView2) {
    if (!this.mIsLayoutDirty) {
      scrollToChild(paramView2);
    } else {
      this.mChildToScrollTo = paramView2;
    } 
    super.requestChildFocus(paramView1, paramView2);
  }
  
  public boolean requestChildRectangleOnScreen(View paramView, Rect paramRect, boolean paramBoolean) {
    paramRect.offset(paramView.getLeft() - paramView.getScrollX(), paramView.getTop() - paramView.getScrollY());
    return scrollToChildRect(paramRect, paramBoolean);
  }
  
  public void requestDisallowInterceptTouchEvent(boolean paramBoolean) {
    if (paramBoolean)
      recycleVelocityTracker(); 
    super.requestDisallowInterceptTouchEvent(paramBoolean);
  }
  
  public void requestLayout() {
    this.mIsLayoutDirty = true;
    super.requestLayout();
  }
  
  public void scrollTo(int paramInt1, int paramInt2) {
    if (getChildCount() > 0) {
      View view = getChildAt(0);
      paramInt1 = clamp(paramInt1, getWidth() - getPaddingRight() - getPaddingLeft(), view.getWidth());
      paramInt2 = clamp(paramInt2, getHeight() - getPaddingBottom() - getPaddingTop(), view.getHeight());
      if (paramInt1 != getScrollX() || paramInt2 != getScrollY())
        super.scrollTo(paramInt1, paramInt2); 
    } 
  }
  
  public void setFillViewport(boolean paramBoolean) {
    if (paramBoolean != this.mFillViewport) {
      this.mFillViewport = paramBoolean;
      requestLayout();
    } 
  }
  
  public void setNestedScrollingEnabled(boolean paramBoolean) {
    this.mChildHelper.setNestedScrollingEnabled(paramBoolean);
  }
  
  public void setOnScrollChangeListener(OnScrollChangeListener paramOnScrollChangeListener) {
    this.mOnScrollChangeListener = paramOnScrollChangeListener;
  }
  
  public void setSmoothScrollingEnabled(boolean paramBoolean) {
    this.mSmoothScrollingEnabled = paramBoolean;
  }
  
  public boolean shouldDelayChildPressedState() {
    return true;
  }
  
  public final void smoothScrollBy(int paramInt1, int paramInt2) {
    if (getChildCount() != 0) {
      if (AnimationUtils.currentAnimationTimeMillis() - this.mLastScroll > 250L) {
        paramInt1 = getHeight();
        int i = getPaddingBottom();
        int j = getPaddingTop();
        i = Math.max(0, getChildAt(0).getHeight() - paramInt1 - i - j);
        paramInt1 = getScrollY();
        paramInt2 = Math.max(0, Math.min(paramInt1 + paramInt2, i));
        this.mScroller.startScroll(getScrollX(), paramInt1, 0, paramInt2 - paramInt1);
        ViewCompat.postInvalidateOnAnimation((View)this);
      } else {
        if (!this.mScroller.isFinished())
          this.mScroller.abortAnimation(); 
        scrollBy(paramInt1, paramInt2);
      } 
      this.mLastScroll = AnimationUtils.currentAnimationTimeMillis();
    } 
  }
  
  public final void smoothScrollTo(int paramInt1, int paramInt2) {
    smoothScrollBy(paramInt1 - getScrollX(), paramInt2 - getScrollY());
  }
  
  public boolean startNestedScroll(int paramInt) {
    return this.mChildHelper.startNestedScroll(paramInt);
  }
  
  public void stopNestedScroll() {
    this.mChildHelper.stopNestedScroll();
  }
  
  static class AccessibilityDelegate extends AccessibilityDelegateCompat {
    public void onInitializeAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      boolean bool;
      super.onInitializeAccessibilityEvent(param1View, param1AccessibilityEvent);
      NestedScrollView nestedScrollView = (NestedScrollView)param1View;
      param1AccessibilityEvent.setClassName(ScrollView.class.getName());
      AccessibilityRecordCompat accessibilityRecordCompat = AccessibilityEventCompat.asRecord(param1AccessibilityEvent);
      if (nestedScrollView.getScrollRange() > 0) {
        bool = true;
      } else {
        bool = false;
      } 
      accessibilityRecordCompat.setScrollable(bool);
      accessibilityRecordCompat.setScrollX(nestedScrollView.getScrollX());
      accessibilityRecordCompat.setScrollY(nestedScrollView.getScrollY());
      accessibilityRecordCompat.setMaxScrollX(nestedScrollView.getScrollX());
      accessibilityRecordCompat.setMaxScrollY(nestedScrollView.getScrollRange());
    }
    
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
      NestedScrollView nestedScrollView = (NestedScrollView)param1View;
      param1AccessibilityNodeInfoCompat.setClassName(ScrollView.class.getName());
      if (nestedScrollView.isEnabled()) {
        int i = nestedScrollView.getScrollRange();
        if (i > 0) {
          param1AccessibilityNodeInfoCompat.setScrollable(true);
          if (nestedScrollView.getScrollY() > 0)
            param1AccessibilityNodeInfoCompat.addAction(8192); 
          if (nestedScrollView.getScrollY() < i)
            param1AccessibilityNodeInfoCompat.addAction(4096); 
        } 
      } 
    }
    
    public boolean performAccessibilityAction(View param1View, int param1Int, Bundle param1Bundle) {
      boolean bool = true;
      if (!super.performAccessibilityAction(param1View, param1Int, param1Bundle)) {
        NestedScrollView nestedScrollView = (NestedScrollView)param1View;
        if (!nestedScrollView.isEnabled())
          return false; 
        switch (param1Int) {
          default:
            return false;
          case 4096:
            i = nestedScrollView.getHeight();
            j = nestedScrollView.getPaddingBottom();
            param1Int = nestedScrollView.getPaddingTop();
            param1Int = Math.min(nestedScrollView.getScrollY() + i - j - param1Int, nestedScrollView.getScrollRange());
            if (param1Int != nestedScrollView.getScrollY()) {
              nestedScrollView.smoothScrollTo(0, param1Int);
              return bool;
            } 
            return false;
          case 8192:
            break;
        } 
        int j = nestedScrollView.getHeight();
        int i = nestedScrollView.getPaddingBottom();
        param1Int = nestedScrollView.getPaddingTop();
        param1Int = Math.max(nestedScrollView.getScrollY() - j - i - param1Int, 0);
        if (param1Int != nestedScrollView.getScrollY()) {
          nestedScrollView.smoothScrollTo(0, param1Int);
          return bool;
        } 
        bool = false;
      } 
      return bool;
    }
  }
  
  public static interface OnScrollChangeListener {
    void onScrollChange(NestedScrollView param1NestedScrollView, int param1Int1, int param1Int2, int param1Int3, int param1Int4);
  }
  
  static class SavedState extends View.BaseSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public NestedScrollView.SavedState createFromParcel(Parcel param2Parcel) {
          return new NestedScrollView.SavedState(param2Parcel);
        }
        
        public NestedScrollView.SavedState[] newArray(int param2Int) {
          return new NestedScrollView.SavedState[param2Int];
        }
      };
    
    public int scrollPosition;
    
    SavedState(Parcel param1Parcel) {
      super(param1Parcel);
      this.scrollPosition = param1Parcel.readInt();
    }
    
    SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public String toString() {
      return "HorizontalScrollView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " scrollPosition=" + this.scrollPosition + "}";
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.scrollPosition);
    }
  }
  
  static final class null implements Parcelable.Creator<SavedState> {
    public NestedScrollView.SavedState createFromParcel(Parcel param1Parcel) {
      return new NestedScrollView.SavedState(param1Parcel);
    }
    
    public NestedScrollView.SavedState[] newArray(int param1Int) {
      return new NestedScrollView.SavedState[param1Int];
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/widget/NestedScrollView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */