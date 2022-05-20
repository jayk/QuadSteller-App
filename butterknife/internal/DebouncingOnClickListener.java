package butterknife.internal;

import android.view.View;

public abstract class DebouncingOnClickListener implements View.OnClickListener {
  private static final Runnable ENABLE_AGAIN;
  
  static boolean enabled = true;
  
  static {
    ENABLE_AGAIN = new Runnable() {
        public void run() {
          DebouncingOnClickListener.enabled = true;
        }
      };
  }
  
  public abstract void doClick(View paramView);
  
  public final void onClick(View paramView) {
    if (enabled) {
      enabled = false;
      paramView.post(ENABLE_AGAIN);
      doClick(paramView);
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/butterknife/internal/DebouncingOnClickListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */