package com.blankj.utilcode.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

public final class BarUtils {
  private static final int DEFAULT_ALPHA = 112;
  
  private static final String TAG_ALPHA = "TAG_ALPHA";
  
  private static final String TAG_COLOR = "TAG_COLOR";
  
  private static final int TAG_OFFSET = -123;
  
  private BarUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static void addMarginTopEqualStatusBarHeight(@NonNull View paramView) {
    Object object = paramView.getTag(-123);
    if (object == null || !((Boolean)object).booleanValue()) {
      object = paramView.getLayoutParams();
      object.setMargins(((ViewGroup.MarginLayoutParams)object).leftMargin, ((ViewGroup.MarginLayoutParams)object).topMargin + getStatusBarHeight(), ((ViewGroup.MarginLayoutParams)object).rightMargin, ((ViewGroup.MarginLayoutParams)object).bottomMargin);
      paramView.setTag(-123, Boolean.valueOf(true));
    } 
  }
  
  private static void addStatusBarAlpha(Activity paramActivity, int paramInt, boolean paramBoolean) {
    ViewGroup viewGroup;
    if (paramBoolean) {
      viewGroup = (ViewGroup)paramActivity.getWindow().getDecorView();
    } else {
      viewGroup = (ViewGroup)viewGroup.findViewById(16908290);
    } 
    View view = viewGroup.findViewWithTag("TAG_ALPHA");
    if (view != null) {
      if (view.getVisibility() == 8)
        view.setVisibility(0); 
      view.setBackgroundColor(Color.argb(paramInt, 0, 0, 0));
      return;
    } 
    viewGroup.addView(createAlphaStatusBarView(viewGroup.getContext(), paramInt));
  }
  
  private static void addStatusBarColor(Activity paramActivity, int paramInt1, int paramInt2, boolean paramBoolean) {
    ViewGroup viewGroup;
    if (paramBoolean) {
      viewGroup = (ViewGroup)paramActivity.getWindow().getDecorView();
    } else {
      viewGroup = (ViewGroup)viewGroup.findViewById(16908290);
    } 
    View view = viewGroup.findViewWithTag("TAG_COLOR");
    if (view != null) {
      if (view.getVisibility() == 8)
        view.setVisibility(0); 
      view.setBackgroundColor(getStatusBarColor(paramInt1, paramInt2));
      return;
    } 
    viewGroup.addView(createColorStatusBarView(viewGroup.getContext(), paramInt1, paramInt2));
  }
  
  private static View createAlphaStatusBarView(Context paramContext, int paramInt) {
    View view = new View(paramContext);
    view.setLayoutParams((ViewGroup.LayoutParams)new LinearLayout.LayoutParams(-1, getStatusBarHeight()));
    view.setBackgroundColor(Color.argb(paramInt, 0, 0, 0));
    view.setTag("TAG_ALPHA");
    return view;
  }
  
  private static View createColorStatusBarView(Context paramContext, int paramInt1, int paramInt2) {
    View view = new View(paramContext);
    view.setLayoutParams((ViewGroup.LayoutParams)new LinearLayout.LayoutParams(-1, getStatusBarHeight()));
    view.setBackgroundColor(getStatusBarColor(paramInt1, paramInt2));
    view.setTag("TAG_COLOR");
    return view;
  }
  
  public static int getActionBarHeight(@NonNull Activity paramActivity) {
    TypedValue typedValue = new TypedValue();
    return paramActivity.getTheme().resolveAttribute(16843499, typedValue, true) ? TypedValue.complexToDimensionPixelSize(typedValue.data, paramActivity.getResources().getDisplayMetrics()) : 0;
  }
  
  public static int getNavBarHeight() {
    Resources resources = Utils.getApp().getResources();
    null = resources.getIdentifier("navigation_bar_height", "dimen", "android");
    return (null != 0) ? resources.getDimensionPixelSize(null) : 0;
  }
  
  private static int getStatusBarColor(int paramInt1, int paramInt2) {
    if (paramInt2 != 0) {
      float f = 1.0F - paramInt2 / 255.0F;
      paramInt1 = Color.argb(255, (int)(((paramInt1 >> 16 & 0xFF) * f) + 0.5D), (int)(((paramInt1 >> 8 & 0xFF) * f) + 0.5D), (int)(((paramInt1 & 0xFF) * f) + 0.5D));
    } 
    return paramInt1;
  }
  
  public static int getStatusBarHeight() {
    Resources resources = Utils.getApp().getResources();
    return resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"));
  }
  
  private static void hideAlphaView(Activity paramActivity) {
    View view = ((ViewGroup)paramActivity.getWindow().getDecorView()).findViewWithTag("TAG_ALPHA");
    if (view != null)
      view.setVisibility(8); 
  }
  
  private static void hideColorView(Activity paramActivity) {
    View view = ((ViewGroup)paramActivity.getWindow().getDecorView()).findViewWithTag("TAG_COLOR");
    if (view != null)
      view.setVisibility(8); 
  }
  
  public static void hideNavBar(@NonNull Activity paramActivity) {
    if (Build.VERSION.SDK_INT >= 16 && getNavBarHeight() > 0)
      paramActivity.getWindow().getDecorView().setSystemUiVisibility(4610); 
  }
  
  public static void hideNotificationBar(@NonNull Context paramContext) {
    String str;
    if (Build.VERSION.SDK_INT <= 16) {
      str = "collapse";
    } else {
      str = "collapsePanels";
    } 
    invokePanels(paramContext, str);
  }
  
  private static void invokePanels(@NonNull Context paramContext, String paramString) {
    try {
      Object object = paramContext.getSystemService("statusbar");
      Class.forName("android.app.StatusBarManager").getMethod(paramString, new Class[0]).invoke(object, new Object[0]);
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public static void setStatusBarAlpha(@NonNull Activity paramActivity) {
    setStatusBarAlpha(paramActivity, 112, false);
  }
  
  public static void setStatusBarAlpha(@NonNull Activity paramActivity, @IntRange(from = 0L, to = 255L) int paramInt) {
    setStatusBarAlpha(paramActivity, paramInt, false);
  }
  
  public static void setStatusBarAlpha(@NonNull Activity paramActivity, @IntRange(from = 0L, to = 255L) int paramInt, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 19) {
      hideColorView(paramActivity);
      transparentStatusBar(paramActivity);
      addStatusBarAlpha(paramActivity, paramInt, paramBoolean);
    } 
  }
  
  public static void setStatusBarAlpha(@NonNull View paramView) {
    setStatusBarAlpha(paramView, 112);
  }
  
  public static void setStatusBarAlpha(@NonNull View paramView, @IntRange(from = 0L, to = 255L) int paramInt) {
    if (Build.VERSION.SDK_INT >= 19) {
      paramView.setVisibility(0);
      transparentStatusBar((Activity)paramView.getContext());
      ViewGroup.LayoutParams layoutParams = paramView.getLayoutParams();
      layoutParams.width = -1;
      layoutParams.height = getStatusBarHeight();
      paramView.setBackgroundColor(Color.argb(paramInt, 0, 0, 0));
    } 
  }
  
  public static void setStatusBarAlpha4Drawer(@NonNull Activity paramActivity, @NonNull DrawerLayout paramDrawerLayout, @NonNull View paramView, @IntRange(from = 0L, to = 255L) int paramInt, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 19) {
      paramDrawerLayout.setFitsSystemWindows(false);
      transparentStatusBar(paramActivity);
      if (paramBoolean) {
        b = paramInt;
      } else {
        b = 0;
      } 
      setStatusBarAlpha(paramView, b);
      byte b = 0;
      int i = paramDrawerLayout.getChildCount();
      while (b < i) {
        paramDrawerLayout.getChildAt(b).setFitsSystemWindows(false);
        b++;
      } 
      if (paramBoolean) {
        hideAlphaView(paramActivity);
        return;
      } 
      addStatusBarAlpha(paramActivity, paramInt, false);
    } 
  }
  
  public static void setStatusBarAlpha4Drawer(@NonNull Activity paramActivity, @NonNull DrawerLayout paramDrawerLayout, @NonNull View paramView, boolean paramBoolean) {
    setStatusBarAlpha4Drawer(paramActivity, paramDrawerLayout, paramView, 112, paramBoolean);
  }
  
  public static void setStatusBarColor(@NonNull Activity paramActivity, @ColorInt int paramInt) {
    setStatusBarColor(paramActivity, paramInt, 112, false);
  }
  
  public static void setStatusBarColor(@NonNull Activity paramActivity, @ColorInt int paramInt1, @IntRange(from = 0L, to = 255L) int paramInt2) {
    setStatusBarColor(paramActivity, paramInt1, paramInt2, false);
  }
  
  public static void setStatusBarColor(@NonNull Activity paramActivity, @ColorInt int paramInt1, @IntRange(from = 0L, to = 255L) int paramInt2, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 19) {
      hideAlphaView(paramActivity);
      transparentStatusBar(paramActivity);
      addStatusBarColor(paramActivity, paramInt1, paramInt2, paramBoolean);
    } 
  }
  
  public static void setStatusBarColor(@NonNull View paramView, @ColorInt int paramInt) {
    setStatusBarColor(paramView, paramInt, 112);
  }
  
  public static void setStatusBarColor(@NonNull View paramView, @ColorInt int paramInt1, @IntRange(from = 0L, to = 255L) int paramInt2) {
    if (Build.VERSION.SDK_INT >= 19) {
      paramView.setVisibility(0);
      transparentStatusBar((Activity)paramView.getContext());
      ViewGroup.LayoutParams layoutParams = paramView.getLayoutParams();
      layoutParams.width = -1;
      layoutParams.height = getStatusBarHeight();
      paramView.setBackgroundColor(getStatusBarColor(paramInt1, paramInt2));
    } 
  }
  
  public static void setStatusBarColor4Drawer(@NonNull Activity paramActivity, @NonNull DrawerLayout paramDrawerLayout, @NonNull View paramView, @ColorInt int paramInt1, @IntRange(from = 0L, to = 255L) int paramInt2, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 19) {
      paramDrawerLayout.setFitsSystemWindows(false);
      transparentStatusBar(paramActivity);
      if (paramBoolean) {
        i = paramInt2;
      } else {
        i = 0;
      } 
      setStatusBarColor(paramView, paramInt1, i);
      paramInt1 = 0;
      int i = paramDrawerLayout.getChildCount();
      while (paramInt1 < i) {
        paramDrawerLayout.getChildAt(paramInt1).setFitsSystemWindows(false);
        paramInt1++;
      } 
      if (paramBoolean) {
        hideAlphaView(paramActivity);
        return;
      } 
      addStatusBarAlpha(paramActivity, paramInt2, false);
    } 
  }
  
  public static void setStatusBarColor4Drawer(@NonNull Activity paramActivity, @NonNull DrawerLayout paramDrawerLayout, @NonNull View paramView, @ColorInt int paramInt, boolean paramBoolean) {
    setStatusBarColor4Drawer(paramActivity, paramDrawerLayout, paramView, paramInt, 112, paramBoolean);
  }
  
  public static void showNotificationBar(@NonNull Context paramContext, boolean paramBoolean) {
    String str;
    if (Build.VERSION.SDK_INT <= 16) {
      str = "expand";
    } else if (paramBoolean) {
      str = "expandSettingsPanel";
    } else {
      str = "expandNotificationsPanel";
    } 
    invokePanels(paramContext, str);
  }
  
  public static void subtractMarginTopEqualStatusBarHeight(@NonNull View paramView) {
    Object object = paramView.getTag(-123);
    if (object != null && ((Boolean)object).booleanValue()) {
      object = paramView.getLayoutParams();
      object.setMargins(((ViewGroup.MarginLayoutParams)object).leftMargin, ((ViewGroup.MarginLayoutParams)object).topMargin - getStatusBarHeight(), ((ViewGroup.MarginLayoutParams)object).rightMargin, ((ViewGroup.MarginLayoutParams)object).bottomMargin);
      paramView.setTag(-123, Boolean.valueOf(false));
    } 
  }
  
  private static void transparentStatusBar(Activity paramActivity) {
    if (Build.VERSION.SDK_INT >= 19) {
      Window window = paramActivity.getWindow();
      if (Build.VERSION.SDK_INT >= 21) {
        window.getDecorView().setSystemUiVisibility(1280);
        window.setStatusBarColor(0);
        return;
      } 
      window.addFlags(67108864);
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/BarUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */