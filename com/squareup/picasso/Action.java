package com.squareup.picasso;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

abstract class Action<T> {
  boolean cancelled;
  
  final Drawable errorDrawable;
  
  final int errorResId;
  
  final String key;
  
  final int memoryPolicy;
  
  final int networkPolicy;
  
  final boolean noFade;
  
  final Picasso picasso;
  
  final Request request;
  
  final Object tag;
  
  final WeakReference<T> target;
  
  boolean willReplay;
  
  Action(Picasso paramPicasso, T paramT, Request paramRequest, int paramInt1, int paramInt2, int paramInt3, Drawable paramDrawable, String paramString, Object paramObject, boolean paramBoolean) {
    RequestWeakReference<T> requestWeakReference;
    Object object;
    this.picasso = paramPicasso;
    this.request = paramRequest;
    if (paramT == null) {
      paramPicasso = null;
    } else {
      requestWeakReference = new RequestWeakReference<T>(this, paramT, paramPicasso.referenceQueue);
    } 
    this.target = requestWeakReference;
    this.memoryPolicy = paramInt1;
    this.networkPolicy = paramInt2;
    this.noFade = paramBoolean;
    this.errorResId = paramInt3;
    this.errorDrawable = paramDrawable;
    this.key = paramString;
    if (paramObject != null) {
      object = paramObject;
    } else {
      object = this;
    } 
    this.tag = object;
  }
  
  void cancel() {
    this.cancelled = true;
  }
  
  abstract void complete(Bitmap paramBitmap, Picasso.LoadedFrom paramLoadedFrom);
  
  abstract void error();
  
  String getKey() {
    return this.key;
  }
  
  int getMemoryPolicy() {
    return this.memoryPolicy;
  }
  
  int getNetworkPolicy() {
    return this.networkPolicy;
  }
  
  Picasso getPicasso() {
    return this.picasso;
  }
  
  Picasso.Priority getPriority() {
    return this.request.priority;
  }
  
  Request getRequest() {
    return this.request;
  }
  
  Object getTag() {
    return this.tag;
  }
  
  T getTarget() {
    return (this.target == null) ? null : this.target.get();
  }
  
  boolean isCancelled() {
    return this.cancelled;
  }
  
  boolean willReplay() {
    return this.willReplay;
  }
  
  static class RequestWeakReference<M> extends WeakReference<M> {
    final Action action;
    
    public RequestWeakReference(Action param1Action, M param1M, ReferenceQueue<? super M> param1ReferenceQueue) {
      super(param1M, param1ReferenceQueue);
      this.action = param1Action;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/squareup/picasso/Action.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */