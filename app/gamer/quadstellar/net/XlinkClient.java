package app.gamer.quadstellar.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import app.gamer.quadstellar.App;
import app.gamer.quadstellar.Prefix;
import app.gamer.quadstellar.callback.IDeviceConnectCallback;
import app.gamer.quadstellar.callback.ISendCallback;
import app.gamer.quadstellar.domain.CodeEvent;
import app.gamer.quadstellar.mode.ControlDevice;
import app.gamer.quadstellar.mode.EFDeviceLight;
import app.gamer.quadstellar.utils.ByteUtils;
import app.gamer.quadstellar.utils.LogUtil;
import app.gamer.quadstellar.utils.XlinkUtils;
import io.xlink.wifi.sdk.XDevice;
import io.xlink.wifi.sdk.XlinkAgent;
import io.xlink.wifi.sdk.listener.SendPipeListener;
import org.greenrobot.eventbus.EventBus;

public class XlinkClient implements Handler.Callback, IDeviceConnectCallback {
  private static final int CONNECT_DEV = 2228;
  
  private static final int CONNECT_DEV_ALL = 2230;
  
  private static final int RESULT_MSG = 2225;
  
  private static final int SEND_CANCEL = 2224;
  
  private static final int SEND_END = 2223;
  
  private static final int SEND_PRE = 2221;
  
  private static final int SEND_REPEAT = 2229;
  
  private static final int SEND_START = 2222;
  
  private static final int SEND_TWINK = 2227;
  
  private static final String TAG = "XlinkClient";
  
  private static final int UPDATE_DEVICE_STATUS = 2226;
  
  private static XlinkClient instance = null;
  
  private byte[] data;
  
  private EFDeviceLight light = null;
  
  private ControlDevice mDevice;
  
  private Handler mHandler = new Handler(this);
  
  private ISendCallback mSendCallback;
  
  private boolean mShowTip = false;
  
  private Worker mWorker;
  
  private int repeatCount = 0;
  
  private int repeat_time = 1;
  
  private int requestCode;
  
  public static XlinkClient getInstance() {
    if (instance == null)
      instance = new XlinkClient(); 
    return instance;
  }
  
  private void send(final boolean showTip) {
    if (this.mDevice != null)
      XlinkAgent.getInstance().sendPipeData(this.mDevice.getXDevice(), this.data, new SendPipeListener() {
            public void onSendLocalPipeData(XDevice param1XDevice, int param1Int1, int param1Int2) {
              Log.d("XlinkClient", XlinkClient.this.mDevice.getMacAddress());
              switch (param1Int1) {
                default:
                  bool = false;
                  XlinkClient.this.mHandler.obtainMessage(2225, App.getAppResources().getString(2131296543, new Object[] { Integer.valueOf(param1Int1) })).sendToTarget();
                  LogUtil.e("XlinkClient", "控制设备其他错误码:" + param1Int1);
                  XlinkClient.this.mHandler.sendEmptyMessage(2228);
                  XlinkClient.this.mHandler.obtainMessage(2226, Boolean.valueOf(false)).sendToTarget();
                  XlinkClient.access$402(XlinkClient.this, 0);
                  XlinkClient.this.mHandler.obtainMessage(2223, Boolean.valueOf(bool)).sendToTarget();
                  return;
                case 0:
                  bool = true;
                  if (showTip);
                  XlinkClient.this.mHandler.obtainMessage(2226, Boolean.valueOf(true)).sendToTarget();
                  LogUtil.e("XlinkClient", "发送数据" + XlinkUtils.getHexBinString(XlinkClient.this.data) + "成功");
                  XlinkClient.access$402(XlinkClient.this, 0);
                  XlinkClient.this.mHandler.obtainMessage(2223, Boolean.valueOf(bool)).sendToTarget();
                  return;
                case -100:
                  bool = false;
                  LogUtil.e("XlinkClient", "发送数据超时：" + XlinkUtils.getHexBinString(XlinkClient.this.data));
                  if (XlinkClient.this.repeatCount < XlinkClient.this.repeat_time) {
                    XlinkClient.access$408(XlinkClient.this);
                    XlinkClient.this.mHandler.sendEmptyMessage(2229);
                    XlinkClient.this.mHandler.obtainMessage(2225, App.getAppResources().getString(2131297112)).sendToTarget();
                    return;
                  } 
                  XlinkClient.access$402(XlinkClient.this, 0);
                  XlinkClient.this.mHandler.obtainMessage(2223, Boolean.valueOf(bool)).sendToTarget();
                  return;
                case 5:
                  bool = false;
                  XlinkClient.this.mHandler.obtainMessage(2225, App.getAppResources().getString(2131296977)).sendToTarget();
                  XlinkClient.this.mHandler.sendEmptyMessage(2228);
                  XlinkClient.access$402(XlinkClient.this, 0);
                  XlinkClient.this.mHandler.obtainMessage(2223, Boolean.valueOf(bool)).sendToTarget();
                  return;
                case 10:
                  break;
              } 
              boolean bool = false;
              XlinkClient.this.mHandler.obtainMessage(2225, App.getAppResources().getString(2131296644)).sendToTarget();
              XlinkClient.this.mHandler.obtainMessage(2226, Boolean.valueOf(false)).sendToTarget();
              XlinkClient.access$402(XlinkClient.this, 0);
              XlinkClient.this.mHandler.obtainMessage(2223, Boolean.valueOf(bool)).sendToTarget();
            }
          }); 
  }
  
  private void send(boolean paramBoolean, ControlDevice paramControlDevice, byte[] paramArrayOfbyte) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/lang/StringBuilder
    //   5: astore #4
    //   7: aload #4
    //   9: invokespecial <init> : ()V
    //   12: ldc 'kkkk'
    //   14: aload #4
    //   16: ldc '准备发送 ==='
    //   18: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   21: aload_2
    //   22: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   25: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: invokevirtual toString : ()Ljava/lang/String;
    //   31: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)V
    //   34: invokestatic getInstance : ()Lio/xlink/wifi/sdk/XlinkAgent;
    //   37: astore #5
    //   39: aload_2
    //   40: invokevirtual getXDevice : ()Lio/xlink/wifi/sdk/XDevice;
    //   43: astore #4
    //   45: new app/gamer/quadstellar/net/XlinkClient$1
    //   48: astore #6
    //   50: aload #6
    //   52: aload_0
    //   53: iload_1
    //   54: aload_2
    //   55: invokespecial <init> : (Lapp/gamer/quadstellar/net/XlinkClient;ZLapp/gamer/quadstellar/mode/ControlDevice;)V
    //   58: aload #5
    //   60: aload #4
    //   62: aload_3
    //   63: aload #6
    //   65: invokevirtual sendPipeData : (Lio/xlink/wifi/sdk/XDevice;[BLio/xlink/wifi/sdk/listener/SendPipeListener;)I
    //   68: pop
    //   69: aload_0
    //   70: monitorexit
    //   71: return
    //   72: astore_2
    //   73: aload_0
    //   74: monitorexit
    //   75: aload_2
    //   76: athrow
    // Exception table:
    //   from	to	target	type
    //   2	69	72	finally
  }
  
  public void connectCallback(boolean paramBoolean, ControlDevice paramControlDevice) {
    if (paramBoolean) {
      this.mHandler.sendEmptyMessage(2222);
      return;
    } 
    this.mHandler.sendEmptyMessage(2224);
  }
  
  public boolean handleMessage(Message paramMessage) {
    switch (paramMessage.what) {
      default:
        return false;
      case 2221:
        if (this.mSendCallback != null)
          this.mSendCallback.onSendPre(); 
      case 2222:
        App.getInstance().setRequestCode(this.requestCode);
        this.mWorker = new Worker();
        ThreadManger.execute(this.mWorker);
        if (this.mSendCallback != null)
          this.mSendCallback.onSendStart(); 
      case 2224:
        if (this.mSendCallback != null)
          this.mSendCallback.onSendCancel(); 
      case 2223:
        if (this.mSendCallback != null)
          this.mSendCallback.onSendEnd(((Boolean)paramMessage.obj).booleanValue()); 
      case 2227:
        App.getInstance().setRequestCode(this.requestCode);
        ThreadManger.execute(new TwinkleWorker());
      case 2226:
        DeviceManager.setDeviceStatus(((Boolean)paramMessage.obj).booleanValue());
      case 2225:
        XlinkUtils.shortTips((String)paramMessage.obj);
      case 2228:
        DeviceManager.getInstance().connectDevice(App.getInstance().getCurrentActivity(), this.mDevice, this);
      case 2230:
        DeviceManager.getInstance().connectDeviceLinkage(App.getInstance().getCurrentActivity(), this.mDevice, this);
      case 2229:
        break;
    } 
    this.mWorker = new Worker();
    ThreadManger.execute(this.mWorker);
  }
  
  public void sendPipe(ControlDevice paramControlDevice, byte[] paramArrayOfbyte, int paramInt, ISendCallback paramISendCallback) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_2
    //   4: putfield data : [B
    //   7: aload_0
    //   8: iconst_1
    //   9: putfield mShowTip : Z
    //   12: aload_0
    //   13: aload #4
    //   15: putfield mSendCallback : Lapp/gamer/quadstellar/callback/ISendCallback;
    //   18: aload_0
    //   19: aload_1
    //   20: putfield mDevice : Lapp/gamer/quadstellar/mode/ControlDevice;
    //   23: new java/lang/StringBuilder
    //   26: astore_2
    //   27: aload_2
    //   28: invokespecial <init> : ()V
    //   31: ldc 'XlinkClient'
    //   33: aload_2
    //   34: ldc_w 'mDevice:'
    //   37: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   40: aload_0
    //   41: getfield mDevice : Lapp/gamer/quadstellar/mode/ControlDevice;
    //   44: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   47: invokevirtual toString : ()Ljava/lang/String;
    //   50: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   53: pop
    //   54: aload_0
    //   55: iload_3
    //   56: putfield requestCode : I
    //   59: aload_0
    //   60: getfield mHandler : Landroid/os/Handler;
    //   63: sipush #2221
    //   66: invokevirtual sendEmptyMessage : (I)Z
    //   69: pop
    //   70: aload_0
    //   71: getfield mDevice : Lapp/gamer/quadstellar/mode/ControlDevice;
    //   74: ifnonnull -> 113
    //   77: aload_0
    //   78: getfield mHandler : Landroid/os/Handler;
    //   81: sipush #2224
    //   84: invokevirtual sendEmptyMessage : (I)Z
    //   87: pop
    //   88: aload_0
    //   89: getfield mHandler : Landroid/os/Handler;
    //   92: sipush #2225
    //   95: invokestatic getAppResources : ()Landroid/content/res/Resources;
    //   98: ldc_w 2131296975
    //   101: invokevirtual getString : (I)Ljava/lang/String;
    //   104: invokevirtual obtainMessage : (ILjava/lang/Object;)Landroid/os/Message;
    //   107: invokevirtual sendToTarget : ()V
    //   110: aload_0
    //   111: monitorexit
    //   112: return
    //   113: aload_0
    //   114: getfield mDevice : Lapp/gamer/quadstellar/mode/ControlDevice;
    //   117: invokevirtual getXDevice : ()Lio/xlink/wifi/sdk/XDevice;
    //   120: invokevirtual getDevcieConnectStates : ()I
    //   123: iconst_3
    //   124: if_icmpeq -> 143
    //   127: invokestatic getInstance : ()Lapp/gamer/quadstellar/App;
    //   130: invokevirtual getCurrentDeviceMac : ()Ljava/lang/String;
    //   133: aload_1
    //   134: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   137: invokevirtual equals : (Ljava/lang/Object;)Z
    //   140: ifne -> 165
    //   143: invokestatic getInstance : ()Lapp/gamer/quadstellar/net/DeviceManager;
    //   146: invokestatic getInstance : ()Lapp/gamer/quadstellar/App;
    //   149: invokevirtual getCurrentActivity : ()Landroid/app/Activity;
    //   152: aload_1
    //   153: aload_0
    //   154: invokevirtual connectDevice : (Landroid/app/Activity;Lapp/gamer/quadstellar/mode/ControlDevice;Lapp/gamer/quadstellar/callback/IDeviceConnectCallback;)V
    //   157: goto -> 110
    //   160: astore_1
    //   161: aload_0
    //   162: monitorexit
    //   163: aload_1
    //   164: athrow
    //   165: aload_0
    //   166: getfield mHandler : Landroid/os/Handler;
    //   169: sipush #2222
    //   172: invokevirtual sendEmptyMessage : (I)Z
    //   175: pop
    //   176: goto -> 110
    // Exception table:
    //   from	to	target	type
    //   2	110	160	finally
    //   113	143	160	finally
    //   143	157	160	finally
    //   165	176	160	finally
  }
  
  public void sendPipeLinkage(ControlDevice paramControlDevice, byte[] paramArrayOfbyte, int paramInt, ISendCallback paramISendCallback) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_0
    //   4: putfield mShowTip : Z
    //   7: aload_0
    //   8: aload #4
    //   10: putfield mSendCallback : Lapp/gamer/quadstellar/callback/ISendCallback;
    //   13: aload_0
    //   14: iload_3
    //   15: putfield requestCode : I
    //   18: aload_0
    //   19: getfield mHandler : Landroid/os/Handler;
    //   22: sipush #2221
    //   25: invokevirtual sendEmptyMessage : (I)Z
    //   28: pop
    //   29: aload_1
    //   30: ifnonnull -> 69
    //   33: aload_0
    //   34: getfield mHandler : Landroid/os/Handler;
    //   37: sipush #2224
    //   40: invokevirtual sendEmptyMessage : (I)Z
    //   43: pop
    //   44: aload_0
    //   45: getfield mHandler : Landroid/os/Handler;
    //   48: sipush #2225
    //   51: invokestatic getAppResources : ()Landroid/content/res/Resources;
    //   54: ldc_w 2131296975
    //   57: invokevirtual getString : (I)Ljava/lang/String;
    //   60: invokevirtual obtainMessage : (ILjava/lang/Object;)Landroid/os/Message;
    //   63: invokevirtual sendToTarget : ()V
    //   66: aload_0
    //   67: monitorexit
    //   68: return
    //   69: new java/lang/StringBuilder
    //   72: astore #4
    //   74: aload #4
    //   76: invokespecial <init> : ()V
    //   79: ldc 'kkkk'
    //   81: aload #4
    //   83: ldc_w '取出数据==='
    //   86: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   89: aload_1
    //   90: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   93: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   96: invokevirtual toString : ()Ljava/lang/String;
    //   99: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)V
    //   102: new app/gamer/quadstellar/net/XlinkClient$WorkerMulte
    //   105: astore #4
    //   107: aload #4
    //   109: aload_0
    //   110: aload_1
    //   111: aload_2
    //   112: invokespecial <init> : (Lapp/gamer/quadstellar/net/XlinkClient;Lapp/gamer/quadstellar/mode/ControlDevice;[B)V
    //   115: aload #4
    //   117: invokestatic execute : (Ljava/lang/Runnable;)V
    //   120: aload_0
    //   121: getfield mSendCallback : Lapp/gamer/quadstellar/callback/ISendCallback;
    //   124: ifnull -> 66
    //   127: aload_0
    //   128: getfield mSendCallback : Lapp/gamer/quadstellar/callback/ISendCallback;
    //   131: invokeinterface onSendStart : ()V
    //   136: goto -> 66
    //   139: astore_1
    //   140: aload_0
    //   141: monitorexit
    //   142: aload_1
    //   143: athrow
    // Exception table:
    //   from	to	target	type
    //   2	29	139	finally
    //   33	66	139	finally
    //   69	136	139	finally
  }
  
  public void sendPipeWithoutTip(ControlDevice paramControlDevice, byte[] paramArrayOfbyte, int paramInt, ISendCallback paramISendCallback) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_2
    //   4: putfield data : [B
    //   7: aload_0
    //   8: iconst_0
    //   9: putfield mShowTip : Z
    //   12: aload_0
    //   13: aload #4
    //   15: putfield mSendCallback : Lapp/gamer/quadstellar/callback/ISendCallback;
    //   18: aload_0
    //   19: aload_1
    //   20: putfield mDevice : Lapp/gamer/quadstellar/mode/ControlDevice;
    //   23: aload_0
    //   24: iload_3
    //   25: putfield requestCode : I
    //   28: aload_0
    //   29: getfield mHandler : Landroid/os/Handler;
    //   32: sipush #2221
    //   35: invokevirtual sendEmptyMessage : (I)Z
    //   38: pop
    //   39: aload_0
    //   40: getfield mDevice : Lapp/gamer/quadstellar/mode/ControlDevice;
    //   43: ifnonnull -> 82
    //   46: aload_0
    //   47: getfield mHandler : Landroid/os/Handler;
    //   50: sipush #2224
    //   53: invokevirtual sendEmptyMessage : (I)Z
    //   56: pop
    //   57: aload_0
    //   58: getfield mHandler : Landroid/os/Handler;
    //   61: sipush #2225
    //   64: invokestatic getAppResources : ()Landroid/content/res/Resources;
    //   67: ldc_w 2131296975
    //   70: invokevirtual getString : (I)Ljava/lang/String;
    //   73: invokevirtual obtainMessage : (ILjava/lang/Object;)Landroid/os/Message;
    //   76: invokevirtual sendToTarget : ()V
    //   79: aload_0
    //   80: monitorexit
    //   81: return
    //   82: aload_0
    //   83: getfield mDevice : Lapp/gamer/quadstellar/mode/ControlDevice;
    //   86: invokevirtual getXDevice : ()Lio/xlink/wifi/sdk/XDevice;
    //   89: invokevirtual getDevcieConnectStates : ()I
    //   92: iconst_3
    //   93: if_icmpeq -> 112
    //   96: invokestatic getInstance : ()Lapp/gamer/quadstellar/App;
    //   99: invokevirtual getCurrentDeviceMac : ()Ljava/lang/String;
    //   102: aload_1
    //   103: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   106: invokevirtual equals : (Ljava/lang/Object;)Z
    //   109: ifne -> 134
    //   112: invokestatic getInstance : ()Lapp/gamer/quadstellar/net/DeviceManager;
    //   115: invokestatic getInstance : ()Lapp/gamer/quadstellar/App;
    //   118: invokevirtual getCurrentActivity : ()Landroid/app/Activity;
    //   121: aload_1
    //   122: aload_0
    //   123: invokevirtual connectDevice : (Landroid/app/Activity;Lapp/gamer/quadstellar/mode/ControlDevice;Lapp/gamer/quadstellar/callback/IDeviceConnectCallback;)V
    //   126: goto -> 79
    //   129: astore_1
    //   130: aload_0
    //   131: monitorexit
    //   132: aload_1
    //   133: athrow
    //   134: aload_0
    //   135: getfield mHandler : Landroid/os/Handler;
    //   138: sipush #2222
    //   141: invokevirtual sendEmptyMessage : (I)Z
    //   144: pop
    //   145: goto -> 79
    // Exception table:
    //   from	to	target	type
    //   2	79	129	finally
    //   82	112	129	finally
    //   112	126	129	finally
    //   134	145	129	finally
  }
  
  public void sendTwinkleData(EFDeviceLight paramEFDeviceLight, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield light : Lapp/gamer/quadstellar/mode/EFDeviceLight;
    //   7: aload_0
    //   8: aconst_null
    //   9: putfield mSendCallback : Lapp/gamer/quadstellar/callback/ISendCallback;
    //   12: aload_0
    //   13: iload_2
    //   14: putfield requestCode : I
    //   17: aload_0
    //   18: getfield mHandler : Landroid/os/Handler;
    //   21: sipush #2221
    //   24: invokevirtual sendEmptyMessage : (I)Z
    //   27: pop
    //   28: aload_1
    //   29: ifnonnull -> 46
    //   32: aload_0
    //   33: getfield mHandler : Landroid/os/Handler;
    //   36: sipush #2224
    //   39: invokevirtual sendEmptyMessage : (I)Z
    //   42: pop
    //   43: aload_0
    //   44: monitorexit
    //   45: return
    //   46: aload_0
    //   47: invokestatic getInstance : ()Lapp/gamer/quadstellar/net/DeviceManager;
    //   50: aload_1
    //   51: invokevirtual getParentMac : ()Ljava/lang/String;
    //   54: invokevirtual getDevice : (Ljava/lang/String;)Lapp/gamer/quadstellar/mode/ControlDevice;
    //   57: putfield mDevice : Lapp/gamer/quadstellar/mode/ControlDevice;
    //   60: aload_0
    //   61: getfield mDevice : Lapp/gamer/quadstellar/mode/ControlDevice;
    //   64: ifnonnull -> 108
    //   67: aload_0
    //   68: getfield mHandler : Landroid/os/Handler;
    //   71: sipush #2224
    //   74: invokevirtual sendEmptyMessage : (I)Z
    //   77: pop
    //   78: aload_0
    //   79: getfield mHandler : Landroid/os/Handler;
    //   82: sipush #2225
    //   85: invokestatic getAppResources : ()Landroid/content/res/Resources;
    //   88: ldc_w 2131296975
    //   91: invokevirtual getString : (I)Ljava/lang/String;
    //   94: invokevirtual obtainMessage : (ILjava/lang/Object;)Landroid/os/Message;
    //   97: invokevirtual sendToTarget : ()V
    //   100: goto -> 43
    //   103: astore_1
    //   104: aload_0
    //   105: monitorexit
    //   106: aload_1
    //   107: athrow
    //   108: aload_0
    //   109: getfield mDevice : Lapp/gamer/quadstellar/mode/ControlDevice;
    //   112: invokevirtual getXDevice : ()Lio/xlink/wifi/sdk/XDevice;
    //   115: invokevirtual getDevcieConnectStates : ()I
    //   118: iconst_3
    //   119: if_icmpeq -> 141
    //   122: invokestatic getInstance : ()Lapp/gamer/quadstellar/App;
    //   125: invokevirtual getCurrentDeviceMac : ()Ljava/lang/String;
    //   128: aload_0
    //   129: getfield mDevice : Lapp/gamer/quadstellar/mode/ControlDevice;
    //   132: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   135: invokevirtual equals : (Ljava/lang/Object;)Z
    //   138: ifne -> 180
    //   141: invokestatic getInstance : ()Lapp/gamer/quadstellar/net/DeviceManager;
    //   144: astore_3
    //   145: invokestatic getInstance : ()Lapp/gamer/quadstellar/App;
    //   148: invokevirtual getCurrentActivity : ()Landroid/app/Activity;
    //   151: astore #4
    //   153: aload_0
    //   154: getfield mDevice : Lapp/gamer/quadstellar/mode/ControlDevice;
    //   157: astore #5
    //   159: new app/gamer/quadstellar/net/XlinkClient$2
    //   162: astore_1
    //   163: aload_1
    //   164: aload_0
    //   165: invokespecial <init> : (Lapp/gamer/quadstellar/net/XlinkClient;)V
    //   168: aload_3
    //   169: aload #4
    //   171: aload #5
    //   173: aload_1
    //   174: invokevirtual connectDevice : (Landroid/app/Activity;Lapp/gamer/quadstellar/mode/ControlDevice;Lapp/gamer/quadstellar/callback/IDeviceConnectCallback;)V
    //   177: goto -> 43
    //   180: aload_0
    //   181: getfield mHandler : Landroid/os/Handler;
    //   184: sipush #2227
    //   187: invokevirtual sendEmptyMessage : (I)Z
    //   190: pop
    //   191: goto -> 43
    // Exception table:
    //   from	to	target	type
    //   2	28	103	finally
    //   32	43	103	finally
    //   46	100	103	finally
    //   108	141	103	finally
    //   141	177	103	finally
    //   180	191	103	finally
  }
  
  class TwinkleWorker implements Runnable {
    public void run() {
      int i = XlinkClient.this.light.getDeviceState();
      byte[] arrayOfByte = ByteUtils.hexStringToBytes(XlinkClient.this.light.getDeviceMac());
      if (i == 0) {
        try {
          XlinkClient.access$802(XlinkClient.this, ByteUtils.append(50, new byte[][] { Prefix.CHANGE_COLOR_DEVICES, arrayOfByte, Prefix.COLOR_ON }));
          XlinkClient.this.send(false);
          Thread.sleep(500L);
          XlinkClient.access$802(XlinkClient.this, ByteUtils.append(50, new byte[][] { Prefix.CHANGE_COLOR_DEVICES, arrayOfByte, Prefix.COLOR_OFF }));
          XlinkClient.this.send(false);
          Thread.sleep(500L);
          XlinkClient.access$802(XlinkClient.this, ByteUtils.append(50, new byte[][] { Prefix.CHANGE_COLOR_DEVICES, arrayOfByte, Prefix.COLOR_ON }));
          XlinkClient.this.send(false);
          Thread.sleep(500L);
          XlinkClient.access$802(XlinkClient.this, ByteUtils.append(50, new byte[][] { Prefix.CHANGE_COLOR_DEVICES, arrayOfByte, Prefix.COLOR_OFF }));
          XlinkClient.this.send(false);
          Thread.sleep(500L);
          XlinkClient.access$802(XlinkClient.this, ByteUtils.append(50, new byte[][] { Prefix.CHANGE_COLOR_DEVICES, arrayOfByte, Prefix.COLOR_ON }));
          XlinkClient.this.send(false);
          Thread.sleep(500L);
          XlinkClient.access$802(XlinkClient.this, ByteUtils.append(50, new byte[][] { Prefix.CHANGE_COLOR_DEVICES, arrayOfByte, Prefix.COLOR_OFF }));
          XlinkClient.this.send(false);
        } catch (InterruptedException interruptedException) {
          interruptedException.printStackTrace();
        } 
        return;
      } 
      if (i == 1)
        try {
          XlinkClient.access$802(XlinkClient.this, ByteUtils.append(50, new byte[][] { Prefix.CHANGE_COLOR_DEVICES, (byte[])interruptedException, Prefix.COLOR_OFF }));
          XlinkClient.this.send(false);
          Thread.sleep(500L);
          XlinkClient.access$802(XlinkClient.this, ByteUtils.append(50, new byte[][] { Prefix.CHANGE_COLOR_DEVICES, (byte[])interruptedException, Prefix.COLOR_ON }));
          XlinkClient.this.send(false);
          Thread.sleep(500L);
          XlinkClient.access$802(XlinkClient.this, ByteUtils.append(50, new byte[][] { Prefix.CHANGE_COLOR_DEVICES, (byte[])interruptedException, Prefix.COLOR_OFF }));
          XlinkClient.this.send(false);
          Thread.sleep(500L);
          XlinkClient.access$802(XlinkClient.this, ByteUtils.append(50, new byte[][] { Prefix.CHANGE_COLOR_DEVICES, (byte[])interruptedException, Prefix.COLOR_ON }));
          XlinkClient.this.send(false);
          Thread.sleep(500L);
          XlinkClient.access$802(XlinkClient.this, ByteUtils.append(50, new byte[][] { Prefix.CHANGE_COLOR_DEVICES, (byte[])interruptedException, Prefix.COLOR_OFF }));
          XlinkClient.this.send(false);
          Thread.sleep(500L);
          XlinkClient.access$802(XlinkClient.this, ByteUtils.append(50, new byte[][] { Prefix.CHANGE_COLOR_DEVICES, (byte[])interruptedException, Prefix.COLOR_ON }));
          XlinkClient.this.send(false);
        } catch (InterruptedException interruptedException1) {
          interruptedException1.printStackTrace();
        }  
    }
  }
  
  class Worker implements Runnable {
    public void run() {
      XlinkClient.this.send(XlinkClient.this.mShowTip);
    }
  }
  
  class WorkerMulte implements Runnable {
    private byte[] bytes;
    
    private ControlDevice device;
    
    public WorkerMulte(ControlDevice param1ControlDevice, byte[] param1ArrayOfbyte) {
      this.device = param1ControlDevice;
      this.bytes = param1ArrayOfbyte;
    }
    
    public void run() {
      App.getInstance().setRequestCode(XlinkClient.this.requestCode);
      XlinkClient.this.send(XlinkClient.this.mShowTip, this.device, this.bytes);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/net/XlinkClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */