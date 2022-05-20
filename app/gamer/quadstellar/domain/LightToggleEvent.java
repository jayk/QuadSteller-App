package app.gamer.quadstellar.domain;

public class LightToggleEvent {
  private boolean isCheked;
  
  public LightToggleEvent(boolean paramBoolean) {
    this.isCheked = paramBoolean;
  }
  
  public boolean isCheked() {
    return this.isCheked;
  }
  
  public void setCheked(boolean paramBoolean) {
    this.isCheked = paramBoolean;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/LightToggleEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */