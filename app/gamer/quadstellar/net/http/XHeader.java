package app.gamer.quadstellar.net.http;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;

public class XHeader implements Header {
  private String name;
  
  private String value;
  
  public XHeader(String paramString1, String paramString2, HeaderElement[] paramArrayOfHeaderElement) {
    this.value = paramString2;
    this.name = paramString1;
  }
  
  public HeaderElement[] getElements() throws ParseException {
    return null;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getValue() {
    return this.value;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/net/http/XHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */