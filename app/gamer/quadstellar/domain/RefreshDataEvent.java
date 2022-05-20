package app.gamer.quadstellar.domain;

public class RefreshDataEvent {
  private int inFanSpeed;
  
  private int outFanSpeed;
  
  private String temp;
  
  public RefreshDataEvent(int paramInt1, int paramInt2, String paramString) {
    this.inFanSpeed = paramInt1;
    this.outFanSpeed = paramInt2;
    this.temp = paramString;
  }
  
  public int getInFanSpeed() {
    return this.inFanSpeed;
  }
  
  public int getOutFanSpeed() {
    return this.outFanSpeed;
  }
  
  public String getTemp() {
    return this.temp;
  }
  
  public void setInFanSpeed(int paramInt) {
    this.inFanSpeed = paramInt;
  }
  
  public void setOutFanSpeed(int paramInt) {
    this.outFanSpeed = paramInt;
  }
  
  public void setTemp(String paramString) {
    this.temp = paramString;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/RefreshDataEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */