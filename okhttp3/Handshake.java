package okhttp3;

import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.annotation.Nullable;
import javax.net.ssl.SSLSession;
import okhttp3.internal.Util;

public final class Handshake {
  private final CipherSuite cipherSuite;
  
  private final List<Certificate> localCertificates;
  
  private final List<Certificate> peerCertificates;
  
  private final TlsVersion tlsVersion;
  
  private Handshake(TlsVersion paramTlsVersion, CipherSuite paramCipherSuite, List<Certificate> paramList1, List<Certificate> paramList2) {
    this.tlsVersion = paramTlsVersion;
    this.cipherSuite = paramCipherSuite;
    this.peerCertificates = paramList1;
    this.localCertificates = paramList2;
  }
  
  public static Handshake get(SSLSession paramSSLSession) {
    // Byte code:
    //   0: aload_0
    //   1: invokeinterface getCipherSuite : ()Ljava/lang/String;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnonnull -> 21
    //   11: new java/lang/IllegalStateException
    //   14: dup
    //   15: ldc 'cipherSuite == null'
    //   17: invokespecial <init> : (Ljava/lang/String;)V
    //   20: athrow
    //   21: aload_1
    //   22: invokestatic forJavaName : (Ljava/lang/String;)Lokhttp3/CipherSuite;
    //   25: astore_2
    //   26: aload_0
    //   27: invokeinterface getProtocol : ()Ljava/lang/String;
    //   32: astore_1
    //   33: aload_1
    //   34: ifnonnull -> 47
    //   37: new java/lang/IllegalStateException
    //   40: dup
    //   41: ldc 'tlsVersion == null'
    //   43: invokespecial <init> : (Ljava/lang/String;)V
    //   46: athrow
    //   47: aload_1
    //   48: invokestatic forJavaName : (Ljava/lang/String;)Lokhttp3/TlsVersion;
    //   51: astore_3
    //   52: aload_0
    //   53: invokeinterface getPeerCertificates : ()[Ljava/security/cert/Certificate;
    //   58: astore_1
    //   59: aload_1
    //   60: ifnull -> 102
    //   63: aload_1
    //   64: invokestatic immutableList : ([Ljava/lang/Object;)Ljava/util/List;
    //   67: astore_1
    //   68: aload_0
    //   69: invokeinterface getLocalCertificates : ()[Ljava/security/cert/Certificate;
    //   74: astore_0
    //   75: aload_0
    //   76: ifnull -> 109
    //   79: aload_0
    //   80: invokestatic immutableList : ([Ljava/lang/Object;)Ljava/util/List;
    //   83: astore_0
    //   84: new okhttp3/Handshake
    //   87: dup
    //   88: aload_3
    //   89: aload_2
    //   90: aload_1
    //   91: aload_0
    //   92: invokespecial <init> : (Lokhttp3/TlsVersion;Lokhttp3/CipherSuite;Ljava/util/List;Ljava/util/List;)V
    //   95: areturn
    //   96: astore_1
    //   97: aconst_null
    //   98: astore_1
    //   99: goto -> 59
    //   102: invokestatic emptyList : ()Ljava/util/List;
    //   105: astore_1
    //   106: goto -> 68
    //   109: invokestatic emptyList : ()Ljava/util/List;
    //   112: astore_0
    //   113: goto -> 84
    // Exception table:
    //   from	to	target	type
    //   52	59	96	javax/net/ssl/SSLPeerUnverifiedException
  }
  
  public static Handshake get(TlsVersion paramTlsVersion, CipherSuite paramCipherSuite, List<Certificate> paramList1, List<Certificate> paramList2) {
    if (paramTlsVersion == null)
      throw new NullPointerException("tlsVersion == null"); 
    if (paramCipherSuite == null)
      throw new NullPointerException("cipherSuite == null"); 
    return new Handshake(paramTlsVersion, paramCipherSuite, Util.immutableList(paramList1), Util.immutableList(paramList2));
  }
  
  public CipherSuite cipherSuite() {
    return this.cipherSuite;
  }
  
  public boolean equals(@Nullable Object paramObject) {
    boolean bool1 = false;
    if (!(paramObject instanceof Handshake))
      return bool1; 
    paramObject = paramObject;
    boolean bool2 = bool1;
    if (this.tlsVersion.equals(((Handshake)paramObject).tlsVersion)) {
      bool2 = bool1;
      if (this.cipherSuite.equals(((Handshake)paramObject).cipherSuite)) {
        bool2 = bool1;
        if (this.peerCertificates.equals(((Handshake)paramObject).peerCertificates)) {
          bool2 = bool1;
          if (this.localCertificates.equals(((Handshake)paramObject).localCertificates))
            bool2 = true; 
        } 
      } 
    } 
    return bool2;
  }
  
  public int hashCode() {
    return (((this.tlsVersion.hashCode() + 527) * 31 + this.cipherSuite.hashCode()) * 31 + this.peerCertificates.hashCode()) * 31 + this.localCertificates.hashCode();
  }
  
  public List<Certificate> localCertificates() {
    return this.localCertificates;
  }
  
  @Nullable
  public Principal localPrincipal() {
    return !this.localCertificates.isEmpty() ? ((X509Certificate)this.localCertificates.get(0)).getSubjectX500Principal() : null;
  }
  
  public List<Certificate> peerCertificates() {
    return this.peerCertificates;
  }
  
  @Nullable
  public Principal peerPrincipal() {
    return !this.peerCertificates.isEmpty() ? ((X509Certificate)this.peerCertificates.get(0)).getSubjectX500Principal() : null;
  }
  
  public TlsVersion tlsVersion() {
    return this.tlsVersion;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/Handshake.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */