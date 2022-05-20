package app.gamer.quadstellar.domain;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class FatherDeviceInfoDao extends AbstractDao<FatherDeviceInfo, Long> {
  public static final String TABLENAME = "FATHER_DEVICE_INFO";
  
  public FatherDeviceInfoDao(DaoConfig paramDaoConfig) {
    super(paramDaoConfig);
  }
  
  public FatherDeviceInfoDao(DaoConfig paramDaoConfig, DaoSession paramDaoSession) {
    super(paramDaoConfig, paramDaoSession);
  }
  
  public static void createTable(Database paramDatabase, boolean paramBoolean) {
    String str;
    if (paramBoolean) {
      str = "IF NOT EXISTS ";
    } else {
      str = "";
    } 
    paramDatabase.execSQL("CREATE TABLE " + str + "\"FATHER_DEVICE_INFO\" (\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ,\"MAC_ADRASS\" TEXT,\"DEVICE_VERSION\" TEXT,\"TEMP\" TEXT,\"DOOR_OPEN_STATE\" INTEGER NOT NULL ,\"FAN_COUNT\" INTEGER NOT NULL ,\"LIGHT_COUNT\" INTEGER NOT NULL ,\"TOGGLE_STATE\" INTEGER NOT NULL ,\"LIGT_MODE\" INTEGER NOT NULL ,\"DEVICE_IP\" TEXT,\"UUID\" TEXT,\"DEVICE_LEN\" INTEGER NOT NULL ,\"NAME\" TEXT,\"IS_ONLINE\" INTEGER NOT NULL ,\"COLOR_R\" INTEGER NOT NULL ,\"COLOR_G\" INTEGER NOT NULL ,\"COLOR_B\" INTEGER NOT NULL ,\"COLOR_SPEED\" INTEGER NOT NULL ,\"COLOR_BRIGHTNESS\" INTEGER NOT NULL ,\"COLOR_FINENESS\" INTEGER NOT NULL ,\"LIGHT_SON_NUMBER\" INTEGER NOT NULL ,\"LIGHT_SON_TOGGLE\" INTEGER NOT NULL ,\"FAN_SON_NUMER\" INTEGER NOT NULL ,\"FAN_MODE\" INTEGER NOT NULL ,\"DOOR_TYPE\" INTEGER NOT NULL ,\"DOOR_TOGGLE_STATE\" INTEGER NOT NULL ,\"DOOR_OPEN_TEMP\" TEXT,\"DOOR_CLOSE_TEMP\" TEXT,\"CONTROL_TAG\" INTEGER NOT NULL ,\"FAN_SPEED\" INTEGER NOT NULL ,\"PART_LIGHT_MODE\" INTEGER NOT NULL ,\"PART_LIGHT_SPEED\" INTEGER NOT NULL ,\"PART_LIGHT_BRIGTNESS\" INTEGER NOT NULL );");
  }
  
  public static void dropTable(Database paramDatabase, boolean paramBoolean) {
    String str;
    StringBuilder stringBuilder = (new StringBuilder()).append("DROP TABLE ");
    if (paramBoolean) {
      str = "IF EXISTS ";
    } else {
      str = "";
    } 
    paramDatabase.execSQL(stringBuilder.append(str).append("\"FATHER_DEVICE_INFO\"").toString());
  }
  
  protected final void bindValues(SQLiteStatement paramSQLiteStatement, FatherDeviceInfo paramFatherDeviceInfo) {
    long l;
    paramSQLiteStatement.clearBindings();
    Long long_ = paramFatherDeviceInfo.getId();
    if (long_ != null)
      paramSQLiteStatement.bindLong(1, long_.longValue()); 
    String str = paramFatherDeviceInfo.getMacAdrass();
    if (str != null)
      paramSQLiteStatement.bindString(2, str); 
    str = paramFatherDeviceInfo.getDeviceVersion();
    if (str != null)
      paramSQLiteStatement.bindString(3, str); 
    str = paramFatherDeviceInfo.getTemp();
    if (str != null)
      paramSQLiteStatement.bindString(4, str); 
    paramSQLiteStatement.bindLong(5, paramFatherDeviceInfo.getDoorOpenState());
    paramSQLiteStatement.bindLong(6, paramFatherDeviceInfo.getFanCount());
    paramSQLiteStatement.bindLong(7, paramFatherDeviceInfo.getLightCount());
    paramSQLiteStatement.bindLong(8, paramFatherDeviceInfo.getToggleState());
    paramSQLiteStatement.bindLong(9, paramFatherDeviceInfo.getLigtMode());
    str = paramFatherDeviceInfo.getDeviceIP();
    if (str != null)
      paramSQLiteStatement.bindString(10, str); 
    str = paramFatherDeviceInfo.getUuid();
    if (str != null)
      paramSQLiteStatement.bindString(11, str); 
    paramSQLiteStatement.bindLong(12, paramFatherDeviceInfo.getDeviceLen());
    str = paramFatherDeviceInfo.getName();
    if (str != null)
      paramSQLiteStatement.bindString(13, str); 
    if (paramFatherDeviceInfo.getIsOnline()) {
      l = 1L;
    } else {
      l = 0L;
    } 
    paramSQLiteStatement.bindLong(14, l);
    paramSQLiteStatement.bindLong(15, paramFatherDeviceInfo.getColorR());
    paramSQLiteStatement.bindLong(16, paramFatherDeviceInfo.getColorG());
    paramSQLiteStatement.bindLong(17, paramFatherDeviceInfo.getColorB());
    paramSQLiteStatement.bindLong(18, paramFatherDeviceInfo.getColorSpeed());
    paramSQLiteStatement.bindLong(19, paramFatherDeviceInfo.getColorBrightness());
    paramSQLiteStatement.bindLong(20, paramFatherDeviceInfo.getColorFineness());
    paramSQLiteStatement.bindLong(21, paramFatherDeviceInfo.getLightSonNumber());
    paramSQLiteStatement.bindLong(22, paramFatherDeviceInfo.getLightSonToggle());
    paramSQLiteStatement.bindLong(23, paramFatherDeviceInfo.getFanSonNumer());
    paramSQLiteStatement.bindLong(24, paramFatherDeviceInfo.getFanMode());
    paramSQLiteStatement.bindLong(25, paramFatherDeviceInfo.getDoorType());
    paramSQLiteStatement.bindLong(26, paramFatherDeviceInfo.getDoorToggleState());
    str = paramFatherDeviceInfo.getDoorOpenTemp();
    if (str != null)
      paramSQLiteStatement.bindString(27, str); 
    str = paramFatherDeviceInfo.getDoorCloseTemp();
    if (str != null)
      paramSQLiteStatement.bindString(28, str); 
    paramSQLiteStatement.bindLong(29, paramFatherDeviceInfo.getControlTag());
    paramSQLiteStatement.bindLong(30, paramFatherDeviceInfo.getFanSpeed());
    paramSQLiteStatement.bindLong(31, paramFatherDeviceInfo.getPartLightMode());
    paramSQLiteStatement.bindLong(32, paramFatherDeviceInfo.getPartLightSpeed());
    paramSQLiteStatement.bindLong(33, paramFatherDeviceInfo.getPartLightBrigtness());
  }
  
  protected final void bindValues(DatabaseStatement paramDatabaseStatement, FatherDeviceInfo paramFatherDeviceInfo) {
    long l;
    paramDatabaseStatement.clearBindings();
    Long long_ = paramFatherDeviceInfo.getId();
    if (long_ != null)
      paramDatabaseStatement.bindLong(1, long_.longValue()); 
    String str = paramFatherDeviceInfo.getMacAdrass();
    if (str != null)
      paramDatabaseStatement.bindString(2, str); 
    str = paramFatherDeviceInfo.getDeviceVersion();
    if (str != null)
      paramDatabaseStatement.bindString(3, str); 
    str = paramFatherDeviceInfo.getTemp();
    if (str != null)
      paramDatabaseStatement.bindString(4, str); 
    paramDatabaseStatement.bindLong(5, paramFatherDeviceInfo.getDoorOpenState());
    paramDatabaseStatement.bindLong(6, paramFatherDeviceInfo.getFanCount());
    paramDatabaseStatement.bindLong(7, paramFatherDeviceInfo.getLightCount());
    paramDatabaseStatement.bindLong(8, paramFatherDeviceInfo.getToggleState());
    paramDatabaseStatement.bindLong(9, paramFatherDeviceInfo.getLigtMode());
    str = paramFatherDeviceInfo.getDeviceIP();
    if (str != null)
      paramDatabaseStatement.bindString(10, str); 
    str = paramFatherDeviceInfo.getUuid();
    if (str != null)
      paramDatabaseStatement.bindString(11, str); 
    paramDatabaseStatement.bindLong(12, paramFatherDeviceInfo.getDeviceLen());
    str = paramFatherDeviceInfo.getName();
    if (str != null)
      paramDatabaseStatement.bindString(13, str); 
    if (paramFatherDeviceInfo.getIsOnline()) {
      l = 1L;
    } else {
      l = 0L;
    } 
    paramDatabaseStatement.bindLong(14, l);
    paramDatabaseStatement.bindLong(15, paramFatherDeviceInfo.getColorR());
    paramDatabaseStatement.bindLong(16, paramFatherDeviceInfo.getColorG());
    paramDatabaseStatement.bindLong(17, paramFatherDeviceInfo.getColorB());
    paramDatabaseStatement.bindLong(18, paramFatherDeviceInfo.getColorSpeed());
    paramDatabaseStatement.bindLong(19, paramFatherDeviceInfo.getColorBrightness());
    paramDatabaseStatement.bindLong(20, paramFatherDeviceInfo.getColorFineness());
    paramDatabaseStatement.bindLong(21, paramFatherDeviceInfo.getLightSonNumber());
    paramDatabaseStatement.bindLong(22, paramFatherDeviceInfo.getLightSonToggle());
    paramDatabaseStatement.bindLong(23, paramFatherDeviceInfo.getFanSonNumer());
    paramDatabaseStatement.bindLong(24, paramFatherDeviceInfo.getFanMode());
    paramDatabaseStatement.bindLong(25, paramFatherDeviceInfo.getDoorType());
    paramDatabaseStatement.bindLong(26, paramFatherDeviceInfo.getDoorToggleState());
    str = paramFatherDeviceInfo.getDoorOpenTemp();
    if (str != null)
      paramDatabaseStatement.bindString(27, str); 
    str = paramFatherDeviceInfo.getDoorCloseTemp();
    if (str != null)
      paramDatabaseStatement.bindString(28, str); 
    paramDatabaseStatement.bindLong(29, paramFatherDeviceInfo.getControlTag());
    paramDatabaseStatement.bindLong(30, paramFatherDeviceInfo.getFanSpeed());
    paramDatabaseStatement.bindLong(31, paramFatherDeviceInfo.getPartLightMode());
    paramDatabaseStatement.bindLong(32, paramFatherDeviceInfo.getPartLightSpeed());
    paramDatabaseStatement.bindLong(33, paramFatherDeviceInfo.getPartLightBrigtness());
  }
  
  public Long getKey(FatherDeviceInfo paramFatherDeviceInfo) {
    return (paramFatherDeviceInfo != null) ? paramFatherDeviceInfo.getId() : null;
  }
  
  public boolean hasKey(FatherDeviceInfo paramFatherDeviceInfo) {
    return (paramFatherDeviceInfo.getId() != null);
  }
  
  protected final boolean isEntityUpdateable() {
    return true;
  }
  
  public FatherDeviceInfo readEntity(Cursor paramCursor, int paramInt) {
    Long long_;
    String str1;
    String str2;
    String str3;
    String str4;
    String str5;
    String str6;
    boolean bool;
    String str7;
    if (paramCursor.isNull(paramInt + 0)) {
      long_ = null;
    } else {
      long_ = Long.valueOf(paramCursor.getLong(paramInt + 0));
    } 
    if (paramCursor.isNull(paramInt + 1)) {
      str1 = null;
    } else {
      str1 = paramCursor.getString(paramInt + 1);
    } 
    if (paramCursor.isNull(paramInt + 2)) {
      str2 = null;
    } else {
      str2 = paramCursor.getString(paramInt + 2);
    } 
    if (paramCursor.isNull(paramInt + 3)) {
      str3 = null;
    } else {
      str3 = paramCursor.getString(paramInt + 3);
    } 
    int i = paramCursor.getInt(paramInt + 4);
    int j = paramCursor.getInt(paramInt + 5);
    int k = paramCursor.getInt(paramInt + 6);
    int m = paramCursor.getInt(paramInt + 7);
    int n = paramCursor.getInt(paramInt + 8);
    if (paramCursor.isNull(paramInt + 9)) {
      str4 = null;
    } else {
      str4 = paramCursor.getString(paramInt + 9);
    } 
    if (paramCursor.isNull(paramInt + 10)) {
      str5 = null;
    } else {
      str5 = paramCursor.getString(paramInt + 10);
    } 
    int i1 = paramCursor.getInt(paramInt + 11);
    if (paramCursor.isNull(paramInt + 12)) {
      str6 = null;
    } else {
      str6 = paramCursor.getString(paramInt + 12);
    } 
    if (paramCursor.getShort(paramInt + 13) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    int i2 = paramCursor.getInt(paramInt + 14);
    int i3 = paramCursor.getInt(paramInt + 15);
    int i4 = paramCursor.getInt(paramInt + 16);
    int i5 = paramCursor.getInt(paramInt + 17);
    int i6 = paramCursor.getInt(paramInt + 18);
    int i7 = paramCursor.getInt(paramInt + 19);
    int i8 = paramCursor.getInt(paramInt + 20);
    int i9 = paramCursor.getInt(paramInt + 21);
    int i10 = paramCursor.getInt(paramInt + 22);
    int i11 = paramCursor.getInt(paramInt + 23);
    int i12 = paramCursor.getInt(paramInt + 24);
    int i13 = paramCursor.getInt(paramInt + 25);
    if (paramCursor.isNull(paramInt + 26)) {
      str7 = null;
    } else {
      str7 = paramCursor.getString(paramInt + 26);
    } 
    if (paramCursor.isNull(paramInt + 27)) {
      String str = null;
      return new FatherDeviceInfo(long_, str1, str2, str3, i, j, k, m, n, str4, str5, i1, str6, bool, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13, str7, str, paramCursor.getInt(paramInt + 28), paramCursor.getInt(paramInt + 29), paramCursor.getInt(paramInt + 30), paramCursor.getInt(paramInt + 31), paramCursor.getInt(paramInt + 32));
    } 
    String str8 = paramCursor.getString(paramInt + 27);
    return new FatherDeviceInfo(long_, str1, str2, str3, i, j, k, m, n, str4, str5, i1, str6, bool, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13, str7, str8, paramCursor.getInt(paramInt + 28), paramCursor.getInt(paramInt + 29), paramCursor.getInt(paramInt + 30), paramCursor.getInt(paramInt + 31), paramCursor.getInt(paramInt + 32));
  }
  
  public void readEntity(Cursor paramCursor, FatherDeviceInfo paramFatherDeviceInfo, int paramInt) {
    Long long_;
    String str2;
    boolean bool;
    String str1 = null;
    if (paramCursor.isNull(paramInt + 0)) {
      long_ = null;
    } else {
      long_ = Long.valueOf(paramCursor.getLong(paramInt + 0));
    } 
    paramFatherDeviceInfo.setId(long_);
    if (paramCursor.isNull(paramInt + 1)) {
      long_ = null;
    } else {
      str2 = paramCursor.getString(paramInt + 1);
    } 
    paramFatherDeviceInfo.setMacAdrass(str2);
    if (paramCursor.isNull(paramInt + 2)) {
      str2 = null;
    } else {
      str2 = paramCursor.getString(paramInt + 2);
    } 
    paramFatherDeviceInfo.setDeviceVersion(str2);
    if (paramCursor.isNull(paramInt + 3)) {
      str2 = null;
    } else {
      str2 = paramCursor.getString(paramInt + 3);
    } 
    paramFatherDeviceInfo.setTemp(str2);
    paramFatherDeviceInfo.setDoorOpenState(paramCursor.getInt(paramInt + 4));
    paramFatherDeviceInfo.setFanCount(paramCursor.getInt(paramInt + 5));
    paramFatherDeviceInfo.setLightCount(paramCursor.getInt(paramInt + 6));
    paramFatherDeviceInfo.setToggleState(paramCursor.getInt(paramInt + 7));
    paramFatherDeviceInfo.setLigtMode(paramCursor.getInt(paramInt + 8));
    if (paramCursor.isNull(paramInt + 9)) {
      str2 = null;
    } else {
      str2 = paramCursor.getString(paramInt + 9);
    } 
    paramFatherDeviceInfo.setDeviceIP(str2);
    if (paramCursor.isNull(paramInt + 10)) {
      str2 = null;
    } else {
      str2 = paramCursor.getString(paramInt + 10);
    } 
    paramFatherDeviceInfo.setUuid(str2);
    paramFatherDeviceInfo.setDeviceLen(paramCursor.getInt(paramInt + 11));
    if (paramCursor.isNull(paramInt + 12)) {
      str2 = null;
    } else {
      str2 = paramCursor.getString(paramInt + 12);
    } 
    paramFatherDeviceInfo.setName(str2);
    if (paramCursor.getShort(paramInt + 13) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    paramFatherDeviceInfo.setIsOnline(bool);
    paramFatherDeviceInfo.setColorR(paramCursor.getInt(paramInt + 14));
    paramFatherDeviceInfo.setColorG(paramCursor.getInt(paramInt + 15));
    paramFatherDeviceInfo.setColorB(paramCursor.getInt(paramInt + 16));
    paramFatherDeviceInfo.setColorSpeed(paramCursor.getInt(paramInt + 17));
    paramFatherDeviceInfo.setColorBrightness(paramCursor.getInt(paramInt + 18));
    paramFatherDeviceInfo.setColorFineness(paramCursor.getInt(paramInt + 19));
    paramFatherDeviceInfo.setLightSonNumber(paramCursor.getInt(paramInt + 20));
    paramFatherDeviceInfo.setLightSonToggle(paramCursor.getInt(paramInt + 21));
    paramFatherDeviceInfo.setFanSonNumer(paramCursor.getInt(paramInt + 22));
    paramFatherDeviceInfo.setFanMode(paramCursor.getInt(paramInt + 23));
    paramFatherDeviceInfo.setDoorType(paramCursor.getInt(paramInt + 24));
    paramFatherDeviceInfo.setDoorToggleState(paramCursor.getInt(paramInt + 25));
    if (paramCursor.isNull(paramInt + 26)) {
      str2 = null;
    } else {
      str2 = paramCursor.getString(paramInt + 26);
    } 
    paramFatherDeviceInfo.setDoorOpenTemp(str2);
    if (paramCursor.isNull(paramInt + 27)) {
      str2 = str1;
    } else {
      str2 = paramCursor.getString(paramInt + 27);
    } 
    paramFatherDeviceInfo.setDoorCloseTemp(str2);
    paramFatherDeviceInfo.setControlTag(paramCursor.getInt(paramInt + 28));
    paramFatherDeviceInfo.setFanSpeed(paramCursor.getInt(paramInt + 29));
    paramFatherDeviceInfo.setPartLightMode(paramCursor.getInt(paramInt + 30));
    paramFatherDeviceInfo.setPartLightSpeed(paramCursor.getInt(paramInt + 31));
    paramFatherDeviceInfo.setPartLightBrigtness(paramCursor.getInt(paramInt + 32));
  }
  
  public Long readKey(Cursor paramCursor, int paramInt) {
    return paramCursor.isNull(paramInt + 0) ? null : Long.valueOf(paramCursor.getLong(paramInt + 0));
  }
  
  protected final Long updateKeyAfterInsert(FatherDeviceInfo paramFatherDeviceInfo, long paramLong) {
    paramFatherDeviceInfo.setId(Long.valueOf(paramLong));
    return Long.valueOf(paramLong);
  }
  
  public static class Properties {
    public static final Property ColorB;
    
    public static final Property ColorBrightness;
    
    public static final Property ColorFineness;
    
    public static final Property ColorG;
    
    public static final Property ColorR;
    
    public static final Property ColorSpeed;
    
    public static final Property ControlTag;
    
    public static final Property DeviceIP;
    
    public static final Property DeviceLen;
    
    public static final Property DeviceVersion = new Property(2, String.class, "deviceVersion", false, "DEVICE_VERSION");
    
    public static final Property DoorCloseTemp;
    
    public static final Property DoorOpenState;
    
    public static final Property DoorOpenTemp;
    
    public static final Property DoorToggleState;
    
    public static final Property DoorType;
    
    public static final Property FanCount;
    
    public static final Property FanMode;
    
    public static final Property FanSonNumer;
    
    public static final Property FanSpeed;
    
    public static final Property Id = new Property(0, Long.class, "id", true, "_id");
    
    public static final Property IsOnline;
    
    public static final Property LightCount;
    
    public static final Property LightSonNumber;
    
    public static final Property LightSonToggle;
    
    public static final Property LigtMode;
    
    public static final Property MacAdrass = new Property(1, String.class, "macAdrass", false, "MAC_ADRASS");
    
    public static final Property Name;
    
    public static final Property PartLightBrigtness;
    
    public static final Property PartLightMode;
    
    public static final Property PartLightSpeed;
    
    public static final Property Temp = new Property(3, String.class, "temp", false, "TEMP");
    
    public static final Property ToggleState;
    
    public static final Property Uuid;
    
    static {
      DoorOpenState = new Property(4, int.class, "doorOpenState", false, "DOOR_OPEN_STATE");
      FanCount = new Property(5, int.class, "fanCount", false, "FAN_COUNT");
      LightCount = new Property(6, int.class, "lightCount", false, "LIGHT_COUNT");
      ToggleState = new Property(7, int.class, "toggleState", false, "TOGGLE_STATE");
      LigtMode = new Property(8, int.class, "ligtMode", false, "LIGT_MODE");
      DeviceIP = new Property(9, String.class, "deviceIP", false, "DEVICE_IP");
      Uuid = new Property(10, String.class, "uuid", false, "UUID");
      DeviceLen = new Property(11, int.class, "deviceLen", false, "DEVICE_LEN");
      Name = new Property(12, String.class, "name", false, "NAME");
      IsOnline = new Property(13, boolean.class, "isOnline", false, "IS_ONLINE");
      ColorR = new Property(14, int.class, "colorR", false, "COLOR_R");
      ColorG = new Property(15, int.class, "colorG", false, "COLOR_G");
      ColorB = new Property(16, int.class, "colorB", false, "COLOR_B");
      ColorSpeed = new Property(17, int.class, "colorSpeed", false, "COLOR_SPEED");
      ColorBrightness = new Property(18, int.class, "colorBrightness", false, "COLOR_BRIGHTNESS");
      ColorFineness = new Property(19, int.class, "colorFineness", false, "COLOR_FINENESS");
      LightSonNumber = new Property(20, int.class, "lightSonNumber", false, "LIGHT_SON_NUMBER");
      LightSonToggle = new Property(21, int.class, "lightSonToggle", false, "LIGHT_SON_TOGGLE");
      FanSonNumer = new Property(22, int.class, "fanSonNumer", false, "FAN_SON_NUMER");
      FanMode = new Property(23, int.class, "fanMode", false, "FAN_MODE");
      DoorType = new Property(24, int.class, "doorType", false, "DOOR_TYPE");
      DoorToggleState = new Property(25, int.class, "doorToggleState", false, "DOOR_TOGGLE_STATE");
      DoorOpenTemp = new Property(26, String.class, "doorOpenTemp", false, "DOOR_OPEN_TEMP");
      DoorCloseTemp = new Property(27, String.class, "doorCloseTemp", false, "DOOR_CLOSE_TEMP");
      ControlTag = new Property(28, int.class, "controlTag", false, "CONTROL_TAG");
      FanSpeed = new Property(29, int.class, "fanSpeed", false, "FAN_SPEED");
      PartLightMode = new Property(30, int.class, "partLightMode", false, "PART_LIGHT_MODE");
      PartLightSpeed = new Property(31, int.class, "partLightSpeed", false, "PART_LIGHT_SPEED");
      PartLightBrigtness = new Property(32, int.class, "partLightBrigtness", false, "PART_LIGHT_BRIGTNESS");
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/FatherDeviceInfoDao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */