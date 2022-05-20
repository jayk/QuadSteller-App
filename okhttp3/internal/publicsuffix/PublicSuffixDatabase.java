package okhttp3.internal.publicsuffix;

import java.net.IDN;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import okhttp3.internal.Util;

public final class PublicSuffixDatabase {
  private static final String[] EMPTY_RULE;
  
  private static final byte EXCEPTION_MARKER = 33;
  
  private static final String[] PREVAILING_RULE;
  
  public static final String PUBLIC_SUFFIX_RESOURCE = "publicsuffixes.gz";
  
  private static final byte[] WILDCARD_LABEL = new byte[] { 42 };
  
  private static final PublicSuffixDatabase instance;
  
  private final AtomicBoolean listRead = new AtomicBoolean(false);
  
  private byte[] publicSuffixExceptionListBytes;
  
  private byte[] publicSuffixListBytes;
  
  private final CountDownLatch readCompleteLatch = new CountDownLatch(1);
  
  static {
    EMPTY_RULE = new String[0];
    PREVAILING_RULE = new String[] { "*" };
    instance = new PublicSuffixDatabase();
  }
  
  private static String binarySearchBytes(byte[] paramArrayOfbyte, byte[][] paramArrayOfbyte1, int paramInt) {
    String str2;
    int i = 0;
    int j = paramArrayOfbyte.length;
    String str1 = null;
    label51: while (true) {
      str2 = str1;
      if (i < j) {
        int k;
        for (k = (i + j) / 2; k > -1 && paramArrayOfbyte[k] != 10; k--);
        int m = k + 1;
        byte b;
        for (b = 1; paramArrayOfbyte[m + b] != 10; b++);
        int n = m + b - m;
        int i1 = paramInt;
        k = 0;
        int i2 = 0;
        int i3 = 0;
        while (true) {
          int i4;
          if (i3) {
            i4 = 46;
            i3 = 0;
          } else {
            i4 = paramArrayOfbyte1[i1][k] & 0xFF;
          } 
          int i5 = i4 - (paramArrayOfbyte[m + i2] & 0xFF);
          if (i5 == 0) {
            i4 = i2 + 1;
            int i6 = k + 1;
            k = i6;
            i2 = i4;
            if (i4 != n) {
              k = i6;
              i2 = i4;
              if ((paramArrayOfbyte1[i1]).length == i6) {
                k = i6;
                i2 = i4;
                if (i1 != paramArrayOfbyte1.length - 1) {
                  i1++;
                  k = -1;
                  i3 = 1;
                  i2 = i4;
                  continue;
                } 
              } else {
                continue;
              } 
            } 
          } 
          if (i5 < 0) {
            j = m - 1;
            continue label51;
          } 
          if (i5 > 0) {
            i = m + b + 1;
            continue;
          } 
          i3 = n - i2;
          k = (paramArrayOfbyte1[i1]).length - k;
          for (i2 = i1 + 1; i2 < paramArrayOfbyte1.length; i2++)
            k += (paramArrayOfbyte1[i2]).length; 
          if (k < i3) {
            j = m - 1;
            continue;
          } 
          if (k > i3) {
            i = m + b + 1;
            continue;
          } 
          str2 = new String(paramArrayOfbyte, m, n, Util.UTF_8);
          break;
        } 
      } 
      break;
    } 
    return str2;
  }
  
  private String[] findMatchingRule(String[] paramArrayOfString) {
    // Byte code:
    //   0: aload_0
    //   1: getfield listRead : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   4: invokevirtual get : ()Z
    //   7: ifne -> 52
    //   10: aload_0
    //   11: getfield listRead : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   14: iconst_0
    //   15: iconst_1
    //   16: invokevirtual compareAndSet : (ZZ)Z
    //   19: ifeq -> 52
    //   22: aload_0
    //   23: invokespecial readTheList : ()V
    //   26: aload_0
    //   27: monitorenter
    //   28: aload_0
    //   29: getfield publicSuffixListBytes : [B
    //   32: ifnonnull -> 66
    //   35: new java/lang/IllegalStateException
    //   38: astore_1
    //   39: aload_1
    //   40: ldc 'Unable to load publicsuffixes.gz resource from the classpath.'
    //   42: invokespecial <init> : (Ljava/lang/String;)V
    //   45: aload_1
    //   46: athrow
    //   47: astore_1
    //   48: aload_0
    //   49: monitorexit
    //   50: aload_1
    //   51: athrow
    //   52: aload_0
    //   53: getfield readCompleteLatch : Ljava/util/concurrent/CountDownLatch;
    //   56: invokevirtual await : ()V
    //   59: goto -> 26
    //   62: astore_2
    //   63: goto -> 26
    //   66: aload_0
    //   67: monitorexit
    //   68: aload_1
    //   69: arraylength
    //   70: anewarray [B
    //   73: astore_3
    //   74: iconst_0
    //   75: istore #4
    //   77: iload #4
    //   79: aload_1
    //   80: arraylength
    //   81: if_icmpge -> 104
    //   84: aload_3
    //   85: iload #4
    //   87: aload_1
    //   88: iload #4
    //   90: aaload
    //   91: getstatic okhttp3/internal/Util.UTF_8 : Ljava/nio/charset/Charset;
    //   94: invokevirtual getBytes : (Ljava/nio/charset/Charset;)[B
    //   97: aastore
    //   98: iinc #4, 1
    //   101: goto -> 77
    //   104: aconst_null
    //   105: astore_1
    //   106: iconst_0
    //   107: istore #4
    //   109: aload_1
    //   110: astore_2
    //   111: iload #4
    //   113: aload_3
    //   114: arraylength
    //   115: if_icmpge -> 133
    //   118: aload_0
    //   119: getfield publicSuffixListBytes : [B
    //   122: aload_3
    //   123: iload #4
    //   125: invokestatic binarySearchBytes : ([B[[BI)Ljava/lang/String;
    //   128: astore_2
    //   129: aload_2
    //   130: ifnull -> 271
    //   133: aconst_null
    //   134: astore #5
    //   136: aload #5
    //   138: astore_1
    //   139: aload_3
    //   140: arraylength
    //   141: iconst_1
    //   142: if_icmple -> 194
    //   145: aload_3
    //   146: invokevirtual clone : ()Ljava/lang/Object;
    //   149: checkcast [[B
    //   152: astore #6
    //   154: iconst_0
    //   155: istore #4
    //   157: aload #5
    //   159: astore_1
    //   160: iload #4
    //   162: aload #6
    //   164: arraylength
    //   165: iconst_1
    //   166: isub
    //   167: if_icmpge -> 194
    //   170: aload #6
    //   172: iload #4
    //   174: getstatic okhttp3/internal/publicsuffix/PublicSuffixDatabase.WILDCARD_LABEL : [B
    //   177: aastore
    //   178: aload_0
    //   179: getfield publicSuffixListBytes : [B
    //   182: aload #6
    //   184: iload #4
    //   186: invokestatic binarySearchBytes : ([B[[BI)Ljava/lang/String;
    //   189: astore_1
    //   190: aload_1
    //   191: ifnull -> 277
    //   194: aconst_null
    //   195: astore #6
    //   197: aload #6
    //   199: astore #5
    //   201: aload_1
    //   202: ifnull -> 238
    //   205: iconst_0
    //   206: istore #4
    //   208: aload #6
    //   210: astore #5
    //   212: iload #4
    //   214: aload_3
    //   215: arraylength
    //   216: iconst_1
    //   217: isub
    //   218: if_icmpge -> 238
    //   221: aload_0
    //   222: getfield publicSuffixExceptionListBytes : [B
    //   225: aload_3
    //   226: iload #4
    //   228: invokestatic binarySearchBytes : ([B[[BI)Ljava/lang/String;
    //   231: astore #5
    //   233: aload #5
    //   235: ifnull -> 283
    //   238: aload #5
    //   240: ifnull -> 289
    //   243: new java/lang/StringBuilder
    //   246: dup
    //   247: invokespecial <init> : ()V
    //   250: ldc '!'
    //   252: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   255: aload #5
    //   257: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   260: invokevirtual toString : ()Ljava/lang/String;
    //   263: ldc '\.'
    //   265: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
    //   268: astore_1
    //   269: aload_1
    //   270: areturn
    //   271: iinc #4, 1
    //   274: goto -> 109
    //   277: iinc #4, 1
    //   280: goto -> 157
    //   283: iinc #4, 1
    //   286: goto -> 208
    //   289: aload_2
    //   290: ifnonnull -> 304
    //   293: aload_1
    //   294: ifnonnull -> 304
    //   297: getstatic okhttp3/internal/publicsuffix/PublicSuffixDatabase.PREVAILING_RULE : [Ljava/lang/String;
    //   300: astore_1
    //   301: goto -> 269
    //   304: aload_2
    //   305: ifnull -> 343
    //   308: aload_2
    //   309: ldc '\.'
    //   311: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
    //   314: astore_2
    //   315: aload_1
    //   316: ifnull -> 350
    //   319: aload_1
    //   320: ldc '\.'
    //   322: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
    //   325: astore #5
    //   327: aload_2
    //   328: astore_1
    //   329: aload_2
    //   330: arraylength
    //   331: aload #5
    //   333: arraylength
    //   334: if_icmpgt -> 269
    //   337: aload #5
    //   339: astore_1
    //   340: goto -> 269
    //   343: getstatic okhttp3/internal/publicsuffix/PublicSuffixDatabase.EMPTY_RULE : [Ljava/lang/String;
    //   346: astore_2
    //   347: goto -> 315
    //   350: getstatic okhttp3/internal/publicsuffix/PublicSuffixDatabase.EMPTY_RULE : [Ljava/lang/String;
    //   353: astore #5
    //   355: goto -> 327
    // Exception table:
    //   from	to	target	type
    //   28	47	47	finally
    //   48	50	47	finally
    //   52	59	62	java/lang/InterruptedException
    //   66	68	47	finally
  }
  
  public static PublicSuffixDatabase get() {
    return instance;
  }
  
  private void readTheList() {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aconst_null
    //   3: astore_2
    //   4: ldc okhttp3/internal/publicsuffix/PublicSuffixDatabase
    //   6: invokevirtual getClassLoader : ()Ljava/lang/ClassLoader;
    //   9: ldc 'publicsuffixes.gz'
    //   11: invokevirtual getResourceAsStream : (Ljava/lang/String;)Ljava/io/InputStream;
    //   14: astore_3
    //   15: aload_3
    //   16: ifnull -> 70
    //   19: new okio/GzipSource
    //   22: dup
    //   23: aload_3
    //   24: invokestatic source : (Ljava/io/InputStream;)Lokio/Source;
    //   27: invokespecial <init> : (Lokio/Source;)V
    //   30: invokestatic buffer : (Lokio/Source;)Lokio/BufferedSource;
    //   33: astore_3
    //   34: aload_3
    //   35: invokeinterface readInt : ()I
    //   40: newarray byte
    //   42: astore_1
    //   43: aload_3
    //   44: aload_1
    //   45: invokeinterface readFully : ([B)V
    //   50: aload_3
    //   51: invokeinterface readInt : ()I
    //   56: newarray byte
    //   58: astore_2
    //   59: aload_3
    //   60: aload_2
    //   61: invokeinterface readFully : ([B)V
    //   66: aload_3
    //   67: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   70: aload_0
    //   71: monitorenter
    //   72: aload_0
    //   73: aload_1
    //   74: putfield publicSuffixListBytes : [B
    //   77: aload_0
    //   78: aload_2
    //   79: putfield publicSuffixExceptionListBytes : [B
    //   82: aload_0
    //   83: monitorexit
    //   84: aload_0
    //   85: getfield readCompleteLatch : Ljava/util/concurrent/CountDownLatch;
    //   88: invokevirtual countDown : ()V
    //   91: return
    //   92: astore_2
    //   93: invokestatic get : ()Lokhttp3/internal/platform/Platform;
    //   96: iconst_5
    //   97: ldc 'Failed to read public suffix list'
    //   99: aload_2
    //   100: invokevirtual log : (ILjava/lang/String;Ljava/lang/Throwable;)V
    //   103: aconst_null
    //   104: astore_1
    //   105: aconst_null
    //   106: astore_2
    //   107: aload_3
    //   108: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   111: goto -> 70
    //   114: astore_2
    //   115: aload_3
    //   116: invokestatic closeQuietly : (Ljava/io/Closeable;)V
    //   119: aload_2
    //   120: athrow
    //   121: astore_2
    //   122: aload_0
    //   123: monitorexit
    //   124: aload_2
    //   125: athrow
    // Exception table:
    //   from	to	target	type
    //   34	66	92	java/io/IOException
    //   34	66	114	finally
    //   72	84	121	finally
    //   93	103	114	finally
    //   122	124	121	finally
  }
  
  public String getEffectiveTldPlusOne(String paramString) {
    int i;
    if (paramString == null)
      throw new NullPointerException("domain == null"); 
    String[] arrayOfString2 = IDN.toUnicode(paramString).split("\\.");
    String[] arrayOfString3 = findMatchingRule(arrayOfString2);
    if (arrayOfString2.length == arrayOfString3.length && arrayOfString3[0].charAt(0) != '!')
      return null; 
    if (arrayOfString3[0].charAt(0) == '!') {
      i = arrayOfString2.length - arrayOfString3.length;
    } else {
      i = arrayOfString2.length - arrayOfString3.length + 1;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    String[] arrayOfString1 = paramString.split("\\.");
    while (i < arrayOfString1.length) {
      stringBuilder.append(arrayOfString1[i]).append('.');
      i++;
    } 
    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    return stringBuilder.toString();
  }
  
  void setListBytes(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    this.publicSuffixListBytes = paramArrayOfbyte1;
    this.publicSuffixExceptionListBytes = paramArrayOfbyte2;
    this.listRead.set(true);
    this.readCompleteLatch.countDown();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/publicsuffix/PublicSuffixDatabase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */