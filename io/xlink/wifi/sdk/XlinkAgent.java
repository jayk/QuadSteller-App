package io.xlink.wifi.sdk;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.SparseArray;
import io.xlink.wifi.sdk.encoder.b;
import io.xlink.wifi.sdk.encoder.c;
import io.xlink.wifi.sdk.encoder.d;
import io.xlink.wifi.sdk.encoder.e;
import io.xlink.wifi.sdk.encoder.f;
import io.xlink.wifi.sdk.encoder.g;
import io.xlink.wifi.sdk.event.a;
import io.xlink.wifi.sdk.event.b;
import io.xlink.wifi.sdk.event.c;
import io.xlink.wifi.sdk.global.a;
import io.xlink.wifi.sdk.listener.ConnectDeviceListener;
import io.xlink.wifi.sdk.listener.ScanDeviceListener;
import io.xlink.wifi.sdk.listener.SendPipeListener;
import io.xlink.wifi.sdk.listener.SetDataPointListener;
import io.xlink.wifi.sdk.listener.SetDeviceAccessKeyListener;
import io.xlink.wifi.sdk.listener.SubscribeDeviceListener;
import io.xlink.wifi.sdk.listener.XlinkNetListener;
import io.xlink.wifi.sdk.manage.a;
import io.xlink.wifi.sdk.manage.b;
import io.xlink.wifi.sdk.manage.c;
import io.xlink.wifi.sdk.tcp.a;
import io.xlink.wifi.sdk.tcp.c;
import io.xlink.wifi.sdk.udp.b;
import io.xlink.wifi.sdk.util.MyLog;
import io.xlink.wifi.sdk.util.b;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class XlinkAgent {
  protected static boolean b = false;
  
  public boolean a = false;
  
  boolean c = false;
  
  private XlinkAgent() {}
  
  public static XDevice JsonToDevice(JSONObject paramJSONObject) {
    return (paramJSONObject == null) ? null : a.a().a(paramJSONObject);
  }
  
  private int a(XDevice paramXDevice, byte paramByte, byte[] paramArrayOfbyte, SendPipeListener paramSendPipeListener) {
    return (paramArrayOfbyte == null || paramSendPipeListener == null) ? -8 : (!b ? -1 : (c.a().a(paramXDevice) ? -3 : ((paramXDevice == null) ? -6 : (!paramXDevice.isLAN() ? -2 : b.a().a(paramXDevice, paramByte, paramArrayOfbyte, paramSendPipeListener)))));
  }
  
  private int a(XDevice paramXDevice, int paramInt, SetDeviceAccessKeyListener paramSetDeviceAccessKeyListener) {
    if (!b)
      return -1; 
    if (paramInt < 0 || paramInt > 999999999)
      return -8; 
    if (c.a().a(paramXDevice))
      return -3; 
    XDevice xDevice = c.a().b(paramXDevice.getMacAddress());
    if (xDevice == null)
      return -6; 
    paramXDevice.b(paramInt);
    paramXDevice.setAuthkey(paramInt + "");
    return b.a().a(xDevice, paramInt, paramSetDeviceAccessKeyListener);
  }
  
  @Deprecated
  private int a(XDevice paramXDevice, String paramString1, String paramString2, SetDeviceAccessKeyListener paramSetDeviceAccessKeyListener) {
    int i;
    if (!b)
      return -1; 
    if (TextUtils.isEmpty(paramString1) || TextUtils.isEmpty(paramString2) || paramSetDeviceAccessKeyListener == null)
      return -8; 
    if (c.a().a(paramXDevice))
      return -3; 
    XDevice xDevice = c.a().b(paramXDevice.getMacAddress());
    if (xDevice == null)
      return -6; 
    paramXDevice.setAuthkey(paramString2);
    try {
      paramXDevice.b(Integer.parseInt(paramString2));
      i = b.a().a(xDevice, paramString1, paramString2, paramSetDeviceAccessKeyListener);
    } catch (Exception exception) {}
    return i;
  }
  
  private void a() {
    Intent intent = new Intent(b.a, XlinkUdpService.class);
    b.a.startService(intent);
    b("start inner service");
  }
  
  private void a(int paramInt) {
    if (XlinkTcpService.c()) {
      c.a().a(paramInt);
      try {
        Thread.sleep(100L);
      } catch (InterruptedException interruptedException) {
        interruptedException.printStackTrace();
      } 
    } 
    if (XlinkTcpService.a() != null)
      XlinkTcpService.a().a(false, 0, false); 
    Intent intent = new Intent(b.a, XlinkTcpService.class);
    b.a.stopService(intent);
    b("stop cloud Service");
  }
  
  private void a(int paramInt, String paramString) {
    XlinkTcpService.a = paramInt;
    XlinkTcpService.b = paramString;
    Intent intent = new Intent(b.a, XlinkTcpService.class);
    b.a.startService(intent);
    b("start cloud Service");
  }
  
  private void a(XDevice paramXDevice) {
    d d = f.a().c(paramXDevice);
    if (d != null) {
      g g = new g(d);
      a.a().d(g);
    } 
  }
  
  private void b() {
    if (!this.c) {
      this.c = true;
      b.b(new Runnable(this) {
            public void run() {
              for (XDevice xDevice : c.a().c()) {
                XlinkAgent.a(this.a, xDevice);
                try {
                  Thread.sleep(30L);
                } catch (InterruptedException interruptedException) {}
              } 
              if (XlinkUdpService.b() != null)
                XlinkUdpService.b().a(false, 0); 
              Intent intent = new Intent(b.a, XlinkUdpService.class);
              b.a.stopService(intent);
              this.a.c = false;
              XlinkAgent.a("stop inner service");
              c.a().b();
            }
          });
    } 
  }
  
  private static void b(String paramString) {
    MyLog.e("XlinkAgent", paramString);
  }
  
  public static void debug(boolean paramBoolean) {
    MyLog.a = Boolean.valueOf(paramBoolean);
  }
  
  public static JSONObject deviceToJson(XDevice paramXDevice) {
    XDevice xDevice1 = null;
    if (paramXDevice == null)
      return (JSONObject)xDevice1; 
    XDevice xDevice2 = c.a().b(paramXDevice.getMacAddress());
    xDevice1 = xDevice2;
    if (xDevice2 == null)
      xDevice1 = paramXDevice; 
    xDevice1.setDeviceName(paramXDevice.getDeviceName());
    try {
      JSONObject jSONObject = a.a().c(paramXDevice);
    } catch (JSONException jSONException) {
      jSONException.printStackTrace();
      jSONException = null;
    } 
    return (JSONObject)jSONException;
  }
  
  public static XlinkAgent getInstance() {
    return a.a();
  }
  
  public static void init(Context paramContext) {
    b.a = paramContext;
    b.a();
    b = true;
  }
  
  public static int scanByMac(XDevice paramXDevice, a parama) {
    if (!b)
      return -1; 
    if (!XlinkUdpService.a())
      return -4; 
    b.a().a(paramXDevice, parama);
    return 0;
  }
  
  public static void setCMServer(String paramString, int paramInt) {
    a.a = paramString;
    a.b = paramInt;
  }
  
  public static boolean setDataTemplate(String paramString1, String paramString2) {
    boolean bool = true;
    try {
      JSONArray jSONArray = new JSONArray();
      this(paramString2);
      SparseArray sparseArray = new SparseArray();
      this();
      int i = jSONArray.length();
      for (byte b = 0; b < i; b++) {
        byte b1;
        JSONObject jSONObject = jSONArray.getJSONObject(b);
        int j = jSONObject.getInt("key");
        String str = jSONObject.getString("type");
        if (str.equals("bool")) {
          b1 = 1;
        } else if (str.equals("byte")) {
          b1 = 2;
        } else if (str.equals("int16")) {
          b1 = 3;
        } else if (str.equals("int32")) {
          b1 = 4;
        } else if (str.equals("string")) {
          b1 = 5;
        } else {
          jSONException2 = new JSONException();
          StringBuilder stringBuilder1 = new StringBuilder();
          this();
          this(stringBuilder1.append("type:").append(str).append(" Not Found ").toString());
          throw jSONException2;
        } 
        DataPoint dataPoint = new DataPoint();
        this(j, b1);
        jSONException2.put(dataPoint.getKey(), dataPoint);
      } 
    } catch (JSONException jSONException1) {
      jSONException1.printStackTrace();
      b("setDataTemplate error：json Exception");
      return false;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    this();
    JSONException jSONException2;
    b(stringBuilder.append("setDataTemplate succeed id：").append((String)jSONException1).append(" point size:").append(jSONException2.size()).toString());
    b.a().a((String)jSONException1, (SparseArray)jSONException2);
    return bool;
  }
  
  protected int a(XDevice paramXDevice, int paramInt1, b paramb, int paramInt2) {
    if (paramInt1 < 0 || paramInt1 > 999999999)
      return -8; 
    if (!b)
      return -1; 
    if (c.a().a(paramXDevice))
      return -3; 
    if (!XlinkUdpService.a())
      return -4; 
    paramXDevice = c.a().b(paramXDevice.getMacAddress());
    if (paramXDevice == null)
      return -6; 
    b.a().a(paramXDevice, paramInt1, paramb, paramInt2);
    return 0;
  }
  
  protected int a(XDevice paramXDevice, a parama) {
    // Byte code:
    //   0: bipush #-4
    //   2: istore_3
    //   3: invokestatic a : ()Lio/xlink/wifi/sdk/manage/a;
    //   6: invokevirtual d : ()Z
    //   9: ifne -> 19
    //   12: bipush #-10
    //   14: istore #4
    //   16: iload #4
    //   18: ireturn
    //   19: invokestatic c : ()Z
    //   22: ifne -> 34
    //   25: iload_3
    //   26: istore #4
    //   28: invokestatic a : ()Z
    //   31: ifeq -> 16
    //   34: invokestatic a : ()Lio/xlink/wifi/sdk/manage/c;
    //   37: aload_1
    //   38: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   41: invokevirtual b : (Ljava/lang/String;)Lio/xlink/wifi/sdk/XDevice;
    //   44: astore #5
    //   46: aload #5
    //   48: ifnonnull -> 58
    //   51: bipush #-6
    //   53: istore #4
    //   55: goto -> 16
    //   58: aload #5
    //   60: invokevirtual isLAN : ()Z
    //   63: ifeq -> 86
    //   66: invokestatic a : ()Z
    //   69: ifeq -> 86
    //   72: invokestatic a : ()Lio/xlink/wifi/sdk/encoder/f;
    //   75: aload #5
    //   77: invokevirtual a : (Lio/xlink/wifi/sdk/XDevice;)V
    //   80: iconst_0
    //   81: istore #4
    //   83: goto -> 16
    //   86: aload #5
    //   88: invokevirtual isValidId : ()Z
    //   91: ifne -> 101
    //   94: bipush #-9
    //   96: istore #4
    //   98: goto -> 16
    //   101: iload_3
    //   102: istore #4
    //   104: invokestatic c : ()Z
    //   107: ifeq -> 16
    //   110: invokestatic a : ()Lio/xlink/wifi/sdk/tcp/c;
    //   113: aload_1
    //   114: iconst_0
    //   115: aload_2
    //   116: bipush #7
    //   118: invokevirtual a : (Lio/xlink/wifi/sdk/XDevice;ILio/xlink/wifi/sdk/event/a;I)V
    //   121: iconst_0
    //   122: istore #4
    //   124: goto -> 16
  }
  
  protected int a(XDevice paramXDevice, String paramString, b paramb, int paramInt) {
    if (TextUtils.isEmpty(paramString) || paramb == null)
      return -8; 
    if (!b)
      return -1; 
    if (c.a().a(paramXDevice))
      return -3; 
    if (!XlinkUdpService.a())
      return -4; 
    paramXDevice = c.a().b(paramXDevice.getMacAddress());
    if (paramXDevice == null)
      return -6; 
    b.a().a(paramXDevice, paramString, paramb, paramInt);
    return 0;
  }
  
  public void addXlinkListener(XlinkNetListener paramXlinkNetListener) {
    c.a(paramXlinkNetListener.getClass().getName(), paramXlinkNetListener);
  }
  
  public int connectDevice(XDevice paramXDevice, int paramInt, ConnectDeviceListener paramConnectDeviceListener) {
    return !b ? -1 : ((paramXDevice.getVersion() == 1) ? (new b(paramXDevice, paramInt + "", paramConnectDeviceListener)).a() : (new c(paramXDevice, paramInt, paramConnectDeviceListener)).a());
  }
  
  @Deprecated
  public int connectDevice(XDevice paramXDevice, String paramString, ConnectDeviceListener paramConnectDeviceListener) {
    return !b ? -1 : (new b(paramXDevice, paramString, paramConnectDeviceListener)).a();
  }
  
  public int initDevice(XDevice paramXDevice) {
    if (c.a().a(paramXDevice))
      return -3; 
    paramXDevice.a(-1);
    paramXDevice.c(-1);
    c.a().b(paramXDevice);
    return 0;
  }
  
  public boolean isConnectedLocal() {
    return XlinkUdpService.a();
  }
  
  public boolean isConnectedOuterNet() {
    return XlinkTcpService.c();
  }
  
  public int login(int paramInt, String paramString) {
    byte b1 = -8;
    byte b2 = b1;
    if (paramInt != 0) {
      if (TextUtils.isEmpty(paramString))
        return b1; 
    } else {
      return b2;
    } 
    if (!b)
      return -1; 
    if (XlinkTcpService.c() || XlinkTcpService.d)
      return -7; 
    b2 = b1;
    if (paramInt != 0) {
      b2 = b1;
      if (!TextUtils.isEmpty(paramString)) {
        a.a().b();
        a(paramInt, paramString);
        b2 = 0;
      } 
    } 
    return b2;
  }
  
  public void removeAllDevice() {
    c.a().b();
  }
  
  public int removeDevice(XDevice paramXDevice) {
    if (paramXDevice == null || paramXDevice.getMacAddress() == null)
      return -8; 
    removeDevice(paramXDevice.getMacAddress());
    return 0;
  }
  
  public int removeDevice(String paramString) {
    if (paramString == null)
      return -8; 
    c.a().c(paramString);
    return 0;
  }
  
  public void removeListener(XlinkNetListener paramXlinkNetListener) {
    c.a(paramXlinkNetListener.getClass().getName());
  }
  
  public int scanDeviceByProductId(String paramString, ScanDeviceListener paramScanDeviceListener) {
    byte b1 = -10;
    if (!b)
      return -1; 
    byte b2 = b1;
    if (a.a().d()) {
      if (!XlinkUdpService.a())
        return -4; 
      if (TextUtils.isEmpty(paramString))
        return -8; 
      b2 = b1;
      if (!TextUtils.isEmpty(b.d())) {
        c.a(paramScanDeviceListener);
        b.a().a(paramString);
        b2 = 0;
      } 
    } 
    return b2;
  }
  
  public int sendPipe(int paramInt, byte[] paramArrayOfbyte, SendPipeListener paramSendPipeListener) {
    return (paramArrayOfbyte == null || paramSendPipeListener == null) ? -8 : (!a.a().d() ? -10 : (!b ? -1 : (XlinkTcpService.c() ? c.a().a(paramInt, (byte)0, paramArrayOfbyte, paramSendPipeListener, a.l) : -4)));
  }
  
  public int sendPipeData(XDevice paramXDevice, byte paramByte, byte[] paramArrayOfbyte, SendPipeListener paramSendPipeListener) {
    if (paramArrayOfbyte == null || paramSendPipeListener == null)
      return -8; 
    if (!a.a().d())
      return -10; 
    if (!b)
      return -1; 
    if (c.a().a(paramXDevice))
      return -3; 
    paramXDevice = c.a().b(paramXDevice.getMacAddress());
    return (paramXDevice == null) ? -6 : ((XlinkUdpService.a() && paramXDevice.getDevcieConnectStates() == 0) ? a(paramXDevice, paramByte, paramArrayOfbyte, paramSendPipeListener) : (XlinkTcpService.c() ? (!paramXDevice.isValidId() ? -9 : c.a().a(paramXDevice, paramByte, paramArrayOfbyte, paramSendPipeListener, a.l)) : -4));
  }
  
  public int sendPipeData(XDevice paramXDevice, byte[] paramArrayOfbyte, SendPipeListener paramSendPipeListener) {
    return sendPipeData(paramXDevice, (byte)0, paramArrayOfbyte, paramSendPipeListener);
  }
  
  public int sendProbe(XDevice paramXDevice) {
    // Byte code:
    //   0: bipush #-4
    //   2: istore_2
    //   3: invokestatic a : ()Lio/xlink/wifi/sdk/manage/a;
    //   6: invokevirtual d : ()Z
    //   9: ifne -> 17
    //   12: bipush #-10
    //   14: istore_3
    //   15: iload_3
    //   16: ireturn
    //   17: invokestatic c : ()Z
    //   20: ifne -> 31
    //   23: iload_2
    //   24: istore_3
    //   25: invokestatic a : ()Z
    //   28: ifeq -> 15
    //   31: invokestatic a : ()Lio/xlink/wifi/sdk/manage/c;
    //   34: aload_1
    //   35: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   38: invokevirtual b : (Ljava/lang/String;)Lio/xlink/wifi/sdk/XDevice;
    //   41: astore #4
    //   43: aload #4
    //   45: ifnonnull -> 54
    //   48: bipush #-6
    //   50: istore_3
    //   51: goto -> 15
    //   54: aload #4
    //   56: invokevirtual isLAN : ()Z
    //   59: ifeq -> 81
    //   62: invokestatic a : ()Z
    //   65: ifeq -> 81
    //   68: invokestatic a : ()Lio/xlink/wifi/sdk/encoder/f;
    //   71: aload #4
    //   73: invokevirtual a : (Lio/xlink/wifi/sdk/XDevice;)V
    //   76: iconst_0
    //   77: istore_3
    //   78: goto -> 15
    //   81: aload #4
    //   83: invokevirtual isValidId : ()Z
    //   86: ifne -> 95
    //   89: bipush #-9
    //   91: istore_3
    //   92: goto -> 15
    //   95: iload_2
    //   96: istore_3
    //   97: invokestatic c : ()Z
    //   100: ifeq -> 15
    //   103: invokestatic a : ()Lio/xlink/wifi/sdk/tcp/c;
    //   106: aload_1
    //   107: iconst_0
    //   108: aconst_null
    //   109: bipush #7
    //   111: invokevirtual a : (Lio/xlink/wifi/sdk/XDevice;ILio/xlink/wifi/sdk/event/a;I)V
    //   114: iconst_0
    //   115: istore_3
    //   116: goto -> 15
  }
  
  public int setDataPoint(XDevice paramXDevice, int paramInt, Object paramObject, SetDataPointListener paramSetDataPointListener) {
    // Byte code:
    //   0: bipush #-4
    //   2: istore #5
    //   4: invokestatic a : ()Lio/xlink/wifi/sdk/manage/a;
    //   7: invokevirtual d : ()Z
    //   10: ifne -> 20
    //   13: bipush #-10
    //   15: istore #6
    //   17: iload #6
    //   19: ireturn
    //   20: invokestatic c : ()Z
    //   23: ifne -> 36
    //   26: iload #5
    //   28: istore #6
    //   30: invokestatic a : ()Z
    //   33: ifeq -> 17
    //   36: invokestatic a : ()Lio/xlink/wifi/sdk/manage/c;
    //   39: aload_1
    //   40: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   43: invokevirtual b : (Ljava/lang/String;)Lio/xlink/wifi/sdk/XDevice;
    //   46: astore_1
    //   47: aload_1
    //   48: ifnonnull -> 58
    //   51: bipush #-6
    //   53: istore #6
    //   55: goto -> 17
    //   58: invokestatic a : ()Lio/xlink/wifi/sdk/manage/b;
    //   61: aload_1
    //   62: iload_2
    //   63: aload_3
    //   64: invokevirtual a : (Lio/xlink/wifi/sdk/XDevice;ILjava/lang/Object;)Lio/xlink/wifi/sdk/DataPoint;
    //   67: astore_3
    //   68: aload_3
    //   69: ifnonnull -> 79
    //   72: bipush #-11
    //   74: istore #6
    //   76: goto -> 17
    //   79: aload_1
    //   80: invokevirtual isLAN : ()Z
    //   83: ifeq -> 107
    //   86: invokestatic a : ()Z
    //   89: ifeq -> 107
    //   92: invokestatic a : ()Lio/xlink/wifi/sdk/udp/b;
    //   95: aload_1
    //   96: aload_3
    //   97: aload #4
    //   99: invokevirtual a : (Lio/xlink/wifi/sdk/XDevice;Lio/xlink/wifi/sdk/DataPoint;Lio/xlink/wifi/sdk/listener/SetDataPointListener;)I
    //   102: istore #6
    //   104: goto -> 17
    //   107: aload_1
    //   108: invokevirtual isValidId : ()Z
    //   111: ifne -> 121
    //   114: bipush #-9
    //   116: istore #6
    //   118: goto -> 17
    //   121: iload #5
    //   123: istore #6
    //   125: invokestatic c : ()Z
    //   128: ifeq -> 17
    //   131: invokestatic a : ()Lio/xlink/wifi/sdk/tcp/c;
    //   134: aload_1
    //   135: aload_3
    //   136: aload #4
    //   138: invokevirtual a : (Lio/xlink/wifi/sdk/XDevice;Lio/xlink/wifi/sdk/DataPoint;Lio/xlink/wifi/sdk/listener/SetDataPointListener;)I
    //   141: istore #6
    //   143: goto -> 17
  }
  
  public int setDeviceAccessKey(XDevice paramXDevice, int paramInt, SetDeviceAccessKeyListener paramSetDeviceAccessKeyListener) {
    if (paramXDevice.getVersion() == 1)
      return setDeviceAuthorizeCode(paramXDevice, paramInt + "", paramInt + "", paramSetDeviceAccessKeyListener); 
    if (!a.a().d())
      return -10; 
    if (paramInt < 0 || paramInt > 999999999)
      return -8; 
    if (!b)
      return -1; 
    if (c.a().a(paramXDevice))
      return -3; 
    paramXDevice = c.a().b(paramXDevice.getMacAddress());
    return (paramXDevice == null) ? -6 : a(paramXDevice, paramInt, paramSetDeviceAccessKeyListener);
  }
  
  @Deprecated
  public int setDeviceAuthorizeCode(XDevice paramXDevice, String paramString1, String paramString2, SetDeviceAccessKeyListener paramSetDeviceAccessKeyListener) {
    if (!a.a().d())
      return -10; 
    if (TextUtils.isEmpty(paramString2) || paramString2.length() < 3 || paramSetDeviceAccessKeyListener == null)
      return -8; 
    if (!b)
      return -1; 
    if (c.a().a(paramXDevice))
      return -3; 
    paramXDevice = c.a().b(paramXDevice.getMacAddress());
    if (paramXDevice == null)
      return -6; 
    if (XlinkUdpService.a() && paramXDevice.isLAN())
      return a(paramXDevice, paramString1, paramString2, paramSetDeviceAccessKeyListener); 
    if (!XlinkUdpService.a() && !XlinkTcpService.c())
      return -4; 
    if (!XlinkUdpService.a() && XlinkTcpService.c())
      return !paramXDevice.isValidId() ? -9 : c.a().a(paramXDevice, paramString1, paramString2, paramSetDeviceAccessKeyListener, a.l); 
    if (!XlinkUdpService.a() || !XlinkTcpService.c());
    return scanByMac(paramXDevice, new a(this, paramString1, paramString2, paramSetDeviceAccessKeyListener) {
          public void onResponse(e param1e) {
            switch (param1e.b) {
              default:
                return;
              case 0:
                if (XlinkAgent.a(this.d, param1e.a, this.a, this.b, this.c) < 0) {
                  param1e.b = -100;
                  this.c.onResponse(param1e);
                } 
              case -100:
                break;
            } 
            if (!XlinkTcpService.c())
              if (XlinkAgent.a(this.d, param1e.a, this.a, this.b, this.c) < 0) {
                param1e.b = -100;
                this.c.onResponse(param1e);
              }  
            if (!param1e.a.isValidId()) {
              if (this.d.isConnectedLocal())
                if (XlinkAgent.a(this.d, param1e.a, this.a, this.b, this.c) < 0) {
                  param1e.b = -100;
                  this.c.onResponse(param1e);
                }  
              param1e.b = 3;
              this.c.onResponse(param1e);
            } 
            c.a().a(param1e.a, this.a, this.b, this.c, a.l);
          }
        });
  }
  
  public int start() {
    byte b = 0;
    this.a = false;
    if (!b)
      return -1; 
    a.a().b();
    if (XlinkUdpService.a())
      return -7; 
    a();
    return b;
  }
  
  public void stop() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: getstatic io/xlink/wifi/sdk/XlinkAgent.b : Z
    //   5: istore_1
    //   6: iload_1
    //   7: ifne -> 13
    //   10: aload_0
    //   11: monitorexit
    //   12: return
    //   13: aload_0
    //   14: iconst_1
    //   15: putfield a : Z
    //   18: iconst_0
    //   19: putstatic io/xlink/wifi/sdk/XlinkTcpService.a : I
    //   22: aconst_null
    //   23: putstatic io/xlink/wifi/sdk/XlinkTcpService.b : Ljava/lang/String;
    //   26: invokestatic a : ()Lio/xlink/wifi/sdk/manage/a;
    //   29: invokevirtual e : ()V
    //   32: aload_0
    //   33: invokespecial b : ()V
    //   36: ldc2_w 300
    //   39: invokestatic sleep : (J)V
    //   42: aload_0
    //   43: iconst_0
    //   44: invokespecial a : (I)V
    //   47: invokestatic a : ()Lio/xlink/wifi/sdk/tcp/a;
    //   50: invokevirtual d : ()V
    //   53: goto -> 10
    //   56: astore_2
    //   57: aload_0
    //   58: monitorexit
    //   59: aload_2
    //   60: athrow
    //   61: astore_2
    //   62: aload_2
    //   63: invokevirtual printStackTrace : ()V
    //   66: goto -> 42
    // Exception table:
    //   from	to	target	type
    //   2	6	56	finally
    //   13	36	56	finally
    //   36	42	61	java/lang/InterruptedException
    //   36	42	56	finally
    //   42	53	56	finally
    //   62	66	56	finally
  }
  
  public int subscribeDevice(XDevice paramXDevice, int paramInt, SubscribeDeviceListener paramSubscribeDeviceListener) {
    if (paramXDevice.getVersion() == 1)
      return subscribeDevice(paramXDevice, paramInt + "", paramSubscribeDeviceListener); 
    if (!b)
      return -1; 
    if (paramInt < 0 || paramInt > 999999999)
      return -8; 
    if (c.a().a(paramXDevice))
      return -3; 
    if (!XlinkTcpService.c())
      return -4; 
    paramXDevice = c.a().b(paramXDevice.getMacAddress());
    if (paramXDevice == null)
      return -6; 
    c.a().a(paramXDevice, paramInt, paramSubscribeDeviceListener);
    return 0;
  }
  
  @Deprecated
  public int subscribeDevice(XDevice paramXDevice, String paramString, SubscribeDeviceListener paramSubscribeDeviceListener) {
    if (!b)
      return -1; 
    if (TextUtils.isEmpty(paramString))
      return -8; 
    if (c.a().a(paramXDevice))
      return -3; 
    if (!XlinkTcpService.c())
      return -4; 
    paramXDevice = c.a().b(paramXDevice.getMacAddress());
    if (paramXDevice == null)
      return -6; 
    c.a().a(paramXDevice, paramString, paramSubscribeDeviceListener);
    return 0;
  }
  
  @Deprecated
  public int unsubscribeDevice(XDevice paramXDevice, int paramInt, SubscribeDeviceListener paramSubscribeDeviceListener) {
    if (paramXDevice.getVersion() == 1)
      return unsubscribeDevice(paramXDevice, paramInt + "", paramSubscribeDeviceListener); 
    if (!b)
      return -1; 
    if (paramInt < 0 || paramInt > 999999999)
      return -8; 
    if (c.a().a(paramXDevice))
      return -3; 
    if (!XlinkTcpService.c())
      return -4; 
    paramXDevice = c.a().b(paramXDevice.getMacAddress());
    if (paramXDevice == null)
      return -6; 
    c.a().b(paramXDevice, paramInt, paramSubscribeDeviceListener);
    return 0;
  }
  
  @Deprecated
  public int unsubscribeDevice(XDevice paramXDevice, String paramString, SubscribeDeviceListener paramSubscribeDeviceListener) {
    if (!b)
      return -1; 
    if (TextUtils.isEmpty(paramString))
      return -8; 
    if (c.a().a(paramXDevice))
      return -3; 
    if (!XlinkTcpService.c())
      return -4; 
    paramXDevice = c.a().b(paramXDevice.getMacAddress());
    if (paramXDevice == null)
      return -6; 
    c.a().b(paramXDevice, paramString, paramSubscribeDeviceListener);
    return 0;
  }
  
  private static class a {
    private static XlinkAgent a = new XlinkAgent();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/XlinkAgent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */