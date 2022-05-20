package com.loopj.android.http;

import android.os.SystemClock;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import javax.net.ssl.SSLException;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

class RetryHandler implements HttpRequestRetryHandler {
  private static final HashSet<Class<?>> exceptionBlacklist;
  
  private static final HashSet<Class<?>> exceptionWhitelist = new HashSet<Class<?>>();
  
  private final int maxRetries;
  
  private final int retrySleepTimeMS;
  
  static {
    exceptionBlacklist = new HashSet<Class<?>>();
    exceptionWhitelist.add(NoHttpResponseException.class);
    exceptionWhitelist.add(UnknownHostException.class);
    exceptionWhitelist.add(SocketException.class);
    exceptionBlacklist.add(InterruptedIOException.class);
    exceptionBlacklist.add(SSLException.class);
  }
  
  public RetryHandler(int paramInt1, int paramInt2) {
    this.maxRetries = paramInt1;
    this.retrySleepTimeMS = paramInt2;
  }
  
  static void addClassToBlacklist(Class<?> paramClass) {
    exceptionBlacklist.add(paramClass);
  }
  
  static void addClassToWhitelist(Class<?> paramClass) {
    exceptionWhitelist.add(paramClass);
  }
  
  protected boolean isInList(HashSet<Class<?>> paramHashSet, Throwable paramThrowable) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual iterator : ()Ljava/util/Iterator;
    //   4: astore_1
    //   5: aload_1
    //   6: invokeinterface hasNext : ()Z
    //   11: ifeq -> 34
    //   14: aload_1
    //   15: invokeinterface next : ()Ljava/lang/Object;
    //   20: checkcast java/lang/Class
    //   23: aload_2
    //   24: invokevirtual isInstance : (Ljava/lang/Object;)Z
    //   27: ifeq -> 5
    //   30: iconst_1
    //   31: istore_3
    //   32: iload_3
    //   33: ireturn
    //   34: iconst_0
    //   35: istore_3
    //   36: goto -> 32
  }
  
  public boolean retryRequest(IOException paramIOException, int paramInt, HttpContext paramHttpContext) {
    boolean bool3;
    boolean bool1 = false;
    boolean bool2 = true;
    Boolean bool = (Boolean)paramHttpContext.getAttribute("http.request_sent");
    if (bool != null && bool.booleanValue()) {
      bool3 = true;
    } else {
      bool3 = false;
    } 
    if (paramInt > this.maxRetries) {
      bool2 = false;
    } else if (isInList(exceptionWhitelist, paramIOException)) {
      bool2 = true;
    } else if (isInList(exceptionBlacklist, paramIOException)) {
      bool2 = false;
    } else if (!bool3) {
      bool2 = true;
    } 
    if (bool2 && (HttpUriRequest)paramHttpContext.getAttribute("http.request") == null)
      return bool1; 
    if (bool2) {
      SystemClock.sleep(this.retrySleepTimeMS);
      return bool2;
    } 
    paramIOException.printStackTrace();
    return bool2;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/loopj/android/http/RetryHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */