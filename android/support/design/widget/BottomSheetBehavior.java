package android.support.design.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.design.R;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

public class BottomSheetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
  private static final float HIDE_FRICTION = 0.1F;
  
  private static final float HIDE_THRESHOLD = 0.5F;
  
  public static final int PEEK_HEIGHT_AUTO = -1;
  
  public static final int STATE_COLLAPSED = 4;
  
  public static final int STATE_DRAGGING = 1;
  
  public static final int STATE_EXPANDED = 3;
  
  public static final int STATE_HIDDEN = 5;
  
  public static final int STATE_SETTLING = 2;
  
  int mActivePointerId;
  
  private BottomSheetCallback mCallback;
  
  private final ViewDragHelper.Callback mDragCallback = new ViewDragHelper.Callback() {
      public int clampViewPositionHorizontal(View param1View, int param1Int1, int param1Int2) {
        return param1View.getLeft();
      }
      
      public int clampViewPositionVertical(View param1View, int param1Int1, int param1Int2) {
        int i = BottomSheetBehavior.this.mMinOffset;
        if (BottomSheetBehavior.this.mHideable) {
          param1Int2 = BottomSheetBehavior.this.mParentHeight;
          return MathUtils.constrain(param1Int1, i, param1Int2);
        } 
        param1Int2 = BottomSheetBehavior.this.mMaxOffset;
        return MathUtils.constrain(param1Int1, i, param1Int2);
      }
      
      public int getViewVerticalDragRange(View param1View) {
        return BottomSheetBehavior.this.mHideable ? (BottomSheetBehavior.this.mParentHeight - BottomSheetBehavior.this.mMinOffset) : (BottomSheetBehavior.this.mMaxOffset - BottomSheetBehavior.this.mMinOffset);
      }
      
      public void onViewDragStateChanged(int param1Int) {
        if (param1Int == 1)
          BottomSheetBehavior.this.setStateInternal(1); 
      }
      
      public void onViewPositionChanged(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
        BottomSheetBehavior.this.dispatchOnSlide(param1Int2);
      }
      
      public void onViewReleased(View param1View, float param1Float1, float param1Float2) {
        int i;
        byte b;
        if (param1Float2 < 0.0F) {
          i = BottomSheetBehavior.this.mMinOffset;
          b = 3;
        } else if (BottomSheetBehavior.this.mHideable && BottomSheetBehavior.this.shouldHide(param1View, param1Float2)) {
          i = BottomSheetBehavior.this.mParentHeight;
          b = 5;
        } else if (param1Float2 == 0.0F) {
          b = param1View.getTop();
          if (Math.abs(b - BottomSheetBehavior.this.mMinOffset) < Math.abs(b - BottomSheetBehavior.this.mMaxOffset)) {
            i = BottomSheetBehavior.this.mMinOffset;
            b = 3;
          } else {
            i = BottomSheetBehavior.this.mMaxOffset;
            b = 4;
          } 
        } else {
          i = BottomSheetBehavior.this.mMaxOffset;
          b = 4;
        } 
        if (BottomSheetBehavior.this.mViewDragHelper.settleCapturedViewAt(param1View.getLeft(), i)) {
          BottomSheetBehavior.this.setStateInternal(2);
          ViewCompat.postOnAnimation(param1View, new BottomSheetBehavior.SettleRunnable(param1View, b));
          return;
        } 
        BottomSheetBehavior.this.setStateInternal(b);
      }
      
      public boolean tryCaptureView(View param1View, int param1Int) {
        // Byte code:
        //   0: iconst_1
        //   1: istore_3
        //   2: iconst_0
        //   3: istore #4
        //   5: aload_0
        //   6: getfield this$0 : Landroid/support/design/widget/BottomSheetBehavior;
        //   9: getfield mState : I
        //   12: iconst_1
        //   13: if_icmpne -> 23
        //   16: iload #4
        //   18: istore #5
        //   20: iload #5
        //   22: ireturn
        //   23: iload #4
        //   25: istore #5
        //   27: aload_0
        //   28: getfield this$0 : Landroid/support/design/widget/BottomSheetBehavior;
        //   31: getfield mTouchingScrollingChild : Z
        //   34: ifne -> 20
        //   37: aload_0
        //   38: getfield this$0 : Landroid/support/design/widget/BottomSheetBehavior;
        //   41: getfield mState : I
        //   44: iconst_3
        //   45: if_icmpne -> 92
        //   48: aload_0
        //   49: getfield this$0 : Landroid/support/design/widget/BottomSheetBehavior;
        //   52: getfield mActivePointerId : I
        //   55: iload_2
        //   56: if_icmpne -> 92
        //   59: aload_0
        //   60: getfield this$0 : Landroid/support/design/widget/BottomSheetBehavior;
        //   63: getfield mNestedScrollingChildRef : Ljava/lang/ref/WeakReference;
        //   66: invokevirtual get : ()Ljava/lang/Object;
        //   69: checkcast android/view/View
        //   72: astore #6
        //   74: aload #6
        //   76: ifnull -> 92
        //   79: iload #4
        //   81: istore #5
        //   83: aload #6
        //   85: iconst_m1
        //   86: invokestatic canScrollVertically : (Landroid/view/View;I)Z
        //   89: ifne -> 20
        //   92: aload_0
        //   93: getfield this$0 : Landroid/support/design/widget/BottomSheetBehavior;
        //   96: getfield mViewRef : Ljava/lang/ref/WeakReference;
        //   99: ifnull -> 122
        //   102: aload_0
        //   103: getfield this$0 : Landroid/support/design/widget/BottomSheetBehavior;
        //   106: getfield mViewRef : Ljava/lang/ref/WeakReference;
        //   109: invokevirtual get : ()Ljava/lang/Object;
        //   112: aload_1
        //   113: if_acmpne -> 122
        //   116: iload_3
        //   117: istore #5
        //   119: goto -> 20
        //   122: iconst_0
        //   123: istore #5
        //   125: goto -> 119
      }
    };
  
  boolean mHideable;
  
  private boolean mIgnoreEvents;
  
  private int mInitialY;
  
  private int mLastNestedScrollDy;
  
  int mMaxOffset;
  
  private float mMaximumVelocity;
  
  int mMinOffset;
  
  private boolean mNestedScrolled;
  
  WeakReference<View> mNestedScrollingChildRef;
  
  int mParentHeight;
  
  private int mPeekHeight;
  
  private boolean mPeekHeightAuto;
  
  private int mPeekHeightMin;
  
  private boolean mSkipCollapsed;
  
  int mState = 4;
  
  boolean mTouchingScrollingChild;
  
  private VelocityTracker mVelocityTracker;
  
  ViewDragHelper mViewDragHelper;
  
  WeakReference<V> mViewRef;
  
  public BottomSheetBehavior() {}
  
  public BottomSheetBehavior(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.BottomSheetBehavior_Layout);
    TypedValue typedValue = typedArray.peekValue(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight);
    if (typedValue != null && typedValue.data == -1) {
      setPeekHeight(typedValue.data);
    } else {
      setPeekHeight(typedArray.getDimensionPixelSize(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight, -1));
    } 
    setHideable(typedArray.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
    setSkipCollapsed(typedArray.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false));
    typedArray.recycle();
    this.mMaximumVelocity = ViewConfiguration.get(paramContext).getScaledMaximumFlingVelocity();
  }
  
  private View findScrollingChild(View paramView) {
    // Byte code:
    //   0: aload_1
    //   1: instanceof android/support/v4/view/NestedScrollingChild
    //   4: ifeq -> 9
    //   7: aload_1
    //   8: areturn
    //   9: aload_1
    //   10: instanceof android/view/ViewGroup
    //   13: ifeq -> 58
    //   16: aload_1
    //   17: checkcast android/view/ViewGroup
    //   20: astore_2
    //   21: iconst_0
    //   22: istore_3
    //   23: aload_2
    //   24: invokevirtual getChildCount : ()I
    //   27: istore #4
    //   29: iload_3
    //   30: iload #4
    //   32: if_icmpge -> 58
    //   35: aload_0
    //   36: aload_2
    //   37: iload_3
    //   38: invokevirtual getChildAt : (I)Landroid/view/View;
    //   41: invokespecial findScrollingChild : (Landroid/view/View;)Landroid/view/View;
    //   44: astore_1
    //   45: aload_1
    //   46: ifnull -> 52
    //   49: goto -> 7
    //   52: iinc #3, 1
    //   55: goto -> 29
    //   58: aconst_null
    //   59: astore_1
    //   60: goto -> 7
  }
  
  public static <V extends View> BottomSheetBehavior<V> from(V paramV) {
    ViewGroup.LayoutParams layoutParams = paramV.getLayoutParams();
    if (!(layoutParams instanceof CoordinatorLayout.LayoutParams))
      throw new IllegalArgumentException("The view is not a child of CoordinatorLayout"); 
    CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams)layoutParams).getBehavior();
    if (!(behavior instanceof BottomSheetBehavior))
      throw new IllegalArgumentException("The view is not associated with BottomSheetBehavior"); 
    return (BottomSheetBehavior<V>)behavior;
  }
  
  private float getYVelocity() {
    this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
    return VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId);
  }
  
  private void reset() {
    this.mActivePointerId = -1;
    if (this.mVelocityTracker != null) {
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
    } 
  }
  
  void dispatchOnSlide(int paramInt) {
    View view = (View)this.mViewRef.get();
    if (view != null && this.mCallback != null) {
      if (paramInt > this.mMaxOffset) {
        this.mCallback.onSlide(view, (this.mMaxOffset - paramInt) / (this.mParentHeight - this.mMaxOffset));
        return;
      } 
    } else {
      return;
    } 
    this.mCallback.onSlide(view, (this.mMaxOffset - paramInt) / (this.mMaxOffset - this.mMinOffset));
  }
  
  public final int getPeekHeight() {
    return this.mPeekHeightAuto ? -1 : this.mPeekHeight;
  }
  
  @VisibleForTesting
  int getPeekHeightMin() {
    return this.mPeekHeightMin;
  }
  
  public boolean getSkipCollapsed() {
    return this.mSkipCollapsed;
  }
  
  public final int getState() {
    return this.mState;
  }
  
  public boolean isHideable() {
    return this.mHideable;
  }
  
  public boolean onInterceptTouchEvent(CoordinatorLayout paramCoordinatorLayout, V paramV, MotionEvent paramMotionEvent) {
    int j;
    View view2;
    boolean bool = true;
    null = false;
    if (!paramV.isShown()) {
      this.mIgnoreEvents = true;
      return null;
    } 
    int i = MotionEventCompat.getActionMasked(paramMotionEvent);
    if (i == 0)
      reset(); 
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
    this.mVelocityTracker.addMovement(paramMotionEvent);
    switch (i) {
      default:
        if (!this.mIgnoreEvents && this.mViewDragHelper.shouldInterceptTouchEvent(paramMotionEvent))
          return true; 
        break;
      case 1:
      case 3:
        this.mTouchingScrollingChild = false;
        this.mActivePointerId = -1;
        if (this.mIgnoreEvents) {
          this.mIgnoreEvents = false;
          return null;
        } 
      case 0:
        j = (int)paramMotionEvent.getX();
        this.mInitialY = (int)paramMotionEvent.getY();
        view2 = this.mNestedScrollingChildRef.get();
        if (view2 != null && paramCoordinatorLayout.isPointInChildBounds(view2, j, this.mInitialY)) {
          this.mActivePointerId = paramMotionEvent.getPointerId(paramMotionEvent.getActionIndex());
          this.mTouchingScrollingChild = true;
        } 
        if (this.mActivePointerId == -1 && !paramCoordinatorLayout.isPointInChildBounds((View)paramV, j, this.mInitialY)) {
          null = true;
        } else {
          null = false;
        } 
        this.mIgnoreEvents = null;
    } 
    View view1 = this.mNestedScrollingChildRef.get();
    return (i == 2 && view1 != null && !this.mIgnoreEvents && this.mState != 1 && !paramCoordinatorLayout.isPointInChildBounds(view1, (int)paramMotionEvent.getX(), (int)paramMotionEvent.getY()) && Math.abs(this.mInitialY - paramMotionEvent.getY()) > this.mViewDragHelper.getTouchSlop()) ? bool : false;
  }
  
  public boolean onLayoutChild(CoordinatorLayout paramCoordinatorLayout, V paramV, int paramInt) {
    if (ViewCompat.getFitsSystemWindows((View)paramCoordinatorLayout) && !ViewCompat.getFitsSystemWindows((View)paramV))
      ViewCompat.setFitsSystemWindows((View)paramV, true); 
    int i = paramV.getTop();
    paramCoordinatorLayout.onLayoutChild((View)paramV, paramInt);
    this.mParentHeight = paramCoordinatorLayout.getHeight();
    if (this.mPeekHeightAuto) {
      if (this.mPeekHeightMin == 0)
        this.mPeekHeightMin = paramCoordinatorLayout.getResources().getDimensionPixelSize(R.dimen.design_bottom_sheet_peek_height_min); 
      paramInt = Math.max(this.mPeekHeightMin, this.mParentHeight - paramCoordinatorLayout.getWidth() * 9 / 16);
    } else {
      paramInt = this.mPeekHeight;
    } 
    this.mMinOffset = Math.max(0, this.mParentHeight - paramV.getHeight());
    this.mMaxOffset = Math.max(this.mParentHeight - paramInt, this.mMinOffset);
    if (this.mState == 3) {
      ViewCompat.offsetTopAndBottom((View)paramV, this.mMinOffset);
    } else if (this.mHideable && this.mState == 5) {
      ViewCompat.offsetTopAndBottom((View)paramV, this.mParentHeight);
    } else if (this.mState == 4) {
      ViewCompat.offsetTopAndBottom((View)paramV, this.mMaxOffset);
    } else if (this.mState == 1 || this.mState == 2) {
      ViewCompat.offsetTopAndBottom((View)paramV, i - paramV.getTop());
    } 
    if (this.mViewDragHelper == null)
      this.mViewDragHelper = ViewDragHelper.create(paramCoordinatorLayout, this.mDragCallback); 
    this.mViewRef = new WeakReference<V>(paramV);
    this.mNestedScrollingChildRef = new WeakReference<View>(findScrollingChild((View)paramV));
    return true;
  }
  
  public boolean onNestedPreFling(CoordinatorLayout paramCoordinatorLayout, V paramV, View paramView, float paramFloat1, float paramFloat2) {
    return (paramView == this.mNestedScrollingChildRef.get() && (this.mState != 3 || super.onNestedPreFling(paramCoordinatorLayout, paramV, paramView, paramFloat1, paramFloat2)));
  }
  
  public void onNestedPreScroll(CoordinatorLayout paramCoordinatorLayout, V paramV, View paramView, int paramInt1, int paramInt2, int[] paramArrayOfint) {
    if (paramView == (View)this.mNestedScrollingChildRef.get()) {
      paramInt1 = paramV.getTop();
      int i = paramInt1 - paramInt2;
      if (paramInt2 > 0) {
        if (i < this.mMinOffset) {
          paramArrayOfint[1] = paramInt1 - this.mMinOffset;
          ViewCompat.offsetTopAndBottom((View)paramV, -paramArrayOfint[1]);
          setStateInternal(3);
        } else {
          paramArrayOfint[1] = paramInt2;
          ViewCompat.offsetTopAndBottom((View)paramV, -paramInt2);
          setStateInternal(1);
        } 
      } else if (paramInt2 < 0 && !ViewCompat.canScrollVertically(paramView, -1)) {
        if (i <= this.mMaxOffset || this.mHideable) {
          paramArrayOfint[1] = paramInt2;
          ViewCompat.offsetTopAndBottom((View)paramV, -paramInt2);
          setStateInternal(1);
        } else {
          paramArrayOfint[1] = paramInt1 - this.mMaxOffset;
          ViewCompat.offsetTopAndBottom((View)paramV, -paramArrayOfint[1]);
          setStateInternal(4);
        } 
      } 
      dispatchOnSlide(paramV.getTop());
      this.mLastNestedScrollDy = paramInt2;
      this.mNestedScrolled = true;
    } 
  }
  
  public void onRestoreInstanceState(CoordinatorLayout paramCoordinatorLayout, V paramV, Parcelable paramParcelable) {
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(paramCoordinatorLayout, paramV, savedState.getSuperState());
    if (savedState.state == 1 || savedState.state == 2) {
      this.mState = 4;
      return;
    } 
    this.mState = savedState.state;
  }
  
  public Parcelable onSaveInstanceState(CoordinatorLayout paramCoordinatorLayout, V paramV) {
    return (Parcelable)new SavedState(super.onSaveInstanceState(paramCoordinatorLayout, paramV), this.mState);
  }
  
  public boolean onStartNestedScroll(CoordinatorLayout paramCoordinatorLayout, V paramV, View paramView1, View paramView2, int paramInt) {
    boolean bool = false;
    this.mLastNestedScrollDy = 0;
    this.mNestedScrolled = false;
    if ((paramInt & 0x2) != 0)
      bool = true; 
    return bool;
  }
  
  public void onStopNestedScroll(CoordinatorLayout paramCoordinatorLayout, V paramV, View paramView) {
    if (paramV.getTop() == this.mMinOffset) {
      setStateInternal(3);
      return;
    } 
    if (paramView == this.mNestedScrollingChildRef.get() && this.mNestedScrolled) {
      int i;
      byte b;
      if (this.mLastNestedScrollDy > 0) {
        i = this.mMinOffset;
        b = 3;
      } else if (this.mHideable && shouldHide((View)paramV, getYVelocity())) {
        i = this.mParentHeight;
        b = 5;
      } else if (this.mLastNestedScrollDy == 0) {
        b = paramV.getTop();
        if (Math.abs(b - this.mMinOffset) < Math.abs(b - this.mMaxOffset)) {
          i = this.mMinOffset;
          b = 3;
        } else {
          i = this.mMaxOffset;
          b = 4;
        } 
      } else {
        i = this.mMaxOffset;
        b = 4;
      } 
      if (this.mViewDragHelper.smoothSlideViewTo((View)paramV, paramV.getLeft(), i)) {
        setStateInternal(2);
        ViewCompat.postOnAnimation((View)paramV, new SettleRunnable((View)paramV, b));
      } else {
        setStateInternal(b);
      } 
      this.mNestedScrolled = false;
    } 
  }
  
  public boolean onTouchEvent(CoordinatorLayout paramCoordinatorLayout, V paramV, MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: iconst_1
    //   1: istore #4
    //   3: aload_2
    //   4: invokevirtual isShown : ()Z
    //   7: ifne -> 16
    //   10: iconst_0
    //   11: istore #5
    //   13: iload #5
    //   15: ireturn
    //   16: aload_3
    //   17: invokestatic getActionMasked : (Landroid/view/MotionEvent;)I
    //   20: istore #6
    //   22: aload_0
    //   23: getfield mState : I
    //   26: iconst_1
    //   27: if_icmpne -> 39
    //   30: iload #4
    //   32: istore #5
    //   34: iload #6
    //   36: ifeq -> 13
    //   39: aload_0
    //   40: getfield mViewDragHelper : Landroid/support/v4/widget/ViewDragHelper;
    //   43: aload_3
    //   44: invokevirtual processTouchEvent : (Landroid/view/MotionEvent;)V
    //   47: iload #6
    //   49: ifne -> 56
    //   52: aload_0
    //   53: invokespecial reset : ()V
    //   56: aload_0
    //   57: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   60: ifnonnull -> 70
    //   63: aload_0
    //   64: invokestatic obtain : ()Landroid/view/VelocityTracker;
    //   67: putfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   70: aload_0
    //   71: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   74: aload_3
    //   75: invokevirtual addMovement : (Landroid/view/MotionEvent;)V
    //   78: iload #6
    //   80: iconst_2
    //   81: if_icmpne -> 132
    //   84: aload_0
    //   85: getfield mIgnoreEvents : Z
    //   88: ifne -> 132
    //   91: aload_0
    //   92: getfield mInitialY : I
    //   95: i2f
    //   96: aload_3
    //   97: invokevirtual getY : ()F
    //   100: fsub
    //   101: invokestatic abs : (F)F
    //   104: aload_0
    //   105: getfield mViewDragHelper : Landroid/support/v4/widget/ViewDragHelper;
    //   108: invokevirtual getTouchSlop : ()I
    //   111: i2f
    //   112: fcmpl
    //   113: ifle -> 132
    //   116: aload_0
    //   117: getfield mViewDragHelper : Landroid/support/v4/widget/ViewDragHelper;
    //   120: aload_2
    //   121: aload_3
    //   122: aload_3
    //   123: invokevirtual getActionIndex : ()I
    //   126: invokevirtual getPointerId : (I)I
    //   129: invokevirtual captureChildView : (Landroid/view/View;I)V
    //   132: iload #4
    //   134: istore #5
    //   136: aload_0
    //   137: getfield mIgnoreEvents : Z
    //   140: ifeq -> 13
    //   143: iconst_0
    //   144: istore #5
    //   146: goto -> 13
  }
  
  public void setBottomSheetCallback(BottomSheetCallback paramBottomSheetCallback) {
    this.mCallback = paramBottomSheetCallback;
  }
  
  public void setHideable(boolean paramBoolean) {
    this.mHideable = paramBoolean;
  }
  
  public final void setPeekHeight(int paramInt) {
    boolean bool = false;
    if (paramInt == -1) {
      if (!this.mPeekHeightAuto) {
        this.mPeekHeightAuto = true;
        bool = true;
      } 
    } else if (this.mPeekHeightAuto || this.mPeekHeight != paramInt) {
      this.mPeekHeightAuto = false;
      this.mPeekHeight = Math.max(0, paramInt);
      this.mMaxOffset = this.mParentHeight - paramInt;
      bool = true;
    } 
    if (bool && this.mState == 4 && this.mViewRef != null) {
      View view = (View)this.mViewRef.get();
      if (view != null)
        view.requestLayout(); 
    } 
  }
  
  public void setSkipCollapsed(boolean paramBoolean) {
    this.mSkipCollapsed = paramBoolean;
  }
  
  public final void setState(final int state) {
    if (state != this.mState) {
      if (this.mViewRef == null) {
        if (state == 4 || state == 3 || (this.mHideable && state == 5))
          this.mState = state; 
        return;
      } 
      final View child = (View)this.mViewRef.get();
      if (view != null) {
        ViewParent viewParent = view.getParent();
        if (viewParent != null && viewParent.isLayoutRequested() && ViewCompat.isAttachedToWindow(view)) {
          view.post(new Runnable() {
                public void run() {
                  BottomSheetBehavior.this.startSettlingAnimation(child, state);
                }
              });
          return;
        } 
        startSettlingAnimation(view, state);
      } 
    } 
  }
  
  void setStateInternal(int paramInt) {
    if (this.mState != paramInt) {
      this.mState = paramInt;
      View view = (View)this.mViewRef.get();
      if (view != null && this.mCallback != null)
        this.mCallback.onStateChanged(view, paramInt); 
    } 
  }
  
  boolean shouldHide(View paramView, float paramFloat) {
    boolean bool = true;
    if (!this.mSkipCollapsed) {
      if (paramView.getTop() < this.mMaxOffset)
        return false; 
      if (Math.abs(paramView.getTop() + 0.1F * paramFloat - this.mMaxOffset) / this.mPeekHeight <= 0.5F)
        bool = false; 
    } 
    return bool;
  }
  
  void startSettlingAnimation(View paramView, int paramInt) {
    int i;
    if (paramInt == 4) {
      i = this.mMaxOffset;
    } else if (paramInt == 3) {
      i = this.mMinOffset;
    } else if (this.mHideable && paramInt == 5) {
      i = this.mParentHeight;
    } else {
      throw new IllegalArgumentException("Illegal state argument: " + paramInt);
    } 
    setStateInternal(2);
    if (this.mViewDragHelper.smoothSlideViewTo(paramView, paramView.getLeft(), i))
      ViewCompat.postOnAnimation(paramView, new SettleRunnable(paramView, paramInt)); 
  }
  
  public static abstract class BottomSheetCallback {
    public abstract void onSlide(@NonNull View param1View, float param1Float);
    
    public abstract void onStateChanged(@NonNull View param1View, int param1Int);
  }
  
  protected static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
          public BottomSheetBehavior.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
            return new BottomSheetBehavior.SavedState(param2Parcel, param2ClassLoader);
          }
          
          public BottomSheetBehavior.SavedState[] newArray(int param2Int) {
            return new BottomSheetBehavior.SavedState[param2Int];
          }
        });
    
    final int state;
    
    public SavedState(Parcel param1Parcel) {
      this(param1Parcel, (ClassLoader)null);
    }
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      this.state = param1Parcel.readInt();
    }
    
    public SavedState(Parcelable param1Parcelable, int param1Int) {
      super(param1Parcelable);
      this.state = param1Int;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.state);
    }
  }
  
  static final class null implements ParcelableCompatCreatorCallbacks<SavedState> {
    public BottomSheetBehavior.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new BottomSheetBehavior.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public BottomSheetBehavior.SavedState[] newArray(int param1Int) {
      return new BottomSheetBehavior.SavedState[param1Int];
    }
  }
  
  private class SettleRunnable implements Runnable {
    private final int mTargetState;
    
    private final View mView;
    
    SettleRunnable(View param1View, int param1Int) {
      this.mView = param1View;
      this.mTargetState = param1Int;
    }
    
    public void run() {
      if (BottomSheetBehavior.this.mViewDragHelper != null && BottomSheetBehavior.this.mViewDragHelper.continueSettling(true)) {
        ViewCompat.postOnAnimation(this.mView, this);
        return;
      } 
      BottomSheetBehavior.this.setStateInternal(this.mTargetState);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface State {}
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/BottomSheetBehavior.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */