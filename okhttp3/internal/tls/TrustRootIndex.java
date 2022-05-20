package okhttp3.internal.tls;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;

public abstract class TrustRootIndex {
  public static TrustRootIndex get(X509TrustManager paramX509TrustManager) {
    TrustRootIndex trustRootIndex;
    try {
      Method method = paramX509TrustManager.getClass().getDeclaredMethod("findTrustAnchorByIssuerAndSignature", new Class[] { X509Certificate.class });
      method.setAccessible(true);
      AndroidTrustRootIndex androidTrustRootIndex = new AndroidTrustRootIndex();
      this(paramX509TrustManager, method);
      trustRootIndex = androidTrustRootIndex;
    } catch (NoSuchMethodException noSuchMethodException) {
      trustRootIndex = get(trustRootIndex.getAcceptedIssuers());
    } 
    return trustRootIndex;
  }
  
  public static TrustRootIndex get(X509Certificate... paramVarArgs) {
    return new BasicTrustRootIndex(paramVarArgs);
  }
  
  public abstract X509Certificate findByIssuerAndSignature(X509Certificate paramX509Certificate);
  
  static final class AndroidTrustRootIndex extends TrustRootIndex {
    private final Method findByIssuerAndSignatureMethod;
    
    private final X509TrustManager trustManager;
    
    AndroidTrustRootIndex(X509TrustManager param1X509TrustManager, Method param1Method) {
      this.findByIssuerAndSignatureMethod = param1Method;
      this.trustManager = param1X509TrustManager;
    }
    
    public boolean equals(Object param1Object) {
      boolean bool = true;
      if (param1Object != this) {
        if (!(param1Object instanceof AndroidTrustRootIndex))
          return false; 
        param1Object = param1Object;
        if (!this.trustManager.equals(((AndroidTrustRootIndex)param1Object).trustManager) || !this.findByIssuerAndSignatureMethod.equals(((AndroidTrustRootIndex)param1Object).findByIssuerAndSignatureMethod))
          bool = false; 
      } 
      return bool;
    }
    
    public X509Certificate findByIssuerAndSignature(X509Certificate param1X509Certificate) {
      X509Certificate x509Certificate1;
      X509Certificate x509Certificate2 = null;
      try {
        TrustAnchor trustAnchor = (TrustAnchor)this.findByIssuerAndSignatureMethod.invoke(this.trustManager, new Object[] { param1X509Certificate });
        param1X509Certificate = x509Certificate2;
        if (trustAnchor != null)
          param1X509Certificate = trustAnchor.getTrustedCert(); 
      } catch (IllegalAccessException illegalAccessException) {
        throw new AssertionError();
      } catch (InvocationTargetException invocationTargetException) {
        x509Certificate1 = x509Certificate2;
      } 
      return x509Certificate1;
    }
    
    public int hashCode() {
      return this.trustManager.hashCode() + this.findByIssuerAndSignatureMethod.hashCode() * 31;
    }
  }
  
  static final class BasicTrustRootIndex extends TrustRootIndex {
    private final Map<X500Principal, Set<X509Certificate>> subjectToCaCerts = new LinkedHashMap<X500Principal, Set<X509Certificate>>();
    
    BasicTrustRootIndex(X509Certificate... param1VarArgs) {
      int i = param1VarArgs.length;
      for (byte b = 0; b < i; b++) {
        X509Certificate x509Certificate = param1VarArgs[b];
        X500Principal x500Principal = x509Certificate.getSubjectX500Principal();
        Set<X509Certificate> set1 = this.subjectToCaCerts.get(x500Principal);
        Set<X509Certificate> set2 = set1;
        if (set1 == null) {
          set2 = new LinkedHashSet(1);
          this.subjectToCaCerts.put(x500Principal, set2);
        } 
        set2.add(x509Certificate);
      } 
    }
    
    public boolean equals(Object param1Object) {
      boolean bool = true;
      if (param1Object != this && (!(param1Object instanceof BasicTrustRootIndex) || !((BasicTrustRootIndex)param1Object).subjectToCaCerts.equals(this.subjectToCaCerts)))
        bool = false; 
      return bool;
    }
    
    public X509Certificate findByIssuerAndSignature(X509Certificate param1X509Certificate) {
      // Byte code:
      //   0: aload_1
      //   1: invokevirtual getIssuerX500Principal : ()Ljavax/security/auth/x500/X500Principal;
      //   4: astore_2
      //   5: aload_0
      //   6: getfield subjectToCaCerts : Ljava/util/Map;
      //   9: aload_2
      //   10: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
      //   15: checkcast java/util/Set
      //   18: astore_2
      //   19: aload_2
      //   20: ifnonnull -> 27
      //   23: aconst_null
      //   24: astore_1
      //   25: aload_1
      //   26: areturn
      //   27: aload_2
      //   28: invokeinterface iterator : ()Ljava/util/Iterator;
      //   33: astore_3
      //   34: aload_3
      //   35: invokeinterface hasNext : ()Z
      //   40: ifeq -> 74
      //   43: aload_3
      //   44: invokeinterface next : ()Ljava/lang/Object;
      //   49: checkcast java/security/cert/X509Certificate
      //   52: astore_2
      //   53: aload_2
      //   54: invokevirtual getPublicKey : ()Ljava/security/PublicKey;
      //   57: astore #4
      //   59: aload_1
      //   60: aload #4
      //   62: invokevirtual verify : (Ljava/security/PublicKey;)V
      //   65: aload_2
      //   66: astore_1
      //   67: goto -> 25
      //   70: astore_2
      //   71: goto -> 34
      //   74: aconst_null
      //   75: astore_1
      //   76: goto -> 25
      // Exception table:
      //   from	to	target	type
      //   59	65	70	java/lang/Exception
    }
    
    public int hashCode() {
      return this.subjectToCaCerts.hashCode();
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/tls/TrustRootIndex.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */