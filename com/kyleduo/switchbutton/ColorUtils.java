package com.kyleduo.switchbutton;

import android.content.res.ColorStateList;

public class ColorUtils {
  private static final int CHECKED_ATTR = 16842912;
  
  private static final int ENABLE_ATTR = 16842910;
  
  private static final int PRESSED_ATTR = 16842919;
  
  public static ColorStateList generateBackColorWithTintColor(int paramInt) {
    return new ColorStateList(new int[][] { { -16842910, 16842912 }, , { -16842910 }, , { 16842912, 16842919 }, , { -16842912, 16842919 }, , { 16842912 }, , { -16842912 },  }, new int[] { paramInt + 520093696, 268435456, paramInt + 805306368, 536870912, paramInt + 805306368, 536870912 });
  }
  
  public static ColorStateList generateThumbColorWithTintColor(int paramInt) {
    int[] arrayOfInt1 = { 16842919, -16842912 };
    int[] arrayOfInt2 = { 16842919, 16842912 };
    int[] arrayOfInt3 = { 16842912 };
    int[] arrayOfInt4 = { -16842912 };
    return new ColorStateList(new int[][] { { -16842910, 16842912 }, , { -16842910 }, , arrayOfInt1, arrayOfInt2, arrayOfInt3, arrayOfInt4 }, new int[] { paramInt + 1442840576, -4539718, paramInt + 1728053248, paramInt + 1728053248, 0xFF000000 | paramInt, -1118482 });
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/kyleduo/switchbutton/ColorUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */