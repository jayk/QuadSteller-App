package net.steamcrafted.materialiconlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class MaterialIconView extends ImageView {
  private static final int ACTIONBAR_HEIGHT_DP = 24;
  
  private MaterialDrawableBuilder mBuilder;
  
  private Drawable mDrawable;
  
  private MaterialDrawableBuilder.IconValue mIcon;
  
  private int mOverruledSize = -1;
  
  public MaterialIconView(Context paramContext) {
    super(paramContext);
    init();
  }
  
  public MaterialIconView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramContext, paramAttributeSet);
  }
  
  public MaterialIconView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext, paramAttributeSet);
  }
  
  private void init() {
    this.mBuilder = MaterialDrawableBuilder.with(getContext());
  }
  
  private void init(Context paramContext, AttributeSet paramAttributeSet) {
    init();
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.MaterialIconViewFormat);
    try {
      int i = typedArray.getInt(R.styleable.MaterialIconViewFormat_materialIcon, 0);
      if (i >= 0)
        setIcon(i); 
    } catch (Exception exception) {}
    try {
      setColor(typedArray.getColor(R.styleable.MaterialIconViewFormat_materialIconColor, -16777216));
    } catch (Exception exception) {}
    try {
      int i = typedArray.getDimensionPixelSize(R.styleable.MaterialIconViewFormat_materialIconSize, -1);
      if (i >= 0)
        setSizePx(i); 
    } catch (Exception exception) {}
    typedArray.recycle();
  }
  
  private void regenerateDrawable() {
    if (this.mIcon != null) {
      this.mDrawable = this.mBuilder.build();
      setImageDrawable(this.mDrawable);
    } 
  }
  
  private void setIcon(int paramInt) {
    setIcon(MaterialDrawableBuilder.IconValue.values()[paramInt]);
  }
  
  protected void onDraw(Canvas paramCanvas) {
    if (getWidth() != 0 && getHeight() != 0) {
      int i = getMeasuredWidth();
      int j = Math.min(getMeasuredHeight(), i);
      i = 0;
      if (this.mDrawable == null) {
        i = 1;
      } else if (Math.min(this.mDrawable.getIntrinsicHeight(), this.mDrawable.getIntrinsicHeight()) != j) {
        i = 1;
      } 
      if (i != 0) {
        if (this.mOverruledSize >= 0) {
          this.mBuilder.setSizePx(this.mOverruledSize);
        } else {
          this.mBuilder.setSizePx(j);
        } 
        regenerateDrawable();
      } 
      super.onDraw(paramCanvas);
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    if (this.mDrawable == null) {
      int i = MaterialIconUtils.convertDpToPx(getContext(), 24.0F);
      int j = View.MeasureSpec.getMode(paramInt1);
      int k = View.MeasureSpec.getMode(paramInt2);
      int m = getPaddingLeft() + getPaddingRight();
      int n = getPaddingTop() + getPaddingBottom();
      if (j != 0 || k != 0)
        if (j == 0) {
          i = View.MeasureSpec.getSize(paramInt2) - n;
        } else if (k == 0) {
          i = View.MeasureSpec.getSize(paramInt1) - m;
        } else {
          i = Math.min(View.MeasureSpec.getSize(paramInt2) - n, View.MeasureSpec.getSize(paramInt1) - m);
        }  
      Math.max(0, i);
      super.onMeasure(paramInt1, paramInt2);
      regenerateDrawable();
      return;
    } 
    super.onMeasure(paramInt1, paramInt2);
  }
  
  public void setColor(int paramInt) {
    this.mBuilder.setColor(paramInt);
    regenerateDrawable();
  }
  
  public void setColorResource(int paramInt) {
    this.mBuilder.setColorResource(paramInt);
    regenerateDrawable();
  }
  
  public void setIcon(MaterialDrawableBuilder.IconValue paramIconValue) {
    this.mIcon = paramIconValue;
    this.mBuilder.setIcon(paramIconValue);
    regenerateDrawable();
  }
  
  public void setSizeDp(int paramInt) {
    this.mBuilder.setSizeDp(paramInt);
    this.mOverruledSize = MaterialIconUtils.convertDpToPx(getContext(), paramInt);
    regenerateDrawable();
  }
  
  public void setSizePx(int paramInt) {
    this.mBuilder.setSizePx(paramInt);
    this.mOverruledSize = paramInt;
    regenerateDrawable();
  }
  
  public void setSizeResource(int paramInt) {
    this.mBuilder.setSizeResource(paramInt);
    this.mOverruledSize = getContext().getResources().getDimensionPixelSize(paramInt);
    regenerateDrawable();
  }
  
  public void setStyle(Paint.Style paramStyle) {
    this.mBuilder.setStyle(paramStyle);
    regenerateDrawable();
  }
  
  public void setToActionbarSize() {
    setSizeDp(24);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/net/steamcrafted/materialiconlib/MaterialIconView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */