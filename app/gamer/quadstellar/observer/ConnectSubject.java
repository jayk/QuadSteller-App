package app.gamer.quadstellar.observer;

import app.gamer.quadstellar.domain.FatherDeviceInfo;

public class ConnectSubject extends SceneSubject {
  public void connectDeviceChange(FatherDeviceInfo paramFatherDeviceInfo) {
    notifyObservers(new Object[] { paramFatherDeviceInfo });
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/observer/ConnectSubject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */