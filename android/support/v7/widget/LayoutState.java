package android.support.v7.widget;

import android.view.View;

class LayoutState {
  static final int INVALID_LAYOUT = -2147483648;
  
  static final int ITEM_DIRECTION_HEAD = -1;
  
  static final int ITEM_DIRECTION_TAIL = 1;
  
  static final int LAYOUT_END = 1;
  
  static final int LAYOUT_START = -1;
  
  static final String TAG = "LayoutState";
  
  int mAvailable;
  
  int mCurrentPosition;
  
  int mEndLine = 0;
  
  boolean mInfinite;
  
  int mItemDirection;
  
  int mLayoutDirection;
  
  boolean mRecycle = true;
  
  int mStartLine = 0;
  
  boolean mStopInFocusable;
  
  boolean hasMore(RecyclerView.State paramState) {
    return (this.mCurrentPosition >= 0 && this.mCurrentPosition < paramState.getItemCount());
  }
  
  View next(RecyclerView.Recycler paramRecycler) {
    View view = paramRecycler.getViewForPosition(this.mCurrentPosition);
    this.mCurrentPosition += this.mItemDirection;
    return view;
  }
  
  public String toString() {
    return "LayoutState{mAvailable=" + this.mAvailable + ", mCurrentPosition=" + this.mCurrentPosition + ", mItemDirection=" + this.mItemDirection + ", mLayoutDirection=" + this.mLayoutDirection + ", mStartLine=" + this.mStartLine + ", mEndLine=" + this.mEndLine + '}';
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/LayoutState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */