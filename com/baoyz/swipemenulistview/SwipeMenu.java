package com.baoyz.swipemenulistview;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

public class SwipeMenu {
  private Context mContext;
  
  private List<SwipeMenuItem> mItems;
  
  private int mViewType;
  
  public SwipeMenu(Context paramContext) {
    this.mContext = paramContext;
    this.mItems = new ArrayList<SwipeMenuItem>();
  }
  
  public void addMenuItem(SwipeMenuItem paramSwipeMenuItem) {
    this.mItems.add(paramSwipeMenuItem);
  }
  
  public Context getContext() {
    return this.mContext;
  }
  
  public SwipeMenuItem getMenuItem(int paramInt) {
    return this.mItems.get(paramInt);
  }
  
  public List<SwipeMenuItem> getMenuItems() {
    return this.mItems;
  }
  
  public int getViewType() {
    return this.mViewType;
  }
  
  public void removeMenuItem(SwipeMenuItem paramSwipeMenuItem) {
    this.mItems.remove(paramSwipeMenuItem);
  }
  
  public void setViewType(int paramInt) {
    this.mViewType = paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/baoyz/swipemenulistview/SwipeMenu.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */