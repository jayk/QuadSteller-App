package org.xutils.http.body;

import android.text.TextUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.xutils.common.Callback;
import org.xutils.common.util.IOUtil;
import org.xutils.http.ProgressHandler;

public class InputStreamBody implements ProgressBody {
  private ProgressHandler callBackHandler;
  
  private InputStream content;
  
  private String contentType;
  
  private long current = 0L;
  
  private final long total;
  
  public InputStreamBody(InputStream paramInputStream) {
    this(paramInputStream, null);
  }
  
  public InputStreamBody(InputStream paramInputStream, String paramString) {
    this.content = paramInputStream;
    this.contentType = paramString;
    this.total = getInputStreamLength(paramInputStream);
  }
  
  public static long getInputStreamLength(InputStream paramInputStream) {
    try {
      if (paramInputStream instanceof java.io.FileInputStream || paramInputStream instanceof java.io.ByteArrayInputStream) {
        int i = paramInputStream.available();
        return i;
      } 
    } catch (Throwable throwable) {}
    return -1L;
  }
  
  public long getContentLength() {
    return this.total;
  }
  
  public String getContentType() {
    return TextUtils.isEmpty(this.contentType) ? "application/octet-stream" : this.contentType;
  }
  
  public void setContentType(String paramString) {
    this.contentType = paramString;
  }
  
  public void setProgressHandler(ProgressHandler paramProgressHandler) {
    this.callBackHandler = paramProgressHandler;
  }
  
  public void writeTo(OutputStream paramOutputStream) throws IOException {
    if (this.callBackHandler != null && !this.callBackHandler.updateProgress(this.total, this.current, true))
      throw new Callback.CancelledException("upload stopped!"); 
    byte[] arrayOfByte = new byte[1024];
    try {
      while (true) {
        Callback.CancelledException cancelledException;
        int i = this.content.read(arrayOfByte);
        if (i != -1) {
          paramOutputStream.write(arrayOfByte, 0, i);
          this.current += i;
          if (this.callBackHandler != null && !this.callBackHandler.updateProgress(this.total, this.current, false)) {
            cancelledException = new Callback.CancelledException();
            this("upload stopped!");
            throw cancelledException;
          } 
          continue;
        } 
        cancelledException.flush();
        if (this.callBackHandler != null)
          this.callBackHandler.updateProgress(this.total, this.total, true); 
        return;
      } 
    } finally {
      IOUtil.closeQuietly(this.content);
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/body/InputStreamBody.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */