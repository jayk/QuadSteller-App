package org.xutils.db.table;

import android.database.Cursor;
import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import org.xutils.DbManager;
import org.xutils.common.util.IOUtil;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

public final class TableEntity<T> {
  private volatile boolean checkedDatabase;
  
  private final LinkedHashMap<String, ColumnEntity> columnMap;
  
  private Constructor<T> constructor;
  
  private final DbManager db;
  
  private Class<T> entityType;
  
  private ColumnEntity id;
  
  private final String name;
  
  private final String onCreated;
  
  TableEntity(DbManager paramDbManager, Class<T> paramClass) throws Throwable {
    this.db = paramDbManager;
    this.entityType = paramClass;
    this.constructor = paramClass.getConstructor(new Class[0]);
    this.constructor.setAccessible(true);
    Table table = paramClass.<Table>getAnnotation(Table.class);
    this.name = table.name();
    this.onCreated = table.onCreated();
    this.columnMap = TableUtils.findColumnMap(paramClass);
    for (ColumnEntity columnEntity : this.columnMap.values()) {
      if (columnEntity.isId()) {
        this.id = columnEntity;
        break;
      } 
    } 
  }
  
  public T createEntity() throws Throwable {
    return this.constructor.newInstance(new Object[0]);
  }
  
  public LinkedHashMap<String, ColumnEntity> getColumnMap() {
    return this.columnMap;
  }
  
  public DbManager getDb() {
    return this.db;
  }
  
  public Class<T> getEntityType() {
    return this.entityType;
  }
  
  public ColumnEntity getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getOnCreated() {
    return this.onCreated;
  }
  
  boolean isCheckedDatabase() {
    return this.checkedDatabase;
  }
  
  void setCheckedDatabase(boolean paramBoolean) {
    this.checkedDatabase = paramBoolean;
  }
  
  public boolean tableIsExist() throws DbException {
    boolean bool = true;
    if (isCheckedDatabase())
      return bool; 
    Cursor cursor = this.db.execQuery("SELECT COUNT(*) AS c FROM sqlite_master WHERE type='table' AND name='" + this.name + "'");
    if (cursor != null) {
      try {
        if (cursor.moveToNext() && cursor.getInt(0) > 0) {
          setCheckedDatabase(true);
          return bool;
        } 
        IOUtil.closeQuietly(cursor);
      } catch (Throwable throwable) {
        DbException dbException = new DbException();
        this(throwable);
        throw dbException;
      } finally {
        IOUtil.closeQuietly(cursor);
      } 
      return bool;
    } 
    bool = false;
  }
  
  public String toString() {
    return this.name;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/table/TableEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */