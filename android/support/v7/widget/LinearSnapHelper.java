package android.support.v7.widget;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public class LinearSnapHelper extends SnapHelper {
  private static final float INVALID_DISTANCE = 1.0F;
  
  @Nullable
  private OrientationHelper mHorizontalHelper;
  
  @Nullable
  private OrientationHelper mVerticalHelper;
  
  private float computeDistancePerChild(RecyclerView.LayoutManager paramLayoutManager, OrientationHelper paramOrientationHelper) {
    View view1 = null;
    View view2 = null;
    int i = Integer.MAX_VALUE;
    int j = Integer.MIN_VALUE;
    int k = paramLayoutManager.getChildCount();
    if (k == 0)
      return 1.0F; 
    byte b = 0;
    while (b < k) {
      View view4;
      int i1;
      View view3 = paramLayoutManager.getChildAt(b);
      int n = paramLayoutManager.getPosition(view3);
      if (n == -1) {
        view4 = view1;
        i1 = i;
        i = j;
      } else {
        int i2 = i;
        if (n < i) {
          i2 = n;
          view1 = view3;
        } 
        i = j;
        i1 = i2;
        view4 = view1;
        if (n > j) {
          i = n;
          view2 = view3;
          i1 = i2;
          view4 = view1;
        } 
      } 
      b++;
      j = i;
      i = i1;
      view1 = view4;
    } 
    if (view1 == null || view2 == null)
      return 1.0F; 
    int m = Math.min(paramOrientationHelper.getDecoratedStart(view1), paramOrientationHelper.getDecoratedStart(view2));
    m = Math.max(paramOrientationHelper.getDecoratedEnd(view1), paramOrientationHelper.getDecoratedEnd(view2)) - m;
    return (m == 0) ? 1.0F : (1.0F * m / (j - i + 1));
  }
  
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
  
  private int estimateNextPositionDiffForFling(RecyclerView.LayoutManager paramLayoutManager, OrientationHelper paramOrientationHelper, int paramInt1, int paramInt2) {
    boolean bool = false;
    int[] arrayOfInt = calculateScrollDistance(paramInt1, paramInt2);
    float f = computeDistancePerChild(paramLayoutManager, paramOrientationHelper);
    if (f <= 0.0F)
      return bool; 
    if (Math.abs(arrayOfInt[0]) > Math.abs(arrayOfInt[1])) {
      paramInt1 = arrayOfInt[0];
    } else {
      paramInt1 = arrayOfInt[1];
    } 
    return (paramInt1 > 0) ? (int)Math.floor((paramInt1 / f)) : (int)Math.ceil((paramInt1 / f));
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
  
  public View findSnapView(RecyclerView.LayoutManager paramLayoutManager) {
    return paramLayoutManager.canScrollVertically() ? findCenterView(paramLayoutManager, getVerticalHelper(paramLayoutManager)) : (paramLayoutManager.canScrollHorizontally() ? findCenterView(paramLayoutManager, getHorizontalHelper(paramLayoutManager)) : null);
  }
  
  public int findTargetSnapPosition(RecyclerView.LayoutManager paramLayoutManager, int paramInt1, int paramInt2) {
    byte b = -1;
    if (!(paramLayoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider))
      return b; 
    int j = paramLayoutManager.getItemCount();
    int i = b;
    if (j != 0) {
      View view = findSnapView(paramLayoutManager);
      i = b;
      if (view != null) {
        int k = paramLayoutManager.getPosition(view);
        i = b;
        if (k != -1) {
          PointF pointF = ((RecyclerView.SmoothScroller.ScrollVectorProvider)paramLayoutManager).computeScrollVectorForPosition(j - 1);
          i = b;
          if (pointF != null) {
            if (paramLayoutManager.canScrollHorizontally()) {
              i = estimateNextPositionDiffForFling(paramLayoutManager, getHorizontalHelper(paramLayoutManager), paramInt1, 0);
              paramInt1 = i;
              if (pointF.x < 0.0F)
                paramInt1 = -i; 
            } else {
              paramInt1 = 0;
            } 
            if (paramLayoutManager.canScrollVertically()) {
              i = estimateNextPositionDiffForFling(paramLayoutManager, getVerticalHelper(paramLayoutManager), 0, paramInt2);
              paramInt2 = i;
              if (pointF.y < 0.0F)
                paramInt2 = -i; 
            } else {
              paramInt2 = 0;
            } 
            if (paramLayoutManager.canScrollVertically())
              paramInt1 = paramInt2; 
            i = b;
            if (paramInt1 != 0) {
              paramInt2 = k + paramInt1;
              paramInt1 = paramInt2;
              if (paramInt2 < 0)
                paramInt1 = 0; 
              i = paramInt1;
              if (paramInt1 >= j)
                i = j - 1; 
            } 
          } 
        } 
      } 
    } 
    return i;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/LinearSnapHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */