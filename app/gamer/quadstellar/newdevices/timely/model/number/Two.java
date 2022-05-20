package app.gamer.quadstellar.newdevices.timely.model.number;

import app.gamer.quadstellar.newdevices.timely.model.core.Figure;

public class Two extends Figure {
  private static Two INSTANCE;
  
  private static final float[][] POINTS;
  
  static {
    float[] arrayOfFloat1 = { 0.30939227F, 0.3314917F };
    float[] arrayOfFloat2 = { 0.32596686F, 0.011049724F };
    float[] arrayOfFloat3 = { 0.7900553F, 0.022099448F };
    float[] arrayOfFloat4 = { 0.7983425F, 0.33701658F };
    float[] arrayOfFloat5 = { 0.71823204F, 0.5414365F };
    float[] arrayOfFloat6 = { 0.5966851F, 0.67403316F };
    float[] arrayOfFloat7 = { 0.4088398F, 0.8563536F };
    float[] arrayOfFloat8 = { 0.31491712F, 0.97790056F };
    float[] arrayOfFloat9 = { 0.31491712F, 0.97790056F };
    float[] arrayOfFloat10 = { 0.8121547F, 0.97790056F };
    float[] arrayOfFloat11 = { 0.8121547F, 0.97790056F };
    POINTS = new float[][] { 
        arrayOfFloat1, arrayOfFloat2, arrayOfFloat3, arrayOfFloat4, { 0.7983425F, 0.43093923F }, arrayOfFloat5, arrayOfFloat6, { 0.519337F, 0.76243097F }, arrayOfFloat7, arrayOfFloat8, 
        arrayOfFloat9, arrayOfFloat10, arrayOfFloat11 };
    INSTANCE = new Two();
  }
  
  protected Two() {
    super(POINTS);
  }
  
  public static Two getInstance() {
    return INSTANCE;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/timely/model/number/Two.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */