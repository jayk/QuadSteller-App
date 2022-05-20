package app.esptouch;

import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicBoolean;

public class EsptouchResult implements IEsptouchResult {
  private final String mBssid;
  
  private final InetAddress mInetAddress;
  
  private AtomicBoolean mIsCancelled;
  
  private final boolean mIsSuc;
  
  public EsptouchResult(boolean paramBoolean, String paramString, InetAddress paramInetAddress) {
    this.mIsSuc = paramBoolean;
    this.mBssid = paramString;
    this.mInetAddress = paramInetAddress;
    this.mIsCancelled = new AtomicBoolean(false);
  }
  
  public String getBssid() {
    return this.mBssid;
  }
  
  public InetAddress getInetAddress() {
    return this.mInetAddress;
  }
  
  public boolean isCancelled() {
    return this.mIsCancelled.get();
  }
  
  public boolean isSuc() {
    return this.mIsSuc;
  }
  
  public void setIsCancelled(boolean paramBoolean) {
    this.mIsCancelled.set(paramBoolean);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/esptouch/EsptouchResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */