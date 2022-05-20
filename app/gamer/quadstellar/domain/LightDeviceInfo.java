package app.gamer.quadstellar.domain;

public class LightDeviceInfo {
  private int colorB;
  
  private int colorG;
  
  private int colorR = 255;
  
  private Long id;
  
  private String macAdrass;
  
  private int sonBrightness = 100;
  
  private int sonLightColor = -65536;
  
  private int sonLightFineness;
  
  private int sonLightMode;
  
  private int sonLightNumber;
  
  private int sonLightSpeed = 50;
  
  private int sonLightState;
  
  public LightDeviceInfo() {}
  
  public LightDeviceInfo(Long paramLong, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10) {
    this.id = paramLong;
    this.macAdrass = paramString;
    this.sonLightNumber = paramInt1;
    this.sonLightState = paramInt2;
    this.sonLightMode = paramInt3;
    this.colorR = paramInt4;
    this.colorG = paramInt5;
    this.colorB = paramInt6;
    this.sonBrightness = paramInt7;
    this.sonLightSpeed = paramInt8;
    this.sonLightFineness = paramInt9;
    this.sonLightColor = paramInt10;
  }
  
  public int getColorB() {
    return this.colorB;
  }
  
  public int getColorG() {
    return this.colorG;
  }
  
  public int getColorR() {
    return this.colorR;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public String getMacAdrass() {
    return this.macAdrass;
  }
  
  public int getSonBrightness() {
    return this.sonBrightness;
  }
  
  public int getSonLightColor() {
    return this.sonLightColor;
  }
  
  public int getSonLightFineness() {
    return this.sonLightFineness;
  }
  
  public int getSonLightMode() {
    return this.sonLightMode;
  }
  
  public int getSonLightNumber() {
    return this.sonLightNumber;
  }
  
  public int getSonLightSpeed() {
    return this.sonLightSpeed;
  }
  
  public int getSonLightState() {
    return this.sonLightState;
  }
  
  public void setColorB(int paramInt) {
    this.colorB = paramInt;
  }
  
  public void setColorG(int paramInt) {
    this.colorG = paramInt;
  }
  
  public void setColorR(int paramInt) {
    this.colorR = paramInt;
  }
  
  public void setId(Long paramLong) {
    this.id = paramLong;
  }
  
  public void setMacAdrass(String paramString) {
    this.macAdrass = paramString;
  }
  
  public void setSonBrightness(int paramInt) {
    this.sonBrightness = paramInt;
  }
  
  public void setSonLightColor(int paramInt) {
    this.sonLightColor = paramInt;
  }
  
  public void setSonLightFineness(int paramInt) {
    this.sonLightFineness = paramInt;
  }
  
  public void setSonLightMode(int paramInt) {
    this.sonLightMode = paramInt;
  }
  
  public void setSonLightNumber(int paramInt) {
    this.sonLightNumber = paramInt;
  }
  
  public void setSonLightSpeed(int paramInt) {
    this.sonLightSpeed = paramInt;
  }
  
  public void setSonLightState(int paramInt) {
    this.sonLightState = paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/LightDeviceInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */