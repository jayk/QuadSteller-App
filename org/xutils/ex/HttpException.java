package org.xutils.ex;

import android.text.TextUtils;

public class HttpException extends BaseException {
  private static final long serialVersionUID = 1L;
  
  private int code;
  
  private String customMessage;
  
  private String errorCode;
  
  private String result;
  
  public HttpException(int paramInt, String paramString) {
    super(paramString);
    this.code = paramInt;
  }
  
  public int getCode() {
    return this.code;
  }
  
  public String getErrorCode() {
    return (this.errorCode == null) ? String.valueOf(this.code) : this.errorCode;
  }
  
  public String getMessage() {
    return !TextUtils.isEmpty(this.customMessage) ? this.customMessage : super.getMessage();
  }
  
  public String getResult() {
    return this.result;
  }
  
  public void setCode(int paramInt) {
    this.code = paramInt;
  }
  
  public void setErrorCode(String paramString) {
    this.errorCode = paramString;
  }
  
  public void setMessage(String paramString) {
    this.customMessage = paramString;
  }
  
  public void setResult(String paramString) {
    this.result = paramString;
  }
  
  public String toString() {
    return "errorCode: " + getErrorCode() + ", msg: " + getMessage() + ", result: " + this.result;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/ex/HttpException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */