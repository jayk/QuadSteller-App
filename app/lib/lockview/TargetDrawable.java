package app.lib.lockview;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.animation.Animation;

public class TargetDrawable {
  private static final boolean DEBUG = false;
  
  public static final int[] STATE_ACTIVE = new int[] { 16842910, 16842914 };
  
  public static final int[] STATE_FOCUSED;
  
  public static final int[] STATE_INACTIVE = new int[] { 16842910, -16842914 };
  
  private static final String TAG = "TargetDrawable";
  
  private float mAlpha = 1.0F;
  
  private Drawable mDrawable;
  
  private boolean mEnabled = true;
  
  private int mNumDrawables = 1;
  
  private float mPositionX = 0.0F;
  
  private float mPositionY = 0.0F;
  
  private final int mResourceId;
  
  private float mScaleX = 1.0F;
  
  private float mScaleY = 1.0F;
  
  private float mTranslationX = 0.0F;
  
  private float mTranslationY = 0.0F;
  
  static {
    STATE_FOCUSED = new int[] { 16842910, -16842914, 16842908 };
  }
  
  public TargetDrawable(Resources paramResources, int paramInt1, int paramInt2) {
    this.mResourceId = paramInt1;
    setDrawable(paramResources, paramInt1);
    this.mNumDrawables = paramInt2;
  }
  
  public TargetDrawable(TargetDrawable paramTargetDrawable) {
    this.mResourceId = paramTargetDrawable.mResourceId;
    if (paramTargetDrawable.mDrawable != null) {
      Drawable drawable = paramTargetDrawable.mDrawable.mutate();
    } else {
      paramTargetDrawable = null;
    } 
    this.mDrawable = (Drawable)paramTargetDrawable;
    resizeDrawables();
    setState(STATE_INACTIVE);
  }
  
  private void resizeDrawables() {
    if (this.mDrawable instanceof StateListDrawable) {
      StateListDrawable stateListDrawable = (StateListDrawable)this.mDrawable;
      int i = 0;
      int j = 0;
      byte b;
      for (b = 0; b < this.mNumDrawables; b++) {
        stateListDrawable.selectDrawable(b);
        Drawable drawable = stateListDrawable.getCurrent();
        i = Math.max(i, drawable.getIntrinsicWidth());
        j = Math.max(j, drawable.getIntrinsicHeight());
      } 
      stateListDrawable.setBounds(0, 0, i, j);
      for (b = 0; b < this.mNumDrawables; b++) {
        stateListDrawable.selectDrawable(b);
        stateListDrawable.getCurrent().setBounds(0, 0, i, j);
      } 
    } else if (this.mDrawable != null) {
      this.mDrawable.setBounds(0, 0, this.mDrawable.getIntrinsicWidth(), this.mDrawable.getIntrinsicHeight());
    } 
  }
  
  public void draw(Canvas paramCanvas) {
    if (this.mDrawable != null && this.mEnabled) {
      paramCanvas.save(1);
      paramCanvas.scale(this.mScaleX, this.mScaleY, this.mPositionX, this.mPositionY);
      paramCanvas.translate(this.mTranslationX + this.mPositionX, this.mTranslationY + this.mPositionY);
      paramCanvas.translate(getWidth() * -0.5F, getHeight() * -0.5F);
      this.mDrawable.setAlpha(Math.round(this.mAlpha * 255.0F));
      this.mDrawable.draw(paramCanvas);
      paramCanvas.restore();
    } 
  }
  
  public float getAlpha() {
    return this.mAlpha;
  }
  
  public int getHeight() {
    return (this.mDrawable != null) ? this.mDrawable.getIntrinsicHeight() : 0;
  }
  
  public float getPositionX() {
    return this.mPositionX;
  }
  
  public float getPositionY() {
    return this.mPositionY;
  }
  
  public int getResourceId() {
    return this.mResourceId;
  }
  
  public float getScaleX() {
    return this.mScaleX;
  }
  
  public float getScaleY() {
    return this.mScaleY;
  }
  
  public int getWidth() {
    return (this.mDrawable != null) ? this.mDrawable.getIntrinsicWidth() : 0;
  }
  
  public float getX() {
    return this.mTranslationX;
  }
  
  public float getY() {
    return this.mTranslationY;
  }
  
  public boolean isActive() {
    int[] arrayOfInt;
    byte b;
    if (this.mDrawable instanceof StateListDrawable) {
      arrayOfInt = ((StateListDrawable)this.mDrawable).getState();
      for (b = 0; b < arrayOfInt.length; b++) {
        if (arrayOfInt[b] == 16842908)
          return true; 
      } 
    } 
    boolean bool = false;
    while (b < arrayOfInt.length) {
      if (arrayOfInt[b] == 16842908)
        return true; 
      b++;
    } 
  }
  
  public boolean isEnabled() {
    return (this.mDrawable != null && this.mEnabled);
  }
  
  public void setAlpha(float paramFloat) {
    this.mAlpha = paramFloat;
  }
  
  public void setAnimation(Animation paramAnimation) {}
  
  public void setDrawable(Resources paramResources, int paramInt) {
    Drawable drawable1;
    Drawable drawable2 = null;
    if (paramInt == 0) {
      paramResources = null;
    } else {
      drawable1 = paramResources.getDrawable(paramInt);
    } 
    if (drawable1 != null)
      drawable2 = drawable1.mutate(); 
    this.mDrawable = drawable2;
    resizeDrawables();
    setState(STATE_INACTIVE);
  }
  
  public void setEnabled(boolean paramBoolean) {
    this.mEnabled = paramBoolean;
  }
  
  public void setPositionX(float paramFloat) {
    this.mPositionX = paramFloat;
  }
  
  public void setPositionY(float paramFloat) {
    this.mPositionY = paramFloat;
  }
  
  public void setScaleX(float paramFloat) {
    this.mScaleX = paramFloat;
  }
  
  public void setScaleY(float paramFloat) {
    this.mScaleY = paramFloat;
  }
  
  public void setState(int[] paramArrayOfint) {
    if (this.mDrawable instanceof StateListDrawable)
      ((StateListDrawable)this.mDrawable).setState(paramArrayOfint); 
  }
  
  public void setX(float paramFloat) {
    this.mTranslationX = paramFloat;
  }
  
  public void setY(float paramFloat) {
    this.mTranslationY = paramFloat;
  }
  
  static class DrawableWithAlpha extends Drawable {
    private float mAlpha = 1.0F;
    
    private Drawable mRealDrawable;
    
    public DrawableWithAlpha(Drawable param1Drawable) {
      this.mRealDrawable = param1Drawable;
    }
    
    public void draw(Canvas param1Canvas) {
      this.mRealDrawable.setAlpha(Math.round(this.mAlpha * 255.0F));
      this.mRealDrawable.draw(param1Canvas);
    }
    
    public int getAlpha() {
      return (int)this.mAlpha;
    }
    
    public int getOpacity() {
      return this.mRealDrawable.getOpacity();
    }
    
    public void setAlpha(float param1Float) {
      this.mAlpha = param1Float;
    }
    
    public void setAlpha(int param1Int) {
      this.mRealDrawable.setAlpha(param1Int);
    }
    
    public void setColorFilter(ColorFilter param1ColorFilter) {
      this.mRealDrawable.setColorFilter(param1ColorFilter);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/lockview/TargetDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */