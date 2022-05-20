package org.xutils.db.converter;

import android.database.Cursor;
import java.util.Date;
import org.xutils.db.sqlite.ColumnDbType;

public class DateColumnConverter implements ColumnConverter<Date> {
  public Object fieldValue2DbValue(Date paramDate) {
    return (paramDate == null) ? null : Long.valueOf(paramDate.getTime());
  }
  
  public ColumnDbType getColumnDbType() {
    return ColumnDbType.INTEGER;
  }
  
  public Date getFieldValue(Cursor paramCursor, int paramInt) {
    return paramCursor.isNull(paramInt) ? null : new Date(paramCursor.getLong(paramInt));
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/converter/DateColumnConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */