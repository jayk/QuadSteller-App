package okhttp3;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSource;

public abstract class ResponseBody implements Closeable {
  private Reader reader;
  
  private Charset charset() {
    MediaType mediaType = contentType();
    return (mediaType != null) ? mediaType.charset(Util.UTF_8) : Util.UTF_8;
  }
  
  public static ResponseBody create(@Nullable final MediaType contentType, final long contentLength, final BufferedSource content) {
    if (content == null)
      throw new NullPointerException("source == null"); 
    return new ResponseBody() {
        public long contentLength() {
          return contentLength;
        }
        
        @Nullable
        public MediaType contentType() {
          return contentType;
        }
        
        public BufferedSource source() {
          return content;
        }
      };
  }
  
  public static ResponseBody create(@Nullable MediaType paramMediaType, String paramString) {
    Charset charset = Util.UTF_8;
    MediaType mediaType = paramMediaType;
    if (paramMediaType != null) {
      Charset charset1 = paramMediaType.charset();
      charset = charset1;
      mediaType = paramMediaType;
      if (charset1 == null) {
        charset = Util.UTF_8;
        mediaType = MediaType.parse(paramMediaType + "; charset=utf-8");
      } 
    } 
    Buffer buffer = (new Buffer()).writeString(paramString, charset);
    return create(mediaType, buffer.size(), (BufferedSource)buffer);
  }
  
  public static ResponseBody create(@Nullable MediaType paramMediaType, byte[] paramArrayOfbyte) {
    Buffer buffer = (new Buffer()).write(paramArrayOfbyte);
    return create(paramMediaType, paramArrayOfbyte.length, (BufferedSource)buffer);
  }
  
  public final InputStream byteStream() {
    return source().inputStream();
  }
  
  public final byte[] bytes() throws IOException {
    long l = contentLength();
    if (l > 2147483647L)
      throw new IOException("Cannot buffer entire body for content length: " + l); 
    BufferedSource bufferedSource = source();
    try {
      byte[] arrayOfByte = bufferedSource.readByteArray();
      Util.closeQuietly((Closeable)bufferedSource);
    } finally {
      Util.closeQuietly((Closeable)bufferedSource);
    } 
    return (byte[])SYNTHETIC_LOCAL_VARIABLE_4;
  }
  
  public final Reader charStream() {
    Reader reader = this.reader;
    if (reader == null) {
      reader = new BomAwareReader(source(), charset());
      this.reader = reader;
    } 
    return reader;
  }
  
  public void close() {
    Util.closeQuietly((Closeable)source());
  }
  
  public abstract long contentLength();
  
  @Nullable
  public abstract MediaType contentType();
  
  public abstract BufferedSource source();
  
  public final String string() throws IOException {
    BufferedSource bufferedSource = source();
    try {
      return bufferedSource.readString(Util.bomAwareCharset(bufferedSource, charset()));
    } finally {
      Util.closeQuietly((Closeable)bufferedSource);
    } 
  }
  
  static final class BomAwareReader extends Reader {
    private final Charset charset;
    
    private boolean closed;
    
    private Reader delegate;
    
    private final BufferedSource source;
    
    BomAwareReader(BufferedSource param1BufferedSource, Charset param1Charset) {
      this.source = param1BufferedSource;
      this.charset = param1Charset;
    }
    
    public void close() throws IOException {
      this.closed = true;
      if (this.delegate != null) {
        this.delegate.close();
        return;
      } 
      this.source.close();
    }
    
    public int read(char[] param1ArrayOfchar, int param1Int1, int param1Int2) throws IOException {
      if (this.closed)
        throw new IOException("Stream closed"); 
      Reader reader1 = this.delegate;
      Reader reader2 = reader1;
      if (reader1 == null) {
        Charset charset = Util.bomAwareCharset(this.source, this.charset);
        reader2 = new InputStreamReader(this.source.inputStream(), charset);
        this.delegate = reader2;
      } 
      return reader2.read(param1ArrayOfchar, param1Int1, param1Int2);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/ResponseBody.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */