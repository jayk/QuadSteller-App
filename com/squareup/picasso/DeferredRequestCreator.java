package com.squareup.picasso;

import android.view.ViewTreeObserver;
import android.widget.ImageView;
import java.lang.ref.WeakReference;

class DeferredRequestCreator implements ViewTreeObserver.OnPreDrawListener {
  Callback callback;
  
  final RequestCreator creator;
  
  final WeakReference<ImageView> target;
  
  DeferredRequestCreator(RequestCreator paramRequestCreator, ImageView paramImageView) {
    this(paramRequestCreator, paramImageView, null);
  }
  
  DeferredRequestCreator(RequestCreator paramRequestCreator, ImageView paramImageView, Callback paramCallback) {
    this.creator = paramRequestCreator;
    this.target = new WeakReference<ImageView>(paramImageView);
    this.callback = paramCallback;
    paramImageView.getViewTreeObserver().addOnPreDrawListener(this);
  }
  
  void cancel() {
    this.callback = null;
    ImageView imageView = this.target.get();
    if (imageView != null) {
      ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
      if (viewTreeObserver.isAlive())
        viewTreeObserver.removeOnPreDrawListener(this); 
    } 
  }
  
  public boolean onPreDraw() {
    ImageView imageView = this.target.get();
    if (imageView != null) {
      ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
      if (viewTreeObserver.isAlive()) {
        int i = imageView.getWidth();
        int j = imageView.getHeight();
        if (i > 0 && j > 0) {
          viewTreeObserver.removeOnPreDrawListener(this);
          this.creator.unfit().resize(i, j).into(imageView, this.callback);
        } 
      } 
    } 
    return true;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/squareup/picasso/DeferredRequestCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */