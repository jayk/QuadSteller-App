package okhttp3.internal.http2;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Source;
import okio.Timeout;

final class Http2Reader implements Closeable {
  static final Logger logger = Logger.getLogger(Http2.class.getName());
  
  private final boolean client;
  
  private final ContinuationSource continuation;
  
  final Hpack.Reader hpackReader;
  
  private final BufferedSource source;
  
  Http2Reader(BufferedSource paramBufferedSource, boolean paramBoolean) {
    this.source = paramBufferedSource;
    this.client = paramBoolean;
    this.continuation = new ContinuationSource(this.source);
    this.hpackReader = new Hpack.Reader(4096, this.continuation);
  }
  
  static int lengthWithoutPadding(int paramInt, byte paramByte, short paramShort) throws IOException {
    int i = paramInt;
    if ((paramByte & 0x8) != 0)
      i = paramInt - 1; 
    if (paramShort > i)
      throw Http2.ioException("PROTOCOL_ERROR padding %s > remaining length %s", new Object[] { Short.valueOf(paramShort), Integer.valueOf(i) }); 
    return (short)(i - paramShort);
  }
  
  private void readData(Handler paramHandler, int paramInt1, byte paramByte, int paramInt2) throws IOException {
    boolean bool;
    short s1 = 1;
    short s2 = 0;
    if (paramInt2 == 0)
      throw Http2.ioException("PROTOCOL_ERROR: TYPE_DATA streamId == 0", new Object[0]); 
    if ((paramByte & 0x1) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    if ((paramByte & 0x20) == 0)
      s1 = 0; 
    if (s1)
      throw Http2.ioException("PROTOCOL_ERROR: FLAG_COMPRESSED without SETTINGS_COMPRESS_DATA", new Object[0]); 
    short s3 = s2;
    if ((paramByte & 0x8) != 0) {
      s1 = (short)(this.source.readByte() & 0xFF);
      s3 = s1;
    } 
    paramInt1 = lengthWithoutPadding(paramInt1, paramByte, s3);
    paramHandler.data(bool, paramInt2, this.source, paramInt1);
    this.source.skip(s3);
  }
  
  private void readGoAway(Handler paramHandler, int paramInt1, byte paramByte, int paramInt2) throws IOException {
    if (paramInt1 < 8)
      throw Http2.ioException("TYPE_GOAWAY length < 8: %s", new Object[] { Integer.valueOf(paramInt1) }); 
    if (paramInt2 != 0)
      throw Http2.ioException("TYPE_GOAWAY streamId != 0", new Object[0]); 
    int i = this.source.readInt();
    paramInt2 = this.source.readInt();
    paramInt1 -= 8;
    ErrorCode errorCode = ErrorCode.fromHttp2(paramInt2);
    if (errorCode == null)
      throw Http2.ioException("TYPE_GOAWAY unexpected error code: %d", new Object[] { Integer.valueOf(paramInt2) }); 
    ByteString byteString = ByteString.EMPTY;
    if (paramInt1 > 0)
      byteString = this.source.readByteString(paramInt1); 
    paramHandler.goAway(i, errorCode, byteString);
  }
  
  private List<Header> readHeaderBlock(int paramInt1, short paramShort, byte paramByte, int paramInt2) throws IOException {
    ContinuationSource continuationSource = this.continuation;
    this.continuation.left = paramInt1;
    continuationSource.length = paramInt1;
    this.continuation.padding = (short)paramShort;
    this.continuation.flags = (byte)paramByte;
    this.continuation.streamId = paramInt2;
    this.hpackReader.readHeaders();
    return this.hpackReader.getAndResetHeaderList();
  }
  
  private void readHeaders(Handler paramHandler, int paramInt1, byte paramByte, int paramInt2) throws IOException {
    boolean bool;
    int i = 0;
    if (paramInt2 == 0)
      throw Http2.ioException("PROTOCOL_ERROR: TYPE_HEADERS streamId == 0", new Object[0]); 
    if ((paramByte & 0x1) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    int j = i;
    if ((paramByte & 0x8) != 0) {
      i = (short)(this.source.readByte() & 0xFF);
      j = i;
    } 
    i = paramInt1;
    if ((paramByte & 0x20) != 0) {
      readPriority(paramHandler, paramInt2);
      i = paramInt1 - 5;
    } 
    paramHandler.headers(bool, paramInt2, -1, readHeaderBlock(lengthWithoutPadding(i, paramByte, j), j, paramByte, paramInt2));
  }
  
  static int readMedium(BufferedSource paramBufferedSource) throws IOException {
    return (paramBufferedSource.readByte() & 0xFF) << 16 | (paramBufferedSource.readByte() & 0xFF) << 8 | paramBufferedSource.readByte() & 0xFF;
  }
  
  private void readPing(Handler paramHandler, int paramInt1, byte paramByte, int paramInt2) throws IOException {
    boolean bool = true;
    if (paramInt1 != 8)
      throw Http2.ioException("TYPE_PING length != 8: %s", new Object[] { Integer.valueOf(paramInt1) }); 
    if (paramInt2 != 0)
      throw Http2.ioException("TYPE_PING streamId != 0", new Object[0]); 
    paramInt1 = this.source.readInt();
    paramInt2 = this.source.readInt();
    if ((paramByte & 0x1) == 0)
      bool = false; 
    paramHandler.ping(bool, paramInt1, paramInt2);
  }
  
  private void readPriority(Handler paramHandler, int paramInt) throws IOException {
    boolean bool;
    int i = this.source.readInt();
    if ((Integer.MIN_VALUE & i) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    paramHandler.priority(paramInt, i & Integer.MAX_VALUE, (this.source.readByte() & 0xFF) + 1, bool);
  }
  
  private void readPriority(Handler paramHandler, int paramInt1, byte paramByte, int paramInt2) throws IOException {
    if (paramInt1 != 5)
      throw Http2.ioException("TYPE_PRIORITY length: %d != 5", new Object[] { Integer.valueOf(paramInt1) }); 
    if (paramInt2 == 0)
      throw Http2.ioException("TYPE_PRIORITY streamId == 0", new Object[0]); 
    readPriority(paramHandler, paramInt2);
  }
  
  private void readPushPromise(Handler paramHandler, int paramInt1, byte paramByte, int paramInt2) throws IOException {
    short s1 = 0;
    if (paramInt2 == 0)
      throw Http2.ioException("PROTOCOL_ERROR: TYPE_PUSH_PROMISE streamId == 0", new Object[0]); 
    short s2 = s1;
    if ((paramByte & 0x8) != 0) {
      s1 = (short)(this.source.readByte() & 0xFF);
      s2 = s1;
    } 
    paramHandler.pushPromise(paramInt2, this.source.readInt() & Integer.MAX_VALUE, readHeaderBlock(lengthWithoutPadding(paramInt1 - 4, paramByte, s2), s2, paramByte, paramInt2));
  }
  
  private void readRstStream(Handler paramHandler, int paramInt1, byte paramByte, int paramInt2) throws IOException {
    if (paramInt1 != 4)
      throw Http2.ioException("TYPE_RST_STREAM length: %d != 4", new Object[] { Integer.valueOf(paramInt1) }); 
    if (paramInt2 == 0)
      throw Http2.ioException("TYPE_RST_STREAM streamId == 0", new Object[0]); 
    paramInt1 = this.source.readInt();
    ErrorCode errorCode = ErrorCode.fromHttp2(paramInt1);
    if (errorCode == null)
      throw Http2.ioException("TYPE_RST_STREAM unexpected error code: %d", new Object[] { Integer.valueOf(paramInt1) }); 
    paramHandler.rstStream(paramInt2, errorCode);
  }
  
  private void readSettings(Handler paramHandler, int paramInt1, byte paramByte, int paramInt2) throws IOException {
    if (paramInt2 != 0)
      throw Http2.ioException("TYPE_SETTINGS streamId != 0", new Object[0]); 
    if ((paramByte & 0x1) != 0) {
      if (paramInt1 != 0)
        throw Http2.ioException("FRAME_SIZE_ERROR ack frame should be empty!", new Object[0]); 
      paramHandler.ackSettings();
      return;
    } 
    if (paramInt1 % 6 != 0)
      throw Http2.ioException("TYPE_SETTINGS length %% 6 != 0: %s", new Object[] { Integer.valueOf(paramInt1) }); 
    Settings settings = new Settings();
    paramInt2 = 0;
    while (paramInt2 < paramInt1) {
      short s1 = this.source.readShort();
      int i = this.source.readInt();
      short s = s1;
      switch (s1) {
        default:
          s = s1;
        case 1:
        case 6:
          settings.set(s, i);
          paramInt2 += 6;
          continue;
        case 2:
          s = s1;
          if (i != 0) {
            s = s1;
            if (i != 1)
              throw Http2.ioException("PROTOCOL_ERROR SETTINGS_ENABLE_PUSH != 0 or 1", new Object[0]); 
          } 
        case 3:
          s = 4;
        case 4:
          s = 7;
          if (i < 0)
            throw Http2.ioException("PROTOCOL_ERROR SETTINGS_INITIAL_WINDOW_SIZE > 2^31 - 1", new Object[0]); 
        case 5:
          break;
      } 
      if (i >= 16384) {
        s = s1;
        if (i > 16777215)
          throw Http2.ioException("PROTOCOL_ERROR SETTINGS_MAX_FRAME_SIZE: %s", new Object[] { Integer.valueOf(i) }); 
      } 
      throw Http2.ioException("PROTOCOL_ERROR SETTINGS_MAX_FRAME_SIZE: %s", new Object[] { Integer.valueOf(i) });
    } 
    paramHandler.settings(false, settings);
  }
  
  private void readWindowUpdate(Handler paramHandler, int paramInt1, byte paramByte, int paramInt2) throws IOException {
    if (paramInt1 != 4)
      throw Http2.ioException("TYPE_WINDOW_UPDATE length !=4: %s", new Object[] { Integer.valueOf(paramInt1) }); 
    long l = this.source.readInt() & 0x7FFFFFFFL;
    if (l == 0L)
      throw Http2.ioException("windowSizeIncrement was 0", new Object[] { Long.valueOf(l) }); 
    paramHandler.windowUpdate(paramInt2, l);
  }
  
  public void close() throws IOException {
    this.source.close();
  }
  
  public boolean nextFrame(boolean paramBoolean, Handler paramHandler) throws IOException {
    int i;
    boolean bool = true;
    try {
      this.source.require(9L);
      i = readMedium(this.source);
      if (i < 0 || i > 16384)
        throw Http2.ioException("FRAME_SIZE_ERROR: %s", new Object[] { Integer.valueOf(i) }); 
    } catch (IOException iOException) {
      return false;
    } 
    byte b1 = (byte)(this.source.readByte() & 0xFF);
    if (paramBoolean && b1 != 4)
      throw Http2.ioException("Expected a SETTINGS frame but was %s", new Object[] { Byte.valueOf(b1) }); 
    byte b2 = (byte)(this.source.readByte() & 0xFF);
    int j = this.source.readInt() & Integer.MAX_VALUE;
    if (logger.isLoggable(Level.FINE))
      logger.fine(Http2.frameLog(true, j, i, b1, b2)); 
    switch (b1) {
      default:
        this.source.skip(i);
        return bool;
      case 0:
        readData((Handler)iOException, i, b2, j);
        return bool;
      case 1:
        readHeaders((Handler)iOException, i, b2, j);
        return bool;
      case 2:
        readPriority((Handler)iOException, i, b2, j);
        return bool;
      case 3:
        readRstStream((Handler)iOException, i, b2, j);
        return bool;
      case 4:
        readSettings((Handler)iOException, i, b2, j);
        return bool;
      case 5:
        readPushPromise((Handler)iOException, i, b2, j);
        return bool;
      case 6:
        readPing((Handler)iOException, i, b2, j);
        return bool;
      case 7:
        readGoAway((Handler)iOException, i, b2, j);
        return bool;
      case 8:
        break;
    } 
    readWindowUpdate((Handler)iOException, i, b2, j);
    return bool;
  }
  
  public void readConnectionPreface(Handler paramHandler) throws IOException {
    if (this.client) {
      if (!nextFrame(true, paramHandler))
        throw Http2.ioException("Required SETTINGS preface not received", new Object[0]); 
    } else {
      ByteString byteString = this.source.readByteString(Http2.CONNECTION_PREFACE.size());
      if (logger.isLoggable(Level.FINE))
        logger.fine(Util.format("<< CONNECTION %s", new Object[] { byteString.hex() })); 
      if (!Http2.CONNECTION_PREFACE.equals(byteString))
        throw Http2.ioException("Expected a connection header but was %s", new Object[] { byteString.utf8() }); 
    } 
  }
  
  static final class ContinuationSource implements Source {
    byte flags;
    
    int left;
    
    int length;
    
    short padding;
    
    private final BufferedSource source;
    
    int streamId;
    
    ContinuationSource(BufferedSource param1BufferedSource) {
      this.source = param1BufferedSource;
    }
    
    private void readContinuationHeader() throws IOException {
      int i = this.streamId;
      int j = Http2Reader.readMedium(this.source);
      this.left = j;
      this.length = j;
      byte b = (byte)(this.source.readByte() & 0xFF);
      this.flags = (byte)(byte)(this.source.readByte() & 0xFF);
      if (Http2Reader.logger.isLoggable(Level.FINE))
        Http2Reader.logger.fine(Http2.frameLog(true, this.streamId, this.length, b, this.flags)); 
      this.streamId = this.source.readInt() & Integer.MAX_VALUE;
      if (b != 9)
        throw Http2.ioException("%s != TYPE_CONTINUATION", new Object[] { Byte.valueOf(b) }); 
      if (this.streamId != i)
        throw Http2.ioException("TYPE_CONTINUATION streamId changed", new Object[0]); 
    }
    
    public void close() throws IOException {}
    
    public long read(Buffer param1Buffer, long param1Long) throws IOException {
      while (this.left == 0) {
        this.source.skip(this.padding);
        this.padding = (short)0;
        if ((this.flags & 0x4) != 0)
          return -1L; 
        readContinuationHeader();
      } 
      param1Long = this.source.read(param1Buffer, Math.min(param1Long, this.left));
      if (param1Long == -1L)
        return -1L; 
      this.left = (int)(this.left - param1Long);
      return param1Long;
    }
    
    public Timeout timeout() {
      return this.source.timeout();
    }
  }
  
  static interface Handler {
    void ackSettings();
    
    void alternateService(int param1Int1, String param1String1, ByteString param1ByteString, String param1String2, int param1Int2, long param1Long);
    
    void data(boolean param1Boolean, int param1Int1, BufferedSource param1BufferedSource, int param1Int2) throws IOException;
    
    void goAway(int param1Int, ErrorCode param1ErrorCode, ByteString param1ByteString);
    
    void headers(boolean param1Boolean, int param1Int1, int param1Int2, List<Header> param1List);
    
    void ping(boolean param1Boolean, int param1Int1, int param1Int2);
    
    void priority(int param1Int1, int param1Int2, int param1Int3, boolean param1Boolean);
    
    void pushPromise(int param1Int1, int param1Int2, List<Header> param1List) throws IOException;
    
    void rstStream(int param1Int, ErrorCode param1ErrorCode);
    
    void settings(boolean param1Boolean, Settings param1Settings);
    
    void windowUpdate(int param1Int, long param1Long);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/http2/Http2Reader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */