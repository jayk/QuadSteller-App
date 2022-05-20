package app.lib.pullToRefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

public abstract class PullToRefreshAdapterViewBase<T extends AbsListView> extends PullToRefreshBase<T> implements AbsListView.OnScrollListener {
  private View mEmptyView;
  
  private IndicatorLayout mIndicatorIvBottom;
  
  private IndicatorLayout mIndicatorIvTop;
  
  private boolean mLastItemVisible;
  
  private PullToRefreshBase.OnLastItemVisibleListener mOnLastItemVisibleListener;
  
  private AbsListView.OnScrollListener mOnScrollListener;
  
  private boolean mScrollEmptyView = true;
  
  private boolean mShowIndicator;
  
  public PullToRefreshAdapterViewBase(Context paramContext) {
    super(paramContext);
    ((AbsListView)this.mRefreshableView).setOnScrollListener(this);
  }
  
  public PullToRefreshAdapterViewBase(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    ((AbsListView)this.mRefreshableView).setOnScrollListener(this);
  }
  
  public PullToRefreshAdapterViewBase(Context paramContext, PullToRefreshBase.Mode paramMode) {
    super(paramContext, paramMode);
    ((AbsListView)this.mRefreshableView).setOnScrollListener(this);
  }
  
  public PullToRefreshAdapterViewBase(Context paramContext, PullToRefreshBase.Mode paramMode, PullToRefreshBase.AnimationStyle paramAnimationStyle) {
    super(paramContext, paramMode, paramAnimationStyle);
    ((AbsListView)this.mRefreshableView).setOnScrollListener(this);
  }
  
  private void addIndicatorViews() {
    FrameLayout.LayoutParams layoutParams;
    PullToRefreshBase.Mode mode = getMode();
    FrameLayout frameLayout = getRefreshableViewWrapper();
    if (mode.showHeaderLoadingLayout() && this.mIndicatorIvTop == null) {
      this.mIndicatorIvTop = new IndicatorLayout(getContext(), PullToRefreshBase.Mode.PULL_FROM_START);
      FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(-2, -2);
      layoutParams1.rightMargin = getResources().getDimensionPixelSize(2131362857);
      layoutParams1.gravity = 53;
      frameLayout.addView((View)this.mIndicatorIvTop, (ViewGroup.LayoutParams)layoutParams1);
    } else if (!mode.showHeaderLoadingLayout() && this.mIndicatorIvTop != null) {
      frameLayout.removeView((View)this.mIndicatorIvTop);
      this.mIndicatorIvTop = null;
    } 
    if (mode.showFooterLoadingLayout() && this.mIndicatorIvBottom == null) {
      this.mIndicatorIvBottom = new IndicatorLayout(getContext(), PullToRefreshBase.Mode.PULL_FROM_END);
      layoutParams = new FrameLayout.LayoutParams(-2, -2);
      layoutParams.rightMargin = getResources().getDimensionPixelSize(2131362857);
      layoutParams.gravity = 85;
      frameLayout.addView((View)this.mIndicatorIvBottom, (ViewGroup.LayoutParams)layoutParams);
      return;
    } 
    if (!layoutParams.showFooterLoadingLayout() && this.mIndicatorIvBottom != null) {
      frameLayout.removeView((View)this.mIndicatorIvBottom);
      this.mIndicatorIvBottom = null;
    } 
  }
  
  private static FrameLayout.LayoutParams convertEmptyViewLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    FrameLayout.LayoutParams layoutParams = null;
    if (paramLayoutParams != null) {
      layoutParams = new FrameLayout.LayoutParams(paramLayoutParams);
      if (paramLayoutParams instanceof LinearLayout.LayoutParams) {
        layoutParams.gravity = ((LinearLayout.LayoutParams)paramLayoutParams).gravity;
        return layoutParams;
      } 
    } else {
      return layoutParams;
    } 
    layoutParams.gravity = 17;
    return layoutParams;
  }
  
  private boolean getShowIndicatorInternal() {
    return (this.mShowIndicator && isPullToRefreshEnabled());
  }
  
  private boolean isFirstItemVisible() {
    Adapter adapter = ((AbsListView)this.mRefreshableView).getAdapter();
    if (adapter == null || adapter.isEmpty()) {
      Log.d("PullToRefresh", "isFirstItemVisible. Empty View.");
      return true;
    } 
    if (((AbsListView)this.mRefreshableView).getFirstVisiblePosition() <= 1) {
      View view = ((AbsListView)this.mRefreshableView).getChildAt(0);
      if (view != null)
        return (view.getTop() >= ((AbsListView)this.mRefreshableView).getTop()); 
    } 
    return false;
  }
  
  private boolean isLastItemVisible() {
    Adapter adapter = ((AbsListView)this.mRefreshableView).getAdapter();
    if (adapter == null || adapter.isEmpty()) {
      Log.d("PullToRefresh", "isLastItemVisible. Empty View.");
      return true;
    } 
    int i = ((AbsListView)this.mRefreshableView).getCount() - 1;
    int j = ((AbsListView)this.mRefreshableView).getLastVisiblePosition();
    Log.d("PullToRefresh", "isLastItemVisible. Last Item Position: " + i + " Last Visible Pos: " + j);
    if (j >= i - 1) {
      i = ((AbsListView)this.mRefreshableView).getFirstVisiblePosition();
      View view = ((AbsListView)this.mRefreshableView).getChildAt(j - i);
      if (view != null)
        return (view.getBottom() <= ((AbsListView)this.mRefreshableView).getBottom()); 
    } 
    return false;
  }
  
  private void removeIndicatorViews() {
    if (this.mIndicatorIvTop != null) {
      getRefreshableViewWrapper().removeView((View)this.mIndicatorIvTop);
      this.mIndicatorIvTop = null;
    } 
    if (this.mIndicatorIvBottom != null) {
      getRefreshableViewWrapper().removeView((View)this.mIndicatorIvBottom);
      this.mIndicatorIvBottom = null;
    } 
  }
  
  private void updateIndicatorViewsVisibility() {
    if (this.mIndicatorIvTop != null)
      if (!isRefreshing() && isReadyForPullStart()) {
        if (!this.mIndicatorIvTop.isVisible())
          this.mIndicatorIvTop.show(); 
      } else if (this.mIndicatorIvTop.isVisible()) {
        this.mIndicatorIvTop.hide();
      }  
    if (this.mIndicatorIvBottom != null) {
      if (!isRefreshing() && isReadyForPullEnd()) {
        if (!this.mIndicatorIvBottom.isVisible())
          this.mIndicatorIvBottom.show(); 
        return;
      } 
    } else {
      return;
    } 
    if (this.mIndicatorIvBottom.isVisible())
      this.mIndicatorIvBottom.hide(); 
  }
  
  public boolean getShowIndicator() {
    return this.mShowIndicator;
  }
  
  protected void handleStyledAttributes(TypedArray paramTypedArray) {
    boolean bool;
    if (!isPullToRefreshOverScrollEnabled()) {
      bool = true;
    } else {
      bool = false;
    } 
    this.mShowIndicator = paramTypedArray.getBoolean(5, bool);
  }
  
  protected boolean isReadyForPullEnd() {
    return isLastItemVisible();
  }
  
  protected boolean isReadyForPullStart() {
    return isFirstItemVisible();
  }
  
  protected void onPullToRefresh() {
    super.onPullToRefresh();
    if (getShowIndicatorInternal()) {
      switch (getCurrentMode()) {
        default:
          return;
        case PULL_FROM_END:
          this.mIndicatorIvBottom.pullToRefresh();
        case PULL_FROM_START:
          break;
      } 
      this.mIndicatorIvTop.pullToRefresh();
    } 
  }
  
  protected void onRefreshing(boolean paramBoolean) {
    super.onRefreshing(paramBoolean);
    if (getShowIndicatorInternal())
      updateIndicatorViewsVisibility(); 
  }
  
  protected void onReleaseToRefresh() {
    super.onReleaseToRefresh();
    if (getShowIndicatorInternal()) {
      switch (getCurrentMode()) {
        default:
          return;
        case PULL_FROM_END:
          this.mIndicatorIvBottom.releaseToRefresh();
        case PULL_FROM_START:
          break;
      } 
      this.mIndicatorIvTop.releaseToRefresh();
    } 
  }
  
  protected void onReset() {
    super.onReset();
    if (getShowIndicatorInternal())
      updateIndicatorViewsVisibility(); 
  }
  
  public final void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3) {
    Log.d("PullToRefresh", "First Visible: " + paramInt1 + ". Visible Count: " + paramInt2 + ". Total Items:" + paramInt3);
    if (this.mOnLastItemVisibleListener != null) {
      boolean bool;
      if (paramInt3 > 0 && paramInt1 + paramInt2 >= paramInt3 - 1) {
        bool = true;
      } else {
        bool = false;
      } 
      this.mLastItemVisible = bool;
    } 
    if (getShowIndicatorInternal())
      updateIndicatorViewsVisibility(); 
    if (this.mOnScrollListener != null)
      this.mOnScrollListener.onScroll(paramAbsListView, paramInt1, paramInt2, paramInt3); 
  }
  
  protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (this.mEmptyView != null && !this.mScrollEmptyView)
      this.mEmptyView.scrollTo(-paramInt1, -paramInt2); 
  }
  
  public final void onScrollStateChanged(AbsListView paramAbsListView, int paramInt) {
    if (paramInt == 0 && this.mOnLastItemVisibleListener != null && this.mLastItemVisible)
      this.mOnLastItemVisibleListener.onLastItemVisible(); 
    if (this.mOnScrollListener != null)
      this.mOnScrollListener.onScrollStateChanged(paramAbsListView, paramInt); 
  }
  
  public void setAdapter(ListAdapter paramListAdapter) {
    ((AdapterView)this.mRefreshableView).setAdapter((Adapter)paramListAdapter);
  }
  
  public final void setEmptyView(View paramView) {
    FrameLayout frameLayout = getRefreshableViewWrapper();
    if (paramView != null) {
      paramView.setClickable(true);
      ViewParent viewParent = paramView.getParent();
      if (viewParent != null && viewParent instanceof ViewGroup)
        ((ViewGroup)viewParent).removeView(paramView); 
      FrameLayout.LayoutParams layoutParams = convertEmptyViewLayoutParams(paramView.getLayoutParams());
      if (layoutParams != null) {
        frameLayout.addView(paramView, (ViewGroup.LayoutParams)layoutParams);
      } else {
        frameLayout.addView(paramView);
      } 
    } 
    if (this.mRefreshableView instanceof EmptyViewMethodAccessor) {
      ((EmptyViewMethodAccessor)this.mRefreshableView).setEmptyViewInternal(paramView);
    } else {
      ((AbsListView)this.mRefreshableView).setEmptyView(paramView);
    } 
    this.mEmptyView = paramView;
  }
  
  public void setOnItemClickListener(AdapterView.OnItemClickListener paramOnItemClickListener) {
    ((AbsListView)this.mRefreshableView).setOnItemClickListener(paramOnItemClickListener);
  }
  
  public final void setOnLastItemVisibleListener(PullToRefreshBase.OnLastItemVisibleListener paramOnLastItemVisibleListener) {
    this.mOnLastItemVisibleListener = paramOnLastItemVisibleListener;
  }
  
  public final void setOnScrollListener(AbsListView.OnScrollListener paramOnScrollListener) {
    this.mOnScrollListener = paramOnScrollListener;
  }
  
  public final void setScrollEmptyView(boolean paramBoolean) {
    this.mScrollEmptyView = paramBoolean;
  }
  
  public void setShowIndicator(boolean paramBoolean) {
    this.mShowIndicator = paramBoolean;
    if (getShowIndicatorInternal()) {
      addIndicatorViews();
      return;
    } 
    removeIndicatorViews();
  }
  
  protected void updateUIForMode() {
    super.updateUIForMode();
    if (getShowIndicatorInternal()) {
      addIndicatorViews();
      return;
    } 
    removeIndicatorViews();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/pullToRefresh/PullToRefreshAdapterViewBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */