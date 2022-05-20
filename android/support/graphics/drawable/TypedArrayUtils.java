package android.support.graphics.drawable;

import android.content.res.TypedArray;
import org.xmlpull.v1.XmlPullParser;

class TypedArrayUtils {
  private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";
  
  public static boolean getNamedBoolean(TypedArray paramTypedArray, XmlPullParser paramXmlPullParser, String paramString, int paramInt, boolean paramBoolean) {
    if (hasAttribute(paramXmlPullParser, paramString))
      paramBoolean = paramTypedArray.getBoolean(paramInt, paramBoolean); 
    return paramBoolean;
  }
  
  public static int getNamedColor(TypedArray paramTypedArray, XmlPullParser paramXmlPullParser, String paramString, int paramInt1, int paramInt2) {
    if (hasAttribute(paramXmlPullParser, paramString))
      paramInt2 = paramTypedArray.getColor(paramInt1, paramInt2); 
    return paramInt2;
  }
  
  public static float getNamedFloat(TypedArray paramTypedArray, XmlPullParser paramXmlPullParser, String paramString, int paramInt, float paramFloat) {
    if (hasAttribute(paramXmlPullParser, paramString))
      paramFloat = paramTypedArray.getFloat(paramInt, paramFloat); 
    return paramFloat;
  }
  
  public static int getNamedInt(TypedArray paramTypedArray, XmlPullParser paramXmlPullParser, String paramString, int paramInt1, int paramInt2) {
    if (hasAttribute(paramXmlPullParser, paramString))
      paramInt2 = paramTypedArray.getInt(paramInt1, paramInt2); 
    return paramInt2;
  }
  
  public static boolean hasAttribute(XmlPullParser paramXmlPullParser, String paramString) {
    return (paramXmlPullParser.getAttributeValue("http://schemas.android.com/apk/res/android", paramString) != null);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/graphics/drawable/TypedArrayUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */