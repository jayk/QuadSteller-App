package app.gamer.quadstellar.mode;

import java.io.Serializable;

public class LinkageControl implements Serializable {
  private ControlDevice controlDevice;
  
  private String deviceMac;
  
  private String deviceName;
  
  private int deviceType;
  
  private boolean isSelect;
  
  public LinkageControl(ControlDevice paramControlDevice, boolean paramBoolean, int paramInt, String paramString1, String paramString2) {
    this.controlDevice = paramControlDevice;
    this.isSelect = paramBoolean;
    this.deviceType = paramInt;
    this.deviceMac = paramString1;
    this.deviceName = paramString2;
  }
  
  public ControlDevice getControlDevice() {
    return this.controlDevice;
  }
  
  public String getDeviceMac() {
    return this.deviceMac;
  }
  
  public String getDeviceName() {
    return this.deviceName;
  }
  
  public int getDeviceType() {
    return this.deviceType;
  }
  
  public boolean isSelect() {
    return this.isSelect;
  }
  
  public void setControlDevice(ControlDevice paramControlDevice) {
    this.controlDevice = paramControlDevice;
  }
  
  public void setDeviceMac(String paramString) {
    this.deviceMac = paramString;
  }
  
  public void setDeviceType(int paramInt) {
    this.deviceType = paramInt;
  }
  
  public void setSelect(boolean paramBoolean) {
    this.isSelect = paramBoolean;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/mode/LinkageControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */