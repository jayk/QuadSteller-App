package app.gamer.quadstellar.domain;

import app.gamer.quadstellar.mode.ControlDevice;
import java.util.ArrayList;

public class AddDeviceEvent {
  private ArrayList<ControlDevice> mList;
  
  public AddDeviceEvent(ArrayList<ControlDevice> paramArrayList) {
    this.mList = paramArrayList;
  }
  
  public ArrayList<ControlDevice> getmList() {
    return this.mList;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/AddDeviceEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */