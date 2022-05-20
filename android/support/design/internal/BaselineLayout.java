package android.support.design.internal;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.ViewUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class BaselineLayout extends ViewGroup {
  private int mBaseline = -1;
  
  public BaselineLayout(Context paramContext) {
    super(paramContext, null, 0);
  }
  
  public BaselineLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet, 0);
  }
  
  public BaselineLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public int getBaseline() {
    return this.mBaseline;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = getChildCount();
    int j = getPaddingLeft();
    int k = getPaddingRight();
    int m = getPaddingTop();
    for (paramInt2 = 0; paramInt2 < i; paramInt2++) {
      View view = getChildAt(paramInt2);
      if (view.getVisibility() != 8) {
        int n = view.getMeasuredWidth();
        int i1 = view.getMeasuredHeight();
        int i2 = j + (paramInt3 - paramInt1 - k - j - n) / 2;
        if (this.mBaseline != -1 && view.getBaseline() != -1) {
          paramInt4 = this.mBaseline + m - view.getBaseline();
        } else {
          paramInt4 = m;
        } 
        view.layout(i2, paramInt4, i2 + n, paramInt4 + i1);
      } 
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = getChildCount();
    int j = 0;
    int k = 0;
    int m = -1;
    int n = -1;
    int i1 = 0;
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        measureChild(view, paramInt1, paramInt2);
        int i3 = view.getBaseline();
        int i4 = m;
        int i5 = n;
        if (i3 != -1) {
          i4 = Math.max(m, i3);
          i5 = Math.max(n, view.getMeasuredHeight() - i3);
        } 
        j = Math.max(j, view.getMeasuredWidth());
        k = Math.max(k, view.getMeasuredHeight());
        i1 = ViewUtils.combineMeasuredStates(i1, ViewCompat.getMeasuredState(view));
        m = i4;
        n = i5;
      } 
    } 
    int i2 = k;
    if (m != -1) {
      i2 = Math.max(k, m + n);
      this.mBaseline = m;
    } 
    k = Math.max(i2, getSuggestedMinimumHeight());
    setMeasuredDimension(ViewCompat.resolveSizeAndState(Math.max(j, getSuggestedMinimumWidth()), paramInt1, i1), ViewCompat.resolveSizeAndState(k, paramInt2, i1 << 16));
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/internal/BaselineLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */