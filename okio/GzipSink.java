package okio;

import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Deflater;

public final class GzipSink implements Sink {
  private boolean closed;
  
  private final CRC32 crc = new CRC32();
  
  private final Deflater deflater;
  
  private final DeflaterSink deflaterSink;
  
  private final BufferedSink sink;
  
  public GzipSink(Sink paramSink) {
    if (paramSink == null)
      throw new IllegalArgumentException("sink == null"); 
    this.deflater = new Deflater(-1, true);
    this.sink = Okio.buffer(paramSink);
    this.deflaterSink = new DeflaterSink(this.sink, this.deflater);
    writeHeader();
  }
  
  private void updateCrc(Buffer paramBuffer, long paramLong) {
    for (Segment segment = paramBuffer.head; paramLong > 0L; segment = segment.next) {
      int i = (int)Math.min(paramLong, (segment.limit - segment.pos));
      this.crc.update(segment.data, segment.pos, i);
      paramLong -= i;
    } 
  }
  
  private void writeFooter() throws IOException {
    this.sink.writeIntLe((int)this.crc.getValue());
    this.sink.writeIntLe((int)this.deflater.getBytesRead());
  }
  
  private void writeHeader() {
    Buffer buffer = this.sink.buffer();
    buffer.writeShort(8075);
    buffer.writeByte(8);
    buffer.writeByte(0);
    buffer.writeInt(0);
    buffer.writeByte(0);
    buffer.writeByte(0);
  }
  
  public void close() throws IOException {
    Throwable throwable2;
    if (this.closed)
      return; 
    throwable1 = null;
    try {
      this.deflaterSink.finishDeflate();
      writeFooter();
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
  
  public Deflater deflater() {
    return this.deflater;
  }
  
  public void flush() throws IOException {
    this.deflaterSink.flush();
  }
  
  public Timeout timeout() {
    return this.sink.timeout();
  }
  
  public void write(Buffer paramBuffer, long paramLong) throws IOException {
    if (paramLong < 0L)
      throw new IllegalArgumentException("byteCount < 0: " + paramLong); 
    if (paramLong != 0L) {
      updateCrc(paramBuffer, paramLong);
      this.deflaterSink.write(paramBuffer, paramLong);
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okio/GzipSink.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */