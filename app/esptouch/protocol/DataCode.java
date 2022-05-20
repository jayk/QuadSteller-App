package app.esptouch.protocol;

import app.esptouch.task.ICodeData;
import app.esptouch.util.ByteUtil;
import app.esptouch.util.CRC8;

public class DataCode implements ICodeData {
  public static final int DATA_CODE_LEN = 6;
  
  private static final int INDEX_MAX = 127;
  
  private final byte mCrcHigh;
  
  private final byte mCrcLow;
  
  private final byte mDataHigh;
  
  private final byte mDataLow;
  
  private final byte mSeqHeader;
  
  public DataCode(char paramChar, int paramInt) {
    if (paramInt > 127)
      throw new RuntimeException("index > INDEX_MAX"); 
    byte[] arrayOfByte2 = ByteUtil.splitUint8To2bytes(paramChar);
    this.mDataHigh = (byte)arrayOfByte2[0];
    this.mDataLow = (byte)arrayOfByte2[1];
    CRC8 cRC8 = new CRC8();
    cRC8.update(ByteUtil.convertUint8toByte(paramChar));
    cRC8.update(paramInt);
    byte[] arrayOfByte1 = ByteUtil.splitUint8To2bytes((char)(int)cRC8.getValue());
    this.mCrcHigh = (byte)arrayOfByte1[0];
    this.mCrcLow = (byte)arrayOfByte1[1];
    this.mSeqHeader = (byte)(byte)paramInt;
  }
  
  public byte[] getBytes() {
    return new byte[] { 0, ByteUtil.combine2bytesToOne(this.mCrcHigh, this.mDataHigh), 1, this.mSeqHeader, 0, ByteUtil.combine2bytesToOne(this.mCrcLow, this.mDataLow) };
  }
  
  public char[] getU8s() {
    throw new RuntimeException("DataCode don't support getU8s()");
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    byte[] arrayOfByte = getBytes();
    for (byte b = 0; b < 6; b++) {
      String str = ByteUtil.convertByte2HexString(arrayOfByte[b]);
      stringBuilder.append("0x");
      if (str.length() == 1)
        stringBuilder.append("0"); 
      stringBuilder.append(str).append(" ");
    } 
    return stringBuilder.toString();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/esptouch/protocol/DataCode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */