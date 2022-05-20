package app.gamer.quadstellar.domain;

public class LinkageEditEvent {
  private String modeName;
  
  private int position;
  
  public LinkageEditEvent(String paramString, int paramInt) {
    this.modeName = paramString;
    this.position = paramInt;
  }
  
  public String getModeName() {
    return this.modeName;
  }
  
  public int getPosition() {
    return this.position;
  }
  
  public void setModeName(String paramString) {
    this.modeName = paramString;
  }
  
  public void setPosition(int paramInt) {
    this.position = paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/LinkageEditEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */