package app.gamer.quadstellar.net;

import android.content.Intent;
import android.support.annotation.NonNull;
import app.gamer.quadstellar.App;
import app.gamer.quadstellar.Prefix;
import app.gamer.quadstellar.db.FatherDeviceInfoDB;
import app.gamer.quadstellar.domain.DeviceStateEvent;
import app.gamer.quadstellar.domain.FatherDeviceInfo;
import app.gamer.quadstellar.utils.ByteUtils;
import app.gamer.quadstellar.utils.LogUtil;
import app.gamer.quadstellar.utils.XlinkUtils;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.greenrobot.eventbus.EventBus;

public class UdpClient implements Runnable {
  private static final int CALLBACK = 2221;
  
  private static final int LOCAL_PORT = 9432;
  
  public static final String REMOTE_IP = "255.255.255.255";
  
  private static final int REMOTE_PORT = 5876;
  
  private static final String TAG = "UdpClient";
  
  private static final int UPDATE_DATA = 2220;
  
  private static UdpClient client;
  
  private ReceiveCallback callback;
  
  private String deviceIP;
  
  private boolean isThreadDisable = false;
  
  private Object object = new Object();
  
  private byte[] rBuffer = new byte[200];
  
  private DatagramPacket rPacket = null;
  
  private DatagramPacket rPacket2 = null;
  
  private DatagramSocket rUdp = null;
  
  private int requestCode = -1;
  
  private Thread rthread = null;
  
  private DatagramPacket sPacket = null;
  
  private DatagramSocket sUdp = null;
  
  private Runnable sthread = null;
  
  Timer timer = new Timer();
  
  private TimerTask timerTask = new TimerTask() {
      public void run() {
        if (App.heartDeviceTime.size() == 0) {
          List list = FatherDeviceInfoDB.getInstance().queryUserList();
          if (list != null) {
            for (FatherDeviceInfo fatherDeviceInfo : list) {
              fatherDeviceInfo.setIsOnline(false);
              FatherDeviceInfoDB.getInstance().updateUser(fatherDeviceInfo);
            } 
            EventBus.getDefault().post(new DeviceStateEvent());
          } 
        } 
        for (Map.Entry entry : App.heartDeviceTime.entrySet()) {
          FatherDeviceInfo fatherDeviceInfo = FatherDeviceInfoDB.getInstance().queryUserList((String)entry.getKey());
          if (fatherDeviceInfo != null) {
            if (System.currentTimeMillis() - ((Long)entry.getValue()).longValue() > 60000L) {
              fatherDeviceInfo.setIsOnline(false);
            } else {
              fatherDeviceInfo.setIsOnline(true);
            } 
            FatherDeviceInfoDB.getInstance().updateUser(fatherDeviceInfo);
            EventBus.getDefault().post(new DeviceStateEvent());
          } 
        } 
        UdpClient.this.sendData(-1, "255.255.255.255", ByteUtils.sendUDPHeartData(), null);
      }
    };
  
  private byte[] upDataSuccessTag;
  
  private UdpClient() {
    this.timer.schedule(this.timerTask, 60000L, 60000L);
  }
  
  public static UdpClient getInstance() {
    // Byte code:
    //   0: ldc app/gamer/quadstellar/net/UdpClient
    //   2: monitorenter
    //   3: getstatic app/gamer/quadstellar/net/UdpClient.client : Lapp/gamer/quadstellar/net/UdpClient;
    //   6: ifnonnull -> 33
    //   9: ldc app/gamer/quadstellar/net/UdpClient
    //   11: monitorenter
    //   12: getstatic app/gamer/quadstellar/net/UdpClient.client : Lapp/gamer/quadstellar/net/UdpClient;
    //   15: ifnonnull -> 30
    //   18: new app/gamer/quadstellar/net/UdpClient
    //   21: astore_0
    //   22: aload_0
    //   23: invokespecial <init> : ()V
    //   26: aload_0
    //   27: putstatic app/gamer/quadstellar/net/UdpClient.client : Lapp/gamer/quadstellar/net/UdpClient;
    //   30: ldc app/gamer/quadstellar/net/UdpClient
    //   32: monitorexit
    //   33: ldc app/gamer/quadstellar/net/UdpClient
    //   35: monitorexit
    //   36: getstatic app/gamer/quadstellar/net/UdpClient.client : Lapp/gamer/quadstellar/net/UdpClient;
    //   39: areturn
    //   40: astore_0
    //   41: ldc app/gamer/quadstellar/net/UdpClient
    //   43: monitorexit
    //   44: aload_0
    //   45: athrow
    //   46: astore_0
    //   47: ldc app/gamer/quadstellar/net/UdpClient
    //   49: monitorexit
    //   50: aload_0
    //   51: athrow
    // Exception table:
    //   from	to	target	type
    //   3	12	46	finally
    //   12	30	40	finally
    //   30	33	40	finally
    //   33	36	46	finally
    //   41	44	40	finally
    //   44	46	46	finally
    //   47	50	46	finally
  }
  
  private void receiveData(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte != null && paramArrayOfbyte[0] == -16 && paramArrayOfbyte[2] == 100) {
      String str = ByteUtils.bytesToHexString(ByteUtils.arrayCopyBytes(paramArrayOfbyte, 7, 16)).trim().toLowerCase();
      if (str.equals(App.uniqueId) || str.equals(Prefix.ALL_FF)) {
        String str1 = ByteUtils.bytesToHexString(ByteUtils.arrayCopyBytes(paramArrayOfbyte, 27, 6));
        App.heartDeviceTime.put(str1, Long.valueOf(System.currentTimeMillis()));
        FatherDeviceInfo fatherDeviceInfo = FatherDeviceInfoDB.getInstance().queryUserList(str1);
        if (fatherDeviceInfo != null) {
          fatherDeviceInfo.setIsOnline(true);
          FatherDeviceInfoDB.getInstance().updateUser(fatherDeviceInfo);
          EventBus.getDefault().post(new DeviceStateEvent());
        } 
      } 
    } 
  }
  
  private void recvData(@NonNull DatagramPacket paramDatagramPacket) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield rPacket2 : Ljava/net/DatagramPacket;
    //   6: invokevirtual getData : ()[B
    //   9: invokevirtual clone : ()Ljava/lang/Object;
    //   12: checkcast [B
    //   15: astore_2
    //   16: aload_0
    //   17: getfield object : Ljava/lang/Object;
    //   20: astore_3
    //   21: aload_3
    //   22: monitorenter
    //   23: aload_0
    //   24: getfield rPacket2 : Ljava/net/DatagramPacket;
    //   27: invokevirtual getLength : ()I
    //   30: bipush #7
    //   32: if_icmple -> 166
    //   35: aload_2
    //   36: ifnull -> 166
    //   39: aload_2
    //   40: iconst_2
    //   41: baload
    //   42: bipush #100
    //   44: if_icmpne -> 166
    //   47: new java/lang/StringBuilder
    //   50: astore #4
    //   52: aload #4
    //   54: invokespecial <init> : ()V
    //   57: ldc 'UdpClient'
    //   59: aload #4
    //   61: ldc '接收数据'
    //   63: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   66: aload_2
    //   67: invokestatic getHexBinString : ([B)Ljava/lang/String;
    //   70: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   73: invokevirtual toString : ()Ljava/lang/String;
    //   76: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)V
    //   79: aload_1
    //   80: invokevirtual getAddress : ()Ljava/net/InetAddress;
    //   83: ifnull -> 114
    //   86: aload_0
    //   87: aload_1
    //   88: invokevirtual getAddress : ()Ljava/net/InetAddress;
    //   91: invokevirtual getHostAddress : ()Ljava/lang/String;
    //   94: putfield deviceIP : Ljava/lang/String;
    //   97: aload_2
    //   98: iconst_2
    //   99: baload
    //   100: istore #5
    //   102: iload #5
    //   104: bipush #100
    //   106: if_icmpeq -> 181
    //   109: aload_3
    //   110: monitorexit
    //   111: aload_0
    //   112: monitorexit
    //   113: return
    //   114: aload_0
    //   115: aload_2
    //   116: bipush #34
    //   118: iconst_4
    //   119: invokestatic arrayCopyBytes : ([BII)[B
    //   122: invokestatic getDeviecIP : ([B)Ljava/lang/String;
    //   125: putfield deviceIP : Ljava/lang/String;
    //   128: goto -> 97
    //   131: astore_1
    //   132: new java/lang/StringBuilder
    //   135: astore_2
    //   136: aload_2
    //   137: invokespecial <init> : ()V
    //   140: ldc 'UdpClient'
    //   142: aload_2
    //   143: ldc_w 'recvdata error:'
    //   146: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   149: aload_1
    //   150: invokevirtual getMessage : ()Ljava/lang/String;
    //   153: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   156: invokevirtual toString : ()Ljava/lang/String;
    //   159: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)V
    //   162: aload_0
    //   163: invokevirtual disConnectSocket : ()V
    //   166: aload_3
    //   167: monitorexit
    //   168: goto -> 111
    //   171: astore_1
    //   172: aload_3
    //   173: monitorexit
    //   174: aload_1
    //   175: athrow
    //   176: astore_1
    //   177: aload_0
    //   178: monitorexit
    //   179: aload_1
    //   180: athrow
    //   181: aload_2
    //   182: iconst_0
    //   183: baload
    //   184: iconst_m1
    //   185: if_icmpne -> 236
    //   188: aload_2
    //   189: bipush #27
    //   191: bipush #6
    //   193: invokestatic arrayCopyBytes : ([BII)[B
    //   196: invokestatic bytesToHexString : ([B)Ljava/lang/String;
    //   199: astore_1
    //   200: ldc 'UdpClient'
    //   202: aload_1
    //   203: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   206: pop
    //   207: aload_1
    //   208: ifnull -> 166
    //   211: invokestatic getInstance : ()Lapp/gamer/quadstellar/db/FatherDeviceInfoDB;
    //   214: aload_1
    //   215: invokevirtual queryUserList : (Ljava/lang/String;)Lapp/gamer/quadstellar/domain/FatherDeviceInfo;
    //   218: ifnull -> 166
    //   221: aload_0
    //   222: getstatic app/gamer/quadstellar/Config.BROADCAST_RECVPIPE : Ljava/lang/String;
    //   225: aload_1
    //   226: aload_2
    //   227: iconst_0
    //   228: baload
    //   229: aload_2
    //   230: invokevirtual sendPipeBroad : (Ljava/lang/String;Ljava/lang/String;I[B)V
    //   233: goto -> 166
    //   236: aload_2
    //   237: iconst_0
    //   238: baload
    //   239: iconst_1
    //   240: if_icmpne -> 1070
    //   243: aload_2
    //   244: iconst_2
    //   245: baload
    //   246: bipush #100
    //   248: if_icmpne -> 1070
    //   251: aload_2
    //   252: bipush #7
    //   254: bipush #16
    //   256: invokestatic arrayCopyBytes : ([BII)[B
    //   259: invokestatic bytesToHexString : ([B)Ljava/lang/String;
    //   262: invokevirtual trim : ()Ljava/lang/String;
    //   265: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   268: astore #6
    //   270: aload #6
    //   272: getstatic app/gamer/quadstellar/App.uniqueId : Ljava/lang/String;
    //   275: invokevirtual equals : (Ljava/lang/Object;)Z
    //   278: istore #7
    //   280: iload #7
    //   282: ifne -> 290
    //   285: aload_3
    //   286: monitorexit
    //   287: goto -> 111
    //   290: aload_2
    //   291: bipush #27
    //   293: bipush #6
    //   295: invokestatic arrayCopyBytes : ([BII)[B
    //   298: invokestatic bytesToHexString : ([B)Ljava/lang/String;
    //   301: astore #4
    //   303: getstatic app/gamer/quadstellar/App.heartDeviceTime : Ljava/util/HashMap;
    //   306: aload #4
    //   308: invokestatic currentTimeMillis : ()J
    //   311: invokestatic valueOf : (J)Ljava/lang/Long;
    //   314: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   317: pop
    //   318: invokestatic getInstance : ()Lapp/gamer/quadstellar/db/FatherDeviceInfoDB;
    //   321: aload #4
    //   323: invokevirtual queryUserList : (Ljava/lang/String;)Lapp/gamer/quadstellar/domain/FatherDeviceInfo;
    //   326: astore_1
    //   327: aload_1
    //   328: ifnull -> 704
    //   331: aload_1
    //   332: aload_2
    //   333: bipush #33
    //   335: baload
    //   336: invokevirtual setDeviceLen : (I)V
    //   339: new java/lang/StringBuilder
    //   342: astore #8
    //   344: aload #8
    //   346: invokespecial <init> : ()V
    //   349: aload #8
    //   351: aload_2
    //   352: bipush #34
    //   354: baload
    //   355: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   358: aload_2
    //   359: bipush #35
    //   361: baload
    //   362: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   365: aload_2
    //   366: bipush #36
    //   368: baload
    //   369: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   372: pop
    //   373: aload #8
    //   375: invokevirtual toString : ()Ljava/lang/String;
    //   378: invokevirtual trim : ()Ljava/lang/String;
    //   381: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   384: astore #8
    //   386: aload_1
    //   387: aload #4
    //   389: invokevirtual setMacAdrass : (Ljava/lang/String;)V
    //   392: aload_1
    //   393: aload #6
    //   395: invokevirtual setUuid : (Ljava/lang/String;)V
    //   398: new java/lang/StringBuilder
    //   401: astore #6
    //   403: aload #6
    //   405: invokespecial <init> : ()V
    //   408: aload_1
    //   409: aload #6
    //   411: aload_2
    //   412: bipush #39
    //   414: baload
    //   415: i2d
    //   416: aload_2
    //   417: bipush #40
    //   419: baload
    //   420: i2d
    //   421: ldc2_w 10.0
    //   424: ddiv
    //   425: dadd
    //   426: invokevirtual append : (D)Ljava/lang/StringBuilder;
    //   429: ldc_w '℃'
    //   432: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   435: invokevirtual toString : ()Ljava/lang/String;
    //   438: invokevirtual setTemp : (Ljava/lang/String;)V
    //   441: aload_1
    //   442: aload_2
    //   443: bipush #41
    //   445: baload
    //   446: invokevirtual setDoorToggleState : (I)V
    //   449: aload_1
    //   450: aload_2
    //   451: bipush #53
    //   453: baload
    //   454: invokevirtual setToggleState : (I)V
    //   457: aload_1
    //   458: aload_0
    //   459: getfield deviceIP : Ljava/lang/String;
    //   462: invokevirtual setDeviceIP : (Ljava/lang/String;)V
    //   465: aload_1
    //   466: aload #8
    //   468: invokevirtual setDeviceVersion : (Ljava/lang/String;)V
    //   471: aload_1
    //   472: aload_2
    //   473: bipush #42
    //   475: baload
    //   476: invokevirtual setFanCount : (I)V
    //   479: iconst_0
    //   480: istore #5
    //   482: iload #5
    //   484: aload_1
    //   485: invokevirtual getFanCount : ()I
    //   488: if_icmpge -> 568
    //   491: invokestatic getInstance : ()Lapp/gamer/quadstellar/db/FanDeviceInfoDB;
    //   494: aload #4
    //   496: iload #5
    //   498: invokevirtual queryUserList : (Ljava/lang/String;I)Lapp/gamer/quadstellar/domain/FanDeviceInfo;
    //   501: astore #6
    //   503: aload #6
    //   505: ifnull -> 562
    //   508: aload #6
    //   510: aload #4
    //   512: invokevirtual setMacAdrass : (Ljava/lang/String;)V
    //   515: aload #6
    //   517: aload_2
    //   518: iload #5
    //   520: iconst_5
    //   521: imul
    //   522: bipush #43
    //   524: iadd
    //   525: baload
    //   526: invokevirtual setSonFanNumber : (I)V
    //   529: aload #6
    //   531: aload_2
    //   532: iload #5
    //   534: iconst_5
    //   535: imul
    //   536: bipush #46
    //   538: iadd
    //   539: iconst_2
    //   540: invokestatic arrayCopyBytes : ([BII)[B
    //   543: invokestatic bytesToHexString : ([B)Ljava/lang/String;
    //   546: bipush #16
    //   548: invokestatic parseInt : (Ljava/lang/String;I)I
    //   551: invokevirtual setRealitySonFanSpeed : (I)V
    //   554: invokestatic getInstance : ()Lapp/gamer/quadstellar/db/FanDeviceInfoDB;
    //   557: aload #6
    //   559: invokevirtual updateUser : (Lapp/gamer/quadstellar/domain/FanDeviceInfo;)V
    //   562: iinc #5, 1
    //   565: goto -> 482
    //   568: aload_1
    //   569: aload_2
    //   570: bipush #55
    //   572: baload
    //   573: invokevirtual setLightCount : (I)V
    //   576: aload_1
    //   577: aload_2
    //   578: bipush #62
    //   580: baload
    //   581: invokevirtual setFanSpeed : (I)V
    //   584: iconst_0
    //   585: istore #5
    //   587: iload #5
    //   589: aload_1
    //   590: invokevirtual getLightCount : ()I
    //   593: if_icmpge -> 659
    //   596: invokestatic getInstance : ()Lapp/gamer/quadstellar/db/LightDeviceInfoDB;
    //   599: aload #4
    //   601: iload #5
    //   603: invokevirtual queryUserList : (Ljava/lang/String;I)Lapp/gamer/quadstellar/domain/LightDeviceInfo;
    //   606: astore #6
    //   608: aload #6
    //   610: aload #4
    //   612: invokevirtual setMacAdrass : (Ljava/lang/String;)V
    //   615: aload #6
    //   617: aload_2
    //   618: iload #5
    //   620: bipush #9
    //   622: imul
    //   623: bipush #56
    //   625: iadd
    //   626: baload
    //   627: invokevirtual setSonLightNumber : (I)V
    //   630: aload #6
    //   632: aload_2
    //   633: iload #5
    //   635: bipush #9
    //   637: imul
    //   638: bipush #57
    //   640: iadd
    //   641: baload
    //   642: invokevirtual setSonLightState : (I)V
    //   645: invokestatic getInstance : ()Lapp/gamer/quadstellar/db/LightDeviceInfoDB;
    //   648: aload #6
    //   650: invokevirtual updateUser : (Lapp/gamer/quadstellar/domain/LightDeviceInfo;)V
    //   653: iinc #5, 1
    //   656: goto -> 587
    //   659: aload_1
    //   660: iconst_1
    //   661: invokevirtual setIsOnline : (Z)V
    //   664: invokestatic getInstance : ()Lapp/gamer/quadstellar/db/FatherDeviceInfoDB;
    //   667: aload_1
    //   668: invokevirtual updateUser : (Lapp/gamer/quadstellar/domain/FatherDeviceInfo;)V
    //   671: new android/content/Intent
    //   674: astore_2
    //   675: aload_2
    //   676: getstatic app/gamer/quadstellar/Config.BROADCAST_RECVPIPE_SCAN : Ljava/lang/String;
    //   679: invokespecial <init> : (Ljava/lang/String;)V
    //   682: aload_2
    //   683: ldc_w 'mac'
    //   686: aload_1
    //   687: invokevirtual getMacAdrass : ()Ljava/lang/String;
    //   690: invokevirtual putExtra : (Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;
    //   693: pop
    //   694: invokestatic getAppContext : ()Landroid/content/Context;
    //   697: aload_2
    //   698: invokevirtual sendBroadcast : (Landroid/content/Intent;)V
    //   701: goto -> 166
    //   704: new app/gamer/quadstellar/domain/FatherDeviceInfo
    //   707: astore_1
    //   708: aload_1
    //   709: invokespecial <init> : ()V
    //   712: aload_1
    //   713: aload_2
    //   714: bipush #33
    //   716: baload
    //   717: invokevirtual setDeviceLen : (I)V
    //   720: new java/lang/StringBuilder
    //   723: astore #8
    //   725: aload #8
    //   727: invokespecial <init> : ()V
    //   730: aload #8
    //   732: aload_2
    //   733: bipush #34
    //   735: baload
    //   736: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   739: aload_2
    //   740: bipush #35
    //   742: baload
    //   743: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   746: aload_2
    //   747: bipush #36
    //   749: baload
    //   750: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   753: pop
    //   754: aload #8
    //   756: invokevirtual toString : ()Ljava/lang/String;
    //   759: invokevirtual trim : ()Ljava/lang/String;
    //   762: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   765: astore #8
    //   767: aload_1
    //   768: aload #4
    //   770: invokevirtual setMacAdrass : (Ljava/lang/String;)V
    //   773: aload_1
    //   774: aload #6
    //   776: invokevirtual setUuid : (Ljava/lang/String;)V
    //   779: new java/lang/StringBuilder
    //   782: astore #6
    //   784: aload #6
    //   786: invokespecial <init> : ()V
    //   789: aload_1
    //   790: aload #6
    //   792: aload_2
    //   793: bipush #39
    //   795: baload
    //   796: i2d
    //   797: aload_2
    //   798: bipush #40
    //   800: baload
    //   801: i2d
    //   802: ldc2_w 10.0
    //   805: ddiv
    //   806: dadd
    //   807: invokevirtual append : (D)Ljava/lang/StringBuilder;
    //   810: ldc_w '℃'
    //   813: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   816: invokevirtual toString : ()Ljava/lang/String;
    //   819: invokevirtual setTemp : (Ljava/lang/String;)V
    //   822: aload_1
    //   823: aload_2
    //   824: bipush #41
    //   826: baload
    //   827: invokevirtual setDoorToggleState : (I)V
    //   830: aload_1
    //   831: aload_2
    //   832: bipush #53
    //   834: baload
    //   835: invokevirtual setToggleState : (I)V
    //   838: aload_1
    //   839: aload_0
    //   840: getfield deviceIP : Ljava/lang/String;
    //   843: invokevirtual setDeviceIP : (Ljava/lang/String;)V
    //   846: aload_1
    //   847: aload #8
    //   849: invokevirtual setDeviceVersion : (Ljava/lang/String;)V
    //   852: aload_1
    //   853: aload_2
    //   854: bipush #42
    //   856: baload
    //   857: invokevirtual setFanCount : (I)V
    //   860: aload_1
    //   861: invokevirtual getFanCount : ()I
    //   864: istore #5
    //   866: iload #5
    //   868: ifne -> 876
    //   871: aload_3
    //   872: monitorexit
    //   873: goto -> 111
    //   876: iconst_0
    //   877: istore #5
    //   879: iload #5
    //   881: aload_1
    //   882: invokevirtual getFanCount : ()I
    //   885: if_icmpge -> 958
    //   888: new app/gamer/quadstellar/domain/FanDeviceInfo
    //   891: astore #6
    //   893: aload #6
    //   895: invokespecial <init> : ()V
    //   898: aload #6
    //   900: aload #4
    //   902: invokevirtual setMacAdrass : (Ljava/lang/String;)V
    //   905: aload #6
    //   907: aload_2
    //   908: iload #5
    //   910: iconst_5
    //   911: imul
    //   912: bipush #43
    //   914: iadd
    //   915: baload
    //   916: invokevirtual setSonFanNumber : (I)V
    //   919: aload #6
    //   921: aload_2
    //   922: iload #5
    //   924: iconst_5
    //   925: imul
    //   926: bipush #46
    //   928: iadd
    //   929: iconst_2
    //   930: invokestatic arrayCopyBytes : ([BII)[B
    //   933: invokestatic bytesToHexString : ([B)Ljava/lang/String;
    //   936: bipush #16
    //   938: invokestatic parseInt : (Ljava/lang/String;I)I
    //   941: invokevirtual setRealitySonFanSpeed : (I)V
    //   944: invokestatic getInstance : ()Lapp/gamer/quadstellar/db/FanDeviceInfoDB;
    //   947: aload #6
    //   949: invokevirtual insertUser : (Lapp/gamer/quadstellar/domain/FanDeviceInfo;)V
    //   952: iinc #5, 1
    //   955: goto -> 879
    //   958: aload_1
    //   959: aload_2
    //   960: bipush #55
    //   962: baload
    //   963: invokevirtual setLightCount : (I)V
    //   966: aload_1
    //   967: invokevirtual getLightCount : ()I
    //   970: istore #5
    //   972: iload #5
    //   974: ifne -> 982
    //   977: aload_3
    //   978: monitorexit
    //   979: goto -> 111
    //   982: iconst_0
    //   983: istore #5
    //   985: iload #5
    //   987: aload_1
    //   988: invokevirtual getLightCount : ()I
    //   991: if_icmpge -> 1055
    //   994: new app/gamer/quadstellar/domain/LightDeviceInfo
    //   997: astore #6
    //   999: aload #6
    //   1001: invokespecial <init> : ()V
    //   1004: aload #6
    //   1006: aload #4
    //   1008: invokevirtual setMacAdrass : (Ljava/lang/String;)V
    //   1011: aload #6
    //   1013: aload_2
    //   1014: iload #5
    //   1016: bipush #9
    //   1018: imul
    //   1019: bipush #56
    //   1021: iadd
    //   1022: baload
    //   1023: invokevirtual setSonLightNumber : (I)V
    //   1026: aload #6
    //   1028: aload_2
    //   1029: iload #5
    //   1031: bipush #9
    //   1033: imul
    //   1034: bipush #57
    //   1036: iadd
    //   1037: baload
    //   1038: invokevirtual setSonLightState : (I)V
    //   1041: invokestatic getInstance : ()Lapp/gamer/quadstellar/db/LightDeviceInfoDB;
    //   1044: aload #6
    //   1046: invokevirtual insertUser : (Lapp/gamer/quadstellar/domain/LightDeviceInfo;)V
    //   1049: iinc #5, 1
    //   1052: goto -> 985
    //   1055: aload_1
    //   1056: iconst_1
    //   1057: invokevirtual setIsOnline : (Z)V
    //   1060: invokestatic getInstance : ()Lapp/gamer/quadstellar/db/FatherDeviceInfoDB;
    //   1063: aload_1
    //   1064: invokevirtual insertUser : (Lapp/gamer/quadstellar/domain/FatherDeviceInfo;)V
    //   1067: goto -> 671
    //   1070: aload_2
    //   1071: iconst_0
    //   1072: baload
    //   1073: bipush #12
    //   1075: if_icmpne -> 1494
    //   1078: aload_2
    //   1079: iconst_2
    //   1080: baload
    //   1081: bipush #100
    //   1083: if_icmpne -> 1494
    //   1086: aload_0
    //   1087: getfield upDataSuccessTag : [B
    //   1090: ifnull -> 1118
    //   1093: aload_2
    //   1094: iconst_3
    //   1095: iconst_4
    //   1096: invokestatic arrayCopyBytes : ([BII)[B
    //   1099: aload_0
    //   1100: getfield upDataSuccessTag : [B
    //   1103: invokestatic isSameTime : ([B[B)Z
    //   1106: istore #7
    //   1108: iload #7
    //   1110: ifeq -> 1118
    //   1113: aload_3
    //   1114: monitorexit
    //   1115: goto -> 111
    //   1118: aload_0
    //   1119: aload_2
    //   1120: iconst_3
    //   1121: iconst_4
    //   1122: invokestatic arrayCopyBytes : ([BII)[B
    //   1125: putfield upDataSuccessTag : [B
    //   1128: aload_2
    //   1129: bipush #27
    //   1131: bipush #6
    //   1133: invokestatic arrayCopyBytes : ([BII)[B
    //   1136: invokestatic bytesToHexString : ([B)Ljava/lang/String;
    //   1139: astore #4
    //   1141: invokestatic getInstance : ()Lapp/gamer/quadstellar/db/FatherDeviceInfoDB;
    //   1144: aload #4
    //   1146: invokevirtual queryUserList : (Ljava/lang/String;)Lapp/gamer/quadstellar/domain/FatherDeviceInfo;
    //   1149: astore_1
    //   1150: aload_1
    //   1151: ifnull -> 1192
    //   1154: aload_2
    //   1155: bipush #34
    //   1157: baload
    //   1158: istore #5
    //   1160: iload #5
    //   1162: tableswitch default -> 1192, 0 -> 1192, 1 -> 1192, 2 -> 1197, 3 -> 1417
    //   1192: aload_3
    //   1193: monitorexit
    //   1194: goto -> 111
    //   1197: invokestatic getInstance : ()Lapp/gamer/quadstellar/db/FanDeviceInfoDB;
    //   1200: aload #4
    //   1202: invokevirtual queryUserList : (Ljava/lang/String;)Ljava/util/List;
    //   1205: astore #4
    //   1207: aload #4
    //   1209: iconst_0
    //   1210: invokeinterface get : (I)Ljava/lang/Object;
    //   1215: checkcast app/gamer/quadstellar/domain/FanDeviceInfo
    //   1218: aload_2
    //   1219: bipush #39
    //   1221: iconst_2
    //   1222: invokestatic arrayCopyBytes : ([BII)[B
    //   1225: invokestatic bytesToHexString : ([B)Ljava/lang/String;
    //   1228: bipush #16
    //   1230: invokestatic parseInt : (Ljava/lang/String;I)I
    //   1233: invokevirtual setRealitySonFanSpeed : (I)V
    //   1236: aload #4
    //   1238: iconst_1
    //   1239: invokeinterface get : (I)Ljava/lang/Object;
    //   1244: checkcast app/gamer/quadstellar/domain/FanDeviceInfo
    //   1247: aload_2
    //   1248: bipush #44
    //   1250: iconst_2
    //   1251: invokestatic arrayCopyBytes : ([BII)[B
    //   1254: invokestatic bytesToHexString : ([B)Ljava/lang/String;
    //   1257: bipush #16
    //   1259: invokestatic parseInt : (Ljava/lang/String;I)I
    //   1262: invokevirtual setRealitySonFanSpeed : (I)V
    //   1265: invokestatic getInstance : ()Lapp/gamer/quadstellar/db/FanDeviceInfoDB;
    //   1268: aload #4
    //   1270: iconst_0
    //   1271: invokeinterface get : (I)Ljava/lang/Object;
    //   1276: checkcast app/gamer/quadstellar/domain/FanDeviceInfo
    //   1279: invokevirtual updateUser : (Lapp/gamer/quadstellar/domain/FanDeviceInfo;)V
    //   1282: invokestatic getInstance : ()Lapp/gamer/quadstellar/db/FanDeviceInfoDB;
    //   1285: aload #4
    //   1287: iconst_1
    //   1288: invokeinterface get : (I)Ljava/lang/Object;
    //   1293: checkcast app/gamer/quadstellar/domain/FanDeviceInfo
    //   1296: invokevirtual updateUser : (Lapp/gamer/quadstellar/domain/FanDeviceInfo;)V
    //   1299: new java/lang/StringBuilder
    //   1302: astore_2
    //   1303: aload_2
    //   1304: invokespecial <init> : ()V
    //   1307: ldc_w 'Udp'
    //   1310: aload_2
    //   1311: ldc_w '进风'
    //   1314: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1317: aload #4
    //   1319: iconst_0
    //   1320: invokeinterface get : (I)Ljava/lang/Object;
    //   1325: checkcast app/gamer/quadstellar/domain/FanDeviceInfo
    //   1328: invokevirtual getRealitySonFanSpeed : ()I
    //   1331: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1334: ldc_w '出风'
    //   1337: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1340: aload #4
    //   1342: iconst_1
    //   1343: invokeinterface get : (I)Ljava/lang/Object;
    //   1348: checkcast app/gamer/quadstellar/domain/FanDeviceInfo
    //   1351: invokevirtual getRealitySonFanSpeed : ()I
    //   1354: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1357: invokevirtual toString : ()Ljava/lang/String;
    //   1360: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)V
    //   1363: invokestatic getDefault : ()Lorg/greenrobot/eventbus/EventBus;
    //   1366: astore #6
    //   1368: new app/gamer/quadstellar/domain/RefreshDataEvent
    //   1371: astore_2
    //   1372: aload_2
    //   1373: aload #4
    //   1375: iconst_0
    //   1376: invokeinterface get : (I)Ljava/lang/Object;
    //   1381: checkcast app/gamer/quadstellar/domain/FanDeviceInfo
    //   1384: invokevirtual getRealitySonFanSpeed : ()I
    //   1387: aload #4
    //   1389: iconst_1
    //   1390: invokeinterface get : (I)Ljava/lang/Object;
    //   1395: checkcast app/gamer/quadstellar/domain/FanDeviceInfo
    //   1398: invokevirtual getRealitySonFanSpeed : ()I
    //   1401: aload_1
    //   1402: invokevirtual getTemp : ()Ljava/lang/String;
    //   1405: invokespecial <init> : (IILjava/lang/String;)V
    //   1408: aload #6
    //   1410: aload_2
    //   1411: invokevirtual post : (Ljava/lang/Object;)V
    //   1414: goto -> 1192
    //   1417: new java/lang/StringBuilder
    //   1420: astore #4
    //   1422: aload #4
    //   1424: invokespecial <init> : ()V
    //   1427: aload_1
    //   1428: aload #4
    //   1430: aload_2
    //   1431: bipush #35
    //   1433: baload
    //   1434: i2d
    //   1435: aload_2
    //   1436: bipush #36
    //   1438: baload
    //   1439: i2d
    //   1440: ldc2_w 10.0
    //   1443: ddiv
    //   1444: dadd
    //   1445: invokevirtual append : (D)Ljava/lang/StringBuilder;
    //   1448: ldc_w ''
    //   1451: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1454: invokevirtual toString : ()Ljava/lang/String;
    //   1457: invokevirtual setTemp : (Ljava/lang/String;)V
    //   1460: invokestatic getInstance : ()Lapp/gamer/quadstellar/db/FatherDeviceInfoDB;
    //   1463: aload_1
    //   1464: invokevirtual updateUser : (Lapp/gamer/quadstellar/domain/FatherDeviceInfo;)V
    //   1467: invokestatic getDefault : ()Lorg/greenrobot/eventbus/EventBus;
    //   1470: astore_2
    //   1471: new app/gamer/quadstellar/domain/RefreshTempDataEvent
    //   1474: astore #4
    //   1476: aload #4
    //   1478: aload_1
    //   1479: invokevirtual getTemp : ()Ljava/lang/String;
    //   1482: invokespecial <init> : (Ljava/lang/String;)V
    //   1485: aload_2
    //   1486: aload #4
    //   1488: invokevirtual post : (Ljava/lang/Object;)V
    //   1491: goto -> 1192
    //   1494: aload_2
    //   1495: bipush #7
    //   1497: bipush #16
    //   1499: invokestatic arrayCopyBytes : ([BII)[B
    //   1502: invokestatic bytesToHexString : ([B)Ljava/lang/String;
    //   1505: invokevirtual trim : ()Ljava/lang/String;
    //   1508: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   1511: astore_1
    //   1512: aload_1
    //   1513: getstatic app/gamer/quadstellar/App.uniqueId : Ljava/lang/String;
    //   1516: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1519: ifne -> 1532
    //   1522: aload_1
    //   1523: getstatic app/gamer/quadstellar/Prefix.ALL_FF : Ljava/lang/String;
    //   1526: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1529: ifeq -> 166
    //   1532: aload_2
    //   1533: bipush #27
    //   1535: bipush #6
    //   1537: invokestatic arrayCopyBytes : ([BII)[B
    //   1540: invokestatic bytesToHexString : ([B)Ljava/lang/String;
    //   1543: astore #4
    //   1545: getstatic app/gamer/quadstellar/App.heartDeviceTime : Ljava/util/HashMap;
    //   1548: aload #4
    //   1550: invokestatic currentTimeMillis : ()J
    //   1553: invokestatic valueOf : (J)Ljava/lang/Long;
    //   1556: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1559: pop
    //   1560: invokestatic getInstance : ()Lapp/gamer/quadstellar/db/FatherDeviceInfoDB;
    //   1563: aload #4
    //   1565: invokevirtual queryUserList : (Ljava/lang/String;)Lapp/gamer/quadstellar/domain/FatherDeviceInfo;
    //   1568: astore #6
    //   1570: aload #6
    //   1572: ifnull -> 1608
    //   1575: aload #6
    //   1577: iconst_1
    //   1578: invokevirtual setIsOnline : (Z)V
    //   1581: invokestatic getInstance : ()Lapp/gamer/quadstellar/db/FatherDeviceInfoDB;
    //   1584: aload #6
    //   1586: invokevirtual updateUser : (Lapp/gamer/quadstellar/domain/FatherDeviceInfo;)V
    //   1589: invokestatic getDefault : ()Lorg/greenrobot/eventbus/EventBus;
    //   1592: astore #8
    //   1594: new app/gamer/quadstellar/domain/DeviceStateEvent
    //   1597: astore_1
    //   1598: aload_1
    //   1599: invokespecial <init> : ()V
    //   1602: aload #8
    //   1604: aload_1
    //   1605: invokevirtual post : (Ljava/lang/Object;)V
    //   1608: aload_2
    //   1609: iconst_0
    //   1610: baload
    //   1611: bipush #-16
    //   1613: if_icmpne -> 1671
    //   1616: aload_2
    //   1617: bipush #33
    //   1619: baload
    //   1620: istore #5
    //   1622: iload #5
    //   1624: iconst_2
    //   1625: if_icmpne -> 1633
    //   1628: aload_3
    //   1629: monitorexit
    //   1630: goto -> 111
    //   1633: aload #6
    //   1635: ifnull -> 1666
    //   1638: aload #6
    //   1640: aload_0
    //   1641: getfield deviceIP : Ljava/lang/String;
    //   1644: invokevirtual setDeviceIP : (Ljava/lang/String;)V
    //   1647: aload_2
    //   1648: iconst_0
    //   1649: bipush #60
    //   1651: invokestatic arrayCopyBytes : ([BII)[B
    //   1654: astore_1
    //   1655: aload_0
    //   1656: iconst_m1
    //   1657: aload_0
    //   1658: getfield deviceIP : Ljava/lang/String;
    //   1661: aload_1
    //   1662: aconst_null
    //   1663: invokevirtual sendData : (ILjava/lang/String;[BLapp/gamer/quadstellar/net/UdpClient$ReceiveCallback;)V
    //   1666: aload_3
    //   1667: monitorexit
    //   1668: goto -> 111
    //   1671: ldc 'UdpClient'
    //   1673: aload #4
    //   1675: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   1678: pop
    //   1679: aload #4
    //   1681: ifnull -> 166
    //   1684: invokestatic getInstance : ()Lapp/gamer/quadstellar/db/FatherDeviceInfoDB;
    //   1687: aload #4
    //   1689: invokevirtual queryUserList : (Ljava/lang/String;)Lapp/gamer/quadstellar/domain/FatherDeviceInfo;
    //   1692: ifnull -> 166
    //   1695: aload_0
    //   1696: getstatic app/gamer/quadstellar/Config.BROADCAST_RECVPIPE : Ljava/lang/String;
    //   1699: aload #4
    //   1701: aload_2
    //   1702: iconst_0
    //   1703: baload
    //   1704: aload_2
    //   1705: invokevirtual sendPipeBroad : (Ljava/lang/String;Ljava/lang/String;I[B)V
    //   1708: goto -> 166
    // Exception table:
    //   from	to	target	type
    //   2	23	176	finally
    //   23	35	131	java/lang/Exception
    //   23	35	171	finally
    //   47	97	131	java/lang/Exception
    //   47	97	171	finally
    //   109	111	171	finally
    //   114	128	131	java/lang/Exception
    //   114	128	171	finally
    //   132	166	171	finally
    //   166	168	171	finally
    //   172	174	171	finally
    //   174	176	176	finally
    //   188	207	131	java/lang/Exception
    //   188	207	171	finally
    //   211	233	131	java/lang/Exception
    //   211	233	171	finally
    //   251	280	131	java/lang/Exception
    //   251	280	171	finally
    //   285	287	171	finally
    //   290	327	131	java/lang/Exception
    //   290	327	171	finally
    //   331	479	131	java/lang/Exception
    //   331	479	171	finally
    //   482	503	131	java/lang/Exception
    //   482	503	171	finally
    //   508	562	131	java/lang/Exception
    //   508	562	171	finally
    //   568	584	131	java/lang/Exception
    //   568	584	171	finally
    //   587	653	131	java/lang/Exception
    //   587	653	171	finally
    //   659	671	131	java/lang/Exception
    //   659	671	171	finally
    //   671	701	131	java/lang/Exception
    //   671	701	171	finally
    //   704	866	131	java/lang/Exception
    //   704	866	171	finally
    //   871	873	171	finally
    //   879	952	131	java/lang/Exception
    //   879	952	171	finally
    //   958	972	131	java/lang/Exception
    //   958	972	171	finally
    //   977	979	171	finally
    //   985	1049	131	java/lang/Exception
    //   985	1049	171	finally
    //   1055	1067	131	java/lang/Exception
    //   1055	1067	171	finally
    //   1086	1108	131	java/lang/Exception
    //   1086	1108	171	finally
    //   1113	1115	171	finally
    //   1118	1150	131	java/lang/Exception
    //   1118	1150	171	finally
    //   1192	1194	171	finally
    //   1197	1414	131	java/lang/Exception
    //   1197	1414	171	finally
    //   1417	1491	131	java/lang/Exception
    //   1417	1491	171	finally
    //   1494	1532	131	java/lang/Exception
    //   1494	1532	171	finally
    //   1532	1570	131	java/lang/Exception
    //   1532	1570	171	finally
    //   1575	1608	131	java/lang/Exception
    //   1575	1608	171	finally
    //   1628	1630	171	finally
    //   1638	1666	131	java/lang/Exception
    //   1638	1666	171	finally
    //   1666	1668	171	finally
    //   1671	1679	131	java/lang/Exception
    //   1671	1679	171	finally
    //   1684	1708	131	java/lang/Exception
    //   1684	1708	171	finally
  }
  
  private void startThread() {
    if (this.rthread == null) {
      this.rthread = new Thread(client);
      this.rthread.start();
    } 
  }
  
  private void stopThread() {
    if (this.rthread != null) {
      this.isThreadDisable = true;
      this.rthread.interrupt();
      this.rthread = null;
    } 
  }
  
  public boolean connectSocket() {
    boolean bool = false;
    try {
      if (this.rUdp == null) {
        DatagramSocket datagramSocket = new DatagramSocket();
        this((SocketAddress)null);
        this.rUdp = datagramSocket;
        this.rUdp.setReuseAddress(true);
        this.rUdp.setBroadcast(true);
        datagramSocket = this.rUdp;
        InetSocketAddress inetSocketAddress = new InetSocketAddress();
        this(9432);
        datagramSocket.bind(inetSocketAddress);
      } 
      if (this.sUdp == null) {
        DatagramSocket datagramSocket = new DatagramSocket();
        this((SocketAddress)null);
        this.sUdp = datagramSocket;
        datagramSocket = this.sUdp;
        InetSocketAddress inetSocketAddress = new InetSocketAddress();
        this(this.sUdp.getLocalPort());
        datagramSocket.bind(inetSocketAddress);
        StringBuilder stringBuilder = new StringBuilder();
        this();
        LogUtil.e("UdpClient", stringBuilder.append("sUdp.getLocalPort():").append(this.sUdp.getLocalPort()).toString());
      } 
      if (this.rPacket == null) {
        DatagramPacket datagramPacket = new DatagramPacket();
        this(this.rBuffer, this.rBuffer.length);
        this.rPacket = datagramPacket;
      } 
      if (this.rPacket2 == null) {
        DatagramPacket datagramPacket = new DatagramPacket();
        this(this.rBuffer, this.rBuffer.length);
        this.rPacket2 = datagramPacket;
      } 
      startThread();
      bool = true;
    } catch (SocketException socketException) {
      disConnectSocket();
      LogUtil.e("UdpClient", "open udp port error:" + socketException.getMessage());
    } 
    return bool;
  }
  
  public void disConnectSocket() {
    if (this.sUdp != null) {
      this.sUdp.close();
      this.sUdp = null;
    } 
    if (this.rPacket != null)
      this.rPacket = null; 
    if (this.rPacket2 != null)
      this.rPacket2 = null; 
    stopThread();
  }
  
  public void run() {
    (new Thread() {
        public void run() {
          super.run();
          try {
            while (true) {
              if (UdpClient.this.rthread != null && !UdpClient.this.rthread.isInterrupted()) {
                if (UdpClient.this.rPacket == null) {
                  UdpClient udpClient = UdpClient.this;
                  DatagramPacket datagramPacket = new DatagramPacket();
                  this(UdpClient.this.rBuffer, UdpClient.this.rBuffer.length);
                  UdpClient.access$302(udpClient, datagramPacket);
                } 
                UdpClient.this.rUdp.receive(UdpClient.this.rPacket);
                if (UdpClient.this.rPacket != null) {
                  byte[] arrayOfByte = UdpClient.this.rPacket.getData();
                  if (arrayOfByte != null) {
                    if (arrayOfByte[0] == -16 && arrayOfByte[2] == 100 && UdpClient.this.rPacket.getLength() > 7)
                      UdpClient.this.receiveData(arrayOfByte); 
                    continue;
                  } 
                } 
              } 
              return;
            } 
          } catch (Exception exception) {
            LogUtil.e("UdpClient", "recvdata error:" + exception.getMessage());
            UdpClient.this.disConnectSocket();
            UdpClient.this.connectSocket();
          } 
        }
      }).start();
    (new Thread() {
        public void run() {
          super.run();
          try {
            while (UdpClient.this.rthread != null && !UdpClient.this.rthread.isInterrupted()) {
              if (UdpClient.this.rPacket2 == null) {
                UdpClient udpClient = UdpClient.this;
                DatagramPacket datagramPacket = new DatagramPacket();
                this(UdpClient.this.rBuffer, UdpClient.this.rBuffer.length);
                UdpClient.access$702(udpClient, datagramPacket);
              } 
              UdpClient.this.sUdp.receive(UdpClient.this.rPacket2);
              UdpClient.this.recvData(UdpClient.this.rPacket2);
            } 
          } catch (Exception exception) {
            LogUtil.e("UdpClient", "recvdata error:" + exception.getMessage());
            UdpClient.this.disConnectSocket();
            UdpClient.this.connectSocket();
          } 
        }
      }).start();
  }
  
  public void sendData(final int requestCode, final String deviceIP, final byte[] sData, ReceiveCallback paramReceiveCallback) {
    this.requestCode = requestCode;
    if (paramReceiveCallback != null)
      this.callback = paramReceiveCallback; 
    ThreadManger.execute(new Runnable() {
          public void run() {
            try {
              if (UdpClient.this.sUdp == null) {
                UdpClient udpClient1 = UdpClient.this;
                DatagramSocket datagramSocket = new DatagramSocket();
                this((SocketAddress)null);
                UdpClient.access$002(udpClient1, datagramSocket);
                StringBuilder stringBuilder1 = new StringBuilder();
                this();
                LogUtil.e("UdpClient", stringBuilder1.append("sUdp.getLocalPort():").append(UdpClient.this.sUdp.getLocalPort()).toString());
              } 
              UdpClient udpClient = UdpClient.this;
              DatagramPacket datagramPacket = new DatagramPacket();
              this(sData, sData.length, InetAddress.getByName(deviceIP), 5876);
              UdpClient.access$102(udpClient, datagramPacket);
              UdpClient.this.sUdp.send(UdpClient.this.sPacket);
              StringBuilder stringBuilder = new StringBuilder();
              this();
              LogUtil.e("UdpClient", stringBuilder.append(requestCode).append(deviceIP).append("==").append(5876).append("==").append(XlinkUtils.getHexBinString(sData)).toString());
            } catch (IOException iOException) {
              LogUtil.e("UdpClient", "senddata error:" + iOException.getMessage());
            } 
          }
        });
  }
  
  public void sendPipeBroad(String paramString1, String paramString2, int paramInt, byte[] paramArrayOfbyte) {
    Intent intent = new Intent(paramString1);
    intent.putExtra("device-mac", paramString2);
    intent.putExtra("type", paramInt);
    intent.putExtra("request_code_key", this.requestCode);
    if (paramArrayOfbyte != null) {
      XlinkUtils.getHexBinString(paramArrayOfbyte);
      intent.putExtra("data", paramArrayOfbyte);
    } 
    App.getAppContext().sendBroadcast(intent);
  }
  
  public void setReceiveCallback(ReceiveCallback paramReceiveCallback) {
    this.callback = paramReceiveCallback;
  }
  
  public static interface ReceiveCallback {
    void onReceiveCallback(boolean param1Boolean);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/net/UdpClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */