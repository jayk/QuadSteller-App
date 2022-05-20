package android.support.design.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.design.R;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.appcompat.R;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

public class CollapsingToolbarLayout extends FrameLayout {
  private static final int DEFAULT_SCRIM_ANIMATION_DURATION = 600;
  
  final CollapsingTextHelper mCollapsingTextHelper;
  
  private boolean mCollapsingTitleEnabled;
  
  private Drawable mContentScrim;
  
  int mCurrentOffset;
  
  private boolean mDrawCollapsingTitle;
  
  private View mDummyView;
  
  private int mExpandedMarginBottom;
  
  private int mExpandedMarginEnd;
  
  private int mExpandedMarginStart;
  
  private int mExpandedMarginTop;
  
  WindowInsetsCompat mLastInsets;
  
  private AppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener;
  
  private boolean mRefreshToolbar = true;
  
  private int mScrimAlpha;
  
  private long mScrimAnimationDuration;
  
  private ValueAnimatorCompat mScrimAnimator;
  
  private int mScrimVisibleHeightTrigger = -1;
  
  private boolean mScrimsAreShown;
  
  Drawable mStatusBarScrim;
  
  private final Rect mTmpRect = new Rect();
  
  private Toolbar mToolbar;
  
  private View mToolbarDirectChild;
  
  private int mToolbarId;
  
  public CollapsingToolbarLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public CollapsingToolbarLayout(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public CollapsingToolbarLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    ThemeUtils.checkAppCompatTheme(paramContext);
    this.mCollapsingTextHelper = new CollapsingTextHelper((View)this);
    this.mCollapsingTextHelper.setTextSizeInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CollapsingToolbarLayout, paramInt, R.style.Widget_Design_CollapsingToolbar);
    this.mCollapsingTextHelper.setExpandedTextGravity(typedArray.getInt(R.styleable.CollapsingToolbarLayout_expandedTitleGravity, 8388691));
    this.mCollapsingTextHelper.setCollapsedTextGravity(typedArray.getInt(R.styleable.CollapsingToolbarLayout_collapsedTitleGravity, 8388627));
    paramInt = typedArray.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_expandedTitleMargin, 0);
    this.mExpandedMarginBottom = paramInt;
    this.mExpandedMarginEnd = paramInt;
    this.mExpandedMarginTop = paramInt;
    this.mExpandedMarginStart = paramInt;
    if (typedArray.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginStart))
      this.mExpandedMarginStart = typedArray.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_expandedTitleMarginStart, 0); 
    if (typedArray.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginEnd))
      this.mExpandedMarginEnd = typedArray.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_expandedTitleMarginEnd, 0); 
    if (typedArray.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginTop))
      this.mExpandedMarginTop = typedArray.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_expandedTitleMarginTop, 0); 
    if (typedArray.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginBottom))
      this.mExpandedMarginBottom = typedArray.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_expandedTitleMarginBottom, 0); 
    this.mCollapsingTitleEnabled = typedArray.getBoolean(R.styleable.CollapsingToolbarLayout_titleEnabled, true);
    setTitle(typedArray.getText(R.styleable.CollapsingToolbarLayout_title));
    this.mCollapsingTextHelper.setExpandedTextAppearance(R.style.TextAppearance_Design_CollapsingToolbar_Expanded);
    this.mCollapsingTextHelper.setCollapsedTextAppearance(R.style.TextAppearance_AppCompat_Widget_ActionBar_Title);
    if (typedArray.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleTextAppearance))
      this.mCollapsingTextHelper.setExpandedTextAppearance(typedArray.getResourceId(R.styleable.CollapsingToolbarLayout_expandedTitleTextAppearance, 0)); 
    if (typedArray.hasValue(R.styleable.CollapsingToolbarLayout_collapsedTitleTextAppearance))
      this.mCollapsingTextHelper.setCollapsedTextAppearance(typedArray.getResourceId(R.styleable.CollapsingToolbarLayout_collapsedTitleTextAppearance, 0)); 
    this.mScrimVisibleHeightTrigger = typedArray.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_scrimVisibleHeightTrigger, -1);
    this.mScrimAnimationDuration = typedArray.getInt(R.styleable.CollapsingToolbarLayout_scrimAnimationDuration, 600);
    setContentScrim(typedArray.getDrawable(R.styleable.CollapsingToolbarLayout_contentScrim));
    setStatusBarScrim(typedArray.getDrawable(R.styleable.CollapsingToolbarLayout_statusBarScrim));
    this.mToolbarId = typedArray.getResourceId(R.styleable.CollapsingToolbarLayout_toolbarId, -1);
    typedArray.recycle();
    setWillNotDraw(false);
    ViewCompat.setOnApplyWindowInsetsListener((View)this, new OnApplyWindowInsetsListener() {
          public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat) {
            return CollapsingToolbarLayout.this.onWindowInsetChanged(param1WindowInsetsCompat);
          }
        });
  }
  
  private void animateScrim(int paramInt) {
    ensureToolbar();
    if (this.mScrimAnimator == null) {
      Interpolator interpolator;
      this.mScrimAnimator = ViewUtils.createAnimator();
      this.mScrimAnimator.setDuration(this.mScrimAnimationDuration);
      ValueAnimatorCompat valueAnimatorCompat = this.mScrimAnimator;
      if (paramInt > this.mScrimAlpha) {
        interpolator = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
      } else {
        interpolator = AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR;
      } 
      valueAnimatorCompat.setInterpolator(interpolator);
      this.mScrimAnimator.addUpdateListener(new ValueAnimatorCompat.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimatorCompat param1ValueAnimatorCompat) {
              CollapsingToolbarLayout.this.setScrimAlpha(param1ValueAnimatorCompat.getAnimatedIntValue());
            }
          });
    } else if (this.mScrimAnimator.isRunning()) {
      this.mScrimAnimator.cancel();
    } 
    this.mScrimAnimator.setIntValues(this.mScrimAlpha, paramInt);
    this.mScrimAnimator.start();
  }
  
  private void ensureToolbar() {
    if (!this.mRefreshToolbar)
      return; 
    this.mToolbar = null;
    this.mToolbarDirectChild = null;
    if (this.mToolbarId != -1) {
      this.mToolbar = (Toolbar)findViewById(this.mToolbarId);
      if (this.mToolbar != null)
        this.mToolbarDirectChild = findDirectChild((View)this.mToolbar); 
    } 
    if (this.mToolbar == null) {
      View view = null;
      byte b = 0;
      int i = getChildCount();
      while (true) {
        Toolbar toolbar;
        View view1 = view;
        if (b < i) {
          view1 = getChildAt(b);
          if (view1 instanceof Toolbar) {
            toolbar = (Toolbar)view1;
          } else {
            b++;
            continue;
          } 
        } 
        this.mToolbar = toolbar;
        updateDummyView();
        this.mRefreshToolbar = false;
        return;
      } 
    } 
    updateDummyView();
    this.mRefreshToolbar = false;
  }
  
  private View findDirectChild(View paramView) {
    View view = paramView;
    for (ViewParent viewParent = paramView.getParent(); viewParent != this && viewParent != null; viewParent = viewParent.getParent()) {
      if (viewParent instanceof View)
        view = (View)viewParent; 
    } 
    return view;
  }
  
  private static int getHeightWithMargins(@NonNull View paramView) {
    ViewGroup.LayoutParams layoutParams = paramView.getLayoutParams();
    if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
      ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)layoutParams;
      return paramView.getHeight() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
    } 
    return paramView.getHeight();
  }
  
  static ViewOffsetHelper getViewOffsetHelper(View paramView) {
    ViewOffsetHelper viewOffsetHelper1 = (ViewOffsetHelper)paramView.getTag(R.id.view_offset_helper);
    ViewOffsetHelper viewOffsetHelper2 = viewOffsetHelper1;
    if (viewOffsetHelper1 == null) {
      viewOffsetHelper2 = new ViewOffsetHelper(paramView);
      paramView.setTag(R.id.view_offset_helper, viewOffsetHelper2);
    } 
    return viewOffsetHelper2;
  }
  
  private boolean isToolbarChild(View paramView) {
    boolean bool = true;
    if (this.mToolbarDirectChild == null || this.mToolbarDirectChild == this) {
      if (paramView != this.mToolbar)
        bool = false; 
      return bool;
    } 
    if (paramView != this.mToolbarDirectChild)
      bool = false; 
    return bool;
  }
  
  private void updateDummyView() {
    if (!this.mCollapsingTitleEnabled && this.mDummyView != null) {
      ViewParent viewParent = this.mDummyView.getParent();
      if (viewParent instanceof ViewGroup)
        ((ViewGroup)viewParent).removeView(this.mDummyView); 
    } 
    if (this.mCollapsingTitleEnabled && this.mToolbar != null) {
      if (this.mDummyView == null)
        this.mDummyView = new View(getContext()); 
      if (this.mDummyView.getParent() == null)
        this.mToolbar.addView(this.mDummyView, -1, -1); 
    } 
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  public void draw(Canvas paramCanvas) {
    super.draw(paramCanvas);
    ensureToolbar();
    if (this.mToolbar == null && this.mContentScrim != null && this.mScrimAlpha > 0) {
      this.mContentScrim.mutate().setAlpha(this.mScrimAlpha);
      this.mContentScrim.draw(paramCanvas);
    } 
    if (this.mCollapsingTitleEnabled && this.mDrawCollapsingTitle)
      this.mCollapsingTextHelper.draw(paramCanvas); 
    if (this.mStatusBarScrim != null && this.mScrimAlpha > 0) {
      byte b;
      if (this.mLastInsets != null) {
        b = this.mLastInsets.getSystemWindowInsetTop();
      } else {
        b = 0;
      } 
      if (b) {
        this.mStatusBarScrim.setBounds(0, -this.mCurrentOffset, getWidth(), b - this.mCurrentOffset);
        this.mStatusBarScrim.mutate().setAlpha(this.mScrimAlpha);
        this.mStatusBarScrim.draw(paramCanvas);
      } 
    } 
  }
  
  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong) {
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (this.mContentScrim != null) {
      bool2 = bool1;
      if (this.mScrimAlpha > 0) {
        bool2 = bool1;
        if (isToolbarChild(paramView)) {
          this.mContentScrim.mutate().setAlpha(this.mScrimAlpha);
          this.mContentScrim.draw(paramCanvas);
          bool2 = true;
        } 
      } 
    } 
    return (super.drawChild(paramCanvas, paramView, paramLong) || bool2);
  }
  
  protected void drawableStateChanged() {
    boolean bool1;
    super.drawableStateChanged();
    int[] arrayOfInt = getDrawableState();
    int i = 0;
    Drawable drawable = this.mStatusBarScrim;
    int j = i;
    if (drawable != null) {
      j = i;
      if (drawable.isStateful())
        j = false | drawable.setState(arrayOfInt); 
    } 
    drawable = this.mContentScrim;
    i = j;
    if (drawable != null) {
      i = j;
      if (drawable.isStateful())
        bool1 = j | drawable.setState(arrayOfInt); 
    } 
    boolean bool2 = bool1;
    if (this.mCollapsingTextHelper != null)
      bool2 = bool1 | this.mCollapsingTextHelper.setState(arrayOfInt); 
    if (bool2)
      invalidate(); 
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams(-1, -1);
  }
  
  public FrameLayout.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected FrameLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return new LayoutParams(paramLayoutParams);
  }
  
  public int getCollapsedTitleGravity() {
    return this.mCollapsingTextHelper.getCollapsedTextGravity();
  }
  
  @NonNull
  public Typeface getCollapsedTitleTypeface() {
    return this.mCollapsingTextHelper.getCollapsedTypeface();
  }
  
  @Nullable
  public Drawable getContentScrim() {
    return this.mContentScrim;
  }
  
  public int getExpandedTitleGravity() {
    return this.mCollapsingTextHelper.getExpandedTextGravity();
  }
  
  public int getExpandedTitleMarginBottom() {
    return this.mExpandedMarginBottom;
  }
  
  public int getExpandedTitleMarginEnd() {
    return this.mExpandedMarginEnd;
  }
  
  public int getExpandedTitleMarginStart() {
    return this.mExpandedMarginStart;
  }
  
  public int getExpandedTitleMarginTop() {
    return this.mExpandedMarginTop;
  }
  
  @NonNull
  public Typeface getExpandedTitleTypeface() {
    return this.mCollapsingTextHelper.getExpandedTypeface();
  }
  
  final int getMaxOffsetForPinChild(View paramView) {
    ViewOffsetHelper viewOffsetHelper = getViewOffsetHelper(paramView);
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    return getHeight() - viewOffsetHelper.getLayoutTop() - paramView.getHeight() - layoutParams.bottomMargin;
  }
  
  int getScrimAlpha() {
    return this.mScrimAlpha;
  }
  
  public long getScrimAnimationDuration() {
    return this.mScrimAnimationDuration;
  }
  
  public int getScrimVisibleHeightTrigger() {
    if (this.mScrimVisibleHeightTrigger >= 0)
      return this.mScrimVisibleHeightTrigger; 
    if (this.mLastInsets != null) {
      null = this.mLastInsets.getSystemWindowInsetTop();
    } else {
      null = 0;
    } 
    int i = ViewCompat.getMinimumHeight((View)this);
    return (i > 0) ? Math.min(i * 2 + null, getHeight()) : (getHeight() / 3);
  }
  
  @Nullable
  public Drawable getStatusBarScrim() {
    return this.mStatusBarScrim;
  }
  
  @Nullable
  public CharSequence getTitle() {
    return this.mCollapsingTitleEnabled ? this.mCollapsingTextHelper.getText() : null;
  }
  
  public boolean isTitleEnabled() {
    return this.mCollapsingTitleEnabled;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    ViewParent viewParent = getParent();
    if (viewParent instanceof AppBarLayout) {
      ViewCompat.setFitsSystemWindows((View)this, ViewCompat.getFitsSystemWindows((View)viewParent));
      if (this.mOnOffsetChangedListener == null)
        this.mOnOffsetChangedListener = new OffsetUpdateListener(); 
      ((AppBarLayout)viewParent).addOnOffsetChangedListener(this.mOnOffsetChangedListener);
      ViewCompat.requestApplyInsets((View)this);
    } 
  }
  
  protected void onDetachedFromWindow() {
    ViewParent viewParent = getParent();
    if (this.mOnOffsetChangedListener != null && viewParent instanceof AppBarLayout)
      ((AppBarLayout)viewParent).removeOnOffsetChangedListener(this.mOnOffsetChangedListener); 
    super.onDetachedFromWindow();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (this.mLastInsets != null) {
      int i = this.mLastInsets.getSystemWindowInsetTop();
      byte b = 0;
      int j = getChildCount();
      while (b < j) {
        View view = getChildAt(b);
        if (!ViewCompat.getFitsSystemWindows(view) && view.getTop() < i)
          ViewCompat.offsetTopAndBottom(view, i); 
        b++;
      } 
    } 
    if (this.mCollapsingTitleEnabled && this.mDummyView != null) {
      if (ViewCompat.isAttachedToWindow(this.mDummyView) && this.mDummyView.getVisibility() == 0) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      } 
      this.mDrawCollapsingTitle = paramBoolean;
      if (this.mDrawCollapsingTitle) {
        int i;
        int j;
        Toolbar toolbar;
        if (ViewCompat.getLayoutDirection((View)this) == 1) {
          j = 1;
        } else {
          j = 0;
        } 
        if (this.mToolbarDirectChild != null) {
          View view = this.mToolbarDirectChild;
        } else {
          toolbar = this.mToolbar;
        } 
        int m = getMaxOffsetForPinChild((View)toolbar);
        ViewGroupUtils.getDescendantRect((ViewGroup)this, this.mDummyView, this.mTmpRect);
        CollapsingTextHelper collapsingTextHelper = this.mCollapsingTextHelper;
        int n = this.mTmpRect.left;
        if (j) {
          i = this.mToolbar.getTitleMarginEnd();
        } else {
          i = this.mToolbar.getTitleMarginStart();
        } 
        int i1 = this.mTmpRect.top;
        int i2 = this.mToolbar.getTitleMarginTop();
        int i3 = this.mTmpRect.right;
        if (j) {
          k = this.mToolbar.getTitleMarginStart();
        } else {
          k = this.mToolbar.getTitleMarginEnd();
        } 
        collapsingTextHelper.setCollapsedBounds(n + i, i2 + i1 + m, k + i3, this.mTmpRect.bottom + m - this.mToolbar.getTitleMarginBottom());
        collapsingTextHelper = this.mCollapsingTextHelper;
        if (j) {
          i = this.mExpandedMarginEnd;
        } else {
          i = this.mExpandedMarginStart;
        } 
        int k = this.mTmpRect.top;
        m = this.mExpandedMarginTop;
        if (j) {
          j = this.mExpandedMarginStart;
        } else {
          j = this.mExpandedMarginEnd;
        } 
        collapsingTextHelper.setExpandedBounds(i, m + k, paramInt3 - paramInt1 - j, paramInt4 - paramInt2 - this.mExpandedMarginBottom);
        this.mCollapsingTextHelper.recalculate();
      } 
    } 
    paramInt1 = 0;
    paramInt2 = getChildCount();
    while (paramInt1 < paramInt2) {
      getViewOffsetHelper(getChildAt(paramInt1)).onViewLayout();
      paramInt1++;
    } 
    if (this.mToolbar != null) {
      if (this.mCollapsingTitleEnabled && TextUtils.isEmpty(this.mCollapsingTextHelper.getText()))
        this.mCollapsingTextHelper.setText(this.mToolbar.getTitle()); 
      if (this.mToolbarDirectChild == null || this.mToolbarDirectChild == this) {
        setMinimumHeight(getHeightWithMargins((View)this.mToolbar));
      } else {
        setMinimumHeight(getHeightWithMargins(this.mToolbarDirectChild));
      } 
    } 
    updateScrimVisibility();
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    ensureToolbar();
    super.onMeasure(paramInt1, paramInt2);
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (this.mContentScrim != null)
      this.mContentScrim.setBounds(0, 0, paramInt1, paramInt2); 
  }
  
  WindowInsetsCompat onWindowInsetChanged(WindowInsetsCompat paramWindowInsetsCompat) {
    WindowInsetsCompat windowInsetsCompat = null;
    if (ViewCompat.getFitsSystemWindows((View)this))
      windowInsetsCompat = paramWindowInsetsCompat; 
    if (!ViewUtils.objectEquals(this.mLastInsets, windowInsetsCompat)) {
      this.mLastInsets = windowInsetsCompat;
      requestLayout();
    } 
    return paramWindowInsetsCompat.consumeSystemWindowInsets();
  }
  
  public void setCollapsedTitleGravity(int paramInt) {
    this.mCollapsingTextHelper.setCollapsedTextGravity(paramInt);
  }
  
  public void setCollapsedTitleTextAppearance(@StyleRes int paramInt) {
    this.mCollapsingTextHelper.setCollapsedTextAppearance(paramInt);
  }
  
  public void setCollapsedTitleTextColor(@ColorInt int paramInt) {
    setCollapsedTitleTextColor(ColorStateList.valueOf(paramInt));
  }
  
  public void setCollapsedTitleTextColor(@NonNull ColorStateList paramColorStateList) {
    this.mCollapsingTextHelper.setCollapsedTextColor(paramColorStateList);
  }
  
  public void setCollapsedTitleTypeface(@Nullable Typeface paramTypeface) {
    this.mCollapsingTextHelper.setCollapsedTypeface(paramTypeface);
  }
  
  public void setContentScrim(@Nullable Drawable paramDrawable) {
    Drawable drawable = null;
    if (this.mContentScrim != paramDrawable) {
      if (this.mContentScrim != null)
        this.mContentScrim.setCallback(null); 
      if (paramDrawable != null)
        drawable = paramDrawable.mutate(); 
      this.mContentScrim = drawable;
      if (this.mContentScrim != null) {
        this.mContentScrim.setBounds(0, 0, getWidth(), getHeight());
        this.mContentScrim.setCallback((Drawable.Callback)this);
        this.mContentScrim.setAlpha(this.mScrimAlpha);
      } 
      ViewCompat.postInvalidateOnAnimation((View)this);
    } 
  }
  
  public void setContentScrimColor(@ColorInt int paramInt) {
    setContentScrim((Drawable)new ColorDrawable(paramInt));
  }
  
  public void setContentScrimResource(@DrawableRes int paramInt) {
    setContentScrim(ContextCompat.getDrawable(getContext(), paramInt));
  }
  
  public void setExpandedTitleColor(@ColorInt int paramInt) {
    setExpandedTitleTextColor(ColorStateList.valueOf(paramInt));
  }
  
  public void setExpandedTitleGravity(int paramInt) {
    this.mCollapsingTextHelper.setExpandedTextGravity(paramInt);
  }
  
  public void setExpandedTitleMargin(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mExpandedMarginStart = paramInt1;
    this.mExpandedMarginTop = paramInt2;
    this.mExpandedMarginEnd = paramInt3;
    this.mExpandedMarginBottom = paramInt4;
    requestLayout();
  }
  
  public void setExpandedTitleMarginBottom(int paramInt) {
    this.mExpandedMarginBottom = paramInt;
    requestLayout();
  }
  
  public void setExpandedTitleMarginEnd(int paramInt) {
    this.mExpandedMarginEnd = paramInt;
    requestLayout();
  }
  
  public void setExpandedTitleMarginStart(int paramInt) {
    this.mExpandedMarginStart = paramInt;
    requestLayout();
  }
  
  public void setExpandedTitleMarginTop(int paramInt) {
    this.mExpandedMarginTop = paramInt;
    requestLayout();
  }
  
  public void setExpandedTitleTextAppearance(@StyleRes int paramInt) {
    this.mCollapsingTextHelper.setExpandedTextAppearance(paramInt);
  }
  
  public void setExpandedTitleTextColor(@NonNull ColorStateList paramColorStateList) {
    this.mCollapsingTextHelper.setExpandedTextColor(paramColorStateList);
  }
  
  public void setExpandedTitleTypeface(@Nullable Typeface paramTypeface) {
    this.mCollapsingTextHelper.setExpandedTypeface(paramTypeface);
  }
  
  void setScrimAlpha(int paramInt) {
    if (paramInt != this.mScrimAlpha) {
      if (this.mContentScrim != null && this.mToolbar != null)
        ViewCompat.postInvalidateOnAnimation((View)this.mToolbar); 
      this.mScrimAlpha = paramInt;
      ViewCompat.postInvalidateOnAnimation((View)this);
    } 
  }
  
  public void setScrimAnimationDuration(@IntRange(from = 0L) long paramLong) {
    this.mScrimAnimationDuration = paramLong;
  }
  
  public void setScrimVisibleHeightTrigger(@IntRange(from = 0L) int paramInt) {
    if (this.mScrimVisibleHeightTrigger != paramInt) {
      this.mScrimVisibleHeightTrigger = paramInt;
      updateScrimVisibility();
    } 
  }
  
  public void setScrimsShown(boolean paramBoolean) {
    boolean bool;
    if (ViewCompat.isLaidOut((View)this) && !isInEditMode()) {
      bool = true;
    } else {
      bool = false;
    } 
    setScrimsShown(paramBoolean, bool);
  }
  
  public void setScrimsShown(boolean paramBoolean1, boolean paramBoolean2) {
    char c = 'ÿ';
    if (this.mScrimsAreShown != paramBoolean1) {
      if (paramBoolean2) {
        if (!paramBoolean1)
          c = Character.MIN_VALUE; 
        animateScrim(c);
      } else {
        if (!paramBoolean1)
          c = Character.MIN_VALUE; 
        setScrimAlpha(c);
      } 
      this.mScrimsAreShown = paramBoolean1;
    } 
  }
  
  public void setStatusBarScrim(@Nullable Drawable paramDrawable) {
    Drawable drawable = null;
    if (this.mStatusBarScrim != paramDrawable) {
      if (this.mStatusBarScrim != null)
        this.mStatusBarScrim.setCallback(null); 
      if (paramDrawable != null)
        drawable = paramDrawable.mutate(); 
      this.mStatusBarScrim = drawable;
      if (this.mStatusBarScrim != null) {
        boolean bool;
        if (this.mStatusBarScrim.isStateful())
          this.mStatusBarScrim.setState(getDrawableState()); 
        DrawableCompat.setLayoutDirection(this.mStatusBarScrim, ViewCompat.getLayoutDirection((View)this));
        paramDrawable = this.mStatusBarScrim;
        if (getVisibility() == 0) {
          bool = true;
        } else {
          bool = false;
        } 
        paramDrawable.setVisible(bool, false);
        this.mStatusBarScrim.setCallback((Drawable.Callback)this);
        this.mStatusBarScrim.setAlpha(this.mScrimAlpha);
      } 
      ViewCompat.postInvalidateOnAnimation((View)this);
    } 
  }
  
  public void setStatusBarScrimColor(@ColorInt int paramInt) {
    setStatusBarScrim((Drawable)new ColorDrawable(paramInt));
  }
  
  public void setStatusBarScrimResource(@DrawableRes int paramInt) {
    setStatusBarScrim(ContextCompat.getDrawable(getContext(), paramInt));
  }
  
  public void setTitle(@Nullable CharSequence paramCharSequence) {
    this.mCollapsingTextHelper.setText(paramCharSequence);
  }
  
  public void setTitleEnabled(boolean paramBoolean) {
    if (paramBoolean != this.mCollapsingTitleEnabled) {
      this.mCollapsingTitleEnabled = paramBoolean;
      updateDummyView();
      requestLayout();
    } 
  }
  
  public void setVisibility(int paramInt) {
    boolean bool;
    super.setVisibility(paramInt);
    if (paramInt == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    if (this.mStatusBarScrim != null && this.mStatusBarScrim.isVisible() != bool)
      this.mStatusBarScrim.setVisible(bool, false); 
    if (this.mContentScrim != null && this.mContentScrim.isVisible() != bool)
      this.mContentScrim.setVisible(bool, false); 
  }
  
  final void updateScrimVisibility() {
    if (this.mContentScrim != null || this.mStatusBarScrim != null) {
      boolean bool;
      if (getHeight() + this.mCurrentOffset < getScrimVisibleHeightTrigger()) {
        bool = true;
      } else {
        bool = false;
      } 
      setScrimsShown(bool);
    } 
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable) {
    return (super.verifyDrawable(paramDrawable) || paramDrawable == this.mContentScrim || paramDrawable == this.mStatusBarScrim);
  }
  
  public static class LayoutParams extends FrameLayout.LayoutParams {
    public static final int COLLAPSE_MODE_OFF = 0;
    
    public static final int COLLAPSE_MODE_PARALLAX = 2;
    
    public static final int COLLAPSE_MODE_PIN = 1;
    
    private static final float DEFAULT_PARALLAX_MULTIPLIER = 0.5F;
    
    int mCollapseMode = 0;
    
    float mParallaxMult = 0.5F;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(int param1Int1, int param1Int2, int param1Int3) {
      super(param1Int1, param1Int2, param1Int3);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.CollapsingToolbarLayout_Layout);
      this.mCollapseMode = typedArray.getInt(R.styleable.CollapsingToolbarLayout_Layout_layout_collapseMode, 0);
      setParallaxMultiplier(typedArray.getFloat(R.styleable.CollapsingToolbarLayout_Layout_layout_collapseParallaxMultiplier, 0.5F));
      typedArray.recycle();
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
    
    @TargetApi(19)
    @RequiresApi(19)
    public LayoutParams(FrameLayout.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public int getCollapseMode() {
      return this.mCollapseMode;
    }
    
    public float getParallaxMultiplier() {
      return this.mParallaxMult;
    }
    
    public void setCollapseMode(int param1Int) {
      this.mCollapseMode = param1Int;
    }
    
    public void setParallaxMultiplier(float param1Float) {
      this.mParallaxMult = param1Float;
    }
  }
  
  private class OffsetUpdateListener implements AppBarLayout.OnOffsetChangedListener {
    public void onOffsetChanged(AppBarLayout param1AppBarLayout, int param1Int) {
      byte b;
      CollapsingToolbarLayout.this.mCurrentOffset = param1Int;
      if (CollapsingToolbarLayout.this.mLastInsets != null) {
        b = CollapsingToolbarLayout.this.mLastInsets.getSystemWindowInsetTop();
      } else {
        b = 0;
      } 
      int i = 0;
      int j = CollapsingToolbarLayout.this.getChildCount();
      while (i < j) {
        View view = CollapsingToolbarLayout.this.getChildAt(i);
        CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams)view.getLayoutParams();
        ViewOffsetHelper viewOffsetHelper = CollapsingToolbarLayout.getViewOffsetHelper(view);
        switch (layoutParams.mCollapseMode) {
          case 1:
            viewOffsetHelper.setTopAndBottomOffset(MathUtils.constrain(-param1Int, 0, CollapsingToolbarLayout.this.getMaxOffsetForPinChild(view)));
            i++;
            break;
          case 2:
            viewOffsetHelper.setTopAndBottomOffset(Math.round(-param1Int * layoutParams.mParallaxMult));
            i++;
            break;
        } 
      } 
      CollapsingToolbarLayout.this.updateScrimVisibility();
      if (CollapsingToolbarLayout.this.mStatusBarScrim != null && b)
        ViewCompat.postInvalidateOnAnimation((View)CollapsingToolbarLayout.this); 
      j = CollapsingToolbarLayout.this.getHeight();
      i = ViewCompat.getMinimumHeight((View)CollapsingToolbarLayout.this);
      CollapsingToolbarLayout.this.mCollapsingTextHelper.setExpansionFraction(Math.abs(param1Int) / (j - i - b));
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/CollapsingToolbarLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */