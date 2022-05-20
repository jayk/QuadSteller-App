package android.support.v7.widget;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

@TargetApi(21)
@RequiresApi(21)
class RoundRectDrawable extends Drawable {
  private ColorStateList mBackground;
  
  private final RectF mBoundsF;
  
  private final Rect mBoundsI;
  
  private boolean mInsetForPadding = false;
  
  private boolean mInsetForRadius = true;
  
  private float mPadding;
  
  private final Paint mPaint;
  
  private float mRadius;
  
  private ColorStateList mTint;
  
  private PorterDuffColorFilter mTintFilter;
  
  private PorterDuff.Mode mTintMode = PorterDuff.Mode.SRC_IN;
  
  public RoundRectDrawable(ColorStateList paramColorStateList, float paramFloat) {
    this.mRadius = paramFloat;
    this.mPaint = new Paint(5);
    setBackground(paramColorStateList);
    this.mBoundsF = new RectF();
    this.mBoundsI = new Rect();
  }
  
  private PorterDuffColorFilter createTintFilter(ColorStateList paramColorStateList, PorterDuff.Mode paramMode) {
    return (paramColorStateList == null || paramMode == null) ? null : new PorterDuffColorFilter(paramColorStateList.getColorForState(getState(), 0), paramMode);
  }
  
  private void setBackground(ColorStateList paramColorStateList) {
    ColorStateList colorStateList = paramColorStateList;
    if (paramColorStateList == null)
      colorStateList = ColorStateList.valueOf(0); 
    this.mBackground = colorStateList;
    this.mPaint.setColor(this.mBackground.getColorForState(getState(), this.mBackground.getDefaultColor()));
  }
  
  private void updateBounds(Rect paramRect) {
    Rect rect = paramRect;
    if (paramRect == null)
      rect = getBounds(); 
    this.mBoundsF.set(rect.left, rect.top, rect.right, rect.bottom);
    this.mBoundsI.set(rect);
    if (this.mInsetForPadding) {
      float f1 = RoundRectDrawableWithShadow.calculateVerticalPadding(this.mPadding, this.mRadius, this.mInsetForRadius);
      float f2 = RoundRectDrawableWithShadow.calculateHorizontalPadding(this.mPadding, this.mRadius, this.mInsetForRadius);
      this.mBoundsI.inset((int)Math.ceil(f2), (int)Math.ceil(f1));
      this.mBoundsF.set(this.mBoundsI);
    } 
  }
  
  public void draw(Canvas paramCanvas) {
    boolean bool;
    Paint paint = this.mPaint;
    if (this.mTintFilter != null && paint.getColorFilter() == null) {
      paint.setColorFilter((ColorFilter)this.mTintFilter);
      bool = true;
    } else {
      bool = false;
    } 
    paramCanvas.drawRoundRect(this.mBoundsF, this.mRadius, this.mRadius, paint);
    if (bool)
      paint.setColorFilter(null); 
  }
  
  public ColorStateList getColor() {
    return this.mBackground;
  }
  
  public int getOpacity() {
    return -3;
  }
  
  public void getOutline(Outline paramOutline) {
    paramOutline.setRoundRect(this.mBoundsI, this.mRadius);
  }
  
  float getPadding() {
    return this.mPadding;
  }
  
  public float getRadius() {
    return this.mRadius;
  }
  
  public boolean isStateful() {
    return ((this.mTint != null && this.mTint.isStateful()) || (this.mBackground != null && this.mBackground.isStateful()) || super.isStateful());
  }
  
  protected void onBoundsChange(Rect paramRect) {
    super.onBoundsChange(paramRect);
    updateBounds(paramRect);
  }
  
  protected boolean onStateChange(int[] paramArrayOfint) {
    boolean bool1;
    int i = this.mBackground.getColorForState(paramArrayOfint, this.mBackground.getDefaultColor());
    if (i != this.mPaint.getColor()) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (bool1)
      this.mPaint.setColor(i); 
    boolean bool2 = bool1;
    if (this.mTint != null) {
      bool2 = bool1;
      if (this.mTintMode != null) {
        this.mTintFilter = createTintFilter(this.mTint, this.mTintMode);
        bool2 = true;
      } 
    } 
    return bool2;
  }
  
  public void setAlpha(int paramInt) {
    this.mPaint.setAlpha(paramInt);
  }
  
  public void setColor(@Nullable ColorStateList paramColorStateList) {
    setBackground(paramColorStateList);
    invalidateSelf();
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    this.mPaint.setColorFilter(paramColorFilter);
  }
  
  void setPadding(float paramFloat, boolean paramBoolean1, boolean paramBoolean2) {
    if (paramFloat != this.mPadding || this.mInsetForPadding != paramBoolean1 || this.mInsetForRadius != paramBoolean2) {
      this.mPadding = paramFloat;
      this.mInsetForPadding = paramBoolean1;
      this.mInsetForRadius = paramBoolean2;
      updateBounds((Rect)null);
      invalidateSelf();
    } 
  }
  
  void setRadius(float paramFloat) {
    if (paramFloat != this.mRadius) {
      this.mRadius = paramFloat;
      updateBounds((Rect)null);
      invalidateSelf();
    } 
  }
  
  public void setTintList(ColorStateList paramColorStateList) {
    this.mTint = paramColorStateList;
    this.mTintFilter = createTintFilter(this.mTint, this.mTintMode);
    invalidateSelf();
  }
  
  public void setTintMode(PorterDuff.Mode paramMode) {
    this.mTintMode = paramMode;
    this.mTintFilter = createTintFilter(this.mTint, this.mTintMode);
    invalidateSelf();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/RoundRectDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */