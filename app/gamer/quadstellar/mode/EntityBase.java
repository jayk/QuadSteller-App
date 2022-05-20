package app.gamer.quadstellar.mode;

import java.io.Serializable;
import org.xutils.db.annotation.Column;

public abstract class EntityBase implements Serializable, Comparable<Object> {
  private static final long serialVersionUID = 2858234816204724092L;
  
  @Column(isId = true, name = "id")
  protected int id;
  
  public int getId() {
    return this.id;
  }
  
  public void setId(int paramInt) {
    this.id = paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/mode/EntityBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */