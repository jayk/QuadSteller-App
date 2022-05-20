package com.loopj.android.http;

import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;

public abstract class RangeFileAsyncHttpResponseHandler extends FileAsyncHttpResponseHandler {
  private static final String LOG_TAG = "RangeFileAsyncHttpResponseHandler";
  
  private boolean append = false;
  
  private long current = 0L;
  
  public RangeFileAsyncHttpResponseHandler(File paramFile) {
    super(paramFile);
  }
  
  protected byte[] getResponseData(HttpEntity paramHttpEntity) throws IOException {
    if (paramHttpEntity != null) {
      InputStream inputStream = paramHttpEntity.getContent();
      long l = paramHttpEntity.getContentLength() + this.current;
      FileOutputStream fileOutputStream = new FileOutputStream(getTargetFile(), this.append);
      if (inputStream != null) {
        try {
        
        } finally {
          inputStream.close();
          fileOutputStream.flush();
          fileOutputStream.close();
        } 
        inputStream.close();
        fileOutputStream.flush();
        fileOutputStream.close();
      } 
    } 
    return null;
  }
  
  public void sendResponseMessage(HttpResponse paramHttpResponse) throws IOException {
    StatusLine statusLine;
    if (!Thread.currentThread().isInterrupted()) {
      statusLine = paramHttpResponse.getStatusLine();
      if (statusLine.getStatusCode() == 416) {
        if (!Thread.currentThread().isInterrupted())
          sendSuccessMessage(statusLine.getStatusCode(), paramHttpResponse.getAllHeaders(), null); 
        return;
      } 
    } else {
      return;
    } 
    if (statusLine.getStatusCode() >= 300) {
      if (!Thread.currentThread().isInterrupted())
        sendFailureMessage(statusLine.getStatusCode(), paramHttpResponse.getAllHeaders(), null, (Throwable)new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase())); 
      return;
    } 
    if (!Thread.currentThread().isInterrupted()) {
      Header header = paramHttpResponse.getFirstHeader("Content-Range");
      if (header == null) {
        this.append = false;
        this.current = 0L;
      } else {
        Log.v("RangeFileAsyncHttpResponseHandler", "Content-Range: " + header.getValue());
      } 
      sendSuccessMessage(statusLine.getStatusCode(), paramHttpResponse.getAllHeaders(), getResponseData(paramHttpResponse.getEntity()));
    } 
  }
  
  public void updateRequestHeaders(HttpUriRequest paramHttpUriRequest) {
    if (this.mFile.exists() && this.mFile.canWrite())
      this.current = this.mFile.length(); 
    if (this.current > 0L) {
      this.append = true;
      paramHttpUriRequest.setHeader("Range", "bytes=" + this.current + "-");
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/loopj/android/http/RangeFileAsyncHttpResponseHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */