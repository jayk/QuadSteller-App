package com.squareup.picasso;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import java.io.IOException;

class MediaStoreRequestHandler extends ContentStreamRequestHandler {
  private static final String[] CONTENT_ORIENTATION = new String[] { "orientation" };
  
  MediaStoreRequestHandler(Context paramContext) {
    super(paramContext);
  }
  
  static int getExifOrientation(ContentResolver paramContentResolver, Uri paramUri) {
    Cursor cursor1 = null;
    Cursor cursor2 = null;
    try {
      Cursor cursor = paramContentResolver.query(paramUri, CONTENT_ORIENTATION, null, null, null);
      if (cursor != null) {
        cursor2 = cursor;
        cursor1 = cursor;
        boolean bool = cursor.moveToFirst();
        if (!bool)
          return 0; 
        cursor2 = cursor;
        cursor1 = cursor;
        int j = cursor.getInt(0);
        int i = j;
        return i;
      } 
      return 0;
    } catch (RuntimeException runtimeException) {
      return 0;
    } finally {
      if (cursor1 != null)
        cursor1.close(); 
    } 
  }
  
  static PicassoKind getPicassoKind(int paramInt1, int paramInt2) {
    return (paramInt1 <= PicassoKind.MICRO.width && paramInt2 <= PicassoKind.MICRO.height) ? PicassoKind.MICRO : ((paramInt1 <= PicassoKind.MINI.width && paramInt2 <= PicassoKind.MINI.height) ? PicassoKind.MINI : PicassoKind.FULL);
  }
  
  public boolean canHandleRequest(Request paramRequest) {
    Uri uri = paramRequest.uri;
    return ("content".equals(uri.getScheme()) && "media".equals(uri.getAuthority()));
  }
  
  public RequestHandler.Result load(Request paramRequest, int paramInt) throws IOException {
    ContentResolver contentResolver = this.context.getContentResolver();
    int i = getExifOrientation(contentResolver, paramRequest.uri);
    String str = contentResolver.getType(paramRequest.uri);
    if (str != null && str.startsWith("video/")) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    if (paramRequest.hasSize()) {
      Bitmap bitmap;
      PicassoKind picassoKind = getPicassoKind(paramRequest.targetWidth, paramRequest.targetHeight);
      if (paramInt == 0 && picassoKind == PicassoKind.FULL)
        return new RequestHandler.Result(null, getInputStream(paramRequest), Picasso.LoadedFrom.DISK, i); 
      long l = ContentUris.parseId(paramRequest.uri);
      BitmapFactory.Options options = createBitmapOptions(paramRequest);
      options.inJustDecodeBounds = true;
      calculateInSampleSize(paramRequest.targetWidth, paramRequest.targetHeight, picassoKind.width, picassoKind.height, options, paramRequest);
      if (paramInt != 0) {
        if (picassoKind == PicassoKind.FULL) {
          paramInt = 1;
        } else {
          paramInt = picassoKind.androidKind;
        } 
        bitmap = MediaStore.Video.Thumbnails.getThumbnail(contentResolver, l, paramInt, options);
      } else {
        bitmap = MediaStore.Images.Thumbnails.getThumbnail((ContentResolver)bitmap, l, picassoKind.androidKind, options);
      } 
      if (bitmap != null)
        return new RequestHandler.Result(bitmap, null, Picasso.LoadedFrom.DISK, i); 
    } 
    return new RequestHandler.Result(null, getInputStream(paramRequest), Picasso.LoadedFrom.DISK, i);
  }
  
  enum PicassoKind {
    FULL,
    MICRO(3, 96, 96),
    MINI(1, 512, 384);
    
    final int androidKind;
    
    final int height;
    
    final int width;
    
    static {
      $VALUES = new PicassoKind[] { MICRO, MINI, FULL };
    }
    
    PicassoKind(int param1Int1, int param1Int2, int param1Int3) {
      this.androidKind = param1Int1;
      this.width = param1Int2;
      this.height = param1Int3;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/squareup/picasso/MediaStoreRequestHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */