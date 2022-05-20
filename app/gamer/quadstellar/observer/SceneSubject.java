package app.gamer.quadstellar.observer;

public class SceneSubject extends Subject {
  public void sceneNameChanege(String paramString) {
    notifyObservers(new Object[] { paramString });
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/observer/SceneSubject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */