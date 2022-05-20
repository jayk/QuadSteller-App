package org.greenrobot.greendao.internal;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.Property;

public class SqlUtils {
  private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
  
  public static StringBuilder appendColumn(StringBuilder paramStringBuilder, String paramString) {
    paramStringBuilder.append('"').append(paramString).append('"');
    return paramStringBuilder;
  }
  
  public static StringBuilder appendColumn(StringBuilder paramStringBuilder, String paramString1, String paramString2) {
    paramStringBuilder.append(paramString1).append(".\"").append(paramString2).append('"');
    return paramStringBuilder;
  }
  
  public static StringBuilder appendColumns(StringBuilder paramStringBuilder, String paramString, String[] paramArrayOfString) {
    int i = paramArrayOfString.length;
    for (byte b = 0; b < i; b++) {
      appendColumn(paramStringBuilder, paramString, paramArrayOfString[b]);
      if (b < i - 1)
        paramStringBuilder.append(','); 
    } 
    return paramStringBuilder;
  }
  
  public static StringBuilder appendColumns(StringBuilder paramStringBuilder, String[] paramArrayOfString) {
    int i = paramArrayOfString.length;
    for (byte b = 0; b < i; b++) {
      paramStringBuilder.append('"').append(paramArrayOfString[b]).append('"');
      if (b < i - 1)
        paramStringBuilder.append(','); 
    } 
    return paramStringBuilder;
  }
  
  public static StringBuilder appendColumnsEqValue(StringBuilder paramStringBuilder, String paramString, String[] paramArrayOfString) {
    for (byte b = 0; b < paramArrayOfString.length; b++) {
      appendColumn(paramStringBuilder, paramString, paramArrayOfString[b]).append("=?");
      if (b < paramArrayOfString.length - 1)
        paramStringBuilder.append(','); 
    } 
    return paramStringBuilder;
  }
  
  public static StringBuilder appendColumnsEqualPlaceholders(StringBuilder paramStringBuilder, String[] paramArrayOfString) {
    for (byte b = 0; b < paramArrayOfString.length; b++) {
      appendColumn(paramStringBuilder, paramArrayOfString[b]).append("=?");
      if (b < paramArrayOfString.length - 1)
        paramStringBuilder.append(','); 
    } 
    return paramStringBuilder;
  }
  
  public static StringBuilder appendPlaceholders(StringBuilder paramStringBuilder, int paramInt) {
    for (byte b = 0; b < paramInt; b++) {
      if (b < paramInt - 1) {
        paramStringBuilder.append("?,");
      } else {
        paramStringBuilder.append('?');
      } 
    } 
    return paramStringBuilder;
  }
  
  public static StringBuilder appendProperty(StringBuilder paramStringBuilder, String paramString, Property paramProperty) {
    if (paramString != null)
      paramStringBuilder.append(paramString).append('.'); 
    paramStringBuilder.append('"').append(paramProperty.columnName).append('"');
    return paramStringBuilder;
  }
  
  public static String createSqlCount(String paramString) {
    return "SELECT COUNT(*) FROM \"" + paramString + '"';
  }
  
  public static String createSqlDelete(String paramString, String[] paramArrayOfString) {
    String str = '"' + paramString + '"';
    StringBuilder stringBuilder = new StringBuilder("DELETE FROM ");
    stringBuilder.append(str);
    if (paramArrayOfString != null && paramArrayOfString.length > 0) {
      stringBuilder.append(" WHERE ");
      appendColumnsEqValue(stringBuilder, str, paramArrayOfString);
    } 
    return stringBuilder.toString();
  }
  
  public static String createSqlInsert(String paramString1, String paramString2, String[] paramArrayOfString) {
    StringBuilder stringBuilder = new StringBuilder(paramString1);
    stringBuilder.append('"').append(paramString2).append('"').append(" (");
    appendColumns(stringBuilder, paramArrayOfString);
    stringBuilder.append(") VALUES (");
    appendPlaceholders(stringBuilder, paramArrayOfString.length);
    stringBuilder.append(')');
    return stringBuilder.toString();
  }
  
  public static String createSqlSelect(String paramString1, String paramString2, String[] paramArrayOfString, boolean paramBoolean) {
    if (paramString2 == null || paramString2.length() < 0)
      throw new DaoException("Table alias required"); 
    if (paramBoolean) {
      String str1 = "SELECT DISTINCT ";
      StringBuilder stringBuilder1 = new StringBuilder(str1);
      appendColumns(stringBuilder1, paramString2, paramArrayOfString).append(" FROM ");
      stringBuilder1.append('"').append(paramString1).append('"').append(' ').append(paramString2).append(' ');
      return stringBuilder1.toString();
    } 
    String str = "SELECT ";
    StringBuilder stringBuilder = new StringBuilder(str);
    appendColumns(stringBuilder, paramString2, paramArrayOfString).append(" FROM ");
    stringBuilder.append('"').append(paramString1).append('"').append(' ').append(paramString2).append(' ');
    return stringBuilder.toString();
  }
  
  public static String createSqlSelectCountStar(String paramString1, String paramString2) {
    StringBuilder stringBuilder = new StringBuilder("SELECT COUNT(*) FROM ");
    stringBuilder.append('"').append(paramString1).append('"').append(' ');
    if (paramString2 != null)
      stringBuilder.append(paramString2).append(' '); 
    return stringBuilder.toString();
  }
  
  public static String createSqlUpdate(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2) {
    String str = '"' + paramString + '"';
    StringBuilder stringBuilder = new StringBuilder("UPDATE ");
    stringBuilder.append(str).append(" SET ");
    appendColumnsEqualPlaceholders(stringBuilder, paramArrayOfString1);
    stringBuilder.append(" WHERE ");
    appendColumnsEqValue(stringBuilder, str, paramArrayOfString2);
    return stringBuilder.toString();
  }
  
  public static String escapeBlobArgument(byte[] paramArrayOfbyte) {
    return "X'" + toHex(paramArrayOfbyte) + '\'';
  }
  
  public static String toHex(byte[] paramArrayOfbyte) {
    char[] arrayOfChar = new char[paramArrayOfbyte.length * 2];
    for (byte b = 0; b < paramArrayOfbyte.length; b++) {
      int i = paramArrayOfbyte[b] & 0xFF;
      arrayOfChar[b * 2] = (char)HEX_ARRAY[i >>> 4];
      arrayOfChar[b * 2 + 1] = (char)HEX_ARRAY[i & 0xF];
    } 
    return new String(arrayOfChar);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/internal/SqlUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */