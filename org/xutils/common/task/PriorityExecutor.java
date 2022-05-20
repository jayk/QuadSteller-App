package org.xutils.common.task;

import java.util.Comparator;
import java.util.concurrent.Executor;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PriorityExecutor implements Executor {
  private static final int CORE_POOL_SIZE = 5;
  
  private static final Comparator<Runnable> FIFO_CMP;
  
  private static final Comparator<Runnable> FILO_CMP;
  
  private static final int KEEP_ALIVE = 1;
  
  private static final int MAXIMUM_POOL_SIZE = 256;
  
  private static final AtomicLong SEQ_SEED = new AtomicLong(0L);
  
  private static final ThreadFactory sThreadFactory = new ThreadFactory() {
      private final AtomicInteger mCount = new AtomicInteger(1);
      
      public Thread newThread(Runnable param1Runnable) {
        return new Thread(param1Runnable, "xTID#" + this.mCount.getAndIncrement());
      }
    };
  
  private final ThreadPoolExecutor mThreadPoolExecutor;
  
  static {
    FIFO_CMP = new Comparator<Runnable>() {
        public int compare(Runnable param1Runnable1, Runnable param1Runnable2) {
          if (param1Runnable1 instanceof PriorityRunnable && param1Runnable2 instanceof PriorityRunnable) {
            param1Runnable1 = param1Runnable1;
            param1Runnable2 = param1Runnable2;
            int i = ((PriorityRunnable)param1Runnable1).priority.ordinal() - ((PriorityRunnable)param1Runnable2).priority.ordinal();
            int j = i;
            if (i == 0)
              j = (int)(((PriorityRunnable)param1Runnable1).SEQ - ((PriorityRunnable)param1Runnable2).SEQ); 
            return j;
          } 
          return 0;
        }
      };
    FILO_CMP = new Comparator<Runnable>() {
        public int compare(Runnable param1Runnable1, Runnable param1Runnable2) {
          if (param1Runnable1 instanceof PriorityRunnable && param1Runnable2 instanceof PriorityRunnable) {
            param1Runnable1 = param1Runnable1;
            param1Runnable2 = param1Runnable2;
            int i = ((PriorityRunnable)param1Runnable1).priority.ordinal() - ((PriorityRunnable)param1Runnable2).priority.ordinal();
            int j = i;
            if (i == 0)
              j = (int)(((PriorityRunnable)param1Runnable2).SEQ - ((PriorityRunnable)param1Runnable1).SEQ); 
            return j;
          } 
          return 0;
        }
      };
  }
  
  public PriorityExecutor(int paramInt, boolean paramBoolean) {
    Comparator<Runnable> comparator;
    if (paramBoolean) {
      comparator = FIFO_CMP;
    } else {
      comparator = FILO_CMP;
    } 
    PriorityBlockingQueue<Runnable> priorityBlockingQueue = new PriorityBlockingQueue<Runnable>(256, comparator);
    this.mThreadPoolExecutor = new ThreadPoolExecutor(paramInt, 256, 1L, TimeUnit.SECONDS, priorityBlockingQueue, sThreadFactory);
  }
  
  public PriorityExecutor(boolean paramBoolean) {
    this(5, paramBoolean);
  }
  
  public void execute(Runnable paramRunnable) {
    if (paramRunnable instanceof PriorityRunnable)
      ((PriorityRunnable)paramRunnable).SEQ = SEQ_SEED.getAndIncrement(); 
    this.mThreadPoolExecutor.execute(paramRunnable);
  }
  
  public int getPoolSize() {
    return this.mThreadPoolExecutor.getCorePoolSize();
  }
  
  public ThreadPoolExecutor getThreadPoolExecutor() {
    return this.mThreadPoolExecutor;
  }
  
  public boolean isBusy() {
    return (this.mThreadPoolExecutor.getActiveCount() >= this.mThreadPoolExecutor.getCorePoolSize());
  }
  
  public void setPoolSize(int paramInt) {
    if (paramInt > 0)
      this.mThreadPoolExecutor.setCorePoolSize(paramInt); 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/common/task/PriorityExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */