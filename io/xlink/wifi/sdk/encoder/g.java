package io.xlink.wifi.sdk.encoder;

import io.xlink.wifi.sdk.XDevice;
import io.xlink.wifi.sdk.a;
import io.xlink.wifi.sdk.event.a;
import io.xlink.wifi.sdk.manage.c;
import io.xlink.wifi.sdk.util.MyLog;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class g {
  public static final ScheduledExecutorService a = Executors.newSingleThreadScheduledExecutor();
  
  private d b;
  
  private a c;
  
  private int d = 10;
  
  private Runnable e;
  
  private ScheduledFuture<?> f;
  
  public g(d paramd) {
    this(paramd, null, 0);
  }
  
  public g(d paramd, a parama) {
    this(paramd, parama, 10);
  }
  
  public g(d paramd, a parama, int paramInt) {
    this.b = paramd;
    this.c = parama;
    this.d = paramInt;
  }
  
  public XDevice a() {
    return this.b.b();
  }
  
  public void a(String paramString) {
    this.b.a(paramString);
  }
  
  public d b() {
    return this.b;
  }
  
  public void c() {
    e.a(f());
    d();
  }
  
  public void d() {
    if (this.f != null) {
      this.f.cancel(true);
      this.f = null;
    } 
  }
  
  public void e() {
    if (g() != null && this.d != 0) {
      if (this.f != null) {
        this.f.cancel(true);
        this.f = null;
      } 
      this.e = new Runnable(this) {
          public void run() {
            this.a.c();
            e e = e.b(-100);
            e.a = g.a(this.a).b();
            e.c = g.a(this.a).g();
            this.a.g().onResponse(e);
            if (g.a(this.a).a() == 1) {
              switch (g.a(this.a).e()) {
                default:
                  return;
                case 8:
                  break;
              } 
              int i = (c.a().b(g.a(this.a).b().getMacAddress())).a;
              if (i >= 5) {
                (g.a(this.a).b()).a = 0;
                MyLog.e("TIMEOUT", "TYPE_LOCAL_PIPE::timeoutCount:>5 reconnectDevice");
                a.a().a(g.a(this.a).b());
              } 
              XDevice xDevice = g.a(this.a).b();
              xDevice.a++;
              MyLog.e("TIMEOUT", "TYPE_LOCAL_PIPE::timeoutCount:" + i);
            } 
          }
        };
      this.f = a.schedule(this.e, this.d, TimeUnit.SECONDS);
    } 
  }
  
  public String f() {
    return this.b.f();
  }
  
  public a g() {
    return this.c;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/encoder/g.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */