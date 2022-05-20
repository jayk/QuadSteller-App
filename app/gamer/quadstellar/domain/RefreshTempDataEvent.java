package app.gamer.quadstellar.domain;

public class RefreshTempDataEvent {
  private String temp;
  
  public RefreshTempDataEvent(String paramString) {
    this.temp = paramString;
  }
  
  public String getTemp() {
    return this.temp;
  }
  
  public void setTemp(String paramString) {
    this.temp = paramString;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/RefreshTempDataEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */