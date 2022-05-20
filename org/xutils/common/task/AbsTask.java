package org.xutils.common.task;

import java.util.concurrent.Executor;
import org.xutils.common.Callback;

public abstract class AbsTask<ResultType> implements Callback.Cancelable {
  private final Callback.Cancelable cancelHandler;
  
  private volatile boolean isCancelled = false;
  
  private ResultType result;
  
  private volatile State state = State.IDLE;
  
  private TaskProxy taskProxy = null;
  
  public AbsTask() {
    this(null);
  }
  
  public AbsTask(Callback.Cancelable paramCancelable) {
    this.cancelHandler = paramCancelable;
  }
  
  public final void cancel() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isCancelled : Z
    //   6: ifne -> 107
    //   9: aload_0
    //   10: iconst_1
    //   11: putfield isCancelled : Z
    //   14: aload_0
    //   15: invokevirtual cancelWorks : ()V
    //   18: aload_0
    //   19: getfield cancelHandler : Lorg/xutils/common/Callback$Cancelable;
    //   22: ifnull -> 46
    //   25: aload_0
    //   26: getfield cancelHandler : Lorg/xutils/common/Callback$Cancelable;
    //   29: invokeinterface isCancelled : ()Z
    //   34: ifne -> 46
    //   37: aload_0
    //   38: getfield cancelHandler : Lorg/xutils/common/Callback$Cancelable;
    //   41: invokeinterface cancel : ()V
    //   46: aload_0
    //   47: getfield state : Lorg/xutils/common/task/AbsTask$State;
    //   50: getstatic org/xutils/common/task/AbsTask$State.WAITING : Lorg/xutils/common/task/AbsTask$State;
    //   53: if_acmpeq -> 73
    //   56: aload_0
    //   57: getfield state : Lorg/xutils/common/task/AbsTask$State;
    //   60: getstatic org/xutils/common/task/AbsTask$State.STARTED : Lorg/xutils/common/task/AbsTask$State;
    //   63: if_acmpne -> 107
    //   66: aload_0
    //   67: invokevirtual isCancelFast : ()Z
    //   70: ifeq -> 107
    //   73: aload_0
    //   74: getfield taskProxy : Lorg/xutils/common/task/TaskProxy;
    //   77: ifnull -> 110
    //   80: aload_0
    //   81: getfield taskProxy : Lorg/xutils/common/task/TaskProxy;
    //   84: astore_1
    //   85: new org/xutils/common/Callback$CancelledException
    //   88: astore_2
    //   89: aload_2
    //   90: ldc 'cancelled by user'
    //   92: invokespecial <init> : (Ljava/lang/String;)V
    //   95: aload_1
    //   96: aload_2
    //   97: invokevirtual onCancelled : (Lorg/xutils/common/Callback$CancelledException;)V
    //   100: aload_0
    //   101: getfield taskProxy : Lorg/xutils/common/task/TaskProxy;
    //   104: invokevirtual onFinished : ()V
    //   107: aload_0
    //   108: monitorexit
    //   109: return
    //   110: aload_0
    //   111: instanceof org/xutils/common/task/TaskProxy
    //   114: ifeq -> 107
    //   117: new org/xutils/common/Callback$CancelledException
    //   120: astore_1
    //   121: aload_1
    //   122: ldc 'cancelled by user'
    //   124: invokespecial <init> : (Ljava/lang/String;)V
    //   127: aload_0
    //   128: aload_1
    //   129: invokevirtual onCancelled : (Lorg/xutils/common/Callback$CancelledException;)V
    //   132: aload_0
    //   133: invokevirtual onFinished : ()V
    //   136: goto -> 107
    //   139: astore_1
    //   140: aload_0
    //   141: monitorexit
    //   142: aload_1
    //   143: athrow
    // Exception table:
    //   from	to	target	type
    //   2	46	139	finally
    //   46	73	139	finally
    //   73	107	139	finally
    //   110	136	139	finally
  }
  
  protected void cancelWorks() {}
  
  protected abstract ResultType doBackground() throws Throwable;
  
  public Executor getExecutor() {
    return null;
  }
  
  public Priority getPriority() {
    return null;
  }
  
  public final ResultType getResult() {
    return this.result;
  }
  
  public final State getState() {
    return this.state;
  }
  
  protected boolean isCancelFast() {
    return false;
  }
  
  public final boolean isCancelled() {
    return (this.isCancelled || this.state == State.CANCELLED || (this.cancelHandler != null && this.cancelHandler.isCancelled()));
  }
  
  public final boolean isFinished() {
    return (this.state.value() > State.STARTED.value());
  }
  
  protected void onCancelled(Callback.CancelledException paramCancelledException) {}
  
  protected abstract void onError(Throwable paramThrowable, boolean paramBoolean);
  
  protected void onFinished() {}
  
  protected void onStarted() {}
  
  protected abstract void onSuccess(ResultType paramResultType);
  
  protected void onUpdate(int paramInt, Object... paramVarArgs) {}
  
  protected void onWaiting() {}
  
  final void setResult(ResultType paramResultType) {
    this.result = paramResultType;
  }
  
  void setState(State paramState) {
    this.state = paramState;
  }
  
  final void setTaskProxy(TaskProxy paramTaskProxy) {
    this.taskProxy = paramTaskProxy;
  }
  
  protected final void update(int paramInt, Object... paramVarArgs) {
    if (this.taskProxy != null)
      this.taskProxy.onUpdate(paramInt, paramVarArgs); 
  }
  
  public enum State {
    CANCELLED,
    ERROR,
    IDLE(0),
    STARTED(0),
    SUCCESS(0),
    WAITING(1);
    
    private final int value;
    
    static {
      CANCELLED = new State("CANCELLED", 4, 4);
      ERROR = new State("ERROR", 5, 5);
      $VALUES = new State[] { IDLE, WAITING, STARTED, SUCCESS, CANCELLED, ERROR };
    }
    
    State(int param1Int1) {
      this.value = param1Int1;
    }
    
    public int value() {
      return this.value;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/common/task/AbsTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */