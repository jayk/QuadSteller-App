package app.gamer.quadstellar.domain;

import app.gamer.quadstellar.mode.LinkageControl;

public class SelectDeviceEvent {
  private LinkageControl linkageControl;
  
  private int position;
  
  public SelectDeviceEvent(LinkageControl paramLinkageControl, int paramInt) {
    this.linkageControl = paramLinkageControl;
    this.position = paramInt;
  }
  
  public LinkageControl getLinkageControl() {
    return this.linkageControl;
  }
  
  public int getPosition() {
    return this.position;
  }
  
  public void setLinkageControl(LinkageControl paramLinkageControl) {
    this.linkageControl = paramLinkageControl;
  }
  
  public void setPosition(int paramInt) {
    this.position = paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/SelectDeviceEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */