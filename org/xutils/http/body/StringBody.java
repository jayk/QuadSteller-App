package org.xutils.http.body;

import android.text.TextUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class StringBody implements RequestBody {
  private String charset = "UTF-8";
  
  private byte[] content;
  
  private String contentType;
  
  public StringBody(String paramString1, String paramString2) throws UnsupportedEncodingException {
    if (!TextUtils.isEmpty(paramString2))
      this.charset = paramString2; 
    this.content = paramString1.getBytes(this.charset);
  }
  
  public long getContentLength() {
    return this.content.length;
  }
  
  public String getContentType() {
    return TextUtils.isEmpty(this.contentType) ? ("application/json;charset=" + this.charset) : this.contentType;
  }
  
  public void setContentType(String paramString) {
    this.contentType = paramString;
  }
  
  public void writeTo(OutputStream paramOutputStream) throws IOException {
    paramOutputStream.write(this.content);
    paramOutputStream.flush();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/body/StringBody.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */