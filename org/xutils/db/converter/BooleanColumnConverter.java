package org.xutils.db.converter;

import android.database.Cursor;
import org.xutils.db.sqlite.ColumnDbType;

public class BooleanColumnConverter implements ColumnConverter<Boolean> {
  public Object fieldValue2DbValue(Boolean paramBoolean) {
    boolean bool;
    if (paramBoolean == null)
      return null; 
    if (paramBoolean.booleanValue()) {
      bool = true;
    } else {
      bool = false;
    } 
    return Integer.valueOf(bool);
  }
  
  public ColumnDbType getColumnDbType() {
    return ColumnDbType.INTEGER;
  }
  
  public Boolean getFieldValue(Cursor paramCursor, int paramInt) {
    boolean bool = true;
    if (paramCursor.isNull(paramInt))
      return null; 
    if (paramCursor.getInt(paramInt) != 1)
      bool = false; 
    return Boolean.valueOf(bool);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/converter/BooleanColumnConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */