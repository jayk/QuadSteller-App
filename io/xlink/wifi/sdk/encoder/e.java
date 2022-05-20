package io.xlink.wifi.sdk.encoder;

import io.xlink.wifi.sdk.XDevice;
import java.util.HashMap;

public class e {
  private static final HashMap<String, g> d = new HashMap<String, g>();
  
  public XDevice a;
  
  public int b;
  
  public int c;
  
  public e(int paramInt) {
    this.b = paramInt;
  }
  
  public static g a(int paramInt) {
    return d.get(paramInt + "");
  }
  
  public static void a(String paramString) {
    d.remove(paramString);
  }
  
  public static void a(String paramString, g paramg) {
    d.put(paramString, paramg);
  }
  
  public static e b(int paramInt) {
    return new e(paramInt);
  }
  
  public static g b(String paramString) {
    return d.get(paramString);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/encoder/e.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */