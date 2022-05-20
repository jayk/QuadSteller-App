package android.support.v4.content;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.os.BuildCompat;
import android.util.TypedValue;
import java.io.File;

public class ContextCompat {
  private static final String DIR_ANDROID = "Android";
  
  private static final String DIR_OBB = "obb";
  
  private static final String TAG = "ContextCompat";
  
  private static final Object sLock = new Object();
  
  private static TypedValue sTempValue;
  
  private static File buildPath(File paramFile, String... paramVarArgs) {
    int i = paramVarArgs.length;
    for (byte b = 0; b < i; b++) {
      String str = paramVarArgs[b];
      if (paramFile == null) {
        paramFile = new File(str);
      } else if (str != null) {
        paramFile = new File(paramFile, str);
      } 
    } 
    return paramFile;
  }
  
  public static int checkSelfPermission(@NonNull Context paramContext, @NonNull String paramString) {
    if (paramString == null)
      throw new IllegalArgumentException("permission is null"); 
    return paramContext.checkPermission(paramString, Process.myPid(), Process.myUid());
  }
  
  public static Context createDeviceProtectedStorageContext(Context paramContext) {
    return BuildCompat.isAtLeastN() ? ContextCompatApi24.createDeviceProtectedStorageContext(paramContext) : null;
  }
  
  private static File createFilesDir(File paramFile) {
    // Byte code:
    //   0: ldc android/support/v4/content/ContextCompat
    //   2: monitorenter
    //   3: aload_0
    //   4: astore_1
    //   5: aload_0
    //   6: invokevirtual exists : ()Z
    //   9: ifne -> 32
    //   12: aload_0
    //   13: astore_1
    //   14: aload_0
    //   15: invokevirtual mkdirs : ()Z
    //   18: ifne -> 32
    //   21: aload_0
    //   22: invokevirtual exists : ()Z
    //   25: istore_2
    //   26: iload_2
    //   27: ifeq -> 37
    //   30: aload_0
    //   31: astore_1
    //   32: ldc android/support/v4/content/ContextCompat
    //   34: monitorexit
    //   35: aload_1
    //   36: areturn
    //   37: new java/lang/StringBuilder
    //   40: astore_1
    //   41: aload_1
    //   42: invokespecial <init> : ()V
    //   45: ldc 'ContextCompat'
    //   47: aload_1
    //   48: ldc 'Unable to create files subdir '
    //   50: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   53: aload_0
    //   54: invokevirtual getPath : ()Ljava/lang/String;
    //   57: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   60: invokevirtual toString : ()Ljava/lang/String;
    //   63: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
    //   66: pop
    //   67: aconst_null
    //   68: astore_1
    //   69: goto -> 32
    //   72: astore_0
    //   73: ldc android/support/v4/content/ContextCompat
    //   75: monitorexit
    //   76: aload_0
    //   77: athrow
    // Exception table:
    //   from	to	target	type
    //   5	12	72	finally
    //   14	26	72	finally
    //   37	67	72	finally
  }
  
  public static File getCodeCacheDir(Context paramContext) {
    return (Build.VERSION.SDK_INT >= 21) ? ContextCompatApi21.getCodeCacheDir(paramContext) : createFilesDir(new File((paramContext.getApplicationInfo()).dataDir, "code_cache"));
  }
  
  @ColorInt
  public static final int getColor(Context paramContext, @ColorRes int paramInt) {
    return (Build.VERSION.SDK_INT >= 23) ? ContextCompatApi23.getColor(paramContext, paramInt) : paramContext.getResources().getColor(paramInt);
  }
  
  public static final ColorStateList getColorStateList(Context paramContext, @ColorRes int paramInt) {
    return (Build.VERSION.SDK_INT >= 23) ? ContextCompatApi23.getColorStateList(paramContext, paramInt) : paramContext.getResources().getColorStateList(paramInt);
  }
  
  public static File getDataDir(Context paramContext) {
    if (BuildCompat.isAtLeastN())
      return ContextCompatApi24.getDataDir(paramContext); 
    null = (paramContext.getApplicationInfo()).dataDir;
    return (null != null) ? new File(null) : null;
  }
  
  public static final Drawable getDrawable(Context paramContext, @DrawableRes int paramInt) {
    int i = Build.VERSION.SDK_INT;
    if (i >= 21)
      return ContextCompatApi21.getDrawable(paramContext, paramInt); 
    if (i >= 16)
      return paramContext.getResources().getDrawable(paramInt); 
    Object object = sLock;
    /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    try {
      if (sTempValue == null) {
        TypedValue typedValue = new TypedValue();
        this();
        sTempValue = typedValue;
      } 
      paramContext.getResources().getValue(paramInt, sTempValue, true);
      paramInt = sTempValue.resourceId;
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
      Drawable drawable = paramContext.getResources().getDrawable(paramInt);
    } finally {}
    return (Drawable)paramContext;
  }
  
  public static File[] getExternalCacheDirs(Context paramContext) {
    if (Build.VERSION.SDK_INT >= 19)
      return ContextCompatKitKat.getExternalCacheDirs(paramContext); 
    File[] arrayOfFile = new File[1];
    arrayOfFile[0] = paramContext.getExternalCacheDir();
    return arrayOfFile;
  }
  
  public static File[] getExternalFilesDirs(Context paramContext, String paramString) {
    if (Build.VERSION.SDK_INT >= 19)
      return ContextCompatKitKat.getExternalFilesDirs(paramContext, paramString); 
    File[] arrayOfFile = new File[1];
    arrayOfFile[0] = paramContext.getExternalFilesDir(paramString);
    return arrayOfFile;
  }
  
  public static final File getNoBackupFilesDir(Context paramContext) {
    return (Build.VERSION.SDK_INT >= 21) ? ContextCompatApi21.getNoBackupFilesDir(paramContext) : createFilesDir(new File((paramContext.getApplicationInfo()).dataDir, "no_backup"));
  }
  
  public static File[] getObbDirs(Context paramContext) {
    File file;
    int i = Build.VERSION.SDK_INT;
    if (i >= 19)
      return ContextCompatKitKat.getObbDirs(paramContext); 
    if (i >= 11) {
      file = ContextCompatHoneycomb.getObbDir(paramContext);
    } else {
      file = buildPath(Environment.getExternalStorageDirectory(), new String[] { "Android", "obb", file.getPackageName() });
    } 
    File[] arrayOfFile = new File[1];
    arrayOfFile[0] = file;
    return arrayOfFile;
  }
  
  public static boolean isDeviceProtectedStorage(Context paramContext) {
    return BuildCompat.isAtLeastN() ? ContextCompatApi24.isDeviceProtectedStorage(paramContext) : false;
  }
  
  public static boolean startActivities(Context paramContext, Intent[] paramArrayOfIntent) {
    return startActivities(paramContext, paramArrayOfIntent, null);
  }
  
  public static boolean startActivities(Context paramContext, Intent[] paramArrayOfIntent, Bundle paramBundle) {
    null = true;
    int i = Build.VERSION.SDK_INT;
    if (i >= 16) {
      ContextCompatJellybean.startActivities(paramContext, paramArrayOfIntent, paramBundle);
      return null;
    } 
    if (i >= 11) {
      ContextCompatHoneycomb.startActivities(paramContext, paramArrayOfIntent);
      return null;
    } 
    return false;
  }
  
  public static void startActivity(Context paramContext, Intent paramIntent, @Nullable Bundle paramBundle) {
    if (Build.VERSION.SDK_INT >= 16) {
      ContextCompatJellybean.startActivity(paramContext, paramIntent, paramBundle);
      return;
    } 
    paramContext.startActivity(paramIntent);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/content/ContextCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */