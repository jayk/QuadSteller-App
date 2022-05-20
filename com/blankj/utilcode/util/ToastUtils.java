package com.blankj.utilcode.util;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.ref.WeakReference;

public final class ToastUtils {
  private static final int COLOR_DEFAULT = -16777217;
  
  private static final Handler HANDLER = new Handler(Looper.getMainLooper());
  
  private static int bgColor;
  
  private static int bgResource;
  
  private static int gravity;
  
  private static int msgColor;
  
  private static int sLayoutId = -1;
  
  private static Toast sToast;
  
  private static WeakReference<View> sViewWeakReference;
  
  private static int xOffset;
  
  private static int yOffset;
  
  static {
    gravity = 81;
    xOffset = 0;
    yOffset = (int)((64.0F * (Utils.getApp().getResources().getDisplayMetrics()).density) + 0.5D);
    bgColor = -16777217;
    bgResource = -1;
    msgColor = -16777217;
  }
  
  private ToastUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static void cancel() {
    if (sToast != null) {
      sToast.cancel();
      sToast = null;
    } 
  }
  
  private static View getView(@LayoutRes int paramInt) {
    if (sLayoutId == paramInt && sViewWeakReference != null) {
      View view1 = sViewWeakReference.get();
      if (view1 != null)
        return view1; 
    } 
    View view = ((LayoutInflater)Utils.getApp().getSystemService("layout_inflater")).inflate(paramInt, null);
    sViewWeakReference = new WeakReference<View>(view);
    sLayoutId = paramInt;
    return view;
  }
  
  private static void setBgAndGravity() {
    View view = sToast.getView();
    if (bgResource != -1) {
      view.setBackgroundResource(bgResource);
    } else if (bgColor != -16777217) {
      view.getBackground().setColorFilter((ColorFilter)new PorterDuffColorFilter(bgColor, PorterDuff.Mode.SRC_IN));
    } 
    sToast.setGravity(gravity, xOffset, yOffset);
  }
  
  public static void setBgColor(@ColorInt int paramInt) {
    bgColor = paramInt;
  }
  
  public static void setBgResource(@DrawableRes int paramInt) {
    bgResource = paramInt;
  }
  
  public static void setGravity(int paramInt1, int paramInt2, int paramInt3) {
    gravity = paramInt1;
    xOffset = paramInt2;
    yOffset = paramInt3;
  }
  
  public static void setMsgColor(@ColorInt int paramInt) {
    msgColor = paramInt;
  }
  
  private static void show(@StringRes int paramInt1, int paramInt2) {
    show(Utils.getApp().getResources().getText(paramInt1).toString(), paramInt2);
  }
  
  private static void show(@StringRes int paramInt1, int paramInt2, Object... paramVarArgs) {
    show(String.format(Utils.getApp().getResources().getString(paramInt1), paramVarArgs), paramInt2);
  }
  
  private static void show(final View view, final int duration) {
    HANDLER.post(new Runnable() {
          public void run() {
            ToastUtils.cancel();
            ToastUtils.access$002(new Toast((Context)Utils.getApp()));
            ToastUtils.sToast.setView(view);
            ToastUtils.sToast.setDuration(duration);
            ToastUtils.setBgAndGravity();
            ToastUtils.sToast.show();
          }
        });
  }
  
  private static void show(final CharSequence text, final int duration) {
    HANDLER.post(new Runnable() {
          public void run() {
            ToastUtils.cancel();
            ToastUtils.access$002(Toast.makeText((Context)Utils.getApp(), text, duration));
            TextView textView = (TextView)ToastUtils.sToast.getView().findViewById(16908299);
            TextViewCompat.setTextAppearance(textView, 16973886);
            textView.setTextColor(ToastUtils.msgColor);
            ToastUtils.setBgAndGravity();
            ToastUtils.sToast.show();
          }
        });
  }
  
  private static void show(String paramString, int paramInt, Object... paramVarArgs) {
    show(String.format(paramString, paramVarArgs), paramInt);
  }
  
  public static View showCustomLong(@LayoutRes int paramInt) {
    View view = getView(paramInt);
    show(view, 1);
    return view;
  }
  
  public static View showCustomShort(@LayoutRes int paramInt) {
    View view = getView(paramInt);
    show(view, 0);
    return view;
  }
  
  public static void showLong(@StringRes int paramInt) {
    show(paramInt, 1);
  }
  
  public static void showLong(@StringRes int paramInt, Object... paramVarArgs) {
    show(paramInt, 1, paramVarArgs);
  }
  
  public static void showLong(@NonNull CharSequence paramCharSequence) {
    show(paramCharSequence, 1);
  }
  
  public static void showLong(String paramString, Object... paramVarArgs) {
    show(paramString, 1, paramVarArgs);
  }
  
  public static void showShort(@StringRes int paramInt) {
    show(paramInt, 0);
  }
  
  public static void showShort(@StringRes int paramInt, Object... paramVarArgs) {
    show(paramInt, 0, paramVarArgs);
  }
  
  public static void showShort(@NonNull CharSequence paramCharSequence) {
    show(paramCharSequence, 0);
  }
  
  public static void showShort(String paramString, Object... paramVarArgs) {
    show(paramString, 0, paramVarArgs);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/ToastUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */