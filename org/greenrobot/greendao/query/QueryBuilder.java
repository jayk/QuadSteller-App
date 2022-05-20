package org.greenrobot.greendao.query;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.DaoLog;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.annotation.apihint.Experimental;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.rx.RxQuery;

public class QueryBuilder<T> {
  public static boolean LOG_SQL;
  
  public static boolean LOG_VALUES;
  
  private final AbstractDao<T, ?> dao;
  
  private boolean distinct;
  
  private final List<Join<T, ?>> joins;
  
  private Integer limit;
  
  private Integer offset;
  
  private StringBuilder orderBuilder;
  
  private String stringOrderCollation;
  
  private final String tablePrefix;
  
  private final List<Object> values;
  
  private final WhereCollector<T> whereCollector;
  
  protected QueryBuilder(AbstractDao<T, ?> paramAbstractDao) {
    this(paramAbstractDao, "T");
  }
  
  protected QueryBuilder(AbstractDao<T, ?> paramAbstractDao, String paramString) {
    this.dao = paramAbstractDao;
    this.tablePrefix = paramString;
    this.values = new ArrayList();
    this.joins = new ArrayList<Join<T, ?>>();
    this.whereCollector = new WhereCollector<T>(paramAbstractDao, paramString);
    this.stringOrderCollation = " COLLATE NOCASE";
  }
  
  private <J> Join<T, J> addJoin(String paramString, Property paramProperty1, AbstractDao<J, ?> paramAbstractDao, Property paramProperty2) {
    Join<Object, J> join = new Join<Object, J>(paramString, paramProperty1, paramAbstractDao, paramProperty2, "J" + (this.joins.size() + 1));
    this.joins.add(join);
    return (Join)join;
  }
  
  private void appendJoinsAndWheres(StringBuilder paramStringBuilder, String paramString) {
    boolean bool;
    this.values.clear();
    for (Join<T, ?> join : this.joins) {
      paramStringBuilder.append(" JOIN ").append(join.daoDestination.getTablename()).append(' ');
      paramStringBuilder.append(join.tablePrefix).append(" ON ");
      SqlUtils.appendProperty(paramStringBuilder, join.sourceTablePrefix, join.joinPropertySource).append('=');
      SqlUtils.appendProperty(paramStringBuilder, join.tablePrefix, join.joinPropertyDestination);
    } 
    if (!this.whereCollector.isEmpty()) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool) {
      paramStringBuilder.append(" WHERE ");
      this.whereCollector.appendWhereClause(paramStringBuilder, paramString, this.values);
    } 
    for (Join<T, ?> join : this.joins) {
      if (!join.whereCollector.isEmpty()) {
        if (!bool) {
          paramStringBuilder.append(" WHERE ");
          bool = true;
        } else {
          paramStringBuilder.append(" AND ");
        } 
        join.whereCollector.appendWhereClause(paramStringBuilder, join.tablePrefix, this.values);
      } 
    } 
  }
  
  private int checkAddLimit(StringBuilder paramStringBuilder) {
    int i = -1;
    if (this.limit != null) {
      paramStringBuilder.append(" LIMIT ?");
      this.values.add(this.limit);
      i = this.values.size() - 1;
    } 
    return i;
  }
  
  private int checkAddOffset(StringBuilder paramStringBuilder) {
    int i = -1;
    if (this.offset != null) {
      if (this.limit == null)
        throw new IllegalStateException("Offset cannot be set without limit"); 
      paramStringBuilder.append(" OFFSET ?");
      this.values.add(this.offset);
      i = this.values.size() - 1;
    } 
    return i;
  }
  
  private void checkLog(String paramString) {
    if (LOG_SQL)
      DaoLog.d("Built SQL for query: " + paramString); 
    if (LOG_VALUES)
      DaoLog.d("Values for query: " + this.values); 
  }
  
  private void checkOrderBuilder() {
    if (this.orderBuilder == null) {
      this.orderBuilder = new StringBuilder();
      return;
    } 
    if (this.orderBuilder.length() > 0)
      this.orderBuilder.append(","); 
  }
  
  private StringBuilder createSelectBuilder() {
    StringBuilder stringBuilder = new StringBuilder(SqlUtils.createSqlSelect(this.dao.getTablename(), this.tablePrefix, this.dao.getAllColumns(), this.distinct));
    appendJoinsAndWheres(stringBuilder, this.tablePrefix);
    if (this.orderBuilder != null && this.orderBuilder.length() > 0)
      stringBuilder.append(" ORDER BY ").append(this.orderBuilder); 
    return stringBuilder;
  }
  
  public static <T2> QueryBuilder<T2> internalCreate(AbstractDao<T2, ?> paramAbstractDao) {
    return new QueryBuilder<T2>(paramAbstractDao);
  }
  
  private void orderAscOrDesc(String paramString, Property... paramVarArgs) {
    int i = paramVarArgs.length;
    for (byte b = 0; b < i; b++) {
      Property property = paramVarArgs[b];
      checkOrderBuilder();
      append(this.orderBuilder, property);
      if (String.class.equals(property.type) && this.stringOrderCollation != null)
        this.orderBuilder.append(this.stringOrderCollation); 
      this.orderBuilder.append(paramString);
    } 
  }
  
  public WhereCondition and(WhereCondition paramWhereCondition1, WhereCondition paramWhereCondition2, WhereCondition... paramVarArgs) {
    return this.whereCollector.combineWhereConditions(" AND ", paramWhereCondition1, paramWhereCondition2, paramVarArgs);
  }
  
  protected StringBuilder append(StringBuilder paramStringBuilder, Property paramProperty) {
    this.whereCollector.checkProperty(paramProperty);
    paramStringBuilder.append(this.tablePrefix).append('.').append('\'').append(paramProperty.columnName).append('\'');
    return paramStringBuilder;
  }
  
  public Query<T> build() {
    StringBuilder stringBuilder = createSelectBuilder();
    int i = checkAddLimit(stringBuilder);
    int j = checkAddOffset(stringBuilder);
    String str = stringBuilder.toString();
    checkLog(str);
    return Query.create(this.dao, str, this.values.toArray(), i, j);
  }
  
  public CountQuery<T> buildCount() {
    StringBuilder stringBuilder = new StringBuilder(SqlUtils.createSqlSelectCountStar(this.dao.getTablename(), this.tablePrefix));
    appendJoinsAndWheres(stringBuilder, this.tablePrefix);
    String str = stringBuilder.toString();
    checkLog(str);
    return CountQuery.create(this.dao, str, this.values.toArray());
  }
  
  public CursorQuery buildCursor() {
    StringBuilder stringBuilder = createSelectBuilder();
    int i = checkAddLimit(stringBuilder);
    int j = checkAddOffset(stringBuilder);
    String str = stringBuilder.toString();
    checkLog(str);
    return CursorQuery.create(this.dao, str, this.values.toArray(), i, j);
  }
  
  public DeleteQuery<T> buildDelete() {
    if (!this.joins.isEmpty())
      throw new DaoException("JOINs are not supported for DELETE queries"); 
    String str = this.dao.getTablename();
    StringBuilder stringBuilder = new StringBuilder(SqlUtils.createSqlDelete(str, null));
    appendJoinsAndWheres(stringBuilder, this.tablePrefix);
    str = stringBuilder.toString().replace(this.tablePrefix + ".\"", '"' + str + "\".\"");
    checkLog(str);
    return DeleteQuery.create(this.dao, str, this.values.toArray());
  }
  
  public long count() {
    return buildCount().count();
  }
  
  public QueryBuilder<T> distinct() {
    this.distinct = true;
    return this;
  }
  
  public <J> Join<T, J> join(Class<J> paramClass, Property paramProperty) {
    return join(this.dao.getPkProperty(), paramClass, paramProperty);
  }
  
  public <J> Join<T, J> join(Property paramProperty, Class<J> paramClass) {
    AbstractDao<J, ?> abstractDao = this.dao.getSession().getDao(paramClass);
    Property property = abstractDao.getPkProperty();
    return addJoin(this.tablePrefix, paramProperty, abstractDao, property);
  }
  
  public <J> Join<T, J> join(Property paramProperty1, Class<J> paramClass, Property paramProperty2) {
    AbstractDao<J, ?> abstractDao = this.dao.getSession().getDao(paramClass);
    return addJoin(this.tablePrefix, paramProperty1, abstractDao, paramProperty2);
  }
  
  public <J> Join<T, J> join(Join<?, T> paramJoin, Property paramProperty1, Class<J> paramClass, Property paramProperty2) {
    AbstractDao<J, ?> abstractDao = this.dao.getSession().getDao(paramClass);
    return addJoin(paramJoin.tablePrefix, paramProperty1, abstractDao, paramProperty2);
  }
  
  public QueryBuilder<T> limit(int paramInt) {
    this.limit = Integer.valueOf(paramInt);
    return this;
  }
  
  public List<T> list() {
    return build().list();
  }
  
  public CloseableListIterator<T> listIterator() {
    return build().listIterator();
  }
  
  public LazyList<T> listLazy() {
    return build().listLazy();
  }
  
  public LazyList<T> listLazyUncached() {
    return build().listLazyUncached();
  }
  
  public QueryBuilder<T> offset(int paramInt) {
    this.offset = Integer.valueOf(paramInt);
    return this;
  }
  
  public WhereCondition or(WhereCondition paramWhereCondition1, WhereCondition paramWhereCondition2, WhereCondition... paramVarArgs) {
    return this.whereCollector.combineWhereConditions(" OR ", paramWhereCondition1, paramWhereCondition2, paramVarArgs);
  }
  
  public QueryBuilder<T> orderAsc(Property... paramVarArgs) {
    orderAscOrDesc(" ASC", paramVarArgs);
    return this;
  }
  
  public QueryBuilder<T> orderCustom(Property paramProperty, String paramString) {
    checkOrderBuilder();
    append(this.orderBuilder, paramProperty).append(' ');
    this.orderBuilder.append(paramString);
    return this;
  }
  
  public QueryBuilder<T> orderDesc(Property... paramVarArgs) {
    orderAscOrDesc(" DESC", paramVarArgs);
    return this;
  }
  
  public QueryBuilder<T> orderRaw(String paramString) {
    checkOrderBuilder();
    this.orderBuilder.append(paramString);
    return this;
  }
  
  public QueryBuilder<T> preferLocalizedStringOrder() {
    if (this.dao.getDatabase().getRawDatabase() instanceof android.database.sqlite.SQLiteDatabase)
      this.stringOrderCollation = " COLLATE LOCALIZED"; 
    return this;
  }
  
  @Experimental
  public RxQuery<T> rx() {
    return build().__InternalRx();
  }
  
  @Experimental
  public RxQuery<T> rxPlain() {
    return build().__internalRxPlain();
  }
  
  public QueryBuilder<T> stringOrderCollation(String paramString) {
    if (this.dao.getDatabase().getRawDatabase() instanceof android.database.sqlite.SQLiteDatabase) {
      String str = paramString;
      if (paramString != null)
        if (paramString.startsWith(" ")) {
          str = paramString;
        } else {
          str = " " + paramString;
        }  
      this.stringOrderCollation = str;
    } 
    return this;
  }
  
  public T unique() {
    return build().unique();
  }
  
  public T uniqueOrThrow() {
    return build().uniqueOrThrow();
  }
  
  public QueryBuilder<T> where(WhereCondition paramWhereCondition, WhereCondition... paramVarArgs) {
    this.whereCollector.add(paramWhereCondition, paramVarArgs);
    return this;
  }
  
  public QueryBuilder<T> whereOr(WhereCondition paramWhereCondition1, WhereCondition paramWhereCondition2, WhereCondition... paramVarArgs) {
    this.whereCollector.add(or(paramWhereCondition1, paramWhereCondition2, paramVarArgs), new WhereCondition[0]);
    return this;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/query/QueryBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */