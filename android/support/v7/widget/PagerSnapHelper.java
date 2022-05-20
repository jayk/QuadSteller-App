package android.support.v7.widget;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Interpolator;

public class PagerSnapHelper extends SnapHelper {
  private static final int MAX_SCROLL_ON_FLING_DURATION = 100;
  
  @Nullable
  private OrientationHelper mHorizontalHelper;
  
  @Nullable
  private OrientationHelper mVerticalHelper;
  
  private int distanceToCenter(@NonNull RecyclerView.LayoutManager paramLayoutManager, @NonNull View paramView, OrientationHelper paramOrientationHelper) {
    int i = paramOrientationHelper.getDecoratedStart(paramView);
    int j = paramOrientationHelper.getDecoratedMeasurement(paramView) / 2;
    if (paramLayoutManager.getClipToPadding()) {
      int m = paramOrientationHelper.getStartAfterPadding() + paramOrientationHelper.getTotalSpace() / 2;
      return i + j - m;
    } 
    int k = paramOrientationHelper.getEnd() / 2;
    return i + j - k;
  }
  
  @Nullable
  private View findCenterView(RecyclerView.LayoutManager paramLayoutManager, OrientationHelper paramOrientationHelper) {
    int j;
    int i = paramLayoutManager.getChildCount();
    if (i == 0)
      return null; 
    View view = null;
    if (paramLayoutManager.getClipToPadding()) {
      j = paramOrientationHelper.getStartAfterPadding() + paramOrientationHelper.getTotalSpace() / 2;
    } else {
      j = paramOrientationHelper.getEnd() / 2;
    } 
    int k = Integer.MAX_VALUE;
    byte b = 0;
    while (true) {
      View view1 = view;
      if (b < i) {
        view1 = paramLayoutManager.getChildAt(b);
        int m = Math.abs(paramOrientationHelper.getDecoratedStart(view1) + paramOrientationHelper.getDecoratedMeasurement(view1) / 2 - j);
        int n = k;
        if (m < k) {
          n = m;
          view = view1;
        } 
        b++;
        k = n;
        continue;
      } 
      return view1;
    } 
  }
  
  @Nullable
  private View findStartView(RecyclerView.LayoutManager paramLayoutManager, OrientationHelper paramOrientationHelper) {
    int i = paramLayoutManager.getChildCount();
    if (i == 0)
      return null; 
    View view = null;
    int j = Integer.MAX_VALUE;
    byte b = 0;
    while (true) {
      View view1 = view;
      if (b < i) {
        view1 = paramLayoutManager.getChildAt(b);
        int k = paramOrientationHelper.getDecoratedStart(view1);
        int m = j;
        if (k < j) {
          m = k;
          view = view1;
        } 
        b++;
        j = m;
        continue;
      } 
      return view1;
    } 
  }
  
  @NonNull
  private OrientationHelper getHorizontalHelper(@NonNull RecyclerView.LayoutManager paramLayoutManager) {
    if (this.mHorizontalHelper == null || this.mHorizontalHelper.mLayoutManager != paramLayoutManager)
      this.mHorizontalHelper = OrientationHelper.createHorizontalHelper(paramLayoutManager); 
    return this.mHorizontalHelper;
  }
  
  @NonNull
  private OrientationHelper getVerticalHelper(@NonNull RecyclerView.LayoutManager paramLayoutManager) {
    if (this.mVerticalHelper == null || this.mVerticalHelper.mLayoutManager != paramLayoutManager)
      this.mVerticalHelper = OrientationHelper.createVerticalHelper(paramLayoutManager); 
    return this.mVerticalHelper;
  }
  
  @Nullable
  public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager paramLayoutManager, @NonNull View paramView) {
    int[] arrayOfInt = new int[2];
    if (paramLayoutManager.canScrollHorizontally()) {
      arrayOfInt[0] = distanceToCenter(paramLayoutManager, paramView, getHorizontalHelper(paramLayoutManager));
    } else {
      arrayOfInt[0] = 0;
    } 
    if (paramLayoutManager.canScrollVertically()) {
      arrayOfInt[1] = distanceToCenter(paramLayoutManager, paramView, getVerticalHelper(paramLayoutManager));
      return arrayOfInt;
    } 
    arrayOfInt[1] = 0;
    return arrayOfInt;
  }
  
  protected LinearSmoothScroller createSnapScroller(RecyclerView.LayoutManager paramLayoutManager) {
    return !(paramLayoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) ? null : new LinearSmoothScroller(this.mRecyclerView.getContext()) {
        protected float calculateSpeedPerPixel(DisplayMetrics param1DisplayMetrics) {
          return 100.0F / param1DisplayMetrics.densityDpi;
        }
        
        protected int calculateTimeForScrolling(int param1Int) {
          return Math.min(100, super.calculateTimeForScrolling(param1Int));
        }
        
        protected void onTargetFound(View param1View, RecyclerView.State param1State, RecyclerView.SmoothScroller.Action param1Action) {
          int[] arrayOfInt = PagerSnapHelper.this.calculateDistanceToFinalSnap(PagerSnapHelper.this.mRecyclerView.getLayoutManager(), param1View);
          int i = arrayOfInt[0];
          int j = arrayOfInt[1];
          int k = calculateTimeForDeceleration(Math.max(Math.abs(i), Math.abs(j)));
          if (k > 0)
            param1Action.update(i, j, k, (Interpolator)this.mDecelerateInterpolator); 
        }
      };
  }
  
  @Nullable
  public View findSnapView(RecyclerView.LayoutManager paramLayoutManager) {
    return paramLayoutManager.canScrollVertically() ? findCenterView(paramLayoutManager, getVerticalHelper(paramLayoutManager)) : (paramLayoutManager.canScrollHorizontally() ? findCenterView(paramLayoutManager, getHorizontalHelper(paramLayoutManager)) : null);
  }
  
  public int findTargetSnapPosition(RecyclerView.LayoutManager paramLayoutManager, int paramInt1, int paramInt2) {
    int i = paramLayoutManager.getItemCount();
    if (i == 0)
      return -1; 
    View view = null;
    if (paramLayoutManager.canScrollVertically()) {
      view = findStartView(paramLayoutManager, getVerticalHelper(paramLayoutManager));
    } else if (paramLayoutManager.canScrollHorizontally()) {
      view = findStartView(paramLayoutManager, getHorizontalHelper(paramLayoutManager));
    } 
    if (view == null)
      return -1; 
    int j = paramLayoutManager.getPosition(view);
    if (j == -1)
      return -1; 
    if (paramLayoutManager.canScrollHorizontally()) {
      if (paramInt1 > 0) {
        paramInt2 = 1;
      } else {
        paramInt2 = 0;
      } 
    } else if (paramInt2 > 0) {
      paramInt2 = 1;
    } else {
      paramInt2 = 0;
    } 
    boolean bool = false;
    paramInt1 = bool;
    if (paramLayoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) {
      PointF pointF = ((RecyclerView.SmoothScroller.ScrollVectorProvider)paramLayoutManager).computeScrollVectorForPosition(i - 1);
      paramInt1 = bool;
      if (pointF != null)
        if (pointF.x < 0.0F || pointF.y < 0.0F) {
          paramInt1 = 1;
        } else {
          paramInt1 = 0;
        }  
    } 
    if (paramInt1 != 0) {
      paramInt1 = j;
      if (paramInt2 != 0)
        paramInt1 = j - 1; 
      return paramInt1;
    } 
    paramInt1 = j;
    if (paramInt2 != 0)
      paramInt1 = j + 1; 
    return paramInt1;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/PagerSnapHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */