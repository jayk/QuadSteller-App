package app.gamer.quadstellar.mode;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "EFDeviceSwitch")
public class EFDeviceSwitch extends EFDevice {
  public static final int SWITCH_STATE_0 = 0;
  
  public static final int SWITCH_STATE_1 = 1;
  
  public static final int SWITCH_STATE_2 = 2;
  
  public static final int SWITCH_STATE_3 = 3;
  
  public static final int SWITCH_STATE_4 = 4;
  
  public static final int SWITCH_STATE_5 = 5;
  
  public static final int SWITCH_STATE_6 = 6;
  
  public static final int SWITCH_STATE_7 = 7;
  
  private static final long serialVersionUID = -4999796627255160238L;
  
  @Column(name = "firmwareVersion")
  private String firmwareVersion;
  
  public String getFirmwareVersion() {
    return this.firmwareVersion;
  }
  
  public boolean getSwitchAllState(boolean paramBoolean) {
    if (this.deviceState == 0)
      return false; 
    if (this.deviceState == 7)
      paramBoolean = true; 
    return paramBoolean;
  }
  
  public byte[] getSwitchData() {
    byte[] arrayOfByte = new byte[3];
    switch (this.deviceState) {
      default:
        arrayOfByte[0] = (byte)0;
        arrayOfByte[1] = (byte)0;
        arrayOfByte[2] = (byte)0;
        return arrayOfByte;
      case 0:
        arrayOfByte[0] = (byte)0;
        arrayOfByte[1] = (byte)0;
        arrayOfByte[2] = (byte)0;
        return arrayOfByte;
      case 1:
        arrayOfByte[0] = (byte)0;
        arrayOfByte[1] = (byte)0;
        arrayOfByte[2] = (byte)1;
        return arrayOfByte;
      case 2:
        arrayOfByte[0] = (byte)0;
        arrayOfByte[1] = (byte)1;
        arrayOfByte[2] = (byte)0;
        return arrayOfByte;
      case 3:
        arrayOfByte[0] = (byte)0;
        arrayOfByte[1] = (byte)1;
        arrayOfByte[2] = (byte)1;
        return arrayOfByte;
      case 4:
        arrayOfByte[0] = (byte)1;
        arrayOfByte[1] = (byte)0;
        arrayOfByte[2] = (byte)0;
        return arrayOfByte;
      case 5:
        arrayOfByte[0] = (byte)1;
        arrayOfByte[1] = (byte)0;
        arrayOfByte[2] = (byte)1;
        return arrayOfByte;
      case 6:
        arrayOfByte[0] = (byte)1;
        arrayOfByte[1] = (byte)1;
        arrayOfByte[2] = (byte)0;
        return arrayOfByte;
      case 7:
        break;
    } 
    arrayOfByte[0] = (byte)1;
    arrayOfByte[1] = (byte)1;
    arrayOfByte[2] = (byte)1;
    return arrayOfByte;
  }
  
  public boolean getSwitchLeftState() {
    switch (this.deviceState) {
      default:
        return false;
      case 4:
        return true;
      case 5:
        return true;
      case 6:
        return true;
      case 7:
        break;
    } 
    return true;
  }
  
  public boolean getSwitchMidState() {
    switch (this.deviceState) {
      default:
        return false;
      case 2:
      case 3:
        return true;
      case 6:
        return true;
      case 7:
        break;
    } 
    return true;
  }
  
  public boolean getSwitchRightState() {
    switch (this.deviceState) {
      default:
        return false;
      case 1:
        return true;
      case 3:
        return true;
      case 5:
        return true;
      case 7:
        break;
    } 
    return true;
  }
  
  public void setDeviceState(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
    if (!paramBoolean1 && !paramBoolean2 && !paramBoolean3) {
      this.deviceState = 0;
      return;
    } 
    if (!paramBoolean1 && !paramBoolean2 && paramBoolean3) {
      this.deviceState = 1;
      return;
    } 
    if (!paramBoolean1 && paramBoolean2 && !paramBoolean3) {
      this.deviceState = 2;
      return;
    } 
    if (!paramBoolean1 && paramBoolean2 && paramBoolean3) {
      this.deviceState = 3;
      return;
    } 
    if (paramBoolean1 && !paramBoolean2 && !paramBoolean3) {
      this.deviceState = 4;
      return;
    } 
    if (paramBoolean1 && !paramBoolean2 && paramBoolean3) {
      this.deviceState = 5;
      return;
    } 
    if (paramBoolean1 && paramBoolean2 && !paramBoolean3) {
      this.deviceState = 6;
      return;
    } 
    if (paramBoolean1 && paramBoolean2 && paramBoolean3)
      this.deviceState = 7; 
  }
  
  public void setFirmwareVersion(String paramString) {
    this.firmwareVersion = paramString;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/mode/EFDeviceSwitch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */