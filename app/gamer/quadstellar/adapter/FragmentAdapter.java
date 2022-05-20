package app.gamer.quadstellar.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import java.util.ArrayList;

public class FragmentAdapter<T extends Fragment> extends FragmentStatePagerAdapter {
  private ArrayList<T> list;
  
  public FragmentAdapter(FragmentManager paramFragmentManager, ArrayList<T> paramArrayList) {
    super(paramFragmentManager);
    this.list = paramArrayList;
  }
  
  public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject) {
    super.destroyItem(paramViewGroup, paramInt, paramObject);
  }
  
  public int getCount() {
    return this.list.size();
  }
  
  public T getItem(int paramInt) {
    return this.list.get(paramInt);
  }
  
  public Object instantiateItem(ViewGroup paramViewGroup, int paramInt) {
    return super.instantiateItem(paramViewGroup, paramInt);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/adapter/FragmentAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */