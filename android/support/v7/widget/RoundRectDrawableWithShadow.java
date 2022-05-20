package android.support.v7.widget;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.cardview.R;

@TargetApi(9)
@RequiresApi(9)
class RoundRectDrawableWithShadow extends Drawable {
  static final double COS_45 = Math.cos(Math.toRadians(45.0D));
  
  static final float SHADOW_MULTIPLIER = 1.5F;
  
  static RoundRectHelper sRoundRectHelper;
  
  private boolean mAddPaddingForCorners = true;
  
  private ColorStateList mBackground;
  
  final RectF mCardBounds;
  
  float mCornerRadius;
  
  Paint mCornerShadowPaint;
  
  Path mCornerShadowPath;
  
  private boolean mDirty = true;
  
  Paint mEdgeShadowPaint;
  
  final int mInsetShadow;
  
  float mMaxShadowSize;
  
  Paint mPaint;
  
  private boolean mPrintedShadowClipWarning = false;
  
  float mRawMaxShadowSize;
  
  float mRawShadowSize;
  
  private final int mShadowEndColor;
  
  float mShadowSize;
  
  private final int mShadowStartColor;
  
  RoundRectDrawableWithShadow(Resources paramResources, ColorStateList paramColorStateList, float paramFloat1, float paramFloat2, float paramFloat3) {
    this.mShadowStartColor = paramResources.getColor(R.color.cardview_shadow_start_color);
    this.mShadowEndColor = paramResources.getColor(R.color.cardview_shadow_end_color);
    this.mInsetShadow = paramResources.getDimensionPixelSize(R.dimen.cardview_compat_inset_shadow);
    this.mPaint = new Paint(5);
    setBackground(paramColorStateList);
    this.mCornerShadowPaint = new Paint(5);
    this.mCornerShadowPaint.setStyle(Paint.Style.FILL);
    this.mCornerRadius = (int)(0.5F + paramFloat1);
    this.mCardBounds = new RectF();
    this.mEdgeShadowPaint = new Paint(this.mCornerShadowPaint);
    this.mEdgeShadowPaint.setAntiAlias(false);
    setShadowSize(paramFloat2, paramFloat3);
  }
  
  private void buildComponents(Rect paramRect) {
    float f = this.mRawMaxShadowSize * 1.5F;
    this.mCardBounds.set(paramRect.left + this.mRawMaxShadowSize, paramRect.top + f, paramRect.right - this.mRawMaxShadowSize, paramRect.bottom - f);
    buildShadowCorners();
  }
  
  private void buildShadowCorners() {
    RectF rectF1 = new RectF(-this.mCornerRadius, -this.mCornerRadius, this.mCornerRadius, this.mCornerRadius);
    RectF rectF2 = new RectF(rectF1);
    rectF2.inset(-this.mShadowSize, -this.mShadowSize);
    if (this.mCornerShadowPath == null) {
      this.mCornerShadowPath = new Path();
    } else {
      this.mCornerShadowPath.reset();
    } 
    this.mCornerShadowPath.setFillType(Path.FillType.EVEN_ODD);
    this.mCornerShadowPath.moveTo(-this.mCornerRadius, 0.0F);
    this.mCornerShadowPath.rLineTo(-this.mShadowSize, 0.0F);
    this.mCornerShadowPath.arcTo(rectF2, 180.0F, 90.0F, false);
    this.mCornerShadowPath.arcTo(rectF1, 270.0F, -90.0F, false);
    this.mCornerShadowPath.close();
    float f1 = this.mCornerRadius / (this.mCornerRadius + this.mShadowSize);
    Paint paint = this.mCornerShadowPaint;
    float f2 = this.mCornerRadius;
    float f3 = this.mShadowSize;
    int i = this.mShadowStartColor;
    int j = this.mShadowStartColor;
    int k = this.mShadowEndColor;
    Shader.TileMode tileMode = Shader.TileMode.CLAMP;
    paint.setShader((Shader)new RadialGradient(0.0F, 0.0F, f2 + f3, new int[] { i, j, k }, new float[] { 0.0F, f1, 1.0F }, tileMode));
    paint = this.mEdgeShadowPaint;
    f2 = -this.mCornerRadius;
    f1 = this.mShadowSize;
    f3 = -this.mCornerRadius;
    float f4 = this.mShadowSize;
    k = this.mShadowStartColor;
    j = this.mShadowStartColor;
    i = this.mShadowEndColor;
    tileMode = Shader.TileMode.CLAMP;
    paint.setShader((Shader)new LinearGradient(0.0F, f2 + f1, 0.0F, f3 - f4, new int[] { k, j, i }, new float[] { 0.0F, 0.5F, 1.0F }, tileMode));
    this.mEdgeShadowPaint.setAntiAlias(false);
  }
  
  static float calculateHorizontalPadding(float paramFloat1, float paramFloat2, boolean paramBoolean) {
    float f = paramFloat1;
    if (paramBoolean)
      f = (float)(paramFloat1 + (1.0D - COS_45) * paramFloat2); 
    return f;
  }
  
  static float calculateVerticalPadding(float paramFloat1, float paramFloat2, boolean paramBoolean) {
    return paramBoolean ? (float)((1.5F * paramFloat1) + (1.0D - COS_45) * paramFloat2) : (1.5F * paramFloat1);
  }
  
  private void drawShadow(Canvas paramCanvas) {
    boolean bool;
    float f1 = -this.mCornerRadius - this.mShadowSize;
    float f2 = this.mCornerRadius + this.mInsetShadow + this.mRawShadowSize / 2.0F;
    if (this.mCardBounds.width() - 2.0F * f2 > 0.0F) {
      i = 1;
    } else {
      i = 0;
    } 
    if (this.mCardBounds.height() - 2.0F * f2 > 0.0F) {
      bool = true;
    } else {
      bool = false;
    } 
    int j = paramCanvas.save();
    paramCanvas.translate(this.mCardBounds.left + f2, this.mCardBounds.top + f2);
    paramCanvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
    if (i)
      paramCanvas.drawRect(0.0F, f1, this.mCardBounds.width() - 2.0F * f2, -this.mCornerRadius, this.mEdgeShadowPaint); 
    paramCanvas.restoreToCount(j);
    j = paramCanvas.save();
    paramCanvas.translate(this.mCardBounds.right - f2, this.mCardBounds.bottom - f2);
    paramCanvas.rotate(180.0F);
    paramCanvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
    if (i) {
      float f3 = this.mCardBounds.width();
      float f4 = -this.mCornerRadius;
      paramCanvas.drawRect(0.0F, f1, f3 - 2.0F * f2, this.mShadowSize + f4, this.mEdgeShadowPaint);
    } 
    paramCanvas.restoreToCount(j);
    int i = paramCanvas.save();
    paramCanvas.translate(this.mCardBounds.left + f2, this.mCardBounds.bottom - f2);
    paramCanvas.rotate(270.0F);
    paramCanvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
    if (bool)
      paramCanvas.drawRect(0.0F, f1, this.mCardBounds.height() - 2.0F * f2, -this.mCornerRadius, this.mEdgeShadowPaint); 
    paramCanvas.restoreToCount(i);
    i = paramCanvas.save();
    paramCanvas.translate(this.mCardBounds.right - f2, this.mCardBounds.top + f2);
    paramCanvas.rotate(90.0F);
    paramCanvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
    if (bool)
      paramCanvas.drawRect(0.0F, f1, this.mCardBounds.height() - 2.0F * f2, -this.mCornerRadius, this.mEdgeShadowPaint); 
    paramCanvas.restoreToCount(i);
  }
  
  private void setBackground(ColorStateList paramColorStateList) {
    ColorStateList colorStateList = paramColorStateList;
    if (paramColorStateList == null)
      colorStateList = ColorStateList.valueOf(0); 
    this.mBackground = colorStateList;
    this.mPaint.setColor(this.mBackground.getColorForState(getState(), this.mBackground.getDefaultColor()));
  }
  
  private int toEven(float paramFloat) {
    int i = (int)(0.5F + paramFloat);
    int j = i;
    if (i % 2 == 1)
      j = i - 1; 
    return j;
  }
  
  public void draw(Canvas paramCanvas) {
    if (this.mDirty) {
      buildComponents(getBounds());
      this.mDirty = false;
    } 
    paramCanvas.translate(0.0F, this.mRawShadowSize / 2.0F);
    drawShadow(paramCanvas);
    paramCanvas.translate(0.0F, -this.mRawShadowSize / 2.0F);
    sRoundRectHelper.drawRoundRect(paramCanvas, this.mCardBounds, this.mCornerRadius, this.mPaint);
  }
  
  ColorStateList getColor() {
    return this.mBackground;
  }
  
  float getCornerRadius() {
    return this.mCornerRadius;
  }
  
  void getMaxShadowAndCornerPadding(Rect paramRect) {
    getPadding(paramRect);
  }
  
  float getMaxShadowSize() {
    return this.mRawMaxShadowSize;
  }
  
  float getMinHeight() {
    float f = Math.max(this.mRawMaxShadowSize, this.mCornerRadius + this.mInsetShadow + this.mRawMaxShadowSize * 1.5F / 2.0F);
    return (this.mRawMaxShadowSize * 1.5F + this.mInsetShadow) * 2.0F + 2.0F * f;
  }
  
  float getMinWidth() {
    float f = Math.max(this.mRawMaxShadowSize, this.mCornerRadius + this.mInsetShadow + this.mRawMaxShadowSize / 2.0F);
    return (this.mRawMaxShadowSize + this.mInsetShadow) * 2.0F + 2.0F * f;
  }
  
  public int getOpacity() {
    return -3;
  }
  
  public boolean getPadding(Rect paramRect) {
    int i = (int)Math.ceil(calculateVerticalPadding(this.mRawMaxShadowSize, this.mCornerRadius, this.mAddPaddingForCorners));
    int j = (int)Math.ceil(calculateHorizontalPadding(this.mRawMaxShadowSize, this.mCornerRadius, this.mAddPaddingForCorners));
    paramRect.set(j, i, j, i);
    return true;
  }
  
  float getShadowSize() {
    return this.mRawShadowSize;
  }
  
  public boolean isStateful() {
    return ((this.mBackground != null && this.mBackground.isStateful()) || super.isStateful());
  }
  
  protected void onBoundsChange(Rect paramRect) {
    super.onBoundsChange(paramRect);
    this.mDirty = true;
  }
  
  protected boolean onStateChange(int[] paramArrayOfint) {
    boolean bool = true;
    int i = this.mBackground.getColorForState(paramArrayOfint, this.mBackground.getDefaultColor());
    if (this.mPaint.getColor() == i)
      return false; 
    this.mPaint.setColor(i);
    this.mDirty = true;
    invalidateSelf();
    return bool;
  }
  
  public void setAddPaddingForCorners(boolean paramBoolean) {
    this.mAddPaddingForCorners = paramBoolean;
    invalidateSelf();
  }
  
  public void setAlpha(int paramInt) {
    this.mPaint.setAlpha(paramInt);
    this.mCornerShadowPaint.setAlpha(paramInt);
    this.mEdgeShadowPaint.setAlpha(paramInt);
  }
  
  void setColor(@Nullable ColorStateList paramColorStateList) {
    setBackground(paramColorStateList);
    invalidateSelf();
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    this.mPaint.setColorFilter(paramColorFilter);
  }
  
  void setCornerRadius(float paramFloat) {
    if (paramFloat < 0.0F)
      throw new IllegalArgumentException("Invalid radius " + paramFloat + ". Must be >= 0"); 
    paramFloat = (int)(0.5F + paramFloat);
    if (this.mCornerRadius != paramFloat) {
      this.mCornerRadius = paramFloat;
      this.mDirty = true;
      invalidateSelf();
    } 
  }
  
  void setMaxShadowSize(float paramFloat) {
    setShadowSize(this.mRawShadowSize, paramFloat);
  }
  
  void setShadowSize(float paramFloat) {
    setShadowSize(paramFloat, this.mRawMaxShadowSize);
  }
  
  void setShadowSize(float paramFloat1, float paramFloat2) {
    if (paramFloat1 < 0.0F)
      throw new IllegalArgumentException("Invalid shadow size " + paramFloat1 + ". Must be >= 0"); 
    if (paramFloat2 < 0.0F)
      throw new IllegalArgumentException("Invalid max shadow size " + paramFloat2 + ". Must be >= 0"); 
    float f1 = toEven(paramFloat1);
    float f2 = toEven(paramFloat2);
    paramFloat1 = f1;
    if (f1 > f2) {
      paramFloat2 = f2;
      paramFloat1 = paramFloat2;
      if (!this.mPrintedShadowClipWarning) {
        this.mPrintedShadowClipWarning = true;
        paramFloat1 = paramFloat2;
      } 
    } 
    if (this.mRawShadowSize != paramFloat1 || this.mRawMaxShadowSize != f2) {
      this.mRawShadowSize = paramFloat1;
      this.mRawMaxShadowSize = f2;
      this.mShadowSize = (int)(1.5F * paramFloat1 + this.mInsetShadow + 0.5F);
      this.mMaxShadowSize = this.mInsetShadow + f2;
      this.mDirty = true;
      invalidateSelf();
    } 
  }
  
  static interface RoundRectHelper {
    void drawRoundRect(Canvas param1Canvas, RectF param1RectF, float param1Float, Paint param1Paint);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/RoundRectDrawableWithShadow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */