package okhttp3.internal.http;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import okhttp3.internal.Util;

public final class HttpDate {
  private static final DateFormat[] BROWSER_COMPATIBLE_DATE_FORMATS;
  
  private static final String[] BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS;
  
  public static final long MAX_DATE = 253402300799999L;
  
  private static final ThreadLocal<DateFormat> STANDARD_DATE_FORMAT = new ThreadLocal<DateFormat>() {
      protected DateFormat initialValue() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        simpleDateFormat.setLenient(false);
        simpleDateFormat.setTimeZone(Util.UTC);
        return simpleDateFormat;
      }
    };
  
  static {
    BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS = new String[] { 
        "EEE, dd MMM yyyy HH:mm:ss zzz", "EEEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy", "EEE, dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MMM-yyyy HH-mm-ss z", "EEE, dd MMM yy HH:mm:ss z", "EEE dd-MMM-yyyy HH:mm:ss z", "EEE dd MMM yyyy HH:mm:ss z", "EEE dd-MMM-yyyy HH-mm-ss z", "EEE dd-MMM-yy HH:mm:ss z", 
        "EEE dd MMM yy HH:mm:ss z", "EEE,dd-MMM-yy HH:mm:ss z", "EEE,dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MM-yyyy HH:mm:ss z", "EEE MMM d yyyy HH:mm:ss z" };
    BROWSER_COMPATIBLE_DATE_FORMATS = new DateFormat[BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS.length];
  }
  
  public static String format(Date paramDate) {
    return ((DateFormat)STANDARD_DATE_FORMAT.get()).format(paramDate);
  }
  
  public static Date parse(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual length : ()I
    //   4: ifne -> 11
    //   7: aconst_null
    //   8: astore_1
    //   9: aload_1
    //   10: areturn
    //   11: new java/text/ParsePosition
    //   14: dup
    //   15: iconst_0
    //   16: invokespecial <init> : (I)V
    //   19: astore_2
    //   20: getstatic okhttp3/internal/http/HttpDate.STANDARD_DATE_FORMAT : Ljava/lang/ThreadLocal;
    //   23: invokevirtual get : ()Ljava/lang/Object;
    //   26: checkcast java/text/DateFormat
    //   29: aload_0
    //   30: aload_2
    //   31: invokevirtual parse : (Ljava/lang/String;Ljava/text/ParsePosition;)Ljava/util/Date;
    //   34: astore_1
    //   35: aload_2
    //   36: invokevirtual getIndex : ()I
    //   39: aload_0
    //   40: invokevirtual length : ()I
    //   43: if_icmpeq -> 9
    //   46: getstatic okhttp3/internal/http/HttpDate.BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS : [Ljava/lang/String;
    //   49: astore_3
    //   50: aload_3
    //   51: monitorenter
    //   52: iconst_0
    //   53: istore #4
    //   55: getstatic okhttp3/internal/http/HttpDate.BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS : [Ljava/lang/String;
    //   58: arraylength
    //   59: istore #5
    //   61: iload #4
    //   63: iload #5
    //   65: if_icmpge -> 150
    //   68: getstatic okhttp3/internal/http/HttpDate.BROWSER_COMPATIBLE_DATE_FORMATS : [Ljava/text/DateFormat;
    //   71: iload #4
    //   73: aaload
    //   74: astore #6
    //   76: aload #6
    //   78: astore_1
    //   79: aload #6
    //   81: ifnonnull -> 115
    //   84: new java/text/SimpleDateFormat
    //   87: astore_1
    //   88: aload_1
    //   89: getstatic okhttp3/internal/http/HttpDate.BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS : [Ljava/lang/String;
    //   92: iload #4
    //   94: aaload
    //   95: getstatic java/util/Locale.US : Ljava/util/Locale;
    //   98: invokespecial <init> : (Ljava/lang/String;Ljava/util/Locale;)V
    //   101: aload_1
    //   102: getstatic okhttp3/internal/Util.UTC : Ljava/util/TimeZone;
    //   105: invokevirtual setTimeZone : (Ljava/util/TimeZone;)V
    //   108: getstatic okhttp3/internal/http/HttpDate.BROWSER_COMPATIBLE_DATE_FORMATS : [Ljava/text/DateFormat;
    //   111: iload #4
    //   113: aload_1
    //   114: aastore
    //   115: aload_2
    //   116: iconst_0
    //   117: invokevirtual setIndex : (I)V
    //   120: aload_1
    //   121: aload_0
    //   122: aload_2
    //   123: invokevirtual parse : (Ljava/lang/String;Ljava/text/ParsePosition;)Ljava/util/Date;
    //   126: astore_1
    //   127: aload_2
    //   128: invokevirtual getIndex : ()I
    //   131: ifeq -> 144
    //   134: aload_3
    //   135: monitorexit
    //   136: goto -> 9
    //   139: astore_0
    //   140: aload_3
    //   141: monitorexit
    //   142: aload_0
    //   143: athrow
    //   144: iinc #4, 1
    //   147: goto -> 61
    //   150: aload_3
    //   151: monitorexit
    //   152: aconst_null
    //   153: astore_1
    //   154: goto -> 9
    // Exception table:
    //   from	to	target	type
    //   55	61	139	finally
    //   68	76	139	finally
    //   84	115	139	finally
    //   115	136	139	finally
    //   140	142	139	finally
    //   150	152	139	finally
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/http/HttpDate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */