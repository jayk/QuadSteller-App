package app.gamer.quadstellar.mode;

import app.gamer.quadstellar.App;
import app.gamer.quadstellar.utils.ByteUtils;

public class EFSwitchTimer extends EFTimer {
  public static final int CTL_1 = 1;
  
  public static final int CTL_2 = 2;
  
  public static final int CTL_3 = 3;
  
  public static final int CTL_ALL = 4;
  
  private static final long serialVersionUID = -2962934642515315185L;
  
  private int controlType = 4;
  
  private String getControlText(int paramInt) {
    switch (this.controlType) {
      default:
        return "";
      case 1:
        return App.getAppResources().getString(2131297468);
      case 2:
        return App.getAppResources().getString(2131297469);
      case 3:
        return (paramInt == 10) ? App.getAppResources().getString(2131297469) : App.getAppResources().getString(2131297470);
      case 4:
        break;
    } 
    return App.getAppResources().getString(2131297471);
  }
  
  public static long getSerialversionuid() {
    return -2962934642515315185L;
  }
  
  public String getAllText(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    if (this.type == 1) {
      stringBuilder.append(String.format("%1$s%2$02d:%3$02d", new Object[] { App.getAppResources().getString(2131297500) + ":", Integer.valueOf(this.openHour), Integer.valueOf(this.openMinute) }));
    } else if (this.type == 2) {
      stringBuilder.append(String.format("%1$s%2$02d:%3$02d", new Object[] { App.getAppResources().getString(2131297499) + ":", Integer.valueOf(this.closeHour), Integer.valueOf(this.closeMinute) }));
    } 
    if (stringBuilder.length() > 0 && paramInt != 11) {
      stringBuilder.append(" | ");
      stringBuilder.append(getControlText(paramInt));
    } 
    return stringBuilder.toString();
  }
  
  public int getControlType() {
    return this.controlType;
  }
  
  public byte[] getZigSwitchTimerData() {
    byte[] arrayOfByte1 = new byte[4];
    if (this.type == 1) {
      arrayOfByte1[0] = (byte)(byte)(this.openHour & 0xFF);
      arrayOfByte1[1] = (byte)(byte)(this.openMinute & 0xFF);
      arrayOfByte1[2] = (byte)110;
      arrayOfByte1[3] = (byte)111;
      byte[] arrayOfByte = getRepeatData();
      return ByteUtils.append(6, new byte[][] { arrayOfByte1, { (byte)(this.controlType & 0xFF) }, arrayOfByte });
    } 
    if (this.type == 2) {
      arrayOfByte1[0] = (byte)110;
      arrayOfByte1[1] = (byte)111;
      arrayOfByte1[2] = (byte)(byte)(this.closeHour & 0xFF);
      arrayOfByte1[3] = (byte)(byte)(this.closeMinute & 0xFF);
    } 
    byte[] arrayOfByte2 = getRepeatData();
    return ByteUtils.append(6, new byte[][] { arrayOfByte1, { (byte)(this.controlType & 0xFF) }, arrayOfByte2 });
  }
  
  public void setControlType(int paramInt) {
    this.controlType = paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/mode/EFSwitchTimer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */