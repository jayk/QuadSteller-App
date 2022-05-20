package org.xutils.db.table;

import android.database.Cursor;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.Iterator;
import org.xutils.DbManager;
import org.xutils.common.util.IOUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.db.sqlite.SqlInfoBuilder;
import org.xutils.ex.DbException;

public abstract class DbBase implements DbManager {
  private final HashMap<Class<?>, TableEntity<?>> tableMap = new HashMap<Class<?>, TableEntity<?>>();
  
  public void addColumn(Class<?> paramClass, String paramString) throws DbException {
    TableEntity<?> tableEntity = getTable(paramClass);
    ColumnEntity columnEntity = tableEntity.getColumnMap().get(paramString);
    if (columnEntity != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("ALTER TABLE ").append("\"").append(tableEntity.getName()).append("\"").append(" ADD COLUMN ").append("\"").append(columnEntity.getName()).append("\"").append(" ").append(columnEntity.getColumnDbType()).append(" ").append(columnEntity.getProperty());
      execNonQuery(stringBuilder.toString());
    } 
  }
  
  protected void createTableIfNotExist(TableEntity<?> paramTableEntity) throws DbException {
    if (!paramTableEntity.tableIsExist())
      synchronized (paramTableEntity.getClass()) {
        if (!paramTableEntity.tableIsExist()) {
          execNonQuery(SqlInfoBuilder.buildCreateTableSqlInfo(paramTableEntity));
          String str = paramTableEntity.getOnCreated();
          if (!TextUtils.isEmpty(str))
            execNonQuery(str); 
          paramTableEntity.setCheckedDatabase(true);
          DbManager.TableCreateListener tableCreateListener = getDaoConfig().getTableCreateListener();
          if (tableCreateListener != null)
            tableCreateListener.onTableCreated(this, paramTableEntity); 
        } 
        return;
      }  
  }
  
  public void dropDb() throws DbException {
    Cursor cursor = execQuery("SELECT name FROM sqlite_master WHERE type='table' AND name<>'sqlite_sequence'");
    if (cursor != null)
      try {
        while (true) {
          boolean bool = cursor.moveToNext();
          if (bool) {
            try {
              String str = cursor.getString(0);
              StringBuilder stringBuilder = new StringBuilder();
              this();
              execNonQuery(stringBuilder.append("DROP TABLE ").append(str).toString());
            } catch (Throwable throwable) {
              LogUtil.e(throwable.getMessage(), throwable);
            } 
            continue;
          } 
          synchronized (this.tableMap) {
            Iterator<TableEntity> iterator = this.tableMap.values().iterator();
            while (iterator.hasNext())
              ((TableEntity)iterator.next()).setCheckedDatabase(false); 
          } 
          this.tableMap.clear();
          /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_4} */
          IOUtil.closeQuietly(cursor);
          break;
        } 
      } catch (Throwable throwable) {
        DbException dbException = new DbException();
        this(throwable);
        throw dbException;
      } finally {
        IOUtil.closeQuietly(cursor);
      }  
  }
  
  public void dropTable(Class<?> paramClass) throws DbException {
    TableEntity<?> tableEntity = getTable(paramClass);
    if (tableEntity.tableIsExist()) {
      execNonQuery("DROP TABLE \"" + tableEntity.getName() + "\"");
      tableEntity.setCheckedDatabase(false);
      removeTable(paramClass);
    } 
  }
  
  public <T> TableEntity<T> getTable(Class<T> paramClass) throws DbException {
    synchronized (this.tableMap) {
      DbException dbException;
      TableEntity<?> tableEntity1 = this.tableMap.get(paramClass);
      TableEntity<?> tableEntity2 = tableEntity1;
      if (tableEntity1 == null)
        try {
          tableEntity2 = new TableEntity();
          this(this, paramClass);
          this.tableMap.put(paramClass, tableEntity2);
          return (TableEntity)tableEntity2;
        } catch (Throwable throwable) {
          dbException = new DbException();
          this(throwable);
          throw dbException;
        }  
      return (TableEntity<T>)dbException;
    } 
  }
  
  protected void removeTable(Class<?> paramClass) {
    synchronized (this.tableMap) {
      this.tableMap.remove(paramClass);
      return;
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/table/DbBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */