package com.blankj.utilcode.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public final class KeyboardUtils {
  private KeyboardUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static void clickBlankArea2HideSoftInput() {
    Log.d("tips", "U should copy the following code.");
  }
  
  public static void hideSoftInput(Activity paramActivity) {
    View view1 = paramActivity.getCurrentFocus();
    View view2 = view1;
    if (view1 == null)
      view2 = new View((Context)paramActivity); 
    InputMethodManager inputMethodManager = (InputMethodManager)paramActivity.getSystemService("input_method");
    if (inputMethodManager != null)
      inputMethodManager.hideSoftInputFromWindow(view2.getWindowToken(), 0); 
  }
  
  public static void hideSoftInput(View paramView) {
    InputMethodManager inputMethodManager = (InputMethodManager)Utils.getApp().getSystemService("input_method");
    if (inputMethodManager != null)
      inputMethodManager.hideSoftInputFromWindow(paramView.getWindowToken(), 0); 
  }
  
  public static void showSoftInput(Activity paramActivity) {
    View view1 = paramActivity.getCurrentFocus();
    View view2 = view1;
    if (view1 == null)
      view2 = new View((Context)paramActivity); 
    InputMethodManager inputMethodManager = (InputMethodManager)paramActivity.getSystemService("input_method");
    if (inputMethodManager != null)
      inputMethodManager.showSoftInput(view2, 2); 
  }
  
  public static void showSoftInput(View paramView) {
    paramView.setFocusable(true);
    paramView.setFocusableInTouchMode(true);
    paramView.requestFocus();
    InputMethodManager inputMethodManager = (InputMethodManager)Utils.getApp().getSystemService("input_method");
    if (inputMethodManager != null)
      inputMethodManager.showSoftInput(paramView, 2); 
  }
  
  public static void toggleSoftInput() {
    InputMethodManager inputMethodManager = (InputMethodManager)Utils.getApp().getSystemService("input_method");
    if (inputMethodManager != null)
      inputMethodManager.toggleSoftInput(2, 0); 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/KeyboardUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */