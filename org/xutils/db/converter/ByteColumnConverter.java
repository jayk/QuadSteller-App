package org.xutils.db.converter;

import android.database.Cursor;
import org.xutils.db.sqlite.ColumnDbType;

public class ByteColumnConverter implements ColumnConverter<Byte> {
  public Object fieldValue2DbValue(Byte paramByte) {
    return paramByte;
  }
  
  public ColumnDbType getColumnDbType() {
    return ColumnDbType.INTEGER;
  }
  
  public Byte getFieldValue(Cursor paramCursor, int paramInt) {
    return paramCursor.isNull(paramInt) ? null : Byte.valueOf((byte)paramCursor.getInt(paramInt));
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/converter/ByteColumnConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */