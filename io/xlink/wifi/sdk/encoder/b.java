package io.xlink.wifi.sdk.encoder;

import android.text.TextUtils;
import io.xlink.wifi.sdk.XDevice;
import io.xlink.wifi.sdk.XlinkAgent;
import io.xlink.wifi.sdk.XlinkTcpService;
import io.xlink.wifi.sdk.XlinkUdpService;
import io.xlink.wifi.sdk.a;
import io.xlink.wifi.sdk.event.a;
import io.xlink.wifi.sdk.listener.ConnectDeviceListener;
import io.xlink.wifi.sdk.listener.SubscribeDeviceListener;
import io.xlink.wifi.sdk.manage.a;
import io.xlink.wifi.sdk.manage.c;
import java.util.HashMap;

public class b extends a {
  private static final HashMap<String, b> b = new HashMap<String, b>();
  
  private long c;
  
  private XDevice d;
  
  private String e;
  
  private ConnectDeviceListener f;
  
  private io.xlink.wifi.sdk.event.b g = new io.xlink.wifi.sdk.event.b(this) {
      public void a(XDevice param1XDevice, int param1Int) {
        e e = new e(0);
        switch (param1Int) {
          default:
            e.b = param1Int;
            e.a = param1XDevice;
            this.a.a(e);
            this.a.a("handshake error code:" + param1Int);
            return;
          case 0:
            e.a = param1XDevice;
            e.a = a.a().a(0, e.a);
            c.a().c(e.a);
            this.a.a(e);
            return;
          case 2:
            e.b = 102;
            e.a = param1XDevice;
            this.a.a(e);
            return;
          case -100:
            break;
        } 
        if (!XlinkTcpService.c()) {
          e.b = 200;
          e.a = param1XDevice;
          this.a.a(e);
          return;
        } 
        if (param1XDevice.isValidId()) {
          param1Int = a.a().a(param1XDevice, b.a(this.a));
          if (param1Int < 0) {
            this.a.a("connectdevice subscribeDevice fail ret==" + param1Int);
            e.a = param1XDevice;
            e.b = 200;
            this.a.a(e);
          } 
          return;
        } 
        e.a = param1XDevice;
        e.b = 200;
        this.a.a(e);
      }
    };
  
  private SubscribeDeviceListener h = new SubscribeDeviceListener(this) {
      public void onSubscribeDevice(XDevice param1XDevice, int param1Int) {
        e e = new e(param1Int);
        e.a = param1XDevice;
        switch (param1Int) {
          default:
            this.a.a("subscribe fail code:" + param1Int);
            e.b = 110;
            e.a = param1XDevice;
            this.a.a(e);
            return;
          case 0:
            if (param1XDevice.getDeviceId() == 0) {
              e.b = 104;
            } else {
              e.b = 1;
              e.a = a.a().a(1, e.a);
              c.a().c(e.a);
              XlinkAgent.getInstance().sendProbe(e.a);
            } 
            this.a.a(e);
            return;
          case 2:
            break;
        } 
        e.b = 102;
        e.a = param1XDevice;
        this.a.a(e);
      }
    };
  
  private a i = new a(this) {
      public void onResponse(e param1e) {
        switch (param1e.b) {
          default:
            this.a.a("cloud probe error code:" + param1e.b);
            param1e.b = 110;
            this.a.a(param1e);
            return;
          case 0:
            this.a.a("cloud probe succeed  device state online ");
            param1e.b = 1;
            param1e.a = a.a().a(1, param1e.a);
            c.a().c(param1e.a);
            this.a.a(param1e);
            return;
          case 5:
            this.a.a("cloud probe device not subscribe " + param1e.a.getDeviceId());
            if (XlinkAgent.getInstance().subscribeDevice(param1e.a, b.b(this.a), b.c(this.a)) < 0) {
              param1e.b = 200;
              this.a.a(param1e);
            } 
            return;
          case -100:
            this.a.a("cloud probe packet timeout");
            param1e.b = 200;
            this.a.a(param1e);
            return;
          case 2:
            param1e.b = 102;
            this.a.a(param1e);
            return;
          case 3:
            break;
        } 
        param1e.b = 109;
        this.a.a(param1e);
      }
    };
  
  private a j = new a(this) {
      public void onResponse(e param1e) {
        int i = -1;
        switch (param1e.b) {
          default:
            if (i < 0) {
              this.a.a("call handshakeWithDevice fail code:" + i);
              param1e.a = b.e(this.a);
              param1e.b = 200;
              this.a.a(param1e);
            } 
            return;
          case 0:
            this.a.a("scan by mac succeed :" + param1e.a.getAddress());
            i = a.a().a(param1e.a, b.b(this.a), b.d(this.a), 2);
          case -100:
            break;
        } 
        i = a.a().a(b.e(this.a), b.b(this.a), b.d(this.a), 2);
      }
    };
  
  public b(XDevice paramXDevice, String paramString, ConnectDeviceListener paramConnectDeviceListener) {
    this.e = paramString;
    this.f = paramConnectDeviceListener;
    this.d = paramXDevice;
    this.c = System.currentTimeMillis();
    this.a = b();
  }
  
  private void c() {
    if (this.a == 0)
      b.put(this.d.getMacAddress(), this); 
  }
  
  public int a() {
    if (this.a == 0) {
      if (XlinkUdpService.a() && io.xlink.wifi.sdk.util.b.e()) {
        this.a = XlinkAgent.scanByMac(this.d, this.j);
        c();
        return this.a;
      } 
      if (XlinkTcpService.c()) {
        if (!this.d.isValidId()) {
          this.a = XlinkAgent.getInstance().subscribeDevice(this.d, this.e, this.h);
          c();
          return this.a;
        } 
        this.a = a.a().a(this.d, this.i);
        c();
        return this.a;
      } 
      this.a = -4;
    } 
    return this.a;
  }
  
  public void a(e parame) {
    b.remove(this.d.getMacAddress());
    this.f.onResponse(parame);
  }
  
  public int b() {
    if (TextUtils.isEmpty(this.e) || this.f == null) {
      byte b1 = -8;
      this.a = -8;
      return b1;
    } 
    if (!a.a().d()) {
      byte b1 = -10;
      this.a = -10;
      return b1;
    } 
    if (c.a().a(this.d)) {
      byte b1 = -3;
      this.a = -3;
      return b1;
    } 
    XDevice xDevice = c.a().b(this.d.getMacAddress());
    if (xDevice == null) {
      byte b1 = -6;
      this.a = -6;
      return b1;
    } 
    this.d = xDevice;
    this.d.setAuthkey(this.e);
    return 0;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/encoder/b.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */