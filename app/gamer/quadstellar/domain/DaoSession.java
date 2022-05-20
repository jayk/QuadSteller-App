package app.gamer.quadstellar.domain;

import java.util.Map;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

public class DaoSession extends AbstractDaoSession {
  private final ColorInfoDao colorInfoDao;
  
  private final DaoConfig colorInfoDaoConfig;
  
  private final DeviceInfoDao deviceInfoDao;
  
  private final DaoConfig deviceInfoDaoConfig;
  
  private final FanDeviceInfoDao fanDeviceInfoDao;
  
  private final DaoConfig fanDeviceInfoDaoConfig;
  
  private final FatherDeviceInfoDao fatherDeviceInfoDao;
  
  private final DaoConfig fatherDeviceInfoDaoConfig;
  
  private final LightDeviceInfoDao lightDeviceInfoDao;
  
  private final DaoConfig lightDeviceInfoDaoConfig;
  
  public DaoSession(Database paramDatabase, IdentityScopeType paramIdentityScopeType, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> paramMap) {
    super(paramDatabase);
    this.colorInfoDaoConfig = ((DaoConfig)paramMap.get(ColorInfoDao.class)).clone();
    this.colorInfoDaoConfig.initIdentityScope(paramIdentityScopeType);
    this.deviceInfoDaoConfig = ((DaoConfig)paramMap.get(DeviceInfoDao.class)).clone();
    this.deviceInfoDaoConfig.initIdentityScope(paramIdentityScopeType);
    this.fanDeviceInfoDaoConfig = ((DaoConfig)paramMap.get(FanDeviceInfoDao.class)).clone();
    this.fanDeviceInfoDaoConfig.initIdentityScope(paramIdentityScopeType);
    this.fatherDeviceInfoDaoConfig = ((DaoConfig)paramMap.get(FatherDeviceInfoDao.class)).clone();
    this.fatherDeviceInfoDaoConfig.initIdentityScope(paramIdentityScopeType);
    this.lightDeviceInfoDaoConfig = ((DaoConfig)paramMap.get(LightDeviceInfoDao.class)).clone();
    this.lightDeviceInfoDaoConfig.initIdentityScope(paramIdentityScopeType);
    this.colorInfoDao = new ColorInfoDao(this.colorInfoDaoConfig, this);
    this.deviceInfoDao = new DeviceInfoDao(this.deviceInfoDaoConfig, this);
    this.fanDeviceInfoDao = new FanDeviceInfoDao(this.fanDeviceInfoDaoConfig, this);
    this.fatherDeviceInfoDao = new FatherDeviceInfoDao(this.fatherDeviceInfoDaoConfig, this);
    this.lightDeviceInfoDao = new LightDeviceInfoDao(this.lightDeviceInfoDaoConfig, this);
    registerDao(ColorInfo.class, this.colorInfoDao);
    registerDao(DeviceInfo.class, this.deviceInfoDao);
    registerDao(FanDeviceInfo.class, this.fanDeviceInfoDao);
    registerDao(FatherDeviceInfo.class, this.fatherDeviceInfoDao);
    registerDao(LightDeviceInfo.class, this.lightDeviceInfoDao);
  }
  
  public void clear() {
    this.colorInfoDaoConfig.clearIdentityScope();
    this.deviceInfoDaoConfig.clearIdentityScope();
    this.fanDeviceInfoDaoConfig.clearIdentityScope();
    this.fatherDeviceInfoDaoConfig.clearIdentityScope();
    this.lightDeviceInfoDaoConfig.clearIdentityScope();
  }
  
  public ColorInfoDao getColorInfoDao() {
    return this.colorInfoDao;
  }
  
  public DeviceInfoDao getDeviceInfoDao() {
    return this.deviceInfoDao;
  }
  
  public FanDeviceInfoDao getFanDeviceInfoDao() {
    return this.fanDeviceInfoDao;
  }
  
  public FatherDeviceInfoDao getFatherDeviceInfoDao() {
    return this.fatherDeviceInfoDao;
  }
  
  public LightDeviceInfoDao getLightDeviceInfoDao() {
    return this.lightDeviceInfoDao;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/domain/DaoSession.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */