package app.lib.pullToRefresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

@SuppressLint({"ViewConstructor"})
public class IndicatorLayout extends FrameLayout implements Animation.AnimationListener {
  static final int DEFAULT_ROTATION_ANIMATION_DURATION = 150;
  
  private ImageView mArrowImageView;
  
  private Animation mInAnim;
  
  private Animation mOutAnim;
  
  private final Animation mResetRotateAnimation;
  
  private final Animation mRotateAnimation;
  
  public IndicatorLayout(Context paramContext, PullToRefreshBase.Mode paramMode) {
    super(paramContext);
    this.mArrowImageView = new ImageView(paramContext);
    Drawable drawable = getResources().getDrawable(2130838018);
    this.mArrowImageView.setImageDrawable(drawable);
    int i = getResources().getDimensionPixelSize(2131362856);
    this.mArrowImageView.setPadding(i, i, i, i);
    addView((View)this.mArrowImageView);
    switch (paramMode) {
      default:
        j = 2130968641;
        i = 2130968653;
        setBackgroundResource(2130838021);
        this.mInAnim = AnimationUtils.loadAnimation(paramContext, j);
        this.mInAnim.setAnimationListener(this);
        this.mOutAnim = AnimationUtils.loadAnimation(paramContext, i);
        this.mOutAnim.setAnimationListener(this);
        linearInterpolator = new LinearInterpolator();
        this.mRotateAnimation = (Animation)new RotateAnimation(0.0F, -180.0F, 1, 0.5F, 1, 0.5F);
        this.mRotateAnimation.setInterpolator((Interpolator)linearInterpolator);
        this.mRotateAnimation.setDuration(150L);
        this.mRotateAnimation.setFillAfter(true);
        this.mResetRotateAnimation = (Animation)new RotateAnimation(-180.0F, 0.0F, 1, 0.5F, 1, 0.5F);
        this.mResetRotateAnimation.setInterpolator((Interpolator)linearInterpolator);
        this.mResetRotateAnimation.setDuration(150L);
        this.mResetRotateAnimation.setFillAfter(true);
        return;
      case PULL_FROM_END:
        break;
    } 
    int j = 2130968640;
    i = 2130968652;
    setBackgroundResource(2130838019);
    this.mArrowImageView.setScaleType(ImageView.ScaleType.MATRIX);
    Matrix matrix = new Matrix();
    matrix.setRotate(180.0F, drawable.getIntrinsicWidth() / 2.0F, drawable.getIntrinsicHeight() / 2.0F);
    this.mArrowImageView.setImageMatrix(matrix);
    this.mInAnim = AnimationUtils.loadAnimation((Context)linearInterpolator, j);
    this.mInAnim.setAnimationListener(this);
    this.mOutAnim = AnimationUtils.loadAnimation((Context)linearInterpolator, i);
    this.mOutAnim.setAnimationListener(this);
    LinearInterpolator linearInterpolator = new LinearInterpolator();
    this.mRotateAnimation = (Animation)new RotateAnimation(0.0F, -180.0F, 1, 0.5F, 1, 0.5F);
    this.mRotateAnimation.setInterpolator((Interpolator)linearInterpolator);
    this.mRotateAnimation.setDuration(150L);
    this.mRotateAnimation.setFillAfter(true);
    this.mResetRotateAnimation = (Animation)new RotateAnimation(-180.0F, 0.0F, 1, 0.5F, 1, 0.5F);
    this.mResetRotateAnimation.setInterpolator((Interpolator)linearInterpolator);
    this.mResetRotateAnimation.setDuration(150L);
    this.mResetRotateAnimation.setFillAfter(true);
  }
  
  public void hide() {
    startAnimation(this.mOutAnim);
  }
  
  public final boolean isVisible() {
    boolean bool = true;
    Animation animation = getAnimation();
    if (animation != null) {
      if (this.mInAnim != animation)
        bool = false; 
      return bool;
    } 
    if (getVisibility() != 0)
      bool = false; 
    return bool;
  }
  
  public void onAnimationEnd(Animation paramAnimation) {
    if (paramAnimation == this.mOutAnim) {
      this.mArrowImageView.clearAnimation();
      setVisibility(8);
    } else if (paramAnimation == this.mInAnim) {
      setVisibility(0);
    } 
    clearAnimation();
  }
  
  public void onAnimationRepeat(Animation paramAnimation) {}
  
  public void onAnimationStart(Animation paramAnimation) {
    setVisibility(0);
  }
  
  public void pullToRefresh() {
    this.mArrowImageView.startAnimation(this.mResetRotateAnimation);
  }
  
  public void releaseToRefresh() {
    this.mArrowImageView.startAnimation(this.mRotateAnimation);
  }
  
  public void show() {
    this.mArrowImageView.clearAnimation();
    startAnimation(this.mInAnim);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/pullToRefresh/IndicatorLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */