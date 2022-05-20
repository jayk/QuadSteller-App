package org.xutils.ex;

public class HttpRedirectException extends HttpException {
  private static final long serialVersionUID = 1L;
  
  public HttpRedirectException(int paramInt, String paramString1, String paramString2) {
    super(paramInt, paramString1);
    setResult(paramString2);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/ex/HttpRedirectException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */