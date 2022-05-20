package app.gamer.quadstellar.newdevices.timely.model;

import app.gamer.quadstellar.newdevices.timely.model.number.Eight;
import app.gamer.quadstellar.newdevices.timely.model.number.Five;
import app.gamer.quadstellar.newdevices.timely.model.number.Four;
import app.gamer.quadstellar.newdevices.timely.model.number.Nine;
import app.gamer.quadstellar.newdevices.timely.model.number.Null;
import app.gamer.quadstellar.newdevices.timely.model.number.One;
import app.gamer.quadstellar.newdevices.timely.model.number.Seven;
import app.gamer.quadstellar.newdevices.timely.model.number.Six;
import app.gamer.quadstellar.newdevices.timely.model.number.Three;
import app.gamer.quadstellar.newdevices.timely.model.number.Two;
import app.gamer.quadstellar.newdevices.timely.model.number.Zero;
import java.security.InvalidParameterException;

public class NumberUtils {
  public static float[][] getControlPointsFor(int paramInt) {
    switch (paramInt) {
      default:
        throw new InvalidParameterException("Unsupported number requested");
      case -1:
        return Null.getInstance().getControlPoints();
      case 0:
        return Zero.getInstance().getControlPoints();
      case 1:
        return One.getInstance().getControlPoints();
      case 2:
        return Two.getInstance().getControlPoints();
      case 3:
        return Three.getInstance().getControlPoints();
      case 4:
        return Four.getInstance().getControlPoints();
      case 5:
        return Five.getInstance().getControlPoints();
      case 6:
        return Six.getInstance().getControlPoints();
      case 7:
        return Seven.getInstance().getControlPoints();
      case 8:
        return Eight.getInstance().getControlPoints();
      case 9:
        break;
    } 
    return Nine.getInstance().getControlPoints();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/timely/model/NumberUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */