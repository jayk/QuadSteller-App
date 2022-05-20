package org.greenrobot.greendao.query;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;

public class Join<SRC, DST> {
  final AbstractDao<DST, ?> daoDestination;
  
  final Property joinPropertyDestination;
  
  final Property joinPropertySource;
  
  final String sourceTablePrefix;
  
  final String tablePrefix;
  
  final WhereCollector<DST> whereCollector;
  
  public Join(String paramString1, Property paramProperty1, AbstractDao<DST, ?> paramAbstractDao, Property paramProperty2, String paramString2) {
    this.sourceTablePrefix = paramString1;
    this.joinPropertySource = paramProperty1;
    this.daoDestination = paramAbstractDao;
    this.joinPropertyDestination = paramProperty2;
    this.tablePrefix = paramString2;
    this.whereCollector = new WhereCollector<DST>(paramAbstractDao, paramString2);
  }
  
  public WhereCondition and(WhereCondition paramWhereCondition1, WhereCondition paramWhereCondition2, WhereCondition... paramVarArgs) {
    return this.whereCollector.combineWhereConditions(" AND ", paramWhereCondition1, paramWhereCondition2, paramVarArgs);
  }
  
  public String getTablePrefix() {
    return this.tablePrefix;
  }
  
  public WhereCondition or(WhereCondition paramWhereCondition1, WhereCondition paramWhereCondition2, WhereCondition... paramVarArgs) {
    return this.whereCollector.combineWhereConditions(" OR ", paramWhereCondition1, paramWhereCondition2, paramVarArgs);
  }
  
  public Join<SRC, DST> where(WhereCondition paramWhereCondition, WhereCondition... paramVarArgs) {
    this.whereCollector.add(paramWhereCondition, paramVarArgs);
    return this;
  }
  
  public Join<SRC, DST> whereOr(WhereCondition paramWhereCondition1, WhereCondition paramWhereCondition2, WhereCondition... paramVarArgs) {
    this.whereCollector.add(or(paramWhereCondition1, paramWhereCondition2, paramVarArgs), new WhereCondition[0]);
    return this;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/query/Join.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */