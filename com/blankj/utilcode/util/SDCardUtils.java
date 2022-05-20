package com.blankj.utilcode.util;

import android.os.storage.StorageManager;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SDCardUtils {
  private SDCardUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static List<String> getSDCardPaths() {
    StorageManager storageManager = (StorageManager)Utils.getApp().getSystemService("storage");
    List<String> list = new ArrayList();
    try {
      Method method = StorageManager.class.getMethod("getVolumePaths", new Class[0]);
      method.setAccessible(true);
      List<String> list1 = Arrays.asList((String[])method.invoke(storageManager, new Object[0]));
      list = list1;
    } catch (NoSuchMethodException noSuchMethodException) {
      noSuchMethodException.printStackTrace();
    } catch (IllegalAccessException illegalAccessException) {
      illegalAccessException.printStackTrace();
    } catch (InvocationTargetException invocationTargetException) {
      invocationTargetException.printStackTrace();
    } 
    return list;
  }
  
  public static List<String> getSDCardPaths(boolean paramBoolean) {
    ArrayList<String> arrayList = new ArrayList();
    StorageManager storageManager = (StorageManager)Utils.getApp().getSystemService("storage");
    try {
      Class<?> clazz = Class.forName("android.os.storage.StorageVolume");
      Method method2 = StorageManager.class.getMethod("getVolumeList", new Class[0]);
      Method method3 = clazz.getMethod("getPath", new Class[0]);
      Method method1 = clazz.getMethod("isRemovable", new Class[0]);
      Object object = method2.invoke(storageManager, new Object[0]);
      int i = Array.getLength(object);
      for (byte b = 0; b < i; b++) {
        Object object1 = Array.get(object, b);
        String str = (String)method3.invoke(object1, new Object[0]);
        if (paramBoolean == ((Boolean)method1.invoke(object1, new Object[0])).booleanValue())
          arrayList.add(str); 
      } 
    } catch (ClassNotFoundException classNotFoundException) {
      classNotFoundException.printStackTrace();
    } catch (InvocationTargetException invocationTargetException) {
      invocationTargetException.printStackTrace();
    } catch (NoSuchMethodException noSuchMethodException) {
      noSuchMethodException.printStackTrace();
    } catch (IllegalAccessException illegalAccessException) {
      illegalAccessException.printStackTrace();
    } 
    return arrayList;
  }
  
  public static boolean isSDCardEnable() {
    return !getSDCardPaths().isEmpty();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/SDCardUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */