package com.blankj.utilcode.util;

import android.os.Build;
import android.text.Html;
import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public final class EncodeUtils {
  private EncodeUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static byte[] base64Decode(String paramString) {
    return Base64.decode(paramString, 2);
  }
  
  public static byte[] base64Decode(byte[] paramArrayOfbyte) {
    return Base64.decode(paramArrayOfbyte, 2);
  }
  
  public static byte[] base64Encode(String paramString) {
    return base64Encode(paramString.getBytes());
  }
  
  public static byte[] base64Encode(byte[] paramArrayOfbyte) {
    return Base64.encode(paramArrayOfbyte, 2);
  }
  
  public static String base64Encode2String(byte[] paramArrayOfbyte) {
    return Base64.encodeToString(paramArrayOfbyte, 2);
  }
  
  public static byte[] base64UrlSafeEncode(String paramString) {
    return Base64.encode(paramString.getBytes(), 8);
  }
  
  public static CharSequence htmlDecode(String paramString) {
    return (CharSequence)((Build.VERSION.SDK_INT >= 24) ? Html.fromHtml(paramString, 0) : Html.fromHtml(paramString));
  }
  
  public static String htmlEncode(CharSequence paramCharSequence) {
    StringBuilder stringBuilder = new StringBuilder();
    byte b = 0;
    int i = paramCharSequence.length();
    while (true) {
      if (b < i) {
        char c = paramCharSequence.charAt(b);
        switch (c) {
          default:
            stringBuilder.append(c);
            b++;
            continue;
          case '<':
            stringBuilder.append("&lt;");
            b++;
            continue;
          case '>':
            stringBuilder.append("&gt;");
            b++;
            continue;
          case '&':
            stringBuilder.append("&amp;");
            b++;
            continue;
          case '\'':
            stringBuilder.append("&#39;");
            b++;
            continue;
          case '"':
            break;
        } 
        stringBuilder.append("&quot;");
      } else {
        break;
      } 
      b++;
    } 
    return stringBuilder.toString();
  }
  
  public static String urlDecode(String paramString) {
    return urlDecode(paramString, "UTF-8");
  }
  
  public static String urlDecode(String paramString1, String paramString2) {
    try {
      paramString2 = URLDecoder.decode(paramString1, paramString2);
      paramString1 = paramString2;
    } catch (UnsupportedEncodingException unsupportedEncodingException) {}
    return paramString1;
  }
  
  public static String urlEncode(String paramString) {
    return urlEncode(paramString, "UTF-8");
  }
  
  public static String urlEncode(String paramString1, String paramString2) {
    try {
      paramString2 = URLEncoder.encode(paramString1, paramString2);
      paramString1 = paramString2;
    } catch (UnsupportedEncodingException unsupportedEncodingException) {}
    return paramString1;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/EncodeUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */