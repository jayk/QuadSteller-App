package android.support.v7.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

@TargetApi(9)
@RequiresApi(9)
class CardViewGingerbread implements CardViewImpl {
  final RectF sCornerRect = new RectF();
  
  private RoundRectDrawableWithShadow createBackground(Context paramContext, ColorStateList paramColorStateList, float paramFloat1, float paramFloat2, float paramFloat3) {
    return new RoundRectDrawableWithShadow(paramContext.getResources(), paramColorStateList, paramFloat1, paramFloat2, paramFloat3);
  }
  
  private RoundRectDrawableWithShadow getShadowBackground(CardViewDelegate paramCardViewDelegate) {
    return (RoundRectDrawableWithShadow)paramCardViewDelegate.getCardBackground();
  }
  
  public ColorStateList getBackgroundColor(CardViewDelegate paramCardViewDelegate) {
    return getShadowBackground(paramCardViewDelegate).getColor();
  }
  
  public float getElevation(CardViewDelegate paramCardViewDelegate) {
    return getShadowBackground(paramCardViewDelegate).getShadowSize();
  }
  
  public float getMaxElevation(CardViewDelegate paramCardViewDelegate) {
    return getShadowBackground(paramCardViewDelegate).getMaxShadowSize();
  }
  
  public float getMinHeight(CardViewDelegate paramCardViewDelegate) {
    return getShadowBackground(paramCardViewDelegate).getMinHeight();
  }
  
  public float getMinWidth(CardViewDelegate paramCardViewDelegate) {
    return getShadowBackground(paramCardViewDelegate).getMinWidth();
  }
  
  public float getRadius(CardViewDelegate paramCardViewDelegate) {
    return getShadowBackground(paramCardViewDelegate).getCornerRadius();
  }
  
  public void initStatic() {
    RoundRectDrawableWithShadow.sRoundRectHelper = new RoundRectDrawableWithShadow.RoundRectHelper() {
        public void drawRoundRect(Canvas param1Canvas, RectF param1RectF, float param1Float, Paint param1Paint) {
          float f1 = param1Float * 2.0F;
          float f2 = param1RectF.width() - f1 - 1.0F;
          float f3 = param1RectF.height();
          if (param1Float >= 1.0F) {
            float f = param1Float + 0.5F;
            CardViewGingerbread.this.sCornerRect.set(-f, -f, f, f);
            int i = param1Canvas.save();
            param1Canvas.translate(param1RectF.left + f, param1RectF.top + f);
            param1Canvas.drawArc(CardViewGingerbread.this.sCornerRect, 180.0F, 90.0F, true, param1Paint);
            param1Canvas.translate(f2, 0.0F);
            param1Canvas.rotate(90.0F);
            param1Canvas.drawArc(CardViewGingerbread.this.sCornerRect, 180.0F, 90.0F, true, param1Paint);
            param1Canvas.translate(f3 - f1 - 1.0F, 0.0F);
            param1Canvas.rotate(90.0F);
            param1Canvas.drawArc(CardViewGingerbread.this.sCornerRect, 180.0F, 90.0F, true, param1Paint);
            param1Canvas.translate(f2, 0.0F);
            param1Canvas.rotate(90.0F);
            param1Canvas.drawArc(CardViewGingerbread.this.sCornerRect, 180.0F, 90.0F, true, param1Paint);
            param1Canvas.restoreToCount(i);
            param1Canvas.drawRect(param1RectF.left + f - 1.0F, param1RectF.top, 1.0F + param1RectF.right - f, param1RectF.top + f, param1Paint);
            param1Canvas.drawRect(param1RectF.left + f - 1.0F, param1RectF.bottom - f, 1.0F + param1RectF.right - f, param1RectF.bottom, param1Paint);
          } 
          param1Canvas.drawRect(param1RectF.left, param1RectF.top + param1Float, param1RectF.right, param1RectF.bottom - param1Float, param1Paint);
        }
      };
  }
  
  public void initialize(CardViewDelegate paramCardViewDelegate, Context paramContext, ColorStateList paramColorStateList, float paramFloat1, float paramFloat2, float paramFloat3) {
    RoundRectDrawableWithShadow roundRectDrawableWithShadow = createBackground(paramContext, paramColorStateList, paramFloat1, paramFloat2, paramFloat3);
    roundRectDrawableWithShadow.setAddPaddingForCorners(paramCardViewDelegate.getPreventCornerOverlap());
    paramCardViewDelegate.setCardBackground(roundRectDrawableWithShadow);
    updatePadding(paramCardViewDelegate);
  }
  
  public void onCompatPaddingChanged(CardViewDelegate paramCardViewDelegate) {}
  
  public void onPreventCornerOverlapChanged(CardViewDelegate paramCardViewDelegate) {
    getShadowBackground(paramCardViewDelegate).setAddPaddingForCorners(paramCardViewDelegate.getPreventCornerOverlap());
    updatePadding(paramCardViewDelegate);
  }
  
  public void setBackgroundColor(CardViewDelegate paramCardViewDelegate, @Nullable ColorStateList paramColorStateList) {
    getShadowBackground(paramCardViewDelegate).setColor(paramColorStateList);
  }
  
  public void setElevation(CardViewDelegate paramCardViewDelegate, float paramFloat) {
    getShadowBackground(paramCardViewDelegate).setShadowSize(paramFloat);
  }
  
  public void setMaxElevation(CardViewDelegate paramCardViewDelegate, float paramFloat) {
    getShadowBackground(paramCardViewDelegate).setMaxShadowSize(paramFloat);
    updatePadding(paramCardViewDelegate);
  }
  
  public void setRadius(CardViewDelegate paramCardViewDelegate, float paramFloat) {
    getShadowBackground(paramCardViewDelegate).setCornerRadius(paramFloat);
    updatePadding(paramCardViewDelegate);
  }
  
  public void updatePadding(CardViewDelegate paramCardViewDelegate) {
    Rect rect = new Rect();
    getShadowBackground(paramCardViewDelegate).getMaxShadowAndCornerPadding(rect);
    paramCardViewDelegate.setMinWidthHeightInternal((int)Math.ceil(getMinWidth(paramCardViewDelegate)), (int)Math.ceil(getMinHeight(paramCardViewDelegate)));
    paramCardViewDelegate.setShadowPadding(rect.left, rect.top, rect.right, rect.bottom);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/CardViewGingerbread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */