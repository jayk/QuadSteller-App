package android.support.design.widget;

import android.support.annotation.NonNull;
import android.view.animation.Interpolator;

class ValueAnimatorCompat {
  private final Impl mImpl;
  
  ValueAnimatorCompat(Impl paramImpl) {
    this.mImpl = paramImpl;
  }
  
  public void addListener(final AnimatorListener listener) {
    if (listener != null) {
      this.mImpl.addListener(new Impl.AnimatorListenerProxy() {
            public void onAnimationCancel() {
              listener.onAnimationCancel(ValueAnimatorCompat.this);
            }
            
            public void onAnimationEnd() {
              listener.onAnimationEnd(ValueAnimatorCompat.this);
            }
            
            public void onAnimationStart() {
              listener.onAnimationStart(ValueAnimatorCompat.this);
            }
          });
      return;
    } 
    this.mImpl.addListener(null);
  }
  
  public void addUpdateListener(final AnimatorUpdateListener updateListener) {
    if (updateListener != null) {
      this.mImpl.addUpdateListener(new Impl.AnimatorUpdateListenerProxy() {
            public void onAnimationUpdate() {
              updateListener.onAnimationUpdate(ValueAnimatorCompat.this);
            }
          });
      return;
    } 
    this.mImpl.addUpdateListener(null);
  }
  
  public void cancel() {
    this.mImpl.cancel();
  }
  
  public void end() {
    this.mImpl.end();
  }
  
  public float getAnimatedFloatValue() {
    return this.mImpl.getAnimatedFloatValue();
  }
  
  public float getAnimatedFraction() {
    return this.mImpl.getAnimatedFraction();
  }
  
  public int getAnimatedIntValue() {
    return this.mImpl.getAnimatedIntValue();
  }
  
  public long getDuration() {
    return this.mImpl.getDuration();
  }
  
  public boolean isRunning() {
    return this.mImpl.isRunning();
  }
  
  public void setDuration(long paramLong) {
    this.mImpl.setDuration(paramLong);
  }
  
  public void setFloatValues(float paramFloat1, float paramFloat2) {
    this.mImpl.setFloatValues(paramFloat1, paramFloat2);
  }
  
  public void setIntValues(int paramInt1, int paramInt2) {
    this.mImpl.setIntValues(paramInt1, paramInt2);
  }
  
  public void setInterpolator(Interpolator paramInterpolator) {
    this.mImpl.setInterpolator(paramInterpolator);
  }
  
  public void start() {
    this.mImpl.start();
  }
  
  static interface AnimatorListener {
    void onAnimationCancel(ValueAnimatorCompat param1ValueAnimatorCompat);
    
    void onAnimationEnd(ValueAnimatorCompat param1ValueAnimatorCompat);
    
    void onAnimationStart(ValueAnimatorCompat param1ValueAnimatorCompat);
  }
  
  static class AnimatorListenerAdapter implements AnimatorListener {
    public void onAnimationCancel(ValueAnimatorCompat param1ValueAnimatorCompat) {}
    
    public void onAnimationEnd(ValueAnimatorCompat param1ValueAnimatorCompat) {}
    
    public void onAnimationStart(ValueAnimatorCompat param1ValueAnimatorCompat) {}
  }
  
  static interface AnimatorUpdateListener {
    void onAnimationUpdate(ValueAnimatorCompat param1ValueAnimatorCompat);
  }
  
  static interface Creator {
    @NonNull
    ValueAnimatorCompat createAnimator();
  }
  
  static abstract class Impl {
    abstract void addListener(AnimatorListenerProxy param1AnimatorListenerProxy);
    
    abstract void addUpdateListener(AnimatorUpdateListenerProxy param1AnimatorUpdateListenerProxy);
    
    abstract void cancel();
    
    abstract void end();
    
    abstract float getAnimatedFloatValue();
    
    abstract float getAnimatedFraction();
    
    abstract int getAnimatedIntValue();
    
    abstract long getDuration();
    
    abstract boolean isRunning();
    
    abstract void setDuration(long param1Long);
    
    abstract void setFloatValues(float param1Float1, float param1Float2);
    
    abstract void setIntValues(int param1Int1, int param1Int2);
    
    abstract void setInterpolator(Interpolator param1Interpolator);
    
    abstract void start();
    
    static interface AnimatorListenerProxy {
      void onAnimationCancel();
      
      void onAnimationEnd();
      
      void onAnimationStart();
    }
    
    static interface AnimatorUpdateListenerProxy {
      void onAnimationUpdate();
    }
  }
  
  static interface AnimatorListenerProxy {
    void onAnimationCancel();
    
    void onAnimationEnd();
    
    void onAnimationStart();
  }
  
  static interface AnimatorUpdateListenerProxy {
    void onAnimationUpdate();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/ValueAnimatorCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */