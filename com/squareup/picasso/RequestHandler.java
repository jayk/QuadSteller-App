package com.squareup.picasso;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.NetworkInfo;
import java.io.IOException;
import java.io.InputStream;

public abstract class RequestHandler {
  static void calculateInSampleSize(int paramInt1, int paramInt2, int paramInt3, int paramInt4, BitmapFactory.Options paramOptions, Request paramRequest) {
    int i = 1;
    if (paramInt4 > paramInt2 || paramInt3 > paramInt1)
      if (paramInt2 == 0) {
        i = (int)Math.floor((paramInt3 / paramInt1));
      } else if (paramInt1 == 0) {
        i = (int)Math.floor((paramInt4 / paramInt2));
      } else {
        paramInt2 = (int)Math.floor((paramInt4 / paramInt2));
        paramInt1 = (int)Math.floor((paramInt3 / paramInt1));
        if (paramRequest.centerInside) {
          i = Math.max(paramInt2, paramInt1);
        } else {
          i = Math.min(paramInt2, paramInt1);
        } 
      }  
    paramOptions.inSampleSize = i;
    paramOptions.inJustDecodeBounds = false;
  }
  
  static void calculateInSampleSize(int paramInt1, int paramInt2, BitmapFactory.Options paramOptions, Request paramRequest) {
    calculateInSampleSize(paramInt1, paramInt2, paramOptions.outWidth, paramOptions.outHeight, paramOptions, paramRequest);
  }
  
  static BitmapFactory.Options createBitmapOptions(Request paramRequest) {
    boolean bool1;
    boolean bool = paramRequest.hasSize();
    if (paramRequest.config != null) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    BitmapFactory.Options options = null;
    if (bool || bool1) {
      BitmapFactory.Options options1 = new BitmapFactory.Options();
      options1.inJustDecodeBounds = bool;
      options = options1;
      if (bool1) {
        options1.inPreferredConfig = paramRequest.config;
        options = options1;
      } 
    } 
    return options;
  }
  
  static boolean requiresInSampleSize(BitmapFactory.Options paramOptions) {
    return (paramOptions != null && paramOptions.inJustDecodeBounds);
  }
  
  public abstract boolean canHandleRequest(Request paramRequest);
  
  int getRetryCount() {
    return 0;
  }
  
  public abstract Result load(Request paramRequest, int paramInt) throws IOException;
  
  boolean shouldRetry(boolean paramBoolean, NetworkInfo paramNetworkInfo) {
    return false;
  }
  
  boolean supportsReplay() {
    return false;
  }
  
  public static final class Result {
    private final Bitmap bitmap;
    
    private final int exifOrientation;
    
    private final Picasso.LoadedFrom loadedFrom;
    
    private final InputStream stream;
    
    public Result(Bitmap param1Bitmap, Picasso.LoadedFrom param1LoadedFrom) {
      this(Utils.<Bitmap>checkNotNull(param1Bitmap, "bitmap == null"), null, param1LoadedFrom, 0);
    }
    
    Result(Bitmap param1Bitmap, InputStream param1InputStream, Picasso.LoadedFrom param1LoadedFrom, int param1Int) {
      boolean bool2;
      if (param1Bitmap != null) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      if (param1InputStream == null)
        bool1 = false; 
      if ((bool1 ^ bool2) == 0)
        throw new AssertionError(); 
      this.bitmap = param1Bitmap;
      this.stream = param1InputStream;
      this.loadedFrom = Utils.<Picasso.LoadedFrom>checkNotNull(param1LoadedFrom, "loadedFrom == null");
      this.exifOrientation = param1Int;
    }
    
    public Result(InputStream param1InputStream, Picasso.LoadedFrom param1LoadedFrom) {
      this(null, Utils.<InputStream>checkNotNull(param1InputStream, "stream == null"), param1LoadedFrom, 0);
    }
    
    public Bitmap getBitmap() {
      return this.bitmap;
    }
    
    int getExifOrientation() {
      return this.exifOrientation;
    }
    
    public Picasso.LoadedFrom getLoadedFrom() {
      return this.loadedFrom;
    }
    
    public InputStream getStream() {
      return this.stream;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/squareup/picasso/RequestHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */