package android.support.v4.content;

import android.content.Context;
import android.os.Binder;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v4.app.AppOpsManagerCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class PermissionChecker {
  public static final int PERMISSION_DENIED = -1;
  
  public static final int PERMISSION_DENIED_APP_OP = -2;
  
  public static final int PERMISSION_GRANTED = 0;
  
  public static int checkCallingOrSelfPermission(@NonNull Context paramContext, @NonNull String paramString) {
    if (Binder.getCallingPid() == Process.myPid()) {
      String str1 = paramContext.getPackageName();
      return checkPermission(paramContext, paramString, Binder.getCallingPid(), Binder.getCallingUid(), str1);
    } 
    String str = null;
    return checkPermission(paramContext, paramString, Binder.getCallingPid(), Binder.getCallingUid(), str);
  }
  
  public static int checkCallingPermission(@NonNull Context paramContext, @NonNull String paramString1, String paramString2) {
    return (Binder.getCallingPid() == Process.myPid()) ? -1 : checkPermission(paramContext, paramString1, Binder.getCallingPid(), Binder.getCallingUid(), paramString2);
  }
  
  public static int checkPermission(@NonNull Context paramContext, @NonNull String paramString1, int paramInt1, int paramInt2, String paramString2) {
    String str1;
    byte b = -1;
    if (paramContext.checkPermission(paramString1, paramInt1, paramInt2) == -1)
      return b; 
    String str2 = AppOpsManagerCompat.permissionToOp(paramString1);
    if (str2 == null)
      return 0; 
    paramString1 = paramString2;
    if (paramString2 == null) {
      String[] arrayOfString = paramContext.getPackageManager().getPackagesForUid(paramInt2);
      paramInt1 = b;
      if (arrayOfString != null) {
        paramInt1 = b;
        if (arrayOfString.length > 0) {
          str1 = arrayOfString[0];
        } else {
          return paramInt1;
        } 
      } else {
        return paramInt1;
      } 
    } 
    return (AppOpsManagerCompat.noteProxyOp(paramContext, str2, str1) != 0) ? -2 : 0;
  }
  
  public static int checkSelfPermission(@NonNull Context paramContext, @NonNull String paramString) {
    return checkPermission(paramContext, paramString, Process.myPid(), Process.myUid(), paramContext.getPackageName());
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface PermissionResult {}
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/content/PermissionChecker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */