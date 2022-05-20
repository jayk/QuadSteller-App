package app.esptouch.task;

import app.esptouch.IEsptouchListener;
import app.esptouch.IEsptouchResult;
import java.util.List;

public interface __IEsptouchTask {
  public static final boolean DEBUG = true;
  
  IEsptouchResult executeForResult() throws RuntimeException;
  
  List<IEsptouchResult> executeForResults(int paramInt) throws RuntimeException;
  
  void interrupt();
  
  boolean isCancelled();
  
  void setEsptouchListener(IEsptouchListener paramIEsptouchListener);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/esptouch/task/__IEsptouchTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */