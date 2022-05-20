package okhttp3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;

public final class MultipartBody extends RequestBody {
  public static final MediaType ALTERNATIVE;
  
  private static final byte[] COLONSPACE;
  
  private static final byte[] CRLF;
  
  private static final byte[] DASHDASH;
  
  public static final MediaType DIGEST;
  
  public static final MediaType FORM;
  
  public static final MediaType MIXED = MediaType.parse("multipart/mixed");
  
  public static final MediaType PARALLEL;
  
  private final ByteString boundary;
  
  private long contentLength = -1L;
  
  private final MediaType contentType;
  
  private final MediaType originalType;
  
  private final List<Part> parts;
  
  static {
    ALTERNATIVE = MediaType.parse("multipart/alternative");
    DIGEST = MediaType.parse("multipart/digest");
    PARALLEL = MediaType.parse("multipart/parallel");
    FORM = MediaType.parse("multipart/form-data");
    COLONSPACE = new byte[] { 58, 32 };
    CRLF = new byte[] { 13, 10 };
    DASHDASH = new byte[] { 45, 45 };
  }
  
  MultipartBody(ByteString paramByteString, MediaType paramMediaType, List<Part> paramList) {
    this.boundary = paramByteString;
    this.originalType = paramMediaType;
    this.contentType = MediaType.parse(paramMediaType + "; boundary=" + paramByteString.utf8());
    this.parts = Util.immutableList(paramList);
  }
  
  static StringBuilder appendQuotedString(StringBuilder paramStringBuilder, String paramString) {
    paramStringBuilder.append('"');
    byte b = 0;
    int i = paramString.length();
    while (b < i) {
      char c = paramString.charAt(b);
      switch (c) {
        case '\n':
          paramStringBuilder.append("%0A");
          b++;
          break;
        case '\r':
          paramStringBuilder.append("%0D");
          b++;
          break;
        case '"':
          paramStringBuilder.append("%22");
          b++;
          break;
      } 
    } 
    paramStringBuilder.append('"');
    return paramStringBuilder;
  }
  
  private long writeOrCountBytes(@Nullable BufferedSink paramBufferedSink, boolean paramBoolean) throws IOException {
    Buffer buffer1;
    long l1 = 0L;
    Buffer buffer2 = null;
    if (paramBoolean) {
      buffer2 = new Buffer();
      buffer1 = buffer2;
    } 
    byte b = 0;
    int i = this.parts.size();
    while (b < i) {
      Part part = this.parts.get(b);
      Headers headers = part.headers;
      RequestBody requestBody = part.body;
      buffer1.write(DASHDASH);
      buffer1.write(this.boundary);
      buffer1.write(CRLF);
      if (headers != null) {
        byte b1 = 0;
        int j = headers.size();
        while (b1 < j) {
          buffer1.writeUtf8(headers.name(b1)).write(COLONSPACE).writeUtf8(headers.value(b1)).write(CRLF);
          b1++;
        } 
      } 
      MediaType mediaType = requestBody.contentType();
      if (mediaType != null)
        buffer1.writeUtf8("Content-Type: ").writeUtf8(mediaType.toString()).write(CRLF); 
      long l = requestBody.contentLength();
      if (l != -1L) {
        buffer1.writeUtf8("Content-Length: ").writeDecimalLong(l).write(CRLF);
      } else if (paramBoolean) {
        buffer2.clear();
        return -1L;
      } 
      buffer1.write(CRLF);
      if (paramBoolean) {
        l1 += l;
      } else {
        requestBody.writeTo((BufferedSink)buffer1);
      } 
      buffer1.write(CRLF);
      b++;
    } 
    buffer1.write(DASHDASH);
    buffer1.write(this.boundary);
    buffer1.write(DASHDASH);
    buffer1.write(CRLF);
    long l2 = l1;
    if (paramBoolean) {
      l2 = l1 + buffer2.size();
      buffer2.clear();
    } 
    return l2;
  }
  
  public String boundary() {
    return this.boundary.utf8();
  }
  
  public long contentLength() throws IOException {
    long l = this.contentLength;
    if (l == -1L) {
      l = writeOrCountBytes(null, true);
      this.contentLength = l;
    } 
    return l;
  }
  
  public MediaType contentType() {
    return this.contentType;
  }
  
  public Part part(int paramInt) {
    return this.parts.get(paramInt);
  }
  
  public List<Part> parts() {
    return this.parts;
  }
  
  public int size() {
    return this.parts.size();
  }
  
  public MediaType type() {
    return this.originalType;
  }
  
  public void writeTo(BufferedSink paramBufferedSink) throws IOException {
    writeOrCountBytes(paramBufferedSink, false);
  }
  
  public static final class Builder {
    private final ByteString boundary;
    
    private final List<MultipartBody.Part> parts = new ArrayList<MultipartBody.Part>();
    
    private MediaType type = MultipartBody.MIXED;
    
    public Builder() {
      this(UUID.randomUUID().toString());
    }
    
    public Builder(String param1String) {
      this.boundary = ByteString.encodeUtf8(param1String);
    }
    
    public Builder addFormDataPart(String param1String1, String param1String2) {
      return addPart(MultipartBody.Part.createFormData(param1String1, param1String2));
    }
    
    public Builder addFormDataPart(String param1String1, @Nullable String param1String2, RequestBody param1RequestBody) {
      return addPart(MultipartBody.Part.createFormData(param1String1, param1String2, param1RequestBody));
    }
    
    public Builder addPart(@Nullable Headers param1Headers, RequestBody param1RequestBody) {
      return addPart(MultipartBody.Part.create(param1Headers, param1RequestBody));
    }
    
    public Builder addPart(MultipartBody.Part param1Part) {
      if (param1Part == null)
        throw new NullPointerException("part == null"); 
      this.parts.add(param1Part);
      return this;
    }
    
    public Builder addPart(RequestBody param1RequestBody) {
      return addPart(MultipartBody.Part.create(param1RequestBody));
    }
    
    public MultipartBody build() {
      if (this.parts.isEmpty())
        throw new IllegalStateException("Multipart body must have at least one part."); 
      return new MultipartBody(this.boundary, this.type, this.parts);
    }
    
    public Builder setType(MediaType param1MediaType) {
      if (param1MediaType == null)
        throw new NullPointerException("type == null"); 
      if (!param1MediaType.type().equals("multipart"))
        throw new IllegalArgumentException("multipart != " + param1MediaType); 
      this.type = param1MediaType;
      return this;
    }
  }
  
  public static final class Part {
    final RequestBody body;
    
    @Nullable
    final Headers headers;
    
    private Part(@Nullable Headers param1Headers, RequestBody param1RequestBody) {
      this.headers = param1Headers;
      this.body = param1RequestBody;
    }
    
    public static Part create(@Nullable Headers param1Headers, RequestBody param1RequestBody) {
      if (param1RequestBody == null)
        throw new NullPointerException("body == null"); 
      if (param1Headers != null && param1Headers.get("Content-Type") != null)
        throw new IllegalArgumentException("Unexpected header: Content-Type"); 
      if (param1Headers != null && param1Headers.get("Content-Length") != null)
        throw new IllegalArgumentException("Unexpected header: Content-Length"); 
      return new Part(param1Headers, param1RequestBody);
    }
    
    public static Part create(RequestBody param1RequestBody) {
      return create(null, param1RequestBody);
    }
    
    public static Part createFormData(String param1String1, String param1String2) {
      return createFormData(param1String1, null, RequestBody.create((MediaType)null, param1String2));
    }
    
    public static Part createFormData(String param1String1, @Nullable String param1String2, RequestBody param1RequestBody) {
      if (param1String1 == null)
        throw new NullPointerException("name == null"); 
      StringBuilder stringBuilder = new StringBuilder("form-data; name=");
      MultipartBody.appendQuotedString(stringBuilder, param1String1);
      if (param1String2 != null) {
        stringBuilder.append("; filename=");
        MultipartBody.appendQuotedString(stringBuilder, param1String2);
      } 
      return create(Headers.of(new String[] { "Content-Disposition", stringBuilder.toString() }, ), param1RequestBody);
    }
    
    public RequestBody body() {
      return this.body;
    }
    
    @Nullable
    public Headers headers() {
      return this.headers;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/MultipartBody.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */