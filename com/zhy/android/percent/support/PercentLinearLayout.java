package com.zhy.android.percent.support;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class PercentLinearLayout extends LinearLayout {
  private static final String TAG = "PercentLinearLayout";
  
  private PercentLayoutHelper mPercentLayoutHelper = new PercentLayoutHelper((ViewGroup)this);
  
  public PercentLinearLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  private int getScreenHeight() {
    WindowManager windowManager = (WindowManager)getContext().getSystemService("window");
    DisplayMetrics displayMetrics = new DisplayMetrics();
    windowManager.getDefaultDisplay().getMetrics(displayMetrics);
    return displayMetrics.heightPixels;
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    this.mPercentLayoutHelper.restoreOriginalParams();
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.getSize(paramInt2);
    int j = View.MeasureSpec.getMode(paramInt2);
    int k = View.MeasureSpec.makeMeasureSpec(i, j);
    int m = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(paramInt1), View.MeasureSpec.getMode(paramInt1));
    i = k;
    if (j == 0) {
      i = k;
      if (getParent() != null) {
        i = k;
        if (getParent() instanceof android.widget.ScrollView) {
          Context context = getContext();
          if (context instanceof Activity) {
            i = ((Activity)context).findViewById(16908290).getMeasuredHeight();
          } else {
            i = getScreenHeight();
          } 
          i = View.MeasureSpec.makeMeasureSpec(i, j);
        } 
      } 
    } 
    this.mPercentLayoutHelper.adjustChildren(m, i);
    super.onMeasure(paramInt1, paramInt2);
    if (this.mPercentLayoutHelper.handleMeasuredStateTooSmall())
      super.onMeasure(paramInt1, paramInt2); 
  }
  
  public static class LayoutParams extends LinearLayout.LayoutParams implements PercentLayoutHelper.PercentLayoutParams {
    private PercentLayoutHelper.PercentLayoutInfo mPercentLayoutInfo;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      this.mPercentLayoutInfo = PercentLayoutHelper.getPercentLayoutInfo(param1Context, param1AttributeSet);
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
    
    public PercentLayoutHelper.PercentLayoutInfo getPercentLayoutInfo() {
      return this.mPercentLayoutInfo;
    }
    
    protected void setBaseAttributes(TypedArray param1TypedArray, int param1Int1, int param1Int2) {
      PercentLayoutHelper.fetchWidthAndHeight((ViewGroup.LayoutParams)this, param1TypedArray, param1Int1, param1Int2);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/zhy/android/percent/support/PercentLinearLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */