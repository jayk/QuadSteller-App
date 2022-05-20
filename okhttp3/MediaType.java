package okhttp3;

import java.nio.charset.Charset;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public final class MediaType {
  private static final Pattern PARAMETER;
  
  private static final String QUOTED = "\"([^\"]*)\"";
  
  private static final String TOKEN = "([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)";
  
  private static final Pattern TYPE_SUBTYPE = Pattern.compile("([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)/([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)");
  
  @Nullable
  private final String charset;
  
  private final String mediaType;
  
  private final String subtype;
  
  private final String type;
  
  static {
    PARAMETER = Pattern.compile(";\\s*(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)=(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)|\"([^\"]*)\"))?");
  }
  
  private MediaType(String paramString1, String paramString2, String paramString3, @Nullable String paramString4) {
    this.mediaType = paramString1;
    this.type = paramString2;
    this.subtype = paramString3;
    this.charset = paramString4;
  }
  
  @Nullable
  public static MediaType parse(String paramString) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: getstatic okhttp3/MediaType.TYPE_SUBTYPE : Ljava/util/regex/Pattern;
    //   5: aload_0
    //   6: invokevirtual matcher : (Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
    //   9: astore_2
    //   10: aload_2
    //   11: invokevirtual lookingAt : ()Z
    //   14: ifne -> 21
    //   17: aload_1
    //   18: astore_3
    //   19: aload_3
    //   20: areturn
    //   21: aload_2
    //   22: iconst_1
    //   23: invokevirtual group : (I)Ljava/lang/String;
    //   26: getstatic java/util/Locale.US : Ljava/util/Locale;
    //   29: invokevirtual toLowerCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   32: astore #4
    //   34: aload_2
    //   35: iconst_2
    //   36: invokevirtual group : (I)Ljava/lang/String;
    //   39: getstatic java/util/Locale.US : Ljava/util/Locale;
    //   42: invokevirtual toLowerCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   45: astore #5
    //   47: aconst_null
    //   48: astore #6
    //   50: getstatic okhttp3/MediaType.PARAMETER : Ljava/util/regex/Pattern;
    //   53: aload_0
    //   54: invokevirtual matcher : (Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
    //   57: astore #7
    //   59: aload_2
    //   60: invokevirtual end : ()I
    //   63: istore #8
    //   65: iload #8
    //   67: aload_0
    //   68: invokevirtual length : ()I
    //   71: if_icmpge -> 216
    //   74: aload #7
    //   76: iload #8
    //   78: aload_0
    //   79: invokevirtual length : ()I
    //   82: invokevirtual region : (II)Ljava/util/regex/Matcher;
    //   85: pop
    //   86: aload_1
    //   87: astore_3
    //   88: aload #7
    //   90: invokevirtual lookingAt : ()Z
    //   93: ifeq -> 19
    //   96: aload #7
    //   98: iconst_1
    //   99: invokevirtual group : (I)Ljava/lang/String;
    //   102: astore_3
    //   103: aload #6
    //   105: astore_2
    //   106: aload_3
    //   107: ifnull -> 122
    //   110: aload_3
    //   111: ldc 'charset'
    //   113: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   116: ifne -> 135
    //   119: aload #6
    //   121: astore_2
    //   122: aload #7
    //   124: invokevirtual end : ()I
    //   127: istore #8
    //   129: aload_2
    //   130: astore #6
    //   132: goto -> 65
    //   135: aload #7
    //   137: iconst_2
    //   138: invokevirtual group : (I)Ljava/lang/String;
    //   141: astore_2
    //   142: aload_2
    //   143: ifnull -> 206
    //   146: aload_2
    //   147: ldc '''
    //   149: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   152: ifeq -> 203
    //   155: aload_2
    //   156: ldc '''
    //   158: invokevirtual endsWith : (Ljava/lang/String;)Z
    //   161: ifeq -> 203
    //   164: aload_2
    //   165: invokevirtual length : ()I
    //   168: iconst_2
    //   169: if_icmple -> 203
    //   172: aload_2
    //   173: iconst_1
    //   174: aload_2
    //   175: invokevirtual length : ()I
    //   178: iconst_1
    //   179: isub
    //   180: invokevirtual substring : (II)Ljava/lang/String;
    //   183: astore_2
    //   184: aload #6
    //   186: ifnull -> 200
    //   189: aload_1
    //   190: astore_3
    //   191: aload_2
    //   192: aload #6
    //   194: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   197: ifeq -> 19
    //   200: goto -> 122
    //   203: goto -> 184
    //   206: aload #7
    //   208: iconst_3
    //   209: invokevirtual group : (I)Ljava/lang/String;
    //   212: astore_2
    //   213: goto -> 184
    //   216: new okhttp3/MediaType
    //   219: dup
    //   220: aload_0
    //   221: aload #4
    //   223: aload #5
    //   225: aload #6
    //   227: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   230: astore_3
    //   231: goto -> 19
  }
  
  @Nullable
  public Charset charset() {
    return charset(null);
  }
  
  @Nullable
  public Charset charset(@Nullable Charset paramCharset) {
    Charset charset = paramCharset;
    try {
      if (this.charset != null)
        charset = Charset.forName(this.charset); 
    } catch (IllegalArgumentException illegalArgumentException) {
      charset = paramCharset;
    } 
    return charset;
  }
  
  public boolean equals(@Nullable Object paramObject) {
    return (paramObject instanceof MediaType && ((MediaType)paramObject).mediaType.equals(this.mediaType));
  }
  
  public int hashCode() {
    return this.mediaType.hashCode();
  }
  
  public String subtype() {
    return this.subtype;
  }
  
  public String toString() {
    return this.mediaType;
  }
  
  public String type() {
    return this.type;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/MediaType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */