package org.greenrobot.eventbus;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

final class HandlerPoster extends Handler {
  private final EventBus eventBus;
  
  private boolean handlerActive;
  
  private final int maxMillisInsideHandleMessage;
  
  private final PendingPostQueue queue;
  
  HandlerPoster(EventBus paramEventBus, Looper paramLooper, int paramInt) {
    super(paramLooper);
    this.eventBus = paramEventBus;
    this.maxMillisInsideHandleMessage = paramInt;
    this.queue = new PendingPostQueue();
  }
  
  void enqueue(Subscription paramSubscription, Object paramObject) {
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
    //   17: getfield handlerActive : Z
    //   20: ifne -> 56
    //   23: aload_0
    //   24: iconst_1
    //   25: putfield handlerActive : Z
    //   28: aload_0
    //   29: aload_0
    //   30: invokevirtual obtainMessage : ()Landroid/os/Message;
    //   33: invokevirtual sendMessage : (Landroid/os/Message;)Z
    //   36: ifne -> 56
    //   39: new org/greenrobot/eventbus/EventBusException
    //   42: astore_1
    //   43: aload_1
    //   44: ldc 'Could not send handler message'
    //   46: invokespecial <init> : (Ljava/lang/String;)V
    //   49: aload_1
    //   50: athrow
    //   51: astore_1
    //   52: aload_0
    //   53: monitorexit
    //   54: aload_1
    //   55: athrow
    //   56: aload_0
    //   57: monitorexit
    //   58: return
    // Exception table:
    //   from	to	target	type
    //   8	51	51	finally
    //   52	54	51	finally
    //   56	58	51	finally
  }
  
  public void handleMessage(Message paramMessage) {
    // Byte code:
    //   0: invokestatic uptimeMillis : ()J
    //   3: lstore_2
    //   4: aload_0
    //   5: getfield queue : Lorg/greenrobot/eventbus/PendingPostQueue;
    //   8: invokevirtual poll : ()Lorg/greenrobot/eventbus/PendingPost;
    //   11: astore #4
    //   13: aload #4
    //   15: astore_1
    //   16: aload #4
    //   18: ifnonnull -> 50
    //   21: aload_0
    //   22: monitorenter
    //   23: aload_0
    //   24: getfield queue : Lorg/greenrobot/eventbus/PendingPostQueue;
    //   27: invokevirtual poll : ()Lorg/greenrobot/eventbus/PendingPost;
    //   30: astore_1
    //   31: aload_1
    //   32: ifnonnull -> 48
    //   35: aload_0
    //   36: iconst_0
    //   37: putfield handlerActive : Z
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_0
    //   43: iconst_0
    //   44: putfield handlerActive : Z
    //   47: return
    //   48: aload_0
    //   49: monitorexit
    //   50: aload_0
    //   51: getfield eventBus : Lorg/greenrobot/eventbus/EventBus;
    //   54: aload_1
    //   55: invokevirtual invokeSubscriber : (Lorg/greenrobot/eventbus/PendingPost;)V
    //   58: invokestatic uptimeMillis : ()J
    //   61: lload_2
    //   62: lsub
    //   63: aload_0
    //   64: getfield maxMillisInsideHandleMessage : I
    //   67: i2l
    //   68: lcmp
    //   69: iflt -> 4
    //   72: aload_0
    //   73: aload_0
    //   74: invokevirtual obtainMessage : ()Landroid/os/Message;
    //   77: invokevirtual sendMessage : (Landroid/os/Message;)Z
    //   80: ifne -> 108
    //   83: new org/greenrobot/eventbus/EventBusException
    //   86: astore_1
    //   87: aload_1
    //   88: ldc 'Could not send handler message'
    //   90: invokespecial <init> : (Ljava/lang/String;)V
    //   93: aload_1
    //   94: athrow
    //   95: astore_1
    //   96: aload_0
    //   97: iconst_0
    //   98: putfield handlerActive : Z
    //   101: aload_1
    //   102: athrow
    //   103: astore_1
    //   104: aload_0
    //   105: monitorexit
    //   106: aload_1
    //   107: athrow
    //   108: aload_0
    //   109: iconst_1
    //   110: putfield handlerActive : Z
    //   113: goto -> 47
    // Exception table:
    //   from	to	target	type
    //   0	4	95	finally
    //   4	13	95	finally
    //   21	23	95	finally
    //   23	31	103	finally
    //   35	42	103	finally
    //   48	50	103	finally
    //   50	95	95	finally
    //   104	106	103	finally
    //   106	108	95	finally
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/eventbus/HandlerPoster.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */