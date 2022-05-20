package com.loopj.android.http;

import android.util.Log;
import java.io.UnsupportedEncodingException;
import org.apache.http.Header;

public abstract class TextHttpResponseHandler extends AsyncHttpResponseHandler {
  private static final String LOG_TAG = "TextHttpResponseHandler";
  
  public TextHttpResponseHandler() {
    this("UTF-8");
  }
  
  public TextHttpResponseHandler(String paramString) {
    setCharset(paramString);
  }
  
  public static String getResponseString(byte[] paramArrayOfbyte, String paramString) {
    if (paramArrayOfbyte == null) {
      paramString = null;
    } else {
      paramString = new String(paramArrayOfbyte, paramString);
    } 
    String str = paramString;
    if (paramString != null) {
      str = paramString;
      try {
        if (paramString.startsWith("﻿"))
          str = paramString.substring(1); 
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
        Log.e("TextHttpResponseHandler", "Encoding response into string failed", unsupportedEncodingException);
        unsupportedEncodingException = null;
      } 
    } 
    return (String)unsupportedEncodingException;
  }
  
  public abstract void onFailure(int paramInt, Header[] paramArrayOfHeader, String paramString, Throwable paramThrowable);
  
  public void onFailure(int paramInt, Header[] paramArrayOfHeader, byte[] paramArrayOfbyte, Throwable paramThrowable) {
    onFailure(paramInt, paramArrayOfHeader, getResponseString(paramArrayOfbyte, getCharset()), paramThrowable);
  }
  
  public abstract void onSuccess(int paramInt, Header[] paramArrayOfHeader, String paramString);
  
  public void onSuccess(int paramInt, Header[] paramArrayOfHeader, byte[] paramArrayOfbyte) {
    onSuccess(paramInt, paramArrayOfHeader, getResponseString(paramArrayOfbyte, getCharset()));
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/loopj/android/http/TextHttpResponseHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */