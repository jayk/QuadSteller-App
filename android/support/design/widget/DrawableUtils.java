package android.support.design.widget;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import java.lang.reflect.Method;

class DrawableUtils {
  private static final String LOG_TAG = "DrawableUtils";
  
  private static Method sSetConstantStateMethod;
  
  private static boolean sSetConstantStateMethodFetched;
  
  static boolean setContainerConstantState(DrawableContainer paramDrawableContainer, Drawable.ConstantState paramConstantState) {
    return setContainerConstantStateV9(paramDrawableContainer, paramConstantState);
  }
  
  private static boolean setContainerConstantStateV9(DrawableContainer paramDrawableContainer, Drawable.ConstantState paramConstantState) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_2
    //   2: getstatic android/support/design/widget/DrawableUtils.sSetConstantStateMethodFetched : Z
    //   5: ifne -> 38
    //   8: ldc android/graphics/drawable/DrawableContainer
    //   10: ldc 'setConstantState'
    //   12: iconst_1
    //   13: anewarray java/lang/Class
    //   16: dup
    //   17: iconst_0
    //   18: ldc android/graphics/drawable/DrawableContainer$DrawableContainerState
    //   20: aastore
    //   21: invokevirtual getDeclaredMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   24: putstatic android/support/design/widget/DrawableUtils.sSetConstantStateMethod : Ljava/lang/reflect/Method;
    //   27: getstatic android/support/design/widget/DrawableUtils.sSetConstantStateMethod : Ljava/lang/reflect/Method;
    //   30: iconst_1
    //   31: invokevirtual setAccessible : (Z)V
    //   34: iconst_1
    //   35: putstatic android/support/design/widget/DrawableUtils.sSetConstantStateMethodFetched : Z
    //   38: getstatic android/support/design/widget/DrawableUtils.sSetConstantStateMethod : Ljava/lang/reflect/Method;
    //   41: ifnull -> 83
    //   44: getstatic android/support/design/widget/DrawableUtils.sSetConstantStateMethod : Ljava/lang/reflect/Method;
    //   47: aload_0
    //   48: iconst_1
    //   49: anewarray java/lang/Object
    //   52: dup
    //   53: iconst_0
    //   54: aload_1
    //   55: aastore
    //   56: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   59: pop
    //   60: iload_2
    //   61: ireturn
    //   62: astore_3
    //   63: ldc 'DrawableUtils'
    //   65: ldc 'Could not fetch setConstantState(). Oh well.'
    //   67: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   70: pop
    //   71: goto -> 34
    //   74: astore_0
    //   75: ldc 'DrawableUtils'
    //   77: ldc 'Could not invoke setConstantState(). Oh well.'
    //   79: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   82: pop
    //   83: iconst_0
    //   84: istore_2
    //   85: goto -> 60
    // Exception table:
    //   from	to	target	type
    //   8	34	62	java/lang/NoSuchMethodException
    //   44	60	74	java/lang/Exception
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/DrawableUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */