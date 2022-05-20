package app.gamer.quadstellar.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import app.gamer.quadstellar.db.WifiMessageDB;
import app.gamer.quadstellar.domain.WifiMessageInfo;

public class WifiMessageDao {
  private WifiMessageDB mBDB;
  
  public WifiMessageDao(Context paramContext) {
    this.mBDB = new WifiMessageDB(paramContext);
  }
  
  public void delete(String paramString) {
    SQLiteDatabase sQLiteDatabase = this.mBDB.getReadableDatabase();
    sQLiteDatabase.delete("info", "name=?", new String[] { paramString });
    sQLiteDatabase.close();
  }
  
  public boolean insert(WifiMessageInfo paramWifiMessageInfo) {
    SQLiteDatabase sQLiteDatabase = this.mBDB.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("name", paramWifiMessageInfo.name);
    contentValues.put("password", paramWifiMessageInfo.passWord);
    long l = sQLiteDatabase.insert("info", null, contentValues);
    sQLiteDatabase.close();
    return (l != -1L);
  }
  
  public String queryType(String paramString) {
    SQLiteDatabase sQLiteDatabase = this.mBDB.getReadableDatabase();
    Cursor cursor = sQLiteDatabase.query("info", new String[] { "password" }, "name =?", new String[] { paramString }, null, null, null);
    paramString = null;
    String str = null;
    if (cursor != null)
      for (paramString = str; cursor.moveToNext(); paramString = cursor.getString(0)); 
    sQLiteDatabase.close();
    cursor.close();
    return paramString;
  }
  
  public void updata(WifiMessageInfo paramWifiMessageInfo) {
    SQLiteDatabase sQLiteDatabase = this.mBDB.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("password", paramWifiMessageInfo.passWord);
    sQLiteDatabase.update("info", contentValues, "name=?", new String[] { paramWifiMessageInfo.name });
    sQLiteDatabase.close();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/dao/WifiMessageDao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */