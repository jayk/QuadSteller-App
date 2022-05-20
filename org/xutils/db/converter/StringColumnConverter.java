package org.xutils.db.converter;

import android.database.Cursor;
import org.xutils.db.sqlite.ColumnDbType;

public class StringColumnConverter implements ColumnConverter<String> {
  public Object fieldValue2DbValue(String paramString) {
    return paramString;
  }
  
  public ColumnDbType getColumnDbType() {
    return ColumnDbType.TEXT;
  }
  
  public String getFieldValue(Cursor paramCursor, int paramInt) {
    return paramCursor.isNull(paramInt) ? null : paramCursor.getString(paramInt);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/converter/StringColumnConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */