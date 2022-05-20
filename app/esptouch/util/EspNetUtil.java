package app.esptouch.util;

import android.content.Context;
import android.net.wifi.WifiManager;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class EspNetUtil {
  private static String __formatString(int paramInt) {
    String str = "";
    byte[] arrayOfByte = __intToByteArray(paramInt);
    for (paramInt = arrayOfByte.length - 1; paramInt >= 0; paramInt--) {
      String str1 = str + (arrayOfByte[paramInt] & 0xFF);
      str = str1;
      if (paramInt > 0)
        str = str1 + "."; 
    } 
    return str;
  }
  
  private static byte[] __intToByteArray(int paramInt) {
    byte[] arrayOfByte = new byte[4];
    for (byte b = 0; b < 4; b++)
      arrayOfByte[b] = (byte)(byte)(paramInt >>> (arrayOfByte.length - 1 - b) * 8 & 0xFF); 
    return arrayOfByte;
  }
  
  public static InetAddress getLocalInetAddress(Context paramContext) {
    InetAddress inetAddress;
    String str = __formatString(((WifiManager)paramContext.getSystemService("wifi")).getConnectionInfo().getIpAddress());
    paramContext = null;
    try {
      InetAddress inetAddress1 = InetAddress.getByName(str);
      inetAddress = inetAddress1;
    } catch (UnknownHostException unknownHostException) {
      unknownHostException.printStackTrace();
    } 
    return inetAddress;
  }
  
  public static byte[] parseBssid2bytes(String paramString) {
    String[] arrayOfString = paramString.split(":");
    byte[] arrayOfByte = new byte[arrayOfString.length];
    for (byte b = 0; b < arrayOfString.length; b++)
      arrayOfByte[b] = (byte)(byte)Integer.parseInt(arrayOfString[b], 16); 
    return arrayOfByte;
  }
  
  public static InetAddress parseInetAddr(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    UnknownHostException unknownHostException2 = null;
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b < paramInt2; b++) {
      stringBuilder.append(Integer.toString(paramArrayOfbyte[paramInt1 + b] & 0xFF));
      if (b != paramInt2 - 1)
        stringBuilder.append('.'); 
    } 
    try {
      InetAddress inetAddress = InetAddress.getByName(stringBuilder.toString());
    } catch (UnknownHostException unknownHostException1) {
      unknownHostException1.printStackTrace();
      unknownHostException1 = unknownHostException2;
    } 
    return (InetAddress)unknownHostException1;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/esptouch/util/EspNetUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */