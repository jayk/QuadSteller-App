package app.gamer.quadstellar.mode;

import app.gamer.quadstellar.App;
import app.gamer.quadstellar.utils.ByteUtils;

public class EFPowerStripTimer extends EFTimer {
  public static final int CTL_1 = 2;
  
  public static final int CTL_2 = 4;
  
  public static final int CTL_3 = 8;
  
  public static final int CTL_4 = 16;
  
  public static final int CTL_ALL = 31;
  
  public static final int CTL_USB = 1;
  
  private static final long serialVersionUID = 4365814412846868811L;
  
  private int controlType = 31;
  
  private String getControlText() {
    switch (this.controlType) {
      default:
        return "";
      case 1:
        return App.getAppResources().getString(2131296986);
      case 2:
        return App.getAppResources().getString(2131296981);
      case 4:
        return App.getAppResources().getString(2131296982);
      case 8:
        return App.getAppResources().getString(2131296983);
      case 16:
        return App.getAppResources().getString(2131296984);
      case 31:
        break;
    } 
    return App.getAppResources().getString(2131296985);
  }
  
  public static long getSerialversionuid() {
    return 4365814412846868811L;
  }
  
  public String getAllText(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    if (this.type == 1) {
      stringBuilder.append(String.format("%1$s%2$02d:%3$02d", new Object[] { App.getAppResources().getString(2131297500) + ":", Integer.valueOf(this.openHour), Integer.valueOf(this.openMinute) }));
    } else if (this.type == 2) {
      stringBuilder.append(String.format("%1$s%2$02d:%3$02d", new Object[] { App.getAppResources().getString(2131297499) + ":", Integer.valueOf(this.closeHour), Integer.valueOf(this.closeMinute) }));
    } 
    if (stringBuilder.length() > 0) {
      stringBuilder.append(" | ");
      stringBuilder.append(getControlText());
    } 
    return stringBuilder.toString();
  }
  
  public int getControlType() {
    return this.controlType;
  }
  
  public byte[] getTimerData() {
    byte[] arrayOfByte1 = new byte[4];
    if (this.type == 1) {
      arrayOfByte1[0] = (byte)(byte)(this.openHour & 0xFF);
      arrayOfByte1[1] = (byte)(byte)(this.openMinute & 0xFF);
      arrayOfByte1[2] = (byte)110;
      arrayOfByte1[3] = (byte)111;
    } else if (this.type == 2) {
      arrayOfByte1[0] = (byte)110;
      arrayOfByte1[1] = (byte)111;
      arrayOfByte1[2] = (byte)(byte)(this.closeHour & 0xFF);
      arrayOfByte1[3] = (byte)(byte)(this.closeMinute & 0xFF);
    } 
    byte[] arrayOfByte2 = getRepeatData();
    if (this.state) {
      byte[] arrayOfByte = ByteUtils.int2OneByte(this.controlType + 128);
      return ByteUtils.append(7, new byte[][] { ByteUtils.int2OneByte(this.number), arrayOfByte1, arrayOfByte2, arrayOfByte });
    } 
    byte[] arrayOfByte3 = ByteUtils.int2OneByte(this.controlType);
    return ByteUtils.append(7, new byte[][] { ByteUtils.int2OneByte(this.number), arrayOfByte1, arrayOfByte2, arrayOfByte3 });
  }
  
  public void setControlType(int paramInt) {
    this.controlType = paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/mode/EFPowerStripTimer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */