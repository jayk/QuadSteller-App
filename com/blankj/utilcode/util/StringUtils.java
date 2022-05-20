package com.blankj.utilcode.util;

public final class StringUtils {
  private StringUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static boolean equals(CharSequence paramCharSequence1, CharSequence paramCharSequence2) {
    boolean bool = true;
    if (paramCharSequence1 == paramCharSequence2)
      return bool; 
    if (paramCharSequence1 != null && paramCharSequence2 != null) {
      int i = paramCharSequence1.length();
      if (i == paramCharSequence2.length()) {
        if (paramCharSequence1 instanceof String && paramCharSequence2 instanceof String)
          return paramCharSequence1.equals(paramCharSequence2); 
        byte b = 0;
        while (true) {
          boolean bool1 = bool;
          if (b < i) {
            if (paramCharSequence1.charAt(b) != paramCharSequence2.charAt(b))
              return false; 
            b++;
            continue;
          } 
          return bool1;
        } 
      } 
    } 
    return false;
  }
  
  public static boolean equalsIgnoreCase(String paramString1, String paramString2) {
    return (paramString1 == null) ? ((paramString2 == null)) : paramString1.equalsIgnoreCase(paramString2);
  }
  
  public static boolean isEmpty(CharSequence paramCharSequence) {
    return (paramCharSequence == null || paramCharSequence.length() == 0);
  }
  
  public static boolean isSpace(String paramString) {
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
  
  public static boolean isTrimEmpty(String paramString) {
    return (paramString == null || paramString.trim().length() == 0);
  }
  
  public static int length(CharSequence paramCharSequence) {
    return (paramCharSequence == null) ? 0 : paramCharSequence.length();
  }
  
  public static String lowerFirstLetter(String paramString) {
    null = paramString;
    if (!isEmpty(paramString)) {
      if (!Character.isUpperCase(paramString.charAt(0)))
        return paramString; 
    } else {
      return null;
    } 
    return String.valueOf((char)(paramString.charAt(0) + 32)) + paramString.substring(1);
  }
  
  public static String null2Length0(String paramString) {
    String str = paramString;
    if (paramString == null)
      str = ""; 
    return str;
  }
  
  public static String reverse(String paramString) {
    String str;
    int i = length(paramString);
    if (i > 1) {
      char[] arrayOfChar = paramString.toCharArray();
      for (byte b = 0; b < i >> 1; b++) {
        char c = arrayOfChar[b];
        arrayOfChar[b] = (char)arrayOfChar[i - b - 1];
        arrayOfChar[i - b - 1] = (char)c;
      } 
      str = new String(arrayOfChar);
    } 
    return str;
  }
  
  public static String toDBC(String paramString) {
    String str;
    if (!isEmpty(paramString)) {
      char[] arrayOfChar = paramString.toCharArray();
      byte b = 0;
      int i = arrayOfChar.length;
      while (b < i) {
        if (arrayOfChar[b] == '　') {
          arrayOfChar[b] = (char)' ';
        } else if ('！' <= arrayOfChar[b] && arrayOfChar[b] <= '～') {
          arrayOfChar[b] = (char)(char)(arrayOfChar[b] - 65248);
        } else {
          arrayOfChar[b] = (char)arrayOfChar[b];
        } 
        b++;
      } 
      str = new String(arrayOfChar);
    } 
    return str;
  }
  
  public static String toSBC(String paramString) {
    String str;
    if (!isEmpty(paramString)) {
      char[] arrayOfChar = paramString.toCharArray();
      byte b = 0;
      int i = arrayOfChar.length;
      while (b < i) {
        if (arrayOfChar[b] == ' ') {
          arrayOfChar[b] = (char)'　';
        } else if ('!' <= arrayOfChar[b] && arrayOfChar[b] <= '~') {
          arrayOfChar[b] = (char)(char)(arrayOfChar[b] + 65248);
        } else {
          arrayOfChar[b] = (char)arrayOfChar[b];
        } 
        b++;
      } 
      str = new String(arrayOfChar);
    } 
    return str;
  }
  
  public static String upperFirstLetter(String paramString) {
    null = paramString;
    if (!isEmpty(paramString)) {
      if (!Character.isLowerCase(paramString.charAt(0)))
        return paramString; 
    } else {
      return null;
    } 
    return String.valueOf((char)(paramString.charAt(0) - 32)) + paramString.substring(1);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/StringUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */