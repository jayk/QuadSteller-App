package android.support.design.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.RestrictTo;
import android.support.design.R;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class SnackbarContentLayout extends LinearLayout implements BaseTransientBottomBar.ContentViewCallback {
  private Button mActionView;
  
  private int mMaxInlineActionWidth;
  
  private int mMaxWidth;
  
  private TextView mMessageView;
  
  public SnackbarContentLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public SnackbarContentLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.SnackbarLayout);
    this.mMaxWidth = typedArray.getDimensionPixelSize(R.styleable.SnackbarLayout_android_maxWidth, -1);
    this.mMaxInlineActionWidth = typedArray.getDimensionPixelSize(R.styleable.SnackbarLayout_maxActionInlineWidth, -1);
    typedArray.recycle();
  }
  
  private static void updateTopBottomPadding(View paramView, int paramInt1, int paramInt2) {
    if (ViewCompat.isPaddingRelative(paramView)) {
      ViewCompat.setPaddingRelative(paramView, ViewCompat.getPaddingStart(paramView), paramInt1, ViewCompat.getPaddingEnd(paramView), paramInt2);
      return;
    } 
    paramView.setPadding(paramView.getPaddingLeft(), paramInt1, paramView.getPaddingRight(), paramInt2);
  }
  
  private boolean updateViewsWithinLayout(int paramInt1, int paramInt2, int paramInt3) {
    boolean bool = false;
    if (paramInt1 != getOrientation()) {
      setOrientation(paramInt1);
      bool = true;
    } 
    if (this.mMessageView.getPaddingTop() != paramInt2 || this.mMessageView.getPaddingBottom() != paramInt3) {
      updateTopBottomPadding((View)this.mMessageView, paramInt2, paramInt3);
      bool = true;
    } 
    return bool;
  }
  
  public void animateContentIn(int paramInt1, int paramInt2) {
    ViewCompat.setAlpha((View)this.mMessageView, 0.0F);
    ViewCompat.animate((View)this.mMessageView).alpha(1.0F).setDuration(paramInt2).setStartDelay(paramInt1).start();
    if (this.mActionView.getVisibility() == 0) {
      ViewCompat.setAlpha((View)this.mActionView, 0.0F);
      ViewCompat.animate((View)this.mActionView).alpha(1.0F).setDuration(paramInt2).setStartDelay(paramInt1).start();
    } 
  }
  
  public void animateContentOut(int paramInt1, int paramInt2) {
    ViewCompat.setAlpha((View)this.mMessageView, 1.0F);
    ViewCompat.animate((View)this.mMessageView).alpha(0.0F).setDuration(paramInt2).setStartDelay(paramInt1).start();
    if (this.mActionView.getVisibility() == 0) {
      ViewCompat.setAlpha((View)this.mActionView, 1.0F);
      ViewCompat.animate((View)this.mActionView).alpha(0.0F).setDuration(paramInt2).setStartDelay(paramInt1).start();
    } 
  }
  
  public Button getActionView() {
    return this.mActionView;
  }
  
  public TextView getMessageView() {
    return this.mMessageView;
  }
  
  protected void onFinishInflate() {
    super.onFinishInflate();
    this.mMessageView = (TextView)findViewById(R.id.snackbar_text);
    this.mActionView = (Button)findViewById(R.id.snackbar_action);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    int i = paramInt1;
    if (this.mMaxWidth > 0) {
      i = paramInt1;
      if (getMeasuredWidth() > this.mMaxWidth) {
        i = View.MeasureSpec.makeMeasureSpec(this.mMaxWidth, 1073741824);
        super.onMeasure(i, paramInt2);
      } 
    } 
    int j = getResources().getDimensionPixelSize(R.dimen.design_snackbar_padding_vertical_2lines);
    int k = getResources().getDimensionPixelSize(R.dimen.design_snackbar_padding_vertical);
    if (this.mMessageView.getLayout().getLineCount() > 1) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    boolean bool = false;
    if (paramInt1 != 0 && this.mMaxInlineActionWidth > 0 && this.mActionView.getMeasuredWidth() > this.mMaxInlineActionWidth) {
      paramInt1 = bool;
      if (updateViewsWithinLayout(1, j, j - k))
        paramInt1 = 1; 
    } else {
      if (paramInt1 == 0)
        j = k; 
      paramInt1 = bool;
      if (updateViewsWithinLayout(0, j, j))
        paramInt1 = 1; 
    } 
    if (paramInt1 != 0)
      super.onMeasure(i, paramInt2); 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/internal/SnackbarContentLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */