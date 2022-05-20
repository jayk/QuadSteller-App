package okio;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.annotation.Nullable;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class HashingSink extends ForwardingSink {
  @Nullable
  private final Mac mac;
  
  @Nullable
  private final MessageDigest messageDigest;
  
  private HashingSink(Sink paramSink, String paramString) {
    super(paramSink);
    try {
      this.messageDigest = MessageDigest.getInstance(paramString);
      this.mac = null;
      return;
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      throw new AssertionError();
    } 
  }
  
  private HashingSink(Sink paramSink, ByteString paramByteString, String paramString) {
    super(paramSink);
    try {
      this.mac = Mac.getInstance(paramString);
      Mac mac = this.mac;
      SecretKeySpec secretKeySpec = new SecretKeySpec();
      this(paramByteString.toByteArray(), paramString);
      mac.init(secretKeySpec);
      this.messageDigest = null;
      return;
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      throw new AssertionError();
    } catch (InvalidKeyException invalidKeyException) {
      throw new IllegalArgumentException(invalidKeyException);
    } 
  }
  
  public static HashingSink hmacSha1(Sink paramSink, ByteString paramByteString) {
    return new HashingSink(paramSink, paramByteString, "HmacSHA1");
  }
  
  public static HashingSink hmacSha256(Sink paramSink, ByteString paramByteString) {
    return new HashingSink(paramSink, paramByteString, "HmacSHA256");
  }
  
  public static HashingSink hmacSha512(Sink paramSink, ByteString paramByteString) {
    return new HashingSink(paramSink, paramByteString, "HmacSHA512");
  }
  
  public static HashingSink md5(Sink paramSink) {
    return new HashingSink(paramSink, "MD5");
  }
  
  public static HashingSink sha1(Sink paramSink) {
    return new HashingSink(paramSink, "SHA-1");
  }
  
  public static HashingSink sha256(Sink paramSink) {
    return new HashingSink(paramSink, "SHA-256");
  }
  
  public static HashingSink sha512(Sink paramSink) {
    return new HashingSink(paramSink, "SHA-512");
  }
  
  public ByteString hash() {
    if (this.messageDigest != null) {
      byte[] arrayOfByte1 = this.messageDigest.digest();
      return ByteString.of(arrayOfByte1);
    } 
    byte[] arrayOfByte = this.mac.doFinal();
    return ByteString.of(arrayOfByte);
  }
  
  public void write(Buffer paramBuffer, long paramLong) throws IOException {
    Util.checkOffsetAndCount(paramBuffer.size, 0L, paramLong);
    long l = 0L;
    for (Segment segment = paramBuffer.head; l < paramLong; segment = segment.next) {
      int i = (int)Math.min(paramLong - l, (segment.limit - segment.pos));
      if (this.messageDigest != null) {
        this.messageDigest.update(segment.data, segment.pos, i);
      } else {
        this.mac.update(segment.data, segment.pos, i);
      } 
      l += i;
    } 
    super.write(paramBuffer, paramLong);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okio/HashingSink.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */