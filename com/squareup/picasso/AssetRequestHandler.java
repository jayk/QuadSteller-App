package com.squareup.picasso;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import java.io.IOException;

class AssetRequestHandler extends RequestHandler {
  protected static final String ANDROID_ASSET = "android_asset";
  
  private static final int ASSET_PREFIX_LENGTH = "file:///android_asset/".length();
  
  private final AssetManager assetManager;
  
  public AssetRequestHandler(Context paramContext) {
    this.assetManager = paramContext.getAssets();
  }
  
  static String getFilePath(Request paramRequest) {
    return paramRequest.uri.toString().substring(ASSET_PREFIX_LENGTH);
  }
  
  public boolean canHandleRequest(Request paramRequest) {
    boolean bool1 = false;
    Uri uri = paramRequest.uri;
    boolean bool2 = bool1;
    if ("file".equals(uri.getScheme())) {
      bool2 = bool1;
      if (!uri.getPathSegments().isEmpty()) {
        bool2 = bool1;
        if ("android_asset".equals(uri.getPathSegments().get(0)))
          bool2 = true; 
      } 
    } 
    return bool2;
  }
  
  public RequestHandler.Result load(Request paramRequest, int paramInt) throws IOException {
    return new RequestHandler.Result(this.assetManager.open(getFilePath(paramRequest)), Picasso.LoadedFrom.DISK);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/squareup/picasso/AssetRequestHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */