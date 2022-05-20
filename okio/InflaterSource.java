package okio;

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public final class InflaterSource implements Source {
  private int bufferBytesHeldByInflater;
  
  private boolean closed;
  
  private final Inflater inflater;
  
  private final BufferedSource source;
  
  InflaterSource(BufferedSource paramBufferedSource, Inflater paramInflater) {
    if (paramBufferedSource == null)
      throw new IllegalArgumentException("source == null"); 
    if (paramInflater == null)
      throw new IllegalArgumentException("inflater == null"); 
    this.source = paramBufferedSource;
    this.inflater = paramInflater;
  }
  
  public InflaterSource(Source paramSource, Inflater paramInflater) {
    this(Okio.buffer(paramSource), paramInflater);
  }
  
  private void releaseInflatedBytes() throws IOException {
    if (this.bufferBytesHeldByInflater != 0) {
      int i = this.bufferBytesHeldByInflater - this.inflater.getRemaining();
      this.bufferBytesHeldByInflater -= i;
      this.source.skip(i);
    } 
  }
  
  public void close() throws IOException {
    if (!this.closed) {
      this.inflater.end();
      this.closed = true;
      this.source.close();
    } 
  }
  
  public long read(Buffer paramBuffer, long paramLong) throws IOException {
    long l = 0L;
    if (paramLong < 0L)
      throw new IllegalArgumentException("byteCount < 0: " + paramLong); 
    if (this.closed)
      throw new IllegalStateException("closed"); 
    if (paramLong == 0L)
      return l; 
    while (true) {
      boolean bool = refill();
      try {
        Segment segment = paramBuffer.writableSegment(1);
        int i = this.inflater.inflate(segment.data, segment.limit, 8192 - segment.limit);
        if (i > 0) {
          segment.limit += i;
          paramBuffer.size += i;
          return i;
        } 
        if (this.inflater.finished() || this.inflater.needsDictionary()) {
          releaseInflatedBytes();
          if (segment.pos == segment.limit) {
            paramBuffer.head = segment.pop();
            SegmentPool.recycle(segment);
          } 
          return -1L;
        } 
        if (bool) {
          EOFException eOFException = new EOFException();
          this("source exhausted prematurely");
          throw eOFException;
        } 
      } catch (DataFormatException dataFormatException) {
        throw new IOException(dataFormatException);
      } 
    } 
  }
  
  public boolean refill() throws IOException {
    boolean bool = false;
    if (this.inflater.needsInput()) {
      releaseInflatedBytes();
      if (this.inflater.getRemaining() != 0)
        throw new IllegalStateException("?"); 
      if (this.source.exhausted())
        return true; 
      Segment segment = (this.source.buffer()).head;
      this.bufferBytesHeldByInflater = segment.limit - segment.pos;
      this.inflater.setInput(segment.data, segment.pos, this.bufferBytesHeldByInflater);
    } 
    return bool;
  }
  
  public Timeout timeout() {
    return this.source.timeout();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okio/InflaterSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */