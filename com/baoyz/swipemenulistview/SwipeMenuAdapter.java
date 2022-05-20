package com.baoyz.swipemenulistview;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

public class SwipeMenuAdapter implements WrapperListAdapter, SwipeMenuView.OnSwipeItemClickListener {
  private ListAdapter mAdapter;
  
  private Context mContext;
  
  private SwipeMenuListView.OnMenuItemClickListener onMenuItemClickListener;
  
  public SwipeMenuAdapter(Context paramContext, ListAdapter paramListAdapter) {
    this.mAdapter = paramListAdapter;
    this.mContext = paramContext;
  }
  
  public boolean areAllItemsEnabled() {
    return this.mAdapter.areAllItemsEnabled();
  }
  
  public void createMenu(SwipeMenu paramSwipeMenu) {
    SwipeMenuItem swipeMenuItem = new SwipeMenuItem(this.mContext);
    swipeMenuItem.setTitle("Item 1");
    swipeMenuItem.setBackground((Drawable)new ColorDrawable(-7829368));
    swipeMenuItem.setWidth(300);
    paramSwipeMenu.addMenuItem(swipeMenuItem);
    swipeMenuItem = new SwipeMenuItem(this.mContext);
    swipeMenuItem.setTitle("Item 2");
    swipeMenuItem.setBackground((Drawable)new ColorDrawable(-65536));
    swipeMenuItem.setWidth(300);
    paramSwipeMenu.addMenuItem(swipeMenuItem);
  }
  
  public int getCount() {
    return this.mAdapter.getCount();
  }
  
  public Object getItem(int paramInt) {
    return this.mAdapter.getItem(paramInt);
  }
  
  public long getItemId(int paramInt) {
    return this.mAdapter.getItemId(paramInt);
  }
  
  public int getItemViewType(int paramInt) {
    return this.mAdapter.getItemViewType(paramInt);
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    SwipeMenuListView swipeMenuListView;
    if (paramView == null) {
      paramView = this.mAdapter.getView(paramInt, paramView, paramViewGroup);
      SwipeMenu swipeMenu = new SwipeMenu(this.mContext);
      swipeMenu.setViewType(this.mAdapter.getItemViewType(paramInt));
      createMenu(swipeMenu);
      SwipeMenuView swipeMenuView = new SwipeMenuView(swipeMenu, (SwipeMenuListView)paramViewGroup);
      swipeMenuView.setOnSwipeItemClickListener(this);
      swipeMenuListView = (SwipeMenuListView)paramViewGroup;
      swipeMenuLayout = new SwipeMenuLayout(paramView, swipeMenuView, swipeMenuListView.getCloseInterpolator(), swipeMenuListView.getOpenInterpolator());
      swipeMenuLayout.setPosition(paramInt);
      return (View)swipeMenuLayout;
    } 
    SwipeMenuLayout swipeMenuLayout = swipeMenuLayout;
    swipeMenuLayout.closeMenu();
    swipeMenuLayout.setPosition(paramInt);
    this.mAdapter.getView(paramInt, swipeMenuLayout.getContentView(), (ViewGroup)swipeMenuListView);
    return (View)swipeMenuLayout;
  }
  
  public int getViewTypeCount() {
    return this.mAdapter.getViewTypeCount();
  }
  
  public ListAdapter getWrappedAdapter() {
    return this.mAdapter;
  }
  
  public boolean hasStableIds() {
    return this.mAdapter.hasStableIds();
  }
  
  public boolean isEmpty() {
    return this.mAdapter.isEmpty();
  }
  
  public boolean isEnabled(int paramInt) {
    return this.mAdapter.isEnabled(paramInt);
  }
  
  public void onItemClick(SwipeMenuView paramSwipeMenuView, SwipeMenu paramSwipeMenu, int paramInt) {
    if (this.onMenuItemClickListener != null)
      this.onMenuItemClickListener.onMenuItemClick(paramSwipeMenuView.getPosition(), paramSwipeMenu, paramInt); 
  }
  
  public void registerDataSetObserver(DataSetObserver paramDataSetObserver) {
    this.mAdapter.registerDataSetObserver(paramDataSetObserver);
  }
  
  public void setOnMenuItemClickListener(SwipeMenuListView.OnMenuItemClickListener paramOnMenuItemClickListener) {
    this.onMenuItemClickListener = paramOnMenuItemClickListener;
  }
  
  public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver) {
    this.mAdapter.unregisterDataSetObserver(paramDataSetObserver);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/baoyz/swipemenulistview/SwipeMenuAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */