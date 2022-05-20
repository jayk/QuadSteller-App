package okhttp3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpDate;

public final class Cookie {
  private static final Pattern DAY_OF_MONTH_PATTERN;
  
  private static final Pattern MONTH_PATTERN;
  
  private static final Pattern TIME_PATTERN;
  
  private static final Pattern YEAR_PATTERN = Pattern.compile("(\\d{2,4})[^\\d]*");
  
  private final String domain;
  
  private final long expiresAt;
  
  private final boolean hostOnly;
  
  private final boolean httpOnly;
  
  private final String name;
  
  private final String path;
  
  private final boolean persistent;
  
  private final boolean secure;
  
  private final String value;
  
  static {
    MONTH_PATTERN = Pattern.compile("(?i)(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec).*");
    DAY_OF_MONTH_PATTERN = Pattern.compile("(\\d{1,2})[^\\d]*");
    TIME_PATTERN = Pattern.compile("(\\d{1,2}):(\\d{1,2}):(\\d{1,2})[^\\d]*");
  }
  
  private Cookie(String paramString1, String paramString2, long paramLong, String paramString3, String paramString4, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    this.name = paramString1;
    this.value = paramString2;
    this.expiresAt = paramLong;
    this.domain = paramString3;
    this.path = paramString4;
    this.secure = paramBoolean1;
    this.httpOnly = paramBoolean2;
    this.hostOnly = paramBoolean3;
    this.persistent = paramBoolean4;
  }
  
  Cookie(Builder paramBuilder) {
    if (paramBuilder.name == null)
      throw new NullPointerException("builder.name == null"); 
    if (paramBuilder.value == null)
      throw new NullPointerException("builder.value == null"); 
    if (paramBuilder.domain == null)
      throw new NullPointerException("builder.domain == null"); 
    this.name = paramBuilder.name;
    this.value = paramBuilder.value;
    this.expiresAt = paramBuilder.expiresAt;
    this.domain = paramBuilder.domain;
    this.path = paramBuilder.path;
    this.secure = paramBuilder.secure;
    this.httpOnly = paramBuilder.httpOnly;
    this.persistent = paramBuilder.persistent;
    this.hostOnly = paramBuilder.hostOnly;
  }
  
  private static int dateCharacterOffset(String paramString, int paramInt1, int paramInt2, boolean paramBoolean) {
    // Byte code:
    //   0: iload_1
    //   1: iload_2
    //   2: if_icmpge -> 119
    //   5: aload_0
    //   6: iload_1
    //   7: invokevirtual charAt : (I)C
    //   10: istore #4
    //   12: iload #4
    //   14: bipush #32
    //   16: if_icmpge -> 26
    //   19: iload #4
    //   21: bipush #9
    //   23: if_icmpne -> 82
    //   26: iload #4
    //   28: bipush #127
    //   30: if_icmpge -> 82
    //   33: iload #4
    //   35: bipush #48
    //   37: if_icmplt -> 47
    //   40: iload #4
    //   42: bipush #57
    //   44: if_icmple -> 82
    //   47: iload #4
    //   49: bipush #97
    //   51: if_icmplt -> 61
    //   54: iload #4
    //   56: bipush #122
    //   58: if_icmple -> 82
    //   61: iload #4
    //   63: bipush #65
    //   65: if_icmplt -> 75
    //   68: iload #4
    //   70: bipush #90
    //   72: if_icmple -> 82
    //   75: iload #4
    //   77: bipush #58
    //   79: if_icmpne -> 101
    //   82: iconst_1
    //   83: istore #4
    //   85: iload_3
    //   86: ifne -> 107
    //   89: iconst_1
    //   90: istore #5
    //   92: iload #4
    //   94: iload #5
    //   96: if_icmpne -> 113
    //   99: iload_1
    //   100: ireturn
    //   101: iconst_0
    //   102: istore #4
    //   104: goto -> 85
    //   107: iconst_0
    //   108: istore #5
    //   110: goto -> 92
    //   113: iinc #1, 1
    //   116: goto -> 0
    //   119: iload_2
    //   120: istore_1
    //   121: goto -> 99
  }
  
  private static boolean domainMatch(String paramString1, String paramString2) {
    boolean bool = true;
    if (!paramString1.equals(paramString2) && (!paramString1.endsWith(paramString2) || paramString1.charAt(paramString1.length() - paramString2.length() - 1) != '.' || Util.verifyAsIpAddress(paramString1)))
      bool = false; 
    return bool;
  }
  
  @Nullable
  static Cookie parse(long paramLong, HttpUrl paramHttpUrl, String paramString) {
    // Byte code:
    //   0: aload_3
    //   1: invokevirtual length : ()I
    //   4: istore #4
    //   6: aload_3
    //   7: iconst_0
    //   8: iload #4
    //   10: bipush #59
    //   12: invokestatic delimiterOffset : (Ljava/lang/String;IIC)I
    //   15: istore #5
    //   17: aload_3
    //   18: iconst_0
    //   19: iload #5
    //   21: bipush #61
    //   23: invokestatic delimiterOffset : (Ljava/lang/String;IIC)I
    //   26: istore #6
    //   28: iload #6
    //   30: iload #5
    //   32: if_icmpne -> 39
    //   35: aconst_null
    //   36: astore_2
    //   37: aload_2
    //   38: areturn
    //   39: aload_3
    //   40: iconst_0
    //   41: iload #6
    //   43: invokestatic trimSubstring : (Ljava/lang/String;II)Ljava/lang/String;
    //   46: astore #7
    //   48: aload #7
    //   50: invokevirtual isEmpty : ()Z
    //   53: ifne -> 65
    //   56: aload #7
    //   58: invokestatic indexOfControlOrNonAscii : (Ljava/lang/String;)I
    //   61: iconst_m1
    //   62: if_icmpeq -> 70
    //   65: aconst_null
    //   66: astore_2
    //   67: goto -> 37
    //   70: aload_3
    //   71: iload #6
    //   73: iconst_1
    //   74: iadd
    //   75: iload #5
    //   77: invokestatic trimSubstring : (Ljava/lang/String;II)Ljava/lang/String;
    //   80: astore #8
    //   82: aload #8
    //   84: invokestatic indexOfControlOrNonAscii : (Ljava/lang/String;)I
    //   87: iconst_m1
    //   88: if_icmpeq -> 96
    //   91: aconst_null
    //   92: astore_2
    //   93: goto -> 37
    //   96: ldc2_w 253402300799999
    //   99: lstore #9
    //   101: ldc2_w -1
    //   104: lstore #11
    //   106: aconst_null
    //   107: astore #13
    //   109: aconst_null
    //   110: astore #14
    //   112: iconst_0
    //   113: istore #15
    //   115: iconst_0
    //   116: istore #16
    //   118: iconst_1
    //   119: istore #17
    //   121: iconst_0
    //   122: istore #18
    //   124: iinc #5, 1
    //   127: iload #5
    //   129: iload #4
    //   131: if_icmpge -> 516
    //   134: aload_3
    //   135: iload #5
    //   137: iload #4
    //   139: bipush #59
    //   141: invokestatic delimiterOffset : (Ljava/lang/String;IIC)I
    //   144: istore #6
    //   146: aload_3
    //   147: iload #5
    //   149: iload #6
    //   151: bipush #61
    //   153: invokestatic delimiterOffset : (Ljava/lang/String;IIC)I
    //   156: istore #19
    //   158: aload_3
    //   159: iload #5
    //   161: iload #19
    //   163: invokestatic trimSubstring : (Ljava/lang/String;II)Ljava/lang/String;
    //   166: astore #20
    //   168: iload #19
    //   170: iload #6
    //   172: if_icmpge -> 270
    //   175: aload_3
    //   176: iload #19
    //   178: iconst_1
    //   179: iadd
    //   180: iload #6
    //   182: invokestatic trimSubstring : (Ljava/lang/String;II)Ljava/lang/String;
    //   185: astore #21
    //   187: aload #20
    //   189: ldc 'expires'
    //   191: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   194: ifeq -> 277
    //   197: aload #21
    //   199: iconst_0
    //   200: aload #21
    //   202: invokevirtual length : ()I
    //   205: invokestatic parseExpires : (Ljava/lang/String;II)J
    //   208: lstore #22
    //   210: iconst_1
    //   211: istore #24
    //   213: lload #11
    //   215: lstore #25
    //   217: iload #17
    //   219: istore #27
    //   221: iload #15
    //   223: istore #28
    //   225: aload #14
    //   227: astore #29
    //   229: aload #13
    //   231: astore #21
    //   233: iload #6
    //   235: iconst_1
    //   236: iadd
    //   237: istore #5
    //   239: lload #22
    //   241: lstore #9
    //   243: aload #21
    //   245: astore #13
    //   247: aload #29
    //   249: astore #14
    //   251: iload #28
    //   253: istore #15
    //   255: iload #27
    //   257: istore #17
    //   259: iload #24
    //   261: istore #18
    //   263: lload #25
    //   265: lstore #11
    //   267: goto -> 127
    //   270: ldc ''
    //   272: astore #21
    //   274: goto -> 187
    //   277: aload #20
    //   279: ldc 'max-age'
    //   281: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   284: ifeq -> 320
    //   287: aload #21
    //   289: invokestatic parseMaxAge : (Ljava/lang/String;)J
    //   292: lstore #25
    //   294: iconst_1
    //   295: istore #24
    //   297: lload #9
    //   299: lstore #22
    //   301: aload #13
    //   303: astore #21
    //   305: aload #14
    //   307: astore #29
    //   309: iload #15
    //   311: istore #28
    //   313: iload #17
    //   315: istore #27
    //   317: goto -> 233
    //   320: aload #20
    //   322: ldc 'domain'
    //   324: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   327: ifeq -> 363
    //   330: aload #21
    //   332: invokestatic parseDomain : (Ljava/lang/String;)Ljava/lang/String;
    //   335: astore #21
    //   337: iconst_0
    //   338: istore #27
    //   340: lload #9
    //   342: lstore #22
    //   344: aload #14
    //   346: astore #29
    //   348: iload #15
    //   350: istore #28
    //   352: iload #18
    //   354: istore #24
    //   356: lload #11
    //   358: lstore #25
    //   360: goto -> 233
    //   363: aload #20
    //   365: ldc 'path'
    //   367: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   370: ifeq -> 404
    //   373: aload #21
    //   375: astore #29
    //   377: lload #9
    //   379: lstore #22
    //   381: aload #13
    //   383: astore #21
    //   385: iload #15
    //   387: istore #28
    //   389: iload #17
    //   391: istore #27
    //   393: iload #18
    //   395: istore #24
    //   397: lload #11
    //   399: lstore #25
    //   401: goto -> 233
    //   404: aload #20
    //   406: ldc 'secure'
    //   408: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   411: ifeq -> 444
    //   414: iconst_1
    //   415: istore #28
    //   417: lload #9
    //   419: lstore #22
    //   421: aload #13
    //   423: astore #21
    //   425: aload #14
    //   427: astore #29
    //   429: iload #17
    //   431: istore #27
    //   433: iload #18
    //   435: istore #24
    //   437: lload #11
    //   439: lstore #25
    //   441: goto -> 233
    //   444: lload #9
    //   446: lstore #22
    //   448: aload #13
    //   450: astore #21
    //   452: aload #14
    //   454: astore #29
    //   456: iload #15
    //   458: istore #28
    //   460: iload #17
    //   462: istore #27
    //   464: iload #18
    //   466: istore #24
    //   468: lload #11
    //   470: lstore #25
    //   472: aload #20
    //   474: ldc 'httponly'
    //   476: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   479: ifeq -> 233
    //   482: iconst_1
    //   483: istore #16
    //   485: lload #9
    //   487: lstore #22
    //   489: aload #13
    //   491: astore #21
    //   493: aload #14
    //   495: astore #29
    //   497: iload #15
    //   499: istore #28
    //   501: iload #17
    //   503: istore #27
    //   505: iload #18
    //   507: istore #24
    //   509: lload #11
    //   511: lstore #25
    //   513: goto -> 233
    //   516: lload #11
    //   518: ldc2_w -9223372036854775808
    //   521: lcmp
    //   522: ifne -> 571
    //   525: ldc2_w -9223372036854775808
    //   528: lstore #9
    //   530: aload_2
    //   531: invokevirtual host : ()Ljava/lang/String;
    //   534: astore_3
    //   535: aload #13
    //   537: ifnonnull -> 639
    //   540: aload_3
    //   541: astore #21
    //   543: aload_3
    //   544: invokevirtual length : ()I
    //   547: aload #21
    //   549: invokevirtual length : ()I
    //   552: if_icmpeq -> 657
    //   555: invokestatic get : ()Lokhttp3/internal/publicsuffix/PublicSuffixDatabase;
    //   558: aload #21
    //   560: invokevirtual getEffectiveTldPlusOne : (Ljava/lang/String;)Ljava/lang/String;
    //   563: ifnonnull -> 657
    //   566: aconst_null
    //   567: astore_2
    //   568: goto -> 37
    //   571: lload #11
    //   573: ldc2_w -1
    //   576: lcmp
    //   577: ifeq -> 530
    //   580: lload #11
    //   582: ldc2_w 9223372036854775
    //   585: lcmp
    //   586: ifgt -> 631
    //   589: lload #11
    //   591: ldc2_w 1000
    //   594: lmul
    //   595: lstore #9
    //   597: lload_0
    //   598: lload #9
    //   600: ladd
    //   601: lstore #11
    //   603: lload #11
    //   605: lload_0
    //   606: lcmp
    //   607: iflt -> 623
    //   610: lload #11
    //   612: lstore #9
    //   614: lload #11
    //   616: ldc2_w 253402300799999
    //   619: lcmp
    //   620: ifle -> 530
    //   623: ldc2_w 253402300799999
    //   626: lstore #9
    //   628: goto -> 530
    //   631: ldc2_w 9223372036854775807
    //   634: lstore #9
    //   636: goto -> 597
    //   639: aload #13
    //   641: astore #21
    //   643: aload_3
    //   644: aload #13
    //   646: invokestatic domainMatch : (Ljava/lang/String;Ljava/lang/String;)Z
    //   649: ifne -> 543
    //   652: aconst_null
    //   653: astore_2
    //   654: goto -> 37
    //   657: aload #14
    //   659: ifnull -> 675
    //   662: aload #14
    //   664: astore_3
    //   665: aload #14
    //   667: ldc '/'
    //   669: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   672: ifne -> 701
    //   675: aload_2
    //   676: invokevirtual encodedPath : ()Ljava/lang/String;
    //   679: astore_2
    //   680: aload_2
    //   681: bipush #47
    //   683: invokevirtual lastIndexOf : (I)I
    //   686: istore #5
    //   688: iload #5
    //   690: ifeq -> 729
    //   693: aload_2
    //   694: iconst_0
    //   695: iload #5
    //   697: invokevirtual substring : (II)Ljava/lang/String;
    //   700: astore_3
    //   701: new okhttp3/Cookie
    //   704: dup
    //   705: aload #7
    //   707: aload #8
    //   709: lload #9
    //   711: aload #21
    //   713: aload_3
    //   714: iload #15
    //   716: iload #16
    //   718: iload #17
    //   720: iload #18
    //   722: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;ZZZZ)V
    //   725: astore_2
    //   726: goto -> 37
    //   729: ldc '/'
    //   731: astore_3
    //   732: goto -> 701
    //   735: astore #21
    //   737: lload #9
    //   739: lstore #22
    //   741: aload #13
    //   743: astore #21
    //   745: aload #14
    //   747: astore #29
    //   749: iload #15
    //   751: istore #28
    //   753: iload #17
    //   755: istore #27
    //   757: iload #18
    //   759: istore #24
    //   761: lload #11
    //   763: lstore #25
    //   765: goto -> 233
    //   768: astore #21
    //   770: lload #9
    //   772: lstore #22
    //   774: aload #13
    //   776: astore #21
    //   778: aload #14
    //   780: astore #29
    //   782: iload #15
    //   784: istore #28
    //   786: iload #17
    //   788: istore #27
    //   790: iload #18
    //   792: istore #24
    //   794: lload #11
    //   796: lstore #25
    //   798: goto -> 233
    //   801: astore #21
    //   803: lload #9
    //   805: lstore #22
    //   807: aload #13
    //   809: astore #21
    //   811: aload #14
    //   813: astore #29
    //   815: iload #15
    //   817: istore #28
    //   819: iload #17
    //   821: istore #27
    //   823: iload #18
    //   825: istore #24
    //   827: lload #11
    //   829: lstore #25
    //   831: goto -> 233
    // Exception table:
    //   from	to	target	type
    //   197	210	801	java/lang/IllegalArgumentException
    //   287	294	735	java/lang/NumberFormatException
    //   330	337	768	java/lang/IllegalArgumentException
  }
  
  @Nullable
  public static Cookie parse(HttpUrl paramHttpUrl, String paramString) {
    return parse(System.currentTimeMillis(), paramHttpUrl, paramString);
  }
  
  public static List<Cookie> parseAll(HttpUrl paramHttpUrl, Headers paramHeaders) {
    ArrayList<Cookie> arrayList;
    List<String> list = paramHeaders.values("Set-Cookie");
    paramHeaders = null;
    byte b = 0;
    int i = list.size();
    while (b < i) {
      Cookie cookie = parse(paramHttpUrl, list.get(b));
      if (cookie != null) {
        ArrayList<Cookie> arrayList1;
        Headers headers = paramHeaders;
        if (paramHeaders == null)
          arrayList1 = new ArrayList(); 
        arrayList1.add(cookie);
        arrayList = arrayList1;
      } 
      b++;
    } 
    return (List)((arrayList != null) ? Collections.unmodifiableList(arrayList) : Collections.emptyList());
  }
  
  private static String parseDomain(String paramString) {
    if (paramString.endsWith("."))
      throw new IllegalArgumentException(); 
    String str = paramString;
    if (paramString.startsWith("."))
      str = paramString.substring(1); 
    paramString = Util.domainToAscii(str);
    if (paramString == null)
      throw new IllegalArgumentException(); 
    return paramString;
  }
  
  private static long parseExpires(String paramString, int paramInt1, int paramInt2) {
    int i = dateCharacterOffset(paramString, paramInt1, paramInt2, false);
    byte b1 = -1;
    byte b2 = -1;
    int j = -1;
    byte b3 = -1;
    byte b4 = -1;
    paramInt1 = -1;
    Matcher matcher = TIME_PATTERN.matcher(paramString);
    while (i < paramInt2) {
      byte b5;
      byte b6;
      int m;
      byte b7;
      byte b8;
      int k = dateCharacterOffset(paramString, i + 1, paramInt2, true);
      matcher.region(i, k);
      if (b1 == -1 && matcher.usePattern(TIME_PATTERN).matches()) {
        b5 = Integer.parseInt(matcher.group(1));
        b6 = Integer.parseInt(matcher.group(2));
        i = Integer.parseInt(matcher.group(3));
        m = paramInt1;
        b7 = b4;
        b8 = b3;
      } else if (b3 == -1 && matcher.usePattern(DAY_OF_MONTH_PATTERN).matches()) {
        b8 = Integer.parseInt(matcher.group(1));
        b5 = b1;
        b6 = b2;
        b7 = b4;
        i = j;
        m = paramInt1;
      } else if (b4 == -1 && matcher.usePattern(MONTH_PATTERN).matches()) {
        String str = matcher.group(1).toLowerCase(Locale.US);
        b7 = MONTH_PATTERN.pattern().indexOf(str) / 4;
        b8 = b3;
        b5 = b1;
        b6 = b2;
        i = j;
        m = paramInt1;
      } else {
        b8 = b3;
        b5 = b1;
        b6 = b2;
        b7 = b4;
        i = j;
        m = paramInt1;
        if (paramInt1 == -1) {
          b8 = b3;
          b5 = b1;
          b6 = b2;
          b7 = b4;
          i = j;
          m = paramInt1;
          if (matcher.usePattern(YEAR_PATTERN).matches()) {
            m = Integer.parseInt(matcher.group(1));
            b8 = b3;
            b5 = b1;
            b6 = b2;
            b7 = b4;
            i = j;
          } 
        } 
      } 
      k = dateCharacterOffset(paramString, k + 1, paramInt2, false);
      b3 = b8;
      b1 = b5;
      b2 = b6;
      b4 = b7;
      j = i;
      paramInt1 = m;
      i = k;
    } 
    paramInt2 = paramInt1;
    if (paramInt1 >= 70) {
      paramInt2 = paramInt1;
      if (paramInt1 <= 99)
        paramInt2 = paramInt1 + 1900; 
    } 
    paramInt1 = paramInt2;
    if (paramInt2 >= 0) {
      paramInt1 = paramInt2;
      if (paramInt2 <= 69)
        paramInt1 = paramInt2 + 2000; 
    } 
    if (paramInt1 < 1601)
      throw new IllegalArgumentException(); 
    if (b4 == -1)
      throw new IllegalArgumentException(); 
    if (b3 < 1 || b3 > 31)
      throw new IllegalArgumentException(); 
    if (b1 < 0 || b1 > 23)
      throw new IllegalArgumentException(); 
    if (b2 < 0 || b2 > 59)
      throw new IllegalArgumentException(); 
    if (j < 0 || j > 59)
      throw new IllegalArgumentException(); 
    GregorianCalendar gregorianCalendar = new GregorianCalendar(Util.UTC);
    gregorianCalendar.setLenient(false);
    gregorianCalendar.set(1, paramInt1);
    gregorianCalendar.set(2, b4 - 1);
    gregorianCalendar.set(5, b3);
    gregorianCalendar.set(11, b1);
    gregorianCalendar.set(12, b2);
    gregorianCalendar.set(13, j);
    gregorianCalendar.set(14, 0);
    return gregorianCalendar.getTimeInMillis();
  }
  
  private static long parseMaxAge(String paramString) {
    long l = Long.MIN_VALUE;
    try {
      long l1 = Long.parseLong(paramString);
      l = l1;
      if (l1 <= 0L)
        l = Long.MIN_VALUE; 
      return l;
    } catch (NumberFormatException numberFormatException) {
      if (paramString.matches("-?\\d+")) {
        if (!paramString.startsWith("-"))
          l = Long.MAX_VALUE; 
        return l;
      } 
      throw numberFormatException;
    } 
  }
  
  private static boolean pathMatch(HttpUrl paramHttpUrl, String paramString) {
    boolean bool = true;
    String str = paramHttpUrl.encodedPath();
    if (str.equals(paramString))
      return bool; 
    if (str.startsWith(paramString)) {
      boolean bool1 = bool;
      if (!paramString.endsWith("/")) {
        bool1 = bool;
        if (str.charAt(paramString.length()) != '/')
          return false; 
      } 
      return bool1;
    } 
    return false;
  }
  
  public String domain() {
    return this.domain;
  }
  
  public boolean equals(@Nullable Object paramObject) {
    boolean bool1 = false;
    if (!(paramObject instanceof Cookie))
      return bool1; 
    paramObject = paramObject;
    boolean bool2 = bool1;
    if (((Cookie)paramObject).name.equals(this.name)) {
      bool2 = bool1;
      if (((Cookie)paramObject).value.equals(this.value)) {
        bool2 = bool1;
        if (((Cookie)paramObject).domain.equals(this.domain)) {
          bool2 = bool1;
          if (((Cookie)paramObject).path.equals(this.path)) {
            bool2 = bool1;
            if (((Cookie)paramObject).expiresAt == this.expiresAt) {
              bool2 = bool1;
              if (((Cookie)paramObject).secure == this.secure) {
                bool2 = bool1;
                if (((Cookie)paramObject).httpOnly == this.httpOnly) {
                  bool2 = bool1;
                  if (((Cookie)paramObject).persistent == this.persistent) {
                    bool2 = bool1;
                    if (((Cookie)paramObject).hostOnly == this.hostOnly)
                      bool2 = true; 
                  } 
                } 
              } 
            } 
          } 
        } 
      } 
    } 
    return bool2;
  }
  
  public long expiresAt() {
    return this.expiresAt;
  }
  
  public int hashCode() {
    byte b2;
    byte b3;
    byte b4;
    byte b1 = 0;
    int i = this.name.hashCode();
    int j = this.value.hashCode();
    int k = this.domain.hashCode();
    int m = this.path.hashCode();
    int n = (int)(this.expiresAt ^ this.expiresAt >>> 32L);
    if (this.secure) {
      b2 = 0;
    } else {
      b2 = 1;
    } 
    if (this.httpOnly) {
      b3 = 0;
    } else {
      b3 = 1;
    } 
    if (this.persistent) {
      b4 = 0;
    } else {
      b4 = 1;
    } 
    if (!this.hostOnly)
      b1 = 1; 
    return ((((((((i + 527) * 31 + j) * 31 + k) * 31 + m) * 31 + n) * 31 + b2) * 31 + b3) * 31 + b4) * 31 + b1;
  }
  
  public boolean hostOnly() {
    return this.hostOnly;
  }
  
  public boolean httpOnly() {
    return this.httpOnly;
  }
  
  public boolean matches(HttpUrl paramHttpUrl) {
    boolean bool = false;
    if (this.hostOnly) {
      null = paramHttpUrl.host().equals(this.domain);
    } else {
      null = domainMatch(paramHttpUrl.host(), this.domain);
    } 
    if (!null)
      return bool; 
    null = bool;
    if (pathMatch(paramHttpUrl, this.path)) {
      if (this.secure) {
        null = bool;
        return paramHttpUrl.isHttps() ? true : null;
      } 
    } else {
      return null;
    } 
    return true;
  }
  
  public String name() {
    return this.name;
  }
  
  public String path() {
    return this.path;
  }
  
  public boolean persistent() {
    return this.persistent;
  }
  
  public boolean secure() {
    return this.secure;
  }
  
  public String toString() {
    return toString(false);
  }
  
  String toString(boolean paramBoolean) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.name);
    stringBuilder.append('=');
    stringBuilder.append(this.value);
    if (this.persistent)
      if (this.expiresAt == Long.MIN_VALUE) {
        stringBuilder.append("; max-age=0");
      } else {
        stringBuilder.append("; expires=").append(HttpDate.format(new Date(this.expiresAt)));
      }  
    if (!this.hostOnly) {
      stringBuilder.append("; domain=");
      if (paramBoolean)
        stringBuilder.append("."); 
      stringBuilder.append(this.domain);
    } 
    stringBuilder.append("; path=").append(this.path);
    if (this.secure)
      stringBuilder.append("; secure"); 
    if (this.httpOnly)
      stringBuilder.append("; httponly"); 
    return stringBuilder.toString();
  }
  
  public String value() {
    return this.value;
  }
  
  public static final class Builder {
    String domain;
    
    long expiresAt = 253402300799999L;
    
    boolean hostOnly;
    
    boolean httpOnly;
    
    String name;
    
    String path = "/";
    
    boolean persistent;
    
    boolean secure;
    
    String value;
    
    private Builder domain(String param1String, boolean param1Boolean) {
      if (param1String == null)
        throw new NullPointerException("domain == null"); 
      String str = Util.domainToAscii(param1String);
      if (str == null)
        throw new IllegalArgumentException("unexpected domain: " + param1String); 
      this.domain = str;
      this.hostOnly = param1Boolean;
      return this;
    }
    
    public Cookie build() {
      return new Cookie(this);
    }
    
    public Builder domain(String param1String) {
      return domain(param1String, false);
    }
    
    public Builder expiresAt(long param1Long) {
      long l = param1Long;
      if (param1Long <= 0L)
        l = Long.MIN_VALUE; 
      param1Long = l;
      if (l > 253402300799999L)
        param1Long = 253402300799999L; 
      this.expiresAt = param1Long;
      this.persistent = true;
      return this;
    }
    
    public Builder hostOnlyDomain(String param1String) {
      return domain(param1String, true);
    }
    
    public Builder httpOnly() {
      this.httpOnly = true;
      return this;
    }
    
    public Builder name(String param1String) {
      if (param1String == null)
        throw new NullPointerException("name == null"); 
      if (!param1String.trim().equals(param1String))
        throw new IllegalArgumentException("name is not trimmed"); 
      this.name = param1String;
      return this;
    }
    
    public Builder path(String param1String) {
      if (!param1String.startsWith("/"))
        throw new IllegalArgumentException("path must start with '/'"); 
      this.path = param1String;
      return this;
    }
    
    public Builder secure() {
      this.secure = true;
      return this;
    }
    
    public Builder value(String param1String) {
      if (param1String == null)
        throw new NullPointerException("value == null"); 
      if (!param1String.trim().equals(param1String))
        throw new IllegalArgumentException("value is not trimmed"); 
      this.value = param1String;
      return this;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/Cookie.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */