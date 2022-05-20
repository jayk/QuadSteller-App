package android.support.v7.widget;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public class StaggeredGridLayoutManager extends RecyclerView.LayoutManager implements RecyclerView.SmoothScroller.ScrollVectorProvider {
  static final boolean DEBUG = false;
  
  @Deprecated
  public static final int GAP_HANDLING_LAZY = 1;
  
  public static final int GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS = 2;
  
  public static final int GAP_HANDLING_NONE = 0;
  
  public static final int HORIZONTAL = 0;
  
  static final int INVALID_OFFSET = -2147483648;
  
  private static final float MAX_SCROLL_FACTOR = 0.33333334F;
  
  private static final String TAG = "StaggeredGridLayoutManager";
  
  public static final int VERTICAL = 1;
  
  private final AnchorInfo mAnchorInfo = new AnchorInfo();
  
  private final Runnable mCheckForGapsRunnable = new Runnable() {
      public void run() {
        StaggeredGridLayoutManager.this.checkForGaps();
      }
    };
  
  private int mFullSizeSpec;
  
  private int mGapStrategy = 2;
  
  private boolean mLaidOutInvalidFullSpan = false;
  
  private boolean mLastLayoutFromEnd;
  
  private boolean mLastLayoutRTL;
  
  @NonNull
  private final LayoutState mLayoutState;
  
  LazySpanLookup mLazySpanLookup = new LazySpanLookup();
  
  private int mOrientation;
  
  private SavedState mPendingSavedState;
  
  int mPendingScrollPosition = -1;
  
  int mPendingScrollPositionOffset = Integer.MIN_VALUE;
  
  private int[] mPrefetchDistances;
  
  @NonNull
  OrientationHelper mPrimaryOrientation;
  
  private BitSet mRemainingSpans;
  
  boolean mReverseLayout = false;
  
  @NonNull
  OrientationHelper mSecondaryOrientation;
  
  boolean mShouldReverseLayout = false;
  
  private int mSizePerSpan;
  
  private boolean mSmoothScrollbarEnabled = true;
  
  private int mSpanCount = -1;
  
  Span[] mSpans;
  
  private final Rect mTmpRect = new Rect();
  
  public StaggeredGridLayoutManager(int paramInt1, int paramInt2) {
    this.mOrientation = paramInt2;
    setSpanCount(paramInt1);
    if (this.mGapStrategy == 0)
      bool = false; 
    setAutoMeasureEnabled(bool);
    this.mLayoutState = new LayoutState();
    createOrientationHelpers();
  }
  
  public StaggeredGridLayoutManager(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    RecyclerView.LayoutManager.Properties properties = getProperties(paramContext, paramAttributeSet, paramInt1, paramInt2);
    setOrientation(properties.orientation);
    setSpanCount(properties.spanCount);
    setReverseLayout(properties.reverseLayout);
    if (this.mGapStrategy == 0)
      bool = false; 
    setAutoMeasureEnabled(bool);
    this.mLayoutState = new LayoutState();
    createOrientationHelpers();
  }
  
  private void appendViewToAllSpans(View paramView) {
    for (int i = this.mSpanCount - 1; i >= 0; i--)
      this.mSpans[i].appendToSpan(paramView); 
  }
  
  private void applyPendingSavedState(AnchorInfo paramAnchorInfo) {
    if (this.mPendingSavedState.mSpanOffsetsSize > 0)
      if (this.mPendingSavedState.mSpanOffsetsSize == this.mSpanCount) {
        for (byte b = 0; b < this.mSpanCount; b++) {
          this.mSpans[b].clear();
          int i = this.mPendingSavedState.mSpanOffsets[b];
          int j = i;
          if (i != Integer.MIN_VALUE)
            if (this.mPendingSavedState.mAnchorLayoutFromEnd) {
              j = i + this.mPrimaryOrientation.getEndAfterPadding();
            } else {
              j = i + this.mPrimaryOrientation.getStartAfterPadding();
            }  
          this.mSpans[b].setLine(j);
        } 
      } else {
        this.mPendingSavedState.invalidateSpanInfo();
        this.mPendingSavedState.mAnchorPosition = this.mPendingSavedState.mVisibleAnchorPosition;
      }  
    this.mLastLayoutRTL = this.mPendingSavedState.mLastLayoutRTL;
    setReverseLayout(this.mPendingSavedState.mReverseLayout);
    resolveShouldLayoutReverse();
    if (this.mPendingSavedState.mAnchorPosition != -1) {
      this.mPendingScrollPosition = this.mPendingSavedState.mAnchorPosition;
      paramAnchorInfo.mLayoutFromEnd = this.mPendingSavedState.mAnchorLayoutFromEnd;
    } else {
      paramAnchorInfo.mLayoutFromEnd = this.mShouldReverseLayout;
    } 
    if (this.mPendingSavedState.mSpanLookupSize > 1) {
      this.mLazySpanLookup.mData = this.mPendingSavedState.mSpanLookup;
      this.mLazySpanLookup.mFullSpanItems = this.mPendingSavedState.mFullSpanItems;
    } 
  }
  
  private void attachViewToSpans(View paramView, LayoutParams paramLayoutParams, LayoutState paramLayoutState) {
    if (paramLayoutState.mLayoutDirection == 1) {
      if (paramLayoutParams.mFullSpan) {
        appendViewToAllSpans(paramView);
        return;
      } 
      paramLayoutParams.mSpan.appendToSpan(paramView);
      return;
    } 
    if (paramLayoutParams.mFullSpan) {
      prependViewToAllSpans(paramView);
      return;
    } 
    paramLayoutParams.mSpan.prependToSpan(paramView);
  }
  
  private int calculateScrollDirectionForPosition(int paramInt) {
    boolean bool1;
    byte b = -1;
    boolean bool = true;
    if (getChildCount() == 0)
      return this.mShouldReverseLayout ? bool : -1; 
    if (paramInt < getFirstChildPosition()) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    return (bool1 != this.mShouldReverseLayout) ? b : 1;
  }
  
  private boolean checkSpanForGap(Span paramSpan) {
    null = true;
    if (this.mShouldReverseLayout) {
      if (paramSpan.getEndLine() < this.mPrimaryOrientation.getEndAfterPadding()) {
        if ((paramSpan.getLayoutParams((View)paramSpan.mViews.get(paramSpan.mViews.size() - 1))).mFullSpan)
          null = false; 
        return null;
      } 
    } else if (paramSpan.getStartLine() > this.mPrimaryOrientation.getStartAfterPadding()) {
      if ((paramSpan.getLayoutParams((View)paramSpan.mViews.get(0))).mFullSpan)
        null = false; 
      return null;
    } 
    return false;
  }
  
  private int computeScrollExtent(RecyclerView.State paramState) {
    boolean bool = true;
    int i = 0;
    if (getChildCount() != 0) {
      boolean bool1;
      OrientationHelper orientationHelper = this.mPrimaryOrientation;
      if (!this.mSmoothScrollbarEnabled) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      View view = findFirstVisibleItemClosestToStart(bool1);
      if (!this.mSmoothScrollbarEnabled) {
        bool1 = bool;
      } else {
        bool1 = false;
      } 
      i = ScrollbarHelper.computeScrollExtent(paramState, orientationHelper, view, findFirstVisibleItemClosestToEnd(bool1), this, this.mSmoothScrollbarEnabled);
    } 
    return i;
  }
  
  private int computeScrollOffset(RecyclerView.State paramState) {
    boolean bool = true;
    int i = 0;
    if (getChildCount() != 0) {
      boolean bool1;
      OrientationHelper orientationHelper = this.mPrimaryOrientation;
      if (!this.mSmoothScrollbarEnabled) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      View view = findFirstVisibleItemClosestToStart(bool1);
      if (!this.mSmoothScrollbarEnabled) {
        bool1 = bool;
      } else {
        bool1 = false;
      } 
      i = ScrollbarHelper.computeScrollOffset(paramState, orientationHelper, view, findFirstVisibleItemClosestToEnd(bool1), this, this.mSmoothScrollbarEnabled, this.mShouldReverseLayout);
    } 
    return i;
  }
  
  private int computeScrollRange(RecyclerView.State paramState) {
    boolean bool = true;
    int i = 0;
    if (getChildCount() != 0) {
      boolean bool1;
      OrientationHelper orientationHelper = this.mPrimaryOrientation;
      if (!this.mSmoothScrollbarEnabled) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      View view = findFirstVisibleItemClosestToStart(bool1);
      if (!this.mSmoothScrollbarEnabled) {
        bool1 = bool;
      } else {
        bool1 = false;
      } 
      i = ScrollbarHelper.computeScrollRange(paramState, orientationHelper, view, findFirstVisibleItemClosestToEnd(bool1), this, this.mSmoothScrollbarEnabled);
    } 
    return i;
  }
  
  private int convertFocusDirectionToLayoutDirection(int paramInt) {
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
  
  private LazySpanLookup.FullSpanItem createFullSpanItemFromEnd(int paramInt) {
    LazySpanLookup.FullSpanItem fullSpanItem = new LazySpanLookup.FullSpanItem();
    fullSpanItem.mGapPerSpan = new int[this.mSpanCount];
    for (byte b = 0; b < this.mSpanCount; b++)
      fullSpanItem.mGapPerSpan[b] = paramInt - this.mSpans[b].getEndLine(paramInt); 
    return fullSpanItem;
  }
  
  private LazySpanLookup.FullSpanItem createFullSpanItemFromStart(int paramInt) {
    LazySpanLookup.FullSpanItem fullSpanItem = new LazySpanLookup.FullSpanItem();
    fullSpanItem.mGapPerSpan = new int[this.mSpanCount];
    for (byte b = 0; b < this.mSpanCount; b++)
      fullSpanItem.mGapPerSpan[b] = this.mSpans[b].getStartLine(paramInt) - paramInt; 
    return fullSpanItem;
  }
  
  private void createOrientationHelpers() {
    this.mPrimaryOrientation = OrientationHelper.createOrientationHelper(this, this.mOrientation);
    this.mSecondaryOrientation = OrientationHelper.createOrientationHelper(this, 1 - this.mOrientation);
  }
  
  private int fill(RecyclerView.Recycler paramRecycler, LayoutState paramLayoutState, RecyclerView.State paramState) {
    int i;
    this.mRemainingSpans.set(0, this.mSpanCount, true);
    if (this.mLayoutState.mInfinite) {
      if (paramLayoutState.mLayoutDirection == 1) {
        null = Integer.MAX_VALUE;
      } else {
        null = Integer.MIN_VALUE;
      } 
    } else if (paramLayoutState.mLayoutDirection == 1) {
      null = paramLayoutState.mEndLine + paramLayoutState.mAvailable;
    } else {
      null = paramLayoutState.mStartLine - paramLayoutState.mAvailable;
    } 
    updateAllRemainingSpans(paramLayoutState.mLayoutDirection, null);
    if (this.mShouldReverseLayout) {
      i = this.mPrimaryOrientation.getEndAfterPadding();
    } else {
      i = this.mPrimaryOrientation.getStartAfterPadding();
    } 
    int j;
    for (j = 0; paramLayoutState.hasMore(paramState) && (this.mLayoutState.mInfinite || !this.mRemainingSpans.isEmpty()); j = 1) {
      int m;
      Span span;
      int n;
      int i1;
      View view = paramLayoutState.next(paramRecycler);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      int k = layoutParams.getViewLayoutPosition();
      j = this.mLazySpanLookup.getSpan(k);
      if (j == -1) {
        m = 1;
      } else {
        m = 0;
      } 
      if (m) {
        if (layoutParams.mFullSpan) {
          span = this.mSpans[0];
        } else {
          span = getNextSpan(paramLayoutState);
        } 
        this.mLazySpanLookup.setSpan(k, span);
      } else {
        span = this.mSpans[j];
      } 
      layoutParams.mSpan = span;
      if (paramLayoutState.mLayoutDirection == 1) {
        addView(view);
      } else {
        addView(view, 0);
      } 
      measureChildWithDecorationsAndMargin(view, layoutParams, false);
      if (paramLayoutState.mLayoutDirection == 1) {
        if (layoutParams.mFullSpan) {
          j = getMaxEnd(i);
        } else {
          j = span.getEndLine(i);
        } 
        int i2 = j + this.mPrimaryOrientation.getDecoratedMeasurement(view);
        n = j;
        i1 = i2;
        if (m) {
          n = j;
          i1 = i2;
          if (layoutParams.mFullSpan) {
            LazySpanLookup.FullSpanItem fullSpanItem = createFullSpanItemFromEnd(j);
            fullSpanItem.mGapDir = -1;
            fullSpanItem.mPosition = k;
            this.mLazySpanLookup.addFullSpanItem(fullSpanItem);
            i1 = i2;
            n = j;
          } 
        } 
      } else {
        if (layoutParams.mFullSpan) {
          j = getMinStart(i);
        } else {
          j = span.getStartLine(i);
        } 
        int i2 = j - this.mPrimaryOrientation.getDecoratedMeasurement(view);
        n = i2;
        i1 = j;
        if (m) {
          n = i2;
          i1 = j;
          if (layoutParams.mFullSpan) {
            LazySpanLookup.FullSpanItem fullSpanItem = createFullSpanItemFromStart(j);
            fullSpanItem.mGapDir = 1;
            fullSpanItem.mPosition = k;
            this.mLazySpanLookup.addFullSpanItem(fullSpanItem);
            n = i2;
            i1 = j;
          } 
        } 
      } 
      if (layoutParams.mFullSpan && paramLayoutState.mItemDirection == -1)
        if (m) {
          this.mLaidOutInvalidFullSpan = true;
        } else {
          if (paramLayoutState.mLayoutDirection == 1) {
            if (!areAllEndsEqual()) {
              j = 1;
            } else {
              j = 0;
            } 
          } else if (!areAllStartsEqual()) {
            j = 1;
          } else {
            j = 0;
          } 
          if (j != 0) {
            LazySpanLookup.FullSpanItem fullSpanItem = this.mLazySpanLookup.getFullSpanItem(k);
            if (fullSpanItem != null)
              fullSpanItem.mHasUnwantedGapAfter = true; 
            this.mLaidOutInvalidFullSpan = true;
          } 
        }  
      attachViewToSpans(view, layoutParams, paramLayoutState);
      if (isLayoutRTL() && this.mOrientation == 1) {
        if (layoutParams.mFullSpan) {
          j = this.mSecondaryOrientation.getEndAfterPadding();
        } else {
          j = this.mSecondaryOrientation.getEndAfterPadding() - (this.mSpanCount - 1 - span.mIndex) * this.mSizePerSpan;
        } 
        int i2 = j - this.mSecondaryOrientation.getDecoratedMeasurement(view);
        m = j;
        j = i2;
      } else {
        if (layoutParams.mFullSpan) {
          j = this.mSecondaryOrientation.getStartAfterPadding();
        } else {
          j = span.mIndex * this.mSizePerSpan + this.mSecondaryOrientation.getStartAfterPadding();
        } 
        m = j + this.mSecondaryOrientation.getDecoratedMeasurement(view);
      } 
      if (this.mOrientation == 1) {
        layoutDecoratedWithMargins(view, j, n, m, i1);
      } else {
        layoutDecoratedWithMargins(view, n, j, i1, m);
      } 
      if (layoutParams.mFullSpan) {
        updateAllRemainingSpans(this.mLayoutState.mLayoutDirection, null);
      } else {
        updateRemainingSpans(span, this.mLayoutState.mLayoutDirection, null);
      } 
      recycle(paramRecycler, this.mLayoutState);
      if (this.mLayoutState.mStopInFocusable && view.isFocusable())
        if (layoutParams.mFullSpan) {
          this.mRemainingSpans.clear();
        } else {
          this.mRemainingSpans.set(span.mIndex, false);
        }  
    } 
    if (j == 0)
      recycle(paramRecycler, this.mLayoutState); 
    if (this.mLayoutState.mLayoutDirection == -1) {
      null = getMinStart(this.mPrimaryOrientation.getStartAfterPadding());
      null = this.mPrimaryOrientation.getStartAfterPadding() - null;
    } else {
      null = getMaxEnd(this.mPrimaryOrientation.getEndAfterPadding()) - this.mPrimaryOrientation.getEndAfterPadding();
    } 
    return (null > 0) ? Math.min(paramLayoutState.mAvailable, null) : 0;
  }
  
  private int findFirstReferenceChildPosition(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getChildCount : ()I
    //   4: istore_2
    //   5: iconst_0
    //   6: istore_3
    //   7: iload_3
    //   8: iload_2
    //   9: if_icmpge -> 45
    //   12: aload_0
    //   13: aload_0
    //   14: iload_3
    //   15: invokevirtual getChildAt : (I)Landroid/view/View;
    //   18: invokevirtual getPosition : (Landroid/view/View;)I
    //   21: istore #4
    //   23: iload #4
    //   25: iflt -> 39
    //   28: iload #4
    //   30: iload_1
    //   31: if_icmpge -> 39
    //   34: iload #4
    //   36: istore_1
    //   37: iload_1
    //   38: ireturn
    //   39: iinc #3, 1
    //   42: goto -> 7
    //   45: iconst_0
    //   46: istore_1
    //   47: goto -> 37
  }
  
  private int findLastReferenceChildPosition(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getChildCount : ()I
    //   4: iconst_1
    //   5: isub
    //   6: istore_2
    //   7: iload_2
    //   8: iflt -> 40
    //   11: aload_0
    //   12: aload_0
    //   13: iload_2
    //   14: invokevirtual getChildAt : (I)Landroid/view/View;
    //   17: invokevirtual getPosition : (Landroid/view/View;)I
    //   20: istore_3
    //   21: iload_3
    //   22: iflt -> 34
    //   25: iload_3
    //   26: iload_1
    //   27: if_icmpge -> 34
    //   30: iload_3
    //   31: istore_1
    //   32: iload_1
    //   33: ireturn
    //   34: iinc #2, -1
    //   37: goto -> 7
    //   40: iconst_0
    //   41: istore_1
    //   42: goto -> 32
  }
  
  private void fixEndGap(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, boolean paramBoolean) {
    int i = getMaxEnd(-2147483648);
    if (i != Integer.MIN_VALUE) {
      i = this.mPrimaryOrientation.getEndAfterPadding() - i;
      if (i > 0) {
        i -= -scrollBy(-i, paramRecycler, paramState);
        if (paramBoolean && i > 0)
          this.mPrimaryOrientation.offsetChildren(i); 
      } 
    } 
  }
  
  private void fixStartGap(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, boolean paramBoolean) {
    int i = getMinStart(2147483647);
    if (i != Integer.MAX_VALUE) {
      i -= this.mPrimaryOrientation.getStartAfterPadding();
      if (i > 0) {
        i -= scrollBy(i, paramRecycler, paramState);
        if (paramBoolean && i > 0)
          this.mPrimaryOrientation.offsetChildren(-i); 
      } 
    } 
  }
  
  private int getMaxEnd(int paramInt) {
    int i = this.mSpans[0].getEndLine(paramInt);
    byte b = 1;
    while (b < this.mSpanCount) {
      int j = this.mSpans[b].getEndLine(paramInt);
      int k = i;
      if (j > i)
        k = j; 
      b++;
      i = k;
    } 
    return i;
  }
  
  private int getMaxStart(int paramInt) {
    int i = this.mSpans[0].getStartLine(paramInt);
    byte b = 1;
    while (b < this.mSpanCount) {
      int j = this.mSpans[b].getStartLine(paramInt);
      int k = i;
      if (j > i)
        k = j; 
      b++;
      i = k;
    } 
    return i;
  }
  
  private int getMinEnd(int paramInt) {
    int i = this.mSpans[0].getEndLine(paramInt);
    byte b = 1;
    while (b < this.mSpanCount) {
      int j = this.mSpans[b].getEndLine(paramInt);
      int k = i;
      if (j < i)
        k = j; 
      b++;
      i = k;
    } 
    return i;
  }
  
  private int getMinStart(int paramInt) {
    int i = this.mSpans[0].getStartLine(paramInt);
    byte b = 1;
    while (b < this.mSpanCount) {
      int j = this.mSpans[b].getStartLine(paramInt);
      int k = i;
      if (j < i)
        k = j; 
      b++;
      i = k;
    } 
    return i;
  }
  
  private Span getNextSpan(LayoutState paramLayoutState) {
    int i;
    int j;
    byte b;
    Span span;
    if (preferLastSpan(paramLayoutState.mLayoutDirection)) {
      i = this.mSpanCount - 1;
      j = -1;
      b = -1;
    } else {
      i = 0;
      j = this.mSpanCount;
      b = 1;
    } 
    if (paramLayoutState.mLayoutDirection == 1) {
      paramLayoutState = null;
      int k = Integer.MAX_VALUE;
      int m = this.mPrimaryOrientation.getStartAfterPadding();
      while (true) {
        LayoutState layoutState = paramLayoutState;
        if (i != j) {
          span = this.mSpans[i];
          int n = span.getEndLine(m);
          int i1 = k;
          if (n < k) {
            Span span1 = span;
            i1 = n;
          } 
          i += b;
          k = i1;
          continue;
        } 
        break;
      } 
    } else {
      Span span1;
      paramLayoutState = null;
      int n = Integer.MIN_VALUE;
      int m = this.mPrimaryOrientation.getEndAfterPadding();
      int k = i;
      for (i = n; k != j; i = n) {
        Span span2 = this.mSpans[k];
        int i1 = span2.getStartLine(m);
        n = i;
        if (i1 > i) {
          span1 = span2;
          n = i1;
        } 
        k += b;
      } 
      span = span1;
    } 
    return span;
  }
  
  private void handleUpdate(int paramInt1, int paramInt2, int paramInt3) {
    int i;
    int j;
    int k;
    if (this.mShouldReverseLayout) {
      i = getLastChildPosition();
    } else {
      i = getFirstChildPosition();
    } 
    if (paramInt3 == 8) {
      if (paramInt1 < paramInt2) {
        j = paramInt2 + 1;
        k = paramInt1;
      } else {
        j = paramInt1 + 1;
        k = paramInt2;
      } 
    } else {
      k = paramInt1;
      j = paramInt1 + paramInt2;
    } 
    this.mLazySpanLookup.invalidateAfter(k);
    switch (paramInt3) {
      default:
        if (j <= i)
          return; 
        break;
      case 1:
        this.mLazySpanLookup.offsetForAddition(paramInt1, paramInt2);
      case 2:
        this.mLazySpanLookup.offsetForRemoval(paramInt1, paramInt2);
      case 8:
        this.mLazySpanLookup.offsetForRemoval(paramInt1, 1);
        this.mLazySpanLookup.offsetForAddition(paramInt2, 1);
    } 
    if (this.mShouldReverseLayout) {
      paramInt1 = getFirstChildPosition();
    } else {
      paramInt1 = getLastChildPosition();
    } 
    if (k <= paramInt1)
      requestLayout(); 
  }
  
  private void measureChildWithDecorationsAndMargin(View paramView, int paramInt1, int paramInt2, boolean paramBoolean) {
    calculateItemDecorationsForChild(paramView, this.mTmpRect);
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    paramInt1 = updateSpecWithExtra(paramInt1, layoutParams.leftMargin + this.mTmpRect.left, layoutParams.rightMargin + this.mTmpRect.right);
    paramInt2 = updateSpecWithExtra(paramInt2, layoutParams.topMargin + this.mTmpRect.top, layoutParams.bottomMargin + this.mTmpRect.bottom);
    if (paramBoolean) {
      paramBoolean = shouldReMeasureChild(paramView, paramInt1, paramInt2, layoutParams);
    } else {
      paramBoolean = shouldMeasureChild(paramView, paramInt1, paramInt2, layoutParams);
    } 
    if (paramBoolean)
      paramView.measure(paramInt1, paramInt2); 
  }
  
  private void measureChildWithDecorationsAndMargin(View paramView, LayoutParams paramLayoutParams, boolean paramBoolean) {
    if (paramLayoutParams.mFullSpan) {
      if (this.mOrientation == 1) {
        measureChildWithDecorationsAndMargin(paramView, this.mFullSizeSpec, getChildMeasureSpec(getHeight(), getHeightMode(), 0, paramLayoutParams.height, true), paramBoolean);
        return;
      } 
      measureChildWithDecorationsAndMargin(paramView, getChildMeasureSpec(getWidth(), getWidthMode(), 0, paramLayoutParams.width, true), this.mFullSizeSpec, paramBoolean);
      return;
    } 
    if (this.mOrientation == 1) {
      measureChildWithDecorationsAndMargin(paramView, getChildMeasureSpec(this.mSizePerSpan, getWidthMode(), 0, paramLayoutParams.width, false), getChildMeasureSpec(getHeight(), getHeightMode(), 0, paramLayoutParams.height, true), paramBoolean);
      return;
    } 
    measureChildWithDecorationsAndMargin(paramView, getChildMeasureSpec(getWidth(), getWidthMode(), 0, paramLayoutParams.width, true), getChildMeasureSpec(this.mSizePerSpan, getHeightMode(), 0, paramLayoutParams.height, false), paramBoolean);
  }
  
  private void onLayoutChildren(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, boolean paramBoolean) {
    // Byte code:
    //   0: iconst_1
    //   1: istore #4
    //   3: aload_0
    //   4: getfield mAnchorInfo : Landroid/support/v7/widget/StaggeredGridLayoutManager$AnchorInfo;
    //   7: astore #5
    //   9: aload_0
    //   10: getfield mPendingSavedState : Landroid/support/v7/widget/StaggeredGridLayoutManager$SavedState;
    //   13: ifnonnull -> 24
    //   16: aload_0
    //   17: getfield mPendingScrollPosition : I
    //   20: iconst_m1
    //   21: if_icmpeq -> 42
    //   24: aload_2
    //   25: invokevirtual getItemCount : ()I
    //   28: ifne -> 42
    //   31: aload_0
    //   32: aload_1
    //   33: invokevirtual removeAndRecycleAllViews : (Landroid/support/v7/widget/RecyclerView$Recycler;)V
    //   36: aload #5
    //   38: invokevirtual reset : ()V
    //   41: return
    //   42: aload #5
    //   44: getfield mValid : Z
    //   47: ifeq -> 65
    //   50: aload_0
    //   51: getfield mPendingScrollPosition : I
    //   54: iconst_m1
    //   55: if_icmpne -> 65
    //   58: aload_0
    //   59: getfield mPendingSavedState : Landroid/support/v7/widget/StaggeredGridLayoutManager$SavedState;
    //   62: ifnull -> 241
    //   65: iconst_1
    //   66: istore #6
    //   68: iload #6
    //   70: ifeq -> 104
    //   73: aload #5
    //   75: invokevirtual reset : ()V
    //   78: aload_0
    //   79: getfield mPendingSavedState : Landroid/support/v7/widget/StaggeredGridLayoutManager$SavedState;
    //   82: ifnull -> 247
    //   85: aload_0
    //   86: aload #5
    //   88: invokespecial applyPendingSavedState : (Landroid/support/v7/widget/StaggeredGridLayoutManager$AnchorInfo;)V
    //   91: aload_0
    //   92: aload_2
    //   93: aload #5
    //   95: invokevirtual updateAnchorInfoForLayout : (Landroid/support/v7/widget/RecyclerView$State;Landroid/support/v7/widget/StaggeredGridLayoutManager$AnchorInfo;)V
    //   98: aload #5
    //   100: iconst_1
    //   101: putfield mValid : Z
    //   104: aload_0
    //   105: getfield mPendingSavedState : Landroid/support/v7/widget/StaggeredGridLayoutManager$SavedState;
    //   108: ifnonnull -> 155
    //   111: aload_0
    //   112: getfield mPendingScrollPosition : I
    //   115: iconst_m1
    //   116: if_icmpne -> 155
    //   119: aload #5
    //   121: getfield mLayoutFromEnd : Z
    //   124: aload_0
    //   125: getfield mLastLayoutFromEnd : Z
    //   128: if_icmpne -> 142
    //   131: aload_0
    //   132: invokevirtual isLayoutRTL : ()Z
    //   135: aload_0
    //   136: getfield mLastLayoutRTL : Z
    //   139: if_icmpeq -> 155
    //   142: aload_0
    //   143: getfield mLazySpanLookup : Landroid/support/v7/widget/StaggeredGridLayoutManager$LazySpanLookup;
    //   146: invokevirtual clear : ()V
    //   149: aload #5
    //   151: iconst_1
    //   152: putfield mInvalidateOffsets : Z
    //   155: aload_0
    //   156: invokevirtual getChildCount : ()I
    //   159: ifle -> 326
    //   162: aload_0
    //   163: getfield mPendingSavedState : Landroid/support/v7/widget/StaggeredGridLayoutManager$SavedState;
    //   166: ifnull -> 180
    //   169: aload_0
    //   170: getfield mPendingSavedState : Landroid/support/v7/widget/StaggeredGridLayoutManager$SavedState;
    //   173: getfield mSpanOffsetsSize : I
    //   176: iconst_1
    //   177: if_icmpge -> 326
    //   180: aload #5
    //   182: getfield mInvalidateOffsets : Z
    //   185: ifeq -> 263
    //   188: iconst_0
    //   189: istore #6
    //   191: iload #6
    //   193: aload_0
    //   194: getfield mSpanCount : I
    //   197: if_icmpge -> 326
    //   200: aload_0
    //   201: getfield mSpans : [Landroid/support/v7/widget/StaggeredGridLayoutManager$Span;
    //   204: iload #6
    //   206: aaload
    //   207: invokevirtual clear : ()V
    //   210: aload #5
    //   212: getfield mOffset : I
    //   215: ldc -2147483648
    //   217: if_icmpeq -> 235
    //   220: aload_0
    //   221: getfield mSpans : [Landroid/support/v7/widget/StaggeredGridLayoutManager$Span;
    //   224: iload #6
    //   226: aaload
    //   227: aload #5
    //   229: getfield mOffset : I
    //   232: invokevirtual setLine : (I)V
    //   235: iinc #6, 1
    //   238: goto -> 191
    //   241: iconst_0
    //   242: istore #6
    //   244: goto -> 68
    //   247: aload_0
    //   248: invokespecial resolveShouldLayoutReverse : ()V
    //   251: aload #5
    //   253: aload_0
    //   254: getfield mShouldReverseLayout : Z
    //   257: putfield mLayoutFromEnd : Z
    //   260: goto -> 91
    //   263: iload #6
    //   265: ifne -> 278
    //   268: aload_0
    //   269: getfield mAnchorInfo : Landroid/support/v7/widget/StaggeredGridLayoutManager$AnchorInfo;
    //   272: getfield mSpanReferenceLines : [I
    //   275: ifnonnull -> 600
    //   278: iconst_0
    //   279: istore #6
    //   281: iload #6
    //   283: aload_0
    //   284: getfield mSpanCount : I
    //   287: if_icmpge -> 315
    //   290: aload_0
    //   291: getfield mSpans : [Landroid/support/v7/widget/StaggeredGridLayoutManager$Span;
    //   294: iload #6
    //   296: aaload
    //   297: aload_0
    //   298: getfield mShouldReverseLayout : Z
    //   301: aload #5
    //   303: getfield mOffset : I
    //   306: invokevirtual cacheReferenceLineAndClear : (ZI)V
    //   309: iinc #6, 1
    //   312: goto -> 281
    //   315: aload_0
    //   316: getfield mAnchorInfo : Landroid/support/v7/widget/StaggeredGridLayoutManager$AnchorInfo;
    //   319: aload_0
    //   320: getfield mSpans : [Landroid/support/v7/widget/StaggeredGridLayoutManager$Span;
    //   323: invokevirtual saveSpanReferenceLines : ([Landroid/support/v7/widget/StaggeredGridLayoutManager$Span;)V
    //   326: aload_0
    //   327: aload_1
    //   328: invokevirtual detachAndScrapAttachedViews : (Landroid/support/v7/widget/RecyclerView$Recycler;)V
    //   331: aload_0
    //   332: getfield mLayoutState : Landroid/support/v7/widget/LayoutState;
    //   335: iconst_0
    //   336: putfield mRecycle : Z
    //   339: aload_0
    //   340: iconst_0
    //   341: putfield mLaidOutInvalidFullSpan : Z
    //   344: aload_0
    //   345: aload_0
    //   346: getfield mSecondaryOrientation : Landroid/support/v7/widget/OrientationHelper;
    //   349: invokevirtual getTotalSpace : ()I
    //   352: invokevirtual updateMeasureSpecs : (I)V
    //   355: aload_0
    //   356: aload #5
    //   358: getfield mPosition : I
    //   361: aload_2
    //   362: invokespecial updateLayoutState : (ILandroid/support/v7/widget/RecyclerView$State;)V
    //   365: aload #5
    //   367: getfield mLayoutFromEnd : Z
    //   370: ifeq -> 647
    //   373: aload_0
    //   374: iconst_m1
    //   375: invokespecial setLayoutStateDirection : (I)V
    //   378: aload_0
    //   379: aload_1
    //   380: aload_0
    //   381: getfield mLayoutState : Landroid/support/v7/widget/LayoutState;
    //   384: aload_2
    //   385: invokespecial fill : (Landroid/support/v7/widget/RecyclerView$Recycler;Landroid/support/v7/widget/LayoutState;Landroid/support/v7/widget/RecyclerView$State;)I
    //   388: pop
    //   389: aload_0
    //   390: iconst_1
    //   391: invokespecial setLayoutStateDirection : (I)V
    //   394: aload_0
    //   395: getfield mLayoutState : Landroid/support/v7/widget/LayoutState;
    //   398: aload #5
    //   400: getfield mPosition : I
    //   403: aload_0
    //   404: getfield mLayoutState : Landroid/support/v7/widget/LayoutState;
    //   407: getfield mItemDirection : I
    //   410: iadd
    //   411: putfield mCurrentPosition : I
    //   414: aload_0
    //   415: aload_1
    //   416: aload_0
    //   417: getfield mLayoutState : Landroid/support/v7/widget/LayoutState;
    //   420: aload_2
    //   421: invokespecial fill : (Landroid/support/v7/widget/RecyclerView$Recycler;Landroid/support/v7/widget/LayoutState;Landroid/support/v7/widget/RecyclerView$State;)I
    //   424: pop
    //   425: aload_0
    //   426: invokespecial repositionToWrapContentIfNecessary : ()V
    //   429: aload_0
    //   430: invokevirtual getChildCount : ()I
    //   433: ifle -> 457
    //   436: aload_0
    //   437: getfield mShouldReverseLayout : Z
    //   440: ifeq -> 702
    //   443: aload_0
    //   444: aload_1
    //   445: aload_2
    //   446: iconst_1
    //   447: invokespecial fixEndGap : (Landroid/support/v7/widget/RecyclerView$Recycler;Landroid/support/v7/widget/RecyclerView$State;Z)V
    //   450: aload_0
    //   451: aload_1
    //   452: aload_2
    //   453: iconst_0
    //   454: invokespecial fixStartGap : (Landroid/support/v7/widget/RecyclerView$Recycler;Landroid/support/v7/widget/RecyclerView$State;Z)V
    //   457: iconst_0
    //   458: istore #7
    //   460: iload #7
    //   462: istore #6
    //   464: iload_3
    //   465: ifeq -> 547
    //   468: iload #7
    //   470: istore #6
    //   472: aload_2
    //   473: invokevirtual isPreLayout : ()Z
    //   476: ifne -> 547
    //   479: aload_0
    //   480: getfield mGapStrategy : I
    //   483: ifeq -> 719
    //   486: aload_0
    //   487: invokevirtual getChildCount : ()I
    //   490: ifle -> 719
    //   493: iload #4
    //   495: istore #8
    //   497: aload_0
    //   498: getfield mLaidOutInvalidFullSpan : Z
    //   501: ifne -> 515
    //   504: aload_0
    //   505: invokevirtual hasGapsToFix : ()Landroid/view/View;
    //   508: ifnull -> 719
    //   511: iload #4
    //   513: istore #8
    //   515: iload #7
    //   517: istore #6
    //   519: iload #8
    //   521: ifeq -> 547
    //   524: aload_0
    //   525: aload_0
    //   526: getfield mCheckForGapsRunnable : Ljava/lang/Runnable;
    //   529: invokevirtual removeCallbacks : (Ljava/lang/Runnable;)Z
    //   532: pop
    //   533: iload #7
    //   535: istore #6
    //   537: aload_0
    //   538: invokevirtual checkForGaps : ()Z
    //   541: ifeq -> 547
    //   544: iconst_1
    //   545: istore #6
    //   547: aload_2
    //   548: invokevirtual isPreLayout : ()Z
    //   551: ifeq -> 561
    //   554: aload_0
    //   555: getfield mAnchorInfo : Landroid/support/v7/widget/StaggeredGridLayoutManager$AnchorInfo;
    //   558: invokevirtual reset : ()V
    //   561: aload_0
    //   562: aload #5
    //   564: getfield mLayoutFromEnd : Z
    //   567: putfield mLastLayoutFromEnd : Z
    //   570: aload_0
    //   571: aload_0
    //   572: invokevirtual isLayoutRTL : ()Z
    //   575: putfield mLastLayoutRTL : Z
    //   578: iload #6
    //   580: ifeq -> 41
    //   583: aload_0
    //   584: getfield mAnchorInfo : Landroid/support/v7/widget/StaggeredGridLayoutManager$AnchorInfo;
    //   587: invokevirtual reset : ()V
    //   590: aload_0
    //   591: aload_1
    //   592: aload_2
    //   593: iconst_0
    //   594: invokespecial onLayoutChildren : (Landroid/support/v7/widget/RecyclerView$Recycler;Landroid/support/v7/widget/RecyclerView$State;Z)V
    //   597: goto -> 41
    //   600: iconst_0
    //   601: istore #6
    //   603: iload #6
    //   605: aload_0
    //   606: getfield mSpanCount : I
    //   609: if_icmpge -> 326
    //   612: aload_0
    //   613: getfield mSpans : [Landroid/support/v7/widget/StaggeredGridLayoutManager$Span;
    //   616: iload #6
    //   618: aaload
    //   619: astore #9
    //   621: aload #9
    //   623: invokevirtual clear : ()V
    //   626: aload #9
    //   628: aload_0
    //   629: getfield mAnchorInfo : Landroid/support/v7/widget/StaggeredGridLayoutManager$AnchorInfo;
    //   632: getfield mSpanReferenceLines : [I
    //   635: iload #6
    //   637: iaload
    //   638: invokevirtual setLine : (I)V
    //   641: iinc #6, 1
    //   644: goto -> 603
    //   647: aload_0
    //   648: iconst_1
    //   649: invokespecial setLayoutStateDirection : (I)V
    //   652: aload_0
    //   653: aload_1
    //   654: aload_0
    //   655: getfield mLayoutState : Landroid/support/v7/widget/LayoutState;
    //   658: aload_2
    //   659: invokespecial fill : (Landroid/support/v7/widget/RecyclerView$Recycler;Landroid/support/v7/widget/LayoutState;Landroid/support/v7/widget/RecyclerView$State;)I
    //   662: pop
    //   663: aload_0
    //   664: iconst_m1
    //   665: invokespecial setLayoutStateDirection : (I)V
    //   668: aload_0
    //   669: getfield mLayoutState : Landroid/support/v7/widget/LayoutState;
    //   672: aload #5
    //   674: getfield mPosition : I
    //   677: aload_0
    //   678: getfield mLayoutState : Landroid/support/v7/widget/LayoutState;
    //   681: getfield mItemDirection : I
    //   684: iadd
    //   685: putfield mCurrentPosition : I
    //   688: aload_0
    //   689: aload_1
    //   690: aload_0
    //   691: getfield mLayoutState : Landroid/support/v7/widget/LayoutState;
    //   694: aload_2
    //   695: invokespecial fill : (Landroid/support/v7/widget/RecyclerView$Recycler;Landroid/support/v7/widget/LayoutState;Landroid/support/v7/widget/RecyclerView$State;)I
    //   698: pop
    //   699: goto -> 425
    //   702: aload_0
    //   703: aload_1
    //   704: aload_2
    //   705: iconst_1
    //   706: invokespecial fixStartGap : (Landroid/support/v7/widget/RecyclerView$Recycler;Landroid/support/v7/widget/RecyclerView$State;Z)V
    //   709: aload_0
    //   710: aload_1
    //   711: aload_2
    //   712: iconst_0
    //   713: invokespecial fixEndGap : (Landroid/support/v7/widget/RecyclerView$Recycler;Landroid/support/v7/widget/RecyclerView$State;Z)V
    //   716: goto -> 457
    //   719: iconst_0
    //   720: istore #8
    //   722: goto -> 515
  }
  
  private boolean preferLastSpan(int paramInt) {
    boolean bool2;
    boolean bool = true;
    if (this.mOrientation == 0) {
      if (paramInt == -1) {
        null = true;
      } else {
        null = false;
      } 
      return (null != this.mShouldReverseLayout) ? bool : false;
    } 
    if (paramInt == -1) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (bool1 == this.mShouldReverseLayout) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    boolean bool1 = bool;
    if (bool2 != isLayoutRTL())
      bool1 = false; 
    return bool1;
  }
  
  private void prependViewToAllSpans(View paramView) {
    for (int i = this.mSpanCount - 1; i >= 0; i--)
      this.mSpans[i].prependToSpan(paramView); 
  }
  
  private void recycle(RecyclerView.Recycler paramRecycler, LayoutState paramLayoutState) {
    if (paramLayoutState.mRecycle && !paramLayoutState.mInfinite) {
      if (paramLayoutState.mAvailable == 0) {
        if (paramLayoutState.mLayoutDirection == -1) {
          recycleFromEnd(paramRecycler, paramLayoutState.mEndLine);
          return;
        } 
        recycleFromStart(paramRecycler, paramLayoutState.mStartLine);
        return;
      } 
      if (paramLayoutState.mLayoutDirection == -1) {
        int j = paramLayoutState.mStartLine - getMaxStart(paramLayoutState.mStartLine);
        if (j < 0) {
          j = paramLayoutState.mEndLine;
        } else {
          j = paramLayoutState.mEndLine - Math.min(j, paramLayoutState.mAvailable);
        } 
        recycleFromEnd(paramRecycler, j);
        return;
      } 
      int i = getMinEnd(paramLayoutState.mEndLine) - paramLayoutState.mEndLine;
      if (i < 0) {
        i = paramLayoutState.mStartLine;
      } else {
        i = paramLayoutState.mStartLine + Math.min(i, paramLayoutState.mAvailable);
      } 
      recycleFromStart(paramRecycler, i);
    } 
  }
  
  private void recycleFromEnd(RecyclerView.Recycler paramRecycler, int paramInt) {
    int i = getChildCount() - 1;
    while (true) {
      if (i >= 0) {
        View view = getChildAt(i);
        if (this.mPrimaryOrientation.getDecoratedStart(view) >= paramInt) {
          if (this.mPrimaryOrientation.getTransformedStartWithDecoration(view) >= paramInt) {
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (layoutParams.mFullSpan) {
              byte b;
              for (b = 0; b < this.mSpanCount; b++) {
                if ((this.mSpans[b]).mViews.size() == 1)
                  return; 
              } 
              for (b = 0; b < this.mSpanCount; b++)
                this.mSpans[b].popEnd(); 
            } else if (layoutParams.mSpan.mViews.size() != 1) {
              layoutParams.mSpan.popEnd();
            } else {
              continue;
            } 
            removeAndRecycleView(view, paramRecycler);
            i--;
            continue;
          } 
          continue;
        } 
        continue;
      } 
      continue;
    } 
  }
  
  private void recycleFromStart(RecyclerView.Recycler paramRecycler, int paramInt) {
    while (true) {
      if (getChildCount() > 0) {
        View view = getChildAt(0);
        if (this.mPrimaryOrientation.getDecoratedEnd(view) <= paramInt) {
          if (this.mPrimaryOrientation.getTransformedEndWithDecoration(view) <= paramInt) {
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (layoutParams.mFullSpan) {
              byte b;
              for (b = 0; b < this.mSpanCount; b++) {
                if ((this.mSpans[b]).mViews.size() == 1)
                  return; 
              } 
              for (b = 0; b < this.mSpanCount; b++)
                this.mSpans[b].popStart(); 
            } else if (layoutParams.mSpan.mViews.size() != 1) {
              layoutParams.mSpan.popStart();
            } else {
              continue;
            } 
            removeAndRecycleView(view, paramRecycler);
            continue;
          } 
          continue;
        } 
        continue;
      } 
      continue;
    } 
  }
  
  private void repositionToWrapContentIfNecessary() {
    if (this.mSecondaryOrientation.getMode() != 1073741824) {
      float f = 0.0F;
      int i = getChildCount();
      int j;
      for (j = 0; j < i; j++) {
        View view = getChildAt(j);
        float f1 = this.mSecondaryOrientation.getDecoratedMeasurement(view);
        if (f1 >= f) {
          float f2 = f1;
          if (((LayoutParams)view.getLayoutParams()).isFullSpan())
            f2 = 1.0F * f1 / this.mSpanCount; 
          f = Math.max(f, f2);
        } 
      } 
      int k = this.mSizePerSpan;
      int m = Math.round(this.mSpanCount * f);
      j = m;
      if (this.mSecondaryOrientation.getMode() == Integer.MIN_VALUE)
        j = Math.min(m, this.mSecondaryOrientation.getTotalSpace()); 
      updateMeasureSpecs(j);
      if (this.mSizePerSpan != k) {
        j = 0;
        while (true) {
          if (j < i) {
            View view = getChildAt(j);
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (!layoutParams.mFullSpan)
              if (isLayoutRTL() && this.mOrientation == 1) {
                view.offsetLeftAndRight(-(this.mSpanCount - 1 - layoutParams.mSpan.mIndex) * this.mSizePerSpan - -(this.mSpanCount - 1 - layoutParams.mSpan.mIndex) * k);
              } else {
                int n = layoutParams.mSpan.mIndex * this.mSizePerSpan;
                m = layoutParams.mSpan.mIndex * k;
                if (this.mOrientation == 1) {
                  view.offsetLeftAndRight(n - m);
                } else {
                  view.offsetTopAndBottom(n - m);
                } 
              }  
            j++;
            continue;
          } 
          return;
        } 
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
  
  private void setLayoutStateDirection(int paramInt) {
    boolean bool2;
    boolean bool = true;
    this.mLayoutState.mLayoutDirection = paramInt;
    LayoutState layoutState = this.mLayoutState;
    boolean bool1 = this.mShouldReverseLayout;
    if (paramInt == -1) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (bool1 == bool2) {
      paramInt = bool;
    } else {
      paramInt = -1;
    } 
    layoutState.mItemDirection = paramInt;
  }
  
  private void updateAllRemainingSpans(int paramInt1, int paramInt2) {
    for (byte b = 0; b < this.mSpanCount; b++) {
      if (!(this.mSpans[b]).mViews.isEmpty())
        updateRemainingSpans(this.mSpans[b], paramInt1, paramInt2); 
    } 
  }
  
  private boolean updateAnchorFromChildren(RecyclerView.State paramState, AnchorInfo paramAnchorInfo) {
    if (this.mLastLayoutFromEnd) {
      int j = findLastReferenceChildPosition(paramState.getItemCount());
      paramAnchorInfo.mPosition = j;
      paramAnchorInfo.mOffset = Integer.MIN_VALUE;
      return true;
    } 
    int i = findFirstReferenceChildPosition(paramState.getItemCount());
    paramAnchorInfo.mPosition = i;
    paramAnchorInfo.mOffset = Integer.MIN_VALUE;
    return true;
  }
  
  private void updateLayoutState(int paramInt, RecyclerView.State paramState) {
    boolean bool2;
    boolean bool1 = true;
    this.mLayoutState.mAvailable = 0;
    this.mLayoutState.mCurrentPosition = paramInt;
    byte b1 = 0;
    byte b2 = 0;
    int i = b2;
    int j = b1;
    if (isSmoothScrolling()) {
      int k = paramState.getTargetScrollPosition();
      i = b2;
      j = b1;
      if (k != -1) {
        boolean bool = this.mShouldReverseLayout;
        if (k < paramInt) {
          bool2 = true;
        } else {
          bool2 = false;
        } 
        if (bool == bool2) {
          i = this.mPrimaryOrientation.getTotalSpace();
          j = b1;
        } else {
          j = this.mPrimaryOrientation.getTotalSpace();
          i = b2;
        } 
      } 
    } 
    if (getClipToPadding()) {
      this.mLayoutState.mStartLine = this.mPrimaryOrientation.getStartAfterPadding() - j;
      this.mLayoutState.mEndLine = this.mPrimaryOrientation.getEndAfterPadding() + i;
    } else {
      this.mLayoutState.mEndLine = this.mPrimaryOrientation.getEnd() + i;
      this.mLayoutState.mStartLine = -j;
    } 
    this.mLayoutState.mStopInFocusable = false;
    this.mLayoutState.mRecycle = true;
    LayoutState layoutState = this.mLayoutState;
    if (this.mPrimaryOrientation.getMode() == 0 && this.mPrimaryOrientation.getEnd() == 0) {
      bool2 = bool1;
    } else {
      bool2 = false;
    } 
    layoutState.mInfinite = bool2;
  }
  
  private void updateRemainingSpans(Span paramSpan, int paramInt1, int paramInt2) {
    int i = paramSpan.getDeletedSize();
    if (paramInt1 == -1) {
      if (paramSpan.getStartLine() + i <= paramInt2)
        this.mRemainingSpans.set(paramSpan.mIndex, false); 
      return;
    } 
    if (paramSpan.getEndLine() - i >= paramInt2)
      this.mRemainingSpans.set(paramSpan.mIndex, false); 
  }
  
  private int updateSpecWithExtra(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt2 == 0 && paramInt3 == 0)
      return paramInt1; 
    int i = View.MeasureSpec.getMode(paramInt1);
    if (i != Integer.MIN_VALUE) {
      int j = paramInt1;
      return (i == 1073741824) ? View.MeasureSpec.makeMeasureSpec(Math.max(0, View.MeasureSpec.getSize(paramInt1) - paramInt2 - paramInt3), i) : j;
    } 
    return View.MeasureSpec.makeMeasureSpec(Math.max(0, View.MeasureSpec.getSize(paramInt1) - paramInt2 - paramInt3), i);
  }
  
  boolean areAllEndsEqual() {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: getfield mSpans : [Landroid/support/v7/widget/StaggeredGridLayoutManager$Span;
    //   6: iconst_0
    //   7: aaload
    //   8: ldc -2147483648
    //   10: invokevirtual getEndLine : (I)I
    //   13: istore_2
    //   14: iconst_1
    //   15: istore_3
    //   16: iload_3
    //   17: aload_0
    //   18: getfield mSpanCount : I
    //   21: if_icmpge -> 47
    //   24: aload_0
    //   25: getfield mSpans : [Landroid/support/v7/widget/StaggeredGridLayoutManager$Span;
    //   28: iload_3
    //   29: aaload
    //   30: ldc -2147483648
    //   32: invokevirtual getEndLine : (I)I
    //   35: iload_2
    //   36: if_icmpeq -> 41
    //   39: iload_1
    //   40: ireturn
    //   41: iinc #3, 1
    //   44: goto -> 16
    //   47: iconst_1
    //   48: istore_1
    //   49: goto -> 39
  }
  
  boolean areAllStartsEqual() {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: getfield mSpans : [Landroid/support/v7/widget/StaggeredGridLayoutManager$Span;
    //   6: iconst_0
    //   7: aaload
    //   8: ldc -2147483648
    //   10: invokevirtual getStartLine : (I)I
    //   13: istore_2
    //   14: iconst_1
    //   15: istore_3
    //   16: iload_3
    //   17: aload_0
    //   18: getfield mSpanCount : I
    //   21: if_icmpge -> 47
    //   24: aload_0
    //   25: getfield mSpans : [Landroid/support/v7/widget/StaggeredGridLayoutManager$Span;
    //   28: iload_3
    //   29: aaload
    //   30: ldc -2147483648
    //   32: invokevirtual getStartLine : (I)I
    //   35: iload_2
    //   36: if_icmpeq -> 41
    //   39: iload_1
    //   40: ireturn
    //   41: iinc #3, 1
    //   44: goto -> 16
    //   47: iconst_1
    //   48: istore_1
    //   49: goto -> 39
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
  
  boolean checkForGaps() {
    int i;
    int j;
    byte b;
    boolean bool = true;
    if (getChildCount() == 0 || this.mGapStrategy == 0 || !isAttachedToWindow())
      return false; 
    if (this.mShouldReverseLayout) {
      i = getLastChildPosition();
      j = getFirstChildPosition();
    } else {
      i = getFirstChildPosition();
      j = getLastChildPosition();
    } 
    if (i == 0 && hasGapsToFix() != null) {
      this.mLazySpanLookup.clear();
      requestSimpleAnimationsInNextLayout();
      requestLayout();
      return bool;
    } 
    if (!this.mLaidOutInvalidFullSpan)
      return false; 
    if (this.mShouldReverseLayout) {
      b = -1;
    } else {
      b = 1;
    } 
    LazySpanLookup.FullSpanItem fullSpanItem1 = this.mLazySpanLookup.getFirstFullSpanItemInRange(i, j + 1, b, true);
    if (fullSpanItem1 == null) {
      this.mLaidOutInvalidFullSpan = false;
      this.mLazySpanLookup.forceInvalidateAfter(j + 1);
      return false;
    } 
    LazySpanLookup.FullSpanItem fullSpanItem2 = this.mLazySpanLookup.getFirstFullSpanItemInRange(i, fullSpanItem1.mPosition, b * -1, true);
    if (fullSpanItem2 == null) {
      this.mLazySpanLookup.forceInvalidateAfter(fullSpanItem1.mPosition);
    } else {
      this.mLazySpanLookup.forceInvalidateAfter(fullSpanItem2.mPosition + 1);
    } 
    requestSimpleAnimationsInNextLayout();
    requestLayout();
    return bool;
  }
  
  public boolean checkLayoutParams(RecyclerView.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  public void collectAdjacentPrefetchPositions(int paramInt1, int paramInt2, RecyclerView.State paramState, RecyclerView.LayoutManager.LayoutPrefetchRegistry paramLayoutPrefetchRegistry) {
    if (this.mOrientation != 0)
      paramInt1 = paramInt2; 
    if (getChildCount() != 0 && paramInt1 != 0) {
      prepareLayoutStateForDelta(paramInt1, paramState);
      if (this.mPrefetchDistances == null || this.mPrefetchDistances.length < this.mSpanCount)
        this.mPrefetchDistances = new int[this.mSpanCount]; 
      for (paramInt1 = 0; paramInt1 < this.mSpanCount; paramInt1++) {
        int[] arrayOfInt = this.mPrefetchDistances;
        if (this.mLayoutState.mItemDirection == -1) {
          paramInt2 = this.mLayoutState.mStartLine - this.mSpans[paramInt1].getStartLine(this.mLayoutState.mStartLine);
        } else {
          paramInt2 = this.mSpans[paramInt1].getEndLine(this.mLayoutState.mEndLine) - this.mLayoutState.mEndLine;
        } 
        arrayOfInt[paramInt1] = paramInt2;
      } 
      Arrays.sort(this.mPrefetchDistances, 0, this.mSpanCount);
      paramInt1 = 0;
      while (true) {
        if (paramInt1 < this.mSpanCount && this.mLayoutState.hasMore(paramState)) {
          paramLayoutPrefetchRegistry.addPosition(this.mLayoutState.mCurrentPosition, this.mPrefetchDistances[paramInt1]);
          LayoutState layoutState = this.mLayoutState;
          layoutState.mCurrentPosition += this.mLayoutState.mItemDirection;
          paramInt1++;
          continue;
        } 
        return;
      } 
    } 
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
    paramInt = calculateScrollDirectionForPosition(paramInt);
    PointF pointF = new PointF();
    if (paramInt == 0)
      return null; 
    if (this.mOrientation == 0) {
      pointF.x = paramInt;
      pointF.y = 0.0F;
      return pointF;
    } 
    pointF.x = 0.0F;
    pointF.y = paramInt;
    return pointF;
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
  
  public int[] findFirstCompletelyVisibleItemPositions(int[] paramArrayOfint) {
    int[] arrayOfInt;
    if (paramArrayOfint == null) {
      arrayOfInt = new int[this.mSpanCount];
    } else {
      arrayOfInt = paramArrayOfint;
      if (paramArrayOfint.length < this.mSpanCount)
        throw new IllegalArgumentException("Provided int[]'s size must be more than or equal to span count. Expected:" + this.mSpanCount + ", array size:" + paramArrayOfint.length); 
    } 
    for (byte b = 0; b < this.mSpanCount; b++)
      arrayOfInt[b] = this.mSpans[b].findFirstCompletelyVisibleItemPosition(); 
    return arrayOfInt;
  }
  
  View findFirstVisibleItemClosestToEnd(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mPrimaryOrientation : Landroid/support/v7/widget/OrientationHelper;
    //   4: invokevirtual getStartAfterPadding : ()I
    //   7: istore_2
    //   8: aload_0
    //   9: getfield mPrimaryOrientation : Landroid/support/v7/widget/OrientationHelper;
    //   12: invokevirtual getEndAfterPadding : ()I
    //   15: istore_3
    //   16: aconst_null
    //   17: astore #4
    //   19: aload_0
    //   20: invokevirtual getChildCount : ()I
    //   23: iconst_1
    //   24: isub
    //   25: istore #5
    //   27: iload #5
    //   29: iflt -> 129
    //   32: aload_0
    //   33: iload #5
    //   35: invokevirtual getChildAt : (I)Landroid/view/View;
    //   38: astore #6
    //   40: aload_0
    //   41: getfield mPrimaryOrientation : Landroid/support/v7/widget/OrientationHelper;
    //   44: aload #6
    //   46: invokevirtual getDecoratedStart : (Landroid/view/View;)I
    //   49: istore #7
    //   51: aload_0
    //   52: getfield mPrimaryOrientation : Landroid/support/v7/widget/OrientationHelper;
    //   55: aload #6
    //   57: invokevirtual getDecoratedEnd : (Landroid/view/View;)I
    //   60: istore #8
    //   62: aload #4
    //   64: astore #9
    //   66: iload #8
    //   68: iload_2
    //   69: if_icmple -> 82
    //   72: iload #7
    //   74: iload_3
    //   75: if_icmplt -> 92
    //   78: aload #4
    //   80: astore #9
    //   82: iinc #5, -1
    //   85: aload #9
    //   87: astore #4
    //   89: goto -> 27
    //   92: aload #6
    //   94: astore #9
    //   96: iload #8
    //   98: iload_3
    //   99: if_icmple -> 110
    //   102: iload_1
    //   103: ifne -> 113
    //   106: aload #6
    //   108: astore #9
    //   110: aload #9
    //   112: areturn
    //   113: aload #4
    //   115: astore #9
    //   117: aload #4
    //   119: ifnonnull -> 82
    //   122: aload #6
    //   124: astore #9
    //   126: goto -> 82
    //   129: aload #4
    //   131: astore #9
    //   133: goto -> 110
  }
  
  View findFirstVisibleItemClosestToStart(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mPrimaryOrientation : Landroid/support/v7/widget/OrientationHelper;
    //   4: invokevirtual getStartAfterPadding : ()I
    //   7: istore_2
    //   8: aload_0
    //   9: getfield mPrimaryOrientation : Landroid/support/v7/widget/OrientationHelper;
    //   12: invokevirtual getEndAfterPadding : ()I
    //   15: istore_3
    //   16: aload_0
    //   17: invokevirtual getChildCount : ()I
    //   20: istore #4
    //   22: aconst_null
    //   23: astore #5
    //   25: iconst_0
    //   26: istore #6
    //   28: iload #6
    //   30: iload #4
    //   32: if_icmpge -> 128
    //   35: aload_0
    //   36: iload #6
    //   38: invokevirtual getChildAt : (I)Landroid/view/View;
    //   41: astore #7
    //   43: aload_0
    //   44: getfield mPrimaryOrientation : Landroid/support/v7/widget/OrientationHelper;
    //   47: aload #7
    //   49: invokevirtual getDecoratedStart : (Landroid/view/View;)I
    //   52: istore #8
    //   54: aload #5
    //   56: astore #9
    //   58: aload_0
    //   59: getfield mPrimaryOrientation : Landroid/support/v7/widget/OrientationHelper;
    //   62: aload #7
    //   64: invokevirtual getDecoratedEnd : (Landroid/view/View;)I
    //   67: iload_2
    //   68: if_icmple -> 81
    //   71: iload #8
    //   73: iload_3
    //   74: if_icmplt -> 91
    //   77: aload #5
    //   79: astore #9
    //   81: iinc #6, 1
    //   84: aload #9
    //   86: astore #5
    //   88: goto -> 28
    //   91: aload #7
    //   93: astore #9
    //   95: iload #8
    //   97: iload_2
    //   98: if_icmpge -> 109
    //   101: iload_1
    //   102: ifne -> 112
    //   105: aload #7
    //   107: astore #9
    //   109: aload #9
    //   111: areturn
    //   112: aload #5
    //   114: astore #9
    //   116: aload #5
    //   118: ifnonnull -> 81
    //   121: aload #7
    //   123: astore #9
    //   125: goto -> 81
    //   128: aload #5
    //   130: astore #9
    //   132: goto -> 109
  }
  
  int findFirstVisibleItemPositionInt() {
    View view;
    if (this.mShouldReverseLayout) {
      view = findFirstVisibleItemClosestToEnd(true);
    } else {
      view = findFirstVisibleItemClosestToStart(true);
    } 
    return (view == null) ? -1 : getPosition(view);
  }
  
  public int[] findFirstVisibleItemPositions(int[] paramArrayOfint) {
    int[] arrayOfInt;
    if (paramArrayOfint == null) {
      arrayOfInt = new int[this.mSpanCount];
    } else {
      arrayOfInt = paramArrayOfint;
      if (paramArrayOfint.length < this.mSpanCount)
        throw new IllegalArgumentException("Provided int[]'s size must be more than or equal to span count. Expected:" + this.mSpanCount + ", array size:" + paramArrayOfint.length); 
    } 
    for (byte b = 0; b < this.mSpanCount; b++)
      arrayOfInt[b] = this.mSpans[b].findFirstVisibleItemPosition(); 
    return arrayOfInt;
  }
  
  public int[] findLastCompletelyVisibleItemPositions(int[] paramArrayOfint) {
    int[] arrayOfInt;
    if (paramArrayOfint == null) {
      arrayOfInt = new int[this.mSpanCount];
    } else {
      arrayOfInt = paramArrayOfint;
      if (paramArrayOfint.length < this.mSpanCount)
        throw new IllegalArgumentException("Provided int[]'s size must be more than or equal to span count. Expected:" + this.mSpanCount + ", array size:" + paramArrayOfint.length); 
    } 
    for (byte b = 0; b < this.mSpanCount; b++)
      arrayOfInt[b] = this.mSpans[b].findLastCompletelyVisibleItemPosition(); 
    return arrayOfInt;
  }
  
  public int[] findLastVisibleItemPositions(int[] paramArrayOfint) {
    int[] arrayOfInt;
    if (paramArrayOfint == null) {
      arrayOfInt = new int[this.mSpanCount];
    } else {
      arrayOfInt = paramArrayOfint;
      if (paramArrayOfint.length < this.mSpanCount)
        throw new IllegalArgumentException("Provided int[]'s size must be more than or equal to span count. Expected:" + this.mSpanCount + ", array size:" + paramArrayOfint.length); 
    } 
    for (byte b = 0; b < this.mSpanCount; b++)
      arrayOfInt[b] = this.mSpans[b].findLastVisibleItemPosition(); 
    return arrayOfInt;
  }
  
  public RecyclerView.LayoutParams generateDefaultLayoutParams() {
    return (this.mOrientation == 0) ? new LayoutParams(-2, -1) : new LayoutParams(-1, -2);
  }
  
  public RecyclerView.LayoutParams generateLayoutParams(Context paramContext, AttributeSet paramAttributeSet) {
    return new LayoutParams(paramContext, paramAttributeSet);
  }
  
  public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (paramLayoutParams instanceof ViewGroup.MarginLayoutParams) ? new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams) : new LayoutParams(paramLayoutParams);
  }
  
  public int getColumnCountForAccessibility(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return (this.mOrientation == 1) ? this.mSpanCount : super.getColumnCountForAccessibility(paramRecycler, paramState);
  }
  
  int getFirstChildPosition() {
    int i = 0;
    if (getChildCount() != 0)
      i = getPosition(getChildAt(0)); 
    return i;
  }
  
  public int getGapStrategy() {
    return this.mGapStrategy;
  }
  
  int getLastChildPosition() {
    null = getChildCount();
    return (null == 0) ? 0 : getPosition(getChildAt(null - 1));
  }
  
  public int getOrientation() {
    return this.mOrientation;
  }
  
  public boolean getReverseLayout() {
    return this.mReverseLayout;
  }
  
  public int getRowCountForAccessibility(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return (this.mOrientation == 0) ? this.mSpanCount : super.getRowCountForAccessibility(paramRecycler, paramState);
  }
  
  public int getSpanCount() {
    return this.mSpanCount;
  }
  
  View hasGapsToFix() {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getChildCount : ()I
    //   4: iconst_1
    //   5: isub
    //   6: istore_1
    //   7: new java/util/BitSet
    //   10: dup
    //   11: aload_0
    //   12: getfield mSpanCount : I
    //   15: invokespecial <init> : (I)V
    //   18: astore_2
    //   19: aload_2
    //   20: iconst_0
    //   21: aload_0
    //   22: getfield mSpanCount : I
    //   25: iconst_1
    //   26: invokevirtual set : (IIZ)V
    //   29: aload_0
    //   30: getfield mOrientation : I
    //   33: iconst_1
    //   34: if_icmpne -> 129
    //   37: aload_0
    //   38: invokevirtual isLayoutRTL : ()Z
    //   41: ifeq -> 129
    //   44: iconst_1
    //   45: istore_3
    //   46: aload_0
    //   47: getfield mShouldReverseLayout : Z
    //   50: ifeq -> 134
    //   53: iconst_0
    //   54: iconst_1
    //   55: isub
    //   56: istore #4
    //   58: iload_1
    //   59: iload #4
    //   61: if_icmpge -> 148
    //   64: iconst_1
    //   65: istore #5
    //   67: iload_1
    //   68: istore #6
    //   70: iload #6
    //   72: iload #4
    //   74: if_icmpeq -> 368
    //   77: aload_0
    //   78: iload #6
    //   80: invokevirtual getChildAt : (I)Landroid/view/View;
    //   83: astore #7
    //   85: aload #7
    //   87: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   90: checkcast android/support/v7/widget/StaggeredGridLayoutManager$LayoutParams
    //   93: astore #8
    //   95: aload_2
    //   96: aload #8
    //   98: getfield mSpan : Landroid/support/v7/widget/StaggeredGridLayoutManager$Span;
    //   101: getfield mIndex : I
    //   104: invokevirtual get : (I)Z
    //   107: ifeq -> 166
    //   110: aload_0
    //   111: aload #8
    //   113: getfield mSpan : Landroid/support/v7/widget/StaggeredGridLayoutManager$Span;
    //   116: invokespecial checkSpanForGap : (Landroid/support/v7/widget/StaggeredGridLayoutManager$Span;)Z
    //   119: ifeq -> 154
    //   122: aload #7
    //   124: astore #9
    //   126: aload #9
    //   128: areturn
    //   129: iconst_m1
    //   130: istore_3
    //   131: goto -> 46
    //   134: iconst_0
    //   135: istore #5
    //   137: iload_1
    //   138: iconst_1
    //   139: iadd
    //   140: istore #4
    //   142: iload #5
    //   144: istore_1
    //   145: goto -> 58
    //   148: iconst_m1
    //   149: istore #5
    //   151: goto -> 67
    //   154: aload_2
    //   155: aload #8
    //   157: getfield mSpan : Landroid/support/v7/widget/StaggeredGridLayoutManager$Span;
    //   160: getfield mIndex : I
    //   163: invokevirtual clear : (I)V
    //   166: aload #8
    //   168: getfield mFullSpan : Z
    //   171: ifeq -> 184
    //   174: iload #6
    //   176: iload #5
    //   178: iadd
    //   179: istore #6
    //   181: goto -> 70
    //   184: iload #6
    //   186: iload #5
    //   188: iadd
    //   189: iload #4
    //   191: if_icmpeq -> 174
    //   194: aload_0
    //   195: iload #6
    //   197: iload #5
    //   199: iadd
    //   200: invokevirtual getChildAt : (I)Landroid/view/View;
    //   203: astore #10
    //   205: iconst_0
    //   206: istore_1
    //   207: aload_0
    //   208: getfield mShouldReverseLayout : Z
    //   211: ifeq -> 312
    //   214: aload_0
    //   215: getfield mPrimaryOrientation : Landroid/support/v7/widget/OrientationHelper;
    //   218: aload #7
    //   220: invokevirtual getDecoratedEnd : (Landroid/view/View;)I
    //   223: istore #11
    //   225: aload_0
    //   226: getfield mPrimaryOrientation : Landroid/support/v7/widget/OrientationHelper;
    //   229: aload #10
    //   231: invokevirtual getDecoratedEnd : (Landroid/view/View;)I
    //   234: istore #12
    //   236: aload #7
    //   238: astore #9
    //   240: iload #11
    //   242: iload #12
    //   244: if_icmplt -> 126
    //   247: iload #11
    //   249: iload #12
    //   251: if_icmpne -> 256
    //   254: iconst_1
    //   255: istore_1
    //   256: iload_1
    //   257: ifeq -> 174
    //   260: aload #10
    //   262: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   265: checkcast android/support/v7/widget/StaggeredGridLayoutManager$LayoutParams
    //   268: astore #9
    //   270: aload #8
    //   272: getfield mSpan : Landroid/support/v7/widget/StaggeredGridLayoutManager$Span;
    //   275: getfield mIndex : I
    //   278: aload #9
    //   280: getfield mSpan : Landroid/support/v7/widget/StaggeredGridLayoutManager$Span;
    //   283: getfield mIndex : I
    //   286: isub
    //   287: ifge -> 357
    //   290: iconst_1
    //   291: istore_1
    //   292: iload_3
    //   293: ifge -> 362
    //   296: iconst_1
    //   297: istore #12
    //   299: iload_1
    //   300: iload #12
    //   302: if_icmpeq -> 174
    //   305: aload #7
    //   307: astore #9
    //   309: goto -> 126
    //   312: aload_0
    //   313: getfield mPrimaryOrientation : Landroid/support/v7/widget/OrientationHelper;
    //   316: aload #7
    //   318: invokevirtual getDecoratedStart : (Landroid/view/View;)I
    //   321: istore #11
    //   323: aload_0
    //   324: getfield mPrimaryOrientation : Landroid/support/v7/widget/OrientationHelper;
    //   327: aload #10
    //   329: invokevirtual getDecoratedStart : (Landroid/view/View;)I
    //   332: istore #12
    //   334: aload #7
    //   336: astore #9
    //   338: iload #11
    //   340: iload #12
    //   342: if_icmpgt -> 126
    //   345: iload #11
    //   347: iload #12
    //   349: if_icmpne -> 256
    //   352: iconst_1
    //   353: istore_1
    //   354: goto -> 256
    //   357: iconst_0
    //   358: istore_1
    //   359: goto -> 292
    //   362: iconst_0
    //   363: istore #12
    //   365: goto -> 299
    //   368: aconst_null
    //   369: astore #9
    //   371: goto -> 126
  }
  
  public void invalidateSpanAssignments() {
    this.mLazySpanLookup.clear();
    requestLayout();
  }
  
  boolean isLayoutRTL() {
    boolean bool = true;
    if (getLayoutDirection() != 1)
      bool = false; 
    return bool;
  }
  
  public void offsetChildrenHorizontal(int paramInt) {
    super.offsetChildrenHorizontal(paramInt);
    for (byte b = 0; b < this.mSpanCount; b++)
      this.mSpans[b].onOffset(paramInt); 
  }
  
  public void offsetChildrenVertical(int paramInt) {
    super.offsetChildrenVertical(paramInt);
    for (byte b = 0; b < this.mSpanCount; b++)
      this.mSpans[b].onOffset(paramInt); 
  }
  
  public void onDetachedFromWindow(RecyclerView paramRecyclerView, RecyclerView.Recycler paramRecycler) {
    removeCallbacks(this.mCheckForGapsRunnable);
    for (byte b = 0; b < this.mSpanCount; b++)
      this.mSpans[b].clear(); 
    paramRecyclerView.requestLayout();
  }
  
  @Nullable
  public View onFocusSearchFailed(View paramView, int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getChildCount : ()I
    //   4: ifne -> 11
    //   7: aconst_null
    //   8: astore_1
    //   9: aload_1
    //   10: areturn
    //   11: aload_0
    //   12: aload_1
    //   13: invokevirtual findContainingItemView : (Landroid/view/View;)Landroid/view/View;
    //   16: astore #5
    //   18: aload #5
    //   20: ifnonnull -> 28
    //   23: aconst_null
    //   24: astore_1
    //   25: goto -> 9
    //   28: aload_0
    //   29: invokespecial resolveShouldLayoutReverse : ()V
    //   32: aload_0
    //   33: iload_2
    //   34: invokespecial convertFocusDirectionToLayoutDirection : (I)I
    //   37: istore #6
    //   39: iload #6
    //   41: ldc -2147483648
    //   43: if_icmpne -> 51
    //   46: aconst_null
    //   47: astore_1
    //   48: goto -> 9
    //   51: aload #5
    //   53: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   56: checkcast android/support/v7/widget/StaggeredGridLayoutManager$LayoutParams
    //   59: astore_1
    //   60: aload_1
    //   61: getfield mFullSpan : Z
    //   64: istore #7
    //   66: aload_1
    //   67: getfield mSpan : Landroid/support/v7/widget/StaggeredGridLayoutManager$Span;
    //   70: astore_1
    //   71: iload #6
    //   73: iconst_1
    //   74: if_icmpne -> 245
    //   77: aload_0
    //   78: invokevirtual getLastChildPosition : ()I
    //   81: istore_2
    //   82: aload_0
    //   83: iload_2
    //   84: aload #4
    //   86: invokespecial updateLayoutState : (ILandroid/support/v7/widget/RecyclerView$State;)V
    //   89: aload_0
    //   90: iload #6
    //   92: invokespecial setLayoutStateDirection : (I)V
    //   95: aload_0
    //   96: getfield mLayoutState : Landroid/support/v7/widget/LayoutState;
    //   99: aload_0
    //   100: getfield mLayoutState : Landroid/support/v7/widget/LayoutState;
    //   103: getfield mItemDirection : I
    //   106: iload_2
    //   107: iadd
    //   108: putfield mCurrentPosition : I
    //   111: aload_0
    //   112: getfield mLayoutState : Landroid/support/v7/widget/LayoutState;
    //   115: ldc 0.33333334
    //   117: aload_0
    //   118: getfield mPrimaryOrientation : Landroid/support/v7/widget/OrientationHelper;
    //   121: invokevirtual getTotalSpace : ()I
    //   124: i2f
    //   125: fmul
    //   126: f2i
    //   127: putfield mAvailable : I
    //   130: aload_0
    //   131: getfield mLayoutState : Landroid/support/v7/widget/LayoutState;
    //   134: iconst_1
    //   135: putfield mStopInFocusable : Z
    //   138: aload_0
    //   139: getfield mLayoutState : Landroid/support/v7/widget/LayoutState;
    //   142: iconst_0
    //   143: putfield mRecycle : Z
    //   146: aload_0
    //   147: aload_3
    //   148: aload_0
    //   149: getfield mLayoutState : Landroid/support/v7/widget/LayoutState;
    //   152: aload #4
    //   154: invokespecial fill : (Landroid/support/v7/widget/RecyclerView$Recycler;Landroid/support/v7/widget/LayoutState;Landroid/support/v7/widget/RecyclerView$State;)I
    //   157: pop
    //   158: aload_0
    //   159: aload_0
    //   160: getfield mShouldReverseLayout : Z
    //   163: putfield mLastLayoutFromEnd : Z
    //   166: iload #7
    //   168: ifne -> 191
    //   171: aload_1
    //   172: iload_2
    //   173: iload #6
    //   175: invokevirtual getFocusableViewAfter : (II)Landroid/view/View;
    //   178: astore_3
    //   179: aload_3
    //   180: ifnull -> 191
    //   183: aload_3
    //   184: astore_1
    //   185: aload_3
    //   186: aload #5
    //   188: if_acmpne -> 9
    //   191: aload_0
    //   192: iload #6
    //   194: invokespecial preferLastSpan : (I)Z
    //   197: ifeq -> 253
    //   200: aload_0
    //   201: getfield mSpanCount : I
    //   204: iconst_1
    //   205: isub
    //   206: istore #8
    //   208: iload #8
    //   210: iflt -> 297
    //   213: aload_0
    //   214: getfield mSpans : [Landroid/support/v7/widget/StaggeredGridLayoutManager$Span;
    //   217: iload #8
    //   219: aaload
    //   220: iload_2
    //   221: iload #6
    //   223: invokevirtual getFocusableViewAfter : (II)Landroid/view/View;
    //   226: astore_3
    //   227: aload_3
    //   228: ifnull -> 239
    //   231: aload_3
    //   232: astore_1
    //   233: aload_3
    //   234: aload #5
    //   236: if_acmpne -> 9
    //   239: iinc #8, -1
    //   242: goto -> 208
    //   245: aload_0
    //   246: invokevirtual getFirstChildPosition : ()I
    //   249: istore_2
    //   250: goto -> 82
    //   253: iconst_0
    //   254: istore #8
    //   256: iload #8
    //   258: aload_0
    //   259: getfield mSpanCount : I
    //   262: if_icmpge -> 297
    //   265: aload_0
    //   266: getfield mSpans : [Landroid/support/v7/widget/StaggeredGridLayoutManager$Span;
    //   269: iload #8
    //   271: aaload
    //   272: iload_2
    //   273: iload #6
    //   275: invokevirtual getFocusableViewAfter : (II)Landroid/view/View;
    //   278: astore_3
    //   279: aload_3
    //   280: ifnull -> 291
    //   283: aload_3
    //   284: astore_1
    //   285: aload_3
    //   286: aload #5
    //   288: if_acmpne -> 9
    //   291: iinc #8, 1
    //   294: goto -> 256
    //   297: aconst_null
    //   298: astore_1
    //   299: goto -> 9
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    if (getChildCount() > 0) {
      AccessibilityRecordCompat accessibilityRecordCompat = AccessibilityEventCompat.asRecord(paramAccessibilityEvent);
      View view2 = findFirstVisibleItemClosestToStart(false);
      View view1 = findFirstVisibleItemClosestToEnd(false);
      if (view2 != null && view1 != null) {
        int i = getPosition(view2);
        int j = getPosition(view1);
        if (i < j) {
          accessibilityRecordCompat.setFromIndex(i);
          accessibilityRecordCompat.setToIndex(j);
          return;
        } 
        accessibilityRecordCompat.setFromIndex(j);
        accessibilityRecordCompat.setToIndex(i);
      } 
    } 
  }
  
  public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat) {
    boolean bool;
    ViewGroup.LayoutParams layoutParams1 = paramView.getLayoutParams();
    if (!(layoutParams1 instanceof LayoutParams)) {
      onInitializeAccessibilityNodeInfoForItem(paramView, paramAccessibilityNodeInfoCompat);
      return;
    } 
    LayoutParams layoutParams = (LayoutParams)layoutParams1;
    if (this.mOrientation == 0) {
      int j = layoutParams.getSpanIndex();
      if (layoutParams.mFullSpan) {
        bool = this.mSpanCount;
      } else {
        bool = true;
      } 
      paramAccessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(j, bool, -1, -1, layoutParams.mFullSpan, false));
      return;
    } 
    int i = layoutParams.getSpanIndex();
    if (layoutParams.mFullSpan) {
      bool = this.mSpanCount;
    } else {
      bool = true;
    } 
    paramAccessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(-1, -1, i, bool, layoutParams.mFullSpan, false));
  }
  
  public void onItemsAdded(RecyclerView paramRecyclerView, int paramInt1, int paramInt2) {
    handleUpdate(paramInt1, paramInt2, 1);
  }
  
  public void onItemsChanged(RecyclerView paramRecyclerView) {
    this.mLazySpanLookup.clear();
    requestLayout();
  }
  
  public void onItemsMoved(RecyclerView paramRecyclerView, int paramInt1, int paramInt2, int paramInt3) {
    handleUpdate(paramInt1, paramInt2, 8);
  }
  
  public void onItemsRemoved(RecyclerView paramRecyclerView, int paramInt1, int paramInt2) {
    handleUpdate(paramInt1, paramInt2, 2);
  }
  
  public void onItemsUpdated(RecyclerView paramRecyclerView, int paramInt1, int paramInt2, Object paramObject) {
    handleUpdate(paramInt1, paramInt2, 4);
  }
  
  public void onLayoutChildren(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    onLayoutChildren(paramRecycler, paramState, true);
  }
  
  public void onLayoutCompleted(RecyclerView.State paramState) {
    super.onLayoutCompleted(paramState);
    this.mPendingScrollPosition = -1;
    this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
    this.mPendingSavedState = null;
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
    savedState.mReverseLayout = this.mReverseLayout;
    savedState.mAnchorLayoutFromEnd = this.mLastLayoutFromEnd;
    savedState.mLastLayoutRTL = this.mLastLayoutRTL;
    if (this.mLazySpanLookup != null && this.mLazySpanLookup.mData != null) {
      savedState.mSpanLookup = this.mLazySpanLookup.mData;
      savedState.mSpanLookupSize = savedState.mSpanLookup.length;
      savedState.mFullSpanItems = this.mLazySpanLookup.mFullSpanItems;
    } else {
      savedState.mSpanLookupSize = 0;
    } 
    if (getChildCount() > 0) {
      int i;
      if (this.mLastLayoutFromEnd) {
        i = getLastChildPosition();
      } else {
        i = getFirstChildPosition();
      } 
      savedState.mAnchorPosition = i;
      savedState.mVisibleAnchorPosition = findFirstVisibleItemPositionInt();
      savedState.mSpanOffsetsSize = this.mSpanCount;
      savedState.mSpanOffsets = new int[this.mSpanCount];
      byte b = 0;
      while (true) {
        SavedState savedState1 = savedState;
        if (b < this.mSpanCount) {
          if (this.mLastLayoutFromEnd) {
            int j = this.mSpans[b].getEndLine(-2147483648);
            i = j;
            if (j != Integer.MIN_VALUE)
              i = j - this.mPrimaryOrientation.getEndAfterPadding(); 
          } else {
            int j = this.mSpans[b].getStartLine(-2147483648);
            i = j;
            if (j != Integer.MIN_VALUE)
              i = j - this.mPrimaryOrientation.getStartAfterPadding(); 
          } 
          savedState.mSpanOffsets[b] = i;
          b++;
          continue;
        } 
        return savedState1;
      } 
    } 
    savedState.mAnchorPosition = -1;
    savedState.mVisibleAnchorPosition = -1;
    savedState.mSpanOffsetsSize = 0;
    return savedState;
  }
  
  public void onScrollStateChanged(int paramInt) {
    if (paramInt == 0)
      checkForGaps(); 
  }
  
  void prepareLayoutStateForDelta(int paramInt, RecyclerView.State paramState) {
    byte b;
    int i;
    if (paramInt > 0) {
      b = 1;
      i = getLastChildPosition();
    } else {
      b = -1;
      i = getFirstChildPosition();
    } 
    this.mLayoutState.mRecycle = true;
    updateLayoutState(i, paramState);
    setLayoutStateDirection(b);
    this.mLayoutState.mCurrentPosition = this.mLayoutState.mItemDirection + i;
    this.mLayoutState.mAvailable = Math.abs(paramInt);
  }
  
  int scrollBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    if (getChildCount() == 0 || paramInt == 0)
      return 0; 
    prepareLayoutStateForDelta(paramInt, paramState);
    int i = fill(paramRecycler, this.mLayoutState, paramState);
    if (this.mLayoutState.mAvailable >= i)
      if (paramInt < 0) {
        paramInt = -i;
      } else {
        paramInt = i;
      }  
    this.mPrimaryOrientation.offsetChildren(-paramInt);
    this.mLastLayoutFromEnd = this.mShouldReverseLayout;
    this.mLayoutState.mAvailable = 0;
    recycle(paramRecycler, this.mLayoutState);
    return paramInt;
  }
  
  public int scrollHorizontallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return scrollBy(paramInt, paramRecycler, paramState);
  }
  
  public void scrollToPosition(int paramInt) {
    if (this.mPendingSavedState != null && this.mPendingSavedState.mAnchorPosition != paramInt)
      this.mPendingSavedState.invalidateAnchorPositionInfo(); 
    this.mPendingScrollPosition = paramInt;
    this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
    requestLayout();
  }
  
  public void scrollToPositionWithOffset(int paramInt1, int paramInt2) {
    if (this.mPendingSavedState != null)
      this.mPendingSavedState.invalidateAnchorPositionInfo(); 
    this.mPendingScrollPosition = paramInt1;
    this.mPendingScrollPositionOffset = paramInt2;
    requestLayout();
  }
  
  public int scrollVerticallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return scrollBy(paramInt, paramRecycler, paramState);
  }
  
  public void setGapStrategy(int paramInt) {
    assertNotInLayoutOrScroll((String)null);
    if (paramInt != this.mGapStrategy) {
      boolean bool;
      if (paramInt != 0 && paramInt != 2)
        throw new IllegalArgumentException("invalid gap strategy. Must be GAP_HANDLING_NONE or GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS"); 
      this.mGapStrategy = paramInt;
      if (this.mGapStrategy != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      setAutoMeasureEnabled(bool);
      requestLayout();
    } 
  }
  
  public void setMeasuredDimension(Rect paramRect, int paramInt1, int paramInt2) {
    int i = getPaddingLeft() + getPaddingRight();
    int j = getPaddingTop() + getPaddingBottom();
    if (this.mOrientation == 1) {
      int k = chooseSize(paramInt2, paramRect.height() + j, getMinimumHeight());
      paramInt2 = chooseSize(paramInt1, this.mSizePerSpan * this.mSpanCount + i, getMinimumWidth());
      paramInt1 = k;
    } else {
      int k = chooseSize(paramInt1, paramRect.width() + i, getMinimumWidth());
      paramInt1 = chooseSize(paramInt2, this.mSizePerSpan * this.mSpanCount + j, getMinimumHeight());
      paramInt2 = k;
    } 
    setMeasuredDimension(paramInt2, paramInt1);
  }
  
  public void setOrientation(int paramInt) {
    if (paramInt != 0 && paramInt != 1)
      throw new IllegalArgumentException("invalid orientation."); 
    assertNotInLayoutOrScroll((String)null);
    if (paramInt != this.mOrientation) {
      this.mOrientation = paramInt;
      OrientationHelper orientationHelper = this.mPrimaryOrientation;
      this.mPrimaryOrientation = this.mSecondaryOrientation;
      this.mSecondaryOrientation = orientationHelper;
      requestLayout();
    } 
  }
  
  public void setReverseLayout(boolean paramBoolean) {
    assertNotInLayoutOrScroll((String)null);
    if (this.mPendingSavedState != null && this.mPendingSavedState.mReverseLayout != paramBoolean)
      this.mPendingSavedState.mReverseLayout = paramBoolean; 
    this.mReverseLayout = paramBoolean;
    requestLayout();
  }
  
  public void setSpanCount(int paramInt) {
    assertNotInLayoutOrScroll((String)null);
    if (paramInt != this.mSpanCount) {
      invalidateSpanAssignments();
      this.mSpanCount = paramInt;
      this.mRemainingSpans = new BitSet(this.mSpanCount);
      this.mSpans = new Span[this.mSpanCount];
      for (paramInt = 0; paramInt < this.mSpanCount; paramInt++)
        this.mSpans[paramInt] = new Span(paramInt); 
      requestLayout();
    } 
  }
  
  public void smoothScrollToPosition(RecyclerView paramRecyclerView, RecyclerView.State paramState, int paramInt) {
    LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(paramRecyclerView.getContext());
    linearSmoothScroller.setTargetPosition(paramInt);
    startSmoothScroll(linearSmoothScroller);
  }
  
  public boolean supportsPredictiveItemAnimations() {
    return (this.mPendingSavedState == null);
  }
  
  boolean updateAnchorFromPendingData(RecyclerView.State paramState, AnchorInfo paramAnchorInfo) {
    null = false;
    boolean bool = true;
    if (paramState.isPreLayout() || this.mPendingScrollPosition == -1)
      return false; 
    if (this.mPendingScrollPosition < 0 || this.mPendingScrollPosition >= paramState.getItemCount()) {
      this.mPendingScrollPosition = -1;
      this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
      return false;
    } 
    if (this.mPendingSavedState == null || this.mPendingSavedState.mAnchorPosition == -1 || this.mPendingSavedState.mSpanOffsetsSize < 1) {
      View view = findViewByPosition(this.mPendingScrollPosition);
      if (view != null) {
        if (this.mShouldReverseLayout) {
          i = getLastChildPosition();
        } else {
          i = getFirstChildPosition();
        } 
        paramAnchorInfo.mPosition = i;
        if (this.mPendingScrollPositionOffset != Integer.MIN_VALUE) {
          if (paramAnchorInfo.mLayoutFromEnd) {
            paramAnchorInfo.mOffset = this.mPrimaryOrientation.getEndAfterPadding() - this.mPendingScrollPositionOffset - this.mPrimaryOrientation.getDecoratedEnd(view);
            return bool;
          } 
          paramAnchorInfo.mOffset = this.mPrimaryOrientation.getStartAfterPadding() + this.mPendingScrollPositionOffset - this.mPrimaryOrientation.getDecoratedStart(view);
          return bool;
        } 
        if (this.mPrimaryOrientation.getDecoratedMeasurement(view) > this.mPrimaryOrientation.getTotalSpace()) {
          if (paramAnchorInfo.mLayoutFromEnd) {
            i = this.mPrimaryOrientation.getEndAfterPadding();
          } else {
            i = this.mPrimaryOrientation.getStartAfterPadding();
          } 
          paramAnchorInfo.mOffset = i;
          return bool;
        } 
        int i = this.mPrimaryOrientation.getDecoratedStart(view) - this.mPrimaryOrientation.getStartAfterPadding();
        if (i < 0) {
          paramAnchorInfo.mOffset = -i;
          return bool;
        } 
        i = this.mPrimaryOrientation.getEndAfterPadding() - this.mPrimaryOrientation.getDecoratedEnd(view);
        if (i < 0) {
          paramAnchorInfo.mOffset = i;
          return bool;
        } 
        paramAnchorInfo.mOffset = Integer.MIN_VALUE;
        return bool;
      } 
      paramAnchorInfo.mPosition = this.mPendingScrollPosition;
      if (this.mPendingScrollPositionOffset == Integer.MIN_VALUE) {
        if (calculateScrollDirectionForPosition(paramAnchorInfo.mPosition) == 1)
          null = true; 
        paramAnchorInfo.mLayoutFromEnd = null;
        paramAnchorInfo.assignCoordinateFromPadding();
      } else {
        paramAnchorInfo.assignCoordinateFromPadding(this.mPendingScrollPositionOffset);
      } 
      paramAnchorInfo.mInvalidateOffsets = true;
      return bool;
    } 
    paramAnchorInfo.mOffset = Integer.MIN_VALUE;
    paramAnchorInfo.mPosition = this.mPendingScrollPosition;
    return bool;
  }
  
  void updateAnchorInfoForLayout(RecyclerView.State paramState, AnchorInfo paramAnchorInfo) {
    if (!updateAnchorFromPendingData(paramState, paramAnchorInfo) && !updateAnchorFromChildren(paramState, paramAnchorInfo)) {
      paramAnchorInfo.assignCoordinateFromPadding();
      paramAnchorInfo.mPosition = 0;
    } 
  }
  
  void updateMeasureSpecs(int paramInt) {
    this.mSizePerSpan = paramInt / this.mSpanCount;
    this.mFullSizeSpec = View.MeasureSpec.makeMeasureSpec(paramInt, this.mSecondaryOrientation.getMode());
  }
  
  class AnchorInfo {
    boolean mInvalidateOffsets;
    
    boolean mLayoutFromEnd;
    
    int mOffset;
    
    int mPosition;
    
    int[] mSpanReferenceLines;
    
    boolean mValid;
    
    public AnchorInfo() {
      reset();
    }
    
    void assignCoordinateFromPadding() {
      int i;
      if (this.mLayoutFromEnd) {
        i = StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding();
      } else {
        i = StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding();
      } 
      this.mOffset = i;
    }
    
    void assignCoordinateFromPadding(int param1Int) {
      if (this.mLayoutFromEnd) {
        this.mOffset = StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding() - param1Int;
        return;
      } 
      this.mOffset = StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding() + param1Int;
    }
    
    void reset() {
      this.mPosition = -1;
      this.mOffset = Integer.MIN_VALUE;
      this.mLayoutFromEnd = false;
      this.mInvalidateOffsets = false;
      this.mValid = false;
      if (this.mSpanReferenceLines != null)
        Arrays.fill(this.mSpanReferenceLines, -1); 
    }
    
    void saveSpanReferenceLines(StaggeredGridLayoutManager.Span[] param1ArrayOfSpan) {
      int i = param1ArrayOfSpan.length;
      if (this.mSpanReferenceLines == null || this.mSpanReferenceLines.length < i)
        this.mSpanReferenceLines = new int[StaggeredGridLayoutManager.this.mSpans.length]; 
      for (byte b = 0; b < i; b++)
        this.mSpanReferenceLines[b] = param1ArrayOfSpan[b].getStartLine(-2147483648); 
    }
  }
  
  public static class LayoutParams extends RecyclerView.LayoutParams {
    public static final int INVALID_SPAN_ID = -1;
    
    boolean mFullSpan;
    
    StaggeredGridLayoutManager.Span mSpan;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    public LayoutParams(RecyclerView.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
    
    public final int getSpanIndex() {
      return (this.mSpan == null) ? -1 : this.mSpan.mIndex;
    }
    
    public boolean isFullSpan() {
      return this.mFullSpan;
    }
    
    public void setFullSpan(boolean param1Boolean) {
      this.mFullSpan = param1Boolean;
    }
  }
  
  static class LazySpanLookup {
    private static final int MIN_SIZE = 10;
    
    int[] mData;
    
    List<FullSpanItem> mFullSpanItems;
    
    private int invalidateFullSpansAfter(int param1Int) {
      byte b1 = -1;
      if (this.mFullSpanItems == null)
        return b1; 
      FullSpanItem fullSpanItem = getFullSpanItem(param1Int);
      if (fullSpanItem != null)
        this.mFullSpanItems.remove(fullSpanItem); 
      byte b2 = -1;
      int i = this.mFullSpanItems.size();
      byte b = 0;
      while (true) {
        byte b3 = b2;
        if (b < i)
          if (((FullSpanItem)this.mFullSpanItems.get(b)).mPosition >= param1Int) {
            b3 = b;
          } else {
            b++;
            continue;
          }  
        param1Int = b1;
        if (b3 != -1) {
          fullSpanItem = this.mFullSpanItems.get(b3);
          this.mFullSpanItems.remove(b3);
          param1Int = fullSpanItem.mPosition;
        } 
        return param1Int;
      } 
    }
    
    private void offsetFullSpansForAddition(int param1Int1, int param1Int2) {
      if (this.mFullSpanItems != null) {
        int i = this.mFullSpanItems.size() - 1;
        while (true) {
          if (i >= 0) {
            FullSpanItem fullSpanItem = this.mFullSpanItems.get(i);
            if (fullSpanItem.mPosition >= param1Int1)
              fullSpanItem.mPosition += param1Int2; 
            i--;
            continue;
          } 
          return;
        } 
      } 
    }
    
    private void offsetFullSpansForRemoval(int param1Int1, int param1Int2) {
      if (this.mFullSpanItems != null) {
        int i = this.mFullSpanItems.size() - 1;
        while (true) {
          if (i >= 0) {
            FullSpanItem fullSpanItem = this.mFullSpanItems.get(i);
            if (fullSpanItem.mPosition >= param1Int1)
              if (fullSpanItem.mPosition < param1Int1 + param1Int2) {
                this.mFullSpanItems.remove(i);
              } else {
                fullSpanItem.mPosition -= param1Int2;
              }  
            i--;
            continue;
          } 
          return;
        } 
      } 
    }
    
    public void addFullSpanItem(FullSpanItem param1FullSpanItem) {
      // Byte code:
      //   0: aload_0
      //   1: getfield mFullSpanItems : Ljava/util/List;
      //   4: ifnonnull -> 18
      //   7: aload_0
      //   8: new java/util/ArrayList
      //   11: dup
      //   12: invokespecial <init> : ()V
      //   15: putfield mFullSpanItems : Ljava/util/List;
      //   18: aload_0
      //   19: getfield mFullSpanItems : Ljava/util/List;
      //   22: invokeinterface size : ()I
      //   27: istore_2
      //   28: iconst_0
      //   29: istore_3
      //   30: iload_3
      //   31: iload_2
      //   32: if_icmpge -> 103
      //   35: aload_0
      //   36: getfield mFullSpanItems : Ljava/util/List;
      //   39: iload_3
      //   40: invokeinterface get : (I)Ljava/lang/Object;
      //   45: checkcast android/support/v7/widget/StaggeredGridLayoutManager$LazySpanLookup$FullSpanItem
      //   48: astore #4
      //   50: aload #4
      //   52: getfield mPosition : I
      //   55: aload_1
      //   56: getfield mPosition : I
      //   59: if_icmpne -> 73
      //   62: aload_0
      //   63: getfield mFullSpanItems : Ljava/util/List;
      //   66: iload_3
      //   67: invokeinterface remove : (I)Ljava/lang/Object;
      //   72: pop
      //   73: aload #4
      //   75: getfield mPosition : I
      //   78: aload_1
      //   79: getfield mPosition : I
      //   82: if_icmplt -> 97
      //   85: aload_0
      //   86: getfield mFullSpanItems : Ljava/util/List;
      //   89: iload_3
      //   90: aload_1
      //   91: invokeinterface add : (ILjava/lang/Object;)V
      //   96: return
      //   97: iinc #3, 1
      //   100: goto -> 30
      //   103: aload_0
      //   104: getfield mFullSpanItems : Ljava/util/List;
      //   107: aload_1
      //   108: invokeinterface add : (Ljava/lang/Object;)Z
      //   113: pop
      //   114: goto -> 96
    }
    
    void clear() {
      if (this.mData != null)
        Arrays.fill(this.mData, -1); 
      this.mFullSpanItems = null;
    }
    
    void ensureSize(int param1Int) {
      if (this.mData == null) {
        this.mData = new int[Math.max(param1Int, 10) + 1];
        Arrays.fill(this.mData, -1);
        return;
      } 
      if (param1Int >= this.mData.length) {
        int[] arrayOfInt = this.mData;
        this.mData = new int[sizeForPosition(param1Int)];
        System.arraycopy(arrayOfInt, 0, this.mData, 0, arrayOfInt.length);
        Arrays.fill(this.mData, arrayOfInt.length, this.mData.length, -1);
      } 
    }
    
    int forceInvalidateAfter(int param1Int) {
      if (this.mFullSpanItems != null)
        for (int i = this.mFullSpanItems.size() - 1; i >= 0; i--) {
          if (((FullSpanItem)this.mFullSpanItems.get(i)).mPosition >= param1Int)
            this.mFullSpanItems.remove(i); 
        }  
      return invalidateAfter(param1Int);
    }
    
    public FullSpanItem getFirstFullSpanItemInRange(int param1Int1, int param1Int2, int param1Int3, boolean param1Boolean) {
      // Byte code:
      //   0: aload_0
      //   1: getfield mFullSpanItems : Ljava/util/List;
      //   4: ifnonnull -> 13
      //   7: aconst_null
      //   8: astore #5
      //   10: aload #5
      //   12: areturn
      //   13: aload_0
      //   14: getfield mFullSpanItems : Ljava/util/List;
      //   17: invokeinterface size : ()I
      //   22: istore #6
      //   24: iconst_0
      //   25: istore #7
      //   27: iload #7
      //   29: iload #6
      //   31: if_icmpge -> 118
      //   34: aload_0
      //   35: getfield mFullSpanItems : Ljava/util/List;
      //   38: iload #7
      //   40: invokeinterface get : (I)Ljava/lang/Object;
      //   45: checkcast android/support/v7/widget/StaggeredGridLayoutManager$LazySpanLookup$FullSpanItem
      //   48: astore #8
      //   50: aload #8
      //   52: getfield mPosition : I
      //   55: iload_2
      //   56: if_icmplt -> 65
      //   59: aconst_null
      //   60: astore #5
      //   62: goto -> 10
      //   65: aload #8
      //   67: getfield mPosition : I
      //   70: iload_1
      //   71: if_icmplt -> 112
      //   74: aload #8
      //   76: astore #5
      //   78: iload_3
      //   79: ifeq -> 10
      //   82: aload #8
      //   84: astore #5
      //   86: aload #8
      //   88: getfield mGapDir : I
      //   91: iload_3
      //   92: if_icmpeq -> 10
      //   95: iload #4
      //   97: ifeq -> 112
      //   100: aload #8
      //   102: astore #5
      //   104: aload #8
      //   106: getfield mHasUnwantedGapAfter : Z
      //   109: ifne -> 10
      //   112: iinc #7, 1
      //   115: goto -> 27
      //   118: aconst_null
      //   119: astore #5
      //   121: goto -> 10
    }
    
    public FullSpanItem getFullSpanItem(int param1Int) {
      // Byte code:
      //   0: aload_0
      //   1: getfield mFullSpanItems : Ljava/util/List;
      //   4: ifnonnull -> 11
      //   7: aconst_null
      //   8: astore_2
      //   9: aload_2
      //   10: areturn
      //   11: aload_0
      //   12: getfield mFullSpanItems : Ljava/util/List;
      //   15: invokeinterface size : ()I
      //   20: iconst_1
      //   21: isub
      //   22: istore_3
      //   23: iload_3
      //   24: iflt -> 60
      //   27: aload_0
      //   28: getfield mFullSpanItems : Ljava/util/List;
      //   31: iload_3
      //   32: invokeinterface get : (I)Ljava/lang/Object;
      //   37: checkcast android/support/v7/widget/StaggeredGridLayoutManager$LazySpanLookup$FullSpanItem
      //   40: astore #4
      //   42: aload #4
      //   44: astore_2
      //   45: aload #4
      //   47: getfield mPosition : I
      //   50: iload_1
      //   51: if_icmpeq -> 9
      //   54: iinc #3, -1
      //   57: goto -> 23
      //   60: aconst_null
      //   61: astore_2
      //   62: goto -> 9
    }
    
    int getSpan(int param1Int) {
      return (this.mData == null || param1Int >= this.mData.length) ? -1 : this.mData[param1Int];
    }
    
    int invalidateAfter(int param1Int) {
      int i = -1;
      if (this.mData != null && param1Int < this.mData.length) {
        i = invalidateFullSpansAfter(param1Int);
        if (i == -1) {
          Arrays.fill(this.mData, param1Int, this.mData.length, -1);
          return this.mData.length;
        } 
        Arrays.fill(this.mData, param1Int, i + 1, -1);
        i++;
      } 
      return i;
    }
    
    void offsetForAddition(int param1Int1, int param1Int2) {
      if (this.mData != null && param1Int1 < this.mData.length) {
        ensureSize(param1Int1 + param1Int2);
        System.arraycopy(this.mData, param1Int1, this.mData, param1Int1 + param1Int2, this.mData.length - param1Int1 - param1Int2);
        Arrays.fill(this.mData, param1Int1, param1Int1 + param1Int2, -1);
        offsetFullSpansForAddition(param1Int1, param1Int2);
      } 
    }
    
    void offsetForRemoval(int param1Int1, int param1Int2) {
      if (this.mData != null && param1Int1 < this.mData.length) {
        ensureSize(param1Int1 + param1Int2);
        System.arraycopy(this.mData, param1Int1 + param1Int2, this.mData, param1Int1, this.mData.length - param1Int1 - param1Int2);
        Arrays.fill(this.mData, this.mData.length - param1Int2, this.mData.length, -1);
        offsetFullSpansForRemoval(param1Int1, param1Int2);
      } 
    }
    
    void setSpan(int param1Int, StaggeredGridLayoutManager.Span param1Span) {
      ensureSize(param1Int);
      this.mData[param1Int] = param1Span.mIndex;
    }
    
    int sizeForPosition(int param1Int) {
      int i;
      for (i = this.mData.length; i <= param1Int; i *= 2);
      return i;
    }
    
    static class FullSpanItem implements Parcelable {
      public static final Parcelable.Creator<FullSpanItem> CREATOR = new Parcelable.Creator<FullSpanItem>() {
          public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem createFromParcel(Parcel param3Parcel) {
            return new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem(param3Parcel);
          }
          
          public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem[] newArray(int param3Int) {
            return new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem[param3Int];
          }
        };
      
      int mGapDir;
      
      int[] mGapPerSpan;
      
      boolean mHasUnwantedGapAfter;
      
      int mPosition;
      
      public FullSpanItem() {}
      
      public FullSpanItem(Parcel param2Parcel) {
        this.mPosition = param2Parcel.readInt();
        this.mGapDir = param2Parcel.readInt();
        if (param2Parcel.readInt() != 1)
          bool = false; 
        this.mHasUnwantedGapAfter = bool;
        int i = param2Parcel.readInt();
        if (i > 0) {
          this.mGapPerSpan = new int[i];
          param2Parcel.readIntArray(this.mGapPerSpan);
        } 
      }
      
      public int describeContents() {
        return 0;
      }
      
      int getGapForSpan(int param2Int) {
        return (this.mGapPerSpan == null) ? 0 : this.mGapPerSpan[param2Int];
      }
      
      public String toString() {
        return "FullSpanItem{mPosition=" + this.mPosition + ", mGapDir=" + this.mGapDir + ", mHasUnwantedGapAfter=" + this.mHasUnwantedGapAfter + ", mGapPerSpan=" + Arrays.toString(this.mGapPerSpan) + '}';
      }
      
      public void writeToParcel(Parcel param2Parcel, int param2Int) {
        param2Parcel.writeInt(this.mPosition);
        param2Parcel.writeInt(this.mGapDir);
        if (this.mHasUnwantedGapAfter) {
          param2Int = 1;
        } else {
          param2Int = 0;
        } 
        param2Parcel.writeInt(param2Int);
        if (this.mGapPerSpan != null && this.mGapPerSpan.length > 0) {
          param2Parcel.writeInt(this.mGapPerSpan.length);
          param2Parcel.writeIntArray(this.mGapPerSpan);
          return;
        } 
        param2Parcel.writeInt(0);
      }
    }
    
    static final class null implements Parcelable.Creator<FullSpanItem> {
      public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem createFromParcel(Parcel param2Parcel) {
        return new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem(param2Parcel);
      }
      
      public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem[] newArray(int param2Int) {
        return new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem[param2Int];
      }
    }
  }
  
  static class FullSpanItem implements Parcelable {
    public static final Parcelable.Creator<FullSpanItem> CREATOR = new Parcelable.Creator<FullSpanItem>() {
        public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem createFromParcel(Parcel param3Parcel) {
          return new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem(param3Parcel);
        }
        
        public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem[] newArray(int param3Int) {
          return new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem[param3Int];
        }
      };
    
    int mGapDir;
    
    int[] mGapPerSpan;
    
    boolean mHasUnwantedGapAfter;
    
    int mPosition;
    
    public FullSpanItem() {}
    
    public FullSpanItem(Parcel param1Parcel) {
      this.mPosition = param1Parcel.readInt();
      this.mGapDir = param1Parcel.readInt();
      if (param1Parcel.readInt() != 1)
        bool = false; 
      this.mHasUnwantedGapAfter = bool;
      int i = param1Parcel.readInt();
      if (i > 0) {
        this.mGapPerSpan = new int[i];
        param1Parcel.readIntArray(this.mGapPerSpan);
      } 
    }
    
    public int describeContents() {
      return 0;
    }
    
    int getGapForSpan(int param1Int) {
      return (this.mGapPerSpan == null) ? 0 : this.mGapPerSpan[param1Int];
    }
    
    public String toString() {
      return "FullSpanItem{mPosition=" + this.mPosition + ", mGapDir=" + this.mGapDir + ", mHasUnwantedGapAfter=" + this.mHasUnwantedGapAfter + ", mGapPerSpan=" + Arrays.toString(this.mGapPerSpan) + '}';
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      param1Parcel.writeInt(this.mPosition);
      param1Parcel.writeInt(this.mGapDir);
      if (this.mHasUnwantedGapAfter) {
        param1Int = 1;
      } else {
        param1Int = 0;
      } 
      param1Parcel.writeInt(param1Int);
      if (this.mGapPerSpan != null && this.mGapPerSpan.length > 0) {
        param1Parcel.writeInt(this.mGapPerSpan.length);
        param1Parcel.writeIntArray(this.mGapPerSpan);
        return;
      } 
      param1Parcel.writeInt(0);
    }
  }
  
  static final class null implements Parcelable.Creator<LazySpanLookup.FullSpanItem> {
    public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem createFromParcel(Parcel param1Parcel) {
      return new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem(param1Parcel);
    }
    
    public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem[] newArray(int param1Int) {
      return new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem[param1Int];
    }
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static class SavedState implements Parcelable {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public StaggeredGridLayoutManager.SavedState createFromParcel(Parcel param2Parcel) {
          return new StaggeredGridLayoutManager.SavedState(param2Parcel);
        }
        
        public StaggeredGridLayoutManager.SavedState[] newArray(int param2Int) {
          return new StaggeredGridLayoutManager.SavedState[param2Int];
        }
      };
    
    boolean mAnchorLayoutFromEnd;
    
    int mAnchorPosition;
    
    List<StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem> mFullSpanItems;
    
    boolean mLastLayoutRTL;
    
    boolean mReverseLayout;
    
    int[] mSpanLookup;
    
    int mSpanLookupSize;
    
    int[] mSpanOffsets;
    
    int mSpanOffsetsSize;
    
    int mVisibleAnchorPosition;
    
    public SavedState() {}
    
    SavedState(Parcel param1Parcel) {
      boolean bool2;
      this.mAnchorPosition = param1Parcel.readInt();
      this.mVisibleAnchorPosition = param1Parcel.readInt();
      this.mSpanOffsetsSize = param1Parcel.readInt();
      if (this.mSpanOffsetsSize > 0) {
        this.mSpanOffsets = new int[this.mSpanOffsetsSize];
        param1Parcel.readIntArray(this.mSpanOffsets);
      } 
      this.mSpanLookupSize = param1Parcel.readInt();
      if (this.mSpanLookupSize > 0) {
        this.mSpanLookup = new int[this.mSpanLookupSize];
        param1Parcel.readIntArray(this.mSpanLookup);
      } 
      if (param1Parcel.readInt() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      this.mReverseLayout = bool2;
      if (param1Parcel.readInt() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      this.mAnchorLayoutFromEnd = bool2;
      if (param1Parcel.readInt() == 1) {
        bool2 = bool1;
      } else {
        bool2 = false;
      } 
      this.mLastLayoutRTL = bool2;
      this.mFullSpanItems = param1Parcel.readArrayList(StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem.class.getClassLoader());
    }
    
    public SavedState(SavedState param1SavedState) {
      this.mSpanOffsetsSize = param1SavedState.mSpanOffsetsSize;
      this.mAnchorPosition = param1SavedState.mAnchorPosition;
      this.mVisibleAnchorPosition = param1SavedState.mVisibleAnchorPosition;
      this.mSpanOffsets = param1SavedState.mSpanOffsets;
      this.mSpanLookupSize = param1SavedState.mSpanLookupSize;
      this.mSpanLookup = param1SavedState.mSpanLookup;
      this.mReverseLayout = param1SavedState.mReverseLayout;
      this.mAnchorLayoutFromEnd = param1SavedState.mAnchorLayoutFromEnd;
      this.mLastLayoutRTL = param1SavedState.mLastLayoutRTL;
      this.mFullSpanItems = param1SavedState.mFullSpanItems;
    }
    
    public int describeContents() {
      return 0;
    }
    
    void invalidateAnchorPositionInfo() {
      this.mSpanOffsets = null;
      this.mSpanOffsetsSize = 0;
      this.mAnchorPosition = -1;
      this.mVisibleAnchorPosition = -1;
    }
    
    void invalidateSpanInfo() {
      this.mSpanOffsets = null;
      this.mSpanOffsetsSize = 0;
      this.mSpanLookupSize = 0;
      this.mSpanLookup = null;
      this.mFullSpanItems = null;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      boolean bool = true;
      param1Parcel.writeInt(this.mAnchorPosition);
      param1Parcel.writeInt(this.mVisibleAnchorPosition);
      param1Parcel.writeInt(this.mSpanOffsetsSize);
      if (this.mSpanOffsetsSize > 0)
        param1Parcel.writeIntArray(this.mSpanOffsets); 
      param1Parcel.writeInt(this.mSpanLookupSize);
      if (this.mSpanLookupSize > 0)
        param1Parcel.writeIntArray(this.mSpanLookup); 
      if (this.mReverseLayout) {
        param1Int = 1;
      } else {
        param1Int = 0;
      } 
      param1Parcel.writeInt(param1Int);
      if (this.mAnchorLayoutFromEnd) {
        param1Int = 1;
      } else {
        param1Int = 0;
      } 
      param1Parcel.writeInt(param1Int);
      if (this.mLastLayoutRTL) {
        param1Int = bool;
      } else {
        param1Int = 0;
      } 
      param1Parcel.writeInt(param1Int);
      param1Parcel.writeList(this.mFullSpanItems);
    }
  }
  
  static final class null implements Parcelable.Creator<SavedState> {
    public StaggeredGridLayoutManager.SavedState createFromParcel(Parcel param1Parcel) {
      return new StaggeredGridLayoutManager.SavedState(param1Parcel);
    }
    
    public StaggeredGridLayoutManager.SavedState[] newArray(int param1Int) {
      return new StaggeredGridLayoutManager.SavedState[param1Int];
    }
  }
  
  class Span {
    static final int INVALID_LINE = -2147483648;
    
    int mCachedEnd = Integer.MIN_VALUE;
    
    int mCachedStart = Integer.MIN_VALUE;
    
    int mDeletedSize = 0;
    
    final int mIndex;
    
    ArrayList<View> mViews = new ArrayList<View>();
    
    Span(int param1Int) {
      this.mIndex = param1Int;
    }
    
    void appendToSpan(View param1View) {
      StaggeredGridLayoutManager.LayoutParams layoutParams = getLayoutParams(param1View);
      layoutParams.mSpan = this;
      this.mViews.add(param1View);
      this.mCachedEnd = Integer.MIN_VALUE;
      if (this.mViews.size() == 1)
        this.mCachedStart = Integer.MIN_VALUE; 
      if (layoutParams.isItemRemoved() || layoutParams.isItemChanged())
        this.mDeletedSize += StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(param1View); 
    }
    
    void cacheReferenceLineAndClear(boolean param1Boolean, int param1Int) {
      int i;
      if (param1Boolean) {
        i = getEndLine(-2147483648);
      } else {
        i = getStartLine(-2147483648);
      } 
      clear();
      if (i != Integer.MIN_VALUE && (!param1Boolean || i >= StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding()) && (param1Boolean || i <= StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding())) {
        int j = i;
        if (param1Int != Integer.MIN_VALUE)
          j = i + param1Int; 
        this.mCachedEnd = j;
        this.mCachedStart = j;
      } 
    }
    
    void calculateCachedEnd() {
      View view = this.mViews.get(this.mViews.size() - 1);
      StaggeredGridLayoutManager.LayoutParams layoutParams = getLayoutParams(view);
      this.mCachedEnd = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedEnd(view);
      if (layoutParams.mFullSpan) {
        StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem fullSpanItem = StaggeredGridLayoutManager.this.mLazySpanLookup.getFullSpanItem(layoutParams.getViewLayoutPosition());
        if (fullSpanItem != null && fullSpanItem.mGapDir == 1)
          this.mCachedEnd += fullSpanItem.getGapForSpan(this.mIndex); 
      } 
    }
    
    void calculateCachedStart() {
      View view = this.mViews.get(0);
      StaggeredGridLayoutManager.LayoutParams layoutParams = getLayoutParams(view);
      this.mCachedStart = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedStart(view);
      if (layoutParams.mFullSpan) {
        StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem fullSpanItem = StaggeredGridLayoutManager.this.mLazySpanLookup.getFullSpanItem(layoutParams.getViewLayoutPosition());
        if (fullSpanItem != null && fullSpanItem.mGapDir == -1)
          this.mCachedStart -= fullSpanItem.getGapForSpan(this.mIndex); 
      } 
    }
    
    void clear() {
      this.mViews.clear();
      invalidateCache();
      this.mDeletedSize = 0;
    }
    
    public int findFirstCompletelyVisibleItemPosition() {
      return StaggeredGridLayoutManager.this.mReverseLayout ? findOneVisibleChild(this.mViews.size() - 1, -1, true) : findOneVisibleChild(0, this.mViews.size(), true);
    }
    
    public int findFirstVisibleItemPosition() {
      return StaggeredGridLayoutManager.this.mReverseLayout ? findOneVisibleChild(this.mViews.size() - 1, -1, false) : findOneVisibleChild(0, this.mViews.size(), false);
    }
    
    public int findLastCompletelyVisibleItemPosition() {
      return StaggeredGridLayoutManager.this.mReverseLayout ? findOneVisibleChild(0, this.mViews.size(), true) : findOneVisibleChild(this.mViews.size() - 1, -1, true);
    }
    
    public int findLastVisibleItemPosition() {
      return StaggeredGridLayoutManager.this.mReverseLayout ? findOneVisibleChild(0, this.mViews.size(), false) : findOneVisibleChild(this.mViews.size() - 1, -1, false);
    }
    
    int findOneVisibleChild(int param1Int1, int param1Int2, boolean param1Boolean) {
      byte b2;
      byte b1 = -1;
      int i = StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding();
      int j = StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding();
      if (param1Int2 > param1Int1) {
        b2 = 1;
      } else {
        b2 = -1;
      } 
      int k;
      for (k = param1Int1;; k += b2) {
        param1Int1 = b1;
        if (k != param1Int2) {
          View view = this.mViews.get(k);
          param1Int1 = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedStart(view);
          int m = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedEnd(view);
          if (param1Int1 < j && m > i)
            if (param1Boolean) {
              if (param1Int1 >= i && m <= j)
                return StaggeredGridLayoutManager.this.getPosition(view); 
            } else {
              return StaggeredGridLayoutManager.this.getPosition(view);
            }  
        } else {
          return param1Int1;
        } 
      } 
    }
    
    public int getDeletedSize() {
      return this.mDeletedSize;
    }
    
    int getEndLine() {
      if (this.mCachedEnd != Integer.MIN_VALUE)
        return this.mCachedEnd; 
      calculateCachedEnd();
      return this.mCachedEnd;
    }
    
    int getEndLine(int param1Int) {
      if (this.mCachedEnd != Integer.MIN_VALUE)
        return this.mCachedEnd; 
      if (this.mViews.size() != 0) {
        calculateCachedEnd();
        param1Int = this.mCachedEnd;
      } 
      return param1Int;
    }
    
    public View getFocusableViewAfter(int param1Int1, int param1Int2) {
      View view1 = null;
      View view2 = null;
      if (param1Int2 == -1) {
        int i = this.mViews.size();
        param1Int2 = 0;
        while (true) {
          view1 = view2;
          if (param1Int2 < i) {
            View view = this.mViews.get(param1Int2);
            view1 = view2;
            if (view.isFocusable()) {
              boolean bool;
              if (StaggeredGridLayoutManager.this.getPosition(view) > param1Int1) {
                bool = true;
              } else {
                bool = false;
              } 
              view1 = view2;
              if (bool == StaggeredGridLayoutManager.this.mReverseLayout) {
                view2 = view;
                param1Int2++;
                continue;
              } 
            } 
          } 
          break;
        } 
      } else {
        param1Int2 = this.mViews.size() - 1;
        view2 = view1;
        while (true) {
          view1 = view2;
          if (param1Int2 >= 0) {
            View view = this.mViews.get(param1Int2);
            view1 = view2;
            if (view.isFocusable()) {
              boolean bool1;
              boolean bool2;
              if (StaggeredGridLayoutManager.this.getPosition(view) > param1Int1) {
                bool1 = true;
              } else {
                bool1 = false;
              } 
              if (!StaggeredGridLayoutManager.this.mReverseLayout) {
                bool2 = true;
              } else {
                bool2 = false;
              } 
              view1 = view2;
              if (bool1 == bool2) {
                view2 = view;
                param1Int2--;
                continue;
              } 
            } 
          } 
          break;
        } 
      } 
      return view1;
    }
    
    StaggeredGridLayoutManager.LayoutParams getLayoutParams(View param1View) {
      return (StaggeredGridLayoutManager.LayoutParams)param1View.getLayoutParams();
    }
    
    int getStartLine() {
      if (this.mCachedStart != Integer.MIN_VALUE)
        return this.mCachedStart; 
      calculateCachedStart();
      return this.mCachedStart;
    }
    
    int getStartLine(int param1Int) {
      if (this.mCachedStart != Integer.MIN_VALUE)
        return this.mCachedStart; 
      if (this.mViews.size() != 0) {
        calculateCachedStart();
        param1Int = this.mCachedStart;
      } 
      return param1Int;
    }
    
    void invalidateCache() {
      this.mCachedStart = Integer.MIN_VALUE;
      this.mCachedEnd = Integer.MIN_VALUE;
    }
    
    void onOffset(int param1Int) {
      if (this.mCachedStart != Integer.MIN_VALUE)
        this.mCachedStart += param1Int; 
      if (this.mCachedEnd != Integer.MIN_VALUE)
        this.mCachedEnd += param1Int; 
    }
    
    void popEnd() {
      int i = this.mViews.size();
      View view = this.mViews.remove(i - 1);
      StaggeredGridLayoutManager.LayoutParams layoutParams = getLayoutParams(view);
      layoutParams.mSpan = null;
      if (layoutParams.isItemRemoved() || layoutParams.isItemChanged())
        this.mDeletedSize -= StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(view); 
      if (i == 1)
        this.mCachedStart = Integer.MIN_VALUE; 
      this.mCachedEnd = Integer.MIN_VALUE;
    }
    
    void popStart() {
      View view = this.mViews.remove(0);
      StaggeredGridLayoutManager.LayoutParams layoutParams = getLayoutParams(view);
      layoutParams.mSpan = null;
      if (this.mViews.size() == 0)
        this.mCachedEnd = Integer.MIN_VALUE; 
      if (layoutParams.isItemRemoved() || layoutParams.isItemChanged())
        this.mDeletedSize -= StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(view); 
      this.mCachedStart = Integer.MIN_VALUE;
    }
    
    void prependToSpan(View param1View) {
      StaggeredGridLayoutManager.LayoutParams layoutParams = getLayoutParams(param1View);
      layoutParams.mSpan = this;
      this.mViews.add(0, param1View);
      this.mCachedStart = Integer.MIN_VALUE;
      if (this.mViews.size() == 1)
        this.mCachedEnd = Integer.MIN_VALUE; 
      if (layoutParams.isItemRemoved() || layoutParams.isItemChanged())
        this.mDeletedSize += StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(param1View); 
    }
    
    void setLine(int param1Int) {
      this.mCachedStart = param1Int;
      this.mCachedEnd = param1Int;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/StaggeredGridLayoutManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */