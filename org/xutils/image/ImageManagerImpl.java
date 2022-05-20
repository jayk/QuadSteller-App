package org.xutils.image;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import java.io.File;
import org.xutils.ImageManager;
import org.xutils.common.Callback;
import org.xutils.x;

public final class ImageManagerImpl implements ImageManager {
  private static volatile ImageManagerImpl instance;
  
  private static final Object lock = new Object();
  
  public static void registerInstance() {
    if (instance == null)
      synchronized (lock) {
        if (instance == null) {
          ImageManagerImpl imageManagerImpl = new ImageManagerImpl();
          this();
          instance = imageManagerImpl;
        } 
        x.Ext.setImageManager(instance);
        return;
      }  
    x.Ext.setImageManager(instance);
  }
  
  public void bind(final ImageView view, final String url) {
    x.task().autoPost(new Runnable() {
          public void run() {
            ImageLoader.doBind(view, url, null, null);
          }
        });
  }
  
  public void bind(final ImageView view, final String url, final Callback.CommonCallback<Drawable> callback) {
    x.task().autoPost(new Runnable() {
          public void run() {
            ImageLoader.doBind(view, url, null, callback);
          }
        });
  }
  
  public void bind(final ImageView view, final String url, final ImageOptions options) {
    x.task().autoPost(new Runnable() {
          public void run() {
            ImageLoader.doBind(view, url, options, null);
          }
        });
  }
  
  public void bind(final ImageView view, final String url, final ImageOptions options, final Callback.CommonCallback<Drawable> callback) {
    x.task().autoPost(new Runnable() {
          public void run() {
            ImageLoader.doBind(view, url, options, callback);
          }
        });
  }
  
  public void clearCacheFiles() {
    ImageLoader.clearCacheFiles();
    ImageDecoder.clearCacheFiles();
  }
  
  public void clearMemCache() {
    ImageLoader.clearMemCache();
  }
  
  public Callback.Cancelable loadDrawable(String paramString, ImageOptions paramImageOptions, Callback.CommonCallback<Drawable> paramCommonCallback) {
    return ImageLoader.doLoadDrawable(paramString, paramImageOptions, paramCommonCallback);
  }
  
  public Callback.Cancelable loadFile(String paramString, ImageOptions paramImageOptions, Callback.CacheCallback<File> paramCacheCallback) {
    return ImageLoader.doLoadFile(paramString, paramImageOptions, paramCacheCallback);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/image/ImageManagerImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */