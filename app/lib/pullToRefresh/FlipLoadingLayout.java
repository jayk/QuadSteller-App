package app.lib.pullToRefresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

@SuppressLint({"ViewConstructor"})
public class FlipLoadingLayout extends LoadingLayout {
  static final int FLIP_ANIMATION_DURATION = 150;
  
  private final Animation mResetRotateAnimation;
  
  private final Animation mRotateAnimation;
  
  public FlipLoadingLayout(Context paramContext, PullToRefreshBase.Mode paramMode, PullToRefreshBase.Orientation paramOrientation, TypedArray paramTypedArray) {
    super(paramContext, paramMode, paramOrientation, paramTypedArray);
    char c;
    if (paramMode == PullToRefreshBase.Mode.PULL_FROM_START) {
      c = 'ｌ';
    } else {
      c = '´';
    } 
    this.mRotateAnimation = (Animation)new RotateAnimation(0.0F, c, 1, 0.5F, 1, 0.5F);
    this.mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
    this.mRotateAnimation.setDuration(150L);
    this.mRotateAnimation.setFillAfter(true);
    this.mResetRotateAnimation = (Animation)new RotateAnimation(c, 0.0F, 1, 0.5F, 1, 0.5F);
    this.mResetRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
    this.mResetRotateAnimation.setDuration(150L);
    this.mResetRotateAnimation.setFillAfter(true);
  }
  
  private float getDrawableRotationAngle() {
    int i = null.$SwitchMap$app$lib$pullToRefresh$PullToRefreshBase$Mode[this.mMode.ordinal()];
    return 0.0F;
  }
  
  protected int getDefaultDrawableResId() {
    return 2130837816;
  }
  
  protected void onLoadingDrawableSet(Drawable paramDrawable) {
    if (paramDrawable != null) {
      int i = paramDrawable.getIntrinsicHeight();
      int j = paramDrawable.getIntrinsicWidth();
      ViewGroup.LayoutParams layoutParams = this.mHeaderImage.getLayoutParams();
      int k = Math.max(i, j);
      layoutParams.height = k;
      layoutParams.width = k;
      this.mHeaderImage.requestLayout();
      this.mHeaderImage.setScaleType(ImageView.ScaleType.MATRIX);
      Matrix matrix = new Matrix();
      matrix.postTranslate((layoutParams.width - j) / 2.0F, (layoutParams.height - i) / 2.0F);
      matrix.postRotate(getDrawableRotationAngle(), layoutParams.width / 2.0F, layoutParams.height / 2.0F);
      this.mHeaderImage.setImageMatrix(matrix);
    } 
  }
  
  protected void onPullImpl(float paramFloat) {}
  
  protected void pullToRefreshImpl() {
    if (this.mRotateAnimation == this.mHeaderImage.getAnimation())
      this.mHeaderImage.startAnimation(this.mResetRotateAnimation); 
  }
  
  protected void refreshingImpl() {
    this.mHeaderImage.clearAnimation();
    this.mHeaderImage.setVisibility(4);
    this.mHeaderProgress.setVisibility(0);
  }
  
  protected void releaseToRefreshImpl() {
    this.mHeaderImage.startAnimation(this.mRotateAnimation);
  }
  
  protected void resetImpl() {
    this.mHeaderImage.clearAnimation();
    this.mHeaderProgress.setVisibility(8);
    this.mHeaderImage.setVisibility(0);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/pullToRefresh/FlipLoadingLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */