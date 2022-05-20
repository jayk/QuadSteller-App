package android.support.v4.media;

import android.os.Bundle;
import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class MediaBrowserCompatUtils {
  public static boolean areSameOptions(Bundle paramBundle1, Bundle paramBundle2) {
    boolean bool = true;
    if (paramBundle1 != paramBundle2) {
      if (paramBundle1 == null) {
        if (paramBundle2.getInt("android.media.browse.extra.PAGE", -1) != -1 || paramBundle2.getInt("android.media.browse.extra.PAGE_SIZE", -1) != -1)
          bool = false; 
        return bool;
      } 
      if (paramBundle2 == null) {
        if (paramBundle1.getInt("android.media.browse.extra.PAGE", -1) != -1 || paramBundle1.getInt("android.media.browse.extra.PAGE_SIZE", -1) != -1)
          bool = false; 
        return bool;
      } 
      if (paramBundle1.getInt("android.media.browse.extra.PAGE", -1) != paramBundle2.getInt("android.media.browse.extra.PAGE", -1) || paramBundle1.getInt("android.media.browse.extra.PAGE_SIZE", -1) != paramBundle2.getInt("android.media.browse.extra.PAGE_SIZE", -1))
        bool = false; 
    } 
    return bool;
  }
  
  public static boolean hasDuplicatedItems(Bundle paramBundle1, Bundle paramBundle2) {
    int i;
    int j;
    int k;
    int m;
    boolean bool = true;
    if (paramBundle1 == null) {
      i = -1;
    } else {
      i = paramBundle1.getInt("android.media.browse.extra.PAGE", -1);
    } 
    if (paramBundle2 == null) {
      j = -1;
    } else {
      j = paramBundle2.getInt("android.media.browse.extra.PAGE", -1);
    } 
    if (paramBundle1 == null) {
      k = -1;
    } else {
      k = paramBundle1.getInt("android.media.browse.extra.PAGE_SIZE", -1);
    } 
    if (paramBundle2 == null) {
      m = -1;
    } else {
      m = paramBundle2.getInt("android.media.browse.extra.PAGE_SIZE", -1);
    } 
    if (i == -1 || k == -1) {
      i = 0;
      k = Integer.MAX_VALUE;
    } else {
      i = k * i;
      k = i + k - 1;
    } 
    if (j == -1 || m == -1) {
      m = 0;
      j = Integer.MAX_VALUE;
    } else {
      int n = m * j;
      j = n + m - 1;
      m = n;
    } 
    if ((i > m || m > k) && (i > j || j > k))
      bool = false; 
    return bool;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/media/MediaBrowserCompatUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */