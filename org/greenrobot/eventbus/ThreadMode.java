package org.greenrobot.eventbus;

public enum ThreadMode {
  ASYNC, BACKGROUND, MAIN, POSTING;
  
  static {
    MAIN = new ThreadMode("MAIN", 1);
    BACKGROUND = new ThreadMode("BACKGROUND", 2);
    ASYNC = new ThreadMode("ASYNC", 3);
    $VALUES = new ThreadMode[] { POSTING, MAIN, BACKGROUND, ASYNC };
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/eventbus/ThreadMode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */