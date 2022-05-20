package okhttp3.internal.http;

import java.io.IOException;
import java.net.ProtocolException;
import okhttp3.Protocol;
import okhttp3.Response;

public final class StatusLine {
  public static final int HTTP_CONTINUE = 100;
  
  public static final int HTTP_PERM_REDIRECT = 308;
  
  public static final int HTTP_TEMP_REDIRECT = 307;
  
  public final int code;
  
  public final String message;
  
  public final Protocol protocol;
  
  public StatusLine(Protocol paramProtocol, int paramInt, String paramString) {
    this.protocol = paramProtocol;
    this.code = paramInt;
    this.message = paramString;
  }
  
  public static StatusLine get(Response paramResponse) {
    return new StatusLine(paramResponse.protocol(), paramResponse.code(), paramResponse.message());
  }
  
  public static StatusLine parse(String paramString) throws IOException {
    int i;
    byte b;
    String str;
    if (paramString.startsWith("HTTP/1.")) {
      if (paramString.length() < 9 || paramString.charAt(8) != ' ')
        throw new ProtocolException("Unexpected status line: " + paramString); 
      i = paramString.charAt(7) - 48;
      b = 9;
      if (i == 0) {
        Protocol protocol = Protocol.HTTP_1_0;
      } else if (i == 1) {
        Protocol protocol = Protocol.HTTP_1_1;
      } else {
        throw new ProtocolException("Unexpected status line: " + paramString);
      } 
    } else if (paramString.startsWith("ICY ")) {
      Protocol protocol = Protocol.HTTP_1_0;
      b = 4;
    } else {
      throw new ProtocolException("Unexpected status line: " + paramString);
    } 
    if (paramString.length() < b + 3)
      throw new ProtocolException("Unexpected status line: " + paramString); 
    try {
      i = Integer.parseInt(paramString.substring(b, b + 3));
      str = "";
      if (paramString.length() > b + 3) {
        if (paramString.charAt(b + 3) != ' ')
          throw new ProtocolException("Unexpected status line: " + paramString); 
        str = paramString.substring(b + 4);
      } 
    } catch (NumberFormatException numberFormatException) {
      throw new ProtocolException("Unexpected status line: " + paramString);
    } 
    return new StatusLine((Protocol)numberFormatException, i, str);
  }
  
  public String toString() {
    String str;
    StringBuilder stringBuilder = new StringBuilder();
    if (this.protocol == Protocol.HTTP_1_0) {
      str = "HTTP/1.0";
    } else {
      str = "HTTP/1.1";
    } 
    stringBuilder.append(str);
    stringBuilder.append(' ').append(this.code);
    if (this.message != null)
      stringBuilder.append(' ').append(this.message); 
    return stringBuilder.toString();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/http/StatusLine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */