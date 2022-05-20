package org.xutils.image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

final class ReusableBitmapDrawable extends BitmapDrawable implements ReusableDrawable {
  private MemCacheKey key;
  
  public ReusableBitmapDrawable(Resources paramResources, Bitmap paramBitmap) {
    super(paramResources, paramBitmap);
  }
  
  public MemCacheKey getMemCacheKey() {
    return this.key;
  }
  
  public void setMemCacheKey(MemCacheKey paramMemCacheKey) {
    this.key = paramMemCacheKey;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/image/ReusableBitmapDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */