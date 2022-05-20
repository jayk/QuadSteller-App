package io.xlink.wifi.sdk.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class a extends Thread {
  private InetAddress a;
  
  private String b;
  
  public a(String paramString) {
    this.b = paramString;
  }
  
  private void a(InetAddress paramInetAddress) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield a : Ljava/net/InetAddress;
    //   7: aload_0
    //   8: monitorexit
    //   9: return
    //   10: astore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_1
    //   14: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	10	finally
  }
  
  public InetAddress a() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield a : Ljava/net/InetAddress;
    //   6: ifnull -> 18
    //   9: aload_0
    //   10: getfield a : Ljava/net/InetAddress;
    //   13: astore_1
    //   14: aload_0
    //   15: monitorexit
    //   16: aload_1
    //   17: areturn
    //   18: aconst_null
    //   19: astore_1
    //   20: goto -> 14
    //   23: astore_1
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_1
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	23	finally
  }
  
  public void run() {
    try {
      a(InetAddress.getByName(this.b));
    } catch (UnknownHostException unknownHostException) {}
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/io/xlink/wifi/sdk/util/a.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */