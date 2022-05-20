package android.support.v7.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.Xml;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

final class AppCompatColorStateListInflater {
  private static final int DEFAULT_COLOR = -65536;
  
  @NonNull
  public static ColorStateList createFromXml(@NonNull Resources paramResources, @NonNull XmlPullParser paramXmlPullParser, @Nullable Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    int i;
    AttributeSet attributeSet = Xml.asAttributeSet(paramXmlPullParser);
    do {
      i = paramXmlPullParser.next();
    } while (i != 2 && i != 1);
    if (i != 2)
      throw new XmlPullParserException("No start tag found"); 
    return createFromXmlInner(paramResources, paramXmlPullParser, attributeSet, paramTheme);
  }
  
  @NonNull
  private static ColorStateList createFromXmlInner(@NonNull Resources paramResources, @NonNull XmlPullParser paramXmlPullParser, @NonNull AttributeSet paramAttributeSet, @Nullable Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    String str = paramXmlPullParser.getName();
    if (!str.equals("selector"))
      throw new XmlPullParserException(paramXmlPullParser.getPositionDescription() + ": invalid color state list tag " + str); 
    return inflate(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
  }
  
  private static ColorStateList inflate(@NonNull Resources paramResources, @NonNull XmlPullParser paramXmlPullParser, @NonNull AttributeSet paramAttributeSet, @Nullable Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    int i = paramXmlPullParser.getDepth() + 1;
    int[][] arrayOfInt3 = new int[20][];
    int[] arrayOfInt4 = new int[arrayOfInt3.length];
    byte b = 0;
    while (true) {
      int j = paramXmlPullParser.next();
      if (j != 1) {
        int k = paramXmlPullParser.getDepth();
        if (k >= i || j != 3) {
          if (j == 2 && k <= i && paramXmlPullParser.getName().equals("item")) {
            TypedArray typedArray = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.ColorStateListItem);
            int m = typedArray.getColor(R.styleable.ColorStateListItem_android_color, -65281);
            float f = 1.0F;
            if (typedArray.hasValue(R.styleable.ColorStateListItem_android_alpha)) {
              f = typedArray.getFloat(R.styleable.ColorStateListItem_android_alpha, 1.0F);
            } else if (typedArray.hasValue(R.styleable.ColorStateListItem_alpha)) {
              f = typedArray.getFloat(R.styleable.ColorStateListItem_alpha, 1.0F);
            } 
            typedArray.recycle();
            int n = paramAttributeSet.getAttributeCount();
            int[] arrayOfInt = new int[n];
            j = 0;
            k = 0;
            while (j < n) {
              int i1 = paramAttributeSet.getAttributeNameResource(j);
              if (i1 != 16843173 && i1 != 16843551 && i1 != R.attr.alpha) {
                int i2 = k + 1;
                if (!paramAttributeSet.getAttributeBooleanValue(j, false))
                  i1 = -i1; 
                arrayOfInt[k] = i1;
                k = i2;
              } 
              j++;
            } 
            arrayOfInt = StateSet.trimStateSet(arrayOfInt, k);
            k = modulateColorAlpha(m, f);
            if (!b || arrayOfInt.length == 0);
            arrayOfInt4 = GrowingArrayUtils.append(arrayOfInt4, b, k);
            arrayOfInt3 = GrowingArrayUtils.<int[]>append(arrayOfInt3, b, arrayOfInt);
            b++;
          } 
          continue;
        } 
      } 
      break;
    } 
    int[] arrayOfInt1 = new int[b];
    int[][] arrayOfInt2 = new int[b][];
    System.arraycopy(arrayOfInt4, 0, arrayOfInt1, 0, b);
    System.arraycopy(arrayOfInt3, 0, arrayOfInt2, 0, b);
    return new ColorStateList(arrayOfInt2, arrayOfInt1);
  }
  
  private static int modulateColorAlpha(int paramInt, float paramFloat) {
    return ColorUtils.setAlphaComponent(paramInt, Math.round(Color.alpha(paramInt) * paramFloat));
  }
  
  private static TypedArray obtainAttributes(Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, int[] paramArrayOfint) {
    return (paramTheme == null) ? paramResources.obtainAttributes(paramAttributeSet, paramArrayOfint) : paramTheme.obtainStyledAttributes(paramAttributeSet, paramArrayOfint, 0, 0);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/content/res/AppCompatColorStateListInflater.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */