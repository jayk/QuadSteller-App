package app.gamer.quadstellar.domain;

public class ControlRenameEvent {
  String name;
  
  int positon;
  
  public String getName() {
    return this.name;
  }
  
  public int getPositon() {
    return this.positon;
  }
  
  public void setName(String paramString) {
    this.name = paramString;
  }
  
  public void setPositon(int paramInt) {
    this.positon = paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/ControlRenameEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */