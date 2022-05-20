package io.xlink.wifi.sdk;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import io.xlink.wifi.sdk.encoder.d;
import io.xlink.wifi.sdk.encoder.e;
import io.xlink.wifi.sdk.encoder.f;
import io.xlink.wifi.sdk.encoder.g;
import io.xlink.wifi.sdk.event.a;
import io.xlink.wifi.sdk.event.c;
import io.xlink.wifi.sdk.global.a;
import io.xlink.wifi.sdk.manage.a;
import io.xlink.wifi.sdk.tcp.a;
import io.xlink.wifi.sdk.tcp.b;
import io.xlink.wifi.sdk.tcp.c;
import io.xlink.wifi.sdk.util.MyLog;
import io.xlink.wifi.sdk.util.b;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class XlinkTcpService extends Service {
  public static int a;
  
  public static String b;
  
  public static boolean d = false;
  
  public static int j;
  
  private static XlinkTcpService l;
  
  private static String p = b.a.getPackageName() + "-tcp-keep";
  
  private static String q = b.a.getPackageName() + "-tcp-reconnect";
  
  public b c;
  
  public int e = 0;
  
  public long f;
  
  public final Timer g = new Timer();
  
  int h = 0;
  
  boolean i = false;
  
  private final String k = "TCPService";
  
  private Socket m;
  
  private boolean n;
  
  private boolean o;
  
  private boolean r = false;
  
  private BroadcastReceiver s = new BroadcastReceiver(this) {
      public void onReceive(Context param1Context, Intent param1Intent) {
        g g;
        boolean bool = false;
        String str = param1Intent.getAction();
        MyLog.e("TCPService", "TCP mReceiver " + new Date() + "  " + System.currentTimeMillis());
        if (str.equals(XlinkTcpService.e())) {
          if (!XlinkTcpService.c()) {
            XlinkTcpService.a(this.a);
            XlinkTcpService.a(this.a, "tcp !isConnected stopKeepAlive ");
            return;
          } 
          long l1 = (System.currentTimeMillis() - this.a.f) / 1000L;
          if (this.a.e > 3) {
            XlinkTcpService.a(this.a, "tcp 3次心跳超时" + l1 + " cloud service 3 times not response ping app logout ");
            XlinkTcpService.a(this.a);
            this.a.a(true, -2, true);
            return;
          } 
          long l2 = System.currentTimeMillis();
          l1 = this.a.f;
          if (this.a.e != 0 || l2 - l1 >= ((a.e / 2 - 3) * 1000)) {
            XlinkTcpService xlinkTcpService = this.a;
            xlinkTcpService.e++;
            b.a = 0;
            g = c.a().b();
            this.a.a(g);
            XlinkTcpService.a(this.a, "tcp send keep alive packet ");
            this.a.f = System.currentTimeMillis();
          } 
          return;
        } 
        if (str.equals(XlinkTcpService.f())) {
          XlinkTcpService.b(this.a);
          return;
        } 
        if (param1Intent.getAction().equals("android.intent.action.TIME_TICK")) {
          Iterator iterator = ((ActivityManager)b.a.getSystemService("activity")).getRunningServices(2147483647).iterator();
          while (iterator.hasNext()) {
            if ("io.xlink.wifi.sdk.XlinkTcpService".equals(((ActivityManager.RunningServiceInfo)iterator.next()).service.getClassName()))
              bool = true; 
          } 
          XlinkTcpService.a(this.a, "tcp isServiceRunning:" + bool);
          if (!bool) {
            g.startService(new Intent((Context)g, XlinkTcpService.class));
            XlinkTcpService.a(this.a, "tcp RestartService...");
          } 
        } 
      }
    };
  
  private TimerTask t;
  
  private InputStream u;
  
  private OutputStream v;
  
  private InetAddress w;
  
  public static XlinkTcpService a() {
    return l;
  }
  
  private void a(String paramString) {
    MyLog.e("TCPService", paramString);
  }
  
  private void a(Socket paramSocket) throws IOException {
    boolean bool = true;
    if (this.c != null)
      bool = false; 
    try {
      this.u = paramSocket.getInputStream();
      this.v = paramSocket.getOutputStream();
      if (bool) {
        b b1 = new b();
        this(this, this.u);
        this.c = b1;
      } else {
        this.c.a(this.u);
      } 
      if (!a.a().c())
        a.a().b(); 
      a.a().a(this, this.v);
      this.c.a();
      this.n = true;
      if (a.i == 3 || j == 3) {
        a("send http prot head");
        a a = new a() {
            public void onResponse(e param1e) {
              if (param1e.b == 0) {
                XlinkTcpService.a(this.a, "http prot head succeed  send login packet");
                XlinkTcpService.g(this.a);
                return;
              } 
              XlinkTcpService.a(this.a, "http prot head timeout  connect tcp error");
              XlinkTcpService.b(this.a, false);
              c.a(5, -1);
              this.a.a(false, -1, true);
            }
          };
        super(this);
        d d = f.a().a(a);
        g g = new g();
        this(d, a, 3);
        a.a().a(g);
      } else {
        l();
      } 
      this.h = 0;
      return;
    } catch (IOException iOException) {
      if (this.c != null) {
        try {
          this.c.b();
        } catch (Throwable throwable) {}
        this.c = null;
      } 
      if (this.u != null) {
        try {
          this.u.close();
        } catch (Throwable throwable) {}
        this.u = null;
      } 
      if (this.v != null) {
        try {
          this.v.close();
        } catch (Throwable throwable) {}
        this.v = null;
      } 
      if (paramSocket != null)
        try {
          paramSocket.close();
        } catch (Exception exception) {} 
      this.n = false;
      throw iOException;
    } 
  }
  
  public static boolean c() {
    return (a() == null) ? false : a().b();
  }
  
  private void g() {
    this.h++;
    if (this.h > 2) {
      this.h = 0;
      return;
    } 
    a("start reconnect tcp server");
    this.i = false;
    this.g.schedule(new TimerTask(this) {
          public void run() {
            if (!this.a.i)
              XlinkTcpService.b(this.a); 
          }
        },  10000L);
  }
  
  private void h() {
    if (this.t != null) {
      this.t.cancel();
      this.g.purge();
      this.t = null;
    } 
    this.r = false;
  }
  
  private void i() {
    this.i = true;
  }
  
  private void j() {
    if (d) {
      a("connectInSSL ，return...connecting is true");
      return;
    } 
    if (c()) {
      a("connectInSSL---return isConnected is true");
      return;
    } 
    (new Thread(this) {
        public void run() {
          super.run();
          if (XlinkTcpService.c()) {
            XlinkTcpService.a(this.a, "isConnected is true  --return connectInSSL()");
            return;
          } 
          try {
            if (XlinkTcpService.c(this.a) == null)
              XlinkTcpService.a(this.a, InetAddress.getByName(a.a)); 
            SSLContext sSLContext = SSLContext.getInstance("SSL");
            KeyStore keyStore1 = KeyStore.getInstance("BKS");
            InputStream inputStream1 = this.a.getResources().getAssets().open("xlink_kclient.bks");
            InputStream inputStream2 = this.a.getResources().getAssets().open("xlink_tclient.bks");
            keyStore1.load(inputStream1, "123456".toCharArray());
            KeyStore keyStore2 = KeyStore.getInstance("BKS");
            keyStore2.load(inputStream2, "123456".toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
            keyManagerFactory.init(keyStore1, "123456".toCharArray());
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
            trustManagerFactory.init(keyStore2);
            sSLContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            XlinkTcpService.a(this.a, "SSLContext initialize succeed");
            SSLSocketFactory sSLSocketFactory = sSLContext.getSocketFactory();
            XlinkTcpService.a(this.a, sSLSocketFactory.createSocket(XlinkTcpService.c(this.a), a.c));
            XlinkTcpService.b(this.a, XlinkTcpService.d(this.a));
            return;
          } catch (UnknownHostException unknownHostException) {
            XlinkTcpService.e(this.a);
            XlinkTcpService.d = false;
            XlinkTcpService.a(this.a, "UnknownHostException connect SSL tcp error...");
            c.a(5, -1);
            return;
          } catch (IOException iOException) {
            iOException.printStackTrace();
            XlinkTcpService.e(this.a);
            XlinkTcpService.d = false;
            XlinkTcpService.a(this.a, "connect SSL tcp IOException...");
            c.a(5, -1);
            return;
          } catch (Exception exception) {
            exception.printStackTrace();
            XlinkTcpService.e(this.a);
            XlinkTcpService.d = false;
            XlinkTcpService.a(this.a, "connect SSL tcp Exception...");
            c.a(5, -1);
            return;
          } finally {
            XlinkTcpService.d = false;
          } 
        }
      }).start();
    d = true;
    a("cloud connectSSL main thread done");
  }
  
  private void k() {
    if (d) {
      a("connecting is true  intercept connect() method");
      return;
    } 
    if (c()) {
      a("isConnected is true  intercept connect() method");
      return;
    } 
    if (!a.a().d()) {
      a("connect tcp  network is not available");
      c.a(5, -2);
      return;
    } 
    i();
    d = true;
    (new Thread(this) {
        public void run() {
          // Byte code:
          //   0: aload_0
          //   1: invokespecial run : ()V
          //   4: invokestatic c : ()Z
          //   7: ifeq -> 20
          //   10: aload_0
          //   11: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   14: ldc 'isConnected is true  intercept work thread connect() method'
          //   16: invokestatic a : (Lio/xlink/wifi/sdk/XlinkTcpService;Ljava/lang/String;)V
          //   19: return
          //   20: aload_0
          //   21: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   24: invokestatic f : (Lio/xlink/wifi/sdk/XlinkTcpService;)Z
          //   27: ifeq -> 55
          //   30: getstatic io/xlink/wifi/sdk/XlinkTcpService.a : I
          //   33: ifeq -> 55
          //   36: aload_0
          //   37: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   40: ldc 'tcp connect succeed dolonig。。。'
          //   42: invokestatic a : (Lio/xlink/wifi/sdk/XlinkTcpService;Ljava/lang/String;)V
          //   45: aload_0
          //   46: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   49: invokestatic g : (Lio/xlink/wifi/sdk/XlinkTcpService;)V
          //   52: goto -> 19
          //   55: aload_0
          //   56: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   59: astore_1
          //   60: new java/lang/StringBuilder
          //   63: astore_2
          //   64: aload_2
          //   65: invokespecial <init> : ()V
          //   68: aload_1
          //   69: aload_2
          //   70: ldc 'DNS: '
          //   72: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   75: getstatic io/xlink/wifi/sdk/global/a.a : Ljava/lang/String;
          //   78: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   81: invokevirtual toString : ()Ljava/lang/String;
          //   84: invokestatic a : (Lio/xlink/wifi/sdk/XlinkTcpService;Ljava/lang/String;)V
          //   87: aload_0
          //   88: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   91: invokestatic c : (Lio/xlink/wifi/sdk/XlinkTcpService;)Ljava/net/InetAddress;
          //   94: ifnonnull -> 131
          //   97: new io/xlink/wifi/sdk/util/a
          //   100: astore_2
          //   101: aload_2
          //   102: getstatic io/xlink/wifi/sdk/global/a.a : Ljava/lang/String;
          //   105: invokespecial <init> : (Ljava/lang/String;)V
          //   108: aload_2
          //   109: invokevirtual start : ()V
          //   112: aload_2
          //   113: ldc2_w 8000
          //   116: invokevirtual join : (J)V
          //   119: aload_0
          //   120: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   123: aload_2
          //   124: invokevirtual a : ()Ljava/net/InetAddress;
          //   127: invokestatic a : (Lio/xlink/wifi/sdk/XlinkTcpService;Ljava/net/InetAddress;)Ljava/net/InetAddress;
          //   130: pop
          //   131: aload_0
          //   132: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   135: invokestatic c : (Lio/xlink/wifi/sdk/XlinkTcpService;)Ljava/net/InetAddress;
          //   138: ifnonnull -> 267
          //   141: new java/net/UnknownHostException
          //   144: astore_1
          //   145: new java/lang/StringBuilder
          //   148: astore_2
          //   149: aload_2
          //   150: invokespecial <init> : ()V
          //   153: aload_1
          //   154: aload_2
          //   155: ldc 'DNS '
          //   157: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   160: getstatic io/xlink/wifi/sdk/global/a.a : Ljava/lang/String;
          //   163: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   166: ldc ' fail'
          //   168: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   171: invokevirtual toString : ()Ljava/lang/String;
          //   174: invokespecial <init> : (Ljava/lang/String;)V
          //   177: aload_1
          //   178: athrow
          //   179: astore_1
          //   180: iconst_0
          //   181: putstatic io/xlink/wifi/sdk/XlinkTcpService.d : Z
          //   184: aload_0
          //   185: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   188: ldc 'UnknownHostException connect tcp error...'
          //   190: invokestatic a : (Lio/xlink/wifi/sdk/XlinkTcpService;Ljava/lang/String;)V
          //   193: iconst_5
          //   194: iconst_m1
          //   195: invokestatic a : (II)V
          //   198: aload_0
          //   199: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   202: invokestatic e : (Lio/xlink/wifi/sdk/XlinkTcpService;)V
          //   205: goto -> 19
          //   208: astore_1
          //   209: aload_1
          //   210: invokevirtual printStackTrace : ()V
          //   213: goto -> 119
          //   216: astore_1
          //   217: aload_0
          //   218: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   221: ldc 'connect tcp IOException...'
          //   223: invokestatic a : (Lio/xlink/wifi/sdk/XlinkTcpService;Ljava/lang/String;)V
          //   226: getstatic io/xlink/wifi/sdk/global/a.i : I
          //   229: iconst_1
          //   230: if_icmpne -> 543
          //   233: iconst_0
          //   234: putstatic io/xlink/wifi/sdk/XlinkTcpService.d : Z
          //   237: getstatic io/xlink/wifi/sdk/XlinkTcpService.j : I
          //   240: iconst_1
          //   241: if_icmpne -> 485
          //   244: aload_0
          //   245: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   248: ldc 'connect NORMAL fail--attempt http port'
          //   250: invokestatic a : (Lio/xlink/wifi/sdk/XlinkTcpService;Ljava/lang/String;)V
          //   253: iconst_3
          //   254: putstatic io/xlink/wifi/sdk/XlinkTcpService.j : I
          //   257: aload_0
          //   258: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   261: invokestatic b : (Lio/xlink/wifi/sdk/XlinkTcpService;)V
          //   264: goto -> 19
          //   267: aload_0
          //   268: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   271: astore_1
          //   272: new java/net/Socket
          //   275: astore_2
          //   276: aload_2
          //   277: invokespecial <init> : ()V
          //   280: aload_1
          //   281: aload_2
          //   282: invokestatic a : (Lio/xlink/wifi/sdk/XlinkTcpService;Ljava/net/Socket;)Ljava/net/Socket;
          //   285: pop
          //   286: getstatic io/xlink/wifi/sdk/global/a.i : I
          //   289: iconst_3
          //   290: if_icmpeq -> 300
          //   293: getstatic io/xlink/wifi/sdk/XlinkTcpService.j : I
          //   296: iconst_3
          //   297: if_icmpne -> 404
          //   300: aload_0
          //   301: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   304: astore_1
          //   305: new java/lang/StringBuilder
          //   308: astore_2
          //   309: aload_2
          //   310: invokespecial <init> : ()V
          //   313: aload_1
          //   314: aload_2
          //   315: ldc ' connect tcp '
          //   317: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   320: aload_0
          //   321: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   324: invokestatic c : (Lio/xlink/wifi/sdk/XlinkTcpService;)Ljava/net/InetAddress;
          //   327: invokevirtual toString : ()Ljava/lang/String;
          //   330: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   333: ldc ' port...TCP_TYPE =HTTP and currentType= HTTP'
          //   335: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   338: invokevirtual toString : ()Ljava/lang/String;
          //   341: invokestatic a : (Lio/xlink/wifi/sdk/XlinkTcpService;Ljava/lang/String;)V
          //   344: aload_0
          //   345: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   348: invokestatic d : (Lio/xlink/wifi/sdk/XlinkTcpService;)Ljava/net/Socket;
          //   351: astore_1
          //   352: new java/net/InetSocketAddress
          //   355: astore_2
          //   356: aload_2
          //   357: aload_0
          //   358: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   361: invokestatic c : (Lio/xlink/wifi/sdk/XlinkTcpService;)Ljava/net/InetAddress;
          //   364: getstatic io/xlink/wifi/sdk/global/a.d : I
          //   367: invokespecial <init> : (Ljava/net/InetAddress;I)V
          //   370: aload_1
          //   371: aload_2
          //   372: sipush #8000
          //   375: invokevirtual connect : (Ljava/net/SocketAddress;I)V
          //   378: aload_0
          //   379: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   382: aload_0
          //   383: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   386: invokestatic d : (Lio/xlink/wifi/sdk/XlinkTcpService;)Ljava/net/Socket;
          //   389: invokestatic b : (Lio/xlink/wifi/sdk/XlinkTcpService;Ljava/net/Socket;)V
          //   392: aload_0
          //   393: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   396: ldc 'tcp connect succeed..'
          //   398: invokestatic a : (Lio/xlink/wifi/sdk/XlinkTcpService;Ljava/lang/String;)V
          //   401: goto -> 19
          //   404: aload_0
          //   405: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   408: astore_1
          //   409: new java/lang/StringBuilder
          //   412: astore_2
          //   413: aload_2
          //   414: invokespecial <init> : ()V
          //   417: aload_1
          //   418: aload_2
          //   419: ldc ' connect tcp '
          //   421: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   424: aload_0
          //   425: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   428: invokestatic c : (Lio/xlink/wifi/sdk/XlinkTcpService;)Ljava/net/InetAddress;
          //   431: invokevirtual toString : ()Ljava/lang/String;
          //   434: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   437: ldc '   TCP_TYPE_NORMAL.'
          //   439: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   442: invokevirtual toString : ()Ljava/lang/String;
          //   445: invokestatic a : (Lio/xlink/wifi/sdk/XlinkTcpService;Ljava/lang/String;)V
          //   448: aload_0
          //   449: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   452: invokestatic d : (Lio/xlink/wifi/sdk/XlinkTcpService;)Ljava/net/Socket;
          //   455: astore_1
          //   456: new java/net/InetSocketAddress
          //   459: astore_2
          //   460: aload_2
          //   461: aload_0
          //   462: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   465: invokestatic c : (Lio/xlink/wifi/sdk/XlinkTcpService;)Ljava/net/InetAddress;
          //   468: getstatic io/xlink/wifi/sdk/global/a.b : I
          //   471: invokespecial <init> : (Ljava/net/InetAddress;I)V
          //   474: aload_1
          //   475: aload_2
          //   476: sipush #8000
          //   479: invokevirtual connect : (Ljava/net/SocketAddress;I)V
          //   482: goto -> 378
          //   485: getstatic io/xlink/wifi/sdk/XlinkTcpService.j : I
          //   488: iconst_3
          //   489: if_icmpne -> 515
          //   492: aload_0
          //   493: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   496: ldc 'connect http port fail --attempt ssl port'
          //   498: invokestatic a : (Lio/xlink/wifi/sdk/XlinkTcpService;Ljava/lang/String;)V
          //   501: iconst_4
          //   502: putstatic io/xlink/wifi/sdk/XlinkTcpService.j : I
          //   505: aload_0
          //   506: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   509: invokestatic h : (Lio/xlink/wifi/sdk/XlinkTcpService;)V
          //   512: goto -> 19
          //   515: aload_0
          //   516: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   519: invokestatic e : (Lio/xlink/wifi/sdk/XlinkTcpService;)V
          //   522: iconst_0
          //   523: putstatic io/xlink/wifi/sdk/XlinkTcpService.d : Z
          //   526: aload_0
          //   527: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   530: ldc 'connect tcp IOException...'
          //   532: invokestatic a : (Lio/xlink/wifi/sdk/XlinkTcpService;Ljava/lang/String;)V
          //   535: iconst_5
          //   536: iconst_m1
          //   537: invokestatic a : (II)V
          //   540: goto -> 19
          //   543: iconst_0
          //   544: putstatic io/xlink/wifi/sdk/XlinkTcpService.d : Z
          //   547: aload_0
          //   548: getfield a : Lio/xlink/wifi/sdk/XlinkTcpService;
          //   551: invokestatic e : (Lio/xlink/wifi/sdk/XlinkTcpService;)V
          //   554: iconst_5
          //   555: iconst_m1
          //   556: invokestatic a : (II)V
          //   559: goto -> 19
          // Exception table:
          //   from	to	target	type
          //   55	112	179	java/net/UnknownHostException
          //   55	112	216	java/io/IOException
          //   112	119	208	java/lang/InterruptedException
          //   112	119	179	java/net/UnknownHostException
          //   112	119	216	java/io/IOException
          //   119	131	179	java/net/UnknownHostException
          //   119	131	216	java/io/IOException
          //   131	179	179	java/net/UnknownHostException
          //   131	179	216	java/io/IOException
          //   209	213	179	java/net/UnknownHostException
          //   209	213	216	java/io/IOException
          //   267	300	179	java/net/UnknownHostException
          //   267	300	216	java/io/IOException
          //   300	378	179	java/net/UnknownHostException
          //   300	378	216	java/io/IOException
          //   378	401	179	java/net/UnknownHostException
          //   378	401	216	java/io/IOException
          //   404	482	179	java/net/UnknownHostException
          //   404	482	216	java/io/IOException
        }
      }).start();
    a("connect tcp main thread done.");
  }
  
  private void l() {
    c.a().a(a, b, new a(this) {
          public void onResponse(e param1e) {
            XlinkTcpService.d = false;
            switch (param1e.b) {
              default:
                XlinkTcpService.a(this.a, "login fail code ::" + param1e.b);
                c.a(5, param1e.b);
                return;
              case 0:
                XlinkTcpService.a(this.a, "login - - connect tcp succeed");
                XlinkTcpService.a(this.a, true);
                c.a(4, 0);
                this.a.d();
                return;
              case -100:
                break;
            } 
            XlinkTcpService.a(this.a, "login fail service not response packet timeout ! ");
            XlinkTcpService.e(this.a);
            c.a(5, -100);
          }
        }6);
  }
  
  public void a(g paramg) {
    a.a().b(paramg);
  }
  
  public void a(boolean paramBoolean1, int paramInt, boolean paramBoolean2) {
    this.e = 0;
    if (!this.o)
      paramBoolean1 = false; 
    this.o = false;
    if (this.n) {
      this.n = false;
      a("disconnect shutdown isDispatch :" + paramBoolean1 + " code :" + paramInt);
      if (this.c != null)
        this.c.b(); 
      if (this.u != null) {
        try {
          this.u.close();
        } catch (Throwable throwable) {}
        this.u = null;
      } 
      if (this.v != null) {
        try {
          this.v.close();
        } catch (Throwable throwable) {}
        this.v = null;
      } 
      try {
        this.m.close();
      } catch (Exception exception) {}
      if (paramBoolean1) {
        if (paramBoolean2)
          g(); 
        a("dispatch NetEvent cloud disconnect");
        c.a(6, paramInt);
      } 
    } 
  }
  
  public boolean b() {
    return (this.n && this.o && a != 0);
  }
  
  public void d() {
    MyLog.e("TCPService", "cloud start KeepAlive ");
    if (System.currentTimeMillis() - this.f > (a.e * 1000))
      this.r = false; 
    if (!this.r) {
      this.f = System.currentTimeMillis();
      int i = a.e / 3 - 2;
      if (this.t != null) {
        this.t.cancel();
        this.t = null;
      } 
      this.t = new TimerTask(this) {
          public void run() {
            if (!XlinkTcpService.c()) {
              XlinkTcpService.a(this.a);
              XlinkTcpService.a(this.a, "tcp !isConnected stopKeepAlive ");
              return;
            } 
            long l = (System.currentTimeMillis() - this.a.f) / 1000L;
            if (this.a.e > 3) {
              XlinkTcpService.a(this.a, "tcp 3次心跳超时" + l + " cloud service 3 times not response ping app logout ");
              XlinkTcpService.a(this.a);
              this.a.a(true, -2, true);
              return;
            } 
            System.currentTimeMillis();
            l = this.a.f;
            XlinkTcpService xlinkTcpService = this.a;
            xlinkTcpService.e++;
            b.a = 0;
            g g = c.a().b();
            this.a.a(g);
            XlinkTcpService.a(this.a, "tcp send keep alive packet ");
            this.a.f = System.currentTimeMillis();
          }
        };
      this.g.schedule(this.t, (i * 1000), (i * 1000));
      this.r = true;
    } 
  }
  
  public IBinder onBind(Intent paramIntent) {
    return null;
  }
  
  public void onCreate() {
    super.onCreate();
    a("Xlink Tcp Service onCreate! ");
    l = this;
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(p);
    intentFilter.addAction(q);
    intentFilter.addAction("android.intent.action.TIME_TICK");
    registerReceiver(this.s, intentFilter);
  }
  
  public void onDestroy() {
    unregisterReceiver(this.s);
    a("XlinkTcpService onDestroy! ");
    c.a("XTService");
    a(true, -3, false);
    i();
    startService(new Intent((Context)this, XlinkTcpService.class));
    a("tcp RestartService... on onDestroy");
    super.onDestroy();
  }
  
  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
    a("onStart service statue:" + c());
    if (a == 0 || b == null) {
      a("appid ==0 ||keys==null return onStartCommand");
      return 2;
    } 
    if (!c()) {
      j = a.i;
      switch (a.i) {
        default:
          return 1;
        case 1:
          k();
        case 2:
          k();
        case 3:
          k();
        case 4:
          break;
      } 
      j();
    } 
    a("onStart do login succeed ");
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/XlinkTcpService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */