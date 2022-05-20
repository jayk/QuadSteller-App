package app.gamer.quadstellar.adapter;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import app.gamer.quadstellar.ui.BaseActivity;
import java.util.List;

public abstract class ArrayListAdapter<T> extends BaseAdapter {
  protected BaseActivity mContext;
  
  protected int mCount;
  
  protected List<T> mList;
  
  public ArrayListAdapter(BaseActivity paramBaseActivity) {
    this.mContext = paramBaseActivity;
  }
  
  public void addItem(T paramT) {
    this.mList.add(paramT);
    notifyDataSetChanged();
  }
  
  public void addItems(List<T> paramList) {
    for (List<T> paramList : paramList)
      this.mList.add((T)paramList); 
    notifyDataSetChanged();
  }
  
  public void clearItem() {
    this.mList.clear();
    notifyDataSetChanged();
  }
  
  protected <T extends View> T getAdapterView(View paramView, int paramInt) {
    SparseArray sparseArray1 = (SparseArray)paramView.getTag();
    SparseArray sparseArray2 = sparseArray1;
    if (sparseArray1 == null) {
      sparseArray2 = new SparseArray();
      paramView.setTag(sparseArray2);
    } 
    View view2 = (View)sparseArray2.get(paramInt);
    View view1 = view2;
    if (view2 == null) {
      view1 = paramView.findViewById(paramInt);
      sparseArray2.put(paramInt, view1);
    } 
    return (T)view1;
  }
  
  public int getCount() {
    if (this.mList != null) {
      int i = this.mList.size();
      this.mCount = i;
      return this.mCount;
    } 
    boolean bool = false;
    this.mCount = bool;
    return this.mCount;
  }
  
  public T getItem(int paramInt) {
    return (this.mList != null) ? this.mList.get(paramInt) : null;
  }
  
  public long getItemId(int paramInt) {
    return paramInt;
  }
  
  public List<T> getList() {
    return this.mList;
  }
  
  public abstract View getView(int paramInt, View paramView, ViewGroup paramViewGroup);
  
  public void remove(int paramInt) {
    this.mList.remove(paramInt);
    notifyDataSetChanged();
  }
  
  public void remove(T paramT) {
    this.mList.remove(paramT);
    notifyDataSetChanged();
  }
  
  public void setList(List<? extends T> paramList) {
    this.mList = (List)paramList;
    notifyDataSetChanged();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/adapter/ArrayListAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */