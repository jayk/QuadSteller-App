package org.xutils.http.body;

import android.net.Uri;
import android.text.TextUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.xutils.common.util.KeyValue;

public class UrlEncodedParamsBody implements RequestBody {
  private String charset = "UTF-8";
  
  private byte[] content;
  
  public UrlEncodedParamsBody(List<KeyValue> paramList, String paramString) throws IOException {
    if (!TextUtils.isEmpty(paramString))
      this.charset = paramString; 
    StringBuilder stringBuilder = new StringBuilder();
    if (paramList != null)
      for (KeyValue keyValue : paramList) {
        String str2 = keyValue.key;
        String str1 = keyValue.getValueStr();
        if (!TextUtils.isEmpty(str2) && str1 != null) {
          if (stringBuilder.length() > 0)
            stringBuilder.append("&"); 
          stringBuilder.append(Uri.encode(str2, this.charset)).append("=").append(Uri.encode(str1, this.charset));
        } 
      }  
    this.content = stringBuilder.toString().getBytes(this.charset);
  }
  
  public long getContentLength() {
    return this.content.length;
  }
  
  public String getContentType() {
    return "application/x-www-form-urlencoded;charset=" + this.charset;
  }
  
  public void setContentType(String paramString) {}
  
  public void writeTo(OutputStream paramOutputStream) throws IOException {
    paramOutputStream.write(this.content);
    paramOutputStream.flush();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/body/UrlEncodedParamsBody.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */