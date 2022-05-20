package app.esptouch.protocol;

import app.esptouch.task.IEsptouchGenerator;
import app.esptouch.util.ByteUtil;
import java.net.InetAddress;

public class EsptouchGenerator implements IEsptouchGenerator {
  private final byte[][] mDcBytes2;
  
  private final byte[][] mGcBytes2;
  
  public EsptouchGenerator(String paramString1, String paramString2, String paramString3, InetAddress paramInetAddress, boolean paramBoolean) {
    char[] arrayOfChar2 = (new GuideCode()).getU8s();
    this.mGcBytes2 = new byte[arrayOfChar2.length][];
    byte b;
    for (b = 0; b < this.mGcBytes2.length; b++)
      this.mGcBytes2[b] = ByteUtil.genSpecBytes(arrayOfChar2[b]); 
    char[] arrayOfChar1 = (new DatumCode(paramString1, paramString2, paramString3, paramInetAddress, paramBoolean)).getU8s();
    this.mDcBytes2 = new byte[arrayOfChar1.length][];
    for (b = 0; b < this.mDcBytes2.length; b++)
      this.mDcBytes2[b] = ByteUtil.genSpecBytes(arrayOfChar1[b]); 
  }
  
  public byte[][] getDCBytes2() {
    return this.mDcBytes2;
  }
  
  public byte[][] getGCBytes2() {
    return this.mGcBytes2;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/esptouch/protocol/EsptouchGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */