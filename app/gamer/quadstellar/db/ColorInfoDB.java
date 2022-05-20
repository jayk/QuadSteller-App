package app.gamer.quadstellar.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import app.gamer.quadstellar.domain.ColorInfo;
import app.gamer.quadstellar.domain.ColorInfoDao;
import app.gamer.quadstellar.domain.DaoMaster;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.greendao.query.WhereCondition;

public class ColorInfoDB {
  private static final String dbName = "colorinfo.db";
  
  private static ColorInfoDB mInstance;
  
  private Context mContext;
  
  private DaoMaster.DevOpenHelper mOpenHelper;
  
  public ColorInfoDB(Context paramContext) {
    this.mContext = paramContext;
    this.mOpenHelper = new DaoMaster.DevOpenHelper(paramContext, "colorinfo.db", null);
  }
  
  public static ColorInfoDB getInstance() {
    // Byte code:
    //   0: getstatic app/gamer/quadstellar/db/ColorInfoDB.mInstance : Lapp/gamer/quadstellar/db/ColorInfoDB;
    //   3: ifnonnull -> 39
    //   6: ldc app/gamer/quadstellar/db/ColorInfoDB
    //   8: monitorenter
    //   9: getstatic app/gamer/quadstellar/db/ColorInfoDB.mInstance : Lapp/gamer/quadstellar/db/ColorInfoDB;
    //   12: ifnonnull -> 36
    //   15: ldc app/gamer/quadstellar/db/ColorInfoDB
    //   17: monitorenter
    //   18: new app/gamer/quadstellar/db/ColorInfoDB
    //   21: astore_0
    //   22: aload_0
    //   23: invokestatic getAppContext : ()Landroid/content/Context;
    //   26: invokespecial <init> : (Landroid/content/Context;)V
    //   29: aload_0
    //   30: putstatic app/gamer/quadstellar/db/ColorInfoDB.mInstance : Lapp/gamer/quadstellar/db/ColorInfoDB;
    //   33: ldc app/gamer/quadstellar/db/ColorInfoDB
    //   35: monitorexit
    //   36: ldc app/gamer/quadstellar/db/ColorInfoDB
    //   38: monitorexit
    //   39: getstatic app/gamer/quadstellar/db/ColorInfoDB.mInstance : Lapp/gamer/quadstellar/db/ColorInfoDB;
    //   42: areturn
    //   43: astore_0
    //   44: ldc app/gamer/quadstellar/db/ColorInfoDB
    //   46: monitorexit
    //   47: aload_0
    //   48: athrow
    //   49: astore_0
    //   50: ldc app/gamer/quadstellar/db/ColorInfoDB
    //   52: monitorexit
    //   53: aload_0
    //   54: athrow
    // Exception table:
    //   from	to	target	type
    //   9	18	49	finally
    //   18	36	43	finally
    //   36	39	49	finally
    //   44	47	43	finally
    //   47	49	49	finally
    //   50	53	49	finally
  }
  
  public void deleteAll() {
    (new DaoMaster(getWritableDatabase())).newSession().getColorInfoDao().deleteAll();
  }
  
  public void deleteUser(String paramString) {
    ColorInfoDao colorInfoDao = (new DaoMaster(getWritableDatabase())).newSession().getColorInfoDao();
    List list = colorInfoDao.queryBuilder().where(ColorInfoDao.Properties.MacDrass.eq(paramString), new WhereCondition[0]).build().list();
    if (list != null && list.size() > 0) {
      Iterator<ColorInfo> iterator = list.iterator();
      while (iterator.hasNext())
        colorInfoDao.deleteByKey(((ColorInfo)iterator.next()).getId()); 
    } 
  }
  
  public SQLiteDatabase getReadableDatabase() {
    if (this.mOpenHelper == null)
      this.mOpenHelper = new DaoMaster.DevOpenHelper(this.mContext, "colorinfo.db", null); 
    return this.mOpenHelper.getReadableDatabase();
  }
  
  public SQLiteDatabase getWritableDatabase() {
    if (this.mOpenHelper == null)
      this.mOpenHelper = new DaoMaster.DevOpenHelper(this.mContext, "colorinfo.db", null); 
    return this.mOpenHelper.getWritableDatabase();
  }
  
  public void insertUser(ColorInfo paramColorInfo) {
    (new DaoMaster(getWritableDatabase())).newSession().getColorInfoDao().insert(paramColorInfo);
  }
  
  public void insertUserList(List<ColorInfo> paramList) {
    if (paramList != null && !paramList.isEmpty())
      (new DaoMaster(getWritableDatabase())).newSession().getColorInfoDao().insertInTx(paramList); 
  }
  
  public ColorInfo queryUserList(String paramString, boolean paramBoolean) {
    return (ColorInfo)(new DaoMaster(getReadableDatabase())).newSession().getColorInfoDao().queryBuilder().where(ColorInfoDao.Properties.MacDrass.eq(paramString), new WhereCondition[] { ColorInfoDao.Properties.IsWhole.eq(Boolean.valueOf(paramBoolean)) }).build().unique();
  }
  
  public List<ColorInfo> queryUserList(boolean paramBoolean) {
    return (new DaoMaster(getReadableDatabase())).newSession().getColorInfoDao().queryBuilder().where(ColorInfoDao.Properties.IsWhole.eq(Boolean.valueOf(paramBoolean)), new WhereCondition[0]).build().list();
  }
  
  public void updateUser(ColorInfo paramColorInfo) {
    (new DaoMaster(getWritableDatabase())).newSession().getColorInfoDao().update(paramColorInfo);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/db/ColorInfoDB.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */