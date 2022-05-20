package org.greenrobot.greendao.database;

public interface DatabaseStatement {
  void bindBlob(int paramInt, byte[] paramArrayOfbyte);
  
  void bindDouble(int paramInt, double paramDouble);
  
  void bindLong(int paramInt, long paramLong);
  
  void bindNull(int paramInt);
  
  void bindString(int paramInt, String paramString);
  
  void clearBindings();
  
  void close();
  
  void execute();
  
  long executeInsert();
  
  Object getRawStatement();
  
  long simpleQueryForLong();
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/database/DatabaseStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */