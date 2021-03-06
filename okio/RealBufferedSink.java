package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

final class RealBufferedSink implements BufferedSink {
  public final Buffer buffer = new Buffer();
  
  boolean closed;
  
  public final Sink sink;
  
  RealBufferedSink(Sink paramSink) {
    if (paramSink == null)
      throw new NullPointerException("sink == null"); 
    this.sink = paramSink;
  }
  
  public Buffer buffer() {
    return this.buffer;
  }
  
  public void close() throws IOException {
    if (this.closed)
      return; 
    Throwable throwable1 = null;
    throwable2 = throwable1;
    try {
      if (this.buffer.size > 0L) {
        this.sink.write(this.buffer, this.buffer.size);
        throwable2 = throwable1;
      } 
    } catch (Throwable throwable2) {}
    try {
      this.sink.close();
      throwable1 = throwable2;
    } catch (Throwable throwable) {
      throwable1 = throwable2;
    } 
    this.closed = true;
    if (throwable1 != null)
      Util.sneakyRethrow(throwable1); 
  }
  
  public BufferedSink emit() throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    long l = this.buffer.size();
    if (l > 0L)
      this.sink.write(this.buffer, l); 
    return this;
  }
  
  public BufferedSink emitCompleteSegments() throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    long l = this.buffer.completeSegmentByteCount();
    if (l > 0L)
      this.sink.write(this.buffer, l); 
    return this;
  }
  
  public void flush() throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    if (this.buffer.size > 0L)
      this.sink.write(this.buffer, this.buffer.size); 
    this.sink.flush();
  }
  
  public OutputStream outputStream() {
    return new OutputStream() {
        public void close() throws IOException {
          RealBufferedSink.this.close();
        }
        
        public void flush() throws IOException {
          if (!RealBufferedSink.this.closed)
            RealBufferedSink.this.flush(); 
        }
        
        public String toString() {
          return RealBufferedSink.this + ".outputStream()";
        }
        
        public void write(int param1Int) throws IOException {
          if (RealBufferedSink.this.closed)
            throw new IOException("closed"); 
          RealBufferedSink.this.buffer.writeByte((byte)param1Int);
          RealBufferedSink.this.emitCompleteSegments();
        }
        
        public void write(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
          if (RealBufferedSink.this.closed)
            throw new IOException("closed"); 
          RealBufferedSink.this.buffer.write(param1ArrayOfbyte, param1Int1, param1Int2);
          RealBufferedSink.this.emitCompleteSegments();
        }
      };
  }
  
  public Timeout timeout() {
    return this.sink.timeout();
  }
  
  public String toString() {
    return "buffer(" + this.sink + ")";
  }
  
  public BufferedSink write(ByteString paramByteString) throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    this.buffer.write(paramByteString);
    return emitCompleteSegments();
  }
  
  public BufferedSink write(Source paramSource, long paramLong) throws IOException {
    while (paramLong > 0L) {
      long l = paramSource.read(this.buffer, paramLong);
      if (l == -1L)
        throw new EOFException(); 
      paramLong -= l;
      emitCompleteSegments();
    } 
    return this;
  }
  
  public BufferedSink write(byte[] paramArrayOfbyte) throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    this.buffer.write(paramArrayOfbyte);
    return emitCompleteSegments();
  }
  
  public BufferedSink write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    this.buffer.write(paramArrayOfbyte, paramInt1, paramInt2);
    return emitCompleteSegments();
  }
  
  public void write(Buffer paramBuffer, long paramLong) throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    this.buffer.write(paramBuffer, paramLong);
    emitCompleteSegments();
  }
  
  public long writeAll(Source paramSource) throws IOException {
    if (paramSource == null)
      throw new IllegalArgumentException("source == null"); 
    long l = 0L;
    while (true) {
      long l1 = paramSource.read(this.buffer, 8192L);
      if (l1 != -1L) {
        l += l1;
        emitCompleteSegments();
        continue;
      } 
      return l;
    } 
  }
  
  public BufferedSink writeByte(int paramInt) throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    this.buffer.writeByte(paramInt);
    return emitCompleteSegments();
  }
  
  public BufferedSink writeDecimalLong(long paramLong) throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    this.buffer.writeDecimalLong(paramLong);
    return emitCompleteSegments();
  }
  
  public BufferedSink writeHexadecimalUnsignedLong(long paramLong) throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    this.buffer.writeHexadecimalUnsignedLong(paramLong);
    return emitCompleteSegments();
  }
  
  public BufferedSink writeInt(int paramInt) throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    this.buffer.writeInt(paramInt);
    return emitCompleteSegments();
  }
  
  public BufferedSink writeIntLe(int paramInt) throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    this.buffer.writeIntLe(paramInt);
    return emitCompleteSegments();
  }
  
  public BufferedSink writeLong(long paramLong) throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    this.buffer.writeLong(paramLong);
    return emitCompleteSegments();
  }
  
  public BufferedSink writeLongLe(long paramLong) throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    this.buffer.writeLongLe(paramLong);
    return emitCompleteSegments();
  }
  
  public BufferedSink writeShort(int paramInt) throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    this.buffer.writeShort(paramInt);
    return emitCompleteSegments();
  }
  
  public BufferedSink writeShortLe(int paramInt) throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    this.buffer.writeShortLe(paramInt);
    return emitCompleteSegments();
  }
  
  public BufferedSink writeString(String paramString, int paramInt1, int paramInt2, Charset paramCharset) throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    this.buffer.writeString(paramString, paramInt1, paramInt2, paramCharset);
    return emitCompleteSegments();
  }
  
  public BufferedSink writeString(String paramString, Charset paramCharset) throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    this.buffer.writeString(paramString, paramCharset);
    return emitCompleteSegments();
  }
  
  public BufferedSink writeUtf8(String paramString) throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    this.buffer.writeUtf8(paramString);
    return emitCompleteSegments();
  }
  
  public BufferedSink writeUtf8(String paramString, int paramInt1, int paramInt2) throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    this.buffer.writeUtf8(paramString, paramInt1, paramInt2);
    return emitCompleteSegments();
  }
  
  public BufferedSink writeUtf8CodePoint(int paramInt) throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    this.buffer.writeUtf8CodePoint(paramInt);
    return emitCompleteSegments();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okio/RealBufferedSink.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */