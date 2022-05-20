package app.gamer.quadstellar.mode;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "EFDeviceCurtains")
public class EFDeviceCurtains extends EFDevice implements Cloneable {
  public static final int CHANGE_DIRECTION = 1;
  
  public static final byte C_CHANGE_DIRECTION = 17;
  
  public static final byte C_CLOSE = 12;
  
  public static final byte C_CTL_LED = -19;
  
  public static final byte C_CTL_SPEED = 21;
  
  public static final byte C_OPEN = 10;
  
  public static final byte C_PAUSE = 11;
  
  public static final byte C_QUE_LOCATION = 20;
  
  public static final byte C_SPE_LOCATION = 15;
  
  public static final int DEFAULT_DIRECTION = 0;
  
  public static final byte UP_BYTE_1 = -31;
  
  public static final byte UP_BYTE_4 = -17;
  
  private static final long serialVersionUID = -1718737604556435633L;
  
  @Column(name = "direction")
  private int direction;
  
  public static byte[] getDirectionBytes(int paramInt) {
    return new byte[] { -31, 17, (byte)(paramInt & 0xFF), -17 };
  }
  
  public static byte[] getLedCtlBytes(int paramInt) {
    return new byte[] { -31, -19, (byte)(paramInt & 0xFF), -17 };
  }
  
  public static byte[] getLocOpenBytes(int paramInt) {
    if (paramInt < 0) {
      boolean bool = false;
      return new byte[] { -31, 15, (byte)(bool & 0xFF), -17 };
    } 
    int i = paramInt;
    if (paramInt > 100)
      i = 100; 
    return new byte[] { -31, 15, (byte)(i & 0xFF), -17 };
  }
  
  public static byte[] getSpeedBytes(int paramInt) {
    return new byte[] { -31, 21, (byte)(paramInt & 0xFF), -17 };
  }
  
  public static byte[] getWholeCloseBytes() {
    return new byte[] { -31, 12, 0, -17 };
  }
  
  public static byte[] getWholeOpenBytes() {
    return new byte[] { -31, 10, 1, -17 };
  }
  
  public static byte[] queryLocation() {
    return new byte[] { -31, 20, 1, -17 };
  }
  
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  
  public int compareTo(Object paramObject) {
    return 0;
  }
  
  public int getDirection() {
    return this.direction;
  }
  
  public void setDirection(int paramInt) {
    this.direction = paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/mode/EFDeviceCurtains.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */