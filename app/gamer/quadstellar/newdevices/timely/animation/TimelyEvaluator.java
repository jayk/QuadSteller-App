package app.gamer.quadstellar.newdevices.timely.animation;

import android.animation.TypeEvaluator;

public class TimelyEvaluator implements TypeEvaluator<float[][]> {
  private float[][] _cachedPoints = (float[][])null;
  
  private void initCache(int paramInt) {
    if (this._cachedPoints == null || this._cachedPoints.length != paramInt)
      this._cachedPoints = new float[paramInt][2]; 
  }
  
  public float[][] evaluate(float paramFloat, float[][] paramArrayOffloat1, float[][] paramArrayOffloat2) {
    int i = paramArrayOffloat1.length;
    initCache(i);
    for (byte b = 0; b < i; b++) {
      this._cachedPoints[b][0] = paramArrayOffloat1[b][0] + (paramArrayOffloat2[b][0] - paramArrayOffloat1[b][0]) * paramFloat;
      this._cachedPoints[b][1] = paramArrayOffloat1[b][1] + (paramArrayOffloat2[b][1] - paramArrayOffloat1[b][1]) * paramFloat;
    } 
    return this._cachedPoints;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/timely/animation/TimelyEvaluator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */