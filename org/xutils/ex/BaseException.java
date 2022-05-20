package org.xutils.ex;

import java.io.IOException;

public class BaseException extends IOException {
  private static final long serialVersionUID = 1L;
  
  public BaseException() {}
  
  public BaseException(String paramString) {
    super(paramString);
  }
  
  public BaseException(String paramString, Throwable paramThrowable) {
    super(paramString);
    initCause(paramThrowable);
  }
  
  public BaseException(Throwable paramThrowable) {
    super(paramThrowable.getMessage());
    initCause(paramThrowable);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/ex/BaseException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */