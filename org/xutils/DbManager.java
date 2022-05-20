package org.xutils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.xutils.common.util.KeyValue;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.DbModel;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;

public interface DbManager extends Closeable {
  void addColumn(Class<?> paramClass, String paramString) throws DbException;
  
  void close() throws IOException;
  
  int delete(Class<?> paramClass, WhereBuilder paramWhereBuilder) throws DbException;
  
  void delete(Class<?> paramClass) throws DbException;
  
  void delete(Object paramObject) throws DbException;
  
  void deleteById(Class<?> paramClass, Object paramObject) throws DbException;
  
  void dropDb() throws DbException;
  
  void dropTable(Class<?> paramClass) throws DbException;
  
  void execNonQuery(String paramString) throws DbException;
  
  void execNonQuery(SqlInfo paramSqlInfo) throws DbException;
  
  Cursor execQuery(String paramString) throws DbException;
  
  Cursor execQuery(SqlInfo paramSqlInfo) throws DbException;
  
  int executeUpdateDelete(String paramString) throws DbException;
  
  int executeUpdateDelete(SqlInfo paramSqlInfo) throws DbException;
  
  <T> List<T> findAll(Class<T> paramClass) throws DbException;
  
  <T> T findById(Class<T> paramClass, Object paramObject) throws DbException;
  
  List<DbModel> findDbModelAll(SqlInfo paramSqlInfo) throws DbException;
  
  DbModel findDbModelFirst(SqlInfo paramSqlInfo) throws DbException;
  
  <T> T findFirst(Class<T> paramClass) throws DbException;
  
  DaoConfig getDaoConfig();
  
  SQLiteDatabase getDatabase();
  
  <T> TableEntity<T> getTable(Class<T> paramClass) throws DbException;
  
  void replace(Object paramObject) throws DbException;
  
  void save(Object paramObject) throws DbException;
  
  boolean saveBindingId(Object paramObject) throws DbException;
  
  void saveOrUpdate(Object paramObject) throws DbException;
  
  <T> Selector<T> selector(Class<T> paramClass) throws DbException;
  
  int update(Class<?> paramClass, WhereBuilder paramWhereBuilder, KeyValue... paramVarArgs) throws DbException;
  
  void update(Object paramObject, String... paramVarArgs) throws DbException;
  
  public static class DaoConfig {
    private boolean allowTransaction = true;
    
    private File dbDir;
    
    private String dbName = "xUtils.db";
    
    private DbManager.DbOpenListener dbOpenListener;
    
    private DbManager.DbUpgradeListener dbUpgradeListener;
    
    private int dbVersion = 1;
    
    private DbManager.TableCreateListener tableCreateListener;
    
    public boolean equals(Object param1Object) {
      boolean bool = true;
      if (this != param1Object) {
        if (param1Object == null || getClass() != param1Object.getClass())
          return false; 
        param1Object = param1Object;
        if (!this.dbName.equals(((DaoConfig)param1Object).dbName))
          return false; 
        if (this.dbDir == null) {
          if (((DaoConfig)param1Object).dbDir != null)
            bool = false; 
          return bool;
        } 
        bool = this.dbDir.equals(((DaoConfig)param1Object).dbDir);
      } 
      return bool;
    }
    
    public File getDbDir() {
      return this.dbDir;
    }
    
    public String getDbName() {
      return this.dbName;
    }
    
    public DbManager.DbOpenListener getDbOpenListener() {
      return this.dbOpenListener;
    }
    
    public DbManager.DbUpgradeListener getDbUpgradeListener() {
      return this.dbUpgradeListener;
    }
    
    public int getDbVersion() {
      return this.dbVersion;
    }
    
    public DbManager.TableCreateListener getTableCreateListener() {
      return this.tableCreateListener;
    }
    
    public int hashCode() {
      int i = this.dbName.hashCode();
      if (this.dbDir != null) {
        int j = this.dbDir.hashCode();
        return i * 31 + j;
      } 
      byte b = 0;
      return i * 31 + b;
    }
    
    public boolean isAllowTransaction() {
      return this.allowTransaction;
    }
    
    public DaoConfig setAllowTransaction(boolean param1Boolean) {
      this.allowTransaction = param1Boolean;
      return this;
    }
    
    public DaoConfig setDbDir(File param1File) {
      this.dbDir = param1File;
      return this;
    }
    
    public DaoConfig setDbName(String param1String) {
      if (!TextUtils.isEmpty(param1String))
        this.dbName = param1String; 
      return this;
    }
    
    public DaoConfig setDbOpenListener(DbManager.DbOpenListener param1DbOpenListener) {
      this.dbOpenListener = param1DbOpenListener;
      return this;
    }
    
    public DaoConfig setDbUpgradeListener(DbManager.DbUpgradeListener param1DbUpgradeListener) {
      this.dbUpgradeListener = param1DbUpgradeListener;
      return this;
    }
    
    public DaoConfig setDbVersion(int param1Int) {
      this.dbVersion = param1Int;
      return this;
    }
    
    public DaoConfig setTableCreateListener(DbManager.TableCreateListener param1TableCreateListener) {
      this.tableCreateListener = param1TableCreateListener;
      return this;
    }
    
    public String toString() {
      return String.valueOf(this.dbDir) + "/" + this.dbName;
    }
  }
  
  public static interface DbOpenListener {
    void onDbOpened(DbManager param1DbManager);
  }
  
  public static interface DbUpgradeListener {
    void onUpgrade(DbManager param1DbManager, int param1Int1, int param1Int2);
  }
  
  public static interface TableCreateListener {
    void onTableCreated(DbManager param1DbManager, TableEntity<?> param1TableEntity);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/DbManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */