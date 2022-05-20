package android.support.v7.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public abstract class SnapHelper extends RecyclerView.OnFlingListener {
  static final float MILLISECONDS_PER_INCH = 100.0F;
  
  private Scroller mGravityScroller;
  
  RecyclerView mRecyclerView;
  
  private final RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
      boolean mScrolled = false;
      
      public void onScrollStateChanged(RecyclerView param1RecyclerView, int param1Int) {
        super.onScrollStateChanged(param1RecyclerView, param1Int);
        if (param1Int == 0 && this.mScrolled) {
          this.mScrolled = false;
          SnapHelper.this.snapToTargetExistingView();
        } 
      }
      
      public void onScrolled(RecyclerView param1RecyclerView, int param1Int1, int param1Int2) {
        if (param1Int1 != 0 || param1Int2 != 0)
          this.mScrolled = true; 
      }
    };
  
  private void destroyCallbacks() {
    this.mRecyclerView.removeOnScrollListener(this.mScrollListener);
    this.mRecyclerView.setOnFlingListener((RecyclerView.OnFlingListener)null);
  }
  
  private void setupCallbacks() throws IllegalStateException {
    if (this.mRecyclerView.getOnFlingListener() != null)
      throw new IllegalStateException("An instance of OnFlingListener already set."); 
    this.mRecyclerView.addOnScrollListener(this.mScrollListener);
    this.mRecyclerView.setOnFlingListener(this);
  }
  
  private boolean snapFromFling(@NonNull RecyclerView.LayoutManager paramLayoutManager, int paramInt1, int paramInt2) {
    boolean bool1 = false;
    if (!(paramLayoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider))
      return bool1; 
    LinearSmoothScroller linearSmoothScroller = createSnapScroller(paramLayoutManager);
    boolean bool2 = bool1;
    if (linearSmoothScroller != null) {
      paramInt1 = findTargetSnapPosition(paramLayoutManager, paramInt1, paramInt2);
      bool2 = bool1;
      if (paramInt1 != -1) {
        linearSmoothScroller.setTargetPosition(paramInt1);
        paramLayoutManager.startSmoothScroll(linearSmoothScroller);
        bool2 = true;
      } 
    } 
    return bool2;
  }
  
  public void attachToRecyclerView(@Nullable RecyclerView paramRecyclerView) throws IllegalStateException {
    if (this.mRecyclerView != paramRecyclerView) {
      if (this.mRecyclerView != null)
        destroyCallbacks(); 
      this.mRecyclerView = paramRecyclerView;
      if (this.mRecyclerView != null) {
        setupCallbacks();
        this.mGravityScroller = new Scroller(this.mRecyclerView.getContext(), (Interpolator)new DecelerateInterpolator());
        snapToTargetExistingView();
      } 
    } 
  }
  
  @Nullable
  public abstract int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager paramLayoutManager, @NonNull View paramView);
  
  public int[] calculateScrollDistance(int paramInt1, int paramInt2) {
    this.mGravityScroller.fling(0, 0, paramInt1, paramInt2, -2147483648, 2147483647, -2147483648, 2147483647);
    return new int[] { this.mGravityScroller.getFinalX(), this.mGravityScroller.getFinalY() };
  }
  
  @Nullable
  protected LinearSmoothScroller createSnapScroller(RecyclerView.LayoutManager paramLayoutManager) {
    return !(paramLayoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) ? null : new LinearSmoothScroller(this.mRecyclerView.getContext()) {
        protected float calculateSpeedPerPixel(DisplayMetrics param1DisplayMetrics) {
          return 100.0F / param1DisplayMetrics.densityDpi;
        }
        
        protected void onTargetFound(View param1View, RecyclerView.State param1State, RecyclerView.SmoothScroller.Action param1Action) {
          int[] arrayOfInt = SnapHelper.this.calculateDistanceToFinalSnap(SnapHelper.this.mRecyclerView.getLayoutManager(), param1View);
          int i = arrayOfInt[0];
          int j = arrayOfInt[1];
          int k = calculateTimeForDeceleration(Math.max(Math.abs(i), Math.abs(j)));
          if (k > 0)
            param1Action.update(i, j, k, (Interpolator)this.mDecelerateInterpolator); 
        }
      };
  }
  
  @Nullable
  public abstract View findSnapView(RecyclerView.LayoutManager paramLayoutManager);
  
  public abstract int findTargetSnapPosition(RecyclerView.LayoutManager paramLayoutManager, int paramInt1, int paramInt2);
  
  public boolean onFling(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: aload_0
    //   3: getfield mRecyclerView : Landroid/support/v7/widget/RecyclerView;
    //   6: invokevirtual getLayoutManager : ()Landroid/support/v7/widget/RecyclerView$LayoutManager;
    //   9: astore #4
    //   11: aload #4
    //   13: ifnonnull -> 22
    //   16: iload_3
    //   17: istore #5
    //   19: iload #5
    //   21: ireturn
    //   22: iload_3
    //   23: istore #5
    //   25: aload_0
    //   26: getfield mRecyclerView : Landroid/support/v7/widget/RecyclerView;
    //   29: invokevirtual getAdapter : ()Landroid/support/v7/widget/RecyclerView$Adapter;
    //   32: ifnull -> 19
    //   35: aload_0
    //   36: getfield mRecyclerView : Landroid/support/v7/widget/RecyclerView;
    //   39: invokevirtual getMinFlingVelocity : ()I
    //   42: istore #6
    //   44: iload_2
    //   45: invokestatic abs : (I)I
    //   48: iload #6
    //   50: if_icmpgt -> 65
    //   53: iload_3
    //   54: istore #5
    //   56: iload_1
    //   57: invokestatic abs : (I)I
    //   60: iload #6
    //   62: if_icmple -> 19
    //   65: iload_3
    //   66: istore #5
    //   68: aload_0
    //   69: aload #4
    //   71: iload_1
    //   72: iload_2
    //   73: invokespecial snapFromFling : (Landroid/support/v7/widget/RecyclerView$LayoutManager;II)Z
    //   76: ifeq -> 19
    //   79: iconst_1
    //   80: istore #5
    //   82: goto -> 19
  }
  
  void snapToTargetExistingView() {
    if (this.mRecyclerView != null) {
      RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
      if (layoutManager != null) {
        View view = findSnapView(layoutManager);
        if (view != null) {
          int[] arrayOfInt = calculateDistanceToFinalSnap(layoutManager, view);
          if (arrayOfInt[0] != 0 || arrayOfInt[1] != 0)
            this.mRecyclerView.smoothScrollBy(arrayOfInt[0], arrayOfInt[1]); 
        } 
      } 
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/SnapHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */