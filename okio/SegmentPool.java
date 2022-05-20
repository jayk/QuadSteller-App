package okio;

import javax.annotation.Nullable;

final class SegmentPool {
  static final long MAX_SIZE = 65536L;
  
  static long byteCount;
  
  @Nullable
  static Segment next;
  
  static void recycle(Segment paramSegment) {
    // Byte code:
    //   0: aload_0
    //   1: getfield next : Lokio/Segment;
    //   4: ifnonnull -> 14
    //   7: aload_0
    //   8: getfield prev : Lokio/Segment;
    //   11: ifnull -> 22
    //   14: new java/lang/IllegalArgumentException
    //   17: dup
    //   18: invokespecial <init> : ()V
    //   21: athrow
    //   22: aload_0
    //   23: getfield shared : Z
    //   26: ifeq -> 30
    //   29: return
    //   30: ldc okio/SegmentPool
    //   32: monitorenter
    //   33: getstatic okio/SegmentPool.byteCount : J
    //   36: ldc2_w 8192
    //   39: ladd
    //   40: ldc2_w 65536
    //   43: lcmp
    //   44: ifle -> 59
    //   47: ldc okio/SegmentPool
    //   49: monitorexit
    //   50: goto -> 29
    //   53: astore_0
    //   54: ldc okio/SegmentPool
    //   56: monitorexit
    //   57: aload_0
    //   58: athrow
    //   59: getstatic okio/SegmentPool.byteCount : J
    //   62: ldc2_w 8192
    //   65: ladd
    //   66: putstatic okio/SegmentPool.byteCount : J
    //   69: aload_0
    //   70: getstatic okio/SegmentPool.next : Lokio/Segment;
    //   73: putfield next : Lokio/Segment;
    //   76: aload_0
    //   77: iconst_0
    //   78: putfield limit : I
    //   81: aload_0
    //   82: iconst_0
    //   83: putfield pos : I
    //   86: aload_0
    //   87: putstatic okio/SegmentPool.next : Lokio/Segment;
    //   90: ldc okio/SegmentPool
    //   92: monitorexit
    //   93: goto -> 29
    // Exception table:
    //   from	to	target	type
    //   33	50	53	finally
    //   54	57	53	finally
    //   59	93	53	finally
  }
  
  static Segment take() {
    // Byte code:
    //   0: ldc okio/SegmentPool
    //   2: monitorenter
    //   3: getstatic okio/SegmentPool.next : Lokio/Segment;
    //   6: ifnull -> 40
    //   9: getstatic okio/SegmentPool.next : Lokio/Segment;
    //   12: astore_0
    //   13: aload_0
    //   14: getfield next : Lokio/Segment;
    //   17: putstatic okio/SegmentPool.next : Lokio/Segment;
    //   20: aload_0
    //   21: aconst_null
    //   22: putfield next : Lokio/Segment;
    //   25: getstatic okio/SegmentPool.byteCount : J
    //   28: ldc2_w 8192
    //   31: lsub
    //   32: putstatic okio/SegmentPool.byteCount : J
    //   35: ldc okio/SegmentPool
    //   37: monitorexit
    //   38: aload_0
    //   39: areturn
    //   40: ldc okio/SegmentPool
    //   42: monitorexit
    //   43: new okio/Segment
    //   46: dup
    //   47: invokespecial <init> : ()V
    //   50: astore_0
    //   51: goto -> 38
    //   54: astore_0
    //   55: ldc okio/SegmentPool
    //   57: monitorexit
    //   58: aload_0
    //   59: athrow
    // Exception table:
    //   from	to	target	type
    //   3	38	54	finally
    //   40	43	54	finally
    //   55	58	54	finally
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okio/SegmentPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */