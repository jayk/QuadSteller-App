package app.esptouch.udp;

import android.util.Log;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPSocketClient {
  private static final String TAG = "UDPSocketClient";
  
  private volatile boolean mIsClosed;
  
  private volatile boolean mIsStop;
  
  private DatagramSocket mSocket;
  
  public UDPSocketClient() {
    try {
      DatagramSocket datagramSocket = new DatagramSocket();
      this();
      this.mSocket = datagramSocket;
      this.mIsStop = false;
      this.mIsClosed = false;
    } catch (SocketException socketException) {
      Log.e("UDPSocketClient", "SocketException");
      socketException.printStackTrace();
    } 
  }
  
  public void close() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mIsClosed : Z
    //   6: ifne -> 21
    //   9: aload_0
    //   10: getfield mSocket : Ljava/net/DatagramSocket;
    //   13: invokevirtual close : ()V
    //   16: aload_0
    //   17: iconst_1
    //   18: putfield mIsClosed : Z
    //   21: aload_0
    //   22: monitorexit
    //   23: return
    //   24: astore_1
    //   25: aload_0
    //   26: monitorexit
    //   27: aload_1
    //   28: athrow
    // Exception table:
    //   from	to	target	type
    //   2	21	24	finally
  }
  
  protected void finalize() throws Throwable {
    close();
    super.finalize();
  }
  
  public void interrupt() {
    Log.i("UDPSocketClient", "USPSocketClient is interrupt");
    this.mIsStop = true;
  }
  
  public void sendData(byte[][] paramArrayOfbyte, int paramInt1, int paramInt2, String paramString, int paramInt3, long paramLong) {
    if (paramArrayOfbyte == null || paramArrayOfbyte.length <= 0) {
      Log.e("UDPSocketClient", "sendData(): data == null or length <= 0");
      return;
    } 
    for (int i = paramInt1;; i++) {
      if (!this.mIsStop && i < paramInt1 + paramInt2) {
        if ((paramArrayOfbyte[i]).length == 0)
          continue; 
        try {
          DatagramPacket datagramPacket = new DatagramPacket();
          this(paramArrayOfbyte[i], (paramArrayOfbyte[i]).length, InetAddress.getByName(paramString), paramInt3);
          this.mSocket.send(datagramPacket);
        } catch (UnknownHostException unknownHostException) {
          Log.e("UDPSocketClient", "sendData(): UnknownHostException");
          unknownHostException.printStackTrace();
          this.mIsStop = true;
          if (this.mIsStop)
            close(); 
          return;
        } catch (IOException iOException) {
          Log.e("UDPSocketClient", "sendData(): IOException, but just ignore it");
        } 
        try {
          Thread.sleep(paramLong);
          continue;
        } catch (InterruptedException interruptedException) {
          interruptedException.printStackTrace();
          Log.e("UDPSocketClient", "sendData is Interrupted");
          this.mIsStop = true;
        } 
      } 
      if (this.mIsStop)
        close(); 
      return;
    } 
  }
  
  public void sendData(byte[][] paramArrayOfbyte, String paramString, int paramInt, long paramLong) {
    sendData(paramArrayOfbyte, 0, paramArrayOfbyte.length, paramString, paramInt, paramLong);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/esptouch/udp/UDPSocketClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */