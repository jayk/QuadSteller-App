package org.xutils.http.body;

import android.text.TextUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.xutils.common.Callback;
import org.xutils.common.util.IOUtil;
import org.xutils.common.util.KeyValue;
import org.xutils.http.ProgressHandler;

public class MultipartBody implements ProgressBody {
  private static byte[] BOUNDARY_PREFIX_BYTES = "--------7da3d81520810".getBytes();
  
  private static byte[] END_BYTES = "\r\n".getBytes();
  
  private static byte[] TWO_DASHES_BYTES = "--".getBytes();
  
  private byte[] boundaryPostfixBytes;
  
  private ProgressHandler callBackHandler;
  
  private String charset = "UTF-8";
  
  private String contentType;
  
  private long current = 0L;
  
  private List<KeyValue> multipartParams;
  
  private long total = 0L;
  
  public MultipartBody(List<KeyValue> paramList, String paramString) {
    if (!TextUtils.isEmpty(paramString))
      this.charset = paramString; 
    this.multipartParams = paramList;
    generateContentType();
    CounterOutputStream counterOutputStream = new CounterOutputStream();
    try {
      writeTo(counterOutputStream);
      this.total = counterOutputStream.total.get();
    } catch (IOException iOException) {
      this.total = -1L;
    } 
  }
  
  private static byte[] buildContentDisposition(String paramString1, String paramString2, String paramString3) throws UnsupportedEncodingException {
    StringBuilder stringBuilder = new StringBuilder("Content-Disposition: form-data");
    stringBuilder.append("; name=\"").append(paramString1.replace("\"", "\\\"")).append("\"");
    if (!TextUtils.isEmpty(paramString2))
      stringBuilder.append("; filename=\"").append(paramString2.replace("\"", "\\\"")).append("\""); 
    return stringBuilder.toString().getBytes(paramString3);
  }
  
  private static byte[] buildContentType(Object paramObject, String paramString1, String paramString2) throws UnsupportedEncodingException {
    StringBuilder stringBuilder = new StringBuilder("Content-Type: ");
    if (TextUtils.isEmpty(paramString1)) {
      if (paramObject instanceof String) {
        paramObject = "text/plain; charset=" + paramString2;
        stringBuilder.append((String)paramObject);
        return stringBuilder.toString().getBytes(paramString2);
      } 
      paramObject = "application/octet-stream";
      stringBuilder.append((String)paramObject);
      return stringBuilder.toString().getBytes(paramString2);
    } 
    paramObject = paramString1.replaceFirst("\\/jpg$", "/jpeg");
    stringBuilder.append((String)paramObject);
    return stringBuilder.toString().getBytes(paramString2);
  }
  
  private void generateContentType() {
    String str = Double.toHexString(Math.random() * 65535.0D);
    this.boundaryPostfixBytes = str.getBytes();
    this.contentType = "multipart/form-data; boundary=" + new String(BOUNDARY_PREFIX_BYTES) + str;
  }
  
  private void writeEntry(OutputStream paramOutputStream, String paramString, Object paramObject) throws IOException {
    byte[] arrayOfByte;
    writeLine(paramOutputStream, new byte[][] { TWO_DASHES_BYTES, BOUNDARY_PREFIX_BYTES, this.boundaryPostfixBytes });
    String str1 = "";
    String str2 = null;
    Object object = paramObject;
    if (paramObject instanceof BodyItemWrapper) {
      paramObject = paramObject;
      object = paramObject.getValue();
      str1 = paramObject.getFileName();
      str2 = paramObject.getContentType();
    } 
    if (object instanceof File) {
      File file = (File)object;
      paramObject = str1;
      if (TextUtils.isEmpty(str1))
        paramObject = file.getName(); 
      str1 = str2;
      if (TextUtils.isEmpty(str2))
        str1 = FileBody.getFileContentType(file); 
      writeLine(paramOutputStream, new byte[][] { buildContentDisposition(paramString, (String)paramObject, this.charset) });
      writeLine(paramOutputStream, new byte[][] { buildContentType(object, str1, this.charset) });
      writeLine(paramOutputStream, new byte[0][]);
      writeFile(paramOutputStream, file);
      writeLine(paramOutputStream, new byte[0][]);
      return;
    } 
    writeLine(paramOutputStream, new byte[][] { buildContentDisposition(paramString, str1, this.charset) });
    writeLine(paramOutputStream, new byte[][] { buildContentType(object, str2, this.charset) });
    writeLine(paramOutputStream, new byte[0][]);
    if (object instanceof InputStream) {
      writeStreamAndCloseIn(paramOutputStream, (InputStream)object);
      writeLine(paramOutputStream, new byte[0][]);
      return;
    } 
    if (object instanceof byte[]) {
      arrayOfByte = (byte[])object;
    } else {
      arrayOfByte = String.valueOf(object).getBytes(this.charset);
    } 
    writeLine(paramOutputStream, new byte[][] { arrayOfByte });
    this.current += arrayOfByte.length;
    if (this.callBackHandler != null && !this.callBackHandler.updateProgress(this.total, this.current, false))
      throw new Callback.CancelledException("upload stopped!"); 
  }
  
  private void writeFile(OutputStream paramOutputStream, File paramFile) throws IOException {
    if (paramOutputStream instanceof CounterOutputStream) {
      ((CounterOutputStream)paramOutputStream).addFile(paramFile);
      return;
    } 
    writeStreamAndCloseIn(paramOutputStream, new FileInputStream(paramFile));
  }
  
  private void writeLine(OutputStream paramOutputStream, byte[]... paramVarArgs) throws IOException {
    if (paramVarArgs != null) {
      int i = paramVarArgs.length;
      for (byte b = 0; b < i; b++)
        paramOutputStream.write(paramVarArgs[b]); 
    } 
    paramOutputStream.write(END_BYTES);
  }
  
  private void writeStreamAndCloseIn(OutputStream paramOutputStream, InputStream paramInputStream) throws IOException {
    if (paramOutputStream instanceof CounterOutputStream) {
      ((CounterOutputStream)paramOutputStream).addStream(paramInputStream);
      return;
    } 
    try {
      byte[] arrayOfByte = new byte[1024];
      while (true) {
        int i = paramInputStream.read(arrayOfByte);
        if (i >= 0) {
          paramOutputStream.write(arrayOfByte, 0, i);
          this.current += i;
          if (this.callBackHandler != null && !this.callBackHandler.updateProgress(this.total, this.current, false)) {
            Callback.CancelledException cancelledException = new Callback.CancelledException();
            this("upload stopped!");
            throw cancelledException;
          } 
          continue;
        } 
        return;
      } 
    } finally {
      IOUtil.closeQuietly(paramInputStream);
    } 
  }
  
  public long getContentLength() {
    return this.total;
  }
  
  public String getContentType() {
    return this.contentType;
  }
  
  public void setContentType(String paramString) {
    int i = this.contentType.indexOf(";");
    this.contentType = "multipart/" + paramString + this.contentType.substring(i);
  }
  
  public void setProgressHandler(ProgressHandler paramProgressHandler) {
    this.callBackHandler = paramProgressHandler;
  }
  
  public void writeTo(OutputStream paramOutputStream) throws IOException {
    if (this.callBackHandler != null && !this.callBackHandler.updateProgress(this.total, this.current, true))
      throw new Callback.CancelledException("upload stopped!"); 
    for (KeyValue keyValue : this.multipartParams) {
      String str = keyValue.key;
      Object object = keyValue.value;
      if (!TextUtils.isEmpty(str) && object != null)
        writeEntry(paramOutputStream, str, object); 
    } 
    writeLine(paramOutputStream, new byte[][] { TWO_DASHES_BYTES, BOUNDARY_PREFIX_BYTES, this.boundaryPostfixBytes, TWO_DASHES_BYTES });
    paramOutputStream.flush();
    if (this.callBackHandler != null)
      this.callBackHandler.updateProgress(this.total, this.total, true); 
  }
  
  private class CounterOutputStream extends OutputStream {
    final AtomicLong total = new AtomicLong(0L);
    
    public void addFile(File param1File) {
      if (this.total.get() != -1L)
        this.total.addAndGet(param1File.length()); 
    }
    
    public void addStream(InputStream param1InputStream) {
      if (this.total.get() != -1L) {
        long l = InputStreamBody.getInputStreamLength(param1InputStream);
        if (l > 0L) {
          this.total.addAndGet(l);
          return;
        } 
        this.total.set(-1L);
      } 
    }
    
    public void write(int param1Int) throws IOException {
      if (this.total.get() != -1L)
        this.total.incrementAndGet(); 
    }
    
    public void write(byte[] param1ArrayOfbyte) throws IOException {
      if (this.total.get() != -1L)
        this.total.addAndGet(param1ArrayOfbyte.length); 
    }
    
    public void write(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
      if (this.total.get() != -1L)
        this.total.addAndGet(param1Int2); 
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/body/MultipartBody.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */