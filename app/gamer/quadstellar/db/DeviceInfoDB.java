package app.gamer.quadstellar.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import app.gamer.quadstellar.domain.DaoMaster;
import app.gamer.quadstellar.domain.DeviceInfo;
import app.gamer.quadstellar.domain.DeviceInfoDao;
import java.util.List;

public class DeviceInfoDB {
  private static final String TAG = "DeviceInfoDB";
  
  private static final String dbName = "deviceinfo.db";
  
  private static DeviceInfoDB mInstance;
  
  private Context mContext;
  
  private DaoMaster.DevOpenHelper mOpenHelper;
  
  public DeviceInfoDB(Context paramContext) {
    this.mContext = paramContext;
    this.mOpenHelper = new DaoMaster.DevOpenHelper(paramContext, "deviceinfo.db", null);
  }
  
  public static DeviceInfoDB getInstance(Context paramContext) {
    // Byte code:
    //   0: getstatic app/gamer/quadstellar/db/DeviceInfoDB.mInstance : Lapp/gamer/quadstellar/db/DeviceInfoDB;
    //   3: ifnonnull -> 37
    //   6: ldc app/gamer/quadstellar/db/DeviceInfoDB
    //   8: monitorenter
    //   9: getstatic app/gamer/quadstellar/db/DeviceInfoDB.mInstance : Lapp/gamer/quadstellar/db/DeviceInfoDB;
    //   12: ifnonnull -> 34
    //   15: ldc app/gamer/quadstellar/db/DeviceInfoDB
    //   17: monitorenter
    //   18: new app/gamer/quadstellar/db/DeviceInfoDB
    //   21: astore_1
    //   22: aload_1
    //   23: aload_0
    //   24: invokespecial <init> : (Landroid/content/Context;)V
    //   27: aload_1
    //   28: putstatic app/gamer/quadstellar/db/DeviceInfoDB.mInstance : Lapp/gamer/quadstellar/db/DeviceInfoDB;
    //   31: ldc app/gamer/quadstellar/db/DeviceInfoDB
    //   33: monitorexit
    //   34: ldc app/gamer/quadstellar/db/DeviceInfoDB
    //   36: monitorexit
    //   37: getstatic app/gamer/quadstellar/db/DeviceInfoDB.mInstance : Lapp/gamer/quadstellar/db/DeviceInfoDB;
    //   40: areturn
    //   41: astore_0
    //   42: ldc app/gamer/quadstellar/db/DeviceInfoDB
    //   44: monitorexit
    //   45: aload_0
    //   46: athrow
    //   47: astore_0
    //   48: ldc app/gamer/quadstellar/db/DeviceInfoDB
    //   50: monitorexit
    //   51: aload_0
    //   52: athrow
    // Exception table:
    //   from	to	target	type
    //   9	18	47	finally
    //   18	34	41	finally
    //   34	37	47	finally
    //   42	45	41	finally
    //   45	47	47	finally
    //   48	51	47	finally
  }
  
  public void deleteAll() {
    (new DaoMaster(getWritableDatabase())).newSession().getDeviceInfoDao().deleteAll();
  }
  
  public void deleteUser(String paramString) {
    DeviceInfoDao deviceInfoDao = (new DaoMaster(getWritableDatabase())).newSession().getDeviceInfoDao();
    DeviceInfo deviceInfo = (DeviceInfo)deviceInfoDao.queryBuilder().where(DeviceInfoDao.Properties.MacDrass.eq(paramString), new org.greenrobot.greendao.query.WhereCondition[0]).build().unique();
    if (deviceInfo != null)
      deviceInfoDao.deleteByKey(deviceInfo.getId()); 
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
  
  public void insertUser(DeviceInfo paramDeviceInfo) {
    (new DaoMaster(getWritableDatabase())).newSession().getDeviceInfoDao().insert(paramDeviceInfo);
  }
  
  public void insertUserList(List<DeviceInfo> paramList) {
    if (paramList != null && !paramList.isEmpty())
      (new DaoMaster(getWritableDatabase())).newSession().getDeviceInfoDao().insertInTx(paramList); 
  }
  
  public DeviceInfo queryUserList(String paramString) {
    DeviceInfo deviceInfo = (DeviceInfo)(new DaoMaster(getReadableDatabase())).newSession().getDeviceInfoDao().queryBuilder().where(DeviceInfoDao.Properties.MacDrass.eq(paramString), new org.greenrobot.greendao.query.WhereCondition[0]).build().unique();
    Log.d("DeviceInfoDB", "unique:" + deviceInfo + "回火");
    return deviceInfo;
  }
  
  public List<DeviceInfo> queryUserList() {
    return (new DaoMaster(getReadableDatabase())).newSession().getDeviceInfoDao().queryBuilder().build().list();
  }
  
  public void updateUser(DeviceInfo paramDeviceInfo) {
    (new DaoMaster(getWritableDatabase())).newSession().getDeviceInfoDao().update(paramDeviceInfo);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/db/DeviceInfoDB.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */