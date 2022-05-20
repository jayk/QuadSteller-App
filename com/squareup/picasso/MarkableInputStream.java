package com.squareup.picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

final class MarkableInputStream extends InputStream {
  private static final int DEFAULT_BUFFER_SIZE = 4096;
  
  private long defaultMark = -1L;
  
  private final InputStream in;
  
  private long limit;
  
  private long offset;
  
  private long reset;
  
  public MarkableInputStream(InputStream paramInputStream) {
    this(paramInputStream, 4096);
  }
  
  public MarkableInputStream(InputStream paramInputStream, int paramInt) {
    InputStream inputStream = paramInputStream;
    if (!paramInputStream.markSupported())
      inputStream = new BufferedInputStream(paramInputStream, paramInt); 
    this.in = inputStream;
  }
  
  private void setLimit(long paramLong) {
    try {
      if (this.reset < this.offset && this.offset <= this.limit) {
        this.in.reset();
        this.in.mark((int)(paramLong - this.reset));
        skip(this.reset, this.offset);
      } else {
        this.reset = this.offset;
        this.in.mark((int)(paramLong - this.offset));
      } 
      this.limit = paramLong;
      return;
    } catch (IOException iOException) {
      throw new IllegalStateException("Unable to mark: " + iOException);
    } 
  }
  
  private void skip(long paramLong1, long paramLong2) throws IOException {
    while (true) {
      long l;
      if (paramLong1 < paramLong2) {
        long l1 = this.in.skip(paramLong2 - paramLong1);
        l = l1;
        if (l1 == 0L) {
          if (read() == -1)
            return; 
          l = 1L;
        } 
      } else {
        return;
      } 
      paramLong1 += l;
    } 
  }
  
  public int available() throws IOException {
    return this.in.available();
  }
  
  public void close() throws IOException {
    this.in.close();
  }
  
  public void mark(int paramInt) {
    this.defaultMark = savePosition(paramInt);
  }
  
  public boolean markSupported() {
    return this.in.markSupported();
  }
  
  public int read() throws IOException {
    int i = this.in.read();
    if (i != -1)
      this.offset++; 
    return i;
  }
  
  public int read(byte[] paramArrayOfbyte) throws IOException {
    int i = this.in.read(paramArrayOfbyte);
    if (i != -1)
      this.offset += i; 
    return i;
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    paramInt1 = this.in.read(paramArrayOfbyte, paramInt1, paramInt2);
    if (paramInt1 != -1)
      this.offset += paramInt1; 
    return paramInt1;
  }
  
  public void reset() throws IOException {
    reset(this.defaultMark);
  }
  
  public void reset(long paramLong) throws IOException {
    if (this.offset > this.limit || paramLong < this.reset)
      throw new IOException("Cannot reset"); 
    this.in.reset();
    skip(this.reset, paramLong);
    this.offset = paramLong;
  }
  
  public long savePosition(int paramInt) {
    long l = this.offset + paramInt;
    if (this.limit < l)
      setLimit(l); 
    return this.offset;
  }
  
  public long skip(long paramLong) throws IOException {
    paramLong = this.in.skip(paramLong);
    this.offset += paramLong;
    return paramLong;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/squareup/picasso/MarkableInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */