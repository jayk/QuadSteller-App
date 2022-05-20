package org.greenrobot.greendao.query;

import java.util.Date;
import org.greenrobot.greendao.AbstractDao;

abstract class AbstractQueryWithLimit<T> extends AbstractQuery<T> {
  protected final int limitPosition;
  
  protected final int offsetPosition;
  
  protected AbstractQueryWithLimit(AbstractDao<T, ?> paramAbstractDao, String paramString, String[] paramArrayOfString, int paramInt1, int paramInt2) {
    super(paramAbstractDao, paramString, paramArrayOfString);
    this.limitPosition = paramInt1;
    this.offsetPosition = paramInt2;
  }
  
  public void setLimit(int paramInt) {
    checkThread();
    if (this.limitPosition == -1)
      throw new IllegalStateException("Limit must be set with QueryBuilder before it can be used here"); 
    this.parameters[this.limitPosition] = Integer.toString(paramInt);
  }
  
  public void setOffset(int paramInt) {
    checkThread();
    if (this.offsetPosition == -1)
      throw new IllegalStateException("Offset must be set with QueryBuilder before it can be used here"); 
    this.parameters[this.offsetPosition] = Integer.toString(paramInt);
  }
  
  public AbstractQueryWithLimit<T> setParameter(int paramInt, Boolean paramBoolean) {
    if (paramBoolean != null) {
      boolean bool;
      if (paramBoolean.booleanValue()) {
        bool = true;
      } else {
        bool = false;
      } 
      Integer integer = Integer.valueOf(bool);
      return setParameter(paramInt, integer);
    } 
    paramBoolean = null;
    return setParameter(paramInt, paramBoolean);
  }
  
  public AbstractQueryWithLimit<T> setParameter(int paramInt, Object paramObject) {
    if (paramInt >= 0 && (paramInt == this.limitPosition || paramInt == this.offsetPosition))
      throw new IllegalArgumentException("Illegal parameter index: " + paramInt); 
    return (AbstractQueryWithLimit<T>)super.setParameter(paramInt, paramObject);
  }
  
  public AbstractQueryWithLimit<T> setParameter(int paramInt, Date paramDate) {
    if (paramDate != null) {
      Long long_ = Long.valueOf(paramDate.getTime());
      return setParameter(paramInt, long_);
    } 
    paramDate = null;
    return setParameter(paramInt, paramDate);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/query/AbstractQueryWithLimit.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */