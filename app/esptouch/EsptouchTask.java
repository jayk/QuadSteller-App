package app.esptouch;

import android.content.Context;
import android.util.Log;
import app.esptouch.task.EsptouchTaskParameter;
import app.esptouch.task.IEsptouchTaskParameter;
import app.esptouch.task.__EsptouchTask;
import java.util.List;

public class EsptouchTask implements IEsptouchTask {
  public __EsptouchTask _mEsptouchTask;
  
  private IEsptouchTaskParameter _mParameter = (IEsptouchTaskParameter)new EsptouchTaskParameter();
  
  public EsptouchTask(String paramString1, String paramString2, String paramString3, boolean paramBoolean, int paramInt, Context paramContext) {
    this._mParameter.setWaitUdpTotalMillisecond(paramInt);
    this._mEsptouchTask = new __EsptouchTask(paramString1, paramString2, paramString3, paramContext, this._mParameter, paramBoolean);
  }
  
  public EsptouchTask(String paramString1, String paramString2, String paramString3, boolean paramBoolean, Context paramContext) {
    this._mEsptouchTask = new __EsptouchTask(paramString1, paramString2, paramString3, paramContext, this._mParameter, paramBoolean);
  }
  
  public IEsptouchResult executeForResult() throws RuntimeException {
    return this._mEsptouchTask.executeForResult();
  }
  
  public List<IEsptouchResult> executeForResults(int paramInt) throws RuntimeException {
    int i = paramInt;
    if (paramInt <= 0)
      i = Integer.MAX_VALUE; 
    return this._mEsptouchTask.executeForResults(i);
  }
  
  public void interrupt() {
    this._mEsptouchTask.interrupt();
    Log.d("EsptouchTask", "执行lma==============");
  }
  
  public boolean isCancelled() {
    return this._mEsptouchTask.isCancelled();
  }
  
  public void setEsptouchListener(IEsptouchListener paramIEsptouchListener) {
    this._mEsptouchTask.setEsptouchListener(paramIEsptouchListener);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/esptouch/EsptouchTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */