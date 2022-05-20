package com.squareup.picasso;

import android.graphics.Bitmap;

public interface Cache {
  public static final Cache NONE = new Cache() {
      public void clear() {}
      
      public void clearKeyUri(String param1String) {}
      
      public Bitmap get(String param1String) {
        return null;
      }
      
      public int maxSize() {
        return 0;
      }
      
      public void set(String param1String, Bitmap param1Bitmap) {}
      
      public int size() {
        return 0;
      }
    };
  
  void clear();
  
  void clearKeyUri(String paramString);
  
  Bitmap get(String paramString);
  
  int maxSize();
  
  void set(String paramString, Bitmap paramBitmap);
  
  int size();
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/squareup/picasso/Cache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */