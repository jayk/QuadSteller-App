package app.gamer.quadstellar.domain;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class FanDeviceInfoDao extends AbstractDao<FanDeviceInfo, Long> {
  public static final String TABLENAME = "FAN_DEVICE_INFO";
  
  public FanDeviceInfoDao(DaoConfig paramDaoConfig) {
    super(paramDaoConfig);
  }
  
  public FanDeviceInfoDao(DaoConfig paramDaoConfig, DaoSession paramDaoSession) {
    super(paramDaoConfig, paramDaoSession);
  }
  
  public static void createTable(Database paramDatabase, boolean paramBoolean) {
    String str;
    if (paramBoolean) {
      str = "IF NOT EXISTS ";
    } else {
      str = "";
    } 
    paramDatabase.execSQL("CREATE TABLE " + str + "\"FAN_DEVICE_INFO\" (\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ,\"MAC_ADRASS\" TEXT,\"SON_FAN_NUMBER\" INTEGER NOT NULL ,\"SON_FAN_MODE\" INTEGER NOT NULL ,\"SET_SON_FAN_SPEED\" INTEGER NOT NULL ,\"REALITY_SON_FAN_SPEED\" INTEGER NOT NULL );");
  }
  
  public static void dropTable(Database paramDatabase, boolean paramBoolean) {
    String str;
    StringBuilder stringBuilder = (new StringBuilder()).append("DROP TABLE ");
    if (paramBoolean) {
      str = "IF EXISTS ";
    } else {
      str = "";
    } 
    paramDatabase.execSQL(stringBuilder.append(str).append("\"FAN_DEVICE_INFO\"").toString());
  }
  
  protected final void bindValues(SQLiteStatement paramSQLiteStatement, FanDeviceInfo paramFanDeviceInfo) {
    paramSQLiteStatement.clearBindings();
    Long long_ = paramFanDeviceInfo.getId();
    if (long_ != null)
      paramSQLiteStatement.bindLong(1, long_.longValue()); 
    String str = paramFanDeviceInfo.getMacAdrass();
    if (str != null)
      paramSQLiteStatement.bindString(2, str); 
    paramSQLiteStatement.bindLong(3, paramFanDeviceInfo.getSonFanNumber());
    paramSQLiteStatement.bindLong(4, paramFanDeviceInfo.getSonFanMode());
    paramSQLiteStatement.bindLong(5, paramFanDeviceInfo.getSetSonFanSpeed());
    paramSQLiteStatement.bindLong(6, paramFanDeviceInfo.getRealitySonFanSpeed());
  }
  
  protected final void bindValues(DatabaseStatement paramDatabaseStatement, FanDeviceInfo paramFanDeviceInfo) {
    paramDatabaseStatement.clearBindings();
    Long long_ = paramFanDeviceInfo.getId();
    if (long_ != null)
      paramDatabaseStatement.bindLong(1, long_.longValue()); 
    String str = paramFanDeviceInfo.getMacAdrass();
    if (str != null)
      paramDatabaseStatement.bindString(2, str); 
    paramDatabaseStatement.bindLong(3, paramFanDeviceInfo.getSonFanNumber());
    paramDatabaseStatement.bindLong(4, paramFanDeviceInfo.getSonFanMode());
    paramDatabaseStatement.bindLong(5, paramFanDeviceInfo.getSetSonFanSpeed());
    paramDatabaseStatement.bindLong(6, paramFanDeviceInfo.getRealitySonFanSpeed());
  }
  
  public Long getKey(FanDeviceInfo paramFanDeviceInfo) {
    return (paramFanDeviceInfo != null) ? paramFanDeviceInfo.getId() : null;
  }
  
  public boolean hasKey(FanDeviceInfo paramFanDeviceInfo) {
    return (paramFanDeviceInfo.getId() != null);
  }
  
  protected final boolean isEntityUpdateable() {
    return true;
  }
  
  public FanDeviceInfo readEntity(Cursor paramCursor, int paramInt) {
    Long long_;
    String str = null;
    if (paramCursor.isNull(paramInt + 0)) {
      long_ = null;
    } else {
      long_ = Long.valueOf(paramCursor.getLong(paramInt + 0));
    } 
    if (!paramCursor.isNull(paramInt + 1))
      str = paramCursor.getString(paramInt + 1); 
    return new FanDeviceInfo(long_, str, paramCursor.getInt(paramInt + 2), paramCursor.getInt(paramInt + 3), paramCursor.getInt(paramInt + 4), paramCursor.getInt(paramInt + 5));
  }
  
  public void readEntity(Cursor paramCursor, FanDeviceInfo paramFanDeviceInfo, int paramInt) {
    Long long_2;
    String str;
    Long long_1 = null;
    if (paramCursor.isNull(paramInt + 0)) {
      long_2 = null;
    } else {
      long_2 = Long.valueOf(paramCursor.getLong(paramInt + 0));
    } 
    paramFanDeviceInfo.setId(long_2);
    if (paramCursor.isNull(paramInt + 1)) {
      long_2 = long_1;
    } else {
      str = paramCursor.getString(paramInt + 1);
    } 
    paramFanDeviceInfo.setMacAdrass(str);
    paramFanDeviceInfo.setSonFanNumber(paramCursor.getInt(paramInt + 2));
    paramFanDeviceInfo.setSonFanMode(paramCursor.getInt(paramInt + 3));
    paramFanDeviceInfo.setSetSonFanSpeed(paramCursor.getInt(paramInt + 4));
    paramFanDeviceInfo.setRealitySonFanSpeed(paramCursor.getInt(paramInt + 5));
  }
  
  public Long readKey(Cursor paramCursor, int paramInt) {
    return paramCursor.isNull(paramInt + 0) ? null : Long.valueOf(paramCursor.getLong(paramInt + 0));
  }
  
  protected final Long updateKeyAfterInsert(FanDeviceInfo paramFanDeviceInfo, long paramLong) {
    paramFanDeviceInfo.setId(Long.valueOf(paramLong));
    return Long.valueOf(paramLong);
  }
  
  public static class Properties {
    public static final Property Id = new Property(0, Long.class, "id", true, "_id");
    
    public static final Property MacAdrass = new Property(1, String.class, "macAdrass", false, "MAC_ADRASS");
    
    public static final Property RealitySonFanSpeed;
    
    public static final Property SetSonFanSpeed;
    
    public static final Property SonFanMode = new Property(3, int.class, "sonFanMode", false, "SON_FAN_MODE");
    
    public static final Property SonFanNumber = new Property(2, int.class, "sonFanNumber", false, "SON_FAN_NUMBER");
    
    static {
      SetSonFanSpeed = new Property(4, int.class, "setSonFanSpeed", false, "SET_SON_FAN_SPEED");
      RealitySonFanSpeed = new Property(5, int.class, "realitySonFanSpeed", false, "REALITY_SON_FAN_SPEED");
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/FanDeviceInfoDao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */