package org.greenrobot.greendao.database;

import android.database.Cursor;
import android.database.SQLException;

public interface Database {
  void beginTransaction();
  
  void close();
  
  DatabaseStatement compileStatement(String paramString);
  
  void endTransaction();
  
  void execSQL(String paramString) throws SQLException;
  
  void execSQL(String paramString, Object[] paramArrayOfObject) throws SQLException;
  
  Object getRawDatabase();
  
  boolean inTransaction();
  
  boolean isDbLockedByCurrentThread();
  
  Cursor rawQuery(String paramString, String[] paramArrayOfString);
  
  void setTransactionSuccessful();
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/database/Database.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */