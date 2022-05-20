package app.gamer.quadstellar.newdevices.timely.model.number;

import app.gamer.quadstellar.newdevices.timely.model.core.Figure;

public class One extends Figure {
  private static One INSTANCE;
  
  private static final float[][] POINTS;
  
  static {
    float[] arrayOfFloat1 = { 0.42541435F, 0.113259666F };
    float[] arrayOfFloat2 = { 0.57734805F, 1.0F };
    float[] arrayOfFloat3 = { 0.57734805F, 1.0F };
    POINTS = new float[][] { 
        arrayOfFloat1, { 0.42541435F, 0.113259666F }, { 0.57734805F, 0.113259666F }, { 0.57734805F, 0.113259666F }, { 0.57734805F, 0.113259666F }, { 0.57734805F, 1.0F }, arrayOfFloat2, arrayOfFloat3, { 0.57734805F, 1.0F }, { 0.57734805F, 1.0F }, 
        { 0.57734805F, 1.0F }, { 0.57734805F, 1.0F }, { 0.57734805F, 1.0F } };
    INSTANCE = new One();
  }
  
  protected One() {
    super(POINTS);
  }
  
  public static One getInstance() {
    return INSTANCE;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/timely/model/number/One.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */