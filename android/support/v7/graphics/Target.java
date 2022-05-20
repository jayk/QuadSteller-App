package android.support.v7.graphics;

import android.support.annotation.FloatRange;

public final class Target {
  public static final Target DARK_MUTED;
  
  public static final Target DARK_VIBRANT;
  
  static final int INDEX_MAX = 2;
  
  static final int INDEX_MIN = 0;
  
  static final int INDEX_TARGET = 1;
  
  static final int INDEX_WEIGHT_LUMA = 1;
  
  static final int INDEX_WEIGHT_POP = 2;
  
  static final int INDEX_WEIGHT_SAT = 0;
  
  public static final Target LIGHT_MUTED;
  
  public static final Target LIGHT_VIBRANT = new Target();
  
  private static final float MAX_DARK_LUMA = 0.45F;
  
  private static final float MAX_MUTED_SATURATION = 0.4F;
  
  private static final float MAX_NORMAL_LUMA = 0.7F;
  
  private static final float MIN_LIGHT_LUMA = 0.55F;
  
  private static final float MIN_NORMAL_LUMA = 0.3F;
  
  private static final float MIN_VIBRANT_SATURATION = 0.35F;
  
  public static final Target MUTED;
  
  private static final float TARGET_DARK_LUMA = 0.26F;
  
  private static final float TARGET_LIGHT_LUMA = 0.74F;
  
  private static final float TARGET_MUTED_SATURATION = 0.3F;
  
  private static final float TARGET_NORMAL_LUMA = 0.5F;
  
  private static final float TARGET_VIBRANT_SATURATION = 1.0F;
  
  public static final Target VIBRANT = new Target();
  
  private static final float WEIGHT_LUMA = 0.52F;
  
  private static final float WEIGHT_POPULATION = 0.24F;
  
  private static final float WEIGHT_SATURATION = 0.24F;
  
  boolean mIsExclusive = true;
  
  final float[] mLightnessTargets = new float[3];
  
  final float[] mSaturationTargets = new float[3];
  
  final float[] mWeights = new float[3];
  
  static {
    setDefaultNormalLightnessValues(VIBRANT);
    setDefaultVibrantSaturationValues(VIBRANT);
    DARK_VIBRANT = new Target();
    setDefaultDarkLightnessValues(DARK_VIBRANT);
    setDefaultVibrantSaturationValues(DARK_VIBRANT);
    LIGHT_MUTED = new Target();
    setDefaultLightLightnessValues(LIGHT_MUTED);
    setDefaultMutedSaturationValues(LIGHT_MUTED);
    MUTED = new Target();
    setDefaultNormalLightnessValues(MUTED);
    setDefaultMutedSaturationValues(MUTED);
    DARK_MUTED = new Target();
    setDefaultDarkLightnessValues(DARK_MUTED);
    setDefaultMutedSaturationValues(DARK_MUTED);
  }
  
  Target() {
    setTargetDefaultValues(this.mSaturationTargets);
    setTargetDefaultValues(this.mLightnessTargets);
    setDefaultWeights();
  }
  
  Target(Target paramTarget) {
    System.arraycopy(paramTarget.mSaturationTargets, 0, this.mSaturationTargets, 0, this.mSaturationTargets.length);
    System.arraycopy(paramTarget.mLightnessTargets, 0, this.mLightnessTargets, 0, this.mLightnessTargets.length);
    System.arraycopy(paramTarget.mWeights, 0, this.mWeights, 0, this.mWeights.length);
  }
  
  private static void setDefaultDarkLightnessValues(Target paramTarget) {
    paramTarget.mLightnessTargets[1] = 0.26F;
    paramTarget.mLightnessTargets[2] = 0.45F;
  }
  
  private static void setDefaultLightLightnessValues(Target paramTarget) {
    paramTarget.mLightnessTargets[0] = 0.55F;
    paramTarget.mLightnessTargets[1] = 0.74F;
  }
  
  private static void setDefaultMutedSaturationValues(Target paramTarget) {
    paramTarget.mSaturationTargets[1] = 0.3F;
    paramTarget.mSaturationTargets[2] = 0.4F;
  }
  
  private static void setDefaultNormalLightnessValues(Target paramTarget) {
    paramTarget.mLightnessTargets[0] = 0.3F;
    paramTarget.mLightnessTargets[1] = 0.5F;
    paramTarget.mLightnessTargets[2] = 0.7F;
  }
  
  private static void setDefaultVibrantSaturationValues(Target paramTarget) {
    paramTarget.mSaturationTargets[0] = 0.35F;
    paramTarget.mSaturationTargets[1] = 1.0F;
  }
  
  private void setDefaultWeights() {
    this.mWeights[0] = 0.24F;
    this.mWeights[1] = 0.52F;
    this.mWeights[2] = 0.24F;
  }
  
  private static void setTargetDefaultValues(float[] paramArrayOffloat) {
    paramArrayOffloat[0] = 0.0F;
    paramArrayOffloat[1] = 0.5F;
    paramArrayOffloat[2] = 1.0F;
  }
  
  public float getLightnessWeight() {
    return this.mWeights[1];
  }
  
  @FloatRange(from = 0.0D, to = 1.0D)
  public float getMaximumLightness() {
    return this.mLightnessTargets[2];
  }
  
  @FloatRange(from = 0.0D, to = 1.0D)
  public float getMaximumSaturation() {
    return this.mSaturationTargets[2];
  }
  
  @FloatRange(from = 0.0D, to = 1.0D)
  public float getMinimumLightness() {
    return this.mLightnessTargets[0];
  }
  
  @FloatRange(from = 0.0D, to = 1.0D)
  public float getMinimumSaturation() {
    return this.mSaturationTargets[0];
  }
  
  public float getPopulationWeight() {
    return this.mWeights[2];
  }
  
  public float getSaturationWeight() {
    return this.mWeights[0];
  }
  
  @FloatRange(from = 0.0D, to = 1.0D)
  public float getTargetLightness() {
    return this.mLightnessTargets[1];
  }
  
  @FloatRange(from = 0.0D, to = 1.0D)
  public float getTargetSaturation() {
    return this.mSaturationTargets[1];
  }
  
  public boolean isExclusive() {
    return this.mIsExclusive;
  }
  
  void normalizeWeights() {
    float f = 0.0F;
    byte b = 0;
    int i = this.mWeights.length;
    while (b < i) {
      float f1 = this.mWeights[b];
      float f2 = f;
      if (f1 > 0.0F)
        f2 = f + f1; 
      b++;
      f = f2;
    } 
    if (f != 0.0F) {
      b = 0;
      i = this.mWeights.length;
      while (b < i) {
        if (this.mWeights[b] > 0.0F) {
          float[] arrayOfFloat = this.mWeights;
          arrayOfFloat[b] = arrayOfFloat[b] / f;
        } 
        b++;
      } 
    } 
  }
  
  static {
    setDefaultLightLightnessValues(LIGHT_VIBRANT);
    setDefaultVibrantSaturationValues(LIGHT_VIBRANT);
  }
  
  public static final class Builder {
    private final Target mTarget = new Target();
    
    public Builder() {}
    
    public Builder(Target param1Target) {}
    
    public Target build() {
      return this.mTarget;
    }
    
    public Builder setExclusive(boolean param1Boolean) {
      this.mTarget.mIsExclusive = param1Boolean;
      return this;
    }
    
    public Builder setLightnessWeight(@FloatRange(from = 0.0D) float param1Float) {
      this.mTarget.mWeights[1] = param1Float;
      return this;
    }
    
    public Builder setMaximumLightness(@FloatRange(from = 0.0D, to = 1.0D) float param1Float) {
      this.mTarget.mLightnessTargets[2] = param1Float;
      return this;
    }
    
    public Builder setMaximumSaturation(@FloatRange(from = 0.0D, to = 1.0D) float param1Float) {
      this.mTarget.mSaturationTargets[2] = param1Float;
      return this;
    }
    
    public Builder setMinimumLightness(@FloatRange(from = 0.0D, to = 1.0D) float param1Float) {
      this.mTarget.mLightnessTargets[0] = param1Float;
      return this;
    }
    
    public Builder setMinimumSaturation(@FloatRange(from = 0.0D, to = 1.0D) float param1Float) {
      this.mTarget.mSaturationTargets[0] = param1Float;
      return this;
    }
    
    public Builder setPopulationWeight(@FloatRange(from = 0.0D) float param1Float) {
      this.mTarget.mWeights[2] = param1Float;
      return this;
    }
    
    public Builder setSaturationWeight(@FloatRange(from = 0.0D) float param1Float) {
      this.mTarget.mWeights[0] = param1Float;
      return this;
    }
    
    public Builder setTargetLightness(@FloatRange(from = 0.0D, to = 1.0D) float param1Float) {
      this.mTarget.mLightnessTargets[1] = param1Float;
      return this;
    }
    
    public Builder setTargetSaturation(@FloatRange(from = 0.0D, to = 1.0D) float param1Float) {
      this.mTarget.mSaturationTargets[1] = param1Float;
      return this;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/graphics/Target.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */