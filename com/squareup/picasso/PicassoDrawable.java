package com.squareup.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.widget.ImageView;

final class PicassoDrawable extends BitmapDrawable {
  private static final Paint DEBUG_PAINT = new Paint();
  
  private static final float FADE_DURATION = 200.0F;
  
  int alpha;
  
  boolean animating;
  
  private final boolean debugging;
  
  private final float density;
  
  private final Picasso.LoadedFrom loadedFrom;
  
  Drawable placeholder;
  
  long startTimeMillis;
  
  PicassoDrawable(Context paramContext, Bitmap paramBitmap, Drawable paramDrawable, Picasso.LoadedFrom paramLoadedFrom, boolean paramBoolean1, boolean paramBoolean2) {
    super(paramContext.getResources(), paramBitmap);
    boolean bool;
    this.alpha = 255;
    this.debugging = paramBoolean2;
    this.density = (paramContext.getResources().getDisplayMetrics()).density;
    this.loadedFrom = paramLoadedFrom;
    if (paramLoadedFrom != Picasso.LoadedFrom.MEMORY && !paramBoolean1) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool) {
      this.placeholder = paramDrawable;
      this.animating = true;
      this.startTimeMillis = SystemClock.uptimeMillis();
    } 
  }
  
  private void drawDebugIndicator(Canvas paramCanvas) {
    DEBUG_PAINT.setColor(-1);
    paramCanvas.drawPath(getTrianglePath(new Point(0, 0), (int)(16.0F * this.density)), DEBUG_PAINT);
    DEBUG_PAINT.setColor(this.loadedFrom.debugColor);
    paramCanvas.drawPath(getTrianglePath(new Point(0, 0), (int)(15.0F * this.density)), DEBUG_PAINT);
  }
  
  private static Path getTrianglePath(Point paramPoint, int paramInt) {
    Point point1 = new Point(paramPoint.x + paramInt, paramPoint.y);
    Point point2 = new Point(paramPoint.x, paramPoint.y + paramInt);
    Path path = new Path();
    path.moveTo(paramPoint.x, paramPoint.y);
    path.lineTo(point1.x, point1.y);
    path.lineTo(point2.x, point2.y);
    return path;
  }
  
  static void setBitmap(ImageView paramImageView, Context paramContext, Bitmap paramBitmap, Picasso.LoadedFrom paramLoadedFrom, boolean paramBoolean1, boolean paramBoolean2) {
    Drawable drawable = paramImageView.getDrawable();
    if (drawable instanceof AnimationDrawable)
      ((AnimationDrawable)drawable).stop(); 
    paramImageView.setImageDrawable((Drawable)new PicassoDrawable(paramContext, paramBitmap, drawable, paramLoadedFrom, paramBoolean1, paramBoolean2));
  }
  
  static void setPlaceholder(ImageView paramImageView, Drawable paramDrawable) {
    paramImageView.setImageDrawable(paramDrawable);
    if (paramImageView.getDrawable() instanceof AnimationDrawable)
      ((AnimationDrawable)paramImageView.getDrawable()).start(); 
  }
  
  public void draw(Canvas paramCanvas) {
    if (!this.animating) {
      super.draw(paramCanvas);
    } else {
      float f = (float)(SystemClock.uptimeMillis() - this.startTimeMillis) / 200.0F;
      if (f >= 1.0F) {
        this.animating = false;
        this.placeholder = null;
        super.draw(paramCanvas);
      } else {
        if (this.placeholder != null)
          this.placeholder.draw(paramCanvas); 
        super.setAlpha((int)(this.alpha * f));
        super.draw(paramCanvas);
        super.setAlpha(this.alpha);
        if (Build.VERSION.SDK_INT <= 10)
          invalidateSelf(); 
      } 
    } 
    if (this.debugging)
      drawDebugIndicator(paramCanvas); 
  }
  
  protected void onBoundsChange(Rect paramRect) {
    if (this.placeholder != null)
      this.placeholder.setBounds(paramRect); 
    super.onBoundsChange(paramRect);
  }
  
  public void setAlpha(int paramInt) {
    this.alpha = paramInt;
    if (this.placeholder != null)
      this.placeholder.setAlpha(paramInt); 
    super.setAlpha(paramInt);
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    if (this.placeholder != null)
      this.placeholder.setColorFilter(paramColorFilter); 
    super.setColorFilter(paramColorFilter);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/squareup/picasso/PicassoDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */