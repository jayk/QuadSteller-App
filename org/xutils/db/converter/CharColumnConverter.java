package org.xutils.db.converter;

import android.database.Cursor;
import org.xutils.db.sqlite.ColumnDbType;

public class CharColumnConverter implements ColumnConverter<Character> {
  public Object fieldValue2DbValue(Character paramCharacter) {
    return (paramCharacter == null) ? null : Integer.valueOf(paramCharacter.charValue());
  }
  
  public ColumnDbType getColumnDbType() {
    return ColumnDbType.INTEGER;
  }
  
  public Character getFieldValue(Cursor paramCursor, int paramInt) {
    return paramCursor.isNull(paramInt) ? null : Character.valueOf((char)paramCursor.getInt(paramInt));
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/converter/CharColumnConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */