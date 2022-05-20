package io.xlink.wifi.sdk.tcp;

import io.xlink.wifi.sdk.XlinkTcpService;
import io.xlink.wifi.sdk.XlinkUdpService;
import io.xlink.wifi.sdk.encoder.d;
import io.xlink.wifi.sdk.encoder.e;
import io.xlink.wifi.sdk.encoder.g;
import io.xlink.wifi.sdk.util.MyLog;
import io.xlink.wifi.sdk.util.b;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class a {
  private static a d;
  
  public Boolean a;
  
  private Thread b;
  
  private final BlockingQueue<g> c = new ArrayBlockingQueue<g>(500, true);
  
  private XlinkTcpService e;
  
  private OutputStream f;
  
  private XlinkUdpService g;
  
  private DatagramSocket h;
  
  public static a a() {
    if (d == null)
      d = new a(); 
    return d;
  }
  
  private void a(String paramString) {
    MyLog.e(" Write", paramString);
  }
  
  private void e() {
    this.b = new Thread(this) {
        public void run() {
          a.a(this.a);
        }
      };
    this.b.setName("Packet Writer");
    this.b.setDaemon(true);
    this.a = Boolean.valueOf(false);
    this.b.start();
  }
  
  private void e(g paramg) {
    BlockingQueue<g> blockingQueue;
    if (!this.a.booleanValue()) {
      try {
        this.c.put(paramg);
        blockingQueue = this.c;
        /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/util/concurrent/BlockingQueue<ObjectType{io/xlink/wifi/sdk/encoder/g}>}, name=null} */
        try {
          this.c.notifyAll();
          /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/util/concurrent/BlockingQueue<ObjectType{io/xlink/wifi/sdk/encoder/g}>}, name=null} */
          return;
        } finally {}
      } catch (InterruptedException interruptedException) {
        interruptedException.printStackTrace();
        return;
      } 
    } else {
      return;
    } 
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/util/concurrent/BlockingQueue<ObjectType{io/xlink/wifi/sdk/encoder/g}>}, name=null} */
    throw interruptedException;
  }
  
  private g f() {
    g g1 = null;
    g g2 = null;
    while (!this.a.booleanValue()) {
      g g = this.c.poll();
      g2 = g;
      if (g == null)
        try {
          BlockingQueue<g> blockingQueue;
          g g4;
          synchronized (this.c) {
            this.c.wait();
            g4 = g;
          } 
        } catch (InterruptedException interruptedException) {
          g2 = g;
        }  
    } 
    g g3 = g1;
    if (g2 != null)
      g3 = g2; 
    return g3;
  }
  
  private void g() {
    while (!this.a.booleanValue()) {
      g g = f();
      if (g == null) {
        a("writePackets task null");
        continue;
      } 
      if (g.g() != null && g.f() != null) {
        e.a(g.f(), g);
        g.e();
      } 
      d d = g.b();
      if (d == null) {
        a("writePackets EncodeBuffer null");
        continue;
      } 
      if (d.a() == 2) {
        c(g);
        continue;
      } 
      if (d.a() == 1)
        d(g); 
    } 
    a("write therad logout done:" + this.a);
  }
  
  public void a(XlinkTcpService paramXlinkTcpService, OutputStream paramOutputStream) {
    this.e = paramXlinkTcpService;
    this.f = paramOutputStream;
  }
  
  public void a(XlinkUdpService paramXlinkUdpService, DatagramSocket paramDatagramSocket) {
    this.g = paramXlinkUdpService;
    this.h = paramDatagramSocket;
  }
  
  public void a(g paramg) {
    e(paramg);
  }
  
  public void b() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield b : Ljava/lang/Thread;
    //   6: ifnonnull -> 16
    //   9: aload_0
    //   10: invokespecial e : ()V
    //   13: aload_0
    //   14: monitorexit
    //   15: return
    //   16: aload_0
    //   17: getfield b : Ljava/lang/Thread;
    //   20: invokevirtual isAlive : ()Z
    //   23: ifeq -> 40
    //   26: aload_0
    //   27: ldc 'startup fail writerThread.isAlive() is true'
    //   29: invokespecial a : (Ljava/lang/String;)V
    //   32: goto -> 13
    //   35: astore_1
    //   36: aload_0
    //   37: monitorexit
    //   38: aload_1
    //   39: athrow
    //   40: aload_0
    //   41: getfield b : Ljava/lang/Thread;
    //   44: invokevirtual interrupt : ()V
    //   47: aload_0
    //   48: aconst_null
    //   49: putfield b : Ljava/lang/Thread;
    //   52: aload_0
    //   53: invokespecial e : ()V
    //   56: goto -> 13
    // Exception table:
    //   from	to	target	type
    //   2	13	35	finally
    //   16	32	35	finally
    //   40	56	35	finally
  }
  
  public void b(g paramg) {
    b.b(new Runnable(this, paramg) {
          public void run() {
            a.a(this.b, this.a);
          }
        });
  }
  
  public void c(g paramg) {
    if (this.f != null)
      try {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        a(stringBuilder.append("TCP send :").append(paramg.b()).toString());
        this.f.write((paramg.b()).b.a());
      } catch (IOException iOException) {
        XlinkTcpService.a().a(true, -1, true);
      }  
  }
  
  public boolean c() {
    return (this.b != null && this.b.isAlive());
  }
  
  public void d() {
    this.b = null;
    this.a = Boolean.valueOf(true);
    synchronized (this.c) {
      this.c.notifyAll();
      this.c.clear();
      return;
    } 
  }
  
  public void d(g paramg) {
    if (this.h != null && paramg != null) {
      d d = paramg.b();
      DatagramPacket datagramPacket = new DatagramPacket(d.b.a(), (d.b.a()).length);
      datagramPacket.setAddress(d.d());
      datagramPacket.setPort(d.c());
      String str = "";
      if (paramg.a() != null)
        str = paramg.a().toString(); 
      a("udp sendData device:" + str + " len :" + datagramPacket.getLength() + " type:" + d.e() + " addr:" + d.d());
      try {
        this.h.send(datagramPacket);
        int i = d.e();
        if (i == 8)
          try {
            Thread.sleep(100L);
          } catch (InterruptedException interruptedException) {} 
      } catch (IOException iOException) {
        a("udp socket error");
        XlinkUdpService.b().a(true, -1);
      } 
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/tcp/a.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */