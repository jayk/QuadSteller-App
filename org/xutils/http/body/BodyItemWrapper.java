package org.xutils.http.body;

import android.text.TextUtils;

public final class BodyItemWrapper {
  private final String contentType;
  
  private final String fileName;
  
  private final Object value;
  
  public BodyItemWrapper(Object paramObject, String paramString) {
    this(paramObject, paramString, null);
  }
  
  public BodyItemWrapper(Object paramObject, String paramString1, String paramString2) {
    this.value = paramObject;
    if (TextUtils.isEmpty(paramString1)) {
      this.contentType = "application/octet-stream";
    } else {
      this.contentType = paramString1;
    } 
    this.fileName = paramString2;
  }
  
  public String getContentType() {
    return this.contentType;
  }
  
  public String getFileName() {
    return this.fileName;
  }
  
  public Object getValue() {
    return this.value;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/body/BodyItemWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */