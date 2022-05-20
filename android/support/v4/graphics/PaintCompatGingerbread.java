package android.support.v4.graphics;

import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.util.Pair;

@RequiresApi(9)
class PaintCompatGingerbread {
  private static final String TOFU_STRING = "󟿽";
  
  private static final ThreadLocal<Pair<Rect, Rect>> sRectThreadLocal = new ThreadLocal<Pair<Rect, Rect>>();
  
  static boolean hasGlyph(@NonNull Paint paramPaint, @NonNull String paramString) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: aload_1
    //   3: invokevirtual length : ()I
    //   6: istore_3
    //   7: iload_3
    //   8: iconst_1
    //   9: if_icmpne -> 29
    //   12: aload_1
    //   13: iconst_0
    //   14: invokevirtual charAt : (I)C
    //   17: invokestatic isWhitespace : (C)Z
    //   20: ifeq -> 29
    //   23: iconst_1
    //   24: istore #4
    //   26: iload #4
    //   28: ireturn
    //   29: aload_0
    //   30: ldc '󟿽'
    //   32: invokevirtual measureText : (Ljava/lang/String;)F
    //   35: fstore #5
    //   37: aload_0
    //   38: aload_1
    //   39: invokevirtual measureText : (Ljava/lang/String;)F
    //   42: fstore #6
    //   44: iload_2
    //   45: istore #4
    //   47: fload #6
    //   49: fconst_0
    //   50: fcmpl
    //   51: ifeq -> 26
    //   54: aload_1
    //   55: iconst_0
    //   56: aload_1
    //   57: invokevirtual length : ()I
    //   60: invokevirtual codePointCount : (II)I
    //   63: iconst_1
    //   64: if_icmple -> 141
    //   67: iload_2
    //   68: istore #4
    //   70: fload #6
    //   72: fconst_2
    //   73: fload #5
    //   75: fmul
    //   76: fcmpl
    //   77: ifgt -> 26
    //   80: fconst_0
    //   81: fstore #7
    //   83: iconst_0
    //   84: istore #8
    //   86: iload #8
    //   88: iload_3
    //   89: if_icmpge -> 130
    //   92: aload_1
    //   93: iload #8
    //   95: invokevirtual codePointAt : (I)I
    //   98: invokestatic charCount : (I)I
    //   101: istore #9
    //   103: fload #7
    //   105: aload_0
    //   106: aload_1
    //   107: iload #8
    //   109: iload #8
    //   111: iload #9
    //   113: iadd
    //   114: invokevirtual measureText : (Ljava/lang/String;II)F
    //   117: fadd
    //   118: fstore #7
    //   120: iload #8
    //   122: iload #9
    //   124: iadd
    //   125: istore #8
    //   127: goto -> 86
    //   130: iload_2
    //   131: istore #4
    //   133: fload #6
    //   135: fload #7
    //   137: fcmpl
    //   138: ifge -> 26
    //   141: fload #6
    //   143: fload #5
    //   145: fcmpl
    //   146: ifeq -> 155
    //   149: iconst_1
    //   150: istore #4
    //   152: goto -> 26
    //   155: invokestatic obtainEmptyRects : ()Landroid/support/v4/util/Pair;
    //   158: astore #10
    //   160: aload_0
    //   161: ldc '󟿽'
    //   163: iconst_0
    //   164: ldc '󟿽'
    //   166: invokevirtual length : ()I
    //   169: aload #10
    //   171: getfield first : Ljava/lang/Object;
    //   174: checkcast android/graphics/Rect
    //   177: invokevirtual getTextBounds : (Ljava/lang/String;IILandroid/graphics/Rect;)V
    //   180: aload_0
    //   181: aload_1
    //   182: iconst_0
    //   183: iload_3
    //   184: aload #10
    //   186: getfield second : Ljava/lang/Object;
    //   189: checkcast android/graphics/Rect
    //   192: invokevirtual getTextBounds : (Ljava/lang/String;IILandroid/graphics/Rect;)V
    //   195: aload #10
    //   197: getfield first : Ljava/lang/Object;
    //   200: checkcast android/graphics/Rect
    //   203: aload #10
    //   205: getfield second : Ljava/lang/Object;
    //   208: invokevirtual equals : (Ljava/lang/Object;)Z
    //   211: ifne -> 220
    //   214: iconst_1
    //   215: istore #4
    //   217: goto -> 26
    //   220: iconst_0
    //   221: istore #4
    //   223: goto -> 217
  }
  
  private static Pair<Rect, Rect> obtainEmptyRects() {
    Pair<Rect, Rect> pair = sRectThreadLocal.get();
    if (pair == null) {
      pair = new Pair(new Rect(), new Rect());
      sRectThreadLocal.set(pair);
      return pair;
    } 
    ((Rect)pair.first).setEmpty();
    ((Rect)pair.second).setEmpty();
    return pair;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/graphics/PaintCompatGingerbread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */