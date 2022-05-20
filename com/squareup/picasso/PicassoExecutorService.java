package com.squareup.picasso;

import android.net.NetworkInfo;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class PicassoExecutorService extends ThreadPoolExecutor {
  private static final int DEFAULT_THREAD_COUNT = 3;
  
  PicassoExecutorService() {
    super(3, 3, 0L, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<Runnable>(), new Utils.PicassoThreadFactory());
  }
  
  private void setThreadCount(int paramInt) {
    setCorePoolSize(paramInt);
    setMaximumPoolSize(paramInt);
  }
  
  void adjustThreadCount(NetworkInfo paramNetworkInfo) {
    if (paramNetworkInfo == null || !paramNetworkInfo.isConnectedOrConnecting()) {
      setThreadCount(3);
      return;
    } 
    switch (paramNetworkInfo.getType()) {
      default:
        setThreadCount(3);
        return;
      case 1:
      case 6:
      case 9:
        setThreadCount(4);
        return;
      case 0:
        break;
    } 
    switch (paramNetworkInfo.getSubtype()) {
      default:
        setThreadCount(3);
        return;
      case 13:
      case 14:
      case 15:
        setThreadCount(3);
        return;
      case 3:
      case 4:
      case 5:
      case 6:
      case 12:
        setThreadCount(2);
        return;
      case 1:
      case 2:
        break;
    } 
    setThreadCount(1);
  }
  
  public Future<?> submit(Runnable paramRunnable) {
    paramRunnable = new PicassoFutureTask((BitmapHunter)paramRunnable);
    execute(paramRunnable);
    return (Future<?>)paramRunnable;
  }
  
  private static final class PicassoFutureTask extends FutureTask<BitmapHunter> implements Comparable<PicassoFutureTask> {
    private final BitmapHunter hunter;
    
    public PicassoFutureTask(BitmapHunter param1BitmapHunter) {
      super(param1BitmapHunter, null);
      this.hunter = param1BitmapHunter;
    }
    
    public int compareTo(PicassoFutureTask param1PicassoFutureTask) {
      Picasso.Priority priority1 = this.hunter.getPriority();
      Picasso.Priority priority2 = param1PicassoFutureTask.hunter.getPriority();
      return (priority1 == priority2) ? (this.hunter.sequence - param1PicassoFutureTask.hunter.sequence) : (priority2.ordinal() - priority1.ordinal());
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/squareup/picasso/PicassoExecutorService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */