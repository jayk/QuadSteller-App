package org.xutils.common;

import org.xutils.common.task.AbsTask;

public interface TaskController {
  void autoPost(Runnable paramRunnable);
  
  void post(Runnable paramRunnable);
  
  void postDelayed(Runnable paramRunnable, long paramLong);
  
  void removeCallbacks(Runnable paramRunnable);
  
  void run(Runnable paramRunnable);
  
  <T> AbsTask<T> start(AbsTask<T> paramAbsTask);
  
  <T> T startSync(AbsTask<T> paramAbsTask) throws Throwable;
  
  <T extends AbsTask<?>> Callback.Cancelable startTasks(Callback.GroupCallback<T> paramGroupCallback, T... paramVarArgs);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/common/TaskController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */