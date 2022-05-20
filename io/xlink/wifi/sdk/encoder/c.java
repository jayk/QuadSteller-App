package io.xlink.wifi.sdk.encoder;

import io.xlink.wifi.sdk.XDevice;
import io.xlink.wifi.sdk.XlinkAgent;
import io.xlink.wifi.sdk.XlinkTcpService;
import io.xlink.wifi.sdk.XlinkUdpService;
import io.xlink.wifi.sdk.a;
import io.xlink.wifi.sdk.event.a;
import io.xlink.wifi.sdk.event.b;
import io.xlink.wifi.sdk.listener.ConnectDeviceListener;
import io.xlink.wifi.sdk.listener.SubscribeDeviceListener;
import io.xlink.wifi.sdk.manage.a;
import io.xlink.wifi.sdk.util.b;
import java.util.HashMap;

public class c extends a {
  private static final HashMap<String, c> b = new HashMap<String, c>();
  
  private long c;
  
  private XDevice d;
  
  private int e;
  
  private ConnectDeviceListener f;
  
  private b g = new b(this) {
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
            io.xlink.wifi.sdk.manage.c.a().c(e.a);
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
          param1Int = a.a().a(param1XDevice, c.a(this.a));
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
              io.xlink.wifi.sdk.manage.c.a().c(e.a);
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
            io.xlink.wifi.sdk.manage.c.a().c(param1e.a);
            this.a.a(param1e);
            return;
          case 5:
            this.a.a("cloud probe device not subscribe " + param1e.a.getDeviceId());
            if (XlinkAgent.getInstance().subscribeDevice(param1e.a, c.b(this.a), c.c(this.a)) < 0) {
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
              param1e.a = c.e(this.a);
              param1e.b = 200;
              this.a.a(param1e);
            } 
            return;
          case 0:
            this.a.a("scan by mac succeed :" + param1e.a.getAddress());
            i = a.a().a(param1e.a, c.b(this.a), c.d(this.a), 2);
          case -100:
            break;
        } 
        i = a.a().a(c.e(this.a), c.b(this.a), c.d(this.a), 2);
      }
    };
  
  public c(XDevice paramXDevice, int paramInt, ConnectDeviceListener paramConnectDeviceListener) {
    this.e = paramInt;
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
      if (XlinkUdpService.a() && b.e()) {
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
    null = -8;
    if (this.e < 0 || this.e > 999999999) {
      this.a = -8;
      return null;
    } 
    if (this.f == null) {
      this.a = -8;
      return null;
    } 
    if (!a.a().d()) {
      null = -10;
      this.a = -10;
      return null;
    } 
    if (io.xlink.wifi.sdk.manage.c.a().a(this.d)) {
      null = -3;
      this.a = -3;
      return null;
    } 
    XDevice xDevice = io.xlink.wifi.sdk.manage.c.a().b(this.d.getMacAddress());
    if (xDevice == null) {
      null = -6;
      this.a = -6;
      return null;
    } 
    this.d = xDevice;
    this.d = a.a().c(this.d, this.e);
    return 0;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/encoder/c.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */