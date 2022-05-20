package okhttp3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum TlsVersion {
  SSL_3_0,
  TLS_1_0,
  TLS_1_1,
  TLS_1_2,
  TLS_1_3("TLSv1.3");
  
  final String javaName;
  
  static {
    TLS_1_2 = new TlsVersion("TLS_1_2", 1, "TLSv1.2");
    TLS_1_1 = new TlsVersion("TLS_1_1", 2, "TLSv1.1");
    TLS_1_0 = new TlsVersion("TLS_1_0", 3, "TLSv1");
    SSL_3_0 = new TlsVersion("SSL_3_0", 4, "SSLv3");
    $VALUES = new TlsVersion[] { TLS_1_3, TLS_1_2, TLS_1_1, TLS_1_0, SSL_3_0 };
  }
  
  TlsVersion(String paramString1) {
    this.javaName = paramString1;
  }
  
  public static TlsVersion forJavaName(String paramString) {
    byte b = -1;
    switch (paramString.hashCode()) {
      default:
        switch (b) {
          default:
            throw new IllegalArgumentException("Unexpected TLS version: " + paramString);
          case 0:
            return TLS_1_3;
          case 1:
            return TLS_1_2;
          case 2:
            return TLS_1_1;
          case 3:
            return TLS_1_0;
          case 4:
            break;
        } 
        break;
      case -503070501:
        if (paramString.equals("TLSv1.3"))
          b = 0; 
      case -503070502:
        if (paramString.equals("TLSv1.2"))
          b = 1; 
      case -503070503:
        if (paramString.equals("TLSv1.1"))
          b = 2; 
      case 79923350:
        if (paramString.equals("TLSv1"))
          b = 3; 
      case 79201641:
        if (paramString.equals("SSLv3"))
          b = 4; 
    } 
    return SSL_3_0;
  }
  
  static List<TlsVersion> forJavaNames(String... paramVarArgs) {
    ArrayList<TlsVersion> arrayList = new ArrayList(paramVarArgs.length);
    int i = paramVarArgs.length;
    for (byte b = 0; b < i; b++)
      arrayList.add(forJavaName(paramVarArgs[b])); 
    return Collections.unmodifiableList(arrayList);
  }
  
  public String javaName() {
    return this.javaName;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/TlsVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */