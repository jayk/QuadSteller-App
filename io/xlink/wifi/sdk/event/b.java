package io.xlink.wifi.sdk.event;

import io.xlink.wifi.sdk.XDevice;
import io.xlink.wifi.sdk.encoder.e;

public abstract class b extends a {
  public abstract void a(XDevice paramXDevice, int paramInt);
  
  public final void onResponse(e parame) {
    a(parame.a, parame.b);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/event/b.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */