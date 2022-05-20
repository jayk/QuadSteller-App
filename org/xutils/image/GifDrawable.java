package org.xutils.image;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Movie;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import org.xutils.common.util.LogUtil;

public class GifDrawable extends Drawable implements Runnable, Animatable {
  private final long begin = SystemClock.uptimeMillis();
  
  private int byteCount;
  
  private final int duration;
  
  private final Movie movie;
  
  private int rate = 300;
  
  private volatile boolean running;
  
  public GifDrawable(Movie paramMovie, int paramInt) {
    this.movie = paramMovie;
    this.byteCount = paramInt;
    this.duration = paramMovie.duration();
  }
  
  public void draw(Canvas paramCanvas) {
    try {
      boolean bool;
      if (this.duration > 0) {
        bool = (int)(SystemClock.uptimeMillis() - this.begin) % this.duration;
      } else {
        bool = false;
      } 
      this.movie.setTime(bool);
      this.movie.draw(paramCanvas, 0.0F, 0.0F);
      start();
    } catch (Throwable throwable) {
      LogUtil.e(throwable.getMessage(), throwable);
    } 
  }
  
  public int getByteCount() {
    if (this.byteCount == 0)
      this.byteCount = this.movie.width() * this.movie.height() * 3 * 5; 
    return this.byteCount;
  }
  
  public int getDuration() {
    return this.duration;
  }
  
  public int getIntrinsicHeight() {
    return this.movie.height();
  }
  
  public int getIntrinsicWidth() {
    return this.movie.width();
  }
  
  public Movie getMovie() {
    return this.movie;
  }
  
  public int getOpacity() {
    return this.movie.isOpaque() ? -1 : -3;
  }
  
  public int getRate() {
    return this.rate;
  }
  
  public boolean isRunning() {
    return (this.running && this.duration > 0);
  }
  
  public void run() {
    if (this.duration > 0) {
      invalidateSelf();
      scheduleSelf(this, SystemClock.uptimeMillis() + this.rate);
    } 
  }
  
  public void setAlpha(int paramInt) {}
  
  public void setColorFilter(ColorFilter paramColorFilter) {}
  
  public void setRate(int paramInt) {
    this.rate = paramInt;
  }
  
  public void start() {
    if (!isRunning()) {
      this.running = true;
      run();
    } 
  }
  
  public void stop() {
    if (isRunning())
      unscheduleSelf(this); 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/image/GifDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */