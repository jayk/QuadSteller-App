package app.gamer.quadstellar.newdevices.timely.model.number;

import app.gamer.quadstellar.newdevices.timely.model.core.Figure;

public class Four extends Figure {
  private static Four INSTANCE;
  
  private static final float[][] POINTS;
  
  static {
    float[] arrayOfFloat1 = { 0.8563536F, 0.80662984F };
    float[] arrayOfFloat2 = { 0.23756906F, 0.80662984F };
    float[] arrayOfFloat3 = { 0.23756906F, 0.80662984F };
    float[] arrayOfFloat4 = { 0.23756906F, 0.80662984F };
    float[] arrayOfFloat5 = { 0.71270716F, 0.13812155F };
    float[] arrayOfFloat6 = { 0.71270716F, 0.13812155F };
    float[] arrayOfFloat7 = { 0.71270716F, 0.80662984F };
    float[] arrayOfFloat8 = { 0.71270716F, 0.80662984F };
    float[] arrayOfFloat9 = { 0.71270716F, 0.80662984F };
    float[] arrayOfFloat10 = { 0.71270716F, 0.98895025F };
    POINTS = new float[][] { 
        { 0.8563536F, 0.80662984F }, arrayOfFloat1, arrayOfFloat2, arrayOfFloat3, arrayOfFloat4, { 0.71270716F, 0.13812155F }, arrayOfFloat5, arrayOfFloat6, arrayOfFloat7, arrayOfFloat8, 
        arrayOfFloat9, arrayOfFloat10, { 0.71270716F, 0.98895025F } };
    INSTANCE = new Four();
  }
  
  protected Four() {
    super(POINTS);
  }
  
  public static Four getInstance() {
    return INSTANCE;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/timely/model/number/Four.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */