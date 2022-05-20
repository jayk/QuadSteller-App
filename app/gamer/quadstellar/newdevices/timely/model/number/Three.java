package app.gamer.quadstellar.newdevices.timely.model.number;

import app.gamer.quadstellar.newdevices.timely.model.core.Figure;

public class Three extends Figure {
  private static Three INSTANCE;
  
  private static final float[][] POINTS = new float[][] { 
      { 0.36187845F, 0.29834256F }, { 0.3480663F, 0.14917128F }, { 0.47513813F, 0.09944751F }, { 0.54972374F, 0.09944751F }, { 0.86187845F, 0.09944751F }, { 0.80662984F, 0.53038675F }, { 0.54972374F, 0.53038675F }, { 0.8729282F, 0.53038675F }, { 0.8287293F, 0.9944751F }, { 0.5524862F, 0.9944751F }, 
      { 0.29834256F, 0.9944751F }, { 0.30939227F, 0.8287293F }, { 0.3121547F, 0.7900553F } };
  
  static {
    INSTANCE = new Three();
  }
  
  protected Three() {
    super(POINTS);
  }
  
  public static Three getInstance() {
    return INSTANCE;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/timely/model/number/Three.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */