package org.greenrobot.eventbus;

import java.util.ArrayList;
import java.util.List;

final class PendingPost {
  private static final List<PendingPost> pendingPostPool = new ArrayList<PendingPost>();
  
  Object event;
  
  PendingPost next;
  
  Subscription subscription;
  
  private PendingPost(Object paramObject, Subscription paramSubscription) {
    this.event = paramObject;
    this.subscription = paramSubscription;
  }
  
  static PendingPost obtainPendingPost(Subscription paramSubscription, Object paramObject) {
    synchronized (pendingPostPool) {
      int i = pendingPostPool.size();
      if (i > 0) {
        PendingPost pendingPost = pendingPostPool.remove(i - 1);
        pendingPost.event = paramObject;
        pendingPost.subscription = paramSubscription;
        pendingPost.next = null;
        return pendingPost;
      } 
      return new PendingPost(paramObject, paramSubscription);
    } 
  }
  
  static void releasePendingPost(PendingPost paramPendingPost) {
    paramPendingPost.event = null;
    paramPendingPost.subscription = null;
    paramPendingPost.next = null;
    synchronized (pendingPostPool) {
      if (pendingPostPool.size() < 10000)
        pendingPostPool.add(paramPendingPost); 
      return;
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/eventbus/PendingPost.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */