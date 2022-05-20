package com.loopj.android.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

public class PersistentCookieStore implements CookieStore {
  private static final String COOKIE_NAME_PREFIX = "cookie_";
  
  private static final String COOKIE_NAME_STORE = "names";
  
  private static final String COOKIE_PREFS = "CookiePrefsFile";
  
  private static final String LOG_TAG = "PersistentCookieStore";
  
  private final SharedPreferences cookiePrefs;
  
  private final ConcurrentHashMap<String, Cookie> cookies;
  
  private boolean omitNonPersistentCookies = false;
  
  public PersistentCookieStore(Context paramContext) {
    this.cookiePrefs = paramContext.getSharedPreferences("CookiePrefsFile", 0);
    this.cookies = new ConcurrentHashMap<String, Cookie>();
    str = this.cookiePrefs.getString("names", null);
    if (str != null) {
      for (String str : TextUtils.split(str, ",")) {
        String str1 = this.cookiePrefs.getString("cookie_" + str, null);
        if (str1 != null) {
          Cookie cookie = decodeCookie(str1);
          if (cookie != null)
            this.cookies.put(str, cookie); 
        } 
      } 
      clearExpired(new Date());
    } 
  }
  
  public void addCookie(Cookie paramCookie) {
    if (!this.omitNonPersistentCookies || paramCookie.isPersistent()) {
      String str = paramCookie.getName() + paramCookie.getDomain();
      if (!paramCookie.isExpired(new Date())) {
        this.cookies.put(str, paramCookie);
      } else {
        this.cookies.remove(str);
      } 
      SharedPreferences.Editor editor = this.cookiePrefs.edit();
      editor.putString("names", TextUtils.join(",", this.cookies.keySet()));
      editor.putString("cookie_" + str, encodeCookie(new SerializableCookie(paramCookie)));
      editor.commit();
    } 
  }
  
  protected String byteArrayToHexString(byte[] paramArrayOfbyte) {
    StringBuilder stringBuilder = new StringBuilder(paramArrayOfbyte.length * 2);
    int i = paramArrayOfbyte.length;
    for (byte b = 0; b < i; b++) {
      int j = paramArrayOfbyte[b] & 0xFF;
      if (j < 16)
        stringBuilder.append('0'); 
      stringBuilder.append(Integer.toHexString(j));
    } 
    return stringBuilder.toString().toUpperCase(Locale.US);
  }
  
  public void clear() {
    SharedPreferences.Editor editor = this.cookiePrefs.edit();
    for (String str : this.cookies.keySet())
      editor.remove("cookie_" + str); 
    editor.remove("names");
    editor.commit();
    this.cookies.clear();
  }
  
  public boolean clearExpired(Date paramDate) {
    boolean bool = false;
    SharedPreferences.Editor editor = this.cookiePrefs.edit();
    for (Map.Entry<String, Cookie> entry : this.cookies.entrySet()) {
      String str = (String)entry.getKey();
      if (((Cookie)entry.getValue()).isExpired(paramDate)) {
        this.cookies.remove(str);
        editor.remove("cookie_" + str);
        bool = true;
      } 
    } 
    if (bool)
      editor.putString("names", TextUtils.join(",", this.cookies.keySet())); 
    editor.commit();
    return bool;
  }
  
  protected Cookie decodeCookie(String paramString) {
    Cookie cookie;
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(hexStringToByteArray(paramString));
    paramString = null;
    try {
      ObjectInputStream objectInputStream = new ObjectInputStream();
      this(byteArrayInputStream);
      Cookie cookie1 = ((SerializableCookie)objectInputStream.readObject()).getCookie();
      cookie = cookie1;
    } catch (IOException iOException) {
      Log.d("PersistentCookieStore", "IOException in decodeCookie", iOException);
    } catch (ClassNotFoundException classNotFoundException) {
      Log.d("PersistentCookieStore", "ClassNotFoundException in decodeCookie", classNotFoundException);
    } 
    return cookie;
  }
  
  public void deleteCookie(Cookie paramCookie) {
    String str = paramCookie.getName() + paramCookie.getDomain();
    this.cookies.remove(str);
    SharedPreferences.Editor editor = this.cookiePrefs.edit();
    editor.remove("cookie_" + str);
    editor.commit();
  }
  
  protected String encodeCookie(SerializableCookie paramSerializableCookie) {
    SerializableCookie serializableCookie1;
    SerializableCookie serializableCookie2 = null;
    if (paramSerializableCookie == null)
      return (String)serializableCookie2; 
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    try {
      ObjectOutputStream objectOutputStream = new ObjectOutputStream();
      this(byteArrayOutputStream);
      objectOutputStream.writeObject(paramSerializableCookie);
      String str = byteArrayToHexString(byteArrayOutputStream.toByteArray());
    } catch (IOException iOException) {
      Log.d("PersistentCookieStore", "IOException in encodeCookie", iOException);
      serializableCookie1 = serializableCookie2;
    } 
    return (String)serializableCookie1;
  }
  
  public List<Cookie> getCookies() {
    return new ArrayList<Cookie>(this.cookies.values());
  }
  
  protected byte[] hexStringToByteArray(String paramString) {
    int i = paramString.length();
    byte[] arrayOfByte = new byte[i / 2];
    for (byte b = 0; b < i; b += 2)
      arrayOfByte[b / 2] = (byte)(byte)((Character.digit(paramString.charAt(b), 16) << 4) + Character.digit(paramString.charAt(b + 1), 16)); 
    return arrayOfByte;
  }
  
  public void setOmitNonPersistentCookies(boolean paramBoolean) {
    this.omitNonPersistentCookies = paramBoolean;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/loopj/android/http/PersistentCookieStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */