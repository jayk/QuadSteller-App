package android.support.v7.widget;

import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import java.lang.reflect.Field;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class DrawableUtils {
  public static final Rect INSETS_NONE = new Rect();
  
  private static final String TAG = "DrawableUtils";
  
  private static final String VECTOR_DRAWABLE_CLAZZ_NAME = "android.graphics.drawable.VectorDrawable";
  
  private static Class<?> sInsetsClazz;
  
  static {
    if (Build.VERSION.SDK_INT >= 18)
      try {
        sInsetsClazz = Class.forName("android.graphics.Insets");
      } catch (ClassNotFoundException classNotFoundException) {} 
  }
  
  public static boolean canSafelyMutateDrawable(@NonNull Drawable paramDrawable) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: getstatic android/os/Build$VERSION.SDK_INT : I
    //   5: bipush #15
    //   7: if_icmpge -> 21
    //   10: aload_0
    //   11: instanceof android/graphics/drawable/InsetDrawable
    //   14: ifeq -> 21
    //   17: iload_1
    //   18: istore_2
    //   19: iload_2
    //   20: ireturn
    //   21: getstatic android/os/Build$VERSION.SDK_INT : I
    //   24: bipush #15
    //   26: if_icmpge -> 38
    //   29: iload_1
    //   30: istore_2
    //   31: aload_0
    //   32: instanceof android/graphics/drawable/GradientDrawable
    //   35: ifne -> 19
    //   38: getstatic android/os/Build$VERSION.SDK_INT : I
    //   41: bipush #17
    //   43: if_icmpge -> 55
    //   46: iload_1
    //   47: istore_2
    //   48: aload_0
    //   49: instanceof android/graphics/drawable/LayerDrawable
    //   52: ifne -> 19
    //   55: aload_0
    //   56: instanceof android/graphics/drawable/DrawableContainer
    //   59: ifeq -> 112
    //   62: aload_0
    //   63: invokevirtual getConstantState : ()Landroid/graphics/drawable/Drawable$ConstantState;
    //   66: astore_0
    //   67: aload_0
    //   68: instanceof android/graphics/drawable/DrawableContainer$DrawableContainerState
    //   71: ifeq -> 177
    //   74: aload_0
    //   75: checkcast android/graphics/drawable/DrawableContainer$DrawableContainerState
    //   78: invokevirtual getChildren : ()[Landroid/graphics/drawable/Drawable;
    //   81: astore_0
    //   82: aload_0
    //   83: arraylength
    //   84: istore_3
    //   85: iconst_0
    //   86: istore #4
    //   88: iload #4
    //   90: iload_3
    //   91: if_icmpge -> 177
    //   94: iload_1
    //   95: istore_2
    //   96: aload_0
    //   97: iload #4
    //   99: aaload
    //   100: invokestatic canSafelyMutateDrawable : (Landroid/graphics/drawable/Drawable;)Z
    //   103: ifeq -> 19
    //   106: iinc #4, 1
    //   109: goto -> 88
    //   112: aload_0
    //   113: instanceof android/support/v4/graphics/drawable/DrawableWrapper
    //   116: ifeq -> 135
    //   119: aload_0
    //   120: checkcast android/support/v4/graphics/drawable/DrawableWrapper
    //   123: invokeinterface getWrappedDrawable : ()Landroid/graphics/drawable/Drawable;
    //   128: invokestatic canSafelyMutateDrawable : (Landroid/graphics/drawable/Drawable;)Z
    //   131: istore_2
    //   132: goto -> 19
    //   135: aload_0
    //   136: instanceof android/support/v7/graphics/drawable/DrawableWrapper
    //   139: ifeq -> 156
    //   142: aload_0
    //   143: checkcast android/support/v7/graphics/drawable/DrawableWrapper
    //   146: invokevirtual getWrappedDrawable : ()Landroid/graphics/drawable/Drawable;
    //   149: invokestatic canSafelyMutateDrawable : (Landroid/graphics/drawable/Drawable;)Z
    //   152: istore_2
    //   153: goto -> 19
    //   156: aload_0
    //   157: instanceof android/graphics/drawable/ScaleDrawable
    //   160: ifeq -> 177
    //   163: aload_0
    //   164: checkcast android/graphics/drawable/ScaleDrawable
    //   167: invokevirtual getDrawable : ()Landroid/graphics/drawable/Drawable;
    //   170: invokestatic canSafelyMutateDrawable : (Landroid/graphics/drawable/Drawable;)Z
    //   173: istore_2
    //   174: goto -> 19
    //   177: iconst_1
    //   178: istore_2
    //   179: goto -> 19
  }
  
  static void fixDrawable(@NonNull Drawable paramDrawable) {
    if (Build.VERSION.SDK_INT == 21 && "android.graphics.drawable.VectorDrawable".equals(paramDrawable.getClass().getName()))
      fixVectorDrawableTinting(paramDrawable); 
  }
  
  private static void fixVectorDrawableTinting(Drawable paramDrawable) {
    int[] arrayOfInt = paramDrawable.getState();
    if (arrayOfInt == null || arrayOfInt.length == 0) {
      paramDrawable.setState(ThemeUtils.CHECKED_STATE_SET);
    } else {
      paramDrawable.setState(ThemeUtils.EMPTY_STATE_SET);
    } 
    paramDrawable.setState(arrayOfInt);
  }
  
  public static Rect getOpticalBounds(Drawable paramDrawable) {
    if (sInsetsClazz != null)
      try {
        paramDrawable = DrawableCompat.unwrap(paramDrawable);
        Object object = paramDrawable.getClass().getMethod("getOpticalInsets", new Class[0]).invoke(paramDrawable, new Object[0]);
        if (object != null) {
          Rect rect = new Rect();
          this();
          Field[] arrayOfField = sInsetsClazz.getFields();
          int i = arrayOfField.length;
          byte b = 0;
          while (true) {
            Field field;
            Rect rect1 = rect;
            if (b < i) {
              field = arrayOfField[b];
              String str = field.getName();
              byte b1 = -1;
              switch (str.hashCode()) {
                default:
                  switch (b1) {
                    case 0:
                      rect.left = field.getInt(object);
                      b++;
                      break;
                    case 1:
                      rect.top = field.getInt(object);
                      b++;
                      break;
                    case 2:
                      rect.right = field.getInt(object);
                      b++;
                      break;
                    case 3:
                      break;
                  } 
                  continue;
                case 3317767:
                  if (str.equals("left"))
                    b1 = 0; 
                case 115029:
                  if (str.equals("top"))
                    b1 = 1; 
                case 108511772:
                  if (str.equals("right"))
                    b1 = 2; 
                case -1383228885:
                  if (str.equals("bottom"))
                    b1 = 3; 
              } 
            } else {
              return (Rect)field;
            } 
            rect.bottom = field.getInt(object);
            b++;
            break;
          } 
        } 
      } catch (Exception exception) {
        Log.e("DrawableUtils", "Couldn't obtain the optical insets. Ignoring.");
      }  
    return INSETS_NONE;
  }
  
  static PorterDuff.Mode parseTintMode(int paramInt, PorterDuff.Mode paramMode) {
    PorterDuff.Mode mode = paramMode;
    switch (paramInt) {
      default:
        mode = paramMode;
      case 4:
      case 6:
      case 7:
      case 8:
      case 10:
      case 11:
      case 12:
      case 13:
        return mode;
      case 3:
        mode = PorterDuff.Mode.SRC_OVER;
      case 5:
        mode = PorterDuff.Mode.SRC_IN;
      case 9:
        mode = PorterDuff.Mode.SRC_ATOP;
      case 14:
        mode = PorterDuff.Mode.MULTIPLY;
      case 15:
        mode = PorterDuff.Mode.SCREEN;
      case 16:
        break;
    } 
    mode = paramMode;
    if (Build.VERSION.SDK_INT >= 11)
      mode = PorterDuff.Mode.valueOf("ADD"); 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/DrawableUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */