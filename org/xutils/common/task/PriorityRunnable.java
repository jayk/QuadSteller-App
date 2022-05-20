package org.xutils.common.task;

class PriorityRunnable implements Runnable {
  long SEQ;
  
  public final Priority priority;
  
  private final Runnable runnable;
  
  public PriorityRunnable(Priority paramPriority, Runnable paramRunnable) {
    Priority priority = paramPriority;
    if (paramPriority == null)
      priority = Priority.DEFAULT; 
    this.priority = priority;
    this.runnable = paramRunnable;
  }
  
  public final void run() {
    this.runnable.run();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/common/task/PriorityRunnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */