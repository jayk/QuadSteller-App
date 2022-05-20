package com.google.gson;

import java.lang.reflect.Field;

public enum FieldNamingPolicy implements FieldNamingStrategy {
  IDENTITY {
    public String translateName(Field param1Field) {
      return param1Field.getName();
    }
  },
  LOWER_CASE_WITH_DASHES,
  LOWER_CASE_WITH_UNDERSCORES,
  UPPER_CAMEL_CASE {
    public String translateName(Field param1Field) {
      return upperCaseFirstLetter(param1Field.getName());
    }
  },
  UPPER_CAMEL_CASE_WITH_SPACES {
    public String translateName(Field param1Field) {
      return upperCaseFirstLetter(separateCamelCase(param1Field.getName(), " "));
    }
  };
  
  static {
    LOWER_CASE_WITH_UNDERSCORES = new null("LOWER_CASE_WITH_UNDERSCORES", 3);
    LOWER_CASE_WITH_DASHES = new null("LOWER_CASE_WITH_DASHES", 4);
    $VALUES = new FieldNamingPolicy[] { IDENTITY, UPPER_CAMEL_CASE, UPPER_CAMEL_CASE_WITH_SPACES, LOWER_CASE_WITH_UNDERSCORES, LOWER_CASE_WITH_DASHES };
  }
  
  private static String modifyString(char paramChar, String paramString, int paramInt) {
    return (paramInt < paramString.length()) ? (paramChar + paramString.substring(paramInt)) : String.valueOf(paramChar);
  }
  
  private static String separateCamelCase(String paramString1, String paramString2) {
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b < paramString1.length(); b++) {
      char c = paramString1.charAt(b);
      if (Character.isUpperCase(c) && stringBuilder.length() != 0)
        stringBuilder.append(paramString2); 
      stringBuilder.append(c);
    } 
    return stringBuilder.toString();
  }
  
  private static String upperCaseFirstLetter(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    byte b = 0;
    char c = paramString.charAt(0);
    char c1 = c;
    while (true) {
      if (b >= paramString.length() - 1 || Character.isLetter(c1)) {
        if (b == paramString.length())
          return stringBuilder.toString(); 
      } else {
        stringBuilder.append(c1);
        c = paramString.charAt(++b);
        c1 = c;
        continue;
      } 
      String str = paramString;
      if (!Character.isUpperCase(c1))
        str = stringBuilder.append(modifyString(Character.toUpperCase(c1), paramString, b + 1)).toString(); 
      return str;
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/google/gson/FieldNamingPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */