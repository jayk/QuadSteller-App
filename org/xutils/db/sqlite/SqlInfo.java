package org.xutils.db.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import java.util.ArrayList;
import java.util.List;
import org.xutils.common.util.KeyValue;
import org.xutils.db.converter.ColumnConverterFactory;
import org.xutils.db.table.ColumnUtils;

public final class SqlInfo {
  private List<KeyValue> bindArgs;
  
  private String sql;
  
  public SqlInfo() {}
  
  public SqlInfo(String paramString) {
    this.sql = paramString;
  }
  
  public void addBindArg(KeyValue paramKeyValue) {
    if (this.bindArgs == null)
      this.bindArgs = new ArrayList<KeyValue>(); 
    this.bindArgs.add(paramKeyValue);
  }
  
  public void addBindArgs(List<KeyValue> paramList) {
    if (this.bindArgs == null) {
      this.bindArgs = paramList;
      return;
    } 
    this.bindArgs.addAll(paramList);
  }
  
  public SQLiteStatement buildStatement(SQLiteDatabase paramSQLiteDatabase) {
    SQLiteStatement sQLiteStatement = paramSQLiteDatabase.compileStatement(this.sql);
    if (this.bindArgs != null) {
      byte b = 1;
      while (true) {
        if (b < this.bindArgs.size() + 1) {
          Object object = ColumnUtils.convert2DbValueIfNeeded(((KeyValue)this.bindArgs.get(b - 1)).value);
          if (object == null) {
            sQLiteStatement.bindNull(b);
          } else {
            ColumnDbType columnDbType = ColumnConverterFactory.getColumnConverter(object.getClass()).getColumnDbType();
            switch (columnDbType) {
              case INTEGER:
                sQLiteStatement.bindLong(b, ((Number)object).longValue());
                b++;
                break;
              case REAL:
                sQLiteStatement.bindDouble(b, ((Number)object).doubleValue());
                b++;
                break;
              case TEXT:
                sQLiteStatement.bindString(b, object.toString());
                b++;
                break;
              case BLOB:
                sQLiteStatement.bindBlob(b, (byte[])object);
                b++;
                break;
            } 
            continue;
          } 
        } else {
          break;
        } 
        b++;
        break;
      } 
    } 
    return sQLiteStatement;
  }
  
  public Object[] getBindArgs() {
    Object[] arrayOfObject = null;
    if (this.bindArgs != null) {
      Object[] arrayOfObject1 = new Object[this.bindArgs.size()];
      byte b = 0;
      while (true) {
        arrayOfObject = arrayOfObject1;
        if (b < this.bindArgs.size()) {
          arrayOfObject1[b] = ColumnUtils.convert2DbValueIfNeeded(((KeyValue)this.bindArgs.get(b)).value);
          b++;
          continue;
        } 
        break;
      } 
    } 
    return arrayOfObject;
  }
  
  public String[] getBindArgsAsStrArray() {
    Object object = null;
    if (this.bindArgs != null) {
      String[] arrayOfString = new String[this.bindArgs.size()];
      byte b = 0;
      while (true) {
        object = arrayOfString;
        if (b < this.bindArgs.size()) {
          object = ColumnUtils.convert2DbValueIfNeeded(((KeyValue)this.bindArgs.get(b)).value);
          if (object == null) {
            object = null;
          } else {
            object = object.toString();
          } 
          arrayOfString[b] = (String)object;
          b++;
          continue;
        } 
        break;
      } 
    } 
    return (String[])object;
  }
  
  public String getSql() {
    return this.sql;
  }
  
  public void setSql(String paramString) {
    this.sql = paramString;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/sqlite/SqlInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */