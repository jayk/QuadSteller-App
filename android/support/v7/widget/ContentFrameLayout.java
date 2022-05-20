package android.support.v7.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

public class ContentFrameLayout extends FrameLayout {
  private OnAttachListener mAttachListener;
  
  private final Rect mDecorPadding = new Rect();
  
  private TypedValue mFixedHeightMajor;
  
  private TypedValue mFixedHeightMinor;
  
  private TypedValue mFixedWidthMajor;
  
  private TypedValue mFixedWidthMinor;
  
  private TypedValue mMinWidthMajor;
  
  private TypedValue mMinWidthMinor;
  
  public ContentFrameLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public ContentFrameLayout(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ContentFrameLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void dispatchFitSystemWindows(Rect paramRect) {
    fitSystemWindows(paramRect);
  }
  
  public TypedValue getFixedHeightMajor() {
    if (this.mFixedHeightMajor == null)
      this.mFixedHeightMajor = new TypedValue(); 
    return this.mFixedHeightMajor;
  }
  
  public TypedValue getFixedHeightMinor() {
    if (this.mFixedHeightMinor == null)
      this.mFixedHeightMinor = new TypedValue(); 
    return this.mFixedHeightMinor;
  }
  
  public TypedValue getFixedWidthMajor() {
    if (this.mFixedWidthMajor == null)
      this.mFixedWidthMajor = new TypedValue(); 
    return this.mFixedWidthMajor;
  }
  
  public TypedValue getFixedWidthMinor() {
    if (this.mFixedWidthMinor == null)
      this.mFixedWidthMinor = new TypedValue(); 
    return this.mFixedWidthMinor;
  }
  
  public TypedValue getMinWidthMajor() {
    if (this.mMinWidthMajor == null)
      this.mMinWidthMajor = new TypedValue(); 
    return this.mMinWidthMajor;
  }
  
  public TypedValue getMinWidthMinor() {
    if (this.mMinWidthMinor == null)
      this.mMinWidthMinor = new TypedValue(); 
    return this.mMinWidthMinor;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (this.mAttachListener != null)
      this.mAttachListener.onAttachedFromWindow(); 
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (this.mAttachListener != null)
      this.mAttachListener.onDetachedFromWindow(); 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i;
    DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
    if (displayMetrics.widthPixels < displayMetrics.heightPixels) {
      i = 1;
    } else {
      i = 0;
    } 
    int j = View.MeasureSpec.getMode(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    boolean bool1 = false;
    boolean bool2 = bool1;
    int m = paramInt1;
    if (j == Integer.MIN_VALUE) {
      TypedValue typedValue;
      if (i) {
        typedValue = this.mFixedWidthMinor;
      } else {
        typedValue = this.mFixedWidthMajor;
      } 
      bool2 = bool1;
      m = paramInt1;
      if (typedValue != null) {
        bool2 = bool1;
        m = paramInt1;
        if (typedValue.type != 0) {
          int i1 = 0;
          if (typedValue.type == 5) {
            i1 = (int)typedValue.getDimension(displayMetrics);
          } else if (typedValue.type == 6) {
            i1 = (int)typedValue.getFraction(displayMetrics.widthPixels, displayMetrics.widthPixels);
          } 
          bool2 = bool1;
          m = paramInt1;
          if (i1 > 0) {
            m = View.MeasureSpec.makeMeasureSpec(Math.min(i1 - this.mDecorPadding.left + this.mDecorPadding.right, View.MeasureSpec.getSize(paramInt1)), 1073741824);
            bool2 = true;
          } 
        } 
      } 
    } 
    int n = paramInt2;
    if (k == Integer.MIN_VALUE) {
      TypedValue typedValue;
      if (i) {
        typedValue = this.mFixedHeightMajor;
      } else {
        typedValue = this.mFixedHeightMinor;
      } 
      n = paramInt2;
      if (typedValue != null) {
        n = paramInt2;
        if (typedValue.type != 0) {
          paramInt1 = 0;
          if (typedValue.type == 5) {
            paramInt1 = (int)typedValue.getDimension(displayMetrics);
          } else if (typedValue.type == 6) {
            paramInt1 = (int)typedValue.getFraction(displayMetrics.heightPixels, displayMetrics.heightPixels);
          } 
          n = paramInt2;
          if (paramInt1 > 0)
            n = View.MeasureSpec.makeMeasureSpec(Math.min(paramInt1 - this.mDecorPadding.top + this.mDecorPadding.bottom, View.MeasureSpec.getSize(paramInt2)), 1073741824); 
        } 
      } 
    } 
    super.onMeasure(m, n);
    k = getMeasuredWidth();
    bool1 = false;
    m = View.MeasureSpec.makeMeasureSpec(k, 1073741824);
    paramInt2 = bool1;
    paramInt1 = m;
    if (!bool2) {
      paramInt2 = bool1;
      paramInt1 = m;
      if (j == Integer.MIN_VALUE) {
        TypedValue typedValue;
        if (i) {
          typedValue = this.mMinWidthMinor;
        } else {
          typedValue = this.mMinWidthMajor;
        } 
        paramInt2 = bool1;
        paramInt1 = m;
        if (typedValue != null) {
          paramInt2 = bool1;
          paramInt1 = m;
          if (typedValue.type != 0) {
            paramInt1 = 0;
            if (typedValue.type == 5) {
              paramInt1 = (int)typedValue.getDimension(displayMetrics);
            } else if (typedValue.type == 6) {
              paramInt1 = (int)typedValue.getFraction(displayMetrics.widthPixels, displayMetrics.widthPixels);
            } 
            i = paramInt1;
            if (paramInt1 > 0)
              i = paramInt1 - this.mDecorPadding.left + this.mDecorPadding.right; 
            paramInt2 = bool1;
            paramInt1 = m;
            if (k < i) {
              paramInt1 = View.MeasureSpec.makeMeasureSpec(i, 1073741824);
              paramInt2 = 1;
            } 
          } 
        } 
      } 
    } 
    if (paramInt2 != 0)
      super.onMeasure(paramInt1, n); 
  }
  
  public void setAttachListener(OnAttachListener paramOnAttachListener) {
    this.mAttachListener = paramOnAttachListener;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setDecorPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mDecorPadding.set(paramInt1, paramInt2, paramInt3, paramInt4);
    if (ViewCompat.isLaidOut((View)this))
      requestLayout(); 
  }
  
  public static interface OnAttachListener {
    void onAttachedFromWindow();
    
    void onDetachedFromWindow();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/ContentFrameLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */