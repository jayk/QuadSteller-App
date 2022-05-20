package org.greenrobot.greendao.async;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.database.Database;

public class AsyncOperation {
  public static final int FLAG_MERGE_TX = 1;
  
  public static final int FLAG_STOP_QUEUE_ON_EXCEPTION = 2;
  
  public static final int FLAG_TRACK_CREATOR_STACKTRACE = 4;
  
  private volatile boolean completed;
  
  final Exception creatorStacktrace;
  
  final AbstractDao<Object, Object> dao;
  
  private final Database database;
  
  final int flags;
  
  volatile int mergedOperationsCount;
  
  final Object parameter;
  
  volatile Object result;
  
  int sequenceNumber;
  
  volatile Throwable throwable;
  
  volatile long timeCompleted;
  
  volatile long timeStarted;
  
  final OperationType type;
  
  AsyncOperation(OperationType paramOperationType, AbstractDao<?, ?> paramAbstractDao, Database paramDatabase, Object paramObject, int paramInt) {
    this.type = paramOperationType;
    this.flags = paramInt;
    this.dao = (AbstractDao)paramAbstractDao;
    this.database = paramDatabase;
    this.parameter = paramObject;
    if ((paramInt & 0x4) != 0) {
      Exception exception = new Exception("AsyncOperation was created here");
    } else {
      paramOperationType = null;
    } 
    this.creatorStacktrace = (Exception)paramOperationType;
  }
  
  public Exception getCreatorStacktrace() {
    return this.creatorStacktrace;
  }
  
  Database getDatabase() {
    return (this.database != null) ? this.database : this.dao.getDatabase();
  }
  
  public long getDuration() {
    if (this.timeCompleted == 0L)
      throw new DaoException("This operation did not yet complete"); 
    return this.timeCompleted - this.timeStarted;
  }
  
  public int getMergedOperationsCount() {
    return this.mergedOperationsCount;
  }
  
  public Object getParameter() {
    return this.parameter;
  }
  
  public Object getResult() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield completed : Z
    //   6: ifne -> 14
    //   9: aload_0
    //   10: invokevirtual waitForCompletion : ()Ljava/lang/Object;
    //   13: pop
    //   14: aload_0
    //   15: getfield throwable : Ljava/lang/Throwable;
    //   18: ifnull -> 41
    //   21: new org/greenrobot/greendao/async/AsyncDaoException
    //   24: astore_1
    //   25: aload_1
    //   26: aload_0
    //   27: aload_0
    //   28: getfield throwable : Ljava/lang/Throwable;
    //   31: invokespecial <init> : (Lorg/greenrobot/greendao/async/AsyncOperation;Ljava/lang/Throwable;)V
    //   34: aload_1
    //   35: athrow
    //   36: astore_1
    //   37: aload_0
    //   38: monitorexit
    //   39: aload_1
    //   40: athrow
    //   41: aload_0
    //   42: getfield result : Ljava/lang/Object;
    //   45: astore_1
    //   46: aload_0
    //   47: monitorexit
    //   48: aload_1
    //   49: areturn
    // Exception table:
    //   from	to	target	type
    //   2	14	36	finally
    //   14	36	36	finally
    //   41	46	36	finally
  }
  
  public int getSequenceNumber() {
    return this.sequenceNumber;
  }
  
  public Throwable getThrowable() {
    return this.throwable;
  }
  
  public long getTimeCompleted() {
    return this.timeCompleted;
  }
  
  public long getTimeStarted() {
    return this.timeStarted;
  }
  
  public OperationType getType() {
    return this.type;
  }
  
  public boolean isCompleted() {
    return this.completed;
  }
  
  public boolean isCompletedSucessfully() {
    return (this.completed && this.throwable == null);
  }
  
  public boolean isFailed() {
    return (this.throwable != null);
  }
  
  public boolean isMergeTx() {
    return ((this.flags & 0x1) != 0);
  }
  
  boolean isMergeableWith(AsyncOperation paramAsyncOperation) {
    return (paramAsyncOperation != null && isMergeTx() && paramAsyncOperation.isMergeTx() && getDatabase() == paramAsyncOperation.getDatabase());
  }
  
  void reset() {
    this.timeStarted = 0L;
    this.timeCompleted = 0L;
    this.completed = false;
    this.throwable = null;
    this.result = null;
    this.mergedOperationsCount = 0;
  }
  
  void setCompleted() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_1
    //   4: putfield completed : Z
    //   7: aload_0
    //   8: invokevirtual notifyAll : ()V
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	14	finally
  }
  
  public void setThrowable(Throwable paramThrowable) {
    this.throwable = paramThrowable;
  }
  
  public Object waitForCompletion() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield completed : Z
    //   6: istore_1
    //   7: iload_1
    //   8: ifne -> 37
    //   11: aload_0
    //   12: invokevirtual wait : ()V
    //   15: goto -> 2
    //   18: astore_2
    //   19: new org/greenrobot/greendao/DaoException
    //   22: astore_3
    //   23: aload_3
    //   24: ldc 'Interrupted while waiting for operation to complete'
    //   26: aload_2
    //   27: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   30: aload_3
    //   31: athrow
    //   32: astore_2
    //   33: aload_0
    //   34: monitorexit
    //   35: aload_2
    //   36: athrow
    //   37: aload_0
    //   38: getfield result : Ljava/lang/Object;
    //   41: astore_2
    //   42: aload_0
    //   43: monitorexit
    //   44: aload_2
    //   45: areturn
    // Exception table:
    //   from	to	target	type
    //   2	7	32	finally
    //   11	15	18	java/lang/InterruptedException
    //   11	15	32	finally
    //   19	32	32	finally
    //   37	42	32	finally
  }
  
  public boolean waitForCompletion(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield completed : Z
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
    //   20: getfield completed : Z
    //   23: istore_2
    //   24: aload_0
    //   25: monitorexit
    //   26: iload_2
    //   27: ireturn
    //   28: astore #5
    //   30: new org/greenrobot/greendao/DaoException
    //   33: astore #6
    //   35: aload #6
    //   37: ldc 'Interrupted while waiting for operation to complete'
    //   39: aload #5
    //   41: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   44: aload #6
    //   46: athrow
    //   47: astore #6
    //   49: aload_0
    //   50: monitorexit
    //   51: aload #6
    //   53: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	47	finally
    //   14	19	28	java/lang/InterruptedException
    //   14	19	47	finally
    //   19	24	47	finally
    //   30	47	47	finally
  }
  
  public enum OperationType {
    Count, Delete, DeleteAll, DeleteByKey, DeleteInTxArray, DeleteInTxIterable, Insert, InsertInTxArray, InsertInTxIterable, InsertOrReplace, InsertOrReplaceInTxArray, InsertOrReplaceInTxIterable, Load, LoadAll, QueryList, QueryUnique, Refresh, TransactionCallable, TransactionRunnable, Update, UpdateInTxArray, UpdateInTxIterable;
    
    static {
      InsertOrReplaceInTxArray = new OperationType("InsertOrReplaceInTxArray", 5);
      Update = new OperationType("Update", 6);
      UpdateInTxIterable = new OperationType("UpdateInTxIterable", 7);
      UpdateInTxArray = new OperationType("UpdateInTxArray", 8);
      Delete = new OperationType("Delete", 9);
      DeleteInTxIterable = new OperationType("DeleteInTxIterable", 10);
      DeleteInTxArray = new OperationType("DeleteInTxArray", 11);
      DeleteByKey = new OperationType("DeleteByKey", 12);
      DeleteAll = new OperationType("DeleteAll", 13);
      TransactionRunnable = new OperationType("TransactionRunnable", 14);
      TransactionCallable = new OperationType("TransactionCallable", 15);
      QueryList = new OperationType("QueryList", 16);
      QueryUnique = new OperationType("QueryUnique", 17);
      Load = new OperationType("Load", 18);
      LoadAll = new OperationType("LoadAll", 19);
      Count = new OperationType("Count", 20);
      Refresh = new OperationType("Refresh", 21);
      $VALUES = new OperationType[] { 
          Insert, InsertInTxIterable, InsertInTxArray, InsertOrReplace, InsertOrReplaceInTxIterable, InsertOrReplaceInTxArray, Update, UpdateInTxIterable, UpdateInTxArray, Delete, 
          DeleteInTxIterable, DeleteInTxArray, DeleteByKey, DeleteAll, TransactionRunnable, TransactionCallable, QueryList, QueryUnique, Load, LoadAll, 
          Count, Refresh };
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/async/AsyncOperation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */