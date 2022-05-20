package org.xutils.db;

import android.database.Cursor;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import org.xutils.common.util.IOUtil;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.DbModel;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;

public final class DbModelSelector {
  private String[] columnExpressions;
  
  private String groupByColumnName;
  
  private WhereBuilder having;
  
  private Selector<?> selector;
  
  protected DbModelSelector(Selector<?> paramSelector, String paramString) {
    this.selector = paramSelector;
    this.groupByColumnName = paramString;
  }
  
  protected DbModelSelector(Selector<?> paramSelector, String[] paramArrayOfString) {
    this.selector = paramSelector;
    this.columnExpressions = paramArrayOfString;
  }
  
  private DbModelSelector(TableEntity<?> paramTableEntity) {
    this.selector = Selector.from(paramTableEntity);
  }
  
  static DbModelSelector from(TableEntity<?> paramTableEntity) {
    return new DbModelSelector(paramTableEntity);
  }
  
  public DbModelSelector and(String paramString1, String paramString2, Object paramObject) {
    this.selector.and(paramString1, paramString2, paramObject);
    return this;
  }
  
  public DbModelSelector and(WhereBuilder paramWhereBuilder) {
    this.selector.and(paramWhereBuilder);
    return this;
  }
  
  public DbModelSelector expr(String paramString) {
    this.selector.expr(paramString);
    return this;
  }
  
  public List<DbModel> findAll() throws DbException {
    TableEntity<?> tableEntity = this.selector.getTable();
    if (!tableEntity.tableIsExist())
      return null; 
    ArrayList<DbModel> arrayList = null;
    Cursor cursor = tableEntity.getDb().execQuery(toString());
    if (cursor != null)
      try {
        arrayList = new ArrayList();
        this();
      } catch (Throwable throwable) {
      
      } finally {
        IOUtil.closeQuietly(cursor);
      }  
    return arrayList;
  }
  
  public DbModel findFirst() throws DbException {
    TableEntity<?> tableEntity1 = null;
    TableEntity<?> tableEntity2 = this.selector.getTable();
    if (!tableEntity2.tableIsExist())
      return (DbModel)tableEntity1; 
    limit(1);
    Cursor cursor = tableEntity2.getDb().execQuery(toString());
    tableEntity2 = tableEntity1;
    if (cursor != null)
      try {
        if (cursor.moveToNext())
          return CursorUtils.getDbModel(cursor); 
        IOUtil.closeQuietly(cursor);
      } catch (Throwable throwable) {
        DbException dbException = new DbException();
        this(throwable);
        throw dbException;
      } finally {
        IOUtil.closeQuietly(cursor);
      }  
    return (DbModel)tableEntity2;
  }
  
  public TableEntity<?> getTable() {
    return this.selector.getTable();
  }
  
  public DbModelSelector groupBy(String paramString) {
    this.groupByColumnName = paramString;
    return this;
  }
  
  public DbModelSelector having(WhereBuilder paramWhereBuilder) {
    this.having = paramWhereBuilder;
    return this;
  }
  
  public DbModelSelector limit(int paramInt) {
    this.selector.limit(paramInt);
    return this;
  }
  
  public DbModelSelector offset(int paramInt) {
    this.selector.offset(paramInt);
    return this;
  }
  
  public DbModelSelector or(String paramString1, String paramString2, Object paramObject) {
    this.selector.or(paramString1, paramString2, paramObject);
    return this;
  }
  
  public DbModelSelector or(WhereBuilder paramWhereBuilder) {
    this.selector.or(paramWhereBuilder);
    return this;
  }
  
  public DbModelSelector orderBy(String paramString) {
    this.selector.orderBy(paramString);
    return this;
  }
  
  public DbModelSelector orderBy(String paramString, boolean paramBoolean) {
    this.selector.orderBy(paramString, paramBoolean);
    return this;
  }
  
  public DbModelSelector select(String... paramVarArgs) {
    this.columnExpressions = paramVarArgs;
    return this;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("SELECT ");
    if (this.columnExpressions != null && this.columnExpressions.length > 0) {
      String[] arrayOfString = this.columnExpressions;
      int i = arrayOfString.length;
      for (byte b = 0; b < i; b++) {
        stringBuilder.append(arrayOfString[b]);
        stringBuilder.append(",");
      } 
      stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    } else if (!TextUtils.isEmpty(this.groupByColumnName)) {
      stringBuilder.append(this.groupByColumnName);
    } else {
      stringBuilder.append("*");
    } 
    stringBuilder.append(" FROM ").append("\"").append(this.selector.getTable().getName()).append("\"");
    WhereBuilder whereBuilder = this.selector.getWhereBuilder();
    if (whereBuilder != null && whereBuilder.getWhereItemSize() > 0)
      stringBuilder.append(" WHERE ").append(whereBuilder.toString()); 
    if (!TextUtils.isEmpty(this.groupByColumnName)) {
      stringBuilder.append(" GROUP BY ").append("\"").append(this.groupByColumnName).append("\"");
      if (this.having != null && this.having.getWhereItemSize() > 0)
        stringBuilder.append(" HAVING ").append(this.having.toString()); 
    } 
    List<Selector.OrderBy> list = this.selector.getOrderByList();
    if (list != null && list.size() > 0) {
      for (byte b = 0; b < list.size(); b++)
        stringBuilder.append(" ORDER BY ").append(((Selector.OrderBy)list.get(b)).toString()).append(','); 
      stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    } 
    if (this.selector.getLimit() > 0) {
      stringBuilder.append(" LIMIT ").append(this.selector.getLimit());
      stringBuilder.append(" OFFSET ").append(this.selector.getOffset());
    } 
    return stringBuilder.toString();
  }
  
  public DbModelSelector where(String paramString1, String paramString2, Object paramObject) {
    this.selector.where(paramString1, paramString2, paramObject);
    return this;
  }
  
  public DbModelSelector where(WhereBuilder paramWhereBuilder) {
    this.selector.where(paramWhereBuilder);
    return this;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/DbModelSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */