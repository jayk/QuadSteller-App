package app.gamer.quadstellar.utils;

import java.util.Locale;
import java.util.regex.Pattern;

public class StringUtils {
  private static final Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
  
  private static final Pattern phone = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
  
  public static final String byteArrayToHexString(byte[] paramArrayOfbyte) {
    StringBuilder stringBuilder = new StringBuilder(paramArrayOfbyte.length * 2);
    int i = paramArrayOfbyte.length;
    for (byte b = 0; b < i; b++) {
      int j = paramArrayOfbyte[b] & 0xFF;
      if (j < 16)
        stringBuilder.append('0'); 
      stringBuilder.append(Integer.toHexString(j));
    } 
    return stringBuilder.toString().toUpperCase(Locale.getDefault());
  }
  
  public static void handleNull(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6) {
    // Byte code:
    //   0: aload_0
    //   1: ifnonnull -> 4
    //   4: aload_1
    //   5: ifnonnull -> 8
    //   8: aload_2
    //   9: ifnonnull -> 12
    //   12: aload_3
    //   13: ifnonnull -> 16
    //   16: aload #4
    //   18: ifnonnull -> 21
    //   21: aload #5
    //   23: ifnonnull -> 26
    //   26: return
  }
  
  public static byte[] hexStringToByteArray(String paramString) {
    int i = paramString.length();
    byte[] arrayOfByte = new byte[i / 2];
    for (byte b = 0; b < i; b += 2)
      arrayOfByte[b / 2] = (byte)(byte)((Character.digit(paramString.charAt(b), 16) << 4) + Character.digit(paramString.charAt(b + 1), 16)); 
    return arrayOfByte;
  }
  
  public static boolean isEmail(CharSequence paramCharSequence) {
    return isEmpty(paramCharSequence) ? false : emailer.matcher(paramCharSequence).matches();
  }
  
  public static boolean isEmpty(CharSequence paramCharSequence) {
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (paramCharSequence != null) {
      if ("".equals(paramCharSequence))
        return bool1; 
    } else {
      return bool2;
    } 
    byte b = 0;
    while (true) {
      bool2 = bool1;
      if (b < paramCharSequence.length()) {
        char c = paramCharSequence.charAt(b);
        if (c != ' ' && c != '\t' && c != '\r' && c != '\n')
          return false; 
        b++;
        continue;
      } 
      return bool2;
    } 
  }
  
  public static boolean isEmpty(String paramString) {
    return (paramString == null || paramString.equals(""));
  }
  
  public static boolean isNumber(CharSequence paramCharSequence) {
    boolean bool;
    try {
      Integer.parseInt(paramCharSequence.toString());
      bool = true;
    } catch (Exception exception) {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean isPhone(CharSequence paramCharSequence) {
    return isEmpty(paramCharSequence) ? false : phone.matcher(paramCharSequence).matches();
  }
  
  public static String replaceUrl(String paramString) {
    String str = paramString;
    if (!paramString.contains("https://"))
      str = paramString.replace("http://", "http://"); 
    return str;
  }
  
  public static boolean toBool(String paramString) {
    boolean bool;
    try {
      bool = Boolean.parseBoolean(paramString);
    } catch (Exception exception) {
      bool = false;
    } 
    return bool;
  }
  
  public static double toDouble(String paramString) {
    double d;
    try {
      d = Double.parseDouble(paramString);
    } catch (Exception exception) {
      d = 0.0D;
    } 
    return d;
  }
  
  public static int toInt(Object paramObject) {
    int i = 0;
    if (paramObject != null)
      i = toInt(paramObject.toString(), 0); 
    return i;
  }
  
  public static int toInt(String paramString, int paramInt) {
    try {
      int i = Integer.parseInt(paramString);
      paramInt = i;
    } catch (Exception exception) {}
    return paramInt;
  }
  
  public static long toLong(String paramString) {
    long l;
    try {
      l = Long.parseLong(paramString);
    } catch (Exception exception) {
      l = 0L;
    } 
    return l;
  }
  
  public boolean isNumeric(String paramString) {
    return Pattern.compile("[0-9]*").matcher(paramString).matches();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/utils/StringUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */