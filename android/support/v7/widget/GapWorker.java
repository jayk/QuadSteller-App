package android.support.v7.widget;

import android.support.annotation.Nullable;
import android.support.v4.os.TraceCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

final class GapWorker implements Runnable {
  static final ThreadLocal<GapWorker> sGapWorker = new ThreadLocal<GapWorker>();
  
  static Comparator<Task> sTaskComparator = new Comparator<Task>() {
      public int compare(GapWorker.Task param1Task1, GapWorker.Task param1Task2) {
        byte b3;
        byte b1 = -1;
        byte b2 = 1;
        if (param1Task1.view == null) {
          i = 1;
        } else {
          i = 0;
        } 
        if (param1Task2.view == null) {
          b3 = 1;
        } else {
          b3 = 0;
        } 
        if (i != b3)
          return (param1Task1.view == null) ? b2 : -1; 
        if (param1Task1.immediate != param1Task2.immediate)
          return param1Task1.immediate ? b1 : 1; 
        int i = param1Task2.viewVelocity - param1Task1.viewVelocity;
        if (i == 0) {
          i = param1Task1.distanceToItem - param1Task2.distanceToItem;
          if (i == 0)
            i = 0; 
        } 
        return i;
      }
    };
  
  long mFrameIntervalNs;
  
  long mPostTimeNs;
  
  ArrayList<RecyclerView> mRecyclerViews = new ArrayList<RecyclerView>();
  
  private ArrayList<Task> mTasks = new ArrayList<Task>();
  
  private void buildTaskList() {
    int i = this.mRecyclerViews.size();
    int j = 0;
    byte b;
    for (b = 0; b < i; b++) {
      RecyclerView recyclerView = this.mRecyclerViews.get(b);
      recyclerView.mPrefetchRegistry.collectPrefetchPositionsFromView(recyclerView, false);
      j += recyclerView.mPrefetchRegistry.mCount;
    } 
    this.mTasks.ensureCapacity(j);
    b = 0;
    for (j = 0; j < i; j++) {
      RecyclerView recyclerView = this.mRecyclerViews.get(j);
      LayoutPrefetchRegistryImpl layoutPrefetchRegistryImpl = recyclerView.mPrefetchRegistry;
      int k = Math.abs(layoutPrefetchRegistryImpl.mPrefetchDx) + Math.abs(layoutPrefetchRegistryImpl.mPrefetchDy);
      for (byte b1 = 0; b1 < layoutPrefetchRegistryImpl.mCount * 2; b1 += 2) {
        Task task;
        boolean bool;
        if (b >= this.mTasks.size()) {
          task = new Task();
          this.mTasks.add(task);
        } else {
          task = this.mTasks.get(b);
        } 
        int m = layoutPrefetchRegistryImpl.mPrefetchArray[b1 + 1];
        if (m <= k) {
          bool = true;
        } else {
          bool = false;
        } 
        task.immediate = bool;
        task.viewVelocity = k;
        task.distanceToItem = m;
        task.view = recyclerView;
        task.position = layoutPrefetchRegistryImpl.mPrefetchArray[b1];
        b++;
      } 
    } 
    Collections.sort(this.mTasks, sTaskComparator);
  }
  
  private void flushTaskWithDeadline(Task paramTask, long paramLong) {
    long l;
    if (paramTask.immediate) {
      l = Long.MAX_VALUE;
    } else {
      l = paramLong;
    } 
    RecyclerView.ViewHolder viewHolder = prefetchPositionWithDeadline(paramTask.view, paramTask.position, l);
    if (viewHolder != null && viewHolder.mNestedRecyclerView != null)
      prefetchInnerRecyclerViewWithDeadline(viewHolder.mNestedRecyclerView.get(), paramLong); 
  }
  
  private void flushTasksWithDeadline(long paramLong) {
    byte b = 0;
    while (true) {
      if (b < this.mTasks.size()) {
        Task task = this.mTasks.get(b);
        if (task.view != null) {
          flushTaskWithDeadline(task, paramLong);
          task.clear();
          b++;
          continue;
        } 
      } 
      return;
    } 
  }
  
  static boolean isPrefetchPositionAttached(RecyclerView paramRecyclerView, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mChildHelper : Landroid/support/v7/widget/ChildHelper;
    //   4: invokevirtual getUnfilteredChildCount : ()I
    //   7: istore_2
    //   8: iconst_0
    //   9: istore_3
    //   10: iload_3
    //   11: iload_2
    //   12: if_icmpge -> 57
    //   15: aload_0
    //   16: getfield mChildHelper : Landroid/support/v7/widget/ChildHelper;
    //   19: iload_3
    //   20: invokevirtual getUnfilteredChildAt : (I)Landroid/view/View;
    //   23: invokestatic getChildViewHolderInt : (Landroid/view/View;)Landroid/support/v7/widget/RecyclerView$ViewHolder;
    //   26: astore #4
    //   28: aload #4
    //   30: getfield mPosition : I
    //   33: iload_1
    //   34: if_icmpne -> 51
    //   37: aload #4
    //   39: invokevirtual isInvalid : ()Z
    //   42: ifne -> 51
    //   45: iconst_1
    //   46: istore #5
    //   48: iload #5
    //   50: ireturn
    //   51: iinc #3, 1
    //   54: goto -> 10
    //   57: iconst_0
    //   58: istore #5
    //   60: goto -> 48
  }
  
  private void prefetchInnerRecyclerViewWithDeadline(@Nullable RecyclerView paramRecyclerView, long paramLong) {
    if (paramRecyclerView != null) {
      if (paramRecyclerView.mDataSetHasChangedAfterLayout && paramRecyclerView.mChildHelper.getUnfilteredChildCount() != 0)
        paramRecyclerView.removeAndRecycleViews(); 
      LayoutPrefetchRegistryImpl layoutPrefetchRegistryImpl = paramRecyclerView.mPrefetchRegistry;
      layoutPrefetchRegistryImpl.collectPrefetchPositionsFromView(paramRecyclerView, true);
      if (layoutPrefetchRegistryImpl.mCount != 0)
        try {
          TraceCompat.beginSection("RV Nested Prefetch");
          paramRecyclerView.mState.prepareForNestedPrefetch(paramRecyclerView.mAdapter);
          for (byte b = 0; b < layoutPrefetchRegistryImpl.mCount * 2; b += 2)
            prefetchPositionWithDeadline(paramRecyclerView, layoutPrefetchRegistryImpl.mPrefetchArray[b], paramLong); 
          return;
        } finally {
          TraceCompat.endSection();
        }  
    } 
  }
  
  private RecyclerView.ViewHolder prefetchPositionWithDeadline(RecyclerView paramRecyclerView, int paramInt, long paramLong) {
    if (isPrefetchPositionAttached(paramRecyclerView, paramInt))
      return null; 
    RecyclerView.Recycler recycler = paramRecyclerView.mRecycler;
    RecyclerView.ViewHolder viewHolder2 = recycler.tryGetViewHolderForPositionByDeadline(paramInt, false, paramLong);
    RecyclerView.ViewHolder viewHolder1 = viewHolder2;
    if (viewHolder2 != null) {
      if (viewHolder2.isBound()) {
        recycler.recycleView(viewHolder2.itemView);
        return viewHolder2;
      } 
      recycler.addViewHolderToRecycledViewPool(viewHolder2, false);
      viewHolder1 = viewHolder2;
    } 
    return viewHolder1;
  }
  
  public void add(RecyclerView paramRecyclerView) {
    this.mRecyclerViews.add(paramRecyclerView);
  }
  
  void postFromTraversal(RecyclerView paramRecyclerView, int paramInt1, int paramInt2) {
    if (paramRecyclerView.isAttachedToWindow() && this.mPostTimeNs == 0L) {
      this.mPostTimeNs = paramRecyclerView.getNanoTime();
      paramRecyclerView.post(this);
    } 
    paramRecyclerView.mPrefetchRegistry.setPrefetchVector(paramInt1, paramInt2);
  }
  
  void prefetch(long paramLong) {
    buildTaskList();
    flushTasksWithDeadline(paramLong);
  }
  
  public void remove(RecyclerView paramRecyclerView) {
    this.mRecyclerViews.remove(paramRecyclerView);
  }
  
  public void run() {
    try {
      TraceCompat.beginSection("RV Prefetch");
      boolean bool = this.mRecyclerViews.isEmpty();
      if (bool)
        return; 
      long l = TimeUnit.MILLISECONDS.toNanos(((RecyclerView)this.mRecyclerViews.get(0)).getDrawingTime());
      if (l == 0L)
        return; 
      prefetch(l + this.mFrameIntervalNs);
      return;
    } finally {
      this.mPostTimeNs = 0L;
      TraceCompat.endSection();
    } 
  }
  
  static class LayoutPrefetchRegistryImpl implements RecyclerView.LayoutManager.LayoutPrefetchRegistry {
    int mCount;
    
    int[] mPrefetchArray;
    
    int mPrefetchDx;
    
    int mPrefetchDy;
    
    public void addPosition(int param1Int1, int param1Int2) {
      if (param1Int2 < 0)
        throw new IllegalArgumentException("Pixel distance must be non-negative"); 
      int i = this.mCount * 2;
      if (this.mPrefetchArray == null) {
        this.mPrefetchArray = new int[4];
        Arrays.fill(this.mPrefetchArray, -1);
      } else if (i >= this.mPrefetchArray.length) {
        int[] arrayOfInt = this.mPrefetchArray;
        this.mPrefetchArray = new int[i * 2];
        System.arraycopy(arrayOfInt, 0, this.mPrefetchArray, 0, arrayOfInt.length);
      } 
      this.mPrefetchArray[i] = param1Int1;
      this.mPrefetchArray[i + 1] = param1Int2;
      this.mCount++;
    }
    
    void clearPrefetchPositions() {
      if (this.mPrefetchArray != null)
        Arrays.fill(this.mPrefetchArray, -1); 
    }
    
    void collectPrefetchPositionsFromView(RecyclerView param1RecyclerView, boolean param1Boolean) {
      this.mCount = 0;
      if (this.mPrefetchArray != null)
        Arrays.fill(this.mPrefetchArray, -1); 
      RecyclerView.LayoutManager layoutManager = param1RecyclerView.mLayout;
      if (param1RecyclerView.mAdapter != null && layoutManager != null && layoutManager.isItemPrefetchEnabled()) {
        if (param1Boolean) {
          if (!param1RecyclerView.mAdapterHelper.hasPendingUpdates())
            layoutManager.collectInitialPrefetchPositions(param1RecyclerView.mAdapter.getItemCount(), this); 
        } else if (!param1RecyclerView.hasPendingAdapterUpdates()) {
          layoutManager.collectAdjacentPrefetchPositions(this.mPrefetchDx, this.mPrefetchDy, param1RecyclerView.mState, this);
        } 
        if (this.mCount > layoutManager.mPrefetchMaxCountObserved) {
          layoutManager.mPrefetchMaxCountObserved = this.mCount;
          layoutManager.mPrefetchMaxObservedInInitialPrefetch = param1Boolean;
          param1RecyclerView.mRecycler.updateViewCacheSize();
        } 
      } 
    }
    
    boolean lastPrefetchIncludedPosition(int param1Int) {
      int i;
      byte b;
      if (this.mPrefetchArray != null) {
        i = this.mCount;
        for (b = 0; b < i * 2; b += 2) {
          if (this.mPrefetchArray[b] == param1Int)
            return true; 
        } 
      } 
      boolean bool = false;
      while (b < i * 2) {
        if (this.mPrefetchArray[b] == param1Int)
          return true; 
        b += 2;
      } 
    }
    
    void setPrefetchVector(int param1Int1, int param1Int2) {
      this.mPrefetchDx = param1Int1;
      this.mPrefetchDy = param1Int2;
    }
  }
  
  static class Task {
    public int distanceToItem;
    
    public boolean immediate;
    
    public int position;
    
    public RecyclerView view;
    
    public int viewVelocity;
    
    public void clear() {
      this.immediate = false;
      this.viewVelocity = 0;
      this.distanceToItem = 0;
      this.view = null;
      this.position = 0;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/GapWorker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */