package okio;

final class Base64 {
  private static final byte[] MAP = new byte[] { 
      65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 
      75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 
      85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 
      101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 
      111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 
      121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 
      56, 57, 43, 47 };
  
  private static final byte[] URL_MAP = new byte[] { 
      65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 
      75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 
      85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 
      101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 
      111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 
      121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 
      56, 57, 45, 95 };
  
  public static byte[] decode(String paramString) {
    int k;
    int i = paramString.length();
    while (true) {
      Object object;
      if (i > 0) {
        char c = paramString.charAt(i - 1);
        if (c == '=' || c == '\n' || c == '\r' || c == ' ' || c == '\t') {
          i--;
          continue;
        } 
      } 
      byte[] arrayOfByte = new byte[(int)(i * 6L / 8L)];
      int n = 0;
      boolean bool = false;
      byte b = 0;
      int m = 0;
      while (true) {
        int i1;
        if (b < i) {
          Object object1;
          char c = paramString.charAt(b);
          if (c >= 'A' && c <= 'Z') {
            int i3 = c - 65;
          } else if (c >= 'a' && c <= 'z') {
            int i3 = c - 71;
          } else if (c >= '0' && c <= '9') {
            int i3 = c + 4;
          } else if (c == '+' || c == '-') {
            byte b1 = 62;
          } else if (c == '/' || c == '_') {
            byte b1 = 63;
          } else {
            int i3 = n;
            object1 = object;
            if (c != '\n') {
              i3 = n;
              object1 = object;
              if (c != '\r') {
                i3 = n;
                object1 = object;
                if (c != ' ') {
                  if (c == '\t') {
                    object1 = object;
                  } else {
                    return null;
                  } 
                  continue;
                } 
              } 
            } 
            n = i3;
          } 
          i1 = object << 6 | (byte)object1;
          k = ++n;
          int i2 = i1;
          if (n % 4 == 0) {
            k = m + 1;
            arrayOfByte[m] = (byte)(byte)(i1 >> 16);
            i2 = k + 1;
            arrayOfByte[k] = (byte)(byte)(i1 >> 8);
            m = i2 + 1;
            arrayOfByte[i2] = (byte)(byte)i1;
            i2 = i1;
            continue;
          } 
        } else {
          int i2;
          n %= 4;
          if (n == 1)
            return null; 
          if (n == 2) {
            i2 = m + 1;
            arrayOfByte[m] = (byte)(byte)(i1 << 12 >> 16);
          } else {
            i2 = m;
            if (n == 3) {
              i1 <<= 6;
              n = m + 1;
              arrayOfByte[m] = (byte)(byte)(i1 >> 16);
              i2 = n + 1;
              arrayOfByte[n] = (byte)(byte)(i1 >> 8);
            } 
          } 
          byte[] arrayOfByte1 = arrayOfByte;
          if (i2 != arrayOfByte.length) {
            arrayOfByte1 = new byte[i2];
            System.arraycopy(arrayOfByte, 0, arrayOfByte1, 0, i2);
          } 
          return arrayOfByte1;
        } 
        n = k;
        b++;
        object = SYNTHETIC_LOCAL_VARIABLE_8;
      } 
    } 
    int j = k;
  }
  
  public static String encode(byte[] paramArrayOfbyte) {
    return encode(paramArrayOfbyte, MAP);
  }
  
  private static String encode(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    // Byte code:
    //   0: aload_0
    //   1: arraylength
    //   2: iconst_2
    //   3: iadd
    //   4: iconst_3
    //   5: idiv
    //   6: iconst_4
    //   7: imul
    //   8: newarray byte
    //   10: astore_2
    //   11: aload_0
    //   12: arraylength
    //   13: aload_0
    //   14: arraylength
    //   15: iconst_3
    //   16: irem
    //   17: isub
    //   18: istore_3
    //   19: iconst_0
    //   20: istore #4
    //   22: iconst_0
    //   23: istore #5
    //   25: iload #4
    //   27: iload_3
    //   28: if_icmpge -> 154
    //   31: iload #5
    //   33: iconst_1
    //   34: iadd
    //   35: istore #6
    //   37: aload_2
    //   38: iload #5
    //   40: aload_1
    //   41: aload_0
    //   42: iload #4
    //   44: baload
    //   45: sipush #255
    //   48: iand
    //   49: iconst_2
    //   50: ishr
    //   51: baload
    //   52: i2b
    //   53: bastore
    //   54: iload #6
    //   56: iconst_1
    //   57: iadd
    //   58: istore #5
    //   60: aload_2
    //   61: iload #6
    //   63: aload_1
    //   64: aload_0
    //   65: iload #4
    //   67: baload
    //   68: iconst_3
    //   69: iand
    //   70: iconst_4
    //   71: ishl
    //   72: aload_0
    //   73: iload #4
    //   75: iconst_1
    //   76: iadd
    //   77: baload
    //   78: sipush #255
    //   81: iand
    //   82: iconst_4
    //   83: ishr
    //   84: ior
    //   85: baload
    //   86: i2b
    //   87: bastore
    //   88: iload #5
    //   90: iconst_1
    //   91: iadd
    //   92: istore #6
    //   94: aload_2
    //   95: iload #5
    //   97: aload_1
    //   98: aload_0
    //   99: iload #4
    //   101: iconst_1
    //   102: iadd
    //   103: baload
    //   104: bipush #15
    //   106: iand
    //   107: iconst_2
    //   108: ishl
    //   109: aload_0
    //   110: iload #4
    //   112: iconst_2
    //   113: iadd
    //   114: baload
    //   115: sipush #255
    //   118: iand
    //   119: bipush #6
    //   121: ishr
    //   122: ior
    //   123: baload
    //   124: i2b
    //   125: bastore
    //   126: iload #6
    //   128: iconst_1
    //   129: iadd
    //   130: istore #5
    //   132: aload_2
    //   133: iload #6
    //   135: aload_1
    //   136: aload_0
    //   137: iload #4
    //   139: iconst_2
    //   140: iadd
    //   141: baload
    //   142: bipush #63
    //   144: iand
    //   145: baload
    //   146: i2b
    //   147: bastore
    //   148: iinc #4, 3
    //   151: goto -> 25
    //   154: aload_0
    //   155: arraylength
    //   156: iconst_3
    //   157: irem
    //   158: tableswitch default -> 180, 1 -> 193, 2 -> 258
    //   180: new java/lang/String
    //   183: dup
    //   184: aload_2
    //   185: ldc 'US-ASCII'
    //   187: invokespecial <init> : ([BLjava/lang/String;)V
    //   190: astore_0
    //   191: aload_0
    //   192: areturn
    //   193: iload #5
    //   195: iconst_1
    //   196: iadd
    //   197: istore #4
    //   199: aload_2
    //   200: iload #5
    //   202: aload_1
    //   203: aload_0
    //   204: iload_3
    //   205: baload
    //   206: sipush #255
    //   209: iand
    //   210: iconst_2
    //   211: ishr
    //   212: baload
    //   213: i2b
    //   214: bastore
    //   215: iload #4
    //   217: iconst_1
    //   218: iadd
    //   219: istore #5
    //   221: aload_2
    //   222: iload #4
    //   224: aload_1
    //   225: aload_0
    //   226: iload_3
    //   227: baload
    //   228: iconst_3
    //   229: iand
    //   230: iconst_4
    //   231: ishl
    //   232: baload
    //   233: i2b
    //   234: bastore
    //   235: iload #5
    //   237: iconst_1
    //   238: iadd
    //   239: istore #4
    //   241: aload_2
    //   242: iload #5
    //   244: bipush #61
    //   246: i2b
    //   247: bastore
    //   248: aload_2
    //   249: iload #4
    //   251: bipush #61
    //   253: i2b
    //   254: bastore
    //   255: goto -> 180
    //   258: iload #5
    //   260: iconst_1
    //   261: iadd
    //   262: istore #4
    //   264: aload_2
    //   265: iload #5
    //   267: aload_1
    //   268: aload_0
    //   269: iload_3
    //   270: baload
    //   271: sipush #255
    //   274: iand
    //   275: iconst_2
    //   276: ishr
    //   277: baload
    //   278: i2b
    //   279: bastore
    //   280: iload #4
    //   282: iconst_1
    //   283: iadd
    //   284: istore #5
    //   286: aload_2
    //   287: iload #4
    //   289: aload_1
    //   290: aload_0
    //   291: iload_3
    //   292: baload
    //   293: iconst_3
    //   294: iand
    //   295: iconst_4
    //   296: ishl
    //   297: aload_0
    //   298: iload_3
    //   299: iconst_1
    //   300: iadd
    //   301: baload
    //   302: sipush #255
    //   305: iand
    //   306: iconst_4
    //   307: ishr
    //   308: ior
    //   309: baload
    //   310: i2b
    //   311: bastore
    //   312: iload #5
    //   314: iconst_1
    //   315: iadd
    //   316: istore #4
    //   318: aload_2
    //   319: iload #5
    //   321: aload_1
    //   322: aload_0
    //   323: iload_3
    //   324: iconst_1
    //   325: iadd
    //   326: baload
    //   327: bipush #15
    //   329: iand
    //   330: iconst_2
    //   331: ishl
    //   332: baload
    //   333: i2b
    //   334: bastore
    //   335: iload #4
    //   337: iconst_1
    //   338: iadd
    //   339: istore #5
    //   341: aload_2
    //   342: iload #4
    //   344: bipush #61
    //   346: i2b
    //   347: bastore
    //   348: goto -> 180
    //   351: astore_0
    //   352: new java/lang/AssertionError
    //   355: dup
    //   356: aload_0
    //   357: invokespecial <init> : (Ljava/lang/Object;)V
    //   360: athrow
    // Exception table:
    //   from	to	target	type
    //   180	191	351	java/io/UnsupportedEncodingException
  }
  
  public static String encodeUrl(byte[] paramArrayOfbyte) {
    return encode(paramArrayOfbyte, URL_MAP);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okio/Base64.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */