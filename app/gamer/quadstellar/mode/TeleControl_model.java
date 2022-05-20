package app.gamer.quadstellar.mode;

import java.util.List;

public class TeleControl_model {
  private List<ControlDevice> mDevicesList;
  
  private List<String> mMacList;
  
  public List<ControlDevice> getmDevicesList() {
    return this.mDevicesList;
  }
  
  public List<String> getmMacList() {
    return this.mMacList;
  }
  
  public void setmDevicesList(List<ControlDevice> paramList) {
    this.mDevicesList = paramList;
  }
  
  public void setmMacList(List<String> paramList) {
    this.mMacList = paramList;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/mode/TeleControl_model.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */