package android.support.v4.text;

import android.text.SpannableStringBuilder;
import java.util.Locale;

public final class BidiFormatter {
  private static final int DEFAULT_FLAGS = 2;
  
  private static final BidiFormatter DEFAULT_LTR_INSTANCE;
  
  private static final BidiFormatter DEFAULT_RTL_INSTANCE;
  
  private static TextDirectionHeuristicCompat DEFAULT_TEXT_DIRECTION_HEURISTIC = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
  
  private static final int DIR_LTR = -1;
  
  private static final int DIR_RTL = 1;
  
  private static final int DIR_UNKNOWN = 0;
  
  private static final String EMPTY_STRING = "";
  
  private static final int FLAG_STEREO_RESET = 2;
  
  private static final char LRE = '‪';
  
  private static final char LRM = '‎';
  
  private static final String LRM_STRING = Character.toString('‎');
  
  private static final char PDF = '‬';
  
  private static final char RLE = '‫';
  
  private static final char RLM = '‏';
  
  private static final String RLM_STRING = Character.toString('‏');
  
  private final TextDirectionHeuristicCompat mDefaultTextDirectionHeuristicCompat;
  
  private final int mFlags;
  
  private final boolean mIsRtlContext;
  
  static {
    DEFAULT_LTR_INSTANCE = new BidiFormatter(false, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
    DEFAULT_RTL_INSTANCE = new BidiFormatter(true, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
  }
  
  private BidiFormatter(boolean paramBoolean, int paramInt, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat) {
    this.mIsRtlContext = paramBoolean;
    this.mFlags = paramInt;
    this.mDefaultTextDirectionHeuristicCompat = paramTextDirectionHeuristicCompat;
  }
  
  private static int getEntryDir(CharSequence paramCharSequence) {
    return (new DirectionalityEstimator(paramCharSequence, false)).getEntryDir();
  }
  
  private static int getExitDir(CharSequence paramCharSequence) {
    return (new DirectionalityEstimator(paramCharSequence, false)).getExitDir();
  }
  
  public static BidiFormatter getInstance() {
    return (new Builder()).build();
  }
  
  public static BidiFormatter getInstance(Locale paramLocale) {
    return (new Builder(paramLocale)).build();
  }
  
  public static BidiFormatter getInstance(boolean paramBoolean) {
    return (new Builder(paramBoolean)).build();
  }
  
  private static boolean isRtlLocale(Locale paramLocale) {
    boolean bool = true;
    if (TextUtilsCompat.getLayoutDirectionFromLocale(paramLocale) != 1)
      bool = false; 
    return bool;
  }
  
  private String markAfter(CharSequence paramCharSequence, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat) {
    boolean bool = paramTextDirectionHeuristicCompat.isRtl(paramCharSequence, 0, paramCharSequence.length());
    return (!this.mIsRtlContext && (bool || getExitDir(paramCharSequence) == 1)) ? LRM_STRING : ((this.mIsRtlContext && (!bool || getExitDir(paramCharSequence) == -1)) ? RLM_STRING : "");
  }
  
  private String markBefore(CharSequence paramCharSequence, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat) {
    boolean bool = paramTextDirectionHeuristicCompat.isRtl(paramCharSequence, 0, paramCharSequence.length());
    return (!this.mIsRtlContext && (bool || getEntryDir(paramCharSequence) == 1)) ? LRM_STRING : ((this.mIsRtlContext && (!bool || getEntryDir(paramCharSequence) == -1)) ? RLM_STRING : "");
  }
  
  public boolean getStereoReset() {
    return ((this.mFlags & 0x2) != 0);
  }
  
  public boolean isRtl(CharSequence paramCharSequence) {
    return this.mDefaultTextDirectionHeuristicCompat.isRtl(paramCharSequence, 0, paramCharSequence.length());
  }
  
  public boolean isRtl(String paramString) {
    return isRtl(paramString);
  }
  
  public boolean isRtlContext() {
    return this.mIsRtlContext;
  }
  
  public CharSequence unicodeWrap(CharSequence paramCharSequence) {
    return unicodeWrap(paramCharSequence, this.mDefaultTextDirectionHeuristicCompat, true);
  }
  
  public CharSequence unicodeWrap(CharSequence paramCharSequence, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat) {
    return unicodeWrap(paramCharSequence, paramTextDirectionHeuristicCompat, true);
  }
  
  public CharSequence unicodeWrap(CharSequence paramCharSequence, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat, boolean paramBoolean) {
    if (paramCharSequence == null)
      return null; 
    boolean bool = paramTextDirectionHeuristicCompat.isRtl(paramCharSequence, 0, paramCharSequence.length());
    SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
    if (getStereoReset() && paramBoolean) {
      if (bool) {
        paramTextDirectionHeuristicCompat = TextDirectionHeuristicsCompat.RTL;
      } else {
        paramTextDirectionHeuristicCompat = TextDirectionHeuristicsCompat.LTR;
      } 
      spannableStringBuilder2.append(markBefore(paramCharSequence, paramTextDirectionHeuristicCompat));
    } 
    if (bool != this.mIsRtlContext) {
      char c;
      if (bool) {
        char c1 = '‫';
        c = c1;
      } else {
        char c1 = '‪';
        c = c1;
      } 
      spannableStringBuilder2.append(c);
      spannableStringBuilder2.append(paramCharSequence);
      spannableStringBuilder2.append('‬');
    } else {
      spannableStringBuilder2.append(paramCharSequence);
    } 
    SpannableStringBuilder spannableStringBuilder1 = spannableStringBuilder2;
    if (paramBoolean) {
      TextDirectionHeuristicCompat textDirectionHeuristicCompat;
      if (bool) {
        textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.RTL;
      } else {
        textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.LTR;
      } 
      spannableStringBuilder2.append(markAfter(paramCharSequence, textDirectionHeuristicCompat));
      spannableStringBuilder1 = spannableStringBuilder2;
    } 
    return (CharSequence)spannableStringBuilder1;
  }
  
  public CharSequence unicodeWrap(CharSequence paramCharSequence, boolean paramBoolean) {
    return unicodeWrap(paramCharSequence, this.mDefaultTextDirectionHeuristicCompat, paramBoolean);
  }
  
  public String unicodeWrap(String paramString) {
    return unicodeWrap(paramString, this.mDefaultTextDirectionHeuristicCompat, true);
  }
  
  public String unicodeWrap(String paramString, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat) {
    return unicodeWrap(paramString, paramTextDirectionHeuristicCompat, true);
  }
  
  public String unicodeWrap(String paramString, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat, boolean paramBoolean) {
    return (paramString == null) ? null : unicodeWrap(paramString, paramTextDirectionHeuristicCompat, paramBoolean).toString();
  }
  
  public String unicodeWrap(String paramString, boolean paramBoolean) {
    return unicodeWrap(paramString, this.mDefaultTextDirectionHeuristicCompat, paramBoolean);
  }
  
  public static final class Builder {
    private int mFlags;
    
    private boolean mIsRtlContext;
    
    private TextDirectionHeuristicCompat mTextDirectionHeuristicCompat;
    
    public Builder() {
      initialize(BidiFormatter.isRtlLocale(Locale.getDefault()));
    }
    
    public Builder(Locale param1Locale) {
      initialize(BidiFormatter.isRtlLocale(param1Locale));
    }
    
    public Builder(boolean param1Boolean) {
      initialize(param1Boolean);
    }
    
    private static BidiFormatter getDefaultInstanceFromContext(boolean param1Boolean) {
      return param1Boolean ? BidiFormatter.DEFAULT_RTL_INSTANCE : BidiFormatter.DEFAULT_LTR_INSTANCE;
    }
    
    private void initialize(boolean param1Boolean) {
      this.mIsRtlContext = param1Boolean;
      this.mTextDirectionHeuristicCompat = BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC;
      this.mFlags = 2;
    }
    
    public BidiFormatter build() {
      return (this.mFlags == 2 && this.mTextDirectionHeuristicCompat == BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC) ? getDefaultInstanceFromContext(this.mIsRtlContext) : new BidiFormatter(this.mIsRtlContext, this.mFlags, this.mTextDirectionHeuristicCompat);
    }
    
    public Builder setTextDirectionHeuristic(TextDirectionHeuristicCompat param1TextDirectionHeuristicCompat) {
      this.mTextDirectionHeuristicCompat = param1TextDirectionHeuristicCompat;
      return this;
    }
    
    public Builder stereoReset(boolean param1Boolean) {
      if (param1Boolean) {
        this.mFlags |= 0x2;
        return this;
      } 
      this.mFlags &= 0xFFFFFFFD;
      return this;
    }
  }
  
  private static class DirectionalityEstimator {
    private static final byte[] DIR_TYPE_CACHE = new byte[1792];
    
    private static final int DIR_TYPE_CACHE_SIZE = 1792;
    
    private int charIndex;
    
    private final boolean isHtml;
    
    private char lastChar;
    
    private final int length;
    
    private final CharSequence text;
    
    static {
      for (byte b = 0; b < '܀'; b++)
        DIR_TYPE_CACHE[b] = Character.getDirectionality(b); 
    }
    
    DirectionalityEstimator(CharSequence param1CharSequence, boolean param1Boolean) {
      this.text = param1CharSequence;
      this.isHtml = param1Boolean;
      this.length = param1CharSequence.length();
    }
    
    private static byte getCachedDirectionality(char param1Char) {
      if (param1Char < '܀')
        return DIR_TYPE_CACHE[param1Char]; 
      return Character.getDirectionality(param1Char);
    }
    
    private byte skipEntityBackward() {
      // Byte code:
      //   0: aload_0
      //   1: getfield charIndex : I
      //   4: istore_1
      //   5: aload_0
      //   6: getfield charIndex : I
      //   9: ifle -> 67
      //   12: aload_0
      //   13: getfield text : Ljava/lang/CharSequence;
      //   16: astore_2
      //   17: aload_0
      //   18: getfield charIndex : I
      //   21: iconst_1
      //   22: isub
      //   23: istore_3
      //   24: aload_0
      //   25: iload_3
      //   26: putfield charIndex : I
      //   29: aload_0
      //   30: aload_2
      //   31: iload_3
      //   32: invokeinterface charAt : (I)C
      //   37: putfield lastChar : C
      //   40: aload_0
      //   41: getfield lastChar : C
      //   44: bipush #38
      //   46: if_icmpne -> 58
      //   49: bipush #12
      //   51: istore_3
      //   52: iload_3
      //   53: istore #4
      //   55: iload #4
      //   57: ireturn
      //   58: aload_0
      //   59: getfield lastChar : C
      //   62: bipush #59
      //   64: if_icmpne -> 5
      //   67: aload_0
      //   68: iload_1
      //   69: putfield charIndex : I
      //   72: aload_0
      //   73: bipush #59
      //   75: i2c
      //   76: putfield lastChar : C
      //   79: bipush #13
      //   81: istore_3
      //   82: iload_3
      //   83: istore #4
      //   85: goto -> 55
    }
    
    private byte skipEntityForward() {
      while (this.charIndex < this.length) {
        CharSequence charSequence = this.text;
        int i = this.charIndex;
        this.charIndex = i + 1;
        i = charSequence.charAt(i);
        this.lastChar = (char)i;
        if (i == 59)
          break; 
      } 
      return 12;
    }
    
    private byte skipTagBackward() {
      int i = this.charIndex;
      while (true) {
        if (this.charIndex > 0) {
          CharSequence charSequence = this.text;
          int j = this.charIndex - 1;
          this.charIndex = j;
          this.lastChar = charSequence.charAt(j);
          if (this.lastChar == '<') {
            i = 12;
            return i;
          } 
          if (this.lastChar != '>') {
            if (this.lastChar == '"' || this.lastChar == '\'') {
              j = this.lastChar;
              while (this.charIndex > 0) {
                charSequence = this.text;
                int k = this.charIndex - 1;
                this.charIndex = k;
                k = charSequence.charAt(k);
                this.lastChar = (char)k;
                if (k != j);
              } 
            } 
            continue;
          } 
        } 
        this.charIndex = i;
        this.lastChar = (char)'>';
        i = 13;
        return i;
      } 
    }
    
    private byte skipTagForward() {
      // Byte code:
      //   0: aload_0
      //   1: getfield charIndex : I
      //   4: istore_1
      //   5: aload_0
      //   6: getfield charIndex : I
      //   9: aload_0
      //   10: getfield length : I
      //   13: if_icmpge -> 141
      //   16: aload_0
      //   17: getfield text : Ljava/lang/CharSequence;
      //   20: astore_2
      //   21: aload_0
      //   22: getfield charIndex : I
      //   25: istore_3
      //   26: aload_0
      //   27: iload_3
      //   28: iconst_1
      //   29: iadd
      //   30: putfield charIndex : I
      //   33: aload_0
      //   34: aload_2
      //   35: iload_3
      //   36: invokeinterface charAt : (I)C
      //   41: putfield lastChar : C
      //   44: aload_0
      //   45: getfield lastChar : C
      //   48: bipush #62
      //   50: if_icmpne -> 62
      //   53: bipush #12
      //   55: istore_1
      //   56: iload_1
      //   57: istore #4
      //   59: iload #4
      //   61: ireturn
      //   62: aload_0
      //   63: getfield lastChar : C
      //   66: bipush #34
      //   68: if_icmpeq -> 80
      //   71: aload_0
      //   72: getfield lastChar : C
      //   75: bipush #39
      //   77: if_icmpne -> 5
      //   80: aload_0
      //   81: getfield lastChar : C
      //   84: istore_3
      //   85: aload_0
      //   86: getfield charIndex : I
      //   89: aload_0
      //   90: getfield length : I
      //   93: if_icmpge -> 5
      //   96: aload_0
      //   97: getfield text : Ljava/lang/CharSequence;
      //   100: astore_2
      //   101: aload_0
      //   102: getfield charIndex : I
      //   105: istore #5
      //   107: aload_0
      //   108: iload #5
      //   110: iconst_1
      //   111: iadd
      //   112: putfield charIndex : I
      //   115: aload_2
      //   116: iload #5
      //   118: invokeinterface charAt : (I)C
      //   123: istore #5
      //   125: aload_0
      //   126: iload #5
      //   128: i2c
      //   129: putfield lastChar : C
      //   132: iload #5
      //   134: iload_3
      //   135: if_icmpeq -> 5
      //   138: goto -> 85
      //   141: aload_0
      //   142: iload_1
      //   143: putfield charIndex : I
      //   146: aload_0
      //   147: bipush #60
      //   149: i2c
      //   150: putfield lastChar : C
      //   153: bipush #13
      //   155: istore_1
      //   156: iload_1
      //   157: istore #4
      //   159: goto -> 59
    }
    
    byte dirTypeBackward() {
      this.lastChar = this.text.charAt(this.charIndex - 1);
      if (Character.isLowSurrogate(this.lastChar)) {
        int i = Character.codePointBefore(this.text, this.charIndex);
        this.charIndex -= Character.charCount(i);
        i = Character.getDirectionality(i);
        return i;
      } 
      this.charIndex--;
      byte b = getCachedDirectionality(this.lastChar);
      byte b1 = b;
      if (this.isHtml) {
        if (this.lastChar == '>') {
          b = skipTagBackward();
          return b;
        } 
        b1 = b;
        if (this.lastChar == ';') {
          b = skipEntityBackward();
          b1 = b;
        } 
      } 
      return b1;
    }
    
    byte dirTypeForward() {
      this.lastChar = this.text.charAt(this.charIndex);
      if (Character.isHighSurrogate(this.lastChar)) {
        int i = Character.codePointAt(this.text, this.charIndex);
        this.charIndex += Character.charCount(i);
        i = Character.getDirectionality(i);
        return i;
      } 
      this.charIndex++;
      byte b = getCachedDirectionality(this.lastChar);
      byte b1 = b;
      if (this.isHtml) {
        if (this.lastChar == '<') {
          b = skipTagForward();
          return b;
        } 
        b1 = b;
        if (this.lastChar == '&') {
          b = skipEntityForward();
          b1 = b;
        } 
      } 
      return b1;
    }
    
    int getEntryDir() {
      // Byte code:
      //   0: aload_0
      //   1: iconst_0
      //   2: putfield charIndex : I
      //   5: iconst_0
      //   6: istore_1
      //   7: iconst_0
      //   8: istore_2
      //   9: iconst_0
      //   10: istore_3
      //   11: aload_0
      //   12: getfield charIndex : I
      //   15: aload_0
      //   16: getfield length : I
      //   19: if_icmpge -> 179
      //   22: iload_3
      //   23: ifne -> 179
      //   26: aload_0
      //   27: invokevirtual dirTypeForward : ()B
      //   30: tableswitch default -> 120, 0 -> 149, 1 -> 164, 2 -> 164, 3 -> 120, 4 -> 120, 5 -> 120, 6 -> 120, 7 -> 120, 8 -> 120, 9 -> 11, 10 -> 120, 11 -> 120, 12 -> 120, 13 -> 120, 14 -> 125, 15 -> 125, 16 -> 133, 17 -> 133, 18 -> 141
      //   120: iload_1
      //   121: istore_3
      //   122: goto -> 11
      //   125: iinc #1, 1
      //   128: iconst_m1
      //   129: istore_2
      //   130: goto -> 11
      //   133: iinc #1, 1
      //   136: iconst_1
      //   137: istore_2
      //   138: goto -> 11
      //   141: iinc #1, -1
      //   144: iconst_0
      //   145: istore_2
      //   146: goto -> 11
      //   149: iload_1
      //   150: ifne -> 159
      //   153: iconst_m1
      //   154: istore #4
      //   156: iload #4
      //   158: ireturn
      //   159: iload_1
      //   160: istore_3
      //   161: goto -> 11
      //   164: iload_1
      //   165: ifne -> 174
      //   168: iconst_1
      //   169: istore #4
      //   171: goto -> 156
      //   174: iload_1
      //   175: istore_3
      //   176: goto -> 11
      //   179: iload_3
      //   180: ifne -> 189
      //   183: iconst_0
      //   184: istore #4
      //   186: goto -> 156
      //   189: iload_2
      //   190: istore #4
      //   192: iload_2
      //   193: ifne -> 156
      //   196: aload_0
      //   197: getfield charIndex : I
      //   200: ifle -> 283
      //   203: aload_0
      //   204: invokevirtual dirTypeBackward : ()B
      //   207: tableswitch default -> 240, 14 -> 243, 15 -> 243, 16 -> 260, 17 -> 260, 18 -> 277
      //   240: goto -> 196
      //   243: iload_3
      //   244: iload_1
      //   245: if_icmpne -> 254
      //   248: iconst_m1
      //   249: istore #4
      //   251: goto -> 156
      //   254: iinc #1, -1
      //   257: goto -> 196
      //   260: iload_3
      //   261: iload_1
      //   262: if_icmpne -> 271
      //   265: iconst_1
      //   266: istore #4
      //   268: goto -> 156
      //   271: iinc #1, -1
      //   274: goto -> 196
      //   277: iinc #1, 1
      //   280: goto -> 196
      //   283: iconst_0
      //   284: istore #4
      //   286: goto -> 156
    }
    
    int getExitDir() {
      // Byte code:
      //   0: iconst_m1
      //   1: istore_1
      //   2: aload_0
      //   3: aload_0
      //   4: getfield length : I
      //   7: putfield charIndex : I
      //   10: iconst_0
      //   11: istore_2
      //   12: iconst_0
      //   13: istore_3
      //   14: aload_0
      //   15: getfield charIndex : I
      //   18: ifle -> 200
      //   21: aload_0
      //   22: invokevirtual dirTypeBackward : ()B
      //   25: tableswitch default -> 116, 0 -> 125, 1 -> 158, 2 -> 158, 3 -> 116, 4 -> 116, 5 -> 116, 6 -> 116, 7 -> 116, 8 -> 116, 9 -> 14, 10 -> 116, 11 -> 116, 12 -> 116, 13 -> 116, 14 -> 144, 15 -> 144, 16 -> 177, 17 -> 177, 18 -> 194
      //   116: iload_3
      //   117: ifne -> 14
      //   120: iload_2
      //   121: istore_3
      //   122: goto -> 14
      //   125: iload_2
      //   126: ifne -> 135
      //   129: iload_1
      //   130: istore #4
      //   132: iload #4
      //   134: ireturn
      //   135: iload_3
      //   136: ifne -> 14
      //   139: iload_2
      //   140: istore_3
      //   141: goto -> 14
      //   144: iload_1
      //   145: istore #4
      //   147: iload_3
      //   148: iload_2
      //   149: if_icmpeq -> 132
      //   152: iinc #2, -1
      //   155: goto -> 14
      //   158: iload_2
      //   159: ifne -> 168
      //   162: iconst_1
      //   163: istore #4
      //   165: goto -> 132
      //   168: iload_3
      //   169: ifne -> 14
      //   172: iload_2
      //   173: istore_3
      //   174: goto -> 14
      //   177: iload_3
      //   178: iload_2
      //   179: if_icmpne -> 188
      //   182: iconst_1
      //   183: istore #4
      //   185: goto -> 132
      //   188: iinc #2, -1
      //   191: goto -> 14
      //   194: iinc #2, 1
      //   197: goto -> 14
      //   200: iconst_0
      //   201: istore #4
      //   203: goto -> 132
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/text/BidiFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */