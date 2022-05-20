package io.xlink.wifi.sdk.udp;

import io.xlink.wifi.sdk.DataPoint;
import io.xlink.wifi.sdk.XDevice;
import io.xlink.wifi.sdk.XlinkUdpService;
import io.xlink.wifi.sdk.encoder.d;
import io.xlink.wifi.sdk.encoder.f;
import io.xlink.wifi.sdk.encoder.g;
import io.xlink.wifi.sdk.event.a;
import io.xlink.wifi.sdk.listener.SendPipeListener;
import io.xlink.wifi.sdk.listener.SetDataPointListener;
import io.xlink.wifi.sdk.listener.SetDeviceAccessKeyListener;

public class b {
  private static b a;
  
  public static b a() {
    if (a == null)
      a = new b(); 
    return a;
  }
  
  public int a(XDevice paramXDevice, byte paramByte, byte[] paramArrayOfbyte, SendPipeListener paramSendPipeListener) {
    d d = f.a().a(paramXDevice, paramByte, paramArrayOfbyte);
    g g = new g(d, (a)paramSendPipeListener);
    XlinkUdpService.b().a(g);
    return d.g();
  }
  
  public int a(XDevice paramXDevice, int paramInt, SetDeviceAccessKeyListener paramSetDeviceAccessKeyListener) {
    d d = f.a().b(paramXDevice, paramInt);
    g g = new g(d, (a)paramSetDeviceAccessKeyListener);
    XlinkUdpService.b().a(g);
    return d.g();
  }
  
  public int a(XDevice paramXDevice, DataPoint paramDataPoint, SetDataPointListener paramSetDataPointListener) {
    d d = f.a().a(paramXDevice, paramDataPoint);
    g g = new g(d, (a)paramSetDataPointListener);
    XlinkUdpService.b().a(g);
    return d.g();
  }
  
  public int a(XDevice paramXDevice, String paramString1, String paramString2, SetDeviceAccessKeyListener paramSetDeviceAccessKeyListener) {
    d d = f.a().a(paramXDevice, paramString1, paramString2);
    g g = new g(d, (a)paramSetDeviceAccessKeyListener);
    XlinkUdpService.b().a(g);
    return d.g();
  }
  
  public void a(XDevice paramXDevice) {
    g g = new g(f.a().b(paramXDevice));
    XlinkUdpService.b().a(g);
  }
  
  public void a(XDevice paramXDevice, int paramInt1, io.xlink.wifi.sdk.event.b paramb, int paramInt2) {
    g g = new g(f.a().a(paramXDevice, paramInt1), (a)paramb, paramInt2);
    XlinkUdpService.b().a(g);
  }
  
  public void a(XDevice paramXDevice, a parama) {
    d d = f.a().a(1, paramXDevice.getMacAddress());
    g g = new g(d, parama, 2);
    d.a(paramXDevice);
    g.a(paramXDevice.getMacAddress() + '\001');
    XlinkUdpService.b().a(g);
  }
  
  public void a(XDevice paramXDevice, String paramString, io.xlink.wifi.sdk.event.b paramb, int paramInt) {
    g g = new g(f.a().a(paramXDevice, paramString), (a)paramb, paramInt);
    XlinkUdpService.b().a(g);
  }
  
  public void a(String paramString) {
    d d = f.a().a(2, paramString);
    d.a = true;
    g g = new g(d);
    XlinkUdpService.b().a(g);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/udp/b.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */