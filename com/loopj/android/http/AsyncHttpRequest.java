package com.loopj.android.http;

import android.util.Log;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

public class AsyncHttpRequest implements Runnable {
  private boolean cancelIsNotified;
  
  private final AbstractHttpClient client;
  
  private final HttpContext context;
  
  private int executionCount;
  
  private boolean isCancelled;
  
  private boolean isFinished;
  
  private boolean isRequestPreProcessed;
  
  private final HttpUriRequest request;
  
  private final ResponseHandlerInterface responseHandler;
  
  public AsyncHttpRequest(AbstractHttpClient paramAbstractHttpClient, HttpContext paramHttpContext, HttpUriRequest paramHttpUriRequest, ResponseHandlerInterface paramResponseHandlerInterface) {
    this.client = paramAbstractHttpClient;
    this.context = paramHttpContext;
    this.request = paramHttpUriRequest;
    this.responseHandler = paramResponseHandlerInterface;
  }
  
  private void makeRequest() throws IOException {
    if (!isCancelled()) {
      if (this.request.getURI().getScheme() == null)
        throw new MalformedURLException("No valid URI scheme was provided"); 
      HttpResponse httpResponse = this.client.execute(this.request, this.context);
      if (!isCancelled() && this.responseHandler != null) {
        this.responseHandler.onPreProcessResponse(this.responseHandler, httpResponse);
        if (!isCancelled()) {
          this.responseHandler.sendResponseMessage(httpResponse);
          if (!isCancelled())
            this.responseHandler.onPostProcessResponse(this.responseHandler, httpResponse); 
        } 
      } 
    } 
  }
  
  private void makeRequestWithRetries() throws IOException {
    boolean bool = true;
    HttpRequestRetryHandler httpRequestRetryHandler = this.client.getHttpRequestRetryHandler();
    Object object = null;
    while (true) {
      if (bool) {
        try {
          makeRequest();
          return;
        } catch (UnknownHostException unknownHostException) {
          StringBuilder stringBuilder = new StringBuilder();
          this();
          exception = new IOException(stringBuilder.append("UnknownHostException exception: ").append(unknownHostException.getMessage()).toString());
          try {
            if (this.executionCount > 0) {
              int i = this.executionCount + 1;
              this.executionCount = i;
              if (httpRequestRetryHandler.retryRequest((IOException)exception, i, this.context)) {
                bool = true;
                continue;
              } 
            } 
            bool = false;
            continue;
          } catch (Exception exception1) {}
        } catch (NullPointerException nullPointerException) {
          StringBuilder stringBuilder = new StringBuilder();
          this();
          exception = new IOException(stringBuilder.append("NPE in HttpClient: ").append(nullPointerException.getMessage()).toString());
          int i = this.executionCount + 1;
          this.executionCount = i;
          bool = httpRequestRetryHandler.retryRequest((IOException)exception, i, this.context);
          continue;
        } catch (IOException null) {
          bool = isCancelled();
          if (!bool) {
            int i = this.executionCount + 1;
            this.executionCount = i;
            bool = httpRequestRetryHandler.retryRequest((IOException)exception, i, this.context);
          } else {
            return;
          } 
          continue;
        } catch (Exception exception) {}
      } else {
        throw exception;
      } 
      Log.e("AsyncHttpRequest", "Unhandled exception origin cause", exception);
      exception = new IOException("Unhandled exception: " + exception.getMessage());
      throw exception;
      if (bool && this.responseHandler != null)
        this.responseHandler.sendRetryMessage(this.executionCount); 
    } 
  }
  
  private void sendCancelNotification() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isFinished : Z
    //   6: ifne -> 44
    //   9: aload_0
    //   10: getfield isCancelled : Z
    //   13: ifeq -> 44
    //   16: aload_0
    //   17: getfield cancelIsNotified : Z
    //   20: ifne -> 44
    //   23: aload_0
    //   24: iconst_1
    //   25: putfield cancelIsNotified : Z
    //   28: aload_0
    //   29: getfield responseHandler : Lcom/loopj/android/http/ResponseHandlerInterface;
    //   32: ifnull -> 44
    //   35: aload_0
    //   36: getfield responseHandler : Lcom/loopj/android/http/ResponseHandlerInterface;
    //   39: invokeinterface sendCancelMessage : ()V
    //   44: aload_0
    //   45: monitorexit
    //   46: return
    //   47: astore_1
    //   48: aload_0
    //   49: monitorexit
    //   50: aload_1
    //   51: athrow
    // Exception table:
    //   from	to	target	type
    //   2	44	47	finally
  }
  
  public boolean cancel(boolean paramBoolean) {
    this.isCancelled = true;
    this.request.abort();
    return isCancelled();
  }
  
  public boolean isCancelled() {
    if (this.isCancelled)
      sendCancelNotification(); 
    return this.isCancelled;
  }
  
  public boolean isDone() {
    return (isCancelled() || this.isFinished);
  }
  
  public void onPostProcessRequest(AsyncHttpRequest paramAsyncHttpRequest) {}
  
  public void onPreProcessRequest(AsyncHttpRequest paramAsyncHttpRequest) {}
  
  public void run() {
    if (isCancelled())
      return; 
    if (!this.isRequestPreProcessed) {
      this.isRequestPreProcessed = true;
      onPreProcessRequest(this);
    } 
    if (!isCancelled()) {
      if (this.responseHandler != null)
        this.responseHandler.sendStartMessage(); 
      if (!isCancelled()) {
        try {
          makeRequestWithRetries();
        } catch (IOException iOException) {}
      } else {
        return;
      } 
    } else {
      return;
    } 
    if (!isCancelled()) {
      if (this.responseHandler != null)
        this.responseHandler.sendFinishMessage(); 
      if (!isCancelled()) {
        onPostProcessRequest(this);
        this.isFinished = true;
      } 
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/loopj/android/http/AsyncHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */