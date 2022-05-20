package android.support.design.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.R;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

class FloatingActionButtonGingerbread extends FloatingActionButtonImpl {
  ShadowDrawableWrapper mShadowDrawable;
  
  private final StateListAnimator mStateListAnimator = new StateListAnimator();
  
  FloatingActionButtonGingerbread(VisibilityAwareImageButton paramVisibilityAwareImageButton, ShadowViewDelegate paramShadowViewDelegate, ValueAnimatorCompat.Creator paramCreator) {
    super(paramVisibilityAwareImageButton, paramShadowViewDelegate, paramCreator);
    this.mStateListAnimator.addState(PRESSED_ENABLED_STATE_SET, createAnimator(new ElevateToTranslationZAnimation()));
    this.mStateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, createAnimator(new ElevateToTranslationZAnimation()));
    this.mStateListAnimator.addState(ENABLED_STATE_SET, createAnimator(new ResetElevationAnimation()));
    this.mStateListAnimator.addState(EMPTY_STATE_SET, createAnimator(new DisabledElevationAnimation()));
  }
  
  private ValueAnimatorCompat createAnimator(@NonNull ShadowAnimatorImpl paramShadowAnimatorImpl) {
    ValueAnimatorCompat valueAnimatorCompat = this.mAnimatorCreator.createAnimator();
    valueAnimatorCompat.setInterpolator(ANIM_INTERPOLATOR);
    valueAnimatorCompat.setDuration(100L);
    valueAnimatorCompat.addListener(paramShadowAnimatorImpl);
    valueAnimatorCompat.addUpdateListener(paramShadowAnimatorImpl);
    valueAnimatorCompat.setFloatValues(0.0F, 1.0F);
    return valueAnimatorCompat;
  }
  
  private static ColorStateList createColorStateList(int paramInt) {
    int[][] arrayOfInt = new int[3][];
    int[] arrayOfInt1 = new int[3];
    arrayOfInt[0] = FOCUSED_ENABLED_STATE_SET;
    arrayOfInt1[0] = paramInt;
    int i = 0 + 1;
    arrayOfInt[i] = PRESSED_ENABLED_STATE_SET;
    arrayOfInt1[i] = paramInt;
    paramInt = i + 1;
    arrayOfInt[paramInt] = new int[0];
    arrayOfInt1[paramInt] = 0;
    return new ColorStateList(arrayOfInt, arrayOfInt1);
  }
  
  float getElevation() {
    return this.mElevation;
  }
  
  void getPadding(Rect paramRect) {
    this.mShadowDrawable.getPadding(paramRect);
  }
  
  void hide(@Nullable final FloatingActionButtonImpl.InternalVisibilityChangedListener listener, final boolean fromUser) {
    if (!isOrWillBeHidden()) {
      this.mAnimState = 1;
      Animation animation = AnimationUtils.loadAnimation(this.mView.getContext(), R.anim.design_fab_out);
      animation.setInterpolator(AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR);
      animation.setDuration(200L);
      animation.setAnimationListener(new AnimationUtils.AnimationListenerAdapter() {
            public void onAnimationEnd(Animation param1Animation) {
              byte b;
              FloatingActionButtonGingerbread.this.mAnimState = 0;
              VisibilityAwareImageButton visibilityAwareImageButton = FloatingActionButtonGingerbread.this.mView;
              if (fromUser) {
                b = 8;
              } else {
                b = 4;
              } 
              visibilityAwareImageButton.internalSetVisibility(b, fromUser);
              if (listener != null)
                listener.onHidden(); 
            }
          });
      this.mView.startAnimation(animation);
    } 
  }
  
  void jumpDrawableToCurrentState() {
    this.mStateListAnimator.jumpToCurrentState();
  }
  
  void onCompatShadowChanged() {}
  
  void onDrawableStateChanged(int[] paramArrayOfint) {
    this.mStateListAnimator.setState(paramArrayOfint);
  }
  
  void onElevationsChanged(float paramFloat1, float paramFloat2) {
    if (this.mShadowDrawable != null) {
      this.mShadowDrawable.setShadowSize(paramFloat1, this.mPressedTranslationZ + paramFloat1);
      updatePadding();
    } 
  }
  
  void setBackgroundDrawable(ColorStateList paramColorStateList, PorterDuff.Mode paramMode, int paramInt1, int paramInt2) {
    Drawable[] arrayOfDrawable;
    this.mShapeDrawable = DrawableCompat.wrap((Drawable)createShapeDrawable());
    DrawableCompat.setTintList(this.mShapeDrawable, paramColorStateList);
    if (paramMode != null)
      DrawableCompat.setTintMode(this.mShapeDrawable, paramMode); 
    this.mRippleDrawable = DrawableCompat.wrap((Drawable)createShapeDrawable());
    DrawableCompat.setTintList(this.mRippleDrawable, createColorStateList(paramInt1));
    if (paramInt2 > 0) {
      this.mBorderDrawable = createBorderDrawable(paramInt2, paramColorStateList);
      arrayOfDrawable = new Drawable[3];
      arrayOfDrawable[0] = this.mBorderDrawable;
      arrayOfDrawable[1] = this.mShapeDrawable;
      arrayOfDrawable[2] = this.mRippleDrawable;
    } else {
      this.mBorderDrawable = null;
      arrayOfDrawable = new Drawable[2];
      arrayOfDrawable[0] = this.mShapeDrawable;
      arrayOfDrawable[1] = this.mRippleDrawable;
    } 
    this.mContentBackground = (Drawable)new LayerDrawable(arrayOfDrawable);
    this.mShadowDrawable = new ShadowDrawableWrapper(this.mView.getContext(), this.mContentBackground, this.mShadowViewDelegate.getRadius(), this.mElevation, this.mElevation + this.mPressedTranslationZ);
    this.mShadowDrawable.setAddPaddingForCorners(false);
    this.mShadowViewDelegate.setBackgroundDrawable((Drawable)this.mShadowDrawable);
  }
  
  void setBackgroundTintList(ColorStateList paramColorStateList) {
    if (this.mShapeDrawable != null)
      DrawableCompat.setTintList(this.mShapeDrawable, paramColorStateList); 
    if (this.mBorderDrawable != null)
      this.mBorderDrawable.setBorderTint(paramColorStateList); 
  }
  
  void setBackgroundTintMode(PorterDuff.Mode paramMode) {
    if (this.mShapeDrawable != null)
      DrawableCompat.setTintMode(this.mShapeDrawable, paramMode); 
  }
  
  void setRippleColor(int paramInt) {
    if (this.mRippleDrawable != null)
      DrawableCompat.setTintList(this.mRippleDrawable, createColorStateList(paramInt)); 
  }
  
  void show(@Nullable final FloatingActionButtonImpl.InternalVisibilityChangedListener listener, boolean paramBoolean) {
    if (!isOrWillBeShown()) {
      this.mAnimState = 2;
      this.mView.internalSetVisibility(0, paramBoolean);
      Animation animation = AnimationUtils.loadAnimation(this.mView.getContext(), R.anim.design_fab_in);
      animation.setDuration(200L);
      animation.setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
      animation.setAnimationListener(new AnimationUtils.AnimationListenerAdapter() {
            public void onAnimationEnd(Animation param1Animation) {
              FloatingActionButtonGingerbread.this.mAnimState = 0;
              if (listener != null)
                listener.onShown(); 
            }
          });
      this.mView.startAnimation(animation);
    } 
  }
  
  private class DisabledElevationAnimation extends ShadowAnimatorImpl {
    protected float getTargetShadowSize() {
      return 0.0F;
    }
  }
  
  private class ElevateToTranslationZAnimation extends ShadowAnimatorImpl {
    protected float getTargetShadowSize() {
      return FloatingActionButtonGingerbread.this.mElevation + FloatingActionButtonGingerbread.this.mPressedTranslationZ;
    }
  }
  
  private class ResetElevationAnimation extends ShadowAnimatorImpl {
    protected float getTargetShadowSize() {
      return FloatingActionButtonGingerbread.this.mElevation;
    }
  }
  
  private abstract class ShadowAnimatorImpl extends ValueAnimatorCompat.AnimatorListenerAdapter implements ValueAnimatorCompat.AnimatorUpdateListener {
    private float mShadowSizeEnd;
    
    private float mShadowSizeStart;
    
    private boolean mValidValues;
    
    private ShadowAnimatorImpl() {}
    
    protected abstract float getTargetShadowSize();
    
    public void onAnimationEnd(ValueAnimatorCompat param1ValueAnimatorCompat) {
      FloatingActionButtonGingerbread.this.mShadowDrawable.setShadowSize(this.mShadowSizeEnd);
      this.mValidValues = false;
    }
    
    public void onAnimationUpdate(ValueAnimatorCompat param1ValueAnimatorCompat) {
      if (!this.mValidValues) {
        this.mShadowSizeStart = FloatingActionButtonGingerbread.this.mShadowDrawable.getShadowSize();
        this.mShadowSizeEnd = getTargetShadowSize();
        this.mValidValues = true;
      } 
      FloatingActionButtonGingerbread.this.mShadowDrawable.setShadowSize(this.mShadowSizeStart + (this.mShadowSizeEnd - this.mShadowSizeStart) * param1ValueAnimatorCompat.getAnimatedFraction());
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/FloatingActionButtonGingerbread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */