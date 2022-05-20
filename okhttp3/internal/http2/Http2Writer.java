package okhttp3.internal.http2;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import okio.BufferedSink;

final class Http2Writer implements Closeable {
  private static final Logger logger = Logger.getLogger(Http2.class.getName());
  
  private final boolean client;
  
  private boolean closed;
  
  private final Buffer hpackBuffer;
  
  final Hpack.Writer hpackWriter;
  
  private int maxFrameSize;
  
  private final BufferedSink sink;
  
  Http2Writer(BufferedSink paramBufferedSink, boolean paramBoolean) {
    this.sink = paramBufferedSink;
    this.client = paramBoolean;
    this.hpackBuffer = new Buffer();
    this.hpackWriter = new Hpack.Writer(this.hpackBuffer);
    this.maxFrameSize = 16384;
  }
  
  private void writeContinuationFrames(int paramInt, long paramLong) throws IOException {
    while (paramLong > 0L) {
      boolean bool;
      int i = (int)Math.min(this.maxFrameSize, paramLong);
      paramLong -= i;
      if (paramLong == 0L) {
        byte b = 4;
        bool = b;
      } else {
        boolean bool1 = false;
        bool = bool1;
      } 
      frameHeader(paramInt, i, (byte)9, bool);
      this.sink.write(this.hpackBuffer, i);
    } 
  }
  
  private static void writeMedium(BufferedSink paramBufferedSink, int paramInt) throws IOException {
    paramBufferedSink.writeByte(paramInt >>> 16 & 0xFF);
    paramBufferedSink.writeByte(paramInt >>> 8 & 0xFF);
    paramBufferedSink.writeByte(paramInt & 0xFF);
  }
  
  public void applyAndAckSettings(Settings paramSettings) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifeq -> 26
    //   9: new java/io/IOException
    //   12: astore_1
    //   13: aload_1
    //   14: ldc 'closed'
    //   16: invokespecial <init> : (Ljava/lang/String;)V
    //   19: aload_1
    //   20: athrow
    //   21: astore_1
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_1
    //   25: athrow
    //   26: aload_0
    //   27: aload_1
    //   28: aload_0
    //   29: getfield maxFrameSize : I
    //   32: invokevirtual getMaxFrameSize : (I)I
    //   35: putfield maxFrameSize : I
    //   38: aload_1
    //   39: invokevirtual getHeaderTableSize : ()I
    //   42: iconst_m1
    //   43: if_icmpeq -> 57
    //   46: aload_0
    //   47: getfield hpackWriter : Lokhttp3/internal/http2/Hpack$Writer;
    //   50: aload_1
    //   51: invokevirtual getHeaderTableSize : ()I
    //   54: invokevirtual setHeaderTableSizeSetting : (I)V
    //   57: aload_0
    //   58: iconst_0
    //   59: iconst_0
    //   60: iconst_4
    //   61: iconst_1
    //   62: invokevirtual frameHeader : (IIBB)V
    //   65: aload_0
    //   66: getfield sink : Lokio/BufferedSink;
    //   69: invokeinterface flush : ()V
    //   74: aload_0
    //   75: monitorexit
    //   76: return
    // Exception table:
    //   from	to	target	type
    //   2	21	21	finally
    //   26	57	21	finally
    //   57	74	21	finally
  }
  
  public void close() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_1
    //   4: putfield closed : Z
    //   7: aload_0
    //   8: getfield sink : Lokio/BufferedSink;
    //   11: invokeinterface close : ()V
    //   16: aload_0
    //   17: monitorexit
    //   18: return
    //   19: astore_1
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_1
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	19	finally
  }
  
  public void connectionPreface() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifeq -> 26
    //   9: new java/io/IOException
    //   12: astore_1
    //   13: aload_1
    //   14: ldc 'closed'
    //   16: invokespecial <init> : (Ljava/lang/String;)V
    //   19: aload_1
    //   20: athrow
    //   21: astore_1
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_1
    //   25: athrow
    //   26: aload_0
    //   27: getfield client : Z
    //   30: istore_2
    //   31: iload_2
    //   32: ifne -> 38
    //   35: aload_0
    //   36: monitorexit
    //   37: return
    //   38: getstatic okhttp3/internal/http2/Http2Writer.logger : Ljava/util/logging/Logger;
    //   41: getstatic java/util/logging/Level.FINE : Ljava/util/logging/Level;
    //   44: invokevirtual isLoggable : (Ljava/util/logging/Level;)Z
    //   47: ifeq -> 74
    //   50: getstatic okhttp3/internal/http2/Http2Writer.logger : Ljava/util/logging/Logger;
    //   53: ldc '>> CONNECTION %s'
    //   55: iconst_1
    //   56: anewarray java/lang/Object
    //   59: dup
    //   60: iconst_0
    //   61: getstatic okhttp3/internal/http2/Http2.CONNECTION_PREFACE : Lokio/ByteString;
    //   64: invokevirtual hex : ()Ljava/lang/String;
    //   67: aastore
    //   68: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   71: invokevirtual fine : (Ljava/lang/String;)V
    //   74: aload_0
    //   75: getfield sink : Lokio/BufferedSink;
    //   78: getstatic okhttp3/internal/http2/Http2.CONNECTION_PREFACE : Lokio/ByteString;
    //   81: invokevirtual toByteArray : ()[B
    //   84: invokeinterface write : ([B)Lokio/BufferedSink;
    //   89: pop
    //   90: aload_0
    //   91: getfield sink : Lokio/BufferedSink;
    //   94: invokeinterface flush : ()V
    //   99: goto -> 35
    // Exception table:
    //   from	to	target	type
    //   2	21	21	finally
    //   26	31	21	finally
    //   38	74	21	finally
    //   74	99	21	finally
  }
  
  public void data(boolean paramBoolean, int paramInt1, Buffer paramBuffer, int paramInt2) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifeq -> 26
    //   9: new java/io/IOException
    //   12: astore_3
    //   13: aload_3
    //   14: ldc 'closed'
    //   16: invokespecial <init> : (Ljava/lang/String;)V
    //   19: aload_3
    //   20: athrow
    //   21: astore_3
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_3
    //   25: athrow
    //   26: iconst_0
    //   27: istore #5
    //   29: iload #5
    //   31: istore #6
    //   33: iload_1
    //   34: ifeq -> 45
    //   37: iconst_1
    //   38: i2b
    //   39: istore #5
    //   41: iload #5
    //   43: istore #6
    //   45: aload_0
    //   46: iload_2
    //   47: iload #6
    //   49: aload_3
    //   50: iload #4
    //   52: invokevirtual dataFrame : (IBLokio/Buffer;I)V
    //   55: aload_0
    //   56: monitorexit
    //   57: return
    // Exception table:
    //   from	to	target	type
    //   2	21	21	finally
    //   45	55	21	finally
  }
  
  void dataFrame(int paramInt1, byte paramByte, Buffer paramBuffer, int paramInt2) throws IOException {
    frameHeader(paramInt1, paramInt2, (byte)0, paramByte);
    if (paramInt2 > 0)
      this.sink.write(paramBuffer, paramInt2); 
  }
  
  public void flush() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifeq -> 26
    //   9: new java/io/IOException
    //   12: astore_1
    //   13: aload_1
    //   14: ldc 'closed'
    //   16: invokespecial <init> : (Ljava/lang/String;)V
    //   19: aload_1
    //   20: athrow
    //   21: astore_1
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_1
    //   25: athrow
    //   26: aload_0
    //   27: getfield sink : Lokio/BufferedSink;
    //   30: invokeinterface flush : ()V
    //   35: aload_0
    //   36: monitorexit
    //   37: return
    // Exception table:
    //   from	to	target	type
    //   2	21	21	finally
    //   26	35	21	finally
  }
  
  public void frameHeader(int paramInt1, int paramInt2, byte paramByte1, byte paramByte2) throws IOException {
    if (logger.isLoggable(Level.FINE))
      logger.fine(Http2.frameLog(false, paramInt1, paramInt2, paramByte1, paramByte2)); 
    if (paramInt2 > this.maxFrameSize)
      throw Http2.illegalArgument("FRAME_SIZE_ERROR length > %d: %d", new Object[] { Integer.valueOf(this.maxFrameSize), Integer.valueOf(paramInt2) }); 
    if ((Integer.MIN_VALUE & paramInt1) != 0)
      throw Http2.illegalArgument("reserved bit set: %s", new Object[] { Integer.valueOf(paramInt1) }); 
    writeMedium(this.sink, paramInt2);
    this.sink.writeByte(paramByte1 & 0xFF);
    this.sink.writeByte(paramByte2 & 0xFF);
    this.sink.writeInt(Integer.MAX_VALUE & paramInt1);
  }
  
  public void goAway(int paramInt, ErrorCode paramErrorCode, byte[] paramArrayOfbyte) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifeq -> 26
    //   9: new java/io/IOException
    //   12: astore_2
    //   13: aload_2
    //   14: ldc 'closed'
    //   16: invokespecial <init> : (Ljava/lang/String;)V
    //   19: aload_2
    //   20: athrow
    //   21: astore_2
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_2
    //   25: athrow
    //   26: aload_2
    //   27: getfield httpCode : I
    //   30: iconst_m1
    //   31: if_icmpne -> 44
    //   34: ldc 'errorCode.httpCode == -1'
    //   36: iconst_0
    //   37: anewarray java/lang/Object
    //   40: invokestatic illegalArgument : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/IllegalArgumentException;
    //   43: athrow
    //   44: aload_0
    //   45: iconst_0
    //   46: aload_3
    //   47: arraylength
    //   48: bipush #8
    //   50: iadd
    //   51: bipush #7
    //   53: iconst_0
    //   54: invokevirtual frameHeader : (IIBB)V
    //   57: aload_0
    //   58: getfield sink : Lokio/BufferedSink;
    //   61: iload_1
    //   62: invokeinterface writeInt : (I)Lokio/BufferedSink;
    //   67: pop
    //   68: aload_0
    //   69: getfield sink : Lokio/BufferedSink;
    //   72: aload_2
    //   73: getfield httpCode : I
    //   76: invokeinterface writeInt : (I)Lokio/BufferedSink;
    //   81: pop
    //   82: aload_3
    //   83: arraylength
    //   84: ifle -> 98
    //   87: aload_0
    //   88: getfield sink : Lokio/BufferedSink;
    //   91: aload_3
    //   92: invokeinterface write : ([B)Lokio/BufferedSink;
    //   97: pop
    //   98: aload_0
    //   99: getfield sink : Lokio/BufferedSink;
    //   102: invokeinterface flush : ()V
    //   107: aload_0
    //   108: monitorexit
    //   109: return
    // Exception table:
    //   from	to	target	type
    //   2	21	21	finally
    //   26	44	21	finally
    //   44	98	21	finally
    //   98	107	21	finally
  }
  
  public void headers(int paramInt, List<Header> paramList) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifeq -> 26
    //   9: new java/io/IOException
    //   12: astore_2
    //   13: aload_2
    //   14: ldc 'closed'
    //   16: invokespecial <init> : (Ljava/lang/String;)V
    //   19: aload_2
    //   20: athrow
    //   21: astore_2
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_2
    //   25: athrow
    //   26: aload_0
    //   27: iconst_0
    //   28: iload_1
    //   29: aload_2
    //   30: invokevirtual headers : (ZILjava/util/List;)V
    //   33: aload_0
    //   34: monitorexit
    //   35: return
    // Exception table:
    //   from	to	target	type
    //   2	21	21	finally
    //   26	33	21	finally
  }
  
  void headers(boolean paramBoolean, int paramInt, List<Header> paramList) throws IOException {
    byte b1;
    if (this.closed)
      throw new IOException("closed"); 
    this.hpackWriter.writeHeaders(paramList);
    long l = this.hpackBuffer.size();
    int i = (int)Math.min(this.maxFrameSize, l);
    if (l == i) {
      b1 = 4;
    } else {
      b1 = 0;
    } 
    byte b2 = b1;
    if (paramBoolean) {
      b1 = (byte)(b1 | 0x1);
      b2 = b1;
    } 
    frameHeader(paramInt, i, (byte)1, b2);
    this.sink.write(this.hpackBuffer, i);
    if (l > i)
      writeContinuationFrames(paramInt, l - i); 
  }
  
  public int maxDataLength() {
    return this.maxFrameSize;
  }
  
  public void ping(boolean paramBoolean, int paramInt1, int paramInt2) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifeq -> 31
    //   9: new java/io/IOException
    //   12: astore #4
    //   14: aload #4
    //   16: ldc 'closed'
    //   18: invokespecial <init> : (Ljava/lang/String;)V
    //   21: aload #4
    //   23: athrow
    //   24: astore #4
    //   26: aload_0
    //   27: monitorexit
    //   28: aload #4
    //   30: athrow
    //   31: iload_1
    //   32: ifeq -> 87
    //   35: iconst_1
    //   36: istore #5
    //   38: iload #5
    //   40: istore #6
    //   42: aload_0
    //   43: iconst_0
    //   44: bipush #8
    //   46: bipush #6
    //   48: iload #6
    //   50: invokevirtual frameHeader : (IIBB)V
    //   53: aload_0
    //   54: getfield sink : Lokio/BufferedSink;
    //   57: iload_2
    //   58: invokeinterface writeInt : (I)Lokio/BufferedSink;
    //   63: pop
    //   64: aload_0
    //   65: getfield sink : Lokio/BufferedSink;
    //   68: iload_3
    //   69: invokeinterface writeInt : (I)Lokio/BufferedSink;
    //   74: pop
    //   75: aload_0
    //   76: getfield sink : Lokio/BufferedSink;
    //   79: invokeinterface flush : ()V
    //   84: aload_0
    //   85: monitorexit
    //   86: return
    //   87: iconst_0
    //   88: istore #5
    //   90: iload #5
    //   92: istore #6
    //   94: goto -> 42
    // Exception table:
    //   from	to	target	type
    //   2	24	24	finally
    //   42	84	24	finally
  }
  
  public void pushPromise(int paramInt1, int paramInt2, List<Header> paramList) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifeq -> 26
    //   9: new java/io/IOException
    //   12: astore_3
    //   13: aload_3
    //   14: ldc 'closed'
    //   16: invokespecial <init> : (Ljava/lang/String;)V
    //   19: aload_3
    //   20: athrow
    //   21: astore_3
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_3
    //   25: athrow
    //   26: aload_0
    //   27: getfield hpackWriter : Lokhttp3/internal/http2/Hpack$Writer;
    //   30: aload_3
    //   31: invokevirtual writeHeaders : (Ljava/util/List;)V
    //   34: aload_0
    //   35: getfield hpackBuffer : Lokio/Buffer;
    //   38: invokevirtual size : ()J
    //   41: lstore #4
    //   43: aload_0
    //   44: getfield maxFrameSize : I
    //   47: iconst_4
    //   48: isub
    //   49: i2l
    //   50: lload #4
    //   52: invokestatic min : (JJ)J
    //   55: l2i
    //   56: istore #6
    //   58: lload #4
    //   60: iload #6
    //   62: i2l
    //   63: lcmp
    //   64: ifne -> 139
    //   67: iconst_4
    //   68: istore #7
    //   70: iload #7
    //   72: istore #8
    //   74: aload_0
    //   75: iload_1
    //   76: iload #6
    //   78: iconst_4
    //   79: iadd
    //   80: iconst_5
    //   81: iload #8
    //   83: invokevirtual frameHeader : (IIBB)V
    //   86: aload_0
    //   87: getfield sink : Lokio/BufferedSink;
    //   90: ldc 2147483647
    //   92: iload_2
    //   93: iand
    //   94: invokeinterface writeInt : (I)Lokio/BufferedSink;
    //   99: pop
    //   100: aload_0
    //   101: getfield sink : Lokio/BufferedSink;
    //   104: aload_0
    //   105: getfield hpackBuffer : Lokio/Buffer;
    //   108: iload #6
    //   110: i2l
    //   111: invokeinterface write : (Lokio/Buffer;J)V
    //   116: lload #4
    //   118: iload #6
    //   120: i2l
    //   121: lcmp
    //   122: ifle -> 136
    //   125: aload_0
    //   126: iload_1
    //   127: lload #4
    //   129: iload #6
    //   131: i2l
    //   132: lsub
    //   133: invokespecial writeContinuationFrames : (IJ)V
    //   136: aload_0
    //   137: monitorexit
    //   138: return
    //   139: iconst_0
    //   140: istore #7
    //   142: iload #7
    //   144: istore #8
    //   146: goto -> 74
    // Exception table:
    //   from	to	target	type
    //   2	21	21	finally
    //   26	58	21	finally
    //   74	116	21	finally
    //   125	136	21	finally
  }
  
  public void rstStream(int paramInt, ErrorCode paramErrorCode) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifeq -> 26
    //   9: new java/io/IOException
    //   12: astore_2
    //   13: aload_2
    //   14: ldc 'closed'
    //   16: invokespecial <init> : (Ljava/lang/String;)V
    //   19: aload_2
    //   20: athrow
    //   21: astore_2
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_2
    //   25: athrow
    //   26: aload_2
    //   27: getfield httpCode : I
    //   30: iconst_m1
    //   31: if_icmpne -> 44
    //   34: new java/lang/IllegalArgumentException
    //   37: astore_2
    //   38: aload_2
    //   39: invokespecial <init> : ()V
    //   42: aload_2
    //   43: athrow
    //   44: aload_0
    //   45: iload_1
    //   46: iconst_4
    //   47: iconst_3
    //   48: iconst_0
    //   49: invokevirtual frameHeader : (IIBB)V
    //   52: aload_0
    //   53: getfield sink : Lokio/BufferedSink;
    //   56: aload_2
    //   57: getfield httpCode : I
    //   60: invokeinterface writeInt : (I)Lokio/BufferedSink;
    //   65: pop
    //   66: aload_0
    //   67: getfield sink : Lokio/BufferedSink;
    //   70: invokeinterface flush : ()V
    //   75: aload_0
    //   76: monitorexit
    //   77: return
    // Exception table:
    //   from	to	target	type
    //   2	21	21	finally
    //   26	44	21	finally
    //   44	75	21	finally
  }
  
  public void settings(Settings paramSettings) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifeq -> 26
    //   9: new java/io/IOException
    //   12: astore_1
    //   13: aload_1
    //   14: ldc 'closed'
    //   16: invokespecial <init> : (Ljava/lang/String;)V
    //   19: aload_1
    //   20: athrow
    //   21: astore_1
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_1
    //   25: athrow
    //   26: aload_0
    //   27: iconst_0
    //   28: aload_1
    //   29: invokevirtual size : ()I
    //   32: bipush #6
    //   34: imul
    //   35: iconst_4
    //   36: iconst_0
    //   37: invokevirtual frameHeader : (IIBB)V
    //   40: iconst_0
    //   41: istore_2
    //   42: iload_2
    //   43: bipush #10
    //   45: if_icmpge -> 117
    //   48: aload_1
    //   49: iload_2
    //   50: invokevirtual isSet : (I)Z
    //   53: ifne -> 62
    //   56: iinc #2, 1
    //   59: goto -> 42
    //   62: iload_2
    //   63: istore_3
    //   64: iload_3
    //   65: iconst_4
    //   66: if_icmpne -> 102
    //   69: iconst_3
    //   70: istore #4
    //   72: aload_0
    //   73: getfield sink : Lokio/BufferedSink;
    //   76: iload #4
    //   78: invokeinterface writeShort : (I)Lokio/BufferedSink;
    //   83: pop
    //   84: aload_0
    //   85: getfield sink : Lokio/BufferedSink;
    //   88: aload_1
    //   89: iload_2
    //   90: invokevirtual get : (I)I
    //   93: invokeinterface writeInt : (I)Lokio/BufferedSink;
    //   98: pop
    //   99: goto -> 56
    //   102: iload_3
    //   103: istore #4
    //   105: iload_3
    //   106: bipush #7
    //   108: if_icmpne -> 72
    //   111: iconst_4
    //   112: istore #4
    //   114: goto -> 72
    //   117: aload_0
    //   118: getfield sink : Lokio/BufferedSink;
    //   121: invokeinterface flush : ()V
    //   126: aload_0
    //   127: monitorexit
    //   128: return
    // Exception table:
    //   from	to	target	type
    //   2	21	21	finally
    //   26	40	21	finally
    //   48	56	21	finally
    //   72	99	21	finally
    //   117	126	21	finally
  }
  
  public void synReply(boolean paramBoolean, int paramInt, List<Header> paramList) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifeq -> 26
    //   9: new java/io/IOException
    //   12: astore_3
    //   13: aload_3
    //   14: ldc 'closed'
    //   16: invokespecial <init> : (Ljava/lang/String;)V
    //   19: aload_3
    //   20: athrow
    //   21: astore_3
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_3
    //   25: athrow
    //   26: aload_0
    //   27: iload_1
    //   28: iload_2
    //   29: aload_3
    //   30: invokevirtual headers : (ZILjava/util/List;)V
    //   33: aload_0
    //   34: monitorexit
    //   35: return
    // Exception table:
    //   from	to	target	type
    //   2	21	21	finally
    //   26	33	21	finally
  }
  
  public void synStream(boolean paramBoolean, int paramInt1, int paramInt2, List<Header> paramList) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifeq -> 31
    //   9: new java/io/IOException
    //   12: astore #4
    //   14: aload #4
    //   16: ldc 'closed'
    //   18: invokespecial <init> : (Ljava/lang/String;)V
    //   21: aload #4
    //   23: athrow
    //   24: astore #4
    //   26: aload_0
    //   27: monitorexit
    //   28: aload #4
    //   30: athrow
    //   31: aload_0
    //   32: iload_1
    //   33: iload_2
    //   34: aload #4
    //   36: invokevirtual headers : (ZILjava/util/List;)V
    //   39: aload_0
    //   40: monitorexit
    //   41: return
    // Exception table:
    //   from	to	target	type
    //   2	24	24	finally
    //   31	39	24	finally
  }
  
  public void windowUpdate(int paramInt, long paramLong) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield closed : Z
    //   6: ifeq -> 31
    //   9: new java/io/IOException
    //   12: astore #4
    //   14: aload #4
    //   16: ldc 'closed'
    //   18: invokespecial <init> : (Ljava/lang/String;)V
    //   21: aload #4
    //   23: athrow
    //   24: astore #4
    //   26: aload_0
    //   27: monitorexit
    //   28: aload #4
    //   30: athrow
    //   31: lload_2
    //   32: lconst_0
    //   33: lcmp
    //   34: ifeq -> 45
    //   37: lload_2
    //   38: ldc2_w 2147483647
    //   41: lcmp
    //   42: ifle -> 62
    //   45: ldc 'windowSizeIncrement == 0 || windowSizeIncrement > 0x7fffffffL: %s'
    //   47: iconst_1
    //   48: anewarray java/lang/Object
    //   51: dup
    //   52: iconst_0
    //   53: lload_2
    //   54: invokestatic valueOf : (J)Ljava/lang/Long;
    //   57: aastore
    //   58: invokestatic illegalArgument : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/IllegalArgumentException;
    //   61: athrow
    //   62: aload_0
    //   63: iload_1
    //   64: iconst_4
    //   65: bipush #8
    //   67: iconst_0
    //   68: invokevirtual frameHeader : (IIBB)V
    //   71: aload_0
    //   72: getfield sink : Lokio/BufferedSink;
    //   75: lload_2
    //   76: l2i
    //   77: invokeinterface writeInt : (I)Lokio/BufferedSink;
    //   82: pop
    //   83: aload_0
    //   84: getfield sink : Lokio/BufferedSink;
    //   87: invokeinterface flush : ()V
    //   92: aload_0
    //   93: monitorexit
    //   94: return
    // Exception table:
    //   from	to	target	type
    //   2	24	24	finally
    //   45	62	24	finally
    //   62	92	24	finally
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/http2/Http2Writer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */