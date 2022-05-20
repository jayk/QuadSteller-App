package com.blankj.utilcode.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

public final class Utils {
  private static Application.ActivityLifecycleCallbacks mCallbacks;
  
  static List<Activity> sActivityList = new LinkedList<Activity>();
  
  @SuppressLint({"StaticFieldLeak"})
  private static Application sApplication;
  
  static WeakReference<Activity> sTopActivityWeakRef;
  
  static {
    mCallbacks = new Application.ActivityLifecycleCallbacks() {
        public void onActivityCreated(Activity param1Activity, Bundle param1Bundle) {
          Utils.sActivityList.add(param1Activity);
          Utils.setTopActivityWeakRef(param1Activity);
        }
        
        public void onActivityDestroyed(Activity param1Activity) {
          Utils.sActivityList.remove(param1Activity);
        }
        
        public void onActivityPaused(Activity param1Activity) {}
        
        public void onActivityResumed(Activity param1Activity) {
          Utils.setTopActivityWeakRef(param1Activity);
        }
        
        public void onActivitySaveInstanceState(Activity param1Activity, Bundle param1Bundle) {}
        
        public void onActivityStarted(Activity param1Activity) {
          Utils.setTopActivityWeakRef(param1Activity);
        }
        
        public void onActivityStopped(Activity param1Activity) {}
      };
  }
  
  private Utils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static Application getApp() {
    if (sApplication != null)
      return sApplication; 
    throw new NullPointerException("u should init first");
  }
  
  public static void init(@NonNull Application paramApplication) {
    sApplication = paramApplication;
    paramApplication.registerActivityLifecycleCallbacks(mCallbacks);
  }
  
  private static void setTopActivityWeakRef(Activity paramActivity) {
    if (sTopActivityWeakRef == null || !paramActivity.equals(sTopActivityWeakRef.get()))
      sTopActivityWeakRef = new WeakReference<Activity>(paramActivity); 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/Utils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */