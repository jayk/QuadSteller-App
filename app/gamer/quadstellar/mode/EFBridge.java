package app.gamer.quadstellar.mode;

import org.xutils.db.annotation.Table;

@Table(name = "EFBridge")
public class EFBridge extends EFDevice implements Cloneable {
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  
  public int compareTo(Object paramObject) {
    return 0;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/mode/EFBridge.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */