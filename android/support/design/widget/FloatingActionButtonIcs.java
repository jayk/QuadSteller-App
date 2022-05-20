package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.view.View;

@TargetApi(14)
@RequiresApi(14)
class FloatingActionButtonIcs extends FloatingActionButtonGingerbread {
  private float mRotation = this.mView.getRotation();
  
  FloatingActionButtonIcs(VisibilityAwareImageButton paramVisibilityAwareImageButton, ShadowViewDelegate paramShadowViewDelegate, ValueAnimatorCompat.Creator paramCreator) {
    super(paramVisibilityAwareImageButton, paramShadowViewDelegate, paramCreator);
  }
  
  private boolean shouldAnimateVisibilityChange() {
    return (ViewCompat.isLaidOut((View)this.mView) && !this.mView.isInEditMode());
  }
  
  private void updateFromViewRotation() {
    if (Build.VERSION.SDK_INT == 19)
      if (this.mRotation % 90.0F != 0.0F) {
        if (this.mView.getLayerType() != 1)
          this.mView.setLayerType(1, null); 
      } else if (this.mView.getLayerType() != 0) {
        this.mView.setLayerType(0, null);
      }  
    if (this.mShadowDrawable != null)
      this.mShadowDrawable.setRotation(-this.mRotation); 
    if (this.mBorderDrawable != null)
      this.mBorderDrawable.setRotation(-this.mRotation); 
  }
  
  void hide(@Nullable final FloatingActionButtonImpl.InternalVisibilityChangedListener listener, final boolean fromUser) {
    if (!isOrWillBeHidden()) {
      byte b;
      this.mView.animate().cancel();
      if (shouldAnimateVisibilityChange()) {
        this.mAnimState = 1;
        this.mView.animate().scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setDuration(200L).setInterpolator((TimeInterpolator)AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
              private boolean mCancelled;
              
              public void onAnimationCancel(Animator param1Animator) {
                this.mCancelled = true;
              }
              
              public void onAnimationEnd(Animator param1Animator) {
                FloatingActionButtonIcs.this.mAnimState = 0;
                if (!this.mCancelled) {
                  byte b;
                  VisibilityAwareImageButton visibilityAwareImageButton = FloatingActionButtonIcs.this.mView;
                  if (fromUser) {
                    b = 8;
                  } else {
                    b = 4;
                  } 
                  visibilityAwareImageButton.internalSetVisibility(b, fromUser);
                  if (listener != null)
                    listener.onHidden(); 
                } 
              }
              
              public void onAnimationStart(Animator param1Animator) {
                FloatingActionButtonIcs.this.mView.internalSetVisibility(0, fromUser);
                this.mCancelled = false;
              }
            });
        return;
      } 
      VisibilityAwareImageButton visibilityAwareImageButton = this.mView;
      if (fromUser) {
        b = 8;
      } else {
        b = 4;
      } 
      visibilityAwareImageButton.internalSetVisibility(b, fromUser);
      if (listener != null)
        listener.onHidden(); 
    } 
  }
  
  void onPreDraw() {
    float f = this.mView.getRotation();
    if (this.mRotation != f) {
      this.mRotation = f;
      updateFromViewRotation();
    } 
  }
  
  boolean requirePreDrawListener() {
    return true;
  }
  
  void show(@Nullable final FloatingActionButtonImpl.InternalVisibilityChangedListener listener, final boolean fromUser) {
    if (!isOrWillBeShown()) {
      this.mView.animate().cancel();
      if (shouldAnimateVisibilityChange()) {
        this.mAnimState = 2;
        if (this.mView.getVisibility() != 0) {
          this.mView.setAlpha(0.0F);
          this.mView.setScaleY(0.0F);
          this.mView.setScaleX(0.0F);
        } 
        this.mView.animate().scaleX(1.0F).scaleY(1.0F).alpha(1.0F).setDuration(200L).setInterpolator((TimeInterpolator)AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
              public void onAnimationEnd(Animator param1Animator) {
                FloatingActionButtonIcs.this.mAnimState = 0;
                if (listener != null)
                  listener.onShown(); 
              }
              
              public void onAnimationStart(Animator param1Animator) {
                FloatingActionButtonIcs.this.mView.internalSetVisibility(0, fromUser);
              }
            });
        return;
      } 
      this.mView.internalSetVisibility(0, fromUser);
      this.mView.setAlpha(1.0F);
      this.mView.setScaleY(1.0F);
      this.mView.setScaleX(1.0F);
      if (listener != null)
        listener.onShown(); 
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/FloatingActionButtonIcs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */