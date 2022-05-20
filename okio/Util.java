package okio;

import java.nio.charset.Charset;

final class Util {
  public static final Charset UTF_8 = Charset.forName("UTF-8");
  
  public static boolean arrayRangeEquals(byte[] paramArrayOfbyte1, int paramInt1, byte[] paramArrayOfbyte2, int paramInt2, int paramInt3) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #5
    //   3: iload #5
    //   5: iload #4
    //   7: if_icmpge -> 37
    //   10: aload_0
    //   11: iload #5
    //   13: iload_1
    //   14: iadd
    //   15: baload
    //   16: aload_2
    //   17: iload #5
    //   19: iload_3
    //   20: iadd
    //   21: baload
    //   22: if_icmpeq -> 31
    //   25: iconst_0
    //   26: istore #6
    //   28: iload #6
    //   30: ireturn
    //   31: iinc #5, 1
    //   34: goto -> 3
    //   37: iconst_1
    //   38: istore #6
    //   40: goto -> 28
  }
  
  public static void checkOffsetAndCount(long paramLong1, long paramLong2, long paramLong3) {
    if ((paramLong2 | paramLong3) < 0L || paramLong2 > paramLong1 || paramLong1 - paramLong2 < paramLong3)
      throw new ArrayIndexOutOfBoundsException(String.format("size=%s offset=%s byteCount=%s", new Object[] { Long.valueOf(paramLong1), Long.valueOf(paramLong2), Long.valueOf(paramLong3) })); 
  }
  
  public static int reverseBytesInt(int paramInt) {
    return (0xFF000000 & paramInt) >>> 24 | (0xFF0000 & paramInt) >>> 8 | (0xFF00 & paramInt) << 8 | (paramInt & 0xFF) << 24;
  }
  
  public static long reverseBytesLong(long paramLong) {
    return (0xFF00000000000000L & paramLong) >>> 56L | (0xFF000000000000L & paramLong) >>> 40L | (0xFF0000000000L & paramLong) >>> 24L | (0xFF00000000L & paramLong) >>> 8L | (0xFF000000L & paramLong) << 8L | (0xFF0000L & paramLong) << 24L | (0xFF00L & paramLong) << 40L | (0xFFL & paramLong) << 56L;
  }
  
  public static short reverseBytesShort(short paramShort) {
    int i = paramShort & 0xFFFF;
    return (short)((0xFF00 & i) >>> 8 | (i & 0xFF) << 8);
  }
  
  public static void sneakyRethrow(Throwable paramThrowable) {
    sneakyThrow2(paramThrowable);
  }
  
  private static <T extends Throwable> void sneakyThrow2(Throwable paramThrowable) throws T {
    throw (T)paramThrowable;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okio/Util.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */