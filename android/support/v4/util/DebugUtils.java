package android.support.v4.util;

import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class DebugUtils {
  public static void buildShortClassTag(Object paramObject, StringBuilder paramStringBuilder) {
    // Byte code:
    //   0: aload_0
    //   1: ifnonnull -> 12
    //   4: aload_1
    //   5: ldc 'null'
    //   7: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10: pop
    //   11: return
    //   12: aload_0
    //   13: invokevirtual getClass : ()Ljava/lang/Class;
    //   16: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   19: astore_2
    //   20: aload_2
    //   21: ifnull -> 33
    //   24: aload_2
    //   25: astore_3
    //   26: aload_2
    //   27: invokevirtual length : ()I
    //   30: ifgt -> 65
    //   33: aload_0
    //   34: invokevirtual getClass : ()Ljava/lang/Class;
    //   37: invokevirtual getName : ()Ljava/lang/String;
    //   40: astore_2
    //   41: aload_2
    //   42: bipush #46
    //   44: invokevirtual lastIndexOf : (I)I
    //   47: istore #4
    //   49: aload_2
    //   50: astore_3
    //   51: iload #4
    //   53: ifle -> 65
    //   56: aload_2
    //   57: iload #4
    //   59: iconst_1
    //   60: iadd
    //   61: invokevirtual substring : (I)Ljava/lang/String;
    //   64: astore_3
    //   65: aload_1
    //   66: aload_3
    //   67: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   70: pop
    //   71: aload_1
    //   72: bipush #123
    //   74: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   77: pop
    //   78: aload_1
    //   79: aload_0
    //   80: invokestatic identityHashCode : (Ljava/lang/Object;)I
    //   83: invokestatic toHexString : (I)Ljava/lang/String;
    //   86: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   89: pop
    //   90: goto -> 11
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/util/DebugUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */