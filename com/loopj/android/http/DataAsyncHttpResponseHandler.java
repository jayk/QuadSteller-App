package com.loopj.android.http;

import android.os.Message;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.util.ByteArrayBuffer;

public abstract class DataAsyncHttpResponseHandler extends AsyncHttpResponseHandler {
  private static final String LOG_TAG = "DataAsyncHttpResponseHandler";
  
  protected static final int PROGRESS_DATA_MESSAGE = 6;
  
  public static byte[] copyOfRange(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NullPointerException {
    if (paramInt1 > paramInt2)
      throw new IllegalArgumentException(); 
    int i = paramArrayOfbyte.length;
    if (paramInt1 < 0 || paramInt1 > i)
      throw new ArrayIndexOutOfBoundsException(); 
    paramInt2 -= paramInt1;
    i = Math.min(paramInt2, i - paramInt1);
    byte[] arrayOfByte = new byte[paramInt2];
    System.arraycopy(paramArrayOfbyte, paramInt1, arrayOfByte, 0, i);
    return arrayOfByte;
  }
  
  byte[] getResponseData(HttpEntity paramHttpEntity) throws IOException {
    byte[] arrayOfByte1 = null;
    byte[] arrayOfByte2 = arrayOfByte1;
    if (paramHttpEntity != null) {
      InputStream inputStream = paramHttpEntity.getContent();
      arrayOfByte2 = arrayOfByte1;
      if (inputStream != null) {
        long l1 = paramHttpEntity.getContentLength();
        if (l1 > 2147483647L)
          throw new IllegalArgumentException("HTTP entity too large to be buffered in memory"); 
        long l2 = l1;
        if (l1 < 0L)
          l2 = 4096L; 
        try {
          null = new ByteArrayBuffer();
          this((int)l2);
          try {
            arrayOfByte2 = new byte[4096];
            while (true) {
              int i = inputStream.read(arrayOfByte2);
              if (i != -1 && !Thread.currentThread().isInterrupted()) {
                null.append(arrayOfByte2, 0, i);
                sendProgressDataMessage(copyOfRange(arrayOfByte2, 0, i));
                continue;
              } 
              break;
            } 
          } finally {
            AsyncHttpClient.silentCloseInputStream(inputStream);
          } 
        } catch (OutOfMemoryError outOfMemoryError) {
          System.gc();
          throw new IOException("File too large to fit into available memory");
        } 
        AsyncHttpClient.silentCloseInputStream(inputStream);
        arrayOfByte2 = outOfMemoryError.toByteArray();
      } 
    } 
    return arrayOfByte2;
  }
  
  protected void handleMessage(Message paramMessage) {
    super.handleMessage(paramMessage);
    switch (paramMessage.what) {
      default:
        return;
      case 6:
        break;
    } 
    Object[] arrayOfObject = (Object[])paramMessage.obj;
    if (arrayOfObject != null && arrayOfObject.length >= 1)
      try {
        onProgressData((byte[])arrayOfObject[0]);
      } catch (Throwable throwable) {
        Log.e("DataAsyncHttpResponseHandler", "custom onProgressData contains an error", throwable);
      }  
    Log.e("DataAsyncHttpResponseHandler", "PROGRESS_DATA_MESSAGE didn't got enough params");
  }
  
  public void onProgressData(byte[] paramArrayOfbyte) {
    Log.d("DataAsyncHttpResponseHandler", "onProgressData(byte[]) was not overriden, but callback was received");
  }
  
  public final void sendProgressDataMessage(byte[] paramArrayOfbyte) {
    sendMessage(obtainMessage(6, new Object[] { paramArrayOfbyte }));
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/loopj/android/http/DataAsyncHttpResponseHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */