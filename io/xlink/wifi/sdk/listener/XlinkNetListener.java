package io.xlink.wifi.sdk.listener;

import io.xlink.wifi.sdk.XDevice;
import io.xlink.wifi.sdk.bean.EventNotify;

public interface XlinkNetListener {
  void onDataPointUpdate(XDevice paramXDevice, int paramInt1, Object paramObject, int paramInt2, int paramInt3);
  
  void onDeviceStateChanged(XDevice paramXDevice, int paramInt);
  
  void onDisconnect(int paramInt);
  
  void onEventNotify(EventNotify paramEventNotify);
  
  void onLocalDisconnect(int paramInt);
  
  void onLogin(int paramInt);
  
  void onRecvPipeData(XDevice paramXDevice, byte paramByte, byte[] paramArrayOfbyte);
  
  void onRecvPipeSyncData(XDevice paramXDevice, byte paramByte, byte[] paramArrayOfbyte);
  
  void onStart(int paramInt);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/listener/XlinkNetListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */