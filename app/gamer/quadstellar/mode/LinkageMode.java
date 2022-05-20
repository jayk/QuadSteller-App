package app.gamer.quadstellar.mode;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "LinkageMode")
public class LinkageMode extends EntityBase {
  @Column(name = "LinkageDeviceList")
  protected String deviceList;
  
  @Column(name = "modeName")
  protected String modeName;
  
  @Column(name = "picPath")
  protected String picPath;
  
  public int compareTo(Object paramObject) {
    return 0;
  }
  
  public String getDeviceList() {
    return this.deviceList;
  }
  
  public String getModeName() {
    return this.modeName;
  }
  
  public String getPicPath() {
    return this.picPath;
  }
  
  public void setDeviceList(String paramString) {
    this.deviceList = paramString;
  }
  
  public void setModeName(String paramString) {
    this.modeName = paramString;
  }
  
  public void setPicPath(String paramString) {
    this.picPath = paramString;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/mode/LinkageMode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */