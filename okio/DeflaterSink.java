package okio;

import java.io.IOException;
import java.util.zip.Deflater;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

public final class DeflaterSink implements Sink {
  private boolean closed;
  
  private final Deflater deflater;
  
  private final BufferedSink sink;
  
  DeflaterSink(BufferedSink paramBufferedSink, Deflater paramDeflater) {
    if (paramBufferedSink == null)
      throw new IllegalArgumentException("source == null"); 
    if (paramDeflater == null)
      throw new IllegalArgumentException("inflater == null"); 
    this.sink = paramBufferedSink;
    this.deflater = paramDeflater;
  }
  
  public DeflaterSink(Sink paramSink, Deflater paramDeflater) {
    this(Okio.buffer(paramSink), paramDeflater);
  }
  
  @IgnoreJRERequirement
  private void deflate(boolean paramBoolean) throws IOException {
    Buffer buffer = this.sink.buffer();
    while (true) {
      int i;
      Segment segment = buffer.writableSegment(1);
      if (paramBoolean) {
        i = this.deflater.deflate(segment.data, segment.limit, 8192 - segment.limit, 2);
      } else {
        i = this.deflater.deflate(segment.data, segment.limit, 8192 - segment.limit);
      } 
      if (i > 0) {
        segment.limit += i;
        buffer.size += i;
        this.sink.emitCompleteSegments();
        continue;
      } 
      if (this.deflater.needsInput()) {
        if (segment.pos == segment.limit) {
          buffer.head = segment.pop();
          SegmentPool.recycle(segment);
        } 
        return;
      } 
    } 
  }
  
  public void close() throws IOException {
    Throwable throwable2;
    if (this.closed)
      return; 
    throwable1 = null;
    try {
      finishDeflate();
    } catch (Throwable throwable1) {}
    try {
      this.deflater.end();
      throwable2 = throwable1;
    } catch (Throwable throwable) {
      throwable2 = throwable1;
    } 
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
  
  void finishDeflate() throws IOException {
    this.deflater.finish();
    deflate(false);
  }
  
  public void flush() throws IOException {
    deflate(true);
    this.sink.flush();
  }
  
  public Timeout timeout() {
    return this.sink.timeout();
  }
  
  public String toString() {
    return "DeflaterSink(" + this.sink + ")";
  }
  
  public void write(Buffer paramBuffer, long paramLong) throws IOException {
    Util.checkOffsetAndCount(paramBuffer.size, 0L, paramLong);
    while (paramLong > 0L) {
      Segment segment = paramBuffer.head;
      int i = (int)Math.min(paramLong, (segment.limit - segment.pos));
      this.deflater.setInput(segment.data, segment.pos, i);
      deflate(false);
      paramBuffer.size -= i;
      segment.pos += i;
      if (segment.pos == segment.limit) {
        paramBuffer.head = segment.pop();
        SegmentPool.recycle(segment);
      } 
      paramLong -= i;
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okio/DeflaterSink.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */