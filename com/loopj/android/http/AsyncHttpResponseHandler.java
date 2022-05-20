package com.loopj.android.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.util.ByteArrayBuffer;

public abstract class AsyncHttpResponseHandler implements ResponseHandlerInterface {
  protected static final int BUFFER_SIZE = 4096;
  
  protected static final int CANCEL_MESSAGE = 6;
  
  public static final String DEFAULT_CHARSET = "UTF-8";
  
  protected static final int FAILURE_MESSAGE = 1;
  
  protected static final int FINISH_MESSAGE = 3;
  
  private static final String LOG_TAG = "AsyncHttpResponseHandler";
  
  protected static final int PROGRESS_MESSAGE = 4;
  
  protected static final int RETRY_MESSAGE = 5;
  
  protected static final int START_MESSAGE = 2;
  
  protected static final int SUCCESS_MESSAGE = 0;
  
  public static final String UTF8_BOM = "﻿";
  
  private Handler handler;
  
  private Looper looper = null;
  
  private Header[] requestHeaders = null;
  
  private URI requestURI = null;
  
  private String responseCharset = "UTF-8";
  
  private boolean useSynchronousMode;
  
  public AsyncHttpResponseHandler() {
    this(null);
  }
  
  public AsyncHttpResponseHandler(Looper paramLooper) {
    Looper looper = paramLooper;
    if (paramLooper == null)
      looper = Looper.myLooper(); 
    this.looper = looper;
    setUseSynchronousMode(false);
  }
  
  public String getCharset() {
    return (this.responseCharset == null) ? "UTF-8" : this.responseCharset;
  }
  
  public Header[] getRequestHeaders() {
    return this.requestHeaders;
  }
  
  public URI getRequestURI() {
    return this.requestURI;
  }
  
  byte[] getResponseData(HttpEntity paramHttpEntity) throws IOException {
    byte[] arrayOfByte;
    int i = 4096;
    ByteArrayBuffer byteArrayBuffer1 = null;
    ByteArrayBuffer byteArrayBuffer2 = byteArrayBuffer1;
    if (paramHttpEntity != null) {
      InputStream inputStream = paramHttpEntity.getContent();
      byteArrayBuffer2 = byteArrayBuffer1;
      if (inputStream != null) {
        long l = paramHttpEntity.getContentLength();
        if (l > 2147483647L)
          throw new IllegalArgumentException("HTTP entity too large to be buffered in memory"); 
        if (l > 0L)
          i = (int)l; 
        try {
          byteArrayBuffer2 = new ByteArrayBuffer();
          this(i);
          try {
            byte[] arrayOfByte1 = new byte[4096];
            i = 0;
            while (true) {
              int j = inputStream.read(arrayOfByte1);
              if (j != -1 && !Thread.currentThread().isInterrupted()) {
                long l1;
                i += j;
                byteArrayBuffer2.append(arrayOfByte1, 0, j);
                if (l <= 0L) {
                  l1 = 1L;
                } else {
                  l1 = l;
                } 
                sendProgressMessage(i, (int)l1);
                continue;
              } 
              break;
            } 
          } finally {
            AsyncHttpClient.silentCloseInputStream(inputStream);
            AsyncHttpClient.endEntityViaReflection(paramHttpEntity);
          } 
        } catch (OutOfMemoryError outOfMemoryError) {
          System.gc();
          throw new IOException("File too large to fit into available memory");
        } 
        AsyncHttpClient.silentCloseInputStream(inputStream);
        AsyncHttpClient.endEntityViaReflection((HttpEntity)outOfMemoryError);
        arrayOfByte = byteArrayBuffer2.toByteArray();
      } 
    } 
    return arrayOfByte;
  }
  
  public boolean getUseSynchronousMode() {
    return this.useSynchronousMode;
  }
  
  protected void handleMessage(Message paramMessage) {
    Object[] arrayOfObject2;
    Object[] arrayOfObject1;
    switch (paramMessage.what) {
      default:
        return;
      case 0:
        arrayOfObject2 = (Object[])paramMessage.obj;
        if (arrayOfObject2 != null && arrayOfObject2.length >= 3)
          onSuccess(((Integer)arrayOfObject2[0]).intValue(), (Header[])arrayOfObject2[1], (byte[])arrayOfObject2[2]); 
        Log.e("AsyncHttpResponseHandler", "SUCCESS_MESSAGE didn't got enough params");
      case 1:
        arrayOfObject2 = (Object[])((Message)arrayOfObject2).obj;
        if (arrayOfObject2 != null && arrayOfObject2.length >= 4)
          onFailure(((Integer)arrayOfObject2[0]).intValue(), (Header[])arrayOfObject2[1], (byte[])arrayOfObject2[2], (Throwable)arrayOfObject2[3]); 
        Log.e("AsyncHttpResponseHandler", "FAILURE_MESSAGE didn't got enough params");
      case 2:
        onStart();
      case 3:
        onFinish();
      case 4:
        arrayOfObject2 = (Object[])((Message)arrayOfObject2).obj;
        if (arrayOfObject2 != null && arrayOfObject2.length >= 2)
          try {
            onProgress(((Integer)arrayOfObject2[0]).intValue(), ((Integer)arrayOfObject2[1]).intValue());
          } catch (Throwable throwable) {
            Log.e("AsyncHttpResponseHandler", "custom onProgress contains an error", throwable);
          }  
        Log.e("AsyncHttpResponseHandler", "PROGRESS_MESSAGE didn't got enough params");
      case 5:
        arrayOfObject1 = (Object[])((Message)throwable).obj;
        if (arrayOfObject1 != null && arrayOfObject1.length == 1)
          onRetry(((Integer)arrayOfObject1[0]).intValue()); 
        Log.e("AsyncHttpResponseHandler", "RETRY_MESSAGE didn't get enough params");
      case 6:
        break;
    } 
    onCancel();
  }
  
  protected Message obtainMessage(int paramInt, Object paramObject) {
    return Message.obtain(this.handler, paramInt, paramObject);
  }
  
  public void onCancel() {
    Log.d("AsyncHttpResponseHandler", "Request got cancelled");
  }
  
  public abstract void onFailure(int paramInt, Header[] paramArrayOfHeader, byte[] paramArrayOfbyte, Throwable paramThrowable);
  
  public void onFinish() {}
  
  public void onPostProcessResponse(ResponseHandlerInterface paramResponseHandlerInterface, HttpResponse paramHttpResponse) {}
  
  public void onPreProcessResponse(ResponseHandlerInterface paramResponseHandlerInterface, HttpResponse paramHttpResponse) {}
  
  public void onProgress(int paramInt1, int paramInt2) {
    double d;
    if (paramInt2 > 0) {
      d = paramInt1 * 1.0D / paramInt2 * 100.0D;
    } else {
      d = -1.0D;
    } 
    Log.v("AsyncHttpResponseHandler", String.format("Progress %d from %d (%2.0f%%)", new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Double.valueOf(d) }));
  }
  
  public void onRetry(int paramInt) {
    Log.d("AsyncHttpResponseHandler", String.format("Request retry no. %d", new Object[] { Integer.valueOf(paramInt) }));
  }
  
  public void onStart() {}
  
  public abstract void onSuccess(int paramInt, Header[] paramArrayOfHeader, byte[] paramArrayOfbyte);
  
  protected void postRunnable(Runnable paramRunnable) {
    boolean bool;
    if (paramRunnable != null) {
      if (getUseSynchronousMode() || this.handler == null) {
        paramRunnable.run();
        return;
      } 
    } else {
      return;
    } 
    if (this.handler != null) {
      bool = true;
    } else {
      bool = false;
    } 
    AssertUtils.asserts(bool, "handler should not be null!");
    this.handler.post(paramRunnable);
  }
  
  public final void sendCancelMessage() {
    sendMessage(obtainMessage(6, null));
  }
  
  public final void sendFailureMessage(int paramInt, Header[] paramArrayOfHeader, byte[] paramArrayOfbyte, Throwable paramThrowable) {
    sendMessage(obtainMessage(1, new Object[] { Integer.valueOf(paramInt), paramArrayOfHeader, paramArrayOfbyte, paramThrowable }));
  }
  
  public final void sendFinishMessage() {
    sendMessage(obtainMessage(3, null));
  }
  
  protected void sendMessage(Message paramMessage) {
    if (getUseSynchronousMode() || this.handler == null) {
      handleMessage(paramMessage);
      return;
    } 
    if (!Thread.currentThread().isInterrupted()) {
      boolean bool;
      if (this.handler != null) {
        bool = true;
      } else {
        bool = false;
      } 
      AssertUtils.asserts(bool, "handler should not be null!");
      this.handler.sendMessage(paramMessage);
    } 
  }
  
  public final void sendProgressMessage(int paramInt1, int paramInt2) {
    sendMessage(obtainMessage(4, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) }));
  }
  
  public void sendResponseMessage(HttpResponse paramHttpResponse) throws IOException {
    StatusLine statusLine;
    byte[] arrayOfByte;
    if (!Thread.currentThread().isInterrupted()) {
      statusLine = paramHttpResponse.getStatusLine();
      arrayOfByte = getResponseData(paramHttpResponse.getEntity());
      if (!Thread.currentThread().isInterrupted()) {
        if (statusLine.getStatusCode() >= 300) {
          sendFailureMessage(statusLine.getStatusCode(), paramHttpResponse.getAllHeaders(), arrayOfByte, (Throwable)new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase()));
          return;
        } 
      } else {
        return;
      } 
    } else {
      return;
    } 
    sendSuccessMessage(statusLine.getStatusCode(), paramHttpResponse.getAllHeaders(), arrayOfByte);
  }
  
  public final void sendRetryMessage(int paramInt) {
    sendMessage(obtainMessage(5, new Object[] { Integer.valueOf(paramInt) }));
  }
  
  public final void sendStartMessage() {
    sendMessage(obtainMessage(2, null));
  }
  
  public final void sendSuccessMessage(int paramInt, Header[] paramArrayOfHeader, byte[] paramArrayOfbyte) {
    sendMessage(obtainMessage(0, new Object[] { Integer.valueOf(paramInt), paramArrayOfHeader, paramArrayOfbyte }));
  }
  
  public void setCharset(String paramString) {
    this.responseCharset = paramString;
  }
  
  public void setRequestHeaders(Header[] paramArrayOfHeader) {
    this.requestHeaders = paramArrayOfHeader;
  }
  
  public void setRequestURI(URI paramURI) {
    this.requestURI = paramURI;
  }
  
  public void setUseSynchronousMode(boolean paramBoolean) {
    boolean bool = paramBoolean;
    if (!paramBoolean) {
      bool = paramBoolean;
      if (this.looper == null) {
        bool = true;
        Log.w("AsyncHttpResponseHandler", "Current thread has not called Looper.prepare(). Forcing synchronous mode.");
      } 
    } 
    if (!bool && this.handler == null) {
      this.handler = new ResponderHandler(this, this.looper);
    } else if (bool && this.handler != null) {
      this.handler = null;
    } 
    this.useSynchronousMode = bool;
  }
  
  private static class ResponderHandler extends Handler {
    private final AsyncHttpResponseHandler mResponder;
    
    ResponderHandler(AsyncHttpResponseHandler param1AsyncHttpResponseHandler, Looper param1Looper) {
      super(param1Looper);
      this.mResponder = param1AsyncHttpResponseHandler;
    }
    
    public void handleMessage(Message param1Message) {
      this.mResponder.handleMessage(param1Message);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/loopj/android/http/AsyncHttpResponseHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */