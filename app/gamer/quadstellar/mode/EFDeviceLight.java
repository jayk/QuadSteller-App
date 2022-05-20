package app.gamer.quadstellar.mode;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "EFDeviceLight")
public class EFDeviceLight extends EFDevice implements Cloneable {
  public static final int STATE_OFF = 0;
  
  public static final int STATE_ON = 1;
  
  private static final long serialVersionUID = -5175746057420287905L;
  
  @Column(name = "firmwareVersion")
  private String firmwareVersion;
  
  @Column(name = "lastColor")
  private int lastColor;
  
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  
  public int compareTo(Object paramObject) {
    return 0;
  }
  
  public String getFirmwareVersion() {
    return this.firmwareVersion;
  }
  
  public int getLastColor() {
    return this.lastColor;
  }
  
  public void setFirmwareVersion(String paramString) {
    this.firmwareVersion = paramString;
  }
  
  public void setLastColor(int paramInt) {
    this.lastColor = paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/mode/EFDeviceLight.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */