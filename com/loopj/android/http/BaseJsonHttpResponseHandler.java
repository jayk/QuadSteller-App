package com.loopj.android.http;

import android.util.Log;
import org.apache.http.Header;

public abstract class BaseJsonHttpResponseHandler<JSON_TYPE> extends TextHttpResponseHandler {
  private static final String LOG_TAG = "BaseJsonHttpResponseHandler";
  
  public BaseJsonHttpResponseHandler() {
    this("UTF-8");
  }
  
  public BaseJsonHttpResponseHandler(String paramString) {
    super(paramString);
  }
  
  public final void onFailure(final int statusCode, final Header[] headers, final String responseString, final Throwable throwable) {
    Runnable runnable;
    if (responseString != null) {
      runnable = new Runnable() {
          public void run() {
            try {
              Object object = BaseJsonHttpResponseHandler.this.parseResponse(responseString, true);
              BaseJsonHttpResponseHandler baseJsonHttpResponseHandler = BaseJsonHttpResponseHandler.this;
              Runnable runnable = new Runnable() {
                  public void run() {
                    BaseJsonHttpResponseHandler.this.onFailure(statusCode, headers, throwable, responseString, jsonResponse);
                  }
                };
              super(this, object);
              baseJsonHttpResponseHandler.postRunnable(runnable);
            } catch (Throwable throwable) {
              Log.d("BaseJsonHttpResponseHandler", "parseResponse thrown an problem", throwable);
              BaseJsonHttpResponseHandler.this.postRunnable(new Runnable() {
                    public void run() {
                      BaseJsonHttpResponseHandler.this.onFailure(statusCode, headers, throwable, responseString, (Object)null);
                    }
                  });
            } 
          }
        };
      if (!getUseSynchronousMode()) {
        (new Thread(runnable)).start();
        return;
      } 
      runnable.run();
      return;
    } 
    onFailure(statusCode, (Header[])runnable, throwable, (String)null, (JSON_TYPE)null);
  }
  
  public abstract void onFailure(int paramInt, Header[] paramArrayOfHeader, Throwable paramThrowable, String paramString, JSON_TYPE paramJSON_TYPE);
  
  public final void onSuccess(final int statusCode, final Header[] headers, final String responseString) {
    Runnable runnable;
    if (statusCode != 204) {
      runnable = new Runnable() {
          public void run() {
            try {
              Object object = BaseJsonHttpResponseHandler.this.parseResponse(responseString, false);
              BaseJsonHttpResponseHandler baseJsonHttpResponseHandler = BaseJsonHttpResponseHandler.this;
              Runnable runnable = new Runnable() {
                  public void run() {
                    BaseJsonHttpResponseHandler.this.onSuccess(statusCode, headers, responseString, jsonResponse);
                  }
                };
              super(this, object);
              baseJsonHttpResponseHandler.postRunnable(runnable);
            } catch (Throwable throwable) {
              Log.d("BaseJsonHttpResponseHandler", "parseResponse thrown an problem", throwable);
              BaseJsonHttpResponseHandler.this.postRunnable(new Runnable() {
                    public void run() {
                      BaseJsonHttpResponseHandler.this.onFailure(statusCode, headers, t, responseString, (Object)null);
                    }
                  });
            } 
          }
        };
      if (!getUseSynchronousMode()) {
        (new Thread(runnable)).start();
        return;
      } 
      runnable.run();
      return;
    } 
    onSuccess(statusCode, (Header[])runnable, (String)null, (JSON_TYPE)null);
  }
  
  public abstract void onSuccess(int paramInt, Header[] paramArrayOfHeader, String paramString, JSON_TYPE paramJSON_TYPE);
  
  protected abstract JSON_TYPE parseResponse(String paramString, boolean paramBoolean) throws Throwable;
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/loopj/android/http/BaseJsonHttpResponseHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */