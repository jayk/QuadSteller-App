package app.gamer.quadstellar.mode;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "EFDeviceYaoKong")
public class EFDeviceYaoKong extends EFDevice implements Cloneable {
  @Column(name = "position1")
  private int position1;
  
  @Column(name = "yaokonglist")
  private String yaokongList;
  
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  
  public int compareTo(Object paramObject) {
    return 0;
  }
  
  public int getPosition1() {
    return this.position1;
  }
  
  public String getYaokongList() {
    return this.yaokongList;
  }
  
  public void setPosition1(int paramInt) {
    this.position1 = paramInt;
  }
  
  public void setYaokongList(String paramString) {
    this.yaokongList = paramString;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/mode/EFDeviceYaoKong.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */