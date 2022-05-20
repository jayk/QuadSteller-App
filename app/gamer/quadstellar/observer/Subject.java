package app.gamer.quadstellar.observer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Subject {
  private List<Observer> observers = new ArrayList<Observer>();
  
  public void attach(Observer paramObserver) {
    this.observers.add(paramObserver);
  }
  
  public void detach(Observer paramObserver) {
    this.observers.remove(paramObserver);
  }
  
  protected void notifyObservers(Object... paramVarArgs) {
    Iterator<Observer> iterator = this.observers.iterator();
    while (iterator.hasNext())
      ((Observer)iterator.next()).update(this, paramVarArgs); 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/observer/Subject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */