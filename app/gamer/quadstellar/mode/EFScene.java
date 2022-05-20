package app.gamer.quadstellar.mode;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "EFScene")
public class EFScene extends EntityBase {
  public static final int DEFAULT_SCENEID = 0;
  
  private static final long serialVersionUID = -6041837688123987783L;
  
  @Column(name = "courtainsCount")
  private int courtainsCount;
  
  @Column(name = "sceneName")
  private String sceneName;
  
  @Column(name = "scenePic")
  private String scenePic;
  
  @Column(name = "teleCount")
  private int teleCount;
  
  @Column(name = "wifiLampCount")
  private int wifiLampCount;
  
  @Column(name = "wifiOutletCount")
  private int wifiOutletCount;
  
  @Column(name = "zigLampCount")
  private int zigLampCount;
  
  @Column(name = "zigOutletCount")
  private int zigOutletCount;
  
  @Column(name = "zigSwitchCount")
  private int zigSwitchCount;
  
  public static long getSerialversionuid() {
    return -6041837688123987783L;
  }
  
  public int compareTo(Object paramObject) {
    return 0;
  }
  
  public int getAllCount() {
    return this.zigLampCount + this.wifiLampCount + this.zigSwitchCount + this.zigOutletCount + this.wifiOutletCount + this.courtainsCount + this.teleCount;
  }
  
  public int getCourtainsCount() {
    return this.courtainsCount;
  }
  
  public String getSceneName() {
    return this.sceneName;
  }
  
  public String getScenePic() {
    return this.scenePic;
  }
  
  public int getTeleCount() {
    return this.teleCount;
  }
  
  public int getWifiLampCount() {
    return this.wifiLampCount;
  }
  
  public int getWifiOutletCount() {
    return this.wifiOutletCount;
  }
  
  public int getZigCount() {
    return this.zigLampCount + this.zigSwitchCount + this.zigOutletCount + this.courtainsCount;
  }
  
  public int getZigLampCount() {
    return this.zigLampCount;
  }
  
  public int getZigOutletCount() {
    return this.zigOutletCount;
  }
  
  public int getZigSwitchCount() {
    return this.zigSwitchCount;
  }
  
  public void setCourtainsCount(int paramInt) {
    this.courtainsCount = paramInt;
  }
  
  public void setSceneName(String paramString) {
    this.sceneName = paramString;
  }
  
  public void setScenePic(String paramString) {
    this.scenePic = paramString;
  }
  
  public void setTeleCount(int paramInt) {
    this.teleCount = paramInt;
  }
  
  public void setWifiLampCount(int paramInt) {
    this.wifiLampCount = paramInt;
  }
  
  public void setWifiOutletCount(int paramInt) {
    this.wifiOutletCount = paramInt;
  }
  
  public void setZigLampCount(int paramInt) {
    this.zigLampCount = paramInt;
  }
  
  public void setZigOutletCount(int paramInt) {
    this.zigOutletCount = paramInt;
  }
  
  public void setZigSwitchCount(int paramInt) {
    this.zigSwitchCount = paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/mode/EFScene.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */