package app.gamer.quadstellar.newdevices.timely.model.number;

import app.gamer.quadstellar.newdevices.timely.model.core.Figure;

public class Null extends Figure {
  private static final Null INSTANCE;
  
  private static final float[][] POINTS;
  
  static {
    float[] arrayOfFloat1 = { 0.5F, 0.5F };
    float[] arrayOfFloat2 = { 0.5F, 0.5F };
    float[] arrayOfFloat3 = { 0.5F, 0.5F };
    float[] arrayOfFloat4 = { 0.5F, 0.5F };
    float[] arrayOfFloat5 = { 0.5F, 0.5F };
    float[] arrayOfFloat6 = { 0.5F, 0.5F };
    float[] arrayOfFloat7 = { 0.5F, 0.5F };
    float[] arrayOfFloat8 = { 0.5F, 0.5F };
    float[] arrayOfFloat9 = { 0.5F, 0.5F };
    float[] arrayOfFloat10 = { 0.5F, 0.5F };
    float[] arrayOfFloat11 = { 0.5F, 0.5F };
    POINTS = new float[][] { 
        arrayOfFloat1, arrayOfFloat2, arrayOfFloat3, arrayOfFloat4, arrayOfFloat5, arrayOfFloat6, { 0.5F, 0.5F }, arrayOfFloat7, arrayOfFloat8, arrayOfFloat9, 
        arrayOfFloat10, { 0.5F, 0.5F }, arrayOfFloat11 };
    INSTANCE = new Null();
  }
  
  protected Null() {
    super(POINTS);
  }
  
  public static Null getInstance() {
    return INSTANCE;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/timely/model/number/Null.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */