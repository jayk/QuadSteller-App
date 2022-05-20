package app.gamer.quadstellar.domain;

public class LinkageDeleteEvent {
  private String modeName;
  
  public LinkageDeleteEvent(String paramString) {
    this.modeName = paramString;
  }
  
  public String getModeName() {
    return this.modeName;
  }
  
  public void setModeName(String paramString) {
    this.modeName = paramString;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/LinkageDeleteEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */