package org.greenrobot.eventbus;

final class PendingPostQueue {
  private PendingPost head;
  
  private PendingPost tail;
  
  void enqueue(PendingPost paramPendingPost) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ifnonnull -> 23
    //   6: new java/lang/NullPointerException
    //   9: astore_1
    //   10: aload_1
    //   11: ldc 'null cannot be enqueued'
    //   13: invokespecial <init> : (Ljava/lang/String;)V
    //   16: aload_1
    //   17: athrow
    //   18: astore_1
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_1
    //   22: athrow
    //   23: aload_0
    //   24: getfield tail : Lorg/greenrobot/eventbus/PendingPost;
    //   27: ifnull -> 50
    //   30: aload_0
    //   31: getfield tail : Lorg/greenrobot/eventbus/PendingPost;
    //   34: aload_1
    //   35: putfield next : Lorg/greenrobot/eventbus/PendingPost;
    //   38: aload_0
    //   39: aload_1
    //   40: putfield tail : Lorg/greenrobot/eventbus/PendingPost;
    //   43: aload_0
    //   44: invokevirtual notifyAll : ()V
    //   47: aload_0
    //   48: monitorexit
    //   49: return
    //   50: aload_0
    //   51: getfield head : Lorg/greenrobot/eventbus/PendingPost;
    //   54: ifnonnull -> 70
    //   57: aload_0
    //   58: aload_1
    //   59: putfield tail : Lorg/greenrobot/eventbus/PendingPost;
    //   62: aload_0
    //   63: aload_1
    //   64: putfield head : Lorg/greenrobot/eventbus/PendingPost;
    //   67: goto -> 43
    //   70: new java/lang/IllegalStateException
    //   73: astore_1
    //   74: aload_1
    //   75: ldc 'Head present, but no tail'
    //   77: invokespecial <init> : (Ljava/lang/String;)V
    //   80: aload_1
    //   81: athrow
    // Exception table:
    //   from	to	target	type
    //   6	18	18	finally
    //   23	43	18	finally
    //   43	47	18	finally
    //   50	67	18	finally
    //   70	82	18	finally
  }
  
  PendingPost poll() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield head : Lorg/greenrobot/eventbus/PendingPost;
    //   6: astore_1
    //   7: aload_0
    //   8: getfield head : Lorg/greenrobot/eventbus/PendingPost;
    //   11: ifnull -> 37
    //   14: aload_0
    //   15: aload_0
    //   16: getfield head : Lorg/greenrobot/eventbus/PendingPost;
    //   19: getfield next : Lorg/greenrobot/eventbus/PendingPost;
    //   22: putfield head : Lorg/greenrobot/eventbus/PendingPost;
    //   25: aload_0
    //   26: getfield head : Lorg/greenrobot/eventbus/PendingPost;
    //   29: ifnonnull -> 37
    //   32: aload_0
    //   33: aconst_null
    //   34: putfield tail : Lorg/greenrobot/eventbus/PendingPost;
    //   37: aload_0
    //   38: monitorexit
    //   39: aload_1
    //   40: areturn
    //   41: astore_1
    //   42: aload_0
    //   43: monitorexit
    //   44: aload_1
    //   45: athrow
    // Exception table:
    //   from	to	target	type
    //   2	37	41	finally
  }
  
  PendingPost poll(int paramInt) throws InterruptedException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield head : Lorg/greenrobot/eventbus/PendingPost;
    //   6: ifnonnull -> 15
    //   9: aload_0
    //   10: iload_1
    //   11: i2l
    //   12: invokevirtual wait : (J)V
    //   15: aload_0
    //   16: invokevirtual poll : ()Lorg/greenrobot/eventbus/PendingPost;
    //   19: astore_2
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_2
    //   23: areturn
    //   24: astore_2
    //   25: aload_0
    //   26: monitorexit
    //   27: aload_2
    //   28: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	24	finally
    //   15	20	24	finally
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/eventbus/PendingPostQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */