package android.support.design.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.design.R;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

@DefaultBehavior(AppBarLayout.Behavior.class)
public class AppBarLayout extends LinearLayout {
  private static final int INVALID_SCROLL_RANGE = -1;
  
  static final int PENDING_ACTION_ANIMATE_ENABLED = 4;
  
  static final int PENDING_ACTION_COLLAPSED = 2;
  
  static final int PENDING_ACTION_EXPANDED = 1;
  
  static final int PENDING_ACTION_NONE = 0;
  
  private boolean mCollapsed;
  
  private boolean mCollapsible;
  
  private int mDownPreScrollRange = -1;
  
  private int mDownScrollRange = -1;
  
  private boolean mHaveChildWithInterpolator;
  
  private WindowInsetsCompat mLastInsets;
  
  private List<OnOffsetChangedListener> mListeners;
  
  private int mPendingAction = 0;
  
  private final int[] mTmpStatesArray = new int[2];
  
  private int mTotalScrollRange = -1;
  
  public AppBarLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public AppBarLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    setOrientation(1);
    ThemeUtils.checkAppCompatTheme(paramContext);
    if (Build.VERSION.SDK_INT >= 21) {
      ViewUtilsLollipop.setBoundsViewOutlineProvider((View)this);
      ViewUtilsLollipop.setStateListAnimatorFromAttrs((View)this, paramAttributeSet, 0, R.style.Widget_Design_AppBarLayout);
    } 
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.AppBarLayout, 0, R.style.Widget_Design_AppBarLayout);
    ViewCompat.setBackground((View)this, typedArray.getDrawable(R.styleable.AppBarLayout_android_background));
    if (typedArray.hasValue(R.styleable.AppBarLayout_expanded))
      setExpanded(typedArray.getBoolean(R.styleable.AppBarLayout_expanded, false)); 
    if (Build.VERSION.SDK_INT >= 21 && typedArray.hasValue(R.styleable.AppBarLayout_elevation))
      ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator((View)this, typedArray.getDimensionPixelSize(R.styleable.AppBarLayout_elevation, 0)); 
    typedArray.recycle();
    ViewCompat.setOnApplyWindowInsetsListener((View)this, new OnApplyWindowInsetsListener() {
          public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat) {
            return AppBarLayout.this.onWindowInsetChanged(param1WindowInsetsCompat);
          }
        });
  }
  
  private void invalidateScrollRanges() {
    this.mTotalScrollRange = -1;
    this.mDownPreScrollRange = -1;
    this.mDownScrollRange = -1;
  }
  
  private boolean setCollapsibleState(boolean paramBoolean) {
    if (this.mCollapsible != paramBoolean) {
      this.mCollapsible = paramBoolean;
      refreshDrawableState();
      return true;
    } 
    return false;
  }
  
  private void updateCollapsible() {
    boolean bool = false;
    byte b = 0;
    int i = getChildCount();
    while (true) {
      boolean bool1 = bool;
      if (b < i)
        if (((LayoutParams)getChildAt(b).getLayoutParams()).isCollapsible()) {
          bool1 = true;
        } else {
          b++;
          continue;
        }  
      setCollapsibleState(bool1);
      return;
    } 
  }
  
  public void addOnOffsetChangedListener(OnOffsetChangedListener paramOnOffsetChangedListener) {
    if (this.mListeners == null)
      this.mListeners = new ArrayList<OnOffsetChangedListener>(); 
    if (paramOnOffsetChangedListener != null && !this.mListeners.contains(paramOnOffsetChangedListener))
      this.mListeners.add(paramOnOffsetChangedListener); 
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  void dispatchOffsetUpdates(int paramInt) {
    if (this.mListeners != null) {
      byte b = 0;
      int i = this.mListeners.size();
      while (b < i) {
        OnOffsetChangedListener onOffsetChangedListener = this.mListeners.get(b);
        if (onOffsetChangedListener != null)
          onOffsetChangedListener.onOffsetChanged(this, paramInt); 
        b++;
      } 
    } 
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams(-1, -2);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (Build.VERSION.SDK_INT >= 19 && paramLayoutParams instanceof LinearLayout.LayoutParams) ? new LayoutParams((LinearLayout.LayoutParams)paramLayoutParams) : ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams) ? new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams) : new LayoutParams(paramLayoutParams));
  }
  
  int getDownNestedPreScrollRange() {
    if (this.mDownPreScrollRange != -1)
      return this.mDownPreScrollRange; 
    int j = 0;
    int k = getChildCount() - 1;
    while (k >= 0) {
      View view = getChildAt(k);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      int n = view.getMeasuredHeight();
      int m = layoutParams.mScrollFlags;
      if ((m & 0x5) == 5) {
        j += layoutParams.topMargin + layoutParams.bottomMargin;
        if ((m & 0x8) != 0) {
          m = j + ViewCompat.getMinimumHeight(view);
        } else if ((m & 0x2) != 0) {
          m = j + n - ViewCompat.getMinimumHeight(view);
        } else {
          m = j + n;
        } 
      } else {
        m = j;
        if (j > 0)
          break; 
      } 
      k--;
      j = m;
    } 
    int i = Math.max(0, j);
    this.mDownPreScrollRange = i;
    return i;
  }
  
  int getDownNestedScrollRange() {
    if (this.mDownScrollRange != -1)
      return this.mDownScrollRange; 
    int i = 0;
    byte b = 0;
    int j = getChildCount();
    while (true) {
      int k = i;
      if (b < j) {
        View view = getChildAt(b);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        int m = view.getMeasuredHeight();
        int n = layoutParams.topMargin;
        int i1 = layoutParams.bottomMargin;
        int i2 = layoutParams.mScrollFlags;
        k = i;
        if ((i2 & 0x1) != 0) {
          i += m + n + i1;
          if ((i2 & 0x2) != 0) {
            k = i - ViewCompat.getMinimumHeight(view) + getTopInset();
          } else {
            b++;
            continue;
          } 
        } 
      } 
      i = Math.max(0, k);
      this.mDownScrollRange = i;
      return i;
    } 
  }
  
  final int getMinimumHeightForVisibleOverlappingContent() {
    int i = getTopInset();
    null = ViewCompat.getMinimumHeight((View)this);
    if (null != 0)
      return null * 2 + i; 
    null = getChildCount();
    if (null >= 1) {
      null = ViewCompat.getMinimumHeight(getChildAt(null - 1));
    } else {
      null = 0;
    } 
    return (null != 0) ? (null * 2 + i) : (getHeight() / 3);
  }
  
  int getPendingAction() {
    return this.mPendingAction;
  }
  
  @Deprecated
  public float getTargetElevation() {
    return 0.0F;
  }
  
  @VisibleForTesting
  final int getTopInset() {
    return (this.mLastInsets != null) ? this.mLastInsets.getSystemWindowInsetTop() : 0;
  }
  
  public final int getTotalScrollRange() {
    if (this.mTotalScrollRange != -1)
      return this.mTotalScrollRange; 
    int i = 0;
    byte b = 0;
    int j = getChildCount();
    while (true) {
      int k = i;
      if (b < j) {
        View view = getChildAt(b);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        int m = view.getMeasuredHeight();
        int n = layoutParams.mScrollFlags;
        k = i;
        if ((n & 0x1) != 0) {
          i += layoutParams.topMargin + m + layoutParams.bottomMargin;
          if ((n & 0x2) != 0) {
            k = i - ViewCompat.getMinimumHeight(view);
          } else {
            b++;
            continue;
          } 
        } 
      } 
      i = Math.max(0, k - getTopInset());
      this.mTotalScrollRange = i;
      return i;
    } 
  }
  
  int getUpNestedPreScrollRange() {
    return getTotalScrollRange();
  }
  
  boolean hasChildWithInterpolator() {
    return this.mHaveChildWithInterpolator;
  }
  
  boolean hasScrollableChildren() {
    return (getTotalScrollRange() != 0);
  }
  
  protected int[] onCreateDrawableState(int paramInt) {
    int[] arrayOfInt1 = this.mTmpStatesArray;
    int[] arrayOfInt2 = super.onCreateDrawableState(arrayOfInt1.length + paramInt);
    if (this.mCollapsible) {
      paramInt = R.attr.state_collapsible;
    } else {
      paramInt = -R.attr.state_collapsible;
    } 
    arrayOfInt1[0] = paramInt;
    if (this.mCollapsible && this.mCollapsed) {
      paramInt = R.attr.state_collapsed;
      arrayOfInt1[1] = paramInt;
      return mergeDrawableStates(arrayOfInt2, arrayOfInt1);
    } 
    paramInt = -R.attr.state_collapsed;
    arrayOfInt1[1] = paramInt;
    return mergeDrawableStates(arrayOfInt2, arrayOfInt1);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    invalidateScrollRanges();
    this.mHaveChildWithInterpolator = false;
    paramInt1 = 0;
    paramInt2 = getChildCount();
    while (true) {
      if (paramInt1 < paramInt2)
        if (((LayoutParams)getChildAt(paramInt1).getLayoutParams()).getScrollInterpolator() != null) {
          this.mHaveChildWithInterpolator = true;
        } else {
          paramInt1++;
          continue;
        }  
      updateCollapsible();
      return;
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    invalidateScrollRanges();
  }
  
  WindowInsetsCompat onWindowInsetChanged(WindowInsetsCompat paramWindowInsetsCompat) {
    WindowInsetsCompat windowInsetsCompat = null;
    if (ViewCompat.getFitsSystemWindows((View)this))
      windowInsetsCompat = paramWindowInsetsCompat; 
    if (!ViewUtils.objectEquals(this.mLastInsets, windowInsetsCompat)) {
      this.mLastInsets = windowInsetsCompat;
      invalidateScrollRanges();
    } 
    return paramWindowInsetsCompat;
  }
  
  public void removeOnOffsetChangedListener(OnOffsetChangedListener paramOnOffsetChangedListener) {
    if (this.mListeners != null && paramOnOffsetChangedListener != null)
      this.mListeners.remove(paramOnOffsetChangedListener); 
  }
  
  void resetPendingAction() {
    this.mPendingAction = 0;
  }
  
  boolean setCollapsedState(boolean paramBoolean) {
    if (this.mCollapsed != paramBoolean) {
      this.mCollapsed = paramBoolean;
      refreshDrawableState();
      return true;
    } 
    return false;
  }
  
  public void setExpanded(boolean paramBoolean) {
    setExpanded(paramBoolean, ViewCompat.isLaidOut((View)this));
  }
  
  public void setExpanded(boolean paramBoolean1, boolean paramBoolean2) {
    byte b1;
    byte b2;
    if (paramBoolean1) {
      b1 = 1;
    } else {
      b1 = 2;
    } 
    if (paramBoolean2) {
      b2 = 4;
    } else {
      b2 = 0;
    } 
    this.mPendingAction = b2 | b1;
    requestLayout();
  }
  
  public void setOrientation(int paramInt) {
    if (paramInt != 1)
      throw new IllegalArgumentException("AppBarLayout is always vertical and does not support horizontal orientation"); 
    super.setOrientation(paramInt);
  }
  
  @Deprecated
  public void setTargetElevation(float paramFloat) {
    if (Build.VERSION.SDK_INT >= 21)
      ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator((View)this, paramFloat); 
  }
  
  public static class Behavior extends HeaderBehavior<AppBarLayout> {
    private static final int INVALID_POSITION = -1;
    
    private static final int MAX_OFFSET_ANIMATION_DURATION = 600;
    
    private WeakReference<View> mLastNestedScrollingChildRef;
    
    private ValueAnimatorCompat mOffsetAnimator;
    
    private int mOffsetDelta;
    
    private int mOffsetToChildIndexOnLayout = -1;
    
    private boolean mOffsetToChildIndexOnLayoutIsMinHeight;
    
    private float mOffsetToChildIndexOnLayoutPerc;
    
    private DragCallback mOnDragCallback;
    
    private boolean mSkipNestedPreScroll;
    
    private boolean mWasNestedFlung;
    
    public Behavior() {}
    
    public Behavior(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    private void animateOffsetTo(CoordinatorLayout param1CoordinatorLayout, AppBarLayout param1AppBarLayout, int param1Int, float param1Float) {
      int i = Math.abs(getTopBottomOffsetForScrollingSibling() - param1Int);
      param1Float = Math.abs(param1Float);
      if (param1Float > 0.0F) {
        i = Math.round(1000.0F * i / param1Float) * 3;
      } else {
        i = (int)((1.0F + i / param1AppBarLayout.getHeight()) * 150.0F);
      } 
      animateOffsetWithDuration(param1CoordinatorLayout, param1AppBarLayout, param1Int, i);
    }
    
    private void animateOffsetWithDuration(final CoordinatorLayout coordinatorLayout, final AppBarLayout child, int param1Int1, int param1Int2) {
      int i = getTopBottomOffsetForScrollingSibling();
      if (i == param1Int1) {
        if (this.mOffsetAnimator != null && this.mOffsetAnimator.isRunning())
          this.mOffsetAnimator.cancel(); 
        return;
      } 
      if (this.mOffsetAnimator == null) {
        this.mOffsetAnimator = ViewUtils.createAnimator();
        this.mOffsetAnimator.setInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
        this.mOffsetAnimator.addUpdateListener(new ValueAnimatorCompat.AnimatorUpdateListener() {
              public void onAnimationUpdate(ValueAnimatorCompat param2ValueAnimatorCompat) {
                AppBarLayout.Behavior.this.setHeaderTopBottomOffset(coordinatorLayout, child, param2ValueAnimatorCompat.getAnimatedIntValue());
              }
            });
      } else {
        this.mOffsetAnimator.cancel();
      } 
      this.mOffsetAnimator.setDuration(Math.min(param1Int2, 600));
      this.mOffsetAnimator.setIntValues(i, param1Int1);
      this.mOffsetAnimator.start();
    }
    
    private static boolean checkFlag(int param1Int1, int param1Int2) {
      return ((param1Int1 & param1Int2) == param1Int2);
    }
    
    private static View getAppBarChildOnOffset(AppBarLayout param1AppBarLayout, int param1Int) {
      // Byte code:
      //   0: iload_1
      //   1: invokestatic abs : (I)I
      //   4: istore_2
      //   5: iconst_0
      //   6: istore_1
      //   7: aload_0
      //   8: invokevirtual getChildCount : ()I
      //   11: istore_3
      //   12: iload_1
      //   13: iload_3
      //   14: if_icmpge -> 53
      //   17: aload_0
      //   18: iload_1
      //   19: invokevirtual getChildAt : (I)Landroid/view/View;
      //   22: astore #4
      //   24: iload_2
      //   25: aload #4
      //   27: invokevirtual getTop : ()I
      //   30: if_icmplt -> 47
      //   33: iload_2
      //   34: aload #4
      //   36: invokevirtual getBottom : ()I
      //   39: if_icmpgt -> 47
      //   42: aload #4
      //   44: astore_0
      //   45: aload_0
      //   46: areturn
      //   47: iinc #1, 1
      //   50: goto -> 12
      //   53: aconst_null
      //   54: astore_0
      //   55: goto -> 45
    }
    
    private int getChildIndexOnOffset(AppBarLayout param1AppBarLayout, int param1Int) {
      // Byte code:
      //   0: iconst_0
      //   1: istore_3
      //   2: aload_1
      //   3: invokevirtual getChildCount : ()I
      //   6: istore #4
      //   8: iload_3
      //   9: iload #4
      //   11: if_icmpge -> 49
      //   14: aload_1
      //   15: iload_3
      //   16: invokevirtual getChildAt : (I)Landroid/view/View;
      //   19: astore #5
      //   21: aload #5
      //   23: invokevirtual getTop : ()I
      //   26: iload_2
      //   27: ineg
      //   28: if_icmpgt -> 43
      //   31: aload #5
      //   33: invokevirtual getBottom : ()I
      //   36: iload_2
      //   37: ineg
      //   38: if_icmplt -> 43
      //   41: iload_3
      //   42: ireturn
      //   43: iinc #3, 1
      //   46: goto -> 8
      //   49: iconst_m1
      //   50: istore_3
      //   51: goto -> 41
    }
    
    private int interpolateOffset(AppBarLayout param1AppBarLayout, int param1Int) {
      int i = Math.abs(param1Int);
      int j = 0;
      int k = param1AppBarLayout.getChildCount();
      while (true) {
        int m = param1Int;
        if (j < k) {
          View view = param1AppBarLayout.getChildAt(j);
          AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams)view.getLayoutParams();
          Interpolator interpolator = layoutParams.getScrollInterpolator();
          if (i >= view.getTop() && i <= view.getBottom()) {
            m = param1Int;
            if (interpolator != null) {
              m = 0;
              k = layoutParams.getScrollFlags();
              if ((k & 0x1) != 0) {
                j = 0 + view.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
                m = j;
                if ((k & 0x2) != 0)
                  m = j - ViewCompat.getMinimumHeight(view); 
              } 
              j = m;
              if (ViewCompat.getFitsSystemWindows(view))
                j = m - param1AppBarLayout.getTopInset(); 
              m = param1Int;
              if (j > 0) {
                m = view.getTop();
                m = Math.round(j * interpolator.getInterpolation((i - m) / j));
                m = Integer.signum(param1Int) * (view.getTop() + m);
              } 
            } 
            return m;
          } 
        } else {
          return m;
        } 
        j++;
      } 
    }
    
    private boolean shouldJumpElevationState(CoordinatorLayout param1CoordinatorLayout, AppBarLayout param1AppBarLayout) {
      boolean bool = false;
      List<View> list = param1CoordinatorLayout.getDependents((View)param1AppBarLayout);
      byte b = 0;
      int i = list.size();
      while (true) {
        boolean bool1 = bool;
        if (b < i) {
          CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams)((View)list.get(b)).getLayoutParams()).getBehavior();
          if (behavior instanceof AppBarLayout.ScrollingViewBehavior) {
            bool1 = bool;
            if (((AppBarLayout.ScrollingViewBehavior)behavior).getOverlayTop() != 0)
              bool1 = true; 
            return bool1;
          } 
        } else {
          return bool1;
        } 
        b++;
      } 
    }
    
    private void snapToChildIfNeeded(CoordinatorLayout param1CoordinatorLayout, AppBarLayout param1AppBarLayout) {
      int i = getTopBottomOffsetForScrollingSibling();
      int j = getChildIndexOnOffset(param1AppBarLayout, i);
      if (j >= 0) {
        View view = param1AppBarLayout.getChildAt(j);
        int k = ((AppBarLayout.LayoutParams)view.getLayoutParams()).getScrollFlags();
        if ((k & 0x11) == 17) {
          int m = -view.getTop();
          int n = -view.getBottom();
          int i1 = n;
          if (j == param1AppBarLayout.getChildCount() - 1)
            i1 = n + param1AppBarLayout.getTopInset(); 
          if (checkFlag(k, 2)) {
            n = i1 + ViewCompat.getMinimumHeight(view);
            j = m;
          } else {
            n = i1;
            j = m;
            if (checkFlag(k, 5)) {
              n = i1 + ViewCompat.getMinimumHeight(view);
              if (i < n) {
                j = n;
                n = i1;
              } else {
                j = m;
              } 
            } 
          } 
          if (i >= (n + j) / 2)
            n = j; 
          animateOffsetTo(param1CoordinatorLayout, param1AppBarLayout, MathUtils.constrain(n, -param1AppBarLayout.getTotalScrollRange(), 0), 0.0F);
        } 
      } 
    }
    
    private void updateAppBarLayoutDrawableState(CoordinatorLayout param1CoordinatorLayout, AppBarLayout param1AppBarLayout, int param1Int1, int param1Int2) {
      View view = getAppBarChildOnOffset(param1AppBarLayout, param1Int1);
      if (view != null) {
        int i = ((AppBarLayout.LayoutParams)view.getLayoutParams()).getScrollFlags();
        boolean bool1 = false;
        boolean bool2 = bool1;
        if ((i & 0x1) != 0) {
          int j = ViewCompat.getMinimumHeight(view);
          if (param1Int2 > 0 && (i & 0xC) != 0) {
            if (-param1Int1 >= view.getBottom() - j - param1AppBarLayout.getTopInset()) {
              bool2 = true;
            } else {
              bool2 = false;
            } 
          } else {
            bool2 = bool1;
            if ((i & 0x2) != 0)
              if (-param1Int1 >= view.getBottom() - j - param1AppBarLayout.getTopInset()) {
                bool2 = true;
              } else {
                bool2 = false;
              }  
          } 
        } 
        if (param1AppBarLayout.setCollapsedState(bool2) && Build.VERSION.SDK_INT >= 11 && shouldJumpElevationState(param1CoordinatorLayout, param1AppBarLayout))
          param1AppBarLayout.jumpDrawablesToCurrentState(); 
      } 
    }
    
    boolean canDragView(AppBarLayout param1AppBarLayout) {
      boolean bool = true;
      if (this.mOnDragCallback != null)
        return this.mOnDragCallback.canDrag(param1AppBarLayout); 
      null = bool;
      if (this.mLastNestedScrollingChildRef != null) {
        View view = this.mLastNestedScrollingChildRef.get();
        if (view != null && view.isShown()) {
          null = bool;
          return ViewCompat.canScrollVertically(view, -1) ? false : null;
        } 
      } else {
        return null;
      } 
      return false;
    }
    
    int getMaxDragOffset(AppBarLayout param1AppBarLayout) {
      return -param1AppBarLayout.getDownNestedScrollRange();
    }
    
    int getScrollRangeForDragFling(AppBarLayout param1AppBarLayout) {
      return param1AppBarLayout.getTotalScrollRange();
    }
    
    int getTopBottomOffsetForScrollingSibling() {
      return getTopAndBottomOffset() + this.mOffsetDelta;
    }
    
    @VisibleForTesting
    boolean isOffsetAnimatorRunning() {
      return (this.mOffsetAnimator != null && this.mOffsetAnimator.isRunning());
    }
    
    void onFlingFinished(CoordinatorLayout param1CoordinatorLayout, AppBarLayout param1AppBarLayout) {
      snapToChildIfNeeded(param1CoordinatorLayout, param1AppBarLayout);
    }
    
    public boolean onLayoutChild(CoordinatorLayout param1CoordinatorLayout, AppBarLayout param1AppBarLayout, int param1Int) {
      boolean bool = super.onLayoutChild(param1CoordinatorLayout, param1AppBarLayout, param1Int);
      int i = param1AppBarLayout.getPendingAction();
      if (i != 0) {
        if ((i & 0x4) != 0) {
          param1Int = 1;
        } else {
          param1Int = 0;
        } 
        if ((i & 0x2) != 0) {
          i = -param1AppBarLayout.getUpNestedPreScrollRange();
          if (param1Int != 0) {
            animateOffsetTo(param1CoordinatorLayout, param1AppBarLayout, i, 0.0F);
            param1AppBarLayout.resetPendingAction();
            this.mOffsetToChildIndexOnLayout = -1;
            setTopAndBottomOffset(MathUtils.constrain(getTopAndBottomOffset(), -param1AppBarLayout.getTotalScrollRange(), 0));
            param1AppBarLayout.dispatchOffsetUpdates(getTopAndBottomOffset());
            return bool;
          } 
          setHeaderTopBottomOffset(param1CoordinatorLayout, param1AppBarLayout, i);
          param1AppBarLayout.resetPendingAction();
          this.mOffsetToChildIndexOnLayout = -1;
          setTopAndBottomOffset(MathUtils.constrain(getTopAndBottomOffset(), -param1AppBarLayout.getTotalScrollRange(), 0));
          param1AppBarLayout.dispatchOffsetUpdates(getTopAndBottomOffset());
          return bool;
        } 
        if ((i & 0x1) != 0) {
          if (param1Int != 0) {
            animateOffsetTo(param1CoordinatorLayout, param1AppBarLayout, 0, 0.0F);
            param1AppBarLayout.resetPendingAction();
            this.mOffsetToChildIndexOnLayout = -1;
            setTopAndBottomOffset(MathUtils.constrain(getTopAndBottomOffset(), -param1AppBarLayout.getTotalScrollRange(), 0));
            param1AppBarLayout.dispatchOffsetUpdates(getTopAndBottomOffset());
            return bool;
          } 
          setHeaderTopBottomOffset(param1CoordinatorLayout, param1AppBarLayout, 0);
        } 
        param1AppBarLayout.resetPendingAction();
        this.mOffsetToChildIndexOnLayout = -1;
        setTopAndBottomOffset(MathUtils.constrain(getTopAndBottomOffset(), -param1AppBarLayout.getTotalScrollRange(), 0));
        param1AppBarLayout.dispatchOffsetUpdates(getTopAndBottomOffset());
        return bool;
      } 
      if (this.mOffsetToChildIndexOnLayout >= 0) {
        View view = param1AppBarLayout.getChildAt(this.mOffsetToChildIndexOnLayout);
        param1Int = -view.getBottom();
        if (this.mOffsetToChildIndexOnLayoutIsMinHeight) {
          param1Int += ViewCompat.getMinimumHeight(view);
        } else {
          param1Int += Math.round(view.getHeight() * this.mOffsetToChildIndexOnLayoutPerc);
        } 
        setTopAndBottomOffset(param1Int);
      } 
      param1AppBarLayout.resetPendingAction();
      this.mOffsetToChildIndexOnLayout = -1;
      setTopAndBottomOffset(MathUtils.constrain(getTopAndBottomOffset(), -param1AppBarLayout.getTotalScrollRange(), 0));
      param1AppBarLayout.dispatchOffsetUpdates(getTopAndBottomOffset());
      return bool;
    }
    
    public boolean onMeasureChild(CoordinatorLayout param1CoordinatorLayout, AppBarLayout param1AppBarLayout, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      if (((CoordinatorLayout.LayoutParams)param1AppBarLayout.getLayoutParams()).height == -2) {
        param1CoordinatorLayout.onMeasureChild((View)param1AppBarLayout, param1Int1, param1Int2, View.MeasureSpec.makeMeasureSpec(0, 0), param1Int4);
        return true;
      } 
      return super.onMeasureChild(param1CoordinatorLayout, param1AppBarLayout, param1Int1, param1Int2, param1Int3, param1Int4);
    }
    
    public boolean onNestedFling(CoordinatorLayout param1CoordinatorLayout, AppBarLayout param1AppBarLayout, View param1View, float param1Float1, float param1Float2, boolean param1Boolean) {
      boolean bool = false;
      if (!param1Boolean) {
        param1Boolean = fling(param1CoordinatorLayout, param1AppBarLayout, -param1AppBarLayout.getTotalScrollRange(), 0, -param1Float2);
        this.mWasNestedFlung = param1Boolean;
        return param1Boolean;
      } 
      if (param1Float2 < 0.0F) {
        int j = -param1AppBarLayout.getTotalScrollRange() + param1AppBarLayout.getDownNestedPreScrollRange();
        param1Boolean = bool;
        if (getTopBottomOffsetForScrollingSibling() < j) {
          animateOffsetTo(param1CoordinatorLayout, param1AppBarLayout, j, param1Float2);
          param1Boolean = true;
        } 
        this.mWasNestedFlung = param1Boolean;
        return param1Boolean;
      } 
      int i = -param1AppBarLayout.getUpNestedPreScrollRange();
      param1Boolean = bool;
      if (getTopBottomOffsetForScrollingSibling() > i) {
        animateOffsetTo(param1CoordinatorLayout, param1AppBarLayout, i, param1Float2);
        param1Boolean = true;
      } 
      this.mWasNestedFlung = param1Boolean;
      return param1Boolean;
    }
    
    public void onNestedPreScroll(CoordinatorLayout param1CoordinatorLayout, AppBarLayout param1AppBarLayout, View param1View, int param1Int1, int param1Int2, int[] param1ArrayOfint) {
      if (param1Int2 != 0 && !this.mSkipNestedPreScroll) {
        boolean bool;
        if (param1Int2 < 0) {
          param1Int1 = -param1AppBarLayout.getTotalScrollRange();
          bool = param1Int1 + param1AppBarLayout.getDownNestedPreScrollRange();
        } else {
          param1Int1 = -param1AppBarLayout.getUpNestedPreScrollRange();
          bool = false;
        } 
        param1ArrayOfint[1] = scroll(param1CoordinatorLayout, param1AppBarLayout, param1Int2, param1Int1, bool);
      } 
    }
    
    public void onNestedScroll(CoordinatorLayout param1CoordinatorLayout, AppBarLayout param1AppBarLayout, View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      if (param1Int4 < 0) {
        scroll(param1CoordinatorLayout, param1AppBarLayout, param1Int4, -param1AppBarLayout.getDownNestedScrollRange(), 0);
        this.mSkipNestedPreScroll = true;
        return;
      } 
      this.mSkipNestedPreScroll = false;
    }
    
    public void onRestoreInstanceState(CoordinatorLayout param1CoordinatorLayout, AppBarLayout param1AppBarLayout, Parcelable param1Parcelable) {
      SavedState savedState;
      if (param1Parcelable instanceof SavedState) {
        savedState = (SavedState)param1Parcelable;
        super.onRestoreInstanceState(param1CoordinatorLayout, param1AppBarLayout, savedState.getSuperState());
        this.mOffsetToChildIndexOnLayout = savedState.firstVisibleChildIndex;
        this.mOffsetToChildIndexOnLayoutPerc = savedState.firstVisibleChildPercentageShown;
        this.mOffsetToChildIndexOnLayoutIsMinHeight = savedState.firstVisibleChildAtMinimumHeight;
        return;
      } 
      super.onRestoreInstanceState(param1CoordinatorLayout, param1AppBarLayout, (Parcelable)savedState);
      this.mOffsetToChildIndexOnLayout = -1;
    }
    
    public Parcelable onSaveInstanceState(CoordinatorLayout param1CoordinatorLayout, AppBarLayout param1AppBarLayout) {
      Parcelable parcelable = super.onSaveInstanceState(param1CoordinatorLayout, param1AppBarLayout);
      int i = getTopAndBottomOffset();
      byte b = 0;
      int j = param1AppBarLayout.getChildCount();
      while (true) {
        SavedState savedState;
        if (b < j) {
          View view = param1AppBarLayout.getChildAt(b);
          int k = view.getBottom() + i;
          if (view.getTop() + i <= 0 && k >= 0) {
            boolean bool;
            savedState = new SavedState(parcelable);
            savedState.firstVisibleChildIndex = b;
            if (k == ViewCompat.getMinimumHeight(view) + param1AppBarLayout.getTopInset()) {
              bool = true;
            } else {
              bool = false;
            } 
            savedState.firstVisibleChildAtMinimumHeight = bool;
            savedState.firstVisibleChildPercentageShown = k / view.getHeight();
            return (Parcelable)savedState;
          } 
        } else {
          return (Parcelable)savedState;
        } 
        b++;
      } 
    }
    
    public boolean onStartNestedScroll(CoordinatorLayout param1CoordinatorLayout, AppBarLayout param1AppBarLayout, View param1View1, View param1View2, int param1Int) {
      boolean bool;
      if ((param1Int & 0x2) != 0 && param1AppBarLayout.hasScrollableChildren() && param1CoordinatorLayout.getHeight() - param1View1.getHeight() <= param1AppBarLayout.getHeight()) {
        bool = true;
      } else {
        bool = false;
      } 
      if (bool && this.mOffsetAnimator != null)
        this.mOffsetAnimator.cancel(); 
      this.mLastNestedScrollingChildRef = null;
      return bool;
    }
    
    public void onStopNestedScroll(CoordinatorLayout param1CoordinatorLayout, AppBarLayout param1AppBarLayout, View param1View) {
      if (!this.mWasNestedFlung)
        snapToChildIfNeeded(param1CoordinatorLayout, param1AppBarLayout); 
      this.mSkipNestedPreScroll = false;
      this.mWasNestedFlung = false;
      this.mLastNestedScrollingChildRef = new WeakReference<View>(param1View);
    }
    
    public void setDragCallback(@Nullable DragCallback param1DragCallback) {
      this.mOnDragCallback = param1DragCallback;
    }
    
    int setHeaderTopBottomOffset(CoordinatorLayout param1CoordinatorLayout, AppBarLayout param1AppBarLayout, int param1Int1, int param1Int2, int param1Int3) {
      int i = getTopBottomOffsetForScrollingSibling();
      boolean bool = false;
      if (param1Int2 != 0 && i >= param1Int2 && i <= param1Int3) {
        param1Int2 = MathUtils.constrain(param1Int1, param1Int2, param1Int3);
        param1Int1 = bool;
        if (i != param1Int2) {
          if (param1AppBarLayout.hasChildWithInterpolator()) {
            param1Int1 = interpolateOffset(param1AppBarLayout, param1Int2);
          } else {
            param1Int1 = param1Int2;
          } 
          boolean bool1 = setTopAndBottomOffset(param1Int1);
          param1Int3 = i - param1Int2;
          this.mOffsetDelta = param1Int2 - param1Int1;
          if (!bool1 && param1AppBarLayout.hasChildWithInterpolator())
            param1CoordinatorLayout.dispatchDependentViewsChanged((View)param1AppBarLayout); 
          param1AppBarLayout.dispatchOffsetUpdates(getTopAndBottomOffset());
          if (param1Int2 < i) {
            param1Int1 = -1;
          } else {
            param1Int1 = 1;
          } 
          updateAppBarLayoutDrawableState(param1CoordinatorLayout, param1AppBarLayout, param1Int2, param1Int1);
          param1Int1 = param1Int3;
        } 
        return param1Int1;
      } 
      this.mOffsetDelta = 0;
      return bool;
    }
    
    public static abstract class DragCallback {
      public abstract boolean canDrag(@NonNull AppBarLayout param2AppBarLayout);
    }
    
    protected static class SavedState extends AbsSavedState {
      public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
            public AppBarLayout.Behavior.SavedState createFromParcel(Parcel param3Parcel, ClassLoader param3ClassLoader) {
              return new AppBarLayout.Behavior.SavedState(param3Parcel, param3ClassLoader);
            }
            
            public AppBarLayout.Behavior.SavedState[] newArray(int param3Int) {
              return new AppBarLayout.Behavior.SavedState[param3Int];
            }
          });
      
      boolean firstVisibleChildAtMinimumHeight;
      
      int firstVisibleChildIndex;
      
      float firstVisibleChildPercentageShown;
      
      public SavedState(Parcel param2Parcel, ClassLoader param2ClassLoader) {
        super(param2Parcel, param2ClassLoader);
        boolean bool;
        this.firstVisibleChildIndex = param2Parcel.readInt();
        this.firstVisibleChildPercentageShown = param2Parcel.readFloat();
        if (param2Parcel.readByte() != 0) {
          bool = true;
        } else {
          bool = false;
        } 
        this.firstVisibleChildAtMinimumHeight = bool;
      }
      
      public SavedState(Parcelable param2Parcelable) {
        super(param2Parcelable);
      }
      
      public void writeToParcel(Parcel param2Parcel, int param2Int) {
        super.writeToParcel(param2Parcel, param2Int);
        param2Parcel.writeInt(this.firstVisibleChildIndex);
        param2Parcel.writeFloat(this.firstVisibleChildPercentageShown);
        if (this.firstVisibleChildAtMinimumHeight) {
          param2Int = 1;
        } else {
          param2Int = 0;
        } 
        param2Parcel.writeByte((byte)param2Int);
      }
    }
    
    static final class null implements ParcelableCompatCreatorCallbacks<SavedState> {
      public AppBarLayout.Behavior.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
        return new AppBarLayout.Behavior.SavedState(param2Parcel, param2ClassLoader);
      }
      
      public AppBarLayout.Behavior.SavedState[] newArray(int param2Int) {
        return new AppBarLayout.Behavior.SavedState[param2Int];
      }
    }
  }
  
  class null implements ValueAnimatorCompat.AnimatorUpdateListener {
    public void onAnimationUpdate(ValueAnimatorCompat param1ValueAnimatorCompat) {
      this.this$0.setHeaderTopBottomOffset(coordinatorLayout, child, param1ValueAnimatorCompat.getAnimatedIntValue());
    }
  }
  
  public static abstract class DragCallback {
    public abstract boolean canDrag(@NonNull AppBarLayout param1AppBarLayout);
  }
  
  protected static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
          public AppBarLayout.Behavior.SavedState createFromParcel(Parcel param3Parcel, ClassLoader param3ClassLoader) {
            return new AppBarLayout.Behavior.SavedState(param3Parcel, param3ClassLoader);
          }
          
          public AppBarLayout.Behavior.SavedState[] newArray(int param3Int) {
            return new AppBarLayout.Behavior.SavedState[param3Int];
          }
        });
    
    boolean firstVisibleChildAtMinimumHeight;
    
    int firstVisibleChildIndex;
    
    float firstVisibleChildPercentageShown;
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      boolean bool;
      this.firstVisibleChildIndex = param1Parcel.readInt();
      this.firstVisibleChildPercentageShown = param1Parcel.readFloat();
      if (param1Parcel.readByte() != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      this.firstVisibleChildAtMinimumHeight = bool;
    }
    
    public SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.firstVisibleChildIndex);
      param1Parcel.writeFloat(this.firstVisibleChildPercentageShown);
      if (this.firstVisibleChildAtMinimumHeight) {
        param1Int = 1;
      } else {
        param1Int = 0;
      } 
      param1Parcel.writeByte((byte)param1Int);
    }
  }
  
  static final class null implements ParcelableCompatCreatorCallbacks<Behavior.SavedState> {
    public AppBarLayout.Behavior.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new AppBarLayout.Behavior.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public AppBarLayout.Behavior.SavedState[] newArray(int param1Int) {
      return new AppBarLayout.Behavior.SavedState[param1Int];
    }
  }
  
  public static class LayoutParams extends LinearLayout.LayoutParams {
    static final int COLLAPSIBLE_FLAGS = 10;
    
    static final int FLAG_QUICK_RETURN = 5;
    
    static final int FLAG_SNAP = 17;
    
    public static final int SCROLL_FLAG_ENTER_ALWAYS = 4;
    
    public static final int SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED = 8;
    
    public static final int SCROLL_FLAG_EXIT_UNTIL_COLLAPSED = 2;
    
    public static final int SCROLL_FLAG_SCROLL = 1;
    
    public static final int SCROLL_FLAG_SNAP = 16;
    
    int mScrollFlags = 1;
    
    Interpolator mScrollInterpolator;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(int param1Int1, int param1Int2, float param1Float) {
      super(param1Int1, param1Int2, param1Float);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.AppBarLayout_Layout);
      this.mScrollFlags = typedArray.getInt(R.styleable.AppBarLayout_Layout_layout_scrollFlags, 0);
      if (typedArray.hasValue(R.styleable.AppBarLayout_Layout_layout_scrollInterpolator))
        this.mScrollInterpolator = AnimationUtils.loadInterpolator(param1Context, typedArray.getResourceId(R.styleable.AppBarLayout_Layout_layout_scrollInterpolator, 0)); 
      typedArray.recycle();
    }
    
    @TargetApi(19)
    @RequiresApi(19)
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.mScrollFlags = param1LayoutParams.mScrollFlags;
      this.mScrollInterpolator = param1LayoutParams.mScrollInterpolator;
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
    
    @TargetApi(19)
    @RequiresApi(19)
    public LayoutParams(LinearLayout.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public int getScrollFlags() {
      return this.mScrollFlags;
    }
    
    public Interpolator getScrollInterpolator() {
      return this.mScrollInterpolator;
    }
    
    boolean isCollapsible() {
      boolean bool = true;
      if ((this.mScrollFlags & 0x1) != 1 || (this.mScrollFlags & 0xA) == 0)
        bool = false; 
      return bool;
    }
    
    public void setScrollFlags(int param1Int) {
      this.mScrollFlags = param1Int;
    }
    
    public void setScrollInterpolator(Interpolator param1Interpolator) {
      this.mScrollInterpolator = param1Interpolator;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface ScrollFlags {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface ScrollFlags {}
  
  public static interface OnOffsetChangedListener {
    void onOffsetChanged(AppBarLayout param1AppBarLayout, int param1Int);
  }
  
  public static class ScrollingViewBehavior extends HeaderScrollingViewBehavior {
    public ScrollingViewBehavior() {}
    
    public ScrollingViewBehavior(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.ScrollingViewBehavior_Layout);
      setOverlayTop(typedArray.getDimensionPixelSize(R.styleable.ScrollingViewBehavior_Layout_behavior_overlapTop, 0));
      typedArray.recycle();
    }
    
    private static int getAppBarLayoutOffset(AppBarLayout param1AppBarLayout) {
      CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams)param1AppBarLayout.getLayoutParams()).getBehavior();
      return (behavior instanceof AppBarLayout.Behavior) ? ((AppBarLayout.Behavior)behavior).getTopBottomOffsetForScrollingSibling() : 0;
    }
    
    private void offsetChildAsNeeded(CoordinatorLayout param1CoordinatorLayout, View param1View1, View param1View2) {
      CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams)param1View2.getLayoutParams()).getBehavior();
      if (behavior instanceof AppBarLayout.Behavior) {
        behavior = behavior;
        ViewCompat.offsetTopAndBottom(param1View1, param1View2.getBottom() - param1View1.getTop() + ((AppBarLayout.Behavior)behavior).mOffsetDelta + getVerticalLayoutGap() - getOverlapPixelsForOffset(param1View2));
      } 
    }
    
    AppBarLayout findFirstDependency(List<View> param1List) {
      // Byte code:
      //   0: iconst_0
      //   1: istore_2
      //   2: aload_1
      //   3: invokeinterface size : ()I
      //   8: istore_3
      //   9: iload_2
      //   10: iload_3
      //   11: if_icmpge -> 48
      //   14: aload_1
      //   15: iload_2
      //   16: invokeinterface get : (I)Ljava/lang/Object;
      //   21: checkcast android/view/View
      //   24: astore #4
      //   26: aload #4
      //   28: instanceof android/support/design/widget/AppBarLayout
      //   31: ifeq -> 42
      //   34: aload #4
      //   36: checkcast android/support/design/widget/AppBarLayout
      //   39: astore_1
      //   40: aload_1
      //   41: areturn
      //   42: iinc #2, 1
      //   45: goto -> 9
      //   48: aconst_null
      //   49: astore_1
      //   50: goto -> 40
    }
    
    float getOverlapRatioForOffset(View param1View) {
      int i;
      int k;
      float f1 = 0.0F;
      float f2 = f1;
      if (param1View instanceof AppBarLayout) {
        AppBarLayout appBarLayout = (AppBarLayout)param1View;
        i = appBarLayout.getTotalScrollRange();
        j = appBarLayout.getDownNestedPreScrollRange();
        k = getAppBarLayoutOffset(appBarLayout);
        if (j != 0 && i + k <= j)
          return f1; 
      } else {
        return f2;
      } 
      int j = i - j;
      f2 = f1;
      if (j != 0)
        f2 = 1.0F + k / j; 
      return f2;
    }
    
    int getScrollRange(View param1View) {
      return (param1View instanceof AppBarLayout) ? ((AppBarLayout)param1View).getTotalScrollRange() : super.getScrollRange(param1View);
    }
    
    public boolean layoutDependsOn(CoordinatorLayout param1CoordinatorLayout, View param1View1, View param1View2) {
      return param1View2 instanceof AppBarLayout;
    }
    
    public boolean onDependentViewChanged(CoordinatorLayout param1CoordinatorLayout, View param1View1, View param1View2) {
      offsetChildAsNeeded(param1CoordinatorLayout, param1View1, param1View2);
      return false;
    }
    
    public boolean onRequestChildRectangleOnScreen(CoordinatorLayout param1CoordinatorLayout, View param1View, Rect param1Rect, boolean param1Boolean) {
      boolean bool = true;
      AppBarLayout appBarLayout = findFirstDependency(param1CoordinatorLayout.getDependencies(param1View));
      if (appBarLayout != null) {
        param1Rect.offset(param1View.getLeft(), param1View.getTop());
        Rect rect = this.mTempRect1;
        rect.set(0, 0, param1CoordinatorLayout.getWidth(), param1CoordinatorLayout.getHeight());
        if (!rect.contains(param1Rect)) {
          if (!param1Boolean) {
            param1Boolean = true;
          } else {
            param1Boolean = false;
          } 
          appBarLayout.setExpanded(false, param1Boolean);
          return bool;
        } 
      } 
      return false;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/AppBarLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */