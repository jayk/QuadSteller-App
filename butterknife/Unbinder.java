package butterknife;

import android.support.annotation.UiThread;

public interface Unbinder {
  public static final Unbinder EMPTY = new Unbinder() {
      public void unbind() {}
    };
  
  @UiThread
  void unbind();
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/butterknife/Unbinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */