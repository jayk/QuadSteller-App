package okhttp3.internal.cache2;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.ByteString;
import okio.Source;
import okio.Timeout;

final class Relay {
  private static final long FILE_HEADER_SIZE = 32L;
  
  static final ByteString PREFIX_CLEAN = ByteString.encodeUtf8("OkHttp cache v1\n");
  
  static final ByteString PREFIX_DIRTY = ByteString.encodeUtf8("OkHttp DIRTY :(\n");
  
  private static final int SOURCE_FILE = 2;
  
  private static final int SOURCE_UPSTREAM = 1;
  
  final Buffer buffer;
  
  final long bufferMaxSize;
  
  boolean complete;
  
  RandomAccessFile file;
  
  private final ByteString metadata;
  
  int sourceCount;
  
  Source upstream;
  
  final Buffer upstreamBuffer;
  
  long upstreamPos;
  
  Thread upstreamReader;
  
  private Relay(RandomAccessFile paramRandomAccessFile, Source paramSource, long paramLong1, ByteString paramByteString, long paramLong2) {
    boolean bool;
    this.upstreamBuffer = new Buffer();
    this.buffer = new Buffer();
    this.file = paramRandomAccessFile;
    this.upstream = paramSource;
    if (paramSource == null) {
      bool = true;
    } else {
      bool = false;
    } 
    this.complete = bool;
    this.upstreamPos = paramLong1;
    this.metadata = paramByteString;
    this.bufferMaxSize = paramLong2;
  }
  
  public static Relay edit(File paramFile, Source paramSource, ByteString paramByteString, long paramLong) throws IOException {
    RandomAccessFile randomAccessFile = new RandomAccessFile(paramFile, "rw");
    Relay relay = new Relay(randomAccessFile, paramSource, 0L, paramByteString, paramLong);
    randomAccessFile.setLength(0L);
    relay.writeHeader(PREFIX_DIRTY, -1L, -1L);
    return relay;
  }
  
  public static Relay read(File paramFile) throws IOException {
    RandomAccessFile randomAccessFile = new RandomAccessFile(paramFile, "rw");
    FileOperator fileOperator = new FileOperator(randomAccessFile.getChannel());
    Buffer buffer = new Buffer();
    fileOperator.read(0L, buffer, 32L);
    if (!buffer.readByteString(PREFIX_CLEAN.size()).equals(PREFIX_CLEAN))
      throw new IOException("unreadable cache file"); 
    long l1 = buffer.readLong();
    long l2 = buffer.readLong();
    buffer = new Buffer();
    fileOperator.read(32L + l1, buffer, l2);
    return new Relay(randomAccessFile, null, l1, buffer.readByteString(), 0L);
  }
  
  private void writeHeader(ByteString paramByteString, long paramLong1, long paramLong2) throws IOException {
    Buffer buffer = new Buffer();
    buffer.write(paramByteString);
    buffer.writeLong(paramLong1);
    buffer.writeLong(paramLong2);
    if (buffer.size() != 32L)
      throw new IllegalArgumentException(); 
    (new FileOperator(this.file.getChannel())).write(0L, buffer, 32L);
  }
  
  private void writeMetadata(long paramLong) throws IOException {
    Buffer buffer = new Buffer();
    buffer.write(this.metadata);
    (new FileOperator(this.file.getChannel())).write(32L + paramLong, buffer, this.metadata.size());
  }
  
  void commit(long paramLong) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: lload_1
    //   2: invokespecial writeMetadata : (J)V
    //   5: aload_0
    //   6: getfield file : Ljava/io/RandomAccessFile;
    //   9: invokevirtual getChannel : ()Ljava/nio/channels/FileChannel;
    //   12: iconst_0
    //   13: invokevirtual force : (Z)V
    //   16: aload_0
    //   17: getstatic okhttp3/internal/cache2/Relay.PREFIX_CLEAN : Lokio/ByteString;
    //   20: lload_1
    //   21: aload_0
    //   22: getfield metadata : Lokio/ByteString;
    //   25: invokevirtual size : ()I
    //   28: i2l
    //   29: invokespecial writeHeader : (Lokio/ByteString;JJ)V
    //   32: aload_0
    //   33: getfield file : Ljava/io/RandomAccessFile;
    //   36: invokevirtual getChannel : ()Ljava/nio/channels/FileChannel;
    //   39: iconst_0
    //   40: invokevirtual force : (Z)V
    //   43: aload_0
    //   44: monitorenter
    //   45: aload_0
    //   46: iconst_1
    //   47: putfield complete : Z
    //   50: aload_0
    //   51: monitorexit
    //   52: aload_0
    //   53: getfield upstream : Lokio/Source;
    //   56: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   59: aload_0
    //   60: aconst_null
    //   61: putfield upstream : Lokio/Source;
    //   64: return
    //   65: astore_3
    //   66: aload_0
    //   67: monitorexit
    //   68: aload_3
    //   69: athrow
    // Exception table:
    //   from	to	target	type
    //   45	52	65	finally
    //   66	68	65	finally
  }
  
  boolean isClosed() {
    return (this.file == null);
  }
  
  public ByteString metadata() {
    return this.metadata;
  }
  
  public Source newSource() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield file : Ljava/io/RandomAccessFile;
    //   6: ifnonnull -> 15
    //   9: aconst_null
    //   10: astore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_1
    //   14: areturn
    //   15: aload_0
    //   16: aload_0
    //   17: getfield sourceCount : I
    //   20: iconst_1
    //   21: iadd
    //   22: putfield sourceCount : I
    //   25: aload_0
    //   26: monitorexit
    //   27: new okhttp3/internal/cache2/Relay$RelaySource
    //   30: dup
    //   31: aload_0
    //   32: invokespecial <init> : (Lokhttp3/internal/cache2/Relay;)V
    //   35: astore_1
    //   36: goto -> 13
    //   39: astore_1
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_1
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	39	finally
    //   11	13	39	finally
    //   15	27	39	finally
    //   40	42	39	finally
  }
  
  class RelaySource implements Source {
    private FileOperator fileOperator = new FileOperator(Relay.this.file.getChannel());
    
    private long sourcePos;
    
    private final Timeout timeout = new Timeout();
    
    public void close() throws IOException {
      if (this.fileOperator != null) {
        this.fileOperator = null;
        null = null;
        synchronized (Relay.this) {
          Relay relay = Relay.this;
          relay.sourceCount--;
          if (Relay.this.sourceCount == 0) {
            null = Relay.this.file;
            Relay.this.file = null;
          } 
          if (null != null)
            Util.closeQuietly(null); 
          return;
        } 
      } 
    }
    
    public long read(Buffer param1Buffer, long param1Long) throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: getfield fileOperator : Lokhttp3/internal/cache2/FileOperator;
      //   4: ifnonnull -> 17
      //   7: new java/lang/IllegalStateException
      //   10: dup
      //   11: ldc 'closed'
      //   13: invokespecial <init> : (Ljava/lang/String;)V
      //   16: athrow
      //   17: aload_0
      //   18: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   21: astore #4
      //   23: aload #4
      //   25: monitorenter
      //   26: aload_0
      //   27: getfield sourcePos : J
      //   30: lstore #5
      //   32: aload_0
      //   33: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   36: getfield upstreamPos : J
      //   39: lstore #7
      //   41: lload #5
      //   43: lload #7
      //   45: lcmp
      //   46: ifne -> 162
      //   49: aload_0
      //   50: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   53: getfield complete : Z
      //   56: ifeq -> 68
      //   59: ldc2_w -1
      //   62: lstore_2
      //   63: aload #4
      //   65: monitorexit
      //   66: lload_2
      //   67: lreturn
      //   68: aload_0
      //   69: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   72: getfield upstreamReader : Ljava/lang/Thread;
      //   75: ifnull -> 98
      //   78: aload_0
      //   79: getfield timeout : Lokio/Timeout;
      //   82: aload_0
      //   83: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   86: invokevirtual waitUntilNotified : (Ljava/lang/Object;)V
      //   89: goto -> 26
      //   92: astore_1
      //   93: aload #4
      //   95: monitorexit
      //   96: aload_1
      //   97: athrow
      //   98: aload_0
      //   99: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   102: invokestatic currentThread : ()Ljava/lang/Thread;
      //   105: putfield upstreamReader : Ljava/lang/Thread;
      //   108: iconst_1
      //   109: istore #9
      //   111: aload #4
      //   113: monitorexit
      //   114: iload #9
      //   116: iconst_2
      //   117: if_icmpne -> 244
      //   120: lload_2
      //   121: lload #7
      //   123: aload_0
      //   124: getfield sourcePos : J
      //   127: lsub
      //   128: invokestatic min : (JJ)J
      //   131: lstore_2
      //   132: aload_0
      //   133: getfield fileOperator : Lokhttp3/internal/cache2/FileOperator;
      //   136: ldc2_w 32
      //   139: aload_0
      //   140: getfield sourcePos : J
      //   143: ladd
      //   144: aload_1
      //   145: lload_2
      //   146: invokevirtual read : (JLokio/Buffer;J)V
      //   149: aload_0
      //   150: aload_0
      //   151: getfield sourcePos : J
      //   154: lload_2
      //   155: ladd
      //   156: putfield sourcePos : J
      //   159: goto -> 66
      //   162: lload #7
      //   164: aload_0
      //   165: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   168: getfield buffer : Lokio/Buffer;
      //   171: invokevirtual size : ()J
      //   174: lsub
      //   175: lstore #5
      //   177: aload_0
      //   178: getfield sourcePos : J
      //   181: lload #5
      //   183: lcmp
      //   184: ifge -> 196
      //   187: iconst_2
      //   188: istore #9
      //   190: aload #4
      //   192: monitorexit
      //   193: goto -> 114
      //   196: lload_2
      //   197: lload #7
      //   199: aload_0
      //   200: getfield sourcePos : J
      //   203: lsub
      //   204: invokestatic min : (JJ)J
      //   207: lstore_2
      //   208: aload_0
      //   209: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   212: getfield buffer : Lokio/Buffer;
      //   215: aload_1
      //   216: aload_0
      //   217: getfield sourcePos : J
      //   220: lload #5
      //   222: lsub
      //   223: lload_2
      //   224: invokevirtual copyTo : (Lokio/Buffer;JJ)Lokio/Buffer;
      //   227: pop
      //   228: aload_0
      //   229: aload_0
      //   230: getfield sourcePos : J
      //   233: lload_2
      //   234: ladd
      //   235: putfield sourcePos : J
      //   238: aload #4
      //   240: monitorexit
      //   241: goto -> 66
      //   244: aload_0
      //   245: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   248: getfield upstream : Lokio/Source;
      //   251: aload_0
      //   252: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   255: getfield upstreamBuffer : Lokio/Buffer;
      //   258: aload_0
      //   259: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   262: getfield bufferMaxSize : J
      //   265: invokeinterface read : (Lokio/Buffer;J)J
      //   270: lstore #5
      //   272: lload #5
      //   274: ldc2_w -1
      //   277: lcmp
      //   278: ifne -> 330
      //   281: aload_0
      //   282: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   285: lload #7
      //   287: invokevirtual commit : (J)V
      //   290: ldc2_w -1
      //   293: lstore_2
      //   294: aload_0
      //   295: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   298: astore #4
      //   300: aload #4
      //   302: monitorenter
      //   303: aload_0
      //   304: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   307: aconst_null
      //   308: putfield upstreamReader : Ljava/lang/Thread;
      //   311: aload_0
      //   312: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   315: invokevirtual notifyAll : ()V
      //   318: aload #4
      //   320: monitorexit
      //   321: goto -> 66
      //   324: astore_1
      //   325: aload #4
      //   327: monitorexit
      //   328: aload_1
      //   329: athrow
      //   330: lload #5
      //   332: lload_2
      //   333: invokestatic min : (JJ)J
      //   336: lstore_2
      //   337: aload_0
      //   338: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   341: getfield upstreamBuffer : Lokio/Buffer;
      //   344: aload_1
      //   345: lconst_0
      //   346: lload_2
      //   347: invokevirtual copyTo : (Lokio/Buffer;JJ)Lokio/Buffer;
      //   350: pop
      //   351: aload_0
      //   352: aload_0
      //   353: getfield sourcePos : J
      //   356: lload_2
      //   357: ladd
      //   358: putfield sourcePos : J
      //   361: aload_0
      //   362: getfield fileOperator : Lokhttp3/internal/cache2/FileOperator;
      //   365: ldc2_w 32
      //   368: lload #7
      //   370: ladd
      //   371: aload_0
      //   372: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   375: getfield upstreamBuffer : Lokio/Buffer;
      //   378: invokevirtual clone : ()Lokio/Buffer;
      //   381: lload #5
      //   383: invokevirtual write : (JLokio/Buffer;J)V
      //   386: aload_0
      //   387: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   390: astore_1
      //   391: aload_1
      //   392: monitorenter
      //   393: aload_0
      //   394: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   397: getfield buffer : Lokio/Buffer;
      //   400: aload_0
      //   401: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   404: getfield upstreamBuffer : Lokio/Buffer;
      //   407: lload #5
      //   409: invokevirtual write : (Lokio/Buffer;J)V
      //   412: aload_0
      //   413: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   416: getfield buffer : Lokio/Buffer;
      //   419: invokevirtual size : ()J
      //   422: aload_0
      //   423: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   426: getfield bufferMaxSize : J
      //   429: lcmp
      //   430: ifle -> 461
      //   433: aload_0
      //   434: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   437: getfield buffer : Lokio/Buffer;
      //   440: aload_0
      //   441: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   444: getfield buffer : Lokio/Buffer;
      //   447: invokevirtual size : ()J
      //   450: aload_0
      //   451: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   454: getfield bufferMaxSize : J
      //   457: lsub
      //   458: invokevirtual skip : (J)V
      //   461: aload_0
      //   462: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   465: astore #4
      //   467: aload #4
      //   469: aload #4
      //   471: getfield upstreamPos : J
      //   474: lload #5
      //   476: ladd
      //   477: putfield upstreamPos : J
      //   480: aload_1
      //   481: monitorexit
      //   482: aload_0
      //   483: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   486: astore #4
      //   488: aload #4
      //   490: monitorenter
      //   491: aload_0
      //   492: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   495: aconst_null
      //   496: putfield upstreamReader : Ljava/lang/Thread;
      //   499: aload_0
      //   500: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   503: invokevirtual notifyAll : ()V
      //   506: aload #4
      //   508: monitorexit
      //   509: goto -> 66
      //   512: astore #4
      //   514: aload_1
      //   515: monitorexit
      //   516: aload #4
      //   518: athrow
      //   519: astore #4
      //   521: aload_0
      //   522: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   525: astore_1
      //   526: aload_1
      //   527: monitorenter
      //   528: aload_0
      //   529: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   532: aconst_null
      //   533: putfield upstreamReader : Ljava/lang/Thread;
      //   536: aload_0
      //   537: getfield this$0 : Lokhttp3/internal/cache2/Relay;
      //   540: invokevirtual notifyAll : ()V
      //   543: aload_1
      //   544: monitorexit
      //   545: aload #4
      //   547: athrow
      //   548: astore_1
      //   549: aload #4
      //   551: monitorexit
      //   552: aload_1
      //   553: athrow
      //   554: astore #4
      //   556: aload_1
      //   557: monitorexit
      //   558: aload #4
      //   560: athrow
      // Exception table:
      //   from	to	target	type
      //   26	41	92	finally
      //   49	59	92	finally
      //   63	66	92	finally
      //   68	89	92	finally
      //   93	96	92	finally
      //   98	108	92	finally
      //   111	114	92	finally
      //   162	187	92	finally
      //   190	193	92	finally
      //   196	241	92	finally
      //   244	272	519	finally
      //   281	290	519	finally
      //   303	321	324	finally
      //   325	328	324	finally
      //   330	393	519	finally
      //   393	461	512	finally
      //   461	482	512	finally
      //   491	509	548	finally
      //   514	516	512	finally
      //   516	519	519	finally
      //   528	545	554	finally
      //   549	552	548	finally
      //   556	558	554	finally
    }
    
    public Timeout timeout() {
      return this.timeout;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/cache2/Relay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */