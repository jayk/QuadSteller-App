package okhttp3;

import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.cache.CacheRequest;
import okhttp3.internal.cache.CacheStrategy;
import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.cache.InternalCache;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.http.StatusLine;
import okhttp3.internal.io.FileSystem;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.ForwardingSink;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

public final class Cache implements Closeable, Flushable {
  private static final int ENTRY_BODY = 1;
  
  private static final int ENTRY_COUNT = 2;
  
  private static final int ENTRY_METADATA = 0;
  
  private static final int VERSION = 201105;
  
  final DiskLruCache cache;
  
  private int hitCount;
  
  final InternalCache internalCache = new InternalCache() {
      public Response get(Request param1Request) throws IOException {
        return Cache.this.get(param1Request);
      }
      
      public CacheRequest put(Response param1Response) throws IOException {
        return Cache.this.put(param1Response);
      }
      
      public void remove(Request param1Request) throws IOException {
        Cache.this.remove(param1Request);
      }
      
      public void trackConditionalCacheHit() {
        Cache.this.trackConditionalCacheHit();
      }
      
      public void trackResponse(CacheStrategy param1CacheStrategy) {
        Cache.this.trackResponse(param1CacheStrategy);
      }
      
      public void update(Response param1Response1, Response param1Response2) {
        Cache.this.update(param1Response1, param1Response2);
      }
    };
  
  private int networkCount;
  
  private int requestCount;
  
  int writeAbortCount;
  
  int writeSuccessCount;
  
  public Cache(File paramFile, long paramLong) {
    this(paramFile, paramLong, FileSystem.SYSTEM);
  }
  
  Cache(File paramFile, long paramLong, FileSystem paramFileSystem) {
    this.cache = DiskLruCache.create(paramFileSystem, paramFile, 201105, 2, paramLong);
  }
  
  private void abortQuietly(@Nullable DiskLruCache.Editor paramEditor) {
    if (paramEditor != null)
      try {
        paramEditor.abort();
      } catch (IOException iOException) {} 
  }
  
  public static String key(HttpUrl paramHttpUrl) {
    return ByteString.encodeUtf8(paramHttpUrl.toString()).md5().hex();
  }
  
  static int readInt(BufferedSource paramBufferedSource) throws IOException {
    long l;
    try {
      l = paramBufferedSource.readDecimalLong();
      String str = paramBufferedSource.readUtf8LineStrict();
      if (l < 0L || l > 2147483647L || !str.isEmpty()) {
        IOException iOException = new IOException();
        StringBuilder stringBuilder = new StringBuilder();
        this();
        this(stringBuilder.append("expected an int but was \"").append(l).append(str).append("\"").toString());
        throw iOException;
      } 
    } catch (NumberFormatException numberFormatException) {
      throw new IOException(numberFormatException.getMessage());
    } 
    return (int)l;
  }
  
  public void close() throws IOException {
    this.cache.close();
  }
  
  public void delete() throws IOException {
    this.cache.delete();
  }
  
  public File directory() {
    return this.cache.getDirectory();
  }
  
  public void evictAll() throws IOException {
    this.cache.evictAll();
  }
  
  public void flush() throws IOException {
    this.cache.flush();
  }
  
  @Nullable
  Response get(Request paramRequest) {
    Response response;
    String str = key(paramRequest.url());
    try {
      DiskLruCache.Snapshot snapshot = this.cache.get(str);
      if (snapshot == null)
        return null; 
    } catch (IOException iOException) {
      return null;
    } 
    try {
      Entry entry = new Entry();
      this(str.getSource(0));
      Response response1 = entry.response((DiskLruCache.Snapshot)str);
      response = response1;
      if (!entry.matches((Request)iOException, response1)) {
        Util.closeQuietly(response1.body());
        response = null;
      } 
    } catch (IOException iOException1) {
      Util.closeQuietly(response);
      response = null;
    } 
    return response;
  }
  
  public int hitCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield hitCount : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public void initialize() throws IOException {
    this.cache.initialize();
  }
  
  public boolean isClosed() {
    return this.cache.isClosed();
  }
  
  public long maxSize() {
    return this.cache.getMaxSize();
  }
  
  public int networkCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield networkCount : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  @Nullable
  CacheRequest put(Response paramResponse) {
    CacheRequest cacheRequest1 = null;
    String str = paramResponse.request().method();
    if (HttpMethod.invalidatesCache(paramResponse.request().method())) {
      CacheRequest cacheRequest;
      try {
        remove(paramResponse.request());
        cacheRequest = cacheRequest1;
      } catch (IOException iOException) {
        cacheRequest = cacheRequest1;
      } 
      return cacheRequest;
    } 
    CacheRequest cacheRequest2 = cacheRequest1;
    if (str.equals("GET")) {
      cacheRequest2 = cacheRequest1;
      if (!HttpHeaders.hasVaryAll((Response)iOException)) {
        CacheRequestImpl cacheRequestImpl;
        Entry entry = new Entry((Response)iOException);
        cacheRequest2 = null;
        try {
          DiskLruCache.Editor editor = this.cache.edit(key(iOException.request().url()));
          cacheRequest2 = cacheRequest1;
          if (editor != null) {
            DiskLruCache.Editor editor1 = editor;
            entry.writeTo(editor);
            editor1 = editor;
            CacheRequestImpl cacheRequestImpl1 = new CacheRequestImpl();
            editor1 = editor;
            this(this, editor);
            cacheRequestImpl = cacheRequestImpl1;
          } 
        } catch (IOException iOException1) {
          abortQuietly((DiskLruCache.Editor)cacheRequestImpl);
          cacheRequest2 = cacheRequest1;
        } 
      } 
    } 
    return cacheRequest2;
  }
  
  void remove(Request paramRequest) throws IOException {
    this.cache.remove(key(paramRequest.url()));
  }
  
  public int requestCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield requestCount : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public long size() throws IOException {
    return this.cache.size();
  }
  
  void trackConditionalCacheHit() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield hitCount : I
    //   7: iconst_1
    //   8: iadd
    //   9: putfield hitCount : I
    //   12: aload_0
    //   13: monitorexit
    //   14: return
    //   15: astore_1
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_1
    //   19: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	15	finally
  }
  
  void trackResponse(CacheStrategy paramCacheStrategy) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield requestCount : I
    //   7: iconst_1
    //   8: iadd
    //   9: putfield requestCount : I
    //   12: aload_1
    //   13: getfield networkRequest : Lokhttp3/Request;
    //   16: ifnull -> 32
    //   19: aload_0
    //   20: aload_0
    //   21: getfield networkCount : I
    //   24: iconst_1
    //   25: iadd
    //   26: putfield networkCount : I
    //   29: aload_0
    //   30: monitorexit
    //   31: return
    //   32: aload_1
    //   33: getfield cacheResponse : Lokhttp3/Response;
    //   36: ifnull -> 29
    //   39: aload_0
    //   40: aload_0
    //   41: getfield hitCount : I
    //   44: iconst_1
    //   45: iadd
    //   46: putfield hitCount : I
    //   49: goto -> 29
    //   52: astore_1
    //   53: aload_0
    //   54: monitorexit
    //   55: aload_1
    //   56: athrow
    // Exception table:
    //   from	to	target	type
    //   2	29	52	finally
    //   32	49	52	finally
  }
  
  void update(Response paramResponse1, Response paramResponse2) {
    DiskLruCache.Editor editor;
    Entry entry = new Entry(paramResponse2);
    DiskLruCache.Snapshot snapshot = ((CacheResponseBody)paramResponse1.body()).snapshot;
    paramResponse1 = null;
    try {
      DiskLruCache.Editor editor1 = snapshot.edit();
      if (editor1 != null) {
        editor = editor1;
        entry.writeTo(editor1);
        editor = editor1;
        editor1.commit();
      } 
    } catch (IOException iOException) {
      abortQuietly(editor);
    } 
  }
  
  public Iterator<String> urls() throws IOException {
    return new Iterator<String>() {
        boolean canRemove;
        
        final Iterator<DiskLruCache.Snapshot> delegate = Cache.this.cache.snapshots();
        
        @Nullable
        String nextUrl;
        
        public boolean hasNext() {
          // Byte code:
          //   0: iconst_1
          //   1: istore_1
          //   2: aload_0
          //   3: getfield nextUrl : Ljava/lang/String;
          //   6: ifnull -> 11
          //   9: iload_1
          //   10: ireturn
          //   11: aload_0
          //   12: iconst_0
          //   13: putfield canRemove : Z
          //   16: aload_0
          //   17: getfield delegate : Ljava/util/Iterator;
          //   20: invokeinterface hasNext : ()Z
          //   25: ifeq -> 80
          //   28: aload_0
          //   29: getfield delegate : Ljava/util/Iterator;
          //   32: invokeinterface next : ()Ljava/lang/Object;
          //   37: checkcast okhttp3/internal/cache/DiskLruCache$Snapshot
          //   40: astore_2
          //   41: aload_0
          //   42: aload_2
          //   43: iconst_0
          //   44: invokevirtual getSource : (I)Lokio/Source;
          //   47: invokestatic buffer : (Lokio/Source;)Lokio/BufferedSource;
          //   50: invokeinterface readUtf8LineStrict : ()Ljava/lang/String;
          //   55: putfield nextUrl : Ljava/lang/String;
          //   58: aload_2
          //   59: invokevirtual close : ()V
          //   62: goto -> 9
          //   65: astore_3
          //   66: aload_2
          //   67: invokevirtual close : ()V
          //   70: goto -> 16
          //   73: astore_3
          //   74: aload_2
          //   75: invokevirtual close : ()V
          //   78: aload_3
          //   79: athrow
          //   80: iconst_0
          //   81: istore_1
          //   82: goto -> 9
          // Exception table:
          //   from	to	target	type
          //   41	58	65	java/io/IOException
          //   41	58	73	finally
        }
        
        public String next() {
          if (!hasNext())
            throw new NoSuchElementException(); 
          String str = this.nextUrl;
          this.nextUrl = null;
          this.canRemove = true;
          return str;
        }
        
        public void remove() {
          if (!this.canRemove)
            throw new IllegalStateException("remove() before next()"); 
          this.delegate.remove();
        }
      };
  }
  
  public int writeAbortCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield writeAbortCount : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public int writeSuccessCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield writeSuccessCount : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  private final class CacheRequestImpl implements CacheRequest {
    private Sink body;
    
    private Sink cacheOut;
    
    boolean done;
    
    private final DiskLruCache.Editor editor;
    
    CacheRequestImpl(final DiskLruCache.Editor editor) {
      this.editor = editor;
      this.cacheOut = editor.newSink(1);
      this.body = (Sink)new ForwardingSink(this.cacheOut) {
          public void close() throws IOException {
            synchronized (Cache.this) {
              if (Cache.CacheRequestImpl.this.done)
                return; 
              Cache.CacheRequestImpl.this.done = true;
              Cache cache = Cache.this;
              cache.writeSuccessCount++;
              super.close();
              editor.commit();
              return;
            } 
          }
        };
    }
    
    public void abort() {
      Cache cache;
      synchronized (Cache.this) {
        if (this.done)
          return; 
        this.done = true;
        Cache cache1 = Cache.this;
        cache1.writeAbortCount++;
        Util.closeQuietly((Closeable)this.cacheOut);
        try {
          this.editor.abort();
        } catch (IOException iOException) {}
        return;
      } 
    }
    
    public Sink body() {
      return this.body;
    }
  }
  
  class null extends ForwardingSink {
    null(Sink param1Sink) {
      super(param1Sink);
    }
    
    public void close() throws IOException {
      synchronized (Cache.this) {
        if (this.this$1.done)
          return; 
        this.this$1.done = true;
        Cache cache = Cache.this;
        cache.writeSuccessCount++;
        super.close();
        editor.commit();
        return;
      } 
    }
  }
  
  private static class CacheResponseBody extends ResponseBody {
    private final BufferedSource bodySource;
    
    @Nullable
    private final String contentLength;
    
    @Nullable
    private final String contentType;
    
    final DiskLruCache.Snapshot snapshot;
    
    CacheResponseBody(final DiskLruCache.Snapshot snapshot, String param1String1, String param1String2) {
      this.snapshot = snapshot;
      this.contentType = param1String1;
      this.contentLength = param1String2;
      this.bodySource = Okio.buffer((Source)new ForwardingSource(snapshot.getSource(1)) {
            public void close() throws IOException {
              snapshot.close();
              super.close();
            }
          });
    }
    
    public long contentLength() {
      long l1 = -1L;
      long l2 = l1;
      try {
        if (this.contentLength != null)
          l2 = Long.parseLong(this.contentLength); 
      } catch (NumberFormatException numberFormatException) {
        l2 = l1;
      } 
      return l2;
    }
    
    public MediaType contentType() {
      return (this.contentType != null) ? MediaType.parse(this.contentType) : null;
    }
    
    public BufferedSource source() {
      return this.bodySource;
    }
  }
  
  class null extends ForwardingSource {
    null(Source param1Source) {
      super(param1Source);
    }
    
    public void close() throws IOException {
      snapshot.close();
      super.close();
    }
  }
  
  private static final class Entry {
    private static final String RECEIVED_MILLIS = Platform.get().getPrefix() + "-Received-Millis";
    
    private static final String SENT_MILLIS = Platform.get().getPrefix() + "-Sent-Millis";
    
    private final int code;
    
    @Nullable
    private final Handshake handshake;
    
    private final String message;
    
    private final Protocol protocol;
    
    private final long receivedResponseMillis;
    
    private final String requestMethod;
    
    private final Headers responseHeaders;
    
    private final long sentRequestMillis;
    
    private final String url;
    
    private final Headers varyHeaders;
    
    static {
    
    }
    
    Entry(Response param1Response) {
      this.url = param1Response.request().url().toString();
      this.varyHeaders = HttpHeaders.varyHeaders(param1Response);
      this.requestMethod = param1Response.request().method();
      this.protocol = param1Response.protocol();
      this.code = param1Response.code();
      this.message = param1Response.message();
      this.responseHeaders = param1Response.headers();
      this.handshake = param1Response.handshake();
      this.sentRequestMillis = param1Response.sentRequestAtMillis();
      this.receivedResponseMillis = param1Response.receivedResponseAtMillis();
    }
    
    Entry(Source param1Source) throws IOException {
      try {
        long l;
        BufferedSource bufferedSource = Okio.buffer(param1Source);
        this.url = bufferedSource.readUtf8LineStrict();
        this.requestMethod = bufferedSource.readUtf8LineStrict();
        Headers.Builder builder1 = new Headers.Builder();
        this();
        int i = Cache.readInt(bufferedSource);
        byte b;
        for (b = 0; b < i; b++)
          builder1.addLenient(bufferedSource.readUtf8LineStrict()); 
        this.varyHeaders = builder1.build();
        StatusLine statusLine = StatusLine.parse(bufferedSource.readUtf8LineStrict());
        this.protocol = statusLine.protocol;
        this.code = statusLine.code;
        this.message = statusLine.message;
        Headers.Builder builder2 = new Headers.Builder();
        this();
        i = Cache.readInt(bufferedSource);
        for (b = 0; b < i; b++)
          builder2.addLenient(bufferedSource.readUtf8LineStrict()); 
        String str1 = builder2.get(SENT_MILLIS);
        String str2 = builder2.get(RECEIVED_MILLIS);
        builder2.removeAll(SENT_MILLIS);
        builder2.removeAll(RECEIVED_MILLIS);
        if (str1 != null) {
          l = Long.parseLong(str1);
        } else {
          l = 0L;
        } 
        this.sentRequestMillis = l;
        if (str2 != null) {
          l = Long.parseLong(str2);
        } else {
          l = 0L;
        } 
        this.receivedResponseMillis = l;
        this.responseHeaders = builder2.build();
      } finally {
        param1Source.close();
      } 
      param1Source.close();
    }
    
    private boolean isHttps() {
      return this.url.startsWith("https://");
    }
    
    private List<Certificate> readCertificateList(BufferedSource param1BufferedSource) throws IOException {
      int i = Cache.readInt(param1BufferedSource);
      if (i == -1)
        return (List)Collections.emptyList(); 
      try {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        ArrayList<Certificate> arrayList = new ArrayList();
        this(i);
        byte b = 0;
        while (true) {
          String str;
          ArrayList<Certificate> arrayList1 = arrayList;
          if (b < i) {
            str = param1BufferedSource.readUtf8LineStrict();
            Buffer buffer = new Buffer();
            this();
            buffer.write(ByteString.decodeBase64(str));
            arrayList.add(certificateFactory.generateCertificate(buffer.inputStream()));
            b++;
            continue;
          } 
          return (List<Certificate>)str;
        } 
      } catch (CertificateException certificateException) {
        throw new IOException(certificateException.getMessage());
      } 
    }
    
    private void writeCertList(BufferedSink param1BufferedSink, List<Certificate> param1List) throws IOException {
      try {
        param1BufferedSink.writeDecimalLong(param1List.size()).writeByte(10);
        byte b = 0;
        int i = param1List.size();
        while (b < i) {
          param1BufferedSink.writeUtf8(ByteString.of(((Certificate)param1List.get(b)).getEncoded()).base64()).writeByte(10);
          b++;
        } 
      } catch (CertificateEncodingException certificateEncodingException) {
        throw new IOException(certificateEncodingException.getMessage());
      } 
    }
    
    public boolean matches(Request param1Request, Response param1Response) {
      return (this.url.equals(param1Request.url().toString()) && this.requestMethod.equals(param1Request.method()) && HttpHeaders.varyMatches(param1Response, this.varyHeaders, param1Request));
    }
    
    public Response response(DiskLruCache.Snapshot param1Snapshot) {
      String str1 = this.responseHeaders.get("Content-Type");
      String str2 = this.responseHeaders.get("Content-Length");
      Request request = (new Request.Builder()).url(this.url).method(this.requestMethod, null).headers(this.varyHeaders).build();
      return (new Response.Builder()).request(request).protocol(this.protocol).code(this.code).message(this.message).headers(this.responseHeaders).body(new Cache.CacheResponseBody(param1Snapshot, str1, str2)).handshake(this.handshake).sentRequestAtMillis(this.sentRequestMillis).receivedResponseAtMillis(this.receivedResponseMillis).build();
    }
    
    public void writeTo(DiskLruCache.Editor param1Editor) throws IOException {
      BufferedSink bufferedSink = Okio.buffer(param1Editor.newSink(0));
      bufferedSink.writeUtf8(this.url).writeByte(10);
      bufferedSink.writeUtf8(this.requestMethod).writeByte(10);
      bufferedSink.writeDecimalLong(this.varyHeaders.size()).writeByte(10);
      byte b = 0;
      int i = this.varyHeaders.size();
      while (b < i) {
        bufferedSink.writeUtf8(this.varyHeaders.name(b)).writeUtf8(": ").writeUtf8(this.varyHeaders.value(b)).writeByte(10);
        b++;
      } 
      bufferedSink.writeUtf8((new StatusLine(this.protocol, this.code, this.message)).toString()).writeByte(10);
      bufferedSink.writeDecimalLong((this.responseHeaders.size() + 2)).writeByte(10);
      b = 0;
      i = this.responseHeaders.size();
      while (b < i) {
        bufferedSink.writeUtf8(this.responseHeaders.name(b)).writeUtf8(": ").writeUtf8(this.responseHeaders.value(b)).writeByte(10);
        b++;
      } 
      bufferedSink.writeUtf8(SENT_MILLIS).writeUtf8(": ").writeDecimalLong(this.sentRequestMillis).writeByte(10);
      bufferedSink.writeUtf8(RECEIVED_MILLIS).writeUtf8(": ").writeDecimalLong(this.receivedResponseMillis).writeByte(10);
      if (isHttps()) {
        bufferedSink.writeByte(10);
        bufferedSink.writeUtf8(this.handshake.cipherSuite().javaName()).writeByte(10);
        writeCertList(bufferedSink, this.handshake.peerCertificates());
        writeCertList(bufferedSink, this.handshake.localCertificates());
        bufferedSink.writeUtf8(this.handshake.tlsVersion().javaName()).writeByte(10);
      } 
      bufferedSink.close();
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/Cache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */