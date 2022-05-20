package android.support.v4.net;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.RequiresApi;

@TargetApi(13)
@RequiresApi(13)
class ConnectivityManagerCompatHoneycombMR2 {
  public static boolean isActiveNetworkMetered(ConnectivityManager paramConnectivityManager) {
    boolean bool1 = true;
    NetworkInfo networkInfo = paramConnectivityManager.getActiveNetworkInfo();
    if (networkInfo == null)
      boolean bool = bool1; 
    boolean bool2 = bool1;
    switch (networkInfo.getType()) {
      case 0:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
        return bool2;
      default:
        bool2 = bool1;
      case 1:
      case 7:
      case 9:
        break;
    } 
    bool2 = false;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/net/ConnectivityManagerCompatHoneycombMR2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */