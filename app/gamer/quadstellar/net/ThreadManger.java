package app.gamer.quadstellar.net;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadManger {
  private static int CORE_POOL_SIZE = 30;
  
  private static int KEEP_ALIVE_TIME;
  
  private static int MAX_POOL_SIZE = Integer.MAX_VALUE;
  
  private static ThreadFactory threadFactory;
  
  private static ThreadPoolExecutor threadPool;
  
  private static BlockingQueue<Runnable> workQueue;
  
  static {
    KEEP_ALIVE_TIME = 10000;
    workQueue = new ArrayBlockingQueue<Runnable>(10);
    threadFactory = new ThreadFactory() {
        private final AtomicInteger integer = new AtomicInteger();
        
        public Thread newThread(Runnable param1Runnable) {
          return new Thread(param1Runnable, "myThreadPool thread:" + this.integer.getAndIncrement());
        }
      };
    threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue, threadFactory);
  }
  
  public static void execute(Runnable paramRunnable) {
    threadPool.execute(paramRunnable);
  }
  
  public static void remove(Runnable paramRunnable) {
    threadPool.remove(paramRunnable);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/net/ThreadManger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */