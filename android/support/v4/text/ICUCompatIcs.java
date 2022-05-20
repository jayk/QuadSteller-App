package android.support.v4.text;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

@TargetApi(14)
@RequiresApi(14)
class ICUCompatIcs {
  private static final String TAG = "ICUCompatIcs";
  
  private static Method sAddLikelySubtagsMethod;
  
  private static Method sGetScriptMethod;
  
  static {
    try {
      Class<?> clazz = Class.forName("libcore.icu.ICU");
      if (clazz != null) {
        sGetScriptMethod = clazz.getMethod("getScript", new Class[] { String.class });
        sAddLikelySubtagsMethod = clazz.getMethod("addLikelySubtags", new Class[] { String.class });
      } 
    } catch (Exception exception) {
      sGetScriptMethod = null;
      sAddLikelySubtagsMethod = null;
      Log.w("ICUCompatIcs", exception);
    } 
  }
  
  private static String addLikelySubtags(Locale paramLocale) {
    String str = paramLocale.toString();
    try {
      if (sAddLikelySubtagsMethod != null) {
        String str1 = (String)sAddLikelySubtagsMethod.invoke(null, new Object[] { str });
        str = str1;
      } 
    } catch (IllegalAccessException illegalAccessException) {
      Log.w("ICUCompatIcs", illegalAccessException);
    } catch (InvocationTargetException invocationTargetException) {
      Log.w("ICUCompatIcs", invocationTargetException);
    } 
    return str;
  }
  
  private static String getScript(String paramString) {
    try {
      if (sGetScriptMethod != null)
        return (String)sGetScriptMethod.invoke(null, new Object[] { paramString }); 
    } catch (IllegalAccessException illegalAccessException) {
      Log.w("ICUCompatIcs", illegalAccessException);
    } catch (InvocationTargetException invocationTargetException) {
      Log.w("ICUCompatIcs", invocationTargetException);
    } 
    return null;
  }
  
  public static String maximizeAndGetScript(Locale paramLocale) {
    null = addLikelySubtags(paramLocale);
    return (null != null) ? getScript(null) : null;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/text/ICUCompatIcs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */