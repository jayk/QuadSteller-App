package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.ConfigurationHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class ButtonBarLayout extends LinearLayout {
  private static final int ALLOW_STACKING_MIN_HEIGHT_DP = 320;
  
  private static final int PEEK_BUTTON_DP = 16;
  
  private boolean mAllowStacking;
  
  private int mLastWidthSize;
  
  public ButtonBarLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    boolean bool;
    this.mLastWidthSize = -1;
    if (ConfigurationHelper.getScreenHeightDp(getResources()) >= 320) {
      bool = true;
    } else {
      bool = false;
    } 
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ButtonBarLayout);
    this.mAllowStacking = typedArray.getBoolean(R.styleable.ButtonBarLayout_allowStacking, bool);
    typedArray.recycle();
  }
  
  private int getNextVisibleChildIndex(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getChildCount : ()I
    //   4: istore_2
    //   5: iload_1
    //   6: iload_2
    //   7: if_icmpge -> 29
    //   10: aload_0
    //   11: iload_1
    //   12: invokevirtual getChildAt : (I)Landroid/view/View;
    //   15: invokevirtual getVisibility : ()I
    //   18: ifne -> 23
    //   21: iload_1
    //   22: ireturn
    //   23: iinc #1, 1
    //   26: goto -> 5
    //   29: iconst_m1
    //   30: istore_1
    //   31: goto -> 21
  }
  
  private boolean isStacked() {
    boolean bool = true;
    if (getOrientation() != 1)
      bool = false; 
    return bool;
  }
  
  private void setStacked(boolean paramBoolean) {
    if (paramBoolean) {
      i = 1;
    } else {
      i = 0;
    } 
    setOrientation(i);
    if (paramBoolean) {
      i = 5;
    } else {
      i = 80;
    } 
    setGravity(i);
    View view = findViewById(R.id.spacer);
    if (view != null) {
      if (paramBoolean) {
        i = 8;
      } else {
        i = 4;
      } 
      view.setVisibility(i);
    } 
    int i;
    for (i = getChildCount() - 2; i >= 0; i--)
      bringChildToFront(getChildAt(i)); 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int k;
    int i = View.MeasureSpec.getSize(paramInt1);
    if (this.mAllowStacking) {
      if (i > this.mLastWidthSize && isStacked())
        setStacked(false); 
      this.mLastWidthSize = i;
    } 
    int j = 0;
    if (!isStacked() && View.MeasureSpec.getMode(paramInt1) == 1073741824) {
      k = View.MeasureSpec.makeMeasureSpec(i, -2147483648);
      j = 1;
    } else {
      k = paramInt1;
    } 
    super.onMeasure(k, paramInt2);
    int m = j;
    if (this.mAllowStacking) {
      m = j;
      if (!isStacked()) {
        if (Build.VERSION.SDK_INT >= 11) {
          if ((ViewCompat.getMeasuredWidthAndState((View)this) & 0xFF000000) == 16777216) {
            k = 1;
          } else {
            k = 0;
          } 
        } else {
          m = 0;
          k = 0;
          int n = getChildCount();
          while (k < n) {
            m += getChildAt(k).getMeasuredWidth();
            k++;
          } 
          if (getPaddingLeft() + m + getPaddingRight() > i) {
            k = 1;
          } else {
            k = 0;
          } 
        } 
        m = j;
        if (k != 0) {
          setStacked(true);
          m = 1;
        } 
      } 
    } 
    if (m != 0)
      super.onMeasure(paramInt1, paramInt2); 
    paramInt1 = 0;
    j = getNextVisibleChildIndex(0);
    if (j >= 0) {
      View view = getChildAt(j);
      LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)view.getLayoutParams();
      paramInt2 = 0 + getPaddingTop() + view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
      if (isStacked()) {
        j = getNextVisibleChildIndex(j + 1);
        paramInt1 = paramInt2;
        if (j >= 0)
          paramInt1 = (int)(paramInt2 + getChildAt(j).getPaddingTop() + 16.0F * (getResources().getDisplayMetrics()).density); 
      } else {
        paramInt1 = paramInt2 + getPaddingBottom();
      } 
    } 
    if (ViewCompat.getMinimumHeight((View)this) != paramInt1)
      setMinimumHeight(paramInt1); 
  }
  
  public void setAllowStacking(boolean paramBoolean) {
    if (this.mAllowStacking != paramBoolean) {
      this.mAllowStacking = paramBoolean;
      if (!this.mAllowStacking && getOrientation() == 1)
        setStacked(false); 
      requestLayout();
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/ButtonBarLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */