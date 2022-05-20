package com.squareup.picasso;

import android.app.Notification;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.RemoteViews;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestCreator {
  private static final AtomicInteger nextId = new AtomicInteger();
  
  private final Request.Builder data;
  
  private boolean deferred;
  
  private Drawable errorDrawable;
  
  private int errorResId;
  
  private int memoryPolicy;
  
  private int networkPolicy;
  
  private boolean noFade;
  
  private final Picasso picasso;
  
  private Drawable placeholderDrawable;
  
  private int placeholderResId;
  
  private boolean setPlaceholder = true;
  
  private Object tag;
  
  RequestCreator() {
    this.picasso = null;
    this.data = new Request.Builder(null, 0, null);
  }
  
  RequestCreator(Picasso paramPicasso, Uri paramUri, int paramInt) {
    if (paramPicasso.shutdown)
      throw new IllegalStateException("Picasso instance already shut down. Cannot submit new requests."); 
    this.picasso = paramPicasso;
    this.data = new Request.Builder(paramUri, paramInt, paramPicasso.defaultBitmapConfig);
  }
  
  private Request createRequest(long paramLong) {
    int i = nextId.getAndIncrement();
    Request request1 = this.data.build();
    request1.id = i;
    request1.started = paramLong;
    boolean bool = this.picasso.loggingEnabled;
    if (bool)
      Utils.log("Main", "created", request1.plainId(), request1.toString()); 
    Request request2 = this.picasso.transformRequest(request1);
    if (request2 != request1) {
      request2.id = i;
      request2.started = paramLong;
      if (bool)
        Utils.log("Main", "changed", request2.logId(), "into " + request2); 
    } 
    return request2;
  }
  
  private Drawable getPlaceholderDrawable() {
    return (this.placeholderResId != 0) ? this.picasso.context.getResources().getDrawable(this.placeholderResId) : this.placeholderDrawable;
  }
  
  private void performRemoteViewInto(RemoteViewsAction paramRemoteViewsAction) {
    if (MemoryPolicy.shouldReadFromMemoryCache(this.memoryPolicy)) {
      Bitmap bitmap = this.picasso.quickMemoryCacheCheck(paramRemoteViewsAction.getKey());
      if (bitmap != null) {
        paramRemoteViewsAction.complete(bitmap, Picasso.LoadedFrom.MEMORY);
        return;
      } 
    } 
    if (this.placeholderResId != 0)
      paramRemoteViewsAction.setImageResource(this.placeholderResId); 
    this.picasso.enqueueAndSubmit(paramRemoteViewsAction);
  }
  
  public RequestCreator centerCrop() {
    this.data.centerCrop();
    return this;
  }
  
  public RequestCreator centerInside() {
    this.data.centerInside();
    return this;
  }
  
  public RequestCreator config(Bitmap.Config paramConfig) {
    this.data.config(paramConfig);
    return this;
  }
  
  public RequestCreator error(int paramInt) {
    if (paramInt == 0)
      throw new IllegalArgumentException("Error image resource invalid."); 
    if (this.errorDrawable != null)
      throw new IllegalStateException("Error image already set."); 
    this.errorResId = paramInt;
    return this;
  }
  
  public RequestCreator error(Drawable paramDrawable) {
    if (paramDrawable == null)
      throw new IllegalArgumentException("Error image may not be null."); 
    if (this.errorResId != 0)
      throw new IllegalStateException("Error image already set."); 
    this.errorDrawable = paramDrawable;
    return this;
  }
  
  public void fetch() {
    fetch(null);
  }
  
  public void fetch(Callback paramCallback) {
    Request request;
    String str;
    long l = System.nanoTime();
    if (this.deferred)
      throw new IllegalStateException("Fit cannot be used with fetch."); 
    if (this.data.hasImage()) {
      if (!this.data.hasPriority())
        this.data.priority(Picasso.Priority.LOW); 
      request = createRequest(l);
      str = Utils.createKey(request, new StringBuilder());
      if (this.picasso.quickMemoryCacheCheck(str) != null) {
        if (this.picasso.loggingEnabled)
          Utils.log("Main", "completed", request.plainId(), "from " + Picasso.LoadedFrom.MEMORY); 
        if (paramCallback != null)
          paramCallback.onSuccess(); 
        return;
      } 
    } else {
      return;
    } 
    FetchAction fetchAction = new FetchAction(this.picasso, request, this.memoryPolicy, this.networkPolicy, this.tag, str, paramCallback);
    this.picasso.submit(fetchAction);
  }
  
  public RequestCreator fit() {
    this.deferred = true;
    return this;
  }
  
  public Bitmap get() throws IOException {
    long l = System.nanoTime();
    Utils.checkNotMain();
    if (this.deferred)
      throw new IllegalStateException("Fit cannot be used with get."); 
    if (!this.data.hasImage())
      return null; 
    Request request = createRequest(l);
    String str = Utils.createKey(request, new StringBuilder());
    GetAction getAction = new GetAction(this.picasso, request, this.memoryPolicy, this.networkPolicy, this.tag, str);
    return BitmapHunter.forRequest(this.picasso, this.picasso.dispatcher, this.picasso.cache, this.picasso.stats, getAction).hunt();
  }
  
  public void into(ImageView paramImageView) {
    into(paramImageView, null);
  }
  
  public void into(ImageView paramImageView, Callback paramCallback) {
    long l = System.nanoTime();
    Utils.checkMain();
    if (paramImageView == null)
      throw new IllegalArgumentException("Target must not be null."); 
    if (!this.data.hasImage()) {
      this.picasso.cancelRequest(paramImageView);
      if (this.setPlaceholder)
        PicassoDrawable.setPlaceholder(paramImageView, getPlaceholderDrawable()); 
      return;
    } 
    if (this.deferred) {
      if (this.data.hasSize())
        throw new IllegalStateException("Fit cannot be used with resize."); 
      int i = paramImageView.getWidth();
      int j = paramImageView.getHeight();
      if (i == 0 || j == 0) {
        if (this.setPlaceholder)
          PicassoDrawable.setPlaceholder(paramImageView, getPlaceholderDrawable()); 
        this.picasso.defer(paramImageView, new DeferredRequestCreator(this, paramImageView, paramCallback));
        return;
      } 
      this.data.resize(i, j);
    } 
    Request request = createRequest(l);
    String str = Utils.createKey(request);
    if (MemoryPolicy.shouldReadFromMemoryCache(this.memoryPolicy)) {
      Bitmap bitmap = this.picasso.quickMemoryCacheCheck(str);
      if (bitmap != null) {
        this.picasso.cancelRequest(paramImageView);
        PicassoDrawable.setBitmap(paramImageView, this.picasso.context, bitmap, Picasso.LoadedFrom.MEMORY, this.noFade, this.picasso.indicatorsEnabled);
        if (this.picasso.loggingEnabled)
          Utils.log("Main", "completed", request.plainId(), "from " + Picasso.LoadedFrom.MEMORY); 
        if (paramCallback != null)
          paramCallback.onSuccess(); 
        return;
      } 
    } 
    if (this.setPlaceholder)
      PicassoDrawable.setPlaceholder(paramImageView, getPlaceholderDrawable()); 
    ImageViewAction imageViewAction = new ImageViewAction(this.picasso, paramImageView, request, this.memoryPolicy, this.networkPolicy, this.errorResId, this.errorDrawable, str, this.tag, paramCallback, this.noFade);
    this.picasso.enqueueAndSubmit(imageViewAction);
  }
  
  public void into(RemoteViews paramRemoteViews, int paramInt1, int paramInt2, Notification paramNotification) {
    long l = System.nanoTime();
    if (paramRemoteViews == null)
      throw new IllegalArgumentException("RemoteViews must not be null."); 
    if (paramNotification == null)
      throw new IllegalArgumentException("Notification must not be null."); 
    if (this.deferred)
      throw new IllegalStateException("Fit cannot be used with RemoteViews."); 
    if (this.placeholderDrawable != null || this.placeholderResId != 0 || this.errorDrawable != null)
      throw new IllegalArgumentException("Cannot use placeholder or error drawables with remote views."); 
    Request request = createRequest(l);
    String str = Utils.createKey(request, new StringBuilder());
    performRemoteViewInto(new RemoteViewsAction.NotificationAction(this.picasso, request, paramRemoteViews, paramInt1, paramInt2, paramNotification, this.memoryPolicy, this.networkPolicy, str, this.tag, this.errorResId));
  }
  
  public void into(RemoteViews paramRemoteViews, int paramInt, int[] paramArrayOfint) {
    long l = System.nanoTime();
    if (paramRemoteViews == null)
      throw new IllegalArgumentException("remoteViews must not be null."); 
    if (paramArrayOfint == null)
      throw new IllegalArgumentException("appWidgetIds must not be null."); 
    if (this.deferred)
      throw new IllegalStateException("Fit cannot be used with remote views."); 
    if (this.placeholderDrawable != null || this.placeholderResId != 0 || this.errorDrawable != null)
      throw new IllegalArgumentException("Cannot use placeholder or error drawables with remote views."); 
    Request request = createRequest(l);
    String str = Utils.createKey(request, new StringBuilder());
    performRemoteViewInto(new RemoteViewsAction.AppWidgetAction(this.picasso, request, paramRemoteViews, paramInt, paramArrayOfint, this.memoryPolicy, this.networkPolicy, str, this.tag, this.errorResId));
  }
  
  public void into(Target paramTarget) {
    Drawable drawable1 = null;
    Drawable drawable2 = null;
    long l = System.nanoTime();
    Utils.checkMain();
    if (paramTarget == null)
      throw new IllegalArgumentException("Target must not be null."); 
    if (this.deferred)
      throw new IllegalStateException("Fit cannot be used with a Target."); 
    if (!this.data.hasImage()) {
      this.picasso.cancelRequest(paramTarget);
      drawable1 = drawable2;
      if (this.setPlaceholder)
        drawable1 = getPlaceholderDrawable(); 
      paramTarget.onPrepareLoad(drawable1);
      return;
    } 
    Request request = createRequest(l);
    String str = Utils.createKey(request);
    if (MemoryPolicy.shouldReadFromMemoryCache(this.memoryPolicy)) {
      Bitmap bitmap = this.picasso.quickMemoryCacheCheck(str);
      if (bitmap != null) {
        this.picasso.cancelRequest(paramTarget);
        paramTarget.onBitmapLoaded(bitmap, Picasso.LoadedFrom.MEMORY);
        return;
      } 
    } 
    if (this.setPlaceholder)
      drawable1 = getPlaceholderDrawable(); 
    paramTarget.onPrepareLoad(drawable1);
    TargetAction targetAction = new TargetAction(this.picasso, paramTarget, request, this.memoryPolicy, this.networkPolicy, this.errorDrawable, str, this.tag, this.errorResId);
    this.picasso.enqueueAndSubmit(targetAction);
  }
  
  public RequestCreator memoryPolicy(MemoryPolicy paramMemoryPolicy, MemoryPolicy... paramVarArgs) {
    if (paramMemoryPolicy == null)
      throw new IllegalArgumentException("Memory policy cannot be null."); 
    this.memoryPolicy |= paramMemoryPolicy.index;
    if (paramVarArgs == null)
      throw new IllegalArgumentException("Memory policy cannot be null."); 
    if (paramVarArgs.length > 0) {
      int i = paramVarArgs.length;
      for (byte b = 0; b < i; b++) {
        paramMemoryPolicy = paramVarArgs[b];
        if (paramMemoryPolicy == null)
          throw new IllegalArgumentException("Memory policy cannot be null."); 
        this.memoryPolicy |= paramMemoryPolicy.index;
      } 
    } 
    return this;
  }
  
  public RequestCreator networkPolicy(NetworkPolicy paramNetworkPolicy, NetworkPolicy... paramVarArgs) {
    if (paramNetworkPolicy == null)
      throw new IllegalArgumentException("Network policy cannot be null."); 
    this.networkPolicy |= paramNetworkPolicy.index;
    if (paramVarArgs == null)
      throw new IllegalArgumentException("Network policy cannot be null."); 
    if (paramVarArgs.length > 0) {
      int i = paramVarArgs.length;
      for (byte b = 0; b < i; b++) {
        paramNetworkPolicy = paramVarArgs[b];
        if (paramNetworkPolicy == null)
          throw new IllegalArgumentException("Network policy cannot be null."); 
        this.networkPolicy |= paramNetworkPolicy.index;
      } 
    } 
    return this;
  }
  
  public RequestCreator noFade() {
    this.noFade = true;
    return this;
  }
  
  public RequestCreator noPlaceholder() {
    if (this.placeholderResId != 0)
      throw new IllegalStateException("Placeholder resource already set."); 
    if (this.placeholderDrawable != null)
      throw new IllegalStateException("Placeholder image already set."); 
    this.setPlaceholder = false;
    return this;
  }
  
  public RequestCreator onlyScaleDown() {
    this.data.onlyScaleDown();
    return this;
  }
  
  public RequestCreator placeholder(int paramInt) {
    if (!this.setPlaceholder)
      throw new IllegalStateException("Already explicitly declared as no placeholder."); 
    if (paramInt == 0)
      throw new IllegalArgumentException("Placeholder image resource invalid."); 
    if (this.placeholderDrawable != null)
      throw new IllegalStateException("Placeholder image already set."); 
    this.placeholderResId = paramInt;
    return this;
  }
  
  public RequestCreator placeholder(Drawable paramDrawable) {
    if (!this.setPlaceholder)
      throw new IllegalStateException("Already explicitly declared as no placeholder."); 
    if (this.placeholderResId != 0)
      throw new IllegalStateException("Placeholder image already set."); 
    this.placeholderDrawable = paramDrawable;
    return this;
  }
  
  public RequestCreator priority(Picasso.Priority paramPriority) {
    this.data.priority(paramPriority);
    return this;
  }
  
  public RequestCreator resize(int paramInt1, int paramInt2) {
    this.data.resize(paramInt1, paramInt2);
    return this;
  }
  
  public RequestCreator resizeDimen(int paramInt1, int paramInt2) {
    Resources resources = this.picasso.context.getResources();
    return resize(resources.getDimensionPixelSize(paramInt1), resources.getDimensionPixelSize(paramInt2));
  }
  
  public RequestCreator rotate(float paramFloat) {
    this.data.rotate(paramFloat);
    return this;
  }
  
  public RequestCreator rotate(float paramFloat1, float paramFloat2, float paramFloat3) {
    this.data.rotate(paramFloat1, paramFloat2, paramFloat3);
    return this;
  }
  
  @Deprecated
  public RequestCreator skipMemoryCache() {
    return memoryPolicy(MemoryPolicy.NO_CACHE, new MemoryPolicy[] { MemoryPolicy.NO_STORE });
  }
  
  public RequestCreator stableKey(String paramString) {
    this.data.stableKey(paramString);
    return this;
  }
  
  public RequestCreator tag(Object paramObject) {
    if (paramObject == null)
      throw new IllegalArgumentException("Tag invalid."); 
    if (this.tag != null)
      throw new IllegalStateException("Tag already set."); 
    this.tag = paramObject;
    return this;
  }
  
  public RequestCreator transform(Transformation paramTransformation) {
    this.data.transform(paramTransformation);
    return this;
  }
  
  public RequestCreator transform(List<? extends Transformation> paramList) {
    this.data.transform(paramList);
    return this;
  }
  
  RequestCreator unfit() {
    this.deferred = false;
    return this;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/squareup/picasso/RequestCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */