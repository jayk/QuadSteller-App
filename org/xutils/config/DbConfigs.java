package org.xutils.config;

import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;

public enum DbConfigs {
  COOKIE,
  HTTP((new DbManager.DaoConfig()).setDbName("xUtils_http_cache.db").setDbVersion(1).setDbOpenListener(new DbManager.DbOpenListener() {
        public void onDbOpened(DbManager param1DbManager) {
          param1DbManager.getDatabase().enableWriteAheadLogging();
        }
      }).setDbUpgradeListener(new DbManager.DbUpgradeListener() {
        public void onUpgrade(DbManager param1DbManager, int param1Int1, int param1Int2) {
          try {
            param1DbManager.dropDb();
          } catch (DbException dbException) {
            LogUtil.e(dbException.getMessage(), (Throwable)dbException);
          } 
        }
      }));
  
  private DbManager.DaoConfig config;
  
  static {
    COOKIE = new DbConfigs("COOKIE", 1, (new DbManager.DaoConfig()).setDbName("xUtils_http_cookie.db").setDbVersion(1).setDbOpenListener(new DbManager.DbOpenListener() {
            public void onDbOpened(DbManager param1DbManager) {
              param1DbManager.getDatabase().enableWriteAheadLogging();
            }
          }).setDbUpgradeListener(new DbManager.DbUpgradeListener() {
            public void onUpgrade(DbManager param1DbManager, int param1Int1, int param1Int2) {
              try {
                param1DbManager.dropDb();
              } catch (DbException dbException) {
                LogUtil.e(dbException.getMessage(), (Throwable)dbException);
              } 
            }
          }));
    $VALUES = new DbConfigs[] { HTTP, COOKIE };
  }
  
  DbConfigs(DbManager.DaoConfig paramDaoConfig) {
    this.config = paramDaoConfig;
  }
  
  public DbManager.DaoConfig getConfig() {
    return this.config;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/config/DbConfigs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */