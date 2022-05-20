package com.zhy.android.percent.support;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PercentLayoutHelper {
  private static final String REGEX_PERCENT = "^(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)%([s]?[wh]?)$";
  
  private static final String TAG = "PercentLayout";
  
  private static int mHeightScreen;
  
  private static int mWidthScreen;
  
  private final ViewGroup mHost;
  
  public PercentLayoutHelper(ViewGroup paramViewGroup) {
    this.mHost = paramViewGroup;
    getScreenSize();
  }
  
  @NonNull
  private static PercentLayoutInfo checkForInfoExists(PercentLayoutInfo paramPercentLayoutInfo) {
    if (paramPercentLayoutInfo == null)
      paramPercentLayoutInfo = new PercentLayoutInfo(); 
    return paramPercentLayoutInfo;
  }
  
  public static void fetchWidthAndHeight(ViewGroup.LayoutParams paramLayoutParams, TypedArray paramTypedArray, int paramInt1, int paramInt2) {
    paramLayoutParams.width = paramTypedArray.getLayoutDimension(paramInt1, 0);
    paramLayoutParams.height = paramTypedArray.getLayoutDimension(paramInt2, 0);
  }
  
  private static int getBaseByModeAndVal(int paramInt1, int paramInt2, PercentLayoutInfo.BASEMODE paramBASEMODE) {
    switch (paramBASEMODE) {
      default:
        paramInt2 = 0;
      case BASE_HEIGHT:
        return paramInt2;
      case BASE_WIDTH:
        paramInt2 = paramInt1;
      case BASE_SCREEN_WIDTH:
        paramInt2 = mWidthScreen;
      case BASE_SCREEN_HEIGHT:
        break;
    } 
    paramInt2 = mHeightScreen;
  }
  
  public static PercentLayoutInfo getPercentLayoutInfo(Context paramContext, AttributeSet paramAttributeSet) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.PercentLayout_Layout);
    PercentLayoutInfo percentLayoutInfo = setPaddingRelatedVal(typedArray, setMinMaxWidthHeightRelatedVal(typedArray, setTextSizeSupportVal(typedArray, setMarginRelatedVal(typedArray, setWidthAndHeightVal(typedArray, null)))));
    typedArray.recycle();
    if (Log.isLoggable("PercentLayout", 3))
      Log.d("PercentLayout", "constructed: " + percentLayoutInfo); 
    return percentLayoutInfo;
  }
  
  private static PercentLayoutInfo.PercentVal getPercentVal(TypedArray paramTypedArray, int paramInt, boolean paramBoolean) {
    return getPercentVal(paramTypedArray.getString(paramInt), paramBoolean);
  }
  
  private static PercentLayoutInfo.PercentVal getPercentVal(String paramString, boolean paramBoolean) {
    if (paramString == null)
      return null; 
    Matcher matcher = Pattern.compile("^(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)%([s]?[wh]?)$").matcher(paramString);
    if (!matcher.matches())
      throw new RuntimeException("the value of layout_xxxPercent invalid! ==>" + paramString); 
    int i = paramString.length();
    String str = matcher.group(1);
    paramString.substring(i - 1);
    float f = Float.parseFloat(str) / 100.0F;
    PercentLayoutInfo.PercentVal percentVal = new PercentLayoutInfo.PercentVal();
    percentVal.percent = f;
    if (paramString.endsWith("sw")) {
      percentVal.basemode = PercentLayoutInfo.BASEMODE.BASE_SCREEN_WIDTH;
      return percentVal;
    } 
    if (paramString.endsWith("sh")) {
      percentVal.basemode = PercentLayoutInfo.BASEMODE.BASE_SCREEN_HEIGHT;
      return percentVal;
    } 
    if (paramString.endsWith("%")) {
      if (paramBoolean) {
        percentVal.basemode = PercentLayoutInfo.BASEMODE.BASE_WIDTH;
        return percentVal;
      } 
      percentVal.basemode = PercentLayoutInfo.BASEMODE.BASE_HEIGHT;
      return percentVal;
    } 
    if (paramString.endsWith("w")) {
      percentVal.basemode = PercentLayoutInfo.BASEMODE.BASE_WIDTH;
      return percentVal;
    } 
    if (paramString.endsWith("h")) {
      percentVal.basemode = PercentLayoutInfo.BASEMODE.BASE_HEIGHT;
      return percentVal;
    } 
    throw new IllegalArgumentException("the " + paramString + " must be endWith [%|w|h|sw|sh]");
  }
  
  private void getScreenSize() {
    WindowManager windowManager = (WindowManager)this.mHost.getContext().getSystemService("window");
    DisplayMetrics displayMetrics = new DisplayMetrics();
    windowManager.getDefaultDisplay().getMetrics(displayMetrics);
    mWidthScreen = displayMetrics.widthPixels;
    mHeightScreen = displayMetrics.heightPixels;
  }
  
  private void invokeMethod(String paramString, int paramInt1, int paramInt2, View paramView, Class paramClass, PercentLayoutInfo.PercentVal paramPercentVal) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    if (Log.isLoggable("PercentLayout", 3))
      Log.d("PercentLayout", paramString + " ==> " + paramPercentVal); 
    if (paramPercentVal != null) {
      Method method = paramClass.getMethod(paramString, new Class[] { int.class });
      method.setAccessible(true);
      method.invoke(paramView, new Object[] { Integer.valueOf((int)(getBaseByModeAndVal(paramInt1, paramInt2, paramPercentVal.basemode) * paramPercentVal.percent)) });
    } 
  }
  
  private static PercentLayoutInfo setMarginRelatedVal(TypedArray paramTypedArray, PercentLayoutInfo paramPercentLayoutInfo) {
    PercentLayoutInfo.PercentVal percentVal1 = getPercentVal(paramTypedArray, R.styleable.PercentLayout_Layout_layout_marginPercent, true);
    PercentLayoutInfo percentLayoutInfo2 = paramPercentLayoutInfo;
    if (percentVal1 != null) {
      if (Log.isLoggable("PercentLayout", 2))
        Log.v("PercentLayout", "percent margin: " + percentVal1.percent); 
      percentLayoutInfo2 = checkForInfoExists(paramPercentLayoutInfo);
      percentLayoutInfo2.leftMarginPercent = percentVal1;
      percentLayoutInfo2.topMarginPercent = percentVal1;
      percentLayoutInfo2.rightMarginPercent = percentVal1;
      percentLayoutInfo2.bottomMarginPercent = percentVal1;
    } 
    percentVal1 = getPercentVal(paramTypedArray, R.styleable.PercentLayout_Layout_layout_marginLeftPercent, true);
    paramPercentLayoutInfo = percentLayoutInfo2;
    if (percentVal1 != null) {
      if (Log.isLoggable("PercentLayout", 2))
        Log.v("PercentLayout", "percent left margin: " + percentVal1.percent); 
      paramPercentLayoutInfo = checkForInfoExists(percentLayoutInfo2);
      paramPercentLayoutInfo.leftMarginPercent = percentVal1;
    } 
    percentVal1 = getPercentVal(paramTypedArray, R.styleable.PercentLayout_Layout_layout_marginTopPercent, false);
    percentLayoutInfo2 = paramPercentLayoutInfo;
    if (percentVal1 != null) {
      if (Log.isLoggable("PercentLayout", 2))
        Log.v("PercentLayout", "percent top margin: " + percentVal1.percent); 
      percentLayoutInfo2 = checkForInfoExists(paramPercentLayoutInfo);
      percentLayoutInfo2.topMarginPercent = percentVal1;
    } 
    percentVal1 = getPercentVal(paramTypedArray, R.styleable.PercentLayout_Layout_layout_marginRightPercent, true);
    paramPercentLayoutInfo = percentLayoutInfo2;
    if (percentVal1 != null) {
      if (Log.isLoggable("PercentLayout", 2))
        Log.v("PercentLayout", "percent right margin: " + percentVal1.percent); 
      paramPercentLayoutInfo = checkForInfoExists(percentLayoutInfo2);
      paramPercentLayoutInfo.rightMarginPercent = percentVal1;
    } 
    percentVal1 = getPercentVal(paramTypedArray, R.styleable.PercentLayout_Layout_layout_marginBottomPercent, false);
    percentLayoutInfo2 = paramPercentLayoutInfo;
    if (percentVal1 != null) {
      if (Log.isLoggable("PercentLayout", 2))
        Log.v("PercentLayout", "percent bottom margin: " + percentVal1.percent); 
      percentLayoutInfo2 = checkForInfoExists(paramPercentLayoutInfo);
      percentLayoutInfo2.bottomMarginPercent = percentVal1;
    } 
    percentVal1 = getPercentVal(paramTypedArray, R.styleable.PercentLayout_Layout_layout_marginStartPercent, true);
    paramPercentLayoutInfo = percentLayoutInfo2;
    if (percentVal1 != null) {
      if (Log.isLoggable("PercentLayout", 2))
        Log.v("PercentLayout", "percent start margin: " + percentVal1.percent); 
      paramPercentLayoutInfo = checkForInfoExists(percentLayoutInfo2);
      paramPercentLayoutInfo.startMarginPercent = percentVal1;
    } 
    PercentLayoutInfo.PercentVal percentVal2 = getPercentVal(paramTypedArray, R.styleable.PercentLayout_Layout_layout_marginEndPercent, true);
    PercentLayoutInfo percentLayoutInfo1 = paramPercentLayoutInfo;
    if (percentVal2 != null) {
      if (Log.isLoggable("PercentLayout", 2))
        Log.v("PercentLayout", "percent end margin: " + percentVal2.percent); 
      percentLayoutInfo1 = checkForInfoExists(paramPercentLayoutInfo);
      percentLayoutInfo1.endMarginPercent = percentVal2;
    } 
    return percentLayoutInfo1;
  }
  
  private static PercentLayoutInfo setMinMaxWidthHeightRelatedVal(TypedArray paramTypedArray, PercentLayoutInfo paramPercentLayoutInfo) {
    PercentLayoutInfo.PercentVal percentVal2 = getPercentVal(paramTypedArray, R.styleable.PercentLayout_Layout_layout_maxWidthPercent, true);
    PercentLayoutInfo percentLayoutInfo4 = paramPercentLayoutInfo;
    if (percentVal2 != null) {
      percentLayoutInfo4 = checkForInfoExists(paramPercentLayoutInfo);
      percentLayoutInfo4.maxWidthPercent = percentVal2;
    } 
    PercentLayoutInfo.PercentVal percentVal1 = getPercentVal(paramTypedArray, R.styleable.PercentLayout_Layout_layout_maxHeightPercent, false);
    PercentLayoutInfo percentLayoutInfo3 = percentLayoutInfo4;
    if (percentVal1 != null) {
      percentLayoutInfo3 = checkForInfoExists(percentLayoutInfo4);
      percentLayoutInfo3.maxHeightPercent = percentVal1;
    } 
    PercentLayoutInfo.PercentVal percentVal3 = getPercentVal(paramTypedArray, R.styleable.PercentLayout_Layout_layout_minWidthPercent, true);
    PercentLayoutInfo percentLayoutInfo2 = percentLayoutInfo3;
    if (percentVal3 != null) {
      percentLayoutInfo2 = checkForInfoExists(percentLayoutInfo3);
      percentLayoutInfo2.minWidthPercent = percentVal3;
    } 
    percentVal3 = getPercentVal(paramTypedArray, R.styleable.PercentLayout_Layout_layout_minHeightPercent, false);
    PercentLayoutInfo percentLayoutInfo1 = percentLayoutInfo2;
    if (percentVal3 != null) {
      percentLayoutInfo1 = checkForInfoExists(percentLayoutInfo2);
      percentLayoutInfo1.minHeightPercent = percentVal3;
    } 
    return percentLayoutInfo1;
  }
  
  private static PercentLayoutInfo setPaddingRelatedVal(TypedArray paramTypedArray, PercentLayoutInfo paramPercentLayoutInfo) {
    PercentLayoutInfo.PercentVal percentVal1 = getPercentVal(paramTypedArray, R.styleable.PercentLayout_Layout_layout_paddingPercent, true);
    PercentLayoutInfo percentLayoutInfo2 = paramPercentLayoutInfo;
    if (percentVal1 != null) {
      percentLayoutInfo2 = checkForInfoExists(paramPercentLayoutInfo);
      percentLayoutInfo2.paddingLeftPercent = percentVal1;
      percentLayoutInfo2.paddingRightPercent = percentVal1;
      percentLayoutInfo2.paddingBottomPercent = percentVal1;
      percentLayoutInfo2.paddingTopPercent = percentVal1;
    } 
    percentVal1 = getPercentVal(paramTypedArray, R.styleable.PercentLayout_Layout_layout_paddingLeftPercent, true);
    paramPercentLayoutInfo = percentLayoutInfo2;
    if (percentVal1 != null) {
      paramPercentLayoutInfo = checkForInfoExists(percentLayoutInfo2);
      paramPercentLayoutInfo.paddingLeftPercent = percentVal1;
    } 
    percentVal1 = getPercentVal(paramTypedArray, R.styleable.PercentLayout_Layout_layout_paddingRightPercent, true);
    percentLayoutInfo2 = paramPercentLayoutInfo;
    if (percentVal1 != null) {
      percentLayoutInfo2 = checkForInfoExists(paramPercentLayoutInfo);
      percentLayoutInfo2.paddingRightPercent = percentVal1;
    } 
    percentVal1 = getPercentVal(paramTypedArray, R.styleable.PercentLayout_Layout_layout_paddingTopPercent, true);
    paramPercentLayoutInfo = percentLayoutInfo2;
    if (percentVal1 != null) {
      paramPercentLayoutInfo = checkForInfoExists(percentLayoutInfo2);
      paramPercentLayoutInfo.paddingTopPercent = percentVal1;
    } 
    PercentLayoutInfo.PercentVal percentVal2 = getPercentVal(paramTypedArray, R.styleable.PercentLayout_Layout_layout_paddingBottomPercent, true);
    PercentLayoutInfo percentLayoutInfo1 = paramPercentLayoutInfo;
    if (percentVal2 != null) {
      percentLayoutInfo1 = checkForInfoExists(paramPercentLayoutInfo);
      percentLayoutInfo1.paddingBottomPercent = percentVal2;
    } 
    return percentLayoutInfo1;
  }
  
  private static PercentLayoutInfo setTextSizeSupportVal(TypedArray paramTypedArray, PercentLayoutInfo paramPercentLayoutInfo) {
    PercentLayoutInfo.PercentVal percentVal = getPercentVal(paramTypedArray, R.styleable.PercentLayout_Layout_layout_textSizePercent, false);
    PercentLayoutInfo percentLayoutInfo = paramPercentLayoutInfo;
    if (percentVal != null) {
      if (Log.isLoggable("PercentLayout", 2))
        Log.v("PercentLayout", "percent text size: " + percentVal.percent); 
      percentLayoutInfo = checkForInfoExists(paramPercentLayoutInfo);
      percentLayoutInfo.textSizePercent = percentVal;
    } 
    return percentLayoutInfo;
  }
  
  private static PercentLayoutInfo setWidthAndHeightVal(TypedArray paramTypedArray, PercentLayoutInfo paramPercentLayoutInfo) {
    PercentLayoutInfo.PercentVal percentVal2 = getPercentVal(paramTypedArray, R.styleable.PercentLayout_Layout_layout_widthPercent, true);
    PercentLayoutInfo percentLayoutInfo2 = paramPercentLayoutInfo;
    if (percentVal2 != null) {
      if (Log.isLoggable("PercentLayout", 2))
        Log.v("PercentLayout", "percent width: " + percentVal2.percent); 
      percentLayoutInfo2 = checkForInfoExists(paramPercentLayoutInfo);
      percentLayoutInfo2.widthPercent = percentVal2;
    } 
    PercentLayoutInfo.PercentVal percentVal1 = getPercentVal(paramTypedArray, R.styleable.PercentLayout_Layout_layout_heightPercent, false);
    PercentLayoutInfo percentLayoutInfo1 = percentLayoutInfo2;
    if (percentVal1 != null) {
      if (Log.isLoggable("PercentLayout", 2))
        Log.v("PercentLayout", "percent height: " + percentVal1.percent); 
      percentLayoutInfo1 = checkForInfoExists(percentLayoutInfo2);
      percentLayoutInfo1.heightPercent = percentVal1;
    } 
    return percentLayoutInfo1;
  }
  
  private static boolean shouldHandleMeasuredHeightTooSmall(View paramView, PercentLayoutInfo paramPercentLayoutInfo) {
    boolean bool1 = false;
    int i = ViewCompat.getMeasuredHeightAndState(paramView);
    boolean bool2 = bool1;
    if (paramPercentLayoutInfo != null) {
      if (paramPercentLayoutInfo.heightPercent == null)
        return bool1; 
    } else {
      return bool2;
    } 
    bool2 = bool1;
    if ((i & 0xFF000000) == 16777216) {
      bool2 = bool1;
      if (paramPercentLayoutInfo.heightPercent.percent >= 0.0F) {
        bool2 = bool1;
        if (paramPercentLayoutInfo.mPreservedParams.height == -2)
          bool2 = true; 
      } 
    } 
    return bool2;
  }
  
  private static boolean shouldHandleMeasuredWidthTooSmall(View paramView, PercentLayoutInfo paramPercentLayoutInfo) {
    boolean bool1 = false;
    int i = ViewCompat.getMeasuredWidthAndState(paramView);
    boolean bool2 = bool1;
    if (paramPercentLayoutInfo != null) {
      if (paramPercentLayoutInfo.widthPercent == null)
        return bool1; 
    } else {
      return bool2;
    } 
    bool2 = bool1;
    if ((i & 0xFF000000) == 16777216) {
      bool2 = bool1;
      if (paramPercentLayoutInfo.widthPercent.percent >= 0.0F) {
        bool2 = bool1;
        if (paramPercentLayoutInfo.mPreservedParams.width == -2)
          bool2 = true; 
      } 
    } 
    return bool2;
  }
  
  private void supportMinOrMaxDimesion(int paramInt1, int paramInt2, View paramView, PercentLayoutInfo paramPercentLayoutInfo) {
    try {
      Class<?> clazz = paramView.getClass();
      invokeMethod("setMaxWidth", paramInt1, paramInt2, paramView, clazz, paramPercentLayoutInfo.maxWidthPercent);
      invokeMethod("setMaxHeight", paramInt1, paramInt2, paramView, clazz, paramPercentLayoutInfo.maxHeightPercent);
      invokeMethod("setMinWidth", paramInt1, paramInt2, paramView, clazz, paramPercentLayoutInfo.minWidthPercent);
      invokeMethod("setMinHeight", paramInt1, paramInt2, paramView, clazz, paramPercentLayoutInfo.minHeightPercent);
    } catch (NoSuchMethodException noSuchMethodException) {
      noSuchMethodException.printStackTrace();
    } catch (InvocationTargetException invocationTargetException) {
      invocationTargetException.printStackTrace();
    } catch (IllegalAccessException illegalAccessException) {
      illegalAccessException.printStackTrace();
    } 
  }
  
  private void supportPadding(int paramInt1, int paramInt2, View paramView, PercentLayoutInfo paramPercentLayoutInfo) {
    int i = paramView.getPaddingLeft();
    int j = paramView.getPaddingRight();
    int k = paramView.getPaddingTop();
    int m = paramView.getPaddingBottom();
    PercentLayoutInfo.PercentVal percentVal2 = paramPercentLayoutInfo.paddingLeftPercent;
    if (percentVal2 != null)
      i = (int)(getBaseByModeAndVal(paramInt1, paramInt2, percentVal2.basemode) * percentVal2.percent); 
    percentVal2 = paramPercentLayoutInfo.paddingRightPercent;
    if (percentVal2 != null)
      j = (int)(getBaseByModeAndVal(paramInt1, paramInt2, percentVal2.basemode) * percentVal2.percent); 
    percentVal2 = paramPercentLayoutInfo.paddingTopPercent;
    if (percentVal2 != null)
      k = (int)(getBaseByModeAndVal(paramInt1, paramInt2, percentVal2.basemode) * percentVal2.percent); 
    PercentLayoutInfo.PercentVal percentVal1 = paramPercentLayoutInfo.paddingBottomPercent;
    if (percentVal1 != null)
      m = (int)(getBaseByModeAndVal(paramInt1, paramInt2, percentVal1.basemode) * percentVal1.percent); 
    paramView.setPadding(i, k, j, m);
  }
  
  private void supportTextSize(int paramInt1, int paramInt2, View paramView, PercentLayoutInfo paramPercentLayoutInfo) {
    PercentLayoutInfo.PercentVal percentVal = paramPercentLayoutInfo.textSizePercent;
    if (percentVal != null) {
      float f = (int)(getBaseByModeAndVal(paramInt1, paramInt2, percentVal.basemode) * percentVal.percent);
      if (paramView instanceof TextView)
        ((TextView)paramView).setTextSize(0, f); 
    } 
  }
  
  public void adjustChildren(int paramInt1, int paramInt2) {
    if (Log.isLoggable("PercentLayout", 3))
      Log.d("PercentLayout", "adjustChildren: " + this.mHost + " widthMeasureSpec: " + View.MeasureSpec.toString(paramInt1) + " heightMeasureSpec: " + View.MeasureSpec.toString(paramInt2)); 
    int i = View.MeasureSpec.getSize(paramInt1);
    paramInt2 = View.MeasureSpec.getSize(paramInt2);
    if (Log.isLoggable("PercentLayout", 3))
      Log.d("PercentLayout", "widthHint = " + i + " , heightHint = " + paramInt2); 
    paramInt1 = 0;
    int j = this.mHost.getChildCount();
    while (paramInt1 < j) {
      View view = this.mHost.getChildAt(paramInt1);
      ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
      if (Log.isLoggable("PercentLayout", 3))
        Log.d("PercentLayout", "should adjust " + view + " " + layoutParams); 
      if (layoutParams instanceof PercentLayoutParams) {
        PercentLayoutInfo percentLayoutInfo = ((PercentLayoutParams)layoutParams).getPercentLayoutInfo();
        if (Log.isLoggable("PercentLayout", 3))
          Log.d("PercentLayout", "using " + percentLayoutInfo); 
        if (percentLayoutInfo != null) {
          supportTextSize(i, paramInt2, view, percentLayoutInfo);
          supportPadding(i, paramInt2, view, percentLayoutInfo);
          supportMinOrMaxDimesion(i, paramInt2, view, percentLayoutInfo);
          if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            percentLayoutInfo.fillMarginLayoutParams((ViewGroup.MarginLayoutParams)layoutParams, i, paramInt2);
          } else {
            percentLayoutInfo.fillLayoutParams(layoutParams, i, paramInt2);
          } 
        } 
      } 
      paramInt1++;
    } 
  }
  
  public boolean handleMeasuredStateTooSmall() {
    boolean bool = false;
    byte b = 0;
    int i = this.mHost.getChildCount();
    while (b < i) {
      View view = this.mHost.getChildAt(b);
      ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
      if (Log.isLoggable("PercentLayout", 3))
        Log.d("PercentLayout", "should handle measured state too small " + view + " " + layoutParams); 
      boolean bool1 = bool;
      if (layoutParams instanceof PercentLayoutParams) {
        PercentLayoutInfo percentLayoutInfo = ((PercentLayoutParams)layoutParams).getPercentLayoutInfo();
        bool1 = bool;
        if (percentLayoutInfo != null) {
          if (shouldHandleMeasuredWidthTooSmall(view, percentLayoutInfo)) {
            bool = true;
            layoutParams.width = -2;
          } 
          bool1 = bool;
          if (shouldHandleMeasuredHeightTooSmall(view, percentLayoutInfo)) {
            bool1 = true;
            layoutParams.height = -2;
          } 
        } 
      } 
      b++;
      bool = bool1;
    } 
    if (Log.isLoggable("PercentLayout", 3))
      Log.d("PercentLayout", "should trigger second measure pass: " + bool); 
    return bool;
  }
  
  public void restoreOriginalParams() {
    byte b = 0;
    int i = this.mHost.getChildCount();
    while (b < i) {
      View view = this.mHost.getChildAt(b);
      ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
      if (Log.isLoggable("PercentLayout", 3))
        Log.d("PercentLayout", "should restore " + view + " " + layoutParams); 
      if (layoutParams instanceof PercentLayoutParams) {
        PercentLayoutInfo percentLayoutInfo = ((PercentLayoutParams)layoutParams).getPercentLayoutInfo();
        if (Log.isLoggable("PercentLayout", 3))
          Log.d("PercentLayout", "using " + percentLayoutInfo); 
        if (percentLayoutInfo != null)
          if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            percentLayoutInfo.restoreMarginLayoutParams((ViewGroup.MarginLayoutParams)layoutParams);
          } else {
            percentLayoutInfo.restoreLayoutParams(layoutParams);
          }  
      } 
      b++;
    } 
  }
  
  public static class PercentLayoutInfo {
    public PercentVal bottomMarginPercent;
    
    public PercentVal endMarginPercent;
    
    public PercentVal heightPercent;
    
    public PercentVal leftMarginPercent;
    
    final ViewGroup.MarginLayoutParams mPreservedParams = new ViewGroup.MarginLayoutParams(0, 0);
    
    public PercentVal maxHeightPercent;
    
    public PercentVal maxWidthPercent;
    
    public PercentVal minHeightPercent;
    
    public PercentVal minWidthPercent;
    
    public PercentVal paddingBottomPercent;
    
    public PercentVal paddingLeftPercent;
    
    public PercentVal paddingRightPercent;
    
    public PercentVal paddingTopPercent;
    
    public PercentVal rightMarginPercent;
    
    public PercentVal startMarginPercent;
    
    public PercentVal textSizePercent;
    
    public PercentVal topMarginPercent;
    
    public PercentVal widthPercent;
    
    public void fillLayoutParams(ViewGroup.LayoutParams param1LayoutParams, int param1Int1, int param1Int2) {
      this.mPreservedParams.width = param1LayoutParams.width;
      this.mPreservedParams.height = param1LayoutParams.height;
      if (this.widthPercent != null)
        param1LayoutParams.width = (int)(PercentLayoutHelper.getBaseByModeAndVal(param1Int1, param1Int2, this.widthPercent.basemode) * this.widthPercent.percent); 
      if (this.heightPercent != null)
        param1LayoutParams.height = (int)(PercentLayoutHelper.getBaseByModeAndVal(param1Int1, param1Int2, this.heightPercent.basemode) * this.heightPercent.percent); 
      if (Log.isLoggable("PercentLayout", 3))
        Log.d("PercentLayout", "after fillLayoutParams: (" + param1LayoutParams.width + ", " + param1LayoutParams.height + ")"); 
    }
    
    public void fillMarginLayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams, int param1Int1, int param1Int2) {
      fillLayoutParams((ViewGroup.LayoutParams)param1MarginLayoutParams, param1Int1, param1Int2);
      this.mPreservedParams.leftMargin = param1MarginLayoutParams.leftMargin;
      this.mPreservedParams.topMargin = param1MarginLayoutParams.topMargin;
      this.mPreservedParams.rightMargin = param1MarginLayoutParams.rightMargin;
      this.mPreservedParams.bottomMargin = param1MarginLayoutParams.bottomMargin;
      MarginLayoutParamsCompat.setMarginStart(this.mPreservedParams, MarginLayoutParamsCompat.getMarginStart(param1MarginLayoutParams));
      MarginLayoutParamsCompat.setMarginEnd(this.mPreservedParams, MarginLayoutParamsCompat.getMarginEnd(param1MarginLayoutParams));
      if (this.leftMarginPercent != null)
        param1MarginLayoutParams.leftMargin = (int)(PercentLayoutHelper.getBaseByModeAndVal(param1Int1, param1Int2, this.leftMarginPercent.basemode) * this.leftMarginPercent.percent); 
      if (this.topMarginPercent != null)
        param1MarginLayoutParams.topMargin = (int)(PercentLayoutHelper.getBaseByModeAndVal(param1Int1, param1Int2, this.topMarginPercent.basemode) * this.topMarginPercent.percent); 
      if (this.rightMarginPercent != null)
        param1MarginLayoutParams.rightMargin = (int)(PercentLayoutHelper.getBaseByModeAndVal(param1Int1, param1Int2, this.rightMarginPercent.basemode) * this.rightMarginPercent.percent); 
      if (this.bottomMarginPercent != null)
        param1MarginLayoutParams.bottomMargin = (int)(PercentLayoutHelper.getBaseByModeAndVal(param1Int1, param1Int2, this.bottomMarginPercent.basemode) * this.bottomMarginPercent.percent); 
      if (this.startMarginPercent != null)
        MarginLayoutParamsCompat.setMarginStart(param1MarginLayoutParams, (int)(PercentLayoutHelper.getBaseByModeAndVal(param1Int1, param1Int2, this.startMarginPercent.basemode) * this.startMarginPercent.percent)); 
      if (this.endMarginPercent != null)
        MarginLayoutParamsCompat.setMarginEnd(param1MarginLayoutParams, (int)(PercentLayoutHelper.getBaseByModeAndVal(param1Int1, param1Int2, this.endMarginPercent.basemode) * this.endMarginPercent.percent)); 
      if (Log.isLoggable("PercentLayout", 3))
        Log.d("PercentLayout", "after fillMarginLayoutParams: (" + param1MarginLayoutParams.width + ", " + param1MarginLayoutParams.height + ")"); 
    }
    
    public void restoreLayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      param1LayoutParams.width = this.mPreservedParams.width;
      param1LayoutParams.height = this.mPreservedParams.height;
    }
    
    public void restoreMarginLayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      restoreLayoutParams((ViewGroup.LayoutParams)param1MarginLayoutParams);
      param1MarginLayoutParams.leftMargin = this.mPreservedParams.leftMargin;
      param1MarginLayoutParams.topMargin = this.mPreservedParams.topMargin;
      param1MarginLayoutParams.rightMargin = this.mPreservedParams.rightMargin;
      param1MarginLayoutParams.bottomMargin = this.mPreservedParams.bottomMargin;
      MarginLayoutParamsCompat.setMarginStart(param1MarginLayoutParams, MarginLayoutParamsCompat.getMarginStart(this.mPreservedParams));
      MarginLayoutParamsCompat.setMarginEnd(param1MarginLayoutParams, MarginLayoutParamsCompat.getMarginEnd(this.mPreservedParams));
    }
    
    public String toString() {
      return "PercentLayoutInfo{widthPercent=" + this.widthPercent + ", heightPercent=" + this.heightPercent + ", leftMarginPercent=" + this.leftMarginPercent + ", topMarginPercent=" + this.topMarginPercent + ", rightMarginPercent=" + this.rightMarginPercent + ", bottomMarginPercent=" + this.bottomMarginPercent + ", startMarginPercent=" + this.startMarginPercent + ", endMarginPercent=" + this.endMarginPercent + ", textSizePercent=" + this.textSizePercent + ", maxWidthPercent=" + this.maxWidthPercent + ", maxHeightPercent=" + this.maxHeightPercent + ", minWidthPercent=" + this.minWidthPercent + ", minHeightPercent=" + this.minHeightPercent + ", paddingLeftPercent=" + this.paddingLeftPercent + ", paddingRightPercent=" + this.paddingRightPercent + ", paddingTopPercent=" + this.paddingTopPercent + ", paddingBottomPercent=" + this.paddingBottomPercent + ", mPreservedParams=" + this.mPreservedParams + '}';
    }
    
    private enum BASEMODE {
      BASE_HEIGHT, BASE_SCREEN_HEIGHT, BASE_SCREEN_WIDTH, BASE_WIDTH;
      
      public static final String H = "h";
      
      public static final String PERCENT = "%";
      
      public static final String SH = "sh";
      
      public static final String SW = "sw";
      
      public static final String W = "w";
      
      static {
        $VALUES = new BASEMODE[] { BASE_WIDTH, BASE_HEIGHT, BASE_SCREEN_WIDTH, BASE_SCREEN_HEIGHT };
      }
    }
    
    public static class PercentVal {
      public PercentLayoutHelper.PercentLayoutInfo.BASEMODE basemode;
      
      public float percent = -1.0F;
      
      public PercentVal() {}
      
      public PercentVal(float param2Float, PercentLayoutHelper.PercentLayoutInfo.BASEMODE param2BASEMODE) {
        this.percent = param2Float;
        this.basemode = param2BASEMODE;
      }
      
      public String toString() {
        return "PercentVal{percent=" + this.percent + ", basemode=" + this.basemode.name() + '}';
      }
    }
  }
  
  private enum BASEMODE {
    BASE_HEIGHT, BASE_SCREEN_HEIGHT, BASE_SCREEN_WIDTH, BASE_WIDTH;
    
    public static final String H = "h";
    
    public static final String PERCENT = "%";
    
    public static final String SH = "sh";
    
    public static final String SW = "sw";
    
    public static final String W = "w";
    
    static {
      BASE_SCREEN_HEIGHT = new BASEMODE("BASE_SCREEN_HEIGHT", 3);
      $VALUES = new BASEMODE[] { BASE_WIDTH, BASE_HEIGHT, BASE_SCREEN_WIDTH, BASE_SCREEN_HEIGHT };
    }
  }
  
  public static class PercentVal {
    public PercentLayoutHelper.PercentLayoutInfo.BASEMODE basemode;
    
    public float percent = -1.0F;
    
    public PercentVal() {}
    
    public PercentVal(float param1Float, PercentLayoutHelper.PercentLayoutInfo.BASEMODE param1BASEMODE) {
      this.percent = param1Float;
      this.basemode = param1BASEMODE;
    }
    
    public String toString() {
      return "PercentVal{percent=" + this.percent + ", basemode=" + this.basemode.name() + '}';
    }
  }
  
  public static interface PercentLayoutParams {
    PercentLayoutHelper.PercentLayoutInfo getPercentLayoutInfo();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/zhy/android/percent/support/PercentLayoutHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */