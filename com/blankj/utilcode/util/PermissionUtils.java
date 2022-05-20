package com.blankj.utilcode.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import java.util.ArrayList;

public final class PermissionUtils {
  private static OnPermissionListener mOnPermissionListener;
  
  private static int mRequestCode = -1;
  
  private static String[] getDeniedPermissions(Context paramContext, String[] paramArrayOfString) {
    ArrayList<String> arrayList = new ArrayList();
    int i = paramArrayOfString.length;
    for (byte b = 0; b < i; b++) {
      String str = paramArrayOfString[b];
      if (ContextCompat.checkSelfPermission(paramContext, str) == -1)
        arrayList.add(str); 
    } 
    return arrayList.<String>toArray(new String[arrayList.size()]);
  }
  
  public static boolean hasAlwaysDeniedPermission(Context paramContext, String... paramVarArgs) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_2
    //   2: aload_1
    //   3: arraylength
    //   4: istore_3
    //   5: iconst_0
    //   6: istore #4
    //   8: iload #4
    //   10: iload_3
    //   11: if_icmpge -> 40
    //   14: aload_0
    //   15: iconst_1
    //   16: anewarray java/lang/String
    //   19: dup
    //   20: iconst_0
    //   21: aload_1
    //   22: iload #4
    //   24: aaload
    //   25: aastore
    //   26: invokestatic shouldShowRequestPermissionRationale : (Landroid/content/Context;[Ljava/lang/String;)Z
    //   29: ifne -> 34
    //   32: iload_2
    //   33: ireturn
    //   34: iinc #4, 1
    //   37: goto -> 8
    //   40: iconst_0
    //   41: istore_2
    //   42: goto -> 32
  }
  
  public static void onRequestPermissionsResult(Activity paramActivity, int paramInt, String[] paramArrayOfString, int[] paramArrayOfint) {
    if (mRequestCode != -1 && paramInt == mRequestCode && mOnPermissionListener != null) {
      String[] arrayOfString = getDeniedPermissions((Context)paramActivity, paramArrayOfString);
      if (arrayOfString.length > 0) {
        mOnPermissionListener.onPermissionDenied(arrayOfString);
        return;
      } 
    } else {
      return;
    } 
    mOnPermissionListener.onPermissionGranted();
  }
  
  @TargetApi(23)
  public static void requestPermissions(Context paramContext, int paramInt, String[] paramArrayOfString, OnPermissionListener paramOnPermissionListener) {
    requestPermissions(paramContext, paramInt, paramArrayOfString, paramOnPermissionListener, null);
  }
  
  @TargetApi(23)
  public static void requestPermissions(Context paramContext, int paramInt, String[] paramArrayOfString, OnPermissionListener paramOnPermissionListener, RationaleHandler paramRationaleHandler) {
    if (paramContext instanceof Activity) {
      mRequestCode = paramInt;
      mOnPermissionListener = paramOnPermissionListener;
      paramArrayOfString = getDeniedPermissions(paramContext, paramArrayOfString);
      if (paramArrayOfString.length > 0) {
        if (shouldShowRequestPermissionRationale(paramContext, paramArrayOfString) && paramRationaleHandler != null) {
          paramRationaleHandler.showRationale(paramContext, paramInt, paramArrayOfString);
          return;
        } 
        ((Activity)paramContext).requestPermissions(paramArrayOfString, paramInt);
        return;
      } 
      if (mOnPermissionListener != null)
        mOnPermissionListener.onPermissionGranted(); 
      return;
    } 
    throw new RuntimeException("Context must be an Activity");
  }
  
  private static boolean shouldShowRequestPermissionRationale(Context paramContext, String... paramVarArgs) {
    // Byte code:
    //   0: getstatic android/os/Build$VERSION.SDK_INT : I
    //   3: bipush #23
    //   5: if_icmpge -> 12
    //   8: iconst_0
    //   9: istore_2
    //   10: iload_2
    //   11: ireturn
    //   12: aload_1
    //   13: arraylength
    //   14: istore_3
    //   15: iconst_0
    //   16: istore #4
    //   18: iload #4
    //   20: iload_3
    //   21: if_icmpge -> 53
    //   24: aload_1
    //   25: iload #4
    //   27: aaload
    //   28: astore #5
    //   30: aload_0
    //   31: checkcast android/app/Activity
    //   34: aload #5
    //   36: invokestatic shouldShowRequestPermissionRationale : (Landroid/app/Activity;Ljava/lang/String;)Z
    //   39: ifeq -> 47
    //   42: iconst_1
    //   43: istore_2
    //   44: goto -> 10
    //   47: iinc #4, 1
    //   50: goto -> 18
    //   53: iconst_0
    //   54: istore_2
    //   55: goto -> 10
  }
  
  public static interface OnPermissionListener {
    void onPermissionDenied(String[] param1ArrayOfString);
    
    void onPermissionGranted();
  }
  
  public static abstract class RationaleHandler {
    private Context context;
    
    private String[] permissions;
    
    private int requestCode;
    
    @TargetApi(23)
    public void requestPermissionsAgain() {
      ((Activity)this.context).requestPermissions(this.permissions, this.requestCode);
    }
    
    protected abstract void showRationale();
    
    void showRationale(Context param1Context, int param1Int, String[] param1ArrayOfString) {
      this.context = param1Context;
      this.requestCode = param1Int;
      this.permissions = param1ArrayOfString;
      showRationale();
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/PermissionUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */