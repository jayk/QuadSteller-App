package com.blankj.utilcode.util;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@SuppressLint({"ApplySharedPref"})
public final class SPUtils {
  private static SimpleArrayMap<String, SPUtils> SP_UTILS_MAP = new SimpleArrayMap();
  
  private SharedPreferences sp;
  
  private SPUtils(String paramString) {
    this.sp = Utils.getApp().getSharedPreferences(paramString, 0);
  }
  
  public static SPUtils getInstance() {
    return getInstance("");
  }
  
  public static SPUtils getInstance(String paramString) {
    String str = paramString;
    if (isSpace(paramString))
      str = "spUtils"; 
    SPUtils sPUtils2 = (SPUtils)SP_UTILS_MAP.get(str);
    SPUtils sPUtils1 = sPUtils2;
    if (sPUtils2 == null) {
      sPUtils1 = new SPUtils(str);
      SP_UTILS_MAP.put(str, sPUtils1);
    } 
    return sPUtils1;
  }
  
  private static boolean isSpace(String paramString) {
    boolean bool = true;
    if (paramString == null)
      return bool; 
    byte b = 0;
    int i = paramString.length();
    while (true) {
      boolean bool1 = bool;
      if (b < i) {
        if (!Character.isWhitespace(paramString.charAt(b)))
          return false; 
        b++;
        continue;
      } 
      return bool1;
    } 
  }
  
  public void clear() {
    clear(false);
  }
  
  public void clear(boolean paramBoolean) {
    if (paramBoolean) {
      this.sp.edit().clear().commit();
      return;
    } 
    this.sp.edit().clear().apply();
  }
  
  public boolean contains(@NonNull String paramString) {
    return this.sp.contains(paramString);
  }
  
  public Map<String, ?> getAll() {
    return this.sp.getAll();
  }
  
  public boolean getBoolean(@NonNull String paramString) {
    return getBoolean(paramString, false);
  }
  
  public boolean getBoolean(@NonNull String paramString, boolean paramBoolean) {
    return this.sp.getBoolean(paramString, paramBoolean);
  }
  
  public float getFloat(@NonNull String paramString) {
    return getFloat(paramString, -1.0F);
  }
  
  public float getFloat(@NonNull String paramString, float paramFloat) {
    return this.sp.getFloat(paramString, paramFloat);
  }
  
  public int getInt(@NonNull String paramString) {
    return getInt(paramString, -1);
  }
  
  public int getInt(@NonNull String paramString, int paramInt) {
    return this.sp.getInt(paramString, paramInt);
  }
  
  public long getLong(@NonNull String paramString) {
    return getLong(paramString, -1L);
  }
  
  public long getLong(@NonNull String paramString, long paramLong) {
    return this.sp.getLong(paramString, paramLong);
  }
  
  public String getString(@NonNull String paramString) {
    return getString(paramString, "");
  }
  
  public String getString(@NonNull String paramString1, @NonNull String paramString2) {
    return this.sp.getString(paramString1, paramString2);
  }
  
  public Set<String> getStringSet(@NonNull String paramString) {
    return getStringSet(paramString, Collections.emptySet());
  }
  
  public Set<String> getStringSet(@NonNull String paramString, @NonNull Set<String> paramSet) {
    return this.sp.getStringSet(paramString, paramSet);
  }
  
  public void put(@NonNull String paramString, float paramFloat) {
    put(paramString, paramFloat, false);
  }
  
  public void put(@NonNull String paramString, float paramFloat, boolean paramBoolean) {
    if (paramBoolean) {
      this.sp.edit().putFloat(paramString, paramFloat).commit();
      return;
    } 
    this.sp.edit().putFloat(paramString, paramFloat).apply();
  }
  
  public void put(@NonNull String paramString, int paramInt) {
    put(paramString, paramInt, false);
  }
  
  public void put(@NonNull String paramString, int paramInt, boolean paramBoolean) {
    if (paramBoolean) {
      this.sp.edit().putInt(paramString, paramInt).commit();
      return;
    } 
    this.sp.edit().putInt(paramString, paramInt).apply();
  }
  
  public void put(@NonNull String paramString, long paramLong) {
    put(paramString, paramLong, false);
  }
  
  public void put(@NonNull String paramString, long paramLong, boolean paramBoolean) {
    if (paramBoolean) {
      this.sp.edit().putLong(paramString, paramLong).commit();
      return;
    } 
    this.sp.edit().putLong(paramString, paramLong).apply();
  }
  
  public void put(@NonNull String paramString1, @NonNull String paramString2) {
    put(paramString1, paramString2, false);
  }
  
  public void put(@NonNull String paramString1, @NonNull String paramString2, boolean paramBoolean) {
    if (paramBoolean) {
      this.sp.edit().putString(paramString1, paramString2).commit();
      return;
    } 
    this.sp.edit().putString(paramString1, paramString2).apply();
  }
  
  public void put(@NonNull String paramString, @NonNull Set<String> paramSet) {
    put(paramString, paramSet, false);
  }
  
  public void put(@NonNull String paramString, @NonNull Set<String> paramSet, boolean paramBoolean) {
    if (paramBoolean) {
      this.sp.edit().putStringSet(paramString, paramSet).commit();
      return;
    } 
    this.sp.edit().putStringSet(paramString, paramSet).apply();
  }
  
  public void put(@NonNull String paramString, boolean paramBoolean) {
    put(paramString, paramBoolean, false);
  }
  
  public void put(@NonNull String paramString, boolean paramBoolean1, boolean paramBoolean2) {
    if (paramBoolean2) {
      this.sp.edit().putBoolean(paramString, paramBoolean1).commit();
      return;
    } 
    this.sp.edit().putBoolean(paramString, paramBoolean1).apply();
  }
  
  public void remove(@NonNull String paramString) {
    remove(paramString, false);
  }
  
  public void remove(@NonNull String paramString, boolean paramBoolean) {
    if (paramBoolean) {
      this.sp.edit().remove(paramString).commit();
      return;
    } 
    this.sp.edit().remove(paramString).apply();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/SPUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */