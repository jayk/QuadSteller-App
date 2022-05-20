package com.squareup.picasso;

import android.graphics.Bitmap;
import android.net.NetworkInfo;
import java.io.IOException;
import java.io.InputStream;

class NetworkRequestHandler extends RequestHandler {
  static final int RETRY_COUNT = 2;
  
  private static final String SCHEME_HTTP = "http";
  
  private static final String SCHEME_HTTPS = "https";
  
  private final Downloader downloader;
  
  private final Stats stats;
  
  public NetworkRequestHandler(Downloader paramDownloader, Stats paramStats) {
    this.downloader = paramDownloader;
    this.stats = paramStats;
  }
  
  public boolean canHandleRequest(Request paramRequest) {
    String str = paramRequest.uri.getScheme();
    return ("http".equals(str) || "https".equals(str));
  }
  
  int getRetryCount() {
    return 2;
  }
  
  public RequestHandler.Result load(Request paramRequest, int paramInt) throws IOException {
    RequestHandler.Result result;
    Picasso.LoadedFrom loadedFrom;
    Request request2 = null;
    Downloader.Response response = this.downloader.load(paramRequest.uri, paramRequest.networkPolicy);
    if (response == null)
      return (RequestHandler.Result)request2; 
    if (response.cached) {
      loadedFrom = Picasso.LoadedFrom.DISK;
    } else {
      loadedFrom = Picasso.LoadedFrom.NETWORK;
    } 
    Bitmap bitmap = response.getBitmap();
    if (bitmap != null)
      return new RequestHandler.Result(bitmap, loadedFrom); 
    InputStream inputStream = response.getInputStream();
    Request request1 = request2;
    if (inputStream != null) {
      if (loadedFrom == Picasso.LoadedFrom.DISK && response.getContentLength() == 0L) {
        Utils.closeQuietly(inputStream);
        throw new ContentLengthException("Received response with 0 content-length header.");
      } 
      if (loadedFrom == Picasso.LoadedFrom.NETWORK && response.getContentLength() > 0L)
        this.stats.dispatchDownloadFinished(response.getContentLength()); 
      result = new RequestHandler.Result(inputStream, loadedFrom);
    } 
    return result;
  }
  
  boolean shouldRetry(boolean paramBoolean, NetworkInfo paramNetworkInfo) {
    return (paramNetworkInfo == null || paramNetworkInfo.isConnected());
  }
  
  boolean supportsReplay() {
    return true;
  }
  
  static class ContentLengthException extends IOException {
    public ContentLengthException(String param1String) {
      super(param1String);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/squareup/picasso/NetworkRequestHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */