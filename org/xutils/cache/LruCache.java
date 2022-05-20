package org.xutils.cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<K, V> {
  private int createCount;
  
  private int evictionCount;
  
  private int hitCount;
  
  private final LinkedHashMap<K, V> map;
  
  private int maxSize;
  
  private int missCount;
  
  private int putCount;
  
  private int size;
  
  public LruCache(int paramInt) {
    if (paramInt <= 0)
      throw new IllegalArgumentException("maxSize <= 0"); 
    this.maxSize = paramInt;
    this.map = new LinkedHashMap<K, V>(0, 0.75F, true);
  }
  
  private int safeSizeOf(K paramK, V paramV) {
    int i = sizeOf(paramK, paramV);
    if (i < 0)
      throw new IllegalStateException("Negative size: " + paramK + "=" + paramV); 
    return i;
  }
  
  protected V create(K paramK) {
    return null;
  }
  
  public final int createCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield createCount : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  protected void entryRemoved(boolean paramBoolean, K paramK, V paramV1, V paramV2) {}
  
  public final void evictAll() {
    trimToSize(-1);
  }
  
  public final int evictionCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield evictionCount : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public final V get(K paramK) {
    // Byte code:
    //   0: aload_1
    //   1: ifnonnull -> 14
    //   4: new java/lang/NullPointerException
    //   7: dup
    //   8: ldc 'key == null'
    //   10: invokespecial <init> : (Ljava/lang/String;)V
    //   13: athrow
    //   14: aload_0
    //   15: monitorenter
    //   16: aload_0
    //   17: getfield map : Ljava/util/LinkedHashMap;
    //   20: aload_1
    //   21: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   24: astore_2
    //   25: aload_2
    //   26: ifnull -> 45
    //   29: aload_0
    //   30: aload_0
    //   31: getfield hitCount : I
    //   34: iconst_1
    //   35: iadd
    //   36: putfield hitCount : I
    //   39: aload_0
    //   40: monitorexit
    //   41: aload_2
    //   42: astore_1
    //   43: aload_1
    //   44: areturn
    //   45: aload_0
    //   46: aload_0
    //   47: getfield missCount : I
    //   50: iconst_1
    //   51: iadd
    //   52: putfield missCount : I
    //   55: aload_0
    //   56: monitorexit
    //   57: aload_0
    //   58: aload_1
    //   59: invokevirtual create : (Ljava/lang/Object;)Ljava/lang/Object;
    //   62: astore_2
    //   63: aload_2
    //   64: ifnonnull -> 77
    //   67: aconst_null
    //   68: astore_1
    //   69: goto -> 43
    //   72: astore_1
    //   73: aload_0
    //   74: monitorexit
    //   75: aload_1
    //   76: athrow
    //   77: aload_0
    //   78: monitorenter
    //   79: aload_0
    //   80: aload_0
    //   81: getfield createCount : I
    //   84: iconst_1
    //   85: iadd
    //   86: putfield createCount : I
    //   89: aload_0
    //   90: getfield map : Ljava/util/LinkedHashMap;
    //   93: aload_1
    //   94: aload_2
    //   95: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   98: astore_3
    //   99: aload_3
    //   100: ifnull -> 132
    //   103: aload_0
    //   104: getfield map : Ljava/util/LinkedHashMap;
    //   107: aload_1
    //   108: aload_3
    //   109: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   112: pop
    //   113: aload_0
    //   114: monitorexit
    //   115: aload_3
    //   116: ifnull -> 155
    //   119: aload_0
    //   120: iconst_0
    //   121: aload_1
    //   122: aload_2
    //   123: aload_3
    //   124: invokevirtual entryRemoved : (ZLjava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
    //   127: aload_3
    //   128: astore_1
    //   129: goto -> 43
    //   132: aload_0
    //   133: aload_0
    //   134: getfield size : I
    //   137: aload_0
    //   138: aload_1
    //   139: aload_2
    //   140: invokespecial safeSizeOf : (Ljava/lang/Object;Ljava/lang/Object;)I
    //   143: iadd
    //   144: putfield size : I
    //   147: goto -> 113
    //   150: astore_1
    //   151: aload_0
    //   152: monitorexit
    //   153: aload_1
    //   154: athrow
    //   155: aload_0
    //   156: aload_0
    //   157: getfield maxSize : I
    //   160: invokevirtual trimToSize : (I)V
    //   163: aload_2
    //   164: astore_1
    //   165: goto -> 43
    // Exception table:
    //   from	to	target	type
    //   16	25	72	finally
    //   29	41	72	finally
    //   45	57	72	finally
    //   73	75	72	finally
    //   79	99	150	finally
    //   103	113	150	finally
    //   113	115	150	finally
    //   132	147	150	finally
    //   151	153	150	finally
  }
  
  public final int hitCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield hitCount : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public final int maxSize() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield maxSize : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public final int missCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield missCount : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public final V put(K paramK, V paramV) {
    // Byte code:
    //   0: aload_1
    //   1: ifnull -> 8
    //   4: aload_2
    //   5: ifnonnull -> 18
    //   8: new java/lang/NullPointerException
    //   11: dup
    //   12: ldc 'key == null || value == null'
    //   14: invokespecial <init> : (Ljava/lang/String;)V
    //   17: athrow
    //   18: aload_0
    //   19: monitorenter
    //   20: aload_0
    //   21: aload_0
    //   22: getfield putCount : I
    //   25: iconst_1
    //   26: iadd
    //   27: putfield putCount : I
    //   30: aload_0
    //   31: aload_0
    //   32: getfield size : I
    //   35: aload_0
    //   36: aload_1
    //   37: aload_2
    //   38: invokespecial safeSizeOf : (Ljava/lang/Object;Ljava/lang/Object;)I
    //   41: iadd
    //   42: putfield size : I
    //   45: aload_0
    //   46: getfield map : Ljava/util/LinkedHashMap;
    //   49: aload_1
    //   50: aload_2
    //   51: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   54: astore_3
    //   55: aload_3
    //   56: ifnull -> 74
    //   59: aload_0
    //   60: aload_0
    //   61: getfield size : I
    //   64: aload_0
    //   65: aload_1
    //   66: aload_3
    //   67: invokespecial safeSizeOf : (Ljava/lang/Object;Ljava/lang/Object;)I
    //   70: isub
    //   71: putfield size : I
    //   74: aload_0
    //   75: monitorexit
    //   76: aload_3
    //   77: ifnull -> 88
    //   80: aload_0
    //   81: iconst_0
    //   82: aload_1
    //   83: aload_3
    //   84: aload_2
    //   85: invokevirtual entryRemoved : (ZLjava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
    //   88: aload_0
    //   89: aload_0
    //   90: getfield maxSize : I
    //   93: invokevirtual trimToSize : (I)V
    //   96: aload_3
    //   97: areturn
    //   98: astore_1
    //   99: aload_0
    //   100: monitorexit
    //   101: aload_1
    //   102: athrow
    // Exception table:
    //   from	to	target	type
    //   20	55	98	finally
    //   59	74	98	finally
    //   74	76	98	finally
    //   99	101	98	finally
  }
  
  public final int putCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield putCount : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public final V remove(K paramK) {
    // Byte code:
    //   0: aload_1
    //   1: ifnonnull -> 14
    //   4: new java/lang/NullPointerException
    //   7: dup
    //   8: ldc 'key == null'
    //   10: invokespecial <init> : (Ljava/lang/String;)V
    //   13: athrow
    //   14: aload_0
    //   15: monitorenter
    //   16: aload_0
    //   17: getfield map : Ljava/util/LinkedHashMap;
    //   20: aload_1
    //   21: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   24: astore_2
    //   25: aload_2
    //   26: ifnull -> 44
    //   29: aload_0
    //   30: aload_0
    //   31: getfield size : I
    //   34: aload_0
    //   35: aload_1
    //   36: aload_2
    //   37: invokespecial safeSizeOf : (Ljava/lang/Object;Ljava/lang/Object;)I
    //   40: isub
    //   41: putfield size : I
    //   44: aload_0
    //   45: monitorexit
    //   46: aload_2
    //   47: ifnull -> 58
    //   50: aload_0
    //   51: iconst_0
    //   52: aload_1
    //   53: aload_2
    //   54: aconst_null
    //   55: invokevirtual entryRemoved : (ZLjava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
    //   58: aload_2
    //   59: areturn
    //   60: astore_1
    //   61: aload_0
    //   62: monitorexit
    //   63: aload_1
    //   64: athrow
    // Exception table:
    //   from	to	target	type
    //   16	25	60	finally
    //   29	44	60	finally
    //   44	46	60	finally
    //   61	63	60	finally
  }
  
  public void resize(int paramInt) {
    // Byte code:
    //   0: iload_1
    //   1: ifgt -> 14
    //   4: new java/lang/IllegalArgumentException
    //   7: dup
    //   8: ldc 'maxSize <= 0'
    //   10: invokespecial <init> : (Ljava/lang/String;)V
    //   13: athrow
    //   14: aload_0
    //   15: monitorenter
    //   16: aload_0
    //   17: iload_1
    //   18: putfield maxSize : I
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_0
    //   24: iload_1
    //   25: invokevirtual trimToSize : (I)V
    //   28: return
    //   29: astore_2
    //   30: aload_0
    //   31: monitorexit
    //   32: aload_2
    //   33: athrow
    // Exception table:
    //   from	to	target	type
    //   16	23	29	finally
    //   30	32	29	finally
  }
  
  public final int size() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield size : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  protected int sizeOf(K paramK, V paramV) {
    return 1;
  }
  
  public final Map<K, V> snapshot() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/LinkedHashMap
    //   5: dup
    //   6: aload_0
    //   7: getfield map : Ljava/util/LinkedHashMap;
    //   10: invokespecial <init> : (Ljava/util/Map;)V
    //   13: astore_1
    //   14: aload_0
    //   15: monitorexit
    //   16: aload_1
    //   17: areturn
    //   18: astore_1
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_1
    //   22: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	18	finally
  }
  
  public final String toString() {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: monitorenter
    //   4: aload_0
    //   5: getfield hitCount : I
    //   8: aload_0
    //   9: getfield missCount : I
    //   12: iadd
    //   13: istore_2
    //   14: iload_2
    //   15: ifeq -> 28
    //   18: aload_0
    //   19: getfield hitCount : I
    //   22: bipush #100
    //   24: imul
    //   25: iload_2
    //   26: idiv
    //   27: istore_1
    //   28: ldc 'LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]'
    //   30: iconst_4
    //   31: anewarray java/lang/Object
    //   34: dup
    //   35: iconst_0
    //   36: aload_0
    //   37: getfield maxSize : I
    //   40: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   43: aastore
    //   44: dup
    //   45: iconst_1
    //   46: aload_0
    //   47: getfield hitCount : I
    //   50: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   53: aastore
    //   54: dup
    //   55: iconst_2
    //   56: aload_0
    //   57: getfield missCount : I
    //   60: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   63: aastore
    //   64: dup
    //   65: iconst_3
    //   66: iload_1
    //   67: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   70: aastore
    //   71: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   74: astore_3
    //   75: aload_0
    //   76: monitorexit
    //   77: aload_3
    //   78: areturn
    //   79: astore_3
    //   80: aload_0
    //   81: monitorexit
    //   82: aload_3
    //   83: athrow
    // Exception table:
    //   from	to	target	type
    //   4	14	79	finally
    //   18	28	79	finally
    //   28	75	79	finally
  }
  
  public void trimToSize(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield size : I
    //   6: iflt -> 26
    //   9: aload_0
    //   10: getfield map : Ljava/util/LinkedHashMap;
    //   13: invokevirtual isEmpty : ()Z
    //   16: ifeq -> 68
    //   19: aload_0
    //   20: getfield size : I
    //   23: ifeq -> 68
    //   26: new java/lang/IllegalStateException
    //   29: astore_2
    //   30: new java/lang/StringBuilder
    //   33: astore_3
    //   34: aload_3
    //   35: invokespecial <init> : ()V
    //   38: aload_2
    //   39: aload_3
    //   40: aload_0
    //   41: invokevirtual getClass : ()Ljava/lang/Class;
    //   44: invokevirtual getName : ()Ljava/lang/String;
    //   47: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   50: ldc '.sizeOf() is reporting inconsistent results!'
    //   52: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   55: invokevirtual toString : ()Ljava/lang/String;
    //   58: invokespecial <init> : (Ljava/lang/String;)V
    //   61: aload_2
    //   62: athrow
    //   63: astore_3
    //   64: aload_0
    //   65: monitorexit
    //   66: aload_3
    //   67: athrow
    //   68: aload_0
    //   69: getfield size : I
    //   72: iload_1
    //   73: if_icmple -> 86
    //   76: aload_0
    //   77: getfield map : Ljava/util/LinkedHashMap;
    //   80: invokevirtual isEmpty : ()Z
    //   83: ifeq -> 89
    //   86: aload_0
    //   87: monitorexit
    //   88: return
    //   89: aload_0
    //   90: getfield map : Ljava/util/LinkedHashMap;
    //   93: invokevirtual entrySet : ()Ljava/util/Set;
    //   96: invokeinterface iterator : ()Ljava/util/Iterator;
    //   101: invokeinterface next : ()Ljava/lang/Object;
    //   106: checkcast java/util/Map$Entry
    //   109: astore_2
    //   110: aload_2
    //   111: invokeinterface getKey : ()Ljava/lang/Object;
    //   116: astore_3
    //   117: aload_2
    //   118: invokeinterface getValue : ()Ljava/lang/Object;
    //   123: astore_2
    //   124: aload_0
    //   125: getfield map : Ljava/util/LinkedHashMap;
    //   128: aload_3
    //   129: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   132: pop
    //   133: aload_0
    //   134: aload_0
    //   135: getfield size : I
    //   138: aload_0
    //   139: aload_3
    //   140: aload_2
    //   141: invokespecial safeSizeOf : (Ljava/lang/Object;Ljava/lang/Object;)I
    //   144: isub
    //   145: putfield size : I
    //   148: aload_0
    //   149: aload_0
    //   150: getfield evictionCount : I
    //   153: iconst_1
    //   154: iadd
    //   155: putfield evictionCount : I
    //   158: aload_0
    //   159: monitorexit
    //   160: aload_0
    //   161: iconst_1
    //   162: aload_3
    //   163: aload_2
    //   164: aconst_null
    //   165: invokevirtual entryRemoved : (ZLjava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
    //   168: goto -> 0
    // Exception table:
    //   from	to	target	type
    //   2	26	63	finally
    //   26	63	63	finally
    //   64	66	63	finally
    //   68	86	63	finally
    //   86	88	63	finally
    //   89	160	63	finally
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/cache/LruCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */