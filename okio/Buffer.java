package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class Buffer implements BufferedSource, BufferedSink, Cloneable {
  private static final byte[] DIGITS = new byte[] { 
      48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 
      97, 98, 99, 100, 101, 102 };
  
  static final int REPLACEMENT_CHARACTER = 65533;
  
  @Nullable
  Segment head;
  
  long size;
  
  private ByteString digest(String paramString) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance(paramString);
      if (this.head != null) {
        messageDigest.update(this.head.data, this.head.pos, this.head.limit - this.head.pos);
        for (Segment segment = this.head.next; segment != this.head; segment = segment.next)
          messageDigest.update(segment.data, segment.pos, segment.limit - segment.pos); 
      } 
      return ByteString.of(messageDigest.digest());
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      throw new AssertionError();
    } 
  }
  
  private ByteString hmac(String paramString, ByteString paramByteString) {
    try {
      Mac mac = Mac.getInstance(paramString);
      SecretKeySpec secretKeySpec = new SecretKeySpec();
      this(paramByteString.toByteArray(), paramString);
      mac.init(secretKeySpec);
      if (this.head != null) {
        mac.update(this.head.data, this.head.pos, this.head.limit - this.head.pos);
        for (Segment segment = this.head.next; segment != this.head; segment = segment.next)
          mac.update(segment.data, segment.pos, segment.limit - segment.pos); 
      } 
      return ByteString.of(mac.doFinal());
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      throw new AssertionError();
    } catch (InvalidKeyException invalidKeyException) {
      throw new IllegalArgumentException(invalidKeyException);
    } 
  }
  
  private boolean rangeEquals(Segment paramSegment, int paramInt1, ByteString paramByteString, int paramInt2, int paramInt3) {
    // Byte code:
    //   0: aload_1
    //   1: getfield limit : I
    //   4: istore #6
    //   6: aload_1
    //   7: getfield data : [B
    //   10: astore #7
    //   12: aload_1
    //   13: astore #8
    //   15: iload #4
    //   17: iload #5
    //   19: if_icmpge -> 100
    //   22: iload #6
    //   24: istore #9
    //   26: aload #8
    //   28: astore_1
    //   29: iload_2
    //   30: istore #10
    //   32: iload_2
    //   33: iload #6
    //   35: if_icmpne -> 62
    //   38: aload #8
    //   40: getfield next : Lokio/Segment;
    //   43: astore_1
    //   44: aload_1
    //   45: getfield data : [B
    //   48: astore #7
    //   50: aload_1
    //   51: getfield pos : I
    //   54: istore #10
    //   56: aload_1
    //   57: getfield limit : I
    //   60: istore #9
    //   62: aload #7
    //   64: iload #10
    //   66: baload
    //   67: aload_3
    //   68: iload #4
    //   70: invokevirtual getByte : (I)B
    //   73: if_icmpeq -> 82
    //   76: iconst_0
    //   77: istore #11
    //   79: iload #11
    //   81: ireturn
    //   82: iload #10
    //   84: iconst_1
    //   85: iadd
    //   86: istore_2
    //   87: iinc #4, 1
    //   90: iload #9
    //   92: istore #6
    //   94: aload_1
    //   95: astore #8
    //   97: goto -> 15
    //   100: iconst_1
    //   101: istore #11
    //   103: goto -> 79
  }
  
  private void readFrom(InputStream paramInputStream, long paramLong, boolean paramBoolean) throws IOException {
    if (paramInputStream == null)
      throw new IllegalArgumentException("in == null"); 
    while (paramLong > 0L || paramBoolean) {
      Segment segment = writableSegment(1);
      int i = (int)Math.min(paramLong, (8192 - segment.limit));
      i = paramInputStream.read(segment.data, segment.limit, i);
      if (i == -1) {
        if (paramBoolean)
          break; 
        throw new EOFException();
      } 
      segment.limit += i;
      this.size += i;
      paramLong -= i;
    } 
  }
  
  public Buffer buffer() {
    return this;
  }
  
  public void clear() {
    try {
      skip(this.size);
      return;
    } catch (EOFException eOFException) {
      throw new AssertionError(eOFException);
    } 
  }
  
  public Buffer clone() {
    Buffer buffer = new Buffer();
    if (this.size != 0L) {
      buffer.head = new Segment(this.head);
      Segment segment1 = buffer.head;
      Segment segment2 = buffer.head;
      Segment segment3 = buffer.head;
      segment2.prev = segment3;
      segment1.next = segment3;
      for (segment1 = this.head.next; segment1 != this.head; segment1 = segment1.next)
        buffer.head.prev.push(new Segment(segment1)); 
      buffer.size = this.size;
    } 
    return buffer;
  }
  
  public void close() {}
  
  public long completeSegmentByteCount() {
    long l1 = 0L;
    long l2 = this.size;
    if (l2 != 0L) {
      Segment segment = this.head.prev;
      l1 = l2;
      if (segment.limit < 8192) {
        l1 = l2;
        if (segment.owner)
          l1 = l2 - (segment.limit - segment.pos); 
      } 
    } 
    return l1;
  }
  
  public Buffer copyTo(OutputStream paramOutputStream) throws IOException {
    return copyTo(paramOutputStream, 0L, this.size);
  }
  
  public Buffer copyTo(OutputStream paramOutputStream, long paramLong1, long paramLong2) throws IOException {
    if (paramOutputStream == null)
      throw new IllegalArgumentException("out == null"); 
    Util.checkOffsetAndCount(this.size, paramLong1, paramLong2);
    if (paramLong2 != 0L) {
      Segment segment2;
      long l1;
      long l2;
      Segment segment1 = this.head;
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
      while (true) {
        if (l2 > 0L) {
          int i = (int)(segment2.pos + l1);
          int j = (int)Math.min((segment2.limit - i), l2);
          paramOutputStream.write(segment2.data, i, j);
          l2 -= j;
          l1 = 0L;
          segment2 = segment2.next;
          continue;
        } 
        return this;
      } 
    } 
    return this;
  }
  
  public Buffer copyTo(Buffer paramBuffer, long paramLong1, long paramLong2) {
    if (paramBuffer == null)
      throw new IllegalArgumentException("out == null"); 
    Util.checkOffsetAndCount(this.size, paramLong1, paramLong2);
    if (paramLong2 != 0L) {
      Segment segment2;
      long l1;
      long l2;
      paramBuffer.size += paramLong2;
      Segment segment1 = this.head;
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
      while (true) {
        if (l2 > 0L) {
          segment1 = new Segment(segment2);
          segment1.pos = (int)(segment1.pos + l1);
          segment1.limit = Math.min(segment1.pos + (int)l2, segment1.limit);
          if (paramBuffer.head == null) {
            segment1.prev = segment1;
            segment1.next = segment1;
            paramBuffer.head = segment1;
          } else {
            paramBuffer.head.prev.push(segment1);
          } 
          l2 -= (segment1.limit - segment1.pos);
          l1 = 0L;
          segment2 = segment2.next;
          continue;
        } 
        return this;
      } 
    } 
    return this;
  }
  
  public BufferedSink emit() {
    return this;
  }
  
  public Buffer emitCompleteSegments() {
    return this;
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (!(paramObject instanceof Buffer))
      return false; 
    paramObject = paramObject;
    if (this.size != ((Buffer)paramObject).size)
      return false; 
    if (this.size == 0L)
      return true; 
    Segment segment = this.head;
    paramObject = ((Buffer)paramObject).head;
    int i = segment.pos;
    int j = ((Segment)paramObject).pos;
    long l;
    for (l = 0L; l < this.size; l += l1) {
      long l1 = Math.min(segment.limit - i, ((Segment)paramObject).limit - j);
      byte b = 0;
      while (b < l1) {
        if (segment.data[i] != ((Segment)paramObject).data[j])
          return false; 
        b++;
        j++;
        i++;
      } 
      if (i == segment.limit) {
        segment = segment.next;
        i = segment.pos;
      } 
      if (j == ((Segment)paramObject).limit) {
        paramObject = ((Segment)paramObject).next;
        j = ((Segment)paramObject).pos;
      } 
    } 
    return true;
  }
  
  public boolean exhausted() {
    return (this.size == 0L);
  }
  
  public void flush() {}
  
  public byte getByte(long paramLong) {
    Util.checkOffsetAndCount(this.size, paramLong, 1L);
    for (Segment segment = this.head;; segment = segment.next) {
      int i = segment.limit - segment.pos;
      if (paramLong < i)
        return segment.data[segment.pos + (int)paramLong]; 
      paramLong -= i;
    } 
  }
  
  public int hashCode() {
    Segment segment = this.head;
    if (segment == null)
      return 0; 
    int i = 1;
    while (true) {
      int k = segment.pos;
      int m = segment.limit;
      int j = i;
      for (i = k; i < m; i++)
        j = j * 31 + segment.data[i]; 
      Segment segment1 = segment.next;
      i = j;
      segment = segment1;
      if (segment1 == this.head)
        return j; 
    } 
  }
  
  public ByteString hmacSha1(ByteString paramByteString) {
    return hmac("HmacSHA1", paramByteString);
  }
  
  public ByteString hmacSha256(ByteString paramByteString) {
    return hmac("HmacSHA256", paramByteString);
  }
  
  public ByteString hmacSha512(ByteString paramByteString) {
    return hmac("HmacSHA512", paramByteString);
  }
  
  public long indexOf(byte paramByte) {
    return indexOf(paramByte, 0L, Long.MAX_VALUE);
  }
  
  public long indexOf(byte paramByte, long paramLong) {
    return indexOf(paramByte, paramLong, Long.MAX_VALUE);
  }
  
  public long indexOf(byte paramByte, long paramLong1, long paramLong2) {
    Segment segment2;
    long l2;
    if (paramLong1 < 0L || paramLong2 < paramLong1)
      throw new IllegalArgumentException(String.format("size=%s fromIndex=%s toIndex=%s", new Object[] { Long.valueOf(this.size), Long.valueOf(paramLong1), Long.valueOf(paramLong2) })); 
    long l1 = paramLong2;
    if (paramLong2 > this.size)
      l1 = this.size; 
    if (paramLong1 == l1)
      return -1L; 
    Segment segment1 = this.head;
    if (segment1 == null)
      return -1L; 
    if (this.size - paramLong1 < paramLong1) {
      long l = this.size;
      while (true) {
        paramLong2 = l;
        segment2 = segment1;
        l2 = paramLong1;
        if (l > paramLong1) {
          segment1 = segment1.prev;
          l -= (segment1.limit - segment1.pos);
          continue;
        } 
        break;
      } 
    } else {
      paramLong2 = 0L;
      while (true) {
        long l = paramLong2 + (segment1.limit - segment1.pos);
        segment2 = segment1;
        l2 = paramLong1;
        if (l < paramLong1) {
          segment1 = segment1.next;
          paramLong2 = l;
          continue;
        } 
        break;
      } 
    } 
    label40: while (paramLong2 < l1) {
      byte[] arrayOfByte = segment2.data;
      int i = (int)Math.min(segment2.limit, segment2.pos + l1 - paramLong2);
      int j = (int)(segment2.pos + l2 - paramLong2);
      while (true) {
        if (j < i) {
          if (arrayOfByte[j] == paramByte)
            return (j - segment2.pos) + paramLong2; 
          j++;
          continue;
        } 
        paramLong2 += (segment2.limit - segment2.pos);
        l2 = paramLong2;
        segment2 = segment2.next;
        continue label40;
      } 
    } 
    return -1L;
  }
  
  public long indexOf(ByteString paramByteString) throws IOException {
    return indexOf(paramByteString, 0L);
  }
  
  public long indexOf(ByteString paramByteString, long paramLong) throws IOException {
    Segment segment2;
    long l2;
    if (paramByteString.size() == 0)
      throw new IllegalArgumentException("bytes is empty"); 
    if (paramLong < 0L)
      throw new IllegalArgumentException("fromIndex < 0"); 
    Segment segment1 = this.head;
    if (segment1 == null)
      return -1L; 
    if (this.size - paramLong < paramLong) {
      long l = this.size;
      while (true) {
        segment2 = segment1;
        l2 = l;
        if (l > paramLong) {
          segment1 = segment1.prev;
          l -= (segment1.limit - segment1.pos);
          continue;
        } 
        break;
      } 
    } else {
      l2 = 0L;
      while (true) {
        long l = l2 + (segment1.limit - segment1.pos);
        segment2 = segment1;
        if (l < paramLong) {
          segment1 = segment1.next;
          l2 = l;
          continue;
        } 
        break;
      } 
    } 
    byte b = paramByteString.getByte(0);
    int i = paramByteString.size();
    long l1 = this.size - i + 1L;
    while (l2 < l1) {
      byte[] arrayOfByte = segment2.data;
      int j = (int)Math.min(segment2.limit, segment2.pos + l1 - l2);
      for (int k = (int)(segment2.pos + paramLong - l2); k < j; k++) {
        if (arrayOfByte[k] == b && rangeEquals(segment2, k + 1, paramByteString, 1, i))
          return (k - segment2.pos) + l2; 
      } 
      l2 += (segment2.limit - segment2.pos);
      paramLong = l2;
      segment2 = segment2.next;
    } 
    return -1L;
  }
  
  public long indexOfElement(ByteString paramByteString) {
    return indexOfElement(paramByteString, 0L);
  }
  
  public long indexOfElement(ByteString paramByteString, long paramLong) {
    byte[] arrayOfByte;
    long l;
    Segment segment2;
    if (paramLong < 0L)
      throw new IllegalArgumentException("fromIndex < 0"); 
    Segment segment1 = this.head;
    if (segment1 == null)
      return -1L; 
    if (this.size - paramLong < paramLong) {
      long l1 = this.size;
      while (true) {
        l = l1;
        segment2 = segment1;
        if (l1 > paramLong) {
          segment1 = segment1.prev;
          l1 -= (segment1.limit - segment1.pos);
          continue;
        } 
        break;
      } 
    } else {
      l = 0L;
      while (true) {
        long l1 = l + (segment1.limit - segment1.pos);
        segment2 = segment1;
        if (l1 < paramLong) {
          segment1 = segment1.next;
          l = l1;
          continue;
        } 
        break;
      } 
    } 
    if (paramByteString.size() == 2) {
      byte b1 = paramByteString.getByte(0);
      byte b2 = paramByteString.getByte(1);
      while (l < this.size) {
        arrayOfByte = segment2.data;
        int i = (int)(segment2.pos + paramLong - l);
        int j = segment2.limit;
        while (i < j) {
          byte b = arrayOfByte[i];
          if (b == b1 || b == b2)
            return (i - segment2.pos) + l; 
          i++;
        } 
        l += (segment2.limit - segment2.pos);
        paramLong = l;
        segment2 = segment2.next;
      } 
    } else {
      arrayOfByte = arrayOfByte.internalArray();
      while (l < this.size) {
        byte[] arrayOfByte1 = segment2.data;
        int i = (int)(segment2.pos + paramLong - l);
        int j = segment2.limit;
        while (i < j) {
          byte b1 = arrayOfByte1[i];
          int k = arrayOfByte.length;
          for (byte b = 0; b < k; b++) {
            if (b1 == arrayOfByte[b])
              return (i - segment2.pos) + l; 
          } 
          i++;
        } 
        l += (segment2.limit - segment2.pos);
        paramLong = l;
        segment2 = segment2.next;
      } 
    } 
    return -1L;
  }
  
  public InputStream inputStream() {
    return new InputStream() {
        public int available() {
          return (int)Math.min(Buffer.this.size, 2147483647L);
        }
        
        public void close() {}
        
        public int read() {
          return (Buffer.this.size > 0L) ? (Buffer.this.readByte() & 0xFF) : -1;
        }
        
        public int read(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) {
          return Buffer.this.read(param1ArrayOfbyte, param1Int1, param1Int2);
        }
        
        public String toString() {
          return Buffer.this + ".inputStream()";
        }
      };
  }
  
  public ByteString md5() {
    return digest("MD5");
  }
  
  public OutputStream outputStream() {
    return new OutputStream() {
        public void close() {}
        
        public void flush() {}
        
        public String toString() {
          return Buffer.this + ".outputStream()";
        }
        
        public void write(int param1Int) {
          Buffer.this.writeByte((byte)param1Int);
        }
        
        public void write(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) {
          Buffer.this.write(param1ArrayOfbyte, param1Int1, param1Int2);
        }
      };
  }
  
  public boolean rangeEquals(long paramLong, ByteString paramByteString) {
    return rangeEquals(paramLong, paramByteString, 0, paramByteString.size());
  }
  
  public boolean rangeEquals(long paramLong, ByteString paramByteString, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #6
    //   3: iload #6
    //   5: istore #7
    //   7: lload_1
    //   8: lconst_0
    //   9: lcmp
    //   10: iflt -> 64
    //   13: iload #6
    //   15: istore #7
    //   17: iload #4
    //   19: iflt -> 64
    //   22: iload #6
    //   24: istore #7
    //   26: iload #5
    //   28: iflt -> 64
    //   31: iload #6
    //   33: istore #7
    //   35: aload_0
    //   36: getfield size : J
    //   39: lload_1
    //   40: lsub
    //   41: iload #5
    //   43: i2l
    //   44: lcmp
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
    //   74: if_icmpge -> 108
    //   77: iload #6
    //   79: istore #7
    //   81: aload_0
    //   82: iload #8
    //   84: i2l
    //   85: lload_1
    //   86: ladd
    //   87: invokevirtual getByte : (J)B
    //   90: aload_3
    //   91: iload #4
    //   93: iload #8
    //   95: iadd
    //   96: invokevirtual getByte : (I)B
    //   99: if_icmpne -> 64
    //   102: iinc #8, 1
    //   105: goto -> 70
    //   108: iconst_1
    //   109: istore #7
    //   111: goto -> 64
  }
  
  public int read(byte[] paramArrayOfbyte) {
    return read(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    Util.checkOffsetAndCount(paramArrayOfbyte.length, paramInt1, paramInt2);
    Segment segment = this.head;
    if (segment == null)
      return -1; 
    paramInt2 = Math.min(paramInt2, segment.limit - segment.pos);
    System.arraycopy(segment.data, segment.pos, paramArrayOfbyte, paramInt1, paramInt2);
    segment.pos += paramInt2;
    this.size -= paramInt2;
    paramInt1 = paramInt2;
    if (segment.pos == segment.limit) {
      this.head = segment.pop();
      SegmentPool.recycle(segment);
      paramInt1 = paramInt2;
    } 
    return paramInt1;
  }
  
  public long read(Buffer paramBuffer, long paramLong) {
    if (paramBuffer == null)
      throw new IllegalArgumentException("sink == null"); 
    if (paramLong < 0L)
      throw new IllegalArgumentException("byteCount < 0: " + paramLong); 
    if (this.size == 0L)
      return -1L; 
    long l = paramLong;
    if (paramLong > this.size)
      l = this.size; 
    paramBuffer.write(this, l);
    return l;
  }
  
  public long readAll(Sink paramSink) throws IOException {
    long l = this.size;
    if (l > 0L)
      paramSink.write(this, l); 
    return l;
  }
  
  public byte readByte() {
    if (this.size == 0L)
      throw new IllegalStateException("size == 0"); 
    Segment segment = this.head;
    int i = segment.pos;
    int j = segment.limit;
    byte[] arrayOfByte = segment.data;
    int k = i + 1;
    byte b = arrayOfByte[i];
    this.size--;
    if (k == j) {
      this.head = segment.pop();
      SegmentPool.recycle(segment);
      return b;
    } 
    segment.pos = k;
    return b;
  }
  
  public byte[] readByteArray() {
    try {
      return readByteArray(this.size);
    } catch (EOFException eOFException) {
      throw new AssertionError(eOFException);
    } 
  }
  
  public byte[] readByteArray(long paramLong) throws EOFException {
    Util.checkOffsetAndCount(this.size, 0L, paramLong);
    if (paramLong > 2147483647L)
      throw new IllegalArgumentException("byteCount > Integer.MAX_VALUE: " + paramLong); 
    byte[] arrayOfByte = new byte[(int)paramLong];
    readFully(arrayOfByte);
    return arrayOfByte;
  }
  
  public ByteString readByteString() {
    return new ByteString(readByteArray());
  }
  
  public ByteString readByteString(long paramLong) throws EOFException {
    return new ByteString(readByteArray(paramLong));
  }
  
  public long readDecimalLong() {
    // Byte code:
    //   0: aload_0
    //   1: getfield size : J
    //   4: lconst_0
    //   5: lcmp
    //   6: ifne -> 20
    //   9: new java/lang/IllegalStateException
    //   12: dup
    //   13: ldc_w 'size == 0'
    //   16: invokespecial <init> : (Ljava/lang/String;)V
    //   19: athrow
    //   20: lconst_0
    //   21: lstore_1
    //   22: iconst_0
    //   23: istore_3
    //   24: iconst_0
    //   25: istore #4
    //   27: iconst_0
    //   28: istore #5
    //   30: ldc2_w -7
    //   33: lstore #6
    //   35: aload_0
    //   36: getfield head : Lokio/Segment;
    //   39: astore #8
    //   41: aload #8
    //   43: getfield data : [B
    //   46: astore #9
    //   48: aload #8
    //   50: getfield pos : I
    //   53: istore #10
    //   55: aload #8
    //   57: getfield limit : I
    //   60: istore #11
    //   62: lload_1
    //   63: lstore #12
    //   65: iload_3
    //   66: istore #14
    //   68: lload #6
    //   70: lstore_1
    //   71: iload #10
    //   73: istore_3
    //   74: iload #4
    //   76: istore #10
    //   78: iload #5
    //   80: istore #4
    //   82: iload_3
    //   83: iload #11
    //   85: if_icmpge -> 286
    //   88: aload #9
    //   90: iload_3
    //   91: baload
    //   92: istore #15
    //   94: iload #15
    //   96: bipush #48
    //   98: if_icmplt -> 224
    //   101: iload #15
    //   103: bipush #57
    //   105: if_icmpgt -> 224
    //   108: bipush #48
    //   110: iload #15
    //   112: isub
    //   113: istore #4
    //   115: lload #12
    //   117: ldc2_w -922337203685477580
    //   120: lcmp
    //   121: iflt -> 141
    //   124: lload #12
    //   126: ldc2_w -922337203685477580
    //   129: lcmp
    //   130: ifne -> 203
    //   133: iload #4
    //   135: i2l
    //   136: lload_1
    //   137: lcmp
    //   138: ifge -> 203
    //   141: new okio/Buffer
    //   144: dup
    //   145: invokespecial <init> : ()V
    //   148: lload #12
    //   150: invokevirtual writeDecimalLong : (J)Lokio/Buffer;
    //   153: iload #15
    //   155: invokevirtual writeByte : (I)Lokio/Buffer;
    //   158: astore #8
    //   160: iload #10
    //   162: ifne -> 171
    //   165: aload #8
    //   167: invokevirtual readByte : ()B
    //   170: pop
    //   171: new java/lang/NumberFormatException
    //   174: dup
    //   175: new java/lang/StringBuilder
    //   178: dup
    //   179: invokespecial <init> : ()V
    //   182: ldc_w 'Number too large: '
    //   185: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   188: aload #8
    //   190: invokevirtual readUtf8 : ()Ljava/lang/String;
    //   193: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   196: invokevirtual toString : ()Ljava/lang/String;
    //   199: invokespecial <init> : (Ljava/lang/String;)V
    //   202: athrow
    //   203: lload #12
    //   205: ldc2_w 10
    //   208: lmul
    //   209: iload #4
    //   211: i2l
    //   212: ladd
    //   213: lstore #12
    //   215: iinc #3, 1
    //   218: iinc #14, 1
    //   221: goto -> 78
    //   224: iload #15
    //   226: bipush #45
    //   228: if_icmpne -> 246
    //   231: iload #14
    //   233: ifne -> 246
    //   236: iconst_1
    //   237: istore #10
    //   239: lload_1
    //   240: lconst_1
    //   241: lsub
    //   242: lstore_1
    //   243: goto -> 215
    //   246: iload #14
    //   248: ifne -> 283
    //   251: new java/lang/NumberFormatException
    //   254: dup
    //   255: new java/lang/StringBuilder
    //   258: dup
    //   259: invokespecial <init> : ()V
    //   262: ldc_w 'Expected leading [0-9] or '-' character but was 0x'
    //   265: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   268: iload #15
    //   270: invokestatic toHexString : (I)Ljava/lang/String;
    //   273: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   276: invokevirtual toString : ()Ljava/lang/String;
    //   279: invokespecial <init> : (Ljava/lang/String;)V
    //   282: athrow
    //   283: iconst_1
    //   284: istore #4
    //   286: iload_3
    //   287: iload #11
    //   289: if_icmpne -> 355
    //   292: aload_0
    //   293: aload #8
    //   295: invokevirtual pop : ()Lokio/Segment;
    //   298: putfield head : Lokio/Segment;
    //   301: aload #8
    //   303: invokestatic recycle : (Lokio/Segment;)V
    //   306: iload #4
    //   308: ifne -> 335
    //   311: iload #4
    //   313: istore #5
    //   315: iload #10
    //   317: istore #4
    //   319: lload_1
    //   320: lstore #6
    //   322: iload #14
    //   324: istore_3
    //   325: lload #12
    //   327: lstore_1
    //   328: aload_0
    //   329: getfield head : Lokio/Segment;
    //   332: ifnonnull -> 35
    //   335: aload_0
    //   336: aload_0
    //   337: getfield size : J
    //   340: iload #14
    //   342: i2l
    //   343: lsub
    //   344: putfield size : J
    //   347: iload #10
    //   349: ifeq -> 364
    //   352: lload #12
    //   354: lreturn
    //   355: aload #8
    //   357: iload_3
    //   358: putfield pos : I
    //   361: goto -> 306
    //   364: lload #12
    //   366: lneg
    //   367: lstore #12
    //   369: goto -> 352
  }
  
  public Buffer readFrom(InputStream paramInputStream) throws IOException {
    readFrom(paramInputStream, Long.MAX_VALUE, true);
    return this;
  }
  
  public Buffer readFrom(InputStream paramInputStream, long paramLong) throws IOException {
    if (paramLong < 0L)
      throw new IllegalArgumentException("byteCount < 0: " + paramLong); 
    readFrom(paramInputStream, paramLong, false);
    return this;
  }
  
  public void readFully(Buffer paramBuffer, long paramLong) throws EOFException {
    if (this.size < paramLong) {
      paramBuffer.write(this, this.size);
      throw new EOFException();
    } 
    paramBuffer.write(this, paramLong);
  }
  
  public void readFully(byte[] paramArrayOfbyte) throws EOFException {
    for (int i = 0; i < paramArrayOfbyte.length; i += j) {
      int j = read(paramArrayOfbyte, i, paramArrayOfbyte.length - i);
      if (j == -1)
        throw new EOFException(); 
    } 
  }
  
  public long readHexadecimalUnsignedLong() {
    // Byte code:
    //   0: aload_0
    //   1: getfield size : J
    //   4: lconst_0
    //   5: lcmp
    //   6: ifne -> 20
    //   9: new java/lang/IllegalStateException
    //   12: dup
    //   13: ldc_w 'size == 0'
    //   16: invokespecial <init> : (Ljava/lang/String;)V
    //   19: athrow
    //   20: lconst_0
    //   21: lstore_1
    //   22: iconst_0
    //   23: istore_3
    //   24: iconst_0
    //   25: istore #4
    //   27: aload_0
    //   28: getfield head : Lokio/Segment;
    //   31: astore #5
    //   33: aload #5
    //   35: getfield data : [B
    //   38: astore #6
    //   40: aload #5
    //   42: getfield pos : I
    //   45: istore #7
    //   47: aload #5
    //   49: getfield limit : I
    //   52: istore #8
    //   54: lload_1
    //   55: lstore #9
    //   57: iload_3
    //   58: istore #11
    //   60: iload #4
    //   62: istore_3
    //   63: iload #7
    //   65: iload #8
    //   67: if_icmpge -> 250
    //   70: aload #6
    //   72: iload #7
    //   74: baload
    //   75: istore #12
    //   77: iload #12
    //   79: bipush #48
    //   81: if_icmplt -> 159
    //   84: iload #12
    //   86: bipush #57
    //   88: if_icmpgt -> 159
    //   91: iload #12
    //   93: bipush #48
    //   95: isub
    //   96: istore_3
    //   97: ldc2_w -1152921504606846976
    //   100: lload #9
    //   102: land
    //   103: lconst_0
    //   104: lcmp
    //   105: ifeq -> 306
    //   108: new okio/Buffer
    //   111: dup
    //   112: invokespecial <init> : ()V
    //   115: lload #9
    //   117: invokevirtual writeHexadecimalUnsignedLong : (J)Lokio/Buffer;
    //   120: iload #12
    //   122: invokevirtual writeByte : (I)Lokio/Buffer;
    //   125: astore #5
    //   127: new java/lang/NumberFormatException
    //   130: dup
    //   131: new java/lang/StringBuilder
    //   134: dup
    //   135: invokespecial <init> : ()V
    //   138: ldc_w 'Number too large: '
    //   141: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   144: aload #5
    //   146: invokevirtual readUtf8 : ()Ljava/lang/String;
    //   149: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   152: invokevirtual toString : ()Ljava/lang/String;
    //   155: invokespecial <init> : (Ljava/lang/String;)V
    //   158: athrow
    //   159: iload #12
    //   161: bipush #97
    //   163: if_icmplt -> 185
    //   166: iload #12
    //   168: bipush #102
    //   170: if_icmpgt -> 185
    //   173: iload #12
    //   175: bipush #97
    //   177: isub
    //   178: bipush #10
    //   180: iadd
    //   181: istore_3
    //   182: goto -> 97
    //   185: iload #12
    //   187: bipush #65
    //   189: if_icmplt -> 211
    //   192: iload #12
    //   194: bipush #70
    //   196: if_icmpgt -> 211
    //   199: iload #12
    //   201: bipush #65
    //   203: isub
    //   204: bipush #10
    //   206: iadd
    //   207: istore_3
    //   208: goto -> 97
    //   211: iload #11
    //   213: ifne -> 248
    //   216: new java/lang/NumberFormatException
    //   219: dup
    //   220: new java/lang/StringBuilder
    //   223: dup
    //   224: invokespecial <init> : ()V
    //   227: ldc_w 'Expected leading [0-9a-fA-F] character but was 0x'
    //   230: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   233: iload #12
    //   235: invokestatic toHexString : (I)Ljava/lang/String;
    //   238: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   241: invokevirtual toString : ()Ljava/lang/String;
    //   244: invokespecial <init> : (Ljava/lang/String;)V
    //   247: athrow
    //   248: iconst_1
    //   249: istore_3
    //   250: iload #7
    //   252: iload #8
    //   254: if_icmpne -> 324
    //   257: aload_0
    //   258: aload #5
    //   260: invokevirtual pop : ()Lokio/Segment;
    //   263: putfield head : Lokio/Segment;
    //   266: aload #5
    //   268: invokestatic recycle : (Lokio/Segment;)V
    //   271: iload_3
    //   272: ifne -> 291
    //   275: iload_3
    //   276: istore #4
    //   278: iload #11
    //   280: istore_3
    //   281: lload #9
    //   283: lstore_1
    //   284: aload_0
    //   285: getfield head : Lokio/Segment;
    //   288: ifnonnull -> 27
    //   291: aload_0
    //   292: aload_0
    //   293: getfield size : J
    //   296: iload #11
    //   298: i2l
    //   299: lsub
    //   300: putfield size : J
    //   303: lload #9
    //   305: lreturn
    //   306: lload #9
    //   308: iconst_4
    //   309: lshl
    //   310: iload_3
    //   311: i2l
    //   312: lor
    //   313: lstore #9
    //   315: iinc #7, 1
    //   318: iinc #11, 1
    //   321: goto -> 60
    //   324: aload #5
    //   326: iload #7
    //   328: putfield pos : I
    //   331: goto -> 271
  }
  
  public int readInt() {
    if (this.size < 4L)
      throw new IllegalStateException("size < 4: " + this.size); 
    Segment segment = this.head;
    int i = segment.pos;
    int j = segment.limit;
    if (j - i < 4)
      return (readByte() & 0xFF) << 24 | (readByte() & 0xFF) << 16 | (readByte() & 0xFF) << 8 | readByte() & 0xFF; 
    byte[] arrayOfByte = segment.data;
    int k = i + 1;
    i = arrayOfByte[i];
    int m = k + 1;
    byte b1 = arrayOfByte[k];
    k = m + 1;
    byte b2 = arrayOfByte[m];
    m = k + 1;
    i = (i & 0xFF) << 24 | (b1 & 0xFF) << 16 | (b2 & 0xFF) << 8 | arrayOfByte[k] & 0xFF;
    this.size -= 4L;
    if (m == j) {
      this.head = segment.pop();
      SegmentPool.recycle(segment);
      return i;
    } 
    segment.pos = m;
    return i;
  }
  
  public int readIntLe() {
    return Util.reverseBytesInt(readInt());
  }
  
  public long readLong() {
    if (this.size < 8L)
      throw new IllegalStateException("size < 8: " + this.size); 
    Segment segment = this.head;
    int i = segment.pos;
    int j = segment.limit;
    if (j - i < 8)
      return (readInt() & 0xFFFFFFFFL) << 32L | readInt() & 0xFFFFFFFFL; 
    byte[] arrayOfByte = segment.data;
    int k = i + 1;
    long l2 = arrayOfByte[i];
    i = k + 1;
    long l3 = arrayOfByte[k];
    k = i + 1;
    long l4 = arrayOfByte[i];
    i = k + 1;
    long l5 = arrayOfByte[k];
    k = i + 1;
    long l6 = arrayOfByte[i];
    i = k + 1;
    long l7 = arrayOfByte[k];
    k = i + 1;
    long l1 = arrayOfByte[i];
    i = k + 1;
    l1 = (l2 & 0xFFL) << 56L | (l3 & 0xFFL) << 48L | (l4 & 0xFFL) << 40L | (l5 & 0xFFL) << 32L | (l6 & 0xFFL) << 24L | (l7 & 0xFFL) << 16L | (l1 & 0xFFL) << 8L | arrayOfByte[k] & 0xFFL;
    this.size -= 8L;
    if (i == j) {
      this.head = segment.pop();
      SegmentPool.recycle(segment);
      return l1;
    } 
    segment.pos = i;
    return l1;
  }
  
  public long readLongLe() {
    return Util.reverseBytesLong(readLong());
  }
  
  public short readShort() {
    if (this.size < 2L)
      throw new IllegalStateException("size < 2: " + this.size); 
    Segment segment = this.head;
    int i = segment.pos;
    int j = segment.limit;
    if (j - i < 2) {
      j = (short)((readByte() & 0xFF) << 8 | readByte() & 0xFF);
      return j;
    } 
    byte[] arrayOfByte = segment.data;
    int k = i + 1;
    i = arrayOfByte[i];
    int m = k + 1;
    k = arrayOfByte[k];
    this.size -= 2L;
    if (m == j) {
      this.head = segment.pop();
      SegmentPool.recycle(segment);
    } else {
      segment.pos = m;
    } 
    j = (short)((i & 0xFF) << 8 | k & 0xFF);
    return j;
  }
  
  public short readShortLe() {
    return Util.reverseBytesShort(readShort());
  }
  
  public String readString(long paramLong, Charset paramCharset) throws EOFException {
    Util.checkOffsetAndCount(this.size, 0L, paramLong);
    if (paramCharset == null)
      throw new IllegalArgumentException("charset == null"); 
    if (paramLong > 2147483647L)
      throw new IllegalArgumentException("byteCount > Integer.MAX_VALUE: " + paramLong); 
    if (paramLong == 0L)
      return ""; 
    Segment segment = this.head;
    if (segment.pos + paramLong > segment.limit)
      return new String(readByteArray(paramLong), paramCharset); 
    String str2 = new String(segment.data, segment.pos, (int)paramLong, paramCharset);
    segment.pos = (int)(segment.pos + paramLong);
    this.size -= paramLong;
    String str1 = str2;
    if (segment.pos == segment.limit) {
      this.head = segment.pop();
      SegmentPool.recycle(segment);
      str1 = str2;
    } 
    return str1;
  }
  
  public String readString(Charset paramCharset) {
    try {
      return readString(this.size, paramCharset);
    } catch (EOFException eOFException) {
      throw new AssertionError(eOFException);
    } 
  }
  
  public String readUtf8() {
    try {
      return readString(this.size, Util.UTF_8);
    } catch (EOFException eOFException) {
      throw new AssertionError(eOFException);
    } 
  }
  
  public String readUtf8(long paramLong) throws EOFException {
    return readString(paramLong, Util.UTF_8);
  }
  
  public int readUtf8CodePoint() throws EOFException {
    int i;
    byte b1;
    int k;
    if (this.size == 0L)
      throw new EOFException(); 
    byte b = getByte(0L);
    if ((b & 0x80) == 0) {
      int m = b & Byte.MAX_VALUE;
      b1 = 1;
      k = 0;
    } else if ((b & 0xE0) == 192) {
      int m = b & 0x1F;
      b1 = 2;
      k = 128;
    } else if ((b & 0xF0) == 224) {
      int m = b & 0xF;
      b1 = 3;
      k = 2048;
    } else if ((b & 0xF8) == 240) {
      int m = b & 0x7;
      b1 = 4;
      k = 65536;
    } else {
      skip(1L);
      return 65533;
    } 
    if (this.size < b1)
      throw new EOFException("size < " + b1 + ": " + this.size + " (to read code point prefixed 0x" + Integer.toHexString(b) + ")"); 
    byte b2 = 1;
    Object object = SYNTHETIC_LOCAL_VARIABLE_2;
    while (b2 < b1) {
      byte b3 = getByte(b2);
      if ((b3 & 0xC0) == 128) {
        i = object << 6 | b3 & 0x3F;
        b2++;
        continue;
      } 
      skip(b2);
      return 65533;
    } 
    skip(b1);
    if (i > 1114111)
      return 65533; 
    if (i >= 55296 && i <= 57343)
      return 65533; 
    int j = i;
    if (i < k)
      j = 65533; 
    return j;
  }
  
  @Nullable
  public String readUtf8Line() throws EOFException {
    long l = indexOf((byte)10);
    return (l == -1L) ? ((this.size != 0L) ? readUtf8(this.size) : null) : readUtf8Line(l);
  }
  
  String readUtf8Line(long paramLong) throws EOFException {
    if (paramLong > 0L && getByte(paramLong - 1L) == 13) {
      String str1 = readUtf8(paramLong - 1L);
      skip(2L);
      return str1;
    } 
    String str = readUtf8(paramLong);
    skip(1L);
    return str;
  }
  
  public String readUtf8LineStrict() throws EOFException {
    return readUtf8LineStrict(Long.MAX_VALUE);
  }
  
  public String readUtf8LineStrict(long paramLong) throws EOFException {
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
      return readUtf8Line(l2); 
    if (l1 < size() && getByte(l1 - 1L) == 13 && getByte(l1) == 10)
      return readUtf8Line(l1); 
    Buffer buffer = new Buffer();
    copyTo(buffer, 0L, Math.min(32L, size()));
    throw new EOFException("\\n not found: limit=" + Math.min(size(), paramLong) + " content=" + buffer.readByteString().hex() + '…');
  }
  
  public boolean request(long paramLong) {
    return (this.size >= paramLong);
  }
  
  public void require(long paramLong) throws EOFException {
    if (this.size < paramLong)
      throw new EOFException(); 
  }
  
  List<Integer> segmentSizes() {
    if (this.head == null)
      return (List)Collections.emptyList(); 
    ArrayList<Integer> arrayList = new ArrayList();
    arrayList.add(Integer.valueOf(this.head.limit - this.head.pos));
    Segment segment = this.head.next;
    while (true) {
      ArrayList<Integer> arrayList1 = arrayList;
      if (segment != this.head) {
        arrayList.add(Integer.valueOf(segment.limit - segment.pos));
        segment = segment.next;
        continue;
      } 
      return arrayList1;
    } 
  }
  
  public int select(Options paramOptions) {
    // Byte code:
    //   0: aload_0
    //   1: getfield head : Lokio/Segment;
    //   4: astore_2
    //   5: aload_2
    //   6: ifnonnull -> 19
    //   9: aload_1
    //   10: getstatic okio/ByteString.EMPTY : Lokio/ByteString;
    //   13: invokevirtual indexOf : (Ljava/lang/Object;)I
    //   16: istore_3
    //   17: iload_3
    //   18: ireturn
    //   19: aload_1
    //   20: getfield byteStrings : [Lokio/ByteString;
    //   23: astore #4
    //   25: iconst_0
    //   26: istore_3
    //   27: aload #4
    //   29: arraylength
    //   30: istore #5
    //   32: iload_3
    //   33: iload #5
    //   35: if_icmpge -> 102
    //   38: aload #4
    //   40: iload_3
    //   41: aaload
    //   42: astore_1
    //   43: aload_0
    //   44: getfield size : J
    //   47: aload_1
    //   48: invokevirtual size : ()I
    //   51: i2l
    //   52: lcmp
    //   53: iflt -> 96
    //   56: aload_0
    //   57: aload_2
    //   58: aload_2
    //   59: getfield pos : I
    //   62: aload_1
    //   63: iconst_0
    //   64: aload_1
    //   65: invokevirtual size : ()I
    //   68: invokespecial rangeEquals : (Lokio/Segment;ILokio/ByteString;II)Z
    //   71: ifeq -> 96
    //   74: aload_0
    //   75: aload_1
    //   76: invokevirtual size : ()I
    //   79: i2l
    //   80: invokevirtual skip : (J)V
    //   83: goto -> 17
    //   86: astore_1
    //   87: new java/lang/AssertionError
    //   90: dup
    //   91: aload_1
    //   92: invokespecial <init> : (Ljava/lang/Object;)V
    //   95: athrow
    //   96: iinc #3, 1
    //   99: goto -> 32
    //   102: iconst_m1
    //   103: istore_3
    //   104: goto -> 17
    // Exception table:
    //   from	to	target	type
    //   74	83	86	java/io/EOFException
  }
  
  int selectPrefix(Options paramOptions) {
    // Byte code:
    //   0: aload_0
    //   1: getfield head : Lokio/Segment;
    //   4: astore_2
    //   5: aload_1
    //   6: getfield byteStrings : [Lokio/ByteString;
    //   9: astore_3
    //   10: iconst_0
    //   11: istore #4
    //   13: aload_3
    //   14: arraylength
    //   15: istore #5
    //   17: iload #4
    //   19: iload #5
    //   21: if_icmpge -> 82
    //   24: aload_3
    //   25: iload #4
    //   27: aaload
    //   28: astore_1
    //   29: aload_0
    //   30: getfield size : J
    //   33: aload_1
    //   34: invokevirtual size : ()I
    //   37: i2l
    //   38: invokestatic min : (JJ)J
    //   41: l2i
    //   42: istore #6
    //   44: iload #4
    //   46: istore #7
    //   48: iload #6
    //   50: ifeq -> 73
    //   53: aload_0
    //   54: aload_2
    //   55: aload_2
    //   56: getfield pos : I
    //   59: aload_1
    //   60: iconst_0
    //   61: iload #6
    //   63: invokespecial rangeEquals : (Lokio/Segment;ILokio/ByteString;II)Z
    //   66: ifeq -> 76
    //   69: iload #4
    //   71: istore #7
    //   73: iload #7
    //   75: ireturn
    //   76: iinc #4, 1
    //   79: goto -> 17
    //   82: iconst_m1
    //   83: istore #7
    //   85: goto -> 73
  }
  
  public ByteString sha1() {
    return digest("SHA-1");
  }
  
  public ByteString sha256() {
    return digest("SHA-256");
  }
  
  public ByteString sha512() {
    return digest("SHA-512");
  }
  
  public long size() {
    return this.size;
  }
  
  public void skip(long paramLong) throws EOFException {
    while (paramLong > 0L) {
      if (this.head == null)
        throw new EOFException(); 
      int i = (int)Math.min(paramLong, (this.head.limit - this.head.pos));
      this.size -= i;
      long l = paramLong - i;
      Segment segment = this.head;
      segment.pos += i;
      paramLong = l;
      if (this.head.pos == this.head.limit) {
        segment = this.head;
        this.head = segment.pop();
        SegmentPool.recycle(segment);
        paramLong = l;
      } 
    } 
  }
  
  public ByteString snapshot() {
    if (this.size > 2147483647L)
      throw new IllegalArgumentException("size > Integer.MAX_VALUE: " + this.size); 
    return snapshot((int)this.size);
  }
  
  public ByteString snapshot(int paramInt) {
    return (paramInt == 0) ? ByteString.EMPTY : new SegmentedByteString(this, paramInt);
  }
  
  public Timeout timeout() {
    return Timeout.NONE;
  }
  
  public String toString() {
    return snapshot().toString();
  }
  
  Segment writableSegment(int paramInt) {
    if (paramInt < 1 || paramInt > 8192)
      throw new IllegalArgumentException(); 
    if (this.head == null) {
      this.head = SegmentPool.take();
      Segment segment1 = this.head;
      Segment segment2 = this.head;
      Segment segment3 = this.head;
      segment2.prev = segment3;
      segment1.next = segment3;
      return segment3;
    } 
    Segment segment = this.head.prev;
    if (segment.limit + paramInt <= 8192) {
      Segment segment1 = segment;
      return !segment.owner ? segment.push(SegmentPool.take()) : segment1;
    } 
    return segment.push(SegmentPool.take());
  }
  
  public Buffer write(ByteString paramByteString) {
    if (paramByteString == null)
      throw new IllegalArgumentException("byteString == null"); 
    paramByteString.write(this);
    return this;
  }
  
  public Buffer write(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte == null)
      throw new IllegalArgumentException("source == null"); 
    return write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public Buffer write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    if (paramArrayOfbyte == null)
      throw new IllegalArgumentException("source == null"); 
    Util.checkOffsetAndCount(paramArrayOfbyte.length, paramInt1, paramInt2);
    int i = paramInt1 + paramInt2;
    while (paramInt1 < i) {
      Segment segment = writableSegment(1);
      int j = Math.min(i - paramInt1, 8192 - segment.limit);
      System.arraycopy(paramArrayOfbyte, paramInt1, segment.data, segment.limit, j);
      paramInt1 += j;
      segment.limit += j;
    } 
    this.size += paramInt2;
    return this;
  }
  
  public BufferedSink write(Source paramSource, long paramLong) throws IOException {
    while (paramLong > 0L) {
      long l = paramSource.read(this, paramLong);
      if (l == -1L)
        throw new EOFException(); 
      paramLong -= l;
    } 
    return this;
  }
  
  public void write(Buffer paramBuffer, long paramLong) {
    if (paramBuffer == null)
      throw new IllegalArgumentException("source == null"); 
    if (paramBuffer == this)
      throw new IllegalArgumentException("source == this"); 
    Util.checkOffsetAndCount(paramBuffer.size, 0L, paramLong);
    while (true) {
      if (paramLong > 0L) {
        if (paramLong < (paramBuffer.head.limit - paramBuffer.head.pos)) {
          Segment segment1;
          if (this.head != null) {
            segment1 = this.head.prev;
          } else {
            segment1 = null;
          } 
          if (segment1 != null && segment1.owner) {
            int i;
            long l1 = segment1.limit;
            if (segment1.shared) {
              i = 0;
            } else {
              i = segment1.pos;
            } 
            if (paramLong + l1 - i <= 8192L) {
              paramBuffer.head.writeTo(segment1, (int)paramLong);
              paramBuffer.size -= paramLong;
              this.size += paramLong;
              return;
            } 
          } 
          paramBuffer.head = paramBuffer.head.split((int)paramLong);
        } 
      } else {
        return;
      } 
      Segment segment = paramBuffer.head;
      long l = (segment.limit - segment.pos);
      paramBuffer.head = segment.pop();
      if (this.head == null) {
        this.head = segment;
        Segment segment1 = this.head;
        Segment segment2 = this.head;
        segment = this.head;
        segment2.prev = segment;
        segment1.next = segment;
      } else {
        this.head.prev.push(segment).compact();
      } 
      paramBuffer.size -= l;
      this.size += l;
      paramLong -= l;
    } 
  }
  
  public long writeAll(Source paramSource) throws IOException {
    if (paramSource == null)
      throw new IllegalArgumentException("source == null"); 
    long l = 0L;
    while (true) {
      long l1 = paramSource.read(this, 8192L);
      if (l1 != -1L) {
        l += l1;
        continue;
      } 
      return l;
    } 
  }
  
  public Buffer writeByte(int paramInt) {
    Segment segment = writableSegment(1);
    byte[] arrayOfByte = segment.data;
    int i = segment.limit;
    segment.limit = i + 1;
    arrayOfByte[i] = (byte)(byte)paramInt;
    this.size++;
    return this;
  }
  
  public Buffer writeDecimalLong(long paramLong) {
    if (paramLong == 0L)
      return writeByte(48); 
    boolean bool = false;
    long l = paramLong;
    if (paramLong < 0L) {
      l = -paramLong;
      if (l < 0L)
        return writeUtf8("-9223372036854775808"); 
      bool = true;
    } 
    if (l < 100000000L) {
      if (l < 10000L) {
        if (l < 100L) {
          if (l < 10L) {
            i = 1;
          } else {
            i = 2;
          } 
        } else if (l < 1000L) {
          i = 3;
        } else {
          i = 4;
        } 
      } else if (l < 1000000L) {
        if (l < 100000L) {
          i = 5;
        } else {
          i = 6;
        } 
      } else if (l < 10000000L) {
        i = 7;
      } else {
        i = 8;
      } 
    } else if (l < 1000000000000L) {
      if (l < 10000000000L) {
        if (l < 1000000000L) {
          i = 9;
        } else {
          i = 10;
        } 
      } else if (l < 100000000000L) {
        i = 11;
      } else {
        i = 12;
      } 
    } else if (l < 1000000000000000L) {
      if (l < 10000000000000L) {
        i = 13;
      } else if (l < 100000000000000L) {
        i = 14;
      } else {
        i = 15;
      } 
    } else if (l < 100000000000000000L) {
      if (l < 10000000000000000L) {
        i = 16;
      } else {
        i = 17;
      } 
    } else if (l < 1000000000000000000L) {
      i = 18;
    } else {
      i = 19;
    } 
    int j = i;
    if (bool)
      j = i + 1; 
    Segment segment = writableSegment(j);
    byte[] arrayOfByte = segment.data;
    int i = segment.limit + j;
    while (l != 0L) {
      int k = (int)(l % 10L);
      arrayOfByte[--i] = (byte)DIGITS[k];
      l /= 10L;
    } 
    if (bool)
      arrayOfByte[i - 1] = (byte)45; 
    segment.limit += j;
    this.size += j;
    return this;
  }
  
  public Buffer writeHexadecimalUnsignedLong(long paramLong) {
    if (paramLong == 0L)
      return writeByte(48); 
    int i = Long.numberOfTrailingZeros(Long.highestOneBit(paramLong)) / 4 + 1;
    Segment segment = writableSegment(i);
    byte[] arrayOfByte = segment.data;
    int j = segment.limit + i - 1;
    int k = segment.limit;
    while (j >= k) {
      arrayOfByte[j] = (byte)DIGITS[(int)(0xFL & paramLong)];
      paramLong >>>= 4L;
      j--;
    } 
    segment.limit += i;
    this.size += i;
    return this;
  }
  
  public Buffer writeInt(int paramInt) {
    Segment segment = writableSegment(4);
    byte[] arrayOfByte = segment.data;
    int i = segment.limit;
    int j = i + 1;
    arrayOfByte[i] = (byte)(byte)(paramInt >>> 24 & 0xFF);
    i = j + 1;
    arrayOfByte[j] = (byte)(byte)(paramInt >>> 16 & 0xFF);
    j = i + 1;
    arrayOfByte[i] = (byte)(byte)(paramInt >>> 8 & 0xFF);
    arrayOfByte[j] = (byte)(byte)(paramInt & 0xFF);
    segment.limit = j + 1;
    this.size += 4L;
    return this;
  }
  
  public Buffer writeIntLe(int paramInt) {
    return writeInt(Util.reverseBytesInt(paramInt));
  }
  
  public Buffer writeLong(long paramLong) {
    Segment segment = writableSegment(8);
    byte[] arrayOfByte = segment.data;
    int i = segment.limit;
    int j = i + 1;
    arrayOfByte[i] = (byte)(byte)(int)(paramLong >>> 56L & 0xFFL);
    i = j + 1;
    arrayOfByte[j] = (byte)(byte)(int)(paramLong >>> 48L & 0xFFL);
    j = i + 1;
    arrayOfByte[i] = (byte)(byte)(int)(paramLong >>> 40L & 0xFFL);
    i = j + 1;
    arrayOfByte[j] = (byte)(byte)(int)(paramLong >>> 32L & 0xFFL);
    j = i + 1;
    arrayOfByte[i] = (byte)(byte)(int)(paramLong >>> 24L & 0xFFL);
    i = j + 1;
    arrayOfByte[j] = (byte)(byte)(int)(paramLong >>> 16L & 0xFFL);
    j = i + 1;
    arrayOfByte[i] = (byte)(byte)(int)(paramLong >>> 8L & 0xFFL);
    arrayOfByte[j] = (byte)(byte)(int)(paramLong & 0xFFL);
    segment.limit = j + 1;
    this.size += 8L;
    return this;
  }
  
  public Buffer writeLongLe(long paramLong) {
    return writeLong(Util.reverseBytesLong(paramLong));
  }
  
  public Buffer writeShort(int paramInt) {
    Segment segment = writableSegment(2);
    byte[] arrayOfByte = segment.data;
    int i = segment.limit;
    int j = i + 1;
    arrayOfByte[i] = (byte)(byte)(paramInt >>> 8 & 0xFF);
    arrayOfByte[j] = (byte)(byte)(paramInt & 0xFF);
    segment.limit = j + 1;
    this.size += 2L;
    return this;
  }
  
  public Buffer writeShortLe(int paramInt) {
    return writeShort(Util.reverseBytesShort((short)paramInt));
  }
  
  public Buffer writeString(String paramString, int paramInt1, int paramInt2, Charset paramCharset) {
    if (paramString == null)
      throw new IllegalArgumentException("string == null"); 
    if (paramInt1 < 0)
      throw new IllegalAccessError("beginIndex < 0: " + paramInt1); 
    if (paramInt2 < paramInt1)
      throw new IllegalArgumentException("endIndex < beginIndex: " + paramInt2 + " < " + paramInt1); 
    if (paramInt2 > paramString.length())
      throw new IllegalArgumentException("endIndex > string.length: " + paramInt2 + " > " + paramString.length()); 
    if (paramCharset == null)
      throw new IllegalArgumentException("charset == null"); 
    if (paramCharset.equals(Util.UTF_8))
      return writeUtf8(paramString, paramInt1, paramInt2); 
    byte[] arrayOfByte = paramString.substring(paramInt1, paramInt2).getBytes(paramCharset);
    return write(arrayOfByte, 0, arrayOfByte.length);
  }
  
  public Buffer writeString(String paramString, Charset paramCharset) {
    return writeString(paramString, 0, paramString.length(), paramCharset);
  }
  
  public Buffer writeTo(OutputStream paramOutputStream) throws IOException {
    return writeTo(paramOutputStream, this.size);
  }
  
  public Buffer writeTo(OutputStream paramOutputStream, long paramLong) throws IOException {
    if (paramOutputStream == null)
      throw new IllegalArgumentException("out == null"); 
    Util.checkOffsetAndCount(this.size, 0L, paramLong);
    Segment segment = this.head;
    while (true) {
      Segment segment1 = segment;
      if (paramLong > 0L) {
        int i = (int)Math.min(paramLong, (segment1.limit - segment1.pos));
        paramOutputStream.write(segment1.data, segment1.pos, i);
        segment1.pos += i;
        this.size -= i;
        long l = paramLong - i;
        segment = segment1;
        paramLong = l;
        if (segment1.pos == segment1.limit) {
          segment = segment1.pop();
          this.head = segment;
          SegmentPool.recycle(segment1);
          paramLong = l;
        } 
        continue;
      } 
      return this;
    } 
  }
  
  public Buffer writeUtf8(String paramString) {
    return writeUtf8(paramString, 0, paramString.length());
  }
  
  public Buffer writeUtf8(String paramString, int paramInt1, int paramInt2) {
    if (paramString == null)
      throw new IllegalArgumentException("string == null"); 
    if (paramInt1 < 0)
      throw new IllegalArgumentException("beginIndex < 0: " + paramInt1); 
    if (paramInt2 < paramInt1)
      throw new IllegalArgumentException("endIndex < beginIndex: " + paramInt2 + " < " + paramInt1); 
    if (paramInt2 > paramString.length())
      throw new IllegalArgumentException("endIndex > string.length: " + paramInt2 + " > " + paramString.length()); 
    label42: while (paramInt1 < paramInt2) {
      char c = paramString.charAt(paramInt1);
      if (c < '') {
        Segment segment = writableSegment(1);
        byte[] arrayOfByte = segment.data;
        int j = segment.limit - paramInt1;
        i = Math.min(paramInt2, 8192 - j);
        arrayOfByte[j + paramInt1] = (byte)(byte)c;
        paramInt1++;
        while (true) {
          if (paramInt1 < i) {
            c = paramString.charAt(paramInt1);
            if (c < '') {
              arrayOfByte[j + paramInt1] = (byte)(byte)c;
              paramInt1++;
              continue;
            } 
          } 
          i = paramInt1 + j - segment.limit;
          segment.limit += i;
          this.size += i;
          continue label42;
        } 
      } 
      if (c < 'ࠀ') {
        writeByte(c >> 6 | 0xC0);
        writeByte(c & 0x3F | 0x80);
        paramInt1++;
        continue;
      } 
      if (c < '?' || c > '?') {
        writeByte(c >> 12 | 0xE0);
        writeByte(c >> 6 & 0x3F | 0x80);
        writeByte(c & 0x3F | 0x80);
        paramInt1++;
        continue;
      } 
      if (paramInt1 + 1 < paramInt2) {
        i = paramString.charAt(paramInt1 + 1);
      } else {
        i = 0;
      } 
      if (c > '?' || i < 56320 || i > 57343) {
        writeByte(63);
        paramInt1++;
        continue;
      } 
      int i = 65536 + ((0xFFFF27FF & c) << 10 | 0xFFFF23FF & i);
      writeByte(i >> 18 | 0xF0);
      writeByte(i >> 12 & 0x3F | 0x80);
      writeByte(i >> 6 & 0x3F | 0x80);
      writeByte(i & 0x3F | 0x80);
      paramInt1 += 2;
    } 
    return this;
  }
  
  public Buffer writeUtf8CodePoint(int paramInt) {
    if (paramInt < 128) {
      writeByte(paramInt);
      return this;
    } 
    if (paramInt < 2048) {
      writeByte(paramInt >> 6 | 0xC0);
      writeByte(paramInt & 0x3F | 0x80);
      return this;
    } 
    if (paramInt < 65536) {
      if (paramInt >= 55296 && paramInt <= 57343) {
        writeByte(63);
        return this;
      } 
      writeByte(paramInt >> 12 | 0xE0);
      writeByte(paramInt >> 6 & 0x3F | 0x80);
      writeByte(paramInt & 0x3F | 0x80);
      return this;
    } 
    if (paramInt <= 1114111) {
      writeByte(paramInt >> 18 | 0xF0);
      writeByte(paramInt >> 12 & 0x3F | 0x80);
      writeByte(paramInt >> 6 & 0x3F | 0x80);
      writeByte(paramInt & 0x3F | 0x80);
      return this;
    } 
    throw new IllegalArgumentException("Unexpected code point: " + Integer.toHexString(paramInt));
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okio/Buffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */