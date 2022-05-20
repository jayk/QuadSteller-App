package okhttp3.internal;

import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.IDN;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Source;

public final class Util {
  public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
  
  public static final RequestBody EMPTY_REQUEST;
  
  public static final ResponseBody EMPTY_RESPONSE;
  
  public static final String[] EMPTY_STRING_ARRAY = new String[0];
  
  public static final Comparator<String> NATURAL_ORDER;
  
  public static final TimeZone UTC;
  
  private static final Charset UTF_16_BE;
  
  private static final ByteString UTF_16_BE_BOM;
  
  private static final Charset UTF_16_LE;
  
  private static final ByteString UTF_16_LE_BOM;
  
  private static final Charset UTF_32_BE;
  
  private static final ByteString UTF_32_BE_BOM;
  
  private static final Charset UTF_32_LE;
  
  private static final ByteString UTF_32_LE_BOM;
  
  public static final Charset UTF_8;
  
  private static final ByteString UTF_8_BOM;
  
  private static final Pattern VERIFY_AS_IP_ADDRESS;
  
  static {
    EMPTY_RESPONSE = ResponseBody.create(null, EMPTY_BYTE_ARRAY);
    EMPTY_REQUEST = RequestBody.create(null, EMPTY_BYTE_ARRAY);
    UTF_8_BOM = ByteString.decodeHex("efbbbf");
    UTF_16_BE_BOM = ByteString.decodeHex("feff");
    UTF_16_LE_BOM = ByteString.decodeHex("fffe");
    UTF_32_BE_BOM = ByteString.decodeHex("0000ffff");
    UTF_32_LE_BOM = ByteString.decodeHex("ffff0000");
    UTF_8 = Charset.forName("UTF-8");
    UTF_16_BE = Charset.forName("UTF-16BE");
    UTF_16_LE = Charset.forName("UTF-16LE");
    UTF_32_BE = Charset.forName("UTF-32BE");
    UTF_32_LE = Charset.forName("UTF-32LE");
    UTC = TimeZone.getTimeZone("GMT");
    NATURAL_ORDER = new Comparator<String>() {
        public int compare(String param1String1, String param1String2) {
          return param1String1.compareTo(param1String2);
        }
      };
    VERIFY_AS_IP_ADDRESS = Pattern.compile("([0-9a-fA-F]*:[0-9a-fA-F:.]*)|([\\d.]+)");
  }
  
  public static Charset bomAwareCharset(BufferedSource paramBufferedSource, Charset paramCharset) throws IOException {
    if (paramBufferedSource.rangeEquals(0L, UTF_8_BOM)) {
      paramBufferedSource.skip(UTF_8_BOM.size());
      return UTF_8;
    } 
    if (paramBufferedSource.rangeEquals(0L, UTF_16_BE_BOM)) {
      paramBufferedSource.skip(UTF_16_BE_BOM.size());
      return UTF_16_BE;
    } 
    if (paramBufferedSource.rangeEquals(0L, UTF_16_LE_BOM)) {
      paramBufferedSource.skip(UTF_16_LE_BOM.size());
      return UTF_16_LE;
    } 
    if (paramBufferedSource.rangeEquals(0L, UTF_32_BE_BOM)) {
      paramBufferedSource.skip(UTF_32_BE_BOM.size());
      return UTF_32_BE;
    } 
    if (paramBufferedSource.rangeEquals(0L, UTF_32_LE_BOM)) {
      paramBufferedSource.skip(UTF_32_LE_BOM.size());
      paramCharset = UTF_32_LE;
    } 
    return paramCharset;
  }
  
  public static void checkOffsetAndCount(long paramLong1, long paramLong2, long paramLong3) {
    if ((paramLong2 | paramLong3) < 0L || paramLong2 > paramLong1 || paramLong1 - paramLong2 < paramLong3)
      throw new ArrayIndexOutOfBoundsException(); 
  }
  
  public static void closeQuietly(Closeable paramCloseable) {
    if (paramCloseable != null)
      try {
        paramCloseable.close();
      } catch (RuntimeException runtimeException) {
        throw runtimeException;
      } catch (Exception exception) {} 
  }
  
  public static void closeQuietly(ServerSocket paramServerSocket) {
    if (paramServerSocket != null)
      try {
        paramServerSocket.close();
      } catch (RuntimeException runtimeException) {
        throw runtimeException;
      } catch (Exception exception) {} 
  }
  
  public static void closeQuietly(Socket paramSocket) {
    if (paramSocket != null)
      try {
        paramSocket.close();
      } catch (AssertionError assertionError) {
        if (!isAndroidGetsocknameError(assertionError))
          throw assertionError; 
      } catch (RuntimeException runtimeException) {
        throw runtimeException;
      } catch (Exception exception) {} 
  }
  
  public static String[] concat(String[] paramArrayOfString, String paramString) {
    String[] arrayOfString = new String[paramArrayOfString.length + 1];
    System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, paramArrayOfString.length);
    arrayOfString[arrayOfString.length - 1] = paramString;
    return arrayOfString;
  }
  
  private static boolean containsInvalidHostnameAsciiCodes(String paramString) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_1
    //   2: iconst_0
    //   3: istore_2
    //   4: iload_2
    //   5: aload_0
    //   6: invokevirtual length : ()I
    //   9: if_icmpge -> 58
    //   12: aload_0
    //   13: iload_2
    //   14: invokevirtual charAt : (I)C
    //   17: istore_3
    //   18: iload_1
    //   19: istore #4
    //   21: iload_3
    //   22: bipush #31
    //   24: if_icmple -> 36
    //   27: iload_3
    //   28: bipush #127
    //   30: if_icmplt -> 39
    //   33: iload_1
    //   34: istore #4
    //   36: iload #4
    //   38: ireturn
    //   39: iload_1
    //   40: istore #4
    //   42: ldc ' #%/:?@[\]'
    //   44: iload_3
    //   45: invokevirtual indexOf : (I)I
    //   48: iconst_m1
    //   49: if_icmpne -> 36
    //   52: iinc #2, 1
    //   55: goto -> 4
    //   58: iconst_0
    //   59: istore #4
    //   61: goto -> 36
  }
  
  public static int delimiterOffset(String paramString, int paramInt1, int paramInt2, char paramChar) {
    // Byte code:
    //   0: iload_1
    //   1: iload_2
    //   2: if_icmpge -> 22
    //   5: aload_0
    //   6: iload_1
    //   7: invokevirtual charAt : (I)C
    //   10: iload_3
    //   11: if_icmpne -> 16
    //   14: iload_1
    //   15: ireturn
    //   16: iinc #1, 1
    //   19: goto -> 0
    //   22: iload_2
    //   23: istore_1
    //   24: goto -> 14
  }
  
  public static int delimiterOffset(String paramString1, int paramInt1, int paramInt2, String paramString2) {
    // Byte code:
    //   0: iload_1
    //   1: iload_2
    //   2: if_icmpge -> 26
    //   5: aload_3
    //   6: aload_0
    //   7: iload_1
    //   8: invokevirtual charAt : (I)C
    //   11: invokevirtual indexOf : (I)I
    //   14: iconst_m1
    //   15: if_icmpeq -> 20
    //   18: iload_1
    //   19: ireturn
    //   20: iinc #1, 1
    //   23: goto -> 0
    //   26: iload_2
    //   27: istore_1
    //   28: goto -> 18
  }
  
  public static boolean discard(Source paramSource, int paramInt, TimeUnit paramTimeUnit) {
    boolean bool;
    try {
      bool = skipAll(paramSource, paramInt, paramTimeUnit);
    } catch (IOException iOException) {
      bool = false;
    } 
    return bool;
  }
  
  public static String domainToAscii(String paramString) {
    try {
      paramString = IDN.toASCII(paramString).toLowerCase(Locale.US);
      if (paramString.isEmpty())
        return null; 
      boolean bool = containsInvalidHostnameAsciiCodes(paramString);
      if (bool)
        paramString = null; 
    } catch (IllegalArgumentException illegalArgumentException) {
      illegalArgumentException = null;
    } 
    return (String)illegalArgumentException;
  }
  
  public static boolean equal(Object paramObject1, Object paramObject2) {
    return (paramObject1 == paramObject2 || (paramObject1 != null && paramObject1.equals(paramObject2)));
  }
  
  public static String format(String paramString, Object... paramVarArgs) {
    return String.format(Locale.US, paramString, paramVarArgs);
  }
  
  public static String hostHeader(HttpUrl paramHttpUrl, boolean paramBoolean) {
    String str;
    if (paramHttpUrl.host().contains(":")) {
      str = "[" + paramHttpUrl.host() + "]";
    } else {
      str = paramHttpUrl.host();
    } 
    if (!paramBoolean) {
      String str1 = str;
      return (paramHttpUrl.port() != HttpUrl.defaultPort(paramHttpUrl.scheme())) ? (str + ":" + paramHttpUrl.port()) : str1;
    } 
    return str + ":" + paramHttpUrl.port();
  }
  
  public static <T> List<T> immutableList(List<T> paramList) {
    return Collections.unmodifiableList(new ArrayList<T>(paramList));
  }
  
  public static <T> List<T> immutableList(T... paramVarArgs) {
    return Collections.unmodifiableList(Arrays.asList((T[])paramVarArgs.clone()));
  }
  
  public static int indexOf(Comparator<String> paramComparator, String[] paramArrayOfString, String paramString) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: aload_1
    //   3: arraylength
    //   4: istore #4
    //   6: iload_3
    //   7: iload #4
    //   9: if_icmpge -> 33
    //   12: aload_0
    //   13: aload_1
    //   14: iload_3
    //   15: aaload
    //   16: aload_2
    //   17: invokeinterface compare : (Ljava/lang/Object;Ljava/lang/Object;)I
    //   22: ifne -> 27
    //   25: iload_3
    //   26: ireturn
    //   27: iinc #3, 1
    //   30: goto -> 6
    //   33: iconst_m1
    //   34: istore_3
    //   35: goto -> 25
  }
  
  public static int indexOfControlOrNonAscii(String paramString) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: invokevirtual length : ()I
    //   6: istore_2
    //   7: iload_1
    //   8: iload_2
    //   9: if_icmpge -> 45
    //   12: aload_0
    //   13: iload_1
    //   14: invokevirtual charAt : (I)C
    //   17: istore_3
    //   18: iload_1
    //   19: istore #4
    //   21: iload_3
    //   22: bipush #31
    //   24: if_icmple -> 36
    //   27: iload_3
    //   28: bipush #127
    //   30: if_icmplt -> 39
    //   33: iload_1
    //   34: istore #4
    //   36: iload #4
    //   38: ireturn
    //   39: iinc #1, 1
    //   42: goto -> 7
    //   45: iconst_m1
    //   46: istore #4
    //   48: goto -> 36
  }
  
  public static String[] intersect(Comparator<? super String> paramComparator, String[] paramArrayOfString1, String[] paramArrayOfString2) {
    ArrayList<String> arrayList = new ArrayList();
    int i = paramArrayOfString1.length;
    byte b = 0;
    label14: while (b < i) {
      String str = paramArrayOfString1[b];
      int j = paramArrayOfString2.length;
      byte b1 = 0;
      while (true) {
        if (b1 < j)
          if (paramComparator.compare(str, paramArrayOfString2[b1]) == 0) {
            arrayList.add(str);
          } else {
            b1++;
            continue;
          }  
        b++;
        continue label14;
      } 
    } 
    return arrayList.<String>toArray(new String[arrayList.size()]);
  }
  
  public static boolean isAndroidGetsocknameError(AssertionError paramAssertionError) {
    return (paramAssertionError.getCause() != null && paramAssertionError.getMessage() != null && paramAssertionError.getMessage().contains("getsockname failed"));
  }
  
  public static boolean nonEmptyIntersection(Comparator<String> paramComparator, String[] paramArrayOfString1, String[] paramArrayOfString2) {
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (paramArrayOfString1 != null) {
      bool2 = bool1;
      if (paramArrayOfString2 != null) {
        bool2 = bool1;
        if (paramArrayOfString1.length != 0) {
          if (paramArrayOfString2.length == 0)
            return bool1; 
        } else {
          return bool2;
        } 
      } else {
        return bool2;
      } 
    } else {
      return bool2;
    } 
    int i = paramArrayOfString1.length;
    byte b = 0;
    while (true) {
      bool2 = bool1;
      if (b < i) {
        String str = paramArrayOfString1[b];
        int j = paramArrayOfString2.length;
        for (byte b1 = 0; b1 < j; b1++) {
          if (paramComparator.compare(str, paramArrayOfString2[b1]) == 0)
            return true; 
        } 
        b++;
        continue;
      } 
      // Byte code: goto -> 32
    } 
  }
  
  public static boolean skipAll(Source paramSource, int paramInt, TimeUnit paramTimeUnit) throws IOException {
    long l2;
    long l1 = System.nanoTime();
    if (paramSource.timeout().hasDeadline()) {
      l2 = paramSource.timeout().deadlineNanoTime() - l1;
    } else {
      l2 = Long.MAX_VALUE;
    } 
    paramSource.timeout().deadlineNanoTime(Math.min(l2, paramTimeUnit.toNanos(paramInt)) + l1);
    try {
      Buffer buffer = new Buffer();
      this();
      while (paramSource.read(buffer, 8192L) != -1L)
        buffer.clear(); 
      return true;
    } catch (InterruptedIOException interruptedIOException) {
      return false;
    } finally {
      paramTimeUnit = null;
    } 
    throw paramTimeUnit;
  }
  
  public static int skipLeadingAsciiWhitespace(String paramString, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iload_1
    //   1: iload_2
    //   2: if_icmpge -> 68
    //   5: aload_0
    //   6: iload_1
    //   7: invokevirtual charAt : (I)C
    //   10: lookupswitch default -> 60, 9 -> 62, 10 -> 62, 12 -> 62, 13 -> 62, 32 -> 62
    //   60: iload_1
    //   61: ireturn
    //   62: iinc #1, 1
    //   65: goto -> 0
    //   68: iload_2
    //   69: istore_1
    //   70: goto -> 60
  }
  
  public static int skipTrailingAsciiWhitespace(String paramString, int paramInt1, int paramInt2) {
    paramInt2--;
    while (true) {
      int i = paramInt1;
      if (paramInt2 >= paramInt1) {
        switch (paramString.charAt(paramInt2)) {
          default:
            return paramInt2 + 1;
          case '\t':
          case '\n':
          case '\f':
          case '\r':
          case ' ':
            break;
        } 
      } else {
        return i;
      } 
      paramInt2--;
    } 
  }
  
  public static ThreadFactory threadFactory(final String name, final boolean daemon) {
    return new ThreadFactory() {
        public Thread newThread(Runnable param1Runnable) {
          param1Runnable = new Thread(param1Runnable, name);
          param1Runnable.setDaemon(daemon);
          return (Thread)param1Runnable;
        }
      };
  }
  
  public static String toHumanReadableAscii(String paramString) {
    String str;
    int i = 0;
    int j = paramString.length();
    while (true) {
      str = paramString;
      if (i < j) {
        int k = paramString.codePointAt(i);
        if (k > 31 && k < 127) {
          i += Character.charCount(k);
          continue;
        } 
        Buffer buffer = new Buffer();
        buffer.writeUtf8(paramString, 0, i);
        while (i < j) {
          int m = paramString.codePointAt(i);
          if (m > 31 && m < 127) {
            k = m;
          } else {
            k = 63;
          } 
          buffer.writeUtf8CodePoint(k);
          i += Character.charCount(m);
        } 
        str = buffer.readUtf8();
      } 
      break;
    } 
    return str;
  }
  
  public static String trimSubstring(String paramString, int paramInt1, int paramInt2) {
    paramInt1 = skipLeadingAsciiWhitespace(paramString, paramInt1, paramInt2);
    return paramString.substring(paramInt1, skipTrailingAsciiWhitespace(paramString, paramInt1, paramInt2));
  }
  
  public static boolean verifyAsIpAddress(String paramString) {
    return VERIFY_AS_IP_ADDRESS.matcher(paramString).matches();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/Util.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */