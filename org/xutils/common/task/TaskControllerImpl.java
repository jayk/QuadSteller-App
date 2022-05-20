package org.xutils.common.task;

import android.os.Looper;
import java.util.concurrent.atomic.AtomicInteger;
import org.xutils.common.Callback;
import org.xutils.common.TaskController;
import org.xutils.common.util.LogUtil;

public final class TaskControllerImpl implements TaskController {
  private static volatile TaskController instance;
  
  public static void registerInstance() {
    // Byte code:
    //   0: getstatic org/xutils/common/task/TaskControllerImpl.instance : Lorg/xutils/common/TaskController;
    //   3: ifnonnull -> 30
    //   6: ldc org/xutils/common/TaskController
    //   8: monitorenter
    //   9: getstatic org/xutils/common/task/TaskControllerImpl.instance : Lorg/xutils/common/TaskController;
    //   12: ifnonnull -> 27
    //   15: new org/xutils/common/task/TaskControllerImpl
    //   18: astore_0
    //   19: aload_0
    //   20: invokespecial <init> : ()V
    //   23: aload_0
    //   24: putstatic org/xutils/common/task/TaskControllerImpl.instance : Lorg/xutils/common/TaskController;
    //   27: ldc org/xutils/common/TaskController
    //   29: monitorexit
    //   30: getstatic org/xutils/common/task/TaskControllerImpl.instance : Lorg/xutils/common/TaskController;
    //   33: invokestatic setTaskController : (Lorg/xutils/common/TaskController;)V
    //   36: return
    //   37: astore_0
    //   38: ldc org/xutils/common/TaskController
    //   40: monitorexit
    //   41: aload_0
    //   42: athrow
    // Exception table:
    //   from	to	target	type
    //   9	27	37	finally
    //   27	30	37	finally
    //   38	41	37	finally
  }
  
  public void autoPost(Runnable paramRunnable) {
    if (paramRunnable != null) {
      if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
        paramRunnable.run();
        return;
      } 
      TaskProxy.sHandler.post(paramRunnable);
    } 
  }
  
  public void post(Runnable paramRunnable) {
    if (paramRunnable != null)
      TaskProxy.sHandler.post(paramRunnable); 
  }
  
  public void postDelayed(Runnable paramRunnable, long paramLong) {
    if (paramRunnable != null)
      TaskProxy.sHandler.postDelayed(paramRunnable, paramLong); 
  }
  
  public void removeCallbacks(Runnable paramRunnable) {
    TaskProxy.sHandler.removeCallbacks(paramRunnable);
  }
  
  public void run(Runnable paramRunnable) {
    if (!TaskProxy.sDefaultExecutor.isBusy()) {
      TaskProxy.sDefaultExecutor.execute(paramRunnable);
      return;
    } 
    (new Thread(paramRunnable)).start();
  }
  
  public <T> AbsTask<T> start(AbsTask<T> paramAbsTask) {
    if (paramAbsTask instanceof TaskProxy) {
      paramAbsTask = paramAbsTask;
    } else {
      paramAbsTask = new TaskProxy<T>(paramAbsTask);
    } 
    try {
      paramAbsTask.doBackground();
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
    } 
    return paramAbsTask;
  }
  
  public <T> T startSync(AbsTask<T> paramAbsTask) throws Throwable {
    T t = null;
    null = t;
    try {
      paramAbsTask.onWaiting();
      null = t;
      paramAbsTask.onStarted();
      null = t;
      t = paramAbsTask.doBackground();
      T t1 = t;
      paramAbsTask.onSuccess(t);
      return t;
    } catch (org.xutils.common.Callback.CancelledException cancelledException) {
      paramAbsTask.onCancelled(cancelledException);
      return null;
    } catch (Throwable throwable) {
      paramAbsTask.onError(throwable, false);
      throw throwable;
    } finally {
      paramAbsTask.onFinished();
    } 
  }
  
  public <T extends AbsTask<?>> Callback.Cancelable startTasks(final Callback.GroupCallback<T> groupCallback, T... tasks) {
    if (tasks == null)
      throw new IllegalArgumentException("task must not be null"); 
    final Runnable callIfOnAllFinished = new Runnable() {
        private final AtomicInteger count = new AtomicInteger(0);
        
        private final int total = tasks.length;
        
        public void run() {
          if (this.count.incrementAndGet() == this.total && groupCallback != null)
            groupCallback.onAllFinished(); 
        }
      };
    int i = tasks.length;
    for (byte b = 0; b < i; b++) {
      final T task = tasks[b];
      start(new TaskProxy((AbsTask)t) {
            protected void onCancelled(final Callback.CancelledException cex) {
              super.onCancelled(cex);
              TaskControllerImpl.this.post(new Runnable() {
                    public void run() {
                      if (groupCallback != null)
                        groupCallback.onCancelled(task, cex); 
                    }
                  });
            }
            
            protected void onError(final Throwable ex, final boolean isCallbackError) {
              super.onError(ex, isCallbackError);
              TaskControllerImpl.this.post(new Runnable() {
                    public void run() {
                      if (groupCallback != null)
                        groupCallback.onError(task, ex, isCallbackError); 
                    }
                  });
            }
            
            protected void onFinished() {
              super.onFinished();
              TaskControllerImpl.this.post(new Runnable() {
                    public void run() {
                      if (groupCallback != null)
                        groupCallback.onFinished(task); 
                      callIfOnAllFinished.run();
                    }
                  });
            }
            
            protected void onSuccess(Object param1Object) {
              super.onSuccess(param1Object);
              TaskControllerImpl.this.post(new Runnable() {
                    public void run() {
                      if (groupCallback != null)
                        groupCallback.onSuccess(task); 
                    }
                  });
            }
          });
    } 
    return new Callback.Cancelable() {
        public void cancel() {
          AbsTask[] arrayOfAbsTask = tasks;
          int i = arrayOfAbsTask.length;
          for (byte b = 0; b < i; b++)
            arrayOfAbsTask[b].cancel(); 
        }
        
        public boolean isCancelled() {
          boolean bool = true;
          AbsTask[] arrayOfAbsTask = tasks;
          int i = arrayOfAbsTask.length;
          for (byte b = 0; b < i; b++) {
            if (!arrayOfAbsTask[b].isCancelled())
              bool = false; 
          } 
          return bool;
        }
      };
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/common/task/TaskControllerImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */