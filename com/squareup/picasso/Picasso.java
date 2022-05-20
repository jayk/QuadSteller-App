package com.squareup.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.ImageView;
import android.widget.RemoteViews;
import java.io.File;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;

public class Picasso {
  static final Handler HANDLER = new Handler(Looper.getMainLooper()) {
      public void handleMessage(Message param1Message) {
        List<BitmapHunter> list1;
        Action action;
        switch (param1Message.what) {
          default:
            throw new AssertionError("Unknown handler message received: " + param1Message.what);
          case 8:
            list1 = (List)param1Message.obj;
            b = 0;
            i = list1.size();
            while (b < i) {
              BitmapHunter bitmapHunter = list1.get(b);
              bitmapHunter.picasso.complete(bitmapHunter);
              b++;
            } 
            return;
          case 3:
            action = (Action)((Message)list1).obj;
            if ((action.getPicasso()).loggingEnabled)
              Utils.log("Main", "canceled", action.request.logId(), "target got garbage collected"); 
            action.picasso.cancelExistingRequest(action.getTarget());
            return;
          case 13:
            break;
        } 
        List<Action> list = (List)((Message)action).obj;
        byte b = 0;
        int i = list.size();
        while (true) {
          if (b < i) {
            Action action1 = list.get(b);
            action1.picasso.resumeAction(action1);
            b++;
            continue;
          } 
          return;
        } 
      }
    };
  
  static final String TAG = "Picasso";
  
  static volatile Picasso singleton = null;
  
  final Cache cache;
  
  private final CleanupThread cleanupThread;
  
  final Context context;
  
  final Bitmap.Config defaultBitmapConfig;
  
  final Dispatcher dispatcher;
  
  boolean indicatorsEnabled;
  
  private final Listener listener;
  
  volatile boolean loggingEnabled;
  
  final ReferenceQueue<Object> referenceQueue;
  
  private final List<RequestHandler> requestHandlers;
  
  private final RequestTransformer requestTransformer;
  
  boolean shutdown;
  
  final Stats stats;
  
  final Map<Object, Action> targetToAction;
  
  final Map<ImageView, DeferredRequestCreator> targetToDeferredRequestCreator;
  
  Picasso(Context paramContext, Dispatcher paramDispatcher, Cache paramCache, Listener paramListener, RequestTransformer paramRequestTransformer, List<RequestHandler> paramList, Stats paramStats, Bitmap.Config paramConfig, boolean paramBoolean1, boolean paramBoolean2) {
    byte b;
    this.context = paramContext;
    this.dispatcher = paramDispatcher;
    this.cache = paramCache;
    this.listener = paramListener;
    this.requestTransformer = paramRequestTransformer;
    this.defaultBitmapConfig = paramConfig;
    if (paramList != null) {
      b = paramList.size();
    } else {
      b = 0;
    } 
    ArrayList<ResourceRequestHandler> arrayList = new ArrayList(7 + b);
    arrayList.add(new ResourceRequestHandler(paramContext));
    if (paramList != null)
      arrayList.addAll(paramList); 
    arrayList.add(new ContactsPhotoRequestHandler(paramContext));
    arrayList.add(new MediaStoreRequestHandler(paramContext));
    arrayList.add(new ContentStreamRequestHandler(paramContext));
    arrayList.add(new AssetRequestHandler(paramContext));
    arrayList.add(new FileRequestHandler(paramContext));
    arrayList.add(new NetworkRequestHandler(paramDispatcher.downloader, paramStats));
    this.requestHandlers = Collections.unmodifiableList((List)arrayList);
    this.stats = paramStats;
    this.targetToAction = new WeakHashMap<Object, Action>();
    this.targetToDeferredRequestCreator = new WeakHashMap<ImageView, DeferredRequestCreator>();
    this.indicatorsEnabled = paramBoolean1;
    this.loggingEnabled = paramBoolean2;
    this.referenceQueue = new ReferenceQueue();
    this.cleanupThread = new CleanupThread(this.referenceQueue, HANDLER);
    this.cleanupThread.start();
  }
  
  private void cancelExistingRequest(Object paramObject) {
    Utils.checkMain();
    Action action = this.targetToAction.remove(paramObject);
    if (action != null) {
      action.cancel();
      this.dispatcher.dispatchCancel(action);
    } 
    if (paramObject instanceof ImageView) {
      paramObject = paramObject;
      paramObject = this.targetToDeferredRequestCreator.remove(paramObject);
      if (paramObject != null)
        paramObject.cancel(); 
    } 
  }
  
  private void deliverAction(Bitmap paramBitmap, LoadedFrom paramLoadedFrom, Action paramAction) {
    if (!paramAction.isCancelled()) {
      if (!paramAction.willReplay())
        this.targetToAction.remove(paramAction.getTarget()); 
      if (paramBitmap != null) {
        if (paramLoadedFrom == null)
          throw new AssertionError("LoadedFrom cannot be null."); 
        paramAction.complete(paramBitmap, paramLoadedFrom);
        if (this.loggingEnabled)
          Utils.log("Main", "completed", paramAction.request.logId(), "from " + paramLoadedFrom); 
        return;
      } 
      paramAction.error();
      if (this.loggingEnabled)
        Utils.log("Main", "errored", paramAction.request.logId()); 
    } 
  }
  
  public static void setSingletonInstance(Picasso paramPicasso) {
    // Byte code:
    //   0: ldc com/squareup/picasso/Picasso
    //   2: monitorenter
    //   3: getstatic com/squareup/picasso/Picasso.singleton : Lcom/squareup/picasso/Picasso;
    //   6: ifnull -> 28
    //   9: new java/lang/IllegalStateException
    //   12: astore_0
    //   13: aload_0
    //   14: ldc_w 'Singleton instance already exists.'
    //   17: invokespecial <init> : (Ljava/lang/String;)V
    //   20: aload_0
    //   21: athrow
    //   22: astore_0
    //   23: ldc com/squareup/picasso/Picasso
    //   25: monitorexit
    //   26: aload_0
    //   27: athrow
    //   28: aload_0
    //   29: putstatic com/squareup/picasso/Picasso.singleton : Lcom/squareup/picasso/Picasso;
    //   32: ldc com/squareup/picasso/Picasso
    //   34: monitorexit
    //   35: return
    // Exception table:
    //   from	to	target	type
    //   3	22	22	finally
    //   23	26	22	finally
    //   28	35	22	finally
  }
  
  public static Picasso with(Context paramContext) {
    // Byte code:
    //   0: getstatic com/squareup/picasso/Picasso.singleton : Lcom/squareup/picasso/Picasso;
    //   3: ifnonnull -> 34
    //   6: ldc com/squareup/picasso/Picasso
    //   8: monitorenter
    //   9: getstatic com/squareup/picasso/Picasso.singleton : Lcom/squareup/picasso/Picasso;
    //   12: ifnonnull -> 31
    //   15: new com/squareup/picasso/Picasso$Builder
    //   18: astore_1
    //   19: aload_1
    //   20: aload_0
    //   21: invokespecial <init> : (Landroid/content/Context;)V
    //   24: aload_1
    //   25: invokevirtual build : ()Lcom/squareup/picasso/Picasso;
    //   28: putstatic com/squareup/picasso/Picasso.singleton : Lcom/squareup/picasso/Picasso;
    //   31: ldc com/squareup/picasso/Picasso
    //   33: monitorexit
    //   34: getstatic com/squareup/picasso/Picasso.singleton : Lcom/squareup/picasso/Picasso;
    //   37: areturn
    //   38: astore_0
    //   39: ldc com/squareup/picasso/Picasso
    //   41: monitorexit
    //   42: aload_0
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   9	31	38	finally
    //   31	34	38	finally
    //   39	42	38	finally
  }
  
  public boolean areIndicatorsEnabled() {
    return this.indicatorsEnabled;
  }
  
  public void cancelRequest(ImageView paramImageView) {
    cancelExistingRequest(paramImageView);
  }
  
  public void cancelRequest(RemoteViews paramRemoteViews, int paramInt) {
    cancelExistingRequest(new RemoteViewsAction.RemoteViewsTarget(paramRemoteViews, paramInt));
  }
  
  public void cancelRequest(Target paramTarget) {
    cancelExistingRequest(paramTarget);
  }
  
  public void cancelTag(Object paramObject) {
    Utils.checkMain();
    ArrayList<Action> arrayList = new ArrayList(this.targetToAction.values());
    byte b = 0;
    int i = arrayList.size();
    while (b < i) {
      Action action = arrayList.get(b);
      if (action.getTag().equals(paramObject))
        cancelExistingRequest(action.getTarget()); 
      b++;
    } 
  }
  
  void complete(BitmapHunter paramBitmapHunter) {
    byte b;
    int i = 0;
    Action action = paramBitmapHunter.getAction();
    List<Action> list = paramBitmapHunter.getActions();
    if (list != null && !list.isEmpty()) {
      b = 1;
    } else {
      b = 0;
    } 
    if (action != null || b)
      i = 1; 
    if (i) {
      Uri uri = (paramBitmapHunter.getData()).uri;
      Exception exception = paramBitmapHunter.getException();
      Bitmap bitmap = paramBitmapHunter.getResult();
      LoadedFrom loadedFrom = paramBitmapHunter.getLoadedFrom();
      if (action != null)
        deliverAction(bitmap, loadedFrom, action); 
      if (b) {
        b = 0;
        i = list.size();
        while (b < i) {
          deliverAction(bitmap, loadedFrom, list.get(b));
          b++;
        } 
      } 
      if (this.listener != null && exception != null)
        this.listener.onImageLoadFailed(this, uri, exception); 
    } 
  }
  
  void defer(ImageView paramImageView, DeferredRequestCreator paramDeferredRequestCreator) {
    this.targetToDeferredRequestCreator.put(paramImageView, paramDeferredRequestCreator);
  }
  
  void enqueueAndSubmit(Action<Object> paramAction) {
    Object object = paramAction.getTarget();
    if (object != null && this.targetToAction.get(object) != paramAction) {
      cancelExistingRequest(object);
      this.targetToAction.put(object, paramAction);
    } 
    submit(paramAction);
  }
  
  List<RequestHandler> getRequestHandlers() {
    return this.requestHandlers;
  }
  
  public StatsSnapshot getSnapshot() {
    return this.stats.createSnapshot();
  }
  
  public void invalidate(Uri paramUri) {
    if (paramUri == null)
      throw new IllegalArgumentException("uri == null"); 
    this.cache.clearKeyUri(paramUri.toString());
  }
  
  public void invalidate(File paramFile) {
    if (paramFile == null)
      throw new IllegalArgumentException("file == null"); 
    invalidate(Uri.fromFile(paramFile));
  }
  
  public void invalidate(String paramString) {
    if (paramString == null)
      throw new IllegalArgumentException("path == null"); 
    invalidate(Uri.parse(paramString));
  }
  
  @Deprecated
  public boolean isDebugging() {
    return (areIndicatorsEnabled() && isLoggingEnabled());
  }
  
  public boolean isLoggingEnabled() {
    return this.loggingEnabled;
  }
  
  public RequestCreator load(int paramInt) {
    if (paramInt == 0)
      throw new IllegalArgumentException("Resource ID must not be zero."); 
    return new RequestCreator(this, null, paramInt);
  }
  
  public RequestCreator load(Uri paramUri) {
    return new RequestCreator(this, paramUri, 0);
  }
  
  public RequestCreator load(File paramFile) {
    return (paramFile == null) ? new RequestCreator(this, null, 0) : load(Uri.fromFile(paramFile));
  }
  
  public RequestCreator load(String paramString) {
    if (paramString == null)
      return new RequestCreator(this, null, 0); 
    if (paramString.trim().length() == 0)
      throw new IllegalArgumentException("Path must not be empty."); 
    return load(Uri.parse(paramString));
  }
  
  public void pauseTag(Object paramObject) {
    this.dispatcher.dispatchPauseTag(paramObject);
  }
  
  Bitmap quickMemoryCacheCheck(String paramString) {
    Bitmap bitmap = this.cache.get(paramString);
    if (bitmap != null) {
      this.stats.dispatchCacheHit();
      return bitmap;
    } 
    this.stats.dispatchCacheMiss();
    return bitmap;
  }
  
  void resumeAction(Action paramAction) {
    Bitmap bitmap = null;
    if (MemoryPolicy.shouldReadFromMemoryCache(paramAction.memoryPolicy))
      bitmap = quickMemoryCacheCheck(paramAction.getKey()); 
    if (bitmap != null) {
      deliverAction(bitmap, LoadedFrom.MEMORY, paramAction);
      if (this.loggingEnabled)
        Utils.log("Main", "completed", paramAction.request.logId(), "from " + LoadedFrom.MEMORY); 
      return;
    } 
    enqueueAndSubmit(paramAction);
    if (this.loggingEnabled)
      Utils.log("Main", "resumed", paramAction.request.logId()); 
  }
  
  public void resumeTag(Object paramObject) {
    this.dispatcher.dispatchResumeTag(paramObject);
  }
  
  @Deprecated
  public void setDebugging(boolean paramBoolean) {
    setIndicatorsEnabled(paramBoolean);
  }
  
  public void setIndicatorsEnabled(boolean paramBoolean) {
    this.indicatorsEnabled = paramBoolean;
  }
  
  public void setLoggingEnabled(boolean paramBoolean) {
    this.loggingEnabled = paramBoolean;
  }
  
  public void shutdown() {
    if (this == singleton)
      throw new UnsupportedOperationException("Default singleton instance cannot be shutdown."); 
    if (!this.shutdown) {
      this.cache.clear();
      this.cleanupThread.shutdown();
      this.stats.shutdown();
      this.dispatcher.shutdown();
      Iterator<DeferredRequestCreator> iterator = this.targetToDeferredRequestCreator.values().iterator();
      while (iterator.hasNext())
        ((DeferredRequestCreator)iterator.next()).cancel(); 
      this.targetToDeferredRequestCreator.clear();
      this.shutdown = true;
    } 
  }
  
  void submit(Action paramAction) {
    this.dispatcher.dispatchSubmit(paramAction);
  }
  
  Request transformRequest(Request paramRequest) {
    Request request = this.requestTransformer.transformRequest(paramRequest);
    if (request == null)
      throw new IllegalStateException("Request transformer " + this.requestTransformer.getClass().getCanonicalName() + " returned null for " + paramRequest); 
    return request;
  }
  
  public static class Builder {
    private Cache cache;
    
    private final Context context;
    
    private Bitmap.Config defaultBitmapConfig;
    
    private Downloader downloader;
    
    private boolean indicatorsEnabled;
    
    private Picasso.Listener listener;
    
    private boolean loggingEnabled;
    
    private List<RequestHandler> requestHandlers;
    
    private ExecutorService service;
    
    private Picasso.RequestTransformer transformer;
    
    public Builder(Context param1Context) {
      if (param1Context == null)
        throw new IllegalArgumentException("Context must not be null."); 
      this.context = param1Context.getApplicationContext();
    }
    
    public Builder addRequestHandler(RequestHandler param1RequestHandler) {
      if (param1RequestHandler == null)
        throw new IllegalArgumentException("RequestHandler must not be null."); 
      if (this.requestHandlers == null)
        this.requestHandlers = new ArrayList<RequestHandler>(); 
      if (this.requestHandlers.contains(param1RequestHandler))
        throw new IllegalStateException("RequestHandler already registered."); 
      this.requestHandlers.add(param1RequestHandler);
      return this;
    }
    
    public Picasso build() {
      Context context = this.context;
      if (this.downloader == null)
        this.downloader = Utils.createDefaultDownloader(context); 
      if (this.cache == null)
        this.cache = new LruCache(context); 
      if (this.service == null)
        this.service = new PicassoExecutorService(); 
      if (this.transformer == null)
        this.transformer = Picasso.RequestTransformer.IDENTITY; 
      Stats stats = new Stats(this.cache);
      return new Picasso(context, new Dispatcher(context, this.service, Picasso.HANDLER, this.downloader, this.cache, stats), this.cache, this.listener, this.transformer, this.requestHandlers, stats, this.defaultBitmapConfig, this.indicatorsEnabled, this.loggingEnabled);
    }
    
    @Deprecated
    public Builder debugging(boolean param1Boolean) {
      return indicatorsEnabled(param1Boolean);
    }
    
    public Builder defaultBitmapConfig(Bitmap.Config param1Config) {
      if (param1Config == null)
        throw new IllegalArgumentException("Bitmap config must not be null."); 
      this.defaultBitmapConfig = param1Config;
      return this;
    }
    
    public Builder downloader(Downloader param1Downloader) {
      if (param1Downloader == null)
        throw new IllegalArgumentException("Downloader must not be null."); 
      if (this.downloader != null)
        throw new IllegalStateException("Downloader already set."); 
      this.downloader = param1Downloader;
      return this;
    }
    
    public Builder executor(ExecutorService param1ExecutorService) {
      if (param1ExecutorService == null)
        throw new IllegalArgumentException("Executor service must not be null."); 
      if (this.service != null)
        throw new IllegalStateException("Executor service already set."); 
      this.service = param1ExecutorService;
      return this;
    }
    
    public Builder indicatorsEnabled(boolean param1Boolean) {
      this.indicatorsEnabled = param1Boolean;
      return this;
    }
    
    public Builder listener(Picasso.Listener param1Listener) {
      if (param1Listener == null)
        throw new IllegalArgumentException("Listener must not be null."); 
      if (this.listener != null)
        throw new IllegalStateException("Listener already set."); 
      this.listener = param1Listener;
      return this;
    }
    
    public Builder loggingEnabled(boolean param1Boolean) {
      this.loggingEnabled = param1Boolean;
      return this;
    }
    
    public Builder memoryCache(Cache param1Cache) {
      if (param1Cache == null)
        throw new IllegalArgumentException("Memory cache must not be null."); 
      if (this.cache != null)
        throw new IllegalStateException("Memory cache already set."); 
      this.cache = param1Cache;
      return this;
    }
    
    public Builder requestTransformer(Picasso.RequestTransformer param1RequestTransformer) {
      if (param1RequestTransformer == null)
        throw new IllegalArgumentException("Transformer must not be null."); 
      if (this.transformer != null)
        throw new IllegalStateException("Transformer already set."); 
      this.transformer = param1RequestTransformer;
      return this;
    }
  }
  
  private static class CleanupThread extends Thread {
    private final Handler handler;
    
    private final ReferenceQueue<Object> referenceQueue;
    
    CleanupThread(ReferenceQueue<Object> param1ReferenceQueue, Handler param1Handler) {
      this.referenceQueue = param1ReferenceQueue;
      this.handler = param1Handler;
      setDaemon(true);
      setName("Picasso-refQueue");
    }
    
    public void run() {
      Process.setThreadPriority(10);
      while (true) {
        try {
          Action.RequestWeakReference requestWeakReference = (Action.RequestWeakReference)this.referenceQueue.remove(1000L);
          Message message = this.handler.obtainMessage();
          if (requestWeakReference != null) {
            message.what = 3;
            message.obj = requestWeakReference.action;
            this.handler.sendMessage(message);
            continue;
          } 
          message.recycle();
          continue;
        } catch (InterruptedException interruptedException) {
        
        } catch (Exception exception) {
          this.handler.post(new Runnable() {
                public void run() {
                  throw new RuntimeException(e);
                }
              });
        } 
        return;
      } 
    }
    
    void shutdown() {
      interrupt();
    }
  }
  
  class null implements Runnable {
    public void run() {
      throw new RuntimeException(e);
    }
  }
  
  public static interface Listener {
    void onImageLoadFailed(Picasso param1Picasso, Uri param1Uri, Exception param1Exception);
  }
  
  public enum LoadedFrom {
    DISK,
    MEMORY(-16711936),
    NETWORK(-16711936);
    
    final int debugColor;
    
    static {
      $VALUES = new LoadedFrom[] { MEMORY, DISK, NETWORK };
    }
    
    LoadedFrom(int param1Int1) {
      this.debugColor = param1Int1;
    }
  }
  
  public enum Priority {
    HIGH(-16711936),
    LOW,
    NORMAL;
    
    static {
      $VALUES = new Priority[] { LOW, NORMAL, HIGH };
    }
  }
  
  public static interface RequestTransformer {
    public static final RequestTransformer IDENTITY = new RequestTransformer() {
        public Request transformRequest(Request param2Request) {
          return param2Request;
        }
      };
    
    Request transformRequest(Request param1Request);
  }
  
  static final class null implements RequestTransformer {
    public Request transformRequest(Request param1Request) {
      return param1Request;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/squareup/picasso/Picasso.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */