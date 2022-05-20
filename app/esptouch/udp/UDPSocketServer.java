package app.esptouch.udp;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

public class UDPSocketServer {
  private static final String TAG = "UDPSocketServer";
  
  private final byte[] buffer;
  
  private Context mContext;
  
  private volatile boolean mIsClosed;
  
  private WifiManager.MulticastLock mLock;
  
  private DatagramPacket mReceivePacket;
  
  private DatagramSocket mServerSocket;
  
  private int port;
  
  public UDPSocketServer(int paramInt1, int paramInt2, Context paramContext) {
    this.port = paramInt1;
    this.mContext = paramContext;
    this.buffer = new byte[64];
    this.mReceivePacket = new DatagramPacket(this.buffer, 64);
    try {
      DatagramSocket datagramSocket = new DatagramSocket();
      this(paramInt1);
      this.mServerSocket = datagramSocket;
      this.mServerSocket.setSoTimeout(paramInt2);
      this.mIsClosed = false;
      this.mLock = ((WifiManager)this.mContext.getSystemService("wifi")).createMulticastLock("test wifi");
      StringBuilder stringBuilder = new StringBuilder();
      this();
      Log.d("UDPSocketServer", stringBuilder.append("mServerSocket is created, socket read timeout: ").append(paramInt2).append(", port: ").append(paramInt1).toString());
    } catch (IOException iOException) {
      Log.e("UDPSocketServer", "IOException");
      iOException.printStackTrace();
    } 
  }
  
  private void acquireLock() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mLock : Landroid/net/wifi/WifiManager$MulticastLock;
    //   6: ifnull -> 26
    //   9: aload_0
    //   10: getfield mLock : Landroid/net/wifi/WifiManager$MulticastLock;
    //   13: invokevirtual isHeld : ()Z
    //   16: ifne -> 26
    //   19: aload_0
    //   20: getfield mLock : Landroid/net/wifi/WifiManager$MulticastLock;
    //   23: invokevirtual acquire : ()V
    //   26: aload_0
    //   27: monitorexit
    //   28: return
    //   29: astore_1
    //   30: aload_0
    //   31: monitorexit
    //   32: aload_1
    //   33: athrow
    // Exception table:
    //   from	to	target	type
    //   2	26	29	finally
  }
  
  private void releaseLock() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mLock : Landroid/net/wifi/WifiManager$MulticastLock;
    //   6: ifnull -> 28
    //   9: aload_0
    //   10: getfield mLock : Landroid/net/wifi/WifiManager$MulticastLock;
    //   13: invokevirtual isHeld : ()Z
    //   16: istore_1
    //   17: iload_1
    //   18: ifeq -> 28
    //   21: aload_0
    //   22: getfield mLock : Landroid/net/wifi/WifiManager$MulticastLock;
    //   25: invokevirtual release : ()V
    //   28: aload_0
    //   29: monitorexit
    //   30: return
    //   31: astore_2
    //   32: aload_0
    //   33: monitorexit
    //   34: aload_2
    //   35: athrow
    //   36: astore_2
    //   37: goto -> 28
    // Exception table:
    //   from	to	target	type
    //   2	17	31	finally
    //   21	28	36	java/lang/Throwable
    //   21	28	31	finally
  }
  
  public void close() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mIsClosed : Z
    //   6: ifne -> 33
    //   9: ldc 'UDPSocketServer'
    //   11: ldc 'mServerSocket is closed'
    //   13: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   16: pop
    //   17: aload_0
    //   18: getfield mServerSocket : Ljava/net/DatagramSocket;
    //   21: invokevirtual close : ()V
    //   24: aload_0
    //   25: invokespecial releaseLock : ()V
    //   28: aload_0
    //   29: iconst_1
    //   30: putfield mIsClosed : Z
    //   33: aload_0
    //   34: monitorexit
    //   35: return
    //   36: astore_1
    //   37: aload_0
    //   38: monitorexit
    //   39: aload_1
    //   40: athrow
    // Exception table:
    //   from	to	target	type
    //   2	33	36	finally
  }
  
  protected void finalize() throws Throwable {
    close();
    super.finalize();
  }
  
  public void interrupt() {
    Log.d("UDPSocketServer", "z=0o9-0009---------------------");
    close();
  }
  
  public byte receiveOneByte() {
    byte b;
    Log.d("UDPSocketServer", "receiveOneByte() entrance");
    try {
      acquireLock();
      this.mServerSocket.receive(this.mReceivePacket);
      StringBuilder stringBuilder = new StringBuilder();
      this();
      Log.d("UDPSocketServer", stringBuilder.append("receive: ").append(this.mReceivePacket.getData()[0] + 0).toString());
      byte b1 = this.mReceivePacket.getData()[0];
      b = b1;
    } catch (IOException iOException) {
      iOException.printStackTrace();
      byte b1 = -128;
      b = b1;
    } 
    return b;
  }
  
  public byte[] receiveSpecLenBytes(int paramInt) {
    Log.d("UDPSocketServer", "receiveSpecLenBytes() entrance: len = " + paramInt);
    try {
      acquireLock();
      StringBuilder stringBuilder1 = new StringBuilder();
      this();
      Log.d("UDPSocketServer", stringBuilder1.append("mServerSocket:").append(this.mServerSocket).toString());
      stringBuilder1 = new StringBuilder();
      this();
      Log.d("UDPSocketServer", stringBuilder1.append("mReceivePacket:").append(this.mReceivePacket).toString());
      this.mServerSocket.receive(this.mReceivePacket);
      byte[] arrayOfByte2 = Arrays.copyOf(this.mReceivePacket.getData(), this.mReceivePacket.getLength());
      stringBuilder1 = new StringBuilder();
      this();
      Log.d("UDPSocketServer", stringBuilder1.append("received len : ").append(arrayOfByte2.length).toString());
      for (byte b = 0; b < arrayOfByte2.length; b++) {
        stringBuilder1 = new StringBuilder();
        this();
        Log.e("UDPSocketServer", stringBuilder1.append("recDatas[").append(b).append("]:").append(arrayOfByte2[b]).toString());
      } 
      stringBuilder1 = new StringBuilder();
      this();
      StringBuilder stringBuilder2 = stringBuilder1.append("receiveSpecLenBytes: ");
      String str = new String();
      this(arrayOfByte2);
      Log.e("UDPSocketServer", stringBuilder2.append(str).toString());
      byte[] arrayOfByte1 = arrayOfByte2;
      if (arrayOfByte2.length != paramInt) {
        Log.w("UDPSocketServer", "received len is different from specific len, return null");
        arrayOfByte1 = null;
      } 
    } catch (IOException iOException) {
      iOException.printStackTrace();
      iOException = null;
    } 
    return (byte[])iOException;
  }
  
  public boolean setSoTimeout(int paramInt) {
    boolean bool;
    try {
      this.mServerSocket.setSoTimeout(paramInt);
      bool = true;
    } catch (SocketException socketException) {
      socketException.printStackTrace();
      bool = false;
    } 
    return bool;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/esptouch/udp/UDPSocketServer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */