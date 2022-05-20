package app.gamer.quadstellar.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.xutils.x;

public abstract class BaseFragment extends Fragment {
  protected boolean isFirst = true;
  
  protected boolean isPrepared = false;
  
  protected boolean isVisible = false;
  
  protected BaseActivity mActivity;
  
  protected FragmentManager mFragmentManager;
  
  protected View mView;
  
  protected abstract void initUI(View paramView);
  
  protected View initView(LayoutInflater paramLayoutInflater, int paramInt, ViewGroup paramViewGroup) {
    ViewGroup viewGroup;
    if (this.mView != null) {
      viewGroup = (ViewGroup)this.mView.getParent();
      if (viewGroup != null)
        viewGroup.removeView(this.mView); 
      this.isPrepared = true;
      lazyLoad();
      return this.mView;
    } 
    this.mView = viewGroup.inflate(paramInt, paramViewGroup, false);
    x.view().inject(this, this.mView);
    this.mFragmentManager = this.mActivity.getFM();
    initUI(this.mView);
    this.isPrepared = true;
    lazyLoad();
    return this.mView;
  }
  
  protected abstract void lazyLoad();
  
  public void onAttach(Activity paramActivity) {
    super.onAttach(paramActivity);
    this.mActivity = (BaseActivity)paramActivity;
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
  }
  
  public void onDestroy() {
    super.onDestroy();
  }
  
  protected void onInvisible() {}
  
  public void onPause() {
    super.onPause();
  }
  
  public void onResume() {
    super.onResume();
  }
  
  protected void onVisible() {
    lazyLoad();
  }
  
  public void setUserVisibleHint(boolean paramBoolean) {
    super.setUserVisibleHint(paramBoolean);
    if (getUserVisibleHint()) {
      this.isVisible = true;
      onVisible();
      return;
    } 
    this.isVisible = false;
    onInvisible();
  }
  
  protected void showActivityForResult(Activity paramActivity, Class<?> paramClass, int paramInt) {
    showActivityForResult(paramActivity, paramClass, paramInt, (Bundle)null);
  }
  
  protected void showActivityForResult(Activity paramActivity, Class<?> paramClass, int paramInt, Bundle paramBundle) {
    Intent intent = new Intent();
    intent.setClass((Context)paramActivity, paramClass);
    if (paramBundle != null)
      intent.putExtra("bundle", paramBundle); 
    startActivityForResult(intent, paramInt);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/BaseFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */