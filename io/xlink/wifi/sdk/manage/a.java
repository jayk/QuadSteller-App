package io.xlink.wifi.sdk.manage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import io.xlink.wifi.sdk.util.b;

public class a {
  private static a c;
  
  boolean a = true;
  
  BroadcastReceiver b = new BroadcastReceiver(this) {
      public void onReceive(Context param1Context, Intent param1Intent) {
        // Byte code:
        //   0: aload_2
        //   1: invokevirtual getAction : ()Ljava/lang/String;
        //   4: ldc 'android.net.conn.CONNECTIVITY_CHANGE'
        //   6: invokestatic equals : (Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Z
        //   9: ifeq -> 109
        //   12: aload_0
        //   13: getfield a : Lio/xlink/wifi/sdk/manage/a;
        //   16: invokevirtual c : ()Landroid/net/NetworkInfo;
        //   19: astore_1
        //   20: aload_1
        //   21: ifnull -> 103
        //   24: aload_1
        //   25: invokevirtual isAvailable : ()Z
        //   28: ifeq -> 103
        //   31: ldc 'NetWrok'
        //   33: new java/lang/StringBuilder
        //   36: dup
        //   37: invokespecial <init> : ()V
        //   40: ldc 'NetworkInfo：：'
        //   42: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   45: aload_1
        //   46: invokevirtual getType : ()I
        //   49: invokevirtual append : (I)Ljava/lang/StringBuilder;
        //   52: invokevirtual toString : ()Ljava/lang/String;
        //   55: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)V
        //   58: aload_1
        //   59: invokevirtual getType : ()I
        //   62: tableswitch default -> 84, 0 -> 146, 1 -> 110
        //   84: invokestatic c : ()Z
        //   87: ifne -> 103
        //   90: invokestatic getInstance : ()Lio/xlink/wifi/sdk/XlinkAgent;
        //   93: getstatic io/xlink/wifi/sdk/XlinkTcpService.a : I
        //   96: getstatic io/xlink/wifi/sdk/XlinkTcpService.b : Ljava/lang/String;
        //   99: invokevirtual login : (ILjava/lang/String;)I
        //   102: pop
        //   103: invokestatic a : ()Lio/xlink/wifi/sdk/manage/c;
        //   106: invokevirtual e : ()V
        //   109: return
        //   110: invokestatic a : ()Z
        //   113: ifne -> 84
        //   116: invokestatic b : ()Lio/xlink/wifi/sdk/XlinkUdpService;
        //   119: ifnull -> 84
        //   122: new android/content/Intent
        //   125: dup
        //   126: getstatic io/xlink/wifi/sdk/util/b.a : Landroid/content/Context;
        //   129: ldc io/xlink/wifi/sdk/XlinkUdpService
        //   131: invokespecial <init> : (Landroid/content/Context;Ljava/lang/Class;)V
        //   134: astore_1
        //   135: getstatic io/xlink/wifi/sdk/util/b.a : Landroid/content/Context;
        //   138: aload_1
        //   139: invokevirtual startService : (Landroid/content/Intent;)Landroid/content/ComponentName;
        //   142: pop
        //   143: goto -> 84
        //   146: invokestatic a : ()Lio/xlink/wifi/sdk/manage/c;
        //   149: invokevirtual e : ()V
        //   152: invokestatic b : ()Lio/xlink/wifi/sdk/XlinkUdpService;
        //   155: ifnull -> 84
        //   158: invokestatic b : ()Lio/xlink/wifi/sdk/XlinkUdpService;
        //   161: iconst_0
        //   162: iconst_0
        //   163: invokevirtual a : (ZI)V
        //   166: goto -> 84
      }
    };
  
  public static a a() {
    if (c == null)
      c = new a(); 
    return c;
  }
  
  private void f() {
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
    intentFilter.setPriority(1000);
    b.a.registerReceiver(this.b, intentFilter);
  }
  
  public void b() {
    if (this.a) {
      this.a = false;
      f();
    } 
  }
  
  public NetworkInfo c() {
    return ((ConnectivityManager)b.a.getSystemService("connectivity")).getActiveNetworkInfo();
  }
  
  public boolean d() {
    NetworkInfo networkInfo = c();
    return (networkInfo == null) ? false : networkInfo.isAvailable();
  }
  
  public void e() {
    if (!this.a) {
      this.a = true;
      b.a.unregisterReceiver(this.b);
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/manage/a.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */