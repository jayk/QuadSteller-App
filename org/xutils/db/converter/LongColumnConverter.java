package org.xutils.db.converter;

import android.database.Cursor;
import org.xutils.db.sqlite.ColumnDbType;

public class LongColumnConverter implements ColumnConverter<Long> {
  public Object fieldValue2DbValue(Long paramLong) {
    return paramLong;
  }
  
  public ColumnDbType getColumnDbType() {
    return ColumnDbType.INTEGER;
  }
  
  public Long getFieldValue(Cursor paramCursor, int paramInt) {
    return paramCursor.isNull(paramInt) ? null : Long.valueOf(paramCursor.getLong(paramInt));
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/converter/LongColumnConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */