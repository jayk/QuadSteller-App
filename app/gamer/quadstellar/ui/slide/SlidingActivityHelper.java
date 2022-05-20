package app.gamer.quadstellar.ui.slide;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SlidingActivityHelper {
  private Activity mActivity;
  
  private boolean mBroadcasting = false;
  
  private boolean mEnableSlide = true;
  
  private boolean mOnPostCreateCalled = false;
  
  private SlidingMenu mSlidingMenu;
  
  private View mViewAbove;
  
  private View mViewBehind;
  
  public SlidingActivityHelper(Activity paramActivity) {
    this.mActivity = paramActivity;
  }
  
  public View findViewById(int paramInt) {
    if (this.mSlidingMenu != null) {
      View view = this.mSlidingMenu.findViewById(paramInt);
      if (view != null)
        return view; 
    } 
    return null;
  }
  
  public View getAboveView() {
    return this.mViewAbove;
  }
  
  public SlidingMenu getSlidingMenu() {
    return this.mSlidingMenu;
  }
  
  public void onCreate(Bundle paramBundle) {
    this.mSlidingMenu = (SlidingMenu)LayoutInflater.from((Context)this.mActivity).inflate(2130903296, null);
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
    if (paramInt == 4 && this.mSlidingMenu.isMenuShowing()) {
      showContent();
      return true;
    } 
    return false;
  }
  
  public void onPostCreate(Bundle paramBundle) {
    final boolean open;
    final boolean secondary;
    boolean bool1 = true;
    if (this.mViewBehind == null || this.mViewAbove == null)
      throw new IllegalStateException("Both setBehindContentView must be called in onCreate in addition to setContentView."); 
    this.mOnPostCreateCalled = true;
    SlidingMenu slidingMenu = this.mSlidingMenu;
    Activity activity = this.mActivity;
    if (this.mEnableSlide)
      bool1 = false; 
    slidingMenu.attachToActivity(activity, bool1);
    if (paramBundle != null) {
      bool2 = paramBundle.getBoolean("SlidingActivityHelper.open");
      bool3 = paramBundle.getBoolean("SlidingActivityHelper.secondary");
    } else {
      bool2 = false;
      bool3 = false;
    } 
    (new Handler()).post(new Runnable() {
          public void run() {
            if (open) {
              if (secondary) {
                SlidingActivityHelper.this.mSlidingMenu.showSecondaryMenu(false);
                return;
              } 
              SlidingActivityHelper.this.mSlidingMenu.showMenu(false);
              return;
            } 
            SlidingActivityHelper.this.mSlidingMenu.showContent(false);
          }
        });
  }
  
  public void onSaveInstanceState(Bundle paramBundle) {
    paramBundle.putBoolean("SlidingActivityHelper.open", this.mSlidingMenu.isMenuShowing());
    paramBundle.putBoolean("SlidingActivityHelper.secondary", this.mSlidingMenu.isSecondaryMenuShowing());
  }
  
  public void registerAboveContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    if (!this.mBroadcasting)
      this.mViewAbove = paramView; 
  }
  
  public void setBehindContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    this.mViewBehind = paramView;
    this.mSlidingMenu.setMenu(this.mViewBehind);
  }
  
  public void setContentView(View paramView) {
    this.mBroadcasting = true;
    this.mActivity.setContentView(paramView);
  }
  
  public void setSlidingActionBarEnabled(boolean paramBoolean) {
    if (this.mOnPostCreateCalled)
      throw new IllegalStateException("enableSlidingActionBar must be called in onCreate."); 
    this.mEnableSlide = paramBoolean;
  }
  
  public void showContent() {
    this.mSlidingMenu.showContent();
  }
  
  public void showMenu() {
    this.mSlidingMenu.showMenu();
  }
  
  public void showSecondaryMenu() {
    this.mSlidingMenu.showSecondaryMenu();
  }
  
  public void toggle() {
    this.mSlidingMenu.toggle();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/slide/SlidingActivityHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */