package app.gamer.quadstellar.newdevices.timely.model.number;

import app.gamer.quadstellar.newdevices.timely.model.core.Figure;

public class Eight extends Figure {
  private static Eight INSTANCE;
  
  private static final float[][] POINTS;
  
  static {
    float[] arrayOfFloat1 = { 0.55801105F, 0.53038675F };
    float[] arrayOfFloat2 = { 0.24309392F, 0.5248619F };
    float[] arrayOfFloat3 = { 0.24309392F, 0.10497238F };
    float[] arrayOfFloat4 = { 0.8508287F, 0.53038675F };
    float[] arrayOfFloat5 = { 0.24309392F, 0.53038675F };
    float[] arrayOfFloat6 = { 0.8508287F, 0.98895025F };
    float[] arrayOfFloat7 = { 0.8508287F, 0.53038675F };
    POINTS = new float[][] { 
        arrayOfFloat1, arrayOfFloat2, arrayOfFloat3, { 0.55801105F, 0.10497238F }, { 0.8508287F, 0.10497238F }, arrayOfFloat4, { 0.55801105F, 0.53038675F }, arrayOfFloat5, { 0.19889502F, 0.98895025F }, { 0.55801105F, 0.98895025F }, 
        arrayOfFloat6, arrayOfFloat7, { 0.55801105F, 0.53038675F } };
    INSTANCE = new Eight();
  }
  
  protected Eight() {
    super(POINTS);
  }
  
  public static Eight getInstance() {
    return INSTANCE;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/timely/model/number/Eight.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */