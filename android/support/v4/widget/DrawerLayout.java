package android.support.v4.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewGroupCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import java.util.ArrayList;
import java.util.List;

public class DrawerLayout extends ViewGroup implements DrawerLayoutImpl {
  private static final boolean ALLOW_EDGE_LOCK = false;
  
  static final boolean CAN_HIDE_DESCENDANTS;
  
  private static final boolean CHILDREN_DISALLOW_INTERCEPT = true;
  
  private static final int DEFAULT_SCRIM_COLOR = -1728053248;
  
  private static final int DRAWER_ELEVATION = 10;
  
  static final DrawerLayoutCompatImpl IMPL;
  
  static final int[] LAYOUT_ATTRS = new int[] { 16842931 };
  
  public static final int LOCK_MODE_LOCKED_CLOSED = 1;
  
  public static final int LOCK_MODE_LOCKED_OPEN = 2;
  
  public static final int LOCK_MODE_UNDEFINED = 3;
  
  public static final int LOCK_MODE_UNLOCKED = 0;
  
  private static final int MIN_DRAWER_MARGIN = 64;
  
  private static final int MIN_FLING_VELOCITY = 400;
  
  private static final int PEEK_DELAY = 160;
  
  private static final boolean SET_DRAWER_SHADOW_FROM_ELEVATION;
  
  public static final int STATE_DRAGGING = 1;
  
  public static final int STATE_IDLE = 0;
  
  public static final int STATE_SETTLING = 2;
  
  private static final String TAG = "DrawerLayout";
  
  private static final float TOUCH_SLOP_SENSITIVITY = 1.0F;
  
  private final ChildAccessibilityDelegate mChildAccessibilityDelegate = new ChildAccessibilityDelegate();
  
  private boolean mChildrenCanceledTouch;
  
  private boolean mDisallowInterceptRequested;
  
  private boolean mDrawStatusBarBackground;
  
  private float mDrawerElevation;
  
  private int mDrawerState;
  
  private boolean mFirstLayout = true;
  
  private boolean mInLayout;
  
  private float mInitialMotionX;
  
  private float mInitialMotionY;
  
  private Object mLastInsets;
  
  private final ViewDragCallback mLeftCallback;
  
  private final ViewDragHelper mLeftDragger;
  
  @Nullable
  private DrawerListener mListener;
  
  private List<DrawerListener> mListeners;
  
  private int mLockModeEnd = 3;
  
  private int mLockModeLeft = 3;
  
  private int mLockModeRight = 3;
  
  private int mLockModeStart = 3;
  
  private int mMinDrawerMargin;
  
  private final ArrayList<View> mNonDrawerViews;
  
  private final ViewDragCallback mRightCallback;
  
  private final ViewDragHelper mRightDragger;
  
  private int mScrimColor = -1728053248;
  
  private float mScrimOpacity;
  
  private Paint mScrimPaint = new Paint();
  
  private Drawable mShadowEnd = null;
  
  private Drawable mShadowLeft = null;
  
  private Drawable mShadowLeftResolved;
  
  private Drawable mShadowRight = null;
  
  private Drawable mShadowRightResolved;
  
  private Drawable mShadowStart = null;
  
  private Drawable mStatusBarBackground;
  
  private CharSequence mTitleLeft;
  
  private CharSequence mTitleRight;
  
  static {
    if (Build.VERSION.SDK_INT >= 19) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    CAN_HIDE_DESCENDANTS = bool2;
    if (Build.VERSION.SDK_INT >= 21) {
      bool2 = bool1;
    } else {
      bool2 = false;
    } 
    SET_DRAWER_SHADOW_FROM_ELEVATION = bool2;
    if (Build.VERSION.SDK_INT >= 21) {
      IMPL = new DrawerLayoutCompatImplApi21();
      return;
    } 
    IMPL = new DrawerLayoutCompatImplBase();
  }
  
  public DrawerLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public DrawerLayout(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public DrawerLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    setDescendantFocusability(262144);
    float f1 = (getResources().getDisplayMetrics()).density;
    this.mMinDrawerMargin = (int)(64.0F * f1 + 0.5F);
    float f2 = 400.0F * f1;
    this.mLeftCallback = new ViewDragCallback(3);
    this.mRightCallback = new ViewDragCallback(5);
    this.mLeftDragger = ViewDragHelper.create(this, 1.0F, this.mLeftCallback);
    this.mLeftDragger.setEdgeTrackingEnabled(1);
    this.mLeftDragger.setMinVelocity(f2);
    this.mLeftCallback.setDragger(this.mLeftDragger);
    this.mRightDragger = ViewDragHelper.create(this, 1.0F, this.mRightCallback);
    this.mRightDragger.setEdgeTrackingEnabled(2);
    this.mRightDragger.setMinVelocity(f2);
    this.mRightCallback.setDragger(this.mRightDragger);
    setFocusableInTouchMode(true);
    ViewCompat.setImportantForAccessibility((View)this, 1);
    ViewCompat.setAccessibilityDelegate((View)this, new AccessibilityDelegate());
    ViewGroupCompat.setMotionEventSplittingEnabled(this, false);
    if (ViewCompat.getFitsSystemWindows((View)this)) {
      IMPL.configureApplyInsets((View)this);
      this.mStatusBarBackground = IMPL.getDefaultStatusBarBackground(paramContext);
    } 
    this.mDrawerElevation = 10.0F * f1;
    this.mNonDrawerViews = new ArrayList<View>();
  }
  
  static String gravityToString(int paramInt) {
    return ((paramInt & 0x3) == 3) ? "LEFT" : (((paramInt & 0x5) == 5) ? "RIGHT" : Integer.toHexString(paramInt));
  }
  
  private static boolean hasOpaqueBackground(View paramView) {
    boolean bool1 = false;
    Drawable drawable = paramView.getBackground();
    boolean bool2 = bool1;
    if (drawable != null) {
      bool2 = bool1;
      if (drawable.getOpacity() == -1)
        bool2 = true; 
    } 
    return bool2;
  }
  
  private boolean hasPeekingDrawer() {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getChildCount : ()I
    //   4: istore_1
    //   5: iconst_0
    //   6: istore_2
    //   7: iload_2
    //   8: iload_1
    //   9: if_icmpge -> 39
    //   12: aload_0
    //   13: iload_2
    //   14: invokevirtual getChildAt : (I)Landroid/view/View;
    //   17: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   20: checkcast android/support/v4/widget/DrawerLayout$LayoutParams
    //   23: getfield isPeeking : Z
    //   26: ifeq -> 33
    //   29: iconst_1
    //   30: istore_3
    //   31: iload_3
    //   32: ireturn
    //   33: iinc #2, 1
    //   36: goto -> 7
    //   39: iconst_0
    //   40: istore_3
    //   41: goto -> 31
  }
  
  private boolean hasVisibleDrawer() {
    return (findVisibleDrawer() != null);
  }
  
  static boolean includeChildForAccessibility(View paramView) {
    return (ViewCompat.getImportantForAccessibility(paramView) != 4 && ViewCompat.getImportantForAccessibility(paramView) != 2);
  }
  
  private boolean mirror(Drawable paramDrawable, int paramInt) {
    if (paramDrawable == null || !DrawableCompat.isAutoMirrored(paramDrawable))
      return false; 
    DrawableCompat.setLayoutDirection(paramDrawable, paramInt);
    return true;
  }
  
  private Drawable resolveLeftShadow() {
    int i = ViewCompat.getLayoutDirection((View)this);
    if (i == 0) {
      if (this.mShadowStart != null) {
        mirror(this.mShadowStart, i);
        return this.mShadowStart;
      } 
    } else if (this.mShadowEnd != null) {
      mirror(this.mShadowEnd, i);
      return this.mShadowEnd;
    } 
    return this.mShadowLeft;
  }
  
  private Drawable resolveRightShadow() {
    int i = ViewCompat.getLayoutDirection((View)this);
    if (i == 0) {
      if (this.mShadowEnd != null) {
        mirror(this.mShadowEnd, i);
        return this.mShadowEnd;
      } 
    } else if (this.mShadowStart != null) {
      mirror(this.mShadowStart, i);
      return this.mShadowStart;
    } 
    return this.mShadowRight;
  }
  
  private void resolveShadowDrawables() {
    if (!SET_DRAWER_SHADOW_FROM_ELEVATION) {
      this.mShadowLeftResolved = resolveLeftShadow();
      this.mShadowRightResolved = resolveRightShadow();
    } 
  }
  
  private void updateChildrenImportantForAccessibility(View paramView, boolean paramBoolean) {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      if ((!paramBoolean && !isDrawerView(view)) || (paramBoolean && view == paramView)) {
        ViewCompat.setImportantForAccessibility(view, 1);
      } else {
        ViewCompat.setImportantForAccessibility(view, 4);
      } 
    } 
  }
  
  public void addDrawerListener(@NonNull DrawerListener paramDrawerListener) {
    if (paramDrawerListener != null) {
      if (this.mListeners == null)
        this.mListeners = new ArrayList<DrawerListener>(); 
      this.mListeners.add(paramDrawerListener);
    } 
  }
  
  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2) {
    if (getDescendantFocusability() != 393216) {
      int i = getChildCount();
      int j = 0;
      byte b;
      for (b = 0; b < i; b++) {
        View view = getChildAt(b);
        if (isDrawerView(view)) {
          if (isDrawerOpen(view)) {
            j = 1;
            view.addFocusables(paramArrayList, paramInt1, paramInt2);
          } 
        } else {
          this.mNonDrawerViews.add(view);
        } 
      } 
      if (!j) {
        j = this.mNonDrawerViews.size();
        for (b = 0; b < j; b++) {
          View view = this.mNonDrawerViews.get(b);
          if (view.getVisibility() == 0)
            view.addFocusables(paramArrayList, paramInt1, paramInt2); 
        } 
      } 
      this.mNonDrawerViews.clear();
    } 
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    super.addView(paramView, paramInt, paramLayoutParams);
    if (findOpenDrawer() != null || isDrawerView(paramView)) {
      ViewCompat.setImportantForAccessibility(paramView, 4);
    } else {
      ViewCompat.setImportantForAccessibility(paramView, 1);
    } 
    if (!CAN_HIDE_DESCENDANTS)
      ViewCompat.setAccessibilityDelegate(paramView, this.mChildAccessibilityDelegate); 
  }
  
  void cancelChildViewTouch() {
    if (!this.mChildrenCanceledTouch) {
      long l = SystemClock.uptimeMillis();
      MotionEvent motionEvent = MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0);
      int i = getChildCount();
      for (byte b = 0; b < i; b++)
        getChildAt(b).dispatchTouchEvent(motionEvent); 
      motionEvent.recycle();
      this.mChildrenCanceledTouch = true;
    } 
  }
  
  boolean checkDrawerViewAbsoluteGravity(View paramView, int paramInt) {
    return ((getDrawerViewAbsoluteGravity(paramView) & paramInt) == paramInt);
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (paramLayoutParams instanceof LayoutParams && super.checkLayoutParams(paramLayoutParams));
  }
  
  public void closeDrawer(int paramInt) {
    closeDrawer(paramInt, true);
  }
  
  public void closeDrawer(int paramInt, boolean paramBoolean) {
    View view = findDrawerWithGravity(paramInt);
    if (view == null)
      throw new IllegalArgumentException("No drawer view found with gravity " + gravityToString(paramInt)); 
    closeDrawer(view, paramBoolean);
  }
  
  public void closeDrawer(View paramView) {
    closeDrawer(paramView, true);
  }
  
  public void closeDrawer(View paramView, boolean paramBoolean) {
    if (!isDrawerView(paramView))
      throw new IllegalArgumentException("View " + paramView + " is not a sliding drawer"); 
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (this.mFirstLayout) {
      layoutParams.onScreen = 0.0F;
      layoutParams.openState = 0;
    } else if (paramBoolean) {
      layoutParams.openState |= 0x4;
      if (checkDrawerViewAbsoluteGravity(paramView, 3)) {
        this.mLeftDragger.smoothSlideViewTo(paramView, -paramView.getWidth(), paramView.getTop());
      } else {
        this.mRightDragger.smoothSlideViewTo(paramView, getWidth(), paramView.getTop());
      } 
    } else {
      moveDrawerToOffset(paramView, 0.0F);
      updateDrawerState(layoutParams.gravity, 0, paramView);
      paramView.setVisibility(4);
    } 
    invalidate();
  }
  
  public void closeDrawers() {
    closeDrawers(false);
  }
  
  void closeDrawers(boolean paramBoolean) {
    boolean bool;
    byte b1 = 0;
    int i = getChildCount();
    byte b2 = 0;
    while (b2 < i) {
      boolean bool1;
      View view = getChildAt(b2);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      int j = b1;
      if (isDrawerView(view))
        if (paramBoolean && !layoutParams.isPeeking) {
          j = b1;
        } else {
          boolean bool2;
          j = view.getWidth();
          if (checkDrawerViewAbsoluteGravity(view, 3)) {
            bool2 = b1 | this.mLeftDragger.smoothSlideViewTo(view, -j, view.getTop());
          } else {
            bool2 |= this.mRightDragger.smoothSlideViewTo(view, getWidth(), view.getTop());
          } 
          layoutParams.isPeeking = false;
          bool1 = bool2;
        }  
      b2++;
      bool = bool1;
    } 
    this.mLeftCallback.removeCallbacks();
    this.mRightCallback.removeCallbacks();
    if (bool)
      invalidate(); 
  }
  
  public void computeScroll() {
    int i = getChildCount();
    float f = 0.0F;
    for (byte b = 0; b < i; b++)
      f = Math.max(f, ((LayoutParams)getChildAt(b).getLayoutParams()).onScreen); 
    this.mScrimOpacity = f;
    if ((this.mLeftDragger.continueSettling(true) | this.mRightDragger.continueSettling(true)) != 0)
      ViewCompat.postInvalidateOnAnimation((View)this); 
  }
  
  void dispatchOnDrawerClosed(View paramView) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if ((layoutParams.openState & 0x1) == 1) {
      layoutParams.openState = 0;
      if (this.mListeners != null)
        for (int i = this.mListeners.size() - 1; i >= 0; i--)
          ((DrawerListener)this.mListeners.get(i)).onDrawerClosed(paramView);  
      updateChildrenImportantForAccessibility(paramView, false);
      if (hasWindowFocus()) {
        paramView = getRootView();
        if (paramView != null)
          paramView.sendAccessibilityEvent(32); 
      } 
    } 
  }
  
  void dispatchOnDrawerOpened(View paramView) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if ((layoutParams.openState & 0x1) == 0) {
      layoutParams.openState = 1;
      if (this.mListeners != null)
        for (int i = this.mListeners.size() - 1; i >= 0; i--)
          ((DrawerListener)this.mListeners.get(i)).onDrawerOpened(paramView);  
      updateChildrenImportantForAccessibility(paramView, true);
      if (hasWindowFocus())
        sendAccessibilityEvent(32); 
    } 
  }
  
  void dispatchOnDrawerSlide(View paramView, float paramFloat) {
    if (this.mListeners != null)
      for (int i = this.mListeners.size() - 1; i >= 0; i--)
        ((DrawerListener)this.mListeners.get(i)).onDrawerSlide(paramView, paramFloat);  
  }
  
  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong) {
    int i = getHeight();
    boolean bool1 = isContentView(paramView);
    int j = 0;
    int k = 0;
    int m = getWidth();
    int n = paramCanvas.save();
    int i1 = m;
    if (bool1) {
      int i2 = getChildCount();
      i1 = 0;
      j = k;
      while (i1 < i2) {
        View view = getChildAt(i1);
        k = j;
        int i3 = m;
        if (view != paramView) {
          k = j;
          i3 = m;
          if (view.getVisibility() == 0) {
            k = j;
            i3 = m;
            if (hasOpaqueBackground(view)) {
              k = j;
              i3 = m;
              if (isDrawerView(view))
                if (view.getHeight() < i) {
                  i3 = m;
                  k = j;
                } else if (checkDrawerViewAbsoluteGravity(view, 3)) {
                  int i4 = view.getRight();
                  k = j;
                  i3 = m;
                  if (i4 > j) {
                    k = i4;
                    i3 = m;
                  } 
                } else {
                  int i4 = view.getLeft();
                  k = j;
                  i3 = m;
                  if (i4 < m) {
                    i3 = i4;
                    k = j;
                  } 
                }  
            } 
          } 
        } 
        i1++;
        j = k;
        m = i3;
      } 
      paramCanvas.clipRect(j, 0, m, getHeight());
      i1 = m;
    } 
    boolean bool2 = super.drawChild(paramCanvas, paramView, paramLong);
    paramCanvas.restoreToCount(n);
    if (this.mScrimOpacity > 0.0F && bool1) {
      m = (int)(((this.mScrimColor & 0xFF000000) >>> 24) * this.mScrimOpacity);
      k = this.mScrimColor;
      this.mScrimPaint.setColor(m << 24 | k & 0xFFFFFF);
      paramCanvas.drawRect(j, 0.0F, i1, getHeight(), this.mScrimPaint);
      return bool2;
    } 
    if (this.mShadowLeftResolved != null && checkDrawerViewAbsoluteGravity(paramView, 3)) {
      i1 = this.mShadowLeftResolved.getIntrinsicWidth();
      m = paramView.getRight();
      j = this.mLeftDragger.getEdgeSize();
      float f = Math.max(0.0F, Math.min(m / j, 1.0F));
      this.mShadowLeftResolved.setBounds(m, paramView.getTop(), m + i1, paramView.getBottom());
      this.mShadowLeftResolved.setAlpha((int)(255.0F * f));
      this.mShadowLeftResolved.draw(paramCanvas);
      return bool2;
    } 
    if (this.mShadowRightResolved != null && checkDrawerViewAbsoluteGravity(paramView, 5)) {
      j = this.mShadowRightResolved.getIntrinsicWidth();
      i1 = paramView.getLeft();
      m = getWidth();
      k = this.mRightDragger.getEdgeSize();
      float f = Math.max(0.0F, Math.min((m - i1) / k, 1.0F));
      this.mShadowRightResolved.setBounds(i1 - j, paramView.getTop(), i1, paramView.getBottom());
      this.mShadowRightResolved.setAlpha((int)(255.0F * f));
      this.mShadowRightResolved.draw(paramCanvas);
    } 
    return bool2;
  }
  
  View findDrawerWithGravity(int paramInt) {
    // Byte code:
    //   0: iload_1
    //   1: aload_0
    //   2: invokestatic getLayoutDirection : (Landroid/view/View;)I
    //   5: invokestatic getAbsoluteGravity : (II)I
    //   8: istore_2
    //   9: aload_0
    //   10: invokevirtual getChildCount : ()I
    //   13: istore_3
    //   14: iconst_0
    //   15: istore_1
    //   16: iload_1
    //   17: iload_3
    //   18: if_icmpge -> 53
    //   21: aload_0
    //   22: iload_1
    //   23: invokevirtual getChildAt : (I)Landroid/view/View;
    //   26: astore #4
    //   28: aload_0
    //   29: aload #4
    //   31: invokevirtual getDrawerViewAbsoluteGravity : (Landroid/view/View;)I
    //   34: bipush #7
    //   36: iand
    //   37: iload_2
    //   38: bipush #7
    //   40: iand
    //   41: if_icmpne -> 47
    //   44: aload #4
    //   46: areturn
    //   47: iinc #1, 1
    //   50: goto -> 16
    //   53: aconst_null
    //   54: astore #4
    //   56: goto -> 44
  }
  
  View findOpenDrawer() {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getChildCount : ()I
    //   4: istore_1
    //   5: iconst_0
    //   6: istore_2
    //   7: iload_2
    //   8: iload_1
    //   9: if_icmpge -> 42
    //   12: aload_0
    //   13: iload_2
    //   14: invokevirtual getChildAt : (I)Landroid/view/View;
    //   17: astore_3
    //   18: aload_3
    //   19: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   22: checkcast android/support/v4/widget/DrawerLayout$LayoutParams
    //   25: getfield openState : I
    //   28: iconst_1
    //   29: iand
    //   30: iconst_1
    //   31: if_icmpne -> 36
    //   34: aload_3
    //   35: areturn
    //   36: iinc #2, 1
    //   39: goto -> 7
    //   42: aconst_null
    //   43: astore_3
    //   44: goto -> 34
  }
  
  View findVisibleDrawer() {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getChildCount : ()I
    //   4: istore_1
    //   5: iconst_0
    //   6: istore_2
    //   7: iload_2
    //   8: iload_1
    //   9: if_icmpge -> 42
    //   12: aload_0
    //   13: iload_2
    //   14: invokevirtual getChildAt : (I)Landroid/view/View;
    //   17: astore_3
    //   18: aload_0
    //   19: aload_3
    //   20: invokevirtual isDrawerView : (Landroid/view/View;)Z
    //   23: ifeq -> 36
    //   26: aload_0
    //   27: aload_3
    //   28: invokevirtual isDrawerVisible : (Landroid/view/View;)Z
    //   31: ifeq -> 36
    //   34: aload_3
    //   35: areturn
    //   36: iinc #2, 1
    //   39: goto -> 7
    //   42: aconst_null
    //   43: astore_3
    //   44: goto -> 34
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
    return (ViewGroup.LayoutParams)new LayoutParams(-1, -1);
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return (ViewGroup.LayoutParams)new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (ViewGroup.LayoutParams)((paramLayoutParams instanceof LayoutParams) ? new LayoutParams((LayoutParams)paramLayoutParams) : ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams) ? new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams) : new LayoutParams(paramLayoutParams)));
  }
  
  public float getDrawerElevation() {
    return SET_DRAWER_SHADOW_FROM_ELEVATION ? this.mDrawerElevation : 0.0F;
  }
  
  public int getDrawerLockMode(int paramInt) {
    int i = ViewCompat.getLayoutDirection((View)this);
    switch (paramInt) {
      default:
        return 0;
      case 3:
        if (this.mLockModeLeft != 3)
          return this.mLockModeLeft; 
        if (i == 0) {
          paramInt = this.mLockModeStart;
        } else {
          paramInt = this.mLockModeEnd;
        } 
        if (paramInt != 3)
          return paramInt; 
      case 5:
        if (this.mLockModeRight != 3)
          return this.mLockModeRight; 
        if (i == 0) {
          paramInt = this.mLockModeEnd;
        } else {
          paramInt = this.mLockModeStart;
        } 
        if (paramInt != 3)
          return paramInt; 
      case 8388611:
        if (this.mLockModeStart != 3)
          return this.mLockModeStart; 
        if (i == 0) {
          paramInt = this.mLockModeLeft;
        } else {
          paramInt = this.mLockModeRight;
        } 
        if (paramInt != 3)
          return paramInt; 
      case 8388613:
        break;
    } 
    if (this.mLockModeEnd != 3)
      return this.mLockModeEnd; 
    if (i == 0) {
      paramInt = this.mLockModeRight;
    } else {
      paramInt = this.mLockModeLeft;
    } 
    if (paramInt != 3)
      return paramInt; 
  }
  
  public int getDrawerLockMode(View paramView) {
    if (!isDrawerView(paramView))
      throw new IllegalArgumentException("View " + paramView + " is not a drawer"); 
    return getDrawerLockMode(((LayoutParams)paramView.getLayoutParams()).gravity);
  }
  
  @Nullable
  public CharSequence getDrawerTitle(int paramInt) {
    paramInt = GravityCompat.getAbsoluteGravity(paramInt, ViewCompat.getLayoutDirection((View)this));
    return (paramInt == 3) ? this.mTitleLeft : ((paramInt == 5) ? this.mTitleRight : null);
  }
  
  int getDrawerViewAbsoluteGravity(View paramView) {
    return GravityCompat.getAbsoluteGravity(((LayoutParams)paramView.getLayoutParams()).gravity, ViewCompat.getLayoutDirection((View)this));
  }
  
  float getDrawerViewOffset(View paramView) {
    return ((LayoutParams)paramView.getLayoutParams()).onScreen;
  }
  
  public Drawable getStatusBarBackgroundDrawable() {
    return this.mStatusBarBackground;
  }
  
  boolean isContentView(View paramView) {
    return (((LayoutParams)paramView.getLayoutParams()).gravity == 0);
  }
  
  public boolean isDrawerOpen(int paramInt) {
    View view = findDrawerWithGravity(paramInt);
    return (view != null) ? isDrawerOpen(view) : false;
  }
  
  public boolean isDrawerOpen(View paramView) {
    boolean bool = true;
    if (!isDrawerView(paramView))
      throw new IllegalArgumentException("View " + paramView + " is not a drawer"); 
    if ((((LayoutParams)paramView.getLayoutParams()).openState & 0x1) != 1)
      bool = false; 
    return bool;
  }
  
  boolean isDrawerView(View paramView) {
    int i = GravityCompat.getAbsoluteGravity(((LayoutParams)paramView.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(paramView));
    return ((i & 0x3) != 0) ? true : (((i & 0x5) != 0));
  }
  
  public boolean isDrawerVisible(int paramInt) {
    View view = findDrawerWithGravity(paramInt);
    return (view != null) ? isDrawerVisible(view) : false;
  }
  
  public boolean isDrawerVisible(View paramView) {
    if (!isDrawerView(paramView))
      throw new IllegalArgumentException("View " + paramView + " is not a drawer"); 
    return (((LayoutParams)paramView.getLayoutParams()).onScreen > 0.0F);
  }
  
  void moveDrawerToOffset(View paramView, float paramFloat) {
    float f = getDrawerViewOffset(paramView);
    int i = paramView.getWidth();
    int j = (int)(i * f);
    j = (int)(i * paramFloat) - j;
    if (!checkDrawerViewAbsoluteGravity(paramView, 3))
      j = -j; 
    paramView.offsetLeftAndRight(j);
    setDrawerViewOffset(paramView, paramFloat);
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    this.mFirstLayout = true;
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    this.mFirstLayout = true;
  }
  
  public void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    if (this.mDrawStatusBarBackground && this.mStatusBarBackground != null) {
      int i = IMPL.getTopInset(this.mLastInsets);
      if (i > 0) {
        this.mStatusBarBackground.setBounds(0, 0, getWidth(), i);
        this.mStatusBarBackground.draw(paramCanvas);
      } 
    } 
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: aload_1
    //   3: invokestatic getActionMasked : (Landroid/view/MotionEvent;)I
    //   6: istore_3
    //   7: aload_0
    //   8: getfield mLeftDragger : Landroid/support/v4/widget/ViewDragHelper;
    //   11: aload_1
    //   12: invokevirtual shouldInterceptTouchEvent : (Landroid/view/MotionEvent;)Z
    //   15: istore #4
    //   17: aload_0
    //   18: getfield mRightDragger : Landroid/support/v4/widget/ViewDragHelper;
    //   21: aload_1
    //   22: invokevirtual shouldInterceptTouchEvent : (Landroid/view/MotionEvent;)Z
    //   25: istore #5
    //   27: iconst_0
    //   28: istore #6
    //   30: iconst_0
    //   31: istore #7
    //   33: iload_3
    //   34: tableswitch default -> 64, 0 -> 97, 1 -> 214, 2 -> 180, 3 -> 214
    //   64: iload #7
    //   66: istore_3
    //   67: iload #4
    //   69: iload #5
    //   71: ior
    //   72: ifne -> 93
    //   75: iload_3
    //   76: ifne -> 93
    //   79: aload_0
    //   80: invokespecial hasPeekingDrawer : ()Z
    //   83: ifne -> 93
    //   86: aload_0
    //   87: getfield mChildrenCanceledTouch : Z
    //   90: ifeq -> 95
    //   93: iconst_1
    //   94: istore_2
    //   95: iload_2
    //   96: ireturn
    //   97: aload_1
    //   98: invokevirtual getX : ()F
    //   101: fstore #8
    //   103: aload_1
    //   104: invokevirtual getY : ()F
    //   107: fstore #9
    //   109: aload_0
    //   110: fload #8
    //   112: putfield mInitialMotionX : F
    //   115: aload_0
    //   116: fload #9
    //   118: putfield mInitialMotionY : F
    //   121: iload #6
    //   123: istore_3
    //   124: aload_0
    //   125: getfield mScrimOpacity : F
    //   128: fconst_0
    //   129: fcmpl
    //   130: ifle -> 167
    //   133: aload_0
    //   134: getfield mLeftDragger : Landroid/support/v4/widget/ViewDragHelper;
    //   137: fload #8
    //   139: f2i
    //   140: fload #9
    //   142: f2i
    //   143: invokevirtual findTopChildUnder : (II)Landroid/view/View;
    //   146: astore_1
    //   147: iload #6
    //   149: istore_3
    //   150: aload_1
    //   151: ifnull -> 167
    //   154: iload #6
    //   156: istore_3
    //   157: aload_0
    //   158: aload_1
    //   159: invokevirtual isContentView : (Landroid/view/View;)Z
    //   162: ifeq -> 167
    //   165: iconst_1
    //   166: istore_3
    //   167: aload_0
    //   168: iconst_0
    //   169: putfield mDisallowInterceptRequested : Z
    //   172: aload_0
    //   173: iconst_0
    //   174: putfield mChildrenCanceledTouch : Z
    //   177: goto -> 67
    //   180: iload #7
    //   182: istore_3
    //   183: aload_0
    //   184: getfield mLeftDragger : Landroid/support/v4/widget/ViewDragHelper;
    //   187: iconst_3
    //   188: invokevirtual checkTouchSlop : (I)Z
    //   191: ifeq -> 67
    //   194: aload_0
    //   195: getfield mLeftCallback : Landroid/support/v4/widget/DrawerLayout$ViewDragCallback;
    //   198: invokevirtual removeCallbacks : ()V
    //   201: aload_0
    //   202: getfield mRightCallback : Landroid/support/v4/widget/DrawerLayout$ViewDragCallback;
    //   205: invokevirtual removeCallbacks : ()V
    //   208: iload #7
    //   210: istore_3
    //   211: goto -> 67
    //   214: aload_0
    //   215: iconst_1
    //   216: invokevirtual closeDrawers : (Z)V
    //   219: aload_0
    //   220: iconst_0
    //   221: putfield mDisallowInterceptRequested : Z
    //   224: aload_0
    //   225: iconst_0
    //   226: putfield mChildrenCanceledTouch : Z
    //   229: iload #7
    //   231: istore_3
    //   232: goto -> 67
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    if (paramInt == 4 && hasVisibleDrawer()) {
      paramKeyEvent.startTracking();
      return true;
    } 
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
    View view;
    if (paramInt == 4) {
      view = findVisibleDrawer();
      if (view != null && getDrawerLockMode(view) == 0)
        closeDrawers(); 
      return (view != null);
    } 
    return super.onKeyUp(paramInt, (KeyEvent)view);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    // Byte code:
    //   0: aload_0
    //   1: iconst_1
    //   2: putfield mInLayout : Z
    //   5: iload #4
    //   7: iload_2
    //   8: isub
    //   9: istore #6
    //   11: aload_0
    //   12: invokevirtual getChildCount : ()I
    //   15: istore #7
    //   17: iconst_0
    //   18: istore #4
    //   20: iload #4
    //   22: iload #7
    //   24: if_icmpge -> 446
    //   27: aload_0
    //   28: iload #4
    //   30: invokevirtual getChildAt : (I)Landroid/view/View;
    //   33: astore #8
    //   35: aload #8
    //   37: invokevirtual getVisibility : ()I
    //   40: bipush #8
    //   42: if_icmpne -> 51
    //   45: iinc #4, 1
    //   48: goto -> 20
    //   51: aload #8
    //   53: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   56: checkcast android/support/v4/widget/DrawerLayout$LayoutParams
    //   59: astore #9
    //   61: aload_0
    //   62: aload #8
    //   64: invokevirtual isContentView : (Landroid/view/View;)Z
    //   67: ifeq -> 110
    //   70: aload #8
    //   72: aload #9
    //   74: getfield leftMargin : I
    //   77: aload #9
    //   79: getfield topMargin : I
    //   82: aload #9
    //   84: getfield leftMargin : I
    //   87: aload #8
    //   89: invokevirtual getMeasuredWidth : ()I
    //   92: iadd
    //   93: aload #9
    //   95: getfield topMargin : I
    //   98: aload #8
    //   100: invokevirtual getMeasuredHeight : ()I
    //   103: iadd
    //   104: invokevirtual layout : (IIII)V
    //   107: goto -> 45
    //   110: aload #8
    //   112: invokevirtual getMeasuredWidth : ()I
    //   115: istore #10
    //   117: aload #8
    //   119: invokevirtual getMeasuredHeight : ()I
    //   122: istore #11
    //   124: aload_0
    //   125: aload #8
    //   127: iconst_3
    //   128: invokevirtual checkDrawerViewAbsoluteGravity : (Landroid/view/View;I)Z
    //   131: ifeq -> 280
    //   134: iload #10
    //   136: ineg
    //   137: iload #10
    //   139: i2f
    //   140: aload #9
    //   142: getfield onScreen : F
    //   145: fmul
    //   146: f2i
    //   147: iadd
    //   148: istore #12
    //   150: iload #10
    //   152: iload #12
    //   154: iadd
    //   155: i2f
    //   156: iload #10
    //   158: i2f
    //   159: fdiv
    //   160: fstore #13
    //   162: fload #13
    //   164: aload #9
    //   166: getfield onScreen : F
    //   169: fcmpl
    //   170: ifeq -> 310
    //   173: iconst_1
    //   174: istore #14
    //   176: aload #9
    //   178: getfield gravity : I
    //   181: bipush #112
    //   183: iand
    //   184: lookupswitch default -> 212, 16 -> 356, 80 -> 316
    //   212: aload #8
    //   214: iload #12
    //   216: aload #9
    //   218: getfield topMargin : I
    //   221: iload #12
    //   223: iload #10
    //   225: iadd
    //   226: aload #9
    //   228: getfield topMargin : I
    //   231: iload #11
    //   233: iadd
    //   234: invokevirtual layout : (IIII)V
    //   237: iload #14
    //   239: ifeq -> 250
    //   242: aload_0
    //   243: aload #8
    //   245: fload #13
    //   247: invokevirtual setDrawerViewOffset : (Landroid/view/View;F)V
    //   250: aload #9
    //   252: getfield onScreen : F
    //   255: fconst_0
    //   256: fcmpl
    //   257: ifle -> 441
    //   260: iconst_0
    //   261: istore_2
    //   262: aload #8
    //   264: invokevirtual getVisibility : ()I
    //   267: iload_2
    //   268: if_icmpeq -> 45
    //   271: aload #8
    //   273: iload_2
    //   274: invokevirtual setVisibility : (I)V
    //   277: goto -> 45
    //   280: iload #6
    //   282: iload #10
    //   284: i2f
    //   285: aload #9
    //   287: getfield onScreen : F
    //   290: fmul
    //   291: f2i
    //   292: isub
    //   293: istore #12
    //   295: iload #6
    //   297: iload #12
    //   299: isub
    //   300: i2f
    //   301: iload #10
    //   303: i2f
    //   304: fdiv
    //   305: fstore #13
    //   307: goto -> 162
    //   310: iconst_0
    //   311: istore #14
    //   313: goto -> 176
    //   316: iload #5
    //   318: iload_3
    //   319: isub
    //   320: istore_2
    //   321: aload #8
    //   323: iload #12
    //   325: iload_2
    //   326: aload #9
    //   328: getfield bottomMargin : I
    //   331: isub
    //   332: aload #8
    //   334: invokevirtual getMeasuredHeight : ()I
    //   337: isub
    //   338: iload #12
    //   340: iload #10
    //   342: iadd
    //   343: iload_2
    //   344: aload #9
    //   346: getfield bottomMargin : I
    //   349: isub
    //   350: invokevirtual layout : (IIII)V
    //   353: goto -> 237
    //   356: iload #5
    //   358: iload_3
    //   359: isub
    //   360: istore #15
    //   362: iload #15
    //   364: iload #11
    //   366: isub
    //   367: iconst_2
    //   368: idiv
    //   369: istore #16
    //   371: iload #16
    //   373: aload #9
    //   375: getfield topMargin : I
    //   378: if_icmpge -> 407
    //   381: aload #9
    //   383: getfield topMargin : I
    //   386: istore_2
    //   387: aload #8
    //   389: iload #12
    //   391: iload_2
    //   392: iload #12
    //   394: iload #10
    //   396: iadd
    //   397: iload_2
    //   398: iload #11
    //   400: iadd
    //   401: invokevirtual layout : (IIII)V
    //   404: goto -> 237
    //   407: iload #16
    //   409: istore_2
    //   410: iload #16
    //   412: iload #11
    //   414: iadd
    //   415: iload #15
    //   417: aload #9
    //   419: getfield bottomMargin : I
    //   422: isub
    //   423: if_icmple -> 387
    //   426: iload #15
    //   428: aload #9
    //   430: getfield bottomMargin : I
    //   433: isub
    //   434: iload #11
    //   436: isub
    //   437: istore_2
    //   438: goto -> 387
    //   441: iconst_4
    //   442: istore_2
    //   443: goto -> 262
    //   446: aload_0
    //   447: iconst_0
    //   448: putfield mInLayout : Z
    //   451: aload_0
    //   452: iconst_0
    //   453: putfield mFirstLayout : Z
    //   456: return
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iload_1
    //   1: invokestatic getMode : (I)I
    //   4: istore_3
    //   5: iload_2
    //   6: invokestatic getMode : (I)I
    //   9: istore #4
    //   11: iload_1
    //   12: invokestatic getSize : (I)I
    //   15: istore #5
    //   17: iload_2
    //   18: invokestatic getSize : (I)I
    //   21: istore #6
    //   23: iload_3
    //   24: ldc_w 1073741824
    //   27: if_icmpne -> 46
    //   30: iload #6
    //   32: istore #7
    //   34: iload #5
    //   36: istore #8
    //   38: iload #4
    //   40: ldc_w 1073741824
    //   43: if_icmpeq -> 76
    //   46: aload_0
    //   47: invokevirtual isInEditMode : ()Z
    //   50: ifeq -> 187
    //   53: iload_3
    //   54: ldc_w -2147483648
    //   57: if_icmpne -> 150
    //   60: iload #4
    //   62: ldc_w -2147483648
    //   65: if_icmpne -> 162
    //   68: iload #5
    //   70: istore #8
    //   72: iload #6
    //   74: istore #7
    //   76: aload_0
    //   77: iload #8
    //   79: iload #7
    //   81: invokevirtual setMeasuredDimension : (II)V
    //   84: aload_0
    //   85: getfield mLastInsets : Ljava/lang/Object;
    //   88: ifnull -> 198
    //   91: aload_0
    //   92: invokestatic getFitsSystemWindows : (Landroid/view/View;)Z
    //   95: ifeq -> 198
    //   98: iconst_1
    //   99: istore #4
    //   101: aload_0
    //   102: invokestatic getLayoutDirection : (Landroid/view/View;)I
    //   105: istore #9
    //   107: iconst_0
    //   108: istore #6
    //   110: iconst_0
    //   111: istore #5
    //   113: aload_0
    //   114: invokevirtual getChildCount : ()I
    //   117: istore #10
    //   119: iconst_0
    //   120: istore_3
    //   121: iload_3
    //   122: iload #10
    //   124: if_icmpge -> 587
    //   127: aload_0
    //   128: iload_3
    //   129: invokevirtual getChildAt : (I)Landroid/view/View;
    //   132: astore #11
    //   134: aload #11
    //   136: invokevirtual getVisibility : ()I
    //   139: bipush #8
    //   141: if_icmpne -> 204
    //   144: iinc #3, 1
    //   147: goto -> 121
    //   150: iload_3
    //   151: ifne -> 60
    //   154: sipush #300
    //   157: istore #5
    //   159: goto -> 60
    //   162: iload #6
    //   164: istore #7
    //   166: iload #5
    //   168: istore #8
    //   170: iload #4
    //   172: ifne -> 76
    //   175: sipush #300
    //   178: istore #7
    //   180: iload #5
    //   182: istore #8
    //   184: goto -> 76
    //   187: new java/lang/IllegalArgumentException
    //   190: dup
    //   191: ldc_w 'DrawerLayout must be measured with MeasureSpec.EXACTLY.'
    //   194: invokespecial <init> : (Ljava/lang/String;)V
    //   197: athrow
    //   198: iconst_0
    //   199: istore #4
    //   201: goto -> 101
    //   204: aload #11
    //   206: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   209: checkcast android/support/v4/widget/DrawerLayout$LayoutParams
    //   212: astore #12
    //   214: iload #4
    //   216: ifeq -> 255
    //   219: aload #12
    //   221: getfield gravity : I
    //   224: iload #9
    //   226: invokestatic getAbsoluteGravity : (II)I
    //   229: istore #13
    //   231: aload #11
    //   233: invokestatic getFitsSystemWindows : (Landroid/view/View;)Z
    //   236: ifeq -> 312
    //   239: getstatic android/support/v4/widget/DrawerLayout.IMPL : Landroid/support/v4/widget/DrawerLayout$DrawerLayoutCompatImpl;
    //   242: aload #11
    //   244: aload_0
    //   245: getfield mLastInsets : Ljava/lang/Object;
    //   248: iload #13
    //   250: invokeinterface dispatchChildInsets : (Landroid/view/View;Ljava/lang/Object;I)V
    //   255: aload_0
    //   256: aload #11
    //   258: invokevirtual isContentView : (Landroid/view/View;)Z
    //   261: ifeq -> 331
    //   264: aload #11
    //   266: iload #8
    //   268: aload #12
    //   270: getfield leftMargin : I
    //   273: isub
    //   274: aload #12
    //   276: getfield rightMargin : I
    //   279: isub
    //   280: ldc_w 1073741824
    //   283: invokestatic makeMeasureSpec : (II)I
    //   286: iload #7
    //   288: aload #12
    //   290: getfield topMargin : I
    //   293: isub
    //   294: aload #12
    //   296: getfield bottomMargin : I
    //   299: isub
    //   300: ldc_w 1073741824
    //   303: invokestatic makeMeasureSpec : (II)I
    //   306: invokevirtual measure : (II)V
    //   309: goto -> 144
    //   312: getstatic android/support/v4/widget/DrawerLayout.IMPL : Landroid/support/v4/widget/DrawerLayout$DrawerLayoutCompatImpl;
    //   315: aload #12
    //   317: aload_0
    //   318: getfield mLastInsets : Ljava/lang/Object;
    //   321: iload #13
    //   323: invokeinterface applyMarginInsets : (Landroid/view/ViewGroup$MarginLayoutParams;Ljava/lang/Object;I)V
    //   328: goto -> 255
    //   331: aload_0
    //   332: aload #11
    //   334: invokevirtual isDrawerView : (Landroid/view/View;)Z
    //   337: ifeq -> 536
    //   340: getstatic android/support/v4/widget/DrawerLayout.SET_DRAWER_SHADOW_FROM_ELEVATION : Z
    //   343: ifeq -> 368
    //   346: aload #11
    //   348: invokestatic getElevation : (Landroid/view/View;)F
    //   351: aload_0
    //   352: getfield mDrawerElevation : F
    //   355: fcmpl
    //   356: ifeq -> 368
    //   359: aload #11
    //   361: aload_0
    //   362: getfield mDrawerElevation : F
    //   365: invokestatic setElevation : (Landroid/view/View;F)V
    //   368: aload_0
    //   369: aload #11
    //   371: invokevirtual getDrawerViewAbsoluteGravity : (Landroid/view/View;)I
    //   374: bipush #7
    //   376: iand
    //   377: istore #14
    //   379: iload #14
    //   381: iconst_3
    //   382: if_icmpne -> 463
    //   385: iconst_1
    //   386: istore #13
    //   388: iload #13
    //   390: ifeq -> 398
    //   393: iload #6
    //   395: ifne -> 408
    //   398: iload #13
    //   400: ifne -> 469
    //   403: iload #5
    //   405: ifeq -> 469
    //   408: new java/lang/IllegalStateException
    //   411: dup
    //   412: new java/lang/StringBuilder
    //   415: dup
    //   416: invokespecial <init> : ()V
    //   419: ldc_w 'Child drawer has absolute gravity '
    //   422: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   425: iload #14
    //   427: invokestatic gravityToString : (I)Ljava/lang/String;
    //   430: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   433: ldc_w ' but this '
    //   436: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   439: ldc 'DrawerLayout'
    //   441: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   444: ldc_w ' already has a '
    //   447: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   450: ldc_w 'drawer view along that edge'
    //   453: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   456: invokevirtual toString : ()Ljava/lang/String;
    //   459: invokespecial <init> : (Ljava/lang/String;)V
    //   462: athrow
    //   463: iconst_0
    //   464: istore #13
    //   466: goto -> 388
    //   469: iload #13
    //   471: ifeq -> 530
    //   474: iconst_1
    //   475: istore #6
    //   477: aload #11
    //   479: iload_1
    //   480: aload_0
    //   481: getfield mMinDrawerMargin : I
    //   484: aload #12
    //   486: getfield leftMargin : I
    //   489: iadd
    //   490: aload #12
    //   492: getfield rightMargin : I
    //   495: iadd
    //   496: aload #12
    //   498: getfield width : I
    //   501: invokestatic getChildMeasureSpec : (III)I
    //   504: iload_2
    //   505: aload #12
    //   507: getfield topMargin : I
    //   510: aload #12
    //   512: getfield bottomMargin : I
    //   515: iadd
    //   516: aload #12
    //   518: getfield height : I
    //   521: invokestatic getChildMeasureSpec : (III)I
    //   524: invokevirtual measure : (II)V
    //   527: goto -> 144
    //   530: iconst_1
    //   531: istore #5
    //   533: goto -> 477
    //   536: new java/lang/IllegalStateException
    //   539: dup
    //   540: new java/lang/StringBuilder
    //   543: dup
    //   544: invokespecial <init> : ()V
    //   547: ldc_w 'Child '
    //   550: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   553: aload #11
    //   555: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   558: ldc_w ' at index '
    //   561: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   564: iload_3
    //   565: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   568: ldc_w ' does not have a valid layout_gravity - must be Gravity.LEFT, '
    //   571: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   574: ldc_w 'Gravity.RIGHT or Gravity.NO_GRAVITY'
    //   577: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   580: invokevirtual toString : ()Ljava/lang/String;
    //   583: invokespecial <init> : (Ljava/lang/String;)V
    //   586: athrow
    //   587: return
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    if (savedState.openDrawerGravity != 0) {
      View view = findDrawerWithGravity(savedState.openDrawerGravity);
      if (view != null)
        openDrawer(view); 
    } 
    if (savedState.lockModeLeft != 3)
      setDrawerLockMode(savedState.lockModeLeft, 3); 
    if (savedState.lockModeRight != 3)
      setDrawerLockMode(savedState.lockModeRight, 5); 
    if (savedState.lockModeStart != 3)
      setDrawerLockMode(savedState.lockModeStart, 8388611); 
    if (savedState.lockModeEnd != 3)
      setDrawerLockMode(savedState.lockModeEnd, 8388613); 
  }
  
  public void onRtlPropertiesChanged(int paramInt) {
    resolveShadowDrawables();
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    int i = getChildCount();
    for (byte b = 0;; b++) {
      if (b < i) {
        boolean bool1;
        boolean bool2;
        LayoutParams layoutParams = (LayoutParams)getChildAt(b).getLayoutParams();
        if (layoutParams.openState == 1) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        if (layoutParams.openState == 2) {
          bool2 = true;
        } else {
          bool2 = false;
        } 
        if (bool1 || bool2) {
          savedState.openDrawerGravity = layoutParams.gravity;
          savedState.lockModeLeft = this.mLockModeLeft;
          savedState.lockModeRight = this.mLockModeRight;
          savedState.lockModeStart = this.mLockModeStart;
          savedState.lockModeEnd = this.mLockModeEnd;
          return (Parcelable)savedState;
        } 
      } else {
        savedState.lockModeLeft = this.mLockModeLeft;
        savedState.lockModeRight = this.mLockModeRight;
        savedState.lockModeStart = this.mLockModeStart;
        savedState.lockModeEnd = this.mLockModeEnd;
        return (Parcelable)savedState;
      } 
    } 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    View view;
    float f1;
    float f2;
    boolean bool1;
    boolean bool2;
    this.mLeftDragger.processTouchEvent(paramMotionEvent);
    this.mRightDragger.processTouchEvent(paramMotionEvent);
    switch (paramMotionEvent.getAction() & 0xFF) {
      default:
        return true;
      case 0:
        f1 = paramMotionEvent.getX();
        f2 = paramMotionEvent.getY();
        this.mInitialMotionX = f1;
        this.mInitialMotionY = f2;
        this.mDisallowInterceptRequested = false;
        this.mChildrenCanceledTouch = false;
      case 1:
        f2 = paramMotionEvent.getX();
        f1 = paramMotionEvent.getY();
        bool1 = true;
        view = this.mLeftDragger.findTopChildUnder((int)f2, (int)f1);
        bool2 = bool1;
        if (view != null) {
          bool2 = bool1;
          if (isContentView(view)) {
            f2 -= this.mInitialMotionX;
            f1 -= this.mInitialMotionY;
            int i = this.mLeftDragger.getTouchSlop();
            bool2 = bool1;
            if (f2 * f2 + f1 * f1 < (i * i)) {
              view = findOpenDrawer();
              bool2 = bool1;
              if (view != null)
                if (getDrawerLockMode(view) == 2) {
                  bool2 = true;
                } else {
                  bool2 = false;
                }  
            } 
          } 
        } 
        closeDrawers(bool2);
        this.mDisallowInterceptRequested = false;
      case 3:
        break;
    } 
    closeDrawers(true);
    this.mDisallowInterceptRequested = false;
    this.mChildrenCanceledTouch = false;
  }
  
  public void openDrawer(int paramInt) {
    openDrawer(paramInt, true);
  }
  
  public void openDrawer(int paramInt, boolean paramBoolean) {
    View view = findDrawerWithGravity(paramInt);
    if (view == null)
      throw new IllegalArgumentException("No drawer view found with gravity " + gravityToString(paramInt)); 
    openDrawer(view, paramBoolean);
  }
  
  public void openDrawer(View paramView) {
    openDrawer(paramView, true);
  }
  
  public void openDrawer(View paramView, boolean paramBoolean) {
    if (!isDrawerView(paramView))
      throw new IllegalArgumentException("View " + paramView + " is not a sliding drawer"); 
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (this.mFirstLayout) {
      layoutParams.onScreen = 1.0F;
      layoutParams.openState = 1;
      updateChildrenImportantForAccessibility(paramView, true);
    } else if (paramBoolean) {
      layoutParams.openState |= 0x2;
      if (checkDrawerViewAbsoluteGravity(paramView, 3)) {
        this.mLeftDragger.smoothSlideViewTo(paramView, 0, paramView.getTop());
      } else {
        this.mRightDragger.smoothSlideViewTo(paramView, getWidth() - paramView.getWidth(), paramView.getTop());
      } 
    } else {
      moveDrawerToOffset(paramView, 1.0F);
      updateDrawerState(layoutParams.gravity, 0, paramView);
      paramView.setVisibility(0);
    } 
    invalidate();
  }
  
  public void removeDrawerListener(@NonNull DrawerListener paramDrawerListener) {
    if (paramDrawerListener != null && this.mListeners != null)
      this.mListeners.remove(paramDrawerListener); 
  }
  
  public void requestDisallowInterceptTouchEvent(boolean paramBoolean) {
    super.requestDisallowInterceptTouchEvent(paramBoolean);
    this.mDisallowInterceptRequested = paramBoolean;
    if (paramBoolean)
      closeDrawers(true); 
  }
  
  public void requestLayout() {
    if (!this.mInLayout)
      super.requestLayout(); 
  }
  
  public void setChildInsets(Object paramObject, boolean paramBoolean) {
    this.mLastInsets = paramObject;
    this.mDrawStatusBarBackground = paramBoolean;
    if (!paramBoolean && getBackground() == null) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    } 
    setWillNotDraw(paramBoolean);
    requestLayout();
  }
  
  public void setDrawerElevation(float paramFloat) {
    this.mDrawerElevation = paramFloat;
    for (byte b = 0; b < getChildCount(); b++) {
      View view = getChildAt(b);
      if (isDrawerView(view))
        ViewCompat.setElevation(view, this.mDrawerElevation); 
    } 
  }
  
  @Deprecated
  public void setDrawerListener(DrawerListener paramDrawerListener) {
    if (this.mListener != null)
      removeDrawerListener(this.mListener); 
    if (paramDrawerListener != null)
      addDrawerListener(paramDrawerListener); 
    this.mListener = paramDrawerListener;
  }
  
  public void setDrawerLockMode(int paramInt) {
    setDrawerLockMode(paramInt, 3);
    setDrawerLockMode(paramInt, 5);
  }
  
  public void setDrawerLockMode(int paramInt1, int paramInt2) {
    int i = GravityCompat.getAbsoluteGravity(paramInt2, ViewCompat.getLayoutDirection((View)this));
    switch (paramInt2) {
      default:
        if (paramInt1 != 0) {
          ViewDragHelper viewDragHelper;
          if (i == 3) {
            viewDragHelper = this.mLeftDragger;
          } else {
            viewDragHelper = this.mRightDragger;
          } 
          viewDragHelper.cancel();
        } 
        switch (paramInt1) {
          default:
            return;
          case 2:
            view = findDrawerWithGravity(i);
            if (view != null)
              openDrawer(view); 
          case 1:
            break;
        } 
        break;
      case 3:
        this.mLockModeLeft = paramInt1;
      case 5:
        this.mLockModeRight = paramInt1;
      case 8388611:
        this.mLockModeStart = paramInt1;
      case 8388613:
        this.mLockModeEnd = paramInt1;
    } 
    View view = findDrawerWithGravity(i);
    if (view != null)
      closeDrawer(view); 
  }
  
  public void setDrawerLockMode(int paramInt, View paramView) {
    if (!isDrawerView(paramView))
      throw new IllegalArgumentException("View " + paramView + " is not a " + "drawer with appropriate layout_gravity"); 
    setDrawerLockMode(paramInt, ((LayoutParams)paramView.getLayoutParams()).gravity);
  }
  
  public void setDrawerShadow(@DrawableRes int paramInt1, int paramInt2) {
    setDrawerShadow(ContextCompat.getDrawable(getContext(), paramInt1), paramInt2);
  }
  
  public void setDrawerShadow(Drawable paramDrawable, int paramInt) {
    if (!SET_DRAWER_SHADOW_FROM_ELEVATION) {
      if ((paramInt & 0x800003) == 8388611) {
        this.mShadowStart = paramDrawable;
      } else if ((paramInt & 0x800005) == 8388613) {
        this.mShadowEnd = paramDrawable;
      } else if ((paramInt & 0x3) == 3) {
        this.mShadowLeft = paramDrawable;
      } else if ((paramInt & 0x5) == 5) {
        this.mShadowRight = paramDrawable;
      } else {
        return;
      } 
      resolveShadowDrawables();
      invalidate();
    } 
  }
  
  public void setDrawerTitle(int paramInt, CharSequence paramCharSequence) {
    paramInt = GravityCompat.getAbsoluteGravity(paramInt, ViewCompat.getLayoutDirection((View)this));
    if (paramInt == 3) {
      this.mTitleLeft = paramCharSequence;
      return;
    } 
    if (paramInt == 5)
      this.mTitleRight = paramCharSequence; 
  }
  
  void setDrawerViewOffset(View paramView, float paramFloat) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (paramFloat != layoutParams.onScreen) {
      layoutParams.onScreen = paramFloat;
      dispatchOnDrawerSlide(paramView, paramFloat);
    } 
  }
  
  public void setScrimColor(@ColorInt int paramInt) {
    this.mScrimColor = paramInt;
    invalidate();
  }
  
  public void setStatusBarBackground(int paramInt) {
    Drawable drawable;
    if (paramInt != 0) {
      drawable = ContextCompat.getDrawable(getContext(), paramInt);
    } else {
      drawable = null;
    } 
    this.mStatusBarBackground = drawable;
    invalidate();
  }
  
  public void setStatusBarBackground(Drawable paramDrawable) {
    this.mStatusBarBackground = paramDrawable;
    invalidate();
  }
  
  public void setStatusBarBackgroundColor(@ColorInt int paramInt) {
    this.mStatusBarBackground = (Drawable)new ColorDrawable(paramInt);
    invalidate();
  }
  
  void updateDrawerState(int paramInt1, int paramInt2, View paramView) {
    int i = this.mLeftDragger.getViewDragState();
    paramInt1 = this.mRightDragger.getViewDragState();
    if (i == 1 || paramInt1 == 1) {
      paramInt1 = 1;
    } else if (i == 2 || paramInt1 == 2) {
      paramInt1 = 2;
    } else {
      paramInt1 = 0;
    } 
    if (paramView != null && paramInt2 == 0) {
      LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
      if (layoutParams.onScreen == 0.0F) {
        dispatchOnDrawerClosed(paramView);
      } else if (layoutParams.onScreen == 1.0F) {
        dispatchOnDrawerOpened(paramView);
      } 
    } 
    if (paramInt1 != this.mDrawerState) {
      this.mDrawerState = paramInt1;
      if (this.mListeners != null)
        for (paramInt2 = this.mListeners.size() - 1; paramInt2 >= 0; paramInt2--)
          ((DrawerListener)this.mListeners.get(paramInt2)).onDrawerStateChanged(paramInt1);  
    } 
  }
  
  static {
    boolean bool2;
    boolean bool1 = true;
  }
  
  class AccessibilityDelegate extends AccessibilityDelegateCompat {
    private final Rect mTmpRect = new Rect();
    
    private void addChildrenForAccessibility(AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat, ViewGroup param1ViewGroup) {
      int i = param1ViewGroup.getChildCount();
      for (byte b = 0; b < i; b++) {
        View view = param1ViewGroup.getChildAt(b);
        if (DrawerLayout.includeChildForAccessibility(view))
          param1AccessibilityNodeInfoCompat.addChild(view); 
      } 
    }
    
    private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat1, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat2) {
      Rect rect = this.mTmpRect;
      param1AccessibilityNodeInfoCompat2.getBoundsInParent(rect);
      param1AccessibilityNodeInfoCompat1.setBoundsInParent(rect);
      param1AccessibilityNodeInfoCompat2.getBoundsInScreen(rect);
      param1AccessibilityNodeInfoCompat1.setBoundsInScreen(rect);
      param1AccessibilityNodeInfoCompat1.setVisibleToUser(param1AccessibilityNodeInfoCompat2.isVisibleToUser());
      param1AccessibilityNodeInfoCompat1.setPackageName(param1AccessibilityNodeInfoCompat2.getPackageName());
      param1AccessibilityNodeInfoCompat1.setClassName(param1AccessibilityNodeInfoCompat2.getClassName());
      param1AccessibilityNodeInfoCompat1.setContentDescription(param1AccessibilityNodeInfoCompat2.getContentDescription());
      param1AccessibilityNodeInfoCompat1.setEnabled(param1AccessibilityNodeInfoCompat2.isEnabled());
      param1AccessibilityNodeInfoCompat1.setClickable(param1AccessibilityNodeInfoCompat2.isClickable());
      param1AccessibilityNodeInfoCompat1.setFocusable(param1AccessibilityNodeInfoCompat2.isFocusable());
      param1AccessibilityNodeInfoCompat1.setFocused(param1AccessibilityNodeInfoCompat2.isFocused());
      param1AccessibilityNodeInfoCompat1.setAccessibilityFocused(param1AccessibilityNodeInfoCompat2.isAccessibilityFocused());
      param1AccessibilityNodeInfoCompat1.setSelected(param1AccessibilityNodeInfoCompat2.isSelected());
      param1AccessibilityNodeInfoCompat1.setLongClickable(param1AccessibilityNodeInfoCompat2.isLongClickable());
      param1AccessibilityNodeInfoCompat1.addAction(param1AccessibilityNodeInfoCompat2.getActions());
    }
    
    public boolean dispatchPopulateAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      List<CharSequence> list;
      CharSequence charSequence;
      if (param1AccessibilityEvent.getEventType() == 32) {
        list = param1AccessibilityEvent.getText();
        View view = DrawerLayout.this.findVisibleDrawer();
        if (view != null) {
          int i = DrawerLayout.this.getDrawerViewAbsoluteGravity(view);
          charSequence = DrawerLayout.this.getDrawerTitle(i);
          if (charSequence != null)
            list.add(charSequence); 
        } 
        return true;
      } 
      return super.dispatchPopulateAccessibilityEvent((View)list, (AccessibilityEvent)charSequence);
    }
    
    public void onInitializeAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      super.onInitializeAccessibilityEvent(param1View, param1AccessibilityEvent);
      param1AccessibilityEvent.setClassName(DrawerLayout.class.getName());
    }
    
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      if (DrawerLayout.CAN_HIDE_DESCENDANTS) {
        super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
      } else {
        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.obtain(param1AccessibilityNodeInfoCompat);
        super.onInitializeAccessibilityNodeInfo(param1View, accessibilityNodeInfoCompat);
        param1AccessibilityNodeInfoCompat.setSource(param1View);
        ViewParent viewParent = ViewCompat.getParentForAccessibility(param1View);
        if (viewParent instanceof View)
          param1AccessibilityNodeInfoCompat.setParent((View)viewParent); 
        copyNodeInfoNoChildren(param1AccessibilityNodeInfoCompat, accessibilityNodeInfoCompat);
        accessibilityNodeInfoCompat.recycle();
        addChildrenForAccessibility(param1AccessibilityNodeInfoCompat, (ViewGroup)param1View);
      } 
      param1AccessibilityNodeInfoCompat.setClassName(DrawerLayout.class.getName());
      param1AccessibilityNodeInfoCompat.setFocusable(false);
      param1AccessibilityNodeInfoCompat.setFocused(false);
      param1AccessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_FOCUS);
      param1AccessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLEAR_FOCUS);
    }
    
    public boolean onRequestSendAccessibilityEvent(ViewGroup param1ViewGroup, View param1View, AccessibilityEvent param1AccessibilityEvent) {
      return (DrawerLayout.CAN_HIDE_DESCENDANTS || DrawerLayout.includeChildForAccessibility(param1View)) ? super.onRequestSendAccessibilityEvent(param1ViewGroup, param1View, param1AccessibilityEvent) : false;
    }
  }
  
  final class ChildAccessibilityDelegate extends AccessibilityDelegateCompat {
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
      if (!DrawerLayout.includeChildForAccessibility(param1View))
        param1AccessibilityNodeInfoCompat.setParent(null); 
    }
  }
  
  static interface DrawerLayoutCompatImpl {
    void applyMarginInsets(ViewGroup.MarginLayoutParams param1MarginLayoutParams, Object param1Object, int param1Int);
    
    void configureApplyInsets(View param1View);
    
    void dispatchChildInsets(View param1View, Object param1Object, int param1Int);
    
    Drawable getDefaultStatusBarBackground(Context param1Context);
    
    int getTopInset(Object param1Object);
  }
  
  static class DrawerLayoutCompatImplApi21 implements DrawerLayoutCompatImpl {
    public void applyMarginInsets(ViewGroup.MarginLayoutParams param1MarginLayoutParams, Object param1Object, int param1Int) {
      DrawerLayoutCompatApi21.applyMarginInsets(param1MarginLayoutParams, param1Object, param1Int);
    }
    
    public void configureApplyInsets(View param1View) {
      DrawerLayoutCompatApi21.configureApplyInsets(param1View);
    }
    
    public void dispatchChildInsets(View param1View, Object param1Object, int param1Int) {
      DrawerLayoutCompatApi21.dispatchChildInsets(param1View, param1Object, param1Int);
    }
    
    public Drawable getDefaultStatusBarBackground(Context param1Context) {
      return DrawerLayoutCompatApi21.getDefaultStatusBarBackground(param1Context);
    }
    
    public int getTopInset(Object param1Object) {
      return DrawerLayoutCompatApi21.getTopInset(param1Object);
    }
  }
  
  static class DrawerLayoutCompatImplBase implements DrawerLayoutCompatImpl {
    public void applyMarginInsets(ViewGroup.MarginLayoutParams param1MarginLayoutParams, Object param1Object, int param1Int) {}
    
    public void configureApplyInsets(View param1View) {}
    
    public void dispatchChildInsets(View param1View, Object param1Object, int param1Int) {}
    
    public Drawable getDefaultStatusBarBackground(Context param1Context) {
      return null;
    }
    
    public int getTopInset(Object param1Object) {
      return 0;
    }
  }
  
  public static interface DrawerListener {
    void onDrawerClosed(View param1View);
    
    void onDrawerOpened(View param1View);
    
    void onDrawerSlide(View param1View, float param1Float);
    
    void onDrawerStateChanged(int param1Int);
  }
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    private static final int FLAG_IS_CLOSING = 4;
    
    private static final int FLAG_IS_OPENED = 1;
    
    private static final int FLAG_IS_OPENING = 2;
    
    public int gravity = 0;
    
    boolean isPeeking;
    
    float onScreen;
    
    int openState;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(int param1Int1, int param1Int2, int param1Int3) {
      this(param1Int1, param1Int2);
      this.gravity = param1Int3;
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, DrawerLayout.LAYOUT_ATTRS);
      this.gravity = typedArray.getInt(0, 0);
      typedArray.recycle();
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.gravity = param1LayoutParams.gravity;
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
  }
  
  protected static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
          public DrawerLayout.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
            return new DrawerLayout.SavedState(param2Parcel, param2ClassLoader);
          }
          
          public DrawerLayout.SavedState[] newArray(int param2Int) {
            return new DrawerLayout.SavedState[param2Int];
          }
        });
    
    int lockModeEnd;
    
    int lockModeLeft;
    
    int lockModeRight;
    
    int lockModeStart;
    
    int openDrawerGravity = 0;
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      this.openDrawerGravity = param1Parcel.readInt();
      this.lockModeLeft = param1Parcel.readInt();
      this.lockModeRight = param1Parcel.readInt();
      this.lockModeStart = param1Parcel.readInt();
      this.lockModeEnd = param1Parcel.readInt();
    }
    
    public SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.openDrawerGravity);
      param1Parcel.writeInt(this.lockModeLeft);
      param1Parcel.writeInt(this.lockModeRight);
      param1Parcel.writeInt(this.lockModeStart);
      param1Parcel.writeInt(this.lockModeEnd);
    }
  }
  
  static final class null implements ParcelableCompatCreatorCallbacks<SavedState> {
    public DrawerLayout.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new DrawerLayout.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public DrawerLayout.SavedState[] newArray(int param1Int) {
      return new DrawerLayout.SavedState[param1Int];
    }
  }
  
  public static abstract class SimpleDrawerListener implements DrawerListener {
    public void onDrawerClosed(View param1View) {}
    
    public void onDrawerOpened(View param1View) {}
    
    public void onDrawerSlide(View param1View, float param1Float) {}
    
    public void onDrawerStateChanged(int param1Int) {}
  }
  
  private class ViewDragCallback extends ViewDragHelper.Callback {
    private final int mAbsGravity;
    
    private ViewDragHelper mDragger;
    
    private final Runnable mPeekRunnable = new Runnable() {
        public void run() {
          DrawerLayout.ViewDragCallback.this.peekDrawer();
        }
      };
    
    ViewDragCallback(int param1Int) {
      this.mAbsGravity = param1Int;
    }
    
    private void closeOtherDrawer() {
      byte b = 3;
      if (this.mAbsGravity == 3)
        b = 5; 
      View view = DrawerLayout.this.findDrawerWithGravity(b);
      if (view != null)
        DrawerLayout.this.closeDrawer(view); 
    }
    
    public int clampViewPositionHorizontal(View param1View, int param1Int1, int param1Int2) {
      if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(param1View, 3))
        return Math.max(-param1View.getWidth(), Math.min(param1Int1, 0)); 
      param1Int2 = DrawerLayout.this.getWidth();
      return Math.max(param1Int2 - param1View.getWidth(), Math.min(param1Int1, param1Int2));
    }
    
    public int clampViewPositionVertical(View param1View, int param1Int1, int param1Int2) {
      return param1View.getTop();
    }
    
    public int getViewHorizontalDragRange(View param1View) {
      return DrawerLayout.this.isDrawerView(param1View) ? param1View.getWidth() : 0;
    }
    
    public void onEdgeDragStarted(int param1Int1, int param1Int2) {
      View view;
      if ((param1Int1 & 0x1) == 1) {
        view = DrawerLayout.this.findDrawerWithGravity(3);
      } else {
        view = DrawerLayout.this.findDrawerWithGravity(5);
      } 
      if (view != null && DrawerLayout.this.getDrawerLockMode(view) == 0)
        this.mDragger.captureChildView(view, param1Int2); 
    }
    
    public boolean onEdgeLock(int param1Int) {
      return false;
    }
    
    public void onEdgeTouched(int param1Int1, int param1Int2) {
      DrawerLayout.this.postDelayed(this.mPeekRunnable, 160L);
    }
    
    public void onViewCaptured(View param1View, int param1Int) {
      ((DrawerLayout.LayoutParams)param1View.getLayoutParams()).isPeeking = false;
      closeOtherDrawer();
    }
    
    public void onViewDragStateChanged(int param1Int) {
      DrawerLayout.this.updateDrawerState(this.mAbsGravity, param1Int, this.mDragger.getCapturedView());
    }
    
    public void onViewPositionChanged(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      float f;
      param1Int2 = param1View.getWidth();
      if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(param1View, 3)) {
        f = (param1Int2 + param1Int1) / param1Int2;
      } else {
        f = (DrawerLayout.this.getWidth() - param1Int1) / param1Int2;
      } 
      DrawerLayout.this.setDrawerViewOffset(param1View, f);
      if (f == 0.0F) {
        param1Int1 = 4;
      } else {
        param1Int1 = 0;
      } 
      param1View.setVisibility(param1Int1);
      DrawerLayout.this.invalidate();
    }
    
    public void onViewReleased(View param1View, float param1Float1, float param1Float2) {
      int j;
      param1Float2 = DrawerLayout.this.getDrawerViewOffset(param1View);
      int i = param1View.getWidth();
      if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(param1View, 3)) {
        if (param1Float1 > 0.0F || (param1Float1 == 0.0F && param1Float2 > 0.5F)) {
          j = 0;
        } else {
          j = -i;
        } 
      } else {
        j = DrawerLayout.this.getWidth();
        if (param1Float1 < 0.0F || (param1Float1 == 0.0F && param1Float2 > 0.5F))
          j -= i; 
      } 
      this.mDragger.settleCapturedViewAt(j, param1View.getTop());
      DrawerLayout.this.invalidate();
    }
    
    void peekDrawer() {
      boolean bool;
      View view;
      int i = 0;
      int j = this.mDragger.getEdgeSize();
      if (this.mAbsGravity == 3) {
        bool = true;
      } else {
        bool = false;
      } 
      if (bool) {
        view = DrawerLayout.this.findDrawerWithGravity(3);
        if (view != null)
          i = -view.getWidth(); 
        i += j;
      } else {
        view = DrawerLayout.this.findDrawerWithGravity(5);
        i = DrawerLayout.this.getWidth() - j;
      } 
      if (view != null && ((bool && view.getLeft() < i) || (!bool && view.getLeft() > i)) && DrawerLayout.this.getDrawerLockMode(view) == 0) {
        DrawerLayout.LayoutParams layoutParams = (DrawerLayout.LayoutParams)view.getLayoutParams();
        this.mDragger.smoothSlideViewTo(view, i, view.getTop());
        layoutParams.isPeeking = true;
        DrawerLayout.this.invalidate();
        closeOtherDrawer();
        DrawerLayout.this.cancelChildViewTouch();
      } 
    }
    
    public void removeCallbacks() {
      DrawerLayout.this.removeCallbacks(this.mPeekRunnable);
    }
    
    public void setDragger(ViewDragHelper param1ViewDragHelper) {
      this.mDragger = param1ViewDragHelper;
    }
    
    public boolean tryCaptureView(View param1View, int param1Int) {
      return (DrawerLayout.this.isDrawerView(param1View) && DrawerLayout.this.checkDrawerViewAbsoluteGravity(param1View, this.mAbsGravity) && DrawerLayout.this.getDrawerLockMode(param1View) == 0);
    }
  }
  
  class null implements Runnable {
    public void run() {
      this.this$1.peekDrawer();
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/widget/DrawerLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */