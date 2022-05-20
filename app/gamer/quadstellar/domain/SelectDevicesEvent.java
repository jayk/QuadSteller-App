package app.gamer.quadstellar.domain;

import app.gamer.quadstellar.mode.ControlDevice;
import java.util.ArrayList;

public class SelectDevicesEvent {
  private final ArrayList<ControlDevice> controlDevices;
  
  public SelectDevicesEvent(ArrayList<ControlDevice> paramArrayList) {
    this.controlDevices = paramArrayList;
  }
  
  public ArrayList<ControlDevice> getControlDevices() {
    return this.controlDevices;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/SelectDevicesEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */