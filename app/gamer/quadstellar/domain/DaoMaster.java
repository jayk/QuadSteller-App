package app.gamer.quadstellar.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.identityscope.IdentityScopeType;

public class DaoMaster extends AbstractDaoMaster {
  public static final int SCHEMA_VERSION = 14;
  
  public DaoMaster(SQLiteDatabase paramSQLiteDatabase) {
    this((Database)new StandardDatabase(paramSQLiteDatabase));
  }
  
  public DaoMaster(Database paramDatabase) {
    super(paramDatabase, 14);
    registerDaoClass(ColorInfoDao.class);
    registerDaoClass(DeviceInfoDao.class);
    registerDaoClass(FanDeviceInfoDao.class);
    registerDaoClass(FatherDeviceInfoDao.class);
    registerDaoClass(LightDeviceInfoDao.class);
  }
  
  public static void createAllTables(Database paramDatabase, boolean paramBoolean) {
    ColorInfoDao.createTable(paramDatabase, paramBoolean);
    DeviceInfoDao.createTable(paramDatabase, paramBoolean);
    FanDeviceInfoDao.createTable(paramDatabase, paramBoolean);
    FatherDeviceInfoDao.createTable(paramDatabase, paramBoolean);
    LightDeviceInfoDao.createTable(paramDatabase, paramBoolean);
  }
  
  public static void dropAllTables(Database paramDatabase, boolean paramBoolean) {
    ColorInfoDao.dropTable(paramDatabase, paramBoolean);
    DeviceInfoDao.dropTable(paramDatabase, paramBoolean);
    FanDeviceInfoDao.dropTable(paramDatabase, paramBoolean);
    FatherDeviceInfoDao.dropTable(paramDatabase, paramBoolean);
    LightDeviceInfoDao.dropTable(paramDatabase, paramBoolean);
  }
  
  public static DaoSession newDevSession(Context paramContext, String paramString) {
    return (new DaoMaster((new DevOpenHelper(paramContext, paramString)).getWritableDb())).newSession();
  }
  
  public DaoSession newSession() {
    return new DaoSession(this.db, IdentityScopeType.Session, this.daoConfigMap);
  }
  
  public DaoSession newSession(IdentityScopeType paramIdentityScopeType) {
    return new DaoSession(this.db, paramIdentityScopeType, this.daoConfigMap);
  }
  
  public static class DevOpenHelper extends OpenHelper {
    public DevOpenHelper(Context param1Context, String param1String) {
      super(param1Context, param1String);
    }
    
    public DevOpenHelper(Context param1Context, String param1String, SQLiteDatabase.CursorFactory param1CursorFactory) {
      super(param1Context, param1String, param1CursorFactory);
    }
    
    public void onUpgrade(Database param1Database, int param1Int1, int param1Int2) {
      Log.i("greenDAO", "Upgrading schema from version " + param1Int1 + " to " + param1Int2 + " by dropping all tables");
      DaoMaster.dropAllTables(param1Database, true);
      onCreate(param1Database);
    }
  }
  
  public static abstract class OpenHelper extends DatabaseOpenHelper {
    public OpenHelper(Context param1Context, String param1String) {
      super(param1Context, param1String, 14);
    }
    
    public OpenHelper(Context param1Context, String param1String, SQLiteDatabase.CursorFactory param1CursorFactory) {
      super(param1Context, param1String, param1CursorFactory, 14);
    }
    
    public void onCreate(Database param1Database) {
      Log.i("greenDAO", "Creating tables for schema version 14");
      DaoMaster.createAllTables(param1Database, false);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/DaoMaster.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */