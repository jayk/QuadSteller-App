package app.gamer.quadstellar.domain;

public class FatherDeviceInfo {
  private int colorB;
  
  private int colorBrightness = 100;
  
  private int colorFineness = 100;
  
  private int colorG;
  
  private int colorR = 255;
  
  private int colorSpeed = 50;
  
  private int controlTag;
  
  private String deviceIP;
  
  private int deviceLen;
  
  private String deviceVersion;
  
  private String doorCloseTemp;
  
  private int doorOpenState;
  
  private String doorOpenTemp;
  
  private int doorToggleState;
  
  private int doorType;
  
  private int fanCount;
  
  private int fanMode = 1;
  
  private int fanSonNumer;
  
  private int fanSpeed;
  
  private Long id;
  
  private boolean isOnline;
  
  private int lightCount;
  
  private int lightSonNumber;
  
  private int lightSonToggle;
  
  private int ligtMode;
  
  private String macAdrass;
  
  private String name;
  
  private int partLightBrigtness = 100;
  
  private int partLightMode;
  
  private int partLightSpeed = 50;
  
  private String temp;
  
  private int toggleState = 1;
  
  private String uuid;
  
  public FatherDeviceInfo() {}
  
  public FatherDeviceInfo(Long paramLong, String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString4, String paramString5, int paramInt6, String paramString6, boolean paramBoolean, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, int paramInt13, int paramInt14, int paramInt15, int paramInt16, int paramInt17, int paramInt18, String paramString7, String paramString8, int paramInt19, int paramInt20, int paramInt21, int paramInt22, int paramInt23) {
    this.id = paramLong;
    this.macAdrass = paramString1;
    this.deviceVersion = paramString2;
    this.temp = paramString3;
    this.doorOpenState = paramInt1;
    this.fanCount = paramInt2;
    this.lightCount = paramInt3;
    this.toggleState = paramInt4;
    this.ligtMode = paramInt5;
    this.deviceIP = paramString4;
    this.uuid = paramString5;
    this.deviceLen = paramInt6;
    this.name = paramString6;
    this.isOnline = paramBoolean;
    this.colorR = paramInt7;
    this.colorG = paramInt8;
    this.colorB = paramInt9;
    this.colorSpeed = paramInt10;
    this.colorBrightness = paramInt11;
    this.colorFineness = paramInt12;
    this.lightSonNumber = paramInt13;
    this.lightSonToggle = paramInt14;
    this.fanSonNumer = paramInt15;
    this.fanMode = paramInt16;
    this.doorType = paramInt17;
    this.doorToggleState = paramInt18;
    this.doorOpenTemp = paramString7;
    this.doorCloseTemp = paramString8;
    this.controlTag = paramInt19;
    this.fanSpeed = paramInt20;
    this.partLightMode = paramInt21;
    this.partLightSpeed = paramInt22;
    this.partLightBrigtness = paramInt23;
  }
  
  public int getColorB() {
    return this.colorB;
  }
  
  public int getColorBrightness() {
    return this.colorBrightness;
  }
  
  public int getColorFineness() {
    return this.colorFineness;
  }
  
  public int getColorG() {
    return this.colorG;
  }
  
  public int getColorR() {
    return this.colorR;
  }
  
  public int getColorSpeed() {
    return this.colorSpeed;
  }
  
  public int getControlTag() {
    return this.controlTag;
  }
  
  public String getDeviceIP() {
    return this.deviceIP;
  }
  
  public int getDeviceLen() {
    return this.deviceLen;
  }
  
  public String getDeviceVersion() {
    return this.deviceVersion;
  }
  
  public String getDoorCloseTemp() {
    return this.doorCloseTemp;
  }
  
  public int getDoorOpenState() {
    return this.doorOpenState;
  }
  
  public String getDoorOpenTemp() {
    return this.doorOpenTemp;
  }
  
  public int getDoorToggleState() {
    return this.doorToggleState;
  }
  
  public int getDoorType() {
    return this.doorType;
  }
  
  public int getFanCount() {
    return this.fanCount;
  }
  
  public int getFanMode() {
    return this.fanMode;
  }
  
  public int getFanSonNumer() {
    return this.fanSonNumer;
  }
  
  public int getFanSpeed() {
    return this.fanSpeed;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public boolean getIsOnline() {
    return this.isOnline;
  }
  
  public int getLightCount() {
    return this.lightCount;
  }
  
  public int getLightSonNumber() {
    return this.lightSonNumber;
  }
  
  public int getLightSonToggle() {
    return this.lightSonToggle;
  }
  
  public int getLigtMode() {
    return this.ligtMode;
  }
  
  public String getMacAdrass() {
    return this.macAdrass;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getPartLightBrigtness() {
    return this.partLightBrigtness;
  }
  
  public int getPartLightMode() {
    return this.partLightMode;
  }
  
  public int getPartLightSpeed() {
    return this.partLightSpeed;
  }
  
  public String getTemp() {
    return this.temp;
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
  
  public void setColorBrightness(int paramInt) {
    this.colorBrightness = paramInt;
  }
  
  public void setColorFineness(int paramInt) {
    this.colorFineness = paramInt;
  }
  
  public void setColorG(int paramInt) {
    this.colorG = paramInt;
  }
  
  public void setColorR(int paramInt) {
    this.colorR = paramInt;
  }
  
  public void setColorSpeed(int paramInt) {
    this.colorSpeed = paramInt;
  }
  
  public void setControlTag(int paramInt) {
    this.controlTag = paramInt;
  }
  
  public void setDeviceIP(String paramString) {
    this.deviceIP = paramString;
  }
  
  public void setDeviceLen(int paramInt) {
    this.deviceLen = paramInt;
  }
  
  public void setDeviceVersion(String paramString) {
    this.deviceVersion = paramString;
  }
  
  public void setDoorCloseTemp(String paramString) {
    this.doorCloseTemp = paramString;
  }
  
  public void setDoorOpenState(int paramInt) {
    this.doorOpenState = paramInt;
  }
  
  public void setDoorOpenTemp(String paramString) {
    this.doorOpenTemp = paramString;
  }
  
  public void setDoorToggleState(int paramInt) {
    this.doorToggleState = paramInt;
  }
  
  public void setDoorType(int paramInt) {
    this.doorType = paramInt;
  }
  
  public void setFanCount(int paramInt) {
    this.fanCount = paramInt;
  }
  
  public void setFanMode(int paramInt) {
    this.fanMode = paramInt;
  }
  
  public void setFanSonNumer(int paramInt) {
    this.fanSonNumer = paramInt;
  }
  
  public void setFanSpeed(int paramInt) {
    this.fanSpeed = paramInt;
  }
  
  public void setId(Long paramLong) {
    this.id = paramLong;
  }
  
  public void setIsOnline(boolean paramBoolean) {
    this.isOnline = paramBoolean;
  }
  
  public void setLightCount(int paramInt) {
    this.lightCount = paramInt;
  }
  
  public void setLightSonNumber(int paramInt) {
    this.lightSonNumber = paramInt;
  }
  
  public void setLightSonToggle(int paramInt) {
    this.lightSonToggle = paramInt;
  }
  
  public void setLigtMode(int paramInt) {
    this.ligtMode = paramInt;
  }
  
  public void setMacAdrass(String paramString) {
    this.macAdrass = paramString;
  }
  
  public void setName(String paramString) {
    this.name = paramString;
  }
  
  public void setPartLightBrigtness(int paramInt) {
    this.partLightBrigtness = paramInt;
  }
  
  public void setPartLightMode(int paramInt) {
    this.partLightMode = paramInt;
  }
  
  public void setPartLightSpeed(int paramInt) {
    this.partLightSpeed = paramInt;
  }
  
  public void setTemp(String paramString) {
    this.temp = paramString;
  }
  
  public void setToggleState(int paramInt) {
    this.toggleState = paramInt;
  }
  
  public void setUuid(String paramString) {
    this.uuid = paramString;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/FatherDeviceInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */