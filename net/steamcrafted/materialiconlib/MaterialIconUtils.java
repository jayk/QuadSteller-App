package net.steamcrafted.materialiconlib;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;

public class MaterialIconUtils {
  private static final String mFontPath = "materialdesignicons-webfont.ttf";
  
  private static Typeface materialFont;
  
  private static Typeface materialFontEdit;
  
  static int convertDpToPx(Context paramContext, float paramFloat) {
    return (int)TypedValue.applyDimension(1, paramFloat, paramContext.getResources().getDisplayMetrics());
  }
  
  public static String getIconString(int paramInt) {
    return new String(Character.toChars(61697 + paramInt));
  }
  
  static Typeface getTypeFace(Context paramContext) {
    if (materialFont == null)
      materialFont = Typeface.createFromAsset(paramContext.getAssets(), "materialdesignicons-webfont.ttf"); 
    return materialFont;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/net/steamcrafted/materialiconlib/MaterialIconUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */