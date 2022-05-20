package app.gamer.quadstellar.newdevices.timely.model.number;

import app.gamer.quadstellar.newdevices.timely.model.core.Figure;

public class Nine extends Figure {
  private static Nine INSTANCE;
  
  private static final float[][] POINTS;
  
  static {
    float[] arrayOfFloat1 = { 0.2596685F, 0.4088398F };
    float[] arrayOfFloat2 = { 0.49723756F, 0.9944751F };
    POINTS = new float[][] { 
        { 0.8093923F, 0.5524862F }, { 0.68508285F, 0.7513812F }, { 0.29834256F, 0.7403315F }, arrayOfFloat1, { 0.2320442F, 0.044198897F }, { 0.8176796F, -0.044198897F }, { 0.8508287F, 0.4088398F }, { 0.839779F, 0.5966851F }, { 0.71270716F, 0.6685083F }, { 0.49723756F, 0.9944751F }, 
        arrayOfFloat2, { 0.49723756F, 0.9944751F }, { 0.49723756F, 0.9944751F } };
    INSTANCE = new Nine();
  }
  
  protected Nine() {
    super(POINTS);
  }
  
  public static Nine getInstance() {
    return INSTANCE;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/timely/model/number/Nine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */