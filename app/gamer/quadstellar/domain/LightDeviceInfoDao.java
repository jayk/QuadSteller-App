package app.gamer.quadstellar.domain;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class LightDeviceInfoDao extends AbstractDao<LightDeviceInfo, Long> {
  public static final String TABLENAME = "LIGHT_DEVICE_INFO";
  
  public LightDeviceInfoDao(DaoConfig paramDaoConfig) {
    super(paramDaoConfig);
  }
  
  public LightDeviceInfoDao(DaoConfig paramDaoConfig, DaoSession paramDaoSession) {
    super(paramDaoConfig, paramDaoSession);
  }
  
  public static void createTable(Database paramDatabase, boolean paramBoolean) {
    String str;
    if (paramBoolean) {
      str = "IF NOT EXISTS ";
    } else {
      str = "";
    } 
    paramDatabase.execSQL("CREATE TABLE " + str + "\"LIGHT_DEVICE_INFO\" (\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ,\"MAC_ADRASS\" TEXT,\"SON_LIGHT_NUMBER\" INTEGER NOT NULL ,\"SON_LIGHT_STATE\" INTEGER NOT NULL ,\"SON_LIGHT_MODE\" INTEGER NOT NULL ,\"COLOR_R\" INTEGER NOT NULL ,\"COLOR_G\" INTEGER NOT NULL ,\"COLOR_B\" INTEGER NOT NULL ,\"SON_BRIGHTNESS\" INTEGER NOT NULL ,\"SON_LIGHT_SPEED\" INTEGER NOT NULL ,\"SON_LIGHT_FINENESS\" INTEGER NOT NULL ,\"SON_LIGHT_COLOR\" INTEGER NOT NULL );");
  }
  
  public static void dropTable(Database paramDatabase, boolean paramBoolean) {
    String str;
    StringBuilder stringBuilder = (new StringBuilder()).append("DROP TABLE ");
    if (paramBoolean) {
      str = "IF EXISTS ";
    } else {
      str = "";
    } 
    paramDatabase.execSQL(stringBuilder.append(str).append("\"LIGHT_DEVICE_INFO\"").toString());
  }
  
  protected final void bindValues(SQLiteStatement paramSQLiteStatement, LightDeviceInfo paramLightDeviceInfo) {
    paramSQLiteStatement.clearBindings();
    Long long_ = paramLightDeviceInfo.getId();
    if (long_ != null)
      paramSQLiteStatement.bindLong(1, long_.longValue()); 
    String str = paramLightDeviceInfo.getMacAdrass();
    if (str != null)
      paramSQLiteStatement.bindString(2, str); 
    paramSQLiteStatement.bindLong(3, paramLightDeviceInfo.getSonLightNumber());
    paramSQLiteStatement.bindLong(4, paramLightDeviceInfo.getSonLightState());
    paramSQLiteStatement.bindLong(5, paramLightDeviceInfo.getSonLightMode());
    paramSQLiteStatement.bindLong(6, paramLightDeviceInfo.getColorR());
    paramSQLiteStatement.bindLong(7, paramLightDeviceInfo.getColorG());
    paramSQLiteStatement.bindLong(8, paramLightDeviceInfo.getColorB());
    paramSQLiteStatement.bindLong(9, paramLightDeviceInfo.getSonBrightness());
    paramSQLiteStatement.bindLong(10, paramLightDeviceInfo.getSonLightSpeed());
    paramSQLiteStatement.bindLong(11, paramLightDeviceInfo.getSonLightFineness());
    paramSQLiteStatement.bindLong(12, paramLightDeviceInfo.getSonLightColor());
  }
  
  protected final void bindValues(DatabaseStatement paramDatabaseStatement, LightDeviceInfo paramLightDeviceInfo) {
    paramDatabaseStatement.clearBindings();
    Long long_ = paramLightDeviceInfo.getId();
    if (long_ != null)
      paramDatabaseStatement.bindLong(1, long_.longValue()); 
    String str = paramLightDeviceInfo.getMacAdrass();
    if (str != null)
      paramDatabaseStatement.bindString(2, str); 
    paramDatabaseStatement.bindLong(3, paramLightDeviceInfo.getSonLightNumber());
    paramDatabaseStatement.bindLong(4, paramLightDeviceInfo.getSonLightState());
    paramDatabaseStatement.bindLong(5, paramLightDeviceInfo.getSonLightMode());
    paramDatabaseStatement.bindLong(6, paramLightDeviceInfo.getColorR());
    paramDatabaseStatement.bindLong(7, paramLightDeviceInfo.getColorG());
    paramDatabaseStatement.bindLong(8, paramLightDeviceInfo.getColorB());
    paramDatabaseStatement.bindLong(9, paramLightDeviceInfo.getSonBrightness());
    paramDatabaseStatement.bindLong(10, paramLightDeviceInfo.getSonLightSpeed());
    paramDatabaseStatement.bindLong(11, paramLightDeviceInfo.getSonLightFineness());
    paramDatabaseStatement.bindLong(12, paramLightDeviceInfo.getSonLightColor());
  }
  
  public Long getKey(LightDeviceInfo paramLightDeviceInfo) {
    return (paramLightDeviceInfo != null) ? paramLightDeviceInfo.getId() : null;
  }
  
  public boolean hasKey(LightDeviceInfo paramLightDeviceInfo) {
    return (paramLightDeviceInfo.getId() != null);
  }
  
  protected final boolean isEntityUpdateable() {
    return true;
  }
  
  public LightDeviceInfo readEntity(Cursor paramCursor, int paramInt) {
    Long long_;
    String str = null;
    if (paramCursor.isNull(paramInt + 0)) {
      long_ = null;
    } else {
      long_ = Long.valueOf(paramCursor.getLong(paramInt + 0));
    } 
    if (!paramCursor.isNull(paramInt + 1))
      str = paramCursor.getString(paramInt + 1); 
    return new LightDeviceInfo(long_, str, paramCursor.getInt(paramInt + 2), paramCursor.getInt(paramInt + 3), paramCursor.getInt(paramInt + 4), paramCursor.getInt(paramInt + 5), paramCursor.getInt(paramInt + 6), paramCursor.getInt(paramInt + 7), paramCursor.getInt(paramInt + 8), paramCursor.getInt(paramInt + 9), paramCursor.getInt(paramInt + 10), paramCursor.getInt(paramInt + 11));
  }
  
  public void readEntity(Cursor paramCursor, LightDeviceInfo paramLightDeviceInfo, int paramInt) {
    Long long_2;
    String str;
    Long long_1 = null;
    if (paramCursor.isNull(paramInt + 0)) {
      long_2 = null;
    } else {
      long_2 = Long.valueOf(paramCursor.getLong(paramInt + 0));
    } 
    paramLightDeviceInfo.setId(long_2);
    if (paramCursor.isNull(paramInt + 1)) {
      long_2 = long_1;
    } else {
      str = paramCursor.getString(paramInt + 1);
    } 
    paramLightDeviceInfo.setMacAdrass(str);
    paramLightDeviceInfo.setSonLightNumber(paramCursor.getInt(paramInt + 2));
    paramLightDeviceInfo.setSonLightState(paramCursor.getInt(paramInt + 3));
    paramLightDeviceInfo.setSonLightMode(paramCursor.getInt(paramInt + 4));
    paramLightDeviceInfo.setColorR(paramCursor.getInt(paramInt + 5));
    paramLightDeviceInfo.setColorG(paramCursor.getInt(paramInt + 6));
    paramLightDeviceInfo.setColorB(paramCursor.getInt(paramInt + 7));
    paramLightDeviceInfo.setSonBrightness(paramCursor.getInt(paramInt + 8));
    paramLightDeviceInfo.setSonLightSpeed(paramCursor.getInt(paramInt + 9));
    paramLightDeviceInfo.setSonLightFineness(paramCursor.getInt(paramInt + 10));
    paramLightDeviceInfo.setSonLightColor(paramCursor.getInt(paramInt + 11));
  }
  
  public Long readKey(Cursor paramCursor, int paramInt) {
    return paramCursor.isNull(paramInt + 0) ? null : Long.valueOf(paramCursor.getLong(paramInt + 0));
  }
  
  protected final Long updateKeyAfterInsert(LightDeviceInfo paramLightDeviceInfo, long paramLong) {
    paramLightDeviceInfo.setId(Long.valueOf(paramLong));
    return Long.valueOf(paramLong);
  }
  
  public static class Properties {
    public static final Property ColorB;
    
    public static final Property ColorG;
    
    public static final Property ColorR;
    
    public static final Property Id = new Property(0, Long.class, "id", true, "_id");
    
    public static final Property MacAdrass = new Property(1, String.class, "macAdrass", false, "MAC_ADRASS");
    
    public static final Property SonBrightness;
    
    public static final Property SonLightColor;
    
    public static final Property SonLightFineness;
    
    public static final Property SonLightMode = new Property(4, int.class, "sonLightMode", false, "SON_LIGHT_MODE");
    
    public static final Property SonLightNumber = new Property(2, int.class, "sonLightNumber", false, "SON_LIGHT_NUMBER");
    
    public static final Property SonLightSpeed;
    
    public static final Property SonLightState = new Property(3, int.class, "sonLightState", false, "SON_LIGHT_STATE");
    
    static {
      ColorR = new Property(5, int.class, "colorR", false, "COLOR_R");
      ColorG = new Property(6, int.class, "colorG", false, "COLOR_G");
      ColorB = new Property(7, int.class, "colorB", false, "COLOR_B");
      SonBrightness = new Property(8, int.class, "sonBrightness", false, "SON_BRIGHTNESS");
      SonLightSpeed = new Property(9, int.class, "sonLightSpeed", false, "SON_LIGHT_SPEED");
      SonLightFineness = new Property(10, int.class, "sonLightFineness", false, "SON_LIGHT_FINENESS");
      SonLightColor = new Property(11, int.class, "sonLightColor", false, "SON_LIGHT_COLOR");
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/LightDeviceInfoDao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */