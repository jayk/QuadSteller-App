package org.greenrobot.greendao.database;

import android.database.Cursor;
import android.database.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

public class EncryptedDatabase implements Database {
  private final SQLiteDatabase delegate;
  
  public EncryptedDatabase(SQLiteDatabase paramSQLiteDatabase) {
    this.delegate = paramSQLiteDatabase;
  }
  
  public void beginTransaction() {
    this.delegate.beginTransaction();
  }
  
  public void close() {
    this.delegate.close();
  }
  
  public DatabaseStatement compileStatement(String paramString) {
    return new EncryptedDatabaseStatement(this.delegate.compileStatement(paramString));
  }
  
  public void endTransaction() {
    this.delegate.endTransaction();
  }
  
  public void execSQL(String paramString) throws SQLException {
    this.delegate.execSQL(paramString);
  }
  
  public void execSQL(String paramString, Object[] paramArrayOfObject) throws SQLException {
    this.delegate.execSQL(paramString, paramArrayOfObject);
  }
  
  public Object getRawDatabase() {
    return this.delegate;
  }
  
  public SQLiteDatabase getSQLiteDatabase() {
    return this.delegate;
  }
  
  public boolean inTransaction() {
    return this.delegate.inTransaction();
  }
  
  public boolean isDbLockedByCurrentThread() {
    return this.delegate.isDbLockedByCurrentThread();
  }
  
  public Cursor rawQuery(String paramString, String[] paramArrayOfString) {
    return (Cursor)this.delegate.rawQuery(paramString, paramArrayOfString);
  }
  
  public void setTransactionSuccessful() {
    this.delegate.setTransactionSuccessful();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/database/EncryptedDatabase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */