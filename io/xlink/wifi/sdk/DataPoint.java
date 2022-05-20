package io.xlink.wifi.sdk;

import java.io.Serializable;

public class DataPoint implements Serializable {
  private int a;
  
  private Object b;
  
  private int c;
  
  public DataPoint(int paramInt1, int paramInt2) {
    this.a = paramInt1;
    this.c = paramInt2;
    switch (paramInt2) {
      default:
        return;
      case 1:
        this.b = Boolean.valueOf(false);
      case 2:
        this.b = Integer.valueOf(0);
      case 3:
        this.b = Integer.valueOf(0);
      case 4:
        this.b = Byte.valueOf((byte)0);
      case 5:
        break;
    } 
    this.b = "";
  }
  
  public boolean getBooleanValue() {
    try {
      return ((Boolean)this.b).booleanValue();
    } catch (ClassCastException classCastException) {
      throw new ClassCastException("当前端点类型不是boolean,请根据数据模版来获取具体类型的端点值");
    } 
  }
  
  public int getInt32Value() {
    try {
      return ((Integer)this.b).intValue();
    } catch (ClassCastException classCastException) {
      throw new ClassCastException("当前端点类型不是Int32,请根据数据模版来获取具体类型的端点值");
    } 
  }
  
  public int getKey() {
    return this.a;
  }
  
  public Object getObjectValue() {
    return this.b;
  }
  
  public short getShortValue() {
    try {
      return ((Short)this.b).shortValue();
    } catch (ClassCastException classCastException) {
      throw new ClassCastException("当前端点类型不是Short,请根据数据模版来获取具体类型的端点值");
    } 
  }
  
  public int getType() {
    return this.c;
  }
  
  public int getUnsignedByteValue() {
    try {
      byte b = ((Byte)this.b).byteValue();
      return b & 0xFF;
    } catch (ClassCastException classCastException) {
      throw new ClassCastException("当前端点类型不是byte,请根据数据模版来获取具体类型的端点值");
    } 
  }
  
  public void setValue(Object paramObject) {
    this.b = paramObject;
  }
  
  public String toString() {
    return "key :" + this.a + "  value：" + this.b;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/DataPoint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */