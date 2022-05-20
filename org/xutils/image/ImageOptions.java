package org.xutils.image;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import java.lang.reflect.Field;
import org.xutils.common.util.DensityUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

public class ImageOptions {
  public static final ImageOptions DEFAULT = new ImageOptions();
  
  private Animation animation = null;
  
  private boolean autoRotate = false;
  
  private boolean circular = false;
  
  private boolean compress = true;
  
  private Bitmap.Config config = Bitmap.Config.RGB_565;
  
  private boolean crop = false;
  
  private boolean fadeIn = false;
  
  private Drawable failureDrawable = null;
  
  private int failureDrawableId = 0;
  
  private boolean forceLoadingDrawable = true;
  
  private int height = 0;
  
  private boolean ignoreGif = true;
  
  private ImageView.ScaleType imageScaleType = ImageView.ScaleType.CENTER_CROP;
  
  private Drawable loadingDrawable = null;
  
  private int loadingDrawableId = 0;
  
  private int maxHeight = 0;
  
  private int maxWidth = 0;
  
  private ParamsBuilder paramsBuilder;
  
  private ImageView.ScaleType placeholderScaleType = ImageView.ScaleType.CENTER_INSIDE;
  
  private int radius = 0;
  
  private boolean square = false;
  
  private boolean useMemCache = true;
  
  private int width = 0;
  
  private static int getImageViewFieldValue(ImageView paramImageView, String paramString) {
    byte b2;
    byte b1 = 0;
    try {
      Field field = ImageView.class.getDeclaredField(paramString);
      field.setAccessible(true);
      int i = ((Integer)field.get(paramImageView)).intValue();
      b2 = b1;
      if (i > 0) {
        b2 = b1;
        if (i < Integer.MAX_VALUE)
          b2 = i; 
      } 
    } catch (Throwable throwable) {
      b2 = b1;
    } 
    return b2;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool1 = true;
    boolean bool2 = false;
    if (this == paramObject)
      return true; 
    boolean bool3 = bool2;
    if (paramObject != null) {
      bool3 = bool2;
      if (getClass() == paramObject.getClass()) {
        paramObject = paramObject;
        bool3 = bool2;
        if (this.maxWidth == ((ImageOptions)paramObject).maxWidth) {
          bool3 = bool2;
          if (this.maxHeight == ((ImageOptions)paramObject).maxHeight) {
            bool3 = bool2;
            if (this.width == ((ImageOptions)paramObject).width) {
              bool3 = bool2;
              if (this.height == ((ImageOptions)paramObject).height) {
                bool3 = bool2;
                if (this.crop == ((ImageOptions)paramObject).crop) {
                  bool3 = bool2;
                  if (this.radius == ((ImageOptions)paramObject).radius) {
                    bool3 = bool2;
                    if (this.square == ((ImageOptions)paramObject).square) {
                      bool3 = bool2;
                      if (this.circular == ((ImageOptions)paramObject).circular) {
                        bool3 = bool2;
                        if (this.autoRotate == ((ImageOptions)paramObject).autoRotate) {
                          bool3 = bool2;
                          if (this.compress == ((ImageOptions)paramObject).compress) {
                            if (this.config == ((ImageOptions)paramObject).config)
                              return bool1; 
                            bool3 = false;
                          } 
                        } 
                      } 
                    } 
                  } 
                } 
              } 
            } 
          } 
        } 
      } 
    } 
    return bool3;
  }
  
  public Animation getAnimation() {
    return this.animation;
  }
  
  public Bitmap.Config getConfig() {
    return this.config;
  }
  
  public Drawable getFailureDrawable(ImageView paramImageView) {
    if (this.failureDrawable == null && this.failureDrawableId > 0 && paramImageView != null)
      try {
        this.failureDrawable = paramImageView.getResources().getDrawable(this.failureDrawableId);
      } catch (Throwable throwable) {
        LogUtil.e(throwable.getMessage(), throwable);
      }  
    return this.failureDrawable;
  }
  
  public int getHeight() {
    return this.height;
  }
  
  public ImageView.ScaleType getImageScaleType() {
    return this.imageScaleType;
  }
  
  public Drawable getLoadingDrawable(ImageView paramImageView) {
    if (this.loadingDrawable == null && this.loadingDrawableId > 0 && paramImageView != null)
      try {
        this.loadingDrawable = paramImageView.getResources().getDrawable(this.loadingDrawableId);
      } catch (Throwable throwable) {
        LogUtil.e(throwable.getMessage(), throwable);
      }  
    return this.loadingDrawable;
  }
  
  public int getMaxHeight() {
    return this.maxHeight;
  }
  
  public int getMaxWidth() {
    return this.maxWidth;
  }
  
  public ParamsBuilder getParamsBuilder() {
    return this.paramsBuilder;
  }
  
  public ImageView.ScaleType getPlaceholderScaleType() {
    return this.placeholderScaleType;
  }
  
  public int getRadius() {
    return this.radius;
  }
  
  public int getWidth() {
    return this.width;
  }
  
  public int hashCode() {
    byte b2;
    byte b3;
    byte b4;
    byte b5;
    byte b1 = 1;
    int i = 0;
    int j = this.maxWidth;
    int k = this.maxHeight;
    int m = this.width;
    int n = this.height;
    if (this.crop) {
      b2 = 1;
    } else {
      b2 = 0;
    } 
    int i1 = this.radius;
    if (this.square) {
      b3 = 1;
    } else {
      b3 = 0;
    } 
    if (this.circular) {
      b4 = 1;
    } else {
      b4 = 0;
    } 
    if (this.autoRotate) {
      b5 = 1;
    } else {
      b5 = 0;
    } 
    if (!this.compress)
      b1 = 0; 
    if (this.config != null)
      i = this.config.hashCode(); 
    return (((((((((j * 31 + k) * 31 + m) * 31 + n) * 31 + b2) * 31 + i1) * 31 + b3) * 31 + b4) * 31 + b5) * 31 + b1) * 31 + i;
  }
  
  public boolean isAutoRotate() {
    return this.autoRotate;
  }
  
  public boolean isCircular() {
    return this.circular;
  }
  
  public boolean isCompress() {
    return this.compress;
  }
  
  public boolean isCrop() {
    return this.crop;
  }
  
  public boolean isFadeIn() {
    return this.fadeIn;
  }
  
  public boolean isForceLoadingDrawable() {
    return this.forceLoadingDrawable;
  }
  
  public boolean isIgnoreGif() {
    return this.ignoreGif;
  }
  
  public boolean isSquare() {
    return this.square;
  }
  
  public boolean isUseMemCache() {
    return this.useMemCache;
  }
  
  final void optimizeMaxSize(ImageView paramImageView) {
    if (this.width > 0 && this.height > 0) {
      this.maxWidth = this.width;
      this.maxHeight = this.height;
      return;
    } 
    int i = DensityUtil.getScreenWidth();
    int j = DensityUtil.getScreenHeight();
    if (this.width < 0) {
      this.maxWidth = i * 3 / 2;
      this.compress = false;
    } 
    if (this.height < 0) {
      this.maxHeight = j * 3 / 2;
      this.compress = false;
    } 
    if (paramImageView == null && this.maxWidth <= 0 && this.maxHeight <= 0) {
      this.maxWidth = i;
      this.maxHeight = j;
      return;
    } 
    int k = this.maxWidth;
    int m = this.maxHeight;
    int n = m;
    int i1 = k;
    if (paramImageView != null) {
      ViewGroup.LayoutParams layoutParams = paramImageView.getLayoutParams();
      int i3 = m;
      i1 = k;
      if (layoutParams != null) {
        n = k;
        if (k <= 0)
          if (layoutParams.width > 0) {
            i3 = layoutParams.width;
            n = i3;
            if (this.width <= 0) {
              this.width = i3;
              n = i3;
            } 
          } else {
            n = k;
            if (layoutParams.width != -2)
              n = paramImageView.getWidth(); 
          }  
        i3 = m;
        i1 = n;
        if (m <= 0)
          if (layoutParams.height > 0) {
            k = layoutParams.height;
            i3 = k;
            i1 = n;
            if (this.height <= 0) {
              this.height = k;
              i1 = n;
              i3 = k;
            } 
          } else {
            i3 = m;
            i1 = n;
            if (layoutParams.height != -2) {
              i3 = paramImageView.getHeight();
              i1 = n;
            } 
          }  
      } 
      k = i1;
      if (i1 <= 0)
        k = getImageViewFieldValue(paramImageView, "mMaxWidth"); 
      n = i3;
      i1 = k;
      if (i3 <= 0) {
        n = getImageViewFieldValue(paramImageView, "mMaxHeight");
        i1 = k;
      } 
    } 
    int i2 = i1;
    if (i1 <= 0)
      i2 = i; 
    i1 = n;
    if (n <= 0)
      i1 = j; 
    this.maxWidth = i2;
    this.maxHeight = i1;
  }
  
  public String toString() {
    boolean bool1 = true;
    StringBuilder stringBuilder1 = new StringBuilder("_");
    stringBuilder1.append(this.maxWidth).append("_");
    stringBuilder1.append(this.maxHeight).append("_");
    stringBuilder1.append(this.width).append("_");
    stringBuilder1.append(this.height).append("_");
    stringBuilder1.append(this.radius).append("_");
    stringBuilder1.append(this.config).append("_");
    if (this.crop) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    StringBuilder stringBuilder2 = stringBuilder1.append(bool2);
    if (this.square) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    stringBuilder2 = stringBuilder2.append(bool2);
    if (this.circular) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    stringBuilder2.append(bool2);
    if (this.autoRotate) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    stringBuilder2 = stringBuilder1.append(bool2);
    if (this.compress) {
      bool2 = bool1;
      stringBuilder2.append(bool2);
      return stringBuilder1.toString();
    } 
    boolean bool2 = false;
    stringBuilder2.append(bool2);
    return stringBuilder1.toString();
  }
  
  public static class Builder {
    protected ImageOptions options;
    
    public Builder() {
      newImageOptions();
    }
    
    public ImageOptions build() {
      return this.options;
    }
    
    protected void newImageOptions() {
      this.options = new ImageOptions();
    }
    
    public Builder setAnimation(Animation param1Animation) {
      ImageOptions.access$1402(this.options, param1Animation);
      return this;
    }
    
    public Builder setAutoRotate(boolean param1Boolean) {
      ImageOptions.access$602(this.options, param1Boolean);
      return this;
    }
    
    public Builder setCircular(boolean param1Boolean) {
      ImageOptions.access$502(this.options, param1Boolean);
      return this;
    }
    
    public Builder setConfig(Bitmap.Config param1Config) {
      ImageOptions.access$702(this.options, param1Config);
      return this;
    }
    
    public Builder setCrop(boolean param1Boolean) {
      ImageOptions.access$202(this.options, param1Boolean);
      return this;
    }
    
    public Builder setFadeIn(boolean param1Boolean) {
      ImageOptions.access$1302(this.options, param1Boolean);
      return this;
    }
    
    public Builder setFailureDrawable(Drawable param1Drawable) {
      ImageOptions.access$1202(this.options, param1Drawable);
      return this;
    }
    
    public Builder setFailureDrawableId(int param1Int) {
      ImageOptions.access$1102(this.options, param1Int);
      return this;
    }
    
    public Builder setForceLoadingDrawable(boolean param1Boolean) {
      ImageOptions.access$1702(this.options, param1Boolean);
      return this;
    }
    
    public Builder setIgnoreGif(boolean param1Boolean) {
      ImageOptions.access$802(this.options, param1Boolean);
      return this;
    }
    
    public Builder setImageScaleType(ImageView.ScaleType param1ScaleType) {
      ImageOptions.access$1602(this.options, param1ScaleType);
      return this;
    }
    
    public Builder setLoadingDrawable(Drawable param1Drawable) {
      ImageOptions.access$1002(this.options, param1Drawable);
      return this;
    }
    
    public Builder setLoadingDrawableId(int param1Int) {
      ImageOptions.access$902(this.options, param1Int);
      return this;
    }
    
    public Builder setParamsBuilder(ImageOptions.ParamsBuilder param1ParamsBuilder) {
      ImageOptions.access$1902(this.options, param1ParamsBuilder);
      return this;
    }
    
    public Builder setPlaceholderScaleType(ImageView.ScaleType param1ScaleType) {
      ImageOptions.access$1502(this.options, param1ScaleType);
      return this;
    }
    
    public Builder setRadius(int param1Int) {
      ImageOptions.access$302(this.options, param1Int);
      return this;
    }
    
    public Builder setSize(int param1Int1, int param1Int2) {
      ImageOptions.access$002(this.options, param1Int1);
      ImageOptions.access$102(this.options, param1Int2);
      return this;
    }
    
    public Builder setSquare(boolean param1Boolean) {
      ImageOptions.access$402(this.options, param1Boolean);
      return this;
    }
    
    public Builder setUseMemCache(boolean param1Boolean) {
      ImageOptions.access$1802(this.options, param1Boolean);
      return this;
    }
  }
  
  public static interface ParamsBuilder {
    RequestParams buildParams(RequestParams param1RequestParams, ImageOptions param1ImageOptions);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/image/ImageOptions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */