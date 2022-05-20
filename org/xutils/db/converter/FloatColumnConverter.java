package org.xutils.db.converter;

import android.database.Cursor;
import org.xutils.db.sqlite.ColumnDbType;

public class FloatColumnConverter implements ColumnConverter<Float> {
  public Object fieldValue2DbValue(Float paramFloat) {
    return paramFloat;
  }
  
  public ColumnDbType getColumnDbType() {
    return ColumnDbType.REAL;
  }
  
  public Float getFieldValue(Cursor paramCursor, int paramInt) {
    return paramCursor.isNull(paramInt) ? null : Float.valueOf(paramCursor.getFloat(paramInt));
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/converter/FloatColumnConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */