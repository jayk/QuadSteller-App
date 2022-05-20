package android.support.v4.content.res;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AnyRes;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleableRes;
import android.util.TypedValue;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class TypedArrayUtils {
  public static int getAttr(Context paramContext, int paramInt1, int paramInt2) {
    TypedValue typedValue = new TypedValue();
    paramContext.getTheme().resolveAttribute(paramInt1, typedValue, true);
    if (typedValue.resourceId == 0)
      paramInt1 = paramInt2; 
    return paramInt1;
  }
  
  public static boolean getBoolean(TypedArray paramTypedArray, @StyleableRes int paramInt1, @StyleableRes int paramInt2, boolean paramBoolean) {
    return paramTypedArray.getBoolean(paramInt1, paramTypedArray.getBoolean(paramInt2, paramBoolean));
  }
  
  public static Drawable getDrawable(TypedArray paramTypedArray, @StyleableRes int paramInt1, @StyleableRes int paramInt2) {
    Drawable drawable1 = paramTypedArray.getDrawable(paramInt1);
    Drawable drawable2 = drawable1;
    if (drawable1 == null)
      drawable2 = paramTypedArray.getDrawable(paramInt2); 
    return drawable2;
  }
  
  public static int getInt(TypedArray paramTypedArray, @StyleableRes int paramInt1, @StyleableRes int paramInt2, int paramInt3) {
    return paramTypedArray.getInt(paramInt1, paramTypedArray.getInt(paramInt2, paramInt3));
  }
  
  @AnyRes
  public static int getResourceId(TypedArray paramTypedArray, @StyleableRes int paramInt1, @StyleableRes int paramInt2, @AnyRes int paramInt3) {
    return paramTypedArray.getResourceId(paramInt1, paramTypedArray.getResourceId(paramInt2, paramInt3));
  }
  
  public static String getString(TypedArray paramTypedArray, @StyleableRes int paramInt1, @StyleableRes int paramInt2) {
    String str1 = paramTypedArray.getString(paramInt1);
    String str2 = str1;
    if (str1 == null)
      str2 = paramTypedArray.getString(paramInt2); 
    return str2;
  }
  
  public static CharSequence getText(TypedArray paramTypedArray, @StyleableRes int paramInt1, @StyleableRes int paramInt2) {
    CharSequence charSequence1 = paramTypedArray.getText(paramInt1);
    CharSequence charSequence2 = charSequence1;
    if (charSequence1 == null)
      charSequence2 = paramTypedArray.getText(paramInt2); 
    return charSequence2;
  }
  
  public static CharSequence[] getTextArray(TypedArray paramTypedArray, @StyleableRes int paramInt1, @StyleableRes int paramInt2) {
    CharSequence[] arrayOfCharSequence1 = paramTypedArray.getTextArray(paramInt1);
    CharSequence[] arrayOfCharSequence2 = arrayOfCharSequence1;
    if (arrayOfCharSequence1 == null)
      arrayOfCharSequence2 = paramTypedArray.getTextArray(paramInt2); 
    return arrayOfCharSequence2;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/content/res/TypedArrayUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */