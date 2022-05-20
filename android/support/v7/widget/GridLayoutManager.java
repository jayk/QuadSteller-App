package android.support.v7.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import java.util.Arrays;

public class GridLayoutManager extends LinearLayoutManager {
  private static final boolean DEBUG = false;
  
  public static final int DEFAULT_SPAN_COUNT = -1;
  
  private static final String TAG = "GridLayoutManager";
  
  int[] mCachedBorders;
  
  final Rect mDecorInsets = new Rect();
  
  boolean mPendingSpanCountChange = false;
  
  final SparseIntArray mPreLayoutSpanIndexCache = new SparseIntArray();
  
  final SparseIntArray mPreLayoutSpanSizeCache = new SparseIntArray();
  
  View[] mSet;
  
  int mSpanCount = -1;
  
  SpanSizeLookup mSpanSizeLookup = new DefaultSpanSizeLookup();
  
  public GridLayoutManager(Context paramContext, int paramInt) {
    super(paramContext);
    setSpanCount(paramInt);
  }
  
  public GridLayoutManager(Context paramContext, int paramInt1, int paramInt2, boolean paramBoolean) {
    super(paramContext, paramInt2, paramBoolean);
    setSpanCount(paramInt1);
  }
  
  public GridLayoutManager(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    setSpanCount((getProperties(paramContext, paramAttributeSet, paramInt1, paramInt2)).spanCount);
  }
  
  private void assignSpans(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt1, int paramInt2, boolean paramBoolean) {
    byte b;
    if (paramBoolean) {
      boolean bool = false;
      b = paramInt1;
      paramInt2 = 1;
      paramInt1 = bool;
    } else {
      paramInt1--;
      b = -1;
      paramInt2 = -1;
    } 
    int i = 0;
    while (paramInt1 != b) {
      View view = this.mSet[paramInt1];
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      layoutParams.mSpanSize = getSpanSize(paramRecycler, paramState, getPosition(view));
      layoutParams.mSpanIndex = i;
      i += layoutParams.mSpanSize;
      paramInt1 += paramInt2;
    } 
  }
  
  private void cachePreLayoutSpanMapping() {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      LayoutParams layoutParams = (LayoutParams)getChildAt(b).getLayoutParams();
      int j = layoutParams.getViewLayoutPosition();
      this.mPreLayoutSpanSizeCache.put(j, layoutParams.getSpanSize());
      this.mPreLayoutSpanIndexCache.put(j, layoutParams.getSpanIndex());
    } 
  }
  
  private void calculateItemBorders(int paramInt) {
    this.mCachedBorders = calculateItemBorders(this.mCachedBorders, this.mSpanCount, paramInt);
  }
  
  static int[] calculateItemBorders(int[] paramArrayOfint, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: ifnull -> 24
    //   4: aload_0
    //   5: arraylength
    //   6: iload_1
    //   7: iconst_1
    //   8: iadd
    //   9: if_icmpne -> 24
    //   12: aload_0
    //   13: astore_3
    //   14: aload_0
    //   15: aload_0
    //   16: arraylength
    //   17: iconst_1
    //   18: isub
    //   19: iaload
    //   20: iload_2
    //   21: if_icmpeq -> 30
    //   24: iload_1
    //   25: iconst_1
    //   26: iadd
    //   27: newarray int
    //   29: astore_3
    //   30: aload_3
    //   31: iconst_0
    //   32: iconst_0
    //   33: iastore
    //   34: iload_2
    //   35: iload_1
    //   36: idiv
    //   37: istore #4
    //   39: iload_2
    //   40: iload_1
    //   41: irem
    //   42: istore #5
    //   44: iconst_0
    //   45: istore #6
    //   47: iconst_0
    //   48: istore_2
    //   49: iconst_1
    //   50: istore #7
    //   52: iload #7
    //   54: iload_1
    //   55: if_icmpgt -> 126
    //   58: iload #4
    //   60: istore #8
    //   62: iload_2
    //   63: iload #5
    //   65: iadd
    //   66: istore #9
    //   68: iload #9
    //   70: istore_2
    //   71: iload #8
    //   73: istore #10
    //   75: iload #9
    //   77: ifle -> 107
    //   80: iload #9
    //   82: istore_2
    //   83: iload #8
    //   85: istore #10
    //   87: iload_1
    //   88: iload #9
    //   90: isub
    //   91: iload #5
    //   93: if_icmpge -> 107
    //   96: iload #8
    //   98: iconst_1
    //   99: iadd
    //   100: istore #10
    //   102: iload #9
    //   104: iload_1
    //   105: isub
    //   106: istore_2
    //   107: iload #6
    //   109: iload #10
    //   111: iadd
    //   112: istore #6
    //   114: aload_3
    //   115: iload #7
    //   117: iload #6
    //   119: iastore
    //   120: iinc #7, 1
    //   123: goto -> 52
    //   126: aload_3
    //   127: areturn
  }
  
  private void clearPreLayoutSpanMappingCache() {
    this.mPreLayoutSpanSizeCache.clear();
    this.mPreLayoutSpanIndexCache.clear();
  }
  
  private void ensureAnchorIsInCorrectSpan(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, LinearLayoutManager.AnchorInfo paramAnchorInfo, int paramInt) {
    int i = 1;
    if (paramInt == 1) {
      paramInt = i;
    } else {
      paramInt = 0;
    } 
    i = getSpanIndex(paramRecycler, paramState, paramAnchorInfo.mPosition);
    if (paramInt != 0) {
      while (i > 0 && paramAnchorInfo.mPosition > 0) {
        paramAnchorInfo.mPosition--;
        i = getSpanIndex(paramRecycler, paramState, paramAnchorInfo.mPosition);
      } 
    } else {
      int j = paramState.getItemCount();
      paramInt = paramAnchorInfo.mPosition;
      while (paramInt < j - 1) {
        int k = getSpanIndex(paramRecycler, paramState, paramInt + 1);
        if (k > i) {
          paramInt++;
          i = k;
        } 
      } 
      paramAnchorInfo.mPosition = paramInt;
    } 
  }
  
  private void ensureViewSet() {
    if (this.mSet == null || this.mSet.length != this.mSpanCount)
      this.mSet = new View[this.mSpanCount]; 
  }
  
  private int getSpanGroupIndex(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt) {
    if (!paramState.isPreLayout())
      return this.mSpanSizeLookup.getSpanGroupIndex(paramInt, this.mSpanCount); 
    int i = paramRecycler.convertPreLayoutPositionToPostLayout(paramInt);
    if (i == -1) {
      Log.w("GridLayoutManager", "Cannot find span size for pre layout position. " + paramInt);
      return 0;
    } 
    return this.mSpanSizeLookup.getSpanGroupIndex(i, this.mSpanCount);
  }
  
  private int getSpanIndex(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt) {
    if (!paramState.isPreLayout())
      return this.mSpanSizeLookup.getCachedSpanIndex(paramInt, this.mSpanCount); 
    int j = this.mPreLayoutSpanIndexCache.get(paramInt, -1);
    int i = j;
    if (j == -1) {
      i = paramRecycler.convertPreLayoutPositionToPostLayout(paramInt);
      if (i == -1) {
        Log.w("GridLayoutManager", "Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:" + paramInt);
        return 0;
      } 
      i = this.mSpanSizeLookup.getCachedSpanIndex(i, this.mSpanCount);
    } 
    return i;
  }
  
  private int getSpanSize(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt) {
    if (!paramState.isPreLayout())
      return this.mSpanSizeLookup.getSpanSize(paramInt); 
    int j = this.mPreLayoutSpanSizeCache.get(paramInt, -1);
    int i = j;
    if (j == -1) {
      i = paramRecycler.convertPreLayoutPositionToPostLayout(paramInt);
      if (i == -1) {
        Log.w("GridLayoutManager", "Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:" + paramInt);
        return 1;
      } 
      i = this.mSpanSizeLookup.getSpanSize(i);
    } 
    return i;
  }
  
  private void guessMeasurement(float paramFloat, int paramInt) {
    calculateItemBorders(Math.max(Math.round(this.mSpanCount * paramFloat), paramInt));
  }
  
  private void measureChild(View paramView, int paramInt, boolean paramBoolean) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    Rect rect = layoutParams.mDecorInsets;
    int i = rect.top + rect.bottom + layoutParams.topMargin + layoutParams.bottomMargin;
    int j = rect.left + rect.right + layoutParams.leftMargin + layoutParams.rightMargin;
    int k = getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
    if (this.mOrientation == 1) {
      j = getChildMeasureSpec(k, paramInt, j, layoutParams.width, false);
      paramInt = getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getHeightMode(), i, layoutParams.height, true);
    } else {
      paramInt = getChildMeasureSpec(k, paramInt, i, layoutParams.height, false);
      j = getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getWidthMode(), j, layoutParams.width, true);
    } 
    measureChildWithDecorationsAndMargin(paramView, j, paramInt, paramBoolean);
  }
  
  private void measureChildWithDecorationsAndMargin(View paramView, int paramInt1, int paramInt2, boolean paramBoolean) {
    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)paramView.getLayoutParams();
    if (paramBoolean) {
      paramBoolean = shouldReMeasureChild(paramView, paramInt1, paramInt2, layoutParams);
    } else {
      paramBoolean = shouldMeasureChild(paramView, paramInt1, paramInt2, layoutParams);
    } 
    if (paramBoolean)
      paramView.measure(paramInt1, paramInt2); 
  }
  
  private void updateMeasurements() {
    int i;
    if (getOrientation() == 1) {
      i = getWidth() - getPaddingRight() - getPaddingLeft();
    } else {
      i = getHeight() - getPaddingBottom() - getPaddingTop();
    } 
    calculateItemBorders(i);
  }
  
  public boolean checkLayoutParams(RecyclerView.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  void collectPrefetchPositionsForLayoutState(RecyclerView.State paramState, LinearLayoutManager.LayoutState paramLayoutState, RecyclerView.LayoutManager.LayoutPrefetchRegistry paramLayoutPrefetchRegistry) {
    int i = this.mSpanCount;
    for (byte b = 0; b < this.mSpanCount && paramLayoutState.hasMore(paramState) && i > 0; b++) {
      int j = paramLayoutState.mCurrentPosition;
      paramLayoutPrefetchRegistry.addPosition(j, paramLayoutState.mScrollingOffset);
      i -= this.mSpanSizeLookup.getSpanSize(j);
      paramLayoutState.mCurrentPosition += paramLayoutState.mItemDirection;
    } 
  }
  
  View findReferenceChild(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt1, int paramInt2, int paramInt3) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual ensureLayoutState : ()V
    //   4: aconst_null
    //   5: astore #6
    //   7: aconst_null
    //   8: astore #7
    //   10: aload_0
    //   11: getfield mOrientationHelper : Landroid/support/v7/widget/OrientationHelper;
    //   14: invokevirtual getStartAfterPadding : ()I
    //   17: istore #8
    //   19: aload_0
    //   20: getfield mOrientationHelper : Landroid/support/v7/widget/OrientationHelper;
    //   23: invokevirtual getEndAfterPadding : ()I
    //   26: istore #9
    //   28: iload #4
    //   30: iload_3
    //   31: if_icmple -> 121
    //   34: iconst_1
    //   35: istore #10
    //   37: iload_3
    //   38: iload #4
    //   40: if_icmpeq -> 221
    //   43: aload_0
    //   44: iload_3
    //   45: invokevirtual getChildAt : (I)Landroid/view/View;
    //   48: astore #11
    //   50: aload_0
    //   51: aload #11
    //   53: invokevirtual getPosition : (Landroid/view/View;)I
    //   56: istore #12
    //   58: aload #6
    //   60: astore #13
    //   62: aload #7
    //   64: astore #14
    //   66: iload #12
    //   68: iflt -> 105
    //   71: aload #6
    //   73: astore #13
    //   75: aload #7
    //   77: astore #14
    //   79: iload #12
    //   81: iload #5
    //   83: if_icmpge -> 105
    //   86: aload_0
    //   87: aload_1
    //   88: aload_2
    //   89: iload #12
    //   91: invokespecial getSpanIndex : (Landroid/support/v7/widget/RecyclerView$Recycler;Landroid/support/v7/widget/RecyclerView$State;I)I
    //   94: ifeq -> 127
    //   97: aload #7
    //   99: astore #14
    //   101: aload #6
    //   103: astore #13
    //   105: iload_3
    //   106: iload #10
    //   108: iadd
    //   109: istore_3
    //   110: aload #13
    //   112: astore #6
    //   114: aload #14
    //   116: astore #7
    //   118: goto -> 37
    //   121: iconst_m1
    //   122: istore #10
    //   124: goto -> 37
    //   127: aload #11
    //   129: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   132: checkcast android/support/v7/widget/RecyclerView$LayoutParams
    //   135: invokevirtual isItemRemoved : ()Z
    //   138: ifeq -> 165
    //   141: aload #6
    //   143: astore #13
    //   145: aload #7
    //   147: astore #14
    //   149: aload #6
    //   151: ifnonnull -> 105
    //   154: aload #11
    //   156: astore #13
    //   158: aload #7
    //   160: astore #14
    //   162: goto -> 105
    //   165: aload_0
    //   166: getfield mOrientationHelper : Landroid/support/v7/widget/OrientationHelper;
    //   169: aload #11
    //   171: invokevirtual getDecoratedStart : (Landroid/view/View;)I
    //   174: iload #9
    //   176: if_icmpge -> 197
    //   179: aload #11
    //   181: astore #13
    //   183: aload_0
    //   184: getfield mOrientationHelper : Landroid/support/v7/widget/OrientationHelper;
    //   187: aload #11
    //   189: invokevirtual getDecoratedEnd : (Landroid/view/View;)I
    //   192: iload #8
    //   194: if_icmpge -> 230
    //   197: aload #6
    //   199: astore #13
    //   201: aload #7
    //   203: astore #14
    //   205: aload #7
    //   207: ifnonnull -> 105
    //   210: aload #6
    //   212: astore #13
    //   214: aload #11
    //   216: astore #14
    //   218: goto -> 105
    //   221: aload #7
    //   223: ifnull -> 233
    //   226: aload #7
    //   228: astore #13
    //   230: aload #13
    //   232: areturn
    //   233: aload #6
    //   235: astore #7
    //   237: goto -> 226
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
    return (this.mOrientation == 1) ? this.mSpanCount : ((paramState.getItemCount() < 1) ? 0 : (getSpanGroupIndex(paramRecycler, paramState, paramState.getItemCount() - 1) + 1));
  }
  
  public int getRowCountForAccessibility(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return (this.mOrientation == 0) ? this.mSpanCount : ((paramState.getItemCount() < 1) ? 0 : (getSpanGroupIndex(paramRecycler, paramState, paramState.getItemCount() - 1) + 1));
  }
  
  int getSpaceForSpanRange(int paramInt1, int paramInt2) {
    return (this.mOrientation == 1 && isLayoutRTL()) ? (this.mCachedBorders[this.mSpanCount - paramInt1] - this.mCachedBorders[this.mSpanCount - paramInt1 - paramInt2]) : (this.mCachedBorders[paramInt1 + paramInt2] - this.mCachedBorders[paramInt1]);
  }
  
  public int getSpanCount() {
    return this.mSpanCount;
  }
  
  public SpanSizeLookup getSpanSizeLookup() {
    return this.mSpanSizeLookup;
  }
  
  void layoutChunk(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, LinearLayoutManager.LayoutState paramLayoutState, LinearLayoutManager.LayoutChunkResult paramLayoutChunkResult) {
    int j;
    int k;
    boolean bool;
    int i = this.mOrientationHelper.getModeInOther();
    if (i != 1073741824) {
      j = 1;
    } else {
      j = 0;
    } 
    if (getChildCount() > 0) {
      k = this.mCachedBorders[this.mSpanCount];
    } else {
      k = 0;
    } 
    if (j)
      updateMeasurements(); 
    if (paramLayoutState.mItemDirection == 1) {
      bool = true;
    } else {
      bool = false;
    } 
    int m = 0;
    int n = 0;
    int i1 = this.mSpanCount;
    int i2 = m;
    int i3 = n;
    if (!bool) {
      i1 = getSpanIndex(paramRecycler, paramState, paramLayoutState.mCurrentPosition) + getSpanSize(paramRecycler, paramState, paramLayoutState.mCurrentPosition);
      i3 = n;
      i2 = m;
    } 
    while (true) {
      if (i2 < this.mSpanCount && paramLayoutState.hasMore(paramState) && i1 > 0) {
        m = paramLayoutState.mCurrentPosition;
        n = getSpanSize(paramRecycler, paramState, m);
        if (n > this.mSpanCount)
          throw new IllegalArgumentException("Item at position " + m + " requires " + n + " spans but GridLayoutManager has only " + this.mSpanCount + " spans."); 
        i1 -= n;
        if (i1 >= 0) {
          View view = paramLayoutState.next(paramRecycler);
          if (view != null) {
            i3 += n;
            this.mSet[i2] = view;
            i2++;
            continue;
          } 
        } 
      } 
      if (i2 == 0) {
        paramLayoutChunkResult.mFinished = true;
        return;
      } 
      i1 = 0;
      float f = 0.0F;
      assignSpans(paramRecycler, paramState, i2, i3, bool);
      n = 0;
      while (n < i2) {
        View view = this.mSet[n];
        if (paramLayoutState.mScrapList == null) {
          if (bool) {
            addView(view);
          } else {
            addView(view, 0);
          } 
        } else if (bool) {
          addDisappearingView(view);
        } else {
          addDisappearingView(view, 0);
        } 
        calculateItemDecorationsForChild(view, this.mDecorInsets);
        measureChild(view, i, false);
        m = this.mOrientationHelper.getDecoratedMeasurement(view);
        i3 = i1;
        if (m > i1)
          i3 = m; 
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        float f1 = 1.0F * this.mOrientationHelper.getDecoratedMeasurementInOther(view) / layoutParams.mSpanSize;
        float f2 = f;
        if (f1 > f)
          f2 = f1; 
        n++;
        i1 = i3;
        f = f2;
      } 
      i3 = i1;
      if (j) {
        guessMeasurement(f, k);
        i1 = 0;
        j = 0;
        while (true) {
          i3 = i1;
          if (j < i2) {
            View view = this.mSet[j];
            measureChild(view, 1073741824, true);
            k = this.mOrientationHelper.getDecoratedMeasurement(view);
            i3 = i1;
            if (k > i1)
              i3 = k; 
            j++;
            i1 = i3;
            continue;
          } 
          break;
        } 
      } 
      for (i1 = 0; i1 < i2; i1++) {
        View view = this.mSet[i1];
        if (this.mOrientationHelper.getDecoratedMeasurement(view) != i3) {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          Rect rect = layoutParams.mDecorInsets;
          k = rect.top + rect.bottom + layoutParams.topMargin + layoutParams.bottomMargin;
          j = rect.left + rect.right + layoutParams.leftMargin + layoutParams.rightMargin;
          n = getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
          if (this.mOrientation == 1) {
            j = getChildMeasureSpec(n, 1073741824, j, layoutParams.width, false);
            k = View.MeasureSpec.makeMeasureSpec(i3 - k, 1073741824);
          } else {
            j = View.MeasureSpec.makeMeasureSpec(i3 - j, 1073741824);
            k = getChildMeasureSpec(n, 1073741824, k, layoutParams.height, false);
          } 
          measureChildWithDecorationsAndMargin(view, j, k, true);
        } 
      } 
      paramLayoutChunkResult.mConsumed = i3;
      i1 = 0;
      n = 0;
      j = 0;
      k = 0;
      if (this.mOrientation == 1) {
        if (paramLayoutState.mLayoutDirection == -1) {
          k = paramLayoutState.mOffset;
          j = k - i3;
          i3 = n;
        } else {
          j = paramLayoutState.mOffset;
          k = j + i3;
          i3 = n;
        } 
      } else if (paramLayoutState.mLayoutDirection == -1) {
        n = paramLayoutState.mOffset;
        i1 = n - i3;
        i3 = n;
      } else {
        i1 = paramLayoutState.mOffset;
        i3 = i1 + i3;
      } 
      n = 0;
      m = i3;
      i3 = j;
      while (n < i2) {
        View view = this.mSet[n];
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (this.mOrientation == 1) {
          if (isLayoutRTL()) {
            j = getPaddingLeft() + this.mCachedBorders[this.mSpanCount - layoutParams.mSpanIndex];
            i1 = j - this.mOrientationHelper.getDecoratedMeasurementInOther(view);
          } else {
            i1 = getPaddingLeft() + this.mCachedBorders[layoutParams.mSpanIndex];
            j = i1 + this.mOrientationHelper.getDecoratedMeasurementInOther(view);
          } 
        } else {
          i3 = getPaddingTop() + this.mCachedBorders[layoutParams.mSpanIndex];
          k = i3 + this.mOrientationHelper.getDecoratedMeasurementInOther(view);
          j = m;
        } 
        layoutDecoratedWithMargins(view, i1, i3, j, k);
        if (layoutParams.isItemRemoved() || layoutParams.isItemChanged())
          paramLayoutChunkResult.mIgnoreConsumed = true; 
        paramLayoutChunkResult.mFocusable |= view.isFocusable();
        n++;
        m = j;
      } 
      Arrays.fill((Object[])this.mSet, (Object)null);
      return;
    } 
  }
  
  void onAnchorReady(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, LinearLayoutManager.AnchorInfo paramAnchorInfo, int paramInt) {
    super.onAnchorReady(paramRecycler, paramState, paramAnchorInfo, paramInt);
    updateMeasurements();
    if (paramState.getItemCount() > 0 && !paramState.isPreLayout())
      ensureAnchorIsInCorrectSpan(paramRecycler, paramState, paramAnchorInfo, paramInt); 
    ensureViewSet();
  }
  
  public View onFocusSearchFailed(View paramView, int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual findContainingItemView : (Landroid/view/View;)Landroid/view/View;
    //   5: astore #5
    //   7: aload #5
    //   9: ifnonnull -> 16
    //   12: aconst_null
    //   13: astore_1
    //   14: aload_1
    //   15: areturn
    //   16: aload #5
    //   18: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   21: checkcast android/support/v7/widget/GridLayoutManager$LayoutParams
    //   24: astore #6
    //   26: aload #6
    //   28: getfield mSpanIndex : I
    //   31: istore #7
    //   33: aload #6
    //   35: getfield mSpanIndex : I
    //   38: aload #6
    //   40: getfield mSpanSize : I
    //   43: iadd
    //   44: istore #8
    //   46: aload_0
    //   47: aload_1
    //   48: iload_2
    //   49: aload_3
    //   50: aload #4
    //   52: invokespecial onFocusSearchFailed : (Landroid/view/View;ILandroid/support/v7/widget/RecyclerView$Recycler;Landroid/support/v7/widget/RecyclerView$State;)Landroid/view/View;
    //   55: ifnonnull -> 63
    //   58: aconst_null
    //   59: astore_1
    //   60: goto -> 14
    //   63: aload_0
    //   64: iload_2
    //   65: invokevirtual convertFocusDirectionToLayoutDirection : (I)I
    //   68: iconst_1
    //   69: if_icmpne -> 159
    //   72: iconst_1
    //   73: istore #9
    //   75: iload #9
    //   77: aload_0
    //   78: getfield mShouldReverseLayout : Z
    //   81: if_icmpeq -> 165
    //   84: iconst_1
    //   85: istore_2
    //   86: iload_2
    //   87: ifeq -> 170
    //   90: aload_0
    //   91: invokevirtual getChildCount : ()I
    //   94: iconst_1
    //   95: isub
    //   96: istore_2
    //   97: iconst_m1
    //   98: istore #10
    //   100: iconst_m1
    //   101: istore #11
    //   103: aload_0
    //   104: getfield mOrientation : I
    //   107: iconst_1
    //   108: if_icmpne -> 184
    //   111: aload_0
    //   112: invokevirtual isLayoutRTL : ()Z
    //   115: ifeq -> 184
    //   118: iconst_1
    //   119: istore #12
    //   121: aconst_null
    //   122: astore_3
    //   123: iconst_m1
    //   124: istore #13
    //   126: iconst_0
    //   127: istore #14
    //   129: iload_2
    //   130: istore #15
    //   132: iload #15
    //   134: iload #11
    //   136: if_icmpeq -> 154
    //   139: aload_0
    //   140: iload #15
    //   142: invokevirtual getChildAt : (I)Landroid/view/View;
    //   145: astore #4
    //   147: aload #4
    //   149: aload #5
    //   151: if_acmpne -> 190
    //   154: aload_3
    //   155: astore_1
    //   156: goto -> 14
    //   159: iconst_0
    //   160: istore #9
    //   162: goto -> 75
    //   165: iconst_0
    //   166: istore_2
    //   167: goto -> 86
    //   170: iconst_0
    //   171: istore_2
    //   172: iconst_1
    //   173: istore #10
    //   175: aload_0
    //   176: invokevirtual getChildCount : ()I
    //   179: istore #11
    //   181: goto -> 103
    //   184: iconst_0
    //   185: istore #12
    //   187: goto -> 121
    //   190: aload #4
    //   192: invokevirtual isFocusable : ()Z
    //   195: ifne -> 208
    //   198: iload #15
    //   200: iload #10
    //   202: iadd
    //   203: istore #15
    //   205: goto -> 132
    //   208: aload #4
    //   210: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   213: checkcast android/support/v7/widget/GridLayoutManager$LayoutParams
    //   216: astore #6
    //   218: aload #6
    //   220: getfield mSpanIndex : I
    //   223: istore #16
    //   225: aload #6
    //   227: getfield mSpanIndex : I
    //   230: aload #6
    //   232: getfield mSpanSize : I
    //   235: iadd
    //   236: istore #17
    //   238: iload #16
    //   240: iload #7
    //   242: if_icmpne -> 255
    //   245: aload #4
    //   247: astore_1
    //   248: iload #17
    //   250: iload #8
    //   252: if_icmpeq -> 14
    //   255: iconst_0
    //   256: istore #18
    //   258: aload_3
    //   259: ifnonnull -> 298
    //   262: iconst_1
    //   263: istore_2
    //   264: iload_2
    //   265: ifeq -> 198
    //   268: aload #4
    //   270: astore_3
    //   271: aload #6
    //   273: getfield mSpanIndex : I
    //   276: istore #13
    //   278: iload #17
    //   280: iload #8
    //   282: invokestatic min : (II)I
    //   285: iload #16
    //   287: iload #7
    //   289: invokestatic max : (II)I
    //   292: isub
    //   293: istore #14
    //   295: goto -> 198
    //   298: iload #16
    //   300: iload #7
    //   302: invokestatic max : (II)I
    //   305: istore_2
    //   306: iload #17
    //   308: iload #8
    //   310: invokestatic min : (II)I
    //   313: iload_2
    //   314: isub
    //   315: istore #19
    //   317: iload #19
    //   319: iload #14
    //   321: if_icmple -> 329
    //   324: iconst_1
    //   325: istore_2
    //   326: goto -> 264
    //   329: iload #18
    //   331: istore_2
    //   332: iload #19
    //   334: iload #14
    //   336: if_icmpne -> 264
    //   339: iload #16
    //   341: iload #13
    //   343: if_icmple -> 364
    //   346: iconst_1
    //   347: istore #19
    //   349: iload #18
    //   351: istore_2
    //   352: iload #12
    //   354: iload #19
    //   356: if_icmpne -> 264
    //   359: iconst_1
    //   360: istore_2
    //   361: goto -> 264
    //   364: iconst_0
    //   365: istore #19
    //   367: goto -> 349
  }
  
  public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat) {
    boolean bool;
    ViewGroup.LayoutParams layoutParams1 = paramView.getLayoutParams();
    if (!(layoutParams1 instanceof LayoutParams)) {
      onInitializeAccessibilityNodeInfoForItem(paramView, paramAccessibilityNodeInfoCompat);
      return;
    } 
    LayoutParams layoutParams = (LayoutParams)layoutParams1;
    int i = getSpanGroupIndex(paramRecycler, paramState, layoutParams.getViewLayoutPosition());
    if (this.mOrientation == 0) {
      int m = layoutParams.getSpanIndex();
      int n = layoutParams.getSpanSize();
      if (this.mSpanCount > 1 && layoutParams.getSpanSize() == this.mSpanCount) {
        bool = true;
      } else {
        bool = false;
      } 
      paramAccessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(m, n, i, 1, bool, false));
      return;
    } 
    int j = layoutParams.getSpanIndex();
    int k = layoutParams.getSpanSize();
    if (this.mSpanCount > 1 && layoutParams.getSpanSize() == this.mSpanCount) {
      bool = true;
    } else {
      bool = false;
    } 
    paramAccessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(i, 1, j, k, bool, false));
  }
  
  public void onItemsAdded(RecyclerView paramRecyclerView, int paramInt1, int paramInt2) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
  }
  
  public void onItemsChanged(RecyclerView paramRecyclerView) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
  }
  
  public void onItemsMoved(RecyclerView paramRecyclerView, int paramInt1, int paramInt2, int paramInt3) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
  }
  
  public void onItemsRemoved(RecyclerView paramRecyclerView, int paramInt1, int paramInt2) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
  }
  
  public void onItemsUpdated(RecyclerView paramRecyclerView, int paramInt1, int paramInt2, Object paramObject) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
  }
  
  public void onLayoutChildren(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    if (paramState.isPreLayout())
      cachePreLayoutSpanMapping(); 
    super.onLayoutChildren(paramRecycler, paramState);
    clearPreLayoutSpanMappingCache();
  }
  
  public void onLayoutCompleted(RecyclerView.State paramState) {
    super.onLayoutCompleted(paramState);
    this.mPendingSpanCountChange = false;
  }
  
  public int scrollHorizontallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    updateMeasurements();
    ensureViewSet();
    return super.scrollHorizontallyBy(paramInt, paramRecycler, paramState);
  }
  
  public int scrollVerticallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    updateMeasurements();
    ensureViewSet();
    return super.scrollVerticallyBy(paramInt, paramRecycler, paramState);
  }
  
  public void setMeasuredDimension(Rect paramRect, int paramInt1, int paramInt2) {
    if (this.mCachedBorders == null)
      super.setMeasuredDimension(paramRect, paramInt1, paramInt2); 
    int i = getPaddingLeft() + getPaddingRight();
    int j = getPaddingTop() + getPaddingBottom();
    if (this.mOrientation == 1) {
      int k = chooseSize(paramInt2, paramRect.height() + j, getMinimumHeight());
      paramInt2 = chooseSize(paramInt1, this.mCachedBorders[this.mCachedBorders.length - 1] + i, getMinimumWidth());
      paramInt1 = k;
    } else {
      int k = chooseSize(paramInt1, paramRect.width() + i, getMinimumWidth());
      paramInt1 = chooseSize(paramInt2, this.mCachedBorders[this.mCachedBorders.length - 1] + j, getMinimumHeight());
      paramInt2 = k;
    } 
    setMeasuredDimension(paramInt2, paramInt1);
  }
  
  public void setSpanCount(int paramInt) {
    if (paramInt != this.mSpanCount) {
      this.mPendingSpanCountChange = true;
      if (paramInt < 1)
        throw new IllegalArgumentException("Span count should be at least 1. Provided " + paramInt); 
      this.mSpanCount = paramInt;
      this.mSpanSizeLookup.invalidateSpanIndexCache();
      requestLayout();
    } 
  }
  
  public void setSpanSizeLookup(SpanSizeLookup paramSpanSizeLookup) {
    this.mSpanSizeLookup = paramSpanSizeLookup;
  }
  
  public void setStackFromEnd(boolean paramBoolean) {
    if (paramBoolean)
      throw new UnsupportedOperationException("GridLayoutManager does not support stack from end. Consider using reverse layout"); 
    super.setStackFromEnd(false);
  }
  
  public boolean supportsPredictiveItemAnimations() {
    return (this.mPendingSavedState == null && !this.mPendingSpanCountChange);
  }
  
  public static final class DefaultSpanSizeLookup extends SpanSizeLookup {
    public int getSpanIndex(int param1Int1, int param1Int2) {
      return param1Int1 % param1Int2;
    }
    
    public int getSpanSize(int param1Int) {
      return 1;
    }
  }
  
  public static class LayoutParams extends RecyclerView.LayoutParams {
    public static final int INVALID_SPAN_ID = -1;
    
    int mSpanIndex = -1;
    
    int mSpanSize = 0;
    
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
    
    public int getSpanIndex() {
      return this.mSpanIndex;
    }
    
    public int getSpanSize() {
      return this.mSpanSize;
    }
  }
  
  public static abstract class SpanSizeLookup {
    private boolean mCacheSpanIndices = false;
    
    final SparseIntArray mSpanIndexCache = new SparseIntArray();
    
    int findReferenceIndexFromCache(int param1Int) {
      int i = 0;
      for (int j = this.mSpanIndexCache.size() - 1; i <= j; j = k - 1) {
        int k = i + j >>> 1;
        if (this.mSpanIndexCache.keyAt(k) < param1Int) {
          i = k + 1;
          continue;
        } 
      } 
      param1Int = i - 1;
      return (param1Int >= 0 && param1Int < this.mSpanIndexCache.size()) ? this.mSpanIndexCache.keyAt(param1Int) : -1;
    }
    
    int getCachedSpanIndex(int param1Int1, int param1Int2) {
      if (!this.mCacheSpanIndices)
        return getSpanIndex(param1Int1, param1Int2); 
      int j = this.mSpanIndexCache.get(param1Int1, -1);
      int i = j;
      if (j == -1) {
        i = getSpanIndex(param1Int1, param1Int2);
        this.mSpanIndexCache.put(param1Int1, i);
      } 
      return i;
    }
    
    public int getSpanGroupIndex(int param1Int1, int param1Int2) {
      int i = 0;
      int j = 0;
      int k = getSpanSize(param1Int1);
      byte b = 0;
      while (b < param1Int1) {
        int i1;
        int m = getSpanSize(b);
        int n = i + m;
        if (n == param1Int2) {
          i = 0;
          i1 = j + 1;
        } else {
          i1 = j;
          i = n;
          if (n > param1Int2) {
            i = m;
            i1 = j + 1;
          } 
        } 
        b++;
        j = i1;
      } 
      param1Int1 = j;
      if (i + k > param1Int2)
        param1Int1 = j + 1; 
      return param1Int1;
    }
    
    public int getSpanIndex(int param1Int1, int param1Int2) {
      int i = getSpanSize(param1Int1);
      if (i == param1Int2)
        return 0; 
      int j = 0;
      int k = 0;
      int m = j;
      int n = k;
      if (this.mCacheSpanIndices) {
        m = j;
        n = k;
        if (this.mSpanIndexCache.size() > 0) {
          int i1 = findReferenceIndexFromCache(param1Int1);
          m = j;
          n = k;
          if (i1 >= 0) {
            m = this.mSpanIndexCache.get(i1) + getSpanSize(i1);
            n = i1 + 1;
          } 
        } 
      } 
      while (n < param1Int1) {
        j = getSpanSize(n);
        k = m + j;
        if (k == param1Int2) {
          m = 0;
        } else {
          m = k;
          if (k > param1Int2)
            m = j; 
        } 
        n++;
      } 
      param1Int1 = m;
      if (m + i > param1Int2)
        param1Int1 = 0; 
      return param1Int1;
    }
    
    public abstract int getSpanSize(int param1Int);
    
    public void invalidateSpanIndexCache() {
      this.mSpanIndexCache.clear();
    }
    
    public boolean isSpanIndexCacheEnabled() {
      return this.mCacheSpanIndices;
    }
    
    public void setSpanIndexCacheEnabled(boolean param1Boolean) {
      this.mCacheSpanIndices = param1Boolean;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/GridLayoutManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */