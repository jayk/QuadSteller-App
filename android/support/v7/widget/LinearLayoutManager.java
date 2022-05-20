package android.support.v7.widget;

import android.content.Context;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import java.util.List;

public class LinearLayoutManager extends RecyclerView.LayoutManager implements ItemTouchHelper.ViewDropHandler, RecyclerView.SmoothScroller.ScrollVectorProvider {
  static final boolean DEBUG = false;
  
  public static final int HORIZONTAL = 0;
  
  public static final int INVALID_OFFSET = -2147483648;
  
  private static final float MAX_SCROLL_FACTOR = 0.33333334F;
  
  private static final String TAG = "LinearLayoutManager";
  
  public static final int VERTICAL = 1;
  
  final AnchorInfo mAnchorInfo = new AnchorInfo();
  
  private int mInitialItemPrefetchCount = 2;
  
  private boolean mLastStackFromEnd;
  
  private final LayoutChunkResult mLayoutChunkResult = new LayoutChunkResult();
  
  private LayoutState mLayoutState;
  
  int mOrientation;
  
  OrientationHelper mOrientationHelper;
  
  SavedState mPendingSavedState = null;
  
  int mPendingScrollPosition = -1;
  
  int mPendingScrollPositionOffset = Integer.MIN_VALUE;
  
  private boolean mRecycleChildrenOnDetach;
  
  private boolean mReverseLayout = false;
  
  boolean mShouldReverseLayout = false;
  
  private boolean mSmoothScrollbarEnabled = true;
  
  private boolean mStackFromEnd = false;
  
  public LinearLayoutManager(Context paramContext) {
    this(paramContext, 1, false);
  }
  
  public LinearLayoutManager(Context paramContext, int paramInt, boolean paramBoolean) {
    setOrientation(paramInt);
    setReverseLayout(paramBoolean);
    setAutoMeasureEnabled(true);
  }
  
  public LinearLayoutManager(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    RecyclerView.LayoutManager.Properties properties = getProperties(paramContext, paramAttributeSet, paramInt1, paramInt2);
    setOrientation(properties.orientation);
    setReverseLayout(properties.reverseLayout);
    setStackFromEnd(properties.stackFromEnd);
    setAutoMeasureEnabled(true);
  }
  
  private int computeScrollExtent(RecyclerView.State paramState) {
    boolean bool = false;
    int i = 0;
    if (getChildCount() != 0) {
      ensureLayoutState();
      OrientationHelper orientationHelper = this.mOrientationHelper;
      if (!this.mSmoothScrollbarEnabled) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      View view = findFirstVisibleChildClosestToStart(bool1, true);
      boolean bool1 = bool;
      if (!this.mSmoothScrollbarEnabled)
        bool1 = true; 
      i = ScrollbarHelper.computeScrollExtent(paramState, orientationHelper, view, findFirstVisibleChildClosestToEnd(bool1, true), this, this.mSmoothScrollbarEnabled);
    } 
    return i;
  }
  
  private int computeScrollOffset(RecyclerView.State paramState) {
    boolean bool = false;
    int i = 0;
    if (getChildCount() != 0) {
      ensureLayoutState();
      OrientationHelper orientationHelper = this.mOrientationHelper;
      if (!this.mSmoothScrollbarEnabled) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      View view = findFirstVisibleChildClosestToStart(bool1, true);
      boolean bool1 = bool;
      if (!this.mSmoothScrollbarEnabled)
        bool1 = true; 
      i = ScrollbarHelper.computeScrollOffset(paramState, orientationHelper, view, findFirstVisibleChildClosestToEnd(bool1, true), this, this.mSmoothScrollbarEnabled, this.mShouldReverseLayout);
    } 
    return i;
  }
  
  private int computeScrollRange(RecyclerView.State paramState) {
    boolean bool = false;
    int i = 0;
    if (getChildCount() != 0) {
      ensureLayoutState();
      OrientationHelper orientationHelper = this.mOrientationHelper;
      if (!this.mSmoothScrollbarEnabled) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      View view = findFirstVisibleChildClosestToStart(bool1, true);
      boolean bool1 = bool;
      if (!this.mSmoothScrollbarEnabled)
        bool1 = true; 
      i = ScrollbarHelper.computeScrollRange(paramState, orientationHelper, view, findFirstVisibleChildClosestToEnd(bool1, true), this, this.mSmoothScrollbarEnabled);
    } 
    return i;
  }
  
  private View findFirstReferenceChild(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return findReferenceChild(paramRecycler, paramState, 0, getChildCount(), paramState.getItemCount());
  }
  
  private View findFirstVisibleChildClosestToEnd(boolean paramBoolean1, boolean paramBoolean2) {
    return this.mShouldReverseLayout ? findOneVisibleChild(0, getChildCount(), paramBoolean1, paramBoolean2) : findOneVisibleChild(getChildCount() - 1, -1, paramBoolean1, paramBoolean2);
  }
  
  private View findFirstVisibleChildClosestToStart(boolean paramBoolean1, boolean paramBoolean2) {
    return this.mShouldReverseLayout ? findOneVisibleChild(getChildCount() - 1, -1, paramBoolean1, paramBoolean2) : findOneVisibleChild(0, getChildCount(), paramBoolean1, paramBoolean2);
  }
  
  private View findLastReferenceChild(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return findReferenceChild(paramRecycler, paramState, getChildCount() - 1, -1, paramState.getItemCount());
  }
  
  private View findReferenceChildClosestToEnd(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return this.mShouldReverseLayout ? findFirstReferenceChild(paramRecycler, paramState) : findLastReferenceChild(paramRecycler, paramState);
  }
  
  private View findReferenceChildClosestToStart(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return this.mShouldReverseLayout ? findLastReferenceChild(paramRecycler, paramState) : findFirstReferenceChild(paramRecycler, paramState);
  }
  
  private int fixLayoutEndGap(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, boolean paramBoolean) {
    int i = this.mOrientationHelper.getEndAfterPadding() - paramInt;
    if (i > 0) {
      i = -scrollBy(-i, paramRecycler, paramState);
      if (paramBoolean) {
        paramInt = this.mOrientationHelper.getEndAfterPadding() - paramInt + i;
        if (paramInt > 0) {
          this.mOrientationHelper.offsetChildren(paramInt);
          paramInt += i;
          return paramInt;
        } 
      } 
    } else {
      return 0;
    } 
    return i;
  }
  
  private int fixLayoutStartGap(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, boolean paramBoolean) {
    int i = paramInt - this.mOrientationHelper.getStartAfterPadding();
    if (i > 0) {
      i = -scrollBy(i, paramRecycler, paramState);
      if (paramBoolean) {
        paramInt = paramInt + i - this.mOrientationHelper.getStartAfterPadding();
        if (paramInt > 0) {
          this.mOrientationHelper.offsetChildren(-paramInt);
          return i - paramInt;
        } 
      } 
    } else {
      return 0;
    } 
    return i;
  }
  
  private View getChildClosestToEnd() {
    if (this.mShouldReverseLayout) {
      boolean bool = false;
      return getChildAt(bool);
    } 
    int i = getChildCount() - 1;
    return getChildAt(i);
  }
  
  private View getChildClosestToStart() {
    if (this.mShouldReverseLayout) {
      int i = getChildCount() - 1;
      return getChildAt(i);
    } 
    boolean bool = false;
    return getChildAt(bool);
  }
  
  private void layoutForPredictiveAnimations(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt1, int paramInt2) {
    if (paramState.willRunPredictiveAnimations() && getChildCount() != 0 && !paramState.isPreLayout() && supportsPredictiveItemAnimations()) {
      int i = 0;
      int j = 0;
      List<RecyclerView.ViewHolder> list = paramRecycler.getScrapList();
      int k = list.size();
      int m = getPosition(getChildAt(0));
      for (byte b = 0; b < k; b++) {
        RecyclerView.ViewHolder viewHolder = list.get(b);
        if (!viewHolder.isRemoved()) {
          boolean bool;
          boolean bool1;
          if (viewHolder.getLayoutPosition() < m) {
            bool = true;
          } else {
            bool = false;
          } 
          if (bool != this.mShouldReverseLayout) {
            bool1 = true;
          } else {
            bool1 = true;
          } 
          if (bool1 == -1) {
            i += this.mOrientationHelper.getDecoratedMeasurement(viewHolder.itemView);
          } else {
            j += this.mOrientationHelper.getDecoratedMeasurement(viewHolder.itemView);
          } 
        } 
      } 
      this.mLayoutState.mScrapList = list;
      if (i > 0) {
        updateLayoutStateToFillStart(getPosition(getChildClosestToStart()), paramInt1);
        this.mLayoutState.mExtra = i;
        this.mLayoutState.mAvailable = 0;
        this.mLayoutState.assignPositionFromScrapList();
        fill(paramRecycler, this.mLayoutState, paramState, false);
      } 
      if (j > 0) {
        updateLayoutStateToFillEnd(getPosition(getChildClosestToEnd()), paramInt2);
        this.mLayoutState.mExtra = j;
        this.mLayoutState.mAvailable = 0;
        this.mLayoutState.assignPositionFromScrapList();
        fill(paramRecycler, this.mLayoutState, paramState, false);
      } 
      this.mLayoutState.mScrapList = null;
    } 
  }
  
  private void logChildren() {
    Log.d("LinearLayoutManager", "internal representation of views on the screen");
    for (byte b = 0; b < getChildCount(); b++) {
      View view = getChildAt(b);
      Log.d("LinearLayoutManager", "item " + getPosition(view) + ", coord:" + this.mOrientationHelper.getDecoratedStart(view));
    } 
    Log.d("LinearLayoutManager", "==============");
  }
  
  private void recycleByLayoutState(RecyclerView.Recycler paramRecycler, LayoutState paramLayoutState) {
    if (paramLayoutState.mRecycle && !paramLayoutState.mInfinite) {
      if (paramLayoutState.mLayoutDirection == -1) {
        recycleViewsFromEnd(paramRecycler, paramLayoutState.mScrollingOffset);
        return;
      } 
      recycleViewsFromStart(paramRecycler, paramLayoutState.mScrollingOffset);
    } 
  }
  
  private void recycleChildren(RecyclerView.Recycler paramRecycler, int paramInt1, int paramInt2) {
    if (paramInt1 != paramInt2) {
      if (paramInt2 > paramInt1) {
        paramInt2--;
        while (true) {
          if (paramInt2 >= paramInt1) {
            removeAndRecycleViewAt(paramInt2, paramRecycler);
            paramInt2--;
            continue;
          } 
          return;
        } 
      } 
      while (true) {
        if (paramInt1 > paramInt2) {
          removeAndRecycleViewAt(paramInt1, paramRecycler);
          paramInt1--;
          continue;
        } 
        return;
      } 
    } 
  }
  
  private void recycleViewsFromEnd(RecyclerView.Recycler paramRecycler, int paramInt) {
    int i = getChildCount();
    if (paramInt >= 0) {
      int j = this.mOrientationHelper.getEnd() - paramInt;
      if (this.mShouldReverseLayout) {
        paramInt = 0;
        while (true) {
          if (paramInt < i) {
            View view = getChildAt(paramInt);
            if (this.mOrientationHelper.getDecoratedStart(view) < j || this.mOrientationHelper.getTransformedStartWithDecoration(view) < j) {
              recycleChildren(paramRecycler, 0, paramInt);
              return;
            } 
            paramInt++;
            continue;
          } 
          return;
        } 
      } 
      paramInt = i - 1;
      while (true) {
        if (paramInt >= 0) {
          View view = getChildAt(paramInt);
          if (this.mOrientationHelper.getDecoratedStart(view) < j || this.mOrientationHelper.getTransformedStartWithDecoration(view) < j) {
            recycleChildren(paramRecycler, i - 1, paramInt);
            return;
          } 
          paramInt--;
          continue;
        } 
        return;
      } 
    } 
  }
  
  private void recycleViewsFromStart(RecyclerView.Recycler paramRecycler, int paramInt) {
    if (paramInt >= 0) {
      int i = getChildCount();
      if (this.mShouldReverseLayout) {
        int j = i - 1;
        while (true) {
          if (j >= 0) {
            View view = getChildAt(j);
            if (this.mOrientationHelper.getDecoratedEnd(view) > paramInt || this.mOrientationHelper.getTransformedEndWithDecoration(view) > paramInt) {
              recycleChildren(paramRecycler, i - 1, j);
              return;
            } 
            j--;
            continue;
          } 
          return;
        } 
      } 
      byte b = 0;
      while (true) {
        if (b < i) {
          View view = getChildAt(b);
          if (this.mOrientationHelper.getDecoratedEnd(view) > paramInt || this.mOrientationHelper.getTransformedEndWithDecoration(view) > paramInt) {
            recycleChildren(paramRecycler, 0, b);
            return;
          } 
          b++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  private void resolveShouldLayoutReverse() {
    boolean bool = true;
    if (this.mOrientation == 1 || !isLayoutRTL()) {
      this.mShouldReverseLayout = this.mReverseLayout;
      return;
    } 
    if (this.mReverseLayout)
      bool = false; 
    this.mShouldReverseLayout = bool;
  }
  
  private boolean updateAnchorFromChildren(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, AnchorInfo paramAnchorInfo) {
    boolean bool1 = false;
    if (getChildCount() == 0)
      return bool1; 
    View view = getFocusedChild();
    if (view != null && paramAnchorInfo.isViewValidAsAnchor(view, paramState)) {
      paramAnchorInfo.assignFromViewAndKeepVisibleRect(view);
      return true;
    } 
    boolean bool2 = bool1;
    if (this.mLastStackFromEnd == this.mStackFromEnd) {
      View view1;
      if (paramAnchorInfo.mLayoutFromEnd) {
        view1 = findReferenceChildClosestToEnd(paramRecycler, paramState);
      } else {
        view1 = findReferenceChildClosestToStart((RecyclerView.Recycler)view1, paramState);
      } 
      bool2 = bool1;
      if (view1 != null) {
        paramAnchorInfo.assignFromView(view1);
        if (!paramState.isPreLayout() && supportsPredictiveItemAnimations()) {
          int i;
          if (this.mOrientationHelper.getDecoratedStart(view1) >= this.mOrientationHelper.getEndAfterPadding() || this.mOrientationHelper.getDecoratedEnd(view1) < this.mOrientationHelper.getStartAfterPadding()) {
            i = 1;
          } else {
            i = 0;
          } 
          if (i) {
            if (paramAnchorInfo.mLayoutFromEnd) {
              i = this.mOrientationHelper.getEndAfterPadding();
            } else {
              i = this.mOrientationHelper.getStartAfterPadding();
            } 
            paramAnchorInfo.mCoordinate = i;
          } 
        } 
        bool2 = true;
      } 
    } 
    return bool2;
  }
  
  private boolean updateAnchorFromPendingData(RecyclerView.State paramState, AnchorInfo paramAnchorInfo) {
    boolean bool1 = false;
    boolean bool2 = true;
    if (paramState.isPreLayout() || this.mPendingScrollPosition == -1)
      return false; 
    if (this.mPendingScrollPosition < 0 || this.mPendingScrollPosition >= paramState.getItemCount()) {
      this.mPendingScrollPosition = -1;
      this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
      return false;
    } 
    paramAnchorInfo.mPosition = this.mPendingScrollPosition;
    if (this.mPendingSavedState != null && this.mPendingSavedState.hasValidAnchor()) {
      paramAnchorInfo.mLayoutFromEnd = this.mPendingSavedState.mAnchorLayoutFromEnd;
      if (paramAnchorInfo.mLayoutFromEnd) {
        paramAnchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - this.mPendingSavedState.mAnchorOffset;
        return bool2;
      } 
      paramAnchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding() + this.mPendingSavedState.mAnchorOffset;
      return bool2;
    } 
    if (this.mPendingScrollPositionOffset == Integer.MIN_VALUE) {
      View view = findViewByPosition(this.mPendingScrollPosition);
      if (view != null) {
        int i;
        if (this.mOrientationHelper.getDecoratedMeasurement(view) > this.mOrientationHelper.getTotalSpace()) {
          paramAnchorInfo.assignCoordinateFromPadding();
          return bool2;
        } 
        if (this.mOrientationHelper.getDecoratedStart(view) - this.mOrientationHelper.getStartAfterPadding() < 0) {
          paramAnchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding();
          paramAnchorInfo.mLayoutFromEnd = false;
          return bool2;
        } 
        if (this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(view) < 0) {
          paramAnchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding();
          paramAnchorInfo.mLayoutFromEnd = true;
          return bool2;
        } 
        if (paramAnchorInfo.mLayoutFromEnd) {
          i = this.mOrientationHelper.getDecoratedEnd(view) + this.mOrientationHelper.getTotalSpaceChange();
        } else {
          i = this.mOrientationHelper.getDecoratedStart(view);
        } 
        paramAnchorInfo.mCoordinate = i;
        return bool2;
      } 
      if (getChildCount() > 0) {
        boolean bool;
        int i = getPosition(getChildAt(0));
        if (this.mPendingScrollPosition < i) {
          bool = true;
        } else {
          bool = false;
        } 
        if (bool == this.mShouldReverseLayout)
          bool1 = true; 
        paramAnchorInfo.mLayoutFromEnd = bool1;
      } 
      paramAnchorInfo.assignCoordinateFromPadding();
      return bool2;
    } 
    paramAnchorInfo.mLayoutFromEnd = this.mShouldReverseLayout;
    if (this.mShouldReverseLayout) {
      paramAnchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - this.mPendingScrollPositionOffset;
      return bool2;
    } 
    paramAnchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding() + this.mPendingScrollPositionOffset;
    return bool2;
  }
  
  private void updateAnchorInfoForLayout(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, AnchorInfo paramAnchorInfo) {
    if (!updateAnchorFromPendingData(paramState, paramAnchorInfo) && !updateAnchorFromChildren(paramRecycler, paramState, paramAnchorInfo)) {
      boolean bool;
      paramAnchorInfo.assignCoordinateFromPadding();
      if (this.mStackFromEnd) {
        bool = paramState.getItemCount() - 1;
      } else {
        bool = false;
      } 
      paramAnchorInfo.mPosition = bool;
    } 
  }
  
  private void updateLayoutState(int paramInt1, int paramInt2, boolean paramBoolean, RecyclerView.State paramState) {
    byte b = -1;
    boolean bool = true;
    this.mLayoutState.mInfinite = resolveIsInfinite();
    this.mLayoutState.mExtra = getExtraLayoutSpace(paramState);
    this.mLayoutState.mLayoutDirection = paramInt1;
    if (paramInt1 == 1) {
      LayoutState layoutState1 = this.mLayoutState;
      layoutState1.mExtra += this.mOrientationHelper.getEndPadding();
      View view = getChildClosestToEnd();
      LayoutState layoutState2 = this.mLayoutState;
      if (this.mShouldReverseLayout) {
        paramInt1 = b;
      } else {
        paramInt1 = 1;
      } 
      layoutState2.mItemDirection = paramInt1;
      this.mLayoutState.mCurrentPosition = getPosition(view) + this.mLayoutState.mItemDirection;
      this.mLayoutState.mOffset = this.mOrientationHelper.getDecoratedEnd(view);
      paramInt1 = this.mOrientationHelper.getDecoratedEnd(view) - this.mOrientationHelper.getEndAfterPadding();
    } else {
      View view = getChildClosestToStart();
      LayoutState layoutState = this.mLayoutState;
      layoutState.mExtra += this.mOrientationHelper.getStartAfterPadding();
      layoutState = this.mLayoutState;
      if (this.mShouldReverseLayout) {
        paramInt1 = bool;
      } else {
        paramInt1 = -1;
      } 
      layoutState.mItemDirection = paramInt1;
      this.mLayoutState.mCurrentPosition = getPosition(view) + this.mLayoutState.mItemDirection;
      this.mLayoutState.mOffset = this.mOrientationHelper.getDecoratedStart(view);
      paramInt1 = -this.mOrientationHelper.getDecoratedStart(view) + this.mOrientationHelper.getStartAfterPadding();
    } 
    this.mLayoutState.mAvailable = paramInt2;
    if (paramBoolean) {
      LayoutState layoutState = this.mLayoutState;
      layoutState.mAvailable -= paramInt1;
    } 
    this.mLayoutState.mScrollingOffset = paramInt1;
  }
  
  private void updateLayoutStateToFillEnd(int paramInt1, int paramInt2) {
    boolean bool;
    this.mLayoutState.mAvailable = this.mOrientationHelper.getEndAfterPadding() - paramInt2;
    LayoutState layoutState = this.mLayoutState;
    if (this.mShouldReverseLayout) {
      bool = true;
    } else {
      bool = true;
    } 
    layoutState.mItemDirection = bool;
    this.mLayoutState.mCurrentPosition = paramInt1;
    this.mLayoutState.mLayoutDirection = 1;
    this.mLayoutState.mOffset = paramInt2;
    this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
  }
  
  private void updateLayoutStateToFillEnd(AnchorInfo paramAnchorInfo) {
    updateLayoutStateToFillEnd(paramAnchorInfo.mPosition, paramAnchorInfo.mCoordinate);
  }
  
  private void updateLayoutStateToFillStart(int paramInt1, int paramInt2) {
    this.mLayoutState.mAvailable = paramInt2 - this.mOrientationHelper.getStartAfterPadding();
    this.mLayoutState.mCurrentPosition = paramInt1;
    LayoutState layoutState = this.mLayoutState;
    if (this.mShouldReverseLayout) {
      paramInt1 = 1;
    } else {
      paramInt1 = -1;
    } 
    layoutState.mItemDirection = paramInt1;
    this.mLayoutState.mLayoutDirection = -1;
    this.mLayoutState.mOffset = paramInt2;
    this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
  }
  
  private void updateLayoutStateToFillStart(AnchorInfo paramAnchorInfo) {
    updateLayoutStateToFillStart(paramAnchorInfo.mPosition, paramAnchorInfo.mCoordinate);
  }
  
  public void assertNotInLayoutOrScroll(String paramString) {
    if (this.mPendingSavedState == null)
      super.assertNotInLayoutOrScroll(paramString); 
  }
  
  public boolean canScrollHorizontally() {
    return (this.mOrientation == 0);
  }
  
  public boolean canScrollVertically() {
    boolean bool = true;
    if (this.mOrientation != 1)
      bool = false; 
    return bool;
  }
  
  public void collectAdjacentPrefetchPositions(int paramInt1, int paramInt2, RecyclerView.State paramState, RecyclerView.LayoutManager.LayoutPrefetchRegistry paramLayoutPrefetchRegistry) {
    if (this.mOrientation != 0)
      paramInt1 = paramInt2; 
    if (getChildCount() != 0 && paramInt1 != 0) {
      if (paramInt1 > 0) {
        paramInt2 = 1;
      } else {
        paramInt2 = -1;
      } 
      updateLayoutState(paramInt2, Math.abs(paramInt1), true, paramState);
      collectPrefetchPositionsForLayoutState(paramState, this.mLayoutState, paramLayoutPrefetchRegistry);
    } 
  }
  
  public void collectInitialPrefetchPositions(int paramInt, RecyclerView.LayoutManager.LayoutPrefetchRegistry paramLayoutPrefetchRegistry) {
    boolean bool;
    int i;
    byte b = -1;
    if (this.mPendingSavedState != null && this.mPendingSavedState.hasValidAnchor()) {
      bool = this.mPendingSavedState.mAnchorLayoutFromEnd;
      i = this.mPendingSavedState.mAnchorPosition;
    } else {
      resolveShouldLayoutReverse();
      bool = this.mShouldReverseLayout;
      if (this.mPendingScrollPosition == -1) {
        if (bool) {
          i = paramInt - 1;
        } else {
          i = 0;
        } 
      } else {
        i = this.mPendingScrollPosition;
      } 
    } 
    if (!bool)
      b = 1; 
    for (byte b1 = 0; b1 < this.mInitialItemPrefetchCount && i >= 0 && i < paramInt; b1++) {
      paramLayoutPrefetchRegistry.addPosition(i, 0);
      i += b;
    } 
  }
  
  void collectPrefetchPositionsForLayoutState(RecyclerView.State paramState, LayoutState paramLayoutState, RecyclerView.LayoutManager.LayoutPrefetchRegistry paramLayoutPrefetchRegistry) {
    int i = paramLayoutState.mCurrentPosition;
    if (i >= 0 && i < paramState.getItemCount())
      paramLayoutPrefetchRegistry.addPosition(i, paramLayoutState.mScrollingOffset); 
  }
  
  public int computeHorizontalScrollExtent(RecyclerView.State paramState) {
    return computeScrollExtent(paramState);
  }
  
  public int computeHorizontalScrollOffset(RecyclerView.State paramState) {
    return computeScrollOffset(paramState);
  }
  
  public int computeHorizontalScrollRange(RecyclerView.State paramState) {
    return computeScrollRange(paramState);
  }
  
  public PointF computeScrollVectorForPosition(int paramInt) {
    boolean bool = false;
    if (getChildCount() == 0)
      return null; 
    if (paramInt < getPosition(getChildAt(0)))
      bool = true; 
    if (bool != this.mShouldReverseLayout) {
      paramInt = -1;
    } else {
      paramInt = 1;
    } 
    return (this.mOrientation == 0) ? new PointF(paramInt, 0.0F) : new PointF(0.0F, paramInt);
  }
  
  public int computeVerticalScrollExtent(RecyclerView.State paramState) {
    return computeScrollExtent(paramState);
  }
  
  public int computeVerticalScrollOffset(RecyclerView.State paramState) {
    return computeScrollOffset(paramState);
  }
  
  public int computeVerticalScrollRange(RecyclerView.State paramState) {
    return computeScrollRange(paramState);
  }
  
  int convertFocusDirectionToLayoutDirection(int paramInt) {
    byte b = -1;
    int i = Integer.MIN_VALUE;
    boolean bool = true;
    switch (paramInt) {
      default:
        return Integer.MIN_VALUE;
      case 1:
        paramInt = b;
        if (this.mOrientation != 1) {
          paramInt = b;
          if (isLayoutRTL())
            paramInt = 1; 
        } 
        return paramInt;
      case 2:
        if (this.mOrientation == 1)
          return 1; 
        paramInt = b;
        if (!isLayoutRTL())
          paramInt = 1; 
        return paramInt;
      case 33:
        paramInt = b;
        if (this.mOrientation != 1)
          paramInt = Integer.MIN_VALUE; 
        return paramInt;
      case 130:
        paramInt = i;
        if (this.mOrientation == 1)
          paramInt = 1; 
        return paramInt;
      case 17:
        paramInt = b;
        if (this.mOrientation != 0)
          paramInt = Integer.MIN_VALUE; 
        return paramInt;
      case 66:
        break;
    } 
    return (this.mOrientation == 0) ? bool : Integer.MIN_VALUE;
  }
  
  LayoutState createLayoutState() {
    return new LayoutState();
  }
  
  void ensureLayoutState() {
    if (this.mLayoutState == null)
      this.mLayoutState = createLayoutState(); 
    if (this.mOrientationHelper == null)
      this.mOrientationHelper = OrientationHelper.createOrientationHelper(this, this.mOrientation); 
  }
  
  int fill(RecyclerView.Recycler paramRecycler, LayoutState paramLayoutState, RecyclerView.State paramState, boolean paramBoolean) {
    int i = paramLayoutState.mAvailable;
    if (paramLayoutState.mScrollingOffset != Integer.MIN_VALUE) {
      if (paramLayoutState.mAvailable < 0)
        paramLayoutState.mScrollingOffset += paramLayoutState.mAvailable; 
      recycleByLayoutState(paramRecycler, paramLayoutState);
    } 
    int j = paramLayoutState.mAvailable + paramLayoutState.mExtra;
    LayoutChunkResult layoutChunkResult = this.mLayoutChunkResult;
    while (true) {
      while (true)
        break; 
      if (paramBoolean) {
        Object object = SYNTHETIC_LOCAL_VARIABLE_8;
        if (layoutChunkResult.mFocusable)
          return i - paramLayoutState.mAvailable; 
      } 
    } 
  }
  
  public int findFirstCompletelyVisibleItemPosition() {
    View view = findOneVisibleChild(0, getChildCount(), true, false);
    return (view == null) ? -1 : getPosition(view);
  }
  
  public int findFirstVisibleItemPosition() {
    View view = findOneVisibleChild(0, getChildCount(), false, true);
    return (view == null) ? -1 : getPosition(view);
  }
  
  public int findLastCompletelyVisibleItemPosition() {
    int i = -1;
    View view = findOneVisibleChild(getChildCount() - 1, -1, true, false);
    if (view != null)
      i = getPosition(view); 
    return i;
  }
  
  public int findLastVisibleItemPosition() {
    int i = -1;
    View view = findOneVisibleChild(getChildCount() - 1, -1, false, true);
    if (view != null)
      i = getPosition(view); 
    return i;
  }
  
  View findOneVisibleChild(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual ensureLayoutState : ()V
    //   4: aload_0
    //   5: getfield mOrientationHelper : Landroid/support/v7/widget/OrientationHelper;
    //   8: invokevirtual getStartAfterPadding : ()I
    //   11: istore #5
    //   13: aload_0
    //   14: getfield mOrientationHelper : Landroid/support/v7/widget/OrientationHelper;
    //   17: invokevirtual getEndAfterPadding : ()I
    //   20: istore #6
    //   22: iload_2
    //   23: iload_1
    //   24: if_icmple -> 118
    //   27: iconst_1
    //   28: istore #7
    //   30: aconst_null
    //   31: astore #8
    //   33: iload_1
    //   34: iload_2
    //   35: if_icmpeq -> 158
    //   38: aload_0
    //   39: iload_1
    //   40: invokevirtual getChildAt : (I)Landroid/view/View;
    //   43: astore #9
    //   45: aload_0
    //   46: getfield mOrientationHelper : Landroid/support/v7/widget/OrientationHelper;
    //   49: aload #9
    //   51: invokevirtual getDecoratedStart : (Landroid/view/View;)I
    //   54: istore #10
    //   56: aload_0
    //   57: getfield mOrientationHelper : Landroid/support/v7/widget/OrientationHelper;
    //   60: aload #9
    //   62: invokevirtual getDecoratedEnd : (Landroid/view/View;)I
    //   65: istore #11
    //   67: aload #8
    //   69: astore #12
    //   71: iload #10
    //   73: iload #6
    //   75: if_icmpge -> 146
    //   78: aload #8
    //   80: astore #12
    //   82: iload #11
    //   84: iload #5
    //   86: if_icmple -> 146
    //   89: aload #9
    //   91: astore #12
    //   93: iload_3
    //   94: ifeq -> 115
    //   97: iload #10
    //   99: iload #5
    //   101: if_icmplt -> 124
    //   104: iload #11
    //   106: iload #6
    //   108: if_icmpgt -> 124
    //   111: aload #9
    //   113: astore #12
    //   115: aload #12
    //   117: areturn
    //   118: iconst_m1
    //   119: istore #7
    //   121: goto -> 30
    //   124: aload #8
    //   126: astore #12
    //   128: iload #4
    //   130: ifeq -> 146
    //   133: aload #8
    //   135: astore #12
    //   137: aload #8
    //   139: ifnonnull -> 146
    //   142: aload #9
    //   144: astore #12
    //   146: iload_1
    //   147: iload #7
    //   149: iadd
    //   150: istore_1
    //   151: aload #12
    //   153: astore #8
    //   155: goto -> 33
    //   158: aload #8
    //   160: astore #12
    //   162: goto -> 115
  }
  
  View findReferenceChild(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt1, int paramInt2, int paramInt3) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual ensureLayoutState : ()V
    //   4: aconst_null
    //   5: astore_2
    //   6: aconst_null
    //   7: astore_1
    //   8: aload_0
    //   9: getfield mOrientationHelper : Landroid/support/v7/widget/OrientationHelper;
    //   12: invokevirtual getStartAfterPadding : ()I
    //   15: istore #6
    //   17: aload_0
    //   18: getfield mOrientationHelper : Landroid/support/v7/widget/OrientationHelper;
    //   21: invokevirtual getEndAfterPadding : ()I
    //   24: istore #7
    //   26: iload #4
    //   28: iload_3
    //   29: if_icmple -> 125
    //   32: iconst_1
    //   33: istore #8
    //   35: iload_3
    //   36: iload #4
    //   38: if_icmpeq -> 183
    //   41: aload_0
    //   42: iload_3
    //   43: invokevirtual getChildAt : (I)Landroid/view/View;
    //   46: astore #9
    //   48: aload_0
    //   49: aload #9
    //   51: invokevirtual getPosition : (Landroid/view/View;)I
    //   54: istore #10
    //   56: aload_2
    //   57: astore #11
    //   59: aload_1
    //   60: astore #12
    //   62: iload #10
    //   64: iflt -> 111
    //   67: aload_2
    //   68: astore #11
    //   70: aload_1
    //   71: astore #12
    //   73: iload #10
    //   75: iload #5
    //   77: if_icmpge -> 111
    //   80: aload #9
    //   82: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   85: checkcast android/support/v7/widget/RecyclerView$LayoutParams
    //   88: invokevirtual isItemRemoved : ()Z
    //   91: ifeq -> 131
    //   94: aload_2
    //   95: astore #11
    //   97: aload_1
    //   98: astore #12
    //   100: aload_2
    //   101: ifnonnull -> 111
    //   104: aload_1
    //   105: astore #12
    //   107: aload #9
    //   109: astore #11
    //   111: iload_3
    //   112: iload #8
    //   114: iadd
    //   115: istore_3
    //   116: aload #11
    //   118: astore_2
    //   119: aload #12
    //   121: astore_1
    //   122: goto -> 35
    //   125: iconst_m1
    //   126: istore #8
    //   128: goto -> 35
    //   131: aload_0
    //   132: getfield mOrientationHelper : Landroid/support/v7/widget/OrientationHelper;
    //   135: aload #9
    //   137: invokevirtual getDecoratedStart : (Landroid/view/View;)I
    //   140: iload #7
    //   142: if_icmpge -> 163
    //   145: aload #9
    //   147: astore #11
    //   149: aload_0
    //   150: getfield mOrientationHelper : Landroid/support/v7/widget/OrientationHelper;
    //   153: aload #9
    //   155: invokevirtual getDecoratedEnd : (Landroid/view/View;)I
    //   158: iload #6
    //   160: if_icmpge -> 190
    //   163: aload_2
    //   164: astore #11
    //   166: aload_1
    //   167: astore #12
    //   169: aload_1
    //   170: ifnonnull -> 111
    //   173: aload_2
    //   174: astore #11
    //   176: aload #9
    //   178: astore #12
    //   180: goto -> 111
    //   183: aload_1
    //   184: ifnull -> 193
    //   187: aload_1
    //   188: astore #11
    //   190: aload #11
    //   192: areturn
    //   193: aload_2
    //   194: astore_1
    //   195: goto -> 187
  }
  
  public View findViewByPosition(int paramInt) {
    int i = getChildCount();
    if (i == 0)
      return null; 
    int j = paramInt - getPosition(getChildAt(0));
    if (j >= 0 && j < i) {
      View view2 = getChildAt(j);
      View view1 = view2;
      return (getPosition(view2) != paramInt) ? super.findViewByPosition(paramInt) : view1;
    } 
    return super.findViewByPosition(paramInt);
  }
  
  public RecyclerView.LayoutParams generateDefaultLayoutParams() {
    return new RecyclerView.LayoutParams(-2, -2);
  }
  
  protected int getExtraLayoutSpace(RecyclerView.State paramState) {
    return paramState.hasTargetScrollPosition() ? this.mOrientationHelper.getTotalSpace() : 0;
  }
  
  public int getInitialItemPrefetchCount() {
    return this.mInitialItemPrefetchCount;
  }
  
  public int getOrientation() {
    return this.mOrientation;
  }
  
  public boolean getRecycleChildrenOnDetach() {
    return this.mRecycleChildrenOnDetach;
  }
  
  public boolean getReverseLayout() {
    return this.mReverseLayout;
  }
  
  public boolean getStackFromEnd() {
    return this.mStackFromEnd;
  }
  
  protected boolean isLayoutRTL() {
    boolean bool = true;
    if (getLayoutDirection() != 1)
      bool = false; 
    return bool;
  }
  
  public boolean isSmoothScrollbarEnabled() {
    return this.mSmoothScrollbarEnabled;
  }
  
  void layoutChunk(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, LayoutState paramLayoutState, LayoutChunkResult paramLayoutChunkResult) {
    int i;
    int j;
    int k;
    int m;
    View view = paramLayoutState.next(paramRecycler);
    if (view == null) {
      paramLayoutChunkResult.mFinished = true;
      return;
    } 
    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
    if (paramLayoutState.mScrapList == null) {
      boolean bool2;
      boolean bool1 = this.mShouldReverseLayout;
      if (paramLayoutState.mLayoutDirection == -1) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      if (bool1 == bool2) {
        addView(view);
      } else {
        addView(view, 0);
      } 
    } else {
      boolean bool2;
      boolean bool1 = this.mShouldReverseLayout;
      if (paramLayoutState.mLayoutDirection == -1) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      if (bool1 == bool2) {
        addDisappearingView(view);
      } else {
        addDisappearingView(view, 0);
      } 
    } 
    measureChildWithMargins(view, 0, 0);
    paramLayoutChunkResult.mConsumed = this.mOrientationHelper.getDecoratedMeasurement(view);
    if (this.mOrientation == 1) {
      if (isLayoutRTL()) {
        i = getWidth() - getPaddingRight();
        j = i - this.mOrientationHelper.getDecoratedMeasurementInOther(view);
      } else {
        j = getPaddingLeft();
        i = j + this.mOrientationHelper.getDecoratedMeasurementInOther(view);
      } 
      if (paramLayoutState.mLayoutDirection == -1) {
        k = paramLayoutState.mOffset;
        m = paramLayoutState.mOffset - paramLayoutChunkResult.mConsumed;
      } else {
        m = paramLayoutState.mOffset;
        k = paramLayoutState.mOffset + paramLayoutChunkResult.mConsumed;
      } 
    } else {
      m = getPaddingTop();
      k = m + this.mOrientationHelper.getDecoratedMeasurementInOther(view);
      if (paramLayoutState.mLayoutDirection == -1) {
        i = paramLayoutState.mOffset;
        j = paramLayoutState.mOffset - paramLayoutChunkResult.mConsumed;
      } else {
        j = paramLayoutState.mOffset;
        i = paramLayoutState.mOffset + paramLayoutChunkResult.mConsumed;
      } 
    } 
    layoutDecoratedWithMargins(view, j, m, i, k);
    if (layoutParams.isItemRemoved() || layoutParams.isItemChanged())
      paramLayoutChunkResult.mIgnoreConsumed = true; 
    paramLayoutChunkResult.mFocusable = view.isFocusable();
  }
  
  void onAnchorReady(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, AnchorInfo paramAnchorInfo, int paramInt) {}
  
  public void onDetachedFromWindow(RecyclerView paramRecyclerView, RecyclerView.Recycler paramRecycler) {
    super.onDetachedFromWindow(paramRecyclerView, paramRecycler);
    if (this.mRecycleChildrenOnDetach) {
      removeAndRecycleAllViews(paramRecycler);
      paramRecycler.clear();
    } 
  }
  
  public View onFocusSearchFailed(View paramView, int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    View view;
    resolveShouldLayoutReverse();
    if (getChildCount() == 0)
      return null; 
    paramInt = convertFocusDirectionToLayoutDirection(paramInt);
    if (paramInt == Integer.MIN_VALUE)
      return null; 
    ensureLayoutState();
    if (paramInt == -1) {
      paramView = findReferenceChildClosestToStart(paramRecycler, paramState);
    } else {
      paramView = findReferenceChildClosestToEnd(paramRecycler, paramState);
    } 
    if (paramView == null)
      return null; 
    ensureLayoutState();
    updateLayoutState(paramInt, (int)(0.33333334F * this.mOrientationHelper.getTotalSpace()), false, paramState);
    this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
    this.mLayoutState.mRecycle = false;
    fill(paramRecycler, this.mLayoutState, paramState, true);
    if (paramInt == -1) {
      view = getChildClosestToStart();
    } else {
      view = getChildClosestToEnd();
    } 
    if (view != paramView) {
      paramView = view;
      return !view.isFocusable() ? null : paramView;
    } 
    return null;
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    if (getChildCount() > 0) {
      AccessibilityRecordCompat accessibilityRecordCompat = AccessibilityEventCompat.asRecord(paramAccessibilityEvent);
      accessibilityRecordCompat.setFromIndex(findFirstVisibleItemPosition());
      accessibilityRecordCompat.setToIndex(findLastVisibleItemPosition());
    } 
  }
  
  public void onLayoutChildren(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    if ((this.mPendingSavedState != null || this.mPendingScrollPosition != -1) && paramState.getItemCount() == 0) {
      removeAndRecycleAllViews(paramRecycler);
      return;
    } 
    if (this.mPendingSavedState != null && this.mPendingSavedState.hasValidAnchor())
      this.mPendingScrollPosition = this.mPendingSavedState.mAnchorPosition; 
    ensureLayoutState();
    this.mLayoutState.mRecycle = false;
    resolveShouldLayoutReverse();
    if (!this.mAnchorInfo.mValid || this.mPendingScrollPosition != -1 || this.mPendingSavedState != null) {
      this.mAnchorInfo.reset();
      this.mAnchorInfo.mLayoutFromEnd = this.mShouldReverseLayout ^ this.mStackFromEnd;
      updateAnchorInfoForLayout(paramRecycler, paramState, this.mAnchorInfo);
      this.mAnchorInfo.mValid = true;
    } 
    int i = getExtraLayoutSpace(paramState);
    if (this.mLayoutState.mLastScrollDelta >= 0) {
      j = 0;
    } else {
      j = i;
      i = 0;
    } 
    int k = j + this.mOrientationHelper.getStartAfterPadding();
    int m = i + this.mOrientationHelper.getEndPadding();
    int j = m;
    i = k;
    if (paramState.isPreLayout()) {
      j = m;
      i = k;
      if (this.mPendingScrollPosition != -1) {
        j = m;
        i = k;
        if (this.mPendingScrollPositionOffset != Integer.MIN_VALUE) {
          View view = findViewByPosition(this.mPendingScrollPosition);
          j = m;
          i = k;
          if (view != null) {
            if (this.mShouldReverseLayout) {
              i = this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(view) - this.mPendingScrollPositionOffset;
            } else {
              j = this.mOrientationHelper.getDecoratedStart(view);
              i = this.mOrientationHelper.getStartAfterPadding();
              i = this.mPendingScrollPositionOffset - j - i;
            } 
            if (i > 0) {
              i = k + i;
              j = m;
            } else {
              j = m - i;
              i = k;
            } 
          } 
        } 
      } 
    } 
    if (this.mAnchorInfo.mLayoutFromEnd) {
      if (this.mShouldReverseLayout) {
        k = 1;
      } else {
        k = -1;
      } 
    } else if (this.mShouldReverseLayout) {
      k = -1;
    } else {
      k = 1;
    } 
    onAnchorReady(paramRecycler, paramState, this.mAnchorInfo, k);
    detachAndScrapAttachedViews(paramRecycler);
    this.mLayoutState.mInfinite = resolveIsInfinite();
    this.mLayoutState.mIsPreLayout = paramState.isPreLayout();
    if (this.mAnchorInfo.mLayoutFromEnd) {
      updateLayoutStateToFillStart(this.mAnchorInfo);
      this.mLayoutState.mExtra = i;
      fill(paramRecycler, this.mLayoutState, paramState, false);
      k = this.mLayoutState.mOffset;
      int n = this.mLayoutState.mCurrentPosition;
      i = j;
      if (this.mLayoutState.mAvailable > 0)
        i = j + this.mLayoutState.mAvailable; 
      updateLayoutStateToFillEnd(this.mAnchorInfo);
      this.mLayoutState.mExtra = i;
      LayoutState layoutState = this.mLayoutState;
      layoutState.mCurrentPosition += this.mLayoutState.mItemDirection;
      fill(paramRecycler, this.mLayoutState, paramState, false);
      m = this.mLayoutState.mOffset;
      i = m;
      j = k;
      if (this.mLayoutState.mAvailable > 0) {
        i = this.mLayoutState.mAvailable;
        updateLayoutStateToFillStart(n, k);
        this.mLayoutState.mExtra = i;
        fill(paramRecycler, this.mLayoutState, paramState, false);
        j = this.mLayoutState.mOffset;
        i = m;
      } 
    } else {
      updateLayoutStateToFillEnd(this.mAnchorInfo);
      this.mLayoutState.mExtra = j;
      fill(paramRecycler, this.mLayoutState, paramState, false);
      k = this.mLayoutState.mOffset;
      int n = this.mLayoutState.mCurrentPosition;
      j = i;
      if (this.mLayoutState.mAvailable > 0)
        j = i + this.mLayoutState.mAvailable; 
      updateLayoutStateToFillStart(this.mAnchorInfo);
      this.mLayoutState.mExtra = j;
      LayoutState layoutState = this.mLayoutState;
      layoutState.mCurrentPosition += this.mLayoutState.mItemDirection;
      fill(paramRecycler, this.mLayoutState, paramState, false);
      m = this.mLayoutState.mOffset;
      i = k;
      j = m;
      if (this.mLayoutState.mAvailable > 0) {
        i = this.mLayoutState.mAvailable;
        updateLayoutStateToFillEnd(n, k);
        this.mLayoutState.mExtra = i;
        fill(paramRecycler, this.mLayoutState, paramState, false);
        i = this.mLayoutState.mOffset;
        j = m;
      } 
    } 
    m = i;
    k = j;
    if (getChildCount() > 0)
      if ((this.mShouldReverseLayout ^ this.mStackFromEnd) != 0) {
        m = fixLayoutEndGap(i, paramRecycler, paramState, true);
        k = j + m;
        j = fixLayoutStartGap(k, paramRecycler, paramState, false);
        k += j;
        m = i + m + j;
      } else {
        k = fixLayoutStartGap(j, paramRecycler, paramState, true);
        m = i + k;
        i = fixLayoutEndGap(m, paramRecycler, paramState, false);
        k = j + k + i;
        m += i;
      }  
    layoutForPredictiveAnimations(paramRecycler, paramState, k, m);
    if (!paramState.isPreLayout()) {
      this.mOrientationHelper.onLayoutComplete();
    } else {
      this.mAnchorInfo.reset();
    } 
    this.mLastStackFromEnd = this.mStackFromEnd;
  }
  
  public void onLayoutCompleted(RecyclerView.State paramState) {
    super.onLayoutCompleted(paramState);
    this.mPendingSavedState = null;
    this.mPendingScrollPosition = -1;
    this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
    this.mAnchorInfo.reset();
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    if (paramParcelable instanceof SavedState) {
      this.mPendingSavedState = (SavedState)paramParcelable;
      requestLayout();
    } 
  }
  
  public Parcelable onSaveInstanceState() {
    if (this.mPendingSavedState != null)
      return new SavedState(this.mPendingSavedState); 
    SavedState savedState = new SavedState();
    if (getChildCount() > 0) {
      ensureLayoutState();
      int i = this.mLastStackFromEnd ^ this.mShouldReverseLayout;
      savedState.mAnchorLayoutFromEnd = i;
      if (i != 0) {
        View view1 = getChildClosestToEnd();
        savedState.mAnchorOffset = this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(view1);
        savedState.mAnchorPosition = getPosition(view1);
        return savedState;
      } 
      View view = getChildClosestToStart();
      savedState.mAnchorPosition = getPosition(view);
      savedState.mAnchorOffset = this.mOrientationHelper.getDecoratedStart(view) - this.mOrientationHelper.getStartAfterPadding();
      return savedState;
    } 
    savedState.invalidateAnchor();
    return savedState;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void prepareForDrop(View paramView1, View paramView2, int paramInt1, int paramInt2) {
    assertNotInLayoutOrScroll("Cannot drop a view during a scroll or layout calculation");
    ensureLayoutState();
    resolveShouldLayoutReverse();
    paramInt1 = getPosition(paramView1);
    paramInt2 = getPosition(paramView2);
    if (paramInt1 < paramInt2) {
      paramInt1 = 1;
    } else {
      paramInt1 = -1;
    } 
    if (this.mShouldReverseLayout) {
      if (paramInt1 == 1) {
        scrollToPositionWithOffset(paramInt2, this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedStart(paramView2) + this.mOrientationHelper.getDecoratedMeasurement(paramView1));
        return;
      } 
      scrollToPositionWithOffset(paramInt2, this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(paramView2));
      return;
    } 
    if (paramInt1 == -1) {
      scrollToPositionWithOffset(paramInt2, this.mOrientationHelper.getDecoratedStart(paramView2));
      return;
    } 
    scrollToPositionWithOffset(paramInt2, this.mOrientationHelper.getDecoratedEnd(paramView2) - this.mOrientationHelper.getDecoratedMeasurement(paramView1));
  }
  
  boolean resolveIsInfinite() {
    return (this.mOrientationHelper.getMode() == 0 && this.mOrientationHelper.getEnd() == 0);
  }
  
  int scrollBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    byte b1;
    byte b = 0;
    int i = b;
    if (getChildCount() != 0) {
      if (paramInt == 0)
        return b; 
    } else {
      return i;
    } 
    this.mLayoutState.mRecycle = true;
    ensureLayoutState();
    if (paramInt > 0) {
      b1 = 1;
    } else {
      b1 = -1;
    } 
    int j = Math.abs(paramInt);
    updateLayoutState(b1, j, true, paramState);
    int k = this.mLayoutState.mScrollingOffset + fill(paramRecycler, this.mLayoutState, paramState, false);
    i = b;
    if (k >= 0) {
      if (j > k)
        paramInt = b1 * k; 
      this.mOrientationHelper.offsetChildren(-paramInt);
      this.mLayoutState.mLastScrollDelta = paramInt;
      i = paramInt;
    } 
    return i;
  }
  
  public int scrollHorizontallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return (this.mOrientation == 1) ? 0 : scrollBy(paramInt, paramRecycler, paramState);
  }
  
  public void scrollToPosition(int paramInt) {
    this.mPendingScrollPosition = paramInt;
    this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
    if (this.mPendingSavedState != null)
      this.mPendingSavedState.invalidateAnchor(); 
    requestLayout();
  }
  
  public void scrollToPositionWithOffset(int paramInt1, int paramInt2) {
    this.mPendingScrollPosition = paramInt1;
    this.mPendingScrollPositionOffset = paramInt2;
    if (this.mPendingSavedState != null)
      this.mPendingSavedState.invalidateAnchor(); 
    requestLayout();
  }
  
  public int scrollVerticallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return (this.mOrientation == 0) ? 0 : scrollBy(paramInt, paramRecycler, paramState);
  }
  
  public void setInitialPrefetchItemCount(int paramInt) {
    this.mInitialItemPrefetchCount = paramInt;
  }
  
  public void setOrientation(int paramInt) {
    if (paramInt != 0 && paramInt != 1)
      throw new IllegalArgumentException("invalid orientation:" + paramInt); 
    assertNotInLayoutOrScroll((String)null);
    if (paramInt != this.mOrientation) {
      this.mOrientation = paramInt;
      this.mOrientationHelper = null;
      requestLayout();
    } 
  }
  
  public void setRecycleChildrenOnDetach(boolean paramBoolean) {
    this.mRecycleChildrenOnDetach = paramBoolean;
  }
  
  public void setReverseLayout(boolean paramBoolean) {
    assertNotInLayoutOrScroll((String)null);
    if (paramBoolean != this.mReverseLayout) {
      this.mReverseLayout = paramBoolean;
      requestLayout();
    } 
  }
  
  public void setSmoothScrollbarEnabled(boolean paramBoolean) {
    this.mSmoothScrollbarEnabled = paramBoolean;
  }
  
  public void setStackFromEnd(boolean paramBoolean) {
    assertNotInLayoutOrScroll((String)null);
    if (this.mStackFromEnd != paramBoolean) {
      this.mStackFromEnd = paramBoolean;
      requestLayout();
    } 
  }
  
  boolean shouldMeasureTwice() {
    return (getHeightMode() != 1073741824 && getWidthMode() != 1073741824 && hasFlexibleChildInBothOrientations());
  }
  
  public void smoothScrollToPosition(RecyclerView paramRecyclerView, RecyclerView.State paramState, int paramInt) {
    LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(paramRecyclerView.getContext());
    linearSmoothScroller.setTargetPosition(paramInt);
    startSmoothScroll(linearSmoothScroller);
  }
  
  public boolean supportsPredictiveItemAnimations() {
    return (this.mPendingSavedState == null && this.mLastStackFromEnd == this.mStackFromEnd);
  }
  
  void validateChildOrder() {
    boolean bool1 = true;
    boolean bool2 = true;
    Log.d("LinearLayoutManager", "validating child count " + getChildCount());
    if (getChildCount() >= 1) {
      int i = getPosition(getChildAt(0));
      int j = this.mOrientationHelper.getDecoratedStart(getChildAt(0));
      if (this.mShouldReverseLayout) {
        byte b1 = 1;
        while (true) {
          if (b1 < getChildCount()) {
            View view = getChildAt(b1);
            int k = getPosition(view);
            int m = this.mOrientationHelper.getDecoratedStart(view);
            if (k < i) {
              logChildren();
              StringBuilder stringBuilder = (new StringBuilder()).append("detected invalid position. loc invalid? ");
              if (m >= j)
                bool2 = false; 
              throw new RuntimeException(stringBuilder.append(bool2).toString());
            } 
            if (m > j) {
              logChildren();
              throw new RuntimeException("detected invalid location");
            } 
            b1++;
            continue;
          } 
          return;
        } 
      } 
      byte b = 1;
      while (true) {
        if (b < getChildCount()) {
          View view = getChildAt(b);
          int m = getPosition(view);
          int k = this.mOrientationHelper.getDecoratedStart(view);
          if (m < i) {
            logChildren();
            StringBuilder stringBuilder = (new StringBuilder()).append("detected invalid position. loc invalid? ");
            if (k < j) {
              bool2 = bool1;
              throw new RuntimeException(stringBuilder.append(bool2).toString());
            } 
            bool2 = false;
            throw new RuntimeException(stringBuilder.append(bool2).toString());
          } 
          if (k < j) {
            logChildren();
            throw new RuntimeException("detected invalid location");
          } 
          b++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  class AnchorInfo {
    int mCoordinate;
    
    boolean mLayoutFromEnd;
    
    int mPosition;
    
    boolean mValid;
    
    AnchorInfo() {
      reset();
    }
    
    void assignCoordinateFromPadding() {
      int i;
      if (this.mLayoutFromEnd) {
        i = LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding();
      } else {
        i = LinearLayoutManager.this.mOrientationHelper.getStartAfterPadding();
      } 
      this.mCoordinate = i;
    }
    
    public void assignFromView(View param1View) {
      if (this.mLayoutFromEnd) {
        this.mCoordinate = LinearLayoutManager.this.mOrientationHelper.getDecoratedEnd(param1View) + LinearLayoutManager.this.mOrientationHelper.getTotalSpaceChange();
      } else {
        this.mCoordinate = LinearLayoutManager.this.mOrientationHelper.getDecoratedStart(param1View);
      } 
      this.mPosition = LinearLayoutManager.this.getPosition(param1View);
    }
    
    public void assignFromViewAndKeepVisibleRect(View param1View) {
      int i = LinearLayoutManager.this.mOrientationHelper.getTotalSpaceChange();
      if (i >= 0) {
        assignFromView(param1View);
        return;
      } 
      this.mPosition = LinearLayoutManager.this.getPosition(param1View);
      if (this.mLayoutFromEnd) {
        int m = LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding() - i - LinearLayoutManager.this.mOrientationHelper.getDecoratedEnd(param1View);
        this.mCoordinate = LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding() - m;
        if (m > 0) {
          int n = LinearLayoutManager.this.mOrientationHelper.getDecoratedMeasurement(param1View);
          int i1 = this.mCoordinate;
          i = LinearLayoutManager.this.mOrientationHelper.getStartAfterPadding();
          i1 = i1 - n - i + Math.min(LinearLayoutManager.this.mOrientationHelper.getDecoratedStart(param1View) - i, 0);
          if (i1 < 0)
            this.mCoordinate += Math.min(m, -i1); 
        } 
        return;
      } 
      int k = LinearLayoutManager.this.mOrientationHelper.getDecoratedStart(param1View);
      int j = k - LinearLayoutManager.this.mOrientationHelper.getStartAfterPadding();
      this.mCoordinate = k;
      if (j > 0) {
        int m = LinearLayoutManager.this.mOrientationHelper.getDecoratedMeasurement(param1View);
        int n = LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding();
        int i1 = LinearLayoutManager.this.mOrientationHelper.getDecoratedEnd(param1View);
        n = LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding() - Math.min(0, n - i - i1) - k + m;
        if (n < 0)
          this.mCoordinate -= Math.min(j, -n); 
      } 
    }
    
    boolean isViewValidAsAnchor(View param1View, RecyclerView.State param1State) {
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param1View.getLayoutParams();
      return (!layoutParams.isItemRemoved() && layoutParams.getViewLayoutPosition() >= 0 && layoutParams.getViewLayoutPosition() < param1State.getItemCount());
    }
    
    void reset() {
      this.mPosition = -1;
      this.mCoordinate = Integer.MIN_VALUE;
      this.mLayoutFromEnd = false;
      this.mValid = false;
    }
    
    public String toString() {
      return "AnchorInfo{mPosition=" + this.mPosition + ", mCoordinate=" + this.mCoordinate + ", mLayoutFromEnd=" + this.mLayoutFromEnd + ", mValid=" + this.mValid + '}';
    }
  }
  
  protected static class LayoutChunkResult {
    public int mConsumed;
    
    public boolean mFinished;
    
    public boolean mFocusable;
    
    public boolean mIgnoreConsumed;
    
    void resetInternal() {
      this.mConsumed = 0;
      this.mFinished = false;
      this.mIgnoreConsumed = false;
      this.mFocusable = false;
    }
  }
  
  static class LayoutState {
    static final int INVALID_LAYOUT = -2147483648;
    
    static final int ITEM_DIRECTION_HEAD = -1;
    
    static final int ITEM_DIRECTION_TAIL = 1;
    
    static final int LAYOUT_END = 1;
    
    static final int LAYOUT_START = -1;
    
    static final int SCROLLING_OFFSET_NaN = -2147483648;
    
    static final String TAG = "LLM#LayoutState";
    
    int mAvailable;
    
    int mCurrentPosition;
    
    int mExtra = 0;
    
    boolean mInfinite;
    
    boolean mIsPreLayout = false;
    
    int mItemDirection;
    
    int mLastScrollDelta;
    
    int mLayoutDirection;
    
    int mOffset;
    
    boolean mRecycle = true;
    
    List<RecyclerView.ViewHolder> mScrapList = null;
    
    int mScrollingOffset;
    
    private View nextViewFromScrapList() {
      // Byte code:
      //   0: aload_0
      //   1: getfield mScrapList : Ljava/util/List;
      //   4: invokeinterface size : ()I
      //   9: istore_1
      //   10: iconst_0
      //   11: istore_2
      //   12: iload_2
      //   13: iload_1
      //   14: if_icmpge -> 76
      //   17: aload_0
      //   18: getfield mScrapList : Ljava/util/List;
      //   21: iload_2
      //   22: invokeinterface get : (I)Ljava/lang/Object;
      //   27: checkcast android/support/v7/widget/RecyclerView$ViewHolder
      //   30: getfield itemView : Landroid/view/View;
      //   33: astore_3
      //   34: aload_3
      //   35: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
      //   38: checkcast android/support/v7/widget/RecyclerView$LayoutParams
      //   41: astore #4
      //   43: aload #4
      //   45: invokevirtual isItemRemoved : ()Z
      //   48: ifeq -> 57
      //   51: iinc #2, 1
      //   54: goto -> 12
      //   57: aload_0
      //   58: getfield mCurrentPosition : I
      //   61: aload #4
      //   63: invokevirtual getViewLayoutPosition : ()I
      //   66: if_icmpne -> 51
      //   69: aload_0
      //   70: aload_3
      //   71: invokevirtual assignPositionFromScrapList : (Landroid/view/View;)V
      //   74: aload_3
      //   75: areturn
      //   76: aconst_null
      //   77: astore_3
      //   78: goto -> 74
    }
    
    public void assignPositionFromScrapList() {
      assignPositionFromScrapList(null);
    }
    
    public void assignPositionFromScrapList(View param1View) {
      param1View = nextViewInLimitedList(param1View);
      if (param1View == null) {
        this.mCurrentPosition = -1;
        return;
      } 
      this.mCurrentPosition = ((RecyclerView.LayoutParams)param1View.getLayoutParams()).getViewLayoutPosition();
    }
    
    boolean hasMore(RecyclerView.State param1State) {
      return (this.mCurrentPosition >= 0 && this.mCurrentPosition < param1State.getItemCount());
    }
    
    void log() {
      Log.d("LLM#LayoutState", "avail:" + this.mAvailable + ", ind:" + this.mCurrentPosition + ", dir:" + this.mItemDirection + ", offset:" + this.mOffset + ", layoutDir:" + this.mLayoutDirection);
    }
    
    View next(RecyclerView.Recycler param1Recycler) {
      if (this.mScrapList != null)
        return nextViewFromScrapList(); 
      View view = param1Recycler.getViewForPosition(this.mCurrentPosition);
      this.mCurrentPosition += this.mItemDirection;
      return view;
    }
    
    public View nextViewInLimitedList(View param1View) {
      View view2;
      int i = this.mScrapList.size();
      View view1 = null;
      int j = Integer.MAX_VALUE;
      byte b = 0;
      while (true) {
        view2 = view1;
        if (b < i) {
          View view = ((RecyclerView.ViewHolder)this.mScrapList.get(b)).itemView;
          RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
          view2 = view1;
          int k = j;
          if (view != param1View)
            if (layoutParams.isItemRemoved()) {
              k = j;
              view2 = view1;
            } else {
              int m = (layoutParams.getViewLayoutPosition() - this.mCurrentPosition) * this.mItemDirection;
              view2 = view1;
              k = j;
              if (m >= 0) {
                view2 = view1;
                k = j;
                if (m < j) {
                  view1 = view;
                  k = m;
                  view2 = view1;
                  if (m == 0) {
                    view2 = view1;
                    break;
                  } 
                } 
              } 
            }  
          b++;
          view1 = view2;
          j = k;
          continue;
        } 
        break;
      } 
      return view2;
    }
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static class SavedState implements Parcelable {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public LinearLayoutManager.SavedState createFromParcel(Parcel param2Parcel) {
          return new LinearLayoutManager.SavedState(param2Parcel);
        }
        
        public LinearLayoutManager.SavedState[] newArray(int param2Int) {
          return new LinearLayoutManager.SavedState[param2Int];
        }
      };
    
    boolean mAnchorLayoutFromEnd;
    
    int mAnchorOffset;
    
    int mAnchorPosition;
    
    public SavedState() {}
    
    SavedState(Parcel param1Parcel) {
      this.mAnchorPosition = param1Parcel.readInt();
      this.mAnchorOffset = param1Parcel.readInt();
      if (param1Parcel.readInt() != 1)
        bool = false; 
      this.mAnchorLayoutFromEnd = bool;
    }
    
    public SavedState(SavedState param1SavedState) {
      this.mAnchorPosition = param1SavedState.mAnchorPosition;
      this.mAnchorOffset = param1SavedState.mAnchorOffset;
      this.mAnchorLayoutFromEnd = param1SavedState.mAnchorLayoutFromEnd;
    }
    
    public int describeContents() {
      return 0;
    }
    
    boolean hasValidAnchor() {
      return (this.mAnchorPosition >= 0);
    }
    
    void invalidateAnchor() {
      this.mAnchorPosition = -1;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      param1Parcel.writeInt(this.mAnchorPosition);
      param1Parcel.writeInt(this.mAnchorOffset);
      if (this.mAnchorLayoutFromEnd) {
        param1Int = 1;
      } else {
        param1Int = 0;
      } 
      param1Parcel.writeInt(param1Int);
    }
  }
  
  static final class null implements Parcelable.Creator<SavedState> {
    public LinearLayoutManager.SavedState createFromParcel(Parcel param1Parcel) {
      return new LinearLayoutManager.SavedState(param1Parcel);
    }
    
    public LinearLayoutManager.SavedState[] newArray(int param1Int) {
      return new LinearLayoutManager.SavedState[param1Int];
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/LinearLayoutManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */