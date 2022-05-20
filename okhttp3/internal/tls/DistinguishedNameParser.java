package okhttp3.internal.tls;

import javax.security.auth.x500.X500Principal;

final class DistinguishedNameParser {
  private int beg;
  
  private char[] chars;
  
  private int cur;
  
  private final String dn;
  
  private int end;
  
  private final int length;
  
  private int pos;
  
  DistinguishedNameParser(X500Principal paramX500Principal) {
    this.dn = paramX500Principal.getName("RFC2253");
    this.length = this.dn.length();
  }
  
  private String escapedAV() {
    // Byte code:
    //   0: aload_0
    //   1: aload_0
    //   2: getfield pos : I
    //   5: putfield beg : I
    //   8: aload_0
    //   9: aload_0
    //   10: getfield pos : I
    //   13: putfield end : I
    //   16: aload_0
    //   17: getfield pos : I
    //   20: aload_0
    //   21: getfield length : I
    //   24: if_icmplt -> 54
    //   27: new java/lang/String
    //   30: dup
    //   31: aload_0
    //   32: getfield chars : [C
    //   35: aload_0
    //   36: getfield beg : I
    //   39: aload_0
    //   40: getfield end : I
    //   43: aload_0
    //   44: getfield beg : I
    //   47: isub
    //   48: invokespecial <init> : ([CII)V
    //   51: astore_1
    //   52: aload_1
    //   53: areturn
    //   54: aload_0
    //   55: getfield chars : [C
    //   58: aload_0
    //   59: getfield pos : I
    //   62: caload
    //   63: lookupswitch default -> 112, 32 -> 220, 43 -> 155, 44 -> 155, 59 -> 155, 92 -> 183
    //   112: aload_0
    //   113: getfield chars : [C
    //   116: astore_1
    //   117: aload_0
    //   118: getfield end : I
    //   121: istore_2
    //   122: aload_0
    //   123: iload_2
    //   124: iconst_1
    //   125: iadd
    //   126: putfield end : I
    //   129: aload_1
    //   130: iload_2
    //   131: aload_0
    //   132: getfield chars : [C
    //   135: aload_0
    //   136: getfield pos : I
    //   139: caload
    //   140: i2c
    //   141: castore
    //   142: aload_0
    //   143: aload_0
    //   144: getfield pos : I
    //   147: iconst_1
    //   148: iadd
    //   149: putfield pos : I
    //   152: goto -> 16
    //   155: new java/lang/String
    //   158: dup
    //   159: aload_0
    //   160: getfield chars : [C
    //   163: aload_0
    //   164: getfield beg : I
    //   167: aload_0
    //   168: getfield end : I
    //   171: aload_0
    //   172: getfield beg : I
    //   175: isub
    //   176: invokespecial <init> : ([CII)V
    //   179: astore_1
    //   180: goto -> 52
    //   183: aload_0
    //   184: getfield chars : [C
    //   187: astore_1
    //   188: aload_0
    //   189: getfield end : I
    //   192: istore_2
    //   193: aload_0
    //   194: iload_2
    //   195: iconst_1
    //   196: iadd
    //   197: putfield end : I
    //   200: aload_1
    //   201: iload_2
    //   202: aload_0
    //   203: invokespecial getEscaped : ()C
    //   206: castore
    //   207: aload_0
    //   208: aload_0
    //   209: getfield pos : I
    //   212: iconst_1
    //   213: iadd
    //   214: putfield pos : I
    //   217: goto -> 16
    //   220: aload_0
    //   221: aload_0
    //   222: getfield end : I
    //   225: putfield cur : I
    //   228: aload_0
    //   229: aload_0
    //   230: getfield pos : I
    //   233: iconst_1
    //   234: iadd
    //   235: putfield pos : I
    //   238: aload_0
    //   239: getfield chars : [C
    //   242: astore_1
    //   243: aload_0
    //   244: getfield end : I
    //   247: istore_2
    //   248: aload_0
    //   249: iload_2
    //   250: iconst_1
    //   251: iadd
    //   252: putfield end : I
    //   255: aload_1
    //   256: iload_2
    //   257: bipush #32
    //   259: i2c
    //   260: castore
    //   261: aload_0
    //   262: getfield pos : I
    //   265: aload_0
    //   266: getfield length : I
    //   269: if_icmpge -> 322
    //   272: aload_0
    //   273: getfield chars : [C
    //   276: aload_0
    //   277: getfield pos : I
    //   280: caload
    //   281: bipush #32
    //   283: if_icmpne -> 322
    //   286: aload_0
    //   287: getfield chars : [C
    //   290: astore_1
    //   291: aload_0
    //   292: getfield end : I
    //   295: istore_2
    //   296: aload_0
    //   297: iload_2
    //   298: iconst_1
    //   299: iadd
    //   300: putfield end : I
    //   303: aload_1
    //   304: iload_2
    //   305: bipush #32
    //   307: i2c
    //   308: castore
    //   309: aload_0
    //   310: aload_0
    //   311: getfield pos : I
    //   314: iconst_1
    //   315: iadd
    //   316: putfield pos : I
    //   319: goto -> 261
    //   322: aload_0
    //   323: getfield pos : I
    //   326: aload_0
    //   327: getfield length : I
    //   330: if_icmpeq -> 375
    //   333: aload_0
    //   334: getfield chars : [C
    //   337: aload_0
    //   338: getfield pos : I
    //   341: caload
    //   342: bipush #44
    //   344: if_icmpeq -> 375
    //   347: aload_0
    //   348: getfield chars : [C
    //   351: aload_0
    //   352: getfield pos : I
    //   355: caload
    //   356: bipush #43
    //   358: if_icmpeq -> 375
    //   361: aload_0
    //   362: getfield chars : [C
    //   365: aload_0
    //   366: getfield pos : I
    //   369: caload
    //   370: bipush #59
    //   372: if_icmpne -> 16
    //   375: new java/lang/String
    //   378: dup
    //   379: aload_0
    //   380: getfield chars : [C
    //   383: aload_0
    //   384: getfield beg : I
    //   387: aload_0
    //   388: getfield cur : I
    //   391: aload_0
    //   392: getfield beg : I
    //   395: isub
    //   396: invokespecial <init> : ([CII)V
    //   399: astore_1
    //   400: goto -> 52
  }
  
  private int getByte(int paramInt) {
    if (paramInt + 1 >= this.length)
      throw new IllegalStateException("Malformed DN: " + this.dn); 
    char c = this.chars[paramInt];
    if (c >= '0' && c <= '9') {
      c -= '0';
    } else if (c >= 'a' && c <= 'f') {
      c -= 'W';
    } else if (c >= 'A' && c <= 'F') {
      c -= '7';
    } else {
      throw new IllegalStateException("Malformed DN: " + this.dn);
    } 
    paramInt = this.chars[paramInt + 1];
    if (paramInt >= 48 && paramInt <= 57) {
      paramInt -= 48;
      return (c << 4) + paramInt;
    } 
    if (paramInt >= 97 && paramInt <= 102) {
      paramInt -= 87;
      return (c << 4) + paramInt;
    } 
    if (paramInt >= 65 && paramInt <= 70) {
      paramInt -= 55;
      return (c << 4) + paramInt;
    } 
    throw new IllegalStateException("Malformed DN: " + this.dn);
  }
  
  private char getEscaped() {
    this.pos++;
    if (this.pos == this.length)
      throw new IllegalStateException("Unexpected end of DN: " + this.dn); 
    switch (this.chars[this.pos]) {
      default:
        c = getUTF8();
        return c;
      case ' ':
      case '"':
      case '#':
      case '%':
      case '*':
      case '+':
      case ',':
      case ';':
      case '<':
      case '=':
      case '>':
      case '\\':
      case '_':
        break;
    } 
    char c = this.chars[this.pos];
    return c;
  }
  
  private char getUTF8() {
    // Byte code:
    //   0: bipush #63
    //   2: istore_1
    //   3: aload_0
    //   4: aload_0
    //   5: getfield pos : I
    //   8: invokespecial getByte : (I)I
    //   11: istore_2
    //   12: aload_0
    //   13: aload_0
    //   14: getfield pos : I
    //   17: iconst_1
    //   18: iadd
    //   19: putfield pos : I
    //   22: iload_2
    //   23: sipush #128
    //   26: if_icmpge -> 36
    //   29: iload_2
    //   30: i2c
    //   31: istore_2
    //   32: iload_2
    //   33: istore_3
    //   34: iload_3
    //   35: ireturn
    //   36: iload_1
    //   37: istore_3
    //   38: iload_2
    //   39: sipush #192
    //   42: if_icmplt -> 34
    //   45: iload_1
    //   46: istore_3
    //   47: iload_2
    //   48: sipush #247
    //   51: if_icmpgt -> 34
    //   54: iload_2
    //   55: sipush #223
    //   58: if_icmpgt -> 186
    //   61: iconst_1
    //   62: istore #4
    //   64: iload_2
    //   65: bipush #31
    //   67: iand
    //   68: istore_2
    //   69: iconst_0
    //   70: istore #5
    //   72: iload_2
    //   73: istore #6
    //   75: iload #5
    //   77: istore_2
    //   78: iload_2
    //   79: iload #4
    //   81: if_icmpge -> 215
    //   84: aload_0
    //   85: aload_0
    //   86: getfield pos : I
    //   89: iconst_1
    //   90: iadd
    //   91: putfield pos : I
    //   94: iload_1
    //   95: istore_3
    //   96: aload_0
    //   97: getfield pos : I
    //   100: aload_0
    //   101: getfield length : I
    //   104: if_icmpeq -> 34
    //   107: iload_1
    //   108: istore_3
    //   109: aload_0
    //   110: getfield chars : [C
    //   113: aload_0
    //   114: getfield pos : I
    //   117: caload
    //   118: bipush #92
    //   120: if_icmpne -> 34
    //   123: aload_0
    //   124: aload_0
    //   125: getfield pos : I
    //   128: iconst_1
    //   129: iadd
    //   130: putfield pos : I
    //   133: aload_0
    //   134: aload_0
    //   135: getfield pos : I
    //   138: invokespecial getByte : (I)I
    //   141: istore #5
    //   143: aload_0
    //   144: aload_0
    //   145: getfield pos : I
    //   148: iconst_1
    //   149: iadd
    //   150: putfield pos : I
    //   153: iload_1
    //   154: istore_3
    //   155: iload #5
    //   157: sipush #192
    //   160: iand
    //   161: sipush #128
    //   164: if_icmpne -> 34
    //   167: iload #6
    //   169: bipush #6
    //   171: ishl
    //   172: iload #5
    //   174: bipush #63
    //   176: iand
    //   177: iadd
    //   178: istore #6
    //   180: iinc #2, 1
    //   183: goto -> 78
    //   186: iload_2
    //   187: sipush #239
    //   190: if_icmpgt -> 204
    //   193: iconst_2
    //   194: istore #4
    //   196: iload_2
    //   197: bipush #15
    //   199: iand
    //   200: istore_2
    //   201: goto -> 69
    //   204: iconst_3
    //   205: istore #4
    //   207: iload_2
    //   208: bipush #7
    //   210: iand
    //   211: istore_2
    //   212: goto -> 69
    //   215: iload #6
    //   217: i2c
    //   218: istore_2
    //   219: iload_2
    //   220: istore_3
    //   221: goto -> 34
  }
  
  private String hexAV() {
    if (this.pos + 4 >= this.length)
      throw new IllegalStateException("Unexpected end of DN: " + this.dn); 
    this.beg = this.pos;
    this.pos++;
    while (true) {
      if (this.pos == this.length || this.chars[this.pos] == '+' || this.chars[this.pos] == ',' || this.chars[this.pos] == ';') {
        this.end = this.pos;
        continue;
      } 
      if (this.chars[this.pos] == ' ') {
        this.end = this.pos;
        this.pos++;
        while (true) {
          if (this.pos < this.length && this.chars[this.pos] == ' ') {
            this.pos++;
            continue;
          } 
          int i = this.end - this.beg;
          if (i < 5 || (i & 0x1) == 0)
            throw new IllegalStateException("Unexpected end of DN: " + this.dn); 
          byte[] arrayOfByte = new byte[i / 2];
          byte b = 0;
          int j = this.beg + 1;
          while (b < arrayOfByte.length) {
            arrayOfByte[b] = (byte)(byte)getByte(j);
            j += 2;
            b++;
          } 
          return new String(this.chars, this.beg, i);
        } 
        break;
      } 
      if (this.chars[this.pos] >= 'A' && this.chars[this.pos] <= 'F') {
        char[] arrayOfChar = this.chars;
        int i = this.pos;
        arrayOfChar[i] = (char)(char)(arrayOfChar[i] + 32);
      } 
      this.pos++;
    } 
  }
  
  private String nextAT() {
    while (this.pos < this.length && this.chars[this.pos] == ' ')
      this.pos++; 
    if (this.pos == this.length)
      return null; 
    this.beg = this.pos;
    this.pos++;
    while (this.pos < this.length && this.chars[this.pos] != '=' && this.chars[this.pos] != ' ')
      this.pos++; 
    if (this.pos >= this.length)
      throw new IllegalStateException("Unexpected end of DN: " + this.dn); 
    this.end = this.pos;
    if (this.chars[this.pos] == ' ') {
      while (this.pos < this.length && this.chars[this.pos] != '=' && this.chars[this.pos] == ' ')
        this.pos++; 
      if (this.chars[this.pos] != '=' || this.pos == this.length)
        throw new IllegalStateException("Unexpected end of DN: " + this.dn); 
    } 
    this.pos++;
    while (this.pos < this.length && this.chars[this.pos] == ' ')
      this.pos++; 
    if (this.end - this.beg > 4 && this.chars[this.beg + 3] == '.' && (this.chars[this.beg] == 'O' || this.chars[this.beg] == 'o') && (this.chars[this.beg + 1] == 'I' || this.chars[this.beg + 1] == 'i') && (this.chars[this.beg + 2] == 'D' || this.chars[this.beg + 2] == 'd'))
      this.beg += 4; 
    return new String(this.chars, this.beg, this.end - this.beg);
  }
  
  private String quotedAV() {
    this.beg = ++this.pos;
    this.end = this.beg;
    while (true) {
      if (this.pos == this.length)
        throw new IllegalStateException("Unexpected end of DN: " + this.dn); 
      if (this.chars[this.pos] == '"') {
        this.pos++;
        while (this.pos < this.length && this.chars[this.pos] == ' ')
          this.pos++; 
        break;
      } 
      if (this.chars[this.pos] == '\\') {
        this.chars[this.end] = getEscaped();
      } else {
        this.chars[this.end] = (char)this.chars[this.pos];
      } 
      this.pos++;
      this.end++;
    } 
    return new String(this.chars, this.beg, this.end - this.beg);
  }
  
  public String findMostSpecific(String paramString) {
    this.pos = 0;
    this.beg = 0;
    this.end = 0;
    this.cur = 0;
    this.chars = this.dn.toCharArray();
    String str1 = nextAT();
    String str2 = str1;
    if (str1 == null)
      return null; 
    while (true) {
      str1 = "";
      if (this.pos == this.length)
        return null; 
      switch (this.chars[this.pos]) {
        default:
          str1 = escapedAV();
        case '+':
        case ',':
        case ';':
          if (!paramString.equalsIgnoreCase(str2)) {
            if (this.pos >= this.length)
              return null; 
            break;
          } 
          return str1;
        case '"':
          str1 = quotedAV();
        case '#':
          str1 = hexAV();
      } 
      if (this.chars[this.pos] != ',' && this.chars[this.pos] != ';' && this.chars[this.pos] != '+')
        throw new IllegalStateException("Malformed DN: " + this.dn); 
      this.pos++;
      str1 = nextAT();
      str2 = str1;
      if (str1 == null)
        throw new IllegalStateException("Malformed DN: " + this.dn); 
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/tls/DistinguishedNameParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */