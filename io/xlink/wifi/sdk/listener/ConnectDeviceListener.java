package io.xlink.wifi.sdk.listener;

import io.xlink.wifi.sdk.XDevice;
import io.xlink.wifi.sdk.encoder.e;
import io.xlink.wifi.sdk.event.a;
import io.xlink.wifi.sdk.util.b;

public abstract class ConnectDeviceListener extends a {
  public abstract void onConnectDevice(XDevice paramXDevice, int paramInt);
  
  public final void onResponse(e parame) {
    b.a(new Runnable(this, parame) {
          public void run() {
            this.b.onConnectDevice(this.a.a, this.a.b);
          }
        });
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/listener/ConnectDeviceListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */