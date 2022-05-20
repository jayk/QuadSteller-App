package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import javax.annotation.Nullable;

final class RealBufferedSource implements BufferedSource {
  public final Buffer buffer = new Buffer();
  
  boolean closed;
  
  public final Source source;
  
  RealBufferedSource(Source paramSource) {
    if (paramSource == null)
      throw new NullPointerException("source == null"); 
    this.source = paramSource;
  }
  
  public Buffer buffer() {
    return this.buffer;
  }
  
  public void close() throws IOException {
    if (!this.closed) {
      this.closed = true;
      this.source.close();
      this.buffer.clear();
    } 
  }
  
  public boolean exhausted() throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    return (this.buffer.exhausted() && this.source.read(this.buffer, 8192L) == -1L);
  }
  
  public long indexOf(byte paramByte) throws IOException {
    return indexOf(paramByte, 0L, Long.MAX_VALUE);
  }
  
  public long indexOf(byte paramByte, long paramLong) throws IOException {
    return indexOf(paramByte, paramLong, Long.MAX_VALUE);
  }
  
  public long indexOf(byte paramByte, long paramLong1, long paramLong2) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield closed : Z
    //   4: ifeq -> 17
    //   7: new java/lang/IllegalStateException
    //   10: dup
    //   11: ldc 'closed'
    //   13: invokespecial <init> : (Ljava/lang/String;)V
    //   16: athrow
    //   17: lload_2
    //   18: lconst_0
    //   19: lcmp
    //   20: iflt -> 33
    //   23: lload_2
    //   24: lstore #6
    //   26: lload #4
    //   28: lload_2
    //   29: lcmp
    //   30: ifge -> 73
    //   33: new java/lang/IllegalArgumentException
    //   36: dup
    //   37: ldc 'fromIndex=%s toIndex=%s'
    //   39: iconst_2
    //   40: anewarray java/lang/Object
    //   43: dup
    //   44: iconst_0
    //   45: lload_2
    //   46: invokestatic valueOf : (J)Ljava/lang/Long;
    //   49: aastore
    //   50: dup
    //   51: iconst_1
    //   52: lload #4
    //   54: invokestatic valueOf : (J)Ljava/lang/Long;
    //   57: aastore
    //   58: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   61: invokespecial <init> : (Ljava/lang/String;)V
    //   64: athrow
    //   65: lload #6
    //   67: lload_2
    //   68: invokestatic max : (JJ)J
    //   71: lstore #6
    //   73: lload #6
    //   75: lload #4
    //   77: lcmp
    //   78: ifge -> 149
    //   81: aload_0
    //   82: getfield buffer : Lokio/Buffer;
    //   85: iload_1
    //   86: lload #6
    //   88: lload #4
    //   90: invokevirtual indexOf : (BJJ)J
    //   93: lstore_2
    //   94: lload_2
    //   95: ldc2_w -1
    //   98: lcmp
    //   99: ifeq -> 104
    //   102: lload_2
    //   103: lreturn
    //   104: aload_0
    //   105: getfield buffer : Lokio/Buffer;
    //   108: getfield size : J
    //   111: lstore_2
    //   112: lload_2
    //   113: lload #4
    //   115: lcmp
    //   116: ifge -> 142
    //   119: aload_0
    //   120: getfield source : Lokio/Source;
    //   123: aload_0
    //   124: getfield buffer : Lokio/Buffer;
    //   127: ldc2_w 8192
    //   130: invokeinterface read : (Lokio/Buffer;J)J
    //   135: ldc2_w -1
    //   138: lcmp
    //   139: ifne -> 65
    //   142: ldc2_w -1
    //   145: lstore_2
    //   146: goto -> 102
    //   149: ldc2_w -1
    //   152: lstore_2
    //   153: goto -> 102
  }
  
  public long indexOf(ByteString paramByteString) throws IOException {
    return indexOf(paramByteString, 0L);
  }
  
  public long indexOf(ByteString paramByteString, long paramLong) throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    while (true) {
      long l = this.buffer.indexOf(paramByteString, paramLong);
      if (l != -1L)
        return l; 
      l = this.buffer.size;
      if (this.source.read(this.buffer, 8192L) == -1L)
        return -1L; 
      paramLong = Math.max(paramLong, l - paramByteString.size() + 1L);
    } 
  }
  
  public long indexOfElement(ByteString paramByteString) throws IOException {
    return indexOfElement(paramByteString, 0L);
  }
  
  public long indexOfElement(ByteString paramByteString, long paramLong) throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    while (true) {
      long l = this.buffer.indexOfElement(paramByteString, paramLong);
      if (l != -1L)
        return l; 
      l = this.buffer.size;
      if (this.source.read(this.buffer, 8192L) == -1L)
        return -1L; 
      paramLong = Math.max(paramLong, l);
    } 
  }
  
  public InputStream inputStream() {
    return new InputStream() {
        public int available() throws IOException {
          if (RealBufferedSource.this.closed)
            throw new IOException("closed"); 
          return (int)Math.min(RealBufferedSource.this.buffer.size, 2147483647L);
        }
        
        public void close() throws IOException {
          RealBufferedSource.this.close();
        }
        
        public int read() throws IOException {
          if (RealBufferedSource.this.closed)
            throw new IOException("closed"); 
          return (RealBufferedSource.this.buffer.size == 0L && RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, 8192L) == -1L) ? -1 : (RealBufferedSource.this.buffer.readByte() & 0xFF);
        }
        
        public int read(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
          if (RealBufferedSource.this.closed)
            throw new IOException("closed"); 
          Util.checkOffsetAndCount(param1ArrayOfbyte.length, param1Int1, param1Int2);
          return (RealBufferedSource.this.buffer.size == 0L && RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, 8192L) == -1L) ? -1 : RealBufferedSource.this.buffer.read(param1ArrayOfbyte, param1Int1, param1Int2);
        }
        
        public String toString() {
          return RealBufferedSource.this + ".inputStream()";
        }
      };
  }
  
  public boolean rangeEquals(long paramLong, ByteString paramByteString) throws IOException {
    return rangeEquals(paramLong, paramByteString, 0, paramByteString.size());
  }
  
  public boolean rangeEquals(long paramLong, ByteString paramByteString, int paramInt1, int paramInt2) throws IOException {
    // Byte code:
    //   0: iconst_0
    //   1: istore #6
    //   3: aload_0
    //   4: getfield closed : Z
    //   7: ifeq -> 20
    //   10: new java/lang/IllegalStateException
    //   13: dup
    //   14: ldc 'closed'
    //   16: invokespecial <init> : (Ljava/lang/String;)V
    //   19: athrow
    //   20: iload #6
    //   22: istore #7
    //   24: lload_1
    //   25: lconst_0
    //   26: lcmp
    //   27: iflt -> 64
    //   30: iload #6
    //   32: istore #7
    //   34: iload #4
    //   36: iflt -> 64
    //   39: iload #6
    //   41: istore #7
    //   43: iload #5
    //   45: iflt -> 64
    //   48: aload_3
    //   49: invokevirtual size : ()I
    //   52: iload #4
    //   54: isub
    //   55: iload #5
    //   57: if_icmpge -> 67
    //   60: iload #6
    //   62: istore #7
    //   64: iload #7
    //   66: ireturn
    //   67: iconst_0
    //   68: istore #8
    //   70: iload #8
    //   72: iload #5
    //   74: if_icmpge -> 130
    //   77: lload_1
    //   78: iload #8
    //   80: i2l
    //   81: ladd
    //   82: lstore #9
    //   84: iload #6
    //   86: istore #7
    //   88: aload_0
    //   89: lconst_1
    //   90: lload #9
    //   92: ladd
    //   93: invokevirtual request : (J)Z
    //   96: ifeq -> 64
    //   99: iload #6
    //   101: istore #7
    //   103: aload_0
    //   104: getfield buffer : Lokio/Buffer;
    //   107: lload #9
    //   109: invokevirtual getByte : (J)B
    //   112: aload_3
    //   113: iload #4
    //   115: iload #8
    //   117: iadd
    //   118: invokevirtual getByte : (I)B
    //   121: if_icmpne -> 64
    //   124: iinc #8, 1
    //   127: goto -> 70
    //   130: iconst_1
    //   131: istore #7
    //   133: goto -> 64
  }
  
  public int read(byte[] paramArrayOfbyte) throws IOException {
    return read(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    Util.checkOffsetAndCount(paramArrayOfbyte.length, paramInt1, paramInt2);
    if (this.buffer.size == 0L && this.source.read(this.buffer, 8192L) == -1L)
      return -1; 
    paramInt2 = (int)Math.min(paramInt2, this.buffer.size);
    return this.buffer.read(paramArrayOfbyte, paramInt1, paramInt2);
  }
  
  public long read(Buffer paramBuffer, long paramLong) throws IOException {
    long l = -1L;
    if (paramBuffer == null)
      throw new IllegalArgumentException("sink == null"); 
    if (paramLong < 0L)
      throw new IllegalArgumentException("byteCount < 0: " + paramLong); 
    if (this.closed)
      throw new IllegalStateException("closed"); 
    if (this.buffer.size == 0L && this.source.read(this.buffer, 8192L) == -1L)
      return l; 
    paramLong = Math.min(paramLong, this.buffer.size);
    return this.buffer.read(paramBuffer, paramLong);
  }
  
  public long readAll(Sink paramSink) throws IOException {
    if (paramSink == null)
      throw new IllegalArgumentException("sink == null"); 
    long l1 = 0L;
    while (this.source.read(this.buffer, 8192L) != -1L) {
      long l = this.buffer.completeSegmentByteCount();
      if (l > 0L) {
        l1 += l;
        paramSink.write(this.buffer, l);
      } 
    } 
    long l2 = l1;
    if (this.buffer.size() > 0L) {
      l2 = l1 + this.buffer.size();
      paramSink.write(this.buffer, this.buffer.size());
    } 
    return l2;
  }
  
  public byte readByte() throws IOException {
    require(1L);
    return this.buffer.readByte();
  }
  
  public byte[] readByteArray() throws IOException {
    this.buffer.writeAll(this.source);
    return this.buffer.readByteArray();
  }
  
  public byte[] readByteArray(long paramLong) throws IOException {
    require(paramLong);
    return this.buffer.readByteArray(paramLong);
  }
  
  public ByteString readByteString() throws IOException {
    this.buffer.writeAll(this.source);
    return this.buffer.readByteString();
  }
  
  public ByteString readByteString(long paramLong) throws IOException {
    require(paramLong);
    return this.buffer.readByteString(paramLong);
  }
  
  public long readDecimalLong() throws IOException {
    require(1L);
    for (byte b = 0; request((b + 1)); b++) {
      byte b1 = this.buffer.getByte(b);
      if ((b1 < 48 || b1 > 57) && (b != 0 || b1 != 45)) {
        if (b == 0)
          throw new NumberFormatException(String.format("Expected leading [0-9] or '-' character but was %#x", new Object[] { Byte.valueOf(b1) })); 
        break;
      } 
    } 
    return this.buffer.readDecimalLong();
  }
  
  public void readFully(Buffer paramBuffer, long paramLong) throws IOException {
    try {
      require(paramLong);
      this.buffer.readFully(paramBuffer, paramLong);
      return;
    } catch (EOFException eOFException) {
      paramBuffer.writeAll(this.buffer);
      throw eOFException;
    } 
  }
  
  public void readFully(byte[] paramArrayOfbyte) throws IOException {
    try {
      require(paramArrayOfbyte.length);
      this.buffer.readFully(paramArrayOfbyte);
      return;
    } catch (EOFException eOFException) {
      for (int i = 0; this.buffer.size > 0L; i += j) {
        int j = this.buffer.read(paramArrayOfbyte, i, (int)this.buffer.size);
        if (j == -1)
          throw new AssertionError(); 
      } 
      throw eOFException;
    } 
  }
  
  public long readHexadecimalUnsignedLong() throws IOException {
    require(1L);
    for (byte b = 0; request((b + 1)); b++) {
      byte b1 = this.buffer.getByte(b);
      if ((b1 < 48 || b1 > 57) && (b1 < 97 || b1 > 102) && (b1 < 65 || b1 > 70)) {
        if (b == 0)
          throw new NumberFormatException(String.format("Expected leading [0-9a-fA-F] character but was %#x", new Object[] { Byte.valueOf(b1) })); 
        break;
      } 
    } 
    return this.buffer.readHexadecimalUnsignedLong();
  }
  
  public int readInt() throws IOException {
    require(4L);
    return this.buffer.readInt();
  }
  
  public int readIntLe() throws IOException {
    require(4L);
    return this.buffer.readIntLe();
  }
  
  public long readLong() throws IOException {
    require(8L);
    return this.buffer.readLong();
  }
  
  public long readLongLe() throws IOException {
    require(8L);
    return this.buffer.readLongLe();
  }
  
  public short readShort() throws IOException {
    require(2L);
    return this.buffer.readShort();
  }
  
  public short readShortLe() throws IOException {
    require(2L);
    return this.buffer.readShortLe();
  }
  
  public String readString(long paramLong, Charset paramCharset) throws IOException {
    require(paramLong);
    if (paramCharset == null)
      throw new IllegalArgumentException("charset == null"); 
    return this.buffer.readString(paramLong, paramCharset);
  }
  
  public String readString(Charset paramCharset) throws IOException {
    if (paramCharset == null)
      throw new IllegalArgumentException("charset == null"); 
    this.buffer.writeAll(this.source);
    return this.buffer.readString(paramCharset);
  }
  
  public String readUtf8() throws IOException {
    this.buffer.writeAll(this.source);
    return this.buffer.readUtf8();
  }
  
  public String readUtf8(long paramLong) throws IOException {
    require(paramLong);
    return this.buffer.readUtf8(paramLong);
  }
  
  public int readUtf8CodePoint() throws IOException {
    require(1L);
    byte b = this.buffer.getByte(0L);
    if ((b & 0xE0) == 192) {
      require(2L);
      return this.buffer.readUtf8CodePoint();
    } 
    if ((b & 0xF0) == 224) {
      require(3L);
      return this.buffer.readUtf8CodePoint();
    } 
    if ((b & 0xF8) == 240)
      require(4L); 
    return this.buffer.readUtf8CodePoint();
  }
  
  @Nullable
  public String readUtf8Line() throws IOException {
    long l = indexOf((byte)10);
    return (l == -1L) ? ((this.buffer.size != 0L) ? readUtf8(this.buffer.size) : null) : this.buffer.readUtf8Line(l);
  }
  
  public String readUtf8LineStrict() throws IOException {
    return readUtf8LineStrict(Long.MAX_VALUE);
  }
  
  public String readUtf8LineStrict(long paramLong) throws IOException {
    long l1;
    if (paramLong < 0L)
      throw new IllegalArgumentException("limit < 0: " + paramLong); 
    if (paramLong == Long.MAX_VALUE) {
      l1 = Long.MAX_VALUE;
    } else {
      l1 = paramLong + 1L;
    } 
    long l2 = indexOf((byte)10, 0L, l1);
    if (l2 != -1L)
      return this.buffer.readUtf8Line(l2); 
    if (l1 < Long.MAX_VALUE && request(l1) && this.buffer.getByte(l1 - 1L) == 13 && request(1L + l1) && this.buffer.getByte(l1) == 10)
      return this.buffer.readUtf8Line(l1); 
    Buffer buffer = new Buffer();
    this.buffer.copyTo(buffer, 0L, Math.min(32L, this.buffer.size()));
    throw new EOFException("\\n not found: limit=" + Math.min(this.buffer.size(), paramLong) + " content=" + buffer.readByteString().hex() + '…');
  }
  
  public boolean request(long paramLong) throws IOException {
    // Byte code:
    //   0: lload_1
    //   1: lconst_0
    //   2: lcmp
    //   3: ifge -> 33
    //   6: new java/lang/IllegalArgumentException
    //   9: dup
    //   10: new java/lang/StringBuilder
    //   13: dup
    //   14: invokespecial <init> : ()V
    //   17: ldc 'byteCount < 0: '
    //   19: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   22: lload_1
    //   23: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   26: invokevirtual toString : ()Ljava/lang/String;
    //   29: invokespecial <init> : (Ljava/lang/String;)V
    //   32: athrow
    //   33: aload_0
    //   34: getfield closed : Z
    //   37: ifeq -> 50
    //   40: new java/lang/IllegalStateException
    //   43: dup
    //   44: ldc 'closed'
    //   46: invokespecial <init> : (Ljava/lang/String;)V
    //   49: athrow
    //   50: aload_0
    //   51: getfield buffer : Lokio/Buffer;
    //   54: getfield size : J
    //   57: lload_1
    //   58: lcmp
    //   59: ifge -> 89
    //   62: aload_0
    //   63: getfield source : Lokio/Source;
    //   66: aload_0
    //   67: getfield buffer : Lokio/Buffer;
    //   70: ldc2_w 8192
    //   73: invokeinterface read : (Lokio/Buffer;J)J
    //   78: ldc2_w -1
    //   81: lcmp
    //   82: ifne -> 50
    //   85: iconst_0
    //   86: istore_3
    //   87: iload_3
    //   88: ireturn
    //   89: iconst_1
    //   90: istore_3
    //   91: goto -> 87
  }
  
  public void require(long paramLong) throws IOException {
    if (!request(paramLong))
      throw new EOFException(); 
  }
  
  public int select(Options paramOptions) throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    while (true) {
      int i = this.buffer.selectPrefix(paramOptions);
      if (i == -1)
        return -1; 
      int j = paramOptions.byteStrings[i].size();
      if (j <= this.buffer.size) {
        this.buffer.skip(j);
        return i;
      } 
      if (this.source.read(this.buffer, 8192L) == -1L)
        return -1; 
    } 
  }
  
  public void skip(long paramLong) throws IOException {
    if (this.closed)
      throw new IllegalStateException("closed"); 
    while (paramLong > 0L) {
      if (this.buffer.size == 0L && this.source.read(this.buffer, 8192L) == -1L)
        throw new EOFException(); 
      long l = Math.min(paramLong, this.buffer.size());
      this.buffer.skip(l);
      paramLong -= l;
    } 
  }
  
  public Timeout timeout() {
    return this.source.timeout();
  }
  
  public String toString() {
    return "buffer(" + this.source + ")";
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okio/RealBufferedSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */