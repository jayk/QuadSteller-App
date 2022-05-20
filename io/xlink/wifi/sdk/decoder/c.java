package io.xlink.wifi.sdk.decoder;

import android.util.SparseArray;
import io.xlink.wifi.sdk.DataPoint;
import io.xlink.wifi.sdk.XDevice;
import io.xlink.wifi.sdk.XlinkTcpService;
import io.xlink.wifi.sdk.XlinkUdpService;
import io.xlink.wifi.sdk.bean.EventNotify;
import io.xlink.wifi.sdk.encoder.e;
import io.xlink.wifi.sdk.encoder.f;
import io.xlink.wifi.sdk.encoder.g;
import io.xlink.wifi.sdk.manage.b;
import io.xlink.wifi.sdk.util.MyLog;
import io.xlink.wifi.sdk.util.b;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.ArrayList;

public class c {
  private static String a = "Packet Decoder";
  
  private c() {}
  
  private SparseArray<DataPoint> a(XDevice paramXDevice, io.xlink.wifi.sdk.buffer.a parama) {
    byte b = 0;
    int i = b.a().a(paramXDevice.getProductId());
    SparseArray sparseArray = b.a().b(paramXDevice.getProductId());
    SparseArray<DataPoint> sparseArray1 = new SparseArray();
    if (i != 0) {
      byte b1;
      if (sparseArray == null) {
        a("parse datapoint error product not found pid:" + paramXDevice.getProductId() + " deviceId:" + paramXDevice.getDeviceId());
        return sparseArray1;
      } 
      ArrayList<Integer> arrayList = new ArrayList();
      byte[] arrayOfByte = parama.a(i);
      i = 0;
      while (true) {
        b1 = b;
        if (i < arrayOfByte.length) {
          for (b1 = 0; b1 <= 7; b1++) {
            if (b.a(arrayOfByte[i], b1))
              arrayList.add(Integer.valueOf(i * 8 + b1)); 
          } 
          i++;
          continue;
        } 
        break;
      } 
      while (true) {
        if (b1 < arrayList.size()) {
          DataPoint dataPoint = (DataPoint)sparseArray.get(((Integer)arrayList.get(b1)).intValue());
          if (dataPoint != null) {
            Object object = f.a(parama, dataPoint.getType());
            if (object != null) {
              dataPoint.setValue(object);
              sparseArray1.put(dataPoint.getKey(), dataPoint);
            } 
          } 
          b1++;
          continue;
        } 
        return sparseArray1;
      } 
    } 
    return sparseArray1;
  }
  
  public static c a() {
    return a.a();
  }
  
  private void a(String paramString) {
    MyLog.e(a, paramString);
  }
  
  private void b(a parama) {
    io.xlink.wifi.sdk.buffer.a a1 = parama.b();
    int i = a1.f();
    short s = a1.g();
    XDevice xDevice = io.xlink.wifi.sdk.manage.c.a().a(i);
    g g = e.a(s);
    i = a1.d();
    if (g == null) {
      a("received probe packets  no task ");
    } else {
      g.c();
      e e = new e(i);
      e.a = g.a();
      g.g().onResponse(e);
    } 
    if (i == 0 && xDevice != null) {
      byte b = a1.d();
      if (b.a(b, 0))
        xDevice.setDeviceName(b.c(a1.a(a1.g()))); 
      if (b.a(b, 1)) {
        SparseArray<DataPoint> sparseArray = a(xDevice, a1);
        int j = sparseArray.size();
        for (i = 0; i < j; i++) {
          DataPoint dataPoint = (DataPoint)sparseArray.valueAt(i);
          io.xlink.wifi.sdk.event.c.a(xDevice, dataPoint.getKey(), dataPoint.getObjectValue(), 1, dataPoint.getType());
        } 
      } 
    } 
  }
  
  private void c(a parama) {
    io.xlink.wifi.sdk.buffer.a a1 = parama.b();
    a1.f();
    short s = a1.g();
    byte b = a1.d();
    g g = e.a(s);
    if (g != null) {
      g.c();
      e e = new e(b);
      e.a = g.a();
      e.c = s;
      g.g().onResponse(e);
    } 
  }
  
  private void d(a parama) {
    io.xlink.wifi.sdk.buffer.a a1 = parama.b();
    int i = a1.f();
    short s = a1.g();
    byte b = a1.d();
    a("parse subscription deviceid:" + i + "  code：" + b);
    g g = e.b(s + "");
    if (g != null) {
      g.c();
      e e = e.b(b);
      e.a = g.b().b();
      if (i > 0 && b == 0)
        e.a = io.xlink.wifi.sdk.a.a().b(e.a, i); 
      g.g().onResponse(e);
      return;
    } 
    a("error:subscription task is null");
  }
  
  private void e(a parama) {
    io.xlink.wifi.sdk.buffer.a a1 = parama.b();
    int i = a1.f();
    XDevice xDevice = io.xlink.wifi.sdk.manage.c.a().a(i);
    if (xDevice == null) {
      a("cloud sync fail deviceID =" + i + " device not found");
      return;
    } 
    a1.g();
    byte b = a1.d();
    if (b.a(b, 0))
      xDevice.setDeviceName(b.c(a1.a(a1.g()))); 
    if (b.a(b, 1)) {
      SparseArray<DataPoint> sparseArray = a(xDevice, a1);
      for (i = 0; i < sparseArray.size(); i++) {
        DataPoint dataPoint = (DataPoint)sparseArray.valueAt(i);
        io.xlink.wifi.sdk.event.c.a(xDevice, dataPoint.getKey(), dataPoint.getObjectValue(), 1, dataPoint.getType());
      } 
    } 
    a("cloud sync   device :" + xDevice.getMacAddress());
  }
  
  private void f(a parama) {
    io.xlink.wifi.sdk.buffer.a a1 = parama.b();
    a1.f();
    short s = a1.g();
    byte b = a1.d();
    g g = e.a(s);
    if (g != null) {
      g.c();
      e e = e.b(b);
      e.a = g.b().b();
      e.c = s;
      g.g().onResponse(e);
    } 
    a("outer set  code :" + b);
  }
  
  private void g(a parama) {
    io.xlink.wifi.sdk.buffer.a a1 = parama.b();
    byte b = a1.d();
    a1.d();
    g g = e.b("1");
    if (g != null) {
      g.c();
      g.g().onResponse(e.b(b));
    } 
    a("parse Login response  code :" + b);
  }
  
  private void h(a parama) {
    io.xlink.wifi.sdk.buffer.a a1 = parama.b();
    a1.f();
    short s = a1.g();
    byte b = a1.d();
    g g = e.b(s + "");
    if (g != null) {
      g.c();
      e e = e.b(b);
      e.a = g.b().b();
      e.c = s;
      g.g().onResponse(e);
    } 
    a("receive send Pipe response code :" + b);
  }
  
  private void i(a parama) {
    io.xlink.wifi.sdk.buffer.a a1 = parama.b();
    int i = a1.f();
    a1.g();
    byte b = a1.d();
    byte[] arrayOfByte = a1.e();
    XDevice xDevice2 = io.xlink.wifi.sdk.manage.c.a().a(i);
    XDevice xDevice1 = xDevice2;
    if (xDevice2 == null) {
      xDevice1 = io.xlink.wifi.sdk.a.a().a("");
      xDevice1 = io.xlink.wifi.sdk.a.a().b(xDevice1, i);
    } 
    io.xlink.wifi.sdk.event.c.a(false, xDevice1, b, arrayOfByte);
    a("receive push pipe1 packet  deviceId :" + i + " dataLength :" + arrayOfByte.length);
  }
  
  private void j(a parama) {
    io.xlink.wifi.sdk.buffer.a a1 = parama.b();
    int i = a1.f();
    a1.g();
    byte b = a1.d();
    byte[] arrayOfByte = a1.e();
    XDevice xDevice = io.xlink.wifi.sdk.manage.c.a().a(i);
    if (xDevice != null) {
      io.xlink.wifi.sdk.event.c.a(true, xDevice, b, arrayOfByte);
      a("receive push sync pipe2 packet  deviceId :" + i + " dataLength :" + arrayOfByte.length);
    } 
  }
  
  private void k(a parama) {
    io.xlink.wifi.sdk.buffer.a a1 = parama.b();
    if (a1.d() == 10) {
      EventNotify eventNotify = new EventNotify();
      eventNotify.notyfyFlags = a1.d();
      eventNotify.formId = a1.f();
      eventNotify.messageId = a1.g();
      eventNotify.messageType = a1.g();
      eventNotify.notifyData = a1.e();
      io.xlink.wifi.sdk.event.c.a(eventNotify);
      a("cloud event notify: " + eventNotify.toString());
    } 
  }
  
  private void l(a parama) {
    switch (parama.b().d()) {
      default:
        return;
      case 3:
        break;
    } 
    XlinkTcpService.a().a(true, -4, false);
  }
  
  private void m(a parama) {
    a("UDP read packet : " + parama.toString());
    if ((parama.c() != 1 || parama.c() != 2 || parama.c() != 9) && !XlinkUdpService.b().c())
      XlinkUdpService.b().d(); 
    switch (parama.c()) {
      default:
        a("Receive udp error type of package:" + parama);
        return;
      case 1:
        w(parama);
        return;
      case 2:
        v(parama);
        return;
      case 13:
        u(parama);
        return;
      case 3:
        r(parama);
        return;
      case 4:
        t(parama);
        return;
      case 5:
        s(parama);
        return;
      case 8:
        if (parama.d().d()) {
          q(parama);
          return;
        } 
        p(parama);
        return;
      case 9:
        o(parama);
        return;
      case 11:
        break;
    } 
    n(parama);
  }
  
  private void n(a parama) {
    io.xlink.wifi.sdk.buffer.a a1 = parama.b();
    short s = a1.g();
    byte b = a1.d();
    g g = e.a(s);
    if (g == null) {
      a("receive setAccessKey  task not found ,code:" + b);
      return;
    } 
    g.c();
    e e = e.b(b);
    e.a = g.b().b();
    if (e.a != null) {
      XDevice xDevice = io.xlink.wifi.sdk.manage.c.a().b(e.a.getMacAddress());
      if (xDevice != null) {
        e.a = io.xlink.wifi.sdk.a.a().a(true, xDevice);
      } else {
        e.a = io.xlink.wifi.sdk.a.a().a(true, e.a);
      } 
      e.a.setLocalAddress(parama.e());
      e.c = s;
      g.g().onResponse(e);
      return;
    } 
    a("error:set device accesskey fail device not found  。");
  }
  
  private void o(a parama) {
    io.xlink.wifi.sdk.buffer.a a1 = parama.b();
    short s = a1.g();
    byte b = a1.d();
    g g = e.a(s);
    if (g == null) {
      a("receive setPw  task not found ,code:" + b);
      return;
    } 
    g.c();
    e e = e.b(b);
    e.a = g.b().b();
    if (e.a != null) {
      XDevice xDevice = io.xlink.wifi.sdk.manage.c.a().b(e.a.getMacAddress());
      if (xDevice != null) {
        e.a = io.xlink.wifi.sdk.a.a().a(true, xDevice);
      } else {
        e.a = io.xlink.wifi.sdk.a.a().a(true, e.a);
      } 
      e.a.setLocalAddress(parama.e());
      e.c = s;
      g.g().onResponse(e);
      return;
    } 
    a("error:set device pw fail device not found  。");
  }
  
  private void p(a parama) {
    io.xlink.wifi.sdk.buffer.a a1 = parama.b();
    String str = b.d(a1.a(6));
    XDevice xDevice = io.xlink.wifi.sdk.manage.c.a().b(str);
    if (xDevice == null) {
      a("error udp push Pipe device not found mac:" + str);
      return;
    } 
    a1.g();
    byte b = a1.d();
    byte[] arrayOfByte = a1.e();
    xDevice = io.xlink.wifi.sdk.a.a().a(0, xDevice);
    xDevice.setLocalAddress(parama.e());
    io.xlink.wifi.sdk.event.c.a(false, xDevice, b, arrayOfByte);
  }
  
  private void q(a parama) {
    io.xlink.wifi.sdk.buffer.a a1 = parama.b();
    short s = a1.g();
    byte b = a1.d();
    g g = e.a(s);
    if (g != null) {
      if (b == 1) {
        a("parseLocalPipe::code==1；ssid ==" + g.a().getSessionId() + "error! reconnectDevice");
        io.xlink.wifi.sdk.a.a().a(g.a());
      } 
      e e = e.b(b);
      io.xlink.wifi.sdk.manage.c.a().a(g.a().getMacAddress());
      e.a = g.a();
      e.a.setLocalAddress(parama.e());
      e.c = s;
      g.c();
      g.g().onResponse(e);
    } 
  }
  
  private void r(a parama) {
    byte b = parama.b().d();
    a("local probe fail errorCode:" + b);
  }
  
  private void s(a parama) {
    io.xlink.wifi.sdk.buffer.a a1 = parama.b();
    String str = b.d(a1.a(6));
    XDevice xDevice = io.xlink.wifi.sdk.manage.c.a().b(str);
    if (xDevice == null) {
      a("local sync device not found,MAC :" + str);
      return;
    } 
    xDevice.setLocalAddress(parama.e());
    a("receive local sync deviceMac " + str);
    byte b = a1.d();
    if (b.a(b, 0))
      xDevice.setDeviceName(b.c(a1.a(a1.g()))); 
    if (b.a(b, 1)) {
      SparseArray<DataPoint> sparseArray = a(xDevice, a1);
      byte b1 = 0;
      while (true) {
        if (b1 < sparseArray.size()) {
          DataPoint dataPoint = (DataPoint)sparseArray.valueAt(b1);
          io.xlink.wifi.sdk.event.c.a(xDevice, dataPoint.getKey(), dataPoint.getObjectValue(), 0, dataPoint.getType());
          b1++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  private void t(a parama) {
    io.xlink.wifi.sdk.buffer.a a1 = parama.b();
    short s = a1.g();
    byte b = a1.d();
    g g = e.a(s);
    if (g != null) {
      g.c();
      e e = e.b(b);
      e.a = g.b().b();
      e.a.setLocalAddress(parama.e());
      e.c = s;
      g.g().onResponse(e);
      return;
    } 
    a("recerve local set datapoint task not found :");
  }
  
  private void u(a parama) {
    String str = b.d(parama.b().a(6));
    XDevice xDevice = io.xlink.wifi.sdk.manage.c.a().b(str);
    if (xDevice == null) {
      a("local ping device not found MAC: " + str);
      return;
    } 
    xDevice.setLocalAddress(parama.e());
    a("receive device Mac " + str + " ping ");
    io.xlink.wifi.sdk.a.a().b(xDevice);
  }
  
  private void v(a parama) {
    io.xlink.wifi.sdk.buffer.a a1 = new io.xlink.wifi.sdk.buffer.a(parama.b().b(), 5);
    byte b1 = a1.d();
    byte b2 = a1.d();
    byte[] arrayOfByte = a1.a(6);
    String str = b.d(arrayOfByte).trim();
    g g = e.b(str);
    if (g == null) {
      a("receive handshake device not found mac:" + str);
      return;
    } 
    g.c();
    e e = e.b(b1);
    XDevice xDevice = io.xlink.wifi.sdk.manage.c.a().b(str);
    if (xDevice != null) {
      xDevice.setLocalAddress(parama.e());
      xDevice.setVersion(b2);
      if (b1 != 0) {
        e.a = xDevice;
        io.xlink.wifi.sdk.a.a().a(xDevice, str);
        g.g().onResponse(e);
        return;
      } 
      int i = a1.f();
      io.xlink.wifi.sdk.a.a().b(xDevice, i);
      xDevice.setMcuSoftVersion(a1.g());
      i = b.d(a1.g());
      XDevice xDevice1 = io.xlink.wifi.sdk.a.a().a(xDevice, parama.a(), arrayOfByte, xDevice.getProductId(), xDevice.getPort());
      xDevice1 = io.xlink.wifi.sdk.a.a().a(xDevice1, i);
      a1.d();
      io.xlink.wifi.sdk.manage.c.a().c(xDevice1);
      e.a = xDevice1;
      g.g().onResponse(e);
      XlinkUdpService.b().d();
      a("receive device MAC: " + str + " HandShake  sessionId :" + xDevice1.getSessionId() + " deviceId:" + xDevice1.getDeviceId());
    } 
  }
  
  private void w(a parama) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual b : ()Lio/xlink/wifi/sdk/buffer/a;
    //   4: astore_2
    //   5: aload_2
    //   6: invokevirtual d : ()B
    //   9: istore_3
    //   10: aload_2
    //   11: bipush #6
    //   13: invokevirtual a : (I)[B
    //   16: astore #4
    //   18: aload #4
    //   20: invokestatic d : ([B)Ljava/lang/String;
    //   23: astore #5
    //   25: invokestatic a : ()Lio/xlink/wifi/sdk/a;
    //   28: aload #5
    //   30: invokevirtual a : (Ljava/lang/String;)Lio/xlink/wifi/sdk/XDevice;
    //   33: astore #6
    //   35: aload #6
    //   37: iload_3
    //   38: invokevirtual setVersion : (B)V
    //   41: aload_2
    //   42: aload_2
    //   43: invokevirtual g : ()S
    //   46: invokevirtual a : (I)[B
    //   49: invokestatic c : ([B)Ljava/lang/String;
    //   52: astore #7
    //   54: aload #6
    //   56: aload_2
    //   57: invokevirtual d : ()B
    //   60: invokevirtual setMcuHardVersion : (B)V
    //   63: aload #6
    //   65: aload_2
    //   66: invokevirtual g : ()S
    //   69: invokevirtual setMcuSoftVersion : (I)V
    //   72: aload_2
    //   73: invokevirtual g : ()S
    //   76: invokestatic d : (I)I
    //   79: istore #8
    //   81: aload #6
    //   83: astore #9
    //   85: iload_3
    //   86: iconst_2
    //   87: if_icmpne -> 111
    //   90: aload_2
    //   91: invokevirtual g : ()S
    //   94: invokestatic d : (I)I
    //   97: istore #10
    //   99: invokestatic a : ()Lio/xlink/wifi/sdk/a;
    //   102: aload #6
    //   104: iload #10
    //   106: invokevirtual d : (Lio/xlink/wifi/sdk/XDevice;I)Lio/xlink/wifi/sdk/XDevice;
    //   109: astore #9
    //   111: aload_2
    //   112: invokevirtual d : ()B
    //   115: istore #11
    //   117: iload #11
    //   119: iconst_0
    //   120: invokestatic a : (BI)Z
    //   123: ifeq -> 142
    //   126: aload #9
    //   128: aload_2
    //   129: aload_2
    //   130: invokevirtual g : ()S
    //   133: invokevirtual a : (I)[B
    //   136: invokestatic c : ([B)Ljava/lang/String;
    //   139: invokevirtual setDeviceName : (Ljava/lang/String;)V
    //   142: iload #11
    //   144: iconst_1
    //   145: invokestatic a : (BI)Z
    //   148: ifeq -> 151
    //   151: iload_3
    //   152: iconst_1
    //   153: if_icmpne -> 534
    //   156: aload #9
    //   158: astore #6
    //   160: iload #11
    //   162: iconst_2
    //   163: invokestatic a : (BI)Z
    //   166: ifeq -> 180
    //   169: invokestatic a : ()Lio/xlink/wifi/sdk/a;
    //   172: iconst_1
    //   173: aload #9
    //   175: invokevirtual a : (ZLio/xlink/wifi/sdk/XDevice;)Lio/xlink/wifi/sdk/XDevice;
    //   178: astore #6
    //   180: invokestatic a : ()Lio/xlink/wifi/sdk/a;
    //   183: aload #6
    //   185: aload_1
    //   186: invokevirtual a : ()Ljava/net/InetAddress;
    //   189: aload #4
    //   191: aload #7
    //   193: iload #8
    //   195: invokevirtual a : (Lio/xlink/wifi/sdk/XDevice;Ljava/net/InetAddress;[BLjava/lang/String;I)Lio/xlink/wifi/sdk/XDevice;
    //   198: astore #6
    //   200: invokestatic a : ()Lio/xlink/wifi/sdk/manage/c;
    //   203: aload #6
    //   205: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   208: invokevirtual b : (Ljava/lang/String;)Lio/xlink/wifi/sdk/XDevice;
    //   211: astore #9
    //   213: aload #9
    //   215: ifnull -> 600
    //   218: invokestatic a : ()Lio/xlink/wifi/sdk/a;
    //   221: aload #6
    //   223: invokevirtual isInit : ()Z
    //   226: aload #9
    //   228: invokevirtual a : (ZLio/xlink/wifi/sdk/XDevice;)Lio/xlink/wifi/sdk/XDevice;
    //   231: astore #9
    //   233: invokestatic a : ()Lio/xlink/wifi/sdk/a;
    //   236: aload #9
    //   238: aload_1
    //   239: invokevirtual a : ()Ljava/net/InetAddress;
    //   242: aload #4
    //   244: aload #7
    //   246: iload #8
    //   248: invokevirtual a : (Lio/xlink/wifi/sdk/XDevice;Ljava/net/InetAddress;[BLjava/lang/String;I)Lio/xlink/wifi/sdk/XDevice;
    //   251: astore #9
    //   253: invokestatic a : ()Lio/xlink/wifi/sdk/a;
    //   256: aload #9
    //   258: aload #6
    //   260: invokevirtual getAccessKey : ()I
    //   263: invokevirtual c : (Lio/xlink/wifi/sdk/XDevice;I)Lio/xlink/wifi/sdk/XDevice;
    //   266: astore #9
    //   268: invokestatic a : ()Lio/xlink/wifi/sdk/a;
    //   271: aload #9
    //   273: aload #6
    //   275: invokevirtual getProductType : ()I
    //   278: invokevirtual d : (Lio/xlink/wifi/sdk/XDevice;I)Lio/xlink/wifi/sdk/XDevice;
    //   281: astore #6
    //   283: aload #6
    //   285: aload_1
    //   286: invokevirtual e : ()Ljava/net/SocketAddress;
    //   289: invokevirtual setLocalAddress : (Ljava/net/SocketAddress;)V
    //   292: invokestatic a : ()Lio/xlink/wifi/sdk/manage/c;
    //   295: aload #6
    //   297: invokevirtual c : (Lio/xlink/wifi/sdk/XDevice;)V
    //   300: new java/lang/StringBuilder
    //   303: astore_1
    //   304: aload_1
    //   305: invokespecial <init> : ()V
    //   308: iload #11
    //   310: iconst_4
    //   311: invokestatic a : (BI)Z
    //   314: ifeq -> 622
    //   317: new java/lang/StringBuilder
    //   320: astore #9
    //   322: aload #9
    //   324: invokespecial <init> : ()V
    //   327: aload_1
    //   328: aload #9
    //   330: ldc_w 'read device: '
    //   333: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   336: aload #5
    //   338: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   341: invokevirtual toString : ()Ljava/lang/String;
    //   344: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   347: pop
    //   348: new java/lang/StringBuilder
    //   351: astore #9
    //   353: aload #9
    //   355: invokespecial <init> : ()V
    //   358: aload #9
    //   360: aload #5
    //   362: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   365: iconst_1
    //   366: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   369: invokevirtual toString : ()Ljava/lang/String;
    //   372: invokestatic b : (Ljava/lang/String;)Lio/xlink/wifi/sdk/encoder/g;
    //   375: astore #5
    //   377: aload #5
    //   379: ifnull -> 603
    //   382: new io/xlink/wifi/sdk/encoder/e
    //   385: astore #9
    //   387: aload #9
    //   389: iconst_0
    //   390: invokespecial <init> : (I)V
    //   393: aload #9
    //   395: aload #6
    //   397: putfield a : Lio/xlink/wifi/sdk/XDevice;
    //   400: aload #5
    //   402: invokevirtual c : ()V
    //   405: aload #5
    //   407: invokevirtual g : ()Lio/xlink/wifi/sdk/event/a;
    //   410: aload #9
    //   412: invokevirtual onResponse : (Lio/xlink/wifi/sdk/encoder/e;)V
    //   415: aload_1
    //   416: ldc_w ' Scaning by mac '
    //   419: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   422: pop
    //   423: new java/lang/StringBuilder
    //   426: astore #9
    //   428: aload #9
    //   430: invokespecial <init> : ()V
    //   433: aload_1
    //   434: aload #9
    //   436: ldc_w 'port '
    //   439: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   442: aload #6
    //   444: invokevirtual getPort : ()I
    //   447: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   450: invokevirtual toString : ()Ljava/lang/String;
    //   453: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   456: pop
    //   457: new java/lang/StringBuilder
    //   460: astore #9
    //   462: aload #9
    //   464: invokespecial <init> : ()V
    //   467: aload_1
    //   468: aload #9
    //   470: ldc_w 'deviceId:'
    //   473: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   476: aload #6
    //   478: invokevirtual getDeviceId : ()I
    //   481: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   484: invokevirtual toString : ()Ljava/lang/String;
    //   487: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   490: pop
    //   491: new java/lang/StringBuilder
    //   494: astore #9
    //   496: aload #9
    //   498: invokespecial <init> : ()V
    //   501: aload_1
    //   502: aload #9
    //   504: ldc_w ' productId:'
    //   507: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   510: aload #6
    //   512: invokevirtual getProductId : ()Ljava/lang/String;
    //   515: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   518: invokevirtual toString : ()Ljava/lang/String;
    //   521: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   524: pop
    //   525: aload_0
    //   526: aload_1
    //   527: invokevirtual toString : ()Ljava/lang/String;
    //   530: invokespecial a : (Ljava/lang/String;)V
    //   533: return
    //   534: iload #11
    //   536: iconst_2
    //   537: invokestatic a : (BI)Z
    //   540: ifeq -> 575
    //   543: invokestatic a : ()Lio/xlink/wifi/sdk/a;
    //   546: iconst_1
    //   547: aload #9
    //   549: invokevirtual a : (ZLio/xlink/wifi/sdk/XDevice;)Lio/xlink/wifi/sdk/XDevice;
    //   552: astore #6
    //   554: aload_2
    //   555: invokevirtual f : ()I
    //   558: istore #10
    //   560: invokestatic a : ()Lio/xlink/wifi/sdk/a;
    //   563: aload #6
    //   565: iload #10
    //   567: invokevirtual c : (Lio/xlink/wifi/sdk/XDevice;I)Lio/xlink/wifi/sdk/XDevice;
    //   570: astore #6
    //   572: goto -> 180
    //   575: invokestatic a : ()Lio/xlink/wifi/sdk/a;
    //   578: aload #9
    //   580: iconst_m1
    //   581: invokevirtual c : (Lio/xlink/wifi/sdk/XDevice;I)Lio/xlink/wifi/sdk/XDevice;
    //   584: astore #6
    //   586: invokestatic a : ()Lio/xlink/wifi/sdk/a;
    //   589: iconst_0
    //   590: aload #6
    //   592: invokevirtual a : (ZLio/xlink/wifi/sdk/XDevice;)Lio/xlink/wifi/sdk/XDevice;
    //   595: astore #6
    //   597: goto -> 180
    //   600: goto -> 283
    //   603: aload_1
    //   604: ldc_w ' Scaning by mac task is null'
    //   607: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   610: pop
    //   611: goto -> 423
    //   614: astore_1
    //   615: aload_1
    //   616: invokevirtual printStackTrace : ()V
    //   619: goto -> 533
    //   622: new java/lang/StringBuilder
    //   625: astore #9
    //   627: aload #9
    //   629: invokespecial <init> : ()V
    //   632: aload_1
    //   633: aload #9
    //   635: ldc_w 'read device: '
    //   638: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   641: aload #5
    //   643: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   646: invokevirtual toString : ()Ljava/lang/String;
    //   649: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   652: pop
    //   653: aload_1
    //   654: ldc_w ' Scaning by productId '
    //   657: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   660: pop
    //   661: new java/lang/StringBuilder
    //   664: astore #9
    //   666: aload #9
    //   668: invokespecial <init> : ()V
    //   671: aload_1
    //   672: aload #9
    //   674: ldc_w 'port '
    //   677: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   680: aload #6
    //   682: invokevirtual getPort : ()I
    //   685: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   688: invokevirtual toString : ()Ljava/lang/String;
    //   691: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   694: pop
    //   695: new java/lang/StringBuilder
    //   698: astore #9
    //   700: aload #9
    //   702: invokespecial <init> : ()V
    //   705: aload_1
    //   706: aload #9
    //   708: ldc_w 'deviceId:'
    //   711: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   714: aload #6
    //   716: invokevirtual getDeviceId : ()I
    //   719: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   722: invokevirtual toString : ()Ljava/lang/String;
    //   725: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   728: pop
    //   729: new java/lang/StringBuilder
    //   732: astore #9
    //   734: aload #9
    //   736: invokespecial <init> : ()V
    //   739: aload_1
    //   740: aload #9
    //   742: ldc_w ' productId:'
    //   745: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   748: aload #6
    //   750: invokevirtual getProductId : ()Ljava/lang/String;
    //   753: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   756: invokevirtual toString : ()Ljava/lang/String;
    //   759: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   762: pop
    //   763: aload #6
    //   765: invokestatic a : (Lio/xlink/wifi/sdk/XDevice;)V
    //   768: goto -> 525
    // Exception table:
    //   from	to	target	type
    //   0	81	614	java/lang/IndexOutOfBoundsException
    //   90	111	614	java/lang/IndexOutOfBoundsException
    //   111	142	614	java/lang/IndexOutOfBoundsException
    //   142	151	614	java/lang/IndexOutOfBoundsException
    //   160	180	614	java/lang/IndexOutOfBoundsException
    //   180	213	614	java/lang/IndexOutOfBoundsException
    //   218	283	614	java/lang/IndexOutOfBoundsException
    //   283	377	614	java/lang/IndexOutOfBoundsException
    //   382	423	614	java/lang/IndexOutOfBoundsException
    //   423	525	614	java/lang/IndexOutOfBoundsException
    //   525	533	614	java/lang/IndexOutOfBoundsException
    //   534	572	614	java/lang/IndexOutOfBoundsException
    //   575	597	614	java/lang/IndexOutOfBoundsException
    //   603	611	614	java/lang/IndexOutOfBoundsException
    //   622	768	614	java/lang/IndexOutOfBoundsException
  }
  
  public void a(a parama) {
    StringBuilder stringBuilder;
    try {
      if (parama.d().d()) {
        StringBuilder stringBuilder1;
        switch (parama.c()) {
          default:
            stringBuilder1 = new StringBuilder();
            this();
            a(stringBuilder1.append("Receive TCP error type of package:").append(parama).toString());
          case 8:
            return;
          case 1:
            g(parama);
          case 3:
            f(parama);
          case 7:
            h(parama);
          case 9:
            d(parama);
          case 10:
            b(parama);
          case 5:
            c(parama);
          case 12:
            break;
        } 
        k(parama);
      } 
    } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
      a("read Exception tcp packet:" + parama.toString() + " IndexOutOfBoundsException error");
    } 
    switch (parama.c()) {
      default:
        stringBuilder = new StringBuilder();
        this();
        a(stringBuilder.append("Received is not the response TCP error type of package:").append(parama).toString());
      case 4:
        e(parama);
      case 7:
        i(parama);
      case 8:
        j(parama);
      case 14:
        l(parama);
      case 12:
        break;
    } 
    k(parama);
  }
  
  public void a(InetAddress paramInetAddress, io.xlink.wifi.sdk.buffer.a parama, int paramInt, SocketAddress paramSocketAddress) {
    a a1;
    if ((parama.b()).length <= 5) {
      a("error data length:" + (parama.b()).length + " buf:" + b.a(parama.b()));
      return;
    } 
    b b = new b(parama.b());
    if (b.b() && b.a() <= (parama.b()).length - 5 && b.a() != 0) {
      a1 = new a(new io.xlink.wifi.sdk.buffer.a(parama.b(), 5), b);
      a1.a(paramInetAddress);
      a1.a = paramInt;
      a1.a(paramSocketAddress);
      try {
        m(a1);
      } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
        a("udp dispatchPacket error  :" + indexOutOfBoundsException.toString() + "   dataBuff：" + a1.toString());
      } 
      return;
    } 
    a("parse data error header length：" + b.a() + " read data length：" + (a1.b()).length + " type:" + b.c());
  }
  
  private static class a {
    private static c a = new c();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/decoder/c.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */