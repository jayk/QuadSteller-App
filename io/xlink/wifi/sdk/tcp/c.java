package io.xlink.wifi.sdk.tcp;

import io.xlink.wifi.sdk.DataPoint;
import io.xlink.wifi.sdk.XDevice;
import io.xlink.wifi.sdk.XlinkTcpService;
import io.xlink.wifi.sdk.encoder.d;
import io.xlink.wifi.sdk.encoder.f;
import io.xlink.wifi.sdk.encoder.g;
import io.xlink.wifi.sdk.event.a;
import io.xlink.wifi.sdk.listener.SendPipeListener;
import io.xlink.wifi.sdk.listener.SetDataPointListener;
import io.xlink.wifi.sdk.listener.SetDeviceAccessKeyListener;
import io.xlink.wifi.sdk.listener.SubscribeDeviceListener;
import io.xlink.wifi.sdk.util.b;

public class c {
  private static c a;
  
  public static c a() {
    if (a == null)
      a = new c(); 
    return a;
  }
  
  public int a(int paramInt1, byte paramByte, byte[] paramArrayOfbyte, SendPipeListener paramSendPipeListener, int paramInt2) {
    d d = f.a().a(paramInt1, paramByte, paramArrayOfbyte, (a)paramSendPipeListener, paramInt2);
    g g = new g(d, (a)paramSendPipeListener);
    XlinkTcpService.a().a(g);
    return d.g();
  }
  
  public int a(XDevice paramXDevice, byte paramByte, byte[] paramArrayOfbyte, SendPipeListener paramSendPipeListener, int paramInt) {
    d d = f.a().a(paramXDevice, paramByte, paramArrayOfbyte, (a)paramSendPipeListener, paramInt);
    g g = new g(d, (a)paramSendPipeListener);
    XlinkTcpService.a().a(g);
    return d.g();
  }
  
  public int a(XDevice paramXDevice, DataPoint paramDataPoint, SetDataPointListener paramSetDataPointListener) {
    d d = f.a().b(paramXDevice, paramDataPoint);
    g g = new g(d, (a)paramSetDataPointListener);
    XlinkTcpService.a().a(g);
    return d.g();
  }
  
  public int a(XDevice paramXDevice, String paramString1, String paramString2, SetDeviceAccessKeyListener paramSetDeviceAccessKeyListener, int paramInt) {
    d d = f.a().b(paramXDevice, paramString1, paramString2);
    g g = new g(d, (a)paramSetDeviceAccessKeyListener, paramInt);
    XlinkTcpService.a().a(g);
    return d.g();
  }
  
  public void a(int paramInt) {
    b.b(new Runnable(this, new g(f.a().a((byte)paramInt))) {
          public void run() {
            a.a().c(this.a);
          }
        });
  }
  
  public void a(int paramInt1, String paramString, a parama, int paramInt2) {
    d d = f.a().b(paramInt1, paramString);
    d.a("1");
    g g = new g(d, parama, paramInt2);
    a.a().a(g);
  }
  
  public void a(XDevice paramXDevice, int paramInt1, a parama, int paramInt2) {
    g g = new g(f.a().c(paramXDevice, paramInt1), parama, paramInt2);
    XlinkTcpService.a().a(g);
  }
  
  public void a(XDevice paramXDevice, int paramInt, SubscribeDeviceListener paramSubscribeDeviceListener) {
    g g = new g(f.a().a(paramXDevice, paramInt, true, (a)paramSubscribeDeviceListener), (a)paramSubscribeDeviceListener);
    XlinkTcpService.a().a(g);
  }
  
  public void a(XDevice paramXDevice, String paramString, SubscribeDeviceListener paramSubscribeDeviceListener) {
    g g = new g(f.a().a(paramXDevice, paramString, true, (a)paramSubscribeDeviceListener), (a)paramSubscribeDeviceListener);
    XlinkTcpService.a().a(g);
  }
  
  public g b() {
    return new g(f.a().b());
  }
  
  public void b(XDevice paramXDevice, int paramInt, SubscribeDeviceListener paramSubscribeDeviceListener) {
    g g = new g(f.a().a(paramXDevice, paramInt, false, (a)paramSubscribeDeviceListener), (a)paramSubscribeDeviceListener);
    XlinkTcpService.a().a(g);
  }
  
  public void b(XDevice paramXDevice, String paramString, SubscribeDeviceListener paramSubscribeDeviceListener) {
    g g = new g(f.a().a(paramXDevice, paramString, false, (a)paramSubscribeDeviceListener), (a)paramSubscribeDeviceListener);
    XlinkTcpService.a().a(g);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/tcp/c.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */