package okhttp3;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import javax.net.ssl.SSLSocket;
import okhttp3.internal.Util;

public final class ConnectionSpec {
  private static final CipherSuite[] APPROVED_CIPHER_SUITES = new CipherSuite[] { 
      CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA, 
      CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_RSA_WITH_3DES_EDE_CBC_SHA };
  
  public static final ConnectionSpec CLEARTEXT;
  
  public static final ConnectionSpec COMPATIBLE_TLS;
  
  public static final ConnectionSpec MODERN_TLS = (new Builder(true)).cipherSuites(APPROVED_CIPHER_SUITES).tlsVersions(new TlsVersion[] { TlsVersion.TLS_1_3, TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0 }).supportsTlsExtensions(true).build();
  
  @Nullable
  final String[] cipherSuites;
  
  final boolean supportsTlsExtensions;
  
  final boolean tls;
  
  @Nullable
  final String[] tlsVersions;
  
  static {
    COMPATIBLE_TLS = (new Builder(MODERN_TLS)).tlsVersions(new TlsVersion[] { TlsVersion.TLS_1_0 }).supportsTlsExtensions(true).build();
    CLEARTEXT = (new Builder(false)).build();
  }
  
  ConnectionSpec(Builder paramBuilder) {
    this.tls = paramBuilder.tls;
    this.cipherSuites = paramBuilder.cipherSuites;
    this.tlsVersions = paramBuilder.tlsVersions;
    this.supportsTlsExtensions = paramBuilder.supportsTlsExtensions;
  }
  
  private ConnectionSpec supportedSpec(SSLSocket paramSSLSocket, boolean paramBoolean) {
    String[] arrayOfString2;
    String[] arrayOfString3;
    if (this.cipherSuites != null) {
      arrayOfString2 = Util.intersect(CipherSuite.ORDER_BY_NAME, paramSSLSocket.getEnabledCipherSuites(), this.cipherSuites);
    } else {
      arrayOfString2 = paramSSLSocket.getEnabledCipherSuites();
    } 
    if (this.tlsVersions != null) {
      arrayOfString3 = Util.intersect(Util.NATURAL_ORDER, paramSSLSocket.getEnabledProtocols(), this.tlsVersions);
    } else {
      arrayOfString3 = paramSSLSocket.getEnabledProtocols();
    } 
    String[] arrayOfString4 = paramSSLSocket.getSupportedCipherSuites();
    int i = Util.indexOf(CipherSuite.ORDER_BY_NAME, arrayOfString4, "TLS_FALLBACK_SCSV");
    String[] arrayOfString1 = arrayOfString2;
    if (paramBoolean) {
      arrayOfString1 = arrayOfString2;
      if (i != -1)
        arrayOfString1 = Util.concat(arrayOfString2, arrayOfString4[i]); 
    } 
    return (new Builder(this)).cipherSuites(arrayOfString1).tlsVersions(arrayOfString3).build();
  }
  
  void apply(SSLSocket paramSSLSocket, boolean paramBoolean) {
    ConnectionSpec connectionSpec = supportedSpec(paramSSLSocket, paramBoolean);
    if (connectionSpec.tlsVersions != null)
      paramSSLSocket.setEnabledProtocols(connectionSpec.tlsVersions); 
    if (connectionSpec.cipherSuites != null)
      paramSSLSocket.setEnabledCipherSuites(connectionSpec.cipherSuites); 
  }
  
  @Nullable
  public List<CipherSuite> cipherSuites() {
    return (this.cipherSuites != null) ? CipherSuite.forJavaNames(this.cipherSuites) : null;
  }
  
  public boolean equals(@Nullable Object paramObject) {
    boolean bool = false;
    if (!(paramObject instanceof ConnectionSpec))
      return bool; 
    if (paramObject == this)
      return true; 
    paramObject = paramObject;
    null = bool;
    if (this.tls == ((ConnectionSpec)paramObject).tls) {
      if (this.tls) {
        null = bool;
        if (Arrays.equals((Object[])this.cipherSuites, (Object[])((ConnectionSpec)paramObject).cipherSuites)) {
          null = bool;
          if (Arrays.equals((Object[])this.tlsVersions, (Object[])((ConnectionSpec)paramObject).tlsVersions)) {
            null = bool;
            if (this.supportsTlsExtensions == ((ConnectionSpec)paramObject).supportsTlsExtensions)
              return true; 
          } 
        } 
        return null;
      } 
    } else {
      return null;
    } 
    return true;
  }
  
  public int hashCode() {
    int i = 17;
    if (this.tls) {
      int j = Arrays.hashCode((Object[])this.cipherSuites);
      int k = Arrays.hashCode((Object[])this.tlsVersions);
      if (this.supportsTlsExtensions) {
        i = 0;
      } else {
        i = 1;
      } 
      i = ((j + 527) * 31 + k) * 31 + i;
    } 
    return i;
  }
  
  public boolean isCompatible(SSLSocket paramSSLSocket) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: aload_0
    //   3: getfield tls : Z
    //   6: ifne -> 13
    //   9: iload_2
    //   10: istore_3
    //   11: iload_3
    //   12: ireturn
    //   13: aload_0
    //   14: getfield tlsVersions : [Ljava/lang/String;
    //   17: ifnull -> 39
    //   20: iload_2
    //   21: istore_3
    //   22: getstatic okhttp3/internal/Util.NATURAL_ORDER : Ljava/util/Comparator;
    //   25: aload_0
    //   26: getfield tlsVersions : [Ljava/lang/String;
    //   29: aload_1
    //   30: invokevirtual getEnabledProtocols : ()[Ljava/lang/String;
    //   33: invokestatic nonEmptyIntersection : (Ljava/util/Comparator;[Ljava/lang/String;[Ljava/lang/String;)Z
    //   36: ifeq -> 11
    //   39: aload_0
    //   40: getfield cipherSuites : [Ljava/lang/String;
    //   43: ifnull -> 65
    //   46: iload_2
    //   47: istore_3
    //   48: getstatic okhttp3/CipherSuite.ORDER_BY_NAME : Ljava/util/Comparator;
    //   51: aload_0
    //   52: getfield cipherSuites : [Ljava/lang/String;
    //   55: aload_1
    //   56: invokevirtual getEnabledCipherSuites : ()[Ljava/lang/String;
    //   59: invokestatic nonEmptyIntersection : (Ljava/util/Comparator;[Ljava/lang/String;[Ljava/lang/String;)Z
    //   62: ifeq -> 11
    //   65: iconst_1
    //   66: istore_3
    //   67: goto -> 11
  }
  
  public boolean isTls() {
    return this.tls;
  }
  
  public boolean supportsTlsExtensions() {
    return this.supportsTlsExtensions;
  }
  
  @Nullable
  public List<TlsVersion> tlsVersions() {
    return (this.tlsVersions != null) ? TlsVersion.forJavaNames(this.tlsVersions) : null;
  }
  
  public String toString() {
    String str;
    if (!this.tls)
      return "ConnectionSpec()"; 
    if (this.cipherSuites != null) {
      null = cipherSuites().toString();
    } else {
      null = "[all enabled]";
    } 
    if (this.tlsVersions != null) {
      str = tlsVersions().toString();
    } else {
      str = "[all enabled]";
    } 
    return "ConnectionSpec(cipherSuites=" + null + ", tlsVersions=" + str + ", supportsTlsExtensions=" + this.supportsTlsExtensions + ")";
  }
  
  public static final class Builder {
    @Nullable
    String[] cipherSuites;
    
    boolean supportsTlsExtensions;
    
    boolean tls;
    
    @Nullable
    String[] tlsVersions;
    
    public Builder(ConnectionSpec param1ConnectionSpec) {
      this.tls = param1ConnectionSpec.tls;
      this.cipherSuites = param1ConnectionSpec.cipherSuites;
      this.tlsVersions = param1ConnectionSpec.tlsVersions;
      this.supportsTlsExtensions = param1ConnectionSpec.supportsTlsExtensions;
    }
    
    Builder(boolean param1Boolean) {
      this.tls = param1Boolean;
    }
    
    public Builder allEnabledCipherSuites() {
      if (!this.tls)
        throw new IllegalStateException("no cipher suites for cleartext connections"); 
      this.cipherSuites = null;
      return this;
    }
    
    public Builder allEnabledTlsVersions() {
      if (!this.tls)
        throw new IllegalStateException("no TLS versions for cleartext connections"); 
      this.tlsVersions = null;
      return this;
    }
    
    public ConnectionSpec build() {
      return new ConnectionSpec(this);
    }
    
    public Builder cipherSuites(String... param1VarArgs) {
      if (!this.tls)
        throw new IllegalStateException("no cipher suites for cleartext connections"); 
      if (param1VarArgs.length == 0)
        throw new IllegalArgumentException("At least one cipher suite is required"); 
      this.cipherSuites = (String[])param1VarArgs.clone();
      return this;
    }
    
    public Builder cipherSuites(CipherSuite... param1VarArgs) {
      if (!this.tls)
        throw new IllegalStateException("no cipher suites for cleartext connections"); 
      String[] arrayOfString = new String[param1VarArgs.length];
      for (byte b = 0; b < param1VarArgs.length; b++)
        arrayOfString[b] = (param1VarArgs[b]).javaName; 
      return cipherSuites(arrayOfString);
    }
    
    public Builder supportsTlsExtensions(boolean param1Boolean) {
      if (!this.tls)
        throw new IllegalStateException("no TLS extensions for cleartext connections"); 
      this.supportsTlsExtensions = param1Boolean;
      return this;
    }
    
    public Builder tlsVersions(String... param1VarArgs) {
      if (!this.tls)
        throw new IllegalStateException("no TLS versions for cleartext connections"); 
      if (param1VarArgs.length == 0)
        throw new IllegalArgumentException("At least one TLS version is required"); 
      this.tlsVersions = (String[])param1VarArgs.clone();
      return this;
    }
    
    public Builder tlsVersions(TlsVersion... param1VarArgs) {
      if (!this.tls)
        throw new IllegalStateException("no TLS versions for cleartext connections"); 
      String[] arrayOfString = new String[param1VarArgs.length];
      for (byte b = 0; b < param1VarArgs.length; b++)
        arrayOfString[b] = (param1VarArgs[b]).javaName; 
      return tlsVersions(arrayOfString);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/ConnectionSpec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */