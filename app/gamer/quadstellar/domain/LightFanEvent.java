package app.gamer.quadstellar.domain;

public class LightFanEvent {
  private byte lowSpeed;
  
  private byte maxSpeed;
  
  public byte getLowSpeed() {
    return this.lowSpeed;
  }
  
  public byte getMaxSpeed() {
    return this.maxSpeed;
  }
  
  public void setLowSpeed(byte paramByte) {
    this.lowSpeed = (byte)paramByte;
  }
  
  public void setMaxSpeed(byte paramByte) {
    this.maxSpeed = (byte)paramByte;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/LightFanEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */