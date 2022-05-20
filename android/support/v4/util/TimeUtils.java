package android.support.v4.util;

import android.support.annotation.RestrictTo;
import java.io.PrintWriter;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public final class TimeUtils {
  public static final int HUNDRED_DAY_FIELD_LEN = 19;
  
  private static final int SECONDS_PER_DAY = 86400;
  
  private static final int SECONDS_PER_HOUR = 3600;
  
  private static final int SECONDS_PER_MINUTE = 60;
  
  private static char[] sFormatStr;
  
  private static final Object sFormatSync = new Object();
  
  static {
    sFormatStr = new char[24];
  }
  
  private static int accumField(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3) {
    return (paramInt1 > 99 || (paramBoolean && paramInt3 >= 3)) ? (paramInt2 + 3) : ((paramInt1 > 9 || (paramBoolean && paramInt3 >= 2)) ? (paramInt2 + 2) : ((paramBoolean || paramInt1 > 0) ? (paramInt2 + 1) : 0));
  }
  
  public static void formatDuration(long paramLong1, long paramLong2, PrintWriter paramPrintWriter) {
    if (paramLong1 == 0L) {
      paramPrintWriter.print("--");
      return;
    } 
    formatDuration(paramLong1 - paramLong2, paramPrintWriter, 0);
  }
  
  public static void formatDuration(long paramLong, PrintWriter paramPrintWriter) {
    formatDuration(paramLong, paramPrintWriter, 0);
  }
  
  public static void formatDuration(long paramLong, PrintWriter paramPrintWriter, int paramInt) {
    synchronized (sFormatSync) {
      paramInt = formatDurationLocked(paramLong, paramInt);
      String str = new String();
      this(sFormatStr, 0, paramInt);
      paramPrintWriter.print(str);
      return;
    } 
  }
  
  public static void formatDuration(long paramLong, StringBuilder paramStringBuilder) {
    synchronized (sFormatSync) {
      int i = formatDurationLocked(paramLong, 0);
      paramStringBuilder.append(sFormatStr, 0, i);
      return;
    } 
  }
  
  private static int formatDurationLocked(long paramLong, int paramInt) {
    byte b;
    boolean bool2;
    if (sFormatStr.length < paramInt)
      sFormatStr = new char[paramInt]; 
    char[] arrayOfChar = sFormatStr;
    if (paramLong == 0L) {
      while (paramInt - 1 < 0)
        arrayOfChar[0] = (char)' '; 
      arrayOfChar[0] = (char)'0';
      return 1;
    } 
    if (paramLong > 0L) {
      b = 43;
    } else {
      b = 45;
      paramLong = -paramLong;
    } 
    int i = (int)(paramLong % 1000L);
    int j = (int)Math.floor((paramLong / 1000L));
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = j;
    if (j > 86400) {
      k = j / 86400;
      i1 = j - 86400 * k;
    } 
    j = i1;
    if (i1 > 3600) {
      m = i1 / 3600;
      j = i1 - m * 3600;
    } 
    int i2 = j;
    if (j > 60) {
      n = j / 60;
      i2 = j - n * 60;
    } 
    int i3 = 0;
    boolean bool1 = false;
    if (paramInt != 0) {
      i1 = accumField(k, 1, false, 0);
      if (i1 > 0) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      i1 += accumField(m, 1, bool2, 2);
      if (i1 > 0) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      i1 += accumField(n, 1, bool2, 2);
      if (i1 > 0) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      j = i1 + accumField(i2, 1, bool2, 2);
      if (j > 0) {
        i1 = 3;
      } else {
        i1 = 0;
      } 
      j += accumField(i, 2, true, i1) + 1;
      i1 = bool1;
      while (true) {
        i3 = i1;
        if (j < paramInt) {
          arrayOfChar[i1] = (char)' ';
          i1++;
          j++;
          continue;
        } 
        break;
      } 
    } 
    arrayOfChar[i3] = (char)b;
    j = i3 + 1;
    if (paramInt != 0) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    k = printField(arrayOfChar, k, 'd', j, false, 0);
    if (k != j) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (paramInt != 0) {
      i1 = 2;
    } else {
      i1 = 0;
    } 
    k = printField(arrayOfChar, m, 'h', k, bool2, i1);
    if (k != j) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (paramInt != 0) {
      i1 = 2;
    } else {
      i1 = 0;
    } 
    n = printField(arrayOfChar, n, 'm', k, bool2, i1);
    if (n != j) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (paramInt != 0) {
      i1 = 2;
    } else {
      i1 = 0;
    } 
    i1 = printField(arrayOfChar, i2, 's', n, bool2, i1);
    if (paramInt != 0 && i1 != j) {
      paramInt = 3;
    } else {
      paramInt = 0;
    } 
    paramInt = printField(arrayOfChar, i, 'm', i1, true, paramInt);
    arrayOfChar[paramInt] = (char)'s';
    return ++paramInt;
  }
  
  private static int printField(char[] paramArrayOfchar, int paramInt1, char paramChar, int paramInt2, boolean paramBoolean, int paramInt3) {
    // Byte code:
    //   0: iload #4
    //   2: ifne -> 12
    //   5: iload_3
    //   6: istore #6
    //   8: iload_1
    //   9: ifle -> 149
    //   12: iload #4
    //   14: ifeq -> 23
    //   17: iload #5
    //   19: iconst_3
    //   20: if_icmpge -> 35
    //   23: iload_1
    //   24: istore #6
    //   26: iload_3
    //   27: istore #7
    //   29: iload_1
    //   30: bipush #99
    //   32: if_icmple -> 65
    //   35: iload_1
    //   36: bipush #100
    //   38: idiv
    //   39: istore #6
    //   41: aload_0
    //   42: iload_3
    //   43: iload #6
    //   45: bipush #48
    //   47: iadd
    //   48: i2c
    //   49: i2c
    //   50: castore
    //   51: iload_3
    //   52: iconst_1
    //   53: iadd
    //   54: istore #7
    //   56: iload_1
    //   57: iload #6
    //   59: bipush #100
    //   61: imul
    //   62: isub
    //   63: istore #6
    //   65: iload #4
    //   67: ifeq -> 76
    //   70: iload #5
    //   72: iconst_2
    //   73: if_icmpge -> 96
    //   76: iload #6
    //   78: bipush #9
    //   80: if_icmpgt -> 96
    //   83: iload #6
    //   85: istore #5
    //   87: iload #7
    //   89: istore_1
    //   90: iload_3
    //   91: iload #7
    //   93: if_icmpeq -> 126
    //   96: iload #6
    //   98: bipush #10
    //   100: idiv
    //   101: istore_3
    //   102: aload_0
    //   103: iload #7
    //   105: iload_3
    //   106: bipush #48
    //   108: iadd
    //   109: i2c
    //   110: i2c
    //   111: castore
    //   112: iload #7
    //   114: iconst_1
    //   115: iadd
    //   116: istore_1
    //   117: iload #6
    //   119: iload_3
    //   120: bipush #10
    //   122: imul
    //   123: isub
    //   124: istore #5
    //   126: aload_0
    //   127: iload_1
    //   128: iload #5
    //   130: bipush #48
    //   132: iadd
    //   133: i2c
    //   134: i2c
    //   135: castore
    //   136: iinc #1, 1
    //   139: aload_0
    //   140: iload_1
    //   141: iload_2
    //   142: i2c
    //   143: castore
    //   144: iload_1
    //   145: iconst_1
    //   146: iadd
    //   147: istore #6
    //   149: iload #6
    //   151: ireturn
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/util/TimeUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */