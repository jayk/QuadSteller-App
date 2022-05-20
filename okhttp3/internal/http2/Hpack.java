package okhttp3.internal.http2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;
import okio.Source;

final class Hpack {
  static final Map<ByteString, Integer> NAME_TO_FIRST_INDEX;
  
  private static final int PREFIX_4_BITS = 15;
  
  private static final int PREFIX_5_BITS = 31;
  
  private static final int PREFIX_6_BITS = 63;
  
  private static final int PREFIX_7_BITS = 127;
  
  static final Header[] STATIC_HEADER_TABLE = new Header[] { 
      new Header(Header.TARGET_AUTHORITY, ""), new Header(Header.TARGET_METHOD, "GET"), new Header(Header.TARGET_METHOD, "POST"), new Header(Header.TARGET_PATH, "/"), new Header(Header.TARGET_PATH, "/index.html"), new Header(Header.TARGET_SCHEME, "http"), new Header(Header.TARGET_SCHEME, "https"), new Header(Header.RESPONSE_STATUS, "200"), new Header(Header.RESPONSE_STATUS, "204"), new Header(Header.RESPONSE_STATUS, "206"), 
      new Header(Header.RESPONSE_STATUS, "304"), new Header(Header.RESPONSE_STATUS, "400"), new Header(Header.RESPONSE_STATUS, "404"), new Header(Header.RESPONSE_STATUS, "500"), new Header("accept-charset", ""), new Header("accept-encoding", "gzip, deflate"), new Header("accept-language", ""), new Header("accept-ranges", ""), new Header("accept", ""), new Header("access-control-allow-origin", ""), 
      new Header("age", ""), new Header("allow", ""), new Header("authorization", ""), new Header("cache-control", ""), new Header("content-disposition", ""), new Header("content-encoding", ""), new Header("content-language", ""), new Header("content-length", ""), new Header("content-location", ""), new Header("content-range", ""), 
      new Header("content-type", ""), new Header("cookie", ""), new Header("date", ""), new Header("etag", ""), new Header("expect", ""), new Header("expires", ""), new Header("from", ""), new Header("host", ""), new Header("if-match", ""), new Header("if-modified-since", ""), 
      new Header("if-none-match", ""), new Header("if-range", ""), new Header("if-unmodified-since", ""), new Header("last-modified", ""), new Header("link", ""), new Header("location", ""), new Header("max-forwards", ""), new Header("proxy-authenticate", ""), new Header("proxy-authorization", ""), new Header("range", ""), 
      new Header("referer", ""), new Header("refresh", ""), new Header("retry-after", ""), new Header("server", ""), new Header("set-cookie", ""), new Header("strict-transport-security", ""), new Header("transfer-encoding", ""), new Header("user-agent", ""), new Header("vary", ""), new Header("via", ""), 
      new Header("www-authenticate", "") };
  
  static {
    NAME_TO_FIRST_INDEX = nameToFirstIndex();
  }
  
  static ByteString checkLowercase(ByteString paramByteString) throws IOException {
    byte b = 0;
    int i = paramByteString.size();
    while (b < i) {
      byte b1 = paramByteString.getByte(b);
      if (b1 >= 65 && b1 <= 90)
        throw new IOException("PROTOCOL_ERROR response malformed: mixed case name: " + paramByteString.utf8()); 
      b++;
    } 
    return paramByteString;
  }
  
  private static Map<ByteString, Integer> nameToFirstIndex() {
    LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<Object, Object>(STATIC_HEADER_TABLE.length);
    for (byte b = 0; b < STATIC_HEADER_TABLE.length; b++) {
      if (!linkedHashMap.containsKey((STATIC_HEADER_TABLE[b]).name))
        linkedHashMap.put((STATIC_HEADER_TABLE[b]).name, Integer.valueOf(b)); 
    } 
    return (Map)Collections.unmodifiableMap(linkedHashMap);
  }
  
  static final class Reader {
    Header[] dynamicTable = new Header[8];
    
    int dynamicTableByteCount = 0;
    
    int headerCount = 0;
    
    private final List<Header> headerList = new ArrayList<Header>();
    
    private final int headerTableSizeSetting;
    
    private int maxDynamicTableByteCount;
    
    int nextHeaderIndex = this.dynamicTable.length - 1;
    
    private final BufferedSource source;
    
    Reader(int param1Int1, int param1Int2, Source param1Source) {
      this.headerTableSizeSetting = param1Int1;
      this.maxDynamicTableByteCount = param1Int2;
      this.source = Okio.buffer(param1Source);
    }
    
    Reader(int param1Int, Source param1Source) {
      this(param1Int, param1Int, param1Source);
    }
    
    private void adjustDynamicTableByteCount() {
      if (this.maxDynamicTableByteCount < this.dynamicTableByteCount) {
        if (this.maxDynamicTableByteCount == 0) {
          clearDynamicTable();
          return;
        } 
      } else {
        return;
      } 
      evictToRecoverBytes(this.dynamicTableByteCount - this.maxDynamicTableByteCount);
    }
    
    private void clearDynamicTable() {
      Arrays.fill((Object[])this.dynamicTable, (Object)null);
      this.nextHeaderIndex = this.dynamicTable.length - 1;
      this.headerCount = 0;
      this.dynamicTableByteCount = 0;
    }
    
    private int dynamicTableIndex(int param1Int) {
      return this.nextHeaderIndex + 1 + param1Int;
    }
    
    private int evictToRecoverBytes(int param1Int) {
      int i = 0;
      boolean bool = false;
      if (param1Int > 0) {
        i = this.dynamicTable.length - 1;
        int j = param1Int;
        param1Int = bool;
        while (i >= this.nextHeaderIndex && j > 0) {
          j -= (this.dynamicTable[i]).hpackSize;
          this.dynamicTableByteCount -= (this.dynamicTable[i]).hpackSize;
          this.headerCount--;
          param1Int++;
          i--;
        } 
        System.arraycopy(this.dynamicTable, this.nextHeaderIndex + 1, this.dynamicTable, this.nextHeaderIndex + 1 + param1Int, this.headerCount);
        this.nextHeaderIndex += param1Int;
        i = param1Int;
      } 
      return i;
    }
    
    private ByteString getName(int param1Int) {
      return isStaticHeader(param1Int) ? (Hpack.STATIC_HEADER_TABLE[param1Int]).name : (this.dynamicTable[dynamicTableIndex(param1Int - Hpack.STATIC_HEADER_TABLE.length)]).name;
    }
    
    private void insertIntoDynamicTable(int param1Int, Header param1Header) {
      this.headerList.add(param1Header);
      int i = param1Header.hpackSize;
      int j = i;
      if (param1Int != -1)
        j = i - (this.dynamicTable[dynamicTableIndex(param1Int)]).hpackSize; 
      if (j > this.maxDynamicTableByteCount) {
        clearDynamicTable();
        return;
      } 
      i = evictToRecoverBytes(this.dynamicTableByteCount + j - this.maxDynamicTableByteCount);
      if (param1Int == -1) {
        if (this.headerCount + 1 > this.dynamicTable.length) {
          Header[] arrayOfHeader = new Header[this.dynamicTable.length * 2];
          System.arraycopy(this.dynamicTable, 0, arrayOfHeader, this.dynamicTable.length, this.dynamicTable.length);
          this.nextHeaderIndex = this.dynamicTable.length - 1;
          this.dynamicTable = arrayOfHeader;
        } 
        param1Int = this.nextHeaderIndex;
        this.nextHeaderIndex = param1Int - 1;
        this.dynamicTable[param1Int] = param1Header;
        this.headerCount++;
      } else {
        int k = dynamicTableIndex(param1Int);
        this.dynamicTable[param1Int + k + i] = param1Header;
      } 
      this.dynamicTableByteCount += j;
    }
    
    private boolean isStaticHeader(int param1Int) {
      return (param1Int >= 0 && param1Int <= Hpack.STATIC_HEADER_TABLE.length - 1);
    }
    
    private int readByte() throws IOException {
      return this.source.readByte() & 0xFF;
    }
    
    private void readIndexedHeader(int param1Int) throws IOException {
      if (isStaticHeader(param1Int)) {
        Header header = Hpack.STATIC_HEADER_TABLE[param1Int];
        this.headerList.add(header);
        return;
      } 
      int i = dynamicTableIndex(param1Int - Hpack.STATIC_HEADER_TABLE.length);
      if (i < 0 || i > this.dynamicTable.length - 1)
        throw new IOException("Header index too large " + (param1Int + 1)); 
      this.headerList.add(this.dynamicTable[i]);
    }
    
    private void readLiteralHeaderWithIncrementalIndexingIndexedName(int param1Int) throws IOException {
      insertIntoDynamicTable(-1, new Header(getName(param1Int), readByteString()));
    }
    
    private void readLiteralHeaderWithIncrementalIndexingNewName() throws IOException {
      insertIntoDynamicTable(-1, new Header(Hpack.checkLowercase(readByteString()), readByteString()));
    }
    
    private void readLiteralHeaderWithoutIndexingIndexedName(int param1Int) throws IOException {
      ByteString byteString1 = getName(param1Int);
      ByteString byteString2 = readByteString();
      this.headerList.add(new Header(byteString1, byteString2));
    }
    
    private void readLiteralHeaderWithoutIndexingNewName() throws IOException {
      ByteString byteString1 = Hpack.checkLowercase(readByteString());
      ByteString byteString2 = readByteString();
      this.headerList.add(new Header(byteString1, byteString2));
    }
    
    public List<Header> getAndResetHeaderList() {
      ArrayList<Header> arrayList = new ArrayList<Header>(this.headerList);
      this.headerList.clear();
      return arrayList;
    }
    
    int maxDynamicTableByteCount() {
      return this.maxDynamicTableByteCount;
    }
    
    ByteString readByteString() throws IOException {
      boolean bool;
      int i = readByte();
      if ((i & 0x80) == 128) {
        bool = true;
      } else {
        bool = false;
      } 
      i = readInt(i, 127);
      return bool ? ByteString.of(Huffman.get().decode(this.source.readByteArray(i))) : this.source.readByteString(i);
    }
    
    void readHeaders() throws IOException {
      while (!this.source.exhausted()) {
        int i = this.source.readByte() & 0xFF;
        if (i == 128)
          throw new IOException("index == 0"); 
        if ((i & 0x80) == 128) {
          readIndexedHeader(readInt(i, 127) - 1);
          continue;
        } 
        if (i == 64) {
          readLiteralHeaderWithIncrementalIndexingNewName();
          continue;
        } 
        if ((i & 0x40) == 64) {
          readLiteralHeaderWithIncrementalIndexingIndexedName(readInt(i, 63) - 1);
          continue;
        } 
        if ((i & 0x20) == 32) {
          this.maxDynamicTableByteCount = readInt(i, 31);
          if (this.maxDynamicTableByteCount < 0 || this.maxDynamicTableByteCount > this.headerTableSizeSetting)
            throw new IOException("Invalid dynamic table size update " + this.maxDynamicTableByteCount); 
          adjustDynamicTableByteCount();
          continue;
        } 
        if (i == 16 || i == 0) {
          readLiteralHeaderWithoutIndexingNewName();
          continue;
        } 
        readLiteralHeaderWithoutIndexingIndexedName(readInt(i, 15) - 1);
      } 
    }
    
    int readInt(int param1Int1, int param1Int2) throws IOException {
      param1Int1 &= param1Int2;
      if (param1Int1 < param1Int2)
        return param1Int1; 
      param1Int1 = 0;
      while (true) {
        int i = readByte();
        if ((i & 0x80) != 0) {
          param1Int2 += (i & 0x7F) << param1Int1;
          param1Int1 += 7;
          continue;
        } 
        param1Int1 = param1Int2 + (i << param1Int1);
        // Byte code: goto -> 9
      } 
    }
  }
  
  static final class Writer {
    private static final int SETTINGS_HEADER_TABLE_SIZE = 4096;
    
    private static final int SETTINGS_HEADER_TABLE_SIZE_LIMIT = 16384;
    
    Header[] dynamicTable = new Header[8];
    
    int dynamicTableByteCount = 0;
    
    private boolean emitDynamicTableSizeUpdate;
    
    int headerCount = 0;
    
    int headerTableSizeSetting;
    
    int maxDynamicTableByteCount;
    
    int nextHeaderIndex = this.dynamicTable.length - 1;
    
    private final Buffer out;
    
    private int smallestHeaderTableSizeSetting = Integer.MAX_VALUE;
    
    private final boolean useCompression;
    
    Writer(int param1Int, boolean param1Boolean, Buffer param1Buffer) {
      this.headerTableSizeSetting = param1Int;
      this.maxDynamicTableByteCount = param1Int;
      this.useCompression = param1Boolean;
      this.out = param1Buffer;
    }
    
    Writer(Buffer param1Buffer) {
      this(4096, true, param1Buffer);
    }
    
    private void adjustDynamicTableByteCount() {
      if (this.maxDynamicTableByteCount < this.dynamicTableByteCount) {
        if (this.maxDynamicTableByteCount == 0) {
          clearDynamicTable();
          return;
        } 
      } else {
        return;
      } 
      evictToRecoverBytes(this.dynamicTableByteCount - this.maxDynamicTableByteCount);
    }
    
    private void clearDynamicTable() {
      Arrays.fill((Object[])this.dynamicTable, (Object)null);
      this.nextHeaderIndex = this.dynamicTable.length - 1;
      this.headerCount = 0;
      this.dynamicTableByteCount = 0;
    }
    
    private int evictToRecoverBytes(int param1Int) {
      int i = 0;
      boolean bool = false;
      if (param1Int > 0) {
        i = this.dynamicTable.length - 1;
        int j = param1Int;
        param1Int = bool;
        while (i >= this.nextHeaderIndex && j > 0) {
          j -= (this.dynamicTable[i]).hpackSize;
          this.dynamicTableByteCount -= (this.dynamicTable[i]).hpackSize;
          this.headerCount--;
          param1Int++;
          i--;
        } 
        System.arraycopy(this.dynamicTable, this.nextHeaderIndex + 1, this.dynamicTable, this.nextHeaderIndex + 1 + param1Int, this.headerCount);
        Arrays.fill((Object[])this.dynamicTable, this.nextHeaderIndex + 1, this.nextHeaderIndex + 1 + param1Int, (Object)null);
        this.nextHeaderIndex += param1Int;
        i = param1Int;
      } 
      return i;
    }
    
    private void insertIntoDynamicTable(Header param1Header) {
      int i = param1Header.hpackSize;
      if (i > this.maxDynamicTableByteCount) {
        clearDynamicTable();
        return;
      } 
      evictToRecoverBytes(this.dynamicTableByteCount + i - this.maxDynamicTableByteCount);
      if (this.headerCount + 1 > this.dynamicTable.length) {
        Header[] arrayOfHeader = new Header[this.dynamicTable.length * 2];
        System.arraycopy(this.dynamicTable, 0, arrayOfHeader, this.dynamicTable.length, this.dynamicTable.length);
        this.nextHeaderIndex = this.dynamicTable.length - 1;
        this.dynamicTable = arrayOfHeader;
      } 
      int j = this.nextHeaderIndex;
      this.nextHeaderIndex = j - 1;
      this.dynamicTable[j] = param1Header;
      this.headerCount++;
      this.dynamicTableByteCount += i;
    }
    
    void setHeaderTableSizeSetting(int param1Int) {
      this.headerTableSizeSetting = param1Int;
      param1Int = Math.min(param1Int, 16384);
      if (this.maxDynamicTableByteCount != param1Int) {
        if (param1Int < this.maxDynamicTableByteCount)
          this.smallestHeaderTableSizeSetting = Math.min(this.smallestHeaderTableSizeSetting, param1Int); 
        this.emitDynamicTableSizeUpdate = true;
        this.maxDynamicTableByteCount = param1Int;
        adjustDynamicTableByteCount();
      } 
    }
    
    void writeByteString(ByteString param1ByteString) throws IOException {
      if (this.useCompression && Huffman.get().encodedLength(param1ByteString) < param1ByteString.size()) {
        Buffer buffer = new Buffer();
        Huffman.get().encode(param1ByteString, (BufferedSink)buffer);
        param1ByteString = buffer.readByteString();
        writeInt(param1ByteString.size(), 127, 128);
        this.out.write(param1ByteString);
        return;
      } 
      writeInt(param1ByteString.size(), 127, 0);
      this.out.write(param1ByteString);
    }
    
    void writeHeaders(List<Header> param1List) throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: getfield emitDynamicTableSizeUpdate : Z
      //   4: ifeq -> 53
      //   7: aload_0
      //   8: getfield smallestHeaderTableSizeSetting : I
      //   11: aload_0
      //   12: getfield maxDynamicTableByteCount : I
      //   15: if_icmpge -> 30
      //   18: aload_0
      //   19: aload_0
      //   20: getfield smallestHeaderTableSizeSetting : I
      //   23: bipush #31
      //   25: bipush #32
      //   27: invokevirtual writeInt : (III)V
      //   30: aload_0
      //   31: iconst_0
      //   32: putfield emitDynamicTableSizeUpdate : Z
      //   35: aload_0
      //   36: ldc 2147483647
      //   38: putfield smallestHeaderTableSizeSetting : I
      //   41: aload_0
      //   42: aload_0
      //   43: getfield maxDynamicTableByteCount : I
      //   46: bipush #31
      //   48: bipush #32
      //   50: invokevirtual writeInt : (III)V
      //   53: iconst_0
      //   54: istore_2
      //   55: aload_1
      //   56: invokeinterface size : ()I
      //   61: istore_3
      //   62: iload_2
      //   63: iload_3
      //   64: if_icmpge -> 490
      //   67: aload_1
      //   68: iload_2
      //   69: invokeinterface get : (I)Ljava/lang/Object;
      //   74: checkcast okhttp3/internal/http2/Header
      //   77: astore #4
      //   79: aload #4
      //   81: getfield name : Lokio/ByteString;
      //   84: invokevirtual toAsciiLowercase : ()Lokio/ByteString;
      //   87: astore #5
      //   89: aload #4
      //   91: getfield value : Lokio/ByteString;
      //   94: astore #6
      //   96: iconst_m1
      //   97: istore #7
      //   99: iconst_m1
      //   100: istore #8
      //   102: getstatic okhttp3/internal/http2/Hpack.NAME_TO_FIRST_INDEX : Ljava/util/Map;
      //   105: aload #5
      //   107: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
      //   112: checkcast java/lang/Integer
      //   115: astore #9
      //   117: iload #7
      //   119: istore #10
      //   121: aload #9
      //   123: ifnull -> 191
      //   126: aload #9
      //   128: invokevirtual intValue : ()I
      //   131: iconst_1
      //   132: iadd
      //   133: istore #11
      //   135: iload #7
      //   137: istore #10
      //   139: iload #11
      //   141: istore #8
      //   143: iload #11
      //   145: iconst_1
      //   146: if_icmple -> 191
      //   149: iload #7
      //   151: istore #10
      //   153: iload #11
      //   155: istore #8
      //   157: iload #11
      //   159: bipush #8
      //   161: if_icmpge -> 191
      //   164: getstatic okhttp3/internal/http2/Hpack.STATIC_HEADER_TABLE : [Lokhttp3/internal/http2/Header;
      //   167: iload #11
      //   169: iconst_1
      //   170: isub
      //   171: aaload
      //   172: getfield value : Lokio/ByteString;
      //   175: aload #6
      //   177: invokestatic equal : (Ljava/lang/Object;Ljava/lang/Object;)Z
      //   180: ifeq -> 316
      //   183: iload #11
      //   185: istore #10
      //   187: iload #11
      //   189: istore #8
      //   191: iload #10
      //   193: istore #12
      //   195: iload #8
      //   197: istore #7
      //   199: iload #10
      //   201: iconst_m1
      //   202: if_icmpne -> 293
      //   205: aload_0
      //   206: getfield nextHeaderIndex : I
      //   209: iconst_1
      //   210: iadd
      //   211: istore #11
      //   213: aload_0
      //   214: getfield dynamicTable : [Lokhttp3/internal/http2/Header;
      //   217: arraylength
      //   218: istore #13
      //   220: iload #10
      //   222: istore #12
      //   224: iload #8
      //   226: istore #7
      //   228: iload #11
      //   230: iload #13
      //   232: if_icmpge -> 293
      //   235: iload #8
      //   237: istore #7
      //   239: aload_0
      //   240: getfield dynamicTable : [Lokhttp3/internal/http2/Header;
      //   243: iload #11
      //   245: aaload
      //   246: getfield name : Lokio/ByteString;
      //   249: aload #5
      //   251: invokestatic equal : (Ljava/lang/Object;Ljava/lang/Object;)Z
      //   254: ifeq -> 378
      //   257: aload_0
      //   258: getfield dynamicTable : [Lokhttp3/internal/http2/Header;
      //   261: iload #11
      //   263: aaload
      //   264: getfield value : Lokio/ByteString;
      //   267: aload #6
      //   269: invokestatic equal : (Ljava/lang/Object;Ljava/lang/Object;)Z
      //   272: ifeq -> 354
      //   275: iload #11
      //   277: aload_0
      //   278: getfield nextHeaderIndex : I
      //   281: isub
      //   282: getstatic okhttp3/internal/http2/Hpack.STATIC_HEADER_TABLE : [Lokhttp3/internal/http2/Header;
      //   285: arraylength
      //   286: iadd
      //   287: istore #12
      //   289: iload #8
      //   291: istore #7
      //   293: iload #12
      //   295: iconst_m1
      //   296: if_icmpeq -> 388
      //   299: aload_0
      //   300: iload #12
      //   302: bipush #127
      //   304: sipush #128
      //   307: invokevirtual writeInt : (III)V
      //   310: iinc #2, 1
      //   313: goto -> 62
      //   316: iload #7
      //   318: istore #10
      //   320: iload #11
      //   322: istore #8
      //   324: getstatic okhttp3/internal/http2/Hpack.STATIC_HEADER_TABLE : [Lokhttp3/internal/http2/Header;
      //   327: iload #11
      //   329: aaload
      //   330: getfield value : Lokio/ByteString;
      //   333: aload #6
      //   335: invokestatic equal : (Ljava/lang/Object;Ljava/lang/Object;)Z
      //   338: ifeq -> 191
      //   341: iload #11
      //   343: iconst_1
      //   344: iadd
      //   345: istore #10
      //   347: iload #11
      //   349: istore #8
      //   351: goto -> 191
      //   354: iload #8
      //   356: istore #7
      //   358: iload #8
      //   360: iconst_m1
      //   361: if_icmpne -> 378
      //   364: iload #11
      //   366: aload_0
      //   367: getfield nextHeaderIndex : I
      //   370: isub
      //   371: getstatic okhttp3/internal/http2/Hpack.STATIC_HEADER_TABLE : [Lokhttp3/internal/http2/Header;
      //   374: arraylength
      //   375: iadd
      //   376: istore #7
      //   378: iinc #11, 1
      //   381: iload #7
      //   383: istore #8
      //   385: goto -> 220
      //   388: iload #7
      //   390: iconst_m1
      //   391: if_icmpne -> 425
      //   394: aload_0
      //   395: getfield out : Lokio/Buffer;
      //   398: bipush #64
      //   400: invokevirtual writeByte : (I)Lokio/Buffer;
      //   403: pop
      //   404: aload_0
      //   405: aload #5
      //   407: invokevirtual writeByteString : (Lokio/ByteString;)V
      //   410: aload_0
      //   411: aload #6
      //   413: invokevirtual writeByteString : (Lokio/ByteString;)V
      //   416: aload_0
      //   417: aload #4
      //   419: invokespecial insertIntoDynamicTable : (Lokhttp3/internal/http2/Header;)V
      //   422: goto -> 310
      //   425: aload #5
      //   427: getstatic okhttp3/internal/http2/Header.PSEUDO_PREFIX : Lokio/ByteString;
      //   430: invokevirtual startsWith : (Lokio/ByteString;)Z
      //   433: ifeq -> 465
      //   436: getstatic okhttp3/internal/http2/Header.TARGET_AUTHORITY : Lokio/ByteString;
      //   439: aload #5
      //   441: invokevirtual equals : (Ljava/lang/Object;)Z
      //   444: ifne -> 465
      //   447: aload_0
      //   448: iload #7
      //   450: bipush #15
      //   452: iconst_0
      //   453: invokevirtual writeInt : (III)V
      //   456: aload_0
      //   457: aload #6
      //   459: invokevirtual writeByteString : (Lokio/ByteString;)V
      //   462: goto -> 310
      //   465: aload_0
      //   466: iload #7
      //   468: bipush #63
      //   470: bipush #64
      //   472: invokevirtual writeInt : (III)V
      //   475: aload_0
      //   476: aload #6
      //   478: invokevirtual writeByteString : (Lokio/ByteString;)V
      //   481: aload_0
      //   482: aload #4
      //   484: invokespecial insertIntoDynamicTable : (Lokhttp3/internal/http2/Header;)V
      //   487: goto -> 310
      //   490: return
    }
    
    void writeInt(int param1Int1, int param1Int2, int param1Int3) {
      if (param1Int1 < param1Int2) {
        this.out.writeByte(param1Int3 | param1Int1);
        return;
      } 
      this.out.writeByte(param1Int3 | param1Int2);
      for (param1Int1 -= param1Int2; param1Int1 >= 128; param1Int1 >>>= 7)
        this.out.writeByte(param1Int1 & 0x7F | 0x80); 
      this.out.writeByte(param1Int1);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/http2/Hpack.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */