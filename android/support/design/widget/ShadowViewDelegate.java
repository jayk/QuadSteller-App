package android.support.design.widget;

import android.graphics.drawable.Drawable;

interface ShadowViewDelegate {
  float getRadius();
  
  boolean isCompatPaddingEnabled();
  
  void setBackgroundDrawable(Drawable paramDrawable);
  
  void setShadowPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/ShadowViewDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */