package com.loopj.android.http;

import android.util.Log;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonHttpResponseHandler extends TextHttpResponseHandler {
  private static final String LOG_TAG = "JsonHttpResponseHandler";
  
  public JsonHttpResponseHandler() {
    super("UTF-8");
  }
  
  public JsonHttpResponseHandler(String paramString) {
    super(paramString);
  }
  
  public void onFailure(int paramInt, Header[] paramArrayOfHeader, String paramString, Throwable paramThrowable) {
    Log.w("JsonHttpResponseHandler", "onFailure(int, Header[], String, Throwable) was not overriden, but callback was received", paramThrowable);
  }
  
  public void onFailure(int paramInt, Header[] paramArrayOfHeader, Throwable paramThrowable, JSONArray paramJSONArray) {
    Log.w("JsonHttpResponseHandler", "onFailure(int, Header[], Throwable, JSONArray) was not overriden, but callback was received", paramThrowable);
  }
  
  public void onFailure(int paramInt, Header[] paramArrayOfHeader, Throwable paramThrowable, JSONObject paramJSONObject) {
    Log.w("JsonHttpResponseHandler", "onFailure(int, Header[], Throwable, JSONObject) was not overriden, but callback was received", paramThrowable);
  }
  
  public final void onFailure(final int statusCode, final Header[] headers, final byte[] responseBytes, final Throwable throwable) {
    Runnable runnable;
    if (responseBytes != null) {
      runnable = new Runnable() {
          public void run() {
            try {
              Object object = JsonHttpResponseHandler.this.parseResponse(responseBytes);
              JsonHttpResponseHandler jsonHttpResponseHandler = JsonHttpResponseHandler.this;
              Runnable runnable = new Runnable() {
                  public void run() {
                    if (jsonResponse instanceof JSONObject) {
                      JsonHttpResponseHandler.this.onFailure(statusCode, headers, throwable, (JSONObject)jsonResponse);
                      return;
                    } 
                    if (jsonResponse instanceof JSONArray) {
                      JsonHttpResponseHandler.this.onFailure(statusCode, headers, throwable, (JSONArray)jsonResponse);
                      return;
                    } 
                    if (jsonResponse instanceof String) {
                      JsonHttpResponseHandler.this.onFailure(statusCode, headers, (String)jsonResponse, throwable);
                      return;
                    } 
                    JsonHttpResponseHandler.this.onFailure(statusCode, headers, (Throwable)new JSONException("Unexpected response type " + jsonResponse.getClass().getName()), (JSONObject)null);
                  }
                };
              super(this, object);
              jsonHttpResponseHandler.postRunnable(runnable);
            } catch (JSONException jSONException) {
              JsonHttpResponseHandler.this.postRunnable(new Runnable() {
                    public void run() {
                      JsonHttpResponseHandler.this.onFailure(statusCode, headers, (Throwable)ex, (JSONObject)null);
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
    Log.v("JsonHttpResponseHandler", "response body is null, calling onFailure(Throwable, JSONObject)");
    onFailure(statusCode, (Header[])runnable, throwable, (JSONObject)null);
  }
  
  public void onSuccess(int paramInt, Header[] paramArrayOfHeader, String paramString) {
    Log.w("JsonHttpResponseHandler", "onSuccess(int, Header[], String) was not overriden, but callback was received");
  }
  
  public void onSuccess(int paramInt, Header[] paramArrayOfHeader, JSONArray paramJSONArray) {
    Log.w("JsonHttpResponseHandler", "onSuccess(int, Header[], JSONArray) was not overriden, but callback was received");
  }
  
  public void onSuccess(int paramInt, Header[] paramArrayOfHeader, JSONObject paramJSONObject) {
    Log.w("JsonHttpResponseHandler", "onSuccess(int, Header[], JSONObject) was not overriden, but callback was received");
  }
  
  public final void onSuccess(final int statusCode, final Header[] headers, final byte[] responseBytes) {
    Runnable runnable;
    if (statusCode != 204) {
      runnable = new Runnable() {
          public void run() {
            try {
              Object object = JsonHttpResponseHandler.this.parseResponse(responseBytes);
              JsonHttpResponseHandler jsonHttpResponseHandler = JsonHttpResponseHandler.this;
              Runnable runnable = new Runnable() {
                  public void run() {
                    if (jsonResponse instanceof JSONObject) {
                      JsonHttpResponseHandler.this.onSuccess(statusCode, headers, (JSONObject)jsonResponse);
                      return;
                    } 
                    if (jsonResponse instanceof JSONArray) {
                      JsonHttpResponseHandler.this.onSuccess(statusCode, headers, (JSONArray)jsonResponse);
                      return;
                    } 
                    if (jsonResponse instanceof String) {
                      JsonHttpResponseHandler.this.onFailure(statusCode, headers, (String)jsonResponse, (Throwable)new JSONException("Response cannot be parsed as JSON data"));
                      return;
                    } 
                    JsonHttpResponseHandler.this.onFailure(statusCode, headers, (Throwable)new JSONException("Unexpected response type " + jsonResponse.getClass().getName()), (JSONObject)null);
                  }
                };
              super(this, object);
              jsonHttpResponseHandler.postRunnable(runnable);
            } catch (JSONException jSONException) {
              JsonHttpResponseHandler.this.postRunnable(new Runnable() {
                    public void run() {
                      JsonHttpResponseHandler.this.onFailure(statusCode, headers, (Throwable)ex, (JSONObject)null);
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
    onSuccess(statusCode, (Header[])runnable, new JSONObject());
  }
  
  protected Object parseResponse(byte[] paramArrayOfbyte) throws JSONException {
    // Byte code:
    //   0: aload_1
    //   1: ifnonnull -> 8
    //   4: aconst_null
    //   5: astore_1
    //   6: aload_1
    //   7: areturn
    //   8: aconst_null
    //   9: astore_2
    //   10: aload_1
    //   11: aload_0
    //   12: invokevirtual getCharset : ()Ljava/lang/String;
    //   15: invokestatic getResponseString : ([BLjava/lang/String;)Ljava/lang/String;
    //   18: astore_1
    //   19: aload_1
    //   20: astore_3
    //   21: aload_2
    //   22: astore #4
    //   24: aload_1
    //   25: ifnull -> 92
    //   28: aload_1
    //   29: invokevirtual trim : ()Ljava/lang/String;
    //   32: astore #4
    //   34: aload #4
    //   36: astore_1
    //   37: aload #4
    //   39: ldc '﻿'
    //   41: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   44: ifeq -> 54
    //   47: aload #4
    //   49: iconst_1
    //   50: invokevirtual substring : (I)Ljava/lang/String;
    //   53: astore_1
    //   54: aload_1
    //   55: ldc '{'
    //   57: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   60: ifne -> 77
    //   63: aload_1
    //   64: astore_3
    //   65: aload_2
    //   66: astore #4
    //   68: aload_1
    //   69: ldc '['
    //   71: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   74: ifeq -> 92
    //   77: new org/json/JSONTokener
    //   80: dup
    //   81: aload_1
    //   82: invokespecial <init> : (Ljava/lang/String;)V
    //   85: invokevirtual nextValue : ()Ljava/lang/Object;
    //   88: astore #4
    //   90: aload_1
    //   91: astore_3
    //   92: aload #4
    //   94: astore_1
    //   95: aload #4
    //   97: ifnonnull -> 6
    //   100: aload_3
    //   101: astore_1
    //   102: goto -> 6
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/loopj/android/http/JsonHttpResponseHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */