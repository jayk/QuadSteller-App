package okhttp3;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import javax.net.ssl.SSLPeerUnverifiedException;
import okhttp3.internal.Util;
import okhttp3.internal.tls.CertificateChainCleaner;
import okio.ByteString;

public final class CertificatePinner {
  public static final CertificatePinner DEFAULT = (new Builder()).build();
  
  @Nullable
  private final CertificateChainCleaner certificateChainCleaner;
  
  private final Set<Pin> pins;
  
  CertificatePinner(Set<Pin> paramSet, @Nullable CertificateChainCleaner paramCertificateChainCleaner) {
    this.pins = paramSet;
    this.certificateChainCleaner = paramCertificateChainCleaner;
  }
  
  public static String pin(Certificate paramCertificate) {
    if (!(paramCertificate instanceof X509Certificate))
      throw new IllegalArgumentException("Certificate pinning requires X509 certificates"); 
    return "sha256/" + sha256((X509Certificate)paramCertificate).base64();
  }
  
  static ByteString sha1(X509Certificate paramX509Certificate) {
    return ByteString.of(paramX509Certificate.getPublicKey().getEncoded()).sha1();
  }
  
  static ByteString sha256(X509Certificate paramX509Certificate) {
    return ByteString.of(paramX509Certificate.getPublicKey().getEncoded()).sha256();
  }
  
  public void check(String paramString, List<Certificate> paramList) throws SSLPeerUnverifiedException {
    List<Pin> list = findMatchingPins(paramString);
    if (!list.isEmpty()) {
      List<Certificate> list1 = paramList;
      if (this.certificateChainCleaner != null)
        list1 = this.certificateChainCleaner.clean(paramList, paramString); 
      byte b = 0;
      int i = list1.size();
      while (b < i) {
        X509Certificate x509Certificate = (X509Certificate)list1.get(b);
        ByteString byteString = null;
        paramList = null;
        byte b1 = 0;
        int k = list.size();
        while (b1 < k) {
          Pin pin = list.get(b1);
          if (pin.hashAlgorithm.equals("sha256/")) {
            ByteString byteString1;
            List<Certificate> list2 = paramList;
            if (paramList == null)
              byteString1 = sha256(x509Certificate); 
            if (!pin.hash.equals(byteString1)) {
              ByteString byteString2 = byteString1;
            } else {
              return;
            } 
          } else if (pin.hashAlgorithm.equals("sha1/")) {
            ByteString byteString1 = byteString;
            if (byteString == null)
              byteString1 = sha1(x509Certificate); 
            byteString = byteString1;
            if (pin.hash.equals(byteString1))
              return; 
          } else {
            throw new AssertionError();
          } 
          b1++;
        } 
        b++;
      } 
      StringBuilder stringBuilder = (new StringBuilder()).append("Certificate pinning failure!").append("\n  Peer certificate chain:");
      b = 0;
      int j = list1.size();
      while (b < j) {
        X509Certificate x509Certificate = (X509Certificate)list1.get(b);
        stringBuilder.append("\n    ").append(pin(x509Certificate)).append(": ").append(x509Certificate.getSubjectDN().getName());
        b++;
      } 
      stringBuilder.append("\n  Pinned certificates for ").append(paramString).append(":");
      b = 0;
      j = list.size();
      while (b < j) {
        Pin pin = list.get(b);
        stringBuilder.append("\n    ").append(pin);
        b++;
      } 
      throw new SSLPeerUnverifiedException(stringBuilder.toString());
    } 
  }
  
  public void check(String paramString, Certificate... paramVarArgs) throws SSLPeerUnverifiedException {
    check(paramString, Arrays.asList(paramVarArgs));
  }
  
  public boolean equals(@Nullable Object paramObject) {
    boolean bool = true;
    if (paramObject != this) {
      if (paramObject instanceof CertificatePinner && Util.equal(this.certificateChainCleaner, ((CertificatePinner)paramObject).certificateChainCleaner) && this.pins.equals(((CertificatePinner)paramObject).pins))
        return true; 
      bool = false;
    } 
    return bool;
  }
  
  List<Pin> findMatchingPins(String paramString) {
    List<?> list = Collections.emptyList();
    for (Pin pin : this.pins) {
      if (pin.matches(paramString)) {
        List<?> list1 = list;
        if (list.isEmpty())
          list1 = new ArrayList(); 
        list1.add(pin);
        list = list1;
      } 
    } 
    return (List)list;
  }
  
  public int hashCode() {
    if (this.certificateChainCleaner != null) {
      int i = this.certificateChainCleaner.hashCode();
      return i * 31 + this.pins.hashCode();
    } 
    byte b = 0;
    return b * 31 + this.pins.hashCode();
  }
  
  CertificatePinner withCertificateChainCleaner(CertificateChainCleaner paramCertificateChainCleaner) {
    return Util.equal(this.certificateChainCleaner, paramCertificateChainCleaner) ? this : new CertificatePinner(this.pins, paramCertificateChainCleaner);
  }
  
  public static final class Builder {
    private final List<CertificatePinner.Pin> pins = new ArrayList<CertificatePinner.Pin>();
    
    public Builder add(String param1String, String... param1VarArgs) {
      if (param1String == null)
        throw new NullPointerException("pattern == null"); 
      int i = param1VarArgs.length;
      for (byte b = 0; b < i; b++) {
        String str = param1VarArgs[b];
        this.pins.add(new CertificatePinner.Pin(param1String, str));
      } 
      return this;
    }
    
    public CertificatePinner build() {
      return new CertificatePinner(new LinkedHashSet<CertificatePinner.Pin>(this.pins), null);
    }
  }
  
  static final class Pin {
    private static final String WILDCARD = "*.";
    
    final String canonicalHostname;
    
    final ByteString hash;
    
    final String hashAlgorithm;
    
    final String pattern;
    
    Pin(String param1String1, String param1String2) {
      this.pattern = param1String1;
      if (param1String1.startsWith("*.")) {
        param1String1 = HttpUrl.parse("http://" + param1String1.substring("*.".length())).host();
      } else {
        param1String1 = HttpUrl.parse("http://" + param1String1).host();
      } 
      this.canonicalHostname = param1String1;
      if (param1String2.startsWith("sha1/")) {
        this.hashAlgorithm = "sha1/";
        this.hash = ByteString.decodeBase64(param1String2.substring("sha1/".length()));
      } else if (param1String2.startsWith("sha256/")) {
        this.hashAlgorithm = "sha256/";
        this.hash = ByteString.decodeBase64(param1String2.substring("sha256/".length()));
      } else {
        throw new IllegalArgumentException("pins must start with 'sha256/' or 'sha1/': " + param1String2);
      } 
      if (this.hash == null)
        throw new IllegalArgumentException("pins must be base64: " + param1String2); 
    }
    
    public boolean equals(Object param1Object) {
      return (param1Object instanceof Pin && this.pattern.equals(((Pin)param1Object).pattern) && this.hashAlgorithm.equals(((Pin)param1Object).hashAlgorithm) && this.hash.equals(((Pin)param1Object).hash));
    }
    
    public int hashCode() {
      return ((this.pattern.hashCode() + 527) * 31 + this.hashAlgorithm.hashCode()) * 31 + this.hash.hashCode();
    }
    
    boolean matches(String param1String) {
      return this.pattern.startsWith("*.") ? param1String.regionMatches(false, param1String.indexOf('.') + 1, this.canonicalHostname, 0, this.canonicalHostname.length()) : param1String.equals(this.canonicalHostname);
    }
    
    public String toString() {
      return this.hashAlgorithm + this.hash.base64();
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/CertificatePinner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */