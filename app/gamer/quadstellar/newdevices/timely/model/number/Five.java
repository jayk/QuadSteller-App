package app.gamer.quadstellar.newdevices.timely.model.number;

import app.gamer.quadstellar.newdevices.timely.model.core.Figure;

public class Five extends Figure {
  private static Five INSTANCE;
  
  private static final float[][] POINTS;
  
  static {
    float[] arrayOfFloat1 = { 0.80662984F, 0.110497236F };
    float[] arrayOfFloat2 = { 0.50276244F, 0.110497236F };
    float[] arrayOfFloat3 = { 0.50276244F, 0.110497236F };
    float[] arrayOfFloat4 = { 0.50276244F, 0.110497236F };
    float[] arrayOfFloat5 = { 0.39779004F, 0.43093923F };
    float[] arrayOfFloat6 = { 0.39779004F, 0.43093923F };
    float[] arrayOfFloat7 = { 0.39779004F, 0.43093923F };
    float[] arrayOfFloat8 = { 0.80110496F, 0.46961325F };
    float[] arrayOfFloat9 = { 0.80110496F, 0.71270716F };
    float[] arrayOfFloat10 = { 0.77348065F, 1.0110497F };
    float[] arrayOfFloat11 = { 0.3756906F, 1.0939226F };
    float[] arrayOfFloat12 = { 0.24861878F, 0.8508287F };
    POINTS = new float[][] { 
        arrayOfFloat1, arrayOfFloat2, arrayOfFloat3, arrayOfFloat4, arrayOfFloat5, arrayOfFloat6, arrayOfFloat7, { 0.5359116F, 0.3646409F }, arrayOfFloat8, arrayOfFloat9, 
        arrayOfFloat10, arrayOfFloat11, arrayOfFloat12 };
    INSTANCE = new Five();
  }
  
  protected Five() {
    super(POINTS);
  }
  
  public static Five getInstance() {
    return INSTANCE;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/timely/model/number/Five.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */