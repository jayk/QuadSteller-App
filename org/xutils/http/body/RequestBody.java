package org.xutils.http.body;

import java.io.IOException;
import java.io.OutputStream;

public interface RequestBody {
  long getContentLength();
  
  String getContentType();
  
  void setContentType(String paramString);
  
  void writeTo(OutputStream paramOutputStream) throws IOException;
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/body/RequestBody.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */