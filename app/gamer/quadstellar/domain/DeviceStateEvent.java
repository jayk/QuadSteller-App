package app.gamer.quadstellar.domain;

public class DeviceStateEvent {
  private boolean isOnline;
  
  private int position;
  
  public int getPosition() {
    return this.position;
  }
  
  public boolean isOnline() {
    return this.isOnline;
  }
  
  public void setOnline(boolean paramBoolean) {
    this.isOnline = paramBoolean;
  }
  
  public void setPosition(int paramInt) {
    this.position = paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/DeviceStateEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */