package app.gamer.quadstellar.mode;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "EFDeviceOutlet")
public class EFDeviceOutlet extends EFDevice {
  public static final int STATE_OFF = 0;
  
  public static final int STATE_ON = 1;
  
  private static final long serialVersionUID = 7752532925770258157L;
  
  @Column(name = "firmwareVersion")
  private String firmwareVersion;
  
  @Column(name = "lock")
  private int lock;
  
  public static long getSerialversionuid() {
    return 7752532925770258157L;
  }
  
  public String getFirmwareVersion() {
    return this.firmwareVersion;
  }
  
  public int getLock() {
    return this.lock;
  }
  
  public void setFirmwareVersion(String paramString) {
    this.firmwareVersion = paramString;
  }
  
  public void setLock(int paramInt) {
    this.lock = paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/mode/EFDeviceOutlet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */