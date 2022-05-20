package io.xlink.wifi.sdk;

import io.xlink.wifi.sdk.event.a;
import io.xlink.wifi.sdk.event.b;
import io.xlink.wifi.sdk.event.c;
import io.xlink.wifi.sdk.global.a;
import io.xlink.wifi.sdk.listener.ConnectDeviceListener;
import io.xlink.wifi.sdk.util.MyLog;
import io.xlink.wifi.sdk.util.b;
import java.net.InetAddress;
import org.json.JSONException;
import org.json.JSONObject;

public class a {
  public static a a;
  
  private final String b = "pid";
  
  private final String c = "mac";
  
  private final String d = "dname";
  
  private final String e = "ip";
  
  private final String f = "did";
  
  private final String g = "port";
  
  private final String h = "version";
  
  private final String i = "mhv";
  
  private final String j = "msv";
  
  private final String k = "accesskey";
  
  private final String l = "init";
  
  private final String m = "protocol";
  
  private final String n = "device";
  
  private final String o = "macAddress";
  
  private final String p = "version";
  
  private final String q = "deviceID";
  
  private final String r = "deviceName";
  
  private final String s = "deviceIP";
  
  private final String t = "devicePort";
  
  private final String u = "deviceInit";
  
  private final String v = "mcuHardVersion";
  
  private final String w = "mucSoftVersion";
  
  private final String x = "mcuSoftVersion";
  
  private final String y = "productID";
  
  private final String z = "accesskey";
  
  public static a a() {
    if (a == null)
      a = new a(); 
    return a;
  }
  
  public int a(XDevice paramXDevice, int paramInt1, b paramb, int paramInt2) {
    return XlinkAgent.getInstance().a(paramXDevice, paramInt1, paramb, paramInt2);
  }
  
  public int a(XDevice paramXDevice, a parama) {
    return XlinkAgent.getInstance().a(paramXDevice, parama);
  }
  
  public int a(XDevice paramXDevice, String paramString, b paramb, int paramInt) {
    return XlinkAgent.getInstance().a(paramXDevice, paramString, paramb, paramInt);
  }
  
  public XDevice a(int paramInt, XDevice paramXDevice) {
    paramXDevice.a(paramInt);
    return paramXDevice;
  }
  
  public XDevice a(XDevice paramXDevice, int paramInt) {
    paramXDevice.c(paramInt);
    return paramXDevice;
  }
  
  public XDevice a(XDevice paramXDevice, InetAddress paramInetAddress, byte[] paramArrayOfbyte, String paramString, int paramInt) {
    if (paramInetAddress != null)
      paramXDevice.a(paramInetAddress); 
    if (paramArrayOfbyte != null)
      paramXDevice.a(paramArrayOfbyte); 
    if (paramString != null)
      paramXDevice.a(paramString); 
    if (paramInt > 0)
      paramXDevice.e(paramInt); 
    return paramXDevice;
  }
  
  public XDevice a(String paramString) {
    return new XDevice(paramString);
  }
  
  public XDevice a(JSONObject paramJSONObject) {
    XDevice xDevice;
    try {
      XDevice xDevice1 = new XDevice();
      this(paramJSONObject.getString("mac"));
      xDevice1.a(paramJSONObject.getString("pid"));
      if (!paramJSONObject.isNull("dname")) {
        xDevice1.setDeviceName(paramJSONObject.getString("dname"));
      } else {
        xDevice1.setDeviceName("");
      } 
      if (paramJSONObject.isNull("ip")) {
        xDevice1.a(b.b());
      } else {
        xDevice1.a(b.e(paramJSONObject.getString("ip")));
      } 
      xDevice1.f(paramJSONObject.getInt("did"));
      if (paramJSONObject.isNull("port")) {
        xDevice1.e(a.f);
      } else {
        xDevice1.e(paramJSONObject.getInt("port"));
      } 
      xDevice1.setVersion((byte)paramJSONObject.getInt("version"));
      if (paramJSONObject.isNull("init")) {
        xDevice1.a(true);
      } else {
        xDevice1.a(paramJSONObject.getBoolean("init"));
      } 
      if (paramJSONObject.isNull("accesskey")) {
        xDevice1.b(-1);
      } else {
        xDevice1.b(paramJSONObject.getInt("accesskey"));
      } 
      xDevice1.setMcuHardVersion((byte)paramJSONObject.getInt("mhv"));
      xDevice1.setMcuSoftVersion((byte)paramJSONObject.getInt("msv"));
      xDevice = xDevice1;
    } catch (JSONException jSONException) {}
    return xDevice;
  }
  
  public XDevice a(boolean paramBoolean, XDevice paramXDevice) {
    paramXDevice.a(paramBoolean);
    return paramXDevice;
  }
  
  public void a(XDevice paramXDevice) {
    MyLog.e("DeviceAgent", "reconnectDevice xDevice:" + paramXDevice);
    c.a(-1, paramXDevice);
    XlinkAgent.getInstance().connectDevice(paramXDevice, paramXDevice.getAuthkey(), new ConnectDeviceListener(this) {
          public void onConnectDevice(XDevice param1XDevice, int param1Int) {
            MyLog.e("Reconnect", "auth reconnectDevice:" + param1XDevice + " code:" + param1Int);
            if (param1Int == 0 || param1Int == 1) {
              c.a(-3, param1XDevice);
              return;
            } 
            param1XDevice.c(0);
            c.a(-2, param1XDevice);
          }
        });
  }
  
  public void a(XDevice paramXDevice, String paramString) {
    paramXDevice.b(paramString);
  }
  
  public XDevice b(XDevice paramXDevice, int paramInt) {
    paramXDevice.f(paramInt);
    return paramXDevice;
  }
  
  public void b(XDevice paramXDevice) {
    paramXDevice.b();
  }
  
  public XDevice c(XDevice paramXDevice, int paramInt) {
    paramXDevice.b(paramInt);
    return paramXDevice;
  }
  
  public JSONObject c(XDevice paramXDevice) throws JSONException {
    JSONObject jSONObject1 = new JSONObject();
    jSONObject1.put("protocol", 1);
    JSONObject jSONObject2 = new JSONObject();
    jSONObject2.put("deviceName", paramXDevice.getDeviceName() + "");
    jSONObject2.put("macAddress", paramXDevice.getMacAddress());
    jSONObject2.put("deviceIP", paramXDevice.getAddress().getHostAddress());
    jSONObject2.put("deviceID", paramXDevice.getDeviceId());
    jSONObject2.put("productID", paramXDevice.getProductId());
    jSONObject2.put("devicePort", paramXDevice.getPort());
    jSONObject2.put("version", paramXDevice.getVersion());
    jSONObject2.put("mcuHardVersion", paramXDevice.getMcuHardVersion());
    jSONObject2.put("mcuSoftVersion", paramXDevice.getMcuSoftVersion());
    jSONObject2.put("deviceInit", paramXDevice.isInit());
    jSONObject2.put("accesskey", paramXDevice.getAccessKey());
    jSONObject1.put("device", jSONObject2);
    return jSONObject1;
  }
  
  public XDevice d(XDevice paramXDevice, int paramInt) {
    paramXDevice.d(paramInt);
    return paramXDevice;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/a.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */