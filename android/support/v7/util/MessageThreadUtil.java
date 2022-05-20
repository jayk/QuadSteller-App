package android.support.v7.util;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ParallelExecutorCompat;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

class MessageThreadUtil<T> implements ThreadUtil<T> {
  public ThreadUtil.BackgroundCallback<T> getBackgroundProxy(final ThreadUtil.BackgroundCallback<T> callback) {
    return new ThreadUtil.BackgroundCallback<T>() {
        static final int LOAD_TILE = 3;
        
        static final int RECYCLE_TILE = 4;
        
        static final int REFRESH = 1;
        
        static final int UPDATE_RANGE = 2;
        
        private Runnable mBackgroundRunnable = new Runnable() {
            public void run() {
              while (true) {
                MessageThreadUtil.SyncQueueItem syncQueueItem = MessageThreadUtil.null.this.mQueue.next();
                if (syncQueueItem == null) {
                  MessageThreadUtil.null.this.mBackgroundRunning.set(false);
                  return;
                } 
                switch (syncQueueItem.what) {
                  case 1:
                    MessageThreadUtil.null.this.mQueue.removeMessages(1);
                    callback.refresh(syncQueueItem.arg1);
                    break;
                  case 2:
                    MessageThreadUtil.null.this.mQueue.removeMessages(2);
                    MessageThreadUtil.null.this.mQueue.removeMessages(3);
                    callback.updateRange(syncQueueItem.arg1, syncQueueItem.arg2, syncQueueItem.arg3, syncQueueItem.arg4, syncQueueItem.arg5);
                    break;
                  case 3:
                    callback.loadTile(syncQueueItem.arg1, syncQueueItem.arg2);
                    break;
                  case 4:
                    callback.recycleTile((TileList.Tile)syncQueueItem.data);
                    break;
                } 
              } 
            }
          };
        
        AtomicBoolean mBackgroundRunning = new AtomicBoolean(false);
        
        private final Executor mExecutor = ParallelExecutorCompat.getParallelExecutor();
        
        final MessageThreadUtil.MessageQueue mQueue = new MessageThreadUtil.MessageQueue();
        
        private void maybeExecuteBackgroundRunnable() {
          if (this.mBackgroundRunning.compareAndSet(false, true))
            this.mExecutor.execute(this.mBackgroundRunnable); 
        }
        
        private void sendMessage(MessageThreadUtil.SyncQueueItem param1SyncQueueItem) {
          this.mQueue.sendMessage(param1SyncQueueItem);
          maybeExecuteBackgroundRunnable();
        }
        
        private void sendMessageAtFrontOfQueue(MessageThreadUtil.SyncQueueItem param1SyncQueueItem) {
          this.mQueue.sendMessageAtFrontOfQueue(param1SyncQueueItem);
          maybeExecuteBackgroundRunnable();
        }
        
        public void loadTile(int param1Int1, int param1Int2) {
          sendMessage(MessageThreadUtil.SyncQueueItem.obtainMessage(3, param1Int1, param1Int2));
        }
        
        public void recycleTile(TileList.Tile<T> param1Tile) {
          sendMessage(MessageThreadUtil.SyncQueueItem.obtainMessage(4, 0, param1Tile));
        }
        
        public void refresh(int param1Int) {
          sendMessageAtFrontOfQueue(MessageThreadUtil.SyncQueueItem.obtainMessage(1, param1Int, (Object)null));
        }
        
        public void updateRange(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5) {
          sendMessageAtFrontOfQueue(MessageThreadUtil.SyncQueueItem.obtainMessage(2, param1Int1, param1Int2, param1Int3, param1Int4, param1Int5, null));
        }
      };
  }
  
  public ThreadUtil.MainThreadCallback<T> getMainThreadProxy(final ThreadUtil.MainThreadCallback<T> callback) {
    return new ThreadUtil.MainThreadCallback<T>() {
        static final int ADD_TILE = 2;
        
        static final int REMOVE_TILE = 3;
        
        static final int UPDATE_ITEM_COUNT = 1;
        
        private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());
        
        private Runnable mMainThreadRunnable = new Runnable() {
            public void run() {
              MessageThreadUtil.SyncQueueItem syncQueueItem = MessageThreadUtil.null.this.mQueue.next();
              while (syncQueueItem != null) {
                switch (syncQueueItem.what) {
                  case 1:
                    callback.updateItemCount(syncQueueItem.arg1, syncQueueItem.arg2);
                    syncQueueItem = MessageThreadUtil.null.this.mQueue.next();
                    break;
                  case 2:
                    callback.addTile(syncQueueItem.arg1, (TileList.Tile)syncQueueItem.data);
                    syncQueueItem = MessageThreadUtil.null.this.mQueue.next();
                    break;
                  case 3:
                    callback.removeTile(syncQueueItem.arg1, syncQueueItem.arg2);
                    syncQueueItem = MessageThreadUtil.null.this.mQueue.next();
                    break;
                } 
              } 
            }
          };
        
        final MessageThreadUtil.MessageQueue mQueue = new MessageThreadUtil.MessageQueue();
        
        private void sendMessage(MessageThreadUtil.SyncQueueItem param1SyncQueueItem) {
          this.mQueue.sendMessage(param1SyncQueueItem);
          this.mMainThreadHandler.post(this.mMainThreadRunnable);
        }
        
        public void addTile(int param1Int, TileList.Tile<T> param1Tile) {
          sendMessage(MessageThreadUtil.SyncQueueItem.obtainMessage(2, param1Int, param1Tile));
        }
        
        public void removeTile(int param1Int1, int param1Int2) {
          sendMessage(MessageThreadUtil.SyncQueueItem.obtainMessage(3, param1Int1, param1Int2));
        }
        
        public void updateItemCount(int param1Int1, int param1Int2) {
          sendMessage(MessageThreadUtil.SyncQueueItem.obtainMessage(1, param1Int1, param1Int2));
        }
      };
  }
  
  static class MessageQueue {
    private MessageThreadUtil.SyncQueueItem mRoot;
    
    MessageThreadUtil.SyncQueueItem next() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield mRoot : Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   6: astore_1
      //   7: aload_1
      //   8: ifnonnull -> 17
      //   11: aconst_null
      //   12: astore_1
      //   13: aload_0
      //   14: monitorexit
      //   15: aload_1
      //   16: areturn
      //   17: aload_0
      //   18: getfield mRoot : Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   21: astore_1
      //   22: aload_0
      //   23: aload_0
      //   24: getfield mRoot : Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   27: invokestatic access$000 : (Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;)Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   30: putfield mRoot : Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   33: goto -> 13
      //   36: astore_1
      //   37: aload_0
      //   38: monitorexit
      //   39: aload_1
      //   40: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	36	finally
      //   17	33	36	finally
    }
    
    void removeMessages(int param1Int) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield mRoot : Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   6: ifnull -> 48
      //   9: aload_0
      //   10: getfield mRoot : Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   13: getfield what : I
      //   16: iload_1
      //   17: if_icmpne -> 48
      //   20: aload_0
      //   21: getfield mRoot : Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   24: astore_2
      //   25: aload_0
      //   26: aload_0
      //   27: getfield mRoot : Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   30: invokestatic access$000 : (Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;)Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   33: putfield mRoot : Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   36: aload_2
      //   37: invokevirtual recycle : ()V
      //   40: goto -> 2
      //   43: astore_2
      //   44: aload_0
      //   45: monitorexit
      //   46: aload_2
      //   47: athrow
      //   48: aload_0
      //   49: getfield mRoot : Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   52: ifnull -> 105
      //   55: aload_0
      //   56: getfield mRoot : Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   59: astore_3
      //   60: aload_3
      //   61: invokestatic access$000 : (Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;)Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   64: astore_2
      //   65: aload_2
      //   66: ifnull -> 105
      //   69: aload_2
      //   70: invokestatic access$000 : (Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;)Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   73: astore #4
      //   75: aload_2
      //   76: getfield what : I
      //   79: iload_1
      //   80: if_icmpne -> 100
      //   83: aload_3
      //   84: aload #4
      //   86: invokestatic access$002 : (Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;)Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   89: pop
      //   90: aload_2
      //   91: invokevirtual recycle : ()V
      //   94: aload #4
      //   96: astore_2
      //   97: goto -> 65
      //   100: aload_2
      //   101: astore_3
      //   102: goto -> 94
      //   105: aload_0
      //   106: monitorexit
      //   107: return
      // Exception table:
      //   from	to	target	type
      //   2	40	43	finally
      //   48	65	43	finally
      //   69	94	43	finally
    }
    
    void sendMessage(MessageThreadUtil.SyncQueueItem param1SyncQueueItem) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield mRoot : Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   6: ifnonnull -> 17
      //   9: aload_0
      //   10: aload_1
      //   11: putfield mRoot : Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   14: aload_0
      //   15: monitorexit
      //   16: return
      //   17: aload_0
      //   18: getfield mRoot : Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   21: astore_2
      //   22: aload_2
      //   23: invokestatic access$000 : (Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;)Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   26: ifnull -> 37
      //   29: aload_2
      //   30: invokestatic access$000 : (Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;)Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   33: astore_2
      //   34: goto -> 22
      //   37: aload_2
      //   38: aload_1
      //   39: invokestatic access$002 : (Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;)Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   42: pop
      //   43: goto -> 14
      //   46: astore_1
      //   47: aload_0
      //   48: monitorexit
      //   49: aload_1
      //   50: athrow
      // Exception table:
      //   from	to	target	type
      //   2	14	46	finally
      //   17	22	46	finally
      //   22	34	46	finally
      //   37	43	46	finally
    }
    
    void sendMessageAtFrontOfQueue(MessageThreadUtil.SyncQueueItem param1SyncQueueItem) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_1
      //   3: aload_0
      //   4: getfield mRoot : Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   7: invokestatic access$002 : (Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;)Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   10: pop
      //   11: aload_0
      //   12: aload_1
      //   13: putfield mRoot : Landroid/support/v7/util/MessageThreadUtil$SyncQueueItem;
      //   16: aload_0
      //   17: monitorexit
      //   18: return
      //   19: astore_1
      //   20: aload_0
      //   21: monitorexit
      //   22: aload_1
      //   23: athrow
      // Exception table:
      //   from	to	target	type
      //   2	16	19	finally
    }
  }
  
  static class SyncQueueItem {
    private static SyncQueueItem sPool;
    
    private static final Object sPoolLock = new Object();
    
    public int arg1;
    
    public int arg2;
    
    public int arg3;
    
    public int arg4;
    
    public int arg5;
    
    public Object data;
    
    private SyncQueueItem next;
    
    public int what;
    
    static SyncQueueItem obtainMessage(int param1Int1, int param1Int2, int param1Int3) {
      return obtainMessage(param1Int1, param1Int2, param1Int3, 0, 0, 0, null);
    }
    
    static SyncQueueItem obtainMessage(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6, Object param1Object) {
      synchronized (sPoolLock) {
        if (sPool == null) {
          SyncQueueItem syncQueueItem1 = new SyncQueueItem();
          this();
          syncQueueItem1.what = param1Int1;
          syncQueueItem1.arg1 = param1Int2;
          syncQueueItem1.arg2 = param1Int3;
          syncQueueItem1.arg3 = param1Int4;
          syncQueueItem1.arg4 = param1Int5;
          syncQueueItem1.arg5 = param1Int6;
          syncQueueItem1.data = param1Object;
          return syncQueueItem1;
        } 
        SyncQueueItem syncQueueItem = sPool;
        sPool = sPool.next;
        syncQueueItem.next = null;
        syncQueueItem.what = param1Int1;
        syncQueueItem.arg1 = param1Int2;
        syncQueueItem.arg2 = param1Int3;
        syncQueueItem.arg3 = param1Int4;
        syncQueueItem.arg4 = param1Int5;
        syncQueueItem.arg5 = param1Int6;
        syncQueueItem.data = param1Object;
        return syncQueueItem;
      } 
    }
    
    static SyncQueueItem obtainMessage(int param1Int1, int param1Int2, Object param1Object) {
      return obtainMessage(param1Int1, param1Int2, 0, 0, 0, 0, param1Object);
    }
    
    void recycle() {
      this.next = null;
      this.arg5 = 0;
      this.arg4 = 0;
      this.arg3 = 0;
      this.arg2 = 0;
      this.arg1 = 0;
      this.what = 0;
      this.data = null;
      synchronized (sPoolLock) {
        if (sPool != null)
          this.next = sPool; 
        sPool = this;
        return;
      } 
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/util/MessageThreadUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */