package com.blankj.utilcode.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class ServiceUtils {
  private ServiceUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static void bindService(Class<?> paramClass, ServiceConnection paramServiceConnection, int paramInt) {
    Intent intent = new Intent((Context)Utils.getApp(), paramClass);
    Utils.getApp().bindService(intent, paramServiceConnection, paramInt);
  }
  
  public static void bindService(String paramString, ServiceConnection paramServiceConnection, int paramInt) {
    try {
      bindService(Class.forName(paramString), paramServiceConnection, paramInt);
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public static Set getAllRunningService() {
    List list = ((ActivityManager)Utils.getApp().getSystemService("activity")).getRunningServices(2147483647);
    HashSet<String> hashSet = new HashSet();
    if (list == null || list.size() == 0)
      return null; 
    Iterator iterator = list.iterator();
    while (true) {
      HashSet<String> hashSet1 = hashSet;
      if (iterator.hasNext()) {
        hashSet.add(((ActivityManager.RunningServiceInfo)iterator.next()).service.getClassName());
        continue;
      } 
      return hashSet1;
    } 
  }
  
  public static boolean isServiceRunning(String paramString) {
    boolean bool1 = false;
    List list = ((ActivityManager)Utils.getApp().getSystemService("activity")).getRunningServices(2147483647);
    boolean bool2 = bool1;
    if (list != null) {
      if (list.size() == 0)
        return bool1; 
    } else {
      return bool2;
    } 
    Iterator iterator = list.iterator();
    while (true) {
      bool2 = bool1;
      if (iterator.hasNext()) {
        if (paramString.equals(((ActivityManager.RunningServiceInfo)iterator.next()).service.getClassName()))
          return true; 
        continue;
      } 
      return bool2;
    } 
  }
  
  public static void startService(Class<?> paramClass) {
    Intent intent = new Intent((Context)Utils.getApp(), paramClass);
    Utils.getApp().startService(intent);
  }
  
  public static void startService(String paramString) {
    try {
      startService(Class.forName(paramString));
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public static boolean stopService(Class<?> paramClass) {
    Intent intent = new Intent((Context)Utils.getApp(), paramClass);
    return Utils.getApp().stopService(intent);
  }
  
  public static boolean stopService(String paramString) {
    boolean bool;
    try {
      bool = stopService(Class.forName(paramString));
    } catch (Exception exception) {
      exception.printStackTrace();
      bool = false;
    } 
    return bool;
  }
  
  public static void unbindService(ServiceConnection paramServiceConnection) {
    Utils.getApp().unbindService(paramServiceConnection);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/ServiceUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */