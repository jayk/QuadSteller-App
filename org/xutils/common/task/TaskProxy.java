package org.xutils.common.task;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.concurrent.Executor;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;

class TaskProxy<ResultType> extends AbsTask<ResultType> {
  private static final int MSG_WHAT_BASE = 1000000000;
  
  private static final int MSG_WHAT_ON_CANCEL = 1000000006;
  
  private static final int MSG_WHAT_ON_ERROR = 1000000004;
  
  private static final int MSG_WHAT_ON_FINISHED = 1000000007;
  
  private static final int MSG_WHAT_ON_START = 1000000002;
  
  private static final int MSG_WHAT_ON_SUCCESS = 1000000003;
  
  private static final int MSG_WHAT_ON_UPDATE = 1000000005;
  
  private static final int MSG_WHAT_ON_WAITING = 1000000001;
  
  static final PriorityExecutor sDefaultExecutor;
  
  static final InternalHandler sHandler = new InternalHandler();
  
  private volatile boolean callOnCanceled = false;
  
  private volatile boolean callOnFinished = false;
  
  private final Executor executor;
  
  private final AbsTask<ResultType> task;
  
  static {
    sDefaultExecutor = new PriorityExecutor(true);
  }
  
  TaskProxy(AbsTask<ResultType> paramAbsTask) {
    super(paramAbsTask);
    this.task = paramAbsTask;
    this.task.setTaskProxy(this);
    setTaskProxy(null);
    Executor executor2 = paramAbsTask.getExecutor();
    Executor executor1 = executor2;
    if (executor2 == null)
      executor1 = sDefaultExecutor; 
    this.executor = executor1;
  }
  
  protected final ResultType doBackground() throws Throwable {
    onWaiting();
    PriorityRunnable priorityRunnable = new PriorityRunnable(this.task.getPriority(), new Runnable() {
          public void run() {
            try {
              if (TaskProxy.this.callOnCanceled || TaskProxy.this.isCancelled()) {
                Callback.CancelledException cancelledException = new Callback.CancelledException();
                this("");
                throw cancelledException;
              } 
              TaskProxy.this.onStarted();
              if (TaskProxy.this.isCancelled()) {
                Callback.CancelledException cancelledException = new Callback.CancelledException();
                this("");
                throw cancelledException;
              } 
              TaskProxy.this.task.setResult(TaskProxy.this.task.doBackground());
              TaskProxy.this.setResult(TaskProxy.this.task.getResult());
            } catch (org.xutils.common.Callback.CancelledException cancelledException) {
              TaskProxy.this.onCancelled(cancelledException);
              return;
            } catch (Throwable throwable) {
              TaskProxy.this.onError(throwable, false);
              return;
            } finally {
              TaskProxy.this.onFinished();
            } 
            TaskProxy.this.onSuccess(TaskProxy.this.task.getResult());
            TaskProxy.this.onFinished();
          }
        });
    this.executor.execute(priorityRunnable);
    return null;
  }
  
  public final Executor getExecutor() {
    return this.executor;
  }
  
  public final Priority getPriority() {
    return this.task.getPriority();
  }
  
  protected void onCancelled(Callback.CancelledException paramCancelledException) {
    setState(AbsTask.State.CANCELLED);
    sHandler.obtainMessage(1000000006, new ArgsObj(this, new Object[] { paramCancelledException })).sendToTarget();
  }
  
  protected void onError(Throwable paramThrowable, boolean paramBoolean) {
    setState(AbsTask.State.ERROR);
    sHandler.obtainMessage(1000000004, new ArgsObj(this, new Object[] { paramThrowable })).sendToTarget();
  }
  
  protected void onFinished() {
    sHandler.obtainMessage(1000000007, this).sendToTarget();
  }
  
  protected void onStarted() {
    setState(AbsTask.State.STARTED);
    sHandler.obtainMessage(1000000002, this).sendToTarget();
  }
  
  protected void onSuccess(ResultType paramResultType) {
    setState(AbsTask.State.SUCCESS);
    sHandler.obtainMessage(1000000003, this).sendToTarget();
  }
  
  protected void onUpdate(int paramInt, Object... paramVarArgs) {
    sHandler.obtainMessage(1000000005, paramInt, paramInt, new ArgsObj(this, paramVarArgs)).sendToTarget();
  }
  
  protected void onWaiting() {
    setState(AbsTask.State.WAITING);
    sHandler.obtainMessage(1000000001, this).sendToTarget();
  }
  
  final void setState(AbsTask.State paramState) {
    super.setState(paramState);
    this.task.setState(paramState);
  }
  
  private static class ArgsObj {
    final Object[] args;
    
    final TaskProxy taskProxy;
    
    public ArgsObj(TaskProxy param1TaskProxy, Object... param1VarArgs) {
      this.taskProxy = param1TaskProxy;
      this.args = param1VarArgs;
    }
  }
  
  static final class InternalHandler extends Handler {
    static {
      boolean bool;
      if (!TaskProxy.class.desiredAssertionStatus()) {
        bool = true;
      } else {
        bool = false;
      } 
      $assertionsDisabled = bool;
    }
    
    private InternalHandler() {
      super(Looper.getMainLooper());
    }
    
    public void handleMessage(Message param1Message) {
      Object[] arrayOfObject;
      if (param1Message.obj == null)
        throw new IllegalArgumentException("msg must not be null"); 
      TaskProxy taskProxy = null;
      TaskProxy.ArgsObj argsObj = null;
      if (param1Message.obj instanceof TaskProxy) {
        taskProxy = (TaskProxy)param1Message.obj;
      } else if (param1Message.obj instanceof TaskProxy.ArgsObj) {
        argsObj = (TaskProxy.ArgsObj)param1Message.obj;
        taskProxy = argsObj.taskProxy;
        arrayOfObject = argsObj.args;
      } 
      if (taskProxy == null)
        throw new RuntimeException("msg.obj not instanceof TaskProxy"); 
      try {
        AssertionError assertionError;
        switch (param1Message.what) {
          default:
            return;
          case 1000000001:
            taskProxy.task.onWaiting();
          case 1000000002:
            taskProxy.task.onStarted();
          case 1000000003:
            taskProxy.task.onSuccess(taskProxy.getResult());
          case 1000000004:
            assert arrayOfObject != null;
            assertionError = assertionError[0];
            LogUtil.d(assertionError.getMessage(), assertionError);
            taskProxy.task.onError(assertionError, false);
          case 1000000005:
            taskProxy.task.onUpdate(param1Message.arg1, (Object[])assertionError);
          case 1000000006:
            if (!taskProxy.callOnCanceled) {
              TaskProxy.access$102(taskProxy, true);
              assert assertionError != null;
              taskProxy.task.onCancelled((Callback.CancelledException)assertionError[0]);
            } 
          case 1000000007:
            break;
        } 
      } catch (Throwable throwable) {
        taskProxy.setState(AbsTask.State.ERROR);
        if (param1Message.what != 1000000004)
          taskProxy.task.onError(throwable, true); 
      } 
      if (!taskProxy.callOnFinished) {
        TaskProxy.access$302(taskProxy, true);
        taskProxy.task.onFinished();
      } 
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/common/task/TaskProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */