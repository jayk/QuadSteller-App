package app.gamer.quadstellar.domain;

import app.gamer.quadstellar.App;

public class DeviceInfo {
  private int colorB = 0;
  
  private int colorG = 0;
  
  private int colorLuminance = 100;
  
  private int colorMode = 0;
  
  private int colorR = 255;
  
  private int colorSpeed = 50;
  
  private String deviceIP;
  
  private String deviceVersion;
  
  private int fanLowSpeed;
  
  private int fanMaxSpeed;
  
  private int fanMode = 1;
  
  private int fanNomalSpeed;
  
  private Long id;
  
  private boolean isOnline;
  
  private String macDrass;
  
  private String name;
  
  private int permission = 2;
  
  private int toggleState = 1;
  
  private String uuid = App.uniqueId;
  
  public DeviceInfo() {}
  
  public DeviceInfo(Long paramLong, int paramInt1, String paramString1, String paramString2, String paramString3, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, String paramString4, String paramString5, boolean paramBoolean) {
    this.id = paramLong;
    this.permission = paramInt1;
    this.macDrass = paramString1;
    this.deviceVersion = paramString2;
    this.uuid = paramString3;
    this.toggleState = paramInt2;
    this.colorMode = paramInt3;
    this.colorR = paramInt4;
    this.colorG = paramInt5;
    this.colorB = paramInt6;
    this.colorLuminance = paramInt7;
    this.colorSpeed = paramInt8;
    this.fanMode = paramInt9;
    this.fanLowSpeed = paramInt10;
    this.fanMaxSpeed = paramInt11;
    this.fanNomalSpeed = paramInt12;
    this.deviceIP = paramString4;
    this.name = paramString5;
    this.isOnline = paramBoolean;
  }
  
  public byte[] getBytedate() {
    return new byte[] { (byte)this.toggleState, (byte)this.colorMode, (byte)this.colorR, (byte)this.colorG, (byte)this.colorB, (byte)this.colorLuminance, (byte)this.colorSpeed };
  }
  
  public int getColorB() {
    return this.colorB;
  }
  
  public int getColorG() {
    return this.colorG;
  }
  
  public int getColorLuminance() {
    return this.colorLuminance;
  }
  
  public int getColorMode() {
    return this.colorMode;
  }
  
  public int getColorR() {
    return this.colorR;
  }
  
  public int getColorSpeed() {
    return this.colorSpeed;
  }
  
  public String getDeviceIP() {
    return this.deviceIP;
  }
  
  public String getDeviceVersion() {
    return this.deviceVersion;
  }
  
  public byte[] getFanBytedate() {
    byte[] arrayOfByte = new byte[4];
    arrayOfByte[0] = (byte)(byte)this.fanMode;
    arrayOfByte[1] = (byte)(byte)this.fanLowSpeed;
    arrayOfByte[2] = (byte)(byte)this.fanMaxSpeed;
    return arrayOfByte;
  }
  
  public int getFanLowSpeed() {
    return this.fanLowSpeed;
  }
  
  public int getFanMaxSpeed() {
    return this.fanMaxSpeed;
  }
  
  public int getFanMode() {
    return this.fanMode;
  }
  
  public int getFanNomalSpeed() {
    return this.fanNomalSpeed;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public boolean getIsOnline() {
    return this.isOnline;
  }
  
  public String getMacDrass() {
    return this.macDrass;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getPermission() {
    return this.permission;
  }
  
  public int getToggleState() {
    return this.toggleState;
  }
  
  public String getUuid() {
    return this.uuid;
  }
  
  public void setColorB(int paramInt) {
    this.colorB = paramInt;
  }
  
  public void setColorG(int paramInt) {
    this.colorG = paramInt;
  }
  
  public void setColorLuminance(int paramInt) {
    this.colorLuminance = paramInt;
  }
  
  public void setColorMode(int paramInt) {
    this.colorMode = paramInt;
  }
  
  public void setColorR(int paramInt) {
    this.colorR = paramInt;
  }
  
  public void setColorSpeed(int paramInt) {
    this.colorSpeed = paramInt;
  }
  
  public void setDeviceIP(String paramString) {
    this.deviceIP = paramString;
  }
  
  public void setDeviceVersion(String paramString) {
    this.deviceVersion = paramString;
  }
  
  public void setFanLowSpeed(int paramInt) {
    this.fanLowSpeed = paramInt;
  }
  
  public void setFanMaxSpeed(int paramInt) {
    this.fanMaxSpeed = paramInt;
  }
  
  public void setFanMode(int paramInt) {
    this.fanMode = paramInt;
  }
  
  public void setFanNomalSpeed(int paramInt) {
    this.fanNomalSpeed = paramInt;
  }
  
  public void setId(Long paramLong) {
    this.id = paramLong;
  }
  
  public void setIsOnline(boolean paramBoolean) {
    this.isOnline = paramBoolean;
  }
  
  public void setMacDrass(String paramString) {
    this.macDrass = paramString;
  }
  
  public void setName(String paramString) {
    this.name = paramString;
  }
  
  public void setPermission(int paramInt) {
    this.permission = paramInt;
  }
  
  public void setToggleState(int paramInt) {
    this.toggleState = paramInt;
  }
  
  public void setUuid(String paramString) {
    this.uuid = paramString;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/DeviceInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */