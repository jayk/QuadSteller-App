package app.lib.pullToRefresh;

import android.util.Log;

public class Utils {
  static final String LOG_TAG = "PullToRefresh";
  
  public static void warnDeprecation(String paramString1, String paramString2) {
    Log.w("PullToRefresh", "You're using the deprecated " + paramString1 + " attr, please switch over to " + paramString2);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/pullToRefresh/Utils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */