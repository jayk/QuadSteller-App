package app.gamer.quadstellar.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LazyViewPager extends ViewGroup {
  private static final Comparator<ItemInfo> COMPARATOR;
  
  private static final boolean DEBUG = false;
  
  private static final int DEFAULT_OFFSCREEN_PAGES = 0;
  
  private static final int INVALID_POINTER = -1;
  
  private static final int[] LAYOUT_ATTRS = new int[] { 16842931 };
  
  private static final int MAX_SETTLE_DURATION = 600;
  
  private static final int MIN_DISTANCE_FOR_FLING = 25;
  
  public static final int SCROLL_STATE_DRAGGING = 1;
  
  public static final int SCROLL_STATE_IDLE = 0;
  
  public static final int SCROLL_STATE_SETTLING = 2;
  
  private static final String TAG = "LazyViewPager";
  
  private static final boolean USE_CACHE = false;
  
  private static final Interpolator sInterpolator;
  
  private int mActivePointerId = -1;
  
  private PagerAdapter mAdapter;
  
  private OnAdapterChangeListener mAdapterChangeListener;
  
  private int mBottomPageBounds;
  
  private boolean mCalledSuper;
  
  private int mChildHeightMeasureSpec;
  
  private int mChildWidthMeasureSpec;
  
  private int mCurItem;
  
  private int mDecorChildCount;
  
  private long mFakeDragBeginTime;
  
  private boolean mFakeDragging;
  
  private boolean mFirstLayout = true;
  
  private int mFlingDistance;
  
  private boolean mInLayout;
  
  private float mInitialMotionX;
  
  private OnPageChangeListener mInternalPageChangeListener;
  
  private boolean mIsBeingDragged;
  
  private boolean mIsUnableToDrag;
  
  private final ArrayList<ItemInfo> mItems = new ArrayList<ItemInfo>();
  
  private float mLastMotionX;
  
  private float mLastMotionY;
  
  private EdgeEffectCompat mLeftEdge;
  
  private Drawable mMarginDrawable;
  
  private int mMaximumVelocity;
  
  private int mMinimumVelocity;
  
  private PagerObserver mObserver;
  
  private int mOffscreenPageLimit = 0;
  
  private OnPageChangeListener mOnPageChangeListener;
  
  private int mPageMargin;
  
  private boolean mPopulatePending;
  
  private Parcelable mRestoredAdapterState = null;
  
  private ClassLoader mRestoredClassLoader = null;
  
  private int mRestoredCurItem = -1;
  
  private EdgeEffectCompat mRightEdge;
  
  private int mScrollState = 0;
  
  private Scroller mScroller;
  
  private boolean mScrolling;
  
  private boolean mScrollingCacheEnabled;
  
  private int mTopPageBounds;
  
  private int mTouchSlop;
  
  private VelocityTracker mVelocityTracker;
  
  static {
    COMPARATOR = new Comparator<ItemInfo>() {
        public int compare(LazyViewPager.ItemInfo param1ItemInfo1, LazyViewPager.ItemInfo param1ItemInfo2) {
          return param1ItemInfo1.position - param1ItemInfo2.position;
        }
      };
    sInterpolator = new Interpolator() {
        public float getInterpolation(float param1Float) {
          param1Float--;
          return param1Float * param1Float * param1Float * param1Float * param1Float + 1.0F;
        }
      };
  }
  
  public LazyViewPager(Context paramContext) {
    super(paramContext);
    initViewPager();
  }
  
  public LazyViewPager(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    initViewPager();
  }
  
  private void completeScroll() {
    boolean bool = this.mScrolling;
    if (bool) {
      setScrollingCacheEnabled(false);
      this.mScroller.abortAnimation();
      int i = getScrollX();
      int j = getScrollY();
      int k = this.mScroller.getCurrX();
      int m = this.mScroller.getCurrY();
      if (i != k || j != m)
        scrollTo(k, m); 
      setScrollState(0);
    } 
    this.mPopulatePending = false;
    this.mScrolling = false;
    for (byte b = 0; b < this.mItems.size(); b++) {
      ItemInfo itemInfo = this.mItems.get(b);
      if (itemInfo.scrolling) {
        bool = true;
        itemInfo.scrolling = false;
      } 
    } 
    if (bool)
      populate(); 
  }
  
  private int determineTargetPage(int paramInt1, float paramFloat, int paramInt2, int paramInt3) {
    if (Math.abs(paramInt3) > this.mFlingDistance && Math.abs(paramInt2) > this.mMinimumVelocity) {
      if (paramInt2 <= 0)
        paramInt1++; 
      return paramInt1;
    } 
    return (int)(paramInt1 + paramFloat + 0.5F);
  }
  
  private void endDrag() {
    this.mIsBeingDragged = false;
    this.mIsUnableToDrag = false;
    if (this.mVelocityTracker != null) {
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
    } 
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
    int i = getWidth() + this.mPageMargin;
    int j = paramInt / i;
    paramInt %= i;
    float f = paramInt / i;
    this.mCalledSuper = false;
    onPageScrolled(j, f, paramInt);
    if (!this.mCalledSuper)
      throw new IllegalStateException("onPageScrolled did not call superclass implementation"); 
  }
  
  private void recomputeScrollPosition(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramInt1 += paramInt3;
    if (paramInt2 > 0) {
      paramInt3 = getScrollX();
      paramInt4 = paramInt2 + paramInt4;
      paramInt2 = paramInt3 / paramInt4;
      float f = (paramInt3 % paramInt4) / paramInt4;
      paramInt4 = (int)((paramInt2 + f) * paramInt1);
      scrollTo(paramInt4, getScrollY());
      if (!this.mScroller.isFinished()) {
        paramInt3 = this.mScroller.getDuration();
        paramInt2 = this.mScroller.timePassed();
        this.mScroller.startScroll(paramInt4, 0, this.mCurItem * paramInt1, 0, paramInt3 - paramInt2);
      } 
      return;
    } 
    paramInt1 = this.mCurItem * paramInt1;
    if (paramInt1 != getScrollX()) {
      completeScroll();
      scrollTo(paramInt1, getScrollY());
    } 
  }
  
  private void removeNonDecorViews() {
    for (int i = 0; i < getChildCount(); i = j + 1) {
      int j = i;
      if (!((LayoutParams)getChildAt(i).getLayoutParams()).isDecor) {
        removeViewAt(i);
        j = i - 1;
      } 
    } 
  }
  
  private void setScrollState(int paramInt) {
    if (this.mScrollState != paramInt) {
      this.mScrollState = paramInt;
      if (this.mOnPageChangeListener != null)
        this.mOnPageChangeListener.onPageScrollStateChanged(paramInt); 
    } 
  }
  
  private void setScrollingCacheEnabled(boolean paramBoolean) {
    if (this.mScrollingCacheEnabled != paramBoolean)
      this.mScrollingCacheEnabled = paramBoolean; 
  }
  
  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2) {
    int i = paramArrayList.size();
    int j = getDescendantFocusability();
    if (j != 393216)
      for (byte b = 0; b < getChildCount(); b++) {
        View view = getChildAt(b);
        if (view.getVisibility() == 0) {
          ItemInfo itemInfo = infoForChild(view);
          if (itemInfo != null && itemInfo.position == this.mCurItem)
            view.addFocusables(paramArrayList, paramInt1, paramInt2); 
        } 
      }  
    if ((j != 262144 || i == paramArrayList.size()) && isFocusable() && ((paramInt2 & 0x1) != 1 || !isInTouchMode() || isFocusableInTouchMode()) && paramArrayList != null)
      paramArrayList.add(this); 
  }
  
  void addNewItem(int paramInt1, int paramInt2) {
    ItemInfo itemInfo = new ItemInfo();
    itemInfo.position = paramInt1;
    itemInfo.object = this.mAdapter.instantiateItem(this, paramInt1);
    if (paramInt2 < 0) {
      this.mItems.add(itemInfo);
      return;
    } 
    this.mItems.add(paramInt2, itemInfo);
  }
  
  public void addTouchables(ArrayList<View> paramArrayList) {
    for (byte b = 0; b < getChildCount(); b++) {
      View view = getChildAt(b);
      if (view.getVisibility() == 0) {
        ItemInfo itemInfo = infoForChild(view);
        if (itemInfo != null && itemInfo.position == this.mCurItem)
          view.addTouchables(paramArrayList); 
      } 
    } 
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    ViewGroup.LayoutParams layoutParams = paramLayoutParams;
    if (!checkLayoutParams(paramLayoutParams))
      layoutParams = generateLayoutParams(paramLayoutParams); 
    paramLayoutParams = layoutParams;
    ((LayoutParams)paramLayoutParams).isDecor |= paramView instanceof Decor;
    if (this.mInLayout) {
      if (paramLayoutParams != null && ((LayoutParams)paramLayoutParams).isDecor)
        throw new IllegalStateException("Cannot add pager decor view during layout"); 
      addViewInLayout(paramView, paramInt, layoutParams);
      paramView.measure(this.mChildWidthMeasureSpec, this.mChildHeightMeasureSpec);
      return;
    } 
    super.addView(paramView, paramInt, layoutParams);
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
        if (view2 != null && view1.getLeft() >= view2.getLeft()) {
          bool = pageLeft();
        } else {
          bool = view1.requestFocus();
        } 
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
  
  public boolean beginFakeDrag() {
    boolean bool = false;
    if (!this.mIsBeingDragged) {
      this.mFakeDragging = true;
      setScrollState(1);
      this.mLastMotionX = 0.0F;
      this.mInitialMotionX = 0.0F;
      if (this.mVelocityTracker == null) {
        this.mVelocityTracker = VelocityTracker.obtain();
      } else {
        this.mVelocityTracker.clear();
      } 
      long l = SystemClock.uptimeMillis();
      MotionEvent motionEvent = MotionEvent.obtain(l, l, 0, 0.0F, 0.0F, 0);
      this.mVelocityTracker.addMovement(motionEvent);
      motionEvent.recycle();
      this.mFakeDragBeginTime = l;
      bool = true;
    } 
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
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (paramLayoutParams instanceof LayoutParams && super.checkLayoutParams(paramLayoutParams));
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
  
  void dataSetChanged() {
    boolean bool;
    if (this.mItems.size() < 3 && this.mItems.size() < this.mAdapter.getCount()) {
      bool = true;
    } else {
      bool = false;
    } 
    int i = -1;
    byte b = 0;
    int j = 0;
    while (j < this.mItems.size()) {
      int m;
      byte b1;
      int n;
      ItemInfo itemInfo = this.mItems.get(j);
      int k = this.mAdapter.getItemPosition(itemInfo.object);
      if (k == -1) {
        m = i;
        b1 = b;
        n = j;
      } else if (k == -2) {
        this.mItems.remove(j);
        k = j - 1;
        j = b;
        if (!b) {
          this.mAdapter.startUpdate(this);
          j = 1;
        } 
        this.mAdapter.destroyItem(this, itemInfo.position, itemInfo.object);
        b = 1;
        n = k;
        b1 = j;
        bool = b;
        m = i;
        if (this.mCurItem == itemInfo.position) {
          m = Math.max(0, Math.min(this.mCurItem, this.mAdapter.getCount() - 1));
          n = k;
          b1 = j;
          bool = b;
        } 
      } else {
        n = j;
        b1 = b;
        m = i;
        if (itemInfo.position != k) {
          if (itemInfo.position == this.mCurItem)
            i = k; 
          itemInfo.position = k;
          bool = true;
          n = j;
          b1 = b;
          m = i;
        } 
      } 
      j = n + 1;
      b = b1;
      i = m;
    } 
    if (b != 0)
      this.mAdapter.finishUpdate(this); 
    Collections.sort(this.mItems, COMPARATOR);
    if (i >= 0) {
      setCurrentItemInternal(i, false, true);
      bool = true;
    } 
    if (bool) {
      populate();
      requestLayout();
    } 
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
    return (super.dispatchKeyEvent(paramKeyEvent) || executeKeyEvent(paramKeyEvent));
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getChildCount : ()I
    //   4: istore_2
    //   5: iconst_0
    //   6: istore_3
    //   7: iload_3
    //   8: iload_2
    //   9: if_icmpge -> 73
    //   12: aload_0
    //   13: iload_3
    //   14: invokevirtual getChildAt : (I)Landroid/view/View;
    //   17: astore #4
    //   19: aload #4
    //   21: invokevirtual getVisibility : ()I
    //   24: ifne -> 67
    //   27: aload_0
    //   28: aload #4
    //   30: invokevirtual infoForChild : (Landroid/view/View;)Lapp/gamer/quadstellar/utils/LazyViewPager$ItemInfo;
    //   33: astore #5
    //   35: aload #5
    //   37: ifnull -> 67
    //   40: aload #5
    //   42: getfield position : I
    //   45: aload_0
    //   46: getfield mCurItem : I
    //   49: if_icmpne -> 67
    //   52: aload #4
    //   54: aload_1
    //   55: invokevirtual dispatchPopulateAccessibilityEvent : (Landroid/view/accessibility/AccessibilityEvent;)Z
    //   58: ifeq -> 67
    //   61: iconst_1
    //   62: istore #6
    //   64: iload #6
    //   66: ireturn
    //   67: iinc #3, 1
    //   70: goto -> 7
    //   73: iconst_0
    //   74: istore #6
    //   76: goto -> 64
  }
  
  float distanceInfluenceForSnapDuration(float paramFloat) {
    return (float)Math.sin((float)((paramFloat - 0.5F) * 0.4712389167638204D));
  }
  
  public void draw(Canvas paramCanvas) {
    boolean bool1;
    boolean bool = true;
    super.draw(paramCanvas);
    int i = 0;
    int j = 0;
    int k = ViewCompat.getOverScrollMode((View)this);
    if (k == 0 || (k == 1 && this.mAdapter != null && this.mAdapter.getCount() > 1)) {
      if (!this.mLeftEdge.isFinished()) {
        i = paramCanvas.save();
        j = getHeight() - getPaddingTop() - getPaddingBottom();
        paramCanvas.rotate(270.0F);
        paramCanvas.translate((-j + getPaddingTop()), 0.0F);
        this.mLeftEdge.setSize(j, getWidth());
        j = false | this.mLeftEdge.draw(paramCanvas);
        paramCanvas.restoreToCount(i);
      } 
      i = j;
      if (!this.mRightEdge.isFinished()) {
        k = paramCanvas.save();
        int m = getWidth();
        int n = getHeight();
        int i1 = getPaddingTop();
        int i2 = getPaddingBottom();
        i = bool;
        if (this.mAdapter != null)
          i = this.mAdapter.getCount(); 
        paramCanvas.rotate(90.0F);
        paramCanvas.translate(-getPaddingTop(), (-i * (this.mPageMargin + m) + this.mPageMargin));
        this.mRightEdge.setSize(n - i1 - i2, m);
        bool1 = j | this.mRightEdge.draw(paramCanvas);
        paramCanvas.restoreToCount(k);
      } 
    } else {
      this.mLeftEdge.finish();
      this.mRightEdge.finish();
    } 
    if (bool1)
      invalidate(); 
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    Drawable drawable = this.mMarginDrawable;
    if (drawable != null && drawable.isStateful())
      drawable.setState(getDrawableState()); 
  }
  
  public void endFakeDrag() {
    if (!this.mFakeDragging)
      throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first."); 
    VelocityTracker velocityTracker = this.mVelocityTracker;
    velocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
    int i = (int)VelocityTrackerCompat.getYVelocity(velocityTracker, this.mActivePointerId);
    this.mPopulatePending = true;
    int j = (int)(this.mLastMotionX - this.mInitialMotionX);
    int k = getScrollX();
    int m = getWidth() + this.mPageMargin;
    setCurrentItemInternal(determineTargetPage(k / m, (k % m) / m, i, j), true, true, i);
    endDrag();
    this.mFakeDragging = false;
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
  
  public void fakeDragBy(float paramFloat) {
    if (!this.mFakeDragging)
      throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first."); 
    this.mLastMotionX += paramFloat;
    float f1 = getScrollX() - paramFloat;
    int i = getWidth() + this.mPageMargin;
    paramFloat = Math.max(0, (this.mCurItem - 1) * i);
    float f2 = (Math.min(this.mCurItem + 1, this.mAdapter.getCount() - 1) * i);
    if (f1 >= paramFloat) {
      paramFloat = f1;
      if (f1 > f2)
        paramFloat = f2; 
    } 
    this.mLastMotionX += paramFloat - (int)paramFloat;
    scrollTo((int)paramFloat, getScrollY());
    pageScrolled((int)paramFloat);
    long l = SystemClock.uptimeMillis();
    MotionEvent motionEvent = MotionEvent.obtain(this.mFakeDragBeginTime, l, 2, this.mLastMotionX, 0.0F, 0);
    this.mVelocityTracker.addMovement(motionEvent);
    motionEvent.recycle();
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams();
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return generateDefaultLayoutParams();
  }
  
  public PagerAdapter getAdapter() {
    return this.mAdapter;
  }
  
  public int getCurrentItem() {
    return this.mCurItem;
  }
  
  public int getOffscreenPageLimit() {
    return this.mOffscreenPageLimit;
  }
  
  public int getPageMargin() {
    return this.mPageMargin;
  }
  
  ItemInfo infoForAnyChild(View paramView) {
    while (true) {
      ViewParent viewParent = paramView.getParent();
      if (viewParent != this) {
        if (viewParent == null || !(viewParent instanceof View))
          return null; 
        paramView = (View)viewParent;
        continue;
      } 
      return infoForChild(paramView);
    } 
  }
  
  ItemInfo infoForChild(View paramView) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: iload_2
    //   3: aload_0
    //   4: getfield mItems : Ljava/util/ArrayList;
    //   7: invokevirtual size : ()I
    //   10: if_icmpge -> 50
    //   13: aload_0
    //   14: getfield mItems : Ljava/util/ArrayList;
    //   17: iload_2
    //   18: invokevirtual get : (I)Ljava/lang/Object;
    //   21: checkcast app/gamer/quadstellar/utils/LazyViewPager$ItemInfo
    //   24: astore_3
    //   25: aload_0
    //   26: getfield mAdapter : Landroid/support/v4/view/PagerAdapter;
    //   29: aload_1
    //   30: aload_3
    //   31: getfield object : Ljava/lang/Object;
    //   34: invokevirtual isViewFromObject : (Landroid/view/View;Ljava/lang/Object;)Z
    //   37: ifeq -> 44
    //   40: aload_3
    //   41: astore_1
    //   42: aload_1
    //   43: areturn
    //   44: iinc #2, 1
    //   47: goto -> 2
    //   50: aconst_null
    //   51: astore_1
    //   52: goto -> 42
  }
  
  void initViewPager() {
    setWillNotDraw(false);
    setDescendantFocusability(262144);
    setFocusable(true);
    Context context = getContext();
    this.mScroller = new Scroller(context, sInterpolator);
    ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
    this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(viewConfiguration);
    this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
    this.mLeftEdge = new EdgeEffectCompat(context);
    this.mRightEdge = new EdgeEffectCompat(context);
    this.mFlingDistance = (int)(25.0F * (context.getResources().getDisplayMetrics()).density);
  }
  
  public boolean isFakeDragging() {
    return this.mFakeDragging;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    this.mFirstLayout = true;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    if (this.mPageMargin > 0 && this.mMarginDrawable != null) {
      int i = getScrollX();
      int j = getWidth();
      int k = i % (this.mPageMargin + j);
      if (k != 0) {
        j = i - k + j;
        this.mMarginDrawable.setBounds(j, this.mTopPageBounds, this.mPageMargin + j, this.mBottomPageBounds);
        this.mMarginDrawable.draw(paramCanvas);
      } 
    } 
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    float f;
    int i = paramMotionEvent.getAction() & 0xFF;
    if (i == 3 || i == 1) {
      this.mIsBeingDragged = false;
      this.mIsUnableToDrag = false;
      this.mActivePointerId = -1;
      if (this.mVelocityTracker != null) {
        this.mVelocityTracker.recycle();
        this.mVelocityTracker = null;
      } 
      return false;
    } 
    if (i != 0) {
      if (this.mIsBeingDragged)
        return true; 
      if (this.mIsUnableToDrag)
        return false; 
    } 
    switch (i) {
      default:
        if (!this.mIsBeingDragged) {
          if (this.mVelocityTracker == null)
            this.mVelocityTracker = VelocityTracker.obtain(); 
          this.mVelocityTracker.addMovement(paramMotionEvent);
        } 
        return this.mIsBeingDragged;
      case 2:
        i = this.mActivePointerId;
        if (i != -1) {
          i = MotionEventCompat.findPointerIndex(paramMotionEvent, i);
          float f1 = MotionEventCompat.getX(paramMotionEvent, i);
          float f2 = f1 - this.mLastMotionX;
          float f3 = Math.abs(f2);
          float f4 = MotionEventCompat.getY(paramMotionEvent, i);
          float f5 = Math.abs(f4 - this.mLastMotionY);
          if (canScroll((View)this, false, (int)f2, (int)f1, (int)f4)) {
            this.mLastMotionX = f1;
            this.mInitialMotionX = f1;
            this.mLastMotionY = f4;
            return false;
          } 
          if (f3 > this.mTouchSlop && f3 > f5) {
            this.mIsBeingDragged = true;
            setScrollState(1);
            this.mLastMotionX = f1;
            setScrollingCacheEnabled(true);
          } else if (f5 > this.mTouchSlop) {
            this.mIsUnableToDrag = true;
          } 
        } 
      case 0:
        f = paramMotionEvent.getX();
        this.mInitialMotionX = f;
        this.mLastMotionX = f;
        this.mLastMotionY = paramMotionEvent.getY();
        this.mActivePointerId = MotionEventCompat.getPointerId(paramMotionEvent, 0);
        if (this.mScrollState == 2) {
          this.mIsBeingDragged = true;
          this.mIsUnableToDrag = false;
          setScrollState(1);
        } else {
          completeScroll();
          this.mIsBeingDragged = false;
          this.mIsUnableToDrag = false;
        } 
      case 6:
        break;
    } 
    onSecondaryPointerUp(paramMotionEvent);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    // Byte code:
    //   0: aload_0
    //   1: iconst_1
    //   2: putfield mInLayout : Z
    //   5: aload_0
    //   6: invokevirtual populate : ()V
    //   9: aload_0
    //   10: iconst_0
    //   11: putfield mInLayout : Z
    //   14: aload_0
    //   15: invokevirtual getChildCount : ()I
    //   18: istore #6
    //   20: iload #4
    //   22: iload_2
    //   23: isub
    //   24: istore #7
    //   26: iload #5
    //   28: iload_3
    //   29: isub
    //   30: istore #8
    //   32: aload_0
    //   33: invokevirtual getPaddingLeft : ()I
    //   36: istore_3
    //   37: aload_0
    //   38: invokevirtual getPaddingTop : ()I
    //   41: istore_2
    //   42: aload_0
    //   43: invokevirtual getPaddingRight : ()I
    //   46: istore #9
    //   48: aload_0
    //   49: invokevirtual getPaddingBottom : ()I
    //   52: istore #5
    //   54: aload_0
    //   55: invokevirtual getScrollX : ()I
    //   58: istore #10
    //   60: iconst_0
    //   61: istore #11
    //   63: iconst_0
    //   64: istore #12
    //   66: iload #12
    //   68: iload #6
    //   70: if_icmpge -> 511
    //   73: aload_0
    //   74: iload #12
    //   76: invokevirtual getChildAt : (I)Landroid/view/View;
    //   79: astore #13
    //   81: iload #11
    //   83: istore #14
    //   85: iload #5
    //   87: istore #15
    //   89: iload_3
    //   90: istore #16
    //   92: iload #9
    //   94: istore #17
    //   96: iload_2
    //   97: istore #4
    //   99: aload #13
    //   101: invokevirtual getVisibility : ()I
    //   104: bipush #8
    //   106: if_icmpeq -> 273
    //   109: aload #13
    //   111: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   114: checkcast app/gamer/quadstellar/utils/LazyViewPager$LayoutParams
    //   117: astore #18
    //   119: aload #18
    //   121: getfield isDecor : Z
    //   124: ifeq -> 419
    //   127: aload #18
    //   129: getfield gravity : I
    //   132: istore #4
    //   134: aload #18
    //   136: getfield gravity : I
    //   139: istore #17
    //   141: iload #4
    //   143: bipush #7
    //   145: iand
    //   146: tableswitch default -> 180, 1 -> 312, 2 -> 180, 3 -> 297, 4 -> 180, 5 -> 334
    //   180: iload_3
    //   181: istore #4
    //   183: iload_3
    //   184: istore #16
    //   186: iload #17
    //   188: bipush #112
    //   190: iand
    //   191: lookupswitch default -> 224, 16 -> 376, 48 -> 363, 80 -> 394
    //   224: iload_2
    //   225: istore_3
    //   226: iload #4
    //   228: iload #10
    //   230: iadd
    //   231: istore #4
    //   233: iload #11
    //   235: iconst_1
    //   236: iadd
    //   237: istore #14
    //   239: aload #13
    //   241: iload #4
    //   243: iload_3
    //   244: aload #13
    //   246: invokevirtual getMeasuredWidth : ()I
    //   249: iload #4
    //   251: iadd
    //   252: aload #13
    //   254: invokevirtual getMeasuredHeight : ()I
    //   257: iload_3
    //   258: iadd
    //   259: invokevirtual layout : (IIII)V
    //   262: iload_2
    //   263: istore #4
    //   265: iload #9
    //   267: istore #17
    //   269: iload #5
    //   271: istore #15
    //   273: iinc #12, 1
    //   276: iload #14
    //   278: istore #11
    //   280: iload #15
    //   282: istore #5
    //   284: iload #16
    //   286: istore_3
    //   287: iload #17
    //   289: istore #9
    //   291: iload #4
    //   293: istore_2
    //   294: goto -> 66
    //   297: iload_3
    //   298: istore #4
    //   300: iload_3
    //   301: aload #13
    //   303: invokevirtual getMeasuredWidth : ()I
    //   306: iadd
    //   307: istore #16
    //   309: goto -> 186
    //   312: iload #7
    //   314: aload #13
    //   316: invokevirtual getMeasuredWidth : ()I
    //   319: isub
    //   320: iconst_2
    //   321: idiv
    //   322: iload_3
    //   323: invokestatic max : (II)I
    //   326: istore #4
    //   328: iload_3
    //   329: istore #16
    //   331: goto -> 186
    //   334: iload #7
    //   336: iload #9
    //   338: isub
    //   339: aload #13
    //   341: invokevirtual getMeasuredWidth : ()I
    //   344: isub
    //   345: istore #4
    //   347: iload #9
    //   349: aload #13
    //   351: invokevirtual getMeasuredWidth : ()I
    //   354: iadd
    //   355: istore #9
    //   357: iload_3
    //   358: istore #16
    //   360: goto -> 186
    //   363: iload_2
    //   364: istore_3
    //   365: iload_2
    //   366: aload #13
    //   368: invokevirtual getMeasuredHeight : ()I
    //   371: iadd
    //   372: istore_2
    //   373: goto -> 226
    //   376: iload #8
    //   378: aload #13
    //   380: invokevirtual getMeasuredHeight : ()I
    //   383: isub
    //   384: iconst_2
    //   385: idiv
    //   386: iload_2
    //   387: invokestatic max : (II)I
    //   390: istore_3
    //   391: goto -> 226
    //   394: iload #8
    //   396: iload #5
    //   398: isub
    //   399: aload #13
    //   401: invokevirtual getMeasuredHeight : ()I
    //   404: isub
    //   405: istore_3
    //   406: iload #5
    //   408: aload #13
    //   410: invokevirtual getMeasuredHeight : ()I
    //   413: iadd
    //   414: istore #5
    //   416: goto -> 226
    //   419: aload_0
    //   420: aload #13
    //   422: invokevirtual infoForChild : (Landroid/view/View;)Lapp/gamer/quadstellar/utils/LazyViewPager$ItemInfo;
    //   425: astore #18
    //   427: iload #11
    //   429: istore #14
    //   431: iload #5
    //   433: istore #15
    //   435: iload_3
    //   436: istore #16
    //   438: iload #9
    //   440: istore #17
    //   442: iload_2
    //   443: istore #4
    //   445: aload #18
    //   447: ifnull -> 273
    //   450: iload_3
    //   451: aload_0
    //   452: getfield mPageMargin : I
    //   455: iload #7
    //   457: iadd
    //   458: aload #18
    //   460: getfield position : I
    //   463: imul
    //   464: iadd
    //   465: istore #4
    //   467: aload #13
    //   469: iload #4
    //   471: iload_2
    //   472: aload #13
    //   474: invokevirtual getMeasuredWidth : ()I
    //   477: iload #4
    //   479: iadd
    //   480: aload #13
    //   482: invokevirtual getMeasuredHeight : ()I
    //   485: iload_2
    //   486: iadd
    //   487: invokevirtual layout : (IIII)V
    //   490: iload #11
    //   492: istore #14
    //   494: iload #5
    //   496: istore #15
    //   498: iload_3
    //   499: istore #16
    //   501: iload #9
    //   503: istore #17
    //   505: iload_2
    //   506: istore #4
    //   508: goto -> 273
    //   511: aload_0
    //   512: iload_2
    //   513: putfield mTopPageBounds : I
    //   516: aload_0
    //   517: iload #8
    //   519: iload #5
    //   521: isub
    //   522: putfield mBottomPageBounds : I
    //   525: aload_0
    //   526: iload #11
    //   528: putfield mDecorChildCount : I
    //   531: aload_0
    //   532: iconst_0
    //   533: putfield mFirstLayout : Z
    //   536: return
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    setMeasuredDimension(getDefaultSize(0, paramInt1), getDefaultSize(0, paramInt2));
    paramInt2 = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    int i = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
    int j = getChildCount();
    paramInt1 = 0;
    while (paramInt1 < j) {
      View view = getChildAt(paramInt1);
      int k = i;
      int m = paramInt2;
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        k = i;
        m = paramInt2;
        if (layoutParams != null) {
          k = i;
          m = paramInt2;
          if (layoutParams.isDecor) {
            int n = layoutParams.gravity & 0x7;
            m = layoutParams.gravity & 0x70;
            Log.d("LazyViewPager", "gravity: " + layoutParams.gravity + " hgrav: " + n + " vgrav: " + m);
            int i1 = Integer.MIN_VALUE;
            int i2 = Integer.MIN_VALUE;
            if (m == 48 || m == 80) {
              m = 1;
            } else {
              m = 0;
            } 
            if (n == 3 || n == 5) {
              n = 1;
            } else {
              n = 0;
            } 
            if (m != 0) {
              k = 1073741824;
            } else {
              k = i1;
              if (n != 0) {
                i2 = 1073741824;
                k = i1;
              } 
            } 
            view.measure(View.MeasureSpec.makeMeasureSpec(paramInt2, k), View.MeasureSpec.makeMeasureSpec(i, i2));
            if (m != 0) {
              k = i - view.getMeasuredHeight();
              m = paramInt2;
            } else {
              k = i;
              m = paramInt2;
              if (n != 0) {
                m = paramInt2 - view.getMeasuredWidth();
                k = i;
              } 
            } 
          } 
        } 
      } 
      paramInt1++;
      i = k;
      paramInt2 = m;
    } 
    this.mChildWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824);
    this.mChildHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(i, 1073741824);
    this.mInLayout = true;
    populate();
    this.mInLayout = false;
    paramInt2 = getChildCount();
    for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++) {
      View view = getChildAt(paramInt1);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams == null || !layoutParams.isDecor)
          view.measure(this.mChildWidthMeasureSpec, this.mChildHeightMeasureSpec); 
      } 
    } 
  }
  
  protected void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mDecorChildCount : I
    //   4: ifle -> 247
    //   7: aload_0
    //   8: invokevirtual getScrollX : ()I
    //   11: istore #4
    //   13: aload_0
    //   14: invokevirtual getPaddingLeft : ()I
    //   17: istore #5
    //   19: aload_0
    //   20: invokevirtual getPaddingRight : ()I
    //   23: istore #6
    //   25: aload_0
    //   26: invokevirtual getWidth : ()I
    //   29: istore #7
    //   31: aload_0
    //   32: invokevirtual getChildCount : ()I
    //   35: istore #8
    //   37: iconst_0
    //   38: istore #9
    //   40: iload #9
    //   42: iload #8
    //   44: if_icmpge -> 247
    //   47: aload_0
    //   48: iload #9
    //   50: invokevirtual getChildAt : (I)Landroid/view/View;
    //   53: astore #10
    //   55: aload #10
    //   57: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   60: checkcast app/gamer/quadstellar/utils/LazyViewPager$LayoutParams
    //   63: astore #11
    //   65: aload #11
    //   67: getfield isDecor : Z
    //   70: ifne -> 95
    //   73: iload #6
    //   75: istore #12
    //   77: iload #5
    //   79: istore #13
    //   81: iinc #9, 1
    //   84: iload #13
    //   86: istore #5
    //   88: iload #12
    //   90: istore #6
    //   92: goto -> 40
    //   95: aload #11
    //   97: getfield gravity : I
    //   100: bipush #7
    //   102: iand
    //   103: tableswitch default -> 136, 1 -> 201, 2 -> 136, 3 -> 184, 4 -> 136, 5 -> 221
    //   136: iload #5
    //   138: istore #12
    //   140: iload #12
    //   142: iload #4
    //   144: iadd
    //   145: aload #10
    //   147: invokevirtual getLeft : ()I
    //   150: isub
    //   151: istore #14
    //   153: iload #5
    //   155: istore #13
    //   157: iload #6
    //   159: istore #12
    //   161: iload #14
    //   163: ifeq -> 81
    //   166: aload #10
    //   168: iload #14
    //   170: invokevirtual offsetLeftAndRight : (I)V
    //   173: iload #5
    //   175: istore #13
    //   177: iload #6
    //   179: istore #12
    //   181: goto -> 81
    //   184: iload #5
    //   186: istore #12
    //   188: iload #5
    //   190: aload #10
    //   192: invokevirtual getWidth : ()I
    //   195: iadd
    //   196: istore #5
    //   198: goto -> 140
    //   201: iload #7
    //   203: aload #10
    //   205: invokevirtual getMeasuredWidth : ()I
    //   208: isub
    //   209: iconst_2
    //   210: idiv
    //   211: iload #5
    //   213: invokestatic max : (II)I
    //   216: istore #12
    //   218: goto -> 140
    //   221: iload #7
    //   223: iload #6
    //   225: isub
    //   226: aload #10
    //   228: invokevirtual getMeasuredWidth : ()I
    //   231: isub
    //   232: istore #12
    //   234: iload #6
    //   236: aload #10
    //   238: invokevirtual getMeasuredWidth : ()I
    //   241: iadd
    //   242: istore #6
    //   244: goto -> 140
    //   247: aload_0
    //   248: getfield mOnPageChangeListener : Lapp/gamer/quadstellar/utils/LazyViewPager$OnPageChangeListener;
    //   251: ifnull -> 266
    //   254: aload_0
    //   255: getfield mOnPageChangeListener : Lapp/gamer/quadstellar/utils/LazyViewPager$OnPageChangeListener;
    //   258: iload_1
    //   259: fload_2
    //   260: iload_3
    //   261: invokeinterface onPageScrolled : (IFI)V
    //   266: aload_0
    //   267: getfield mInternalPageChangeListener : Lapp/gamer/quadstellar/utils/LazyViewPager$OnPageChangeListener;
    //   270: ifnull -> 285
    //   273: aload_0
    //   274: getfield mInternalPageChangeListener : Lapp/gamer/quadstellar/utils/LazyViewPager$OnPageChangeListener;
    //   277: iload_1
    //   278: fload_2
    //   279: iload_3
    //   280: invokeinterface onPageScrolled : (IFI)V
    //   285: aload_0
    //   286: iconst_1
    //   287: putfield mCalledSuper : Z
    //   290: return
  }
  
  protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getChildCount : ()I
    //   4: istore_3
    //   5: iload_1
    //   6: iconst_2
    //   7: iand
    //   8: ifeq -> 80
    //   11: iconst_0
    //   12: istore #4
    //   14: iconst_1
    //   15: istore #5
    //   17: iload #4
    //   19: iload_3
    //   20: if_icmpeq -> 103
    //   23: aload_0
    //   24: iload #4
    //   26: invokevirtual getChildAt : (I)Landroid/view/View;
    //   29: astore #6
    //   31: aload #6
    //   33: invokevirtual getVisibility : ()I
    //   36: ifne -> 93
    //   39: aload_0
    //   40: aload #6
    //   42: invokevirtual infoForChild : (Landroid/view/View;)Lapp/gamer/quadstellar/utils/LazyViewPager$ItemInfo;
    //   45: astore #7
    //   47: aload #7
    //   49: ifnull -> 93
    //   52: aload #7
    //   54: getfield position : I
    //   57: aload_0
    //   58: getfield mCurItem : I
    //   61: if_icmpne -> 93
    //   64: aload #6
    //   66: iload_1
    //   67: aload_2
    //   68: invokevirtual requestFocus : (ILandroid/graphics/Rect;)Z
    //   71: ifeq -> 93
    //   74: iconst_1
    //   75: istore #8
    //   77: iload #8
    //   79: ireturn
    //   80: iload_3
    //   81: iconst_1
    //   82: isub
    //   83: istore #4
    //   85: iconst_m1
    //   86: istore #5
    //   88: iconst_m1
    //   89: istore_3
    //   90: goto -> 17
    //   93: iload #4
    //   95: iload #5
    //   97: iadd
    //   98: istore #4
    //   100: goto -> 17
    //   103: iconst_0
    //   104: istore #8
    //   106: goto -> 77
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    if (this.mAdapter != null) {
      this.mAdapter.restoreState(savedState.adapterState, savedState.loader);
      setCurrentItemInternal(savedState.position, false, true);
      return;
    } 
    this.mRestoredCurItem = savedState.position;
    this.mRestoredAdapterState = savedState.adapterState;
    this.mRestoredClassLoader = savedState.loader;
  }
  
  public Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.position = this.mCurItem;
    if (this.mAdapter != null)
      savedState.adapterState = this.mAdapter.saveState(); 
    return (Parcelable)savedState;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 != paramInt3)
      recomputeScrollPosition(paramInt1, paramInt3, this.mPageMargin, this.mPageMargin); 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    float f;
    if (this.mFakeDragging)
      return true; 
    if (paramMotionEvent.getAction() == 0 && paramMotionEvent.getEdgeFlags() != 0)
      return false; 
    if (this.mAdapter == null || this.mAdapter.getCount() == 0)
      return false; 
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
    this.mVelocityTracker.addMovement(paramMotionEvent);
    int i = paramMotionEvent.getAction();
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    boolean bool5 = false;
    null = bool5;
    switch (i & 0xFF) {
      default:
        null = bool5;
      case 4:
        if (null)
          invalidate(); 
        return true;
      case 0:
        completeScroll();
        f = paramMotionEvent.getX();
        this.mInitialMotionX = f;
        this.mLastMotionX = f;
        this.mActivePointerId = MotionEventCompat.getPointerId(paramMotionEvent, 0);
        bool1 = bool5;
      case 2:
        if (!this.mIsBeingDragged) {
          i = MotionEventCompat.findPointerIndex(paramMotionEvent, this.mActivePointerId);
          float f1 = MotionEventCompat.getX(paramMotionEvent, i);
          f = Math.abs(f1 - this.mLastMotionX);
          float f2 = Math.abs(MotionEventCompat.getY(paramMotionEvent, i) - this.mLastMotionY);
          if (f > this.mTouchSlop && f > f2) {
            this.mIsBeingDragged = true;
            this.mLastMotionX = f1;
            setScrollState(1);
            setScrollingCacheEnabled(true);
          } 
        } 
        bool1 = bool5;
        if (this.mIsBeingDragged) {
          float f1 = MotionEventCompat.getX(paramMotionEvent, MotionEventCompat.findPointerIndex(paramMotionEvent, this.mActivePointerId));
          f = this.mLastMotionX;
          this.mLastMotionX = f1;
          float f2 = getScrollX() + f - f1;
          int j = getWidth();
          i = j + this.mPageMargin;
          int k = this.mAdapter.getCount() - 1;
          f = Math.max(0, (this.mCurItem - 1) * i);
          f1 = (Math.min(this.mCurItem + 1, k) * i);
          if (f2 < f) {
            bool1 = bool2;
            if (f == 0.0F) {
              f1 = -f2;
              bool1 = this.mLeftEdge.onPull(f1 / j);
            } 
          } else {
            bool1 = bool3;
            f = f2;
            if (f2 > f1) {
              bool1 = bool4;
              if (f1 == (k * i))
                bool1 = this.mRightEdge.onPull((f2 - f1) / j); 
              f = f1;
            } 
          } 
          this.mLastMotionX += f - (int)f;
          scrollTo((int)f, getScrollY());
          pageScrolled((int)f);
        } 
      case 1:
        bool1 = bool5;
        if (this.mIsBeingDragged) {
          VelocityTracker velocityTracker = this.mVelocityTracker;
          velocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
          i = (int)VelocityTrackerCompat.getXVelocity(velocityTracker, this.mActivePointerId);
          this.mPopulatePending = true;
          int k = getWidth() + this.mPageMargin;
          int m = getScrollX();
          setCurrentItemInternal(determineTargetPage(m / k, (m % k) / k, i, (int)(MotionEventCompat.getX(paramMotionEvent, MotionEventCompat.findPointerIndex(paramMotionEvent, this.mActivePointerId)) - this.mInitialMotionX)), true, true, i);
          this.mActivePointerId = -1;
          endDrag();
          int j = this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
        } 
      case 3:
        bool1 = bool5;
        if (this.mIsBeingDragged) {
          setCurrentItemInternal(this.mCurItem, true, true);
          this.mActivePointerId = -1;
          endDrag();
          int j = this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
        } 
      case 5:
        i = MotionEventCompat.getActionIndex(paramMotionEvent);
        this.mLastMotionX = MotionEventCompat.getX(paramMotionEvent, i);
        this.mActivePointerId = MotionEventCompat.getPointerId(paramMotionEvent, i);
        bool1 = bool5;
      case 6:
        break;
    } 
    onSecondaryPointerUp(paramMotionEvent);
    this.mLastMotionX = MotionEventCompat.getX(paramMotionEvent, MotionEventCompat.findPointerIndex(paramMotionEvent, this.mActivePointerId));
    boolean bool1 = bool5;
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
    if (this.mAdapter != null && this.mCurItem < this.mAdapter.getCount() - 1) {
      setCurrentItem(this.mCurItem + 1, true);
      return null;
    } 
    return false;
  }
  
  void populate() {
    if (this.mAdapter != null && !this.mPopulatePending && getWindowToken() != null) {
      this.mAdapter.startUpdate(this);
      int i = this.mOffscreenPageLimit;
      int j = Math.max(0, this.mCurItem - i);
      int k = Math.min(this.mAdapter.getCount() - 1, this.mCurItem + i);
      int m = -1;
      i = 0;
      label71: while (i < this.mItems.size()) {
        ItemInfo itemInfo1 = this.mItems.get(i);
        if ((itemInfo1.position < j || itemInfo1.position > k) && !itemInfo1.scrolling) {
          this.mItems.remove(i);
          int i1 = i - 1;
          this.mAdapter.destroyItem(this, itemInfo1.position, itemInfo1.object);
          continue;
        } 
        int n = i;
        if (m < k) {
          n = i;
          if (itemInfo1.position > j) {
            n = m + 1;
            m = i;
            int i1 = n;
            if (n < j) {
              i1 = j;
              m = i;
            } 
            while (true) {
              n = m;
              if (i1 <= k) {
                n = m;
                if (i1 < itemInfo1.position) {
                  addNewItem(i1, m);
                  i1++;
                  m++;
                  continue;
                } 
              } 
              m = itemInfo1.position;
              i = n + 1;
              continue label71;
            } 
          } 
          continue;
        } 
        continue;
      } 
      if (this.mItems.size() > 0) {
        i = ((ItemInfo)this.mItems.get(this.mItems.size() - 1)).position;
      } else {
        i = -1;
      } 
      if (i < k) {
        if (++i <= j)
          i = j; 
        while (i <= k) {
          addNewItem(i, -1);
          i++;
        } 
      } 
      ItemInfo itemInfo = null;
      i = 0;
      while (true) {
        ItemInfo itemInfo1 = itemInfo;
        if (i < this.mItems.size())
          if (((ItemInfo)this.mItems.get(i)).position == this.mCurItem) {
            itemInfo1 = this.mItems.get(i);
          } else {
            i++;
            continue;
          }  
        PagerAdapter pagerAdapter = this.mAdapter;
        i = this.mCurItem;
        if (itemInfo1 != null) {
          Object object = itemInfo1.object;
        } else {
          itemInfo1 = null;
        } 
        pagerAdapter.setPrimaryItem(this, i, itemInfo1);
        this.mAdapter.finishUpdate(this);
        if (hasFocus()) {
          View view = findFocus();
          if (view != null) {
            ItemInfo itemInfo2 = infoForAnyChild(view);
          } else {
            view = null;
          } 
          if (view == null || ((ItemInfo)view).position != this.mCurItem) {
            i = 0;
            while (true) {
              if (i < getChildCount()) {
                view = getChildAt(i);
                ItemInfo itemInfo2 = infoForChild(view);
                if (itemInfo2 == null || itemInfo2.position != this.mCurItem || !view.requestFocus(2)) {
                  i++;
                  continue;
                } 
              } 
              return;
            } 
            break;
          } 
          // Byte code: goto -> 7
        } 
        // Byte code: goto -> 7
      } 
    } 
  }
  
  public void setAdapter(PagerAdapter paramPagerAdapter) {
    if (this.mAdapter != null) {
      this.mAdapter.startUpdate(this);
      for (byte b = 0; b < this.mItems.size(); b++) {
        ItemInfo itemInfo = this.mItems.get(b);
        this.mAdapter.destroyItem(this, itemInfo.position, itemInfo.object);
      } 
      this.mAdapter.finishUpdate(this);
      this.mItems.clear();
      removeNonDecorViews();
      this.mCurItem = 0;
      scrollTo(0, 0);
    } 
    PagerAdapter pagerAdapter = this.mAdapter;
    this.mAdapter = paramPagerAdapter;
    if (this.mAdapter != null) {
      if (this.mObserver == null)
        this.mObserver = new PagerObserver(); 
      this.mPopulatePending = false;
      if (this.mRestoredCurItem >= 0) {
        this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
        setCurrentItemInternal(this.mRestoredCurItem, false, true);
        this.mRestoredCurItem = -1;
        this.mRestoredAdapterState = null;
        this.mRestoredClassLoader = null;
      } else {
        populate();
      } 
    } 
    if (this.mAdapterChangeListener != null && pagerAdapter != paramPagerAdapter)
      this.mAdapterChangeListener.onAdapterChanged(pagerAdapter, paramPagerAdapter); 
  }
  
  public void setCurrentItem(int paramInt) {
    boolean bool;
    this.mPopulatePending = false;
    if (!this.mFirstLayout) {
      bool = true;
    } else {
      bool = false;
    } 
    setCurrentItemInternal(paramInt, bool, false);
  }
  
  public void setCurrentItem(int paramInt, boolean paramBoolean) {
    this.mPopulatePending = false;
    setCurrentItemInternal(paramInt, paramBoolean, false);
  }
  
  void setCurrentItemInternal(int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
    setCurrentItemInternal(paramInt, paramBoolean1, paramBoolean2, 0);
  }
  
  void setCurrentItemInternal(int paramInt1, boolean paramBoolean1, boolean paramBoolean2, int paramInt2) {
    int j;
    int i = 1;
    if (this.mAdapter == null || this.mAdapter.getCount() <= 0) {
      setScrollingCacheEnabled(false);
      return;
    } 
    if (!paramBoolean2 && this.mCurItem == paramInt1 && this.mItems.size() != 0) {
      setScrollingCacheEnabled(false);
      return;
    } 
    if (paramInt1 < 0) {
      j = 0;
    } else {
      j = paramInt1;
      if (paramInt1 >= this.mAdapter.getCount())
        j = this.mAdapter.getCount() - 1; 
    } 
    paramInt1 = this.mOffscreenPageLimit;
    if (j > this.mCurItem + paramInt1 || j < this.mCurItem - paramInt1)
      for (paramInt1 = 0; paramInt1 < this.mItems.size(); paramInt1++)
        ((ItemInfo)this.mItems.get(paramInt1)).scrolling = true;  
    if (this.mCurItem != j) {
      paramInt1 = i;
    } else {
      paramInt1 = 0;
    } 
    this.mCurItem = j;
    populate();
    i = (getWidth() + this.mPageMargin) * j;
    if (paramBoolean1) {
      smoothScrollTo(i, 0, paramInt2);
      if (paramInt1 != 0 && this.mOnPageChangeListener != null)
        this.mOnPageChangeListener.onPageSelected(j); 
      if (paramInt1 != 0 && this.mInternalPageChangeListener != null)
        this.mInternalPageChangeListener.onPageSelected(j); 
      return;
    } 
    if (paramInt1 != 0 && this.mOnPageChangeListener != null)
      this.mOnPageChangeListener.onPageSelected(j); 
    if (paramInt1 != 0 && this.mInternalPageChangeListener != null)
      this.mInternalPageChangeListener.onPageSelected(j); 
    completeScroll();
    scrollTo(i, 0);
  }
  
  OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener paramOnPageChangeListener) {
    OnPageChangeListener onPageChangeListener = this.mInternalPageChangeListener;
    this.mInternalPageChangeListener = paramOnPageChangeListener;
    return onPageChangeListener;
  }
  
  public void setOffscreenPageLimit(int paramInt) {
    int i = paramInt;
    if (paramInt < 0) {
      Log.w("LazyViewPager", "Requested offscreen page limit " + paramInt + " too small; defaulting to " + Character.MIN_VALUE);
      i = 0;
    } 
    if (i != this.mOffscreenPageLimit) {
      this.mOffscreenPageLimit = i;
      populate();
    } 
  }
  
  void setOnAdapterChangeListener(OnAdapterChangeListener paramOnAdapterChangeListener) {
    this.mAdapterChangeListener = paramOnAdapterChangeListener;
  }
  
  public void setOnPageChangeListener(OnPageChangeListener paramOnPageChangeListener) {
    this.mOnPageChangeListener = paramOnPageChangeListener;
  }
  
  public void setPageMargin(int paramInt) {
    int i = this.mPageMargin;
    this.mPageMargin = paramInt;
    int j = getWidth();
    recomputeScrollPosition(j, j, paramInt, i);
    requestLayout();
  }
  
  public void setPageMarginDrawable(int paramInt) {
    setPageMarginDrawable(getContext().getResources().getDrawable(paramInt));
  }
  
  public void setPageMarginDrawable(Drawable paramDrawable) {
    boolean bool;
    this.mMarginDrawable = paramDrawable;
    if (paramDrawable != null)
      refreshDrawableState(); 
    if (paramDrawable == null) {
      bool = true;
    } else {
      bool = false;
    } 
    setWillNotDraw(bool);
    invalidate();
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
      setScrollState(0);
      return;
    } 
    setScrollingCacheEnabled(true);
    this.mScrolling = true;
    setScrollState(2);
    paramInt1 = getWidth();
    int m = paramInt1 / 2;
    float f1 = Math.min(1.0F, 1.0F * Math.abs(k) / paramInt1);
    float f2 = m;
    float f3 = m;
    f1 = distanceInfluenceForSnapDuration(f1);
    paramInt3 = Math.abs(paramInt3);
    if (paramInt3 > 0) {
      paramInt1 = Math.round(1000.0F * Math.abs((f2 + f3 * f1) / paramInt3)) * 4;
    } else {
      paramInt1 = (int)((1.0F + Math.abs(k) / (this.mPageMargin + paramInt1)) * 100.0F);
    } 
    paramInt1 = Math.min(paramInt1, 600);
    this.mScroller.startScroll(i, j, k, paramInt2, paramInt1);
    invalidate();
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable) {
    return (super.verifyDrawable(paramDrawable) || paramDrawable == this.mMarginDrawable);
  }
  
  static interface Decor {}
  
  static class ItemInfo {
    Object object;
    
    int position;
    
    boolean scrolling;
  }
  
  public static class LayoutParams extends ViewGroup.LayoutParams {
    public int gravity;
    
    public boolean isDecor;
    
    public LayoutParams() {
      super(-1, -1);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, LazyViewPager.LAYOUT_ATTRS);
      this.gravity = typedArray.getInteger(0, 0);
      typedArray.recycle();
    }
  }
  
  static interface OnAdapterChangeListener {
    void onAdapterChanged(PagerAdapter param1PagerAdapter1, PagerAdapter param1PagerAdapter2);
  }
  
  public static interface OnPageChangeListener {
    void onPageScrollStateChanged(int param1Int);
    
    void onPageScrolled(int param1Int1, float param1Float, int param1Int2);
    
    void onPageSelected(int param1Int);
  }
  
  private class PagerObserver extends DataSetObserver {
    private PagerObserver() {}
    
    public void onChanged() {
      LazyViewPager.this.dataSetChanged();
    }
    
    public void onInvalidated() {
      LazyViewPager.this.dataSetChanged();
    }
  }
  
  public static class SavedState extends View.BaseSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
          public LazyViewPager.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
            return new LazyViewPager.SavedState(param2Parcel, param2ClassLoader);
          }
          
          public LazyViewPager.SavedState[] newArray(int param2Int) {
            return new LazyViewPager.SavedState[param2Int];
          }
        });
    
    Parcelable adapterState;
    
    ClassLoader loader;
    
    int position;
    
    SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel);
      ClassLoader classLoader = param1ClassLoader;
      if (param1ClassLoader == null)
        classLoader = getClass().getClassLoader(); 
      this.position = param1Parcel.readInt();
      this.adapterState = param1Parcel.readParcelable(classLoader);
      this.loader = classLoader;
    }
    
    public SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public String toString() {
      return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + this.position + "}";
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.position);
      param1Parcel.writeParcelable(this.adapterState, param1Int);
    }
  }
  
  static final class null implements ParcelableCompatCreatorCallbacks<SavedState> {
    public LazyViewPager.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new LazyViewPager.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public LazyViewPager.SavedState[] newArray(int param1Int) {
      return new LazyViewPager.SavedState[param1Int];
    }
  }
  
  public static class SimpleOnPageChangeListener implements OnPageChangeListener {
    public void onPageScrollStateChanged(int param1Int) {}
    
    public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {}
    
    public void onPageSelected(int param1Int) {}
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/utils/LazyViewPager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */