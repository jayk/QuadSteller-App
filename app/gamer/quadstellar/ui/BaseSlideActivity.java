package app.gamer.quadstellar.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public abstract class BaseSlideActivity extends FragmentActivity {
  protected abstract void init();
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    init();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/BaseSlideActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */