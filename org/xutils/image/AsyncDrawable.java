package org.xutils.image;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import java.lang.ref.WeakReference;

public final class AsyncDrawable extends Drawable {
  private Drawable baseDrawable;
  
  private final WeakReference<ImageLoader> imageLoaderReference;
  
  public AsyncDrawable(ImageLoader paramImageLoader, Drawable paramDrawable) {
    if (paramImageLoader == null)
      throw new IllegalArgumentException("imageLoader may not be null"); 
    this.baseDrawable = paramDrawable;
    while (this.baseDrawable instanceof AsyncDrawable)
      this.baseDrawable = ((AsyncDrawable)this.baseDrawable).baseDrawable; 
    this.imageLoaderReference = new WeakReference<ImageLoader>(paramImageLoader);
  }
  
  public void clearColorFilter() {
    if (this.baseDrawable != null)
      this.baseDrawable.clearColorFilter(); 
  }
  
  public void draw(Canvas paramCanvas) {
    if (this.baseDrawable != null)
      this.baseDrawable.draw(paramCanvas); 
  }
  
  protected void finalize() throws Throwable {
    super.finalize();
    ImageLoader imageLoader = getImageLoader();
    if (imageLoader != null)
      imageLoader.cancel(); 
  }
  
  public Drawable getBaseDrawable() {
    return this.baseDrawable;
  }
  
  public int getChangingConfigurations() {
    return (this.baseDrawable == null) ? 0 : this.baseDrawable.getChangingConfigurations();
  }
  
  public Drawable.ConstantState getConstantState() {
    return (this.baseDrawable == null) ? null : this.baseDrawable.getConstantState();
  }
  
  public Drawable getCurrent() {
    return (this.baseDrawable == null) ? null : this.baseDrawable.getCurrent();
  }
  
  public ImageLoader getImageLoader() {
    return this.imageLoaderReference.get();
  }
  
  public int getIntrinsicHeight() {
    return (this.baseDrawable == null) ? 0 : this.baseDrawable.getIntrinsicHeight();
  }
  
  public int getIntrinsicWidth() {
    return (this.baseDrawable == null) ? 0 : this.baseDrawable.getIntrinsicWidth();
  }
  
  public int getMinimumHeight() {
    return (this.baseDrawable == null) ? 0 : this.baseDrawable.getMinimumHeight();
  }
  
  public int getMinimumWidth() {
    return (this.baseDrawable == null) ? 0 : this.baseDrawable.getMinimumWidth();
  }
  
  public int getOpacity() {
    return (this.baseDrawable == null) ? -3 : this.baseDrawable.getOpacity();
  }
  
  public boolean getPadding(Rect paramRect) {
    return (this.baseDrawable != null && this.baseDrawable.getPadding(paramRect));
  }
  
  public int[] getState() {
    return (this.baseDrawable == null) ? null : this.baseDrawable.getState();
  }
  
  public Region getTransparentRegion() {
    return (this.baseDrawable == null) ? null : this.baseDrawable.getTransparentRegion();
  }
  
  public void invalidateSelf() {
    if (this.baseDrawable != null)
      this.baseDrawable.invalidateSelf(); 
  }
  
  public boolean isStateful() {
    return (this.baseDrawable != null && this.baseDrawable.isStateful());
  }
  
  public Drawable mutate() {
    return (this.baseDrawable == null) ? null : this.baseDrawable.mutate();
  }
  
  public void scheduleSelf(Runnable paramRunnable, long paramLong) {
    if (this.baseDrawable != null)
      this.baseDrawable.scheduleSelf(paramRunnable, paramLong); 
  }
  
  public void setAlpha(int paramInt) {
    if (this.baseDrawable != null)
      this.baseDrawable.setAlpha(paramInt); 
  }
  
  public void setBaseDrawable(Drawable paramDrawable) {
    this.baseDrawable = paramDrawable;
  }
  
  public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (this.baseDrawable != null)
      this.baseDrawable.setBounds(paramInt1, paramInt2, paramInt3, paramInt4); 
  }
  
  public void setBounds(Rect paramRect) {
    if (this.baseDrawable != null)
      this.baseDrawable.setBounds(paramRect); 
  }
  
  public void setChangingConfigurations(int paramInt) {
    if (this.baseDrawable != null)
      this.baseDrawable.setChangingConfigurations(paramInt); 
  }
  
  public void setColorFilter(int paramInt, PorterDuff.Mode paramMode) {
    if (this.baseDrawable != null)
      this.baseDrawable.setColorFilter(paramInt, paramMode); 
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    if (this.baseDrawable != null)
      this.baseDrawable.setColorFilter(paramColorFilter); 
  }
  
  public void setDither(boolean paramBoolean) {
    if (this.baseDrawable != null)
      this.baseDrawable.setDither(paramBoolean); 
  }
  
  public void setFilterBitmap(boolean paramBoolean) {
    if (this.baseDrawable != null)
      this.baseDrawable.setFilterBitmap(paramBoolean); 
  }
  
  public boolean setState(int[] paramArrayOfint) {
    return (this.baseDrawable != null && this.baseDrawable.setState(paramArrayOfint));
  }
  
  public boolean setVisible(boolean paramBoolean1, boolean paramBoolean2) {
    return (this.baseDrawable != null && this.baseDrawable.setVisible(paramBoolean1, paramBoolean2));
  }
  
  public void unscheduleSelf(Runnable paramRunnable) {
    if (this.baseDrawable != null)
      this.baseDrawable.unscheduleSelf(paramRunnable); 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/image/AsyncDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */