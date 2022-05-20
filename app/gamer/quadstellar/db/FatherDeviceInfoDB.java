package app.gamer.quadstellar.db;

import android.content.Context;
import android.util.Log;
import app.gamer.quadstellar.domain.DaoMaster;
import app.gamer.quadstellar.domain.DaoSession;
import app.gamer.quadstellar.domain.FatherDeviceInfo;
import app.gamer.quadstellar.domain.FatherDeviceInfoDao;
import java.util.List;

public class FatherDeviceInfoDB {
  private static final String TAG = "DeviceInfoDB";
  
  private static final String dbName = "deviceinfo.db";
  
  private static FatherDeviceInfoDB mInstance;
  
  private DaoMaster.DevOpenHelper mOpenHelper;
  
  private final DaoSession readDaoSession;
  
  private final DaoSession writeDaoSession;
  
  public FatherDeviceInfoDB(Context paramContext) {
    this.mOpenHelper = new DaoMaster.DevOpenHelper(paramContext, "deviceinfo.db", null);
    this.mOpenHelper.getReadableDatabase();
    this.writeDaoSession = (new DaoMaster(this.mOpenHelper.getWritableDatabase())).newSession();
    this.readDaoSession = (new DaoMaster(this.mOpenHelper.getReadableDatabase())).newSession();
  }
  
  public static FatherDeviceInfoDB getInstance() {
    // Byte code:
    //   0: getstatic app/gamer/quadstellar/db/FatherDeviceInfoDB.mInstance : Lapp/gamer/quadstellar/db/FatherDeviceInfoDB;
    //   3: ifnonnull -> 39
    //   6: ldc app/gamer/quadstellar/db/FatherDeviceInfoDB
    //   8: monitorenter
    //   9: getstatic app/gamer/quadstellar/db/FatherDeviceInfoDB.mInstance : Lapp/gamer/quadstellar/db/FatherDeviceInfoDB;
    //   12: ifnonnull -> 36
    //   15: ldc app/gamer/quadstellar/db/FatherDeviceInfoDB
    //   17: monitorenter
    //   18: new app/gamer/quadstellar/db/FatherDeviceInfoDB
    //   21: astore_0
    //   22: aload_0
    //   23: invokestatic getAppContext : ()Landroid/content/Context;
    //   26: invokespecial <init> : (Landroid/content/Context;)V
    //   29: aload_0
    //   30: putstatic app/gamer/quadstellar/db/FatherDeviceInfoDB.mInstance : Lapp/gamer/quadstellar/db/FatherDeviceInfoDB;
    //   33: ldc app/gamer/quadstellar/db/FatherDeviceInfoDB
    //   35: monitorexit
    //   36: ldc app/gamer/quadstellar/db/FatherDeviceInfoDB
    //   38: monitorexit
    //   39: getstatic app/gamer/quadstellar/db/FatherDeviceInfoDB.mInstance : Lapp/gamer/quadstellar/db/FatherDeviceInfoDB;
    //   42: areturn
    //   43: astore_0
    //   44: ldc app/gamer/quadstellar/db/FatherDeviceInfoDB
    //   46: monitorexit
    //   47: aload_0
    //   48: athrow
    //   49: astore_0
    //   50: ldc app/gamer/quadstellar/db/FatherDeviceInfoDB
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
    this.writeDaoSession.getFatherDeviceInfoDao().deleteAll();
  }
  
  public void deleteUser(String paramString) {
    FatherDeviceInfoDao fatherDeviceInfoDao = this.writeDaoSession.getFatherDeviceInfoDao();
    FatherDeviceInfo fatherDeviceInfo = (FatherDeviceInfo)fatherDeviceInfoDao.queryBuilder().where(FatherDeviceInfoDao.Properties.MacAdrass.eq(paramString), new org.greenrobot.greendao.query.WhereCondition[0]).build().unique();
    if (fatherDeviceInfo != null)
      fatherDeviceInfoDao.deleteByKey(fatherDeviceInfo.getId()); 
  }
  
  public void insertUser(FatherDeviceInfo paramFatherDeviceInfo) {
    this.writeDaoSession.getFatherDeviceInfoDao().insert(paramFatherDeviceInfo);
  }
  
  public void insertUserList(List<FatherDeviceInfo> paramList) {
    if (paramList != null && !paramList.isEmpty())
      this.writeDaoSession.getFatherDeviceInfoDao().insertInTx(paramList); 
  }
  
  public FatherDeviceInfo queryUserList(String paramString) {
    FatherDeviceInfo fatherDeviceInfo = (FatherDeviceInfo)this.readDaoSession.getFatherDeviceInfoDao().queryBuilder().where(FatherDeviceInfoDao.Properties.MacAdrass.eq(paramString), new org.greenrobot.greendao.query.WhereCondition[0]).build().unique();
    Log.d("DeviceInfoDB", "unique:" + fatherDeviceInfo + "回火");
    return fatherDeviceInfo;
  }
  
  public List<FatherDeviceInfo> queryUserList() {
    return this.readDaoSession.getFatherDeviceInfoDao().queryBuilder().build().list();
  }
  
  public void updateUser(FatherDeviceInfo paramFatherDeviceInfo) {
    this.writeDaoSession.getFatherDeviceInfoDao().update(paramFatherDeviceInfo);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/db/FatherDeviceInfoDB.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */