package okhttp3.internal.ws;

import java.io.IOException;
import java.util.Random;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import okio.Sink;
import okio.Timeout;

final class WebSocketWriter {
  boolean activeWriter;
  
  final Buffer buffer;
  
  final FrameSink frameSink;
  
  final boolean isClient;
  
  final byte[] maskBuffer;
  
  final byte[] maskKey;
  
  final Random random;
  
  final BufferedSink sink;
  
  boolean writerClosed;
  
  static {
    boolean bool;
    if (!WebSocketWriter.class.desiredAssertionStatus()) {
      bool = true;
    } else {
      bool = false;
    } 
    $assertionsDisabled = bool;
  }
  
  WebSocketWriter(boolean paramBoolean, BufferedSink paramBufferedSink, Random paramRandom) {
    byte[] arrayOfByte;
    this.buffer = new Buffer();
    this.frameSink = new FrameSink();
    if (paramBufferedSink == null)
      throw new NullPointerException("sink == null"); 
    if (paramRandom == null)
      throw new NullPointerException("random == null"); 
    this.isClient = paramBoolean;
    this.sink = paramBufferedSink;
    this.random = paramRandom;
    if (paramBoolean) {
      arrayOfByte = new byte[4];
    } else {
      paramBufferedSink = null;
    } 
    this.maskKey = (byte[])paramBufferedSink;
    paramBufferedSink = bufferedSink;
    if (paramBoolean)
      arrayOfByte = new byte[8192]; 
    this.maskBuffer = arrayOfByte;
  }
  
  private void writeControlFrameSynchronized(int paramInt, ByteString paramByteString) throws IOException {
    byte[] arrayOfByte;
    assert Thread.holdsLock(this);
    if (this.writerClosed)
      throw new IOException("closed"); 
    int i = paramByteString.size();
    if (i > 125L)
      throw new IllegalArgumentException("Payload size must be less than or equal to 125"); 
    this.sink.writeByte(paramInt | 0x80);
    if (this.isClient) {
      this.sink.writeByte(i | 0x80);
      this.random.nextBytes(this.maskKey);
      this.sink.write(this.maskKey);
      arrayOfByte = paramByteString.toByteArray();
      WebSocketProtocol.toggleMask(arrayOfByte, arrayOfByte.length, this.maskKey, 0L);
      this.sink.write(arrayOfByte);
    } else {
      this.sink.writeByte(i);
      this.sink.write((ByteString)arrayOfByte);
    } 
    this.sink.flush();
  }
  
  Sink newMessageSink(int paramInt, long paramLong) {
    if (this.activeWriter)
      throw new IllegalStateException("Another message writer is active. Did you call close()?"); 
    this.activeWriter = true;
    this.frameSink.formatOpcode = paramInt;
    this.frameSink.contentLength = paramLong;
    this.frameSink.isFirstFrame = true;
    this.frameSink.closed = false;
    return this.frameSink;
  }
  
  void writeClose(int paramInt, ByteString paramByteString) throws IOException {
    ByteString byteString = ByteString.EMPTY;
    if (paramInt != 0 || paramByteString != null) {
      if (paramInt != 0)
        WebSocketProtocol.validateCloseCode(paramInt); 
      Buffer buffer = new Buffer();
      buffer.writeShort(paramInt);
      if (paramByteString != null)
        buffer.write(paramByteString); 
      byteString = buffer.readByteString();
    } 
    /* monitor enter ThisExpression{ObjectType{okhttp3/internal/ws/WebSocketWriter}} */
    try {
      writeControlFrameSynchronized(8, byteString);
    } finally {
      this.writerClosed = true;
    } 
    /* monitor exit ThisExpression{ObjectType{okhttp3/internal/ws/WebSocketWriter}} */
    throw paramByteString;
  }
  
  void writeMessageFrameSynchronized(int paramInt, long paramLong, boolean paramBoolean1, boolean paramBoolean2) throws IOException {
    assert Thread.holdsLock(this);
    if (this.writerClosed)
      throw new IOException("closed"); 
    if (!paramBoolean1)
      paramInt = 0; 
    int i = paramInt;
    if (paramBoolean2)
      i = paramInt | 0x80; 
    this.sink.writeByte(i);
    paramInt = 0;
    if (this.isClient)
      paramInt = Character.MIN_VALUE | 0x80; 
    if (paramLong <= 125L) {
      i = (int)paramLong;
      this.sink.writeByte(paramInt | i);
    } else if (paramLong <= 65535L) {
      this.sink.writeByte(paramInt | 0x7E);
      this.sink.writeShort((int)paramLong);
    } else {
      this.sink.writeByte(paramInt | 0x7F);
      this.sink.writeLong(paramLong);
    } 
    if (this.isClient) {
      this.random.nextBytes(this.maskKey);
      this.sink.write(this.maskKey);
      long l;
      for (l = 0L; l < paramLong; l += paramInt) {
        paramInt = (int)Math.min(paramLong, this.maskBuffer.length);
        paramInt = this.buffer.read(this.maskBuffer, 0, paramInt);
        if (paramInt == -1)
          throw new AssertionError(); 
        WebSocketProtocol.toggleMask(this.maskBuffer, paramInt, this.maskKey, l);
        this.sink.write(this.maskBuffer, 0, paramInt);
      } 
    } else {
      this.sink.write(this.buffer, paramLong);
    } 
    this.sink.emit();
  }
  
  void writePing(ByteString paramByteString) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: bipush #9
    //   5: aload_1
    //   6: invokespecial writeControlFrameSynchronized : (ILokio/ByteString;)V
    //   9: aload_0
    //   10: monitorexit
    //   11: return
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_1
    //   16: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	12	finally
    //   13	15	12	finally
  }
  
  void writePong(ByteString paramByteString) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: bipush #10
    //   5: aload_1
    //   6: invokespecial writeControlFrameSynchronized : (ILokio/ByteString;)V
    //   9: aload_0
    //   10: monitorexit
    //   11: return
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_1
    //   16: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	12	finally
    //   13	15	12	finally
  }
  
  final class FrameSink implements Sink {
    boolean closed;
    
    long contentLength;
    
    int formatOpcode;
    
    boolean isFirstFrame;
    
    public void close() throws IOException {
      if (this.closed)
        throw new IOException("closed"); 
      synchronized (WebSocketWriter.this) {
        WebSocketWriter.this.writeMessageFrameSynchronized(this.formatOpcode, WebSocketWriter.this.buffer.size(), this.isFirstFrame, true);
        this.closed = true;
        WebSocketWriter.this.activeWriter = false;
        return;
      } 
    }
    
    public void flush() throws IOException {
      if (this.closed)
        throw new IOException("closed"); 
      synchronized (WebSocketWriter.this) {
        WebSocketWriter.this.writeMessageFrameSynchronized(this.formatOpcode, WebSocketWriter.this.buffer.size(), this.isFirstFrame, false);
        this.isFirstFrame = false;
        return;
      } 
    }
    
    public Timeout timeout() {
      return WebSocketWriter.this.sink.timeout();
    }
    
    public void write(Buffer param1Buffer, long param1Long) throws IOException {
      boolean bool;
      if (this.closed)
        throw new IOException("closed"); 
      WebSocketWriter.this.buffer.write(param1Buffer, param1Long);
      if (this.isFirstFrame && this.contentLength != -1L && WebSocketWriter.this.buffer.size() > this.contentLength - 8192L) {
        bool = true;
      } else {
        bool = false;
      } 
      param1Long = WebSocketWriter.this.buffer.completeSegmentByteCount();
      if (param1Long > 0L && !bool)
        synchronized (WebSocketWriter.this) {
          WebSocketWriter.this.writeMessageFrameSynchronized(this.formatOpcode, param1Long, this.isFirstFrame, false);
          this.isFirstFrame = false;
          return;
        }  
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/ws/WebSocketWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */