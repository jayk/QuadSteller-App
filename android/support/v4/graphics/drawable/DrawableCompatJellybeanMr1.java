package android.support.v4.graphics.drawable;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import java.lang.reflect.Method;

@TargetApi(17)
@RequiresApi(17)
class DrawableCompatJellybeanMr1 {
  private static final String TAG = "DrawableCompatJellybeanMr1";
  
  private static Method sGetLayoutDirectionMethod;
  
  private static boolean sGetLayoutDirectionMethodFetched;
  
  private static Method sSetLayoutDirectionMethod;
  
  private static boolean sSetLayoutDirectionMethodFetched;
  
  public static int getLayoutDirection(Drawable paramDrawable) {
    // Byte code:
    //   0: getstatic android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sGetLayoutDirectionMethodFetched : Z
    //   3: ifne -> 31
    //   6: ldc android/graphics/drawable/Drawable
    //   8: ldc 'getLayoutDirection'
    //   10: iconst_0
    //   11: anewarray java/lang/Class
    //   14: invokevirtual getDeclaredMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   17: putstatic android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod : Ljava/lang/reflect/Method;
    //   20: getstatic android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod : Ljava/lang/reflect/Method;
    //   23: iconst_1
    //   24: invokevirtual setAccessible : (Z)V
    //   27: iconst_1
    //   28: putstatic android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sGetLayoutDirectionMethodFetched : Z
    //   31: getstatic android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod : Ljava/lang/reflect/Method;
    //   34: ifnull -> 84
    //   37: getstatic android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod : Ljava/lang/reflect/Method;
    //   40: aload_0
    //   41: iconst_0
    //   42: anewarray java/lang/Object
    //   45: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   48: checkcast java/lang/Integer
    //   51: invokevirtual intValue : ()I
    //   54: istore_1
    //   55: iload_1
    //   56: ireturn
    //   57: astore_2
    //   58: ldc 'DrawableCompatJellybeanMr1'
    //   60: ldc 'Failed to retrieve getLayoutDirection() method'
    //   62: aload_2
    //   63: invokestatic i : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   66: pop
    //   67: goto -> 27
    //   70: astore_0
    //   71: ldc 'DrawableCompatJellybeanMr1'
    //   73: ldc 'Failed to invoke getLayoutDirection() via reflection'
    //   75: aload_0
    //   76: invokestatic i : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   79: pop
    //   80: aconst_null
    //   81: putstatic android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod : Ljava/lang/reflect/Method;
    //   84: iconst_m1
    //   85: istore_1
    //   86: goto -> 55
    // Exception table:
    //   from	to	target	type
    //   6	27	57	java/lang/NoSuchMethodException
    //   37	55	70	java/lang/Exception
  }
  
  public static boolean setLayoutDirection(Drawable paramDrawable, int paramInt) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_2
    //   2: getstatic android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sSetLayoutDirectionMethodFetched : Z
    //   5: ifne -> 39
    //   8: ldc android/graphics/drawable/Drawable
    //   10: ldc 'setLayoutDirection'
    //   12: iconst_1
    //   13: anewarray java/lang/Class
    //   16: dup
    //   17: iconst_0
    //   18: getstatic java/lang/Integer.TYPE : Ljava/lang/Class;
    //   21: aastore
    //   22: invokevirtual getDeclaredMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   25: putstatic android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod : Ljava/lang/reflect/Method;
    //   28: getstatic android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod : Ljava/lang/reflect/Method;
    //   31: iconst_1
    //   32: invokevirtual setAccessible : (Z)V
    //   35: iconst_1
    //   36: putstatic android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sSetLayoutDirectionMethodFetched : Z
    //   39: getstatic android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod : Ljava/lang/reflect/Method;
    //   42: ifnull -> 93
    //   45: getstatic android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod : Ljava/lang/reflect/Method;
    //   48: aload_0
    //   49: iconst_1
    //   50: anewarray java/lang/Object
    //   53: dup
    //   54: iconst_0
    //   55: iload_1
    //   56: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   59: aastore
    //   60: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   63: pop
    //   64: iload_2
    //   65: ireturn
    //   66: astore_3
    //   67: ldc 'DrawableCompatJellybeanMr1'
    //   69: ldc 'Failed to retrieve setLayoutDirection(int) method'
    //   71: aload_3
    //   72: invokestatic i : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   75: pop
    //   76: goto -> 35
    //   79: astore_0
    //   80: ldc 'DrawableCompatJellybeanMr1'
    //   82: ldc 'Failed to invoke setLayoutDirection(int) via reflection'
    //   84: aload_0
    //   85: invokestatic i : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   88: pop
    //   89: aconst_null
    //   90: putstatic android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod : Ljava/lang/reflect/Method;
    //   93: iconst_0
    //   94: istore_2
    //   95: goto -> 64
    // Exception table:
    //   from	to	target	type
    //   8	35	66	java/lang/NoSuchMethodException
    //   45	64	79	java/lang/Exception
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */