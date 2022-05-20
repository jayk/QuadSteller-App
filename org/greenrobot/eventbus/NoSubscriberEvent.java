package org.greenrobot.eventbus;

public final class NoSubscriberEvent {
  public final EventBus eventBus;
  
  public final Object originalEvent;
  
  public NoSubscriberEvent(EventBus paramEventBus, Object paramObject) {
    this.eventBus = paramEventBus;
    this.originalEvent = paramObject;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/eventbus/NoSubscriberEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */