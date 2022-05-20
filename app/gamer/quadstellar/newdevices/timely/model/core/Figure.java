package app.gamer.quadstellar.newdevices.timely.model.core;

public abstract class Figure {
  public static final int NO_VALUE = -1;
  
  protected float[][] controlPoints = (float[][])null;
  
  protected int pointsCount = -1;
  
  protected Figure(float[][] paramArrayOffloat) {
    this.controlPoints = paramArrayOffloat;
    this.pointsCount = (paramArrayOffloat.length + 2) / 3;
  }
  
  public float[][] getControlPoints() {
    return this.controlPoints;
  }
  
  public int getPointsCount() {
    return this.pointsCount;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/timely/model/core/Figure.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */