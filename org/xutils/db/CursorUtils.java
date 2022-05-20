package org.xutils.db;

import android.database.Cursor;
import java.util.LinkedHashMap;
import org.xutils.db.table.ColumnEntity;
import org.xutils.db.table.DbModel;
import org.xutils.db.table.TableEntity;

final class CursorUtils {
  public static DbModel getDbModel(Cursor paramCursor) {
    DbModel dbModel = new DbModel();
    int i = paramCursor.getColumnCount();
    for (byte b = 0; b < i; b++)
      dbModel.add(paramCursor.getColumnName(b), paramCursor.getString(b)); 
    return dbModel;
  }
  
  public static <T> T getEntity(TableEntity<T> paramTableEntity, Cursor paramCursor) throws Throwable {
    Object object = paramTableEntity.createEntity();
    LinkedHashMap linkedHashMap = paramTableEntity.getColumnMap();
    int i = paramCursor.getColumnCount();
    for (byte b = 0; b < i; b++) {
      ColumnEntity columnEntity = (ColumnEntity)linkedHashMap.get(paramCursor.getColumnName(b));
      if (columnEntity != null)
        columnEntity.setValueFromCursor(object, paramCursor, b); 
    } 
    return (T)object;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/CursorUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */