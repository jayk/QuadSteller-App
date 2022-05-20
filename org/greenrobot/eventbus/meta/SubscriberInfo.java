package org.greenrobot.eventbus.meta;

import org.greenrobot.eventbus.SubscriberMethod;

public interface SubscriberInfo {
  Class<?> getSubscriberClass();
  
  SubscriberMethod[] getSubscriberMethods();
  
  SubscriberInfo getSuperSubscriberInfo();
  
  boolean shouldCheckSuperclass();
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/eventbus/meta/SubscriberInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */