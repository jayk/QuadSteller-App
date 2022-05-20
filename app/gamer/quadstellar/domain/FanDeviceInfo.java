package app.gamer.quadstellar.domain;

public class FanDeviceInfo {
  private Long id;
  
  private String macAdrass;
  
  private int realitySonFanSpeed;
  
  private int setSonFanSpeed = 100;
  
  private int sonFanMode = 1;
  
  private int sonFanNumber;
  
  public FanDeviceInfo() {}
  
  public FanDeviceInfo(Long paramLong, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.id = paramLong;
    this.macAdrass = paramString;
    this.sonFanNumber = paramInt1;
    this.sonFanMode = paramInt2;
    this.setSonFanSpeed = paramInt3;
    this.realitySonFanSpeed = paramInt4;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public String getMacAdrass() {
    return this.macAdrass;
  }
  
  public int getRealitySonFanSpeed() {
    return this.realitySonFanSpeed;
  }
  
  public int getSetSonFanSpeed() {
    return this.setSonFanSpeed;
  }
  
  public int getSonFanMode() {
    return this.sonFanMode;
  }
  
  public int getSonFanNumber() {
    return this.sonFanNumber;
  }
  
  public void setId(Long paramLong) {
    this.id = paramLong;
  }
  
  public void setMacAdrass(String paramString) {
    this.macAdrass = paramString;
  }
  
  public void setRealitySonFanSpeed(int paramInt) {
    this.realitySonFanSpeed = paramInt;
  }
  
  public void setSetSonFanSpeed(int paramInt) {
    this.setSonFanSpeed = paramInt;
  }
  
  public void setSonFanMode(int paramInt) {
    this.sonFanMode = paramInt;
  }
  
  public void setSonFanNumber(int paramInt) {
    this.sonFanNumber = paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/FanDeviceInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */