package okhttp3.internal.ws;

import okio.ByteString;

public final class WebSocketProtocol {
  static final String ACCEPT_MAGIC = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
  
  static final int B0_FLAG_FIN = 128;
  
  static final int B0_FLAG_RSV1 = 64;
  
  static final int B0_FLAG_RSV2 = 32;
  
  static final int B0_FLAG_RSV3 = 16;
  
  static final int B0_MASK_OPCODE = 15;
  
  static final int B1_FLAG_MASK = 128;
  
  static final int B1_MASK_LENGTH = 127;
  
  static final int CLOSE_ABNORMAL_TERMINATION = 1006;
  
  static final int CLOSE_CLIENT_GOING_AWAY = 1001;
  
  static final long CLOSE_MESSAGE_MAX = 123L;
  
  static final int CLOSE_NO_STATUS_CODE = 1005;
  
  static final int CLOSE_PROTOCOL_EXCEPTION = 1002;
  
  static final int OPCODE_BINARY = 2;
  
  static final int OPCODE_CONTINUATION = 0;
  
  static final int OPCODE_CONTROL_CLOSE = 8;
  
  static final int OPCODE_CONTROL_PING = 9;
  
  static final int OPCODE_CONTROL_PONG = 10;
  
  static final int OPCODE_FLAG_CONTROL = 8;
  
  static final int OPCODE_TEXT = 1;
  
  static final long PAYLOAD_BYTE_MAX = 125L;
  
  static final int PAYLOAD_LONG = 127;
  
  static final int PAYLOAD_SHORT = 126;
  
  static final long PAYLOAD_SHORT_MAX = 65535L;
  
  private WebSocketProtocol() {
    throw new AssertionError("No instances.");
  }
  
  public static String acceptHeader(String paramString) {
    return ByteString.encodeUtf8(paramString + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").sha1().base64();
  }
  
  static String closeCodeExceptionMessage(int paramInt) {
    return (paramInt < 1000 || paramInt >= 5000) ? ("Code must be in range [1000,5000): " + paramInt) : (((paramInt >= 1004 && paramInt <= 1006) || (paramInt >= 1012 && paramInt <= 2999)) ? ("Code " + paramInt + " is reserved and may not be used.") : null);
  }
  
  static void toggleMask(byte[] paramArrayOfbyte1, long paramLong1, byte[] paramArrayOfbyte2, long paramLong2) {
    int i = paramArrayOfbyte2.length;
    byte b = 0;
    while (b < paramLong1) {
      int j = (int)(paramLong2 % i);
      paramArrayOfbyte1[b] = (byte)(byte)(paramArrayOfbyte1[b] ^ paramArrayOfbyte2[j]);
      b++;
      paramLong2++;
    } 
  }
  
  static void validateCloseCode(int paramInt) {
    String str = closeCodeExceptionMessage(paramInt);
    if (str != null)
      throw new IllegalArgumentException(str); 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/ws/WebSocketProtocol.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */