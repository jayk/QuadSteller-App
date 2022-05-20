package android.support.v7.widget.helper;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.animation.AnimatorCompatHelper;
import android.support.v4.animation.AnimatorListenerCompat;
import android.support.v4.animation.AnimatorUpdateListenerCompat;
import android.support.v4.animation.ValueAnimatorCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.recyclerview.R;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import java.util.ArrayList;
import java.util.List;

public class ItemTouchHelper extends RecyclerView.ItemDecoration implements RecyclerView.OnChildAttachStateChangeListener {
  static final int ACTION_MODE_DRAG_MASK = 16711680;
  
  private static final int ACTION_MODE_IDLE_MASK = 255;
  
  static final int ACTION_MODE_SWIPE_MASK = 65280;
  
  public static final int ACTION_STATE_DRAG = 2;
  
  public static final int ACTION_STATE_IDLE = 0;
  
  public static final int ACTION_STATE_SWIPE = 1;
  
  static final int ACTIVE_POINTER_ID_NONE = -1;
  
  public static final int ANIMATION_TYPE_DRAG = 8;
  
  public static final int ANIMATION_TYPE_SWIPE_CANCEL = 4;
  
  public static final int ANIMATION_TYPE_SWIPE_SUCCESS = 2;
  
  static final boolean DEBUG = false;
  
  static final int DIRECTION_FLAG_COUNT = 8;
  
  public static final int DOWN = 2;
  
  public static final int END = 32;
  
  public static final int LEFT = 4;
  
  private static final int PIXELS_PER_SECOND = 1000;
  
  public static final int RIGHT = 8;
  
  public static final int START = 16;
  
  static final String TAG = "ItemTouchHelper";
  
  public static final int UP = 1;
  
  int mActionState = 0;
  
  int mActivePointerId = -1;
  
  Callback mCallback;
  
  private RecyclerView.ChildDrawingOrderCallback mChildDrawingOrderCallback = null;
  
  private List<Integer> mDistances;
  
  private long mDragScrollStartTimeInMs;
  
  float mDx;
  
  float mDy;
  
  GestureDetectorCompat mGestureDetector;
  
  float mInitialTouchX;
  
  float mInitialTouchY;
  
  float mMaxSwipeVelocity;
  
  private final RecyclerView.OnItemTouchListener mOnItemTouchListener = new RecyclerView.OnItemTouchListener() {
      public boolean onInterceptTouchEvent(RecyclerView param1RecyclerView, MotionEvent param1MotionEvent) {
        boolean bool = true;
        ItemTouchHelper.this.mGestureDetector.onTouchEvent(param1MotionEvent);
        int i = MotionEventCompat.getActionMasked(param1MotionEvent);
        if (i == 0) {
          ItemTouchHelper.this.mActivePointerId = param1MotionEvent.getPointerId(0);
          ItemTouchHelper.this.mInitialTouchX = param1MotionEvent.getX();
          ItemTouchHelper.this.mInitialTouchY = param1MotionEvent.getY();
          ItemTouchHelper.this.obtainVelocityTracker();
          if (ItemTouchHelper.this.mSelected == null) {
            ItemTouchHelper.RecoverAnimation recoverAnimation = ItemTouchHelper.this.findAnimation(param1MotionEvent);
            if (recoverAnimation != null) {
              ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
              itemTouchHelper.mInitialTouchX -= recoverAnimation.mX;
              itemTouchHelper = ItemTouchHelper.this;
              itemTouchHelper.mInitialTouchY -= recoverAnimation.mY;
              ItemTouchHelper.this.endRecoverAnimation(recoverAnimation.mViewHolder, true);
              if (ItemTouchHelper.this.mPendingCleanup.remove(recoverAnimation.mViewHolder.itemView))
                ItemTouchHelper.this.mCallback.clearView(ItemTouchHelper.this.mRecyclerView, recoverAnimation.mViewHolder); 
              ItemTouchHelper.this.select(recoverAnimation.mViewHolder, recoverAnimation.mActionState);
              ItemTouchHelper.this.updateDxDy(param1MotionEvent, ItemTouchHelper.this.mSelectedFlags, 0);
            } 
          } 
        } else if (i == 3 || i == 1) {
          ItemTouchHelper.this.mActivePointerId = -1;
          ItemTouchHelper.this.select(null, 0);
        } else if (ItemTouchHelper.this.mActivePointerId != -1) {
          int j = param1MotionEvent.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
          if (j >= 0)
            ItemTouchHelper.this.checkSelectForSwipe(i, param1MotionEvent, j); 
        } 
        if (ItemTouchHelper.this.mVelocityTracker != null)
          ItemTouchHelper.this.mVelocityTracker.addMovement(param1MotionEvent); 
        if (ItemTouchHelper.this.mSelected == null)
          bool = false; 
        return bool;
      }
      
      public void onRequestDisallowInterceptTouchEvent(boolean param1Boolean) {
        if (param1Boolean)
          ItemTouchHelper.this.select(null, 0); 
      }
      
      public void onTouchEvent(RecyclerView param1RecyclerView, MotionEvent param1MotionEvent) {
        boolean bool = false;
        ItemTouchHelper.this.mGestureDetector.onTouchEvent(param1MotionEvent);
        if (ItemTouchHelper.this.mVelocityTracker != null)
          ItemTouchHelper.this.mVelocityTracker.addMovement(param1MotionEvent); 
        if (ItemTouchHelper.this.mActivePointerId == -1);
        int i = MotionEventCompat.getActionMasked(param1MotionEvent);
        int j = param1MotionEvent.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
        if (j >= 0)
          ItemTouchHelper.this.checkSelectForSwipe(i, param1MotionEvent, j); 
        RecyclerView.ViewHolder viewHolder = ItemTouchHelper.this.mSelected;
        if (viewHolder != null) {
          switch (i) {
            default:
              return;
            case 1:
              ItemTouchHelper.this.select(null, 0);
              ItemTouchHelper.this.mActivePointerId = -1;
            case 2:
              if (j >= 0) {
                ItemTouchHelper.this.updateDxDy(param1MotionEvent, ItemTouchHelper.this.mSelectedFlags, j);
                ItemTouchHelper.this.moveIfNecessary(viewHolder);
                ItemTouchHelper.this.mRecyclerView.removeCallbacks(ItemTouchHelper.this.mScrollRunnable);
                ItemTouchHelper.this.mScrollRunnable.run();
                ItemTouchHelper.this.mRecyclerView.invalidate();
              } 
            case 3:
              if (ItemTouchHelper.this.mVelocityTracker != null)
                ItemTouchHelper.this.mVelocityTracker.clear(); 
            case 6:
              break;
          } 
          j = MotionEventCompat.getActionIndex(param1MotionEvent);
          if (param1MotionEvent.getPointerId(j) == ItemTouchHelper.this.mActivePointerId) {
            if (j == 0)
              bool = true; 
            ItemTouchHelper.this.mActivePointerId = param1MotionEvent.getPointerId(bool);
            ItemTouchHelper.this.updateDxDy(param1MotionEvent, ItemTouchHelper.this.mSelectedFlags, j);
          } 
        } 
      }
    };
  
  View mOverdrawChild = null;
  
  int mOverdrawChildPosition = -1;
  
  final List<View> mPendingCleanup = new ArrayList<View>();
  
  List<RecoverAnimation> mRecoverAnimations = new ArrayList<RecoverAnimation>();
  
  RecyclerView mRecyclerView;
  
  final Runnable mScrollRunnable = new Runnable() {
      public void run() {
        if (ItemTouchHelper.this.mSelected != null && ItemTouchHelper.this.scrollIfNecessary()) {
          if (ItemTouchHelper.this.mSelected != null)
            ItemTouchHelper.this.moveIfNecessary(ItemTouchHelper.this.mSelected); 
          ItemTouchHelper.this.mRecyclerView.removeCallbacks(ItemTouchHelper.this.mScrollRunnable);
          ViewCompat.postOnAnimation((View)ItemTouchHelper.this.mRecyclerView, this);
        } 
      }
    };
  
  RecyclerView.ViewHolder mSelected = null;
  
  int mSelectedFlags;
  
  float mSelectedStartX;
  
  float mSelectedStartY;
  
  private int mSlop;
  
  private List<RecyclerView.ViewHolder> mSwapTargets;
  
  float mSwipeEscapeVelocity;
  
  private final float[] mTmpPosition = new float[2];
  
  private Rect mTmpRect;
  
  VelocityTracker mVelocityTracker;
  
  public ItemTouchHelper(Callback paramCallback) {
    this.mCallback = paramCallback;
  }
  
  private void addChildDrawingOrderCallback() {
    if (Build.VERSION.SDK_INT < 21) {
      if (this.mChildDrawingOrderCallback == null)
        this.mChildDrawingOrderCallback = new RecyclerView.ChildDrawingOrderCallback() {
            public int onGetChildDrawingOrder(int param1Int1, int param1Int2) {
              if (ItemTouchHelper.this.mOverdrawChild == null)
                return param1Int2; 
              int i = ItemTouchHelper.this.mOverdrawChildPosition;
              int j = i;
              if (i == -1) {
                j = ItemTouchHelper.this.mRecyclerView.indexOfChild(ItemTouchHelper.this.mOverdrawChild);
                ItemTouchHelper.this.mOverdrawChildPosition = j;
              } 
              if (param1Int2 == param1Int1 - 1)
                return j; 
              param1Int1 = param1Int2;
              if (param1Int2 >= j)
                param1Int1 = param1Int2 + 1; 
              return param1Int1;
            }
          }; 
      this.mRecyclerView.setChildDrawingOrderCallback(this.mChildDrawingOrderCallback);
    } 
  }
  
  private int checkHorizontalSwipe(RecyclerView.ViewHolder paramViewHolder, int paramInt) {
    if ((paramInt & 0xC) != 0) {
      byte b;
      if (this.mDx > 0.0F) {
        b = 8;
      } else {
        b = 4;
      } 
      if (this.mVelocityTracker != null && this.mActivePointerId > -1) {
        byte b1;
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mCallback.getSwipeVelocityThreshold(this.mMaxSwipeVelocity));
        float f3 = VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId);
        float f4 = VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId);
        if (f3 > 0.0F) {
          b1 = 8;
        } else {
          b1 = 4;
        } 
        f3 = Math.abs(f3);
        if ((b1 & paramInt) != 0 && b == b1 && f3 >= this.mCallback.getSwipeEscapeVelocity(this.mSwipeEscapeVelocity) && f3 > Math.abs(f4))
          return b1; 
      } 
      float f2 = this.mRecyclerView.getWidth();
      float f1 = this.mCallback.getSwipeThreshold(paramViewHolder);
      if ((paramInt & b) != 0 && Math.abs(this.mDx) > f2 * f1)
        return b; 
    } 
    return 0;
  }
  
  private int checkVerticalSwipe(RecyclerView.ViewHolder paramViewHolder, int paramInt) {
    if ((paramInt & 0x3) != 0) {
      byte b;
      if (this.mDy > 0.0F) {
        b = 2;
      } else {
        b = 1;
      } 
      if (this.mVelocityTracker != null && this.mActivePointerId > -1) {
        int i;
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mCallback.getSwipeVelocityThreshold(this.mMaxSwipeVelocity));
        float f3 = VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId);
        float f4 = VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId);
        if (f4 > 0.0F) {
          i = 2;
        } else {
          i = 1;
        } 
        f4 = Math.abs(f4);
        if ((i & paramInt) != 0 && i == b && f4 >= this.mCallback.getSwipeEscapeVelocity(this.mSwipeEscapeVelocity) && f4 > Math.abs(f3))
          return i; 
      } 
      float f1 = this.mRecyclerView.getHeight();
      float f2 = this.mCallback.getSwipeThreshold(paramViewHolder);
      if ((paramInt & b) != 0 && Math.abs(this.mDy) > f1 * f2)
        return b; 
    } 
    return 0;
  }
  
  private void destroyCallbacks() {
    this.mRecyclerView.removeItemDecoration(this);
    this.mRecyclerView.removeOnItemTouchListener(this.mOnItemTouchListener);
    this.mRecyclerView.removeOnChildAttachStateChangeListener(this);
    for (int i = this.mRecoverAnimations.size() - 1; i >= 0; i--) {
      RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(0);
      this.mCallback.clearView(this.mRecyclerView, recoverAnimation.mViewHolder);
    } 
    this.mRecoverAnimations.clear();
    this.mOverdrawChild = null;
    this.mOverdrawChildPosition = -1;
    releaseVelocityTracker();
  }
  
  private List<RecyclerView.ViewHolder> findSwapTargets(RecyclerView.ViewHolder paramViewHolder) {
    if (this.mSwapTargets == null) {
      this.mSwapTargets = new ArrayList<RecyclerView.ViewHolder>();
      this.mDistances = new ArrayList<Integer>();
    } else {
      this.mSwapTargets.clear();
      this.mDistances.clear();
    } 
    int i = this.mCallback.getBoundingBoxMargin();
    int j = Math.round(this.mSelectedStartX + this.mDx) - i;
    int k = Math.round(this.mSelectedStartY + this.mDy) - i;
    int m = paramViewHolder.itemView.getWidth() + j + i * 2;
    int n = paramViewHolder.itemView.getHeight() + k + i * 2;
    int i1 = (j + m) / 2;
    int i2 = (k + n) / 2;
    RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
    int i3 = layoutManager.getChildCount();
    for (i = 0; i < i3; i++) {
      View view = layoutManager.getChildAt(i);
      if (view != paramViewHolder.itemView && view.getBottom() >= k && view.getTop() <= n && view.getRight() >= j && view.getLeft() <= m) {
        RecyclerView.ViewHolder viewHolder = this.mRecyclerView.getChildViewHolder(view);
        if (this.mCallback.canDropOver(this.mRecyclerView, this.mSelected, viewHolder)) {
          int i4 = Math.abs(i1 - (view.getLeft() + view.getRight()) / 2);
          int i5 = Math.abs(i2 - (view.getTop() + view.getBottom()) / 2);
          int i6 = i4 * i4 + i5 * i5;
          i5 = 0;
          int i7 = this.mSwapTargets.size();
          for (i4 = 0; i4 < i7 && i6 > ((Integer)this.mDistances.get(i4)).intValue(); i4++)
            i5++; 
          this.mSwapTargets.add(i5, viewHolder);
          this.mDistances.add(i5, Integer.valueOf(i6));
        } 
      } 
    } 
    return this.mSwapTargets;
  }
  
  private RecyclerView.ViewHolder findSwipedView(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aload_0
    //   3: getfield mRecyclerView : Landroid/support/v7/widget/RecyclerView;
    //   6: invokevirtual getLayoutManager : ()Landroid/support/v7/widget/RecyclerView$LayoutManager;
    //   9: astore_3
    //   10: aload_0
    //   11: getfield mActivePointerId : I
    //   14: iconst_m1
    //   15: if_icmpne -> 24
    //   18: aload_2
    //   19: astore #4
    //   21: aload #4
    //   23: areturn
    //   24: aload_1
    //   25: aload_0
    //   26: getfield mActivePointerId : I
    //   29: invokevirtual findPointerIndex : (I)I
    //   32: istore #5
    //   34: aload_1
    //   35: iload #5
    //   37: invokevirtual getX : (I)F
    //   40: fstore #6
    //   42: aload_0
    //   43: getfield mInitialTouchX : F
    //   46: fstore #7
    //   48: aload_1
    //   49: iload #5
    //   51: invokevirtual getY : (I)F
    //   54: fstore #8
    //   56: aload_0
    //   57: getfield mInitialTouchY : F
    //   60: fstore #9
    //   62: fload #6
    //   64: fload #7
    //   66: fsub
    //   67: invokestatic abs : (F)F
    //   70: fstore #6
    //   72: fload #8
    //   74: fload #9
    //   76: fsub
    //   77: invokestatic abs : (F)F
    //   80: fstore #9
    //   82: fload #6
    //   84: aload_0
    //   85: getfield mSlop : I
    //   88: i2f
    //   89: fcmpg
    //   90: ifge -> 107
    //   93: aload_2
    //   94: astore #4
    //   96: fload #9
    //   98: aload_0
    //   99: getfield mSlop : I
    //   102: i2f
    //   103: fcmpg
    //   104: iflt -> 21
    //   107: fload #6
    //   109: fload #9
    //   111: fcmpl
    //   112: ifle -> 125
    //   115: aload_2
    //   116: astore #4
    //   118: aload_3
    //   119: invokevirtual canScrollHorizontally : ()Z
    //   122: ifne -> 21
    //   125: fload #9
    //   127: fload #6
    //   129: fcmpl
    //   130: ifle -> 143
    //   133: aload_2
    //   134: astore #4
    //   136: aload_3
    //   137: invokevirtual canScrollVertically : ()Z
    //   140: ifne -> 21
    //   143: aload_0
    //   144: aload_1
    //   145: invokevirtual findChildView : (Landroid/view/MotionEvent;)Landroid/view/View;
    //   148: astore_1
    //   149: aload_2
    //   150: astore #4
    //   152: aload_1
    //   153: ifnull -> 21
    //   156: aload_0
    //   157: getfield mRecyclerView : Landroid/support/v7/widget/RecyclerView;
    //   160: aload_1
    //   161: invokevirtual getChildViewHolder : (Landroid/view/View;)Landroid/support/v7/widget/RecyclerView$ViewHolder;
    //   164: astore #4
    //   166: goto -> 21
  }
  
  private void getSelectedDxDy(float[] paramArrayOffloat) {
    if ((this.mSelectedFlags & 0xC) != 0) {
      paramArrayOffloat[0] = this.mSelectedStartX + this.mDx - this.mSelected.itemView.getLeft();
    } else {
      paramArrayOffloat[0] = ViewCompat.getTranslationX(this.mSelected.itemView);
    } 
    if ((this.mSelectedFlags & 0x3) != 0) {
      paramArrayOffloat[1] = this.mSelectedStartY + this.mDy - this.mSelected.itemView.getTop();
      return;
    } 
    paramArrayOffloat[1] = ViewCompat.getTranslationY(this.mSelected.itemView);
  }
  
  private static boolean hitTest(View paramView, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    return (paramFloat1 >= paramFloat3 && paramFloat1 <= paramView.getWidth() + paramFloat3 && paramFloat2 >= paramFloat4 && paramFloat2 <= paramView.getHeight() + paramFloat4);
  }
  
  private void initGestureDetector() {
    if (this.mGestureDetector == null)
      this.mGestureDetector = new GestureDetectorCompat(this.mRecyclerView.getContext(), (GestureDetector.OnGestureListener)new ItemTouchHelperGestureListener()); 
  }
  
  private void releaseVelocityTracker() {
    if (this.mVelocityTracker != null) {
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
    } 
  }
  
  private void setupCallbacks() {
    this.mSlop = ViewConfiguration.get(this.mRecyclerView.getContext()).getScaledTouchSlop();
    this.mRecyclerView.addItemDecoration(this);
    this.mRecyclerView.addOnItemTouchListener(this.mOnItemTouchListener);
    this.mRecyclerView.addOnChildAttachStateChangeListener(this);
    initGestureDetector();
  }
  
  private int swipeIfNecessary(RecyclerView.ViewHolder paramViewHolder) {
    if (this.mActionState == 2)
      return 0; 
    null = this.mCallback.getMovementFlags(this.mRecyclerView, paramViewHolder);
    int i = (this.mCallback.convertToAbsoluteDirection(null, ViewCompat.getLayoutDirection((View)this.mRecyclerView)) & 0xFF00) >> 8;
    if (i == 0)
      return 0; 
    int j = (null & 0xFF00) >> 8;
    if (Math.abs(this.mDx) > Math.abs(this.mDy)) {
      int m = checkHorizontalSwipe(paramViewHolder, i);
      if (m > 0) {
        null = m;
        if ((j & m) == 0)
          null = Callback.convertToRelativeDirection(m, ViewCompat.getLayoutDirection((View)this.mRecyclerView)); 
        return null;
      } 
      m = checkVerticalSwipe(paramViewHolder, i);
      null = m;
      return (m <= 0) ? 0 : null;
    } 
    int k = checkVerticalSwipe(paramViewHolder, i);
    null = k;
    if (k <= 0) {
      k = checkHorizontalSwipe(paramViewHolder, i);
      if (k > 0) {
        null = k;
        if ((j & k) == 0)
          null = Callback.convertToRelativeDirection(k, ViewCompat.getLayoutDirection((View)this.mRecyclerView)); 
        return null;
      } 
    } else {
      return null;
    } 
    return 0;
  }
  
  public void attachToRecyclerView(@Nullable RecyclerView paramRecyclerView) {
    if (this.mRecyclerView != paramRecyclerView) {
      if (this.mRecyclerView != null)
        destroyCallbacks(); 
      this.mRecyclerView = paramRecyclerView;
      if (this.mRecyclerView != null) {
        Resources resources = paramRecyclerView.getResources();
        this.mSwipeEscapeVelocity = resources.getDimension(R.dimen.item_touch_helper_swipe_escape_velocity);
        this.mMaxSwipeVelocity = resources.getDimension(R.dimen.item_touch_helper_swipe_escape_max_velocity);
        setupCallbacks();
      } 
    } 
  }
  
  boolean checkSelectForSwipe(int paramInt1, MotionEvent paramMotionEvent, int paramInt2) {
    if (this.mSelected != null || paramInt1 != 2 || this.mActionState == 2 || !this.mCallback.isItemViewSwipeEnabled())
      return false; 
    if (this.mRecyclerView.getScrollState() == 1)
      return false; 
    RecyclerView.ViewHolder viewHolder = findSwipedView(paramMotionEvent);
    if (viewHolder == null)
      return false; 
    paramInt1 = (0xFF00 & this.mCallback.getAbsoluteMovementFlags(this.mRecyclerView, viewHolder)) >> 8;
    if (paramInt1 == 0)
      return false; 
    float f1 = paramMotionEvent.getX(paramInt2);
    float f2 = paramMotionEvent.getY(paramInt2);
    f1 -= this.mInitialTouchX;
    float f3 = f2 - this.mInitialTouchY;
    f2 = Math.abs(f1);
    float f4 = Math.abs(f3);
    if (f2 < this.mSlop && f4 < this.mSlop)
      return false; 
    if (f2 > f4) {
      if (f1 < 0.0F && (paramInt1 & 0x4) == 0)
        return false; 
      if (f1 > 0.0F && (paramInt1 & 0x8) == 0)
        return false; 
    } else {
      if (f3 < 0.0F && (paramInt1 & 0x1) == 0)
        return false; 
      if (f3 > 0.0F && (paramInt1 & 0x2) == 0)
        return false; 
    } 
    this.mDy = 0.0F;
    this.mDx = 0.0F;
    this.mActivePointerId = paramMotionEvent.getPointerId(0);
    select(viewHolder, 1);
    return true;
  }
  
  int endRecoverAnimation(RecyclerView.ViewHolder paramViewHolder, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mRecoverAnimations : Ljava/util/List;
    //   4: invokeinterface size : ()I
    //   9: iconst_1
    //   10: isub
    //   11: istore_3
    //   12: iload_3
    //   13: iflt -> 90
    //   16: aload_0
    //   17: getfield mRecoverAnimations : Ljava/util/List;
    //   20: iload_3
    //   21: invokeinterface get : (I)Ljava/lang/Object;
    //   26: checkcast android/support/v7/widget/helper/ItemTouchHelper$RecoverAnimation
    //   29: astore #4
    //   31: aload #4
    //   33: getfield mViewHolder : Landroid/support/v7/widget/RecyclerView$ViewHolder;
    //   36: aload_1
    //   37: if_acmpne -> 84
    //   40: aload #4
    //   42: aload #4
    //   44: getfield mOverridden : Z
    //   47: iload_2
    //   48: ior
    //   49: putfield mOverridden : Z
    //   52: aload #4
    //   54: getfield mEnded : Z
    //   57: ifne -> 65
    //   60: aload #4
    //   62: invokevirtual cancel : ()V
    //   65: aload_0
    //   66: getfield mRecoverAnimations : Ljava/util/List;
    //   69: iload_3
    //   70: invokeinterface remove : (I)Ljava/lang/Object;
    //   75: pop
    //   76: aload #4
    //   78: getfield mAnimationType : I
    //   81: istore_3
    //   82: iload_3
    //   83: ireturn
    //   84: iinc #3, -1
    //   87: goto -> 12
    //   90: iconst_0
    //   91: istore_3
    //   92: goto -> 82
  }
  
  RecoverAnimation findAnimation(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mRecoverAnimations : Ljava/util/List;
    //   4: invokeinterface isEmpty : ()Z
    //   9: ifeq -> 16
    //   12: aconst_null
    //   13: astore_1
    //   14: aload_1
    //   15: areturn
    //   16: aload_0
    //   17: aload_1
    //   18: invokevirtual findChildView : (Landroid/view/MotionEvent;)Landroid/view/View;
    //   21: astore_2
    //   22: aload_0
    //   23: getfield mRecoverAnimations : Ljava/util/List;
    //   26: invokeinterface size : ()I
    //   31: iconst_1
    //   32: isub
    //   33: istore_3
    //   34: iload_3
    //   35: iflt -> 74
    //   38: aload_0
    //   39: getfield mRecoverAnimations : Ljava/util/List;
    //   42: iload_3
    //   43: invokeinterface get : (I)Ljava/lang/Object;
    //   48: checkcast android/support/v7/widget/helper/ItemTouchHelper$RecoverAnimation
    //   51: astore #4
    //   53: aload #4
    //   55: astore_1
    //   56: aload #4
    //   58: getfield mViewHolder : Landroid/support/v7/widget/RecyclerView$ViewHolder;
    //   61: getfield itemView : Landroid/view/View;
    //   64: aload_2
    //   65: if_acmpeq -> 14
    //   68: iinc #3, -1
    //   71: goto -> 34
    //   74: aconst_null
    //   75: astore_1
    //   76: goto -> 14
  }
  
  View findChildView(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getX : ()F
    //   4: fstore_2
    //   5: aload_1
    //   6: invokevirtual getY : ()F
    //   9: fstore_3
    //   10: aload_0
    //   11: getfield mSelected : Landroid/support/v7/widget/RecyclerView$ViewHolder;
    //   14: ifnull -> 54
    //   17: aload_0
    //   18: getfield mSelected : Landroid/support/v7/widget/RecyclerView$ViewHolder;
    //   21: getfield itemView : Landroid/view/View;
    //   24: astore_1
    //   25: aload_1
    //   26: fload_2
    //   27: fload_3
    //   28: aload_0
    //   29: getfield mSelectedStartX : F
    //   32: aload_0
    //   33: getfield mDx : F
    //   36: fadd
    //   37: aload_0
    //   38: getfield mSelectedStartY : F
    //   41: aload_0
    //   42: getfield mDy : F
    //   45: fadd
    //   46: invokestatic hitTest : (Landroid/view/View;FFFF)Z
    //   49: ifeq -> 54
    //   52: aload_1
    //   53: areturn
    //   54: aload_0
    //   55: getfield mRecoverAnimations : Ljava/util/List;
    //   58: invokeinterface size : ()I
    //   63: iconst_1
    //   64: isub
    //   65: istore #4
    //   67: iload #4
    //   69: iflt -> 125
    //   72: aload_0
    //   73: getfield mRecoverAnimations : Ljava/util/List;
    //   76: iload #4
    //   78: invokeinterface get : (I)Ljava/lang/Object;
    //   83: checkcast android/support/v7/widget/helper/ItemTouchHelper$RecoverAnimation
    //   86: astore #5
    //   88: aload #5
    //   90: getfield mViewHolder : Landroid/support/v7/widget/RecyclerView$ViewHolder;
    //   93: getfield itemView : Landroid/view/View;
    //   96: astore_1
    //   97: aload_1
    //   98: fload_2
    //   99: fload_3
    //   100: aload #5
    //   102: getfield mX : F
    //   105: aload #5
    //   107: getfield mY : F
    //   110: invokestatic hitTest : (Landroid/view/View;FFFF)Z
    //   113: ifeq -> 119
    //   116: goto -> 52
    //   119: iinc #4, -1
    //   122: goto -> 67
    //   125: aload_0
    //   126: getfield mRecyclerView : Landroid/support/v7/widget/RecyclerView;
    //   129: fload_2
    //   130: fload_3
    //   131: invokevirtual findChildViewUnder : (FF)Landroid/view/View;
    //   134: astore_1
    //   135: goto -> 52
  }
  
  public void getItemOffsets(Rect paramRect, View paramView, RecyclerView paramRecyclerView, RecyclerView.State paramState) {
    paramRect.setEmpty();
  }
  
  boolean hasRunningRecoverAnim() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mRecoverAnimations : Ljava/util/List;
    //   4: invokeinterface size : ()I
    //   9: istore_1
    //   10: iconst_0
    //   11: istore_2
    //   12: iload_2
    //   13: iload_1
    //   14: if_icmpge -> 46
    //   17: aload_0
    //   18: getfield mRecoverAnimations : Ljava/util/List;
    //   21: iload_2
    //   22: invokeinterface get : (I)Ljava/lang/Object;
    //   27: checkcast android/support/v7/widget/helper/ItemTouchHelper$RecoverAnimation
    //   30: getfield mEnded : Z
    //   33: ifne -> 40
    //   36: iconst_1
    //   37: istore_3
    //   38: iload_3
    //   39: ireturn
    //   40: iinc #2, 1
    //   43: goto -> 12
    //   46: iconst_0
    //   47: istore_3
    //   48: goto -> 38
  }
  
  void moveIfNecessary(RecyclerView.ViewHolder paramViewHolder) {
    if (!this.mRecyclerView.isLayoutRequested() && this.mActionState == 2) {
      float f = this.mCallback.getMoveThreshold(paramViewHolder);
      int i = (int)(this.mSelectedStartX + this.mDx);
      int j = (int)(this.mSelectedStartY + this.mDy);
      if (Math.abs(j - paramViewHolder.itemView.getTop()) >= paramViewHolder.itemView.getHeight() * f || Math.abs(i - paramViewHolder.itemView.getLeft()) >= paramViewHolder.itemView.getWidth() * f) {
        List<RecyclerView.ViewHolder> list = findSwapTargets(paramViewHolder);
        if (list.size() != 0) {
          RecyclerView.ViewHolder viewHolder = this.mCallback.chooseDropTarget(paramViewHolder, list, i, j);
          if (viewHolder == null) {
            this.mSwapTargets.clear();
            this.mDistances.clear();
            return;
          } 
          int k = viewHolder.getAdapterPosition();
          int m = paramViewHolder.getAdapterPosition();
          if (this.mCallback.onMove(this.mRecyclerView, paramViewHolder, viewHolder))
            this.mCallback.onMoved(this.mRecyclerView, paramViewHolder, m, viewHolder, k, i, j); 
        } 
      } 
    } 
  }
  
  void obtainVelocityTracker() {
    if (this.mVelocityTracker != null)
      this.mVelocityTracker.recycle(); 
    this.mVelocityTracker = VelocityTracker.obtain();
  }
  
  public void onChildViewAttachedToWindow(View paramView) {}
  
  public void onChildViewDetachedFromWindow(View paramView) {
    removeChildDrawingOrderCallbackIfNecessary(paramView);
    RecyclerView.ViewHolder viewHolder = this.mRecyclerView.getChildViewHolder(paramView);
    if (viewHolder != null) {
      if (this.mSelected != null && viewHolder == this.mSelected) {
        select(null, 0);
        return;
      } 
      endRecoverAnimation(viewHolder, false);
      if (this.mPendingCleanup.remove(viewHolder.itemView))
        this.mCallback.clearView(this.mRecyclerView, viewHolder); 
    } 
  }
  
  public void onDraw(Canvas paramCanvas, RecyclerView paramRecyclerView, RecyclerView.State paramState) {
    this.mOverdrawChildPosition = -1;
    float f1 = 0.0F;
    float f2 = 0.0F;
    if (this.mSelected != null) {
      getSelectedDxDy(this.mTmpPosition);
      f1 = this.mTmpPosition[0];
      f2 = this.mTmpPosition[1];
    } 
    this.mCallback.onDraw(paramCanvas, paramRecyclerView, this.mSelected, this.mRecoverAnimations, this.mActionState, f1, f2);
  }
  
  public void onDrawOver(Canvas paramCanvas, RecyclerView paramRecyclerView, RecyclerView.State paramState) {
    float f1 = 0.0F;
    float f2 = 0.0F;
    if (this.mSelected != null) {
      getSelectedDxDy(this.mTmpPosition);
      f1 = this.mTmpPosition[0];
      f2 = this.mTmpPosition[1];
    } 
    this.mCallback.onDrawOver(paramCanvas, paramRecyclerView, this.mSelected, this.mRecoverAnimations, this.mActionState, f1, f2);
  }
  
  void postDispatchSwipe(final RecoverAnimation anim, final int swipeDir) {
    this.mRecyclerView.post(new Runnable() {
          public void run() {
            if (ItemTouchHelper.this.mRecyclerView != null && ItemTouchHelper.this.mRecyclerView.isAttachedToWindow() && !anim.mOverridden && anim.mViewHolder.getAdapterPosition() != -1) {
              RecyclerView.ItemAnimator itemAnimator = ItemTouchHelper.this.mRecyclerView.getItemAnimator();
              if ((itemAnimator == null || !itemAnimator.isRunning(null)) && !ItemTouchHelper.this.hasRunningRecoverAnim()) {
                ItemTouchHelper.this.mCallback.onSwiped(anim.mViewHolder, swipeDir);
                return;
              } 
            } else {
              return;
            } 
            ItemTouchHelper.this.mRecyclerView.post(this);
          }
        });
  }
  
  void removeChildDrawingOrderCallbackIfNecessary(View paramView) {
    if (paramView == this.mOverdrawChild) {
      this.mOverdrawChild = null;
      if (this.mChildDrawingOrderCallback != null)
        this.mRecyclerView.setChildDrawingOrderCallback(null); 
    } 
  }
  
  boolean scrollIfNecessary() {
    long l2;
    if (this.mSelected == null) {
      this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
      return false;
    } 
    long l1 = System.currentTimeMillis();
    if (this.mDragScrollStartTimeInMs == Long.MIN_VALUE) {
      l2 = 0L;
    } else {
      l2 = l1 - this.mDragScrollStartTimeInMs;
    } 
    RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
    if (this.mTmpRect == null)
      this.mTmpRect = new Rect(); 
    int i = 0;
    int j = 0;
    layoutManager.calculateItemDecorationsForChild(this.mSelected.itemView, this.mTmpRect);
    int k = i;
    if (layoutManager.canScrollHorizontally()) {
      int m = (int)(this.mSelectedStartX + this.mDx);
      k = m - this.mTmpRect.left - this.mRecyclerView.getPaddingLeft();
      if (this.mDx >= 0.0F || k >= 0) {
        k = i;
        if (this.mDx > 0.0F) {
          m = this.mSelected.itemView.getWidth() + m + this.mTmpRect.right - this.mRecyclerView.getWidth() - this.mRecyclerView.getPaddingRight();
          k = i;
          if (m > 0)
            k = m; 
        } 
      } 
    } 
    i = j;
    if (layoutManager.canScrollVertically()) {
      int m = (int)(this.mSelectedStartY + this.mDy);
      i = m - this.mTmpRect.top - this.mRecyclerView.getPaddingTop();
      if (this.mDy >= 0.0F || i >= 0) {
        i = j;
        if (this.mDy > 0.0F) {
          m = this.mSelected.itemView.getHeight() + m + this.mTmpRect.bottom - this.mRecyclerView.getHeight() - this.mRecyclerView.getPaddingBottom();
          i = j;
          if (m > 0)
            i = m; 
        } 
      } 
    } 
    j = k;
    if (k != 0)
      j = this.mCallback.interpolateOutOfBoundsScroll(this.mRecyclerView, this.mSelected.itemView.getWidth(), k, this.mRecyclerView.getWidth(), l2); 
    k = i;
    if (i != 0)
      k = this.mCallback.interpolateOutOfBoundsScroll(this.mRecyclerView, this.mSelected.itemView.getHeight(), i, this.mRecyclerView.getHeight(), l2); 
    if (j != 0 || k != 0) {
      if (this.mDragScrollStartTimeInMs == Long.MIN_VALUE)
        this.mDragScrollStartTimeInMs = l1; 
      this.mRecyclerView.scrollBy(j, k);
      return true;
    } 
    this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
    return false;
  }
  
  void select(RecyclerView.ViewHolder paramViewHolder, int paramInt) {
    // Byte code:
    //   0: aload_1
    //   1: aload_0
    //   2: getfield mSelected : Landroid/support/v7/widget/RecyclerView$ViewHolder;
    //   5: if_acmpne -> 17
    //   8: iload_2
    //   9: aload_0
    //   10: getfield mActionState : I
    //   13: if_icmpne -> 17
    //   16: return
    //   17: aload_0
    //   18: ldc2_w -9223372036854775808
    //   21: putfield mDragScrollStartTimeInMs : J
    //   24: aload_0
    //   25: getfield mActionState : I
    //   28: istore_3
    //   29: aload_0
    //   30: aload_1
    //   31: iconst_1
    //   32: invokevirtual endRecoverAnimation : (Landroid/support/v7/widget/RecyclerView$ViewHolder;Z)I
    //   35: pop
    //   36: aload_0
    //   37: iload_2
    //   38: putfield mActionState : I
    //   41: iload_2
    //   42: iconst_2
    //   43: if_icmpne -> 58
    //   46: aload_0
    //   47: aload_1
    //   48: getfield itemView : Landroid/view/View;
    //   51: putfield mOverdrawChild : Landroid/view/View;
    //   54: aload_0
    //   55: invokespecial addChildDrawingOrderCallback : ()V
    //   58: iconst_0
    //   59: istore #4
    //   61: iconst_0
    //   62: istore #5
    //   64: aload_0
    //   65: getfield mSelected : Landroid/support/v7/widget/RecyclerView$ViewHolder;
    //   68: ifnull -> 279
    //   71: aload_0
    //   72: getfield mSelected : Landroid/support/v7/widget/RecyclerView$ViewHolder;
    //   75: astore #6
    //   77: aload #6
    //   79: getfield itemView : Landroid/view/View;
    //   82: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   85: ifnull -> 511
    //   88: iload_3
    //   89: iconst_2
    //   90: if_icmpne -> 435
    //   93: iconst_0
    //   94: istore #5
    //   96: aload_0
    //   97: invokespecial releaseVelocityTracker : ()V
    //   100: iload #5
    //   102: lookupswitch default -> 160, 1 -> 470, 2 -> 470, 4 -> 446, 8 -> 446, 16 -> 446, 32 -> 446
    //   160: fconst_0
    //   161: fstore #7
    //   163: fconst_0
    //   164: fstore #8
    //   166: iload_3
    //   167: iconst_2
    //   168: if_icmpne -> 494
    //   171: bipush #8
    //   173: istore #4
    //   175: aload_0
    //   176: aload_0
    //   177: getfield mTmpPosition : [F
    //   180: invokespecial getSelectedDxDy : ([F)V
    //   183: aload_0
    //   184: getfield mTmpPosition : [F
    //   187: iconst_0
    //   188: faload
    //   189: fstore #9
    //   191: aload_0
    //   192: getfield mTmpPosition : [F
    //   195: iconst_1
    //   196: faload
    //   197: fstore #10
    //   199: new android/support/v7/widget/helper/ItemTouchHelper$3
    //   202: dup
    //   203: aload_0
    //   204: aload #6
    //   206: iload #4
    //   208: iload_3
    //   209: fload #9
    //   211: fload #10
    //   213: fload #7
    //   215: fload #8
    //   217: iload #5
    //   219: aload #6
    //   221: invokespecial <init> : (Landroid/support/v7/widget/helper/ItemTouchHelper;Landroid/support/v7/widget/RecyclerView$ViewHolder;IIFFFFILandroid/support/v7/widget/RecyclerView$ViewHolder;)V
    //   224: astore #6
    //   226: aload #6
    //   228: aload_0
    //   229: getfield mCallback : Landroid/support/v7/widget/helper/ItemTouchHelper$Callback;
    //   232: aload_0
    //   233: getfield mRecyclerView : Landroid/support/v7/widget/RecyclerView;
    //   236: iload #4
    //   238: fload #7
    //   240: fload #9
    //   242: fsub
    //   243: fload #8
    //   245: fload #10
    //   247: fsub
    //   248: invokevirtual getAnimationDuration : (Landroid/support/v7/widget/RecyclerView;IFF)J
    //   251: invokevirtual setDuration : (J)V
    //   254: aload_0
    //   255: getfield mRecoverAnimations : Ljava/util/List;
    //   258: aload #6
    //   260: invokeinterface add : (Ljava/lang/Object;)Z
    //   265: pop
    //   266: aload #6
    //   268: invokevirtual start : ()V
    //   271: iconst_1
    //   272: istore #4
    //   274: aload_0
    //   275: aconst_null
    //   276: putfield mSelected : Landroid/support/v7/widget/RecyclerView$ViewHolder;
    //   279: aload_1
    //   280: ifnull -> 365
    //   283: aload_0
    //   284: aload_0
    //   285: getfield mCallback : Landroid/support/v7/widget/helper/ItemTouchHelper$Callback;
    //   288: aload_0
    //   289: getfield mRecyclerView : Landroid/support/v7/widget/RecyclerView;
    //   292: aload_1
    //   293: invokevirtual getAbsoluteMovementFlags : (Landroid/support/v7/widget/RecyclerView;Landroid/support/v7/widget/RecyclerView$ViewHolder;)I
    //   296: iconst_1
    //   297: iload_2
    //   298: bipush #8
    //   300: imul
    //   301: bipush #8
    //   303: iadd
    //   304: ishl
    //   305: iconst_1
    //   306: isub
    //   307: iand
    //   308: aload_0
    //   309: getfield mActionState : I
    //   312: bipush #8
    //   314: imul
    //   315: ishr
    //   316: putfield mSelectedFlags : I
    //   319: aload_0
    //   320: aload_1
    //   321: getfield itemView : Landroid/view/View;
    //   324: invokevirtual getLeft : ()I
    //   327: i2f
    //   328: putfield mSelectedStartX : F
    //   331: aload_0
    //   332: aload_1
    //   333: getfield itemView : Landroid/view/View;
    //   336: invokevirtual getTop : ()I
    //   339: i2f
    //   340: putfield mSelectedStartY : F
    //   343: aload_0
    //   344: aload_1
    //   345: putfield mSelected : Landroid/support/v7/widget/RecyclerView$ViewHolder;
    //   348: iload_2
    //   349: iconst_2
    //   350: if_icmpne -> 365
    //   353: aload_0
    //   354: getfield mSelected : Landroid/support/v7/widget/RecyclerView$ViewHolder;
    //   357: getfield itemView : Landroid/view/View;
    //   360: iconst_0
    //   361: invokevirtual performHapticFeedback : (I)Z
    //   364: pop
    //   365: aload_0
    //   366: getfield mRecyclerView : Landroid/support/v7/widget/RecyclerView;
    //   369: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   372: astore_1
    //   373: aload_1
    //   374: ifnull -> 395
    //   377: aload_0
    //   378: getfield mSelected : Landroid/support/v7/widget/RecyclerView$ViewHolder;
    //   381: ifnull -> 540
    //   384: iconst_1
    //   385: istore #11
    //   387: aload_1
    //   388: iload #11
    //   390: invokeinterface requestDisallowInterceptTouchEvent : (Z)V
    //   395: iload #4
    //   397: ifne -> 410
    //   400: aload_0
    //   401: getfield mRecyclerView : Landroid/support/v7/widget/RecyclerView;
    //   404: invokevirtual getLayoutManager : ()Landroid/support/v7/widget/RecyclerView$LayoutManager;
    //   407: invokevirtual requestSimpleAnimationsInNextLayout : ()V
    //   410: aload_0
    //   411: getfield mCallback : Landroid/support/v7/widget/helper/ItemTouchHelper$Callback;
    //   414: aload_0
    //   415: getfield mSelected : Landroid/support/v7/widget/RecyclerView$ViewHolder;
    //   418: aload_0
    //   419: getfield mActionState : I
    //   422: invokevirtual onSelectedChanged : (Landroid/support/v7/widget/RecyclerView$ViewHolder;I)V
    //   425: aload_0
    //   426: getfield mRecyclerView : Landroid/support/v7/widget/RecyclerView;
    //   429: invokevirtual invalidate : ()V
    //   432: goto -> 16
    //   435: aload_0
    //   436: aload #6
    //   438: invokespecial swipeIfNecessary : (Landroid/support/v7/widget/RecyclerView$ViewHolder;)I
    //   441: istore #5
    //   443: goto -> 96
    //   446: fconst_0
    //   447: fstore #8
    //   449: aload_0
    //   450: getfield mDx : F
    //   453: invokestatic signum : (F)F
    //   456: aload_0
    //   457: getfield mRecyclerView : Landroid/support/v7/widget/RecyclerView;
    //   460: invokevirtual getWidth : ()I
    //   463: i2f
    //   464: fmul
    //   465: fstore #7
    //   467: goto -> 166
    //   470: fconst_0
    //   471: fstore #7
    //   473: aload_0
    //   474: getfield mDy : F
    //   477: invokestatic signum : (F)F
    //   480: aload_0
    //   481: getfield mRecyclerView : Landroid/support/v7/widget/RecyclerView;
    //   484: invokevirtual getHeight : ()I
    //   487: i2f
    //   488: fmul
    //   489: fstore #8
    //   491: goto -> 166
    //   494: iload #5
    //   496: ifle -> 505
    //   499: iconst_2
    //   500: istore #4
    //   502: goto -> 175
    //   505: iconst_4
    //   506: istore #4
    //   508: goto -> 175
    //   511: aload_0
    //   512: aload #6
    //   514: getfield itemView : Landroid/view/View;
    //   517: invokevirtual removeChildDrawingOrderCallbackIfNecessary : (Landroid/view/View;)V
    //   520: aload_0
    //   521: getfield mCallback : Landroid/support/v7/widget/helper/ItemTouchHelper$Callback;
    //   524: aload_0
    //   525: getfield mRecyclerView : Landroid/support/v7/widget/RecyclerView;
    //   528: aload #6
    //   530: invokevirtual clearView : (Landroid/support/v7/widget/RecyclerView;Landroid/support/v7/widget/RecyclerView$ViewHolder;)V
    //   533: iload #5
    //   535: istore #4
    //   537: goto -> 274
    //   540: iconst_0
    //   541: istore #11
    //   543: goto -> 387
  }
  
  public void startDrag(RecyclerView.ViewHolder paramViewHolder) {
    if (!this.mCallback.hasDragFlag(this.mRecyclerView, paramViewHolder)) {
      Log.e("ItemTouchHelper", "Start drag has been called but swiping is not enabled");
      return;
    } 
    if (paramViewHolder.itemView.getParent() != this.mRecyclerView) {
      Log.e("ItemTouchHelper", "Start drag has been called with a view holder which is not a child of the RecyclerView which is controlled by this ItemTouchHelper.");
      return;
    } 
    obtainVelocityTracker();
    this.mDy = 0.0F;
    this.mDx = 0.0F;
    select(paramViewHolder, 2);
  }
  
  public void startSwipe(RecyclerView.ViewHolder paramViewHolder) {
    if (!this.mCallback.hasSwipeFlag(this.mRecyclerView, paramViewHolder)) {
      Log.e("ItemTouchHelper", "Start swipe has been called but dragging is not enabled");
      return;
    } 
    if (paramViewHolder.itemView.getParent() != this.mRecyclerView) {
      Log.e("ItemTouchHelper", "Start swipe has been called with a view holder which is not a child of the RecyclerView controlled by this ItemTouchHelper.");
      return;
    } 
    obtainVelocityTracker();
    this.mDy = 0.0F;
    this.mDx = 0.0F;
    select(paramViewHolder, 1);
  }
  
  void updateDxDy(MotionEvent paramMotionEvent, int paramInt1, int paramInt2) {
    float f1 = paramMotionEvent.getX(paramInt2);
    float f2 = paramMotionEvent.getY(paramInt2);
    this.mDx = f1 - this.mInitialTouchX;
    this.mDy = f2 - this.mInitialTouchY;
    if ((paramInt1 & 0x4) == 0)
      this.mDx = Math.max(0.0F, this.mDx); 
    if ((paramInt1 & 0x8) == 0)
      this.mDx = Math.min(0.0F, this.mDx); 
    if ((paramInt1 & 0x1) == 0)
      this.mDy = Math.max(0.0F, this.mDy); 
    if ((paramInt1 & 0x2) == 0)
      this.mDy = Math.min(0.0F, this.mDy); 
  }
  
  public static abstract class Callback {
    private static final int ABS_HORIZONTAL_DIR_FLAGS = 789516;
    
    public static final int DEFAULT_DRAG_ANIMATION_DURATION = 200;
    
    public static final int DEFAULT_SWIPE_ANIMATION_DURATION = 250;
    
    private static final long DRAG_SCROLL_ACCELERATION_LIMIT_TIME_MS = 2000L;
    
    static final int RELATIVE_DIR_FLAGS = 3158064;
    
    private static final Interpolator sDragScrollInterpolator = new Interpolator() {
        public float getInterpolation(float param2Float) {
          return param2Float * param2Float * param2Float * param2Float * param2Float;
        }
      };
    
    private static final Interpolator sDragViewScrollCapInterpolator = new Interpolator() {
        public float getInterpolation(float param2Float) {
          param2Float--;
          return param2Float * param2Float * param2Float * param2Float * param2Float + 1.0F;
        }
      };
    
    private static final ItemTouchUIUtil sUICallback = new ItemTouchUIUtilImpl.Gingerbread();
    
    private int mCachedMaxScrollSpeed = -1;
    
    public static int convertToRelativeDirection(int param1Int1, int param1Int2) {
      int i = param1Int1 & 0xC0C0C;
      if (i != 0) {
        param1Int1 &= i ^ 0xFFFFFFFF;
        if (param1Int2 == 0) {
          param1Int1 |= i << 2;
          return param1Int1;
        } 
        param1Int1 = param1Int1 | i << 1 & 0xFFF3F3F3 | (i << 1 & 0xC0C0C) << 2;
      } 
      return param1Int1;
    }
    
    public static ItemTouchUIUtil getDefaultUIUtil() {
      return sUICallback;
    }
    
    private int getMaxDragScroll(RecyclerView param1RecyclerView) {
      if (this.mCachedMaxScrollSpeed == -1)
        this.mCachedMaxScrollSpeed = param1RecyclerView.getResources().getDimensionPixelSize(R.dimen.item_touch_helper_max_drag_scroll_per_frame); 
      return this.mCachedMaxScrollSpeed;
    }
    
    public static int makeFlag(int param1Int1, int param1Int2) {
      return param1Int2 << param1Int1 * 8;
    }
    
    public static int makeMovementFlags(int param1Int1, int param1Int2) {
      return makeFlag(0, param1Int2 | param1Int1) | makeFlag(1, param1Int2) | makeFlag(2, param1Int1);
    }
    
    public boolean canDropOver(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder1, RecyclerView.ViewHolder param1ViewHolder2) {
      return true;
    }
    
    public RecyclerView.ViewHolder chooseDropTarget(RecyclerView.ViewHolder param1ViewHolder, List<RecyclerView.ViewHolder> param1List, int param1Int1, int param1Int2) {
      int i = param1ViewHolder.itemView.getWidth();
      int j = param1ViewHolder.itemView.getHeight();
      RecyclerView.ViewHolder viewHolder = null;
      int k = -1;
      int m = param1Int1 - param1ViewHolder.itemView.getLeft();
      int n = param1Int2 - param1ViewHolder.itemView.getTop();
      int i1 = param1List.size();
      for (byte b = 0; b < i1; b++) {
        RecyclerView.ViewHolder viewHolder1 = param1List.get(b);
        RecyclerView.ViewHolder viewHolder2 = viewHolder;
        int i2 = k;
        if (m > 0) {
          int i3 = viewHolder1.itemView.getRight() - param1Int1 + i;
          viewHolder2 = viewHolder;
          i2 = k;
          if (i3 < 0) {
            viewHolder2 = viewHolder;
            i2 = k;
            if (viewHolder1.itemView.getRight() > param1ViewHolder.itemView.getRight()) {
              i3 = Math.abs(i3);
              viewHolder2 = viewHolder;
              i2 = k;
              if (i3 > k) {
                i2 = i3;
                viewHolder2 = viewHolder1;
              } 
            } 
          } 
        } 
        viewHolder = viewHolder2;
        k = i2;
        if (m < 0) {
          int i3 = viewHolder1.itemView.getLeft() - param1Int1;
          viewHolder = viewHolder2;
          k = i2;
          if (i3 > 0) {
            viewHolder = viewHolder2;
            k = i2;
            if (viewHolder1.itemView.getLeft() < param1ViewHolder.itemView.getLeft()) {
              i3 = Math.abs(i3);
              viewHolder = viewHolder2;
              k = i2;
              if (i3 > i2) {
                k = i3;
                viewHolder = viewHolder1;
              } 
            } 
          } 
        } 
        viewHolder2 = viewHolder;
        i2 = k;
        if (n < 0) {
          int i3 = viewHolder1.itemView.getTop() - param1Int2;
          viewHolder2 = viewHolder;
          i2 = k;
          if (i3 > 0) {
            viewHolder2 = viewHolder;
            i2 = k;
            if (viewHolder1.itemView.getTop() < param1ViewHolder.itemView.getTop()) {
              i3 = Math.abs(i3);
              viewHolder2 = viewHolder;
              i2 = k;
              if (i3 > k) {
                i2 = i3;
                viewHolder2 = viewHolder1;
              } 
            } 
          } 
        } 
        viewHolder = viewHolder2;
        k = i2;
        if (n > 0) {
          int i3 = viewHolder1.itemView.getBottom() - param1Int2 + j;
          viewHolder = viewHolder2;
          k = i2;
          if (i3 < 0) {
            viewHolder = viewHolder2;
            k = i2;
            if (viewHolder1.itemView.getBottom() > param1ViewHolder.itemView.getBottom()) {
              i3 = Math.abs(i3);
              viewHolder = viewHolder2;
              k = i2;
              if (i3 > i2) {
                k = i3;
                viewHolder = viewHolder1;
              } 
            } 
          } 
        } 
      } 
      return viewHolder;
    }
    
    public void clearView(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder) {
      sUICallback.clearView(param1ViewHolder.itemView);
    }
    
    public int convertToAbsoluteDirection(int param1Int1, int param1Int2) {
      int i = param1Int1 & 0x303030;
      if (i != 0) {
        param1Int1 &= i ^ 0xFFFFFFFF;
        if (param1Int2 == 0) {
          param1Int1 |= i >> 2;
          return param1Int1;
        } 
        param1Int1 = param1Int1 | i >> 1 & 0xFFCFCFCF | (i >> 1 & 0x303030) >> 2;
      } 
      return param1Int1;
    }
    
    final int getAbsoluteMovementFlags(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder) {
      return convertToAbsoluteDirection(getMovementFlags(param1RecyclerView, param1ViewHolder), ViewCompat.getLayoutDirection((View)param1RecyclerView));
    }
    
    public long getAnimationDuration(RecyclerView param1RecyclerView, int param1Int, float param1Float1, float param1Float2) {
      RecyclerView.ItemAnimator itemAnimator = param1RecyclerView.getItemAnimator();
      return (itemAnimator == null) ? ((param1Int == 8) ? 200L : 250L) : ((param1Int == 8) ? itemAnimator.getMoveDuration() : itemAnimator.getRemoveDuration());
    }
    
    public int getBoundingBoxMargin() {
      return 0;
    }
    
    public float getMoveThreshold(RecyclerView.ViewHolder param1ViewHolder) {
      return 0.5F;
    }
    
    public abstract int getMovementFlags(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder);
    
    public float getSwipeEscapeVelocity(float param1Float) {
      return param1Float;
    }
    
    public float getSwipeThreshold(RecyclerView.ViewHolder param1ViewHolder) {
      return 0.5F;
    }
    
    public float getSwipeVelocityThreshold(float param1Float) {
      return param1Float;
    }
    
    boolean hasDragFlag(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder) {
      return ((0xFF0000 & getAbsoluteMovementFlags(param1RecyclerView, param1ViewHolder)) != 0);
    }
    
    boolean hasSwipeFlag(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder) {
      return ((0xFF00 & getAbsoluteMovementFlags(param1RecyclerView, param1ViewHolder)) != 0);
    }
    
    public int interpolateOutOfBoundsScroll(RecyclerView param1RecyclerView, int param1Int1, int param1Int2, int param1Int3, long param1Long) {
      int i = getMaxDragScroll(param1RecyclerView);
      int j = Math.abs(param1Int2);
      param1Int3 = (int)Math.signum(param1Int2);
      float f = Math.min(1.0F, 1.0F * j / param1Int1);
      param1Int1 = (int)((param1Int3 * i) * sDragViewScrollCapInterpolator.getInterpolation(f));
      if (param1Long > 2000L) {
        f = 1.0F;
      } else {
        f = (float)param1Long / 2000.0F;
      } 
      param1Int1 = (int)(param1Int1 * sDragScrollInterpolator.getInterpolation(f));
      if (param1Int1 == 0) {
        if (param1Int2 > 0)
          return 1; 
      } else {
        return param1Int1;
      } 
      return -1;
    }
    
    public boolean isItemViewSwipeEnabled() {
      return true;
    }
    
    public boolean isLongPressDragEnabled() {
      return true;
    }
    
    public void onChildDraw(Canvas param1Canvas, RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder, float param1Float1, float param1Float2, int param1Int, boolean param1Boolean) {
      sUICallback.onDraw(param1Canvas, param1RecyclerView, param1ViewHolder.itemView, param1Float1, param1Float2, param1Int, param1Boolean);
    }
    
    public void onChildDrawOver(Canvas param1Canvas, RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder, float param1Float1, float param1Float2, int param1Int, boolean param1Boolean) {
      sUICallback.onDrawOver(param1Canvas, param1RecyclerView, param1ViewHolder.itemView, param1Float1, param1Float2, param1Int, param1Boolean);
    }
    
    void onDraw(Canvas param1Canvas, RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder, List<ItemTouchHelper.RecoverAnimation> param1List, int param1Int, float param1Float1, float param1Float2) {
      int i = param1List.size();
      int j;
      for (j = 0; j < i; j++) {
        ItemTouchHelper.RecoverAnimation recoverAnimation = param1List.get(j);
        recoverAnimation.update();
        int k = param1Canvas.save();
        onChildDraw(param1Canvas, param1RecyclerView, recoverAnimation.mViewHolder, recoverAnimation.mX, recoverAnimation.mY, recoverAnimation.mActionState, false);
        param1Canvas.restoreToCount(k);
      } 
      if (param1ViewHolder != null) {
        j = param1Canvas.save();
        onChildDraw(param1Canvas, param1RecyclerView, param1ViewHolder, param1Float1, param1Float2, param1Int, true);
        param1Canvas.restoreToCount(j);
      } 
    }
    
    void onDrawOver(Canvas param1Canvas, RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder, List<ItemTouchHelper.RecoverAnimation> param1List, int param1Int, float param1Float1, float param1Float2) {
      int i = param1List.size();
      int j;
      for (j = 0; j < i; j++) {
        ItemTouchHelper.RecoverAnimation recoverAnimation = param1List.get(j);
        int k = param1Canvas.save();
        onChildDrawOver(param1Canvas, param1RecyclerView, recoverAnimation.mViewHolder, recoverAnimation.mX, recoverAnimation.mY, recoverAnimation.mActionState, false);
        param1Canvas.restoreToCount(k);
      } 
      if (param1ViewHolder != null) {
        j = param1Canvas.save();
        onChildDrawOver(param1Canvas, param1RecyclerView, param1ViewHolder, param1Float1, param1Float2, param1Int, true);
        param1Canvas.restoreToCount(j);
      } 
      j = 0;
      for (param1Int = i - 1; param1Int >= 0; param1Int--) {
        ItemTouchHelper.RecoverAnimation recoverAnimation = param1List.get(param1Int);
        if (recoverAnimation.mEnded && !recoverAnimation.mIsPendingCleanup) {
          param1List.remove(param1Int);
        } else if (!recoverAnimation.mEnded) {
          j = 1;
        } 
      } 
      if (j != 0)
        param1RecyclerView.invalidate(); 
    }
    
    public abstract boolean onMove(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder1, RecyclerView.ViewHolder param1ViewHolder2);
    
    public void onMoved(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder1, int param1Int1, RecyclerView.ViewHolder param1ViewHolder2, int param1Int2, int param1Int3, int param1Int4) {
      RecyclerView.LayoutManager layoutManager = param1RecyclerView.getLayoutManager();
      if (layoutManager instanceof ItemTouchHelper.ViewDropHandler) {
        ((ItemTouchHelper.ViewDropHandler)layoutManager).prepareForDrop(param1ViewHolder1.itemView, param1ViewHolder2.itemView, param1Int3, param1Int4);
        return;
      } 
      if (layoutManager.canScrollHorizontally()) {
        if (layoutManager.getDecoratedLeft(param1ViewHolder2.itemView) <= param1RecyclerView.getPaddingLeft())
          param1RecyclerView.scrollToPosition(param1Int2); 
        if (layoutManager.getDecoratedRight(param1ViewHolder2.itemView) >= param1RecyclerView.getWidth() - param1RecyclerView.getPaddingRight())
          param1RecyclerView.scrollToPosition(param1Int2); 
      } 
      if (layoutManager.canScrollVertically()) {
        if (layoutManager.getDecoratedTop(param1ViewHolder2.itemView) <= param1RecyclerView.getPaddingTop())
          param1RecyclerView.scrollToPosition(param1Int2); 
        if (layoutManager.getDecoratedBottom(param1ViewHolder2.itemView) >= param1RecyclerView.getHeight() - param1RecyclerView.getPaddingBottom())
          param1RecyclerView.scrollToPosition(param1Int2); 
      } 
    }
    
    public void onSelectedChanged(RecyclerView.ViewHolder param1ViewHolder, int param1Int) {
      if (param1ViewHolder != null)
        sUICallback.onSelected(param1ViewHolder.itemView); 
    }
    
    public abstract void onSwiped(RecyclerView.ViewHolder param1ViewHolder, int param1Int);
    
    static {
      if (Build.VERSION.SDK_INT >= 21) {
        sUICallback = new ItemTouchUIUtilImpl.Lollipop();
        return;
      } 
      if (Build.VERSION.SDK_INT >= 11) {
        sUICallback = new ItemTouchUIUtilImpl.Honeycomb();
        return;
      } 
    }
  }
  
  static final class null implements Interpolator {
    public float getInterpolation(float param1Float) {
      return param1Float * param1Float * param1Float * param1Float * param1Float;
    }
  }
  
  static final class null implements Interpolator {
    public float getInterpolation(float param1Float) {
      param1Float--;
      return param1Float * param1Float * param1Float * param1Float * param1Float + 1.0F;
    }
  }
  
  private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
    public boolean onDown(MotionEvent param1MotionEvent) {
      return true;
    }
    
    public void onLongPress(MotionEvent param1MotionEvent) {
      View view = ItemTouchHelper.this.findChildView(param1MotionEvent);
      if (view != null) {
        RecyclerView.ViewHolder viewHolder = ItemTouchHelper.this.mRecyclerView.getChildViewHolder(view);
        if (viewHolder != null && ItemTouchHelper.this.mCallback.hasDragFlag(ItemTouchHelper.this.mRecyclerView, viewHolder) && param1MotionEvent.getPointerId(0) == ItemTouchHelper.this.mActivePointerId) {
          int i = param1MotionEvent.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
          float f1 = param1MotionEvent.getX(i);
          float f2 = param1MotionEvent.getY(i);
          ItemTouchHelper.this.mInitialTouchX = f1;
          ItemTouchHelper.this.mInitialTouchY = f2;
          ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
          ItemTouchHelper.this.mDy = 0.0F;
          itemTouchHelper.mDx = 0.0F;
          if (ItemTouchHelper.this.mCallback.isLongPressDragEnabled())
            ItemTouchHelper.this.select(viewHolder, 2); 
        } 
      } 
    }
  }
  
  private class RecoverAnimation implements AnimatorListenerCompat {
    final int mActionState;
    
    final int mAnimationType;
    
    boolean mEnded = false;
    
    private float mFraction;
    
    public boolean mIsPendingCleanup;
    
    boolean mOverridden = false;
    
    final float mStartDx;
    
    final float mStartDy;
    
    final float mTargetX;
    
    final float mTargetY;
    
    private final ValueAnimatorCompat mValueAnimator;
    
    final RecyclerView.ViewHolder mViewHolder;
    
    float mX;
    
    float mY;
    
    public RecoverAnimation(RecyclerView.ViewHolder param1ViewHolder, int param1Int1, int param1Int2, float param1Float1, float param1Float2, float param1Float3, float param1Float4) {
      this.mActionState = param1Int2;
      this.mAnimationType = param1Int1;
      this.mViewHolder = param1ViewHolder;
      this.mStartDx = param1Float1;
      this.mStartDy = param1Float2;
      this.mTargetX = param1Float3;
      this.mTargetY = param1Float4;
      this.mValueAnimator = AnimatorCompatHelper.emptyValueAnimator();
      this.mValueAnimator.addUpdateListener(new AnimatorUpdateListenerCompat() {
            public void onAnimationUpdate(ValueAnimatorCompat param2ValueAnimatorCompat) {
              ItemTouchHelper.RecoverAnimation.this.setFraction(param2ValueAnimatorCompat.getAnimatedFraction());
            }
          });
      this.mValueAnimator.setTarget(param1ViewHolder.itemView);
      this.mValueAnimator.addListener(this);
      setFraction(0.0F);
    }
    
    public void cancel() {
      this.mValueAnimator.cancel();
    }
    
    public void onAnimationCancel(ValueAnimatorCompat param1ValueAnimatorCompat) {
      setFraction(1.0F);
    }
    
    public void onAnimationEnd(ValueAnimatorCompat param1ValueAnimatorCompat) {
      if (!this.mEnded)
        this.mViewHolder.setIsRecyclable(true); 
      this.mEnded = true;
    }
    
    public void onAnimationRepeat(ValueAnimatorCompat param1ValueAnimatorCompat) {}
    
    public void onAnimationStart(ValueAnimatorCompat param1ValueAnimatorCompat) {}
    
    public void setDuration(long param1Long) {
      this.mValueAnimator.setDuration(param1Long);
    }
    
    public void setFraction(float param1Float) {
      this.mFraction = param1Float;
    }
    
    public void start() {
      this.mViewHolder.setIsRecyclable(false);
      this.mValueAnimator.start();
    }
    
    public void update() {
      if (this.mStartDx == this.mTargetX) {
        this.mX = ViewCompat.getTranslationX(this.mViewHolder.itemView);
      } else {
        this.mX = this.mStartDx + this.mFraction * (this.mTargetX - this.mStartDx);
      } 
      if (this.mStartDy == this.mTargetY) {
        this.mY = ViewCompat.getTranslationY(this.mViewHolder.itemView);
        return;
      } 
      this.mY = this.mStartDy + this.mFraction * (this.mTargetY - this.mStartDy);
    }
  }
  
  class null implements AnimatorUpdateListenerCompat {
    public void onAnimationUpdate(ValueAnimatorCompat param1ValueAnimatorCompat) {
      this.this$1.setFraction(param1ValueAnimatorCompat.getAnimatedFraction());
    }
  }
  
  public static abstract class SimpleCallback extends Callback {
    private int mDefaultDragDirs;
    
    private int mDefaultSwipeDirs;
    
    public SimpleCallback(int param1Int1, int param1Int2) {
      this.mDefaultSwipeDirs = param1Int2;
      this.mDefaultDragDirs = param1Int1;
    }
    
    public int getDragDirs(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder) {
      return this.mDefaultDragDirs;
    }
    
    public int getMovementFlags(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder) {
      return makeMovementFlags(getDragDirs(param1RecyclerView, param1ViewHolder), getSwipeDirs(param1RecyclerView, param1ViewHolder));
    }
    
    public int getSwipeDirs(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder) {
      return this.mDefaultSwipeDirs;
    }
    
    public void setDefaultDragDirs(int param1Int) {
      this.mDefaultDragDirs = param1Int;
    }
    
    public void setDefaultSwipeDirs(int param1Int) {
      this.mDefaultSwipeDirs = param1Int;
    }
  }
  
  public static interface ViewDropHandler {
    void prepareForDrop(View param1View1, View param1View2, int param1Int1, int param1Int2);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/helper/ItemTouchHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */