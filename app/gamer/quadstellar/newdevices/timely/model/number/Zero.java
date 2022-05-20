package app.gamer.quadstellar.newdevices.timely.model.number;

import app.gamer.quadstellar.newdevices.timely.model.core.Figure;

public class Zero extends Figure {
  private static Zero INSTANCE;
  
  private static final float[][] POINTS;
  
  static {
    float[] arrayOfFloat1 = { 0.5524862F, 0.09944751F };
    float[] arrayOfFloat2 = { 0.86187845F, 0.3314917F };
    float[] arrayOfFloat3 = { 0.73480666F, 0.9944751F };
    POINTS = new float[][] { 
        { 0.24585636F, 0.5524862F }, { 0.24585636F, 0.3314917F }, { 0.37016574F, 0.09944751F }, arrayOfFloat1, { 0.73480666F, 0.09944751F }, arrayOfFloat2, { 0.86187845F, 0.5524862F }, { 0.86187845F, 0.77348065F }, arrayOfFloat3, { 0.5524862F, 0.9944751F }, 
        { 0.37016574F, 0.9944751F }, { 0.24585636F, 0.77348065F }, { 0.24585636F, 0.5524862F } };
    INSTANCE = new Zero();
  }
  
  protected Zero() {
    super(POINTS);
  }
  
  public static Zero getInstance() {
    return INSTANCE;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/timely/model/number/Zero.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */