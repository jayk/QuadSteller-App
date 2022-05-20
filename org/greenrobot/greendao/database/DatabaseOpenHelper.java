package org.greenrobot.greendao.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

public abstract class DatabaseOpenHelper extends SQLiteOpenHelper {
  private final Context context;
  
  private EncryptedHelper encryptedHelper;
  
  private boolean loadSQLCipherNativeLibs = true;
  
  private final String name;
  
  private final int version;
  
  public DatabaseOpenHelper(Context paramContext, String paramString, int paramInt) {
    this(paramContext, paramString, (SQLiteDatabase.CursorFactory)null, paramInt);
  }
  
  public DatabaseOpenHelper(Context paramContext, String paramString, SQLiteDatabase.CursorFactory paramCursorFactory, int paramInt) {
    super(paramContext, paramString, paramCursorFactory, paramInt);
    this.context = paramContext;
    this.name = paramString;
    this.version = paramInt;
  }
  
  private EncryptedHelper checkEncryptedHelper() {
    if (this.encryptedHelper == null)
      this.encryptedHelper = new EncryptedHelper(this.context, this.name, this.version, this.loadSQLCipherNativeLibs); 
    return this.encryptedHelper;
  }
  
  public Database getEncryptedReadableDb(String paramString) {
    EncryptedHelper encryptedHelper = checkEncryptedHelper();
    return encryptedHelper.wrap(encryptedHelper.getReadableDatabase(paramString));
  }
  
  public Database getEncryptedReadableDb(char[] paramArrayOfchar) {
    EncryptedHelper encryptedHelper = checkEncryptedHelper();
    return encryptedHelper.wrap(encryptedHelper.getReadableDatabase(paramArrayOfchar));
  }
  
  public Database getEncryptedWritableDb(String paramString) {
    EncryptedHelper encryptedHelper = checkEncryptedHelper();
    return encryptedHelper.wrap(encryptedHelper.getReadableDatabase(paramString));
  }
  
  public Database getEncryptedWritableDb(char[] paramArrayOfchar) {
    EncryptedHelper encryptedHelper = checkEncryptedHelper();
    return encryptedHelper.wrap(encryptedHelper.getWritableDatabase(paramArrayOfchar));
  }
  
  public Database getReadableDb() {
    return wrap(getReadableDatabase());
  }
  
  public Database getWritableDb() {
    return wrap(getWritableDatabase());
  }
  
  public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
    onCreate(wrap(paramSQLiteDatabase));
  }
  
  public void onCreate(Database paramDatabase) {}
  
  public void onOpen(SQLiteDatabase paramSQLiteDatabase) {
    onOpen(wrap(paramSQLiteDatabase));
  }
  
  public void onOpen(Database paramDatabase) {}
  
  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {
    onUpgrade(wrap(paramSQLiteDatabase), paramInt1, paramInt2);
  }
  
  public void onUpgrade(Database paramDatabase, int paramInt1, int paramInt2) {}
  
  public void setLoadSQLCipherNativeLibs(boolean paramBoolean) {
    this.loadSQLCipherNativeLibs = paramBoolean;
  }
  
  protected Database wrap(SQLiteDatabase paramSQLiteDatabase) {
    return new StandardDatabase(paramSQLiteDatabase);
  }
  
  private class EncryptedHelper extends SQLiteOpenHelper {
    public EncryptedHelper(Context param1Context, String param1String, int param1Int, boolean param1Boolean) {
      super(param1Context, param1String, null, param1Int);
      if (param1Boolean)
        SQLiteDatabase.loadLibs(param1Context); 
    }
    
    public void onCreate(SQLiteDatabase param1SQLiteDatabase) {
      DatabaseOpenHelper.this.onCreate(wrap(param1SQLiteDatabase));
    }
    
    public void onOpen(SQLiteDatabase param1SQLiteDatabase) {
      DatabaseOpenHelper.this.onOpen(wrap(param1SQLiteDatabase));
    }
    
    public void onUpgrade(SQLiteDatabase param1SQLiteDatabase, int param1Int1, int param1Int2) {
      DatabaseOpenHelper.this.onUpgrade(wrap(param1SQLiteDatabase), param1Int1, param1Int2);
    }
    
    protected Database wrap(SQLiteDatabase param1SQLiteDatabase) {
      return new EncryptedDatabase(param1SQLiteDatabase);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/database/DatabaseOpenHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */