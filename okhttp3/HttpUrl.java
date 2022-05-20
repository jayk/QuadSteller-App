package okhttp3;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.publicsuffix.PublicSuffixDatabase;
import okio.Buffer;

public final class HttpUrl {
  static final String FORM_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#&!$(),~";
  
  static final String FRAGMENT_ENCODE_SET = "";
  
  static final String FRAGMENT_ENCODE_SET_URI = " \"#<>\\^`{|}";
  
  private static final char[] HEX_DIGITS = new char[] { 
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
      'A', 'B', 'C', 'D', 'E', 'F' };
  
  static final String PASSWORD_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
  
  static final String PATH_SEGMENT_ENCODE_SET = " \"<>^`{}|/\\?#";
  
  static final String PATH_SEGMENT_ENCODE_SET_URI = "[]";
  
  static final String QUERY_COMPONENT_ENCODE_SET = " \"'<>#&=";
  
  static final String QUERY_COMPONENT_ENCODE_SET_URI = "\\^`{|}";
  
  static final String QUERY_ENCODE_SET = " \"'<>#";
  
  static final String USERNAME_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
  
  @Nullable
  private final String fragment;
  
  final String host;
  
  private final String password;
  
  private final List<String> pathSegments;
  
  final int port;
  
  @Nullable
  private final List<String> queryNamesAndValues;
  
  final String scheme;
  
  private final String url;
  
  private final String username;
  
  HttpUrl(Builder paramBuilder) {
    String str;
    this.scheme = paramBuilder.scheme;
    this.username = percentDecode(paramBuilder.encodedUsername, false);
    this.password = percentDecode(paramBuilder.encodedPassword, false);
    this.host = paramBuilder.host;
    this.port = paramBuilder.effectivePort();
    this.pathSegments = percentDecode(paramBuilder.encodedPathSegments, false);
    if (paramBuilder.encodedQueryNamesAndValues != null) {
      list2 = percentDecode(paramBuilder.encodedQueryNamesAndValues, true);
    } else {
      list2 = null;
    } 
    this.queryNamesAndValues = list2;
    List list2 = list1;
    if (paramBuilder.encodedFragment != null)
      str = percentDecode(paramBuilder.encodedFragment, false); 
    this.fragment = str;
    this.url = paramBuilder.toString();
  }
  
  static String canonicalize(String paramString1, int paramInt1, int paramInt2, String paramString2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    // Byte code:
    //   0: iload_1
    //   1: istore #8
    //   3: iload #8
    //   5: iload_2
    //   6: if_icmpge -> 151
    //   9: aload_0
    //   10: iload #8
    //   12: invokevirtual codePointAt : (I)I
    //   15: istore #9
    //   17: iload #9
    //   19: bipush #32
    //   21: if_icmplt -> 93
    //   24: iload #9
    //   26: bipush #127
    //   28: if_icmpeq -> 93
    //   31: iload #9
    //   33: sipush #128
    //   36: if_icmplt -> 44
    //   39: iload #7
    //   41: ifne -> 93
    //   44: aload_3
    //   45: iload #9
    //   47: invokevirtual indexOf : (I)I
    //   50: iconst_m1
    //   51: if_icmpne -> 93
    //   54: iload #9
    //   56: bipush #37
    //   58: if_icmpne -> 81
    //   61: iload #4
    //   63: ifeq -> 93
    //   66: iload #5
    //   68: ifeq -> 81
    //   71: aload_0
    //   72: iload #8
    //   74: iload_2
    //   75: invokestatic percentEncoded : (Ljava/lang/String;II)Z
    //   78: ifeq -> 93
    //   81: iload #9
    //   83: bipush #43
    //   85: if_icmpne -> 138
    //   88: iload #6
    //   90: ifeq -> 138
    //   93: new okio/Buffer
    //   96: dup
    //   97: invokespecial <init> : ()V
    //   100: astore #10
    //   102: aload #10
    //   104: aload_0
    //   105: iload_1
    //   106: iload #8
    //   108: invokevirtual writeUtf8 : (Ljava/lang/String;II)Lokio/Buffer;
    //   111: pop
    //   112: aload #10
    //   114: aload_0
    //   115: iload #8
    //   117: iload_2
    //   118: aload_3
    //   119: iload #4
    //   121: iload #5
    //   123: iload #6
    //   125: iload #7
    //   127: invokestatic canonicalize : (Lokio/Buffer;Ljava/lang/String;IILjava/lang/String;ZZZZ)V
    //   130: aload #10
    //   132: invokevirtual readUtf8 : ()Ljava/lang/String;
    //   135: astore_0
    //   136: aload_0
    //   137: areturn
    //   138: iload #8
    //   140: iload #9
    //   142: invokestatic charCount : (I)I
    //   145: iadd
    //   146: istore #8
    //   148: goto -> 3
    //   151: aload_0
    //   152: iload_1
    //   153: iload_2
    //   154: invokevirtual substring : (II)Ljava/lang/String;
    //   157: astore_0
    //   158: goto -> 136
  }
  
  static String canonicalize(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    return canonicalize(paramString1, 0, paramString1.length(), paramString2, paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4);
  }
  
  static void canonicalize(Buffer paramBuffer, String paramString1, int paramInt1, int paramInt2, String paramString2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    Buffer buffer = null;
    label45: while (paramInt1 < paramInt2) {
      int i = paramString1.codePointAt(paramInt1);
      if (paramBoolean1) {
        Buffer buffer2 = buffer;
        if (i != 9) {
          buffer2 = buffer;
          if (i != 10) {
            buffer2 = buffer;
            if (i != 12) {
              if (i == 13) {
                buffer2 = buffer;
                continue;
              } 
            } else {
              continue;
            } 
          } else {
            continue;
          } 
        } else {
          continue;
        } 
      } 
      if (i == 43 && paramBoolean3) {
        String str;
        if (paramBoolean1) {
          str = "+";
        } else {
          str = "%2B";
        } 
        paramBuffer.writeUtf8(str);
        Buffer buffer2 = buffer;
        continue;
      } 
      if (i < 32 || i == 127 || (i >= 128 && paramBoolean4) || paramString2.indexOf(i) != -1 || (i == 37 && (!paramBoolean1 || (paramBoolean2 && !percentEncoded(paramString1, paramInt1, paramInt2))))) {
        Buffer buffer2 = buffer;
        if (buffer == null)
          buffer2 = new Buffer(); 
        buffer2.writeUtf8CodePoint(i);
        while (true) {
          Buffer buffer3 = buffer2;
          if (!buffer2.exhausted()) {
            int j = buffer2.readByte() & 0xFF;
            paramBuffer.writeByte(37);
            paramBuffer.writeByte(HEX_DIGITS[j >> 4 & 0xF]);
            paramBuffer.writeByte(HEX_DIGITS[j & 0xF]);
            continue;
          } 
          paramInt1 += Character.charCount(i);
          buffer = buffer3;
          continue label45;
        } 
      } 
      paramBuffer.writeUtf8CodePoint(i);
      Buffer buffer1 = buffer;
      continue;
    } 
  }
  
  static int decodeHexDigit(char paramChar) {
    if (paramChar >= '0' && paramChar <= '9') {
      paramChar -= '0';
      return paramChar;
    } 
    return (paramChar >= 'a' && paramChar <= 'f') ? (paramChar - 97 + 10) : ((paramChar >= 'A' && paramChar <= 'F') ? (paramChar - 65 + 10) : -1);
  }
  
  public static int defaultPort(String paramString) {
    return paramString.equals("http") ? 80 : (paramString.equals("https") ? 443 : -1);
  }
  
  @Nullable
  public static HttpUrl get(URI paramURI) {
    return parse(paramURI.toString());
  }
  
  @Nullable
  public static HttpUrl get(URL paramURL) {
    return parse(paramURL.toString());
  }
  
  static HttpUrl getChecked(String paramString) throws MalformedURLException, UnknownHostException {
    Builder builder = new Builder();
    Builder.ParseResult parseResult = builder.parse(null, paramString);
    switch (parseResult) {
      default:
        throw new MalformedURLException("Invalid URL: " + parseResult + " for " + paramString);
      case SUCCESS:
        return builder.build();
      case INVALID_HOST:
        break;
    } 
    throw new UnknownHostException("Invalid host: " + paramString);
  }
  
  static void namesAndValuesToQueryString(StringBuilder paramStringBuilder, List<String> paramList) {
    byte b = 0;
    int i = paramList.size();
    while (b < i) {
      String str1 = paramList.get(b);
      String str2 = paramList.get(b + 1);
      if (b > 0)
        paramStringBuilder.append('&'); 
      paramStringBuilder.append(str1);
      if (str2 != null) {
        paramStringBuilder.append('=');
        paramStringBuilder.append(str2);
      } 
      b += 2;
    } 
  }
  
  @Nullable
  public static HttpUrl parse(String paramString) {
    HttpUrl httpUrl = null;
    Builder builder = new Builder();
    if (builder.parse(null, paramString) == Builder.ParseResult.SUCCESS)
      httpUrl = builder.build(); 
    return httpUrl;
  }
  
  static void pathSegmentsToString(StringBuilder paramStringBuilder, List<String> paramList) {
    byte b = 0;
    int i = paramList.size();
    while (b < i) {
      paramStringBuilder.append('/');
      paramStringBuilder.append(paramList.get(b));
      b++;
    } 
  }
  
  static String percentDecode(String paramString, int paramInt1, int paramInt2, boolean paramBoolean) {
    // Byte code:
    //   0: iload_1
    //   1: istore #4
    //   3: iload #4
    //   5: iload_2
    //   6: if_icmpge -> 78
    //   9: aload_0
    //   10: iload #4
    //   12: invokevirtual charAt : (I)C
    //   15: istore #5
    //   17: iload #5
    //   19: bipush #37
    //   21: if_icmpeq -> 35
    //   24: iload #5
    //   26: bipush #43
    //   28: if_icmpne -> 72
    //   31: iload_3
    //   32: ifeq -> 72
    //   35: new okio/Buffer
    //   38: dup
    //   39: invokespecial <init> : ()V
    //   42: astore #6
    //   44: aload #6
    //   46: aload_0
    //   47: iload_1
    //   48: iload #4
    //   50: invokevirtual writeUtf8 : (Ljava/lang/String;II)Lokio/Buffer;
    //   53: pop
    //   54: aload #6
    //   56: aload_0
    //   57: iload #4
    //   59: iload_2
    //   60: iload_3
    //   61: invokestatic percentDecode : (Lokio/Buffer;Ljava/lang/String;IIZ)V
    //   64: aload #6
    //   66: invokevirtual readUtf8 : ()Ljava/lang/String;
    //   69: astore_0
    //   70: aload_0
    //   71: areturn
    //   72: iinc #4, 1
    //   75: goto -> 3
    //   78: aload_0
    //   79: iload_1
    //   80: iload_2
    //   81: invokevirtual substring : (II)Ljava/lang/String;
    //   84: astore_0
    //   85: goto -> 70
  }
  
  static String percentDecode(String paramString, boolean paramBoolean) {
    return percentDecode(paramString, 0, paramString.length(), paramBoolean);
  }
  
  private List<String> percentDecode(List<String> paramList, boolean paramBoolean) {
    int i = paramList.size();
    ArrayList<String> arrayList = new ArrayList(i);
    for (byte b = 0; b < i; b++) {
      String str = paramList.get(b);
      if (str != null) {
        str = percentDecode(str, paramBoolean);
      } else {
        str = null;
      } 
      arrayList.add(str);
    } 
    return Collections.unmodifiableList(arrayList);
  }
  
  static void percentDecode(Buffer paramBuffer, String paramString, int paramInt1, int paramInt2, boolean paramBoolean) {
    while (paramInt1 < paramInt2) {
      int i = paramString.codePointAt(paramInt1);
      if (i == 37 && paramInt1 + 2 < paramInt2) {
        int j = decodeHexDigit(paramString.charAt(paramInt1 + 1));
        int k = decodeHexDigit(paramString.charAt(paramInt1 + 2));
        if (j != -1 && k != -1) {
          paramBuffer.writeByte((j << 4) + k);
          paramInt1 += 2;
          continue;
        } 
      } else if (i == 43 && paramBoolean) {
        paramBuffer.writeByte(32);
        continue;
      } 
      paramBuffer.writeUtf8CodePoint(i);
      continue;
      paramInt1 += Character.charCount(SYNTHETIC_LOCAL_VARIABLE_5);
    } 
  }
  
  static boolean percentEncoded(String paramString, int paramInt1, int paramInt2) {
    return (paramInt1 + 2 < paramInt2 && paramString.charAt(paramInt1) == '%' && decodeHexDigit(paramString.charAt(paramInt1 + 1)) != -1 && decodeHexDigit(paramString.charAt(paramInt1 + 2)) != -1);
  }
  
  static List<String> queryStringToNamesAndValues(String paramString) {
    ArrayList<String> arrayList = new ArrayList();
    for (int i = 0; i <= paramString.length(); i = k + 1) {
      int j = paramString.indexOf('&', i);
      int k = j;
      if (j == -1)
        k = paramString.length(); 
      j = paramString.indexOf('=', i);
      if (j == -1 || j > k) {
        arrayList.add(paramString.substring(i, k));
        arrayList.add(null);
      } else {
        arrayList.add(paramString.substring(i, j));
        arrayList.add(paramString.substring(j + 1, k));
      } 
    } 
    return arrayList;
  }
  
  @Nullable
  public String encodedFragment() {
    if (this.fragment == null)
      return null; 
    int i = this.url.indexOf('#');
    return this.url.substring(i + 1);
  }
  
  public String encodedPassword() {
    if (this.password.isEmpty())
      return ""; 
    int i = this.url.indexOf(':', this.scheme.length() + 3);
    int j = this.url.indexOf('@');
    return this.url.substring(i + 1, j);
  }
  
  public String encodedPath() {
    int i = this.url.indexOf('/', this.scheme.length() + 3);
    int j = Util.delimiterOffset(this.url, i, this.url.length(), "?#");
    return this.url.substring(i, j);
  }
  
  public List<String> encodedPathSegments() {
    int i = this.url.indexOf('/', this.scheme.length() + 3);
    int j = Util.delimiterOffset(this.url, i, this.url.length(), "?#");
    ArrayList<String> arrayList = new ArrayList();
    while (i < j) {
      int k = i + 1;
      i = Util.delimiterOffset(this.url, k, j, '/');
      arrayList.add(this.url.substring(k, i));
    } 
    return arrayList;
  }
  
  @Nullable
  public String encodedQuery() {
    if (this.queryNamesAndValues == null)
      return null; 
    int i = this.url.indexOf('?') + 1;
    int j = Util.delimiterOffset(this.url, i + 1, this.url.length(), '#');
    return this.url.substring(i, j);
  }
  
  public String encodedUsername() {
    if (this.username.isEmpty())
      return ""; 
    int i = this.scheme.length() + 3;
    int j = Util.delimiterOffset(this.url, i, this.url.length(), ":@");
    return this.url.substring(i, j);
  }
  
  public boolean equals(@Nullable Object paramObject) {
    return (paramObject instanceof HttpUrl && ((HttpUrl)paramObject).url.equals(this.url));
  }
  
  @Nullable
  public String fragment() {
    return this.fragment;
  }
  
  public int hashCode() {
    return this.url.hashCode();
  }
  
  public String host() {
    return this.host;
  }
  
  public boolean isHttps() {
    return this.scheme.equals("https");
  }
  
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.scheme = this.scheme;
    builder.encodedUsername = encodedUsername();
    builder.encodedPassword = encodedPassword();
    builder.host = this.host;
    if (this.port != defaultPort(this.scheme)) {
      int i = this.port;
      builder.port = i;
      builder.encodedPathSegments.clear();
      builder.encodedPathSegments.addAll(encodedPathSegments());
      builder.encodedQuery(encodedQuery());
      builder.encodedFragment = encodedFragment();
      return builder;
    } 
    byte b = -1;
    builder.port = b;
    builder.encodedPathSegments.clear();
    builder.encodedPathSegments.addAll(encodedPathSegments());
    builder.encodedQuery(encodedQuery());
    builder.encodedFragment = encodedFragment();
    return builder;
  }
  
  @Nullable
  public Builder newBuilder(String paramString) {
    Builder builder = new Builder();
    return (builder.parse(this, paramString) == Builder.ParseResult.SUCCESS) ? builder : null;
  }
  
  public String password() {
    return this.password;
  }
  
  public List<String> pathSegments() {
    return this.pathSegments;
  }
  
  public int pathSize() {
    return this.pathSegments.size();
  }
  
  public int port() {
    return this.port;
  }
  
  @Nullable
  public String query() {
    if (this.queryNamesAndValues == null)
      return null; 
    StringBuilder stringBuilder = new StringBuilder();
    namesAndValuesToQueryString(stringBuilder, this.queryNamesAndValues);
    return stringBuilder.toString();
  }
  
  @Nullable
  public String queryParameter(String paramString) {
    String str = null;
    if (this.queryNamesAndValues == null)
      return str; 
    byte b = 0;
    int i = this.queryNamesAndValues.size();
    while (true) {
      String str1 = str;
      if (b < i) {
        if (paramString.equals(this.queryNamesAndValues.get(b)))
          return this.queryNamesAndValues.get(b + 1); 
        b += 2;
        continue;
      } 
      return str1;
    } 
  }
  
  public String queryParameterName(int paramInt) {
    if (this.queryNamesAndValues == null)
      throw new IndexOutOfBoundsException(); 
    return this.queryNamesAndValues.get(paramInt * 2);
  }
  
  public Set<String> queryParameterNames() {
    if (this.queryNamesAndValues == null)
      return (Set)Collections.emptySet(); 
    LinkedHashSet<?> linkedHashSet = new LinkedHashSet();
    byte b = 0;
    int i = this.queryNamesAndValues.size();
    while (b < i) {
      linkedHashSet.add(this.queryNamesAndValues.get(b));
      b += 2;
    } 
    return (Set)Collections.unmodifiableSet(linkedHashSet);
  }
  
  public String queryParameterValue(int paramInt) {
    if (this.queryNamesAndValues == null)
      throw new IndexOutOfBoundsException(); 
    return this.queryNamesAndValues.get(paramInt * 2 + 1);
  }
  
  public List<String> queryParameterValues(String paramString) {
    if (this.queryNamesAndValues == null)
      return (List)Collections.emptyList(); 
    ArrayList<?> arrayList = new ArrayList();
    byte b = 0;
    int i = this.queryNamesAndValues.size();
    while (b < i) {
      if (paramString.equals(this.queryNamesAndValues.get(b)))
        arrayList.add(this.queryNamesAndValues.get(b + 1)); 
      b += 2;
    } 
    return (List)Collections.unmodifiableList(arrayList);
  }
  
  public int querySize() {
    return (this.queryNamesAndValues != null) ? (this.queryNamesAndValues.size() / 2) : 0;
  }
  
  public String redact() {
    return newBuilder("/...").username("").password("").build().toString();
  }
  
  @Nullable
  public HttpUrl resolve(String paramString) {
    null = newBuilder(paramString);
    return (null != null) ? null.build() : null;
  }
  
  public String scheme() {
    return this.scheme;
  }
  
  public String toString() {
    return this.url;
  }
  
  @Nullable
  public String topPrivateDomain() {
    return Util.verifyAsIpAddress(this.host) ? null : PublicSuffixDatabase.get().getEffectiveTldPlusOne(this.host);
  }
  
  public URI uri() {
    URI uRI;
    String str = newBuilder().reencodeForUri().toString();
    try {
      uRI = new URI();
      this(str);
    } catch (URISyntaxException uRISyntaxException) {}
    return uRI;
  }
  
  public URL url() {
    try {
      return new URL(this.url);
    } catch (MalformedURLException malformedURLException) {
      throw new RuntimeException(malformedURLException);
    } 
  }
  
  public String username() {
    return this.username;
  }
  
  public static final class Builder {
    @Nullable
    String encodedFragment;
    
    String encodedPassword = "";
    
    final List<String> encodedPathSegments = new ArrayList<String>();
    
    @Nullable
    List<String> encodedQueryNamesAndValues;
    
    String encodedUsername = "";
    
    @Nullable
    String host;
    
    int port = -1;
    
    @Nullable
    String scheme;
    
    public Builder() {
      this.encodedPathSegments.add("");
    }
    
    private Builder addPathSegments(String param1String, boolean param1Boolean) {
      int i = 0;
      while (true) {
        boolean bool;
        int j = Util.delimiterOffset(param1String, i, param1String.length(), "/\\");
        if (j < param1String.length()) {
          bool = true;
        } else {
          bool = false;
        } 
        push(param1String, i, j, bool, param1Boolean);
        i = ++j;
        if (j > param1String.length())
          return this; 
      } 
    }
    
    private static String canonicalizeHost(String param1String, int param1Int1, int param1Int2) {
      param1String = HttpUrl.percentDecode(param1String, param1Int1, param1Int2, false);
      if (param1String.contains(":")) {
        InetAddress inetAddress;
        if (param1String.startsWith("[") && param1String.endsWith("]")) {
          inetAddress = decodeIpv6(param1String, 1, param1String.length() - 1);
        } else {
          inetAddress = decodeIpv6((String)inetAddress, 0, inetAddress.length());
        } 
        if (inetAddress == null)
          return null; 
        byte[] arrayOfByte = inetAddress.getAddress();
        if (arrayOfByte.length == 16)
          return inet6AddressToAscii(arrayOfByte); 
        throw new AssertionError();
      } 
      return Util.domainToAscii(param1String);
    }
    
    private static boolean decodeIpv4Suffix(String param1String, int param1Int1, int param1Int2, byte[] param1ArrayOfbyte, int param1Int3) {
      boolean bool1 = false;
      int i = param1Int3;
      int j = param1Int1;
      while (j < param1Int2) {
        if (i == param1ArrayOfbyte.length) {
          boolean bool3 = bool1;
          continue;
        } 
        param1Int1 = j;
        if (i != param1Int3) {
          boolean bool3 = bool1;
          if (param1String.charAt(j) == '.') {
            param1Int1 = j + 1;
          } else {
            continue;
          } 
        } 
        boolean bool = false;
        j = param1Int1;
        while (true)
          j++; 
      } 
      boolean bool2 = bool1;
      if (i == param1Int3 + 4)
        bool2 = true; 
      return bool2;
    }
    
    @Nullable
    private static InetAddress decodeIpv6(String param1String, int param1Int1, int param1Int2) {
      // Byte code:
      //   0: bipush #16
      //   2: newarray byte
      //   4: astore_3
      //   5: iconst_0
      //   6: istore #4
      //   8: iconst_m1
      //   9: istore #5
      //   11: iconst_m1
      //   12: istore #6
      //   14: iload_1
      //   15: istore #7
      //   17: iload #4
      //   19: istore_1
      //   20: iload #5
      //   22: istore #8
      //   24: iload #7
      //   26: iload_2
      //   27: if_icmpge -> 107
      //   30: iload #4
      //   32: aload_3
      //   33: arraylength
      //   34: if_icmpne -> 41
      //   37: aconst_null
      //   38: astore_0
      //   39: aload_0
      //   40: areturn
      //   41: iload #7
      //   43: iconst_2
      //   44: iadd
      //   45: iload_2
      //   46: if_icmpgt -> 124
      //   49: aload_0
      //   50: iload #7
      //   52: ldc '::'
      //   54: iconst_0
      //   55: iconst_2
      //   56: invokevirtual regionMatches : (ILjava/lang/String;II)Z
      //   59: ifeq -> 124
      //   62: iload #5
      //   64: iconst_m1
      //   65: if_icmpeq -> 73
      //   68: aconst_null
      //   69: astore_0
      //   70: goto -> 39
      //   73: iinc #7, 2
      //   76: iinc #4, 2
      //   79: iload #4
      //   81: istore #5
      //   83: iload #4
      //   85: istore #9
      //   87: iload #5
      //   89: istore #8
      //   91: iload #7
      //   93: istore_1
      //   94: iload #7
      //   96: iload_2
      //   97: if_icmpne -> 166
      //   100: iload #5
      //   102: istore #8
      //   104: iload #4
      //   106: istore_1
      //   107: iload_1
      //   108: aload_3
      //   109: arraylength
      //   110: if_icmpeq -> 369
      //   113: iload #8
      //   115: iconst_m1
      //   116: if_icmpne -> 337
      //   119: aconst_null
      //   120: astore_0
      //   121: goto -> 39
      //   124: iload #4
      //   126: istore #9
      //   128: iload #5
      //   130: istore #8
      //   132: iload #7
      //   134: istore_1
      //   135: iload #4
      //   137: ifeq -> 166
      //   140: aload_0
      //   141: iload #7
      //   143: ldc ':'
      //   145: iconst_0
      //   146: iconst_1
      //   147: invokevirtual regionMatches : (ILjava/lang/String;II)Z
      //   150: ifeq -> 215
      //   153: iload #7
      //   155: iconst_1
      //   156: iadd
      //   157: istore_1
      //   158: iload #5
      //   160: istore #8
      //   162: iload #4
      //   164: istore #9
      //   166: iconst_0
      //   167: istore #4
      //   169: iload_1
      //   170: istore #7
      //   172: iload_1
      //   173: iload_2
      //   174: if_icmpge -> 193
      //   177: aload_0
      //   178: iload_1
      //   179: invokevirtual charAt : (I)C
      //   182: invokestatic decodeHexDigit : (C)I
      //   185: istore #5
      //   187: iload #5
      //   189: iconst_m1
      //   190: if_icmpne -> 265
      //   193: iload_1
      //   194: iload #7
      //   196: isub
      //   197: istore #5
      //   199: iload #5
      //   201: ifeq -> 210
      //   204: iload #5
      //   206: iconst_4
      //   207: if_icmple -> 280
      //   210: aconst_null
      //   211: astore_0
      //   212: goto -> 39
      //   215: aload_0
      //   216: iload #7
      //   218: ldc '.'
      //   220: iconst_0
      //   221: iconst_1
      //   222: invokevirtual regionMatches : (ILjava/lang/String;II)Z
      //   225: ifeq -> 260
      //   228: aload_0
      //   229: iload #6
      //   231: iload_2
      //   232: aload_3
      //   233: iload #4
      //   235: iconst_2
      //   236: isub
      //   237: invokestatic decodeIpv4Suffix : (Ljava/lang/String;II[BI)Z
      //   240: ifne -> 248
      //   243: aconst_null
      //   244: astore_0
      //   245: goto -> 39
      //   248: iload #4
      //   250: iconst_2
      //   251: iadd
      //   252: istore_1
      //   253: iload #5
      //   255: istore #8
      //   257: goto -> 107
      //   260: aconst_null
      //   261: astore_0
      //   262: goto -> 39
      //   265: iload #4
      //   267: iconst_4
      //   268: ishl
      //   269: iload #5
      //   271: iadd
      //   272: istore #4
      //   274: iinc #1, 1
      //   277: goto -> 172
      //   280: iload #9
      //   282: iconst_1
      //   283: iadd
      //   284: istore #6
      //   286: aload_3
      //   287: iload #9
      //   289: iload #4
      //   291: bipush #8
      //   293: iushr
      //   294: sipush #255
      //   297: iand
      //   298: i2b
      //   299: i2b
      //   300: bastore
      //   301: iload #6
      //   303: iconst_1
      //   304: iadd
      //   305: istore #5
      //   307: aload_3
      //   308: iload #6
      //   310: iload #4
      //   312: sipush #255
      //   315: iand
      //   316: i2b
      //   317: i2b
      //   318: bastore
      //   319: iload #5
      //   321: istore #4
      //   323: iload #8
      //   325: istore #5
      //   327: iload #7
      //   329: istore #6
      //   331: iload_1
      //   332: istore #7
      //   334: goto -> 17
      //   337: aload_3
      //   338: iload #8
      //   340: aload_3
      //   341: aload_3
      //   342: arraylength
      //   343: iload_1
      //   344: iload #8
      //   346: isub
      //   347: isub
      //   348: iload_1
      //   349: iload #8
      //   351: isub
      //   352: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
      //   355: aload_3
      //   356: iload #8
      //   358: aload_3
      //   359: arraylength
      //   360: iload_1
      //   361: isub
      //   362: iload #8
      //   364: iadd
      //   365: iconst_0
      //   366: invokestatic fill : ([BIIB)V
      //   369: aload_3
      //   370: invokestatic getByAddress : ([B)Ljava/net/InetAddress;
      //   373: astore_0
      //   374: goto -> 39
      //   377: astore_0
      //   378: new java/lang/AssertionError
      //   381: dup
      //   382: invokespecial <init> : ()V
      //   385: athrow
      // Exception table:
      //   from	to	target	type
      //   369	374	377	java/net/UnknownHostException
    }
    
    private static String inet6AddressToAscii(byte[] param1ArrayOfbyte) {
      int i = -1;
      int j = 0;
      int k = 0;
      while (k < param1ArrayOfbyte.length) {
        int n;
        int m = k;
        while (true) {
          n = m;
          if (n < 16 && param1ArrayOfbyte[n] == 0 && param1ArrayOfbyte[n + 1] == 0) {
            m = n + 2;
            continue;
          } 
          break;
        } 
        int i1 = n - k;
        int i2 = j;
        m = i;
        if (i1 > j) {
          i2 = j;
          m = i;
          if (i1 >= 4) {
            i2 = i1;
            m = k;
          } 
        } 
        k = n + 2;
        j = i2;
        i = m;
      } 
      Buffer buffer = new Buffer();
      for (k = 0; k < param1ArrayOfbyte.length; k += 2) {
        if (k == i) {
          buffer.writeByte(58);
          int m = k + j;
          k = m;
          if (m == 16) {
            buffer.writeByte(58);
            k = m;
          } 
          continue;
        } 
        if (k > 0)
          buffer.writeByte(58); 
        buffer.writeHexadecimalUnsignedLong(((param1ArrayOfbyte[k] & 0xFF) << 8 | param1ArrayOfbyte[k + 1] & 0xFF));
      } 
      return buffer.readUtf8();
    }
    
    private boolean isDot(String param1String) {
      return (param1String.equals(".") || param1String.equalsIgnoreCase("%2e"));
    }
    
    private boolean isDotDot(String param1String) {
      return (param1String.equals("..") || param1String.equalsIgnoreCase("%2e.") || param1String.equalsIgnoreCase(".%2e") || param1String.equalsIgnoreCase("%2e%2e"));
    }
    
    private static int parsePort(String param1String, int param1Int1, int param1Int2) {
      try {
        param1Int1 = Integer.parseInt(HttpUrl.canonicalize(param1String, param1Int1, param1Int2, "", false, false, false, true));
        if (param1Int1 <= 0 || param1Int1 > 65535)
          param1Int1 = -1; 
      } catch (NumberFormatException numberFormatException) {
        param1Int1 = -1;
      } 
      return param1Int1;
    }
    
    private void pop() {
      if (((String)this.encodedPathSegments.remove(this.encodedPathSegments.size() - 1)).isEmpty() && !this.encodedPathSegments.isEmpty()) {
        this.encodedPathSegments.set(this.encodedPathSegments.size() - 1, "");
        return;
      } 
      this.encodedPathSegments.add("");
    }
    
    private static int portColonOffset(String param1String, int param1Int1, int param1Int2) {
      label16: while (param1Int1 < param1Int2) {
        int i = param1Int1;
        int j = param1Int1;
        switch (param1String.charAt(param1Int1)) {
          default:
            i = param1Int1;
            continue;
          case '[':
            while (true) {
              param1Int1 = i + 1;
              i = param1Int1;
              if (param1Int1 < param1Int2) {
                i = param1Int1;
                if (param1String.charAt(param1Int1) == ']') {
                  i = param1Int1;
                } else {
                  continue;
                } 
              } 
              param1Int1 = i + 1;
              continue label16;
            } 
          case ':':
            break;
        } 
        // Byte code: goto -> 80
      } 
      return param1Int2;
    }
    
    private void push(String param1String, int param1Int1, int param1Int2, boolean param1Boolean1, boolean param1Boolean2) {
      param1String = HttpUrl.canonicalize(param1String, param1Int1, param1Int2, " \"<>^`{}|/\\?#", param1Boolean2, false, false, true);
      if (!isDot(param1String)) {
        if (isDotDot(param1String)) {
          pop();
          return;
        } 
        if (((String)this.encodedPathSegments.get(this.encodedPathSegments.size() - 1)).isEmpty()) {
          this.encodedPathSegments.set(this.encodedPathSegments.size() - 1, param1String);
        } else {
          this.encodedPathSegments.add(param1String);
        } 
        if (param1Boolean1)
          this.encodedPathSegments.add(""); 
      } 
    }
    
    private void removeAllCanonicalQueryParameters(String param1String) {
      for (int i = this.encodedQueryNamesAndValues.size() - 2;; i -= 2) {
        if (i >= 0) {
          if (param1String.equals(this.encodedQueryNamesAndValues.get(i))) {
            this.encodedQueryNamesAndValues.remove(i + 1);
            this.encodedQueryNamesAndValues.remove(i);
            if (this.encodedQueryNamesAndValues.isEmpty()) {
              this.encodedQueryNamesAndValues = null;
              return;
            } 
          } 
        } else {
          return;
        } 
      } 
    }
    
    private void resolvePath(String param1String, int param1Int1, int param1Int2) {
      if (param1Int1 != param1Int2) {
        char c = param1String.charAt(param1Int1);
        if (c == '/' || c == '\\') {
          this.encodedPathSegments.clear();
          this.encodedPathSegments.add("");
          param1Int1++;
        } else {
          this.encodedPathSegments.set(this.encodedPathSegments.size() - 1, "");
        } 
        while (true) {
          if (param1Int1 < param1Int2) {
            boolean bool;
            int i = Util.delimiterOffset(param1String, param1Int1, param1Int2, "/\\");
            if (i < param1Int2) {
              bool = true;
            } else {
              bool = false;
            } 
            push(param1String, param1Int1, i, bool, true);
            param1Int1 = i;
            if (bool)
              param1Int1 = i + 1; 
            continue;
          } 
          return;
        } 
      } 
    }
    
    private static int schemeDelimiterOffset(String param1String, int param1Int1, int param1Int2) {
      // Byte code:
      //   0: iload_2
      //   1: iload_1
      //   2: isub
      //   3: iconst_2
      //   4: if_icmpge -> 11
      //   7: iconst_m1
      //   8: istore_1
      //   9: iload_1
      //   10: ireturn
      //   11: aload_0
      //   12: iload_1
      //   13: invokevirtual charAt : (I)C
      //   16: istore_3
      //   17: iload_3
      //   18: bipush #97
      //   20: if_icmplt -> 29
      //   23: iload_3
      //   24: bipush #122
      //   26: if_icmple -> 46
      //   29: iload_3
      //   30: bipush #65
      //   32: if_icmplt -> 41
      //   35: iload_3
      //   36: bipush #90
      //   38: if_icmple -> 46
      //   41: iconst_m1
      //   42: istore_1
      //   43: goto -> 9
      //   46: iinc #1, 1
      //   49: iload_1
      //   50: iload_2
      //   51: if_icmpge -> 131
      //   54: aload_0
      //   55: iload_1
      //   56: invokevirtual charAt : (I)C
      //   59: istore_3
      //   60: iload_3
      //   61: bipush #97
      //   63: if_icmplt -> 72
      //   66: iload_3
      //   67: bipush #122
      //   69: if_icmple -> 114
      //   72: iload_3
      //   73: bipush #65
      //   75: if_icmplt -> 84
      //   78: iload_3
      //   79: bipush #90
      //   81: if_icmple -> 114
      //   84: iload_3
      //   85: bipush #48
      //   87: if_icmplt -> 96
      //   90: iload_3
      //   91: bipush #57
      //   93: if_icmple -> 114
      //   96: iload_3
      //   97: bipush #43
      //   99: if_icmpeq -> 114
      //   102: iload_3
      //   103: bipush #45
      //   105: if_icmpeq -> 114
      //   108: iload_3
      //   109: bipush #46
      //   111: if_icmpne -> 120
      //   114: iinc #1, 1
      //   117: goto -> 49
      //   120: iload_3
      //   121: bipush #58
      //   123: if_icmpeq -> 9
      //   126: iconst_m1
      //   127: istore_1
      //   128: goto -> 9
      //   131: iconst_m1
      //   132: istore_1
      //   133: goto -> 9
    }
    
    private static int slashCount(String param1String, int param1Int1, int param1Int2) {
      byte b = 0;
      while (param1Int1 < param1Int2) {
        char c = param1String.charAt(param1Int1);
        if (c == '\\' || c == '/') {
          b++;
          param1Int1++;
        } 
      } 
      return b;
    }
    
    public Builder addEncodedPathSegment(String param1String) {
      if (param1String == null)
        throw new NullPointerException("encodedPathSegment == null"); 
      push(param1String, 0, param1String.length(), false, true);
      return this;
    }
    
    public Builder addEncodedPathSegments(String param1String) {
      if (param1String == null)
        throw new NullPointerException("encodedPathSegments == null"); 
      return addPathSegments(param1String, true);
    }
    
    public Builder addEncodedQueryParameter(String param1String1, @Nullable String param1String2) {
      if (param1String1 == null)
        throw new NullPointerException("encodedName == null"); 
      if (this.encodedQueryNamesAndValues == null)
        this.encodedQueryNamesAndValues = new ArrayList<String>(); 
      this.encodedQueryNamesAndValues.add(HttpUrl.canonicalize(param1String1, " \"'<>#&=", true, false, true, true));
      List<String> list = this.encodedQueryNamesAndValues;
      if (param1String2 != null) {
        param1String1 = HttpUrl.canonicalize(param1String2, " \"'<>#&=", true, false, true, true);
        list.add(param1String1);
        return this;
      } 
      param1String1 = null;
      list.add(param1String1);
      return this;
    }
    
    public Builder addPathSegment(String param1String) {
      if (param1String == null)
        throw new NullPointerException("pathSegment == null"); 
      push(param1String, 0, param1String.length(), false, false);
      return this;
    }
    
    public Builder addPathSegments(String param1String) {
      if (param1String == null)
        throw new NullPointerException("pathSegments == null"); 
      return addPathSegments(param1String, false);
    }
    
    public Builder addQueryParameter(String param1String1, @Nullable String param1String2) {
      if (param1String1 == null)
        throw new NullPointerException("name == null"); 
      if (this.encodedQueryNamesAndValues == null)
        this.encodedQueryNamesAndValues = new ArrayList<String>(); 
      this.encodedQueryNamesAndValues.add(HttpUrl.canonicalize(param1String1, " \"'<>#&=", false, false, true, true));
      List<String> list = this.encodedQueryNamesAndValues;
      if (param1String2 != null) {
        param1String1 = HttpUrl.canonicalize(param1String2, " \"'<>#&=", false, false, true, true);
        list.add(param1String1);
        return this;
      } 
      param1String1 = null;
      list.add(param1String1);
      return this;
    }
    
    public HttpUrl build() {
      if (this.scheme == null)
        throw new IllegalStateException("scheme == null"); 
      if (this.host == null)
        throw new IllegalStateException("host == null"); 
      return new HttpUrl(this);
    }
    
    int effectivePort() {
      return (this.port != -1) ? this.port : HttpUrl.defaultPort(this.scheme);
    }
    
    public Builder encodedFragment(@Nullable String param1String) {
      if (param1String != null) {
        param1String = HttpUrl.canonicalize(param1String, "", true, false, false, false);
        this.encodedFragment = param1String;
        return this;
      } 
      param1String = null;
      this.encodedFragment = param1String;
      return this;
    }
    
    public Builder encodedPassword(String param1String) {
      if (param1String == null)
        throw new NullPointerException("encodedPassword == null"); 
      this.encodedPassword = HttpUrl.canonicalize(param1String, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
      return this;
    }
    
    public Builder encodedPath(String param1String) {
      if (param1String == null)
        throw new NullPointerException("encodedPath == null"); 
      if (!param1String.startsWith("/"))
        throw new IllegalArgumentException("unexpected encodedPath: " + param1String); 
      resolvePath(param1String, 0, param1String.length());
      return this;
    }
    
    public Builder encodedQuery(@Nullable String param1String) {
      if (param1String != null) {
        List<String> list = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(param1String, " \"'<>#", true, false, true, true));
        this.encodedQueryNamesAndValues = list;
        return this;
      } 
      param1String = null;
      this.encodedQueryNamesAndValues = (List<String>)param1String;
      return this;
    }
    
    public Builder encodedUsername(String param1String) {
      if (param1String == null)
        throw new NullPointerException("encodedUsername == null"); 
      this.encodedUsername = HttpUrl.canonicalize(param1String, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
      return this;
    }
    
    public Builder fragment(@Nullable String param1String) {
      if (param1String != null) {
        param1String = HttpUrl.canonicalize(param1String, "", false, false, false, false);
        this.encodedFragment = param1String;
        return this;
      } 
      param1String = null;
      this.encodedFragment = param1String;
      return this;
    }
    
    public Builder host(String param1String) {
      if (param1String == null)
        throw new NullPointerException("host == null"); 
      String str = canonicalizeHost(param1String, 0, param1String.length());
      if (str == null)
        throw new IllegalArgumentException("unexpected host: " + param1String); 
      this.host = str;
      return this;
    }
    
    ParseResult parse(@Nullable HttpUrl param1HttpUrl, String param1String) {
      // Byte code:
      //   0: aload_2
      //   1: iconst_0
      //   2: aload_2
      //   3: invokevirtual length : ()I
      //   6: invokestatic skipLeadingAsciiWhitespace : (Ljava/lang/String;II)I
      //   9: istore_3
      //   10: aload_2
      //   11: iload_3
      //   12: aload_2
      //   13: invokevirtual length : ()I
      //   16: invokestatic skipTrailingAsciiWhitespace : (Ljava/lang/String;II)I
      //   19: istore #4
      //   21: aload_2
      //   22: iload_3
      //   23: iload #4
      //   25: invokestatic schemeDelimiterOffset : (Ljava/lang/String;II)I
      //   28: iconst_m1
      //   29: if_icmpeq -> 302
      //   32: aload_2
      //   33: iconst_1
      //   34: iload_3
      //   35: ldc_w 'https:'
      //   38: iconst_0
      //   39: bipush #6
      //   41: invokevirtual regionMatches : (ZILjava/lang/String;II)Z
      //   44: ifeq -> 262
      //   47: aload_0
      //   48: ldc_w 'https'
      //   51: putfield scheme : Ljava/lang/String;
      //   54: iload_3
      //   55: ldc_w 'https:'
      //   58: invokevirtual length : ()I
      //   61: iadd
      //   62: istore_3
      //   63: iconst_0
      //   64: istore #5
      //   66: iconst_0
      //   67: istore #6
      //   69: aload_2
      //   70: iload_3
      //   71: iload #4
      //   73: invokestatic slashCount : (Ljava/lang/String;II)I
      //   76: istore #7
      //   78: iload #7
      //   80: iconst_2
      //   81: if_icmpge -> 102
      //   84: aload_1
      //   85: ifnull -> 102
      //   88: aload_1
      //   89: getfield scheme : Ljava/lang/String;
      //   92: aload_0
      //   93: getfield scheme : Ljava/lang/String;
      //   96: invokevirtual equals : (Ljava/lang/Object;)Z
      //   99: ifne -> 656
      //   102: iload_3
      //   103: iload #7
      //   105: iadd
      //   106: istore #7
      //   108: iload #6
      //   110: istore_3
      //   111: iload #7
      //   113: istore #6
      //   115: aload_2
      //   116: iload #6
      //   118: iload #4
      //   120: ldc_w '@/\?#'
      //   123: invokestatic delimiterOffset : (Ljava/lang/String;IILjava/lang/String;)I
      //   126: istore #8
      //   128: iload #8
      //   130: iload #4
      //   132: if_icmpeq -> 324
      //   135: aload_2
      //   136: iload #8
      //   138: invokevirtual charAt : (I)C
      //   141: istore #7
      //   143: iload #7
      //   145: lookupswitch default -> 204, -1 -> 207, 35 -> 207, 47 -> 207, 63 -> 207, 64 -> 330, 92 -> 207
      //   204: goto -> 115
      //   207: aload_2
      //   208: iload #6
      //   210: iload #8
      //   212: invokestatic portColonOffset : (Ljava/lang/String;II)I
      //   215: istore_3
      //   216: iload_3
      //   217: iconst_1
      //   218: iadd
      //   219: iload #8
      //   221: if_icmpge -> 495
      //   224: aload_0
      //   225: aload_2
      //   226: iload #6
      //   228: iload_3
      //   229: invokestatic canonicalizeHost : (Ljava/lang/String;II)Ljava/lang/String;
      //   232: putfield host : Ljava/lang/String;
      //   235: aload_0
      //   236: aload_2
      //   237: iload_3
      //   238: iconst_1
      //   239: iadd
      //   240: iload #8
      //   242: invokestatic parsePort : (Ljava/lang/String;II)I
      //   245: putfield port : I
      //   248: aload_0
      //   249: getfield port : I
      //   252: iconst_m1
      //   253: if_icmpne -> 517
      //   256: getstatic okhttp3/HttpUrl$Builder$ParseResult.INVALID_PORT : Lokhttp3/HttpUrl$Builder$ParseResult;
      //   259: astore_1
      //   260: aload_1
      //   261: areturn
      //   262: aload_2
      //   263: iconst_1
      //   264: iload_3
      //   265: ldc_w 'http:'
      //   268: iconst_0
      //   269: iconst_5
      //   270: invokevirtual regionMatches : (ZILjava/lang/String;II)Z
      //   273: ifeq -> 295
      //   276: aload_0
      //   277: ldc_w 'http'
      //   280: putfield scheme : Ljava/lang/String;
      //   283: iload_3
      //   284: ldc_w 'http:'
      //   287: invokevirtual length : ()I
      //   290: iadd
      //   291: istore_3
      //   292: goto -> 63
      //   295: getstatic okhttp3/HttpUrl$Builder$ParseResult.UNSUPPORTED_SCHEME : Lokhttp3/HttpUrl$Builder$ParseResult;
      //   298: astore_1
      //   299: goto -> 260
      //   302: aload_1
      //   303: ifnull -> 317
      //   306: aload_0
      //   307: aload_1
      //   308: getfield scheme : Ljava/lang/String;
      //   311: putfield scheme : Ljava/lang/String;
      //   314: goto -> 63
      //   317: getstatic okhttp3/HttpUrl$Builder$ParseResult.MISSING_SCHEME : Lokhttp3/HttpUrl$Builder$ParseResult;
      //   320: astore_1
      //   321: goto -> 260
      //   324: iconst_m1
      //   325: istore #7
      //   327: goto -> 143
      //   330: iload_3
      //   331: ifne -> 447
      //   334: aload_2
      //   335: iload #6
      //   337: iload #8
      //   339: bipush #58
      //   341: invokestatic delimiterOffset : (Ljava/lang/String;IIC)I
      //   344: istore #7
      //   346: aload_2
      //   347: iload #6
      //   349: iload #7
      //   351: ldc_w ' "':;<=>@[]^`{}|/\?#'
      //   354: iconst_1
      //   355: iconst_0
      //   356: iconst_0
      //   357: iconst_1
      //   358: invokestatic canonicalize : (Ljava/lang/String;IILjava/lang/String;ZZZZ)Ljava/lang/String;
      //   361: astore #9
      //   363: aload #9
      //   365: astore_1
      //   366: iload #5
      //   368: ifeq -> 400
      //   371: new java/lang/StringBuilder
      //   374: dup
      //   375: invokespecial <init> : ()V
      //   378: aload_0
      //   379: getfield encodedUsername : Ljava/lang/String;
      //   382: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   385: ldc_w '%40'
      //   388: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   391: aload #9
      //   393: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   396: invokevirtual toString : ()Ljava/lang/String;
      //   399: astore_1
      //   400: aload_0
      //   401: aload_1
      //   402: putfield encodedUsername : Ljava/lang/String;
      //   405: iload #7
      //   407: iload #8
      //   409: if_icmpeq -> 435
      //   412: iconst_1
      //   413: istore_3
      //   414: aload_0
      //   415: aload_2
      //   416: iload #7
      //   418: iconst_1
      //   419: iadd
      //   420: iload #8
      //   422: ldc_w ' "':;<=>@[]^`{}|/\?#'
      //   425: iconst_1
      //   426: iconst_0
      //   427: iconst_0
      //   428: iconst_1
      //   429: invokestatic canonicalize : (Ljava/lang/String;IILjava/lang/String;ZZZZ)Ljava/lang/String;
      //   432: putfield encodedPassword : Ljava/lang/String;
      //   435: iconst_1
      //   436: istore #5
      //   438: iload #8
      //   440: iconst_1
      //   441: iadd
      //   442: istore #6
      //   444: goto -> 115
      //   447: aload_0
      //   448: new java/lang/StringBuilder
      //   451: dup
      //   452: invokespecial <init> : ()V
      //   455: aload_0
      //   456: getfield encodedPassword : Ljava/lang/String;
      //   459: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   462: ldc_w '%40'
      //   465: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   468: aload_2
      //   469: iload #6
      //   471: iload #8
      //   473: ldc_w ' "':;<=>@[]^`{}|/\?#'
      //   476: iconst_1
      //   477: iconst_0
      //   478: iconst_0
      //   479: iconst_1
      //   480: invokestatic canonicalize : (Ljava/lang/String;IILjava/lang/String;ZZZZ)Ljava/lang/String;
      //   483: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   486: invokevirtual toString : ()Ljava/lang/String;
      //   489: putfield encodedPassword : Ljava/lang/String;
      //   492: goto -> 438
      //   495: aload_0
      //   496: aload_2
      //   497: iload #6
      //   499: iload_3
      //   500: invokestatic canonicalizeHost : (Ljava/lang/String;II)Ljava/lang/String;
      //   503: putfield host : Ljava/lang/String;
      //   506: aload_0
      //   507: aload_0
      //   508: getfield scheme : Ljava/lang/String;
      //   511: invokestatic defaultPort : (Ljava/lang/String;)I
      //   514: putfield port : I
      //   517: aload_0
      //   518: getfield host : Ljava/lang/String;
      //   521: ifnonnull -> 531
      //   524: getstatic okhttp3/HttpUrl$Builder$ParseResult.INVALID_HOST : Lokhttp3/HttpUrl$Builder$ParseResult;
      //   527: astore_1
      //   528: goto -> 260
      //   531: iload #8
      //   533: istore #6
      //   535: aload_2
      //   536: iload #6
      //   538: iload #4
      //   540: ldc_w '?#'
      //   543: invokestatic delimiterOffset : (Ljava/lang/String;IILjava/lang/String;)I
      //   546: istore_3
      //   547: aload_0
      //   548: aload_2
      //   549: iload #6
      //   551: iload_3
      //   552: invokespecial resolvePath : (Ljava/lang/String;II)V
      //   555: iload_3
      //   556: istore #6
      //   558: iload_3
      //   559: iload #4
      //   561: if_icmpge -> 611
      //   564: iload_3
      //   565: istore #6
      //   567: aload_2
      //   568: iload_3
      //   569: invokevirtual charAt : (I)C
      //   572: bipush #63
      //   574: if_icmpne -> 611
      //   577: aload_2
      //   578: iload_3
      //   579: iload #4
      //   581: bipush #35
      //   583: invokestatic delimiterOffset : (Ljava/lang/String;IIC)I
      //   586: istore #6
      //   588: aload_0
      //   589: aload_2
      //   590: iload_3
      //   591: iconst_1
      //   592: iadd
      //   593: iload #6
      //   595: ldc_w ' "'<>#'
      //   598: iconst_1
      //   599: iconst_0
      //   600: iconst_1
      //   601: iconst_1
      //   602: invokestatic canonicalize : (Ljava/lang/String;IILjava/lang/String;ZZZZ)Ljava/lang/String;
      //   605: invokestatic queryStringToNamesAndValues : (Ljava/lang/String;)Ljava/util/List;
      //   608: putfield encodedQueryNamesAndValues : Ljava/util/List;
      //   611: iload #6
      //   613: iload #4
      //   615: if_icmpge -> 649
      //   618: aload_2
      //   619: iload #6
      //   621: invokevirtual charAt : (I)C
      //   624: bipush #35
      //   626: if_icmpne -> 649
      //   629: aload_0
      //   630: aload_2
      //   631: iload #6
      //   633: iconst_1
      //   634: iadd
      //   635: iload #4
      //   637: ldc ''
      //   639: iconst_1
      //   640: iconst_0
      //   641: iconst_0
      //   642: iconst_0
      //   643: invokestatic canonicalize : (Ljava/lang/String;IILjava/lang/String;ZZZZ)Ljava/lang/String;
      //   646: putfield encodedFragment : Ljava/lang/String;
      //   649: getstatic okhttp3/HttpUrl$Builder$ParseResult.SUCCESS : Lokhttp3/HttpUrl$Builder$ParseResult;
      //   652: astore_1
      //   653: goto -> 260
      //   656: aload_0
      //   657: aload_1
      //   658: invokevirtual encodedUsername : ()Ljava/lang/String;
      //   661: putfield encodedUsername : Ljava/lang/String;
      //   664: aload_0
      //   665: aload_1
      //   666: invokevirtual encodedPassword : ()Ljava/lang/String;
      //   669: putfield encodedPassword : Ljava/lang/String;
      //   672: aload_0
      //   673: aload_1
      //   674: getfield host : Ljava/lang/String;
      //   677: putfield host : Ljava/lang/String;
      //   680: aload_0
      //   681: aload_1
      //   682: getfield port : I
      //   685: putfield port : I
      //   688: aload_0
      //   689: getfield encodedPathSegments : Ljava/util/List;
      //   692: invokeinterface clear : ()V
      //   697: aload_0
      //   698: getfield encodedPathSegments : Ljava/util/List;
      //   701: aload_1
      //   702: invokevirtual encodedPathSegments : ()Ljava/util/List;
      //   705: invokeinterface addAll : (Ljava/util/Collection;)Z
      //   710: pop
      //   711: iload_3
      //   712: iload #4
      //   714: if_icmpeq -> 730
      //   717: iload_3
      //   718: istore #6
      //   720: aload_2
      //   721: iload_3
      //   722: invokevirtual charAt : (I)C
      //   725: bipush #35
      //   727: if_icmpne -> 535
      //   730: aload_0
      //   731: aload_1
      //   732: invokevirtual encodedQuery : ()Ljava/lang/String;
      //   735: invokevirtual encodedQuery : (Ljava/lang/String;)Lokhttp3/HttpUrl$Builder;
      //   738: pop
      //   739: iload_3
      //   740: istore #6
      //   742: goto -> 535
    }
    
    public Builder password(String param1String) {
      if (param1String == null)
        throw new NullPointerException("password == null"); 
      this.encodedPassword = HttpUrl.canonicalize(param1String, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
      return this;
    }
    
    public Builder port(int param1Int) {
      if (param1Int <= 0 || param1Int > 65535)
        throw new IllegalArgumentException("unexpected port: " + param1Int); 
      this.port = param1Int;
      return this;
    }
    
    public Builder query(@Nullable String param1String) {
      if (param1String != null) {
        List<String> list = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(param1String, " \"'<>#", false, false, true, true));
        this.encodedQueryNamesAndValues = list;
        return this;
      } 
      param1String = null;
      this.encodedQueryNamesAndValues = (List<String>)param1String;
      return this;
    }
    
    Builder reencodeForUri() {
      byte b = 0;
      int i = this.encodedPathSegments.size();
      while (b < i) {
        String str = this.encodedPathSegments.get(b);
        this.encodedPathSegments.set(b, HttpUrl.canonicalize(str, "[]", true, true, false, true));
        b++;
      } 
      if (this.encodedQueryNamesAndValues != null) {
        b = 0;
        i = this.encodedQueryNamesAndValues.size();
        while (b < i) {
          String str = this.encodedQueryNamesAndValues.get(b);
          if (str != null)
            this.encodedQueryNamesAndValues.set(b, HttpUrl.canonicalize(str, "\\^`{|}", true, true, true, true)); 
          b++;
        } 
      } 
      if (this.encodedFragment != null)
        this.encodedFragment = HttpUrl.canonicalize(this.encodedFragment, " \"#<>\\^`{|}", true, true, false, false); 
      return this;
    }
    
    public Builder removeAllEncodedQueryParameters(String param1String) {
      if (param1String == null)
        throw new NullPointerException("encodedName == null"); 
      if (this.encodedQueryNamesAndValues != null)
        removeAllCanonicalQueryParameters(HttpUrl.canonicalize(param1String, " \"'<>#&=", true, false, true, true)); 
      return this;
    }
    
    public Builder removeAllQueryParameters(String param1String) {
      if (param1String == null)
        throw new NullPointerException("name == null"); 
      if (this.encodedQueryNamesAndValues != null)
        removeAllCanonicalQueryParameters(HttpUrl.canonicalize(param1String, " \"'<>#&=", false, false, true, true)); 
      return this;
    }
    
    public Builder removePathSegment(int param1Int) {
      this.encodedPathSegments.remove(param1Int);
      if (this.encodedPathSegments.isEmpty())
        this.encodedPathSegments.add(""); 
      return this;
    }
    
    public Builder scheme(String param1String) {
      if (param1String == null)
        throw new NullPointerException("scheme == null"); 
      if (param1String.equalsIgnoreCase("http")) {
        this.scheme = "http";
        return this;
      } 
      if (param1String.equalsIgnoreCase("https")) {
        this.scheme = "https";
        return this;
      } 
      throw new IllegalArgumentException("unexpected scheme: " + param1String);
    }
    
    public Builder setEncodedPathSegment(int param1Int, String param1String) {
      if (param1String == null)
        throw new NullPointerException("encodedPathSegment == null"); 
      String str = HttpUrl.canonicalize(param1String, 0, param1String.length(), " \"<>^`{}|/\\?#", true, false, false, true);
      this.encodedPathSegments.set(param1Int, str);
      if (isDot(str) || isDotDot(str))
        throw new IllegalArgumentException("unexpected path segment: " + param1String); 
      return this;
    }
    
    public Builder setEncodedQueryParameter(String param1String1, @Nullable String param1String2) {
      removeAllEncodedQueryParameters(param1String1);
      addEncodedQueryParameter(param1String1, param1String2);
      return this;
    }
    
    public Builder setPathSegment(int param1Int, String param1String) {
      if (param1String == null)
        throw new NullPointerException("pathSegment == null"); 
      String str = HttpUrl.canonicalize(param1String, 0, param1String.length(), " \"<>^`{}|/\\?#", false, false, false, true);
      if (isDot(str) || isDotDot(str))
        throw new IllegalArgumentException("unexpected path segment: " + param1String); 
      this.encodedPathSegments.set(param1Int, str);
      return this;
    }
    
    public Builder setQueryParameter(String param1String1, @Nullable String param1String2) {
      removeAllQueryParameters(param1String1);
      addQueryParameter(param1String1, param1String2);
      return this;
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.scheme);
      stringBuilder.append("://");
      if (!this.encodedUsername.isEmpty() || !this.encodedPassword.isEmpty()) {
        stringBuilder.append(this.encodedUsername);
        if (!this.encodedPassword.isEmpty()) {
          stringBuilder.append(':');
          stringBuilder.append(this.encodedPassword);
        } 
        stringBuilder.append('@');
      } 
      if (this.host.indexOf(':') != -1) {
        stringBuilder.append('[');
        stringBuilder.append(this.host);
        stringBuilder.append(']');
      } else {
        stringBuilder.append(this.host);
      } 
      int i = effectivePort();
      if (i != HttpUrl.defaultPort(this.scheme)) {
        stringBuilder.append(':');
        stringBuilder.append(i);
      } 
      HttpUrl.pathSegmentsToString(stringBuilder, this.encodedPathSegments);
      if (this.encodedQueryNamesAndValues != null) {
        stringBuilder.append('?');
        HttpUrl.namesAndValuesToQueryString(stringBuilder, this.encodedQueryNamesAndValues);
      } 
      if (this.encodedFragment != null) {
        stringBuilder.append('#');
        stringBuilder.append(this.encodedFragment);
      } 
      return stringBuilder.toString();
    }
    
    public Builder username(String param1String) {
      if (param1String == null)
        throw new NullPointerException("username == null"); 
      this.encodedUsername = HttpUrl.canonicalize(param1String, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
      return this;
    }
    
    enum ParseResult {
      INVALID_HOST, INVALID_PORT, MISSING_SCHEME, SUCCESS, UNSUPPORTED_SCHEME;
      
      static {
        INVALID_HOST = new ParseResult("INVALID_HOST", 4);
        $VALUES = new ParseResult[] { SUCCESS, MISSING_SCHEME, UNSUPPORTED_SCHEME, INVALID_PORT, INVALID_HOST };
      }
    }
  }
  
  enum ParseResult {
    INVALID_HOST, INVALID_PORT, MISSING_SCHEME, SUCCESS, UNSUPPORTED_SCHEME;
    
    static {
      INVALID_PORT = new ParseResult("INVALID_PORT", 3);
      INVALID_HOST = new ParseResult("INVALID_HOST", 4);
      $VALUES = new ParseResult[] { SUCCESS, MISSING_SCHEME, UNSUPPORTED_SCHEME, INVALID_PORT, INVALID_HOST };
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/HttpUrl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */