package org.greenrobot.eventbus;

final class Subscription {
  volatile boolean active;
  
  final Object subscriber;
  
  final SubscriberMethod subscriberMethod;
  
  Subscription(Object paramObject, SubscriberMethod paramSubscriberMethod) {
    this.subscriber = paramObject;
    this.subscriberMethod = paramSubscriberMethod;
    this.active = true;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (paramObject instanceof Subscription) {
      paramObject = paramObject;
      bool2 = bool1;
      if (this.subscriber == ((Subscription)paramObject).subscriber) {
        bool2 = bool1;
        if (this.subscriberMethod.equals(((Subscription)paramObject).subscriberMethod))
          bool2 = true; 
      } 
    } 
    return bool2;
  }
  
  public int hashCode() {
    return this.subscriber.hashCode() + this.subscriberMethod.methodString.hashCode();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/eventbus/Subscription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */