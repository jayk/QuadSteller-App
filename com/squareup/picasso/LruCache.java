package com.squareup.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import java.util.LinkedHashMap;

public class LruCache implements Cache {
  private int evictionCount;
  
  private int hitCount;
  
  final LinkedHashMap<String, Bitmap> map;
  
  private final int maxSize;
  
  private int missCount;
  
  private int putCount;
  
  private int size;
  
  public LruCache(int paramInt) {
    if (paramInt <= 0)
      throw new IllegalArgumentException("Max size must be positive."); 
    this.maxSize = paramInt;
    this.map = new LinkedHashMap<String, Bitmap>(0, 0.75F, true);
  }
  
  public LruCache(Context paramContext) {
    this(Utils.calculateMemoryCacheSize(paramContext));
  }
  
  private void trimToSize(int paramInt) {
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
    //   63: astore_2
    //   64: aload_0
    //   65: monitorexit
    //   66: aload_2
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
    //   109: astore_3
    //   110: aload_3
    //   111: invokeinterface getKey : ()Ljava/lang/Object;
    //   116: checkcast java/lang/String
    //   119: astore_2
    //   120: aload_3
    //   121: invokeinterface getValue : ()Ljava/lang/Object;
    //   126: checkcast android/graphics/Bitmap
    //   129: astore_3
    //   130: aload_0
    //   131: getfield map : Ljava/util/LinkedHashMap;
    //   134: aload_2
    //   135: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   138: pop
    //   139: aload_0
    //   140: aload_0
    //   141: getfield size : I
    //   144: aload_3
    //   145: invokestatic getBitmapBytes : (Landroid/graphics/Bitmap;)I
    //   148: isub
    //   149: putfield size : I
    //   152: aload_0
    //   153: aload_0
    //   154: getfield evictionCount : I
    //   157: iconst_1
    //   158: iadd
    //   159: putfield evictionCount : I
    //   162: aload_0
    //   163: monitorexit
    //   164: goto -> 0
    // Exception table:
    //   from	to	target	type
    //   2	26	63	finally
    //   26	63	63	finally
    //   64	66	63	finally
    //   68	86	63	finally
    //   86	88	63	finally
    //   89	164	63	finally
  }
  
  public final void clear() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual evictAll : ()V
    //   6: aload_0
    //   7: monitorexit
    //   8: return
    //   9: astore_1
    //   10: aload_0
    //   11: monitorexit
    //   12: aload_1
    //   13: athrow
    // Exception table:
    //   from	to	target	type
    //   2	6	9	finally
  }
  
  public final void clearKeyUri(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iconst_0
    //   3: istore_2
    //   4: aload_1
    //   5: invokevirtual length : ()I
    //   8: istore_3
    //   9: aload_0
    //   10: getfield map : Ljava/util/LinkedHashMap;
    //   13: invokevirtual entrySet : ()Ljava/util/Set;
    //   16: invokeinterface iterator : ()Ljava/util/Iterator;
    //   21: astore #4
    //   23: aload #4
    //   25: invokeinterface hasNext : ()Z
    //   30: ifeq -> 125
    //   33: aload #4
    //   35: invokeinterface next : ()Ljava/lang/Object;
    //   40: checkcast java/util/Map$Entry
    //   43: astore #5
    //   45: aload #5
    //   47: invokeinterface getKey : ()Ljava/lang/Object;
    //   52: checkcast java/lang/String
    //   55: astore #6
    //   57: aload #5
    //   59: invokeinterface getValue : ()Ljava/lang/Object;
    //   64: checkcast android/graphics/Bitmap
    //   67: astore #5
    //   69: aload #6
    //   71: bipush #10
    //   73: invokevirtual indexOf : (I)I
    //   76: istore #7
    //   78: iload #7
    //   80: iload_3
    //   81: if_icmpne -> 23
    //   84: aload #6
    //   86: iconst_0
    //   87: iload #7
    //   89: invokevirtual substring : (II)Ljava/lang/String;
    //   92: aload_1
    //   93: invokevirtual equals : (Ljava/lang/Object;)Z
    //   96: ifeq -> 23
    //   99: aload #4
    //   101: invokeinterface remove : ()V
    //   106: aload_0
    //   107: aload_0
    //   108: getfield size : I
    //   111: aload #5
    //   113: invokestatic getBitmapBytes : (Landroid/graphics/Bitmap;)I
    //   116: isub
    //   117: putfield size : I
    //   120: iconst_1
    //   121: istore_2
    //   122: goto -> 23
    //   125: iload_2
    //   126: ifeq -> 137
    //   129: aload_0
    //   130: aload_0
    //   131: getfield maxSize : I
    //   134: invokespecial trimToSize : (I)V
    //   137: aload_0
    //   138: monitorexit
    //   139: return
    //   140: astore_1
    //   141: aload_0
    //   142: monitorexit
    //   143: aload_1
    //   144: athrow
    // Exception table:
    //   from	to	target	type
    //   4	23	140	finally
    //   23	78	140	finally
    //   84	120	140	finally
    //   129	137	140	finally
  }
  
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
  
  public Bitmap get(String paramString) {
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
    //   24: checkcast android/graphics/Bitmap
    //   27: astore_1
    //   28: aload_1
    //   29: ifnull -> 46
    //   32: aload_0
    //   33: aload_0
    //   34: getfield hitCount : I
    //   37: iconst_1
    //   38: iadd
    //   39: putfield hitCount : I
    //   42: aload_0
    //   43: monitorexit
    //   44: aload_1
    //   45: areturn
    //   46: aload_0
    //   47: aload_0
    //   48: getfield missCount : I
    //   51: iconst_1
    //   52: iadd
    //   53: putfield missCount : I
    //   56: aload_0
    //   57: monitorexit
    //   58: aconst_null
    //   59: astore_1
    //   60: goto -> 44
    //   63: astore_1
    //   64: aload_0
    //   65: monitorexit
    //   66: aload_1
    //   67: athrow
    // Exception table:
    //   from	to	target	type
    //   16	28	63	finally
    //   32	44	63	finally
    //   46	58	63	finally
    //   64	66	63	finally
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
  
  public void set(String paramString, Bitmap paramBitmap) {
    // Byte code:
    //   0: aload_1
    //   1: ifnull -> 8
    //   4: aload_2
    //   5: ifnonnull -> 18
    //   8: new java/lang/NullPointerException
    //   11: dup
    //   12: ldc 'key == null || bitmap == null'
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
    //   35: aload_2
    //   36: invokestatic getBitmapBytes : (Landroid/graphics/Bitmap;)I
    //   39: iadd
    //   40: putfield size : I
    //   43: aload_0
    //   44: getfield map : Ljava/util/LinkedHashMap;
    //   47: aload_1
    //   48: aload_2
    //   49: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   52: checkcast android/graphics/Bitmap
    //   55: astore_1
    //   56: aload_1
    //   57: ifnull -> 73
    //   60: aload_0
    //   61: aload_0
    //   62: getfield size : I
    //   65: aload_1
    //   66: invokestatic getBitmapBytes : (Landroid/graphics/Bitmap;)I
    //   69: isub
    //   70: putfield size : I
    //   73: aload_0
    //   74: monitorexit
    //   75: aload_0
    //   76: aload_0
    //   77: getfield maxSize : I
    //   80: invokespecial trimToSize : (I)V
    //   83: return
    //   84: astore_1
    //   85: aload_0
    //   86: monitorexit
    //   87: aload_1
    //   88: athrow
    // Exception table:
    //   from	to	target	type
    //   20	56	84	finally
    //   60	73	84	finally
    //   73	75	84	finally
    //   85	87	84	finally
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
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/squareup/picasso/LruCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */