package okhttp3;

import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public final class CacheControl {
  public static final CacheControl FORCE_CACHE;
  
  public static final CacheControl FORCE_NETWORK = (new Builder()).noCache().build();
  
  @Nullable
  String headerValue;
  
  private final boolean immutable;
  
  private final boolean isPrivate;
  
  private final boolean isPublic;
  
  private final int maxAgeSeconds;
  
  private final int maxStaleSeconds;
  
  private final int minFreshSeconds;
  
  private final boolean mustRevalidate;
  
  private final boolean noCache;
  
  private final boolean noStore;
  
  private final boolean noTransform;
  
  private final boolean onlyIfCached;
  
  private final int sMaxAgeSeconds;
  
  static {
    FORCE_CACHE = (new Builder()).onlyIfCached().maxStale(2147483647, TimeUnit.SECONDS).build();
  }
  
  CacheControl(Builder paramBuilder) {
    this.noCache = paramBuilder.noCache;
    this.noStore = paramBuilder.noStore;
    this.maxAgeSeconds = paramBuilder.maxAgeSeconds;
    this.sMaxAgeSeconds = -1;
    this.isPrivate = false;
    this.isPublic = false;
    this.mustRevalidate = false;
    this.maxStaleSeconds = paramBuilder.maxStaleSeconds;
    this.minFreshSeconds = paramBuilder.minFreshSeconds;
    this.onlyIfCached = paramBuilder.onlyIfCached;
    this.noTransform = paramBuilder.noTransform;
    this.immutable = paramBuilder.immutable;
  }
  
  private CacheControl(boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, int paramInt3, int paramInt4, boolean paramBoolean6, boolean paramBoolean7, boolean paramBoolean8, @Nullable String paramString) {
    this.noCache = paramBoolean1;
    this.noStore = paramBoolean2;
    this.maxAgeSeconds = paramInt1;
    this.sMaxAgeSeconds = paramInt2;
    this.isPrivate = paramBoolean3;
    this.isPublic = paramBoolean4;
    this.mustRevalidate = paramBoolean5;
    this.maxStaleSeconds = paramInt3;
    this.minFreshSeconds = paramInt4;
    this.onlyIfCached = paramBoolean6;
    this.noTransform = paramBoolean7;
    this.immutable = paramBoolean8;
    this.headerValue = paramString;
  }
  
  private String headerValue() {
    StringBuilder stringBuilder = new StringBuilder();
    if (this.noCache)
      stringBuilder.append("no-cache, "); 
    if (this.noStore)
      stringBuilder.append("no-store, "); 
    if (this.maxAgeSeconds != -1)
      stringBuilder.append("max-age=").append(this.maxAgeSeconds).append(", "); 
    if (this.sMaxAgeSeconds != -1)
      stringBuilder.append("s-maxage=").append(this.sMaxAgeSeconds).append(", "); 
    if (this.isPrivate)
      stringBuilder.append("private, "); 
    if (this.isPublic)
      stringBuilder.append("public, "); 
    if (this.mustRevalidate)
      stringBuilder.append("must-revalidate, "); 
    if (this.maxStaleSeconds != -1)
      stringBuilder.append("max-stale=").append(this.maxStaleSeconds).append(", "); 
    if (this.minFreshSeconds != -1)
      stringBuilder.append("min-fresh=").append(this.minFreshSeconds).append(", "); 
    if (this.onlyIfCached)
      stringBuilder.append("only-if-cached, "); 
    if (this.noTransform)
      stringBuilder.append("no-transform, "); 
    if (this.immutable)
      stringBuilder.append("immutable, "); 
    if (stringBuilder.length() == 0)
      return ""; 
    stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
    return stringBuilder.toString();
  }
  
  public static CacheControl parse(Headers paramHeaders) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: iconst_0
    //   3: istore_2
    //   4: iconst_m1
    //   5: istore_3
    //   6: iconst_m1
    //   7: istore #4
    //   9: iconst_0
    //   10: istore #5
    //   12: iconst_0
    //   13: istore #6
    //   15: iconst_0
    //   16: istore #7
    //   18: iconst_m1
    //   19: istore #8
    //   21: iconst_m1
    //   22: istore #9
    //   24: iconst_0
    //   25: istore #10
    //   27: iconst_0
    //   28: istore #11
    //   30: iconst_0
    //   31: istore #12
    //   33: iconst_1
    //   34: istore #13
    //   36: aconst_null
    //   37: astore #14
    //   39: iconst_0
    //   40: istore #15
    //   42: aload_0
    //   43: invokevirtual size : ()I
    //   46: istore #16
    //   48: iload #15
    //   50: iload #16
    //   52: if_icmpge -> 715
    //   55: aload_0
    //   56: iload #15
    //   58: invokevirtual name : (I)Ljava/lang/String;
    //   61: astore #17
    //   63: aload_0
    //   64: iload #15
    //   66: invokevirtual value : (I)Ljava/lang/String;
    //   69: astore #18
    //   71: aload #17
    //   73: ldc 'Cache-Control'
    //   75: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   78: ifeq -> 246
    //   81: aload #14
    //   83: ifnull -> 239
    //   86: iconst_0
    //   87: istore #13
    //   89: iconst_0
    //   90: istore #19
    //   92: iload_1
    //   93: istore #20
    //   95: iload_2
    //   96: istore #21
    //   98: iload_3
    //   99: istore #22
    //   101: iload #4
    //   103: istore #23
    //   105: iload #5
    //   107: istore #24
    //   109: iload #6
    //   111: istore #25
    //   113: iload #7
    //   115: istore #26
    //   117: iload #8
    //   119: istore #27
    //   121: iload #9
    //   123: istore #28
    //   125: iload #10
    //   127: istore #29
    //   129: iload #11
    //   131: istore #30
    //   133: iload #12
    //   135: istore #31
    //   137: aload #14
    //   139: astore #32
    //   141: iload #13
    //   143: istore #33
    //   145: iload #19
    //   147: aload #18
    //   149: invokevirtual length : ()I
    //   152: if_icmpge -> 656
    //   155: aload #18
    //   157: iload #19
    //   159: ldc '=,;'
    //   161: invokestatic skipUntil : (Ljava/lang/String;ILjava/lang/String;)I
    //   164: istore #28
    //   166: aload #18
    //   168: iload #19
    //   170: iload #28
    //   172: invokevirtual substring : (II)Ljava/lang/String;
    //   175: invokevirtual trim : ()Ljava/lang/String;
    //   178: astore #17
    //   180: iload #28
    //   182: aload #18
    //   184: invokevirtual length : ()I
    //   187: if_icmpeq -> 214
    //   190: aload #18
    //   192: iload #28
    //   194: invokevirtual charAt : (I)C
    //   197: bipush #44
    //   199: if_icmpeq -> 214
    //   202: aload #18
    //   204: iload #28
    //   206: invokevirtual charAt : (I)C
    //   209: bipush #59
    //   211: if_icmpne -> 315
    //   214: iinc #28, 1
    //   217: aconst_null
    //   218: astore #32
    //   220: ldc 'no-cache'
    //   222: aload #17
    //   224: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   227: ifeq -> 413
    //   230: iconst_1
    //   231: istore_1
    //   232: iload #28
    //   234: istore #19
    //   236: goto -> 92
    //   239: aload #18
    //   241: astore #14
    //   243: goto -> 89
    //   246: iload_1
    //   247: istore #20
    //   249: iload_2
    //   250: istore #21
    //   252: iload_3
    //   253: istore #22
    //   255: iload #4
    //   257: istore #23
    //   259: iload #5
    //   261: istore #24
    //   263: iload #6
    //   265: istore #25
    //   267: iload #7
    //   269: istore #26
    //   271: iload #8
    //   273: istore #27
    //   275: iload #9
    //   277: istore #28
    //   279: iload #10
    //   281: istore #29
    //   283: iload #11
    //   285: istore #30
    //   287: iload #12
    //   289: istore #31
    //   291: aload #14
    //   293: astore #32
    //   295: iload #13
    //   297: istore #33
    //   299: aload #17
    //   301: ldc 'Pragma'
    //   303: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   306: ifeq -> 656
    //   309: iconst_0
    //   310: istore #13
    //   312: goto -> 89
    //   315: aload #18
    //   317: iload #28
    //   319: iconst_1
    //   320: iadd
    //   321: invokestatic skipWhitespace : (Ljava/lang/String;I)I
    //   324: istore #19
    //   326: iload #19
    //   328: aload #18
    //   330: invokevirtual length : ()I
    //   333: if_icmpge -> 385
    //   336: aload #18
    //   338: iload #19
    //   340: invokevirtual charAt : (I)C
    //   343: bipush #34
    //   345: if_icmpne -> 385
    //   348: iload #19
    //   350: iconst_1
    //   351: iadd
    //   352: istore #28
    //   354: aload #18
    //   356: iload #28
    //   358: ldc '"'
    //   360: invokestatic skipUntil : (Ljava/lang/String;ILjava/lang/String;)I
    //   363: istore #19
    //   365: aload #18
    //   367: iload #28
    //   369: iload #19
    //   371: invokevirtual substring : (II)Ljava/lang/String;
    //   374: astore #32
    //   376: iload #19
    //   378: iconst_1
    //   379: iadd
    //   380: istore #28
    //   382: goto -> 220
    //   385: aload #18
    //   387: iload #19
    //   389: ldc ',;'
    //   391: invokestatic skipUntil : (Ljava/lang/String;ILjava/lang/String;)I
    //   394: istore #28
    //   396: aload #18
    //   398: iload #19
    //   400: iload #28
    //   402: invokevirtual substring : (II)Ljava/lang/String;
    //   405: invokevirtual trim : ()Ljava/lang/String;
    //   408: astore #32
    //   410: goto -> 220
    //   413: ldc 'no-store'
    //   415: aload #17
    //   417: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   420: ifeq -> 432
    //   423: iconst_1
    //   424: istore_2
    //   425: iload #28
    //   427: istore #19
    //   429: goto -> 92
    //   432: ldc 'max-age'
    //   434: aload #17
    //   436: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   439: ifeq -> 456
    //   442: aload #32
    //   444: iconst_m1
    //   445: invokestatic parseSeconds : (Ljava/lang/String;I)I
    //   448: istore_3
    //   449: iload #28
    //   451: istore #19
    //   453: goto -> 92
    //   456: ldc 's-maxage'
    //   458: aload #17
    //   460: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   463: ifeq -> 481
    //   466: aload #32
    //   468: iconst_m1
    //   469: invokestatic parseSeconds : (Ljava/lang/String;I)I
    //   472: istore #4
    //   474: iload #28
    //   476: istore #19
    //   478: goto -> 92
    //   481: ldc 'private'
    //   483: aload #17
    //   485: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   488: ifeq -> 501
    //   491: iconst_1
    //   492: istore #5
    //   494: iload #28
    //   496: istore #19
    //   498: goto -> 92
    //   501: ldc 'public'
    //   503: aload #17
    //   505: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   508: ifeq -> 521
    //   511: iconst_1
    //   512: istore #6
    //   514: iload #28
    //   516: istore #19
    //   518: goto -> 92
    //   521: ldc 'must-revalidate'
    //   523: aload #17
    //   525: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   528: ifeq -> 541
    //   531: iconst_1
    //   532: istore #7
    //   534: iload #28
    //   536: istore #19
    //   538: goto -> 92
    //   541: ldc 'max-stale'
    //   543: aload #17
    //   545: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   548: ifeq -> 567
    //   551: aload #32
    //   553: ldc 2147483647
    //   555: invokestatic parseSeconds : (Ljava/lang/String;I)I
    //   558: istore #8
    //   560: iload #28
    //   562: istore #19
    //   564: goto -> 92
    //   567: ldc 'min-fresh'
    //   569: aload #17
    //   571: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   574: ifeq -> 592
    //   577: aload #32
    //   579: iconst_m1
    //   580: invokestatic parseSeconds : (Ljava/lang/String;I)I
    //   583: istore #9
    //   585: iload #28
    //   587: istore #19
    //   589: goto -> 92
    //   592: ldc 'only-if-cached'
    //   594: aload #17
    //   596: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   599: ifeq -> 612
    //   602: iconst_1
    //   603: istore #10
    //   605: iload #28
    //   607: istore #19
    //   609: goto -> 92
    //   612: ldc 'no-transform'
    //   614: aload #17
    //   616: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   619: ifeq -> 632
    //   622: iconst_1
    //   623: istore #11
    //   625: iload #28
    //   627: istore #19
    //   629: goto -> 92
    //   632: iload #28
    //   634: istore #19
    //   636: ldc 'immutable'
    //   638: aload #17
    //   640: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   643: ifeq -> 92
    //   646: iconst_1
    //   647: istore #12
    //   649: iload #28
    //   651: istore #19
    //   653: goto -> 92
    //   656: iinc #15, 1
    //   659: iload #20
    //   661: istore_1
    //   662: iload #21
    //   664: istore_2
    //   665: iload #22
    //   667: istore_3
    //   668: iload #23
    //   670: istore #4
    //   672: iload #24
    //   674: istore #5
    //   676: iload #25
    //   678: istore #6
    //   680: iload #26
    //   682: istore #7
    //   684: iload #27
    //   686: istore #8
    //   688: iload #28
    //   690: istore #9
    //   692: iload #29
    //   694: istore #10
    //   696: iload #30
    //   698: istore #11
    //   700: iload #31
    //   702: istore #12
    //   704: aload #32
    //   706: astore #14
    //   708: iload #33
    //   710: istore #13
    //   712: goto -> 48
    //   715: iload #13
    //   717: ifne -> 723
    //   720: aconst_null
    //   721: astore #14
    //   723: new okhttp3/CacheControl
    //   726: dup
    //   727: iload_1
    //   728: iload_2
    //   729: iload_3
    //   730: iload #4
    //   732: iload #5
    //   734: iload #6
    //   736: iload #7
    //   738: iload #8
    //   740: iload #9
    //   742: iload #10
    //   744: iload #11
    //   746: iload #12
    //   748: aload #14
    //   750: invokespecial <init> : (ZZIIZZZIIZZZLjava/lang/String;)V
    //   753: areturn
  }
  
  public boolean immutable() {
    return this.immutable;
  }
  
  public boolean isPrivate() {
    return this.isPrivate;
  }
  
  public boolean isPublic() {
    return this.isPublic;
  }
  
  public int maxAgeSeconds() {
    return this.maxAgeSeconds;
  }
  
  public int maxStaleSeconds() {
    return this.maxStaleSeconds;
  }
  
  public int minFreshSeconds() {
    return this.minFreshSeconds;
  }
  
  public boolean mustRevalidate() {
    return this.mustRevalidate;
  }
  
  public boolean noCache() {
    return this.noCache;
  }
  
  public boolean noStore() {
    return this.noStore;
  }
  
  public boolean noTransform() {
    return this.noTransform;
  }
  
  public boolean onlyIfCached() {
    return this.onlyIfCached;
  }
  
  public int sMaxAgeSeconds() {
    return this.sMaxAgeSeconds;
  }
  
  public String toString() {
    String str = this.headerValue;
    if (str == null) {
      str = headerValue();
      this.headerValue = str;
    } 
    return str;
  }
  
  public static final class Builder {
    boolean immutable;
    
    int maxAgeSeconds = -1;
    
    int maxStaleSeconds = -1;
    
    int minFreshSeconds = -1;
    
    boolean noCache;
    
    boolean noStore;
    
    boolean noTransform;
    
    boolean onlyIfCached;
    
    public CacheControl build() {
      return new CacheControl(this);
    }
    
    public Builder immutable() {
      this.immutable = true;
      return this;
    }
    
    public Builder maxAge(int param1Int, TimeUnit param1TimeUnit) {
      if (param1Int < 0)
        throw new IllegalArgumentException("maxAge < 0: " + param1Int); 
      long l = param1TimeUnit.toSeconds(param1Int);
      if (l > 2147483647L) {
        param1Int = Integer.MAX_VALUE;
        this.maxAgeSeconds = param1Int;
        return this;
      } 
      param1Int = (int)l;
      this.maxAgeSeconds = param1Int;
      return this;
    }
    
    public Builder maxStale(int param1Int, TimeUnit param1TimeUnit) {
      if (param1Int < 0)
        throw new IllegalArgumentException("maxStale < 0: " + param1Int); 
      long l = param1TimeUnit.toSeconds(param1Int);
      if (l > 2147483647L) {
        param1Int = Integer.MAX_VALUE;
        this.maxStaleSeconds = param1Int;
        return this;
      } 
      param1Int = (int)l;
      this.maxStaleSeconds = param1Int;
      return this;
    }
    
    public Builder minFresh(int param1Int, TimeUnit param1TimeUnit) {
      if (param1Int < 0)
        throw new IllegalArgumentException("minFresh < 0: " + param1Int); 
      long l = param1TimeUnit.toSeconds(param1Int);
      if (l > 2147483647L) {
        param1Int = Integer.MAX_VALUE;
        this.minFreshSeconds = param1Int;
        return this;
      } 
      param1Int = (int)l;
      this.minFreshSeconds = param1Int;
      return this;
    }
    
    public Builder noCache() {
      this.noCache = true;
      return this;
    }
    
    public Builder noStore() {
      this.noStore = true;
      return this;
    }
    
    public Builder noTransform() {
      this.noTransform = true;
      return this;
    }
    
    public Builder onlyIfCached() {
      this.onlyIfCached = true;
      return this;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/CacheControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */