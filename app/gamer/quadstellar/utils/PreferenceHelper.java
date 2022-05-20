package app.gamer.quadstellar.utils;

import android.content.Context;
import android.content.SharedPreferences;
import app.gamer.quadstellar.App;
import com.google.gson.Gson;

public class PreferenceHelper {
  public static void clean() {
    SharedPreferences.Editor editor = getSharedPreferences().edit();
    editor.clear();
    editor.commit();
  }
  
  public static SharedPreferences getLoginSharedPreferences() {
    return App.getAppContext().getSharedPreferences("login_prefernce", 0);
  }
  
  public static SharedPreferences getSharedPreferences() {
    return App.getAppContext().getSharedPreferences("ehome_prefernce", 0);
  }
  
  public static void loginWrite(String paramString, int paramInt) {
    SharedPreferences.Editor editor = getLoginSharedPreferences().edit();
    editor.putInt(paramString, paramInt);
    editor.commit();
  }
  
  public static void loginWrite(String paramString1, String paramString2) {
    SharedPreferences.Editor editor = getLoginSharedPreferences().edit();
    editor.putString(paramString1, paramString2);
    editor.commit();
  }
  
  public static boolean readBoolean(String paramString) {
    return getSharedPreferences().getBoolean(paramString, false);
  }
  
  public static boolean readBoolean(String paramString, boolean paramBoolean) {
    return getSharedPreferences().getBoolean(paramString, paramBoolean);
  }
  
  public static int readInt(String paramString) {
    return getSharedPreferences().getInt(paramString, 0);
  }
  
  public static int readInt(String paramString, int paramInt) {
    return getSharedPreferences().getInt(paramString, paramInt);
  }
  
  public static int readLoginInt(String paramString) {
    return getLoginSharedPreferences().getInt(paramString, 0);
  }
  
  public static int readLoginInt(String paramString, int paramInt) {
    return getLoginSharedPreferences().getInt(paramString, paramInt);
  }
  
  public static String readLoginString(String paramString) {
    return getLoginSharedPreferences().getString(paramString, null);
  }
  
  public static String readLoginString(String paramString1, String paramString2) {
    return getLoginSharedPreferences().getString(paramString1, paramString2);
  }
  
  public static Long readLong(String paramString) {
    return Long.valueOf(getSharedPreferences().getLong(paramString, 0L));
  }
  
  public static Long readLong(String paramString, long paramLong) {
    return Long.valueOf(getSharedPreferences().getLong(paramString, paramLong));
  }
  
  public static Long readLong(String paramString1, String paramString2) {
    return Long.valueOf(getSharedPreferences().getLong(paramString2, 0L));
  }
  
  public static <T> T readString(Context paramContext, Class<T> paramClass, String paramString) {
    String str = getSharedPreferences().getString(paramString, null);
    return (T)(new Gson()).fromJson(str, paramClass);
  }
  
  public static String readString(String paramString) {
    return getSharedPreferences().getString(paramString, null);
  }
  
  public static String readString(String paramString1, String paramString2) {
    return getSharedPreferences().getString(paramString1, paramString2);
  }
  
  public static void remove(String paramString) {
    SharedPreferences.Editor editor = getSharedPreferences().edit();
    editor.remove(paramString);
    editor.commit();
  }
  
  public static void write(String paramString, int paramInt) {
    SharedPreferences.Editor editor = getSharedPreferences().edit();
    editor.putInt(paramString, paramInt);
    editor.commit();
  }
  
  public static void write(String paramString, long paramLong) {
    SharedPreferences.Editor editor = getSharedPreferences().edit();
    editor.putLong(paramString, paramLong);
    editor.commit();
  }
  
  public static void write(String paramString1, String paramString2) {
    SharedPreferences.Editor editor = getSharedPreferences().edit();
    editor.putString(paramString1, paramString2);
    editor.commit();
  }
  
  public static void write(String paramString1, String paramString2, long paramLong) {
    SharedPreferences.Editor editor = getSharedPreferences().edit();
    editor.putLong(paramString2, paramLong);
    editor.commit();
  }
  
  public static void write(String paramString, boolean paramBoolean) {
    SharedPreferences.Editor editor = getSharedPreferences().edit();
    editor.putBoolean(paramString, paramBoolean);
    editor.commit();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/utils/PreferenceHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */