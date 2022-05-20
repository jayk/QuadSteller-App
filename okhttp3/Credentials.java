package okhttp3;

import java.nio.charset.Charset;
import okio.ByteString;

public final class Credentials {
  public static String basic(String paramString1, String paramString2) {
    return basic(paramString1, paramString2, Charset.forName("ISO-8859-1"));
  }
  
  public static String basic(String paramString1, String paramString2, Charset paramCharset) {
    paramString1 = ByteString.of((paramString1 + ":" + paramString2).getBytes(paramCharset)).base64();
    return "Basic " + paramString1;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/Credentials.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */