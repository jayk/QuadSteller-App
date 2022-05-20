package app.gamer.quadstellar.newdevices.Base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class New_BaseFragment extends Fragment {
  protected AppCompatActivity mActivity;
  
  protected void initData() {}
  
  protected abstract View initView();
  
  public void onActivityCreated(Bundle paramBundle) {
    super.onActivityCreated(paramBundle);
    initData();
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    this.mActivity = (AppCompatActivity)getActivity();
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
    return initView();
  }
  
  public void onStart() {
    super.onStart();
    reInitData();
  }
  
  protected void reInitData() {}
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/Base/New_BaseFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */