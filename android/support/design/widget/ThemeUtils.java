package android.support.design.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.appcompat.R;

class ThemeUtils {
  private static final int[] APPCOMPAT_CHECK_ATTRS = new int[] { R.attr.colorPrimary };
  
  static void checkAppCompatTheme(Context paramContext) {
    boolean bool = false;
    TypedArray typedArray = paramContext.obtainStyledAttributes(APPCOMPAT_CHECK_ATTRS);
    if (!typedArray.hasValue(0))
      bool = true; 
    if (typedArray != null)
      typedArray.recycle(); 
    if (bool)
      throw new IllegalArgumentException("You need to use a Theme.AppCompat theme (or descendant) with the design library."); 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/ThemeUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */