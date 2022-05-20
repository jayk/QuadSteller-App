package okhttp3.internal.tls;

import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;

public final class BasicCertificateChainCleaner extends CertificateChainCleaner {
  private static final int MAX_SIGNERS = 9;
  
  private final TrustRootIndex trustRootIndex;
  
  public BasicCertificateChainCleaner(TrustRootIndex paramTrustRootIndex) {
    this.trustRootIndex = paramTrustRootIndex;
  }
  
  private boolean verifySignature(X509Certificate paramX509Certificate1, X509Certificate paramX509Certificate2) {
    boolean bool = false;
    if (paramX509Certificate1.getIssuerDN().equals(paramX509Certificate2.getSubjectDN()))
      try {
        paramX509Certificate1.verify(paramX509Certificate2.getPublicKey());
        bool = true;
      } catch (GeneralSecurityException generalSecurityException) {} 
    return bool;
  }
  
  public List<Certificate> clean(List<Certificate> paramList, String paramString) throws SSLPeerUnverifiedException {
    ArrayDeque<Certificate> arrayDeque = new ArrayDeque<Certificate>(paramList);
    ArrayList<X509Certificate> arrayList = new ArrayList();
    arrayList.add(arrayDeque.removeFirst());
    boolean bool = false;
    byte b = 0;
    label24: while (b < 9) {
      X509Certificate x509Certificate1 = arrayList.get(arrayList.size() - 1);
      X509Certificate x509Certificate2 = this.trustRootIndex.findByIssuerAndSignature(x509Certificate1);
      if (x509Certificate2 != null) {
        if (arrayList.size() > 1 || !x509Certificate1.equals(x509Certificate2))
          arrayList.add(x509Certificate2); 
        if (!verifySignature(x509Certificate2, x509Certificate2)) {
          bool = true;
          b++;
          continue;
        } 
        return (List)arrayList;
      } 
      Iterator<Certificate> iterator = arrayDeque.iterator();
      while (iterator.hasNext()) {
        X509Certificate x509Certificate = (X509Certificate)iterator.next();
        if (verifySignature(x509Certificate1, x509Certificate)) {
          iterator.remove();
          arrayList.add(x509Certificate);
          continue label24;
        } 
      } 
      if (!bool)
        throw new SSLPeerUnverifiedException("Failed to find a trusted cert that signed " + x509Certificate1); 
      return (List)arrayList;
    } 
    throw new SSLPeerUnverifiedException("Certificate chain too long: " + arrayList);
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = true;
    if (paramObject != this && (!(paramObject instanceof BasicCertificateChainCleaner) || !((BasicCertificateChainCleaner)paramObject).trustRootIndex.equals(this.trustRootIndex)))
      bool = false; 
    return bool;
  }
  
  public int hashCode() {
    return this.trustRootIndex.hashCode();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/tls/BasicCertificateChainCleaner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */