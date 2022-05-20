package org.greenrobot.greendao.test;

import org.greenrobot.greendao.AbstractDao;

public abstract class AbstractDaoTestStringPk<D extends AbstractDao<T, String>, T> extends AbstractDaoTestSinglePk<D, T, String> {
  public AbstractDaoTestStringPk(Class<D> paramClass) {
    super(paramClass);
  }
  
  protected String createRandomPk() {
    int i = this.random.nextInt(30);
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b < i + 1; b++)
      stringBuilder.append((char)(this.random.nextInt(25) + 97)); 
    return stringBuilder.toString();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/test/AbstractDaoTestStringPk.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */