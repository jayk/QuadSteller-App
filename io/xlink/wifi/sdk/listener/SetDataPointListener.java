package io.xlink.wifi.sdk.listener;

import io.xlink.wifi.sdk.XDevice;
import io.xlink.wifi.sdk.encoder.e;
import io.xlink.wifi.sdk.event.a;
import io.xlink.wifi.sdk.util.b;

public abstract class SetDataPointListener extends a {
  public final void onResponse(e parame) {
    b.a(new Runnable(this, parame) {
          public void run() {
            this.b.onSetDataPoint(this.a.a, this.a.b, this.a.c);
          }
        });
  }
  
  public abstract void onSetDataPoint(XDevice paramXDevice, int paramInt1, int paramInt2);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/listener/SetDataPointListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */