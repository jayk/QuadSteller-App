package app.gamer.quadstellar.newdevices.timely.model.number;

import app.gamer.quadstellar.newdevices.timely.model.core.Figure;

public class Six extends Figure {
  private static Six INSTANCE;
  
  private static final float[][] POINTS;
  
  static {
    float[] arrayOfFloat1 = { 0.6077348F, 0.110497236F };
    float[] arrayOfFloat2 = { 0.26519337F, 0.5082873F };
    float[] arrayOfFloat3 = { 0.80662984F, 0.3646409F };
    POINTS = new float[][] { 
        { 0.6077348F, 0.110497236F }, { 0.6077348F, 0.110497236F }, arrayOfFloat1, { 0.6077348F, 0.110497236F }, { 0.3922652F, 0.4364641F }, arrayOfFloat2, { 0.25414366F, 0.6961326F }, { 0.2872928F, 1.1301713F }, { 0.8729282F, 1.0607735F }, { 0.8453039F, 0.6961326F }, 
        arrayOfFloat3, { 0.4198895F, 0.35359117F }, { 0.29558012F, 0.5524862F } };
    INSTANCE = new Six();
  }
  
  protected Six() {
    super(POINTS);
  }
  
  public static Six getInstance() {
    return INSTANCE;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/timely/model/number/Six.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */