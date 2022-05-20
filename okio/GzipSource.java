package okio;

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Inflater;

public final class GzipSource implements Source {
  private static final byte FCOMMENT = 4;
  
  private static final byte FEXTRA = 2;
  
  private static final byte FHCRC = 1;
  
  private static final byte FNAME = 3;
  
  private static final byte SECTION_BODY = 1;
  
  private static final byte SECTION_DONE = 3;
  
  private static final byte SECTION_HEADER = 0;
  
  private static final byte SECTION_TRAILER = 2;
  
  private final CRC32 crc = new CRC32();
  
  private final Inflater inflater;
  
  private final InflaterSource inflaterSource;
  
  private int section = 0;
  
  private final BufferedSource source;
  
  public GzipSource(Source paramSource) {
    if (paramSource == null)
      throw new IllegalArgumentException("source == null"); 
    this.inflater = new Inflater(true);
    this.source = Okio.buffer(paramSource);
    this.inflaterSource = new InflaterSource(this.source, this.inflater);
  }
  
  private void checkEqual(String paramString, int paramInt1, int paramInt2) throws IOException {
    if (paramInt2 != paramInt1)
      throw new IOException(String.format("%s: actual 0x%08x != expected 0x%08x", new Object[] { paramString, Integer.valueOf(paramInt2), Integer.valueOf(paramInt1) })); 
  }
  
  private void consumeHeader() throws IOException {
    boolean bool;
    this.source.require(10L);
    byte b = this.source.buffer().getByte(3L);
    if ((b >> 1 & 0x1) == 1) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool)
      updateCrc(this.source.buffer(), 0L, 10L); 
    checkEqual("ID1ID2", 8075, this.source.readShort());
    this.source.skip(8L);
    if ((b >> 2 & 0x1) == 1) {
      this.source.require(2L);
      if (bool)
        updateCrc(this.source.buffer(), 0L, 2L); 
      short s = this.source.buffer().readShortLe();
      this.source.require(s);
      if (bool)
        updateCrc(this.source.buffer(), 0L, s); 
      this.source.skip(s);
    } 
    if ((b >> 3 & 0x1) == 1) {
      long l = this.source.indexOf((byte)0);
      if (l == -1L)
        throw new EOFException(); 
      if (bool)
        updateCrc(this.source.buffer(), 0L, 1L + l); 
      this.source.skip(1L + l);
    } 
    if ((b >> 4 & 0x1) == 1) {
      long l = this.source.indexOf((byte)0);
      if (l == -1L)
        throw new EOFException(); 
      if (bool)
        updateCrc(this.source.buffer(), 0L, 1L + l); 
      this.source.skip(1L + l);
    } 
    if (bool) {
      checkEqual("FHCRC", this.source.readShortLe(), (short)(int)this.crc.getValue());
      this.crc.reset();
    } 
  }
  
  private void consumeTrailer() throws IOException {
    checkEqual("CRC", this.source.readIntLe(), (int)this.crc.getValue());
    checkEqual("ISIZE", this.source.readIntLe(), (int)this.inflater.getBytesWritten());
  }
  
  private void updateCrc(Buffer paramBuffer, long paramLong1, long paramLong2) {
    Segment segment2;
    long l1;
    long l2;
    Segment segment1 = paramBuffer.head;
    while (true) {
      segment2 = segment1;
      l1 = paramLong1;
      l2 = paramLong2;
      if (paramLong1 >= (segment1.limit - segment1.pos)) {
        paramLong1 -= (segment1.limit - segment1.pos);
        segment1 = segment1.next;
        continue;
      } 
      break;
    } 
    while (l2 > 0L) {
      int i = (int)(segment2.pos + l1);
      int j = (int)Math.min((segment2.limit - i), l2);
      this.crc.update(segment2.data, i, j);
      l2 -= j;
      l1 = 0L;
      segment2 = segment2.next;
    } 
  }
  
  public void close() throws IOException {
    this.inflaterSource.close();
  }
  
  public long read(Buffer paramBuffer, long paramLong) throws IOException {
    long l = 0L;
    if (paramLong < 0L)
      throw new IllegalArgumentException("byteCount < 0: " + paramLong); 
    if (paramLong == 0L)
      return l; 
    if (this.section == 0) {
      consumeHeader();
      this.section = 1;
    } 
    if (this.section == 1) {
      l = paramBuffer.size;
      paramLong = this.inflaterSource.read(paramBuffer, paramLong);
      if (paramLong != -1L) {
        updateCrc(paramBuffer, l, paramLong);
        return paramLong;
      } 
      this.section = 2;
    } 
    if (this.section == 2) {
      consumeTrailer();
      this.section = 3;
      if (!this.source.exhausted())
        throw new IOException("gzip finished without exhausting source"); 
    } 
    return -1L;
  }
  
  public Timeout timeout() {
    return this.source.timeout();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okio/GzipSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */