package app.gamer.quadstellar.net;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import app.gamer.quadstellar.App;
import app.gamer.quadstellar.Config;
import app.gamer.quadstellar.callback.IDeviceConnectCallback;
import app.gamer.quadstellar.callback.IDeviceLinkageConnectCallback;
import app.gamer.quadstellar.mode.ControlDevice;
import app.gamer.quadstellar.mode.LinkageControl;
import app.gamer.quadstellar.utils.LogUtil;
import app.gamer.quadstellar.utils.PreferenceHelper;
import app.gamer.quadstellar.utils.XTGlobals;
import app.gamer.quadstellar.utils.XlinkUtils;
import io.xlink.wifi.sdk.XDevice;
import io.xlink.wifi.sdk.XlinkAgent;
import io.xlink.wifi.sdk.listener.ConnectDeviceListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class DeviceManager {
  private static String PASSWORD;
  
  private static final String TAG = "DeviceManage";
  
  public static ConcurrentHashMap<String, ControlDevice> deviceMap = new ConcurrentHashMap<String, ControlDevice>();
  
  private static DeviceManager instance;
  
  public static final ArrayList<ControlDevice> listDev;
  
  private Activity lastActivity;
  
  private ControlDevice mDevice;
  
  private ProgressDialog mProgressDialog;
  
  static {
    for (Map.Entry entry : XTGlobals.getAllProperty().entrySet()) {
      try {
        JSONObject jSONObject = new JSONObject();
        this((String)entry.getValue());
        XDevice xDevice = XlinkAgent.JsonToDevice(jSONObject);
        if (xDevice != null) {
          ControlDevice controlDevice = new ControlDevice();
          this(xDevice);
          if (!jSONObject.isNull("password"))
            controlDevice.setPassword(jSONObject.getString("password")); 
          controlDevice.setType(PreferenceHelper.readInt(controlDevice.getMacAddress(), -1));
          deviceMap.put(xDevice.getMacAddress(), controlDevice);
        } 
      } catch (JSONException jSONException) {
        jSONException.printStackTrace();
      } 
    } 
    PASSWORD = "password";
    listDev = new ArrayList<ControlDevice>();
  }
  
  public static boolean checkDevice(ControlDevice paramControlDevice) {
    boolean bool = true;
    if (paramControlDevice == null) {
      XlinkUtils.shortTips(App.getInstance().getString(2131296975));
      return false;
    } 
    if (paramControlDevice.getType() != 0) {
      XlinkUtils.shortTips(App.getInstance().getString(2131297595));
      bool = false;
    } 
    return bool;
  }
  
  public static boolean checkTeleDevice(ControlDevice paramControlDevice) {
    boolean bool = true;
    if (paramControlDevice == null) {
      XlinkUtils.shortTips(App.getInstance().getString(2131296975));
      return false;
    } 
    if (paramControlDevice.getType() != 3) {
      XlinkUtils.shortTips(App.getInstance().getString(2131297482));
      bool = false;
    } 
    return bool;
  }
  
  public static DeviceManager getInstance() {
    if (instance == null)
      instance = new DeviceManager(); 
    return instance;
  }
  
  public static String getMac(ControlDevice paramControlDevice) {
    return (paramControlDevice == null) ? "" : paramControlDevice.getMacAddress();
  }
  
  public static void setDeviceStatus(boolean paramBoolean) {
    (App.getInstance()).isOnline = paramBoolean;
  }
  
  private void updateRecentDevice(ControlDevice paramControlDevice) {
    int i = paramControlDevice.getType();
    String str = paramControlDevice.getMacAddress();
    PreferenceHelper.write("device-mac", str);
    if (i == 0) {
      PreferenceHelper.write("recent_normal_device", str);
      return;
    } 
    if (i == 3) {
      PreferenceHelper.write("recent_tele_device", str);
      return;
    } 
    if (i == 6) {
      PreferenceHelper.write("recent_normal_device", str);
      return;
    } 
    if (i == 4) {
      PreferenceHelper.write("recent_normal_device", str);
      PreferenceHelper.write("recent_tele_device", str);
    } 
  }
  
  public static byte weekToByte(ArrayList<Integer> paramArrayList) {
    byte b = 0;
    if (paramArrayList == null || paramArrayList.size() == 0) {
      b = 0;
      return b;
    } 
    Iterator<Integer> iterator = paramArrayList.iterator();
    null = b;
    while (iterator.hasNext()) {
      Integer integer = iterator.next();
      if (integer.intValue() <= 6) {
        b = XlinkUtils.setByteBit(integer.intValue(), null);
        null = b;
      } 
    } 
    if (null == 0) {
      b = null;
      return b;
    } 
    b = XlinkUtils.setByteBit(7, null);
    return b;
  }
  
  public void addDevice(ControlDevice paramControlDevice) {
    deviceMap.put(paramControlDevice.getMacAddress(), paramControlDevice);
    saveDevice(paramControlDevice);
  }
  
  public void addDevice(XDevice paramXDevice) {
    ControlDevice controlDevice = deviceMap.get(paramXDevice.getMacAddress());
    if (controlDevice != null) {
      controlDevice.setxDevice(paramXDevice);
      deviceMap.put(paramXDevice.getMacAddress(), controlDevice);
      saveDevice(controlDevice);
      return;
    } 
    controlDevice = new ControlDevice(paramXDevice);
    deviceMap.put(paramXDevice.getMacAddress(), controlDevice);
    saveDevice(controlDevice);
  }
  
  public void addDevice(XDevice paramXDevice, int paramInt) {
    ControlDevice controlDevice = deviceMap.get(paramXDevice.getMacAddress());
    PreferenceHelper.write(paramXDevice.getMacAddress(), paramInt);
    if (controlDevice != null) {
      controlDevice.setxDevice(paramXDevice);
      controlDevice.setType(paramInt);
      deviceMap.put(paramXDevice.getMacAddress(), controlDevice);
      saveDevice(controlDevice);
      return;
    } 
    controlDevice = new ControlDevice(paramXDevice);
    controlDevice.setType(paramInt);
    deviceMap.put(paramXDevice.getMacAddress(), controlDevice);
    saveDevice(controlDevice);
  }
  
  public void clearAllDevice() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual getDevices : ()Ljava/util/ArrayList;
    //   6: invokevirtual iterator : ()Ljava/util/Iterator;
    //   9: astore_1
    //   10: aload_1
    //   11: invokeinterface hasNext : ()Z
    //   16: ifeq -> 42
    //   19: aload_1
    //   20: invokeinterface next : ()Ljava/lang/Object;
    //   25: checkcast app/gamer/quadstellar/mode/ControlDevice
    //   28: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   31: invokestatic deleteProperty : (Ljava/lang/String;)V
    //   34: goto -> 10
    //   37: astore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_1
    //   41: athrow
    //   42: getstatic app/gamer/quadstellar/net/DeviceManager.deviceMap : Ljava/util/concurrent/ConcurrentHashMap;
    //   45: invokevirtual clear : ()V
    //   48: aload_0
    //   49: monitorexit
    //   50: return
    // Exception table:
    //   from	to	target	type
    //   2	10	37	finally
    //   10	34	37	finally
    //   42	48	37	finally
  }
  
  public void connectDevice(Activity paramActivity, ControlDevice paramControlDevice, IDeviceConnectCallback paramIDeviceConnectCallback) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_2
    //   4: putfield mDevice : Lapp/gamer/quadstellar/mode/ControlDevice;
    //   7: aload_0
    //   8: getfield mDevice : Lapp/gamer/quadstellar/mode/ControlDevice;
    //   11: ifnonnull -> 26
    //   14: aload_1
    //   15: ldc 2131296610
    //   17: invokevirtual getString : (I)Ljava/lang/String;
    //   20: invokestatic shortTips : (Ljava/lang/String;)V
    //   23: aload_0
    //   24: monitorexit
    //   25: return
    //   26: invokestatic getInstance : ()Lio/xlink/wifi/sdk/XlinkAgent;
    //   29: astore_2
    //   30: aload_0
    //   31: getfield mDevice : Lapp/gamer/quadstellar/mode/ControlDevice;
    //   34: invokevirtual getXDevice : ()Lio/xlink/wifi/sdk/XDevice;
    //   37: astore #4
    //   39: ldc '8888'
    //   41: invokestatic parseInt : (Ljava/lang/String;)I
    //   44: istore #5
    //   46: new app/gamer/quadstellar/net/DeviceManager$1
    //   49: astore_1
    //   50: aload_1
    //   51: aload_0
    //   52: aload_3
    //   53: invokespecial <init> : (Lapp/gamer/quadstellar/net/DeviceManager;Lapp/gamer/quadstellar/callback/IDeviceConnectCallback;)V
    //   56: aload_2
    //   57: aload #4
    //   59: iload #5
    //   61: aload_1
    //   62: invokevirtual connectDevice : (Lio/xlink/wifi/sdk/XDevice;ILio/xlink/wifi/sdk/listener/ConnectDeviceListener;)I
    //   65: ifge -> 23
    //   68: aload_0
    //   69: invokevirtual dismissConnectDialog : ()V
    //   72: iconst_0
    //   73: invokestatic setDeviceStatus : (Z)V
    //   76: aload_3
    //   77: ifnull -> 23
    //   80: aload_3
    //   81: iconst_0
    //   82: aload_0
    //   83: getfield mDevice : Lapp/gamer/quadstellar/mode/ControlDevice;
    //   86: invokeinterface connectCallback : (ZLapp/gamer/quadstellar/mode/ControlDevice;)V
    //   91: goto -> 23
    //   94: astore_1
    //   95: aload_0
    //   96: monitorexit
    //   97: aload_1
    //   98: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	94	finally
    //   26	76	94	finally
    //   80	91	94	finally
  }
  
  public void connectDeviceLinkage(Activity paramActivity, ControlDevice paramControlDevice, IDeviceConnectCallback paramIDeviceConnectCallback) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: invokestatic getInstance : ()Lio/xlink/wifi/sdk/XlinkAgent;
    //   5: astore #4
    //   7: aload_2
    //   8: invokevirtual getXDevice : ()Lio/xlink/wifi/sdk/XDevice;
    //   11: astore #5
    //   13: ldc '8888'
    //   15: invokestatic parseInt : (Ljava/lang/String;)I
    //   18: istore #6
    //   20: new app/gamer/quadstellar/net/DeviceManager$2
    //   23: astore_1
    //   24: aload_1
    //   25: aload_0
    //   26: aload_2
    //   27: aload_3
    //   28: invokespecial <init> : (Lapp/gamer/quadstellar/net/DeviceManager;Lapp/gamer/quadstellar/mode/ControlDevice;Lapp/gamer/quadstellar/callback/IDeviceConnectCallback;)V
    //   31: aload #4
    //   33: aload #5
    //   35: iload #6
    //   37: aload_1
    //   38: invokevirtual connectDevice : (Lio/xlink/wifi/sdk/XDevice;ILio/xlink/wifi/sdk/listener/ConnectDeviceListener;)I
    //   41: ifge -> 60
    //   44: iconst_0
    //   45: invokestatic setDeviceStatus : (Z)V
    //   48: aload_3
    //   49: ifnull -> 60
    //   52: aload_3
    //   53: iconst_0
    //   54: aload_2
    //   55: invokeinterface connectCallback : (ZLapp/gamer/quadstellar/mode/ControlDevice;)V
    //   60: aload_0
    //   61: monitorexit
    //   62: return
    //   63: astore_1
    //   64: aload_0
    //   65: monitorexit
    //   66: aload_1
    //   67: athrow
    // Exception table:
    //   from	to	target	type
    //   2	48	63	finally
    //   52	60	63	finally
  }
  
  public void connectLinkageDevice(Activity paramActivity, LinkageControl paramLinkageControl, IDeviceLinkageConnectCallback paramIDeviceLinkageConnectCallback) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_2
    //   4: invokevirtual getControlDevice : ()Lapp/gamer/quadstellar/mode/ControlDevice;
    //   7: putfield mDevice : Lapp/gamer/quadstellar/mode/ControlDevice;
    //   10: aload_0
    //   11: getfield mDevice : Lapp/gamer/quadstellar/mode/ControlDevice;
    //   14: ifnonnull -> 29
    //   17: aload_1
    //   18: ldc 2131296610
    //   20: invokevirtual getString : (I)Ljava/lang/String;
    //   23: invokestatic shortTips : (Ljava/lang/String;)V
    //   26: aload_0
    //   27: monitorexit
    //   28: return
    //   29: invokestatic getInstance : ()Lio/xlink/wifi/sdk/XlinkAgent;
    //   32: astore #4
    //   34: aload_0
    //   35: getfield mDevice : Lapp/gamer/quadstellar/mode/ControlDevice;
    //   38: invokevirtual getXDevice : ()Lio/xlink/wifi/sdk/XDevice;
    //   41: astore #5
    //   43: ldc '8888'
    //   45: invokestatic parseInt : (Ljava/lang/String;)I
    //   48: istore #6
    //   50: new app/gamer/quadstellar/net/DeviceManager$3
    //   53: astore_1
    //   54: aload_1
    //   55: aload_0
    //   56: aload_3
    //   57: aload_2
    //   58: invokespecial <init> : (Lapp/gamer/quadstellar/net/DeviceManager;Lapp/gamer/quadstellar/callback/IDeviceLinkageConnectCallback;Lapp/gamer/quadstellar/mode/LinkageControl;)V
    //   61: aload #4
    //   63: aload #5
    //   65: iload #6
    //   67: aload_1
    //   68: invokevirtual connectDevice : (Lio/xlink/wifi/sdk/XDevice;ILio/xlink/wifi/sdk/listener/ConnectDeviceListener;)I
    //   71: ifge -> 26
    //   74: aload_0
    //   75: invokevirtual dismissConnectDialog : ()V
    //   78: iconst_0
    //   79: invokestatic setDeviceStatus : (Z)V
    //   82: aload_3
    //   83: ifnull -> 26
    //   86: aload_3
    //   87: iconst_0
    //   88: aload_2
    //   89: invokeinterface connectCallback : (ZLapp/gamer/quadstellar/mode/LinkageControl;)V
    //   94: goto -> 26
    //   97: astore_1
    //   98: aload_0
    //   99: monitorexit
    //   100: aload_1
    //   101: athrow
    // Exception table:
    //   from	to	target	type
    //   2	26	97	finally
    //   29	82	97	finally
    //   86	94	97	finally
  }
  
  public void dismissConnectDialog() {
    if (this.mProgressDialog != null)
      this.mProgressDialog.dismiss(); 
  }
  
  public ControlDevice getDevice(int paramInt) {
    ControlDevice controlDevice2;
    ControlDevice controlDevice1 = null;
    Iterator<ControlDevice> iterator = getDevices().iterator();
    while (true) {
      controlDevice2 = controlDevice1;
      if (iterator.hasNext()) {
        controlDevice2 = iterator.next();
        if (controlDevice2.getXDevice().getDeviceId() == paramInt)
          break; 
        continue;
      } 
      break;
    } 
    return controlDevice2;
  }
  
  public ControlDevice getDevice(XDevice paramXDevice) {
    return deviceMap.get(paramXDevice.getMacAddress());
  }
  
  public ControlDevice getDevice(String paramString) {
    return (paramString == null) ? null : deviceMap.get(paramString);
  }
  
  public ArrayList<ControlDevice> getDevices() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: getstatic app/gamer/quadstellar/net/DeviceManager.listDev : Ljava/util/ArrayList;
    //   5: invokevirtual clear : ()V
    //   8: getstatic app/gamer/quadstellar/net/DeviceManager.deviceMap : Ljava/util/concurrent/ConcurrentHashMap;
    //   11: invokevirtual entrySet : ()Ljava/util/Set;
    //   14: invokeinterface iterator : ()Ljava/util/Iterator;
    //   19: astore_1
    //   20: aload_1
    //   21: invokeinterface hasNext : ()Z
    //   26: ifeq -> 76
    //   29: aload_1
    //   30: invokeinterface next : ()Ljava/lang/Object;
    //   35: checkcast java/util/Map$Entry
    //   38: astore_2
    //   39: aload_2
    //   40: invokeinterface getValue : ()Ljava/lang/Object;
    //   45: checkcast app/gamer/quadstellar/mode/ControlDevice
    //   48: invokevirtual getType : ()I
    //   51: iconst_m1
    //   52: if_icmpeq -> 20
    //   55: getstatic app/gamer/quadstellar/net/DeviceManager.listDev : Ljava/util/ArrayList;
    //   58: aload_2
    //   59: invokeinterface getValue : ()Ljava/lang/Object;
    //   64: invokevirtual add : (Ljava/lang/Object;)Z
    //   67: pop
    //   68: goto -> 20
    //   71: astore_2
    //   72: aload_0
    //   73: monitorexit
    //   74: aload_2
    //   75: athrow
    //   76: getstatic app/gamer/quadstellar/net/DeviceManager.listDev : Ljava/util/ArrayList;
    //   79: astore_2
    //   80: aload_0
    //   81: monitorexit
    //   82: aload_2
    //   83: areturn
    // Exception table:
    //   from	to	target	type
    //   2	20	71	finally
    //   20	68	71	finally
    //   76	80	71	finally
  }
  
  public ControlDevice getRecentNorDev() {
    ArrayList<ControlDevice> arrayList;
    ControlDevice controlDevice1 = getDevice(PreferenceHelper.readString("recent_normal_device"));
    ControlDevice controlDevice2 = controlDevice1;
    if (controlDevice1 == null) {
      arrayList = getDevices();
      if (arrayList == null)
        return null; 
    } else {
      return controlDevice2;
    } 
    byte b = 0;
    while (true) {
      controlDevice2 = controlDevice1;
      if (b < arrayList.size()) {
        controlDevice2 = arrayList.get(b);
        int i = controlDevice2.getType();
        if (controlDevice2.getType() != 0 && i != 4 && i != 6) {
          b++;
          continue;
        } 
      } 
      return controlDevice2;
    } 
  }
  
  public ControlDevice getRecentTelDev() {
    ArrayList<ControlDevice> arrayList;
    ControlDevice controlDevice1 = getDevice(PreferenceHelper.readString("recent_tele_device"));
    ControlDevice controlDevice2 = controlDevice1;
    if (controlDevice1 == null) {
      arrayList = getDevices();
      if (arrayList == null)
        return null; 
    } else {
      return controlDevice2;
    } 
    byte b = 0;
    while (true) {
      controlDevice2 = controlDevice1;
      if (b < arrayList.size()) {
        controlDevice2 = arrayList.get(b);
        int i = controlDevice2.getType();
        if (i != 3 && i != 4) {
          b++;
          continue;
        } 
      } 
      return controlDevice2;
    } 
  }
  
  public String getWeekList(ArrayList<Integer> paramArrayList) {
    StringBuffer stringBuffer = new StringBuffer();
    for (Integer integer : paramArrayList)
      stringBuffer.append(integer + " "); 
    return stringBuffer.toString();
  }
  
  public void notificationSocket(ControlDevice paramControlDevice, int paramInt) {
    Intent intent = new Intent(Config.BROADCAST_SOCKET_STATUS);
    intent.putExtra("device-mac", paramControlDevice.getMacAddress());
    intent.putExtra("status", paramInt);
    App.getInstance().sendBroadcast(intent);
  }
  
  public void notificationTimer(ControlDevice paramControlDevice) {
    Intent intent = new Intent(Config.BROADCAST_TIMER_UPDATE);
    intent.putExtra("device-mac", paramControlDevice.getMacAddress());
    App.getInstance().sendBroadcast(intent);
  }
  
  public void removeDevice(XDevice paramXDevice) {
    removeDevice(paramXDevice.getMacAddress());
  }
  
  public void removeDevice(String paramString) {
    deviceMap.remove(paramString);
    PreferenceHelper.remove(paramString);
    XlinkAgent.getInstance().removeDevice(paramString);
    XTGlobals.deleteProperty(paramString);
  }
  
  public void saveDevice(ControlDevice paramControlDevice) {
    JSONObject jSONObject = XlinkAgent.deviceToJson(paramControlDevice.getXDevice());
    if (jSONObject != null) {
      if (paramControlDevice.getPassword() != null)
        try {
          jSONObject.put(PASSWORD, paramControlDevice.getPassword());
        } catch (JSONException jSONException) {
          jSONException.printStackTrace();
        }  
      LogUtil.e("DeviceManage", "save device:" + jSONObject);
      XTGlobals.setProperty(paramControlDevice.getMacAddress(), jSONObject.toString());
    } 
  }
  
  public void setAuth(String paramString1, String paramString2) {
    ControlDevice controlDevice = deviceMap.get(paramString1);
    if (controlDevice != null) {
      controlDevice.setPassword(paramString2);
      updateDevice(controlDevice);
    } 
  }
  
  public void updateDevice(ControlDevice paramControlDevice) {
    deviceMap.remove(paramControlDevice.getMacAddress());
    deviceMap.put(paramControlDevice.getMacAddress(), paramControlDevice);
    saveDevice(paramControlDevice);
  }
  
  public void updateDevice(XDevice paramXDevice) {
    ControlDevice controlDevice = deviceMap.get(paramXDevice.getMacAddress());
    if (controlDevice != null) {
      controlDevice.setxDevice(paramXDevice);
      updateDevice(controlDevice);
    } 
  }
  
  public void updateNoSaveDevice(ControlDevice paramControlDevice) {
    deviceMap.remove(paramControlDevice.getMacAddress());
    deviceMap.put(paramControlDevice.getMacAddress(), paramControlDevice);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/net/DeviceManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */