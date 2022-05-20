package com.squareup.picasso;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.IOException;

class ResourceRequestHandler extends RequestHandler {
  private final Context context;
  
  ResourceRequestHandler(Context paramContext) {
    this.context = paramContext;
  }
  
  private static Bitmap decodeResource(Resources paramResources, int paramInt, Request paramRequest) {
    BitmapFactory.Options options = createBitmapOptions(paramRequest);
    if (requiresInSampleSize(options)) {
      BitmapFactory.decodeResource(paramResources, paramInt, options);
      calculateInSampleSize(paramRequest.targetWidth, paramRequest.targetHeight, options, paramRequest);
    } 
    return BitmapFactory.decodeResource(paramResources, paramInt, options);
  }
  
  public boolean canHandleRequest(Request paramRequest) {
    return (paramRequest.resourceId != 0) ? true : "android.resource".equals(paramRequest.uri.getScheme());
  }
  
  public RequestHandler.Result load(Request paramRequest, int paramInt) throws IOException {
    Resources resources = Utils.getResources(this.context, paramRequest);
    return new RequestHandler.Result(decodeResource(resources, Utils.getResourceId(resources, paramRequest), paramRequest), Picasso.LoadedFrom.DISK);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/squareup/picasso/ResourceRequestHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */