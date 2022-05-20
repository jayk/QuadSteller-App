package android.support.v4.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewPager extends ViewGroup {
  private static final int CLOSE_ENOUGH = 2;
  
  private static final Comparator<ItemInfo> COMPARATOR;
  
  private static final boolean DEBUG = false;
  
  private static final int DEFAULT_GUTTER_SIZE = 16;
  
  private static final int DEFAULT_OFFSCREEN_PAGES = 1;
  
  private static final int DRAW_ORDER_DEFAULT = 0;
  
  private static final int DRAW_ORDER_FORWARD = 1;
  
  private static final int DRAW_ORDER_REVERSE = 2;
  
  private static final int INVALID_POINTER = -1;
  
  static final int[] LAYOUT_ATTRS = new int[] { 16842931 };
  
  private static final int MAX_SETTLE_DURATION = 600;
  
  private static final int MIN_DISTANCE_FOR_FLING = 25;
  
  private static final int MIN_FLING_VELOCITY = 400;
  
  public static final int SCROLL_STATE_DRAGGING = 1;
  
  public static final int SCROLL_STATE_IDLE = 0;
  
  public static final int SCROLL_STATE_SETTLING = 2;
  
  private static final String TAG = "ViewPager";
  
  private static final boolean USE_CACHE = false;
  
  private static final Interpolator sInterpolator;
  
  private static final ViewPositionComparator sPositionComparator;
  
  private int mActivePointerId = -1;
  
  PagerAdapter mAdapter;
  
  private List<OnAdapterChangeListener> mAdapterChangeListeners;
  
  private int mBottomPageBounds;
  
  private boolean mCalledSuper;
  
  private int mChildHeightMeasureSpec;
  
  private int mChildWidthMeasureSpec;
  
  private int mCloseEnough;
  
  int mCurItem;
  
  private int mDecorChildCount;
  
  private int mDefaultGutterSize;
  
  private int mDrawingOrder;
  
  private ArrayList<View> mDrawingOrderedChildren;
  
  private final Runnable mEndScrollRunnable = new Runnable() {
      public void run() {
        ViewPager.this.setScrollState(0);
        ViewPager.this.populate();
      }
    };
  
  private int mExpectedAdapterCount;
  
  private long mFakeDragBeginTime;
  
  private boolean mFakeDragging;
  
  private boolean mFirstLayout = true;
  
  private float mFirstOffset = -3.4028235E38F;
  
  private int mFlingDistance;
  
  private int mGutterSize;
  
  private boolean mInLayout;
  
  private float mInitialMotionX;
  
  private float mInitialMotionY;
  
  private OnPageChangeListener mInternalPageChangeListener;
  
  private boolean mIsBeingDragged;
  
  private boolean mIsScrollStarted;
  
  private boolean mIsUnableToDrag;
  
  private final ArrayList<ItemInfo> mItems = new ArrayList<ItemInfo>();
  
  private float mLastMotionX;
  
  private float mLastMotionY;
  
  private float mLastOffset = Float.MAX_VALUE;
  
  private EdgeEffectCompat mLeftEdge;
  
  private Drawable mMarginDrawable;
  
  private int mMaximumVelocity;
  
  private int mMinimumVelocity;
  
  private boolean mNeedCalculatePageOffsets = false;
  
  private PagerObserver mObserver;
  
  private int mOffscreenPageLimit = 1;
  
  private OnPageChangeListener mOnPageChangeListener;
  
  private List<OnPageChangeListener> mOnPageChangeListeners;
  
  private int mPageMargin;
  
  private PageTransformer mPageTransformer;
  
  private int mPageTransformerLayerType;
  
  private boolean mPopulatePending;
  
  private Parcelable mRestoredAdapterState = null;
  
  private ClassLoader mRestoredClassLoader = null;
  
  private int mRestoredCurItem = -1;
  
  private EdgeEffectCompat mRightEdge;
  
  private int mScrollState = 0;
  
  private Scroller mScroller;
  
  private boolean mScrollingCacheEnabled;
  
  private Method mSetChildrenDrawingOrderEnabled;
  
  private final ItemInfo mTempItem = new ItemInfo();
  
  private final Rect mTempRect = new Rect();
  
  private int mTopPageBounds;
  
  private int mTouchSlop;
  
  private VelocityTracker mVelocityTracker;
  
  static {
    COMPARATOR = new Comparator<ItemInfo>() {
        public int compare(ViewPager.ItemInfo param1ItemInfo1, ViewPager.ItemInfo param1ItemInfo2) {
          return param1ItemInfo1.position - param1ItemInfo2.position;
        }
      };
    sInterpolator = new Interpolator() {
        public float getInterpolation(float param1Float) {
          param1Float--;
          return param1Float * param1Float * param1Float * param1Float * param1Float + 1.0F;
        }
      };
    sPositionComparator = new ViewPositionComparator();
  }
  
  public ViewPager(Context paramContext) {
    super(paramContext);
    initViewPager();
  }
  
  public ViewPager(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    initViewPager();
  }
  
  private void calculatePageOffsets(ItemInfo paramItemInfo1, int paramInt, ItemInfo paramItemInfo2) {
    float f1;
    int i = this.mAdapter.getCount();
    int j = getClientWidth();
    if (j > 0) {
      f1 = this.mPageMargin / j;
    } else {
      f1 = 0.0F;
    } 
    if (paramItemInfo2 != null) {
      j = paramItemInfo2.position;
      if (j < paramItemInfo1.position) {
        byte b = 0;
        f2 = paramItemInfo2.offset + paramItemInfo2.widthFactor + f1;
        while (++j <= paramItemInfo1.position && b < this.mItems.size()) {
          float f;
          int n;
          paramItemInfo2 = this.mItems.get(b);
          while (true) {
            f = f2;
            n = j;
            if (j > paramItemInfo2.position) {
              f = f2;
              n = j;
              if (b < this.mItems.size() - 1) {
                paramItemInfo2 = this.mItems.get(++b);
                continue;
              } 
            } 
            break;
          } 
          while (n < paramItemInfo2.position) {
            f += this.mAdapter.getPageWidth(n) + f1;
            n++;
          } 
          paramItemInfo2.offset = f;
          f2 = f + paramItemInfo2.widthFactor + f1;
          j = n + 1;
        } 
      } else if (j > paramItemInfo1.position) {
        int n = this.mItems.size() - 1;
        f2 = paramItemInfo2.offset;
        while (--j >= paramItemInfo1.position && n >= 0) {
          float f;
          int i1;
          paramItemInfo2 = this.mItems.get(n);
          while (true) {
            f = f2;
            i1 = j;
            if (j < paramItemInfo2.position) {
              f = f2;
              i1 = j;
              if (n > 0) {
                paramItemInfo2 = this.mItems.get(--n);
                continue;
              } 
            } 
            break;
          } 
          while (i1 > paramItemInfo2.position) {
            f -= this.mAdapter.getPageWidth(i1) + f1;
            i1--;
          } 
          f2 = f - paramItemInfo2.widthFactor + f1;
          paramItemInfo2.offset = f2;
          j = i1 - 1;
        } 
      } 
    } 
    int m = this.mItems.size();
    float f3 = paramItemInfo1.offset;
    j = paramItemInfo1.position - 1;
    if (paramItemInfo1.position == 0) {
      f2 = paramItemInfo1.offset;
    } else {
      f2 = -3.4028235E38F;
    } 
    this.mFirstOffset = f2;
    if (paramItemInfo1.position == i - 1) {
      f2 = paramItemInfo1.offset + paramItemInfo1.widthFactor - 1.0F;
    } else {
      f2 = Float.MAX_VALUE;
    } 
    this.mLastOffset = f2;
    int k = paramInt - 1;
    float f2 = f3;
    while (k >= 0) {
      paramItemInfo2 = this.mItems.get(k);
      while (j > paramItemInfo2.position) {
        f2 -= this.mAdapter.getPageWidth(j) + f1;
        j--;
      } 
      f2 -= paramItemInfo2.widthFactor + f1;
      paramItemInfo2.offset = f2;
      if (paramItemInfo2.position == 0)
        this.mFirstOffset = f2; 
      k--;
      j--;
    } 
    f2 = paramItemInfo1.offset + paramItemInfo1.widthFactor + f1;
    j = paramItemInfo1.position + 1;
    k = paramInt + 1;
    paramInt = j;
    j = k;
    while (j < m) {
      paramItemInfo1 = this.mItems.get(j);
      while (paramInt < paramItemInfo1.position) {
        f2 += this.mAdapter.getPageWidth(paramInt) + f1;
        paramInt++;
      } 
      if (paramItemInfo1.position == i - 1)
        this.mLastOffset = paramItemInfo1.widthFactor + f2 - 1.0F; 
      paramItemInfo1.offset = f2;
      f2 += paramItemInfo1.widthFactor + f1;
      j++;
      paramInt++;
    } 
    this.mNeedCalculatePageOffsets = false;
  }
  
  private void completeScroll(boolean paramBoolean) {
    int i = 1;
    if (this.mScrollState == 2) {
      b1 = 1;
    } else {
      b1 = 0;
    } 
    if (b1) {
      setScrollingCacheEnabled(false);
      if (this.mScroller.isFinished())
        i = 0; 
      if (i) {
        this.mScroller.abortAnimation();
        int j = getScrollX();
        i = getScrollY();
        int k = this.mScroller.getCurrX();
        int m = this.mScroller.getCurrY();
        if (j != k || i != m) {
          scrollTo(k, m);
          if (k != j)
            pageScrolled(k); 
        } 
      } 
    } 
    this.mPopulatePending = false;
    byte b2 = 0;
    i = b1;
    byte b1;
    for (b1 = b2; b1 < this.mItems.size(); b1++) {
      ItemInfo itemInfo = this.mItems.get(b1);
      if (itemInfo.scrolling) {
        i = 1;
        itemInfo.scrolling = false;
      } 
    } 
    if (i != 0) {
      if (paramBoolean) {
        ViewCompat.postOnAnimation((View)this, this.mEndScrollRunnable);
        return;
      } 
    } else {
      return;
    } 
    this.mEndScrollRunnable.run();
  }
  
  private int determineTargetPage(int paramInt1, float paramFloat, int paramInt2, int paramInt3) {
    if (Math.abs(paramInt3) > this.mFlingDistance && Math.abs(paramInt2) > this.mMinimumVelocity) {
      if (paramInt2 <= 0)
        paramInt1++; 
    } else {
      float f;
      if (paramInt1 >= this.mCurItem) {
        f = 0.4F;
      } else {
        f = 0.6F;
      } 
      paramInt1 += (int)(paramFloat + f);
    } 
    paramInt2 = paramInt1;
    if (this.mItems.size() > 0) {
      ItemInfo itemInfo1 = this.mItems.get(0);
      ItemInfo itemInfo2 = this.mItems.get(this.mItems.size() - 1);
      paramInt2 = Math.max(itemInfo1.position, Math.min(paramInt1, itemInfo2.position));
    } 
    return paramInt2;
  }
  
  private void dispatchOnPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
    if (this.mOnPageChangeListener != null)
      this.mOnPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2); 
    if (this.mOnPageChangeListeners != null) {
      byte b = 0;
      int i = this.mOnPageChangeListeners.size();
      while (b < i) {
        OnPageChangeListener onPageChangeListener = this.mOnPageChangeListeners.get(b);
        if (onPageChangeListener != null)
          onPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2); 
        b++;
      } 
    } 
    if (this.mInternalPageChangeListener != null)
      this.mInternalPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2); 
  }
  
  private void dispatchOnPageSelected(int paramInt) {
    if (this.mOnPageChangeListener != null)
      this.mOnPageChangeListener.onPageSelected(paramInt); 
    if (this.mOnPageChangeListeners != null) {
      byte b = 0;
      int i = this.mOnPageChangeListeners.size();
      while (b < i) {
        OnPageChangeListener onPageChangeListener = this.mOnPageChangeListeners.get(b);
        if (onPageChangeListener != null)
          onPageChangeListener.onPageSelected(paramInt); 
        b++;
      } 
    } 
    if (this.mInternalPageChangeListener != null)
      this.mInternalPageChangeListener.onPageSelected(paramInt); 
  }
  
  private void dispatchOnScrollStateChanged(int paramInt) {
    if (this.mOnPageChangeListener != null)
      this.mOnPageChangeListener.onPageScrollStateChanged(paramInt); 
    if (this.mOnPageChangeListeners != null) {
      byte b = 0;
      int i = this.mOnPageChangeListeners.size();
      while (b < i) {
        OnPageChangeListener onPageChangeListener = this.mOnPageChangeListeners.get(b);
        if (onPageChangeListener != null)
          onPageChangeListener.onPageScrollStateChanged(paramInt); 
        b++;
      } 
    } 
    if (this.mInternalPageChangeListener != null)
      this.mInternalPageChangeListener.onPageScrollStateChanged(paramInt); 
  }
  
  private void enableLayers(boolean paramBoolean) {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      boolean bool;
      if (paramBoolean) {
        bool = this.mPageTransformerLayerType;
      } else {
        bool = false;
      } 
      ViewCompat.setLayerType(getChildAt(b), bool, null);
    } 
  }
  
  private void endDrag() {
    this.mIsBeingDragged = false;
    this.mIsUnableToDrag = false;
    if (this.mVelocityTracker != null) {
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
    } 
  }
  
  private Rect getChildRectInPagerCoordinates(Rect paramRect, View paramView) {
    Rect rect = paramRect;
    if (paramRect == null)
      rect = new Rect(); 
    if (paramView == null) {
      rect.set(0, 0, 0, 0);
      return rect;
    } 
    rect.left = paramView.getLeft();
    rect.right = paramView.getRight();
    rect.top = paramView.getTop();
    rect.bottom = paramView.getBottom();
    ViewParent viewParent = paramView.getParent();
    while (true) {
      if (viewParent instanceof ViewGroup && viewParent != this) {
        ViewGroup viewGroup = (ViewGroup)viewParent;
        rect.left += viewGroup.getLeft();
        rect.right += viewGroup.getRight();
        rect.top += viewGroup.getTop();
        rect.bottom += viewGroup.getBottom();
        ViewParent viewParent1 = viewGroup.getParent();
        continue;
      } 
      return rect;
    } 
  }
  
  private int getClientWidth() {
    return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
  }
  
  private ItemInfo infoForCurrentScrollPosition() {
    // Byte code:
    //   0: fconst_0
    //   1: fstore_1
    //   2: aload_0
    //   3: invokespecial getClientWidth : ()I
    //   6: istore_2
    //   7: iload_2
    //   8: ifle -> 221
    //   11: aload_0
    //   12: invokevirtual getScrollX : ()I
    //   15: i2f
    //   16: iload_2
    //   17: i2f
    //   18: fdiv
    //   19: fstore_3
    //   20: iload_2
    //   21: ifle -> 33
    //   24: aload_0
    //   25: getfield mPageMargin : I
    //   28: i2f
    //   29: iload_2
    //   30: i2f
    //   31: fdiv
    //   32: fstore_1
    //   33: iconst_m1
    //   34: istore #4
    //   36: fconst_0
    //   37: fstore #5
    //   39: fconst_0
    //   40: fstore #6
    //   42: iconst_1
    //   43: istore #7
    //   45: aconst_null
    //   46: astore #8
    //   48: iconst_0
    //   49: istore_2
    //   50: aload #8
    //   52: astore #9
    //   54: iload_2
    //   55: aload_0
    //   56: getfield mItems : Ljava/util/ArrayList;
    //   59: invokevirtual size : ()I
    //   62: if_icmpge -> 218
    //   65: aload_0
    //   66: getfield mItems : Ljava/util/ArrayList;
    //   69: iload_2
    //   70: invokevirtual get : (I)Ljava/lang/Object;
    //   73: checkcast android/support/v4/view/ViewPager$ItemInfo
    //   76: astore #9
    //   78: iload_2
    //   79: istore #10
    //   81: aload #9
    //   83: astore #11
    //   85: iload #7
    //   87: ifne -> 158
    //   90: iload_2
    //   91: istore #10
    //   93: aload #9
    //   95: astore #11
    //   97: aload #9
    //   99: getfield position : I
    //   102: iload #4
    //   104: iconst_1
    //   105: iadd
    //   106: if_icmpeq -> 158
    //   109: aload_0
    //   110: getfield mTempItem : Landroid/support/v4/view/ViewPager$ItemInfo;
    //   113: astore #11
    //   115: aload #11
    //   117: fload #5
    //   119: fload #6
    //   121: fadd
    //   122: fload_1
    //   123: fadd
    //   124: putfield offset : F
    //   127: aload #11
    //   129: iload #4
    //   131: iconst_1
    //   132: iadd
    //   133: putfield position : I
    //   136: aload #11
    //   138: aload_0
    //   139: getfield mAdapter : Landroid/support/v4/view/PagerAdapter;
    //   142: aload #11
    //   144: getfield position : I
    //   147: invokevirtual getPageWidth : (I)F
    //   150: putfield widthFactor : F
    //   153: iload_2
    //   154: iconst_1
    //   155: isub
    //   156: istore #10
    //   158: aload #11
    //   160: getfield offset : F
    //   163: fstore #5
    //   165: aload #11
    //   167: getfield widthFactor : F
    //   170: fstore #6
    //   172: iload #7
    //   174: ifne -> 188
    //   177: aload #8
    //   179: astore #9
    //   181: fload_3
    //   182: fload #5
    //   184: fcmpl
    //   185: iflt -> 218
    //   188: fload_3
    //   189: fload #6
    //   191: fload #5
    //   193: fadd
    //   194: fload_1
    //   195: fadd
    //   196: fcmpg
    //   197: iflt -> 214
    //   200: iload #10
    //   202: aload_0
    //   203: getfield mItems : Ljava/util/ArrayList;
    //   206: invokevirtual size : ()I
    //   209: iconst_1
    //   210: isub
    //   211: if_icmpne -> 226
    //   214: aload #11
    //   216: astore #9
    //   218: aload #9
    //   220: areturn
    //   221: fconst_0
    //   222: fstore_3
    //   223: goto -> 20
    //   226: iconst_0
    //   227: istore #7
    //   229: aload #11
    //   231: getfield position : I
    //   234: istore #4
    //   236: aload #11
    //   238: getfield widthFactor : F
    //   241: fstore #6
    //   243: iload #10
    //   245: iconst_1
    //   246: iadd
    //   247: istore_2
    //   248: aload #11
    //   250: astore #8
    //   252: goto -> 50
  }
  
  private static boolean isDecorView(@NonNull View paramView) {
    return (paramView.getClass().getAnnotation(DecorView.class) != null);
  }
  
  private boolean isGutterDrag(float paramFloat1, float paramFloat2) {
    return ((paramFloat1 < this.mGutterSize && paramFloat2 > 0.0F) || (paramFloat1 > (getWidth() - this.mGutterSize) && paramFloat2 < 0.0F));
  }
  
  private void onSecondaryPointerUp(MotionEvent paramMotionEvent) {
    int i = MotionEventCompat.getActionIndex(paramMotionEvent);
    if (paramMotionEvent.getPointerId(i) == this.mActivePointerId) {
      if (i == 0) {
        i = 1;
      } else {
        i = 0;
      } 
      this.mLastMotionX = paramMotionEvent.getX(i);
      this.mActivePointerId = paramMotionEvent.getPointerId(i);
      if (this.mVelocityTracker != null)
        this.mVelocityTracker.clear(); 
    } 
  }
  
  private boolean pageScrolled(int paramInt) {
    null = false;
    if (this.mItems.size() == 0) {
      if (!this.mFirstLayout) {
        this.mCalledSuper = false;
        onPageScrolled(0, 0.0F, 0);
        if (!this.mCalledSuper)
          throw new IllegalStateException("onPageScrolled did not call superclass implementation"); 
      } 
      return null;
    } 
    ItemInfo itemInfo = infoForCurrentScrollPosition();
    int i = getClientWidth();
    int j = this.mPageMargin;
    float f = this.mPageMargin / i;
    int k = itemInfo.position;
    f = (paramInt / i - itemInfo.offset) / (itemInfo.widthFactor + f);
    paramInt = (int)((i + j) * f);
    this.mCalledSuper = false;
    onPageScrolled(k, f, paramInt);
    if (!this.mCalledSuper)
      throw new IllegalStateException("onPageScrolled did not call superclass implementation"); 
    return true;
  }
  
  private boolean performDrag(float paramFloat) {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool = false;
    float f1 = this.mLastMotionX;
    this.mLastMotionX = paramFloat;
    float f2 = getScrollX() + f1 - paramFloat;
    int i = getClientWidth();
    paramFloat = i * this.mFirstOffset;
    f1 = i * this.mLastOffset;
    boolean bool3 = true;
    boolean bool4 = true;
    ItemInfo itemInfo1 = this.mItems.get(0);
    ItemInfo itemInfo2 = this.mItems.get(this.mItems.size() - 1);
    if (itemInfo1.position != 0) {
      bool3 = false;
      paramFloat = itemInfo1.offset * i;
    } 
    if (itemInfo2.position != this.mAdapter.getCount() - 1) {
      bool4 = false;
      f1 = itemInfo2.offset * i;
    } 
    if (f2 < paramFloat) {
      if (bool3)
        bool = this.mLeftEdge.onPull(Math.abs(paramFloat - f2) / i); 
      this.mLastMotionX += paramFloat - (int)paramFloat;
      scrollTo((int)paramFloat, getScrollY());
      pageScrolled((int)paramFloat);
      return bool;
    } 
    bool = bool1;
    paramFloat = f2;
    if (f2 > f1) {
      bool = bool2;
      if (bool4)
        bool = this.mRightEdge.onPull(Math.abs(f2 - f1) / i); 
      paramFloat = f1;
    } 
    this.mLastMotionX += paramFloat - (int)paramFloat;
    scrollTo((int)paramFloat, getScrollY());
    pageScrolled((int)paramFloat);
    return bool;
  }
  
  private void recomputeScrollPosition(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    float f;
    if (paramInt2 > 0 && !this.mItems.isEmpty()) {
      if (!this.mScroller.isFinished()) {
        this.mScroller.setFinalX(getCurrentItem() * getClientWidth());
        return;
      } 
      int i = getPaddingLeft();
      int j = getPaddingRight();
      int k = getPaddingLeft();
      int m = getPaddingRight();
      f = getScrollX() / (paramInt2 - k - m + paramInt4);
      scrollTo((int)((paramInt1 - i - j + paramInt3) * f), getScrollY());
      return;
    } 
    ItemInfo itemInfo = infoForPosition(this.mCurItem);
    if (itemInfo != null) {
      f = Math.min(itemInfo.offset, this.mLastOffset);
    } else {
      f = 0.0F;
    } 
    paramInt1 = (int)((paramInt1 - getPaddingLeft() - getPaddingRight()) * f);
    if (paramInt1 != getScrollX()) {
      completeScroll(false);
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
  
  private void requestParentDisallowInterceptTouchEvent(boolean paramBoolean) {
    ViewParent viewParent = getParent();
    if (viewParent != null)
      viewParent.requestDisallowInterceptTouchEvent(paramBoolean); 
  }
  
  private boolean resetTouch() {
    this.mActivePointerId = -1;
    endDrag();
    return this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
  }
  
  private void scrollToItem(int paramInt1, boolean paramBoolean1, int paramInt2, boolean paramBoolean2) {
    ItemInfo itemInfo = infoForPosition(paramInt1);
    int i = 0;
    if (itemInfo != null)
      i = (int)(getClientWidth() * Math.max(this.mFirstOffset, Math.min(itemInfo.offset, this.mLastOffset))); 
    if (paramBoolean1) {
      smoothScrollTo(i, 0, paramInt2);
      if (paramBoolean2)
        dispatchOnPageSelected(paramInt1); 
      return;
    } 
    if (paramBoolean2)
      dispatchOnPageSelected(paramInt1); 
    completeScroll(false);
    scrollTo(i, 0);
    pageScrolled(i);
  }
  
  private void setScrollingCacheEnabled(boolean paramBoolean) {
    if (this.mScrollingCacheEnabled != paramBoolean)
      this.mScrollingCacheEnabled = paramBoolean; 
  }
  
  private void sortChildDrawingOrder() {
    if (this.mDrawingOrder != 0) {
      if (this.mDrawingOrderedChildren == null) {
        this.mDrawingOrderedChildren = new ArrayList<View>();
      } else {
        this.mDrawingOrderedChildren.clear();
      } 
      int i = getChildCount();
      for (byte b = 0; b < i; b++) {
        View view = getChildAt(b);
        this.mDrawingOrderedChildren.add(view);
      } 
      Collections.sort(this.mDrawingOrderedChildren, sPositionComparator);
    } 
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
  
  ItemInfo addNewItem(int paramInt1, int paramInt2) {
    ItemInfo itemInfo = new ItemInfo();
    itemInfo.position = paramInt1;
    itemInfo.object = this.mAdapter.instantiateItem(this, paramInt1);
    itemInfo.widthFactor = this.mAdapter.getPageWidth(paramInt1);
    if (paramInt2 < 0 || paramInt2 >= this.mItems.size()) {
      this.mItems.add(itemInfo);
      return itemInfo;
    } 
    this.mItems.add(paramInt2, itemInfo);
    return itemInfo;
  }
  
  public void addOnAdapterChangeListener(@NonNull OnAdapterChangeListener paramOnAdapterChangeListener) {
    if (this.mAdapterChangeListeners == null)
      this.mAdapterChangeListeners = new ArrayList<OnAdapterChangeListener>(); 
    this.mAdapterChangeListeners.add(paramOnAdapterChangeListener);
  }
  
  public void addOnPageChangeListener(OnPageChangeListener paramOnPageChangeListener) {
    if (this.mOnPageChangeListeners == null)
      this.mOnPageChangeListeners = new ArrayList<OnPageChangeListener>(); 
    this.mOnPageChangeListeners.add(paramOnPageChangeListener);
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
    ((LayoutParams)paramLayoutParams).isDecor |= isDecorView(paramView);
    if (this.mInLayout) {
      if (paramLayoutParams != null && ((LayoutParams)paramLayoutParams).isDecor)
        throw new IllegalStateException("Cannot add pager decor view during layout"); 
      ((LayoutParams)paramLayoutParams).needsMeasure = true;
      addViewInLayout(paramView, paramInt, layoutParams);
      return;
    } 
    super.addView(paramView, paramInt, layoutParams);
  }
  
  public boolean arrowScroll(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual findFocus : ()Landroid/view/View;
    //   4: astore_2
    //   5: aload_2
    //   6: aload_0
    //   7: if_acmpne -> 101
    //   10: aconst_null
    //   11: astore_3
    //   12: iconst_0
    //   13: istore #4
    //   15: invokestatic getInstance : ()Landroid/view/FocusFinder;
    //   18: aload_0
    //   19: aload_3
    //   20: iload_1
    //   21: invokevirtual findNextFocus : (Landroid/view/ViewGroup;Landroid/view/View;I)Landroid/view/View;
    //   24: astore_2
    //   25: aload_2
    //   26: ifnull -> 321
    //   29: aload_2
    //   30: aload_3
    //   31: if_acmpeq -> 321
    //   34: iload_1
    //   35: bipush #17
    //   37: if_icmpne -> 258
    //   40: aload_0
    //   41: aload_0
    //   42: getfield mTempRect : Landroid/graphics/Rect;
    //   45: aload_2
    //   46: invokespecial getChildRectInPagerCoordinates : (Landroid/graphics/Rect;Landroid/view/View;)Landroid/graphics/Rect;
    //   49: getfield left : I
    //   52: istore #5
    //   54: aload_0
    //   55: aload_0
    //   56: getfield mTempRect : Landroid/graphics/Rect;
    //   59: aload_3
    //   60: invokespecial getChildRectInPagerCoordinates : (Landroid/graphics/Rect;Landroid/view/View;)Landroid/graphics/Rect;
    //   63: getfield left : I
    //   66: istore #6
    //   68: aload_3
    //   69: ifnull -> 249
    //   72: iload #5
    //   74: iload #6
    //   76: if_icmplt -> 249
    //   79: aload_0
    //   80: invokevirtual pageLeft : ()Z
    //   83: istore #4
    //   85: iload #4
    //   87: ifeq -> 98
    //   90: aload_0
    //   91: iload_1
    //   92: invokestatic getContantForFocusDirection : (I)I
    //   95: invokevirtual playSoundEffect : (I)V
    //   98: iload #4
    //   100: ireturn
    //   101: aload_2
    //   102: astore_3
    //   103: aload_2
    //   104: ifnull -> 12
    //   107: iconst_0
    //   108: istore #5
    //   110: aload_2
    //   111: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   114: astore_3
    //   115: iload #5
    //   117: istore #6
    //   119: aload_3
    //   120: instanceof android/view/ViewGroup
    //   123: ifeq -> 134
    //   126: aload_3
    //   127: aload_0
    //   128: if_acmpne -> 204
    //   131: iconst_1
    //   132: istore #6
    //   134: aload_2
    //   135: astore_3
    //   136: iload #6
    //   138: ifne -> 12
    //   141: new java/lang/StringBuilder
    //   144: dup
    //   145: invokespecial <init> : ()V
    //   148: astore #7
    //   150: aload #7
    //   152: aload_2
    //   153: invokevirtual getClass : ()Ljava/lang/Class;
    //   156: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   159: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   162: pop
    //   163: aload_2
    //   164: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   167: astore_3
    //   168: aload_3
    //   169: instanceof android/view/ViewGroup
    //   172: ifeq -> 214
    //   175: aload #7
    //   177: ldc_w ' => '
    //   180: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   183: aload_3
    //   184: invokevirtual getClass : ()Ljava/lang/Class;
    //   187: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   190: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   193: pop
    //   194: aload_3
    //   195: invokeinterface getParent : ()Landroid/view/ViewParent;
    //   200: astore_3
    //   201: goto -> 168
    //   204: aload_3
    //   205: invokeinterface getParent : ()Landroid/view/ViewParent;
    //   210: astore_3
    //   211: goto -> 115
    //   214: ldc 'ViewPager'
    //   216: new java/lang/StringBuilder
    //   219: dup
    //   220: invokespecial <init> : ()V
    //   223: ldc_w 'arrowScroll tried to find focus based on non-child current focused view '
    //   226: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   229: aload #7
    //   231: invokevirtual toString : ()Ljava/lang/String;
    //   234: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   237: invokevirtual toString : ()Ljava/lang/String;
    //   240: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   243: pop
    //   244: aconst_null
    //   245: astore_3
    //   246: goto -> 12
    //   249: aload_2
    //   250: invokevirtual requestFocus : ()Z
    //   253: istore #4
    //   255: goto -> 85
    //   258: iload_1
    //   259: bipush #66
    //   261: if_icmpne -> 85
    //   264: aload_0
    //   265: aload_0
    //   266: getfield mTempRect : Landroid/graphics/Rect;
    //   269: aload_2
    //   270: invokespecial getChildRectInPagerCoordinates : (Landroid/graphics/Rect;Landroid/view/View;)Landroid/graphics/Rect;
    //   273: getfield left : I
    //   276: istore #6
    //   278: aload_0
    //   279: aload_0
    //   280: getfield mTempRect : Landroid/graphics/Rect;
    //   283: aload_3
    //   284: invokespecial getChildRectInPagerCoordinates : (Landroid/graphics/Rect;Landroid/view/View;)Landroid/graphics/Rect;
    //   287: getfield left : I
    //   290: istore #5
    //   292: aload_3
    //   293: ifnull -> 312
    //   296: iload #6
    //   298: iload #5
    //   300: if_icmpgt -> 312
    //   303: aload_0
    //   304: invokevirtual pageRight : ()Z
    //   307: istore #4
    //   309: goto -> 85
    //   312: aload_2
    //   313: invokevirtual requestFocus : ()Z
    //   316: istore #4
    //   318: goto -> 85
    //   321: iload_1
    //   322: bipush #17
    //   324: if_icmpeq -> 332
    //   327: iload_1
    //   328: iconst_1
    //   329: if_icmpne -> 341
    //   332: aload_0
    //   333: invokevirtual pageLeft : ()Z
    //   336: istore #4
    //   338: goto -> 85
    //   341: iload_1
    //   342: bipush #66
    //   344: if_icmpeq -> 352
    //   347: iload_1
    //   348: iconst_2
    //   349: if_icmpne -> 85
    //   352: aload_0
    //   353: invokevirtual pageRight : ()Z
    //   356: istore #4
    //   358: goto -> 85
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
  
  public boolean canScrollHorizontally(int paramInt) {
    boolean bool1 = true;
    boolean bool2 = true;
    boolean bool3 = false;
    if (this.mAdapter == null)
      return bool3; 
    int i = getClientWidth();
    int j = getScrollX();
    if (paramInt < 0) {
      if (j <= (int)(i * this.mFirstOffset))
        bool2 = false; 
      return bool2;
    } 
    bool2 = bool3;
    if (paramInt > 0) {
      if (j < (int)(i * this.mLastOffset))
        return bool1; 
      bool2 = false;
    } 
    return bool2;
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (paramLayoutParams instanceof LayoutParams && super.checkLayoutParams(paramLayoutParams));
  }
  
  public void clearOnPageChangeListeners() {
    if (this.mOnPageChangeListeners != null)
      this.mOnPageChangeListeners.clear(); 
  }
  
  public void computeScroll() {
    this.mIsScrollStarted = true;
    if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
      int i = getScrollX();
      int j = getScrollY();
      int k = this.mScroller.getCurrX();
      int m = this.mScroller.getCurrY();
      if (i != k || j != m) {
        scrollTo(k, m);
        if (!pageScrolled(k)) {
          this.mScroller.abortAnimation();
          scrollTo(0, m);
        } 
      } 
      ViewCompat.postInvalidateOnAnimation((View)this);
      return;
    } 
    completeScroll(true);
  }
  
  void dataSetChanged() {
    byte b;
    int i = this.mAdapter.getCount();
    this.mExpectedAdapterCount = i;
    if (this.mItems.size() < this.mOffscreenPageLimit * 2 + 1 && this.mItems.size() < i) {
      b = 1;
    } else {
      b = 0;
    } 
    int j = this.mCurItem;
    int k = 0;
    int m = 0;
    while (m < this.mItems.size()) {
      int i1;
      int i2;
      int i3;
      ItemInfo itemInfo = this.mItems.get(m);
      int n = this.mAdapter.getItemPosition(itemInfo.object);
      if (n == -1) {
        i1 = j;
        i2 = k;
        i3 = m;
      } else if (n == -2) {
        this.mItems.remove(m);
        n = m - 1;
        m = k;
        if (!k) {
          this.mAdapter.startUpdate(this);
          m = 1;
        } 
        this.mAdapter.destroyItem(this, itemInfo.position, itemInfo.object);
        b = 1;
        i3 = n;
        i2 = m;
        i1 = j;
        if (this.mCurItem == itemInfo.position) {
          i1 = Math.max(0, Math.min(this.mCurItem, i - 1));
          b = 1;
          i3 = n;
          i2 = m;
        } 
      } else {
        i3 = m;
        i2 = k;
        i1 = j;
        if (itemInfo.position != n) {
          if (itemInfo.position == this.mCurItem)
            j = n; 
          itemInfo.position = n;
          b = 1;
          i3 = m;
          i2 = k;
          i1 = j;
        } 
      } 
      m = i3 + 1;
      k = i2;
      j = i1;
    } 
    if (k != 0)
      this.mAdapter.finishUpdate(this); 
    Collections.sort(this.mItems, COMPARATOR);
    if (b) {
      k = getChildCount();
      for (b = 0; b < k; b++) {
        LayoutParams layoutParams = (LayoutParams)getChildAt(b).getLayoutParams();
        if (!layoutParams.isDecor)
          layoutParams.widthFactor = 0.0F; 
      } 
      setCurrentItemInternal(j, false, true);
      requestLayout();
    } 
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
    return (super.dispatchKeyEvent(paramKeyEvent) || executeKeyEvent(paramKeyEvent));
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getEventType : ()I
    //   4: sipush #4096
    //   7: if_icmpne -> 18
    //   10: aload_0
    //   11: aload_1
    //   12: invokespecial dispatchPopulateAccessibilityEvent : (Landroid/view/accessibility/AccessibilityEvent;)Z
    //   15: istore_2
    //   16: iload_2
    //   17: ireturn
    //   18: aload_0
    //   19: invokevirtual getChildCount : ()I
    //   22: istore_3
    //   23: iconst_0
    //   24: istore #4
    //   26: iload #4
    //   28: iload_3
    //   29: if_icmpge -> 93
    //   32: aload_0
    //   33: iload #4
    //   35: invokevirtual getChildAt : (I)Landroid/view/View;
    //   38: astore #5
    //   40: aload #5
    //   42: invokevirtual getVisibility : ()I
    //   45: ifne -> 87
    //   48: aload_0
    //   49: aload #5
    //   51: invokevirtual infoForChild : (Landroid/view/View;)Landroid/support/v4/view/ViewPager$ItemInfo;
    //   54: astore #6
    //   56: aload #6
    //   58: ifnull -> 87
    //   61: aload #6
    //   63: getfield position : I
    //   66: aload_0
    //   67: getfield mCurItem : I
    //   70: if_icmpne -> 87
    //   73: aload #5
    //   75: aload_1
    //   76: invokevirtual dispatchPopulateAccessibilityEvent : (Landroid/view/accessibility/AccessibilityEvent;)Z
    //   79: ifeq -> 87
    //   82: iconst_1
    //   83: istore_2
    //   84: goto -> 16
    //   87: iinc #4, 1
    //   90: goto -> 26
    //   93: iconst_0
    //   94: istore_2
    //   95: goto -> 16
  }
  
  float distanceInfluenceForSnapDuration(float paramFloat) {
    return (float)Math.sin((float)((paramFloat - 0.5F) * 0.4712389167638204D));
  }
  
  public void draw(Canvas paramCanvas) {
    boolean bool;
    super.draw(paramCanvas);
    int i = 0;
    int j = 0;
    int k = getOverScrollMode();
    if (k == 0 || (k == 1 && this.mAdapter != null && this.mAdapter.getCount() > 1)) {
      if (!this.mLeftEdge.isFinished()) {
        i = paramCanvas.save();
        j = getHeight() - getPaddingTop() - getPaddingBottom();
        k = getWidth();
        paramCanvas.rotate(270.0F);
        paramCanvas.translate((-j + getPaddingTop()), this.mFirstOffset * k);
        this.mLeftEdge.setSize(j, k);
        j = false | this.mLeftEdge.draw(paramCanvas);
        paramCanvas.restoreToCount(i);
      } 
      i = j;
      if (!this.mRightEdge.isFinished()) {
        k = paramCanvas.save();
        i = getWidth();
        int m = getHeight();
        int n = getPaddingTop();
        int i1 = getPaddingBottom();
        paramCanvas.rotate(90.0F);
        paramCanvas.translate(-getPaddingTop(), -(this.mLastOffset + 1.0F) * i);
        this.mRightEdge.setSize(m - n - i1, i);
        bool = j | this.mRightEdge.draw(paramCanvas);
        paramCanvas.restoreToCount(k);
      } 
    } else {
      this.mLeftEdge.finish();
      this.mRightEdge.finish();
    } 
    if (bool)
      ViewCompat.postInvalidateOnAnimation((View)this); 
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
    if (this.mAdapter != null) {
      VelocityTracker velocityTracker = this.mVelocityTracker;
      velocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
      int i = (int)VelocityTrackerCompat.getXVelocity(velocityTracker, this.mActivePointerId);
      this.mPopulatePending = true;
      int j = getClientWidth();
      int k = getScrollX();
      ItemInfo itemInfo = infoForCurrentScrollPosition();
      setCurrentItemInternal(determineTargetPage(itemInfo.position, (k / j - itemInfo.offset) / itemInfo.widthFactor, i, (int)(this.mLastMotionX - this.mInitialMotionX)), true, true, i);
    } 
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
    if (this.mAdapter != null) {
      this.mLastMotionX += paramFloat;
      float f1 = getScrollX() - paramFloat;
      int i = getClientWidth();
      paramFloat = i * this.mFirstOffset;
      float f2 = i * this.mLastOffset;
      ItemInfo itemInfo1 = this.mItems.get(0);
      ItemInfo itemInfo2 = this.mItems.get(this.mItems.size() - 1);
      if (itemInfo1.position != 0)
        paramFloat = itemInfo1.offset * i; 
      if (itemInfo2.position != this.mAdapter.getCount() - 1)
        f2 = itemInfo2.offset * i; 
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
  
  protected int getChildDrawingOrder(int paramInt1, int paramInt2) {
    if (this.mDrawingOrder == 2)
      paramInt2 = paramInt1 - 1 - paramInt2; 
    return ((LayoutParams)((View)this.mDrawingOrderedChildren.get(paramInt2)).getLayoutParams()).childIndex;
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
    //   21: checkcast android/support/v4/view/ViewPager$ItemInfo
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
  
  ItemInfo infoForPosition(int paramInt) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: iload_2
    //   3: aload_0
    //   4: getfield mItems : Ljava/util/ArrayList;
    //   7: invokevirtual size : ()I
    //   10: if_icmpge -> 41
    //   13: aload_0
    //   14: getfield mItems : Ljava/util/ArrayList;
    //   17: iload_2
    //   18: invokevirtual get : (I)Ljava/lang/Object;
    //   21: checkcast android/support/v4/view/ViewPager$ItemInfo
    //   24: astore_3
    //   25: aload_3
    //   26: getfield position : I
    //   29: iload_1
    //   30: if_icmpne -> 35
    //   33: aload_3
    //   34: areturn
    //   35: iinc #2, 1
    //   38: goto -> 2
    //   41: aconst_null
    //   42: astore_3
    //   43: goto -> 33
  }
  
  void initViewPager() {
    setWillNotDraw(false);
    setDescendantFocusability(262144);
    setFocusable(true);
    Context context = getContext();
    this.mScroller = new Scroller(context, sInterpolator);
    ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
    float f = (context.getResources().getDisplayMetrics()).density;
    this.mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
    this.mMinimumVelocity = (int)(400.0F * f);
    this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
    this.mLeftEdge = new EdgeEffectCompat(context);
    this.mRightEdge = new EdgeEffectCompat(context);
    this.mFlingDistance = (int)(25.0F * f);
    this.mCloseEnough = (int)(2.0F * f);
    this.mDefaultGutterSize = (int)(16.0F * f);
    ViewCompat.setAccessibilityDelegate((View)this, new MyAccessibilityDelegate());
    if (ViewCompat.getImportantForAccessibility((View)this) == 0)
      ViewCompat.setImportantForAccessibility((View)this, 1); 
    ViewCompat.setOnApplyWindowInsetsListener((View)this, new OnApplyWindowInsetsListener() {
          private final Rect mTempRect = new Rect();
          
          public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat) {
            WindowInsetsCompat windowInsetsCompat = ViewCompat.onApplyWindowInsets(param1View, param1WindowInsetsCompat);
            if (!windowInsetsCompat.isConsumed()) {
              Rect rect = this.mTempRect;
              rect.left = windowInsetsCompat.getSystemWindowInsetLeft();
              rect.top = windowInsetsCompat.getSystemWindowInsetTop();
              rect.right = windowInsetsCompat.getSystemWindowInsetRight();
              rect.bottom = windowInsetsCompat.getSystemWindowInsetBottom();
              byte b = 0;
              int i = ViewPager.this.getChildCount();
              while (b < i) {
                param1WindowInsetsCompat = ViewCompat.dispatchApplyWindowInsets(ViewPager.this.getChildAt(b), windowInsetsCompat);
                rect.left = Math.min(param1WindowInsetsCompat.getSystemWindowInsetLeft(), rect.left);
                rect.top = Math.min(param1WindowInsetsCompat.getSystemWindowInsetTop(), rect.top);
                rect.right = Math.min(param1WindowInsetsCompat.getSystemWindowInsetRight(), rect.right);
                rect.bottom = Math.min(param1WindowInsetsCompat.getSystemWindowInsetBottom(), rect.bottom);
                b++;
              } 
              windowInsetsCompat = windowInsetsCompat.replaceSystemWindowInsets(rect.left, rect.top, rect.right, rect.bottom);
            } 
            return windowInsetsCompat;
          }
        });
  }
  
  public boolean isFakeDragging() {
    return this.mFakeDragging;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    this.mFirstLayout = true;
  }
  
  protected void onDetachedFromWindow() {
    removeCallbacks(this.mEndScrollRunnable);
    if (this.mScroller != null && !this.mScroller.isFinished())
      this.mScroller.abortAnimation(); 
    super.onDetachedFromWindow();
  }
  
  protected void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
      int i = getScrollX();
      int j = getWidth();
      float f1 = this.mPageMargin / j;
      byte b = 0;
      ItemInfo itemInfo = this.mItems.get(0);
      float f2 = itemInfo.offset;
      int k = this.mItems.size();
      int m = itemInfo.position;
      int n = ((ItemInfo)this.mItems.get(k - 1)).position;
      while (true) {
        if (m < n) {
          ItemInfo itemInfo1;
          float f;
          while (m > itemInfo.position && b < k) {
            ArrayList<ItemInfo> arrayList = this.mItems;
            itemInfo1 = arrayList.get(++b);
          } 
          if (m == itemInfo1.position) {
            f = (itemInfo1.offset + itemInfo1.widthFactor) * j;
            f2 = itemInfo1.offset + itemInfo1.widthFactor + f1;
          } else {
            float f3 = this.mAdapter.getPageWidth(m);
            f = (f2 + f3) * j;
            f2 += f3 + f1;
          } 
          if (this.mPageMargin + f > i) {
            this.mMarginDrawable.setBounds(Math.round(f), this.mTopPageBounds, Math.round(this.mPageMargin + f), this.mBottomPageBounds);
            this.mMarginDrawable.draw(paramCanvas);
          } 
          if (f <= (i + j)) {
            m++;
            continue;
          } 
        } 
        return;
      } 
    } 
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    float f;
    int i = paramMotionEvent.getAction() & 0xFF;
    if (i == 3 || i == 1) {
      resetTouch();
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
        if (this.mVelocityTracker == null)
          this.mVelocityTracker = VelocityTracker.obtain(); 
        this.mVelocityTracker.addMovement(paramMotionEvent);
        return this.mIsBeingDragged;
      case 2:
        i = this.mActivePointerId;
        if (i != -1) {
          i = paramMotionEvent.findPointerIndex(i);
          float f1 = paramMotionEvent.getX(i);
          float f2 = f1 - this.mLastMotionX;
          float f3 = Math.abs(f2);
          float f4 = paramMotionEvent.getY(i);
          float f5 = Math.abs(f4 - this.mInitialMotionY);
          if (f2 != 0.0F && !isGutterDrag(this.mLastMotionX, f2) && canScroll((View)this, false, (int)f2, (int)f1, (int)f4)) {
            this.mLastMotionX = f1;
            this.mLastMotionY = f4;
            this.mIsUnableToDrag = true;
            return false;
          } 
          if (f3 > this.mTouchSlop && 0.5F * f3 > f5) {
            this.mIsBeingDragged = true;
            requestParentDisallowInterceptTouchEvent(true);
            setScrollState(1);
            if (f2 > 0.0F) {
              f2 = this.mInitialMotionX + this.mTouchSlop;
            } else {
              f2 = this.mInitialMotionX - this.mTouchSlop;
            } 
            this.mLastMotionX = f2;
            this.mLastMotionY = f4;
            setScrollingCacheEnabled(true);
          } else if (f5 > this.mTouchSlop) {
            this.mIsUnableToDrag = true;
          } 
          if (this.mIsBeingDragged && performDrag(f1))
            ViewCompat.postInvalidateOnAnimation((View)this); 
        } 
      case 0:
        f = paramMotionEvent.getX();
        this.mInitialMotionX = f;
        this.mLastMotionX = f;
        f = paramMotionEvent.getY();
        this.mInitialMotionY = f;
        this.mLastMotionY = f;
        this.mActivePointerId = paramMotionEvent.getPointerId(0);
        this.mIsUnableToDrag = false;
        this.mIsScrollStarted = true;
        this.mScroller.computeScrollOffset();
        if (this.mScrollState == 2 && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) {
          this.mScroller.abortAnimation();
          this.mPopulatePending = false;
          populate();
          this.mIsBeingDragged = true;
          requestParentDisallowInterceptTouchEvent(true);
          setScrollState(1);
        } else {
          completeScroll(false);
          this.mIsBeingDragged = false;
        } 
      case 6:
        break;
    } 
    onSecondaryPointerUp(paramMotionEvent);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getChildCount : ()I
    //   4: istore #6
    //   6: iload #4
    //   8: iload_2
    //   9: isub
    //   10: istore #7
    //   12: iload #5
    //   14: iload_3
    //   15: isub
    //   16: istore #8
    //   18: aload_0
    //   19: invokevirtual getPaddingLeft : ()I
    //   22: istore_3
    //   23: aload_0
    //   24: invokevirtual getPaddingTop : ()I
    //   27: istore_2
    //   28: aload_0
    //   29: invokevirtual getPaddingRight : ()I
    //   32: istore #9
    //   34: aload_0
    //   35: invokevirtual getPaddingBottom : ()I
    //   38: istore #5
    //   40: aload_0
    //   41: invokevirtual getScrollX : ()I
    //   44: istore #10
    //   46: iconst_0
    //   47: istore #11
    //   49: iconst_0
    //   50: istore #12
    //   52: iload #12
    //   54: iload #6
    //   56: if_icmpge -> 423
    //   59: aload_0
    //   60: iload #12
    //   62: invokevirtual getChildAt : (I)Landroid/view/View;
    //   65: astore #13
    //   67: iload #11
    //   69: istore #14
    //   71: iload #5
    //   73: istore #15
    //   75: iload_3
    //   76: istore #16
    //   78: iload #9
    //   80: istore #17
    //   82: iload_2
    //   83: istore #4
    //   85: aload #13
    //   87: invokevirtual getVisibility : ()I
    //   90: bipush #8
    //   92: if_icmpeq -> 277
    //   95: aload #13
    //   97: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   100: checkcast android/support/v4/view/ViewPager$LayoutParams
    //   103: astore #18
    //   105: iload #11
    //   107: istore #14
    //   109: iload #5
    //   111: istore #15
    //   113: iload_3
    //   114: istore #16
    //   116: iload #9
    //   118: istore #17
    //   120: iload_2
    //   121: istore #4
    //   123: aload #18
    //   125: getfield isDecor : Z
    //   128: ifeq -> 277
    //   131: aload #18
    //   133: getfield gravity : I
    //   136: istore #4
    //   138: aload #18
    //   140: getfield gravity : I
    //   143: istore #17
    //   145: iload #4
    //   147: bipush #7
    //   149: iand
    //   150: tableswitch default -> 184, 1 -> 316, 2 -> 184, 3 -> 301, 4 -> 184, 5 -> 338
    //   184: iload_3
    //   185: istore #4
    //   187: iload_3
    //   188: istore #16
    //   190: iload #17
    //   192: bipush #112
    //   194: iand
    //   195: lookupswitch default -> 228, 16 -> 380, 48 -> 367, 80 -> 398
    //   228: iload_2
    //   229: istore_3
    //   230: iload #4
    //   232: iload #10
    //   234: iadd
    //   235: istore #4
    //   237: aload #13
    //   239: iload #4
    //   241: iload_3
    //   242: aload #13
    //   244: invokevirtual getMeasuredWidth : ()I
    //   247: iload #4
    //   249: iadd
    //   250: aload #13
    //   252: invokevirtual getMeasuredHeight : ()I
    //   255: iload_3
    //   256: iadd
    //   257: invokevirtual layout : (IIII)V
    //   260: iload #11
    //   262: iconst_1
    //   263: iadd
    //   264: istore #14
    //   266: iload_2
    //   267: istore #4
    //   269: iload #9
    //   271: istore #17
    //   273: iload #5
    //   275: istore #15
    //   277: iinc #12, 1
    //   280: iload #14
    //   282: istore #11
    //   284: iload #15
    //   286: istore #5
    //   288: iload #16
    //   290: istore_3
    //   291: iload #17
    //   293: istore #9
    //   295: iload #4
    //   297: istore_2
    //   298: goto -> 52
    //   301: iload_3
    //   302: istore #4
    //   304: iload_3
    //   305: aload #13
    //   307: invokevirtual getMeasuredWidth : ()I
    //   310: iadd
    //   311: istore #16
    //   313: goto -> 190
    //   316: iload #7
    //   318: aload #13
    //   320: invokevirtual getMeasuredWidth : ()I
    //   323: isub
    //   324: iconst_2
    //   325: idiv
    //   326: iload_3
    //   327: invokestatic max : (II)I
    //   330: istore #4
    //   332: iload_3
    //   333: istore #16
    //   335: goto -> 190
    //   338: iload #7
    //   340: iload #9
    //   342: isub
    //   343: aload #13
    //   345: invokevirtual getMeasuredWidth : ()I
    //   348: isub
    //   349: istore #4
    //   351: iload #9
    //   353: aload #13
    //   355: invokevirtual getMeasuredWidth : ()I
    //   358: iadd
    //   359: istore #9
    //   361: iload_3
    //   362: istore #16
    //   364: goto -> 190
    //   367: iload_2
    //   368: istore_3
    //   369: iload_2
    //   370: aload #13
    //   372: invokevirtual getMeasuredHeight : ()I
    //   375: iadd
    //   376: istore_2
    //   377: goto -> 230
    //   380: iload #8
    //   382: aload #13
    //   384: invokevirtual getMeasuredHeight : ()I
    //   387: isub
    //   388: iconst_2
    //   389: idiv
    //   390: iload_2
    //   391: invokestatic max : (II)I
    //   394: istore_3
    //   395: goto -> 230
    //   398: iload #8
    //   400: iload #5
    //   402: isub
    //   403: aload #13
    //   405: invokevirtual getMeasuredHeight : ()I
    //   408: isub
    //   409: istore_3
    //   410: iload #5
    //   412: aload #13
    //   414: invokevirtual getMeasuredHeight : ()I
    //   417: iadd
    //   418: istore #5
    //   420: goto -> 230
    //   423: iload #7
    //   425: iload_3
    //   426: isub
    //   427: iload #9
    //   429: isub
    //   430: istore #16
    //   432: iconst_0
    //   433: istore #4
    //   435: iload #4
    //   437: iload #6
    //   439: if_icmpge -> 582
    //   442: aload_0
    //   443: iload #4
    //   445: invokevirtual getChildAt : (I)Landroid/view/View;
    //   448: astore #18
    //   450: aload #18
    //   452: invokevirtual getVisibility : ()I
    //   455: bipush #8
    //   457: if_icmpeq -> 576
    //   460: aload #18
    //   462: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   465: checkcast android/support/v4/view/ViewPager$LayoutParams
    //   468: astore #13
    //   470: aload #13
    //   472: getfield isDecor : Z
    //   475: ifne -> 576
    //   478: aload_0
    //   479: aload #18
    //   481: invokevirtual infoForChild : (Landroid/view/View;)Landroid/support/v4/view/ViewPager$ItemInfo;
    //   484: astore #19
    //   486: aload #19
    //   488: ifnull -> 576
    //   491: iload_3
    //   492: iload #16
    //   494: i2f
    //   495: aload #19
    //   497: getfield offset : F
    //   500: fmul
    //   501: f2i
    //   502: iadd
    //   503: istore #9
    //   505: aload #13
    //   507: getfield needsMeasure : Z
    //   510: ifeq -> 553
    //   513: aload #13
    //   515: iconst_0
    //   516: putfield needsMeasure : Z
    //   519: aload #18
    //   521: iload #16
    //   523: i2f
    //   524: aload #13
    //   526: getfield widthFactor : F
    //   529: fmul
    //   530: f2i
    //   531: ldc_w 1073741824
    //   534: invokestatic makeMeasureSpec : (II)I
    //   537: iload #8
    //   539: iload_2
    //   540: isub
    //   541: iload #5
    //   543: isub
    //   544: ldc_w 1073741824
    //   547: invokestatic makeMeasureSpec : (II)I
    //   550: invokevirtual measure : (II)V
    //   553: aload #18
    //   555: iload #9
    //   557: iload_2
    //   558: aload #18
    //   560: invokevirtual getMeasuredWidth : ()I
    //   563: iload #9
    //   565: iadd
    //   566: aload #18
    //   568: invokevirtual getMeasuredHeight : ()I
    //   571: iload_2
    //   572: iadd
    //   573: invokevirtual layout : (IIII)V
    //   576: iinc #4, 1
    //   579: goto -> 435
    //   582: aload_0
    //   583: iload_2
    //   584: putfield mTopPageBounds : I
    //   587: aload_0
    //   588: iload #8
    //   590: iload #5
    //   592: isub
    //   593: putfield mBottomPageBounds : I
    //   596: aload_0
    //   597: iload #11
    //   599: putfield mDecorChildCount : I
    //   602: aload_0
    //   603: getfield mFirstLayout : Z
    //   606: ifeq -> 620
    //   609: aload_0
    //   610: aload_0
    //   611: getfield mCurItem : I
    //   614: iconst_0
    //   615: iconst_0
    //   616: iconst_0
    //   617: invokespecial scrollToItem : (IZIZ)V
    //   620: aload_0
    //   621: iconst_0
    //   622: putfield mFirstLayout : Z
    //   625: return
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    setMeasuredDimension(getDefaultSize(0, paramInt1), getDefaultSize(0, paramInt2));
    paramInt1 = getMeasuredWidth();
    this.mGutterSize = Math.min(paramInt1 / 10, this.mDefaultGutterSize);
    paramInt1 = paramInt1 - getPaddingLeft() - getPaddingRight();
    paramInt2 = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
    int i = getChildCount();
    byte b = 0;
    while (b < i) {
      View view = getChildAt(b);
      int k = paramInt2;
      int m = paramInt1;
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        k = paramInt2;
        m = paramInt1;
        if (layoutParams != null) {
          k = paramInt2;
          m = paramInt1;
          if (layoutParams.isDecor) {
            boolean bool;
            k = layoutParams.gravity & 0x7;
            int n = layoutParams.gravity & 0x70;
            int i1 = Integer.MIN_VALUE;
            m = Integer.MIN_VALUE;
            if (n == 48 || n == 80) {
              n = 1;
            } else {
              n = 0;
            } 
            if (k == 3 || k == 5) {
              bool = true;
            } else {
              bool = false;
            } 
            if (n != 0) {
              k = 1073741824;
            } else {
              k = i1;
              if (bool) {
                m = 1073741824;
                k = i1;
              } 
            } 
            int i2 = paramInt1;
            i1 = paramInt2;
            int i3 = k;
            k = i2;
            if (layoutParams.width != -2) {
              int i4 = 1073741824;
              i3 = i4;
              k = i2;
              if (layoutParams.width != -1) {
                k = layoutParams.width;
                i3 = i4;
              } 
            } 
            i2 = i1;
            if (layoutParams.height != -2) {
              int i4 = 1073741824;
              m = i4;
              i2 = i1;
              if (layoutParams.height != -1) {
                i2 = layoutParams.height;
                m = i4;
              } 
            } 
            view.measure(View.MeasureSpec.makeMeasureSpec(k, i3), View.MeasureSpec.makeMeasureSpec(i2, m));
            if (n != 0) {
              k = paramInt2 - view.getMeasuredHeight();
              m = paramInt1;
            } else {
              k = paramInt2;
              m = paramInt1;
              if (bool) {
                m = paramInt1 - view.getMeasuredWidth();
                k = paramInt2;
              } 
            } 
          } 
        } 
      } 
      b++;
      paramInt2 = k;
      paramInt1 = m;
    } 
    this.mChildWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824);
    this.mChildHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824);
    this.mInLayout = true;
    populate();
    this.mInLayout = false;
    int j = getChildCount();
    for (paramInt2 = 0; paramInt2 < j; paramInt2++) {
      View view = getChildAt(paramInt2);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams == null || !layoutParams.isDecor)
          view.measure(View.MeasureSpec.makeMeasureSpec((int)(paramInt1 * layoutParams.widthFactor), 1073741824), this.mChildHeightMeasureSpec); 
      } 
    } 
  }
  
  @CallSuper
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
    //   60: checkcast android/support/v4/view/ViewPager$LayoutParams
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
    //   248: iload_1
    //   249: fload_2
    //   250: iload_3
    //   251: invokespecial dispatchOnPageScrolled : (IFI)V
    //   254: aload_0
    //   255: getfield mPageTransformer : Landroid/support/v4/view/ViewPager$PageTransformer;
    //   258: ifnull -> 337
    //   261: aload_0
    //   262: invokevirtual getScrollX : ()I
    //   265: istore #5
    //   267: aload_0
    //   268: invokevirtual getChildCount : ()I
    //   271: istore_3
    //   272: iconst_0
    //   273: istore_1
    //   274: iload_1
    //   275: iload_3
    //   276: if_icmpge -> 337
    //   279: aload_0
    //   280: iload_1
    //   281: invokevirtual getChildAt : (I)Landroid/view/View;
    //   284: astore #11
    //   286: aload #11
    //   288: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   291: checkcast android/support/v4/view/ViewPager$LayoutParams
    //   294: getfield isDecor : Z
    //   297: ifeq -> 306
    //   300: iinc #1, 1
    //   303: goto -> 274
    //   306: aload #11
    //   308: invokevirtual getLeft : ()I
    //   311: iload #5
    //   313: isub
    //   314: i2f
    //   315: aload_0
    //   316: invokespecial getClientWidth : ()I
    //   319: i2f
    //   320: fdiv
    //   321: fstore_2
    //   322: aload_0
    //   323: getfield mPageTransformer : Landroid/support/v4/view/ViewPager$PageTransformer;
    //   326: aload #11
    //   328: fload_2
    //   329: invokeinterface transformPage : (Landroid/view/View;F)V
    //   334: goto -> 300
    //   337: aload_0
    //   338: iconst_1
    //   339: putfield mCalledSuper : Z
    //   342: return
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
    //   42: invokevirtual infoForChild : (Landroid/view/View;)Landroid/support/v4/view/ViewPager$ItemInfo;
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
    paramParcelable = paramParcelable;
    super.onRestoreInstanceState(paramParcelable.getSuperState());
    if (this.mAdapter != null) {
      this.mAdapter.restoreState(((SavedState)paramParcelable).adapterState, ((SavedState)paramParcelable).loader);
      setCurrentItemInternal(((SavedState)paramParcelable).position, false, true);
      return;
    } 
    this.mRestoredCurItem = ((SavedState)paramParcelable).position;
    this.mRestoredAdapterState = ((SavedState)paramParcelable).adapterState;
    this.mRestoredClassLoader = ((SavedState)paramParcelable).loader;
  }
  
  public Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.position = this.mCurItem;
    if (this.mAdapter != null)
      savedState.adapterState = this.mAdapter.saveState(); 
    return savedState;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 != paramInt3)
      recomputeScrollPosition(paramInt1, paramInt3, this.mPageMargin, this.mPageMargin); 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mFakeDragging : Z
    //   4: ifeq -> 11
    //   7: iconst_1
    //   8: istore_2
    //   9: iload_2
    //   10: ireturn
    //   11: aload_1
    //   12: invokevirtual getAction : ()I
    //   15: ifne -> 30
    //   18: aload_1
    //   19: invokevirtual getEdgeFlags : ()I
    //   22: ifeq -> 30
    //   25: iconst_0
    //   26: istore_2
    //   27: goto -> 9
    //   30: aload_0
    //   31: getfield mAdapter : Landroid/support/v4/view/PagerAdapter;
    //   34: ifnull -> 47
    //   37: aload_0
    //   38: getfield mAdapter : Landroid/support/v4/view/PagerAdapter;
    //   41: invokevirtual getCount : ()I
    //   44: ifne -> 52
    //   47: iconst_0
    //   48: istore_2
    //   49: goto -> 9
    //   52: aload_0
    //   53: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   56: ifnonnull -> 66
    //   59: aload_0
    //   60: invokestatic obtain : ()Landroid/view/VelocityTracker;
    //   63: putfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   66: aload_0
    //   67: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   70: aload_1
    //   71: invokevirtual addMovement : (Landroid/view/MotionEvent;)V
    //   74: aload_1
    //   75: invokevirtual getAction : ()I
    //   78: istore_3
    //   79: iconst_0
    //   80: istore #4
    //   82: iload #4
    //   84: istore_2
    //   85: iload_3
    //   86: sipush #255
    //   89: iand
    //   90: tableswitch default -> 132, 0 -> 148, 1 -> 423, 2 -> 215, 3 -> 563, 4 -> 135, 5 -> 592, 6 -> 621
    //   132: iload #4
    //   134: istore_2
    //   135: iload_2
    //   136: ifeq -> 143
    //   139: aload_0
    //   140: invokestatic postInvalidateOnAnimation : (Landroid/view/View;)V
    //   143: iconst_1
    //   144: istore_2
    //   145: goto -> 9
    //   148: aload_0
    //   149: getfield mScroller : Landroid/widget/Scroller;
    //   152: invokevirtual abortAnimation : ()V
    //   155: aload_0
    //   156: iconst_0
    //   157: putfield mPopulatePending : Z
    //   160: aload_0
    //   161: invokevirtual populate : ()V
    //   164: aload_1
    //   165: invokevirtual getX : ()F
    //   168: fstore #5
    //   170: aload_0
    //   171: fload #5
    //   173: putfield mInitialMotionX : F
    //   176: aload_0
    //   177: fload #5
    //   179: putfield mLastMotionX : F
    //   182: aload_1
    //   183: invokevirtual getY : ()F
    //   186: fstore #5
    //   188: aload_0
    //   189: fload #5
    //   191: putfield mInitialMotionY : F
    //   194: aload_0
    //   195: fload #5
    //   197: putfield mLastMotionY : F
    //   200: aload_0
    //   201: aload_1
    //   202: iconst_0
    //   203: invokevirtual getPointerId : (I)I
    //   206: putfield mActivePointerId : I
    //   209: iload #4
    //   211: istore_2
    //   212: goto -> 135
    //   215: aload_0
    //   216: getfield mIsBeingDragged : Z
    //   219: ifne -> 376
    //   222: aload_1
    //   223: aload_0
    //   224: getfield mActivePointerId : I
    //   227: invokevirtual findPointerIndex : (I)I
    //   230: istore_3
    //   231: iload_3
    //   232: iconst_m1
    //   233: if_icmpne -> 244
    //   236: aload_0
    //   237: invokespecial resetTouch : ()Z
    //   240: istore_2
    //   241: goto -> 135
    //   244: aload_1
    //   245: iload_3
    //   246: invokevirtual getX : (I)F
    //   249: fstore #5
    //   251: fload #5
    //   253: aload_0
    //   254: getfield mLastMotionX : F
    //   257: fsub
    //   258: invokestatic abs : (F)F
    //   261: fstore #6
    //   263: aload_1
    //   264: iload_3
    //   265: invokevirtual getY : (I)F
    //   268: fstore #7
    //   270: fload #7
    //   272: aload_0
    //   273: getfield mLastMotionY : F
    //   276: fsub
    //   277: invokestatic abs : (F)F
    //   280: fstore #8
    //   282: fload #6
    //   284: aload_0
    //   285: getfield mTouchSlop : I
    //   288: i2f
    //   289: fcmpl
    //   290: ifle -> 376
    //   293: fload #6
    //   295: fload #8
    //   297: fcmpl
    //   298: ifle -> 376
    //   301: aload_0
    //   302: iconst_1
    //   303: putfield mIsBeingDragged : Z
    //   306: aload_0
    //   307: iconst_1
    //   308: invokespecial requestParentDisallowInterceptTouchEvent : (Z)V
    //   311: fload #5
    //   313: aload_0
    //   314: getfield mInitialMotionX : F
    //   317: fsub
    //   318: fconst_0
    //   319: fcmpl
    //   320: ifle -> 408
    //   323: aload_0
    //   324: getfield mInitialMotionX : F
    //   327: aload_0
    //   328: getfield mTouchSlop : I
    //   331: i2f
    //   332: fadd
    //   333: fstore #5
    //   335: aload_0
    //   336: fload #5
    //   338: putfield mLastMotionX : F
    //   341: aload_0
    //   342: fload #7
    //   344: putfield mLastMotionY : F
    //   347: aload_0
    //   348: iconst_1
    //   349: invokevirtual setScrollState : (I)V
    //   352: aload_0
    //   353: iconst_1
    //   354: invokespecial setScrollingCacheEnabled : (Z)V
    //   357: aload_0
    //   358: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   361: astore #9
    //   363: aload #9
    //   365: ifnull -> 376
    //   368: aload #9
    //   370: iconst_1
    //   371: invokeinterface requestDisallowInterceptTouchEvent : (Z)V
    //   376: iload #4
    //   378: istore_2
    //   379: aload_0
    //   380: getfield mIsBeingDragged : Z
    //   383: ifeq -> 135
    //   386: iconst_0
    //   387: aload_0
    //   388: aload_1
    //   389: aload_1
    //   390: aload_0
    //   391: getfield mActivePointerId : I
    //   394: invokevirtual findPointerIndex : (I)I
    //   397: invokevirtual getX : (I)F
    //   400: invokespecial performDrag : (F)Z
    //   403: ior
    //   404: istore_2
    //   405: goto -> 135
    //   408: aload_0
    //   409: getfield mInitialMotionX : F
    //   412: aload_0
    //   413: getfield mTouchSlop : I
    //   416: i2f
    //   417: fsub
    //   418: fstore #5
    //   420: goto -> 335
    //   423: iload #4
    //   425: istore_2
    //   426: aload_0
    //   427: getfield mIsBeingDragged : Z
    //   430: ifeq -> 135
    //   433: aload_0
    //   434: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   437: astore #9
    //   439: aload #9
    //   441: sipush #1000
    //   444: aload_0
    //   445: getfield mMaximumVelocity : I
    //   448: i2f
    //   449: invokevirtual computeCurrentVelocity : (IF)V
    //   452: aload #9
    //   454: aload_0
    //   455: getfield mActivePointerId : I
    //   458: invokestatic getXVelocity : (Landroid/view/VelocityTracker;I)F
    //   461: f2i
    //   462: istore #10
    //   464: aload_0
    //   465: iconst_1
    //   466: putfield mPopulatePending : Z
    //   469: aload_0
    //   470: invokespecial getClientWidth : ()I
    //   473: istore #11
    //   475: aload_0
    //   476: invokevirtual getScrollX : ()I
    //   479: istore_3
    //   480: aload_0
    //   481: invokespecial infoForCurrentScrollPosition : ()Landroid/support/v4/view/ViewPager$ItemInfo;
    //   484: astore #9
    //   486: aload_0
    //   487: getfield mPageMargin : I
    //   490: i2f
    //   491: iload #11
    //   493: i2f
    //   494: fdiv
    //   495: fstore #5
    //   497: aload_0
    //   498: aload_0
    //   499: aload #9
    //   501: getfield position : I
    //   504: iload_3
    //   505: i2f
    //   506: iload #11
    //   508: i2f
    //   509: fdiv
    //   510: aload #9
    //   512: getfield offset : F
    //   515: fsub
    //   516: aload #9
    //   518: getfield widthFactor : F
    //   521: fload #5
    //   523: fadd
    //   524: fdiv
    //   525: iload #10
    //   527: aload_1
    //   528: aload_1
    //   529: aload_0
    //   530: getfield mActivePointerId : I
    //   533: invokevirtual findPointerIndex : (I)I
    //   536: invokevirtual getX : (I)F
    //   539: aload_0
    //   540: getfield mInitialMotionX : F
    //   543: fsub
    //   544: f2i
    //   545: invokespecial determineTargetPage : (IFII)I
    //   548: iconst_1
    //   549: iconst_1
    //   550: iload #10
    //   552: invokevirtual setCurrentItemInternal : (IZZI)V
    //   555: aload_0
    //   556: invokespecial resetTouch : ()Z
    //   559: istore_2
    //   560: goto -> 135
    //   563: iload #4
    //   565: istore_2
    //   566: aload_0
    //   567: getfield mIsBeingDragged : Z
    //   570: ifeq -> 135
    //   573: aload_0
    //   574: aload_0
    //   575: getfield mCurItem : I
    //   578: iconst_1
    //   579: iconst_0
    //   580: iconst_0
    //   581: invokespecial scrollToItem : (IZIZ)V
    //   584: aload_0
    //   585: invokespecial resetTouch : ()Z
    //   588: istore_2
    //   589: goto -> 135
    //   592: aload_1
    //   593: invokestatic getActionIndex : (Landroid/view/MotionEvent;)I
    //   596: istore_3
    //   597: aload_0
    //   598: aload_1
    //   599: iload_3
    //   600: invokevirtual getX : (I)F
    //   603: putfield mLastMotionX : F
    //   606: aload_0
    //   607: aload_1
    //   608: iload_3
    //   609: invokevirtual getPointerId : (I)I
    //   612: putfield mActivePointerId : I
    //   615: iload #4
    //   617: istore_2
    //   618: goto -> 135
    //   621: aload_0
    //   622: aload_1
    //   623: invokespecial onSecondaryPointerUp : (Landroid/view/MotionEvent;)V
    //   626: aload_0
    //   627: aload_1
    //   628: aload_1
    //   629: aload_0
    //   630: getfield mActivePointerId : I
    //   633: invokevirtual findPointerIndex : (I)I
    //   636: invokevirtual getX : (I)F
    //   639: putfield mLastMotionX : F
    //   642: iload #4
    //   644: istore_2
    //   645: goto -> 135
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
    populate(this.mCurItem);
  }
  
  void populate(int paramInt) {
    ItemInfo itemInfo = null;
    if (this.mCurItem != paramInt) {
      itemInfo = infoForPosition(this.mCurItem);
      this.mCurItem = paramInt;
    } 
    if (this.mAdapter == null) {
      sortChildDrawingOrder();
      return;
    } 
    if (this.mPopulatePending) {
      sortChildDrawingOrder();
      return;
    } 
    if (getWindowToken() != null) {
      this.mAdapter.startUpdate(this);
      paramInt = this.mOffscreenPageLimit;
      int i = Math.max(0, this.mCurItem - paramInt);
      int j = this.mAdapter.getCount();
      int k = Math.min(j - 1, this.mCurItem + paramInt);
      if (j != this.mExpectedAdapterCount) {
        String str;
        try {
          str = getResources().getResourceName(getId());
        } catch (android.content.res.Resources.NotFoundException notFoundException) {
          str = Integer.toHexString(getId());
        } 
        throw new IllegalStateException("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: " + this.mExpectedAdapterCount + ", found: " + j + " Pager id: " + str + " Pager class: " + getClass() + " Problematic adapter: " + this.mAdapter.getClass());
      } 
      ItemInfo itemInfo1 = null;
      paramInt = 0;
      while (true) {
        ItemInfo itemInfo2 = itemInfo1;
        if (paramInt < this.mItems.size()) {
          ItemInfo itemInfo3 = this.mItems.get(paramInt);
          if (itemInfo3.position >= this.mCurItem) {
            itemInfo2 = itemInfo1;
            if (itemInfo3.position == this.mCurItem)
              itemInfo2 = itemInfo3; 
          } else {
            paramInt++;
            continue;
          } 
        } 
        itemInfo1 = itemInfo2;
        if (itemInfo2 == null) {
          itemInfo1 = itemInfo2;
          if (j > 0)
            itemInfo1 = addNewItem(this.mCurItem, paramInt); 
        } 
        if (itemInfo1 != null) {
          Object object1;
          Object object2;
          float f2;
          float f1 = 0.0F;
          int m = paramInt - 1;
          if (m >= 0) {
            itemInfo2 = this.mItems.get(m);
          } else {
            itemInfo2 = null;
          } 
          int n = getClientWidth();
          if (n <= 0) {
            f2 = 0.0F;
          } else {
            f2 = 2.0F - itemInfo1.widthFactor + getPaddingLeft() / n;
          } 
          int i1 = this.mCurItem - 1;
          ItemInfo itemInfo3 = itemInfo2;
          int i2 = paramInt;
          while (true) {
            float f4;
            if (i1 >= 0)
              if (object1 >= f2) {
                if (i1 < i) {
                  if (itemInfo3 != null) {
                    paramInt = i2;
                    Object object4 = object1;
                    itemInfo2 = itemInfo3;
                    Object object3 = object2;
                    if (i1 == itemInfo3.position) {
                      paramInt = i2;
                      object4 = object1;
                      itemInfo2 = itemInfo3;
                      object3 = object2;
                      if (!itemInfo3.scrolling) {
                        this.mItems.remove(object2);
                        this.mAdapter.destroyItem(this, i1, itemInfo3.object);
                        int i3 = object2 - 1;
                        paramInt = i2 - 1;
                        if (i3 >= 0) {
                          itemInfo2 = this.mItems.get(i3);
                          object4 = object1;
                        } else {
                          itemInfo2 = null;
                          object4 = object1;
                        } 
                      } 
                    } 
                    continue;
                  } 
                } else {
                  if (itemInfo3 != null && i1 == itemInfo3.position) {
                    f4 = object1 + itemInfo3.widthFactor;
                    int i3 = object2 - 1;
                    if (i3 >= 0) {
                      itemInfo2 = this.mItems.get(i3);
                    } else {
                      itemInfo2 = null;
                    } 
                    paramInt = i2;
                  } else {
                    f4 = object1 + (addNewItem(i1, object2 + 1)).widthFactor;
                    paramInt = i2 + 1;
                    if (object2 >= null) {
                      itemInfo2 = this.mItems.get(object2);
                    } else {
                      itemInfo2 = null;
                    } 
                    Object object = object2;
                  } 
                  continue;
                } 
              } else {
                continue;
              }  
            float f3 = itemInfo1.widthFactor;
            i1 = i2 + 1;
            if (f3 < 2.0F) {
              if (i1 < this.mItems.size()) {
                itemInfo2 = this.mItems.get(i1);
              } else {
                itemInfo2 = null;
              } 
              if (n <= 0) {
                f2 = 0.0F;
              } else {
                f2 = getPaddingRight() / n + 2.0F;
              } 
              int i3 = this.mCurItem + 1;
              itemInfo3 = itemInfo2;
              while (true) {
                if (i3 < j)
                  if (f3 >= f2 && i3 > k) {
                    if (itemInfo3 != null) {
                      f4 = f3;
                      itemInfo2 = itemInfo3;
                      paramInt = i1;
                      if (i3 == itemInfo3.position) {
                        f4 = f3;
                        itemInfo2 = itemInfo3;
                        paramInt = i1;
                        if (!itemInfo3.scrolling) {
                          this.mItems.remove(i1);
                          this.mAdapter.destroyItem(this, i3, itemInfo3.object);
                          if (i1 < this.mItems.size()) {
                            itemInfo2 = this.mItems.get(i1);
                            paramInt = i1;
                            f4 = f3;
                          } else {
                            itemInfo2 = null;
                            f4 = f3;
                            paramInt = i1;
                          } 
                        } 
                      } 
                      continue;
                    } 
                  } else {
                    if (itemInfo3 != null && i3 == itemInfo3.position) {
                      f4 = f3 + itemInfo3.widthFactor;
                      paramInt = i1 + 1;
                      if (paramInt < this.mItems.size()) {
                        itemInfo2 = this.mItems.get(paramInt);
                      } else {
                        itemInfo2 = null;
                      } 
                    } else {
                      itemInfo2 = addNewItem(i3, i1);
                      paramInt = i1 + 1;
                      f4 = f3 + itemInfo2.widthFactor;
                      if (paramInt < this.mItems.size()) {
                        itemInfo2 = this.mItems.get(paramInt);
                      } else {
                        itemInfo2 = null;
                      } 
                    } 
                    continue;
                  }  
                calculatePageOffsets(itemInfo1, i2, itemInfo);
                PagerAdapter pagerAdapter = this.mAdapter;
                paramInt = this.mCurItem;
                if (itemInfo1 != null) {
                  Object object = itemInfo1.object;
                } else {
                  itemInfo2 = null;
                } 
                pagerAdapter.setPrimaryItem(this, paramInt, itemInfo2);
                this.mAdapter.finishUpdate(this);
                i3 = getChildCount();
                for (paramInt = 0; paramInt < i3; paramInt++) {
                  View view = getChildAt(paramInt);
                  LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                  layoutParams.childIndex = paramInt;
                  if (!layoutParams.isDecor && layoutParams.widthFactor == 0.0F) {
                    ItemInfo itemInfo4 = infoForChild(view);
                    if (itemInfo4 != null) {
                      layoutParams.widthFactor = itemInfo4.widthFactor;
                      layoutParams.position = itemInfo4.position;
                    } 
                  } 
                } 
                sortChildDrawingOrder();
                if (hasFocus()) {
                  View view = findFocus();
                  if (view != null) {
                    ItemInfo itemInfo4 = infoForAnyChild(view);
                  } else {
                    view = null;
                  } 
                  if (view == null || ((ItemInfo)view).position != this.mCurItem) {
                    paramInt = 0;
                    while (true) {
                      if (paramInt < getChildCount()) {
                        view = getChildAt(paramInt);
                        ItemInfo itemInfo4 = infoForChild(view);
                        if (itemInfo4 == null || itemInfo4.position != this.mCurItem || !view.requestFocus(2)) {
                          paramInt++;
                          continue;
                        } 
                      } 
                      return;
                    } 
                    break;
                  } 
                  // Byte code: goto -> 35
                } 
                // Byte code: goto -> 35
                i3++;
                f3 = f4;
                itemInfo3 = itemInfo2;
                i1 = paramInt;
              } 
              break;
            } 
            continue;
            i1--;
            i2 = paramInt;
            object1 = SYNTHETIC_LOCAL_VARIABLE_16;
            itemInfo3 = itemInfo2;
            object2 = SYNTHETIC_LOCAL_VARIABLE_15;
          } 
        } 
        continue;
      } 
    } 
  }
  
  public void removeOnAdapterChangeListener(@NonNull OnAdapterChangeListener paramOnAdapterChangeListener) {
    if (this.mAdapterChangeListeners != null)
      this.mAdapterChangeListeners.remove(paramOnAdapterChangeListener); 
  }
  
  public void removeOnPageChangeListener(OnPageChangeListener paramOnPageChangeListener) {
    if (this.mOnPageChangeListeners != null)
      this.mOnPageChangeListeners.remove(paramOnPageChangeListener); 
  }
  
  public void removeView(View paramView) {
    if (this.mInLayout) {
      removeViewInLayout(paramView);
      return;
    } 
    super.removeView(paramView);
  }
  
  public void setAdapter(PagerAdapter paramPagerAdapter) {
    if (this.mAdapter != null) {
      this.mAdapter.setViewPagerObserver(null);
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
    this.mExpectedAdapterCount = 0;
    if (this.mAdapter != null) {
      if (this.mObserver == null)
        this.mObserver = new PagerObserver(); 
      this.mAdapter.setViewPagerObserver(this.mObserver);
      this.mPopulatePending = false;
      boolean bool = this.mFirstLayout;
      this.mFirstLayout = true;
      this.mExpectedAdapterCount = this.mAdapter.getCount();
      if (this.mRestoredCurItem >= 0) {
        this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
        setCurrentItemInternal(this.mRestoredCurItem, false, true);
        this.mRestoredCurItem = -1;
        this.mRestoredAdapterState = null;
        this.mRestoredClassLoader = null;
      } else if (!bool) {
        populate();
      } else {
        requestLayout();
      } 
    } 
    if (this.mAdapterChangeListeners != null && !this.mAdapterChangeListeners.isEmpty()) {
      byte b = 0;
      int i = this.mAdapterChangeListeners.size();
      while (b < i) {
        ((OnAdapterChangeListener)this.mAdapterChangeListeners.get(b)).onAdapterChanged(this, pagerAdapter, paramPagerAdapter);
        b++;
      } 
    } 
  }
  
  void setChildrenDrawingOrderEnabledCompat(boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 7) {
      if (this.mSetChildrenDrawingOrderEnabled == null)
        try {
          this.mSetChildrenDrawingOrderEnabled = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", new Class[] { boolean.class });
        } catch (NoSuchMethodException noSuchMethodException) {
          Log.e("ViewPager", "Can't find setChildrenDrawingOrderEnabled", noSuchMethodException);
        }  
      try {
        this.mSetChildrenDrawingOrderEnabled.invoke(this, new Object[] { Boolean.valueOf(paramBoolean) });
      } catch (Exception exception) {
        Log.e("ViewPager", "Error changing children drawing order", exception);
      } 
    } 
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
    int i;
    boolean bool = true;
    if (this.mAdapter == null || this.mAdapter.getCount() <= 0) {
      setScrollingCacheEnabled(false);
      return;
    } 
    if (!paramBoolean2 && this.mCurItem == paramInt1 && this.mItems.size() != 0) {
      setScrollingCacheEnabled(false);
      return;
    } 
    if (paramInt1 < 0) {
      i = 0;
    } else {
      i = paramInt1;
      if (paramInt1 >= this.mAdapter.getCount())
        i = this.mAdapter.getCount() - 1; 
    } 
    paramInt1 = this.mOffscreenPageLimit;
    if (i > this.mCurItem + paramInt1 || i < this.mCurItem - paramInt1)
      for (paramInt1 = 0; paramInt1 < this.mItems.size(); paramInt1++)
        ((ItemInfo)this.mItems.get(paramInt1)).scrolling = true;  
    if (this.mCurItem != i) {
      paramBoolean2 = bool;
    } else {
      paramBoolean2 = false;
    } 
    if (this.mFirstLayout) {
      this.mCurItem = i;
      if (paramBoolean2)
        dispatchOnPageSelected(i); 
      requestLayout();
      return;
    } 
    populate(i);
    scrollToItem(i, paramBoolean1, paramInt2, paramBoolean2);
  }
  
  OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener paramOnPageChangeListener) {
    OnPageChangeListener onPageChangeListener = this.mInternalPageChangeListener;
    this.mInternalPageChangeListener = paramOnPageChangeListener;
    return onPageChangeListener;
  }
  
  public void setOffscreenPageLimit(int paramInt) {
    int i = paramInt;
    if (paramInt < 1) {
      Log.w("ViewPager", "Requested offscreen page limit " + paramInt + " too small; defaulting to " + '\001');
      i = 1;
    } 
    if (i != this.mOffscreenPageLimit) {
      this.mOffscreenPageLimit = i;
      populate();
    } 
  }
  
  @Deprecated
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
  
  public void setPageMarginDrawable(@DrawableRes int paramInt) {
    setPageMarginDrawable(ContextCompat.getDrawable(getContext(), paramInt));
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
  
  public void setPageTransformer(boolean paramBoolean, PageTransformer paramPageTransformer) {
    setPageTransformer(paramBoolean, paramPageTransformer, 2);
  }
  
  public void setPageTransformer(boolean paramBoolean, PageTransformer paramPageTransformer, int paramInt) {
    byte b = 1;
    if (Build.VERSION.SDK_INT >= 11) {
      boolean bool1;
      boolean bool2;
      boolean bool3;
      if (paramPageTransformer != null) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (this.mPageTransformer != null) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      if (bool1 != bool2) {
        bool3 = true;
      } else {
        bool3 = false;
      } 
      this.mPageTransformer = paramPageTransformer;
      setChildrenDrawingOrderEnabledCompat(bool1);
      if (bool1) {
        if (paramBoolean)
          b = 2; 
        this.mDrawingOrder = b;
        this.mPageTransformerLayerType = paramInt;
      } else {
        this.mDrawingOrder = 0;
      } 
      if (bool3)
        populate(); 
    } 
  }
  
  void setScrollState(int paramInt) {
    if (this.mScrollState != paramInt) {
      this.mScrollState = paramInt;
      if (this.mPageTransformer != null) {
        boolean bool;
        if (paramInt != 0) {
          bool = true;
        } else {
          bool = false;
        } 
        enableLayers(bool);
      } 
      dispatchOnScrollStateChanged(paramInt);
    } 
  }
  
  void smoothScrollTo(int paramInt1, int paramInt2) {
    smoothScrollTo(paramInt1, paramInt2, 0);
  }
  
  void smoothScrollTo(int paramInt1, int paramInt2, int paramInt3) {
    int i;
    if (getChildCount() == 0) {
      setScrollingCacheEnabled(false);
      return;
    } 
    if (this.mScroller != null && !this.mScroller.isFinished()) {
      i = 1;
    } else {
      i = 0;
    } 
    if (i) {
      if (this.mIsScrollStarted) {
        i = this.mScroller.getCurrX();
      } else {
        i = this.mScroller.getStartX();
      } 
      this.mScroller.abortAnimation();
      setScrollingCacheEnabled(false);
    } else {
      i = getScrollX();
    } 
    int j = getScrollY();
    int k = paramInt1 - i;
    paramInt2 -= j;
    if (k == 0 && paramInt2 == 0) {
      completeScroll(false);
      populate();
      setScrollState(0);
      return;
    } 
    setScrollingCacheEnabled(true);
    setScrollState(2);
    paramInt1 = getClientWidth();
    int m = paramInt1 / 2;
    float f1 = Math.min(1.0F, 1.0F * Math.abs(k) / paramInt1);
    float f2 = m;
    float f3 = m;
    f1 = distanceInfluenceForSnapDuration(f1);
    paramInt3 = Math.abs(paramInt3);
    if (paramInt3 > 0) {
      paramInt1 = Math.round(1000.0F * Math.abs((f2 + f3 * f1) / paramInt3)) * 4;
    } else {
      f2 = paramInt1;
      f3 = this.mAdapter.getPageWidth(this.mCurItem);
      paramInt1 = (int)((1.0F + Math.abs(k) / (this.mPageMargin + f2 * f3)) * 100.0F);
    } 
    paramInt1 = Math.min(paramInt1, 600);
    this.mIsScrollStarted = false;
    this.mScroller.startScroll(i, j, k, paramInt2, paramInt1);
    ViewCompat.postInvalidateOnAnimation((View)this);
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable) {
    return (super.verifyDrawable(paramDrawable) || paramDrawable == this.mMarginDrawable);
  }
  
  @Inherited
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.TYPE})
  public static @interface DecorView {}
  
  static class ItemInfo {
    Object object;
    
    float offset;
    
    int position;
    
    boolean scrolling;
    
    float widthFactor;
  }
  
  public static class LayoutParams extends ViewGroup.LayoutParams {
    int childIndex;
    
    public int gravity;
    
    public boolean isDecor;
    
    boolean needsMeasure;
    
    int position;
    
    float widthFactor = 0.0F;
    
    public LayoutParams() {
      super(-1, -1);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, ViewPager.LAYOUT_ATTRS);
      this.gravity = typedArray.getInteger(0, 48);
      typedArray.recycle();
    }
  }
  
  class MyAccessibilityDelegate extends AccessibilityDelegateCompat {
    private boolean canScroll() {
      boolean bool = true;
      if (ViewPager.this.mAdapter == null || ViewPager.this.mAdapter.getCount() <= 1)
        bool = false; 
      return bool;
    }
    
    public void onInitializeAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      super.onInitializeAccessibilityEvent(param1View, param1AccessibilityEvent);
      param1AccessibilityEvent.setClassName(ViewPager.class.getName());
      AccessibilityRecordCompat accessibilityRecordCompat = AccessibilityEventCompat.asRecord(param1AccessibilityEvent);
      accessibilityRecordCompat.setScrollable(canScroll());
      if (param1AccessibilityEvent.getEventType() == 4096 && ViewPager.this.mAdapter != null) {
        accessibilityRecordCompat.setItemCount(ViewPager.this.mAdapter.getCount());
        accessibilityRecordCompat.setFromIndex(ViewPager.this.mCurItem);
        accessibilityRecordCompat.setToIndex(ViewPager.this.mCurItem);
      } 
    }
    
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
      param1AccessibilityNodeInfoCompat.setClassName(ViewPager.class.getName());
      param1AccessibilityNodeInfoCompat.setScrollable(canScroll());
      if (ViewPager.this.canScrollHorizontally(1))
        param1AccessibilityNodeInfoCompat.addAction(4096); 
      if (ViewPager.this.canScrollHorizontally(-1))
        param1AccessibilityNodeInfoCompat.addAction(8192); 
    }
    
    public boolean performAccessibilityAction(View param1View, int param1Int, Bundle param1Bundle) {
      boolean bool = true;
      if (!super.performAccessibilityAction(param1View, param1Int, param1Bundle)) {
        switch (param1Int) {
          default:
            return false;
          case 4096:
            if (ViewPager.this.canScrollHorizontally(1)) {
              ViewPager.this.setCurrentItem(ViewPager.this.mCurItem + 1);
              return bool;
            } 
            return false;
          case 8192:
            break;
        } 
        if (ViewPager.this.canScrollHorizontally(-1)) {
          ViewPager.this.setCurrentItem(ViewPager.this.mCurItem - 1);
          return bool;
        } 
        bool = false;
      } 
      return bool;
    }
  }
  
  public static interface OnAdapterChangeListener {
    void onAdapterChanged(@NonNull ViewPager param1ViewPager, @Nullable PagerAdapter param1PagerAdapter1, @Nullable PagerAdapter param1PagerAdapter2);
  }
  
  public static interface OnPageChangeListener {
    void onPageScrollStateChanged(int param1Int);
    
    void onPageScrolled(int param1Int1, float param1Float, int param1Int2);
    
    void onPageSelected(int param1Int);
  }
  
  public static interface PageTransformer {
    void transformPage(View param1View, float param1Float);
  }
  
  private class PagerObserver extends DataSetObserver {
    public void onChanged() {
      ViewPager.this.dataSetChanged();
    }
    
    public void onInvalidated() {
      ViewPager.this.dataSetChanged();
    }
  }
  
  public static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
          public ViewPager.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
            return new ViewPager.SavedState(param2Parcel, param2ClassLoader);
          }
          
          public ViewPager.SavedState[] newArray(int param2Int) {
            return new ViewPager.SavedState[param2Int];
          }
        });
    
    Parcelable adapterState;
    
    ClassLoader loader;
    
    int position;
    
    SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
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
    public ViewPager.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new ViewPager.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public ViewPager.SavedState[] newArray(int param1Int) {
      return new ViewPager.SavedState[param1Int];
    }
  }
  
  public static class SimpleOnPageChangeListener implements OnPageChangeListener {
    public void onPageScrollStateChanged(int param1Int) {}
    
    public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {}
    
    public void onPageSelected(int param1Int) {}
  }
  
  static class ViewPositionComparator implements Comparator<View> {
    public int compare(View param1View1, View param1View2) {
      ViewPager.LayoutParams layoutParams1 = (ViewPager.LayoutParams)param1View1.getLayoutParams();
      ViewPager.LayoutParams layoutParams2 = (ViewPager.LayoutParams)param1View2.getLayoutParams();
      return (layoutParams1.isDecor != layoutParams2.isDecor) ? (layoutParams1.isDecor ? 1 : -1) : (layoutParams1.position - layoutParams2.position);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/view/ViewPager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */