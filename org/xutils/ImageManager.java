package org.xutils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import java.io.File;
import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;

public interface ImageManager {
  void bind(ImageView paramImageView, String paramString);
  
  void bind(ImageView paramImageView, String paramString, Callback.CommonCallback<Drawable> paramCommonCallback);
  
  void bind(ImageView paramImageView, String paramString, ImageOptions paramImageOptions);
  
  void bind(ImageView paramImageView, String paramString, ImageOptions paramImageOptions, Callback.CommonCallback<Drawable> paramCommonCallback);
  
  void clearCacheFiles();
  
  void clearMemCache();
  
  Callback.Cancelable loadDrawable(String paramString, ImageOptions paramImageOptions, Callback.CommonCallback<Drawable> paramCommonCallback);
  
  Callback.Cancelable loadFile(String paramString, ImageOptions paramImageOptions, Callback.CacheCallback<File> paramCacheCallback);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/ImageManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */