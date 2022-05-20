package android.support.percent;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class PercentLayoutHelper {
  private static final boolean DEBUG = false;
  
  private static final String TAG = "PercentLayout";
  
  private static final boolean VERBOSE = false;
  
  private final ViewGroup mHost;
  
  public PercentLayoutHelper(@NonNull ViewGroup paramViewGroup) {
    if (paramViewGroup == null)
      throw new IllegalArgumentException("host must be non-null"); 
    this.mHost = paramViewGroup;
  }
  
  public static void fetchWidthAndHeight(ViewGroup.LayoutParams paramLayoutParams, TypedArray paramTypedArray, int paramInt1, int paramInt2) {
    paramLayoutParams.width = paramTypedArray.getLayoutDimension(paramInt1, 0);
    paramLayoutParams.height = paramTypedArray.getLayoutDimension(paramInt2, 0);
  }
  
  public static PercentLayoutInfo getPercentLayoutInfo(Context paramContext, AttributeSet paramAttributeSet) {
    Context context1 = null;
    Context context2 = null;
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.PercentLayout_Layout);
    float f = typedArray.getFraction(R.styleable.PercentLayout_Layout_layout_widthPercent, 1, 1, -1.0F);
    paramContext = context1;
    if (f != -1.0F) {
      if (false) {
        paramContext = context2;
      } else {
        percentLayoutInfo1 = new PercentLayoutInfo();
      } 
      percentLayoutInfo1.widthPercent = f;
    } 
    f = typedArray.getFraction(R.styleable.PercentLayout_Layout_layout_heightPercent, 1, 1, -1.0F);
    PercentLayoutInfo percentLayoutInfo2 = percentLayoutInfo1;
    if (f != -1.0F) {
      if (percentLayoutInfo1 == null)
        percentLayoutInfo1 = new PercentLayoutInfo(); 
      percentLayoutInfo1.heightPercent = f;
      percentLayoutInfo2 = percentLayoutInfo1;
    } 
    f = typedArray.getFraction(R.styleable.PercentLayout_Layout_layout_marginPercent, 1, 1, -1.0F);
    PercentLayoutInfo percentLayoutInfo1 = percentLayoutInfo2;
    if (f != -1.0F) {
      if (percentLayoutInfo2 != null) {
        percentLayoutInfo1 = percentLayoutInfo2;
      } else {
        percentLayoutInfo1 = new PercentLayoutInfo();
      } 
      percentLayoutInfo1.leftMarginPercent = f;
      percentLayoutInfo1.topMarginPercent = f;
      percentLayoutInfo1.rightMarginPercent = f;
      percentLayoutInfo1.bottomMarginPercent = f;
    } 
    f = typedArray.getFraction(R.styleable.PercentLayout_Layout_layout_marginLeftPercent, 1, 1, -1.0F);
    percentLayoutInfo2 = percentLayoutInfo1;
    if (f != -1.0F) {
      if (percentLayoutInfo1 == null)
        percentLayoutInfo1 = new PercentLayoutInfo(); 
      percentLayoutInfo1.leftMarginPercent = f;
      percentLayoutInfo2 = percentLayoutInfo1;
    } 
    f = typedArray.getFraction(R.styleable.PercentLayout_Layout_layout_marginTopPercent, 1, 1, -1.0F);
    percentLayoutInfo1 = percentLayoutInfo2;
    if (f != -1.0F) {
      if (percentLayoutInfo2 != null) {
        percentLayoutInfo1 = percentLayoutInfo2;
      } else {
        percentLayoutInfo1 = new PercentLayoutInfo();
      } 
      percentLayoutInfo1.topMarginPercent = f;
    } 
    f = typedArray.getFraction(R.styleable.PercentLayout_Layout_layout_marginRightPercent, 1, 1, -1.0F);
    percentLayoutInfo2 = percentLayoutInfo1;
    if (f != -1.0F) {
      if (percentLayoutInfo1 == null)
        percentLayoutInfo1 = new PercentLayoutInfo(); 
      percentLayoutInfo1.rightMarginPercent = f;
      percentLayoutInfo2 = percentLayoutInfo1;
    } 
    f = typedArray.getFraction(R.styleable.PercentLayout_Layout_layout_marginBottomPercent, 1, 1, -1.0F);
    percentLayoutInfo1 = percentLayoutInfo2;
    if (f != -1.0F) {
      if (percentLayoutInfo2 != null) {
        percentLayoutInfo1 = percentLayoutInfo2;
      } else {
        percentLayoutInfo1 = new PercentLayoutInfo();
      } 
      percentLayoutInfo1.bottomMarginPercent = f;
    } 
    f = typedArray.getFraction(R.styleable.PercentLayout_Layout_layout_marginStartPercent, 1, 1, -1.0F);
    percentLayoutInfo2 = percentLayoutInfo1;
    if (f != -1.0F) {
      if (percentLayoutInfo1 == null)
        percentLayoutInfo1 = new PercentLayoutInfo(); 
      percentLayoutInfo1.startMarginPercent = f;
      percentLayoutInfo2 = percentLayoutInfo1;
    } 
    f = typedArray.getFraction(R.styleable.PercentLayout_Layout_layout_marginEndPercent, 1, 1, -1.0F);
    percentLayoutInfo1 = percentLayoutInfo2;
    if (f != -1.0F) {
      if (percentLayoutInfo2 != null) {
        percentLayoutInfo1 = percentLayoutInfo2;
      } else {
        percentLayoutInfo1 = new PercentLayoutInfo();
      } 
      percentLayoutInfo1.endMarginPercent = f;
    } 
    f = typedArray.getFraction(R.styleable.PercentLayout_Layout_layout_aspectRatio, 1, 1, -1.0F);
    percentLayoutInfo2 = percentLayoutInfo1;
    if (f != -1.0F) {
      if (percentLayoutInfo1 == null)
        percentLayoutInfo1 = new PercentLayoutInfo(); 
      percentLayoutInfo1.aspectRatio = f;
      percentLayoutInfo2 = percentLayoutInfo1;
    } 
    typedArray.recycle();
    return percentLayoutInfo2;
  }
  
  private static boolean shouldHandleMeasuredHeightTooSmall(View paramView, PercentLayoutInfo paramPercentLayoutInfo) {
    return ((ViewCompat.getMeasuredHeightAndState(paramView) & 0xFF000000) == 16777216 && paramPercentLayoutInfo.heightPercent >= 0.0F && paramPercentLayoutInfo.mPreservedParams.height == -2);
  }
  
  private static boolean shouldHandleMeasuredWidthTooSmall(View paramView, PercentLayoutInfo paramPercentLayoutInfo) {
    return ((ViewCompat.getMeasuredWidthAndState(paramView) & 0xFF000000) == 16777216 && paramPercentLayoutInfo.widthPercent >= 0.0F && paramPercentLayoutInfo.mPreservedParams.width == -2);
  }
  
  public void adjustChildren(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.getSize(paramInt1) - this.mHost.getPaddingLeft() - this.mHost.getPaddingRight();
    paramInt2 = View.MeasureSpec.getSize(paramInt2) - this.mHost.getPaddingTop() - this.mHost.getPaddingBottom();
    paramInt1 = 0;
    int j = this.mHost.getChildCount();
    while (paramInt1 < j) {
      View view = this.mHost.getChildAt(paramInt1);
      ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
      if (layoutParams instanceof PercentLayoutParams) {
        PercentLayoutInfo percentLayoutInfo = ((PercentLayoutParams)layoutParams).getPercentLayoutInfo();
        if (percentLayoutInfo != null)
          if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            percentLayoutInfo.fillMarginLayoutParams(view, (ViewGroup.MarginLayoutParams)layoutParams, i, paramInt2);
          } else {
            percentLayoutInfo.fillLayoutParams(layoutParams, i, paramInt2);
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
      boolean bool1 = bool;
      if (layoutParams instanceof PercentLayoutParams) {
        PercentLayoutInfo percentLayoutInfo = ((PercentLayoutParams)layoutParams).getPercentLayoutInfo();
        bool1 = bool;
        if (percentLayoutInfo != null) {
          bool1 = bool;
          if (shouldHandleMeasuredWidthTooSmall(view, percentLayoutInfo)) {
            bool1 = true;
            layoutParams.width = -2;
          } 
          if (shouldHandleMeasuredHeightTooSmall(view, percentLayoutInfo)) {
            bool1 = true;
            layoutParams.height = -2;
          } 
        } 
      } 
      b++;
      bool = bool1;
    } 
    return bool;
  }
  
  public void restoreOriginalParams() {
    byte b = 0;
    int i = this.mHost.getChildCount();
    while (b < i) {
      ViewGroup.LayoutParams layoutParams = this.mHost.getChildAt(b).getLayoutParams();
      if (layoutParams instanceof PercentLayoutParams) {
        PercentLayoutInfo percentLayoutInfo = ((PercentLayoutParams)layoutParams).getPercentLayoutInfo();
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
    public float aspectRatio;
    
    public float bottomMarginPercent = -1.0F;
    
    public float endMarginPercent = -1.0F;
    
    public float heightPercent = -1.0F;
    
    public float leftMarginPercent = -1.0F;
    
    final PercentLayoutHelper.PercentMarginLayoutParams mPreservedParams = new PercentLayoutHelper.PercentMarginLayoutParams(0, 0);
    
    public float rightMarginPercent = -1.0F;
    
    public float startMarginPercent = -1.0F;
    
    public float topMarginPercent = -1.0F;
    
    public float widthPercent = -1.0F;
    
    public void fillLayoutParams(ViewGroup.LayoutParams param1LayoutParams, int param1Int1, int param1Int2) {
      boolean bool1;
      boolean bool2;
      this.mPreservedParams.width = param1LayoutParams.width;
      this.mPreservedParams.height = param1LayoutParams.height;
      if ((this.mPreservedParams.mIsWidthComputedFromAspectRatio || this.mPreservedParams.width == 0) && this.widthPercent < 0.0F) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if ((this.mPreservedParams.mIsHeightComputedFromAspectRatio || this.mPreservedParams.height == 0) && this.heightPercent < 0.0F) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      if (this.widthPercent >= 0.0F)
        param1LayoutParams.width = Math.round(param1Int1 * this.widthPercent); 
      if (this.heightPercent >= 0.0F)
        param1LayoutParams.height = Math.round(param1Int2 * this.heightPercent); 
      if (this.aspectRatio >= 0.0F) {
        if (bool1) {
          param1LayoutParams.width = Math.round(param1LayoutParams.height * this.aspectRatio);
          PercentLayoutHelper.PercentMarginLayoutParams.access$002(this.mPreservedParams, true);
        } 
        if (bool2) {
          param1LayoutParams.height = Math.round(param1LayoutParams.width / this.aspectRatio);
          PercentLayoutHelper.PercentMarginLayoutParams.access$102(this.mPreservedParams, true);
        } 
      } 
    }
    
    public void fillMarginLayoutParams(View param1View, ViewGroup.MarginLayoutParams param1MarginLayoutParams, int param1Int1, int param1Int2) {
      fillLayoutParams((ViewGroup.LayoutParams)param1MarginLayoutParams, param1Int1, param1Int2);
      this.mPreservedParams.leftMargin = param1MarginLayoutParams.leftMargin;
      this.mPreservedParams.topMargin = param1MarginLayoutParams.topMargin;
      this.mPreservedParams.rightMargin = param1MarginLayoutParams.rightMargin;
      this.mPreservedParams.bottomMargin = param1MarginLayoutParams.bottomMargin;
      MarginLayoutParamsCompat.setMarginStart(this.mPreservedParams, MarginLayoutParamsCompat.getMarginStart(param1MarginLayoutParams));
      MarginLayoutParamsCompat.setMarginEnd(this.mPreservedParams, MarginLayoutParamsCompat.getMarginEnd(param1MarginLayoutParams));
      if (this.leftMarginPercent >= 0.0F)
        param1MarginLayoutParams.leftMargin = Math.round(param1Int1 * this.leftMarginPercent); 
      if (this.topMarginPercent >= 0.0F)
        param1MarginLayoutParams.topMargin = Math.round(param1Int2 * this.topMarginPercent); 
      if (this.rightMarginPercent >= 0.0F)
        param1MarginLayoutParams.rightMargin = Math.round(param1Int1 * this.rightMarginPercent); 
      if (this.bottomMarginPercent >= 0.0F)
        param1MarginLayoutParams.bottomMargin = Math.round(param1Int2 * this.bottomMarginPercent); 
      param1Int2 = 0;
      if (this.startMarginPercent >= 0.0F) {
        MarginLayoutParamsCompat.setMarginStart(param1MarginLayoutParams, Math.round(param1Int1 * this.startMarginPercent));
        param1Int2 = 1;
      } 
      if (this.endMarginPercent >= 0.0F) {
        MarginLayoutParamsCompat.setMarginEnd(param1MarginLayoutParams, Math.round(param1Int1 * this.endMarginPercent));
        param1Int2 = 1;
      } 
      if (param1Int2 != 0 && param1View != null)
        MarginLayoutParamsCompat.resolveLayoutDirection(param1MarginLayoutParams, ViewCompat.getLayoutDirection(param1View)); 
    }
    
    @Deprecated
    public void fillMarginLayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams, int param1Int1, int param1Int2) {
      fillMarginLayoutParams(null, param1MarginLayoutParams, param1Int1, param1Int2);
    }
    
    public void restoreLayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      if (!this.mPreservedParams.mIsWidthComputedFromAspectRatio)
        param1LayoutParams.width = this.mPreservedParams.width; 
      if (!this.mPreservedParams.mIsHeightComputedFromAspectRatio)
        param1LayoutParams.height = this.mPreservedParams.height; 
      PercentLayoutHelper.PercentMarginLayoutParams.access$002(this.mPreservedParams, false);
      PercentLayoutHelper.PercentMarginLayoutParams.access$102(this.mPreservedParams, false);
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
      return String.format("PercentLayoutInformation width: %f height %f, margins (%f, %f,  %f, %f, %f, %f)", new Object[] { Float.valueOf(this.widthPercent), Float.valueOf(this.heightPercent), Float.valueOf(this.leftMarginPercent), Float.valueOf(this.topMarginPercent), Float.valueOf(this.rightMarginPercent), Float.valueOf(this.bottomMarginPercent), Float.valueOf(this.startMarginPercent), Float.valueOf(this.endMarginPercent) });
    }
  }
  
  public static interface PercentLayoutParams {
    PercentLayoutHelper.PercentLayoutInfo getPercentLayoutInfo();
  }
  
  static class PercentMarginLayoutParams extends ViewGroup.MarginLayoutParams {
    private boolean mIsHeightComputedFromAspectRatio;
    
    private boolean mIsWidthComputedFromAspectRatio;
    
    public PercentMarginLayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/percent/PercentLayoutHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */