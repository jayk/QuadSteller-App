package org.xutils.db.converter;

import android.database.Cursor;
import org.xutils.db.sqlite.ColumnDbType;

public class IntegerColumnConverter implements ColumnConverter<Integer> {
  public Object fieldValue2DbValue(Integer paramInteger) {
    return paramInteger;
  }
  
  public ColumnDbType getColumnDbType() {
    return ColumnDbType.INTEGER;
  }
  
  public Integer getFieldValue(Cursor paramCursor, int paramInt) {
    return paramCursor.isNull(paramInt) ? null : Integer.valueOf(paramCursor.getInt(paramInt));
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/converter/IntegerColumnConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */