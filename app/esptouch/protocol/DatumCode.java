package app.esptouch.protocol;

import app.esptouch.task.ICodeData;
import app.esptouch.util.ByteUtil;
import app.esptouch.util.CRC8;
import app.esptouch.util.EspNetUtil;
import java.net.InetAddress;

public class DatumCode implements ICodeData {
  private static final int EXTRA_HEAD_LEN = 5;
  
  private static final int EXTRA_LEN = 40;
  
  private final DataCode[] mDataCodes;
  
  public DatumCode(String paramString1, String paramString2, String paramString3, InetAddress paramInetAddress, boolean paramBoolean) {
    char c1 = (char)(ByteUtil.getBytesByString(paramString3)).length;
    CRC8 cRC8 = new CRC8();
    cRC8.update(ByteUtil.getBytesByString(paramString1));
    char c2 = (char)(int)cRC8.getValue();
    cRC8.reset();
    cRC8.update(EspNetUtil.parseBssid2bytes(paramString2));
    char c3 = (char)(int)cRC8.getValue();
    char c4 = (char)(ByteUtil.getBytesByString(paramString1)).length;
    String[] arrayOfString = paramInetAddress.getHostAddress().split("\\.");
    int i = arrayOfString.length;
    char[] arrayOfChar2 = new char[i];
    char c;
    for (c = Character.MIN_VALUE; c < i; c++)
      arrayOfChar2[c] = (char)(char)Integer.parseInt(arrayOfString[c]); 
    char c5 = (char)(i + 5 + c1 + c4);
    if (paramBoolean) {
      c = (char)(i + 5 + c1 + c4);
    } else {
      c = (char)(i + 5 + c1);
    } 
    this.mDataCodes = new DataCode[c];
    this.mDataCodes[0] = new DataCode(c5, 0);
    c = (char)(Character.MIN_VALUE ^ c5);
    this.mDataCodes[1] = new DataCode(c1, 1);
    c = (char)(c ^ c1);
    this.mDataCodes[2] = new DataCode(c2, 2);
    c = (char)(c ^ c2);
    this.mDataCodes[3] = new DataCode(c3, 3);
    c = (char)(c ^ c3);
    this.mDataCodes[4] = null;
    for (c4 = Character.MIN_VALUE; c4 < i; c4++) {
      this.mDataCodes[c4 + 5] = new DataCode(arrayOfChar2[c4], c4 + 5);
      c = (char)(arrayOfChar2[c4] ^ c);
    } 
    byte[] arrayOfByte2 = ByteUtil.getBytesByString(paramString3);
    char[] arrayOfChar1 = new char[arrayOfByte2.length];
    for (c4 = Character.MIN_VALUE; c4 < arrayOfByte2.length; c4++)
      arrayOfChar1[c4] = ByteUtil.convertByte2Uint8(arrayOfByte2[c4]); 
    for (c4 = Character.MIN_VALUE; c4 < arrayOfChar1.length; c4++) {
      this.mDataCodes[c4 + 5 + i] = new DataCode(arrayOfChar1[c4], c4 + 5 + i);
      c = (char)(arrayOfChar1[c4] ^ c);
    } 
    byte[] arrayOfByte1 = ByteUtil.getBytesByString(paramString1);
    arrayOfChar1 = new char[arrayOfByte1.length];
    c4 = Character.MIN_VALUE;
    c3 = c;
    c = c4;
    while (c < arrayOfByte1.length) {
      arrayOfChar1[c] = ByteUtil.convertByte2Uint8(arrayOfByte1[c]);
      c4 = (char)(arrayOfChar1[c] ^ c3);
      c++;
      c3 = c4;
    } 
    if (paramBoolean)
      for (c = Character.MIN_VALUE; c < arrayOfChar1.length; c++)
        this.mDataCodes[c + 5 + i + c1] = new DataCode(arrayOfChar1[c], c + 5 + i + c1);  
    this.mDataCodes[4] = new DataCode(c3, 4);
  }
  
  public byte[] getBytes() {
    byte[] arrayOfByte = new byte[this.mDataCodes.length * 6];
    for (byte b = 0; b < this.mDataCodes.length; b++)
      System.arraycopy(this.mDataCodes[b].getBytes(), 0, arrayOfByte, b * 6, 6); 
    return arrayOfByte;
  }
  
  public char[] getU8s() {
    byte[] arrayOfByte = getBytes();
    int i = arrayOfByte.length / 2;
    char[] arrayOfChar = new char[i];
    for (byte b = 0; b < i; b++)
      arrayOfChar[b] = (char)(char)(ByteUtil.combine2bytesToU16(arrayOfByte[b * 2], arrayOfByte[b * 2 + 1]) + 40); 
    return arrayOfChar;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    byte[] arrayOfByte = getBytes();
    for (byte b = 0; b < arrayOfByte.length; b++) {
      String str = ByteUtil.convertByte2HexString(arrayOfByte[b]);
      stringBuilder.append("0x");
      if (str.length() == 1)
        stringBuilder.append("0"); 
      stringBuilder.append(str).append(" ");
    } 
    return stringBuilder.toString();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/esptouch/protocol/DatumCode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */