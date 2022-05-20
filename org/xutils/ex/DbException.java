package org.xutils.ex;

public class DbException extends BaseException {
  private static final long serialVersionUID = 1L;
  
  public DbException() {}
  
  public DbException(String paramString) {
    super(paramString);
  }
  
  public DbException(String paramString, Throwable paramThrowable) {
    super(paramString, paramThrowable);
  }
  
  public DbException(Throwable paramThrowable) {
    super(paramThrowable);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/ex/DbException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */