package android.support.v4.content;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;

@TargetApi(16)
@RequiresApi(16)
class ContentResolverCompatJellybean {
  static boolean isFrameworkOperationCanceledException(Exception paramException) {
    return paramException instanceof android.os.OperationCanceledException;
  }
  
  public static Cursor query(ContentResolver paramContentResolver, Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2, Object paramObject) {
    return paramContentResolver.query(paramUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2, (CancellationSignal)paramObject);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/content/ContentResolverCompatJellybean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */