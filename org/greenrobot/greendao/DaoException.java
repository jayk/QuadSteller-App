package org.greenrobot.greendao;

import android.database.SQLException;

public class DaoException extends SQLException {
  private static final long serialVersionUID = -5877937327907457779L;
  
  public DaoException() {}
  
  public DaoException(String paramString) {
    super(paramString);
  }
  
  public DaoException(String paramString, Throwable paramThrowable) {
    super(paramString);
    safeInitCause(paramThrowable);
  }
  
  public DaoException(Throwable paramThrowable) {
    safeInitCause(paramThrowable);
  }
  
  protected void safeInitCause(Throwable paramThrowable) {
    try {
      initCause(paramThrowable);
    } catch (Throwable throwable) {
      DaoLog.e("Could not set initial cause", throwable);
      DaoLog.e("Initial cause is:", paramThrowable);
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/DaoException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */