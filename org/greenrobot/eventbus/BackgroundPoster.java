package org.greenrobot.eventbus;

final class BackgroundPoster implements Runnable {
  private final EventBus eventBus;
  
  private volatile boolean executorRunning;
  
  private final PendingPostQueue queue;
  
  BackgroundPoster(EventBus paramEventBus) {
    this.eventBus = paramEventBus;
    this.queue = new PendingPostQueue();
  }
  
  public void enqueue(Subscription paramSubscription, Object paramObject) {
    // Byte code:
    //   0: aload_1
    //   1: aload_2
    //   2: invokestatic obtainPendingPost : (Lorg/greenrobot/eventbus/Subscription;Ljava/lang/Object;)Lorg/greenrobot/eventbus/PendingPost;
    //   5: astore_1
    //   6: aload_0
    //   7: monitorenter
    //   8: aload_0
    //   9: getfield queue : Lorg/greenrobot/eventbus/PendingPostQueue;
    //   12: aload_1
    //   13: invokevirtual enqueue : (Lorg/greenrobot/eventbus/PendingPost;)V
    //   16: aload_0
    //   17: getfield executorRunning : Z
    //   20: ifne -> 41
    //   23: aload_0
    //   24: iconst_1
    //   25: putfield executorRunning : Z
    //   28: aload_0
    //   29: getfield eventBus : Lorg/greenrobot/eventbus/EventBus;
    //   32: invokevirtual getExecutorService : ()Ljava/util/concurrent/ExecutorService;
    //   35: aload_0
    //   36: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   41: aload_0
    //   42: monitorexit
    //   43: return
    //   44: astore_1
    //   45: aload_0
    //   46: monitorexit
    //   47: aload_1
    //   48: athrow
    // Exception table:
    //   from	to	target	type
    //   8	41	44	finally
    //   41	43	44	finally
    //   45	47	44	finally
  }
  
  public void run() {
    // Byte code:
    //   0: aload_0
    //   1: getfield queue : Lorg/greenrobot/eventbus/PendingPostQueue;
    //   4: sipush #1000
    //   7: invokevirtual poll : (I)Lorg/greenrobot/eventbus/PendingPost;
    //   10: astore_1
    //   11: aload_1
    //   12: astore_2
    //   13: aload_1
    //   14: ifnonnull -> 46
    //   17: aload_0
    //   18: monitorenter
    //   19: aload_0
    //   20: getfield queue : Lorg/greenrobot/eventbus/PendingPostQueue;
    //   23: invokevirtual poll : ()Lorg/greenrobot/eventbus/PendingPost;
    //   26: astore_2
    //   27: aload_2
    //   28: ifnonnull -> 44
    //   31: aload_0
    //   32: iconst_0
    //   33: putfield executorRunning : Z
    //   36: aload_0
    //   37: monitorexit
    //   38: aload_0
    //   39: iconst_0
    //   40: putfield executorRunning : Z
    //   43: return
    //   44: aload_0
    //   45: monitorexit
    //   46: aload_0
    //   47: getfield eventBus : Lorg/greenrobot/eventbus/EventBus;
    //   50: aload_2
    //   51: invokevirtual invokeSubscriber : (Lorg/greenrobot/eventbus/PendingPost;)V
    //   54: goto -> 0
    //   57: astore_2
    //   58: new java/lang/StringBuilder
    //   61: astore_1
    //   62: aload_1
    //   63: invokespecial <init> : ()V
    //   66: ldc 'Event'
    //   68: aload_1
    //   69: invokestatic currentThread : ()Ljava/lang/Thread;
    //   72: invokevirtual getName : ()Ljava/lang/String;
    //   75: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   78: ldc ' was interruppted'
    //   80: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   83: invokevirtual toString : ()Ljava/lang/String;
    //   86: aload_2
    //   87: invokestatic w : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   90: pop
    //   91: aload_0
    //   92: iconst_0
    //   93: putfield executorRunning : Z
    //   96: goto -> 43
    //   99: astore_2
    //   100: aload_0
    //   101: monitorexit
    //   102: aload_2
    //   103: athrow
    //   104: astore_2
    //   105: aload_0
    //   106: iconst_0
    //   107: putfield executorRunning : Z
    //   110: aload_2
    //   111: athrow
    // Exception table:
    //   from	to	target	type
    //   0	11	57	java/lang/InterruptedException
    //   0	11	104	finally
    //   17	19	57	java/lang/InterruptedException
    //   17	19	104	finally
    //   19	27	99	finally
    //   31	38	99	finally
    //   44	46	99	finally
    //   46	54	57	java/lang/InterruptedException
    //   46	54	104	finally
    //   58	91	104	finally
    //   100	102	99	finally
    //   102	104	57	java/lang/InterruptedException
    //   102	104	104	finally
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/eventbus/BackgroundPoster.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */