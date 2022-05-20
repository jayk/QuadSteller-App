package app.gamer.quadstellar.domain;

import app.gamer.quadstellar.ui.widget.DragGridView;

public class ContrllerEvent {
  private DragGridView mMDevices;
  
  private int mPosition;
  
  public ContrllerEvent(DragGridView paramDragGridView, int paramInt) {
    this.mMDevices = paramDragGridView;
    this.mPosition = paramInt;
  }
  
  public DragGridView getMDevices() {
    return this.mMDevices;
  }
  
  public int getPosition() {
    return this.mPosition;
  }
  
  public void setMDevices(DragGridView paramDragGridView) {
    this.mMDevices = paramDragGridView;
  }
  
  public void setPosition(int paramInt) {
    this.mPosition = paramInt;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/ContrllerEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */