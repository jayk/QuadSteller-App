package okhttp3.internal.http2;

import java.io.IOException;
import okhttp3.internal.Util;
import okio.ByteString;

public final class Http2 {
  static final String[] BINARY;
  
  static final ByteString CONNECTION_PREFACE = ByteString.encodeUtf8("PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n");
  
  static final String[] FLAGS;
  
  static final byte FLAG_ACK = 1;
  
  static final byte FLAG_COMPRESSED = 32;
  
  static final byte FLAG_END_HEADERS = 4;
  
  static final byte FLAG_END_PUSH_PROMISE = 4;
  
  static final byte FLAG_END_STREAM = 1;
  
  static final byte FLAG_NONE = 0;
  
  static final byte FLAG_PADDED = 8;
  
  static final byte FLAG_PRIORITY = 32;
  
  private static final String[] FRAME_NAMES = new String[] { "DATA", "HEADERS", "PRIORITY", "RST_STREAM", "SETTINGS", "PUSH_PROMISE", "PING", "GOAWAY", "WINDOW_UPDATE", "CONTINUATION" };
  
  static final int INITIAL_MAX_FRAME_SIZE = 16384;
  
  static final byte TYPE_CONTINUATION = 9;
  
  static final byte TYPE_DATA = 0;
  
  static final byte TYPE_GOAWAY = 7;
  
  static final byte TYPE_HEADERS = 1;
  
  static final byte TYPE_PING = 6;
  
  static final byte TYPE_PRIORITY = 2;
  
  static final byte TYPE_PUSH_PROMISE = 5;
  
  static final byte TYPE_RST_STREAM = 3;
  
  static final byte TYPE_SETTINGS = 4;
  
  static final byte TYPE_WINDOW_UPDATE = 8;
  
  static {
    FLAGS = new String[64];
    BINARY = new String[256];
    byte b;
    for (b = 0; b < BINARY.length; b++) {
      BINARY[b] = Util.format("%8s", new Object[] { Integer.toBinaryString(b) }).replace(' ', '0');
    } 
    FLAGS[0] = "";
    FLAGS[1] = "END_STREAM";
    int[] arrayOfInt1 = new int[1];
    arrayOfInt1[0] = 1;
    FLAGS[8] = "PADDED";
    int i = arrayOfInt1.length;
    for (b = 0; b < i; b++) {
      int k = arrayOfInt1[b];
      FLAGS[k | 0x8] = FLAGS[k] + "|PADDED";
    } 
    FLAGS[4] = "END_HEADERS";
    FLAGS[32] = "PRIORITY";
    FLAGS[36] = "END_HEADERS|PRIORITY";
    int[] arrayOfInt2 = new int[3];
    arrayOfInt2[0] = 4;
    arrayOfInt2[1] = 32;
    arrayOfInt2[2] = 36;
    int j = arrayOfInt2.length;
    for (b = 0; b < j; b++) {
      int k = arrayOfInt2[b];
      int m = arrayOfInt1.length;
      for (i = 0; i < m; i++) {
        int n = arrayOfInt1[i];
        FLAGS[n | k] = FLAGS[n] + '|' + FLAGS[k];
        FLAGS[n | k | 0x8] = FLAGS[n] + '|' + FLAGS[k] + "|PADDED";
      } 
    } 
    for (b = 0; b < FLAGS.length; b++) {
      if (FLAGS[b] == null)
        FLAGS[b] = BINARY[b]; 
    } 
  }
  
  static String formatFlags(byte paramByte1, byte paramByte2) {
    String str;
    if (paramByte2 == 0)
      return ""; 
    switch (paramByte1) {
      default:
        if (paramByte2 < FLAGS.length) {
          null = FLAGS[paramByte2];
        } else {
          null = BINARY[paramByte2];
        } 
        if (paramByte1 == 5 && (paramByte2 & 0x4) != 0)
          return null.replace("HEADERS", "PUSH_PROMISE"); 
        break;
      case 4:
      case 6:
        return (paramByte2 == 1) ? "ACK" : BINARY[paramByte2];
      case 2:
      case 3:
      case 7:
      case 8:
        return BINARY[paramByte2];
    } 
    if (paramByte1 == 0 && (paramByte2 & 0x20) != 0)
      str = SYNTHETIC_LOCAL_VARIABLE_2.replace("PRIORITY", "COMPRESSED"); 
    return str;
  }
  
  static String frameLog(boolean paramBoolean, int paramInt1, int paramInt2, byte paramByte1, byte paramByte2) {
    String str1;
    if (paramByte1 < FRAME_NAMES.length) {
      str1 = FRAME_NAMES[paramByte1];
    } else {
      str1 = Util.format("0x%02x", new Object[] { Byte.valueOf(paramByte1) });
    } 
    String str2 = formatFlags(paramByte1, paramByte2);
    if (paramBoolean) {
      String str = "<<";
      return Util.format("%s 0x%08x %5d %-13s %s", new Object[] { str, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), str1, str2 });
    } 
    String str3 = ">>";
    return Util.format("%s 0x%08x %5d %-13s %s", new Object[] { str3, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), str1, str2 });
  }
  
  static IllegalArgumentException illegalArgument(String paramString, Object... paramVarArgs) {
    throw new IllegalArgumentException(Util.format(paramString, paramVarArgs));
  }
  
  static IOException ioException(String paramString, Object... paramVarArgs) throws IOException {
    throw new IOException(Util.format(paramString, paramVarArgs));
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/http2/Http2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */