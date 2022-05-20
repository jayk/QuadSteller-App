package okhttp3.internal.tls;

import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import okhttp3.internal.Util;

public final class OkHostnameVerifier implements HostnameVerifier {
  private static final int ALT_DNS_NAME = 2;
  
  private static final int ALT_IPA_NAME = 7;
  
  public static final OkHostnameVerifier INSTANCE = new OkHostnameVerifier();
  
  public static List<String> allSubjectAltNames(X509Certificate paramX509Certificate) {
    List<String> list1 = getSubjectAltNames(paramX509Certificate, 7);
    List<String> list2 = getSubjectAltNames(paramX509Certificate, 2);
    ArrayList<String> arrayList = new ArrayList(list1.size() + list2.size());
    arrayList.addAll(list1);
    arrayList.addAll(list2);
    return arrayList;
  }
  
  private static List<String> getSubjectAltNames(X509Certificate paramX509Certificate, int paramInt) {
    List<?> list;
    ArrayList<List<?>> arrayList = new ArrayList();
    try {
      Collection<List<?>> collection = paramX509Certificate.getSubjectAlternativeNames();
      if (collection == null)
        return (List)Collections.emptyList(); 
      Iterator<List<?>> iterator = collection.iterator();
      while (true) {
        String str;
        collection = arrayList;
        if (iterator.hasNext()) {
          collection = (List)iterator.next();
          if (collection != null && collection.size() >= 2) {
            Integer integer = (Integer)collection.get(0);
            if (integer != null && integer.intValue() == paramInt) {
              str = (String)collection.get(1);
              if (str != null)
                arrayList.add(str); 
            } 
          } 
          continue;
        } 
        return (List<String>)str;
      } 
    } catch (CertificateParsingException certificateParsingException) {
      list = Collections.emptyList();
    } 
    return (List)list;
  }
  
  private boolean verifyHostname(String paramString, X509Certificate paramX509Certificate) {
    paramString = paramString.toLowerCase(Locale.US);
    boolean bool = false;
    List<String> list = getSubjectAltNames(paramX509Certificate, 2);
    byte b = 0;
    int i = list.size();
    while (b < i) {
      bool = true;
      if (verifyHostname(paramString, list.get(b)))
        return true; 
      b++;
    } 
    if (!bool) {
      String str = (new DistinguishedNameParser(paramX509Certificate.getSubjectX500Principal())).findMostSpecific("cn");
      if (str != null)
        return verifyHostname(paramString, str); 
    } 
    return false;
  }
  
  private boolean verifyIpAddress(String paramString, X509Certificate paramX509Certificate) {
    // Byte code:
    //   0: aload_2
    //   1: bipush #7
    //   3: invokestatic getSubjectAltNames : (Ljava/security/cert/X509Certificate;I)Ljava/util/List;
    //   6: astore_2
    //   7: iconst_0
    //   8: istore_3
    //   9: aload_2
    //   10: invokeinterface size : ()I
    //   15: istore #4
    //   17: iload_3
    //   18: iload #4
    //   20: if_icmpge -> 52
    //   23: aload_1
    //   24: aload_2
    //   25: iload_3
    //   26: invokeinterface get : (I)Ljava/lang/Object;
    //   31: checkcast java/lang/String
    //   34: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   37: ifeq -> 46
    //   40: iconst_1
    //   41: istore #5
    //   43: iload #5
    //   45: ireturn
    //   46: iinc #3, 1
    //   49: goto -> 17
    //   52: iconst_0
    //   53: istore #5
    //   55: goto -> 43
  }
  
  public boolean verify(String paramString, X509Certificate paramX509Certificate) {
    return Util.verifyAsIpAddress(paramString) ? verifyIpAddress(paramString, paramX509Certificate) : verifyHostname(paramString, paramX509Certificate);
  }
  
  public boolean verify(String paramString, SSLSession paramSSLSession) {
    boolean bool;
    try {
      bool = verify(paramString, (X509Certificate)paramSSLSession.getPeerCertificates()[0]);
    } catch (SSLException sSLException) {
      bool = false;
    } 
    return bool;
  }
  
  public boolean verifyHostname(String paramString1, String paramString2) {
    boolean bool = false;
    null = bool;
    if (paramString1 != null) {
      null = bool;
      if (paramString1.length() != 0) {
        null = bool;
        if (!paramString1.startsWith(".")) {
          if (paramString1.endsWith(".."))
            return bool; 
        } else {
          return null;
        } 
      } else {
        return null;
      } 
    } else {
      return null;
    } 
    null = bool;
    if (paramString2 != null) {
      null = bool;
      if (paramString2.length() != 0) {
        null = bool;
        if (!paramString2.startsWith(".")) {
          null = bool;
          if (!paramString2.endsWith("..")) {
            String str = paramString1;
            if (!paramString1.endsWith("."))
              str = paramString1 + '.'; 
            paramString1 = paramString2;
            if (!paramString2.endsWith("."))
              paramString1 = paramString2 + '.'; 
            paramString1 = paramString1.toLowerCase(Locale.US);
            if (!paramString1.contains("*"))
              return str.equals(paramString1); 
            null = bool;
            if (paramString1.startsWith("*.")) {
              null = bool;
              if (paramString1.indexOf('*', 1) == -1) {
                null = bool;
                if (str.length() >= paramString1.length()) {
                  null = bool;
                  if (!"*.".equals(paramString1)) {
                    paramString1 = paramString1.substring(1);
                    null = bool;
                    if (str.endsWith(paramString1)) {
                      int i = str.length() - paramString1.length();
                      if (i > 0) {
                        null = bool;
                        return (str.lastIndexOf('.', i - 1) == -1) ? true : null;
                      } 
                    } else {
                      return null;
                    } 
                  } else {
                    return null;
                  } 
                } else {
                  return null;
                } 
              } else {
                return null;
              } 
            } else {
              return null;
            } 
          } else {
            return null;
          } 
        } else {
          return null;
        } 
      } else {
        return null;
      } 
    } else {
      return null;
    } 
    return true;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/tls/OkHostnameVerifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */