package app.gamer.quadstellar.mode;

import org.xutils.db.annotation.Column;

public class EFDevice extends EntityBase {
  public static final int STATUS_UNKNOWN = -1;
  
  public static final int TYPE_CAMERA = 16;
  
  public static final int TYPE_COMPOSITE = 8;
  
  public static final int TYPE_MUSIC = 15;
  
  public static final int TYPE_TELECONTROL = 6;
  
  public static final int TYPE_WIFI_BRIDGE = 12;
  
  public static final int TYPE_WIFI_LIGHT = 5;
  
  public static final int TYPE_WIFI_LIGHT_FAN = 17;
  
  public static final int TYPE_WIFI_LIGHT_FAN_TWO = 100;
  
  public static final int TYPE_WIFI_OUTLET = 4;
  
  public static final int TYPE_WIFI_POWER_STRIP = 9;
  
  public static final int TYPE_ZIG_BRIDGE = 13;
  
  public static final int TYPE_ZIG_CURTAINS = 7;
  
  public static final int TYPE_ZIG_DOORLOCK = 14;
  
  public static final int TYPE_ZIG_LIGHT = 1;
  
  public static final int TYPE_ZIG_OUTLET = 2;
  
  public static final int TYPE_ZIG_SWITCH = 3;
  
  public static final int TYPE_ZIG_SWITCH_DOUBLE = 10;
  
  public static final int TYPE_ZIG_SWITCH_SINGLE = 11;
  
  private static final long serialVersionUID = -7817904295404686067L;
  
  @Column(name = "deviceMac")
  protected String deviceMac;
  
  @Column(name = "deviceName")
  protected String deviceName;
  
  @Column(name = "deviceState")
  protected int deviceState;
  
  @Column(name = "deviceType")
  protected int deviceType;
  
  @Column(name = "parentMac")
  protected String parentMac;
  
  @Column(name = "sceneId")
  protected int sceneId;
  
  public static long getSerialversionuid() {
    return -7817904295404686067L;
  }
  
  public int compareTo(Object paramObject) {
    return 0;
  }
  
  public String getDeviceMac() {
    return this.deviceMac;
  }
  
  public String getDeviceName() {
    return this.deviceName;
  }
  
  public int getDeviceState() {
    return this.deviceState;
  }
  
  public int getDeviceType() {
    return this.deviceType;
  }
  
  public String getParentMac() {
    return this.parentMac;
  }
  
  public int getSceneId() {
    return this.sceneId;
  }
  
  public void setDeviceMac(String paramString) {
    this.deviceMac = paramString;
  }
  
  public void setDeviceName(String paramString) {
    this.deviceName = paramString;
  }
  
  public void setDeviceState(int paramInt) {
    this.deviceState = paramInt;
  }
  
  public void setDeviceType(int paramInt) {
    this.deviceType = paramInt;
  }
  
  public void setParentMac(String paramString) {
    this.parentMac = paramString;
  }
  
  public void setSceneId(int paramInt) {
    this.sceneId = paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/mode/EFDevice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */