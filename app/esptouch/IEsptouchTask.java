package app.esptouch;

import java.util.List;

public interface IEsptouchTask {
  IEsptouchResult executeForResult() throws RuntimeException;
  
  List<IEsptouchResult> executeForResults(int paramInt) throws RuntimeException;
  
  void interrupt();
  
  boolean isCancelled();
  
  void setEsptouchListener(IEsptouchListener paramIEsptouchListener);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/esptouch/IEsptouchTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */