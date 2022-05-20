package okio;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

final class SegmentedByteString extends ByteString {
  final transient int[] directory;
  
  final transient byte[][] segments;
  
  SegmentedByteString(Buffer paramBuffer, int paramInt) {
    super(null);
    Util.checkOffsetAndCount(paramBuffer.size, 0L, paramInt);
    int i = 0;
    int j = 0;
    for (Segment segment2 = paramBuffer.head; i < paramInt; segment2 = segment2.next) {
      if (segment2.limit == segment2.pos)
        throw new AssertionError("s.limit == s.pos"); 
      i += segment2.limit - segment2.pos;
      j++;
    } 
    this.segments = new byte[j][];
    this.directory = new int[j * 2];
    j = 0;
    i = 0;
    for (Segment segment1 = paramBuffer.head; j < paramInt; segment1 = segment1.next) {
      this.segments[i] = segment1.data;
      int k = j + segment1.limit - segment1.pos;
      j = k;
      if (k > paramInt)
        j = paramInt; 
      this.directory[i] = j;
      this.directory[this.segments.length + i] = segment1.pos;
      segment1.shared = true;
      i++;
    } 
  }
  
  private int segment(int paramInt) {
    paramInt = Arrays.binarySearch(this.directory, 0, this.segments.length, paramInt + 1);
    if (paramInt < 0)
      paramInt ^= 0xFFFFFFFF; 
    return paramInt;
  }
  
  private ByteString toByteString() {
    return new ByteString(toByteArray());
  }
  
  private Object writeReplace() {
    return toByteString();
  }
  
  public ByteBuffer asByteBuffer() {
    return ByteBuffer.wrap(toByteArray()).asReadOnlyBuffer();
  }
  
  public String base64() {
    return toByteString().base64();
  }
  
  public String base64Url() {
    return toByteString().base64Url();
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = true;
    if (paramObject != this) {
      if (paramObject instanceof ByteString && ((ByteString)paramObject).size() == size() && rangeEquals(0, (ByteString)paramObject, 0, size()))
        return true; 
      bool = false;
    } 
    return bool;
  }
  
  public byte getByte(int paramInt) {
    Util.checkOffsetAndCount(this.directory[this.segments.length - 1], paramInt, 1L);
    int i = segment(paramInt);
    if (i == 0) {
      byte b = 0;
      int m = this.directory[this.segments.length + i];
      return this.segments[i][paramInt - b + m];
    } 
    int j = this.directory[i - 1];
    int k = this.directory[this.segments.length + i];
    return this.segments[i][paramInt - j + k];
  }
  
  public int hashCode() {
    int i = this.hashCode;
    if (i == 0) {
      i = 1;
      int j = 0;
      byte b = 0;
      int k = this.segments.length;
      while (b < k) {
        byte[] arrayOfByte = this.segments[b];
        int m = this.directory[k + b];
        int n = this.directory[b];
        for (int i1 = m; i1 < m + n - j; i1++)
          i = i * 31 + arrayOfByte[i1]; 
        j = n;
        b++;
      } 
      this.hashCode = i;
    } 
    return i;
  }
  
  public String hex() {
    return toByteString().hex();
  }
  
  public ByteString hmacSha1(ByteString paramByteString) {
    return toByteString().hmacSha1(paramByteString);
  }
  
  public ByteString hmacSha256(ByteString paramByteString) {
    return toByteString().hmacSha256(paramByteString);
  }
  
  public int indexOf(byte[] paramArrayOfbyte, int paramInt) {
    return toByteString().indexOf(paramArrayOfbyte, paramInt);
  }
  
  byte[] internalArray() {
    return toByteArray();
  }
  
  public int lastIndexOf(byte[] paramArrayOfbyte, int paramInt) {
    return toByteString().lastIndexOf(paramArrayOfbyte, paramInt);
  }
  
  public ByteString md5() {
    return toByteString().md5();
  }
  
  public boolean rangeEquals(int paramInt1, ByteString paramByteString, int paramInt2, int paramInt3) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #5
    //   3: iload #5
    //   5: istore #6
    //   7: iload_1
    //   8: iflt -> 26
    //   11: iload_1
    //   12: aload_0
    //   13: invokevirtual size : ()I
    //   16: iload #4
    //   18: isub
    //   19: if_icmple -> 29
    //   22: iload #5
    //   24: istore #6
    //   26: iload #6
    //   28: ireturn
    //   29: aload_0
    //   30: iload_1
    //   31: invokespecial segment : (I)I
    //   34: istore #7
    //   36: iload #4
    //   38: ifle -> 151
    //   41: iload #7
    //   43: ifne -> 137
    //   46: iconst_0
    //   47: istore #8
    //   49: iload #4
    //   51: iload #8
    //   53: aload_0
    //   54: getfield directory : [I
    //   57: iload #7
    //   59: iaload
    //   60: iload #8
    //   62: isub
    //   63: iadd
    //   64: iload_1
    //   65: isub
    //   66: invokestatic min : (II)I
    //   69: istore #9
    //   71: aload_0
    //   72: getfield directory : [I
    //   75: aload_0
    //   76: getfield segments : [[B
    //   79: arraylength
    //   80: iload #7
    //   82: iadd
    //   83: iaload
    //   84: istore #10
    //   86: iload #5
    //   88: istore #6
    //   90: aload_2
    //   91: iload_3
    //   92: aload_0
    //   93: getfield segments : [[B
    //   96: iload #7
    //   98: aaload
    //   99: iload_1
    //   100: iload #8
    //   102: isub
    //   103: iload #10
    //   105: iadd
    //   106: iload #9
    //   108: invokevirtual rangeEquals : (I[BII)Z
    //   111: ifeq -> 26
    //   114: iload_1
    //   115: iload #9
    //   117: iadd
    //   118: istore_1
    //   119: iload_3
    //   120: iload #9
    //   122: iadd
    //   123: istore_3
    //   124: iload #4
    //   126: iload #9
    //   128: isub
    //   129: istore #4
    //   131: iinc #7, 1
    //   134: goto -> 36
    //   137: aload_0
    //   138: getfield directory : [I
    //   141: iload #7
    //   143: iconst_1
    //   144: isub
    //   145: iaload
    //   146: istore #8
    //   148: goto -> 49
    //   151: iconst_1
    //   152: istore #6
    //   154: goto -> 26
  }
  
  public boolean rangeEquals(int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #5
    //   3: iload #5
    //   5: istore #6
    //   7: iload_1
    //   8: iflt -> 47
    //   11: iload #5
    //   13: istore #6
    //   15: iload_1
    //   16: aload_0
    //   17: invokevirtual size : ()I
    //   20: iload #4
    //   22: isub
    //   23: if_icmpgt -> 47
    //   26: iload #5
    //   28: istore #6
    //   30: iload_3
    //   31: iflt -> 47
    //   34: iload_3
    //   35: aload_2
    //   36: arraylength
    //   37: iload #4
    //   39: isub
    //   40: if_icmple -> 50
    //   43: iload #5
    //   45: istore #6
    //   47: iload #6
    //   49: ireturn
    //   50: aload_0
    //   51: iload_1
    //   52: invokespecial segment : (I)I
    //   55: istore #7
    //   57: iload_1
    //   58: istore #8
    //   60: iload #7
    //   62: istore_1
    //   63: iload #4
    //   65: ifle -> 177
    //   68: iload_1
    //   69: ifne -> 164
    //   72: iconst_0
    //   73: istore #7
    //   75: iload #4
    //   77: iload #7
    //   79: aload_0
    //   80: getfield directory : [I
    //   83: iload_1
    //   84: iaload
    //   85: iload #7
    //   87: isub
    //   88: iadd
    //   89: iload #8
    //   91: isub
    //   92: invokestatic min : (II)I
    //   95: istore #9
    //   97: aload_0
    //   98: getfield directory : [I
    //   101: aload_0
    //   102: getfield segments : [[B
    //   105: arraylength
    //   106: iload_1
    //   107: iadd
    //   108: iaload
    //   109: istore #10
    //   111: iload #5
    //   113: istore #6
    //   115: aload_0
    //   116: getfield segments : [[B
    //   119: iload_1
    //   120: aaload
    //   121: iload #8
    //   123: iload #7
    //   125: isub
    //   126: iload #10
    //   128: iadd
    //   129: aload_2
    //   130: iload_3
    //   131: iload #9
    //   133: invokestatic arrayRangeEquals : ([BI[BII)Z
    //   136: ifeq -> 47
    //   139: iload #8
    //   141: iload #9
    //   143: iadd
    //   144: istore #8
    //   146: iload_3
    //   147: iload #9
    //   149: iadd
    //   150: istore_3
    //   151: iload #4
    //   153: iload #9
    //   155: isub
    //   156: istore #4
    //   158: iinc #1, 1
    //   161: goto -> 63
    //   164: aload_0
    //   165: getfield directory : [I
    //   168: iload_1
    //   169: iconst_1
    //   170: isub
    //   171: iaload
    //   172: istore #7
    //   174: goto -> 75
    //   177: iconst_1
    //   178: istore #6
    //   180: goto -> 47
  }
  
  public ByteString sha1() {
    return toByteString().sha1();
  }
  
  public ByteString sha256() {
    return toByteString().sha256();
  }
  
  public int size() {
    return this.directory[this.segments.length - 1];
  }
  
  public String string(Charset paramCharset) {
    return toByteString().string(paramCharset);
  }
  
  public ByteString substring(int paramInt) {
    return toByteString().substring(paramInt);
  }
  
  public ByteString substring(int paramInt1, int paramInt2) {
    return toByteString().substring(paramInt1, paramInt2);
  }
  
  public ByteString toAsciiLowercase() {
    return toByteString().toAsciiLowercase();
  }
  
  public ByteString toAsciiUppercase() {
    return toByteString().toAsciiUppercase();
  }
  
  public byte[] toByteArray() {
    byte[] arrayOfByte = new byte[this.directory[this.segments.length - 1]];
    int i = 0;
    byte b = 0;
    int j = this.segments.length;
    while (b < j) {
      int k = this.directory[j + b];
      int m = this.directory[b];
      System.arraycopy(this.segments[b], k, arrayOfByte, i, m - i);
      i = m;
      b++;
    } 
    return arrayOfByte;
  }
  
  public String toString() {
    return toByteString().toString();
  }
  
  public String utf8() {
    return toByteString().utf8();
  }
  
  public void write(OutputStream paramOutputStream) throws IOException {
    if (paramOutputStream == null)
      throw new IllegalArgumentException("out == null"); 
    int i = 0;
    byte b = 0;
    int j = this.segments.length;
    while (b < j) {
      int k = this.directory[j + b];
      int m = this.directory[b];
      paramOutputStream.write(this.segments[b], k, m - i);
      i = m;
      b++;
    } 
  }
  
  void write(Buffer paramBuffer) {
    int i = 0;
    byte b = 0;
    int j = this.segments.length;
    while (b < j) {
      int k = this.directory[j + b];
      int m = this.directory[b];
      Segment segment = new Segment(this.segments[b], k, k + m - i);
      if (paramBuffer.head == null) {
        segment.prev = segment;
        segment.next = segment;
        paramBuffer.head = segment;
      } else {
        paramBuffer.head.prev.push(segment);
      } 
      i = m;
      b++;
    } 
    paramBuffer.size += i;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okio/SegmentedByteString.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */