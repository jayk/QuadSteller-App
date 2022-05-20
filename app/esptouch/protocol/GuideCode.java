package app.esptouch.protocol;

import app.esptouch.task.ICodeData;
import app.esptouch.util.ByteUtil;

public class GuideCode implements ICodeData {
  public static final int GUIDE_CODE_LEN = 4;
  
  public byte[] getBytes() {
    throw new RuntimeException("DataCode don't support getBytes()");
  }
  
  public char[] getU8s() {
    return new char[] { 'ȃ', 'Ȃ', 'ȁ', 'Ȁ' };
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    char[] arrayOfChar = getU8s();
    for (byte b = 0; b < 4; b++) {
      String str = ByteUtil.convertU8ToHexString(arrayOfChar[b]);
      stringBuilder.append("0x");
      if (str.length() == 1)
        stringBuilder.append("0"); 
      stringBuilder.append(str).append(" ");
    } 
    return stringBuilder.toString();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/esptouch/protocol/GuideCode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */