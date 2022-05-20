package app.gamer.quadstellar.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import app.gamer.quadstellar.domain.DaoMaster;
import app.gamer.quadstellar.domain.FanDeviceInfo;
import app.gamer.quadstellar.domain.FanDeviceInfoDao;
import java.util.List;
import org.greenrobot.greendao.query.WhereCondition;

public class FanDeviceInfoDB {
  private static final String TAG = "DeviceInfoDB";
  
  private static final String dbName = "deviceinfo.db";
  
  private static FanDeviceInfoDB mInstance;
  
  private Context mContext;
  
  private DaoMaster.DevOpenHelper mOpenHelper;
  
  private FanDeviceInfoDB(Context paramContext) {
    this.mContext = paramContext;
    this.mOpenHelper = new DaoMaster.DevOpenHelper(paramContext, "deviceinfo.db", null);
  }
  
  public static FanDeviceInfoDB getInstance() {
    // Byte code:
    //   0: getstatic app/gamer/quadstellar/db/FanDeviceInfoDB.mInstance : Lapp/gamer/quadstellar/db/FanDeviceInfoDB;
    //   3: ifnonnull -> 39
    //   6: ldc app/gamer/quadstellar/db/FanDeviceInfoDB
    //   8: monitorenter
    //   9: getstatic app/gamer/quadstellar/db/FanDeviceInfoDB.mInstance : Lapp/gamer/quadstellar/db/FanDeviceInfoDB;
    //   12: ifnonnull -> 36
    //   15: ldc app/gamer/quadstellar/db/FanDeviceInfoDB
    //   17: monitorenter
    //   18: new app/gamer/quadstellar/db/FanDeviceInfoDB
    //   21: astore_0
    //   22: aload_0
    //   23: invokestatic getAppContext : ()Landroid/content/Context;
    //   26: invokespecial <init> : (Landroid/content/Context;)V
    //   29: aload_0
    //   30: putstatic app/gamer/quadstellar/db/FanDeviceInfoDB.mInstance : Lapp/gamer/quadstellar/db/FanDeviceInfoDB;
    //   33: ldc app/gamer/quadstellar/db/FanDeviceInfoDB
    //   35: monitorexit
    //   36: ldc app/gamer/quadstellar/db/FanDeviceInfoDB
    //   38: monitorexit
    //   39: getstatic app/gamer/quadstellar/db/FanDeviceInfoDB.mInstance : Lapp/gamer/quadstellar/db/FanDeviceInfoDB;
    //   42: areturn
    //   43: astore_0
    //   44: ldc app/gamer/quadstellar/db/FanDeviceInfoDB
    //   46: monitorexit
    //   47: aload_0
    //   48: athrow
    //   49: astore_0
    //   50: ldc app/gamer/quadstellar/db/FanDeviceInfoDB
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
    (new DaoMaster(getWritableDatabase())).newSession().getFanDeviceInfoDao().deleteAll();
  }
  
  public void deleteUser(String paramString) {
    FanDeviceInfoDao fanDeviceInfoDao = (new DaoMaster(getWritableDatabase())).newSession().getFanDeviceInfoDao();
    List<FanDeviceInfo> list = fanDeviceInfoDao.queryBuilder().where(FanDeviceInfoDao.Properties.MacAdrass.eq(paramString), new WhereCondition[0]).build().list();
    if (list != null)
      for (byte b = 0; b < list.size(); b++) {
        if (list.get(b) != null)
          fanDeviceInfoDao.deleteByKey(((FanDeviceInfo)list.get(b)).getId()); 
      }  
  }
  
  public SQLiteDatabase getReadableDatabase() {
    if (this.mOpenHelper == null)
      this.mOpenHelper = new DaoMaster.DevOpenHelper(this.mContext, "deviceinfo.db", null); 
    return this.mOpenHelper.getReadableDatabase();
  }
  
  public SQLiteDatabase getWritableDatabase() {
    if (this.mOpenHelper == null)
      this.mOpenHelper = new DaoMaster.DevOpenHelper(this.mContext, "deviceinfo.db", null); 
    return this.mOpenHelper.getWritableDatabase();
  }
  
  public void insertUser(FanDeviceInfo paramFanDeviceInfo) {
    FanDeviceInfoDao fanDeviceInfoDao = (new DaoMaster(getWritableDatabase())).newSession().getFanDeviceInfoDao();
    if ((FanDeviceInfo)fanDeviceInfoDao.queryBuilder().where(FanDeviceInfoDao.Properties.MacAdrass.eq(paramFanDeviceInfo.getMacAdrass()), new WhereCondition[] { FanDeviceInfoDao.Properties.SonFanNumber.eq(Integer.valueOf(paramFanDeviceInfo.getSonFanNumber())) }).build().unique() != null) {
      fanDeviceInfoDao.update(paramFanDeviceInfo);
      return;
    } 
    fanDeviceInfoDao.insert(paramFanDeviceInfo);
  }
  
  public void insertUserList(List<FanDeviceInfo> paramList) {
    if (paramList != null && !paramList.isEmpty())
      (new DaoMaster(getWritableDatabase())).newSession().getFanDeviceInfoDao().insertInTx(paramList); 
  }
  
  public FanDeviceInfo queryUserList(String paramString, int paramInt) {
    return (FanDeviceInfo)(new DaoMaster(getReadableDatabase())).newSession().getFanDeviceInfoDao().queryBuilder().where(FanDeviceInfoDao.Properties.MacAdrass.eq(paramString), new WhereCondition[] { FanDeviceInfoDao.Properties.SonFanNumber.eq(Integer.valueOf(paramInt)) }).build().unique();
  }
  
  public List<FanDeviceInfo> queryUserList() {
    return (new DaoMaster(getReadableDatabase())).newSession().getFanDeviceInfoDao().queryBuilder().build().list();
  }
  
  public List<FanDeviceInfo> queryUserList(String paramString) {
    return (new DaoMaster(getReadableDatabase())).newSession().getFanDeviceInfoDao().queryBuilder().where(FanDeviceInfoDao.Properties.MacAdrass.eq(paramString), new WhereCondition[0]).build().list();
  }
  
  public void updateUser(FanDeviceInfo paramFanDeviceInfo) {
    (new DaoMaster(getWritableDatabase())).newSession().getFanDeviceInfoDao().update(paramFanDeviceInfo);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/db/FanDeviceInfoDB.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */