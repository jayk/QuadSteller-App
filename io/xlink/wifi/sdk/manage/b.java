package io.xlink.wifi.sdk.manage;

import android.util.SparseArray;
import io.xlink.wifi.sdk.DataPoint;
import io.xlink.wifi.sdk.XDevice;
import io.xlink.wifi.sdk.encoder.f;
import java.util.HashMap;

public class b {
  private static b a;
  
  private static HashMap<String, SparseArray<DataPoint>> b = new HashMap<String, SparseArray<DataPoint>>();
  
  public static b a() {
    if (a == null)
      a = new b(); 
    return a;
  }
  
  public int a(String paramString) {
    SparseArray sparseArray = b.get(paramString);
    if (sparseArray == null || sparseArray.size() == 0)
      return 0; 
    null = sparseArray.size();
    if (null <= 8)
      return 1; 
    if (null % 8 == 0) {
      null /= 8;
      return null;
    } 
    return null / 8 + 1;
  }
  
  public DataPoint a(XDevice paramXDevice, int paramInt, Object paramObject) {
    null = b.get(paramXDevice.getProductId());
    if (null != null) {
      DataPoint dataPoint = (DataPoint)null.get(paramInt);
      if (dataPoint != null)
        return f.a(paramObject, dataPoint); 
    } 
    return null;
  }
  
  public void a(String paramString, SparseArray<DataPoint> paramSparseArray) {
    b.put(paramString, paramSparseArray);
  }
  
  public SparseArray<DataPoint> b(String paramString) {
    null = b.get(paramString);
    return (null == null) ? null : null.clone();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/manage/b.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */