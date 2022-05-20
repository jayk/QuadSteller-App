package android.support.v4.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import java.util.Arrays;

public class ViewDragHelper {
  private static final int BASE_SETTLE_DURATION = 256;
  
  public static final int DIRECTION_ALL = 3;
  
  public static final int DIRECTION_HORIZONTAL = 1;
  
  public static final int DIRECTION_VERTICAL = 2;
  
  public static final int EDGE_ALL = 15;
  
  public static final int EDGE_BOTTOM = 8;
  
  public static final int EDGE_LEFT = 1;
  
  public static final int EDGE_RIGHT = 2;
  
  private static final int EDGE_SIZE = 20;
  
  public static final int EDGE_TOP = 4;
  
  public static final int INVALID_POINTER = -1;
  
  private static final int MAX_SETTLE_DURATION = 600;
  
  public static final int STATE_DRAGGING = 1;
  
  public static final int STATE_IDLE = 0;
  
  public static final int STATE_SETTLING = 2;
  
  private static final String TAG = "ViewDragHelper";
  
  private static final Interpolator sInterpolator = new Interpolator() {
      public float getInterpolation(float param1Float) {
        param1Float--;
        return param1Float * param1Float * param1Float * param1Float * param1Float + 1.0F;
      }
    };
  
  private int mActivePointerId = -1;
  
  private final Callback mCallback;
  
  private View mCapturedView;
  
  private int mDragState;
  
  private int[] mEdgeDragsInProgress;
  
  private int[] mEdgeDragsLocked;
  
  private int mEdgeSize;
  
  private int[] mInitialEdgesTouched;
  
  private float[] mInitialMotionX;
  
  private float[] mInitialMotionY;
  
  private float[] mLastMotionX;
  
  private float[] mLastMotionY;
  
  private float mMaxVelocity;
  
  private float mMinVelocity;
  
  private final ViewGroup mParentView;
  
  private int mPointersDown;
  
  private boolean mReleaseInProgress;
  
  private ScrollerCompat mScroller;
  
  private final Runnable mSetIdleRunnable = new Runnable() {
      public void run() {
        ViewDragHelper.this.setDragState(0);
      }
    };
  
  private int mTouchSlop;
  
  private int mTrackingEdges;
  
  private VelocityTracker mVelocityTracker;
  
  private ViewDragHelper(Context paramContext, ViewGroup paramViewGroup, Callback paramCallback) {
    if (paramViewGroup == null)
      throw new IllegalArgumentException("Parent view may not be null"); 
    if (paramCallback == null)
      throw new IllegalArgumentException("Callback may not be null"); 
    this.mParentView = paramViewGroup;
    this.mCallback = paramCallback;
    ViewConfiguration viewConfiguration = ViewConfiguration.get(paramContext);
    this.mEdgeSize = (int)(20.0F * (paramContext.getResources().getDisplayMetrics()).density + 0.5F);
    this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
    this.mMaxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
    this.mMinVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    this.mScroller = ScrollerCompat.create(paramContext, sInterpolator);
  }
  
  private boolean checkNewEdgeDrag(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2) {
    boolean bool1 = false;
    paramFloat1 = Math.abs(paramFloat1);
    paramFloat2 = Math.abs(paramFloat2);
    boolean bool2 = bool1;
    if ((this.mInitialEdgesTouched[paramInt1] & paramInt2) == paramInt2) {
      bool2 = bool1;
      if ((this.mTrackingEdges & paramInt2) != 0) {
        bool2 = bool1;
        if ((this.mEdgeDragsLocked[paramInt1] & paramInt2) != paramInt2) {
          bool2 = bool1;
          if ((this.mEdgeDragsInProgress[paramInt1] & paramInt2) != paramInt2) {
            if (paramFloat1 <= this.mTouchSlop && paramFloat2 <= this.mTouchSlop)
              return bool1; 
          } else {
            return bool2;
          } 
        } else {
          return bool2;
        } 
      } else {
        return bool2;
      } 
    } else {
      return bool2;
    } 
    if (paramFloat1 < 0.5F * paramFloat2 && this.mCallback.onEdgeLock(paramInt2)) {
      int[] arrayOfInt = this.mEdgeDragsLocked;
      arrayOfInt[paramInt1] = arrayOfInt[paramInt1] | paramInt2;
      return bool1;
    } 
    bool2 = bool1;
    if ((this.mEdgeDragsInProgress[paramInt1] & paramInt2) == 0) {
      bool2 = bool1;
      if (paramFloat1 > this.mTouchSlop)
        bool2 = true; 
    } 
    return bool2;
  }
  
  private boolean checkTouchSlop(View paramView, float paramFloat1, float paramFloat2) {
    boolean bool1;
    boolean bool2;
    null = true;
    if (paramView == null)
      return false; 
    if (this.mCallback.getViewHorizontalDragRange(paramView) > 0) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (this.mCallback.getViewVerticalDragRange(paramView) > 0) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (bool1 && bool2) {
      if (paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2 <= (this.mTouchSlop * this.mTouchSlop))
        null = false; 
      return null;
    } 
    if (bool1) {
      if (Math.abs(paramFloat1) <= this.mTouchSlop)
        null = false; 
      return null;
    } 
    if (bool2) {
      if (Math.abs(paramFloat2) <= this.mTouchSlop)
        null = false; 
      return null;
    } 
    return false;
  }
  
  private float clampMag(float paramFloat1, float paramFloat2, float paramFloat3) {
    float f = Math.abs(paramFloat1);
    if (f < paramFloat2)
      return 0.0F; 
    if (f > paramFloat3) {
      paramFloat2 = paramFloat3;
      if (paramFloat1 <= 0.0F)
        paramFloat2 = -paramFloat3; 
      return paramFloat2;
    } 
    return paramFloat1;
  }
  
  private int clampMag(int paramInt1, int paramInt2, int paramInt3) {
    int i = Math.abs(paramInt1);
    if (i < paramInt2)
      return 0; 
    if (i > paramInt3) {
      paramInt2 = paramInt3;
      if (paramInt1 <= 0)
        paramInt2 = -paramInt3; 
      return paramInt2;
    } 
    return paramInt1;
  }
  
  private void clearMotionHistory() {
    if (this.mInitialMotionX != null) {
      Arrays.fill(this.mInitialMotionX, 0.0F);
      Arrays.fill(this.mInitialMotionY, 0.0F);
      Arrays.fill(this.mLastMotionX, 0.0F);
      Arrays.fill(this.mLastMotionY, 0.0F);
      Arrays.fill(this.mInitialEdgesTouched, 0);
      Arrays.fill(this.mEdgeDragsInProgress, 0);
      Arrays.fill(this.mEdgeDragsLocked, 0);
      this.mPointersDown = 0;
    } 
  }
  
  private void clearMotionHistory(int paramInt) {
    if (this.mInitialMotionX != null && isPointerDown(paramInt)) {
      this.mInitialMotionX[paramInt] = 0.0F;
      this.mInitialMotionY[paramInt] = 0.0F;
      this.mLastMotionX[paramInt] = 0.0F;
      this.mLastMotionY[paramInt] = 0.0F;
      this.mInitialEdgesTouched[paramInt] = 0;
      this.mEdgeDragsInProgress[paramInt] = 0;
      this.mEdgeDragsLocked[paramInt] = 0;
      this.mPointersDown &= 1 << paramInt ^ 0xFFFFFFFF;
    } 
  }
  
  private int computeAxisDuration(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt1 == 0)
      return 0; 
    int i = this.mParentView.getWidth();
    int j = i / 2;
    float f1 = Math.min(1.0F, Math.abs(paramInt1) / i);
    float f2 = j;
    float f3 = j;
    f1 = distanceInfluenceForSnapDuration(f1);
    paramInt2 = Math.abs(paramInt2);
    if (paramInt2 > 0) {
      paramInt1 = Math.round(1000.0F * Math.abs((f2 + f3 * f1) / paramInt2)) * 4;
    } else {
      paramInt1 = (int)((Math.abs(paramInt1) / paramInt3 + 1.0F) * 256.0F);
    } 
    return Math.min(paramInt1, 600);
  }
  
  private int computeSettleDuration(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    float f1;
    paramInt3 = clampMag(paramInt3, (int)this.mMinVelocity, (int)this.mMaxVelocity);
    paramInt4 = clampMag(paramInt4, (int)this.mMinVelocity, (int)this.mMaxVelocity);
    int i = Math.abs(paramInt1);
    int j = Math.abs(paramInt2);
    int k = Math.abs(paramInt3);
    int m = Math.abs(paramInt4);
    int n = k + m;
    int i1 = i + j;
    if (paramInt3 != 0) {
      f1 = k / n;
    } else {
      f1 = i / i1;
    } 
    if (paramInt4 != 0) {
      float f = m / n;
      paramInt1 = computeAxisDuration(paramInt1, paramInt3, this.mCallback.getViewHorizontalDragRange(paramView));
      paramInt2 = computeAxisDuration(paramInt2, paramInt4, this.mCallback.getViewVerticalDragRange(paramView));
      return (int)(paramInt1 * f1 + paramInt2 * f);
    } 
    float f2 = j / i1;
    paramInt1 = computeAxisDuration(paramInt1, paramInt3, this.mCallback.getViewHorizontalDragRange(paramView));
    paramInt2 = computeAxisDuration(paramInt2, paramInt4, this.mCallback.getViewVerticalDragRange(paramView));
    return (int)(paramInt1 * f1 + paramInt2 * f2);
  }
  
  public static ViewDragHelper create(ViewGroup paramViewGroup, float paramFloat, Callback paramCallback) {
    ViewDragHelper viewDragHelper = create(paramViewGroup, paramCallback);
    viewDragHelper.mTouchSlop = (int)(viewDragHelper.mTouchSlop * 1.0F / paramFloat);
    return viewDragHelper;
  }
  
  public static ViewDragHelper create(ViewGroup paramViewGroup, Callback paramCallback) {
    return new ViewDragHelper(paramViewGroup.getContext(), paramViewGroup, paramCallback);
  }
  
  private void dispatchViewReleased(float paramFloat1, float paramFloat2) {
    this.mReleaseInProgress = true;
    this.mCallback.onViewReleased(this.mCapturedView, paramFloat1, paramFloat2);
    this.mReleaseInProgress = false;
    if (this.mDragState == 1)
      setDragState(0); 
  }
  
  private float distanceInfluenceForSnapDuration(float paramFloat) {
    return (float)Math.sin((float)((paramFloat - 0.5F) * 0.4712389167638204D));
  }
  
  private void dragTo(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = paramInt1;
    int j = paramInt2;
    int k = this.mCapturedView.getLeft();
    int m = this.mCapturedView.getTop();
    if (paramInt3 != 0) {
      i = this.mCallback.clampViewPositionHorizontal(this.mCapturedView, paramInt1, paramInt3);
      ViewCompat.offsetLeftAndRight(this.mCapturedView, i - k);
    } 
    if (paramInt4 != 0) {
      j = this.mCallback.clampViewPositionVertical(this.mCapturedView, paramInt2, paramInt4);
      ViewCompat.offsetTopAndBottom(this.mCapturedView, j - m);
    } 
    if (paramInt3 != 0 || paramInt4 != 0)
      this.mCallback.onViewPositionChanged(this.mCapturedView, i, j, i - k, j - m); 
  }
  
  private void ensureMotionHistorySizeForId(int paramInt) {
    if (this.mInitialMotionX == null || this.mInitialMotionX.length <= paramInt) {
      float[] arrayOfFloat1 = new float[paramInt + 1];
      float[] arrayOfFloat2 = new float[paramInt + 1];
      float[] arrayOfFloat3 = new float[paramInt + 1];
      float[] arrayOfFloat4 = new float[paramInt + 1];
      int[] arrayOfInt1 = new int[paramInt + 1];
      int[] arrayOfInt2 = new int[paramInt + 1];
      int[] arrayOfInt3 = new int[paramInt + 1];
      if (this.mInitialMotionX != null) {
        System.arraycopy(this.mInitialMotionX, 0, arrayOfFloat1, 0, this.mInitialMotionX.length);
        System.arraycopy(this.mInitialMotionY, 0, arrayOfFloat2, 0, this.mInitialMotionY.length);
        System.arraycopy(this.mLastMotionX, 0, arrayOfFloat3, 0, this.mLastMotionX.length);
        System.arraycopy(this.mLastMotionY, 0, arrayOfFloat4, 0, this.mLastMotionY.length);
        System.arraycopy(this.mInitialEdgesTouched, 0, arrayOfInt1, 0, this.mInitialEdgesTouched.length);
        System.arraycopy(this.mEdgeDragsInProgress, 0, arrayOfInt2, 0, this.mEdgeDragsInProgress.length);
        System.arraycopy(this.mEdgeDragsLocked, 0, arrayOfInt3, 0, this.mEdgeDragsLocked.length);
      } 
      this.mInitialMotionX = arrayOfFloat1;
      this.mInitialMotionY = arrayOfFloat2;
      this.mLastMotionX = arrayOfFloat3;
      this.mLastMotionY = arrayOfFloat4;
      this.mInitialEdgesTouched = arrayOfInt1;
      this.mEdgeDragsInProgress = arrayOfInt2;
      this.mEdgeDragsLocked = arrayOfInt3;
    } 
  }
  
  private boolean forceSettleCapturedViewAt(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    null = false;
    int i = this.mCapturedView.getLeft();
    int j = this.mCapturedView.getTop();
    paramInt1 -= i;
    paramInt2 -= j;
    if (paramInt1 == 0 && paramInt2 == 0) {
      this.mScroller.abortAnimation();
      setDragState(0);
      return null;
    } 
    paramInt3 = computeSettleDuration(this.mCapturedView, paramInt1, paramInt2, paramInt3, paramInt4);
    this.mScroller.startScroll(i, j, paramInt1, paramInt2, paramInt3);
    setDragState(2);
    return true;
  }
  
  private int getEdgesTouched(int paramInt1, int paramInt2) {
    int i = 0;
    if (paramInt1 < this.mParentView.getLeft() + this.mEdgeSize)
      i = false | true; 
    int j = i;
    if (paramInt2 < this.mParentView.getTop() + this.mEdgeSize)
      j = i | 0x4; 
    i = j;
    if (paramInt1 > this.mParentView.getRight() - this.mEdgeSize)
      i = j | 0x2; 
    paramInt1 = i;
    if (paramInt2 > this.mParentView.getBottom() - this.mEdgeSize)
      paramInt1 = i | 0x8; 
    return paramInt1;
  }
  
  private boolean isValidPointerForActionMove(int paramInt) {
    if (!isPointerDown(paramInt)) {
      Log.e("ViewDragHelper", "Ignoring pointerId=" + paramInt + " because ACTION_DOWN was not received " + "for this pointer before ACTION_MOVE. It likely happened because " + " ViewDragHelper did not receive all the events in the event stream.");
      return false;
    } 
    return true;
  }
  
  private void releaseViewForPointerUp() {
    this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
    dispatchViewReleased(clampMag(VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity), clampMag(VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity));
  }
  
  private void reportNewEdgeDrags(float paramFloat1, float paramFloat2, int paramInt) {
    int i = 0;
    if (checkNewEdgeDrag(paramFloat1, paramFloat2, paramInt, 1))
      i = false | true; 
    int j = i;
    if (checkNewEdgeDrag(paramFloat2, paramFloat1, paramInt, 4))
      j = i | 0x4; 
    i = j;
    if (checkNewEdgeDrag(paramFloat1, paramFloat2, paramInt, 2))
      i = j | 0x2; 
    j = i;
    if (checkNewEdgeDrag(paramFloat2, paramFloat1, paramInt, 8))
      j = i | 0x8; 
    if (j != 0) {
      int[] arrayOfInt = this.mEdgeDragsInProgress;
      arrayOfInt[paramInt] = arrayOfInt[paramInt] | j;
      this.mCallback.onEdgeDragStarted(j, paramInt);
    } 
  }
  
  private void saveInitialMotion(float paramFloat1, float paramFloat2, int paramInt) {
    ensureMotionHistorySizeForId(paramInt);
    float[] arrayOfFloat = this.mInitialMotionX;
    this.mLastMotionX[paramInt] = paramFloat1;
    arrayOfFloat[paramInt] = paramFloat1;
    arrayOfFloat = this.mInitialMotionY;
    this.mLastMotionY[paramInt] = paramFloat2;
    arrayOfFloat[paramInt] = paramFloat2;
    this.mInitialEdgesTouched[paramInt] = getEdgesTouched((int)paramFloat1, (int)paramFloat2);
    this.mPointersDown |= 1 << paramInt;
  }
  
  private void saveLastMotion(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getPointerCount();
    for (byte b = 0; b < i; b++) {
      int j = paramMotionEvent.getPointerId(b);
      if (isValidPointerForActionMove(j)) {
        float f1 = paramMotionEvent.getX(b);
        float f2 = paramMotionEvent.getY(b);
        this.mLastMotionX[j] = f1;
        this.mLastMotionY[j] = f2;
      } 
    } 
  }
  
  public void abort() {
    cancel();
    if (this.mDragState == 2) {
      int i = this.mScroller.getCurrX();
      int j = this.mScroller.getCurrY();
      this.mScroller.abortAnimation();
      int k = this.mScroller.getCurrX();
      int m = this.mScroller.getCurrY();
      this.mCallback.onViewPositionChanged(this.mCapturedView, k, m, k - i, m - j);
    } 
    setDragState(0);
  }
  
  protected boolean canScroll(View paramView, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (paramView instanceof ViewGroup) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      int i = paramView.getScrollX();
      int j = paramView.getScrollY();
      for (int k = viewGroup.getChildCount() - 1; k >= 0; k--) {
        View view = viewGroup.getChildAt(k);
        if (paramInt3 + i >= view.getLeft() && paramInt3 + i < view.getRight() && paramInt4 + j >= view.getTop() && paramInt4 + j < view.getBottom() && canScroll(view, true, paramInt1, paramInt2, paramInt3 + i - view.getLeft(), paramInt4 + j - view.getTop()))
          return true; 
      } 
    } 
    return (paramBoolean && (ViewCompat.canScrollHorizontally(paramView, -paramInt1) || ViewCompat.canScrollVertically(paramView, -paramInt2)));
  }
  
  public void cancel() {
    this.mActivePointerId = -1;
    clearMotionHistory();
    if (this.mVelocityTracker != null) {
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
    } 
  }
  
  public void captureChildView(View paramView, int paramInt) {
    if (paramView.getParent() != this.mParentView)
      throw new IllegalArgumentException("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (" + this.mParentView + ")"); 
    this.mCapturedView = paramView;
    this.mActivePointerId = paramInt;
    this.mCallback.onViewCaptured(paramView, paramInt);
    setDragState(1);
  }
  
  public boolean checkTouchSlop(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mInitialMotionX : [F
    //   4: arraylength
    //   5: istore_2
    //   6: iconst_0
    //   7: istore_3
    //   8: iload_3
    //   9: iload_2
    //   10: if_icmpge -> 34
    //   13: aload_0
    //   14: iload_1
    //   15: iload_3
    //   16: invokevirtual checkTouchSlop : (II)Z
    //   19: ifeq -> 28
    //   22: iconst_1
    //   23: istore #4
    //   25: iload #4
    //   27: ireturn
    //   28: iinc #3, 1
    //   31: goto -> 8
    //   34: iconst_0
    //   35: istore #4
    //   37: goto -> 25
  }
  
  public boolean checkTouchSlop(int paramInt1, int paramInt2) {
    boolean bool;
    null = true;
    if (!isPointerDown(paramInt2))
      return false; 
    if ((paramInt1 & 0x1) == 1) {
      bool = true;
    } else {
      bool = false;
    } 
    if ((paramInt1 & 0x2) == 2) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    float f1 = this.mLastMotionX[paramInt2] - this.mInitialMotionX[paramInt2];
    float f2 = this.mLastMotionY[paramInt2] - this.mInitialMotionY[paramInt2];
    if (bool && paramInt1 != 0) {
      if (f1 * f1 + f2 * f2 <= (this.mTouchSlop * this.mTouchSlop))
        null = false; 
      return null;
    } 
    if (bool) {
      if (Math.abs(f1) <= this.mTouchSlop)
        null = false; 
      return null;
    } 
    if (paramInt1 != 0) {
      if (Math.abs(f2) <= this.mTouchSlop)
        null = false; 
      return null;
    } 
    return false;
  }
  
  public boolean continueSettling(boolean paramBoolean) {
    if (this.mDragState == 2) {
      boolean bool1 = this.mScroller.computeScrollOffset();
      int i = this.mScroller.getCurrX();
      int j = this.mScroller.getCurrY();
      int k = i - this.mCapturedView.getLeft();
      int m = j - this.mCapturedView.getTop();
      if (k != 0)
        ViewCompat.offsetLeftAndRight(this.mCapturedView, k); 
      if (m != 0)
        ViewCompat.offsetTopAndBottom(this.mCapturedView, m); 
      if (k != 0 || m != 0)
        this.mCallback.onViewPositionChanged(this.mCapturedView, i, j, k, m); 
      boolean bool2 = bool1;
      if (bool1) {
        bool2 = bool1;
        if (i == this.mScroller.getFinalX()) {
          bool2 = bool1;
          if (j == this.mScroller.getFinalY()) {
            this.mScroller.abortAnimation();
            bool2 = false;
          } 
        } 
      } 
      if (!bool2)
        if (paramBoolean) {
          this.mParentView.post(this.mSetIdleRunnable);
        } else {
          setDragState(0);
        }  
    } 
    return (this.mDragState == 2);
  }
  
  public View findTopChildUnder(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mParentView : Landroid/view/ViewGroup;
    //   4: invokevirtual getChildCount : ()I
    //   7: iconst_1
    //   8: isub
    //   9: istore_3
    //   10: iload_3
    //   11: iflt -> 76
    //   14: aload_0
    //   15: getfield mParentView : Landroid/view/ViewGroup;
    //   18: aload_0
    //   19: getfield mCallback : Landroid/support/v4/widget/ViewDragHelper$Callback;
    //   22: iload_3
    //   23: invokevirtual getOrderedChildIndex : (I)I
    //   26: invokevirtual getChildAt : (I)Landroid/view/View;
    //   29: astore #4
    //   31: iload_1
    //   32: aload #4
    //   34: invokevirtual getLeft : ()I
    //   37: if_icmplt -> 70
    //   40: iload_1
    //   41: aload #4
    //   43: invokevirtual getRight : ()I
    //   46: if_icmpge -> 70
    //   49: iload_2
    //   50: aload #4
    //   52: invokevirtual getTop : ()I
    //   55: if_icmplt -> 70
    //   58: iload_2
    //   59: aload #4
    //   61: invokevirtual getBottom : ()I
    //   64: if_icmpge -> 70
    //   67: aload #4
    //   69: areturn
    //   70: iinc #3, -1
    //   73: goto -> 10
    //   76: aconst_null
    //   77: astore #4
    //   79: goto -> 67
  }
  
  public void flingCapturedView(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (!this.mReleaseInProgress)
      throw new IllegalStateException("Cannot flingCapturedView outside of a call to Callback#onViewReleased"); 
    this.mScroller.fling(this.mCapturedView.getLeft(), this.mCapturedView.getTop(), (int)VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), (int)VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId), paramInt1, paramInt3, paramInt2, paramInt4);
    setDragState(2);
  }
  
  public int getActivePointerId() {
    return this.mActivePointerId;
  }
  
  public View getCapturedView() {
    return this.mCapturedView;
  }
  
  public int getEdgeSize() {
    return this.mEdgeSize;
  }
  
  public float getMinVelocity() {
    return this.mMinVelocity;
  }
  
  public int getTouchSlop() {
    return this.mTouchSlop;
  }
  
  public int getViewDragState() {
    return this.mDragState;
  }
  
  public boolean isCapturedViewUnder(int paramInt1, int paramInt2) {
    return isViewUnder(this.mCapturedView, paramInt1, paramInt2);
  }
  
  public boolean isEdgeTouched(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mInitialEdgesTouched : [I
    //   4: arraylength
    //   5: istore_2
    //   6: iconst_0
    //   7: istore_3
    //   8: iload_3
    //   9: iload_2
    //   10: if_icmpge -> 34
    //   13: aload_0
    //   14: iload_1
    //   15: iload_3
    //   16: invokevirtual isEdgeTouched : (II)Z
    //   19: ifeq -> 28
    //   22: iconst_1
    //   23: istore #4
    //   25: iload #4
    //   27: ireturn
    //   28: iinc #3, 1
    //   31: goto -> 8
    //   34: iconst_0
    //   35: istore #4
    //   37: goto -> 25
  }
  
  public boolean isEdgeTouched(int paramInt1, int paramInt2) {
    return (isPointerDown(paramInt2) && (this.mInitialEdgesTouched[paramInt2] & paramInt1) != 0);
  }
  
  public boolean isPointerDown(int paramInt) {
    boolean bool = true;
    if ((this.mPointersDown & 1 << paramInt) == 0)
      bool = false; 
    return bool;
  }
  
  public boolean isViewUnder(View paramView, int paramInt1, int paramInt2) {
    boolean bool1 = false;
    if (paramView == null)
      return bool1; 
    boolean bool2 = bool1;
    if (paramInt1 >= paramView.getLeft()) {
      bool2 = bool1;
      if (paramInt1 < paramView.getRight()) {
        bool2 = bool1;
        if (paramInt2 >= paramView.getTop()) {
          bool2 = bool1;
          if (paramInt2 < paramView.getBottom())
            bool2 = true; 
        } 
      } 
    } 
    return bool2;
  }
  
  public void processTouchEvent(MotionEvent paramMotionEvent) {
    View view;
    float f1;
    float f2;
    int k;
    int i = MotionEventCompat.getActionMasked(paramMotionEvent);
    int j = MotionEventCompat.getActionIndex(paramMotionEvent);
    if (i == 0)
      cancel(); 
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
    this.mVelocityTracker.addMovement(paramMotionEvent);
    switch (i) {
      default:
        return;
      case 0:
        f1 = paramMotionEvent.getX();
        f2 = paramMotionEvent.getY();
        i = paramMotionEvent.getPointerId(0);
        view = findTopChildUnder((int)f1, (int)f2);
        saveInitialMotion(f1, f2, i);
        tryCaptureViewForDrag(view, i);
        j = this.mInitialEdgesTouched[i];
        if ((this.mTrackingEdges & j) != 0)
          this.mCallback.onEdgeTouched(this.mTrackingEdges & j, i); 
      case 5:
        i = view.getPointerId(j);
        f1 = view.getX(j);
        f2 = view.getY(j);
        saveInitialMotion(f1, f2, i);
        if (this.mDragState == 0) {
          tryCaptureViewForDrag(findTopChildUnder((int)f1, (int)f2), i);
          j = this.mInitialEdgesTouched[i];
          if ((this.mTrackingEdges & j) != 0)
            this.mCallback.onEdgeTouched(this.mTrackingEdges & j, i); 
        } 
        if (isCapturedViewUnder((int)f1, (int)f2))
          tryCaptureViewForDrag(this.mCapturedView, i); 
      case 2:
        if (this.mDragState == 1)
          if (isValidPointerForActionMove(this.mActivePointerId)) {
            j = view.findPointerIndex(this.mActivePointerId);
            f2 = view.getX(j);
            f1 = view.getY(j);
            j = (int)(f2 - this.mLastMotionX[this.mActivePointerId]);
            i = (int)(f1 - this.mLastMotionY[this.mActivePointerId]);
            dragTo(this.mCapturedView.getLeft() + j, this.mCapturedView.getTop() + i, j, i);
            saveLastMotion((MotionEvent)view);
          }  
        i = view.getPointerCount();
        j = 0;
        while (true) {
          if (j < i) {
            int m = view.getPointerId(j);
            if (isValidPointerForActionMove(m)) {
              f2 = view.getX(j);
              f1 = view.getY(j);
              float f3 = f2 - this.mInitialMotionX[m];
              float f4 = f1 - this.mInitialMotionY[m];
              reportNewEdgeDrags(f3, f4, m);
              if (this.mDragState == 1)
                saveLastMotion((MotionEvent)view); 
              View view1 = findTopChildUnder((int)f2, (int)f1);
              if (checkTouchSlop(view1, f3, f4) && tryCaptureViewForDrag(view1, m))
                saveLastMotion((MotionEvent)view); 
            } 
            j++;
            continue;
          } 
          saveLastMotion((MotionEvent)view);
        } 
      case 6:
        k = view.getPointerId(j);
        if (this.mDragState == 1 && k == this.mActivePointerId) {
          byte b = -1;
          int m = view.getPointerCount();
          j = 0;
          while (true) {
            i = b;
            if (j < m) {
              i = view.getPointerId(j);
              if (i != this.mActivePointerId) {
                f1 = view.getX(j);
                f2 = view.getY(j);
                if (findTopChildUnder((int)f1, (int)f2) == this.mCapturedView && tryCaptureViewForDrag(this.mCapturedView, i)) {
                  i = this.mActivePointerId;
                  break;
                } 
              } 
              j++;
              continue;
            } 
            break;
          } 
          if (i == -1)
            releaseViewForPointerUp(); 
        } 
        clearMotionHistory(k);
      case 1:
        if (this.mDragState == 1)
          releaseViewForPointerUp(); 
        cancel();
      case 3:
        break;
    } 
    if (this.mDragState == 1)
      dispatchViewReleased(0.0F, 0.0F); 
    cancel();
  }
  
  void setDragState(int paramInt) {
    this.mParentView.removeCallbacks(this.mSetIdleRunnable);
    if (this.mDragState != paramInt) {
      this.mDragState = paramInt;
      this.mCallback.onViewDragStateChanged(paramInt);
      if (this.mDragState == 0)
        this.mCapturedView = null; 
    } 
  }
  
  public void setEdgeTrackingEnabled(int paramInt) {
    this.mTrackingEdges = paramInt;
  }
  
  public void setMinVelocity(float paramFloat) {
    this.mMinVelocity = paramFloat;
  }
  
  public boolean settleCapturedViewAt(int paramInt1, int paramInt2) {
    if (!this.mReleaseInProgress)
      throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased"); 
    return forceSettleCapturedViewAt(paramInt1, paramInt2, (int)VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), (int)VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId));
  }
  
  public boolean shouldInterceptTouchEvent(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: aload_1
    //   1: invokestatic getActionMasked : (Landroid/view/MotionEvent;)I
    //   4: istore_2
    //   5: aload_1
    //   6: invokestatic getActionIndex : (Landroid/view/MotionEvent;)I
    //   9: istore_3
    //   10: iload_2
    //   11: ifne -> 18
    //   14: aload_0
    //   15: invokevirtual cancel : ()V
    //   18: aload_0
    //   19: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   22: ifnonnull -> 32
    //   25: aload_0
    //   26: invokestatic obtain : ()Landroid/view/VelocityTracker;
    //   29: putfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   32: aload_0
    //   33: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   36: aload_1
    //   37: invokevirtual addMovement : (Landroid/view/MotionEvent;)V
    //   40: iload_2
    //   41: tableswitch default -> 84, 0 -> 98, 1 -> 601, 2 -> 298, 3 -> 601, 4 -> 84, 5 -> 192, 6 -> 589
    //   84: aload_0
    //   85: getfield mDragState : I
    //   88: iconst_1
    //   89: if_icmpne -> 608
    //   92: iconst_1
    //   93: istore #4
    //   95: iload #4
    //   97: ireturn
    //   98: aload_1
    //   99: invokevirtual getX : ()F
    //   102: fstore #5
    //   104: aload_1
    //   105: invokevirtual getY : ()F
    //   108: fstore #6
    //   110: aload_1
    //   111: iconst_0
    //   112: invokevirtual getPointerId : (I)I
    //   115: istore_3
    //   116: aload_0
    //   117: fload #5
    //   119: fload #6
    //   121: iload_3
    //   122: invokespecial saveInitialMotion : (FFI)V
    //   125: aload_0
    //   126: fload #5
    //   128: f2i
    //   129: fload #6
    //   131: f2i
    //   132: invokevirtual findTopChildUnder : (II)Landroid/view/View;
    //   135: astore_1
    //   136: aload_1
    //   137: aload_0
    //   138: getfield mCapturedView : Landroid/view/View;
    //   141: if_acmpne -> 159
    //   144: aload_0
    //   145: getfield mDragState : I
    //   148: iconst_2
    //   149: if_icmpne -> 159
    //   152: aload_0
    //   153: aload_1
    //   154: iload_3
    //   155: invokevirtual tryCaptureViewForDrag : (Landroid/view/View;I)Z
    //   158: pop
    //   159: aload_0
    //   160: getfield mInitialEdgesTouched : [I
    //   163: iload_3
    //   164: iaload
    //   165: istore_2
    //   166: aload_0
    //   167: getfield mTrackingEdges : I
    //   170: iload_2
    //   171: iand
    //   172: ifeq -> 84
    //   175: aload_0
    //   176: getfield mCallback : Landroid/support/v4/widget/ViewDragHelper$Callback;
    //   179: aload_0
    //   180: getfield mTrackingEdges : I
    //   183: iload_2
    //   184: iand
    //   185: iload_3
    //   186: invokevirtual onEdgeTouched : (II)V
    //   189: goto -> 84
    //   192: aload_1
    //   193: iload_3
    //   194: invokevirtual getPointerId : (I)I
    //   197: istore_2
    //   198: aload_1
    //   199: iload_3
    //   200: invokevirtual getX : (I)F
    //   203: fstore #6
    //   205: aload_1
    //   206: iload_3
    //   207: invokevirtual getY : (I)F
    //   210: fstore #5
    //   212: aload_0
    //   213: fload #6
    //   215: fload #5
    //   217: iload_2
    //   218: invokespecial saveInitialMotion : (FFI)V
    //   221: aload_0
    //   222: getfield mDragState : I
    //   225: ifne -> 261
    //   228: aload_0
    //   229: getfield mInitialEdgesTouched : [I
    //   232: iload_2
    //   233: iaload
    //   234: istore_3
    //   235: aload_0
    //   236: getfield mTrackingEdges : I
    //   239: iload_3
    //   240: iand
    //   241: ifeq -> 84
    //   244: aload_0
    //   245: getfield mCallback : Landroid/support/v4/widget/ViewDragHelper$Callback;
    //   248: aload_0
    //   249: getfield mTrackingEdges : I
    //   252: iload_3
    //   253: iand
    //   254: iload_2
    //   255: invokevirtual onEdgeTouched : (II)V
    //   258: goto -> 84
    //   261: aload_0
    //   262: getfield mDragState : I
    //   265: iconst_2
    //   266: if_icmpne -> 84
    //   269: aload_0
    //   270: fload #6
    //   272: f2i
    //   273: fload #5
    //   275: f2i
    //   276: invokevirtual findTopChildUnder : (II)Landroid/view/View;
    //   279: astore_1
    //   280: aload_1
    //   281: aload_0
    //   282: getfield mCapturedView : Landroid/view/View;
    //   285: if_acmpne -> 84
    //   288: aload_0
    //   289: aload_1
    //   290: iload_2
    //   291: invokevirtual tryCaptureViewForDrag : (Landroid/view/View;I)Z
    //   294: pop
    //   295: goto -> 84
    //   298: aload_0
    //   299: getfield mInitialMotionX : [F
    //   302: ifnull -> 84
    //   305: aload_0
    //   306: getfield mInitialMotionY : [F
    //   309: ifnull -> 84
    //   312: aload_1
    //   313: invokevirtual getPointerCount : ()I
    //   316: istore #7
    //   318: iconst_0
    //   319: istore_2
    //   320: iload_2
    //   321: iload #7
    //   323: if_icmpge -> 540
    //   326: aload_1
    //   327: iload_2
    //   328: invokevirtual getPointerId : (I)I
    //   331: istore #8
    //   333: aload_0
    //   334: iload #8
    //   336: invokespecial isValidPointerForActionMove : (I)Z
    //   339: ifne -> 348
    //   342: iinc #2, 1
    //   345: goto -> 320
    //   348: aload_1
    //   349: iload_2
    //   350: invokevirtual getX : (I)F
    //   353: fstore #9
    //   355: aload_1
    //   356: iload_2
    //   357: invokevirtual getY : (I)F
    //   360: fstore #5
    //   362: fload #9
    //   364: aload_0
    //   365: getfield mInitialMotionX : [F
    //   368: iload #8
    //   370: faload
    //   371: fsub
    //   372: fstore #6
    //   374: fload #5
    //   376: aload_0
    //   377: getfield mInitialMotionY : [F
    //   380: iload #8
    //   382: faload
    //   383: fsub
    //   384: fstore #10
    //   386: aload_0
    //   387: fload #9
    //   389: f2i
    //   390: fload #5
    //   392: f2i
    //   393: invokevirtual findTopChildUnder : (II)Landroid/view/View;
    //   396: astore #11
    //   398: aload #11
    //   400: ifnull -> 548
    //   403: aload_0
    //   404: aload #11
    //   406: fload #6
    //   408: fload #10
    //   410: invokespecial checkTouchSlop : (Landroid/view/View;FF)Z
    //   413: ifeq -> 548
    //   416: iconst_1
    //   417: istore_3
    //   418: iload_3
    //   419: ifeq -> 553
    //   422: aload #11
    //   424: invokevirtual getLeft : ()I
    //   427: istore #12
    //   429: fload #6
    //   431: f2i
    //   432: istore #13
    //   434: aload_0
    //   435: getfield mCallback : Landroid/support/v4/widget/ViewDragHelper$Callback;
    //   438: aload #11
    //   440: iload #12
    //   442: iload #13
    //   444: iadd
    //   445: fload #6
    //   447: f2i
    //   448: invokevirtual clampViewPositionHorizontal : (Landroid/view/View;II)I
    //   451: istore #14
    //   453: aload #11
    //   455: invokevirtual getTop : ()I
    //   458: istore #13
    //   460: fload #10
    //   462: f2i
    //   463: istore #15
    //   465: aload_0
    //   466: getfield mCallback : Landroid/support/v4/widget/ViewDragHelper$Callback;
    //   469: aload #11
    //   471: iload #13
    //   473: iload #15
    //   475: iadd
    //   476: fload #10
    //   478: f2i
    //   479: invokevirtual clampViewPositionVertical : (Landroid/view/View;II)I
    //   482: istore #16
    //   484: aload_0
    //   485: getfield mCallback : Landroid/support/v4/widget/ViewDragHelper$Callback;
    //   488: aload #11
    //   490: invokevirtual getViewHorizontalDragRange : (Landroid/view/View;)I
    //   493: istore #15
    //   495: aload_0
    //   496: getfield mCallback : Landroid/support/v4/widget/ViewDragHelper$Callback;
    //   499: aload #11
    //   501: invokevirtual getViewVerticalDragRange : (Landroid/view/View;)I
    //   504: istore #17
    //   506: iload #15
    //   508: ifeq -> 523
    //   511: iload #15
    //   513: ifle -> 553
    //   516: iload #14
    //   518: iload #12
    //   520: if_icmpne -> 553
    //   523: iload #17
    //   525: ifeq -> 540
    //   528: iload #17
    //   530: ifle -> 553
    //   533: iload #16
    //   535: iload #13
    //   537: if_icmpne -> 553
    //   540: aload_0
    //   541: aload_1
    //   542: invokespecial saveLastMotion : (Landroid/view/MotionEvent;)V
    //   545: goto -> 84
    //   548: iconst_0
    //   549: istore_3
    //   550: goto -> 418
    //   553: aload_0
    //   554: fload #6
    //   556: fload #10
    //   558: iload #8
    //   560: invokespecial reportNewEdgeDrags : (FFI)V
    //   563: aload_0
    //   564: getfield mDragState : I
    //   567: iconst_1
    //   568: if_icmpeq -> 540
    //   571: iload_3
    //   572: ifeq -> 342
    //   575: aload_0
    //   576: aload #11
    //   578: iload #8
    //   580: invokevirtual tryCaptureViewForDrag : (Landroid/view/View;I)Z
    //   583: ifeq -> 342
    //   586: goto -> 540
    //   589: aload_0
    //   590: aload_1
    //   591: iload_3
    //   592: invokevirtual getPointerId : (I)I
    //   595: invokespecial clearMotionHistory : (I)V
    //   598: goto -> 84
    //   601: aload_0
    //   602: invokevirtual cancel : ()V
    //   605: goto -> 84
    //   608: iconst_0
    //   609: istore #4
    //   611: goto -> 95
  }
  
  public boolean smoothSlideViewTo(View paramView, int paramInt1, int paramInt2) {
    this.mCapturedView = paramView;
    this.mActivePointerId = -1;
    boolean bool = forceSettleCapturedViewAt(paramInt1, paramInt2, 0, 0);
    if (!bool && this.mDragState == 0 && this.mCapturedView != null)
      this.mCapturedView = null; 
    return bool;
  }
  
  boolean tryCaptureViewForDrag(View paramView, int paramInt) {
    boolean bool = true;
    if (paramView != this.mCapturedView || this.mActivePointerId != paramInt) {
      if (paramView != null && this.mCallback.tryCaptureView(paramView, paramInt)) {
        this.mActivePointerId = paramInt;
        captureChildView(paramView, paramInt);
        return bool;
      } 
      bool = false;
    } 
    return bool;
  }
  
  public static abstract class Callback {
    public int clampViewPositionHorizontal(View param1View, int param1Int1, int param1Int2) {
      return 0;
    }
    
    public int clampViewPositionVertical(View param1View, int param1Int1, int param1Int2) {
      return 0;
    }
    
    public int getOrderedChildIndex(int param1Int) {
      return param1Int;
    }
    
    public int getViewHorizontalDragRange(View param1View) {
      return 0;
    }
    
    public int getViewVerticalDragRange(View param1View) {
      return 0;
    }
    
    public void onEdgeDragStarted(int param1Int1, int param1Int2) {}
    
    public boolean onEdgeLock(int param1Int) {
      return false;
    }
    
    public void onEdgeTouched(int param1Int1, int param1Int2) {}
    
    public void onViewCaptured(View param1View, int param1Int) {}
    
    public void onViewDragStateChanged(int param1Int) {}
    
    public void onViewPositionChanged(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {}
    
    public void onViewReleased(View param1View, float param1Float1, float param1Float2) {}
    
    public abstract boolean tryCaptureView(View param1View, int param1Int);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/widget/ViewDragHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */