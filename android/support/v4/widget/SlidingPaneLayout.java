package android.support.v4.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class SlidingPaneLayout extends ViewGroup {
  private static final int DEFAULT_FADE_COLOR = -858993460;
  
  private static final int DEFAULT_OVERHANG_SIZE = 32;
  
  static final SlidingPanelLayoutImpl IMPL = new SlidingPanelLayoutImplBase();
  
  private static final int MIN_FLING_VELOCITY = 400;
  
  private static final String TAG = "SlidingPaneLayout";
  
  private boolean mCanSlide;
  
  private int mCoveredFadeColor;
  
  final ViewDragHelper mDragHelper;
  
  private boolean mFirstLayout = true;
  
  private float mInitialMotionX;
  
  private float mInitialMotionY;
  
  boolean mIsUnableToDrag;
  
  private final int mOverhangSize;
  
  private PanelSlideListener mPanelSlideListener;
  
  private int mParallaxBy;
  
  private float mParallaxOffset;
  
  final ArrayList<DisableLayerRunnable> mPostedRunnables = new ArrayList<DisableLayerRunnable>();
  
  boolean mPreservedOpenState;
  
  private Drawable mShadowDrawableLeft;
  
  private Drawable mShadowDrawableRight;
  
  float mSlideOffset;
  
  int mSlideRange;
  
  View mSlideableView;
  
  private int mSliderFadeColor = -858993460;
  
  private final Rect mTmpRect = new Rect();
  
  public SlidingPaneLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public SlidingPaneLayout(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public SlidingPaneLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    float f = (paramContext.getResources().getDisplayMetrics()).density;
    this.mOverhangSize = (int)(32.0F * f + 0.5F);
    ViewConfiguration.get(paramContext);
    setWillNotDraw(false);
    ViewCompat.setAccessibilityDelegate((View)this, new AccessibilityDelegate());
    ViewCompat.setImportantForAccessibility((View)this, 1);
    this.mDragHelper = ViewDragHelper.create(this, 0.5F, new DragHelperCallback());
    this.mDragHelper.setMinVelocity(400.0F * f);
  }
  
  private boolean closePane(View paramView, int paramInt) {
    boolean bool = false;
    if (this.mFirstLayout || smoothSlideTo(0.0F, paramInt)) {
      this.mPreservedOpenState = false;
      bool = true;
    } 
    return bool;
  }
  
  private void dimChildView(View paramView, float paramFloat, int paramInt) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (paramFloat > 0.0F && paramInt != 0) {
      int i = (int)(((0xFF000000 & paramInt) >>> 24) * paramFloat);
      if (layoutParams.dimPaint == null)
        layoutParams.dimPaint = new Paint(); 
      layoutParams.dimPaint.setColorFilter((ColorFilter)new PorterDuffColorFilter(i << 24 | 0xFFFFFF & paramInt, PorterDuff.Mode.SRC_OVER));
      if (ViewCompat.getLayerType(paramView) != 2)
        ViewCompat.setLayerType(paramView, 2, layoutParams.dimPaint); 
      invalidateChildRegion(paramView);
      return;
    } 
    if (ViewCompat.getLayerType(paramView) != 0) {
      if (layoutParams.dimPaint != null)
        layoutParams.dimPaint.setColorFilter(null); 
      DisableLayerRunnable disableLayerRunnable = new DisableLayerRunnable(paramView);
      this.mPostedRunnables.add(disableLayerRunnable);
      ViewCompat.postOnAnimation((View)this, disableLayerRunnable);
    } 
  }
  
  private boolean openPane(View paramView, int paramInt) {
    null = true;
    if (this.mFirstLayout || smoothSlideTo(1.0F, paramInt)) {
      this.mPreservedOpenState = true;
      return null;
    } 
    return false;
  }
  
  private void parallaxOtherViews(float paramFloat) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual isLayoutRtlSupport : ()Z
    //   4: istore_2
    //   5: aload_0
    //   6: getfield mSlideableView : Landroid/view/View;
    //   9: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   12: checkcast android/support/v4/widget/SlidingPaneLayout$LayoutParams
    //   15: astore_3
    //   16: aload_3
    //   17: getfield dimWhenOffset : Z
    //   20: ifeq -> 87
    //   23: iload_2
    //   24: ifeq -> 78
    //   27: aload_3
    //   28: getfield rightMargin : I
    //   31: istore #4
    //   33: iload #4
    //   35: ifgt -> 87
    //   38: iconst_1
    //   39: istore #4
    //   41: aload_0
    //   42: invokevirtual getChildCount : ()I
    //   45: istore #5
    //   47: iconst_0
    //   48: istore #6
    //   50: iload #6
    //   52: iload #5
    //   54: if_icmpge -> 189
    //   57: aload_0
    //   58: iload #6
    //   60: invokevirtual getChildAt : (I)Landroid/view/View;
    //   63: astore_3
    //   64: aload_3
    //   65: aload_0
    //   66: getfield mSlideableView : Landroid/view/View;
    //   69: if_acmpne -> 93
    //   72: iinc #6, 1
    //   75: goto -> 50
    //   78: aload_3
    //   79: getfield leftMargin : I
    //   82: istore #4
    //   84: goto -> 33
    //   87: iconst_0
    //   88: istore #4
    //   90: goto -> 41
    //   93: fconst_1
    //   94: aload_0
    //   95: getfield mParallaxOffset : F
    //   98: fsub
    //   99: aload_0
    //   100: getfield mParallaxBy : I
    //   103: i2f
    //   104: fmul
    //   105: f2i
    //   106: istore #7
    //   108: aload_0
    //   109: fload_1
    //   110: putfield mParallaxOffset : F
    //   113: iload #7
    //   115: fconst_1
    //   116: fload_1
    //   117: fsub
    //   118: aload_0
    //   119: getfield mParallaxBy : I
    //   122: i2f
    //   123: fmul
    //   124: f2i
    //   125: isub
    //   126: istore #8
    //   128: iload #8
    //   130: istore #7
    //   132: iload_2
    //   133: ifeq -> 141
    //   136: iload #8
    //   138: ineg
    //   139: istore #7
    //   141: aload_3
    //   142: iload #7
    //   144: invokevirtual offsetLeftAndRight : (I)V
    //   147: iload #4
    //   149: ifeq -> 72
    //   152: iload_2
    //   153: ifeq -> 178
    //   156: aload_0
    //   157: getfield mParallaxOffset : F
    //   160: fconst_1
    //   161: fsub
    //   162: fstore #9
    //   164: aload_0
    //   165: aload_3
    //   166: fload #9
    //   168: aload_0
    //   169: getfield mCoveredFadeColor : I
    //   172: invokespecial dimChildView : (Landroid/view/View;FI)V
    //   175: goto -> 72
    //   178: fconst_1
    //   179: aload_0
    //   180: getfield mParallaxOffset : F
    //   183: fsub
    //   184: fstore #9
    //   186: goto -> 164
    //   189: return
  }
  
  private static boolean viewIsOpaque(View paramView) {
    boolean bool = true;
    if (!paramView.isOpaque()) {
      if (Build.VERSION.SDK_INT >= 18)
        return false; 
      Drawable drawable = paramView.getBackground();
      if (drawable != null) {
        if (drawable.getOpacity() != -1)
          bool = false; 
        return bool;
      } 
      bool = false;
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
    if (paramBoolean) {
      if (!isLayoutRtlSupport())
        paramInt1 = -paramInt1; 
      if (ViewCompat.canScrollHorizontally(paramView, paramInt1))
        return true; 
    } 
    return false;
  }
  
  @Deprecated
  public boolean canSlide() {
    return this.mCanSlide;
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (paramLayoutParams instanceof LayoutParams && super.checkLayoutParams(paramLayoutParams));
  }
  
  public boolean closePane() {
    return closePane(this.mSlideableView, 0);
  }
  
  public void computeScroll() {
    if (this.mDragHelper.continueSettling(true)) {
      if (!this.mCanSlide) {
        this.mDragHelper.abort();
        return;
      } 
    } else {
      return;
    } 
    ViewCompat.postInvalidateOnAnimation((View)this);
  }
  
  void dispatchOnPanelClosed(View paramView) {
    if (this.mPanelSlideListener != null)
      this.mPanelSlideListener.onPanelClosed(paramView); 
    sendAccessibilityEvent(32);
  }
  
  void dispatchOnPanelOpened(View paramView) {
    if (this.mPanelSlideListener != null)
      this.mPanelSlideListener.onPanelOpened(paramView); 
    sendAccessibilityEvent(32);
  }
  
  void dispatchOnPanelSlide(View paramView) {
    if (this.mPanelSlideListener != null)
      this.mPanelSlideListener.onPanelSlide(paramView, this.mSlideOffset); 
  }
  
  public void draw(Canvas paramCanvas) {
    Drawable drawable;
    View view;
    super.draw(paramCanvas);
    if (isLayoutRtlSupport()) {
      drawable = this.mShadowDrawableRight;
    } else {
      drawable = this.mShadowDrawableLeft;
    } 
    if (getChildCount() > 1) {
      view = getChildAt(1);
    } else {
      view = null;
    } 
    if (view != null && drawable != null) {
      int m;
      int n;
      int i = view.getTop();
      int j = view.getBottom();
      int k = drawable.getIntrinsicWidth();
      if (isLayoutRtlSupport()) {
        m = view.getRight();
        n = m + k;
      } else {
        n = view.getLeft();
        m = n - k;
      } 
      drawable.setBounds(m, i, n, j);
      drawable.draw(paramCanvas);
    } 
  }
  
  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = paramCanvas.save(2);
    if (this.mCanSlide && !layoutParams.slideable && this.mSlideableView != null) {
      paramCanvas.getClipBounds(this.mTmpRect);
      if (isLayoutRtlSupport()) {
        this.mTmpRect.left = Math.max(this.mTmpRect.left, this.mSlideableView.getRight());
      } else {
        this.mTmpRect.right = Math.min(this.mTmpRect.right, this.mSlideableView.getLeft());
      } 
      paramCanvas.clipRect(this.mTmpRect);
    } 
    if (Build.VERSION.SDK_INT >= 11) {
      boolean bool1 = super.drawChild(paramCanvas, paramView, paramLong);
      paramCanvas.restoreToCount(i);
      return bool1;
    } 
    if (layoutParams.dimWhenOffset && this.mSlideOffset > 0.0F) {
      if (!paramView.isDrawingCacheEnabled())
        paramView.setDrawingCacheEnabled(true); 
      Bitmap bitmap = paramView.getDrawingCache();
      if (bitmap != null) {
        paramCanvas.drawBitmap(bitmap, paramView.getLeft(), paramView.getTop(), layoutParams.dimPaint);
        boolean bool2 = false;
        paramCanvas.restoreToCount(i);
        return bool2;
      } 
      Log.e("SlidingPaneLayout", "drawChild: child view " + paramView + " returned null drawing cache");
      boolean bool1 = super.drawChild(paramCanvas, paramView, paramLong);
      paramCanvas.restoreToCount(i);
      return bool1;
    } 
    if (paramView.isDrawingCacheEnabled())
      paramView.setDrawingCacheEnabled(false); 
    boolean bool = super.drawChild(paramCanvas, paramView, paramLong);
    paramCanvas.restoreToCount(i);
    return bool;
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
    return (ViewGroup.LayoutParams)new LayoutParams();
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return (ViewGroup.LayoutParams)new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (ViewGroup.LayoutParams)((paramLayoutParams instanceof ViewGroup.MarginLayoutParams) ? new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams) : new LayoutParams(paramLayoutParams));
  }
  
  @ColorInt
  public int getCoveredFadeColor() {
    return this.mCoveredFadeColor;
  }
  
  public int getParallaxDistance() {
    return this.mParallaxBy;
  }
  
  @ColorInt
  public int getSliderFadeColor() {
    return this.mSliderFadeColor;
  }
  
  void invalidateChildRegion(View paramView) {
    IMPL.invalidateChildRegion(this, paramView);
  }
  
  boolean isDimmed(View paramView) {
    boolean bool1 = false;
    if (paramView == null)
      return bool1; 
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    boolean bool2 = bool1;
    if (this.mCanSlide) {
      bool2 = bool1;
      if (layoutParams.dimWhenOffset) {
        bool2 = bool1;
        if (this.mSlideOffset > 0.0F)
          bool2 = true; 
      } 
    } 
    return bool2;
  }
  
  boolean isLayoutRtlSupport() {
    boolean bool = true;
    if (ViewCompat.getLayoutDirection((View)this) != 1)
      bool = false; 
    return bool;
  }
  
  public boolean isOpen() {
    return (!this.mCanSlide || this.mSlideOffset == 1.0F);
  }
  
  public boolean isSlideable() {
    return this.mCanSlide;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    this.mFirstLayout = true;
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    this.mFirstLayout = true;
    byte b = 0;
    int i = this.mPostedRunnables.size();
    while (b < i) {
      ((DisableLayerRunnable)this.mPostedRunnables.get(b)).run();
      b++;
    } 
    this.mPostedRunnables.clear();
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    float f1;
    float f2;
    int i = MotionEventCompat.getActionMasked(paramMotionEvent);
    if (!this.mCanSlide && i == 0 && getChildCount() > 1) {
      View view = getChildAt(1);
      if (view != null) {
        boolean bool;
        if (!this.mDragHelper.isViewUnder(view, (int)paramMotionEvent.getX(), (int)paramMotionEvent.getY())) {
          bool = true;
        } else {
          bool = false;
        } 
        this.mPreservedOpenState = bool;
      } 
    } 
    if (!this.mCanSlide || (this.mIsUnableToDrag && i != 0)) {
      this.mDragHelper.cancel();
      return super.onInterceptTouchEvent(paramMotionEvent);
    } 
    if (i == 3 || i == 1) {
      this.mDragHelper.cancel();
      return false;
    } 
    boolean bool1 = false;
    boolean bool2 = bool1;
    switch (i) {
      default:
        bool2 = bool1;
      case 1:
        if (this.mDragHelper.shouldInterceptTouchEvent(paramMotionEvent) || bool2)
          return true; 
        break;
      case 0:
        this.mIsUnableToDrag = false;
        f1 = paramMotionEvent.getX();
        f2 = paramMotionEvent.getY();
        this.mInitialMotionX = f1;
        this.mInitialMotionY = f2;
        bool2 = bool1;
        if (this.mDragHelper.isViewUnder(this.mSlideableView, (int)f1, (int)f2)) {
          bool2 = bool1;
          if (isDimmed(this.mSlideableView))
            bool2 = true; 
        } 
      case 2:
        f2 = paramMotionEvent.getX();
        f1 = paramMotionEvent.getY();
        f2 = Math.abs(f2 - this.mInitialMotionX);
        f1 = Math.abs(f1 - this.mInitialMotionY);
        bool2 = bool1;
        if (f2 > this.mDragHelper.getTouchSlop()) {
          bool2 = bool1;
          if (f1 > f2) {
            this.mDragHelper.cancel();
            this.mIsUnableToDrag = true;
            return false;
          } 
        } 
    } 
    return false;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual isLayoutRtlSupport : ()Z
    //   4: istore #6
    //   6: iload #6
    //   8: ifeq -> 134
    //   11: aload_0
    //   12: getfield mDragHelper : Landroid/support/v4/widget/ViewDragHelper;
    //   15: iconst_2
    //   16: invokevirtual setEdgeTrackingEnabled : (I)V
    //   19: iload #4
    //   21: iload_2
    //   22: isub
    //   23: istore #7
    //   25: iload #6
    //   27: ifeq -> 145
    //   30: aload_0
    //   31: invokevirtual getPaddingRight : ()I
    //   34: istore_2
    //   35: iload #6
    //   37: ifeq -> 153
    //   40: aload_0
    //   41: invokevirtual getPaddingLeft : ()I
    //   44: istore #4
    //   46: aload_0
    //   47: invokevirtual getPaddingTop : ()I
    //   50: istore #8
    //   52: aload_0
    //   53: invokevirtual getChildCount : ()I
    //   56: istore #9
    //   58: iload_2
    //   59: istore #5
    //   61: aload_0
    //   62: getfield mFirstLayout : Z
    //   65: ifeq -> 91
    //   68: aload_0
    //   69: getfield mCanSlide : Z
    //   72: ifeq -> 162
    //   75: aload_0
    //   76: getfield mPreservedOpenState : Z
    //   79: ifeq -> 162
    //   82: fconst_1
    //   83: fstore #10
    //   85: aload_0
    //   86: fload #10
    //   88: putfield mSlideOffset : F
    //   91: iconst_0
    //   92: istore #11
    //   94: iload_2
    //   95: istore_3
    //   96: iload #5
    //   98: istore_2
    //   99: iload #11
    //   101: istore #5
    //   103: iload #5
    //   105: iload #9
    //   107: if_icmpge -> 441
    //   110: aload_0
    //   111: iload #5
    //   113: invokevirtual getChildAt : (I)Landroid/view/View;
    //   116: astore #12
    //   118: aload #12
    //   120: invokevirtual getVisibility : ()I
    //   123: bipush #8
    //   125: if_icmpne -> 168
    //   128: iinc #5, 1
    //   131: goto -> 103
    //   134: aload_0
    //   135: getfield mDragHelper : Landroid/support/v4/widget/ViewDragHelper;
    //   138: iconst_1
    //   139: invokevirtual setEdgeTrackingEnabled : (I)V
    //   142: goto -> 19
    //   145: aload_0
    //   146: invokevirtual getPaddingLeft : ()I
    //   149: istore_2
    //   150: goto -> 35
    //   153: aload_0
    //   154: invokevirtual getPaddingRight : ()I
    //   157: istore #4
    //   159: goto -> 46
    //   162: fconst_0
    //   163: fstore #10
    //   165: goto -> 85
    //   168: aload #12
    //   170: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   173: checkcast android/support/v4/widget/SlidingPaneLayout$LayoutParams
    //   176: astore #13
    //   178: aload #12
    //   180: invokevirtual getMeasuredWidth : ()I
    //   183: istore #14
    //   185: iconst_0
    //   186: istore #15
    //   188: aload #13
    //   190: getfield slideable : Z
    //   193: ifeq -> 382
    //   196: aload #13
    //   198: getfield leftMargin : I
    //   201: istore #11
    //   203: aload #13
    //   205: getfield rightMargin : I
    //   208: istore #16
    //   210: iload_2
    //   211: iload #7
    //   213: iload #4
    //   215: isub
    //   216: aload_0
    //   217: getfield mOverhangSize : I
    //   220: isub
    //   221: invokestatic min : (II)I
    //   224: iload_3
    //   225: isub
    //   226: iload #11
    //   228: iload #16
    //   230: iadd
    //   231: isub
    //   232: istore #16
    //   234: aload_0
    //   235: iload #16
    //   237: putfield mSlideRange : I
    //   240: iload #6
    //   242: ifeq -> 367
    //   245: aload #13
    //   247: getfield rightMargin : I
    //   250: istore #11
    //   252: iload_3
    //   253: iload #11
    //   255: iadd
    //   256: iload #16
    //   258: iadd
    //   259: iload #14
    //   261: iconst_2
    //   262: idiv
    //   263: iadd
    //   264: iload #7
    //   266: iload #4
    //   268: isub
    //   269: if_icmple -> 377
    //   272: iconst_1
    //   273: istore_1
    //   274: aload #13
    //   276: iload_1
    //   277: putfield dimWhenOffset : Z
    //   280: iload #16
    //   282: i2f
    //   283: aload_0
    //   284: getfield mSlideOffset : F
    //   287: fmul
    //   288: f2i
    //   289: istore #16
    //   291: iload_3
    //   292: iload #16
    //   294: iload #11
    //   296: iadd
    //   297: iadd
    //   298: istore_3
    //   299: aload_0
    //   300: iload #16
    //   302: i2f
    //   303: aload_0
    //   304: getfield mSlideRange : I
    //   307: i2f
    //   308: fdiv
    //   309: putfield mSlideOffset : F
    //   312: iload #15
    //   314: istore #11
    //   316: iload #6
    //   318: ifeq -> 425
    //   321: iload #7
    //   323: iload_3
    //   324: isub
    //   325: iload #11
    //   327: iadd
    //   328: istore #15
    //   330: iload #15
    //   332: iload #14
    //   334: isub
    //   335: istore #11
    //   337: aload #12
    //   339: iload #11
    //   341: iload #8
    //   343: iload #15
    //   345: iload #8
    //   347: aload #12
    //   349: invokevirtual getMeasuredHeight : ()I
    //   352: iadd
    //   353: invokevirtual layout : (IIII)V
    //   356: iload_2
    //   357: aload #12
    //   359: invokevirtual getWidth : ()I
    //   362: iadd
    //   363: istore_2
    //   364: goto -> 128
    //   367: aload #13
    //   369: getfield leftMargin : I
    //   372: istore #11
    //   374: goto -> 252
    //   377: iconst_0
    //   378: istore_1
    //   379: goto -> 274
    //   382: aload_0
    //   383: getfield mCanSlide : Z
    //   386: ifeq -> 416
    //   389: aload_0
    //   390: getfield mParallaxBy : I
    //   393: ifeq -> 416
    //   396: fconst_1
    //   397: aload_0
    //   398: getfield mSlideOffset : F
    //   401: fsub
    //   402: aload_0
    //   403: getfield mParallaxBy : I
    //   406: i2f
    //   407: fmul
    //   408: f2i
    //   409: istore #11
    //   411: iload_2
    //   412: istore_3
    //   413: goto -> 316
    //   416: iload_2
    //   417: istore_3
    //   418: iload #15
    //   420: istore #11
    //   422: goto -> 316
    //   425: iload_3
    //   426: iload #11
    //   428: isub
    //   429: istore #11
    //   431: iload #11
    //   433: iload #14
    //   435: iadd
    //   436: istore #15
    //   438: goto -> 337
    //   441: aload_0
    //   442: getfield mFirstLayout : Z
    //   445: ifeq -> 510
    //   448: aload_0
    //   449: getfield mCanSlide : Z
    //   452: ifeq -> 516
    //   455: aload_0
    //   456: getfield mParallaxBy : I
    //   459: ifeq -> 470
    //   462: aload_0
    //   463: aload_0
    //   464: getfield mSlideOffset : F
    //   467: invokespecial parallaxOtherViews : (F)V
    //   470: aload_0
    //   471: getfield mSlideableView : Landroid/view/View;
    //   474: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   477: checkcast android/support/v4/widget/SlidingPaneLayout$LayoutParams
    //   480: getfield dimWhenOffset : Z
    //   483: ifeq -> 502
    //   486: aload_0
    //   487: aload_0
    //   488: getfield mSlideableView : Landroid/view/View;
    //   491: aload_0
    //   492: getfield mSlideOffset : F
    //   495: aload_0
    //   496: getfield mSliderFadeColor : I
    //   499: invokespecial dimChildView : (Landroid/view/View;FI)V
    //   502: aload_0
    //   503: aload_0
    //   504: getfield mSlideableView : Landroid/view/View;
    //   507: invokevirtual updateObscuredViewsVisibility : (Landroid/view/View;)V
    //   510: aload_0
    //   511: iconst_0
    //   512: putfield mFirstLayout : Z
    //   515: return
    //   516: iconst_0
    //   517: istore_2
    //   518: iload_2
    //   519: iload #9
    //   521: if_icmpge -> 502
    //   524: aload_0
    //   525: aload_0
    //   526: iload_2
    //   527: invokevirtual getChildAt : (I)Landroid/view/View;
    //   530: fconst_0
    //   531: aload_0
    //   532: getfield mSliderFadeColor : I
    //   535: invokespecial dimChildView : (Landroid/view/View;FI)V
    //   538: iinc #2, 1
    //   541: goto -> 518
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iload_1
    //   1: invokestatic getMode : (I)I
    //   4: istore_3
    //   5: iload_1
    //   6: invokestatic getSize : (I)I
    //   9: istore #4
    //   11: iload_2
    //   12: invokestatic getMode : (I)I
    //   15: istore #5
    //   17: iload_2
    //   18: invokestatic getSize : (I)I
    //   21: istore_2
    //   22: iload_3
    //   23: ldc_w 1073741824
    //   26: if_icmpeq -> 248
    //   29: aload_0
    //   30: invokevirtual isInEditMode : ()Z
    //   33: ifeq -> 237
    //   36: iload_3
    //   37: ldc_w -2147483648
    //   40: if_icmpne -> 209
    //   43: iload #4
    //   45: istore #6
    //   47: iload_2
    //   48: istore_1
    //   49: iload #5
    //   51: istore #7
    //   53: iconst_0
    //   54: istore #4
    //   56: iconst_m1
    //   57: istore_2
    //   58: iload #7
    //   60: lookupswitch default -> 88, -2147483648 -> 330, 1073741824 -> 312
    //   88: fconst_0
    //   89: fstore #8
    //   91: iconst_0
    //   92: istore #9
    //   94: iload #6
    //   96: aload_0
    //   97: invokevirtual getPaddingLeft : ()I
    //   100: isub
    //   101: aload_0
    //   102: invokevirtual getPaddingRight : ()I
    //   105: isub
    //   106: istore #10
    //   108: iload #10
    //   110: istore_3
    //   111: aload_0
    //   112: invokevirtual getChildCount : ()I
    //   115: istore #11
    //   117: iload #11
    //   119: iconst_2
    //   120: if_icmple -> 132
    //   123: ldc 'SlidingPaneLayout'
    //   125: ldc_w 'onMeasure: More than two child views are not supported.'
    //   128: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   131: pop
    //   132: aload_0
    //   133: aconst_null
    //   134: putfield mSlideableView : Landroid/view/View;
    //   137: iconst_0
    //   138: istore #12
    //   140: iload #12
    //   142: iload #11
    //   144: if_icmpge -> 650
    //   147: aload_0
    //   148: iload #12
    //   150: invokevirtual getChildAt : (I)Landroid/view/View;
    //   153: astore #13
    //   155: aload #13
    //   157: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   160: checkcast android/support/v4/widget/SlidingPaneLayout$LayoutParams
    //   163: astore #14
    //   165: aload #13
    //   167: invokevirtual getVisibility : ()I
    //   170: bipush #8
    //   172: if_icmpne -> 345
    //   175: aload #14
    //   177: iconst_0
    //   178: putfield dimWhenOffset : Z
    //   181: iload_3
    //   182: istore #15
    //   184: iload #4
    //   186: istore #5
    //   188: iload #9
    //   190: istore #16
    //   192: iinc #12, 1
    //   195: iload #16
    //   197: istore #9
    //   199: iload #5
    //   201: istore #4
    //   203: iload #15
    //   205: istore_3
    //   206: goto -> 140
    //   209: iload #5
    //   211: istore #7
    //   213: iload_2
    //   214: istore_1
    //   215: iload #4
    //   217: istore #6
    //   219: iload_3
    //   220: ifne -> 53
    //   223: sipush #300
    //   226: istore #6
    //   228: iload #5
    //   230: istore #7
    //   232: iload_2
    //   233: istore_1
    //   234: goto -> 53
    //   237: new java/lang/IllegalStateException
    //   240: dup
    //   241: ldc_w 'Width must have an exact value or MATCH_PARENT'
    //   244: invokespecial <init> : (Ljava/lang/String;)V
    //   247: athrow
    //   248: iload #5
    //   250: istore #7
    //   252: iload_2
    //   253: istore_1
    //   254: iload #4
    //   256: istore #6
    //   258: iload #5
    //   260: ifne -> 53
    //   263: aload_0
    //   264: invokevirtual isInEditMode : ()Z
    //   267: ifeq -> 301
    //   270: iload #5
    //   272: istore #7
    //   274: iload_2
    //   275: istore_1
    //   276: iload #4
    //   278: istore #6
    //   280: iload #5
    //   282: ifne -> 53
    //   285: ldc_w -2147483648
    //   288: istore #7
    //   290: sipush #300
    //   293: istore_1
    //   294: iload #4
    //   296: istore #6
    //   298: goto -> 53
    //   301: new java/lang/IllegalStateException
    //   304: dup
    //   305: ldc_w 'Height must not be UNSPECIFIED'
    //   308: invokespecial <init> : (Ljava/lang/String;)V
    //   311: athrow
    //   312: iload_1
    //   313: aload_0
    //   314: invokevirtual getPaddingTop : ()I
    //   317: isub
    //   318: aload_0
    //   319: invokevirtual getPaddingBottom : ()I
    //   322: isub
    //   323: istore_2
    //   324: iload_2
    //   325: istore #4
    //   327: goto -> 88
    //   330: iload_1
    //   331: aload_0
    //   332: invokevirtual getPaddingTop : ()I
    //   335: isub
    //   336: aload_0
    //   337: invokevirtual getPaddingBottom : ()I
    //   340: isub
    //   341: istore_2
    //   342: goto -> 88
    //   345: fload #8
    //   347: fstore #17
    //   349: aload #14
    //   351: getfield weight : F
    //   354: fconst_0
    //   355: fcmpl
    //   356: ifle -> 392
    //   359: fload #8
    //   361: aload #14
    //   363: getfield weight : F
    //   366: fadd
    //   367: fstore #17
    //   369: iload #9
    //   371: istore #16
    //   373: iload #4
    //   375: istore #5
    //   377: fload #17
    //   379: fstore #8
    //   381: iload_3
    //   382: istore #15
    //   384: aload #14
    //   386: getfield width : I
    //   389: ifeq -> 192
    //   392: aload #14
    //   394: getfield leftMargin : I
    //   397: aload #14
    //   399: getfield rightMargin : I
    //   402: iadd
    //   403: istore_1
    //   404: aload #14
    //   406: getfield width : I
    //   409: bipush #-2
    //   411: if_icmpne -> 569
    //   414: iload #10
    //   416: iload_1
    //   417: isub
    //   418: ldc_w -2147483648
    //   421: invokestatic makeMeasureSpec : (II)I
    //   424: istore_1
    //   425: aload #14
    //   427: getfield height : I
    //   430: bipush #-2
    //   432: if_icmpne -> 607
    //   435: iload_2
    //   436: ldc_w -2147483648
    //   439: invokestatic makeMeasureSpec : (II)I
    //   442: istore #5
    //   444: aload #13
    //   446: iload_1
    //   447: iload #5
    //   449: invokevirtual measure : (II)V
    //   452: aload #13
    //   454: invokevirtual getMeasuredWidth : ()I
    //   457: istore #5
    //   459: aload #13
    //   461: invokevirtual getMeasuredHeight : ()I
    //   464: istore #15
    //   466: iload #4
    //   468: istore_1
    //   469: iload #7
    //   471: ldc_w -2147483648
    //   474: if_icmpne -> 494
    //   477: iload #4
    //   479: istore_1
    //   480: iload #15
    //   482: iload #4
    //   484: if_icmple -> 494
    //   487: iload #15
    //   489: iload_2
    //   490: invokestatic min : (II)I
    //   493: istore_1
    //   494: iload_3
    //   495: iload #5
    //   497: isub
    //   498: istore #4
    //   500: iload #4
    //   502: ifge -> 644
    //   505: iconst_1
    //   506: istore #16
    //   508: aload #14
    //   510: iload #16
    //   512: putfield slideable : Z
    //   515: iload #9
    //   517: iload #16
    //   519: ior
    //   520: istore #9
    //   522: iload #9
    //   524: istore #16
    //   526: iload_1
    //   527: istore #5
    //   529: fload #17
    //   531: fstore #8
    //   533: iload #4
    //   535: istore #15
    //   537: aload #14
    //   539: getfield slideable : Z
    //   542: ifeq -> 192
    //   545: aload_0
    //   546: aload #13
    //   548: putfield mSlideableView : Landroid/view/View;
    //   551: iload #9
    //   553: istore #16
    //   555: iload_1
    //   556: istore #5
    //   558: fload #17
    //   560: fstore #8
    //   562: iload #4
    //   564: istore #15
    //   566: goto -> 192
    //   569: aload #14
    //   571: getfield width : I
    //   574: iconst_m1
    //   575: if_icmpne -> 592
    //   578: iload #10
    //   580: iload_1
    //   581: isub
    //   582: ldc_w 1073741824
    //   585: invokestatic makeMeasureSpec : (II)I
    //   588: istore_1
    //   589: goto -> 425
    //   592: aload #14
    //   594: getfield width : I
    //   597: ldc_w 1073741824
    //   600: invokestatic makeMeasureSpec : (II)I
    //   603: istore_1
    //   604: goto -> 425
    //   607: aload #14
    //   609: getfield height : I
    //   612: iconst_m1
    //   613: if_icmpne -> 628
    //   616: iload_2
    //   617: ldc_w 1073741824
    //   620: invokestatic makeMeasureSpec : (II)I
    //   623: istore #5
    //   625: goto -> 444
    //   628: aload #14
    //   630: getfield height : I
    //   633: ldc_w 1073741824
    //   636: invokestatic makeMeasureSpec : (II)I
    //   639: istore #5
    //   641: goto -> 444
    //   644: iconst_0
    //   645: istore #16
    //   647: goto -> 508
    //   650: iload #9
    //   652: ifne -> 662
    //   655: fload #8
    //   657: fconst_0
    //   658: fcmpl
    //   659: ifle -> 1068
    //   662: iload #10
    //   664: aload_0
    //   665: getfield mOverhangSize : I
    //   668: isub
    //   669: istore #12
    //   671: iconst_0
    //   672: istore #5
    //   674: iload #5
    //   676: iload #11
    //   678: if_icmpge -> 1068
    //   681: aload_0
    //   682: iload #5
    //   684: invokevirtual getChildAt : (I)Landroid/view/View;
    //   687: astore #14
    //   689: aload #14
    //   691: invokevirtual getVisibility : ()I
    //   694: bipush #8
    //   696: if_icmpne -> 705
    //   699: iinc #5, 1
    //   702: goto -> 674
    //   705: aload #14
    //   707: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   710: checkcast android/support/v4/widget/SlidingPaneLayout$LayoutParams
    //   713: astore #13
    //   715: aload #14
    //   717: invokevirtual getVisibility : ()I
    //   720: bipush #8
    //   722: if_icmpeq -> 699
    //   725: aload #13
    //   727: getfield width : I
    //   730: ifne -> 830
    //   733: aload #13
    //   735: getfield weight : F
    //   738: fconst_0
    //   739: fcmpl
    //   740: ifle -> 830
    //   743: iconst_1
    //   744: istore_1
    //   745: iload_1
    //   746: ifeq -> 835
    //   749: iconst_0
    //   750: istore #7
    //   752: iload #9
    //   754: ifeq -> 895
    //   757: aload #14
    //   759: aload_0
    //   760: getfield mSlideableView : Landroid/view/View;
    //   763: if_acmpeq -> 895
    //   766: aload #13
    //   768: getfield width : I
    //   771: ifge -> 699
    //   774: iload #7
    //   776: iload #12
    //   778: if_icmpgt -> 791
    //   781: aload #13
    //   783: getfield weight : F
    //   786: fconst_0
    //   787: fcmpl
    //   788: ifle -> 699
    //   791: iload_1
    //   792: ifeq -> 880
    //   795: aload #13
    //   797: getfield height : I
    //   800: bipush #-2
    //   802: if_icmpne -> 845
    //   805: iload_2
    //   806: ldc_w -2147483648
    //   809: invokestatic makeMeasureSpec : (II)I
    //   812: istore_1
    //   813: aload #14
    //   815: iload #12
    //   817: ldc_w 1073741824
    //   820: invokestatic makeMeasureSpec : (II)I
    //   823: iload_1
    //   824: invokevirtual measure : (II)V
    //   827: goto -> 699
    //   830: iconst_0
    //   831: istore_1
    //   832: goto -> 745
    //   835: aload #14
    //   837: invokevirtual getMeasuredWidth : ()I
    //   840: istore #7
    //   842: goto -> 752
    //   845: aload #13
    //   847: getfield height : I
    //   850: iconst_m1
    //   851: if_icmpne -> 865
    //   854: iload_2
    //   855: ldc_w 1073741824
    //   858: invokestatic makeMeasureSpec : (II)I
    //   861: istore_1
    //   862: goto -> 813
    //   865: aload #13
    //   867: getfield height : I
    //   870: ldc_w 1073741824
    //   873: invokestatic makeMeasureSpec : (II)I
    //   876: istore_1
    //   877: goto -> 813
    //   880: aload #14
    //   882: invokevirtual getMeasuredHeight : ()I
    //   885: ldc_w 1073741824
    //   888: invokestatic makeMeasureSpec : (II)I
    //   891: istore_1
    //   892: goto -> 813
    //   895: aload #13
    //   897: getfield weight : F
    //   900: fconst_0
    //   901: fcmpl
    //   902: ifle -> 699
    //   905: aload #13
    //   907: getfield width : I
    //   910: ifne -> 1015
    //   913: aload #13
    //   915: getfield height : I
    //   918: bipush #-2
    //   920: if_icmpne -> 980
    //   923: iload_2
    //   924: ldc_w -2147483648
    //   927: invokestatic makeMeasureSpec : (II)I
    //   930: istore_1
    //   931: iload #9
    //   933: ifeq -> 1030
    //   936: iload #10
    //   938: aload #13
    //   940: getfield leftMargin : I
    //   943: aload #13
    //   945: getfield rightMargin : I
    //   948: iadd
    //   949: isub
    //   950: istore #15
    //   952: iload #15
    //   954: ldc_w 1073741824
    //   957: invokestatic makeMeasureSpec : (II)I
    //   960: istore #18
    //   962: iload #7
    //   964: iload #15
    //   966: if_icmpeq -> 699
    //   969: aload #14
    //   971: iload #18
    //   973: iload_1
    //   974: invokevirtual measure : (II)V
    //   977: goto -> 699
    //   980: aload #13
    //   982: getfield height : I
    //   985: iconst_m1
    //   986: if_icmpne -> 1000
    //   989: iload_2
    //   990: ldc_w 1073741824
    //   993: invokestatic makeMeasureSpec : (II)I
    //   996: istore_1
    //   997: goto -> 931
    //   1000: aload #13
    //   1002: getfield height : I
    //   1005: ldc_w 1073741824
    //   1008: invokestatic makeMeasureSpec : (II)I
    //   1011: istore_1
    //   1012: goto -> 931
    //   1015: aload #14
    //   1017: invokevirtual getMeasuredHeight : ()I
    //   1020: ldc_w 1073741824
    //   1023: invokestatic makeMeasureSpec : (II)I
    //   1026: istore_1
    //   1027: goto -> 931
    //   1030: iconst_0
    //   1031: iload_3
    //   1032: invokestatic max : (II)I
    //   1035: istore #15
    //   1037: aload #14
    //   1039: iload #7
    //   1041: aload #13
    //   1043: getfield weight : F
    //   1046: iload #15
    //   1048: i2f
    //   1049: fmul
    //   1050: fload #8
    //   1052: fdiv
    //   1053: f2i
    //   1054: iadd
    //   1055: ldc_w 1073741824
    //   1058: invokestatic makeMeasureSpec : (II)I
    //   1061: iload_1
    //   1062: invokevirtual measure : (II)V
    //   1065: goto -> 699
    //   1068: aload_0
    //   1069: iload #6
    //   1071: aload_0
    //   1072: invokevirtual getPaddingTop : ()I
    //   1075: iload #4
    //   1077: iadd
    //   1078: aload_0
    //   1079: invokevirtual getPaddingBottom : ()I
    //   1082: iadd
    //   1083: invokevirtual setMeasuredDimension : (II)V
    //   1086: aload_0
    //   1087: iload #9
    //   1089: putfield mCanSlide : Z
    //   1092: aload_0
    //   1093: getfield mDragHelper : Landroid/support/v4/widget/ViewDragHelper;
    //   1096: invokevirtual getViewDragState : ()I
    //   1099: ifeq -> 1114
    //   1102: iload #9
    //   1104: ifne -> 1114
    //   1107: aload_0
    //   1108: getfield mDragHelper : Landroid/support/v4/widget/ViewDragHelper;
    //   1111: invokevirtual abort : ()V
    //   1114: return
  }
  
  void onPanelDragged(int paramInt) {
    int j;
    if (this.mSlideableView == null) {
      this.mSlideOffset = 0.0F;
      return;
    } 
    boolean bool = isLayoutRtlSupport();
    LayoutParams layoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
    int i = this.mSlideableView.getWidth();
    if (bool)
      paramInt = getWidth() - paramInt - i; 
    if (bool) {
      i = getPaddingRight();
    } else {
      i = getPaddingLeft();
    } 
    if (bool) {
      j = layoutParams.rightMargin;
    } else {
      j = layoutParams.leftMargin;
    } 
    this.mSlideOffset = (paramInt - i + j) / this.mSlideRange;
    if (this.mParallaxBy != 0)
      parallaxOtherViews(this.mSlideOffset); 
    if (layoutParams.dimWhenOffset)
      dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor); 
    dispatchOnPanelSlide(this.mSlideableView);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    if (savedState.isOpen) {
      openPane();
    } else {
      closePane();
    } 
    this.mPreservedOpenState = savedState.isOpen;
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    if (isSlideable()) {
      boolean bool1 = isOpen();
      savedState.isOpen = bool1;
      return (Parcelable)savedState;
    } 
    boolean bool = this.mPreservedOpenState;
    savedState.isOpen = bool;
    return (Parcelable)savedState;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 != paramInt3)
      this.mFirstLayout = true; 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    float f1;
    float f2;
    if (!this.mCanSlide)
      return super.onTouchEvent(paramMotionEvent); 
    this.mDragHelper.processTouchEvent(paramMotionEvent);
    int i = paramMotionEvent.getAction();
    boolean bool2 = true;
    switch (i & 0xFF) {
      default:
        return bool2;
      case 0:
        f1 = paramMotionEvent.getX();
        f2 = paramMotionEvent.getY();
        this.mInitialMotionX = f1;
        this.mInitialMotionY = f2;
        return bool2;
      case 1:
        break;
    } 
    boolean bool1 = bool2;
    if (isDimmed(this.mSlideableView)) {
      float f3 = paramMotionEvent.getX();
      f1 = paramMotionEvent.getY();
      float f4 = f3 - this.mInitialMotionX;
      f2 = f1 - this.mInitialMotionY;
      i = this.mDragHelper.getTouchSlop();
      bool1 = bool2;
      if (f4 * f4 + f2 * f2 < (i * i)) {
        bool1 = bool2;
        if (this.mDragHelper.isViewUnder(this.mSlideableView, (int)f3, (int)f1)) {
          closePane(this.mSlideableView, 0);
          bool1 = bool2;
        } 
      } 
    } 
    return bool1;
  }
  
  public boolean openPane() {
    return openPane(this.mSlideableView, 0);
  }
  
  public void requestChildFocus(View paramView1, View paramView2) {
    super.requestChildFocus(paramView1, paramView2);
    if (!isInTouchMode() && !this.mCanSlide) {
      boolean bool;
      if (paramView1 == this.mSlideableView) {
        bool = true;
      } else {
        bool = false;
      } 
      this.mPreservedOpenState = bool;
    } 
  }
  
  void setAllChildrenVisible() {
    byte b = 0;
    int i = getChildCount();
    while (b < i) {
      View view = getChildAt(b);
      if (view.getVisibility() == 4)
        view.setVisibility(0); 
      b++;
    } 
  }
  
  public void setCoveredFadeColor(@ColorInt int paramInt) {
    this.mCoveredFadeColor = paramInt;
  }
  
  public void setPanelSlideListener(PanelSlideListener paramPanelSlideListener) {
    this.mPanelSlideListener = paramPanelSlideListener;
  }
  
  public void setParallaxDistance(int paramInt) {
    this.mParallaxBy = paramInt;
    requestLayout();
  }
  
  @Deprecated
  public void setShadowDrawable(Drawable paramDrawable) {
    setShadowDrawableLeft(paramDrawable);
  }
  
  public void setShadowDrawableLeft(Drawable paramDrawable) {
    this.mShadowDrawableLeft = paramDrawable;
  }
  
  public void setShadowDrawableRight(Drawable paramDrawable) {
    this.mShadowDrawableRight = paramDrawable;
  }
  
  @Deprecated
  public void setShadowResource(@DrawableRes int paramInt) {
    setShadowDrawable(getResources().getDrawable(paramInt));
  }
  
  public void setShadowResourceLeft(int paramInt) {
    setShadowDrawableLeft(ContextCompat.getDrawable(getContext(), paramInt));
  }
  
  public void setShadowResourceRight(int paramInt) {
    setShadowDrawableRight(ContextCompat.getDrawable(getContext(), paramInt));
  }
  
  public void setSliderFadeColor(@ColorInt int paramInt) {
    this.mSliderFadeColor = paramInt;
  }
  
  @Deprecated
  public void smoothSlideClosed() {
    closePane();
  }
  
  @Deprecated
  public void smoothSlideOpen() {
    openPane();
  }
  
  boolean smoothSlideTo(float paramFloat, int paramInt) {
    boolean bool = false;
    if (this.mCanSlide) {
      boolean bool1 = isLayoutRtlSupport();
      LayoutParams layoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
      if (bool1) {
        int i = getPaddingRight();
        int j = layoutParams.rightMargin;
        paramInt = this.mSlideableView.getWidth();
        paramInt = (int)(getWidth() - (i + j) + this.mSlideRange * paramFloat + paramInt);
      } else {
        paramInt = (int)((getPaddingLeft() + layoutParams.leftMargin) + this.mSlideRange * paramFloat);
      } 
      if (this.mDragHelper.smoothSlideViewTo(this.mSlideableView, paramInt, this.mSlideableView.getTop())) {
        setAllChildrenVisible();
        ViewCompat.postInvalidateOnAnimation((View)this);
        bool = true;
      } 
    } 
    return bool;
  }
  
  void updateObscuredViewsVisibility(View paramView) {
    int i;
    int j;
    byte b1;
    byte b2;
    byte b3;
    byte b4;
    boolean bool = isLayoutRtlSupport();
    if (bool) {
      i = getWidth() - getPaddingRight();
    } else {
      i = getPaddingLeft();
    } 
    if (bool) {
      j = getPaddingLeft();
    } else {
      j = getWidth() - getPaddingRight();
    } 
    int k = getPaddingTop();
    int m = getHeight();
    int n = getPaddingBottom();
    if (paramView != null && viewIsOpaque(paramView)) {
      b1 = paramView.getLeft();
      b2 = paramView.getRight();
      b3 = paramView.getTop();
      b4 = paramView.getBottom();
    } else {
      b4 = 0;
      b3 = 0;
      b2 = 0;
      b1 = 0;
    } 
    byte b5 = 0;
    int i1 = getChildCount();
    while (true) {
      if (b5 < i1) {
        View view = getChildAt(b5);
        if (view != paramView) {
          if (view.getVisibility() != 8) {
            if (bool) {
              i2 = j;
            } else {
              i2 = i;
            } 
            int i3 = Math.max(i2, view.getLeft());
            int i4 = Math.max(k, view.getTop());
            if (bool) {
              i2 = i;
            } else {
              i2 = j;
            } 
            int i5 = Math.min(i2, view.getRight());
            int i2 = Math.min(m - n, view.getBottom());
            if (i3 >= b1 && i4 >= b3 && i5 <= b2 && i2 <= b4) {
              i2 = 4;
            } else {
              i2 = 0;
            } 
            view.setVisibility(i2);
          } 
          b5++;
          continue;
        } 
      } 
      return;
    } 
  }
  
  static {
    int i = Build.VERSION.SDK_INT;
    if (i >= 17) {
      IMPL = new SlidingPanelLayoutImplJBMR1();
      return;
    } 
    if (i >= 16) {
      IMPL = new SlidingPanelLayoutImplJB();
      return;
    } 
  }
  
  class AccessibilityDelegate extends AccessibilityDelegateCompat {
    private final Rect mTmpRect = new Rect();
    
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
      param1AccessibilityNodeInfoCompat1.setMovementGranularities(param1AccessibilityNodeInfoCompat2.getMovementGranularities());
    }
    
    public boolean filter(View param1View) {
      return SlidingPaneLayout.this.isDimmed(param1View);
    }
    
    public void onInitializeAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      super.onInitializeAccessibilityEvent(param1View, param1AccessibilityEvent);
      param1AccessibilityEvent.setClassName(SlidingPaneLayout.class.getName());
    }
    
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.obtain(param1AccessibilityNodeInfoCompat);
      super.onInitializeAccessibilityNodeInfo(param1View, accessibilityNodeInfoCompat);
      copyNodeInfoNoChildren(param1AccessibilityNodeInfoCompat, accessibilityNodeInfoCompat);
      accessibilityNodeInfoCompat.recycle();
      param1AccessibilityNodeInfoCompat.setClassName(SlidingPaneLayout.class.getName());
      param1AccessibilityNodeInfoCompat.setSource(param1View);
      ViewParent viewParent = ViewCompat.getParentForAccessibility(param1View);
      if (viewParent instanceof View)
        param1AccessibilityNodeInfoCompat.setParent((View)viewParent); 
      int i = SlidingPaneLayout.this.getChildCount();
      for (byte b = 0; b < i; b++) {
        View view = SlidingPaneLayout.this.getChildAt(b);
        if (!filter(view) && view.getVisibility() == 0) {
          ViewCompat.setImportantForAccessibility(view, 1);
          param1AccessibilityNodeInfoCompat.addChild(view);
        } 
      } 
    }
    
    public boolean onRequestSendAccessibilityEvent(ViewGroup param1ViewGroup, View param1View, AccessibilityEvent param1AccessibilityEvent) {
      return !filter(param1View) ? super.onRequestSendAccessibilityEvent(param1ViewGroup, param1View, param1AccessibilityEvent) : false;
    }
  }
  
  private class DisableLayerRunnable implements Runnable {
    final View mChildView;
    
    DisableLayerRunnable(View param1View) {
      this.mChildView = param1View;
    }
    
    public void run() {
      if (this.mChildView.getParent() == SlidingPaneLayout.this) {
        ViewCompat.setLayerType(this.mChildView, 0, null);
        SlidingPaneLayout.this.invalidateChildRegion(this.mChildView);
      } 
      SlidingPaneLayout.this.mPostedRunnables.remove(this);
    }
  }
  
  private class DragHelperCallback extends ViewDragHelper.Callback {
    public int clampViewPositionHorizontal(View param1View, int param1Int1, int param1Int2) {
      SlidingPaneLayout.LayoutParams layoutParams = (SlidingPaneLayout.LayoutParams)SlidingPaneLayout.this.mSlideableView.getLayoutParams();
      if (SlidingPaneLayout.this.isLayoutRtlSupport()) {
        int j = SlidingPaneLayout.this.getWidth() - SlidingPaneLayout.this.getPaddingRight() + layoutParams.rightMargin + SlidingPaneLayout.this.mSlideableView.getWidth();
        param1Int2 = SlidingPaneLayout.this.mSlideRange;
        return Math.max(Math.min(param1Int1, j), j - param1Int2);
      } 
      int i = SlidingPaneLayout.this.getPaddingLeft() + layoutParams.leftMargin;
      param1Int2 = SlidingPaneLayout.this.mSlideRange;
      return Math.min(Math.max(param1Int1, i), i + param1Int2);
    }
    
    public int clampViewPositionVertical(View param1View, int param1Int1, int param1Int2) {
      return param1View.getTop();
    }
    
    public int getViewHorizontalDragRange(View param1View) {
      return SlidingPaneLayout.this.mSlideRange;
    }
    
    public void onEdgeDragStarted(int param1Int1, int param1Int2) {
      SlidingPaneLayout.this.mDragHelper.captureChildView(SlidingPaneLayout.this.mSlideableView, param1Int2);
    }
    
    public void onViewCaptured(View param1View, int param1Int) {
      SlidingPaneLayout.this.setAllChildrenVisible();
    }
    
    public void onViewDragStateChanged(int param1Int) {
      if (SlidingPaneLayout.this.mDragHelper.getViewDragState() == 0) {
        if (SlidingPaneLayout.this.mSlideOffset == 0.0F) {
          SlidingPaneLayout.this.updateObscuredViewsVisibility(SlidingPaneLayout.this.mSlideableView);
          SlidingPaneLayout.this.dispatchOnPanelClosed(SlidingPaneLayout.this.mSlideableView);
          SlidingPaneLayout.this.mPreservedOpenState = false;
          return;
        } 
      } else {
        return;
      } 
      SlidingPaneLayout.this.dispatchOnPanelOpened(SlidingPaneLayout.this.mSlideableView);
      SlidingPaneLayout.this.mPreservedOpenState = true;
    }
    
    public void onViewPositionChanged(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      SlidingPaneLayout.this.onPanelDragged(param1Int1);
      SlidingPaneLayout.this.invalidate();
    }
    
    public void onViewReleased(View param1View, float param1Float1, float param1Float2) {
      // Byte code:
      //   0: aload_1
      //   1: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
      //   4: checkcast android/support/v4/widget/SlidingPaneLayout$LayoutParams
      //   7: astore #4
      //   9: aload_0
      //   10: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   13: invokevirtual isLayoutRtlSupport : ()Z
      //   16: ifeq -> 131
      //   19: aload_0
      //   20: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   23: invokevirtual getPaddingRight : ()I
      //   26: aload #4
      //   28: getfield rightMargin : I
      //   31: iadd
      //   32: istore #5
      //   34: fload_2
      //   35: fconst_0
      //   36: fcmpg
      //   37: iflt -> 67
      //   40: iload #5
      //   42: istore #6
      //   44: fload_2
      //   45: fconst_0
      //   46: fcmpl
      //   47: ifne -> 79
      //   50: iload #5
      //   52: istore #6
      //   54: aload_0
      //   55: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   58: getfield mSlideOffset : F
      //   61: ldc 0.5
      //   63: fcmpl
      //   64: ifle -> 79
      //   67: iload #5
      //   69: aload_0
      //   70: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   73: getfield mSlideRange : I
      //   76: iadd
      //   77: istore #6
      //   79: aload_0
      //   80: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   83: getfield mSlideableView : Landroid/view/View;
      //   86: invokevirtual getWidth : ()I
      //   89: istore #5
      //   91: aload_0
      //   92: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   95: invokevirtual getWidth : ()I
      //   98: iload #6
      //   100: isub
      //   101: iload #5
      //   103: isub
      //   104: istore #6
      //   106: aload_0
      //   107: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   110: getfield mDragHelper : Landroid/support/v4/widget/ViewDragHelper;
      //   113: iload #6
      //   115: aload_1
      //   116: invokevirtual getTop : ()I
      //   119: invokevirtual settleCapturedViewAt : (II)Z
      //   122: pop
      //   123: aload_0
      //   124: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   127: invokevirtual invalidate : ()V
      //   130: return
      //   131: aload_0
      //   132: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   135: invokevirtual getPaddingLeft : ()I
      //   138: aload #4
      //   140: getfield leftMargin : I
      //   143: iadd
      //   144: istore #5
      //   146: fload_2
      //   147: fconst_0
      //   148: fcmpl
      //   149: ifgt -> 179
      //   152: iload #5
      //   154: istore #6
      //   156: fload_2
      //   157: fconst_0
      //   158: fcmpl
      //   159: ifne -> 106
      //   162: iload #5
      //   164: istore #6
      //   166: aload_0
      //   167: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   170: getfield mSlideOffset : F
      //   173: ldc 0.5
      //   175: fcmpl
      //   176: ifle -> 106
      //   179: iload #5
      //   181: aload_0
      //   182: getfield this$0 : Landroid/support/v4/widget/SlidingPaneLayout;
      //   185: getfield mSlideRange : I
      //   188: iadd
      //   189: istore #6
      //   191: goto -> 106
    }
    
    public boolean tryCaptureView(View param1View, int param1Int) {
      return SlidingPaneLayout.this.mIsUnableToDrag ? false : ((SlidingPaneLayout.LayoutParams)param1View.getLayoutParams()).slideable;
    }
  }
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    private static final int[] ATTRS = new int[] { 16843137 };
    
    Paint dimPaint;
    
    boolean dimWhenOffset;
    
    boolean slideable;
    
    public float weight = 0.0F;
    
    public LayoutParams() {
      super(-1, -1);
    }
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, ATTRS);
      this.weight = typedArray.getFloat(0, 0.0F);
      typedArray.recycle();
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.weight = param1LayoutParams.weight;
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
  }
  
  public static interface PanelSlideListener {
    void onPanelClosed(View param1View);
    
    void onPanelOpened(View param1View);
    
    void onPanelSlide(View param1View, float param1Float);
  }
  
  static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
          public SlidingPaneLayout.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
            return new SlidingPaneLayout.SavedState(param2Parcel, param2ClassLoader);
          }
          
          public SlidingPaneLayout.SavedState[] newArray(int param2Int) {
            return new SlidingPaneLayout.SavedState[param2Int];
          }
        });
    
    boolean isOpen;
    
    SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      boolean bool;
      if (param1Parcel.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      this.isOpen = bool;
    }
    
    SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      if (this.isOpen) {
        param1Int = 1;
      } else {
        param1Int = 0;
      } 
      param1Parcel.writeInt(param1Int);
    }
  }
  
  static final class null implements ParcelableCompatCreatorCallbacks<SavedState> {
    public SlidingPaneLayout.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new SlidingPaneLayout.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public SlidingPaneLayout.SavedState[] newArray(int param1Int) {
      return new SlidingPaneLayout.SavedState[param1Int];
    }
  }
  
  public static class SimplePanelSlideListener implements PanelSlideListener {
    public void onPanelClosed(View param1View) {}
    
    public void onPanelOpened(View param1View) {}
    
    public void onPanelSlide(View param1View, float param1Float) {}
  }
  
  static interface SlidingPanelLayoutImpl {
    void invalidateChildRegion(SlidingPaneLayout param1SlidingPaneLayout, View param1View);
  }
  
  static class SlidingPanelLayoutImplBase implements SlidingPanelLayoutImpl {
    public void invalidateChildRegion(SlidingPaneLayout param1SlidingPaneLayout, View param1View) {
      ViewCompat.postInvalidateOnAnimation((View)param1SlidingPaneLayout, param1View.getLeft(), param1View.getTop(), param1View.getRight(), param1View.getBottom());
    }
  }
  
  static class SlidingPanelLayoutImplJB extends SlidingPanelLayoutImplBase {
    private Method mGetDisplayList;
    
    private Field mRecreateDisplayList;
    
    SlidingPanelLayoutImplJB() {
      try {
        this.mGetDisplayList = View.class.getDeclaredMethod("getDisplayList", (Class[])null);
      } catch (NoSuchMethodException noSuchMethodException) {
        Log.e("SlidingPaneLayout", "Couldn't fetch getDisplayList method; dimming won't work right.", noSuchMethodException);
      } 
      try {
        this.mRecreateDisplayList = View.class.getDeclaredField("mRecreateDisplayList");
        this.mRecreateDisplayList.setAccessible(true);
      } catch (NoSuchFieldException noSuchFieldException) {
        Log.e("SlidingPaneLayout", "Couldn't fetch mRecreateDisplayList field; dimming will be slow.", noSuchFieldException);
      } 
    }
    
    public void invalidateChildRegion(SlidingPaneLayout param1SlidingPaneLayout, View param1View) {
      if (this.mGetDisplayList != null && this.mRecreateDisplayList != null) {
        try {
          this.mRecreateDisplayList.setBoolean(param1View, true);
          this.mGetDisplayList.invoke(param1View, (Object[])null);
        } catch (Exception exception) {
          Log.e("SlidingPaneLayout", "Error refreshing display list state", exception);
        } 
        super.invalidateChildRegion(param1SlidingPaneLayout, param1View);
        return;
      } 
      param1View.invalidate();
    }
  }
  
  static class SlidingPanelLayoutImplJBMR1 extends SlidingPanelLayoutImplBase {
    public void invalidateChildRegion(SlidingPaneLayout param1SlidingPaneLayout, View param1View) {
      ViewCompat.setLayerPaint(param1View, ((SlidingPaneLayout.LayoutParams)param1View.getLayoutParams()).dimPaint);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/widget/SlidingPaneLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */