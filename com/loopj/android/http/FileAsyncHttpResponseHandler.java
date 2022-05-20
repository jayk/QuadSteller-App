package com.loopj.android.http;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;

public abstract class FileAsyncHttpResponseHandler extends AsyncHttpResponseHandler {
  private static final String LOG_TAG = "FileAsyncHttpResponseHandler";
  
  protected final boolean append;
  
  protected final File mFile;
  
  static {
    boolean bool;
    if (!FileAsyncHttpResponseHandler.class.desiredAssertionStatus()) {
      bool = true;
    } else {
      bool = false;
    } 
    $assertionsDisabled = bool;
  }
  
  public FileAsyncHttpResponseHandler(Context paramContext) {
    this.mFile = getTemporaryFile(paramContext);
    this.append = false;
  }
  
  public FileAsyncHttpResponseHandler(File paramFile) {
    this(paramFile, false);
  }
  
  public FileAsyncHttpResponseHandler(File paramFile, boolean paramBoolean) {
    boolean bool;
    if (paramFile != null) {
      bool = true;
    } else {
      bool = false;
    } 
    AssertUtils.asserts(bool, "File passed into FileAsyncHttpResponseHandler constructor must not be null");
    this.mFile = paramFile;
    this.append = paramBoolean;
  }
  
  public boolean deleteTargetFile() {
    return (getTargetFile() != null && getTargetFile().delete());
  }
  
  protected byte[] getResponseData(HttpEntity paramHttpEntity) throws IOException {
    if (paramHttpEntity != null) {
      InputStream inputStream = paramHttpEntity.getContent();
      long l = paramHttpEntity.getContentLength();
      FileOutputStream fileOutputStream = new FileOutputStream(getTargetFile(), this.append);
      if (inputStream != null) {
        try {
          byte[] arrayOfByte = new byte[4096];
          int i = 0;
          while (true) {
            int j = inputStream.read(arrayOfByte);
            if (j != -1 && !Thread.currentThread().isInterrupted()) {
              i += j;
              fileOutputStream.write(arrayOfByte, 0, j);
              sendProgressMessage(i, (int)l);
              continue;
            } 
            break;
          } 
        } finally {
          AsyncHttpClient.silentCloseInputStream(inputStream);
          fileOutputStream.flush();
          AsyncHttpClient.silentCloseOutputStream(fileOutputStream);
        } 
        AsyncHttpClient.silentCloseInputStream(inputStream);
        fileOutputStream.flush();
        AsyncHttpClient.silentCloseOutputStream(fileOutputStream);
      } 
    } 
    return null;
  }
  
  protected File getTargetFile() {
    assert this.mFile != null;
    return this.mFile;
  }
  
  protected File getTemporaryFile(Context paramContext) {
    boolean bool;
    if (paramContext != null) {
      bool = true;
    } else {
      bool = false;
    } 
    AssertUtils.asserts(bool, "Tried creating temporary file without having Context");
    try {
      assert paramContext != null;
    } catch (IOException null) {
      Log.e("FileAsyncHttpResponseHandler", "Cannot create temporary file", null);
      return null;
    } 
    return File.createTempFile("temp_", "_handled", paramContext.getCacheDir());
  }
  
  public abstract void onFailure(int paramInt, Header[] paramArrayOfHeader, Throwable paramThrowable, File paramFile);
  
  public final void onFailure(int paramInt, Header[] paramArrayOfHeader, byte[] paramArrayOfbyte, Throwable paramThrowable) {
    onFailure(paramInt, paramArrayOfHeader, paramThrowable, getTargetFile());
  }
  
  public abstract void onSuccess(int paramInt, Header[] paramArrayOfHeader, File paramFile);
  
  public final void onSuccess(int paramInt, Header[] paramArrayOfHeader, byte[] paramArrayOfbyte) {
    onSuccess(paramInt, paramArrayOfHeader, getTargetFile());
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/loopj/android/http/FileAsyncHttpResponseHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */