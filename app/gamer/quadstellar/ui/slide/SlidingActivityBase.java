package app.gamer.quadstellar.ui.slide;

import android.view.View;
import android.view.ViewGroup;

public interface SlidingActivityBase {
  View getAboveView();
  
  SlidingMenu getSlidingMenu();
  
  void setBehindContentView(int paramInt);
  
  void setBehindContentView(View paramView);
  
  void setBehindContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams);
  
  void setSlidingActionBarEnabled(boolean paramBoolean);
  
  void showContent();
  
  void showMenu();
  
  void showSecondaryMenu();
  
  void toggle();
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/slide/SlidingActivityBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */