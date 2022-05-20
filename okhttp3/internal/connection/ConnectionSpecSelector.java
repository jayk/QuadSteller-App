package okhttp3.internal.connection;

import java.io.IOException;
import java.net.UnknownServiceException;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLSocket;
import okhttp3.ConnectionSpec;
import okhttp3.internal.Internal;

public final class ConnectionSpecSelector {
  private final List<ConnectionSpec> connectionSpecs;
  
  private boolean isFallback;
  
  private boolean isFallbackPossible;
  
  private int nextModeIndex = 0;
  
  public ConnectionSpecSelector(List<ConnectionSpec> paramList) {
    this.connectionSpecs = paramList;
  }
  
  private boolean isFallbackPossible(SSLSocket paramSSLSocket) {
    // Byte code:
    //   0: aload_0
    //   1: getfield nextModeIndex : I
    //   4: istore_2
    //   5: iload_2
    //   6: aload_0
    //   7: getfield connectionSpecs : Ljava/util/List;
    //   10: invokeinterface size : ()I
    //   15: if_icmpge -> 48
    //   18: aload_0
    //   19: getfield connectionSpecs : Ljava/util/List;
    //   22: iload_2
    //   23: invokeinterface get : (I)Ljava/lang/Object;
    //   28: checkcast okhttp3/ConnectionSpec
    //   31: aload_1
    //   32: invokevirtual isCompatible : (Ljavax/net/ssl/SSLSocket;)Z
    //   35: ifeq -> 42
    //   38: iconst_1
    //   39: istore_3
    //   40: iload_3
    //   41: ireturn
    //   42: iinc #2, 1
    //   45: goto -> 5
    //   48: iconst_0
    //   49: istore_3
    //   50: goto -> 40
  }
  
  public ConnectionSpec configureSecureSocket(SSLSocket paramSSLSocket) throws IOException {
    ConnectionSpec connectionSpec = null;
    int i = this.nextModeIndex;
    int j = this.connectionSpecs.size();
    while (true) {
      ConnectionSpec connectionSpec1 = connectionSpec;
      if (i < j) {
        connectionSpec1 = this.connectionSpecs.get(i);
        if (connectionSpec1.isCompatible(paramSSLSocket)) {
          this.nextModeIndex = i + 1;
        } else {
          i++;
          continue;
        } 
      } 
      if (connectionSpec1 == null)
        throw new UnknownServiceException("Unable to find acceptable protocols. isFallback=" + this.isFallback + ", modes=" + this.connectionSpecs + ", supported protocols=" + Arrays.toString(paramSSLSocket.getEnabledProtocols())); 
      this.isFallbackPossible = isFallbackPossible(paramSSLSocket);
      Internal.instance.apply(connectionSpec1, paramSSLSocket, this.isFallback);
      return connectionSpec1;
    } 
  }
  
  public boolean connectionFailed(IOException paramIOException) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: aload_0
    //   3: iconst_1
    //   4: putfield isFallback : Z
    //   7: aload_0
    //   8: getfield isFallbackPossible : Z
    //   11: ifne -> 18
    //   14: iload_2
    //   15: istore_3
    //   16: iload_3
    //   17: ireturn
    //   18: iload_2
    //   19: istore_3
    //   20: aload_1
    //   21: instanceof java/net/ProtocolException
    //   24: ifne -> 16
    //   27: iload_2
    //   28: istore_3
    //   29: aload_1
    //   30: instanceof java/io/InterruptedIOException
    //   33: ifne -> 16
    //   36: aload_1
    //   37: instanceof javax/net/ssl/SSLHandshakeException
    //   40: ifeq -> 55
    //   43: iload_2
    //   44: istore_3
    //   45: aload_1
    //   46: invokevirtual getCause : ()Ljava/lang/Throwable;
    //   49: instanceof java/security/cert/CertificateException
    //   52: ifne -> 16
    //   55: iload_2
    //   56: istore_3
    //   57: aload_1
    //   58: instanceof javax/net/ssl/SSLPeerUnverifiedException
    //   61: ifne -> 16
    //   64: aload_1
    //   65: instanceof javax/net/ssl/SSLHandshakeException
    //   68: ifne -> 80
    //   71: iload_2
    //   72: istore_3
    //   73: aload_1
    //   74: instanceof javax/net/ssl/SSLProtocolException
    //   77: ifeq -> 16
    //   80: iconst_1
    //   81: istore_3
    //   82: goto -> 16
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/internal/connection/ConnectionSpecSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */