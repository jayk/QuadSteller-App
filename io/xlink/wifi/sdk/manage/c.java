package io.xlink.wifi.sdk.manage;

import io.xlink.wifi.sdk.XDevice;
import io.xlink.wifi.sdk.XlinkUdpService;
import io.xlink.wifi.sdk.a;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class c {
  public static final LinkedHashMap<String, XDevice> a = new LinkedHashMap<String, XDevice>();
  
  public static final ArrayList<XDevice> b = new ArrayList<XDevice>();
  
  private static c c;
  
  private boolean d = false;
  
  public static c a() {
    if (c == null)
      c = new c(); 
    return c;
  }
  
  public XDevice a(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual c : ()Ljava/util/ArrayList;
    //   4: invokevirtual iterator : ()Ljava/util/Iterator;
    //   7: astore_2
    //   8: aload_2
    //   9: invokeinterface hasNext : ()Z
    //   14: ifeq -> 37
    //   17: aload_2
    //   18: invokeinterface next : ()Ljava/lang/Object;
    //   23: checkcast io/xlink/wifi/sdk/XDevice
    //   26: astore_3
    //   27: aload_3
    //   28: invokevirtual getDeviceId : ()I
    //   31: iload_1
    //   32: if_icmpne -> 8
    //   35: aload_3
    //   36: areturn
    //   37: aconst_null
    //   38: astore_3
    //   39: goto -> 35
  }
  
  public void a(String paramString) {
    this.d = true;
    XDevice xDevice = a.get(paramString);
    if (xDevice != null)
      xDevice.a = 0; 
  }
  
  public boolean a(XDevice paramXDevice) {
    return (paramXDevice == null || paramXDevice.getAddress() == null || paramXDevice.getMacAddress() == null || paramXDevice.getMacAddress().equals(""));
  }
  
  public XDevice b(String paramString) {
    return a.get(paramString);
  }
  
  public void b() {
    this.d = true;
    a.clear();
  }
  
  public void b(XDevice paramXDevice) {
    this.d = true;
    a.put(paramXDevice.getMacAddress(), paramXDevice);
  }
  
  public ArrayList<XDevice> c() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield d : Z
    //   6: ifeq -> 85
    //   9: getstatic io/xlink/wifi/sdk/manage/c.b : Ljava/util/ArrayList;
    //   12: invokevirtual clear : ()V
    //   15: getstatic io/xlink/wifi/sdk/manage/c.a : Ljava/util/LinkedHashMap;
    //   18: astore_1
    //   19: aload_1
    //   20: monitorenter
    //   21: getstatic io/xlink/wifi/sdk/manage/c.a : Ljava/util/LinkedHashMap;
    //   24: invokevirtual entrySet : ()Ljava/util/Set;
    //   27: invokeinterface iterator : ()Ljava/util/Iterator;
    //   32: astore_2
    //   33: aload_2
    //   34: invokeinterface hasNext : ()Z
    //   39: ifeq -> 78
    //   42: aload_2
    //   43: invokeinterface next : ()Ljava/lang/Object;
    //   48: checkcast java/util/Map$Entry
    //   51: astore_3
    //   52: getstatic io/xlink/wifi/sdk/manage/c.b : Ljava/util/ArrayList;
    //   55: aload_3
    //   56: invokeinterface getValue : ()Ljava/lang/Object;
    //   61: invokevirtual add : (Ljava/lang/Object;)Z
    //   64: pop
    //   65: goto -> 33
    //   68: astore_3
    //   69: aload_1
    //   70: monitorexit
    //   71: aload_3
    //   72: athrow
    //   73: astore_1
    //   74: aload_0
    //   75: monitorexit
    //   76: aload_1
    //   77: athrow
    //   78: aload_1
    //   79: monitorexit
    //   80: aload_0
    //   81: iconst_0
    //   82: putfield d : Z
    //   85: getstatic io/xlink/wifi/sdk/manage/c.b : Ljava/util/ArrayList;
    //   88: astore_1
    //   89: aload_0
    //   90: monitorexit
    //   91: aload_1
    //   92: areturn
    // Exception table:
    //   from	to	target	type
    //   2	21	73	finally
    //   21	33	68	finally
    //   33	65	68	finally
    //   69	71	68	finally
    //   71	73	73	finally
    //   78	80	68	finally
    //   80	85	73	finally
    //   85	89	73	finally
  }
  
  public void c(XDevice paramXDevice) {
    this.d = true;
    a.remove(paramXDevice.getMacAddress());
    a.put(paramXDevice.getMacAddress(), paramXDevice);
  }
  
  public void c(String paramString) {
    this.d = true;
    a.remove(paramString);
  }
  
  public void d() {
    if (a.size() == 0) {
      XlinkUdpService.b().e();
      return;
    } 
    Iterator<XDevice> iterator = c().iterator();
    boolean bool = false;
    while (iterator.hasNext()) {
      XDevice xDevice = iterator.next();
      if (xDevice.isLAN()) {
        bool = true;
        xDevice.checkKeepAlive();
      } 
    } 
    if (!bool)
      XlinkUdpService.b().e(); 
  }
  
  public void e() {
    for (XDevice xDevice : c()) {
      if (xDevice.isLAN()) {
        xDevice = a.a().a(xDevice, -1);
        c(xDevice);
        a.a().a(xDevice);
      } 
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/manage/c.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */