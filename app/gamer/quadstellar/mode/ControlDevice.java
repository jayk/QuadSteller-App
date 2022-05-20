package app.gamer.quadstellar.mode;

import app.gamer.quadstellar.App;
import app.gamer.quadstellar.domain.DeviceInfo;
import io.xlink.wifi.sdk.XDevice;
import java.io.Serializable;

public class ControlDevice implements Serializable {
  public static final int COMPOSITE_DEVICE = 4;
  
  public static final int NORMAL_DEVICE = 0;
  
  public static final int TELECONTROLLER_DEVICE = 3;
  
  public static final int WIFI_BRIDGE = 6;
  
  public static final int WIFI_DEVICE_LIGHT = 1;
  
  public static final int WIFI_DEVICE_OUTLET = 2;
  
  public static final int WIFI_POWER_STRIP = 5;
  
  private static final long serialVersionUID = 1L;
  
  private boolean isOnline = true;
  
  private DeviceInfo mLights;
  
  private String password;
  
  private int state = -1;
  
  private boolean switch_;
  
  private short th;
  
  private int type;
  
  private int wind;
  
  private XDevice xDevice;
  
  public ControlDevice(XDevice paramXDevice) {
    this.xDevice = paramXDevice;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof ControlDevice) {
      paramObject = paramObject;
      return this.xDevice.equals(paramObject.getXDevice());
    } 
    return (paramObject instanceof XDevice) ? this.xDevice.equals(paramObject) : super.equals(paramObject);
  }
  
  public String getMacAddress() {
    return (this.xDevice == null) ? "" : this.xDevice.getMacAddress();
  }
  
  public String getName() {
    return this.xDevice.getDeviceName();
  }
  
  public String getPassword() {
    return this.password;
  }
  
  public int getState() {
    return this.state;
  }
  
  public short getTh() {
    return this.th;
  }
  
  public int getType() {
    return this.type;
  }
  
  public CharSequence getTypeText() {
    StringBuilder stringBuilder = new StringBuilder("");
    switch (this.type) {
      default:
        stringBuilder.append("(");
        stringBuilder.append(getMacAddress());
        stringBuilder.append(")");
        return stringBuilder.toString();
      case 0:
        stringBuilder.append(App.getAppContext().getString(2131296542));
      case 1:
        stringBuilder.append(App.getAppContext().getString(2131297583));
      case 2:
        stringBuilder.append(App.getAppContext().getString(2131297584));
      case 5:
        stringBuilder.append(App.getAppContext().getString(2131297585));
      case 3:
        stringBuilder.append(App.getAppContext().getString(2131297483));
      case 4:
        stringBuilder.append(App.getAppContext().getString(2131296503));
      case 6:
        break;
    } 
    stringBuilder.append(App.getAppContext().getString(2131297579));
  }
  
  public int getWind() {
    return this.wind;
  }
  
  public XDevice getXDevice() {
    return this.xDevice;
  }
  
  public DeviceInfo getmLights() {
    return this.mLights;
  }
  
  public boolean isConnect() {
    return !(this.xDevice.getDevcieConnectStates() == -1);
  }
  
  public boolean isOnline() {
    return this.isOnline;
  }
  
  public boolean isSwitch_() {
    return this.switch_;
  }
  
  public void setOnline(boolean paramBoolean) {
    this.isOnline = paramBoolean;
  }
  
  public void setPassword(String paramString) {
    this.password = paramString;
  }
  
  public void setState(int paramInt) {
    this.state = paramInt;
  }
  
  public void setSwitch_(boolean paramBoolean) {
    this.switch_ = paramBoolean;
  }
  
  public void setTh(short paramShort) {
    this.th = (short)paramShort;
  }
  
  public void setType(int paramInt) {
    this.type = paramInt;
  }
  
  public void setWind(int paramInt) {
    this.wind = paramInt;
  }
  
  public void setmLights(DeviceInfo paramDeviceInfo) {
    this.mLights = paramDeviceInfo;
  }
  
  public void setxDevice(XDevice paramXDevice) {
    this.xDevice = paramXDevice;
  }
  
  public String toString() {
    return this.xDevice.toString() + " pwd:" + this.password;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/mode/ControlDevice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */