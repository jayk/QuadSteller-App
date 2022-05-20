package org.xutils.db.table;

import android.database.Cursor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.xutils.common.util.LogUtil;
import org.xutils.db.annotation.Column;
import org.xutils.db.converter.ColumnConverter;
import org.xutils.db.converter.ColumnConverterFactory;
import org.xutils.db.sqlite.ColumnDbType;

public final class ColumnEntity {
  protected final ColumnConverter columnConverter;
  
  protected final Field columnField;
  
  protected final Method getMethod;
  
  private final boolean isAutoId;
  
  private final boolean isId;
  
  protected final String name;
  
  private final String property;
  
  protected final Method setMethod;
  
  ColumnEntity(Class<?> paramClass, Field paramField, Column paramColumn) {
    boolean bool;
    paramField.setAccessible(true);
    this.columnField = paramField;
    this.name = paramColumn.name();
    this.property = paramColumn.property();
    this.isId = paramColumn.isId();
    Class<?> clazz = paramField.getType();
    if (this.isId && paramColumn.autoGen() && ColumnUtils.isAutoIdType(clazz)) {
      bool = true;
    } else {
      bool = false;
    } 
    this.isAutoId = bool;
    this.columnConverter = ColumnConverterFactory.getColumnConverter(clazz);
    this.getMethod = ColumnUtils.findGetMethod(paramClass, paramField);
    if (this.getMethod != null && !this.getMethod.isAccessible())
      this.getMethod.setAccessible(true); 
    this.setMethod = ColumnUtils.findSetMethod(paramClass, paramField);
    if (this.setMethod != null && !this.setMethod.isAccessible())
      this.setMethod.setAccessible(true); 
  }
  
  public ColumnConverter getColumnConverter() {
    return this.columnConverter;
  }
  
  public ColumnDbType getColumnDbType() {
    return this.columnConverter.getColumnDbType();
  }
  
  public Field getColumnField() {
    return this.columnField;
  }
  
  public Object getColumnValue(Object paramObject) {
    paramObject = getFieldValue(paramObject);
    return (this.isAutoId && (paramObject.equals(Long.valueOf(0L)) || paramObject.equals(Integer.valueOf(0)))) ? null : this.columnConverter.fieldValue2DbValue(paramObject);
  }
  
  public Object getFieldValue(Object paramObject) {
    Object object = null;
    Object object1 = object;
    if (paramObject != null) {
      if (this.getMethod != null) {
        try {
          object1 = this.getMethod.invoke(paramObject, new Object[0]);
        } catch (Throwable throwable) {
          LogUtil.e(throwable.getMessage(), throwable);
          object1 = object;
        } 
        return object1;
      } 
    } else {
      return object1;
    } 
    try {
      object1 = this.columnField.get(throwable);
    } catch (Throwable throwable1) {
      LogUtil.e(throwable1.getMessage(), throwable1);
      object1 = object;
    } 
    return object1;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getProperty() {
    return this.property;
  }
  
  public boolean isAutoId() {
    return this.isAutoId;
  }
  
  public boolean isId() {
    return this.isId;
  }
  
  public void setAutoIdValue(Object paramObject, long paramLong) {
    Integer integer;
    Long long_ = Long.valueOf(paramLong);
    if (ColumnUtils.isInteger(this.columnField.getType()))
      integer = Integer.valueOf((int)paramLong); 
    if (this.setMethod != null) {
      try {
        this.setMethod.invoke(paramObject, new Object[] { integer });
      } catch (Throwable throwable) {
        LogUtil.e(throwable.getMessage(), throwable);
      } 
      return;
    } 
    try {
      this.columnField.set(throwable, integer);
    } catch (Throwable throwable1) {
      LogUtil.e(throwable1.getMessage(), throwable1);
    } 
  }
  
  public void setValueFromCursor(Object paramObject, Cursor paramCursor, int paramInt) {
    Object object = this.columnConverter.getFieldValue(paramCursor, paramInt);
    if (object != null) {
      if (this.setMethod != null) {
        try {
          this.setMethod.invoke(paramObject, new Object[] { object });
        } catch (Throwable throwable) {
          LogUtil.e(throwable.getMessage(), throwable);
        } 
        return;
      } 
      try {
        this.columnField.set(throwable, object);
      } catch (Throwable throwable1) {
        LogUtil.e(throwable1.getMessage(), throwable1);
      } 
    } 
  }
  
  public String toString() {
    return this.name;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/table/ColumnEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */