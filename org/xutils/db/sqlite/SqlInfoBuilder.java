package org.xutils.db.sqlite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.xutils.common.util.KeyValue;
import org.xutils.db.table.ColumnEntity;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;

public final class SqlInfoBuilder {
  private static final ConcurrentHashMap<TableEntity<?>, String> INSERT_SQL_CACHE = new ConcurrentHashMap<TableEntity<?>, String>();
  
  private static final ConcurrentHashMap<TableEntity<?>, String> REPLACE_SQL_CACHE = new ConcurrentHashMap<TableEntity<?>, String>();
  
  public static SqlInfo buildCreateTableSqlInfo(TableEntity<?> paramTableEntity) throws DbException {
    columnEntity = paramTableEntity.getId();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("CREATE TABLE IF NOT EXISTS ");
    stringBuilder.append("\"").append(paramTableEntity.getName()).append("\"");
    stringBuilder.append(" ( ");
    if (columnEntity.isAutoId()) {
      stringBuilder.append("\"").append(columnEntity.getName()).append("\"").append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
    } else {
      stringBuilder.append("\"").append(columnEntity.getName()).append("\"").append(columnEntity.getColumnDbType()).append(" PRIMARY KEY, ");
    } 
    for (ColumnEntity columnEntity : paramTableEntity.getColumnMap().values()) {
      if (!columnEntity.isId()) {
        stringBuilder.append("\"").append(columnEntity.getName()).append("\"");
        stringBuilder.append(' ').append(columnEntity.getColumnDbType());
        stringBuilder.append(' ').append(columnEntity.getProperty());
        stringBuilder.append(',');
      } 
    } 
    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    stringBuilder.append(" )");
    return new SqlInfo(stringBuilder.toString());
  }
  
  public static SqlInfo buildDeleteSqlInfo(TableEntity<?> paramTableEntity, Object paramObject) throws DbException {
    SqlInfo sqlInfo = new SqlInfo();
    ColumnEntity columnEntity = paramTableEntity.getId();
    paramObject = columnEntity.getColumnValue(paramObject);
    if (paramObject == null)
      throw new DbException("this entity[" + paramTableEntity.getEntityType() + "]'s id value is null"); 
    StringBuilder stringBuilder = new StringBuilder("DELETE FROM ");
    stringBuilder.append("\"").append(paramTableEntity.getName()).append("\"");
    stringBuilder.append(" WHERE ").append(WhereBuilder.b(columnEntity.getName(), "=", paramObject));
    sqlInfo.setSql(stringBuilder.toString());
    return sqlInfo;
  }
  
  public static SqlInfo buildDeleteSqlInfo(TableEntity<?> paramTableEntity, WhereBuilder paramWhereBuilder) throws DbException {
    StringBuilder stringBuilder = new StringBuilder("DELETE FROM ");
    stringBuilder.append("\"").append(paramTableEntity.getName()).append("\"");
    if (paramWhereBuilder != null && paramWhereBuilder.getWhereItemSize() > 0)
      stringBuilder.append(" WHERE ").append(paramWhereBuilder.toString()); 
    return new SqlInfo(stringBuilder.toString());
  }
  
  public static SqlInfo buildDeleteSqlInfoById(TableEntity<?> paramTableEntity, Object paramObject) throws DbException {
    SqlInfo sqlInfo = new SqlInfo();
    ColumnEntity columnEntity = paramTableEntity.getId();
    if (paramObject == null)
      throw new DbException("this entity[" + paramTableEntity.getEntityType() + "]'s id value is null"); 
    StringBuilder stringBuilder = new StringBuilder("DELETE FROM ");
    stringBuilder.append("\"").append(paramTableEntity.getName()).append("\"");
    stringBuilder.append(" WHERE ").append(WhereBuilder.b(columnEntity.getName(), "=", paramObject));
    sqlInfo.setSql(stringBuilder.toString());
    return sqlInfo;
  }
  
  public static SqlInfo buildInsertSqlInfo(TableEntity<?> paramTableEntity, Object paramObject) throws DbException {
    List<KeyValue> list = entity2KeyValueList(paramTableEntity, paramObject);
    if (list.size() == 0)
      return null; 
    paramObject = new SqlInfo();
    String str = INSERT_SQL_CACHE.get(paramTableEntity);
    if (str == null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("INSERT INTO ");
      stringBuilder.append("\"").append(paramTableEntity.getName()).append("\"");
      stringBuilder.append(" (");
      for (KeyValue keyValue : list)
        stringBuilder.append("\"").append(keyValue.key).append("\"").append(','); 
      stringBuilder.deleteCharAt(stringBuilder.length() - 1);
      stringBuilder.append(") VALUES (");
      int i = list.size();
      for (byte b = 0; b < i; b++)
        stringBuilder.append("?,"); 
      stringBuilder.deleteCharAt(stringBuilder.length() - 1);
      stringBuilder.append(")");
      str = stringBuilder.toString();
      paramObject.setSql(str);
      paramObject.addBindArgs(list);
      INSERT_SQL_CACHE.put(paramTableEntity, str);
      return (SqlInfo)paramObject;
    } 
    paramObject.setSql(str);
    paramObject.addBindArgs(list);
    return (SqlInfo)paramObject;
  }
  
  public static SqlInfo buildReplaceSqlInfo(TableEntity<?> paramTableEntity, Object paramObject) throws DbException {
    List<KeyValue> list = entity2KeyValueList(paramTableEntity, paramObject);
    if (list.size() == 0)
      return null; 
    paramObject = new SqlInfo();
    String str = REPLACE_SQL_CACHE.get(paramTableEntity);
    if (str == null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("REPLACE INTO ");
      stringBuilder.append("\"").append(paramTableEntity.getName()).append("\"");
      stringBuilder.append(" (");
      for (KeyValue keyValue : list)
        stringBuilder.append("\"").append(keyValue.key).append("\"").append(','); 
      stringBuilder.deleteCharAt(stringBuilder.length() - 1);
      stringBuilder.append(") VALUES (");
      int i = list.size();
      for (byte b = 0; b < i; b++)
        stringBuilder.append("?,"); 
      stringBuilder.deleteCharAt(stringBuilder.length() - 1);
      stringBuilder.append(")");
      str = stringBuilder.toString();
      paramObject.setSql(str);
      paramObject.addBindArgs(list);
      REPLACE_SQL_CACHE.put(paramTableEntity, str);
      return (SqlInfo)paramObject;
    } 
    paramObject.setSql(str);
    paramObject.addBindArgs(list);
    return (SqlInfo)paramObject;
  }
  
  public static SqlInfo buildUpdateSqlInfo(TableEntity<?> paramTableEntity, Object paramObject, String... paramVarArgs) throws DbException {
    List<KeyValue> list = entity2KeyValueList(paramTableEntity, paramObject);
    if (list.size() == 0)
      return null; 
    HashSet<? super String> hashSet1 = null;
    HashSet<? super String> hashSet2 = hashSet1;
    if (paramVarArgs != null) {
      hashSet2 = hashSet1;
      if (paramVarArgs.length > 0) {
        hashSet2 = new HashSet(paramVarArgs.length);
        Collections.addAll(hashSet2, paramVarArgs);
      } 
    } 
    ColumnEntity columnEntity = paramTableEntity.getId();
    Object object = columnEntity.getColumnValue(paramObject);
    if (object == null)
      throw new DbException("this entity[" + paramTableEntity.getEntityType() + "]'s id value is null"); 
    paramObject = new SqlInfo();
    StringBuilder stringBuilder = new StringBuilder("UPDATE ");
    stringBuilder.append("\"").append(paramTableEntity.getName()).append("\"");
    stringBuilder.append(" SET ");
    for (KeyValue keyValue : list) {
      if (hashSet2 == null || hashSet2.contains(keyValue.key)) {
        stringBuilder.append("\"").append(keyValue.key).append("\"").append("=?,");
        paramObject.addBindArg(keyValue);
      } 
    } 
    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    stringBuilder.append(" WHERE ").append(WhereBuilder.b(columnEntity.getName(), "=", object));
    paramObject.setSql(stringBuilder.toString());
    return (SqlInfo)paramObject;
  }
  
  public static SqlInfo buildUpdateSqlInfo(TableEntity<?> paramTableEntity, WhereBuilder paramWhereBuilder, KeyValue... paramVarArgs) throws DbException {
    if (paramVarArgs == null || paramVarArgs.length == 0)
      return null; 
    SqlInfo sqlInfo = new SqlInfo();
    StringBuilder stringBuilder = new StringBuilder("UPDATE ");
    stringBuilder.append("\"").append(paramTableEntity.getName()).append("\"");
    stringBuilder.append(" SET ");
    int i = paramVarArgs.length;
    for (byte b = 0; b < i; b++) {
      KeyValue keyValue = paramVarArgs[b];
      stringBuilder.append("\"").append(keyValue.key).append("\"").append("=?,");
      sqlInfo.addBindArg(keyValue);
    } 
    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    if (paramWhereBuilder != null && paramWhereBuilder.getWhereItemSize() > 0)
      stringBuilder.append(" WHERE ").append(paramWhereBuilder.toString()); 
    sqlInfo.setSql(stringBuilder.toString());
    return sqlInfo;
  }
  
  private static KeyValue column2KeyValue(Object paramObject, ColumnEntity paramColumnEntity) {
    return paramColumnEntity.isAutoId() ? null : new KeyValue(paramColumnEntity.getName(), paramColumnEntity.getFieldValue(paramObject));
  }
  
  public static List<KeyValue> entity2KeyValueList(TableEntity<?> paramTableEntity, Object paramObject) {
    Collection collection = paramTableEntity.getColumnMap().values();
    ArrayList<KeyValue> arrayList = new ArrayList(collection.size());
    Iterator<ColumnEntity> iterator = collection.iterator();
    while (iterator.hasNext()) {
      KeyValue keyValue = column2KeyValue(paramObject, iterator.next());
      if (keyValue != null)
        arrayList.add(keyValue); 
    } 
    return arrayList;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/sqlite/SqlInfoBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */