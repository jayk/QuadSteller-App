package org.greenrobot.eventbus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.greenrobot.eventbus.meta.SubscriberInfoIndex;

public class EventBusBuilder {
  private static final ExecutorService DEFAULT_EXECUTOR_SERVICE = Executors.newCachedThreadPool();
  
  boolean eventInheritance = true;
  
  ExecutorService executorService = DEFAULT_EXECUTOR_SERVICE;
  
  boolean ignoreGeneratedIndex;
  
  boolean logNoSubscriberMessages = true;
  
  boolean logSubscriberExceptions = true;
  
  boolean sendNoSubscriberEvent = true;
  
  boolean sendSubscriberExceptionEvent = true;
  
  List<Class<?>> skipMethodVerificationForClasses;
  
  boolean strictMethodVerification;
  
  List<SubscriberInfoIndex> subscriberInfoIndexes;
  
  boolean throwSubscriberException;
  
  public EventBusBuilder addIndex(SubscriberInfoIndex paramSubscriberInfoIndex) {
    if (this.subscriberInfoIndexes == null)
      this.subscriberInfoIndexes = new ArrayList<SubscriberInfoIndex>(); 
    this.subscriberInfoIndexes.add(paramSubscriberInfoIndex);
    return this;
  }
  
  public EventBus build() {
    return new EventBus(this);
  }
  
  public EventBusBuilder eventInheritance(boolean paramBoolean) {
    this.eventInheritance = paramBoolean;
    return this;
  }
  
  public EventBusBuilder executorService(ExecutorService paramExecutorService) {
    this.executorService = paramExecutorService;
    return this;
  }
  
  public EventBusBuilder ignoreGeneratedIndex(boolean paramBoolean) {
    this.ignoreGeneratedIndex = paramBoolean;
    return this;
  }
  
  public EventBus installDefaultEventBus() {
    // Byte code:
    //   0: ldc org/greenrobot/eventbus/EventBus
    //   2: monitorenter
    //   3: getstatic org/greenrobot/eventbus/EventBus.defaultInstance : Lorg/greenrobot/eventbus/EventBus;
    //   6: ifnull -> 27
    //   9: new org/greenrobot/eventbus/EventBusException
    //   12: astore_1
    //   13: aload_1
    //   14: ldc 'Default instance already exists. It may be only set once before it's used the first time to ensure consistent behavior.'
    //   16: invokespecial <init> : (Ljava/lang/String;)V
    //   19: aload_1
    //   20: athrow
    //   21: astore_1
    //   22: ldc org/greenrobot/eventbus/EventBus
    //   24: monitorexit
    //   25: aload_1
    //   26: athrow
    //   27: aload_0
    //   28: invokevirtual build : ()Lorg/greenrobot/eventbus/EventBus;
    //   31: putstatic org/greenrobot/eventbus/EventBus.defaultInstance : Lorg/greenrobot/eventbus/EventBus;
    //   34: getstatic org/greenrobot/eventbus/EventBus.defaultInstance : Lorg/greenrobot/eventbus/EventBus;
    //   37: astore_1
    //   38: ldc org/greenrobot/eventbus/EventBus
    //   40: monitorexit
    //   41: aload_1
    //   42: areturn
    // Exception table:
    //   from	to	target	type
    //   3	21	21	finally
    //   22	25	21	finally
    //   27	41	21	finally
  }
  
  public EventBusBuilder logNoSubscriberMessages(boolean paramBoolean) {
    this.logNoSubscriberMessages = paramBoolean;
    return this;
  }
  
  public EventBusBuilder logSubscriberExceptions(boolean paramBoolean) {
    this.logSubscriberExceptions = paramBoolean;
    return this;
  }
  
  public EventBusBuilder sendNoSubscriberEvent(boolean paramBoolean) {
    this.sendNoSubscriberEvent = paramBoolean;
    return this;
  }
  
  public EventBusBuilder sendSubscriberExceptionEvent(boolean paramBoolean) {
    this.sendSubscriberExceptionEvent = paramBoolean;
    return this;
  }
  
  public EventBusBuilder skipMethodVerificationFor(Class<?> paramClass) {
    if (this.skipMethodVerificationForClasses == null)
      this.skipMethodVerificationForClasses = new ArrayList<Class<?>>(); 
    this.skipMethodVerificationForClasses.add(paramClass);
    return this;
  }
  
  public EventBusBuilder strictMethodVerification(boolean paramBoolean) {
    this.strictMethodVerification = paramBoolean;
    return this;
  }
  
  public EventBusBuilder throwSubscriberException(boolean paramBoolean) {
    this.throwSubscriberException = paramBoolean;
    return this;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/eventbus/EventBusBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */