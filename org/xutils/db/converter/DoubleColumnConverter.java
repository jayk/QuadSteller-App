package org.xutils.db.converter;

import android.database.Cursor;
import org.xutils.db.sqlite.ColumnDbType;

public class DoubleColumnConverter implements ColumnConverter<Double> {
  public Object fieldValue2DbValue(Double paramDouble) {
    return paramDouble;
  }
  
  public ColumnDbType getColumnDbType() {
    return ColumnDbType.REAL;
  }
  
  public Double getFieldValue(Cursor paramCursor, int paramInt) {
    return paramCursor.isNull(paramInt) ? null : Double.valueOf(paramCursor.getDouble(paramInt));
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/converter/DoubleColumnConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */