package org.greenrobot.greendao.internal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScope;
import org.greenrobot.greendao.identityscope.IdentityScopeLong;
import org.greenrobot.greendao.identityscope.IdentityScopeObject;
import org.greenrobot.greendao.identityscope.IdentityScopeType;

public final class DaoConfig implements Cloneable {
  public final String[] allColumns;
  
  public final Database db;
  
  private IdentityScope<?, ?> identityScope;
  
  public final boolean keyIsNumeric;
  
  public final String[] nonPkColumns;
  
  public final String[] pkColumns;
  
  public final Property pkProperty;
  
  public final Property[] properties;
  
  public final TableStatements statements;
  
  public final String tablename;
  
  public DaoConfig(Database paramDatabase, Class<? extends AbstractDao<?, ?>> paramClass) {
    ArrayList<String> arrayList1;
    ArrayList<String> arrayList2;
    this.db = paramDatabase;
    try {
      this.tablename = (String)paramClass.getField("TABLENAME").get(null);
      Property[] arrayOfProperty = reflectProperties(paramClass);
      this.properties = arrayOfProperty;
      this.allColumns = new String[arrayOfProperty.length];
      arrayList1 = new ArrayList();
      this();
      arrayList2 = new ArrayList();
      this();
      paramClass = null;
      for (byte b = 0; b < arrayOfProperty.length; b++) {
        Property property = arrayOfProperty[b];
        String str = property.columnName;
        this.allColumns[b] = str;
        if (property.primaryKey) {
          arrayList1.add(str);
          Property property1 = property;
        } else {
          arrayList2.add(str);
        } 
      } 
    } catch (Exception exception) {
      throw new DaoException("Could not init DAOConfig", exception);
    } 
    this.nonPkColumns = arrayList2.<String>toArray(new String[arrayList2.size()]);
    this.pkColumns = arrayList1.<String>toArray(new String[arrayList1.size()]);
    if (this.pkColumns.length != 1)
      paramClass = null; 
    this.pkProperty = (Property)paramClass;
    TableStatements tableStatements = new TableStatements();
    this((Database)exception, this.tablename, this.allColumns, this.pkColumns);
    this.statements = tableStatements;
    if (this.pkProperty != null) {
      boolean bool;
      Class clazz = this.pkProperty.type;
      if (clazz.equals(long.class) || clazz.equals(Long.class) || clazz.equals(int.class) || clazz.equals(Integer.class) || clazz.equals(short.class) || clazz.equals(Short.class) || clazz.equals(byte.class) || clazz.equals(Byte.class)) {
        bool = true;
      } else {
        bool = false;
      } 
      this.keyIsNumeric = bool;
      return;
    } 
    this.keyIsNumeric = false;
  }
  
  public DaoConfig(DaoConfig paramDaoConfig) {
    this.db = paramDaoConfig.db;
    this.tablename = paramDaoConfig.tablename;
    this.properties = paramDaoConfig.properties;
    this.allColumns = paramDaoConfig.allColumns;
    this.pkColumns = paramDaoConfig.pkColumns;
    this.nonPkColumns = paramDaoConfig.nonPkColumns;
    this.pkProperty = paramDaoConfig.pkProperty;
    this.statements = paramDaoConfig.statements;
    this.keyIsNumeric = paramDaoConfig.keyIsNumeric;
  }
  
  private static Property[] reflectProperties(Class<? extends AbstractDao<?, ?>> paramClass) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
    Field[] arrayOfField = Class.forName(paramClass.getName() + "$Properties").getDeclaredFields();
    ArrayList<Property> arrayList = new ArrayList();
    int i = arrayOfField.length;
    for (byte b = 0; b < i; b++) {
      Field field = arrayOfField[b];
      if ((field.getModifiers() & 0x9) == 9) {
        Object object = field.get(null);
        if (object instanceof Property)
          arrayList.add((Property)object); 
      } 
    } 
    Property[] arrayOfProperty = new Property[arrayList.size()];
    for (Property property : arrayList) {
      if (arrayOfProperty[property.ordinal] != null)
        throw new DaoException("Duplicate property ordinals"); 
      arrayOfProperty[property.ordinal] = property;
    } 
    return arrayOfProperty;
  }
  
  public void clearIdentityScope() {
    IdentityScope<?, ?> identityScope = this.identityScope;
    if (identityScope != null)
      identityScope.clear(); 
  }
  
  public DaoConfig clone() {
    return new DaoConfig(this);
  }
  
  public IdentityScope<?, ?> getIdentityScope() {
    return this.identityScope;
  }
  
  public void initIdentityScope(IdentityScopeType paramIdentityScopeType) {
    if (paramIdentityScopeType == IdentityScopeType.None) {
      this.identityScope = null;
      return;
    } 
    if (paramIdentityScopeType == IdentityScopeType.Session) {
      if (this.keyIsNumeric) {
        this.identityScope = (IdentityScope<?, ?>)new IdentityScopeLong();
        return;
      } 
      this.identityScope = (IdentityScope<?, ?>)new IdentityScopeObject();
      return;
    } 
    throw new IllegalArgumentException("Unsupported type: " + paramIdentityScopeType);
  }
  
  public void setIdentityScope(IdentityScope<?, ?> paramIdentityScope) {
    this.identityScope = paramIdentityScope;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/internal/DaoConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */