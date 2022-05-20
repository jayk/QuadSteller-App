package android.support.design.widget;

import android.graphics.PorterDuff;
import android.os.Build;

class ViewUtils {
  static final ValueAnimatorCompat.Creator DEFAULT_ANIMATOR_CREATOR = new ValueAnimatorCompat.Creator() {
      public ValueAnimatorCompat createAnimator() {
        if (Build.VERSION.SDK_INT >= 12) {
          ValueAnimatorCompatImplHoneycombMr1 valueAnimatorCompatImplHoneycombMr1 = new ValueAnimatorCompatImplHoneycombMr1();
          return new ValueAnimatorCompat(valueAnimatorCompatImplHoneycombMr1);
        } 
        ValueAnimatorCompatImplGingerbread valueAnimatorCompatImplGingerbread = new ValueAnimatorCompatImplGingerbread();
        return new ValueAnimatorCompat(valueAnimatorCompatImplGingerbread);
      }
    };
  
  static ValueAnimatorCompat createAnimator() {
    return DEFAULT_ANIMATOR_CREATOR.createAnimator();
  }
  
  static boolean objectEquals(Object paramObject1, Object paramObject2) {
    return (paramObject1 == paramObject2 || (paramObject1 != null && paramObject1.equals(paramObject2)));
  }
  
  static PorterDuff.Mode parseTintMode(int paramInt, PorterDuff.Mode paramMode) {
    switch (paramInt) {
      default:
        return paramMode;
      case 3:
        paramMode = PorterDuff.Mode.SRC_OVER;
      case 5:
        paramMode = PorterDuff.Mode.SRC_IN;
      case 9:
        paramMode = PorterDuff.Mode.SRC_ATOP;
      case 14:
        paramMode = PorterDuff.Mode.MULTIPLY;
      case 15:
        break;
    } 
    paramMode = PorterDuff.Mode.SCREEN;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/ViewUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */