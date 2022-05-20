package app.gamer.quadstellar.domain;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class ColorInfoDao extends AbstractDao<ColorInfo, Long> {
  public static final String TABLENAME = "COLOR_INFO";
  
  public ColorInfoDao(DaoConfig paramDaoConfig) {
    super(paramDaoConfig);
  }
  
  public ColorInfoDao(DaoConfig paramDaoConfig, DaoSession paramDaoSession) {
    super(paramDaoConfig, paramDaoSession);
  }
  
  public static void createTable(Database paramDatabase, boolean paramBoolean) {
    String str;
    if (paramBoolean) {
      str = "IF NOT EXISTS ";
    } else {
      str = "";
    } 
    paramDatabase.execSQL("CREATE TABLE " + str + "\"COLOR_INFO\" (\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ,\"MAC_DRASS\" TEXT,\"COLOR_ONE\" INTEGER NOT NULL ,\"COLOR_TWO\" INTEGER NOT NULL ,\"COLOR_THREE\" INTEGER NOT NULL ,\"COLOR_FOUR\" INTEGER NOT NULL ,\"COLOR_FIVE\" INTEGER NOT NULL ,\"IS_WHOLE\" INTEGER NOT NULL );");
  }
  
  public static void dropTable(Database paramDatabase, boolean paramBoolean) {
    String str;
    StringBuilder stringBuilder = (new StringBuilder()).append("DROP TABLE ");
    if (paramBoolean) {
      str = "IF EXISTS ";
    } else {
      str = "";
    } 
    paramDatabase.execSQL(stringBuilder.append(str).append("\"COLOR_INFO\"").toString());
  }
  
  protected final void bindValues(SQLiteStatement paramSQLiteStatement, ColorInfo paramColorInfo) {
    long l;
    paramSQLiteStatement.clearBindings();
    Long long_ = paramColorInfo.getId();
    if (long_ != null)
      paramSQLiteStatement.bindLong(1, long_.longValue()); 
    String str = paramColorInfo.getMacDrass();
    if (str != null)
      paramSQLiteStatement.bindString(2, str); 
    paramSQLiteStatement.bindLong(3, paramColorInfo.getColorOne());
    paramSQLiteStatement.bindLong(4, paramColorInfo.getColorTwo());
    paramSQLiteStatement.bindLong(5, paramColorInfo.getColorThree());
    paramSQLiteStatement.bindLong(6, paramColorInfo.getColorFour());
    paramSQLiteStatement.bindLong(7, paramColorInfo.getColorFive());
    if (paramColorInfo.getIsWhole()) {
      l = 1L;
    } else {
      l = 0L;
    } 
    paramSQLiteStatement.bindLong(8, l);
  }
  
  protected final void bindValues(DatabaseStatement paramDatabaseStatement, ColorInfo paramColorInfo) {
    long l;
    paramDatabaseStatement.clearBindings();
    Long long_ = paramColorInfo.getId();
    if (long_ != null)
      paramDatabaseStatement.bindLong(1, long_.longValue()); 
    String str = paramColorInfo.getMacDrass();
    if (str != null)
      paramDatabaseStatement.bindString(2, str); 
    paramDatabaseStatement.bindLong(3, paramColorInfo.getColorOne());
    paramDatabaseStatement.bindLong(4, paramColorInfo.getColorTwo());
    paramDatabaseStatement.bindLong(5, paramColorInfo.getColorThree());
    paramDatabaseStatement.bindLong(6, paramColorInfo.getColorFour());
    paramDatabaseStatement.bindLong(7, paramColorInfo.getColorFive());
    if (paramColorInfo.getIsWhole()) {
      l = 1L;
    } else {
      l = 0L;
    } 
    paramDatabaseStatement.bindLong(8, l);
  }
  
  public Long getKey(ColorInfo paramColorInfo) {
    return (paramColorInfo != null) ? paramColorInfo.getId() : null;
  }
  
  public boolean hasKey(ColorInfo paramColorInfo) {
    return (paramColorInfo.getId() != null);
  }
  
  protected final boolean isEntityUpdateable() {
    return true;
  }
  
  public ColorInfo readEntity(Cursor paramCursor, int paramInt) {
    Long long_;
    String str = null;
    if (paramCursor.isNull(paramInt + 0)) {
      long_ = null;
    } else {
      long_ = Long.valueOf(paramCursor.getLong(paramInt + 0));
    } 
    if (!paramCursor.isNull(paramInt + 1))
      str = paramCursor.getString(paramInt + 1); 
    int i = paramCursor.getInt(paramInt + 2);
    int j = paramCursor.getInt(paramInt + 3);
    int k = paramCursor.getInt(paramInt + 4);
    int m = paramCursor.getInt(paramInt + 5);
    int n = paramCursor.getInt(paramInt + 6);
    if (paramCursor.getShort(paramInt + 7) != 0) {
      boolean bool1 = true;
      return new ColorInfo(long_, str, i, j, k, m, n, bool1);
    } 
    boolean bool = false;
    return new ColorInfo(long_, str, i, j, k, m, n, bool);
  }
  
  public void readEntity(Cursor paramCursor, ColorInfo paramColorInfo, int paramInt) {
    Long long_2;
    String str;
    boolean bool;
    Long long_1 = null;
    if (paramCursor.isNull(paramInt + 0)) {
      long_2 = null;
    } else {
      long_2 = Long.valueOf(paramCursor.getLong(paramInt + 0));
    } 
    paramColorInfo.setId(long_2);
    if (paramCursor.isNull(paramInt + 1)) {
      long_2 = long_1;
    } else {
      str = paramCursor.getString(paramInt + 1);
    } 
    paramColorInfo.setMacDrass(str);
    paramColorInfo.setColorOne(paramCursor.getInt(paramInt + 2));
    paramColorInfo.setColorTwo(paramCursor.getInt(paramInt + 3));
    paramColorInfo.setColorThree(paramCursor.getInt(paramInt + 4));
    paramColorInfo.setColorFour(paramCursor.getInt(paramInt + 5));
    paramColorInfo.setColorFive(paramCursor.getInt(paramInt + 6));
    if (paramCursor.getShort(paramInt + 7) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    paramColorInfo.setIsWhole(bool);
  }
  
  public Long readKey(Cursor paramCursor, int paramInt) {
    return paramCursor.isNull(paramInt + 0) ? null : Long.valueOf(paramCursor.getLong(paramInt + 0));
  }
  
  protected final Long updateKeyAfterInsert(ColorInfo paramColorInfo, long paramLong) {
    paramColorInfo.setId(Long.valueOf(paramLong));
    return Long.valueOf(paramLong);
  }
  
  public static class Properties {
    public static final Property ColorFive;
    
    public static final Property ColorFour;
    
    public static final Property ColorOne = new Property(2, int.class, "colorOne", false, "COLOR_ONE");
    
    public static final Property ColorThree;
    
    public static final Property ColorTwo = new Property(3, int.class, "colorTwo", false, "COLOR_TWO");
    
    public static final Property Id = new Property(0, Long.class, "id", true, "_id");
    
    public static final Property IsWhole;
    
    public static final Property MacDrass = new Property(1, String.class, "macDrass", false, "MAC_DRASS");
    
    static {
      ColorThree = new Property(4, int.class, "colorThree", false, "COLOR_THREE");
      ColorFour = new Property(5, int.class, "colorFour", false, "COLOR_FOUR");
      ColorFive = new Property(6, int.class, "colorFive", false, "COLOR_FIVE");
      IsWhole = new Property(7, boolean.class, "isWhole", false, "IS_WHOLE");
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/ColorInfoDao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */