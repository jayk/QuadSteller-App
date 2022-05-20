package app.gamer.quadstellar.domain;

import app.gamer.quadstellar.mode.ControlDevice;
import java.util.ArrayList;

public class LinkageEvent {
  private ArrayList<ControlDevice> controlDevicesList;
  
  private String modeName;
  
  public LinkageEvent(ArrayList<ControlDevice> paramArrayList, String paramString) {
    this.controlDevicesList = paramArrayList;
    this.modeName = paramString;
  }
  
  public ArrayList<ControlDevice> getControlDevicesList() {
    return this.controlDevicesList;
  }
  
  public String getModeName() {
    return this.modeName;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/LinkageEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */