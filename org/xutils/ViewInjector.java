package org.xutils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface ViewInjector {
  View inject(Object paramObject, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup);
  
  void inject(Activity paramActivity);
  
  void inject(View paramView);
  
  void inject(Object paramObject, View paramView);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/ViewInjector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */