package app.gamer.quadstellar.mode.dao;

import app.gamer.quadstellar.mode.EntityBase;
import java.util.List;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

public class DaoUtil {
  public static <T extends EntityBase> void delete(T paramT) {
    try {
      DBHelper.getdbInstance().delete(paramT);
    } catch (DbException dbException) {
      dbException.printStackTrace();
    } 
  }
  
  public static <T extends EntityBase> void delete(Class<T> paramClass, WhereBuilder paramWhereBuilder) {
    try {
      DBHelper.getdbInstance().delete(paramClass, paramWhereBuilder);
    } catch (DbException dbException) {
      dbException.printStackTrace();
    } 
  }
  
  public static <T extends EntityBase> void deleteById(Class<T> paramClass, Object paramObject) {
    try {
      DBHelper.getdbInstance().deleteById(paramClass, paramObject);
    } catch (DbException dbException) {
      dbException.printStackTrace();
    } 
  }
  
  public static void dropDb() {
    try {
      DBHelper.getdbInstance().dropDb();
    } catch (DbException dbException) {
      dbException.printStackTrace();
    } 
  }
  
  public static <T extends EntityBase> void dropTable(Class<T> paramClass) {
    try {
      DBHelper.getdbInstance().dropTable(paramClass);
    } catch (DbException dbException) {
      dbException.printStackTrace();
    } 
  }
  
  public static <T extends EntityBase> List<T> getAllList(Class<T> paramClass) {
    DbException dbException2 = null;
    try {
      List list = DBHelper.getdbInstance().findAll(paramClass);
    } catch (DbException dbException1) {
      dbException1.printStackTrace();
      dbException1 = dbException2;
    } 
    return (List<T>)dbException1;
  }
  
  public static <T extends EntityBase> List<T> getAllList(Class<T> paramClass, WhereBuilder paramWhereBuilder) {
    DbException dbException2 = null;
    try {
      List list = DBHelper.getdbInstance().selector(paramClass).where(paramWhereBuilder).findAll();
    } catch (DbException dbException1) {
      dbException1.printStackTrace();
      dbException1 = dbException2;
    } 
    return (List<T>)dbException1;
  }
  
  public static <T extends EntityBase> T queryOne(Class<T> paramClass, WhereBuilder paramWhereBuilder) {
    DbException dbException2 = null;
    try {
      EntityBase entityBase = (EntityBase)DBHelper.getdbInstance().selector(paramClass).where(paramWhereBuilder).findFirst();
    } catch (DbException dbException1) {
      dbException1.printStackTrace();
      dbException1 = dbException2;
    } 
    return (T)dbException1;
  }
  
  public static <T extends EntityBase> void saveOrUpdate(T paramT) {
    try {
      DBHelper.getdbInstance().saveOrUpdate(paramT);
    } catch (DbException dbException) {
      dbException.printStackTrace();
    } 
  }
  
  public static <T extends EntityBase> void update(T paramT, String... paramVarArgs) {
    try {
      DBHelper.getdbInstance().update(paramT, paramVarArgs);
    } catch (DbException dbException) {
      dbException.printStackTrace();
    } 
  }
  
  public static <T extends EntityBase> void update(Class<T> paramClass, WhereBuilder paramWhereBuilder, KeyValue paramKeyValue) {
    try {
      DBHelper.getdbInstance().update(paramClass, paramWhereBuilder, new KeyValue[] { paramKeyValue });
    } catch (DbException dbException) {
      dbException.printStackTrace();
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/mode/dao/DaoUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */