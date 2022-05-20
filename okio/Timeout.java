package okio;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;

public class Timeout {
  public static final Timeout NONE = new Timeout() {
      public Timeout deadlineNanoTime(long param1Long) {
        return this;
      }
      
      public void throwIfReached() throws IOException {}
      
      public Timeout timeout(long param1Long, TimeUnit param1TimeUnit) {
        return this;
      }
    };
  
  private long deadlineNanoTime;
  
  private boolean hasDeadline;
  
  private long timeoutNanos;
  
  public Timeout clearDeadline() {
    this.hasDeadline = false;
    return this;
  }
  
  public Timeout clearTimeout() {
    this.timeoutNanos = 0L;
    return this;
  }
  
  public final Timeout deadline(long paramLong, TimeUnit paramTimeUnit) {
    if (paramLong <= 0L)
      throw new IllegalArgumentException("duration <= 0: " + paramLong); 
    if (paramTimeUnit == null)
      throw new IllegalArgumentException("unit == null"); 
    return deadlineNanoTime(System.nanoTime() + paramTimeUnit.toNanos(paramLong));
  }
  
  public long deadlineNanoTime() {
    if (!this.hasDeadline)
      throw new IllegalStateException("No deadline"); 
    return this.deadlineNanoTime;
  }
  
  public Timeout deadlineNanoTime(long paramLong) {
    this.hasDeadline = true;
    this.deadlineNanoTime = paramLong;
    return this;
  }
  
  public boolean hasDeadline() {
    return this.hasDeadline;
  }
  
  public void throwIfReached() throws IOException {
    if (Thread.interrupted())
      throw new InterruptedIOException("thread interrupted"); 
    if (this.hasDeadline && this.deadlineNanoTime - System.nanoTime() <= 0L)
      throw new InterruptedIOException("deadline reached"); 
  }
  
  public Timeout timeout(long paramLong, TimeUnit paramTimeUnit) {
    if (paramLong < 0L)
      throw new IllegalArgumentException("timeout < 0: " + paramLong); 
    if (paramTimeUnit == null)
      throw new IllegalArgumentException("unit == null"); 
    this.timeoutNanos = paramTimeUnit.toNanos(paramLong);
    return this;
  }
  
  public long timeoutNanos() {
    return this.timeoutNanos;
  }
  
  public final void waitUntilNotified(Object paramObject) throws InterruptedIOException {
    try {
      boolean bool = hasDeadline();
      long l1 = timeoutNanos();
      if (!bool && l1 == 0L) {
        paramObject.wait();
        return;
      } 
      long l2 = System.nanoTime();
      if (bool && l1 != 0L) {
        l1 = Math.min(l1, deadlineNanoTime() - l2);
      } else if (bool) {
        l1 = deadlineNanoTime();
        l1 -= l2;
      } 
      long l3 = 0L;
      if (l1 > 0L) {
        l3 = l1 / 1000000L;
        paramObject.wait(l3, (int)(l1 - 1000000L * l3));
        l3 = System.nanoTime() - l2;
      } 
      if (l3 >= l1) {
        paramObject = new InterruptedIOException();
        super("timeout");
        throw paramObject;
      } 
      return;
    } catch (InterruptedException interruptedException) {
      throw new InterruptedIOException("interrupted");
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okio/Timeout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */