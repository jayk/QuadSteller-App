package android.support.v7.widget;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RestrictTo;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.widget.ImageView;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class AppCompatImageHelper {
  private final ImageView mView;
  
  public AppCompatImageHelper(ImageView paramImageView) {
    this.mView = paramImageView;
  }
  
  boolean hasOverlappingRendering() {
    Drawable drawable = this.mView.getBackground();
    return !(Build.VERSION.SDK_INT >= 21 && drawable instanceof android.graphics.drawable.RippleDrawable);
  }
  
  public void loadFromAttributes(AttributeSet paramAttributeSet, int paramInt) {
    TintTypedArray tintTypedArray1 = null;
    TintTypedArray tintTypedArray2 = null;
    TintTypedArray tintTypedArray3 = tintTypedArray1;
    try {
      Drawable drawable1 = this.mView.getDrawable();
      Drawable drawable2 = drawable1;
      if (drawable1 == null) {
        tintTypedArray3 = tintTypedArray1;
        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), paramAttributeSet, R.styleable.AppCompatImageView, paramInt, 0);
        tintTypedArray3 = tintTypedArray;
        paramInt = tintTypedArray.getResourceId(R.styleable.AppCompatImageView_srcCompat, -1);
        tintTypedArray2 = tintTypedArray;
        drawable2 = drawable1;
        if (paramInt != -1) {
          tintTypedArray3 = tintTypedArray;
          drawable1 = AppCompatResources.getDrawable(this.mView.getContext(), paramInt);
          tintTypedArray2 = tintTypedArray;
          drawable2 = drawable1;
          if (drawable1 != null) {
            tintTypedArray3 = tintTypedArray;
            this.mView.setImageDrawable(drawable1);
            drawable2 = drawable1;
            tintTypedArray2 = tintTypedArray;
          } 
        } 
      } 
      if (drawable2 != null) {
        tintTypedArray3 = tintTypedArray2;
        DrawableUtils.fixDrawable(drawable2);
      } 
      return;
    } finally {
      if (tintTypedArray3 != null)
        tintTypedArray3.recycle(); 
    } 
  }
  
  public void setImageResource(int paramInt) {
    if (paramInt != 0) {
      Drawable drawable = AppCompatResources.getDrawable(this.mView.getContext(), paramInt);
      if (drawable != null)
        DrawableUtils.fixDrawable(drawable); 
      this.mView.setImageDrawable(drawable);
      return;
    } 
    this.mView.setImageDrawable(null);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/AppCompatImageHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */