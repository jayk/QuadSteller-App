package app.gamer.quadstellar.domain;

import app.gamer.quadstellar.mode.LinkageControl;

public class AddLinkageEvent {
  private boolean isAddLinkage;
  
  private LinkageControl mLinkageControl;
  
  private int position;
  
  public AddLinkageEvent(LinkageControl paramLinkageControl, int paramInt, boolean paramBoolean) {
    this.mLinkageControl = paramLinkageControl;
    this.position = paramInt;
    this.isAddLinkage = paramBoolean;
  }
  
  public LinkageControl getLinkageControl() {
    return this.mLinkageControl;
  }
  
  public int getPosition() {
    return this.position;
  }
  
  public boolean isAddLinkage() {
    return this.isAddLinkage;
  }
  
  public void setAddLinkage(boolean paramBoolean) {
    this.isAddLinkage = paramBoolean;
  }
  
  public void setLinkageControl(LinkageControl paramLinkageControl) {
    this.mLinkageControl = paramLinkageControl;
  }
  
  public void setPosition(int paramInt) {
    this.position = paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/AddLinkageEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */