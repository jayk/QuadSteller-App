package app.gamer.quadstellar.mode.dao;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

public class DBHelper {
  private static DbManager db;
  
  public static DbManager getdbInstance() {
    // Byte code:
    //   0: ldc app/gamer/quadstellar/mode/dao/DBHelper
    //   2: monitorenter
    //   3: getstatic app/gamer/quadstellar/mode/dao/DBHelper.db : Lorg/xutils/DbManager;
    //   6: ifnonnull -> 61
    //   9: new org/xutils/DbManager$DaoConfig
    //   12: astore_0
    //   13: aload_0
    //   14: invokespecial <init> : ()V
    //   17: aload_0
    //   18: ldc 'intelligent.db'
    //   20: invokevirtual setDbName : (Ljava/lang/String;)Lorg/xutils/DbManager$DaoConfig;
    //   23: iconst_1
    //   24: invokevirtual setDbVersion : (I)Lorg/xutils/DbManager$DaoConfig;
    //   27: astore_0
    //   28: new app/gamer/quadstellar/mode/dao/DBHelper$2
    //   31: astore_1
    //   32: aload_1
    //   33: invokespecial <init> : ()V
    //   36: aload_0
    //   37: aload_1
    //   38: invokevirtual setDbOpenListener : (Lorg/xutils/DbManager$DbOpenListener;)Lorg/xutils/DbManager$DaoConfig;
    //   41: astore_1
    //   42: new app/gamer/quadstellar/mode/dao/DBHelper$1
    //   45: astore_0
    //   46: aload_0
    //   47: invokespecial <init> : ()V
    //   50: aload_1
    //   51: aload_0
    //   52: invokevirtual setDbUpgradeListener : (Lorg/xutils/DbManager$DbUpgradeListener;)Lorg/xutils/DbManager$DaoConfig;
    //   55: invokestatic getDb : (Lorg/xutils/DbManager$DaoConfig;)Lorg/xutils/DbManager;
    //   58: putstatic app/gamer/quadstellar/mode/dao/DBHelper.db : Lorg/xutils/DbManager;
    //   61: getstatic app/gamer/quadstellar/mode/dao/DBHelper.db : Lorg/xutils/DbManager;
    //   64: astore_0
    //   65: ldc app/gamer/quadstellar/mode/dao/DBHelper
    //   67: monitorexit
    //   68: aload_0
    //   69: areturn
    //   70: astore_0
    //   71: ldc app/gamer/quadstellar/mode/dao/DBHelper
    //   73: monitorexit
    //   74: aload_0
    //   75: athrow
    // Exception table:
    //   from	to	target	type
    //   3	61	70	finally
    //   61	65	70	finally
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/mode/dao/DBHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */