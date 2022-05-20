package org.greenrobot.eventbus;

import android.os.Looper;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

public class EventBus {
  private static final EventBusBuilder DEFAULT_BUILDER;
  
  public static String TAG = "EventBus";
  
  static volatile EventBus defaultInstance;
  
  private static final Map<Class<?>, List<Class<?>>> eventTypesCache;
  
  private final AsyncPoster asyncPoster;
  
  private final BackgroundPoster backgroundPoster;
  
  private final ThreadLocal<PostingThreadState> currentPostingThreadState;
  
  private final boolean eventInheritance;
  
  private final ExecutorService executorService;
  
  private final int indexCount;
  
  private final boolean logNoSubscriberMessages;
  
  private final boolean logSubscriberExceptions;
  
  private final HandlerPoster mainThreadPoster;
  
  private final boolean sendNoSubscriberEvent;
  
  private final boolean sendSubscriberExceptionEvent;
  
  private final Map<Class<?>, Object> stickyEvents;
  
  private final SubscriberMethodFinder subscriberMethodFinder;
  
  private final Map<Class<?>, CopyOnWriteArrayList<Subscription>> subscriptionsByEventType;
  
  private final boolean throwSubscriberException;
  
  private final Map<Object, List<Class<?>>> typesBySubscriber;
  
  static {
    DEFAULT_BUILDER = new EventBusBuilder();
    eventTypesCache = new HashMap<Class<?>, List<Class<?>>>();
  }
  
  public EventBus() {
    this(DEFAULT_BUILDER);
  }
  
  EventBus(EventBusBuilder paramEventBusBuilder) {
    boolean bool;
    this.currentPostingThreadState = new ThreadLocal<PostingThreadState>() {
        protected EventBus.PostingThreadState initialValue() {
          return new EventBus.PostingThreadState();
        }
      };
    this.subscriptionsByEventType = new HashMap<Class<?>, CopyOnWriteArrayList<Subscription>>();
    this.typesBySubscriber = new HashMap<Object, List<Class<?>>>();
    this.stickyEvents = new ConcurrentHashMap<Class<?>, Object>();
    this.mainThreadPoster = new HandlerPoster(this, Looper.getMainLooper(), 10);
    this.backgroundPoster = new BackgroundPoster(this);
    this.asyncPoster = new AsyncPoster(this);
    if (paramEventBusBuilder.subscriberInfoIndexes != null) {
      bool = paramEventBusBuilder.subscriberInfoIndexes.size();
    } else {
      bool = false;
    } 
    this.indexCount = bool;
    this.subscriberMethodFinder = new SubscriberMethodFinder(paramEventBusBuilder.subscriberInfoIndexes, paramEventBusBuilder.strictMethodVerification, paramEventBusBuilder.ignoreGeneratedIndex);
    this.logSubscriberExceptions = paramEventBusBuilder.logSubscriberExceptions;
    this.logNoSubscriberMessages = paramEventBusBuilder.logNoSubscriberMessages;
    this.sendSubscriberExceptionEvent = paramEventBusBuilder.sendSubscriberExceptionEvent;
    this.sendNoSubscriberEvent = paramEventBusBuilder.sendNoSubscriberEvent;
    this.throwSubscriberException = paramEventBusBuilder.throwSubscriberException;
    this.eventInheritance = paramEventBusBuilder.eventInheritance;
    this.executorService = paramEventBusBuilder.executorService;
  }
  
  static void addInterfaces(List<Class<?>> paramList, Class<?>[] paramArrayOfClass) {
    int i = paramArrayOfClass.length;
    for (byte b = 0; b < i; b++) {
      Class<?> clazz = paramArrayOfClass[b];
      if (!paramList.contains(clazz)) {
        paramList.add(clazz);
        addInterfaces(paramList, clazz.getInterfaces());
      } 
    } 
  }
  
  public static EventBusBuilder builder() {
    return new EventBusBuilder();
  }
  
  private void checkPostStickyEventToSubscription(Subscription paramSubscription, Object paramObject) {
    if (paramObject != null) {
      boolean bool;
      if (Looper.getMainLooper() == Looper.myLooper()) {
        bool = true;
      } else {
        bool = false;
      } 
      postToSubscription(paramSubscription, paramObject, bool);
    } 
  }
  
  public static void clearCaches() {
    SubscriberMethodFinder.clearCaches();
    eventTypesCache.clear();
  }
  
  public static EventBus getDefault() {
    // Byte code:
    //   0: getstatic org/greenrobot/eventbus/EventBus.defaultInstance : Lorg/greenrobot/eventbus/EventBus;
    //   3: ifnonnull -> 30
    //   6: ldc org/greenrobot/eventbus/EventBus
    //   8: monitorenter
    //   9: getstatic org/greenrobot/eventbus/EventBus.defaultInstance : Lorg/greenrobot/eventbus/EventBus;
    //   12: ifnonnull -> 27
    //   15: new org/greenrobot/eventbus/EventBus
    //   18: astore_0
    //   19: aload_0
    //   20: invokespecial <init> : ()V
    //   23: aload_0
    //   24: putstatic org/greenrobot/eventbus/EventBus.defaultInstance : Lorg/greenrobot/eventbus/EventBus;
    //   27: ldc org/greenrobot/eventbus/EventBus
    //   29: monitorexit
    //   30: getstatic org/greenrobot/eventbus/EventBus.defaultInstance : Lorg/greenrobot/eventbus/EventBus;
    //   33: areturn
    //   34: astore_0
    //   35: ldc org/greenrobot/eventbus/EventBus
    //   37: monitorexit
    //   38: aload_0
    //   39: athrow
    // Exception table:
    //   from	to	target	type
    //   9	27	34	finally
    //   27	30	34	finally
    //   35	38	34	finally
  }
  
  private void handleSubscriberException(Subscription paramSubscription, Object paramObject, Throwable paramThrowable) {
    SubscriberExceptionEvent subscriberExceptionEvent;
    if (paramObject instanceof SubscriberExceptionEvent) {
      if (this.logSubscriberExceptions) {
        Log.e(TAG, "SubscriberExceptionEvent subscriber " + paramSubscription.subscriber.getClass() + " threw an exception", paramThrowable);
        subscriberExceptionEvent = (SubscriberExceptionEvent)paramObject;
        Log.e(TAG, "Initial event " + subscriberExceptionEvent.causingEvent + " caused exception in " + subscriberExceptionEvent.causingSubscriber, subscriberExceptionEvent.throwable);
      } 
      return;
    } 
    if (this.throwSubscriberException)
      throw new EventBusException("Invoking subscriber failed", paramThrowable); 
    if (this.logSubscriberExceptions)
      Log.e(TAG, "Could not dispatch event: " + paramObject.getClass() + " to subscribing class " + ((Subscription)subscriberExceptionEvent).subscriber.getClass(), paramThrowable); 
    if (this.sendSubscriberExceptionEvent)
      post(new SubscriberExceptionEvent(this, paramThrowable, paramObject, ((Subscription)subscriberExceptionEvent).subscriber)); 
  }
  
  private static List<Class<?>> lookupAllEventTypes(Class<?> paramClass) {
    synchronized (eventTypesCache) {
      List<Class<?>> list1 = eventTypesCache.get(paramClass);
      List<Class<?>> list2 = list1;
      if (list1 == null) {
        list1 = new ArrayList();
        super();
        for (Class<?> clazz = paramClass; clazz != null; clazz = clazz.getSuperclass()) {
          list1.add(clazz);
          addInterfaces(list1, clazz.getInterfaces());
        } 
        eventTypesCache.put(paramClass, list1);
        list2 = list1;
      } 
      return list2;
    } 
  }
  
  private void postSingleEvent(Object paramObject, PostingThreadState paramPostingThreadState) throws Error {
    boolean bool2;
    Class<?> clazz = paramObject.getClass();
    boolean bool1 = false;
    if (this.eventInheritance) {
      List<Class<?>> list = lookupAllEventTypes(clazz);
      int i = list.size();
      byte b = 0;
      while (true) {
        bool2 = bool1;
        if (b < i) {
          bool1 |= postSingleEventForEventType(paramObject, paramPostingThreadState, list.get(b));
          b++;
          continue;
        } 
        break;
      } 
    } else {
      bool2 = postSingleEventForEventType(paramObject, paramPostingThreadState, clazz);
    } 
    if (!bool2) {
      if (this.logNoSubscriberMessages)
        Log.d(TAG, "No subscribers registered for event " + clazz); 
      if (this.sendNoSubscriberEvent && clazz != NoSubscriberEvent.class && clazz != SubscriberExceptionEvent.class)
        post(new NoSubscriberEvent(this, paramObject)); 
    } 
  }
  
  private boolean postSingleEventForEventType(Object paramObject, PostingThreadState paramPostingThreadState, Class<?> paramClass) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #4
    //   3: aload_0
    //   4: monitorenter
    //   5: aload_0
    //   6: getfield subscriptionsByEventType : Ljava/util/Map;
    //   9: aload_3
    //   10: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   15: checkcast java/util/concurrent/CopyOnWriteArrayList
    //   18: astore_3
    //   19: aload_0
    //   20: monitorexit
    //   21: iload #4
    //   23: istore #5
    //   25: aload_3
    //   26: ifnull -> 116
    //   29: iload #4
    //   31: istore #5
    //   33: aload_3
    //   34: invokevirtual isEmpty : ()Z
    //   37: ifne -> 116
    //   40: aload_3
    //   41: invokevirtual iterator : ()Ljava/util/Iterator;
    //   44: astore #6
    //   46: aload #6
    //   48: invokeinterface hasNext : ()Z
    //   53: ifeq -> 113
    //   56: aload #6
    //   58: invokeinterface next : ()Ljava/lang/Object;
    //   63: checkcast org/greenrobot/eventbus/Subscription
    //   66: astore_3
    //   67: aload_2
    //   68: aload_1
    //   69: putfield event : Ljava/lang/Object;
    //   72: aload_2
    //   73: aload_3
    //   74: putfield subscription : Lorg/greenrobot/eventbus/Subscription;
    //   77: aload_0
    //   78: aload_3
    //   79: aload_1
    //   80: aload_2
    //   81: getfield isMainThread : Z
    //   84: invokespecial postToSubscription : (Lorg/greenrobot/eventbus/Subscription;Ljava/lang/Object;Z)V
    //   87: aload_2
    //   88: getfield canceled : Z
    //   91: istore #5
    //   93: aload_2
    //   94: aconst_null
    //   95: putfield event : Ljava/lang/Object;
    //   98: aload_2
    //   99: aconst_null
    //   100: putfield subscription : Lorg/greenrobot/eventbus/Subscription;
    //   103: aload_2
    //   104: iconst_0
    //   105: putfield canceled : Z
    //   108: iload #5
    //   110: ifeq -> 46
    //   113: iconst_1
    //   114: istore #5
    //   116: iload #5
    //   118: ireturn
    //   119: astore_1
    //   120: aload_0
    //   121: monitorexit
    //   122: aload_1
    //   123: athrow
    //   124: astore_1
    //   125: aload_2
    //   126: aconst_null
    //   127: putfield event : Ljava/lang/Object;
    //   130: aload_2
    //   131: aconst_null
    //   132: putfield subscription : Lorg/greenrobot/eventbus/Subscription;
    //   135: aload_2
    //   136: iconst_0
    //   137: putfield canceled : Z
    //   140: aload_1
    //   141: athrow
    // Exception table:
    //   from	to	target	type
    //   5	21	119	finally
    //   77	93	124	finally
    //   120	122	119	finally
  }
  
  private void postToSubscription(Subscription paramSubscription, Object paramObject, boolean paramBoolean) {
    switch (paramSubscription.subscriberMethod.threadMode) {
      default:
        throw new IllegalStateException("Unknown thread mode: " + paramSubscription.subscriberMethod.threadMode);
      case POSTING:
        invokeSubscriber(paramSubscription, paramObject);
        return;
      case MAIN:
        if (paramBoolean) {
          invokeSubscriber(paramSubscription, paramObject);
          return;
        } 
        this.mainThreadPoster.enqueue(paramSubscription, paramObject);
        return;
      case BACKGROUND:
        if (paramBoolean) {
          this.backgroundPoster.enqueue(paramSubscription, paramObject);
          return;
        } 
        invokeSubscriber(paramSubscription, paramObject);
        return;
      case ASYNC:
        break;
    } 
    this.asyncPoster.enqueue(paramSubscription, paramObject);
  }
  
  private void subscribe(Map.Entry<Class<?>, Object> paramObject, SubscriberMethod paramSubscriberMethod) {
    CopyOnWriteArrayList<Subscription> copyOnWriteArrayList2;
    Class<?> clazz = paramSubscriberMethod.eventType;
    Subscription subscription = new Subscription(paramObject, paramSubscriberMethod);
    CopyOnWriteArrayList<Subscription> copyOnWriteArrayList1 = this.subscriptionsByEventType.get(clazz);
    if (copyOnWriteArrayList1 == null) {
      copyOnWriteArrayList2 = new CopyOnWriteArrayList();
      this.subscriptionsByEventType.put(clazz, copyOnWriteArrayList2);
    } else {
      copyOnWriteArrayList2 = copyOnWriteArrayList1;
      if (copyOnWriteArrayList1.contains(subscription))
        throw new EventBusException("Subscriber " + paramObject.getClass() + " already registered to event " + clazz); 
    } 
    int i = copyOnWriteArrayList2.size();
    byte b = 0;
    while (true) {
      if (b <= i)
        if (b == i || paramSubscriberMethod.priority > ((Subscription)copyOnWriteArrayList2.get(b)).subscriberMethod.priority) {
          copyOnWriteArrayList2.add(b, subscription);
        } else {
          b++;
          continue;
        }  
      List<Class<?>> list1 = this.typesBySubscriber.get(paramObject);
      List<Class<?>> list2 = list1;
      if (list1 == null) {
        list2 = new ArrayList();
        this.typesBySubscriber.put(paramObject, list2);
      } 
      list2.add(clazz);
      if (paramSubscriberMethod.sticky) {
        if (this.eventInheritance) {
          for (Map.Entry<Class<?>, Object> paramObject : this.stickyEvents.entrySet()) {
            if (clazz.isAssignableFrom((Class)paramObject.getKey()))
              checkPostStickyEventToSubscription(subscription, paramObject.getValue()); 
          } 
          break;
        } 
        checkPostStickyEventToSubscription(subscription, this.stickyEvents.get(clazz));
      } 
      break;
    } 
  }
  
  private void unsubscribeByEventType(Object paramObject, Class<?> paramClass) {
    List<Subscription> list = this.subscriptionsByEventType.get(paramClass);
    if (list != null) {
      int i = list.size();
      int j = 0;
      while (j < i) {
        Subscription subscription = list.get(j);
        int k = j;
        int m = i;
        if (subscription.subscriber == paramObject) {
          subscription.active = false;
          list.remove(j);
          k = j - 1;
          m = i - 1;
        } 
        j = k + 1;
        i = m;
      } 
    } 
  }
  
  public void cancelEventDelivery(Object paramObject) {
    PostingThreadState postingThreadState = this.currentPostingThreadState.get();
    if (!postingThreadState.isPosting)
      throw new EventBusException("This method may only be called from inside event handling methods on the posting thread"); 
    if (paramObject == null)
      throw new EventBusException("Event may not be null"); 
    if (postingThreadState.event != paramObject)
      throw new EventBusException("Only the currently handled event may be aborted"); 
    if (postingThreadState.subscription.subscriberMethod.threadMode != ThreadMode.POSTING)
      throw new EventBusException(" event handlers may only abort the incoming event"); 
    postingThreadState.canceled = true;
  }
  
  ExecutorService getExecutorService() {
    return this.executorService;
  }
  
  public <T> T getStickyEvent(Class<T> paramClass) {
    synchronized (this.stickyEvents) {
      paramClass = (Class<T>)paramClass.cast(this.stickyEvents.get(paramClass));
      return (T)paramClass;
    } 
  }
  
  public boolean hasSubscriberForEvent(Class<?> paramClass) {
    // Byte code:
    //   0: aload_1
    //   1: invokestatic lookupAllEventTypes : (Ljava/lang/Class;)Ljava/util/List;
    //   4: astore_1
    //   5: aload_1
    //   6: ifnull -> 85
    //   9: aload_1
    //   10: invokeinterface size : ()I
    //   15: istore_2
    //   16: iconst_0
    //   17: istore_3
    //   18: iload_3
    //   19: iload_2
    //   20: if_icmpge -> 85
    //   23: aload_1
    //   24: iload_3
    //   25: invokeinterface get : (I)Ljava/lang/Object;
    //   30: checkcast java/lang/Class
    //   33: astore #4
    //   35: aload_0
    //   36: monitorenter
    //   37: aload_0
    //   38: getfield subscriptionsByEventType : Ljava/util/Map;
    //   41: aload #4
    //   43: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   48: checkcast java/util/concurrent/CopyOnWriteArrayList
    //   51: astore #4
    //   53: aload_0
    //   54: monitorexit
    //   55: aload #4
    //   57: ifnull -> 79
    //   60: aload #4
    //   62: invokevirtual isEmpty : ()Z
    //   65: ifne -> 79
    //   68: iconst_1
    //   69: istore #5
    //   71: iload #5
    //   73: ireturn
    //   74: astore_1
    //   75: aload_0
    //   76: monitorexit
    //   77: aload_1
    //   78: athrow
    //   79: iinc #3, 1
    //   82: goto -> 18
    //   85: iconst_0
    //   86: istore #5
    //   88: goto -> 71
    // Exception table:
    //   from	to	target	type
    //   37	55	74	finally
    //   75	77	74	finally
  }
  
  void invokeSubscriber(PendingPost paramPendingPost) {
    Object object = paramPendingPost.event;
    Subscription subscription = paramPendingPost.subscription;
    PendingPost.releasePendingPost(paramPendingPost);
    if (subscription.active)
      invokeSubscriber(subscription, object); 
  }
  
  void invokeSubscriber(Subscription paramSubscription, Object paramObject) {
    try {
      paramSubscription.subscriberMethod.method.invoke(paramSubscription.subscriber, new Object[] { paramObject });
      return;
    } catch (InvocationTargetException invocationTargetException) {
      handleSubscriberException(paramSubscription, paramObject, invocationTargetException.getCause());
      return;
    } catch (IllegalAccessException illegalAccessException) {
      throw new IllegalStateException("Unexpected exception", illegalAccessException);
    } 
  }
  
  public boolean isRegistered(Object paramObject) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield typesBySubscriber : Ljava/util/Map;
    //   6: aload_1
    //   7: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   12: istore_2
    //   13: aload_0
    //   14: monitorexit
    //   15: iload_2
    //   16: ireturn
    //   17: astore_1
    //   18: aload_0
    //   19: monitorexit
    //   20: aload_1
    //   21: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	17	finally
  }
  
  public void post(Object paramObject) {
    PostingThreadState postingThreadState = this.currentPostingThreadState.get();
    List<Object> list = postingThreadState.eventQueue;
    list.add(paramObject);
    if (!postingThreadState.isPosting) {
      boolean bool;
      if (Looper.getMainLooper() == Looper.myLooper()) {
        bool = true;
      } else {
        bool = false;
      } 
      postingThreadState.isMainThread = bool;
      postingThreadState.isPosting = true;
      if (postingThreadState.canceled)
        throw new EventBusException("Internal error. Abort state was not reset"); 
      try {
        while (!list.isEmpty())
          postSingleEvent(list.remove(0), postingThreadState); 
      } finally {
        postingThreadState.isPosting = false;
        postingThreadState.isMainThread = false;
      } 
    } 
  }
  
  public void postSticky(Object paramObject) {
    synchronized (this.stickyEvents) {
      this.stickyEvents.put(paramObject.getClass(), paramObject);
      post(paramObject);
      return;
    } 
  }
  
  public void register(Object paramObject) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getClass : ()Ljava/lang/Class;
    //   4: astore_2
    //   5: aload_0
    //   6: getfield subscriberMethodFinder : Lorg/greenrobot/eventbus/SubscriberMethodFinder;
    //   9: aload_2
    //   10: invokevirtual findSubscriberMethods : (Ljava/lang/Class;)Ljava/util/List;
    //   13: astore_2
    //   14: aload_0
    //   15: monitorenter
    //   16: aload_2
    //   17: invokeinterface iterator : ()Ljava/util/Iterator;
    //   22: astore_2
    //   23: aload_2
    //   24: invokeinterface hasNext : ()Z
    //   29: ifeq -> 54
    //   32: aload_0
    //   33: aload_1
    //   34: aload_2
    //   35: invokeinterface next : ()Ljava/lang/Object;
    //   40: checkcast org/greenrobot/eventbus/SubscriberMethod
    //   43: invokespecial subscribe : (Ljava/lang/Object;Lorg/greenrobot/eventbus/SubscriberMethod;)V
    //   46: goto -> 23
    //   49: astore_1
    //   50: aload_0
    //   51: monitorexit
    //   52: aload_1
    //   53: athrow
    //   54: aload_0
    //   55: monitorexit
    //   56: return
    // Exception table:
    //   from	to	target	type
    //   16	23	49	finally
    //   23	46	49	finally
    //   50	52	49	finally
    //   54	56	49	finally
  }
  
  public void removeAllStickyEvents() {
    synchronized (this.stickyEvents) {
      this.stickyEvents.clear();
      return;
    } 
  }
  
  public <T> T removeStickyEvent(Class<T> paramClass) {
    synchronized (this.stickyEvents) {
      paramClass = (Class<T>)paramClass.cast(this.stickyEvents.remove(paramClass));
      return (T)paramClass;
    } 
  }
  
  public boolean removeStickyEvent(Object paramObject) {
    synchronized (this.stickyEvents) {
      Class<?> clazz = paramObject.getClass();
      if (paramObject.equals(this.stickyEvents.get(clazz))) {
        this.stickyEvents.remove(clazz);
        return true;
      } 
      return false;
    } 
  }
  
  public String toString() {
    return "EventBus[indexCount=" + this.indexCount + ", eventInheritance=" + this.eventInheritance + "]";
  }
  
  public void unregister(Object paramObject) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield typesBySubscriber : Ljava/util/Map;
    //   6: aload_1
    //   7: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   12: checkcast java/util/List
    //   15: astore_2
    //   16: aload_2
    //   17: ifnull -> 72
    //   20: aload_2
    //   21: invokeinterface iterator : ()Ljava/util/Iterator;
    //   26: astore_2
    //   27: aload_2
    //   28: invokeinterface hasNext : ()Z
    //   33: ifeq -> 58
    //   36: aload_0
    //   37: aload_1
    //   38: aload_2
    //   39: invokeinterface next : ()Ljava/lang/Object;
    //   44: checkcast java/lang/Class
    //   47: invokespecial unsubscribeByEventType : (Ljava/lang/Object;Ljava/lang/Class;)V
    //   50: goto -> 27
    //   53: astore_1
    //   54: aload_0
    //   55: monitorexit
    //   56: aload_1
    //   57: athrow
    //   58: aload_0
    //   59: getfield typesBySubscriber : Ljava/util/Map;
    //   62: aload_1
    //   63: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   68: pop
    //   69: aload_0
    //   70: monitorexit
    //   71: return
    //   72: getstatic org/greenrobot/eventbus/EventBus.TAG : Ljava/lang/String;
    //   75: astore_2
    //   76: new java/lang/StringBuilder
    //   79: astore_3
    //   80: aload_3
    //   81: invokespecial <init> : ()V
    //   84: aload_2
    //   85: aload_3
    //   86: ldc_w 'Subscriber to unregister was not registered before: '
    //   89: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   92: aload_1
    //   93: invokevirtual getClass : ()Ljava/lang/Class;
    //   96: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   99: invokevirtual toString : ()Ljava/lang/String;
    //   102: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
    //   105: pop
    //   106: goto -> 69
    // Exception table:
    //   from	to	target	type
    //   2	16	53	finally
    //   20	27	53	finally
    //   27	50	53	finally
    //   58	69	53	finally
    //   72	106	53	finally
  }
  
  static interface PostCallback {
    void onPostCompleted(List<SubscriberExceptionEvent> param1List);
  }
  
  static final class PostingThreadState {
    boolean canceled;
    
    Object event;
    
    final List<Object> eventQueue = new ArrayList();
    
    boolean isMainThread;
    
    boolean isPosting;
    
    Subscription subscription;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/eventbus/EventBus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */