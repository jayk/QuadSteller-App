package okhttp3;

import java.io.IOException;
import java.util.ArrayList;
import okhttp3.internal.NamedRunnable;
import okhttp3.internal.cache.CacheInterceptor;
import okhttp3.internal.connection.ConnectInterceptor;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.http.BridgeInterceptor;
import okhttp3.internal.http.CallServerInterceptor;
import okhttp3.internal.http.RealInterceptorChain;
import okhttp3.internal.http.RetryAndFollowUpInterceptor;
import okhttp3.internal.platform.Platform;

final class RealCall implements Call {
  final OkHttpClient client;
  
  final EventListener eventListener;
  
  private boolean executed;
  
  final boolean forWebSocket;
  
  final Request originalRequest;
  
  final RetryAndFollowUpInterceptor retryAndFollowUpInterceptor;
  
  RealCall(OkHttpClient paramOkHttpClient, Request paramRequest, boolean paramBoolean) {
    EventListener.Factory factory = paramOkHttpClient.eventListenerFactory();
    this.client = paramOkHttpClient;
    this.originalRequest = paramRequest;
    this.forWebSocket = paramBoolean;
    this.retryAndFollowUpInterceptor = new RetryAndFollowUpInterceptor(paramOkHttpClient, paramBoolean);
    this.eventListener = factory.create(this);
  }
  
  private void captureCallStackTrace() {
    Object object = Platform.get().getStackTraceForCloseable("response.body().close()");
    this.retryAndFollowUpInterceptor.setCallStackTrace(object);
  }
  
  public void cancel() {
    this.retryAndFollowUpInterceptor.cancel();
  }
  
  public RealCall clone() {
    return new RealCall(this.client, this.originalRequest, this.forWebSocket);
  }
  
  public void enqueue(Callback paramCallback) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield executed : Z
    //   6: ifeq -> 26
    //   9: new java/lang/IllegalStateException
    //   12: astore_1
    //   13: aload_1
    //   14: ldc 'Already Executed'
    //   16: invokespecial <init> : (Ljava/lang/String;)V
    //   19: aload_1
    //   20: athrow
    //   21: astore_1
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_1
    //   25: athrow
    //   26: aload_0
    //   27: iconst_1
    //   28: putfield executed : Z
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_0
    //   34: invokespecial captureCallStackTrace : ()V
    //   37: aload_0
    //   38: getfield client : Lokhttp3/OkHttpClient;
    //   41: invokevirtual dispatcher : ()Lokhttp3/Dispatcher;
    //   44: new okhttp3/RealCall$AsyncCall
    //   47: dup
    //   48: aload_0
    //   49: aload_1
    //   50: invokespecial <init> : (Lokhttp3/RealCall;Lokhttp3/Callback;)V
    //   53: invokevirtual enqueue : (Lokhttp3/RealCall$AsyncCall;)V
    //   56: return
    // Exception table:
    //   from	to	target	type
    //   2	21	21	finally
    //   22	24	21	finally
    //   26	33	21	finally
  }
  
  public Response execute() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield executed : Z
    //   6: ifeq -> 26
    //   9: new java/lang/IllegalStateException
    //   12: astore_1
    //   13: aload_1
    //   14: ldc 'Already Executed'
    //   16: invokespecial <init> : (Ljava/lang/String;)V
    //   19: aload_1
    //   20: athrow
    //   21: astore_1
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_1
    //   25: athrow
    //   26: aload_0
    //   27: iconst_1
    //   28: putfield executed : Z
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_0
    //   34: invokespecial captureCallStackTrace : ()V
    //   37: aload_0
    //   38: getfield client : Lokhttp3/OkHttpClient;
    //   41: invokevirtual dispatcher : ()Lokhttp3/Dispatcher;
    //   44: aload_0
    //   45: invokevirtual executed : (Lokhttp3/RealCall;)V
    //   48: aload_0
    //   49: invokevirtual getResponseWithInterceptorChain : ()Lokhttp3/Response;
    //   52: astore_1
    //   53: aload_1
    //   54: ifnonnull -> 83
    //   57: new java/io/IOException
    //   60: astore_1
    //   61: aload_1
    //   62: ldc 'Canceled'
    //   64: invokespecial <init> : (Ljava/lang/String;)V
    //   67: aload_1
    //   68: athrow
    //   69: astore_1
    //   70: aload_0
    //   71: getfield client : Lokhttp3/OkHttpClient;
    //   74: invokevirtual dispatcher : ()Lokhttp3/Dispatcher;
    //   77: aload_0
    //   78: invokevirtual finished : (Lokhttp3/RealCall;)V
    //   81: aload_1
    //   82: athrow
    //   83: aload_0
    //   84: getfield client : Lokhttp3/OkHttpClient;
    //   87: invokevirtual dispatcher : ()Lokhttp3/Dispatcher;
    //   90: aload_0
    //   91: invokevirtual finished : (Lokhttp3/RealCall;)V
    //   94: aload_1
    //   95: areturn
    // Exception table:
    //   from	to	target	type
    //   2	21	21	finally
    //   22	24	21	finally
    //   26	33	21	finally
    //   37	53	69	finally
    //   57	69	69	finally
  }
  
  Response getResponseWithInterceptorChain() throws IOException {
    ArrayList<Interceptor> arrayList = new ArrayList();
    arrayList.addAll(this.client.interceptors());
    arrayList.add(this.retryAndFollowUpInterceptor);
    arrayList.add(new BridgeInterceptor(this.client.cookieJar()));
    arrayList.add(new CacheInterceptor(this.client.internalCache()));
    arrayList.add(new ConnectInterceptor(this.client));
    if (!this.forWebSocket)
      arrayList.addAll(this.client.networkInterceptors()); 
    arrayList.add(new CallServerInterceptor(this.forWebSocket));
    return (new RealInterceptorChain(arrayList, null, null, null, 0, this.originalRequest)).proceed(this.originalRequest);
  }
  
  public boolean isCanceled() {
    return this.retryAndFollowUpInterceptor.isCanceled();
  }
  
  public boolean isExecuted() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield executed : Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  String redactedUrl() {
    return this.originalRequest.url().redact();
  }
  
  public Request request() {
    return this.originalRequest;
  }
  
  StreamAllocation streamAllocation() {
    return this.retryAndFollowUpInterceptor.streamAllocation();
  }
  
  String toLoggableString() {
    StringBuilder stringBuilder = new StringBuilder();
    if (isCanceled()) {
      str = "canceled ";
    } else {
      str = "";
    } 
    stringBuilder = stringBuilder.append(str);
    if (this.forWebSocket) {
      str = "web socket";
      return stringBuilder.append(str).append(" to ").append(redactedUrl()).toString();
    } 
    String str = "call";
    return stringBuilder.append(str).append(" to ").append(redactedUrl()).toString();
  }
  
  final class AsyncCall extends NamedRunnable {
    private final Callback responseCallback;
    
    AsyncCall(Callback param1Callback) {
      super("OkHttp %s", new Object[] { this$0.redactedUrl() });
      this.responseCallback = param1Callback;
    }
    
    protected void execute() {
      boolean bool1 = false;
      boolean bool2 = bool1;
      try {
        Callback callback;
        Response response = RealCall.this.getResponseWithInterceptorChain();
        bool2 = bool1;
        if (RealCall.this.retryAndFollowUpInterceptor.isCanceled()) {
          bool1 = true;
          bool2 = bool1;
          callback = this.responseCallback;
          bool2 = bool1;
          RealCall realCall = RealCall.this;
          bool2 = bool1;
          IOException iOException = new IOException();
          bool2 = bool1;
          this("Canceled");
          bool2 = bool1;
          callback.onFailure(realCall, iOException);
        } else {
          bool2 = true;
          this.responseCallback.onResponse(RealCall.this, (Response)callback);
        } 
        return;
      } catch (IOException iOException) {
        if (bool2) {
          Platform platform = Platform.get();
          StringBuilder stringBuilder = new StringBuilder();
          this();
          platform.log(4, stringBuilder.append("Callback failure for ").append(RealCall.this.toLoggableString()).toString(), iOException);
        } else {
          this.responseCallback.onFailure(RealCall.this, iOException);
        } 
        return;
      } finally {
        RealCall.this.client.dispatcher().finished(this);
      } 
    }
    
    RealCall get() {
      return RealCall.this;
    }
    
    String host() {
      return RealCall.this.originalRequest.url().host();
    }
    
    Request request() {
      return RealCall.this.originalRequest;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/okhttp3/RealCall.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */