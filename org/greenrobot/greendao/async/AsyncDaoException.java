package org.greenrobot.greendao.async;

import org.greenrobot.greendao.DaoException;

public class AsyncDaoException extends DaoException {
  private static final long serialVersionUID = 5872157552005102382L;
  
  private final AsyncOperation failedOperation;
  
  public AsyncDaoException(AsyncOperation paramAsyncOperation, Throwable paramThrowable) {
    super(paramThrowable);
    this.failedOperation = paramAsyncOperation;
  }
  
  public AsyncOperation getFailedOperation() {
    return this.failedOperation;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/async/AsyncDaoException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */