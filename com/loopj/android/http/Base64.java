package com.loopj.android.http;

import java.io.UnsupportedEncodingException;

public class Base64 {
  public static final int CRLF = 4;
  
  public static final int DEFAULT = 0;
  
  public static final int NO_CLOSE = 16;
  
  public static final int NO_PADDING = 1;
  
  public static final int NO_WRAP = 2;
  
  public static final int URL_SAFE = 8;
  
  public static byte[] decode(String paramString, int paramInt) {
    return decode(paramString.getBytes(), paramInt);
  }
  
  public static byte[] decode(byte[] paramArrayOfbyte, int paramInt) {
    return decode(paramArrayOfbyte, 0, paramArrayOfbyte.length, paramInt);
  }
  
  public static byte[] decode(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3) {
    Decoder decoder = new Decoder(paramInt3, new byte[paramInt2 * 3 / 4]);
    if (!decoder.process(paramArrayOfbyte, paramInt1, paramInt2, true))
      throw new IllegalArgumentException("bad base-64"); 
    if (decoder.op == decoder.output.length)
      return decoder.output; 
    paramArrayOfbyte = new byte[decoder.op];
    System.arraycopy(decoder.output, 0, paramArrayOfbyte, 0, decoder.op);
    return paramArrayOfbyte;
  }
  
  public static byte[] encode(byte[] paramArrayOfbyte, int paramInt) {
    return encode(paramArrayOfbyte, 0, paramArrayOfbyte.length, paramInt);
  }
  
  public static byte[] encode(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3) {
    Encoder encoder = new Encoder(paramInt3, null);
    int i = paramInt2 / 3 * 4;
    if (encoder.do_padding) {
      paramInt3 = i;
      if (paramInt2 % 3 > 0)
        paramInt3 = i + 4; 
    } 
    paramInt3 = i;
    switch (paramInt2 % 3) {
      case 0:
        i = paramInt3;
        if (encoder.do_newline) {
          i = paramInt3;
          if (paramInt2 > 0) {
            int j = (paramInt2 - 1) / 57;
            if (encoder.do_cr) {
              i = 2;
            } else {
              break;
            } 
            i = paramInt3 + i * (j + 1);
          } 
        } 
        encoder.output = new byte[i];
        encoder.process(paramArrayOfbyte, paramInt1, paramInt2, true);
        return encoder.output;
      default:
        paramInt3 = i;
      case 1:
        paramInt3 = i + 2;
      case 2:
        paramInt3 = i + 3;
    } 
    i = 1;
  }
  
  public static String encodeToString(byte[] paramArrayOfbyte, int paramInt) {
    try {
      return new String(encode(paramArrayOfbyte, paramInt), "US-ASCII");
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      throw new AssertionError(unsupportedEncodingException);
    } 
  }
  
  public static String encodeToString(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3) {
    try {
      return new String(encode(paramArrayOfbyte, paramInt1, paramInt2, paramInt3), "US-ASCII");
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      throw new AssertionError(unsupportedEncodingException);
    } 
  }
  
  static abstract class Coder {
    public int op;
    
    public byte[] output;
    
    public abstract int maxOutputSize(int param1Int);
    
    public abstract boolean process(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2, boolean param1Boolean);
  }
  
  static class Decoder extends Coder {
    private static final int[] DECODE = new int[] { 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 
        54, 55, 56, 57, 58, 59, 60, 61, -1, -1, 
        -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 
        5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 
        15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 
        25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 
        29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 
        39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 
        49, 50, 51, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1 };
    
    private static final int[] DECODE_WEBSAFE = new int[] { 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, 62, -1, -1, 52, 53, 
        54, 55, 56, 57, 58, 59, 60, 61, -1, -1, 
        -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 
        5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 
        15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 
        25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 
        29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 
        39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 
        49, 50, 51, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1 };
    
    private static final int EQUALS = -2;
    
    private static final int SKIP = -1;
    
    private final int[] alphabet;
    
    private int state;
    
    private int value;
    
    public Decoder(int param1Int, byte[] param1ArrayOfbyte) {
      int[] arrayOfInt;
      this.output = param1ArrayOfbyte;
      if ((param1Int & 0x8) == 0) {
        arrayOfInt = DECODE;
      } else {
        arrayOfInt = DECODE_WEBSAFE;
      } 
      this.alphabet = arrayOfInt;
      this.state = 0;
      this.value = 0;
    }
    
    public int maxOutputSize(int param1Int) {
      return param1Int * 3 / 4 + 10;
    }
    
    public boolean process(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2, boolean param1Boolean) {
      // Byte code:
      //   0: aload_0
      //   1: getfield state : I
      //   4: bipush #6
      //   6: if_icmpne -> 15
      //   9: iconst_0
      //   10: istore #4
      //   12: iload #4
      //   14: ireturn
      //   15: iload_2
      //   16: istore #5
      //   18: iload_3
      //   19: iload_2
      //   20: iadd
      //   21: istore #6
      //   23: aload_0
      //   24: getfield state : I
      //   27: istore #7
      //   29: aload_0
      //   30: getfield value : I
      //   33: istore_3
      //   34: iconst_0
      //   35: istore #8
      //   37: aload_0
      //   38: getfield output : [B
      //   41: astore #9
      //   43: aload_0
      //   44: getfield alphabet : [I
      //   47: astore #10
      //   49: iload #5
      //   51: iload #6
      //   53: if_icmpge -> 853
      //   56: iload #8
      //   58: istore #11
      //   60: iload #5
      //   62: istore #12
      //   64: iload_3
      //   65: istore #13
      //   67: iload #7
      //   69: ifne -> 247
      //   72: iload_3
      //   73: istore_2
      //   74: iload #5
      //   76: iconst_4
      //   77: iadd
      //   78: iload #6
      //   80: if_icmpgt -> 197
      //   83: aload #10
      //   85: aload_1
      //   86: iload #5
      //   88: baload
      //   89: sipush #255
      //   92: iand
      //   93: iaload
      //   94: bipush #18
      //   96: ishl
      //   97: aload #10
      //   99: aload_1
      //   100: iload #5
      //   102: iconst_1
      //   103: iadd
      //   104: baload
      //   105: sipush #255
      //   108: iand
      //   109: iaload
      //   110: bipush #12
      //   112: ishl
      //   113: ior
      //   114: aload #10
      //   116: aload_1
      //   117: iload #5
      //   119: iconst_2
      //   120: iadd
      //   121: baload
      //   122: sipush #255
      //   125: iand
      //   126: iaload
      //   127: bipush #6
      //   129: ishl
      //   130: ior
      //   131: aload #10
      //   133: aload_1
      //   134: iload #5
      //   136: iconst_3
      //   137: iadd
      //   138: baload
      //   139: sipush #255
      //   142: iand
      //   143: iaload
      //   144: ior
      //   145: istore_3
      //   146: iload_3
      //   147: istore_2
      //   148: iload_3
      //   149: iflt -> 197
      //   152: aload #9
      //   154: iload #8
      //   156: iconst_2
      //   157: iadd
      //   158: iload_3
      //   159: i2b
      //   160: i2b
      //   161: bastore
      //   162: aload #9
      //   164: iload #8
      //   166: iconst_1
      //   167: iadd
      //   168: iload_3
      //   169: bipush #8
      //   171: ishr
      //   172: i2b
      //   173: i2b
      //   174: bastore
      //   175: aload #9
      //   177: iload #8
      //   179: iload_3
      //   180: bipush #16
      //   182: ishr
      //   183: i2b
      //   184: i2b
      //   185: bastore
      //   186: iinc #8, 3
      //   189: iinc #5, 4
      //   192: iload_3
      //   193: istore_2
      //   194: goto -> 74
      //   197: iload #8
      //   199: istore #11
      //   201: iload #5
      //   203: istore #12
      //   205: iload_2
      //   206: istore #13
      //   208: iload #5
      //   210: iload #6
      //   212: if_icmplt -> 247
      //   215: iload_2
      //   216: istore_3
      //   217: iload #8
      //   219: istore_2
      //   220: iload #4
      //   222: ifne -> 721
      //   225: aload_0
      //   226: iload #7
      //   228: putfield state : I
      //   231: aload_0
      //   232: iload_3
      //   233: putfield value : I
      //   236: aload_0
      //   237: iload_2
      //   238: putfield op : I
      //   241: iconst_1
      //   242: istore #4
      //   244: goto -> 12
      //   247: aload #10
      //   249: aload_1
      //   250: iload #12
      //   252: baload
      //   253: sipush #255
      //   256: iand
      //   257: iaload
      //   258: istore #5
      //   260: iload #7
      //   262: tableswitch default -> 300, 0 -> 322, 1 -> 370, 2 -> 424, 3 -> 510, 4 -> 643, 5 -> 693
      //   300: iload #13
      //   302: istore_3
      //   303: iload #7
      //   305: istore_2
      //   306: iload #11
      //   308: istore #8
      //   310: iload #12
      //   312: iconst_1
      //   313: iadd
      //   314: istore #5
      //   316: iload_2
      //   317: istore #7
      //   319: goto -> 49
      //   322: iload #5
      //   324: iflt -> 342
      //   327: iload #5
      //   329: istore_3
      //   330: iload #7
      //   332: iconst_1
      //   333: iadd
      //   334: istore_2
      //   335: iload #11
      //   337: istore #8
      //   339: goto -> 310
      //   342: iload #11
      //   344: istore #8
      //   346: iload #7
      //   348: istore_2
      //   349: iload #13
      //   351: istore_3
      //   352: iload #5
      //   354: iconst_m1
      //   355: if_icmpeq -> 310
      //   358: aload_0
      //   359: bipush #6
      //   361: putfield state : I
      //   364: iconst_0
      //   365: istore #4
      //   367: goto -> 12
      //   370: iload #5
      //   372: iflt -> 396
      //   375: iload #13
      //   377: bipush #6
      //   379: ishl
      //   380: iload #5
      //   382: ior
      //   383: istore_3
      //   384: iload #7
      //   386: iconst_1
      //   387: iadd
      //   388: istore_2
      //   389: iload #11
      //   391: istore #8
      //   393: goto -> 310
      //   396: iload #11
      //   398: istore #8
      //   400: iload #7
      //   402: istore_2
      //   403: iload #13
      //   405: istore_3
      //   406: iload #5
      //   408: iconst_m1
      //   409: if_icmpeq -> 310
      //   412: aload_0
      //   413: bipush #6
      //   415: putfield state : I
      //   418: iconst_0
      //   419: istore #4
      //   421: goto -> 12
      //   424: iload #5
      //   426: iflt -> 450
      //   429: iload #13
      //   431: bipush #6
      //   433: ishl
      //   434: iload #5
      //   436: ior
      //   437: istore_3
      //   438: iload #7
      //   440: iconst_1
      //   441: iadd
      //   442: istore_2
      //   443: iload #11
      //   445: istore #8
      //   447: goto -> 310
      //   450: iload #5
      //   452: bipush #-2
      //   454: if_icmpne -> 482
      //   457: aload #9
      //   459: iload #11
      //   461: iload #13
      //   463: iconst_4
      //   464: ishr
      //   465: i2b
      //   466: i2b
      //   467: bastore
      //   468: iconst_4
      //   469: istore_2
      //   470: iload #11
      //   472: iconst_1
      //   473: iadd
      //   474: istore #8
      //   476: iload #13
      //   478: istore_3
      //   479: goto -> 310
      //   482: iload #11
      //   484: istore #8
      //   486: iload #7
      //   488: istore_2
      //   489: iload #13
      //   491: istore_3
      //   492: iload #5
      //   494: iconst_m1
      //   495: if_icmpeq -> 310
      //   498: aload_0
      //   499: bipush #6
      //   501: putfield state : I
      //   504: iconst_0
      //   505: istore #4
      //   507: goto -> 12
      //   510: iload #5
      //   512: iflt -> 569
      //   515: iload #13
      //   517: bipush #6
      //   519: ishl
      //   520: iload #5
      //   522: ior
      //   523: istore_3
      //   524: aload #9
      //   526: iload #11
      //   528: iconst_2
      //   529: iadd
      //   530: iload_3
      //   531: i2b
      //   532: i2b
      //   533: bastore
      //   534: aload #9
      //   536: iload #11
      //   538: iconst_1
      //   539: iadd
      //   540: iload_3
      //   541: bipush #8
      //   543: ishr
      //   544: i2b
      //   545: i2b
      //   546: bastore
      //   547: aload #9
      //   549: iload #11
      //   551: iload_3
      //   552: bipush #16
      //   554: ishr
      //   555: i2b
      //   556: i2b
      //   557: bastore
      //   558: iload #11
      //   560: iconst_3
      //   561: iadd
      //   562: istore #8
      //   564: iconst_0
      //   565: istore_2
      //   566: goto -> 310
      //   569: iload #5
      //   571: bipush #-2
      //   573: if_icmpne -> 615
      //   576: aload #9
      //   578: iload #11
      //   580: iconst_1
      //   581: iadd
      //   582: iload #13
      //   584: iconst_2
      //   585: ishr
      //   586: i2b
      //   587: i2b
      //   588: bastore
      //   589: aload #9
      //   591: iload #11
      //   593: iload #13
      //   595: bipush #10
      //   597: ishr
      //   598: i2b
      //   599: i2b
      //   600: bastore
      //   601: iload #11
      //   603: iconst_2
      //   604: iadd
      //   605: istore #8
      //   607: iconst_5
      //   608: istore_2
      //   609: iload #13
      //   611: istore_3
      //   612: goto -> 310
      //   615: iload #11
      //   617: istore #8
      //   619: iload #7
      //   621: istore_2
      //   622: iload #13
      //   624: istore_3
      //   625: iload #5
      //   627: iconst_m1
      //   628: if_icmpeq -> 310
      //   631: aload_0
      //   632: bipush #6
      //   634: putfield state : I
      //   637: iconst_0
      //   638: istore #4
      //   640: goto -> 12
      //   643: iload #5
      //   645: bipush #-2
      //   647: if_icmpne -> 665
      //   650: iload #7
      //   652: iconst_1
      //   653: iadd
      //   654: istore_2
      //   655: iload #11
      //   657: istore #8
      //   659: iload #13
      //   661: istore_3
      //   662: goto -> 310
      //   665: iload #11
      //   667: istore #8
      //   669: iload #7
      //   671: istore_2
      //   672: iload #13
      //   674: istore_3
      //   675: iload #5
      //   677: iconst_m1
      //   678: if_icmpeq -> 310
      //   681: aload_0
      //   682: bipush #6
      //   684: putfield state : I
      //   687: iconst_0
      //   688: istore #4
      //   690: goto -> 12
      //   693: iload #11
      //   695: istore #8
      //   697: iload #7
      //   699: istore_2
      //   700: iload #13
      //   702: istore_3
      //   703: iload #5
      //   705: iconst_m1
      //   706: if_icmpeq -> 310
      //   709: aload_0
      //   710: bipush #6
      //   712: putfield state : I
      //   715: iconst_0
      //   716: istore #4
      //   718: goto -> 12
      //   721: iload #7
      //   723: tableswitch default -> 756, 0 -> 773, 1 -> 776, 2 -> 788, 3 -> 808, 4 -> 841
      //   756: aload_0
      //   757: iload #7
      //   759: putfield state : I
      //   762: aload_0
      //   763: iload_2
      //   764: putfield op : I
      //   767: iconst_1
      //   768: istore #4
      //   770: goto -> 12
      //   773: goto -> 756
      //   776: aload_0
      //   777: bipush #6
      //   779: putfield state : I
      //   782: iconst_0
      //   783: istore #4
      //   785: goto -> 12
      //   788: iload_2
      //   789: iconst_1
      //   790: iadd
      //   791: istore #8
      //   793: aload #9
      //   795: iload_2
      //   796: iload_3
      //   797: iconst_4
      //   798: ishr
      //   799: i2b
      //   800: i2b
      //   801: bastore
      //   802: iload #8
      //   804: istore_2
      //   805: goto -> 756
      //   808: iload_2
      //   809: iconst_1
      //   810: iadd
      //   811: istore #8
      //   813: aload #9
      //   815: iload_2
      //   816: iload_3
      //   817: bipush #10
      //   819: ishr
      //   820: i2b
      //   821: i2b
      //   822: bastore
      //   823: aload #9
      //   825: iload #8
      //   827: iload_3
      //   828: iconst_2
      //   829: ishr
      //   830: i2b
      //   831: i2b
      //   832: bastore
      //   833: iload #8
      //   835: iconst_1
      //   836: iadd
      //   837: istore_2
      //   838: goto -> 756
      //   841: aload_0
      //   842: bipush #6
      //   844: putfield state : I
      //   847: iconst_0
      //   848: istore #4
      //   850: goto -> 12
      //   853: iload #8
      //   855: istore_2
      //   856: goto -> 220
    }
  }
  
  static class Encoder extends Coder {
    private static final byte[] ENCODE = new byte[] { 
        65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 
        75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 
        85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 
        101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 
        111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 
        121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 
        56, 57, 43, 47 };
    
    private static final byte[] ENCODE_WEBSAFE = new byte[] { 
        65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 
        75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 
        85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 
        101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 
        111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 
        121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 
        56, 57, 45, 95 };
    
    public static final int LINE_GROUPS = 19;
    
    private final byte[] alphabet;
    
    private int count;
    
    public final boolean do_cr;
    
    public final boolean do_newline;
    
    public final boolean do_padding;
    
    private final byte[] tail;
    
    int tailLen;
    
    public Encoder(int param1Int, byte[] param1ArrayOfbyte) {
      boolean bool2;
      this.output = param1ArrayOfbyte;
      if ((param1Int & 0x1) == 0) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      this.do_padding = bool2;
      if ((param1Int & 0x2) == 0) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      this.do_newline = bool2;
      if ((param1Int & 0x4) != 0) {
        bool2 = bool1;
      } else {
        bool2 = false;
      } 
      this.do_cr = bool2;
      if ((param1Int & 0x8) == 0) {
        param1ArrayOfbyte = ENCODE;
      } else {
        param1ArrayOfbyte = ENCODE_WEBSAFE;
      } 
      this.alphabet = param1ArrayOfbyte;
      this.tail = new byte[2];
      this.tailLen = 0;
      if (this.do_newline) {
        param1Int = 19;
      } else {
        param1Int = -1;
      } 
      this.count = param1Int;
    }
    
    public int maxOutputSize(int param1Int) {
      return param1Int * 8 / 5 + 10;
    }
    
    public boolean process(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2, boolean param1Boolean) {
      // Byte code:
      //   0: aload_0
      //   1: getfield alphabet : [B
      //   4: astore #5
      //   6: aload_0
      //   7: getfield output : [B
      //   10: astore #6
      //   12: iconst_0
      //   13: istore #7
      //   15: aload_0
      //   16: getfield count : I
      //   19: istore #8
      //   21: iload_2
      //   22: istore #9
      //   24: iload_3
      //   25: iload_2
      //   26: iadd
      //   27: istore #10
      //   29: iconst_m1
      //   30: istore_3
      //   31: iload #9
      //   33: istore_2
      //   34: iload_3
      //   35: istore #11
      //   37: aload_0
      //   38: getfield tailLen : I
      //   41: tableswitch default -> 68, 0 -> 74, 1 -> 420, 2 -> 496
      //   68: iload_3
      //   69: istore #11
      //   71: iload #9
      //   73: istore_2
      //   74: iload #8
      //   76: istore_3
      //   77: iload #7
      //   79: istore #9
      //   81: iload_2
      //   82: istore #7
      //   84: iload #11
      //   86: iconst_m1
      //   87: if_icmpeq -> 1169
      //   90: iconst_0
      //   91: iconst_1
      //   92: iadd
      //   93: istore_3
      //   94: aload #6
      //   96: iconst_0
      //   97: aload #5
      //   99: iload #11
      //   101: bipush #18
      //   103: ishr
      //   104: bipush #63
      //   106: iand
      //   107: baload
      //   108: i2b
      //   109: bastore
      //   110: iload_3
      //   111: iconst_1
      //   112: iadd
      //   113: istore #9
      //   115: aload #6
      //   117: iload_3
      //   118: aload #5
      //   120: iload #11
      //   122: bipush #12
      //   124: ishr
      //   125: bipush #63
      //   127: iand
      //   128: baload
      //   129: i2b
      //   130: bastore
      //   131: iload #9
      //   133: iconst_1
      //   134: iadd
      //   135: istore_3
      //   136: aload #6
      //   138: iload #9
      //   140: aload #5
      //   142: iload #11
      //   144: bipush #6
      //   146: ishr
      //   147: bipush #63
      //   149: iand
      //   150: baload
      //   151: i2b
      //   152: bastore
      //   153: iload_3
      //   154: iconst_1
      //   155: iadd
      //   156: istore #12
      //   158: aload #6
      //   160: iload_3
      //   161: aload #5
      //   163: iload #11
      //   165: bipush #63
      //   167: iand
      //   168: baload
      //   169: i2b
      //   170: bastore
      //   171: iload #8
      //   173: iconst_1
      //   174: isub
      //   175: istore #11
      //   177: iload #11
      //   179: istore_3
      //   180: iload #12
      //   182: istore #9
      //   184: iload_2
      //   185: istore #7
      //   187: iload #11
      //   189: ifne -> 1169
      //   192: iload #12
      //   194: istore_3
      //   195: aload_0
      //   196: getfield do_cr : Z
      //   199: ifeq -> 215
      //   202: aload #6
      //   204: iload #12
      //   206: bipush #13
      //   208: i2b
      //   209: bastore
      //   210: iload #12
      //   212: iconst_1
      //   213: iadd
      //   214: istore_3
      //   215: iload_3
      //   216: iconst_1
      //   217: iadd
      //   218: istore #9
      //   220: aload #6
      //   222: iload_3
      //   223: bipush #10
      //   225: i2b
      //   226: bastore
      //   227: bipush #19
      //   229: istore #11
      //   231: iload_2
      //   232: istore_3
      //   233: iload #9
      //   235: istore_2
      //   236: iload #11
      //   238: istore #9
      //   240: iload_3
      //   241: iconst_3
      //   242: iadd
      //   243: iload #10
      //   245: if_icmpgt -> 562
      //   248: aload_1
      //   249: iload_3
      //   250: baload
      //   251: sipush #255
      //   254: iand
      //   255: bipush #16
      //   257: ishl
      //   258: aload_1
      //   259: iload_3
      //   260: iconst_1
      //   261: iadd
      //   262: baload
      //   263: sipush #255
      //   266: iand
      //   267: bipush #8
      //   269: ishl
      //   270: ior
      //   271: aload_1
      //   272: iload_3
      //   273: iconst_2
      //   274: iadd
      //   275: baload
      //   276: sipush #255
      //   279: iand
      //   280: ior
      //   281: istore #11
      //   283: aload #6
      //   285: iload_2
      //   286: aload #5
      //   288: iload #11
      //   290: bipush #18
      //   292: ishr
      //   293: bipush #63
      //   295: iand
      //   296: baload
      //   297: i2b
      //   298: bastore
      //   299: aload #6
      //   301: iload_2
      //   302: iconst_1
      //   303: iadd
      //   304: aload #5
      //   306: iload #11
      //   308: bipush #12
      //   310: ishr
      //   311: bipush #63
      //   313: iand
      //   314: baload
      //   315: i2b
      //   316: bastore
      //   317: aload #6
      //   319: iload_2
      //   320: iconst_2
      //   321: iadd
      //   322: aload #5
      //   324: iload #11
      //   326: bipush #6
      //   328: ishr
      //   329: bipush #63
      //   331: iand
      //   332: baload
      //   333: i2b
      //   334: bastore
      //   335: aload #6
      //   337: iload_2
      //   338: iconst_3
      //   339: iadd
      //   340: aload #5
      //   342: iload #11
      //   344: bipush #63
      //   346: iand
      //   347: baload
      //   348: i2b
      //   349: bastore
      //   350: iload_3
      //   351: iconst_3
      //   352: iadd
      //   353: istore #11
      //   355: iinc #2, 4
      //   358: iload #9
      //   360: iconst_1
      //   361: isub
      //   362: istore #12
      //   364: iload #12
      //   366: istore_3
      //   367: iload_2
      //   368: istore #9
      //   370: iload #11
      //   372: istore #7
      //   374: iload #12
      //   376: ifne -> 1169
      //   379: iload_2
      //   380: istore_3
      //   381: aload_0
      //   382: getfield do_cr : Z
      //   385: ifeq -> 399
      //   388: aload #6
      //   390: iload_2
      //   391: bipush #13
      //   393: i2b
      //   394: bastore
      //   395: iload_2
      //   396: iconst_1
      //   397: iadd
      //   398: istore_3
      //   399: iload_3
      //   400: iconst_1
      //   401: iadd
      //   402: istore_2
      //   403: aload #6
      //   405: iload_3
      //   406: bipush #10
      //   408: i2b
      //   409: bastore
      //   410: bipush #19
      //   412: istore #9
      //   414: iload #11
      //   416: istore_3
      //   417: goto -> 240
      //   420: iload #9
      //   422: istore_2
      //   423: iload_3
      //   424: istore #11
      //   426: iload #9
      //   428: iconst_2
      //   429: iadd
      //   430: iload #10
      //   432: if_icmpgt -> 74
      //   435: aload_0
      //   436: getfield tail : [B
      //   439: iconst_0
      //   440: baload
      //   441: istore_3
      //   442: iload #9
      //   444: iconst_1
      //   445: iadd
      //   446: istore #11
      //   448: aload_1
      //   449: iload #9
      //   451: baload
      //   452: istore #9
      //   454: iload #11
      //   456: iconst_1
      //   457: iadd
      //   458: istore_2
      //   459: iload_3
      //   460: sipush #255
      //   463: iand
      //   464: bipush #16
      //   466: ishl
      //   467: iload #9
      //   469: sipush #255
      //   472: iand
      //   473: bipush #8
      //   475: ishl
      //   476: ior
      //   477: aload_1
      //   478: iload #11
      //   480: baload
      //   481: sipush #255
      //   484: iand
      //   485: ior
      //   486: istore #11
      //   488: aload_0
      //   489: iconst_0
      //   490: putfield tailLen : I
      //   493: goto -> 74
      //   496: iload #9
      //   498: istore_2
      //   499: iload_3
      //   500: istore #11
      //   502: iload #9
      //   504: iconst_1
      //   505: iadd
      //   506: iload #10
      //   508: if_icmpgt -> 74
      //   511: aload_0
      //   512: getfield tail : [B
      //   515: iconst_0
      //   516: baload
      //   517: sipush #255
      //   520: iand
      //   521: bipush #16
      //   523: ishl
      //   524: aload_0
      //   525: getfield tail : [B
      //   528: iconst_1
      //   529: baload
      //   530: sipush #255
      //   533: iand
      //   534: bipush #8
      //   536: ishl
      //   537: ior
      //   538: aload_1
      //   539: iload #9
      //   541: baload
      //   542: sipush #255
      //   545: iand
      //   546: ior
      //   547: istore #11
      //   549: aload_0
      //   550: iconst_0
      //   551: putfield tailLen : I
      //   554: iload #9
      //   556: iconst_1
      //   557: iadd
      //   558: istore_2
      //   559: goto -> 74
      //   562: iload #4
      //   564: ifeq -> 1055
      //   567: iload_3
      //   568: aload_0
      //   569: getfield tailLen : I
      //   572: isub
      //   573: iload #10
      //   575: iconst_1
      //   576: isub
      //   577: if_icmpne -> 763
      //   580: iconst_0
      //   581: istore #11
      //   583: aload_0
      //   584: getfield tailLen : I
      //   587: ifle -> 748
      //   590: aload_0
      //   591: getfield tail : [B
      //   594: iconst_0
      //   595: baload
      //   596: istore #11
      //   598: iconst_0
      //   599: iconst_1
      //   600: iadd
      //   601: istore_3
      //   602: iload #11
      //   604: sipush #255
      //   607: iand
      //   608: iconst_4
      //   609: ishl
      //   610: istore #11
      //   612: aload_0
      //   613: aload_0
      //   614: getfield tailLen : I
      //   617: iload_3
      //   618: isub
      //   619: putfield tailLen : I
      //   622: iload_2
      //   623: iconst_1
      //   624: iadd
      //   625: istore_3
      //   626: aload #6
      //   628: iload_2
      //   629: aload #5
      //   631: iload #11
      //   633: bipush #6
      //   635: ishr
      //   636: bipush #63
      //   638: iand
      //   639: baload
      //   640: i2b
      //   641: bastore
      //   642: iload_3
      //   643: iconst_1
      //   644: iadd
      //   645: istore_2
      //   646: aload #6
      //   648: iload_3
      //   649: aload #5
      //   651: iload #11
      //   653: bipush #63
      //   655: iand
      //   656: baload
      //   657: i2b
      //   658: bastore
      //   659: iload_2
      //   660: istore_3
      //   661: aload_0
      //   662: getfield do_padding : Z
      //   665: ifeq -> 693
      //   668: iload_2
      //   669: iconst_1
      //   670: iadd
      //   671: istore #11
      //   673: aload #6
      //   675: iload_2
      //   676: bipush #61
      //   678: i2b
      //   679: bastore
      //   680: iload #11
      //   682: iconst_1
      //   683: iadd
      //   684: istore_3
      //   685: aload #6
      //   687: iload #11
      //   689: bipush #61
      //   691: i2b
      //   692: bastore
      //   693: iload_3
      //   694: istore_2
      //   695: aload_0
      //   696: getfield do_newline : Z
      //   699: ifeq -> 735
      //   702: iload_3
      //   703: istore_2
      //   704: aload_0
      //   705: getfield do_cr : Z
      //   708: ifeq -> 722
      //   711: aload #6
      //   713: iload_3
      //   714: bipush #13
      //   716: i2b
      //   717: bastore
      //   718: iload_3
      //   719: iconst_1
      //   720: iadd
      //   721: istore_2
      //   722: iload_2
      //   723: iconst_1
      //   724: iadd
      //   725: istore_3
      //   726: aload #6
      //   728: iload_2
      //   729: bipush #10
      //   731: i2b
      //   732: bastore
      //   733: iload_3
      //   734: istore_2
      //   735: aload_0
      //   736: iload_2
      //   737: putfield op : I
      //   740: aload_0
      //   741: iload #9
      //   743: putfield count : I
      //   746: iconst_1
      //   747: ireturn
      //   748: aload_1
      //   749: iload_3
      //   750: baload
      //   751: istore #7
      //   753: iload #11
      //   755: istore_3
      //   756: iload #7
      //   758: istore #11
      //   760: goto -> 602
      //   763: iload_3
      //   764: aload_0
      //   765: getfield tailLen : I
      //   768: isub
      //   769: iload #10
      //   771: iconst_2
      //   772: isub
      //   773: if_icmpne -> 1004
      //   776: iconst_0
      //   777: istore #12
      //   779: aload_0
      //   780: getfield tailLen : I
      //   783: iconst_1
      //   784: if_icmple -> 979
      //   787: aload_0
      //   788: getfield tail : [B
      //   791: iconst_0
      //   792: baload
      //   793: istore #11
      //   795: iconst_0
      //   796: iconst_1
      //   797: iadd
      //   798: istore #12
      //   800: iload_3
      //   801: istore #7
      //   803: iload #12
      //   805: istore_3
      //   806: aload_0
      //   807: getfield tailLen : I
      //   810: ifle -> 995
      //   813: aload_0
      //   814: getfield tail : [B
      //   817: iload_3
      //   818: baload
      //   819: istore #7
      //   821: iinc #3, 1
      //   824: iload #11
      //   826: sipush #255
      //   829: iand
      //   830: bipush #10
      //   832: ishl
      //   833: iload #7
      //   835: sipush #255
      //   838: iand
      //   839: iconst_2
      //   840: ishl
      //   841: ior
      //   842: istore #11
      //   844: aload_0
      //   845: aload_0
      //   846: getfield tailLen : I
      //   849: iload_3
      //   850: isub
      //   851: putfield tailLen : I
      //   854: iload_2
      //   855: iconst_1
      //   856: iadd
      //   857: istore_3
      //   858: aload #6
      //   860: iload_2
      //   861: aload #5
      //   863: iload #11
      //   865: bipush #12
      //   867: ishr
      //   868: bipush #63
      //   870: iand
      //   871: baload
      //   872: i2b
      //   873: bastore
      //   874: iload_3
      //   875: iconst_1
      //   876: iadd
      //   877: istore #7
      //   879: aload #6
      //   881: iload_3
      //   882: aload #5
      //   884: iload #11
      //   886: bipush #6
      //   888: ishr
      //   889: bipush #63
      //   891: iand
      //   892: baload
      //   893: i2b
      //   894: bastore
      //   895: iload #7
      //   897: iconst_1
      //   898: iadd
      //   899: istore_2
      //   900: aload #6
      //   902: iload #7
      //   904: aload #5
      //   906: iload #11
      //   908: bipush #63
      //   910: iand
      //   911: baload
      //   912: i2b
      //   913: bastore
      //   914: iload_2
      //   915: istore_3
      //   916: aload_0
      //   917: getfield do_padding : Z
      //   920: ifeq -> 934
      //   923: aload #6
      //   925: iload_2
      //   926: bipush #61
      //   928: i2b
      //   929: bastore
      //   930: iload_2
      //   931: iconst_1
      //   932: iadd
      //   933: istore_3
      //   934: iload_3
      //   935: istore_2
      //   936: aload_0
      //   937: getfield do_newline : Z
      //   940: ifeq -> 735
      //   943: iload_3
      //   944: istore_2
      //   945: aload_0
      //   946: getfield do_cr : Z
      //   949: ifeq -> 963
      //   952: aload #6
      //   954: iload_3
      //   955: bipush #13
      //   957: i2b
      //   958: bastore
      //   959: iload_3
      //   960: iconst_1
      //   961: iadd
      //   962: istore_2
      //   963: iload_2
      //   964: iconst_1
      //   965: iadd
      //   966: istore_3
      //   967: aload #6
      //   969: iload_2
      //   970: bipush #10
      //   972: i2b
      //   973: bastore
      //   974: iload_3
      //   975: istore_2
      //   976: goto -> 735
      //   979: iload_3
      //   980: iconst_1
      //   981: iadd
      //   982: istore #7
      //   984: aload_1
      //   985: iload_3
      //   986: baload
      //   987: istore #11
      //   989: iload #12
      //   991: istore_3
      //   992: goto -> 806
      //   995: aload_1
      //   996: iload #7
      //   998: baload
      //   999: istore #7
      //   1001: goto -> 824
      //   1004: aload_0
      //   1005: getfield do_newline : Z
      //   1008: ifeq -> 1163
      //   1011: iload_2
      //   1012: ifle -> 1163
      //   1015: iload #9
      //   1017: bipush #19
      //   1019: if_icmpeq -> 1163
      //   1022: aload_0
      //   1023: getfield do_cr : Z
      //   1026: ifeq -> 1166
      //   1029: iload_2
      //   1030: iconst_1
      //   1031: iadd
      //   1032: istore_3
      //   1033: aload #6
      //   1035: iload_2
      //   1036: bipush #13
      //   1038: i2b
      //   1039: bastore
      //   1040: iload_3
      //   1041: istore_2
      //   1042: aload #6
      //   1044: iload_2
      //   1045: bipush #10
      //   1047: i2b
      //   1048: bastore
      //   1049: iinc #2, 1
      //   1052: goto -> 735
      //   1055: iload_3
      //   1056: iload #10
      //   1058: iconst_1
      //   1059: isub
      //   1060: if_icmpne -> 1095
      //   1063: aload_0
      //   1064: getfield tail : [B
      //   1067: astore #5
      //   1069: aload_0
      //   1070: getfield tailLen : I
      //   1073: istore #11
      //   1075: aload_0
      //   1076: iload #11
      //   1078: iconst_1
      //   1079: iadd
      //   1080: putfield tailLen : I
      //   1083: aload #5
      //   1085: iload #11
      //   1087: aload_1
      //   1088: iload_3
      //   1089: baload
      //   1090: i2b
      //   1091: bastore
      //   1092: goto -> 735
      //   1095: iload_3
      //   1096: iload #10
      //   1098: iconst_2
      //   1099: isub
      //   1100: if_icmpne -> 1163
      //   1103: aload_0
      //   1104: getfield tail : [B
      //   1107: astore #5
      //   1109: aload_0
      //   1110: getfield tailLen : I
      //   1113: istore #11
      //   1115: aload_0
      //   1116: iload #11
      //   1118: iconst_1
      //   1119: iadd
      //   1120: putfield tailLen : I
      //   1123: aload #5
      //   1125: iload #11
      //   1127: aload_1
      //   1128: iload_3
      //   1129: baload
      //   1130: i2b
      //   1131: bastore
      //   1132: aload_0
      //   1133: getfield tail : [B
      //   1136: astore #5
      //   1138: aload_0
      //   1139: getfield tailLen : I
      //   1142: istore #11
      //   1144: aload_0
      //   1145: iload #11
      //   1147: iconst_1
      //   1148: iadd
      //   1149: putfield tailLen : I
      //   1152: aload #5
      //   1154: iload #11
      //   1156: aload_1
      //   1157: iload_3
      //   1158: iconst_1
      //   1159: iadd
      //   1160: baload
      //   1161: i2b
      //   1162: bastore
      //   1163: goto -> 735
      //   1166: goto -> 1042
      //   1169: iload #9
      //   1171: istore_2
      //   1172: iload_3
      //   1173: istore #9
      //   1175: iload #7
      //   1177: istore_3
      //   1178: goto -> 240
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/loopj/android/http/Base64.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */