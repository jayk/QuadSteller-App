package com.google.gson.stream;

import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

public class JsonReader implements Closeable {
  private static final long MIN_INCOMPLETE_INTEGER = -922337203685477580L;
  
  private static final char[] NON_EXECUTE_PREFIX = ")]}'\n".toCharArray();
  
  private static final int NUMBER_CHAR_DECIMAL = 3;
  
  private static final int NUMBER_CHAR_DIGIT = 2;
  
  private static final int NUMBER_CHAR_EXP_DIGIT = 7;
  
  private static final int NUMBER_CHAR_EXP_E = 5;
  
  private static final int NUMBER_CHAR_EXP_SIGN = 6;
  
  private static final int NUMBER_CHAR_FRACTION_DIGIT = 4;
  
  private static final int NUMBER_CHAR_NONE = 0;
  
  private static final int NUMBER_CHAR_SIGN = 1;
  
  private static final int PEEKED_BEGIN_ARRAY = 3;
  
  private static final int PEEKED_BEGIN_OBJECT = 1;
  
  private static final int PEEKED_BUFFERED = 11;
  
  private static final int PEEKED_DOUBLE_QUOTED = 9;
  
  private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
  
  private static final int PEEKED_END_ARRAY = 4;
  
  private static final int PEEKED_END_OBJECT = 2;
  
  private static final int PEEKED_EOF = 17;
  
  private static final int PEEKED_FALSE = 6;
  
  private static final int PEEKED_LONG = 15;
  
  private static final int PEEKED_NONE = 0;
  
  private static final int PEEKED_NULL = 7;
  
  private static final int PEEKED_NUMBER = 16;
  
  private static final int PEEKED_SINGLE_QUOTED = 8;
  
  private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
  
  private static final int PEEKED_TRUE = 5;
  
  private static final int PEEKED_UNQUOTED = 10;
  
  private static final int PEEKED_UNQUOTED_NAME = 14;
  
  private final char[] buffer = new char[1024];
  
  private final Reader in;
  
  private boolean lenient = false;
  
  private int limit = 0;
  
  private int lineNumber = 0;
  
  private int lineStart = 0;
  
  private int peeked = 0;
  
  private long peekedLong;
  
  private int peekedNumberLength;
  
  private String peekedString;
  
  private int pos = 0;
  
  private int[] stack = new int[32];
  
  private int stackSize = 0;
  
  static {
    JsonReaderInternalAccess.INSTANCE = new JsonReaderInternalAccess() {
        public void promoteNameToValue(JsonReader param1JsonReader) throws IOException {
          if (param1JsonReader instanceof JsonTreeReader) {
            ((JsonTreeReader)param1JsonReader).promoteNameToValue();
            return;
          } 
          int i = param1JsonReader.peeked;
          int j = i;
          if (i == 0)
            j = param1JsonReader.doPeek(); 
          if (j == 13) {
            JsonReader.access$002(param1JsonReader, 9);
            return;
          } 
          if (j == 12) {
            JsonReader.access$002(param1JsonReader, 8);
            return;
          } 
          if (j == 14) {
            JsonReader.access$002(param1JsonReader, 10);
            return;
          } 
          throw new IllegalStateException("Expected a name but was " + param1JsonReader.peek() + " " + " at line " + param1JsonReader.getLineNumber() + " column " + param1JsonReader.getColumnNumber());
        }
      };
  }
  
  public JsonReader(Reader paramReader) {
    int[] arrayOfInt = this.stack;
    int i = this.stackSize;
    this.stackSize = i + 1;
    arrayOfInt[i] = 6;
    if (paramReader == null)
      throw new NullPointerException("in == null"); 
    this.in = paramReader;
  }
  
  private void checkLenient() throws IOException {
    if (!this.lenient)
      throw syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON"); 
  }
  
  private void consumeNonExecutePrefix() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: iconst_1
    //   2: invokespecial nextNonWhitespace : (Z)I
    //   5: pop
    //   6: aload_0
    //   7: aload_0
    //   8: getfield pos : I
    //   11: iconst_1
    //   12: isub
    //   13: putfield pos : I
    //   16: aload_0
    //   17: getfield pos : I
    //   20: getstatic com/google/gson/stream/JsonReader.NON_EXECUTE_PREFIX : [C
    //   23: arraylength
    //   24: iadd
    //   25: aload_0
    //   26: getfield limit : I
    //   29: if_icmple -> 44
    //   32: aload_0
    //   33: getstatic com/google/gson/stream/JsonReader.NON_EXECUTE_PREFIX : [C
    //   36: arraylength
    //   37: invokespecial fillBuffer : (I)Z
    //   40: ifne -> 44
    //   43: return
    //   44: iconst_0
    //   45: istore_1
    //   46: iload_1
    //   47: getstatic com/google/gson/stream/JsonReader.NON_EXECUTE_PREFIX : [C
    //   50: arraylength
    //   51: if_icmpge -> 79
    //   54: aload_0
    //   55: getfield buffer : [C
    //   58: aload_0
    //   59: getfield pos : I
    //   62: iload_1
    //   63: iadd
    //   64: caload
    //   65: getstatic com/google/gson/stream/JsonReader.NON_EXECUTE_PREFIX : [C
    //   68: iload_1
    //   69: caload
    //   70: if_icmpne -> 43
    //   73: iinc #1, 1
    //   76: goto -> 46
    //   79: aload_0
    //   80: aload_0
    //   81: getfield pos : I
    //   84: getstatic com/google/gson/stream/JsonReader.NON_EXECUTE_PREFIX : [C
    //   87: arraylength
    //   88: iadd
    //   89: putfield pos : I
    //   92: goto -> 43
  }
  
  private int doPeek() throws IOException {
    int i = 4;
    int j = this.stack[this.stackSize - 1];
    if (j == 1)
      this.stack[this.stackSize - 1] = 2; 
    if (j == 2) {
      switch (nextNonWhitespace(true)) {
        case 44:
          switch (nextNonWhitespace(true)) {
            default:
              this.pos--;
              if (this.stackSize == 1)
                checkLenient(); 
              i = peekKeyword();
              if (i != 0)
                return i; 
              break;
            case 93:
              if (j == 1) {
                this.peeked = 4;
                return i;
              } 
            case 44:
            case 59:
              if (j == 1 || j == 2) {
                checkLenient();
                this.pos--;
                this.peeked = 7;
                return 7;
              } 
              throw syntaxError("Unexpected value");
            case 39:
              checkLenient();
              i = 8;
              this.peeked = 8;
              return i;
            case 34:
              if (this.stackSize == 1)
                checkLenient(); 
              i = 9;
              this.peeked = 9;
              return i;
            case 91:
              i = 3;
              this.peeked = 3;
              return i;
            case 123:
              this.peeked = 1;
              return 1;
          } 
          break;
        default:
          throw syntaxError("Unterminated array");
        case 93:
          this.peeked = 4;
          return i;
        case 59:
          checkLenient();
      } 
    } else {
      if (j == 3 || j == 5) {
        this.stack[this.stackSize - 1] = 4;
        if (j == 5)
          switch (nextNonWhitespace(true)) {
            default:
              throw syntaxError("Unterminated object");
            case 125:
              this.peeked = 2;
              return 2;
            case 59:
              checkLenient();
              break;
            case 44:
              break;
          }  
        i = nextNonWhitespace(true);
        switch (i) {
          default:
            checkLenient();
            this.pos--;
            if (isLiteral((char)i)) {
              i = 14;
              this.peeked = 14;
              return i;
            } 
            throw syntaxError("Expected name");
          case 34:
            i = 13;
            this.peeked = 13;
            return i;
          case 39:
            checkLenient();
            i = 12;
            this.peeked = 12;
            return i;
          case 125:
            break;
        } 
        if (j != 5) {
          this.peeked = 2;
          return 2;
        } 
        throw syntaxError("Expected name");
      } 
      if (j == 4) {
        this.stack[this.stackSize - 1] = 5;
        switch (nextNonWhitespace(true)) {
          case 58:
          
          default:
            throw syntaxError("Expected ':'");
          case 61:
            break;
        } 
        checkLenient();
        if ((this.pos < this.limit || fillBuffer(1)) && this.buffer[this.pos] == '>')
          this.pos++; 
      } 
      if (j == 6) {
        if (this.lenient)
          consumeNonExecutePrefix(); 
        this.stack[this.stackSize - 1] = 7;
      } 
      if (j == 7) {
        if (nextNonWhitespace(false) == -1) {
          i = 17;
          this.peeked = 17;
          return i;
        } 
        checkLenient();
        this.pos--;
      } 
      if (j == 8)
        throw new IllegalStateException("JsonReader is closed"); 
    } 
    j = peekNumber();
    i = j;
    if (j == 0) {
      if (!isLiteral(this.buffer[this.pos]))
        throw syntaxError("Expected value"); 
      checkLenient();
      i = 10;
      this.peeked = 10;
    } 
    return i;
  }
  
  private boolean fillBuffer(int paramInt) throws IOException {
    boolean bool2;
    boolean bool1 = false;
    char[] arrayOfChar = this.buffer;
    this.lineStart -= this.pos;
    if (this.limit != this.pos) {
      this.limit -= this.pos;
      System.arraycopy(arrayOfChar, this.pos, arrayOfChar, 0, this.limit);
    } else {
      this.limit = 0;
    } 
    this.pos = 0;
    while (true) {
      int i = this.in.read(arrayOfChar, this.limit, arrayOfChar.length - this.limit);
      bool2 = bool1;
      if (i != -1) {
        this.limit += i;
        i = paramInt;
        if (this.lineNumber == 0) {
          i = paramInt;
          if (this.lineStart == 0) {
            i = paramInt;
            if (this.limit > 0) {
              i = paramInt;
              if (arrayOfChar[0] == '﻿') {
                this.pos++;
                this.lineStart++;
                i = paramInt + 1;
              } 
            } 
          } 
        } 
        paramInt = i;
        if (this.limit >= i) {
          bool2 = true;
          break;
        } 
        continue;
      } 
      break;
    } 
    return bool2;
  }
  
  private int getColumnNumber() {
    return this.pos - this.lineStart + 1;
  }
  
  private int getLineNumber() {
    return this.lineNumber + 1;
  }
  
  private boolean isLiteral(char paramChar) throws IOException {
    switch (paramChar) {
      default:
        return true;
      case '#':
      case '/':
      case ';':
      case '=':
      case '\\':
        checkLenient();
        break;
      case '\t':
      case '\n':
      case '\f':
      case '\r':
      case ' ':
      case ',':
      case ':':
      case '[':
      case ']':
      case '{':
      case '}':
        break;
    } 
    return false;
  }
  
  private int nextNonWhitespace(boolean paramBoolean) throws IOException {
    char[] arrayOfChar = this.buffer;
    int i = this.pos;
    for (int j = this.limit;; j = arrayOfChar[m]) {
      int k = j;
      int m = i;
      if (i == j) {
        this.pos = i;
        if (!fillBuffer(1)) {
          if (paramBoolean)
            throw new EOFException("End of input at line " + getLineNumber() + " column " + getColumnNumber()); 
        } else {
          m = this.pos;
          k = this.limit;
          i = m + 1;
          j = arrayOfChar[m];
        } 
        return -1;
      } 
      i = m + 1;
    } 
  }
  
  private String nextQuotedValue(char paramChar) throws IOException {
    char[] arrayOfChar = this.buffer;
    StringBuilder stringBuilder = new StringBuilder();
    while (true) {
      int i = this.pos;
      int j = this.limit;
      int k;
      for (k = i; i < j; k = i1) {
        int n;
        int i1;
        int m = i + 1;
        char c = arrayOfChar[i];
        if (c == paramChar) {
          this.pos = m;
          stringBuilder.append(arrayOfChar, k, m - k - 1);
          return stringBuilder.toString();
        } 
        if (c == '\\') {
          this.pos = m;
          stringBuilder.append(arrayOfChar, k, m - k - 1);
          stringBuilder.append(readEscapeCharacter());
          i = this.pos;
          n = this.limit;
          i1 = i;
        } else {
          n = j;
          i = m;
          i1 = k;
          if (c == '\n') {
            this.lineNumber++;
            this.lineStart = m;
            n = j;
            i = m;
            i1 = k;
          } 
        } 
        j = n;
      } 
      stringBuilder.append(arrayOfChar, k, i - k);
      this.pos = i;
      if (!fillBuffer(1))
        throw syntaxError("Unterminated string"); 
    } 
  }
  
  private String nextUnquotedValue() throws IOException {
    String str = null;
    byte b = 0;
    while (true) {
      StringBuilder stringBuilder;
      byte b1;
      while (true) {
        String str1;
        if (this.pos + b < this.limit) {
          str1 = str;
          b1 = b;
          switch (this.buffer[this.pos + b]) {
            default:
              b++;
              continue;
            case '#':
            case '/':
            case ';':
            case '=':
            case '\\':
              checkLenient();
              b1 = b;
              str1 = str;
              break;
            case '\t':
              continue;
            case '\n':
              continue;
            case '\f':
              continue;
            case '\r':
              continue;
            case ' ':
              continue;
            case ',':
              continue;
            case ':':
              continue;
            case '[':
              continue;
            case ']':
              continue;
            case '{':
              continue;
            case '}':
              continue;
          } 
        } else {
          break;
        } 
        if (str1 == null) {
          str = new String(this.buffer, this.pos, b1);
          this.pos += b1;
          return str;
        } 
        str1.append(this.buffer, this.pos, b1);
        str = str1.toString();
        this.pos += b1;
        return str;
      } 
      if (b < this.buffer.length) {
        String str1 = str;
        b1 = b;
        if (fillBuffer(b + 1))
          continue; 
      } else {
        String str1 = str;
        if (str == null)
          stringBuilder = new StringBuilder(); 
        stringBuilder.append(this.buffer, this.pos, b);
        this.pos += b;
        b1 = 0;
        b = 0;
        StringBuilder stringBuilder1 = stringBuilder;
        if (!fillBuffer(1)) {
          if (stringBuilder == null) {
            String str3 = new String(this.buffer, this.pos, b1);
            this.pos += b1;
            return str3;
          } 
          stringBuilder.append(this.buffer, this.pos, b1);
          String str2 = stringBuilder.toString();
          this.pos += b1;
          return str2;
        } 
        continue;
      } 
      if (stringBuilder == null) {
        str = new String(this.buffer, this.pos, b1);
        this.pos += b1;
        return str;
      } 
      stringBuilder.append(this.buffer, this.pos, b1);
      str = stringBuilder.toString();
      this.pos += b1;
      return str;
    } 
  }
  
  private int peekKeyword() throws IOException {
    String str1;
    String str2;
    char c = this.buffer[this.pos];
    if (c == 't' || c == 'T') {
      str1 = "true";
      str2 = "TRUE";
      c = '\005';
    } else if (c == 'f' || c == 'F') {
      str1 = "false";
      str2 = "FALSE";
      c = '\006';
    } else if (c == 'n' || c == 'N') {
      str1 = "null";
      str2 = "NULL";
      c = '\007';
    } else {
      return 0;
    } 
    int i = str1.length();
    for (byte b = 1; b < i; b++) {
      if (this.pos + b >= this.limit && !fillBuffer(b + 1))
        return 0; 
      char c1 = this.buffer[this.pos + b];
      if (c1 != str1.charAt(b) && c1 != str2.charAt(b))
        return 0; 
    } 
    if ((this.pos + i < this.limit || fillBuffer(i + 1)) && isLiteral(this.buffer[this.pos + i]))
      return 0; 
    this.pos += i;
    this.peeked = c;
    return c;
  }
  
  private int peekNumber() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield buffer : [C
    //   4: astore_1
    //   5: aload_0
    //   6: getfield pos : I
    //   9: istore_2
    //   10: aload_0
    //   11: getfield limit : I
    //   14: istore_3
    //   15: lconst_0
    //   16: lstore #4
    //   18: iconst_0
    //   19: istore #6
    //   21: iconst_1
    //   22: istore #7
    //   24: iconst_0
    //   25: istore #8
    //   27: iconst_0
    //   28: istore #9
    //   30: iload_3
    //   31: istore #10
    //   33: iload_2
    //   34: istore #11
    //   36: iload_2
    //   37: iload #9
    //   39: iadd
    //   40: iload_3
    //   41: if_icmpne -> 137
    //   44: iload #9
    //   46: aload_1
    //   47: arraylength
    //   48: if_icmpne -> 55
    //   51: iconst_0
    //   52: istore_2
    //   53: iload_2
    //   54: ireturn
    //   55: aload_0
    //   56: iload #9
    //   58: iconst_1
    //   59: iadd
    //   60: invokespecial fillBuffer : (I)Z
    //   63: ifne -> 125
    //   66: iload #8
    //   68: iconst_2
    //   69: if_icmpne -> 574
    //   72: iload #7
    //   74: ifeq -> 574
    //   77: lload #4
    //   79: ldc2_w -9223372036854775808
    //   82: lcmp
    //   83: ifne -> 91
    //   86: iload #6
    //   88: ifeq -> 574
    //   91: iload #6
    //   93: ifeq -> 566
    //   96: aload_0
    //   97: lload #4
    //   99: putfield peekedLong : J
    //   102: aload_0
    //   103: aload_0
    //   104: getfield pos : I
    //   107: iload #9
    //   109: iadd
    //   110: putfield pos : I
    //   113: bipush #15
    //   115: istore_2
    //   116: aload_0
    //   117: bipush #15
    //   119: putfield peeked : I
    //   122: goto -> 53
    //   125: aload_0
    //   126: getfield pos : I
    //   129: istore #11
    //   131: aload_0
    //   132: getfield limit : I
    //   135: istore #10
    //   137: aload_1
    //   138: iload #11
    //   140: iload #9
    //   142: iadd
    //   143: caload
    //   144: istore #12
    //   146: iload #12
    //   148: lookupswitch default -> 200, 43 -> 299, 45 -> 228, 46 -> 360, 69 -> 327, 101 -> 327
    //   200: iload #12
    //   202: bipush #48
    //   204: if_icmplt -> 214
    //   207: iload #12
    //   209: bipush #57
    //   211: if_icmple -> 387
    //   214: aload_0
    //   215: iload #12
    //   217: invokespecial isLiteral : (C)Z
    //   220: ifeq -> 66
    //   223: iconst_0
    //   224: istore_2
    //   225: goto -> 53
    //   228: iload #8
    //   230: ifne -> 271
    //   233: iconst_1
    //   234: istore #13
    //   236: iconst_1
    //   237: istore_2
    //   238: lload #4
    //   240: lstore #14
    //   242: iload #7
    //   244: istore_3
    //   245: iinc #9, 1
    //   248: iload_3
    //   249: istore #7
    //   251: iload #10
    //   253: istore_3
    //   254: iload_2
    //   255: istore #8
    //   257: iload #13
    //   259: istore #6
    //   261: iload #11
    //   263: istore_2
    //   264: lload #14
    //   266: lstore #4
    //   268: goto -> 30
    //   271: iload #8
    //   273: iconst_5
    //   274: if_icmpne -> 294
    //   277: bipush #6
    //   279: istore_2
    //   280: iload #7
    //   282: istore_3
    //   283: iload #6
    //   285: istore #13
    //   287: lload #4
    //   289: lstore #14
    //   291: goto -> 245
    //   294: iconst_0
    //   295: istore_2
    //   296: goto -> 53
    //   299: iload #8
    //   301: iconst_5
    //   302: if_icmpne -> 322
    //   305: bipush #6
    //   307: istore_2
    //   308: iload #7
    //   310: istore_3
    //   311: iload #6
    //   313: istore #13
    //   315: lload #4
    //   317: lstore #14
    //   319: goto -> 245
    //   322: iconst_0
    //   323: istore_2
    //   324: goto -> 53
    //   327: iload #8
    //   329: iconst_2
    //   330: if_icmpeq -> 339
    //   333: iload #8
    //   335: iconst_4
    //   336: if_icmpne -> 355
    //   339: iconst_5
    //   340: istore_2
    //   341: iload #7
    //   343: istore_3
    //   344: iload #6
    //   346: istore #13
    //   348: lload #4
    //   350: lstore #14
    //   352: goto -> 245
    //   355: iconst_0
    //   356: istore_2
    //   357: goto -> 53
    //   360: iload #8
    //   362: iconst_2
    //   363: if_icmpne -> 382
    //   366: iconst_3
    //   367: istore_2
    //   368: iload #7
    //   370: istore_3
    //   371: iload #6
    //   373: istore #13
    //   375: lload #4
    //   377: lstore #14
    //   379: goto -> 245
    //   382: iconst_0
    //   383: istore_2
    //   384: goto -> 53
    //   387: iload #8
    //   389: iconst_1
    //   390: if_icmpeq -> 398
    //   393: iload #8
    //   395: ifne -> 419
    //   398: iload #12
    //   400: bipush #48
    //   402: isub
    //   403: ineg
    //   404: i2l
    //   405: lstore #14
    //   407: iconst_2
    //   408: istore_2
    //   409: iload #7
    //   411: istore_3
    //   412: iload #6
    //   414: istore #13
    //   416: goto -> 245
    //   419: iload #8
    //   421: iconst_2
    //   422: if_icmpne -> 500
    //   425: lload #4
    //   427: lconst_0
    //   428: lcmp
    //   429: ifne -> 437
    //   432: iconst_0
    //   433: istore_2
    //   434: goto -> 53
    //   437: ldc2_w 10
    //   440: lload #4
    //   442: lmul
    //   443: iload #12
    //   445: bipush #48
    //   447: isub
    //   448: i2l
    //   449: lsub
    //   450: lstore #14
    //   452: lload #4
    //   454: ldc2_w -922337203685477580
    //   457: lcmp
    //   458: ifgt -> 478
    //   461: lload #4
    //   463: ldc2_w -922337203685477580
    //   466: lcmp
    //   467: ifne -> 495
    //   470: lload #14
    //   472: lload #4
    //   474: lcmp
    //   475: ifge -> 495
    //   478: iconst_1
    //   479: istore_2
    //   480: iload #7
    //   482: iload_2
    //   483: iand
    //   484: istore_3
    //   485: iload #8
    //   487: istore_2
    //   488: iload #6
    //   490: istore #13
    //   492: goto -> 245
    //   495: iconst_0
    //   496: istore_2
    //   497: goto -> 480
    //   500: iload #8
    //   502: iconst_3
    //   503: if_icmpne -> 522
    //   506: iconst_4
    //   507: istore_2
    //   508: iload #7
    //   510: istore_3
    //   511: iload #6
    //   513: istore #13
    //   515: lload #4
    //   517: lstore #14
    //   519: goto -> 245
    //   522: iload #8
    //   524: iconst_5
    //   525: if_icmpeq -> 549
    //   528: iload #7
    //   530: istore_3
    //   531: iload #8
    //   533: istore_2
    //   534: iload #6
    //   536: istore #13
    //   538: lload #4
    //   540: lstore #14
    //   542: iload #8
    //   544: bipush #6
    //   546: if_icmpne -> 245
    //   549: bipush #7
    //   551: istore_2
    //   552: iload #7
    //   554: istore_3
    //   555: iload #6
    //   557: istore #13
    //   559: lload #4
    //   561: lstore #14
    //   563: goto -> 245
    //   566: lload #4
    //   568: lneg
    //   569: lstore #4
    //   571: goto -> 96
    //   574: iload #8
    //   576: iconst_2
    //   577: if_icmpeq -> 593
    //   580: iload #8
    //   582: iconst_4
    //   583: if_icmpeq -> 593
    //   586: iload #8
    //   588: bipush #7
    //   590: if_icmpne -> 611
    //   593: aload_0
    //   594: iload #9
    //   596: putfield peekedNumberLength : I
    //   599: bipush #16
    //   601: istore_2
    //   602: aload_0
    //   603: bipush #16
    //   605: putfield peeked : I
    //   608: goto -> 53
    //   611: iconst_0
    //   612: istore_2
    //   613: goto -> 53
  }
  
  private void push(int paramInt) {
    if (this.stackSize == this.stack.length) {
      int[] arrayOfInt1 = new int[this.stackSize * 2];
      System.arraycopy(this.stack, 0, arrayOfInt1, 0, this.stackSize);
      this.stack = arrayOfInt1;
    } 
    int[] arrayOfInt = this.stack;
    int i = this.stackSize;
    this.stackSize = i + 1;
    arrayOfInt[i] = paramInt;
  }
  
  private char readEscapeCharacter() throws IOException {
    int j;
    int k;
    if (this.pos == this.limit && !fillBuffer(1))
      throw syntaxError("Unterminated escape sequence"); 
    char[] arrayOfChar = this.buffer;
    int i = this.pos;
    this.pos = i + 1;
    i = arrayOfChar[i];
    switch (i) {
      default:
        return i;
      case 117:
        if (this.pos + 4 > this.limit && !fillBuffer(4))
          throw syntaxError("Unterminated escape sequence"); 
        i = 0;
        j = this.pos;
        for (k = j; k < j + 4; k++) {
          char c = this.buffer[k];
          i = (char)(i << 4);
          if (c >= '0' && c <= '9') {
            i = (char)(c - 48 + i);
          } else if (c >= 'a' && c <= 'f') {
            i = (char)(c - 97 + 10 + i);
          } else if (c >= 'A' && c <= 'F') {
            i = (char)(c - 65 + 10 + i);
          } else {
            throw new NumberFormatException("\\u" + new String(this.buffer, this.pos, 4));
          } 
        } 
        this.pos += 4;
        return i;
      case 116:
        i = 9;
        return i;
      case 98:
        i = 8;
        return i;
      case 110:
        i = 10;
        return i;
      case 114:
        i = 13;
        return i;
      case 102:
        i = 12;
        return i;
      case 10:
        break;
    } 
    this.lineNumber++;
    this.lineStart = this.pos;
  }
  
  private void skipQuotedValue(char paramChar) throws IOException {
    char[] arrayOfChar = this.buffer;
    while (true) {
      int i = this.pos;
      for (int j = this.limit; i < j; j = m) {
        int m;
        int k = i + 1;
        char c = arrayOfChar[i];
        if (c == paramChar) {
          this.pos = k;
          return;
        } 
        if (c == '\\') {
          this.pos = k;
          readEscapeCharacter();
          i = this.pos;
          m = this.limit;
        } else {
          m = j;
          i = k;
          if (c == '\n') {
            this.lineNumber++;
            this.lineStart = k;
            m = j;
            i = k;
          } 
        } 
      } 
      this.pos = i;
      if (!fillBuffer(1))
        throw syntaxError("Unterminated string"); 
    } 
  }
  
  private boolean skipTo(String paramString) throws IOException {
    label18: while (true) {
      if (this.pos + paramString.length() <= this.limit || fillBuffer(paramString.length())) {
        if (this.buffer[this.pos] == '\n') {
          this.lineNumber++;
          this.lineStart = this.pos + 1;
          continue;
        } 
        byte b = 0;
        while (b < paramString.length()) {
          if (this.buffer[this.pos + b] == paramString.charAt(b)) {
            b++;
            continue;
          } 
          this.pos++;
          continue label18;
        } 
        return true;
      } 
      return false;
    } 
  }
  
  private void skipToEndOfLine() throws IOException {
    while (true) {
      int i;
      if (this.pos < this.limit || fillBuffer(1)) {
        char[] arrayOfChar = this.buffer;
        i = this.pos;
        this.pos = i + 1;
        i = arrayOfChar[i];
        if (i == 10) {
          this.lineNumber++;
          this.lineStart = this.pos;
          return;
        } 
      } else {
        return;
      } 
      if (i == 13)
        return; 
    } 
  }
  
  private void skipUnquotedValue() throws IOException {
    while (true) {
      byte b = 0;
      while (this.pos + b < this.limit) {
        switch (this.buffer[this.pos + b]) {
          default:
            b++;
            continue;
          case '#':
          case '/':
          case ';':
          case '=':
          case '\\':
            checkLenient();
            break;
          case '\t':
            continue;
          case '\n':
          case '\f':
          case '\r':
          case ' ':
          case ',':
          case ':':
          case '[':
          case ']':
          case '{':
          case '}':
            break;
        } 
        this.pos += b;
        return;
      } 
      this.pos += b;
      if (!fillBuffer(1))
        return; 
    } 
  }
  
  private IOException syntaxError(String paramString) throws IOException {
    throw new MalformedJsonException(paramString + " at line " + getLineNumber() + " column " + getColumnNumber());
  }
  
  public void beginArray() throws IOException {
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    if (j == 3) {
      push(1);
      this.peeked = 0;
      return;
    } 
    throw new IllegalStateException("Expected BEGIN_ARRAY but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
  }
  
  public void beginObject() throws IOException {
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    if (j == 1) {
      push(3);
      this.peeked = 0;
      return;
    } 
    throw new IllegalStateException("Expected BEGIN_OBJECT but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
  }
  
  public void close() throws IOException {
    this.peeked = 0;
    this.stack[0] = 8;
    this.stackSize = 1;
    this.in.close();
  }
  
  public void endArray() throws IOException {
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    if (j == 4) {
      this.stackSize--;
      this.peeked = 0;
      return;
    } 
    throw new IllegalStateException("Expected END_ARRAY but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
  }
  
  public void endObject() throws IOException {
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    if (j == 2) {
      this.stackSize--;
      this.peeked = 0;
      return;
    } 
    throw new IllegalStateException("Expected END_OBJECT but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
  }
  
  public boolean hasNext() throws IOException {
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    return (j != 2 && j != 4);
  }
  
  public final boolean isLenient() {
    return this.lenient;
  }
  
  public boolean nextBoolean() throws IOException {
    boolean bool = false;
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    if (j == 5) {
      this.peeked = 0;
      return true;
    } 
    if (j == 6) {
      this.peeked = 0;
      return bool;
    } 
    throw new IllegalStateException("Expected a boolean but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
  }
  
  public double nextDouble() throws IOException {
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    if (j == 15) {
      this.peeked = 0;
      return this.peekedLong;
    } 
    if (j == 16) {
      this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
      this.pos += this.peekedNumberLength;
    } else if (j == 8 || j == 9) {
      int k;
      if (j == 8) {
        j = 39;
        k = j;
      } else {
        j = 34;
        k = j;
      } 
      this.peekedString = nextQuotedValue(k);
    } else if (j == 10) {
      this.peekedString = nextUnquotedValue();
    } else if (j != 11) {
      throw new IllegalStateException("Expected a double but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
    } 
    this.peeked = 11;
    double d = Double.parseDouble(this.peekedString);
    if (!this.lenient && (Double.isNaN(d) || Double.isInfinite(d)))
      throw new MalformedJsonException("JSON forbids NaN and infinities: " + d + " at line " + getLineNumber() + " column " + getColumnNumber()); 
    this.peekedString = null;
    this.peeked = 0;
    return d;
  }
  
  public int nextInt() throws IOException {
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    if (j == 15) {
      j = (int)this.peekedLong;
      if (this.peekedLong != j)
        throw new NumberFormatException("Expected an int but was " + this.peekedLong + " at line " + getLineNumber() + " column " + getColumnNumber()); 
      this.peeked = 0;
      return j;
    } 
    if (j == 16) {
      this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
      this.pos += this.peekedNumberLength;
      this.peeked = 11;
      double d = Double.parseDouble(this.peekedString);
      j = (int)d;
      if (j != d)
        throw new NumberFormatException("Expected an int but was " + this.peekedString + " at line " + getLineNumber() + " column " + getColumnNumber()); 
    } else {
      if (j == 8 || j == 9) {
        int k;
        if (j == 8) {
          j = 39;
          k = j;
        } else {
          j = 34;
          k = j;
        } 
        this.peekedString = nextQuotedValue(k);
        try {
          j = Integer.parseInt(this.peekedString);
          this.peeked = 0;
        } catch (NumberFormatException numberFormatException) {}
        return j;
      } 
      throw new IllegalStateException("Expected an int but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
    } 
    this.peekedString = null;
    this.peeked = 0;
    return j;
  }
  
  public long nextLong() throws IOException {
    long l;
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    if (j == 15) {
      this.peeked = 0;
      return this.peekedLong;
    } 
    if (j == 16) {
      this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
      this.pos += this.peekedNumberLength;
      this.peeked = 11;
      double d = Double.parseDouble(this.peekedString);
      l = (long)d;
      if (l != d)
        throw new NumberFormatException("Expected a long but was " + this.peekedString + " at line " + getLineNumber() + " column " + getColumnNumber()); 
    } else {
      if (j == 8 || j == 9) {
        int k;
        if (j == 8) {
          j = 39;
          k = j;
        } else {
          j = 34;
          k = j;
        } 
        this.peekedString = nextQuotedValue(k);
        try {
          l = Long.parseLong(this.peekedString);
          this.peeked = 0;
        } catch (NumberFormatException numberFormatException) {}
        return l;
      } 
      throw new IllegalStateException("Expected a long but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
    } 
    this.peekedString = null;
    this.peeked = 0;
    return l;
  }
  
  public String nextName() throws IOException {
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    if (j == 14) {
      String str = nextUnquotedValue();
      this.peeked = 0;
      return str;
    } 
    if (j == 12) {
      String str = nextQuotedValue('\'');
      this.peeked = 0;
      return str;
    } 
    if (j == 13) {
      String str = nextQuotedValue('"');
      this.peeked = 0;
      return str;
    } 
    throw new IllegalStateException("Expected a name but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
  }
  
  public void nextNull() throws IOException {
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    if (j == 7) {
      this.peeked = 0;
      return;
    } 
    throw new IllegalStateException("Expected null but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
  }
  
  public String nextString() throws IOException {
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    if (j == 10) {
      String str = nextUnquotedValue();
      this.peeked = 0;
      return str;
    } 
    if (j == 8) {
      String str = nextQuotedValue('\'');
      this.peeked = 0;
      return str;
    } 
    if (j == 9) {
      String str = nextQuotedValue('"');
      this.peeked = 0;
      return str;
    } 
    if (j == 11) {
      String str = this.peekedString;
      this.peekedString = null;
      this.peeked = 0;
      return str;
    } 
    if (j == 15) {
      String str = Long.toString(this.peekedLong);
      this.peeked = 0;
      return str;
    } 
    if (j == 16) {
      String str = new String(this.buffer, this.pos, this.peekedNumberLength);
      this.pos += this.peekedNumberLength;
      this.peeked = 0;
      return str;
    } 
    throw new IllegalStateException("Expected a string but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber());
  }
  
  public JsonToken peek() throws IOException {
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    switch (j) {
      default:
        throw new AssertionError();
      case 1:
        return JsonToken.BEGIN_OBJECT;
      case 2:
        return JsonToken.END_OBJECT;
      case 3:
        return JsonToken.BEGIN_ARRAY;
      case 4:
        return JsonToken.END_ARRAY;
      case 12:
      case 13:
      case 14:
        return JsonToken.NAME;
      case 5:
      case 6:
        return JsonToken.BOOLEAN;
      case 7:
        return JsonToken.NULL;
      case 8:
      case 9:
      case 10:
      case 11:
        return JsonToken.STRING;
      case 15:
      case 16:
        return JsonToken.NUMBER;
      case 17:
        break;
    } 
    return JsonToken.END_DOCUMENT;
  }
  
  public final void setLenient(boolean paramBoolean) {
    this.lenient = paramBoolean;
  }
  
  public void skipValue() throws IOException {
    int i = 0;
    while (true) {
      int j = this.peeked;
      int k = j;
      if (j == 0)
        k = doPeek(); 
      if (k == 3) {
        push(1);
        j = i + 1;
      } else if (k == 1) {
        push(3);
        j = i + 1;
      } else if (k == 4) {
        this.stackSize--;
        j = i - 1;
      } else if (k == 2) {
        this.stackSize--;
        j = i - 1;
      } else if (k == 14 || k == 10) {
        skipUnquotedValue();
        j = i;
      } else if (k == 8 || k == 12) {
        skipQuotedValue('\'');
        j = i;
      } else if (k == 9 || k == 13) {
        skipQuotedValue('"');
        j = i;
      } else {
        j = i;
        if (k == 16) {
          this.pos += this.peekedNumberLength;
          j = i;
        } 
      } 
      this.peeked = 0;
      i = j;
      if (j == 0)
        return; 
    } 
  }
  
  public String toString() {
    return getClass().getSimpleName() + " at line " + getLineNumber() + " column " + getColumnNumber();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/google/gson/stream/JsonReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */