package app.gamer.quadstellar.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {
  public static int getData(Context paramContext, String paramString, int paramInt) {
    return paramContext.getSharedPreferences(paramString, 0).getInt(paramString, paramInt);
  }
  
  public static String getData(Context paramContext, String paramString) {
    return paramContext.getSharedPreferences(paramString, 0).getString(paramString, null);
  }
  
  public static boolean getData(Context paramContext, String paramString, boolean paramBoolean) {
    return paramContext.getSharedPreferences(paramString, 0).getBoolean(paramString, paramBoolean);
  }
  
  public static void putData(Context paramContext, String paramString, int paramInt) {
    SharedPreferences.Editor editor = paramContext.getSharedPreferences(paramString, 0).edit();
    editor.putInt(paramString, paramInt);
    editor.commit();
  }
  
  public static void putData(Context paramContext, String paramString1, String paramString2) {
    SharedPreferences.Editor editor = paramContext.getSharedPreferences(paramString1, 0).edit();
    editor.putString(paramString1, paramString2);
    editor.commit();
  }
  
  public static void putData(Context paramContext, String paramString, boolean paramBoolean) {
    SharedPreferences.Editor editor = paramContext.getSharedPreferences(paramString, 0).edit();
    editor.putBoolean(paramString, paramBoolean);
    editor.commit();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/utils/SharedPreferencesUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */