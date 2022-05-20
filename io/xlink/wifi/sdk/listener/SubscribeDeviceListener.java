package io.xlink.wifi.sdk.listener;

import io.xlink.wifi.sdk.XDevice;
import io.xlink.wifi.sdk.encoder.e;
import io.xlink.wifi.sdk.event.a;
import io.xlink.wifi.sdk.manage.c;
import io.xlink.wifi.sdk.util.b;

public abstract class SubscribeDeviceListener extends a {
  public final void onResponse(e parame) {
    c.a().c(parame.a);
    b.a(new Runnable(this, parame) {
          public void run() {
            this.b.onSubscribeDevice(this.a.a, this.a.b);
          }
        });
  }
  
  public abstract void onSubscribeDevice(XDevice paramXDevice, int paramInt);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/listener/SubscribeDeviceListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */