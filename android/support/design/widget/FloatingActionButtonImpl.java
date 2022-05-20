package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.design.R;
import android.support.v4.content.ContextCompat;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;

abstract class FloatingActionButtonImpl {
  static final Interpolator ANIM_INTERPOLATOR = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
  
  static final int ANIM_STATE_HIDING = 1;
  
  static final int ANIM_STATE_NONE = 0;
  
  static final int ANIM_STATE_SHOWING = 2;
  
  static final int[] EMPTY_STATE_SET;
  
  static final int[] ENABLED_STATE_SET;
  
  static final int[] FOCUSED_ENABLED_STATE_SET;
  
  static final long PRESSED_ANIM_DELAY = 100L;
  
  static final long PRESSED_ANIM_DURATION = 100L;
  
  static final int[] PRESSED_ENABLED_STATE_SET = new int[] { 16842919, 16842910 };
  
  static final int SHOW_HIDE_ANIM_DURATION = 200;
  
  int mAnimState = 0;
  
  final ValueAnimatorCompat.Creator mAnimatorCreator;
  
  CircularBorderDrawable mBorderDrawable;
  
  Drawable mContentBackground;
  
  float mElevation;
  
  private ViewTreeObserver.OnPreDrawListener mPreDrawListener;
  
  float mPressedTranslationZ;
  
  Drawable mRippleDrawable;
  
  final ShadowViewDelegate mShadowViewDelegate;
  
  Drawable mShapeDrawable;
  
  private final Rect mTmpRect = new Rect();
  
  final VisibilityAwareImageButton mView;
  
  static {
    FOCUSED_ENABLED_STATE_SET = new int[] { 16842908, 16842910 };
    ENABLED_STATE_SET = new int[] { 16842910 };
    EMPTY_STATE_SET = new int[0];
  }
  
  FloatingActionButtonImpl(VisibilityAwareImageButton paramVisibilityAwareImageButton, ShadowViewDelegate paramShadowViewDelegate, ValueAnimatorCompat.Creator paramCreator) {
    this.mView = paramVisibilityAwareImageButton;
    this.mShadowViewDelegate = paramShadowViewDelegate;
    this.mAnimatorCreator = paramCreator;
  }
  
  private void ensurePreDrawListener() {
    if (this.mPreDrawListener == null)
      this.mPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
          public boolean onPreDraw() {
            FloatingActionButtonImpl.this.onPreDraw();
            return true;
          }
        }; 
  }
  
  CircularBorderDrawable createBorderDrawable(int paramInt, ColorStateList paramColorStateList) {
    Context context = this.mView.getContext();
    CircularBorderDrawable circularBorderDrawable = newCircularDrawable();
    circularBorderDrawable.setGradientColors(ContextCompat.getColor(context, R.color.design_fab_stroke_top_outer_color), ContextCompat.getColor(context, R.color.design_fab_stroke_top_inner_color), ContextCompat.getColor(context, R.color.design_fab_stroke_end_inner_color), ContextCompat.getColor(context, R.color.design_fab_stroke_end_outer_color));
    circularBorderDrawable.setBorderWidth(paramInt);
    circularBorderDrawable.setBorderTint(paramColorStateList);
    return circularBorderDrawable;
  }
  
  GradientDrawable createShapeDrawable() {
    GradientDrawable gradientDrawable = newGradientDrawableForShape();
    gradientDrawable.setShape(1);
    gradientDrawable.setColor(-1);
    return gradientDrawable;
  }
  
  final Drawable getContentBackground() {
    return this.mContentBackground;
  }
  
  abstract float getElevation();
  
  abstract void getPadding(Rect paramRect);
  
  abstract void hide(@Nullable InternalVisibilityChangedListener paramInternalVisibilityChangedListener, boolean paramBoolean);
  
  boolean isOrWillBeHidden() {
    boolean bool = true;
    if (this.mView.getVisibility() == 0) {
      if (this.mAnimState != 1)
        bool = false; 
      return bool;
    } 
    if (this.mAnimState == 2)
      bool = false; 
    return bool;
  }
  
  boolean isOrWillBeShown() {
    boolean bool = true;
    if (this.mView.getVisibility() != 0) {
      if (this.mAnimState != 2)
        bool = false; 
      return bool;
    } 
    if (this.mAnimState == 1)
      bool = false; 
    return bool;
  }
  
  abstract void jumpDrawableToCurrentState();
  
  CircularBorderDrawable newCircularDrawable() {
    return new CircularBorderDrawable();
  }
  
  GradientDrawable newGradientDrawableForShape() {
    return new GradientDrawable();
  }
  
  void onAttachedToWindow() {
    if (requirePreDrawListener()) {
      ensurePreDrawListener();
      this.mView.getViewTreeObserver().addOnPreDrawListener(this.mPreDrawListener);
    } 
  }
  
  abstract void onCompatShadowChanged();
  
  void onDetachedFromWindow() {
    if (this.mPreDrawListener != null) {
      this.mView.getViewTreeObserver().removeOnPreDrawListener(this.mPreDrawListener);
      this.mPreDrawListener = null;
    } 
  }
  
  abstract void onDrawableStateChanged(int[] paramArrayOfint);
  
  abstract void onElevationsChanged(float paramFloat1, float paramFloat2);
  
  void onPaddingUpdated(Rect paramRect) {}
  
  void onPreDraw() {}
  
  boolean requirePreDrawListener() {
    return false;
  }
  
  abstract void setBackgroundDrawable(ColorStateList paramColorStateList, PorterDuff.Mode paramMode, int paramInt1, int paramInt2);
  
  abstract void setBackgroundTintList(ColorStateList paramColorStateList);
  
  abstract void setBackgroundTintMode(PorterDuff.Mode paramMode);
  
  final void setElevation(float paramFloat) {
    if (this.mElevation != paramFloat) {
      this.mElevation = paramFloat;
      onElevationsChanged(paramFloat, this.mPressedTranslationZ);
    } 
  }
  
  final void setPressedTranslationZ(float paramFloat) {
    if (this.mPressedTranslationZ != paramFloat) {
      this.mPressedTranslationZ = paramFloat;
      onElevationsChanged(this.mElevation, paramFloat);
    } 
  }
  
  abstract void setRippleColor(int paramInt);
  
  abstract void show(@Nullable InternalVisibilityChangedListener paramInternalVisibilityChangedListener, boolean paramBoolean);
  
  final void updatePadding() {
    Rect rect = this.mTmpRect;
    getPadding(rect);
    onPaddingUpdated(rect);
    this.mShadowViewDelegate.setShadowPadding(rect.left, rect.top, rect.right, rect.bottom);
  }
  
  static interface InternalVisibilityChangedListener {
    void onHidden();
    
    void onShown();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/FloatingActionButtonImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */