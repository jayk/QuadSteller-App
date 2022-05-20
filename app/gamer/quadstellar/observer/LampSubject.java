package app.gamer.quadstellar.observer;

public class LampSubject extends Subject {
  public void lampStateChanege(int paramInt) {
    notifyObservers(new Object[] { Integer.valueOf(paramInt) });
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/observer/LampSubject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */