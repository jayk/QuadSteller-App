package app.gamer.quadstellar.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WifiMessageDB extends SQLiteOpenHelper {
  public static final String WIFI_MESSAGE_NAME = "info";
  
  public static final String WIFI_NAME = "name";
  
  public static final String WIFI_PASSWORD = "password";
  
  public WifiMessageDB(Context paramContext) {
    super(paramContext, "wifi_message.db", null, 1);
  }
  
  public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
    paramSQLiteDatabase.execSQL("create table info( _id integer primary key autoincrement ,name varchar,password varchar )");
  }
  
  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {}
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/db/WifiMessageDB.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */