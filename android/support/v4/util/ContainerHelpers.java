package android.support.v4.util;

class ContainerHelpers {
  static final int[] EMPTY_INTS = new int[0];
  
  static final long[] EMPTY_LONGS = new long[0];
  
  static final Object[] EMPTY_OBJECTS = new Object[0];
  
  static int binarySearch(int[] paramArrayOfint, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: iload_1
    //   3: iconst_1
    //   4: isub
    //   5: istore #4
    //   7: iload_3
    //   8: istore_1
    //   9: iload #4
    //   11: istore_3
    //   12: iload_1
    //   13: iload_3
    //   14: if_icmpgt -> 61
    //   17: iload_1
    //   18: iload_3
    //   19: iadd
    //   20: iconst_1
    //   21: iushr
    //   22: istore #4
    //   24: aload_0
    //   25: iload #4
    //   27: iaload
    //   28: istore #5
    //   30: iload #5
    //   32: iload_2
    //   33: if_icmpge -> 44
    //   36: iload #4
    //   38: iconst_1
    //   39: iadd
    //   40: istore_1
    //   41: goto -> 12
    //   44: iload #4
    //   46: istore_3
    //   47: iload #5
    //   49: iload_2
    //   50: if_icmple -> 65
    //   53: iload #4
    //   55: iconst_1
    //   56: isub
    //   57: istore_3
    //   58: goto -> 12
    //   61: iload_1
    //   62: iconst_m1
    //   63: ixor
    //   64: istore_3
    //   65: iload_3
    //   66: ireturn
  }
  
  static int binarySearch(long[] paramArrayOflong, int paramInt, long paramLong) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #4
    //   3: iload_1
    //   4: iconst_1
    //   5: isub
    //   6: istore #5
    //   8: iload #4
    //   10: istore_1
    //   11: iload_1
    //   12: iload #5
    //   14: if_icmpgt -> 66
    //   17: iload_1
    //   18: iload #5
    //   20: iadd
    //   21: iconst_1
    //   22: iushr
    //   23: istore #4
    //   25: aload_0
    //   26: iload #4
    //   28: laload
    //   29: lstore #6
    //   31: lload #6
    //   33: lload_2
    //   34: lcmp
    //   35: ifge -> 46
    //   38: iload #4
    //   40: iconst_1
    //   41: iadd
    //   42: istore_1
    //   43: goto -> 11
    //   46: iload #4
    //   48: istore #5
    //   50: lload #6
    //   52: lload_2
    //   53: lcmp
    //   54: ifle -> 71
    //   57: iload #4
    //   59: iconst_1
    //   60: isub
    //   61: istore #5
    //   63: goto -> 11
    //   66: iload_1
    //   67: iconst_m1
    //   68: ixor
    //   69: istore #5
    //   71: iload #5
    //   73: ireturn
  }
  
  public static boolean equal(Object paramObject1, Object paramObject2) {
    return (paramObject1 == paramObject2 || (paramObject1 != null && paramObject1.equals(paramObject2)));
  }
  
  public static int idealByteArraySize(int paramInt) {
    for (byte b = 4;; b++) {
      int i = paramInt;
      if (b < 32) {
        if (paramInt <= (1 << b) - 12)
          return (1 << b) - 12; 
      } else {
        return i;
      } 
    } 
  }
  
  public static int idealIntArraySize(int paramInt) {
    return idealByteArraySize(paramInt * 4) / 4;
  }
  
  public static int idealLongArraySize(int paramInt) {
    return idealByteArraySize(paramInt * 8) / 8;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/util/ContainerHelpers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */