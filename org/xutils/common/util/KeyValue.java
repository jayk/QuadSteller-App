package org.xutils.common.util;

public class KeyValue {
  public final String key;
  
  public final Object value;
  
  public KeyValue(String paramString, Object paramObject) {
    this.key = paramString;
    this.value = paramObject;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = true;
    if (this != paramObject) {
      if (paramObject == null || getClass() != paramObject.getClass())
        return false; 
      paramObject = paramObject;
      if (this.key == null) {
        if (((KeyValue)paramObject).key != null)
          bool = false; 
        return bool;
      } 
      bool = this.key.equals(((KeyValue)paramObject).key);
    } 
    return bool;
  }
  
  public String getValueStr() {
    return (this.value == null) ? null : this.value.toString();
  }
  
  public int hashCode() {
    return (this.key != null) ? this.key.hashCode() : 0;
  }
  
  public String toString() {
    return "KeyValue{key='" + this.key + '\'' + ", value=" + this.value + '}';
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/common/util/KeyValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */