package app.gamer.quadstellar.domain;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class DeviceInfoDao extends AbstractDao<DeviceInfo, Long> {
  public static final String TABLENAME = "DEVICE_INFO";
  
  public DeviceInfoDao(DaoConfig paramDaoConfig) {
    super(paramDaoConfig);
  }
  
  public DeviceInfoDao(DaoConfig paramDaoConfig, DaoSession paramDaoSession) {
    super(paramDaoConfig, paramDaoSession);
  }
  
  public static void createTable(Database paramDatabase, boolean paramBoolean) {
    String str;
    if (paramBoolean) {
      str = "IF NOT EXISTS ";
    } else {
      str = "";
    } 
    paramDatabase.execSQL("CREATE TABLE " + str + "\"DEVICE_INFO\" (\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ,\"PERMISSION\" INTEGER NOT NULL ,\"MAC_DRASS\" TEXT,\"DEVICE_VERSION\" TEXT,\"UUID\" TEXT,\"TOGGLE_STATE\" INTEGER NOT NULL ,\"COLOR_MODE\" INTEGER NOT NULL ,\"COLOR_R\" INTEGER NOT NULL ,\"COLOR_G\" INTEGER NOT NULL ,\"COLOR_B\" INTEGER NOT NULL ,\"COLOR_LUMINANCE\" INTEGER NOT NULL ,\"COLOR_SPEED\" INTEGER NOT NULL ,\"FAN_MODE\" INTEGER NOT NULL ,\"FAN_LOW_SPEED\" INTEGER NOT NULL ,\"FAN_MAX_SPEED\" INTEGER NOT NULL ,\"FAN_NOMAL_SPEED\" INTEGER NOT NULL ,\"DEVICE_IP\" TEXT,\"NAME\" TEXT,\"IS_ONLINE\" INTEGER NOT NULL );");
  }
  
  public static void dropTable(Database paramDatabase, boolean paramBoolean) {
    String str;
    StringBuilder stringBuilder = (new StringBuilder()).append("DROP TABLE ");
    if (paramBoolean) {
      str = "IF EXISTS ";
    } else {
      str = "";
    } 
    paramDatabase.execSQL(stringBuilder.append(str).append("\"DEVICE_INFO\"").toString());
  }
  
  protected final void bindValues(SQLiteStatement paramSQLiteStatement, DeviceInfo paramDeviceInfo) {
    long l;
    paramSQLiteStatement.clearBindings();
    Long long_ = paramDeviceInfo.getId();
    if (long_ != null)
      paramSQLiteStatement.bindLong(1, long_.longValue()); 
    paramSQLiteStatement.bindLong(2, paramDeviceInfo.getPermission());
    String str = paramDeviceInfo.getMacDrass();
    if (str != null)
      paramSQLiteStatement.bindString(3, str); 
    str = paramDeviceInfo.getDeviceVersion();
    if (str != null)
      paramSQLiteStatement.bindString(4, str); 
    str = paramDeviceInfo.getUuid();
    if (str != null)
      paramSQLiteStatement.bindString(5, str); 
    paramSQLiteStatement.bindLong(6, paramDeviceInfo.getToggleState());
    paramSQLiteStatement.bindLong(7, paramDeviceInfo.getColorMode());
    paramSQLiteStatement.bindLong(8, paramDeviceInfo.getColorR());
    paramSQLiteStatement.bindLong(9, paramDeviceInfo.getColorG());
    paramSQLiteStatement.bindLong(10, paramDeviceInfo.getColorB());
    paramSQLiteStatement.bindLong(11, paramDeviceInfo.getColorLuminance());
    paramSQLiteStatement.bindLong(12, paramDeviceInfo.getColorSpeed());
    paramSQLiteStatement.bindLong(13, paramDeviceInfo.getFanMode());
    paramSQLiteStatement.bindLong(14, paramDeviceInfo.getFanLowSpeed());
    paramSQLiteStatement.bindLong(15, paramDeviceInfo.getFanMaxSpeed());
    paramSQLiteStatement.bindLong(16, paramDeviceInfo.getFanNomalSpeed());
    str = paramDeviceInfo.getDeviceIP();
    if (str != null)
      paramSQLiteStatement.bindString(17, str); 
    str = paramDeviceInfo.getName();
    if (str != null)
      paramSQLiteStatement.bindString(18, str); 
    if (paramDeviceInfo.getIsOnline()) {
      l = 1L;
    } else {
      l = 0L;
    } 
    paramSQLiteStatement.bindLong(19, l);
  }
  
  protected final void bindValues(DatabaseStatement paramDatabaseStatement, DeviceInfo paramDeviceInfo) {
    long l;
    paramDatabaseStatement.clearBindings();
    Long long_ = paramDeviceInfo.getId();
    if (long_ != null)
      paramDatabaseStatement.bindLong(1, long_.longValue()); 
    paramDatabaseStatement.bindLong(2, paramDeviceInfo.getPermission());
    String str = paramDeviceInfo.getMacDrass();
    if (str != null)
      paramDatabaseStatement.bindString(3, str); 
    str = paramDeviceInfo.getDeviceVersion();
    if (str != null)
      paramDatabaseStatement.bindString(4, str); 
    str = paramDeviceInfo.getUuid();
    if (str != null)
      paramDatabaseStatement.bindString(5, str); 
    paramDatabaseStatement.bindLong(6, paramDeviceInfo.getToggleState());
    paramDatabaseStatement.bindLong(7, paramDeviceInfo.getColorMode());
    paramDatabaseStatement.bindLong(8, paramDeviceInfo.getColorR());
    paramDatabaseStatement.bindLong(9, paramDeviceInfo.getColorG());
    paramDatabaseStatement.bindLong(10, paramDeviceInfo.getColorB());
    paramDatabaseStatement.bindLong(11, paramDeviceInfo.getColorLuminance());
    paramDatabaseStatement.bindLong(12, paramDeviceInfo.getColorSpeed());
    paramDatabaseStatement.bindLong(13, paramDeviceInfo.getFanMode());
    paramDatabaseStatement.bindLong(14, paramDeviceInfo.getFanLowSpeed());
    paramDatabaseStatement.bindLong(15, paramDeviceInfo.getFanMaxSpeed());
    paramDatabaseStatement.bindLong(16, paramDeviceInfo.getFanNomalSpeed());
    str = paramDeviceInfo.getDeviceIP();
    if (str != null)
      paramDatabaseStatement.bindString(17, str); 
    str = paramDeviceInfo.getName();
    if (str != null)
      paramDatabaseStatement.bindString(18, str); 
    if (paramDeviceInfo.getIsOnline()) {
      l = 1L;
    } else {
      l = 0L;
    } 
    paramDatabaseStatement.bindLong(19, l);
  }
  
  public Long getKey(DeviceInfo paramDeviceInfo) {
    return (paramDeviceInfo != null) ? paramDeviceInfo.getId() : null;
  }
  
  public boolean hasKey(DeviceInfo paramDeviceInfo) {
    return (paramDeviceInfo.getId() != null);
  }
  
  protected final boolean isEntityUpdateable() {
    return true;
  }
  
  public DeviceInfo readEntity(Cursor paramCursor, int paramInt) {
    Long long_;
    String str1;
    String str2;
    String str3;
    String str4;
    String str5;
    if (paramCursor.isNull(paramInt + 0)) {
      long_ = null;
    } else {
      long_ = Long.valueOf(paramCursor.getLong(paramInt + 0));
    } 
    int i = paramCursor.getInt(paramInt + 1);
    if (paramCursor.isNull(paramInt + 2)) {
      str1 = null;
    } else {
      str1 = paramCursor.getString(paramInt + 2);
    } 
    if (paramCursor.isNull(paramInt + 3)) {
      str2 = null;
    } else {
      str2 = paramCursor.getString(paramInt + 3);
    } 
    if (paramCursor.isNull(paramInt + 4)) {
      str3 = null;
    } else {
      str3 = paramCursor.getString(paramInt + 4);
    } 
    int j = paramCursor.getInt(paramInt + 5);
    int k = paramCursor.getInt(paramInt + 6);
    int m = paramCursor.getInt(paramInt + 7);
    int n = paramCursor.getInt(paramInt + 8);
    int i1 = paramCursor.getInt(paramInt + 9);
    int i2 = paramCursor.getInt(paramInt + 10);
    int i3 = paramCursor.getInt(paramInt + 11);
    int i4 = paramCursor.getInt(paramInt + 12);
    int i5 = paramCursor.getInt(paramInt + 13);
    int i6 = paramCursor.getInt(paramInt + 14);
    int i7 = paramCursor.getInt(paramInt + 15);
    if (paramCursor.isNull(paramInt + 16)) {
      str4 = null;
    } else {
      str4 = paramCursor.getString(paramInt + 16);
    } 
    if (paramCursor.isNull(paramInt + 17)) {
      str5 = null;
    } else {
      str5 = paramCursor.getString(paramInt + 17);
    } 
    if (paramCursor.getShort(paramInt + 18) != 0) {
      boolean bool1 = true;
      return new DeviceInfo(long_, i, str1, str2, str3, j, k, m, n, i1, i2, i3, i4, i5, i6, i7, str4, str5, bool1);
    } 
    boolean bool = false;
    return new DeviceInfo(long_, i, str1, str2, str3, j, k, m, n, i1, i2, i3, i4, i5, i6, i7, str4, str5, bool);
  }
  
  public void readEntity(Cursor paramCursor, DeviceInfo paramDeviceInfo, int paramInt) {
    Long long_;
    String str2;
    boolean bool;
    String str1 = null;
    if (paramCursor.isNull(paramInt + 0)) {
      long_ = null;
    } else {
      long_ = Long.valueOf(paramCursor.getLong(paramInt + 0));
    } 
    paramDeviceInfo.setId(long_);
    paramDeviceInfo.setPermission(paramCursor.getInt(paramInt + 1));
    if (paramCursor.isNull(paramInt + 2)) {
      long_ = null;
    } else {
      str2 = paramCursor.getString(paramInt + 2);
    } 
    paramDeviceInfo.setMacDrass(str2);
    if (paramCursor.isNull(paramInt + 3)) {
      str2 = null;
    } else {
      str2 = paramCursor.getString(paramInt + 3);
    } 
    paramDeviceInfo.setDeviceVersion(str2);
    if (paramCursor.isNull(paramInt + 4)) {
      str2 = null;
    } else {
      str2 = paramCursor.getString(paramInt + 4);
    } 
    paramDeviceInfo.setUuid(str2);
    paramDeviceInfo.setToggleState(paramCursor.getInt(paramInt + 5));
    paramDeviceInfo.setColorMode(paramCursor.getInt(paramInt + 6));
    paramDeviceInfo.setColorR(paramCursor.getInt(paramInt + 7));
    paramDeviceInfo.setColorG(paramCursor.getInt(paramInt + 8));
    paramDeviceInfo.setColorB(paramCursor.getInt(paramInt + 9));
    paramDeviceInfo.setColorLuminance(paramCursor.getInt(paramInt + 10));
    paramDeviceInfo.setColorSpeed(paramCursor.getInt(paramInt + 11));
    paramDeviceInfo.setFanMode(paramCursor.getInt(paramInt + 12));
    paramDeviceInfo.setFanLowSpeed(paramCursor.getInt(paramInt + 13));
    paramDeviceInfo.setFanMaxSpeed(paramCursor.getInt(paramInt + 14));
    paramDeviceInfo.setFanNomalSpeed(paramCursor.getInt(paramInt + 15));
    if (paramCursor.isNull(paramInt + 16)) {
      str2 = null;
    } else {
      str2 = paramCursor.getString(paramInt + 16);
    } 
    paramDeviceInfo.setDeviceIP(str2);
    if (paramCursor.isNull(paramInt + 17)) {
      str2 = str1;
    } else {
      str2 = paramCursor.getString(paramInt + 17);
    } 
    paramDeviceInfo.setName(str2);
    if (paramCursor.getShort(paramInt + 18) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    paramDeviceInfo.setIsOnline(bool);
  }
  
  public Long readKey(Cursor paramCursor, int paramInt) {
    return paramCursor.isNull(paramInt + 0) ? null : Long.valueOf(paramCursor.getLong(paramInt + 0));
  }
  
  protected final Long updateKeyAfterInsert(DeviceInfo paramDeviceInfo, long paramLong) {
    paramDeviceInfo.setId(Long.valueOf(paramLong));
    return Long.valueOf(paramLong);
  }
  
  public static class Properties {
    public static final Property ColorB;
    
    public static final Property ColorG;
    
    public static final Property ColorLuminance;
    
    public static final Property ColorMode;
    
    public static final Property ColorR;
    
    public static final Property ColorSpeed;
    
    public static final Property DeviceIP;
    
    public static final Property DeviceVersion;
    
    public static final Property FanLowSpeed;
    
    public static final Property FanMaxSpeed;
    
    public static final Property FanMode;
    
    public static final Property FanNomalSpeed;
    
    public static final Property Id = new Property(0, Long.class, "id", true, "_id");
    
    public static final Property IsOnline;
    
    public static final Property MacDrass = new Property(2, String.class, "macDrass", false, "MAC_DRASS");
    
    public static final Property Name;
    
    public static final Property Permission = new Property(1, int.class, "permission", false, "PERMISSION");
    
    public static final Property ToggleState;
    
    public static final Property Uuid;
    
    static {
      DeviceVersion = new Property(3, String.class, "deviceVersion", false, "DEVICE_VERSION");
      Uuid = new Property(4, String.class, "uuid", false, "UUID");
      ToggleState = new Property(5, int.class, "toggleState", false, "TOGGLE_STATE");
      ColorMode = new Property(6, int.class, "colorMode", false, "COLOR_MODE");
      ColorR = new Property(7, int.class, "colorR", false, "COLOR_R");
      ColorG = new Property(8, int.class, "colorG", false, "COLOR_G");
      ColorB = new Property(9, int.class, "colorB", false, "COLOR_B");
      ColorLuminance = new Property(10, int.class, "colorLuminance", false, "COLOR_LUMINANCE");
      ColorSpeed = new Property(11, int.class, "colorSpeed", false, "COLOR_SPEED");
      FanMode = new Property(12, int.class, "fanMode", false, "FAN_MODE");
      FanLowSpeed = new Property(13, int.class, "fanLowSpeed", false, "FAN_LOW_SPEED");
      FanMaxSpeed = new Property(14, int.class, "fanMaxSpeed", false, "FAN_MAX_SPEED");
      FanNomalSpeed = new Property(15, int.class, "fanNomalSpeed", false, "FAN_NOMAL_SPEED");
      DeviceIP = new Property(16, String.class, "deviceIP", false, "DEVICE_IP");
      Name = new Property(17, String.class, "name", false, "NAME");
      IsOnline = new Property(18, boolean.class, "isOnline", false, "IS_ONLINE");
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/DeviceInfoDao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */