package org.xutils.db.converter;

import android.database.Cursor;
import org.xutils.db.sqlite.ColumnDbType;

public class ByteArrayColumnConverter implements ColumnConverter<byte[]> {
  public Object fieldValue2DbValue(byte[] paramArrayOfbyte) {
    return paramArrayOfbyte;
  }
  
  public ColumnDbType getColumnDbType() {
    return ColumnDbType.BLOB;
  }
  
  public byte[] getFieldValue(Cursor paramCursor, int paramInt) {
    return paramCursor.isNull(paramInt) ? null : paramCursor.getBlob(paramInt);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/converter/ByteArrayColumnConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */