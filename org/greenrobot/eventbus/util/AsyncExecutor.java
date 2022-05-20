package org.greenrobot.eventbus.util;

import android.app.Activity;
import android.util.Log;
import java.lang.reflect.Constructor;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.greenrobot.eventbus.EventBus;

public class AsyncExecutor {
  private final EventBus eventBus;
  
  private final Constructor<?> failureEventConstructor;
  
  private final Object scope;
  
  private final Executor threadPool;
  
  private AsyncExecutor(Executor paramExecutor, EventBus paramEventBus, Class<?> paramClass, Object paramObject) {
    this.threadPool = paramExecutor;
    this.eventBus = paramEventBus;
    this.scope = paramObject;
    try {
      this.failureEventConstructor = paramClass.getConstructor(new Class[] { Throwable.class });
      return;
    } catch (NoSuchMethodException noSuchMethodException) {
      throw new RuntimeException("Failure event class must have a constructor with one parameter of type Throwable", noSuchMethodException);
    } 
  }
  
  public static Builder builder() {
    return new Builder();
  }
  
  public static AsyncExecutor create() {
    return (new Builder()).build();
  }
  
  public void execute(final RunnableEx runnable) {
    this.threadPool.execute(new Runnable() {
          public void run() {
            try {
              runnable.run();
              return;
            } catch (Exception exception) {
              try {
                HasExecutionScope hasExecutionScope = (HasExecutionScope)AsyncExecutor.this.failureEventConstructor.newInstance(new Object[] { exception });
                if (hasExecutionScope instanceof HasExecutionScope)
                  ((HasExecutionScope)hasExecutionScope).setExecutionScope(AsyncExecutor.this.scope); 
                AsyncExecutor.this.eventBus.post(hasExecutionScope);
                return;
              } catch (Exception exception1) {
                Log.e(EventBus.TAG, "Original exception:", exception);
                throw new RuntimeException("Could not create failure event", exception1);
              } 
            } 
          }
        });
  }
  
  public static class Builder {
    private EventBus eventBus;
    
    private Class<?> failureEventType;
    
    private Executor threadPool;
    
    private Builder() {}
    
    public AsyncExecutor build() {
      return buildForScope(null);
    }
    
    public AsyncExecutor buildForActivityScope(Activity param1Activity) {
      return buildForScope(param1Activity.getClass());
    }
    
    public AsyncExecutor buildForScope(Object param1Object) {
      if (this.eventBus == null)
        this.eventBus = EventBus.getDefault(); 
      if (this.threadPool == null)
        this.threadPool = Executors.newCachedThreadPool(); 
      if (this.failureEventType == null)
        this.failureEventType = ThrowableFailureEvent.class; 
      return new AsyncExecutor(this.threadPool, this.eventBus, this.failureEventType, param1Object);
    }
    
    public Builder eventBus(EventBus param1EventBus) {
      this.eventBus = param1EventBus;
      return this;
    }
    
    public Builder failureEventType(Class<?> param1Class) {
      this.failureEventType = param1Class;
      return this;
    }
    
    public Builder threadPool(Executor param1Executor) {
      this.threadPool = param1Executor;
      return this;
    }
  }
  
  public static interface RunnableEx {
    void run() throws Exception;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/eventbus/util/AsyncExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */