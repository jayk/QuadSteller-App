package app.gamer.quadstellar.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import app.gamer.quadstellar.App;
import app.gamer.quadstellar.utils.LogUtil;
import app.gamer.quadstellar.utils.PreferenceHelper;
import app.gamer.quadstellar.utils.XlinkUtils;
import org.xutils.x;

public abstract class BaseActivity extends FragmentActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
  protected long currentTime = 0L;
  
  protected FragmentManager mFragmentManager;
  
  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent) {
    if (paramMotionEvent.getAction() == 0) {
      View view = getCurrentFocus();
      if (isShouldHideInput(view, paramMotionEvent)) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService("input_method");
        if (inputMethodManager != null)
          inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0); 
      } 
      return super.dispatchTouchEvent(paramMotionEvent);
    } 
    return getWindow().superDispatchTouchEvent(paramMotionEvent) ? true : onTouchEvent(paramMotionEvent);
  }
  
  protected void exit() {
    if (System.currentTimeMillis() - this.currentTime > 1200L) {
      XlinkUtils.shortTips(getString(2131296673));
      this.currentTime = System.currentTimeMillis();
      return;
    } 
    Intent intent = new Intent("android.intent.action.MAIN");
    intent.addCategory("android.intent.category.HOME");
    intent.setFlags(268435456);
    startActivity(intent);
    Process.killProcess(Process.myPid());
  }
  
  public FragmentManager getFM() {
    return this.mFragmentManager;
  }
  
  public Resources getResources() {
    Resources resources = super.getResources();
    Configuration configuration = new Configuration();
    configuration.setToDefaults();
    resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    return resources;
  }
  
  public void hideFragment(Fragment paramFragment) {
    FragmentTransaction fragmentTransaction = this.mFragmentManager.beginTransaction();
    fragmentTransaction.setCustomAnimations(17432578, 17432579);
    fragmentTransaction.hide(paramFragment);
    fragmentTransaction.commit();
  }
  
  protected abstract void init();
  
  public boolean isShouldHideInput(View paramView, MotionEvent paramMotionEvent) {
    boolean bool = false;
    null = bool;
    if (paramView != null) {
      null = bool;
      if (paramView instanceof android.widget.EditText) {
        int[] arrayOfInt = new int[2];
        arrayOfInt[0] = 0;
        arrayOfInt[1] = 0;
        paramView.getLocationInWindow(arrayOfInt);
        int i = arrayOfInt[0];
        int j = arrayOfInt[1];
        int k = paramView.getHeight();
        int m = paramView.getWidth();
        if (paramMotionEvent.getX() > i && paramMotionEvent.getX() < (i + m) && paramMotionEvent.getY() > j && paramMotionEvent.getY() < (j + k))
          return bool; 
      } else {
        return null;
      } 
    } else {
      return null;
    } 
    return true;
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    if ((getIntent().getFlags() & 0x400000) != 0) {
      finish();
      return;
    } 
    this.mFragmentManager = getSupportFragmentManager();
    PreferenceHelper.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    App.getInstance().getActivities().add(this);
  }
  
  protected void onDestroy() {
    super.onDestroy();
    App.getInstance().getActivities().remove(this);
  }
  
  protected void onResume() {
    super.onResume();
  }
  
  public void onSharedPreferenceChanged(SharedPreferences paramSharedPreferences, String paramString) {
    LogUtil.e("key", "key" + paramString);
    if ("bgground_path".equals(paramString));
  }
  
  protected void onStop() {
    super.onStop();
  }
  
  public void replaceFragment(int paramInt, Fragment paramFragment) {
    FragmentTransaction fragmentTransaction = this.mFragmentManager.beginTransaction();
    fragmentTransaction.setTransition(4096);
    fragmentTransaction.replace(paramInt, paramFragment);
    fragmentTransaction.commit();
  }
  
  public void replaceFragment(Fragment paramFragment) {
    FragmentTransaction fragmentTransaction = this.mFragmentManager.beginTransaction();
    fragmentTransaction.setTransition(4096);
    fragmentTransaction.replace(16908305, paramFragment);
    fragmentTransaction.commit();
  }
  
  public void replaceFragmentBack(Fragment paramFragment) {
    FragmentTransaction fragmentTransaction = this.mFragmentManager.beginTransaction();
    fragmentTransaction.setTransition(4096);
    fragmentTransaction.setCustomAnimations(17432578, 17432579);
    fragmentTransaction.replace(16908305, paramFragment);
    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();
  }
  
  public void setContentView(int paramInt) {
    super.setContentView(paramInt);
    x.view().inject((Activity)this);
    init();
  }
  
  public void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    super.setContentView(paramView, paramLayoutParams);
    x.view().inject((Activity)this);
    init();
  }
  
  public void showActivity(Activity paramActivity, Intent paramIntent) {
    paramActivity.startActivity(paramIntent);
  }
  
  public void showActivity(Activity paramActivity, Class<?> paramClass) {
    Intent intent = new Intent();
    intent.setClass((Context)paramActivity, paramClass);
    paramActivity.startActivity(intent);
  }
  
  public void showActivity(Activity paramActivity, Class<?> paramClass, int paramInt) {
    Intent intent = new Intent();
    intent.setFlags(paramInt);
    intent.setClass((Context)paramActivity, paramClass);
    paramActivity.startActivity(intent);
  }
  
  public void showActivity(Activity paramActivity, Class<?> paramClass, Bundle paramBundle) {
    Intent intent = new Intent();
    intent.putExtras(paramBundle);
    intent.setClass((Context)paramActivity, paramClass);
    paramActivity.startActivity(intent);
  }
  
  public void showActivityForResult(Activity paramActivity, Class<?> paramClass, int paramInt, Bundle paramBundle) {
    Intent intent = new Intent();
    intent.setClass((Context)paramActivity, paramClass);
    if (paramBundle != null)
      intent.putExtra("bundle", paramBundle); 
    paramActivity.startActivityForResult(intent, paramInt);
  }
  
  public void showFragment(Fragment paramFragment) {
    FragmentTransaction fragmentTransaction = this.mFragmentManager.beginTransaction();
    fragmentTransaction.setCustomAnimations(17432578, 17432579);
    fragmentTransaction.show(paramFragment);
    fragmentTransaction.commit();
  }
  
  public void skipActivity(Activity paramActivity, Intent paramIntent) {
    showActivity(paramActivity, paramIntent);
    paramActivity.finish();
  }
  
  public void skipActivity(Activity paramActivity, Class<?> paramClass) {
    showActivity(paramActivity, paramClass);
    paramActivity.finish();
  }
  
  public void skipActivity(Activity paramActivity, Class<?> paramClass, int paramInt) {
    showActivity(paramActivity, paramClass, paramInt);
    paramActivity.finish();
  }
  
  public void skipActivity(Activity paramActivity, Class<?> paramClass, Bundle paramBundle) {
    showActivity(paramActivity, paramClass, paramBundle);
    paramActivity.finish();
  }
  
  public void startActivity(Intent paramIntent) {
    super.startActivity(paramIntent);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/BaseActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */