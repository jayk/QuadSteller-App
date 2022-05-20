package app.lib.pullToRefresh;

import android.view.animation.Interpolator;

public interface IPullToRefresh<T extends android.view.View> {
  boolean demo();
  
  PullToRefreshBase.Mode getCurrentMode();
  
  boolean getFilterTouchEvents();
  
  ILoadingLayout getLoadingLayoutProxy();
  
  ILoadingLayout getLoadingLayoutProxy(boolean paramBoolean1, boolean paramBoolean2);
  
  PullToRefreshBase.Mode getMode();
  
  T getRefreshableView();
  
  boolean getShowViewWhileRefreshing();
  
  PullToRefreshBase.State getState();
  
  boolean isPullToRefreshEnabled();
  
  boolean isPullToRefreshOverScrollEnabled();
  
  boolean isRefreshing();
  
  boolean isScrollingWhileRefreshingEnabled();
  
  void onRefreshComplete();
  
  void setFilterTouchEvents(boolean paramBoolean);
  
  void setMode(PullToRefreshBase.Mode paramMode);
  
  void setOnPullEventListener(PullToRefreshBase.OnPullEventListener<T> paramOnPullEventListener);
  
  void setOnRefreshListener(PullToRefreshBase.OnRefreshListener2<T> paramOnRefreshListener2);
  
  void setOnRefreshListener(PullToRefreshBase.OnRefreshListener<T> paramOnRefreshListener);
  
  void setPullToRefreshOverScrollEnabled(boolean paramBoolean);
  
  void setRefreshing();
  
  void setRefreshing(boolean paramBoolean);
  
  void setScrollAnimationInterpolator(Interpolator paramInterpolator);
  
  void setScrollingWhileRefreshingEnabled(boolean paramBoolean);
  
  void setShowViewWhileRefreshing(boolean paramBoolean);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/pullToRefresh/IPullToRefresh.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */