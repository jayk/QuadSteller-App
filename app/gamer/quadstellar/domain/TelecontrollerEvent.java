package app.gamer.quadstellar.domain;

public class TelecontrollerEvent {
  private int mPosition;
  
  public TelecontrollerEvent(int paramInt) {
    this.mPosition = paramInt;
  }
  
  public int getPosition() {
    return this.mPosition;
  }
  
  public void setPosition(int paramInt) {
    this.mPosition = paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/TelecontrollerEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */