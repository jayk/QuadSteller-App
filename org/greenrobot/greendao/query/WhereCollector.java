package org.greenrobot.greendao.query;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.Property;

class WhereCollector<T> {
  private final AbstractDao<T, ?> dao;
  
  private final String tablePrefix;
  
  private final List<WhereCondition> whereConditions;
  
  WhereCollector(AbstractDao<T, ?> paramAbstractDao, String paramString) {
    this.dao = paramAbstractDao;
    this.tablePrefix = paramString;
    this.whereConditions = new ArrayList<WhereCondition>();
  }
  
  void add(WhereCondition paramWhereCondition, WhereCondition... paramVarArgs) {
    checkCondition(paramWhereCondition);
    this.whereConditions.add(paramWhereCondition);
    int i = paramVarArgs.length;
    for (byte b = 0; b < i; b++) {
      paramWhereCondition = paramVarArgs[b];
      checkCondition(paramWhereCondition);
      this.whereConditions.add(paramWhereCondition);
    } 
  }
  
  void addCondition(StringBuilder paramStringBuilder, List<Object> paramList, WhereCondition paramWhereCondition) {
    checkCondition(paramWhereCondition);
    paramWhereCondition.appendTo(paramStringBuilder, this.tablePrefix);
    paramWhereCondition.appendValuesTo(paramList);
  }
  
  void appendWhereClause(StringBuilder paramStringBuilder, String paramString, List<Object> paramList) {
    ListIterator<WhereCondition> listIterator = this.whereConditions.listIterator();
    while (listIterator.hasNext()) {
      if (listIterator.hasPrevious())
        paramStringBuilder.append(" AND "); 
      WhereCondition whereCondition = listIterator.next();
      whereCondition.appendTo(paramStringBuilder, paramString);
      whereCondition.appendValuesTo(paramList);
    } 
  }
  
  void checkCondition(WhereCondition paramWhereCondition) {
    if (paramWhereCondition instanceof WhereCondition.PropertyCondition)
      checkProperty(((WhereCondition.PropertyCondition)paramWhereCondition).property); 
  }
  
  void checkProperty(Property paramProperty) {
    if (this.dao != null) {
      Property[] arrayOfProperty = this.dao.getProperties();
      boolean bool = false;
      int i = arrayOfProperty.length;
      byte b = 0;
      while (true) {
        boolean bool1 = bool;
        if (b < i)
          if (paramProperty == arrayOfProperty[b]) {
            bool1 = true;
          } else {
            b++;
            continue;
          }  
        if (!bool1)
          throw new DaoException("Property '" + paramProperty.name + "' is not part of " + this.dao); 
        break;
      } 
    } 
  }
  
  WhereCondition combineWhereConditions(String paramString, WhereCondition paramWhereCondition1, WhereCondition paramWhereCondition2, WhereCondition... paramVarArgs) {
    StringBuilder stringBuilder = new StringBuilder("(");
    ArrayList<Object> arrayList = new ArrayList();
    addCondition(stringBuilder, arrayList, paramWhereCondition1);
    stringBuilder.append(paramString);
    addCondition(stringBuilder, arrayList, paramWhereCondition2);
    int i = paramVarArgs.length;
    for (byte b = 0; b < i; b++) {
      paramWhereCondition1 = paramVarArgs[b];
      stringBuilder.append(paramString);
      addCondition(stringBuilder, arrayList, paramWhereCondition1);
    } 
    stringBuilder.append(')');
    return new WhereCondition.StringCondition(stringBuilder.toString(), arrayList.toArray());
  }
  
  boolean isEmpty() {
    return this.whereConditions.isEmpty();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/query/WhereCollector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */