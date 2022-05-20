package io.xlink.wifi.sdk;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import io.xlink.wifi.sdk.encoder.g;
import io.xlink.wifi.sdk.event.c;
import io.xlink.wifi.sdk.global.a;
import io.xlink.wifi.sdk.manage.c;
import io.xlink.wifi.sdk.tcp.a;
import io.xlink.wifi.sdk.udp.a;
import io.xlink.wifi.sdk.util.MyLog;
import io.xlink.wifi.sdk.util.b;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public final class XlinkUdpService extends Service {
  public static boolean b;
  
  private static String f = b.a.getPackageName() + "-upd-keep";
  
  private static XlinkUdpService g;
  
  public a a;
  
  public long c;
  
  public Timer d;
  
  private DatagramSocket e;
  
  private boolean h = false;
  
  private boolean i = false;
  
  private long j;
  
  private TimerTask k;
  
  private BroadcastReceiver l = new BroadcastReceiver(this) {
      public void onReceive(Context param1Context, Intent param1Intent) {
        String str = param1Intent.getAction();
        MyLog.e("UDPService", "UDP mReceiver " + new Date() + "  " + System.currentTimeMillis());
        if (str.equals(XlinkUdpService.g()))
          c.a().d(); 
      }
    };
  
  private final String m = "UDPService";
  
  static {
    b = false;
  }
  
  public static boolean a() {
    return (b() == null) ? false : (b()).h;
  }
  
  public static XlinkUdpService b() {
    return g;
  }
  
  private void h() {
    if (b || this.h) {
      a("upd connecting bind ...return ");
      return;
    } 
    b = true;
    b.b(new Runnable(this) {
          public void run() {
            try {
              this.a.a("bind udp ...");
              XlinkUdpService xlinkUdpService2 = this.a;
              DatagramSocket datagramSocket2 = new DatagramSocket();
              this((SocketAddress)null);
              XlinkUdpService.a(xlinkUdpService2, datagramSocket2);
              XlinkUdpService.a(this.a).setBroadcast(true);
              DatagramSocket datagramSocket1 = XlinkUdpService.a(this.a);
              InetSocketAddress inetSocketAddress = new InetSocketAddress();
              this(0);
              datagramSocket1.bind(inetSocketAddress);
              this.a.f();
              a.g = XlinkUdpService.a(this.a).getLocalPort();
              XlinkUdpService xlinkUdpService1 = this.a;
              StringBuilder stringBuilder = new StringBuilder();
              this();
              xlinkUdpService1.a(stringBuilder.append("bind udp prot:").append(a.g).toString());
              return;
            } catch (SocketException socketException) {
              socketException.printStackTrace();
              this.a.a("bind udp  fail ");
              c.a(2, -1);
              return;
            } finally {
              XlinkUdpService.b = false;
            } 
          }
        });
  }
  
  public void a(g paramg) {
    a.a().b(paramg);
  }
  
  public void a(String paramString) {
    MyLog.e("UDPService", paramString);
  }
  
  public void a(boolean paramBoolean, int paramInt) {
    if (this.h) {
      this.h = false;
      if (this.e != null)
        this.e.close(); 
      if (this.a != null)
        this.a.c(); 
      if (paramBoolean) {
        a("upd bind close");
        c.a(3, paramInt);
      } 
    } 
  }
  
  public boolean c() {
    if (System.currentTimeMillis() - this.j > 15000L)
      this.i = false; 
    return this.i;
  }
  
  public void d() {
    if (!c()) {
      this.i = true;
      MyLog.e("UDPService", "local start KeepAlive ");
      this.c = System.currentTimeMillis();
      if (this.k != null) {
        this.k.cancel();
        this.k = null;
      } 
      this.j = System.currentTimeMillis();
      this.k = new TimerTask(this) {
          public void run() {
            c.a().d();
            XlinkUdpService.a(this.a, System.currentTimeMillis());
          }
        };
      this.d.schedule(this.k, 10000L, 10000L);
    } 
  }
  
  public void e() {
    if (this.k != null) {
      this.k.cancel();
      this.k = null;
    } 
  }
  
  public void f() {
    if (!a.a().c())
      a.a().b(); 
    a.a().a(this, this.e);
    this.a = new a(g, this.e);
    this.a.b();
    this.h = true;
    a("udp bind succeed");
    c.a(1, 0);
  }
  
  public IBinder onBind(Intent paramIntent) {
    return null;
  }
  
  public void onCreate() {
    super.onCreate();
    a("UPD service onCreate");
    g = this;
    this.d = new Timer();
  }
  
  public void onDestroy() {
    a(true, -2);
    if (this.l != null)
      try {
        unregisterReceiver(this.l);
      } catch (Exception exception) {} 
    this.d.cancel();
    this.d = null;
    a("upd service onDestroy");
    startService(new Intent((Context)this, XlinkUdpService.class));
    a("upd RestartService... on onDestroy");
    super.onDestroy();
  }
  
  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
    a("UdpService onStartCommand connected:" + this.h);
    if (!this.h)
      h(); 
    return 1;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/XlinkUdpService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */