package app.esptouch;

import java.net.InetAddress;

public interface IEsptouchResult {
  String getBssid();
  
  InetAddress getInetAddress();
  
  boolean isCancelled();
  
  boolean isSuc();
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/esptouch/IEsptouchResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */