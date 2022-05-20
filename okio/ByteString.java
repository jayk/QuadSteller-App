package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.annotation.Nullable;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class ByteString implements Serializable, Comparable<ByteString> {
  public static final ByteString EMPTY;
  
  static final char[] HEX_DIGITS = new char[] { 
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
      'a', 'b', 'c', 'd', 'e', 'f' };
  
  private static final long serialVersionUID = 1L;
  
  final byte[] data;
  
  transient int hashCode;
  
  transient String utf8;
  
  static {
    EMPTY = of(new byte[0]);
  }
  
  ByteString(byte[] paramArrayOfbyte) {
    this.data = paramArrayOfbyte;
  }
  
  static int codePointIndexToCharIndex(String paramString, int paramInt) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: iconst_0
    //   3: istore_3
    //   4: aload_0
    //   5: invokevirtual length : ()I
    //   8: istore #4
    //   10: iload_2
    //   11: iload #4
    //   13: if_icmpge -> 78
    //   16: iload_3
    //   17: iload_1
    //   18: if_icmpne -> 23
    //   21: iload_2
    //   22: ireturn
    //   23: aload_0
    //   24: iload_2
    //   25: invokevirtual codePointAt : (I)I
    //   28: istore #5
    //   30: iload #5
    //   32: invokestatic isISOControl : (I)Z
    //   35: ifeq -> 52
    //   38: iload #5
    //   40: bipush #10
    //   42: if_icmpeq -> 52
    //   45: iload #5
    //   47: bipush #13
    //   49: if_icmpne -> 59
    //   52: iload #5
    //   54: ldc 65533
    //   56: if_icmpne -> 64
    //   59: iconst_m1
    //   60: istore_2
    //   61: goto -> 21
    //   64: iinc #3, 1
    //   67: iload_2
    //   68: iload #5
    //   70: invokestatic charCount : (I)I
    //   73: iadd
    //   74: istore_2
    //   75: goto -> 10
    //   78: aload_0
    //   79: invokevirtual length : ()I
    //   82: istore_2
    //   83: goto -> 21
  }
  
  @Nullable
  public static ByteString decodeBase64(String paramString) {
    if (paramString == null)
      throw new IllegalArgumentException("base64 == null"); 
    null = Base64.decode(paramString);
    return (null != null) ? new ByteString(null) : null;
  }
  
  public static ByteString decodeHex(String paramString) {
    if (paramString == null)
      throw new IllegalArgumentException("hex == null"); 
    if (paramString.length() % 2 != 0)
      throw new IllegalArgumentException("Unexpected hex string: " + paramString); 
    byte[] arrayOfByte = new byte[paramString.length() / 2];
    for (byte b = 0; b < arrayOfByte.length; b++)
      arrayOfByte[b] = (byte)(byte)((decodeHexDigit(paramString.charAt(b * 2)) << 4) + decodeHexDigit(paramString.charAt(b * 2 + 1))); 
    return of(arrayOfByte);
  }
  
  private static int decodeHexDigit(char paramChar) {
    if (paramChar >= '0' && paramChar <= '9')
      return paramChar - 48; 
    if (paramChar >= 'a' && paramChar <= 'f')
      return paramChar - 97 + 10; 
    if (paramChar >= 'A' && paramChar <= 'F')
      return paramChar - 65 + 10; 
    throw new IllegalArgumentException("Unexpected hex digit: " + paramChar);
  }
  
  private ByteString digest(String paramString) {
    try {
      return of(MessageDigest.getInstance(paramString).digest(this.data));
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      throw new AssertionError(noSuchAlgorithmException);
    } 
  }
  
  public static ByteString encodeString(String paramString, Charset paramCharset) {
    if (paramString == null)
      throw new IllegalArgumentException("s == null"); 
    if (paramCharset == null)
      throw new IllegalArgumentException("charset == null"); 
    return new ByteString(paramString.getBytes(paramCharset));
  }
  
  public static ByteString encodeUtf8(String paramString) {
    if (paramString == null)
      throw new IllegalArgumentException("s == null"); 
    ByteString byteString = new ByteString(paramString.getBytes(Util.UTF_8));
    byteString.utf8 = paramString;
    return byteString;
  }
  
  private ByteString hmac(String paramString, ByteString paramByteString) {
    try {
      Mac mac = Mac.getInstance(paramString);
      SecretKeySpec secretKeySpec = new SecretKeySpec();
      this(paramByteString.toByteArray(), paramString);
      mac.init(secretKeySpec);
      return of(mac.doFinal(this.data));
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      throw new AssertionError(noSuchAlgorithmException);
    } catch (InvalidKeyException invalidKeyException) {
      throw new IllegalArgumentException(invalidKeyException);
    } 
  }
  
  public static ByteString of(ByteBuffer paramByteBuffer) {
    if (paramByteBuffer == null)
      throw new IllegalArgumentException("data == null"); 
    byte[] arrayOfByte = new byte[paramByteBuffer.remaining()];
    paramByteBuffer.get(arrayOfByte);
    return new ByteString(arrayOfByte);
  }
  
  public static ByteString of(byte... paramVarArgs) {
    if (paramVarArgs == null)
      throw new IllegalArgumentException("data == null"); 
    return new ByteString((byte[])paramVarArgs.clone());
  }
  
  public static ByteString of(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    if (paramArrayOfbyte == null)
      throw new IllegalArgumentException("data == null"); 
    Util.checkOffsetAndCount(paramArrayOfbyte.length, paramInt1, paramInt2);
    byte[] arrayOfByte = new byte[paramInt2];
    System.arraycopy(paramArrayOfbyte, paramInt1, arrayOfByte, 0, paramInt2);
    return new ByteString(arrayOfByte);
  }
  
  public static ByteString read(InputStream paramInputStream, int paramInt) throws IOException {
    if (paramInputStream == null)
      throw new IllegalArgumentException("in == null"); 
    if (paramInt < 0)
      throw new IllegalArgumentException("byteCount < 0: " + paramInt); 
    byte[] arrayOfByte = new byte[paramInt];
    for (int i = 0; i < paramInt; i += j) {
      int j = paramInputStream.read(arrayOfByte, i, paramInt - i);
      if (j == -1)
        throw new EOFException(); 
    } 
    return new ByteString(arrayOfByte);
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
    ByteString byteString = read(paramObjectInputStream, paramObjectInputStream.readInt());
    try {
      Field field = ByteString.class.getDeclaredField("data");
      field.setAccessible(true);
      field.set(this, byteString.data);
      return;
    } catch (NoSuchFieldException noSuchFieldException) {
      throw new AssertionError();
    } catch (IllegalAccessException illegalAccessException) {
      throw new AssertionError();
    } 
  }
  
  private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
    paramObjectOutputStream.writeInt(this.data.length);
    paramObjectOutputStream.write(this.data);
  }
  
  public ByteBuffer asByteBuffer() {
    return ByteBuffer.wrap(this.data).asReadOnlyBuffer();
  }
  
  public String base64() {
    return Base64.encode(this.data);
  }
  
  public String base64Url() {
    return Base64.encodeUrl(this.data);
  }
  
  public int compareTo(ByteString paramByteString) {
    byte b1 = -1;
    int i = size();
    int j = paramByteString.size();
    byte b2 = 0;
    int k = Math.min(i, j);
    while (b2 < k) {
      int m = getByte(b2) & 0xFF;
      int n = paramByteString.getByte(b2) & 0xFF;
      if (m == n) {
        b2++;
        continue;
      } 
      return (m < n) ? b1 : 1;
    } 
    if (i == j)
      return 0; 
    b2 = b1;
    if (i >= j)
      b2 = 1; 
    return b2;
  }
  
  public final boolean endsWith(ByteString paramByteString) {
    return rangeEquals(size() - paramByteString.size(), paramByteString, 0, paramByteString.size());
  }
  
  public final boolean endsWith(byte[] paramArrayOfbyte) {
    return rangeEquals(size() - paramArrayOfbyte.length, paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = true;
    if (paramObject != this) {
      if (paramObject instanceof ByteString && ((ByteString)paramObject).size() == this.data.length && ((ByteString)paramObject).rangeEquals(0, this.data, 0, this.data.length))
        return true; 
      bool = false;
    } 
    return bool;
  }
  
  public byte getByte(int paramInt) {
    return this.data[paramInt];
  }
  
  public int hashCode() {
    int i = this.hashCode;
    if (i == 0) {
      i = Arrays.hashCode(this.data);
      this.hashCode = i;
    } 
    return i;
  }
  
  public String hex() {
    char[] arrayOfChar = new char[this.data.length * 2];
    byte[] arrayOfByte = this.data;
    int i = arrayOfByte.length;
    byte b = 0;
    int j = 0;
    while (b < i) {
      byte b1 = arrayOfByte[b];
      int k = j + 1;
      arrayOfChar[j] = (char)HEX_DIGITS[b1 >> 4 & 0xF];
      j = k + 1;
      arrayOfChar[k] = (char)HEX_DIGITS[b1 & 0xF];
      b++;
    } 
    return new String(arrayOfChar);
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
  
  public final int indexOf(ByteString paramByteString) {
    return indexOf(paramByteString.internalArray(), 0);
  }
  
  public final int indexOf(ByteString paramByteString, int paramInt) {
    return indexOf(paramByteString.internalArray(), paramInt);
  }
  
  public final int indexOf(byte[] paramArrayOfbyte) {
    return indexOf(paramArrayOfbyte, 0);
  }
  
  public int indexOf(byte[] paramArrayOfbyte, int paramInt) {
    // Byte code:
    //   0: iload_2
    //   1: iconst_0
    //   2: invokestatic max : (II)I
    //   5: istore_2
    //   6: aload_0
    //   7: getfield data : [B
    //   10: arraylength
    //   11: istore_3
    //   12: aload_1
    //   13: arraylength
    //   14: istore #4
    //   16: iload_2
    //   17: iload_3
    //   18: iload #4
    //   20: isub
    //   21: if_icmpgt -> 47
    //   24: aload_0
    //   25: getfield data : [B
    //   28: iload_2
    //   29: aload_1
    //   30: iconst_0
    //   31: aload_1
    //   32: arraylength
    //   33: invokestatic arrayRangeEquals : ([BI[BII)Z
    //   36: ifeq -> 41
    //   39: iload_2
    //   40: ireturn
    //   41: iinc #2, 1
    //   44: goto -> 16
    //   47: iconst_m1
    //   48: istore_2
    //   49: goto -> 39
  }
  
  byte[] internalArray() {
    return this.data;
  }
  
  public final int lastIndexOf(ByteString paramByteString) {
    return lastIndexOf(paramByteString.internalArray(), size());
  }
  
  public final int lastIndexOf(ByteString paramByteString, int paramInt) {
    return lastIndexOf(paramByteString.internalArray(), paramInt);
  }
  
  public final int lastIndexOf(byte[] paramArrayOfbyte) {
    return lastIndexOf(paramArrayOfbyte, size());
  }
  
  public int lastIndexOf(byte[] paramArrayOfbyte, int paramInt) {
    // Byte code:
    //   0: iload_2
    //   1: aload_0
    //   2: getfield data : [B
    //   5: arraylength
    //   6: aload_1
    //   7: arraylength
    //   8: isub
    //   9: invokestatic min : (II)I
    //   12: istore_2
    //   13: iload_2
    //   14: iflt -> 40
    //   17: aload_0
    //   18: getfield data : [B
    //   21: iload_2
    //   22: aload_1
    //   23: iconst_0
    //   24: aload_1
    //   25: arraylength
    //   26: invokestatic arrayRangeEquals : ([BI[BII)Z
    //   29: ifeq -> 34
    //   32: iload_2
    //   33: ireturn
    //   34: iinc #2, -1
    //   37: goto -> 13
    //   40: iconst_m1
    //   41: istore_2
    //   42: goto -> 32
  }
  
  public ByteString md5() {
    return digest("MD5");
  }
  
  public boolean rangeEquals(int paramInt1, ByteString paramByteString, int paramInt2, int paramInt3) {
    return paramByteString.rangeEquals(paramInt2, this.data, paramInt1, paramInt3);
  }
  
  public boolean rangeEquals(int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3) {
    return (paramInt1 >= 0 && paramInt1 <= this.data.length - paramInt3 && paramInt2 >= 0 && paramInt2 <= paramArrayOfbyte.length - paramInt3 && Util.arrayRangeEquals(this.data, paramInt1, paramArrayOfbyte, paramInt2, paramInt3));
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
  
  public int size() {
    return this.data.length;
  }
  
  public final boolean startsWith(ByteString paramByteString) {
    return rangeEquals(0, paramByteString, 0, paramByteString.size());
  }
  
  public final boolean startsWith(byte[] paramArrayOfbyte) {
    return rangeEquals(0, paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public String string(Charset paramCharset) {
    if (paramCharset == null)
      throw new IllegalArgumentException("charset == null"); 
    return new String(this.data, paramCharset);
  }
  
  public ByteString substring(int paramInt) {
    return substring(paramInt, this.data.length);
  }
  
  public ByteString substring(int paramInt1, int paramInt2) {
    if (paramInt1 < 0)
      throw new IllegalArgumentException("beginIndex < 0"); 
    if (paramInt2 > this.data.length)
      throw new IllegalArgumentException("endIndex > length(" + this.data.length + ")"); 
    int i = paramInt2 - paramInt1;
    if (i < 0)
      throw new IllegalArgumentException("endIndex < beginIndex"); 
    if (paramInt1 == 0 && paramInt2 == this.data.length)
      return this; 
    byte[] arrayOfByte = new byte[i];
    System.arraycopy(this.data, paramInt1, arrayOfByte, 0, i);
    return new ByteString(arrayOfByte);
  }
  
  public ByteString toAsciiLowercase() {
    ByteString byteString;
    byte b = 0;
    while (true) {
      byteString = this;
      if (b < this.data.length) {
        byte b1 = this.data[b];
        if (b1 < 65 || b1 > 90) {
          b++;
          continue;
        } 
        byte[] arrayOfByte = (byte[])this.data.clone();
        arrayOfByte[b] = (byte)(byte)(b1 + 32);
        while (++b < arrayOfByte.length) {
          b1 = arrayOfByte[b];
          if (b1 >= 65 && b1 <= 90)
            arrayOfByte[b] = (byte)(byte)(b1 + 32); 
          b++;
        } 
        byteString = new ByteString(arrayOfByte);
      } 
      break;
    } 
    return byteString;
  }
  
  public ByteString toAsciiUppercase() {
    ByteString byteString;
    byte b = 0;
    while (true) {
      byteString = this;
      if (b < this.data.length) {
        byte b1 = this.data[b];
        if (b1 < 97 || b1 > 122) {
          b++;
          continue;
        } 
        byte[] arrayOfByte = (byte[])this.data.clone();
        arrayOfByte[b] = (byte)(byte)(b1 - 32);
        while (++b < arrayOfByte.length) {
          b1 = arrayOfByte[b];
          if (b1 >= 97 && b1 <= 122)
            arrayOfByte[b] = (byte)(byte)(b1 - 32); 
          b++;
        } 
        byteString = new ByteString(arrayOfByte);
      } 
      break;
    } 
    return byteString;
  }
  
  public byte[] toByteArray() {
    return (byte[])this.data.clone();
  }
  
  public String toString() {
    if (this.data.length == 0)
      return "[size=0]"; 
    String str = utf8();
    int i = codePointIndexToCharIndex(str, 64);
    if (i == -1)
      return (this.data.length <= 64) ? ("[hex=" + hex() + "]") : ("[size=" + this.data.length + " hex=" + substring(0, 64).hex() + "…]"); 
    null = str.substring(0, i).replace("\\", "\\\\").replace("\n", "\\n").replace("\r", "\\r");
    return (i < str.length()) ? ("[size=" + this.data.length + " text=" + null + "…]") : ("[text=" + null + "]");
  }
  
  public String utf8() {
    String str = this.utf8;
    if (str == null) {
      str = new String(this.data, Util.UTF_8);
      this.utf8 = str;
    } 
    return str;
  }
  
  public void write(OutputStream paramOutputStream) throws IOException {
    if (paramOutputStream == null)
      throw new IllegalArgumentException("out == null"); 
    paramOutputStream.write(this.data);
  }
  
  void write(Buffer paramBuffer) {
    paramBuffer.write(this.data, 0, this.data.length);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okio/ByteString.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */