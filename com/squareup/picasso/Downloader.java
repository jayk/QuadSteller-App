package com.squareup.picasso;

import android.graphics.Bitmap;
import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;

public interface Downloader {
  Response load(Uri paramUri, int paramInt) throws IOException;
  
  void shutdown();
  
  public static class Response {
    final Bitmap bitmap;
    
    final boolean cached;
    
    final long contentLength;
    
    final InputStream stream;
    
    @Deprecated
    public Response(Bitmap param1Bitmap, boolean param1Boolean) {
      if (param1Bitmap == null)
        throw new IllegalArgumentException("Bitmap may not be null."); 
      this.stream = null;
      this.bitmap = param1Bitmap;
      this.cached = param1Boolean;
      this.contentLength = -1L;
    }
    
    @Deprecated
    public Response(Bitmap param1Bitmap, boolean param1Boolean, long param1Long) {
      this(param1Bitmap, param1Boolean);
    }
    
    @Deprecated
    public Response(InputStream param1InputStream, boolean param1Boolean) {
      this(param1InputStream, param1Boolean, -1L);
    }
    
    public Response(InputStream param1InputStream, boolean param1Boolean, long param1Long) {
      if (param1InputStream == null)
        throw new IllegalArgumentException("Stream may not be null."); 
      this.stream = param1InputStream;
      this.bitmap = null;
      this.cached = param1Boolean;
      this.contentLength = param1Long;
    }
    
    @Deprecated
    public Bitmap getBitmap() {
      return this.bitmap;
    }
    
    public long getContentLength() {
      return this.contentLength;
    }
    
    public InputStream getInputStream() {
      return this.stream;
    }
  }
  
  public static class ResponseException extends IOException {
    final boolean localCacheOnly;
    
    final int responseCode;
    
    public ResponseException(String param1String, int param1Int1, int param1Int2) {
      super(param1String);
      this.localCacheOnly = NetworkPolicy.isOfflineOnly(param1Int1);
      this.responseCode = param1Int2;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/squareup/picasso/Downloader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */