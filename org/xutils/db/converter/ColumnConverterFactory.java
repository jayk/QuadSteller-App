package org.xutils.db.converter;

import java.sql.Date;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import org.xutils.common.util.LogUtil;
import org.xutils.db.sqlite.ColumnDbType;

public final class ColumnConverterFactory {
  private static final ConcurrentHashMap<String, ColumnConverter> columnType_columnConverter_map = new ConcurrentHashMap<String, ColumnConverter>();
  
  static {
    BooleanColumnConverter booleanColumnConverter = new BooleanColumnConverter();
    columnType_columnConverter_map.put(boolean.class.getName(), booleanColumnConverter);
    columnType_columnConverter_map.put(Boolean.class.getName(), booleanColumnConverter);
    ByteArrayColumnConverter byteArrayColumnConverter = new ByteArrayColumnConverter();
    columnType_columnConverter_map.put(byte[].class.getName(), byteArrayColumnConverter);
    ByteColumnConverter byteColumnConverter = new ByteColumnConverter();
    columnType_columnConverter_map.put(byte.class.getName(), byteColumnConverter);
    columnType_columnConverter_map.put(Byte.class.getName(), byteColumnConverter);
    CharColumnConverter charColumnConverter = new CharColumnConverter();
    columnType_columnConverter_map.put(char.class.getName(), charColumnConverter);
    columnType_columnConverter_map.put(Character.class.getName(), charColumnConverter);
    DateColumnConverter dateColumnConverter = new DateColumnConverter();
    columnType_columnConverter_map.put(Date.class.getName(), dateColumnConverter);
    DoubleColumnConverter doubleColumnConverter = new DoubleColumnConverter();
    columnType_columnConverter_map.put(double.class.getName(), doubleColumnConverter);
    columnType_columnConverter_map.put(Double.class.getName(), doubleColumnConverter);
    FloatColumnConverter floatColumnConverter = new FloatColumnConverter();
    columnType_columnConverter_map.put(float.class.getName(), floatColumnConverter);
    columnType_columnConverter_map.put(Float.class.getName(), floatColumnConverter);
    IntegerColumnConverter integerColumnConverter = new IntegerColumnConverter();
    columnType_columnConverter_map.put(int.class.getName(), integerColumnConverter);
    columnType_columnConverter_map.put(Integer.class.getName(), integerColumnConverter);
    LongColumnConverter longColumnConverter = new LongColumnConverter();
    columnType_columnConverter_map.put(long.class.getName(), longColumnConverter);
    columnType_columnConverter_map.put(Long.class.getName(), longColumnConverter);
    ShortColumnConverter shortColumnConverter = new ShortColumnConverter();
    columnType_columnConverter_map.put(short.class.getName(), shortColumnConverter);
    columnType_columnConverter_map.put(Short.class.getName(), shortColumnConverter);
    SqlDateColumnConverter sqlDateColumnConverter = new SqlDateColumnConverter();
    columnType_columnConverter_map.put(Date.class.getName(), sqlDateColumnConverter);
    StringColumnConverter stringColumnConverter = new StringColumnConverter();
    columnType_columnConverter_map.put(String.class.getName(), stringColumnConverter);
  }
  
  public static ColumnConverter getColumnConverter(Class<?> paramClass) {
    ColumnConverter columnConverter2;
    ColumnConverter columnConverter1 = null;
    if (columnType_columnConverter_map.containsKey(paramClass.getName())) {
      columnConverter2 = columnType_columnConverter_map.get(paramClass.getName());
    } else {
      columnConverter2 = columnConverter1;
      if (ColumnConverter.class.isAssignableFrom(paramClass)) {
        try {
          columnConverter2 = (ColumnConverter)paramClass.newInstance();
          if (columnConverter2 != null)
            columnType_columnConverter_map.put(paramClass.getName(), columnConverter2); 
          if (columnConverter2 == null)
            throw new RuntimeException("Database Column Not Support: " + paramClass.getName() + ", please impl ColumnConverter or use ColumnConverterFactory#registerColumnConverter(...)"); 
        } catch (Throwable throwable) {
          LogUtil.e(throwable.getMessage(), throwable);
          columnConverter2 = columnConverter1;
          if (columnConverter2 == null)
            throw new RuntimeException("Database Column Not Support: " + paramClass.getName() + ", please impl ColumnConverter or use ColumnConverterFactory#registerColumnConverter(...)"); 
        } 
        return columnConverter2;
      } 
    } 
    if (columnConverter2 == null)
      throw new RuntimeException("Database Column Not Support: " + paramClass.getName() + ", please impl ColumnConverter or use ColumnConverterFactory#registerColumnConverter(...)"); 
  }
  
  public static ColumnDbType getDbColumnType(Class paramClass) {
    return getColumnConverter(paramClass).getColumnDbType();
  }
  
  public static boolean isSupportColumnConverter(Class<?> paramClass) {
    boolean bool = true;
    if (!columnType_columnConverter_map.containsKey(paramClass.getName())) {
      if (ColumnConverter.class.isAssignableFrom(paramClass)) {
        try {
          ColumnConverter columnConverter = (ColumnConverter)paramClass.newInstance();
          if (columnConverter != null)
            columnType_columnConverter_map.put(paramClass.getName(), columnConverter); 
          if (columnConverter != null)
            bool = false; 
        } catch (Throwable throwable) {}
        return bool;
      } 
      bool = false;
    } 
    return bool;
  }
  
  public static void registerColumnConverter(Class paramClass, ColumnConverter paramColumnConverter) {
    columnType_columnConverter_map.put(paramClass.getName(), paramColumnConverter);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/converter/ColumnConverterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */