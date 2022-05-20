package app.gamer.quadstellar.observer;

import android.os.Bundle;

public class BridgeSubject extends Subject {
  public void switchStateChanege(Bundle paramBundle) {
    notifyObservers(new Object[] { paramBundle });
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/observer/BridgeSubject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */