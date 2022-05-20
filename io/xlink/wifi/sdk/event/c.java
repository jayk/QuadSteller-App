package io.xlink.wifi.sdk.event;

import android.os.Handler;
import android.os.Message;
import io.xlink.wifi.sdk.XDevice;
import io.xlink.wifi.sdk.bean.EventNotify;
import io.xlink.wifi.sdk.bean.a;
import io.xlink.wifi.sdk.listener.ScanDeviceListener;
import io.xlink.wifi.sdk.listener.XlinkNetListener;
import io.xlink.wifi.sdk.util.MyLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class c {
  private static HashMap<String, XlinkNetListener> a = new HashMap<String, XlinkNetListener>();
  
  private static final Handler b = new Handler() {
      public void handleMessage(Message param1Message) {
        Iterator<XlinkNetListener> iterator2;
        XlinkNetListener xlinkNetListener;
        Iterator<XlinkNetListener> iterator3;
        EventNotify eventNotify;
        Iterator<XlinkNetListener> iterator4;
        XDevice xDevice;
        super.handleMessage(param1Message);
        if (param1Message.obj instanceof a) {
          iterator3 = (Iterator<XlinkNetListener>)param1Message.obj;
        } else {
          iterator3 = null;
        } 
        switch (param1Message.what) {
          default:
            return;
          case 1:
            if (c.a() != null && param1Message.obj != null && param1Message.obj instanceof XDevice)
              c.a().onGotDeviceByScan((XDevice)param1Message.obj); 
          case 2:
            iterator2 = c.b().iterator();
            while (true) {
              if (iterator2.hasNext())
                ((XlinkNetListener)iterator2.next()).onDataPointUpdate(((a)iterator3).a, ((a)iterator3).b, ((a)iterator3).d, ((a)iterator3).e, ((a)iterator3).c); 
            } 
          case 3:
            iterator4 = c.b().iterator();
            while (true) {
              if (iterator4.hasNext()) {
                xlinkNetListener = iterator4.next();
                if (((a)iterator3).h) {
                  xlinkNetListener.onRecvPipeSyncData(((a)iterator3).a, ((a)iterator3).g, ((a)iterator3).f);
                  continue;
                } 
                xlinkNetListener.onRecvPipeData(((a)iterator3).a, ((a)iterator3).g, ((a)iterator3).f);
              } 
            } 
          case 4:
            xDevice = (XDevice)((Message)xlinkNetListener).obj;
            iterator3 = c.b().iterator();
            while (true) {
              if (iterator3.hasNext())
                ((XlinkNetListener)iterator3.next()).onDeviceStateChanged(xDevice, ((Message)xlinkNetListener).arg1); 
            } 
          case 6:
            eventNotify = (EventNotify)((Message)xlinkNetListener).obj;
            iterator1 = c.b().iterator();
            while (true) {
              if (iterator1.hasNext())
                ((XlinkNetListener)iterator1.next()).onEventNotify(eventNotify); 
            } 
          case 5:
            break;
        } 
        if (c.c().size() == 0)
          MyLog.e("XlinkNetDispatcher", "XlinkNetListener not found"); 
        int i = ((Message)iterator1).arg1;
        int j = ((Message)iterator1).arg2;
        Iterator<XlinkNetListener> iterator1 = c.b().iterator();
        while (true) {
          if (iterator1.hasNext()) {
            XlinkNetListener xlinkNetListener1 = iterator1.next();
            switch (i) {
              case 2:
              case 0:
                continue;
              case 1:
                xlinkNetListener1.onStart(j);
                continue;
              case 3:
                xlinkNetListener1.onLocalDisconnect(j);
                continue;
              case 4:
                xlinkNetListener1.onLogin(0);
                continue;
              case 6:
                xlinkNetListener1.onDisconnect(j);
                continue;
              case 5:
                break;
            } 
            xlinkNetListener1.onLogin(j);
          } 
        } 
      }
    };
  
  private static ScanDeviceListener c;
  
  private static ArrayList<XlinkNetListener> d = new ArrayList<XlinkNetListener>();
  
  public static void a(int paramInt1, int paramInt2) {
    Message message = new Message();
    message.what = 5;
    message.arg1 = paramInt1;
    message.arg2 = paramInt2;
    b.sendMessage(message);
  }
  
  public static void a(int paramInt, XDevice paramXDevice) {
    MyLog.e("XlinkNetDispatcher", "device :" + paramXDevice.getMacAddress() + " status:" + paramInt);
    Message message = new Message();
    message.what = 4;
    message.arg1 = paramInt;
    message.obj = paramXDevice;
    b.sendMessage(message);
  }
  
  public static void a(XDevice paramXDevice) {
    if (c != null) {
      Message message = new Message();
      message.what = 1;
      message.obj = paramXDevice;
      b.sendMessage(message);
    } 
  }
  
  public static void a(XDevice paramXDevice, int paramInt1, Object paramObject, int paramInt2, int paramInt3) {
    Message message = new Message();
    message.what = 2;
    a a = new a();
    a.a = paramXDevice;
    a.b = paramInt1;
    a.d = paramObject;
    a.e = paramInt2;
    a.c = paramInt3;
    message.obj = a;
    b.sendMessage(message);
  }
  
  public static void a(EventNotify paramEventNotify) {
    MyLog.e("XlinkNetDispatcher", "eventNotify :" + paramEventNotify.toString());
    Message message = new Message();
    message.what = 6;
    message.obj = paramEventNotify;
    b.sendMessage(message);
  }
  
  public static void a(ScanDeviceListener paramScanDeviceListener) {
    c = paramScanDeviceListener;
  }
  
  public static void a(String paramString) {
    if (a.get(paramString) != null) {
      d.remove(a.get(paramString));
      a.remove(paramString);
    } 
  }
  
  public static void a(String paramString, XlinkNetListener paramXlinkNetListener) {
    if (a.get(paramString) != null) {
      d.remove(a.get(paramString));
      a.put(paramString, paramXlinkNetListener);
      d.add(paramXlinkNetListener);
      return;
    } 
    a.put(paramString, paramXlinkNetListener);
    d.add(paramXlinkNetListener);
  }
  
  public static void a(boolean paramBoolean, XDevice paramXDevice, byte paramByte, byte[] paramArrayOfbyte) {
    Message message = new Message();
    message.what = 3;
    a a = new a();
    a.h = paramBoolean;
    a.a = paramXDevice;
    a.f = paramArrayOfbyte;
    a.g = (byte)paramByte;
    message.obj = a;
    b.sendMessage(message);
  }
  
  private static ArrayList<XlinkNetListener> d() {
    // Byte code:
    //   0: ldc io/xlink/wifi/sdk/event/c
    //   2: monitorenter
    //   3: getstatic io/xlink/wifi/sdk/event/c.d : Ljava/util/ArrayList;
    //   6: astore_0
    //   7: aload_0
    //   8: monitorenter
    //   9: getstatic io/xlink/wifi/sdk/event/c.d : Ljava/util/ArrayList;
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: ldc io/xlink/wifi/sdk/event/c
    //   17: monitorexit
    //   18: aload_1
    //   19: areturn
    //   20: astore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_1
    //   24: athrow
    //   25: astore_0
    //   26: ldc io/xlink/wifi/sdk/event/c
    //   28: monitorexit
    //   29: aload_0
    //   30: athrow
    // Exception table:
    //   from	to	target	type
    //   3	9	25	finally
    //   9	15	20	finally
    //   21	23	20	finally
    //   23	25	25	finally
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/event/c.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */