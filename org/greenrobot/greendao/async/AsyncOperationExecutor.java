package org.greenrobot.greendao.async;

import android.os.Handler;
import android.os.Message;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.Query;

class AsyncOperationExecutor implements Runnable, Handler.Callback {
  private static ExecutorService executorService = Executors.newCachedThreadPool();
  
  private int countOperationsCompleted;
  
  private int countOperationsEnqueued;
  
  private volatile boolean executorRunning;
  
  private Handler handlerMainThread;
  
  private int lastSequenceNumber;
  
  private volatile AsyncOperationListener listener;
  
  private volatile AsyncOperationListener listenerMainThread;
  
  private volatile int maxOperationCountToMerge = 50;
  
  private final BlockingQueue<AsyncOperation> queue = new LinkedBlockingQueue<AsyncOperation>();
  
  private volatile int waitForMergeMillis = 50;
  
  private void executeOperation(AsyncOperation paramAsyncOperation) {
    paramAsyncOperation.timeStarted = System.currentTimeMillis();
    try {
      DaoException daoException;
      StringBuilder stringBuilder;
      switch (paramAsyncOperation.type) {
        default:
          daoException = new DaoException();
          stringBuilder = new StringBuilder();
          this();
          this(stringBuilder.append("Unsupported operation: ").append(paramAsyncOperation.type).toString());
          throw daoException;
        case Delete:
          paramAsyncOperation.dao.delete(paramAsyncOperation.parameter);
          paramAsyncOperation.timeCompleted = System.currentTimeMillis();
          return;
        case DeleteInTxIterable:
          paramAsyncOperation.dao.deleteInTx((Iterable)paramAsyncOperation.parameter);
          paramAsyncOperation.timeCompleted = System.currentTimeMillis();
          return;
        case DeleteInTxArray:
          paramAsyncOperation.dao.deleteInTx((Object[])paramAsyncOperation.parameter);
          paramAsyncOperation.timeCompleted = System.currentTimeMillis();
          return;
        case Insert:
          paramAsyncOperation.dao.insert(paramAsyncOperation.parameter);
          paramAsyncOperation.timeCompleted = System.currentTimeMillis();
          return;
        case InsertInTxIterable:
          paramAsyncOperation.dao.insertInTx((Iterable)paramAsyncOperation.parameter);
          paramAsyncOperation.timeCompleted = System.currentTimeMillis();
          return;
        case InsertInTxArray:
          paramAsyncOperation.dao.insertInTx((Object[])paramAsyncOperation.parameter);
          paramAsyncOperation.timeCompleted = System.currentTimeMillis();
          return;
        case InsertOrReplace:
          paramAsyncOperation.dao.insertOrReplace(paramAsyncOperation.parameter);
          paramAsyncOperation.timeCompleted = System.currentTimeMillis();
          return;
        case InsertOrReplaceInTxIterable:
          paramAsyncOperation.dao.insertOrReplaceInTx((Iterable)paramAsyncOperation.parameter);
          paramAsyncOperation.timeCompleted = System.currentTimeMillis();
          return;
        case InsertOrReplaceInTxArray:
          paramAsyncOperation.dao.insertOrReplaceInTx((Object[])paramAsyncOperation.parameter);
          paramAsyncOperation.timeCompleted = System.currentTimeMillis();
          return;
        case Update:
          paramAsyncOperation.dao.update(paramAsyncOperation.parameter);
          paramAsyncOperation.timeCompleted = System.currentTimeMillis();
          return;
        case UpdateInTxIterable:
          paramAsyncOperation.dao.updateInTx((Iterable)paramAsyncOperation.parameter);
          paramAsyncOperation.timeCompleted = System.currentTimeMillis();
          return;
        case UpdateInTxArray:
          paramAsyncOperation.dao.updateInTx((Object[])paramAsyncOperation.parameter);
          paramAsyncOperation.timeCompleted = System.currentTimeMillis();
          return;
        case TransactionRunnable:
          executeTransactionRunnable(paramAsyncOperation);
          paramAsyncOperation.timeCompleted = System.currentTimeMillis();
          return;
        case TransactionCallable:
          executeTransactionCallable(paramAsyncOperation);
          paramAsyncOperation.timeCompleted = System.currentTimeMillis();
          return;
        case QueryList:
          paramAsyncOperation.result = ((Query)paramAsyncOperation.parameter).forCurrentThread().list();
          paramAsyncOperation.timeCompleted = System.currentTimeMillis();
          return;
        case QueryUnique:
          paramAsyncOperation.result = ((Query)paramAsyncOperation.parameter).forCurrentThread().unique();
          paramAsyncOperation.timeCompleted = System.currentTimeMillis();
          return;
        case DeleteByKey:
          paramAsyncOperation.dao.deleteByKey(paramAsyncOperation.parameter);
          paramAsyncOperation.timeCompleted = System.currentTimeMillis();
          return;
        case DeleteAll:
          paramAsyncOperation.dao.deleteAll();
          paramAsyncOperation.timeCompleted = System.currentTimeMillis();
          return;
        case Load:
          paramAsyncOperation.result = paramAsyncOperation.dao.load(paramAsyncOperation.parameter);
          paramAsyncOperation.timeCompleted = System.currentTimeMillis();
          return;
        case LoadAll:
          paramAsyncOperation.result = paramAsyncOperation.dao.loadAll();
          paramAsyncOperation.timeCompleted = System.currentTimeMillis();
          return;
        case Count:
          paramAsyncOperation.result = Long.valueOf(paramAsyncOperation.dao.count());
          paramAsyncOperation.timeCompleted = System.currentTimeMillis();
          return;
        case Refresh:
          break;
      } 
    } catch (Throwable throwable) {
      paramAsyncOperation.throwable = throwable;
      paramAsyncOperation.timeCompleted = System.currentTimeMillis();
      return;
    } 
    paramAsyncOperation.dao.refresh(paramAsyncOperation.parameter);
    paramAsyncOperation.timeCompleted = System.currentTimeMillis();
  }
  
  private void executeOperationAndPostCompleted(AsyncOperation paramAsyncOperation) {
    executeOperation(paramAsyncOperation);
    handleOperationCompleted(paramAsyncOperation);
  }
  
  private void executeTransactionCallable(AsyncOperation paramAsyncOperation) throws Exception {
    Database database = paramAsyncOperation.getDatabase();
    database.beginTransaction();
    try {
      paramAsyncOperation.result = ((Callable)paramAsyncOperation.parameter).call();
      database.setTransactionSuccessful();
      return;
    } finally {
      database.endTransaction();
    } 
  }
  
  private void executeTransactionRunnable(AsyncOperation paramAsyncOperation) {
    Database database = paramAsyncOperation.getDatabase();
    database.beginTransaction();
    try {
      ((Runnable)paramAsyncOperation.parameter).run();
      database.setTransactionSuccessful();
      return;
    } finally {
      database.endTransaction();
    } 
  }
  
  private void handleOperationCompleted(AsyncOperation paramAsyncOperation) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual setCompleted : ()V
    //   4: aload_0
    //   5: getfield listener : Lorg/greenrobot/greendao/async/AsyncOperationListener;
    //   8: astore_2
    //   9: aload_2
    //   10: ifnull -> 20
    //   13: aload_2
    //   14: aload_1
    //   15: invokeinterface onAsyncOperationCompleted : (Lorg/greenrobot/greendao/async/AsyncOperation;)V
    //   20: aload_0
    //   21: getfield listenerMainThread : Lorg/greenrobot/greendao/async/AsyncOperationListener;
    //   24: ifnull -> 68
    //   27: aload_0
    //   28: getfield handlerMainThread : Landroid/os/Handler;
    //   31: ifnonnull -> 49
    //   34: aload_0
    //   35: new android/os/Handler
    //   38: dup
    //   39: invokestatic getMainLooper : ()Landroid/os/Looper;
    //   42: aload_0
    //   43: invokespecial <init> : (Landroid/os/Looper;Landroid/os/Handler$Callback;)V
    //   46: putfield handlerMainThread : Landroid/os/Handler;
    //   49: aload_0
    //   50: getfield handlerMainThread : Landroid/os/Handler;
    //   53: iconst_1
    //   54: aload_1
    //   55: invokevirtual obtainMessage : (ILjava/lang/Object;)Landroid/os/Message;
    //   58: astore_1
    //   59: aload_0
    //   60: getfield handlerMainThread : Landroid/os/Handler;
    //   63: aload_1
    //   64: invokevirtual sendMessage : (Landroid/os/Message;)Z
    //   67: pop
    //   68: aload_0
    //   69: monitorenter
    //   70: aload_0
    //   71: aload_0
    //   72: getfield countOperationsCompleted : I
    //   75: iconst_1
    //   76: iadd
    //   77: putfield countOperationsCompleted : I
    //   80: aload_0
    //   81: getfield countOperationsCompleted : I
    //   84: aload_0
    //   85: getfield countOperationsEnqueued : I
    //   88: if_icmpne -> 95
    //   91: aload_0
    //   92: invokevirtual notifyAll : ()V
    //   95: aload_0
    //   96: monitorexit
    //   97: return
    //   98: astore_1
    //   99: aload_0
    //   100: monitorexit
    //   101: aload_1
    //   102: athrow
    // Exception table:
    //   from	to	target	type
    //   70	95	98	finally
    //   95	97	98	finally
    //   99	101	98	finally
  }
  
  private void mergeTxAndExecute(AsyncOperation paramAsyncOperation1, AsyncOperation paramAsyncOperation2) {
    // Byte code:
    //   0: new java/util/ArrayList
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_3
    //   8: aload_3
    //   9: aload_1
    //   10: invokevirtual add : (Ljava/lang/Object;)Z
    //   13: pop
    //   14: aload_3
    //   15: aload_2
    //   16: invokevirtual add : (Ljava/lang/Object;)Z
    //   19: pop
    //   20: aload_1
    //   21: invokevirtual getDatabase : ()Lorg/greenrobot/greendao/database/Database;
    //   24: astore_1
    //   25: aload_1
    //   26: invokeinterface beginTransaction : ()V
    //   31: iconst_0
    //   32: istore #4
    //   34: iconst_0
    //   35: istore #5
    //   37: iload #4
    //   39: istore #6
    //   41: iload #5
    //   43: aload_3
    //   44: invokevirtual size : ()I
    //   47: if_icmpge -> 83
    //   50: aload_3
    //   51: iload #5
    //   53: invokevirtual get : (I)Ljava/lang/Object;
    //   56: checkcast org/greenrobot/greendao/async/AsyncOperation
    //   59: astore #7
    //   61: aload_0
    //   62: aload #7
    //   64: invokespecial executeOperation : (Lorg/greenrobot/greendao/async/AsyncOperation;)V
    //   67: aload #7
    //   69: invokevirtual isFailed : ()Z
    //   72: istore #6
    //   74: iload #6
    //   76: ifeq -> 138
    //   79: iload #4
    //   81: istore #6
    //   83: aload_1
    //   84: invokeinterface endTransaction : ()V
    //   89: iload #6
    //   91: ifeq -> 309
    //   94: aload_3
    //   95: invokevirtual size : ()I
    //   98: istore #5
    //   100: aload_3
    //   101: invokevirtual iterator : ()Ljava/util/Iterator;
    //   104: astore_2
    //   105: aload_2
    //   106: invokeinterface hasNext : ()Z
    //   111: ifeq -> 352
    //   114: aload_2
    //   115: invokeinterface next : ()Ljava/lang/Object;
    //   120: checkcast org/greenrobot/greendao/async/AsyncOperation
    //   123: astore_1
    //   124: aload_1
    //   125: iload #5
    //   127: putfield mergedOperationsCount : I
    //   130: aload_0
    //   131: aload_1
    //   132: invokespecial handleOperationCompleted : (Lorg/greenrobot/greendao/async/AsyncOperation;)V
    //   135: goto -> 105
    //   138: iload #5
    //   140: aload_3
    //   141: invokevirtual size : ()I
    //   144: iconst_1
    //   145: isub
    //   146: if_icmpne -> 229
    //   149: aload_0
    //   150: getfield queue : Ljava/util/concurrent/BlockingQueue;
    //   153: invokeinterface peek : ()Ljava/lang/Object;
    //   158: checkcast org/greenrobot/greendao/async/AsyncOperation
    //   161: astore_2
    //   162: iload #5
    //   164: aload_0
    //   165: getfield maxOperationCountToMerge : I
    //   168: if_icmpge -> 235
    //   171: aload #7
    //   173: aload_2
    //   174: invokevirtual isMergeableWith : (Lorg/greenrobot/greendao/async/AsyncOperation;)Z
    //   177: ifeq -> 235
    //   180: aload_0
    //   181: getfield queue : Ljava/util/concurrent/BlockingQueue;
    //   184: invokeinterface remove : ()Ljava/lang/Object;
    //   189: checkcast org/greenrobot/greendao/async/AsyncOperation
    //   192: astore #7
    //   194: aload #7
    //   196: aload_2
    //   197: if_acmpeq -> 222
    //   200: new org/greenrobot/greendao/DaoException
    //   203: astore_2
    //   204: aload_2
    //   205: ldc_w 'Internal error: peeked op did not match removed op'
    //   208: invokespecial <init> : (Ljava/lang/String;)V
    //   211: aload_2
    //   212: athrow
    //   213: astore_2
    //   214: aload_1
    //   215: invokeinterface endTransaction : ()V
    //   220: aload_2
    //   221: athrow
    //   222: aload_3
    //   223: aload #7
    //   225: invokevirtual add : (Ljava/lang/Object;)Z
    //   228: pop
    //   229: iinc #5, 1
    //   232: goto -> 37
    //   235: aload_1
    //   236: invokeinterface setTransactionSuccessful : ()V
    //   241: iconst_1
    //   242: istore #6
    //   244: goto -> 83
    //   247: astore_1
    //   248: new java/lang/StringBuilder
    //   251: dup
    //   252: invokespecial <init> : ()V
    //   255: ldc_w 'Async transaction could not be ended, success so far was: '
    //   258: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   261: iload #6
    //   263: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   266: invokevirtual toString : ()Ljava/lang/String;
    //   269: aload_1
    //   270: invokestatic i : (Ljava/lang/String;Ljava/lang/Throwable;)I
    //   273: pop
    //   274: iconst_0
    //   275: istore #6
    //   277: goto -> 89
    //   280: astore_1
    //   281: new java/lang/StringBuilder
    //   284: dup
    //   285: invokespecial <init> : ()V
    //   288: ldc_w 'Async transaction could not be ended, success so far was: '
    //   291: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   294: iconst_0
    //   295: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   298: invokevirtual toString : ()Ljava/lang/String;
    //   301: aload_1
    //   302: invokestatic i : (Ljava/lang/String;Ljava/lang/Throwable;)I
    //   305: pop
    //   306: goto -> 220
    //   309: ldc_w 'Reverted merged transaction because one of the operations failed. Executing operations one by one instead...'
    //   312: invokestatic i : (Ljava/lang/String;)I
    //   315: pop
    //   316: aload_3
    //   317: invokevirtual iterator : ()Ljava/util/Iterator;
    //   320: astore_1
    //   321: aload_1
    //   322: invokeinterface hasNext : ()Z
    //   327: ifeq -> 352
    //   330: aload_1
    //   331: invokeinterface next : ()Ljava/lang/Object;
    //   336: checkcast org/greenrobot/greendao/async/AsyncOperation
    //   339: astore_2
    //   340: aload_2
    //   341: invokevirtual reset : ()V
    //   344: aload_0
    //   345: aload_2
    //   346: invokespecial executeOperationAndPostCompleted : (Lorg/greenrobot/greendao/async/AsyncOperation;)V
    //   349: goto -> 321
    //   352: return
    // Exception table:
    //   from	to	target	type
    //   41	74	213	finally
    //   83	89	247	java/lang/RuntimeException
    //   138	194	213	finally
    //   200	213	213	finally
    //   214	220	280	java/lang/RuntimeException
    //   222	229	213	finally
    //   235	241	213	finally
  }
  
  public void enqueue(AsyncOperation paramAsyncOperation) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield lastSequenceNumber : I
    //   6: iconst_1
    //   7: iadd
    //   8: istore_2
    //   9: aload_0
    //   10: iload_2
    //   11: putfield lastSequenceNumber : I
    //   14: aload_1
    //   15: iload_2
    //   16: putfield sequenceNumber : I
    //   19: aload_0
    //   20: getfield queue : Ljava/util/concurrent/BlockingQueue;
    //   23: aload_1
    //   24: invokeinterface add : (Ljava/lang/Object;)Z
    //   29: pop
    //   30: aload_0
    //   31: aload_0
    //   32: getfield countOperationsEnqueued : I
    //   35: iconst_1
    //   36: iadd
    //   37: putfield countOperationsEnqueued : I
    //   40: aload_0
    //   41: getfield executorRunning : Z
    //   44: ifne -> 61
    //   47: aload_0
    //   48: iconst_1
    //   49: putfield executorRunning : Z
    //   52: getstatic org/greenrobot/greendao/async/AsyncOperationExecutor.executorService : Ljava/util/concurrent/ExecutorService;
    //   55: aload_0
    //   56: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   61: aload_0
    //   62: monitorexit
    //   63: return
    //   64: astore_1
    //   65: aload_0
    //   66: monitorexit
    //   67: aload_1
    //   68: athrow
    // Exception table:
    //   from	to	target	type
    //   2	61	64	finally
    //   61	63	64	finally
    //   65	67	64	finally
  }
  
  public AsyncOperationListener getListener() {
    return this.listener;
  }
  
  public AsyncOperationListener getListenerMainThread() {
    return this.listenerMainThread;
  }
  
  public int getMaxOperationCountToMerge() {
    return this.maxOperationCountToMerge;
  }
  
  public int getWaitForMergeMillis() {
    return this.waitForMergeMillis;
  }
  
  public boolean handleMessage(Message paramMessage) {
    AsyncOperationListener asyncOperationListener = this.listenerMainThread;
    if (asyncOperationListener != null)
      asyncOperationListener.onAsyncOperationCompleted((AsyncOperation)paramMessage.obj); 
    return false;
  }
  
  public boolean isCompleted() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield countOperationsEnqueued : I
    //   6: istore_1
    //   7: aload_0
    //   8: getfield countOperationsCompleted : I
    //   11: istore_2
    //   12: iload_1
    //   13: iload_2
    //   14: if_icmpne -> 23
    //   17: iconst_1
    //   18: istore_3
    //   19: aload_0
    //   20: monitorexit
    //   21: iload_3
    //   22: ireturn
    //   23: iconst_0
    //   24: istore_3
    //   25: goto -> 19
    //   28: astore #4
    //   30: aload_0
    //   31: monitorexit
    //   32: aload #4
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	28	finally
  }
  
  public void run() {
    // Byte code:
    //   0: aload_0
    //   1: getfield queue : Ljava/util/concurrent/BlockingQueue;
    //   4: lconst_1
    //   5: getstatic java/util/concurrent/TimeUnit.SECONDS : Ljava/util/concurrent/TimeUnit;
    //   8: invokeinterface poll : (JLjava/util/concurrent/TimeUnit;)Ljava/lang/Object;
    //   13: checkcast org/greenrobot/greendao/async/AsyncOperation
    //   16: astore_1
    //   17: aload_1
    //   18: astore_2
    //   19: aload_1
    //   20: ifnonnull -> 57
    //   23: aload_0
    //   24: monitorenter
    //   25: aload_0
    //   26: getfield queue : Ljava/util/concurrent/BlockingQueue;
    //   29: invokeinterface poll : ()Ljava/lang/Object;
    //   34: checkcast org/greenrobot/greendao/async/AsyncOperation
    //   37: astore_2
    //   38: aload_2
    //   39: ifnonnull -> 55
    //   42: aload_0
    //   43: iconst_0
    //   44: putfield executorRunning : Z
    //   47: aload_0
    //   48: monitorexit
    //   49: aload_0
    //   50: iconst_0
    //   51: putfield executorRunning : Z
    //   54: return
    //   55: aload_0
    //   56: monitorexit
    //   57: aload_2
    //   58: invokevirtual isMergeTx : ()Z
    //   61: ifeq -> 173
    //   64: aload_0
    //   65: getfield queue : Ljava/util/concurrent/BlockingQueue;
    //   68: aload_0
    //   69: getfield waitForMergeMillis : I
    //   72: i2l
    //   73: getstatic java/util/concurrent/TimeUnit.MILLISECONDS : Ljava/util/concurrent/TimeUnit;
    //   76: invokeinterface poll : (JLjava/util/concurrent/TimeUnit;)Ljava/lang/Object;
    //   81: checkcast org/greenrobot/greendao/async/AsyncOperation
    //   84: astore_1
    //   85: aload_1
    //   86: ifnull -> 173
    //   89: aload_2
    //   90: aload_1
    //   91: invokevirtual isMergeableWith : (Lorg/greenrobot/greendao/async/AsyncOperation;)Z
    //   94: ifeq -> 160
    //   97: aload_0
    //   98: aload_2
    //   99: aload_1
    //   100: invokespecial mergeTxAndExecute : (Lorg/greenrobot/greendao/async/AsyncOperation;Lorg/greenrobot/greendao/async/AsyncOperation;)V
    //   103: goto -> 0
    //   106: astore_1
    //   107: new java/lang/StringBuilder
    //   110: astore_2
    //   111: aload_2
    //   112: invokespecial <init> : ()V
    //   115: aload_2
    //   116: invokestatic currentThread : ()Ljava/lang/Thread;
    //   119: invokevirtual getName : ()Ljava/lang/String;
    //   122: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   125: ldc_w ' was interruppted'
    //   128: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: invokevirtual toString : ()Ljava/lang/String;
    //   134: aload_1
    //   135: invokestatic w : (Ljava/lang/String;Ljava/lang/Throwable;)I
    //   138: pop
    //   139: aload_0
    //   140: iconst_0
    //   141: putfield executorRunning : Z
    //   144: goto -> 54
    //   147: astore_2
    //   148: aload_0
    //   149: monitorexit
    //   150: aload_2
    //   151: athrow
    //   152: astore_2
    //   153: aload_0
    //   154: iconst_0
    //   155: putfield executorRunning : Z
    //   158: aload_2
    //   159: athrow
    //   160: aload_0
    //   161: aload_2
    //   162: invokespecial executeOperationAndPostCompleted : (Lorg/greenrobot/greendao/async/AsyncOperation;)V
    //   165: aload_0
    //   166: aload_1
    //   167: invokespecial executeOperationAndPostCompleted : (Lorg/greenrobot/greendao/async/AsyncOperation;)V
    //   170: goto -> 0
    //   173: aload_0
    //   174: aload_2
    //   175: invokespecial executeOperationAndPostCompleted : (Lorg/greenrobot/greendao/async/AsyncOperation;)V
    //   178: goto -> 0
    // Exception table:
    //   from	to	target	type
    //   0	17	106	java/lang/InterruptedException
    //   0	17	152	finally
    //   23	25	106	java/lang/InterruptedException
    //   23	25	152	finally
    //   25	38	147	finally
    //   42	49	147	finally
    //   55	57	147	finally
    //   57	85	106	java/lang/InterruptedException
    //   57	85	152	finally
    //   89	103	106	java/lang/InterruptedException
    //   89	103	152	finally
    //   107	139	152	finally
    //   148	150	147	finally
    //   150	152	106	java/lang/InterruptedException
    //   150	152	152	finally
    //   160	170	106	java/lang/InterruptedException
    //   160	170	152	finally
    //   173	178	106	java/lang/InterruptedException
    //   173	178	152	finally
  }
  
  public void setListener(AsyncOperationListener paramAsyncOperationListener) {
    this.listener = paramAsyncOperationListener;
  }
  
  public void setListenerMainThread(AsyncOperationListener paramAsyncOperationListener) {
    this.listenerMainThread = paramAsyncOperationListener;
  }
  
  public void setMaxOperationCountToMerge(int paramInt) {
    this.maxOperationCountToMerge = paramInt;
  }
  
  public void setWaitForMergeMillis(int paramInt) {
    this.waitForMergeMillis = paramInt;
  }
  
  public void waitForCompletion() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual isCompleted : ()Z
    //   6: istore_1
    //   7: iload_1
    //   8: ifne -> 38
    //   11: aload_0
    //   12: invokevirtual wait : ()V
    //   15: goto -> 2
    //   18: astore_2
    //   19: new org/greenrobot/greendao/DaoException
    //   22: astore_3
    //   23: aload_3
    //   24: ldc_w 'Interrupted while waiting for all operations to complete'
    //   27: aload_2
    //   28: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   31: aload_3
    //   32: athrow
    //   33: astore_2
    //   34: aload_0
    //   35: monitorexit
    //   36: aload_2
    //   37: athrow
    //   38: aload_0
    //   39: monitorexit
    //   40: return
    // Exception table:
    //   from	to	target	type
    //   2	7	33	finally
    //   11	15	18	java/lang/InterruptedException
    //   11	15	33	finally
    //   19	33	33	finally
  }
  
  public boolean waitForCompletion(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual isCompleted : ()Z
    //   6: istore_2
    //   7: iload_2
    //   8: ifne -> 19
    //   11: iload_1
    //   12: i2l
    //   13: lstore_3
    //   14: aload_0
    //   15: lload_3
    //   16: invokevirtual wait : (J)V
    //   19: aload_0
    //   20: invokevirtual isCompleted : ()Z
    //   23: istore_2
    //   24: aload_0
    //   25: monitorexit
    //   26: iload_2
    //   27: ireturn
    //   28: astore #5
    //   30: new org/greenrobot/greendao/DaoException
    //   33: astore #6
    //   35: aload #6
    //   37: ldc_w 'Interrupted while waiting for all operations to complete'
    //   40: aload #5
    //   42: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   45: aload #6
    //   47: athrow
    //   48: astore #5
    //   50: aload_0
    //   51: monitorexit
    //   52: aload #5
    //   54: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	48	finally
    //   14	19	28	java/lang/InterruptedException
    //   14	19	48	finally
    //   19	24	48	finally
    //   30	48	48	finally
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/async/AsyncOperationExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */