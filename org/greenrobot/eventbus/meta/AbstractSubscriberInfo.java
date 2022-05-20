package org.greenrobot.eventbus.meta;

import org.greenrobot.eventbus.EventBusException;
import org.greenrobot.eventbus.SubscriberMethod;
import org.greenrobot.eventbus.ThreadMode;

public abstract class AbstractSubscriberInfo implements SubscriberInfo {
  private final boolean shouldCheckSuperclass;
  
  private final Class subscriberClass;
  
  private final Class<? extends SubscriberInfo> superSubscriberInfoClass;
  
  protected AbstractSubscriberInfo(Class paramClass, Class<? extends SubscriberInfo> paramClass1, boolean paramBoolean) {
    this.subscriberClass = paramClass;
    this.superSubscriberInfoClass = paramClass1;
    this.shouldCheckSuperclass = paramBoolean;
  }
  
  protected SubscriberMethod createSubscriberMethod(String paramString, Class<?> paramClass) {
    return createSubscriberMethod(paramString, paramClass, ThreadMode.POSTING, 0, false);
  }
  
  protected SubscriberMethod createSubscriberMethod(String paramString, Class<?> paramClass, ThreadMode paramThreadMode) {
    return createSubscriberMethod(paramString, paramClass, paramThreadMode, 0, false);
  }
  
  protected SubscriberMethod createSubscriberMethod(String paramString, Class<?> paramClass, ThreadMode paramThreadMode, int paramInt, boolean paramBoolean) {
    try {
      return new SubscriberMethod(this.subscriberClass.getDeclaredMethod(paramString, new Class[] { paramClass }), paramClass, paramThreadMode, paramInt, paramBoolean);
    } catch (NoSuchMethodException noSuchMethodException) {
      throw new EventBusException("Could not find subscriber method in " + this.subscriberClass + ". Maybe a missing ProGuard rule?", noSuchMethodException);
    } 
  }
  
  public Class getSubscriberClass() {
    return this.subscriberClass;
  }
  
  public SubscriberInfo getSuperSubscriberInfo() {
    if (this.superSubscriberInfoClass == null)
      return null; 
    try {
      return this.superSubscriberInfoClass.newInstance();
    } catch (InstantiationException instantiationException) {
    
    } catch (IllegalAccessException illegalAccessException) {}
    throw new RuntimeException(illegalAccessException);
  }
  
  public boolean shouldCheckSuperclass() {
    return this.shouldCheckSuperclass;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/eventbus/meta/AbstractSubscriberInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */