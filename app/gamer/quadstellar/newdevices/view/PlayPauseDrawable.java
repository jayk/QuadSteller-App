package app.gamer.quadstellar.newdevices.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Property;
import android.view.animation.DecelerateInterpolator;

public class PlayPauseDrawable extends Drawable {
  private static final Property<PlayPauseDrawable, Float> PROGRESS;
  
  private static final String TAG = PlayPauseDrawable.class.getSimpleName();
  
  @Nullable
  private Animator animator;
  
  private boolean isPlay;
  
  private final Path leftPauseBar = new Path();
  
  private final Paint paint = new Paint();
  
  private float progress;
  
  private final Path rightPauseBar = new Path();
  
  static {
    PROGRESS = new Property<PlayPauseDrawable, Float>(Float.class, "progress") {
        public Float get(PlayPauseDrawable param1PlayPauseDrawable) {
          return Float.valueOf(param1PlayPauseDrawable.getProgress());
        }
        
        public void set(PlayPauseDrawable param1PlayPauseDrawable, Float param1Float) {
          param1PlayPauseDrawable.setProgress(param1Float.floatValue());
        }
      };
  }
  
  public PlayPauseDrawable() {
    this.paint.setAntiAlias(true);
    this.paint.setStyle(Paint.Style.FILL);
    this.paint.setColor(-1);
  }
  
  private float getProgress() {
    return this.progress;
  }
  
  private static float interpolate(float paramFloat1, float paramFloat2, float paramFloat3) {
    return (paramFloat2 - paramFloat1) * paramFloat3 + paramFloat1;
  }
  
  private void setProgress(float paramFloat) {
    this.progress = paramFloat;
    invalidateSelf();
  }
  
  private void toggle() {
    float f2;
    float f1 = 0.0F;
    if (this.animator != null)
      this.animator.cancel(); 
    Property<PlayPauseDrawable, Float> property = PROGRESS;
    if (this.isPlay) {
      f2 = 1.0F;
    } else {
      f2 = 0.0F;
    } 
    if (!this.isPlay)
      f1 = 1.0F; 
    this.animator = (Animator)ObjectAnimator.ofFloat(this, property, new float[] { f2, f1 });
    this.animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          public void onAnimationEnd(Animator param1Animator) {
            boolean bool;
            PlayPauseDrawable playPauseDrawable = PlayPauseDrawable.this;
            if (!PlayPauseDrawable.this.isPlay) {
              bool = true;
            } else {
              bool = false;
            } 
            PlayPauseDrawable.access$202(playPauseDrawable, bool);
          }
        });
    this.animator.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
    this.animator.setDuration(200L);
    this.animator.start();
  }
  
  public void draw(Canvas paramCanvas) {
    long l = System.currentTimeMillis();
    this.leftPauseBar.rewind();
    this.rightPauseBar.rewind();
    paramCanvas.translate((getBounds()).left, (getBounds()).top);
    float f1 = 0.5833333F * getBounds().height();
    float f2 = f1 / 3.0F;
    float f3 = interpolate(f1 / 3.6F, 0.0F, this.progress);
    float f4 = interpolate(f2, f1 / 1.75F, this.progress);
    float f5 = interpolate(0.0F, f4, this.progress);
    f2 = interpolate(2.0F * f4 + f3, f4 + f3, this.progress);
    this.leftPauseBar.moveTo(0.0F, 0.0F);
    this.leftPauseBar.lineTo(f5, -f1);
    this.leftPauseBar.lineTo(f4, -f1);
    this.leftPauseBar.lineTo(f4, 0.0F);
    this.leftPauseBar.close();
    this.rightPauseBar.moveTo(f4 + f3, 0.0F);
    this.rightPauseBar.lineTo(f4 + f3, -f1);
    this.rightPauseBar.lineTo(f2, -f1);
    this.rightPauseBar.lineTo(2.0F * f4 + f3, 0.0F);
    this.rightPauseBar.close();
    paramCanvas.save();
    paramCanvas.translate(interpolate(0.0F, f1 / 8.0F, this.progress), 0.0F);
    if (this.isPlay) {
      f2 = 1.0F - this.progress;
    } else {
      f2 = this.progress;
    } 
    if (this.isPlay) {
      f5 = 90.0F;
    } else {
      f5 = 0.0F;
    } 
    paramCanvas.rotate(interpolate(f5, 90.0F + f5, f2), getBounds().width() / 2.0F, getBounds().height() / 2.0F);
    paramCanvas.translate(getBounds().width() / 2.0F - (2.0F * f4 + f3) / 2.0F, getBounds().height() / 2.0F + f1 / 2.0F);
    paramCanvas.drawPath(this.leftPauseBar, this.paint);
    paramCanvas.drawPath(this.rightPauseBar, this.paint);
    paramCanvas.restore();
    l = System.currentTimeMillis() - l;
    if (l > 16L)
      Log.e(TAG, "Drawing took too long=" + l); 
  }
  
  public int getOpacity() {
    return -3;
  }
  
  public void jumpToCurrentState() {
    float f;
    Log.v(TAG, "jumpToCurrentState()");
    if (this.animator != null)
      this.animator.cancel(); 
    if (this.isPlay) {
      f = 1.0F;
    } else {
      f = 0.0F;
    } 
    setProgress(f);
  }
  
  public void setAlpha(int paramInt) {
    this.paint.setAlpha(paramInt);
    invalidateSelf();
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    this.paint.setColorFilter(paramColorFilter);
    invalidateSelf();
  }
  
  public void transformToPause(boolean paramBoolean) {
    if (this.isPlay) {
      if (paramBoolean) {
        toggle();
        return;
      } 
    } else {
      return;
    } 
    this.isPlay = false;
    setProgress(0.0F);
  }
  
  public void transformToPlay(boolean paramBoolean) {
    if (!this.isPlay) {
      if (paramBoolean) {
        toggle();
        return;
      } 
    } else {
      return;
    } 
    this.isPlay = true;
    setProgress(1.0F);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/view/PlayPauseDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */