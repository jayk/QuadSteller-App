package org.xutils.db.converter;

import android.database.Cursor;
import org.xutils.db.sqlite.ColumnDbType;

public interface ColumnConverter<T> {
  Object fieldValue2DbValue(T paramT);
  
  ColumnDbType getColumnDbType();
  
  T getFieldValue(Cursor paramCursor, int paramInt);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/converter/ColumnConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */