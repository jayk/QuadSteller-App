package app.gamer.quadstellar.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimerUtils {
  public static Date format(String paramString) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
    ParseException parseException2 = null;
    try {
      Date date = simpleDateFormat.parse(paramString);
    } catch (ParseException parseException1) {
      parseException1.printStackTrace();
      parseException1 = parseException2;
    } 
    return (Date)parseException1;
  }
  
  public static int getCurHour() {
    return Calendar.getInstance().get(11);
  }
  
  public static int getCurMin() {
    return Calendar.getInstance().get(12);
  }
  
  public static byte[] getCurTimeData() {
    Calendar calendar = Calendar.getInstance();
    int i = calendar.get(1);
    int j = calendar.get(2);
    int k = calendar.get(7);
    int m = calendar.get(5);
    int n = calendar.get(11);
    int i1 = calendar.get(12);
    int i2 = calendar.get(13);
    return new byte[] { (byte)(i - 2000 & 0xFF), (byte)(j + 1 & 0xFF), (byte)(k - 1 & 0xFF), (byte)(m & 0xFF), (byte)(n & 0xFF), (byte)(i1 & 0xFF), (byte)(i2 & 0xFF) };
  }
  
  public static byte[] getZigCurTimeData() {
    Calendar calendar = Calendar.getInstance();
    int i = calendar.get(7);
    int j = calendar.get(11);
    int k = calendar.get(12);
    return new byte[] { (byte)(i - 1 & 0xFF), (byte)(j & 0xFF), (byte)(k & 0xFF) };
  }
  
  public static byte[] timerBetween(String paramString, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic format : (Ljava/lang/String;)Ljava/util/Date;
    //   4: astore_2
    //   5: iconst_4
    //   6: newarray byte
    //   8: astore_3
    //   9: new java/text/SimpleDateFormat
    //   12: dup
    //   13: ldc 'HH:mm'
    //   15: invokespecial <init> : (Ljava/lang/String;)V
    //   18: astore #4
    //   20: aconst_null
    //   21: astore #5
    //   23: aload #5
    //   25: astore_0
    //   26: new java/util/Date
    //   29: astore #6
    //   31: aload #5
    //   33: astore_0
    //   34: aload #6
    //   36: invokespecial <init> : ()V
    //   39: aload #5
    //   41: astore_0
    //   42: aload #4
    //   44: aload #4
    //   46: aload #6
    //   48: invokevirtual format : (Ljava/util/Date;)Ljava/lang/String;
    //   51: invokevirtual parse : (Ljava/lang/String;)Ljava/util/Date;
    //   54: astore #5
    //   56: aload #5
    //   58: astore_0
    //   59: aload #4
    //   61: aload #4
    //   63: aload_2
    //   64: invokevirtual format : (Ljava/util/Date;)Ljava/lang/String;
    //   67: invokevirtual parse : (Ljava/lang/String;)Ljava/util/Date;
    //   70: astore #4
    //   72: aload #4
    //   74: astore_2
    //   75: aload #5
    //   77: astore_0
    //   78: aload_2
    //   79: astore #5
    //   81: invokestatic getInstance : ()Ljava/util/Calendar;
    //   84: astore_2
    //   85: aload_2
    //   86: aload_0
    //   87: invokevirtual setTime : (Ljava/util/Date;)V
    //   90: aload_2
    //   91: invokevirtual getTimeInMillis : ()J
    //   94: lstore #7
    //   96: aload_2
    //   97: aload #5
    //   99: invokevirtual setTime : (Ljava/util/Date;)V
    //   102: aload_2
    //   103: invokevirtual getTimeInMillis : ()J
    //   106: lstore #9
    //   108: lload #9
    //   110: lload #7
    //   112: lsub
    //   113: ldc2_w 3600000
    //   116: ldiv
    //   117: l2i
    //   118: istore #11
    //   120: lload #9
    //   122: lload #7
    //   124: lsub
    //   125: ldc2_w 60000
    //   128: ldiv
    //   129: ldc2_w 60
    //   132: lrem
    //   133: l2i
    //   134: istore #12
    //   136: iload #12
    //   138: istore #13
    //   140: iload #11
    //   142: istore #14
    //   144: iload #11
    //   146: ifne -> 173
    //   149: iload #12
    //   151: istore #13
    //   153: iload #11
    //   155: istore #14
    //   157: iload #12
    //   159: ifge -> 173
    //   162: bipush #23
    //   164: istore #14
    //   166: iload #12
    //   168: bipush #60
    //   170: iadd
    //   171: istore #13
    //   173: iload #13
    //   175: istore #11
    //   177: iload #14
    //   179: istore #12
    //   181: iload #14
    //   183: ifge -> 213
    //   186: iload #13
    //   188: istore #11
    //   190: iload #14
    //   192: istore #12
    //   194: iload #13
    //   196: ifge -> 213
    //   199: iload #14
    //   201: bipush #23
    //   203: iadd
    //   204: istore #12
    //   206: iload #13
    //   208: bipush #60
    //   210: iadd
    //   211: istore #11
    //   213: aload_3
    //   214: iconst_0
    //   215: iload #12
    //   217: sipush #255
    //   220: iand
    //   221: i2b
    //   222: i2b
    //   223: bastore
    //   224: aload_3
    //   225: iconst_1
    //   226: iload #11
    //   228: sipush #255
    //   231: iand
    //   232: i2b
    //   233: i2b
    //   234: bastore
    //   235: aload_3
    //   236: iconst_2
    //   237: iconst_0
    //   238: i2b
    //   239: bastore
    //   240: iload_1
    //   241: ifeq -> 264
    //   244: aload_3
    //   245: iconst_3
    //   246: iconst_1
    //   247: i2b
    //   248: bastore
    //   249: aload_3
    //   250: areturn
    //   251: astore #5
    //   253: aload #5
    //   255: invokevirtual printStackTrace : ()V
    //   258: aload_2
    //   259: astore #5
    //   261: goto -> 81
    //   264: aload_3
    //   265: iconst_3
    //   266: iconst_0
    //   267: i2b
    //   268: bastore
    //   269: goto -> 249
    // Exception table:
    //   from	to	target	type
    //   26	31	251	java/text/ParseException
    //   34	39	251	java/text/ParseException
    //   42	56	251	java/text/ParseException
    //   59	72	251	java/text/ParseException
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/utils/TimerUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */