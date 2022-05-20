package com.blankj.utilcode.util;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;
import java.util.List;

public final class ActivityUtils {
  private ActivityUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static void finishActivity(@NonNull Activity paramActivity) {
    finishActivity(paramActivity, false);
  }
  
  public static void finishActivity(@NonNull Activity paramActivity, @AnimRes int paramInt1, @AnimRes int paramInt2) {
    paramActivity.finish();
    paramActivity.overridePendingTransition(paramInt1, paramInt2);
  }
  
  public static void finishActivity(@NonNull Activity paramActivity, boolean paramBoolean) {
    paramActivity.finish();
    if (!paramBoolean)
      paramActivity.overridePendingTransition(0, 0); 
  }
  
  public static void finishActivity(@NonNull Class<?> paramClass) {
    finishActivity(paramClass, false);
  }
  
  public static void finishActivity(@NonNull Class<?> paramClass, @AnimRes int paramInt1, @AnimRes int paramInt2) {
    for (Activity activity : Utils.sActivityList) {
      if (activity.getClass().equals(paramClass)) {
        activity.finish();
        activity.overridePendingTransition(paramInt1, paramInt2);
      } 
    } 
  }
  
  public static void finishActivity(@NonNull Class<?> paramClass, boolean paramBoolean) {
    for (Activity activity : Utils.sActivityList) {
      if (activity.getClass().equals(paramClass)) {
        activity.finish();
        if (!paramBoolean)
          activity.overridePendingTransition(0, 0); 
      } 
    } 
  }
  
  public static void finishAllActivities() {
    finishAllActivities(false);
  }
  
  public static void finishAllActivities(@AnimRes int paramInt1, @AnimRes int paramInt2) {
    List<Activity> list = Utils.sActivityList;
    for (int i = list.size() - 1; i >= 0; i--) {
      Activity activity = list.get(i);
      activity.finish();
      activity.overridePendingTransition(paramInt1, paramInt2);
    } 
  }
  
  public static void finishAllActivities(boolean paramBoolean) {
    List<Activity> list = Utils.sActivityList;
    for (int i = list.size() - 1; i >= 0; i--) {
      Activity activity = list.get(i);
      activity.finish();
      if (!paramBoolean)
        activity.overridePendingTransition(0, 0); 
    } 
  }
  
  public static void finishOtherActivitiesExceptNewest(@NonNull Class<?> paramClass) {
    finishOtherActivitiesExceptNewest(paramClass, false);
  }
  
  public static void finishOtherActivitiesExceptNewest(@NonNull Class<?> paramClass, @AnimRes int paramInt1, @AnimRes int paramInt2) {
    List<Activity> list = Utils.sActivityList;
    boolean bool = false;
    int i = list.size() - 1;
    while (i >= 0) {
      Activity activity = list.get(i);
      boolean bool1 = bool;
      if (activity.getClass().equals(paramClass))
        if (bool) {
          finishActivity(activity, paramInt1, paramInt2);
          bool1 = bool;
        } else {
          bool1 = true;
        }  
      i--;
      bool = bool1;
    } 
  }
  
  public static void finishOtherActivitiesExceptNewest(@NonNull Class<?> paramClass, boolean paramBoolean) {
    List<Activity> list = Utils.sActivityList;
    boolean bool = false;
    int i = list.size() - 1;
    while (i >= 0) {
      Activity activity = list.get(i);
      boolean bool1 = bool;
      if (activity.getClass().equals(paramClass))
        if (bool) {
          finishActivity(activity, paramBoolean);
          bool1 = bool;
        } else {
          bool1 = true;
        }  
      i--;
      bool = bool1;
    } 
  }
  
  public static boolean finishToActivity(@NonNull Activity paramActivity, boolean paramBoolean) {
    return finishToActivity(paramActivity, paramBoolean, false);
  }
  
  public static boolean finishToActivity(@NonNull Activity paramActivity, boolean paramBoolean, @AnimRes int paramInt1, @AnimRes int paramInt2) {
    // Byte code:
    //   0: getstatic com/blankj/utilcode/util/Utils.sActivityList : Ljava/util/List;
    //   3: astore #4
    //   5: aload #4
    //   7: invokeinterface size : ()I
    //   12: iconst_1
    //   13: isub
    //   14: istore #5
    //   16: iload #5
    //   18: iflt -> 72
    //   21: aload #4
    //   23: iload #5
    //   25: invokeinterface get : (I)Ljava/lang/Object;
    //   30: checkcast android/app/Activity
    //   33: astore #6
    //   35: aload #6
    //   37: aload_0
    //   38: invokevirtual equals : (Ljava/lang/Object;)Z
    //   41: ifeq -> 59
    //   44: iload_1
    //   45: ifeq -> 55
    //   48: aload #6
    //   50: iload_2
    //   51: iload_3
    //   52: invokestatic finishActivity : (Landroid/app/Activity;II)V
    //   55: iconst_1
    //   56: istore_1
    //   57: iload_1
    //   58: ireturn
    //   59: aload #6
    //   61: iload_2
    //   62: iload_3
    //   63: invokestatic finishActivity : (Landroid/app/Activity;II)V
    //   66: iinc #5, -1
    //   69: goto -> 16
    //   72: iconst_0
    //   73: istore_1
    //   74: goto -> 57
  }
  
  public static boolean finishToActivity(@NonNull Activity paramActivity, boolean paramBoolean1, boolean paramBoolean2) {
    // Byte code:
    //   0: getstatic com/blankj/utilcode/util/Utils.sActivityList : Ljava/util/List;
    //   3: astore_3
    //   4: aload_3
    //   5: invokeinterface size : ()I
    //   10: iconst_1
    //   11: isub
    //   12: istore #4
    //   14: iload #4
    //   16: iflt -> 67
    //   19: aload_3
    //   20: iload #4
    //   22: invokeinterface get : (I)Ljava/lang/Object;
    //   27: checkcast android/app/Activity
    //   30: astore #5
    //   32: aload #5
    //   34: aload_0
    //   35: invokevirtual equals : (Ljava/lang/Object;)Z
    //   38: ifeq -> 55
    //   41: iload_1
    //   42: ifeq -> 51
    //   45: aload #5
    //   47: iload_2
    //   48: invokestatic finishActivity : (Landroid/app/Activity;Z)V
    //   51: iconst_1
    //   52: istore_1
    //   53: iload_1
    //   54: ireturn
    //   55: aload #5
    //   57: iload_2
    //   58: invokestatic finishActivity : (Landroid/app/Activity;Z)V
    //   61: iinc #4, -1
    //   64: goto -> 14
    //   67: iconst_0
    //   68: istore_1
    //   69: goto -> 53
  }
  
  public static boolean finishToActivity(@NonNull Class<?> paramClass, boolean paramBoolean) {
    return finishToActivity(paramClass, paramBoolean, false);
  }
  
  public static boolean finishToActivity(@NonNull Class<?> paramClass, boolean paramBoolean, @AnimRes int paramInt1, @AnimRes int paramInt2) {
    // Byte code:
    //   0: getstatic com/blankj/utilcode/util/Utils.sActivityList : Ljava/util/List;
    //   3: astore #4
    //   5: aload #4
    //   7: invokeinterface size : ()I
    //   12: iconst_1
    //   13: isub
    //   14: istore #5
    //   16: iload #5
    //   18: iflt -> 75
    //   21: aload #4
    //   23: iload #5
    //   25: invokeinterface get : (I)Ljava/lang/Object;
    //   30: checkcast android/app/Activity
    //   33: astore #6
    //   35: aload #6
    //   37: invokevirtual getClass : ()Ljava/lang/Class;
    //   40: aload_0
    //   41: invokevirtual equals : (Ljava/lang/Object;)Z
    //   44: ifeq -> 62
    //   47: iload_1
    //   48: ifeq -> 58
    //   51: aload #6
    //   53: iload_2
    //   54: iload_3
    //   55: invokestatic finishActivity : (Landroid/app/Activity;II)V
    //   58: iconst_1
    //   59: istore_1
    //   60: iload_1
    //   61: ireturn
    //   62: aload #6
    //   64: iload_2
    //   65: iload_3
    //   66: invokestatic finishActivity : (Landroid/app/Activity;II)V
    //   69: iinc #5, -1
    //   72: goto -> 16
    //   75: iconst_0
    //   76: istore_1
    //   77: goto -> 60
  }
  
  public static boolean finishToActivity(@NonNull Class<?> paramClass, boolean paramBoolean1, boolean paramBoolean2) {
    // Byte code:
    //   0: getstatic com/blankj/utilcode/util/Utils.sActivityList : Ljava/util/List;
    //   3: astore_3
    //   4: aload_3
    //   5: invokeinterface size : ()I
    //   10: iconst_1
    //   11: isub
    //   12: istore #4
    //   14: iload #4
    //   16: iflt -> 70
    //   19: aload_3
    //   20: iload #4
    //   22: invokeinterface get : (I)Ljava/lang/Object;
    //   27: checkcast android/app/Activity
    //   30: astore #5
    //   32: aload #5
    //   34: invokevirtual getClass : ()Ljava/lang/Class;
    //   37: aload_0
    //   38: invokevirtual equals : (Ljava/lang/Object;)Z
    //   41: ifeq -> 58
    //   44: iload_1
    //   45: ifeq -> 54
    //   48: aload #5
    //   50: iload_2
    //   51: invokestatic finishActivity : (Landroid/app/Activity;Z)V
    //   54: iconst_1
    //   55: istore_1
    //   56: iload_1
    //   57: ireturn
    //   58: aload #5
    //   60: iload_2
    //   61: invokestatic finishActivity : (Landroid/app/Activity;Z)V
    //   64: iinc #4, -1
    //   67: goto -> 14
    //   70: iconst_0
    //   71: istore_1
    //   72: goto -> 56
  }
  
  public static List<Activity> getActivityList() {
    return Utils.sActivityList;
  }
  
  private static Context getActivityOrApp() {
    Application application;
    Activity activity1 = getTopActivity();
    Activity activity2 = activity1;
    if (activity1 == null)
      application = Utils.getApp(); 
    return (Context)application;
  }
  
  public static String getLauncherActivity() {
    return getLauncherActivity(Utils.getApp().getPackageName());
  }
  
  public static String getLauncherActivity(@NonNull String paramString) {
    // Byte code:
    //   0: new android/content/Intent
    //   3: dup
    //   4: ldc 'android.intent.action.MAIN'
    //   6: aconst_null
    //   7: invokespecial <init> : (Ljava/lang/String;Landroid/net/Uri;)V
    //   10: astore_1
    //   11: aload_1
    //   12: ldc 'android.intent.category.LAUNCHER'
    //   14: invokevirtual addCategory : (Ljava/lang/String;)Landroid/content/Intent;
    //   17: pop
    //   18: aload_1
    //   19: ldc 268435456
    //   21: invokevirtual addFlags : (I)Landroid/content/Intent;
    //   24: pop
    //   25: invokestatic getApp : ()Landroid/app/Application;
    //   28: invokevirtual getPackageManager : ()Landroid/content/pm/PackageManager;
    //   31: aload_1
    //   32: iconst_0
    //   33: invokevirtual queryIntentActivities : (Landroid/content/Intent;I)Ljava/util/List;
    //   36: invokeinterface iterator : ()Ljava/util/Iterator;
    //   41: astore_2
    //   42: aload_2
    //   43: invokeinterface hasNext : ()Z
    //   48: ifeq -> 85
    //   51: aload_2
    //   52: invokeinterface next : ()Ljava/lang/Object;
    //   57: checkcast android/content/pm/ResolveInfo
    //   60: astore_1
    //   61: aload_1
    //   62: getfield activityInfo : Landroid/content/pm/ActivityInfo;
    //   65: getfield packageName : Ljava/lang/String;
    //   68: aload_0
    //   69: invokevirtual equals : (Ljava/lang/Object;)Z
    //   72: ifeq -> 42
    //   75: aload_1
    //   76: getfield activityInfo : Landroid/content/pm/ActivityInfo;
    //   79: getfield name : Ljava/lang/String;
    //   82: astore_0
    //   83: aload_0
    //   84: areturn
    //   85: new java/lang/StringBuilder
    //   88: dup
    //   89: invokespecial <init> : ()V
    //   92: ldc 'no '
    //   94: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   97: aload_0
    //   98: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: invokevirtual toString : ()Ljava/lang/String;
    //   104: astore_0
    //   105: goto -> 83
  }
  
  private static Bundle getOptionsBundle(Activity paramActivity, View[] paramArrayOfView) {
    if (Build.VERSION.SDK_INT >= 21) {
      int i = paramArrayOfView.length;
      Pair[] arrayOfPair = new Pair[i];
      for (byte b = 0; b < i; b++)
        arrayOfPair[b] = Pair.create(paramArrayOfView[b], paramArrayOfView[b].getTransitionName()); 
      return ActivityOptionsCompat.makeSceneTransitionAnimation(paramActivity, arrayOfPair).toBundle();
    } 
    return ActivityOptionsCompat.makeSceneTransitionAnimation(paramActivity, null, null).toBundle();
  }
  
  private static Bundle getOptionsBundle(Context paramContext, int paramInt1, int paramInt2) {
    return ActivityOptionsCompat.makeCustomAnimation(paramContext, paramInt1, paramInt2).toBundle();
  }
  
  public static Activity getTopActivity() {
    if (Utils.sTopActivityWeakRef != null) {
      Activity activity = Utils.sTopActivityWeakRef.get();
      if (activity != null)
        return activity; 
    } 
    null = Utils.sActivityList;
    int i = null.size();
    return (i > 0) ? null.get(i - 1) : null;
  }
  
  public static boolean isActivityExists(@NonNull String paramString1, @NonNull String paramString2) {
    boolean bool1 = false;
    Intent intent = new Intent();
    intent.setClassName(paramString1, paramString2);
    boolean bool2 = bool1;
    if (Utils.getApp().getPackageManager().resolveActivity(intent, 0) != null) {
      bool2 = bool1;
      if (intent.resolveActivity(Utils.getApp().getPackageManager()) != null) {
        bool2 = bool1;
        if (Utils.getApp().getPackageManager().queryIntentActivities(intent, 0).size() != 0)
          bool2 = true; 
      } 
    } 
    return bool2;
  }
  
  public static boolean isActivityExistsInStack(@NonNull Activity paramActivity) {
    // Byte code:
    //   0: getstatic com/blankj/utilcode/util/Utils.sActivityList : Ljava/util/List;
    //   3: invokeinterface iterator : ()Ljava/util/Iterator;
    //   8: astore_1
    //   9: aload_1
    //   10: invokeinterface hasNext : ()Z
    //   15: ifeq -> 38
    //   18: aload_1
    //   19: invokeinterface next : ()Ljava/lang/Object;
    //   24: checkcast android/app/Activity
    //   27: aload_0
    //   28: invokevirtual equals : (Ljava/lang/Object;)Z
    //   31: ifeq -> 9
    //   34: iconst_1
    //   35: istore_2
    //   36: iload_2
    //   37: ireturn
    //   38: iconst_0
    //   39: istore_2
    //   40: goto -> 36
  }
  
  public static boolean isActivityExistsInStack(@NonNull Class<?> paramClass) {
    // Byte code:
    //   0: getstatic com/blankj/utilcode/util/Utils.sActivityList : Ljava/util/List;
    //   3: invokeinterface iterator : ()Ljava/util/Iterator;
    //   8: astore_1
    //   9: aload_1
    //   10: invokeinterface hasNext : ()Z
    //   15: ifeq -> 41
    //   18: aload_1
    //   19: invokeinterface next : ()Ljava/lang/Object;
    //   24: checkcast android/app/Activity
    //   27: invokevirtual getClass : ()Ljava/lang/Class;
    //   30: aload_0
    //   31: invokevirtual equals : (Ljava/lang/Object;)Z
    //   34: ifeq -> 9
    //   37: iconst_1
    //   38: istore_2
    //   39: iload_2
    //   40: ireturn
    //   41: iconst_0
    //   42: istore_2
    //   43: goto -> 39
  }
  
  public static void startActivities(@NonNull Activity paramActivity, @NonNull Intent[] paramArrayOfIntent) {
    startActivities(paramArrayOfIntent, (Context)paramActivity, (Bundle)null);
  }
  
  public static void startActivities(@NonNull Activity paramActivity, @NonNull Intent[] paramArrayOfIntent, @AnimRes int paramInt1, @AnimRes int paramInt2) {
    startActivities(paramArrayOfIntent, (Context)paramActivity, getOptionsBundle((Context)paramActivity, paramInt1, paramInt2));
    if (Build.VERSION.SDK_INT < 16)
      paramActivity.overridePendingTransition(paramInt1, paramInt2); 
  }
  
  public static void startActivities(@NonNull Activity paramActivity, @NonNull Intent[] paramArrayOfIntent, @NonNull Bundle paramBundle) {
    startActivities(paramArrayOfIntent, (Context)paramActivity, paramBundle);
  }
  
  public static void startActivities(@NonNull Intent[] paramArrayOfIntent) {
    startActivities(paramArrayOfIntent, getActivityOrApp(), (Bundle)null);
  }
  
  public static void startActivities(@NonNull Intent[] paramArrayOfIntent, @AnimRes int paramInt1, @AnimRes int paramInt2) {
    Context context = getActivityOrApp();
    startActivities(paramArrayOfIntent, context, getOptionsBundle(context, paramInt1, paramInt2));
    if (Build.VERSION.SDK_INT < 16 && context instanceof Activity)
      ((Activity)context).overridePendingTransition(paramInt1, paramInt2); 
  }
  
  private static void startActivities(Intent[] paramArrayOfIntent, Context paramContext, Bundle paramBundle) {
    if (!(paramContext instanceof Activity)) {
      int i = paramArrayOfIntent.length;
      for (byte b = 0; b < i; b++)
        paramArrayOfIntent[b].addFlags(268435456); 
    } 
    if (paramBundle != null && Build.VERSION.SDK_INT >= 16) {
      paramContext.startActivities(paramArrayOfIntent, paramBundle);
      return;
    } 
    paramContext.startActivities(paramArrayOfIntent);
  }
  
  public static void startActivities(@NonNull Intent[] paramArrayOfIntent, @NonNull Bundle paramBundle) {
    startActivities(paramArrayOfIntent, getActivityOrApp(), paramBundle);
  }
  
  public static void startActivity(@NonNull Activity paramActivity, @NonNull Intent paramIntent) {
    startActivity(paramIntent, (Context)paramActivity, (Bundle)null);
  }
  
  public static void startActivity(@NonNull Activity paramActivity, @NonNull Intent paramIntent, @AnimRes int paramInt1, @AnimRes int paramInt2) {
    startActivity(paramIntent, (Context)paramActivity, getOptionsBundle((Context)paramActivity, paramInt1, paramInt2));
    if (Build.VERSION.SDK_INT < 16)
      paramActivity.overridePendingTransition(paramInt1, paramInt2); 
  }
  
  public static void startActivity(@NonNull Activity paramActivity, @NonNull Intent paramIntent, @NonNull Bundle paramBundle) {
    startActivity(paramIntent, (Context)paramActivity, paramBundle);
  }
  
  public static void startActivity(@NonNull Activity paramActivity, @NonNull Intent paramIntent, @NonNull View... paramVarArgs) {
    startActivity(paramIntent, (Context)paramActivity, getOptionsBundle(paramActivity, paramVarArgs));
  }
  
  public static void startActivity(@NonNull Activity paramActivity, @NonNull Class<?> paramClass) {
    startActivity((Context)paramActivity, (Bundle)null, paramActivity.getPackageName(), paramClass.getName(), (Bundle)null);
  }
  
  public static void startActivity(@NonNull Activity paramActivity, @NonNull Class<?> paramClass, @AnimRes int paramInt1, @AnimRes int paramInt2) {
    startActivity((Context)paramActivity, (Bundle)null, paramActivity.getPackageName(), paramClass.getName(), getOptionsBundle((Context)paramActivity, paramInt1, paramInt2));
    if (Build.VERSION.SDK_INT < 16)
      paramActivity.overridePendingTransition(paramInt1, paramInt2); 
  }
  
  public static void startActivity(@NonNull Activity paramActivity, @NonNull Class<?> paramClass, @NonNull Bundle paramBundle) {
    startActivity((Context)paramActivity, (Bundle)null, paramActivity.getPackageName(), paramClass.getName(), paramBundle);
  }
  
  public static void startActivity(@NonNull Activity paramActivity, @NonNull Class<?> paramClass, @NonNull View... paramVarArgs) {
    startActivity((Context)paramActivity, (Bundle)null, paramActivity.getPackageName(), paramClass.getName(), getOptionsBundle(paramActivity, paramVarArgs));
  }
  
  public static void startActivity(@NonNull Activity paramActivity, @NonNull String paramString1, @NonNull String paramString2) {
    startActivity((Context)paramActivity, (Bundle)null, paramString1, paramString2, (Bundle)null);
  }
  
  public static void startActivity(@NonNull Activity paramActivity, @NonNull String paramString1, @NonNull String paramString2, @AnimRes int paramInt1, @AnimRes int paramInt2) {
    startActivity((Context)paramActivity, (Bundle)null, paramString1, paramString2, getOptionsBundle((Context)paramActivity, paramInt1, paramInt2));
    if (Build.VERSION.SDK_INT < 16)
      paramActivity.overridePendingTransition(paramInt1, paramInt2); 
  }
  
  public static void startActivity(@NonNull Activity paramActivity, @NonNull String paramString1, @NonNull String paramString2, @NonNull Bundle paramBundle) {
    startActivity((Context)paramActivity, (Bundle)null, paramString1, paramString2, paramBundle);
  }
  
  public static void startActivity(@NonNull Activity paramActivity, @NonNull String paramString1, @NonNull String paramString2, @NonNull View... paramVarArgs) {
    startActivity((Context)paramActivity, (Bundle)null, paramString1, paramString2, getOptionsBundle(paramActivity, paramVarArgs));
  }
  
  private static void startActivity(Context paramContext, Bundle paramBundle1, String paramString1, String paramString2, Bundle paramBundle2) {
    Intent intent = new Intent("android.intent.action.VIEW");
    if (paramBundle1 != null)
      intent.putExtras(paramBundle1); 
    intent.setComponent(new ComponentName(paramString1, paramString2));
    startActivity(intent, paramContext, paramBundle2);
  }
  
  public static void startActivity(@NonNull Intent paramIntent) {
    startActivity(paramIntent, getActivityOrApp(), (Bundle)null);
  }
  
  public static void startActivity(@NonNull Intent paramIntent, @AnimRes int paramInt1, @AnimRes int paramInt2) {
    Context context = getActivityOrApp();
    startActivity(paramIntent, context, getOptionsBundle(context, paramInt1, paramInt2));
    if (Build.VERSION.SDK_INT < 16 && context instanceof Activity)
      ((Activity)context).overridePendingTransition(paramInt1, paramInt2); 
  }
  
  private static void startActivity(Intent paramIntent, Context paramContext, Bundle paramBundle) {
    if (!(paramContext instanceof Activity))
      paramIntent.addFlags(268435456); 
    if (paramBundle != null && Build.VERSION.SDK_INT >= 16) {
      paramContext.startActivity(paramIntent, paramBundle);
      return;
    } 
    paramContext.startActivity(paramIntent);
  }
  
  public static void startActivity(@NonNull Intent paramIntent, @NonNull Bundle paramBundle) {
    startActivity(paramIntent, getActivityOrApp(), paramBundle);
  }
  
  public static void startActivity(@NonNull Bundle paramBundle, @NonNull Activity paramActivity, @NonNull Class<?> paramClass) {
    startActivity((Context)paramActivity, paramBundle, paramActivity.getPackageName(), paramClass.getName(), (Bundle)null);
  }
  
  public static void startActivity(@NonNull Bundle paramBundle, @NonNull Activity paramActivity, @NonNull Class<?> paramClass, @AnimRes int paramInt1, @AnimRes int paramInt2) {
    startActivity((Context)paramActivity, paramBundle, paramActivity.getPackageName(), paramClass.getName(), getOptionsBundle((Context)paramActivity, paramInt1, paramInt2));
    if (Build.VERSION.SDK_INT < 16)
      paramActivity.overridePendingTransition(paramInt1, paramInt2); 
  }
  
  public static void startActivity(@NonNull Bundle paramBundle1, @NonNull Activity paramActivity, @NonNull Class<?> paramClass, @NonNull Bundle paramBundle2) {
    startActivity((Context)paramActivity, paramBundle1, paramActivity.getPackageName(), paramClass.getName(), paramBundle2);
  }
  
  public static void startActivity(@NonNull Bundle paramBundle, @NonNull Activity paramActivity, @NonNull Class<?> paramClass, @NonNull View... paramVarArgs) {
    startActivity((Context)paramActivity, paramBundle, paramActivity.getPackageName(), paramClass.getName(), getOptionsBundle(paramActivity, paramVarArgs));
  }
  
  public static void startActivity(@NonNull Bundle paramBundle, @NonNull Activity paramActivity, @NonNull String paramString1, @NonNull String paramString2) {
    startActivity((Context)paramActivity, paramBundle, paramString1, paramString2, (Bundle)null);
  }
  
  public static void startActivity(@NonNull Bundle paramBundle, @NonNull Activity paramActivity, @NonNull String paramString1, @NonNull String paramString2, @AnimRes int paramInt1, @AnimRes int paramInt2) {
    startActivity((Context)paramActivity, paramBundle, paramString1, paramString2, getOptionsBundle((Context)paramActivity, paramInt1, paramInt2));
    if (Build.VERSION.SDK_INT < 16)
      paramActivity.overridePendingTransition(paramInt1, paramInt2); 
  }
  
  public static void startActivity(@NonNull Bundle paramBundle1, @NonNull Activity paramActivity, @NonNull String paramString1, @NonNull String paramString2, @NonNull Bundle paramBundle2) {
    startActivity((Context)paramActivity, paramBundle1, paramString1, paramString2, paramBundle2);
  }
  
  public static void startActivity(@NonNull Bundle paramBundle, @NonNull Activity paramActivity, @NonNull String paramString1, @NonNull String paramString2, @NonNull View... paramVarArgs) {
    startActivity((Context)paramActivity, paramBundle, paramString1, paramString2, getOptionsBundle(paramActivity, paramVarArgs));
  }
  
  public static void startActivity(@NonNull Bundle paramBundle, @NonNull Class<?> paramClass) {
    Context context = getActivityOrApp();
    startActivity(context, paramBundle, context.getPackageName(), paramClass.getName(), (Bundle)null);
  }
  
  public static void startActivity(@NonNull Bundle paramBundle, @NonNull Class<?> paramClass, @AnimRes int paramInt1, @AnimRes int paramInt2) {
    Context context = getActivityOrApp();
    startActivity(context, paramBundle, context.getPackageName(), paramClass.getName(), getOptionsBundle(context, paramInt1, paramInt2));
    if (Build.VERSION.SDK_INT < 16 && context instanceof Activity)
      ((Activity)context).overridePendingTransition(paramInt1, paramInt2); 
  }
  
  public static void startActivity(@NonNull Bundle paramBundle1, @NonNull Class<?> paramClass, @NonNull Bundle paramBundle2) {
    Context context = getActivityOrApp();
    startActivity(context, paramBundle1, context.getPackageName(), paramClass.getName(), paramBundle2);
  }
  
  public static void startActivity(@NonNull Bundle paramBundle, @NonNull String paramString1, @NonNull String paramString2) {
    startActivity(getActivityOrApp(), paramBundle, paramString1, paramString2, (Bundle)null);
  }
  
  public static void startActivity(@NonNull Bundle paramBundle, @NonNull String paramString1, @NonNull String paramString2, @AnimRes int paramInt1, @AnimRes int paramInt2) {
    Context context = getActivityOrApp();
    startActivity(context, paramBundle, paramString1, paramString2, getOptionsBundle(context, paramInt1, paramInt2));
    if (Build.VERSION.SDK_INT < 16 && context instanceof Activity)
      ((Activity)context).overridePendingTransition(paramInt1, paramInt2); 
  }
  
  public static void startActivity(@NonNull Bundle paramBundle1, @NonNull String paramString1, @NonNull String paramString2, @NonNull Bundle paramBundle2) {
    startActivity(getActivityOrApp(), paramBundle1, paramString1, paramString2, paramBundle2);
  }
  
  public static void startActivity(@NonNull Class<?> paramClass) {
    Context context = getActivityOrApp();
    startActivity(context, (Bundle)null, context.getPackageName(), paramClass.getName(), (Bundle)null);
  }
  
  public static void startActivity(@NonNull Class<?> paramClass, @AnimRes int paramInt1, @AnimRes int paramInt2) {
    Context context = getActivityOrApp();
    startActivity(context, (Bundle)null, context.getPackageName(), paramClass.getName(), getOptionsBundle(context, paramInt1, paramInt2));
    if (Build.VERSION.SDK_INT < 16 && context instanceof Activity)
      ((Activity)context).overridePendingTransition(paramInt1, paramInt2); 
  }
  
  public static void startActivity(@NonNull Class<?> paramClass, @NonNull Bundle paramBundle) {
    Context context = getActivityOrApp();
    startActivity(context, (Bundle)null, context.getPackageName(), paramClass.getName(), paramBundle);
  }
  
  public static void startActivity(@NonNull String paramString1, @NonNull String paramString2) {
    startActivity(getActivityOrApp(), (Bundle)null, paramString1, paramString2, (Bundle)null);
  }
  
  public static void startActivity(@NonNull String paramString1, @NonNull String paramString2, @AnimRes int paramInt1, @AnimRes int paramInt2) {
    Context context = getActivityOrApp();
    startActivity(context, (Bundle)null, paramString1, paramString2, getOptionsBundle(context, paramInt1, paramInt2));
    if (Build.VERSION.SDK_INT < 16 && context instanceof Activity)
      ((Activity)context).overridePendingTransition(paramInt1, paramInt2); 
  }
  
  public static void startActivity(@NonNull String paramString1, @NonNull String paramString2, @NonNull Bundle paramBundle) {
    startActivity(getActivityOrApp(), (Bundle)null, paramString1, paramString2, paramBundle);
  }
  
  public static void startHomeActivity() {
    Intent intent = new Intent("android.intent.action.MAIN");
    intent.addCategory("android.intent.category.HOME");
    startActivity(intent);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/ActivityUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */