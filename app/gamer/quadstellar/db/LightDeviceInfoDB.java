package app.gamer.quadstellar.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import app.gamer.quadstellar.domain.DaoMaster;
import app.gamer.quadstellar.domain.LightDeviceInfo;
import app.gamer.quadstellar.domain.LightDeviceInfoDao;
import java.util.List;
import org.greenrobot.greendao.query.WhereCondition;

public class LightDeviceInfoDB {
  private static final String TAG = "DeviceInfoDB";
  
  private static final String dbName = "deviceinfo.db";
  
  private static LightDeviceInfoDB mInstance;
  
  private Context mContext;
  
  private DaoMaster.DevOpenHelper mOpenHelper;
  
  public LightDeviceInfoDB(Context paramContext) {
    this.mContext = paramContext;
    this.mOpenHelper = new DaoMaster.DevOpenHelper(paramContext, "deviceinfo.db", null);
  }
  
  public static LightDeviceInfoDB getInstance() {
    // Byte code:
    //   0: getstatic app/gamer/quadstellar/db/LightDeviceInfoDB.mInstance : Lapp/gamer/quadstellar/db/LightDeviceInfoDB;
    //   3: ifnonnull -> 39
    //   6: ldc app/gamer/quadstellar/db/LightDeviceInfoDB
    //   8: monitorenter
    //   9: getstatic app/gamer/quadstellar/db/LightDeviceInfoDB.mInstance : Lapp/gamer/quadstellar/db/LightDeviceInfoDB;
    //   12: ifnonnull -> 36
    //   15: ldc app/gamer/quadstellar/db/LightDeviceInfoDB
    //   17: monitorenter
    //   18: new app/gamer/quadstellar/db/LightDeviceInfoDB
    //   21: astore_0
    //   22: aload_0
    //   23: invokestatic getAppContext : ()Landroid/content/Context;
    //   26: invokespecial <init> : (Landroid/content/Context;)V
    //   29: aload_0
    //   30: putstatic app/gamer/quadstellar/db/LightDeviceInfoDB.mInstance : Lapp/gamer/quadstellar/db/LightDeviceInfoDB;
    //   33: ldc app/gamer/quadstellar/db/LightDeviceInfoDB
    //   35: monitorexit
    //   36: ldc app/gamer/quadstellar/db/LightDeviceInfoDB
    //   38: monitorexit
    //   39: getstatic app/gamer/quadstellar/db/LightDeviceInfoDB.mInstance : Lapp/gamer/quadstellar/db/LightDeviceInfoDB;
    //   42: areturn
    //   43: astore_0
    //   44: ldc app/gamer/quadstellar/db/LightDeviceInfoDB
    //   46: monitorexit
    //   47: aload_0
    //   48: athrow
    //   49: astore_0
    //   50: ldc app/gamer/quadstellar/db/LightDeviceInfoDB
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
    (new DaoMaster(getWritableDatabase())).newSession().getLightDeviceInfoDao().deleteAll();
  }
  
  public void deleteUser(String paramString) {
    LightDeviceInfoDao lightDeviceInfoDao = (new DaoMaster(getWritableDatabase())).newSession().getLightDeviceInfoDao();
    List<LightDeviceInfo> list = lightDeviceInfoDao.queryBuilder().where(LightDeviceInfoDao.Properties.MacAdrass.eq(paramString), new WhereCondition[0]).build().list();
    if (list != null)
      for (byte b = 0; b < list.size(); b++) {
        if (list.get(b) != null)
          lightDeviceInfoDao.deleteByKey(((LightDeviceInfo)list.get(b)).getId()); 
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
  
  public void insertUser(LightDeviceInfo paramLightDeviceInfo) {
    LightDeviceInfoDao lightDeviceInfoDao = (new DaoMaster(getWritableDatabase())).newSession().getLightDeviceInfoDao();
    LightDeviceInfo lightDeviceInfo = (LightDeviceInfo)lightDeviceInfoDao.queryBuilder().where(LightDeviceInfoDao.Properties.MacAdrass.eq(paramLightDeviceInfo.getMacAdrass()), new WhereCondition[] { LightDeviceInfoDao.Properties.SonLightNumber.eq(Integer.valueOf(paramLightDeviceInfo.getSonLightNumber())) }).build().unique();
    if (lightDeviceInfo != null) {
      lightDeviceInfoDao.update(lightDeviceInfo);
      return;
    } 
    lightDeviceInfoDao.insert(paramLightDeviceInfo);
  }
  
  public void insertUserList(List<LightDeviceInfo> paramList) {
    if (paramList != null && !paramList.isEmpty())
      (new DaoMaster(getWritableDatabase())).newSession().getLightDeviceInfoDao().insertInTx(paramList); 
  }
  
  public LightDeviceInfo queryUserList(String paramString, int paramInt) {
    LightDeviceInfo lightDeviceInfo = (LightDeviceInfo)(new DaoMaster(getReadableDatabase())).newSession().getLightDeviceInfoDao().queryBuilder().where(LightDeviceInfoDao.Properties.MacAdrass.eq(paramString), new WhereCondition[] { LightDeviceInfoDao.Properties.SonLightNumber.eq(Integer.valueOf(paramInt)) }).build().unique();
    Log.d("DeviceInfoDB", "unique:" + lightDeviceInfo + "回火");
    return lightDeviceInfo;
  }
  
  public List<LightDeviceInfo> queryUserList() {
    return (new DaoMaster(getReadableDatabase())).newSession().getLightDeviceInfoDao().queryBuilder().build().list();
  }
  
  public List<LightDeviceInfo> queryUserList(String paramString) {
    return (new DaoMaster(getReadableDatabase())).newSession().getLightDeviceInfoDao().queryBuilder().where(LightDeviceInfoDao.Properties.MacAdrass.eq(paramString), new WhereCondition[0]).build().list();
  }
  
  public void updateUser(LightDeviceInfo paramLightDeviceInfo) {
    (new DaoMaster(getWritableDatabase())).newSession().getLightDeviceInfoDao().update(paramLightDeviceInfo);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/db/LightDeviceInfoDB.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */