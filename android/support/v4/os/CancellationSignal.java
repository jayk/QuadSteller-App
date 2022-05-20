package android.support.v4.os;

import android.os.Build;

public final class CancellationSignal {
  private boolean mCancelInProgress;
  
  private Object mCancellationSignalObj;
  
  private boolean mIsCanceled;
  
  private OnCancelListener mOnCancelListener;
  
  private void waitForCancelFinishedLocked() {
    while (this.mCancelInProgress) {
      try {
        wait();
      } catch (InterruptedException interruptedException) {}
    } 
  }
  
  public void cancel() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mIsCanceled : Z
    //   6: ifeq -> 12
    //   9: aload_0
    //   10: monitorexit
    //   11: return
    //   12: aload_0
    //   13: iconst_1
    //   14: putfield mIsCanceled : Z
    //   17: aload_0
    //   18: iconst_1
    //   19: putfield mCancelInProgress : Z
    //   22: aload_0
    //   23: getfield mOnCancelListener : Landroid/support/v4/os/CancellationSignal$OnCancelListener;
    //   26: astore_1
    //   27: aload_0
    //   28: getfield mCancellationSignalObj : Ljava/lang/Object;
    //   31: astore_2
    //   32: aload_0
    //   33: monitorexit
    //   34: aload_1
    //   35: ifnull -> 44
    //   38: aload_1
    //   39: invokeinterface onCancel : ()V
    //   44: aload_2
    //   45: ifnull -> 52
    //   48: aload_2
    //   49: invokestatic cancel : (Ljava/lang/Object;)V
    //   52: aload_0
    //   53: monitorenter
    //   54: aload_0
    //   55: iconst_0
    //   56: putfield mCancelInProgress : Z
    //   59: aload_0
    //   60: invokevirtual notifyAll : ()V
    //   63: aload_0
    //   64: monitorexit
    //   65: goto -> 11
    //   68: astore_2
    //   69: aload_0
    //   70: monitorexit
    //   71: aload_2
    //   72: athrow
    //   73: astore_2
    //   74: aload_0
    //   75: monitorexit
    //   76: aload_2
    //   77: athrow
    //   78: astore_2
    //   79: aload_0
    //   80: monitorenter
    //   81: aload_0
    //   82: iconst_0
    //   83: putfield mCancelInProgress : Z
    //   86: aload_0
    //   87: invokevirtual notifyAll : ()V
    //   90: aload_0
    //   91: monitorexit
    //   92: aload_2
    //   93: athrow
    //   94: astore_2
    //   95: aload_0
    //   96: monitorexit
    //   97: aload_2
    //   98: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	73	finally
    //   12	34	73	finally
    //   38	44	78	finally
    //   48	52	78	finally
    //   54	65	68	finally
    //   69	71	68	finally
    //   74	76	73	finally
    //   81	92	94	finally
    //   95	97	94	finally
  }
  
  public Object getCancellationSignalObject() {
    Object object;
    if (Build.VERSION.SDK_INT < 16)
      return null; 
    /* monitor enter ThisExpression{ObjectType{android/support/v4/os/CancellationSignal}} */
    try {
      if (this.mCancellationSignalObj == null) {
        this.mCancellationSignalObj = CancellationSignalCompatJellybean.create();
        if (this.mIsCanceled)
          CancellationSignalCompatJellybean.cancel(this.mCancellationSignalObj); 
      } 
      object = this.mCancellationSignalObj;
      /* monitor exit ThisExpression{ObjectType{android/support/v4/os/CancellationSignal}} */
    } finally {}
    return object;
  }
  
  public boolean isCanceled() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mIsCanceled : Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	11	finally
    //   12	14	11	finally
  }
  
  public void setOnCancelListener(OnCancelListener paramOnCancelListener) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial waitForCancelFinishedLocked : ()V
    //   6: aload_0
    //   7: getfield mOnCancelListener : Landroid/support/v4/os/CancellationSignal$OnCancelListener;
    //   10: aload_1
    //   11: if_acmpne -> 17
    //   14: aload_0
    //   15: monitorexit
    //   16: return
    //   17: aload_0
    //   18: aload_1
    //   19: putfield mOnCancelListener : Landroid/support/v4/os/CancellationSignal$OnCancelListener;
    //   22: aload_0
    //   23: getfield mIsCanceled : Z
    //   26: ifeq -> 33
    //   29: aload_1
    //   30: ifnonnull -> 43
    //   33: aload_0
    //   34: monitorexit
    //   35: goto -> 16
    //   38: astore_1
    //   39: aload_0
    //   40: monitorexit
    //   41: aload_1
    //   42: athrow
    //   43: aload_0
    //   44: monitorexit
    //   45: aload_1
    //   46: invokeinterface onCancel : ()V
    //   51: goto -> 16
    // Exception table:
    //   from	to	target	type
    //   2	16	38	finally
    //   17	29	38	finally
    //   33	35	38	finally
    //   39	41	38	finally
    //   43	45	38	finally
  }
  
  public void throwIfCanceled() {
    if (isCanceled())
      throw new OperationCanceledException(); 
  }
  
  public static interface OnCancelListener {
    void onCancel();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/os/CancellationSignal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */