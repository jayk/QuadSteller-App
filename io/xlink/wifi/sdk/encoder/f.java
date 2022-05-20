package io.xlink.wifi.sdk.encoder;

import io.xlink.wifi.sdk.DataPoint;
import io.xlink.wifi.sdk.XDevice;
import io.xlink.wifi.sdk.XlinkUdpService;
import io.xlink.wifi.sdk.buffer.a;
import io.xlink.wifi.sdk.buffer.b;
import io.xlink.wifi.sdk.event.a;
import io.xlink.wifi.sdk.global.a;
import io.xlink.wifi.sdk.manage.b;
import io.xlink.wifi.sdk.util.MyLog;
import io.xlink.wifi.sdk.util.b;
import java.nio.charset.Charset;

public class f {
  private static f a;
  
  public static int a(int paramInt) {
    if (paramInt <= 8)
      return 1; 
    if (paramInt % 8 == 0) {
      paramInt /= 8;
      return paramInt;
    } 
    return paramInt / 8 + 1;
  }
  
  public static int a(DataPoint paramDataPoint) {
    int i = 1;
    switch (paramDataPoint.getType()) {
      default:
        i = 0;
      case 1:
      case 2:
        return i;
      case 3:
        i = 2;
      case 4:
        i = 4;
      case 5:
        break;
    } 
    i = (paramDataPoint.getObjectValue().toString().getBytes()).length + 2;
  }
  
  public static DataPoint a(Object paramObject, DataPoint paramDataPoint) {
    try {
      switch (paramDataPoint.getType()) {
        default:
          paramObject = null;
          paramDataPoint.setValue(paramObject);
        case 1:
          paramObject = paramObject;
          paramDataPoint.setValue(paramObject);
        case 3:
          paramObject = paramObject;
          paramDataPoint.setValue(paramObject);
        case 4:
          paramObject = paramObject;
          paramDataPoint.setValue(paramObject);
        case 2:
          paramObject = paramObject;
          paramDataPoint.setValue(paramObject);
        case 5:
          break;
      } 
      paramObject = paramObject;
      paramDataPoint.setValue(paramObject);
    } catch (Exception exception) {
      paramDataPoint = null;
    } 
    return paramDataPoint;
  }
  
  public static b a(int paramInt, DataPoint paramDataPoint) {
    b b = new b(a(paramDataPoint) + paramInt);
    for (byte b1 = 1; b1 <= paramInt; b1++) {
      if (a(paramDataPoint.getKey()) == b1) {
        b.a(b.a(paramDataPoint.getKey() % 8, (byte)0));
      } else {
        b.a((byte)0);
      } 
    } 
    return a(b, paramDataPoint.getType(), paramDataPoint.getObjectValue());
  }
  
  public static b a(b paramb, int paramInt, Object paramObject) {
    switch (paramInt) {
      default:
        return paramb;
      case 1:
        paramb.a(((Boolean)paramObject).booleanValue());
      case 3:
        paramb.a(((Integer)paramObject).intValue());
      case 4:
        paramb.b(((Integer)paramObject).intValue());
      case 2:
        paramb.a(((Byte)paramObject).byteValue());
      case 5:
        break;
    } 
    paramb.a((paramObject.toString().getBytes()).length);
    paramb.a(paramObject.toString().getBytes());
  }
  
  public static f a() {
    if (a == null)
      a = new f(); 
    return a;
  }
  
  public static Object a(a parama, int paramInt) {
    a a1 = null;
    switch (paramInt) {
      default:
        return a1;
      case 1:
        return Boolean.valueOf(parama.c());
      case 3:
        return Short.valueOf(parama.g());
      case 4:
        return Integer.valueOf(parama.f());
      case 2:
        return Byte.valueOf(parama.d());
      case 5:
        break;
    } 
    return new String(parama.a(parama.g()), Charset.forName("UTF-8"));
  }
  
  public d a(byte paramByte) {
    a("tcp sendDisconnect  reason :" + paramByte);
    d d = new d(1, 14, false);
    d.b.a(paramByte);
    return d;
  }
  
  public d a(int paramInt1, byte paramByte, byte[] paramArrayOfbyte, a parama, int paramInt2) {
    d d = new d(paramArrayOfbyte.length + 7, 7, false);
    d.b.b(paramInt1);
    d.h();
    d.b.a(paramByte);
    d.b.a(paramArrayOfbyte);
    return d;
  }
  
  public d a(int paramInt, String paramString) {
    byte[] arrayOfByte;
    byte b = 0;
    if (paramInt == 1) {
      arrayOfByte = b.c(paramString);
    } else {
      arrayOfByte = b.d((String)arrayOfByte);
    } 
    d d = new d(arrayOfByte.length + 4, 1, false);
    d.a(b.b());
    d.b.c(2);
    d.b.a(a.g);
    if (paramInt == 1) {
      paramInt = b.a(4, b.a(0, (byte)0));
      int j = paramInt;
      d.b.a(j);
      d.b.a(arrayOfByte);
      return d;
    } 
    int i = b;
    if (paramInt == 2) {
      paramInt = b.a(1, (byte)0);
      i = paramInt;
    } 
    d.b.a(i);
    d.b.a(arrayOfByte);
    return d;
  }
  
  public d a(XDevice paramXDevice, byte paramByte, byte[] paramArrayOfbyte) {
    d d = new d(paramArrayOfbyte.length + 5, 8, false);
    d.a(paramXDevice);
    d.a(paramXDevice.getAddress());
    d.b.a(paramXDevice.getSessionId());
    d.h();
    d.b.a(paramByte);
    d.b.a(paramArrayOfbyte);
    return d;
  }
  
  public d a(XDevice paramXDevice, byte paramByte, byte[] paramArrayOfbyte, a parama, int paramInt) {
    d d = new d(paramArrayOfbyte.length + 7, 7, false);
    d.a(paramXDevice);
    d.b.b(paramXDevice.getDeviceId());
    d.h();
    d.b.a(paramByte);
    d.b.a(paramArrayOfbyte);
    return d;
  }
  
  public d a(XDevice paramXDevice, int paramInt) {
    d d = new d(22, 2, false);
    d.a(paramXDevice);
    d.a(paramXDevice.getAddress());
    d.a(paramXDevice.getPort());
    d.a(paramXDevice.getMacAddress());
    d.b.a(paramXDevice.getVersion());
    d.b.a(b.a(paramInt));
    d.b.a(a.g);
    d.b.c(0);
    d.b.a(paramXDevice.getTimeout());
    return d;
  }
  
  public d a(XDevice paramXDevice, int paramInt, boolean paramBoolean, a parama) {
    byte[] arrayOfByte1 = b.d(paramXDevice.getProductId());
    byte[] arrayOfByte2 = b.c(paramXDevice.getMacAddress());
    byte[] arrayOfByte3 = b.a(paramInt);
    d d = new d(59, 9, false);
    d.a(paramXDevice);
    d.b.a(arrayOfByte1.length);
    d.b.a(arrayOfByte1);
    d.b.a(arrayOfByte2);
    d.b.a(arrayOfByte3);
    d.h();
    if (paramBoolean) {
      paramInt = 3;
      int j = paramInt;
      d.b.a(j);
      return d;
    } 
    paramInt = 2;
    int i = paramInt;
    d.b.a(i);
    return d;
  }
  
  public d a(XDevice paramXDevice, DataPoint paramDataPoint) {
    b b = a(b.a().a(paramXDevice.getProductId()), paramDataPoint);
    d d = new d((b.a()).length + 5, 4, false);
    d.a(paramXDevice);
    d.a(paramXDevice.getAddress());
    d.b.a(paramXDevice.getSessionId());
    d.h();
    d.b.a(b.a(1, (byte)0));
    d.b.a(b.a());
    return d;
  }
  
  public d a(XDevice paramXDevice, String paramString) {
    d d = new d(22, 2, false);
    d.a(paramXDevice);
    d.a(paramXDevice.getAddress());
    d.a(paramXDevice.getPort());
    d.a(paramXDevice.getMacAddress());
    d.b.a(paramXDevice.getVersion());
    d.b.a(b.b(paramString));
    d.b.a(a.g);
    d.b.c(0);
    d.b.a(paramXDevice.getTimeout());
    return d;
  }
  
  public d a(XDevice paramXDevice, String paramString1, String paramString2) {
    d d = new d(37, 9, false);
    d.a(paramXDevice.getAddress());
    d.a(paramXDevice);
    d.h();
    d.b.c(0);
    d.b.a(a.g);
    d.b.a(b.b(paramString1));
    d.b.a(b.b(paramString2));
    return d;
  }
  
  public d a(XDevice paramXDevice, String paramString, boolean paramBoolean, a parama) {
    byte[] arrayOfByte3 = b.d(paramXDevice.getProductId());
    byte[] arrayOfByte2 = b.c(paramXDevice.getMacAddress());
    byte[] arrayOfByte1 = b.b(paramString);
    d d = new d(59, 9, false);
    d.a(paramXDevice);
    d.b.a(arrayOfByte3.length);
    d.b.a(arrayOfByte3);
    d.b.a(arrayOfByte2);
    d.b.a(arrayOfByte1);
    d.h();
    if (paramBoolean) {
      d.b.a(b.a(0, (byte)0));
      return d;
    } 
    d.b.a((byte)0);
    return d;
  }
  
  public d a(a parama) {
    byte[] arrayOfByte = b.d(a.a());
    b b = new b(arrayOfByte.length);
    b.a(arrayOfByte);
    d d = new d(b);
    d.a("999");
    return d;
  }
  
  public void a(XDevice paramXDevice) {
    d d = new d(3, 3, false);
    d.a(paramXDevice);
    d.a(paramXDevice.getAddress());
    d.b.a(paramXDevice.getSessionId());
    d.b.c(0);
    g g = new g(d);
    XlinkUdpService.b().a(g);
  }
  
  public void a(String paramString) {
    MyLog.e("PacketEncoder", paramString + "");
  }
  
  public d b() {
    return new d(0, 13, false);
  }
  
  public d b(int paramInt, String paramString) {
    a("   doLogin  --   id :" + paramInt + " authorize :" + paramString);
    byte[] arrayOfByte = b.d(paramString);
    d d = new d(arrayOfByte.length + 10, 1, false);
    d.b.c(2);
    d.b.b(paramInt);
    d.b.a(arrayOfByte.length);
    d.b.a(arrayOfByte);
    d.b.c(0);
    d.b.a(a.e);
    return d;
  }
  
  public d b(XDevice paramXDevice) {
    d d = new d(2, 13, false);
    d.a(paramXDevice.getAddress());
    d.a(paramXDevice);
    d.b.a(paramXDevice.getSessionId());
    return d;
  }
  
  public d b(XDevice paramXDevice, int paramInt) {
    d d = new d(9, 11, false);
    d.a(paramXDevice.getAddress());
    d.a(paramXDevice);
    d.h();
    d.b.c(0);
    d.b.a(a.g);
    d.b.b(paramInt);
    return d;
  }
  
  public d b(XDevice paramXDevice, DataPoint paramDataPoint) {
    b b = a(b.a().a(paramXDevice.getProductId()), paramDataPoint);
    d d = new d((b.a()).length + 7, 3, false);
    d.a(paramXDevice);
    d.b.b(paramXDevice.getDeviceId());
    d.h();
    d.b.a(b.a(1, (byte)0));
    d.b.a(b.a());
    return d;
  }
  
  public d b(XDevice paramXDevice, String paramString1, String paramString2) {
    d d = new d(39, 5, false);
    d.a(paramXDevice);
    d.b.b(paramXDevice.getDeviceId());
    d.h();
    d.b.c(0);
    d.b.a(b.b(paramString1));
    d.b.a(b.b(paramString2));
    return d;
  }
  
  public d c(XDevice paramXDevice) {
    if (paramXDevice.getSessionId() < 0)
      return null; 
    d d = new d(2, 14, false);
    d.a(paramXDevice.getAddress());
    d.a(paramXDevice);
    d.b.a(paramXDevice.getSessionId());
    return d;
  }
  
  public d c(XDevice paramXDevice, int paramInt) {
    d d = new d(7, 10, false);
    d.a(paramXDevice);
    d.b.b(paramXDevice.getDeviceId());
    d.h();
    d.b.c(paramInt);
    return d;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/encoder/f.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */