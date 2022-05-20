package org.xutils.db;

import android.database.Cursor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.xutils.common.util.IOUtil;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.DbModel;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;

public final class Selector<T> {
  private int limit = 0;
  
  private int offset = 0;
  
  private List<OrderBy> orderByList;
  
  private final TableEntity<T> table;
  
  private WhereBuilder whereBuilder;
  
  private Selector(TableEntity<T> paramTableEntity) {
    this.table = paramTableEntity;
  }
  
  static <T> Selector<T> from(TableEntity<T> paramTableEntity) {
    return new Selector<T>(paramTableEntity);
  }
  
  public Selector<T> and(String paramString1, String paramString2, Object paramObject) {
    this.whereBuilder.and(paramString1, paramString2, paramObject);
    return this;
  }
  
  public Selector<T> and(WhereBuilder paramWhereBuilder) {
    this.whereBuilder.and(paramWhereBuilder);
    return this;
  }
  
  public long count() throws DbException {
    long l = 0L;
    if (this.table.tableIsExist()) {
      DbModel dbModel = select(new String[] { "count(\"" + this.table.getId().getName() + "\") as count" }).findFirst();
      if (dbModel != null)
        l = dbModel.getLong("count"); 
    } 
    return l;
  }
  
  public Selector<T> expr(String paramString) {
    if (this.whereBuilder == null)
      this.whereBuilder = WhereBuilder.b(); 
    this.whereBuilder.expr(paramString);
    return this;
  }
  
  public List<T> findAll() throws DbException {
    if (!this.table.tableIsExist())
      return null; 
    ArrayList<T> arrayList = null;
    Cursor cursor = this.table.getDb().execQuery(toString());
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
  
  public T findFirst() throws DbException {
    T t1 = null;
    if (!this.table.tableIsExist())
      return t1; 
    limit(1);
    Cursor cursor = this.table.getDb().execQuery(toString());
    T t2 = t1;
    if (cursor != null)
      try {
        if (cursor.moveToNext()) {
          t2 = CursorUtils.getEntity(this.table, cursor);
          return t2;
        } 
        IOUtil.closeQuietly(cursor);
      } catch (Throwable throwable) {
        DbException dbException = new DbException();
        this(throwable);
        throw dbException;
      } finally {
        IOUtil.closeQuietly(cursor);
      }  
    return t2;
  }
  
  public int getLimit() {
    return this.limit;
  }
  
  public int getOffset() {
    return this.offset;
  }
  
  public List<OrderBy> getOrderByList() {
    return this.orderByList;
  }
  
  public TableEntity<T> getTable() {
    return this.table;
  }
  
  public WhereBuilder getWhereBuilder() {
    return this.whereBuilder;
  }
  
  public DbModelSelector groupBy(String paramString) {
    return new DbModelSelector(this, paramString);
  }
  
  public Selector<T> limit(int paramInt) {
    this.limit = paramInt;
    return this;
  }
  
  public Selector<T> offset(int paramInt) {
    this.offset = paramInt;
    return this;
  }
  
  public Selector<T> or(String paramString1, String paramString2, Object paramObject) {
    this.whereBuilder.or(paramString1, paramString2, paramObject);
    return this;
  }
  
  public Selector or(WhereBuilder paramWhereBuilder) {
    this.whereBuilder.or(paramWhereBuilder);
    return this;
  }
  
  public Selector<T> orderBy(String paramString) {
    if (this.orderByList == null)
      this.orderByList = new ArrayList<OrderBy>(5); 
    this.orderByList.add(new OrderBy(paramString));
    return this;
  }
  
  public Selector<T> orderBy(String paramString, boolean paramBoolean) {
    if (this.orderByList == null)
      this.orderByList = new ArrayList<OrderBy>(5); 
    this.orderByList.add(new OrderBy(paramString, paramBoolean));
    return this;
  }
  
  public DbModelSelector select(String... paramVarArgs) {
    return new DbModelSelector(this, paramVarArgs);
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("SELECT ");
    stringBuilder.append("*");
    stringBuilder.append(" FROM ").append("\"").append(this.table.getName()).append("\"");
    if (this.whereBuilder != null && this.whereBuilder.getWhereItemSize() > 0)
      stringBuilder.append(" WHERE ").append(this.whereBuilder.toString()); 
    if (this.orderByList != null && this.orderByList.size() > 0) {
      stringBuilder.append(" ORDER BY ");
      Iterator<OrderBy> iterator = this.orderByList.iterator();
      while (iterator.hasNext())
        stringBuilder.append(((OrderBy)iterator.next()).toString()).append(','); 
      stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    } 
    if (this.limit > 0) {
      stringBuilder.append(" LIMIT ").append(this.limit);
      stringBuilder.append(" OFFSET ").append(this.offset);
    } 
    return stringBuilder.toString();
  }
  
  public Selector<T> where(String paramString1, String paramString2, Object paramObject) {
    this.whereBuilder = WhereBuilder.b(paramString1, paramString2, paramObject);
    return this;
  }
  
  public Selector<T> where(WhereBuilder paramWhereBuilder) {
    this.whereBuilder = paramWhereBuilder;
    return this;
  }
  
  public static class OrderBy {
    private String columnName;
    
    private boolean desc;
    
    public OrderBy(String param1String) {
      this.columnName = param1String;
    }
    
    public OrderBy(String param1String, boolean param1Boolean) {
      this.columnName = param1String;
      this.desc = param1Boolean;
    }
    
    public String toString() {
      StringBuilder stringBuilder = (new StringBuilder()).append("\"").append(this.columnName).append("\"");
      if (this.desc) {
        String str1 = " DESC";
        return stringBuilder.append(str1).toString();
      } 
      String str = " ASC";
      return stringBuilder.append(str).toString();
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/Selector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */