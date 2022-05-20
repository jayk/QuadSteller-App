package app.gamer.quadstellar.ui.widget.ptr.header;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import app.gamer.quadstellar.ui.widget.ptr.PtrFrameLayout;
import app.gamer.quadstellar.ui.widget.ptr.PtrIndicator;
import app.gamer.quadstellar.ui.widget.ptr.PtrUIHandler;
import app.gamer.quadstellar.ui.widget.ptr.PtrUIHandlerHook;

public class MaterialHeader extends View implements PtrUIHandler {
  private MaterialProgressDrawable mDrawable;
  
  private PtrFrameLayout mPtrFrameLayout;
  
  private float mScale = 1.0F;
  
  private Animation mScaleAnimation = new Animation() {
      public void applyTransformation(float param1Float, Transformation param1Transformation) {
        MaterialHeader.access$002(MaterialHeader.this, 1.0F - param1Float);
        MaterialHeader.this.mDrawable.setAlpha((int)(255.0F * MaterialHeader.this.mScale));
        MaterialHeader.this.invalidate();
      }
    };
  
  public MaterialHeader(Context paramContext) {
    super(paramContext);
    initView();
  }
  
  public MaterialHeader(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    initView();
  }
  
  public MaterialHeader(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    initView();
  }
  
  private void initView() {
    this.mDrawable = new MaterialProgressDrawable(getContext(), this);
    this.mDrawable.setBackgroundColor(-1);
    this.mDrawable.setCallback((Drawable.Callback)this);
  }
  
  public void invalidateDrawable(Drawable paramDrawable) {
    if (paramDrawable == this.mDrawable) {
      invalidate();
      return;
    } 
    super.invalidateDrawable(paramDrawable);
  }
  
  protected void onDraw(Canvas paramCanvas) {
    int i = paramCanvas.save();
    Rect rect = this.mDrawable.getBounds();
    paramCanvas.translate((getPaddingLeft() + (getMeasuredWidth() - this.mDrawable.getIntrinsicWidth()) / 2), getPaddingTop());
    paramCanvas.scale(this.mScale, this.mScale, rect.exactCenterX(), rect.exactCenterY());
    this.mDrawable.draw(paramCanvas);
    paramCanvas.restoreToCount(i);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramInt1 = this.mDrawable.getIntrinsicHeight();
    this.mDrawable.setBounds(0, 0, paramInt1, paramInt1);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, View.MeasureSpec.makeMeasureSpec(this.mDrawable.getIntrinsicHeight() + getPaddingTop() + getPaddingBottom(), 1073741824));
  }
  
  public void onUIPositionChange(PtrFrameLayout paramPtrFrameLayout, boolean paramBoolean, byte paramByte, PtrIndicator paramPtrIndicator) {
    float f = Math.min(1.0F, paramPtrIndicator.getCurrentPercent());
    if (paramByte == 2) {
      this.mDrawable.setAlpha((int)(255.0F * f));
      this.mDrawable.showArrow(true);
      this.mDrawable.setStartEndTrim(0.0F, Math.min(0.8F, f * 0.8F));
      this.mDrawable.setArrowScale(Math.min(1.0F, f));
      this.mDrawable.setProgressRotation((-0.25F + 0.4F * f + 2.0F * f) * 0.5F);
      invalidate();
    } 
  }
  
  public void onUIRefreshBegin(PtrFrameLayout paramPtrFrameLayout) {
    this.mDrawable.setAlpha(255);
    this.mDrawable.start();
  }
  
  public void onUIRefreshComplete(PtrFrameLayout paramPtrFrameLayout) {
    this.mDrawable.stop();
  }
  
  public void onUIRefreshPrepare(PtrFrameLayout paramPtrFrameLayout) {}
  
  public void onUIReset(PtrFrameLayout paramPtrFrameLayout) {
    this.mScale = 1.0F;
    this.mDrawable.stop();
  }
  
  public void setColorSchemeColors(int[] paramArrayOfint) {
    this.mDrawable.setColorSchemeColors(paramArrayOfint);
    invalidate();
  }
  
  public void setPtrFrameLayout(PtrFrameLayout paramPtrFrameLayout) {
    final PtrUIHandlerHook mPtrUIHandlerHook = new PtrUIHandlerHook() {
        public void run() {
          MaterialHeader.this.startAnimation(MaterialHeader.this.mScaleAnimation);
        }
      };
    this.mScaleAnimation.setDuration(200L);
    this.mScaleAnimation.setAnimationListener(new Animation.AnimationListener() {
          public void onAnimationEnd(Animation param1Animation) {
            mPtrUIHandlerHook.resume();
          }
          
          public void onAnimationRepeat(Animation param1Animation) {}
          
          public void onAnimationStart(Animation param1Animation) {}
        });
    this.mPtrFrameLayout = paramPtrFrameLayout;
    this.mPtrFrameLayout.setRefreshCompleteHook(ptrUIHandlerHook);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/ptr/header/MaterialHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */