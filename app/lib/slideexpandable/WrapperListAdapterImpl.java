package app.lib.slideexpandable;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

public abstract class WrapperListAdapterImpl extends BaseAdapter implements WrapperListAdapter {
  protected ListAdapter wrapped;
  
  public WrapperListAdapterImpl(ListAdapter paramListAdapter) {
    this.wrapped = paramListAdapter;
  }
  
  public boolean areAllItemsEnabled() {
    return this.wrapped.areAllItemsEnabled();
  }
  
  public int getCount() {
    return this.wrapped.getCount();
  }
  
  public Object getItem(int paramInt) {
    return this.wrapped.getItem(paramInt);
  }
  
  public long getItemId(int paramInt) {
    return this.wrapped.getItemId(paramInt);
  }
  
  public int getItemViewType(int paramInt) {
    return this.wrapped.getItemViewType(paramInt);
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    return this.wrapped.getView(paramInt, paramView, paramViewGroup);
  }
  
  public int getViewTypeCount() {
    return this.wrapped.getViewTypeCount();
  }
  
  public ListAdapter getWrappedAdapter() {
    return this.wrapped;
  }
  
  public boolean hasStableIds() {
    return this.wrapped.hasStableIds();
  }
  
  public boolean isEmpty() {
    return this.wrapped.isEmpty();
  }
  
  public boolean isEnabled(int paramInt) {
    return this.wrapped.isEnabled(paramInt);
  }
  
  public void registerDataSetObserver(DataSetObserver paramDataSetObserver) {
    this.wrapped.registerDataSetObserver(paramDataSetObserver);
  }
  
  public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver) {
    this.wrapped.unregisterDataSetObserver(paramDataSetObserver);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/slideexpandable/WrapperListAdapterImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */