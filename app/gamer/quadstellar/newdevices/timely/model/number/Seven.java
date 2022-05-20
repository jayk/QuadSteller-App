package app.gamer.quadstellar.newdevices.timely.model.number;

import app.gamer.quadstellar.newdevices.timely.model.core.Figure;

public class Seven extends Figure {
  private static Seven INSTANCE;
  
  private static final float[][] POINTS;
  
  static {
    float[] arrayOfFloat1 = { 0.2596685F, 0.1160221F };
    float[] arrayOfFloat2 = { 0.2596685F, 0.1160221F };
    float[] arrayOfFloat3 = { 0.8729282F, 0.1160221F };
    float[] arrayOfFloat4 = { 0.8729282F, 0.1160221F };
    float[] arrayOfFloat5 = { 0.47734806F, 0.7331492F };
    float[] arrayOfFloat6 = { 0.47734806F, 0.7331492F };
    POINTS = new float[][] { 
        arrayOfFloat1, arrayOfFloat2, { 0.8729282F, 0.1160221F }, arrayOfFloat3, arrayOfFloat4, { 0.7F, 0.42209944F }, { 0.7F, 0.42209944F }, { 0.7F, 0.42209944F }, arrayOfFloat5, { 0.47734806F, 0.7331492F }, 
        arrayOfFloat6, { 0.25414366F, 1.0F }, { 0.25414366F, 1.0F } };
    INSTANCE = new Seven();
  }
  
  protected Seven() {
    super(POINTS);
  }
  
  public static Seven getInstance() {
    return INSTANCE;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/timely/model/number/Seven.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */