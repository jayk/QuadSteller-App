package app.esptouch.util;

import java.util.zip.Checksum;

public class CRC8 implements Checksum {
  private static final short CRC_INITIAL = 0;
  
  private static final short CRC_POLYNOM = 140;
  
  private static final short[] crcTable = new short[256];
  
  private final short init = (short)0;
  
  private short value = (short)0;
  
  static {
    for (byte b = 0; b < 'Ā'; b++) {
      int i = b;
      for (byte b1 = 0; b1 < 8; b1++) {
        if ((i & 0x1) != 0) {
          i = i >>> 1 ^ 0x8C;
        } else {
          i >>>= 1;
        } 
      } 
      crcTable[b] = (short)(short)i;
    } 
  }
  
  public long getValue() {
    return (this.value & 0xFF);
  }
  
  public void reset() {
    this.value = (short)this.init;
  }
  
  public void update(int paramInt) {
    update(new byte[] { (byte)paramInt }, 0, 1);
  }
  
  public void update(byte[] paramArrayOfbyte) {
    update(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public void update(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    for (byte b = 0; b < paramInt2; b++) {
      byte b1 = paramArrayOfbyte[paramInt1 + b];
      short s = this.value;
      this.value = (short)(short)(crcTable[(b1 ^ s) & 0xFF] ^ this.value << 8);
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/esptouch/util/CRC8.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */