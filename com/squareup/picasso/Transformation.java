package com.squareup.picasso;

import android.graphics.Bitmap;

public interface Transformation {
  String key();
  
  Bitmap transform(Bitmap paramBitmap);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/squareup/picasso/Transformation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */