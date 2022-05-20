package org.xutils.db.table;

import android.text.TextUtils;
import java.sql.Date;
import java.util.Date;
import java.util.HashMap;

public final class DbModel {
  private HashMap<String, String> dataMap = new HashMap<String, String>();
  
  public void add(String paramString1, String paramString2) {
    this.dataMap.put(paramString1, paramString2);
  }
  
  public boolean getBoolean(String paramString) {
    paramString = this.dataMap.get(paramString);
    return (paramString != null) ? ((paramString.length() == 1) ? "1".equals(paramString) : Boolean.valueOf(paramString).booleanValue()) : false;
  }
  
  public HashMap<String, String> getDataMap() {
    return this.dataMap;
  }
  
  public Date getDate(String paramString) {
    return new Date(Long.valueOf(this.dataMap.get(paramString)).longValue());
  }
  
  public double getDouble(String paramString) {
    return Double.valueOf(this.dataMap.get(paramString)).doubleValue();
  }
  
  public float getFloat(String paramString) {
    return Float.valueOf(this.dataMap.get(paramString)).floatValue();
  }
  
  public int getInt(String paramString) {
    return Integer.valueOf(this.dataMap.get(paramString)).intValue();
  }
  
  public long getLong(String paramString) {
    return Long.valueOf(this.dataMap.get(paramString)).longValue();
  }
  
  public Date getSqlDate(String paramString) {
    return new Date(Long.valueOf(this.dataMap.get(paramString)).longValue());
  }
  
  public String getString(String paramString) {
    return this.dataMap.get(paramString);
  }
  
  public boolean isEmpty(String paramString) {
    return TextUtils.isEmpty(this.dataMap.get(paramString));
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/db/table/DbModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */