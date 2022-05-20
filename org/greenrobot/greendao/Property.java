package org.greenrobot.greendao;

import java.util.Collection;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.query.WhereCondition;

public class Property {
  public final String columnName;
  
  public final String name;
  
  public final int ordinal;
  
  public final boolean primaryKey;
  
  public final Class<?> type;
  
  public Property(int paramInt, Class<?> paramClass, String paramString1, boolean paramBoolean, String paramString2) {
    this.ordinal = paramInt;
    this.type = paramClass;
    this.name = paramString1;
    this.primaryKey = paramBoolean;
    this.columnName = paramString2;
  }
  
  public WhereCondition between(Object paramObject1, Object paramObject2) {
    return (WhereCondition)new WhereCondition.PropertyCondition(this, " BETWEEN ? AND ?", new Object[] { paramObject1, paramObject2 });
  }
  
  public WhereCondition eq(Object paramObject) {
    return (WhereCondition)new WhereCondition.PropertyCondition(this, "=?", paramObject);
  }
  
  public WhereCondition ge(Object paramObject) {
    return (WhereCondition)new WhereCondition.PropertyCondition(this, ">=?", paramObject);
  }
  
  public WhereCondition gt(Object paramObject) {
    return (WhereCondition)new WhereCondition.PropertyCondition(this, ">?", paramObject);
  }
  
  public WhereCondition in(Collection<?> paramCollection) {
    return in(paramCollection.toArray());
  }
  
  public WhereCondition in(Object... paramVarArgs) {
    StringBuilder stringBuilder = new StringBuilder(" IN (");
    SqlUtils.appendPlaceholders(stringBuilder, paramVarArgs.length).append(')');
    return (WhereCondition)new WhereCondition.PropertyCondition(this, stringBuilder.toString(), paramVarArgs);
  }
  
  public WhereCondition isNotNull() {
    return (WhereCondition)new WhereCondition.PropertyCondition(this, " IS NOT NULL");
  }
  
  public WhereCondition isNull() {
    return (WhereCondition)new WhereCondition.PropertyCondition(this, " IS NULL");
  }
  
  public WhereCondition le(Object paramObject) {
    return (WhereCondition)new WhereCondition.PropertyCondition(this, "<=?", paramObject);
  }
  
  public WhereCondition like(String paramString) {
    return (WhereCondition)new WhereCondition.PropertyCondition(this, " LIKE ?", paramString);
  }
  
  public WhereCondition lt(Object paramObject) {
    return (WhereCondition)new WhereCondition.PropertyCondition(this, "<?", paramObject);
  }
  
  public WhereCondition notEq(Object paramObject) {
    return (WhereCondition)new WhereCondition.PropertyCondition(this, "<>?", paramObject);
  }
  
  public WhereCondition notIn(Collection<?> paramCollection) {
    return notIn(paramCollection.toArray());
  }
  
  public WhereCondition notIn(Object... paramVarArgs) {
    StringBuilder stringBuilder = new StringBuilder(" NOT IN (");
    SqlUtils.appendPlaceholders(stringBuilder, paramVarArgs.length).append(')');
    return (WhereCondition)new WhereCondition.PropertyCondition(this, stringBuilder.toString(), paramVarArgs);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/Property.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */