package org.greenrobot.eventbus;

class AsyncPoster implements Runnable {
  private final EventBus eventBus;
  
  private final PendingPostQueue queue;
  
  AsyncPoster(EventBus paramEventBus) {
    this.eventBus = paramEventBus;
    this.queue = new PendingPostQueue();
  }
  
  public void enqueue(Subscription paramSubscription, Object paramObject) {
    PendingPost pendingPost = PendingPost.obtainPendingPost(paramSubscription, paramObject);
    this.queue.enqueue(pendingPost);
    this.eventBus.getExecutorService().execute(this);
  }
  
  public void run() {
    PendingPost pendingPost = this.queue.poll();
    if (pendingPost == null)
      throw new IllegalStateException("No pending post available"); 
    this.eventBus.invokeSubscriber(pendingPost);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/eventbus/AsyncPoster.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */