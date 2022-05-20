package app.gamer.quadstellar.ui.slide;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class SlidingListActivity extends ListActivity implements SlidingActivityBase {
  private SlidingActivityHelper mHelper;
  
  public View findViewById(int paramInt) {
    View view = super.findViewById(paramInt);
    if (view == null)
      view = this.mHelper.findViewById(paramInt); 
    return view;
  }
  
  public View getAboveView() {
    return this.mHelper.getAboveView();
  }
  
  public SlidingMenu getSlidingMenu() {
    return this.mHelper.getSlidingMenu();
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    this.mHelper = new SlidingActivityHelper((Activity)this);
    this.mHelper.onCreate(paramBundle);
    ListView listView = new ListView((Context)this);
    listView.setId(16908298);
    setContentView((View)listView);
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
    boolean bool = this.mHelper.onKeyUp(paramInt, paramKeyEvent);
    if (!bool)
      bool = super.onKeyUp(paramInt, paramKeyEvent); 
    return bool;
  }
  
  public void onPostCreate(Bundle paramBundle) {
    super.onPostCreate(paramBundle);
    this.mHelper.onPostCreate(paramBundle);
  }
  
  protected void onSaveInstanceState(Bundle paramBundle) {
    super.onSaveInstanceState(paramBundle);
    this.mHelper.onSaveInstanceState(paramBundle);
  }
  
  public void setBehindContentView(int paramInt) {
    setBehindContentView(getLayoutInflater().inflate(paramInt, null));
  }
  
  public void setBehindContentView(View paramView) {
    setBehindContentView(paramView, new ViewGroup.LayoutParams(-1, -1));
  }
  
  public void setBehindContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    this.mHelper.setBehindContentView(paramView, paramLayoutParams);
  }
  
  public void setContentView(int paramInt) {
    setContentView(getLayoutInflater().inflate(paramInt, null));
  }
  
  public void setContentView(View paramView) {
    setContentView(paramView, new ViewGroup.LayoutParams(-1, -1));
  }
  
  public void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    super.setContentView(paramView, paramLayoutParams);
    this.mHelper.registerAboveContentView(paramView, paramLayoutParams);
  }
  
  public void setSlidingActionBarEnabled(boolean paramBoolean) {
    this.mHelper.setSlidingActionBarEnabled(paramBoolean);
  }
  
  public void showContent() {
    this.mHelper.showContent();
  }
  
  public void showMenu() {
    this.mHelper.showMenu();
  }
  
  public void showSecondaryMenu() {
    this.mHelper.showSecondaryMenu();
  }
  
  public void toggle() {
    this.mHelper.toggle();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/slide/SlidingListActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */